package proyectofinal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Source2Transformer implements Callable{
	String timestamp;

	public Object onCall(MuleEventContext eventContext) throws Exception {
		String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
		conversionDate(); // se obtiene timestamp de la conversión
		String rateString = valueArray[5].replace("\"rates\":{\"USD\":", "").trim();
		int rate = 1/(Integer.parseInt(rateString));
		String source2Rate = Integer.toString(rate);
		return source2Rate.concat(timestamp);

	}

	public void conversionDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
		Date date = new Date();
		timestamp = dateFormat.format(date);
		;
	}
}
