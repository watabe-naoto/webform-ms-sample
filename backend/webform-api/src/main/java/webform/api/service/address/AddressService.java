package webform.api.service.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.protobuf.Timestamp;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import webform.api.dto.address.SearchAddressDto;
import webform.ms.grpc.address.Address4PostCodeReply;
import webform.ms.grpc.address.Address4PostCodeRequest;
import webform.ms.grpc.address.AddressGrpc;
import webform.ms.grpc.address.AddressGrpc.AddressBlockingStub;

//@RequestScoped
public class AddressService implements AddressInterface{

//    @GrpcClient("myService")
//    private MyServiceBlockingStub myServiceStub;

    public Map<String, List<SearchAddressDto> > serch(String zipCode) {
		System.out.println("call serch zipCode=" + zipCode);

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9898).usePlaintext().build();

		AddressBlockingStub addressServiceStub = AddressGrpc.newBlockingStub(channel);

		Address4PostCodeRequest request = Address4PostCodeRequest.newBuilder().setZipCode(zipCode).build();
		Iterator<Address4PostCodeReply> addressList = addressServiceStub.getAddress4PostCodeServerStreaming(request);

		Map<String, List<SearchAddressDto> > res = new HashMap<String, List<SearchAddressDto> >();
		List<SearchAddressDto> searchAddressDtoList = new ArrayList<SearchAddressDto>();

		int count = 0;
		while(addressList.hasNext()) {
			Address4PostCodeReply address = addressList.next();
			int id = address.getId();
			String todohukenCode = address.getTodohukenCode();
			String shikugunchosonCode = address.getShikugunchosonCode();
			String ohazatsushoCode = address.getOhazatsushoCode();
			String azachomeCode = address.getAzachomeCode();
			String kyuZipcd = address.getKyuZipcd();
			String todohukenName = address.getTodohukenName();
			String shikugunchosonName = address.getShikugunchosonName();
			String ohazatsushoName = address.getOhazatsushoName();
			String azachomeName = address.getAzachomeName();
			Timestamp insertDatetime = address.getInsertDatetime();
			String createUserId = address.getCreateUserId();

			System.out.println("address[" + count + "]" );
			System.out.println("  id=[" + id + "]");
			System.out.println("  todohukenCode=[" + todohukenCode + "]");
			System.out.println("  shikugunchosonCode=[" + shikugunchosonCode + "]");
			System.out.println("  ohazatsushoCode=[" + ohazatsushoCode + "]");
			System.out.println("  azachomeCode=[" + azachomeCode + "]");
			System.out.println("  kyuZipcd=[" + kyuZipcd + "]");
			System.out.println("  todohukenName=[" + todohukenName + "]");
			System.out.println("  shikugunchosonName=[" + shikugunchosonName + "]");
			System.out.println("  ohazatsushoName=[" + ohazatsushoName + "]");
			System.out.println("  azachomeName=[" + azachomeName + "]");
			System.out.println("  insertDatetime=[" + insertDatetime + "]");
			System.out.println("  createUserId=[" + createUserId + "]");
			
			searchAddressDtoList.add(this.address4PostCodeReplyToSearchAddressDto(address));
			
			count++;
		}

		res.put("result", searchAddressDtoList);

		System.out.println("serch end");
		return res;
	}
    
	private SearchAddressDto address4PostCodeReplyToSearchAddressDto(Address4PostCodeReply address) {
		// 住所コードを取得する
		StringBuilder addressCode = new StringBuilder();
		addressCode.append(address.getTodohukenCode());
		addressCode.append(address.getShikugunchosonCode());
		addressCode.append(address.getOhazatsushoCode());
		addressCode.append(address.getAzachomeCode());

		// 住所名を取得する
		String resultAddressString  = this.unionAddressString(address);
		// 取得結果をBeanに格納する
		return new SearchAddressDto(resultAddressString, addressCode.toString());
	}

	private String unionAddressString(Address4PostCodeReply address){
		// 置換対象の文字
		Pattern p = Pattern.compile("[　 ]");
		// 置換後の文字
		String empty = "";

		return this.unionAddressString(
				p.matcher(address.getTodohukenName()).replaceAll(empty),
				p.matcher(address.getShikugunchosonName()).replaceAll(empty),
				p.matcher(address.getOhazatsushoName()).replaceAll(empty),
				p.matcher(address.getAzachomeName()).replaceAll(empty));
	}

	private String unionAddressString(String todohukenName, String shikugunchosonName, String ohazatsushoName,
			String azachomeName) {

		// 住所名を取得する
		StringBuilder addressName = new StringBuilder();
		boolean spaceFlag = false;

		if (StringUtils.isNotEmpty(todohukenName)) {
			addressName.append(todohukenName);
			spaceFlag = true;
		}
		if (StringUtils.isNotEmpty(shikugunchosonName)) {
			addressName.append(shikugunchosonName);
			spaceFlag = true;
		}
		if (StringUtils.isNotEmpty(ohazatsushoName)) {
			addressName.append(ohazatsushoName);
			spaceFlag = true;
		}

		if (StringUtils.isNotEmpty(azachomeName)) {
			if (spaceFlag) {
				addressName.append("　");
			}
			addressName.append(azachomeName);
		}

		return addressName.toString();
	}

}
