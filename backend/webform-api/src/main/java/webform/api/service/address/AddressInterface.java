package webform.api.service.address;

import java.util.List;
import java.util.Map;

import webform.api.dto.address.SearchAddressDto;

public interface AddressInterface {

	public Map<String, List<SearchAddressDto> > serch(String zipCode);

}
