package proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	 * Timestmap de conversion
	 */
	String dateForMap = "";

	/**
	 * url de fuente externa 2 de conversion
	 */
	final private String URL_API_CONVERT = "http://api.exchangeratesapi.io/v1/latest?access_key=9d1d8591cd422e650a25bdf543b5d461&symbols=USD&format=1";

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

			String country = valueArray[4].split(":")[1].replace("\"", "").replace("}", "").trim();

			String respuestaDeAPIPais = conectarToAPI(URL_API_TRANSLATE + country + "?fullText=false");
			respuestaDeAPIPais = extraerPaisTraducido(respuestaDeAPIPais);

			Float factor2 = 1 / Float.parseFloat(respuestaDeAPIEuro);

			euro = dollar * obtenerMenorFactorConversion(factor2, obtenerFactorConversionFuente1());
			conversionDate();
			mapResult.put("nombre", valueArray[1].split(":")[1].replace("\"", "").trim());
			mapResult.put("pais", respuestaDeAPIPais);
			mapResult.put("euros", String.valueOf(euro));
			mapResult.put("fecha", dateForMap);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return mapResult;
	}

	public Float obtenerFactorConversionFuente1() throws IOException, FileNotFoundException {
		FileReader fileReader = null;
		String factor1String = "";
		try {

			File source1File = new File("../proyectofinal/factor1/");
			File[] listado = source1File.listFiles();
			File lastfile = listado[listado.length - 1];

			fileReader = new FileReader(lastfile.getAbsolutePath());
			if (fileReader != null) {
				BufferedReader reader = new BufferedReader(fileReader);
				factor1String = reader.readLine();
				reader.close();
			}

		} catch (FileNotFoundException e) {
			System.out.println("Fichero no encontrado");

		} catch (IOException ex) {
			throw ex;
		}

		finally {
			fileReader.close();
		}
		Float factor1 = Float.parseFloat(factor1String.split(",")[0].split(":")[1].trim());
		return factor1;
	}

	public Float obtenerMenorFactorConversion(Float factor2, Float factor1) {
		return factor2 < factor1 ? factor2 : factor1;
	}

	public void conversionDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDD-hh:mm:ss");
		Date date = new Date();
		dateForMap = dateFormat.format(date);
	}

	private String conectarToAPI(String url) {
		// Recogemos el resultado en una variable String
		String respuesta = "";
		try {
			// Cliente para la conexi�n
			Client client = ClientBuilder.newClient();
			// Definici�n de URL
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
		return respuesta.split(",")[4].split("}")[0].split(":")[2];
	}

	private String extraerPaisTraducido(String respuesta) {
		return respuesta.split("translations")[1].split("}")[0].split("es")[1].split(",")[0].replace("\":\"", "")
				.replace("\"", "").trim();
	}
}
