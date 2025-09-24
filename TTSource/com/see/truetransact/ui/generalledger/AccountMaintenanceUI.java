/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountMaintenanceUI.java
 *
 * Created on August 8, 2003, 1:52 PM
 */
package com.see.truetransact.ui.generalledger;

/**
 * @author  Annamalai
 * @author  Bala
 */
import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.CInternalFrame;

import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import org.apache.log4j.Logger;

public class AccountMaintenanceUI extends CInternalFrame implements Observer, UIMandatoryField {
    //    private AccountMaintenanceRB _resourceBundle = new AccountMaintenanceRB();
    java.util.ResourceBundle _resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.AccountMaintenanceRB", ProxyParameters.LANGUAGE);
    private AccountMaintenanceOB _observable;
    private HashMap _mandatoryMap;
    final int AUTHORIZE = 3;
    int viewType = -1;
    boolean isFilled = false;
    private String btnType = "";
    private final String AHD = "Account Head";//String varibale to identify which button is clicked
    private final String RAHD = "Reconcillation Account Head";//String variable to identify which button is clicked
    private final String GST = "GST Settings";// Added by nithya on 12-01-2018 for 7013
    private final static Logger _log = Logger.getLogger(AccountMaintenanceUI.class);
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private int rejectFlag=0;
    TransactionUI transactionUI = new TransactionUI();
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form AccountMaintenance */
    public AccountMaintenanceUI() {
        initComponents();
        initStartup();
    }

    private void initStartup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaximumLength();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCallingCode);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        _observable.resetStatus();
        _observable.resetOBFields();
        lblReconcillationAcHd.setVisible(false);
        panRAcHd.setVisible(false);
        treAcHdTree.setModel(_observable.getAcHdTree());
        treAcHdTree.revalidate();
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView1.setEnabled(!btnView1.isEnabled());
    }

    private void setObservable() {
        //_observable = new AccountMaintenanceOB();

        /* Implementing Singleton pattern */
        _observable = AccountMaintenanceOB.getInstance();
        _observable.addObserver(this);
    }

    private void initComponentData() {
        cboTransactionPosting.setModel(_observable.getCbmTransactionPosting());
        cboPostingMode.setModel(_observable.getCbmPostingMode());
        cboGLBalanceType.setModel(_observable.getCbmGLBalanceType());
        cboContraHead.setModel(_observable.getCbmContraHead());
        cboAccountType.setModel(_observable.getCbmAccountType());
        cboMajorHead.setModel(_observable.getCbmMajorHead());
        cboSubHead.setModel(_observable.getCbmSubHead());
    }

    private void setMaximumLength() {
        txtBalanceInGL.setValidation(new CurrencyValidation(14, 2));
        txtReconcillationAcHd.setMaxLength(16);
        txtAccountHeadDesc.setMaxLength(128);
        txtAccountHead.setMaxLength(16);
        txtAccountHeadOrder.setValidation(new NumericValidation(16, 0));
    }

    public void setMandatoryHashMap() {
        _mandatoryMap = new HashMap();
        _mandatoryMap.put("txtAccountHead", new Boolean(true));
        _mandatoryMap.put("rdoStatus_Implemented", new Boolean(true));
        _mandatoryMap.put("cboContraHead", new Boolean(true));
        _mandatoryMap.put("chkCreditClearing", new Boolean(true));
        _mandatoryMap.put("chkCreditTransfer", new Boolean(true));
        _mandatoryMap.put("chkCreditCash", new Boolean(true));
        _mandatoryMap.put("chkDebitClearing", new Boolean(true));
        _mandatoryMap.put("chkDebitTransfer", new Boolean(true));
        _mandatoryMap.put("chkDebitCash", new Boolean(true));
        _mandatoryMap.put("cboTransactionPosting", new Boolean(true));
        _mandatoryMap.put("cboPostingMode", new Boolean(true));
        _mandatoryMap.put("cboGLBalanceType", new Boolean(true));
        _mandatoryMap.put("chkReconcilliationAllowed", new Boolean(true));
        _mandatoryMap.put("txtBalanceInGL", new Boolean(false));
        _mandatoryMap.put("rdoFloatAccount_Yes", new Boolean(true));
        _mandatoryMap.put("rdoNegValue_Yes", new Boolean(true));
        _mandatoryMap.put("chkHdOfficeAc", new Boolean(true));
        _mandatoryMap.put("txtReconcillationAcHd", new Boolean(true));
        _mandatoryMap.put("chkBalCheck", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return _mandatoryMap;
    }

    private void setBtnRAcHdEnabled(boolean flag) {
        btnRAcHd.setEnabled(flag);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dlgAccountMaintenance = new com.see.truetransact.uicomponent.CDialog();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        cTable1 = new com.see.truetransact.uicomponent.CTable();
        rdoFloatAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNegValue = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAcctHeadMain = new javax.swing.JToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panAccountCreation = new com.see.truetransact.uicomponent.CPanel();
        panAcctCreationDetails = new com.see.truetransact.uicomponent.CPanel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        cboMajorHead = new com.see.truetransact.uicomponent.CComboBox();
        cboSubHead = new com.see.truetransact.uicomponent.CComboBox();
        txtAccountHead = new com.see.truetransact.uicomponent.CTextField();
        lblAccountType1 = new com.see.truetransact.uicomponent.CLabel();
        lblMajorHead1 = new com.see.truetransact.uicomponent.CLabel();
        lblSubHead1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadCode = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtAccountHeadCode = new com.see.truetransact.uicomponent.CTextField();
        txtAccountHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        panPayDayBookDetail = new com.see.truetransact.uicomponent.CPanel();
        rdoPayDayBookDetail_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPayDayBookDetail_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPayDayBookDetail = new com.see.truetransact.uicomponent.CLabel();
        panReceiveDayBookDetail = new com.see.truetransact.uicomponent.CPanel();
        rdoReceiveDayBookDetail_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoReceiveDayBookDetail_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblReceiveDayBookDetail = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadOrder = new com.see.truetransact.uicomponent.CLabel();
        txtAccountHeadOrder = new com.see.truetransact.uicomponent.CTextField();
        panAcHdTree = new com.see.truetransact.uicomponent.CPanel();
        srpAcHdTree = new com.see.truetransact.uicomponent.CScrollPane();
        treAcHdTree = new javax.swing.JTree();
        panHolder = new com.see.truetransact.uicomponent.CPanel();
        panCallingCode = new com.see.truetransact.uicomponent.CPanel();
        lblFloatAccount = new com.see.truetransact.uicomponent.CLabel();
        lblContraHead = new com.see.truetransact.uicomponent.CLabel();
        cboContraHead = new com.see.truetransact.uicomponent.CComboBox();
        panCreditDebit = new com.see.truetransact.uicomponent.CPanel();
        lblClearing = new com.see.truetransact.uicomponent.CLabel();
        lblTransfer = new com.see.truetransact.uicomponent.CLabel();
        lblCash = new com.see.truetransact.uicomponent.CLabel();
        chkDebitClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditCash = new com.see.truetransact.uicomponent.CCheckBox();
        spt1 = new com.see.truetransact.uicomponent.CSeparator();
        spt3 = new com.see.truetransact.uicomponent.CSeparator();
        spt2 = new com.see.truetransact.uicomponent.CSeparator();
        spt4 = new com.see.truetransact.uicomponent.CSeparator();
        spt5 = new com.see.truetransact.uicomponent.CSeparator();
        spt6 = new com.see.truetransact.uicomponent.CSeparator();
        spt7 = new com.see.truetransact.uicomponent.CSeparator();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        cboTransactionPosting = new com.see.truetransact.uicomponent.CComboBox();
        lblTransactionPosting = new com.see.truetransact.uicomponent.CLabel();
        lblPostingMode = new com.see.truetransact.uicomponent.CLabel();
        cboPostingMode = new com.see.truetransact.uicomponent.CComboBox();
        lblReconcilliationAllowed = new com.see.truetransact.uicomponent.CLabel();
        cboGLBalanceType = new com.see.truetransact.uicomponent.CComboBox();
        chkReconcilliationAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        lblBalanceGLType = new com.see.truetransact.uicomponent.CLabel();
        txtBalanceInGL = new com.see.truetransact.uicomponent.CTextField();
        lblBalanceInGL = new com.see.truetransact.uicomponent.CLabel();
        panFloatAccount = new com.see.truetransact.uicomponent.CPanel();
        rdoFloatAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFloatAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblNegValue = new com.see.truetransact.uicomponent.CLabel();
        panNegValue = new com.see.truetransact.uicomponent.CPanel();
        rdoNegValue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNegValue_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHdOfficeAc = new com.see.truetransact.uicomponent.CLabel();
        chkHdOfficeAc = new com.see.truetransact.uicomponent.CCheckBox();
        lblReconcillationAcHd = new com.see.truetransact.uicomponent.CLabel();
        panRAcHd = new com.see.truetransact.uicomponent.CPanel();
        txtReconcillationAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnRAcHd = new com.see.truetransact.uicomponent.CButton();
        lblBalCheck = new com.see.truetransact.uicomponent.CLabel();
        chkBalCheck = new com.see.truetransact.uicomponent.CCheckBox();
        lblAllowCustomerEntry = new com.see.truetransact.uicomponent.CLabel();
        chkAllowCustomerEntry = new com.see.truetransact.uicomponent.CCheckBox();
        lblSerTaxApl = new com.see.truetransact.uicomponent.CLabel();
        cbServiceTax = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        txtserviceTaxId = new com.see.truetransact.uicomponent.CTextField();
        btnGSTSettings = new com.see.truetransact.uicomponent.CButton();
        panAccountType = new com.see.truetransact.uicomponent.CPanel();
        lblAccHeadCode = new javax.swing.JLabel();
        lblAccountHeadCodeDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadDesc = new javax.swing.JLabel();
        lblAccountHeadDescDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblSubHead = new com.see.truetransact.uicomponent.CLabel();
        lblSubHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblMajorHead = new com.see.truetransact.uicomponent.CLabel();
        lblMajorHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        lblAccountTypeDisplay = new com.see.truetransact.uicomponent.CLabel();
        spt8 = new com.see.truetransact.uicomponent.CSeparator();
        lblAccountOpenedOn = new com.see.truetransact.uicomponent.CLabel();
        lblAccountOpenedOnDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblAccountClosedOn = new com.see.truetransact.uicomponent.CLabel();
        lblAccountClosedOnDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblFirstTransactionDate = new com.see.truetransact.uicomponent.CLabel();
        lblFirstTransactionDateDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblLastTransactionDate = new com.see.truetransact.uicomponent.CLabel();
        lblLastTransactionDateDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        dlgAccountMaintenance.getContentPane().setLayout(new java.awt.GridBagLayout());

        cTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Calling Code", "Calling Code Description"
            }
        ));
        cScrollPane1.setViewportView(cTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dlgAccountMaintenance.getContentPane().add(cScrollPane1, gridBagConstraints);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Account Head Maintenance");
        setMinimumSize(new java.awt.Dimension(840, 620));
        setPreferredSize(new java.awt.Dimension(840, 620));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrAcctHeadMain.setMaximumSize(new java.awt.Dimension(422, 29));
        tbrAcctHeadMain.setMinimumSize(new java.awt.Dimension(422, 29));
        tbrAcctHeadMain.setPreferredSize(new java.awt.Dimension(422, 29));

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnView1);

        lblSpace5.setText("     ");
        tbrAcctHeadMain.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAcctHeadMain.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnEdit);

        lblSpace2.setText("     ");
        tbrAcctHeadMain.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnSave);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAcctHeadMain.add(lblSpace12);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnCancel);

        lblSpace3.setText("     ");
        tbrAcctHeadMain.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnAuthorize);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAcctHeadMain.add(lblSpace13);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnException);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAcctHeadMain.add(lblSpace14);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnReject);

        lblSpace4.setText("     ");
        tbrAcctHeadMain.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnPrint);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAcctHeadMain.add(lblSpace15);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAcctHeadMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 741;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        getContentPane().add(tbrAcctHeadMain, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        cTabbedPane1.setMinimumSize(new java.awt.Dimension(205, 335));
        cTabbedPane1.setPreferredSize(new java.awt.Dimension(205, 335));

        cPanel1.setLayout(null);

        panAccountCreation.setPreferredSize(new java.awt.Dimension(730, 324));
        panAccountCreation.setLayout(new java.awt.GridBagLayout());

        panAcctCreationDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAcctCreationDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panAcctCreationDetails.setLayout(new java.awt.GridBagLayout());

        cboAccountType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAccountType.setPopupWidth(200);
        cboAccountType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAccountTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(cboAccountType, gridBagConstraints);

        cboMajorHead.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMajorHead.setPopupWidth(200);
        cboMajorHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMajorHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(cboMajorHead, gridBagConstraints);

        cboSubHead.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubHead.setPopupWidth(200);
        cboSubHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(cboSubHead, gridBagConstraints);

        txtAccountHead.setAllowAll(true);
        txtAccountHead.setMaxLength(16);
        txtAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHead.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(txtAccountHead, gridBagConstraints);

        lblAccountType1.setText("Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblAccountType1, gridBagConstraints);

        lblMajorHead1.setText("Major Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblMajorHead1, gridBagConstraints);

        lblSubHead1.setText("Sub Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblSubHead1, gridBagConstraints);

        lblAccountHeadCode.setText("Account Head Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblAccountHeadCode, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblAccountHead, gridBagConstraints);

        txtAccountHeadCode.setMaxLength(16);
        txtAccountHeadCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(txtAccountHeadCode, gridBagConstraints);

        txtAccountHeadDesc.setAllowAll(true);
        txtAccountHeadDesc.setMaxLength(128);
        txtAccountHeadDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHeadDesc.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(txtAccountHeadDesc, gridBagConstraints);

        lblAccountHeadDesc.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblAccountHeadDesc, gridBagConstraints);

        panPayDayBookDetail.setLayout(new java.awt.GridBagLayout());

        rdoPayDayBookDetail_Yes.setText("Yes");
        rdoPayDayBookDetail_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoPayDayBookDetail_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoPayDayBookDetail_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoPayDayBookDetail_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPayDayBookDetail_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPayDayBookDetail.add(rdoPayDayBookDetail_Yes, gridBagConstraints);

        rdoPayDayBookDetail_No.setText("No");
        rdoPayDayBookDetail_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPayDayBookDetail_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoPayDayBookDetail_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPayDayBookDetail_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPayDayBookDetail.add(rdoPayDayBookDetail_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctCreationDetails.add(panPayDayBookDetail, gridBagConstraints);

        lblPayDayBookDetail.setText("Payment Detail in Day Book");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblPayDayBookDetail, gridBagConstraints);

        panReceiveDayBookDetail.setLayout(new java.awt.GridBagLayout());

        rdoReceiveDayBookDetail_Yes.setText("Yes");
        rdoReceiveDayBookDetail_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoReceiveDayBookDetail_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoReceiveDayBookDetail_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoReceiveDayBookDetail_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReceiveDayBookDetail_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panReceiveDayBookDetail.add(rdoReceiveDayBookDetail_Yes, gridBagConstraints);

        rdoReceiveDayBookDetail_No.setText("No");
        rdoReceiveDayBookDetail_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoReceiveDayBookDetail_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoReceiveDayBookDetail_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReceiveDayBookDetail_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panReceiveDayBookDetail.add(rdoReceiveDayBookDetail_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctCreationDetails.add(panReceiveDayBookDetail, gridBagConstraints);

        lblReceiveDayBookDetail.setText("Receipt Detail in Day Book");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblReceiveDayBookDetail, gridBagConstraints);

        lblAccountHeadOrder.setText("Account Head Order");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(lblAccountHeadOrder, gridBagConstraints);

        txtAccountHeadOrder.setAllowAll(true);
        txtAccountHeadOrder.setMaxLength(16);
        txtAccountHeadOrder.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHeadOrder.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCreationDetails.add(txtAccountHeadOrder, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panAccountCreation.add(panAcctCreationDetails, gridBagConstraints);

        panAcHdTree.setMinimumSize(new java.awt.Dimension(278, 178));
        panAcHdTree.setPreferredSize(new java.awt.Dimension(378, 178));
        panAcHdTree.setLayout(new java.awt.GridBagLayout());

        srpAcHdTree.setMinimumSize(new java.awt.Dimension(268, 178));
        srpAcHdTree.setPreferredSize(new java.awt.Dimension(368, 178));
        srpAcHdTree.setViewportView(treAcHdTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAcHdTree.add(srpAcHdTree, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panAccountCreation.add(panAcHdTree, gridBagConstraints);

        cPanel1.add(panAccountCreation);
        panAccountCreation.setBounds(4, 5, 1010, 430);

        cTabbedPane1.addTab("Account Creation", cPanel1);

        panHolder.setMaximumSize(new java.awt.Dimension(200, 300));
        panHolder.setMinimumSize(new java.awt.Dimension(200, 300));
        panHolder.setPreferredSize(new java.awt.Dimension(200, 300));
        panHolder.setLayout(new java.awt.GridBagLayout());

        panCallingCode.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Head Maintenance"));
        panCallingCode.setMinimumSize(new java.awt.Dimension(358, 490));
        panCallingCode.setName("cPanel_One"); // NOI18N
        panCallingCode.setPreferredSize(new java.awt.Dimension(380, 510));
        panCallingCode.setLayout(new java.awt.GridBagLayout());

        lblFloatAccount.setText("Float Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblFloatAccount, gridBagConstraints);

        lblContraHead.setText("Contra Account Head");
        lblContraHead.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblContraHead, gridBagConstraints);

        cboContraHead.setMinimumSize(new java.awt.Dimension(100, 21));
        cboContraHead.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(cboContraHead, gridBagConstraints);

        panCreditDebit.setName("cPanel_Five"); // NOI18N
        panCreditDebit.setLayout(new java.awt.GridBagLayout());

        lblClearing.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClearing.setText("Clearing");
        lblClearing.setMaximumSize(new java.awt.Dimension(60, 15));
        lblClearing.setMinimumSize(new java.awt.Dimension(60, 15));
        lblClearing.setName("lblClearing"); // NOI18N
        lblClearing.setPreferredSize(new java.awt.Dimension(60, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblClearing, gridBagConstraints);

        lblTransfer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTransfer.setText("Transfer");
        lblTransfer.setName("lblTransfer"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblTransfer, gridBagConstraints);

        lblCash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCash.setText("Cash");
        lblCash.setMaximumSize(new java.awt.Dimension(42, 15));
        lblCash.setMinimumSize(new java.awt.Dimension(42, 15));
        lblCash.setName("lblCash"); // NOI18N
        lblCash.setPreferredSize(new java.awt.Dimension(42, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblCash, gridBagConstraints);

        chkDebitClearing.setName("chkDebitClearing"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitClearing, gridBagConstraints);

        chkDebitTransfer.setName("chkCreditTransfer"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitTransfer, gridBagConstraints);

        chkDebitCash.setName("chkCreditCash"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitCash, gridBagConstraints);

        chkCreditClearing.setName("chkDebitClearing"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditClearing, gridBagConstraints);

        chkCreditTransfer.setName("chkDebitTransfer"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditTransfer, gridBagConstraints);

        chkCreditCash.setName("chkDebitCash"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditCash, gridBagConstraints);

        spt1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt1, gridBagConstraints);

        spt3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt3, gridBagConstraints);

        spt2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt4, gridBagConstraints);

        spt5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt7, gridBagConstraints);

        lblCredit.setText("Credit");
        lblCredit.setName("lblCredit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 8);
        panCreditDebit.add(lblCredit, gridBagConstraints);

        lblDebit.setText("Debit");
        lblDebit.setName("lblDebit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 4);
        panCreditDebit.add(lblDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(panCreditDebit, gridBagConstraints);

        cboTransactionPosting.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTransactionPosting.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(cboTransactionPosting, gridBagConstraints);

        lblTransactionPosting.setText("Transaction Posting");
        lblTransactionPosting.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblTransactionPosting, gridBagConstraints);

        lblPostingMode.setText("Posting Mode");
        lblPostingMode.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblPostingMode, gridBagConstraints);

        cboPostingMode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPostingMode.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(cboPostingMode, gridBagConstraints);

        lblReconcilliationAllowed.setText("Reconcilliation Allowed");
        lblReconcilliationAllowed.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblReconcilliationAllowed, gridBagConstraints);

        cboGLBalanceType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(cboGLBalanceType, gridBagConstraints);

        chkReconcilliationAllowed.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkReconcilliationAllowed.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chkReconcilliationAllowed.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panCallingCode.add(chkReconcilliationAllowed, gridBagConstraints);

        lblBalanceGLType.setText("Type of Balance in GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblBalanceGLType, gridBagConstraints);

        txtBalanceInGL.setBackground(new java.awt.Color(204, 204, 204));
        txtBalanceInGL.setEditable(false);
        txtBalanceInGL.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(txtBalanceInGL, gridBagConstraints);

        lblBalanceInGL.setText("Balance in GL");
        lblBalanceInGL.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblBalanceInGL, gridBagConstraints);

        panFloatAccount.setLayout(new java.awt.GridBagLayout());

        rdoFloatAccount.add(rdoFloatAccount_Yes);
        rdoFloatAccount_Yes.setText("Yes");
        rdoFloatAccount_Yes.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFloatAccount.add(rdoFloatAccount_Yes, gridBagConstraints);

        rdoFloatAccount.add(rdoFloatAccount_No);
        rdoFloatAccount_No.setText("No");
        rdoFloatAccount_No.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFloatAccount.add(rdoFloatAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCallingCode.add(panFloatAccount, gridBagConstraints);

        lblNegValue.setText("Negative Value Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblNegValue, gridBagConstraints);

        rdoNegValue.add(rdoNegValue_Yes);
        rdoNegValue_Yes.setText("Yes");
        panNegValue.add(rdoNegValue_Yes);

        rdoNegValue.add(rdoNegValue_No);
        rdoNegValue_No.setText("No");
        panNegValue.add(rdoNegValue_No);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCallingCode.add(panNegValue, gridBagConstraints);

        lblHdOfficeAc.setText("Head Office Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblHdOfficeAc, gridBagConstraints);

        chkHdOfficeAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHdOfficeAcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panCallingCode.add(chkHdOfficeAc, gridBagConstraints);

        lblReconcillationAcHd.setText("Reconciliation Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblReconcillationAcHd, gridBagConstraints);

        panRAcHd.setPreferredSize(new java.awt.Dimension(125, 21));
        panRAcHd.setLayout(new java.awt.GridBagLayout());

        txtReconcillationAcHd.setEditable(false);
        txtReconcillationAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReconcillationAcHd.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRAcHd.add(txtReconcillationAcHd, gridBagConstraints);

        btnRAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRAcHd.setEnabled(false);
        btnRAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRAcHd.add(btnRAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCallingCode.add(panRAcHd, gridBagConstraints);

        lblBalCheck.setText("Day End Zero Balance Check");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblBalCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panCallingCode.add(chkBalCheck, gridBagConstraints);

        lblAllowCustomerEntry.setText("Allow Customer A/c No. Entry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallingCode.add(lblAllowCustomerEntry, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panCallingCode.add(chkAllowCustomerEntry, gridBagConstraints);

        lblSerTaxApl.setText("Service Tax Applicable");
        lblSerTaxApl.setMinimumSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panCallingCode.add(lblSerTaxApl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCallingCode.add(cbServiceTax, gridBagConstraints);

        cLabel1.setText("Service Tax Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        panCallingCode.add(cLabel1, gridBagConstraints);

        cPanel2.setPreferredSize(new java.awt.Dimension(300, 27));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        txtserviceTaxId.setAllowAll(true);
        txtserviceTaxId.setMinimumSize(new java.awt.Dimension(6, 21));
        txtserviceTaxId.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel2.add(txtserviceTaxId, gridBagConstraints);

        btnGSTSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGSTSettings.setMinimumSize(new java.awt.Dimension(49, 21));
        btnGSTSettings.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGSTSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGSTSettingsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel2.add(btnGSTSettings, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 44;
        panCallingCode.add(cPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 112;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 0, 3, 0);
        panHolder.add(panCallingCode, gridBagConstraints);

        panAccountType.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Information"));
        panAccountType.setMinimumSize(new java.awt.Dimension(94, 275));
        panAccountType.setName("cPanel_Two"); // NOI18N
        panAccountType.setPreferredSize(new java.awt.Dimension(94, 275));
        panAccountType.setLayout(new java.awt.GridBagLayout());

        lblAccHeadCode.setFont(new java.awt.Font("MS Sans Serif", 0, 13)); // NOI18N
        lblAccHeadCode.setText("Account Head Code");
        lblAccHeadCode.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccHeadCode, gridBagConstraints);

        lblAccountHeadCodeDisplay.setText("x");
        lblAccountHeadCodeDisplay.setMaximumSize(new java.awt.Dimension(250, 15));
        lblAccountHeadCodeDisplay.setPreferredSize(new java.awt.Dimension(250, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountHeadCodeDisplay, gridBagConstraints);

        lblAccHeadDesc.setFont(new java.awt.Font("MS Sans Serif", 0, 13)); // NOI18N
        lblAccHeadDesc.setText("Account Head");
        lblAccHeadDesc.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccHeadDesc, gridBagConstraints);

        lblAccountHeadDescDisplay.setText("x");
        lblAccountHeadDescDisplay.setMaximumSize(new java.awt.Dimension(250, 15));
        lblAccountHeadDescDisplay.setPreferredSize(new java.awt.Dimension(250, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountHeadDescDisplay, gridBagConstraints);

        lblSubHead.setText("Sub Head");
        lblSubHead.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblSubHead, gridBagConstraints);

        lblSubHeadDisplay.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblSubHeadDisplay, gridBagConstraints);

        lblMajorHead.setText("Major Head");
        lblMajorHead.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblMajorHead, gridBagConstraints);

        lblMajorHeadDisplay.setText("x");
        lblMajorHeadDisplay.setMaximumSize(new java.awt.Dimension(250, 15));
        lblMajorHeadDisplay.setPreferredSize(new java.awt.Dimension(250, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblMajorHeadDisplay, gridBagConstraints);

        lblAccountType.setText("Account Type");
        lblAccountType.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountType, gridBagConstraints);

        lblAccountTypeDisplay.setText("x");
        lblAccountTypeDisplay.setMaximumSize(new java.awt.Dimension(250, 15));
        lblAccountTypeDisplay.setPreferredSize(new java.awt.Dimension(250, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountTypeDisplay, gridBagConstraints);

        spt8.setMinimumSize(new java.awt.Dimension(0, 5));
        spt8.setPreferredSize(new java.awt.Dimension(0, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(spt8, gridBagConstraints);

        lblAccountOpenedOn.setText("Account Opened On");
        lblAccountOpenedOn.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountOpenedOn, gridBagConstraints);

        lblAccountOpenedOnDisplay.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountOpenedOnDisplay, gridBagConstraints);

        lblAccountClosedOn.setText("Account Closed On");
        lblAccountClosedOn.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountClosedOn, gridBagConstraints);

        lblAccountClosedOnDisplay.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblAccountClosedOnDisplay, gridBagConstraints);

        lblFirstTransactionDate.setText("First Transaction Date");
        lblFirstTransactionDate.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblFirstTransactionDate, gridBagConstraints);

        lblFirstTransactionDateDisplay.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblFirstTransactionDateDisplay, gridBagConstraints);

        lblLastTransactionDate.setText("Last Transaction Date");
        lblLastTransactionDate.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblLastTransactionDate, gridBagConstraints);

        lblLastTransactionDateDisplay.setText("x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountType.add(lblLastTransactionDateDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 0, 16);
        panHolder.add(panAccountType, gridBagConstraints);

        cTabbedPane1.addTab("Account Maintainance", panHolder);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 875;
        gridBagConstraints.ipady = 202;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(49, 10, 0, 0);
        getContentPane().add(cTabbedPane1, gridBagConstraints);

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        getContentPane().add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        getContentPane().add(lblStatus1, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        // TODO add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        _observable.setStatus();
        lblStatus1.setText(_observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnView1ActionPerformed

    private void btnRAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRAcHdActionPerformed
        // TODO add your handling code here:
        btnType = RAHD;
        popUp();
    }//GEN-LAST:event_btnRAcHdActionPerformed

    private void chkHdOfficeAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHdOfficeAcActionPerformed
        // TODO add your handling code here:
        if (chkHdOfficeAc.isSelected()) {
            _observable.setChkReconcilliationAllowed(chkHdOfficeAc.isSelected());
            setBtnRAcHdEnabled(true);
        } else {
            _observable.setChkReconcilliationAllowed(false);
            txtReconcillationAcHd.setText("");
            setBtnRAcHdEnabled(false);
        }
    }//GEN-LAST:event_chkHdOfficeAcActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            final HashMap accMaintainMap = new HashMap();
            accMaintainMap.put("STATUS", authorizeStatus);
            accMaintainMap.put("USER_ID", TrueTransactMain.USER_ID);
            accMaintainMap.put("A/C HEAD", txtAccountHead.getText());
            ClientUtil.execute("authorizeAccMaintain", accMaintainMap);
            final HashMap accCreationMap = new HashMap();
            accCreationMap.put("STATUS", authorizeStatus);
            accCreationMap.put("USER_ID", TrueTransactMain.USER_ID);
            accCreationMap.put("ACCOUNT HEAD", txtAccountHead.getText());
            ClientUtil.execute("authorizeAccCreation", accCreationMap);
            _observable.setResult(_observable.getActionType());
            viewType = 0;
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Account Head");
            }
             if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Account Head");
            }
            btnCancelActionPerformed(null);
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectAccMaintainAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAccMaintain");
            viewType = AUTHORIZE;
            isFilled = false;
            //__ To Save the data in the Internal Frame...
            setModified(true);

            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        _observable.resetOBFields();
        ClientUtil.enableDisable(this, false);
        acHdEnableDisable(false);
        setBtnRAcHdEnabled(false);
        setButtonEnableDisable();
        _observable.setStatus();
        viewType = -1;
         if (fromNewAuthorizeUI) {
            this.dispose();
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
        }
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
        lblAccountHeadDescDisplay.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void acHdEnableDisable(boolean editEnable) {
        txtAccountHead.setEditable(editEnable);
        //  btnAcHd.setEnabled(editEnable);
        txtBalanceInGL.setEditable(false);
       if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
           lblSerTaxApl.setVisible(true);
           cbServiceTax.setVisible(true);
       }else{
          lblSerTaxApl.setVisible(false);
           cbServiceTax.setVisible(false); 
       }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAccountCreation);
        if (mandatoryMessage.length() > 0) {
            ClientUtil.displayAlert(mandatoryMessage);
        }
        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCallingCode);

//        if (cboContraHead.isEnabled() && (cboContraHead.getSelectedIndex() == -1 || cboContraHead.getSelectedIndex() == 0)){
//            mandatoryMessage = mandatoryMessage+_resourceBundle.getString("cboContraHead");
//        }
//        if(chkHdOfficeAc.isSelected() && txtReconcillationAcHd.getText().trim().equals("")){
//            if(txtReconcillationAcHd.getText().equals("")){
//                mandatoryMessage = mandatoryMessage + _resourceBundle.getString("txtReconcillationAcHd");
//            }
//        }
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else if (_observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || _observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            savePerformed();
        }
        //savePerformed();
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void savePerformed() {
        updateOBFields();
        _observable.doAction();
        _observable.resetOBFields();
        ClientUtil.enableDisable(this, false);
        acHdEnableDisable(false);
        setButtonEnableDisable();
        _observable.setResultStatus();
    }

    private boolean acHdValidate(boolean changeFields) {
        boolean valid = false;
        final HashMap whereMap = new HashMap();
        whereMap.put("AC_HD_ID", txtAccountHead.getText());
        List objList = ClientUtil.executeQuery("accMaintenance.getAcType_param", whereMap);
        if (objList != null && objList.size() > 0) {
            if (changeFields) {
                final String acctType = ((HashMap) objList.get(0)).get("MJR_AC_HD_TYPE").toString();
                operationContraHead(acctType);
                _observable.setGLBalType(acctType);
                _observable.fillAccountHeadInfo(txtAccountHead.getText());
            }
            valid = true;
        } else {
            objList.clear();
            objList = ClientUtil.executeQuery("accMaintenance.getAcType", whereMap);
            if (objList != null && objList.size() > 0) {
                acHdAlert(_resourceBundle.getString("acHdConfigured"));
            } else {
                acHdAlert(_resourceBundle.getString("acHdInvalid"));
            }
        }
        objList = null;
        return valid;
    }

    private void acHdAlert(String message) {
        ClientUtil.displayAlert(message);
        _observable.resetAcHdRelated();
        operationContraHead(null);
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        btnType = ClientConstants.ACTION_STATUS[2];
        popUp();
        cboAccountType.setEnabled(false);
        cboMajorHead.setEnabled(false);
        cboSubHead.setEnabled(false);
        txtAccountHeadCode.setEnabled(false);
        txtAccountHead.setEnabled(false);
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
           lblSerTaxApl.setVisible(true);
           cbServiceTax.setVisible(true);
       }else{
           lblSerTaxApl.setVisible(false);
           cbServiceTax.setVisible(false); 
       }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        _observable.resetOBFields();
        _observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        acHdEnableDisable(true);
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
        txtAccountHeadCode.setEnabled(false);
        txtAccountHeadCode.setEditable(false);
        txtAccountHead.requestFocus();
        setBtnRAcHdEnabled(false);
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

private void cboAccountTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAccountTypeActionPerformed
    // Add your handling code here:
    final String accountType = (String) (((ComboBoxModel) (cboAccountType).getModel())).getKeyForSelected();
    _observable.populateMajorHead(accountType);
    cboMajorHead.setModel(_observable.getCbmMajorHead());
    cboSubHead.setModel(new ComboBoxModel());
    makeAccHdCodeEmpty();
}//GEN-LAST:event_cboAccountTypeActionPerformed
    private void makeAccHdCodeEmpty() {
        this.txtAccountHeadCode.setText("");
    }
private void cboMajorHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMajorHeadActionPerformed
    // Add your handling code here:
    final String majorHead = (String) (((ComboBoxModel) (cboMajorHead).getModel())).getKeyForSelected();
    _observable.populateSubHead(majorHead);
    cboSubHead.setModel(_observable.getCbmSubHead());
    makeAccHdCodeEmpty();
}//GEN-LAST:event_cboMajorHeadActionPerformed

private void cboSubHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubHeadActionPerformed
    // Add your handling code here:
    if (_observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboSubHead.getSelectedIndex() != 0) {
        this.txtAccountHeadCode.setText((String) ((ComboBoxModel) cboMajorHead.getModel()).getKeyForSelected() + ((ComboBoxModel) cboSubHead.getModel()).getKeyForSelected() + _observable.getMaxAcHdCode());
    } else if (cboSubHead.getSelectedIndex() == 0) {
        makeAccHdCodeEmpty();
    }
}//GEN-LAST:event_cboSubHeadActionPerformed

private void rdoReceiveDayBookDetail_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReceiveDayBookDetail_YesActionPerformed
    rdoReceiveDayBookDetail_No.setSelected(false);
}//GEN-LAST:event_rdoReceiveDayBookDetail_YesActionPerformed

private void rdoReceiveDayBookDetail_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReceiveDayBookDetail_NoActionPerformed
    rdoReceiveDayBookDetail_Yes.setSelected(false);
}//GEN-LAST:event_rdoReceiveDayBookDetail_NoActionPerformed

private void rdoPayDayBookDetail_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPayDayBookDetail_NoActionPerformed
    rdoPayDayBookDetail_Yes.setSelected(false);
}//GEN-LAST:event_rdoPayDayBookDetail_NoActionPerformed

private void rdoPayDayBookDetail_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPayDayBookDetail_YesActionPerformed
    rdoPayDayBookDetail_No.setSelected(false);
}//GEN-LAST:event_rdoPayDayBookDetail_YesActionPerformed

    private void btnGSTSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGSTSettingsActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 12-01-2018 for 7013
         btnType = GST;
         popUp();
    }//GEN-LAST:event_btnGSTSettingsActionPerformed

    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        viewType = _observable.getActionType();
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (AHD.equals(btnType)) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectAcctHeadCallTOInsertList");
            } else if (RAHD.equals(btnType)) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectReconcillationAcctHeads");
            } else if (GST.equals(btnType)) {
                HashMap whereMap = new HashMap();
                whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectAllGSTSettings");
            }
        } else if (ClientConstants.ACTION_STATUS[2].equals(btnType)) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAcctHeadCallTOList");
        } else if (RAHD.equals(btnType)) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectReconcillationAcctHeads");
        } else if (_observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAcctHeadCallTOList");
        } else if (GST.equals(btnType)) {
            HashMap whereMap =  new HashMap();
            whereMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAllGSTSettings");
        }
        /*else{
        }
        throw new Exception();
        }*/

        //To avoid generation of errors when the popup table doesn't have any data
        /*ViewAll objViewAll = new ViewAll(this, viewMap);
        if (objViewAll.showPopup()){
        _log.info("This is updated code");
        objViewAll.show();
        }*/

        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        final HashMap hash = (HashMap) obj;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            _observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            _observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            _observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            _observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        _log.info("hash:" + hash);
        System.out.println("hash:hash:hash:hash:hash:" + hash);
        final String callingCode = (String) hash.get("A/C HEAD");
        final String acctType = (String) hash.get("A/C TYPE");
        ClientUtil.enableDisable(this, true);
        disableForDeleteAction();
        operationContraHead(acctType);
        updateOBFields();
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (AHD.equals(btnType)) {
                _observable.setTxtAccountHead(callingCode);
                _observable.setGLBalType(acctType);
            } else if (RAHD.equals(btnType)) {
                _observable.setTxtReconcillationAcHd(callingCode);
                _observable.setChkHdOfficeAc(chkHdOfficeAc.isSelected());
            }else if (GST.equals(btnType)) {// Added by nithya on 12-01-2018 for 7013
                txtserviceTaxId.setText(CommonUtil.convertObjToStr(hash.get("SERVICE_TAX")));
                _observable.setTxtserviceTaxId(txtserviceTaxId.getText());
            }
        } else if (ClientConstants.ACTION_STATUS[2].equals(btnType) || viewType == AUTHORIZE || _observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            isFilled = true;
            actionEditDelete(hash);
            System.out.println("outside actioneditdelete");
            _observable.fillAccountHeadInfo(callingCode);
            setButtonEnableDisable();
        } else if (RAHD.equals(btnType)) {
            _observable.setChkHdOfficeAc(chkHdOfficeAc.isSelected());
            _observable.setTxtReconcillationAcHd(callingCode);
        }else if (GST.equals(btnType)) {
            txtserviceTaxId.setText(CommonUtil.convertObjToStr(hash.get("SERVICE_TAX")));
            _observable.setTxtserviceTaxId(txtserviceTaxId.getText());
        }
        /*else if (_observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || _observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE){
        isFilled = true;
        actionEditDelete(hash);
        setButtonEnableDisable();
        }*/

        if (AHD.equals(btnType)) {
            _observable.fillAccountHeadInfo(callingCode);
        }
        _observable.setStatus();

        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            ClientUtil.enableDisable(panHolder, false);
        }
        //__ To Save the data in the Internal Frame...
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
        }
        setModified(true);
        System.out.println("end of fill data");
    }

    private void disableForDeleteAction() {
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
            ClientUtil.enableDisable(this, false);
        }
    }

    private void operationContraHead(String acctType) {
        if (acctType != null && (acctType.equalsIgnoreCase("ContraAssets") || acctType.equalsIgnoreCase("ContraLiability"))) {
            cboContraHead.setEnabled(true);
            _observable.populateContraHead(acctType);
            cboContraHead.setModel(_observable.getCbmContraHead());
        } else {
            _observable.setCboContraHead("");
            cboContraHead.setEnabled(false);
        }
    }

    /**/
    private void actionEditDelete(HashMap hash) {
        hash.put("AC_HD_ID", (String) hash.get("A/C HEAD"));
        hash.put("ACCOUNT_HEAD", (String) hash.get("A/C HEAD"));
        _observable.populateData(hash);
    }

    private void setFieldNames() {
        cScrollPane1.setName("cScrollPane1");
        cTable1.setName("cTable1");
        cboContraHead.setName("cboContraHead");
        cboGLBalanceType.setName("cboGLBalanceType");
        cboPostingMode.setName("cboPostingMode");
        cboTransactionPosting.setName("cboTransactionPosting");
        chkCreditCash.setName("chkCreditCash");
        chkCreditClearing.setName("chkCreditClearing");
        chkCreditTransfer.setName("chkCreditTransfer");
        chkDebitCash.setName("chkDebitCash");
        chkDebitClearing.setName("chkDebitClearing");
        chkDebitTransfer.setName("chkDebitTransfer");
        chkReconcilliationAllowed.setName("chkReconcilliationAllowed");
        dlgAccountMaintenance.setName("dlgAccountMaintenance");
        lblAccountClosedOn.setName("lblAccountClosedOn");
        lblAccountClosedOnDisplay.setName("lblAccountClosedOnDisplay");
        lblAccountHeadCodeDisplay.setName("lblAccountHeadCodeDisplay");
        lblAccountOpenedOn.setName("lblAccountOpenedOn");
        lblAccountOpenedOnDisplay.setName("lblAccountOpenedOnDisplay");
        lblAccountType.setName("lblAccountType");
        lblAccountTypeDisplay.setName("lblAccountTypeDisplay");
        lblBalanceGLType.setName("lblBalanceGLType");
        lblBalanceInGL.setName("lblBalanceInGL");
        lblAccountHead.setName("lblAccountHead");
        lblCash.setName("lblCash");
        lblClearing.setName("lblClearing");
        lblContraHead.setName("lblContraHead");
        lblCredit.setName("lblCredit");
        lblDebit.setName("lblDebit");
        lblFirstTransactionDate.setName("lblFirstTransactionDate");
        lblFirstTransactionDateDisplay.setName("lblFirstTransactionDateDisplay");
        lblFloatAccount.setName("lblFloatAccount");
        lblLastTransactionDate.setName("lblLastTransactionDate");
        lblLastTransactionDateDisplay.setName("lblLastTransactionDateDisplay");
        lblMajorHead.setName("lblMajorHead");
        lblMajorHeadDisplay.setName("lblMajorHeadDisplay");
        lblPostingMode.setName("lblPostingMode");
        lblReconcilliationAllowed.setName("lblReconcilliationAllowed");
        lblSubHead.setName("lblSubHead");
        lblSubHeadDisplay.setName("lblSubHeadDisplay");
        lblTransactionPosting.setName("lblTransactionPosting");
        lblTransfer.setName("lblTransfer");
        mbrMain.setName("mbrMain");
        panAccountType.setName("panAccountType");
        panCallingCode.setName("panCallingCode");
        panCreditDebit.setName("panCreditDebit");
        panFloatAccount.setName("panFloatAccount");
        rdoFloatAccount_No.setName("rdoFloatAccount_No");
        rdoFloatAccount_Yes.setName("rdoFloatAccount_Yes");

        panNegValue.setName("panNegValue");
        rdoNegValue_No.setName("rdoNegValue_No");
        rdoNegValue_Yes.setName("rdoNegValue_Yes");

        spt1.setName("spt1");
        spt2.setName("spt2");
        spt3.setName("spt3");
        spt4.setName("spt4");
        spt5.setName("spt5");
        spt6.setName("spt6");
        spt7.setName("spt7");
        spt8.setName("spt8");
        txtBalanceInGL.setName("txtBalanceInGL");
        txtAccountHead.setName("txtAccountHead");
        lblHdOfficeAc.setName("lblHdOfficeAc");
        chkHdOfficeAc.setName("chkHdOfficeAc");
        chkBalCheck.setName("chkBalCheck");
        lblReconcillationAcHd.setName("lblReconcillationAcHd");
        txtReconcillationAcHd.setName("txtReconcillationAcHd");
        btnRAcHd.setName("btnRAcHd");
        panRAcHd.setName("panRAcHd");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboAccountType.setName("cboAccountType");
        cboMajorHead.setName("cboMajorHead");
        cboSubHead.setName("cboSubHead");
        lblAccountHeadCode.setName("lblAccountHeadCode");
        lblAccountHeadDesc.setName("lblAccountHeadDesc");
        lblReceiveDayBookDetail.setName("lblReceiveDayBookDetail");
        lblPayDayBookDetail.setName("lblPayDayBookDetail");
        lblAccountHeadOrder.setName("lblAccountHeadOrder");
        lblAccountType.setName("lblAccountType");
        lblMajorHead.setName("lblMajorHead");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSubHead.setName("lblSubHead");
        mbrMain.setName("mbrMain");
        panAccountCreation.setName("panAccountCreation");
        panStatus.setName("panStatus");
        txtAccountHead.setName("txtAccountHead");
        txtAccountHeadCode.setName("txtAccountHeadCode");
        txtAccountHeadDesc.setName("txtAccountHeadDesc");
        lblSerTaxApl.setName("lblSerTaxApl");
        cbServiceTax.setName("cbServiceTax");
    }

    private void internationalize() {
        chkCreditClearing.setText(_resourceBundle.getString("chkCreditClearing"));
        lblAccountType.setText(_resourceBundle.getString("lblAccountType"));
        lblCredit.setText(_resourceBundle.getString("lblCredit"));
        lblAccountHead.setText(_resourceBundle.getString("lblAccountHead"));
        lblAccountClosedOnDisplay.setText(_resourceBundle.getString("lblAccountClosedOnDisplay"));
        chkDebitCash.setText(_resourceBundle.getString("chkDebitCash"));
        chkCreditTransfer.setText(_resourceBundle.getString("chkCreditTransfer"));
        rdoFloatAccount_No.setText(_resourceBundle.getString("rdoFloatAccount_No"));
        lblSubHead.setText(_resourceBundle.getString("lblSubHead"));
        lblLastTransactionDateDisplay.setText(_resourceBundle.getString("lblLastTransactionDateDisplay"));
        chkDebitClearing.setText(_resourceBundle.getString("chkDebitClearing"));
        lblAccountOpenedOn.setText(_resourceBundle.getString("lblAccountOpenedOn"));
        lblAccountClosedOn.setText(_resourceBundle.getString("lblAccountClosedOn"));
        lblSubHeadDisplay.setText(_resourceBundle.getString("lblSubHeadDisplay"));
        lblAccountOpenedOnDisplay.setText(_resourceBundle.getString("lblAccountOpenedOnDisplay"));
        chkCreditCash.setText(_resourceBundle.getString("chkCreditCash"));
        lblBalanceInGL.setText(_resourceBundle.getString("lblBalanceInGL"));
        lblFirstTransactionDateDisplay.setText(_resourceBundle.getString("lblFirstTransactionDateDisplay"));
        lblBalanceGLType.setText(_resourceBundle.getString("lblBalanceGLType"));
        lblClearing.setText(_resourceBundle.getString("lblClearing"));
        lblCash.setText(_resourceBundle.getString("lblCash"));
        rdoFloatAccount_Yes.setText(_resourceBundle.getString("rdoFloatAccount_Yes"));
        lblAccountTypeDisplay.setText(_resourceBundle.getString("lblAccountTypeDisplay"));

        lblAccountHeadCodeDisplay.setText(_resourceBundle.getString("lblAccountHeadCodeDisplay"));
        lblFirstTransactionDate.setText(_resourceBundle.getString("lblFirstTransactionDate"));
        lblPostingMode.setText(_resourceBundle.getString("lblPostingMode"));
        lblTransactionPosting.setText(_resourceBundle.getString("lblTransactionPosting"));
        lblFloatAccount.setText(_resourceBundle.getString("lblFloatAccount"));
        lblDebit.setText(_resourceBundle.getString("lblDebit"));
        lblContraHead.setText(_resourceBundle.getString("lblContraHead"));
        lblTransfer.setText(_resourceBundle.getString("lblTransfer"));
        lblMajorHead.setText(_resourceBundle.getString("lblMajorHead"));
        chkReconcilliationAllowed.setText(_resourceBundle.getString("chkReconcilliationAllowed"));
        lblReconcilliationAllowed.setText(_resourceBundle.getString("lblReconcilliationAllowed"));
        chkDebitTransfer.setText(_resourceBundle.getString("chkDebitTransfer"));
        lblLastTransactionDate.setText(_resourceBundle.getString("lblLastTransactionDate"));
        lblMajorHeadDisplay.setText(_resourceBundle.getString("lblMajorHeadDisplay"));

        rdoNegValue_No.setText(_resourceBundle.getString("rdoNegValue_No"));
        rdoNegValue_Yes.setText(_resourceBundle.getString("rdoNegValue_Yes"));

        lblHdOfficeAc.setText(_resourceBundle.getString("lblHdOfficeAc"));
        lblReconcillationAcHd.setText(_resourceBundle.getString("lblReconcillationAcHd"));
        lblBalCheck.setText(_resourceBundle.getString("lblBalCheck"));
        lblSerTaxApl.setText(_resourceBundle.getString("lblSerTaxApl"));

    }

    public void updateOBFields() {

        _observable.setTxtAccountHead(txtAccountHead.getText());
        if (cboContraHead != null && ((ComboBoxModel) cboContraHead.getModel()).getKeyForSelected() != null) {
            _observable.setCboContraHead((String) ((ComboBoxModel) cboContraHead.getModel()).getKeyForSelected());

        } else {
            _observable.setCboContraHead("");
        }
        _observable.setChkCreditClearing(chkCreditClearing.isSelected());
        _observable.setChkCreditTransfer(chkCreditTransfer.isSelected());
        _observable.setChkCreditCash(chkCreditCash.isSelected());
        _observable.setChkDebitClearing(chkDebitClearing.isSelected());
        _observable.setChkDebitTransfer(chkDebitTransfer.isSelected());
        _observable.setChkDebitCash(chkDebitCash.isSelected());
        _observable.setCboTransactionPosting((String) ((ComboBoxModel) cboTransactionPosting.getModel()).getKeyForSelected());
        _observable.setCboPostingMode((String) ((ComboBoxModel) cboPostingMode.getModel()).getKeyForSelected());
        _observable.setCboGLBalanceType((String) ((ComboBoxModel) cboGLBalanceType.getModel()).getKeyForSelected());
        _observable.setChkReconcilliationAllowed(chkReconcilliationAllowed.isSelected());
        _observable.setTxtBalanceInGL(txtBalanceInGL.getText());
        _observable.setRdoFloatAccount_Yes(rdoFloatAccount_Yes.isSelected());
        _observable.setRdoFloatAccount_No(rdoFloatAccount_No.isSelected());
        _observable.setModule(getModule());
        _observable.setScreen(getScreen());

        _observable.setRdoNegValue_No(rdoNegValue_No.isSelected());
        _observable.setRdoNegValue_Yes(rdoNegValue_Yes.isSelected());

        _observable.setChkHdOfficeAc(chkHdOfficeAc.isSelected());
        _observable.setTxtReconcillationAcHd(txtReconcillationAcHd.getText());
        _observable.setChkBalCheck(chkBalCheck.isSelected());
        _observable.setChkAllowCustomerEntry(chkAllowCustomerEntry.isSelected());
        _observable.setCbmAccountType((com.see.truetransact.clientutil.ComboBoxModel) cboAccountType.getModel());
        _observable.setCbmMajorHead((com.see.truetransact.clientutil.ComboBoxModel) cboMajorHead.getModel());
        _observable.setCbmSubHead((com.see.truetransact.clientutil.ComboBoxModel) cboSubHead.getModel());
        _observable.setCboAccountType((String) ((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected());
        _observable.setCboMajorHead((String) ((ComboBoxModel) cboMajorHead.getModel()).getKeyForSelected());
        _observable.setCboSubHead((String) ((ComboBoxModel) cboSubHead.getModel()).getKeyForSelected());
        _observable.setCbServiceTax(cbServiceTax.isSelected());

        _observable.setTxtAccountHeadCode(txtAccountHeadCode.getText());
        _observable.setTxtAccountHeadDesc(txtAccountHeadDesc.getText());
        _observable.setModule(getModule());
        _observable.setScreen(getScreen());
        if (rdoReceiveDayBookDetail_Yes.isSelected() == true) {
            _observable.setRdoReceiveDayBookDetail(CommonUtil.convertObjToStr("Y"));
        } else {
            _observable.setRdoReceiveDayBookDetail(CommonUtil.convertObjToStr("N"));
        }

        if (rdoPayDayBookDetail_Yes.isSelected() == true) {
            _observable.setRdoPayDayBookDetail(CommonUtil.convertObjToStr("Y"));
        } else {
            _observable.setRdoPayDayBookDetail(CommonUtil.convertObjToStr("N"));
        }
        _observable.setTxtAccountHead(txtAccountHead.getText());
        _observable.setTxtAccountHeadOrder(txtAccountHeadOrder.getText());
        _observable.setTxtserviceTaxId(txtserviceTaxId.getText());  // Added by nithya on 12-01-2018 for 7013

    }

    private void removeRadioButtons() {
        rdoFloatAccount.remove(rdoFloatAccount_No);
        rdoFloatAccount.remove(rdoFloatAccount_Yes);

        rdoNegValue.remove(rdoNegValue_No);
        rdoNegValue.remove(rdoNegValue_Yes);
    }

    public void update(Observable observed, Object arg) {
        System.out.println("inside update");
        removeRadioButtons();
        txtAccountHead.setText(_observable.getTxtAccountHead());
        cboContraHead.setSelectedItem(_observable.getCboContraHead());
        chkCreditClearing.setSelected(_observable.getChkCreditClearing());
        chkCreditTransfer.setSelected(_observable.getChkCreditTransfer());
        chkCreditCash.setSelected(_observable.getChkCreditCash());
        chkDebitClearing.setSelected(_observable.getChkDebitClearing());
        chkDebitTransfer.setSelected(_observable.getChkDebitTransfer());
        chkDebitCash.setSelected(_observable.getChkDebitCash());
        chkReconcilliationAllowed.setSelected(_observable.getChkReconcilliationAllowed());
        chkHdOfficeAc.setSelected(_observable.getChkHdOfficeAc());
        cboTransactionPosting.setSelectedItem(_observable.getCboTransactionPosting());
        cboPostingMode.setSelectedItem(_observable.getCboPostingMode());
        cboGLBalanceType.setSelectedItem(_observable.getCboGLBalanceType());

        txtBalanceInGL.setText(_observable.getTxtBalanceInGL());
        rdoFloatAccount_Yes.setSelected(_observable.getRdoFloatAccount_Yes());
        rdoFloatAccount_No.setSelected(_observable.getRdoFloatAccount_No());

        rdoNegValue_No.setSelected(_observable.getRdoNegValue_No());
        rdoNegValue_Yes.setSelected(_observable.getRdoNegValue_Yes());

        lblAccountClosedOnDisplay.setText(_observable.getLblAccountClosedOnDisplay());
        lblAccountClosedOnDisplay.setToolTipText(lblAccountClosedOnDisplay.getText());
        lblAccountOpenedOnDisplay.setText(_observable.getLblAccountOpenedOnDisplay());
        lblAccountOpenedOnDisplay.setToolTipText(lblAccountOpenedOnDisplay.getText());
        lblAccountTypeDisplay.setText(_observable.getLblAccountTypeDisplay());
        lblAccountTypeDisplay.setToolTipText(lblAccountTypeDisplay.getText());
        lblFirstTransactionDateDisplay.setText(_observable.getLblFirstTransactionDateDisplay());
        lblFirstTransactionDateDisplay.setToolTipText(lblFirstTransactionDateDisplay.getText());
        lblLastTransactionDateDisplay.setText(_observable.getLblLastTransactionDateDisplay());
        lblLastTransactionDateDisplay.setToolTipText(lblLastTransactionDateDisplay.getText());
        lblMajorHeadDisplay.setText(_observable.getLblMajorHeadDisplay());
        lblMajorHeadDisplay.setToolTipText(lblMajorHeadDisplay.getText());
        lblSubHeadDisplay.setText(_observable.getLblSubHeadDisplay());
        lblSubHeadDisplay.setToolTipText(lblSubHeadDisplay.getText());
        lblAccountHeadCodeDisplay.setText(_observable.getLblAccountHeadCodeDisplay());
        lblAccountHeadCodeDisplay.setToolTipText(lblAccountHeadCodeDisplay.getText());
        lblAccountHeadDescDisplay.setText(_observable.getLblAccountHeadDescDisplay());
        lblAccountHeadDescDisplay.setToolTipText(lblAccountHeadDescDisplay.getText());
        lblStatus1.setText(_observable.getLblStatus());

        txtReconcillationAcHd.setText(_observable.getTxtReconcillationAcHd());
        chkBalCheck.setSelected(_observable.getChkBalCheck());
        chkAllowCustomerEntry.setSelected(_observable.isChkAllowCustomerEntry());
        cboAccountType.setSelectedItem(_observable.getCboAccountType());
        cboMajorHead.setSelectedItem(_observable.getCboMajorHead());
        cboSubHead.setSelectedItem(_observable.getCboSubHead());
        txtAccountHead.setText(_observable.getTxtAccountHead());
        txtAccountHeadCode.setText(_observable.getTxtAccountHeadCode());
        txtAccountHeadDesc.setText(_observable.getTxtAccountHeadDesc());
        cbServiceTax.setSelected(_observable.isCbServiceTax());

        if (_observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && _observable.getActionType() != ClientConstants.ACTIONTYPE_CANCEL) {
            String recDayBook = CommonUtil.convertObjToStr(_observable.getRdoReceiveDayBookDetail());
            if (recDayBook.length() > 0 && recDayBook.equals("Y")) {
                rdoReceiveDayBookDetail_Yes.setSelected(true);
                rdoReceiveDayBookDetail_No.setSelected(false);
            } else {
                rdoReceiveDayBookDetail_Yes.setSelected(false);
                rdoReceiveDayBookDetail_No.setSelected(true);
            }
            String payDayBook = CommonUtil.convertObjToStr(_observable.getRdoPayDayBookDetail());
            if (payDayBook.length() > 0 && payDayBook.equals("Y")) {
                rdoPayDayBookDetail_Yes.setSelected(true);
                rdoPayDayBookDetail_No.setSelected(false);
            } else {
                rdoPayDayBookDetail_Yes.setSelected(false);
                rdoPayDayBookDetail_No.setSelected(true);
            }
        }
        txtAccountHeadOrder.setText(_observable.getTxtAccountHeadOrder());
        txtserviceTaxId.setText(_observable.getTxtserviceTaxId()); // Added by nithya on 12-01-2018 for 7013

        addRadioButtons();
    }

    private void addRadioButtons() {
        rdoFloatAccount = new CButtonGroup();
        rdoFloatAccount.add(rdoFloatAccount_No);
        rdoFloatAccount.add(rdoFloatAccount_Yes);

        rdoNegValue = new CButtonGroup();
        rdoNegValue.add(rdoNegValue_No);
        rdoNegValue.add(rdoNegValue_Yes);
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGSTSettings;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRAcHd;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CTable cTable1;
    private com.see.truetransact.uicomponent.CCheckBox cbServiceTax;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboContraHead;
    private com.see.truetransact.uicomponent.CComboBox cboGLBalanceType;
    private com.see.truetransact.uicomponent.CComboBox cboMajorHead;
    private com.see.truetransact.uicomponent.CComboBox cboPostingMode;
    private com.see.truetransact.uicomponent.CComboBox cboSubHead;
    private com.see.truetransact.uicomponent.CComboBox cboTransactionPosting;
    private com.see.truetransact.uicomponent.CCheckBox chkAllowCustomerEntry;
    private com.see.truetransact.uicomponent.CCheckBox chkBalCheck;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditCash;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitCash;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkHdOfficeAc;
    private com.see.truetransact.uicomponent.CCheckBox chkReconcilliationAllowed;
    private com.see.truetransact.uicomponent.CDialog dlgAccountMaintenance;
    private javax.swing.JLabel lblAccHeadCode;
    private javax.swing.JLabel lblAccHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountClosedOn;
    private com.see.truetransact.uicomponent.CLabel lblAccountClosedOnDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadCode;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadCodeDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDescDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadOrder;
    private com.see.truetransact.uicomponent.CLabel lblAccountOpenedOn;
    private com.see.truetransact.uicomponent.CLabel lblAccountOpenedOnDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblAccountType1;
    private com.see.truetransact.uicomponent.CLabel lblAccountTypeDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAllowCustomerEntry;
    private com.see.truetransact.uicomponent.CLabel lblBalCheck;
    private com.see.truetransact.uicomponent.CLabel lblBalanceGLType;
    private com.see.truetransact.uicomponent.CLabel lblBalanceInGL;
    private com.see.truetransact.uicomponent.CLabel lblCash;
    private com.see.truetransact.uicomponent.CLabel lblClearing;
    private com.see.truetransact.uicomponent.CLabel lblContraHead;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblFirstTransactionDate;
    private com.see.truetransact.uicomponent.CLabel lblFirstTransactionDateDisplay;
    private com.see.truetransact.uicomponent.CLabel lblFloatAccount;
    private com.see.truetransact.uicomponent.CLabel lblHdOfficeAc;
    private com.see.truetransact.uicomponent.CLabel lblLastTransactionDate;
    private com.see.truetransact.uicomponent.CLabel lblLastTransactionDateDisplay;
    private com.see.truetransact.uicomponent.CLabel lblMajorHead;
    private com.see.truetransact.uicomponent.CLabel lblMajorHead1;
    private com.see.truetransact.uicomponent.CLabel lblMajorHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNegValue;
    private com.see.truetransact.uicomponent.CLabel lblPayDayBookDetail;
    private com.see.truetransact.uicomponent.CLabel lblPostingMode;
    private com.see.truetransact.uicomponent.CLabel lblReceiveDayBookDetail;
    private com.see.truetransact.uicomponent.CLabel lblReconcillationAcHd;
    private com.see.truetransact.uicomponent.CLabel lblReconcilliationAllowed;
    private com.see.truetransact.uicomponent.CLabel lblSerTaxApl;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblSubHead;
    private com.see.truetransact.uicomponent.CLabel lblSubHead1;
    private com.see.truetransact.uicomponent.CLabel lblSubHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblTransactionPosting;
    private com.see.truetransact.uicomponent.CLabel lblTransfer;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcHdTree;
    private com.see.truetransact.uicomponent.CPanel panAccountCreation;
    private com.see.truetransact.uicomponent.CPanel panAccountType;
    private com.see.truetransact.uicomponent.CPanel panAcctCreationDetails;
    private com.see.truetransact.uicomponent.CPanel panCallingCode;
    private com.see.truetransact.uicomponent.CPanel panCreditDebit;
    private com.see.truetransact.uicomponent.CPanel panFloatAccount;
    private com.see.truetransact.uicomponent.CPanel panHolder;
    private com.see.truetransact.uicomponent.CPanel panNegValue;
    private com.see.truetransact.uicomponent.CPanel panPayDayBookDetail;
    private com.see.truetransact.uicomponent.CPanel panRAcHd;
    private com.see.truetransact.uicomponent.CPanel panReceiveDayBookDetail;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFloatAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoFloatAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFloatAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNegValue;
    private com.see.truetransact.uicomponent.CRadioButton rdoNegValue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoNegValue_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayDayBookDetail_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayDayBookDetail_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoReceiveDayBookDetail_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoReceiveDayBookDetail_Yes;
    private com.see.truetransact.uicomponent.CSeparator spt1;
    private com.see.truetransact.uicomponent.CSeparator spt2;
    private com.see.truetransact.uicomponent.CSeparator spt3;
    private com.see.truetransact.uicomponent.CSeparator spt4;
    private com.see.truetransact.uicomponent.CSeparator spt5;
    private com.see.truetransact.uicomponent.CSeparator spt6;
    private com.see.truetransact.uicomponent.CSeparator spt7;
    private com.see.truetransact.uicomponent.CSeparator spt8;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpAcHdTree;
    private javax.swing.JToolBar tbrAcctHeadMain;
    private javax.swing.JTree treAcHdTree;
    private com.see.truetransact.uicomponent.CTextField txtAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadCode;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadOrder;
    private com.see.truetransact.uicomponent.CTextField txtBalanceInGL;
    private com.see.truetransact.uicomponent.CTextField txtReconcillationAcHd;
    private com.see.truetransact.uicomponent.CTextField txtserviceTaxId;
    // End of variables declaration//GEN-END:variables
}
