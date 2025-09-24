/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanRepaymentUI.java
 *
 * Created on June 17, 2013, 3:55 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.Map;
import java.util.Date;
import java.util.List;
import javax.swing.table.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.TextUI;

/*
 *
 * @author Suresh R
 *
 */
public class LoanRepaymentUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    TransactionUI transactionUI = new TransactionUI();
    String viewType = "";
    private boolean btnNewPressed;
    private boolean isFilled;
    private String CUSTOMER = "CUSTOMER";
    private String SANCTION_NO = "SANCTION_NO";
    final String AUTHORIZE = "Authorize";
    private Date currDt;
    private List finalList = null;
    private final LoanRepaymentOB observable;
    HashMap mandatoryMap = null;
    private boolean tableSelectOption = false;
    ArrayList colourList = new ArrayList();
    public int column = -1;

    /**
     * Creates new form TermLoanUI
     */
    public LoanRepaymentUI() {
        initComponents();
        observable = new LoanRepaymentOB();
        setMaxLengths();
        setButtonEnableDisable();
        lblKCCAccName.setText("");
        lblCustomerName.setText("");
        btnCustID.setEnabled(false);
        btnDisplay.setEnabled(false);
        btnSanctionNo.setEnabled(false);
        btnViewDetails.setEnabled(false);
        btnAppropriate.setEnabled(false);
        btnMembershipLia.setVisible(false);
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(this, false);
        lblTotalOutstandingAmtVal.setText("");
        panTransactionDetails.add(transactionUI);
        transactionUI.setSourceScreen("LOAN_REPAYMENT");
        ClientUtil.enableDisable(panTotalNetAmount, false);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        tblFinYearWise.setModel(observable.getTblFinYearWise());
        tblReleaseDetails.setModel(observable.getTblReleaseDetails());
        tblAppropriated.setModel(observable.getTblAppropriated());
        setSizeTableData();
    }

    private void setSizeTableData() {
        tblReleaseDetails.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblReleaseDetails.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblReleaseDetails.getColumnModel().getColumn(2).setPreferredWidth(23);
        tblReleaseDetails.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblReleaseDetails.getColumnModel().getColumn(4).setPreferredWidth(50);
        tblReleaseDetails.getColumnModel().getColumn(5).setPreferredWidth(50);
        tblReleaseDetails.getColumnModel().getColumn(6).setPreferredWidth(50);
        tblReleaseDetails.getColumnModel().getColumn(7).setPreferredWidth(45);
        tblReleaseDetails.getColumnModel().getColumn(8).setPreferredWidth(40);
        tblReleaseDetails.getColumnModel().getColumn(9).setPreferredWidth(40);
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    /**
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void update(Observable observed, Object arg) {
    }

    private void setMaxLengths() {
        txtRepaymentAmt.setValidation(new CurrencyValidation(14, 2));
        txtTotalClearBalanceAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalPrincipalAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalIntDueAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalIntAfterDtAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalPenalAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalChargeAmount.setValidation(new CurrencyValidation(14, 2));
    }

    private void setSizeTableDataEditMode() {
        tblReleaseDetails.getColumnModel().getColumn(0).setPreferredWidth(83);
        tblReleaseDetails.getColumnModel().getColumn(1).setPreferredWidth(25);
        tblReleaseDetails.getColumnModel().getColumn(2).setPreferredWidth(55);
        tblReleaseDetails.getColumnModel().getColumn(3).setPreferredWidth(55);
        tblReleaseDetails.getColumnModel().getColumn(4).setPreferredWidth(55);
        tblReleaseDetails.getColumnModel().getColumn(5).setPreferredWidth(55);
        tblReleaseDetails.getColumnModel().getColumn(6).setPreferredWidth(35);
        tblReleaseDetails.getColumnModel().getColumn(7).setPreferredWidth(35);
    }

    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdTypeActionPerformed
    private void cboPLINameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void tdtAsOn_GDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOn_GDFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtAsOn_GDFocusLost

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccLimit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNatureInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRiskWeight = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiDisburseAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDoAddSIs = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDatedCheque = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatus_Repayment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExecuted_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGuarnConstution = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSHG = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRenewalGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoEnhanceGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRebateInterestGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidyAddMinusGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDirectRepaymentGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCourtOrderGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabRepayment = new com.see.truetransact.uicomponent.CTabbedPane();
        panRepaymentDetails = new com.see.truetransact.uicomponent.CPanel();
        panRepaymentDet = new com.see.truetransact.uicomponent.CPanel();
        lblSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        btnMembershipLia = new com.see.truetransact.uicomponent.CButton();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtPaymentDt = new com.see.truetransact.uicomponent.CDateField();
        lblKCCAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtKCCAccNo = new com.see.truetransact.uicomponent.CTextField();
        lblKCCAccName = new com.see.truetransact.uicomponent.CLabel();
        txtNCLSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        btnSanctionNo = new com.see.truetransact.uicomponent.CButton();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoReleaseAll = new com.see.truetransact.uicomponent.CRadioButton();
        rdoReleaseOverDue = new com.see.truetransact.uicomponent.CRadioButton();
        lblSanctionAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRepaymentAmt = new com.see.truetransact.uicomponent.CTextField();
        btnViewDetails = new com.see.truetransact.uicomponent.CButton();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        srpTable_FinYearWise = new com.see.truetransact.uicomponent.CScrollPane();
        tblFinYearWise = new com.see.truetransact.uicomponent.CTable();
        lblTotalOutstandingAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalOutstandingAmt = new com.see.truetransact.uicomponent.CLabel();
        panRepaymentTbl = new com.see.truetransact.uicomponent.CPanel();
        srpAppropriated = new com.see.truetransact.uicomponent.CScrollPane();
        tblAppropriated = new com.see.truetransact.uicomponent.CTable();
        btnAppropriate = new com.see.truetransact.uicomponent.CButton();
        srpTable_ReleaseDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblReleaseDetails = new com.see.truetransact.uicomponent.CTable();
        panTotalNetAmount = new com.see.truetransact.uicomponent.CPanel();
        txtTotalChargeAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtTotalPenalAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalIntAfterDtAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalIntDueAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalPrincipalAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalClearBalanceAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalReleaseAdjustment = new com.see.truetransact.uicomponent.CLabel();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        lblSelectedTotal = new com.see.truetransact.uicomponent.CLabel();
        lblAppTotal = new com.see.truetransact.uicomponent.CLabel();
        panTotalSelectedRecord = new com.see.truetransact.uicomponent.CPanel();
        lblTotalRelease = new com.see.truetransact.uicomponent.CLabel();
        lblTotalReleaseVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalSelectedReleaseVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRelease2 = new com.see.truetransact.uicomponent.CLabel();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        mbrTermLoan = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Repayment");
        setMinimumSize(new java.awt.Dimension(860, 663));
        setPreferredSize(new java.awt.Dimension(860, 663));

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(345, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        getContentPane().add(tbrTermLoan, java.awt.BorderLayout.NORTH);

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

        panTermLoan.setMinimumSize(new java.awt.Dimension(850, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(850, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        tabRepayment.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabRepayment.setMinimumSize(new java.awt.Dimension(850, 650));
        tabRepayment.setPreferredSize(new java.awt.Dimension(850, 650));

        panRepaymentDetails.setMinimumSize(new java.awt.Dimension(850, 660));
        panRepaymentDetails.setPreferredSize(new java.awt.Dimension(850, 660));
        panRepaymentDetails.setLayout(new java.awt.GridBagLayout());

        panRepaymentDet.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRepaymentDet.setMinimumSize(new java.awt.Dimension(845, 160));
        panRepaymentDet.setPreferredSize(new java.awt.Dimension(845, 160));
        panRepaymentDet.setLayout(new java.awt.GridBagLayout());

        lblSanctionNo.setText("NCL Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRepaymentDet.add(lblSanctionNo, gridBagConstraints);

        btnMembershipLia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/members2.jpg"))); // NOI18N
        btnMembershipLia.setToolTipText("View MemberShip Liability");
        btnMembershipLia.setMaximumSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.setMinimumSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipLiaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRepaymentDet.add(btnMembershipLia, gridBagConstraints);

        lblCustID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 4);
        panRepaymentDet.add(lblCustID, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        panRepaymentDet.add(txtCustID, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setToolTipText("Select Customer");
        btnCustID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustID.setMaximumSize(new java.awt.Dimension(22, 21));
        btnCustID.setMinimumSize(new java.awt.Dimension(22, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(22, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRepaymentDet.add(btnCustID, gridBagConstraints);

        lblCustomerName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerName.setText("Customer Name");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRepaymentDet.add(lblCustomerName, gridBagConstraints);

        lblSanctionDt.setText("Date of Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panRepaymentDet.add(lblSanctionDt, gridBagConstraints);

        tdtPaymentDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPaymentDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panRepaymentDet.add(tdtPaymentDt, gridBagConstraints);

        lblKCCAccNo.setText("KCC Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRepaymentDet.add(lblKCCAccNo, gridBagConstraints);

        txtKCCAccNo.setMinimumSize(new java.awt.Dimension(130, 21));
        txtKCCAccNo.setPreferredSize(new java.awt.Dimension(130, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panRepaymentDet.add(txtKCCAccNo, gridBagConstraints);

        lblKCCAccName.setForeground(new java.awt.Color(0, 51, 204));
        lblKCCAccName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKCCAccName.setText("Account Name");
        lblKCCAccName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblKCCAccName.setMaximumSize(new java.awt.Dimension(270, 18));
        lblKCCAccName.setMinimumSize(new java.awt.Dimension(270, 18));
        lblKCCAccName.setPreferredSize(new java.awt.Dimension(270, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRepaymentDet.add(lblKCCAccName, gridBagConstraints);

        txtNCLSanctionNo.setMinimumSize(new java.awt.Dimension(130, 21));
        txtNCLSanctionNo.setPreferredSize(new java.awt.Dimension(130, 21));
        txtNCLSanctionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNCLSanctionNoActionPerformed(evt);
            }
        });
        txtNCLSanctionNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNCLSanctionNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        panRepaymentDet.add(txtNCLSanctionNo, gridBagConstraints);

        btnSanctionNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSanctionNo.setToolTipText("Select Customer");
        btnSanctionNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSanctionNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnSanctionNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnSanctionNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnSanctionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSanctionNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRepaymentDet.add(btnSanctionNo, gridBagConstraints);

        panGender.setMinimumSize(new java.awt.Dimension(220, 18));
        panGender.setName("panGender");
        panGender.setPreferredSize(new java.awt.Dimension(220, 18));
        panGender.setLayout(new java.awt.GridBagLayout());

        rdoReleaseAll.setText("All");
        rdoReleaseAll.setMaximumSize(new java.awt.Dimension(40, 18));
        rdoReleaseAll.setMinimumSize(new java.awt.Dimension(40, 18));
        rdoReleaseAll.setName("rdoGender_Male");
        rdoReleaseAll.setPreferredSize(new java.awt.Dimension(40, 18));
        rdoReleaseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReleaseAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panGender.add(rdoReleaseAll, gridBagConstraints);

        rdoReleaseOverDue.setText("Only Over Due Releases");
        rdoReleaseOverDue.setMaximumSize(new java.awt.Dimension(170, 18));
        rdoReleaseOverDue.setMinimumSize(new java.awt.Dimension(170, 18));
        rdoReleaseOverDue.setName("rdoGender_Female");
        rdoReleaseOverDue.setPreferredSize(new java.awt.Dimension(170, 18));
        rdoReleaseOverDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReleaseOverDueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panGender.add(rdoReleaseOverDue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panRepaymentDet.add(panGender, gridBagConstraints);

        lblSanctionAmt.setText("Amount of Repayment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panRepaymentDet.add(lblSanctionAmt, gridBagConstraints);

        txtRepaymentAmt.setMaximumSize(new java.awt.Dimension(130, 21));
        txtRepaymentAmt.setMinimumSize(new java.awt.Dimension(130, 21));
        txtRepaymentAmt.setPreferredSize(new java.awt.Dimension(130, 21));
        txtRepaymentAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRepaymentAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panRepaymentDet.add(txtRepaymentAmt, gridBagConstraints);

        btnViewDetails.setForeground(new java.awt.Color(0, 102, 51));
        btnViewDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Down_Arrow.gif"))); // NOI18N
        btnViewDetails.setText("View Details");
        btnViewDetails.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnViewDetails.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 4);
        panRepaymentDet.add(btnViewDetails, gridBagConstraints);

        btnDisplay.setForeground(new java.awt.Color(51, 0, 51));
        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Right_Arrow.gif"))); // NOI18N
        btnDisplay.setText("Display");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentDet.add(btnDisplay, gridBagConstraints);

        tblFinYearWise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Financial Year", "Total Outstanding", "Over Due Amount"
            }
        ));
        tblFinYearWise.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblFinYearWise.setSelectionForeground(new java.awt.Color(10, 36, 106));
        tblFinYearWise.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFinYearWiseMousePressed(evt);
            }
        });
        srpTable_FinYearWise.setViewportView(tblFinYearWise);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRepaymentDet.add(srpTable_FinYearWise, gridBagConstraints);

        lblTotalOutstandingAmtVal.setForeground(new java.awt.Color(0, 51, 51));
        lblTotalOutstandingAmtVal.setText("Amount");
        lblTotalOutstandingAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRepaymentDet.add(lblTotalOutstandingAmtVal, gridBagConstraints);

        lblTotalOutstandingAmt.setForeground(new java.awt.Color(0, 51, 51));
        lblTotalOutstandingAmt.setText("Total Outstanding : Rs");
        lblTotalOutstandingAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRepaymentDet.add(lblTotalOutstandingAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panRepaymentDetails.add(panRepaymentDet, gridBagConstraints);

        panRepaymentTbl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRepaymentTbl.setMinimumSize(new java.awt.Dimension(815, 380));
        panRepaymentTbl.setPreferredSize(new java.awt.Dimension(815, 380));
        panRepaymentTbl.setLayout(new java.awt.GridBagLayout());

        srpAppropriated.setMaximumSize(new java.awt.Dimension(640, 55));
        srpAppropriated.setMinimumSize(new java.awt.Dimension(640, 55));
        srpAppropriated.setPreferredSize(new java.awt.Dimension(640, 55));

        tblAppropriated.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Total", "O/S Balance", "OverDueAmount", "Int to Pay upto dt", "Int to Pay after due dt", "Penal Int to Pay", "Charges to Pay"
            }
        ));
        tblAppropriated.setMinimumSize(new java.awt.Dimension(800, 32));
        tblAppropriated.setPreferredScrollableViewportSize(new java.awt.Dimension(800, 161));
        tblAppropriated.setPreferredSize(new java.awt.Dimension(800, 32));
        srpAppropriated.setViewportView(tblAppropriated);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panRepaymentTbl.add(srpAppropriated, gridBagConstraints);

        btnAppropriate.setForeground(new java.awt.Color(0, 51, 51));
        btnAppropriate.setText("Appropriate");
        btnAppropriate.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnAppropriate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAppropriateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentTbl.add(btnAppropriate, gridBagConstraints);

        tblReleaseDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Release No", "Due Date", "O/S Balance", "OverDue Amount", "Int upto due dt", "Int after due dt", "Penal Int to Pay", "Charges to Pay", "Released Amt"
            }
        ));
        tblReleaseDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblReleaseDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblReleaseDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblReleaseDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReleaseDetailsMouseClicked(evt);
            }
        });
        srpTable_ReleaseDetails.setViewportView(tblReleaseDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRepaymentTbl.add(srpTable_ReleaseDetails, gridBagConstraints);

        panTotalNetAmount.setMinimumSize(new java.awt.Dimension(705, 38));
        panTotalNetAmount.setPreferredSize(new java.awt.Dimension(705, 38));
        panTotalNetAmount.setLayout(new java.awt.GridBagLayout());

        txtTotalChargeAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalChargeAmount.setMinimumSize(new java.awt.Dimension(65, 21));
        txtTotalChargeAmount.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalChargeAmount, gridBagConstraints);

        lblTotalPayment.setForeground(new java.awt.Color(102, 102, 255));
        lblTotalPayment.setText("Grand Total : ");
        lblTotalPayment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 5, 0);
        panTotalNetAmount.add(lblTotalPayment, gridBagConstraints);

        txtTotalPenalAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPenalAmount.setMinimumSize(new java.awt.Dimension(65, 21));
        txtTotalPenalAmount.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalPenalAmount, gridBagConstraints);

        txtTotalIntAfterDtAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalIntAfterDtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalIntAfterDtAmount, gridBagConstraints);

        txtTotalIntDueAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalIntDueAmount.setMinimumSize(new java.awt.Dimension(105, 21));
        txtTotalIntDueAmount.setPreferredSize(new java.awt.Dimension(105, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalIntDueAmount, gridBagConstraints);

        txtTotalPrincipalAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPrincipalAmount.setMinimumSize(new java.awt.Dimension(105, 21));
        txtTotalPrincipalAmount.setPreferredSize(new java.awt.Dimension(105, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalPrincipalAmount, gridBagConstraints);

        txtTotalClearBalanceAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalClearBalanceAmount.setMinimumSize(new java.awt.Dimension(105, 21));
        txtTotalClearBalanceAmount.setPreferredSize(new java.awt.Dimension(105, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panTotalNetAmount.add(txtTotalClearBalanceAmount, gridBagConstraints);

        lblTotalReleaseAdjustment.setText(" ");
        lblTotalReleaseAdjustment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalReleaseAdjustment.setMaximumSize(new java.awt.Dimension(50, 18));
        lblTotalReleaseAdjustment.setMinimumSize(new java.awt.Dimension(50, 18));
        lblTotalReleaseAdjustment.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotalNetAmount.add(lblTotalReleaseAdjustment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panRepaymentTbl.add(panTotalNetAmount, gridBagConstraints);

        panTotal.setMinimumSize(new java.awt.Dimension(82, 45));
        panTotal.setPreferredSize(new java.awt.Dimension(82, 45));
        panTotal.setLayout(new java.awt.GridBagLayout());

        lblSelectedTotal.setText("Sel. Total : ");
        lblSelectedTotal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(lblSelectedTotal, gridBagConstraints);

        lblAppTotal.setForeground(new java.awt.Color(255, 0, 0));
        lblAppTotal.setText("Appr. Total : ");
        lblAppTotal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(lblAppTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        panRepaymentTbl.add(panTotal, gridBagConstraints);

        panTotalSelectedRecord.setMinimumSize(new java.awt.Dimension(125, 60));
        panTotalSelectedRecord.setPreferredSize(new java.awt.Dimension(125, 60));
        panTotalSelectedRecord.setLayout(new java.awt.GridBagLayout());

        lblTotalRelease.setText("Selected Rel : ");
        lblTotalRelease.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTotalSelectedRecord.add(lblTotalRelease, gridBagConstraints);

        lblTotalReleaseVal.setText(" ");
        lblTotalReleaseVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalReleaseVal.setMaximumSize(new java.awt.Dimension(25, 18));
        lblTotalReleaseVal.setMinimumSize(new java.awt.Dimension(25, 18));
        lblTotalReleaseVal.setPreferredSize(new java.awt.Dimension(25, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        panTotalSelectedRecord.add(lblTotalReleaseVal, gridBagConstraints);

        lblTotalSelectedReleaseVal.setText(" ");
        lblTotalSelectedReleaseVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalSelectedReleaseVal.setMaximumSize(new java.awt.Dimension(25, 18));
        lblTotalSelectedReleaseVal.setMinimumSize(new java.awt.Dimension(25, 18));
        lblTotalSelectedReleaseVal.setPreferredSize(new java.awt.Dimension(25, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panTotalSelectedRecord.add(lblTotalSelectedReleaseVal, gridBagConstraints);

        lblTotalRelease2.setText("Total Rel : ");
        lblTotalRelease2.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 5, 0);
        panTotalSelectedRecord.add(lblTotalRelease2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panRepaymentTbl.add(panTotalSelectedRecord, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panRepaymentDetails.add(panRepaymentTbl, gridBagConstraints);

        tabRepayment.addTab("Repayment Details", panRepaymentDetails);

        panTransactionDetails.setMinimumSize(new java.awt.Dimension(500, 282));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(500, 282));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());
        tabRepayment.addTab("Transaction Details", panTransactionDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTermLoan.add(tabRepayment, gridBagConstraints);

        getContentPane().add(panTermLoan, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.setEnabled(false);
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.setEnabled(false);
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTermLoan.add(mnuProcess);

        setJMenuBar(mbrTermLoan);

        pack();
    }// </editor-fold>//GEN-END:initComponents
            private void btnMembershipLiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipLiaActionPerformed
    }//GEN-LAST:event_btnMembershipLiaActionPerformed

            private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
                // TODO add your handling code here:
                if (txtCustID.getText().length() > 0) {
                    viewType = CUSTOMER;
                    txtCustIDActionPerform();
                }
    }//GEN-LAST:event_txtCustIDFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIDActionPerformed
    }//GEN-LAST:event_txtCustIDActionPerformed

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getLoanRepaymentEnquiry");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getLoanRepaymentDelete");
        } else if (currAction.equals(CUSTOMER)) {
            HashMap map = new HashMap();
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLMasterCustID");
        } else if (currAction.equals(SANCTION_NO)) {
            HashMap map = new HashMap();
            map.put("CUST_ID", txtCustID.getText());
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLMasterCustID");
        }
        new ViewAll(this, viewMap).show();
    }

    private void txtCustIDActionPerform() {
        // TODO add your handling code here:
        String cust_id = CommonUtil.convertObjToStr(txtCustID.getText());
        List lst = null;
        HashMap executeMap = new HashMap();
        HashMap custMap = new HashMap();
        if (cust_id.length() > 0) {
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            executeMap.put("CUST_ID", new String(cust_id));
            executeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            lst = ClientUtil.executeQuery("getNCLMasterCustID", executeMap);
            if (lst != null && lst.size() > 0) {
                executeMap = (HashMap) lst.get(0);
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Customer Number");
                txtCustID.setText("");
                lblCustomerName.setText("");
            }
        }
    }

    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        System.out.println("calling filldata#####" + hash);
        if (hash.containsKey("CUST_ID")) {
            hash.put("CUSTOMER ID", hash.get("CUST_ID"));
        }
        if (viewType == CUSTOMER) {
            txtCustID.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            displayCustomerName();
            txtNCLSanctionNo.setText("");
            txtKCCAccNo.setText("");
            lblKCCAccName.setText("");
        } else if (viewType.equalsIgnoreCase(SANCTION_NO)) {
            txtNCLSanctionNo.setText(CommonUtil.convertObjToStr(hash.get("NCL_SANCTION_NO")));
            displayNCLSanctionDetails();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            this.setButtonEnableDisable();
            ClientUtil.enableDisable(this, false);
            observable.setLoanRepaymentNo(CommonUtil.convertObjToStr(hash.get("LOAN_REPAY_NO")));
            observable.getData(hash);
            tblReleaseDetails.setModel(observable.getTblReleaseDetails());
            panTotalSelectedRecord.setVisible(false);
            lblTotalReleaseAdjustment.setVisible(false);
            setColorListEditMode();
            setColour();
            setSizeTableDataEditMode();
            calcTotalEditMode();
            calcTableTotalAmountEditMode();
            update();
            displayNCLSanctionDetails();
            displayCustomerName();
            btnDisplayActionPerformed(null);
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST") || observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
            //Set Tooltip Text
            if (txtRepaymentAmt.getText().length() > 0) {
                double repaymentAmt = 0;
                repaymentAmt = CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                    txtRepaymentAmt.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue()));
                }
            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            this.setButtonEnableDisable();
            observable.setLoanRepaymentNo(CommonUtil.convertObjToStr(hash.get("LOAN_REPAY_NO")));
            observable.getData(hash);
            tblReleaseDetails.setModel(observable.getTblReleaseDetails());
            panTotalSelectedRecord.setVisible(false);
            lblTotalReleaseAdjustment.setVisible(false);
            setColorListEditMode();
            setColour();
            setSizeTableDataEditMode();
            calcTotalEditMode();
            calcTableTotalAmountEditMode();
            update();
            displayNCLSanctionDetails();
            displayCustomerName();
            btnDisplayActionPerformed(null);
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST") || observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
            //Set Tooltip Text
            if (txtRepaymentAmt.getText().length() > 0) {
                double repaymentAmt = 0;
                repaymentAmt = CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                    txtRepaymentAmt.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue()));
                }
            }
        }
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            ClientUtil.enableDisable(this, false);
        }
    }

    private void displayCustomerName() {
        if (txtCustID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", txtCustID.getText());
            List custLst = (List) ClientUtil.executeQuery("getSelectCustomerName", whereMap);
            if (custLst != null && custLst.size() > 0) {
                HashMap custMap = new HashMap();
                custMap = (HashMap) custLst.get(0);
                lblCustomerName.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER NAME")));
            }
        }
    }

    private void displayNCLSanctionDetails() {
        if (txtNCLSanctionNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
            List sanctionLst = (List) ClientUtil.executeQuery("getNCLSanctionDetails", whereMap);
            if (sanctionLst != null && sanctionLst.size() > 0) {
                whereMap = (HashMap) sanctionLst.get(0);
                txtKCCAccNo.setText(CommonUtil.convertObjToStr(whereMap.get("KCC_ACT_NUM")));
                observable.setTxtKCCAccNo(CommonUtil.convertObjToStr(whereMap.get("KCC_ACT_NUM")));
                observable.setTxtKCCProdID(CommonUtil.convertObjToStr(whereMap.get("KCC_PROD_ID")));
                observable.setTxtNCLSanctionNo(txtNCLSanctionNo.getText());
                observable.setCallingAccNo(CommonUtil.convertObjToStr(whereMap.get("CA_ACT_NUM")));
                txtCustID.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_ID")));
                whereMap = new HashMap();
                whereMap.put("ACT_NUM", txtKCCAccNo.getText());
                List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
                if (accountLst != null && accountLst.size() > 0) {
                    whereMap = (HashMap) accountLst.get(0);
                    lblKCCAccName.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
                }
            }
        }
    }

    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        //        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed

    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        //        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        //        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
            private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
                // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                                                    // Add your handling code here:
                                                    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                                                    updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
                                                    btnCancel.setEnabled(true);
                                                    btnReject.setEnabled(false);
                                                    btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("LOAN_REPAY_NO", observable.getLoanRepaymentNo());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getLoanRepaymentAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // Add your handling code here:
        popUp(CUSTOMER);
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btncancelActionPerformed() {
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("               ");
        observable.resetForm();
//        observable.resetTableValues();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        colourList = null;
        column = -1;
        lblTotalReleaseVal.setText("");
        lblTotalSelectedReleaseVal.setText("");
        lblCustomerName.setText("");
        lblKCCAccName.setText("");
        txtRepaymentAmt.setToolTipText("");
        tableSelectOption = false;
        btnCustID.setEnabled(false);
        btnSanctionNo.setEnabled(false);
        btnDisplay.setEnabled(false);
        btnViewDetails.setEnabled(false);
        btnAppropriate.setEnabled(false);
        lblTotalOutstandingAmtVal.setText("");
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransAcctNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        setSizeTableData();
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        //        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        //        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        //        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        //        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        //        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        //        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        //        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
            private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                // Add your handling code here:
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
                popUp("Edit");
                lblStatus.setText("Edit");
                btnSave.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
                        private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                            // Add your handling code here:
                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                                int transactionSize = 0;
                                if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                                    return;
                                } else {
                                    if (CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                                        transactionSize = (transactionUI.getOutputTO()).size();
                                        if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                                            return;
                                        } else {
                                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                        }
                                    } else if (transactionUI.getOutputTO().size() > 0) {
                                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                    }
                                }
                                if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                                    return;
                                } else if (transactionSize != 0) {
                                    if (!transactionUI.isBtnSaveTransactionDetailsFlag() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                                        return;
                                    }
                                    if (transactionUI.getOutputTO().size() > 0) {
                                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                                            System.out.println("#$#$$# AppropriateList:" + observable.getAppropriateList());
                                            if (observable.getFinalReleaseList() != null && observable.getFinalReleaseList().size() > 0) {
                                                if (observable.getAppropriateList() != null && observable.getAppropriateList().size() > 0) {
                                                    savePerformed();
                                                } else {
                                                    ClientUtil.showMessageWindow("AppropriateList Should not be Empty !!! ");
                                                    return;
                                                }
                                            } else {
                                                ClientUtil.showMessageWindow("Final Release List Should not be Empty !!! ");
                                                return;
                                            }
                                        } else {
                                            savePerformed();
                                        }
                                    }
                                }
                            }
                            //__ Make the Screen Closable..
                            setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
                                        private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                                            // Add your handling code here:
                                            btnnewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed
    private void savePerformed() {
        updateOBFields();
//        System.out.println("########### Repayment Amount    : " + CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()));
//        System.out.println("########### Appropriate Amount  : " + CommonUtil.convertObjToDouble(tblAppropriated.getValueAt(1, 0)));
        if (CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() == CommonUtil.convertObjToDouble(tblAppropriated.getValueAt(1, 0)).doubleValue()) {
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                            && observable.getProxyReturnMap().containsKey("LOAN_REPAY_NO")) {
                        ClientUtil.showMessageWindow("Loan Repayment No : " + observable.getProxyReturnMap().get("LOAN_REPAY_NO"));
                    }
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST") || observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
                btnCancelActionPerformed(null);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("Amount of Repayment is more than the Outstanding Amount...\n "
                    + "Select one more Release Record / Reduce the Amount of Repayment !!!");
        }
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j <= 1) {
                        transId = (String) transMap.get("TRANS_ID");
                        transIdList.add(transId);
                        transMode = "CASH";
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
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
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
    }

    public void updateOBFields() {
        observable.setTxtCustID(txtCustID.getText());
        observable.setTxtNCLSanctionNo(txtNCLSanctionNo.getText());
        observable.setTxtKCCAccNo(txtKCCAccNo.getText());
        observable.setTxtRepaymentAmt(txtRepaymentAmt.getText());
    }

    public void update() {
        txtCustID.setText(observable.getTxtCustID());
        txtNCLSanctionNo.setText(observable.getTxtNCLSanctionNo());
        txtKCCAccNo.setText(observable.getTxtKCCAccNo());
        txtRepaymentAmt.setText(observable.getTxtRepaymentAmt());
        tdtPaymentDt.setDateValue(observable.getTdtRepaymentDt());
    }

    private void txtNCLSanctionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNCLSanctionNoActionPerformed
   }//GEN-LAST:event_txtNCLSanctionNoActionPerformed

    private void txtNCLSanctionNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNCLSanctionNoFocusLost
        // TODO add your handling code here:
        if (txtNCLSanctionNo.getText().length() > 0) {
            viewType = SANCTION_NO;
            txtNCLSanctionNoActionPerformed();
        }
    }//GEN-LAST:event_txtNCLSanctionNoFocusLost

    private void txtNCLSanctionNoActionPerformed() {
        String sanctionNo = CommonUtil.convertObjToStr(txtNCLSanctionNo.getText());
        List lst = null;
        HashMap executeMap = new HashMap();
        HashMap custMap = new HashMap();
        if (sanctionNo.length() > 0) {
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            executeMap.put("NCL_SANCTION_NO", new String(sanctionNo));
            executeMap.put("CUST_ID", txtCustID.getText());
            executeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            lst = ClientUtil.executeQuery("getNCLMasterCustID", executeMap);
            if (lst != null && lst.size() > 0) {
                executeMap = (HashMap) lst.get(0);
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Sanction Number");
                txtNCLSanctionNo.setText("");
                txtKCCAccNo.setText("");
                lblKCCAccName.setText("");
                return;
            }
        }
    }

    private void btnSanctionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSanctionNoActionPerformed
        // Add your handling code here:
        popUp(SANCTION_NO);
    }//GEN-LAST:event_btnSanctionNoActionPerformed

    private void rdoReleaseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReleaseAllActionPerformed
        // TODO add your handling code here:
        rdoReleaseAll.setSelected(true);
        rdoReleaseOverDue.setSelected(false);
    }//GEN-LAST:event_rdoReleaseAllActionPerformed

    private void rdoReleaseOverDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReleaseOverDueActionPerformed
        // TODO add your handling code here:
        rdoReleaseAll.setSelected(false);
        rdoReleaseOverDue.setSelected(true);
    }//GEN-LAST:event_rdoReleaseOverDueActionPerformed

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        if (txtNCLSanctionNo.getText().length() > 0) {
            if (txtRepaymentAmt.getText().length() > 0) {
                if (rdoReleaseOverDue.isSelected()) {
                    observable.setRdoReleaseOverDue("OVER_DUE ONLY");
                } else {
                    observable.setRdoReleaseOverDue("");
                }
                observable.populateFinYearWiseTable(txtNCLSanctionNo.getText());
                tblFinYearWise.setModel(observable.getTblFinYearWise());
                if (tblFinYearWise.getRowCount() > 0) {
                    ClientUtil.enableDisable(panRepaymentDet, false);
                    btnDisplay.setEnabled(false);
                    btnCustID.setEnabled(false);
                    btnSanctionNo.setEnabled(false);
                    ClientUtil.enableDisable(panGender, true);
                } else {
                    ClientUtil.showMessageWindow("List Is Empty !!! ");
                    return;
                }
                calcTotalOutstandingAmount();
            } else {
                ClientUtil.showMessageWindow("Repayment Amount Should not be Empty !!! ");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("NCL Sanction No Should not be Empty !!! ");
            return;
        }
    }//GEN-LAST:event_btnDisplayActionPerformed
    private void calcTotalOutstandingAmount() {
        double outstandingAmount = 0.0;
        if (tblFinYearWise.getRowCount() > 0) {
            for (int i = 0; i < tblFinYearWise.getRowCount(); i++) {
                outstandingAmount += CommonUtil.convertObjToDouble(tblFinYearWise.getValueAt(i, 1));
            }
        }
        lblTotalOutstandingAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(outstandingAmount)));
    }
    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        // TODO add your handling code here:
        try {
            if (tblFinYearWise.getRowCount() > 0) {
                if (tblFinYearWise.getSelectedRow() < 0) {
                    ClientUtil.showMessageWindow("Please Select Particular Financial Year Row !!! ");
                    return;
                }
                String StartYear = "";
                String endYear = "";
                HashMap whereMap = new HashMap();
                String financialYear = CommonUtil.convertObjToStr(tblFinYearWise.getValueAt(tblFinYearWise.getSelectedRow(), 0));
                StartYear = financialYear.substring(0, 4);
                endYear = financialYear.substring(5, 9);
                colourList = new ArrayList();
                whereMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
                whereMap.put("START_YEAR", StartYear);
                whereMap.put("END_YEAR", endYear);
                if (rdoReleaseOverDue.isSelected()) {
                    observable.setRdoReleaseOverDue("OVER_DUE ONLY");
                } else {
                    observable.setRdoReleaseOverDue("");
                }
                observable.insertTableData(whereMap);
                tblReleaseDetails.setModel(observable.getTblReleaseDetails());
                setSizeTableData();
                column = -1;
                tableSelectOption = true;
                observable.setAppropriateTableData(null);
                tblAppropriated.setModel(observable.getTblAppropriated());
                btnAppropriate.setEnabled(true);
                ClientUtil.enableDisable(panGender, true);
                calcTableTotalAmount();
                lblTotalReleaseVal.setText(String.valueOf(CommonUtil.convertObjToInt(tblReleaseDetails.getRowCount())));
                lblTotalSelectedReleaseVal.setText("");
                if (tblReleaseDetails.getRowCount() <= 0) {
                    ClientUtil.showMessageWindow("List Is Empty !!! ");
                    lblTotalReleaseVal.setText("");
                    return;
                }
            } else {
                ClientUtil.showMessageWindow("Financial Year Table is Empty !!! ");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnViewDetailsActionPerformed

    private void tblReleaseDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReleaseDetailsMouseClicked
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && tblReleaseDetails.getRowCount() > 0) {
            if (tableSelectOption) {
                column = -1;
                //Changing Interest Amount Upto Due Date
                if (evt.getClickCount() == 2 && (tblReleaseDetails.getSelectedColumn() == 5) && ((Boolean) tblReleaseDetails.getValueAt(tblReleaseDetails.getSelectedRow(), 0)).booleanValue()) {
                    column = 5;
                    enteredAmount();
                    calcTableTotalAmount();
                    calcActualTotal();
                }

                //Changing Interest Amount After Due Date
                if (evt.getClickCount() == 2 && (tblReleaseDetails.getSelectedColumn() == 6) && ((Boolean) tblReleaseDetails.getValueAt(tblReleaseDetails.getSelectedRow(), 0)).booleanValue()) {
                    column = 6;
                    enteredAmount();
                    calcTableTotalAmount();
                    calcActualTotal();
                }

                if (tblReleaseDetails.getSelectedColumn() == 0) {
                    String st = CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(tblReleaseDetails.getSelectedRow(), 0));
                    if (st.equals("true")) {
                        tblReleaseDetails.setValueAt(new Boolean(false), tblReleaseDetails.getSelectedRow(), 0);
                    } else {
                        tblReleaseDetails.setValueAt(new Boolean(true), tblReleaseDetails.getSelectedRow(), 0);
                    }
                    calcActualTotal();
                }
            }
        }
    }//GEN-LAST:event_tblReleaseDetailsMouseClicked

    //Added By Suresh
    public void enteredAmount() {
        if (column != -1) {
            double intAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(tblReleaseDetails.getSelectedRow(), column).toString()).doubleValue();
            HashMap amountMap = new HashMap();
            amountMap.put("TITLE", "Interest Amount");
            amountMap.put("TOLERANCE_AMT", "10000000");
            amountMap.put("SELECTED_AMT", String.valueOf(intAmount));
            amountMap.put("CALCULATED_AMT", String.valueOf(intAmount));
            TextUI textUI = new TextUI(this, this, amountMap);
        }
    }

    public void modifyTransData(Object objData) {
        TextUI obj = (TextUI) objData;
        double newEnteredData = 0;
        double addDepositAmt = 0;
        String enteredData = obj.getTxtData();
        if (enteredData.length() > 0) {
            newEnteredData = CommonUtil.convertObjToDouble(enteredData).doubleValue();
            tblReleaseDetails.setValueAt(newEnteredData, tblReleaseDetails.getSelectedRow(), column);
        }
    }

    private void btnAppropriateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppropriateActionPerformed
        // TODO add your handling code here:
        try {
            if (tblReleaseDetails.getRowCount() > 0) {
                boolean flag = false;
                for (int k = 0; k < tblReleaseDetails.getRowCount(); k++) {
                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(k, 0)).equals("true")) {
                        flag = true;
                    }
                }
                if (flag) {
                    
                    //Latest Release Payment Validation
                    boolean unSelectedRel = true;
                    int selectedRelNo = 0;
                    int unSelectedRelNo = -1;
                    for (int s = 0; s < tblReleaseDetails.getRowCount(); s++) {
                        if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(s, 0)).equals("true")) {
                            selectedRelNo = s;
                        }else if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(s, 0)).equals("false") && unSelectedRel) {
                            unSelectedRelNo = s;
                            unSelectedRel = false;
                        }
                    }
                    System.out.println("################# unSelectedRelNo : " + unSelectedRelNo);
                    System.out.println("#################   selectedRelNo : " + selectedRelNo);
                    if (unSelectedRelNo < selectedRelNo && unSelectedRelNo>=0) {
//                        int yesNo = 0;
//                        String[] voucherOptions = {"Yes", "No"};
//                        yesNo = COptionPane.showOptionDialog(null, "Do you want to pay Latest Release?", CommonConstants.WARNINGTITLE,
//                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                                null, voucherOptions, voucherOptions[0]);
//                        if (yesNo == 1) {
//                            return;
//                        }
                        ClientUtil.showMessageWindow("You Adjust towards the Oldest Release First !!! ");
                        return;
                    }
                    ArrayList tableList = new ArrayList();
                    tableList = (ArrayList) (observable.getTblReleaseDetails().getDataArrayList());
                    System.out.println("############### Before tableList : " + tableList);
                    double repaymentAmount = 0.0;
                    tableSelectOption = false;
                    repaymentAmount = CommonUtil.convertObjToDouble(txtRepaymentAmt.getText());
                    for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                        String releaseNo = "";
                        if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                            releaseNo = CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 1));
                            ArrayList singleRow = new ArrayList();
                            singleRow.add(new Boolean(false));
                            singleRow.add(releaseNo);
                            singleRow.add("");
                            singleRow.add(CommonUtil.convertObjToDouble("0"));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            singleRow.add(CommonUtil.convertObjToDouble(""));
                            tableList.add(j + 1, singleRow);
                            btnAppropriate.setEnabled(false);
                        }
                    }
                    observable.setReleaseTableData(tableList);
                    tblReleaseDetails.setModel(observable.getTblReleaseDetails());

                    //Checking LOANS_PROD_APPROPRIATE_TRANS table
                    HashMap appropriateMap = new HashMap();
                    appropriateMap.put("PROD_ID", observable.getTxtKCCProdID());
                    List appList = ClientUtil.executeQuery("selectTallyAppropriatTransaction", appropriateMap);
                    if (appList != null && appList.size() > 0) {
                        appropriateMap = (HashMap) appList.get(0);
                        appropriateMap.remove("PROD_ID");
                        System.out.println("####### appropriateMap : " + appropriateMap);
                        java.util.Collection collectedValues = appropriateMap.values();
                        java.util.Iterator it = collectedValues.iterator();
                        int hierarchyLevel = 0;
                        double intAmt = 0.0;
                        double intAfterDueAmt = 0.0;
                        double penalAmt = 0.0;
                        double princAmt = 0.0;
                        double chargeAmt = 0.0;
                        while (it.hasNext()) {
                            hierarchyLevel++;
                            String hierachyValue = CommonUtil.convertObjToStr(it.next());
                            if (hierachyValue.equals("CHARGES")) {
                                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                                        double chargeAmount = 0.0;
                                        chargeAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(j, 8));
                                        if (chargeAmount > 0) {
                                            if (repaymentAmount > 0 && repaymentAmount > chargeAmount) {
                                                repaymentAmount -= chargeAmount;
                                                chargeAmt = chargeAmount;
                                            } else {
                                                chargeAmt = repaymentAmount;
                                                repaymentAmount = 0;
                                            }
                                            tblReleaseDetails.setValueAt(chargeAmt, j + 1, 8);
                                        }
                                    }
                                }
                            }
                            if (hierachyValue.equals("PENALINTEREST")) {
                                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                                        double penalAmount = 0.0;
                                        penalAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(j, 7));
                                        if (penalAmount > 0) {
                                            if (repaymentAmount > 0 && repaymentAmount > penalAmount) {
                                                repaymentAmount -= penalAmount;
                                                penalAmt = penalAmount;
                                            } else {
                                                penalAmt = repaymentAmount;
                                                repaymentAmount = 0;
                                            }
                                            tblReleaseDetails.setValueAt(penalAmt, j + 1, 7);
                                        }
                                    }
                                }
                            }
                            if (hierachyValue.equals("INTEREST")) {
                                //After Due Date
                                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                                        double intAfterDueAmount = 0.0;
                                        intAfterDueAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(j, 6));
                                        if (intAfterDueAmount > 0) {
                                            if (repaymentAmount > 0 && repaymentAmount > intAfterDueAmount) {
                                                repaymentAmount -= intAfterDueAmount;
                                                intAfterDueAmt = intAfterDueAmount;
                                            } else {
                                                intAfterDueAmt = repaymentAmount;
                                                repaymentAmount = 0;
                                            }
                                            tblReleaseDetails.setValueAt(intAfterDueAmt, j + 1, 6);
                                        }
                                    }
                                }
                                //Before Due Date
                                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                                        double intAmount = 0.0;
                                        intAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(j, 5));
                                        if (intAmount > 0) {
                                            if (repaymentAmount > 0 && repaymentAmount > intAmount) {
                                                repaymentAmount -= intAmount;
                                                intAmt = intAmount;
                                            } else {
                                                intAmt = repaymentAmount;
                                                repaymentAmount = 0;
                                            }
                                            tblReleaseDetails.setValueAt(intAmt, j + 1, 5);
                                        }
                                    }
                                }
                            }
                            if (hierachyValue.equals("PRINCIPAL")) {
                                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 0)).equals("true")) {
                                        double princAmount = 0.0;
                                        princAmount = CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(j, 4));
                                        if (princAmount > 0) {
                                            if (repaymentAmount > 0 && repaymentAmount > princAmount) {
                                                repaymentAmount -= princAmount;
                                                princAmt = princAmount;
                                            } else {
                                                princAmt = repaymentAmount;
                                                repaymentAmount = 0;
                                            }
                                            tblReleaseDetails.setValueAt(princAmt, j + 1, 4);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        ClientUtil.showMessageWindow("No data in LOANS_PROD_APPROPRIATE_TRANS table !!! ");
                        return;
                    }
                    btnAppropriate.setEnabled(false);
                    btnViewDetails.setEnabled(false);
                    ClientUtil.enableDisable(panGender, false);
                    setSizeTableData();
                    calcTotal();
                    setAppropriateList();
                    setFinalReleaseList();
                    setColorList();
                    setColour();
                    ClientUtil.enableDisable(this, false);
                } else {
                    ClientUtil.showMessageWindow("Please Select Atleast One Release Record !!! ");
                    return;
                }
            } else {
                ClientUtil.showMessageWindow("No Record in this Table !!! ");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAppropriateActionPerformed
    //Set Color List

    private void setColorList() {
        if (tblReleaseDetails.getRowCount() > 0) {
            colourList = new ArrayList();
            for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 2)).equals("")) {
                    colourList.add(String.valueOf(i));
                }
            }
        }
    }

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblReleaseDetails.setDefaultRenderer(Object.class, renderer);
    }

    //Set Color List
    private void setColorListEditMode() {
        if (tblReleaseDetails.getRowCount() > 0) {
            colourList = new ArrayList();
            for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 1)).equals("")) {
                    colourList.add(String.valueOf(i));
                }
            }
        }
    }

    private void tblFinYearWiseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFinYearWiseMousePressed
        // TODO add your handling code here:
        if (tblFinYearWise.getRowCount() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            btnViewDetails.setEnabled(true);
            ClientUtil.enableDisable(panGender, true);
        }
    }//GEN-LAST:event_tblFinYearWiseMousePressed

    private void txtRepaymentAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRepaymentAmtFocusLost
        // TODO add your handling code here:
        if (txtRepaymentAmt.getText().length() > 0) {
            double repaymentAmt = 0;
            repaymentAmt = CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue();
            if (CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue() > 0) {
                txtRepaymentAmt.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtRepaymentAmt.getText()).doubleValue()));
            } else {
                txtRepaymentAmt.setToolTipText("Rs. Zero");
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingTransType("TRANSFER");
            transactionUI.setCallingTransAcctNo(observable.getCallingAccNo());
            transactionUI.setCallingAmount(txtRepaymentAmt.getText());
        }
    }//GEN-LAST:event_txtRepaymentAmtFocusLost
    private void calcTotal() {
        double outstandingBalance = 0.0;
        double principaltoPay = 0.0;
        double intUptoDuedateAmt = 0.0;
        double intAfterDuedateAmt = 0.0;
        double penalAmt = 0.0;
        double chargesAmt = 0.0;
        double totalAmt = 0.0;

        double appoutstandingBalance = 0.0;
        double appprincipaltoPay = 0.0;
        double appintUptoDuedateAmt = 0.0;
        double appintAfterDuedateAmt = 0.0;
        double apppenalAmt = 0.0;
        double appchargesAmt = 0.0;
        double apptotalAmt = 0.0;
        for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
            //Released Values Total
            if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 0)).equals("true")) {
                outstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
                principaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
                intUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
                intAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
                penalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
                chargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 8));
            }
            //Appropriate Values Total
            if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 2)).equals("")) {
                appoutstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
                appprincipaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
                appintUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
                appintAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
                apppenalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
                appchargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 8));
            }
        }
//        totalAmt = outstandingBalance + principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
//        apptotalAmt = appoutstandingBalance + appprincipaltoPay + appintUptoDuedateAmt + appintAfterDuedateAmt + apppenalAmt + appchargesAmt;
        totalAmt = principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
        apptotalAmt = appprincipaltoPay + appintUptoDuedateAmt + appintAfterDuedateAmt + apppenalAmt + appchargesAmt;
        ArrayList singleRow = new ArrayList();
        ArrayList tableList = new ArrayList();
        if (totalAmt > 0) {
            singleRow = new ArrayList();
            singleRow.add(CommonUtil.convertObjToDouble(totalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(outstandingBalance));
            singleRow.add(CommonUtil.convertObjToDouble(principaltoPay));
            singleRow.add(CommonUtil.convertObjToDouble(intUptoDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(intAfterDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(penalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(chargesAmt));
            tableList.add(singleRow);
            totalAmt = 0;
        }
        if (apptotalAmt > 0) {
            singleRow = new ArrayList();
            singleRow.add(CommonUtil.convertObjToDouble(apptotalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appoutstandingBalance));
            singleRow.add(CommonUtil.convertObjToDouble(appprincipaltoPay));
            singleRow.add(CommonUtil.convertObjToDouble(appintUptoDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appintAfterDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(apppenalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appchargesAmt));
            tableList.add(singleRow);
            apptotalAmt = 0;
        }
        observable.setAppropriateTableData(tableList);
        tblAppropriated.setModel(observable.getTblAppropriated());
    }

    private void calcTableTotalAmountEditMode() {
        double outstandingBalance = 0.0;
        double principaltoPay = 0.0;
        double intUptoDuedateAmt = 0.0;
        double intAfterDuedateAmt = 0.0;
        double penalAmt = 0.0;
        double chargesAmt = 0.0;
        double totalAmt = 0.0;
        for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
            outstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 2));
            principaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
            intUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
            intAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
            penalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
            chargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
        }
        txtTotalClearBalanceAmount.setText(String.valueOf(outstandingBalance));
        txtTotalPrincipalAmount.setText(String.valueOf(principaltoPay));
        txtTotalIntDueAmount.setText(String.valueOf(intUptoDuedateAmt));
        txtTotalIntAfterDtAmount.setText(String.valueOf(intAfterDuedateAmt));
        txtTotalPenalAmount.setText(String.valueOf(penalAmt));
        txtTotalChargeAmount.setText(String.valueOf(chargesAmt));
    }

    private void calcTableTotalAmount() {
        double outstandingBalance = 0.0;
        double principaltoPay = 0.0;
        double intUptoDuedateAmt = 0.0;
        double intAfterDuedateAmt = 0.0;
        double penalAmt = 0.0;
        double chargesAmt = 0.0;
        double totalAmt = 0.0;
        for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
            outstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
            principaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
            intUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
            intAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
            penalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
            chargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 8));
        }
        txtTotalClearBalanceAmount.setText(String.valueOf(outstandingBalance));
        txtTotalPrincipalAmount.setText(String.valueOf(principaltoPay));
        txtTotalIntDueAmount.setText(String.valueOf(intUptoDuedateAmt));
        txtTotalIntAfterDtAmount.setText(String.valueOf(intAfterDuedateAmt));
        txtTotalPenalAmount.setText(String.valueOf(penalAmt));
        txtTotalChargeAmount.setText(String.valueOf(chargesAmt));
    }

    private void calcTotalEditMode() {
        double outstandingBalance = 0.0;
        double principaltoPay = 0.0;
        double intUptoDuedateAmt = 0.0;
        double intAfterDuedateAmt = 0.0;
        double penalAmt = 0.0;
        double chargesAmt = 0.0;
        double totalAmt = 0.0;

        double appoutstandingBalance = 0.0;
        double appprincipaltoPay = 0.0;
        double appintUptoDuedateAmt = 0.0;
        double appintAfterDuedateAmt = 0.0;
        double apppenalAmt = 0.0;
        double appchargesAmt = 0.0;
        double apptotalAmt = 0.0;
        for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
            //Released Values Total
            if (!CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 1)).equals("")) {
                outstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 2));
                principaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
                intUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
                intAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
                penalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
                chargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
            } else {
                //Appropriate Values Total
                appoutstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 2));
                appprincipaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
                appintUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
                appintAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
                apppenalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
                appchargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
            }
        }
//        totalAmt = outstandingBalance + principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
//        apptotalAmt = appoutstandingBalance + appprincipaltoPay + appintUptoDuedateAmt + appintAfterDuedateAmt + apppenalAmt + appchargesAmt;
        totalAmt = principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
        apptotalAmt = appprincipaltoPay + appintUptoDuedateAmt + appintAfterDuedateAmt + apppenalAmt + appchargesAmt;
        ArrayList singleRow = new ArrayList();
        ArrayList tableList = new ArrayList();
        if (totalAmt > 0) {
            singleRow = new ArrayList();
            singleRow.add(CommonUtil.convertObjToDouble(totalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(outstandingBalance));
            singleRow.add(CommonUtil.convertObjToDouble(principaltoPay));
            singleRow.add(CommonUtil.convertObjToDouble(intUptoDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(intAfterDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(penalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(chargesAmt));
            tableList.add(singleRow);
            totalAmt = 0;
        }
        if (apptotalAmt > 0) {
            singleRow = new ArrayList();
            singleRow.add(CommonUtil.convertObjToDouble(apptotalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appoutstandingBalance));
            singleRow.add(CommonUtil.convertObjToDouble(appprincipaltoPay));
            singleRow.add(CommonUtil.convertObjToDouble(appintUptoDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appintAfterDuedateAmt));
            singleRow.add(CommonUtil.convertObjToDouble(apppenalAmt));
            singleRow.add(CommonUtil.convertObjToDouble(appchargesAmt));
            tableList.add(singleRow);
            apptotalAmt = 0;
        }
        observable.setAppropriateTableData(tableList);
        tblAppropriated.setModel(observable.getTblAppropriated());
    }

    private void calcActualTotal() {
        double outstandingBalance = 0.0;
        double principaltoPay = 0.0;
        double intUptoDuedateAmt = 0.0;
        double intAfterDuedateAmt = 0.0;
        double penalAmt = 0.0;
        double chargesAmt = 0.0;
        double totalAmt = 0.0;
        int selectedCount = 0;
        for (int i = 0; i < tblReleaseDetails.getRowCount(); i++) {
            //Actual Values Total
            if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(i, 0)).equals("true")) {
                outstandingBalance += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 3));
                principaltoPay += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 4));
                intUptoDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 5));
                intAfterDuedateAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 6));
                penalAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 7));
                chargesAmt += CommonUtil.convertObjToDouble(tblReleaseDetails.getValueAt(i, 8));
                selectedCount++;
            }
//            totalAmt = outstandingBalance + principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
            totalAmt = principaltoPay + intUptoDuedateAmt + intAfterDuedateAmt + penalAmt + chargesAmt;
            ArrayList singleRow = new ArrayList();
            ArrayList tableList = new ArrayList();
            if (totalAmt > 0) {
                singleRow = new ArrayList();
                singleRow.add(CommonUtil.convertObjToDouble(totalAmt));
                singleRow.add(CommonUtil.convertObjToDouble(outstandingBalance));
                singleRow.add(CommonUtil.convertObjToDouble(principaltoPay));
                singleRow.add(CommonUtil.convertObjToDouble(intUptoDuedateAmt));
                singleRow.add(CommonUtil.convertObjToDouble(intAfterDuedateAmt));
                singleRow.add(CommonUtil.convertObjToDouble(penalAmt));
                singleRow.add(CommonUtil.convertObjToDouble(chargesAmt));
                tableList.add(singleRow);
                totalAmt = 0;
            }
            observable.setAppropriateTableData(tableList);
            tblAppropriated.setModel(observable.getTblAppropriated());
        }
        lblTotalSelectedReleaseVal.setText(String.valueOf(selectedCount));
    }

    private void setAppropriateList() {
        ArrayList appropriateList = new ArrayList();
        ArrayList tableList = new ArrayList();
        tableList = (ArrayList) (observable.getTblReleaseDetails().getDataArrayList());
//        System.out.println("############### Before tableList : " + tableList);
        for (int i = 0; i < tableList.size(); i++) {
            ArrayList rowList = new ArrayList();
            rowList = (ArrayList) tableList.get(i);
//            System.out.println("################## rowList : " + rowList);
            if (rowList.get(0).equals(false) && rowList.get(2).equals("")
                    && CommonUtil.convertObjToDouble(rowList.get(3)) <= 0) {
                appropriateList.add(rowList);
            }
        }
        System.out.println("############### appropriateList : " + appropriateList);
        observable.setAppropriateList(appropriateList);
    }

    private void setFinalReleaseList() {
        finalList = observable.getFinalReleaseList();
        HashMap releaseMap = new HashMap();
        if (finalList != null && finalList.size() > 0) {
//            System.out.println("#$@$#@$@$@ FinalList : " + finalList);
            for (int i = 0; i < finalList.size(); i++) {
                String releaseNo = "";
                releaseMap = (HashMap) finalList.get(i);
                releaseNo = CommonUtil.convertObjToStr(releaseMap.get("RELEASE_NO"));
                for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 1)).equals(releaseNo)) {
                        if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 1)).equals(releaseNo)
                                && ((Boolean) tblReleaseDetails.getValueAt(j, 0)).booleanValue()) {
                            releaseMap.put("INT_UP_TO_DATE_AMT", String.valueOf(CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 5))));
                            releaseMap.put("INT_AFTER_DUE_DATE_AMT", String.valueOf(CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 6))));
                            break;
                        } else {
                            finalList.remove(i--);
                            j = tblReleaseDetails.getRowCount();
                        }
                    }
                }
            }
            System.out.println("############### FINAL Release List Data  : " + finalList);
            observable.setFinalReleaseList(finalList);
        }
    }

    private void btnnewActionPerformed() {
        btnNewPressed = true;
        setModified(true);
        ClientUtil.enableDisable(this, true);// Enables the panel...
        ClientUtil.enableDisable(panTotalNetAmount, false);
        txtKCCAccNo.setEnabled(false);
        panTotalSelectedRecord.setVisible(true);
        lblTotalReleaseAdjustment.setVisible(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        observable.setStatus();
        tdtPaymentDt.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        setFocusFirstTab();
        lblTotalOutstandingAmtVal.setText("");
        colourList = new ArrayList();
        tableSelectOption = true;
        rdoReleaseAll.setSelected(true);
        btnCustID.setEnabled(true);
        btnSanctionNo.setEnabled(true);
        btnDisplay.setEnabled(true);
        btnViewDetails.setEnabled(true);
        btnAppropriate.setEnabled(true);
        tdtPaymentDt.setDateValue(DateUtil.getStringDate(currDt));
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        txtRepaymentAmt.setToolTipText("");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
    }

    private void setFocusFirstTab() {
        tabRepayment.setSelectedIndex(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAppropriate;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMembershipLia;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSanctionNo;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewDetails;
    private com.see.truetransact.uicomponent.CLabel lblAppTotal;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccName;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSanctionAmt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblSelectedTotal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalOutstandingAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalOutstandingAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
    private com.see.truetransact.uicomponent.CLabel lblTotalRelease;
    private com.see.truetransact.uicomponent.CLabel lblTotalRelease2;
    private com.see.truetransact.uicomponent.CLabel lblTotalReleaseAdjustment;
    private com.see.truetransact.uicomponent.CLabel lblTotalReleaseVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalSelectedReleaseVal;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CMenuBar mbrTermLoan;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panRepaymentDet;
    private com.see.truetransact.uicomponent.CPanel panRepaymentDetails;
    private com.see.truetransact.uicomponent.CPanel panRepaymentTbl;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPanel panTotalNetAmount;
    private com.see.truetransact.uicomponent.CPanel panTotalSelectedRecord;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCourtOrderGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDirectRepaymentGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEnhanceGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGuarnConstution;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRebateInterestGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoReleaseAll;
    private com.see.truetransact.uicomponent.CRadioButton rdoReleaseOverDue;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRenewalGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSHG;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSalaryRecovery;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidyAddMinusGroup;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpAppropriated;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_FinYearWise;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ReleaseDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabRepayment;
    private com.see.truetransact.uicomponent.CTable tblAppropriated;
    private com.see.truetransact.uicomponent.CTable tblFinYearWise;
    private com.see.truetransact.uicomponent.CTable tblReleaseDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtPaymentDt;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtKCCAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNCLSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtRepaymentAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalChargeAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalClearBalanceAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalIntAfterDtAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalIntDueAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalPenalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalPrincipalAmount;
    // End of variables declaration//GEN-END:variables
}
