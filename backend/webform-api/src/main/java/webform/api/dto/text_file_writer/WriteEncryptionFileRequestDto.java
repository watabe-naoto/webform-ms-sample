package webform.api.dto.text_file_writer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * テキストファイル書込み（暗号化有無指定あり）リクエスト.
 *
 */
public class WriteEncryptionFileRequestDto implements Serializable {

	// ----- テキストファイル書込み API リクエスト パラメータ -----
	/** 出力先ファイル情報リスト. */
	protected List<EncryptTextFileInfo> textFileInfoList = new ArrayList<EncryptTextFileInfo>();

	// ----- setter、getter -----
	public List<EncryptTextFileInfo> getTextFileInfoList() {
		return textFileInfoList;
	}

}
