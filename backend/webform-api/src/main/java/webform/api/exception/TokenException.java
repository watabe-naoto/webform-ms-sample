package webform.api.exception;

public class TokenException extends RuntimeException {
	/**　Serial Version UID */
	private static final long serialVersionUID = -6813181732523224146L;
	/**
	 * コンストラクタ
	 */
	public TokenException() {
		super();
	}
	/**
	 * コンストラクタ
	 *
	 * @param ex 例外インスタンス
	 */
	public TokenException(Exception ex) {
		super(ex);
	}
	/**
	 * コンストラクタ
	 *
	 * @param msg 例外メッセージ
	 */
	public TokenException(final String msg) {
		super(msg);
	}
}
