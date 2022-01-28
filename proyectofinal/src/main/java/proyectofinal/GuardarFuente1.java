package proyectofinal;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class GuardarFuente1 implements Callable {
	/**
	 * Formato de Timestmap
	 */
	String timeFormat = "DDMMYY-hhmmss";

	/**
	 * Método que transforma la respuesta de la fuente externa 1
	 */
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
		return valueArray[5].replace("\"quotes\":", "").replace("}}", "").trim()
				.concat(",\"DATE\":" + conversionDate(timeFormat) + "}");

	}

	/**
	 * Método que obtiene el timestamp con el formato especificado
	 */
	public String conversionDate(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		String dateConversion = dateFormat.format(date);
		return dateConversion;
	}
}
