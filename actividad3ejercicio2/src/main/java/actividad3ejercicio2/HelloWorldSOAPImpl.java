package actividad3ejercicio2;

public class HelloWorldSOAPImpl implements HelloWorldSOAP{
	@Override
	public String hello(String name) {
		return "Hello World SOAP web service! Your name is: " + name;
	}
}
