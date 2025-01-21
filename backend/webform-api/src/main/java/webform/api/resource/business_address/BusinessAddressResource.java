package webform.api.resource.business_address;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import webform.api.constant.business_address.BusinessAddressConstant;
import webform.api.constant.prop.MatchCountConstant;
import webform.api.constant.prop.MatchMailConstant;
import webform.api.constant.prop.ThanksMailConstant;
import webform.api.dto.business_address.BusinessAddressEditEntity;
import webform.api.dto.text_file_writer.EncryptTextFileInfo;
import webform.api.dto.text_file_writer.WriteEncryptionFileRequestDto;
import webform.api.dto.text_file_writer.WriteEncryptionFileResponseDto;
import webform.api.prop.EnvironmentSettingProperties;
import webform.api.service.business_address.BusinessAddressService;
import webform.api.service.text_file_writer.TextFileWriterService;

@Path("business_address")
public class BusinessAddressResource {
	/** Log */
	private final Logger logger = LogManager.getLogger(BusinessAddressResource.class);

	//@Inject
	//private BusinessAddressService service;
	@Inject
	private TextFileWriterService writerService;

//	public BusinessAddressResource() {
//		this.writerService = new TextFileWriterService();
//	}

	/**
	 * 請求書送付先変更のお申し込み
	 *
	 * @param billingId
	 *            請求番号
	 * @return サンキューメールCSV
	 */
	@Path("/order")
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public WriteEncryptionFileResponseDto order(@Context HttpHeaders headers, BusinessAddressEditEntity entity) {
		logger.info("BusinessAddressResource#order Start");
		// パラメタチェック
// パラメータチェックはアノテーション実装しない処理に変更が必要
//		BeanValidateUtil.validateBean(entity);
		// ReqformApiResourceConfigでInjectすると申込カウンターが上がる為、インスタンスを生成する
		BusinessAddressService service = new BusinessAddressService();
		// FTP CSV出力
		this.writeFtp(entity, service);
		// MatchMailCSV出力
		this.writeMatch(entity, service);
		// サンキューメール出力
		WriteEncryptionFileResponseDto res = this.writeThankYou(entity, service);
		logger.info("BusinessAddressResource#order End");
		return res;
	}

	/**
	 * FTPCSVファイルを作成。
	 *
	 * @param entity
	 * @param service
	 * @param checkResult
	 * @return
	 */
	private WriteEncryptionFileResponseDto writeFtp(BusinessAddressEditEntity entity, BusinessAddressService service) {
		String filePath = new String(
				EnvironmentSettingProperties.getEnvProp(BusinessAddressConstant.PROP_FTP_FILE_PATH));
		logger.info("filePath：" + filePath);

		WriteEncryptionFileRequestDto req = new WriteEncryptionFileRequestDto();

		// FTPCSV
		EncryptTextFileInfo csvFileInfo = new EncryptTextFileInfo();
		csvFileInfo.setFilePath(filePath);
		csvFileInfo.setFileName(service.createFtpCsvFileName());
		csvFileInfo.setCharsetName(EncryptTextFileInfo.CHARSET_NAME_SJIS);
		csvFileInfo.setText(service.createFtpCsv(entity));
		csvFileInfo.setEncryptionCommandName(EncryptTextFileInfo.FOCUS_COMAND);
		req.getTextFileInfoList().add(csvFileInfo);

		return writerService.writeEncryptionFile(req);
	}

	/**
	 * マッチメールファイルを作成。
	 *
	 * @param entity
	 * @param service
	 * @return
	 */
	private WriteEncryptionFileResponseDto writeMatch(BusinessAddressEditEntity entity,
			BusinessAddressService service) {
		String filePath = new String(
				EnvironmentSettingProperties
						.getEnvProp(MatchCountConstant.PROP_MATCH_COUNT_BUSINESS_ADDRESS_EDIT_FILE_PATH));
		logger.info("filePath：" + filePath);

		WriteEncryptionFileRequestDto req = new WriteEncryptionFileRequestDto();

		// マッチカウントCSV
		EncryptTextFileInfo matchCountCsvFileInfo = new EncryptTextFileInfo();
		matchCountCsvFileInfo.setFilePath(filePath);
		matchCountCsvFileInfo.setFileName(service.createMatchCountCsvFileName());
		matchCountCsvFileInfo.setCharsetName(EncryptTextFileInfo.CHARSET_NAME_JIS);
		matchCountCsvFileInfo.setText(service.createMatchCountCsv(entity));
		req.getTextFileInfoList().add(matchCountCsvFileInfo);

		filePath = new String(
				EnvironmentSettingProperties.getEnvProp(MatchMailConstant.PROP_MATCH_MAIL_FILE_PATH));
		// マッチメールCSV
		EncryptTextFileInfo matchMailCsvFileInfo = new EncryptTextFileInfo();
		matchMailCsvFileInfo.setFilePath(filePath);
		matchMailCsvFileInfo.setFileName(service.createMatchMailCsvFileName());
		matchMailCsvFileInfo.setCharsetName(EncryptTextFileInfo.CHARSET_NAME_JIS);
		matchMailCsvFileInfo.setText(service.createMatchMailCsv(entity));
		matchMailCsvFileInfo.setEncryptionCommandName(EncryptTextFileInfo.FOCUS_COMAND);
		req.getTextFileInfoList().add(matchMailCsvFileInfo);
		return writerService.writeEncryptionFile(req);
	}

	/**
	 * サンキューメールファイルを作成。
	 *
	 * @param entity
	 * @param service
	 * @param checkResult
	 * @return
	 */
	private WriteEncryptionFileResponseDto writeThankYou(BusinessAddressEditEntity entity,
			BusinessAddressService service) {
		String filePath = new String(
				EnvironmentSettingProperties.getEnvProp(ThanksMailConstant.PROP_THANKS_OUTPUT_PATH));
		logger.info("filePath：" + filePath);

		WriteEncryptionFileRequestDto req = new WriteEncryptionFileRequestDto();

		// サンキューメールCSV
		EncryptTextFileInfo thanksmMailCsvFileInfo = new EncryptTextFileInfo();
		thanksmMailCsvFileInfo.setFilePath(filePath);
		thanksmMailCsvFileInfo.setFileName(service.createThanksmMailCsvFileName());
		thanksmMailCsvFileInfo.setCharsetName("EUC-JP");
		thanksmMailCsvFileInfo.setText(service.createThanksmMailCsv(entity));
		thanksmMailCsvFileInfo.setEncryptionCommandName(EncryptTextFileInfo.FOCUS_COMAND);
		req.getTextFileInfoList().add(thanksmMailCsvFileInfo);

		return writerService.writeEncryptionFile(req);
	}
}
