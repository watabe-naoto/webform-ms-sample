package webform.ms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webform.ms.constant.CommonConstant;
import webform.ms.constant.TextFileWriterConstant;
import webform.ms.exception.AppException;
import webform.ms.grpc.text_file_writer.EncryptTextFileInfo;
import webform.ms.grpc.text_file_writer.TextFileInfo;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileReply;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileRequest;
import webform.ms.grpc.text_file_writer.WriteFileReply;
import webform.ms.grpc.text_file_writer.WriteFileRequest;

/**
 * テキストファイル書込みファクトリー.
 * パラメータで指定された内容のテキストファイルを生成する。
 * 指定可能なパラメータは、以下の項目。
 * ・出力先ファイルパス
 * ・バックアップ先ファイルパス
 * ・出力ファイル名
 * ・出力ファイルのcharset名
 * ・出力テキスト
 * ・暗号化
 *
 */
class TextFileWriterFactory {
	/** ロガー. */
	private final Logger logger = LoggerFactory.getLogger(TextFileWriterFactory.class);

	/** プロパティ. */
	private static final ResourceBundle RB = ResourceBundle.getBundle(TextFileWriterConstant.TEXT_FILE_WRITER_PROPERTIES_FILE);

	/** テキストファイル書込みのデフォルトのファイルパス. */
	private String filepath = null;

	/**
	 * このクラスのインスタンスを返す.
	 *
	 * @return
	 */
	static TextFileWriterFactory getInstance() {
		return new TextFileWriterFactory();
	}

	/**
	 * コンストラクタ.
	 */
	private TextFileWriterFactory() {
		logger.info("テキストファイル書込みのデフォルト情報");
		this.filepath = RB.getString(TextFileWriterConstant.TEXT_FILE_WRITER_FILEPATH);
		logger.info("filepath=" + this.filepath);
	}

	/**
	 * ファイル書込みリクエストパラメータチェック.
	 *
	 * @param requestBean
	 */
	private void checkWriteFileParam(WriteFileRequest requestBean) {
		List<String> errorStr = new ArrayList<String>();

		// 出力先ファイル情報リスト：必須
		if (requestBean.getTextFileInfoList() == null || requestBean.getTextFileInfoList().size() == 0) {
			logger.debug("validation error: textFileInfoList({})", (requestBean.getTextFileInfoList() == null ? null : ""));
			errorStr.add("textFileInfoList");
		} else {
			for (int count = 0; count < requestBean.getTextFileInfoList().size(); count++) {
				TextFileInfo textFileInfo = requestBean.getTextFileInfoList().get(count);
				// 出力先ファイルパス：必須、半角英数、256文字
				if (StringUtils.isEmpty(textFileInfo.getFilePath())
						|| textFileInfo.getFilePath().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].filePath({})", textFileInfo.getFilePath());
					errorStr.add("textFileInfoList[" + count + "].filePath");
				}

				// バックアップ先ファイルパス：任意、半角英数、256文字
				if (StringUtils.isNotEmpty(textFileInfo.getBackupFilePath())
						&& textFileInfo.getBackupFilePath().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].backupFilePath({})", textFileInfo.getBackupFilePath());
					errorStr.add("textFileInfoList[" + count + "].backupFilePath");
				}

				// ファイル名：必須、半角英数、256桁
				if (StringUtils.isEmpty(textFileInfo.getFileName())
						|| textFileInfo.getFileName().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].fileName({})", textFileInfo.getFileName());
					errorStr.add("textFileInfoList[" + count + "].fileName");
				}

				// 出力ファイルのcharset名：任意、半角英数、30文字
				if (StringUtils.isNotEmpty(textFileInfo.getCharsetName())
						&& textFileInfo.getCharsetName().length() > 30) {
					logger.debug("validation error: textFileInfoList[" + count + "].charsetName({})", textFileInfo.getCharsetName());
					errorStr.add("textFileInfoList[" + count + "].charsetName");
				}

				// 出力テキスト：必須
				if (StringUtils.isEmpty(textFileInfo.getText())) {
					logger.debug("validation error: textFileInfoList[" + count + "].text({})", textFileInfo.getText());
					errorStr.add("textFileInfoList[" + count + "].text");
				}
			}
		}

		// チェックエラーがある場合は例外をスローする
		if (errorStr.size() > 0) {
			throw new AppException(CommonConstant.PARAM_ERR, new String[] { String.join(",", errorStr) });
		}
	}

	/**
	 * ファイル書込みリクエスト（暗号化有無指定あり）パラメータチェック.
	 *
	 * @param requestBean
	 */
	private void checkWriteEncryptionFileParam(WriteEncryptionFileRequest requestBean) {
		List<String> errorStr = new ArrayList<String>();

		// 出力先ファイル情報リスト：必須
		if (requestBean.getTextFileInfoList() == null || requestBean.getTextFileInfoList().size() == 0) {
			logger.debug("validation error: textFileInfoList({})", (requestBean.getTextFileInfoList() == null ? null : ""));
			errorStr.add("textFileInfoList");
		} else {
			for (int count = 0; count < requestBean.getTextFileInfoList().size(); count++) {
				EncryptTextFileInfo textFileInfo = requestBean.getTextFileInfoList().get(count);
				// 出力先ファイルパス：必須、256文字　※半角英数と一部の半角記号のみ可能だがチェックしない
				if (StringUtils.isEmpty(textFileInfo.getFilePath())
						|| textFileInfo.getFilePath().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].filePath({})", textFileInfo.getFilePath());
					errorStr.add("textFileInfoList[" + count + "].filePath");
				}

				// バックアップ先ファイルパス：任意、256文字　※半角英数と一部の半角記号のみ可能だがチェックしない
				if (StringUtils.isNotEmpty(textFileInfo.getBackupFilePath())
						&& textFileInfo.getBackupFilePath().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].backupFilePath({})", textFileInfo.getBackupFilePath());
					errorStr.add("textFileInfoList[" + count + "].backupFilePath");
				}

				// ファイル名：必須、256桁　※半角英数と一部の半角記号のみ可能だがチェックしない
				if (StringUtils.isEmpty(textFileInfo.getFileName())
						|| textFileInfo.getFileName().length() > 256) {
					logger.debug("validation error: textFileInfoList[" + count + "].fileName({})", textFileInfo.getFileName());
					errorStr.add("textFileInfoList[" + count + "].fileName");
				}

				// 出力ファイルのcharset名：任意、半角英数、30文字
				if (StringUtils.isNotEmpty(textFileInfo.getCharsetName())
						&& textFileInfo.getCharsetName().length() > 30) {
					logger.debug("validation error: textFileInfoList[" + count + "].charsetName({})", textFileInfo.getCharsetName());
					errorStr.add("textFileInfoList[" + count + "].charsetName");
				}

				// 出力テキスト：必須
				if (StringUtils.isEmpty(textFileInfo.getText())) {
					logger.debug("validation error: textFileInfoList[" + count + "].text({})", textFileInfo.getText());
					errorStr.add("textFileInfoList[" + count + "].text");
				}

				// 出力テキスト：任意、"focus" or "docomo"
				if (StringUtils.isNotEmpty(textFileInfo.getEncryptionCommandName())
						&& !textFileInfo.getEncryptionCommandName().matches("^(focus|docomo)$")) {
					logger.debug("validation error: textFileInfoList[" + count + "].encryptionCommandName({})", textFileInfo.getEncryptionCommandName());
					errorStr.add("textFileInfoList[" + count + "].encryptionCommandName");
				}
			}
		}

		// チェックエラーがある場合は例外をスローする
		if (errorStr.size() > 0) {
			throw new AppException(CommonConstant.PARAM_ERR, new String[] { String.join(",", errorStr) });
		}
	}

	// ----- publicメソッド -----
	/**
	 * ファイル書込みAPI呼び出し.
	 *
	 * @param requestBean
	 * @return
	 */
	protected WriteFileReply writeFile(WriteFileRequest requestBean) {
		logger.info("writeFile start");
		logger.debug("requestBean=" + requestBean);
		WriteFileReply result = null;
		try {
			// パラメータをチェックする。
			this.checkWriteFileParam(requestBean);

			// ファイルを生成する。
			for (webform.ms.grpc.text_file_writer.TextFileInfo textFileInfo : requestBean.getTextFileInfoList()) {
				TextFileService textFileService = new TextFileService(textFileInfo);
				textFileService.writeTextFile();
			}

			// フィールド画像認識リクエストを送信する。
			result = WriteFileReply.newBuilder().setResultCode("0").build();

		} finally {
			logger.info("writeFile end");
		}
		return result;
	}


	/**
	 * ファイル書込み（暗号化有無指定あり）API呼び出し.
	 *
	 * @param requestBean
	 * @return
	 */
	protected WriteEncryptionFileReply writeEncryptionFile(WriteEncryptionFileRequest requestBean) {
		logger.info("writeFile start");
		logger.debug("requestBean=" + requestBean);
		WriteEncryptionFileReply result = null;
		try {
			// パラメータをチェックする。
			this.checkWriteEncryptionFileParam(requestBean);

			// ファイルを生成する。
			for (webform.ms.grpc.text_file_writer.EncryptTextFileInfo textFileInfo : requestBean.getTextFileInfoList()) {
				TextFileService textFileService = new TextFileService(textFileInfo);
				textFileService.writeEncryptionTextFile();
			}

			// フィールド画像認識リクエストを送信する。
			result = WriteEncryptionFileReply.newBuilder().setResultCode("0").build();

		} finally {
			logger.info("writeFile end");
		}
		return result;
	}

}