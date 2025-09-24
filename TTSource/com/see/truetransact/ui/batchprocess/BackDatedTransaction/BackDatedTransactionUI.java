
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BackDatedTransactionUI.java
 *
 * Created on March 12, 2014, 3:43 PM 2014 
 */
package com.see.truetransact.ui.batchprocess.BackDatedTransaction;

import java.util.*;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.viewall.ViewRespUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewOrgOrRespUI;
import com.see.truetransact.ui.common.viewall.ViewLoansTransUI;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.transaction.common.TransHOCommonUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;

/*
 * For showing the main interface for the transactions which are for transfer
 * Created on March 12, 2014, 3:43 PM 2014
 *
 * @author Suresh R
 *
 */
public class BackDatedTransactionUI extends CInternalFrame implements Observer, UIMandatoryField {

    private java.util.ResourceBundle resourceBundle;
    private BackDatedTransactionOB observable;
    private HashMap authorizationCheckMap = new HashMap();
    private HashMap mandatoryMap;
    private BackDatedTransactionMRB objMandatoryRB;
    private String transactionIdForEdit;
    private String batchIdForEdit;
    private Date transactionDateForEdit;
    private String transactionInitBranForEdit;
    private int rowForEdit;
    private int viewType;
    private boolean _intTransferNew;
    private TransDetailsUI transDetails = null;
    private TransCommonUI transCommonUI = null;
    private TransHOCommonUI transHOCommonUI = null;
    TermDepositUI termDepositUI;
    private String prodType = "";
    private HashMap loanDebitType = new HashMap();
    final int ACT_NUM = 200;
    final int AUTHORIZE = 201;
    final int ACCTHDID = 202;
    final int RECON = 203;
    final int VIEW = 205;
    final int DELETE = 206;
    private CTextField txtDepositAmount;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepLink = false;
    private boolean afterSaveCancel = false;
    private HashMap transferDepMap = new HashMap();
    private HashMap intMap = new HashMap();
    private String depositNo = "";
    private boolean isRowClicked = false;
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
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
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
    ViewRespUI viewRespUI = null;
    //AdviceRequicitionUI adviceRequicitionUI = null;
    //AdviceRequicitionUI adviceRequicition = null;
    private double adviceAmount = 0.0;
    private String adviceBranchId = "";
    private String adviceBranchName = "";
    private String adviceCategory = "";
    private String adviceReqNo = "";
    private java.util.Date adviceDt = null;
    private String adviceTransType = "";
    private String adviceDetails = "";
    ArrayList olist = new ArrayList();
    private boolean updation = false;
    private boolean responding = false;
    private boolean newTransactionInEdit = false;
    private String oldRespAdvNo = "";
    int time = 0;
    private boolean visitedStatus = false;
    private boolean editAlert = false;

    public boolean isVisitedStatus() {
        return visitedStatus;
    }

    public void setVisitedStatus(boolean visitedStatus) {
        this.visitedStatus = visitedStatus;
    }

    /**
     * Creates new form BackDatedTransactionUI
     */
    public BackDatedTransactionUI() {
        // first generate the controls
        curr_dt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
        lblTokenNo.setVisible(false);
        txtTokenNo.setVisible(false);
        this.cboCurrency.setVisible(false);
        btnViewTermLoanDetails.setVisible(true);
        btnViewTermLoanDetails.setEnabled(true);
        btnAdvice.setVisible(false);
        chkSelectAll.setVisible(false);
        chkSelectAll.setSelected(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
//        lblAccountNo.setVisible(false);
//        panAccountNo.setVisible(false);
        lblCustNameValue.setVisible(false);
        lblHouseName.setVisible(false);
//        lblProductID.setVisible(false);
//        cboProductID.setVisible(false);
    }

    private void initStartup() {
        setFieldNames();
        transDetails = new TransDetailsUI(panInfoPanel);
        internationalize();
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panFieldPanel, getMandatoryHashMap());
        initComponentData();
        btnReconsile.setVisible(false);
        ClientUtil.enableDisable(this, false);
        txtAccountHeadValue.setEditable(false);
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
        txtInstrumentNo1.setAllowNumber(true);
        txtInstrumentNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtInstrumentNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtParticulars.setMaxLength(64);
        txtParticulars.setAllowAll(true);
        txtNarration.setMaxLength(128);
        txtNarration.setAllowAll(true);
        txtAccountHeadValue.setAllowAll(true);
    }

    public void setHelpMessage() {
        objMandatoryRB = new BackDatedTransactionMRB();
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
        txtNarration.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNarration"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
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
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtAccountNo", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
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
            observable = new BackDatedTransactionOB();
            observable.addObserver(this);
            observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMenuToolBarPanel() {
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
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblTBSep1 = new javax.swing.JLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblTBSep2 = new javax.swing.JLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnRejection = new com.see.truetransact.uicomponent.CButton();
        lblSep3 = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
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
        btnAdvice = new com.see.truetransact.uicomponent.CButton();
        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransDt = new com.see.truetransact.uicomponent.CLabel();
        lblValueDt = new com.see.truetransact.uicomponent.CLabel();
        tdtValueDt = new com.see.truetransact.uicomponent.CDateField();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblBatchIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionIDValue = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblBatchID = new com.see.truetransact.uicomponent.CLabel();
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
        tdtTransDtValue = new com.see.truetransact.uicomponent.CDateField();
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
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
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
        setMinimumSize(new java.awt.Dimension(810, 660));
        setName("frmTransferFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(810, 660));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
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
        btnAdd.setName("btnAdd"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAdd);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setName("btnEdit"); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
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
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnCancel);

        lblTBSep2.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblTBSep2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setPreferredSize(new java.awt.Dimension(28, 28));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace28);

        btnRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnRejection.setMaximumSize(new java.awt.Dimension(28, 28));
        btnRejection.setMinimumSize(new java.awt.Dimension(28, 28));
        btnRejection.setPreferredSize(new java.awt.Dimension(28, 28));
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
        btnReport.setName("btnReport"); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnReport);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
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

        panTransDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Amount Details"));
        panTransDetail.setMaximumSize(new java.awt.Dimension(435, 140));
        panTransDetail.setMinimumSize(new java.awt.Dimension(435, 140));
        panTransDetail.setName("panTransDetail"); // NOI18N
        panTransDetail.setPreferredSize(new java.awt.Dimension(435, 140));
        panTransDetail.setLayout(new java.awt.GridBagLayout());

        lblInstrumentNo.setText("Instrument No.");
        lblInstrumentNo.setName("lblInstrumentNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(lblInstrumentNo, gridBagConstraints);

        lblInstrumentDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstrumentDate.setText("Instrument Date");
        lblInstrumentDate.setMaximumSize(new java.awt.Dimension(95, 18));
        lblInstrumentDate.setMinimumSize(new java.awt.Dimension(125, 18));
        lblInstrumentDate.setName("lblInstrumentDate"); // NOI18N
        lblInstrumentDate.setPreferredSize(new java.awt.Dimension(95, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(tdtInstrumentDate, gridBagConstraints);

        lblAmount.setText("Amount");
        lblAmount.setName("lblAmount"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(lblAmount, gridBagConstraints);

        lblParticulars.setText("Particulars");
        lblParticulars.setName("lblParticulars"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(lblParticulars, gridBagConstraints);

        txtParticulars.setMinimumSize(new java.awt.Dimension(202, 21));
        txtParticulars.setName("txtParticulars"); // NOI18N
        txtParticulars.setPreferredSize(new java.awt.Dimension(202, 21));
        txtParticulars.setMaxLength(128);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(txtParticulars, gridBagConstraints);

        lblInstrumentType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstrumentType.setText("Instrument Type");
        lblInstrumentType.setMinimumSize(new java.awt.Dimension(133, 18));
        lblInstrumentType.setName("lblInstrumentType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(cboInstrumentType, gridBagConstraints);

        lblTokenNo.setText("Token No.");
        lblTokenNo.setName("lblTokenNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setName("txtTokenNo"); // NOI18N
        txtTokenNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtTokenNo.setMaxLength(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(panTransButtons, gridBagConstraints);

        btnViewTermLoanDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewTermLoanDetails.setText("View");
        btnViewTermLoanDetails.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnViewTermLoanDetails.setMaximumSize(new java.awt.Dimension(60, 27));
        btnViewTermLoanDetails.setMinimumSize(new java.awt.Dimension(60, 27));
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

        cboCurrency.setMinimumSize(new java.awt.Dimension(0, 22));
        cboCurrency.setName("cboCurrency"); // NOI18N
        cboCurrency.setPreferredSize(new java.awt.Dimension(0, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTransDetail.add(cboCurrency, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation(14,2));
        txtAmount.setMaxLength(17);
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(txtAmount, gridBagConstraints);

        btnReconsile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReconsile.setMaximumSize(new java.awt.Dimension(0, 0));
        btnReconsile.setMinimumSize(new java.awt.Dimension(0, 0));
        btnReconsile.setPreferredSize(new java.awt.Dimension(0, 0));
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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(lblNarration, gridBagConstraints);

        txtNarration.setMinimumSize(new java.awt.Dimension(202, 21));
        txtNarration.setName("txtParticulars"); // NOI18N
        txtNarration.setPreferredSize(new java.awt.Dimension(202, 21));
        txtParticulars.setMaxLength(128);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetail.add(txtNarration, gridBagConstraints);

        btnOrgOrResp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOrgOrResp.setText("R");
        btnOrgOrResp.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnOrgOrResp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrgOrRespActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        panTransDetail.add(btnOrgOrResp, gridBagConstraints);

        btnAdvice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAdvice.setText("AR");
        btnAdvice.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAdvice.setMaximumSize(new java.awt.Dimension(82, 27));
        btnAdvice.setMinimumSize(new java.awt.Dimension(82, 27));
        btnAdvice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdviceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panTransDetail.add(btnAdvice, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFieldPanel.add(panTransDetail, gridBagConstraints);

        panTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panTransDetails.setMinimumSize(new java.awt.Dimension(435, 150));
        panTransDetails.setPreferredSize(new java.awt.Dimension(435, 150));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        lblTransDt.setText("Transaction Date");
        lblTransDt.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblTransDt, gridBagConstraints);

        lblValueDt.setText("Value Date");
        lblValueDt.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(tdtValueDt, gridBagConstraints);

        lblAccountNo.setText("Account No.");
        lblAccountNo.setName("lblAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(lblAccountNo, gridBagConstraints);

        lblBatchIDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblBatchIDValue.setText("[xxxxxxxxxx]");
        lblBatchIDValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBatchIDValue.setName("lblBatchIDValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblBatchIDValue, gridBagConstraints);

        lblTransactionIDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTransactionIDValue.setText("[xxxxxxxxxx]");
        lblTransactionIDValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTransactionIDValue.setName("lblTransactionIDValue"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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

        lblBatchID.setText("Batch ID");
        lblBatchID.setName("lblBatchID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblBatchID, gridBagConstraints);

        lblTransactionID.setText("Transaction ID");
        lblTransactionID.setName("lblTransactionID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblTransactionID, gridBagConstraints);

        lblAccountHead.setText("Account Head ID");
        lblAccountHead.setName("lblAccountHead"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblAccountHead, gridBagConstraints);

        panAccountNo.setMinimumSize(new java.awt.Dimension(125, 25));
        panAccountNo.setPreferredSize(new java.awt.Dimension(125, 25));
        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.setName("txtAccountNo"); // NOI18N
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransDetails.add(panAccountNo, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(cboProductType, gridBagConstraints);

        lblCreatedBy.setText("Created By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblCreatedBy, gridBagConstraints);

        lblCreatedByValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCreatedByValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(panAccountHeadValue, gridBagConstraints);

        lblAccountHeadDescValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDescValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
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
        lblCustNameValue.setMinimumSize(new java.awt.Dimension(100, 0));
        lblCustNameValue.setName("lblCustNameValue"); // NOI18N
        lblCustNameValue.setPreferredSize(new java.awt.Dimension(100, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransDetails.add(lblCustNameValue, gridBagConstraints);

        lblAuthBy.setText("Authorize By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(lblAuthBy, gridBagConstraints);

        lblAuthByValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAuthByValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panTransDetails.add(lblAuthByValue, gridBagConstraints);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setMinimumSize(new java.awt.Dimension(100, 0));
        lblHouseName.setName("lblCustNameValue"); // NOI18N
        lblHouseName.setPreferredSize(new java.awt.Dimension(100, 0));
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

        lblBulkTransaction.setText("Bulk Transaction UpLoad");
        lblBulkTransaction.setMaximumSize(new java.awt.Dimension(150, 0));
        lblBulkTransaction.setMinimumSize(new java.awt.Dimension(150, 0));
        lblBulkTransaction.setName("lblTransactionID"); // NOI18N
        lblBulkTransaction.setPreferredSize(new java.awt.Dimension(150, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panTransDetails.add(lblBulkTransaction, gridBagConstraints);

        panBulkTransaction.setMaximumSize(new java.awt.Dimension(50, 0));
        panBulkTransaction.setMinimumSize(new java.awt.Dimension(50, 0));
        panBulkTransaction.setPreferredSize(new java.awt.Dimension(50, 0));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panTransDetails.add(panBulkTransaction, gridBagConstraints);

        tdtTransDtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTransDtValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransDetails.add(tdtTransDtValue, gridBagConstraints);

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
        panInfoPanel.setMinimumSize(new java.awt.Dimension(180, 225));
        panInfoPanel.setName("panInfoPanel"); // NOI18N
        panInfoPanel.setPreferredSize(new java.awt.Dimension(180, 225));
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

        lblTtlCrInstrValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTtlCrInstrValue.setText("[xxxxxxxxxx]");
        lblTtlCrInstrValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
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

        lblTtlCrAmountValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTtlCrAmountValue.setText("[xxxxxxxxxx]");
        lblTtlCrAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
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

        lblTtlDrInstrValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTtlDrInstrValue.setText("[xxxxxxxxxx]");
        lblTtlDrInstrValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
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

        lblTtlDrAmountValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTtlDrAmountValue.setText("[xxxxxxxxxx]");
        lblTtlDrAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
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
        tblTransList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransListMouseClicked(evt);
            }
        });
        srpTransDetails.setViewportView(tblTransList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransInfo.add(srpTransDetails, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.setMaximumSize(new java.awt.Dimension(81, 20));
        chkSelectAll.setMinimumSize(new java.awt.Dimension(81, 20));
        chkSelectAll.setPreferredSize(new java.awt.Dimension(81, 20));
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 70, 0, 0);
        panTransInfo.add(chkSelectAll, gridBagConstraints);

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
        viewType = ACCTHDID;
        HashMap viewMap = new HashMap();
        viewMap.put("BALANCE_TYPE", observable.getTransType());
        viewMap.put("AC_HD_ID", txtAccountHeadValue.getText());
        viewMap.put("BRANCH_GL", "BRANCH_GL");  //Added By Suresh 04-02-2014 Refered Rajesh Sir(In BRANCH_GL Table Status!="DELETED" only to allow transaction)
        List lst = ClientUtil.executeQuery("getACHeadDetails", viewMap);
        if (lst != null && lst.size() > 0) {
            viewMap = (HashMap) lst.get(0);
            fillData(viewMap);
        } else {
            ClientUtil.noDataAlert();
            txtAccountHeadValue.setText("");
            lblAccountHeadDescValue.setText("");
            return;
        }
    }//GEN-LAST:event_txtAccountHeadValueActionPerformed

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
        boolean penalDepositFlag = false;
        ArrayList termLoanDetails = new ArrayList();
        HashMap termLoansDetailsMap = new HashMap();
        double penalAmt = CommonUtil.convertObjToDouble(observable.getDepositPenalAmt()).doubleValue();
        if (penalAmt > 0) {
            penalDepositFlag = true;
        } else {
            penalDepositFlag = false;
        }
        termLoanDetails = transDetails.getTblDataArrayList();
        termLoansDetailsMap.put("DATA", termLoanDetails);
        termLoansDetailsMap.put("ACT_NUM", txtAccountNo.getText());
        termLoansDetailsMap.put("ALL_AMOUNT", observable.getALL_LOAN_AMOUNT());
        termLoansDetailsMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
        System.out.println("tableVal" + termLoanDetails);
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
        if (observable.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {  //Added by kannan
            termLoansDetailsMap.put("ISCREDIT", false);
        } else {
            termLoansDetailsMap.put("ISCREDIT", true);
        }
        boolean showDueTable = true;
        if ((prodType.equals("AD") || prodType.equals("TL")) && transType.equals(CommonConstants.CREDIT)) {
            showDueTable = true;
        } else {
            showDueTable = false;
        }
        new ViewLoansTransUI(termLoansDetailsMap, transViewAmount, transType, showDueTable, penalDepositFlag,"","").show();
    }//GEN-LAST:event_btnViewTermLoanDetailsActionPerformed

    private void tdtValueDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtValueDtFocusLost
        // TODO add your handling code here:
        observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
    }//GEN-LAST:event_tdtValueDtFocusLost

    private void txtAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNoFocusLost
        // TODO add your handling code here:
        if (txtAccountNo.getText().length() > 0) {
            if (!txtAccountNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                HashMap hash = new HashMap();
                prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
                String ACCOUNTNO = txtAccountNo.getText();
                if ((!(CommonUtil.convertObjToStr(prodType).length() > 0)) && ACCOUNTNO.length() > 0) {
                    if (observable.checkAcNoWithoutProdType(ACCOUNTNO, false)) {
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
                if (prodType.equals("TD")) {
                    if (ACCOUNTNO.lastIndexOf("_") != -1) {
                        hash.put("ACCOUNTNO", ACCOUNTNO);
                    } else {
                        hash.put("ACCOUNTNO", ACCOUNTNO + "_1");
                    }
                } else {
                    hash.put("ACCOUNTNO", ACCOUNTNO);
                }
                viewType = ACT_NUM;
                observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));
                fillData(hash);
                if (txtAccountNo.getText().length() > 0) {
                    if (lblCustNameValue.getText().equals("")) {
                        ClientUtil.showAlertWindow("Invalid Account No.");
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
        }
    }//GEN-LAST:event_txtAccountNoFocusLost
    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", curr_dt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        btnCheck();
        callView(VIEW);
        this.populateUIData(ClientConstants.ACTIONTYPE_VIEW);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
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
                if (hash != null && hash.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(hash.get("PENAL_WAIVER")).equals("Y") && penalInt > 0) {
                    int result = ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
                    if (result == 0) {
                        observable.setPenalWaiveOff(true);
                    } else {
                        observable.setPenalWaiveOff(false);
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
            }
        }
        if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
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
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    recurringMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if (lst != null && lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        double finalAmount = 0.0;
                        if (penalAmt > 0) {
                            String[] obj = {"Penalty with Installmets", "Installmets Only."};
                            int option = COptionPane.showOptionDialog(null, ("Select The Desired Option"), ("Receiving Penal with Installmets..."),
                                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                            if (option == 0) {
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                                if (finalAmount != 0) {
                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    txtAmount.setText("");
                                } else {
                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                    observable.setDepositPenalFlag(true);  // added by Rajesh
                                }
                            } else {
                                finalAmount = amount % depAmount;
                                if (finalAmount != 0) {
                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
                                            + "Deposit Amount is : " + depAmount);
                                    txtAmount.setText("");
                                }
                                observable.setDepositPenalAmt(String.valueOf(0.0));
                                observable.setDepositPenalMonth(String.valueOf(0.0));
                                observable.setDepositPenalFlag(false);  // added by Rajesh
                            }
                        } else {
                            finalAmount = amount % depAmount;
                            observable.setDepositPenalAmt(String.valueOf(0.0));
                            observable.setDepositPenalMonth(String.valueOf(0.0));
                            observable.setDepositPenalFlag(false);  // added by Rajesh
                            if (finalAmount != 0) {
                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
                                        + "Deposit Amount is : " + depAmount);
                                txtAmount.setText("");
                            }
                        }
                    }
                    recurringMap = null;
                }
                prodMap = null;
                lst = null;
            }
        } else if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.DEBIT)) {
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
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
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    dailyDepMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                    if (lst.size() > 0) {
                        dailyDepMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                        if (depAmount < amount) {
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
            if (observable.getTransType().equals(CommonConstants.DEBIT)) {
                HashMap acmap = new HashMap();
                HashMap hmap = observable.getALL_LOAN_AMOUNT();
                System.out.println("hmap###" + hmap);
                acmap.put("ACT_NUM", txtAccountNo.getText());
                acmap.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
                List lst = ClientUtil.executeQuery("getIntPrincSubsidy", acmap);
                if (lst != null && lst.size() > 0) {
                    acmap = (HashMap) lst.get(0);
                    String subsidyAvailable = "";
                    String intprincSubsidy = "";
                    double initialNoInst = CommonUtil.convertObjToDouble(acmap.get("INITIAL_NO_OF_INST"));
                    acmap.put("ACT_NUM", txtAccountNo.getText());
                    lst = ClientUtil.executeQuery("getFacilityDEtails", acmap);
                    if (lst != null && lst.size() > 0) {
                        acmap = (HashMap) lst.get(0);
                        subsidyAvailable = CommonUtil.convertObjToStr(acmap.get("SUBSIDY_ALLOWED"));
                        intprincSubsidy = CommonUtil.convertObjToStr(acmap.get("INT_PRINC_SUBSIDY"));
                        double availableBal = CommonUtil.convertObjToDouble(acmap.get("AVAILABLE_BALANCE")).doubleValue();
                        double subsidyAmt = CommonUtil.convertObjToDouble(acmap.get("SUBSIDY_AMT")).doubleValue();
                        double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (subsidyAvailable.equals("Y") && intprincSubsidy.equals("N")) {
                            acmap.put("ACT_NUM", txtAccountNo.getText());
                            lst = ClientUtil.executeQuery("getPaidPrinciple", acmap);
                            if (lst != null && lst.size() > 0) {
                                acmap = (HashMap) lst.get(0);
                                if (acmap.get("PRINCIPLE") != null || acmap.get("INTEREST") != null || acmap.get("PENAL") != null) {
                                    double paidPrincipal = CommonUtil.convertObjToDouble(acmap.get("PRINCIPLE")).doubleValue();
                                    acmap.put("ACT_NUM", txtAccountNo.getText());
                                    lst = ClientUtil.executeQuery("getInstAmount", acmap);
                                    if (lst != null && lst.size() > 0) {
                                        acmap = (HashMap) lst.get(0);
                                        double instAmount = CommonUtil.convertObjToDouble(acmap.get("PRINCIPAL_AMT"));
                                        instAmount = instAmount * initialNoInst;
                                        System.out.println("instAmount###" + instAmount);
                                        System.out.println("paidPrincipal###" + paidPrincipal);
                                        if (paidPrincipal < instAmount) {
                                            ClientUtil.displayAlert("Disbursment not allowed");
                                            txtAmount.setText("");
                                            return;
                                        }
                                    }
                                } else {
                                    if (availableBal > 0.0 && subsidyAmt > 0.0) {
                                        if (initialNoInst > 0.0) {
                                            availableBal = availableBal - subsidyAmt;
                                            if (enteredAmt > availableBal && initialNoInst > 0.0) {
                                                ClientUtil.displayAlert("Disbursment not allowed");
                                                txtAmount.setText("");
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (observable.getTransType().equals(CommonConstants.CREDIT)) {
                HashMap acmap = new HashMap();
                HashMap hmap = observable.getALL_LOAN_AMOUNT();
                System.out.println("hmap###" + hmap);
                acmap.put("ACT_NUM", txtAccountNo.getText());
                acmap.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
                List lst = ClientUtil.executeQuery("getIntPrincSubsidy", acmap);
                if (lst != null && lst.size() > 0) {
                    acmap = (HashMap) lst.get(0);
                    String subsidyAvailable = CommonUtil.convertObjToStr(acmap.get("SUBSIDY_ALLOWED"));
                    String intprincSubsidy = CommonUtil.convertObjToStr(acmap.get("INT_PRINC_SUBSIDY"));
                    String earlyRepayment = CommonUtil.convertObjToStr(acmap.get("EARLY_REPAYMENT"));
                    double initialNoInst = CommonUtil.convertObjToDouble(acmap.get("INITIAL_NO_OF_INST"));
                    acmap.put("ACT_NUM", txtAccountNo.getText());
                    lst = ClientUtil.executeQuery("getFacilityDEtails", acmap);
                    if (lst != null && lst.size() > 0) {
                        acmap = (HashMap) lst.get(0);
                        subsidyAvailable = CommonUtil.convertObjToStr(acmap.get("SUBSIDY_ALLOWED"));
                        intprincSubsidy = CommonUtil.convertObjToStr(acmap.get("INT_PRINC_SUBSIDY"));
                        if (subsidyAvailable.equals("Y") && intprincSubsidy.equals("N") && earlyRepayment.equals("N")) {
                            double prasentinterest = CommonUtil.convertObjToDouble(hmap.get("CURR_MONTH_INT"));
                            double curPrincipaldue = CommonUtil.convertObjToDouble(hmap.get("CURR_MONTH_PRINCEPLE"));
                            double penalInt = CommonUtil.convertObjToDouble(hmap.get("LOAN_CLOSING_PENAL_INT"));
                            prasentinterest = prasentinterest + curPrincipaldue + penalInt;
                            double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            System.out.println("prasentinterest$$$" + prasentinterest);
                            System.out.println("amountt$$$" + amount);
                            if (amount > prasentinterest) {
                                ClientUtil.displayAlert("Repayment not alloewed");
                                txtAmount.setText("");
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (prodType.equals("TL")) {
            moreThanLoanAmountAlert();
        }

        if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0) {

            ClientUtil.displayAlert("amount should not be zero or empty");
            return;
        }
    }//GEN-LAST:event_txtAmountFocusLost

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
               // tdtInstrumentDate.setDateValue(tdtTransDtValue.getDateValue());
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
                tdtInstrumentDate.setEnabled(false);

            } else if (instrumentType.equals("WITHDRAW_SLIP")) {
             //   tdtInstrumentDate.setDateValue(tdtTransDtValue.getDateValue());
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
                tdtInstrumentDate.setEnabled(false);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            } else {
              //  tdtInstrumentDate.setDateValue(tdtTransDtValue.getDateValue());
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
                tdtInstrumentDate.setEnabled(true);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNoActionPerformed
        // TODO add your handling code here:
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
                                                    HashMap acctMap = new HashMap();
                                                    acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                                                    List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                                                    if (head != null && head.size() > 0) {
                                                        acctMap = (HashMap) head.get(0);
                                                        if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
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
                                                            if (observable.isAdviceAccount()) {
                                                                paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransactionForAR");
                                                                paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                                                            } else {
                                                                paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransaction");
                                                                paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                                                            }
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
                                                                paramMap.put("AMOUNT", txtAmount.getText());
                                                                transHOCommonUI = new TransHOCommonUI(paramMap, transType);
                                                            } else if (transHOCommonUI == null && transType.equals("DEBIT")) {
                                                                paramMap.put("DEBIT", "DEBIT");
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
                                                            whereMap.put("BRANCH", TrueTransactMain.BRANCH_ID);
                                                            paramMap.put("AC_HEAD_ID", txtAccountHeadValue.getText());
                                                            whereMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            paramMap.put("TRANS_ID", lblTransactionIDValue.getText());
                                                            if (observable.isAdviceAccount()) {
                                                                paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransactionForAR");
                                                                paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                                                            } else {
                                                                paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransaction");
                                                                paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                                                            }
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
    }

    private void clearProdFields() {
        txtAccountHeadValue.setText("");
        txtAccountNo.setText("");
        lblAccountHeadDescValue.setText("");
        lblCustNameValue.setText("");
    }
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        cboProdTypeActionPerformed();
    }//GEN-LAST:event_cboProductTypeActionPerformed

    //Added By Suresh
    private void cboProdTypeActionPerformed() {
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            clearProdFields();
            //Added BY Suresh
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
                txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
//            populateInstrumentType();
            observable.setMainProductTypeValue(prodType);
            observable.setProductTypeValue(prodType);
            if (prodType.equals("GL")) {
//                if (TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
//                    setProdEnable(false);
//                    cboProductID.setModel(new ComboBoxModel());
//                    btnAccountHead.setEnabled(true);
//                } else {
//                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
//                    observable.setProductTypeValue("");
//                }
            } else if ((prodType.equals("TL") || prodType.equals("TD")) && observable.getTransType().equals(CommonConstants.CREDIT) && termDepositUI != null) {
                COptionPane.showMessageDialog(this, "Not Possible to credit loan accounts...");
                cboProductType.setSelectedIndex(0);
                cboProductID.setModel(new ComboBoxModel());
                return;
            } else {
                setProdEnable(true);
                btnAccountHead.setEnabled(false);
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
        }
         String prodTyp = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if (prodTyp.equals("GL")) {
            cboInstrumentType.setEnabled(false);
        }else{
            cboInstrumentType.setEnabled(true);
        }
    }

    private void setProdEnable(boolean isEnable) {
        cboProductID.setEnabled(isEnable);
        txtAccountNo.setEnabled(isEnable);
        btnAccountNo.setEnabled(isEnable);
        btnAccountHead.setEnabled(isEnable);
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

    private void tblTransListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransListMouseClicked
        // Add your handling code here:
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT || observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) { //added by abi valudate changed but not click bottom save directly click main save we need to give alert 
            editAlert = true;
        }
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
        }
        alreadyExistDeposit = true;
        reconcilebtnDisable = true;
        cboProductActionPerformed();
        cboProdTypeActionPerformed();
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
        tdtValueDt.setEnabled(false);
    }//GEN-LAST:event_tblTransListMouseClicked

    private int getDebitRowNo() {
        int debitRowNum = -1;
        for (int i = 0; i < tblTransList.getRowCount(); i++) {
            String strTransType = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 4));
            if (strTransType.equalsIgnoreCase("DEBIT")) {
                debitRowNum = i;
            }
        }
        return debitRowNum;
    }

    private int getCreditRowNo() {
        int creditRowNum = -1;
        String credit = "false";
        for (int i = 0; i < tblTransList.getRowCount(); i++) {
            String strTransType = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 4));
            System.out.println("credit@@@" + strTransType);
            if (strTransType.equalsIgnoreCase("CREDIT")) {
                credit = "true";
                creditRowNum = i + 1;
            } else if (credit.equals("false")) {
                creditRowNum = 10000;
            }
        }
        return creditRowNum;
    }

    private int getTransIdRowNo() {
        int creditRowNum = -1;
        for (int i = 0; i < tblTransList.getRowCount(); i++) {
            String strTransType = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 2));
            if (strTransType.equalsIgnoreCase("-")) {
                creditRowNum = i;
            }
        }
        return creditRowNum;
    }

    public void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void panTransferEnableDisable(boolean value) {
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
        observable.populatTranferTO(tblTransList.getSelectedRow());
        authorizationCheckMap.put(String.valueOf(tblTransList.getSelectedRow()), String.valueOf(tblTransList.getSelectedRow()));
        observable.getAccountHead();
        HashMap hmap = new HashMap();
        hmap.put("ACHEAD", observable.getAccountHeadId());
        String hoAc = "";
        String adviceReq = "";
        String bankType = CommonConstants.BANK_TYPE;
        String type = "";
        if (tblTransList.getRowCount() > 0) {
            type = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
        }
        List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
        if (list != null && list.size() > 0) {
            hmap = (HashMap) list.get(0);
            hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            adviceReq = CommonUtil.convertObjToStr(hmap.get("ADVICE_REQUICITION"));
        }
        if (bankType.equals("DCCB")) {
            if (hoAc.equals("Y")) {
//                btnOrgOrResp.setVisible(true);
//                observable.setHoAccount(true);
//                if (type.equals("DEBIT")) {
//                    btnOrgOrResp.setText("R");
//                }
//                if (type.equals("CREDIT")) {
//                    btnOrgOrResp.setText("O");
//                }
//                btnOrgOrResp.setEnabled(true);
//                HashMap hashmap1 = new HashMap();
//                HashMap hashmap = new HashMap();
//                hashmap1.put("TRANS_ID", lblBatchIDValue.getText());
//                hashmap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//                hashmap.put("TRANS_ID", lblTransactionIDValue.getText());
//                hashmap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//                if (getOrgOrRespTransType().equals("R")) {
//                    observable.setHoAccountStatus("R");
//                    if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW) || lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
//                        int n = tblTransList.getSelectedRow();
//                        if (getCreditRowNo() <= n) {
//                            n = n - getCreditRowNo();
//                        }
//                        updation = true;
//                        ArrayList lst = (ArrayList) olist.get(0);
//                        int size = olist.size();
//                        System.out.println("size n" + n);
//                        lst = (ArrayList) olist.get(n);
//                        if (lst != null && lst.size() > 0) {
//                            hmap = (HashMap) lst.get(0);
//                            Set keySet;
//                            Object[] objKeySet;
//                            keySet = hmap.keySet();
//                            objKeySet = (Object[]) keySet.toArray();
//                            int key = CommonUtil.convertObjToInt(objKeySet[0]);
//                            hmap = (HashMap) hmap.get(key);
//                            setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("OrgOrRespAdviceDt"))));
//                            setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hmap.get("ADVICE_NO")));
//                            setOrgOrRespAmout(CommonUtil.convertObjToDouble(hmap.get("OrgOrRespAmout")).doubleValue());
//                            setOrgOrRespBranchId(CommonUtil.convertObjToStr(hmap.get("OrgOrRespBranchId")));
//                            setOrgOrRespBranchName(CommonUtil.convertObjToStr(hmap.get("OrgOrRespBranchName")));
//                            setOrgBranch(CommonUtil.convertObjToStr(hmap.get("ORG_BRANCH")));
//                            setOrgBranchName(CommonUtil.convertObjToStr(hmap.get("OrgBranchName")));
//                            setOrgOrRespCategory(CommonUtil.convertObjToStr(hmap.get("OrgOrRespCategory")));
//                            setOrgOrRespDetails(CommonUtil.convertObjToStr(hmap.get("OrgOrRespDetails")));
//                            setOrgOrRespTransType(CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType")));
//                            observable.setHoAccount(true);
//                            observable.setHoAccountCr(getOrgOrRespTransType());
//                        }
//
//                    } else {
//                        List lst = null;
//                        if (olist != null && olist.size() > 0) {
//                            int row = tblTransList.getSelectedRow();
//                            updation = true;
//                            int size = olist.size();
//                            int getRow = size - (row + 1);
//                            if (getTransIdRowNo() != -1 && getTransIdRowNo() < row) {
//                                int n = getTransIdRowNo();
//                                getRow = (size - row) + n;
//                            }
//                            lst = (ArrayList) olist.get(getRow);
//                            if (lst != null && lst.size() > 0) {
//                                HashMap hash = (HashMap) lst.get(0);
//                                Set keySet;
//                                Object[] objKeySet;
//                                keySet = hash.keySet();
//                                objKeySet = (Object[]) keySet.toArray();
//                                int key = CommonUtil.convertObjToInt(objKeySet[0]);
//                                hashmap1 = (HashMap) hash.get(key);
//                                setOldRespAdvNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
//                                setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespAdviceDt"))));
//                                setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
//                                setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("OrgOrRespAmout")).doubleValue());
//                                setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespBranchId")));
//                                setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespBranchName")));
//                                setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
//                                setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("OrgBranchName")));
//                                setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespCategory")));
//                                setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespDetails")));
//                                setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                observable.setHoAccount(true);
//                                observable.setHoAccountStatus(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                observable.setHoAccountCr(getOrgOrRespTransType());
//                            }
//                        } else {
//                            observable.setHoAccount(false);
//                            btnOrgOrResp.setVisible(false);
//                        }
//                    }
//                } else if (getOrgOrRespTransType().equals("O")) {
//                    observable.setHoAccountStatus("O");
//                    if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW) || lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
//                        int n = tblTransList.getSelectedRow();
//                        updation = true;
//                        ArrayList lst = (ArrayList) olist.get(0);
//                        int size = olist.size();
//                        lst = (ArrayList) olist.get(n);
//                        if (lst != null && lst.size() > 0) {
//                            hmap = (HashMap) lst.get(0);
//                            Set keySet;
//                            Object[] objKeySet;
//                            keySet = hmap.keySet();
//                            objKeySet = (Object[]) keySet.toArray();
//                            int key = CommonUtil.convertObjToInt(objKeySet[0]);
//                            hmap = (HashMap) hmap.get(key);
//                            setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("OrgOrRespAdviceDt"))));
//                            setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hmap.get("ADVICE_NO")));
//                            setOrgOrRespAmout(CommonUtil.convertObjToDouble(hmap.get("OrgOrRespAmout")).doubleValue());
//                            setOrgOrRespBranchId(CommonUtil.convertObjToStr(hmap.get("OrgOrRespBranchId")));
//                            setOrgOrRespBranchName(CommonUtil.convertObjToStr(hmap.get("OrgOrRespBranchName")));
//                            setOrgBranch(CommonUtil.convertObjToStr(hmap.get("ORG_BRANCH")));
//                            setOrgBranchName(CommonUtil.convertObjToStr(hmap.get("OrgBranchName")));
//                            setOrgOrRespCategory(CommonUtil.convertObjToStr(hmap.get("OrgOrRespCategory")));
//                            setOrgOrRespDetails(CommonUtil.convertObjToStr(hmap.get("OrgOrRespDetails")));
//                            setOrgOrRespTransType(CommonUtil.convertObjToStr(hmap.get("OrgOrRespTransType")));
//                            observable.setHoAccount(true);
//                            observable.setHoAccountCr(getOrgOrRespTransType());
//                        }
//                    } else {
//                        List lst = null;
//                        if (olist != null && olist.size() > 0) {
//                            int row = tblTransList.getSelectedRow();
//                            updation = true;
//                            int size = olist.size();
//                            int getRow = row - 1;
//                            if (getDebitRowNo() > row) {
//                                getRow++;
//                            }
//                            lst = (ArrayList) olist.get(getRow);
//                            if (lst != null && lst.size() > 0) {
//                                HashMap hash = (HashMap) lst.get(0);
//                                Set keySet;
//                                Object[] objKeySet;
//                                keySet = hash.keySet();
//                                objKeySet = (Object[]) keySet.toArray();
//                                int key = CommonUtil.convertObjToInt(objKeySet[0]);
//                                hashmap1 = (HashMap) hash.get(key);
//                                setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespAdviceDt"))));
//                                setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
//                                setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("OrgOrRespAmout")).doubleValue());
//                                setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespBranchId")));
//                                setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespBranchName")));
//                                setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
//                                setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("OrgBranchName")));
//                                setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespCategory")));
//                                setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespDetails")));
//                                setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                observable.setHoAccount(true);
//                                observable.setHoAccountStatus(CommonUtil.convertObjToStr(hashmap1.get("OrgOrRespTransType")));
//                                observable.setHoAccountCr(getOrgOrRespTransType());
//                            }
//                        } else {
//                            observable.setHoAccount(false);
//                            btnOrgOrResp.setVisible(false);
//                        }
//                    }
//                }
                //Added By Suresh
                observable.setHoAccount(false);
                btnOrgOrResp.setVisible(false);
            } else {
                observable.setHoAccountStatus("");
                btnOrgOrResp.setVisible(false);
            }
        } else {
            observable.setHoAccountStatus("");
        }
        if (adviceReq.equals("Y") && type.equals("DEBIT")) {
            observable.setAdviceAccount(true);
            btnAdvice.setEnabled(true);
            btnAdvice.setVisible(true);
        } else if (adviceReq.equals("Y") && type.equals("CREDIT")) {
            observable.setAdviceAccount(true);
            btnAdvice.setVisible(false);
        } else if (adviceReq.equals("Y")) {
            observable.setAdviceAccount(true);
            btnAdvice.setVisible(false);
        } else {
            observable.setAdviceAccount(false);
            btnAdvice.setVisible(false);
        }
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
                    txtAmount.setEnabled(false);
                } else {
                    txtAmount.setEnabled(true);
                }
            } else {
                txtAmount.setEnabled(true);
            }
        } else {
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
    }

    private void populateTransferDetail() {
        this.update(null, null);
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
        if (!getOrgOrRespAdviceNo().equals("") && observable.getOperation() == (ClientConstants.ACTIONTYPE_EDIT) && prodType.equals("GL")) {
            HashMap hmap = new HashMap();
            hmap.put("STATUS", "DELETED");
            hmap.put("STATUS_BY", TrueTransactMain.USER_ID);
            hmap.put("STATUS_DT", curr_dt.clone());
            hmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
            hmap.put("TRANS_ID", lblBatchIDValue.getText());
            if (observable.deletedList == null) {
                observable.deletedList = new ArrayList();
            }
            observable.deletedList.add(hmap);
            int n = tblTransList.getSelectedRow();
            int row = 0;
            int size = olist.size();
            if (getOrgOrRespTransType().equals("R")) {
                row = size - (n + 1);
            } else {
                row = n - 1;
            }
            List lst = (ArrayList) olist.get(row);
            if (lst != null && lst.size() > 0) {
                HashMap hmap1 = (HashMap) lst.get(0);
                Set keySet;
                Object[] objKeySet;
                keySet = hmap1.keySet();
                objKeySet = (Object[]) keySet.toArray();
                int key = CommonUtil.convertObjToInt(objKeySet[0]);
                olist.remove(row);
                int size1 = olist.size();
                for (int k = 0; k < size1; k++) {
                    ArrayList alist = (ArrayList) olist.get(0);
                    if (alist != null && alist.size() > 0) {
                        HashMap hmap2 = (HashMap) alist.get(0);
                        HashMap ohmap = new HashMap();
                        ArrayList orgList = new ArrayList();
                        keySet = hmap2.keySet();
                        objKeySet = (Object[]) keySet.toArray();
                        key = CommonUtil.convertObjToInt(objKeySet[0]);
                        HashMap HASHMAP = (HashMap) hmap2.get(key);
                        ArrayList orgOrRespList = new ArrayList();
                        ohmap.put(k + 1, HASHMAP);
                        orgList.add(ohmap);
                        olist.remove(0);
                        olist.add(orgList);
                    }
                }

                time = key - 1;
                updation = false;
            }
            updation = false;
        } else if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW) || lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
            if (olist != null && olist.size() > 0) {
                ArrayList lst = (ArrayList) olist.get(0);
                int n = tblTransList.getSelectedRow();
                int size = olist.size();
                if (getCreditRowNo() <= n) {
                    n = n - getCreditRowNo();
                }
                lst = (ArrayList) olist.get(n);
                if (lst != null && lst.size() > 0) {
                    HashMap hmap = (HashMap) lst.get(0);
                    Set keySet;
                    Object[] objKeySet;
                    keySet = hmap.keySet();
                    objKeySet = (Object[]) keySet.toArray();
                    int key = CommonUtil.convertObjToInt(objKeySet[0]);
                    olist.remove(n);
                    int size1 = olist.size();
                    for (int k = 0; k < size1; k++) {
                        ArrayList alist = (ArrayList) olist.get(0);
                        if (alist != null && alist.size() > 0) {
                            HashMap hmap1 = (HashMap) alist.get(0);
                            HashMap ohmap = new HashMap();
                            ArrayList orgList = new ArrayList();
                            keySet = hmap1.keySet();
                            objKeySet = (Object[]) keySet.toArray();
                            key = CommonUtil.convertObjToInt(objKeySet[0]);
                            HashMap HASHMAP = (HashMap) hmap1.get(key);
                            ArrayList orgOrRespList = new ArrayList();
                            ohmap.put(k + 1, HASHMAP);
                            orgList.add(ohmap);
                            olist.remove(0);
                            olist.add(orgList);
                        }
                    }
                    time = key - 1;
                    updation = false;
                }
            }
        }
        observable.deleteTransferData(rowSelected);
        this.updateTable();
        this.updateTransInfo();
        panTransferEnableDisable(false);
        this.btnAddCredit.setEnabled(true);
        this.btnAddDebit.setEnabled(true);

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
        this.tblTransList.revalidate();
    }

    private void updateOrgDetailsResponding() {
        ArrayList lst = (ArrayList) olist.get(0);
        int row = tblTransList.getSelectedRow();
        int getRow = -1;
        int size = olist.size();
        if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW) || lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
            getRow = row;
            if (getCreditRowNo() < getRow) {
                getRow--;
            }
        } else {
            getRow = size - (row + 1);
            if (getTransIdRowNo() != -1 && getTransIdRowNo() < row) {
                int n = getTransIdRowNo();
                getRow = (size - row) + n;
            }
        }
        lst = (ArrayList) olist.get(getRow);
        if (lst != null && lst.size() > 0) {
            HashMap hmap = (HashMap) lst.get(0);
            Set keySet;
            Object[] objKeySet;
            keySet = hmap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            int key = CommonUtil.convertObjToInt(objKeySet[0]);
            int siz = olist.size();
            olist.remove(getRow);
            int size1 = olist.size();
            if (size1 > 0) {
                for (int k = 0; k < size1; k++) {
                    ArrayList alist = (ArrayList) olist.get(0);
                    if (alist != null && alist.size() > 0) {
                        HashMap hmap1 = (HashMap) alist.get(0);
                        HashMap ohmap = new HashMap();
                        ArrayList orgList = new ArrayList();
                        keySet = hmap1.keySet();
                        objKeySet = (Object[]) keySet.toArray();
                        int key1 = CommonUtil.convertObjToInt(objKeySet[0]);
                        if (size1 == 1) {
                            if (key < key1) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                                HashMap HASHMAP = (HashMap) hmap1.get(key1);
                                ohmap.put(key1, HASHMAP);
                                olist.remove(0);
                                orgList.add(ohmap);
                                olist.add(orgList);
                            } else {
                                HashMap HASHMAP = (HashMap) hmap1.get(key1);
                                ohmap.put(key1, HASHMAP);
                                olist.remove(0);
                                orgList.add(ohmap);
                                olist.add(orgList);
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                            }
                        } else {
                            if (key < key1) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                            }
                            HashMap HASHMAP = (HashMap) hmap1.get(key1);
                            ohmap.put(key1, HASHMAP);
                            olist.remove(0);
                            orgList.add(ohmap);
                            olist.add(orgList);
                            if (k == size1 - 1 && siz != olist.size()) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                            }
                        }
                    }
                }
            } else if (siz == 1) {
                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                olist.add(alist1);
                key = size1 + 1;
            }
            time = key - 1;
        }
    }

    private void updateOrgDetails() {
        ArrayList lst = (ArrayList) olist.get(0);
        int row = tblTransList.getSelectedRow();
        int getRow = -1;
        int size = olist.size();
        if (observable.getOperation() == (ClientConstants.ACTIONTYPE_NEW) || lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
            getRow = row;
        } else {
            getRow = row - 1;
            if (getDebitRowNo() > row) {
                getRow++;
            }
        }
        lst = (ArrayList) olist.get(getRow);
        if (lst != null && lst.size() > 0) {
            HashMap hmap = (HashMap) lst.get(0);
            Set keySet;
            Object[] objKeySet;
            keySet = hmap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            int key = CommonUtil.convertObjToInt(objKeySet[0]);
            int siz = olist.size();
            olist.remove(getRow);
            int size1 = olist.size();
            if (size1 > 0) {
                for (int k = 0; k < size1; k++) {
                    ArrayList alist = (ArrayList) olist.get(0);
                    if (alist != null && alist.size() > 0) {
                        HashMap hmap1 = (HashMap) alist.get(0);
                        HashMap ohmap = new HashMap();
                        ArrayList orgList = new ArrayList();
                        keySet = hmap1.keySet();
                        objKeySet = (Object[]) keySet.toArray();
                        int key1 = CommonUtil.convertObjToInt(objKeySet[0]);
                        if (size1 == 1) {
                            if (key < key1) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;

                                HashMap HASHMAP = (HashMap) hmap1.get(key1);
                                ohmap.put(key1, HASHMAP);
                                olist.remove(0);
                                orgList.add(ohmap);
                                olist.add(orgList);
                            } else {
                                HashMap HASHMAP = (HashMap) hmap1.get(key1);
                                ohmap.put(key1, HASHMAP);
                                olist.remove(0);
                                orgList.add(ohmap);
                                olist.add(orgList);
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;

                            }
                        } else {
                            if (key < key1) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                            }
                            HashMap HASHMAP = (HashMap) hmap1.get(key1);
                            ohmap.put(key1, HASHMAP);
                            olist.remove(0);
                            orgList.add(ohmap);
                            olist.add(orgList);
                            if (k == size1 - 1 && siz != olist.size()) {
                                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                                olist.add(alist1);
                                key = size1 + 1;
                            }

                        }
                    }
                }
            } else if (siz == 1) {
                ArrayList alist1 = setUpdateOrgOrRespDetails(key);
                olist.add(alist1);
                key = size1 + 1;
            }
            time = key - 1;
        }
    }

    private void cleareOrgRespDetails() {
        this.setOrgBranch("");
        this.setOrgBranchName("");
        this.setOrgOrRespAdviceDt(null);
        this.setOrgOrRespAdviceNo("");
        this.setOrgOrRespAmout(0.0);
        this.setOrgOrRespBranchId("");
        this.setOrgOrRespBranchName("");
        this.setOrgOrRespCategory("Select");
        this.setOrgOrRespDetails("");
        observable.setHoAccountStatus("");
    }
    private void btnTransSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransSaveActionPerformed
        prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT || observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) { //added by abi valudate changed but not click bottom save directly click main save we need to give alert 
            editAlert = false;
        }
        int preveTime = time;
        if (observable.getTransType().equals("CREDIT") && observable.isHoAccount()) {
            observable.setHoAccountCr("CREDIT");
        }
        if (observable.getTransType().equals("DEBIT") && observable.isHoAccount()) {
            observable.setHoAccountDr("DEBIT");
        }
        if (observable.getTransType() != null && observable.getTransType().equals("CREDIT")) {
            observable.setProdCreditTransfer(prodType);
        }
        if (observable.getTransType() != null && observable.getTransType().equals("DEBIT")) {
            observable.setProdDebitTransfer(prodType);
        }
        String Acno = txtAccountNo.getText();
        HashMap hashmap = new HashMap();
        hashmap.put("ACNO", Acno);
        List lst1 = ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", hashmap);
        if (lst1 != null && lst1.size() > 0) {
            int a = ClientUtil.confirmationAlert("The Account is Death marked, Do you want to continue?");
            int b = 0;
            if (a != b) {
                return;
            }
        }
        if (!prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && txtAccountNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
            ClientUtil.displayAlert("Please Enter Account Number...!!!");
            return;
        }
        //Added By SUresh R
        if (prodType.equals("AD") && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW
                && observable.getTransType().equals(CommonConstants.DEBIT)) {
            java.util.Date expiryDt = DateUtil.getDateMMDDYYYY(transDetails.getExpiryDate());
            if (CommonUtil.convertObjToDouble(transDetails.getAvBalance()) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                ClientUtil.displayAlert("Entered Amount Exceeds The Available Balance");
                return;
            } else if (DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(observable.getCurDate()), expiryDt) < 0) {
                ClientUtil.displayAlert("Account has expired, Please Renew...!!!");
                return;
            }
        }
        if (observable.getHoAccountStatus().equals("O")) {
            if (getOrgOrRespBranchId().equals("") || getOrgOrRespDetails().equals("")) {
                ClientUtil.displayAlert("Visit Orginating Details");
                return;
            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT && !isVisitedStatus()) {
                ClientUtil.displayAlert("Please visit Orginating details and press OK");
                return;
            } else {
                if (updation == true) {
                    updateOrgDetails();
                } else {
                    int size = olist.size();
                    ArrayList alist = setOrgOrRespDetails();
                    olist.add(alist);
                    arrangeOrder(size);
                }
                this.setOrgBranch("");
                this.setOrgBranchName("");
                this.setOrgOrRespAdviceDt(null);
                this.setOrgOrRespAdviceNo("");
                this.setOrgOrRespAmout(0.0);
                this.setOrgOrRespBranchId("");
                this.setOrgOrRespBranchName("");
                this.setOrgOrRespCategory("Select");
                this.setOrgOrRespDetails("");
                time = time + 1;
            }
        } else if (observable.getHoAccountStatus().equals("R")) {
            if (!getOrgOrRespCategory().equals("FT") && (getOrgBranch().equals("") || getOrgOrRespAdviceNo().equals("") || getOrgOrRespAdviceDt().equals(""))) {
                if (responding == false) {
                    ClientUtil.displayAlert("Enter Responding Details");
                    return;
                }
            } else if (getOrgOrRespCategory().equals("FT") && getOrgBranch().equals("")) {
                if (responding == false) {
                    ClientUtil.displayAlert("Enter Responding Details");
                    return;
                }
            } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT && !isVisitedStatus()) {
                ClientUtil.displayAlert("Please visit responding details and press OK");
                return;
            } else {
                setOrgOrRespTransType("R");
                if (updation == true) {
                    updateOrgDetailsResponding();
                } else {
                    int size = olist.size();
                    ArrayList alist = setOrgOrRespDetails();
                    olist.add(alist);
                    arrangeOrder(size);
                }
                this.setOrgBranch("");
                this.setOrgBranchName("");
                this.setOrgOrRespAdviceDt(null);
                this.setOrgOrRespAdviceNo("");
                this.setOrgOrRespAmout(0.0);
                this.setOrgOrRespBranchId("");
                this.setOrgOrRespBranchName("");
                this.setOrgOrRespCategory("Select");
                this.setOrgOrRespDetails("");
                this.setOldRespAdvNo("");
                time = time + 1;
                responding = true;
            }
        }
        HashMap hmap1 = new HashMap();
        hmap1.put("ACHEAD", txtAccountHeadValue.getText());
        List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap1);
        if (list != null && list.size() > 0) {
            hmap1 = (HashMap) list.get(0);
            String adviceReq = CommonUtil.convertObjToStr(hmap1.get("ADVICE_REQUICITION"));
            if (adviceReq.equals("Y") && observable.getTransType().equals("DEBIT")) {
                if (getAdviceBranchId().equals("") || getAdviceDetails().equals("")) {
                    ClientUtil.displayAlert("Enter Advice Requicition Details");
                    return;
                }
            }
        }
        if (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD") || prodType.equals("AD")) {
            checkDocumentDetails();
            if (moreThanLoanAmountAlert()) {
                return;
            }

            if (observable.getTransType().equals(CommonConstants.DEBIT)) {
                if (prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AAD")) {
                    observable.setCheckDebitTermLoan(true);
                }
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
        }
        if (!prodType.equals("GL")) {
            String acct_num = CommonUtil.convertObjToStr(txtAccountNo.getText());
            observable.checkAcNoWithoutProdType(acct_num, true);
        }
        if ((prodType.equals("TL") || prodType.equals("ATL") || prodType.equals("AD")) && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            if (observable.getMultiple_ALL_LOAN_AMOUNT() == null) {
                observable.setMultiple_ALL_LOAN_AMOUNT(new HashMap());
            }
            observable.getMultiple_ALL_LOAN_AMOUNT().put(txtAccountNo.getText(), transDetails.getTermLoanCloseCharge());
        }

        if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            String transType = "";
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if (head != null && head.size() > 0) {
                acctMap = (HashMap) head.get(0);
                if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
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
        if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
            HashMap recurrMap = new HashMap();
            String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            recurrMap.put("PROD_ID", prodId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
            if (lst != null && lst.size() > 0) {
                HashMap recurringMap = (HashMap) lst.get(0);
                if (!recurringMap.get("BEHAVES_LIKE").equals("RECURRING") && !recurringMap.get("BEHAVES_LIKE").equals("DAILY")) {
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
                lienMap.put("LIEN_DT", curr_dt.clone());
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
        if (prodType.equals("TD")) {
            observable.setRenewDepAmt(transDetails.getDepositAmt());
        }

        MandatoryCheck objMandatory = new MandatoryCheck();
        BackDatedTransactionHashMap objMap = new BackDatedTransactionHashMap();
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
            if (!observable.getProductTypeValue().equals("GL")) {
                mandatoryMap.put("cboInstrumentType", new Boolean(true));
                mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
            }
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
                hmap.put("CUR_DATE", curr_dt.clone());
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
            if(tdtTransDtValue.getDateValue().length() == 0){
                ClientUtil.displayAlert("Transaction date should not be empty");
                return;
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
            hmap.put("CUR_DATE", curr_dt.clone());
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
        if (!this._intTransferNew) {
            int rowSelected = this.tblTransList.getSelectedRow();
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
        setVisitedStatus(false);
        alreadyExistDeposit = false;
        btnOrgOrResp.setVisible(false);
        btnAdvice.setVisible(false);
        updation = false;
        TrueTransactMain.populateBranches();
        //TrueTransactMain.getCboBranchList().setSelectedItem(ProxyParameters.BRANCH_ID);
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        
    }//GEN-LAST:event_btnTransSaveActionPerformed

    private HashMap checkLoanDebit(String prodType) {
        HashMap loanDebitType = new HashMap();
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        if (prodType.equals("TL") || prodType.equals("ATL") && observable.getTransType().equals("DEBIT") || (prodType.equals("AD") && observable.getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER")) || (prodType.equals("AAD") && observable.getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))) {
            String[] debitType = {"Debit Interest", "DebitPrinciple", "Debit_Penal_Int", "Other_Charges"};
            String var = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
            // System.out.println("var@#####"+var);
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
        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
        if (observable.getTotalDrInstruments() > 0) {
            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
                ClientUtil.displayAlert("More than one Debit not allowed on Interbranch Transactions...");
                return;
            }
        }
        if (tblTransList.getRowCount() >= 2) {
            ClientUtil.displayAlert("Multiple Debit/Credit Transactions not Allowed !!!");
            return;
        }
        panTransferEnableDisable(true);
        ClientUtil.enableDisable(this, true);
        this.btnAccountNo.setEnabled(true);
        this.btnAccountHead.setEnabled(true);
        this.btnAddCredit.setEnabled(false);
        this.btnTransDelete.setEnabled(false);
        this._intTransferNew = true;
        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        observable.setTransType(CommonConstants.DEBIT);
        btnOrgOrResp.setText("R");
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
        //tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        btnReconsile.setEnabled(false);
        setEnableDisableBulkTransaction(false);
        //Added BY Suresh
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        }
        if (observable.isAdviceAccount()) {
            btnAdvice.setVisible(true);
        }
        tdtValueDt.setEnabled(false);
        if (tblTransList.getRowCount() > 0) {
            tdtTransDtValue.setEnabled(false);
        } else {
            tdtTransDtValue.setEnabled(true);
        }
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
        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
        if (observable.getTotalCrInstruments() > 0) {
            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
                ClientUtil.displayAlert("More than one Credit not allowed on Interbranch Transactions...");
                return;
            }
        }
        panTransferEnableDisable(true);
        ClientUtil.enableDisable(this, true);
        this.btnAccountNo.setEnabled(true);
        this.btnAccountHead.setEnabled(true);
        this.btnAddDebit.setEnabled(false);
        this.btnTransDelete.setEnabled(false);
        this._intTransferNew = true;
        observable.setTransStatus(CommonConstants.STATUS_CREATED);
        observable.setTransType(CommonConstants.CREDIT);
        btnOrgOrResp.setText("O");
        loanAsanWhen = true;
        recurringCredit = true;
        fieldsEnableDisable(false);
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
        //tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        btnReconsile.setEnabled(false);
        setEnableDisableBulkTransaction(false);
        //Added BY Suresh
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        }
        btnAdvice.setVisible(false);
        observable.setHoAccountStatus("");
        btnOrgOrResp.setVisible(false);
        this.setOrgBranch("");
        this.setOrgBranchName("");
        this.setOrgOrRespAdviceDt(null);
        this.setOrgOrRespAdviceNo("");
        this.setOrgOrRespAmout(0.0);
        this.setOrgOrRespBranchId("");
        this.setOrgOrRespBranchName("");
        this.setOrgOrRespCategory("Select");
        this.setOrgOrRespDetails("");
        updation = false;
        newTransactionInEdit = true;
        tdtValueDt.setEnabled(false);
        if (tblTransList.getRowCount() > 0) {
            tdtTransDtValue.setEnabled(false);
        } else {
            tdtTransDtValue.setEnabled(true);
        }
    }//GEN-LAST:event_btnAddCreditActionPerformed
    private void fieldsEnableDisable(boolean yesno) {
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
    }

    private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
        // Add your handling code here:
        final BackDatedTransactionRB rb = new BackDatedTransactionRB();
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
//    private void showUpPop() {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension frameSize = actionPopUp.getSize();
//        if (frameSize.height > screenSize.height) {
//            frameSize.height = screenSize.height;
//        }
//        if (frameSize.width > screenSize.width) {
//            frameSize.width = screenSize.width;
//        }
//        actionPopUp.setLocation((screenSize.width - frameSize.width) / 2,
//                (screenSize.height - frameSize.height) / 2 - 20);
//        actionPopUp.show();
//    }
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
//        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_REJECT, this,"BACK_DATED_TRANSACTION");
//        showUpPop();
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
                cboProductType.setSelectedItem("Operative Account");
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
                    cboProductID.setEnabled(false);
                    txtAccountHeadValue.setText(CommonUtil.convertObjToStr(operativeMap.get("AC_HD_ID")));
                    txtAccountHeadValue.setEnabled(false);
                    lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(operativeMap.get("PROD_DESC")));
                    btnAccountNo.setEnabled(false);
                    txtAmount.setText(String.valueOf(amount));
                    txtParticulars.setText(depositNo + "_1");
                    btnTransSave.setEnabled(true);
                }
            }
        }
    }

    private void btnRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectionActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectionActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
//        if (authorizationCheckMap.size() == tblTransList.getRowCount()) {
//            this.mitExceptionsActionPerformed(evt);
//        } else {
//            COptionPane.showMessageDialog(this, resourceBundle.getString("NOT_VERIFIED"));
//        }
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnRejection.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == this.AUTHORIZE && !this.btnAdd.isEnabled()) {
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
            if (observable.isAdviceAccount()) {
                ArrayList alist = setAdviceDetails();
                observable.setAdviceList(alist);
            }
            observable.authorize(remark);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                }
            }
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
            return;

        } else {
            viewType = AUTHORIZE;
            observable.setStatus();
            lblStatusValue.setText(observable.getLblStatus());
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getBackDatedTransactionAuthList");
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());

        }
    }

    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // Add your handling code here:
        observable.setTransDate(tdtTransDtValue.getDateValue()); // added by nithya on 20-04-2016 for 4252
        observable.setValueDate(DateUtil.getDateMMDDYYYY(tdtValueDt.getDateValue()));        
        boolean checkInterBranch = observable.checkForInterBranchTransExistance();
        if (observable.getTransType().equals(CommonConstants.DEBIT) && observable.getTotalDrInstruments() > 0) {
            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
                ClientUtil.displayAlert("More than one Debit not allowed on Interbranch Transactions...");
                return;
            }
        }
        if (observable.getTransType().equals(CommonConstants.CREDIT) && observable.getTotalCrInstruments() > 0) {
            if (checkInterBranch || !TrueTransactMain.BRANCH_ID.equals(TrueTransactMain.selBranch)) {
                ClientUtil.displayAlert("More than one Credit not allowed on Interbranch Transactions...");
                return;
            }
        }
        if (this.cboProductID.getSelectedItem() != null && ((String) this.cboProductID.getSelectedItem()).length() > 0) {
            callView(ACT_NUM);
        }
    }//GEN-LAST:event_btnAccountNoActionPerformed

    private void mitExceptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionsActionPerformed
        // Add your handling code here:
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
         * BackDatedTransactionUI reference to ActionPopupUI
         */
//        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_EXCEPTION, this,"BACK_DATED_TRANSACTION");//.show();
//        showUpPop();
        authorizeStatus();
    }//GEN-LAST:event_mitExceptionsActionPerformed
    public ArrayList setAdviceDetails() {
        HashMap hmap = new HashMap();
        ArrayList adviceList = new ArrayList();
        hmap.put("AdviceDt", getAdviceDt());
        hmap.put("ADVICE_REQ_NO", getAdviceReqNo());
        hmap.put("adviceAmout", getAdviceAmount());
        hmap.put("adviceBranchId", getAdviceBranchId());
        hmap.put("adviceBranchName", getAdviceBranchName());
        hmap.put("adviceCategory", getAdviceCategory());
        hmap.put("adviceDetails", getAdviceDetails());
        hmap.put("adviceTransType", getAdviceTransType());
        adviceList.add(hmap);
        return adviceList;
    }

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // Add your handling code here:
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
            if (observable.isAdviceAccount()) {
                ArrayList alist = setAdviceDetails();
                observable.setAdviceList(alist);
            }
            observable.authorize(remark);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                }
            }
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
         * do not require to update the UI, so we are not passing the
         * BackDatedTransactionUI reference to ActionPopupUI
         */
//        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_AUTHORIZE, this,"BACK_DATED_TRANSACTION");
//        showUpPop();
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
                        hmap.put("USER_ID", TrueTransactMain.USER_ID);
                        hmap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                        hmap.put("TRANS_DT", curr_dt.clone());
                        hmap.put("BATCH_ID", batchIdForEdit);
                        List aclist = ClientUtil.executeQuery("getAuthorizationNotAllowedAcList", hmap); //Added By Suresh
                        if (aclist != null && aclist.size() > 0) {
                            ClientUtil.displayAlert("Authorization not allowed in own account");
                            btnCancelActionPerformed(null);
                            return;
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
        if (evt != null && (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)) {
            if (tblTransList.getRowCount() > 1) {
                int c = ClientUtil.confirmationAlert("Are You Sure You want To Continue with CANCEL???");
                int d = 0;
                if (c != d) {
                    return;
                }
            }
        }
        deletescreenLock();
        editAlert = false;
        authorizationCheckMap.clear();
        chkSelectAll.setVisible(false);
        chkSelectAll.setSelected(false);
        observable.resetOBFields();
        this.updateTable();
        updateTransInfo();
        transDetails.setTransDetails(null, null, null);
        transDetails.setProductId("");
        transDetails.setSourceFrame(null);
        observable.setEmiNoInstallment(0);
        ClientUtil.enableDisable(this, false);
        this.enableDisableButtons(false);
        clearProdFields();
        setModified(false);
        asAndWhenMap = null;
        observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setTable();
        tblTransList.setModel(observable.getTbmTransfer());
        viewType = 0;
        setupMenuToolBarPanel();
        if (termDepositUI != null) {
            btnCloseActionPerformed(null);
            if (afterSaveCancel == false) {
                termDepositUI.disableSaveButton();
            }
        }
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        btnReconsile.setEnabled(false);
        btnView.setEnabled(true);
        setBatchIdForEdit("");
        observable.setLinkMap(null);
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
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
        }
        lblHouseName.setText("");
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
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
        setAdviceDt(null);
        setAdviceReqNo("");
        setAdviceAmount(0.0);
        setAdviceBranchId("");
        setAdviceBranchName("");
        setAdviceCategory("");
        setAdviceDetails("");
        setAdviceTransType("");
        updation = false;
        time = 0;
        responding = false;
        olist = new ArrayList();
        txtAmount.setToolTipText("");
        tdtTransDtValue.setDateValue("");
        tdtValueDt.setDateValue("");
        observable.resetTransDate();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        //Added By Suresh
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            cboProductActionPerformed();
        }
    }//GEN-LAST:event_cboProductIDActionPerformed

    //Added By Suresh
    private void cboProductActionPerformed() {
        if (this.cboProductID.getSelectedIndex() > 0) {
            clearProdFields();
            //Added BY Suresh
            String productType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && !productType.equals("GL")) {
                txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
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
                        status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", where).get(0)).getSRemarks();
                        where = null;
                        where = (HashMap) lst.get(0);
                        String st = CommonUtil.convertObjToStr(where.get("S_REMARKS"));
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
                        status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", where).get(0)).getSRemarks();
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
                String prodId = "";
                prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", prodId.toString());
                List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                if (behavesLiklst != null && behavesLiklst.size() > 0) {
                    prodMap = (HashMap) behavesLiklst.get(0);
                    depBehavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                    depPartialWithDrawalAllowed = CommonUtil.convertObjToStr(prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED"));
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
                if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                    // Do nothing
                } else {
                    COptionPane.showMessageDialog(this, "Deposit Withdraw can be done using Deposit Closer.");
                    cboProductType.setSelectedIndex(0);
                    cboProductID.setModel(new ComboBoxModel());
                    txtAccountHeadValue.setText("");
                    return;
                }
            }
            if (prodType.equals("TD") && observable.getTransType().equals(CommonConstants.CREDIT)) {
                String prodId = "";
                prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", prodId.toString());
                List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                // System.out.println("######## BEHAVES :"+behavesLiklst);
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
                    observable.getAccountHead();
                    this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
                }
            }
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    public ArrayList setUpdateOrgOrRespDetails(int key) {
        int n = olist.size();
        ArrayList orgOrRespList = new ArrayList();
        HashMap HASHMAP = new HashMap();
        HashMap ohmap = new HashMap();
        ohmap.put("OrgOrRespAdviceDt", getProperFormatDate(getOrgOrRespAdviceDt()));
        ohmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
        ohmap.put("OrgOrRespAmout", CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
        ohmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
        ohmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
        ohmap.put("OrgBranchName", getOrgBranchName());
        ohmap.put("ORG_BRANCH", getOrgBranch());
        ohmap.put("OrgOrRespCategory", getOrgOrRespCategory());
        ohmap.put("OrgOrRespDetails", getOrgOrRespDetails());
        ohmap.put("OrgOrRespTransType", getOrgOrRespTransType());
        if (lblTransactionIDValue.getText().equals("") || lblTransactionIDValue.getText().equals("-")) {
            ohmap.put("ROW_STATUS", "NEW");
        } else {
            ohmap.put("ROW_STATUS", "EDIT");
            ohmap.put("OLD_ADV_NO", getOldRespAdvNo());
        }
        HASHMAP.put(key, ohmap);

        orgOrRespList.add(HASHMAP);
        // System.out.println("orgOrRespList"+orgOrRespList);

        return orgOrRespList;
    }

    private void arrangeOrder(int size) {
        Set keySet;
        Object[] objKeySet;
        int n = 2;
        for (int k = 0; k < size; k++) {

            ArrayList alist = (ArrayList) olist.get(0);
            if (alist != null && alist.size() > 0) {
                HashMap hmap1 = (HashMap) alist.get(0);
                HashMap ohmap = new HashMap();
                ArrayList orgList = new ArrayList();
                keySet = hmap1.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // System.out.println("n@@@@" + objKeySet[0]);
                int key = CommonUtil.convertObjToInt(objKeySet[0]);
                HashMap HASHMAP = (HashMap) hmap1.get(key);
                ArrayList orgOrRespList = new ArrayList();
                ohmap.put(n, HASHMAP);
                orgList.add(ohmap);
                olist.remove(0);
                olist.add(orgList);
                // System.out.println("olist" + olist);
                n++;

            }
        }
        System.out.println("olist@@@" + olist);
    }

    public Date getProperFormatDate(Object obj) {
        Date currDt = null;
        currDt = (Date) curr_dt.clone();
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) currDt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    public ArrayList setOrgOrRespDetails() {
        int n = olist.size();
        ArrayList orgOrRespList = new ArrayList();
        HashMap HASHMAP = new HashMap();
        HashMap ohmap = new HashMap();
        ohmap.put("OrgOrRespAdviceDt", getProperFormatDate(getOrgOrRespAdviceDt()));
        ohmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
        ohmap.put("OrgOrRespAmout", CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
        ohmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
        ohmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
        ohmap.put("OrgBranchName", getOrgBranchName());
        ohmap.put("ORG_BRANCH", getOrgBranch());
        ohmap.put("OrgOrRespCategory", getOrgOrRespCategory());
        ohmap.put("OrgOrRespDetails", getOrgOrRespDetails());
        ohmap.put("OrgOrRespTransType", getOrgOrRespTransType());
        //if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
        ohmap.put("ROW_STATUS", "NEW");
        //}
        HASHMAP.put(1, ohmap);

        orgOrRespList.add(HASHMAP);
        return orgOrRespList;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        if ((observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT || observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) && editAlert) { //added by abi valudate changed but not click bottom save directly click main save we need to give alert 
            ClientUtil.displayAlert("Please Save(Bottom) The Debit/Credit Transaction");
            return;
        }
        observable.setTransDate(tdtTransDtValue.getDateValue());
        String errorMessage = "";
        if (tblTransList.getRowCount() > 0) {
            if (CommonConstants.BANK_TYPE.equals("DCCB") && olist != null && olist.size() > 0) {
                int n = 0;
                int s = 0;
                String type = CommonUtil.convertObjToStr(tblTransList.getValueAt(0, 4));
                if (type.equals("DEBIT")) {
                    n = 1;
                    s = 0;
                } else {
                    s = 1;
                    n = 0;
                }
                for (int i = 1; i < tblTransList.getRowCount(); i++) {
                    String type1 = CommonUtil.convertObjToStr(tblTransList.getValueAt(i, 4));
                    if (type.equals("DEBIT")) {

                        if (!type.equals(type1)) {
                            s = s + 1;
                            if (s != 0 && s > 1 && n > 1) {
                                ClientUtil.displayAlert("Multiple Debits/Credits not allowed");
                                return;
                            }
                        } else if (type.equals(type1)) {
                            n = n + 1;
                            if (n != 0 && n > 1 && s > 1) {
                                ClientUtil.displayAlert("Multiple Debits/Credits not allowed");
                                return;
                            }
                        }

                    } else if (type.equals("CREDIT")) {

                        if (!type.equals(type1)) {
                            n = n + 1;
                            if (s != 0 && s > 1 && n > 1) {
                                ClientUtil.displayAlert("Multiple Debits/Credits not allowed");
                                return;
                            }
                        } else if (type.equals(type1)) {
                            s = s + 1;
                            if (n != 0 && n > 1 && s > 1) {
                                ClientUtil.displayAlert("Multiple Debits/Credits not allowed");
                                return;
                            }
                        }

                    }
                }
            }
            observable.getTransDetails();
            if (observable.getTotalCrAmount() != observable.getTotalDrAmount()) {
                errorMessage += resourceBundle.getString("SAVE_BATCH_TALLY");
            }
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
            if (flag == true) {
                String dep = termDepositUI.getTransSomeAmt();
                if (dep != null && dep.equals("DEP_INTEREST_AMT")) {
                    observable.setDepInterestAmt("DEP_INTEREST_AMT");
                }
            }

            if (!getAdviceBranchId().equals("") && !getAdviceDetails().equals("")) {
                observable.setAdviceAccount(true);
                ArrayList alist = setAdviceDetails();
                observable.setAdviceList(alist);
            }
            if (observable.isAdviceAccount() && transCommonUI != null) {
                HashMap map = transCommonUI.getReturnMap();
                HashMap listMap = new HashMap();
                ArrayList updateList = new ArrayList();
                updateList = (ArrayList) map.get("TOTAL_LIST");
                for (int k = 0; k < updateList.size(); k++) {
                    String status = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(0));
                    if (status.equals("true")) {
                        String value = CommonUtil.convertObjToStr(((ArrayList) updateList.get(k)).get(11));
                        listMap.put("ADVICE_REQ_NO", value);
                        List lst = ClientUtil.executeQuery("getbarnchIdForAR", listMap);
                        if (lst != null && lst.size() > 0) {
                            listMap = (HashMap) lst.get(0);
                            String branch = CommonUtil.convertObjToStr(listMap.get("BRANCH_ID"));
                            String id = getOrgBranch();
                            if (!branch.equals(id)) {
                                ClientUtil.displayAlert("Select Advice requisition sent to  same branch only");
                                return;
                            }
                        }
                    }
                }

            }
            if (getAdviceBranchId().equals("") && getAdviceDetails().equals("")) {
                observable.setAdviceAccount(false);
            }
            if (errorMessage.length() > 0) {
                observable.setResult(ClientConstants.ACTIONTYPE_FAILED);
                COptionPane.showMessageDialog(this, errorMessage);
            } else {
                if (flagDeposit == true) {
                    double intAmt = observable.getTotalCrAmount();
                    txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                }
                if (flag == true) {
                    double intAmt = observable.getTotalCrAmount();
                    txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                }
                if (loanDebitType != null && loanDebitType.size() > 0) {
                    observable.setDebitLoanType(loanDebitType);
                }
                observable.checkForValueDate();
                observable.doAction(null);
            }
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                afterSaveCancel = true;
                btnCancelActionPerformed(null);
                if (termDepositUI != null) {
                    btnCloseActionPerformed(null);
                    if (!termDepositUI.getRenewalTransMap().equals("")) {
                        HashMap renewalMap = termDepositUI.getRenewalTransMap();
                        if (renewalMap.containsKey("INT_AMT")) {
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.changePeriod();
                        } else if (renewalMap.containsKey("INTEREST_AMT_TRANSFER")) {
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.addingSomeAmt();
                            termDepositUI.setRenewalTransMap(new HashMap());
                            termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_TRANSFER", "");
                        }
                        if (renewalMap.containsKey("DEPOSIT_AMT_TRANSFER")) {
                            termDepositUI.transactionCalling();
                        }
                    }
                }
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
            }
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
        } else {
            COptionPane.showMessageDialog(this, resourceBundle.getString("NO_ROWS"));
        }
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setProductTypeModel(new ComboBoxModel(key, value));
        transCommonUI = null;
    }//GEN-LAST:event_btnSaveActionPerformed
                                        private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                                            // TODO add your handling code here:
                                            observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
                                            observable.setStatus();
                                            lblStatusValue.setText(observable.getLblStatus());
                                            btnCheck();
                                            callView(DELETE);
                                            this.populateUIData(ClientConstants.ACTIONTYPE_DELETE);
                                            btnView.setEnabled(false);
                                            txtAmount.setEditable(false);
                                            txtAccountNo.setEditable(false);
                                            setEnableDisableBulkTransaction(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    public void populateUIData(int operation) {
        if (this.batchIdForEdit != null) {
            HashMap hashmap1 = new HashMap();
            hashmap1.put("TRANS_ID", batchIdForEdit);
            hashmap1.put("BRANCH_ID", transactionInitBranForEdit);
//            List lst = ClientUtil.executeQuery("getOrgRespDetailsForOrg", hashmap1);
//            List rLst = ClientUtil.executeQuery("getOrgRespDetailsForResp", hashmap1);
//            List lst1 = ClientUtil.executeQuery("getAdviceDetailsForAuth", hashmap1);
//            if (lst != null && lst.size() > 0) {
//                for (int i = 0; i < lst.size(); i++) {
//                    HashMap ohmap = new HashMap();
//                    hashmap1 = (HashMap) lst.get(i);
//                    HashMap HASHMAP = new HashMap();
//                    ArrayList alist = new ArrayList();
//                    setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
//                    setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
//                    setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
//                    setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH")));
//                    setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH_NAME")));
//                    setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
//                    setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH_NAME")));
//                    setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
//                    setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
//                    setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
//                    btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
//                    ohmap.put("OrgOrRespAdviceDt", getProperFormatDate(getOrgOrRespAdviceDt()));
//                    ohmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
//                    ohmap.put("OrgOrRespAmout", getOrgOrRespAmout());
//                    ohmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
//                    ohmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
//                    ohmap.put("OrgBranchName", getOrgBranchName());
//                    ohmap.put("ORG_BRANCH", getOrgBranch());
//                    ohmap.put("OrgOrRespCategory", getOrgOrRespCategory());
//                    ohmap.put("OrgOrRespDetails", getOrgOrRespDetails());
//                    ohmap.put("OrgOrRespTransType", getOrgOrRespTransType());
//                    ohmap.put("OLD_ADV_NO", getOrgOrRespAdviceNo());
//                    HASHMAP.put(i + 1, ohmap);
//                    alist.add(HASHMAP);
//                    olist.add(alist);
//                    observable.setHoAccount(true);
//                }
//            }
//
//            if (rLst != null && rLst.size() > 0) {
//                for (int i = 0; i < rLst.size(); i++) {
//                    HashMap ohmap = new HashMap();
//                    hashmap1 = (HashMap) rLst.get(i);
//                    HashMap HASHMAP = new HashMap();
//                    ArrayList alist = new ArrayList();
//                    setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
//                    setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
//                    setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
//                    setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH")));
//                    setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH_NAME")));
//                    setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
//                    setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH_NAME")));
//                    setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
//                    setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
//                    setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
//                    btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
//                    ohmap.put("OrgOrRespAdviceDt", getProperFormatDate(getOrgOrRespAdviceDt()));
//                    ohmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
//                    ohmap.put("OrgOrRespAmout", getOrgOrRespAmout());
//                    ohmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
//                    ohmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
//                    ohmap.put("OrgBranchName", getOrgBranchName());
//                    ohmap.put("ORG_BRANCH", getOrgBranch());
//                    ohmap.put("OrgOrRespCategory", getOrgOrRespCategory());
//                    ohmap.put("OrgOrRespDetails", getOrgOrRespDetails());
//                    ohmap.put("OrgOrRespTransType", getOrgOrRespTransType());
//                    ohmap.put("OLD_ADV_NO", getOrgOrRespAdviceNo());
//                    ohmap.put("ROW_STATUS", "EDIT");
//                    HASHMAP.put(i + 1, ohmap);
//                    alist.add(HASHMAP);
//                    olist.add(alist);
//                    observable.setHoAccount(true);
//                }
//            }
//            if (lst1 != null && lst1.size() > 0) {
//                hashmap1 = (HashMap) lst1.get(0);
//                setAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
//                setAdviceReqNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_REQ_NO")));
//                setAdviceAmount(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
//                setAdviceBranchId(CommonUtil.convertObjToStr(hashmap1.get("BRANCH_ID")));
//                setAdviceBranchName(CommonUtil.convertObjToStr(hashmap1.get("BRANCH_NAME")));
//                setAdviceCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
//                setAdviceDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
//                setAdviceTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
//                observable.setAdviceAccount(true);
//            }
            observable.setOperation(operation);

            observable.setBatchId(batchIdForEdit);
            observable.setTransDate(CommonUtil.convertObjToStr(transactionDateForEdit));
            observable.setInitBran(CommonUtil.convertObjToStr(transactionInitBranForEdit));
            rowForEdit = observable.getData(transactionIdForEdit);
            this._intTransferNew = false;
            // update the account information also
            if (transactionIdForEdit != null) {
                updateAccountInfo();
                cboProductIDActionPerformed(null);
            }
            updateTransInfo();
            setupMenuToolBarPanel();

            // Enable all the screen
            if (operation == ClientConstants.ACTIONTYPE_EDIT) {
                setModified(true);
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
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap reportTransIdMap = new HashMap();
                        reportTransIdMap.put("TransId", batchIdForEdit);
                        reportTransIdMap.put("TransDt", curr_dt.clone());
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
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        olist = new ArrayList();
        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.fillInstrumentType();
        this.cboInstrumentType.setModel(observable.getInstrumentTypeModel());
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        setModified(true);
        setupMenuToolBarPanel();
//        tdtTransDtValue.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
//        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
        this.lblCreatedByValue.setText(ProxyParameters.USER_ID);
        this.btnAddDebit.setEnabled(true);
        setEnableDisableBulkTransaction(false);
        rdoBulkTransaction_Yes.setSelected(false);
        rdoBulkTransaction_No.setSelected(true);
        chkSelectAll.setVisible(false);
        btnAddCredit.setEnabled(true);
        tdtValueDt.setEnabled(false);
    }//GEN-LAST:event_btnAddActionPerformed

    private void setEnableDisableBulkTransaction(boolean flag) {
        ClientUtil.enableDisable(panBulkTransaction, flag);
        panBulkTransaction.setVisible(flag);
        lblBulkTransaction.setVisible(flag);

    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
//        actionPopUp = new ActionPopupUI(this, true, ClientConstants.ACTIONTYPE_EDIT, this,"BACK_DATED_TRANSACTION");//.show();
//        showUpPop();
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
            whereListMap.put("FILTERED_LIST", "");
            if (observable.getTransType() != null) {
                String types = "TL";
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
            whereListMap.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        } else if (currField == VIEW || currField == DELETE) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getBackDatedTransactionEnquiryList");
        }
        new ViewAll(this, viewMap).show();
    }

    /*
     * this method will be called by the ViewAll class, to which we are passing
     * the UI class reference this will also set the account number and
     * operation value for OB
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("################### fillData hash : " + hash);         
        observable.setReconcile("");
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            authorizeStatus();
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_VIEW || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
            setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            setTransactionDateForEdit(DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hash.get("TRANS_DT"))));
            setTransactionInitBranForEdit(CommonUtil.convertObjToStr(hash.get("INIT_BRANCH")));
            setTransactionIdForEdit(null);
            setEnableDisableBulkTransaction(false);
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            setBatchIdForEdit(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
            setTransactionDateForEdit(DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hash.get("TRANS_DT"))));
            setTransactionInitBranForEdit(CommonUtil.convertObjToStr(hash.get("INIT_BRANCH")));
            setTransactionIdForEdit(null);
            authorizeStatus();
            setEnableDisableBulkTransaction(false);
        }
        cleareOrgRespDetails();
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        observable.setProductId((String) ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        if (viewType == ACT_NUM) {
            String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
            if (prodType.equals("TD") && viewType == ACT_NUM && ACCOUNTNO.lastIndexOf("_") == -1) {
                ACCOUNTNO = ACCOUNTNO + "_1";
            }
            observable.setAccountNo(ACCOUNTNO);
            lblHouseName.setText(CommonUtil.convertObjToStr(hash.get("HOUSENAME")));
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO, true)) {
                setSelectedBranchID(observable.getSelectedBranchID());
            }
            observable.setAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            observable.setTransDepositAmt(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            observable.setReconcile("N");
            morethanOneDeposit = false;
            if (observable.isAccountNoExists(ACCOUNTNO, false)) {
                displayAlert("This Account no. already present in this batch...");
                morethanOneDeposit = true;
            }
            updateAccountInfo();
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
                }
                if (prodType.equals("OA")) {
                    boolean OverDue = false;
                    HashMap operativeMap = new HashMap();
                    operativeMap.put("ACT_NUM", observable.getAccountNo());
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
                    System.out.println("selectedBranchDt : " + selectedBranchDt + " currentDate : " + currentDate);
                    if (selectedBranchDt == null) {
                        ClientUtil.displayAlert("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                        txtAccountNo.setText("");
                        return;
                    } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) != 0) {
                        ClientUtil.displayAlert("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed");
                        txtAccountNo.setText("");
                        return;
                    } else {
                        System.out.println("Continue for interbranch trasactions ...");
                    }
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
        } else if (viewType == ACCTHDID) {
            cboProductID.setSelectedItem("");
            this.txtAccountNo.setText("");
            btnAccountNo.setEnabled(false);
            String acHdId = hash.get("A/C HEAD").toString();
            String customerAllow = "";
            String hoAc = "";
            String adviceReq = "";
            String bankType = CommonConstants.BANK_TYPE;
            this.txtAccountHeadValue.setText((String) hash.get("A/C HEAD"));
            observable.setBalanceType((String) hash.get("BALANCETYPE"));
            HashMap hmap = new HashMap();
            hmap.put("ACHEAD", acHdId);
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
                adviceReq = CommonUtil.convertObjToStr(hmap.get("ADVICE_REQUICITION"));
            }
            if (bankType.equals("DCCB")) {
                if (hoAc.equals("Y")) {
                    btnOrgOrResp.setVisible(true);
                    observable.setHoAccount(true);
                    btnOrgOrResp.setEnabled(true);
                    observable.setHoAccountStatus(btnOrgOrResp.getText());

                } else {
                    observable.setHoAccount(false);
                    btnOrgOrResp.setVisible(false);
                    updation = false;
                }
            }
            if (adviceReq.equals("Y")) {
                observable.setAdviceAccount(true);
                btnAdvice.setEnabled(true);
                if (observable.getTransType().equals("DEBIT")) {
                    btnAdvice.setVisible(true);
                }
            } else {
                observable.setAdviceAccount(false);
                btnAdvice.setVisible(false);
            }
//            if (customerAllow.equals("Y")) {
//                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
//                hmap.put("ACC_NUM", AccNo);
//                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
//                if (chkList != null && chkList.size() > 0) {
//                    hmap = (HashMap) chkList.get(0);
//                    lblCustNameValue.setText(CommonUtil.convertObjToStr(hmap.get("NAME")));
//                    txtAccountNo.setText(AccNo);
//                    observable.setClosedAccNo(AccNo);
//                } else {
//                    ClientUtil.displayAlert("Invalid Account number");
//                    txtAccountHeadValue.setText("");
//                    return;
//                }
//            }

            this.lblAccountHeadDescValue.setText((String) hash.get("A/C HEAD DESCRIPTION"));
            observable.setAccountHeadId((String) hash.get("A/C HEAD"));
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
                }
            }
        }
    }

    /*
     * use this method to get the customer informatio and update the name and
     * address for the customer in the Account Details screen
     */
    public void updateAccountInfo() {
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
            if (!prodType.equals("")) {
                System.out.println("txtaccountno##" + txtAccountNo.getText());
            }
            String actNum = observable.getAccountNo() != null ? observable.getAccountNo() : txtAccountHeadValue.getText();
            HashMap mapHash = new HashMap();
            long noofInstallment = 0;
            if (observable.getTransType().equals("CREDIT")) {
                mapHash = observable.asAnWhenCustomerComesYesNO(actNum, null);
            }
            if (mapHash != null && mapHash.size() > 0) {
                HashMap transMap = new HashMap();
                if (mapHash != null && mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES").equals("Y") && !prodType.equals("AD")) {
                    if (observable.getTransferTOs() != null && observable.getTransferTOs().size() > 0) {
                        String batch_id = observable.getTermLoanBatch_id();
                        if (batch_id != null && batch_id.length() > 0) {
                            transMap.put("BATCH_ID", batch_id);
                            transMap.put("LINK_BATCH_ID", actNum);
                            transMap.put("TRANS_DT", curr_dt.clone());
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
                }

                String remark = "";
                if (observable.getAuthorizeRemarks() != null && observable.getAuthorizeRemarks().length() > 0 && CommonUtil.convertObjToInt(observable.getAuthorizeRemarks()) > 0) {
                    remark = observable.getAuthorizeRemarks();
                }
                if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") || mapHash.get("INSTALL_TYPE").equals("EYI")
                        || mapHash.get("INSTALL_TYPE").equals("LUMP_SUM"))) && observable.getTransType().equals(CommonConstants.CREDIT) && viewType != this.AUTHORIZE) {
                    if (remark.length() == 0) {
                        remark = COptionPane.showInputDialog(this, "NO OF INSTALLMENT WANT TO PAY");
                    }
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
            }

            if (/*
                     * (prodType.equals("AD") || prodType.equals("TL"))
                     */observable.getLinkMap() != null
                    && observable.getLinkMap().size() > 0 && observable.getTransType().equals(CommonConstants.CREDIT)) {
                if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                    if (asAndWhenMap == null) {
                        asAndWhenMap = new HashMap();
                    }
                    asAndWhenMap = interestCalculationTLAD(actNum, noofInstallment);
                    if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                        asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                        transDetails.setAsAndWhenMap(asAndWhenMap);
                        if (asAndWhenMap.containsKey("NO_OF_INSTALLMENT") && CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT")) > 0) {
                            noofInstallment = CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"));
                        }
                        observable.setEmiNoInstallment(noofInstallment);
                    }
                } else {
                }
            }

            if (prodType.equals("GL") && (observable.getLinkMap() == null || observable.getLinkMap().size() == 0)) {
                if(observable.getBranchId()!=null && !observable.getBranchId().equals("") && observable.getBranchId().length()>0){
                    transDetails.setTransDetails(prodType, observable.getBranchId(), observable.getAccountHeadId());
                }else{
                    transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, observable.getAccountHeadId());
                }
                transDetails.setProductId(CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));//Added by kannan
                transDetails.setSourceFrame(this);
            } else if (prodType.equals("GL") && observable.getLinkMap().size() > 0) {
                transDetails.setTransDetails("TL", ProxyParameters.BRANCH_ID, actNum, observable.getTransType());   
                observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                System.out.println("observable.setALL_LOAN_AMOUNT" + observable.getALL_LOAN_AMOUNT());
                transDetails.setSourceFrame(this);
                btnViewTermLoanDetails.setVisible(true);
                btnViewTermLoanDetails.setEnabled(true);
                if (CommonUtil.convertObjToStr(observable.getTransType()).equals(CommonConstants.CREDIT)) {
                    btnViewTermLoanDetails.setVisible(true);
                    btnViewTermLoanDetails.setEnabled(true);
                }
            } else {
                if (prodType.equals("TL") || prodType.equals("AD")) {
                    transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, actNum, observable.getTransType());
                    transDetails.setProductId(CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));//Added by kannan
                    transDetails.setSourceFrame(this);
                    btnViewTermLoanDetails.setVisible(true);
                    btnViewTermLoanDetails.setEnabled(true);

                } else {
                    if(observable.getBranchId()!=null && !observable.getBranchId().equals("") && observable.getBranchId().length()>0){
                        transDetails.setTransDetails(prodType, observable.getBranchId(), actNum);
                    }else{
                        transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, actNum);
                    }
                    transDetails.setProductId(CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));//Added by kannan
                    transDetails.setSourceFrame(this);
                }
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                if ((prodType.equals("TL") || prodType.equals("AD")) && observable.getTransType().equals(CommonConstants.CREDIT)) {
                    observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                    observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT", new Long(noofInstallment));
                    if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") || mapHash.get("INSTALL_TYPE").equals("EYI")
                            || mapHash.get("INSTALL_TYPE").equals("LUMP_SUM")))) {
                        double totalEMIAmt = setEMIAmount();
                        txtAmount.setText(String.valueOf(totalEMIAmt));
                        observable.setTransferAmount(String.valueOf(totalEMIAmt));
                        txtAmount.setEnabled(false);
                    } else {
                        txtAmount.setEnabled(true);
                        observable.setTransferAmount("");
                    }
                }
            }
            String multiDisburse = transDetails.getIsMultiDisburse();
            if (!prodType.equals("") && prodType.equals("GL") && observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getAccountHeadId());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        String transType = "";
                        if (reconcilebtnDisable == true) {
                            transType = CommonUtil.convertObjToStr(tblTransList.getValueAt(tblTransList.getSelectedRow(), 4));
                        }
                        transType = observable.getTransType();
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
                if (actualAmt >= totalLoanAmt) {
                    int message = ClientUtil.confirmationAlert("Entered Transaction Amount is equal to/more than the Outstanding Loan Amount," + "\n" + "Do You Want to Close the A/c?");
                    if (message == 0) {
                        HashMap hash = new HashMap();
                        CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setSelectedBranchID(getSelectedBranchID());
                        TrueTransactMain.showScreen(frm);
                        hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                        hash.put("ACCOUNT NUMBER", txtAccountNo.getText());
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
                map.put("PROD_ID", prod_id);
                map.put("TRANS_DT", curr_dt.clone());
                map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
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
                    map.put("BRANCH_ID", getSelectedBranchID());
                    map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    if (noOfInstallment > 0) {
                        map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                    }
                    map.putAll(hash);
                    map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                    map.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
                    hash = observable.loanInterestCalculationAsAndWhen(map);
                    if (hash == null) {
                        hash = new HashMap();
                    }
                    hash.putAll(map);
                    hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

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
        this.cboProductID.setModel(observable.getProductTypeModel());
        this.cboInstrumentType.setModel(observable.getInstrumentTypeModel());
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
//        tdtTransDtValue.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
//        tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getValueDate()));
        lblBatchIDValue.setText(observable.getBatchId());
        lblTransactionIDValue.setText(observable.getTransId());
        txtAccountHeadValue.setText(observable.getAccountHeadId());
        lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
        txtAccountNo.setText(observable.getAccountNo());
        String pType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
        if (pType.length() > 0) {
            if (observable.getAccountNo() != null && observable.getAccountNo().length() > 0) {
                lblCustNameValue.setText(setAccountName(observable.getAccountNo()));
            } else {
                lblAccountHeadDescValue.setText(setAccountName(observable.getAccountHeadId()));
            }
        }
        txtInstrumentNo1.setText(observable.getInstrumentNo1());
        txtInstrumentNo2.setText(observable.getInstrumentNo2());
        tdtInstrumentDate.setDateValue(observable.getInstrumentDate());
        txtAmount.setText(observable.getTransferAmount());
        txtParticulars.setText(observable.getParticulars());
        tdtTransDtValue.setDateValue(observable.getTransDate());
        tdtValueDt.setDateValue(observable.getTransDate());
        txtNarration.setText(observable.getNarration());
        lblTtlCrInstrValue.setText(String.valueOf(observable.getTotalCrInstruments()));
        lblTtlCrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalCrAmount())));
        lblTtlDrInstrValue.setText(String.valueOf(observable.getTotalDrInstruments()));
        lblTtlDrAmountValue.setText(CommonUtil.convertObjToStr(new Double(observable.getTotalDrAmount())));
        lblStatusValue.setText(observable.getLblStatus());
        lblAuthByValue.setText(observable.getAuthBy());
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_NEW) {
            this.lblCreatedByValue.setText(observable.getInitTransId());
        }
        this.updateTable();
        addRadioButton();
    }

    public void btnDepositClose() {
        flag = true;
        afterSaveCancel = true;
        viewType = this.AUTHORIZE;
        btnAdd.setEnabled(false);
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")) {
            btnAuthorizeActionPerformed(null);
        }
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")) {
            btnRejectionActionPerformed(null);
        }
        btnCloseActionPerformed(null);
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
        if (intMap != null) {
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
                //tdtValueDt.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
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
                intMap.put("TRANS_ID", observable.getDepositTransId());
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
        if (intMap != null) {
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
                //tdtValueDt.setDateValue(DateUtil.getStringDate(observable.getCurDate()));
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
            }
        }
        return intMap;
    }

    public String setAccountName(String AccountNo) {
        final HashMap accountNameMap = new HashMap();
        HashMap resultMap = new HashMap();
        List resultList = new ArrayList();
        String prodType = (String) ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected();
        String pID = !prodType.equals("GL") ? ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString() : "";
        try {
            accountNameMap.put("ACC_NUM", AccountNo);
            if (prodType.equals("GL") && AccountNo.length() > 0) {
                resultList = ClientUtil.executeQuery("getAccountNumberNameTL", accountNameMap);//
            } else {
                resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, accountNameMap);
            }
            if (resultList != null) {
                if (resultList.size() > 0) {
                    if (!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodType, accountNameMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                dataMap = (HashMap) lst.get(0);
                                if (dataMap.get("PROD_ID").equals(pID)) {
                                    resultMap = (HashMap) resultList.get(0);
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
         * reference of BackDatedTransactionUI, to set the transaction id for
         * the editing purpose
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
        String tranId = lblBatchIDValue.getText();
        int mode = observable.getOperation();
        double transViewAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        if (transViewAmount > 0) {
            if (btnOrgOrResp.getText().equals("R")) {
                if (txtParticulars.getText().length() > 0) {
                    transType = CommonConstants.DEBIT;
                    this.setOrgOrRespDetails(txtParticulars.getText());
                    com.see.truetransact.ui.TrueTransactMain.showScreen(viewRespUI);
                    btnOrgOrResp.setEnabled(false);
                } else {
                    ClientUtil.showMessageWindow("Enter Particulars !!!");
                    return;
                }
            } else if (btnOrgOrResp.getText().equals("O")) {
                if (txtParticulars.getText().length() > 0) {
                    transType = CommonConstants.CREDIT;
                    transType = txtParticulars.getText();
// suresh.r                    vieworgOrRespUI = new ViewOrgOrRespUI(transViewAmount, transType, this,mode,lblTransactionIDValue.getText());
                    com.see.truetransact.ui.TrueTransactMain.showScreen(vieworgOrRespUI);
                    btnOrgOrResp.setEnabled(false);
                } else {
                    ClientUtil.showMessageWindow("Enter Particulars !!!");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("Enter Transaction Amount !!!");
            return;
        }
    }//GEN-LAST:event_btnOrgOrRespActionPerformed

    private void btnAdviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdviceActionPerformed
        // TODO add your handling code here:
//        String transType = "";
//        double transViewAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
//        if (transViewAmount > 0) {
//            transType = CommonConstants.DEBIT;
//            adviceRequicitionUI = new AdviceRequicitionUI(transViewAmount, transType, this);
//            com.see.truetransact.ui.TrueTransactMain.showScreen(adviceRequicitionUI);
//            btnAdvice.setEnabled(false);
//        } else {
//            ClientUtil.displayAlert("Enter Transaction Amount");
//            return;
//        }
    }//GEN-LAST:event_btnAdviceActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:    //Added By Suresh
        if (tblTransList.getRowCount() > 0) {
            authorizationCheckMap = new HashMap();
            if (chkSelectAll.isSelected()) {
                observable.setVerificationStatus("Yes");
                for (int i = 0; i < tblTransList.getRowCount(); i++) {  // setting authorizationCheckMap Size = tblTransList Row Count
                    authorizationCheckMap.put(i, i);
                }
            } else {
                authorizationCheckMap.clear();
                observable.setVerificationStatus("No");
            }
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void tdtTransDtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTransDtValueFocusLost
        // TODO add your handling code here:
        if (tdtTransDtValue.getDateValue().length() > 0) {
//            int maxTransDays = 0;
//             HashMap cbmsMap = new HashMap();
//            List list;
//            cbmsMap.put("CBMS_KEY", "BACK_DATED_TRANS_DAYS");
//            list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
//            if (list != null && list.size() > 0) {
//                cbmsMap = (HashMap) list.get(0);
//                maxTransDays = CommonUtil.convertObjToInt(cbmsMap.get("CBMS_VALUE"));
//            }
//            Date maxDate = DateUtil.addDays(curr_dt, -maxTransDays);
//            System.out.println("maxDate :: " + maxDate);
//            
//            Date enteredDt = DateUtil.getDateMMDDYYYY(tdtTransDtValue.getDateValue());
            boolean transAllowed = true;
            Date maxBackTransDt = null;
            HashMap cbmsMap = new HashMap();
            List list;
            list = ClientUtil.executeQuery("getMaxBackDatedTransDt", null);
            if (list != null && list.size() > 0) {
                cbmsMap = (HashMap) list.get(0);
                maxBackTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cbmsMap.get("BACK_DATED_TRANS_DT")));
            }
            Date enteredDt = DateUtil.getDateMMDDYYYY(tdtTransDtValue.getDateValue());
            if ((maxBackTransDt != null && (DateUtil.dateDiff(maxBackTransDt, enteredDt)) < 0) || (DateUtil.dateDiff(enteredDt, curr_dt) <= 0)) {
                transAllowed = false;
            }
            
            
            HashMap holidayMap = new HashMap();
            boolean holiday = false;
            holidayMap.put("NEXT_DATE", getProperFormatDate(enteredDt));
            holidayMap.put("BRANCH_CODE", TrueTransactMain.selBranch);

            List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
            List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
            boolean isHoliday = Holiday.size() > 0 ? true : false;
            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
            if (isHoliday || isWeekOff) {
                holiday = true;
            }
            
               
           // if((DateUtil.dateDiff(enteredDt, curr_dt) <= 0) || (DateUtil.dateDiff(maxDate, enteredDt))< 0){
            if(!transAllowed){
                ClientUtil.displayAlert("Back dated transaction allowed only\n From :" + DateUtil.getStringDate(maxBackTransDt) +"  To :" + DateUtil.getStringDate(DateUtil.addDays(curr_dt, -1)));
                tdtTransDtValue.setDateValue(null);
            }else if(holiday){
                ClientUtil.displayAlert("Selected Date is a holiday !!!");
                tdtTransDtValue.setDateValue(null);
            }else{            
            tdtValueDt.setDateValue(tdtTransDtValue.getDateValue());
            tdtValueDt.setEnabled(false);
            //tdtInstrumentDate.setDateValue(tdtTransDtValue.getDateValue());
            tdtInstrumentDate.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
            tdtInstrumentDate.setEnabled(false);
            cboInstrumentType.setSelectedItem("Voucher");
            cboInstrumentType.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tdtTransDtValueFocusLost

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
        tdtTransDtValue.setName("tdtTransDtValue");
        lblValueDt.setName("lblValueDt");
        tdtValueDt.setName("tdtValueDt");
        lblAccountHead.setName("lblAccountHead");
        txtAccountHeadValue.setName("txtAccountHeadValue");
        lblAccountNo.setName("lblAccountNo");
        lblAmount.setName("lblAmount");
        lblBatchID.setName("lblBatchID");
        lblBatchIDValue.setName("lblBatchIDValue");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentNo.setName("lblInstrumentNo");
        lblInstrumentType.setName("lblInstrumentType");
        lblParticulars.setName("lblParticulars");
        lblNarration.setName("lblNarration");
        lblProductID.setName("lblProductID");
        lblStatus.setName("lblStatus");
        lblStatusValue.setName("lblStatusValue");
        lblTokenNo.setName("lblTokenNo");
        lblTransactionID.setName("lblTransactionID");
        lblTransactionIDValue.setName("lblTransactionIDValue");
        lblTtlCrAmount.setName("lblTtlCrAmount");
        lblTtlCrAmountValue.setName("lblTtlCrAmountValue");
        lblTtlCrInstr.setName("lblTtlCrInstr");
        lblTtlCrInstrValue.setName("lblTtlCrInstrValue");
        lblTtlDrAmount.setName("lblTtlDrAmount");
        lblTtlDrAmountValue.setName("lblTtlDrAmountValue");
        lblTtlDrInstr.setName("lblTtlDrInstr");
        lblTtlDrInstrValue.setName("lblTtlDrInstrValue");
        panAccountNo.setName("panAccountNo");
        panFieldPanel.setName("panFieldPanel");
        panInfoPanel.setName("panInfoPanel");
        panStatus.setName("panStatus");
        panTransDetail.setName("panTransDetail");
        panTransInfo.setName("panTransInfo");
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
        lblSep3.setName("lblSep3");
        rdoBulkTransaction_Yes.setName("rdoBulkTransaction_Yes");
        rdoBulkTransaction_No.setName("rdoBulkTransaction_No");
        panBulkTransaction.setName("panBulkTransaction");
        lblBulkTransaction.setName("lblBulkTransaction");
    }

    private void internationalize() {
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.batchprocess.BackDatedTransaction.BackDatedTransactionRB", ProxyParameters.LANGUAGE);
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTtlDrAmount.setText(resourceBundle.getString("lblTtlDrAmount"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTransDt.setText(resourceBundle.getString("lblTransDt"));
        lblValueDt.setText(resourceBundle.getString("lblValueDt"));
        lblBatchIDValue.setText(resourceBundle.getString("lblBatchIDValue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblSep3.setText(resourceBundle.getString("lblSep3"));
        lblTransactionID.setText(resourceBundle.getString("lblTransactionID"));
        lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        ((TitledBorder) panTransInfo.getBorder()).setTitle(resourceBundle.getString("panTransInfo"));
        lblTtlDrAmountValue.setText(resourceBundle.getString("lblTtlDrAmountValue"));
        lblTtlCrAmountValue.setText(resourceBundle.getString("lblTtlCrAmountValue"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnAccountNo.setText(resourceBundle.getString("btnAccountNo"));
        lblTransactionIDValue.setText(resourceBundle.getString("lblTransactionIDValue"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblTtlDrInstr.setText(resourceBundle.getString("lblTtlDrInstr"));
        lblBatchID.setText(resourceBundle.getString("lblBatchID"));
        ((TitledBorder) panTransDetail.getBorder()).setTitle(resourceBundle.getString("panTransDetail"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        btnReport.setText(resourceBundle.getString("btnReport"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblTtlCrInstrValue.setText(resourceBundle.getString("lblTtlCrInstrValue"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblNarration.setText(resourceBundle.getString("lblNarration"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        lblTtlCrAmount.setText(resourceBundle.getString("lblTtlCrAmount"));
        txtAccountHeadValue.setText(resourceBundle.getString("lblAccountHeadValue"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        btnRejection.setText(resourceBundle.getString("btnRejection"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblTtlDrInstrValue.setText(resourceBundle.getString("lblTtlDrInstrValue"));
        lblStatusValue.setText(resourceBundle.getString("lblStatusValue"));
        lblTtlCrInstr.setText(resourceBundle.getString("lblTtlCrInstr"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
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
//        observable.setTransDate(DateUtil.getStringDate(observable.getCurDate()));
        observable.setTransDate(tdtTransDtValue.getDateValue());
        observable.setParticulars(txtParticulars.getText());
        observable.setRdoBulkTransaction_Yes(rdoBulkTransaction_Yes.isSelected());
        observable.setRdoBulkTransaction_No(rdoBulkTransaction_No.isSelected());
        observable.setNarration(txtNarration.getText());
    }

    public static void main(String[] arg) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            JFrame jf = new JFrame();
            BackDatedTransactionUI gui = new BackDatedTransactionUI();
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
        if (chqBalList != null && chqBalList.size() > 0) {
            minMap = (HashMap) chqBalList.get(0);
            double withoutChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITHOUT_CHQ")).doubleValue();
            double withChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITH_CHQ")).doubleValue();
            String chqBk = CommonUtil.convertObjToStr(minMap.get("CHQ_BOOK")).toUpperCase();
            int chqcount = CommonUtil.convertObjToInt(minMap.get("CNT"));
            if (chqBk.equals("Y") && chqcount > 0) {
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

    public double getAdviceAmount() {
        return adviceAmount;
    }

    public void setAdviceAmount(double adviceAmount) {
        this.adviceAmount = adviceAmount;
    }

    public String getAdviceBranchName() {
        return adviceBranchName;
    }

    public void setAdviceBranchName(String adviceBranchName) {
        this.adviceBranchName = adviceBranchName;
    }

    public String getAdviceCategory() {
        return adviceCategory;
    }

    public void setAdviceCategory(String adviceCategory) {
        this.adviceCategory = adviceCategory;
    }

    public String getAdviceDetails() {
        return adviceDetails;
    }

    public void setAdviceDetails(String adviceDetails) {
        this.adviceDetails = adviceDetails;
    }

    public Date getAdviceDt() {
        return adviceDt;
    }

    public void setAdviceDt(Date adviceDt) {
        this.adviceDt = adviceDt;
    }

    public String getAdviceReqNo() {
        return adviceReqNo;
    }

    public void setAdviceReqNo(String adviceReqNo) {
        this.adviceReqNo = adviceReqNo;
    }

    public String getAdviceTransType() {
        return adviceTransType;
    }

    public void setAdviceTransType(String adviceTransType) {
        this.adviceTransType = adviceTransType;
    }

    public String getAdviceBranchId() {
        return adviceBranchId;
    }

    public void setAdviceBranchId(String adviceBranchId) {
        this.adviceBranchId = adviceBranchId;
    }

    public String getOldRespAdvNo() {
        return oldRespAdvNo;
    }

    public void setOldRespAdvNo(String oldRespAdvNo) {
        this.oldRespAdvNo = oldRespAdvNo;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAddCredit;
    private com.see.truetransact.uicomponent.CButton btnAddDebit;
    private com.see.truetransact.uicomponent.CButton btnAdvice;
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
    private com.see.truetransact.uicomponent.CButtonGroup bulkTransactionGroup;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
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
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSep3;
    private com.see.truetransact.uicomponent.CLabel lblSep4;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatusValue;
    private javax.swing.JLabel lblTBSep1;
    private javax.swing.JLabel lblTBSep2;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTransDt;
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
    private com.see.truetransact.uicomponent.CDateField tdtTransDtValue;
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
