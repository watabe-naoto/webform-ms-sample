package webform.api.dto.text_file_writer;

import java.io.Serializable;

/**
 * テキストファイル情報.
 *
 */
public class TextFileInfo implements Serializable {
	/** 出力先ファイルパス. */
	private String filePath;

	/** バックアップ先ファイルパス. */
	private String backupFilePath;

	/** 出力ファイル名. */
	private String fileName;

	/** 出力ファイルのcharset名. */
	private String charsetName;

	/** 出力テキスト. */
	private String text;


	// ----- setter、getter -----
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBackupFilePath() {
		return backupFilePath;
	}

	public void setBackupFilePath(String backupFilePath) {
		this.backupFilePath = backupFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
