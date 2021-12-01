package actividad5ejercicio2;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class CalledPath implements Callable {
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String path = eventContext.getMessage().getInboundProperty("http.request.path");
			return String.format("La ruta llamada es: %s", path);
	}

}
