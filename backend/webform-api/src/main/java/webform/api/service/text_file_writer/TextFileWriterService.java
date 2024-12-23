package webform.api.service.text_file_writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import webform.api.dto.text_file_writer.EncryptTextFileInfo;
import webform.api.dto.text_file_writer.TextFileInfo;
import webform.api.dto.text_file_writer.WriteEncryptionFileRequestDto;
import webform.api.dto.text_file_writer.WriteEncryptionFileResponseDto;
import webform.api.dto.text_file_writer.WriteFileRequestDto;
import webform.api.dto.text_file_writer.WriteFileResponseDto;
import webform.ms.grpc.text_file_writer.TextFileWriterGrpc;
import webform.ms.grpc.text_file_writer.TextFileWriterGrpc.TextFileWriterBlockingStub;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileReply;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileRequest;
import webform.ms.grpc.text_file_writer.WriteFileReply;
import webform.ms.grpc.text_file_writer.WriteFileRequest;

/**
 * テキストファイル書込みServiceクラス.
 *
 */
public class TextFileWriterService {
	/** Log */
	private Log log = LogFactory.getLog(getClass());

	public TextFileWriterService() {
	}

	/**
	 * ファイル書込み.
	 *
	 * @return 結果BEAN
	 */
	public WriteFileResponseDto writeFile(WriteFileRequestDto req) {
		log.debug("textFileInfoList=" + (req.getTextFileInfoList() == null ? null : req.getTextFileInfoList()));

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9899).usePlaintext().build();

		TextFileWriterBlockingStub textFileWriterServiceStub = TextFileWriterGrpc.newBlockingStub(channel);

		ArrayList<webform.ms.grpc.text_file_writer.TextFileInfo> list = new ArrayList<webform.ms.grpc.text_file_writer.TextFileInfo>();
		List <TextFileInfo> textFileInfoList = req.getTextFileInfoList();
		for(TextFileInfo textFileInfo : textFileInfoList) {
			webform.ms.grpc.text_file_writer.TextFileInfo info = webform.ms.grpc.text_file_writer.TextFileInfo.newBuilder()
					.setFilePath(textFileInfo.getFilePath())
					.setBackupFilePath(textFileInfo.getBackupFilePath())
					.setFileName(textFileInfo.getFileName())
					.setCharsetName(textFileInfo.getCharsetName())
					.setText(textFileInfo.getText())
					.build();

			list.add(info);
		}
		WriteFileRequest request = WriteFileRequest.newBuilder()
				.addAllTextFileInfo(list)
				.build();
		WriteFileReply writeFileReply = textFileWriterServiceStub.writeFileServerStreaming(request);

		WriteFileResponseDto dto = new WriteFileResponseDto();
		dto.setResultCode(writeFileReply.getResultCode());
		return dto;
	}

	/**
	 * ファイル書込み（暗号化有無指定あり）.
	 *
	 * @return 結果BEAN
	 */
	public WriteEncryptionFileResponseDto writeEncryptionFile(WriteEncryptionFileRequestDto req) {
		log.debug("textFileInfoList=" + (req.getTextFileInfoList() == null ? null : req.getTextFileInfoList()));

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9899).usePlaintext().build();

		TextFileWriterBlockingStub textFileWriterServiceStub = TextFileWriterGrpc.newBlockingStub(channel);

		ArrayList<webform.ms.grpc.text_file_writer.EncryptTextFileInfo> list = new ArrayList<webform.ms.grpc.text_file_writer.EncryptTextFileInfo>();
		List <EncryptTextFileInfo> textFileInfoList = req.getTextFileInfoList();
		for(EncryptTextFileInfo encryptTextFileInfo : textFileInfoList) {
			webform.ms.grpc.text_file_writer.EncryptTextFileInfo info = webform.ms.grpc.text_file_writer.EncryptTextFileInfo.newBuilder()
					.setFilePath(encryptTextFileInfo.getFilePath())
					.setBackupFilePath((encryptTextFileInfo.getBackupFilePath() == null ? "" : encryptTextFileInfo.getBackupFilePath()))
					.setFileName(encryptTextFileInfo.getFileName())
					.setCharsetName(encryptTextFileInfo.getCharsetName())
					.setText(encryptTextFileInfo.getText())
					.setEncryptionCommandName((encryptTextFileInfo.getEncryptionCommandName() == null ? "" : encryptTextFileInfo.getEncryptionCommandName()))
					.build();

			list.add(info);
		}
		WriteEncryptionFileRequest request = WriteEncryptionFileRequest.newBuilder()
				.addAllTextFileInfo(list)
				.build();
		WriteEncryptionFileReply writeEncryptionFileReply = textFileWriterServiceStub.writeEncryptionFileServerStreaming(request);

		WriteEncryptionFileResponseDto dto = new WriteEncryptionFileResponseDto();
		dto.setResultCode(writeEncryptionFileReply.getResultCode());
		return dto;
	}

}
