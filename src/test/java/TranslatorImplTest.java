import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author George Sofianos
 */
public class TranslatorImplTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(TranslatorImplTest.class);
    @Test
    public void testNames() throws InvalidFormatException, FileNotFoundException {

        File xlsxFile = new File("/home/dev-gso/Desktop/AQD/labels.xlsx");
        OPCPackage p = OPCPackage.open(xlsxFile, PackageAccess.READ);
        PrintStream out = new PrintStream(new FileOutputStream("output.xml", true));
        TranslatorImpl tr = new TranslatorImpl(p, out, 5);
        try {
            tr.translateLabels();
        } catch (Exception ex) {
            LOGGER.error("Error: " + ex);
            ex.printStackTrace();
        }
    }

}