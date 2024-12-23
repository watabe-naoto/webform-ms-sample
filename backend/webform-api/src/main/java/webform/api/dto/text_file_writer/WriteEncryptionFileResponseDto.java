package webform.api.dto.text_file_writer;

import java.io.Serializable;

/**
 * テキストファイル書込み（暗号化有無指定あり）レスポンス.
 *
 */
public class WriteEncryptionFileResponseDto implements Serializable {

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
