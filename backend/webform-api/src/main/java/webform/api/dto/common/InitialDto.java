package webform.api.dto.common;

import java.util.HashMap;
import java.util.Map;

/**
 * [導線共通処理]クライアントとビジネスロジック間のデータ運搬オブジェクト用パッケージ
 * @see reqform.api.resource.common.InitialResource
 */
public class InitialDto {

	/** Gateパラメータ */
	private HashMap<String, Object> gateParams;
	/** Gateパラメータに対するチェックエラー情報 */
	private Object InitialCheckError;
	/** Token文字列(URL-safe) */
	private String token;
	/** Systemタイムスタンプ（文字列） */
	private String systemtimestamp;
	/** システム利用可能判定 */
	private boolean result;
	/** システムメンテナンス画面のメッセージ */
	private String message;
	/** 支払い期限延伸フォーム CMS化 */
	private Map<String, String> billingExtensionMessage;


	public HashMap<String, Object> getGateParams() {
		return gateParams;
	}

	public void setGateParams(HashMap<String, Object> gateParams) {
		this.gateParams = gateParams;
	}

	public Object getInitialCheckError() {
		return InitialCheckError;
	}

	public void setInitialCheckError(Object initialCheckError) {
		InitialCheckError = initialCheckError;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSystemtimestamp() {
		return systemtimestamp;
	}

	public void setSystemtimestamp(String systemtimestamp) {
		this.systemtimestamp = systemtimestamp;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, String> getBillingExtensionMessage() {
		return billingExtensionMessage;
	}

	public void setBillingExtensionMessage(Map<String, String> billingExtensionMessageList) {
		this.billingExtensionMessage = billingExtensionMessageList;
	}

}
