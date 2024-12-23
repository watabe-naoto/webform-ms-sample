package webform.ms.constant;

public class TextFileWriterConstant {
	/** システム名称 : テキストファイル書込み */
	public static final String CONNECTION_SYSTEM_NAME = "テキストファイル書込み";

	/** プロパティファイル名 */
	public static final String TEXT_FILE_WRITER_PROPERTIES_FILE = "TextFileWriter";

	/** テキストファイル書込みファイルパス : デフォルトのファイルパスを定義 */
	public static final String TEXT_FILE_WRITER_FILEPATH = "text_file_writer.filepath";


	/** charset名:JIS(ISO2022JP)。※一部文字化けするコードを変換する。 */
	public static final String CHARSET_NAME_JIS= "ISO2022JP";

	/** charset名:Shift-JIS。※一部文字化けするコードを変換する。 */
	public static final String CHARSET_NAME_SJIS= "SJIS";

	/** 暗号化コマンド名:FOCUS向け復号化コマンド. */
	public static final String FOCUS_COMAND = "focus";

	/** 暗号化コマンド名:Docomo向け復号化コマンド. */
	public static final String DOCOMO_COMAND = "docomo";


	/** 復号化コマンド */
	public static final String DECRYPT_COMMAND = "decrypt.command";

	/** 暗号化コマンド */
	public static final String ENCRYPT_COMMAND = "encrypt.command";

	/** 暗号化用のテンポラリフォルダパス */
	public static final String ENCRYPT_TMP_PATH = "encrypt.tmp.path";

	/** サーバ間のファイルコピーコマンド */
	public static final String COPY_REMOTE_FILE_COMMAND = "copy.remote.file.command";

	/** サーバ間のファイル削除コマンド */
	public static final String DELETE_REMOTE_FILE_COMMAND = "delete.remote.file.command";

}
