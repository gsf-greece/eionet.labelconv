import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import xml.XMLWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 *
 * @author George Sofianos
 */
public class TranslatorImpl implements Translator {

    public static final Logger LOGGER = LoggerFactory.getLogger(TranslatorImpl.class);


    private final OPCPackage pkg;
    private final int minColumns;
    private final PrintStream output;
    private XMLWriter writer;

    public TranslatorImpl(OPCPackage pkg, PrintStream output, int minColumns) {
        this.pkg = pkg;
        this.output = output;
        this.minColumns = minColumns;
    }


    private class XLSX2XML implements XSSFSheetXMLHandler.SheetContentsHandler {

        private String elem = null;
        private int cellNumber = 0;

        public void startRow(int i) {
            cellNumber = 1;
        }

        public void endRow(int i) {
            //output.append("</" + elem + ">");
            //output.append("\n");
        }

        public void cell(String s, String s1, XSSFComment xssfComment) {
            if (cellNumber == 1) {
                //output.append("<" + s1 + ">");
                this.elem = s1;
            } else if (cellNumber == 2) {
                writer.writeElement(elem, s1);
                //output.append("<" + elem + ">" + s1 + "</" + elem + ">");
            } else if (cellNumber == 3) {
                writer.writeElement(elem + "_SHORT", s1);
                //output.append("<" + elem + "_SHORT>" + s1 + "</" + elem + "_SHORT>");
            }
            cellNumber++;
        }

        public void headerFooter(String s, boolean b, String s1) {

        }
    }

    public void translateLabels() throws IOException, OpenXML4JException, SAXException, ParserConfigurationException, XMLStreamException {
        this.writer = new XMLWriter();
        writer.startDocument();
        XSSFReader xssfReader = new XSSFReader(this.pkg);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.pkg);
        StylesTable styles = xssfReader.getStylesTable();

        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();

            DataFormatter formatter = new DataFormatter();
            InputSource sheetSource = new InputSource(stream);
            if (!sheetName.equals("Envelope Test")) {
                //output.append("<" + sheetName + ">");
                writer.writeElement(sheetName, "");
                XMLReader reader = SAXHelper.newXMLReader();
                ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, new XLSX2XML(), null, true);
                reader.setContentHandler(handler);
                reader.parse(sheetSource);
                //writer.endElement(sheetName);
                //output.append("</" + sheetName + ">");
                LOGGER.info(sheetName);
            }
        }
        writer.closeDocument();
    }
}
