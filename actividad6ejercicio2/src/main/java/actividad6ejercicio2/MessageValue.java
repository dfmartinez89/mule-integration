package actividad6ejercicio2;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class MessageValue implements Callable {
	public Object onCall(MuleEventContext eventContext)throws Exception{
		//Se divide el mensaje para devolver solamente el campo value
		 String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
		 return valueArray[1].replace("}","").trim();
	}

}
