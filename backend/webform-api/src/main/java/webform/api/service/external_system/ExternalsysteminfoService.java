package webform.api.service.external_system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import webform.api.dto.external_system.VExternalSystemInfo;
import webform.api.util.DateUtil;
import webform.ms.grpc.external_system.ExternalSystemInfo4ExternalSystemIdReply;
import webform.ms.grpc.external_system.ExternalSystemInfo4ExternalSystemIdRequest;
import webform.ms.grpc.external_system.ExternalSystemInfoGrpc;
import webform.ms.grpc.external_system.ExternalSystemInfoGrpc.ExternalSystemInfoBlockingStub;


/**
 * 外部システム情報検索Serviceクラス
 *
 */
public class ExternalsysteminfoService {
	/** Log */
	private final Logger logger = LogManager.getLogger(ExternalsysteminfoService.class);

	/**
	 * コンストラクタ
	 */
	public ExternalsysteminfoService() {
	}

	/**
	 * 外部システム利用不可時エラーメッセージ文言取得
	 *
	 * 複数の外部システムIDから外部システムステータスが利用可能であるか判定し利用不可ならばエラーメッセージを作成する
	 *
	 * @param externalSystemIds
	 *            外部システムID
	 * @return String 画面表示用エラーメッセージ文言
	 */
	public String isSystemAvailable(int[] externalSystemIds) {
		logger.info("isSystemAvailable Start.");

		// 0件ならば常に稼働
		if (externalSystemIds.length == 0) {
			return null;
		}

		List<Integer> externalSystemIdsList= new ArrayList<Integer>();
		for(int externalSystemId: externalSystemIds) {
			externalSystemIdsList.add(externalSystemId);
		}

		// 検索条件設定
		// where (systemid = id[0] and status <> 1) or (systemid = id[1] and
		// status <> 1) or ...
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9898).usePlaintext().build();

		ExternalSystemInfoBlockingStub externalSystemInfoServiceStub = ExternalSystemInfoGrpc.newBlockingStub(channel);

		ExternalSystemInfo4ExternalSystemIdRequest request = ExternalSystemInfo4ExternalSystemIdRequest.newBuilder()
				.addAllExternalSystemIds(externalSystemIdsList)
				.build();

		// MS実行
		Iterator<ExternalSystemInfo4ExternalSystemIdReply> externalSystemInfoList = externalSystemInfoServiceStub.getExternalSystemInfo4ExternalSystemIdServerStreaming(request);

		List<VExternalSystemInfo> resultList = new ArrayList<VExternalSystemInfo>();
        while (externalSystemInfoList.hasNext()) {
        	VExternalSystemInfo externalSystemInfo = new VExternalSystemInfo();
        	ExternalSystemInfo4ExternalSystemIdReply info= externalSystemInfoList.next();
        	externalSystemInfo.setId(info.getId());
        	resultList.add(externalSystemInfo);
        }
        
		List<VExternalSystemInfo> editedResultList = editDisplayMaintenanceTime(resultList);
		// 0件ならば稼働中
		if (editedResultList.size() == 0) {
			return null;
		}

		// 停止時間の昇順に並べ替え
		Collections.sort(editedResultList, new Comparator<VExternalSystemInfo>() {
			public int compare(VExternalSystemInfo o1, VExternalSystemInfo o2) {
				return o1.getStopDatetime().compareTo(o2.getStopDatetime());
			}
		});
		LocalDateTime stopDatetime = DateUtil.convertLocalDateTimeFromDate(editedResultList.get(0).getStopDatetime());
		String message = editedResultList.get(0).getMessage();

		// 再稼動時間の降順に並べ替え
		Collections.sort(editedResultList, new Comparator<VExternalSystemInfo>() {
			public int compare(VExternalSystemInfo o1, VExternalSystemInfo o2) {
				return o1.getRebootDatetime().compareTo(o2.getRebootDatetime()) * -1;
			}
		});
		LocalDateTime rebootDatetime = DateUtil.convertLocalDateTimeFromDate(editedResultList.get(0).getRebootDatetime());

		message = editMaintenanceMesseage(message, stopDatetime, rebootDatetime);

		for (VExternalSystemInfo info : editedResultList) {
			logger.info(info.getExternalSystemName() + " is down. maintenance time: " + info.getStopDatetime().toString()
					+ " - " + info.getRebootDatetime().toString());
		}

		return message;
	}

	/**
	 * 表示メンテナンス時間編集
	 *
	 * @param list
	 * @return list
	 */
	private List<VExternalSystemInfo> editDisplayMaintenanceTime(List<VExternalSystemInfo> list) {
		logger.info("editDisplayMaintenanceTime Start");
		// リストが空ならば何もしない
		if (list.size() == 0) {
			return list;
		}

		List<VExternalSystemInfo> editedList = new ArrayList<VExternalSystemInfo>();
		// 現在時刻の取得
		Date currentTime = Calendar.getInstance().getTime();
		for (VExternalSystemInfo info : list) {
			if (info.getStopDatetime() != null && info.getRebootDatetime() != null
					&& (info.getStopDatetime().compareTo(currentTime) <= 0
							&& info.getRebootDatetime().compareTo(currentTime) >= 0)) {
				// 現在時刻がメンテ時間範囲内の時、リストに追加
				editedList.add(info);
			}
		}

		return editedList;
	}

	/**
	 * システムメンテナンスメッセージ編集
	 *
	 * @param message
	 * @param stopDatetime
	 * @param rebootDatetime
	 * @return String
	 */
	private String editMaintenanceMesseage(String message, LocalDateTime stopDatetime, LocalDateTime rebootDatetime) {
		logger.info("editMaintenanceMesseage Start");

		// メンテナンス開始時刻の編集
		message = message.replace("[0]", stopDatetime.format(DateUtil.format_uuuuMMddEaKmm));

		if (stopDatetime.getDayOfYear() != rebootDatetime.getDayOfYear()
				|| stopDatetime.getYear() != rebootDatetime.getYear()) {
			// メンテナンス終了時刻（年月日曜日時分）の編集
			message = message.replace("[1]", rebootDatetime.format(DateUtil.format_uuuuMMddEaKmm));
		} else {
			// メンテナンス終了時刻（時分）の編集
			message = message.replace("[1]", rebootDatetime.format(DateUtil.format_aKmm));
		}

		return message;
	}
}
