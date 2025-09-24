/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestSubsidyAdjustmentUI.java
 *
 * Created on July 04, 2013, 12:37 PM
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
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
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

/*
 *
 * @author Suresh R
 *
 */
public class InterestSubsidyAdjustmentUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    TransactionUI transactionUI = new TransactionUI();
    String viewType = "";
    private boolean btnNewPressed;
    private boolean isFilled;
    final String AUTHORIZE = "Authorize";
    private Date currDt;
    private final InterestSubsidyAdjustmentOB observable;
    HashMap mandatoryMap = null;
    private TableModelListener tableModelListener;
    DefaultTableModel model = null;
    private List finalList = null;
    int selectedRow = -1;

    public InterestSubsidyAdjustmentUI() {
        initComponents();
        setFieldNames();
        observable = new InterestSubsidyAdjustmentOB();
        setMaxLengths();
        setButtonEnableDisable();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(this, false);
        panTransactionDetails.add(transactionUI);
        transactionUI.setSourceScreen("INTEREST_SUBSIDY");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        panSubsidyProductDetails.setVisible(true);
        panOTSDetails.setVisible(false);
        cboAgencyName.setModel(observable.getCbmAgencyName());
        cboProdType.setModel(observable.getCbmProdType());
        cboOTSSanctionedBy.setModel(observable.getCbmOTSSanctionedBy());
        panTLSubsidy.setVisible(false);
        tblSubsidyDetails.setModel(observable.getTblSubsidyDetails());
        setSizeTableData();
        btnEnableDisable(false);
        srpSubsidyDetails.setVisible(false);
        panTotal.setVisible(false);
    }

    private void btnEnableDisable(boolean flag) {
        btnFromAccountNo.setEnabled(flag);
        btnFromReleaseNo.setEnabled(flag);
        btnToAccountNo.setEnabled(flag);
        btnToReleaseNo.setEnabled(flag);
        btnViewDetails.setEnabled(flag);
        btnDisplay.setEnabled(flag);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDisplay.setName("btnDisplay");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnFromAccountNo.setName("btnFromAccountNo");
        btnFromReleaseNo.setName("btnFromReleaseNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnToAccountNo.setName("btnToAccountNo");
        btnToReleaseNo.setName("btnToReleaseNo");
        btnView.setName("btnView");
        btnViewDetails.setName("btnViewDetails");
        cboAgencyName.setName("cboAgencyName");
        cboOTSSanctionedBy.setName("cboOTSSanctionedBy");
        cboProdID.setName("cboProdID");
        cboProdType.setName("cboProdType");
        lblAgencyName.setName("lblAgencyName");
        lblFromAccountNo.setName("lblFromAccountNo");
        lblFromDt.setName("lblFromDt");
        lblFromReleaseNo.setName("lblFromReleaseNo");
        lblInterestType.setName("lblInterestType");
        lblMsg.setName("lblMsg");
        lblOTSAmount.setName("lblOTSAmount");
        lblOTSSanctionDt.setName("lblOTSSanctionDt");
        lblOTSSanctionNo.setName("lblOTSSanctionNo");
        lblOTSSanctionedBy.setName("lblOTSSanctionedBy");
        lblProdID.setName("lblProdID");
        lblProdType.setName("lblProdType");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpaces.setName("lblSpaces");
        lblStatus.setName("lblStatus");
        lblSubSidyReleaseRefNo.setName("lblSubSidyReleaseRefNo");
        lblSubsidyReceivedAmt.setName("lblSubsidyReceivedAmt");
        lblSubsidyReceivedAmtPer.setName("lblSubsidyReceivedAmtPer");
        lblSubsidyReleaseDt.setName("lblSubsidyReleaseDt");
        lblToAccountNo.setName("lblToAccountNo");
        lblToDt.setName("lblToDt");
        lblToReleaseNo.setName("lblToReleaseNo");
        lblTotalPayment.setName("lblTotalPayment");
        mbrMain.setName("mbrMain");
        panGroupData.setName("panGroupData");
        panGroupInfo.setName("panGroupInfo");
        panInterestGroup.setName("panInterestGroup");
        panInterestSubsidyAdjustment.setName("panInterestSubsidyAdjustment");
        panModeOfOpening.setName("panModeOfOpening");
        panOTSDetails.setName("panOTSDetails");
        panProcess.setName("panProcess");
        panStatus.setName("panStatus");
        panSubsidyProductDetails.setName("panSubsidyProductDetails");
        panTLSubsidy.setName("panTLSubsidy");
        panTotal.setName("panTotal");
        panTransactionDetails.setName("panTransactionDetails");
        rdoInterestType_OTS.setName("rdoInterestType_OTS");
        rdoInterestType_Recovery.setName("rdoInterestType_Recovery");
        rdoInterestType_Subsidy.setName("rdoInterestType_Subsidy");
        rdoInterestType_WriteOff.setName("rdoInterestType_WriteOff");
        rdoTLSubsidyInt.setName("rdoTLSubsidyInt");
        rdoTLSubsidyPrincipal.setName("rdoTLSubsidyPrincipal");
        srpSubsidyDetails.setName("srpSubsidyDetails");
        srpTable_FinYearWise.setName("srpTable_FinYearWise");
        tabInterestSubsidyAdjustment.setName("tabInterestSubsidyAdjustment");
        tblFinYearWise.setName("tblFinYearWise");
        tblSubsidyDetails.setName("tblSubsidyDetails");
        tdtFromDt.setName("tdtFromDt");
        tdtOTSSanctionDt.setName("tdtOTSSanctionDt");
        tdtSubsidyReleaseDt.setName("tdtSubsidyReleaseDt");
        tdtToDt.setName("tdtToDt");
        txtFromAccountNo.setName("txtFromAccountNo");
        txtFromReleaseNo.setName("txtFromReleaseNo");
        txtOTSAmount.setName("txtOTSAmount");
        txtOTSSanctionNo.setName("txtOTSSanctionNo");
        txtRemarks.setName("txtRemarks");
        txtSubSidyReleaseRefNo.setName("txtSubSidyReleaseRefNo");
        txtSubsidyReceivedAmt.setName("txtSubsidyReceivedAmt");
        txtSubsidyReceivedAmtPer.setName("txtSubsidyReceivedAmtPer");
        txtToAccountNo.setName("txtToAccountNo");
        txtToReleaseNo.setName("txtToReleaseNo");
        txtTotalBalSubsidyAmount.setName("txtTotalBalSubsidyAmount");
        txtTotalSubsidyAmount.setName("txtTotalSubsidyAmount");
        txtTotalsubAdjAmount.setName("txtTotalsubAdjAmount");
    }

    private void setSizeTableData() {
        tblSubsidyDetails.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblSubsidyDetails.getColumnModel().getColumn(1).setPreferredWidth(40);
        tblSubsidyDetails.getColumnModel().getColumn(2).setPreferredWidth(85);
        tblSubsidyDetails.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblSubsidyDetails.getColumnModel().getColumn(4).setPreferredWidth(65);
        tblSubsidyDetails.getColumnModel().getColumn(5).setPreferredWidth(65);
        tblSubsidyDetails.getColumnModel().getColumn(6).setPreferredWidth(65);
        tblSubsidyDetails.getColumnModel().getColumn(7).setPreferredWidth(65);
    }
    
    private void setSizeTableDataEdit() {
        tblSubsidyDetails.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblSubsidyDetails.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblSubsidyDetails.getColumnModel().getColumn(2).setPreferredWidth(85);
        tblSubsidyDetails.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblSubsidyDetails.getColumnModel().getColumn(4).setPreferredWidth(65);
        tblSubsidyDetails.getColumnModel().getColumn(5).setPreferredWidth(65);
        tblSubsidyDetails.getColumnModel().getColumn(6).setPreferredWidth(65);
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
        txtRemarks.setAllowAll(true);
        txtToAccountNo.setAllowAll(true);
        txtToReleaseNo.setAllowAll(true);
        txtFromAccountNo.setAllowAll(true);
        txtFromReleaseNo.setAllowAll(true);
        txtOTSSanctionNo.setAllowAll(true);
        txtSubSidyReleaseRefNo.setAllowAll(true);
        txtSubsidyReceivedAmtPer.setValidation(new NumericValidation(3, 2));
        txtOTSAmount.setValidation(new CurrencyValidation(14, 2));
        txtSubsidyReceivedAmt.setValidation(new CurrencyValidation(14, 2));
        txtTotalSubsidyAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalsubAdjAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalBalSubsidyAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalTodayAdjustment.setValidation(new CurrencyValidation(14, 2));
    }

    // To set The Status of the Buttons Depending on the Condition...
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgInterestType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTLSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        panInterestSubsidyAdjustment = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabInterestSubsidyAdjustment = new com.see.truetransact.uicomponent.CTabbedPane();
        panInterestGroup = new com.see.truetransact.uicomponent.CPanel();
        panModeOfOpening = new com.see.truetransact.uicomponent.CPanel();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        rdoInterestType_Subsidy = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestType_Recovery = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestType_WriteOff = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestType_OTS = new com.see.truetransact.uicomponent.CRadioButton();
        panGroupInfo = new com.see.truetransact.uicomponent.CPanel();
        lblAgencyName = new com.see.truetransact.uicomponent.CLabel();
        cboAgencyName = new com.see.truetransact.uicomponent.CComboBox();
        lblSubSidyReleaseRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtSubSidyReleaseRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblSubsidyReleaseDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSubsidyReleaseDt = new com.see.truetransact.uicomponent.CDateField();
        lblSubsidyReceivedAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSubsidyReceivedAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSubsidyReceivedAmtPer = new com.see.truetransact.uicomponent.CLabel();
        txtSubsidyReceivedAmtPer = new com.see.truetransact.uicomponent.CTextField();
        panSubsidyProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtFromAccountNo = new com.see.truetransact.uicomponent.CTextField();
        cboProdID = new com.see.truetransact.uicomponent.CComboBox();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        lblToReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        txtToReleaseNo = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblToAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtToAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblFromReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        txtFromReleaseNo = new com.see.truetransact.uicomponent.CTextField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        btnToReleaseNo = new com.see.truetransact.uicomponent.CButton();
        btnFromReleaseNo = new com.see.truetransact.uicomponent.CButton();
        btnToAccountNo = new com.see.truetransact.uicomponent.CButton();
        btnFromAccountNo = new com.see.truetransact.uicomponent.CButton();
        panTLSubsidy = new com.see.truetransact.uicomponent.CPanel();
        rdoTLSubsidyInt = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTLSubsidyPrincipal = new com.see.truetransact.uicomponent.CRadioButton();
        panOTSDetails = new com.see.truetransact.uicomponent.CPanel();
        lblOTSSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        txtOTSSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        tdtOTSSanctionDt = new com.see.truetransact.uicomponent.CDateField();
        lblOTSSanctionDt = new com.see.truetransact.uicomponent.CLabel();
        lblOTSSanctionedBy = new com.see.truetransact.uicomponent.CLabel();
        cboOTSSanctionedBy = new com.see.truetransact.uicomponent.CComboBox();
        lblOTSAmount = new com.see.truetransact.uicomponent.CLabel();
        txtOTSAmount = new com.see.truetransact.uicomponent.CTextField();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpSubsidyDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubsidyDetails = new com.see.truetransact.uicomponent.CTable();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtTotalBalSubsidyAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalsubAdjAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalSubsidyAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalTodayAdjustment = new com.see.truetransact.uicomponent.CTextField();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        panFinancialYearTable = new com.see.truetransact.uicomponent.CPanel();
        srpTable_FinYearWise = new com.see.truetransact.uicomponent.CScrollPane();
        tblFinYearWise = new com.see.truetransact.uicomponent.CTable();
        btnViewDetails = new com.see.truetransact.uicomponent.CButton();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Interest Subsidy Adjustment Menu");
        setMinimumSize(new java.awt.Dimension(860, 663));
        setPreferredSize(new java.awt.Dimension(860, 663));

        panInterestSubsidyAdjustment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInterestSubsidyAdjustment.setMinimumSize(new java.awt.Dimension(850, 650));
        panInterestSubsidyAdjustment.setPreferredSize(new java.awt.Dimension(850, 650));
        panInterestSubsidyAdjustment.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panInterestSubsidyAdjustment.add(lblSpaces, gridBagConstraints);

        tabInterestSubsidyAdjustment.setMinimumSize(new java.awt.Dimension(860, 575));
        tabInterestSubsidyAdjustment.setPreferredSize(new java.awt.Dimension(860, 575));

        panInterestGroup.setMinimumSize(new java.awt.Dimension(950, 565));
        panInterestGroup.setPreferredSize(new java.awt.Dimension(950, 565));
        panInterestGroup.setLayout(new java.awt.GridBagLayout());

        panModeOfOpening.setMinimumSize(new java.awt.Dimension(850, 40));
        panModeOfOpening.setPreferredSize(new java.awt.Dimension(850, 40));
        panModeOfOpening.setLayout(new java.awt.GridBagLayout());

        lblInterestType.setText("Interest Type");
        lblInterestType.setMinimumSize(new java.awt.Dimension(98, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panModeOfOpening.add(lblInterestType, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_Subsidy);
        rdoInterestType_Subsidy.setText("Subsidy Reimbursement");
        rdoInterestType_Subsidy.setMaximumSize(new java.awt.Dimension(166, 15));
        rdoInterestType_Subsidy.setMinimumSize(new java.awt.Dimension(166, 15));
        rdoInterestType_Subsidy.setPreferredSize(new java.awt.Dimension(175, 15));
        rdoInterestType_Subsidy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_SubsidyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panModeOfOpening.add(rdoInterestType_Subsidy, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_Recovery);
        rdoInterestType_Recovery.setText("Recovery from Customer");
        rdoInterestType_Recovery.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoInterestType_Recovery.setMinimumSize(new java.awt.Dimension(170, 18));
        rdoInterestType_Recovery.setPreferredSize(new java.awt.Dimension(170, 18));
        rdoInterestType_Recovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_RecoveryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panModeOfOpening.add(rdoInterestType_Recovery, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_WriteOff);
        rdoInterestType_WriteOff.setText("Write Off");
        rdoInterestType_WriteOff.setMaximumSize(new java.awt.Dimension(68, 15));
        rdoInterestType_WriteOff.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoInterestType_WriteOff.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoInterestType_WriteOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_WriteOffActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panModeOfOpening.add(rdoInterestType_WriteOff, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_OTS);
        rdoInterestType_OTS.setText("OTS");
        rdoInterestType_OTS.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoInterestType_OTS.setMinimumSize(new java.awt.Dimension(85, 15));
        rdoInterestType_OTS.setPreferredSize(new java.awt.Dimension(85, 18));
        rdoInterestType_OTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_OTSActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panModeOfOpening.add(rdoInterestType_OTS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 12;
        panInterestGroup.add(panModeOfOpening, gridBagConstraints);

        panGroupInfo.setMinimumSize(new java.awt.Dimension(280, 250));
        panGroupInfo.setPreferredSize(new java.awt.Dimension(280, 250));
        panGroupInfo.setLayout(new java.awt.GridBagLayout());

        lblAgencyName.setText("Name of the Agency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(lblAgencyName, gridBagConstraints);

        cboAgencyName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgencyName.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(cboAgencyName, gridBagConstraints);

        lblSubSidyReleaseRefNo.setText("Subsidy Release Ref No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(lblSubSidyReleaseRefNo, gridBagConstraints);

        txtSubSidyReleaseRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(txtSubSidyReleaseRefNo, gridBagConstraints);

        lblSubsidyReleaseDt.setText("Subsidy Release Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(lblSubsidyReleaseDt, gridBagConstraints);

        tdtSubsidyReleaseDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSubsidyReleaseDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(tdtSubsidyReleaseDt, gridBagConstraints);

        lblSubsidyReceivedAmt.setText("Subsidy Amount Received");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(lblSubsidyReceivedAmt, gridBagConstraints);

        txtSubsidyReceivedAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubsidyReceivedAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubsidyReceivedAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(txtSubsidyReceivedAmt, gridBagConstraints);

        lblSubsidyReceivedAmtPer.setText("Subsidy Amount Received %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(lblSubsidyReceivedAmtPer, gridBagConstraints);

        txtSubsidyReceivedAmtPer.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupInfo.add(txtSubsidyReceivedAmtPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestGroup.add(panGroupInfo, gridBagConstraints);

        panSubsidyProductDetails.setMinimumSize(new java.awt.Dimension(256, 250));
        panSubsidyProductDetails.setPreferredSize(new java.awt.Dimension(256, 250));
        panSubsidyProductDetails.setLayout(new java.awt.GridBagLayout());

        lblFromAccountNo.setText("From Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblFromAccountNo, gridBagConstraints);

        txtFromAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(txtFromAccountNo, gridBagConstraints);

        cboProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdID.setPopupWidth(220);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(cboProdID, gridBagConstraints);

        lblProdID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblProdID, gridBagConstraints);

        lblToReleaseNo.setText("To Release No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblToReleaseNo, gridBagConstraints);

        txtToReleaseNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToReleaseNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToReleaseNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(txtToReleaseNo, gridBagConstraints);

        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(tdtFromDt, gridBagConstraints);

        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblFromDt, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(cboProdType, gridBagConstraints);

        lblToAccountNo.setText("To Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblToAccountNo, gridBagConstraints);

        txtToAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(txtToAccountNo, gridBagConstraints);

        lblFromReleaseNo.setText("From Release No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblFromReleaseNo, gridBagConstraints);

        txtFromReleaseNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromReleaseNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromReleaseNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(txtFromReleaseNo, gridBagConstraints);

        tdtToDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(tdtToDt, gridBagConstraints);

        lblToDt.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyProductDetails.add(lblToDt, gridBagConstraints);

        btnToReleaseNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToReleaseNo.setToolTipText("Select Customer");
        btnToReleaseNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToReleaseNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnToReleaseNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnToReleaseNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnToReleaseNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToReleaseNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSubsidyProductDetails.add(btnToReleaseNo, gridBagConstraints);

        btnFromReleaseNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromReleaseNo.setToolTipText("Select Customer");
        btnFromReleaseNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFromReleaseNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnFromReleaseNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnFromReleaseNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnFromReleaseNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromReleaseNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSubsidyProductDetails.add(btnFromReleaseNo, gridBagConstraints);

        btnToAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccountNo.setToolTipText("Select Customer");
        btnToAccountNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToAccountNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnToAccountNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnToAccountNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnToAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSubsidyProductDetails.add(btnToAccountNo, gridBagConstraints);

        btnFromAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccountNo.setToolTipText("Select Customer");
        btnFromAccountNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFromAccountNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnFromAccountNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnFromAccountNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnFromAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSubsidyProductDetails.add(btnFromAccountNo, gridBagConstraints);

        panTLSubsidy.setMinimumSize(new java.awt.Dimension(260, 18));
        panTLSubsidy.setName("panTLSubsidy");
        panTLSubsidy.setPreferredSize(new java.awt.Dimension(260, 18));
        panTLSubsidy.setLayout(new java.awt.GridBagLayout());

        rdgTLSubsidy.add(rdoTLSubsidyInt);
        rdoTLSubsidyInt.setText("Interest Subsidy");
        rdoTLSubsidyInt.setMaximumSize(new java.awt.Dimension(120, 18));
        rdoTLSubsidyInt.setMinimumSize(new java.awt.Dimension(120, 18));
        rdoTLSubsidyInt.setName("rdoGender_Male");
        rdoTLSubsidyInt.setPreferredSize(new java.awt.Dimension(120, 18));
        rdoTLSubsidyInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTLSubsidyIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTLSubsidy.add(rdoTLSubsidyInt, gridBagConstraints);

        rdgTLSubsidy.add(rdoTLSubsidyPrincipal);
        rdoTLSubsidyPrincipal.setText("Principal Subsidy");
        rdoTLSubsidyPrincipal.setMaximumSize(new java.awt.Dimension(130, 18));
        rdoTLSubsidyPrincipal.setMinimumSize(new java.awt.Dimension(130, 18));
        rdoTLSubsidyPrincipal.setName("rdoGender_Female");
        rdoTLSubsidyPrincipal.setPreferredSize(new java.awt.Dimension(130, 18));
        rdoTLSubsidyPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTLSubsidyPrincipalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panTLSubsidy.add(rdoTLSubsidyPrincipal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panSubsidyProductDetails.add(panTLSubsidy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestGroup.add(panSubsidyProductDetails, gridBagConstraints);

        panOTSDetails.setMinimumSize(new java.awt.Dimension(280, 250));
        panOTSDetails.setPreferredSize(new java.awt.Dimension(280, 250));
        panOTSDetails.setLayout(new java.awt.GridBagLayout());

        lblOTSSanctionNo.setText("Write off / OTS Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(lblOTSSanctionNo, gridBagConstraints);

        txtOTSSanctionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(txtOTSSanctionNo, gridBagConstraints);

        lblRemarks.setText("Remarks, if any");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(txtRemarks, gridBagConstraints);

        tdtOTSSanctionDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtOTSSanctionDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(tdtOTSSanctionDt, gridBagConstraints);

        lblOTSSanctionDt.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(lblOTSSanctionDt, gridBagConstraints);

        lblOTSSanctionedBy.setText("Sanctioned By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(lblOTSSanctionedBy, gridBagConstraints);

        cboOTSSanctionedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOTSSanctionedBy.setPopupWidth(100);
        cboOTSSanctionedBy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboOTSSanctionedByItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(cboOTSSanctionedBy, gridBagConstraints);

        lblOTSAmount.setText("Write off / OTS Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(lblOTSAmount, gridBagConstraints);

        txtOTSAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOTSAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOTSAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOTSDetails.add(txtOTSAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestGroup.add(panOTSDetails, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder("A/c wise Subsidy Due Details"));
        panGroupData.setMinimumSize(new java.awt.Dimension(850, 200));
        panGroupData.setPreferredSize(new java.awt.Dimension(850, 200));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpSubsidyDetails.setPreferredSize(new java.awt.Dimension(454, 344));

        tblSubsidyDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Account No", "Name", "Release No", "Total Subsidy Amt", "Subsidy Amt Adjusted", "Balance Subsidy  Due", "Recovery From Cust", "Write off Amt", "OTS Amount"
            }
        ));
        tblSubsidyDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblSubsidyDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblSubsidyDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblSubsidyDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSubsidyDetailsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSubsidyDetailsMouseReleased(evt);
            }
        });
        tblSubsidyDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblSubsidyDetailsFocusLost(evt);
            }
        });
        tblSubsidyDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblSubsidyDetailsKeyReleased(evt);
            }
        });
        srpSubsidyDetails.setViewportView(tblSubsidyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGroupData.add(srpSubsidyDetails, gridBagConstraints);

        panTotal.setMinimumSize(new java.awt.Dimension(530, 28));
        panTotal.setPreferredSize(new java.awt.Dimension(530, 28));
        panTotal.setLayout(new java.awt.GridBagLayout());

        lblTotalPayment.setForeground(new java.awt.Color(0, 0, 255));
        lblTotalPayment.setText("Total : ");
        lblTotalPayment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panTotal.add(lblTotalPayment, gridBagConstraints);

        txtTotalBalSubsidyAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalBalSubsidyAmount.setMinimumSize(new java.awt.Dimension(115, 21));
        txtTotalBalSubsidyAmount.setPreferredSize(new java.awt.Dimension(115, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(txtTotalBalSubsidyAmount, gridBagConstraints);

        txtTotalsubAdjAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalsubAdjAmount.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotalsubAdjAmount.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(txtTotalsubAdjAmount, gridBagConstraints);

        txtTotalSubsidyAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalSubsidyAmount.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotalSubsidyAmount.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(txtTotalSubsidyAmount, gridBagConstraints);

        txtTotalTodayAdjustment.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalTodayAdjustment.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotalTodayAdjustment.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(txtTotalTodayAdjustment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panGroupData.add(panTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 12;
        panInterestGroup.add(panGroupData, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(850, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(850, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnDisplay.setBackground(new java.awt.Color(204, 204, 204));
        btnDisplay.setForeground(new java.awt.Color(255, 0, 0));
        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Right_Arrow.gif"))); // NOI18N
        btnDisplay.setText("Display ");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panProcess.add(btnDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 12;
        panInterestGroup.add(panProcess, gridBagConstraints);

        panFinancialYearTable.setMinimumSize(new java.awt.Dimension(280, 165));
        panFinancialYearTable.setPreferredSize(new java.awt.Dimension(280, 165));
        panFinancialYearTable.setLayout(new java.awt.GridBagLayout());

        tblFinYearWise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Financial Year", "Total Outstanding", "Over Due Amount"
            }
        ));
        tblFinYearWise.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblFinYearWise.setSelectionForeground(new java.awt.Color(10, 36, 106));
        srpTable_FinYearWise.setViewportView(tblFinYearWise);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFinancialYearTable.add(srpTable_FinYearWise, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panFinancialYearTable.add(btnViewDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panInterestGroup.add(panFinancialYearTable, gridBagConstraints);

        tabInterestSubsidyAdjustment.addTab("Interest Rate Group", panInterestGroup);

        panTransactionDetails.setMinimumSize(new java.awt.Dimension(500, 282));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(500, 282));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());
        tabInterestSubsidyAdjustment.addTab("Transaction Details", panTransactionDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestSubsidyAdjustment.add(tabInterestSubsidyAdjustment, gridBagConstraints);

        getContentPane().add(panInterestSubsidyAdjustment, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

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

    private void rdoInterestType_OTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_OTSActionPerformed
        // TODO add your handling code here:
        observable.resetTableValues();
        setVisibleUIData();
    }//GEN-LAST:event_rdoInterestType_OTSActionPerformed

    private void rdoInterestType_WriteOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_WriteOffActionPerformed
        // TODO add your handling code here:
        observable.resetTableValues();
        setVisibleUIData();
    }//GEN-LAST:event_rdoInterestType_WriteOffActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
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
            singleAuthorizeMap.put("ADJUSTMENT_NO", observable.getSubsidyAdjustNo());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected()) {
                singleAuthorizeMap.put("PRINCIPAL_SUBSIDY","PRINCIPAL_SUBSIDY");
            }
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, prodType);
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSubsidyTransAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String prodType) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (!(prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) {
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (viewType == "FROM_ACT_NO") {
                txtFromAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                txtToAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
            } else if (viewType == "TO_ACT_NO") {
                txtToAccountNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
            } else if (viewType == "FROM_RELEASE_NO") {
                txtFromReleaseNo.setText(CommonUtil.convertObjToStr(hashMap.get("RELEASE_NO")));
            } else if (viewType == "TO_RELEASE_NO") {
                txtToReleaseNo.setText(CommonUtil.convertObjToStr(hashMap.get("RELEASE_NO")));
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                this.setButtonEnableDisable();
                observable.setSubsidyAdjustNo(CommonUtil.convertObjToStr(hashMap.get("ADJUSTMENT_NO")));
                observable.getData(hashMap);
                update();
                ClientUtil.enableDisable(this, false);
                tblSubsidyDetails.setModel(observable.getTblSubsidyDetails());
                srpSubsidyDetails.setVisible(true);
                setSizeTableDataEdit();
                panTotal.setVisible(true);
                calcTableTotalAmountEdit();
                btnDisplayActionPerformed(null);
                btnViewDetails.setEnabled(false);
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                observable.setSubsidyAdjustNo(CommonUtil.convertObjToStr(hashMap.get("ADJUSTMENT_NO")));
                observable.getData(hashMap);
                update();
                tblSubsidyDetails.setModel(observable.getTblSubsidyDetails());
                srpSubsidyDetails.setVisible(true);
                setSizeTableDataEdit();
                panTotal.setVisible(true);
                calcTableTotalAmountEdit();
                btnDisplayActionPerformed(null);
                btnViewDetails.setEnabled(false);
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this, false);
                btnEnableDisable(false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("               ");
        observable.resetForm();
        observable.resetTableValues();
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
        setVisibleUIData();
        setSizeTableData();
        btnEnableDisable(false);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransAcctNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        srpSubsidyDetails.setVisible(false);
        panTotal.setVisible(false);
        selectedRow = -1;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String intType = "";
        if (rdoInterestType_Subsidy.isSelected()) {
            intType = "S";
        } else if (rdoInterestType_Recovery.isSelected()) {
            intType = "R";
        } else if (rdoInterestType_WriteOff.isSelected()) {
            intType = "W";
        } else if (rdoInterestType_OTS.isSelected()) {
            intType = "O";
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (!(prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) {
                observable.setPrincipalSubsidy("N");
                if (transactionUI.getOutputTO().size() == 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                }
                if (!transactionUI.isBtnSaveTransactionDetailsFlag() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
            }else{
                observable.setPrincipalSubsidy("Y");
            }
            
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                if (!(prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
                finalList = observable.getFinalList();
                HashMap releaseMap = new HashMap();
                if (finalList != null && finalList.size() > 0) {
                    System.out.println("#$@$#@$@$@ Before FinalList : " + finalList);
                    for (int i = 0; i < finalList.size(); i++) {
                        String releaseNo = "";
                        releaseMap = (HashMap) finalList.get(i);
                        releaseNo = CommonUtil.convertObjToStr(releaseMap.get("RELEASE_NO"));
                        //SetAccount No Column
                        int actNoColumn =-1;
                        if (prodType.equals("TL")) {
                            actNoColumn = 1;
                        }else{
                            actNoColumn = 3;
                        }
                        for (int j = 0; j < tblSubsidyDetails.getRowCount(); j++) {
                            if (CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, actNoColumn)).equals(releaseNo) && !((Boolean) tblSubsidyDetails.getValueAt(j, 0)).booleanValue()) {
                                finalList.remove(i--);
                            }
                            //Setting Adjustment Amount
                            if (CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, actNoColumn)).equals(releaseNo) && ((Boolean) tblSubsidyDetails.getValueAt(j, 0)).booleanValue()) {
                                releaseMap.put("TODAYS_ADJUSTMENT_AMOUNT", "");
                                releaseMap.put("RECOVERY_FROM_CUST_AMOUNT", "");
                                releaseMap.put("WRITE_OFF_AMOUNT", "");
                                releaseMap.put("OTS_AMOUNT", "");
                                if (intType.equals("S")) {
                                    releaseMap.put("TODAYS_ADJUSTMENT_AMOUNT", CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, 7)));
                                } else if (intType.equals("R")) {
                                    releaseMap.put("RECOVERY_FROM_CUST_AMOUNT", CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, 7)));
                                } else if (intType.equals("W")) {
                                    releaseMap.put("WRITE_OFF_AMOUNT", CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, 7)));
                                } else if (intType.equals("O")) {
                                    releaseMap.put("OTS_AMOUNT", CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(j, 7)));
                                }
                            }
                        }
                    }
                    System.out.println("############# After Final List:" + finalList);
                    if (finalList != null && finalList.size() > 0) {
                        observable.setFinalList(finalList);
                        if (intType.equals("S")) {
                            if ((CommonUtil.convertObjToDouble(txtSubsidyReceivedAmt.getText()).doubleValue()
                                    != CommonUtil.convertObjToDouble(txtTotalTodayAdjustment.getText()).doubleValue())) {
                                ClientUtil.showMessageWindow("Transaction Amount and Total of Todays Adjustment Amount Should be Equal !!!");
                                return;
                            }
                        } else if (intType.equals("W") || intType.equals("O")) {
                            if ((CommonUtil.convertObjToDouble(txtOTSAmount.getText()).doubleValue()
                                    != CommonUtil.convertObjToDouble(txtTotalTodayAdjustment.getText()).doubleValue())) {
                                ClientUtil.showMessageWindow("Transaction Amount and Total of Write Off / OTS Amount Should be Equal !!!");
                                return;
                            }
                        }
                        savePerformed();
                    } else {
                        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                        return;
                    }
                }
            }else{
                //Only Delete
                savePerformed();
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() {
        updateOBFields();
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
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
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j<=1) {
                        transId = (String)transMap.get("TRANS_ID");
                        transIdList.add(transId);
                        transMode = "CASH";
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        } 
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

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
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubsidyTransEditEnquiry");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubsidyTransDelete");
        } else if (currAction.equalsIgnoreCase("FROM_RELEASE_NO") || currAction.equalsIgnoreCase("TO_RELEASE_NO")) {
            HashMap map = new HashMap();
            map.put("ACT_NUM", txtFromAccountNo.getText());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectReleaseNumberFromActNum");
        } else if (currAction.equals("FROM_ACT_NO") || currAction.equals("TO_ACT_NO")) {
            HashMap map = new HashMap();
            map.put("PRODUCT_ID", observable.getCbmProdID().getKeyForSelected().toString());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "Remittance.getAccountData" + observable.getCbmProdType().getKeyForSelected().toString());
        }
        new ViewAll(this, viewMap).show();
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        btnNewPressed = true;
        setModified(true);
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        observable.setStatus();
        btnEnableDisable(true);
        btnDisplay.setVisible(true);
        btnViewDetails.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panTotal, false);
        rdoInterestType_Subsidy.setSelected(true);
        rdoInterestType_SubsidyActionPerformed(null);
        srpSubsidyDetails.setVisible(false);
        tabInterestSubsidyAdjustment.addTab("Transaction Details", panTransactionDetails);
        srpTable_FinYearWise.setVisible(true);
        panTotal.setVisible(false);
    }//GEN-LAST:event_btnNewActionPerformed

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

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void rdoInterestType_SubsidyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_SubsidyActionPerformed
        // TODO add your handling code here:
        observable.resetTableValues();
        setVisibleUIData();
    }//GEN-LAST:event_rdoInterestType_SubsidyActionPerformed
    private void setVisibleUIData() {
        visibleADfields();
        panSubsidyProductDetails.setVisible(true);
        if (rdoInterestType_Subsidy.isSelected()) {
            panGroupInfo.setVisible(true);
            panOTSDetails.setVisible(false);
            clearOTSDetails();
        } else if (rdoInterestType_Recovery.isSelected()) {
            panGroupInfo.setVisible(false);
            panOTSDetails.setVisible(false);
            clearSubsidyReimbursement();
            clearOTSDetails();
        } else if (rdoInterestType_WriteOff.isSelected() || rdoInterestType_OTS.isSelected()) {
            panGroupInfo.setVisible(false);
            panOTSDetails.setVisible(true);
            clearSubsidyReimbursement();
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            cboProdTypeActionPerformed(null);
        }
    }

    private void clearSubsidyReimbursement() {
        tdtSubsidyReleaseDt.setDateValue("");
        txtSubsidyReceivedAmtPer.setText("");
        txtSubSidyReleaseRefNo.setText("");
        txtSubsidyReceivedAmt.setText("");
        cboAgencyName.setSelectedItem("");
    }

    private void clearRecoveryFromCust() {
        cboProdType.setSelectedItem("");
        cboProdID.setSelectedItem("");
        txtFromAccountNo.setText("");
        txtFromReleaseNo.setText("");
        txtToReleaseNo.setText("");
        txtToAccountNo.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
    }

    private void clearOTSDetails() {
        cboOTSSanctionedBy.setSelectedItem("");
        tdtOTSSanctionDt.setDateValue("");
        txtOTSSanctionNo.setText("");
        txtOTSAmount.setText("");
        txtRemarks.setText("");
    }

    public void updateOBFields() {
        if (rdoInterestType_Subsidy.isSelected()) {
            observable.setInterestType("S");
        } else if (rdoInterestType_Recovery.isSelected()) {
            observable.setInterestType("R");
        } else if (rdoInterestType_WriteOff.isSelected()) {
            observable.setInterestType("W");
        } else if (rdoInterestType_OTS.isSelected()) {
            observable.setInterestType("O");
        }
        observable.setTdtSubsidyReleaseDt(tdtSubsidyReleaseDt.getDateValue());
        observable.setTxtSubSidyReleaseRefNo(txtSubSidyReleaseRefNo.getText());
        observable.setTxtSubsidyReceivedAmt(txtSubsidyReceivedAmt.getText());
        observable.setTxtSubsidyReceivedAmtPer(txtSubsidyReceivedAmtPer.getText());
        observable.setCboAgencyName(CommonUtil.convertObjToStr(cboAgencyName.getSelectedItem()));
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        observable.setCboProdID(CommonUtil.convertObjToStr(cboProdID.getSelectedItem()));
        observable.setTxtFromAccountNo(txtFromAccountNo.getText());
        observable.setTxtFromReleaseNo(txtFromReleaseNo.getText());
        observable.setTxtToReleaseNo(txtToReleaseNo.getText());
        observable.setTxtToAccountNo(txtToAccountNo.getText());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTdtToDt(tdtToDt.getDateValue());
        observable.setTdtOTSSanctionDt(tdtOTSSanctionDt.getDateValue());
        observable.setCboOTSSanctionedBy(CommonUtil.convertObjToStr(cboOTSSanctionedBy.getSelectedItem()));
        observable.setTxtOTSSanctionNo(txtOTSSanctionNo.getText());
        observable.setTxtOTSAmount(txtOTSAmount.getText());
        observable.setTxtRemarks(txtRemarks.getText());

        //Only TL
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        if (prodType.equals("AD")) {
            observable.setRdoSubsidy("");
        } else {
            if (rdoTLSubsidyInt.isSelected()) {
                observable.setRdoSubsidy("I");
            } else {
                observable.setRdoSubsidy("P");
            }
        }
    }

    public void update() {
        String interestType = "";
        interestType = observable.getInterestType();
        if (interestType.equals("S")) {
            rdoInterestType_Subsidy.setSelected(true);
        } else if (interestType.equals("R")) {
            rdoInterestType_Recovery.setSelected(true);
        } else if (interestType.equals("W")) {
            rdoInterestType_WriteOff.setSelected(true);
        } else if (interestType.equals("O")) {
            rdoInterestType_OTS.setSelected(true);
        }
        tdtSubsidyReleaseDt.setDateValue(observable.getTdtSubsidyReleaseDt());
        txtSubSidyReleaseRefNo.setText(observable.getTxtSubSidyReleaseRefNo());
        txtSubsidyReceivedAmt.setText(observable.getTxtSubsidyReceivedAmt());
        txtSubsidyReceivedAmtPer.setText(observable.getTxtSubsidyReceivedAmtPer());
        cboAgencyName.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboAgencyName()));
        cboProdType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboProdType()));
        cboProdID.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboProdID()));
        txtFromAccountNo.setText(observable.getTxtFromAccountNo());
        txtFromReleaseNo.setText(observable.getTxtFromReleaseNo());
        txtToReleaseNo.setText(observable.getTxtToReleaseNo());
        txtToAccountNo.setText(observable.getTxtToAccountNo());
        tdtFromDt.setDateValue(observable.getTdtFromDt());
        tdtToDt.setDateValue(observable.getTdtToDt());
        tdtOTSSanctionDt.setDateValue(observable.getTdtOTSSanctionDt());
        cboOTSSanctionedBy.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboOTSSanctionedBy()));
        txtOTSSanctionNo.setText(observable.getTxtOTSSanctionNo());
        txtOTSAmount.setText(observable.getTxtOTSAmount());
        txtRemarks.setText(observable.getTxtRemarks());

        //Only TL
        if (observable.getRdoSubsidy().equals("I")) {
            rdoTLSubsidyInt.setSelected(true);
        } else if (observable.getRdoSubsidy().equals("P")) {
            rdoTLSubsidyPrincipal.setSelected(true);
        }
        setVisibleUIData();
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        if (prodType.equals("TL")) {
            visibleTLfields();
            if (observable.getRdoSubsidy().equals("I")) {
                srpTable_FinYearWise.setVisible(true);
                tabInterestSubsidyAdjustment.addTab("Transaction Details", panTransactionDetails);
            } else {
                srpTable_FinYearWise.setVisible(false);
                tabInterestSubsidyAdjustment.remove(panTransactionDetails);
            }

        } else if (prodType.equals("AD")) {
            visibleADfields();
            srpTable_FinYearWise.setVisible(true);
            tabInterestSubsidyAdjustment.addTab("Transaction Details", panTransactionDetails);
        }
    }

    private void rdoInterestType_RecoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_RecoveryActionPerformed
        // TODO add your handling code here:
        observable.resetTableValues();
        setVisibleUIData();
    }//GEN-LAST:event_rdoInterestType_RecoveryActionPerformed

    private void tdtSubsidyReleaseDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubsidyReleaseDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtSubsidyReleaseDtFocusLost

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDtFocusLost

    private void tdtToDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtToDtFocusLost

    private void tdtOTSSanctionDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtOTSSanctionDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtOTSSanctionDtFocusLost

    private void cboOTSSanctionedByItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboOTSSanctionedByItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboOTSSanctionedByItemStateChanged

    private void btnToReleaseNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToReleaseNoActionPerformed
        // Add your handling code here:
        if (txtFromAccountNo.getText().length() > 0) {
            popUp("TO_RELEASE_NO");
        } else {
            ClientUtil.showMessageWindow("From Account Number Should not be Empty !!!");
            return;
        }
    }//GEN-LAST:event_btnToReleaseNoActionPerformed

    private void btnFromReleaseNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromReleaseNoActionPerformed
        // Add your handling code here:
        if (txtFromAccountNo.getText().length() > 0) {
            popUp("FROM_RELEASE_NO");
        } else {
            ClientUtil.showMessageWindow("From Account Number Should not be Empty !!!");
            return;
        }
    }//GEN-LAST:event_btnFromReleaseNoActionPerformed

    private void btnToAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountNoActionPerformed
        // Add your handling code here:
        if (cboProdType.getSelectedIndex() > 0 && cboProdID.getSelectedIndex() > 0) {
            popUp("TO_ACT_NO");
        } else {
            ClientUtil.showMessageWindow("Product Type And Product ID Should not be Empty !!!");
            return;
        }
    }//GEN-LAST:event_btnToAccountNoActionPerformed

    private void btnFromAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountNoActionPerformed
        // Add your handling code here:
        if (cboProdType.getSelectedIndex() > 0 && cboProdID.getSelectedIndex() > 0) {
            popUp("FROM_ACT_NO");
        } else {
            ClientUtil.showMessageWindow("Product Type And Product ID Should not be Empty !!!");
            return;
        }
    }//GEN-LAST:event_btnFromAccountNoActionPerformed

    private void tblSubsidyDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubsidyDetailsMouseClicked
        // TODO add your handling code here:
        if (tblSubsidyDetails.getRowCount() > 0 && tblSubsidyDetails.getSelectedColumn() == 0
                && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String st = CommonUtil.convertObjToStr(tblSubsidyDetails.getValueAt(tblSubsidyDetails.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblSubsidyDetails.setValueAt(new Boolean(false), tblSubsidyDetails.getSelectedRow(), 0);
            } else {
                tblSubsidyDetails.setValueAt(new Boolean(true), tblSubsidyDetails.getSelectedRow(), 0);
            }
            calcTableTotalAmount();
        }
        if (tblSubsidyDetails.getRowCount() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            selectedRow = tblSubsidyDetails.getSelectedRow();
            System.out.println("########### selectedRow : "+selectedRow);
        }
    }//GEN-LAST:event_tblSubsidyDetailsMouseClicked
    private void amountValidation(){
        if (tblSubsidyDetails.getRowCount() > 0 && selectedRow>=0){
            if(CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(selectedRow, 6)).doubleValue()<
                 CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(selectedRow, 7)).doubleValue()){
                ClientUtil.showMessageWindow("Adjustment Amount Should not be Greater than Balance Subsidy Due !!!");
                tblSubsidyDetails.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")),selectedRow, 7);
                return;
            }
        }
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            populateProdId();
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (prodType.equals("TL")) {
                visibleTLfields();
                if(rdoTLSubsidyPrincipal.isSelected()){
                    rdoTLSubsidyPrincipalActionPerformed(null);
                }else{
                    srpTable_FinYearWise.setVisible(true);
                }
            } else if (prodType.equals("AD")) {
                visibleADfields();
                srpTable_FinYearWise.setVisible(true);
                btnDisplay.setVisible(true);
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void visibleTLfields() {
        panTLSubsidy.setVisible(true);
        lblFromDt.setVisible(false);
        tdtFromDt.setVisible(false);
        lblToDt.setVisible(false);
        tdtToDt.setVisible(false);
        lblFromReleaseNo.setVisible(false);
        txtFromReleaseNo.setVisible(false);
        btnFromReleaseNo.setVisible(false);
        lblToReleaseNo.setVisible(false);
        txtToReleaseNo.setVisible(false);
        btnToReleaseNo.setVisible(false);
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        txtFromReleaseNo.setText("");
        txtToReleaseNo.setText("");
    }

    private void visibleADfields() {
        lblFromDt.setVisible(true);
        tdtFromDt.setVisible(true);
        lblToDt.setVisible(true);
        tdtToDt.setVisible(true);
        lblFromReleaseNo.setVisible(true);
        txtFromReleaseNo.setVisible(true);
        btnFromReleaseNo.setVisible(true);
        lblToReleaseNo.setVisible(true);
        txtToReleaseNo.setVisible(true);
        btnToReleaseNo.setVisible(true);
        panTLSubsidy.setVisible(false);
    }

    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        // TODO add your handling code here:
        try {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if ((tblFinYearWise.getRowCount() > 0 || (prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (tblFinYearWise.getSelectedRow() < 0 && !(prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) {
                    ClientUtil.showMessageWindow("Please Select Particular Financial Year Row !!! ");
                    return;
                }
                ClientUtil.clearAll(panTotal);
                observable.resetSubsidyTableValues();
                String StartYear = "";
                String endYear = "";
                String intType = "";
                HashMap whereMap = new HashMap();
                if (tblFinYearWise.getRowCount() > 0) {
                    String financialYear = CommonUtil.convertObjToStr(tblFinYearWise.getValueAt(tblFinYearWise.getSelectedRow(), 0));
                    StartYear = financialYear.substring(0, 4);
                    endYear = financialYear.substring(5, 9);
                }
                whereMap.put("KCC_ACT_NUM", txtFromAccountNo.getText());
                whereMap.put("START_YEAR", StartYear);
                whereMap.put("END_YEAR", endYear);
                if (txtFromReleaseNo.getText().length() > 0) {
                    whereMap.put("FROM_RELEASE_NO", txtFromReleaseNo.getText());
                }
                if (txtToReleaseNo.getText().length() > 0) {
                    whereMap.put("TO_RELEASE_NO", txtToReleaseNo.getText());
                }
                //Only TL
                if (prodType.equals("TL")) {
                    whereMap.put("FROM_ACT_NO", txtFromAccountNo.getText());
                    whereMap.put("TO_ACT_NO", txtToAccountNo.getText());
                    whereMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProdID.getModel()).getKeyForSelected()));
                    if (rdoTLSubsidyInt.isSelected()) {
                        whereMap.put("SUBSIDY_TYPE", "I");
                    }else{
                        whereMap.put("SUBSIDY_TYPE", "P");
                    }
                }

                //Set Interest Type
                if (rdoInterestType_Subsidy.isSelected()) {
                    intType = "S";
                    txtTotalTodayAdjustment.setVisible(true);
                    whereMap.put("INSTITUTION_NAME", CommonUtil.convertObjToStr(cboAgencyName.getSelectedItem()));
                } else if (rdoInterestType_Recovery.isSelected()) {
                    intType = "R";
                    whereMap.put("INT_TYPE", "R");
                } else if (rdoInterestType_WriteOff.isSelected()) {
                    intType = "W";
                    whereMap.put("INT_TYPE", "W");
                } else if (rdoInterestType_OTS.isSelected()) {
                    intType = "O";
                    whereMap.put("INT_TYPE", "O");
                }
                //Insert Table Data
                
                if (prodType.equals("AD")) {
                    observable.insertTableDataAD(whereMap, intType);
                } else if (prodType.equals("TL")) {
                    observable.insertTableDataTL(whereMap, intType);
                }
                tblSubsidyDetails.setModel(observable.getTblSubsidyDetails());
                setSizeTableData();
                if (tblSubsidyDetails.getRowCount() <= 0) {
                    ClientUtil.showMessageWindow("List Is Empty !!! ");
                    srpSubsidyDetails.setVisible(false);
                    panTotal.setVisible(false);
                    return;
                } else {
                    srpSubsidyDetails.setVisible(true);
                    ClientUtil.enableDisable(panInterestGroup, false);
                    panTotal.setVisible(true);
                }
            } else {
                ClientUtil.showMessageWindow("Financial Year Table is Empty !!! ");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnViewDetailsActionPerformed
    private void calcTableTotalAmount() {
        double totalSubsidyAmount = 0.0;
        double subsidyAmountAdjusted = 0.0;
        double balanceSubsidyDue = 0.0;
        double todaysAdjustment = 0.0;
        for (int i = 0; i < tblSubsidyDetails.getRowCount(); i++) {
            if (((Boolean) tblSubsidyDetails.getValueAt(i, 0)).booleanValue()) {
                totalSubsidyAmount += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 4));
                subsidyAmountAdjusted += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 5));
                balanceSubsidyDue += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 6));
                todaysAdjustment += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 7));
            }
        }
        txtTotalSubsidyAmount.setText(String.valueOf(totalSubsidyAmount));
        txtTotalsubAdjAmount.setText(String.valueOf(subsidyAmountAdjusted));
        txtTotalBalSubsidyAmount.setText(String.valueOf(balanceSubsidyDue));
        txtTotalTodayAdjustment.setText(String.valueOf(todaysAdjustment));
    }
    
    private void calcTableTotalAmountEdit() {
        double totalSubsidyAmount = 0.0;
        double subsidyAmountAdjusted = 0.0;
        double balanceSubsidyDue = 0.0;
        double todaysAdjustment = 0.0;
        for (int i = 0; i < tblSubsidyDetails.getRowCount(); i++) {
                totalSubsidyAmount += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 3));
                subsidyAmountAdjusted += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 4));
                balanceSubsidyDue += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 5));
                todaysAdjustment += CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 6));
        }
        txtTotalSubsidyAmount.setText(String.valueOf(totalSubsidyAmount));
        txtTotalsubAdjAmount.setText(String.valueOf(subsidyAmountAdjusted));
        txtTotalBalSubsidyAmount.setText(String.valueOf(balanceSubsidyDue));
        txtTotalTodayAdjustment.setText(String.valueOf(todaysAdjustment));
    }
    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        if (txtFromAccountNo.getText().length() > 0) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (!(prodType.equals("TL") && rdoTLSubsidyPrincipal.isSelected())) {
                //Subsidy Reimbursement
                if (rdoInterestType_Subsidy.isSelected()) {
                    if (cboAgencyName.getSelectedIndex() <= 0) {
                        ClientUtil.showMessageWindow("Name of the Agengy Should not be Empty !!! ");
                        return;
                    }
                    if (txtSubsidyReceivedAmt.getText().length() <= 0) {
                        ClientUtil.showMessageWindow("Subsidy Amount Received Should not be Empty !!! ");
                        return;
                    }
                }
                //Write Off Or OTS
                if (rdoInterestType_WriteOff.isSelected() || rdoInterestType_OTS.isSelected()) {
                    if (cboOTSSanctionedBy.getSelectedIndex() <= 0) {
                        ClientUtil.showMessageWindow("Sanctioned By Should not be Empty !!! ");
                        return;
                    }
                    if (txtOTSAmount.getText().length() <= 0) {
                        ClientUtil.showMessageWindow("Write Off / OTS Amount Should not be Empty !!! ");
                        return;
                    }
                }
                HashMap whereMap = new HashMap();
                if (rdoInterestType_Subsidy.isSelected()) {
                    whereMap.put("INSTITUTION_NAME", CommonUtil.convertObjToStr(cboAgencyName.getSelectedItem()));
                }

                if (prodType.equals("TL")) {
                    if (!(rdoTLSubsidyInt.isSelected() || rdoTLSubsidyPrincipal.isSelected())) {
                        ClientUtil.showMessageWindow("Please Select Interest Subsidy/Principal Subsidy !!! ");
                        return;
                    }
                    if (txtFromAccountNo.getText().length() <= 0 || txtToAccountNo.getText().length() <= 0) {
                        ClientUtil.showMessageWindow("From Account Number And To Account Number Should not be Empty !!! ");
                        return;
                    }
                    whereMap.put("FROM_ACT_NO", txtFromAccountNo.getText());
                    whereMap.put("TO_ACT_NO", txtToAccountNo.getText());
                    whereMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProdID.getModel()).getKeyForSelected()));
                } else {
                    whereMap.put("KCC_ACT_NUM", txtFromAccountNo.getText());
                }
                observable.populateFinYearWiseTable(whereMap, prodType);
                tblFinYearWise.setModel(observable.getTblFinYearWise());
                if (tblFinYearWise.getRowCount() > 0) {
                    btnDisplay.setEnabled(false);
                    btnViewDetails.setEnabled(true);
                    ClientUtil.enableDisable(panInterestGroup, false);
                } else {
                    ClientUtil.showMessageWindow("List Is Empty !!! ");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("From Account No Should not be Empty !!! ");
            return;
        }
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void rdoTLSubsidyIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTLSubsidyIntActionPerformed
        // TODO add your handling code here:
        rdoTLSubsidyInt.setSelected(true);
        rdoTLSubsidyPrincipal.setSelected(false);
        tabInterestSubsidyAdjustment.addTab("Transaction Details", panTransactionDetails);
        btnDisplay.setVisible(true);
        btnViewDetails.setEnabled(false);
        srpTable_FinYearWise.setVisible(true);
        if(txtSubsidyReceivedAmt.getText().length()>0){
            txtSubsidyReceivedAmtFocusLost(null);
        }else if(txtSubsidyReceivedAmt.getText().length()>0){
            txtOTSAmountFocusLost(null);
        }
    }//GEN-LAST:event_rdoTLSubsidyIntActionPerformed

    private void rdoTLSubsidyPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTLSubsidyPrincipalActionPerformed
        // TODO add your handling code here:
        rdoTLSubsidyInt.setSelected(false);
        rdoTLSubsidyPrincipal.setSelected(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        btnDisplay.setVisible(false);
        btnViewDetails.setEnabled(true);
        srpTable_FinYearWise.setVisible(false);
        tabInterestSubsidyAdjustment.remove(panTransactionDetails);
    }//GEN-LAST:event_rdoTLSubsidyPrincipalActionPerformed

    private void tblSubsidyDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSubsidyDetailsFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setTotalAmount();
        }
    }//GEN-LAST:event_tblSubsidyDetailsFocusLost

    private void txtSubsidyReceivedAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubsidyReceivedAmtFocusLost
        // TODO add your handling code here:
        if (txtSubsidyReceivedAmt.getText().length() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setCallingTransType("TRANSFER");
                transactionUI.setCallingAmount(txtSubsidyReceivedAmt.getText());
            }
        }
    }//GEN-LAST:event_txtSubsidyReceivedAmtFocusLost

    private void tblSubsidyDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSubsidyDetailsKeyReleased
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setTotalAmount();
        }
    }//GEN-LAST:event_tblSubsidyDetailsKeyReleased

    private void tblSubsidyDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubsidyDetailsMouseReleased
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setTotalAmount();
        }
    }//GEN-LAST:event_tblSubsidyDetailsMouseReleased

    private void txtOTSAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOTSAmountFocusLost
        // TODO add your handling code here:
        if (txtOTSAmount.getText().length() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setCallingTransType("TRANSFER");
                transactionUI.setCallingAmount(txtOTSAmount.getText());
            }
        }
    }//GEN-LAST:event_txtOTSAmountFocusLost

    private void txtFromReleaseNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromReleaseNoFocusLost
        // TODO add your handling code here:
        if (txtFromAccountNo.getText().length() > 0) {
            if (txtFromReleaseNo.getText().length() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("ACT_NUM", txtFromAccountNo.getText());
                whereMap.put("RELEASE_NO", txtFromReleaseNo.getText());
                List releaseLst = ClientUtil.executeQuery("getSelectReleaseNumberFromActNum", whereMap);
                if (releaseLst != null && releaseLst.size() > 0) {
                    viewType = "FROM_RELEASE_NO";
                    whereMap = (HashMap) releaseLst.get(0);
                    fillData(whereMap);
                    releaseLst = null;
                    whereMap = null;
                } else {
                    ClientUtil.showMessageWindow("Invalid Release Number !!! ");
                    txtFromReleaseNo.setText("");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("From Account Number Should not be Empty.. !!! ");
            txtFromReleaseNo.setText("");
            return;
        }
    }//GEN-LAST:event_txtFromReleaseNoFocusLost

    private void txtToReleaseNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToReleaseNoFocusLost
        // TODO add your handling code here:
        if (txtFromAccountNo.getText().length() > 0) {
            if (txtToReleaseNo.getText().length() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("ACT_NUM", txtFromAccountNo.getText());
                whereMap.put("RELEASE_NO", txtToReleaseNo.getText());
                List releaseLst = ClientUtil.executeQuery("getSelectReleaseNumberFromActNum", whereMap);
                if (releaseLst != null && releaseLst.size() > 0) {
                    viewType = "TO_RELEASE_NO";
                    whereMap = (HashMap) releaseLst.get(0);
                    fillData(whereMap);
                    releaseLst = null;
                    whereMap = null;
                } else {
                    ClientUtil.showMessageWindow("Invalid Release Number !!! ");
                    txtToReleaseNo.setText("");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("From Account Number Should not be Empty.. !!! ");
            txtToReleaseNo.setText("");
            return;
        }
    }//GEN-LAST:event_txtToReleaseNoFocusLost

    private void setTotalAmount() {
        double adjustedAmt = 0.0;
        amountValidation();
        if (tblSubsidyDetails.getRowCount() > 0) {
            for (int i = 0; i < tblSubsidyDetails.getRowCount(); i++) {
                if (((Boolean) tblSubsidyDetails.getValueAt(i, 0)).booleanValue()) {
                    adjustedAmt = adjustedAmt + CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 7)).doubleValue();
                }
            }
        }
        txtTotalTodayAdjustment.setText(CommonUtil.convertObjToStr(new Double(adjustedAmt)));
        //set Transaction Amount For Common Trans Screen
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (rdoInterestType_Recovery.isSelected())) {
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingTransType("TRANSFER");
            transactionUI.setCallingAmount(txtTotalTodayAdjustment.getText());
        }
    }

    private void populateProdId() {
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        observable.setCbmProdId(prodType);
        cboProdID.setModel(observable.getCbmProdID());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new InterestSubsidyAdjustmentUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromAccountNo;
    private com.see.truetransact.uicomponent.CButton btnFromReleaseNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToAccountNo;
    private com.see.truetransact.uicomponent.CButton btnToReleaseNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewDetails;
    private com.see.truetransact.uicomponent.CComboBox cboAgencyName;
    private com.see.truetransact.uicomponent.CComboBox cboOTSSanctionedBy;
    private com.see.truetransact.uicomponent.CComboBox cboProdID;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblFromAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblFromReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOTSAmount;
    private com.see.truetransact.uicomponent.CLabel lblOTSSanctionDt;
    private com.see.truetransact.uicomponent.CLabel lblOTSSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblOTSSanctionedBy;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubSidyReleaseRefNo;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyReceivedAmt;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyReceivedAmtPer;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyReleaseDt;
    private com.see.truetransact.uicomponent.CLabel lblToAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CLabel lblToReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panFinancialYearTable;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panGroupInfo;
    private com.see.truetransact.uicomponent.CPanel panInterestGroup;
    private com.see.truetransact.uicomponent.CPanel panInterestSubsidyAdjustment;
    private com.see.truetransact.uicomponent.CPanel panModeOfOpening;
    private com.see.truetransact.uicomponent.CPanel panOTSDetails;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubsidyProductDetails;
    private com.see.truetransact.uicomponent.CPanel panTLSubsidy;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTLSubsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_OTS;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Recovery;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Subsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_WriteOff;
    private com.see.truetransact.uicomponent.CRadioButton rdoTLSubsidyInt;
    private com.see.truetransact.uicomponent.CRadioButton rdoTLSubsidyPrincipal;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpSubsidyDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_FinYearWise;
    private com.see.truetransact.uicomponent.CTabbedPane tabInterestSubsidyAdjustment;
    private com.see.truetransact.uicomponent.CTable tblFinYearWise;
    private com.see.truetransact.uicomponent.CTable tblSubsidyDetails;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtOTSSanctionDt;
    private com.see.truetransact.uicomponent.CDateField tdtSubsidyReleaseDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtFromReleaseNo;
    private com.see.truetransact.uicomponent.CTextField txtOTSAmount;
    private com.see.truetransact.uicomponent.CTextField txtOTSSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSubSidyReleaseRefNo;
    private com.see.truetransact.uicomponent.CTextField txtSubsidyReceivedAmt;
    private com.see.truetransact.uicomponent.CTextField txtSubsidyReceivedAmtPer;
    private com.see.truetransact.uicomponent.CTextField txtToAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtToReleaseNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalBalSubsidyAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalSubsidyAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalTodayAdjustment;
    private com.see.truetransact.uicomponent.CTextField txtTotalsubAdjAmount;
    // End of variables declaration//GEN-END:variables
}
