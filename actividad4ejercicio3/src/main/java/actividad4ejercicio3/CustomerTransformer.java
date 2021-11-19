package actividad4ejercicio3;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomerTransformer extends AbstractTransformer {
	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Object src, String enc) throws TransformerException {
		List<Map<String, Object>> list = (List<Map<String, Object>>) src;
		Document doc;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Clientes");
			doc.appendChild(rootElement);
			for (Map<String, Object> map : list) {
				// New customer
				logger.info("Cliente:");
				Element customerElement = doc.createElement("Cliente");
				rootElement.appendChild(customerElement);

				// Se eliminan de la lista las entradas con key "id"
				map.remove("id");

				// Each entry in Map for customer
				for (Map.Entry<String, Object> element : map.entrySet()) {

					logger.info(" " + element.getKey() + ":" + element.getValue().toString());
					String cast;
					if (element.getKey() == "name") {
						cast = "nombre"; // se traduce el nombre del key
					} else
						cast = "edad"; // se traduce el nombre del key
					Element e = doc.createElement(cast);
					e.setTextContent(element.getValue().toString());
					customerElement.appendChild(e);
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// Write XML to String
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			logger.info("XML : " + writer.toString());
			return writer.toString();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
