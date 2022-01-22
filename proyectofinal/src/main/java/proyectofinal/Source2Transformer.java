package proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Source2Transformer implements Callable {
	String timestamp = "";

	public Object onCall(MuleEventContext eventContext) throws Exception {
		Float euro = null;
		try {
			String valueArray[] = eventContext.getMessage().getPayloadAsString().split(",");
			conversionDate();
			String dollars = valueArray[3].split(":")[1];
			Float dollar = (Float.parseFloat(dollars));

			String respuestaDeAPI = conectarToAPI();
			respuestaDeAPI = extraerValorDelEuro(respuestaDeAPI);

			Float factor2 = 1 / Float.parseFloat(respuestaDeAPI);

			euro = dollar * obtenerMenorFactorConversion(factor2, obtenerFactorConversionSource1());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return euro;
	}

	public Float obtenerFactorConversionSource1() throws IOException, FileNotFoundException {
		FileReader fileReader = null;
		String factor1String = "";
		try {

			File source1File = new File("../proyectofinal/conversionrate/");
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
		timestamp = dateFormat.format(date);
		;
	}

	private String conectarToAPI() {
		String URL_API = "http://api.exchangeratesapi.io/v1/latest?access_key=9d1d8591cd422e650a25bdf543b5d461&symbols=USD&format=1";

		// Cliente para la conexi�n
		Client client = ClientBuilder.newClient();
		// Definici�n de URL
		WebTarget target = client.target(URL_API);
		// Recogemos el resultado en una variable String
		String respuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);

		return respuesta;
	}

	private String extraerValorDelEuro(String respuesta) {
		return respuesta.split(",")[4].split("}")[0].split(":")[2];
	}
}
