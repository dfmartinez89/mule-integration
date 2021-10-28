package actividad2ejercicio1;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class GreetingText implements Callable {
	public Object onCall(MuleEventContext eventContext) throws Exception {
		return eventContext.getMessage().getPayload() + " Mule!";
	}
}
