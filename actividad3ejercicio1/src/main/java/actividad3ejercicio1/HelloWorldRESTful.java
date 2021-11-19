package actividad3ejercicio1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")

public class HelloWorldRESTful {
		@GET
		public String hello(){
			return "Hello World RESTful web service!";
		}
}
