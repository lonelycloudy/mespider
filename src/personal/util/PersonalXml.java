package personal.util;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import personal.util.RuleXml;

public class PersonalXml {
	String xmlFile = "";
	Vector rule_Vector;
	public void PersonalXml()throws Exception {
		System.out.println("aa");
		//return this.readXMLFile("src/conf/tianya.xml");
	}
	public Vector readXMLFile(String file) throws Exception{
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder builder = dbf.newDocumentBuilder();
		  xmlFile = file;
		  Document doc = builder.parse(file); // fetch xml file
		  // begin read
		  Element root = doc.getDocumentElement(); //fetch root element
		  NodeList crawler = root.getElementsByTagName("crawler");
		  rule_Vector = new Vector();
		  for (int i = 0; i < crawler.getLength(); i++) {
		   // rotate elment
		   Element ss = (Element) crawler.item(i);
		   // create an rule instance
		   RuleXml ruxml = new RuleXml();
		   ruxml.setName(ss.getAttribute("name"));
		   ruxml.setPattern(ss.getAttribute("pattern"));
		   ruxml.setValue(ss.getAttribute("value"));
		   System.out.println(ruxml.name);
		   System.out.println(ruxml.value);
		   /*NodeList names = ss.getElementsByTagName("pattern");
		   Element e = (Element) names.item(0);
		   Node t = e.getFirstChild();
		   ruxml.setName(t.getNodeValue());*/
		   rule_Vector.add(ruxml);
		  }
		  return rule_Vector;
	}
}
