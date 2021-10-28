package actividad2ejercicio2;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class HelloMessage implements Callable {
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		return " Hello " + (String) eventContext.getMessage().getPayload() + "!";
	}
}
