/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * XMLReader.java
 *
 * Created on February 11, 2004, 3:07 PM
 */

package com.see.truetransact.commonutil.xml;

/**
 *
 * @author  Hemant
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Vector;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;
import org.apache.log4j.Logger;

public class XMLReader {
    
    private static Logger logger = Logger.getLogger(XMLGenerator.class.getName());
    private static java.lang.String propertiesURL;;
    private static org.w3c.dom.Document propertiesDocument = null;
    /** Creates a new instance of XMLReader */
    public XMLReader() {
    }
    
    /**
     * Insert the method's description here.
     * @return org.w3c.dom.Document
     * @param argsStringFile java.lang.String
     * @param argsBoolValidate boolean
     */
    
    public static Document parseXMLFile(String argsStringFile, boolean argsBoolValidate) {
        try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(argsBoolValidate);
            
             Document doc = factory.newDocumentBuilder().parse(new File(argsStringFile));
            /*File file = new File(argsStringFile);
            Document doc = null;
            if (file.exists()) {
                // Create the builder and parse the file
                doc = factory.newDocumentBuilder().parse(file);
            }*/
            return doc;
        } catch (SAXException e) {
            // A parsing error occurred; the xml input is not valid
            e.printStackTrace(System.err);
        } catch (ParserConfigurationException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
    public static HashMap read(String fileName){
        HashMap resultMap = new HashMap();
        propertiesDocument = parseXMLFile(fileName, false);
        
        Node  nod = null;
        
        NodeList nodes = propertiesDocument.getChildNodes();
        
        Node root = nodes.item(0);
        resultMap.put("Root",(String)root.getNodeName());
        System.out.println(root.getNodeName());
        NodeList elements = root.getChildNodes();
        int len = elements.getLength();
        for(int k=0; k<len; k++){
            nod =elements.item(k);
            if(nod.getNodeType() == Node.ELEMENT_NODE){
                String value = "";
                if(nod.getFirstChild()!=null)
                    value = nod.getFirstChild().getNodeValue().trim();
                resultMap.put(nod.getNodeName(),value);
                
            }/**/
        }
        
        return resultMap;
    }
    
    public static void main(String[] arg ){
        System.out.println(read("F:\\Hemant\\DOMImpl\\DOMImplSource\\xyz.xml").toString());
    }
}
