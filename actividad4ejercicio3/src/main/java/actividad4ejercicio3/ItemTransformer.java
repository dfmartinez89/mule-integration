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

public class ItemTransformer extends AbstractTransformer {
	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Object src, String enc) throws TransformerException {
		List<Map<String, Object>> list = (List<Map<String, Object>>) src;
		Document doc;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Items");
			doc.appendChild(rootElement);
			for (Map<String, Object> map : list) {
				// New item
				logger.info("Item:");
				Element itemElement = doc.createElement("Item");
				rootElement.appendChild(itemElement);
				// Each entry in Map for item
				for (Map.Entry<String, Object> element : map.entrySet()) {
					logger.info(" " + element.getKey() + ":" + element.getValue().toString());
					Element e = doc.createElement(element.getKey());
					e.setTextContent(element.getValue().toString());
					itemElement.appendChild(e);
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
