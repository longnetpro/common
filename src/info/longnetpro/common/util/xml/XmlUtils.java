package info.longnetpro.common.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {
	public static Document getXmlDocumentFromFile(String xmlfile)
			throws ParserConfigurationException, SAXException, IOException {
		FileInputStream file;
		file = new FileInputStream(new File(xmlfile));
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(file);
		return xmlDocument;
	}

	public static Document getXmlDocumentFromInputStream(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(is);
		return xmlDocument;
	}

	public static Document newDocument() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void formatDocument(Document document, Writer out) throws TransformerException {
		formatDocument(document, out, "UTF-8");
	}

	public static void formatDocument(Document document, Writer out, String encoding) throws TransformerException {
		document.normalizeDocument();
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer idTransform = transFactory.newTransformer();
		idTransform.setOutputProperty(OutputKeys.METHOD, "xml");
		idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
		if (encoding != null) {
			idTransform.setOutputProperty(OutputKeys.ENCODING, encoding);
		}
		idTransform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		Source source = new DOMSource(document);
		Result result = new StreamResult(out);
		idTransform.transform(source, result);
	}

	public static void transformDocument(Document document, Writer out, File stylesheet) throws TransformerException {
		document.normalizeDocument();
		Transformer idTransform = null;
		TransformerFactory transFactory = TransformerFactory.newInstance();
		StreamSource stylesource = new StreamSource(stylesheet);
		idTransform = transFactory.newTransformer(stylesource);
		Source source = new DOMSource(document);
		Result result = new StreamResult(out);
		idTransform.transform(source, result);
	}

	public static String formatDocumentAsString(Document document) throws TransformerException {
		Writer out = new StringWriter();
		formatDocument(document, out);
		String formattedXML = out.toString();
		return formattedXML;
	}

	public static void formatXmlString(String xml, Writer out, String encoding)
			throws TransformerException, ParserConfigurationException, SAXException, IOException {
		byte[] buf = xml.getBytes(encoding);
		InputStream is = new ByteArrayInputStream(buf);
		Document doc = XmlUtils.getXmlDocumentFromInputStream(is);
		XmlUtils.formatDocument(doc, out, encoding);
	}

	public static XPath getXPathInstance() {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return xPath;
	}

	public static XPathExpression getXPathExpression(String expression) throws XPathExpressionException {
		return getXPathInstance().compile(expression);
	}

	public static Object evaluate(Document document, String expression, QName returnType)
			throws XPathExpressionException {
		XPathExpression xpathexp = getXPathExpression(expression);
		return xpathexp.evaluate(document, returnType);
	}

	public static Node evaluateToNode(Document document, String expression) throws XPathExpressionException {
		return (Node) evaluate(document, expression, XPathConstants.NODE);
	}

	public static NodeList evaluateToNodeList(Document document, String expression) throws XPathExpressionException {
		return (NodeList) evaluate(document, expression, XPathConstants.NODESET);
	}

	public static String evaluateToString(Document document, String expression) throws XPathExpressionException {
		return (String) evaluate(document, expression, XPathConstants.STRING);
	}

	public static void setAttribute(Node node, String name, String value) {
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return;
		Element e = (Element) node;
		e.setAttribute(name, value);
	}

	public static String getAttributeValue(Node node, String attrName) {
		if (node == null)
			return null;
		NamedNodeMap attrs = node.getAttributes();
		if (attrs == null)
			return null;
		Node attrNode = attrs.getNamedItem(attrName);
		if (attrNode == null)
			return null;
		return attrNode.getNodeValue();
	}

	public static void removeElement(Element element) {
		if (element.getParentNode() != null) {
			element.getParentNode().removeChild(element);
		}
	}

	public static void removeAttributeFromElement(Element element, String attr) {
		if (element != null && element.hasAttribute(attr)) {
			element.removeAttribute(attr);
		}
	}

	public static String escapeXml(String text) {
		return StringEscapeUtils.escapeXml11(text);
	}

	public static String unescapeXml(String text) {
		return StringEscapeUtils.unescapeXml(text);
	}
}
