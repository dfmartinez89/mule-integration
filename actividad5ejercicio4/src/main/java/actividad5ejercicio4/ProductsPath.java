package actividad5ejercicio4;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class ProductsPath implements Callable {
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String path = eventContext.getMessage().getInboundProperty("http.request.path");
			return String.format("Acceso a productos: %s", path);
	}
}
