package webform.api;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import webform.api.provider.MarshallingFeature;
import webform.api.service.address.AddressService;
import webform.api.service.business_address.BusinessAddressService;
import webform.api.service.common.AvailableIpListService;
import webform.api.service.common.GlobalAddressService;
import webform.api.service.external_system.ExternalsysteminfoService;
import webform.api.service.external_system.ExternalsystemrelationinfoService;
import webform.api.service.text_file_writer.TextFileWriterService;

/**
 * DIコンテナーに動的にクラスリソースを登録するJAX-RSアプリケーション
 * jerseyが登録している既知の@Contextおよびここで追加した型（HttpSession等）に依存するクラスはここでバインドすること。
 *
 * JSR-330の実装であるHK2を利用して、実行時に動的に検出でき、制御の反転（IoC）および依存性注入（DI）を可能にするサービスおよび注入ポイントの設定を実装する。
 */
@ApplicationPath("api")
public class WebformApiResourceConfig extends ResourceConfig {

	@Inject
	public WebformApiResourceConfig() {
		super(MultiPartFeature.class);

		packages(this.getClass().getPackage().getName());

		register(MarshallingFeature.class);
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				// コントラクトタイプとしてバインドする。
//				bindAsContract(StartEndLogInterceptor.class);
//				bindAsContract(PackageScopeInterceptorService.class).to(InterceptionService.class).in(Singleton.class);
//				bindAsContract(BadRequestInterceptor.class);
//				bindAsContract(BadRequestInterceptorService.class).to(InterceptionService.class).in(Singleton.class);

				bindAsContract(AvailableIpListService.class);
				bindAsContract(GlobalAddressService.class);

				bindAsContract(AddressService.class);
				bindAsContract(BusinessAddressService.class);
				bindAsContract(ExternalsystemrelationinfoService.class);
				bindAsContract(ExternalsysteminfoService.class);

				bindAsContract(TextFileWriterService.class);
			}
		});
	}
}
