package webform.api.dto.address;

import java.io.Serializable;

public class SearchAddressDto implements Serializable {

	private static final long serialVersionUID = -7535522556701328851L;

	private String address = null;

	private String addressCode = null;

	public SearchAddressDto() {
	}

	public SearchAddressDto(String address, String addressCode) {
		this.address = address;
		this.addressCode = addressCode;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressCode() {
		return this.addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

}
