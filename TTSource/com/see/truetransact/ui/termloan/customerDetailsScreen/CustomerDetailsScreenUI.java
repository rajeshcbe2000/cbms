/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CustomerDetailsScreenUI.java
 *
 * Created on August 10, 2004, 3:11 PM
 */

package com.see.truetransact.ui.termloan.customerDetailsScreen;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import java.util.List;
import java.awt.Toolkit;
import java.util.HashMap;
import java.awt.Dimension;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.customer.CustomerUISupport;
import java.util.*;

/**
 *
 * @author  shanmuga
 */
public class CustomerDetailsScreenUI extends com.see.truetransact.uicomponent.CDialog implements Observer {
    
    
    
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates new form BeanForm */
    public CustomerDetailsScreenUI(String custId) {
        initComponents();      
        setupInit(custId);
    }
    
    public CustomerDetailsScreenUI(String custId,String addressType) {
        initComponents();      
        setupInit(custId,addressType);
    }
    
    private void setupInit(String custId){
        setFieldNames();
        internationalize();
        setupScreen();
        showingCustomerDetails(custId);
    }
    
    private void setupInit(String custId,String addressType){
        setFieldNames();
        internationalize();
        setupScreen();
        showingCustomerDetails(custId,addressType);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblCustomerId.setName("lblCustomerId");
        lblCustomerName.setName("lblCustomerName");
        lblDOB.setName("lblDOB");
        lblStreet.setName("lblStreet");
        lblPincode1.setName("lblPincode1");
        lblCity.setName("lblCity");
        lblState.setName("lblState");
        lblCountry.setName("lblCountry");
        lblPincode.setName("lblPincode");
        lblPostOffice.setName("lblPostOffice");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {

    }
    
    private void setObservable(){
    }
    
    private void setupScreen() {
        setTitle("Customer Details ");// + CommonUtil.convertObjToStr(((HashMap) paramMap.get(CommonConstants.MAP_WHERE)).get(ACTNUM)));
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(430, 280);
        
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
            super.show();
    }

    public void setVisible(boolean visible) {
            super.setVisible(visible);
    }
     private void showingCustomerDetails(String custId){
        HashMap customerMap = new HashMap();
        customerMap.put("CUST_ID",custId);
        customerMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",customerMap);
        if(custListData !=null && custListData.size()>0) {
            customerMap = (HashMap) custListData.get(0);
            System.out.println("customerMap######"+customerMap);
            lblCustomerIdValue.setText(String.valueOf(custId));
            lblCustomerNameValue.setText(CommonUtil.convertObjToStr(customerMap.get("NAME")));
            lblDOBValue.setText(CommonUtil.convertObjToStr(customerMap.get("DOB"))+" Age:"+CommonUtil.convertObjToStr(customerMap.get("AGE")));
            lblCasteValue.setText(CommonUtil.convertObjToStr(customerMap.get("CASTDESC")));
            lblDesamValue.setText(CommonUtil.convertObjToStr(customerMap.get("DESAM")));
            lblStreetValue.setText(CommonUtil.convertObjToStr(customerMap.get("STREET")));
            lblAreaValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA")));
            lblCityValue.setText(CommonUtil.convertObjToStr(customerMap.get("CITY1")));
            lblStateValue.setText(CommonUtil.convertObjToStr(customerMap.get("STATE1")));
            lblCountryVaue.setText(String.valueOf("India"));
            lblPincodeValue.setText(CommonUtil.convertObjToStr(customerMap.get("PIN_CODE")));
            lblGuardianNameVal.setText(CommonUtil.convertObjToStr(customerMap.get("CARE_OF_NAME")));//Added By Kannan AR for Mantis ID : 0006767
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("LAND LINE"))){
                // If the Phone Type is not null and if it is a Land Line
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("FAX"))){
                // If the Phone Type is not null and if it is a Fax
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
             if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("MOBILE"))){
                // If the Phone Type is not null and if it is a Fax
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
             if (customerMap.containsKey("POSTOFFICE")&& customerMap.get("POSTOFFICE") != null){
                lblPostOfficeValue.setText(CommonUtil.convertObjToStr(customerMap.get("POSTOFFICE")));
            }
        }
        showProofPhoto(custId);
    }
     
    private void showProofPhoto(String custId) {
        HashMap transactionMap = new HashMap();
        transactionMap.put(CommonConstants.MAP_WHERE, custId);
        transactionMap.put("TO_DISPLAY_PROOF_PHOTO", "TO_DISPLAY_PROOF_PHOTO");
        transactionMap.put("CUST_ID", custId);
        HashMap map;
        HashMap result = new HashMap();
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "CustomerJNDI");
            map.put(CommonConstants.HOME, "customer.CustomerHome");
            map.put(CommonConstants.REMOTE, "customer.Customer");
            result = proxy.executeQuery(transactionMap, map);
            if (result != null) {
                if (result.size() > 0 && result.containsKey("PROOF_PHOTO") && result.get("PROOF_PHOTO") != null) {
                    lblProofPhoto.setIcon(new javax.swing.ImageIcon((byte[]) result.get("PROOF_PHOTO")));
                }else{
                    lblProofPhoto.setText("ID Proof not submitted");
                }
            }else{
                lblProofPhoto.setText("ID Proof not submitted");
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        transactionMap = null;
    }
     
    private void showingCustomerDetails(String custId,String addressType){
        HashMap customerMap = new HashMap();
        customerMap.put("CUST_ID",custId);
        customerMap.put("ADDR_TYPE",addressType);
        customerMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        List custListData = null;
        custListData = ClientUtil.executeQuery("getSelectAccInfoDisplayWithAddressType",customerMap);
        if(custListData !=null && custListData.size()>0) {
            customerMap = (HashMap) custListData.get(0);
        }else{
            custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",customerMap);
            if(custListData !=null && custListData.size()>0) {
                customerMap = (HashMap) custListData.get(0);
            }
        }
            System.out.println("customerMap######"+customerMap);
            lblCustomerIdValue.setText(String.valueOf(custId));
            lblCustomerNameValue.setText(CommonUtil.convertObjToStr(customerMap.get("NAME")));
            lblDOBValue.setText(CommonUtil.convertObjToStr(customerMap.get("DOB"))+" Age:"+CommonUtil.convertObjToStr(customerMap.get("AGE")));
            lblCasteValue.setText(CommonUtil.convertObjToStr(customerMap.get("CASTDESC")));
            lblDesamValue.setText(CommonUtil.convertObjToStr(customerMap.get("DESAM")));
            lblStreetValue.setText(CommonUtil.convertObjToStr(customerMap.get("STREET")));
            lblAreaValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA")));
            lblCityValue.setText(CommonUtil.convertObjToStr(customerMap.get("CITY1")));
            lblStateValue.setText(CommonUtil.convertObjToStr(customerMap.get("STATE1")));
            lblCountryVaue.setText(String.valueOf("India"));
            lblPincodeValue.setText(CommonUtil.convertObjToStr(customerMap.get("PIN_CODE")));
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("LAND LINE"))){
                // If the Phone Type is not null and if it is a Land Line
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("FAX"))){
                // If the Phone Type is not null and if it is a Fax
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
             if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("MOBILE"))){
                // If the Phone Type is not null and if it is a Fax
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE"))+CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
       // }else{
            
       // }
             
             showProofPhoto(custId);
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
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        lblPincodeValue = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblCountryVaue = new com.see.truetransact.uicomponent.CLabel();
        lblStateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCityValue = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblDOB = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblDOBValue = new com.see.truetransact.uicomponent.CLabel();
        lblStreetValue = new com.see.truetransact.uicomponent.CLabel();
        lblPincode1 = new com.see.truetransact.uicomponent.CLabel();
        lblPhoneNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblAreaValue = new com.see.truetransact.uicomponent.CLabel();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        lblCasteValue = new com.see.truetransact.uicomponent.CLabel();
        lblDesam = new com.see.truetransact.uicomponent.CLabel();
        lblDesamValue = new com.see.truetransact.uicomponent.CLabel();
        lblGuardianName = new com.see.truetransact.uicomponent.CLabel();
        lblGuardianNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblPostOffice = new com.see.truetransact.uicomponent.CLabel();
        lblPostOfficeValue = new com.see.truetransact.uicomponent.CLabel();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        lblProofPhoto = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();

        setTitle("Customer Details");
        setMinimumSize(new java.awt.Dimension(480, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panViewPhotoSign.setMaximumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setMinimumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setPreferredSize(new java.awt.Dimension(560, 480));
        panViewPhotoSign.setLayout(new java.awt.GridBagLayout());

        lblPincode.setText("Pin Code  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblPincode, gridBagConstraints);

        lblPincodeValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblPincodeValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblPincodeValue, gridBagConstraints);

        lblCountry.setText("Country  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCountry, gridBagConstraints);

        lblCountryVaue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCountryVaue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCountryVaue, gridBagConstraints);

        lblStateValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblStateValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblStateValue, gridBagConstraints);

        lblCityValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCityValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCityValue, gridBagConstraints);

        lblCity.setText("City  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCity, gridBagConstraints);

        lblState.setText("State  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblState, gridBagConstraints);

        lblStreet.setText("Street  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblStreet, gridBagConstraints);

        lblDOB.setText("Date of Birth  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblDOB, gridBagConstraints);

        lblCustomerName.setText("Customer Name  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCustomerName, gridBagConstraints);

        lblCustomerId.setText("Customer Id  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCustomerId, gridBagConstraints);

        lblCustomerIdValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerIdValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCustomerIdValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCustomerIdValue, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCustomerNameValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCustomerNameValue, gridBagConstraints);

        lblDOBValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblDOBValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblDOBValue, gridBagConstraints);

        lblStreetValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblStreetValue.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblStreetValue, gridBagConstraints);

        lblPincode1.setText("Phone No  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblPincode1, gridBagConstraints);

        lblPhoneNoValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblPhoneNoValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblPhoneNoValue, gridBagConstraints);

        lblArea.setText("Area  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblArea, gridBagConstraints);

        lblAreaValue.setMaximumSize(new java.awt.Dimension(300, 15));
        lblAreaValue.setMinimumSize(new java.awt.Dimension(300, 15));
        lblAreaValue.setPreferredSize(new java.awt.Dimension(300, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblAreaValue, gridBagConstraints);

        lblCaste.setText("Caste : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCaste, gridBagConstraints);

        lblCasteValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblCasteValue.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCasteValue, gridBagConstraints);

        lblDesam.setText("Desam :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 6);
        panViewPhotoSign.add(lblDesam, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblDesamValue, gridBagConstraints);

        lblGuardianName.setText("Guardian/Father Name  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblGuardianName, gridBagConstraints);

        lblGuardianNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblGuardianNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblGuardianNameVal.setMinimumSize(new java.awt.Dimension(150, 15));
        lblGuardianNameVal.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblGuardianNameVal, gridBagConstraints);

        lblPostOffice.setText("Post Office    :");
        lblPostOffice.setToolTipText("");
        lblPostOffice.setMaximumSize(new java.awt.Dimension(88, 18));
        lblPostOffice.setMinimumSize(new java.awt.Dimension(88, 18));
        lblPostOffice.setPreferredSize(new java.awt.Dimension(88, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblPostOffice, gridBagConstraints);

        lblPostOfficeValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblPostOfficeValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblPostOfficeValue, gridBagConstraints);

        cButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PHOTO_SIGN.gif"))); // NOI18N
        cButton1.setToolTipText("Photo & Signature");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 133, 0, 0);
        panViewPhotoSign.add(cButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = -38;
        gridBagConstraints.ipady = -174;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 25, 0, 72);
        getContentPane().add(panViewPhotoSign, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblProofPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProofPhotoMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(lblProofPhoto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 248;
        gridBagConstraints.ipady = 103;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 11, 10);
        cPanel1.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 61, 32, 0);
        getContentPane().add(cPanel1, gridBagConstraints);

        cLabel1.setText("Proof");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 132, 0, 0);
        getContentPane().add(cLabel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void lblProofPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblProofPhotoMouseClicked
        // TODO add your handling code here:
        CustomerUISupport objCustomerUISupport = new CustomerUISupport();
            if (evt.getClickCount() == 2) {
            objCustomerUISupport.zoomImage(lblProofPhoto);
        }
    }//GEN-LAST:event_lblProofPhotoMouseClicked
    
    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        // TODO add your handling code here:
        new com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI(lblCustomerIdValue.getText(), "NewActOpening").show();
    }//GEN-LAST:event_cButton1ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaValue;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblCasteValue;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityValue;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCountryVaue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDOB;
    private com.see.truetransact.uicomponent.CLabel lblDOBValue;
    private com.see.truetransact.uicomponent.CLabel lblDesam;
    private com.see.truetransact.uicomponent.CLabel lblDesamValue;
    private com.see.truetransact.uicomponent.CLabel lblGuardianName;
    private com.see.truetransact.uicomponent.CLabel lblGuardianNameVal;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNoValue;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPincode1;
    private com.see.truetransact.uicomponent.CLabel lblPincodeValue;
    private com.see.truetransact.uicomponent.CLabel lblPostOffice;
    private com.see.truetransact.uicomponent.CLabel lblPostOfficeValue;
    private com.see.truetransact.uicomponent.CLabel lblProofPhoto;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateValue;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetValue;
    private com.see.truetransact.uicomponent.CPanel panViewPhotoSign;
    // End of variables declaration//GEN-END:variables
    public static void main(String args[]) {
        
    }
}
