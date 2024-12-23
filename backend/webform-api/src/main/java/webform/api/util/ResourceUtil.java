package webform.api.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

/**
 * リソースユーティリティクラス
 *
 */
public class ResourceUtil {
	/** Bundle */
	private static ResourceBundle bundle;

	private ResourceUtil() {
		super();
	}
	/**
	 * 文字列の取得
	 *
	 * @param key キー
	 * @return 文字列
	 */
	public static String getString(final String key) {
		return getString(key, null);
	}
	/**
	 * 文字列の取得
	 *
	 * @param key キー
	 * @param defaultValue 初期値
	 * @return 文字列
	 */
	public static String getString(final String key, final String defaultValue) {
		if (!StringUtils.isEmpty(key)) {
			try {
				return bundle.getString(key);
			}
			catch (MissingResourceException ex) {
			}
		}
		return defaultValue;
	}
	/**
	 * 数値の取得
	 *
	 * @param key キー
	 * @return 数値
	 */
	public static Integer getInteger(final String key) {
		return getInteger(key, null);
	}
	/**
	 * 数値の取得
	 *
	 * @param key キー
	 * @param defaultValue 初期値
	 * @return 数値
	 */
	public static Integer getInteger(final String key, final Integer defaultValue) {
		String value = getString(key, null);
		if (!StringUtils.isEmpty(value)) {
			try {
				return Integer.parseInt(value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return defaultValue;
	}
	static {
		try {
			bundle = ResourceBundle.getBundle("webform_api");
		}
		catch (MissingResourceException ex) {
			bundle = ResourceBundle.getBundle("webform_api", Locale.US);
		}
	}
	/**
	 * Boolean型の取得
	 *
	 * @param key キー
	 * @return Boolean型
	 */
	public static Boolean getBoolean(final String key) {
		return getBoolean(key, null);
	}
	/**
	 * Boolean型の取得
	 *
	 * @param key キー
	 * @param defaultValue 初期値
	 * @return Boolean型
	 */
	public static Boolean getBoolean(final String key, final Boolean defaultValue) {
		String value = getString(key, null);
		if (!StringUtils.isEmpty(value)) {
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}
	public static byte[] getPrimitiveByte(final String key) {
		return getPrimitiveByte(key, null);
	}
	public static byte[] getPrimitiveByte(final String key, final byte[] defaultValue) {
		String value = getString(key, null);
		if (!StringUtils.isEmpty(value)) {
			return value.getBytes();
		}
		return defaultValue;
	}
}
