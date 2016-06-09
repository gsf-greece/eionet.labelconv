package xml;

import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;

/**
 * @author George Sofianos
 */
public class XMLWriter {

    private SMOutputFactory outf;
    private SMOutputDocument doc;
    private SMOutputElement root;
    public XMLWriter() throws XMLStreamException {
        outf = new SMOutputFactory(XMLOutputFactory.newInstance());
        doc = outf.createOutputDocument(new File("output.xml"));
        doc.setIndentation("\n  ", 1, 1);

    }
    public void startDocument() throws XMLStreamException {
        root = doc.addElement("labels");
    }

    public void writeElement(String name, String content) {
        try {
            root.addElement(name).addCharacters(content);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void closeDocument() throws XMLStreamException {
        doc.closeRoot();
    }

    public void startElement(String name) throws XMLStreamException {
        root.addElement(name);
    }

}
