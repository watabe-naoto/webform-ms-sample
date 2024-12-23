package webform.api.service.billing_extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import webform.api.constant.billing_extension.BillingExtensionConstant;
import webform.api.dto.billing_extension.VBillingExtensionMessage;
import webform.api.exception.ApiException;

/**
 * 支払期限延伸フォーム用メッセージ情報検索Serviceクラス
 *
 */
public class BillingExtensionMessageService {
	/** Log */
	private static final Log log = LogFactory.getLog(BillingExtensionMessageService.class);

	/**
	 * コンストラクタ
	 */
	public BillingExtensionMessageService() {
	}

	/**
	 * 支払期限延伸フォーム用メッセージ情報検索
	 *
	 * 表示開始終了期間内から表示する情報を取得する。
	 *
	 * @return Map<String, String> 支払期限延伸フォーム用メッセージ(Key=メッセージ項目コード,value=表示内容)
	 */
	public Map<String, String> getBillingExtensionMessage() {
		log.info("BillingExtensionMessage getBillingExtensionMessage Start.");

		// 検索条件
		// WHERE display_start_datetime <= :now and display_stop_datetime >= :now

		// MS実行
		List<VBillingExtensionMessage> resultList = new ArrayList<VBillingExtensionMessage>();
		
		// 支払期限延伸フォーム用メッセージ情報チェック
		checkBillingExtensionMessageList(resultList);
		
		Map<String, String> res = resultList.stream().collect(
				Collectors.toMap(VBillingExtensionMessage::getMessageItemCode, e -> StringUtils.defaultString(e.getContents()), (s1, s2) -> s1.concat(s2)));
		log.info("BillingExtensionMessage getBillingExtensionMessage End.");
		return res;
	}
	
	/**
	 * 支払期限延伸フォーム用メッセージチェック
	 *
	 * @param resultList
	 */
	private void checkBillingExtensionMessageList(List<VBillingExtensionMessage> resultList) {
		log.info("BillingExtensionMessageService#checkBillingExtensionMessageList Start.");
		Map<String, Object> map = new HashMap<String, Object>();
		// パラメータチェック
		if (resultList == null || resultList.isEmpty()) {
			log.info("BillingExtensionMessageService#checkBillingExtensionMessageList 取得したリストがありません.");
			map.put("code", ApiException.CODE.systemError.toString());
			map.put("title", "システムエラー");
			map.put("message", "取得したリストがありません");
			throw new ApiException(map);
		}
		
		Set<String> duplicateList = new HashSet<String>(); // 確認用
		for (VBillingExtensionMessage billingExtensionMessage : resultList) {
			if (!duplicateList.add(billingExtensionMessage.getMessageItemCode())) {
				// キー名が重複しているためエラー扱い
				log.info("BillingExtensionMessageService#checkBillingExtensionMessageList キー名が重複している.");
				map.put("code", ApiException.CODE.systemError.toString());
				map.put("title", "システムエラー");
				map.put("message", "キー名が重複しています。key=" + billingExtensionMessage.getMessageItemCode());
				throw new ApiException(map);
			}
		}

		// 存在チェック
		for (String key : Arrays.asList(BillingExtensionConstant.REQUIRED_ITEM_LIST)) {
			if (!duplicateList.contains(key)) {
				log.info("BillingExtensionMessageService#checkBillingExtensionMessageList 取得したキー名がDBに存在しない.");
				map.put("code", ApiException.CODE.systemError.toString());
				map.put("title", "システムエラー");
				map.put("message", "取得したキー名がDBに存在しません。key=" + key);
				throw new ApiException(map);
			}
		}
		
		log.info("BillingExtensionMessageService#checkBillingExtensionMessageList End.");
	}
}
