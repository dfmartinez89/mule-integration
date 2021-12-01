package actividad5ejercicio3;

import java.util.Map;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class HTTPReqToUser extends AbstractMessageTransformer {
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		User user = new User();
		Map<String, String> queryParams = message.getInboundProperty("http.query.params");
		String name = queryParams.get("name");
		String lname = queryParams.get("lname");
		user.setAge(name);
		user.setJob(lname);
		return user;
	}
}
