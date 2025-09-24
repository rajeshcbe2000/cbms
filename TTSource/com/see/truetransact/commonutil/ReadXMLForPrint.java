/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * dailydeposit.java
 *
 * Created on September 28, 2011, 3:03 PM
 */
package com.see.truetransact.commonutil;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.print.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.text.SimpleDateFormat;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import java.util.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;
import java.sql.*;
import javax.print.*;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 
import java.util.Properties;
/**
 *
 * @author  user
 */
public class ReadXMLForPrint extends javax.swing.JFrame {
    
    /** Creates new form dailydeposit */
    public static String xmlPath="C://printer.xml";
    DefaultListModel modelList;
    public ReadXMLForPrint() {
       modelList = new DefaultListModel();
         initComponents();
         this.setBounds(0,0, 700, 400);        
         readPrintXML();
    }
    
    public ReadXMLForPrint(String xmlPath) {
        readPrintXML(xmlPath);
    }
    
    public static String readPrintXML(String reportName) {
        String strModuleName = "";
        String printer = "";
        try {
            Properties prop = System.getProperties();
            File f1 = new File(String.valueOf(prop.get("user.home")));
            while (f1.getParent()!=null) {
                f1 = new File(f1.getParent());
            }
            System.out.println("#$#$ root dir :"+f1);
            xmlPath = f1.getAbsolutePath();
            xmlPath+="printer.xml";

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(xmlPath));
           // normalize text representation
            doc.getDocumentElement ().normalize ();
          
            //System.out.println ("Root element of the doc is " + 
           //      doc.getDocumentElement().getNodeName());


            NodeList listOfMapping = doc.getElementsByTagName("mapping");
            int totalMappings = listOfMapping.getLength();
          //  System.out.println("Total no of mapping : " + totalMappings);

            for(int s=0; s<listOfMapping.getLength() ; s++){


                Node firstMappingNode = listOfMapping.item(s);
                if(firstMappingNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstMappingElement = (Element)firstMappingNode;

                    //-------
                    NodeList ModuleList = firstMappingElement.getElementsByTagName("report");
                    Element ModuleElement = (Element)ModuleList.item(0);

                    NodeList textMList = ModuleElement.getChildNodes();
                    String reportNameFromXML = ((Node)textMList.item(0)).getNodeValue().trim();
//                    System.out.println("Report Name : " + reportNameFromXML);
               
                    strModuleName= ((Node)textMList.item(0)).getNodeValue().trim();
                    //end
                    //-------
                    NodeList pNameList = firstMappingElement.getElementsByTagName("pname");
                    Element pNameElement = (Element)pNameList.item(0);

                    NodeList textPNList = pNameElement.getChildNodes();
                    printer = ((Node)textPNList.item(0)).getNodeValue().trim();
//                    System.out.println("Printer Name : " + printer);
                    if (reportName.equals(reportNameFromXML)) {
                        break;
                    } else {
                        printer = "";
                    }
                    
//                    NodeList noOfCopyies = firstMappingElement.getElementsByTagName("copies");
//                    Element copyElement = (Element)noOfCopyies.item(0);
//
//                    NodeList textCopyList = copyElement.getChildNodes();
//                    System.out.println("No of Copies : " + 
//                    ((Node)textCopyList.item(0)).getNodeValue().trim());
              }//end of if clause


            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        return printer;
        //System.exit (0);
    }
    
    public void readPrintXML() {
        String strModuleName="";
        cboModule.addItem("");
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(xmlPath));
           // normalize text representation
            doc.getDocumentElement ().normalize ();
          
            //System.out.println ("Root element of the doc is " + 
           //      doc.getDocumentElement().getNodeName());


            NodeList listOfMapping = doc.getElementsByTagName("mapping");
            int totalMappings = listOfMapping.getLength();
          //  System.out.println("Total no of mapping : " + totalMappings);

            for(int s=0; s<listOfMapping.getLength() ; s++){


                Node firstMappingNode = listOfMapping.item(s);
                if(firstMappingNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstMappingElement = (Element)firstMappingNode;

                    //-------
                    NodeList ModuleList = firstMappingElement.getElementsByTagName("module");
                    Element ModuleElement = (Element)ModuleList.item(0);

                    NodeList textMList = ModuleElement.getChildNodes();
                  //  System.out.println("module Name : " + 
                  //         ((Node)textMList.item(0)).getNodeValue().trim());
               
                    //Module names loaded in combobox
                    strModuleName= ((Node)textMList.item(0)).getNodeValue().trim();
                    cboModule.addItem(strModuleName);
                    //end
                    //-------
                    NodeList pNameList = firstMappingElement.getElementsByTagName("pname");
                    Element pNameElement = (Element)pNameList.item(0);

                    NodeList textPNList = pNameElement.getChildNodes();
                  //  System.out.println("Printer Name : " + 
                  //         ((Node)textPNList.item(0)).getNodeValue().trim());
              }//end of if clause


            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        //System.exit (0);
    }
    
    public String listPrinterName(String moduleName) {
         String pName="";
        try {
           String cbModule=cboModule.getSelectedItem().toString();
            if(cbModule!=null && !cbModule.equals("")) {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse (new File(xmlPath));
               // normalize text representation
                doc.getDocumentElement ().normalize ();

               // System.out.println ("Root element of the doc is " + 
               //      doc.getDocumentElement().getNodeName());


                NodeList listOfMapping = doc.getElementsByTagName("mapping");
                int totalMappings = listOfMapping.getLength();
                //System.out.println("Total no of mapping : " + totalMappings);
                modelList.clear();
                for(int s=0; s<listOfMapping.getLength() ; s++) {
                    Node firstMappingNode = listOfMapping.item(s);
                    if(firstMappingNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element firstMappingElement = (Element)firstMappingNode;
                        //-------
                        NodeList ModuleList = firstMappingElement.getElementsByTagName("module");
                        Element ModuleElement = (Element)ModuleList.item(0);

                        NodeList textMList = ModuleElement.getChildNodes();
                       // System.out.println("module Name : " + 
                      //         ((Node)textMList.item(0)).getNodeValue().trim());
                        String strModule=((Node)textMList.item(0)).getNodeValue().trim();
                    
                        if(strModule!=null && strModule.equalsIgnoreCase(cbModule)) {
                            NodeList pNameList = firstMappingElement.getElementsByTagName("pname");
                            Element pNameElement = (Element)pNameList.item(0);

                            NodeList textPNList = pNameElement.getChildNodes();
                           // System.out.println("Printer Name : " + 
                           //        ((Node)textPNList.item(0)).getNodeValue().trim());
                            pName=((Node)textPNList.item(0)).getNodeValue().trim();
                          
                        }
                     }//end of if clause
                }//end of for loop with s var
           }
        }
        catch(Exception e) {
            System.out.println("listPrinterName Error :"+e);
        }
        return pName;
    }
    public void getPrint() {
        //PrinterJob job = PrinterJob.getPrinterJob();
        //job.setPrintable(new PrintObject());
        //job.printDialog();
      
       
    }
      private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cboModule = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lstPrinter = new com.see.truetransact.uicomponent.CList();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();

        getContentPane().setLayout(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        cboModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboModuleActionPerformed(evt);
            }
        });

        getContentPane().add(cboModule);
        cboModule.setBounds(120, 20, 210, 21);

        cLabel1.setText("Module");
        getContentPane().add(cLabel1);
        cLabel1.setBounds(20, 20, 70, 20);

        lstPrinter.setModel(modelList);
        lstPrinter.setPreferredSize(new java.awt.Dimension(25, 20));
        getContentPane().add(lstPrinter);
        lstPrinter.setBounds(120, 70, 210, 40);

        btnPrint.setText("Print");
        btnPrint.setEnabled(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        getContentPane().add(btnPrint);
        btnPrint.setBounds(340, 80, 110, 27);

        cLabel2.setText("Printer Name");
        getContentPane().add(cLabel2);
        cLabel2.setBounds(20, 70, 80, 18);

        pack();
    }//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        getPrint();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void cboModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboModuleActionPerformed
        // TODO add your handling code here:
          String cbModule=cboModule.getSelectedItem().toString();
        if(cbModule!=null && !cbModule.equalsIgnoreCase("")  )
        {
           if(listPrinterName(cbModule)!=null && !listPrinterName(cbModule).equalsIgnoreCase(""))
                 modelList.addElement(listPrinterName(cbModule));
        }
    }//GEN-LAST:event_cboModuleActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        new ReadXMLForPrint().show();
        String printer = ReadXMLForPrint.readPrintXML("ReceiptPayment");
        System.out.println("#$#$ Printer name for ReceiptPayment:"+printer);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CComboBox cboModule;
    private com.see.truetransact.uicomponent.CList lstPrinter;
    // End of variables declaration//GEN-END:variables
    
}

