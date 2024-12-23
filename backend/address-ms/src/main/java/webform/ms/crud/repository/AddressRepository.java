package webform.ms.crud.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import webform.ms.crud.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    @Query("SELECT * FROM address WHERE kyu_zipcd = :postCode ORDER BY todohuken_code, shikugunchoson_code, ohazatsusho_code, azachome_code ASC;")
    List<Address> getAddressByPostCodeEquals(String postCode);
}