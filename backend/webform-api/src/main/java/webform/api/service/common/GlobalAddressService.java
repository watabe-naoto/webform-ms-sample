package webform.api.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * グローバルIPアドレス取得サービスクラス.
 *
 */
public class GlobalAddressService {

	@Context
	HttpServletRequest request;

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
	 * グローバルIPアドレス取得.
	 *
	 * @return IPアドレス
	 */
	public String getIPaddress() {
		log.info("★GlobalAddressService Start.");
		try {
			String ipAddress = "";
			String targetIp = null;

			// リクエストヘッダからIPアドレス取得
			String xForwardedFor = request.getHeader("X-Forwarded-For");

			if (StringUtils.isNotEmpty(xForwardedFor)) {
				String[] gipList = xForwardedFor.split(",\\s*");
				// 取得したIP（複数取得の場合あり）からIPアドレス表記かチェックし返す。
				targetIp = checkGipList(gipList);
			}

			if (targetIp != null) {
				ipAddress = targetIp;
			} else {
				log.info("xForwardedFor not found.");
				ipAddress = request.getRemoteAddr();
			}
			return ipAddress;
		} finally {
			log.info("★GlobalAddressResource end.");
		}
	}

	/**
	 * 取得したIP（複数取得の場合あり）からIPアドレス表記かチェックし返す。
	 *
	 * @return IPアドレス
	 */
	public String checkGipList(String[] gipList) {
		for (String ip : gipList) {
			// IPアドレスか正規表現チェック
			if (ip.matches(IP4_REGEX) || ip.matches(IP4_CIDR_REGEX)) {
				return ip;
			}
		}
		// IPアドレスが存在しなかった場合nullを返す
		return null;

	}
}