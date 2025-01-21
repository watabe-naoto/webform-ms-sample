package webform.api.resource.address;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.process.internal.RequestScoped;

import webform.api.dto.address.SearchAddressDto;
import webform.api.service.address.AddressService;

@RequestScoped
@Path("address")
public class AddressResource {

	private final Logger logger = LogManager.getLogger(AddressResource.class);
	
	@Inject
	private AddressService addressService;

	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@GET
	@Path("4PostCode")
	public Map<String, List<SearchAddressDto> > getAddress4PostCode(@QueryParam("zipCode") String zipCode) {
		logger.info("call getAddress4PostCode zipCode=" + zipCode);

//		AddressService addressService =new AddressService();

		Map<String, List<SearchAddressDto> > res = this.addressService.serch(zipCode);

		logger.info("getAddress4PostCode end");
		return res;
	}

}
