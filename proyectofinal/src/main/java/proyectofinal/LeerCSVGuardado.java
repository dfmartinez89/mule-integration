package proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeerCSVGuardado {
	/**
	 * Método que obtiene los datos del fichero csv guardado en el filesystem
	 * @return mapResult: objeto con los datos del fichero csv
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Map<String, String> obtenerJsonFromCSV() throws IOException, FileNotFoundException {
		FileReader fileReader = null;
		String columnasCSV = "";
		String filasCSV = "";
		Map<String, String> mapResult = new LinkedHashMap<String, String>();

		try {

			File csvFile = new File("../proyectofinal/csv/");
			File[] listado = csvFile.listFiles();
			File lastfile = listado[listado.length - 1]; //se obtiene el último fichero guardado en la ruta

			fileReader = new FileReader(lastfile.getAbsolutePath());
			if (fileReader != null) {
				BufferedReader reader = new BufferedReader(fileReader);
				columnasCSV = reader.readLine(); //la primera línea del fichero csv son las columnas
				filasCSV = reader.readLine(); //la segunda línea corresponde a la primera fila
				reader.close();
				String arrayColumnas[] = columnasCSV.split(",");
				String arrayFilas[] = filasCSV.split(",");
				
				for (int i = 0; i < arrayFilas.length; i++) {
					mapResult.put(arrayColumnas[i], arrayFilas[i]); //Se construye un objeto map para devolverlo en el payload del mensaje
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
