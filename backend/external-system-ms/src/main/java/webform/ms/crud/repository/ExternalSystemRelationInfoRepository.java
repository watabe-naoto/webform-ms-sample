package webform.ms.crud.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import webform.ms.crud.entity.ExternalSystemRelationInfo;

public interface ExternalSystemRelationInfoRepository extends CrudRepository<ExternalSystemRelationInfo, Integer> {
    @Query("SELECT * FROM external_system_relation_info WHERE orderform_type = :orderformType ORDER BY external_system_id ASC;")
    List<ExternalSystemRelationInfo> getExternalSystemRelationInfoByOrderformTypeEquals(String orderformType);
}