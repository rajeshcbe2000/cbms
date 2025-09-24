/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositInterestApplicationUI.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */
package com.see.truetransact.ui.deposit.interestapplication;

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
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.*;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;

public class DepositInterestApplicationUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    DepositInterestApplicationOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    ArrayList colorList = new ArrayList();
    boolean mat = false;
    private int flagLien = 0;
    private Boolean flagMaturity;
    List finalList = new ArrayList();
    List calFreqAccountList = new ArrayList();
    private String isDefaultValPresent = "";
    ArrayList matList=new ArrayList(); 
    ArrayList linList=new ArrayList();
    ArrayList MatLienList=new ArrayList(); 
    ButtonGroup btnTrans = new ButtonGroup();
    private boolean  isProcessed=false;
    HashMap  chequeMap=new HashMap();
    
    /**
     * Creates new form TokenConfigUI
     */
    public DepositInterestApplicationUI() {
        returnMap = null;
        flagMaturity = new Boolean(true);
        currDate = ClientUtil.getCurrentDate();
        initForm();
        enableComponents();
        ifDefaultValuePresent();
         
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {

        initComponents();
        observable = new DepositInterestApplicationOB();
        initTableData();
        txtProductID.setAllowAll(true);
        txtSINumber.setAllowAll(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        btnProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        txtProductID.setEnabled(true);
        chkSelectManual.setEnabled(true);
        cboSIProductID.setModel(observable.getCbmSIProductId());
//        cboOAProductID.setModel(observable.getCbmOAProductID());
        lstSelectedPriorityTransaction.setModel(observable.getLstSelectedTransaction());
        panReprint.setVisible(false);
        txtFromTransID.setAllowAll(true);
        txtToTransID.setAllowAll(true);
        manualEntryEnable(false);
        chkExcludeLienDeposit.setEnabled(true);
        chkSelectMaturity.setEnabled(true);
        chkSelectMaturity.setSelected(true);
        cboProdType.setModel(observable.getCbmProdType());
        btnReprintClose.setEnabled(false);
        btnCancel.setEnabled(false);
        txtProductID.setEnabled(false);
        chkSelectManual.setEnabled(false);
        chkSelectMaturity.setEnabled(false);
        chkExcludeLienDeposit.setEnabled(false);
        btnRePrint.setEnabled(false);
        btnClose.setEnabled(false);
        btnClear.setEnabled(false);
        ClientUtil.enableDisable(panProductIdMain, true);
        txtLimitAmount.setAllowNumber(true);
        txtLimitAmount.setText("0");
        txtLimitAmount.setEnabled(true);
        chkExcludeLienDeposit.setSelected(true);        
       
    }

    private void initTableData() {
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
    }

    private void manualEntryEnable(boolean flag) {

        lblManualDepositNo.setVisible(flag);
        txtManualDepositNo.setVisible(flag);
        btnManualDepositNo.setVisible(flag);
        btnDepositRemove.setVisible(flag);
        btnDepositAdd.setVisible(flag);
        lblSBNo.setVisible(flag);
        txtSBNo.setVisible(flag);
        btnSBNo.setVisible(flag);
        cboOAProductID.setVisible(flag);
        cboProdType.setVisible(flag);
        lblOAProductID.setVisible(flag);
        scrollPanAdd1.setVisible(flag);
        lstSelectedPriorityTransaction.setVisible(flag);
        lstSelectedPriorityTransaction.setEnabled(flag);
        panToAccount1.setVisible(flag);
        btnDepositRemove.setEnabled(flag);

        lblManualDepositNo.setEnabled(flag);
        txtManualDepositNo.setEnabled(flag);
        btnManualDepositNo.setEnabled(flag);
        btnDepositAdd.setEnabled(flag);
        lblSBNo.setEnabled(flag);
        txtSBNo.setEnabled(flag);
        btnSBNo.setEnabled(flag);
        scrollPanAdd1.setEnabled(flag);
        cboOAProductID.setEnabled(flag);
        cboProdType.setEnabled(flag);
        txtSBNo.setEnabled(flag);
        btnSBNo.setEnabled(flag);
        panToAccount1.setEnabled(flag);
        lblCustID.setVisible(!flag);
        btnCustID.setVisible(!flag);
        txtCustID.setVisible(!flag);
        panCustID.setVisible(!flag);
        lblFromAccount.setVisible(!flag);
        txtFromAccount.setVisible(!flag);
        btnFromAccount.setVisible(!flag);
        panFromAccount.setVisible(!flag);

        lblToAccount.setVisible(!flag);
        txtToAccount.setVisible(!flag);
        btnToAccount.setVisible(!flag);
        panToAccount.setVisible(!flag);

        btnSINumber.setVisible(!flag);
        txtSINumber.setVisible(!flag);
        lblSINumber.setVisible(!flag);
        cboSIProductID.setVisible(!flag);
        lblSIProductID.setVisible(!flag);
        panSIDetails.setVisible(!flag);

    }

    private void enableDiableSB(boolean flag) {
        txtSBNo.setEnabled(flag);
        btnSBNo.setEnabled(flag);
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

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
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
        if (flagMaturity) {
            hash.put("MATURITY", flagMaturity);
        }
        if (currField.equalsIgnoreCase("PROD_DETAILS")) {
            List prodID = new ArrayList();
            viewMap.put(CommonConstants.MAP_NAME, "getFixedDepositProducts");
        } else if (currField.equalsIgnoreCase("FROM") || currField.equalsIgnoreCase("TO") || currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL")) {
            hash.put("PROD_ID", txtProductID.getText());
            if (currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL") && chkCash.isSelected()) {
                hash.put("CASH", "CASH");
            }
            if (currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL") && chkTransfer.isSelected()) {
                hash.put("TRANSFER", "TRANSFER");
            }
            hash.put("CURR_DATE", getProperDate(currDate));
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.selBranch);
            //changed  by rishad 26/12/2014 for interbranch purpose mantis id:0010097
            // viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctInterestList");
            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
            if (currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL")) {
                //hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
                setSelectedBranchID(TrueTransactMain.selBranch);
            }
            viewMap.put(CommonConstants.MAP_WHERE, hash);
        }
        else if (currField.equalsIgnoreCase("SI_NUMBER")) {
            hash.put("PROD_ID", txtProductID.getText());
            if (cboSIProductID.getSelectedIndex() > 0) {
                hash.put("INT_PAY_PROD_ID", String.valueOf(observable.getCbmSIProductId().getKeyForSelected()));
            }
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositIntPayAccountNo");
        } else if (currField.equalsIgnoreCase("OPERATIVE_NO")) {
            String str = CommonUtil.convertObjToStr(observable.getCbmOAProductID().getKeyForSelected());
            hash.put("PROD_ID", str);
           // hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("SELECTED_BRANCH",TrueTransactMain.selBranch);
            setSelectedBranchID(TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListOA");
              //changed  by rishad 02/01/2014 for interbranch purpose mantis id:0010107
           // viewMap.put(CommonConstants.MAP_NAME, "Cash.getInterestAccountList" + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        }else if (currField.equalsIgnoreCase("FROM_TRANS_ID")) {            
            hash.put("From_Date", currDate);
            hash.put("INTEREST_APPLICATION", "INTEREST_APPLICATION");
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            if(chkReprintCash.isSelected()){
                viewMap.put(CommonConstants.MAP_NAME, "getReprintData");
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "getReprintTransData");
            } 
        }else if (currField.equalsIgnoreCase("TO_TRANS_ID")) {
            hash.put("From_Date", currDate);
            hash.put("INTEREST_APPLICATION", "INTEREST_APPLICATION");
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            if(chkReprintCash.isSelected()){
                viewMap.put(CommonConstants.MAP_NAME, "getReprintData");
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "getReprintTransData");
            }           
        }

        new ViewAll(this, viewMap).show();
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_DETAILS")) {
                    txtProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    lblProductName.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                    btnProductID.setEnabled(true);
                }
            }
            if (viewType.equals("FROM_TRANS_ID")) {
                if(chkReprintCash.isSelected()){
                    txtFromTransID.setText(CommonUtil.convertObjToStr(hash.get("TRANS_ID")));
                }else{
                    txtFromTransID.setText(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
                } 
            }
            if (viewType.equals("TO_TRANS_ID")) {
                if(chkReprintCash.isSelected()){
                    txtToTransID.setText(CommonUtil.convertObjToStr(hash.get("TRANS_ID")));
                }else{
                    txtToTransID.setText(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
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
            if (viewType.equals("DEPOSIT_NO_MANUAL")) {
                txtManualDepositNo.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
            }
            if (viewType.equals("OPERATIVE_NO")) {
                txtSBNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));

            }
            if (viewType.equals("GL")) {
                txtSBNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            }
            if (isDefaultValPresent != null && isDefaultValPresent.equals("Y")) {
                btnCalculate.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CustInfoDisplay(String custId) {

        HashMap custMap = new HashMap();
        custMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        custMap.put("CUST_ID", custId);
        List lst = ClientUtil.executeQuery("getDepositCustomerName", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            lblCustName.setText(CommonUtil.convertObjToStr(custMap.get("FNAME")));
            lst.clear();
            custMap.clear();
        } else {
            ClientUtil.showAlertWindow("Invalid Customer or This Customer not having Deposit A/cs...");
        }
        lst = null;
        custMap = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        panProductIdMain = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        btnProductID = new com.see.truetransact.uicomponent.CButton();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        panSelectAll1 = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll1 = new com.see.truetransact.uicomponent.CLabel();
        chkSelectManual = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcludeLienDeposit = new com.see.truetransact.uicomponent.CCheckBox();
        panSelectAll2 = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll2 = new com.see.truetransact.uicomponent.CLabel();
        chkSelectMaturity = new com.see.truetransact.uicomponent.CCheckBox();
        panFromTonum = new com.see.truetransact.uicomponent.CPanel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        panCustID = new com.see.truetransact.uicomponent.CPanel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        panFromAccount = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        panToAccount = new com.see.truetransact.uicomponent.CPanel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        panSIDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSIProductID = new com.see.truetransact.uicomponent.CLabel();
        cboSIProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblSINumber = new com.see.truetransact.uicomponent.CLabel();
        panSINumber = new com.see.truetransact.uicomponent.CPanel();
        txtSINumber = new com.see.truetransact.uicomponent.CTextField();
        btnSINumber = new com.see.truetransact.uicomponent.CButton();
        scrollPanAdd1 = new com.see.truetransact.uicomponent.CScrollPane();
        lstSelectedPriorityTransaction = new com.see.truetransact.uicomponent.CList();
        btnDepositAdd = new com.see.truetransact.uicomponent.CButton();
        panToAccount2 = new com.see.truetransact.uicomponent.CPanel();
        txtManualDepositNo = new com.see.truetransact.uicomponent.CTextField();
        btnManualDepositNo = new com.see.truetransact.uicomponent.CButton();
        panToAccount1 = new com.see.truetransact.uicomponent.CPanel();
        txtSBNo = new com.see.truetransact.uicomponent.CTextField();
        btnSBNo = new com.see.truetransact.uicomponent.CButton();
        lblSBNo = new com.see.truetransact.uicomponent.CLabel();
        lblManualDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblOAProductID = new com.see.truetransact.uicomponent.CLabel();
        cboOAProductID = new com.see.truetransact.uicomponent.CComboBox();
        btnDepositRemove = new com.see.truetransact.uicomponent.CButton();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        panProcessButton = new com.see.truetransact.uicomponent.CPanel();
        panTransType = new com.see.truetransact.uicomponent.CPanel();
        chkCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtLimitAmount = new com.see.truetransact.uicomponent.CTextField();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositInterestApplication = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnValidate = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnRePrint = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblCount = new com.see.truetransact.uicomponent.CLabel();
        lblCountValue = new com.see.truetransact.uicomponent.CLabel();
        panReprint = new com.see.truetransact.uicomponent.CPanel();
        panReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromTransID = new com.see.truetransact.uicomponent.CLabel();
        lblToTransID = new com.see.truetransact.uicomponent.CLabel();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTransDate = new com.see.truetransact.uicomponent.CDateField();
        txtFromTransID = new com.see.truetransact.uicomponent.CTextField();
        txtToTransID = new com.see.truetransact.uicomponent.CTextField();
        panReprintTransType = new com.see.truetransact.uicomponent.CPanel();
        chkReprintCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkReprintTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkViewPrint = new com.see.truetransact.uicomponent.CCheckBox();
        btnFromPrintTransId = new com.see.truetransact.uicomponent.CButton();
        btnToPrintTransId = new com.see.truetransact.uicomponent.CButton();
        panReprintBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnReprintClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(960, 620));
        setMinimumSize(new java.awt.Dimension(960, 620));
        setPreferredSize(new java.awt.Dimension(960, 620));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setRequestFocusEnabled(false);
        panDepositInterestApplication.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Product Details"));
        panProductDetails.setMaximumSize(new java.awt.Dimension(1000, 160));
        panProductDetails.setMinimumSize(new java.awt.Dimension(940, 160));
        panProductDetails.setPreferredSize(new java.awt.Dimension(940, 160));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductDetails.add(lblCustName, gridBagConstraints);

        panProductIdMain.setMaximumSize(new java.awt.Dimension(250, 100));
        panProductIdMain.setMinimumSize(new java.awt.Dimension(250, 100));
        panProductIdMain.setPreferredSize(new java.awt.Dimension(250, 100));
        panProductIdMain.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        panProductIdMain.add(lblProductID, gridBagConstraints);

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

        btnProductID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductID.setEnabled(false);
        btnProductID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProductID.setNextFocusableComponent(txtCustID);
        btnProductID.setPreferredSize(new java.awt.Dimension(21, 21));
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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panProductIdMain.add(panProductID, gridBagConstraints);

        lblProductName.setMaximumSize(new java.awt.Dimension(200, 21));
        lblProductName.setMinimumSize(new java.awt.Dimension(200, 21));
        lblProductName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 22, 0, 21);
        panProductIdMain.add(lblProductName, gridBagConstraints);

        panSelectAll1.setLayout(new java.awt.GridBagLayout());

        lblSelectAll1.setText("ManualEntry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panSelectAll1.add(lblSelectAll1, gridBagConstraints);

        chkSelectManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectManualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 8, 6);
        panSelectAll1.add(chkSelectManual, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.ipady = -9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panProductIdMain.add(panSelectAll1, gridBagConstraints);

        chkExcludeLienDeposit.setText("Exclude Lien Deposits");
        chkExcludeLienDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExcludeLienDepositActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 53, 0, 0);
        panProductIdMain.add(chkExcludeLienDeposit, gridBagConstraints);

        panSelectAll2.setLayout(new java.awt.GridBagLayout());

        lblSelectAll2.setText("ExcludeMatured");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll2.add(lblSelectAll2, gridBagConstraints);

        chkSelectMaturity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectMaturityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll2.add(chkSelectMaturity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -6;
        gridBagConstraints.ipady = -6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panProductIdMain.add(panSelectAll2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panProductDetails.add(panProductIdMain, gridBagConstraints);

        panFromTonum.setMaximumSize(new java.awt.Dimension(500, 110));
        panFromTonum.setMinimumSize(new java.awt.Dimension(500, 110));
        panFromTonum.setPreferredSize(new java.awt.Dimension(500, 110));
        panFromTonum.setLayout(new java.awt.GridBagLayout());

        lblCustID.setText("Member/Customer ID");
        lblCustID.setToolTipText("Member/Customer ID");
        lblCustID.setMaximumSize(new java.awt.Dimension(159, 18));
        lblCustID.setMinimumSize(new java.awt.Dimension(159, 18));
        lblCustID.setPreferredSize(new java.awt.Dimension(159, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, -14, 0, 0);
        panFromTonum.add(lblCustID, gridBagConstraints);

        panCustID.setLayout(new java.awt.GridBagLayout());

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIDActionPerformed(evt);
            }
        });
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panCustID.add(txtCustID, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setEnabled(false);
        btnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
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
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 0, 0);
        panFromTonum.add(panCustID, gridBagConstraints);

        lblFromAccount.setText("From Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 12, 0, 0);
        panFromTonum.add(lblFromAccount, gridBagConstraints);

        panFromAccount.setLayout(new java.awt.GridBagLayout());

        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtFromAccount.setNextFocusableComponent(btnFromAccount);
        txtFromAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromAccountActionPerformed(evt);
            }
        });
        txtFromAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panFromAccount.add(txtFromAccount, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccount.setEnabled(false);
        btnFromAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setNextFocusableComponent(txtToAccount);
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
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
        gridBagConstraints.gridx = 34;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 49;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 0, 19);
        panFromTonum.add(panFromAccount, gridBagConstraints);

        lblToAccount.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 28, 0, 0);
        panFromTonum.add(lblToAccount, gridBagConstraints);

        panToAccount.setLayout(new java.awt.GridBagLayout());

        txtToAccount.setAllowAll(true);
        txtToAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtToAccount.setNextFocusableComponent(btnToAccount);
        txtToAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToAccountActionPerformed(evt);
            }
        });
        txtToAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panToAccount.add(txtToAccount, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setEnabled(false);
        btnToAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
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
        gridBagConstraints.gridx = 34;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 49;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 19);
        panFromTonum.add(panToAccount, gridBagConstraints);

        panSIDetails.setMinimumSize(new java.awt.Dimension(405, 29));
        panSIDetails.setPreferredSize(new java.awt.Dimension(405, 29));
        panSIDetails.setLayout(new java.awt.GridBagLayout());

        lblSIProductID.setText("SI Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 2);
        panSIDetails.add(lblSIProductID, gridBagConstraints);

        cboSIProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSIProductID.setPopupWidth(170);
        cboSIProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSIProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSIDetails.add(cboSIProductID, gridBagConstraints);

        lblSINumber.setText("SI Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 2);
        panSIDetails.add(lblSINumber, gridBagConstraints);

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

        btnSINumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSINumber.setEnabled(false);
        btnSINumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSINumber.setNextFocusableComponent(chkCash);
        btnSINumber.setPreferredSize(new java.awt.Dimension(21, 21));
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panSIDetails.add(panSINumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 49, 0, 0);
        panFromTonum.add(panSIDetails, gridBagConstraints);

        scrollPanAdd1.setMaximumSize(new java.awt.Dimension(180, 80));
        scrollPanAdd1.setMinimumSize(new java.awt.Dimension(180, 80));
        scrollPanAdd1.setPreferredSize(new java.awt.Dimension(200, 80));

        lstSelectedPriorityTransaction.setMaximumSize(new java.awt.Dimension(200, 800));
        lstSelectedPriorityTransaction.setMinimumSize(new java.awt.Dimension(200, 800));
        lstSelectedPriorityTransaction.setPreferredSize(new java.awt.Dimension(200, 800));
        lstSelectedPriorityTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSelectedPriorityTransactionMouseClicked(evt);
            }
        });
        scrollPanAdd1.setViewportView(lstSelectedPriorityTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 48;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 15, 0, 0);
        panFromTonum.add(scrollPanAdd1, gridBagConstraints);

        btnDepositAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_RIGHTARR.jpg"))); // NOI18N
        btnDepositAdd.setText("Add");
        btnDepositAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 10, 0, 0);
        panFromTonum.add(btnDepositAdd, gridBagConstraints);

        panToAccount2.setLayout(new java.awt.GridBagLayout());

        txtManualDepositNo.setAllowAll(true);
        txtManualDepositNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtManualDepositNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtManualDepositNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panToAccount2.add(txtManualDepositNo, gridBagConstraints);

        btnManualDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnManualDepositNo.setEnabled(false);
        btnManualDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnManualDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnManualDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount2.add(btnManualDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 0);
        panFromTonum.add(panToAccount2, gridBagConstraints);

        panToAccount1.setLayout(new java.awt.GridBagLayout());

        txtSBNo.setAllowAll(true);
        txtSBNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSBNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSBNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panToAccount1.add(txtSBNo, gridBagConstraints);

        btnSBNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSBNo.setEnabled(false);
        btnSBNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSBNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSBNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSBNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccount1.add(btnSBNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 34;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 49;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 19);
        panFromTonum.add(panToAccount1, gridBagConstraints);

        lblSBNo.setText("Ac No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 33;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panFromTonum.add(lblSBNo, gridBagConstraints);

        lblManualDepositNo.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 17, 0, 0);
        panFromTonum.add(lblManualDepositNo, gridBagConstraints);

        lblOAProductID.setText("Product Id");
        lblOAProductID.setMaximumSize(new java.awt.Dimension(100, 21));
        lblOAProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        lblOAProductID.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = -40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 15, 0);
        panFromTonum.add(lblOAProductID, gridBagConstraints);

        cboOAProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOAProductID.setPopupWidth(170);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 15, 0);
        panFromTonum.add(cboOAProductID, gridBagConstraints);

        btnDepositRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_LEFTARR.jpg"))); // NOI18N
        btnDepositRemove.setText("Remove");
        btnDepositRemove.setMaximumSize(new java.awt.Dimension(103, 27));
        btnDepositRemove.setMinimumSize(new java.awt.Dimension(103, 27));
        btnDepositRemove.setPreferredSize(new java.awt.Dimension(103, 27));
        btnDepositRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 32, 0, 0);
        panFromTonum.add(btnDepositRemove, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(30, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(30, 21));
        cboProdType.setPreferredSize(new java.awt.Dimension(30, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 15, 0);
        panFromTonum.add(cboProdType, gridBagConstraints);

        lblProdType.setText("ProdType");
        lblProdType.setMaximumSize(new java.awt.Dimension(53, 18));
        lblProdType.setMinimumSize(new java.awt.Dimension(53, 18));
        lblProdType.setPreferredSize(new java.awt.Dimension(53, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        panFromTonum.add(lblProdType, gridBagConstraints);

        panProductDetails.add(panFromTonum, new java.awt.GridBagConstraints());

        panProcessButton.setMaximumSize(new java.awt.Dimension(170, 100));
        panProcessButton.setMinimumSize(new java.awt.Dimension(170, 100));
        panProcessButton.setPreferredSize(new java.awt.Dimension(170, 100));
        panProcessButton.setLayout(new java.awt.GridBagLayout());

        panTransType.setMinimumSize(new java.awt.Dimension(130, 25));
        panTransType.setPreferredSize(new java.awt.Dimension(130, 25));
        panTransType.setLayout(new java.awt.GridBagLayout());

        btnTrans.add(chkCash);
        chkCash.setText("Cash");
        chkCash.setMaximumSize(new java.awt.Dimension(60, 27));
        chkCash.setNextFocusableComponent(chkTransfer);
        chkCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCashActionPerformed(evt);
            }
        });
        chkCash.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chkCashFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTransType.add(chkCash, gridBagConstraints);

        btnTrans.add(chkTransfer);
        chkTransfer.setText("Transfer");
        chkTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTransferActionPerformed(evt);
            }
        });
        chkTransfer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chkTransferFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panTransType.add(chkTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 5, 0, 4);
        panProcessButton.add(panTransType, gridBagConstraints);

        btnCalculate.setText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(89, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 32, 12, 0);
        panProcessButton.add(btnCalculate, gridBagConstraints);

        cLabel1.setText("Limit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        panProcessButton.add(cLabel1, gridBagConstraints);

        txtLimitAmount.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 58;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 22, 0, 4);
        panProcessButton.add(txtLimitAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panProductDetails.add(panProcessButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 37, 0, 0);
        panDepositInterestApplication.add(panProductDetails, gridBagConstraints);

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Interest Application Details"));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

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

        panProductTableData.add(srpDepositInterestApplication, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 110;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 37, 0, 0);
        panDepositInterestApplication.add(panProductTableData, gridBagConstraints);

        panSelectAll.setMaximumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setMinimumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(200, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 33, 0, 0);
        panDepositInterestApplication.add(panSelectAll, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(880, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(880, 30));
        panProcess.setLayout(new java.awt.GridLayout(1, 0));

        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(80, 21));
        btnClose.setMinimumSize(new java.awt.Dimension(80, 21));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 21));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panProcess.add(btnClose);

        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(70, 21));
        btnClear.setMinimumSize(new java.awt.Dimension(70, 21));
        btnClear.setPreferredSize(new java.awt.Dimension(70, 21));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panProcess.add(btnClear);

        btnValidate.setText("Validation");
        btnValidate.setNextFocusableComponent(btnProcess);
        btnValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidateActionPerformed(evt);
            }
        });
        panProcess.add(btnValidate);

        btnProcess.setText("PROCESS");
        btnProcess.setMaximumSize(new java.awt.Dimension(80, 21));
        btnProcess.setMinimumSize(new java.awt.Dimension(80, 21));
        btnProcess.setPreferredSize(new java.awt.Dimension(80, 21));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        panProcess.add(btnProcess);

        btnRePrint.setForeground(new java.awt.Color(255, 0, 51));
        btnRePrint.setText("Reprint");
        btnRePrint.setToolTipText("Print");
        btnRePrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnRePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRePrintActionPerformed(evt);
            }
        });
        panProcess.add(btnRePrint);

        lblTotalTransactionAmt.setText("Total Amount");
        lblTotalTransactionAmt.setMaximumSize(new java.awt.Dimension(50, 18));
        lblTotalTransactionAmt.setMinimumSize(new java.awt.Dimension(50, 18));
        lblTotalTransactionAmt.setPreferredSize(new java.awt.Dimension(50, 18));
        panProcess.add(lblTotalTransactionAmt);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        panProcess.add(lblTotalTransactionAmtVal);

        lblCount.setText("Total Count");
        panProcess.add(lblCount);
        panProcess.add(lblCountValue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 0, 2);
        panDepositInterestApplication.add(panProcess, gridBagConstraints);

        panReprint.setMinimumSize(new java.awt.Dimension(830, 250));
        panReprint.setPreferredSize(new java.awt.Dimension(830, 250));
        panReprint.setLayout(new java.awt.GridBagLayout());

        panReprintDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Reprint Details"));
        panReprintDetails.setMinimumSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setPreferredSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setLayout(new java.awt.GridBagLayout());

        lblFromTransID.setText("From Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblFromTransID, gridBagConstraints);

        lblToTransID.setText("To Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblToTransID, gridBagConstraints);

        lblTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblTransDate, gridBagConstraints);

        tdtTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(tdtTransDate, gridBagConstraints);

        txtFromTransID.setAllowAll(true);
        txtFromTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromTransID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromTransIDActionPerformed(evt);
            }
        });
        txtFromTransID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromTransIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtFromTransID, gridBagConstraints);

        txtToTransID.setAllowAll(true);
        txtToTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToTransID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToTransIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtToTransID, gridBagConstraints);

        panReprintTransType.setMinimumSize(new java.awt.Dimension(130, 25));
        panReprintTransType.setPreferredSize(new java.awt.Dimension(130, 25));
        panReprintTransType.setLayout(new java.awt.GridBagLayout());

        chkReprintCash.setText("Cash");
        chkReprintCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkReprintCashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintTransType.add(chkReprintCash, gridBagConstraints);

        chkReprintTransfer.setText("Transfer");
        chkReprintTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkReprintTransferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintTransType.add(chkReprintTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintDetails.add(panReprintTransType, gridBagConstraints);

        chkViewPrint.setText("View Print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panReprintDetails.add(chkViewPrint, gridBagConstraints);

        btnFromPrintTransId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromPrintTransId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromPrintTransId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromPrintTransId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromPrintTransIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReprintDetails.add(btnFromPrintTransId, gridBagConstraints);

        btnToPrintTransId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToPrintTransId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToPrintTransId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToPrintTransId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToPrintTransIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReprintDetails.add(btnToPrintTransId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panReprint.add(panReprintDetails, gridBagConstraints);

        panReprintBtn.setMinimumSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setPreferredSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setLayout(new java.awt.GridBagLayout());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnCancel, gridBagConstraints);

        btnPrint.setForeground(new java.awt.Color(255, 0, 51));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setToolTipText("Print");
        btnPrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnPrint, gridBagConstraints);

        btnReprintClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnReprintClose.setText("Close");
        btnReprintClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnReprintClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        panReprint.add(panReprintBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 90, 0, 0);
        panDepositInterestApplication.add(panReprint, gridBagConstraints);

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
    }// </editor-fold>//GEN-END:initComponents

    private void btnDepositRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositRemoveActionPerformed
        // TODO add your handling code here:
        int index = CommonUtil.convertObjToInt(lstSelectedPriorityTransaction.getSelectedIndex());

        if (index != -1) {
            int yes = ClientUtil.confirmationAlert("Do you want to Delete Deposit No");
            if (yes == 0) {

                observable.removeTargetTransactionList(index);
            }
        }
    }//GEN-LAST:event_btnDepositRemoveActionPerformed

    private void lstSelectedPriorityTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSelectedPriorityTransactionMouseClicked

    }//GEN-LAST:event_lstSelectedPriorityTransactionMouseClicked

    private void txtSBNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSBNoFocusLost

        String strNumber = CommonUtil.convertObjToStr(txtSBNo.getText());
        if (strNumber.length() > 0) {
            if (!checkAcNoValid(strNumber)) {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtSBNo.setText("");
                return;
            }
        } else {
            ClientUtil.displayAlert("Please Enter Operative account number");
        }

    }//GEN-LAST:event_txtSBNoFocusLost
    public boolean checkAcNoValid(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                isExists = true;
                txtSBNo.setText(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                observable.getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                observable.getCbmOAProductID().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                cboOAProductID.setModel(observable.getCbmOAProductID());
                cboProdType.setModel(observable.getCbmProdType());

            } else {

                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    private void txtManualDepositNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManualDepositNoFocusLost
        // TODO add your handling code here:
        String depositNO = CommonUtil.convertObjToStr(txtManualDepositNo.getText());
        if (depositNO.length() > 0) {
            HashMap hash = new HashMap();
            hash.put("PROD_ID", txtProductID.getText());
            hash.put("CURR_DATE", getProperDate(currDate));
            hash.put("CASH", "CASH");
            if (flagMaturity) {
                hash.put("MATURITY", flagMaturity);
            }
            String str = CommonUtil.convertObjToStr(txtManualDepositNo.getText());
            if (str.length() > 0) {
                hash.put("DEPOSIT_NO", str);
            }
           // hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
            setSelectedBranchID(TrueTransactMain.selBranch);
            List lst = ClientUtil.executeQuery("TDCharges.getAcctList", hash);
            if (lst != null && lst.size() > 0) {
            } else {
                txtManualDepositNo.setText("");
                ClientUtil.displayAlert("InValid Number");
            }

        } else {
            ClientUtil.displayAlert("InValid Number");
        }
    }//GEN-LAST:event_txtManualDepositNoFocusLost

    private void btnSBNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSBNoActionPerformed
        // TODO add your handling code here:
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("GL")) {
            viewType = "GL";
            final HashMap viewMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            new ViewAll(this, viewMap).show();
        } else {
            String str = CommonUtil.convertObjToStr(observable.getCbmOAProductID().getKeyForSelected());
            if (str.length() > 0) {
                callView("OPERATIVE_NO");
            } else {
                ClientUtil.displayAlert("Please Select Product Id");
            }
        }
    }//GEN-LAST:event_btnSBNoActionPerformed

    private void btnDepositAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositAddActionPerformed
        // TODO add your handling code here:
        String depositNo = CommonUtil.convertObjToStr(txtManualDepositNo.getText());
        if (depositNo.length() > 0) {
//            if (opt1 == 0) {
//                displayInterestDetails();
//            }
            HashMap existMap = new HashMap();
            existMap.put("DEPOSIT_NO", depositNo + "_1");
            List lst = ClientUtil.executeQuery("getCountUnauthorizedTransaction", existMap);
            if (lst != null && lst.size() > 0) {
                existMap = (HashMap) lst.get(0);
                int count = CommonUtil.convertObjToInt(existMap.get("COUNT"));
                if (count > 0) {
                    ClientUtil.displayAlert("Transaction pending for Authorization or Rejection!!!");
                    txtManualDepositNo.setText("");
                } else {
                    observable.addTargetTransactionList(depositNo);
                    txtManualDepositNo.setText("");
                }
            } else {
                observable.addTargetTransactionList(depositNo);
                txtManualDepositNo.setText("");
            }
        }

    }//GEN-LAST:event_btnDepositAddActionPerformed

    private void btnManualDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualDepositNoActionPerformed
        // TODO add your handling code here:
        callView("DEPOSIT_NO_MANUAL");
    }//GEN-LAST:event_btnManualDepositNoActionPerformed

    private void chkSelectManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectManualActionPerformed
        // TODO add your handling code here:
        if (chkSelectManual.isSelected()) {
            chkCash.setSelected(true);
            chkCash.setEnabled(false);
            chkTransfer.setSelected(false);
            chkTransfer.setEnabled(false);
            if (CommonUtil.convertObjToStr(txtProductID.getText()).length() > 0) {
                manualEntryEnable(true);
            } else {
                ClientUtil.displayAlert("Please Enter Prod Id ");
                chkSelectManual.setSelected(false);
                return;
            }
//            //lock created for manual entry -Transfer
//            if (validateScreenLock(getScreenID())) {
//                this.dispose();
//                return;
//            } else {
//                insertScreenLock(getScreenID(), "EDIT");
//            }
        } else {
            chkCash.setSelected(false);
            chkCash.setEnabled(true);
            chkTransfer.setSelected(false);
            chkTransfer.setEnabled(true);
            manualEntryEnable(false);
            deleteScreenLock(getScreenID(), "EDIT");
        }
    }//GEN-LAST:event_chkSelectManualActionPerformed

    private void chkReprintTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkReprintTransferActionPerformed
        // TODO add your handling code here:
        if (chkReprintTransfer.isSelected()) {
            chkReprintCash.setSelected(false);
        } else {
            chkReprintCash.setSelected(true);
        }
        txtFromTransID.setText("");
        txtToTransID.setText("");
    }//GEN-LAST:event_chkReprintTransferActionPerformed

    private void chkReprintCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkReprintCashActionPerformed
        // TODO add your handling code here:
        if (chkReprintCash.isSelected()) {
            chkReprintTransfer.setSelected(false);
        } else {
            chkReprintTransfer.setSelected(true);
        }
        txtFromTransID.setText("");
        txtToTransID.setText("");
    }//GEN-LAST:event_chkReprintCashActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
            ClientUtil.showMessageWindow("Please select Cash or Transfer...");
            return;
        }
        HashMap dataMap = new HashMap();
        dataMap.put("FROM_TRID", txtFromTransID.getText());
        dataMap.put("TRANS_DT", currDate);
        if (chkSelectManual.isSelected()) {
            if (txtSBNo.getText().length() > 0) {
                dataMap.put("TRANSFER", "TRANSFER");
            } else {
                dataMap.put("CASH", "CASH");
            }
        } else {
            if (chkReprintCash.isSelected()) {
                dataMap.put("CASH", "CASH");
            } else {
                dataMap.put("TRANSFER", "TRANSFER");
            }
        }
        List lstData = ClientUtil.executeQuery("getSingTrId", dataMap);
        String id = "";
        if (lstData.size() > 0 && lstData.get(0) != null) {
            HashMap map = (HashMap) lstData.get(0);
            id = map.get("SINGLE_TRANS_ID").toString();
        }
        boolean flag = false;
        if (chkViewPrint.isSelected() == true) {
            flag = true;
        }
        if (txtFromTransID.getText().length() > 0 && txtToTransID.getText().length() > 0 && tdtTransDate.getDateValue().length() > 0) {
            HashMap paramMap = new HashMap();
            TTIntegration ttIntgration = null;
            paramMap.put("FromTransId", id);
            paramMap.put("ToTransId", id);
            paramMap.put("TransId", id);
            paramMap.put("TransDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtTransDate.getDateValue())));
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            System.out.println("####### paramMap" + paramMap);
            ttIntgration.setParam(paramMap);
            if (chkReprintCash.isSelected()) {
                ttIntgration.integrationForPrint("FDIntVoucher", flag);
            } else if (chkReprintTransfer.isSelected()) {
                ttIntgration.integrationForPrint("FDIntTransferVoucher", flag);
            }
            ClientUtil.enableDisable(panReprintDetails, false);
            btnPrint.setEnabled(false);
            ClientUtil.clearAll(this);
        } else {
            ClientUtil.showMessageWindow("Pls Enter all Details of Reprint !!!");
        }
    }//GEN-LAST:event_btnPrintActionPerformed
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    private void btnReprintCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnReprintCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        btnRePrint.setEnabled(true);
        panReprint.setVisible(false);
        panProcess.setVisible(true);
        panSelectAll.setVisible(true);
        panProductDetails.setVisible(true);
        panProductTableData.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cboSIProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSIProductIDActionPerformed
        // TODO add your handling code here:
        if (txtProductID.getText().length() > 0) {
            String prodID = String.valueOf(cboSIProductID.getSelectedItem());
            if (prodID.length() > 0) {
                enableDisable(false);
                ClientUtil.enableDisable(panSINumber, true, false, true);
                btnCalculate.setEnabled(true);
                //                txtSINumber.setEnabled(false);
                //                btnSINumber.setEnabled(false);
                txtFromAccount.setText("");
                txtToAccount.setText("");
                txtCustID.setText("");
                lblCustName.setText("");
                chkTransfer.setSelected(true);
//                chkTransfer.setFocusable(true);
                chkTransferActionPerformed(null);// Added by nithya on 11-12-2019 for KD-961
                cboSIProductID.setEnabled(true);
                chkCash.setSelected(false);
                chkExcludeLienDeposit.setEnabled(true);
            } else {
                enableDisable(true);
            }
        }
    }//GEN-LAST:event_cboSIProductIDActionPerformed

    private void txtSINumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSINumberFocusLost
        // TODO add your handling code here:
        if (txtProductID.getText().length() > 0) {
            if (txtSINumber.getText().length() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("INT_PAY_ACC_NO", txtSINumber.getText());
                whereMap.put("PROD_ID", txtProductID.getText());
                if (cboSIProductID.getSelectedIndex() > 0) {
                    whereMap.put("INT_PAY_PROD_ID", String.valueOf(observable.getCbmSIProductId().getKeyForSelected()));
                }
                List lst = ClientUtil.executeQuery("getDepositIntPayAccountNo", whereMap);
                if (lst != null && lst.size() > 0) {
                    enableDisable(false);
                    ClientUtil.enableDisable(panSINumber, true, false, true);
                    btnCalculate.setEnabled(true);
                    txtFromAccount.setText("");
                    txtToAccount.setText("");
                    txtCustID.setText("");
                    cboSIProductID.setSelectedItem("");
                    viewType = "SI_NUMBER";
                    whereMap = (HashMap) lst.get(0);
                    fillData(whereMap);
                    lst = null;
                    whereMap = null;
                    chkTransfer.setSelected(true);
                    chkCash.setSelected(false);
                } else {
                    ClientUtil.displayAlert("Invalid SI No !!! ");
                    txtSINumber.setText("");
                    enableDisable(true);
                }
            }
            btnCalculate.setFocusable(true);
            btnCalculate.requestFocus(true);
        } else {
            ClientUtil.displayAlert("Select Product ID !!! ");
        }
    }//GEN-LAST:event_txtSINumberFocusLost

    private void btnSINumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSINumberActionPerformed
        // TODO add your handling code here:
        callView("SI_NUMBER");
        if (txtSINumber.getText().length() > 0) {
            enableDisable(false);
            ClientUtil.enableDisable(panSINumber, true, false, true);
            btnCalculate.setEnabled(true);
            txtFromAccount.setText("");
            txtToAccount.setText("");
            txtCustID.setText("");
            chkTransfer.setSelected(true);
            chkCash.setSelected(false);
            cboSIProductID.setEnabled(true);
        } else {
            enableDisable(true);
        }
    }//GEN-LAST:event_btnSINumberActionPerformed

    private void chkTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTransferActionPerformed
        // TODO add your handling code here:
        if (chkTransfer.isSelected()) {
            cboSIProductID.requestFocus(true);
            chkCash.setSelected(false);
//            lblTokenNo.setVisible(false);
//            txtTokenNo.setVisible(false);
            enableDiableSB(true);
            if (validateScreenLock(getScreenID())) {
                btnCloseActionPerformed(null); // Added by nithya on 11-12-2019 for KD-961
                this.dispose();
                return;
            } else {
                insertScreenLock(getScreenID(), "EDIT");
            }
        } else {
            chkCash.setSelected(true);
//            lblTokenNo.setVisible(true);
//            txtTokenNo.setVisible(true);
//            txtTokenNo.setEnabled(true);
            txtSBNo.setEnabled(false);
            btnSBNo.setEnabled(false);
            enableDiableSB(true);
            deleteScreenLock(getScreenID(), "EDIT");
            btnCalculate.requestFocus(true);
        }
    }//GEN-LAST:event_chkTransferActionPerformed

    private void chkCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCashActionPerformed
        // TODO add your handling code here:
        if (chkCash.isSelected()) {
            chkTransfer.setSelected(false);
//            lblTokenNo.setVisible(true);
//            txtTokenNo.setVisible(true);
//            txtTokenNo.setEnabled(true);
            enableDiableSB(true);
            deleteScreenLock(getScreenID(), "EDIT");
            btnCalculate.requestFocus(true);
        } else {
//            lblTokenNo.setVisible(false);
//            txtTokenNo.setVisible(false);
            chkTransfer.setSelected(true);
            chkTransferActionPerformed(null);
            enableDiableSB(true);
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
        if (txtCustomer.length() > 0) {
            HashMap cust = new HashMap();
            cust.put("CUSTOMER ID", txtCustomer);
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
        if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
            insertScreenLock(getScreenID(), "EDIT");
        }
        if (txtProductID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtProductID.getText());
            whereMap.put("BEHAVES_LIKE", "FIXED");
            List lst = ClientUtil.executeQuery("getAcctHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                observable.setTxtProductID(txtProductID.getText());
                lblProductName.setText(CommonUtil.convertObjToStr(whereMap.get("PROD_DESC")));
                enableDisable(true);
            } else {
                ClientUtil.displayAlert("Invalid Product ID !!! ");
                txtProductID.setText("");
                observable.setTxtProductID("");
                enableDisable(false);
            }
            chkExcludeLienDeposit.setEnabled(true);
            chkSelectMaturity.setEnabled(true);
            chkSelectMaturity.setSelected(true);
            chkExcludeLienDeposit.setSelected(true);
        }
    }//GEN-LAST:event_txtProductIDFocusLost

    private void enableDisable(boolean enable) {
        ClientUtil.enableDisable(panCustID, enable, false, true);
        ClientUtil.enableDisable(panFromAccount, enable, false, true);
        ClientUtil.enableDisable(panToAccount, enable, false, true);
        ClientUtil.enableDisable(panTransType, enable, false, true);
        ClientUtil.enableDisable(panSINumber, enable, false, true);
        btnCalculate.setEnabled(enable);
        cboSIProductID.setEnabled(enable);
        chkExcludeLienDeposit.setEnabled(enable);
    }

    private void addingRecordsinFinalList(HashMap hmap, double interestAmount, String depNo, String freq, String custId, String intCalcUpto,String chequeNo,double tdsAmount) {
        HashMap tempMap = new HashMap();
        String type = CommonUtil.convertObjToStr(hmap.get("ACCT_TYPE"));
        String amount = CommonUtil.convertObjToStr(hmap.get("AMOUNT"));
        String actno = CommonUtil.convertObjToStr(hmap.get("ACCT_NUM"));
        String principal = CommonUtil.convertObjToStr(hmap.get("PRINCIPAL"));
        String demand = CommonUtil.convertObjToStr(hmap.get("TOTAL_DEMAND"));
        tempMap = new HashMap();
        tempMap.put("ACCT_NUM", actno);
        tempMap.put("ACT_NUM", depNo + "_1");
        tempMap.put("BONUS_AMT", hmap.get("BONUS"));
        tempMap.put("PENAL_AMT", hmap.get("PENAL"));
        tempMap.put("DISCOUNT_AMT", hmap.get("DISCOUNT"));
        tempMap.put("CHARGES", hmap.get("CHARGES"));
        tempMap.put("INTEREST", hmap.get("INTEREST"));
        tempMap.put("CUST_ID", custId);
        tempMap.put("ACCT_TYPE", type);
        tempMap.put("AMOUNT", amount);
        tempMap.put("INTEREST_AMOUNT", String.valueOf(interestAmount));
        tempMap.put("PRINCIPAL", principal);
        tempMap.put("TOTAL_DEMAND", demand);
        tempMap.put("INT_CALC_UPTO_DT", intCalcUpto);//currDate);
        tempMap.put("ARBITRATION_AMT", hmap.get("ARBITRATION"));
        tempMap.put("NOTICE_AMT", hmap.get("NOTICE_AMT"));
        tempMap.put("CHEQUENO",chequeNo);
        tempMap.put("TDS_AMOUNT",tdsAmount); // 06-02-2020
        if (freq.equals("N")) {
            finalList.add(tempMap);
        } else {
            calFreqAccountList.add(tempMap);
        }
    }
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        int totalCount=0;
        for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
            if (tblDepositInterestApplication.getValueAt(i, 13).equals("Error")) {
            } else {
                totalCount++;
                tblDepositInterestApplication.setValueAt(new Boolean(flag), i, 0);
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
            }
        }
        lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        lblCountValue.setText(CommonUtil.convertObjToStr(totalCount));

    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (matList.contains(row)) {
                    setForeground(Color.magenta);
                } else if (MatLienList.contains(row)) {
                    setForeground(Color.BLUE);
                } else if (linList.contains(row)) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblDepositInterestApplication.setDefaultRenderer(Object.class, renderer);
    }

    private void tblDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
        List LienList = null;
        if (selectMode == true && tblDepositInterestApplication.getSelectedColumn() == 0) {
            String st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0));

            if (st.equals("true")) {
                tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
            } else {
                //added by rishad 18/03/2015 
                tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
                     if (tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 13).equals("Error")) {
                        tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
                    } else {
                        tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
                    }
//                if (tblDepositInterestApplication.getColumnCount() > 13) {
//                    if (tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 13).equals("DataIssue")) {
//                        tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
//                    } else {
//                        tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
//                    }
//
//                } else {
//                    tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
//                }
                //end
            }
            double totAmount = 0;
            int totalCount=0;
            for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 0));
                if (st.equals("true")) {
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
                   totalCount++;
                }
            }
            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
            lblCountValue.setText(CommonUtil.convertObjToStr(totalCount));
        }
        if (evt.getClickCount() == 2) {
            if (returnMap != null) {
                if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(
                        tblDepositInterestApplication.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblDepositInterestApplication.getValueAt(
                            tblDepositInterestApplication.getSelectedRow(), 1));
                    parseException.logException(exception, true);

                }
            }
            //added by rishad 02-09-2016 	0004494: Need a Facility to enter the cheque number in Deposit interest processing screen(assigned by soji)
              if ((tblDepositInterestApplication.getSelectedColumn() == 1) && ((Boolean) tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0)).booleanValue()) {
                 String chequeNo = COptionPane.showInputDialog(this, "Enter Cheque No");
                 chequeMap.put(CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2)),chequeNo);
       
              }
            //Added By Suresh
            if ((tblDepositInterestApplication.getSelectedColumn() == 9) && ((Boolean) tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0)).booleanValue()) {
                if (CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 9).toString()).doubleValue() > 0) {
                    if (chkTransfer.isSelected()) {
                        ArrayList list = observable.getAccountsList();
                        HashMap rdListMap = new HashMap();
                        rdListMap = (HashMap) list.get(tblDepositInterestApplication.getSelectedRow());
                        String type = CommonUtil.convertObjToStr(rdListMap.get("ACCT_TYPE"));
                        if (!type.equals("RECURRING")) {
                            enteredAmount();
                            calculatreAmt();
                        }
                    } else {
                        enteredAmount();
                        calculatreAmt();
                    }
                }
            }
            String lienAccno = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2));
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", lienAccno);
            LienList = ClientUtil.executeQuery("getLienAccNoForDispiaying", hmap);
            if (LienList != null && LienList.size() > 0) {
                ArrayList displayList = new ArrayList();
                for (int i = 0; i < LienList.size(); i++) {
                    hmap = (HashMap) LienList.get(i);
                    displayList.add(hmap);
                }
                new LienDetailsUI(displayList).show();
            }
            HashMap reportParamMap = new HashMap();
            // TODO add your handling code here:
           if ((tblDepositInterestApplication.getSelectedColumn() == 2)) {
            String actNum = (String) tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2);
            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
            HashMap prodMap = new HashMap();

            HashMap paramMap = new HashMap();
            paramMap.put("AccountNo", actNum);
            paramMap.put("FromDt", currDate.clone());
            paramMap.put("ToDt", currDate.clone());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            if (actNum.lastIndexOf('_') != -1) {
                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
            }
            prodMap.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("DAILY")) {
                    ttIntgration.integration("DailyLedger");
                } else {
                    ttIntgration.integration("TDLedger");
                }
            }
               }
        }

    }//GEN-LAST:event_tblDepositInterestApplicationMouseClicked
    
    private void calculatreAmt()
    {  
            double totAmount = 0;
            int totalCount=0;
            for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
            String   st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 0));
                if (st.equals("true")) {
                    System.out.println("enter amount"+ CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue());
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
                    System.out.println("totamount.."+totAmount);
                    totalCount++;
                }
            }
            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
            lblCountValue.setText(CommonUtil.convertObjToStr(totalCount));
        
    }
    
    //Added By Suresh
    
    
    
 
    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock != null && lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }
    
    public void enteredAmount() {
        double intAmount = CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 9).toString()).doubleValue();
        String intPayAcno = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 10));
        double clearBal = 0.0;
        ArrayList list = observable.getAccountsList();
       // System.out.println("########### ACCT list : " + list);
        HashMap rdListMap = new HashMap();
        rdListMap = (HashMap) list.get(tblDepositInterestApplication.getSelectedRow());
        String type = CommonUtil.convertObjToStr(rdListMap.get("ACCT_TYPE"));
        double amount = CommonUtil.convertObjToDouble(rdListMap.get("AMOUNT")).doubleValue();
      //  System.out.println("########### ACCT_TYPE : " + type);
        rdListMap.put("ACT_NUM", intPayAcno);
        List loanList = ClientUtil.executeQuery("LoneFacilityDetailAD", rdListMap);
        if (loanList != null && loanList.size() > 0) {
            rdListMap = (HashMap) loanList.get(0);
            clearBal = CommonUtil.convertObjToDouble(rdListMap.get("CLEAR_BALANCE")).doubleValue();
        }
        HashMap amountMap = new HashMap();
        amountMap.put("TITLE", "Interest Amount");
        if (type.equals("LOANS_AGAINST_DEPOSITS")) {

            amountMap.put("TOTAL_DEMAND", new Double(amount));
            amountMap.put("CLEAR_BALANCE", new Double(clearBal));
        }
        amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
        amountMap.put("SELECTED_AMT", String.valueOf(intAmount));
        amountMap.put("CALCULATED_AMT", String.valueOf(intAmount));
        TextUI textUI = new TextUI(this, this, amountMap);
    }

    public void modifyTransData(Object obj) {
        TextUI objTextUI = (TextUI) obj;
        String selectedDepNo = "";
        String enteredData = objTextUI.getTxtData();
        double intAmt = CommonUtil.convertObjToDouble(enteredData).doubleValue();
        selectedDepNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2));
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            List interestList = null;
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String depNo = "";
                String siNo = "";
                interestList = new ArrayList();
                interestList = (ArrayList) observable.getFinalList().get(i);
                depNo = CommonUtil.convertObjToStr(interestList.get(2));
                siNo = CommonUtil.convertObjToStr(interestList.get(10));
                if (selectedDepNo.equals(depNo)) {
                    interestList.set(9, new Double(intAmt));
                }
                if (siNo == null || siNo.length() <= 0) {
                    siNo = "";
                    interestList.set(10, siNo);
                }
            }
        }
        ArrayList list = observable.getAccountsList();
        observable.updateInterestData();
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        setSizeTableData();
        calculatreAmt();
    }

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        btnCalculateActionPerformed();
    }//GEN-LAST:event_btnCalculateActionPerformed
    private void ifDefaultValuePresent() {
//        isDefaultValPresent
        String defaultVal = "";
        HashMap whereMap = new HashMap();
        List lstDefaultValue = ClientUtil.executeQuery("getDefaultValue", whereMap);
        if (lstDefaultValue != null && lstDefaultValue.size() > 0) {
            whereMap = (HashMap) lstDefaultValue.get(0);
            isDefaultValPresent = "Y";
            if (whereMap.containsKey("PROD_ID") && whereMap.get("PROD_ID") != null) {
                defaultVal = CommonUtil.convertObjToStr(whereMap.get("PROD_ID"));
            }
        }
        if (isDefaultValPresent != null && isDefaultValPresent.equals("Y")) {
            btnCalculate.setEnabled(true);
            ClientUtil.clearAll(panFromTonum);
            chkSelectMaturity.setEnabled(true);
            chkExcludeLienDeposit.setSelected(true);
            txtFromAccount.setText(CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID + defaultVal));
            txtProductID.setText(defaultVal);
            enableDisable(true);
        }
    }

    private void enableComponents() {
        btnReprintClose.setEnabled(true);
        btnCancel.setEnabled(true);
        txtProductID.setEnabled(true);
        chkSelectManual.setEnabled(true);
        chkSelectMaturity.setEnabled(true);
        chkExcludeLienDeposit.setEnabled(true);
        chkExcludeLienDeposit.setSelected(true);
        btnProductID.setEnabled(true);
        btnRePrint.setEnabled(true);
        btnClose.setEnabled(true);
        btnClear.setEnabled(true);
    }

    private void btnCalculateActionPerformed() {
        String actNumStr = "";
        String custIDStr = "";
        String dispString = "";
        if (txtProductID.getText().length() > 0) {
            int opt = 0;
            if (txtSINumber.getText().length() <= 0) {
                if (!chkCash.isSelected() && !chkTransfer.isSelected()) {
                    ClientUtil.showMessageWindow("Please select Cash or Transfer...");
                    return;
                } else {
                    if (cboSIProductID.getSelectedIndex() <= 0 && (!chkSelectManual.isSelected())) {
                        
                        if (txtFromAccount.getText().length() == 0 && txtToAccount.getText().length() == 0) {
                            actNumStr = "From & To Account No not selected.\n";
                        }
                        if (txtFromAccount.getText().length() >= 0 && txtToAccount.getText().length() == 0) {
                            txtToAccount.setText(txtFromAccount.getText());
                        }

                        if (txtCustID.getText().length() == 0) {
                            custIDStr = "Customer ID not selected.\n";
                        }
                    }
                }
                if (cboSIProductID.getSelectedIndex() <= 0 && (!chkSelectManual.isSelected())) {
                    dispString = actNumStr + custIDStr + "Do you want to continue";
                    if (actNumStr.length() == 0 && custIDStr.length() == 0) {
                        dispString += " with all Accounts?";
                    } else {
                        dispString += "?";
                    }
                    if(chkCash.isSelected()){//Added By Suresh R on 30-Aug-2019  Ref By Jithesh  KDSA-449
                        if(txtFromAccount.getText().length() == 0 && txtToAccount.getText().length() == 0 && txtCustID.getText().length() == 0){
                            ClientUtil.showMessageWindow("Please Enter the Customer ID or From & To Account No !!!");
                            return;
                        }
                    }
                    if (isDefaultValPresent != null && !isDefaultValPresent.equals("Y")) {
                        opt = ClientUtil.confirmationAlert(dispString);
                    }
                }
                
                if(chkSelectManual.isSelected() && lstSelectedPriorityTransaction.getModel().getSize() == 0){
                    ClientUtil.showMessageWindow("Please enter at least one deposit No !!!");
                   return;
                }
            }
            if (opt == 0) {
                displayInterestDetails();
            }
        } else {
            ClientUtil.showMessageWindow("Please select a Deposit Product...");
        }
    }

    private void displayInterestDetails() {
        btnRePrint.setEnabled(false);
        selectMode = true;
        observable.setTxtProductID(txtProductID.getText());
        tblDepositInterestApplication.setEnabled(true);
        btnProductID.setEnabled(false);
       // btnProcess.setEnabled(true);
        chkSelectAll.setEnabled(true);
        txtProductID.setEnabled(false);
        chkSelectManual.setEnabled(false);
        final HashMap dataMap = new HashMap();
        dataMap.put("DO_TRANSACTION", new Boolean(false));
        dataMap.put("CHARGES_PROCESS", "CHARGES_PROCESS");
        dataMap.put(CommonConstants.PRODUCT_ID, txtProductID.getText());
        dataMap.put(CommonConstants.PRODUCT_TYPE, "TD");
        if (txtCustID.getText() != null && (!txtCustID.getText().equals(""))) {
            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        }
        if (txtSINumber.getText() != null && (!txtSINumber.getText().equals(""))) {
            dataMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(txtSINumber.getText()));
        }
        if (cboSIProductID.getSelectedIndex() > 0) {
            dataMap.put("INT_PAY_PROD_ID", String.valueOf(observable.getCbmSIProductId().getKeyForSelected()));
        }
        if (txtFromAccount.getText() != null && (!txtFromAccount.getText().equals(""))) {
            dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
        }
        if (txtToAccount.getText() != null && (!txtToAccount.getText().equals(""))) {
            dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
        }
        if (chkSelectManual.isSelected() && lstSelectedPriorityTransaction.getModel().getSize() > 0) {
            dataMap.put("ACT_NO_LIST", observable.getListDeposits());
            dataMap.put("SB_NO", CommonUtil.convertObjToStr(txtSBNo.getText()));
            dataMap.put("DEPOSIT_MANUAL","DEPOSIT_MANUAL");
        }

        Date tempDt = (Date) currDate.clone();
        if (chkCash.isSelected()) {
            dataMap.put("INTPAY_MODE", "CASH");
        }
        if (chkTransfer.isSelected()) {
            dataMap.put("INTPAY_MODE", "TRANSFER");
        }
        dataMap.put("LIMIT_AMOUNT",txtLimitAmount.getText());
      //  dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
       dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
        HashMap lienMap = new HashMap();
        
        lienMap.put("PROD_ID", txtProductID.getText());
        List lienList = ClientUtil.executeQuery("getExcludeLienFrmIntrstAppl", lienMap);
        lienMap = new HashMap();
        lienMap = (HashMap) lienList.get(0);
        System.out.println("EXCLUDE_LIEN_FRM_INTRST_APPL" + lienMap.get("EXCLUDE_LIEN_FRM_INTRST_APPL") + "ss" + chkExcludeLienDeposit.isSelected());
        if (lienMap.get("EXCLUDE_LIEN_FRM_INTRST_APPL").equals("Y") || chkExcludeLienDeposit.isSelected() == true) {
            flagLien = 1;
            dataMap.put("EXCLUDE_LIEN", "EXCLUDE_LIEN");
        } else {
            flagLien = 0;
        }
        dataMap.put("MATURITY", flagMaturity);
        if(flagMaturity){
           dataMap.put("EXC_MATURED", "EXC_MATURED"); 
        }
        if (lienMap.get("EXCLUDE_LIEN_FRM_INTRST_APPL").equals("Y") || chkExcludeLienDeposit.isSelected() == true) {
              dataMap.put("EXC_LIEN", "EXC_LIEN"); 
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                observable.insertTableData(dataMap);
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        tableDataAlignment();
     // tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        ArrayList list = observable.getAccountsList();
        List tabList = observable.getFinalList();
        if (tabList != null && tabList.size() > 0) {
            for (int i = 0; i < tabList.size(); i++) {
                ArrayList dataLst = (ArrayList) tabList.get(i);;
                String colStatus = "";
                if (dataLst.size() > 12) {
                    colStatus = CommonUtil.convertObjToStr(dataLst.get(12));
                }
                if (colStatus != null && colStatus.equals("MATURED LIEN") || colStatus.equals("MATURED MDS LIEN")) {
                    MatLienList.add(i);
                } else if (colStatus != null && colStatus.equals("MATURED")) {
                    matList.add(i);
                } else if (colStatus != null && colStatus.equals("LIEN") || colStatus.equals("MDS LIEN")) {
                    linList.add(i);
                }
            }
            setColour();
        }
       
        if (tblDepositInterestApplication.getRowCount() == 0) {
            String message = getReasonMessage();
            String maturedMessage = "";
            if (message.equals("")) {
                if (chkSelectMaturity.isSelected()) {
                    maturedMessage = "Deposit Already Matured";
                }
                ClientUtil.showMessageWindow(" No Data !!! " + maturedMessage);
            } else {
                ClientUtil.showMessageWindow(message);
            }
            btnProcess.setEnabled(false);
        } else {
            enableDisable(false);
        }

    }
    private void tableDataAlignment() {
        ArrayList _heading = new ArrayList();
        List tabList = observable.getFinalList();
        if (tabList != null && tabList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
          //  tableSorter.addMouseListenerToHeaderInTable(tblDepositInterestApplication);
            TableModel tableModel = new TableModel();
            _heading.add("Select");
            _heading.add("Cust ID");
            _heading.add("Account No");
            _heading.add("Name");
            _heading.add("Dep Amt");
            _heading.add("Dep Date");
            _heading.add("Mat Date");
            _heading.add("From Date");
            _heading.add("To Date");
            _heading.add("Interest");
            _heading.add("SI A/c No");
            _heading.add("Cal Freq");
            _heading.add("Dep Type");
            _heading.add("Status");
          //  _heading.add("Intpay Freq");
            _heading.add("TDS");// Added by nithya on 06-02-2020 for TDS implementation
            
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) tabList);
            //tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblDepositInterestApplication.setAutoResizeMode(0);
            tblDepositInterestApplication.doLayout();
            tblDepositInterestApplication.setModel(tableSorter);
          //  tblDepositInterestApplication.revalidate();
            
            //Set Table Column size 
           setSizeTableData();
         tblDepositInterestApplication.revalidate();
        }
    }
   private void afterTransactiontableDataAlignment() {
        ArrayList _heading = new ArrayList();
       ArrayList rowList = null;
       ArrayList tabList = (ArrayList) observable.getFinalList();
       if (tabList != null && tabList.size() > 0) {
           TableSorter tableSorter = new TableSorter();
           //   tableSorter.addMouseListenerToHeaderInTable(tblDepositInterestApplication);
           TableModel tableModel = new TableModel();
           _heading.add("Cust ID");
           _heading.add("Account No");
           _heading.add("Name");
           _heading.add("Dep Amt");
           _heading.add("Dep Date");
           _heading.add("Mat Date");
           _heading.add("From Date");
           _heading.add("To Date");
           _heading.add("Interest");
           _heading.add("SI A/c No");
           _heading.add("Cal Freq");
           _heading.add("Dep Type");
           _heading.add("TDS");// Added by nithya on 06-02-2020 for TDS implementation
           _heading.add("Status");           
           tableModel.setHeading(_heading);
           for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
               rowList = (ArrayList) tabList.get(i);
               rowList.remove(13);
               if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(i, 2))) {
                   rowList.add("Error");
                   setMode(ClientConstants.ACTIONTYPE_EDIT);
                   removeEditLock(CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 2)));
               } else {
                   if (((Boolean) tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
                       rowList.add("Completed");
                       if (chkTransfer.isSelected()) {
                           setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                           removeEditLock(CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 2)));
                       }
                   } else {
                       rowList.add("Not Processed");
                   }
               }
               rowList.remove(0);
           }
           tableModel.setData((ArrayList) tabList);
           tableModel.fireTableDataChanged();
           tableSorter.setModel(tableModel);
           tableSorter.fireTableDataChanged();
           tblDepositInterestApplication.setAutoResizeMode(0);
           tblDepositInterestApplication.doLayout();
           tblDepositInterestApplication.setModel(tableSorter);
           tblDepositInterestApplication.revalidate();
           //Set Table Column size 
           afterTransSetSizeTableData();
       }
    }
     private void afterTransSetSizeTableData() {
        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
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
    }
    private String getReasonMessage() {
        String message = "";
        String lienMsg = "";
        HashMap whereMap = new HashMap();
        String depositNo = "";
        int size = CommonUtil.convertObjToInt(lstSelectedPriorityTransaction.getModel().getSize());
        if (size == 1) {
            depositNo = CommonUtil.convertObjToStr(lstSelectedPriorityTransaction.getModel().getElementAt(0));
            whereMap.put("DEPOSIT_NO", depositNo);
        } else {
            whereMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(txtFromAccount.getText().trim()));
        }
        String fromAccount = CommonUtil.convertObjToStr(txtFromAccount.getText().trim());
        String toAccount = CommonUtil.convertObjToStr(txtToAccount.getText().trim());

        if (fromAccount.equals(toAccount) || size == 1) {
            List lst = ClientUtil.executeQuery("getLastAndNextIntAppDate", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                if (whereMap.containsKey("NEXT_INT_APPL_DT") && whereMap.get("NEXT_INT_APPL_DT") != null && whereMap.containsKey("LAST_INT_APPL_DT") && whereMap.get("LAST_INT_APPL_DT") != null) {
                    String nextDate = CommonUtil.convertObjToStr(whereMap.get("NEXT_INT_APPL_DT"));
                    String lastDate = CommonUtil.convertObjToStr(whereMap.get("LAST_INT_APPL_DT"));
                    String status = CommonUtil.convertObjToStr(whereMap.get("STATUS"));
                    if (lastDate.equals("")) {
                        lastDate = CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT"));
                    }
                    if (status.equals("LIEN")) {
                        lienMsg = "LIEN Exsist";
                    } else {
                        lienMsg = "";
                    }
                    message = "No Data Found\n" + "last Interest App Date is "
                            + lastDate + "\nNext Interest App Date is "
                            + nextDate + " " + lienMsg;
                }
            }
        }

        return message;
    }
    //Added By Suresh

    private void setSizeTableData() {
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
        col.setMaxWidth(30);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(12));
        col.setMaxWidth(50);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(13)); //06-02-2020
        col.setMaxWidth(50);
    }

    private void btnProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductIDActionPerformed

        callView("PROD_DETAILS");
        if (txtProductID.getText().length() > 0) {
            enableDisable(true);
            chkSelectMaturity.setEnabled(true);
            chkSelectMaturity.setSelected(true);
            chkExcludeLienDeposit.setSelected(true);
        }
    }//GEN-LAST:event_btnProductIDActionPerformed

private void chkSelectMaturityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectMaturityActionPerformed
// TODO add your handling code here:
    if (chkSelectMaturity.isSelected()) {
        flagMaturity = true;
    } else {
        flagMaturity = false;
    }
}//GEN-LAST:event_chkSelectMaturityActionPerformed

private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
   
      if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
            deleteScreenLock(getScreenID(), "EDIT");
        }

        //this.dispose();
}//GEN-LAST:event_formInternalFrameClosing

private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
    // TODO add your handling code here:
    if (cboProdType.getSelectedIndex() > 0) {
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (!prodType.equals("GL")) {
            observable.setCbmProdId(prodType);
            cboOAProductID.setModel(observable.getCbmOAProductID());
        }
    }
}//GEN-LAST:event_cboProdTypeActionPerformed

    private void txtFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromAccountActionPerformed
        // TODO add your handling code here:
        //added by rishad for validation checking 08/09/2016
         String actNum = CommonUtil.convertObjToStr(txtFromAccount.getText());
        if (actNum.length() > 0) {
            boolean isExist = false;
            isExist = observable.checkAcNoWithoutProdType(actNum);
            if (!isExist) {
                ClientUtil.showMessageWindow("Invalid Account No");
                txtFromAccount.setText("");
            }
        } else {
            ClientUtil.showMessageWindow("Invalid Account No");
        }
        if (isDefaultValPresent != null && isDefaultValPresent.equals("Y")) {
            btnCalculate.setEnabled(true);
            chkCash.setSelected(true);
            ClientUtil.enableDisable(panTransType, true);
        }
    }//GEN-LAST:event_txtFromAccountActionPerformed

    private void txtFromAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountFocusLost
        // TODO add your handling code here:
//        txtToAccount.requestFocus(true);
//        txtToAccount.setFocusable(true);
    }//GEN-LAST:event_txtFromAccountFocusLost

    private void chkCashFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCashFocusLost
        // TODO add your handling code here:
        if (chkCash.isSelected()) {
//            btnCalculate.requestFocus(true);
//            btnCalculate.requestFocus(true);
        } else {
//            chkTransfer.requestFocus(true);
//            chkTransfer.setFocusable(true);
        }
    }//GEN-LAST:event_chkCashFocusLost

    private void chkTransferFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkTransferFocusLost
        // TODO add your handling code here:
//        if (chkTransfer.isSelected()) {
////            cboSIProductID.requestFocus(true);
////            cboSIProductID.requestFocus(true);
//        } else {
////            btnCalculate.requesstFocus(true);
////            btnCalculate.requestFocus(true);
//        }      
    }//GEN-LAST:event_chkTransferFocusLost

    private void txtToAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccountFocusLost
        // TODO add your handling code here:
//        chkCash.requestFocus(true);
//        chkCash.setFocusable(true);
    }//GEN-LAST:event_txtToAccountFocusLost

private void txtFromTransIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromTransIDFocusLost
// TODO add your handling code here:
    HashMap whereMap = new HashMap();
    whereMap.put("TRANS_DT", currDate);
    if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
        ClientUtil.showMessageWindow("Please select Cash or Transfer first...");
        return;
    } else {
        if (txtFromTransID.getText() != null && txtFromTransID.getText().length() > 0) {
            whereMap.put("TRANS_ID", "%" + (String) txtFromTransID.getText());
            if (chkReprintCash.isSelected()) {
                whereMap.put("TRANS_MODE", "CASH");
            } else {
                whereMap.put("TRANS_MODE", "TRANSFER");
            }
            List lst = ClientUtil.executeQuery("getSelectTransIdFromPatternMatch", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                if (chkReprintCash.isSelected()) {
                    txtFromTransID.setText(CommonUtil.convertObjToStr(whereMap.get("TRANS_ID")));
                } else {
                    txtFromTransID.setText(CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
                }
            } else {
                ClientUtil.showMessageWindow("Please enter Trans id's last 3 digits !!! ");
                txtFromTransID.setText("");
            }
        } else {
            ClientUtil.showMessageWindow("Please enter Trans id's last 3 digits !!! ");
            txtFromTransID.setText("");
        }
    }

}//GEN-LAST:event_txtFromTransIDFocusLost

private void txtFromTransIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromTransIDActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtFromTransIDActionPerformed

private void txtToTransIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToTransIDFocusLost
// TODO add your handling code here:
    HashMap whereMap = new HashMap();
    whereMap.put("TRANS_DT", currDate);
    if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
        ClientUtil.showMessageWindow("Please select Cash or Transfer first...");
        return;
    } else {
        if (txtToTransID.getText() != null && txtToTransID.getText().length() > 0) {
            whereMap.put("TRANS_ID", "%" + (String) txtToTransID.getText());
            if (chkReprintCash.isSelected()) {
                whereMap.put("TRANS_MODE", "CASH");
            } else {
                whereMap.put("TRANS_MODE", "TRANSFER");
            }
            List lst = ClientUtil.executeQuery("getSelectTransIdFromPatternMatch", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                if (chkReprintCash.isSelected()) {
                    txtToTransID.setText(CommonUtil.convertObjToStr(whereMap.get("TRANS_ID")));
                } else {
                    txtToTransID.setText(CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
                }
            } else {
                ClientUtil.showMessageWindow("Please enter Trans id's last 3 digits !!! ");
                txtToTransID.setText("");
            }
        } else {
            ClientUtil.showMessageWindow("Please enter Trans id's last 3 digits !!! ");
            txtToTransID.setText("");
        }
    }
}//GEN-LAST:event_txtToTransIDFocusLost

private void btnFromPrintTransIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromPrintTransIdActionPerformed
// TODO add your handling code here:
    if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
            ClientUtil.showMessageWindow("Please select Cash or Transfer...");
            return;
    }
    callView("FROM_TRANS_ID");
}//GEN-LAST:event_btnFromPrintTransIdActionPerformed

private void btnToPrintTransIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToPrintTransIdActionPerformed
// TODO add your handling code here:
    if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
            ClientUtil.showMessageWindow("Please select Cash or Transfer...");
            return;
    }
    callView("TO_TRANS_ID");
}//GEN-LAST:event_btnToPrintTransIdActionPerformed

private void txtCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIDActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtCustIDActionPerformed

private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
    // TODO add your handling code here:.
    deleteScreenLock(getScreenID(), "EDIT");
     if(isProcessed==false){
		HashMap map = new HashMap();
		map.put("SCREEN_ID", getScreenID());
		map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
		map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
		map.put("CUR_DATE", currDate);
		ClientUtil.execute("deleteAllAccountLock", map);
    }
     chequeMap=null;
    setModified(false);
    if(isModified()==false)
    {
    this.dispose();}
    TrueTransactMain.populateBranches();
    TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
    observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
    setSelectedBranchID(ProxyParameters.BRANCH_ID);
  
}//GEN-LAST:event_btnCloseActionPerformed

private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    // TODO add your handling code here:
    //added by rishad 19/03/2015 for releasing account lock
    if(isProcessed==false){
		HashMap map = new HashMap();
		map.put("SCREEN_ID", getScreenID());
		map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
		map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
		map.put("CUR_DATE", currDate);
		ClientUtil.execute("deleteAllAccountLock", map);
    }
    setModified(false);
    observable.resetForm();
    observable.updateInterestData();
    tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
    enableDisable(false);
    ClientUtil.enableDisable(panDepositInterestApplication, false);
    if (isDefaultValPresent != null && isDefaultValPresent.equals("Y")) {
        btnCalculate.setEnabled(true);
        ClientUtil.clearAll(panFromTonum);
        chkSelectMaturity.setEnabled(true);
        chkExcludeLienDeposit.setSelected(true);
        txtFromAccount.setText(CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID + txtProductID.getText()));
        //       txtToAccount.setText(CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID+txtProductID.getText()));
    } else {
        ClientUtil.clearAll(this);
        btnCalculate.setEnabled(false);
    }
    btnRePrint.setEnabled(true);
    btnProductID.setEnabled(true);
    txtProductID.setEnabled(true);
    btnProcess.setEnabled(false);
    chkSelectAll.setEnabled(false);
    chkSelectManual.setEnabled(true);
    lblProductName.setText("");
    lblCustName.setText("");
    lblTotalTransactionAmtVal.setText("");//added by Ajay Sharma on 17-May-2014 as after clear transaction system is not clearing total amount.
    lblCountValue.setText("");
    observable.removeTargetALLTransactionList();
    
    observable.setAccountsList(null);
    colorList = null;
    chkExcludeLienDeposit.setSelected(false);
    manualEntryEnable(false);
    deleteScreenLock(getScreenID(), "EDIT");
    txtLimitAmount.setText("9");
    chkSelectManual.setSelected(false);
    chkSelectManualActionPerformed(null);
    chequeMap=null;
    TrueTransactMain.populateBranches();
    TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
    observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
    setSelectedBranchID(ProxyParameters.BRANCH_ID);
    chkExcludeLienDeposit.setSelected(true);
}//GEN-LAST:event_btnClearActionPerformed

private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
    // TODO add your handling code here:
   
    boolean count = false;
    for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
               if (!((Boolean) tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
                HashMap map = new HashMap();
                map.put("SCREEN_ID", getScreenID());
                map.put("RECORD_KEY", tblDepositInterestApplication.getValueAt(i, 2));
                map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("CUR_DATE",currDate.clone());
                ClientUtil.execute("deleteEditLock", map);
            }
        if ((Boolean) tblDepositInterestApplication.getValueAt(i, 0)) {
            count = true;
        }
    }
    if (!count) {
        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
        count = false;
        return;
    }
//    observable.setTxtTokenNo(txtTokenNo.getText());
    finalList = new ArrayList();
    List interestList = null;
    calFreqAccountList = new ArrayList();
//    if (TrueTransactMain.TOKEN_NO_REQ.equals("Y") && chkCash.isSelected() && txtTokenNo.getText().length() == 0) {
//        int opt = ClientUtil.confirmationAlert("Token No not entered\nDo you want to continue?");
//        if (opt == 1) {
//            return;
//        }
//    }
    btnCalculate.setEnabled(false);
    chkSelectAll.setEnabled(false);
    btnProcess.setEnabled(false);
    String SIActNum = "";
    StringBuffer interbranchList = new StringBuffer();
    if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
        for (int i = 0; i < observable.getFinalList().size(); i++) {
            String depNo = "";
            String ChequeNo="No Cheque";
            interestList = (ArrayList) observable.getFinalList().get(i);
            depNo = CommonUtil.convertObjToStr(interestList.get(2));
            HashMap tempMap = new HashMap();
            for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) {
                if (CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 2)).equals(depNo) && ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue()) {
                    String depNum = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 2));
                    ArrayList list = observable.getAccountsList();
                    HashMap hmap = new HashMap();
                    double interestAmount = 0.0;
                    double tdsAmount = 0.0; //06-02-2020
                    if(list!=null && list.size()>0){
                        hmap = (HashMap) list.get(j);
                    }
                    interestAmount = CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(j, 9)).doubleValue();
                    tdsAmount = CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(j, 14)).doubleValue(); // 06-02-2020
                    SIActNum = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 10));
                    String freq = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 11));
                    String custId = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 1));
                    String intCalcUpto = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 8));
                    if(chequeMap!=null&&chequeMap.containsKey(depNo)&&chequeMap.get(depNo)!=null){
                    ChequeNo=CommonUtil.convertObjToStr(chequeMap.get(depNo));
                    }
                    if (chkTransfer.isSelected() && !SIActNum.equals("") && SIActNum.length() > 0) {
                        HashMap existingMap = new HashMap();
                        existingMap.put("ACT_NUM", SIActNum.toUpperCase());
                        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
                        if (mapDataList != null && mapDataList.size() > 0) {
                            existingMap = (HashMap) mapDataList.get(0);
                            if (existingMap != null && !ProxyParameters.BRANCH_ID.equals(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")))) {
                                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")));
                                Date currentDate = (Date) currDate.clone();
                                System.out.println("selectedBranchDt : " + selectedBranchDt + " currentDate : " + currentDate);
                                if (selectedBranchDt == null) {
                                    if (interbranchList.length() == 0) {
                                        interbranchList.append("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed" + "\n" + "Deposit No : " + depNum);
                                    } else {
                                        interbranchList.append("Deposit No : " + depNum);
                                    }
                                } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) != 0) {
                                    if (interbranchList.length() == 0) {
                                        interbranchList.append("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed" + "\n" + "Deposit No : " + depNum);
                                    } else {
                                        interbranchList.append("\n" + "Deposit No : " + depNum);
                                    }
                                } else {
                                    System.out.println("Continue for interbranch trasactions ...");
                                    addingRecordsinFinalList(hmap, interestAmount, depNo, freq, custId, intCalcUpto,ChequeNo,tdsAmount);
                                }
                            } else {
                                addingRecordsinFinalList(hmap, interestAmount, depNo, freq, custId, intCalcUpto,ChequeNo,tdsAmount);
                            }
                        } else {
                            addingRecordsinFinalList(hmap, interestAmount, depNo, freq, custId, intCalcUpto,ChequeNo,tdsAmount);
                        }
                    } else {
                        addingRecordsinFinalList(hmap, interestAmount, depNo, freq, custId, intCalcUpto,ChequeNo,tdsAmount);
                    }
                }
            }
        }
        if (interbranchList != null && interbranchList.length() > 0) {
            ClientUtil.displayAlert("Please deselect the following deposits, " + "\n" + interbranchList);
            btnProcess.setEnabled(true);
            return;
        }
        //babu added for interbranch appln date validation
        if (chkSelectManual.isSelected() && chkCash.isSelected() && CommonUtil.convertObjToStr(txtSBNo.getText()).length() > 0) {
            String crBranchId="";
            HashMap interBranchCodeMap = new HashMap();
            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtSBNo.getText()));
            List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                crBranchId = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
            }
            interBranchCodeMap.put("BRANCH_CODE",crBranchId );
            List dayEndlst =ClientUtil.executeQuery("getApplDate", interBranchCodeMap);
            if(dayEndlst!=null && dayEndlst.size()>0){
                Date applDate = (Date) dayEndlst.get(0);
                GregorianCalendar c1 = new GregorianCalendar();
                applDate.setHours(c1.getTime().getHours());
                applDate.setMinutes(c1.getTime().getMinutes());
                applDate.setSeconds(c1.getTime().getSeconds());
                if(currDate.compareTo(applDate)>=0){
                    ClientUtil.showAlertWindow("Interbranch dayend date mismatch!!!");
                    btnProcess.setEnabled(true);
                    return;
                }
            }
            
        }
        if (calFreqAccountList != null && calFreqAccountList.size() > 0) {
            observable.setCalFreqAccountList(calFreqAccountList);
        }
        if (calFreqAccountList != null && calFreqAccountList.size() > 0) {
            selectMode = false;
        }
        if (chkSelectManual.isSelected() && chkCash.isSelected() && CommonUtil.convertObjToStr(txtSBNo.getText()).length() > 0) {
            if (observable.getCashtoTransferMap() == null) {
                observable.setCashtoTransferMap(new HashMap());
            }
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            observable.getCashtoTransferMap().put("PROD_TYPE", prodType);
            if (prodType.equals("GL")) {
                observable.getCashtoTransferMap().put("ACC_HEAD", CommonUtil.convertObjToStr(txtSBNo.getText()));
                observable.getCashtoTransferMap().put("PROD_ID", null);
            } else {
                observable.getCashtoTransferMap().put("OPERATIVE_NO", CommonUtil.convertObjToStr(txtSBNo.getText()));
                observable.getCashtoTransferMap().put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmOAProductID().getKeyForSelected()));
            }
        } else {
            observable.setCashtoTransferMap(null);
        }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            
            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                try{
	                observable.doAction(finalList);
	                isProcessed=true;
                }catch(Exception e) {  
	                isProcessed=false;
	                e.printStackTrace();
                }
                return null;
            }
            
            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() == null || observable.getProxyReturnMap().size() == 0) {
                ClientUtil.showMessageWindow(" Transaction Completed !!! ");
                btnClearActionPerformed(null);
            } else {
                returnMap = observable.getProxyReturnMap();
//                if(chkTransfer.isSelected()){
//                    setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
//                }
                List errorList = (ArrayList) returnMap.get("INTEREST_DATA");
                List transList = new ArrayList();
                if (errorList != null && errorList.size() > 0) {
                    for (int j = 0; j < errorList.size(); j++) {
                        if (errorList.get(j) instanceof HashMap) {
                            returnMap = (HashMap) errorList.get(j);
                        } else {
                            transList.add(errorList.get(j));
                        }
                    }
                }
                afterTransactiontableDataAlignment();
                String cashier_auth = "";
                if (transList.size() > 0) {
                    String toTransId = "";
                    String fromTransId = "";
                    List listData = ClientUtil.executeQuery("getCashierAuth", new HashMap());
                    HashMap map1 = (HashMap) listData.get(0);
                    if (map1.get("CASHIER_AUTH_ALLOWED") != null) {
                        cashier_auth = map1.get("CASHIER_AUTH_ALLOWED").toString();
                    }
                    if (cashier_auth != null && cashier_auth.equals("Y")) {
                        fromTransId = CommonUtil.convertObjToStr(transList.get(0));
                    } else {
                        fromTransId = CommonUtil.convertObjToStr(transList.get(1));
                    }
                    String message = "Please note the Transactions ID :   " + fromTransId;
                    if (transList.size() > 2) {
                        toTransId = CommonUtil.convertObjToStr(transList.get(2));
                        message = "Please note the Transactions ID" + "\n" + "From Trans ID :   " + fromTransId + "\n" + "To Trans ID :   " + toTransId;
                    }
                    ClientUtil.showMessageWindow(message);
                }
                if (transList.size() > 0) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = null;
                        String toTransId = "", fromTransId = "";
                        //modified by rishad 09/10/2015 for print issue in cashier y
                        if (chkCash.isSelected()) {
                            if (cashier_auth != null && cashier_auth.equals("Y")) {
                                fromTransId = CommonUtil.convertObjToStr(transList.get(0));
                            } else {
                                fromTransId = CommonUtil.convertObjToStr(transList.get(1));
                            }
                        } else {
                            fromTransId = CommonUtil.convertObjToStr(transList.get(1));
                        }
                        if (transList.size() > 2) {
                            toTransId = CommonUtil.convertObjToStr(transList.get(2));
                        }
                        HashMap dataMap = new HashMap();
                        dataMap.put("FROM_TRID", fromTransId);
                        dataMap.put("TRANS_DT", currDate);
                        if (chkSelectManual.isSelected()) {
                            if (txtSBNo.getText().length() > 0) {
                                dataMap.put("TRANSFER", "TRANSFER");
                            } else {
                                dataMap.put("CASH", "CASH");
                            }
                        } else {
                            if (chkCash.isSelected()) {
                                dataMap.put("CASH", "CASH");
                            } else {
                                dataMap.put("TRANSFER", "TRANSFER");
                            }
                        }
                        List lstData = ClientUtil.executeQuery("getSingTrId", dataMap);
                        String id = "";
                        if (lstData.size() > 0 && lstData.get(0) != null) {
                            HashMap map = (HashMap) lstData.get(0);
                            id = map.get("SINGLE_TRANS_ID").toString();
                        }
                        paramMap = new HashMap();
                        paramMap.put("FromTransId", fromTransId);
                        paramMap.put("ToTransId", toTransId);
                        paramMap.put("TransId", id);
                        paramMap.put("TransDt", currDate);
                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(paramMap);
                        if (chkSelectManual.isSelected()) {
                            if (chkCash.isSelected()) {
                                if (txtSBNo.getText().length() > 0) {
                                    ttIntgration.integrationForPrint("FDIntTransferVoucher");
                                } else {
                                    ttIntgration.integrationForPrint("FDIntVoucher");
                                }
                            }
                        } else {
                            if (chkCash.isSelected()) {
                                ttIntgration.integrationForPrint("FDIntVoucher");
                            } else if (chkTransfer.isSelected()) {
                                ttIntgration.integrationForPrint("FDIntTransferVoucher");
                            }
                        }
                    }
                }
                
            }
        }
    } else {
        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
        btnProcess.setEnabled(true);
    }
    
    deleteScreenLock(getScreenID(), "EDIT");
    TrueTransactMain.populateBranches();
    TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
    observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
    setSelectedBranchID(ProxyParameters.BRANCH_ID);
}//GEN-LAST:event_btnProcessActionPerformed

private void btnRePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRePrintActionPerformed
    // TODO add your handling code here:
    //Added By Suresh
    btnPrint.setEnabled(true);
    panReprint.setVisible(true);
    panProcess.setVisible(false);
    panSelectAll.setVisible(false);
    panProductDetails.setVisible(false);
    panProductTableData.setVisible(false);
    ClientUtil.enableDisable(panReprintDetails, true);
    tdtTransDate.setDateValue(DateUtil.getStringDate((Date) currDate.clone()));
}//GEN-LAST:event_btnRePrintActionPerformed
   //added by rishad 11/03/2015 for solving interest doubling issue and for validation checking
private void btnValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateActionPerformed
    setModified(true);
    List erroAcctList = new ArrayList();
    String errorAccounts = "";
    boolean count = false;
    for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
        if ((Boolean) tblDepositInterestApplication.getValueAt(i, 0)) {
            count = true;
            Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 7)));
            Date toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 8)));
            if(DateUtil.dateDiff(fromDt, toDt) < 0){                
                erroAcctList.add(tblDepositInterestApplication.getValueAt(i, 2));
                tblDepositInterestApplication.setValueAt(false, i, 0);
            }
        }
    }
    if (!count) {
        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
        count = false;
        return;
    }
    
   // Added for KD-3331
    if(erroAcctList != null && erroAcctList.size() > 0){        
        for(int k=0; k< erroAcctList.size(); k++){
            errorAccounts =errorAccounts + "\n"+CommonUtil.convertObjToStr(erroAcctList.get(k));
        }
        ClientUtil.showMessageWindow("The following accounts having wrong From - To date " +errorAccounts +" \n. Please check !!! ");
    }
    
    int countIssue = 0;
    //added for data validation issue
  //  try {
//        EnhancedTableModel tbm = observable.getTblDepositInterestApplication();
//        ArrayList head = observable.getTableTitle();
//        ArrayList title = new ArrayList();
//        title.addAll(head);
//        title.add("Status");
//        ArrayList data = tbm.getDataArrayList();
//        ArrayList rowList = null;
//        for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
//            rowList = (ArrayList) data.get(i);
//            if (((Boolean) tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
//                HashMap map = new HashMap();
//                map.put("Dep_No", CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 2)));
//                map.put("INTEREST", CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)));
//                List lstValidation = ClientUtil.executeQuery("selectDepositData", map);
//                if (lstValidation.size() > 0 && lstValidation != null) {
//                    HashMap resultValidateMap = (HashMap) lstValidation.get(0);
//                    String result = CommonUtil.convertObjToStr(resultValidateMap.get("RESULT"));
//                    if (result.equalsIgnoreCase("TRUE")) {
//                        tblDepositInterestApplication.setValueAt(new Boolean(false), i, 0);
//                        countIssue++;
//                        rowList.add("DataIssue");
//                    } else {
//                        rowList.add("Ok");
//                    }
//                }
//            } else {
//                rowList.add("NotValidate");
//            }
//        }
//        tbm.setDataArrayList(data, title);
//        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
//        col.setMaxWidth(100);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
//        col.setMaxWidth(120);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
//        col.setMaxWidth(150);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
//        col.setMaxWidth(65);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
//        col.setMaxWidth(65);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(5));
//        col.setMaxWidth(65);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(6));
//        col.setMaxWidth(65);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(7));
//        col.setMaxWidth(65);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(8));
//        col.setMaxWidth(50);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(9));
//        col.setMaxWidth(120);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(10));
//        col.setMaxWidth(50);
//        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(11));
//        col.setMaxWidth(70);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
    //end
   // if (countIssue == 0) {
    
        for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
            if (((Boolean) tblDepositInterestApplication.getValueAt(i, 0)).booleanValue()) {
                HashMap map = new HashMap();
                map.put("SCREEN_ID", getScreenID());
                map.put("RECORD_KEY", tblDepositInterestApplication.getValueAt(i, 2));
                map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("CUR_DATE", currDate);
                List lstLock = ClientUtil.executeQuery("selectEditLock", map);
                if (lstLock != null && lstLock.size() > 0) {
                    tblDepositInterestApplication.setValueAt(false, i, 0);
                } else {
                    try{
                    ClientUtil.executeWithExceptionHand("insertEditLock", map);
                    countIssue++;
                    }  
                    catch(SQLException e)
                    {
                     tblDepositInterestApplication.setValueAt(false, i, 0);
                    
                    }
               }
            }
        }
        if(countIssue!=0)
        {
        btnProcess.setEnabled(true);
        }
 //   }
    // TODO add your handling code here:
}//GEN-LAST:event_btnValidateActionPerformed

private void chkExcludeLienDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExcludeLienDepositActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_chkExcludeLienDepositActionPerformed

    private void txtToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToAccountActionPerformed
        String actNum = CommonUtil.convertObjToStr(txtToAccount.getText());
        if (actNum.length() > 0) {
            boolean isExist = false;
            isExist = observable.checkAcNoWithoutProdType(actNum);
            if (!isExist) {
                ClientUtil.showMessageWindow("Invalid Account No");
                txtToAccount.setText("");
            }
        } else {
            ClientUtil.showMessageWindow("Invalid Account No");
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToAccountActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDepositAdd;
    private com.see.truetransact.uicomponent.CButton btnDepositRemove;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnFromPrintTransId;
    private com.see.truetransact.uicomponent.CButton btnManualDepositNo;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProductID;
    private com.see.truetransact.uicomponent.CButton btnRePrint;
    private com.see.truetransact.uicomponent.CButton btnReprintClose;
    private com.see.truetransact.uicomponent.CButton btnSBNo;
    private com.see.truetransact.uicomponent.CButton btnSINumber;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CButton btnToPrintTransId;
    private com.see.truetransact.uicomponent.CButton btnValidate;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboOAProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboSIProductID;
    private com.see.truetransact.uicomponent.CCheckBox chkCash;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeLienDeposit;
    private com.see.truetransact.uicomponent.CCheckBox chkReprintCash;
    private com.see.truetransact.uicomponent.CCheckBox chkReprintTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectManual;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectMaturity;
    private com.see.truetransact.uicomponent.CCheckBox chkTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkViewPrint;
    private com.see.truetransact.uicomponent.CLabel lblCount;
    private com.see.truetransact.uicomponent.CLabel lblCountValue;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblFromTransID;
    private com.see.truetransact.uicomponent.CLabel lblManualDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOAProductID;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblSBNo;
    private com.see.truetransact.uicomponent.CLabel lblSINumber;
    private com.see.truetransact.uicomponent.CLabel lblSIProductID;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll1;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll2;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblToTransID;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CList lstSelectedPriorityTransaction;
    private com.see.truetransact.uicomponent.CPanel panCustID;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panFromAccount;
    private com.see.truetransact.uicomponent.CPanel panFromTonum;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProcessButton;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductIdMain;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panReprint;
    private com.see.truetransact.uicomponent.CPanel panReprintBtn;
    private com.see.truetransact.uicomponent.CPanel panReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panReprintTransType;
    private com.see.truetransact.uicomponent.CPanel panSIDetails;
    private com.see.truetransact.uicomponent.CPanel panSINumber;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panSelectAll1;
    private com.see.truetransact.uicomponent.CPanel panSelectAll2;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToAccount;
    private com.see.truetransact.uicomponent.CPanel panToAccount1;
    private com.see.truetransact.uicomponent.CPanel panToAccount2;
    private com.see.truetransact.uicomponent.CPanel panTransType;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanAdd1;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblDepositInterestApplication;
    private com.see.truetransact.uicomponent.CDateField tdtTransDate;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtFromTransID;
    private com.see.truetransact.uicomponent.CTextField txtLimitAmount;
    private com.see.truetransact.uicomponent.CTextField txtManualDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtSBNo;
    private com.see.truetransact.uicomponent.CTextField txtSINumber;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    private com.see.truetransact.uicomponent.CTextField txtToTransID;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DepositInterestApplicationUI fad = new DepositInterestApplicationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
    
    private boolean interbranchDateCheck(HashMap existingMap,String depNum){
        Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")));
        Date currentDate = (Date) currDate.clone();
        System.out.println("selectedBranchDt : " + selectedBranchDt + " currentDate : " + currentDate);
        StringBuffer interbranchList = new StringBuffer();
        boolean interbranchFlag = false;
        if (selectedBranchDt == null) {
            if (interbranchList.length() == 0) {
                interbranchList.append("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed" + "\n" + "Deposit No : " + depNum);
            } else {
                interbranchList.append("Deposit No : " + depNum);
            }
        } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) != 0) {
            if (interbranchList.length() == 0) {
                interbranchList.append("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed" + "\n" + "Deposit No : " + depNum);
            } else {
                interbranchList.append("\n" + "Deposit No : " + depNum);
            }
        } else {
            System.out.println("Continue for interbranch trasactions ...");
            interbranchFlag = false;
        }        
        if (interbranchList != null && interbranchList.length() > 0) {
            ClientUtil.displayAlert("Please deselect the following deposits, " + "\n" + interbranchList);
            btnProcess.setEnabled(true);
            interbranchFlag = true;
        }
        return interbranchFlag;
    }
}
