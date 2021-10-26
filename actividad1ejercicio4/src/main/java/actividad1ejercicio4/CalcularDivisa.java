package actividad1ejercicio4;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class CalcularDivisa implements Callable {
	// se pone la cotización como constante
	static float TASA_CAMBIO = (float) 0.86;

	@Override
	public Object onCall(MuleEventContext eventContext) {
		try {
			// se obtiene del message la url enviada en la petición http
			String input = eventContext.getMessage().getInboundProperty("http.request.path");
			// se valida que la url es la esperada
			if (!urlEsBuena(input)) {
				throw new Error("Error en la url de entrada");
			};
			// se valida que el valor a convertir es un float positivo
			String valorEntrada = input.substring(12);
			if (!dollarEsBueno(valorEntrada)) {
				throw new Error("Valor o formato incorrecto de la cantidad introducida");
			};
			// se calcula la conversión
			float dollar = Float.parseFloat(valorEntrada);
			float euro = convertirDollarEuro(dollar);

			// se envía el resultado en el message
			return String.format("El valor en euros es %s", euro);
		} catch (Exception e) {
			return "Valor o formato incorrecto de la cantidad introducida";
		}
	}
    
	//se valida la url de entrada
	public boolean urlEsBuena(String input) {
		return input.contains("/dollar2eur/");
	}

	//se convierte a float la cantidad recibida en la url y se comprueba que sea positiva
	public boolean dollarEsBueno(String input) {
		return (!(Float.isNaN(Float.parseFloat(input))) && (Float.parseFloat(input) >= 0));
	}

	//se ejecuta la conversión de dollar a euro
	public float convertirDollarEuro(float dollar) {
		return dollar * TASA_CAMBIO;
	}
}
