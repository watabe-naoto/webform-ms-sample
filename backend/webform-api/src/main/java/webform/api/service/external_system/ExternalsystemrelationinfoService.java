package webform.api.service.external_system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import webform.ms.grpc.external_system.ExternalSystemRelationInfo4ExternalSystemIdReply;
import webform.ms.grpc.external_system.ExternalSystemRelationInfo4ExternalSystemIdRequest;
import webform.ms.grpc.external_system.ExternalSystemRelationInfoGrpc;
import webform.ms.grpc.external_system.ExternalSystemRelationInfoGrpc.ExternalSystemRelationInfoBlockingStub;


/**
 * 外部システム情報検索Serviceクラス
 *
 */
public class ExternalsystemrelationinfoService {
	/** Log */
	private final Logger logger = LogManager.getLogger(ExternalsystemrelationinfoService.class);

	/**
	 * コンストラクタ
	 */
	public ExternalsystemrelationinfoService() {
	}

	/**
	 * 関連外部連携先マスタ検索
	 *
	 * 関連外部連携先の外部システムIDの取得
	 *
	 * @param orderformType
	 *            導線種別
	 * @return 外部システムステータス（0：停止、1：起動） 該当データ無しなら-1
	 */
	public int[] selectSystemId(String orderformType) {
		logger.info("ExternalsystemrelationinfoService.selectSystemId Start.");

		// 検索条件設定
		// WHERE orderform_type = :orderformType
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9898).usePlaintext().build();

		ExternalSystemRelationInfoBlockingStub externalSystemRelationInfoServiceStub = ExternalSystemRelationInfoGrpc.newBlockingStub(channel);

		ExternalSystemRelationInfo4ExternalSystemIdRequest request = ExternalSystemRelationInfo4ExternalSystemIdRequest.newBuilder()
				.setOrderformType(orderformType)
				.build();

		// MS実行
		Iterator<ExternalSystemRelationInfo4ExternalSystemIdReply> externalSystemInfoList = externalSystemRelationInfoServiceStub.getExternalSystemInfo4ExternalSystemIdServerStreaming(request);

		List<ExternalSystemRelationInfo4ExternalSystemIdReply> resultList = new ArrayList<ExternalSystemRelationInfo4ExternalSystemIdReply>();
        while (externalSystemInfoList.hasNext()) {
        	resultList.add(externalSystemInfoList.next());
        }

		int[] results = new int[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			results[i] = resultList.get(i).getExternalSystemId();
		}
		return results;
	}
}
