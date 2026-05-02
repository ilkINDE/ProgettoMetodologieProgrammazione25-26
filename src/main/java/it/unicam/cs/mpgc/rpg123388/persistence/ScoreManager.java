package it.unicam.cs.mpgc.rpg123388.persistence;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;

public class ScoreManager {
    private static final String XML_PATH = "run_history.xml";

    public static void saveRunStats(int room, String kills) {
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
            root.appendChild(run);

            Element date = doc.createElement("date");
            date.setTextContent(LocalDateTime.now().toString());
            run.appendChild(date);

            Element roomElem = doc.createElement("room");
            roomElem.setTextContent(String.valueOf(room));
            run.appendChild(roomElem);

            Element killsElem = doc.createElement("kills");
            killsElem.setTextContent(kills);
            run.appendChild(killsElem);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}