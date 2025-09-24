/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SendSMSUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */

package com.see.truetransact.ui.sms;

/**
 *
 * @author  
 */
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
//import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
//import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;

public class SendSMSUI extends CInternalFrame implements Observer{
    
    /** Vairable Declarations */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    SendSMSOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    
    /** Creates new form TokenConfigUI */
    public SendSMSUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        observable = new SendSMSOB();
        initTableData();
        txtProductID.setAllowAll(true);
        txtSINumber.setAllowAll(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication,false);
        btnProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        txtProductID.setEnabled(true);
    }
    
    private void initTableData(){
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
    }
    
   
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        
    }
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        
    }
    
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
    }
    
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        if (currField.equalsIgnoreCase("PROD_DETAILS")) {
            viewMap.put(CommonConstants.MAP_NAME, "getFixedDepositProducts");
        } else if(currField.equalsIgnoreCase("FROM") || currField.equalsIgnoreCase("TO")){
            hash.put("PROD_ID", txtProductID.getText());
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
        }else if(currField.equalsIgnoreCase("SI_NUMBER")){
            viewMap.put(CommonConstants.MAP_NAME, "getDepositIntPayAccountNo");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :"+hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_DETAILS")) {
                    txtProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    lblProductName.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                    btnProductID.setEnabled(true);
                }
            }
            
            if (viewType.equals("Customer")) {
                //__ To reset the data for the Previous selected Customer..
                final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                txtCustID.setText(CUSTID);
                //__ To set the Name of the Customer...
                final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
                lblCustName.setText(CUSTNAME);
                txtFromAccount.setText("");
                txtToAccount.setText("");

            }
            if (viewType.equals("CUSTOMER ID")) {
                CustInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            }
            if (viewType.equals("FROM")) {
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
            }
            
            if (viewType.equals("TO")) {
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
            }
            if (viewType.equals("SI_NUMBER")) {
                txtSINumber.setText(CommonUtil.convertObjToStr(hash.get("SI_NUMBER")));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void CustInfoDisplay(String custId){

        HashMap custMap = new HashMap();
        custMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        custMap.put("CUST_ID",custId);
        List lst = ClientUtil.executeQuery("getDepositCustomerName", custMap);
        if(lst != null && lst.size()>0){
            custMap = (HashMap)lst.get(0);
            lblCustName.setText(CommonUtil.convertObjToStr(custMap.get("FNAME")));
            lst.clear();
            custMap.clear();
        } else {
            ClientUtil.showAlertWindow("Invalid Customer or This Customer not having Deposit A/cs...");
        }
        lst = null;
        custMap = null;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        btnProductID = new com.see.truetransact.uicomponent.CButton();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        panTransType = new com.see.truetransact.uicomponent.CPanel();
        chkCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        panFromAccount = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        panToAccount = new com.see.truetransact.uicomponent.CPanel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        panCustID = new com.see.truetransact.uicomponent.CPanel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        lblSINumber = new com.see.truetransact.uicomponent.CLabel();
        panSINumber = new com.see.truetransact.uicomponent.CPanel();
        txtSINumber = new com.see.truetransact.uicomponent.CTextField();
        btnSINumber = new com.see.truetransact.uicomponent.CButton();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositInterestApplication = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 620));
        setMinimumSize(new java.awt.Dimension(860, 620));
        setPreferredSize(new java.awt.Dimension(860, 620));
        panDepositInterestApplication.setLayout(new java.awt.GridBagLayout());

        panDepositInterestApplication.setBorder(new javax.swing.border.EtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(800, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(800, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(800, 450));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(new javax.swing.border.TitledBorder("Deposit Product Details"));
        panProductDetails.setMinimumSize(new java.awt.Dimension(830, 110));
        panProductDetails.setPreferredSize(new java.awt.Dimension(830, 110));
        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblProductID, gridBagConstraints);

        panProductID.setLayout(new java.awt.GridBagLayout());

        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panProductID.add(txtProductID, gridBagConstraints);

        btnProductID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnProductID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProductID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProductID.setEnabled(false);
        btnProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(btnProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panProductDetails.add(panProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblProductName, gridBagConstraints);

        btnCalculate.setText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductDetails.add(btnCalculate, gridBagConstraints);

        panTransType.setLayout(new java.awt.GridBagLayout());

        panTransType.setMinimumSize(new java.awt.Dimension(130, 25));
        panTransType.setPreferredSize(new java.awt.Dimension(130, 25));
        chkCash.setText("Cash");
        chkCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCashActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransType.add(chkCash, gridBagConstraints);

        chkTransfer.setText("Transfer");
        chkTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTransferActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransType.add(chkTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductDetails.add(panTransType, gridBagConstraints);

        lblFromAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 2);
        panProductDetails.add(lblFromAccount, gridBagConstraints);

        panFromAccount.setLayout(new java.awt.GridBagLayout());

        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panFromAccount.add(txtFromAccount, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnFromAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setEnabled(false);
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromAccount.add(btnFromAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panProductDetails.add(panFromAccount, gridBagConstraints);

        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 2);
        panProductDetails.add(lblToAccount, gridBagConstraints);

        panToAccount.setLayout(new java.awt.GridBagLayout());

        txtToAccount.setAllowAll(true);
        txtToAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panToAccount.add(txtToAccount, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnToAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.setEnabled(false);
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount.add(btnToAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panProductDetails.add(panToAccount, gridBagConstraints);

        lblCustID.setText("Member/Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblCustID, gridBagConstraints);

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblCustName, gridBagConstraints);

        panCustID.setLayout(new java.awt.GridBagLayout());

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panCustID.add(txtCustID, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustID.setEnabled(false);
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustID.add(btnCustID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panProductDetails.add(panCustID, gridBagConstraints);

        lblSINumber.setText("SI Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblSINumber, gridBagConstraints);

        panSINumber.setLayout(new java.awt.GridBagLayout());

        txtSINumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSINumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSINumberFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panSINumber.add(txtSINumber, gridBagConstraints);

        btnSINumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnSINumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSINumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSINumber.setEnabled(false);
        btnSINumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSINumberActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSINumber.add(btnSINumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panProductDetails.add(panSINumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 6, 1);
        panDepositInterestApplication.add(panProductDetails, gridBagConstraints);

        panProductTableData.setLayout(new java.awt.GridBagLayout());

        panProductTableData.setBorder(new javax.swing.border.TitledBorder("Deposit Interest Application Details"));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        srpDepositInterestApplication.setMinimumSize(new java.awt.Dimension(810, 335));
        srpDepositInterestApplication.setPreferredSize(new java.awt.Dimension(810, 335));
        tblDepositInterestApplication.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date", "Mat Date", "From Date", "To Date", "Interest", "SI A/c No", "Cal Freq"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDepositInterestApplication.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositInterestApplicationMouseClicked(evt);
            }
        });

        srpDepositInterestApplication.setViewportView(tblDepositInterestApplication);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panProductTableData.add(srpDepositInterestApplication, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDepositInterestApplication.add(panProductTableData, gridBagConstraints);

        panSelectAll.setLayout(new java.awt.GridBagLayout());

        panSelectAll.setMinimumSize(new java.awt.Dimension(101, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(101, 27));
        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositInterestApplication.add(panSelectAll, gridBagConstraints);

        panProcess.setLayout(new java.awt.GridBagLayout());

        panProcess.setMinimumSize(new java.awt.Dimension(780, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 30));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 120, 4, 4);
        panProcess.add(btnProcess, gridBagConstraints);

        lblTokenNo.setText("Token No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 50, 2, 2);
        panProcess.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTokenNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 6);
        panProcess.add(txtTokenNo, gridBagConstraints);

        lblTotalTransactionAmt.setText("Total Amout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalTransactionAmt, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalTransactionAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDepositInterestApplication.add(panProcess, gridBagConstraints);

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void txtSINumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSINumberFocusLost
        // TODO add your handling code here:
        if(txtProductID.getText().length()>0){
            if(txtSINumber.getText().length()>0){
                HashMap whereMap = new HashMap();
                whereMap.put("INT_PAY_ACC_NO",txtSINumber.getText());
                whereMap.put("PROD_ID",txtProductID.getText());
                List lst=ClientUtil.executeQuery("getDepositIntPayAccountNo",whereMap);
                if(lst !=null && lst.size()>0){
                    enableDisable(false);
                    ClientUtil.enableDisable(panSINumber, true, false, true);
                    btnCalculate.setEnabled(true);
                    txtFromAccount.setText("");
                    txtToAccount.setText("");
                    txtCustID.setText("");
                    viewType = "SI_NUMBER";
                    whereMap=(HashMap)lst.get(0);
                    fillData(whereMap);
                    lst=null;
                    whereMap=null;
                    chkTransfer.setSelected(true);
                    chkCash.setSelected(false);
                }else{
                    ClientUtil.displayAlert("Invalid SI No !!! ");
                    txtSINumber.setText("");
                    enableDisable(true);
                }
            }
        }else{
            ClientUtil.displayAlert("Select Product ID !!! ");
        }
    }//GEN-LAST:event_txtSINumberFocusLost

    private void btnSINumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSINumberActionPerformed
        // TODO add your handling code here:
        callView("SI_NUMBER");
        if(txtSINumber.getText().length()>0){
            enableDisable(false);
            ClientUtil.enableDisable(panSINumber, true, false, true);
            btnCalculate.setEnabled(true);
            txtFromAccount.setText("");
            txtToAccount.setText("");
            txtCustID.setText("");
        }else{
            enableDisable(true);
        }
    }//GEN-LAST:event_btnSINumberActionPerformed

    private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
        // TODO add your handling code here:
            String tokenNo = CommonUtil.convertObjToStr(txtTokenNo.getText());
            if (!tokenNo.equals("")) {
                HashMap tokenWhereMap = new HashMap();
                
                // Separating Serias No and Token No
                char[] chrs = tokenNo.toCharArray();
                StringBuffer seriesNo = new StringBuffer();
                int i=0;
                for (int j= chrs.length; i < j; i++ ) {
                    if (Character.isDigit(chrs[i]))
                        break;
                    else
                        seriesNo.append(chrs[i]);
                }
                
                tokenWhereMap.put("SERIES_NO", seriesNo.toString());
                tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
                tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
                tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                tokenWhereMap.put("CURRENT_DT", currDate.clone());
                
                List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
                
                if (((Integer) lst.get(0)).intValue() == 0) {
                    txtTokenNo.setText("");
                    ClientUtil.showMessageWindow("Token is invalid or not issued for you. Please verify.");
                }
            }
    }//GEN-LAST:event_txtTokenNoFocusLost

    private void chkTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTransferActionPerformed
        // TODO add your handling code here:
        if (chkTransfer.isSelected()) {
            chkCash.setSelected(false);
            lblTokenNo.setVisible(false);
            txtTokenNo.setVisible(false);
        } else {
            chkCash.setSelected(true);
            lblTokenNo.setVisible(true);
            txtTokenNo.setVisible(true);
            txtTokenNo.setEnabled(true);
        }
    }//GEN-LAST:event_chkTransferActionPerformed

    private void chkCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCashActionPerformed
        // TODO add your handling code here:
        if (chkCash.isSelected()) {
            chkTransfer.setSelected(false);
            lblTokenNo.setVisible(true);
            txtTokenNo.setVisible(true);
            txtTokenNo.setEnabled(true);
        } else {
            lblTokenNo.setVisible(false);
            txtTokenNo.setVisible(false);
            chkTransfer.setSelected(true);
        }
    }//GEN-LAST:event_chkCashActionPerformed

    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        callView("TO");
    }//GEN-LAST:event_btnToAccountActionPerformed

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        callView("FROM");
    }//GEN-LAST:event_btnFromAccountActionPerformed

    private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
        // TODO add your handling code here:
        
        String txtCustomer = txtCustID.getText();
        if (txtCustomer.length()>0) {
            HashMap cust = new HashMap();
            cust.put("CUSTOMER ID",txtCustomer);
            viewType = "CUSTOMER ID";
            fillData(cust);
        }
    }//GEN-LAST:event_txtCustIDFocusLost

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if(txtProductID.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID",txtProductID.getText());
            whereMap.put("BEHAVES_LIKE", "FIXED");
            List lst=ClientUtil.executeQuery("getAcctHead",whereMap);
            if(lst !=null && lst.size()>0){
                whereMap = (HashMap) lst.get(0);
                observable.setTxtProductID(txtProductID.getText());
                lblProductName.setText(CommonUtil.convertObjToStr(whereMap.get("PROD_DESC")));
                enableDisable(true);
            }else{
                ClientUtil.displayAlert("Invalid Product ID !!! ");
                txtProductID.setText("");
                observable.setTxtProductID("");
                enableDisable(false);
            }
        }
    }//GEN-LAST:event_txtProductIDFocusLost

    private void enableDisable(boolean enable) {
        ClientUtil.enableDisable(panCustID, enable, false, true);
        ClientUtil.enableDisable(panFromAccount, enable, false, true);
        ClientUtil.enableDisable(panToAccount, enable, false, true);
        ClientUtil.enableDisable(panTransType, enable, false, true);
        ClientUtil.enableDisable(panSINumber, enable, false, true);
        btnCalculate.setEnabled(enable);
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panDepositInterestApplication,false);
        ClientUtil.clearAll(this);
        btnProductID.setEnabled(true);
        txtProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        observable.setTxtTokenNo(txtTokenNo.getText());
        List finalList = new ArrayList();
        List interestList = null;
        List calFreqAccountList = new ArrayList();
        if (TrueTransactMain.TOKEN_NO_REQ.equals("Y")&& chkCash.isSelected() && txtTokenNo.getText().length()==0) {
            int opt = ClientUtil.confirmationAlert("Token No not entered\nDo you want to continue?");
            if (opt==1) {
                return;
            }
        }
        btnCalculate.setEnabled(false);
        chkSelectAll.setEnabled(false);
        btnProcess.setEnabled(false);
        if(observable.getFinalList()!= null && observable.getFinalList().size()>0){
            System.out.println("#$@$#@$@$@ observable FinalList : "+observable.getFinalList());
            for(int i=0; i<observable.getFinalList().size(); i++){
                String depNo="";
                interestList = (ArrayList)observable.getFinalList().get(i);
                depNo = CommonUtil.convertObjToStr(interestList.get(2));
                System.out.println("$#@@$@$#$@$ depNo : "+depNo);
                HashMap tempMap = new HashMap();
                for(int j=0; j<tblDepositInterestApplication.getRowCount();j++) {
                    if (CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 2)).equals(depNo) && ((Boolean)tblDepositInterestApplication.getValueAt(j, 0)).booleanValue()) {
                        tempMap = new HashMap();
                        tempMap.put("ACT_NUM", depNo+"_1");
                        tempMap.put("CUST_ID", tblDepositInterestApplication.getValueAt(j, 1));
                        if (CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 11)).equals("N")) {
                            finalList.add(tempMap);
                        } else {
                            calFreqAccountList.add(tempMap);
                        }
                    }
                }
            }
            System.out.println("#$#$$# final List:"+finalList);
            System.out.println("#$#$$# calFreqAccountList:"+calFreqAccountList);
            if (calFreqAccountList!= null && calFreqAccountList.size()>0){
                observable.setCalFreqAccountList(calFreqAccountList);
            }
             if(finalList!= null && finalList.size()>0){
                 selectMode = false;
                 observable.doAction(finalList);
                 
                 if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                     if (observable.getProxyReturnMap()==null || observable.getProxyReturnMap().size()==0) {
                         ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                         btnClearActionPerformed(null);
                     } else {
                        returnMap = observable.getProxyReturnMap();
                        List errorList = (ArrayList)returnMap.get("INTEREST_DATA");
                        List transList = new ArrayList();
                        if (errorList!=null && errorList.size()>0) {
                            for (int j=0; j<errorList.size(); j++) {
                                if (errorList.get(j) instanceof HashMap) {
                                    returnMap = (HashMap)errorList.get(j);
                                } else {
                                    transList.add(errorList.get(j));
                                }
                            }
                        }
                        System.out.println("#$#$$# returnMap:"+returnMap);
                        System.out.println("#$#$$# transList:"+transList);
                        EnhancedTableModel tbm = observable.getTblDepositInterestApplication();
                        ArrayList head = observable.getTableTitle();
                        ArrayList title = new ArrayList();
                        title.addAll(head);
                        title.add("Status");
                        title.remove(0);
                        ArrayList data = tbm.getDataArrayList();
                        ArrayList rowList = null;
                        for (int i=0; i<tblDepositInterestApplication.getRowCount(); i++) {
                            rowList = (ArrayList)data.get(i);
                             if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(i, 2))) {
                                rowList.add("Error");
                             } else {
                                 if (((Boolean)tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
                                     rowList.add("Completed");
                                 } else {
                                     rowList.add("Not Processed");
                                 }
                             }
                            rowList.remove(0);
                        }
                        tbm.setDataArrayList(data, title);
                        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
//                        col.setMaxWidth(40);
//                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
                        col.setMaxWidth(100);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
                        col.setMaxWidth(120);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
                        col.setMaxWidth(150);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
                        col.setMaxWidth(65);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
                        col.setMaxWidth(65);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(5));
                        col.setMaxWidth(65);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(6));
                        col.setMaxWidth(65);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(7));
                        col.setMaxWidth(65);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(8));
                        col.setMaxWidth(50);
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(9));
                        col.setMaxWidth(120);      
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(10));
                        col.setMaxWidth(50);   
                        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(11));
                        col.setMaxWidth(70);
                        
                        if (transList.size()>0) {
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                            System.out.println("#$#$$ yesNo : "+yesNo);
                            if (yesNo==0) {
                                TTIntegration ttIntgration = null;
                                HashMap paramMap = null;
//                                if(tblDepositInterestApplication.getRowCount() > 0){
//                                    String frmActNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(0, 1));
//                                    String toActNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getRowCount()-1, 1));
//                                    System.out.println("@#$@#$@#$toActNo:"+toActNo+" :frmActNo "+frmActNo);
//                                }
//                                for(int i=0; i<transList.size(); i++) {
                                    String fromTransId = CommonUtil.convertObjToStr(transList.get(1));
                                    String toTransId = CommonUtil.convertObjToStr(transList.get(2));
//                                    if (transId.length()>0) {
                                    paramMap = new HashMap();
                                    paramMap.put("FromTransId", fromTransId);
                                    paramMap.put("ToTransId", toTransId);
                                    paramMap.put("TransDt", currDate);
                                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    ttIntgration.setParam(paramMap);
                                    if (chkCash.isSelected()) {
                                        ttIntgration.integrationForPrint("FDIntVoucher");
                                    } else if (chkTransfer.isSelected()) {
                                        ttIntgration.integrationForPrint("FDIntTransferVoucher");
                                    }
//                                    }
//                                }
                            }
                            
                        }
                        
                     }
                 }
             }else{
                 ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                 btnProcess.setEnabled(true);
             }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

//    public class CustomTableCellRenderer extends DefaultTableCellRenderer 
//    {
//        public Component getTableCellRendererComponent
//           (JTable table, Object value, boolean isSelected,
//           boolean hasFocus, int row, int column) {
//            Component cell = super.getTableCellRendererComponent
//               (table, value, isSelected, hasFocus, row, column);
//            if( value instanceof Boolean ) {
//                HashMap returnMap = observable.getProxyReturnMap();
//                for (int i=0; i<tblDepositInterestApplication.getRowCount(); i++) {
//                     if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(i, 1))) {
//                            cell.setBackground( Color.red );
//                     }
//                }
//
////                Integer amount = (Integer) value;
////                if( amount.intValue() < 0 )
////                {
////                    cell.setBackground( Color.red );
////                    // You can also customize the Font and Foreground this way
////                    // cell.setForeground();
////                    // cell.setFont();
////                }
////                else
////                {
////                    cell.setBackground( Color.white );
////                }
//            }
//            return cell;
//        }
//    }

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if(chkSelectAll.isSelected() == true)
            flag = true;
        else
            flag = false;
        double totAmount = 0;
        for(int i=0;i <tblDepositInterestApplication.getRowCount();i++){
            tblDepositInterestApplication.setValueAt(new Boolean(flag),i,0);
            totAmount = totAmount+CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
        }
        lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));

    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tblDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
        if(selectMode == true){
            String st=CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(),0));
            if(st.equals("true")){
                tblDepositInterestApplication.setValueAt(new Boolean(false),tblDepositInterestApplication.getSelectedRow(),0);
            }else{
                tblDepositInterestApplication.setValueAt(new Boolean(true),tblDepositInterestApplication.getSelectedRow(),0);
            }
            double totAmount = 0;
            for (int i=0; i<tblDepositInterestApplication.getRowCount(); i++) {
                st=CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i,0));
                if(st.equals("true")){
                    totAmount = totAmount+CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
                }
            }
            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        }
        if (evt.getClickCount()==2) {
//            HashMap returnMap = new HashMap();
            if (returnMap!=null) {
//                returnMap = observable.getProxyReturnMap();
                if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(
                tblDepositInterestApplication.getSelectedRow(), 1))) {
                    TTException exception = (TTException)returnMap.get(tblDepositInterestApplication.getValueAt(
                    tblDepositInterestApplication.getSelectedRow(), 1));
                    parseException.logException(exception, true);
                }
            }
        }
    }//GEN-LAST:event_tblDepositInterestApplicationMouseClicked

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        btnCalculateActionPerformed();
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnCalculateActionPerformed() {
        String actNumStr = "";
        String custIDStr = "";
        if(txtProductID.getText().length()>0){
            int opt = 0;
            if(txtSINumber.getText().length()<=0){
                if (!chkCash.isSelected() && !chkTransfer.isSelected()) {
                    ClientUtil.showMessageWindow("Please select Cash or Transfer...");
                    return;
                } else {
                    if (txtFromAccount.getText().length()==0 || txtToAccount.getText().length()==0) {
                        actNumStr = "From & To Account No not selected.\n";
                    }
                    if (txtCustID.getText().length()==0) {
                        custIDStr = "Customer ID not selected.\n";
                    }
                }
                String dispString = actNumStr+custIDStr+"Do you want to continue";
                if (actNumStr.length()==0 && custIDStr.length()==0) {
                    dispString+= " with all Accounts?";
                } else {
                    dispString+= "?";
                }
                opt = ClientUtil.confirmationAlert(dispString);
            }
            if (opt==0) {
                displayInterestDetails();
            }
        } else {
            ClientUtil.showMessageWindow("Please select a Deposit Product...");
        }
    }
    
    private void displayInterestDetails() {
        selectMode = true;
        observable.setTxtProductID(txtProductID.getText());
        tblDepositInterestApplication.setEnabled(true);
        btnProductID.setEnabled(false);
        btnProcess.setEnabled(true);
        chkSelectAll.setEnabled(true);
        txtProductID.setEnabled(false);
        HashMap dataMap = new HashMap();
        dataMap.put("DO_TRANSACTION",new Boolean(false));
        dataMap.put("CHARGES_PROCESS","CHARGES_PROCESS");
        dataMap.put(CommonConstants.PRODUCT_ID, txtProductID.getText());
        dataMap.put(CommonConstants.PRODUCT_TYPE, "TD");
        if(txtCustID.getText()!=null && (!txtCustID.getText().equals(""))) {
            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        }
        if(txtSINumber.getText()!=null && (!txtSINumber.getText().equals(""))) {
            dataMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(txtSINumber.getText()));
        }
        if(txtFromAccount.getText()!=null && (!txtFromAccount.getText().equals(""))) {
            dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
        }
        if(txtToAccount.getText()!=null && (!txtToAccount.getText().equals(""))) {
            dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
        }
        Date tempDt = (Date)currDate.clone();
        if (chkCash.isSelected()) {
            dataMap.put("INTPAY_MODE", "CASH");
        }
        if (chkTransfer.isSelected()) {
            dataMap.put("INTPAY_MODE", "TRANSFER");
        }
        dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        observable.insertTableData(dataMap);
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
        col.setMaxWidth(40);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
        col.setMaxWidth(100);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
        col.setMaxWidth(120);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
        col.setMaxWidth(150);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(5));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(6));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(7));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(8));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(9));
        col.setMaxWidth(50);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(10));
        col.setMaxWidth(120);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(11));
        col.setMaxWidth(50);
        if (tblDepositInterestApplication.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
            btnProcess.setEnabled(false);
        }
    }
    
    private void btnProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductIDActionPerformed
        // TODO add your handling code here:
        callView("PROD_DETAILS");
        if(txtProductID.getText().length()>0){
            enableDisable(true);
        }
    }//GEN-LAST:event_btnProductIDActionPerformed
                                                                                        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProductID;
    private com.see.truetransact.uicomponent.CButton btnSINumber;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CCheckBox chkCash;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkTransfer;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblSINumber;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CPanel panCustID;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panFromAccount;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panSINumber;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToAccount;
    private com.see.truetransact.uicomponent.CPanel panTransType;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtSINumber;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        SendSMSUI fad = new SendSMSUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
