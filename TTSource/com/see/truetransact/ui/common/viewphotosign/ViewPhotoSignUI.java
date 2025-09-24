/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewPhotoSignUI.java
 *
 * Created on August 10, 2004, 3:11 PM
 */

package com.see.truetransact.ui.common.viewphotosign;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.customer.CustomerUISupport;
import java.awt.Color;
import java.util.*;

/**
 *
 * @author  shanmuga
 */
public class ViewPhotoSignUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    private HashMap paramMap = null;
    private ViewPhotoSignRB resourceBundle = new ViewPhotoSignRB();
    private ViewPhotoSignOB observable;
    
    private final static Logger log = Logger.getLogger(ViewPhotoSignUI.class);
    private final String ACTNUM = "ACTNUM";
    private String actNo="";
    
    private CustomerUISupport objCustomerUISupport = null;
    
    /** Creates new form BeanForm */
    public ViewPhotoSignUI(HashMap paramMap) {
        initComponents();
        
        this.paramMap = paramMap;        
        setupInit();
    }
    
    /** Account Number Constructor */
    public ViewPhotoSignUI(String actNum, String prodType) {
        initComponents();
        
        HashMap mapParam = new HashMap();
        HashMap where = new HashMap();
        where.put("ACTNUM", actNum);
        actNo=actNum;
        if(prodType !=null && prodType.length()>0 && prodType.equals("AD"))
            prodType="TL";
        mapParam.put(CommonConstants.MAP_NAME, "getAcctPhotoSignDetails" + prodType);
        mapParam.put(CommonConstants.MAP_WHERE, where);
        mapParam.put("PRODUCT_TYPE",prodType); // 22-08-2019
        this.paramMap = mapParam;

        try {
            HashMap map = new HashMap();
            if( !prodType.equals("SH") && !prodType.equals("LOCKER") && !prodType.equals("NewActOpening") && !prodType.equals("SA")){
            map.put(CommonConstants.MAP_NAME, "getAuthorizedInstruction" + prodType);
            map.put(CommonConstants.MAP_WHERE, where);
            if (!ClientUtil.setTableModel(map, tblInstructions, false)) {
                lblInstruction.setVisible(false);
                panInstructions.setVisible(false);
            }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        setupInit();
    }
    
    private void setupInit(){
        setFieldNames();
        internationalize();
        setObservable();
        observable.setActNum(actNo);
        populateData(paramMap);
        setupScreen();
        objCustomerUISupport = new CustomerUISupport();
        lblPhoto.setToolTipText("Double click to view full Photo");
        lblSign.setToolTipText("Double click to view full Signature");
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblPhoto.setName("lblPhoto");
        lblSign.setName("lblSign");
        panAccDetails.setName("panAccDetails");
        panPhoto.setName("panPhoto");
        panSign.setName("panSign");
        srpAccDetails.setName("srpAccDetails");
        srpPhoto.setName("srpPhoto");
        srpSign.setName("srpSign");
        tblAccDetails.setName("tblAccDetails");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSign.setText(resourceBundle.getString("lblSign"));
        ((javax.swing.border.TitledBorder)panPhoto.getBorder()).setTitle(resourceBundle.getString("panPhoto"));
        ((javax.swing.border.TitledBorder)panSign.getBorder()).setTitle(resourceBundle.getString("panSign"));
        lblPhoto.setText(resourceBundle.getString("lblPhoto"));
    }
    
    private void setObservable(){
        observable = new ViewPhotoSignOB();
        observable.addObserver(this);
    }
    
    public void populateData(HashMap mapID){
        try {
            log.info("populateData...");
            observable.populateData(mapID, tblAccDetails);
            System.out.println("nithya : testing custId :: " + tblAccDetails.getRowCount());
            if(tblAccDetails.getRowCount() > 0){
               ArrayList lst = (ArrayList) ((ArrayList) observable.getTableModel().getDataArrayList()).get(0);        
               String strCustID = CommonUtil.convertObjToStr(lst.get(0)); 
               System.out.println("strCustID :: " + strCustID);
               //customer.getCustomerPANNumber
               //customer.getCustomerAadharCardNumber
               HashMap panMap = new HashMap();
               panMap.put("CUST_ID",strCustID);
               panMap.put("ACT_NUM",observable.getActNum());
               List panList = ClientUtil.executeQuery("customer.getCustomerPANNumber",panMap);             
               List aadharList = ClientUtil.executeQuery("customer.getCustomerAadharCardNumber",panMap); 
               if(panList != null && panList.size() > 0){
                   HashMap panLstMap = (HashMap)panList.get(0);
                   if(panLstMap.containsKey("PAN_NUMBER") && panLstMap.get("PAN_NUMBER") != null && !panLstMap.get("PAN_NUMBER").equals((""))){
                       lblPanVal.setForeground(Color.red);
                       lblPanVal.setText(CommonUtil.convertObjToStr(panLstMap.get("PAN_NUMBER")));
                   }else{
                       lblPan.setVisible(false);
                       lblPanVal.setVisible(false);
                   }
               }else{
                   lblPan.setVisible(false);
                   lblPanVal.setVisible(false);
               }
               if(aadharList != null && aadharList.size() > 0){
                   HashMap aadharLstMap = (HashMap)aadharList.get(0);
                   if(aadharLstMap.containsKey("AADHAR_CARD") && aadharLstMap.get("AADHAR_CARD") != null){
                       lblAadharVal.setText(CommonUtil.convertObjToStr(aadharLstMap.get("AADHAR_CARD")));
                       lblAadharVal.setForeground(Color.red);
                   }else{
                       lblAadharVal.setVisible(false);
                       lblAadhar.setVisible(false);
                   }
               }else{
                       lblAadharVal.setVisible(false);
                       lblAadhar.setVisible(false);
               }                            
                List custPhoneList = ClientUtil.executeQuery("customer.getCustomerPhoneNumberList", panMap);
                if (custPhoneList != null && custPhoneList.size() > 0) {
                    String phoneNumber = "";
                    for(int i=0; i<custPhoneList.size(); i++){
                        HashMap custPhoneMap = (HashMap) custPhoneList.get(i);
                        phoneNumber = phoneNumber + "   "+ CommonUtil.convertObjToStr(custPhoneMap.get("PHONE_NO"));
                    }
                    if(phoneNumber.length() > 0){
                        lblPhoneList.setText("Phone No  " + phoneNumber);
                    }                    
                } else {                   
                    lblPhoneList.setVisible(false);
                }
           }
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    private void setupScreen() {
//        setModal(true);
        setTitle("Customer Details for Account Number " + CommonUtil.convertObjToStr(((HashMap) paramMap.get(CommonConstants.MAP_WHERE)).get(ACTNUM)));
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(570, 470);
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    public void show() {
        if (observable.isAvailable()) {
            super.show();
        }
    }

    public void setVisible(boolean visible) {
        if (observable.isAvailable()) {
            super.setVisible(visible);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panViewPhotoSign = new com.see.truetransact.uicomponent.CPanel();
        panAccDetails = new com.see.truetransact.uicomponent.CPanel();
        srpAccDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAccDetails = new com.see.truetransact.uicomponent.CTable();
        panPhoto = new com.see.truetransact.uicomponent.CPanel();
        srpPhoto = new com.see.truetransact.uicomponent.CScrollPane();
        lblPhoto = new com.see.truetransact.uicomponent.CLabel();
        panSign = new com.see.truetransact.uicomponent.CPanel();
        srpSign = new com.see.truetransact.uicomponent.CScrollPane();
        lblSign = new com.see.truetransact.uicomponent.CLabel();
        panInstructions = new com.see.truetransact.uicomponent.CPanel();
        scrInstructions = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstructions = new com.see.truetransact.uicomponent.CTable();
        lblInstruction = new com.see.truetransact.uicomponent.CLabel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        panCustPAN = new com.see.truetransact.uicomponent.CPanel();
        lblPan = new com.see.truetransact.uicomponent.CLabel();
        lblPanVal = new com.see.truetransact.uicomponent.CLabel();
        panCustAadhar = new com.see.truetransact.uicomponent.CPanel();
        lblAadhar = new com.see.truetransact.uicomponent.CLabel();
        lblAadharVal = new com.see.truetransact.uicomponent.CLabel();
        lblPhoneList = new com.see.truetransact.uicomponent.CLabel();

        setTitle("Customer Details");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panViewPhotoSign.setMaximumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setMinimumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setPreferredSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setLayout(new java.awt.GridBagLayout());

        panAccDetails.setMaximumSize(new java.awt.Dimension(554, 204));
        panAccDetails.setMinimumSize(new java.awt.Dimension(554, 204));
        panAccDetails.setPreferredSize(new java.awt.Dimension(554, 204));
        panAccDetails.setLayout(new java.awt.GridBagLayout());

        srpAccDetails.setMaximumSize(new java.awt.Dimension(550, 200));
        srpAccDetails.setMinimumSize(new java.awt.Dimension(550, 200));
        srpAccDetails.setPreferredSize(new java.awt.Dimension(550, 200));

        tblAccDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAccDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccDetailsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAccDetailsMousePressed(evt);
            }
        });
        srpAccDetails.setViewportView(tblAccDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccDetails.add(srpAccDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewPhotoSign.add(panAccDetails, gridBagConstraints);

        panPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Photograph"));
        panPhoto.setMaximumSize(new java.awt.Dimension(248, 175));
        panPhoto.setMinimumSize(new java.awt.Dimension(248, 175));
        panPhoto.setPreferredSize(new java.awt.Dimension(248, 175));
        panPhoto.setLayout(new java.awt.GridBagLayout());

        srpPhoto.setMaximumSize(new java.awt.Dimension(228, 150));
        srpPhoto.setMinimumSize(new java.awt.Dimension(228, 150));
        srpPhoto.setPreferredSize(new java.awt.Dimension(228, 150));

        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPhotoMouseClicked(evt);
            }
        });
        srpPhoto.setViewportView(lblPhoto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoto.add(srpPhoto, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewPhotoSign.add(panPhoto, gridBagConstraints);

        panSign.setBorder(javax.swing.BorderFactory.createTitledBorder("Signature"));
        panSign.setMaximumSize(new java.awt.Dimension(248, 175));
        panSign.setMinimumSize(new java.awt.Dimension(248, 175));
        panSign.setPreferredSize(new java.awt.Dimension(248, 175));
        panSign.setLayout(new java.awt.GridBagLayout());

        srpSign.setMaximumSize(new java.awt.Dimension(228, 150));
        srpSign.setMinimumSize(new java.awt.Dimension(228, 150));
        srpSign.setPreferredSize(new java.awt.Dimension(228, 150));

        lblSign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSignMouseClicked(evt);
            }
        });
        srpSign.setViewportView(lblSign);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSign.add(srpSign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewPhotoSign.add(panSign, gridBagConstraints);

        panInstructions.setMinimumSize(new java.awt.Dimension(24, 75));
        panInstructions.setLayout(new java.awt.GridBagLayout());

        tblInstructions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrInstructions.setViewportView(tblInstructions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInstructions.add(scrInstructions, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewPhotoSign.add(panInstructions, gridBagConstraints);

        lblInstruction.setText("Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panViewPhotoSign.add(lblInstruction, gridBagConstraints);

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewPhotoSign.add(btnView, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 19, 0);
        panViewPhotoSign.add(panCustPAN, gridBagConstraints);

        lblPan.setText("PAN Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 11);
        panViewPhotoSign.add(lblPan, gridBagConstraints);

        lblPanVal.setText("cLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panViewPhotoSign.add(lblPanVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        panViewPhotoSign.add(panCustAadhar, gridBagConstraints);

        lblAadhar.setText("Aadhar Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panViewPhotoSign.add(lblAadhar, gridBagConstraints);

        lblAadharVal.setText("cLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panViewPhotoSign.add(lblAadharVal, gridBagConstraints);

        lblPhoneList.setText("Phone Number");
        lblPhoneList.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panViewPhotoSign.add(lblPhoneList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panViewPhotoSign, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void lblSignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSignMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {
            objCustomerUISupport.zoomImage(lblSign);
        }
    }//GEN-LAST:event_lblSignMouseClicked

    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked
        // TODO add your handling code here:
    if (evt.getClickCount()==2) {
        objCustomerUISupport.zoomImage(lblPhoto);
    }
    }//GEN-LAST:event_lblPhotoMouseClicked
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        if(tblAccDetails.getSelectedRow()>=0){   //This if condition added by Rajesh.
        IndividualCustUI objCust =  new IndividualCustUI();
        HashMap map = new HashMap();
        ArrayList lst = (ArrayList) ((ArrayList) observable.getTableModel().getDataArrayList()).get(tblAccDetails.getSelectedRow());
        System.out.println("ArrayList() " + lst);
        String strCustID = CommonUtil.convertObjToStr(lst.get(0));
        
        map.put("CUSTOMER ID", strCustID);
        map.put("BRANCH_CODE", CommonUtil.convertObjToStr(lst.get(lst.size()-1)));
        map.put("WHERE", strCustID);
        objCust.setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
        objCust.fillData(map);
        com.see.truetransact.ui.TrueTransactMain.showScreen(objCust);
        }else
            ClientUtil.showAlertWindow("Please select a row");
    }//GEN-LAST:event_btnViewActionPerformed
    private void tblAccDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccDetailsMousePressed
        tblAccDetailsMousePressed();
    }//GEN-LAST:event_tblAccDetailsMousePressed

    private void tblAccDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccDetailsMouseClicked
        // TODO add your handling code here:
        
        if(tblAccDetails.getRowCount() > 0){               
               ArrayList lst = (ArrayList) ((ArrayList) observable.getTableModel().getDataArrayList()).get(tblAccDetails.getSelectedRow());        
               String strCustID = CommonUtil.convertObjToStr(lst.get(0)); 
               System.out.println("strCustID :: " + strCustID);
               //customer.getCustomerPANNumber
               //customer.getCustomerAadharCardNumber
               HashMap panMap = new HashMap();
               panMap.put("CUST_ID",strCustID);
               panMap.put("ACT_NUM",observable.getActNum());
               List panList = ClientUtil.executeQuery("customer.getCustomerPANNumber",panMap);             
               List aadharList = ClientUtil.executeQuery("customer.getCustomerAadharCardNumber",panMap); 
               if(panList != null && panList.size() > 0){
                   HashMap panLstMap = (HashMap)panList.get(0);
                   if(panLstMap.containsKey("PAN_NUMBER") && panLstMap.get("PAN_NUMBER") != null && !panLstMap.get("PAN_NUMBER").equals((""))){
                       lblPan.setVisible(true);
                       lblPanVal.setVisible(true);
                       lblPanVal.setForeground(Color.red);
                       lblPanVal.setText(CommonUtil.convertObjToStr(panLstMap.get("PAN_NUMBER")));
                   }else{
                       lblPan.setVisible(false);
                       lblPanVal.setVisible(false);
                   }
               }else{
                   lblPan.setVisible(false);
                   lblPanVal.setVisible(false);
               }
               if(aadharList != null && aadharList.size() > 0){
                   HashMap aadharLstMap = (HashMap)aadharList.get(0);
                   if(aadharLstMap.containsKey("AADHAR_CARD") && aadharLstMap.get("AADHAR_CARD") != null){
                       lblAadharVal.setVisible(true);
                       lblAadhar.setVisible(true);
                       lblAadharVal.setText(CommonUtil.convertObjToStr(aadharLstMap.get("AADHAR_CARD")));
                       lblAadharVal.setForeground(Color.red);
                   }else{
                       lblAadharVal.setVisible(false);
                       lblAadhar.setVisible(false);
                   }
               }else{
                       lblAadharVal.setVisible(false);
                       lblAadhar.setVisible(false);
               }                            
                List custPhoneList = ClientUtil.executeQuery("customer.getCustomerPhoneNumberList", panMap);
                if (custPhoneList != null && custPhoneList.size() > 0) {
                    String phoneNumber = "";
                    for(int i=0; i<custPhoneList.size(); i++){
                        HashMap custPhoneMap = (HashMap) custPhoneList.get(i);
                        phoneNumber = phoneNumber + "   "+ CommonUtil.convertObjToStr(custPhoneMap.get("PHONE_NO"));
                    }
                    if(phoneNumber.length() > 0){
                        lblPhoneList.setVisible(true);
                        lblPhoneList.setText("Phone No  " + phoneNumber);
                    }                    
                } else {                   
                    lblPhoneList.setVisible(false);
                }
           }
        
    }//GEN-LAST:event_tblAccDetailsMouseClicked
    
    private void tblAccDetailsMousePressed(){
            String prodType = CommonUtil.convertObjToStr(paramMap.get("PRODUCT_TYPE"));
            lblPhoto.setIcon(null);
            observable.setPhotoFile(new byte[0]);
            lblSign.setIcon(null);
            observable.setSignFile(new byte[0]);
            observable.populatePhotoSign(tblAccDetails.getSelectedRow(),prodType);
            observable.fillPhotoSign(lblPhoto, lblSign);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblAadhar;
    private com.see.truetransact.uicomponent.CLabel lblAadharVal;
    private com.see.truetransact.uicomponent.CLabel lblInstruction;
    private com.see.truetransact.uicomponent.CLabel lblPan;
    private com.see.truetransact.uicomponent.CLabel lblPanVal;
    private com.see.truetransact.uicomponent.CLabel lblPhoneList;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblSign;
    private com.see.truetransact.uicomponent.CPanel panAccDetails;
    private com.see.truetransact.uicomponent.CPanel panCustAadhar;
    private com.see.truetransact.uicomponent.CPanel panCustPAN;
    private com.see.truetransact.uicomponent.CPanel panInstructions;
    private com.see.truetransact.uicomponent.CPanel panPhoto;
    private com.see.truetransact.uicomponent.CPanel panSign;
    private com.see.truetransact.uicomponent.CPanel panViewPhotoSign;
    private com.see.truetransact.uicomponent.CScrollPane scrInstructions;
    private com.see.truetransact.uicomponent.CScrollPane srpAccDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpPhoto;
    private com.see.truetransact.uicomponent.CScrollPane srpSign;
    private com.see.truetransact.uicomponent.CTable tblAccDetails;
    private com.see.truetransact.uicomponent.CTable tblInstructions;
    // End of variables declaration//GEN-END:variables
    public static void main(String args[]) {
//        HashMap mapParam = new HashMap();
//        HashMap where = new HashMap();
//        where.put("ACTNUM", "OA060863");
//        mapParam.put(CommonConstants.MAP_NAME, "getAcctPhotoSignDetails");
//        mapParam.put(CommonConstants.MAP_WHERE, where);
//        
//        new ViewPhotoSignUI(mapParam).show();
        new ViewPhotoSignUI("OA060897", "OA").show();
    }
}
