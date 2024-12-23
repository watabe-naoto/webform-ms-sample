package webform.ms.service;

import java.time.ZoneId;
import java.util.List;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Timestamp;

import io.grpc.stub.StreamObserver;
import webform.ms.crud.entity.ExternalSystemRelationInfo;
import webform.ms.crud.repository.ExternalSystemRelationInfoRepository;
import webform.ms.grpc.external_system.ExternalSystemRelationInfo4ExternalSystemIdReply;
import webform.ms.grpc.external_system.ExternalSystemRelationInfo4ExternalSystemIdRequest;
import webform.ms.grpc.external_system.ExternalSystemRelationInfoGrpc;

@GRpcService
public class ExternalSystemRelationInfoGRpcServiceImpl extends ExternalSystemRelationInfoGrpc.ExternalSystemRelationInfoImplBase {

	@Autowired
	private ExternalSystemRelationInfoRepository repository;

	@Override
	public void getExternalSystemInfo4ExternalSystemIdServerStreaming(ExternalSystemRelationInfo4ExternalSystemIdRequest request, StreamObserver<ExternalSystemRelationInfo4ExternalSystemIdReply> responseObserver) {
		System.out.println("orderformType=[" + request.getOrderformType() + "]");

		List<ExternalSystemRelationInfo> externalSystemRelationInfoList = repository.getExternalSystemRelationInfoByOrderformTypeEquals(request.getOrderformType());
		
		for(ExternalSystemRelationInfo externalSystemRelationInfo : externalSystemRelationInfoList){
			ExternalSystemRelationInfo4ExternalSystemIdReply reply = ExternalSystemRelationInfo4ExternalSystemIdReply.newBuilder()
					.setId(externalSystemRelationInfo.getId())
					.setOrderformType(externalSystemRelationInfo.getOrderform_type())
					.setExternalSystemId(externalSystemRelationInfo.getExternal_system_id())
					.setAddUser(externalSystemRelationInfo.getAdd_user())
					.setAddDate(Timestamp.newBuilder().setSeconds(externalSystemRelationInfo.getAdd_date().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
					.build();
			responseObserver.onNext(reply);
		}
		responseObserver.onCompleted();
	}
}