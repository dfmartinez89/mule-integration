package actividad3ejercicio2;

import javax.jws.WebService;

@WebService
public interface HelloWorldSOAP {
	public String hello(String name);
}
