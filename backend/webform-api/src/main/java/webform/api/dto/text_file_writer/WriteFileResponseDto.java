package webform.api.dto.text_file_writer;

import java.io.Serializable;

/**
 * テキストファイル書込みレスポンス.
 *
 */
public class WriteFileResponseDto implements Serializable {

	// ----- テキストファイル書込み API レスポンス パラメータ -----
	/** 結果コード. */
	protected String resultCode;

	// ----- setter、getter -----
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

}
