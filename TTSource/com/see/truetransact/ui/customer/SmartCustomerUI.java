/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SmartCustomerUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.customer;

import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.HashMap;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.mdsapplication.mdschangeofmember.MDSChangeofMemberUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.uicomponent.CButton;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CRadioButton;
import com.sun.tools.jdi.LinkedHashMap;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Suresh
 */
public  class SmartCustomerUI extends CInternalFrame implements Observer {
    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private boolean goldLoanFlag = false;
    private boolean changeMemberFlag = false;
    GoldLoanUI parentGoldLoanUI = null;
    MDSChangeofMemberUI parentChangeMemberUI = null;
    private String custId = null;
    private String memNo = null;
    private String memName = null;
    Date currDt = null;
    public String branchID;
    public HashMap sourceMap;
    CInternalFrame parent = null;
    private TransDetailsUI transDetails = null;
    final int BRANCH = 2;
    int vieType = -1;
    final ArrayList TabTitle = new ArrayList();
    final ArrayList custTitle = new ArrayList();
    HashMap branchMap = new HashMap();
    private String asAnWhenCustomer = new String();
    private HashMap linkMap = new HashMap();
    private HashMap  customerMap = new HashMap();
    

    public HashMap getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(HashMap linkMap) {
        this.linkMap = linkMap;
    }

    public String getAsAnWhenCustomer() {
        return asAnWhenCustomer;
    }

    public void setAsAnWhenCustomer(String asAnWhenCustomer) {
        this.asAnWhenCustomer = asAnWhenCustomer;
    }

    public SmartCustomerUI() {
        initForm();
    }

//    public SmartCustomerUI(GoldLoanUI parent) {
//        this.parentGoldLoanUI = parent;
//        this.parent = parent;
//        goldLoanFlag = true;
//        initForm();
//        show();
//    }

//    public SmartCustomerUI(MDSChangeofMemberUI parent) {
//        this.parentChangeMemberUI = parent;
//        changeMemberFlag = true;
//        initForm();
//        show();
//    }

//    public SmartCustomerUI(CInternalFrame parent) {
//        this.parent = parent;
//        initForm();
//        show();
//    }

//    public SmartCustomerUI(CInternalFrame parent, HashMap map) {
//        if (sourceMap == null) {
//            sourceMap = new HashMap();
//        }
//        sourceMap.putAll(map);
//        this.parent = parent;
//        initForm();
//        show();
//    }

    public SmartCustomerUI(HashMap customerMap) {
        if(customerMap!=null && customerMap.size()>0){
            this.customerMap = customerMap;
        }
        initForm();
        show();
    }
        
    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        setMaxLengths();
        setFieldNames();
        internationalize();
        branchID = TrueTransactMain.BRANCH_ID;        
        currDt = ClientUtil.getCurrentDate();
        
        //Added By Kannan
        //cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[]{" ", "Starts with", "Ends with", "Exact Match"}));
        transDetails = new TransDetailsUI(panCommontransDetails);
        transDetails.setSourceScreen("CHECK_CUSTOMER");   //Added BY Suresh
        if (parent != null || goldLoanFlag) {
            panAccountdetails.setVisible(false);
            //srpRepaymentCTable.setPreferredSize(new java.awt.Dimension(800, 250));
            this.setSize(825, 550);
        }
        branchMap = TrueTransactMain.BRANCHINFO;
        txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
        lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        TabTitle.add("ACT_NUM");
        TabTitle.add("PROD_TYPE");
        TabTitle.add("PROD_ID");
        TabTitle.add("BALANCE");
        showingCustomerDetails(customerMap);
        ClientUtil.enableDisable(panCustomerDetails, false);
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
        txtCustomerName.setMaxLength(128);
        txtCareOfName.setMaxLength(128);
        txtPanNO.setMaxLength(10);
        txtPhoneNumber.setValidation(new NumericValidation());
        txtMemberNo.setAllowAll(true);
        txtEmployeeNo.setAllowAll(true);
        txtCustomerID.setAllowAll(true);
        txtBranchId.setAllowNumber(true);
        //txtAccountNumber.setAllowNumber(true);
    }

    private void showingCustomerDetails(HashMap customerMap){
            //txtCustomerName.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));//NAME
            //Changed By Kannan AR
            txtCustomerName.setText(CommonUtil.convertObjToStr((customerMap.get("Name") == null) ? customerMap.get("NAME") : customerMap.get("Name")));
            txtMemberNo.setText(CommonUtil.convertObjToStr(customerMap.get("MEMBERSHIP_NO")));
            txtAddress.setText(CommonUtil.convertObjToStr(customerMap.get("STREET"))+CommonUtil.convertObjToStr(customerMap.get("AREA")));
            txtPhoneNumber.setText(CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));            
            txtUniqueId.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            txtCustomerID.setText(CommonUtil.convertObjToStr(customerMap.get("CUST_ID")));
            txtEmployeeNo.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            txtUniqueIdNo.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            txtPanNO.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            txtPassPortNo.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            tdtDtOfBirth.setDateValue(CommonUtil.convertObjToStr(customerMap.get("DOB")));
            txtBranchId.setText(TrueTransactMain.BRANCH_ID);
            populateAccountDetails();
    }
    
    private void populateAccountDetails() {            
            if (txtCustomerID.getText()!=null  && txtCustomerID.getText().length() > 0) {
                HashMap where = new HashMap();
                HashMap viewMap = new HashMap();
                where.put("CHECK_CUSTID_SCREEN","CHECK_CUSTID_SCREEN");
                viewMap.put(CommonConstants.MAP_NAME, "getSelectAssetDetails");
                where.put("CUST_ID", txtCustomerID.getText().toUpperCase());
                viewMap.put(CommonConstants.MAP_WHERE, where);
                //Asset Accounts
                try {
                    populateAssetData(viewMap, tblAssetList);
                } catch (Exception e) {
                    System.err.println("Exception " + e.toString() + "Caught");
                    e.printStackTrace();
                }
                //Liability Accounts
                viewMap = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "getSelectLiabilityDetails");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                try {
                    populateLiabilityData(viewMap, tblLiabilityList);
                } catch (Exception e) {
                    System.err.println("Exception " + e.toString() + "Caught");
                    e.printStackTrace();
                }
                //Other Accounts
                viewMap = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "getSelectOtherAccountDetails");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                try {
                    populateOtherData(viewMap, tblOtherList);
                } catch (Exception e) {
                    System.err.println("Exception " + e.toString() + "Caught");
                    e.printStackTrace();
                }
                viewMap = null;
                where = null;
        }            
    }
    
    private void setupScreen() {
        //setModal(true);
        setTitle("Smart Customer" + "[" + branchID + "]");
        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        lblCustomerName.setName("lblCustomerName");
        lblEmployeeNo.setName("lblEmployeeNo");
        lblCareOfName.setName("lblCareOfName");
        lblUniqueIdNo.setName("lblUniqueIdNo");
        lblPanNO.setName("lblPanNO");
        lblPassPortNo.setName("lblPassPortNo");
        lblDtOfBirth.setName("lblDtOfBirth");
        //btnSearch.setName("btnSearch");
        //btnClear.setName("btnClear");
        btnClose.setName("btnClose");
        panCustomerDetails.setName("panCustomerDetails");
        panCustDetails.setName("panCustDetails");
    }
    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */

    private void internationalize() {
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblEmployeeNo.setText(resourceBundle.getString("lblEmployeeNo"));
        lblCareOfName.setText(resourceBundle.getString("lblCareOfName"));
        lblUniqueIdNo.setText(resourceBundle.getString("lblUniqueIdNo"));
        lblPanNO.setText(resourceBundle.getString("lblPanNO"));
        lblPassPortNo.setText(resourceBundle.getString("lblPassPortNo"));
        lblDtOfBirth.setText(resourceBundle.getString("lblDtOfBirth"));
        //btnSearch.setText(resourceBundle.getString("btnSearch"));
        //btnClear.setText(resourceBundle.getString("btnClear"));
        btnClose.setText(resourceBundle.getString("btnClose"));
    }

    public void update(java.util.Observable o, Object arg) {
    }
    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */

    public void updateOBFields() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panCustDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerDetail = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerName = new com.see.truetransact.uicomponent.CTextField();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblPhoneNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPhoneNumber = new com.see.truetransact.uicomponent.CTextField();
        lblCareOfName = new com.see.truetransact.uicomponent.CLabel();
        txtCareOfName = new com.see.truetransact.uicomponent.CTextField();
        txtUniqueId = new com.see.truetransact.uicomponent.CTextField();
        lblUniqueId = new com.see.truetransact.uicomponent.CLabel();
        panCustomerDetail1 = new com.see.truetransact.uicomponent.CPanel();
        lblUniqueIdNo = new com.see.truetransact.uicomponent.CLabel();
        txtUniqueIdNo = new com.see.truetransact.uicomponent.CTextField();
        lblPanNO = new com.see.truetransact.uicomponent.CLabel();
        txtPanNO = new com.see.truetransact.uicomponent.CTextField();
        lblPassPortNo = new com.see.truetransact.uicomponent.CLabel();
        txtPassPortNo = new com.see.truetransact.uicomponent.CTextField();
        lblDtOfBirth = new com.see.truetransact.uicomponent.CLabel();
        tdtDtOfBirth = new com.see.truetransact.uicomponent.CDateField();
        lblEmployeeNo = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchId = new com.see.truetransact.uicomponent.CTextField();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        panAccountdetails = new com.see.truetransact.uicomponent.CPanel();
        tabAccountDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panAsset = new com.see.truetransact.uicomponent.CPanel();
        srpAssetCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblAssetList = new com.see.truetransact.uicomponent.CTable();
        panLiability = new com.see.truetransact.uicomponent.CPanel();
        srpLiabilityCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLiabilityList = new com.see.truetransact.uicomponent.CTable();
        panOther = new com.see.truetransact.uicomponent.CPanel();
        srpAssetCTable1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblOtherList = new com.see.truetransact.uicomponent.CTable();
        panCommontransDetails = new com.see.truetransact.uicomponent.CPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panCustDetails.setMaximumSize(new java.awt.Dimension(830, 630));
        panCustDetails.setMinimumSize(new java.awt.Dimension(830, 630));
        panCustDetails.setPreferredSize(new java.awt.Dimension(830, 630));
        panCustDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panCustomerDetails.setMaximumSize(new java.awt.Dimension(800, 175));
        panCustomerDetails.setMinimumSize(new java.awt.Dimension(800, 175));
        panCustomerDetails.setPreferredSize(new java.awt.Dimension(800, 175));
        panCustomerDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerDetail.setMaximumSize(new java.awt.Dimension(370, 145));
        panCustomerDetail.setMinimumSize(new java.awt.Dimension(370, 150));
        panCustomerDetail.setPreferredSize(new java.awt.Dimension(350, 150));
        panCustomerDetail.setLayout(new java.awt.GridBagLayout());

        lblCustomerName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblCustomerName, gridBagConstraints);

        txtCustomerName.setMaxLength(128);
        txtCustomerName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerName.setName("txtCompany"); // NOI18N
        txtCustomerName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtCustomerName, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblMemberNo, gridBagConstraints);

        txtMemberNo.setMaxLength(128);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.setName("txtCompany"); // NOI18N
        txtMemberNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtMemberNo, gridBagConstraints);

        lblAddress.setText("House Name/Address");
        lblAddress.setToolTipText("Double click for more Details");
        lblAddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddressMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblAddress, gridBagConstraints);

        txtAddress.setMaxLength(128);
        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAddress.setName("txtCompany"); // NOI18N
        txtAddress.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtAddress, gridBagConstraints);

        lblPhoneNumber.setText("Phone Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblPhoneNumber, gridBagConstraints);

        txtPhoneNumber.setMaxLength(128);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPhoneNumber.setName("txtCompany"); // NOI18N
        txtPhoneNumber.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtPhoneNumber, gridBagConstraints);

        lblCareOfName.setText("Care Of Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblCareOfName, gridBagConstraints);

        txtCareOfName.setMaxLength(128);
        txtCareOfName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCareOfName.setName("txtCompany"); // NOI18N
        txtCareOfName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtCareOfName, gridBagConstraints);

        txtUniqueId.setAllowAll(true);
        txtUniqueId.setMaxLength(128);
        txtUniqueId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail.add(txtUniqueId, gridBagConstraints);

        lblUniqueId.setText("Unique Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail.add(lblUniqueId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panCustomerDetails.add(panCustomerDetail, gridBagConstraints);

        panCustomerDetail1.setMaximumSize(new java.awt.Dimension(250, 150));
        panCustomerDetail1.setMinimumSize(new java.awt.Dimension(200, 150));
        panCustomerDetail1.setPreferredSize(new java.awt.Dimension(250, 150));
        panCustomerDetail1.setLayout(new java.awt.GridBagLayout());

        lblUniqueIdNo.setText("Aadhaar No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblUniqueIdNo, gridBagConstraints);

        txtUniqueIdNo.setAllowAll(true);
        txtUniqueIdNo.setMaxLength(128);
        txtUniqueIdNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUniqueIdNo.setName("txtCompany"); // NOI18N
        txtUniqueIdNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtUniqueIdNo, gridBagConstraints);

        lblPanNO.setText("PAN No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblPanNO, gridBagConstraints);

        txtPanNO.setMaxLength(128);
        txtPanNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNO.setName("txtCompany"); // NOI18N
        txtPanNO.setValidation(new DefaultValidation());
        txtPanNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPanNOActionPerformed(evt);
            }
        });
        txtPanNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtPanNO, gridBagConstraints);

        lblPassPortNo.setText("Pass Port No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblPassPortNo, gridBagConstraints);

        txtPassPortNo.setMaxLength(128);
        txtPassPortNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPassPortNo.setName("txtCompany"); // NOI18N
        txtPassPortNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtPassPortNo, gridBagConstraints);

        lblDtOfBirth.setText("Date Of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblDtOfBirth, gridBagConstraints);

        tdtDtOfBirth.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDtOfBirth.setName("tdtToDate"); // NOI18N
        tdtDtOfBirth.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtDtOfBirth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDtOfBirthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(tdtDtOfBirth, gridBagConstraints);

        lblEmployeeNo.setText("Employee No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblEmployeeNo, gridBagConstraints);

        txtEmployeeNo.setMaxLength(128);
        txtEmployeeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmployeeNo.setName("txtCompany"); // NOI18N
        txtEmployeeNo.setValidation(new DefaultValidation());
        txtEmployeeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtEmployeeNo, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 9);
        panCustomerDetail1.add(lblCustomerID, gridBagConstraints);

        txtCustomerID.setMaxLength(128);
        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.setName("txtCompany"); // NOI18N
        txtCustomerID.setValidation(new DefaultValidation());
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 9);
        panCustomerDetail1.add(txtCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panCustomerDetails.add(panCustomerDetail1, gridBagConstraints);

        txtBranchId.setMaxLength(128);
        txtBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchId.setName("txtCompany"); // NOI18N
        txtBranchId.setValidation(new DefaultValidation());
        txtBranchId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panCustomerDetails.add(txtBranchId, gridBagConstraints);

        lblBranchId.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panCustomerDetails.add(lblBranchId, gridBagConstraints);

        lblBranchName.setForeground(new java.awt.Color(0, 0, 255));
        lblBranchName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBranchName.setText("Branch Name");
        lblBranchName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBranchName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblBranchName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblBranchName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblBranchName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panCustDetails.add(panCustomerDetails, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnOk, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Cancel");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDetails.add(panSearch, gridBagConstraints);

        panAccountdetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountdetails.setMaximumSize(new java.awt.Dimension(800, 350));
        panAccountdetails.setMinimumSize(new java.awt.Dimension(800, 250));
        panAccountdetails.setPreferredSize(new java.awt.Dimension(800, 350));
        panAccountdetails.setLayout(new java.awt.GridBagLayout());

        tabAccountDetails.setMaximumSize(new java.awt.Dimension(480, 380));
        tabAccountDetails.setMinimumSize(new java.awt.Dimension(480, 380));
        tabAccountDetails.setPreferredSize(new java.awt.Dimension(480, 380));

        panAsset.setMaximumSize(new java.awt.Dimension(100, 128));
        panAsset.setMinimumSize(new java.awt.Dimension(100, 128));
        panAsset.setPreferredSize(new java.awt.Dimension(100, 128));
        panAsset.setLayout(new java.awt.GridBagLayout());

        srpAssetCTable.setMaximumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable.setMinimumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable.setPreferredSize(new java.awt.Dimension(430, 270));

        tblAssetList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblAssetList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ACT_NUM", "PROD_TYPE", "PROD_ID", "BALANCE"
            }
        ));
        tblAssetList.setToolTipText("Double Click on table row for Trans Details");
        tblAssetList.setDragEnabled(true);
        tblAssetList.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblAssetList.setMinimumSize(new java.awt.Dimension(430, 900));
        tblAssetList.setOpaque(false);
        tblAssetList.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblAssetList.setPreferredSize(new java.awt.Dimension(430, 900));
        tblAssetList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAssetListMouseClicked(evt);
            }
        });
        srpAssetCTable.setViewportView(tblAssetList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAsset.add(srpAssetCTable, gridBagConstraints);

        tabAccountDetails.addTab("Assets", panAsset);

        panLiability.setMaximumSize(new java.awt.Dimension(100, 128));
        panLiability.setMinimumSize(new java.awt.Dimension(100, 128));
        panLiability.setPreferredSize(new java.awt.Dimension(100, 128));
        panLiability.setLayout(new java.awt.GridBagLayout());

        srpLiabilityCTable.setMaximumSize(new java.awt.Dimension(350, 170));
        srpLiabilityCTable.setMinimumSize(new java.awt.Dimension(350, 170));
        srpLiabilityCTable.setPreferredSize(new java.awt.Dimension(430, 270));

        tblLiabilityList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblLiabilityList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ACT_NUM", "PROD_TYPE", "PROD_ID", "BALANCE"
            }
        ));
        tblLiabilityList.setToolTipText("Double Click on table row for Trans Details");
        tblLiabilityList.setDragEnabled(true);
        tblLiabilityList.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblLiabilityList.setMinimumSize(new java.awt.Dimension(350, 900));
        tblLiabilityList.setOpaque(false);
        tblLiabilityList.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblLiabilityList.setPreferredSize(new java.awt.Dimension(430, 900));
        tblLiabilityList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLiabilityListMouseClicked(evt);
            }
        });
        srpLiabilityCTable.setViewportView(tblLiabilityList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLiability.add(srpLiabilityCTable, gridBagConstraints);

        tabAccountDetails.addTab("Liability", panLiability);

        panOther.setMaximumSize(new java.awt.Dimension(100, 128));
        panOther.setMinimumSize(new java.awt.Dimension(100, 128));
        panOther.setPreferredSize(new java.awt.Dimension(100, 128));
        panOther.setLayout(new java.awt.GridBagLayout());

        srpAssetCTable1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        srpAssetCTable1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        srpAssetCTable1.setMaximumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable1.setMinimumSize(new java.awt.Dimension(350, 170));
        srpAssetCTable1.setPreferredSize(new java.awt.Dimension(430, 270));

        tblOtherList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblOtherList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ACT_NUM", "PROD_TYPE", "PROD_ID", "BALANCE"
            }
        ));
        tblOtherList.setToolTipText("No Details Availble");
        tblOtherList.setDragEnabled(true);
        tblOtherList.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblOtherList.setMinimumSize(new java.awt.Dimension(430, 900));
        tblOtherList.setOpaque(false);
        tblOtherList.setPreferredScrollableViewportSize(new java.awt.Dimension(794, 246));
        tblOtherList.setPreferredSize(new java.awt.Dimension(430, 900));
        tblOtherList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOtherListMouseClicked(evt);
            }
        });
        srpAssetCTable1.setViewportView(tblOtherList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOther.add(srpAssetCTable1, gridBagConstraints);

        tabAccountDetails.addTab("Other", panOther);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(tabAccountDetails, gridBagConstraints);

        panCommontransDetails.setMaximumSize(new java.awt.Dimension(300, 250));
        panCommontransDetails.setMinimumSize(new java.awt.Dimension(300, 350));
        panCommontransDetails.setPreferredSize(new java.awt.Dimension(300, 250));
        panCommontransDetails.setRequestFocusEnabled(false);
        panCommontransDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountdetails.add(panCommontransDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCustDetails.add(panAccountdetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panCustDetails, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        if (goldLoanFlag == true) {
            HashMap hash = new HashMap();
            //memNo = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 0));
            if(memNo.equals(null) || memNo.equals(""))
            {
                ClientUtil.displayAlert("This customer does not have a share!!!");
            }
            //custId = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 1));
            //String status = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 10));
            hash.put("CUSTOMER ID", custId);
            hash.put("MEMBER NO", memNo);
            //hash.put("STATUS", status);
            parentGoldLoanUI.insertCustTableRecords(hash);
            this.dispose();
        } else if (changeMemberFlag == true) {
            HashMap hash = new HashMap();
            //memNo = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 0));
            //memName = CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 2));
            hash.put("FNAME", memName);
            hash.put("MEMBER NO", memNo);
            parentChangeMemberUI.insertCustTableRecords(hash);
            this.dispose();
        } else {
            HashMap hash = new HashMap();
            //CommonUtil.convertObjToStr(tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), 0));
//            for (int i=0; i<tblCustomerList.getRowCount(); i++) {
            //for (int j = 0; j < tblCustomerList.getColumnCount(); j++) {
            //    hash.put(tblCustomerList.getColumnName(j), tblCustomerList.getValueAt(tblCustomerList.getSelectedRow(), j));
            //}
//            }
            parent.fillData(hash);
            this.dispose();
        }
         this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        txtCustomerName.setText("");
        txtCustomerID.setText("");
        txtCareOfName.setText("");
        txtUniqueIdNo.setText("");
        txtPanNO.setText("");
        txtPassPortNo.setText("");
        tdtDtOfBirth.setDateValue("");
        txtPhoneNumber.setText("");
        lblBranchName.setText("Branch Name");
        transDetails.setTransDetails(null, null, null);
        ClientUtil.clearAll(this);
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void clearAllTableData() {
        TableModel custModel = new TableModel(new ArrayList(), custTitle);
        //tblCustomerList.setModel(custModel);
        TableModel model = new TableModel(new ArrayList(), TabTitle);
        tblAssetList.setModel(model);
        tblLiabilityList.setModel(model);
        tblLiabilityList.setModel(model);
        transDetails.setTransDetails(null, null, null);
    }
    public ArrayList populateData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) { 
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : " + data);
        populateTable();
        whereMap = null;
        return _heading;
    }

    public ArrayList populateAssetData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : " + data);
        populateAssetTable();
        whereMap = null;
        return _heading;
    }

    public ArrayList populateLiabilityData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : " + data);
        populateLiabilityTable();
        for (int i = 0; i < data.size(); i++) {
////            System.out.println("data.get(0); : " + data.get(0));
////            System.out.println("data.get(0); : " + tblLiabilityList.getValueAt(i, 0));
//            HashMap hmap = new HashMap();
////            Date toDate = new Date();
//            hmap.put("ACCOUNTNO", tblLiabilityList.getValueAt(i, 0));
//            List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
//            if (kccList1 != null && kccList1.size() > 0) {
//                hmap = (HashMap) kccList1.get(0);
//                Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
//                //System.out.println("in focus lost toDate###"+toDate);
//                if (!toDate.equals("") && toDate.before((Date) currDt.clone())) {
//                    tblLiabilityList.setForeground(Color.red);
//                }
//            }
            setColour();
        }
        whereMap = null;
        return _heading;
    }

     private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String str = CommonUtil.convertObjToStr(table.getValueAt(row, 4));
               // System.out.println("setColour str : "+str);
                //if (colorList.contains(String.valueOf(row))) {
                if (CommonUtil.convertObjToStr(str).equals("DUE")) {
                   // System.out.println("setColour str inside closed : "+str);
                    setForeground(Color.red);
                } else {
                    //System.out.println("setColour str else : "+str);
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblLiabilityList.setDefaultRenderer(Object.class, renderer);
    }
    
    public ArrayList populateOtherData(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : " + data);
        populateOtherTable();
        whereMap = null;
        return _heading;
    }
        
    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();

            _tblData.getColumnModel().getColumn(0).setPreferredWidth(70);
            _tblData.getColumnModel().getColumn(1).setPreferredWidth(130);
            _tblData.getColumnModel().getColumn(2).setPreferredWidth(350);
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(95);
            _tblData.getColumnModel().getColumn(4).setPreferredWidth(120);
            _tblData.getColumnModel().getColumn(5).setPreferredWidth(115);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(90);
            _tblData.getColumnModel().getColumn(7).setPreferredWidth(80);
            _tblData.getColumnModel().getColumn(8).setPreferredWidth(95);
//            _tblData.getColumnModel().getColumn(11).setPreferredWidth(85);
        } else {
            ClientUtil.noDataAlert();
            ClientUtil.clearAll(panCustomerDetails);
            txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
            lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        }
    }

    public void populateAssetTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            _tblData.getColumnModel().getColumn(0).setPreferredWidth(95);
        } else {
            TableModel model = new TableModel(new ArrayList(), TabTitle);
            tblAssetList.setModel(model);
            transDetails.setTransDetails(null, null, null);
        }
    }

    public void populateLiabilityTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            _tblData.getColumnModel().getColumn(0).setPreferredWidth(95);
        } else {
            TableModel model = new TableModel(new ArrayList(), TabTitle);
            tblLiabilityList.setModel(model);
            transDetails.setTransDetails(null, null, null);
        }
    }

    public void populateOtherTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(0);
            _tblData.doLayout();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            tblOtherList.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblOtherList.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblOtherList.getColumnModel().getColumn(4).setPreferredWidth(100);
        } else {
            TableModel model = new TableModel(new ArrayList(), TabTitle);
            tblOtherList.setModel(model);
            transDetails.setTransDetails(null, null, null);
        }
    }
        
    private void tdtDtOfBirthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtOfBirthFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDtOfBirth);
    }//GEN-LAST:event_tdtDtOfBirthFocusLost

    private void txtPanNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNOFocusLost
        // TODO add your handling code here:
        if (txtPanNO.getText().length() > 0 && !ClientUtil.validatePAN(txtPanNO)) {
            ClientUtil.showMessageWindow("Invalid Pan Number, Enter Proper Pan No (Format :ABCDE1234F)");
            txtPanNO.setText("");
        }
    }//GEN-LAST:event_txtPanNOFocusLost
                                private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                                                            }//GEN-LAST:event_formWindowClosed

                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                            }//GEN-LAST:event_exitForm

    private void tblAssetListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAssetListMouseClicked
        // TODO add your handling code here:        
        if (tblAssetList.getRowCount() > 0) {
            transDetails.setTransDetails(null, null, null, null);
            if (evt.getClickCount() == 2) {
                String prodType = CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(), 1));
                String actNum = CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(), 0));
                String prodDesc = CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(), 2));
                //String prodDesc = CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(), 2));
                if (prodType.equals("TD")) {
                    if (actNum.lastIndexOf("_") != -1) {
                        actNum = actNum;
                    } else {
                        actNum = actNum + "_1";
                    }
                } else {
                    actNum = actNum;
                }
                transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, actNum);
                //Trans PopUp
                 if (!prodType.equals("TD")) {
                     TransPopUP dialog = new TransPopUP(actNum,prodType); 
                     dialog.show();
                 }else{
                     System.out.println("actNum.substring(4, 3)$#@$@$"+actNum.substring(4, 7));
                    TransPopUP dialog = new TransPopUP(actNum,prodType,prodDesc,actNum.substring(4, 7),TrueTransactMain.BRANCH_ID);
                    dialog.show();
                 }
            }
        }
    }//GEN-LAST:event_tblAssetListMouseClicked

    private void tblLiabilityListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLiabilityListMouseClicked
        // TODO add your handling code here:
        if (tblLiabilityList.getRowCount() > 0) {
            transDetails.setTransDetails(null, null, null, null);
            if (evt.getClickCount() == 2) {
                HashMap asAndWhenMap = new HashMap();
                long noofInstallment = 0;
                transDetails.setCorpDetailMap(new HashMap());
                String ACCOUNTNO = CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 0));
                HashMap mapHash = asAnWhenCustomerComesYesNO(ACCOUNTNO);
                if (ACCOUNTNO != null && ACCOUNTNO.length() > 0) {
                    if (CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)).equals("TL") || CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)).equals("AD")
                            && (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES") != null && mapHash.get("AS_CUSTOMER_COMES").equals("Y"))) {
                        asAndWhenMap = interestCalculationTLAD(ACCOUNTNO, noofInstallment);
                        if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                            asAndWhenMap.put("INSTALL_TYPE", getLinkMap().get("INSTALL_TYPE"));
                            transDetails.setAsAndWhenMap(asAndWhenMap);
                            if (asAndWhenMap.containsKey("NO_OF_INSTALLMENT") && CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT")) > 0) {
                                noofInstallment = CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"));
                            }
                        }
                    }
                }
                transDetails.setTransDetails(CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)),
                        ProxyParameters.BRANCH_ID, CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 0)));
                TransPopUP dialog = new TransPopUP(ACCOUNTNO,CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)),CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 2)),null,null); 
                dialog.show();
            }
        }
    }//GEN-LAST:event_tblLiabilityListMouseClicked

    private HashMap interestCalculationTLAD(String accountNo, long noOfInstallment) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt.clone());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)).equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);  // Changed by Rajesh to get interest for Other branch a/cs
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                if (noOfInstallment > 0) {
                    map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                }

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt);
                // System.out.println("map before intereest###" + map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                // System.out.println("hashinterestoutput###" + hash);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {//dont delete this methode check select dao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("#### MapData :"+mapData);
        return mapData;
    }

    public HashMap asAnWhenCustomerComesYesNO(String acct_no) {
        HashMap map = new HashMap();
        map.put("ACT_NUM", acct_no);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = null;
        if (!CommonUtil.convertObjToStr(tblLiabilityList.getValueAt(tblLiabilityList.getSelectedRow(), 1)).equals("AD")) {
            lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        } else {
            lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }

        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            setLinkMap(map);
        }
        return map;
    }

    private void txtBranchIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchIdFocusLost
        // TODO add your handling code here:
        if (txtBranchId.getText().length() > 0) {
            HashMap dataMap = new HashMap();
            dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(txtBranchId.getText()));
            List lst = ClientUtil.executeQuery("getDisplayRegionalOffice", dataMap);

            if (lst != null && lst.size() > 0) {
                dataMap = (HashMap) lst.get(0);
                lblBranchName.setText(CommonUtil.convertObjToStr(dataMap.get("BRANCH NAME")));
            } else {
                ClientUtil.displayAlert("Invalid Branch Code");
                txtBranchId.setText("");
                lblBranchName.setText("Branch Name");
                return;
            }
        } else {
            lblBranchName.setText("Branch Name");
            return;
        }
    }//GEN-LAST:event_txtBranchIdFocusLost

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if (txtCustomerID.getText().length() > 0) {
            txtBranchId.setText("");
            lblBranchName.setText("Branch Name");
        } else {
            branchMap = TrueTransactMain.BRANCHINFO;
            txtBranchId.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
            lblBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        }
    }//GEN-LAST:event_txtCustomerIDFocusLost

private void txtEmployeeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeNoActionPerformed
// TODO add your handling code here:
   
}//GEN-LAST:event_txtEmployeeNoActionPerformed

private void txtPanNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPanNOActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtPanNOActionPerformed

private void tblOtherListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOtherListMouseClicked
// TODO add your handling code here:
     if(evt.getClickCount()== 2){    
        String prodType = CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(), 1));
        if (!prodType.equals("LOCKER")) {
        TransPopUP dialog = new TransPopUP(CommonUtil.convertObjToStr(tblOtherList.getValueAt(tblOtherList.getSelectedRow(), 0)),CommonUtil.convertObjToStr(tblOtherList.getValueAt(tblOtherList.getSelectedRow(), 2)),CommonUtil.convertObjToStr(tblOtherList.getValueAt(tblOtherList.getSelectedRow(), 3)),
                CommonUtil.convertObjToStr(tblOtherList.getValueAt(tblOtherList.getSelectedRow(), 1)),CommonUtil.convertObjToStr(tblOtherList.getValueAt(tblOtherList.getSelectedRow(), 5))); 
         dialog.show();
        }
     }
}//GEN-LAST:event_tblOtherListMouseClicked

private void lblAddressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddressMouseClicked
// TODO add your handling code here:
     if(evt.getClickCount()== 2){
        new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(custId)).show(); 
     }
}//GEN-LAST:event_lblAddressMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCareOfName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDtOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblPanNO;
    private com.see.truetransact.uicomponent.CLabel lblPassPortNo;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNumber;
    private com.see.truetransact.uicomponent.CLabel lblUniqueId;
    private com.see.truetransact.uicomponent.CLabel lblUniqueIdNo;
    private com.see.truetransact.uicomponent.CPanel panAccountdetails;
    private com.see.truetransact.uicomponent.CPanel panAsset;
    private com.see.truetransact.uicomponent.CPanel panCommontransDetails;
    private com.see.truetransact.uicomponent.CPanel panCustDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail1;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetails;
    private com.see.truetransact.uicomponent.CPanel panLiability;
    private com.see.truetransact.uicomponent.CPanel panOther;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CScrollPane srpAssetCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpAssetCTable1;
    private com.see.truetransact.uicomponent.CScrollPane srpLiabilityCTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabAccountDetails;
    private com.see.truetransact.uicomponent.CTable tblAssetList;
    private com.see.truetransact.uicomponent.CTable tblLiabilityList;
    private com.see.truetransact.uicomponent.CTable tblOtherList;
    private com.see.truetransact.uicomponent.CDateField tdtDtOfBirth;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtBranchId;
    private com.see.truetransact.uicomponent.CTextField txtCareOfName;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtCustomerName;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtPanNO;
    private com.see.truetransact.uicomponent.CTextField txtPassPortNo;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNumber;
    private com.see.truetransact.uicomponent.CTextField txtUniqueId;
    private com.see.truetransact.uicomponent.CTextField txtUniqueIdNo;
    // End of variables declaration//GEN-END:variables

    public class TransPopUP extends CDialog implements Observer {
        private com.see.truetransact.uicomponent.CButtonGroup rdoBtnGroup;
        private com.see.truetransact.uicomponent.CRadioButton rdoCashButton;
        private com.see.truetransact.uicomponent.CRadioButton rdoTransferButton;
        private com.see.truetransact.uicomponent.CRadioButton rdoAcClose;
        private com.see.truetransact.uicomponent.CRadioButton rdoMdsRecipt;
        private com.see.truetransact.uicomponent.CRadioButton rdoMdsPayment;
        private com.see.truetransact.uicomponent.CRadioButton rdoDepositRenewal;
        private com.see.truetransact.uicomponent.CRadioButton rdoDepositClosing;
        private com.see.truetransact.uicomponent.CRadioButton rdoDebit;
        private com.see.truetransact.uicomponent.CRadioButton rdoCredit;
        private com.see.truetransact.uicomponent.CButtonGroup rdoBtnGroupTransfer;
        private com.see.truetransact.uicomponent.CPanel panel;
        private com.see.truetransact.uicomponent.CPanel TransPanel;
        private com.see.truetransact.uicomponent.CButton btnConfrm;
        private String actNum = "",prodType = "",prodDesc = "",subNo="",branchCode="";
        
        public TransPopUP(){
        
        }
        
        public TransPopUP(String actNum,String prodType){
            this.setTitle("Select Transaction");
            this.actNum = actNum;
            this.prodType = prodType;
            setupScreen();
            showAvailableTransactions(actNum,prodType,null);
        }
        
        public TransPopUP(String actNum,String prodtype,String prodDesc,String subNo,String branchCode){
            this.setTitle("Select Transaction");
            this.actNum = actNum;
            this.prodType = prodtype;
            this.prodDesc = prodDesc;
            this.subNo = subNo;
            this.branchCode = branchCode;
            setupScreen();
            showAvailableTransactions(actNum,prodtype,prodDesc);
        }
        
        private void setupScreen() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(400, 300);
            /* Center frame on the screen */
            Dimension frameSize = this.getSize();
            if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
            this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            this.toFront();
        }
        
        public void showAvailableTransactions(String actNum,String prodtype,String prodDesc){
            if(actNum!=null && prodtype!= null && actNum.length()>0 && prodtype.length()>0  ){
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                if(prodtype.equalsIgnoreCase("OA") ||prodtype.equalsIgnoreCase("TL") || prodtype.equalsIgnoreCase("AD")){
                    System.out.println("prodType#%#%#"+prodtype);                    
                    panel = new com.see.truetransact.uicomponent.CPanel();
                    rdoBtnGroup = new com.see.truetransact.uicomponent.CButtonGroup() ;
                    rdoCashButton = new com.see.truetransact.uicomponent.CRadioButton();
                    rdoAcClose = new com.see.truetransact.uicomponent.CRadioButton();
                    rdoTransferButton = new com.see.truetransact.uicomponent.CRadioButton();
                    btnConfrm = new com.see.truetransact.uicomponent.CButton();
                    panel.setLayout(new java.awt.GridBagLayout());
                    panel.setMinimumSize(new java.awt.Dimension(400, 400));
                    panel.setPreferredSize(new java.awt.Dimension(600, 600));
                    rdoCashButton.setText("Cash Transaction");
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);      
                    rdoBtnGroup.add(rdoCashButton);
                    panel.add(rdoCashButton, gridBagConstraints);
                    rdoAcClose.setText("Account Closing");
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 1;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);  
                    rdoBtnGroup.add(rdoAcClose);
                    panel.add(rdoAcClose, gridBagConstraints);
                    rdoTransferButton.setText("Transfer Transaction");
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 2;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);  
                    rdoBtnGroup.add(rdoTransferButton);                    
                    panel.add(rdoTransferButton, gridBagConstraints);
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    rdoBtnGroupTransfer = new com.see.truetransact.uicomponent.CButtonGroup() ;
                    rdoCredit = new com.see.truetransact.uicomponent.CRadioButton();
                    rdoDebit = new com.see.truetransact.uicomponent.CRadioButton();            
                    rdoDebit.setText("Debit");
                    gridBagConstraints.gridx = 1;
                    gridBagConstraints.gridy = 1;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);      
                    rdoBtnGroupTransfer.add(rdoDebit);
                    panel.add(rdoDebit, gridBagConstraints);  
                    rdoCredit.setText("Credit");
                    gridBagConstraints.gridx = 2;
                    gridBagConstraints.gridy = 1;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);      
                    rdoBtnGroupTransfer.add(rdoCredit);
                    panel.add(rdoCredit, gridBagConstraints);
                    rdoTransferButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       rdoTransferButtonActionPerformed(evt);
                    }
                    });
                    rdoCashButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       rdoCashButtonActionPerformed(evt);
                    }
                    });
                    rdoAcClose.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       rdoAcCloseActionPerformed(evt);
                    }
                    });
                }else if(prodtype.equalsIgnoreCase("MDS")){
                    panel = new com.see.truetransact.uicomponent.CPanel();
                    rdoBtnGroup = new com.see.truetransact.uicomponent.CButtonGroup() ;
                    rdoMdsRecipt = new com.see.truetransact.uicomponent.CRadioButton();
                    rdoMdsPayment = new com.see.truetransact.uicomponent.CRadioButton();   
                    panel.setLayout(new java.awt.GridBagLayout());
                    panel.setMinimumSize(new java.awt.Dimension(100, 100));
                    panel.setPreferredSize(new java.awt.Dimension(300, 300));
                    rdoMdsRecipt.setText("Mds Receipt");
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);      
                    rdoBtnGroup.add(rdoMdsRecipt);
                    panel.add(rdoMdsRecipt, gridBagConstraints);
                    rdoMdsPayment.setText("Mds Payment");
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 1;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);  
                    rdoBtnGroup.add(rdoMdsPayment);
                    panel.add(rdoMdsPayment, gridBagConstraints);                      
                }else if(prodtype.equalsIgnoreCase("TD")){
                    panel = new com.see.truetransact.uicomponent.CPanel();
                    rdoBtnGroup = new com.see.truetransact.uicomponent.CButtonGroup() ;
                    //rdoDepositRenewal = new com.see.truetransact.uicomponent.CRadioButton();
                    rdoDepositClosing = new com.see.truetransact.uicomponent.CRadioButton();                   
                    panel.setLayout(new java.awt.GridBagLayout());
                    panel.setMinimumSize(new java.awt.Dimension(100, 100));
                    panel.setPreferredSize(new java.awt.Dimension(300, 300));
                    //rdoDepositRenewal.setText("Deposit Renewal");
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);      
                    //rdoBtnGroup.add(rdoDepositRenewal);
                    //panel.add(rdoDepositRenewal, gridBagConstraints);
                    rdoDepositClosing.setText("Deposit Closing");
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 1;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);  
                    rdoBtnGroup.add(rdoDepositClosing);
                    panel.add(rdoDepositClosing, gridBagConstraints);  
                }
                btnConfrm = new com.see.truetransact.uicomponent.CButton();
                btnConfrm.setText("Proceed");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
                btnConfrm.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if(prodType.equalsIgnoreCase("OA") ||prodType.equalsIgnoreCase("TL") || prodType.equalsIgnoreCase("AD")){
                        if(rdoTransferButton.isSelected() & (!rdoDebit.isSelected() & !rdoCredit.isSelected())){
                            ClientUtil.showMessageWindow("Please Select Debit or Credit!!");
                            return;
                        }else{
                            btnAccNoActionPerformed(evt);
                        }
                    }else{
                        btnAccNoActionPerformed(evt);
                    }
                }
                });
                panel.add(btnConfrm, gridBagConstraints);
                this.add(panel);
            }
        }
        
      private void rdoAcCloseActionPerformed(java.awt.event.ActionEvent evt) {
          rdoCredit.setEnabled(false);
          rdoDebit.setEnabled(false);
          rdoCredit.setSelected(false);
          rdoDebit.setSelected(false);
      }
        
      private void rdoCashButtonActionPerformed(java.awt.event.ActionEvent evt) {
          rdoCredit.setEnabled(false);
          rdoDebit.setEnabled(false);
          rdoCredit.setSelected(false);
          rdoDebit.setSelected(false);
      }
      
      private void rdoTransferButtonActionPerformed(java.awt.event.ActionEvent evt) {
          System.out.println("herereeee");
            rdoDebit.setEnabled(true);  
            rdoCredit.setEnabled(true); 
            rdoDebit.setSelected(false);
            rdoCredit.setSelected(false);
      }
        
      private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {
            CInternalFrame frm;
            HashMap frmMap;      
            if(prodType.equalsIgnoreCase("OA") ||prodType.equalsIgnoreCase("TL") || prodType.equalsIgnoreCase("AD")){
                if(rdoCashButton.isSelected()){
                    System.out.println("cah@$$!$$!");
                    frmMap = new HashMap();
                    frm = new com.see.truetransact.ui.transaction.cash.CashTransactionUI();
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setTitle("Cash Transactions");                    
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("ACT_NUM", actNum);
                    frmMap.put("ACCOUNTNO", actNum);
                    frmMap.put("PARENT", new SmartCustomerUI());
                    frm.fillData(frmMap);
                    this.dispose();
                }else if(rdoAcClose.isSelected()){
                    System.out.println("cah@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    if(prodType.equalsIgnoreCase("TL")|| prodType.equalsIgnoreCase("AD")){
                        frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setTitle("Loan Closing");  
                    }else{
                        frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI();
                        frm.setTitle("Account Closing");  
                    }
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("ACT_NUM", actNum);
                    frmMap.put("ACCOUNT NUMBER", actNum);
                    if(prodDesc!=null && prodDesc.length()>0){
                        frmMap.put("PROD_DESC", prodDesc);
                    }
                frmMap.put("PARENT", new SmartCustomerUI());
                frm.fillData(frmMap);
                this.dispose();
                }else if(rdoTransferButton.isSelected()){
                    System.out.println("Transfer@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    frm = new com.see.truetransact.ui.transaction.transfer.TransferUI();
                    if(rdoCredit.isSelected()){
                        frmMap.put("TRANS_TYPE", "CREDIT");
                    }else if(rdoDebit.isSelected()){
                        frmMap.put("TRANS_TYPE", "DEBIT");
                    }
                    frm.setTitle("Transfer");  
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("ACCOUNTNO", actNum);
                    frmMap.put("ACCOUNT NUMBER", actNum);
                    if(prodDesc!=null && prodDesc.length()>0){
                        frmMap.put("PROD_DESC", prodDesc);
                    }
                frmMap.put("PARENT", new SmartCustomerUI());
                frm.fillData(frmMap);
                this.dispose();
                }
             }else if(prodType.equalsIgnoreCase("MDS")){
                if(rdoMdsRecipt.isSelected()){
                    System.out.println("cah@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    if(CheckMdsSchemeStatus(prodDesc)){
                        frm = new com.see.truetransact.ui.mdsapplication.mdsclosedreceipt.MDSClosedReciptUI();  
                        frm.setTitle("MDS Receipt Entry After Closure");  
                    }else{
                        frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();  
                        frm.setTitle("MDS Receipt Entry");  
                    }
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("CHITTAL_NO", actNum);
                    frmMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    frmMap.put("SCHEME_NAME", prodDesc);
                    frmMap.put("BRANCH_CODE", branchCode);
                    frmMap.put("PARENT", new SmartCustomerUI());
                    frm.fillData(frmMap);
                    this.dispose();
                }else if(rdoMdsPayment.isSelected()){
                    System.out.println("cah@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();  
                    frm.setTitle("MDS Prized Money Payment");  
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("CHITTAL_NO", actNum);
                    frmMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    frmMap.put("SCHEME_NAME", prodDesc);
                    frmMap.put("BRANCH_CODE", branchCode);
                    frmMap.put("PARENT", new SmartCustomerUI());
                    frm.fillData(frmMap);
                    this.dispose();
                }
            }else if(prodType.equalsIgnoreCase("TD")){
                /*if(rdoDepositRenewal.isSelected()){
                    System.out.println("cah@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    frm = new com.see.truetransact.ui.deposit.TermDepositUI();             
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    frmMap.put("CHITTAL_NO", actNum);
                    frmMap.put("SUB_NO", subNo);
                    frmMap.put("SCHEME_NAME", prodDesc);
                    frmMap.put("BRANCH_CODE", branchCode);
                    frmMap.put("PARENT", new SmartCustomerUI());
                    frm.fillData(frmMap);
                    this.dispose();
                }else*/ 
                    if(rdoDepositClosing.isSelected()){
                    System.out.println("cah@$$!$$!"+prodDesc);
                    frmMap = new HashMap();
                    frm = new com.see.truetransact.ui.deposit.closing.DepositClosingUI();    
                    frm.setTitle("Deposit Closing/Transfer to Matured Deposit");  
                    frmMap.put("FROM_SMART_CUSTOMER_UI", "");
                    System.out.println("getSelectedBranchID()%#%#%#%$%" + getSelectedBranchID());
                    frm.setSelectedBranchID(getSelectedBranchID());
                    TrueTransactMain.showScreen(frm);
                    System.out.println("Haash Before" + frmMap);
                    if (actNum.lastIndexOf("_") != -1) {
                        actNum = actNum.substring(0, 13);
                    }    
                    System.out.println("cah@$$!$$!"+actNum);
                    frmMap.put("DEPOSIT_ACT_NUM", actNum);
                    frmMap.put("PROD_ID", subNo);
                    frmMap.put("SCHEME_NAME", prodDesc);
                    frmMap.put("BRANCH_CODE", branchCode);
                    frmMap.put("PARENT", new SmartCustomerUI());
                    frm.fillData(frmMap);
                    this.dispose();
                }
            }
        }
        
        public boolean CheckMdsSchemeStatus(String schemeName){
            boolean status = false;
            HashMap dataMap = new HashMap();
            dataMap.put("SCHEME_NAMES", schemeName);
            dataMap.put("BRANCH_CODE", branchCode);
            dataMap.put("CURR_DATE", currDt);            
            List lst = ClientUtil.executeQuery("getSelectClosedEachSchemeDetails", dataMap);
            if (lst != null && lst.size() > 0) {
                status = true;
            }else{
                status = false;
            }
            return status;
        }
        
        public void update(Observable o, Object arg) {
        }
        
    }
}
