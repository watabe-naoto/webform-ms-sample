package webform.api.constant.business_address;

/**
 * バリデーション定数
 */
public class ValidationConstant {
	/** 半角数字もしくは空 */
	public static final String NUMERIC = "^\\d*$";
	/** 半角英字もしくは空 */
	public static final String ALPHABETIC = "^[a-zA-Z]*$";
	/** 半角英大文字もしくは空 */
	public static final String UPPER_ALPHABETIC = "^[A-Z]*$";
	/** 半角英数字もしくは空 */
	public static final String ALPHANUMERIC = "^[\\da-zA-Z]*$";
	/** 全角文字もしくは空 */
	public static final String ZENKAKU = "^[^\\x01-\\x7E]*$";
	/** 全角カナ全角スペースもしくは空 */
	public static final String ZENKAKU_KANA = "^[　ァ-ヴー]*$";

	/** 請求番号もしくは空 */
	public static final String BILLING_NO = "^$|^R[\\d]{9}$";
	/** 一括用請求番号もしくは空 */
	public static final String IKKATSU_BILLING_NO = "^$|^R7[\\d]{8}$";

	/** 固定電話番号もしくは空 */
	public static final String TEL_NO = "^$|^0[1-9]{2}\\d{7}$";
	/** 携帯電話番号もしくは空 */
	public static final String CELL_NO = "^$|^0[5789]0\\d{4}\\d{4}$";
	/**  固定電話番号、携帯電話番号もしくは空 */
	public static final String ANY_PHONE_NO = "^$|^0[1-9]{2}\\d{7}$|^0[5789]0\\d{4}\\d{4}$";

	/**  メールアドレスもしくは空 */
	public static final String MAIL_ADDRESS = "^$|^([a-zA-Z0-9][a-zA-Z_0-9-.]*@(([a-zA-Z_0-9]|[a-zA-Z_0-9][a-zA-Z_0-9-]*[a-zA-Z_0-9])\\.)+[a-zA-Z0-9]+)$";


}
