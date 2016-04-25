package Procesador;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jos�Luis on 24/04/2016.
 */
public class Features {

	private Feature feature;
	private HashMap config;
	private List<Feature> variableFeatures;
	
	public Features() {
		variableFeatures = new ArrayList<Feature>();
		config = new HashMap();
		loadFeatureFiles();
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public List<Feature> getVariableFeatures() {
		return variableFeatures;
	}

	public void setVariableFeatures(List<Feature> variableFeatures) {
		this.variableFeatures = variableFeatures;
	}

	public void loadFeatureFiles() {
		try {

			readConfigFile( "app/Procesador/default.config" );

			File fXmlFile = new File("app/Procesador/Metaecotour.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("struct");
			Node nNode = nList.item(0);
			System.out.println("\nElement :" + nNode.getNodeName());
			nList = nNode.getChildNodes();
			nNode = nList.item(1);
			System.out.println("\n	Element :" + nNode.getNodeName());
			feature = new Feature(((Element)nNode).getAttribute("name"));
			feature.setChilds(getNodesChild(nNode, false));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private List<Feature> getNodesChild(Node nodo, boolean parentIsAdded) {
		ArrayList list = null;
		NodeList nList;
		boolean added;
		added = parentIsAdded;
		nList = nodo.getChildNodes();
		Node nNode;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String isMandatory = ((Element)nNode).getAttribute("mandatory");
				Feature featureChild = new Feature(((Element)nNode).getAttribute("name"));
				if(isMandatory == null || isMandatory.compareToIgnoreCase("TRUE") != 0){
					if(config.get(((Element)nNode).getAttribute("name")) != null){
						featureChild.setPresent(true);
					}
					added = parentIsAdded;
					if(!parentIsAdded){
						added = true;
						variableFeatures.add(featureChild);
						System.out.println(((Element)nNode).getAttribute("name")+" es variable");
					}
				}
				featureChild.setChilds(getNodesChild(nNode, added));
				if (list == null){
					list = new ArrayList<Feature>();
				}
				list.add(featureChild);
				System.out.println("		Current Element :" + ((Element)nNode).getAttribute("name"));
			}
		}

		return list;
	}

	private void readConfigFile( String file ) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new FileReader(file));
			String         line = null;
			while( ( line = reader.readLine() ) != null ) {
				config.put(line,line);
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}




}
