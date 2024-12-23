package webform.api.constant.common;

public class ConductCodeConstant {
	/** 申込導線区分のコード定義. */
	public static enum CONDUCT {
		BUSINESS_ADDRESS_EDIT("businessAddressEdit","110"),
		EXTENSION_PAYMENT("extensionPaymentOrder","122"),
		;

		private String key = null;
		private String value = null;

		private CONDUCT(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		public static CONDUCT get(String key) {
			for (CONDUCT target : CONDUCT.values()) {
				if (target.key.equals(key)) {
					return target;
				}
			}
			return null;
		}
	}
	/** 申込導線区分種別のコード定義 */
	public static enum CONDUCTGROUP {
			OCNSERVICE,
			ISPFREE,
			AGENCY,
			TAIKAISYA,
	}
}
