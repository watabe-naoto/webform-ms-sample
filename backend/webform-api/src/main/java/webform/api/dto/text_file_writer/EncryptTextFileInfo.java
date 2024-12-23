package webform.api.dto.text_file_writer;

import java.io.Serializable;

/**
 * テキストファイル情報（暗号化有無指定あり）.
 *
 */
public class EncryptTextFileInfo implements Serializable {

	/** charset名:JIS(ISO2022JP)。※一部文字化けするコードを変換する。 */
	public static final String CHARSET_NAME_JIS= "ISO2022JP";

	/** charset名:Shift-JIS。※一部文字化けするコードを変換する。 */
	public static final String CHARSET_NAME_SJIS= "SJIS";

	/** 暗号化コマンド名:FOCUS向け復号化コマンド. */
	public static final String FOCUS_COMAND = "focus";

	/** 暗号化コマンド名:Docomo向け復号化コマンド. */
	public static final String DOCOMO_COMAND = "docomo";

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

	/**
	 * 暗号化コマンド名.
	 * （null or 空文字:暗号化なし、"focus"：FOCUS向け復号化コマンド、"docomo":Docomo向け復号化コマンド）
	 */
	private String encryptionCommandName;

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

	public String getEncryptionCommandName() {
		return encryptionCommandName;
	}

	public void setEncryptionCommandName(String encryptionCommandName) {
		this.encryptionCommandName = encryptionCommandName;
	}

}
