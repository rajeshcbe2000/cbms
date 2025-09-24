/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferUI.java
 *
 * Created on August 5, 2003, 3:00 PM
 */
package com.see.truetransact.ui.transaction.transfer;

import com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.ComboBoxModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
//import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.common.viewall.ViewLoansTransUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;    // Added by Rajesh
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

//import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.ui.transaction.common.TransHOCommonUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.SuspiciousAccountValidation;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.*;
import javax.swing.*;
import com.see.truetransact.ui.customer.SmartCustomerUI;


/**
 * For shwoing the main interface for the transactions which are for transfer
 *
 * @author Pranav @modified Pinky @modified Bala
 * 
 */
public class TransferUI extends CInternalFrame implements Observer, UIMandatoryField {

    public GLAccountNumberListUI glAccountNumberListUI;
    private java.util.ResourceBundle resourceBundle;
    private TransferOB observable;
    private HashMap authorizationCheckMap = new HashMap();
    private HashMap mandatoryMap;
    private TransferMRB objMandatoryRB;
    private ActionPopupUI actionPopUp;
    private String transactionIdForEdit;
    private String batchIdForEdit;
    private Date transactionDateForEdit;
    private Date backDatedTransDate;
    private String transactionInitBranForEdit;
    private int rowForEdit;
    private int viewType;
    private boolean _intTransferNew;
    RejectionApproveUI rejectionApproveUI=null;
    private TransDetailsUI transDetails = null;
    private TransCommonUI transCommonUI = null;
    private TransHOCommonUI transHOCommonUI = null;
    TermDepositUI termDepositUI;
    private String prodType = "";
    private HashMap loanDebitType = new HashMap();
    private int rowCount = 0;
    //CONSTATNTS
    final int ACT_NUM = 200;
    final int AUTHORIZE = 201;
    final int ACCTHDID = 202;
    final int RECON = 203;
    final int VIEW = 205;
    private CTextField txtDepositAmount;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepLink = false;
    private boolean afterSaveCancel = false;
    private HashMap transferDepMap = new HashMap();
    private HashMap intMap = new HashMap();
    private String depositNo = "";
    private boolean isRowClicked = false;
    //validate more than one credit
    private boolean loanAsanWhen = false;
    private boolean reconcilebtnDisable = false;
    private boolean reconcileScreen = false;
    private boolean morethanOneDeposit = false;
    private boolean alreadyExistDeposit = false;
    private boolean recurringCredit = false;
    private Date curr_dt = null;
    private String depBehavesLike = "";
    private String depPartialWithDrawalAllowed = "";
    List chqBalList = null;
    boolean fromAuthorizeUI = false,fromAuthListDebit=false,fromAuthListCredit=false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI authorizeListDebitUI=null;
    AuthorizeListCreditUI authorizeListCreditUI=null;
    private HashMap asAndWhenMap = null;
    private double orgOrRespAmout = 0.0;
    private String orgOrRespBranchId = "";
    private String orgOrRespBranchName = "";
    private String orgBranch = "";
    private String orgBranchName = "";
    private String orgOrRespCategory = "";
    private String orgOrRespAdviceNo = "";
    private java.util.Date orgOrRespAdviceDt = null;
    private String orgOrRespTransType = "";
    private String orgOrRespDetails = "";
    ViewOrgOrRespUI vieworgOrRespUI = null;
    private long noofInstallment2 = 0;
    ViewRespUI viewRespUI = null;
    private int rejectFlag = 0;
    public String accNo1 = "";
    public String prodIdforgl = "";
    public String prodtypeforgl = "";
    private int flagv = 0;
    public int flagDr = 0;
    public int flagCr = 0;
    double penal = 0.0;
    double totAmt1 = 0.0;
    public boolean flagGLAcc = false;
    private double actInterest=0;
    HashMap serviceTax_Map=new HashMap();
    HashMap glserviceTax_Map=new HashMap();
    ServiceTaxCalculation objServiceTax;
   //added by rishad for listing waive detales and enter waive in this grid
    private EditWaiveTableUI editableWaiveUI = null;
    HashMap returnWaiveMap = null;
    HashMap allWaiveMap = new HashMap();
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private int bulkRow = -1; //Added By Revathi
    private boolean bulkProcess = false; //Added By Revathi
    
    //HashMap lockFreeAccount;
    /**
     * Creates new form TransferUI
     */
    public TransferUI() {
        // first generate the controls
        curr_dt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
        chkVerifyAll.setEnabled(false);
        lblTokenNo.setVisible(false);
        txtTokenNo.setVisible(false);
        //txtTokenNo.setText("0");
        //        lblCurCaption.setVisible(false);
        this.cboCurrency.setVisible(false);
        btnViewTermLoanDetails.setVisible(true);
        btnViewTermLoanDetails.setEnabled(true);
        btnWaive.setVisible(true);
        btnWaive.setEnabled(false);
    }

    private void initStartup() {
        // then set the names for the controls using setName()
        setFieldNames();

        transDetails = new TransDetailsUI(panInfoPanel);
//        HashMap paramMap = new HashMap();
//        transCommonUI = new TransCommonUI(paramMap);
        /*
         * call the intenationalize() method to load the RB values and
         * initialize the Observable for this class
         */
        internationalize();
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panFieldPanel, getMandatoryHashMap());

        /*
         * Fill up all the combo boxes and set up the initial values for the
         * radio buttons
         */
        initComponentData();
        btnReconsile.setVisible(false);

        // Disable all the screen
        ClientUtil.enableDisable(this, false);

        txtAccountHeadValue.setEditable(false);
        /*
         * now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        enableDisableButtons(false);
        setupMenuToolBarPanel();
        this.resetUIData();
        btnView.setEnabled(!btnView.isEnabled());
        setEnableDisableBulkTransaction(false);
        btnOrgOrResp.setVisible(false);
    }

    private void enableDisableButtons(boolean value) {
        this.btnAccountNo.setEnabled(value);
        this.btnAddCredit.setEnabled(value);
        this.btnAddDebit.setEnabled(value);
        this.btnTransDelete.setEnabled(value);
        this.btnTransSave.setEnabled(value);
        this.btnAccountHead.setEnabled(value);
        this.btnReconsile.setEnabled(value);
    }

    private void setMaxLength() {
        txtAccountNo.setMaxLength(16);
        txtAccountNo.setAllowAll(true);
        //        txtTokenNo.setMaxLength(16);
        txtInstrumentNo1.setAllowNumber(true);
        txtInstrumentNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtInstrumentNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtParticulars.setMaxLength(100);
        txtParticulars.setAllowAll(true);
        txtNarration.setMaxLength(128);
        txtNarration.setAllowAll(true);
        txtAccountHeadValue.setAllowAll(true);
        txtAmount.setValidation(new CurrencyValidation(13, 2));
    }

    public void setHelpMessage() {
        objMandatoryRB = new TransferMRB();
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
        txtNarration.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNarration"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        //txtTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenNo"));
        txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        txtAccountNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNo"));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("txtNarration", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        //mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtAccountNo", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void checkDocumentDetails() {
        String AccNo = txtAccountNo.getText();
        List lst = null;
        if (AccNo.lastIndexOf('_') != -1) {
            AccNo = txtAccountNo.getText().substring(0, txtAccountNo.getText().lastIndexOf("_"));

        }
        lst = observable.getDocumentDetail("getSelectTermLoanDocumentTO", AccNo);
        String str = "";
        String doc_form_no = null;
        String is_submited = null;
        HashMap hash = new HashMap();
        for (int i = 0; i < lst.size(); i++) {
            hash = (HashMap) lst.get(i);
            is_submited = (String) hash.get("IS_SUBMITTED") == null ? "N" : (String) hash.get("IS_SUBMITTED");
            doc_form_no = (String) hash.get("DOC_DESC");
            if (is_submited.length() >= 1) {
                if (!is_submited.equals("Y")) {
                    str = str + doc_form_no + "\n";
                }
            }
        }
        if (str.length() >= 1) {
            ClientUtil.displayAlert(str + "notsubmited");
        }
    }

    private void setObservable() {
        try {
            observable = new TransferOB();
            observable.addObserver(this);
            observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMenuToolBarPanel() {
        // setup toolbar
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_CANCEL
                || observable.getOperation() == ClientConstants.ACTIONTYPE_FAILED) {
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);

            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
            btnRejection.setEnabled(true);

            mitAdd.setEnabled(true);
            mitEdit.setEnabled(true);
            mitDelete.setEnabled(true);
        } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);

            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnRejection.setEnabled(false);

            mitAdd.setEnabled(false);
            mitEdit.setEnabled(false);
            mitDelete.setEnabled(false);
            btnView.setEnabled(false);

            this.btnAddDebit.setEnabled(true);
        }
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnRejection.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            mitAuthorize.setEnabled(btnAuthorize.isEnabled());
            mitExceptions.setEnabled(btnException.isEnabled());
            mitRejection.setEnabled(btnRejection.isEnabled());
        }
    }

    private void setUpTitle() {
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_CANCEL
                && observable.getOperation() != ClientConstants.ACTIONTYPE_FAILED
                && (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            this.setTitle(resourceBundle.getString("TITLE_TRANSFERTYPE") + observable.getTransType());
        } else {
            this.setTitle(resourceBundle.getString("TITLE"));
        }
    }
    /*
     * Fill up all the combo boxes and set up the initial values for the radio
     * buttons
     */

    private void initComponentData() {

        // fill all the combo boxes, with the data from lookup table, and others

        cboProductID.setModel(observable.getProductTypeModel());
        cboInstrumentType.setModel(observable.getInstrumentTypeModel());
        cboCurrency.setModel(observable.getCurrencyTypeModel());

        tblTransList.setModel(observable.getTbmTransfer());

        cboProductType.setModel(observable.getMainProductTypeModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bulkTransactionGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTransfer = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSep4 = new com.see.truetransact.uicomponent.CLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblTBSep1 = new javax.swing.JLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSep5 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnRejection = new com.see.truetransact.uicomponent.CButton();
        lblSep3 = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panFieldPanel = new com.see.truetransact.uicomponent.CPanel();
        panTransDetail = new com.see.truetransact.uicomponent.CPanel();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        panInstrumentNo = new com.see.truetransact.uicomponent.CPanel();
        txtInstrumentNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        panTransButtons = new com.see.truetransact.uicomponent.CPanel();
        btnAddCredit = new com.see.truetransact.uicomponent.CButton();
        btnAddDebit = new com.see.truetransact.uicomponent.CButton();
        btnTransSave = new com.see.truetransact.uicomponent.CButton();
        btnTransDelete = new com.see.truetransact.uicomponent.CButton();
        btnViewTermLoanDetails = new com.see.truetransact.uicomponent.CButton();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        btnReconsile = new com.see.truetransact.uicomponent.CButton();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        btnOrgOrResp = new com.see.truetransact.uicomponent.CButton();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        btnWaive = new com.see.truetransact.uicomponent.CButton();
        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransDt = new com.see.truetransact.uicomponent.CLabel();
        lblTransDtValue = new com.see.truetransact.uicomponent.CLabel();
        lblValueDt = new com.see.truetransact.uicomponent.CLabel();
        tdtValueDt = new com.see.truetransact.uicomponent.CDateField();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionIDValue = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionID = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblCreatedBy = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedByValue = new com.see.truetransact.uicomponent.CLabel();
        panAccountHeadValue = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHeadValue = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblAccountHeadDescValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblAuthBy = new com.see.truetransact.uicomponent.CLabel();
        lblAuthByValue = new com.see.truetransact.uicomponent.CLabel();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblBulkTransaction = new com.see.truetransact.uicomponent.CLabel();
        panBulkTransaction = new com.see.truetransact.uicomponent.CPanel();
        rdoBulkTransaction_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBulkTransaction_No = new com.see.truetransact.uicomponent.CRadioButton();
        panGlAccNo = new com.see.truetransact.uicomponent.CPanel();
        lblAccNoGl = new com.see.truetransact.uicomponent.CLabel();
        lblLinkId = new com.see.truetransact.uicomponent.CLabel();
        lblBatchIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblBatchID = new com.see.truetransact.uicomponent.CLabel();
        panMainTrans = new com.see.truetransact.uicomponent.CPanel();
        panInfoPanel = new com.see.truetransact.uicomponent.CPanel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        lblTtlCrInstr = new com.see.truetransact.uicomponent.CLabel();
        lblTtlCrInstrValue = new com.see.truetransact.uicomponent.CLabel();
        lblTtlCrAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTtlCrAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTtlDrInstr = new com.see.truetransact.uicomponent.CLabel();
        lblTtlDrInstrValue = new com.see.truetransact.uicomponent.CLabel();
        lblTtlDrAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTtlDrAmountValue = new com.see.truetransact.uicomponent.CLabel();
        panTransInfo = new com.see.truetransact.uicomponent.CPanel();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransList = new com.see.truetransact.uicomponent.CTable();
        chkVerifyAll = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblStatusValue = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTransfer = new javax.swing.JMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitAdd = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess1 = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitRejection = new javax.swing.JMenuItem();
        mitExceptions = new javax.swing.JMenuItem();
        sptProcess2 = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(820, 665));
        setName("frmTransferFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(820, 665));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnView);

        lblSep4.setToolTipText("");
        lblSep4.setMaximumSize(new java.awt.Dimension(15, 15));
        lblSep4.setMinimumSize(new java.awt.Dimension(15, 15));
        lblSep4.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblSep4);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAdd.setToolTipText("New");
        btnAdd.setFocusable(false);
        btnAdd.setName("btnAdd"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAdd);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setFocusable(false);
        btnEdit.setName("btnEdit"); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setFocusable(false);
        btnDelete.setName("btnDelete"); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnDelete);

        lblTBSep1.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblTBSep1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnCancel);

        lblSep5.setToolTipText("");
        lblSep5.setMaximumSize(new java.awt.Dimension(15, 15));
        lblSep5.setMinimumSize(new java.awt.Dimension(15, 15));
        lblSep5.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblSep5);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setFocusable(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace21);

        btnRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnRejection.setFocusable(false);
        btnRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectionActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnRejection);

        lblSep3.setToolTipText("");
        lblSep3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblSep3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblSep3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblSep3);

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnReport.setFocusable(false);
        btnReport.setName("btnReport"); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnReport);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setFocusable(false);
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnClose);

        getContentPane().add(tbrTransfer, java.awt.BorderLayout.NORTH);

        panFieldPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panFieldPanel.setMinimumSize(new java.awt.Dimension(700, 575));
        panFieldPanel.setPreferredSize(new java.awt.Dimension(700, 575));
        panFieldPanel.setLayout(new java.awt.GridBagLayout());

        panTransDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panTransDetail.setMaximumSize(new java.awt.Dimension(495, 115));
        panTransDetail.setMinimumSize(new java.awt.Dimension(495, 115));
        panTransDetail.setName("panTransDetail"); // NOI18N
        panTransDetail.setPreferredSize(new java.awt.Dimension(495, 115));
        panTransDetail.setLayout(new java.awt.GridBagLayout());

        lblInstrumentNo.setText("Instrument No.");
        lblInstrumentNo.setName("lblInstrumentNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(lblInstrumentNo, gridBagConstraints);

        lblInstrumentDate.setText("Instrument Date");
        lblInstrumentDate.setMaximumSize(new java.awt.Dimension(95, 18));
        lblInstrumentDate.setMinimumSize(new java.awt.Dimension(125, 18));
        lblInstrumentDate.setName("lblInstrumentDate"); // NOI18N
        lblInstrumentDate.setPreferredSize(new java.awt.Dimension(95, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(lblInstrumentDate, gridBagConstraints);

        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(tdtInstrumentDate, gridBagConstraints);

        lblAmount.setText("Amount");
        lblAmount.setName("lblAmount"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(lblAmount, gridBagConstraints);

        lblParticulars.setText("Particulars");
        lblParticulars.setName("lblParticulars"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(lblParticulars, gridBagConstraints);

        txtParticulars.setMinimumSize(new java.awt.Dimension(225, 21));
        txtParticulars.setName("txtParticulars"); // NOI18N
        txtParticulars.setPreferredSize(new java.awt.Dimension(225, 21));
        txtParticulars.setMaxLength(128);
        txtParticulars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParticularsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetail.add(txtParticulars, gridBagConstraints);

        lblInstrumentType.setText("Instrument Type");
        lblInstrumentType.setMinimumSize(new java.awt.Dimension(133, 18));
        lblInstrumentType.setName("lblInstrumentType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(lblInstrumentType, gridBagConstraints);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstrumentType.setName("cboCurrency"); // NOI18N
        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        cboInstrumentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInstrumentTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(cboInstrumentType, gridBagConstraints);

        lblTokenNo.setText("Token No.");
        lblTokenNo.setName("lblTokenNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTransDetail.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTokenNo.setName("txtTokenNo"); // NOI18N
        txtTokenNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtTokenNo.setMaxLength(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(txtTokenNo, gridBagConstraints);

        panInstrumentNo.setLayout(new java.awt.GridBagLayout());

        txtInstrumentNo1.setMinimumSize(new java.awt.Dimension(75, 21));
        txtInstrumentNo1.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panInstrumentNo.add(txtInstrumentNo1, gridBagConstraints);

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(125, 21));
        txtInstrumentNo2.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInstrumentNo.add(txtInstrumentNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(panInstrumentNo, gridBagConstraints);

        panTransButtons.setMaximumSize(new java.awt.Dimension(324, 30));
        panTransButtons.setMinimumSize(new java.awt.Dimension(324, 30));
        panTransButtons.setPreferredSize(new java.awt.Dimension(324, 30));
        panTransButtons.setLayout(new java.awt.GridBagLayout());

        btnAddCredit.setText("New Credit");
        btnAddCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCreditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransButtons.add(btnAddCredit, gridBagConstraints);

        btnAddDebit.setText("New Debit");
        btnAddDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransButtons.add(btnAddDebit, gridBagConstraints);

        btnTransSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnTransSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransSaveActionPerformed(evt);
            }
        });
        btnTransSave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnTransSaveFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransButtons.add(btnTransSave, gridBagConstraints);

        btnTransDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnTransDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransButtons.add(btnTransDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(panTransButtons, gridBagConstraints);

        btnViewTermLoanDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewTermLoanDetails.setText("View");
        btnViewTermLoanDetails.setMaximumSize(new java.awt.Dimension(82, 27));
        btnViewTermLoanDetails.setMinimumSize(new java.awt.Dimension(82, 27));
        btnViewTermLoanDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTermLoanDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTransDetail.add(btnViewTermLoanDetails, gridBagConstraints);

        cboCurrency.setName("cboCurrency"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTransDetail.add(cboCurrency, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation(14,2));
        txtAmount.setMaxLength(16);
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransDetail.add(txtAmount, gridBagConstraints);

        btnReconsile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReconsile.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReconsile.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReconsile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconsileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTransDetail.add(btnReconsile, gridBagConstraints);

        lblNarration.setText("Mem Name/Narration");
        lblNarration.setName("lblParticulars"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTransDetail.add(lblNarration, gridBagConstraints);

        txtNarration.setMinimumSize(new java.awt.Dimension(225, 21));
        txtNarration.setName("txtParticulars"); // NOI18N
        txtNarration.setPreferredSize(new java.awt.Dimension(225, 21));
        txtParticulars.setMaxLength(128);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panTransDetail.add(txtNarration, gridBagConstraints);

        btnOrgOrResp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOrgOrResp.setText("View");
        btnOrgOrResp.setMaximumSize(new java.awt.Dimension(82, 27));
        btnOrgOrResp.setMinimumSize(new java.awt.Dimension(82, 27));
        btnOrgOrResp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrgOrRespActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        panTransDetail.add(btnOrgOrResp, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panTransDetail.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(100, 18));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(100, 18));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panTransDetail.add(lblServiceTaxval, gridBagConstraints);

        btnWaive.setText("Waive");
        btnWaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        panTransDetail.add(btnWaive, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFieldPanel.add(panTransDetail, gridBagConstraints);

        panTransDetails.setMaximumSize(new java.awt.Dimension(400, 200));
        panTransDetails.setMinimumSize(new java.awt.Dimension(400, 200));
        panTransDetails.setPreferredSize(new java.awt.Dimension(400, 200));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        lblTransDt.setText("Transaction Date");
        lblTransDt.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 3, 3);
        panTransDetails.add(lblTransDt, gridBagConstraints);

        lblTransDtValue.setText("[xxxxxxxxxx]");
        lblTransDtValue.setName("lblBatchIDValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 3, 3);
        panTransDetails.add(lblTransDtValue, gridBagConstraints);

        lblValueDt.setText("Value Date");
        lblValueDt.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(lblValueDt, gridBagConstraints);

        tdtValueDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtValueDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(tdtValueDt, gridBagConstraints);

        lblAccountNo.setText("Account No.");
        lblAccountNo.setName("lblAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        panTransDetails.add(lblAccountNo, gridBagConstraints);

        lblTransactionIDValue.setText("[xxxxxxxxxx]");
        lblTransactionIDValue.setName("lblTransactionIDValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panTransDetails.add(lblTransactionIDValue, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setName("cboProductID"); // NOI18N
        cboProductID.setPopupWidth(250);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(cboProductID, gridBagConstraints);

        lblProductID.setText("Account Product");
        lblProductID.setName("lblProductID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(lblProductID, gridBagConstraints);

        lblTransactionID.setText("Transaction ID");
        lblTransactionID.setName("lblTransactionID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panTransDetails.add(lblTransactionID, gridBagConstraints);

        lblAccountHead.setText("Account Head ID");
        lblAccountHead.setName("lblAccountHead"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(lblAccountHead, gridBagConstraints);

        panAccountNo.setMinimumSize(new java.awt.Dimension(150, 33));
        panAccountNo.setPreferredSize(new java.awt.Dimension(150, 33));
        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtAccountNo.setName("txtAccountNo"); // NOI18N
        txtAccountNo.setPreferredSize(new java.awt.Dimension(110, 21));
        txtAccountNo.setMaxLength(10);
        txtAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNoActionPerformed(evt);
            }
        });
        txtAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(txtAccountNo, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setMaximumSize(new java.awt.Dimension(25, 25));
        btnAccountNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountNo.setName("btnAccountNo"); // NOI18N
        btnAccountNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountNo.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransDetails.add(panAccountNo, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(lblProductType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(130);
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(cboProductType, gridBagConstraints);

        lblCreatedBy.setText("Created By");
        lblCreatedBy.setMaximumSize(new java.awt.Dimension(86, 16));
        lblCreatedBy.setMinimumSize(new java.awt.Dimension(86, 16));
        lblCreatedBy.setPreferredSize(new java.awt.Dimension(86, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetails.add(lblCreatedBy, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetails.add(lblCreatedByValue, gridBagConstraints);

        panAccountHeadValue.setLayout(new java.awt.GridBagLayout());

        txtAccountHeadValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHeadValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountHeadValueActionPerformed(evt);
            }
        });
        txtAccountHeadValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountHeadValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadValue.add(txtAccountHeadValue, gridBagConstraints);

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadValue.add(btnAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(panAccountHeadValue, gridBagConstraints);

        lblAccountHeadDescValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDescValue.setMaximumSize(new java.awt.Dimension(500, 21));
        lblAccountHeadDescValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccountHeadDescValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransDetails.add(lblAccountHeadDescValue, gridBagConstraints);

        lblCustNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustNameValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCustNameValue.setName("lblCustNameValue"); // NOI18N
        lblCustNameValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransDetails.add(lblCustNameValue, gridBagConstraints);

        lblAuthBy.setText("Authorize By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetails.add(lblAuthBy, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetails.add(lblAuthByValue, gridBagConstraints);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblHouseName.setName("lblCustNameValue"); // NOI18N
        lblHouseName.setPreferredSize(new java.awt.Dimension(100, 21));
        lblHouseName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblHouseNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransDetails.add(lblHouseName, gridBagConstraints);

        lblBulkTransaction.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBulkTransaction.setText("Bulk Transaction UpLoad");
        lblBulkTransaction.setMaximumSize(new java.awt.Dimension(200, 18));
        lblBulkTransaction.setMinimumSize(new java.awt.Dimension(200, 18));
        lblBulkTransaction.setName("lblTransactionID"); // NOI18N
        lblBulkTransaction.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panTransDetails.add(lblBulkTransaction, gridBagConstraints);

        panBulkTransaction.setMaximumSize(new java.awt.Dimension(50, 23));
        panBulkTransaction.setMinimumSize(new java.awt.Dimension(50, 23));
        panBulkTransaction.setPreferredSize(new java.awt.Dimension(50, 23));
        panBulkTransaction.setLayout(new java.awt.GridBagLayout());

        bulkTransactionGroup.add(rdoBulkTransaction_Yes);
        rdoBulkTransaction_Yes.setText("Yes");
        rdoBulkTransaction_Yes.setMaximumSize(new java.awt.Dimension(29, 27));
        rdoBulkTransaction_Yes.setPreferredSize(new java.awt.Dimension(29, 27));
        rdoBulkTransaction_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBulkTransaction_YesActionPerformed(evt);
            }
        });
        panBulkTransaction.add(rdoBulkTransaction_Yes, new java.awt.GridBagConstraints());

        bulkTransactionGroup.add(rdoBulkTransaction_No);
        rdoBulkTransaction_No.setText("No");
        rdoBulkTransaction_No.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoBulkTransaction_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBulkTransaction_NoActionPerformed(evt);
            }
        });
        panBulkTransaction.add(rdoBulkTransaction_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panTransDetails.add(panBulkTransaction, gridBagConstraints);

        panGlAccNo.setMaximumSize(new java.awt.Dimension(360, 21));
        panGlAccNo.setMinimumSize(new java.awt.Dimension(360, 21));
        panGlAccNo.setPreferredSize(new java.awt.Dimension(360, 21));
        panGlAccNo.setLayout(new java.awt.GridBagLayout());

        lblAccNoGl.setText(accNo1);
        lblAccNoGl.setMaximumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setMinimumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panGlAccNo.add(lblAccNoGl, gridBagConstraints);

        lblLinkId.setText("Link Batch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panGlAccNo.add(lblLinkId, gridBagConstraints);

        lblBatchIDValue.setText("[xxxxxxxxxx]");
        lblBatchIDValue.setName("lblBatchIDValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panGlAccNo.add(lblBatchIDValue, gridBagConstraints);

        lblBatchID.setText("Batch ID");
        lblBatchID.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panGlAccNo.add(lblBatchID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        panTransDetails.add(panGlAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFieldPanel.add(panTransDetails, gridBagConstraints);

        panMainTrans.setMinimumSize(new java.awt.Dimension(440, 421));
        panMainTrans.setPreferredSize(new java.awt.Dimension(440, 421));
        panMainTrans.setLayout(new java.awt.GridBagLayout());

        panInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info"));
        panInfoPanel.setMinimumSize(new java.awt.Dimension(200, 225));
        panInfoPanel.setName("panInfoPanel"); // NOI18N
        panInfoPanel.setPreferredSize(new java.awt.Dimension(200, 225));
        panInfoPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMainTrans.add(panInfoPanel, gridBagConstraints);

        panTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTrans.setMinimumSize(new java.awt.Dimension(250, 46));
        panTrans.setPreferredSize(new java.awt.Dimension(250, 46));
        panTrans.setLayout(new java.awt.GridBagLayout());

        lblTtlCrInstr.setText("Credit Instruments");
        lblTtlCrInstr.setName("lblTtlCrInstr"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panTrans.add(lblTtlCrInstr, gridBagConstraints);

        lblTtlCrInstrValue.setText("[xxxxxxxxxx]");
        lblTtlCrInstrValue.setName("lblTtlCrInstrValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panTrans.add(lblTtlCrInstrValue, gridBagConstraints);

        lblTtlCrAmount.setText("Credit Amount");
        lblTtlCrAmount.setName("lblTtlCrAmount"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panTrans.add(lblTtlCrAmount, gridBagConstraints);

        lblTtlCrAmountValue.setText("[xxxxxxxxxx]");
        lblTtlCrAmountValue.setName("lblTtlCrAmountValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panTrans.add(lblTtlCrAmountValue, gridBagConstraints);

        lblTtlDrInstr.setText("Debit Instruments");
        lblTtlDrInstr.setName("lblTtlDrInstr"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panTrans.add(lblTtlDrInstr, gridBagConstraints);

        lblTtlDrInstrValue.setText("[xxxxxxxxxx]");
        lblTtlDrInstrValue.setName("lblTtlDrInstrValue"); // NOI18N
        lblTtlDrInstrValue.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panTrans.add(lblTtlDrInstrValue, gridBagConstraints);

        lblTtlDrAmount.setText("Debit Amount");
        lblTtlDrAmount.setName("lblTtlDrAmount"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panTrans.add(lblTtlDrAmount, gridBagConstraints);

        lblTtlDrAmountValue.setText("[xxxxxxxxxx]");
        lblTtlDrAmountValue.setName("lblTtlDrAmountValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panTrans.add(lblTtlDrAmountValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMainTrans.add(panTrans, gridBagConstraints);

        panTransInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Info"));
        panTransInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panTransInfo.setName("panTransInfo"); // NOI18N
        panTransInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panTransInfo.setLayout(new java.awt.GridBagLayout());

        srpTransDetails.setNextFocusableComponent(btnAuthorize);

        tblTransList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTransList.setNextFocusableComponent(btnAuthorize);
        tblTransList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransListMouseClicked(evt);
            }
        });
        tblTransList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTransListKeyPressed(evt);
            }
        });
        srpTransDetails.setViewportView(tblTransList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 321;
        gridBagConstraints.ipady = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 17, 16);
        panTransInfo.add(srpTransDetails, gridBagConstraints);

        chkVerifyAll.setText("Verify All");
        chkVerifyAll.setMaximumSize(new java.awt.Dimension(300, 24));
        chkVerifyAll.setMinimumSize(new java.awt.Dimension(300, 24));
        chkVerifyAll.setPreferredSize(new java.awt.Dimension(300, 24));
        chkVerifyAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVerifyAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -209;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 260, 0, 16);
        panTransInfo.add(chkVerifyAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMainTrans.add(panTransInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panFieldPanel.add(panMainTrans, gridBagConstraints);

        getContentPane().add(panFieldPanel, java.awt.BorderLayout.CENTER);

        panStatus.setPreferredSize(new java.awt.Dimension(65, 19));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setText("Status :");
        lblStatus.setName("lblStatus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStatus.add(lblStatus, gridBagConstraints);

        lblStatusValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatusValue.setMaximumSize(new java.awt.Dimension(70, 20));
        lblStatusValue.setMinimumSize(new java.awt.Dimension(70, 20));
        lblStatusValue.setName("lblStatusValue"); // NOI18N
        lblStatusValue.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatusValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitAdd.setText("Add");
        mitAdd.setName("mitAdd"); // NOI18N
        mitAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAddActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAdd);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess1);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setName("mitAuthorize"); // NOI18N
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitRejection.setText("Rejection");
        mitRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitRejection);

        mitExceptions.setText("Exceptions");
        mitExceptions.setName("mitExceptions"); // NOI18N
        mitExceptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionsActionPerformed(evt);
            }
        });
        mnuProcess.add(mitExceptions);
        mnuProcess.add(sptProcess2);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTransfer.add(mnuProcess);

        setJMenuBar(mbrTransfer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAccountHeadValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountHeadValueActionPerformed
        // TODO add your handling code here:
       // txtAccountHeadValueActionPerformed();
//        lblAccNoGl.setText("");
//        viewType = ACCTHDID;
//        HashMap viewMap = new HashMap();
//        viewMap.put("BALANCE_TYPE", observable.getTransType());
//        viewMap.put("AC_HD_ID", txtAccountHeadValue.getText());
//        List lst = ClientUtil.executeQuery("getACHeadDetails", viewMap);
//        if (lst != null && lst.size() > 0) {
//            viewMap = (HashMap) lst.get(0);
//            fillData(viewMap);
//        } else {
//            ClientUtil.noDataAlert();
//            txtAccountHeadValue.setText("");
//        }
    }//GEN-LAST:event_txtAccountHeadValueActionPerformed
private void txtAccountHeadValueActionPerformed()
{
    lblAccNoGl.setText("");
        viewType = ACCTHDID;
        HashMap viewMap = new HashMap();
        viewMap.put("BALANCE_TYPE", observable.getTransType());
        viewMap.put("AC_HD_ID", txtAccountHeadValue.getText());
        List lst = ClientUtil.executeQuery("getACHeadDetails", viewMap);
        if (lst != null && lst.size() > 0) {
            viewMap = (HashMap) lst.get(0);
            fillData(viewMap);
        } else {
            ClientUtil.noDataAlert();
            txtAccountHeadValue.setText("");
        } 
}
    private void rdoBulkTransaction_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBulkTransaction_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoBulkTransaction_NoActionPerformed

    private void rdoBulkTransaction_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBulkTransaction_YesActionPerformed
        // TODO add your handling code here:
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            updateOBFields();
            if (observable.insertTransferData(-1) == 1) {
                return;
            }
            this.updateTable();
            updateTransInfo();
            panTransferEnableDisable(false);

        }
    }//GEN-LAST:event_rdoBulkTransaction_YesActionPerformed

    private void lblHouseNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblHouseNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblHouseNameFocusLost

    private void btnViewTermLoanDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTermLoanDetailsActionPerformed
        // TODO add your handling code here:
        prodType=CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (prodType.equals("TL")&& observable.getTransType().equals(CommonConstants.CREDIT) && observable.getOperation()== ClientConstants.ACTIONTYPE_NEW){

                        if (!observable.getInstalType().isEmpty()&&!observable.getInstalType().equals("")&&!observable.getInstalType().equals(null)&&observable.getInstalType().equals("EMI") && observable.getEMIinSimpleInterest().equalsIgnoreCase("N")) {
                             double totalEMIAmt = setEMIAmount();
                             txtAmount.setText(String.valueOf(totalEMIAmt)); //// Added by nithya on 30-04-2021 for KD-2801
                             observable.setTransferAmount(String.valueOf(totalEMIAmt));
                            if (txtAmount.getText().length() > 0) {
                                totalEMIAmt = setEMILoanWaiveAmount();
                            }
                            txtAmount.setText(String.valueOf(totalEMIAmt));
                            observable.setTransferAmount(String.valueOf(totalEMIAmt));
                    }
             }
        boolean penalDepositFlag = false;
        ArrayList termLoanDetails = new ArrayList();
        HashMap termLoansDetailsMap = new HashMap();
         double totWaiveAmt = 0;
        double penalAmt = CommonUtil.convertObjToDouble(observable.getDepositPenalAmt()).doubleValue();
        if (penalAmt > 0) {
            penalDepositFlag = true;
        } else {
            penalDepositFlag = false;
        }
        termLoanDetails = transDetails.getTblDataArrayList();
        termLoansDetailsMap.put("DATA", termLoanDetails);
        //System.out.println("tableVal" + termLoanDetails);
        termLoansDetailsMap.put("WAIVEDATA", returnWaiveMap);
         if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
            totWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("Total_WaiveAmt"));
        }
        double paidAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
        if (totWaiveAmt > 0) {
            paidAmt += totWaiveAmt;
        }
        String transViewAmount = "";
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            double tblSumAmt = 0.0;
            for (int i = 0; i < tblTransList.getRowCount(); i++) {
                String tblCredit = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 4));
                if (tblCredit.equalsIgnoreCase(CommonConstants.CREDIT)) {
                    double tblAmt = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                    tblSumAmt += tblAmt;
                    transViewAmount = "";
                    transViewAmount = String.valueOf(tblSumAmt);
                }
            }
        } else {
            transViewAmount = CommonUtil.convertObjToStr(txtAmount.getText());
        }

        String transType = CommonUtil.convertObjToStr(observable.getTransType());
        boolean showDueTable = true;
        if ((prodType.equals("AD") || prodType.equals("TL")) && transType.equals(CommonConstants.CREDIT)) {
            showDueTable = true;
            termLoansDetailsMap.put("LOAN_ACTNUM_FROM_TRANSFER_SCREEN",txtAccountNo.getText());
        } else {
            showDueTable = false;
        }
        //new ViewLoansTransUI(termLoansDetailsMap,transViewAmount,transType,showDueTable,penalDepositFlag).show();
        new ViewLoansTransUI(termLoansDetailsMap, transViewAmount, transType, showDueTable, penalDepositFlag, observable.isPenalWaiveOff()).show();
    }//GEN-LAST:event_btnViewTermLoanDetailsActionPerformed

    private void tdtValueDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtValueDtFocusLost
        // TODO add your handling code here:
        observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
    }//GEN-LAST:event_tdtValueDtFocusLost
    //Added by chithra for mantis:10345: New Weekly Deposit Schemes For Pudukad SCB 
    private void chkWeeklyDepositReceiptDt(String accNum) {
        if (accNum.length() > 0 && depBehavesLike != null && depBehavesLike.equalsIgnoreCase("DAILY")) {
            HashMap prodMap = new HashMap();
            String depNo = accNum;
            if (depNo != null && depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            prodMap.put("ACCT_NO", depNo);
            List lst = ClientUtil.executeQuery("getMaxDayEndDT", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap != null && prodMap.size() > 0) {
                    String depFre = CommonUtil.convertObjToStr(prodMap.get("DAY_END_DT"));
                    if (depFre != null && !depFre.equals("")) {
                        Calendar a = new GregorianCalendar(2002, 1, 22);
                        a.setTime(DateUtil.getDateMMDDYYYY(depFre));
                        Calendar b = new GregorianCalendar(2015, 0, 12);
                        b.setTime((Date)curr_dt.clone());
                        int weeks = b.get(Calendar.WEEK_OF_YEAR) - a.get(Calendar.WEEK_OF_YEAR);
                        if (weeks <= 0) {
                            ClientUtil.showAlertWindow("Current week Receipt is already done!!");
                            txtAccountNo.setText("");
                            return;
                        }
                    }
                }
            }
        }
    }
    private void txtAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNoFocusLost
        // TODO add your handling code here:
        //        txtAccountNoActionPerformed(null);
           returnWaiveMap = null;
       if (txtAccountNo.getText().length() > 0 && cboProductType.getSelectedIndex() != 1) {
            if (!txtAccountNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                 transDetails.setRepayData(null);
                //System.out.println("txtAccountNo.getText()" + txtAccountNo.getText());
                HashMap hash = new HashMap();
                prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
                //System.out.println("prodType==" + prodType);
                String ACCOUNTNO = txtAccountNo.getText();
                if ((!(CommonUtil.convertObjToStr(prodType).length() > 0)) && ACCOUNTNO.length() > 0) {
                    //System.out.println("hiiii");
                    if (observable.checkAcNoWithoutProdType(ACCOUNTNO, false)) {
                        //System.out.println("txtAcNo" + txtAccountNo.getText() + "_obAcno" + observable.getAccountNo());
                        txtAccountNo.setText(observable.getAccountNo());
                        ACCOUNTNO = txtAccountNo.getText();
                        cboProductID.setModel(observable.getProductTypeModel());
                        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
                        setSelectedBranchID(observable.getSelectedBranchID());
                    } else {
                        ClientUtil.showAlertWindow("Invalid Account No.");
                        txtAccountNo.setText("");
                        return;
                    }
                }
//                 if (chittalNo.indexOf("_")!=-1) {
//                subNo = chittalNo.substring(chittalNo.indexOf("_")+1, chittalNo.length());
//                chittalNo = chittalNo.substring(0,chittalNo.indexOf("_"));
//            }
                //System.out.println("ACCOUNTNO.indexOf(\"_\")" + ACCOUNTNO.indexOf("_"));
                if (ACCOUNTNO.indexOf("_") != -1) {
                    // String subNo=ACCOUNTNO.substring(ACCOUNTNO.indexOf("_")+1,ACCOUNTNO.length());
                    ACCOUNTNO = ACCOUNTNO.substring(0, ACCOUNTNO.indexOf("_"));
                    //System.out.println("ACCOUNTNO=dfdf==" + ACCOUNTNO);
                    //hash.put("ACCOUNTNO", ACCOUNTNO);
                }
                hash.put("ACCOUNTNO", ACCOUNTNO);
                hash.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
                hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                List lstHash = null;
                if (prodType.equals("TD")) {
                    lstHash = ClientUtil.executeQuery("Transfer.getAmount" + prodType, hash);
                    if (ACCOUNTNO.lastIndexOf("_") != -1) {
                        hash.put("ACCOUNTNO", ACCOUNTNO);
                    } else {
                        hash.put("ACCOUNTNO", ACCOUNTNO + "_1");
                    }
                    //System.out.println("acnum frm hash" + hash.get("ACT_NUM"));

                } else {
                    hash.put("ACCOUNTNO", ACCOUNTNO);
                }
                hash.put("ACCOUNTNO", txtAccountNo.getText());
                if (lstHash != null && lstHash.size() > 0) {
                    hash = (HashMap) lstHash.get(0);
                    //System.out.println("hash#%#%#"+hash);
                    hash.put("ACCOUNTNO", hash.get("AccountNo"));
                }else{
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO"));
                }
                
                hash.put("HOUSENAME", hash.get("HouseName"));
                hash.put("AMOUNT", hash.get("Amount"));
                hash.put("PRODUCTTYPE", hash.get("ProductType"));
                hash.put("CUSTOMERNAME", hash.get("CustomerName"));
                //System.out.println("print hash in focuslst" + hash);
                viewType = ACT_NUM;
                observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
                fillData(hash);
                if (txtAccountNo.getText().length() > 0) {
                    if (lblCustNameValue.getText().equals("")) {
                         if (prodType.equals("OA")) {
                             ClientUtil.showAlertWindow("Invalid Account No. - Account Closed ");
                         }else{
                             ClientUtil.showAlertWindow("Invalid Account No.");
                         }                        
                        txtAccountNo.setText("");
                        //Added BY Suresh
                        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                            txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                        }
                    }
                }
                if (!prodType.equals("GL") && ACCOUNTNO.length() > 0) {
                    if (ACCOUNTNO.lastIndexOf("_") == -1 && prodType.equals("TD")) {
                        ACCOUNTNO = ACCOUNTNO + "_1";
                    }
                    setCustHouseName(ACCOUNTNO);
                }
            }
            isAccountNumberLinkedwithATMProd(CommonUtil.convertObjToStr(txtAccountNo.getText()));
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && prodType.equals("OA")) {
                checkValidStatusForOperativeAccts();
            }
        }
    }//GEN-LAST:event_txtAccountNoFocusLost
    
    
    private void checkValidStatusForOperativeAccts() {
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", txtAccountNo.getText());
        List statusList = ClientUtil.executeQuery("getOperativeAccountsStatus", whereMap);
        if (statusList != null && statusList.size() > 0) {
            HashMap statusMap = (HashMap) statusList.get(0);
            if (statusMap.containsKey("ACT_STATUS_ID") && statusMap.get("ACT_STATUS_ID") != null) {
                ClientUtil.showAlertWindow(" Invalid Number\n Account status is :" + CommonUtil.convertObjToStr(statusMap.get("ACT_STATUS_ID")));
                txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                transDetails.setTransDetails(null, null, null);
            }
        } 
    }

    
    public boolean isAccountNumberLinkedwithATMProd(String actNumber) {
        boolean flag = false;
        if(actNumber != null && !actNumber.equals("")){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",actNumber);
            whereMap.put("CURR_DT",ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getIsAccountLinkedwitATM", whereMap);
            if(unAuthList != null && unAuthList.size() >0){
                ClientUtil.showMessageWindow("This account is linked with ATM Product, Not allowed for any Credit/Debit Transactions...!");
                txtAccountNo.setText("");
                flag = true;
            }
        }
        return flag;
    }
    
    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", curr_dt);
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);

        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());

        /*
         * open up the popu window for showing all the records for editing the
         * "Edit" operation requires to update the UI, so we are passing
         * TransferUI reference to ActionPopupUI
         */
        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_VIEW, this);//.show();
        showUpPop();
        //System.out.println("trans_dt" + transactionDateForEdit);
        this.populateUIData(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        //Added by chithra for mantis :10319: Weekly RD customisation for Ollukkara bank
        String prId = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected());
        double tAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
        int freq1 = 0;
        double actualDelay = 0.0;
        if (observable.getTransType().equals(CommonConstants.CREDIT) && depBehavesLike != null && depBehavesLike.equals("RECURRING") && tAmt > 0) {
            List list = null;
            HashMap hmap = new HashMap();
            String instalPendng = "";

            String depNo = txtAccountNo.getText();
            if (depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            hmap.put("ACC_NUM", depNo);
            hmap.put("CURR_DT", curr_dt.clone());
            hmap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap dailyProdID = new HashMap();
            dailyProdID.put("PID", prId);
            List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
            if (dailyFrequency != null && dailyFrequency.size() > 0) {
                HashMap dailyFreq = new HashMap();
                dailyFreq = (HashMap) dailyFrequency.get(0);
                String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                freq1 = CommonUtil.convertObjToInt(daily);
                if (freq1 == 7) {
                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiForWeek", hmap);
                }
            }

            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
            }
        }
        //added by rishad 02/04/2014
        HashMap loanAmountMap = observable.getALL_LOAN_AMOUNT();
        //System.out.println("dfhdhgfhgfh" + "prodType==" + prodType + "observable.getTransType()===" + observable.getTransType());
        String amt = CommonUtil.convertObjToStr(txtAmount.getText());
        double transAmt = 0;
        if (CommonUtil.convertObjToStr(txtAmount.getText()).length() > 0) {
            transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        }
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        //        if(prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)){
        //            txtAmountActionPerformed(null);
        String prodId = "";
        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if ((prodType.equals("TL") || prodType.equals("AD")) && observable.getTransType().equals(CommonConstants.CREDIT) && amt.length() > 0) {
            btnViewTermLoanDetails.setVisible(true);
            btnViewTermLoanDetails.setEnabled(true);
            if (!(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT)) {
                if (asAndWhenMap == null) {
                    asAndWhenMap = new HashMap();
                }
                HashMap hash = asAndWhenMap;
                double penalInt = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                //modified by rishad 02/04/2014
                if (hash != null && hash.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(hash.get("PENAL_WAIVER")).equals("Y")  && loanAmountMap != null && loanAmountMap.containsKey("PENAL_INT")) {
//                    int result = ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
//                    if (result == 0) {
//                        observable.setPenalWaiveOff(true);
//                        //added by rishad 02/04/2014
//                        observable.setPenalWaiveAmount(CommonUtil.convertObjToDouble(loanAmountMap.get("PENAL_INT")));
//                    } else {
//                        observable.setPenalWaiveOff(false);
//                    }
                  
                    //added by rishad for taking waive amount from editwaiveUI 08/04/2015
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
                            if (penalWaiveAmt > 0) {
                                observable.setPenalWaiveOff(true);
                                observable.setPenalWaiveAmount(penalWaiveAmt);
                            } else {
                                observable.setPenalWaiveOff(false);
                            }
                        }
                } else {
                    double totalDue = transDetails.calculatetotalRecivableAmountFromAccountClosing();
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST") && CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue() > 0
                            && CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue() > 0
                            && transAmt >= (totalDue - CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue())) {
                        int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                        if (result == 0) {
                            observable.setRebateInterest(true);
                        } else {
                            observable.setRebateInterest(false);
                        }
                    }
                }
                //added by rishad 09/04/2015
                  if (hash != null && hash.containsKey("INTEREST_WAIVER") && CommonUtil.convertObjToStr(hash.get("INTEREST_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
                            if (interestWaiveAmt > 0) {
                                observable.setInterestWaiveoff(true);
                                observable.setInterestWaiveAmount(interestWaiveAmt);
                            } else {
                                observable.setInterestWaiveoff(false);
                                observable.setInterestWaiveAmount(interestWaiveAmt);
                            }
                        }
                    }
                    if (hash != null && hash.containsKey("NOTICE_WAIVER") && CommonUtil.convertObjToStr(hash.get("NOTICE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
                            if (noticeWaiveAmt > 0) {
                                observable.setNoticeWaiveoff(true);
                                observable.setNoticeWaiveAmount(noticeWaiveAmt);
                            } else {
                                observable.setNoticeWaiveoff(false);
                            }
                        }
                    }
                    if (hash != null && hash.containsKey("PRINCIPAL_WAIVER") && CommonUtil.convertObjToStr(hash.get("PRINCIPAL_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double principalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PRINCIPAL"));
                            if (principalWaiveAmt > 0) {
                                observable.setPrincipalwaiveoff(true);
                                observable.setPrincipalWaiveAmount(principalWaiveAmt);
                            } else {
                                observable.setPrincipalwaiveoff(false);
                            }
                        }
                    }
                  if (hash != null && hash.containsKey("ARC_WAIVER") && CommonUtil.convertObjToStr(hash.get("ARC_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
                        if (arcWaiveAmt > 0) {
                            observable.setArcWaiveOff(true);
                            observable.setArcWaiveAmount(arcWaiveAmt);
                        } else {
                            observable.setArcWaiveOff(false);
                        }
                    }
                }
                if (hash  != null && hash.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(hash.get("ARBITRARY_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
                        if (arbitaryWaiveAmt > 0) {
                            observable.setArbitraryWaiveOff(true);
                            observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
                        } else {
                            observable.setArbitraryWaiveOff(false);
                        }
                    }
                }
                if (hash != null && hash.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(hash.get("POSTAGE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
                        if (postageWaiveAmt > 0) {
                            observable.setPostageWaiveOff(true);
                            observable.setPostageWaiveAmount(postageWaiveAmt);
                        } else {
                            observable.setPostageWaiveOff(false);
                        }
                    }
                }
                
               if (hash != null && hash.containsKey("RECOVERY_WAIVER") && CommonUtil.convertObjToStr(hash.get("RECOVERY_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double recoveryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("RECOVERY CHARGES"));
                        if (recoveryWaiveAmt > 0) {
                            observable.setRecoveryWaiveOff(true);
                            observable.setRecoveryWaiveAmount(recoveryWaiveAmt);
                        } else {
                            observable.setRecoveryWaiveOff(false);
                        }
                    }
                }
               
                if (hash != null && hash.containsKey("MEASUREMENT_WAIVER") && CommonUtil.convertObjToStr(hash.get("MEASUREMENT_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double measurementWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MEASUREMENT CHARGES"));
                        if (measurementWaiveAmt > 0) {
                            observable.setMeasurementWaiveOff(true);
                            observable.setMeasurementWaiveAmount(measurementWaiveAmt);
                        } else {
                            observable.setMeasurementWaiveOff(false);
                        }
                    }
                }
                
                // Kole field expense & operation
                 if (asAndWhenMap != null && asAndWhenMap.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD EXPENSE"));
                            if (koleFieldExpenseWaiveAmt > 0) {
                                observable.setKoleFieldExpenseWaiveOff(true);
                                observable.setKoleFieldExpenseWaiveAmount(koleFieldExpenseWaiveAmt);
                            } else {
                                observable.setKoleFieldExpenseWaiveOff(false);
                            }
                        }
                    }
                    
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("KOLE_FIELD_OPERATION_WAIVER") && asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldOperationWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD OPERATION"));
                            if (koleFieldOperationWaiveAmt > 0) {
                                observable.setKoleFieldOperationWaiveOff(true);
                                observable.setKoleFieldOperationWaiveAmount(koleFieldOperationWaiveAmt);
                            } else {
                                observable.setKoleFieldOperationWaiveOff(false);
                            }
                        }
                    }
                // End
                
                if (hash != null && hash .containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(hash.get("LEGAL_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
                        if (legalWaiveAmt > 0) {
                            observable.setLegalWaiveOff(true);
                            observable.setLegalWaiveAmount(legalWaiveAmt);
                        } else {
                            observable.setLegalWaiveOff(false);
                        }
                    }
                }
                if (hash != null && hash.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(hash.get("INSURANCE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
                        if (insurenceWaiveAmt > 0) {
                            observable.setInsuranceWaiveOff(true);
                            observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
                        } else {
                            observable.setInsuranceWaiveOff(false);
                        }
                    }
                }
                if (hash  != null && hash.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(hash.get("EP_COST_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
                        if (epWaiveAmt > 0) {
                            observable.setEpCostWaiveOff(true);
                            observable.setEpCostWaiveAmount(epWaiveAmt);
                        } else {
                            observable.setEpCostWaiveOff(false);
                        }
                    }
                }
                if (hash  != null && hash .containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(hash.get("DECREE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
                        if (degreeWaiveAmt > 0) {
                            observable.setDecreeWaiveOff(true);
                            observable.setDecreeWaiveAmount(degreeWaiveAmt);
                        } else {
                            observable.setDecreeWaiveOff(false);
                        }
                    }
                }
                if (hash != null && hash.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(hash.get("MISCELLANEOUS_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
                        if (miseWaiveAmt > 0) {
                            observable.setMiscellaneousWaiveOff(true);
                            observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
                        } else {
                            observable.setMiscellaneousWaiveOff(false);
                        }
                    }
                }
                if (hash != null && hash.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(hash.get("ADVERTISE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
                        if (advertiseWaiveAmt > 0) {
                            observable.setAdvertiseWaiveOff(true);
                            observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
                        } else {
                            observable.setAdvertiseWaiveOff(false);
                        }
                    }
                }
                
                // Added by nithya for 0008470 : overdue interest for EMI Loans
                if (hash != null && hash.containsKey("OVERDUEINT_WAIVER") && hash.get("OVERDUEINT_WAIVER") != null && CommonUtil.convertObjToStr(hash.get("OVERDUEINT_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0 && returnWaiveMap.containsKey("EMI_OVERDUE_CHARGE") && returnWaiveMap.get("EMI_OVERDUE_CHARGE") != null) {
                        double overDueIntWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EMI_OVERDUE_CHARGE"));
                        if (overDueIntWaiveAmt > 0) {
                            observable.setOverDueIntWaiveOff(true);
                            observable.setOverDueIntWaiveAmount(overDueIntWaiveAmt);
                        } else {
                            observable.setOverDueIntWaiveOff(false);
                        }
                    }
                }
                // End
                
            
            }
        } else {
//             btnViewTermLoanDetails.setVisible(false);
//             btnViewTermLoanDetails.setEnabled(false);
        }
        //System.out.println("prodType==" + prodType + "observable.getTransType()===" + observable.getTransType());

        if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            //System.out.println("#####ProdId :cvcbvcbvc" + prodId);
        if (depBehavesLike != null && depBehavesLike.equals("DAILY") && transAmt > 0) {
            HashMap dailyProdID = new HashMap();
            dailyProdID.put("PID", prodId);
            List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
            if (dailyFrequency != null && dailyFrequency.size() > 0) {
                HashMap dailyFreq = new HashMap();
                dailyFreq = (HashMap) dailyFrequency.get(0);
                String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                String weekly_spec = CommonUtil.convertObjToStr(dailyFreq.get("WEEKLY_SPEC"));
                String variableAmt = CommonUtil.convertObjToStr(dailyFreq.get("INSTALL_RECURRING_DEPAC"));
                int freq = CommonUtil.convertObjToInt(daily);
                if (freq == 7 && weekly_spec.equals("Y")) {
                    dailyProdID.put("PROD_ID", prodId);
                    List maxAmtList = ClientUtil.executeQuery("getDepProdDetails", dailyProdID);
                    if (maxAmtList != null && maxAmtList.size() > 0) {
                        HashMap maxAmtMap = new HashMap();
                        maxAmtMap = (HashMap) maxAmtList.get(0);
                        if (maxAmtMap != null && maxAmtMap.containsKey("MAX_DEPOSIT_AMT")) {
                            double maxAmt = CommonUtil.convertObjToDouble(maxAmtMap.get("MAX_DEPOSIT_AMT"));
                           if(variableAmt!=null && variableAmt.equalsIgnoreCase("V")){
                            if (maxAmt < transAmt) {
                                ClientUtil.displayAlert("Entered Amount Exceeds The Maximum Amount Limit");
                                txtAmount.setText("");
                                txtAmount.grabFocus();
                                return;
                            }
                         }else if(variableAmt!=null && variableAmt.equalsIgnoreCase("F")){
                              txtAmount.setText(CommonUtil.convertObjToStr(maxAmtMap.get("MAX_DEPOSIT_AMT")));
                         }
                        }
                    }

                }
            }
        }
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    HashMap recurringMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccountNo.getText();
                    //System.out.println("########Amount : " + amount + "####DepNo :" + depNo);
                    if (depNo.contains("_")) {
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    }
                    //System.out.println("######## BHEAVES :" + depNo);
                    recurringMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if (lst != null && lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        //Commented by nithya on 12-07-2019 for KD 551 - Need to allow future installment for weekly RD
//                        if (freq1 == 7) {
//                            if (tAmt > ((actualDelay + 1) * depAmount)) {
//                                ClientUtil.showMessageWindow("No Future Receipts Allowed");
//                                txtAmount.setText("");
//                                return;
//                            }
//                        }
                        double finalAmount = 0.0;
                        if (penalAmt > 0) {
                            String[] obj = {"Penalty with Installmets", "Installmets Only."};
                            int option = COptionPane.showOptionDialog(null, ("Select The Desired Option"), ("Receiving Penal with Installmets..."),
                                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                            //System.out.println("option===" + option);
                            if (option == 0) {
                                //System.out.println("amount1111====" + amount);
                                //System.out.println("depAmount1111====" + depAmount);
                                //System.out.println("penalAmt1111====" + penalAmt);
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                    //System.out.println("amount====" + amount);
                                    //System.out.println("depAmount====" + depAmount);
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                                //System.out.println("###### finalAmount : " + finalAmount);
                                if (finalAmount != 0) { //Added By Suresh R  11-Jul-2017
                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    txtAmount.setText("");
                                } else {
                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                }
                                if (finalAmount >= 0) {
                                    //ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    //txtAmount.setText("");

                                    double receivablAmt = depAmount;
                                    //System.out.println("penalAmt????222@@>>>>" + penalAmt);
                                    double totAmt1 = 0.0;
                                    //System.out.println("noofInstallment2111>>>222???>>>" + noofInstallment2);
                                    totAmt1 = receivablAmt * noofInstallment2;
                                    if (penalAmt > 0) {
                                        totAmt1 = totAmt1 + penalAmt;
                                    }
                                    //
                                    if (totAmt1 > 0.0) {
                                        txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));
                                        if (penalAmt > 0) {
                                            observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                        }
                                    } else {
                                        receivablAmt = depAmount + penalAmt;
                                        txtAmount.setText(CommonUtil.convertObjToStr(receivablAmt));
                                        observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    }
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                    observable.setDepositPenalFlag(true);
                                }
                                if (penalAmt == 0) {

                                    observable.setDepositPenalAmt(String.valueOf(0.0));
                                    observable.setDepositPenalMonth(String.valueOf(0.0));
                                    //                                observable.setDepositPenalAmt(String.valueOf(penalAmt));
//                                  observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                    observable.setDepositPenalFlag(false);  // added by Rajesh
                                }
                                calculateRDServiceTaxAmt(penalAmt);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            } else if (option == 1) {
                                finalAmount = amount % depAmount;
                                observable.setDepositPenalAmt(String.valueOf(0.0));
                                observable.setDepositPenalMonth(String.valueOf(0.0));
                                if (finalAmount >= 0) {
                                    //System.out.println("jhgjk>>>");
//                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                        + "Deposit Amount is : " + depAmount);
                                    double d1 = depAmount * noofInstallment2;
                                    //System.out.println("d1@@###>>>" + d1);
                                    txtAmount.setText(CommonUtil.convertObjToStr(d1));
                                }
                                calculateRDServiceTaxAmt(0.0);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            } else {
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                    //System.out.println("amount====" + amount);
                                    //System.out.println("depAmount====" + depAmount);
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                                if (finalAmount >= 0) {
                                    //vivek
                                    // ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    //double receivablAmt = depAmount + penalAmt;
                                    double receivablAmt = depAmount;
                                    //System.out.println("penalAmt????222@@>>>>" + penalAmt);
                                    double totAmt1 = 0.0;
                                    //System.out.println("noofInstallment2111>>>222???>>>" + noofInstallment2);
                                    totAmt1 = receivablAmt * noofInstallment2;
                                    if (penal > 0) {
                                        totAmt1 = totAmt1 + penal;
                                    }
                                    //
                                    if (totAmt1 > 0.0) {
                                        txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));
                                        if (penal > 0) {
                                            observable.setDepositPenalAmt(String.valueOf(penal));
                                        }
                                    } else {
                                        receivablAmt = depAmount + penalAmt;
                                        txtAmount.setText(CommonUtil.convertObjToStr(receivablAmt));
                                        observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    }
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());

                                } else {
                                    finalAmount = amount % depAmount;
                                    if (finalAmount != 0) {
                                        //System.out.println("jhgjk##111##>>>");
//                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                            + "Deposit Amount is : " + depAmount);
//                                    txtAmount.setText("");
                                    }
                                    observable.setDepositPenalAmt(String.valueOf(0.0));
                                    observable.setDepositPenalMonth(String.valueOf(0.0));
                                }
                                calculateRDServiceTaxAmt(0.0);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            }
                        }
//                        else {
//                            finalAmount = amount % depAmount;
//                            observable.setDepositPenalAmt(String.valueOf(0.0));
//                            observable.setDepositPenalMonth(String.valueOf(0.0));
//                            observable.setDepositPenalFlag(false);  // added by Rajesh
//                            if (finalAmount != 0) {
//                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                        + "Deposit Amount is : " + depAmount);
//                                txtAmount.setText("");
//                            }
//                        }
                        //System.out.println("######## BHEAVES REMAINING :" + finalAmount);
                    }
                    recurringMap = null;
                }
                prodMap = null;
                lst = null;
            }
        } //        ADDED HERE BY NIKHIL FOR DAILY DEPOSIT PARTIAL WITHDRAWAL
        else if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            //System.out.println("#####ProdId :" + prodId);
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("DAILY")
                        && CommonUtil.convertObjToStr(prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED")).equals("Y")) {
                    HashMap dailyDepMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccountNo.getText();
                    if (depNo.contains("_")) {
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    }
                    dailyDepMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                    if (lst.size() > 0) {
                        dailyDepMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                        if (depAmount < amount) {
                            //System.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
                            ClientUtil.displayAlert("Amount Greater than available balance!!");
                            txtAmount.setText("");
                            return;
                        }
                    }
                    dailyDepMap = null;

                }

            }
        }
        if (prodType.equals("TL")) {
            moreThanLoanAmountAlert();
        }
        //Added by chithra for service tax
        if (observable.getTransType().equals(CommonConstants.CREDIT)) {
            double taxAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
            if (taxAmt > 0 && TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                if (prodType.equals("GL")) {
                    String taxApplicable = "";
                    HashMap whereMap = new HashMap();
                    List taxSettingsList = new ArrayList();
                    HashMap taxMap;
                    whereMap.put("AC_HD_ID", txtAccountHeadValue.getText());
                    List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
//                    if (temp != null && temp.size() > 0) {
//                        HashMap value = (HashMap) temp.get(0);
//                        if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
//                            taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
//                        }
//                    }
                    if (temp != null && temp.size() > 0) {
                        HashMap value = (HashMap) temp.get(0);
                        if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                            taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                            if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                                if (value.containsKey("SERVICE_TAX_ID") && value.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID")).length() > 0) {
                                    taxMap = new HashMap();
                                    taxMap.put("SETTINGS_ID", value.get("SERVICE_TAX_ID"));
                                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                                    taxSettingsList.add(taxMap);
                                }
                            }
                        }
                    }
                    //if (taxApplicable != null && taxApplicable.equals("Y")) {
                    if (taxSettingsList != null && taxSettingsList.size() > 0) {
                        HashMap ser_Tax_Val = new HashMap();
                        ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curr_dt.clone());
                        ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                        try {
                            objServiceTax = new ServiceTaxCalculation();
                            //ser_Tax_Val.put("TEXT_BOX","TEXT_BOX");//Added By Kannan AR On 18-Sep-2017
                            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                            glserviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                            if (glserviceTax_Map != null && glserviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                                double tamt = CommonUtil.convertObjToDouble(glserviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                                lblServiceTaxval.setText(objServiceTax.roundOffAmt(tamt, "NEAREST_VALUE"));
//                                serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(tamt, "NEAREST_VALUE"));
                                tamt = Math.round(CommonUtil.convertObjToDouble(tamt));
                                lblServiceTaxval.setText(CommonUtil.convertObjToStr(tamt));
                                glserviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, tamt);
                            } else {
                                lblServiceTaxval.setText("0.00");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (prodType.equals("TL")) {
                    calculateServiceTaxAmt();
                }
                //// Commented by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
//                else if (prodType.equals("TD") && depBehavesLike != null && depBehavesLike.equals("RECURRING")) {// Added by nithya on 02-11-2018 for KD 313 : GST for Recurring deposit penal
//                    calculateRDServiceTaxAmt();
//                }

            } else {
                lblServiceTaxval.setText("0.00");
            }
        }
        if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0) {

            ClientUtil.displayAlert("amount should not be zero or empty");
            return;
        }
                //Added by sreekrishnan
        if(observable.getTransType().equals(CommonConstants.DEBIT) && depBehavesLike != null && depBehavesLike.equals("DAILY")){
            String depNo = txtAccountNo.getText();
            HashMap dailyMap = new HashMap();
            if (depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            dailyMap.put("DEPOSIT_NO", depNo);
            List Dailylst = ClientUtil.executeQuery("getDailyDepositBalnce", dailyMap);
            if (Dailylst.size() > 0 && Dailylst!=null) {
                dailyMap = (HashMap) Dailylst.get(0);
                double balAmount = CommonUtil.convertObjToDouble(dailyMap.get("BALANCE")).doubleValue();
                double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                if (balAmount < amount) {
                    //system.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
                    ClientUtil.displayAlert("Entered Amount is More than available balance!!");
                    txtAmount.setText("");                    
                    return;
                }
            }
        }
    }//GEN-LAST:event_txtAmountFocusLost
  //Added by chithra for service tax
    private void calculateServiceTaxAmt() {
        String actNum = CommonUtil.convertObjToStr(txtAccountNo.getText());
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        //double taxAmt = observable.calcServiceTaxAmount(actNum, prodId);
        List taxSettingsList = observable.calcServiceTaxAmount(actNum, prodId);  
        //if (taxAmt > 0) {
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curr_dt.clone());
            //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
//                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                    lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    //lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    lblServiceTaxval.setText(CommonUtil.convertObjToStr(amt)); // Changed by nithya on 23-04-2020 for KD-1837
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);// Changed by nithya on 23-04-2020 for KD-1837
                } else {
                    lblServiceTaxval.setText("0.00");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
        }

    }
  
    private void btnTransSaveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnTransSaveFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransSaveFocusLost

    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        //        if (observable.getTransStatus().equals(CommonConstants.STATUS_CREATED) && cboInstrumentType.getSelectedIndex() > 0) {
        if (cboInstrumentType.getSelectedIndex() > 0) {
            String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
            if (instrumentType.equals("VOUCHER")) {
                txtInstrumentNo1.setEnabled(false);
                txtInstrumentNo2.setEnabled(false);
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
                tdtInstrumentDate.setEnabled(false);

            } else if (instrumentType.equals("WITHDRAW_SLIP")) {
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
                tdtInstrumentDate.setEnabled(false);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            } else {
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
                tdtInstrumentDate.setEnabled(true);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            }
            String chkbook = "";
            int count = 0;
            HashMap hmap = new HashMap();
            if (chqBalList != null && chqBalList.size() > 0) {
                hmap = (HashMap) chqBalList.get(0);
                chkbook = CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
                count = CommonUtil.convertObjToInt(hmap.get("CNT"));
                hmap = null;
            }
            if ((chkbook.equals("Y")) && (count > 0)) {
                if (!instrumentType.equals("CHEQUE")) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Cheque book is issued to the customer. Do you want to continue?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    //System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo != 0) {
                        cboInstrumentType.setSelectedItem(null);

                    }
                }
            }
        }
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
        //        String prodId ="";
        //        prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        //        if(prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)){
        //            prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
        //            System.out.println("#####ProdId :"+prodId);
        //            HashMap prodMap = new HashMap();
        //            prodMap.put("PROD_ID",prodId.toString());
        //            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
        //            System.out.println("######## BHEAVES :"+lst);
        //            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
        //            if(lst!=null && lst.size()>0){
        //                prodMap= (HashMap)lst.get(0);
        //                if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
        //                    HashMap recurringMap = new HashMap();
        //                    double amount =CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        //                    String depNo = txtAccountNo.getText();
        //                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
        //                    depNo = depNo.substring(0,depNo.lastIndexOf("_"));
        //                    System.out.println("######## BHEAVES :"+depNo);
        //                    recurringMap.put("DEPOSIT_NO",depNo);
        //                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
        //                    if(lst!=null && lst.size()>0) {
        //                        recurringMap = (HashMap)lst.get(0);
        //                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
        //                        double finalAmount = 0.0;
        //                        if(penalAmt>0){
        //                            String[] obj ={"Penalty with Installmets","Installmets Only."};
        //                            int option =COptionPane.showOptionDialog(null,("Select The Desired Option"), ("Receiving Penal with Installmets..."),
        //                            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
        //                            if(option ==0){
        //                                finalAmount = amount % (penalAmt+depAmount);
        //                                if(finalAmount != 0){
        //                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..."+(depAmount+penalAmt));
        //                                    txtAmount.setText("");
        //                                }else{
        //                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
        //                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
        //                                }
        //                            }else{
        //                                finalAmount = amount % depAmount;
        //                                if(finalAmount != 0){
        //                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"+
        //                                    "Deposit Amount is : "+depAmount);
        //                                    txtAmount.setText("");
        //                                }
        //                                observable.setDepositPenalAmt(String.valueOf(0.0));
        //                                observable.setDepositPenalMonth(String.valueOf(0.0));
        //                            }
        //                        }else{
        //                            finalAmount = amount % depAmount;
        //                            observable.setDepositPenalAmt(String.valueOf(0.0));
        //                            observable.setDepositPenalMonth(String.valueOf(0.0));
        //                            if(finalAmount != 0){
        //                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"+
        //                                "Deposit Amount is : "+depAmount);
        //                                txtAmount.setText("");
        //                            }
        //                        }
        //                        System.out.println("######## BHEAVES REMAINING :"+finalAmount);
        //                    }
        //                    recurringMap = null;
        //                }
        //                prodMap = null;
        //                lst = null;
        //            }
        //        }
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        txtAmount.setEditable(true);
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNoActionPerformed
            returnWaiveMap = null;
        // TODO add your handling code here:
        //        HashMap hash = new HashMap();
        //        prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        //        String ACCOUNTNO = txtAccountNo.getText();
        //        if( prodType.equals("TD")){
        //            if (ACCOUNTNO.lastIndexOf("_")!=-1){
        //                hash.put("ACCOUNTNO", txtAccountNo.getText());
        //            }else
        //                hash.put("ACCOUNTNO", txtAccountNo.getText()+"_1");
        //        }else{
        //            hash.put("ACCOUNTNO", txtAccountNo.getText());
        //        }
        //        //        hash.put("ACCOUNTNO", txtAccountNo.getText());
        //        viewType = ACT_NUM;
        //        fillData(hash);
        //        if(lblCustNameValue.getText().equals("")){
        //            ClientUtil.showAlertWindow("Invalid Account No.");
        //            txtAccountNo.setText("");
        //        }
    }//GEN-LAST:event_txtAccountNoActionPerformed

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:
        //String[] reportnames = {"TransferScrollIB"};
        //  com.see.truetransact.clientutil.ttrintegration.TTIntegration.integration("TransferScrollIB");
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnReportActionPerformed
                                        private void btnReconsileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconsileActionPerformed

                                            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW
                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
                                                HashMap paramMap = new HashMap();
                                                HashMap whereMap = new HashMap();
                                                String type = "";
                                                if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                                                        || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                                                        || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
                                                    paramMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                                                    whereMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                                                }
                                                if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                                                    paramMap.put("NEW_MODE", "NEW_MODE");
                                                    whereMap.put("NEW_MODE", "NEW_MODE");
                                                    if (observable.getTransType().equals("CREDIT")) {
                                                        type = "DEBIT";
                                                    }
                                                    if (observable.getTransType().equals("DEBIT")) {
                                                        type = "CREDIT";
                                                    }
                                                }
                                                if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                                                    paramMap.put("EDIT_MODE", "EDIT_MODE");
                                                    whereMap.put("EDIT_MODE", "EDIT_MODE");
                                                }
                                                String transType = "";
                                                if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                                                        || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                                                        || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                                                        || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
//            if(btnAddDebit.isSelected() == false && btnAddCredit.isSelected() == false)
//                transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
//            if(transType.equals("CREDIT"))
//                type = "DEBIT";
//            if(transType.equals("DEBIT"))
//                type = "CREDIT";
                                                    HashMap acctMap = new HashMap();
                                                    acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                                                    List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                                                    if (head != null && head.size() > 0) {
                                                        acctMap = (HashMap) head.get(0);
                                                        if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
//                        if(btnAddDebit.isSelected() == false && btnAddCredit.isSelected() == false)
                                                            transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                                                            if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("CREDIT")) {
                                                                type = "DEBIT";
                                                            } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("DEBIT")) {
                                                                type = "CREDIT";
                                                            } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && observable.getTransType().equals("DEBIT")) {
                                                                observable.setReconcile("Y");
                                                            } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && observable.getTransType().equals("CREDIT")) {
                                                                observable.setReconcile("Y");
                                                            }
                                                            observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                                                            whereMap.put("TRANS_TYPE", type);
                                                            paramMap.put("TRANS_TYPE", type);
                                                            whereMap.put("AC_HEAD_ID", txtAccountHeadValue.getText());
                                                            paramMap.put("AC_HEAD_ID", txtAccountHeadValue.getText());
                                                            whereMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            paramMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransaction");
                                                            paramMap.put(CommonConstants.MAP_WHERE, whereMap);
//                        if(observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && reconcileScreen == true){
//                            whereMap.put("EDIT_MAP_LIST","EDIT_MAP_LIST");
//                            paramMap.put("EDIT_MAP_LIST","EDIT_MAP_LIST");
//                        }
                                                            if (transCommonUI == null) {
                                                                transCommonUI = new TransCommonUI(paramMap);
                                                            }
                                                            transCommonUI.show();

                                                        } else if (!acctMap.get("HO_ACCT").equals("") && acctMap.get("HO_ACCT").equals("Y")) {
                                                            btnReconsile.setVisible(true);
                                                            btnReconsile.setEnabled(true);
                                                            transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                                                            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                                                                paramMap.put("EDIT_MODE", "EDIT_MODE");
                                                            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                                                                    || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
                                                                paramMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                                                            }
                                                            paramMap.put("TRANS_ID", CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 2)));
                                                            if (transHOCommonUI == null && transType.equals("CREDIT")) {
                                                                paramMap.put("CREDIT", "CREDIT");
//                            transHOCommonUI.dispose();
                                                                paramMap.put("AMOUNT", txtAmount.getText());
                                                                transHOCommonUI = new TransHOCommonUI(paramMap, transType);
                                                            } else if (transHOCommonUI == null && transType.equals("DEBIT")) {
                                                                paramMap.put("DEBIT", "DEBIT");
//                            transHOCommonUI.dispose();
                                                                paramMap.put("AMOUNT", txtAmount.getText());
                                                                transHOCommonUI = new TransHOCommonUI(paramMap, transType);
                                                            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                                                                ClientUtil.showAlertWindow("Amount Should be entered");
                                                                return;
                                                            }
                                                            transHOCommonUI.show();
                                                        }
                                                    }
                                                } else {
                                                    HashMap acctMap = new HashMap();
                                                    acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                                                    List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                                                    if (head != null && head.size() > 0) {
                                                        acctMap = (HashMap) head.get(0);
                                                        if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                                                            whereMap.put("TRANS_TYPE", type);
                                                            paramMap.put("TRANS_TYPE", type);
                                                            whereMap.put("AC_HEAD_ID", txtAccountHeadValue.getText());
                                                            paramMap.put("AC_HEAD_ID", txtAccountHeadValue.getText());
                                                            whereMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            paramMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransaction");
                                                            paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                                                            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && reconcileScreen == true) {
                                                                whereMap.put("EDIT_MAP_LIST", "EDIT_MAP_LIST");
                                                                paramMap.put("EDIT_MAP_LIST", "EDIT_MAP_LIST");
                                                            }
                                                            if (transCommonUI == null) {
                                                                transCommonUI = new TransCommonUI(paramMap);
                                                            }
                                                            transCommonUI.show();
                                                            transCommonUI.setTransAmount(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
                                                            paramMap = null;
                                                            whereMap = null;
                                                            reconcileScreen = false;
                                                            reconcilebtnDisable = false;
                                                        } else if (!acctMap.get("HO_ACCT").equals("") && acctMap.get("HO_ACCT").equals("Y")) {
                                                            String TransactionType = "";
                                                            if (observable.getTransType().equals("CREDIT")) {
                                                                TransactionType = "CREDIT";
                                                            } else if (observable.getTransType().equals("DEBIT")) {
                                                                TransactionType = "DEBIT";
                                                            }
                                                            paramMap.put("TRANS_TYPE", "TRANSFER");
                                                            if (observable.getTransType().equals("CREDIT")) {
                                                                if (transHOCommonUI == null && txtAmount.getText().length() > 0) {
                                                                    paramMap.put("AMOUNT", txtAmount.getText());
                                                                    transHOCommonUI = new TransHOCommonUI(paramMap, TransactionType);
                                                                } else if (txtAmount.getText().length() > 0) {
                                                                    transHOCommonUI.dispose();
                                                                    paramMap.put("AMOUNT", txtAmount.getText());
                                                                    transHOCommonUI = new TransHOCommonUI(paramMap, TransactionType);
                                                                } else {
                                                                    ClientUtil.showAlertWindow("Amount Should be entered");
                                                                    return;
                                                                }
                                                            } else {
                                                                if (transHOCommonUI == null) {
                                                                    transHOCommonUI = new TransHOCommonUI(paramMap, TransactionType);
                                                                }
                                                            }
                                                            transHOCommonUI.show();
                                                        }
                                                    }
                                                }
                                            }

    }//GEN-LAST:event_btnReconsileActionPerformed
                                                                                                                                                                                                                            private void cboInstrumentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstrumentTypeFocusLost
                                                                                                                                                                                                                                instrumentTypeFocus();
    }//GEN-LAST:event_cboInstrumentTypeFocusLost
    private void populateInstrumentType() {
        ComboBoxModel objModel = new ComboBoxModel();
        objModel.addKeyAndElement("", "");
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (!prodType.equals("")) {
            if (prodType.equals("OA") || prodType.equals("AD")) {
                objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getInstrumentTypeModel().getDataForKey("WITHDRAW_SLIP"));
                objModel.addKeyAndElement("CHEQUE", observable.getInstrumentTypeModel().getDataForKey("CHEQUE"));
            }
            objModel.addKeyAndElement("VOUCHER", observable.getInstrumentTypeModel().getDataForKey("VOUCHER"));

            cboInstrumentType.setModel(objModel);
            cboInstrumentType.setSelectedItem(
                    CommonUtil.convertObjToStr(observable.getInstrumentTypeModel()));
        }
    }

    private void instrumentTypeFocus() {
        //                if (observable.getTransStatus().equals(CommonConstants.STATUS_CREATED) && cboInstrumentType.getSelectedIndex() > 0) {
        //                    String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
        //                    if (instrumentType.equals("VOUCHER")) {
        //                       txtInstrumentNo1.setEnabled(false);
        //                        txtInstrumentNo2.setEnabled(false);
        //                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(curr_dt.clone()));
        //                        tdtInstrumentDate.setEnabled(false);
        //
        //                    } else {
        //                        tdtInstrumentDate.setDateValue(DateUtil.getStringDate(curr_dt.clone()));
        //                        tdtInstrumentDate.setEnabled(true);
        //                        txtInstrumentNo1.setEnabled(true);
        //                        txtInstrumentNo2.setEnabled(true);
        //                       }
        //
        //                }
    }

    private void clearProdFields() {
        txtAccountHeadValue.setText("");
        txtAccountNo.setText("");
        lblAccountHeadDescValue.setText("");
        lblCustNameValue.setText("");
//                                                                                                                                                                                                                                lblHouseName.setText("");
    }
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            clearProdFields();
            //Added BY Suresh
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
                txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && ( prodType.equals("TL")||prodType.equals("AD"))) {
                btnWaive.setEnabled(true);
            } else {
                btnWaive.setEnabled(false);
            }
            populateInstrumentType();
            observable.setMainProductTypeValue(prodType);
            observable.setProductTypeValue(prodType);
            lblAccNoGl.setText("");
            if (prodType.equals("GL")) {
                lblLinkId.setVisible(true);
                lblAccNoGl.setVisible(true);                
                txtAccountHeadValue.setEnabled(true);
            } else {
                lblLinkId.setVisible(false);
                lblAccNoGl.setVisible(false);
                lblAccNoGl.setText("");
                txtAccountHeadValue.setEnabled(false);
            }
            if (prodType.equals("GL")) {
                if (TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                    setProdEnable(false);
                    cboProductID.setModel(new ComboBoxModel());
//                } else {
//                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
//                    observable.setProductTypeValue("");
                }
                //                observable.setProductTypeModel(new ComboBoxModel());
            } //            COMMENTED HERE TO CHECK
            //            else if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.DEBIT) && termDepositUI == null) {
            ////                COptionPane.showMessageDialog(this, "Deposit Withdraw can be done using Deposit Closer.");
            ////                cboProductType.setSelectedIndex(0);
            ////                cboProductID.setModel(new ComboBoxModel());
            ////                //                observable.setProductTypeModel(new ComboBoxModel());
            ////                return;
            //            }
            else if ((prodType.equals("TL") || prodType.equals("TD")) && observable.getTransType().equals(CommonConstants.CREDIT) && termDepositUI != null) {
                COptionPane.showMessageDialog(this, "Not Possible to credit loan accounts...");
                cboProductType.setSelectedIndex(0);
                cboProductID.setModel(new ComboBoxModel());
                return;
            } else {
                setProdEnable(true);
                //System.out.println("Product type action" + cboProductID.getModel().toString());
                //System.out.println("Product type action" + observable.getProductTypeValue());
                observable.getProducts();
                cboProductID.setModel(observable.getProductTypeModel());
                if (observable.getProductTypeValue() == null || observable.getProductTypeValue().equals("")) {
                    cboProductID.setSelectedIndex(0);
                } else {
                    cboProductID.setSelectedItem(
                            ((ComboBoxModel) cboProductID.getModel()).getDataForKey(
                            observable.getProductId())); // uctTypeValue()
                }
            }
            //System.out.println("observable.getOperation()" + observable.getOperation());
            if (observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
                if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                    if (loanAsanWhen) {
                        ArrayList loanList = new ArrayList();
                        loanList = (ArrayList) observable.getTransferTOs();
                        if (loanList != null && loanList.size() > 0) {//as an when cusstomer credit loan account moore than one credit is not allowed
                            boolean val = observable.moreThanOneCreditNotAllowed(prodType);
                            if (val) {
                                cboProductType.setSelectedIndex(0);
                                cboProductID.setSelectedIndex(0);
                                txtAccountHeadValue.setText("");
                                txtAccountNo.setText("");
                                ClientUtil.enableDisable(this.panTransDetails, false);
                                ClientUtil.enableDisable(this.panTransDetail, false);
                                loanAsanWhen = false;
                                return;
                            }
                        }
                    }
                }
            }
            if (observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
                if (prodType.equals("TD") && recurringCredit) {
                    boolean val = observable.moreThanOneCreditNotAllowed(prodType);
                    if (val) {
                        cboProductID.setSelectedIndex(0);
                        txtAccountHeadValue.setText("");
                        txtAccountNo.setText("");
                        ClientUtil.enableDisable(this.panTransDetails, false);
                        ClientUtil.enableDisable(this.panTransDetail, false);
                        recurringCredit = false;
                        return;
                    }
                }
            }
            //observable.setCbmProdId(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            //cboProdId.setModel(observable.getCbmProdId());
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed
    private void setProdEnable(boolean isEnable) {
        cboProductID.setEnabled(isEnable);
        txtAccountNo.setEnabled(isEnable);
        btnAccountNo.setEnabled(isEnable);
        btnAccountHead.setEnabled(!isEnable);
        //        btnPhoto.setEnabled(isEnable);
    }
    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        // Add your handling code here:
        viewType = ACCTHDID;
        final HashMap viewMap = new HashMap();
        if (observable.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getSelectAcctHeadCR");
        } else if (observable.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getSelectAcctHeadDB");
        }
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccountHeadActionPerformed
//      private void removeEditLock() {
//        //added by rishad 22/07/2015 for locking and removing
//        String acctNo = CommonUtil.convertObjToStr(txtAccountNo.getText());
//        if (acctNo.contains("_")) {
//            acctNo = CommonUtil.convertObjToStr(acctNo.substring(0, acctNo.indexOf("_")));
//        }
//        HashMap lockMap = new HashMap();
//        lockMap.put("RECORD_KEY", acctNo);
//        lockMap.put("CUR_DATE", curr_dt.clone());
//        lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//        ClientUtil.execute("deleteLock", lockMap);
//    }
//    private void removeAllEditLock() {
//        //added by rishad 22/07/2015 for locking and removing
//        int rowCount = this.tblTransList.getRowCount();
//        if (rowCount != 0) {
//            String actNum = "";
//            for (int i = 0; i < rowCount; i++) {
//                actNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
//                if (actNum != null && !actNum.equals("")) {
//                    HashMap lockMap = new HashMap();
//                    lockMap.put("RECORD_KEY", actNum);
//                    lockMap.put("CUR_DATE", curr_dt.clone());
//                    lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//                    ClientUtil.execute("deleteLock", lockMap);
//                }
//            }
//        }
//    }
//    
    private void tblTransListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransListMouseClicked
        // Add your handling code here:
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            panTransferEnableDisable(true);
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            lblAccNoGl.setEnabled(false);

        }
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_EDIT) {
            lblAccNoGl.setEnabled(false);
        }
        alreadyExistDeposit = true;
        reconcilebtnDisable = true;
        updationTransfer();
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            isRowClicked = true;
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            tdtValueDt.setEnabled(true);
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            txtAccountNo.setEnabled(false);
        }
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (prodType.equals("TD") && flagDepLink == true) {
            btnAccountNo.setEnabled(false);
            btnTransDelete.setEnabled(false);
            cboInstrumentType.setEnabled(false);
            txtParticulars.setEnabled(false);
        }
        reconcilebtnDisable = false;
        if ((observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) && prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            String act_num = observable.getAccountNo();
            HashMap inputMap = new HashMap();
            double avbal = 0.0;
            HashMap balMap = new HashMap();
            balMap = getAvailableAndShadowBal();
            inputMap.put("ACCOUNTNO", act_num);
            double tblSumAmt = 0.0;
            for (int i = 0; i < tblTransList.getRowCount(); i++) {
                String tblActNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
                if (act_num.equalsIgnoreCase(tblActNum) && act_num != null) {
                    double tblAmt = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                    tblSumAmt += tblAmt;
                }
            }
            boolean cont = checkForMinAmt(inputMap, 0, 0, CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue(), 0, tblSumAmt);
            if (!cont) {
                btnAuthorize.setEnabled(false);
                btnRejection.setEnabled(false);
                btnException.setEnabled(false);
                return;
            }
        }
        //Added By Suresh
        if (!prodType.equals("GL")) {
            String act_num = observable.getAccountNo();
            setCustHouseName(act_num);
        } else {
            if (txtAccountHeadValue.getText().length() > 0 || observable.getAccountHeadId().length() > 0) {
                observable.getAccountHead();
                this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
            }
        }
//added by anjuanand on 23-08-2014
//        rowCount += 1;
//        if (rowCount == tblTransList.getRowCount() + 1) {
//            tblTransList.setEnabled(false);
//            tblTransList.requestFocus(false);
//            btnAuthorize.setEnabled(true);
//            btnAuthorize.setFocusable(true);
//            btnAuthorize.setFocusPainted(true);
//            btnAuthorize.requestFocus(true);
//            tblTransList.clearSelection();
//        }
        if (observable.getParticulars() != null && observable.getParticulars().equals("Service Tax Recived")) {
            txtAmount.setEnabled(false);
            txtParticulars.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
            txtParticulars.setEnabled(true);
        }
    }//GEN-LAST:event_tblTransListMouseClicked
    public void btnCheck() {
        btnCancel.setEnabled(true);
        if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
            btnSave.setEnabled(true);
        }else{
            btnSave.setEnabled(false);
        }
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void panTransferEnableDisable(boolean value) {
        //this.lblAccountHeadValue.setText("");
        ClientUtil.enableDisable(this, value);

        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            ClientUtil.enableDisable(this.panTransDetails, false);
            this.btnAccountNo.setEnabled(false);
            this.btnAccountHead.setEnabled(false);
        }
        enableDisableButtons(value);
        observable.resetTransactionDetails();
    }

    private void updationTransfer() {
        if(chkVerifyAll.isSelected()==true){
            for (int i = 0; i < tblTransList.getRowCount(); i++) {
              observable.populatTranferTO(i); 
                
              authorizationCheckMap.put(String.valueOf(i), String.valueOf(i));
            }
        }else {
            if (bulkProcess == true && rdoBulkTransaction_Yes.isSelected()) { //Added By Revathi
                observable.populatTranferTO(bulkRow);
            } else {
                observable.populatTranferTO(tblTransList.getSelectedRow());
            }
            //observable.populatTranferTO(tblTransList.getSelectedRow());
            authorizationCheckMap.put(String.valueOf(tblTransList.getSelectedRow()), String.valueOf(tblTransList.getSelectedRow()));
        }
        observable.getAccountHead();
        populateTransferDetail();

        updateAccountInfo();
        _intTransferNew = false;
        reconcileScreen = true;
        observable.setTransStatus(CommonConstants.STATUS_MODIFIED);

        //if(observable.getOperation()==ClientConstants.ACTIONTYPE_EDIT &&
        if (this.lblTransactionIDValue.getText().equals("-")) {
            ClientUtil.enableDisable(this.panTransDetail, true);
            this.btnAccountNo.setEnabled(true);
            this.btnAccountHead.setEnabled(true);
            if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                if (observable.getLinkMap().containsKey("INSTALL_TYPE") && observable.getLinkMap().get("INSTALL_TYPE").equals("EMI") && observable.getTransType().equals("CREDIT")) {
//                ClientUtil.enableDisable(panTransDetail,false);
                    txtAmount.setEnabled(false);
                } else {
                    txtAmount.setEnabled(true);
//                 txtAmount.setText("");
                }
            } else {
                txtAmount.setEnabled(true);
            }
        } else {
            //            ClientUtil.enableDisable(panTransDetail,false);
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            btnAccountHead.setEnabled(false);
            btnAccountNo.setEnabled(false);


            if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y") && observable.getTransType() != null && observable.getTransType().equals("CREDIT")) {
                ClientUtil.enableDisable(panTransDetail, false);
                enableDisableButtons(false);

                if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
        }

        //        if (observable.getTransType().equals(CommonConstants.CREDIT)) {
        //            fieldsEnableDisable(false);
        //        } else {
        //            fieldsEnableDisable(true);
        //        }
        // this.update(null,null);
    }

    private void populateTransferDetail() {
        this.update(null, null);
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            if (prodType.equals("GL")) {
                lblLinkId.setVisible(true);
                lblAccNoGl.setVisible(true);
                lblAccNoGl.setEnabled(true);
                if (observable.getLinkBatchIdForGl() != null) {
                    lblAccNoGl.setText(observable.getLinkBatchIdForGl());
                } else {
                    lblAccNoGl.setText("");
                }
                //System.out.println("val 1==" + lblAccNoGl.getText());
            } else {
                lblAccNoGl.setEnabled(false);
                lblLinkId.setVisible(false);
                lblAccNoGl.setVisible(false);

            }
        }
    }
    private void btnTransDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransDeleteActionPerformed
        // Add your handling code here:
        observable.setTransStatus(CommonConstants.STATUS_DELETED);
        int rowSelected = this.tblTransList.getSelectedRow();
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                && rowSelected < 0) {
            rowSelected = this.rowForEdit;
        }
        String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if (head != null && head.size() > 0) {
                acctMap = (HashMap) head.get(0);
                if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                    ClientUtil.showAlertWindow("deletion not allowed, delete the entire batch");
                    return;
                }
            }
        }
        observable.deleteTransferData(rowSelected);
        this.updateTable();
//        removeEditLock();
        this.updateTransInfo();
        panTransferEnableDisable(false);
        this.btnAddCredit.setEnabled(true);
        this.btnAddDebit.setEnabled(true);
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_btnTransDeleteActionPerformed
    private boolean checkExpiryDate() {
        java.util.Date expiryDt = DateUtil.getDateMMDDYYYY(transDetails.getExpiryDate());
        if (DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(observable.getCurDate()), expiryDt) < 0) {
            int yesno = ClientUtil.confirmationAlert("Limit has Expired Do You Want allow Transaction");
            if (yesno == 1) {
                return true;
            }
        }
        return false;
    }

    private void updateTable() {
        this.tblTransList.setModel(observable.getTbmTransfer());
        this.tblTransList.getColumnModel().getColumn(0).setPreferredWidth(125);
        this.tblTransList.revalidate();
       
    }
    private void btnTransSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransSaveActionPerformed
        txtAmountFocusLost(null);
        //added by sreekrishnan for kcc renewal
//      boolean aa = observable.isAccountNoExists(txtAccountNo.getText(), false);
        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        
        if (prodType != null && (prodType.equals("TL") || prodType.equals("AD"))) {
                //Suspicious Account Check
                String transactionType = "";
                if (observable.getTransType().equals("CREDIT")) {
                    transactionType = "CREDIT";
                } else {
                    transactionType = "DEBIT";
                }
                String suspiciousAccountWarning = SuspiciousAccountValidation.checkForSuspiciousActivity(CommonUtil.convertObjToStr(txtAccountNo.getText()), transactionType,"TRANSFER");
                if (suspiciousAccountWarning.length() > 0) {
                    ClientUtil.showAlertWindow(suspiciousAccountWarning);
                    //btnCancelActionPerformed(null);
                    return;
                }
                // End
            }
        
        
        if(prodType != null && prodType.equals("TL")){
            isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccountNo.getText()));
        }
        boolean checkAcc = observable.checkAcNoWithoutProdType(txtAccountNo.getText(), false);
        //System.out.println("checkAcc"+checkAcc);
        if(!prodType.equals("GL") && !checkAcc){
            ClientUtil.showAlertWindow("Invalid Account Number");
            return;
        }
         double penalInt =0;
        if(asAndWhenMap!=null && asAndWhenMap.containsKey("LOAN_CLOSING_PENAL_INT"))
            penalInt = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
        if (observable.getTransType().equals("CREDIT") && CommonUtil.convertObjToDouble(penalInt) <= 0 && (prodType.equals("TL") || prodType.equals("AD"))) {
            //  row = new ArrayList();
            HashMap hmap = new HashMap();
            String acNo = CommonUtil.convertObjToStr(txtAccountNo.getText());
            hmap.put("ACCT_NUM", acNo);
            List list = ClientUtil.executeQuery("getRebateAllowedForActnum", hmap);
            hmap = null;
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                String rebateCalculation = CommonUtil.convertObjToStr(hmap.get("REBATE_CALCULATION"));
                String rebateAllowed = CommonUtil.convertObjToStr(hmap.get("REBATE_ALLOWED"));
                String rebatePercentage = CommonUtil.convertObjToStr(hmap.get("REBATE_PERCENTAGE"));
                double intper = CommonUtil.convertObjToDouble(hmap.get("INTEREST"));
                String spl_Rebat = CommonUtil.convertObjToStr(hmap.get("INT_RATE_REBATE"));
                double loanIntpercentage = CommonUtil.convertObjToDouble(hmap.get("LOAN_INT_PERCENT")); // Added by nithya on 09-01-2020 for KD-1234
                String Y = "Y";
                if (rebateAllowed.equals(Y)) {
                    if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation") && !spl_Rebat.equals("Y")) {
                        //System.out.println("bbbbb");
                        double rAmt = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) * intper / 100;
                        rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                        if (rAmt > 0) {
                            int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                            if (result == 0) {
                                observable.setRebateInterest(true);
                            } else {
                                observable.setRebateInterest(false);
                            }
                        }
                    } else if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation") && spl_Rebat.equals("Y")) {
                        if (asAndWhenMap.containsKey("ROI") && asAndWhenMap.get("ROI") != null) {
                            String intRt = CurrencyValidation.formatCrore(String.valueOf(asAndWhenMap.get("ROI")));
                            //double rAmt = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) / CommonUtil.convertObjToDouble(intRt); 
                            double rAmt = (CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) * loanIntpercentage) / CommonUtil.convertObjToDouble(intRt);// Added by nithya on 09-01-2020 for KD-1234
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            if (rAmt > 0) {
                                int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                                if (result == 0) {
                                    observable.setRebateInterest(true);
                                } else {
                                    observable.setRebateInterest(false);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Added by nithya on 30-04-2021 for KD-2801
        if (prodType.equals("TL") && observable.getTransType().equals(CommonConstants.CREDIT) && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            if (!observable.getInstalType().isEmpty() && !observable.getInstalType().equals("") && !observable.getInstalType().equals(null) && observable.getInstalType().equals("EMI") && observable.getEMIinSimpleInterest().equalsIgnoreCase("N")) {
                double totalEMIAmt = setEMIAmount();
                observable.setTransferAmount(String.valueOf(totalEMIAmt));
                txtAmount.setText(observable.getAmount());
                if (txtAmount.getText().length() > 0) {
                    totalEMIAmt = setEMILoanWaiveAmount();
                }
                observable.setTransferAmount(String.valueOf(totalEMIAmt));
                txtAmount.setText(observable.getAmount()); 
            }
        }
        
        if (observable.getKccNature().equals("Y")) {
            if (observable.getTransType().equals("DEBIT")) {
                //System.out.println("kccFlag####" + observable.getKccNature());
                //String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                String ACCOUNTNO = (String) txtAccountNo.getText();
                //System.out.println("ACCOUNTNO####Public" + ACCOUNTNO);
                //System.out.println("ACCOUNTNO####PutXTblic" + txtAccountNo.getText());
                //(txtAccNo.getText());
                //System.out.println("HASH kccRenewalChecking ======" + ACCOUNTNO);
                HashMap hmap = new HashMap();
                Date toDate = new Date();
                hmap.put("ACCOUNTNO", ACCOUNTNO);
                List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                if (kccList1 != null && kccList1.size() > 0) {
                    hmap = (HashMap) kccList1.get(0);
                    toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                    //System.out.println("toDate###" + toDate);
                    if (!toDate.equals("")) {
                        if (toDate.before((Date) curr_dt.clone())) {
                            ClientUtil.showMessageWindow("Account not Renewed!Can't make Payments!!");
                            return;
                    	}
                	}
				}
            }
        }
        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if (observable.getTransType().equals("DEBIT") &&prodType.equals("SA")) {
            HashMap wheMap = new HashMap();
            wheMap.put("PRODUCT_ID", prodType);
            wheMap.put("ACCT_NUM", txtAccountNo.getText());
            List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
            double totAmt =CommonUtil.convertObjToDouble( txtAmount.getText());
            if (lt1 != null && lt1.size() > 0) {
                HashMap tMap = (HashMap) lt1.get(0);
                String negYn = CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN"));
                double clAmt = CommonUtil.convertObjToDouble(tMap.get("CLEAR_BALANCE"));
                if (!negYn.equals("Y") && totAmt > clAmt) {
                    int result = ClientUtil.confirmationAlert("The account has _ve/Zero  balance.Do you Want to  continue?");
                    if (result == 0) {
                    } else {
                        return;
                    }
                }
            }
        }
          //Added By rishad 12/11/2019
         if (observable.getTransType().equals("CREDIT") && prodType.equals("AB")) {
                double clAmt = CommonUtil.convertObjToDouble(transDetails.getCBalance());
                double totAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
                if (totAmt > clAmt) {
                    int result = ClientUtil.confirmationAlert("Withdrawal Amount Exeeds Clear Balance.Do you Want to  continue?");
                    if (result == 0) {
                    } else {
                        return;
                    }
                }
            }
        if (observable.getTransType().equals("CREDIT")) {
            observable.setProdCreditTransfer(prodType);
        }
        if (observable.getTransType().equals("DEBIT")) {
            observable.setProdDebitTransfer(prodType);
        }
        String Acno = txtAccountNo.getText();
        HashMap whereMap = new HashMap();
        whereMap.put("ACNO", Acno);
        List lst1 = ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", whereMap);
        if (lst1 != null && lst1.size() > 0) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "The cutomer is death marked , Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo != 0) {
                btnCancelActionPerformed(null);
                return;
            }
        }
        if (!prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && txtAccountNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
            ClientUtil.displayAlert("Please Enter Account Number...!!!");
            return;
        }
        if (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD") || prodType.equals("AD")) {
            checkDocumentDetails();
            if (moreThanLoanAmountAlert()) {
                return;
            }
            //            if(prodType.equals("AD")&& observable.getTransType().equals(CommonConstants.DEBIT)){
            //                boolean expirydate=checkExpiryDate();
            //                         if(expirydate)
            //                             return;//now not needed business rule added
            //            }

            if (observable.getTransType().equals(CommonConstants.DEBIT)) {
                if (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD")) {
                    observable.setCheckDebitTermLoan(true);
                }
                //                hash= new HashMap();
                loanDebitType = null;
                String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
                if (prodType.equals("TL") || prodType.equals("ATL")) {
                    observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                }
                loanDebitType = observable.checkLoanDebit(instrumentType);
                if (loanDebitType != null && loanDebitType.size() > 0) {
                    if (prodType.equals("TL") || prodType.equals("ATL")) {
                        String debitType = CommonUtil.convertObjToStr(loanDebitType.get("DEBIT_LOAN_TYPE"));
                        String multiDisburse = transDetails.getIsMultiDisburse();
                        HashMap closeCharge = transDetails.getTermLoanCloseCharge();
                        double clearBalance = CommonUtil.convertObjToDouble(closeCharge.get("CLEAR_BALANCE")).doubleValue();
                        if (debitType.equals("DebitPrinciple") && multiDisburse.equals("N") && clearBalance < 0) {
                            ClientUtil.showMessageWindow("This Loan Account Not Having Multiple Disbursement");
                            loanDebitType = new HashMap();
                        }
				        //Added for mantis 10029-- Chithra 
                        if (prodType.equals("TL") && !debitType.equalsIgnoreCase("DP")) {
                            HashMap whreMap = new HashMap();
                            String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                            whreMap.put("PROD_ID", prodId);
                            String asCustComes = "";
                            List valList = ClientUtil.executeQuery("getPenalRateLoan", whreMap);
                            if (valList != null && valList.size() > 0) {
                                HashMap sing = (HashMap) valList.get(0);
                                if (sing != null && sing.containsKey("AS_CUSTOMER_COMES")) {
                                    asCustComes = CommonUtil.convertObjToStr(sing.get("AS_CUSTOMER_COMES"));
                                }
                            }
                            if (asCustComes.equals("Y")) {
                                ClientUtil.showMessageWindow("Transaction is not possible...........");
                                return;
                            }
                        }
                        if (prodType.equals("TL") && debitType.equalsIgnoreCase("DP")) {
                            double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            HashMap balMap = new HashMap();
                            balMap = getAvailableAndShadowBal();
                            double avbal = CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue();
                            if (avbal < enteredAmt) {
                                ClientUtil.showMessageWindow("Available balance is less than the Entered amount");
                                return;
                            }
                          }
                    }

                } else {
                    return;
                }
            }
        }
        if (!prodType.equals("") && prodType.equals("GL") && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if (head != null && head.size() > 0) {
                acctMap = (HashMap) head.get(0);
                if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                    if (acctMap.get("BALANCETYPE").equals("DEBIT") && observable.getTransType().equals("CREDIT")) {
                        observable.setReconcile("N");
                        observable.reconcileMap = new HashMap();
                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (transCommonUI == null) {
                            ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                            return;
                        } else {
                            double reconcileAmt = transCommonUI.getReconciledAmt();
                            if (reconcileAmt == txtAmt) {
                                if (transCommonUI.getReturnMap().size() > 0) {
                                    observable.reconcileMap = transCommonUI.getReturnMap();
                                }
                            } else {
                                ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                return;
                            }
                        }
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && observable.getTransType().equals("DEBIT")) {
                        observable.setReconcile("N");
                        observable.reconcileMap = new HashMap();
                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (transCommonUI == null) {
                            ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                            return;
                        } else {
                            double reconcileAmt = transCommonUI.getReconciledAmt();
                            if (reconcileAmt == txtAmt) {
                                if (transCommonUI.getReturnMap().size() > 0) {
                                    observable.reconcileMap = transCommonUI.getReturnMap();
                                }
                            } else {
                                ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                return;
                            }
                        }
                    } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && observable.getTransType().equals("DEBIT")) {
                        observable.setReconcile("Y");
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && observable.getTransType().equals("CREDIT")) {
                        observable.setReconcile("Y");
                    }
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                }
            }
            if (glserviceTax_Map != null && glserviceTax_Map.size() > 0 && lblServiceTaxval.getText() != null && CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0
                    && observable.getTransType().equals("CREDIT")) {
                glserviceTax_Map.put("GL_TRANS", "YES");
            }
        }
        if (prodType.equals("TD") && depBehavesLike != null && depBehavesLike.equalsIgnoreCase("RECURRING")) {// Added by nithya on 02-11-2018 for KD 313 : GST for Recurring deposit penal
            if (glserviceTax_Map != null && glserviceTax_Map.size() > 0 && lblServiceTaxval.getText() != null && CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0
                    && observable.getTransType().equals("CREDIT")) {
                glserviceTax_Map.put("GL_TRANS", "YES");
            }
        }
        if (!prodType.equals("GL")) {
            String acct_num = CommonUtil.convertObjToStr(txtAccountNo.getText());
            observable.checkAcNoWithoutProdType(acct_num, true);
        }
        if ((prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AD")) && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            if (observable.getMultiple_ALL_LOAN_AMOUNT() == null) {
                observable.setMultiple_ALL_LOAN_AMOUNT(new HashMap());
            }
            if (observable.getLblServiceTaxval() != null && CommonUtil.convertObjToDouble(observable.getLblServiceTaxval()) > 0
                    && observable.getTransType().equals("CREDIT")) {
                HashMap tempMap = transDetails.getTermLoanCloseCharge();
                tempMap.put("TOT_SER_TAX_AMT", observable.getLblServiceTaxval());
                tempMap.put("SER_TAX_HEAD", serviceTax_Map.get("TAX_HEAD_ID"));
                tempMap.put("SER_TAX_MAP", serviceTax_Map);
                observable.getMultiple_ALL_LOAN_AMOUNT().put(txtAccountNo.getText(), tempMap);
            } else {
                observable.getMultiple_ALL_LOAN_AMOUNT().put(txtAccountNo.getText(), transDetails.getTermLoanCloseCharge());
            }
        }

        //Added by sreekrishnan for borrowing
        if (prodType.equals("AB") && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            if (observable.getOtherBankMap() == null) {
                observable.setOtherBankMap(new HashMap());
            }
            if (observable.getTransType().equals("DEBIT")) {
                // check for other bank OD
                HashMap odMap = new HashMap();
                odMap.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
                List odList = ClientUtil.executeQuery("getSelectOtherBankAccountHead", odMap);
                if(odList != null && odList.size() > 0){
                    odMap = (HashMap)odList.get(0);
                    if(odMap.containsKey("ACCOUNT_TYPE") && odMap.get("ACCOUNT_TYPE") != null && odMap.get("ACCOUNT_TYPE").equals("OD")){
                        transDetails.getOtherBankAccountMap().put("ACCOUNT_TYPE","OD");
                    }
                }
            }
            observable.getOtherBankMap().put(txtAccountNo.getText(), (transDetails.getOtherBankAccountMap()));            
        }
            
        if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            String transType = "";
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if (head != null && head.size() > 0) {
                acctMap = (HashMap) head.get(0);
                if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
//                        if(btnAddDebit.isSelected() == false && btnAddCredit.isSelected() == false)
                    transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                    if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("CREDIT")) {
                        observable.setReconcile("N");
                        observable.reconcileMap = new HashMap();
                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (transCommonUI == null) {
                            ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                            return;
                        } else {
                            double reconcileAmt = transCommonUI.getReconciledAmt();
                            if (reconcileAmt == txtAmt) {
                                if (transCommonUI.getReturnMap().size() > 0) {
                                    observable.reconcileMap = transCommonUI.getReturnMap();
                                }
                            } else {
                                ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                return;
                            }
                        }
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("DEBIT")) {
                        observable.setReconcile("N");
                        observable.reconcileMap = new HashMap();
                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (transCommonUI == null) {
                            ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                            return;
                        } else {
                            double reconcileAmt = transCommonUI.getReconciledAmt();
                            if (reconcileAmt == txtAmt) {
                                if (transCommonUI.getReturnMap().size() > 0) {
                                    observable.reconcileMap = transCommonUI.getReturnMap();
                                }
                            } else {
                                ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                return;
                            }
                        }
                    } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("DEBIT")) {
                        observable.setReconcile("Y");
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("CREDIT")) {
                        observable.setReconcile("Y");
                    }
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                }
            }
        }
        if (flagCr == 1) {
            int tras_Credit = CommonUtil.convertObjToInt(txtAmount.getText());
            if (tras_Credit > TrueTransactMain.TRANS_CREDIT) {

                ClientUtil.showAlertWindow("Amount should not be greater than " + TrueTransactMain.TRANS_CREDIT);
                return;
            }
            flagCr = 0;
        } else if (flagDr == 1) {
            int tras_Debit = CommonUtil.convertObjToInt(txtAmount.getText());
            if (tras_Debit > TrueTransactMain.TRANS_DEBIT) {

                ClientUtil.showAlertWindow("Amount should not be greater than " + TrueTransactMain.TRANS_DEBIT);
                return;
            }
            flagDr = 0;
        }
        if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
            HashMap recurrMap = new HashMap();
            String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            recurrMap.put("PROD_ID", prodId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
            if (lst != null && lst.size() > 0) {
                HashMap recurringMap = (HashMap) lst.get(0);
                if (!recurringMap.get("BEHAVES_LIKE").equals("RECURRING") && !recurringMap.get("BEHAVES_LIKE").equals("DAILY") && !recurringMap.get("BEHAVES_LIKE").equals("THRIFT") && !recurringMap.get("BEHAVES_LIKE").equals("BENEVOLENT")) {
                    //                    double creditAmt = CommonUtil.convertObjToDouble(transDetails.getShadowCredit()).doubleValue();
                    String depositNo = txtAccountNo.getText();
                    if (depositNo.lastIndexOf("_") != -1) {
                        depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                    }
                    double depositAmt = 0.0;
                    HashMap amountMap = new HashMap();
                    amountMap.put("DEPOSIT_NO", depositNo);
                    lst = ClientUtil.executeQuery("getRenewalNewDetails", amountMap);
                    if (lst != null && lst.size() > 0) {
                        amountMap = (HashMap) lst.get(0);
                        double balanceAmt = 0.0;
                        HashMap transferInMap = new HashMap();
                        transferInMap.put("DEPOSIT_NO", depositNo);
                        lst = ClientUtil.executeQuery("getSelectOriginalAcNo", transferInMap);
                        if (lst != null && lst.size() > 0) {
                            transferInMap = (HashMap) lst.get(0);
                            transferInMap.put("DEPOSIT_NO", transferInMap.get("ORIGINAL_AC_NUMBER"));
                            lst = ClientUtil.executeQuery("getSelectOriginalAcNoTransOut", transferInMap);
                            if (lst != null && lst.size() > 0) {
                                transferInMap = (HashMap) lst.get(0);
                                depositAmt = CommonUtil.convertObjToDouble(transferInMap.get("TRANS_AMT")).doubleValue();
                            }
                        } else {
                            depositAmt = CommonUtil.convertObjToDouble(amountMap.get("DEPOSIT_AMT")).doubleValue();
                        }
                        double totBalance = CommonUtil.convertObjToDouble(amountMap.get("TOTAL_BALANCE")).doubleValue();
                        double creditAmt = CommonUtil.convertObjToDouble(amountMap.get("SHADOW_CREDIT")).doubleValue();
                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                            creditAmt = 0.0;
                            if (totBalance > 0) {
                                balanceAmt = depositAmt - totBalance;
                            } else if (creditAmt > 0) {
                                balanceAmt = depositAmt - 0.0;
                            }
                            if (txtAmt > balanceAmt) {
                                ClientUtil.showAlertWindow("Amount is Exceeding the depositAmount...\n"
                                        + "Enter the Amount as Rs." + balanceAmt);
                                return;
                            } else if (balanceAmt != txtAmt) {
                                ClientUtil.showAlertWindow("Balance Amount is Not Matching...\n"
                                        + "Credit to this Deposit A/c:" + balanceAmt);
                            }
                        } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                            if (totBalance > 0) {
                                balanceAmt = depositAmt - (totBalance + creditAmt);
                            } else if (totBalance == 0) {
                                balanceAmt = depositAmt - creditAmt;
                            }
                            if (depositAmt < txtAmt && totBalance == 0) {
                                ClientUtil.showAlertWindow("Amount is Exceeding the depositAmount...\n"
                                        + "Enter the Amount as Rs." + depositAmt);
                                return;
                            } else if (balanceAmt < txtAmt && totBalance > 0) {
                                if (balanceAmt == 0) {
                                    ClientUtil.showAlertWindow("Deposit Amount already credited...\n");
                                    return;
                                } else {
                                    ClientUtil.showAlertWindow("Amount is Exceeding the depositAmount...\n"
                                            + "Enter the Amount as Rs." + balanceAmt);
                                    return;
                                }
                            } else if (balanceAmt != txtAmt) {
                                ClientUtil.showAlertWindow("Balance Amount is Not Matching...\n"
                                        + "Credit to this Deposit A/c:" + balanceAmt);
                            }
                        }
                    }
                }
            }
            boolean save = true;
            save = observable.calcRecurringDates();
            if (save == false) {
                btnCancelActionPerformed(null);
            }
        }
        if (prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            double transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
            observable.setCreatingFlexi("");
            observable.setFlexiAmount(0.0);
            HashMap flexiMap = new HashMap();
            flexiMap.put("ACCOUNTNO", txtAccountNo.getText());
            List getList = ClientUtil.executeQuery("getFlexiDetails", flexiMap);
            if (getList != null && getList.size() > 0) {
                //System.out.println("getList : " + getList);
                flexiMap = (HashMap) getList.get(0);
                double lienAmt = 0.0;
                double alreadyTransAmt = 0.0;
                double depAvaialAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_BALANCE")).doubleValue();
                double depositAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_AMT")).doubleValue();
                double availBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                double totalBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                double minBal2 = CommonUtil.convertObjToDouble(flexiMap.get("MIN_BAL2_FLEXI")).doubleValue();
                alreadyTransAmt = CommonUtil.convertObjToDouble(flexiMap.get("SHADOW_DEBIT")).doubleValue();
                HashMap depositProdMap = new HashMap();
                double amtMultiples = 0.0;
                depositProdMap.put("PROD_ID", flexiMap.get("PROD_ID"));
                List lstProd = ClientUtil.executeQuery("getSchemeIntroDate", depositProdMap);
                if (lstProd != null && lstProd.size() > 0) {
                    depositProdMap = (HashMap) lstProd.get(0);
                    amtMultiples = CommonUtil.convertObjToDouble(depositProdMap.get("WITHDRAWAL_MULTI")).doubleValue();
                }
                HashMap lienMap = new HashMap();
                lienMap.put("LIEN_AC_NO", txtAccountNo.getText());
                lienMap.put("LIEN_DT", curr_dt);
                List lst = ClientUtil.executeQuery("getDetailsForSBLienActDetails", lienMap);
                if (lst != null && lst.size() > 0) {
                    lienMap = (HashMap) lst.get(0);
                    HashMap sumOfDepMap = new HashMap();
                    sumOfDepMap.put("FLEXI_ACT_NUM", txtAccountNo.getText());
                    List lstSum = ClientUtil.executeQuery("getSelectSumOfDepAmount", sumOfDepMap);
                    if (lstSum != null && lstSum.size() > 0) {
                        sumOfDepMap = (HashMap) lstSum.get(0);
                        depositAmt = CommonUtil.convertObjToDouble(sumOfDepMap.get("TOTAL_BALANCE")).doubleValue();
                    }
                    if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                        lienAmt = CommonUtil.convertObjToDouble(lienMap.get("SUM(LIEN_AMOUNT)")).doubleValue();
                        if (lienAmt > 0) {
                            if (availBal < transAmt) {
                                ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                return;
                            } else if (availBal == transAmt) {
                                lienAmt = depAvaialAmt;
                            } else {
                                lienAmt = transAmt;
                            }
                        } else {
                            if (availBal < transAmt) {
                                ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                return;
                            } else {
                                if (transAmt == availBal) {
                                    lienAmt = depAvaialAmt;
                                } else if (alreadyTransAmt == 0 && minBal2 >= transAmt) {
                                    if (minBal2 > (availBal - transAmt)) {
                                        if (depositAmt > transAmt) {
                                            lienAmt = transAmt;
                                        } else {
                                            lienAmt = depositAmt;
                                        }
                                    } else {
                                        lienAmt = 0.0;
                                    }
                                } else if (alreadyTransAmt == 0 && minBal2 < transAmt) {
                                    lienAmt = availBal - transAmt;
                                    lienAmt = depositAmt - lienAmt;
                                } else if (alreadyTransAmt > 0 && minBal2 >= (transAmt + alreadyTransAmt)) {
                                    lienAmt = 0.0;
                                } else if (alreadyTransAmt > 0 && minBal2 < (transAmt + alreadyTransAmt)) {
                                    if (availBal == (transAmt + alreadyTransAmt)) {
                                        lienAmt = depositAmt;
                                    } else {
                                        lienAmt = availBal - (transAmt + alreadyTransAmt);
                                        lienAmt = depositAmt - lienAmt;
                                    }
                                }
                            }
                        }
                        if (availBal > transAmt) {
                            double balancelienAmt = lienAmt % amtMultiples;
                            if (balancelienAmt != 0) {
                                lienAmt = (lienAmt - balancelienAmt) + amtMultiples;
                            }
                            if (depositAmt < lienAmt) {
                                lienAmt = depAvaialAmt;
                            }
                        } else {
                            lienAmt = depAvaialAmt;
                        }
                    }
                    if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                        ClientUtil.showAlertWindow("Can not Edit this Transaction Please Reject or Delete, and do it fresh transaction...");
                        btnCancelActionPerformed(null);
                        return;
                    }
                    //                        if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT){
                    //                            HashMap cashMap = new HashMap();
                    //                            cashMap.put("TRANS_ID",lblTransactionIDValue.getText());
                    //                            System.out.println("cashMap "+cashMap);
                    //                            lst = ClientUtil.executeQuery("getSBLienTransferAccountNo",cashMap);
                    //                            if(lst !=null && lst.size()>0){
                    //                                cashMap = (HashMap)lst.get(0);
                    //                                double transactionAmt = CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")).doubleValue();
                    //                                String lienNo = CommonUtil.convertObjToStr(cashMap.get("AUTHORIZE_REMARKS"));
                    //                                cashMap.put("LIEN_AC_NO",txtAccountNo.getText());
                    //                                lst = ClientUtil.executeQuery("getDetailsForEditModeSBLienAct",cashMap);
                    //                                if(lst !=null && lst.size()>0){
                    //                                    for(int i = 0; i<lst.size(); i++){
                    //                                        cashMap = (HashMap)lst.get(i);
                    //                                        lienAmt += CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                    //                                    }
                    //                                }
                    //                                if(!lienNo.equals("")){
                    //                                    double balance = availBal - alreadyTransAmt;
                    //                                    if(transAmt>(transactionAmt+balance)){
                    //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                    //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                    //                                        return;
                    //                                    }else{
                    //                                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    //                                        transAmt = alreadyTransAmt - lienAmt;
                    //                                        if(alreadyTransAmt>minBal2){
                    //                                            if(txtAmt>transAmt)
                    //                                                lienAmt = txtAmt - transAmt;
                    //                                            else{
                    //                                                cashMap.put("LIEN_NO",lienNo);
                    //                                                lst = ClientUtil.executeQuery("getDetailsForEditModeSingleSBLienAct",cashMap);
                    //                                                if(lst !=null && lst.size()>0){
                    //                                                    cashMap = (HashMap)lst.get(0);
                    //                                                    lienAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                    //                                                }
                    //                                                if(transactionAmt>transAmt){
                    //                                                    if(txtAmt>minBal2)
                    //                                                        lienAmt = transactionAmt - (txtAmt + lienAmt);
                    //                                                    else
                    //                                                        lienAmt = 0.0;
                    //                                                }else
                    //                                                    lienAmt = txtAmt;
                    //                                            }
                    //
                    //                                        }else
                    //                                            lienAmt = 0.0;
                    //                                    }
                    //                                }else{//ok d'nt change anything...Sathiya.
                    //                                    double balance = availBal - alreadyTransAmt;
                    //                                    if(transAmt>balance){
                    //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                    //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                    //                                        return;
                    //                                    }else{
                    //                                        transAmt = transAmt - transactionAmt + alreadyTransAmt;
                    //                                        if((transAmt+transactionAmt) == availBal)
                    //                                            lienAmt = depositAmt;
                    //                                        else if(transAmt>minBal2){
                    //                                            lienAmt = availBal - transAmt + lienAmt;
                    //                                            lienAmt = depositAmt - lienAmt;
                    //                                        }else
                    //                                            lienAmt = 0.0;
                    //                                    }
                    //                                }
                    //                            }
                    //                        }
                }
                lst = null;
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW
                        || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                    if (lienAmt > 0) {
                        String[] obj = {"Ok", "Cancel"};
                        int options = COptionPane.showOptionDialog(null, ("Lien Marked for Rs. : " + lienAmt + " on Flexi Deposit"), ("Flexi Account"),
                                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                        if (options == 0) {
                            observable.setCreatingFlexi("FLEXI_LIEN_CREATION");
                            observable.setFlexiAmount(lienAmt);
                        } else {
                            return;
                        }
                    } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                        observable.setCreatingFlexi("FLEXI_LIEN_DELETION");
                    }
                }
            }
            getList = null;
        }
        if (prodType.equals("TD"))// && observable.getTransType().equals(CommonConstants.CREDIT))
        {
            observable.setRenewDepAmt(transDetails.getDepositAmt());
        }

        MandatoryCheck objMandatory = new MandatoryCheck();
        TransferHashMap objMap = new TransferHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();

        if (!observable.getProductTypeValue().equals("GL")) {
            mandatoryMap.put("txtAccountNo", new Boolean(true));
            mandatoryMap.put("cboProductID", new Boolean(true));
        }
        if (observable.getTransType().equals(CommonConstants.CREDIT)) {
            mandatoryMap.put("cboInstrumentType", new Boolean(false));
            mandatoryMap.put("tdtInstrumentDate", new Boolean(false));
            mandatoryMap.put("txtInstrumentNo1", new Boolean(false));
            mandatoryMap.put("txtInstrumentNo2", new Boolean(false));
        } else {
            mandatoryMap.put("cboInstrumentType", new Boolean(true));
            mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        }
        if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0) {
            ClientUtil.displayAlert("Amount Should Not Be Empty Or Zero.");
            return;
        }
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        HashMap custStatMap = new HashMap();
        custStatMap.put("ACCOUNTNO", txtAccountNo.getText());
        List cstList = ClientUtil.executeQuery("getCustStatus", custStatMap);
        custStatMap = null;
        if (cstList != null && cstList.size() > 0) {
            custStatMap = (HashMap) cstList.get(0);
            String cust_status = CommonUtil.convertObjToStr(custStatMap.get("C_STATUS"));
            if (cust_status.equals("DECEASED") && !instrumentType.equals("CHEQUE")) {
                ClientUtil.displayAlert("Customer is DeathMarked");
            } else if (cust_status.equals("DECEASED") && instrumentType.equals("CHEQUE")) {
                ClientUtil.displayAlert("Customer is DeathMarked");
                return;
            }
        }

        if (observable.getTransType().equals(CommonConstants.DEBIT)) {
            double avbal = 0.0;
            double shadowDeb = 0.0;
            double tblAmtSum = 0.0;
            double tblAmtFinal = 0.0;

            double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
            HashMap balMap = new HashMap();
            balMap = getAvailableAndShadowBal();
            avbal = CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue();
            shadowDeb = CommonUtil.convertObjToDouble(balMap.get("SH_DEBIT")).doubleValue();
            String act_num = txtAccountNo.getText();
            if (act_num.equals("")) {
                act_num = txtAccountHeadValue.getText();
            }
            double tblAmt = 0.0;
            int rowSel = tblTransList.getSelectedRow();
            if (tblTransList.getRowCount() > 0) {
                for (int i = 0; i < tblTransList.getRowCount(); i++) {
                    String tblac_num = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
                    if (tblac_num.equals("")) {
                        tblac_num = observable.getAcHdID(i);
                    }
                    String tbl_trnas_type = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 4));
                    if (tblac_num.equals(act_num) && tbl_trnas_type.equals("DEBIT") && rowSel != i) {
                        tblAmt = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                        enteredAmt = enteredAmt + tblAmt;
                        tblAmtSum = tblAmtSum + tblAmt;
                    }
                    if (tblac_num.equals(act_num) && tbl_trnas_type.equals("DEBIT")) {
                        double tblAmtForMinBalCheck = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                        tblAmtFinal = tblAmtFinal + tblAmtForMinBalCheck;
                    }
                }
            }
            HashMap inputMap = new HashMap();
            inputMap.put("ACCOUNTNO", act_num);
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            if (prodType.equalsIgnoreCase("OA")) {
                boolean cont = checkForMinAmt(inputMap, enteredAmt, shadowDeb, avbal, tblAmtSum, tblAmtFinal);
                if (!cont) {
                    return;
                }
            }
            if (observable.getTransType().equals(CommonConstants.DEBIT) && observable.getBalanceType().equals(CommonConstants.DEBIT)) {
                String acchead = txtAccountHeadValue.getText();
                double limit = 0.0;
                double annualLimit = 0.0;
                double percentage = 0.0;
                enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                HashMap hmap = new HashMap();
                hmap.put("ACHEAD", acchead);
                hmap.put("CUR_DATE", curr_dt);
                List lmtList = ClientUtil.executeQuery("getLimitAmt", hmap);
                if (lmtList != null && lmtList.size() > 0) {
                    hmap = (HashMap) lmtList.get(0);
                    limit = CommonUtil.convertObjToDouble(hmap.get("LIMIT_AMT")).doubleValue();
                    annualLimit = CommonUtil.convertObjToDouble(hmap.get("ANNUAL_LIMIT_AMT")).doubleValue();
                    percentage = CommonUtil.convertObjToDouble(hmap.get("OVER_DRAW_PER")).doubleValue();
                    annualLimit = annualLimit + (annualLimit * percentage / 100);
                    if (enteredAmt + avbal > annualLimit) {
                        int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                        int d = 0;
                        if (c != d) {
                            return;
                        }
                    }
                    if (enteredAmt > limit) {
                        int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                        int d = 0;
                        if (c != d) {
                            return;
                        }
                    }
                }

            }
        }
        if (observable.getTransType().equals(CommonConstants.CREDIT) && observable.getBalanceType().equals(CommonConstants.CREDIT)) {
            String acchead = txtAccountHeadValue.getText();
            double limit = 0.0;
            double annualLimit = 0.0;
            double percentage = 0.0;
            double avbal = 0.0;
            double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
            HashMap balMap = new HashMap();
            balMap = getAvailableAndShadowBal();
            avbal = CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue();
            HashMap hmap = new HashMap();
            hmap.put("ACHEAD", acchead);
            List lmtList = ClientUtil.executeQuery("getLimitAmt", hmap);
            if (lmtList != null && lmtList.size() > 0) {
                hmap = (HashMap) lmtList.get(0);
                limit = CommonUtil.convertObjToDouble(hmap.get("LIMIT_AMT")).doubleValue();
                annualLimit = CommonUtil.convertObjToDouble(hmap.get("ANNUAL_LIMIT_AMT")).doubleValue();
                percentage = CommonUtil.convertObjToDouble(hmap.get("OVER_DRAW_PER")).doubleValue();
                annualLimit = annualLimit + (annualLimit * percentage / 100);
                if (enteredAmt + avbal > annualLimit) {
                    int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                    int d = 0;
                    if (c != d) {
                        return;
                    }
                }
                if (enteredAmt > limit) {
                    int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                    int d = 0;
                    if (c != d) {
                        return;
                    }
                }
            }
        }
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(), panFieldPanel, mandatoryMap);
        if (observable.getProductTypeValue().equals("GL") && txtAccountHeadValue.getText().length() == 0) {
            mandatoryMessage += "Account Head not selected...";
        }
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        if (!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
            HashMap InterBranMap = new HashMap();
            InterBranMap.put("AC_HD_ID", observable.getAccountHeadId());
            List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
            InterBranMap = null;
            if (lst != null && lst.size() > 0) {
                InterBranMap = (HashMap) lst.get(0);
                String IbAllowed = CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
                if (IbAllowed.equals("N")) {
                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
                    return;
                }
            }
        }
        updateOBFields();
         if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
            HashMap recurrMap = new HashMap();
            String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            recurrMap.put("PROD_ID", prodId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
             if (lst != null && lst.size() > 0) {
                HashMap recurringMap = (HashMap) lst.get(0);
                if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                  //added by rishad 26-11-2015
                HashMap instMap=new HashMap();
                 String depositNo1 = txtAccountNo.getText();
                    if (depositNo1.lastIndexOf("_") != -1) {
                        depositNo1 = depositNo1;
                    } else {
                        depositNo1 = depositNo1 + "_1";
                    }
                         instMap.put("DEPOSIT_NO_SUB",depositNo1);
                        instMap.put("NO_OF_INSTALLMENT",observable.getInstallMentNo());
                        List listInstall = ClientUtil.executeQuery("getMinMaxInstalmentNo",instMap);
                        if(listInstall != null &&listInstall.size() > 0)
                        {
                        HashMap resultMap=(HashMap)listInstall.get(0);
                        String fromInstallment=CommonUtil.convertObjToStr(resultMap.get("FROMMIN"));
                        String toInstallment=CommonUtil.convertObjToStr(resultMap.get("TOIN"));
                      observable.setParticulars(CommonUtil.convertObjToStr(observable.getParticulars())+" Insatllment from"+fromInstallment
                    +"TO "+toInstallment);
                        }
            }
                
             }}
                //end
//        //added by rishad 10/07/2015 for avoiding doubling issue at 10/07/2015
//        HashMap map = new HashMap();
//        map.put("SCREEN_ID", getScreenID());
//        map.put("RECORD_KEY", txtAccountNo.getText());
//        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        map.put("CUR_DATE", curr_dt.clone());
//        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
//        if (lstLock != null && lstLock.size() > 0) {
//            ClientUtil.displayAlert("Account is locked");
//            txtAccountNo.setText("");
//            btnCancelActionPerformed(null);
//            return;
//        } else {
//            HashMap lockMap = new HashMap();
//            ArrayList lst = new ArrayList();
//            lst.add("ACCOUNT NUMBER");
//            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            lockMap.put("ACCOUNT NUMBER", txtAccountNo.getText());
//            setEditLockMap(lockMap);
//            setEditLock();
//        }
//        //end
        if (!this._intTransferNew) {
            int rowSelected = 0;
            if (bulkProcess == true && rdoBulkTransaction_Yes.isSelected()) { //Added By Revathi
                rowSelected = bulkRow;
            } else {
                rowSelected = this.tblTransList.getSelectedRow();
            }
            //int rowSelected = this.tblTransList.getSelectedRow();
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                    && rowSelected < 0) {
                rowSelected = this.rowForEdit;
                observable.setTransStatus(CommonConstants.STATUS_MODIFIED);
            }
            observable.insertTransferData(rowSelected);
        } else {
            observable.insertTransferData(-1);
        }
        this.updateTable();
        updateTransInfo();
        panTransferEnableDisable(false);
        this.btnAddCredit.setEnabled(true);
        this.btnAddDebit.setEnabled(true);
        //        cboProductID.setModel(new ComboBoxModel());
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            reconcilebtnDisable = true;
        }
        alreadyExistDeposit = false;
        //        btnAddCredit.setSelected(false);
        //        btnAddDebit.setSelected(false);
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        TrueTransactMain.populateBranches();
        //TrueTransactMain.getCboBranchList().setSelectedItem(ProxyParameters.BRANCH_ID);
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        //System.out.println("prodtypr44$$$$$$" + prodType + "obse.getTransTyp&&&&" + observable.getTransType());

    }//GEN-LAST:event_btnTransSaveActionPerformed
    //    private boolean checkZergoAmount(){
    //        Double amt = CommonUtil.convertObjToDouble(this.txtAmount.getText());
    //        if(amt.doubleValue()<=0)
    //            return true;
    //        else
    //            return false;
    //    }

    private HashMap checkLoanDebit(String prodType) {
        HashMap loanDebitType = new HashMap();
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        if (prodType.equals("TL") || prodType.equals("ATL") && observable.getTransType().equals("DEBIT") || (prodType.equals("AD") && observable.getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER")) || (prodType.equals("AAD") && observable.getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))) {
            String[] debitType = {"Debit Interest", "DebitPrinciple", "Debit_Penal_Int", "Other_Charges"};
            String var = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
            //System.out.println("var@#####" + var);
            if (var.equals("Debit Interest")) {
                loanDebitType.put("DEBIT_LOAN_TYPE", "DI");
            }
            if (var.equals("DebitPrinciple")) {
                loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
            }
            if (var.equals("Debit_Penal_Int")) {
                loanDebitType.put("DEBIT_LOAN_TYPE", "DPI");
            }
            if (var.equals("Other_Charges")) {
                loanDebitType.put("DEBIT_LOAN_TYPE", "OTHERCHARGES");
            }
            if (prodType.equals("TL") || prodType.equals("ATL")) {
                String multiDisburse = transDetails.getIsMultiDisburse();
                HashMap closeCharge = transDetails.getTermLoanCloseCharge();
                double clearBalance = CommonUtil.convertObjToDouble(closeCharge.get("CLEAR_BALANCE")).doubleValue();
                if (var.equals("DebitPrinciple") && multiDisburse.equals("N") && clearBalance < 0) {
                    ClientUtil.showMessageWindow("This Loan Account Not Having Multiple Disbursement");
                    loanDebitType = new HashMap();
                }
            }

        }
        return loanDebitType;
    }
    private void btnAddDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDebitActionPerformed
        // Add your handling code here:
        lblAccNoGl.setText("");
        observable.setAccNumGl("");
        accNo1 = "";
        flagDr = 1;
//        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
//        if (observable.getTotalDrInstruments() > 0) {
//            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
//                ClientUtil.displayAlert("More than one Debit not allowed on Interbranch Transactions...");
//                return;
//            }
//        }
        panTransferEnableDisable(true);
        ClientUtil.enableDisable(this, true);
        this.btnAccountNo.setEnabled(true);
        this.btnAccountHead.setEnabled(true);
        this.btnAddCredit.setEnabled(true);
        this.btnTransDelete.setEnabled(false);
        this._intTransferNew = true;
        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        observable.setTransType(CommonConstants.DEBIT);
        btnOrgOrResp.setText("R");
        //        setUpTitle();   // Commented by Rajesh

        fieldsEnableDisable(true);
        txtAccountNo.setEditable(true);
        txtAccountNo.setEnabled(true);
        txtAmount.setEnabled(true);
        txtAmount.setEditable(true);
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        cboProductID.setModel(observable.getProductTypeModel());
        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        btnReconsile.setEnabled(false);
        setEnableDisableBulkTransaction(false);
        //Added BY Suresh
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            lblServiceTaxval.setText("");
        }
        /*
         * observable.getProducts();
         * cboProductID.setModel(observable.getProductTypeModel());
         */
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        chkVerifyAll.setVisible(false);        
    }//GEN-LAST:event_btnAddDebitActionPerformed
    private void setAuthorizeButtons() {
        this.btnDelete.setEnabled(false);
        this.btnAdd.setEnabled(false);
        this.btnEdit.setEnabled(false);
        this.btnSave.setEnabled(false);
        this.btnCancel.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        btnAccountHead.setEnabled(false);
        btnAccountNo.setEnabled(false);
    }
    private void btnAddCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCreditActionPerformed
        // Add your handling code here:
        lblAccNoGl.setText("");
        observable.setAccNumGl("");
        accNo1 = "";
//        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
//        if (observable.getTotalCrInstruments() > 0) {
//            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
//                ClientUtil.displayAlert("More than one Credit not allowed on Interbranch Transactions...");
//                return;
//            }
//        }
        panTransferEnableDisable(true);
        ClientUtil.enableDisable(this, true);
        this.btnAccountNo.setEnabled(true);
        this.btnAccountHead.setEnabled(true);
        this.btnAddDebit.setEnabled(true);
        this.btnTransDelete.setEnabled(false);
        this._intTransferNew = true;
        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        observable.setTransType(CommonConstants.CREDIT);
        if (btnOrgOrResp.getText().equals("R") && observable.isHoAccount()) {
            btnOrgOrResp.setText("R");
        } else {
            btnOrgOrResp.setText("O");
        }

        loanAsanWhen = true;
        recurringCredit = true;
        //        setUpTitle();        // Commented by Rajesh

        fieldsEnableDisable(false);
        txtAccountNo.setEditable(true);
        txtAccountNo.setEnabled(true);
        txtAmount.setEnabled(true);
        txtAmount.setEditable(true);
        if (flagDepLink == true) {
            //            getOperativeAccNo(depositNo,amount);
        }
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        cboProductID.setModel(observable.getProductTypeModel());
        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        btnReconsile.setEnabled(false);
        setEnableDisableBulkTransaction(false);
        //Added BY Suresh
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        }
        /*
         * observable.getProducts();
         * cboProductID.setModel(observable.getProductTypeModel());
         */
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        chkVerifyAll.setVisible(false);
        returnWaiveMap = null;
    }//GEN-LAST:event_btnAddCreditActionPerformed
    private void fieldsEnableDisable(boolean yesno) {
        //        this.txtTokenNo.setEnabled(false);
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            yesno = false;
        }
        this.txtInstrumentNo1.setEditable(yesno);
        this.txtInstrumentNo2.setEditable(yesno);
        this.tdtInstrumentDate.setEnabled(yesno);
        this.cboInstrumentType.setEnabled(yesno);
         returnWaiveMap = new HashMap();
    }

    private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
        // Add your handling code here:
        final TransferRB rb = new TransferRB();
        //String openingDate=lblOpeningDateValue.getText();
        if (tdtInstrumentDate.getDateValue() != null && tdtInstrumentDate.getDateValue().length() > 0) {
            ToDateValidation toDate = new ToDateValidation(observable.getCurDate(), true);
            toDate.setComponent(this.tdtInstrumentDate);
            if (!toDate.validate()) {
                COptionPane.showMessageDialog(this, rb.getString("WARNING_INSTRUMENT_DATE1"));
                tdtInstrumentDate.setDateValue("");
                tdtInstrumentDate.requestFocus();
                return;
            }
        }
    }//GEN-LAST:event_tdtInstrumentDateFocusLost
    private void showUpPop() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = actionPopUp.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        actionPopUp.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2 - 20);
        actionPopUp.show();
    }
    private void mitRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectionActionPerformed
        // Add your handling code here:
        if (viewType == this.AUTHORIZE && !this.btnAdd.isEnabled()) {
            observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
            int confirm = ClientUtil.confirmationAlert("Are you sure want to Reject", 1);
            if (confirm != 0) {
                return;
            }
            String remark = "";
            if (flag != true) {
                remark = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_REJE"));
            }
            observable.authorize(remark);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
            return;
        }
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());



        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_REJECT, this);
        showUpPop();
        authorizeStatus();
    }//GEN-LAST:event_mitRejectionActionPerformed

    private void getOperativeAccNo(String depositNo, double amount) {
        HashMap operativeMap = new HashMap();
        operativeMap.put("DEPOSIT_NO", depositNo);
        List lst = ClientUtil.executeQuery("getCustomeridFoeDep", operativeMap);
        if (lst.size() > 0) {
            operativeMap = (HashMap) lst.get(0);
            lst = ClientUtil.executeQuery("getCustomerAccNoFoeDep", operativeMap);
            if (lst.size() > 0) {
                operativeMap = (HashMap) lst.get(0);
                txtParticulars.setText(String.valueOf(operativeMap.get("ACT_NUM")));
                //                btnTransSaveActionPerformed(null);
                cboProductType.setSelectedItem("Operative Account");
                //                cboProductID.setSelectedItem("Savings bank");
                cboProductType.setEnabled(false);
                observable.setTransType("CREDIT");
                cboProductTypeActionPerformed(null);
                txtAccountNo.setText(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                txtAccountNo.setEnabled(false);
                lblCustNameValue.setText(CommonUtil.convertObjToStr(operativeMap.get("ACCT_NAME")));
                lst = ClientUtil.executeQuery("getOperativeProdDesc", operativeMap);
                if (lst.size() > 0) {
                    operativeMap = (HashMap) lst.get(0);
                    observable.setProductId(CommonUtil.convertObjToStr(operativeMap.get("PROD_DESC")));
                    cboProductID.setSelectedItem(((ComboBoxModel) cboProductID.getModel()).getDataForKey(observable.getProductId()));
                    //                    cboProductID.setSelectedItem(CommonUtil.convertObjToStr(operativeMap.get("PROD_DESC")));
                    cboProductID.setEnabled(false);
                    txtAccountHeadValue.setText(CommonUtil.convertObjToStr(operativeMap.get("AC_HD_ID")));
                    txtAccountHeadValue.setEnabled(false);
                    lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(operativeMap.get("PROD_DESC")));
                    btnAccountNo.setEnabled(false);
                    txtAmount.setText(String.valueOf(amount));
                    txtParticulars.setText(depositNo + "_1");
                    btnTransSave.setEnabled(true);
                    //                    btnTransSaveActionPerformed(null);
                }
            }
        }
    }

    private void btnRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectionActionPerformed
        if (authorizationCheckMap.size() == tblTransList.getRowCount() || termDepositUI != null) {
            if (termDepositUI != null) {

                if (!termDepositUI.getRenewalTransMap().equals("")) {

                    if (tblTransList.getRowCount() != 0 && tblTransList.getValueAt(0, 0) != null && txtAccountNo.getText().length() > 0)//&& (!CommonUtil.convertObjToStr(tblTransList.getValueAt(0,0)).equals(""))
                    {
                        termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_TRANSFER", "DEPOSIT_TRANSFER_REJECTED");
                    } else {
                        termDepositUI.getRenewalTransMap().put("INTEREST_AMT_TRANSFER", "INTEREST_TRANSFER_REJECTED");
                    }
                }
            }
            this.mitRejectionActionPerformed(evt);
            txtAmount.setEditable(false);
            txtAccountNo.setEditable(false);
        } else {
            COptionPane.showMessageDialog(this, resourceBundle.getString("NOT_VERIFIED"));
        }
        //verify all button enabled at the time rejection issues raised by prsanth(added by rishad 26-11-2015)
         chkVerifyAll.setEnabled(true);
    }//GEN-LAST:event_btnRejectionActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        if (authorizationCheckMap.size() == tblTransList.getRowCount()) {
            this.mitExceptionsActionPerformed(evt);
        } else {
            COptionPane.showMessageDialog(this, resourceBundle.getString("NOT_VERIFIED"));
        }
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        if (authorizationCheckMap.size() == tblTransList.getRowCount() || termDepositUI != null) {
            if (termDepositUI != null) {
                if (!termDepositUI.getRenewalTransMap().equals("")) {
                    if (tblTransList.getRowCount() != 0 && tblTransList.getValueAt(0, 0) != null && txtAccountNo.getText().length() > 0) {
                        termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_TRANSFER", "DEPOSIT_TRANSFER_AUTHORIZED");
                    } else {
                        termDepositUI.getRenewalTransMap().put("INTEREST_AMT_TRANSFER", "INTEREST_TRANSFER_AUTHORIZED");
                    }
                }
            }
                //added by sreekrishnan 3/3/2016 for avoiding doubling issue
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        mitAuthorizeActionPerformed(null);
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
            
            txtAccountNo.setEditable(false);
            txtAmount.setEditable(false);
            txtAmount.setEnabled(false);
        } else {
            COptionPane.showMessageDialog(this, resourceBundle.getString("NOT_VERIFIED"));
        }
        chkVerifyAll.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // Add your handling code here:
        observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
//        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
//        if (observable.getTransType().equals(CommonConstants.DEBIT) && observable.getTotalDrInstruments() > 0) {
//            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
//                ClientUtil.displayAlert("More than one Debit not allowed on Interbranch Transactions...");
//                return;
//            }
//        }
//        if (observable.getTransType().equals(CommonConstants.CREDIT) && observable.getTotalCrInstruments() > 0) {
//            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
//                ClientUtil.displayAlert("More than one Credit not allowed on Interbranch Transactions...");
//                return;
//            }
//        }
        if (this.cboProductID.getSelectedItem() != null && ((String) this.cboProductID.getSelectedItem()).length() > 0) {

            callView(ACT_NUM);
        }
    }//GEN-LAST:event_btnAccountNoActionPerformed

    private void mitExceptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionsActionPerformed
        // Add your handling code here:
        // setup the operation and the status text
        if (viewType == this.AUTHORIZE && !this.btnAdd.isEnabled()) {
            observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
            String remark = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_EXCE"));
            observable.authorize(remark);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
            return;
        }
        observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());

        /*
         * open up the popu window for showing allrecords for setting exceptions
         * pass one action indicator as "Exceptions" which specify that we want
         * the exceptions operation in the popup window the "Exceptions"
         * operation do not require to update the UI, so we are not passing the
         * TransferUI reference to ActionPopupUI
         */
        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_EXCEPTION, this);//.show();
        showUpPop();
        authorizeStatus();
    }//GEN-LAST:event_mitExceptionsActionPerformed

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // Add your handling code here:
        // setup the operation and the status text
        if (viewType == this.AUTHORIZE && !this.btnAdd.isEnabled()) {
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            boolean depauth = observable.depositAuthorizationValidation(observable.getTransferTOs());
            if (depauth == true) {
                btnCancelActionPerformed(null);
                return;
            }
            String remark = "";
            if (flag != true) {
                remark = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_AUTH"));
            }
            if (txtInstrumentNo2.getText() != null && txtInstrumentNo2.getText().equals("DEPOSIT_PENAL")) {
                observable.setDepositPenalAmt(transDetails.getPenalAmount());
                observable.setDepositPenalMonth(transDetails.getPenalMonth());
            }
            if (observable.isHoAccount()) {
                ArrayList alist = setOrgOrRespDetails();
                if (alist != null && alist.size() > 0) {
                    observable.setOrgRespList(alist);
                }
            }
            observable.authorize(remark);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Transfer");//Changed By Suresh R 12-Jun-2019 - KDSA-506 : Loan authorizing problem
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Transfer Transactions");
                }
                if (fromAuthListDebit) {
                    authorizeListDebitUI.removeSelectedRow();
                    this.dispose();
                    authorizeListDebitUI.setFocusToTable();
                }
                if (fromAuthListCredit) {
                    authorizeListCreditUI.removeSelectedRow();
                    this.dispose();
                    authorizeListCreditUI.setFocusToTable();
                }
                
            }
//            if (observable.isIsTransaction() == true) {
//                removeAllEditLock();
//            }
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
            return;
        }
        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());

        /*
         * open up the popu window for showing all the records for authorization
         * pass one action indicator as "Authorize" which specify that we want
         * the deletion operation in the popup window the "Authorize" operation
         * do not require to update the UI, so we are not passing the TransferUI
         * reference to ActionPopupUI
         */
        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_AUTHORIZE, this);
        showUpPop();
        authorizeStatus();
        setEnableDisableBulkTransaction(false);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    public void authorizeStatus() {
        if (this.batchIdForEdit != null) {
            this.viewType = this.AUTHORIZE;
            if (viewType == AUTHORIZE) {
                HashMap hmap = new HashMap();
                hmap.put("BATCH_ID", batchIdForEdit);
                String accountNo = "";
                List mainList = ClientUtil.executeQuery("getSelectNominalMemFee", hmap);
                if (mainList != null && mainList.size() > 0) {
                    hmap = (HashMap) mainList.get(0);
                    String allowAuth = CommonUtil.convertObjToStr(hmap.get("ALLOW_AUTH_BY_STAFF"));
                    if (allowAuth.equals("N")) {
                        hmap.put("TRANS_DT", curr_dt);
                        hmap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                        List aclist = ClientUtil.executeQuery("getAcNoForTransfer", hmap);//Changed by Revathi 13-03-24 reff Rajesh for avoid unnessary Loading data.
                        if (aclist != null && aclist.size() > 0) {
                            hmap = (HashMap) aclist.get(0);
                            hmap.put("USER_ID", TrueTransactMain.USER_ID);
                            hmap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);

                            accountNo = CommonUtil.convertObjToStr(hmap.get("ACT_NUM"));
                            hmap.put("ACCOUNT NO", accountNo);
                            List lst = ClientUtil.executeQuery("getStaffIdForAccount", hmap);
                            List lst1 = ClientUtil.executeQuery("getStaffIdForLoggedUser", hmap);
                            String staffId = "";
                            String loggedStaffId = "";
                            if (lst != null && lst.size() > 0) {
                                hmap = (HashMap) lst.get(0);
                                staffId = CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                            }
                            if (lst1 != null && lst1.size() > 0) {
                                hmap = (HashMap) lst1.get(0);
                                loggedStaffId = CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                            }
                            if (!staffId.equals("") || !loggedStaffId.equals("")) {
                                if (staffId.equals(loggedStaffId)) {
                                    ClientUtil.displayAlert("Authorization not allowed in own account");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            this.populateUIData(observable.getOperation());
            setAuthorizeButtons();
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAddActionPerformed
        // Add your handling code here:
        btnAddActionPerformed(evt);
    }//GEN-LAST:event_mitAddActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        // clear everything
        if (evt != null && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            if (tblTransList.getRowCount() > 1) {
                int c = ClientUtil.confirmationAlert("Are You Sure You want To Continue with CANCEL???");
                int d = 0;
                if (c != d) {
                    return;
                }
            }
        }
        btnWaive.setEnabled(false);
        observable.setInstalType(null);
        deletescreenLock();
//        if(observable.isIsTransaction()==false || viewType == AUTHORIZE){
//        removeAllEditLock();}
        authorizationCheckMap.clear();
        observable.resetOBFields();
        this.updateTable();
        updateTransInfo();
        transDetails.setTransDetails(null, null, null);
        transDetails.setSourceFrame(null);
        observable.setEmiNoInstallment(0);
        observable.setInstallMentNo(0);
        // Disable all the screen
        ClientUtil.enableDisable(this, false);
        this.enableDisableButtons(false);
        clearProdFields();
        setModified(false);
        asAndWhenMap = null;
        // set the operation as "NOOP" and reload the menu and toolbar
        observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setTable();
        tblTransList.setModel(observable.getTbmTransfer());
        viewType = 0;
        setupMenuToolBarPanel();
        if (termDepositUI != null) {
            btnCloseActionPerformed(null);
            //            if(!termDepositUI.getRenewalTransMap().equals("")){
            //                HashMap renewalMap = termDepositUI.getRenewalTransMap();
            //                System.out.println("transfer UI renewalMap : "+renewalMap);
            //                if(renewalMap.containsKey("INTEREST_AMT_TRANSFER")){
            //                    termDepositUI.getRenewalTransMap().put("INTEREST_AMT_CANCEL","INTEREST_AMT_CANCEL");
            if (afterSaveCancel == false) {
                termDepositUI.disableSaveButton();
            }
            //                }
            //            }
        }
        //        this.setUpTitle();  Comment by Rajesh
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        btnReconsile.setEnabled(false);
        btnView.setEnabled(true);
        setBatchIdForEdit("");
        observable.setLinkMap(null);
        //        cboProductID.setModel(new ComboBoxModel());
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        btnReconsile.setVisible(false);
        btnReconsile.setEnabled(false);
        if (transCommonUI != null) {
            transCommonUI.dispose();
        }
        transCommonUI = null;
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
         if (fromAuthListDebit) {
            this.dispose();
            fromAuthListDebit = false;
            authorizeListDebitUI.setFocusToTable();
        }
          if (fromAuthListCredit) {
            this.dispose();
            fromAuthListCredit = false;
            authorizeListCreditUI.setFocusToTable();
        }
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        lblHouseName.setText("");
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setEnableDisableBulkTransaction(false);
        observable.setMultiple_ALL_LOAN_AMOUNT(null);
        btnOrgOrResp.setVisible(false);
        setOrgBranch("");
        setOrgBranchName("");
        setOrgOrRespAdviceDt(null);
        setOrgOrRespAdviceNo("");
        setOrgOrRespAmout(0.0);
        setOrgOrRespBranchId("");
        setOrgOrRespBranchName("");
        setOrgOrRespCategory("");
        setOrgOrRespDetails("");
        setOrgOrRespTransType("");
//        btnViewTermLoanDetails.setVisible(false);
//        btnViewTermLoanDetails.setEnabled(false);
        lblAccNoGl.setText("");
        observable.setLinkBatchIdForGl("");
        transDetails.setChangeInterest("");
        lblServiceTaxval.setText("");
        returnWaiveMap = new HashMap();
        allWaiveMap = new HashMap();
        transDetails.setRepayData(null);
        chkVerifyAll.setSelected(false);
        bulkProcess = false;//Added By Revathi.L
        observable.setIsBulkUploadTxtFile(false);
        observable.setServiceTax_Map(null);
        observable.setGlserviceTax_Map(null);
        glserviceTax_Map=null;
        serviceTax_Map = null;
    }//GEN-LAST:event_btnCancelActionPerformed
//    public void extensionRecoveringInterestAmt(CTextField txtDepositAmount, HashMap intMap,TermDepositUI termDepositUI){
//        this.termDepositUI = termDepositUI;
//        flag = true;
//        observable.depositRenewalFlag = true;
//        observable.renewalIntFlag = true;
//        flagDepLink = true;
//        if(intMap.containsKey("transferingAmt")){
//            txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("AMOUNT")));
//        }
//        this.txtDepositAmount = txtDepositAmount;
//        transferDepMap = new HashMap();
//        transferDepMap = intMap;
//        System.out.println("$$$$$$$transferDepMap :" +transferDepMap);
//        if(intMap !=null){
//            System.out.println("%%%%%%intMap :"+intMap);
//            HashMap subNoMap = new HashMap();
//            btnAddActionPerformed(null);
//            btnAddCreditActionPerformed(null);
//            btnTransDelete.setEnabled(false);
//            btnAddDebit.setEnabled(false);
//            subNoMap.put("DEPOSIT_NO",intMap.get("ACCOUNTNO"));
//            subNoMap.put("CUST_ID",intMap.get("CUST_ID"));
//            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
//            if(lst!=null && lst.size()>0){
//                subNoMap = (HashMap)lst.get(0);
////                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
////                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
////                String accountNo = depositNo + "_" + subNo;
////                intMap.put("ACCOUNTNO",accountNo);
//                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
//                observable.setTransType("CREDIT");
//                cboProductTypeActionPerformed(null);
////                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
////                lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
////                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
////                txtAccountNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
////                lblCustNameValue.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                lblCreatedByValue.setText(ProxyParameters.USER_ID);
//                double amount = CommonUtil.convertObjToDouble(intMap.get("INT_AMT")).doubleValue();
//                intMap.put("AMOUNT",intMap.get("INT_AMT"));
//                intMap.put("A/C HEAD",intMap.get("ACCT_HEAD"));
//                intMap.put("A/C HEAD DESCRIPTION",intMap.get("PROD_DESC"));
//                fillData(intMap);
//                txtAmount.setText(String.valueOf(amount));
//                ClientUtil.enableDisable(this,true);
//                btnTransSave.setEnabled(true);
//                btnSave.setEnabled(true);
//                txtAmount.setEditable(false);
//                cboProductType.setEnabled(false);
//                cboProductID.setEnabled(false);
//                txtAccountNo.setEditable(false);
//                btnAccountHead.setEnabled(false);
//                btnAccountNo.setEnabled(false);
//                cboInstrumentType.setSelectedItem("Voucher");
//                cboInstrumentType.setEnabled(false);
//                txtParticulars.setText("To "+intMap.get("ACCOUNTNO")+"_1");
//                txtParticulars.setEnabled(false);
//                btnCancel.setEnabled(false);
//                btnView.setEnabled(false);
//                btnTransSaveActionPerformed(null);
//                btnAddDebitActionPerformed(null);
//                if(flagDepLink == true){
//                    observable.setDepLinkBatchId("DEP_LINK");
////                    observable.setDepAccNO(accountNo);
//                }
//            }
//        }
//        intMap = null; 
//    }
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        // get the selcted product from the product list
        /*
         * txtAccountHeadValue.setText( observable.getAccountHeadForProductId(
         * (String) ( (ComboBoxModel)(cboProductID.getModel())
         * ).getKeyForSelected() )
         );
         */
        if (this.cboProductID.getSelectedIndex() > 0) {
            clearProdFields();
            //Added BY Suresh
            String productType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            //System.out.println("productTypeproductType test1" + productType);
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && !productType.equals("GL")) {
                //System.out.println("test 11111111111");
                String prodId = "";
                prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                //System.out.println("prodIdprodIdprodIdprodId test1" + prodId);
                txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
            }
            observable.getAccountHeadForProductId(
                    (String) ((ComboBoxModel) (cboProductID.getModel())).getKeyForSelected());
            txtAccountHeadValue.setText(observable.getAccountHeadId());
            this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
            HashMap where = new HashMap();
            if (tblTransList.getRowCount() > 0) {
                String status = "";
                String act_num = CommonUtil.convertObjToStr(tblTransList.getValueAt(0, 0));
                String trans_type = CommonUtil.convertObjToStr(tblTransList.getValueAt(0, 4));
                where.put("ACT_NUM", act_num);
                where.put("PRODUCT_ID", (String) ((ComboBoxModel) (cboProductID.getModel())).getKeyForSelected());
                List lst = ClientUtil.executeQuery("getProdIdDetails", where);
                if (lst != null && lst.size() > 0) {
                    if (observable.getProductTypeValue().equals("OA")) {                         
                       // status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", where).get(0)).getSRemarks();
                        List statusLsit = ClientUtil.executeQuery("getOpAcctProductTOByProdId", where);
                        if(statusLsit != null && statusLsit.size() > 0){
                            status = ((OperativeAcctProductTO)statusLsit.get(0)).getSRemarks();
                        }
                        where = null;
                        where = (HashMap) lst.get(0);
                        String st = CommonUtil.convertObjToStr(where.get("S_REMARKS"));
                        //System.out.println("st : " + st);
                        //System.out.println("status : " + status);
                        if (!(st.equals("NRE") && trans_type.equals("DEBIT") && (observable.getTransType().equals(CommonConstants.CREDIT)))) {
                            if (!((((st.equals("REGULAR")) || (st.equals("NRO"))) && ((status.equals("REGULAR")) || (status.equals("NRO"))))
                                    || ((st.equals("NRE")) && (status.equals("NRE"))))) {
                                ClientUtil.displayAlert("Local Credits Not Allowed To NRE Accounts");
                                clearProdFields();
                                cboProductID.setSelectedItem("");
                            }
                        }
                    }
                } else {
                    String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
                    if (prodType.equals("OA")) {
                     //   status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", where).get(0)).getSRemarks();
                        List statusList=ClientUtil.executeQuery("getOpAcctProductTOByProdId", where);
                        if(statusList!=null && statusList.size()>0)
                        {
                           status= ((OperativeAcctProductTO)statusList.get(0)).getSRemarks();
                        }
                 System.out.println("status....is"+status+"where"+where);
                    }
                    if (status.equals("NRE") && !act_num.equals("")) {
                        ClientUtil.displayAlert("Local Credits Not Allowed To NRE Accounts");
                        clearProdFields();
                        cboProductID.setSelectedItem("");
                    }
                }

            }
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.DEBIT) && termDepositUI == null) {
                //                ADDED HERE BY NIKHIL FOR PARTIAL WITHDRAWAL
                String prodId = "";
                prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", prodId.toString());
                List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                //System.out.println("######## BEHAVES :" + behavesLiklst);
                if (behavesLiklst != null && behavesLiklst.size() > 0) {
                    prodMap = (HashMap) behavesLiklst.get(0);
                    depBehavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                    depPartialWithDrawalAllowed = CommonUtil.convertObjToStr(prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED"));
                    //System.out.println("$#%#$%#$%behavesLike:" + depBehavesLike);
                    if (depBehavesLike.equals("RECURRING")) {
                        btnViewTermLoanDetails.setVisible(true);
                    } else {
                        btnViewTermLoanDetails.setVisible(false);
                    }
                } else {
                    depBehavesLike = "";
                    depPartialWithDrawalAllowed = "";
                    btnViewTermLoanDetails.setVisible(true);
                }
                //System.out.println("depBehavesLike ====== " + depBehavesLike + " depPartialWithDrawalAllowed === " + depPartialWithDrawalAllowed);
                if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                    // Do nothing
                } else {
                    String specialRDScheme = "N";
                    if (depBehavesLike.equals("RECURRING")) {
                        HashMap roiParamMap = new HashMap();
                        roiParamMap.put("PROD_ID", (String) ((ComboBoxModel) (cboProductID.getModel())).getKeyForSelected());
                        List specialRDCompleteLst = ClientUtil.executeQuery("getSpecialRD", roiParamMap);
                        if (specialRDCompleteLst != null && specialRDCompleteLst.size() > 0) {
                            HashMap specialRDMap = (HashMap) specialRDCompleteLst.get(0);
                            if (specialRDMap.containsKey("SPECIAL_RD") && specialRDMap.get("SPECIAL_RD") != null) {
                                if (CommonUtil.convertObjToStr(specialRDMap.get("SPECIAL_RD")).equalsIgnoreCase("Y")) {
                                    System.out.println("Special RD.....");
                                    specialRDScheme = "Y";
                                }
                            }
                        }
                    }
                    if (specialRDScheme.equalsIgnoreCase("Y")) {
                        // do nothing
                    } else {
                        COptionPane.showMessageDialog(this, "Deposit Withdraw can be done using Deposit Closer.");
                        cboProductType.setSelectedIndex(0);
                        cboProductID.setModel(new ComboBoxModel());
                        txtAccountHeadValue.setText("");
                        return;
                    }
                }
            }
//            if (depBehavesLike.equals("DAILY") && observable.getTransType().equals(CommonConstants.DEBIT)) {
//                ClientUtil.displayAlert("Debit Transactions Not Allowed For This Product!!!");
//                cboProductType.setSelectedIndex(0);
//                cboProductID.setModel(new ComboBoxModel());
//                txtAccountHeadValue.setText("");
//                return;
//            }
            if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
                //                ADDED HERE BY NIKHIL FOR PARTIAL WITHDRAWAL
                String prodId = "";
                prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", prodId.toString());
                List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                //System.out.println("######## BEHAVES :" + behavesLiklst);
                if (behavesLiklst != null && behavesLiklst.size() > 0) {
                    prodMap = (HashMap) behavesLiklst.get(0);
                    depBehavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                    if (depBehavesLike.equals("RECURRING")) {
                        btnViewTermLoanDetails.setVisible(true);
                    } else {
                        btnViewTermLoanDetails.setVisible(false);
                    }
                } else {
                    btnViewTermLoanDetails.setVisible(true);
                }
            }


            if (!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                HashMap InterBranMap = new HashMap();
                InterBranMap.put("AC_HD_ID", observable.getAccountHeadId());
                List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
                InterBranMap = null;
                if (lst != null && lst.size() > 0) {
                    InterBranMap = (HashMap) lst.get(0);
                    String IbAllowed = CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
                    if (IbAllowed.equals("N")) {
                        ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
                        observable.resetTransactionDetails();
                    }
                }
            }
        } else {
            if (observable.getOperation() != ClientConstants.ACTIONTYPE_NEW) {
                if (txtAccountHeadValue.getText() != null && txtAccountHeadValue.getText().length() > 0) {
                    //observable.setAccountHeadId(txtAccountHeadValue.getText());
                    observable.getAccountHead();
                    this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
                }
            }
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    public ArrayList setOrgOrRespDetails() {
        HashMap hmap = new HashMap();
        ArrayList orgOrRespList = new ArrayList();

        hmap.put("OrgOrRespAdviceDt", getOrgOrRespAdviceDt());
        hmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
        hmap.put("OrgOrRespAmout", getOrgOrRespAmout());
        hmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
        hmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
        hmap.put("OrgBranchName", getOrgBranchName());
        hmap.put("ORG_BRANCH", getOrgBranch());
        hmap.put("OrgOrRespCategory", getOrgOrRespCategory());
        hmap.put("OrgOrRespDetails", getOrgOrRespDetails());
        hmap.put("OrgOrRespTransType", getOrgOrRespTransType());
        orgOrRespList.add(hmap);

        return orgOrRespList;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        //             if(prodType.equals("TL"))
        //             {
        //                 checkDocumentDetails();
        //             }
        // txtAmountFocusLost(null);
       
        //btnSave.setEnabled(false);//Commented by sreekrishnan
        String errorMessage = "";
        if (tblTransList.getRowCount() > 0) {
            observable.getTransDetails();
            System.out.print("flagGLAcc === " + flagGLAcc);
            if (!flagGLAcc) {
                lblAccNoGl.setText("");
                observable.setLinkBatchIdForGl("");
            }
            if (observable.getTotalCrAmount() != observable.getTotalDrAmount()) {
                //                COptionPane.showMessageDialog(this, resourceBundle.getString("SAVE_BATCH_TALLY"));
                errorMessage += resourceBundle.getString("SAVE_BATCH_TALLY");
            }
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());

            if (flag == true) {
                String dep = termDepositUI.getTransSomeAmt();
                if (dep != null && dep.equals("DEP_INTEREST_AMT")) {
                    observable.setDepInterestAmt("DEP_INTEREST_AMT");
                }
            }
//            if (observable.getTotalCrInstruments() > 1 && observable.getTotalDrInstruments() > 1) {
//                COptionPane.showMessageDialog(this, resourceBundle.getString("COUNT_BATCH_TALLY"));
//            } 
//            else {
            if (observable.isHoAccount()) {
                ArrayList alist = setOrgOrRespDetails();
                observable.setOrgRespList(alist);
                if (observable.getHoAccountStatus().equals("O")) {
                    if (getOrgOrRespBranchId().equals("") || getOrgOrRespDetails().equals("")) {
                        ClientUtil.displayAlert("Enter Orginating Details");
                        return;
                    }
                } else if (observable.getHoAccountStatus().equals("R")) {
                    if (getOrgBranch().equals("") || getOrgOrRespAdviceNo().equals("") || getOrgOrRespAdviceDt().equals("")) {
                        ClientUtil.displayAlert("Enter Responding Details");
                        return;
                    }
                }
            }
            if (errorMessage.length() > 0) {
                observable.setResult(ClientConstants.ACTIONTYPE_FAILED);
                COptionPane.showMessageDialog(this, errorMessage);
            } else {
                if (flagDeposit == true) {
                    double intAmt = observable.getTotalCrAmount();
                    txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                    //System.out.println("transfer UI interestAmount : " + intAmt);
                }
                if (flag == true) {
                    double intAmt = observable.getTotalCrAmount();
                    txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                    //System.out.println("transfer UI interestAmount : " + intAmt);
                }
                if (loanDebitType != null && loanDebitType.size() > 0) {
                    observable.setDebitLoanType(loanDebitType);
                }
                boolean interBranchFlag = observable.validationForInterbranchTxn();
                if (interBranchFlag) {
                    displayAlert("Incase of interbranch transaction either " + "\n" + "Dr or Cr account of the transaction should be of own branch");
                    return;
                }
                
                if (rdoBulkTransaction_Yes.isSelected() == true) {
                    doProcessBulkTransaction();//Added By Revathi
                }
                //boolean interbranchTLFlag = observable.interbranchTxnTLAccountCount();
                //if(interbranchTLFlag){
                //    displayAlert("dfdsf");
                //    return;
                //}
                //added by rishad 11/07/2015 for avoiding doubling issue
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        savePerformed();
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

            }
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                afterSaveCancel = true;
                btnCancelActionPerformed(null);
                if (termDepositUI != null) {
                    btnCloseActionPerformed(null);
                    if (!termDepositUI.getRenewalTransMap().equals("")) {
                        HashMap renewalMap = termDepositUI.getRenewalTransMap();
                        //System.out.println("transfer UI renewalMap : " + renewalMap);
                        if (renewalMap.containsKey("INT_AMT")) {
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.changePeriod();
//                                return;
                        } else if (renewalMap.containsKey("INTEREST_AMT_TRANSFER")) {
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.addingSomeAmt();
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_TRANSFER", "");
//                                return;
                        }
                        if (renewalMap.containsKey("DEPOSIT_AMT_TRANSFER")) {
                            termDepositUI.transactionCalling();
                        }
//                            if(renewalMap.containsKey("EXTENSION_DEPOSIT_AMT_TRANSFER")){
//                                termDepositUI.recalculationExtensionInterest();
//                            }
//                            if(renewalMap.containsKey("EXTENSION_INTEREST_TRANSFER")){
//                                termDepositUI.setRenewalTransMap(new HashMap());
//                                termDepositUI.extensionPeriodchanging();
//                            }
//                            if(renewalMap.containsKey("EXTENSION_RECOVERING_INT_TRANS")){
//                                termDepositUI.setRenewalTransMap(new HashMap());
//                                termDepositUI.extensionPeriodchanging();                                
//                            }
                    }
                }
                if(fromSmartCustUI){
                    //System.out.println("fromSmartCustUI#%#%"+fromSmartCustUI);
                    this.dispose();
                    fromSmartCustUI = false;
                }
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
            }
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
//            }
        } else {
            COptionPane.showMessageDialog(this, resourceBundle.getString("NO_ROWS"));
        }
        //        cboProductID.setModel(new ComboBoxModel());
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        transCommonUI = null;
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        lblAccNoGl.setText("");
        accNo1 = "";
        flagGLAcc = false;
    }//GEN-LAST:event_btnSaveActionPerformed
                                        private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                                            // Add your handling code here:
                                            // setup the operation and the status text
                                            observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
                                            observable.setStatus();
                                            lblStatusValue.setText(observable.getLblStatus());

                                            /*
                                             * open up the popu window for
                                             * showing all the records for
                                             * deletion pass one action
                                             * indicator as "Delete" which
                                             * specify that we want the deletion
                                             * operation in the popup window the
                                             * "Delete" operation do not require
                                             * to update the UI, so we are not
                                             * passing the TransferUI reference
                                             * to ActionPopupUI
                                             */
                                            actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_DELETE, this);//.show();
                                            showUpPop();
                                            this.populateUIData(ClientConstants.ACTIONTYPE_DELETE);
                                            txtAmount.setEditable(false);
                                            txtAccountNo.setEditable(false);
                                            setEnableDisableBulkTransaction(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void savePerformed() {
        if (backDatedTransDate != null && !backDatedTransDate.equals("") && observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
            if (authorizationCheckMap.size() == tblTransList.getRowCount()) {
                rejectionApproveUI = new RejectionApproveUI(this);
                if (rejectionApproveUI.isCancelActionKey()) {
                    return;
                }
                observable.checkForValueDate();
                observable.doAction(null);
            } else {
                COptionPane.showMessageDialog(this, resourceBundle.getString("NOT_VERIFIED"));
                return;
            }
        } else {
            observable.checkForValueDate();
            observable.doAction(null);
        }
    }
                     
    public void populateUIData(int operation) {
        if (this.batchIdForEdit != null) {
            HashMap hashmap1 = new HashMap();
            hashmap1.put("TRANS_ID", batchIdForEdit);
            hashmap1.put("BRANCH_ID", transactionInitBranForEdit);
            List lst = ClientUtil.executeQuery("getOrgRespDetails", hashmap1);
            if (lst != null && lst.size() > 0) {
                hashmap1 = (HashMap) lst.get(0);
                setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
                setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
                setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
                setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH")));
                setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH_NAME")));
                setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
                setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH_NAME")));
                setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
                setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
                setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
                btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
                btnOrgOrResp.setVisible(true);
                observable.setHoAccount(true);
            }
            observable.setOperation(operation);

            observable.setBatchId(batchIdForEdit);
            observable.setTransDate(CommonUtil.convertObjToStr(transactionDateForEdit));
            if (backDatedTransDate != null && !backDatedTransDate.equals("")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
                observable.setStatus();
                lblStatusValue.setText(observable.getLblStatus());
                observable.setBackDatedTransDate(backDatedTransDate);
            }
            observable.setInitBran(CommonUtil.convertObjToStr(transactionInitBranForEdit));
            rowForEdit = observable.getData(transactionIdForEdit);
            if (observable.displayWaive.length() > 0) {
                ClientUtil.showMessageWindow(observable.displayWaive);
            }
            this._intTransferNew = false;
            // update the account information also
            if (transactionIdForEdit != null) {
                updateAccountInfo();

                /*
                 * call the productID selection combo box action handler to
                 * update the corresponding values
                 */
                cboProductIDActionPerformed(null);

            }
            updateTransInfo();
            setupMenuToolBarPanel();

            // Enable all the screen
            if (operation == ClientConstants.ACTIONTYPE_EDIT) {
                setModified(true);
                //Added by sreekrishnan for slip printing with single trans id
                HashMap editMap = new HashMap();
                String singleTransId = "";
                editMap.put("TRANS_ID", batchIdForEdit);
                editMap.put("TRANS_DT", curr_dt);
                editMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List editList = ClientUtil.executeQuery("getSingleTransIdForEdit", editMap);
                if (editList != null && editList.size() > 0)  { 
                    editMap = (HashMap)editList.get(0);
                    if(editMap != null && editMap.containsKey("SINGLE_TRANS_ID")){
                        singleTransId = CommonUtil.convertObjToStr(editMap.get("SINGLE_TRANS_ID"));
                    }
                }
                ClientUtil.enableDisable(this, true);
                this.enableDisableButtons(true);
                ClientUtil.enableDisable(this.panTransDetails, false);
                this.btnAccountNo.setEnabled(false);
                this.btnAccountHead.setEnabled(false);

                if (transactionIdForEdit == null && batchIdForEdit != null) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    //System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap reportTransIdMap = new HashMap();
                        reportTransIdMap.put("TransId", singleTransId);
                        reportTransIdMap.put("TransDt", curr_dt);
                        reportTransIdMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(reportTransIdMap);
                        ttIntgration.integrationForPrint("ReceiptPayment", false);
                    }
                }
            } else {
                ClientUtil.enableDisable(this, false);
                this.enableDisableButtons(false);
            }
            if (((String) ((ComboBoxModel) this.cboInstrumentType.getModel()).getKeyForSelected()).equalsIgnoreCase("ONLINE_TRANSFER")) {

                this.tdtInstrumentDate.setEnabled(false);
                this.txtInstrumentNo1.setEnabled(false);
                this.txtInstrumentNo2.setEnabled(false);
                this.cboInstrumentType.setEnabled(false);
            }
            if (transactionIdForEdit == null && operation == ClientConstants.ACTIONTYPE_EDIT) {
                this.enableDisableButtons(false);
                this.btnAddCredit.setEnabled(true);
                this.btnAddDebit.setEnabled(true);
                ClientUtil.enableDisable(this, false);
            }
            observable.setStatus();
            lblStatusValue.setText(observable.getLblStatus());
//                                            }
            observable.asAnWhenCustomerComesYesNO(null, batchIdForEdit);
            if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                ClientUtil.enableDisable(panTransDetail, false);
                enableDisableButtons(false);
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
            observable.depositPenalReceving(batchIdForEdit);
            if (observable.getPenalMap() != null && observable.getPenalMap().containsValue("DEPOSIT_PENAL")) {
                ClientUtil.enableDisable(panTransDetail, false);
                enableDisableButtons(false);
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
        }
    }
    
    //Added By Revathi
    private void doProcessBulkTransaction() {
        if (tblTransList.getRowCount() > 0) {
            bulkProcess = true;
            TxTransferTO transferTo = new TxTransferTO();
            int size = this.observable.transferTOs.size();
            for (int i = 0; i < size; i++) {
                transferTo = (TxTransferTO) this.observable.transferTOs.get(i);
                if (transferTo.getProdType().equals("TL")) {
                    bulkRow = i;
                    tblTransListMouseClicked(null);
                    btnTransSaveActionPerformed(null);
                }
            }
        }
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.fillInstrumentType();
        this.cboInstrumentType.setModel(observable.getInstrumentTypeModel());
        observable.setIsBulkUploadTxtFile(false);
        // setup the operation and the status text
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());

        setModified(true);

        /*
         * now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        setupMenuToolBarPanel();
        lblTransDtValue.setText(DateUtil.getStringDate(observable.getCurDate()));
        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        this.lblCreatedByValue.setText(ProxyParameters.USER_ID);
        this.btnAddDebit.setEnabled(true);

        setEnableDisableBulkTransaction(true);
        rdoBulkTransaction_Yes.setSelected(false);
        rdoBulkTransaction_No.setSelected(true);
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        lblAccNoGl.setText("");
        btnAddCredit.setEnabled(true);
        returnWaiveMap = null;
       transDetails.setRepayData(null);
//       observable.setIsTransaction(false);
    }//GEN-LAST:event_btnAddActionPerformed

    private void setEnableDisableBulkTransaction(boolean flag) {
        ClientUtil.enableDisable(panBulkTransaction, flag);
        panBulkTransaction.setVisible(flag);
        lblBulkTransaction.setVisible(flag);

    }
//    public HashMap extensionTransferingInterestAmt(CTextField txtDepositAmount, HashMap intMap,TermDepositUI termDepositUI){
//        this.termDepositUI = termDepositUI;
//        flag = true;
//        observable.depositRenewalFlag = true;
//        observable.renewalIntFlag = true;
//        flagDepLink = true;
//        if(intMap.containsKey("transferingAmt")){
//            txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("AMOUNT")));
//        }
//        this.txtDepositAmount = txtDepositAmount;
//        transferDepMap = new HashMap();
//        transferDepMap = intMap;
//        System.out.println("$$$$$$$transferDepMap :" +transferDepMap);
//        if(intMap !=null){
//            System.out.println("%%%%%%intMap :"+intMap);
//            HashMap subNoMap = new HashMap();
//            btnAddActionPerformed(null);
//            btnAddDebitActionPerformed(null);
//            btnTransDelete.setEnabled(false);
//            btnAddDebit.setEnabled(false);
//            subNoMap.put("DEPOSIT_NO",intMap.get("ACCOUNTNO"));
//            subNoMap.put("CUST_ID",intMap.get("CUST_ID"));
//            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
//            if(lst.size()>0){
//                subNoMap = (HashMap)lst.get(0);
//                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
//                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
//                String accountNo = depositNo + "_" + subNo;
//                intMap.put("ACCOUNTNO",accountNo);
//                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
//                observable.setTransType("DEBIT");
//                cboProductTypeActionPerformed(null);
//                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                txtAccountNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                lblCustNameValue.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                lblCreatedByValue.setText(ProxyParameters.USER_ID);
//                double amount = CommonUtil.convertObjToDouble(intMap.get("INT_AMT")).doubleValue();
//                intMap.put("AMOUNT",intMap.get("INT_AMT"));
//                fillData(intMap);
//                txtAmount.setText(String.valueOf(amount));
//                ClientUtil.enableDisable(this,true);
//                btnTransSave.setEnabled(true);
//                btnSave.setEnabled(true);
//                txtAmount.setEditable(false);
//                cboProductType.setEnabled(false);
//                cboProductID.setEnabled(false);
//                txtAccountNo.setEditable(false);
//                btnAccountHead.setEnabled(false);
//                btnAccountNo.setEnabled(false);
//                cboInstrumentType.setSelectedItem("Voucher");
//                cboInstrumentType.setEnabled(false);
//                txtParticulars.setText("To "+txtAccountNo.getText());
//                txtParticulars.setEnabled(false);
//                btnCancel.setEnabled(false);
//                btnView.setEnabled(false);
//                if(flagDepLink == true){
//                    observable.setDepLinkBatchId("DEP_LINK");
//                    observable.setDepAccNO(accountNo);
//                }
//                intMap.put("TRANS_ID",observable.getDepositTransId());
//                System.out.println("###### IntMap : "+intMap);
//            }
//        }
//        return intMap;
//    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        // close the window and reclaim the memory
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
//    public HashMap transferingExtensionDepAmt(CTextField txtDepositAmount, HashMap intMap,TermDepositUI termDepositUI){
//        this.termDepositUI = termDepositUI;
//        flagDeposit = true;
//        observable.depositRenewalFlag = true;
//        flagDepLink = true;
//        this.txtDepositAmount = txtDepositAmount;
//        if(intMap.containsKey("transferingAmt")){
//            txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("AMOUNT")));
//        }
//        this.txtDepositAmount = txtDepositAmount;
//        transferDepMap = new HashMap();
//        transferDepMap = intMap;
//        System.out.println("$$$$$$$transferDepMap :" +transferDepMap);
//        if(intMap !=null){
//            System.out.println("%%%%%%intMap :"+intMap);
//            HashMap subNoMap = new HashMap();
//            btnAddActionPerformed(null);
//            btnAddDebitActionPerformed(null);
//            btnTransDelete.setEnabled(false);
//            btnAddDebit.setEnabled(false);
//            subNoMap.put("DEPOSIT_NO",intMap.get("ACCOUNTNO"));
//            subNoMap.put("CUST_ID",intMap.get("CUST_ID"));
//            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
//            if(lst.size()>0){
//                subNoMap = (HashMap)lst.get(0);
//                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
//                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
//                String accountNo = depositNo + "_" + subNo;
//                intMap.put("ACCOUNTNO",accountNo);
//                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
//                observable.setTransType("DEBIT");
//                cboProductTypeActionPerformed(null);
//                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                txtAccountNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                lblCustNameValue.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                lblCreatedByValue.setText(ProxyParameters.USER_ID);
//                txtAmount.setText(CommonUtil.convertObjToStr("0.0"));
//                fillData(intMap);
//                ClientUtil.enableDisable(this,true);
//                observable.setDepInterestAmt("");
//                btnTransSave.setEnabled(true);
//                btnSave.setEnabled(true);
//                btnCancel.setEnabled(false);
//                txtAmount.setEditable(true);
//                cboProductType.setEnabled(false);
//                cboProductID.setEnabled(false);
//                txtAccountNo.setEditable(false);
//                btnAccountNo.setEnabled(false);
//                cboInstrumentType.setSelectedItem("Voucher");
//                cboInstrumentType.setEnabled(false);
//                txtParticulars.setText("To "+txtAccountNo.getText());
//                txtParticulars.setEnabled(false);
//                btnView.setEnabled(false);
//                if(flagDepLink == true){
//                    observable.setDepLinkBatchId("DEP_LINK");
//                    observable.setDepAccNO(accountNo);
//                }
//                intMap.put("TRANS_ID",observable.getDepositTransId());
//                System.out.println("###### IntMap : "+intMap);
//            }
//        }
//        return intMap;
//    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        // setup the operation and the status text
        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);

        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());

        /*
         * open up the popu window for showing all the records for editing the
         * "Edit" operation requires to update the UI, so we are passing
         *
         *
         * TransferUI reference to ActionPopupUI
         */
        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_EDIT, this);//.show();
        showUpPop();

        /*
         * pass one action indicator as "Edit" which specify that we want the
         * edit operation in the popup window also we need to upload the data
         * for selected transactionId which was set for transferUI using the
         * reference we passed in the ActionPopupUI constructor
         */
        this.populateUIData(ClientConstants.ACTIONTYPE_EDIT);
        observable.setTransStatus(CommonConstants.STATUS_MODIFIED);
        txtAmount.setEditable(true);
        txtAccountNo.setEditable(false);
        setEnableDisableBulkTransaction(false);
    }//GEN-LAST:event_btnEditActionPerformed
    /*
     * This method is used to popup the window which will have some display
     * information
     */

    private void callView(int currField) {
        HashMap viewMap = new HashMap();

        viewType = currField;
        if (currField == ACT_NUM) {
            this.txtAccountNo.setText("");
            HashMap whereListMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountList" + ((ComboBoxModel) this.cboProductType.getModel()).getKeyForSelected());
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            if (observable.getTransType() != null) {
                String types = "TL";
                //                if(observable.getTransType().equals(CommonConstants.DEBIT) && types.equals(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString())){
                //                    whereListMap.put(CommonConstants.DEBIT, null);
                //                }
                if (observable.getTransType().equals(CommonConstants.CREDIT) && types.equals(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString())) {
                    whereListMap.put(CommonConstants.CREDIT, null);
                }
            }
            ArrayList presentActNums = new ArrayList();
            int rowCount = this.tblTransList.getRowCount();

            if (rowCount != 0) {
                String actNum = "";
                for (int i = 0; i < rowCount; i++) {
                    actNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
                    if (actNum != null && !actNum.equals("")) {
                        presentActNums.add(actNum);
                    }
                }
            }
            if (presentActNums != null) {
                whereListMap.put("ACT NUM", presentActNums);
            }
            whereListMap.put("BRANCH_SA", TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
            //System.out.println("viewMap:" + viewMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public void isAccountNumberExsistInAuthList(String actNumber) {
        if (actNumber != null && !actNumber.equals("")) {//Added By Nidhin
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM", actNumber);
            whereMap.put("CURR_DT", ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getNoOfUnauthorizedTransaction", whereMap);
            if (unAuthList != null && unAuthList.size() > 0) {
                ClientUtil.showMessageWindow(" There is Pending Transaction please Authorize OR Reject first ");
                whereMap = null;
                unAuthList = null;
                txtAccountNo.setText("");
                observable.setAccountNo("");
                return;
            }
        }
    }
    /*
     * this method will be called by the ViewAll class, to which we are passing
     * the UI class reference this will also set the account number and
     * operation value for OB
     */
    public void fillData(Object obj) {
        double depAmt = 0.0;
        HashMap hash = (HashMap) obj;
        observable.setReconcile("");
        //System.out.println("hash : " + hash);
        //added by sreekrishnan for kcc
        HashMap kccNatureMap = new HashMap();
        HashMap kccMap  = new HashMap();
        kccNatureMap.put("ACT_NUM", CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        //kccNatureMap.put("PROD_ID", hash.get("PROD_ID"));
        List kccList = ClientUtil.executeQuery("getKccNature", kccNatureMap);
        if (kccList != null && kccList.size() > 0) {
            kccMap=(HashMap)kccList.get(0);
            if(kccMap.containsKey("KCC_NATURE") && 
                kccMap.get("KCC_NATURE")!=null && kccMap.get("KCC_NATURE").equals("Y")){
       			observable.setKccNature("Y");
               //kccRenewalChecking(hash);
           }else{
               observable.setKccNature("N");
                //kccFlag = false;
           }
         }else{
                observable.setKccNature("N");
			//kccFlag = false;
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            authorizeStatus();
            btnRejection.setEnabled(false);
            rejectFlag = 1;
            tblTransList.requestFocus(true);
            tblTransList.changeSelection(0, 5, false, false);
            btnAuthorize.requestFocus(false);
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            authorizeStatus();
            btnRejection.setEnabled(false);
            rejectFlag = 1;
            tblTransList.requestFocus(true);
            tblTransList.changeSelection(0,5,false, false);
            btnAuthorize.requestFocus(false);
          }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI") || hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
              if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
                authorizeListDebitUI = (AuthorizeListDebitUI) hash.get("PARENT");
                fromAuthListDebit=true;
              }
              if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")){
                authorizeListCreditUI = (AuthorizeListCreditUI) hash.get("PARENT");
                fromAuthListCredit=true;
              }
              hash.remove("PARENT");
              viewType = AUTHORIZE;
              observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
              observable.setStatus();
              setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
              authorizeStatus();
              chkVerifyAll.setEnabled(true);
              btnRejection.setEnabled(true);
              rejectFlag = 1;
              tblTransList.requestFocus(true);
              tblTransList.changeSelection(0,5,false, false);
              btnAuthorize.requestFocus(false);
            }
        if (hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
            fromSmartCustUI= true;
            smartUI = (SmartCustomerUI) hash.get("PARENT");
            hash.remove("PARENT");
            btnAddActionPerformed(null); 
            if (hash.containsKey("TRANS_TYPE") && hash.get("TRANS_TYPE").equals("CREDIT")) {
                btnAddCreditActionPerformed(null);
            }else{
                btnAddDebitActionPerformed(null);
            }
            txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            txtAccountNoFocusLost(null); 
            viewType = -1;
            //viewType = ACT_NUM;
            observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
            observable.setStatus();
              //btnSaveDisable();
        }
        //System.out.println("viewType$$$$$DSDSDSD" + viewType);
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        observable.setProductId((String) ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        if (viewType == ACT_NUM || viewType == VIEW) {
            String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
            //System.out.println("ACCOUNTNO$@$@$" + ACCOUNTNO);
            //System.out.println("prodType$@$@$@" + prodType);
            if (prodType.equals("TD") && viewType == ACT_NUM && ACCOUNTNO.lastIndexOf("_") == -1) {
                ACCOUNTNO = ACCOUNTNO + "_1";
            }
            observable.setAccountNo(ACCOUNTNO);
            //            setCustHouseName(ACCOUNTNO);
            lblHouseName.setText(CommonUtil.convertObjToStr(hash.get("HOUSENAME")));
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO, true)) {
                setSelectedBranchID(observable.getSelectedBranchID());
            }
            observable.setAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            observable.setTransDepositAmt(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            observable.setReconcile("N");
            // Check for existance of account no. in transfer list
            morethanOneDeposit = false;
            if (observable.isAccountNoExists(ACCOUNTNO, false)) {
                flagv = 1;
                displayAlert("This Account no. already present in this batch...");
                //morethanOneDeposit = true;
                // return;
            }
             //Added by sreekrishnan Check authorization pending for overdraft
            if (viewType == ACT_NUM && observable.getOperation()==(ClientConstants.ACTIONTYPE_NEW)) {
                if (prodType.equals(CommonConstants.TXN_PROD_TYPE_LOANS)) {//Added By Nidhin for check pending authrization
                    isAccountNumberExsistInAuthList(ACCOUNTNO);
                }
                if(prodType.equals(CommonConstants.TXN_PROD_TYPE_SUSPENSE)){
                    issuspenseAcctExsistInAuthList(ACCOUNTNO);
                }
                // modified by rishad 08/july/2019 for jirra KDSA-532  OD corruption
   //                observable.getTransType().equals(CommonConstants.DEBIT) &&
                if (prodType.equals(CommonConstants.TXN_PROD_TYPE_ADVANCES)) {
                    if (observable.isAuthorizationPending(ACCOUNTNO)) {
                        ClientUtil.showMessageWindow("There is Pending Transaction please Authorize OR Reject first  ");
                        observable.setAccountNo("");
                        txtAccountNo.setText("");
                        lblCustNameValue.setText("");
                        lblHouseName.setText("");
                        return;
                    }
                }
            }
           
            
            /*
             * we have to update the account information values also
             */
            updateAccountInfo();
            //            observable.checkOAbalanceForTL(); dont delete becase if product level enable means it s working
            observable.notifyObservers();
            if (isRowClicked) {
                cboProductType.setEnabled(false);
                cboProductID.setEnabled(false);
                txtAccountHeadValue.setEnabled(false);
                btnAccountHead.setEnabled(false);
                cboInstrumentType.setSelectedItem("");
                txtInstrumentNo1.setText("");
                txtInstrumentNo2.setText("");
                txtAmount.setText("");
                txtParticulars.setText("");
                tdtInstrumentDate.setDateValue("");
                isRowClicked = false;
            }
            //Added By Suresh
            if (viewType == ACT_NUM) {
                if (prodType.equals("TD")) {
                    if (depBehavesLike.equals("RECURRING")) {
                        if (transDetails.isClearTransFlag() == true) {
                            txtAccountNo.setText("");
                            txtAmount.setText("");
                            lblCustNameValue.setText("");
                            lblHouseName.setText("");
                        }
                    }

                    if (depBehavesLike.equals("RECURRING") && recurringCredit == true && viewType != AUTHORIZE) {

                        //   if (cboProdId.getSelectedItem().equals("Recurring Deposit") || cboProdId.getSelectedItem().equals("Staff R.d") || cboProdId.getSelectedItem().equals("Time Deposit") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {
                        //Added By Suresh 
                        String remark = COptionPane.showInputDialog(this, "INSTALLMENT AMOUNT : Rs " + CommonUtil.convertObjToDouble(hash.get("AMOUNT")) + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                        //                        int remark=ClientUtil.confirmationAlert("NO OF INSTALLMENT WANT TO PAY");
                        //System.out.println("remark" + remark);
                        if (CommonUtil.convertObjToStr(remark).equals("")) {
                            remark = "0";
                        }
                        try {
                            noofInstallment2 = Long.parseLong(remark);
                          observable.setInstallMentNo(CommonUtil.convertObjToInt(noofInstallment2));
                        } catch (java.lang.NumberFormatException e) {
                            ClientUtil.displayAlert("Invalid Number...");
                            txtAccountNoFocusLost(null);
                            return;
                        }
                        //                        noofInstallment=0;
                    }
                    
                    // Added by nithya on 31-03-2020 for KD-1535
                    if (depBehavesLike.equals("RECURRING") && CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.DEBIT) && viewType != AUTHORIZE) {
                      HashMap roiParamMap = new HashMap();
                            roiParamMap.put("DEPOSIT_NO", ACCOUNTNO.substring(0, ACCOUNTNO.lastIndexOf("_")));
                            roiParamMap.put("PROD_ID",  ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString());
                            List specialRDCompleteLst = ClientUtil.executeQuery("getSpecialRDCompletedStatus", roiParamMap);
                            if(specialRDCompleteLst != null && specialRDCompleteLst.size() > 0){
                                HashMap specialRDMap = (HashMap)specialRDCompleteLst.get(0);
                                if(specialRDMap.containsKey("COMPLETESTATUS") && specialRDMap.get("COMPLETESTATUS") != null){
                                    if(CommonUtil.convertObjToStr(specialRDMap.get("COMPLETESTATUS")).equalsIgnoreCase("N")){
                                       ClientUtil.displayAlert("Special RD Receipt not completed. Payment Cannot be done.");
                                       btnCancelActionPerformed(null);
                                    }
                                }
                            }
                    }
                    // End
                    
                    if (depBehavesLike.equals("DAILY") && CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.DEBIT) && depPartialWithDrawalAllowed.equals("Y") && viewType != AUTHORIZE) {
                         HashMap checkMap = new HashMap();
                        checkMap.put("DEPOSIT_NO", ACCOUNTNO.substring(0, ACCOUNTNO.lastIndexOf("_")));
                        List checkForLienList = ClientUtil.executeQuery("getIsGroupDepositExistsForLoan", checkMap);
                        if (checkForLienList != null && checkForLienList.size() > 0) {
                            ClientUtil.showMessageWindow("Lien exists for this deposit");
                        }
                    }
                    
                    //system.out.println("noofInstallment2>>>>@@@111@@@>>>" + noofInstallment2);
                    if (noofInstallment2 != 0) {
                        transDetails.setTransDetails(observable.getProductTypeValue(), TrueTransactMain.selBranch, ACCOUNTNO, noofInstallment2);
                    }
                }
                if (prodType.equals("OA") && CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.DEBIT)) {//If condition changed By Revathi.L Mantis ID (0006730-Over Due message only debit transaction) 
                    boolean OverDue = false;
                    HashMap operativeMap = new HashMap();
                    String act_num = observable.getAccountNo();

                    if (act_num == null || act_num.equals("")) {
                        act_num = txtAccountNo.getText();
                    }
                    operativeMap.put("ACT_NUM", act_num);
                    List allCustLst = ClientUtil.executeQuery("getCustIdfromMembershipLiability", operativeMap);
                    if (allCustLst != null && allCustLst.size() > 0) {
                        HashMap SingleMap = new HashMap();
                        SingleMap = (HashMap) allCustLst.get(0);
                        SingleMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        List dueList = ClientUtil.executeQuery("getSelectMembershipLiabilityDetails", SingleMap);
                        double dueAmt = 0.0;
                        if (dueList != null && dueList.size() > 0) {
                            for (int i = 0; i < dueList.size(); i++) {
                                HashMap OverDueMap = new HashMap();
                                OverDueMap = (HashMap) dueList.get(i);
                                dueAmt = CommonUtil.convertObjToDouble(OverDueMap.get("PRINC_DUE"));
                                if (dueAmt > 0) {
                                    i = dueList.size();
                                    OverDue = true;
                                }
                            }
                        }
                    }
                    if (OverDue) {
                        ClientUtil.showAlertWindow("Overdue Loans In A/C, Click Membership Liability Button For More Details");
                    }
                }
                if (getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(getSelectedBranchID())) {
                    Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(getSelectedBranchID());
                    Date currentDate = (Date) curr_dt.clone();
                    //System.out.println("selectedBranchDt : " + selectedBranchDt + " currentDate : " + currentDate);
                    if (selectedBranchDt == null) {
                        ClientUtil.displayAlert("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                        txtAccountNo.setText("");
                        return;
                    } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) < 0) {
                        ClientUtil.displayAlert("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed");
                        txtAccountNo.setText("");
                        return;
                    } else {
                        System.out.println("Continue for interbranch trasactions ...");
                    }
                }
                //System.out.println("observable.getProductTypeValue()" + observable.getProductTypeValue());
                if (recurringCredit == true && observable.getProductTypeValue().equals("TD")) {
                    double shadowCredit = CommonUtil.convertObjToDouble(transDetails.getShadowCredit()).doubleValue();
                    if (shadowCredit > 0) {
                        ClientUtil.showAlertWindow("Already Transaction is completed, pending for authorization...");
                        btnCancelActionPerformed(null);
                        return;
                    } else {
                        double txtAmt = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
                        //System.out.println("txtAmt==" + txtAmt);
                        depAmt = CommonUtil.convertObjToDouble(transDetails.getDepositAmt()).doubleValue();
                        //System.out.println("depAmt==" + depAmt);
                        double balance = txtAmt - depAmt;
                        //System.out.println("balance===" + balance);
                        if (balance > 0) {
                            //system.out.println("hvbhjvdbb666");
                            observable.setAmount(String.valueOf(balance));
                            txtAmount.setText(String.valueOf(balance));
                        }
                    }
                }
                if (depBehavesLike.equals("RECURRING") && recurringCredit == true && viewType != AUTHORIZE) {
                    HashMap recurringMap = new HashMap();
                    HashMap dailyProdID = new HashMap();
                    String prId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                    int freq = 0;
                    dailyProdID.put("PID", prId);
                    List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                    if (dailyFrequency != null && dailyFrequency.size() > 0) {
                        HashMap dailyFreq = new HashMap();
                        dailyFreq = (HashMap) dailyFrequency.get(0);
                        String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                        freq = CommonUtil.convertObjToInt(daily);
                        if (freq == 7) { //If Condition Added By Suresh R   11-Jul-2017
                            String depNo = txtAccountNo.getText();
//                 double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                            //System.out.println(" ####DepNo12123 :" + depNo);
                            if (depNo.contains("_")) {
                                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                            }
                            //System.out.println(" ####DepNo2222 :" + depNo);
                            recurringMap.put("DEPOSIT_NO", depNo);
                            List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                            if (lst != null && lst.size() > 0) {
                                recurringMap = (HashMap) lst.get(0);
                                double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                                // double receivablAmt=depAmount+penalAmt;//vv
                                //  System.out.println("penalAmt????@@>>>>"+penalAmt);
                                HashMap delayMap = new HashMap();
                                double delayAmt = 0.0;
                                delayMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString());
                                delayMap.put("DEPOSIT_AMT", recurringMap.get("DEPOSIT_AMT"));
                                lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                if (lst != null && lst.size() > 0) {
                                    delayMap = (HashMap) lst.get(0);
                                    delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                    //System.out.println("delayAmt@@1111 in cash>>>>>" + delayAmt);
                                }
                                HashMap hmap = new HashMap();
                                String instalPendng = "";
                                double actualDelay = 0.0;
                                //   System.out.println("acctNo iv>>>"+acctNo);
                                //  String depNo = acctNo;
                                //System.out.println(" ####DepNo ivv in cash>>>>:" + depNo);
                                //depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                                // System.out.println("######## BHEAVES iv in cash>>>>:" + depNo);
                                hmap.put("ACC_NUM", depNo);
                                hmap.put("CURR_DT", curr_dt.clone());
                                hmap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                                //  List list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                //Added by chithra for mantis :10319: Weekly RD customisation for Ollukkara bank
                                prId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                                List list = null;
                                freq = 0;
                                if (depBehavesLike != null && depBehavesLike.equals("RECURRING")) {
                                    dailyProdID = new HashMap();
                                    dailyProdID.put("PID", prId);
                                    dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                                    if (dailyFrequency != null && dailyFrequency.size() > 0) {
                                        dailyFreq = new HashMap();
                                        dailyFreq = (HashMap) dailyFrequency.get(0);
                                        daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                                        freq = CommonUtil.convertObjToInt(daily);
                                        if (freq == 7) {
                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiForWeek", hmap);
                                        } else {
                                            //Added by sreekrishnan for mantis 10452
                                            HashMap paramMap = new HashMap();
                                            List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                            if (paramList != null && paramList.size() > 0) {
                                                paramMap = (HashMap) paramList.get(0);
                                                if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                                } else {
                                                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                                }
                                            } else {
                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                            }
                                        }
                                    } else {
                                        //Added by sreekrishnan for mantis 10452
                                        HashMap paramMap = new HashMap();
                                        List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                        if (paramList != null && paramList.size() > 0) {
                                            paramMap = (HashMap) paramList.get(0);
                                            if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                            } else {
                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                            }
                                        } else {
                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                        }
                                    }

                                }
                                if (list != null && list.size() > 0) {
                                    hmap = (HashMap) list.get(0);
                                    instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                                    actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
                                    //  actualDelay1=actualDelay;
                                    //System.out.println("actualDelay iv in cash>>>" + actualDelay);
                                }
                                if (actualDelay > 0) {
                                    HashMap whr = new HashMap();
                                    whr.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
                                    List irRDlst = ClientUtil.executeQuery("getIrregularRDApply", whr);
                                    if (irRDlst != null && irRDlst.size() > 0) {
                                        HashMap singMap = (HashMap) irRDlst.get(0);
                                        if (singMap != null && singMap.containsKey("RD_IRREGULAR_INSTALLMENTS_DUE")) {
                                            actualDelay = actualDelay - CommonUtil.convertObjToDouble(singMap.get("RD_IRREGULAR_INSTALLMENTS_DUE"));
                                        }
                                    }
                                }
                                //Commented by nithya on 12-07-2019 for KD 551 - Need to allow future installment for weekly RD
//                                if (freq == 7) {
//                                    if (noofInstallment2 > actualDelay + 1) {
//                                        ClientUtil.showMessageWindow("No Future Receipts Allowed");
//                                        txtAmount.setText("");
//                                        return;
//                                    }
//                                }
                                double cummInst = 0.0;
                                if (actualDelay == noofInstallment2) {
                                    cummInst = actualDelay * (actualDelay + 1) / 2;
                                } else {
                                    double diff = actualDelay - noofInstallment2;
                                    cummInst = (actualDelay * (actualDelay + 1) / 2) - (diff * (diff + 1) / 2);
                                }
                                //  double penal=0.0;
                                penal = depAmount * cummInst * delayAmt / 100;
                                String round = transDetails.getRound();
                                //System.out.println("penal in cashhhh??>>" + penal);


                                if (round.equals("NEAREST_VALUE")) {
                                    penal = (double) getNearest((long) (penal * 100), 100) / 100;
                                } else if (round.equals("LOWER_VALUE")) {
                                    penal = (double) roundOffLower((long) (penal * 100), 100) / 100;
                                } else if (round.equals("HIGHER_VALUE")) {
                                    penal = (double) higher((long) (penal * 100), 100) / 100;
                                } else {
                                    //system.out.println(" in no round33333");
                                    penal = new Double(penal);
                                    //system.out.println("maturityAmt 3333" + maturityAmt);
                                }
                                //System.out.println("penalllllllllllllllllll" + penal);
                                double receivablAmt = depAmount * noofInstallment2;
                                //  double totAmt1=0.0;
                                //System.out.println("noofInstallment2111>>>???>>>" + noofInstallment2);
                                if (penal > 0) {
                                    totAmt1 = receivablAmt + penal;
                                } else {
                                    totAmt1 = receivablAmt;
                                }
                                //System.out.println("totAmt1???>>>" + totAmt1);
                                txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));
                                //System.out.println("txtAmount.getText??>>>??>>>" + txtAmount.getText());
                            }
                        }else if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW)) {
                            double amts = CommonUtil.convertObjToDouble((hash.get("AMOUNT")));
                            //system.out.println("amts" + amts);
                            double amt2 = (amts) * noofInstallment2;
                            //System.out.println("###### Final Amount : " + amt2);
                            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                            //System.out.println("###### Penal Amount : " + penalAmt);
                            if (penalAmt > 0 && amt2 > 0) {
                                amt2 = amt2 + penalAmt;
                            }
                            System.out.println("###### Final Amount 1 : " + amt2);
                            if (amt2 != 0 || amt2 != 0.0) {
                                observable.setTransferAmount(CommonUtil.convertObjToStr(amt2));
                                txtAmount.setText(observable.getTransferAmount());
                            } else {
                                observable.setTransferAmount("");
                                txtAmount.setText(observable.getTransferAmount());
                            }
                        }
                    }
                    transDetails.setSourceFrame(this); // Added by nithya on 30-04-2021 for KD-2844
                }

                if (observable.getProductTypeValue().equals("TL")) {
                    HashMap retmpap = setRepaymentData();
                    if (retmpap != null && retmpap.size() > 0) {
                        transDetails.setRepayData(retmpap);
                    } else {
                        transDetails.setRepayData(null);
                    }
                } else {
                    transDetails.setRepayData(null);
                }
            }
            String act_num = observable.getAccountNo();
            HashMap inputMap = new HashMap();
            inputMap.put("ACCOUNTNO", act_num);
            chqBalList = ClientUtil.executeQuery("getMinBalance", inputMap);
            HashMap lockmap = new HashMap();
            lockmap.put("ACCOUNTNO", act_num);
            List lockList = ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
            lockmap = null;
            if (lockList != null && lockList.size() > 0) {
                lockmap = (HashMap) lockList.get(0);
                String lockStatus = CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
                if (lockStatus.equals("Y")) {
                    ClientUtil.displayAlert("Account is locked");
                    txtAccountNo.setText("");
                }
            }

            if (CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.CREDIT) && CommonUtil.convertObjToStr(prodType).equals("OA")
                    && CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.COMPLETE_FREEZE)) {
                ClientUtil.displayAlert("This account has been freezed completely,\n cannot make transaction either debit or credit\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                txtAccountNo.setText("");
                return;
            } else if (CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.DEBIT) && CommonUtil.convertObjToStr(prodType).equals("OA")) {
                if (CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.COMPLETE_FREEZE)) {
                    ClientUtil.displayAlert("This account has been freezed completely,\n cannot make transaction either debit or credit\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                    txtAccountNo.setText("");
                    return;
                } else if (CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.PARTIAL_FREEZE)) {
                    ClientUtil.displayAlert("This account has been freezed partially,\n cannot make debit transaction\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                    //txtAccountNo.setText("");
                    //return;
                }
            }
            
             //Added by sreekrishnan
            //System.out.println("observable.getProdType()###@@@"+prodType);
            if ((CommonUtil.convertObjToStr(prodType).equals(CommonConstants.TXN_PROD_TYPE_ADVANCES) || CommonUtil.convertObjToStr(prodType).equals(CommonConstants.TXN_PROD_TYPE_LOANS)) && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap hmap = new HashMap();
                Date toDate = new Date();
                hmap.put("ACCOUNTNO",txtAccountNo.getText());       
                List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                if (kccList1 != null && kccList1.size() > 0) {
                    hmap = (HashMap) kccList1.get(0);
                    toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                    //System.out.println("toDate###"+toDate);
                    if(!toDate.equals("")){
                        if (toDate.before((Date) curr_dt.clone())) {
                            if(CommonUtil.convertObjToStr(prodType).equals(CommonConstants.TXN_PROD_TYPE_LOANS)){
                                    displayAlert("Loan Period is Over");
                                }else{
                                    displayAlert("Advance Period is Over");
                                }
                        }
                    }
                }
            }
        } else if (viewType == ACCTHDID) {
            setSelectedBranchID(TrueTransactMain.selBranch);
            cboProductID.setSelectedItem("");
            this.txtAccountNo.setText("");
            btnAccountNo.setEnabled(false);
            //cboProductID.setSelectedItem("");
            String acHdId = hash.get("A/C HEAD").toString();
            String customerAllow = "";
            String hoAc = "";
            String bankType = CommonConstants.BANK_TYPE;
            this.txtAccountHeadValue.setText((String) hash.get("A/C HEAD"));
            observable.setBalanceType((String) hash.get("BALANCETYPE"));
            if (observable.isAccountNoExists((String) hash.get("A/C HEAD"), true)) {
                displayAlert("This Account head already present in this batch...");
                //return;
            }
            if (flagv != 1) {
                HashMap hmap = new HashMap();
                hmap.put("ACHEAD", acHdId);
                List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
                if (list != null && list.size() > 0) {
                    hmap = (HashMap) list.get(0);
                    customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                    hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
                }
                if (bankType.equals("DCCB")) {
                    if (hoAc.equals("Y")) {
                        btnOrgOrResp.setVisible(true);
                        observable.setHoAccount(true);
                        observable.setHoAccountStatus(btnOrgOrResp.getText());

                    } else {
                        observable.setHoAccount(false);
                        btnOrgOrResp.setVisible(false);
                    }
                }
                if (customerAllow.equals("Y")) {
                    //System.out.println("innnnn");
                    flagGLAcc = true;
                    CInternalFrame frm = new CInternalFrame();
                    frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                    frm.setSelectedBranchID(getSelectedBranchID());
                    //frm.setSize(1000,1000);
                    TrueTransactMain.showScreen(frm);
//                    String AccNo = COptionPane.showInputDialog(this,"Enter Acc no");
//                    hmap.put("ACC_NUM",AccNo);
//                    List chkList=ClientUtil.executeQuery("checkAccStatus", hmap);
//                    if(chkList!=null && chkList.size()>0){
//                        hmap=(HashMap)chkList.get(0);
//                        lblCustNameValue.setText(CommonUtil.convertObjToStr(hmap.get("NAME")));
//                        txtAccountNo.setText(AccNo);
//                        observable.setClosedAccNo(AccNo);
//                    }else{
//                        ClientUtil.displayAlert("Invalid Account number");
//                        txtAccountHeadValue.setText("");
//                        return;
//                    }
                }
            }

            this.lblAccountHeadDescValue.setText((String) hash.get("A/C HEAD DESCRIPTION"));
            observable.setAccountHeadId((String) hash.get("A/C HEAD"));
            // Check for existance of account no. in transfer list
//            if (observable.isAccountNoExists((String) hash.get("A/C HEAD"), true)){
//                displayAlert("This Account head already present in this batch...");
//                return;
//            }
            if (!hash.get("RECONCILLIATION").equals("") && hash.get("RECONCILLIATION").equals("Y")) {
                if (transCommonUI != null) {
                    transCommonUI.dispose();
                    transCommonUI = null;
                }
            }
            updateAccountInfo();
        } else if (viewType == RECON) {
            txtAmount.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
        }
        if (rejectFlag == 1) {
            btnRejection.setEnabled(false);
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public void setCustHouseName(String actNum) {
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
            if (!prodType.equals("") && !prodType.equals("GL")) {
                HashMap whereMap = new HashMap();
                whereMap.put("ACC_NUM", actNum);
                List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName", whereMap);
                if (custHouseNameList != null && custHouseNameList.size() > 0) {
                    whereMap = (HashMap) custHouseNameList.get(0);
                    lblHouseName.setText(CommonUtil.convertObjToStr(whereMap.get("HOUSE_NAME")));
                    //System.out.println("####### whereMap: " + whereMap);
                }
            }
        }
    }

    public void setServiceTax(String taxAmt, String totAmt, String overDueCharge,String paidInst) {   //Added By Kannan AR
        lblServiceTaxval.setText(taxAmt);
        txtAmount.setText(totAmt);
        observable.setLblServiceTaxval(taxAmt);
        observable.setTransferAmount(totAmt);


        observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
        observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT", new Long(paidInst));
        //System.out.println("observable.setALL_LOAN_AMOUNT  "+observable.getALL_LOAN_AMOUNT());

        // //System.out.println("taxAmt ---------- :" + taxAmt);
        // //System.out.println("totAmt ---------- :" + totAmt);
        //observable.ttNotifyObservers();
    }


    /*
     * use this method to get the customer informatio and update the name and
     * address for the customer in the Account Details screen
     */
    public void updateAccountInfo() {
        String emiInSimpleInterest = "N";// Added by nithya on 19-05-2020 for KD-380
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
            if (!prodType.equals("")) {
                //System.out.println("txtaccountno##" + txtAccountNo.getText());
            }
            //System.out.println("hereTTTTTTTTTTTTTT :"+observable.getOperation());
            String actNum = observable.getAccountNo() != null ? observable.getAccountNo() : txtAccountHeadValue.getText();
            HashMap mapHash = new HashMap();
            long noofInstallment = 0;
            //            if (prodType.equals("TL") || prodType.equals("AD")) {
            if (observable.getTransType().equals("CREDIT")) {
                mapHash = observable.asAnWhenCustomerComesYesNO(actNum, null);
            }
            if (mapHash != null && mapHash.size() > 0) {
                //                if(observable.getOperation()==ClientConstants.ACTIONTYPE_NEW)
                  // Added by nithya on 19-05-2020 for KD-380
            if (mapHash.containsKey("EMI_IN_SIMPLEINTREST") && mapHash.get("EMI_IN_SIMPLEINTREST")!= null && mapHash.get("EMI_IN_SIMPLEINTREST").equals("Y")){
                emiInSimpleInterest = "Y";
                observable.setEMIinSimpleInterest("Y");
            }else{
                observable.setEMIinSimpleInterest("N");
            }
            // End
                
                HashMap transMap = new HashMap();
                if (observable.getTransferTOs() != null && observable.getTransferTOs().size() > 0) {
                    String batch_id = observable.getTermLoanBatch_id();
                    if (batch_id != null && batch_id.length() > 0) {
                        transMap.put("BATCH_ID", batch_id);
                        transMap.put("LINK_BATCH_ID", actNum);
                        transMap.put("TRANS_DT", curr_dt);
                        transMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                        List pendingList = ClientUtil.executeQuery("getPendingTransactionTL", transMap);
                        if (pendingList != null && pendingList.size() > 0) {
                            HashMap hash = (HashMap) pendingList.get(0);
                            String trans_actnum = CommonUtil.convertObjToStr(hash.get("LINK_BATCH_ID"));
                            if (trans_actnum.equals(actNum)) {
                                ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first ");
                                txtAccountNo.setText("");
                                observable.setAccountNo("");
                                hash = null;
                                pendingList = null;
                                return;
                            }
                        }
                    }
                }

                String remark = "";
                if (observable.getAuthorizeRemarks() != null && observable.getAuthorizeRemarks().length() > 0 && CommonUtil.convertObjToInt(observable.getAuthorizeRemarks()) > 0) {
                    remark = observable.getAuthorizeRemarks();
                }
                 if (prodType.equals("TL") || prodType.equals("AD")) {
                      if (mapHash != null && mapHash.containsKey("INSTALL_TYPE")){
                      observable.setInstalType(CommonUtil.convertObjToStr(mapHash.get("INSTALL_TYPE")));}
                     }
                if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") ||mapHash.get("INSTALL_TYPE").equals("USER_DEFINED")|| mapHash.get("INSTALL_TYPE").equals("EYI")
                        || mapHash.get("INSTALL_TYPE").equals("LUMP_SUM"))) && observable.getTransType().equals(CommonConstants.CREDIT) && viewType != this.AUTHORIZE && emiInSimpleInterest.equalsIgnoreCase("N")) {
                    if (remark.length() == 0) {
                       // remark = COptionPane.showInputDialog(this, "NO OF INSTALLMENT WANT TO PAY");
                    
                    String loanNo = CommonUtil.convertObjToStr(actNum);
                    HashMap loanMap = new HashMap();
                    loanMap.put("Act_Num", loanNo);
                    String detail = "";
                    List lst = ClientUtil.executeQuery("getLoanAmountForEmi", loanMap);
                    if (lst != null && lst.size() > 0) {
                        loanMap = (HashMap) lst.get(0);

                        detail = CommonUtil.convertObjToStr(loanMap.get("DETAIL"));
                    }
                    remark = COptionPane.showInputDialog(this, "INSTALLMENT DETAILS:" + "\n" + detail + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                    }

                    //System.out.println("remark" + remark);
                    if (CommonUtil.convertObjToStr(remark).equals("")) {
                        remark = "0";
                    }
                    try {
                        noofInstallment = Long.parseLong(remark);
                        observable.setEmiNoInstallment(noofInstallment);
                    } catch (java.lang.NumberFormatException e) {
                        ClientUtil.displayAlert("Invalid Number...");
                        txtAccountNoFocusLost(null);
                        return;
                    }
                }
                
                 // Added by nithya on 19-05-2020 for KD-380
                if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && mapHash.get("INSTALL_TYPE").equals("EMI") &&  observable.getTransType().equals(CommonConstants.CREDIT) && viewType != AUTHORIZE && emiInSimpleInterest.equalsIgnoreCase("Y")) {
                   
                    HashMap loanMap = new HashMap();
                    loanMap.put("ACCT_NUM", actNum);
                    loanMap.put("CURR_DT", curr_dt.clone());
                    double currentPrincipleDue = 0.0;
                    List emiPricipalDueList = ClientUtil.executeQuery("getEMIInSimpleInterestPrincipleDue", loanMap);
                    if (emiPricipalDueList != null && emiPricipalDueList.size() > 0) {
                        HashMap emiPricipalDueMap = (HashMap) emiPricipalDueList.get(0);
                        if (emiPricipalDueMap.containsKey("PRINCIPAL_DUE") && emiPricipalDueMap.get("PRINCIPAL_DUE") != null) {
                            currentPrincipleDue = CommonUtil.convertObjToDouble(emiPricipalDueMap.get("PRINCIPAL_DUE"));
                            ClientUtil.showMessageWindow("Principal Due :: " + currentPrincipleDue);
                        }
                    }
                }
                
            }
            //              if (!(prodType.equals("TL") || prodType.equals("AD")))
            //                  observable.setLinkMap(null);
            //            if(prodType.equals("TD")){
            //                HashMap recurringMap = new HashMap();
            //                String prodId = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected());
            //                recurringMap.put("PROD_ID",prodId);
            //                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
            //                if(lst!=null && lst.size()>0){
            //                    recurringMap = (HashMap)lst.get(0);
            //                    if(recurringMap.get("BEHAVES_LIKE").equals("RECURRING")){
            //                        ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first ");
            ////                        btnAccountNo.setEnabled(true);
            ////                        txtAccountNo.setText("");
            ////                        observable.setAccountNo("");
            ////                        btnSave.setEnabled(true);
            //                        return;
            //                    }
            //                }
            //            }

            if (/*
                     * (prodType.equals("AD") || prodType.equals("TL"))
                     */observable.getLinkMap() != null
                    && observable.getLinkMap().size() > 0 && observable.getTransType().equals(CommonConstants.CREDIT)) {
                if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                    if (asAndWhenMap == null) {
                        asAndWhenMap = new HashMap();
                    }
                    asAndWhenMap = interestCalculationTLAD(actNum, noofInstallment);
                    actInterest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"));
                    //System.out.println("-----  Transfer  actInterest "+actInterest +"batchIdForEdit-->"+batchIdForEdit);
                    observable.setActInterestLoan(actInterest);
                    transDetails.setChangeInterest("N");
                    if((observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE)&& (prodType.equals("TL") || prodType.equals("AD")) ){
                        HashMap intMap=new HashMap();
                        intMap.put("TRANS_ID",batchIdForEdit);
                        intMap.put("TRANS_DT",curr_dt.clone());
                        intMap.put("TRANSFER","TRANSFER");
                        intMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                        List intData=ClientUtil.executeQuery("getInterestEntered", intMap);
                        double intAmt=0;
                        if(intData!=null && intData.size()>0){
                            HashMap dataMap=(HashMap)intData.get(0);
                            intAmt=CommonUtil.convertObjToDouble(dataMap.get("AMOUNT"));
                        }
                        //System.out.println("actInterest--->"+actInterest+"intAmt  --->"+intAmt+"IMPP--->"+CommonConstants.OPERATE_MODE);
                      
                        if(actInterest!=intAmt && CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
                            transDetails.setChangeInterest("Y");
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                        asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                        transDetails.setAsAndWhenMap(asAndWhenMap);
                        if (asAndWhenMap.containsKey("NO_OF_INSTALLMENT") && CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT")) > 0) {
                            noofInstallment = CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"));
                        }
                        observable.setEmiNoInstallment(noofInstallment);
                        //                        if(asAndWhenMap.containsKey("INTEREST") &&
                        //                        CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue()==0){
                        //                            ClientUtil.showMessageWindow("Interest Calculation is not done");
                        //                            btnCancelActionPerformed(null);
                        //                            return;
                        //                        }
                    }
                } else {
                    //                    ClientUtil.showMessageWindow("Interest Calculation is not done");
                    //                    btnCancelActionPerformed(null);
                    //                    return;
                }
            }

            //            added by nikhil

            if (prodType.equals("GL") && (observable.getLinkMap() == null || observable.getLinkMap().size() == 0)) {
                transDetails.setTransDetails(prodType, TrueTransactMain.selBranch, observable.getAccountHeadId());
                transDetails.setSourceFrame(this);
                //                btnViewTermLoanDetails.setVisible(false);
                //                btnViewTermLoanDetails.setEnabled(false);
            } else if (prodType.equals("GL") && observable.getLinkMap().size() > 0) {
                transDetails.setTransDetails("TL", TrueTransactMain.selBranch, actNum, observable.getTransType());
                transDetails.setSourceFrame(this);
                btnViewTermLoanDetails.setVisible(true);
                btnViewTermLoanDetails.setEnabled(true);
                if (CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.CREDIT)) {
                    btnViewTermLoanDetails.setVisible(true);
                    btnViewTermLoanDetails.setEnabled(true);
                } else {
                    //                    btnViewTermLoanDetails.setVisible(false);
                    //                    btnViewTermLoanDetails.setEnabled(false);
                }
            } else {
                if (prodType.equals("TL") || prodType.equals("AD")) {
                    transDetails.setTransDetails(prodType, TrueTransactMain.selBranch, actNum, observable.getTransType());
                    transDetails.setSourceFrame(this);
                    btnViewTermLoanDetails.setVisible(true);
                    btnViewTermLoanDetails.setEnabled(true);
                } else {
                    transDetails.setTransDetails(prodType, TrueTransactMain.selBranch, actNum);
                    transDetails.setSourceFrame(this);
                    //                    btnViewTermLoanDetails.setVisible(false);
                    //                    btnViewTermLoanDetails.setEnabled(false);
                }
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                if ((prodType.equals("TL") || prodType.equals("AD")) && observable.getTransType().equals(CommonConstants.CREDIT)) {
                    observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                    observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT", new Long(noofInstallment));
                    if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI")||mapHash.get("INSTALL_TYPE").equals("USER_DEFINED") || mapHash.get("INSTALL_TYPE").equals("EYI")
                            || mapHash.get("INSTALL_TYPE").equals("LUMP_SUM")))) {
                        if(emiInSimpleInterest.equalsIgnoreCase("N")){ //Added by nithya on 19-05-2020 for KD-380
                        txtAccountNo.setText(actNum); // Added by nithya on 30-04-2021 for  KD-2801
                        calculateServiceTaxAmt();
                        double totalEMIAmt = setEMIAmount();
                        txtAmount.setText(String.valueOf(totalEMIAmt));
                        observable.setTransferAmount(String.valueOf(totalEMIAmt)); 
                        if(txtAmount.getText().length() > 0){
                          totalEMIAmt = setEMILoanWaiveAmount();
                        }
                        txtAmount.setText(String.valueOf(totalEMIAmt));
                        observable.setTransferAmount(String.valueOf(totalEMIAmt));                        
                        txtAmount.setEnabled(false);
                        }else{
                          txtAmount.setEnabled(true);  
                        }
                    } else {
                        txtAmount.setEnabled(true);
                        observable.setTransferAmount("");
                    }

                    //                observable.setTransAmt(transDetails.getLimitAmount());
                    //                observable.setLoanActNo(observable.getAccountNo());
                }
            }
            String multiDisburse = transDetails.getIsMultiDisburse();
            //System.out.println("filldata multiDisburse" + multiDisburse);
            if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        String transType = "";
                        if (reconcilebtnDisable == true) {
                            //                            btnAddDebit.setSelected(false);
                            //                            btnAddCredit.setSelected(false);
                            transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                        }
                        //                        if(transType.equals("DEBIT"))
                        //                            btnAddDebit.setSelected(true);
                        //                        if(transType.equals("CREDIT"))
                        //                            btnAddCredit.setSelected(true);
                        if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("CREDIT")) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnReconsile.setVisible(true);
                            btnReconsile.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("DEBIT")) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnReconsile.setVisible(true);
                            btnReconsile.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("DEBIT")) {
                            observable.setReconcile("Y");
                            btnReconsile.setVisible(false);
                            btnReconsile.setEnabled(false);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("CREDIT")) {
                            observable.setReconcile("Y");
                            btnReconsile.setVisible(false);
                            btnReconsile.setEnabled(false);
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    } else {
                        btnReconsile.setVisible(false);
                        btnReconsile.setEnabled(false);
                    }
                }
            } else if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        String transType = "";
                        //                        if(btnAddDebit.isSelected() == false && btnAddCredit.isSelected() == false)
                        transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                        if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("CREDIT")) {
                            observable.setReconcile("N");
                            btnReconsile.setVisible(true);
                            btnReconsile.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("DEBIT")) {
                            observable.setReconcile("N");
                            btnReconsile.setVisible(true);
                            btnReconsile.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && transType.equals("DEBIT")) {
                            observable.setReconcile("Y");
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && transType.equals("CREDIT")) {
                            observable.setReconcile("Y");
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    } else {
                        btnReconsile.setVisible(false);
                        btnReconsile.setEnabled(false);
                    }
                }
            } else {
                btnReconsile.setVisible(false);
                btnReconsile.setEnabled(false);
            }
            if (alreadyExistDeposit == false && morethanOneDeposit == true && prodType.equals("TD")) {
                txtAccountNo.setText("");
                observable.setAccountNo("");
            }
            //            if (prodType.equals("TD")) {
            //                String depositNo = observable.getAccountNo()!=null ? observable.getAccountNo() : txtAccountHeadValue.getText();
            //                double totAmt = CommonUtil.convertObjToDouble(observable.getRenewDepAmt()).doubleValue();
            //                double depAmt = CommonUtil.convertObjToDouble(observable.getTransDepositAmt()).doubleValue();
            //                double bal = depAmt - totAmt;
            //                if(totAmt>depAmt){
            //                    txtAmount.setText(observable.getTransDepositAmt());
            //                }else{
            //                    if(viewType != AUTHORIZE)
            //                        txtAmount.setText(String.valueOf(bal));
            //                }
            //                System.out.println("txtAmount transfer :"+txtAmount.getText());
            //                txtAmount.setEnabled(true);
            //            }
            //            if(prodType.equals("TL") multiDisburse.equals("N")) {
            //                txtAmount.setText(transDetails.getAvBalance());
            //                txtAmount.setEnabled(false);
            // proccesesCharge();
            //            }
            //            else
            //                txtAmount.setEnabled(true);
            //Added by chithra for mantis:10345: New Weekly Deposit Schemes For Pudukad SCB 
            if (depBehavesLike != null && depBehavesLike.equals("DAILY")) {
                chkWeeklyDepositReceiptDt(actNum);
                HashMap dailyProdID = new HashMap();
                String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                dailyProdID.put("PID", prodId);
                List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                if (dailyFrequency != null && dailyFrequency.size() > 0) {
                    HashMap dailyFreq = new HashMap();
                    dailyFreq = (HashMap) dailyFrequency.get(0);
                    String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                    String weekly_spec = CommonUtil.convertObjToStr(dailyFreq.get("WEEKLY_SPEC"));
                    String variableAmt = CommonUtil.convertObjToStr(dailyFreq.get("INSTALL_RECURRING_DEPAC"));
                    int freq = CommonUtil.convertObjToInt(daily);
                    if (freq == 7 && weekly_spec.equals("Y")) {
                        dailyProdID.put("PROD_ID", prodId);
                        List maxAmtList = ClientUtil.executeQuery("getDepProdDetails", dailyProdID);
                        if (maxAmtList != null && maxAmtList.size() > 0) {
                            HashMap maxAmtMap = new HashMap();
                            maxAmtMap = (HashMap) maxAmtList.get(0);
                            if (maxAmtMap != null && maxAmtMap.containsKey("MAX_DEPOSIT_AMT")) {
                                double maxAmt = CommonUtil.convertObjToDouble(maxAmtMap.get("MAX_DEPOSIT_AMT"));
                                if (variableAmt != null && variableAmt.equalsIgnoreCase("F")) {
                                    txtAmount.setText(CommonUtil.convertObjToStr(maxAmtMap.get("MAX_DEPOSIT_AMT")));
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private boolean moreThanLoanAmountAlert() {
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            if (observable.getTransType().equals(CommonConstants.CREDIT) && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && prodType.equals("TL")) {
                double totalLoanAmt = setEMIAmount();
                HashMap allAmtMap = observable.getALL_LOAN_AMOUNT();
                if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                    totalLoanAmt -= CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                }
                if (allAmtMap.containsKey("CLEAR_BALANCE") && CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue() < 0) {
                    totalLoanAmt += -CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue();
                }
                double actualAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0) {
                    totalLoanAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    actualAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                }
                if (actualAmt >= totalLoanAmt) {
                    int message = ClientUtil.confirmationAlert("Entered Transaction Amount is equal to/more than the Outstanding Loan Amount," + "\n" + "Do You Want to Close the A/c?");
                    if (message == 0) {
                        HashMap hash = new HashMap();
                        CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setSelectedBranchID(getSelectedBranchID());
                        TrueTransactMain.showScreen(frm);
                        hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                        hash.put("ACCOUNT NUMBER", txtAccountNo.getText());
//                        hash.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected()));
                        hash.put("PROD_ID", CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));
                        frm.fillData(hash);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private double setEMIAmount() {
        HashMap allAmtMap = new HashMap();
        double totEmiAmount = 0.0;
        allAmtMap = observable.getALL_LOAN_AMOUNT();
        if (allAmtMap != null && allAmtMap.size() > 0) {
            if (allAmtMap.containsKey("POSTAGE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue() > 0) {
                totEmiAmount = CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ARBITRARY CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("LEGAL CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("INSURANCE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("MISCELLANEOUS CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EXECUTION DECREE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ADVERTISE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("PENAL_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
            }
            if (allAmtMap.containsKey("NOTICE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES"));
            }
            if (allAmtMap.containsKey("OTHER_CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue();
            }   
            // Added by nithya for 0008470 : overdue interest for EMI Loans
            if (allAmtMap.containsKey("EMI_OVERDUE_CHARGE") && allAmtMap.get("EMI_OVERDUE_CHARGE") != null && CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue();                
            }
            if (allAmtMap.containsKey("RECOVERY CHARGES") && allAmtMap.get("RECOVERY CHARGES") != null && CommonUtil.convertObjToDouble(allAmtMap.get("RECOVERY CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("RECOVERY CHARGES")).doubleValue();                
            }
            if (allAmtMap.containsKey("MEASUREMENT CHARGES") && allAmtMap.get("MEASUREMENT CHARGES") != null && CommonUtil.convertObjToDouble(allAmtMap.get("MEASUREMENT CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("MEASUREMENT CHARGES")).doubleValue();                
            }
            
            if (allAmtMap.containsKey("KOLEFIELD EXPENSE") && allAmtMap.get("KOLEFIELD EXPENSE") != null && CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD EXPENSE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD EXPENSE")).doubleValue();
            } 
            
            if (allAmtMap.containsKey("KOLEFIELD OPERATION") && allAmtMap.get("KOLEFIELD OPERATION") != null && CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD OPERATION")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD OPERATION")).doubleValue();
            }
            
            
            if(serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null){
               totEmiAmount += CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")).doubleValue();  
            }
            
        }
        return totEmiAmount;
    }

    private HashMap interestCalculationTLAD(String accountNo, long noOfInstallment) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            if (accountNo != null && accountNo.length() > 0) {
                map.put("ACT_NUM", accountNo);
                String prod_id = "";
                //            prod_id = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                map.put("PROD_ID", prod_id);
                map.put("TRANS_DT", curr_dt);
                map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                map.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN");
                List lst = ClientUtil.executeQuery("IntCalculationDetail", map);
                if (lst == null || lst.isEmpty()) {
                    lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
                }
                if (lst != null && lst.size() > 0) {
                    hash = (HashMap) lst.get(0);
                    if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                        hash = new HashMap();
                        return hash;
                    }
                    map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    if (noOfInstallment > 0) {
                        map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                    }


                    //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                    map.putAll(hash);
                    map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                    map.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
                    //System.out.println("map before intereest###" + map);
                    //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                    hash = observable.loanInterestCalculationAsAndWhen(map);
                    if (hash == null) {
                        hash = new HashMap();
                    }
                    hash.putAll(map);
                    //System.out.println("hashinterestoutput###" + hash);
                    hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
    /*
     * public void proccesesCharge() { String
     * sangstionAmount=transDetails.getLimitAmount().replaceAll(",","");
     * System.out.println("amount"+CommonUtil.convertObjToDouble(sangstionAmount).doubleValue());
     * int tablerow=tblTransList.getRowCount(); for(int i=0;i<tablerow;i++)
     * String type =CommonUtil.convertObjToStr(tblTransList.getColumn("Type"));
     *
     *
     * //double amount=Double.parseDouble( sangtionAmount);
     * //System.out.println("amount"+amount);
     }
     */
    /*
     * use this method to get the batch information and update the CR/DR details
     * for the selected batch
     */

    public void updateTransInfo() {
        // set the values for UI
        observable.getTransDetails();
        lblTtlCrInstrValue.setText(String.valueOf(observable.getTotalCrInstruments()));
        lblTtlCrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalCrAmount())));
        lblTtlDrInstrValue.setText(String.valueOf(observable.getTotalDrInstruments()));
        lblTtlDrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalDrAmount())));
    }

    /**
     * For updating the UI based on the attached Observable
     */
    public void update(Observable ob, Object arg) {
        removeRadioButton();
        if (observable.getMainProductTypeValue() == null || observable.getMainProductTypeValue().equals("")) {
            cboProductType.setSelectedIndex(0);
        } else {
            cboProductType.setSelectedItem(
                    ((ComboBoxModel) cboProductType.getModel()).getDataForKey(
                    observable.getMainProductTypeValue()));
        }

        //System.out.println("getMainProductTypeValue:" + observable.getMainProductTypeValue());

        this.cboProductID.setModel(observable.getProductTypeModel());
        this.cboInstrumentType.setModel(observable.getInstrumentTypeModel());
        // update the combo boxes
        if (observable.getProductTypeValue() == null || observable.getProductTypeValue().equals("")) {
            cboProductID.setSelectedIndex(0);
        } else {
            cboProductID.setSelectedItem(
                    ((ComboBoxModel) cboProductID.getModel()).getDataForKey(
                    observable.getProductId()));
        }

        if (observable.getInstrumentTypeValue() == null || observable.getInstrumentTypeValue().equals("")) {
            cboInstrumentType.setSelectedIndex(0);
        } else {
            cboInstrumentType.setSelectedItem(
                    ((ComboBoxModel) cboInstrumentType.getModel()).getDataForKey(
                    observable.getInstrumentTypeValue()));
        }

        if (observable.getCurrencyTypeValue() == null || observable.getCurrencyTypeValue().equals("")) {
            cboCurrency.setSelectedIndex(0);
        } else {
            cboCurrency.setSelectedItem(
                    ((ComboBoxModel) cboCurrency.getModel()).getDataForKey(
                    observable.getCurrencyTypeValue()));
        }

        // update the input panel
        lblTransDtValue.setText(DateUtil.getStringDate(observable.getCurDate()));
        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getValueDate()));
        lblBatchIDValue.setText(observable.getBatchId());
        lblTransactionIDValue.setText(observable.getTransId());
        txtAccountHeadValue.setText(observable.getAccountHeadId());
        lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());

        txtAccountNo.setText(observable.getAccountNo());
        //System.out.println("######Product Type : " + ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        String pType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();       
          
        // Added by Rajesh
        if (pType.length() > 0) {            
            //            if (!pType.equals("GL"))observable.getAccountNo()
            if (observable.getAccountNo() != null && observable.getAccountNo().length() > 0) {
                lblCustNameValue.setText(setAccountName(observable.getAccountNo()));
            } else {
                lblAccountHeadDescValue.setText(setAccountName(observable.getAccountHeadId()));
            }
        }
        // update the transaction details panel
        //txtTokenNo.setText(observable.getTokenNo());
        txtInstrumentNo1.setText(observable.getInstrumentNo1());
        txtInstrumentNo2.setText(observable.getInstrumentNo2());
        tdtInstrumentDate.setDateValue(observable.getInstrumentDate());
        txtAmount.setText(observable.getTransferAmount());
        txtParticulars.setText(observable.getParticulars());
        txtNarration.setText(observable.getNarration());
        // update the transaction info panel
        lblTtlCrInstrValue.setText(String.valueOf(observable.getTotalCrInstruments()));
        lblTtlCrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalCrAmount())));
        lblTtlDrInstrValue.setText(String.valueOf(observable.getTotalDrInstruments()));
        lblTtlDrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalDrAmount())));

        // update the account info panel
        //        updateAccountInfo();

        // update the status
        lblStatusValue.setText(observable.getLblStatus());
        lblAuthByValue.setText(observable.getAuthBy());

        if (observable.getOperation() != ClientConstants.ACTIONTYPE_NEW) {
            this.lblCreatedByValue.setText(observable.getInitTransId());
        }

        this.updateTable();
        addRadioButton();
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }

    public void btnDepositClose() {
        flag = true;
        afterSaveCancel = true;
        viewType = this.AUTHORIZE;
        btnAdd.setEnabled(false);
        //        if(txtAmount.getText().length()>0){
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")) {
            btnAuthorizeActionPerformed(null);
        }
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")) {
            btnRejectionActionPerformed(null);
        }
        btnCloseActionPerformed(null);
        //        }
    }

    private void removeRadioButton() {
        bulkTransactionGroup.remove(rdoBulkTransaction_Yes);
        bulkTransactionGroup.remove(rdoBulkTransaction_No);
    }

    private void addRadioButton() {
        bulkTransactionGroup.add(rdoBulkTransaction_Yes);
        bulkTransactionGroup.add(rdoBulkTransaction_No);
    }

    public void btnDepositCancel() {
        afterSaveCancel = true;
        btnCancelActionPerformed(null);
        btnCloseActionPerformed(null);
    }

    public void depositAuthorize(HashMap renewMap, TermDepositUI termDepositUI) {
        this.termDepositUI = termDepositUI;
        String actNum = CommonUtil.convertObjToStr(renewMap.get("ACT_NUM"));
        this.batchIdForEdit = CommonUtil.convertObjToStr(renewMap.get("BATCH_ID"));
        setModified(true);
        authorizeStatus();
        btnCancel.setEnabled(false);
        btnView.setEnabled(false);
        if (termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")) {
            btnAuthorize.setEnabled(true);
            btnRejection.setEnabled(false);
        }
        if (termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")) {
            btnRejection.setEnabled(true);
            btnAuthorize.setEnabled(false);
        }
    }

    public HashMap transferingIntAmt(CTextField txtDepositAmount, HashMap intMap, TermDepositUI termDepositUI) {
        this.termDepositUI = termDepositUI;
        flag = true;
        observable.depositRenewalFlag = true;
        observable.renewalIntFlag = true;
        flagDepLink = true;
        if (intMap.containsKey("transferingAmt")) {
            txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("AMOUNT")));
        }
        this.txtDepositAmount = txtDepositAmount;
        transferDepMap = new HashMap();
        transferDepMap = intMap;
        //System.out.println("$$$$$$$transferDepMap :" + transferDepMap);
        if (intMap != null) {
            //System.out.println("%%%%%%intMap :" + intMap);
            HashMap subNoMap = new HashMap();
            btnAddActionPerformed(null);
            btnAddDebitActionPerformed(null);
            btnTransDelete.setEnabled(false);
            btnAddDebit.setEnabled(false);
            subNoMap.put("DEPOSIT_NO", intMap.get("ACCOUNTNO"));
            subNoMap.put("CUST_ID", intMap.get("CUST_ID"));
            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
            if (lst.size() > 0) {
                subNoMap = (HashMap) lst.get(0);
                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                String accountNo = depositNo + "_" + subNo;
                intMap.put("ACCOUNTNO", accountNo);
                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                observable.setTransType("DEBIT");
                cboProductTypeActionPerformed(null);
                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccountNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblCustNameValue.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                lblCreatedByValue.setText(ProxyParameters.USER_ID);
                double amount = CommonUtil.convertObjToDouble(intMap.get("INT_AMT")).doubleValue();
                intMap.put("AMOUNT", intMap.get("INT_AMT"));
                fillData(intMap);
                tdtValueDt.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
                tdtValueDt.setEnabled(false);
                txtAmount.setText(String.valueOf(amount));
                ClientUtil.enableDisable(this, true);
                btnTransSave.setEnabled(true);
                btnSave.setEnabled(true);
                txtAmount.setEditable(false);
                cboProductType.setEnabled(false);
                cboProductID.setEnabled(false);
                txtAccountNo.setEditable(false);
                btnAccountHead.setEnabled(false);
                btnAccountNo.setEnabled(false);
                cboInstrumentType.setSelectedItem("Voucher");
                cboInstrumentType.setEnabled(false);
                txtParticulars.setText("To " + txtAccountNo.getText());
                txtParticulars.setEnabled(false);
                btnCancel.setEnabled(false);
                btnView.setEnabled(false);
                if (flagDepLink == true) {
                    observable.setDepLinkBatchId("DEP_LINK");
                    observable.setDepAccNO(accountNo);
                }
                //                getOperativeAccNo(depositNo,amount);
                intMap.put("TRANS_ID", observable.getDepositTransId());
                //System.out.println("###### IntMap : " + intMap);
            }
        }
        return intMap;
    }

    public HashMap transferingDepAmt(CTextField txtDepositAmount, HashMap intMap, TermDepositUI termDepositUI) {
        this.termDepositUI = termDepositUI;
        flagDeposit = true;
        observable.depositRenewalFlag = true;
        flagDepLink = true;
        this.txtDepositAmount = txtDepositAmount;
        if (intMap.containsKey("transferingAmt")) {
            txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("AMOUNT")));
        }
        this.txtDepositAmount = txtDepositAmount;
        transferDepMap = new HashMap();
        transferDepMap = intMap;
        //System.out.println("$$$$$$$transferDepMap :" + transferDepMap);
        if (intMap != null) {
            //System.out.println("%%%%%%intMap :" + intMap);
            HashMap subNoMap = new HashMap();
            btnAddActionPerformed(null);
            btnAddDebitActionPerformed(null);
            btnTransDelete.setEnabled(false);
            btnAddDebit.setEnabled(false);
            subNoMap.put("DEPOSIT_NO", intMap.get("ACCOUNTNO"));
            subNoMap.put("CUST_ID", intMap.get("CUST_ID"));
            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
            if (lst.size() > 0) {
                subNoMap = (HashMap) lst.get(0);
                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                String accountNo = depositNo + "_" + subNo;
                intMap.put("ACCOUNTNO", accountNo);
                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                observable.setTransType("DEBIT");
                cboProductTypeActionPerformed(null);
                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccountNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblCustNameValue.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                lblCreatedByValue.setText(ProxyParameters.USER_ID);
                txtAmount.setText(CommonUtil.convertObjToStr("0.0"));
                fillData(intMap);
                ClientUtil.enableDisable(this, true);
                tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
                tdtValueDt.setEnabled(false);
                observable.setDepInterestAmt("");
                btnTransSave.setEnabled(true);
                btnSave.setEnabled(true);
                btnCancel.setEnabled(false);
                txtAmount.setEditable(true);
                cboProductType.setEnabled(false);
                cboProductID.setEnabled(false);
                txtAccountNo.setEditable(false);
                btnAccountNo.setEnabled(false);
                cboInstrumentType.setSelectedItem("Voucher");
                cboInstrumentType.setEnabled(false);
                txtParticulars.setText("To " + txtAccountNo.getText());
                txtParticulars.setEnabled(false);
                btnView.setEnabled(false);
                if (flagDepLink == true) {
                    observable.setDepLinkBatchId("DEP_LINK");
                    observable.setDepAccNO(accountNo);
                }
                intMap.put("TRANS_ID", observable.getDepositTransId());
                //System.out.println("###### IntMap : " + intMap);
            }
        }
        return intMap;
    }

    public void fillAccNo(String accNo, String prodId, String prodType) {
        //System.out.println("in fill accno>>>" + accNo);
        //System.out.println("prodId in fillaccno>>>" + prodId);
        //System.out.println("prodType in fillaccno>>>" + prodType);
        //  accNo1=accNo;
        prodIdforgl = prodId;
        prodtypeforgl = prodType;
        accNo1 = accNo;
        lblAccNoGl.setVisible(true);
        lblAccNoGl.setEnabled(true);
        lblAccNoGl.setText("");
        if (accNo1 != null) {
            lblAccNoGl.setText(accNo1);
            observable.setAccNumGlProdType(prodType);
        }
        //System.out.println("lblAccNoGl.getText>>>" + lblAccNoGl.getText());
        //        //To set the  Name of the Account Holder...
        String pType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();

        //   System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
        //    String prevActNum = observable.getLblAccName();
        //System.out.println("pType>>>" + pType);
        if (pType.length() > 0) {
            if (pType.equals("GL")) {
                setAccountName1(lblAccNoGl.getText().toString());
                //   System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
                //  lblAccName.setText(observable.getLblAccName());
                //  System.out.println("observable.getLblHouseName()>>>"+observable.getLblHouseName());
                //  lblHouseName.setText(observable.getLblHouseName());
                //    observable.setAccountHead();
                //    System.out.println("observable.getLblAccHdDesc()@@>>>"+observable.getLblAccHdDesc());
                //  lblAccHdDesc.setText(observable.getLblAccHdDesc());
            }
        }
    }

    public void setAccountName1(String AccountNo) {
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodtypeforgl.equals("")) {
                accountNameMap.put("ACC_NUM", AccountNo);
                String pID = !prodtypeforgl.equals("GL") ? prodIdforgl.toString() : "";
                if (prodtypeforgl.equals("GL") && AccountNo.length() > 0) {
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL", accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName" + prodtypeforgl, accountNameMap);
                    List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName", accountNameMap);
                    if (custHouseNameList != null && custHouseNameList.size() > 0) {
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) custHouseNameList.get(0);
                        lblHouseName.setText(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                        //System.out.println("lblHouseName>>>" + lblHouseName.getText());
                    }
                }
                if (resultList != null && resultList.size() > 0) {
                    
                    if (resultMap.containsKey("CUST_ID") && resultMap.get("CUST_ID") != null) {
                        // Add retiredt alert here
                        String retireDtAlertRequired = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT"));
                        //System.out.println("retireDtAlertRequired :: " + retireDtAlertRequired);
                        if (retireDtAlertRequired.equals("Y")) {
                            int retireDtAlertDays = CommonUtil.convertObjToInt(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT_DAYS"));
                            List retireDtList = ClientUtil.executeQuery("getBorrowerShareDetails", resultMap);
                            if (retireDtList != null && retireDtList.size() > 0) {
                                HashMap retireDtMap = (HashMap) retireDtList.get(0);
                                if (retireDtMap.containsKey("RETIREMENT_DT") && retireDtMap.get("RETIREMENT_DT") != null) {
                                    Date retireDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retireDtMap.get("RETIREMENT_DT")));
                                    if (DateUtil.dateDiff(curr_dt, retireDt) <= retireDtAlertDays) {
                                        ClientUtil.showMessageWindow("Retirement Date is : " + DateUtil.getStringDate(retireDt));
                                    }
                                }
                            }
                        }
                          // End
                    }
                    
                    if (!prodtypeforgl.equals("GL") && !prodtypeforgl.equals("SH")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodtypeforgl, accountNameMap);
                        if (lst != null && lst.size() > 0) {
                            dataMap = (HashMap) lst.get(0);
                        }
                        if (dataMap.get("PROD_ID").equals(pID)) {
                            resultMap = (HashMap) resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap) resultList.get(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultMap.containsKey("CUSTOMER_NAME")) {
            lblCustNameValue.setText(CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")) + ' ' + ' ' + resultMap.get("CUSTOMER_NAME").toString());
            //System.out.println("lblCustNameValue>>>>>>" + lblCustNameValue.getText());
        } else {
            lblCustNameValue.setText("");
        }
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }

    public String setAccountName(String AccountNo) {
        final HashMap accountNameMap = new HashMap();
        HashMap resultMap = new HashMap();
        List resultList = new ArrayList();
    
        String prodType = (String) ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected();        
        String pID = "";
        if(cboProductID.getSelectedIndex() > 0){
            pID = !prodType.equals("GL") ? ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString() : "";
        }        
        try {
            accountNameMap.put("ACC_NUM", AccountNo);
            if (prodType.equals("GL") && AccountNo.length() > 0) {
                resultList = ClientUtil.executeQuery("getAccountNumberNameGL", accountNameMap);//
            } else {
                resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, accountNameMap);
            }
            if (resultList != null) {
                if (resultList.size() > 0) {
                    if (prodType.length()>0&&!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodType, accountNameMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                dataMap = (HashMap) lst.get(0);
                                if (dataMap.get("PROD_ID").equals(pID)) {
                                    resultMap = (HashMap) resultList.get(0);
                                    if (resultMap.containsKey("CUST_ID") && resultMap.get("CUST_ID") != null) {
                                        // Added by nithya   - KD-3464
                                        String retireDtAlertRequired = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT"));
                                        //System.out.println("retireDtAlertRequired :: " + retireDtAlertRequired);
                                        if (retireDtAlertRequired.equals("Y")) {
                                            int retireDtAlertDays = CommonUtil.convertObjToInt(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT_DAYS"));
                                            List retireDtList = ClientUtil.executeQuery("getBorrowerShareDetails", resultMap);
                                            if (retireDtList != null && retireDtList.size() > 0) {
                                                HashMap retireDtMap = (HashMap) retireDtList.get(0);
                                                if (retireDtMap.containsKey("RETIREMENT_DT") && retireDtMap.get("RETIREMENT_DT") != null) {
                                                    Date retireDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retireDtMap.get("RETIREMENT_DT")));
                                                    if (DateUtil.dateDiff(curr_dt, retireDt) <= retireDtAlertDays) {
                                                        ClientUtil.showMessageWindow("Retirement Date is : " + DateUtil.getStringDate(retireDt));
                                                    }
                                                }
                                            }
                                        }
                                        // End
                                    }
                                }
                            }
                        }
                    } else {
                        resultMap = (HashMap) resultList.get(0);
                    }

                }
            }
        } catch (Exception e) {
          
        }
        if (resultMap.containsKey("CUSTOMER_NAME")) {
            if (prodType.equals("OA")) {
                String custStatus = CommonUtil.convertObjToStr(resultMap.get("MINOR"));
                if (custStatus.equals("Y")) {
                    if (observable.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                        ClientUtil.displayAlert("MINOR ACCOUNT");
                    }
                }
            }
            //Added By Suresh
            resultMap.put("CUSTOMER_NAME", (CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")) + ' ' + ' ' + resultMap.get("CUSTOMER_NAME").toString()));
            return resultMap.get("CUSTOMER_NAME").toString();
        } else {
            return String.valueOf("");
        }
    }

    /**
     * setting the transaction Id for which the editing is to be done
     *
     * @param transactionIdForEdit the transaction id which will be refered for
     * editing
     */
    public void setTransactionIdForEdit(String transactionIdForEdit) {
        /*
         * this method will be called by the ActionPopupUI class on the
         * reference of TransferUI, to set the transaction id for the editing
         * purpose
         */
        this.transactionIdForEdit = transactionIdForEdit;
    }

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm

    private void btnOrgOrRespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrgOrRespActionPerformed
        // TODO add your handling code here:
        String transType = "";
        double transViewAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        if (transViewAmount == 0.0) {
            transViewAmount = CommonUtil.convertObjToDouble(tblTransList.getValueAt(0, 3)).doubleValue();
        }
        if (btnOrgOrResp.getText().equals("R")) {
            transType = CommonConstants.DEBIT;
            viewRespUI = new ViewRespUI(transViewAmount, transType, this);
            com.see.truetransact.ui.TrueTransactMain.showScreen(viewRespUI);
        } else if (btnOrgOrResp.getText().equals("O")) {
            transType = CommonConstants.CREDIT;
            vieworgOrRespUI = new ViewOrgOrRespUI(transViewAmount, transType, this);
            com.see.truetransact.ui.TrueTransactMain.showScreen(vieworgOrRespUI);
        }


    }//GEN-LAST:event_btnOrgOrRespActionPerformed

    private void txtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusGained
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountFocusGained

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));

            //System.out.println("Amount should be lesser than the specified value");
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyTyped

    private void txtAccountHeadValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountHeadValueFocusLost
        // TODO add your handling code here:
          txtAccountHeadValueActionPerformed();
//         if(txtAccountHeadValue.getText()!=null && !txtAccountHeadValue.getText().equalsIgnoreCase("")){
//         HashMap hmap = new HashMap();
//         String customerAllow="";
//            hmap.put("ACHEAD", txtAccountHeadValue.getText());
//            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
//            if (list != null && list.size() > 0) {
//                hmap = (HashMap) list.get(0);
//                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
//              //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
//            }
////            if (bankType.equals("DCCB")) {
////                if (hoAc.equals("Y")) {
////                    btnOrgOrResp.setVisible(true);
////                    observable.setHoAccount(true);
////                } else {
////                    observable.setHoAccount(false);
////                    btnOrgOrResp.setVisible(false);
////                }
////            }
//            System.out.println("customerAllow>>>>"+customerAllow);
//        if (customerAllow.equals("Y")) {
//                System.out.println("innnnn");
//                CInternalFrame frm = new CInternalFrame();
//                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
//                frm.setSelectedBranchID(getSelectedBranchID());
//               //frm.setSize(1000,1000);
//                TrueTransactMain.showScreen(frm);
//                
////               final CInternalFrame frame = new CInternalFrame();
////               CDesktopPane desktop = new CDesktopPane();
////               glAccountNumberListUI=new GLAccountNumberListUI();
////        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
////        frame.setSize(200, 100);
////        frame.setVisible(true);
////        frame.getContentPane().add(glAccountNumberListUI);
////        desktop.add(frame);
//          
////                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
////                glAccNo.show();
////                glAccNo.setVisible(true);
////                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
////                hmap.put("ACC_NUM", AccNo);
////                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
////                if (chkList != null && chkList.size() > 0) {
////                    hmap = (HashMap) chkList.get(0);
////                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
////                    observable.setTxtAccNo(AccNo);
////                    observable.setClosedAccNo(AccNo);
////                } else {
////                    ClientUtil.displayAlert("Invalid Account number");
////                    txtAccHdId.setText("");
////                    return;
////                }
//            }
//        }
    }//GEN-LAST:event_txtAccountHeadValueFocusLost

    private void txtParticularsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParticularsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParticularsActionPerformed

    private void tblTransListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTransListKeyPressed
        // TODO add your handling code here:
       if(evt.getKeyCode() == 10){    //This line added by anjuanand on 25-08-2014
          if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            panTransferEnableDisable(true);
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            lblAccNoGl.setEnabled(false);

        }
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_EDIT) {
            lblAccNoGl.setEnabled(false);
        }
        alreadyExistDeposit = true;
        reconcilebtnDisable = true;
        updationTransfer();
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            isRowClicked = true;
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            tdtValueDt.setEnabled(true);
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            txtAccountNo.setEnabled(false);
        }
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (prodType.equals("TD") && flagDepLink == true) {
            btnAccountNo.setEnabled(false);
            btnTransDelete.setEnabled(false);
            cboInstrumentType.setEnabled(false);
            txtParticulars.setEnabled(false);
        }
        reconcilebtnDisable = false;
        if ((observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) && prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            String act_num = observable.getAccountNo();
            HashMap inputMap = new HashMap();
            double avbal = 0.0;
            HashMap balMap = new HashMap();
            balMap = getAvailableAndShadowBal();
            inputMap.put("ACCOUNTNO", act_num);
            double tblSumAmt = 0.0;
            for (int i = 0; i < tblTransList.getRowCount(); i++) {
                String tblActNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
                if (act_num.equalsIgnoreCase(tblActNum) && act_num != null) {
                    double tblAmt = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                 
           tblSumAmt += tblAmt;
                }
            }
            boolean cont = checkForMinAmt(inputMap, 0, 0, CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue(), 0, tblSumAmt);
            if (!cont) {
                btnAuthorize.setEnabled(false);
                btnRejection.setEnabled(false);
                btnException.setEnabled(false);
                return;
            }
        }
        //Added By Suresh
        if (!prodType.equals("GL")) {
            String act_num = observable.getAccountNo();
            setCustHouseName(act_num);
        } else {
            if (txtAccountHeadValue.getText().length() > 0 || observable.getAccountHeadId().length() > 0) {
                observable.getAccountHead();
                this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
            }
        }
        //added by anjuanand on 23-08-2014
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            rowCount += 1;
            if (rowCount == tblTransList.getRowCount()) {
                tblTransList.setEnabled(false);
                tblTransList.requestFocus(false);
                btnAuthorize.setEnabled(true);
                btnAuthorize.setFocusable(true);
                btnAuthorize.setFocusPainted(true);
                btnAuthorize.requestFocus(true);
                tblTransList.clearSelection();
            }
        }
    }
        if (observable.getParticulars() != null && observable.getParticulars().equals("Service Tax Recived")) {
            txtAmount.setEnabled(false);
            txtParticulars.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
            txtParticulars.setEnabled(true);
        }

    }//GEN-LAST:event_tblTransListKeyPressed
       public void showEditWaiveTableUI(HashMap totalLoanAmount) {
        ArrayList singleList = new ArrayList();
        HashMap listMap = new HashMap();
        listMap.put("PENAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")));
        listMap.put("INTEREST", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_INT")));
        listMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_PRINCEPLE")));
        listMap.put("NOTICE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("NOTICE CHARGES")));
        listMap.put("ARC_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARC_COST")));
        listMap.put("ARBITRARY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARBITRARY CHARGES")));
        listMap.put("EXECUTION DECREE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("EXECUTION DECREE CHARGES")));
        listMap.put("POSTAGE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("POSTAGE CHARGES")));
        listMap.put("ADVERTISE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ADVERTISE CHARGES")));
        listMap.put("LEGAL CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("LEGAL CHARGES")));
        listMap.put("INSURANCE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("INSURANCE CHARGES")));
        listMap.put("EP_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("EP_COST")));
        listMap.put("MISCELLANEOUS CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MISCELLANEOUS CHARGES")));
        if(totalLoanAmount.containsKey("RECOVERY CHARGES") && totalLoanAmount.get("RECOVERY CHARGES") != null){
            listMap.put("RECOVERY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("RECOVERY CHARGES")));
        }
         if(totalLoanAmount.containsKey("MEASUREMENT CHARGES") && totalLoanAmount.get("MEASUREMENT CHARGES") != null){
            listMap.put("MEASUREMENT CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MEASUREMENT CHARGES")));
        }
         
        if(totalLoanAmount.containsKey("KOLEFIELD EXPENSE") && totalLoanAmount.get("KOLEFIELD EXPENSE") != null){
            listMap.put("KOLEFIELD EXPENSE", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD EXPENSE")));
        }
        
        if(totalLoanAmount.containsKey("KOLEFIELD OPERATION") && totalLoanAmount.get("KOLEFIELD OPERATION") != null){
            listMap.put("KOLEFIELD OPERATION", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD OPERATION")));
        }
         
         
        // Added by nithya for 0008470 : overdue interest for EMI Loans
        if(totalLoanAmount.containsKey("EMI_OVERDUE_CHARGE") && totalLoanAmount.get("EMI_OVERDUE_CHARGE") != null)
        listMap.put("EMI_OVERDUE_CHARGE", CommonUtil.convertObjToDouble(totalLoanAmount.get("EMI_OVERDUE_CHARGE")));// For overdue interest
       singleList.add(listMap);
        editableWaiveUI = new EditWaiveTableUI("CASH_SCREEN", listMap);
        editableWaiveUI.show();
//        TrueTransactMain.showScreen(editableUI);
    }
       
    private double setEMILoanWaiveAmount() {
        //System.out.println("asAndWhenMap inside setEMILoanWaiveAmount :: " + asAndWhenMap);
        //System.out.println("amount..... " + observable.getAmount());
        double penalWaiveAmt = 0.0;
        double interestWaiveAmt = 0.0;
        double noticeWaiveAmt = 0.0;
        double arcWaiveAmt = 0.0;
        double arbitaryWaiveAmt = 0.0;
        double postageWaiveAmt = 0.0;
        double recoveryWaiveAmt = 0.0;
        double measurementWaiveAmt = 0.0;
        double legalWaiveAmt = 0.0;
        double insurenceWaiveAmt = 0.0;
        double epWaiveAmt = 0.0;
        double degreeWaiveAmt = 0.0;
        double miseWaiveAmt = 0.0;
        double advertiseWaiveAmt = 0.0;
        double overDueIntWaiveAmt = 0.0;
        double koleFieldExpenseWaiveAmt = 0.0;
        double koleFieldOperationWaiveAmt = 0.0;
        double totalAmount = CommonUtil.convertObjToDouble(observable.getAmount());
        if (asAndWhenMap != null && asAndWhenMap.containsKey("EMI_IN_SIMPLEINTREST") && asAndWhenMap.get("EMI_IN_SIMPLEINTREST") != null && !asAndWhenMap.get("EMI_IN_SIMPLEINTREST").equals("Y") && returnWaiveMap != null) {
            if (asAndWhenMap.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PENAL_WAIVER")).equals("Y") && returnWaiveMap.containsKey("PENAL") && returnWaiveMap.get("PENAL") != null) {
                penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
            }
            if (asAndWhenMap.containsKey("INTEREST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INTEREST_WAIVER")).equals("Y") && returnWaiveMap.containsKey("INTEREST") && returnWaiveMap.get("INTEREST") != null) {
                interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
            }
            if (asAndWhenMap.containsKey("NOTICE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("NOTICE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("NOTICE CHARGES") && returnWaiveMap.get("NOTICE CHARGES") != null) {
                noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
            }
            if (asAndWhenMap.containsKey("ARC_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARC_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ARC_COST") && returnWaiveMap.get("ARC_COST") != null) {
                arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
            }
            if (asAndWhenMap.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARBITRARY_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ARBITRARY CHARGES") && returnWaiveMap.get("ARBITRARY CHARGES") != null) {
                arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
            }
            if (asAndWhenMap.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("POSTAGE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("POSTAGE CHARGES") && returnWaiveMap.get("POSTAGE CHARGES") != null) {
                postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
            }
            if (asAndWhenMap.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MISCELLANEOUS_WAIVER")).equals("Y") && returnWaiveMap.containsKey("MISCELLANEOUS CHARGES") && returnWaiveMap.get("MISCELLANEOUS CHARGES") != null) {
                miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
            }
            if (asAndWhenMap.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INSURANCE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("INSURANCE CHARGES") && returnWaiveMap.get("INSURANCE CHARGES") != null) {
                insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
            }
            if (asAndWhenMap.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("EP_COST_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EP_COST") && returnWaiveMap.get("EP_COST") != null) {
                epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
            }
            if (asAndWhenMap.containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("LEGAL_WAIVER")).equals("Y") && returnWaiveMap.containsKey("LEGAL CHARGES") && returnWaiveMap.get("LEGAL CHARGES") != null) {
                legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
            }
            if (asAndWhenMap.containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("DECREE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EXECUTION DECREE CHARGES") && returnWaiveMap.get("EXECUTION DECREE CHARGES") != null) {
                degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
            }
            if (asAndWhenMap.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ADVERTISE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ADVERTISE CHARGES") && returnWaiveMap.get("ADVERTISE CHARGES") != null) {
                advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
            }
            if (asAndWhenMap.containsKey("OVERDUEINT_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("OVERDUEINT_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EMI_OVERDUE_CHARGE") && returnWaiveMap.get("EMI_OVERDUE_CHARGE") != null) {
                overDueIntWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EMI_OVERDUE_CHARGE"));
            }
            if (asAndWhenMap.containsKey("RECOVERY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("RECOVERY_WAIVER")).equals("Y") && returnWaiveMap.containsKey("RECOVERY CHARGES") && returnWaiveMap.get("RECOVERY CHARGES") != null) {
                recoveryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("RECOVERY CHARGES"));
            }
            if (asAndWhenMap.containsKey("MEASUREMENT_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MEASUREMENT_WAIVER")).equals("Y") && returnWaiveMap.containsKey("MEASUREMENT CHARGES") && returnWaiveMap.get("MEASUREMENT CHARGES") != null) {
                measurementWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MEASUREMENT CHARGES"));
            }
            
            if (asAndWhenMap.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("KOLEFIELD EXPENSE") && returnWaiveMap.get("KOLEFIELD EXPENSE") != null && returnWaiveMap.get("KOLEFIELD EXPENSE") != null) {
                koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD EXPENSE"));
            }
            
            if (asAndWhenMap.containsKey("KOLE_FIELD_OPERATION_WAIVER") && asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y") && returnWaiveMap.containsKey("KOLEFIELD OPERATION") && returnWaiveMap.get("KOLEFIELD OPERATION") != null && returnWaiveMap.get("KOLEFIELD OPERATION") != null) {
                koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD OPERATION"));
            }
        }

        if (penalWaiveAmt > 0) {
            observable.setPenalWaiveOff(true);
            observable.setPenalWaiveAmount(penalWaiveAmt);
            //System.out.println("emi penal waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - penalWaiveAmt));
            totalAmount = totalAmount - penalWaiveAmt;
        } else {
            observable.setPenalWaiveOff(false);
        }

        if (interestWaiveAmt > 0) {
            observable.setInterestWaiveoff(true);
            observable.setInterestWaiveAmount(interestWaiveAmt);
            //System.out.println("emi interest waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - interestWaiveAmt));
            totalAmount = totalAmount - interestWaiveAmt;
        } else {
            observable.setInterestWaiveoff(false);
        }

        if (noticeWaiveAmt > 0) {
            observable.setNoticeWaiveoff(true);
            observable.setNoticeWaiveAmount(noticeWaiveAmt);
            //System.out.println("emi notice waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - noticeWaiveAmt));
            totalAmount = totalAmount - noticeWaiveAmt;
        } else {
            observable.setNoticeWaiveoff(false);
        }

        if (arcWaiveAmt > 0) {
            observable.setArcWaiveOff(true);
            observable.setArcWaiveAmount(arcWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - arcWaiveAmt));
            totalAmount = totalAmount - arcWaiveAmt;
        } else {
            observable.setArcWaiveOff(false);
        }

        if (arbitaryWaiveAmt > 0) {
            observable.setArbitraryWaiveOff(true);
            observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - arbitaryWaiveAmt));
            totalAmount = totalAmount - arbitaryWaiveAmt;
        } else {
            observable.setArbitraryWaiveOff(false);
        }

        if (postageWaiveAmt > 0) {
            observable.setPostageWaiveOff(true);
            observable.setPostageWaiveAmount(postageWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - postageWaiveAmt));
            totalAmount = totalAmount - postageWaiveAmt;
        } else {
            observable.setPostageWaiveOff(false);
        }

        if (legalWaiveAmt > 0) {
            observable.setLegalWaiveOff(true);
            observable.setLegalWaiveAmount(legalWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - legalWaiveAmt));
            totalAmount = totalAmount - legalWaiveAmt;
        } else {
            observable.setLegalWaiveOff(false);
        }

        if (insurenceWaiveAmt > 0) {
            observable.setInsuranceWaiveOff(true);
            observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - insurenceWaiveAmt));
            totalAmount = totalAmount - insurenceWaiveAmt;
        } else {
            observable.setInsuranceWaiveOff(false);
        }

        if (epWaiveAmt > 0) {
            observable.setEpCostWaiveOff(true);
            observable.setEpCostWaiveAmount(epWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - epWaiveAmt));
            totalAmount = totalAmount - epWaiveAmt;
        } else {
            observable.setEpCostWaiveOff(false);
        }

        if (degreeWaiveAmt > 0) {
            observable.setDecreeWaiveOff(true);
            observable.setDecreeWaiveAmount(degreeWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - degreeWaiveAmt));
            totalAmount = totalAmount - degreeWaiveAmt;
        } else {
            observable.setDecreeWaiveOff(false);
        }

        if (miseWaiveAmt > 0) {
            observable.setMiscellaneousWaiveOff(true);
            observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - miseWaiveAmt));
            totalAmount = totalAmount - miseWaiveAmt;
        } else {
            observable.setMiscellaneousWaiveOff(false);
        }

        if (advertiseWaiveAmt > 0) {
            observable.setAdvertiseWaiveOff(true);
            observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
           // System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - advertiseWaiveAmt));
            totalAmount = totalAmount - advertiseWaiveAmt;
        } else {
            observable.setAdvertiseWaiveOff(false);
        }

        if (overDueIntWaiveAmt > 0) {
            observable.setOverDueIntWaiveOff(true);
            observable.setOverDueIntWaiveAmount(overDueIntWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - overDueIntWaiveAmt));
            totalAmount = totalAmount - overDueIntWaiveAmt;
        } else {
            observable.setOverDueIntWaiveOff(false);
        }
        
        if (recoveryWaiveAmt > 0) {
            observable.setRecoveryWaiveOff(true);
            observable.setRecoveryWaiveAmount(recoveryWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - recoveryWaiveAmt));
            totalAmount = totalAmount - recoveryWaiveAmt;
        } else {
            observable.setRecoveryWaiveOff(false);
        }
        if (measurementWaiveAmt > 0) {
            observable.setMeasurementWaiveOff(true);
            observable.setMeasurementWaiveAmount(measurementWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - measurementWaiveAmt));
            totalAmount = totalAmount - measurementWaiveAmt;
        } else {
            observable.setMeasurementWaiveOff(false);
        }
        
        
        if (koleFieldExpenseWaiveAmt > 0) {
            observable.setKoleFieldExpenseWaiveOff(true);
            observable.setKoleFieldExpenseWaiveAmount(koleFieldExpenseWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - koleFieldExpenseWaiveAmt));
            totalAmount = totalAmount - koleFieldExpenseWaiveAmt;
        } else {
            observable.setKoleFieldExpenseWaiveOff(false);
        }
        
          if (koleFieldOperationWaiveAmt > 0) {
            observable.setKoleFieldOperationWaiveOff(true);
            observable.setKoleFieldOperationWaiveAmount(koleFieldOperationWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - koleFieldOperationWaiveAmt));
            totalAmount = totalAmount - koleFieldOperationWaiveAmt;
        } else {
            observable.setKoleFieldOperationWaiveOff(false);
        }
        
        return totalAmount;
    }   
       
       
    private void btnWaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaiveActionPerformed
        if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0.0) {
            allWaiveMap = observable.getALL_LOAN_AMOUNT();
            if (allWaiveMap != null && allWaiveMap.size() > 0) {
                showEditWaiveTableUI(allWaiveMap);
            }
            returnWaiveMap = waiveOffEditInterestAmt();
            // Added by nithya on 10-08-2020 for KD-1982
            if (asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI")
                    && returnWaiveMap != null && returnWaiveMap.size() > 0) {
             double totalEMIAmt =  setEMILoanWaiveAmount();
            }
        } else {
            ClientUtil.displayAlert("amount should not be zero or empty");
            return;
        }
        // TODO add your handling code here:
}//GEN-LAST:event_btnWaiveActionPerformed
 private HashMap waiveOffEditInterestAmt() {
        double totalWaiveamt = 0;
        double editWaiveOffTransAmt = 0;
        HashMap resultWaiveMap = new HashMap();
        ArrayList singleList = new ArrayList();
        if (editableWaiveUI != null) {
            ArrayList list = editableWaiveUI.getTableData();
            for (int i = 0; i < list.size(); i++) {
                singleList = (ArrayList) list.get(i);
                totalWaiveamt += CommonUtil.convertObjToDouble(singleList.get(2));
                resultWaiveMap.put(singleList.get(0), singleList.get(2));
            }
        }
        resultWaiveMap.put("Total_WaiveAmt", CommonUtil.convertObjToStr(totalWaiveamt));
        return resultWaiveMap;
    }
 private void chkVerifyAllActionPerformed(java.awt.event.ActionEvent evt) {
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            panTransferEnableDisable(true);
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            lblAccNoGl.setEnabled(false);
        }
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_EDIT) {
            lblAccNoGl.setEnabled(false);
        }
        alreadyExistDeposit = true;
        reconcilebtnDisable = true;
        updationTransfer();
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_VIEW) {
            isRowClicked = true;
            cboProductType.setEnabled(false);
            cboProductID.setEnabled(false);
            txtAccountHeadValue.setEnabled(false);
            btnAccountHead.setEnabled(false);
            tdtValueDt.setEnabled(true);
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            txtAccountNo.setEnabled(false);
        }
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (prodType.equals("TD") && flagDepLink == true) {
            btnAccountNo.setEnabled(false);
            btnTransDelete.setEnabled(false);
            cboInstrumentType.setEnabled(false);
            txtParticulars.setEnabled(false);
        }
        reconcilebtnDisable = false;
        if ((observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) && prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            String act_num = observable.getAccountNo();
            HashMap inputMap = new HashMap();
            double avbal = 0.0;
            HashMap balMap = new HashMap();
            balMap = getAvailableAndShadowBal();
            inputMap.put("ACCOUNTNO", act_num);
            double tblSumAmt = 0.0;
            for (int i = 0; i < tblTransList.getRowCount(); i++) {
                String tblActNum = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 0));
                if (act_num.equalsIgnoreCase(tblActNum) && act_num != null) {
                    double tblAmt = CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
                    tblSumAmt += tblAmt;
                }
            }
            boolean cont = checkForMinAmt(inputMap, 0, 0, CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue(), 0, tblSumAmt);
            if (!cont) {
                btnAuthorize.setEnabled(false);
                btnRejection.setEnabled(false);
                btnException.setEnabled(false);
                return;
            }
        }
        //Added By Suresh
        if (!prodType.equals("GL")) {
            String act_num = observable.getAccountNo();
            setCustHouseName(act_num);
        } else {
            if (txtAccountHeadValue.getText().length() > 0 || observable.getAccountHeadId().length() > 0) {
                observable.getAccountHead();
                this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
            }
        }
      
//added by anjuanand on 23-08-2014
  if(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE){   
     tblTransList.setEnabled(false);
     tblTransList.requestFocus(false);
     btnAuthorize.setEnabled(true);
     btnAuthorize.setFocusable(true);
     btnAuthorize.setFocusPainted(true);
     btnAuthorize.requestFocus(true);
     tblTransList.clearSelection();
    }
 }

    private void setFieldNames() {
        btnAccountNo.setName("btnAccountNo");
        btnAdd.setName("btnAdd");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnReport.setName("btnReport");
        btnSave.setName("btnSave");
        cboCurrency.setName("cboCurrency");
        cboInstrumentType.setName("cboInstrumentType");
        cboProductID.setName("cboProductID");
        lblTransDt.setName("lblTransDt");
        lblTransDtValue.setName("lblTransDtValue");
        lblValueDt.setName("lblValueDt");
        tdtValueDt.setName("tdtValueDt");
        lblAccountHead.setName("lblAccountHead");
        txtAccountHeadValue.setName("txtAccountHeadValue");
        lblAccountNo.setName("lblAccountNo");
        lblAmount.setName("lblAmount");
        //        lblAvailBalance.setName("lblAvailBalance");
        //        lblAvailBalanceValue.setName("lblAvailBalanceValue");
        lblBatchID.setName("lblBatchID");
        lblBatchIDValue.setName("lblBatchIDValue");
        //        lblCategory.setName("lblCategory");
        //        lblCategoryValue.setName("lblCategoryValue");
        //        lblClrBalance.setName("lblClrBalance");
        //        lblClrBalanceValue.setName("lblClrBalanceValue");
        //        lblConstitution.setName("lblConstitution");
        //        lblConstitutionValue.setName("lblConstitutionValue");
        //        lblCurCaption.setName("lblCurCaption");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentNo.setName("lblInstrumentNo");
        lblInstrumentType.setName("lblInstrumentType");
        //        lblOpMode.setName("lblOpMode");
        //        lblOpModeValue.setName("lblOpModeValue");
        //        lblOpeningDate.setName("lblOpeningDate");
        //        lblOpeningDateValue.setName("lblOpeningDateValue");
        lblParticulars.setName("lblParticulars");
        lblNarration.setName("lblNarration");
        lblProductID.setName("lblProductID");
        //        lblRemarks.setName("lblRemarks");
        //        lblRemarksValue.setName("lblRemarksValue");
        //        lblShadowCr.setName("lblShadowCr");
        //        lblShadowCrValue.setName("lblShadowCrValue");
        //        lblShadowDr.setName("lblShadowDr");
        //        lblShadowDrValue.setName("lblShadowDrValue");
        lblStatus.setName("lblStatus");
        lblStatusValue.setName("lblStatusValue");
        lblTokenNo.setName("lblTokenNo");
        lblTransactionID.setName("lblTransactionID");
        lblTransactionIDValue.setName("lblTransactionIDValue");
        //        lblTtlBalance.setName("lblTtlBalance");
        //        lblTtlBalanceValue.setName("lblTtlBalanceValue");
        lblTtlCrAmount.setName("lblTtlCrAmount");
        lblTtlCrAmountValue.setName("lblTtlCrAmountValue");
        lblTtlCrInstr.setName("lblTtlCrInstr");
        lblTtlCrInstrValue.setName("lblTtlCrInstrValue");
        lblTtlDrAmount.setName("lblTtlDrAmount");
        lblTtlDrAmountValue.setName("lblTtlDrAmountValue");
        lblTtlDrInstr.setName("lblTtlDrInstr");
        lblTtlDrInstrValue.setName("lblTtlDrInstrValue");
        //        panAccInfo.setName("panAccInfo");
        panAccountNo.setName("panAccountNo");
//        panAmountPanel.setName("panAmountPanel");
        panFieldPanel.setName("panFieldPanel");
        panInfoPanel.setName("panInfoPanel");
        panStatus.setName("panStatus");
        panTransDetail.setName("panTransDetail");
        panTransInfo.setName("panTransInfo");
        //        sptBalanceDetail.setName("sptBalanceDetail");
        tdtInstrumentDate.setName("tdtInstrumentDate");
        txtAccountNo.setName("txtAccountNo");
        txtAmount.setName("txtAmount");
        txtInstrumentNo1.setName("txtInstrumentNo1");

        txtInstrumentNo2.setName("txtInstrumentNo2");
        txtParticulars.setName("txtParticulars");
        txtNarration.setName("txtNarration");
        txtTokenNo.setName("txtTokenNo");
        lblMsg.setName("lblMsg");
        panInstrumentNo.setName("panInstrumentNo");
        btnAuthorize.setName("btnAuthorize");
        btnException.setName("btnException");
        btnRejection.setName("btnRejection");
        cboProductType.setName("cboProductType");
        //        btnShadowCredit.setName("btnShadowCredit");
        //	btnShadowDebit.setName("btnShadowDebit");
        lblSep3.setName("lblSep3");
        rdoBulkTransaction_Yes.setName("rdoBulkTransaction_Yes");
        rdoBulkTransaction_No.setName("rdoBulkTransaction_No");
        panBulkTransaction.setName("panBulkTransaction");
        lblBulkTransaction.setName("lblBulkTransaction");

    }

    private void internationalize() {
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.transfer.TransferRB", ProxyParameters.LANGUAGE);
        //        lblCategoryValue.setText(resourceBundle.getString("lblCategoryValue"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTtlDrAmount.setText(resourceBundle.getString("lblTtlDrAmount"));
        //	lblOpMode.setText(resourceBundle.getString("lblOpMode"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTransDt.setText(resourceBundle.getString("lblTransDt"));
        lblTransDtValue.setText(resourceBundle.getString("lblTransDtValue"));
        lblValueDt.setText(resourceBundle.getString("lblValueDt"));
        lblBatchIDValue.setText(resourceBundle.getString("lblBatchIDValue"));
        //	((TitledBorder)panAccInfo.getBorder()).setTitle(resourceBundle.getString("panAccInfo"));
        //	lblAvailBalanceValue.setText(resourceBundle.getString("lblAvailBalanceValue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //	lblShadowCrValue.setText(resourceBundle.getString("lblShadowCrValue"));
        //	lblTtlBalanceValue.setText(resourceBundle.getString("lblTtlBalanceValue"));
        lblSep3.setText(resourceBundle.getString("lblSep3"));
        lblTransactionID.setText(resourceBundle.getString("lblTransactionID"));
        lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        //	lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        //	lblCurCaption.setText(resourceBundle.getString("lblCurCaption"));
        ((TitledBorder) panTransInfo.getBorder()).setTitle(resourceBundle.getString("panTransInfo"));
        lblTtlDrAmountValue.setText(resourceBundle.getString("lblTtlDrAmountValue"));
        lblTtlCrAmountValue.setText(resourceBundle.getString("lblTtlCrAmountValue"));
        //	lblCategory.setText(resourceBundle.getString("lblCategory"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnAccountNo.setText(resourceBundle.getString("btnAccountNo"));
        //	lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        lblTransactionIDValue.setText(resourceBundle.getString("lblTransactionIDValue"));
        //	lblClrBalance.setText(resourceBundle.getString("lblClrBalance"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblTtlDrInstr.setText(resourceBundle.getString("lblTtlDrInstr"));
        //	lblAvailBalance.setText(resourceBundle.getString("lblAvailBalance"));
        //	lblOpeningDateValue.setText(resourceBundle.getString("lblOpeningDateValue"));
        lblBatchID.setText(resourceBundle.getString("lblBatchID"));
        ((TitledBorder) panTransDetail.getBorder()).setTitle(resourceBundle.getString("panTransDetail"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        //	btnShadowCredit.setText(resourceBundle.getString("btnShadowCredit"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        //	btnShadowDebit.setText(resourceBundle.getString("btnShadowDebit"));
        btnReport.setText(resourceBundle.getString("btnReport"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        //	lblShadowDrValue.setText(resourceBundle.getString("lblShadowDrValue"));
        lblTtlCrInstrValue.setText(resourceBundle.getString("lblTtlCrInstrValue"));
        //	lblClrBalanceValue.setText(resourceBundle.getString("lblClrBalanceValue"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblNarration.setText(resourceBundle.getString("lblNarration"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //	lblTtlBalance.setText(resourceBundle.getString("lblTtlBalance"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        //	lblShadowDr.setText(resourceBundle.getString("lblShadowDr"));
        //	lblConstitutionValue.setText(resourceBundle.getString("lblConstitutionValue"));
        lblTtlCrAmount.setText(resourceBundle.getString("lblTtlCrAmount"));
        txtAccountHeadValue.setText(resourceBundle.getString("lblAccountHeadValue"));
        //	lblOpModeValue.setText(resourceBundle.getString("lblOpModeValue"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        //	lblRemarksValue.setText(resourceBundle.getString("lblRemarksValue"));
        //	lblOpeningDate.setText(resourceBundle.getString("lblOpeningDate"));
        btnRejection.setText(resourceBundle.getString("btnRejection"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblTtlDrInstrValue.setText(resourceBundle.getString("lblTtlDrInstrValue"));
        lblStatusValue.setText(resourceBundle.getString("lblStatusValue"));
        lblTtlCrInstr.setText(resourceBundle.getString("lblTtlCrInstr"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //	lblShadowCr.setText(resourceBundle.getString("lblShadowCr"));
        lblTokenNo.setText(resourceBundle.getString("lblTokenNo"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblAuthBy.setText(resourceBundle.getString("lblAuthBy"));

        rdoBulkTransaction_Yes.setText(resourceBundle.getString("rdoBulkTransaction_Yes"));
        rdoBulkTransaction_No.setText(resourceBundle.getString("rdoBulkTransaction_No"));
        lblBulkTransaction.setText(resourceBundle.getString("lblBulkTransaction"));

    }

    /*
     * Use this method to pick all the values from the screen, remainign fields
     * may be coming from some other querying method
     */
    private void updateOBFields() {

        /*
         * anyways we have to pass the transaction id, it will be useful in case
         * of NEW operation only
         */
        //if(flagGLAcc)
        observable.setAccNumGl(accNo1);//bb1
        // else 
        // {
        //     accNo1="";
        //    observable.setAccNumGl(accNo1);
        // }
        observable.setScreen(this.getScreen());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setModule(this.getModule());
        observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
        observable.setBatchId(this.lblBatchIDValue.getText());
        observable.setTransId(lblTransactionIDValue.getText());
        observable.setAccountNo(txtAccountNo.getText());
        observable.setInstType((String) ((ComboBoxModel) (cboInstrumentType.getModel())).getKeyForSelected());

        observable.setInstrumentNo1(txtInstrumentNo1.getText());
        observable.setInstrumentNo2(txtInstrumentNo2.getText());
        observable.setInstrumentDate(tdtInstrumentDate.getDateValue());

        observable.setCurrencyType((String) ((ComboBoxModel) (cboCurrency.getModel())).getKeyForSelected());

        observable.setProductId((String) ((ComboBoxModel) (this.cboProductID.getModel())).getKeyForSelected());
        observable.setProductTypeValue(observable.getProductId());
        observable.setMainProductTypeValue((String) ((ComboBoxModel) (this.cboProductType.getModel())).getKeyForSelected());
        observable.setAmount(txtAmount.getText());
        observable.setTransferAmount(txtAmount.getText());
        observable.setTransDate(DateUtil.getStringDate(observable.getCurDate()));
        observable.setParticulars(txtParticulars.getText());
        observable.setRdoBulkTransaction_Yes(rdoBulkTransaction_Yes.isSelected());
        observable.setRdoBulkTransaction_No(rdoBulkTransaction_No.isSelected());
        observable.setNarration(txtNarration.getText());
       
        
        if (observable.getTransType().equals(CommonConstants.CREDIT)) {
            observable.setServiceTax_Map(serviceTax_Map);
            observable.setGlserviceTax_Map(glserviceTax_Map);
            observable.setLblServiceTaxval(lblServiceTaxval.getText());
        }
        //observable.setTokenNo(txtTokenNo.getText());
        //        observable.setShadowCr(this.lblShadowCrValue.getText());
        //        observable.setShadowDr(this.lblShadowDrValue.getText());
    }

    public static void main(String[] arg) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            JFrame jf = new JFrame();
            TransferUI gui = new TransferUI();
            jf.getContentPane().add(gui);
            jf.setSize(800, 625);
            jf.show();
            gui.show();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * Getter for property batchIdForEdit.
     *
     * @return Value of property batchIdForEdit.
     *
     */
    public java.lang.String getBatchIdForEdit() {
        return batchIdForEdit;
    }

    /**
     * Setter for property batchIdForEdit.
     *
     * @param batchIdForEdit New value of property batchIdForEdit.
     *
     */
    public void setBatchIdForEdit(java.lang.String batchIdForEdit) {
        this.batchIdForEdit = batchIdForEdit;
    }

    private void resetUIData() {
        this._intTransferNew = false;
        this.transactionIdForEdit = null;
        this.batchIdForEdit = null;
        this.viewType = -1;
        this.rowForEdit = -1;
    }

    /**
     * Getter for property viewType.
     *
     * @return Value of property viewType.
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * Setter for property viewType.
     *
     * @param viewType New value of property viewType.
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(true);
    }

    /**
     * Getter for property transactionDateForEdit.
     *
     * @return Value of property transactionDateForEdit.
     */
    public java.util.Date getTransactionDateForEdit() {
        return transactionDateForEdit;
    }

    /**
     * Setter for property transactionDateForEdit.
     *
     * @param transactionDateForEdit New value of property
     * transactionDateForEdit.
     */
    public void setTransactionDateForEdit(java.util.Date transactionDateForEdit) {
        this.transactionDateForEdit = transactionDateForEdit;
    }

    /**
     * Getter for property transactionInitBranForEdit.
     *
     * @return Value of property transactionInitBranForEdit.
     */
    public java.lang.String getTransactionInitBranForEdit() {
        return transactionInitBranForEdit;
    }

    /**
     * Setter for property transactionInitBranForEdit.
     *
     * @param transactionInitBranForEdit New value of property
     * transactionInitBranForEdit.
     */
    public void setTransactionInitBranForEdit(java.lang.String transactionInitBranForEdit) {
        this.transactionInitBranForEdit = transactionInitBranForEdit;
    }
    /*
     * called to check whether the entered amount is less than the amt to be
     * maintained in the act
     */

    public boolean checkForMinAmt(HashMap inputMap, double enteredAmt, double shadowDeb, double avbal, double tblAmt, double tblAmtFinal) {
        double amt = 0.0;
        double amount = 0.0;
        boolean cont = true;
        HashMap minMap = new HashMap();
//         chqBalList= ClientUtil.executeQuery("getMinBalance", inputMap);
        if(chqBalList == null || (chqBalList != null && chqBalList.size() == 0)){
            chqBalList = ClientUtil.executeQuery("getMinBalance", inputMap);
        }
        if (chqBalList != null && chqBalList.size() > 0) {
            minMap = (HashMap) chqBalList.get(0);
            //added by Chithra on 21-10-14(merged)
            if (minMap.containsKey("TEMP_OD_ALLOWED") && CommonUtil.convertObjToStr(minMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                //avbal += CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT"));
                //avbal = CommonUtil.convertObjToDouble(minMap.get("AVAILABLE_BALANCE")) + CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT")); // Added by nithya 5-09-2016
                if (CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT")) > 0) {
                    if (observable.getTransType().equals(CommonConstants.DEBIT)) {
                        String todClearBalance = transDetails.getCBalance();
                        double todClearBal = Double.parseDouble(todClearBalance);
                        if (todClearBal - enteredAmt < 0) {
                            HashMap renewalCheckMap = new HashMap();
                            renewalCheckMap.put("ACT_NUM", inputMap.get("ACCOUNTNO"));                            
                            List chkLst = ClientUtil.executeQuery("checkODForRenewal", renewalCheckMap);
                            if (chkLst != null && chkLst.size() > 0) {
                                ClientUtil.displayAlert("OD period over. Please renew");
                                cont = false;
                            }
                        }
                    }
                }				
            }
            //End...............
            double withoutChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITHOUT_CHQ")).doubleValue();
            double withChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITH_CHQ")).doubleValue();
            String chqBk = CommonUtil.convertObjToStr(minMap.get("CHQ_BOOK")).toUpperCase();
            int chqcount = CommonUtil.convertObjToInt(minMap.get("CNT"));
            //System.out.println("Inside checkForMinAmount :");
            //if (chqBk.equals("Y") && chqcount > 0) { //KD-3678 : Transaction IDs/Batch ID Missing Issue.
            if (chqBk.equals("Y")) {
                amt = withChq;
            } else {
                amt = withoutChq;
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                amount = (avbal - shadowDeb) - enteredAmt;
            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                amount = (avbal - enteredAmt) - Math.abs(tblAmtFinal - shadowDeb);
            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                amount = avbal - tblAmtFinal;
            }
            if (amount < amt) {
                int c = ClientUtil.confirmationAlert("Minimum Balance not maintained ..Do You  Want To Continue?");
                int d = 0;
                if (c != d) {
                    cont = false;
                    return cont;
                }
            } else if ((amt == 0.0) && (enteredAmt == (avbal - shadowDeb))) {
                int a = ClientUtil.confirmationAlert("A/c balance will become Zero.. Do you want to continue?");
                int b = 0;
                if (a != b) {
                    cont = false;
                }
                return cont;
            }
            //KD-3678 : Transaction IDs/Batch ID Missing Issue.
            if (prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT) && ! rdoBulkTransaction_Yes.isSelected()) {                
                if (enteredAmt > (avbal - shadowDeb)) {
                    ClientUtil.displayAlert("Entered Amount Exceeds The Limit");
                    txtAmount.setText("");
                    txtAmount.grabFocus();
                    cont = false;
                } else if (((avbal - shadowDeb) - enteredAmt) < amt) {
                    ClientUtil.displayAlert("Entered Amount Exceeds The Limit");
                    txtAmount.setText("");
                    txtAmount.grabFocus();
                    cont = false;
                }
            }
        }
        return cont;

    }

    public HashMap getAvailableAndShadowBal() {
        double avbal = 0.0;
        double shadowDeb = 0.0;
        ArrayList tableVal = transDetails.getTblDataArrayList();
        for (int k = 0; k < tableVal.size(); k++) {
            ArrayList balList = ((ArrayList) tableVal.get(k));
            if (balList.get(0).equals("Available Balance")) {
                String str = CommonUtil.convertObjToStr(balList.get(1));
                str = str.replaceAll(",", "");
                avbal = CommonUtil.convertObjToDouble(str).doubleValue();
            }
            if (balList.get(0).equals("Shadow Debit")) {
                String shawdowStr = CommonUtil.convertObjToStr(balList.get(1));
                shawdowStr = shawdowStr.replaceAll(",", "");
                shadowDeb = CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
            }
        }
        HashMap balMap = new HashMap();
        balMap.put("AV_BAL", new Double(avbal));
        balMap.put("SH_DEBIT", new Double(shadowDeb));
        return balMap;
    }

    public String getOrgBranch() {
        return orgBranch;
    }

    public void setOrgBranch(String orgBranch) {
        this.orgBranch = orgBranch;
    }

    public String getOrgBranchName() {
        return orgBranchName;
    }

    public void setOrgBranchName(String orgBranchName) {
        this.orgBranchName = orgBranchName;
    }

    public Date getOrgOrRespAdviceDt() {
        return orgOrRespAdviceDt;
    }

    public void setOrgOrRespAdviceDt(Date orgOrRespAdviceDt) {
        this.orgOrRespAdviceDt = orgOrRespAdviceDt;
    }

    public String getOrgOrRespAdviceNo() {
        return orgOrRespAdviceNo;
    }

    public void setOrgOrRespAdviceNo(String orgOrRespAdviceNo) {
        this.orgOrRespAdviceNo = orgOrRespAdviceNo;
    }

    public double getOrgOrRespAmout() {
        return orgOrRespAmout;
    }

    public void setOrgOrRespAmout(double orgOrRespAmout) {
        this.orgOrRespAmout = orgOrRespAmout;
    }

    public String getOrgOrRespBranchId() {
        return orgOrRespBranchId;
    }

    public void setOrgOrRespBranchId(String orgOrRespBranchId) {
        this.orgOrRespBranchId = orgOrRespBranchId;
    }

    public String getOrgOrRespBranchName() {
        return orgOrRespBranchName;
    }

    public void setOrgOrRespBranchName(String orgOrRespBranchName) {
        this.orgOrRespBranchName = orgOrRespBranchName;
    }

    public String getOrgOrRespCategory() {
        return orgOrRespCategory;
    }

    public void setOrgOrRespCategory(String orgOrRespCategory) {
        this.orgOrRespCategory = orgOrRespCategory;
    }

    public String getOrgOrRespDetails() {
        return orgOrRespDetails;
    }

    public void setOrgOrRespDetails(String orgOrRespDetails) {
        this.orgOrRespDetails = orgOrRespDetails;
    }

    public String getOrgOrRespTransType() {
        return orgOrRespTransType;
    }

    public void setOrgOrRespTransType(String orgOrRespTransType) {
        this.orgOrRespTransType = orgOrRespTransType;
    }

    public Date getBackDatedTransDate() {
        return backDatedTransDate;
    }

    public void setBackDatedTransDate(Date backDatedTransDate) {
        this.backDatedTransDate = backDatedTransDate;
    }

    // Added by nithya on 02-11-2018 for KD 313 : GST for Recurring deposit penal
    private void calculateRDServiceTaxAmt(double penalAmt) {
        String taxApplicable = "";
        HashMap taxMap;
        String actNum = CommonUtil.convertObjToStr(txtAccountNo.getText());
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        //double taxAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount());   
        double taxAmt = penalAmt; // Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId);
        List depositClosingHeadLst = ClientUtil.executeQuery("getDepositClosingHeads", prodMap);
        if (depositClosingHeadLst != null && depositClosingHeadLst.size() > 0) {
            HashMap depositClosingHeadMap = (HashMap) depositClosingHeadLst.get(0);
            if (CommonUtil.convertObjToDouble(transDetails.getPenalAmount()) > 0) {
                List taxSettingsList = new ArrayList();
                String achd = CommonUtil.convertObjToStr(depositClosingHeadMap.get("DELAYED_ACHD"));
                HashMap whereMap = new HashMap();
                whereMap.put("AC_HD_ID", achd);
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                            if (value.containsKey("SERVICE_TAX_ID") && value.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID")).length() > 0) {
                                taxMap = new HashMap();
                                taxMap.put("SETTINGS_ID", value.get("SERVICE_TAX_ID"));
                                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                                taxSettingsList.add(taxMap);
                            }
                        }
                    }
                }
                if (taxSettingsList != null && taxSettingsList.size() > 0) {
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curr_dt.clone());                   
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    try {
                        objServiceTax = new ServiceTaxCalculation();                    
                        glserviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                        if (glserviceTax_Map != null && glserviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                            String amt = CommonUtil.convertObjToStr(glserviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                            //lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            lblServiceTaxval.setText(CommonUtil.convertObjToStr(amt)); // Changed by nithya on 23-04-2020 for KD-1837
                            observable.setLblServiceTaxval(lblServiceTaxval.getText());
                            //glserviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            glserviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);// Changed by nithya on 23-04-2020 for KD-1837
                        } else {
                            lblServiceTaxval.setText("0.00");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    lblServiceTaxval.setText("0.00");
                }
            }
        }
    }
    // End
    
    
    // Added by nithya on 30-04-2021 for KD-2844 & KD-2801
    public void modifyTransData(Object objTextUI) {
        String productType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        if (productType.equals("TL")) {
            if (observable.getTransType().equals(CommonConstants.CREDIT) && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                if (!observable.getInstalType().isEmpty() && !observable.getInstalType().equals("") && !observable.getInstalType().equals(null) && observable.getInstalType().equals("EMI") && observable.getEMIinSimpleInterest().equalsIgnoreCase("N")) {
                    double totalEMIAmt = setEMIAmount();
                    observable.setTransferAmount(String.valueOf(totalEMIAmt));
                    txtAmount.setText(observable.getAmount());
                    if (txtAmount.getText().length() > 0) {
                        totalEMIAmt = setEMILoanWaiveAmount();
                    }
                    observable.setTransferAmount(String.valueOf(totalEMIAmt));
                    txtAmount.setText(observable.getAmount());
                }
            }
        }
        if (productType.equals("TD") && depBehavesLike.equals("RECURRING") && observable.getTransType().equals(CommonConstants.CREDIT) && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            double amts = 0;
            String depNo = CommonUtil.convertObjToStr(txtAccountNo.getText());
            HashMap recurringMap = new HashMap();
            if (depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            recurringMap.put("DEPOSIT_NO", depNo.trim());
            double depAmount = 0;
            List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
            if (lst != null && lst.size() > 0) {
                recurringMap = (HashMap) lst.get(0);
                depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT"));
                amts = CommonUtil.convertObjToDouble(depAmount);
            }
            double amt2 = (amts) * noofInstallment2;
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            if (penalAmt > 0 && amt2 > 0) {
                amt2 = amt2 + penalAmt;
                calculateRDServiceTaxAmt(penalAmt);
            }
            if (amt2 != 0 || amt2 != 0.0) {
                observable.setTransferAmount(CommonUtil.convertObjToStr(amt2));
                txtAmount.setText(observable.getAmount());
            } else {
                observable.setTransferAmount("");
                txtAmount.setText(observable.getAmount());
            }
        }
    }
    
    
    public HashMap setRepaymentData() {
        HashMap repayData = new HashMap();
//    //HashMap prodLevelValues = observable.getCompFreqRoundOffValues();
        HashMap newmap = new HashMap();
        newmap.put("ACCT_NUM", txtAccountNo.getText());
        List lst = ClientUtil.executeQuery("getLoansRepaymentData", newmap);
        if (lst != null && lst.size() > 0) {
            HashMap resltMap = (HashMap) lst.get(0);
            if (resltMap != null && resltMap.size() > 0) {
                repayData.put("ACT_NO", txtAccountNo.getText());
                repayData.put("NEW_INSTALLMENT", "NEW_INSTALLMENT");
                repayData.put("FROM_DATE", resltMap.get("FROM_DT"));
                repayData.put("REPAYMENT_START_DT", resltMap.get("FIRST_INSTALL_DT"));
                repayData.put("TO_DATE", resltMap.get("LAST_INSTALL_DT"));
                repayData.put("NO_INSTALL", resltMap.get("NO_INSTALLMENTS"));
                repayData.put("ISDURATION_DDMMYY", "YES");
                repayData.put("INTEREST_TYPE", "COMPOUND");
                repayData.put("DURATION_YY", resltMap.get("NO_INSTALLMENTS"));
                repayData.put("COMPOUNDING_PERIOD", resltMap.get("REPAYMENT_FREQUENCY"));
                repayData.put("REPAYMENT_TYPE", resltMap.get("INSTALL_TYPE"));
                repayData.put("PRINCIPAL_AMOUNT", resltMap.get("LOAN_AMOUNT"));
                repayData.put("ROUNDING_FACTOR", "1_RUPEE");
                HashMap prodLevelValues = observable.getCompFreqRoundOffValues(CommonUtil.convertObjToStr(resltMap.get("PROD_ID")));
                repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
                repayData.put("REPAYMENT_FREQUENCY", resltMap.get("REPAYMENT_FREQUENCY"));
                repayData.put("SCHEDULE_ID", resltMap.get("SCHEDULE_NO"));
                String round = transDetails.getRound();
                double roi = CommonUtil.convertObjToDouble(asAndWhenMap.get("ROI"));
                //System.out.println("roi in roi??>>" + roi);
                if (round.equals("NEAREST_VALUE")) {
                    roi = (double) getNearest((long) (roi * 100), 100) / 100;
                } else if (round.equals("LOWER_VALUE")) {
                    roi = (double) roundOffLower((long) (roi * 100), 100) / 100;
                } else if (round.equals("HIGHER_VALUE")) {
                    roi = (double) higher((long) (roi * 100), 100) / 100;
                } else {
                    roi = new Double(roi);
                }
                repayData.put("INTEREST", roi);
                repayData.put("BALANCE_AMT", resltMap.get("LOAN_BALANCE_PRINCIPAL"));
                repayData.put("PAID_AMT", resltMap.get("PAID"));
            }

        }
        //System.out.println("repayData ---------- :" + repayData);
        return repayData;
    }
    
     public void issuspenseAcctExsistInAuthList(String actNumber) {
        if (actNumber != null && !actNumber.equals("")) {//Added By Nidhin
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM", actNumber);
            whereMap.put("CURR_DT", ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getNoOfUnauthorizedTransaction", whereMap);
            if (unAuthList != null && unAuthList.size() > 0) {
                ClientUtil.showMessageWindow("Pending For Authorization ....!");               
                return;
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAddCredit;
    private com.see.truetransact.uicomponent.CButton btnAddDebit;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnOrgOrResp;
    private com.see.truetransact.uicomponent.CButton btnReconsile;
    private com.see.truetransact.uicomponent.CButton btnRejection;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTransDelete;
    private com.see.truetransact.uicomponent.CButton btnTransSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewTermLoanDetails;
    private com.see.truetransact.uicomponent.CButton btnWaive;
    private com.see.truetransact.uicomponent.CButtonGroup bulkTransactionGroup;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CCheckBox chkVerifyAll;
    private com.see.truetransact.uicomponent.CLabel lblAccNoGl;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDescValue;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAuthBy;
    private com.see.truetransact.uicomponent.CLabel lblAuthByValue;
    private com.see.truetransact.uicomponent.CLabel lblBatchID;
    private com.see.truetransact.uicomponent.CLabel lblBatchIDValue;
    private com.see.truetransact.uicomponent.CLabel lblBulkTransaction;
    private com.see.truetransact.uicomponent.CLabel lblCreatedBy;
    private com.see.truetransact.uicomponent.CLabel lblCreatedByValue;
    private com.see.truetransact.uicomponent.CLabel lblCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblLinkId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSep3;
    private com.see.truetransact.uicomponent.CLabel lblSep4;
    private com.see.truetransact.uicomponent.CLabel lblSep5;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatusValue;
    private javax.swing.JLabel lblTBSep1;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTransDt;
    private com.see.truetransact.uicomponent.CLabel lblTransDtValue;
    private com.see.truetransact.uicomponent.CLabel lblTransactionID;
    private com.see.truetransact.uicomponent.CLabel lblTransactionIDValue;
    private com.see.truetransact.uicomponent.CLabel lblTtlCrAmount;
    private com.see.truetransact.uicomponent.CLabel lblTtlCrAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTtlCrInstr;
    private com.see.truetransact.uicomponent.CLabel lblTtlCrInstrValue;
    private com.see.truetransact.uicomponent.CLabel lblTtlDrAmount;
    private com.see.truetransact.uicomponent.CLabel lblTtlDrAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTtlDrInstr;
    private com.see.truetransact.uicomponent.CLabel lblTtlDrInstrValue;
    private com.see.truetransact.uicomponent.CLabel lblValueDt;
    private javax.swing.JMenuBar mbrTransfer;
    private javax.swing.JMenuItem mitAdd;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitExceptions;
    private javax.swing.JMenuItem mitRejection;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHeadValue;
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panBulkTransaction;
    private com.see.truetransact.uicomponent.CPanel panFieldPanel;
    private com.see.truetransact.uicomponent.CPanel panGlAccNo;
    private com.see.truetransact.uicomponent.CPanel panInfoPanel;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNo;
    private com.see.truetransact.uicomponent.CPanel panMainTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CPanel panTransButtons;
    private com.see.truetransact.uicomponent.CPanel panTransDetail;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CPanel panTransInfo;
    private com.see.truetransact.uicomponent.CRadioButton rdoBulkTransaction_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBulkTransaction_Yes;
    private javax.swing.JSeparator sptProcess1;
    private javax.swing.JSeparator sptProcess2;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private com.see.truetransact.uicomponent.CTable tblTransList;
    private com.see.truetransact.uicomponent.CToolBar tbrTransfer;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CDateField tdtValueDt;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadValue;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables
}
