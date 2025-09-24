/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * XMLGenerator.java
 *
 * Created on February 11, 2004, 2:58 PM
 */

package com.see.truetransact.commonutil.xml;

/**
 *
 * @author  Hemant
 */
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.apache.xerces.parsers.DOMParser;
import org.apache.log4j.Logger;


public class XMLGenerator {
    
    private static Logger logger = Logger.getLogger(XMLGenerator.class.getName());
    private static java.lang.String propertiesURL;;
    private static org.w3c.dom.Document propertiesDocument = null;
    
    /** Creates a new instance of XMLGenerator */
    public XMLGenerator() {
    }
    
    private static void initilize(){
        try{
            propertiesDocument = (DocumentBuilderFactory.newInstance()).newDocumentBuilder().newDocument();
        }catch(Exception e){
            e.printStackTrace();
        }
    } 
    public static void generate(HashMap dataMap,String fileName){
        try{
            initilize();
            String nodeName="";
            Node newNode = null;
            Text dataNode = null;
            Node root = propertiesDocument.createElement((String)dataMap.get("Root"));
            Iterator iterator = dataMap.keySet().iterator();
            while(iterator.hasNext()){
                nodeName = (String)iterator.next();
                System.out.println("Name : "+nodeName);
                if(!nodeName.equals("Root")){
                    newNode = propertiesDocument.createElement(nodeName);
                    dataNode = propertiesDocument.createTextNode((String)dataMap.get(nodeName));
                    newNode.appendChild(dataNode);
                }
                root.appendChild(newNode);
            } 
            
            propertiesDocument.appendChild(root);
            transform2XML(fileName); 
        }catch(Exception e){
            e.printStackTrace();
        }    
    }
    
    private static void transform2XML(String fileName){
        try{
            StreamResult sr = new StreamResult(new FileOutputStream(fileName)); 
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            System.out.println("Root "+propertiesDocument.getDocumentElement().getNodeName());   
            DOMSource source = new DOMSource(propertiesDocument);
            tr.transform(source,sr);
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    public static void main(String[] arg ){
        HashMap result = XMLReader.read("F:\\Hemant\\DOMImpl\\DOMImplSource\\xyz.xml");
        System.out.println(result);
        generate(result,"F:\\Hemant\\DOMImpl\\DOMImplSource\\new1.xml");
    }
}
