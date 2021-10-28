package actividad2ejercicio1;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Greetings implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		return String.format("Hello World");
	}
}
