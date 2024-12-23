package webform.api.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Rest用{@link JacksonJsonProvider}
 * (リクエストされたJSONを読むのに失敗した場合はステータスコード400を返す。)
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonJsonProviderAtRest extends JacksonJaxbJsonProvider {

	/** Log */
	private Log log = LogFactory.getLog(getClass());

	private static ObjectMapper objectMapperAtRest = new ObjectMapper();

	static {
		objectMapperAtRest.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapperAtRest.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapperAtRest.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapperAtRest.setSerializationInclusion(JsonInclude.Include.ALWAYS);
	}

	public JacksonJsonProviderAtRest() {
		super();
		setMapper(objectMapperAtRest);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
		try {
			log.debug("try read json start");
			return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
		} catch (Throwable t) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		} finally {
			log.debug("try read json end");
		}
	}

	/**
	 * Stringからバイト配列に変換
	 *
	 * @return data
	 */
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}