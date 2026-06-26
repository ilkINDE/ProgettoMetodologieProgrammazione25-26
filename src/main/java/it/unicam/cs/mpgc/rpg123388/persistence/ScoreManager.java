package it.unicam.cs.mpgc.rpg123388.persistence;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ScoreManager {
    private static final String XML_PATH = "run_history.xml";
    private static final String XSD_PATH = "run_history.xsd";

    public static void saveRunStats(int room, Map<String, Integer> kills, List<Hero> party) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            Element root;

            File file = new File(XML_PATH);
            if (file.exists()) {
                doc = builder.parse(file);
                root = doc.getDocumentElement();
            } else {
                doc = builder.newDocument();
                root = doc.createElement("history");
                doc.appendChild(root);
            }

            Element run = doc.createElement("run");
            run.setAttribute("date", LocalDateTime.now().toString());
            root.appendChild(run);

            Element partyElem = doc.createElement("party");
            for (Hero h : party) {
                Element heroElem = doc.createElement("hero");
                heroElem.setAttribute("class", h.getClass().getSimpleName());
                heroElem.setAttribute("level", String.valueOf(h.getLevel()));
                heroElem.setTextContent(h.getName());
                partyElem.appendChild(heroElem);
            }
            run.appendChild(partyElem);

            Element dungeonElem = doc.createElement("dungeon");

            Element roomElem = doc.createElement("rooms_cleared");
            roomElem.setTextContent(String.valueOf(room));
            dungeonElem.appendChild(roomElem);

            Element killsElem = doc.createElement("kills");
            for (Map.Entry<String, Integer> entry : kills.entrySet()) {
                Element monsterElem = doc.createElement("monster");
                monsterElem.setAttribute("name", entry.getKey());
                monsterElem.setAttribute("count", String.valueOf(entry.getValue()));
                killsElem.appendChild(monsterElem);
            }
            dungeonElem.appendChild(killsElem);

            run.appendChild(dungeonElem);

            validateXML(doc);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateXML(Document doc) throws Exception {
        File xsdFile = new File(XSD_PATH);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsdFile);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(doc));
    }
}