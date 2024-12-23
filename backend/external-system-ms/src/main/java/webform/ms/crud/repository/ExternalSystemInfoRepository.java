package webform.ms.crud.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import webform.ms.crud.entity.ExternalSystemInfo;

public interface ExternalSystemInfoRepository extends CrudRepository<ExternalSystemInfo, Integer> {
    @Query("SELECT * FROM external_system_info WHERE external_system_id IN (:externalSystemIds) AND external_system_status != 1 ORDER BY external_system_id ASC;")
    List<ExternalSystemInfo> getExternalSystemInfoByExternalSystemIdsEquals(List<Integer> externalSystemIds);
}