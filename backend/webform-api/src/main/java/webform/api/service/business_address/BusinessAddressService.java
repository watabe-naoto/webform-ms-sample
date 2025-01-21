package webform.api.service.business_address;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import webform.api.constant.business_address.BusinessAddressConstant;
import webform.api.constant.common.CommonConstant;
import webform.api.constant.prop.MatchCountConstant;
import webform.api.constant.prop.MatchMailConstant;
import webform.api.constant.prop.ThanksMailConstant;
import webform.api.dto.business_address.BusinessAddressEditEntity;
import webform.api.exception.AppException;
import webform.api.prop.EnvironmentSettingProperties;
import webform.api.service.common.CountService;


/**
 * 請求書送付先変更のお申し込み受付Serviceクラス
 *
 */
public class BusinessAddressService {
	/** Log */
	private final Logger logger = LogManager.getLogger(BusinessAddressService.class);

	/** 請求書送付先変更のマッチカウント。 */
	private String matchCount = null;
	/** 請求書送付先変更のマッチメールカウント。 */
	private String matchMailCount = null;
	/** 請求書送付先変更のお申し込み受付のサンキューメール用の申込連番。 */
	private String thanksMailCount = null;
	/** 現在日時 */
	private Calendar rightNow = null;
	/** マッチカウントファイル名 */
	private String matchCountFileName = "business_address_edit_match_count";

	/**
	 * コンストラクタ
	 */
	public BusinessAddressService() {
		try {
			// 請求書送付先変更のお申し込み受付のマッチカウント取得。
			String matchCountFile = EnvironmentSettingProperties.getEnvProp(
					MatchCountConstant.PROP_MATCH_COUNT_BUSINESS_ADDRESS_EDIT_FILE_PATH) + matchCountFileName + ".dat";
			logger.info("BusinessAddressService.BusinessAddressService() matchCountFile=" + matchCountFile);
			CountService matchCountService = new CountService(matchCountFile);
			this.matchCount = matchCountService.createCount("", 6);

			// 請求書送付先変更のお申し込み受付のマッチメールカウント取得。
			String matchMailCountFile = EnvironmentSettingProperties.getEnvProp(MatchMailConstant.PROP_MATCH_MAIL_FILE_PATH)
					+ MatchCountConstant.MATCH_MAIL_FILE_NAME;
			logger.info("BusinessAddressService.BusinessAddressService() matchMailCountFile=" + matchMailCountFile);
			CountService matchMailCountService = new CountService(matchMailCountFile);
			this.matchMailCount = matchMailCountService.createCount("", 6);

			// 請求書送付先変更のお申し込み受付のサンキューメール用の申込連番取得。
			String thanksCountFile = EnvironmentSettingProperties.getEnvProp(ThanksMailConstant.PROP_THANKS_OUTPUT_PATH)
					+ ThanksMailConstant.THANKS_FILE_NAME;
			logger.info("BusinessAddressService.BusinessAddressService() thanksCountFile=" + thanksCountFile);
			CountService thanksMailCountService = new CountService(thanksCountFile);
			this.thanksMailCount = thanksMailCountService.createCount("", 6);

			rightNow = Calendar.getInstance();

		} catch (IOException e) {
			logger.error("申込情報ファイル作成：リクエストでホスト名の取得に失敗しました。: ", e);
			throw new AppException(CommonConstant.SYSTEM_ERROR,
					new String[] { BusinessAddressConstant.CONNECTION_SYSTEM_NAME });
		}

	}

	/**
	 * FTP CSVファイル名作成。
	 *
	 * @return
	 */
	public String createFtpCsvFileName() {

		StringBuilder sb = new StringBuilder();
		sb.append("BCC_Matsu.Soufusaki.");
		sb.append(new SimpleDateFormat("yyyyMMdd").format(rightNow.getTime()));
		sb.append("-48.csv");

		logger.info("FtpCsvFileName=" + sb.toString());
		return sb.toString();
	}

	/**
	 * FTP CSVファイル内容作成。
	 *
	 * @return
	 */
	public String createFtpCsv(BusinessAddressEditEntity entity) {
		StringJoiner sj = new StringJoiner(",", StringUtils.EMPTY, "\r\n");

		boolean hasAddress2 = StringUtils.equals(entity.getAddress2Select(), "address2");
		// 01.申込連番(マッチメールCVSと同じ連番を設定する)
		sj.add(this.matchMailCount)
			// 02.申し込み日"YYYY/MM/DD" 形式
			.add(entity.getDateOfRequest())
			// 03.申し込み時刻"HH:MM:SS" 形式
			.add(entity.getTimeOfRequest())
			// 04.送信元メールアドレス(FROM)～07.ブラインドコピー送信先メールアドレス(BCC)
			.add("506506@ntt.com").add("chumon@shop.ntt.com").add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			// 08.返信先メールアドレス(Reply-To)
			.add(entity.getMail1())
			// 09.メッセージ形式(Content-Type)～10.メッセージタイトル(Subject)
			.add("text/plain").add("【B】請求書送付先変更申込受付")
			// 11.電話番号
			.add(entity.getTel())
			// 12.投入日(YYYYMMDD)
			.add(entity.getDateOfRequest().replaceAll("/", StringUtils.EMPTY))
			// 13.投入時刻(HHMMSS)
			.add(entity.getTimeOfRequest().replaceAll(":", StringUtils.EMPTY))
			// 14.契約者名～15.契約者名(カナ)
			.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)
			// 16.申し込み者名
			.add(entity.getApplyName())
			// 17.申し込み者名(カナ)
			.add(StringUtils.EMPTY)
			// 18.受付区分(コード)～25.商品名(名称)
			.add("D0005").add("Web").add("E0130").add("キャンペーン").add(StringUtils.EMPTY)
			.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)
			// 26.連絡先電話番号
			.add(entity.getTel())
			// 27.連絡先メールアドレス
			.add(entity.getMail1())
			// 28.連絡先方法種別～30.ポイントトークンID
			.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)
			// 31.ご請求番号(1)
			.add(entity.getBillNumber01())
			// 32.ご請求番号(2)
			.add(entity.getBillNumber02())
			// 33.ご請求番号(3)
			.add(entity.getBillNumber03())
			// 34.ご請求番号(4)
			.add(entity.getBillNumber04())
			// 35.ご請求番号(5)
			.add(entity.getBillNumber05())
			// 36.ご請求番号(6)
			.add(entity.getBillNumber06())
			// 37.ご請求番号(7)
			.add(entity.getBillNumber07())
			// 38.ご請求番号(8)
			.add(entity.getBillNumber08())
			// 39.ご請求番号(9)
			.add(entity.getBillNumber09())
			// 40.ご請求番号(10)
			.add(entity.getBillNumber10())
			// 41.請求書送付先
			.add(StringUtils.EMPTY)
			// 42.住所コード
			.add(entity.getAddressCode())
			// 43.郵便番号
			.add(entity.getPost())
			// 44.都道府県市区町村
			.add(entity.getAddress1())
			// 45.番地号
			.add(hasAddress2 ? entity.getAddress2() : StringUtils.EMPTY)
			// 46.ビル名・部屋番号
			.add(entity.getAddress3())
			// 47.変更前の請求書送付先名
			.add(entity.getDestinationName())
			// 48.変更後の請求書送付先名
			.add(entity.getDestinationSectionName())
			// 49.変更後の請求書送付先名フリガナ
			.add(entity.getDestinationKana())
			// 50.変更希望月
			.add(entity.getDateofchange())
			// 51.法人/個人
			.add(StringUtils.EMPTY)
			// 52.会社名/団体名
			.add(entity.getApplyCorpname())
			// 53.会社名/団体名フリガナ
			.add(StringUtils.EMPTY)
			// 54.NTTコミュニケーションズ組織コード～60.問合せ種類
			.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY)	.add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			.add(StringUtils.EMPTY).add(StringUtils.EMPTY).add("【B】請求書送付先変更申込受付");

		String ret = sj.toString();
		logger.debug("FtpCsv: " + ret);

		return ret;
	}

	/**
	 * サンキューメールCSVファイル名作成。 thanksmail.YYYYMMDD-N.csv 1
	 *
	 * @return
	 */
	public String createThanksmMailCsvFileName() {

		StringBuilder sb = new StringBuilder();

		// 現時刻を取得
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);

		sb.append("thanksmail."); // 固定値:"thanksmail."
		sb.append(new SimpleDateFormat("yyyyMMdd").format(rightNow.getTime()));
		sb.append("-");
		sb.append(hour / 3 + 1);
		sb.append(".csv"); // 固定値:".csv"

		logger.info("thanksmMailCsvFileName=" + sb.toString());
		return sb.toString();
	}

	/**
	 * サンキューメールCSVファイル内容作成。
	 *
	 * @return
	 */
	public String createThanksmMailCsv(BusinessAddressEditEntity entity) {
		StringJoiner sj = new StringJoiner(",", StringUtils.EMPTY, "\n");

		// 01.フォームID(固定値:"business_address_edit")
		sj.add("business_address_edit")
			// 02.申込連番
			.add(this.thanksMailCount)
			// 03.申込日("YYYY/MM/DD"形式)
			.add(new SimpleDateFormat("yyyy/MM/dd").format(rightNow.getTime()))
			// 04.申込時刻("HH:MM:SS"形式)
			.add(new SimpleDateFormat("HH:mm:ss").format(rightNow.getTime()))
			// 05.お客さまメールアドレス
			.add(entity.getMail1())
			// 06.本文(固定値:"請求書送付先変更申込受付完了")
			.add("請求書送付先変更申込受付完了");

		String ret = sj.toString();
		logger.debug("ThanksmMailCsv:" + ret);

		return ret;
	}

	/**
	 * マッチカウントCSVファイル名編集（yyyyMM_business_address_edit_match_count.csv）
	 *
	 * @return マッチカウントCSVファイル名
	 */
	public String createMatchCountCsvFileName() {

		StringBuilder sb = new StringBuilder();

		// 作成された年(西暦)月("YYYYMM"形式)
		sb.append(new SimpleDateFormat("yyyyMM").format(rightNow.getTime()));
		// 固定値:"_business_address_edit_match_count.csv"
		sb.append("_" + matchCountFileName + ".csv");

		logger.info("matchCountCsvFileName=" + sb.toString());
		return sb.toString();
	}

	/**
	 * マッチカウントCSVファイル内容編集
	 * @param entity 請求書送付先変更情報
	 * @return マッチカウントCSVファイル内容
	 */
	public String createMatchCountCsv(BusinessAddressEditEntity entity) {
		StringJoiner sj = new StringJoiner(",", StringUtils.EMPTY, "\n");

		// 01.アクセス数
		sj.add(this.matchCount)
			// 02.アクセス日時
			.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(rightNow.getTime()))
			// 03.申込み種別
			.add("【B】請求書送付先変更申込受付");

		String ret = sj.toString();
		logger.debug("MatchCountCsv: " + ret);

		return ret;
	}

	/**
	 * マッチメールCSVファイル名編集 matchmail.YYYYMMDD-NN.csv 1
	 * @return マッチメールCSVファイル名
	 */
	public String createMatchMailCsvFileName() {

		int flg = rightNow.get(Calendar.MINUTE) < 30 ? 0 : 1;
		int n = rightNow.get(Calendar.HOUR_OF_DAY) * 2 + flg + 1;
		StringBuilder sb = new StringBuilder();

		sb.append("matchmail."); // 固定値:"matchmail."
		sb.append(new SimpleDateFormat("yyyyMMdd").format(rightNow.getTime())); // 申込日("YYYYMMDD"形式)
		sb.append("-" + String.format("%02d", n) + ".csv");

		logger.info("matchMailCsvFileName=" + sb.toString());
		return sb.toString();
	}

	/**
	 * マッチメールCSVファイル内容編集
	 * @param entity 請求書送付先変更情報
	 * @return マッチメールCSVファイル内容
	 */
	public String createMatchMailCsv(BusinessAddressEditEntity entity) {
		StringJoiner sj = new StringJoiner(",");

		// 01.申込連番
		sj.add(this.matchMailCount)
			// 02.申し込み日
			.add(entity.getDateOfRequest())
			// 03.申し込み時刻
			.add(entity.getTimeOfRequest())
			// 04.送信元メールアドレス(FROM)～07.ブラインドコピー送信先メールアドレス(CC)
			.add("506506@ntt.com").add("chumon@shop.ntt.com").add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			// 08.返信先メールアドレス(Reply-To)
			.add(entity.getMail1())
			// 09.メッセージ形式(Content-Type)～10.メッセージタイトル(Subject)
			.add("text/plain").add("【B】請求書送付先変更申込受付")
			// 11.電話番号
			.add(entity.getTel())
			// 12.投入日
			.add(entity.getDateOfRequest().replaceAll("/", StringUtils.EMPTY))
			// 13.投入時刻
			.add(entity.getTimeOfRequest().replaceAll(":", StringUtils.EMPTY))
			// 14.契約者名～15.契約者名(カナ)
			.add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			// 16.申し込み者名
			.add(entity.getApplyName())
			// 17.申し込み者名(カナ)
			.add(StringUtils.EMPTY)
			// 18.受付区分(コード)～25.商品名(名称)
			.add("D0005").add("Web").add("E0130").add("キャンペーン").add(StringUtils.EMPTY)
			.add(StringUtils.EMPTY).add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			// 26.連絡先電話番号
			.add(entity.getTel())
			// 27.連絡先メールアドレス
			.add(entity.getMail1())
			// 28.連絡先方法種別～30.ポイントトークンID
			.add(StringUtils.EMPTY).add(StringUtils.EMPTY).add(StringUtils.EMPTY)
			// 31.請求ID
			.add(entity.getBillNumber01())
			// 32.質問内容
			.add(getQuestion(entity))
			// 33.問合せ種類
			.add("【B】請求書送付先変更申込受付");

		String ret = sj.toString();
		logger.debug("MatchMailCsv: " + ret);

		return ret;
	}

	/**
	 * 質問内容編集
	 * @param entity 請求書送付先変更情報
	 * @return 質問内容
	 */
	private String getQuestion(BusinessAddressEditEntity entity) {
		StringJoiner sj = new StringJoiner("\n");
		boolean hasAddress2 = StringUtils.equals(entity.getAddress2Select(), "address2");
		sj.add(appendValue("ご請求番号(1)", entity.getBillNumber01()))
			.add(appendValue("ご請求番号(2)", entity.getBillNumber02()))
			.add(appendValue("ご請求番号(3)", entity.getBillNumber03()))
			.add(appendValue("ご請求番号(4)", entity.getBillNumber04()))
			.add(appendValue("ご請求番号(5)", entity.getBillNumber05()))
			.add(appendValue("ご請求番号(6)", entity.getBillNumber06()))
			.add(appendValue("ご請求番号(7)", entity.getBillNumber07()))
			.add(appendValue("ご請求番号(8)", entity.getBillNumber08()))
			.add(appendValue("ご請求番号(9)", entity.getBillNumber09()))
			.add(appendValue("ご請求番号(10)", entity.getBillNumber10()))
			.add(appendValue("請求書送付先", StringUtils.EMPTY))
			.add(appendValue("住所コード", entity.getAddressCode()))
			.add(appendValue("郵便番号", entity.getPost()))
			.add(appendValue("都道府県市区町村", entity.getAddress1()))
			.add(appendValue("番地号", hasAddress2 ? entity.getAddress2() : StringUtils.EMPTY))
			.add(appendValue("ビル名・部屋番号", entity.getAddress3()))
			.add(appendValue("変更後の送付先名１", entity.getDestinationName()))
			.add(appendValue("変更後の送付先名２（部課名）", entity.getDestinationSectionName()))
			.add(appendValue("変更後の送付先名（カナ）", entity.getDestinationKana()))
			.add(appendValue("変更希望月", entity.getDateofchange()))
			.add(appendValue("法人/個人", StringUtils.EMPTY))
			.add(appendValue("会社名/団体名", entity.getApplyCorpname()))
			.add(appendValue("会社名/団体名フリガナ", StringUtils.EMPTY))
			.add(appendValue("お申し込み者名", entity.getApplyName()))
			.add(appendValue("お申し込み者名フリガナ", StringUtils.EMPTY))
			.add(appendValue("ご連絡先電話番号", entity.getTel()));

		String ret = sj.toString();
		logger.debug(ret);

		return ret;
	}

	private String appendValue(String title, String value) {
		return new StringJoiner(":").add(title).add(value).toString();
	}
}
