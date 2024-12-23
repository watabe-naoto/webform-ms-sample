package webform.api.prop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 環境設定用プロパティクラス.
 * システムプロパティ(newso.env.properties.list）に設定されているファイルからプロパティファイル一覧を取得して、
 * 全てのプロパティファイルを読み込んだプロパティを生成する。
 * プロパティファイルの文字コードはUTF-8で作成すること。
 */
public class EnvironmentSettingProperties {
	/** プロパティファイル一覧のファイルパスを設定するシステムプロパティキー。 */
	private static final String PROPERTIES_FILE_LIST_SYSTEM_PROPERTY_KEY = "newso.env.properties.list";

	/** プロパティファイル一覧のデフォルトファイル。 */
	private static final String PROPERTIES_FILE_LIST_PATH = "/var/prop/webform/PropertiesFileList.pl";

	/** プロパティファイルの文字コード。 */
	private static final String CHARSET_NAME = "UTF-8";

	/** プロパティファイル一覧のコメント行先頭文字。 */
	private static final String COMMENT_START_CHAR = "#";

	/** プロパティ。 */
	private static final Properties prop = new Properties();

	/**
	 * プロパティファイル一覧のファイルパスを取得する。
	 * @return プロパティファイル一覧のファイルパス
	 */
	private static String getPropertiesListFile() {
		String fileListPath = null;
		try {
			// システムプロパティからファイルパスを取得する。
			fileListPath = System.getProperty(EnvironmentSettingProperties.PROPERTIES_FILE_LIST_SYSTEM_PROPERTY_KEY);
			if (StringUtils.isEmpty(fileListPath)) {
				// システムプロパティがない場合は、デフォルトのファイルパスを取得する。
				System.err.println("EnvironmentSettingProperties System property is null.[newso.env.properties.list]");
				fileListPath = EnvironmentSettingProperties.PROPERTIES_FILE_LIST_PATH;
			}
		} catch (RuntimeException e) {
			// システムプロパティ取得エラー
			System.err.println("EnvironmentSettingProperties System property not found.[newso.env.properties.list]");
			e.printStackTrace();
			fileListPath = EnvironmentSettingProperties.PROPERTIES_FILE_LIST_PATH;
		}
		return fileListPath;
	}

	/**
	 * プロパティファイルを読み込む。
	 * @param fileName プロパティファイル名（フルパス）
	 */
	private static void readPropertiesFile(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName);
				InputStreamReader isr = new InputStreamReader(fis, EnvironmentSettingProperties.CHARSET_NAME);) {
			EnvironmentSettingProperties.prop.load(isr);
		} catch (IOException e) {
			System.err.println("EnvironmentSettingProperties Read properties file error.");
			e.printStackTrace();
		}
	}

	/**
	 * 環境設定用プロパティの内容を初期化する。
	 */
	private static void initProperties() {
		// プロパティをクリアする。
		prop.clear();

		// プロパティファイル一覧のファイルパスを取得する。
		String fileListPath = EnvironmentSettingProperties.getPropertiesListFile();
//		System.out.println("EnvironmentSettingProperties fileListPath=" + fileListPath);

		try (FileInputStream listFis = new FileInputStream(fileListPath);
				InputStreamReader listIsr = new InputStreamReader(listFis, EnvironmentSettingProperties.CHARSET_NAME);
				BufferedReader listBr = new BufferedReader(listIsr);) {
			File listFile = new File(fileListPath);
			if (listFile.isFile()) {
				// プロパティファイル一覧のファイルを読み込む。
				String fileName = listBr.readLine();
//				int count = 0;
//				System.out.println("fileName[" + count + "]=" + fileName);
				while (fileName != null) {
					if (fileName.indexOf(EnvironmentSettingProperties.COMMENT_START_CHAR) == 0) {
						// 開始文字が"#"の場合はコメント行として読み飛ばす。
						fileName = listBr.readLine();
//						count++;
//						System.out.println("fileName[" + count + "]=" + fileName);
						continue;
					}

					if (StringUtils.isEmpty(fileName)) {
						// 空行の場合は読み飛ばす。
						fileName = listBr.readLine();
//						count++;
//						System.out.println("fileName[" + count + "]=" + fileName);
						continue;
					}

					File file = new File(fileName);
					if (!file.isFile()) {
						// プロパティファイルが存在しない。
						System.err.println("EnvironmentSettingProperties Properties file not found.[" + fileName + "]");
						fileName = listBr.readLine();
//						count++;
//						System.out.println("fileName[" + count + "]=" + fileName);
						continue;
					}

					// プロパティファイルを読み込む。
					EnvironmentSettingProperties.readPropertiesFile(fileName);

					fileName = listBr.readLine();
//					count++;
//					System.out.println("fileName[" + count + "]=" + fileName);
				}
			} else {
				// プロパティファイル一覧のファイルが存在しない。
				System.err.println("EnvironmentSettingProperties Properties file list not found.[" + fileListPath + "]");
			}
		} catch (IOException e) {
			System.err.println("EnvironmentSettingProperties Read properties file list error.");
			e.printStackTrace();
		}
	}

	// 本クラスのstatic初期化処理。
	static {
		// 環境設定用プロパティの内容を初期化する。
		EnvironmentSettingProperties.initProperties();
	}

	/**
	 * 環境定義プロパティを取得する。key に該当するメッセージが無い場合、nullを返す。
	 *
	 * @param key
	 * @return
	 */
	public static String getEnvProp(String key) {
		return EnvironmentSettingProperties.prop.getProperty(key);
	}

	/**
	 * 環境定義プロパティを取得する。key に該当するメッセージが無い場合、空文字列を返す。
	 *
	 * @param key
	 * @return
	 */
	public static String getEnvPropQuiet(String key) {
		return EnvironmentSettingProperties.prop.getProperty(key, "");
	}

}
