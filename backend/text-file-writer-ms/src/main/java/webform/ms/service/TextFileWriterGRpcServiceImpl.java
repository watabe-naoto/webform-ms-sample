package webform.ms.service;

import java.util.List;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import webform.ms.grpc.text_file_writer.EncryptTextFileInfo;
import webform.ms.grpc.text_file_writer.TextFileInfo;
import webform.ms.grpc.text_file_writer.TextFileWriterGrpc;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileReply;
import webform.ms.grpc.text_file_writer.WriteEncryptionFileRequest;
import webform.ms.grpc.text_file_writer.WriteFileReply;
import webform.ms.grpc.text_file_writer.WriteFileRequest;

@GRpcService
public class TextFileWriterGRpcServiceImpl extends TextFileWriterGrpc.TextFileWriterImplBase {

	private final Logger logger = LoggerFactory.getLogger(TextFileWriterFactory.class);

	//	@Autowired
//	private AddressRepository repository;

	@Override
	public void writeFileServerStreaming(WriteFileRequest request, StreamObserver<WriteFileReply> responseObserver) {
		List<TextFileInfo> textFileInfoList = request.getTextFileInfoList();
		if(logger.isDebugEnabled()) {
			int count = 0;
			for(TextFileInfo textFileInfo : textFileInfoList) {
				logger.debug("textFileInfo[" + count + "]");
				logger.debug("  filePath=[" + textFileInfo.getFilePath() + "]");
				logger.debug("  backupFilePath=[" + textFileInfo.getBackupFilePath() + "]");
				logger.debug("  fileName=[" + textFileInfo.getFileName() + "]");
				logger.debug("  charsetName=[" + textFileInfo.getCharsetName() + "]");
				logger.debug("  text=[" + textFileInfo.getText() + "]");
				count++;
			}
		}

		// factory呼び出し
		TextFileWriterFactory factory = TextFileWriterFactory.getInstance();
		WriteFileReply reply = factory.writeFile(request);

		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	@Override
	public void writeEncryptionFileServerStreaming(WriteEncryptionFileRequest request, StreamObserver<WriteEncryptionFileReply> responseObserver) {
		List<EncryptTextFileInfo> encryptTextFileInfoList = request.getTextFileInfoList();
		if(logger.isDebugEnabled()) {
			int count = 0;
			for(EncryptTextFileInfo encryptTextFileInfo : encryptTextFileInfoList) {
				logger.debug("encryptTextFileInfo[" + count + "]");
				logger.debug("  filePath=[" + encryptTextFileInfo.getFilePath() + "]");
				logger.debug("  backupFilePath=[" + encryptTextFileInfo.getBackupFilePath() + "]");
				logger.debug("  fileName=[" + encryptTextFileInfo.getFileName() + "]");
				logger.debug("  charsetName=[" + encryptTextFileInfo.getCharsetName() + "]");
				logger.debug("  text=[" + encryptTextFileInfo.getText() + "]");
				logger.debug("  encryptionCommandName=[" + encryptTextFileInfo.getEncryptionCommandName() + "]");
				count++;
			}
		}
		
		// factory呼び出し
		TextFileWriterFactory factory = TextFileWriterFactory.getInstance();
		WriteEncryptionFileReply reply = factory.writeEncryptionFile(request);

		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}