package webform.api.constant.prop;

/**
 * マッチカウントCSVファイル出力用の定数定義。
 */
public class MatchCountConstant {
	/** プロパティキー：本人確認画像アップロードのマッチカウントCSVファイルの出力パス。 */
	public static final String PROP_UPLOAD_MATCH_COUNT_FILE_PATH = "match.count.upload.output.path";

	/** プロパティキー：本人確認画像アップロードのマッチカウントファイル。 */
	public static final String PROP_UPLOAD_MATCH_COUNT_FILE = "match.count.upload.count.file";

	/** プロパティキー：マッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_FILE_PATH = "match.count.output.path";

	/** プロパティキー：請求書送付先変更マッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_ADDRESS_EDIT_FILE_PATH = "match.count.address.edit.output.path";

	/** プロパティキー：請求書送付先変更（法人）マッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_BUSINESS_ADDRESS_EDIT_FILE_PATH = "match.count.business.address.edit.output.path";

	/** プロパティキー：一括請求マッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_IKKATSU_FILE_PATH = "match.count.ikkatsu.output.path";

	/** プロパティキー：口座振替依頼書の郵送（法人）マッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_BUSINESS_KOFURI_FILE_PATH = "match.count.business.kofuri.output.path";

	/** プロパティキー：解約のマッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_KAIYAKUFILE_PATH = "match.count.kaiyaku.output.path";

	/** プロパティキー：光コラボ申込(新規/プラン変)のマッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_OCNHIKARI__FILE_PATH = "match.count.ocnhikari.output.path";

	/** プロパティキー：光コラボ申込のマッチカウントCSVのファイルパス */
	public static final String PROP_MATCH_COUNT_OCNHIKARI_TENYOU_FILE_PATH = "match.count.ocnhikari.tenyou.output.path";

	/** マッチメール用の申込連番取得用ファイル名 */
	public static final String MATCH_MAIL_FILE_NAME = "match_count.dat";
}
