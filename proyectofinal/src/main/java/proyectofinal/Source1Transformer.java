package proyectofinal;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Source1Transformer implements Callable {
	String timestamp;

	public Object onCall(MuleEventContext eventContext) throws Exception {
		String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
		conversionDate(); // se obtiene timestamp de la conversión
		return valueArray[5].replace("\"quotes\":", "").replace("}}", "").trim()
				.concat(",\"DATE\":" + timestamp + "}");

	}

	public void conversionDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("DDMMYY-hhmmss");
		Date date = new Date();
		timestamp = dateFormat.format(date);
		;
	}
}
