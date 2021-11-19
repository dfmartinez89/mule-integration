package actividad3ejercicio4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/dollars2euros/{cantidad}")
public class Convertir {
	@GET
	public String convert(@PathParam("cantidad") String cantidad) {
		Double dollar = 0.0;
		Double euro = 0.0;
		try {
			dollar = new Double(cantidad);
			if (dollar <= 0) {
				return "La cantidad a convertir debe ser mayor que cero";
			}
		} catch (Exception e) {
			return "Valor o formato incorrecto de la cantidad introducida";
		}
		euro = dollar * 0.864865;
		return String.format("La cantidad en euros es %s", euro.toString());
	}
}
