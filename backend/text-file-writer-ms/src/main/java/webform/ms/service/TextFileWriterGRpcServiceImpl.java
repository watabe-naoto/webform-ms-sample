package webform.ms.service;

import java.util.List;

import org.lognet.springboot.grpc.GRpcService;

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

//	@Autowired
//	private AddressRepository repository;

	@Override
	public void writeFileServerStreaming(WriteFileRequest request, StreamObserver<WriteFileReply> responseObserver) {
		List<TextFileInfo> textFileInfoList = request.getTextFileInfoList();
		int count = 0;
		for(TextFileInfo textFileInfo : textFileInfoList) {
			System.out.println("textFileInfo[" + count + "]");
			System.out.println("  filePath=[" + textFileInfo.getFilePath() + "]");
			System.out.println("  backupFilePath=[" + textFileInfo.getBackupFilePath() + "]");
			System.out.println("  fileName=[" + textFileInfo.getFileName() + "]");
			System.out.println("  charsetName=[" + textFileInfo.getCharsetName() + "]");
			System.out.println("  text=[" + textFileInfo.getText() + "]");
			count++;
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
		int count = 0;
		for(EncryptTextFileInfo encryptTextFileInfo : encryptTextFileInfoList) {
			System.out.println("encryptTextFileInfo[" + count + "]");
			System.out.println("  filePath=[" + encryptTextFileInfo.getFilePath() + "]");
			System.out.println("  backupFilePath=[" + encryptTextFileInfo.getBackupFilePath() + "]");
			System.out.println("  fileName=[" + encryptTextFileInfo.getFileName() + "]");
			System.out.println("  charsetName=[" + encryptTextFileInfo.getCharsetName() + "]");
			System.out.println("  text=[" + encryptTextFileInfo.getText() + "]");
			System.out.println("  encryptionCommandName=[" + encryptTextFileInfo.getEncryptionCommandName() + "]");
			count++;
		}
		
		// factory呼び出し
		TextFileWriterFactory factory = TextFileWriterFactory.getInstance();
		WriteEncryptionFileReply reply = factory.writeEncryptionFile(request);

		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}