package webform.api.constant.common;

public class SessionKeyConstant {
	/** トークン */
	public static final String SESSION_TOKEN_KEY = "session_token_info";

	/** 留保パラメータ */
	public static final String SESSION_STACK_PARAMS_KEY = "session_stack_params";

	/** GATE留保パラメーター */
	public static final String GATE_STACK_PARAMS_KEY = "gate_stack_params";

	/** 留保パラメータ入力チェック結果 */
	public static final String SESSION_STACK_PARAMS_VALIDATE_RESULT_KEY = "session_stack_params_validate_result";

	/** クレカ情報 */
	public static final String SESSION_CREDIT_CARD_INFO_KEY = "session_credit_card_info";

	/** クレカ疑似番号 */
	public static final String SESSION_CREDIT_DUMMY_NUMBER_KEY = "session_credit_dummy_number";

	/** 申込導線区分 */
	public static final String SESSION_ORDER_TYPE = "session_order_type";

	/** 導線アクセス日時 */
	public static final String SESSION_ACCESS_DATE_TIME = "session_access_date_time";

	/** OSB 顧客情報 */
	public static final String OSB_CUSTOMER_INFO = "session_osb_customer_info";
}
