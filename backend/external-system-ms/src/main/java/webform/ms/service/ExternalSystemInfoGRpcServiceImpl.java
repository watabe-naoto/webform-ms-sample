package webform.ms.service;

import java.time.ZoneId;
import java.util.List;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Timestamp;

import io.grpc.stub.StreamObserver;
import webform.ms.crud.entity.ExternalSystemInfo;
import webform.ms.crud.repository.ExternalSystemInfoRepository;
import webform.ms.grpc.external_system.ExternalSystemInfo4ExternalSystemIdReply;
import webform.ms.grpc.external_system.ExternalSystemInfo4ExternalSystemIdRequest;
import webform.ms.grpc.external_system.ExternalSystemInfoGrpc;

@GRpcService
public class ExternalSystemInfoGRpcServiceImpl extends ExternalSystemInfoGrpc.ExternalSystemInfoImplBase {

	@Autowired
	private ExternalSystemInfoRepository repository;

	@Override
	public void getExternalSystemInfo4ExternalSystemIdServerStreaming(ExternalSystemInfo4ExternalSystemIdRequest request, StreamObserver<ExternalSystemInfo4ExternalSystemIdReply> responseObserver) {
		System.out.println("externalSystemIds=[" + request.getExternalSystemIdsList() + "]");

		List<ExternalSystemInfo> externalSystemInfoList = repository.getExternalSystemInfoByExternalSystemIdsEquals(request.getExternalSystemIdsList());
		
		for(ExternalSystemInfo externalSystemInfo : externalSystemInfoList){
			ExternalSystemInfo4ExternalSystemIdReply reply = ExternalSystemInfo4ExternalSystemIdReply.newBuilder()
					.setId(externalSystemInfo.getId())
					.setExternalSystemId(externalSystemInfo.getExternal_system_id())
					.setExternalSystemName(externalSystemInfo.getExternal_system_name())
					.setExternalSystemStatus(externalSystemInfo.getExternal_system_status())
					.setUpdateDatetime(Timestamp.newBuilder().setSeconds(externalSystemInfo.getUpdate_datetime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
					.setUpdateUser(externalSystemInfo.getUpdate_user())
					.setStopDatetime(Timestamp.newBuilder().setSeconds(externalSystemInfo.getStop_datetime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
					.setRebootDatetime(Timestamp.newBuilder().setSeconds(externalSystemInfo.getReboot_datetime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
					.setMessage(externalSystemInfo.getMessage())
					.build();
			responseObserver.onNext(reply);
		}
		responseObserver.onCompleted();
	}
}