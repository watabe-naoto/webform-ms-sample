package webform.ms.service;

import java.time.ZoneId;
import java.util.List;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Timestamp;

import io.grpc.stub.StreamObserver;
import webform.ms.crud.entity.Address;
import webform.ms.crud.repository.AddressRepository;
import webform.ms.grpc.address.Address4PostCodeReply;
import webform.ms.grpc.address.Address4PostCodeRequest;
import webform.ms.grpc.address.AddressGrpc;

@GRpcService
public class AddressGRpcServiceImpl extends AddressGrpc.AddressImplBase {

	private final Logger logger = LoggerFactory.getLogger(AddressGRpcServiceImpl.class);

	@Autowired
	private AddressRepository repository;

	@Override
	public void getAddress4PostCodeServerStreaming(Address4PostCodeRequest request, StreamObserver<Address4PostCodeReply> responseObserver) {
		logger.info("zipCode=[" + request.getZipCode() + "]");
		
		List<Address> addressList = repository.getAddressByPostCodeEquals(request.getZipCode());
		
		for(Address address : addressList){
			Address4PostCodeReply reply = Address4PostCodeReply.newBuilder()
					.setId(address.getId())
					.setTodohukenCode(address.getTodohuken_code())
					.setShikugunchosonCode(address.getShikugunchoson_code())
					.setOhazatsushoCode(address.getOhazatsusho_code())
					.setAzachomeCode(address.getAzachome_code())
					.setKyuZipcd(address.getKyu_zipcd())
					.setTodohukenName(address.getTodohuken_name())
					.setShikugunchosonName(address.getShikugunchoson_name())
					.setOhazatsushoName(address.getOhazatsusho_name())
					.setInsertDatetime(Timestamp.newBuilder().setSeconds(address.getInsert_datetime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
					.setAzachomeName(address.getAzachome_name())
					.setCreateUserId(address.getCreate_user_id())
					.build();
			responseObserver.onNext(reply);
		}
		responseObserver.onCompleted();
	}
}