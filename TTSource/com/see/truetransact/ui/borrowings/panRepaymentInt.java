/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * panRepaymentInt.java
 *
 * Created on September 12, 2011, 6:44 PM
 */
package com.see.truetransact.ui.borrowings;

import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.text.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.math.BigDecimal;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
//import java.util.Date;

/**
 *
 * @author user
 */
public class panRepaymentInt extends CInternalFrame implements Observer, UIMandatoryField {

    private RepaymentIntOB observable;
    private RepaymentIntMRB objMandatoryRB = new RepaymentIntMRB();//Instance for the MandatoryResourceBundle
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    private double totalAmt = 0.0;
    TransactionUI transactionUI = new TransactionUI();
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    private int rejectFlag = 0;
    String borrowNumber = "";
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form Repayment
     */
    public panRepaymentInt() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panRepIntCls, getMandatoryHashMap());
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("BORROW_REPAYMENT");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panRepIntCls, false);
        setButtonEnableDisable();
        btnVwBr.setEnabled(false);
        disablepanClose(false);
        tdtLastRepaidDate.setDateValue("");
        //Added By Suresh
        transactionUI.setProdType();
    }

    private void disablepanClose(boolean flag) {
        if (flag) {
            chkClose.setEnabled(true);
        } else {
            chkClose.setEnabled(false);
            tdtClosedDte.setEnabled(false);
        }
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        txtBorrowingRefNo.disable();
        txtIntPayable.disable();
        txtPenalPayable.disable();
        txtPrinBalance.disable();
        txtIntBalance.disable();
        txtPenalBalance.disable();
        txtChargesBalance.disable();
        disablepanClose(false);
        tdtLastRepaidDate.setDateValue("");
    }

    private void setMaxLengths() {
        txtPrinRepaid.setMaxLength(32);
        txtIntRepaid.setMaxLength(32);
        txtPenalRepaid.setMaxLength(32);
        txtChargesRepaid.setMaxLength(32);
        txtPrinBalance.setMaxLength(32);
        txtIntBalance.setMaxLength(32);
        txtPenalBalance.setMaxLength(32);
        txtChargesBalance.setMaxLength(32);
        txtPrinRepaid.setValidation(new CurrencyValidation(16,2));
        txtIntRepaid.setValidation(new CurrencyValidation(16,2));
        txtPenalRepaid.setValidation(new CurrencyValidation(16,2));
        txtChargesRepaid.setValidation(new CurrencyValidation(16,2));
        txtPrinBalance.setValidation(new CurrencyValidation());
        txtIntBalance.setValidation(new CurrencyValidation());
        txtPenalBalance.setValidation(new CurrencyValidation());
        txtChargesBalance.setValidation(new CurrencyValidation());
        txtCurrBal.setValidation(new CurrencyValidation());
        txtAmtSanctioned.setValidation(new CurrencyValidation());
        txtAmtBorrowed.setValidation(new CurrencyValidation());
        txtIntPayable.setValidation(new CurrencyValidation());
        txtPenalPayable.setValidation(new CurrencyValidation());
    }

    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblBrRefNo.setName("lblBrRefNo");
        txtBorrowingRefNo.setName("txtBorrowingRefNo");
        btnVwBr.setName("btnVwBr");
        cDetailPanel.setName("cDetailPanel");
        lblBrNo.setName("lblBrNo");
        txtBorrowingNo.setName("txtBorrowingNo");
        lblAgency.setName("lblAgency");
        txtAgency.setName("txtAgency");
        lblType.setName("lblType");
        txtType.setName("txtType");
        lblDes.setName("lblDes");
        txtaDescription.setName("txtaDescription");
        lblSanDate.setName("lblSanDate");
        tdtDateSanctioned.setName("tdtDateSanctioned");
        lblSanAmt.setName("lblSanAmt");
        txtAmtSanctioned.setName("txtAmtSanctioned");
        lblRateInt.setName("lblRateInt");
        txtRateInterest.setName("txtRateInterest");
        lblNoInstall.setName("lblNoInstall");
        txtnoofInstall.setName("txtnoofInstall");
        lblAmtBorrwed.setName("lblAmtBorrwed");
        txtAmtBorrowed.setName("txtAmtBorrowed");
        lblPrinRep.setName("lblPrinRep");
        txtPrinRepFrq.setName("txtPrinRepFrq");
        lblIntRep.setName("lblIntRep");
        txtIntRepFrq.setName("txtIntRepFrq");
        lblMorotorium.setName("lblMorotorium");
        txtMorotorium.setName("txtMorotorium");
        lblSanExpDate.setName("lblSanExpDate");
        tdtDateExpiry.setName("tdtDateExpiry");
        panRepIntCls.setName("panRepIntCls");
        lblLastRepDate.setName("lblLastRepDate");
        tdtLastRepaidDate.setName("tdtLastRepaidDate");
        lblDteIntPaid.setName("lblDteIntPaid");
        tdtIntPaid.setName("tdtIntPaid");
        tdtClosedDte.setName("tdtClosedDte");
        lblIntPayable.setName("lblIntPayable");
        txtIntPayable.setName("txtIntPayable");
        lblPenalPayable.setName("lblPenalPayable");
        txtPenalPayable.setName("txtPenalPayable");
        lblPrinRepaid.setName("lblPrinRepaid");
        txtPrinRepaid.setName("txtPrinRepaid");
        lblIntRepaid.setName("lblIntRepaid");
        txtIntRepaid.setName("txtIntRepaid");
        lblPenalRepaid.setName("lblPenalRepaid");
        txtPenalRepaid.setName("txtPenalRepaid");
        lalChargesRepaid.setName("lalChargesRepaid");
        txtChargesRepaid.setName("txtChargesRepaid");
        //cheque details
        lblCheckPaid.setName("lblCheckPaid");
        txtCheckNo.setName("txtCheckNo");
        lblCheckDate.setName("lblCheckDate");
        tdtCheckDate.setName("tdtCheckDate");
        //end..
        lblPrinBalance.setName("lblPrinBalance");
        txtPrinBalance.setName("txtPrinBalance");
        lblIntBalance.setName("lblIntBalance");
        txtIntBalance.setName("txtIntBalance");
        lblPenalBalance.setName("lblPenalBalance");
        txtPenalBalance.setName("txtPenalBalance");
        lblChargesBalance.setName("lblChargesBalance");
        txtChargesBalance.setName("txtChargesBalance");
        panTrans.setName("panTrans");
        txtCurrBal.setName("txtCurrBal");
    }

    private void initComponentData() {
        try {
            txtIntPayable.setText("0.00");
            txtPenalPayable.setText("0.00");
            txtPrinBalance.setText("0.00");
            txtIntBalance.setText("0.00");
            txtPenalBalance.setText("0.00");
            txtChargesBalance.setText("0.00");
            tdtLastRepaidDate.setDateValue("");
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setObservable() {
        try {
            observable = RepaymentIntOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panRepIntCls = new com.see.truetransact.uicomponent.CPanel();
        txtBorrowingRefNo = new com.see.truetransact.uicomponent.CTextField();
        btnVwBr = new com.see.truetransact.uicomponent.CButton();
        lblBrRefNo = new com.see.truetransact.uicomponent.CLabel();
        cDetailPanel = new com.see.truetransact.uicomponent.CPanel();
        txtRateInterest = new com.see.truetransact.uicomponent.CTextField();
        txtnoofInstall = new com.see.truetransact.uicomponent.CTextField();
        tdtLastRepaidDate = new com.see.truetransact.uicomponent.CDateField();
        lblSanExpDate = new com.see.truetransact.uicomponent.CLabel();
        lblMorotorium = new com.see.truetransact.uicomponent.CLabel();
        lblIntRep = new com.see.truetransact.uicomponent.CLabel();
        lblPrinRep = new com.see.truetransact.uicomponent.CLabel();
        lblNoInstall = new com.see.truetransact.uicomponent.CLabel();
        lblRateInt = new com.see.truetransact.uicomponent.CLabel();
        lblLastRepDate = new com.see.truetransact.uicomponent.CLabel();
        txtType = new com.see.truetransact.uicomponent.CTextField();
        txtAmtBorrowed = new com.see.truetransact.uicomponent.CTextField();
        tdtDateSanctioned = new com.see.truetransact.uicomponent.CDateField();
        txtaDescription = new com.see.truetransact.uicomponent.CTextArea();
        txtBorrowingNo = new com.see.truetransact.uicomponent.CTextField();
        lblBrNo = new com.see.truetransact.uicomponent.CLabel();
        lblAgency = new com.see.truetransact.uicomponent.CLabel();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        lblDes = new com.see.truetransact.uicomponent.CLabel();
        lblSanDate = new com.see.truetransact.uicomponent.CLabel();
        lblSanAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAmtBorrwed = new com.see.truetransact.uicomponent.CLabel();
        txtAgency = new com.see.truetransact.uicomponent.CTextField();
        txtAmtSanctioned = new com.see.truetransact.uicomponent.CTextField();
        txtPrinRepFrq = new com.see.truetransact.uicomponent.CTextField();
        txtIntRepFrq = new com.see.truetransact.uicomponent.CTextField();
        txtMorotorium = new com.see.truetransact.uicomponent.CTextField();
        tdtDateExpiry = new com.see.truetransact.uicomponent.CDateField();
        lblLastRepDate1 = new com.see.truetransact.uicomponent.CLabel();
        txtCurrBal = new com.see.truetransact.uicomponent.CTextField();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        txtChargesBalance = new com.see.truetransact.uicomponent.CTextField();
        txtPenalBalance = new com.see.truetransact.uicomponent.CTextField();
        txtIntBalance = new com.see.truetransact.uicomponent.CTextField();
        txtPrinBalance = new com.see.truetransact.uicomponent.CTextField();
        lblPrinBalance = new com.see.truetransact.uicomponent.CLabel();
        lblIntBalance = new com.see.truetransact.uicomponent.CLabel();
        lblPenalBalance = new com.see.truetransact.uicomponent.CLabel();
        lblChargesBalance = new com.see.truetransact.uicomponent.CLabel();
        txtChargesRepaid = new com.see.truetransact.uicomponent.CTextField();
        txtPenalRepaid = new com.see.truetransact.uicomponent.CTextField();
        txtIntRepaid = new com.see.truetransact.uicomponent.CTextField();
        txtPrinRepaid = new com.see.truetransact.uicomponent.CTextField();
        lblPrinRepaid = new com.see.truetransact.uicomponent.CLabel();
        lblIntRepaid = new com.see.truetransact.uicomponent.CLabel();
        lblPenalRepaid = new com.see.truetransact.uicomponent.CLabel();
        lalChargesRepaid = new com.see.truetransact.uicomponent.CLabel();
        txtPenalPayable = new com.see.truetransact.uicomponent.CTextField();
        txtIntPayable = new com.see.truetransact.uicomponent.CTextField();
        tdtIntPaid = new com.see.truetransact.uicomponent.CDateField();
        lblDteIntPaid = new com.see.truetransact.uicomponent.CLabel();
        lblIntPayable = new com.see.truetransact.uicomponent.CLabel();
        lblPenalPayable = new com.see.truetransact.uicomponent.CLabel();
        panClose = new com.see.truetransact.uicomponent.CPanel();
        chkClose = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        tdtClosedDte = new com.see.truetransact.uicomponent.CDateField();
        lblCheckPaid = new javax.swing.JLabel();
        txtCheckNo = new javax.swing.JTextField();
        lblCheckDate = new javax.swing.JLabel();
        tdtCheckDate = new com.see.truetransact.uicomponent.CDateField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 660));
        setPreferredSize(new java.awt.Dimension(850, 660));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrTokenConfig.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMain.add(tbrTokenConfig, gridBagConstraints);

        panRepIntCls.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panRepIntCls.setMaximumSize(new java.awt.Dimension(840, 610));
        panRepIntCls.setMinimumSize(new java.awt.Dimension(840, 610));
        panRepIntCls.setPreferredSize(new java.awt.Dimension(840, 610));
        panRepIntCls.setLayout(new java.awt.GridBagLayout());

        txtBorrowingRefNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 149;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRepIntCls.add(txtBorrowingRefNo, gridBagConstraints);

        btnVwBr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnVwBr.setToolTipText("Search");
        btnVwBr.setNextFocusableComponent(tdtIntPaid);
        btnVwBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowingAction(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRepIntCls.add(btnVwBr, gridBagConstraints);

        lblBrRefNo.setText("Borrowing ref number ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRepIntCls.add(lblBrRefNo, gridBagConstraints);

        cDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        cDetailPanel.setMaximumSize(new java.awt.Dimension(800, 180));
        cDetailPanel.setMinimumSize(new java.awt.Dimension(800, 180));
        cDetailPanel.setPreferredSize(new java.awt.Dimension(800, 180));
        cDetailPanel.setLayout(new java.awt.GridBagLayout());

        txtRateInterest.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRateInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateInterest.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtRateInterest, gridBagConstraints);

        txtnoofInstall.setMaximumSize(new java.awt.Dimension(100, 21));
        txtnoofInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        txtnoofInstall.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtnoofInstall, gridBagConstraints);

        tdtLastRepaidDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(tdtLastRepaidDate, gridBagConstraints);

        lblSanExpDate.setText("Sanction expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblSanExpDate, gridBagConstraints);

        lblMorotorium.setText("Morotorium ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblMorotorium, gridBagConstraints);

        lblIntRep.setText("Interest Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblIntRep, gridBagConstraints);

        lblPrinRep.setText("Principal Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(lblPrinRep, gridBagConstraints);

        lblNoInstall.setText("No of installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblNoInstall, gridBagConstraints);

        lblRateInt.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblRateInt, gridBagConstraints);

        lblLastRepDate.setText("Last repaid date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblLastRepDate, gridBagConstraints);

        txtType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtType.setEnabled(false);
        txtType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtType, gridBagConstraints);

        txtAmtBorrowed.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmtBorrowed.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtBorrowed.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAmtBorrowed, gridBagConstraints);

        tdtDateSanctioned.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(tdtDateSanctioned, gridBagConstraints);

        txtaDescription.setRows(7);
        txtaDescription.setMaximumSize(new java.awt.Dimension(160, 45));
        txtaDescription.setMinimumSize(new java.awt.Dimension(160, 45));
        txtaDescription.setPreferredSize(new java.awt.Dimension(160, 45));
        txtaDescription.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtaDescription, gridBagConstraints);

        txtBorrowingNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBorrowingNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBorrowingNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtBorrowingNo, gridBagConstraints);

        lblBrNo.setText("Borrowing number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblBrNo, gridBagConstraints);

        lblAgency.setText("Agency ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblAgency, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblType, gridBagConstraints);

        lblDes.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblDes, gridBagConstraints);

        lblSanDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(lblSanDate, gridBagConstraints);

        lblSanAmt.setText("Sanction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblSanAmt, gridBagConstraints);

        lblAmtBorrwed.setText("Amount Borrowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblAmtBorrwed, gridBagConstraints);

        txtAgency.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAgency.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAgency.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAgency, gridBagConstraints);

        txtAmtSanctioned.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmtSanctioned.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtSanctioned.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAmtSanctioned, gridBagConstraints);

        txtPrinRepFrq.setEnabled(false);
        txtPrinRepFrq.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPrinRepFrq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(txtPrinRepFrq, gridBagConstraints);

        txtIntRepFrq.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntRepFrq.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntRepFrq.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtIntRepFrq, gridBagConstraints);

        txtMorotorium.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMorotorium.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMorotorium.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtMorotorium, gridBagConstraints);

        tdtDateExpiry.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(tdtDateExpiry, gridBagConstraints);

        lblLastRepDate1.setText("Current Balance ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(lblLastRepDate1, gridBagConstraints);

        txtCurrBal.setEnabled(false);
        txtCurrBal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCurrBal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        cDetailPanel.add(txtCurrBal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRepIntCls.add(cDetailPanel, gridBagConstraints);

        panTrans.setMaximumSize(new java.awt.Dimension(800, 215));
        panTrans.setMinimumSize(new java.awt.Dimension(800, 215));
        panTrans.setPreferredSize(new java.awt.Dimension(800, 215));
        panTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        panRepIntCls.add(panTrans, gridBagConstraints);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        cPanel1.setMaximumSize(new java.awt.Dimension(800, 160));
        cPanel1.setMinimumSize(new java.awt.Dimension(800, 160));
        cPanel1.setPreferredSize(new java.awt.Dimension(800, 160));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        txtChargesBalance.setEnabled(false);
        txtChargesBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChargesBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtChargesBalance, gridBagConstraints);

        txtPenalBalance.setEnabled(false);
        txtPenalBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPenalBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtPenalBalance, gridBagConstraints);

        txtIntBalance.setEnabled(false);
        txtIntBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtIntBalance, gridBagConstraints);

        txtPrinBalance.setEnabled(false);
        txtPrinBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPrinBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtPrinBalance, gridBagConstraints);

        lblPrinBalance.setText("Principal Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPrinBalance, gridBagConstraints);

        lblIntBalance.setText("Interest Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblIntBalance, gridBagConstraints);

        lblPenalBalance.setText("Penal Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPenalBalance, gridBagConstraints);

        lblChargesBalance.setText("Charges Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblChargesBalance, gridBagConstraints);

        txtChargesRepaid.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChargesRepaid.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargesRepaid.setNextFocusableComponent(chkClose);
        txtChargesRepaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesRepaidFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtChargesRepaid, gridBagConstraints);

        txtPenalRepaid.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPenalRepaid.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalRepaid.setNextFocusableComponent(txtChargesRepaid);
        txtPenalRepaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalRepaidFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtPenalRepaid, gridBagConstraints);

        txtIntRepaid.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntRepaid.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntRepaid.setNextFocusableComponent(txtPenalRepaid);
        txtIntRepaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntRepaidFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtIntRepaid, gridBagConstraints);

        txtPrinRepaid.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPrinRepaid.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrinRepaid.setNextFocusableComponent(txtIntRepaid);
        txtPrinRepaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrinRepaidFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtPrinRepaid, gridBagConstraints);

        lblPrinRepaid.setText("Principal Repaid ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPrinRepaid, gridBagConstraints);

        lblIntRepaid.setText("Interest Repaid ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblIntRepaid, gridBagConstraints);

        lblPenalRepaid.setText("Penal Repaid ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPenalRepaid, gridBagConstraints);

        lalChargesRepaid.setText("Charges Repaid ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lalChargesRepaid, gridBagConstraints);

        txtPenalPayable.setEnabled(false);
        txtPenalPayable.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPenalPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtPenalPayable, gridBagConstraints);

        txtIntPayable.setEnabled(false);
        txtIntPayable.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtIntPayable, gridBagConstraints);

        tdtIntPaid.setNextFocusableComponent(txtPrinRepaid);
        tdtIntPaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                actionPayablecreate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtIntPaid, gridBagConstraints);

        lblDteIntPaid.setText("Date upto which Interest Paid ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblDteIntPaid, gridBagConstraints);

        lblIntPayable.setText("Interest Payable ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblIntPayable, gridBagConstraints);

        lblPenalPayable.setText("Penal Payable  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPenalPayable, gridBagConstraints);

        panClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panClose.setLayout(null);

        chkClose.setText("Close");
        panClose.add(chkClose);
        chkClose.setBounds(10, 10, 70, 20);

        cLabel1.setText("Closed Date");
        panClose.add(cLabel1);
        cLabel1.setBounds(80, 10, 80, 18);

        tdtClosedDte.setEnabled(false);
        panClose.add(tdtClosedDte);
        tdtClosedDte.setBounds(170, 10, 101, 21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 299;
        gridBagConstraints.ipady = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(panClose, gridBagConstraints);

        lblCheckPaid.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblCheckPaid.setText("Cheque Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 12;
        cPanel1.add(lblCheckPaid, gridBagConstraints);

        txtCheckNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCheckNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCheckNo.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 12;
        cPanel1.add(txtCheckNo, gridBagConstraints);

        lblCheckDate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblCheckDate.setText("Check Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 12;
        cPanel1.add(lblCheckDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 12;
        cPanel1.add(tdtCheckDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panRepIntCls.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMain.add(panRepIntCls, gridBagConstraints);

        getContentPane().add(panMain, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void txtTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTypeActionPerformed

    private void txtChargesRepaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesRepaidFocusLost
        // TODO add your handling code here:
        calculateTotalTransAmt();
        calculateChargesRepaidBalance();
    }//GEN-LAST:event_txtChargesRepaidFocusLost

    private void txtPenalRepaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalRepaidFocusLost
        // TODO add your handling code here:
        calculateTotalTransAmt();
        calculatePenalRepaidBalance();
    }//GEN-LAST:event_txtPenalRepaidFocusLost

    private void txtIntRepaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntRepaidFocusLost
        // TODO add your handling code here:
        calculateTotalTransAmt();
        calculateIntRepaidBalance();
    }//GEN-LAST:event_txtIntRepaidFocusLost

    private void txtPrinRepaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrinRepaidFocusLost
        // TODO add your handling code here:
        double currPrinAmt = CommonUtil.convertObjToDouble(txtPrinRepaid.getText());
        double currBalAmt = CommonUtil.convertObjToDouble(txtCurrBal.getText());
        if (currPrinAmt > currBalAmt) {
            displayAlert("Principal Repaid grater than current balance!!!");
            txtPrinRepaid.setText("0.0");
            return;
        }
        calculateTotalTransAmt();
        calculatePrincipalBalance();
        txtActionEnableCloseedStatus(evt);
    }//GEN-LAST:event_txtPrinRepaidFocusLost
    private void txtActionEnableCloseedStatus(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        enableClosedBrPanel();
    }
    private void actionPayablecreate(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_actionPayablecreate
        // TODO add your handling code here:
        fillIntPenalPayable();
    }//GEN-LAST:event_actionPayablecreate
    private void calculateTotalTransAmt() {
        totalAmt = 0.0;
        totalAmt = CommonUtil.convertObjToDouble(txtPrinRepaid.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtIntRepaid.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtPenalRepaid.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtChargesRepaid.getText()).doubleValue();
        System.out.println("@#%#$%#$%#$%totalAmt : " + totalAmt);
        //Added BY Suresh
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(String.valueOf(totalAmt));
        transactionUI.setCallingTransType("TRANSFER");
    }

    public void calculatePrincipalBalance() {//displayAlert(""+txtPrinRepaid.getText());
        String prinRepaid = txtPrinRepaid.getText();
        if (prinRepaid != null && !prinRepaid.equals("")) {
            if (Double.parseDouble(txtPrinRepaid.getText()) > Double.parseDouble(txtCurrBal.getText())) {
                displayAlert("Principal Repaid is greater than current balance!!!");
                txtPrinRepaid.setText("");
                return;
            }
            double prinBal = Double.parseDouble(txtCurrBal.getText()) - Double.parseDouble(txtPrinRepaid.getText());
            txtPrinBalance.setText(String.valueOf(prinBal));
        }
    }

    public void calculateIntRepaidBalance() {
        String IntRepaid = txtIntRepaid.getText();
        if (IntRepaid != null && !IntRepaid.equals("")) {
            if (Double.parseDouble(txtIntRepaid.getText()) > Double.parseDouble(txtIntPayable.getText())) {
                displayAlert("Interest Repaid is greater than interest payable!!!");
                txtIntRepaid.setText("");
                return;
            }
            double intBal = Double.parseDouble(txtIntPayable.getText()) - Double.parseDouble(txtIntRepaid.getText());
            if (intBal < 0) {
                intBal = 0.0;
            }
            txtIntBalance.setText(String.valueOf(intBal));
        }
    }

    public void calculatePenalRepaidBalance() {
        String PenalRepaid = txtPenalRepaid.getText();
        if (PenalRepaid != null && !PenalRepaid.equals("")) {
            if (Double.parseDouble(txtPenalRepaid.getText()) > Double.parseDouble(txtPenalPayable.getText())) {
                displayAlert("Penal Repaid is greater than penal payable!!!");
                txtPenalRepaid.setText("");
                return;
            }
            double penalBal = Double.parseDouble(txtPenalPayable.getText()) - Double.parseDouble(txtPenalRepaid.getText());
            if (penalBal < 0) {
                penalBal = 0.0;
            }
            txtPenalBalance.setText(String.valueOf(penalBal));
        }
    }

    public void calculateChargesRepaidBalance() {
        String ChargesRepaid = txtChargesRepaid.getText();
        if (ChargesRepaid != null && !ChargesRepaid.equals("")) {
            double chargesBal = 0.0;//Double.parseDouble(txtCurrBal.getText())-Double.parseDouble(txtChargesRepaid.getText());
            txtChargesBalance.setText(String.valueOf(chargesBal));
        }
    }

    //This fn used for enable or disable brclosed panel
    public void enableClosedBrPanel() {
        String principalRepaid = txtPrinRepaid.getText();
        String principalBalance = txtPrinBalance.getText();//txtPrinBalance.getText();
        if (principalBalance != null && !principalBalance.equals("")) {
            //  displayAlert("=="+Double.valueOf(principalBalance).doubleValue());
            if (Double.valueOf(principalBalance).doubleValue() == 0.0) {
                disablepanClose(true);
                chkClose.setSelected(true);
//            Format formatter; formatter = new SimpleDateFormat("MM/dd/yyyy");
//            Date date = new Date();
//            String s = formatter.format(date);
//            tdtClosedDte.setDateValue(getDtPrintValue(s));
                tdtClosedDte.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                tdtClosedDte.setEnabled(false);
                observable.setChkClose(true);
            } else {
                observable.setChkClose(false);
                disablepanClose(false);
                chkClose.setSelected(false);
                tdtClosedDte.setDateValue(null);
                tdtClosedDte.setEnabled(false);
            }
        }
    }

    public void fillIntPenalPayable() {
        try {
            Date lastRepaid = getDateValue(tdtLastRepaidDate.getDateValue());
            Date DtIntPaid = getDateValue(tdtIntPaid.getDateValue());
            if (lastRepaid == null) {
                lastRepaid = getDateValue(tdtDateSanctioned.getDateValue());
            }
            Date today = new Date();
            Date myDate = new Date(today.getYear(), today.getMonth(), today.getDay() - 1);
            if (DtIntPaid != null && DtIntPaid.before(myDate)) {
                displayAlert("Interest paid date is greater than the current date!!!");
                return;
            }
            if (DtIntPaid != null && DtIntPaid.before(lastRepaid)) {
                displayAlert("Interest paid date is greater than the Last repaid date !!!");
                return;
            }
            if (lastRepaid != null && DtIntPaid != null) {
                long days = dateDifference(lastRepaid, DtIntPaid);
                System.out.println("days in DIFFERENCE===" + days);
                BigDecimal dayBig = new BigDecimal(days);
                System.out.println("dayBig in DIFFERENCE===" + dayBig);
                BigDecimal amtborr = new BigDecimal(txtAmtBorrowed.getText());
                System.out.println("amtborr in DIFFERENCE===" + amtborr);
                BigDecimal penalbalance = new BigDecimal(txtPenalBalance.getText());
                System.out.println("penalbalance in DIFFERENCE===" + penalbalance);
                BigDecimal amtIntrate = new BigDecimal(txtRateInterest.getText());
                System.out.println("amtIntrate in DIFFERENCE===" + amtIntrate);
                /* if(amtIntrate.intValue()==0)
                 {
                 amtIntrate=amtIntrate.add(new BigDecimal(1));
                 }*/

                BigDecimal v1 = amtborr.multiply(amtIntrate);
                System.out.println("v1 in DIFFERENCE===" + v1);
                BigDecimal v2 = v1.multiply(dayBig.divide(new BigDecimal(365), 0));
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.USER_ID, "app");
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, "0001");
                singleAuthorizeMap.put("BORROWING_NO", observable.getTxtBorrowingNo());
                List aList = ClientUtil.executeQuery("latestIntPenPayable", singleAuthorizeMap);
                for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(i);
                    if (map.get("INTEREST_PAYABLE") != null) {
                        txtIntPayable.setText(map.get("INTEREST_PAYABLE").toString());
                    }
                    if (map.get("PENAL_PAYABLE") != null) {
                        txtPenalPayable.setText(map.get("PENAL_PAYABLE").toString());
                    }
                }
                if (aList == null) {
                    txtIntPayable.setText(String.valueOf(v2.intValue()));
                }
                //set penal payable
                BigDecimal v3 = penalbalance.multiply(amtIntrate);
                BigDecimal v4 = v3.multiply(dayBig.divide(new BigDecimal(365), 0));
                if (aList == null) {
                    txtPenalPayable.setText(String.valueOf(v4.intValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This fn get long value of the two date difference
    public long dateDifference(Date dt1, Date dt2) {
        long diffDays = 0;
        try {
            // Creates two calendars instances
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            // Set the date for both of the calendar instance
            cal1.set(dt1.getYear(), dt1.getMonth(), dt1.getDay());
            cal2.set(dt2.getYear(), dt2.getMonth(), dt2.getDay());
            // Get the represented date in milliseconds
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            long diff = milis2 - milis1;
            // Calculate difference in days
            diffDays = diff / (24 * 60 * 60 * 1000);
            return diffDays;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;
    }

    //check whether passing date is before current date
    public boolean getBeforeCurrentDate(Date dt1) {
        try {
            if (dt1 != null) {
                Calendar cal = Calendar.getInstance();
                Calendar currentcal = Calendar.getInstance();
                cal.set(dt1.getYear(), dt1.getMonth(), dt1.getDay());
                currentcal.set(currentcal.get(Calendar.YEAR), currentcal.get(Calendar.MONTH), currentcal.get(Calendar.DAY_OF_MONTH));
                if (cal.before(currentcal)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //chk whther the interest paid date is before last repaid date
    public boolean getBeforeLastPaidDate(Date intPaid, Date lastrePaid) {
        try {
            if (intPaid != null && lastrePaid != null) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.set(intPaid.getYear(), intPaid.getMonth(), intPaid.getDay());
                cal2.set(lastrePaid.getYear(), lastrePaid.getMonth(), lastrePaid.getDay());
                if (cal1.before(cal2)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //This fn return sp;ecified date format to convert passing date string
    public String getDtPrintValue(String strDate) {
        try {
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
            //System.out.println("Date is converted from dd/MM/yy format to MM-dd-yyyy hh:mm:ss");
            //System.out.println("Converted date is : " + strDate);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return strDate;
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
//        this.dispose();
        tdtLastRepaidDate.setDateValue("");
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);

        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        removeEditLock(borrowNumber);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false, false, true);
        ClientUtil.enableDisable(panRepIntCls, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnClose.setEnabled(true);
        viewType = "";
        btnVwBr.setEnabled(false);
        ClientUtil.clearAll(this);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        tdtLastRepaidDate.setDateValue("");
        txtIntPayable.setEnabled(false);
        txtPenalPayable.setEnabled(false);
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
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        borrowNumber = "";
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
//        System.out.println("IN btnSaveActionPerformed");
//        savePerformed();
        setModified(false);
        //cheque details
        HashMap map = new HashMap();
        map.put("BORROW_NO", txtBorrowingNo.getText());
        map.put("CHECK_NO", txtCheckNo.getText());
        List checkNo = ClientUtil.executeQuery("getCheckNo", map);
        System.out.println(">>>>checkno>>>>>" + checkNo.size());
        if (checkNo.size() == 0) {
            ClientUtil.displayAlert("Check Number not found");
        }
        //end..
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && totalAmt > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                if (totalAmt > 0) {
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && totalAmt > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if (transactionSize == 0 && totalAmt > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());

                }
            }
        }
        savePerformed();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        removeEditLock(borrowNumber);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        tdtLastRepaidDate.setDateValue("");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        tdtLastRepaidDate.setDateValue("");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panRepIntCls, true);
        ClientUtil.enableDisable(cDetailPanel, false);
        setModified(true);
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnVwBr.setEnabled(true);
        initComponentData();
        txtIntPayable.setEnabled(true);
        txtPenalPayable.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void savePerformed() {

        // System.out.println("IN savePerformed");
        String action = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
        }
        saveAction(action);
    }

    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsCashierAuthorizeList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getBorrowingRepIntClsAuthorizeList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBorrowingRepIntCls");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            //Added By Suresh
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                updateBalalnce();
            }
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            System.out.println("C UTIL CURR DATE ===" + currDt.clone());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("REP_INT_CLS_NO", observable.getRepIntClsNo());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            observable.setAuthMap(singleAuthorizeMap);
            //Added By Suresh
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.execute(authorizeStatus);
            //update int and penal payable
            updateIntPenalPayable();
            viewType = "";
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Borrowing Repayment");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Borrowing Repayment");
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            removeEditLock(observable.getTxtBorrowingNo());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
    }

    public void updateIntPenalPayable() {
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, "app");
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, "0001");
        singleAuthorizeMap.put("BORROWING_NO", txtBorrowingNo.getText());
        singleAuthorizeMap.put("REP_INT_CLS_NO", observable.getRepIntClsNo());
        singleAuthorizeMap.put("INTEREST_BAL", CommonUtil.convertObjToDouble(txtIntBalance.getText()));
        singleAuthorizeMap.put("PENAL_BAL", CommonUtil.convertObjToDouble(txtPenalBalance.getText()));
        singleAuthorizeMap.put("CHARGES_BAL", txtChargesBalance.getText());
        ClientUtil.execute("updateIntPenChargBalance", singleAuthorizeMap);
    }
    private void btnBorrowingAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowingAction
        // TODO add your handling code here:
        callView("BORROWING_DATA");
    }//GEN-LAST:event_btnBorrowingAction
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();

        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            lst.add("REP_INT_CLS_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "BorrowingRepIntCls.getSelectBorrowingRepIntClsList");
        } else {
            lst.add("BORROWING_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "BorrowingRepIntCls.getSelectBorrowingDList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();
        // new ViewAll(this,
    }

    public void update(Observable observed, Object arg) {
        txtBorrowingNo.setText(observable.getTxtBorrowingNo());
        txtAgency.setText(observable.getTxtAgency());
        txtBorrowingRefNo.setText(observable.getTxtBorrowingRefNo());
        txtType.setText(observable.getTxtType());
        txtaDescription.setText(observable.getTxtaDescription());
        txtPrinRepFrq.setText(observable.getTxtPrinRepFrq());
        txtIntRepFrq.setText(observable.getTxtIntRepFrq());
        txtMorotorium.setText(observable.getTxtMorotorium());
//        if (Double.valueOf(observable.getTxtPrinBalance()).doubleValue() == 0.0) {
//               //isablepanClose(true);
//                chkClose.setSelected(true);
//        }
        //hkClose.setSelected(true);
        if (observable.getTxtAmtBorrowed() != null) {
            txtAmtBorrowed.setText(String.valueOf(observable.getTxtAmtBorrowed()));
        }
        if (observable.getTxtRateInterest() != null) {
            txtRateInterest.setText(String.valueOf(observable.getTxtRateInterest()));
        }
        if (observable.getTxtnoofInstall() != null) {
            txtnoofInstall.setText(String.valueOf(observable.getTxtnoofInstall()));
        }

        if (observable.getTxtAmtSanctioned() != null) {
            txtAmtSanctioned.setText(String.valueOf(observable.getTxtAmtSanctioned()));
        }

        if (observable.getTdtDateSanctioned() != null) {
            tdtDateSanctioned.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateSanctioned())));
        }
        if (observable.getTdtDateExpiry() != null) {
            tdtDateExpiry.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateExpiry())));
        }
        if (observable.getTdtLastRepaidDate() != null) {
            //  td.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtLastRepaidDate())));
            tdtLastRepaidDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtLastRepaidDate())));
        }
        if (observable.getTdtIntPaid() != null) {
            //  td.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtLastRepaidDate())));
            tdtIntPaid.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtIntPaid())));
        }
         chkClose.setSelected(observable.isChkClose());
        if (observable.getTdtClosedDte() != null) {
            //  td.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtLastRepaidDate())));
            tdtClosedDte.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtClosedDte())));
        }
        //cheque details
        if (observable.getTdtCheckDate() != null) {
            //  td.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtLastRepaidDate())));
            tdtCheckDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtCheckDate())));
        }
        if (observable.getTxtCheckNo() != null) {
            txtCheckNo.setText(String.valueOf(observable.getTxtCheckNo()));
        }
        //end..

        if (observable.getTxtIntPayable() != null) {
            txtIntPayable.setText(String.valueOf(observable.getTxtIntPayable()));
        }

//        if (observable.isChkClose() == true) {
//            chkClose.setText(String.valueOf(observable.isChkClose()));
//        }
        if (observable.getTxtPenalPayable() != null) {
            txtPenalPayable.setText(String.valueOf(observable.getTxtPenalPayable()));
        }
        if (observable.getTxtPrinRepaid() != null) {
            txtPrinRepaid.setText(String.valueOf(observable.getTxtPrinRepaid()));
        }
        if (observable.getTxtPrinRepaid() != null) {
            txtPrinRepaid.setText(String.valueOf(observable.getTxtPrinRepaid()));
        }
        if (observable.getTxtPenalRepaid() != null) {
            txtPenalRepaid.setText(String.valueOf(observable.getTxtPenalRepaid()));
        }
        if (observable.getTxtChargesRepaid() != null) {
            txtChargesRepaid.setText(String.valueOf(observable.getTxtChargesRepaid()));
        }
        if (observable.getTxtPrinBalance() != null) {
            txtPrinBalance.setText(String.valueOf(observable.getTxtPrinBalance()));
        }
        if (observable.getTxtIntBalance() != null) {
            txtIntBalance.setText(String.valueOf(observable.getTxtIntBalance()));
        }
        if (observable.getTxtPenalBalance() != null) {
            txtPenalBalance.setText(String.valueOf(observable.getTxtPenalBalance()));
        }
        if (observable.getTxtChargesBalance() != null) {
            txtChargesBalance.setText(String.valueOf(observable.getTxtChargesBalance()));
        }
    }

    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
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
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (viewType != null) {
            if (viewType.equals("BORROWING_DATA") || viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                if (viewType.equals("BORROWING_DATA")) {
                    where.put("BORROWING_NO", hash.get("BORROWING_NO"));
                } else {
                    this.setButtonEnableDisable();
                    where.put("BORROWING_NO", hash.get("BORROWING_NO"));
                    where.put("REP_INT_CLS_NO", hash.get("REP_INT_CLS_NO"));
                }
                hash.put(CommonConstants.MAP_WHERE, where);
                borrowNumber = CommonUtil.convertObjToStr(hash.get("BORROWING_NO"));
                observable.populateData(hash);
            }

            if (viewType.equals(AUTHORIZE)) {
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
            }
        }
        fillCurrBal();
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
    }

    public void fillCurrBal() {
        System.out.println("totD 1=== " + observable.getTxtTotD() + "totR ==11=" + observable.getTxtTotR());
        double totD = 0, totR = 0, tot = 0;
        if (observable.getTxtTotD() != null && !observable.getTxtTotD().equals("")) {
            totD = observable.getTxtTotD().doubleValue();
        } else {
            totD = 0.0;
        }
        if (observable.getTxtTotR() != null && !observable.getTxtTotR().equals("")) {
            totR = observable.getTxtTotR().doubleValue();
        } else {
            totR = 0.0;
        }
        System.out.println("totD === " + totD + "totR ===" + totR+ " CURR_AMT = "+tot);
        tot = totD - totR;
        if (tot < 1) {
            tot = 0;
        }
        txtCurrBal.setText(String.valueOf(tot));
    }

    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBorrowingRefNo", new Boolean(true));

    }

    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
        // return
    }

    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed

        final String mandatoryMessage = checkMandatory(panRepIntCls);
        StringBuffer message = new StringBuffer(mandatoryMessage);

        if (txtBorrowingRefNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtBorrowingRefNo"));
        }
        if (txtPrinRepaid.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtPrinRepaid"));
        }

        //setExpDateOnCalculation();
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            updateOBFields();

            observable.execute(status);
            //Commented By Suresh
//            updateBalalnce(status);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("REP_INT_CLS_NO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    displayTransDetail(observable.getProxyReturnMap());
                }
                if (status == CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("REP_INT_CLS_NO", observable.getRepIntClsNo());
                }
                settings();
            }
        }
    }

    public void updateBalalnce() {
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("BORROWING_NO", observable.getTxtBorrowingNo());
        Double avalBal = observable.getTxtCurrBal();
        BigDecimal avB = new BigDecimal(String.valueOf(avalBal));
        BigDecimal pB = new BigDecimal(txtPrinRepaid.getText());
        BigDecimal newVal = avB.add(pB);
        singleAuthorizeMap.put("AVAL_BALANCE", newVal);
        singleAuthorizeMap.put("CLEAR_BALANCE", pB);
        if (observable.getTxtType().equals("Cash Credit")) {
            ClientUtil.execute("updateTotalAndClearBalance", singleAuthorizeMap); //CC Type
        } else if (observable.getTxtType().equals("Loan Simple Interest") || observable.getTxtType().equals("Loan Compound interest")) {
            ClientUtil.execute("updateClearBalanceForLoan", singleAuthorizeMap);    //Loan Type
        }
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {

        // return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void settings() {
        btnCancelActionPerformed(null);
        observable.setResultStatus();
    }

    public Date getDateValue(String date1) {
        //        DateFormat formatter ;
        Date date = null;
        try {
            if (date1 != null && !date1.equalsIgnoreCase("")) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                date = new Date(sdf2.format(sdf1.parse(date1)));
            }
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {

        observable.setTdtCheckDate(getDateValue(tdtCheckDate.getDateValue()));
        observable.setTxtCheckNo(txtCheckNo.getText());
        observable.setTxtBorrowingNo(txtBorrowingNo.getText());
        observable.setTxtAgency(txtAgency.getText());
        observable.setTxtBorrowingRefNo(txtBorrowingRefNo.getText());
        observable.setTxtType(txtType.getText());
        observable.setTxtaDescription(txtaDescription.getText());
        observable.setTxtRateInterest(Double.valueOf(txtRateInterest.getText()));
        observable.setTxtnoofInstall(Double.valueOf(txtnoofInstall.getText()));
        observable.setTxtPrinRepFrq(txtPrinRepFrq.getText());
        observable.setTxtIntRepFrq(txtIntRepFrq.getText());
        observable.setTxtMorotorium(txtMorotorium.getText());

        observable.setTdtDateSanctioned(getDateValue(tdtDateSanctioned.getDateValue()));
        observable.setTdtDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        // observable.setDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        observable.setTxtAmtSanctioned(Double.valueOf(txtAmtSanctioned.getText()));
        observable.setTxtAmtBorrowed(Double.valueOf(txtAmtBorrowed.getText()));
        observable.setTdtLastRepaidDate(getDateValue(tdtLastRepaidDate.getDateValue()));
        observable.setTdtIntPaid(getDateValue(tdtIntPaid.getDateValue()));
       
        observable.setTdtClosedDte(getDateValue(tdtClosedDte.getDateValue()));
        String txtIntPay = txtIntPayable.getText();
        if (txtIntPay != null && !txtIntPay.equalsIgnoreCase("")) {
            observable.setTxtIntPayable(Double.valueOf(txtIntPayable.getText()));
        }

        String txtPenalPay = txtPenalPayable.getText();
        if (txtPenalPay != null && !txtPenalPay.equalsIgnoreCase("")) {
            observable.setTxtPenalPayable(CommonUtil.convertObjToDouble(txtPenalPayable.getText()));
        }
        observable.setTdtClosedDte(getDateValue(tdtClosedDte.getDateValue()));
        observable.setTxtPrinRepaid(CommonUtil.convertObjToDouble(txtPrinRepaid.getText()));
        observable.setTxtIntRepaid(CommonUtil.convertObjToDouble(txtIntRepaid.getText()));
        observable.setTxtPenalRepaid(CommonUtil.convertObjToDouble(txtPenalRepaid.getText()));
        observable.setTxtChargesRepaid(CommonUtil.convertObjToDouble(txtChargesRepaid.getText()));
        String txtPrinbal = txtPrinBalance.getText();
        if (txtPrinbal != null && !txtPrinbal.equalsIgnoreCase("")) {
            observable.setTxtPrinBalance(CommonUtil.convertObjToDouble(txtPrinBalance.getText()));
        }

        String txtIntBal = txtIntBalance.getText();
        if (txtIntBal != null && !txtIntBal.equalsIgnoreCase("")) {
            observable.setTxtIntBalance(CommonUtil.convertObjToDouble(txtIntBalance.getText()));
        }

        String txtPenalBal = txtPenalBalance.getText();
        if (txtPenalBal != null && !txtPenalBal.equalsIgnoreCase("")) {
            observable.setTxtPenalBalance(CommonUtil.convertObjToDouble(txtPenalBalance.getText()));
        }

        String txtChargBal = txtChargesBalance.getText();
        if (txtChargBal != null && !txtChargBal.equalsIgnoreCase("")) {
            observable.setTxtChargesBalance(CommonUtil.convertObjToDouble(txtChargesBalance.getText()));
        }

        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnVwBr;
    private com.see.truetransact.uicomponent.CPanel cDetailPanel;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CCheckBox chkClose;
    private com.see.truetransact.uicomponent.CLabel lalChargesRepaid;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAgency;
    private com.see.truetransact.uicomponent.CLabel lblAmtBorrwed;
    private com.see.truetransact.uicomponent.CLabel lblBrNo;
    private com.see.truetransact.uicomponent.CLabel lblBrRefNo;
    private com.see.truetransact.uicomponent.CLabel lblChargesBalance;
    private javax.swing.JLabel lblCheckDate;
    private javax.swing.JLabel lblCheckPaid;
    private com.see.truetransact.uicomponent.CLabel lblDes;
    private com.see.truetransact.uicomponent.CLabel lblDteIntPaid;
    private com.see.truetransact.uicomponent.CLabel lblIntBalance;
    private com.see.truetransact.uicomponent.CLabel lblIntPayable;
    private com.see.truetransact.uicomponent.CLabel lblIntRep;
    private com.see.truetransact.uicomponent.CLabel lblIntRepaid;
    private com.see.truetransact.uicomponent.CLabel lblLastRepDate;
    private com.see.truetransact.uicomponent.CLabel lblLastRepDate1;
    private com.see.truetransact.uicomponent.CLabel lblMorotorium;
    private com.see.truetransact.uicomponent.CLabel lblNoInstall;
    private com.see.truetransact.uicomponent.CLabel lblPenalBalance;
    private com.see.truetransact.uicomponent.CLabel lblPenalPayable;
    private com.see.truetransact.uicomponent.CLabel lblPenalRepaid;
    private com.see.truetransact.uicomponent.CLabel lblPrinBalance;
    private com.see.truetransact.uicomponent.CLabel lblPrinRep;
    private com.see.truetransact.uicomponent.CLabel lblPrinRepaid;
    private com.see.truetransact.uicomponent.CLabel lblRateInt;
    private com.see.truetransact.uicomponent.CLabel lblSanAmt;
    private com.see.truetransact.uicomponent.CLabel lblSanDate;
    private com.see.truetransact.uicomponent.CLabel lblSanExpDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CPanel panClose;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panRepIntCls;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtCheckDate;
    private com.see.truetransact.uicomponent.CDateField tdtClosedDte;
    private com.see.truetransact.uicomponent.CDateField tdtDateExpiry;
    private com.see.truetransact.uicomponent.CDateField tdtDateSanctioned;
    private com.see.truetransact.uicomponent.CDateField tdtIntPaid;
    private com.see.truetransact.uicomponent.CDateField tdtLastRepaidDate;
    private com.see.truetransact.uicomponent.CTextField txtAgency;
    private com.see.truetransact.uicomponent.CTextField txtAmtBorrowed;
    private com.see.truetransact.uicomponent.CTextField txtAmtSanctioned;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingNo;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingRefNo;
    private com.see.truetransact.uicomponent.CTextField txtChargesBalance;
    private com.see.truetransact.uicomponent.CTextField txtChargesRepaid;
    private javax.swing.JTextField txtCheckNo;
    private com.see.truetransact.uicomponent.CTextField txtCurrBal;
    private com.see.truetransact.uicomponent.CTextField txtIntBalance;
    private com.see.truetransact.uicomponent.CTextField txtIntPayable;
    private com.see.truetransact.uicomponent.CTextField txtIntRepFrq;
    private com.see.truetransact.uicomponent.CTextField txtIntRepaid;
    private com.see.truetransact.uicomponent.CTextField txtMorotorium;
    private com.see.truetransact.uicomponent.CTextField txtPenalBalance;
    private com.see.truetransact.uicomponent.CTextField txtPenalPayable;
    private com.see.truetransact.uicomponent.CTextField txtPenalRepaid;
    private com.see.truetransact.uicomponent.CTextField txtPrinBalance;
    private com.see.truetransact.uicomponent.CTextField txtPrinRepFrq;
    private com.see.truetransact.uicomponent.CTextField txtPrinRepaid;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtType;
    private com.see.truetransact.uicomponent.CTextArea txtaDescription;
    private com.see.truetransact.uicomponent.CTextField txtnoofInstall;
    // End of variables declaration//GEN-END:variables
    private com.see.truetransact.clientutil.TableModel tbModel;
}
