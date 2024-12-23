package webform.api.dto.text_file_writer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * テキストファイル書込みリクエスト.
 *
 */
public class WriteFileRequestDto implements Serializable {

	// ----- テキストファイル書込み API リクエスト パラメータ -----
	/** 出力先ファイル情報リスト. */
	protected List<TextFileInfo> textFileInfoList = new ArrayList<TextFileInfo>();

	// ----- setter、getter -----
	public List<TextFileInfo> getTextFileInfoList() {
		return textFileInfoList;
	}

}
