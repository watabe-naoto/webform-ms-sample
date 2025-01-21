package webform.ms.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webform.ms.constant.CommonConstant;
import webform.ms.constant.TextFileWriterConstant;
import webform.ms.exception.AppException;

/**
 * テキストファイルの読書き用サービス。
 *
 */
public class TextFileService {
	/** ロガー */
	private final Logger logger = LoggerFactory.getLogger(TextFileService.class);

	/** プロパティ */
	private static final ResourceBundle RB = ResourceBundle.getBundle(TextFileWriterConstant.TEXT_FILE_WRITER_PROPERTIES_FILE);

	/** 読書き対象のテキストファイル情報。 */
	private webform.ms.grpc.text_file_writer.TextFileInfo textFileInfo = null;

	/** 暗号化コマンド名。 */
	private String encryptionCommandName = null;

	/**
	 * UTF-8をSJISに変換して文字化けするコードを、別の文字コードに変換する。
	 * @param str
	 * @return
	 */
	private String replaceSjisConvertErrorCode(String str) {
		if (StringUtils.isEmpty(str)) {
			// 空文字の場合は無変換のまま返却する。
			return str;
		}

		char[] cArray = str.toCharArray();
		for (int i = 0; i < cArray.length; i++) {
			char c = cArray[i];
			// ―
			if (c == '\u2015') {
				cArray[i] = '\u2014';
				continue;
			}
			// ～
			if (c == '\uFF5E') {
				cArray[i] = '\u301C';
				continue;
			}
			// ∥
			if (c == '\u2225') {
				cArray[i] = '\u2016';
				continue;
			}
			// －
			if (c == '\uFF0D') {
				cArray[i] = '\u2212';
				continue;
			}
			// ￠
			if (c == '\uFFE0') {
				cArray[i] = '\u00A2';
				continue;
			}
			// ￡
			if (c == '\uFFE1') {
				cArray[i] = '\u00A3';
				continue;
			}
			// ￢
			if (c == '\uFFE2') {
				cArray[i] = '\u00AC';
				continue;
			}
		}

		return new String(cArray);
	}

	/**
	 * UTF-8をJISに変換して文字化けするコードを、別の文字コードに変換する。
	 * @param str
	 * @return
	 */
	private String replaceJisConvertErrorCode(String str) {
		if (StringUtils.isEmpty(str)) {
			// 空文字の場合は無変換のまま返却する。
			return str;
		}

		char[] cArray = str.toCharArray();
		for (int i = 0; i < cArray.length; i++) {
			char c = cArray[i];
			// ―
			if (c == '\u2015') {
				cArray[i] = '\u2014';
				continue;
			}
			// ～
			if (c == '\uFF5E') {
				cArray[i] = '\u301C';
				continue;
			}
			// ∥
			if (c == '\u2225') {
				cArray[i] = '\u2016';
				continue;
			}
			// －
			if (c == '\uFF0D') {
				cArray[i] = '\u2212';
				continue;
			}
			// ￠
			if (c == '\uFFE0') {
				cArray[i] = '\u00A2';
				continue;
			}
			// ￡
			if (c == '\uFFE1') {
				cArray[i] = '\u00A3';
				continue;
			}
			// ￢
			if (c == '\uFFE2') {
				cArray[i] = '\u00AC';
				continue;
			}
		}

		return new String(cArray);
	}



	/**
	 * テキストファイル書き込み。
	 * @param isBackup 出力先をバックアップ先ファイルパスにする
	 */
	private void writeTextFile(boolean isBackup) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		String fileName = null;
		try {
			logger.info("writeTextFile isBackup=" + isBackup);
			if (isBackup) {
				// バックアップ先ファイルパスにファイルを作成する
				fileName = this.textFileInfo.getBackupFilePath() + this.textFileInfo.getFileName();
				String ascFileName = this.textFileInfo.getBackupFilePath() + this.textFileInfo.getFileName() + ".asc";
				logger.info("writeTextFile ascFileName=" + ascFileName);
				File ascFile = new File(ascFileName);
				if (ascFile.isFile()) {
					logger.info("writeTextFile バックアップファイルがあるため複合化する。");
					// 既にバックアップファイルがある場合は、複合化して追記する。
					this.decrypt(ascFileName);
					if (ascFile.delete()) {
						// ファイル削除成功
						logger.info("ファイル削除が成功しました。");
					} else {
						// ファイル削除に失敗。
						logger.info("テキストファイル：リクエストでファイル削除に失敗しました。: 存在しないファイルの削除失敗は処理を継続します。");
					}
				}
			} else {
				// 出力先ファイルパスにファイルを作成する
				fileName = this.textFileInfo.getFilePath() + this.textFileInfo.getFileName();
			}
			logger.info("writeTextFile fileName=" + fileName);
			fos = new FileOutputStream(fileName, true);

			logger.info("writeTextFile charsetName=" + this.textFileInfo.getCharsetName());
			if (StringUtils.isEmpty(this.textFileInfo.getCharsetName())) {
				// charsetが指定されていないので、デフォルト文字コードでファイルを作成する。
				pw = new PrintWriter(fos);
			} else {
				// charsetが指定されているので、指定した文字コードでファイルを作成する。
				osw = new OutputStreamWriter(fos, this.textFileInfo.getCharsetName());
				pw = new PrintWriter(osw);
			}

			String text = this.textFileInfo.getText();
			if (TextFileWriterConstant.CHARSET_NAME_SJIS.equals(this.textFileInfo.getCharsetName())) {
				// SJISを指定された場合は、変換不可能な文字を置き換える。
				text = this.replaceSjisConvertErrorCode(text);
			} else if (TextFileWriterConstant.CHARSET_NAME_JIS.equals(this.textFileInfo.getCharsetName())) {
				// JIS(ISO2022JP)を指定された場合は、変換不可能な文字を置き換える。
				text = this.replaceJisConvertErrorCode(text);
			}

			pw.print(text);
		} catch (FileNotFoundException e) {
			logger.error("テキストファイル：リクエストでファイル生成が失敗しました。: fileName=" + fileName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		} catch (UnsupportedEncodingException e) {
			logger.error("テキストファイル：リクエストでファイル生成が失敗しました。: fileName=" + fileName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}
	}

	/**
	 * 暗号化用のテンポラリファイル名を生成する。
	 * 暗号化用のテンポラリファイル名 = UUID + ".txt"
	 * @return 暗号化用のテンポラリファイル名
	 */
	private String createTmpFileName() {
		return UUID.randomUUID().toString() + ".txt";
	}

	private String writeTmpFile() {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		String fileName = null;
		try{
			fileName = RB.getString(TextFileWriterConstant.ENCRYPT_TMP_PATH) + "/" + this.createTmpFileName();
			logger.info("writeTmpFile fileName=" + fileName);
			fos = new FileOutputStream(fileName, true);

			logger.info("writeEncryptionTextFile charsetName=" + this.textFileInfo.getCharsetName());
			if (StringUtils.isEmpty(this.textFileInfo.getCharsetName())) {
				// charsetが指定されていないので、デフォルト文字コードでファイルを作成する。
				pw = new PrintWriter(fos);
			} else {
				// charsetが指定されているので、指定した文字コードでファイルを作成する。
				osw = new OutputStreamWriter(fos, this.textFileInfo.getCharsetName());
				pw = new PrintWriter(osw);
			}

			String text = this.textFileInfo.getText();
			if (TextFileWriterConstant.CHARSET_NAME_SJIS.equals(this.textFileInfo.getCharsetName())) {
				// SJISを指定された場合は、変換不可能な文字を置き換える。
				text = this.replaceSjisConvertErrorCode(text);
			} else if (TextFileWriterConstant.CHARSET_NAME_JIS.equals(this.textFileInfo.getCharsetName())) {
				// JIS(ISO2022JP)を指定された場合は、変換不可能な文字を置き換える。
				text = this.replaceJisConvertErrorCode(text);
			}

			pw.print(text);
		} catch (FileNotFoundException e) {
			logger.error("テキストファイル：リクエストでファイル生成が失敗しました。: fileName=" + fileName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		} catch (UnsupportedEncodingException e) {
			logger.error("テキストファイル：リクエストでファイル生成が失敗しました。: fileName=" + fileName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}
		return fileName;
	}

	/**
	 * 暗号化されたテンポラリファイルからテキストを読み込む。
	 * @param fileFullName 暗号化されたテンポラリファイル名（フルパス）
	 * @return
	 */
	private String readTmpFile(String fileFullName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			return new String(Files.readAllBytes(Paths.get(fileFullName)));
		} catch (IOException e) {
			logger.error("テキストファイル：リクエストでファイル読込みが失敗しました。: fileFullName=" + fileFullName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		}
	}

	/**
	 * テキストファイル書き込み（暗号化あり）。
	 * @param isBackup 出力先をバックアップ先ファイルパスにする
	 */
	private void writeEncryptionTextFile(boolean isBackup) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		String fileName = null;
		try {
			// テンポラリファイルにテキストを出力する
			String tmpFileName = this.writeTmpFile();

			// テンポラリファイルを暗号化する
			this.encrypt(tmpFileName, this.encryptionCommandName);

			// 暗号化したテンポラリファイルを読み込む
			String ascFileName = tmpFileName + ".asc";
			String encryptionText = this.readTmpFile(ascFileName);

			// テンポラリファイルと暗号化したテンポラリファイルを削除する。
			File tmpFile = new File(tmpFileName);
			if (tmpFile.delete()) {
				// ファイル削除成功
				logger.info("テンポラリファイル削除が成功しました。");
			} else {
				// ファイル削除に失敗。
				logger.info("テキストファイル：リクエストでテンポラリファイル削除に失敗しました。: 存在しないファイルの削除失敗は処理を継続します。");
			}
			File ascTmpFile = new File(ascFileName);
			if (ascTmpFile.delete()) {
				// ファイル削除成功
				logger.info("暗号化したテンポラリファイル削除が成功しました。");
			} else {
				// ファイル削除に失敗。
				logger.info("テキストファイル：リクエストで暗号化したテンポラリファイル削除に失敗しました。: 存在しないファイルの削除失敗は処理を継続します。");
			}

			logger.info("writeEncryptionTextFile isBackup=" + isBackup);
			if (isBackup) {
				// バックアップ先ファイルパスにファイルを作成する
				fileName = this.textFileInfo.getBackupFilePath() + this.textFileInfo.getFileName();
			} else {
				// 出力先ファイルパスにファイルを作成する
				fileName = this.textFileInfo.getFilePath() + this.textFileInfo.getFileName();
			}
			logger.info("writeEncryptionTextFile fileName=" + fileName);
			fos = new FileOutputStream(fileName, true);

			pw = new PrintWriter(fos);
			pw.print(encryptionText);
		} catch (FileNotFoundException e) {
			logger.error("テキストファイル：リクエストでファイル生成が失敗しました。: fileName=" + fileName);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}
	}

	/**
	 * ファイル復号化.
	 *
	 * @param fileFullName
	 *            ファイル名（フルパス）
	 * @return 復号化実行結果（0:復号化成功、1以上:エラー）
	 */
	private int decrypt(String fileFullName) {
		int ret = 0;
		String command = RB.getString(TextFileWriterConstant.DECRYPT_COMMAND);
		command = command.replaceAll("%inputfile%", fileFullName);
		logger.info("command=" + command);

		List<String> commandStrList = new ArrayList<String>();
		commandStrList.addAll(Arrays.asList(command.split(" ")));
		ProcessBuilder pb = new ProcessBuilder(commandStrList);
		Process process;
		try {
			process = pb.start();
			try {
				ret = process.waitFor();
				logger.info("ファイル復号化結果=" + ret);
			} catch (InterruptedException e) {
				// コマンド中断
				logger.error("テキストファイル：リクエストでファイル復号化が失敗(コマンド中断)しました。fileName=" + fileFullName + ": ", e);
				throw new AppException(CommonConstant.SYSTEM_ERROR,
						new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
			}
		} catch (IOException e) {
			// コマンド実行エラー
			logger.error("テキストファイル：リクエストでファイル復号化が失敗しました。fileName=" + fileFullName + ": ", e);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		}
		return ret;
	}

	/**
	 * ファイル暗号化.
	 *
	 * @param fileFullName ファイル名（フルパス）
	 * @param commandName 暗号化コマンド名
	 * @return 暗号化実行結果（0:暗号化成功、1以上:エラー）
	 */
	private int encrypt(String fileFullName, String commandName) {
		int ret = 0;
		// 暗号化コマンド名が空でない場合は、「"encrypt.command" + "." + 暗号化コマンド名」
		// で作成したプロパティからコマンドを取得する。
		String commandKey = TextFileWriterConstant.ENCRYPT_COMMAND
				+ (StringUtils.isEmpty(commandName) ? "" : ("." + commandName));
		logger.info("commandKey=" + commandKey);
		String command = RB.getString(commandKey);
		command = command.replaceAll("%inputfile%", fileFullName);
		logger.info("command=" + command);

		List<String> commandStrList = new ArrayList<String>();
		commandStrList.addAll(Arrays.asList(command.split(" ")));
		ProcessBuilder pb = new ProcessBuilder(commandStrList);
		Process process = null;
		try {
			process = pb.start();
			try {
				ret = process.waitFor();
				logger.info("ファイル暗号化結果=" + ret);
			} catch (InterruptedException e) {
				// コマンド中断
				logger.error("テキストファイル：リクエストでファイル暗号化が失敗(コマンド中断)しました。fileName=" + fileFullName, e);
				throw new AppException(CommonConstant.SYSTEM_ERROR,
						new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
			}
		} catch (IOException e) {
			// コマンド実行エラー
			logger.error("テキストファイル：リクエストでファイル暗号化が失敗しました。fileName=" + fileFullName, e);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		}
		return ret;
	}

	/**
	 * バックアップ先ファイルを出力先ファイルパスに移動する。
	 *
	 */
	private void moveFromBackupToTarget() {
		Path backupFile = null;
		Path targetPath = null;
		try {
			backupFile = Paths.get(this.textFileInfo.getBackupFilePath(), this.textFileInfo.getFileName());
			logger.info("moveFromBackupToTarget backupFile=" + backupFile.toString());

			targetPath = Paths.get(this.textFileInfo.getFilePath());
			logger.info("moveFromBackupToTarget targetPath=" + targetPath.toString());
			Files.move(backupFile, targetPath.resolve(backupFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("テキストファイル：リクエストでファイルの移動に失敗しました。: backupFile=" + backupFile.toString()
				+ ", targetPath=" + targetPath.toString(), e);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		}

	}

	/**
	 * バックアップ先ファイルを出力先ファイルパスにコピーする。
	 *
	 */
	private void copyFromBackupToTarget() {
		Path backupFile = null;
		Path targetPath = null;
		try {
			backupFile = Paths.get(this.textFileInfo.getBackupFilePath(), this.textFileInfo.getFileName());
			logger.info("copyFromBackupToTarget backupFile=" + backupFile.toString());

			targetPath = Paths.get(this.textFileInfo.getFilePath());
			logger.info("copyFromBackupToTarget targetPath=" + targetPath.toString());
			Files.copy(backupFile, targetPath.resolve(backupFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("テキストファイル：リクエストでファイルのコピーに失敗しました。: backupFile=" + backupFile.toString()
				+ ", targetPath=" + targetPath.toString(), e);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { TextFileWriterConstant.CONNECTION_SYSTEM_NAME });
		}

	}

	/**
	 * コンストラクタ。
	 * @param textFileInfo テキストファイル情報
	 */
	public TextFileService(webform.ms.grpc.text_file_writer.TextFileInfo textFileInfo) {
		this.textFileInfo = textFileInfo;
	}

	/**
	 * コンストラクタ。
	 * @param textFileInfo テキストファイル情報
	 */
	public TextFileService(webform.ms.grpc.text_file_writer.EncryptTextFileInfo textFileInfo) {
		this.textFileInfo = webform.ms.grpc.text_file_writer.TextFileInfo.newBuilder()
				.setFilePath(textFileInfo.getFilePath())
				.setBackupFilePath(textFileInfo.getBackupFilePath())
				.setFileName(textFileInfo.getFileName())
				.setCharsetName(textFileInfo.getCharsetName())
				.setText(textFileInfo.getText())
				.build();
		this.encryptionCommandName = textFileInfo.getEncryptionCommandName();
		logger.info("TextFileService encryptionCommandName=" + this.encryptionCommandName);
	}

	/**
	 * テキストファイル書き込み。
	 *
	 */
	public void writeTextFile() {
		if (StringUtils.isEmpty(this.textFileInfo.getBackupFilePath())) {
			// 出力先ファイルパスに直接ファイルを作成する
			this.writeTextFile(false);
		} else {
			// バックアップ先ファイルパスにファイルを作成して、暗号化後にバックアップ先から出力先に移動する
			this.writeTextFile(true);
			String bckupFile = this.textFileInfo.getBackupFilePath() + this.textFileInfo.getFileName();
			this.encrypt(bckupFile, this.encryptionCommandName);
			this.moveFromBackupToTarget();
		}
	}

	/**
	 * テキストファイル書き込み（暗号化有無指定あり）。
	 *
	 */
	public void writeEncryptionTextFile() {
		if (StringUtils.isEmpty(this.encryptionCommandName)) {
			// 暗号化コマンドが指定されていない場合は、暗号化なし。
			this.writeTextFile();
		} else {
			if (StringUtils.isEmpty(this.textFileInfo.getBackupFilePath())) {
				// 出力先ファイルパスに直接ファイルを作成する。
				// 出力テキストを暗号化して、追加書き込みを行う。
				this.writeEncryptionTextFile(false);
			} else {
				// バックアップ先ファイルパスにファイルを作成して、出力テキストを暗号化して、追加書き込みを行う。
				// 書込み後にバックアップ先から出力先にコピーする。
				this.writeEncryptionTextFile(true);
				this.copyFromBackupToTarget();
			}
		}
	}

	/**
	 * テキストファイル削除。
	 *
	 */
	public void deleteTextFile() {
		File tmpFile = new File(this.textFileInfo.getFilePath(), this.textFileInfo.getFileName());
		logger.info("tmpFile=" + tmpFile.getPath());
		if (tmpFile.delete()) {
			// ファイル削除成功
			logger.info("ファイル削除が成功しました。");
		} else {
			// ファイル削除に失敗。
			logger.info("テキストファイル：リクエストでファイル削除に失敗しました。: 存在しないファイルの削除失敗は処理を継続します。");
		}
	}
}
