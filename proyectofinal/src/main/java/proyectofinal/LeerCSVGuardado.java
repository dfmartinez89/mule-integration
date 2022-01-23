package proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeerCSVGuardado {
	public Map<String, String> obtenerJsonFromCSV() throws IOException, FileNotFoundException {
		FileReader fileReader = null;
		String columnasCSV = "";
		String filasCSV = "";
		Map<String, String> mapResult = new LinkedHashMap<String, String>();

		try {

			File csvFile = new File("../proyectofinal/csv/");
			File[] listado = csvFile.listFiles();
			File lastfile = listado[listado.length - 1];

			fileReader = new FileReader(lastfile.getAbsolutePath());
			if (fileReader != null) {
				BufferedReader reader = new BufferedReader(fileReader);
				columnasCSV = reader.readLine();
				filasCSV = reader.readLine();
				reader.close();
				String arrayColumnas[] = columnasCSV.split(",");
				String arrayFilas[] = filasCSV.split(",");
				for (int i = 0; i < arrayFilas.length; i++) {
					mapResult.put(arrayColumnas[i], arrayFilas[i]);
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("Fichero no encontrado");

		} catch (IOException ex) {
			throw ex;
		}

		finally {
			fileReader.close();
		}
		return mapResult;
	}

}
