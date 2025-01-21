package webform.api.resource.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.databind.ObjectMapper;

import webform.api.constant.common.ConductCodeConstant;
import webform.api.constant.common.SessionKeyConstant;
import webform.api.dto.common.CheckAvailableRes;
import webform.api.dto.common.InitialDto;
import webform.api.exception.ApiException;
import webform.api.service.billing_extension.BillingExtensionMessageService;
import webform.api.service.common.AvailableIpListService;
import webform.api.service.common.GlobalAddressService;
import webform.api.service.common.TokenService;
import webform.api.service.external_system.ExternalsysteminfoService;
import webform.api.service.external_system.ExternalsystemrelationinfoService;
import webform.api.util.SecureUtil;


@RequestScoped
@Path("init")
public class InitResource {
	/** Log */
	private final Logger logger = LogManager.getLogger(InitResource.class);

	private static final String ORDER_TYPE = "order_type";

	@Context
	private HttpServletRequest httpServletRequest;

	@Inject
	ExternalsystemrelationinfoService relService;
	@Inject
	ExternalsysteminfoService service;

	/**
	 * グローバルIPアドレス取得サービス.
	 */
	@Inject
	GlobalAddressService globalAddressService;

	/**
	 * メンテナンス中に利用可能なクライアントGIPリストサービス.
	 */
	@Inject
	AvailableIpListService availableIpListService;

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public InitialDto initProc(String conduct) {

		// パラメタチェック
		this.checkParam(conduct);

		// レスポンスオブジェクト作成
		InitialDto resDto = new InitialDto();

		// セッションからGateパラメータを取り出す。
		resDto.setGateParams(this.pickGateParams());
		// セッションからGateパラメータに対するチェックエラー情報を取り出す。
		resDto.setInitialCheckError(this.httpServletRequest.getSession()
				.getAttribute(SessionKeyConstant.SESSION_STACK_PARAMS_VALIDATE_RESULT_KEY));

		// セッションを再取得する。
		HttpSession session = this.resessions();
		// キーを張り替えて留保パラメータをセッション情報にセット
		session.setAttribute(SessionKeyConstant.GATE_STACK_PARAMS_KEY, resDto.getGateParams());

		// Token発行しセッションに登録する。
		resDto.setToken(this.generateToken());

		// Systemタイムスタンプ（文字列）生成
		resDto.setSystemtimestamp(String.valueOf(new Date().getTime()));

		// メンテナンス中に利用可能なクライアントGIPを判定する。
		String gip = this.globalAddressService.getIPaddress();
		logger.info("クライアントGIP=" + gip);
		if (!this.availableIpListService.isAvailableIp(gip)) {
			// メンテナンス中に利用不可のクライアントGIPなので、メンテナンス中判定を行う。
			// 外部システム利用可能判定
			CheckAvailableRes checkAvailableRes = this.checkAvailable(conduct);
			resDto.setResult(checkAvailableRes.result);
			resDto.setMessage(checkAvailableRes.message);
			if (!checkAvailableRes.result) {
				// 導線閉塞中の為、後続処理不要
				return resDto;
			}
		} else {
			// 外部システム利用可能判定に利用可能を設定
			resDto.setResult(true);
			resDto.setMessage("");
		}

		// 支払期限延伸フォーム用メッセージ情報検索
		// TODO:対象導線が支払い延伸フォームとは関係ないため、一旦削除。
//		if (conduct.equals(ConductCodeConstant.CONDUCT.EXTENSION_PAYMENT.getKey())) {
//			Map<String, String> billingExtensionMessageList = this.getBillingExtensionMessage();
//
//			resDto.setBillingExtensionMessage(billingExtensionMessageList);
//		}

		logger.info("InitialResource#initProc End.");
		return resDto;

		//		InitialDto dto = new InitialDto();
		//
		////		dto.setGateParams(new HashMap<String, Object>());
		//		dto.setGateParams(null);
		//
		//		dto.setInitialCheckError(null);
		//
		//		// Tokenは仮で適当な値を返却
		//		dto.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZXFmb3JtIiwiYXVkIjoiIiwidXNlcl90b2tlbiI6IjU0MDcyOWQwMzM2NjZhMGI4ZDA3YTNhNTQ2MmIzMmZmIiwiZXhwIjoxNzMwNDQ5MzIzfQ.nV4gOchEtKj3cZ2DOasw5d70LB-wpiduYvhegGO1axM");
		//		dto.setSystemtimestamp(String.valueOf(new Date().getTime()));
		//
		//		dto.setResult(true);
		//		dto.setMessage(null);
		//
		////		dto.setBillingExtensionMessage(new HashMap<String, String>());
		//		dto.setBillingExtensionMessage(null);
		//
		//		return dto;
	}

	/**
	 * パラメタチェック
	 *
	 * @param conduct
	 */
	private void checkParam(String conduct) {
		logger.info("InitialResource#checkParam Start.");
		// パラメータチェック
		if (ConductCodeConstant.CONDUCT.get(conduct) == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", ApiException.CODE.PARAM_ERR.toString());
			map.put("title", "パラメータエラー");
			map.put("message", "conductが取得出来ません。conduct=" + conduct);
			throw new ApiException(map);
		}
		logger.info("InitialResource#checkParam End.");
	}

	/**
	 * GateResourceでセッションにセットした入力パラメータを取り出す。
	 * @return
	 */
	private HashMap<String, Object> pickGateParams() {
		logger.info("InitialResource#pickGateParams Start.");
		HttpSession primerSession = this.httpServletRequest.getSession();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> stackParams = (HashMap<String, Object>) primerSession
				.getAttribute(SessionKeyConstant.SESSION_STACK_PARAMS_KEY);
		if (logger.isDebugEnabled()) {
			try {
				logger.debug(
						"GateResourceでセッションにセットした入力パラメータの内容確認 : " + new ObjectMapper().writeValueAsString(stackParams));
			} catch (Exception e) {
				logger.debug("", e);
			}
		}
		logger.info("InitialResource#pickGateParams End.");
		return stackParams;
	}

	/**
	 * セッションを張り直す。（注意 : 事前に引き継ぐべき値は他に退避すること）
	 * @return
	 */
	private HttpSession resessions() {
		logger.info("InitialResource#resessions Start.");
		// セッションを破棄する
		this.httpServletRequest.getSession().invalidate();

		// セッション取得
		HttpSession session = this.httpServletRequest.getSession();

		// ヘッダー導線情報取得
		String orderTypeAccessDateTime = this.httpServletRequest.getHeader(ORDER_TYPE);
		// 申込導線区分
		String orderType = StringUtils.left(orderTypeAccessDateTime, 2);
		session.setAttribute(SessionKeyConstant.SESSION_ORDER_TYPE, orderType);
		// 導線アクセス日時
		String accessDateTime = StringUtils.mid(orderTypeAccessDateTime, 2,
				StringUtils.length(orderTypeAccessDateTime));
		session.setAttribute(SessionKeyConstant.SESSION_ACCESS_DATE_TIME, accessDateTime);

		logger.info("InitialResource#resessions End.");
		return session;
	}


	/**
	 * Token発行しセッションに登録する。
	 *
	 * @return Token文字列(URL-safe)
	 */
	private String generateToken() {
		logger.info("InitialResource#generateToken Start.");

		String token = SecureUtil.getRandomToken();
		this.httpServletRequest.getSession().setAttribute(SessionKeyConstant.SESSION_TOKEN_KEY, token);
		String UrlSafeFormatToken = TokenService.createToken(token);

		logger.info("InitialResource#generateToken End.");
		return UrlSafeFormatToken;
	}

	/**
	 * 外部システム利用可能判定
	 *
	 * @param conduct
	 *            導線
	 */
	private CheckAvailableRes checkAvailable(String conduct) {
		logger.info("InitialResource#checkAvailable Start.");

		// 導線に応じたシステムIDの設定
		int[] systemIds = relService.selectSystemId(ConductCodeConstant.CONDUCT.get(conduct).getValue());

		CheckAvailableRes res = new CheckAvailableRes();
		if (systemIds.length == 0) {
			res.result = true;
		}
		// 稼働状況を確認し、メンテナンス中ならばメンテナンス情報のメッセージを取得
		String message = service.isSystemAvailable(systemIds);
		// メッセージがある時は利用不可
		res.result = StringUtils.isEmpty(message);
		res.message = message;

		logger.info("InitialResource#checkAvailable End.");
		return res;
	}

	/**
	 * 支払期限延伸フォーム用メッセージ情報検索
	 *
	 * @param
	 * @return 取得情報
	 */
	private Map<String, String> getBillingExtensionMessage() {
		logger.info("InitialResource#getBillingExtensionMessage Start.");
		// 検索実行
		BillingExtensionMessageService service = new BillingExtensionMessageService();

		logger.info("InitialResource#getBillingExtensionMessage End.");
		return service.getBillingExtensionMessage();
	}

}
