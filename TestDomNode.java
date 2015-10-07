import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class TestDomNode {
	
	public TestDomNode(){}
	
	public static String tempBeginTag = "<tempRoot name=\"id\">";
	public static String tempEndTag = "</tempRoot>";
	private static final String DEFAULT_ENCODING_REL = "UTF-8";
	private static final String PLATFORM = "winphone";
	

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();;
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// Ignore DTD file
			builder.setEntityResolver(new EntityResolver() {
		        @Override
		        public InputSource resolveEntity(String publicId, String systemId)
		                throws SAXException, IOException {
		            if (systemId.contains("asf_1_0.dtd")) {
		                return new InputSource(new StringReader(""));
		            } else {
		                return null;
		            }
		        }
		    });

			String[] locales = {"en_us"};
			
			//String[] locales = {"en_us", "de_de", "fr_fr", "ja_jp", "es_es", "it_it", "pt_br", "nl_nl", "sv_se", "da_dk", "fi_fi", "nb_no", "ko_kr", "zh_cn", "zh_tw", "cs_cz", "pl_pl", "ru_ru", "tr_tr"};
			
			for (String locale : locales) {
				boolean change = false;
				String inputFileName = "";
				String outputFileName = "";
				
				// Reader-Android
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-Android\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-Android\\strings.asfx";
				
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";
								
				
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-Android\\Signature\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-Android\\Signature\\strings.asfx";
				
				
				// Reader-Winphone
				inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-WinPhone\\Main\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-WinPhone\\strings.asfx";
				outputFileName = inputFileName; 
				
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-WinPhone\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-WinPhone\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-WinPhone\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Reader-WinPhone\\strings.asfx";
				
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";
				
				//Reader-iOS
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\build\\" + locale + "\\asf\\Share-PDFViewer\\strings.asfx";				

				//Reader-Android Ship
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Ship\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-Android\\Localizable.asfx";
				//outputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-Android\\Ship\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-Android\\Localizable.asfx";				

				// Reader-ios
				//inputFileName = "C:\\Projects\\centralized_alf\\products\\DCMobile\\Reader-iOS\\Main\\alfa\\resources\\edit\\" + locale + "\\asf\\Reader-iOS\\Localizable.asfx";
				//outputFileName = inputFileName; 				

				FileInputStream stream = new FileInputStream(inputFileName);
				Document doc = builder.parse(stream, DEFAULT_ENCODING_REL);
				
				NodeList strList = doc.getElementsByTagName("str");
				// Move inside the loop in case there is a set parent node
				//Node parent = null; 
			    //if (strList.getLength() > 0) {
			    //	parent = strList.item(0).getParentNode();
			    //}
				for (int i=0; i < strList.getLength(); i++) {
					Node parent = strList.item(i).getParentNode();
					Node cur = strList.item(i);
					String nameVal = ((Element)cur).getAttribute("name");
					String translateVal = "";
					
					boolean needsUpdate = false;
					boolean needsClean = false;
					NodeList valList = ((Element)cur).getElementsByTagName("val");
					Node saveVal = null;
					for (int k=0; k < valList.getLength(); k++) {
						Node curVal = valList.item(k);
						if (((Element)curVal).hasAttribute("plat")) {
							 if (((Element)curVal).getAttribute("plat").equalsIgnoreCase(PLATFORM)) {
									if (curVal.getFirstChild() != null && !curVal.getFirstChild().getNodeValue().equalsIgnoreCase("null")) {
										needsUpdate = true;
										saveVal = curVal;
										break;
									}	
							 } else {
								 
							 }
						} else if (curVal.getFirstChild() == null) {
							needsClean = true;
						}
						
					}
					if (needsUpdate && (saveVal != null)) {
						Element newNode = doc.createElement("str");
						newNode.setAttribute("name", nameVal);
						if (((Element)cur).hasAttribute("translate")) {
							newNode.setAttribute("translate", ((Element)cur).getAttribute("translate"));
						}
						((Element)saveVal).removeAttribute("plat");
						
						NodeList descList = ((Element)cur).getElementsByTagName("desc");
						Node desc = null;
						if (descList.getLength() > 0) {
							desc = descList.item(0);
							newNode.appendChild(desc);
						}						
						newNode.appendChild(saveVal);
						

						
						parent.replaceChild(newNode, cur);
						needsUpdate = false;
						change = true;
					} else if (needsClean) {
						valList = ((Element)cur).getElementsByTagName("val");
						for (int j=0; j < valList.getLength(); j++) {
							Node curVal = valList.item(j);
							if (((Element)curVal).hasAttribute("plat")) {
								needsClean = true;
								break;
							}							
						}
						if (needsClean) {
							parent.removeChild(cur);
						}
						needsClean = false;
						change = true;
					}
				}
				
				if (change) {
				    // Write output to xml file
				    TransformerFactory tFactory = TransformerFactory.newInstance();
				    tFactory.setAttribute("indent-number", 4);
				    Transformer transformer = tFactory.newTransformer();		    
				    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				    
				    // Add DTD back
				    DOMImplementation domImpl = doc.getImplementation();
				    DocumentType doctype = domImpl.createDocumentType("doctype",
				        "",
				        "http://ns.adobe.com/asf/asf_1_0.dtd");
				    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
				    
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
