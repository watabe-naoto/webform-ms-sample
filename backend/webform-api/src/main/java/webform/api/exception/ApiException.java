package webform.api.exception;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * API例外クラス
 *
 */
public class ApiException extends WebApplicationException {
	/**　Serial Version UID */
	private static final long serialVersionUID = -5642272321913345947L;

	/**
	 * コード定義
	 *
	 */
	public enum CODE {
		systemError,
		OPT_ERR,
		PARAM_ERR, // パラメータエラー
		INS_ERR, // 登録エラー
		UPD_ERR,
		PASSWORD_ERR,
		NO_MATCH_ERR,
		LOCKOUT_ERR,
		AUTH_ERR,
		MKDIR_ERR,
		COPYFILE_ERR,

		// NB連携のエラー
		NB_ERR,
		NB_SYS_ERR,
	}

	private Map<String, Object> messageMap;

	public Map<String, Object> getMessageMap() {
		return messageMap;
	}

	/**
	 * コンストラクタ
	 * @deprecated TODO 2020/01/24 非公開にします。理由：messageもcauseも無しのため
	 */
	public ApiException() {
		super(Response.status(Status.INTERNAL_SERVER_ERROR).build());
	}
	/**
	 * コンストラクタ
	 *
	 * @param message メッセージ
	 */
	public ApiException(Map<String, Object> message) {
		this(Status.INTERNAL_SERVER_ERROR, message);
	}
	/**
	 * コンストラクタ
	 *
	 * @param status ステータス
	 * @param message メッセージ
	 */
	public ApiException(Status status, Map<String, Object> message) {
		this(status.getStatusCode(), message);
	}
	/**
	 * コンストラクタ
	 *
	 * @param code コード
	 * @param message メッセージ
	 */
	public ApiException(int code, Map<String, Object> message) {
		super(Response.status(code).entity(message).build());
		messageMap = message;
	}

	/**
	 * コンストラクタ
	 *
	 * @param message メッセージ
	 */
	public ApiException(Map<String, Object> message, Throwable cause) {
		this(Status.INTERNAL_SERVER_ERROR, message, cause);
	}
	/**
	 * コンストラクタ
	 *
	 * @param status ステータス
	 * @param message メッセージ
	 */
	public ApiException(Status status, Map<String, Object> message, Throwable cause) {
		this(status.getStatusCode(), message, cause);
	}
	/**
	 * コンストラクタ
	 *
	 * @param code コード
	 * @param message メッセージ
	 */
	public ApiException(int code, Map<String, Object> message, Throwable cause) {
		super(cause, Response.status(code).entity(message).build());
		messageMap = message;
	}
}