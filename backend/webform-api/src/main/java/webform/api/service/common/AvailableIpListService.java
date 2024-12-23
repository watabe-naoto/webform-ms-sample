package webform.api.service.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.util.SubnetUtils;

/**
 * メンテナンス中に利用可能なクライアントGIPリストサービスクラス。
 */
public class AvailableIpListService {

	/**
	 * IPv4のIPアドレスの正規表現。
	 */
	private static String IP4_REGEX = "^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";

	/**
	 * IPv4のIPアドレスCIDR表記の正規表現。
	 */
	private static String IP4_CIDR_REGEX = "^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(/([0-9]|[1-2][0-9]|3[0-2]))$";

	/** Log */
	private Log log = LogFactory.getLog(getClass());

	/**
	 * プロパティファイルパス.
	 */
	private String filePath = "/var/prop/webform/AvailableIpList.properties";

	/**
	 * 前回読み込んだプロパティファイルの最終変更時間。
	 */
	private FileTime beforLastModifiedTime = null;

	/**
	 * メンテナンス中に利用可能なクライアントGIPリスト.
	 */
	private Set<String> gipSet = new HashSet<String>();

	/**
	 * コンストラクタ.
	 */
	public AvailableIpListService() {
		// 処理なし
	}

	/**
	 * プロパティファイルパス設定.
	 */
	public void setFilePath(String filePath) {
		synchronized  (this) {
			this.filePath = filePath;
		}
	}

	/**
	 * 読み込んだテキストの行をIPリストに登録する。
	 * ただし、空行または先頭が"#"の行はコメント行として読み飛ばす。
	 * @param lineList
	 * @return
	 */
	private Set<String> readProp(String[] lineList) {
		log.debug("readProp start");
		Set<String> newGipSet = new HashSet<String>();
		try {
			for (String line : lineList) {
				log.debug("line=" + line);
				if(StringUtils.isEmpty(line) || line.startsWith("#")) {
					// 空行または先頭が"#"はコメント行として読み飛ばす。
					continue;
				}

				// IP行をリストに追加する。
				newGipSet.add(line);
			}
			return newGipSet;
		} finally {
			if(log.isDebugEnabled()){
				for (String gip : newGipSet) {
					log.debug("gip=" + gip);
				}
			}
			log.debug("readProp end");
		}
	}

	/**
	 * プロパティファイル読み込み。
	 */
	private void loadFile() {
		synchronized (this) {
			try {
				File file = new File(this.filePath);
				log.info("filePath=" + this.filePath);
				Path path = Paths.get(file.getAbsolutePath());
				FileTime fileTime = Files.getLastModifiedTime(path);
				log.debug("beforLastModifiedTime=" + this.beforLastModifiedTime + ", fileTime=" + fileTime);
				if (this.beforLastModifiedTime != null && this.beforLastModifiedTime.compareTo(fileTime) == 0) {
					// 最終変更時間が同じなのでファイル読み込みなし。
					log.info("プロパティファイルの再読み込み不要");
					return;
				}

				// 最終変更時間が違うのでファイル再読み込み。
				String text = new String(Files.readAllBytes(path));
				String[] lineList = text.split("[\r\n]+");

				this.gipSet = this.readProp(lineList);
				this.beforLastModifiedTime = fileTime;
			} catch (IOException e) {
				log.warn("プロパティファイルの読み込みに失敗しました。", e);
				// プロパティファイルの読み込みに失敗したため、一覧を空にする。
				this.gipSet = new HashSet<String>();
				this.beforLastModifiedTime = null;
			}
		}
	}

	/**
	 * メンテナンス中に利用可能なクライアントGIPを判定する.
	 * @param ipAddress
	 * @return
	 */
	public boolean isAvailableIp(String ipAddress) {
		if(!ipAddress.matches(IP4_REGEX)) {
			// ipAddressがIPv4のIPアドレス形式の文字列でない場合は、メンテナンス中に利用可能なクライアントGIPに不一致とする。
			log.info("クライアントのIP " + ipAddress + " は不一致");
	        return false;
		}

		// プロパティファイルのタイムスタンプが変更されていれば、再読み込みを行う。
		this.loadFile();

		// チック処理
		synchronized  (this) {
			Set<String> currentGipSet = this.gipSet;
			for (String gip : currentGipSet) {
				log.debug("gip=" + gip);
				if (gip.matches(IP4_REGEX)) {
					// IPv4アドレス
					if (gip.equals(ipAddress)) {
						// IPアドレス一致
						log.info("プロパティのIP " + gip + " とクライアントのIP " + ipAddress + " が一致");
						return true;
					}
				} else if (gip.matches(IP4_CIDR_REGEX)) {
					// IPv4CIDR表記
					SubnetUtils utils = new SubnetUtils(gip);
					utils.setInclusiveHostCount(true);
					if (utils.getInfo().isInRange(ipAddress)) {
						// IPアドレス一致
						log.info("プロパティのIP " + gip + " にクライアントのIP " + ipAddress + " が含まれる");
						return true;
					}
				} else {
					// IPアドレスのフォーマット以外はスキップ。
					log.info("プロパティの値 " + gip + " がIPアドレスではないスキップ");
					continue;
				}
			}
		}

		// メンテナンス中に利用可能なクライアントGIPに不一致。
		log.info("クライアントのIP " + ipAddress + " は不一致");
        return false;
	}

}
