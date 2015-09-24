import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class TestDomNode {
	
	public TestDomNode(){}
	
	public static String tempBeginTag = "<tempRoot name=\"id\">";
	public static String tempEndTag = "</tempRoot>";
	private static final String DEFAULT_ENCODING_REL = "UTF-8";

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();;
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// Test 1
			/*String strValue = "Start<![CDATA[<font color=\'#005CFF\'>%1$s</font> appreciated your work]]>End";
	        //String strValue = "testhere\\n\\n<head><title>Hello world!</title></head><body><table><tr>row1</tr><tr>row1</tr></table>Some <b>text</b> here too</body>endhere too";
	        //strValue = "<a href=\"http://test.com\"> high light </a>";
			String temp = tempBeginTag + strValue + tempEndTag;
			Document strDoc = builder.parse(new ByteArrayInputStream(temp.getBytes(StandardCharsets.UTF_8)));
			Node root = strDoc.getDocumentElement();
			String result = nodeToString(root);
			System.out.println(result);*/

			String[] locales = {"ja_jp", "de_de", "fr_fr", "es_es", "it_it", "pt_br", "nl_nl", "sv_se", "fi_fi", "da_dk", "nb_no", "ko_kr", "zh_cn", "zh_tw", "cs_cz", "pl_pl", "ru_ru", "tr_tr", "en_us"};
			for (String locale : locales) {
				boolean change = false;
				String inputFileName = "";
				String outputFileName = "";
				

				inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-iOS\\Localizable.asfx";
				outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-iOS\\update.asfx";
				FileInputStream stream = new FileInputStream(inputFileName);
				Document doc = builder.parse(stream, DEFAULT_ENCODING_REL);
				
				NodeList strList = doc.getElementsByTagName("str");
				Node parent = null;
			    if (strList.getLength() > 0) {
			    	parent = strList.item(0).getParentNode();
			    }
				for (int i=0; i < strList.getLength(); i++) {
					Node cur = strList.item(i);
					String nameVal = ((Element)cur).getAttribute("name");
					String translateVal = "";
					
					boolean needsUpdate = false;
					String iosVal = "";
					NodeList valList = ((Element)cur).getElementsByTagName("val");
					Node saveVal = null;
					for (int k=0; k < valList.getLength(); k++) {
						Node curVal = valList.item(k);
						if (((Element)curVal).hasAttribute("plat") && 
								((Element)curVal).getAttribute("plat").equalsIgnoreCase("ios") &&
								(curVal.getNodeValue() != "")){
							if (curVal.getFirstChild() != null) {
								needsUpdate = true;
								saveVal = curVal;
								break;
							}						
						}
						
					}
					if (needsUpdate && (saveVal != null)) {
						Element newNode = doc.createElement("str");
						newNode.setAttribute("name", nameVal);
						if (((Element)cur).hasAttribute("translate")) {
							newNode.setAttribute("translate", ((Element)cur).getAttribute("translate"));
						}
						((Element)saveVal).removeAttribute("plat");
						newNode.appendChild(saveVal);
						
						parent.replaceChild(newNode, cur);
						change = true;
					}
				}
				
				if (change) {
				    // Write output to xml file
				    TransformerFactory tFactory = TransformerFactory.newInstance();
				    Transformer transformer = tFactory.newTransformer();
				    doc.setXmlStandalone(true);
				    DOMSource source = new DOMSource(doc);
				    StreamResult result = new StreamResult(new FileOutputStream(outputFileName));
				    transformer.transform(source, result);
				}

				
			}
			


		} catch (Exception e) {
			System.out.println("error thrown." + e);
		}
	}
	
	public static String printNode(Node node) {
		String result = "";
		NodeList nodeList = node.getChildNodes();
		for (int i=0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			short nodeType = childNode.getNodeType();
			switch (nodeType) {
			    case Node.CDATA_SECTION_NODE:
			    	result += "<![CDATA[" + ((CDATASection)childNode).getData() + "]]>";
			    	break;
			    case Node.TEXT_NODE:
			     	result +=  childNode.getTextContent();
			     	break;
			    case Node.ELEMENT_NODE:
					String nodeName = childNode.getNodeName();
					String beginTag = "<" + nodeName + ">";
					String endTag = "</" + nodeName + ">";
					// Todo: need to add attributes here too
					result += beginTag + printNode(childNode) + endTag;
			    default:
			    	
			}
		}
		return result;
	}
	

	/*
	 * Convert a DOM node to string without including the root node
	 * root node matched pattern where attribute name is plat
	 * Used in importResource/Read - import string into ALF db
	 * @param node
	 * 				a DOM node from resource file
	 */
	public static String nodeToString3(Node node) {
		String nodeName = node.getNodeName();
		String tempEndTag = "</" + nodeName + ">";
		String str = "";
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		
		str = sw.toString();
		str = str.replace(tempEndTag, "");
		String regex = "<" + nodeName + "\\s*" + "plat=[\"'][^\\s<>]+[\"']" + "\\s*" + ">";
		str = str.replaceAll(regex, "");
		return str;
	}	
	
	
	/*
	 * Convert a DOM node to string without including the root node
	 * root node matched pattern such as <string name="login_dialog_connection_error">
	 * Used in importResource/Read - import string into ALF db
	 * @param node
	 * 				a DOM node from resource file
	 */
	public static String nodeToString2(Node node) {
		String nodeName = node.getNodeName();
		String tempEndTag = "</" + nodeName + ">";
		String str = "";
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		
		str = sw.toString();
		str = str.replace(tempEndTag, "");
		String regex = "<" + nodeName + "\\s*" + "name=[\"'][^\\s<>]+[\"']" + "\\s*" + ">";
		str = str.replaceAll(regex, "");
		return str;
	}
	
	public static String nodeToString(Node node) {
		String str = "";
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		
		str = sw.toString();
		return str;
	}	

}
