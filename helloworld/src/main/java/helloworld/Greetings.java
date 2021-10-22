package helloworld;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Greetings implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String name = eventContext.getMessage().getInboundProperty("http.request.path");
			return String.format("Hello %s!", name.substring(1));
	}
}


