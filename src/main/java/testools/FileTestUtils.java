package testools;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.google.common.io.Files.write;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class FileTestUtils {
    private FileTestUtils() {
    }

    public static String fileContent(final File alterationFile) {
        try {
            return Files.toString(alterationFile, Charsets.UTF_8);
        } catch (final Exception ignored) {
            return "";
        }
    }

    public static File createEmptyFile(final File father, final String fileName) throws IOException {
        final File file = new File(father, fileName);
        write("", file, UTF_8);
        return file;
    }

    public static boolean areSimilar(final String controlXml, final String testXml) {
        return !DiffBuilder.compare(controlXml).withTest(testXml)
                .checkForSimilar()
                .normalizeWhitespace()
                .ignoreWhitespace()
                .ignoreComments()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
                .build().hasDifferences();
    }

    public static boolean isXpathResolved(final String controlXml, final String xpathStr) {

        final InputSource source = new InputSource(new StringReader(controlXml));

        try {
            final DocumentBuilder db = DocumentBuilders.createSaferDocumentBuilder(factory -> {
                factory.isIgnoringElementContentWhitespace();
            });
            final Document document = db.parse(source);
            final XPathFactory xpathFactory = XPathFactory.newInstance();
            final XPath xpath = xpathFactory.newXPath();

            final String result = xpath.evaluate(xpathStr, document);
            if (result == null || result.isEmpty() || "false".equals(result)) {
                return false;
            }
        } catch (final Exception ignored) {
            return false;
        }
        return true;
    }

    private static String nodeToString(final Node node)
            throws TransformerException {
        final StringWriter buf = new StringWriter();
        TransformerFactory trfactory = TransformerFactory.newInstance();
        trfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        trfactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        trfactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        final Transformer xform = trfactory.newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(node), new StreamResult(buf));
        return buf.toString();
    }

    public static boolean subsetsSimilar(final String controlXml,
                                         final String testXml,
                                         final String xpath) {
        try {
            DocumentBuilder db = DocumentBuilders.createSaferDocumentBuilder(factory -> {
                factory.isIgnoringElementContentWhitespace();
            });
            final Document doc = db.parse(new InputSource(new StringReader(controlXml)));
            final XPath xPath = XPathFactory.newInstance().newXPath();
            final Node result = (Node) xPath.evaluate(xpath, doc, XPathConstants.NODE);

            return areSimilar(nodeToString(result), testXml);
        } catch (final Exception ignored) {
        }
        return false;
    }
}