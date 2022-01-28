package proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Convertir implements Callable {
	/**
	 * Formato de Timestmap 
	 */
	String dateFormat = "YYYYMMDD-hh:mm:ss";
	
	GuardarFuente1 fuente1 = new GuardarFuente1();
	
	/**
	 * url de fuente externa 2 de conversion
	 */
//	final private String URL_API_CONVERT = "http://api.exchangeratesapi.io/v1/latest?access_key=9d1d8591cd422e650a25bdf543b5d461&symbols=USD&format=1";
	final private String URL_API_CONVERT = "https://v6.exchangerate-api.com/v6/a1a3428c39b01fc4cdf4e323/pair/USD/EUR";
	/**
	 * url de fuente externa de traduccion
	 */
	final private String URL_API_TRANSLATE = "https://restcountries.com/v2/name/";

	public Object onCall(MuleEventContext eventContext) throws Exception {
		Float euro = (float) 0.0;
		Map<String, String> mapResult = new LinkedHashMap<String, String>();

		try {
			String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
			String dollars = valueArray[3].split(":")[1];
			Float dollar = (Float.parseFloat(dollars));

			String respuestaDeAPIEuro = conectarToAPI(URL_API_CONVERT);
			respuestaDeAPIEuro = extraerValorDelEuro(respuestaDeAPIEuro);
			Float factor2 = Float.parseFloat(respuestaDeAPIEuro);

			String country = valueArray[4].split(":")[1].replace("\"", "").replace("}", "").trim();

			String respuestaDeAPIPais = conectarToAPI(URL_API_TRANSLATE + country + "?fullText=false");
			respuestaDeAPIPais = extraerPaisTraducido(respuestaDeAPIPais);

			euro = dollar * obtenerMenorFactorConversion(factor2, obtenerFactorConversionFuente1());
			mapResult.put("nombre", valueArray[1].split(":")[1].replace("\"", "").trim());
			mapResult.put("pais", respuestaDeAPIPais);
			mapResult.put("euros", String.valueOf(euro));
			mapResult.put("fecha", fuente1.conversionDate(dateFormat));

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return mapResult;
	}

	public Float obtenerFactorConversionFuente1() throws IOException, FileNotFoundException {
		FileReader fileReader = null;
		String factor1String = "";
		Float factor1 = (float) 0.0;

		try {

			File source1File = new File("../proyectofinal/factor1/");
			File[] listado = source1File.listFiles();
			File lastfile = listado[listado.length - 1];

			fileReader = new FileReader(lastfile.getAbsolutePath());
			if (fileReader != null) {
				BufferedReader reader = new BufferedReader(fileReader);
				factor1String = reader.readLine();
				reader.close();
				factor1 = Float.parseFloat(factor1String.split(",")[0].split(":")[1].trim());
			}

		} catch (FileNotFoundException e) {
			System.out.println("Fichero no encontrado");

		} catch (IOException ex) {
			throw ex;
		}

		finally {
			fileReader.close();
		}
		return factor1;
	}

	public Float obtenerMenorFactorConversion(Float factor2, Float factor1) {
		return factor2 < factor1 ? factor2 : factor1;
	}

	

	private String conectarToAPI(String url) {
		// Recogemos el resultado en una variable String
		String respuesta = "";
		try {
			// Cliente para la conexion
			Client client = ClientBuilder.newClient();
			// Definicion de URL
			WebTarget target = client.target(url);
			respuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
			if (respuesta.contains("404")) {
				throw new Error("Literal de país erróneo");
			}
		} catch (Error e) {
			e.printStackTrace();
			throw e;
		}

		return respuesta;
	}

	private String extraerValorDelEuro(String respuesta) {
		return respuesta.split(",")[11].split(":")[1].replace("}", "").trim();
	}

	private String extraerPaisTraducido(String respuesta) {
		return respuesta.split("translations")[1].split("}")[0].split("es")[1].split(",")[0].replace("\":\"", "")
				.replace("\"", "").trim();
	}
}
