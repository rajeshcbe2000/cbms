/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositLienUI.java
 *
 * Created on May 25, 2004, 5:18 PM
 */
package com.see.truetransact.ui.deposit.lien;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  Pinky
 */
public class DepositLienUI extends CInternalFrame implements Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    private DepositLienOB observable;
    //private DepositLienRB resourceBundle;
    private DepositLienMRB objMandatoryRB;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.lien.DepositLienRB", ProxyParameters.LANGUAGE);
    private int viewType = -1;
    private final int DEPOSIT_ACCT = 100;
    private final int LIEN_ACT_NUM = 101;
    private boolean _intLienNew;
    private boolean lienChanged = false;
    private boolean callFromOtherModule = false;
    private boolean closingFlag = false;
    private boolean loanLienDisable = false;
    private boolean fromLoanScreen = false;
    int rowSelected = -1;
    private boolean depositOldNo = true;
    private boolean isMds = false;
    private Date currDt = null;
    /** Creates new form BeanForm */
    public DepositLienUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetUp();
    }

    private void initSetUp() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panDepositDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panLienInfo);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panLoanotherSociety);
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        initComponentData();
        panLienEnableDisable(false);
        panLienEnableDisable(false);
        btnDelete.setEnabled(false);
        panLoanotherSociety.setVisible(false);
        btnUnLien.setVisible(true);
    }
    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */

    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDepositNo.setName("btnDepositNo");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnLienActNum.setName("btnLienActNum");
        btnLienDelete.setName("btnLienDelete");
        btnLienNew.setName("btnLienNew");
        btnLienSave.setName("btnLienSave");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnUnLien.setName("btnUnLien");
        cboCreditType.setName("cboCreditType");
        cboSubDepositNo.setName("cboSubDepositNo");
        cboLienProductID.setName("cboLienProductID");
        cboProductID.setName("cboProductID");
        lblAccountHD.setName("lblAccountHD");
        lblAccountHDValue.setName("lblAccountHDValue");
        lblClearBalance.setName("lblClearBalance");
        lblClearBalanceValue.setName("lblClearBalanceValue");
        lblCreditLienAccount.setName("lblCreditLienAccount");
        lblCustomerID.setName("lblCustomerID");
        lblCustomerIDValue.setName("lblCustomerIDValue");
//      lblCustomerName.setName("lblCustomerName");
        lblCustomerNameValue.setName("lblCustomerNameValue");
        lblDepositNo.setName("lblDepositNo");
        lblLienAccHDValue.setName("lblLienAccHDValue");
        lblLienAccountHead.setName("lblLienAccountHead");
        lblLienAccountNumber.setName("lblLienAccountNumber");
        lblLienAmount.setName("lblLienAmount");
        lblLienCustId.setName("lblLienCustId");
        lblLienCustIdValue.setName("lblLienCustIdValue");
        lblLienDate.setName("lblLienDate");
        lblLienProductID.setName("lblLienProductID");
        lblLienSum.setName("lblLienSum");
        lblLienSumValue.setName("lblLienSumValue");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblRemark.setName("lblRemark");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblSubDepositNo.setName("lblSubDepositNo");
        mbrMain.setName("mbrMain");
        panCustomer.setName("panCustomer");
        panDeposit.setName("panDeposit");
        panDepositDetails.setName("panDepositDetails");
        panDepositNo.setName("panDepositNo");
        panLien.setName("panLien");
        panLienActNum.setName("panLienActNum");
        panLienButtons.setName("panLienButtons");
        panLienDetail.setName("panLienDetail");
        panLienInfo.setName("panLienInfo");
        panLienTable.setName("panLienTable");
        panStatus.setName("panStatus");
        sptDeposit.setName("sptDeposit");
        srpLien.setName("srpLien");
        tblLien.setName("tblLien");
        tdtLienDate.setName("tdtLienDate");
        txtDepositNo.setName("txtDepositNo");
        txtLienActNum.setName("txtLienActNum");
        txtLienAmount.setName("txtLienAmount");
        txtRemark.setName("txtRemark");
        lblDepositAmt.setName("lblDepositAmt");
        lblDepositAmtValue.setName("lblDepositAmtValue");
        lblFreezeSum.setName("lblFreezeSum");
        lblFreezeSumValue.setName("lblFreezeSumValue");
        lblShadowLien.setName("lblShadowLien");
        lblShadowLienValue.setName("lblShadowLienValue");
        lblDepositLienSLNO.setName("lblDepositLienSLNO");
        lblDepositLienDesc.setName("lblDepositLienDesc");
        lblLoanOtherSocietyLoanType.setName("lblLoanOtherSocietyLoanType");
        lblLLoanOtherSocietyLienAcNo.setName("lblLLoanOtherSocietyLienAcNo");
        lblLoanOtherSocietyLienCustName.setName("lblLoanOtherSocietyLienCustName");
        lblLoanOtherSocietyLienAmount.setName("lblLoanOtherSocietyLienAmount");
        lblLoanOtherSocietyLienDate.setName("lblLoanOtherSocietyLienDate");
        lblLoanOtherSocietyRemark.setName("lblLoanOtherSocietyRemark");
        panLoanotherSociety.setName("panLoanotherSociety");
        btnLoanOtherSocietyLienNew.setName("btnLoanOtherSocietyLienNew");
        btnLoanOtherSocietyLienSave.setName("btnLoanOtherSocietyLienSave");
        btnLoanOtherSocietyLienDelete.setName("btnLoanOtherSocietyLienDelete");
        btnLoanOtherSocietyUnLien.setName("btnLoanOtherSocietyUnLien");
        cboLienLoanType.setName("cboLienLoanType");
        txtLoanOtherSocietyLienAcNo.setName("txtLoanOtherSocietyLienAcNo");
        txtLoanOtherSocietyLienCustName.setName("txtLoanOtherSocietyLienCustName");
        txtLoanOtherSocietyLienAmount.setName("txtLoanOtherSocietyLienAmount");
        tdtLoanOtherSocietyLienDate.setName("tdtLoanOtherSocietyLienDate");
        txtLoanOtherSocietyRemark.setName("txtLoanOtherSocietyRemark");
    }
    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */

    private void internationalize() {
        resourceBundle = new DepositLienRB();
        btnLienSave.setText(resourceBundle.getString("btnLienSave"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblLienAccountNumber.setText(resourceBundle.getString("lblLienAccountNumber"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSubDepositNo.setText(resourceBundle.getString("lblSubDepositNo"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblLienAmount.setText(resourceBundle.getString("lblLienAmount"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblLienSum.setText(resourceBundle.getString("lblLienSum"));
        lblLienProductID.setText(resourceBundle.getString("lblLienProductID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnLienDelete.setText(resourceBundle.getString("btnLienDelete"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblLienCustId.setText(resourceBundle.getString("lblLienCustId"));
        lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblLienCustIdValue.setText(resourceBundle.getString("lblLienCustIdValue"));
        lblClearBalance.setText(resourceBundle.getString("lblClearBalance"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblCustomerIDValue.setText(resourceBundle.getString("lblCustomerIDValue"));
//      lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblAccountHDValue.setText(resourceBundle.getString("lblAccountHDValue"));
        btnLienNew.setText(resourceBundle.getString("btnLienNew"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnUnLien.setText(resourceBundle.getString("btnUnLien"));
        lblCreditLienAccount.setText(resourceBundle.getString("lblCreditLienAccount"));
        lblRemark.setText(resourceBundle.getString("lblRemark"));
        btnLienActNum.setText(resourceBundle.getString("btnLienActNum"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblAccountHD.setText(resourceBundle.getString("lblAccountHD"));
        lblCustomerNameValue.setText(resourceBundle.getString("lblCustomerNameValue"));
        lblLienAccountHead.setText(resourceBundle.getString("lblLienAccountHead"));
        lblLienAccHDValue.setText(resourceBundle.getString("lblLienAccHDValue"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblClearBalanceValue.setText(resourceBundle.getString("lblClearBalanceValue"));
        lblLienDate.setText(resourceBundle.getString("lblLienDate"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnDepositNo.setText(resourceBundle.getString("btnDepositNo"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblLienSumValue.setText(resourceBundle.getString("lblLienSumValue"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));

        lblDepositAmt.setText(resourceBundle.getString("lblDepositAmt"));
        lblFreezeSum.setText(resourceBundle.getString("lblFreezeSum"));
        lblFreezeSumValue.setText(resourceBundle.getString("lblFreezeSumValue"));
        lblDepositAmtValue.setText(resourceBundle.getString("lblDepositAmtValue"));
        lblShadowLien.setText(resourceBundle.getString("lblShadowLien"));
        lblShadowLienValue.setText(resourceBundle.getString("lblShadowLienValue"));
        lblDepositLienSLNO.setText(resourceBundle.getString("lblDepositLienSLNO"));
        lblDepositLienDesc.setText(resourceBundle.getString("lblDepositLienDesc"));
    }
    /* Auto Generated Method - setHelpMessage()
    This method shows tooltip help for all the input fields
    available in the UI. It needs the Mandatory Resource Bundle
    object. Help display Label name should be lblMsg. */

    public void setHelpMessage() {
        objMandatoryRB = new DepositLienMRB();
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        txtDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositNo"));
        cboSubDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubDepositNo"));
        tdtLienDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLienDate"));
        txtLienAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLienAmount"));
        txtRemark.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemark"));
        cboLienProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLienProductID"));
        txtLienActNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLienActNum"));
        cboCreditType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditType"));
        tdtLoanOtherSocietyLienDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanOtherSocietyLienDate"));
    }

    private void initComponentData() {
        this.cboProductID.setModel(observable.getCbmProductId());
        this.cboLienLoanType.setModel(observable.getCbmLOSLoanType());
        this.cboLienProductID.setModel(observable.getCbmLienProductId());
        this.cboCreditType.setModel(observable.getCbmCreditType());
        this.tblLien.setModel(observable.getTbmLien());
        chkDepositUnlien.setEnabled(false);

    }

    private void setObservable() {
        observable = DepositLienOB.getInstance();
        observable.addObserver(this);
    }

    public void setUp(int actionType, boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);

        observable.setActionType(actionType);
        observable.setStatus();
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        // btnDelete.setEnabled(btnNew.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());

        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        //        mitDelete.setEnabled(btnDelete.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void enableDisableButtons(boolean enableDisable) {
        this.btnDepositNo.setEnabled(enableDisable);
        this.btnLienNew.setEnabled(enableDisable);
        btnLoanOtherSocietyLienNew.setEnabled(enableDisable);
    }

    private void setMaxLength() {
        this.txtRemark.setMaxLength(128);
        txtLienActNum.setAllowAll(true);
        txtDepositNo.setAllowAll(true);
        txtLoanOtherSocietyLienAcNo.setAllowAll(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panLien = new com.see.truetransact.uicomponent.CPanel();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        panDeposit = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHD = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHDValue = new com.see.truetransact.uicomponent.CLabel();
        panDepositNo = new com.see.truetransact.uicomponent.CPanel();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        chkLoanwithothersociety = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositUnlien = new com.see.truetransact.uicomponent.CCheckBox();
        panCustomer = new com.see.truetransact.uicomponent.CPanel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceValue = new com.see.truetransact.uicomponent.CLabel();
        lblLienSum = new com.see.truetransact.uicomponent.CLabel();
        lblLienSumValue = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeSum = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeSumValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmtValue = new com.see.truetransact.uicomponent.CLabel();
        cboSubDepositNo = new com.see.truetransact.uicomponent.CComboBox();
        lblSubDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblShadowLien = new com.see.truetransact.uicomponent.CLabel();
        lblShadowLienValue = new com.see.truetransact.uicomponent.CLabel();
        sptDeposit = new com.see.truetransact.uicomponent.CSeparator();
        panLienDetail = new com.see.truetransact.uicomponent.CPanel();
        panLienTable = new com.see.truetransact.uicomponent.CPanel();
        srpLien = new com.see.truetransact.uicomponent.CScrollPane();
        tblLien = new com.see.truetransact.uicomponent.CTable();
        panLienInfo = new com.see.truetransact.uicomponent.CPanel();
        lblLienAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblLienAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblLienAmount = new com.see.truetransact.uicomponent.CLabel();
        lblLienDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLienDate = new com.see.truetransact.uicomponent.CDateField();
        txtLienAmount = new com.see.truetransact.uicomponent.CTextField();
        lblRemark = new com.see.truetransact.uicomponent.CLabel();
        txtRemark = new com.see.truetransact.uicomponent.CTextField();
        lblCreditLienAccount = new com.see.truetransact.uicomponent.CLabel();
        panLienButtons = new com.see.truetransact.uicomponent.CPanel();
        btnLienNew = new com.see.truetransact.uicomponent.CButton();
        btnLienSave = new com.see.truetransact.uicomponent.CButton();
        btnLienDelete = new com.see.truetransact.uicomponent.CButton();
        btnUnLien = new com.see.truetransact.uicomponent.CButton();
        lblLienProductID = new com.see.truetransact.uicomponent.CLabel();
        lblLienAccHDValue = new com.see.truetransact.uicomponent.CLabel();
        cboLienProductID = new com.see.truetransact.uicomponent.CComboBox();
        panLienActNum = new com.see.truetransact.uicomponent.CPanel();
        txtLienActNum = new com.see.truetransact.uicomponent.CTextField();
        btnLienActNum = new com.see.truetransact.uicomponent.CButton();
        lblLienCustId = new com.see.truetransact.uicomponent.CLabel();
        lblLienCustIdValue = new com.see.truetransact.uicomponent.CLabel();
        cboCreditType = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositLienSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblDepositLienDesc = new com.see.truetransact.uicomponent.CLabel();
        panLoanotherSociety = new com.see.truetransact.uicomponent.CPanel();
        lblLLoanOtherSocietyLienAcNo = new com.see.truetransact.uicomponent.CLabel();
        lblLoanOtherSocietyLienAmount = new com.see.truetransact.uicomponent.CLabel();
        lblLoanOtherSocietyLienDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanOtherSocietyLienDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoanOtherSocietyRemark = new com.see.truetransact.uicomponent.CLabel();
        txtLoanOtherSocietyRemark = new com.see.truetransact.uicomponent.CTextField();
        panLienButtons2 = new com.see.truetransact.uicomponent.CPanel();
        btnLoanOtherSocietyLienNew = new com.see.truetransact.uicomponent.CButton();
        btnLoanOtherSocietyLienSave = new com.see.truetransact.uicomponent.CButton();
        btnLoanOtherSocietyLienDelete = new com.see.truetransact.uicomponent.CButton();
        btnLoanOtherSocietyUnLien = new com.see.truetransact.uicomponent.CButton();
        lblLoanOtherSocietyLoanType = new com.see.truetransact.uicomponent.CLabel();
        cboLienLoanType = new com.see.truetransact.uicomponent.CComboBox();
        lblLoanOtherSocietyLienCustName = new com.see.truetransact.uicomponent.CLabel();
        lblDepositLienSLNO2 = new com.see.truetransact.uicomponent.CLabel();
        lblDepositLienDesc2 = new com.see.truetransact.uicomponent.CLabel();
        txtLoanOtherSocietyLienAmount = new com.see.truetransact.uicomponent.CTextField();
        txtLoanOtherSocietyLienCustName = new com.see.truetransact.uicomponent.CTextField();
        txtLoanOtherSocietyLienAcNo = new com.see.truetransact.uicomponent.CTextField();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(815, 625));
        setPreferredSize(new java.awt.Dimension(815, 625));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Close");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Print");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Print");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        panLien.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLien.setMinimumSize(new java.awt.Dimension(580, 400));
        panLien.setPreferredSize(new java.awt.Dimension(572, 400));
        panLien.setLayout(new java.awt.GridBagLayout());

        panDepositDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepositDetails.setMinimumSize(new java.awt.Dimension(500, 120));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(572, 120));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDeposit.setMinimumSize(new java.awt.Dimension(250, 150));
        panDeposit.setPreferredSize(new java.awt.Dimension(250, 150));
        panDeposit.setLayout(new java.awt.GridBagLayout());

        lblAccountHD.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblAccountHD, gridBagConstraints);

        cboProductID.setPopupWidth(200);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(cboProductID, gridBagConstraints);

        lblDepositNo.setText("Deposit Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblDepositNo, gridBagConstraints);

        lblAccountHDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHDValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHDValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblAccountHDValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAccountHDValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblAccountHDValue, gridBagConstraints);

        panDepositNo.setLayout(new java.awt.GridBagLayout());

        txtDepositNo.setEnabled(false);
        txtDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositNoActionPerformed(evt);
            }
        });
        txtDepositNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panDepositNo.add(txtDepositNo, gridBagConstraints);

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 3);
        panDepositNo.add(btnDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDeposit.add(panDepositNo, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblProductID, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblCustomerID, gridBagConstraints);

        lblCustomerIDValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblCustomerIDValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblCustomerIDValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblCustomerIDValue, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblCustomerNameValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCustomerNameValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeposit.add(lblCustomerNameValue, gridBagConstraints);

        chkLoanwithothersociety.setText("Loan with other society");
        chkLoanwithothersociety.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoanwithothersocietyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panDeposit.add(chkLoanwithothersociety, gridBagConstraints);

        chkDepositUnlien.setText("Deposit Unlien");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panDeposit.add(chkDepositUnlien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(panDeposit, gridBagConstraints);

        panCustomer.setMinimumSize(new java.awt.Dimension(250, 150));
        panCustomer.setPreferredSize(new java.awt.Dimension(250, 150));
        panCustomer.setLayout(new java.awt.GridBagLayout());

        lblClearBalance.setText("Clear Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblClearBalance, gridBagConstraints);

        lblClearBalanceValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblClearBalanceValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblClearBalanceValue, gridBagConstraints);

        lblLienSum.setText("Sum of Authorized Liens");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblLienSum, gridBagConstraints);

        lblLienSumValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblLienSumValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblLienSumValue, gridBagConstraints);

        lblFreezeSum.setText("Sum of Authorized Freeze");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblFreezeSum, gridBagConstraints);

        lblFreezeSumValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblFreezeSumValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblFreezeSumValue, gridBagConstraints);

        lblDepositAmt.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblDepositAmt, gridBagConstraints);

        lblDepositAmtValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDepositAmtValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblDepositAmtValue, gridBagConstraints);

        cboSubDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(cboSubDepositNo, gridBagConstraints);

        lblSubDepositNo.setText("Deposit Sub Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblSubDepositNo, gridBagConstraints);

        lblShadowLien.setText("Shadow Lien");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblShadowLien, gridBagConstraints);

        lblShadowLienValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblShadowLienValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(panCustomer, gridBagConstraints);

        sptDeposit.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(sptDeposit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLien.add(panDepositDetails, gridBagConstraints);

        panLienDetail.setMinimumSize(new java.awt.Dimension(80, 230));
        panLienDetail.setPreferredSize(new java.awt.Dimension(400, 230));
        panLienDetail.setLayout(new java.awt.GridBagLayout());

        panLienTable.setMinimumSize(new java.awt.Dimension(195, 22));
        panLienTable.setPreferredSize(new java.awt.Dimension(195, 403));
        panLienTable.setLayout(new java.awt.GridBagLayout());

        tblLien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLienMouseClicked(evt);
            }
        });
        srpLien.setViewportView(tblLien);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panLienTable.add(srpLien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLienDetail.add(panLienTable, gridBagConstraints);

        panLienInfo.setMinimumSize(new java.awt.Dimension(325, 140));
        panLienInfo.setPreferredSize(new java.awt.Dimension(385, 140));
        panLienInfo.setLayout(new java.awt.GridBagLayout());

        lblLienAccountHead.setText("Lien Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienAccountHead, gridBagConstraints);

        lblLienAccountNumber.setText("Lien Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienAccountNumber, gridBagConstraints);

        lblLienAmount.setText("Lien Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienAmount, gridBagConstraints);

        lblLienDate.setText("Lien Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(tdtLienDate, gridBagConstraints);

        txtLienAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLienAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(txtLienAmount, gridBagConstraints);

        lblRemark.setText("Remark");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblRemark, gridBagConstraints);

        txtRemark.setMinimumSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(txtRemark, gridBagConstraints);

        lblCreditLienAccount.setText("Credit Lien Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblCreditLienAccount, gridBagConstraints);

        btnLienNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnLienNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLienNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienNewActionPerformed(evt);
            }
        });
        panLienButtons.add(btnLienNew);

        btnLienSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnLienSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLienSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienSaveActionPerformed(evt);
            }
        });
        panLienButtons.add(btnLienSave);

        btnLienDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnLienDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLienDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienDeleteActionPerformed(evt);
            }
        });
        panLienButtons.add(btnLienDelete);

        btnUnLien.setText("UnLien");
        btnUnLien.setPreferredSize(new java.awt.Dimension(80, 30));
        btnUnLien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnLienActionPerformed(evt);
            }
        });
        panLienButtons.add(btnUnLien);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panLienInfo.add(panLienButtons, gridBagConstraints);

        lblLienProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienProductID, gridBagConstraints);

        lblLienAccHDValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblLienAccHDValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienAccHDValue, gridBagConstraints);

        cboLienProductID.setMinimumSize(new java.awt.Dimension(97, 22));
        cboLienProductID.setPopupWidth(250);
        cboLienProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLienProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(cboLienProductID, gridBagConstraints);

        panLienActNum.setLayout(new java.awt.GridBagLayout());

        txtLienActNum.setMinimumSize(new java.awt.Dimension(120, 21));
        txtLienActNum.setPreferredSize(new java.awt.Dimension(120, 21));
        txtLienActNum.setEnabled(false);
        txtLienActNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLienActNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienActNum.add(txtLienActNum, gridBagConstraints);

        btnLienActNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLienActNum.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLienActNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLienActNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLienActNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienActNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panLienActNum.add(btnLienActNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLienInfo.add(panLienActNum, gridBagConstraints);

        lblLienCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienCustId, gridBagConstraints);

        lblLienCustIdValue.setForeground(new java.awt.Color(0, 51, 204));
        lblLienCustIdValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLienCustIdValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblLienCustIdValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblLienCustIdValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblLienCustIdValue, gridBagConstraints);

        cboCreditType.setMinimumSize(new java.awt.Dimension(97, 22));
        cboCreditType.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(cboCreditType, gridBagConstraints);

        lblDepositLienSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblDepositLienSLNO, gridBagConstraints);

        lblDepositLienDesc.setMaximumSize(new java.awt.Dimension(150, 16));
        lblDepositLienDesc.setMinimumSize(new java.awt.Dimension(150, 16));
        lblDepositLienDesc.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienInfo.add(lblDepositLienDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLienDetail.add(panLienInfo, gridBagConstraints);

        panLoanotherSociety.setMinimumSize(new java.awt.Dimension(285, 140));
        panLoanotherSociety.setPreferredSize(new java.awt.Dimension(285, 140));
        panLoanotherSociety.setLayout(new java.awt.GridBagLayout());

        lblLLoanOtherSocietyLienAcNo.setText("Lien Ac Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLLoanOtherSocietyLienAcNo, gridBagConstraints);

        lblLoanOtherSocietyLienAmount.setText("Lien Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLoanOtherSocietyLienAmount, gridBagConstraints);

        lblLoanOtherSocietyLienDate.setText("Lien Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLoanOtherSocietyLienDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(tdtLoanOtherSocietyLienDate, gridBagConstraints);

        lblLoanOtherSocietyRemark.setText("Remark");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLoanOtherSocietyRemark, gridBagConstraints);

        txtLoanOtherSocietyRemark.setMinimumSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(txtLoanOtherSocietyRemark, gridBagConstraints);

        btnLoanOtherSocietyLienNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnLoanOtherSocietyLienNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLoanOtherSocietyLienNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanOtherSocietyLienNewActionPerformed(evt);
            }
        });
        panLienButtons2.add(btnLoanOtherSocietyLienNew);

        btnLoanOtherSocietyLienSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnLoanOtherSocietyLienSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLoanOtherSocietyLienSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanOtherSocietyLienSaveActionPerformed(evt);
            }
        });
        panLienButtons2.add(btnLoanOtherSocietyLienSave);

        btnLoanOtherSocietyLienDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnLoanOtherSocietyLienDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnLoanOtherSocietyLienDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanOtherSocietyLienDeleteActionPerformed(evt);
            }
        });
        panLienButtons2.add(btnLoanOtherSocietyLienDelete);

        btnLoanOtherSocietyUnLien.setText("UnLien");
        btnLoanOtherSocietyUnLien.setPreferredSize(new java.awt.Dimension(80, 30));
        btnLoanOtherSocietyUnLien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanOtherSocietyUnLienActionPerformed(evt);
            }
        });
        panLienButtons2.add(btnLoanOtherSocietyUnLien);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panLoanotherSociety.add(panLienButtons2, gridBagConstraints);

        lblLoanOtherSocietyLoanType.setText("LoanType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLoanOtherSocietyLoanType, gridBagConstraints);

        cboLienLoanType.setMinimumSize(new java.awt.Dimension(97, 22));
        cboLienLoanType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(cboLienLoanType, gridBagConstraints);

        lblLoanOtherSocietyLienCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblLoanOtherSocietyLienCustName, gridBagConstraints);

        lblDepositLienSLNO2.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblDepositLienSLNO2, gridBagConstraints);

        lblDepositLienDesc2.setMaximumSize(new java.awt.Dimension(150, 16));
        lblDepositLienDesc2.setMinimumSize(new java.awt.Dimension(150, 16));
        lblDepositLienDesc2.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(lblDepositLienDesc2, gridBagConstraints);

        txtLoanOtherSocietyLienAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanOtherSocietyLienAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(txtLoanOtherSocietyLienAmount, gridBagConstraints);

        txtLoanOtherSocietyLienCustName.setMinimumSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(txtLoanOtherSocietyLienCustName, gridBagConstraints);

        txtLoanOtherSocietyLienAcNo.setMinimumSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanotherSociety.add(txtLoanOtherSocietyLienAcNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLienDetail.add(panLoanotherSociety, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLien.add(panLienDetail, gridBagConstraints);

        getContentPane().add(panLien, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

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

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
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

    private void btnLoanOtherSocietyUnLienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanOtherSocietyUnLienActionPerformed
        // TODO add your handling code here:
        int a = 0;
        int b = 0;
        if (closingFlag != true) {
            HashMap where = new HashMap();
            where.put("LACT_NUM", observable.getLOSLienAcNo());
//            List lists = ClientUtil.executeQuery("detailsForLfd", where);
//            HashMap where1 = new HashMap();
//            where1=(HashMap)lists.get(0);
//            a= CommonUtil.convertObjToInt(where1.get("CNT"));
//            if(a!=b) {
//                ClientUtil.displayAlert("Loan exists.");
//                return;
//            }
        }
        String unLienRemark;
        if (closingFlag == true) {
            unLienRemark = observable.getUnLienRemark();
        } else {
            unLienRemark = COptionPane.showInputDialog(this, resourceBundle.getString("UNLIEN_REMARK"), "");
        }
        updateLOSLienOBFields();
        observable.setUnLienRemark(unLienRemark);
        observable.setLienStatus(CommonConstants.STATUS_UNLIEN);

        int selectRow = -1;
        if (closingFlag == true) {
            selectRow = 0;
        } else {
            selectRow = tblLien.getSelectedRow();
        }
        observable.deleteLienData(selectRow);
        //        observable.deleteLienData(this.tblLien.getSelectedRow());
        this.updateTable();
        panLOSLienEnableDisable(false);
        this.btnLoanOtherSocietyLienNew.setEnabled(true);

    }//GEN-LAST:event_btnLoanOtherSocietyUnLienActionPerformed

    private void btnLoanOtherSocietyLienDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanOtherSocietyLienDeleteActionPerformed
        // TODO add your handling code here:
        observable.setLienStatus(CommonConstants.STATUS_DELETED);
        observable.deleteLienData(this.tblLien.getSelectedRow());
        this.updateTable();
        this.updateBalance(observable.getLOSLienAmount(), false);
        panLOSLienEnableDisable(false);
        this.btnLoanOtherSocietyLienNew.setEnabled(true);
    }//GEN-LAST:event_btnLoanOtherSocietyLienDeleteActionPerformed

    private void btnLoanOtherSocietyLienSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanOtherSocietyLienSaveActionPerformed
        // TODO add your handling code here:
        String LienNo = observable.getLienNo();
        String mandatoryMessage;
        mandatoryMessage = checkMandatory(this.panDepositDetails);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        mandatoryMessage = checkMandatory(this.panLoanotherSociety);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        if (checkZeroAmountLos()) {
            COptionPane.showMessageDialog(this, resourceBundle.getString("ZEROAMOUNT_WARNING"));
            return;
        }
        if (!checkLienDateLos()) {
            StringBuffer msg = new StringBuffer();
            msg.append(resourceBundle.getString("LIENDATE_WARNING") + "\n");
            msg.append(resourceBundle.getString("DEPOSIT_DATE"));
            msg.append(DateUtil.getStringDate(observable.getDepositDate()) + "\n");
            msg.append(resourceBundle.getString("AOD_DATE"));
            msg.append(DateUtil.getStringDate(observable.getActOpeningDate()));
            COptionPane.showMessageDialog(this, msg.toString());
            this.tdtLienDate.setDateValue("");
            this.tdtLienDate.requestFocus();
            return;
        }
        if (!this._intLienNew) {
            if (this.tblLien.getSelectedRow() != -1) {
                ArrayList arr = ((TableModel) tblLien.getModel()).getRow(this.tblLien.getSelectedRow());
                this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)), false);
            }
        }
        if (!checkValidLienAmount()) {
            COptionPane.showMessageDialog(this, resourceBundle.getString("LIENSUM_WARNING"));
            if (!this._intLienNew) {
                if (this.tblLien.getSelectedRow() != -1) {
                    ArrayList arr = ((TableModel) tblLien.getModel()).getRow(this.tblLien.getSelectedRow());
                    this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)), true);
                }
            }
            return;
        }

        updateLOSLienOBFields();
        if (!this._intLienNew) {
            observable.insertLOSLienData(this.tblLien.getSelectedRow());
        } else {
            observable.insertLOSLienData(-1);
        }
        HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("LIENNO");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            lockMap.put("LIENNO", LienNo);

            setEditLockMap(lockMap);
            setEditLock();
        }
        this.updateTable();
        this.updateBalance(this.txtLoanOtherSocietyLienAmount.getText(), true);
        this.btnLoanOtherSocietyLienNew.setEnabled(true);
        panLOSLienEnableDisable(false);
    }//GEN-LAST:event_btnLoanOtherSocietyLienSaveActionPerformed

    private void btnLoanOtherSocietyLienNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanOtherSocietyLienNewActionPerformed
        // TODO add your handling code here:
        if (chkLoanwithothersociety.isSelected()) {
            if (observable.checkAuthStatus()) {
                ClientUtil.showMessageWindow("Existing Lien Authorize/Reject first");
                return;
            }

            panLoanOtherSocietyLienEnableDisable();
            panLOSLienEnableDisable(true);
            this.btnLoanOtherSocietyUnLien.setEnabled(false);
            this.btnLoanOtherSocietyLienDelete.setEnabled(false);
            this._intLienNew = true;
            observable.setLienStatus(CommonConstants.STATUS_CREATED);
//        this.tdtLienDate.setDateValue(DateUtil.getStringDate(currDt.clone()));
//        this.tdtLienDate.setEnabled(true);

        }
    }//GEN-LAST:event_btnLoanOtherSocietyLienNewActionPerformed

    private void chkLoanwithothersocietyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoanwithothersocietyActionPerformed
        // TODO add your handling code here:
        if (chkLoanwithothersociety.isSelected()) {
            panLoanotherSociety.setVisible(true);
            panLienInfo.setVisible(false);
        } else {
            panLoanotherSociety.setVisible(false);
            panLienInfo.setVisible(true);
        }
    }//GEN-LAST:event_chkLoanwithothersocietyActionPerformed

    private void txtLienActNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLienActNumFocusLost
        // TODO add your handling code here:
        observable.depFlag = false;
        observable.loanFlag = false;
        String ACCOUNTNO = (String) txtLienActNum.getText();
        if (ACCOUNTNO.length() > 0) {
            System.out.println("innnnn" + ACCOUNTNO);
            observable.loanFlag = true;
            depositOldNo = false;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO, depositOldNo)) {
                txtLienActNum.setText(observable.getLienAccountNo());
                System.out.println("observableobservable" + observable.getLienAccountNo());
                cboLienProductID.setModel(observable.getCbmLienProductId());
                cboLienProductIDActionPerformed(null);
//                txtLienActNum.setText(ACCOUNTNO);
                if (lblLienAccHDValue.getText().length() > 0) {
                    viewType = LIEN_ACT_NUM;
                    HashMap fillMap = new HashMap();
                    fillMap.put("PROD_ID", observable.getCbmLienProductId().getKeyForSelected());
                    fillMap.put("ACT_NUM", txtLienActNum.getText());
//                    fillMap.put("SUBNO", "1");
                    fillMap.put("DEFAULT", "DEFAULT");
                    fillData(fillMap);
                } else {
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtLienActNum.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtLienActNum.setText("");
                return;
            }
        }

    }//GEN-LAST:event_txtLienActNumFocusLost

    private void txtDepositNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositNoFocusLost
        // TODO add your handling code here:
        observable.depFlag = false;
        observable.loanFlag = false;
        String ACCOUNTNO = (String) txtDepositNo.getText();
        if (ACCOUNTNO.length() > 0) {
            observable.depFlag = true;
            depositOldNo = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO, depositOldNo)) {
                txtDepositNo.setText(observable.getTxtDepositNo());
                ACCOUNTNO = (String) txtDepositNo.getText();
                cboProductID.setModel(observable.getCbmProductId());
                cboProductIDActionPerformed(null);
//                txtDepositNo.setText(ACCOUNTNO);
                if (lblAccountHDValue.getText().length() > 0) {
                    viewType = DEPOSIT_ACCT;
                    HashMap fillMap = new HashMap();
                    fillMap.put("PROD_ID", observable.getCbmSubDepositNos().getKeyForSelected());
                    fillMap.put("DEPOSIT_ACT_NUM", txtDepositNo.getText());
                    fillMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                    fillMap.put("DEFAULT", "DEFAULT");
                    fillData(fillMap);
                } else {
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtDepositNo.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtDepositNo.setText("");
                return;
            }
        }

    }//GEN-LAST:event_txtDepositNoFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnLienActNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienActNumActionPerformed
        // Add your handling code here:
        callView(LIEN_ACT_NUM);
        this.txtLienActNum.setEnabled(false);
    }//GEN-LAST:event_btnLienActNumActionPerformed

    private void cboLienProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLienProductIDActionPerformed
        // Add your handling code here:
        String lienProdId = (String) ((ComboBoxModel) this.cboLienProductID.getModel()).getKeyForSelected();
        if (lienProdId != null && lienProdId.length() > 0) {
            String actHD = observable.getLienProductHead(lienProdId);
            lblLienAccHDValue.setText(actHD);
        } else {
            lblLienAccHDValue.setText("");
        }
    }//GEN-LAST:event_cboLienProductIDActionPerformed

    private void btnUnLienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnLienActionPerformed
        // Add your handling code here:
        int a = 0;
        int b = 0;
        if (closingFlag != true) {
            HashMap where = new HashMap();
            where.put("LACT_NUM", observable.getLienAccountNo());
            List lists = ClientUtil.executeQuery("detailsForLfd", where);
            HashMap where1 = new HashMap();
            where1 = (HashMap) lists.get(0);
            a = CommonUtil.convertObjToInt(where1.get("CNT"));
//            if(a!=b) {
//                ClientUtil.displayAlert("Loan exists.");
//                return;
//            }
        }
        String unLienRemark;
        if (closingFlag == true) {
            unLienRemark = observable.getUnLienRemark();
        } else {
            unLienRemark = COptionPane.showInputDialog(this, resourceBundle.getString("UNLIEN_REMARK"), "");
        }
        updateLienOBFields();
        observable.setUnLienRemark(unLienRemark);
        observable.setLienStatus(CommonConstants.STATUS_UNLIEN);

        int selectRow = -1;
        if (closingFlag == true) {
            selectRow = 0;
        } else {
            selectRow = tblLien.getSelectedRow();
        }
        observable.deleteLienData(selectRow);
        //        observable.deleteLienData(this.tblLien.getSelectedRow());
        this.updateTable();
        panLienEnableDisable(false);
        this.btnLienNew.setEnabled(true);
    }//GEN-LAST:event_btnUnLienActionPerformed

    private void btnLienDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienDeleteActionPerformed
        // Add your handling code here:
        observable.setLienStatus(CommonConstants.STATUS_DELETED);
        observable.deleteLienData(this.tblLien.getSelectedRow());
        this.updateTable();
        this.updateBalance(observable.getLienAmount(), false);
        panLienEnableDisable(false);
        this.btnLienNew.setEnabled(true);
    }//GEN-LAST:event_btnLienDeleteActionPerformed

    private void btnLienSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienSaveActionPerformed
        // Add your handling code here:
        //mm
        String LienNo = observable.getLienNo();
        String mandatoryMessage;
        mandatoryMessage = checkMandatory(this.panDepositDetails);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        mandatoryMessage = checkMandatory(this.panLienInfo);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        if (checkZeroAmount()) {
            COptionPane.showMessageDialog(this, resourceBundle.getString("ZEROAMOUNT_WARNING"));
            return;
        }
        if (!checkLienDate()) {
            StringBuffer msg = new StringBuffer();
            msg.append(resourceBundle.getString("LIENDATE_WARNING") + "\n");
            msg.append(resourceBundle.getString("DEPOSIT_DATE"));
            msg.append(DateUtil.getStringDate(observable.getDepositDate()) + "\n");
            msg.append(resourceBundle.getString("AOD_DATE"));
            msg.append(DateUtil.getStringDate(observable.getActOpeningDate()));
            COptionPane.showMessageDialog(this, msg.toString());
            this.tdtLienDate.setDateValue("");
            this.tdtLienDate.requestFocus();
            return;
        }
        if (!this._intLienNew) {
            if (this.tblLien.getSelectedRow() != -1) {
                ArrayList arr = ((TableModel) tblLien.getModel()).getRow(this.tblLien.getSelectedRow());
                this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)), false);
            }
        }
        if (!checkValidLienAmount()) {
            COptionPane.showMessageDialog(this, resourceBundle.getString("LIENSUM_WARNING"));
            if (!this._intLienNew) {
                if (this.tblLien.getSelectedRow() != -1) {
                    ArrayList arr = ((TableModel) tblLien.getModel()).getRow(this.tblLien.getSelectedRow());
                    this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)), true);
                }
            }
            return;
        }
        updateLienOBFields();
        if (!this._intLienNew) {
            observable.insertLienData(this.tblLien.getSelectedRow());
        } else {
            observable.insertLienData(-1);
        }
        HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("LIENNO");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            lockMap.put("LIENNO", LienNo);

            setEditLockMap(lockMap);
            setEditLock();
        }
        this.updateTable();
        this.updateBalance(this.txtLienAmount.getText(), true);
        this.btnLienNew.setEnabled(true);
        panLienEnableDisable(false);
    }//GEN-LAST:event_btnLienSaveActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private boolean checkZeroAmount() {
        Double amt = CommonUtil.convertObjToDouble(this.txtLienAmount.getText());
        if (amt != null) {
            if (amt.doubleValue() <= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkZeroAmountLos() {
        Double amt = CommonUtil.convertObjToDouble(this.txtLoanOtherSocietyLienAmount.getText());
        if (amt != null) {
            if (amt.doubleValue() <= 0) {
                return true;
            }
        }
        return false;
    }

    private String checkMandatory(JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private boolean checkLienDate() {
        Date lienDate = DateUtil.getDateMMDDYYYY(this.tdtLienDate.getDateValue());
        if (lienDate != null) {
            if (observable.getActOpeningDate() != null) {
                if (lienDate.before(observable.getActOpeningDate())) {
                    return false;
                }
            }
            if (observable.getDepositDate() != null) {
                if (lienDate.before(observable.getDepositDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkLienDateLos() {
        Date lienDate = DateUtil.getDateMMDDYYYY(this.tdtLoanOtherSocietyLienDate.getDateValue());
        if (lienDate != null) {
            if (observable.getActOpeningDate() != null) {
                if (lienDate.before(observable.getActOpeningDate())) {
                    return false;
                }
            }
            if (observable.getDepositDate() != null) {
                if (lienDate.before(observable.getDepositDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateLienOBFields() {
        observable.setLienAccountHD(this.lblLienAccHDValue.getText());
        observable.setCboLienProdID((String) ((ComboBoxModel) this.cboLienProductID.getModel()).getKeyForSelected());
        observable.setLienAccountNo(this.txtLienActNum.getText());
        observable.setLienAmount(this.txtLienAmount.getText());
        observable.setLienDate(this.tdtLienDate.getDateValue());
        observable.setLienRemark(this.txtRemark.getText());
        observable.setLienCreditType((String) ((ComboBoxModel) this.cboCreditType.getModel()).getKeyForSelected());
        observable.setTxtDepositNo(this.txtDepositNo.getText());
        observable.setCboSubDepositNo((String)((ComboBoxModel) this.cboSubDepositNo.getModel()).getKeyForSelected());

    }

    private void updateLOSLienOBFields() {
        observable.setCboLOSLoanType((String) ((ComboBoxModel) this.cboLienLoanType.getModel()).getKeyForSelected());
        observable.setLOSLienAcNo(txtLoanOtherSocietyLienAcNo.getText());
        observable.setLOSLienAmount(txtLoanOtherSocietyLienAmount.getText());
        observable.setLOSLienCustName(txtLoanOtherSocietyLienCustName.getText());
        observable.setLOSLienDate(tdtLoanOtherSocietyLienDate.getDateValue());
        observable.setLOSLienRemarks(txtLoanOtherSocietyRemark.getText());
        observable.setTxtDepositNo(this.txtDepositNo.getText());
        observable.setCboSubDepositNo((String) ((ComboBoxModel) this.cboSubDepositNo.getModel()).getKeyForSelected());
        observable.setChkLOS(chkLoanwithothersociety.isSelected());

    }

    private void btnLienNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienNewActionPerformed
        // Add your handling code here:
        if (observable.checkAuthStatus()) {
            ClientUtil.showMessageWindow("Existing Lien Authorize/Reject first");
            return;
        }

        panLienEnableDisable(true);
        this.btnUnLien.setEnabled(true);
        this.btnLienDelete.setEnabled(false);
        this._intLienNew = true;
        observable.setLienStatus(CommonConstants.STATUS_CREATED);
        this.tdtLienDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        this.tdtLienDate.setEnabled(true);
    }//GEN-LAST:event_btnLienNewActionPerformed

    private void tblLienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLienMouseClicked
        // Add your handling code here:
        rowSelected = tblLien.getSelectedRow();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            panLienEnableDisable(false);
            panLOSLienEnableDisable(false);
            updationLien();
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getActionType() != ClientConstants.ACTIONTYPE_EXCEPTION
                && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                panLienEnableDisable(true);
                panLOSLienEnableDisable(true);
            }
            updationLien();
            HashMap viewMap = new HashMap();
            this.viewType = viewType;
            if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
                String LienNo = lblDepositLienDesc.getText();
                viewMap.put("LIENNO", LienNo);
                whenTableRowSelected(viewMap);
                if (isLoanLienDisable()) {
                    ClientUtil.enableDisable(this.panLienInfo, false);
                    btnSave.setEnabled(false);
                    btnLienNew.setEnabled(false);
                }
            }
            this.btnUnLien.setEnabled(true);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || isLoanLienDisable() || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                this.btnUnLien.setEnabled(true);
            }

        }
        for (int i = 0; i < tblLien.getRowCount(); i++) {
            String status = CommonUtil.convertObjToStr((tblLien.getValueAt(i, 4)));
            if (status.length() == 0) {
                this.btnUnLien.setEnabled(true);
            }
        }
        if (closingFlag != true) {
            String status = CommonUtil.convertObjToStr(tblLien.getValueAt(rowSelected, 3));
            String status1 = CommonUtil.convertObjToStr(tblLien.getValueAt(rowSelected, 4));
            if (status.equalsIgnoreCase("UNLIENED")) {
                this.btnLienDelete.setEnabled(false);
                this.btnLienSave.setEnabled(false);
            }
            if (status.equalsIgnoreCase("MODIFIED") && (status1.equalsIgnoreCase("REJECTED"))) {
                ClientUtil.enableDisable(this.panLienInfo, false);
                this.btnLienDelete.setEnabled(false);
                this.btnLienSave.setEnabled(false);
            }
        }
        if (viewType == ClientConstants.ACTIONTYPE_EDIT && lblDepositLienDesc.getText().length() > 0) {
            HashMap flexiMap = new HashMap();
            flexiMap.put("LIENNO", lblDepositLienDesc.getText());
            List lst = ClientUtil.executeQuery("getSelectFlexiDeteails", flexiMap);
            if (lst != null && lst.size() > 0) {
                flexiMap = (HashMap) lst.get(0);
                btnLienNew.setEnabled(false);
                btnUnLien.setEnabled(true);
                btnSave.setEnabled(false);
            }
        }
        btnSave.setEnabled(true);
    }//GEN-LAST:event_tblLienMouseClicked
    private void whenTableRowSelected(HashMap paramMap) {
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("LIENNO"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", currDt.clone());
        System.out.println("Record Key Map : " + map);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                //                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                btnSave.setEnabled(false);
            } else {
                //                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                btnSave.setEnabled(true);
            }
        } else {
            //                        setMode(ClientConstants.ACTIONTYPE_EDIT);
            btnSave.setEnabled(true);
        }

        setOpenForEditBy(lockedBy);
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID());
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            btnSave.setEnabled(false);
            //                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);

        }

    }

    public void depRenewalLineMarked(HashMap lienMap) {//this method is using for while going for deposit renewal incase lien is marked.....
        HashMap depLienMap = new HashMap();
        HashMap custMap = new HashMap();
        HashMap fillMap = new HashMap();
        depLienMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
        List lst = ClientUtil.executeQuery("getDepositLienDet", depLienMap);
        if (lst != null && lst.size() > 0) {
            btnSave.setEnabled(true);
            depLienMap = (HashMap) lst.get(0);
            if (!depLienMap.get("STATUS").equals("UNLIENED")) {
                closingFlag = true;
                String custId = null;
                String custName = null;
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                observable.setStatus();
                viewType = ClientConstants.ACTIONTYPE_EDIT;
                fillMap.put("DEPOSIT_ACT_NUM", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
                cboProductID.setSelectedItem(CommonUtil.convertObjToStr(lienMap.get("HEAD_DESC")));
                lblAccountHDValue.setText(CommonUtil.convertObjToStr(lienMap.get("HEAD_DESC")));
                txtDepositNo.setText(CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
                custMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
                fillMap.put("PROD_ID", CommonUtil.convertObjToStr(lienMap.get("PROD_ID")));
                List custlst = ClientUtil.executeQuery("getCustIdForDeposits", custMap);
                if (custlst.size() > 0) {
                    custMap = (HashMap) custlst.get(0);
                    custId = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
                    fillMap.put("CUST_ID", CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
                    List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
                    if (namelst.size() > 0) {
                        custMap = (HashMap) namelst.get(0);
                        fillMap.put("CUSTOMER_NAME", CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
                        custName = CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME"));
                    }
                }
                fillMap.put("SUBNO", CommonUtil.convertObjToInt(depLienMap.get("DEPOSIT_SUB_NO")));
                fillData(fillMap);
                depLienMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
                lst = ClientUtil.executeQuery("getDepositLienDet", depLienMap);
                if (lst != null && lst.size() > 0) {
                    btnSave.setEnabled(true);
                    for (int i = 0; i < lst.size(); i++) {
                        depLienMap = (HashMap) lst.get(i);
                        tblLienMouseClicked(null);
                        btnUnLienActionPerformed(null);
                    }
                }
                btnSaveActionPerformed(null);
            }
        }
    }

    public void depClosingLineMarked(HashMap lienMap) {//this method is using for while going for deposit closing incase lien is marked.....
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        cboProductID.setSelectedItem(CommonUtil.convertObjToStr(lienMap.get("HEAD_DESC")));
        lblAccountHDValue.setText(CommonUtil.convertObjToStr(lienMap.get("HEAD_DESC")));
        txtDepositNo.setText(CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
        HashMap depLienMap = new HashMap();
        HashMap custMap = new HashMap();
        HashMap fillMap = new HashMap();
        custMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
        fillMap.put("DEPOSIT_ACT_NUM", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
        fillMap.put("PROD_ID", CommonUtil.convertObjToStr(lienMap.get("PROD_ID")));
        List lst = ClientUtil.executeQuery("getCustIdForDeposits", custMap);
        if (lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            lblCustomerIDValue.setText(CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
            fillMap.put("CUST_ID", CommonUtil.convertObjToStr(custMap.get("CUST_ID")));
            lst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (lst.size() > 0) {
                custMap = (HashMap) lst.get(0);
                fillMap.put("CUSTOMER_NAME", CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
                lblCustomerNameValue.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
        depLienMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(lienMap.get("DEPOSIT_NO")));
        lst = ClientUtil.executeQuery("getDepositLienDet", depLienMap);
        if (lst.size() > 0) {
            depLienMap = (HashMap) lst.get(0);
            fillMap.put("SUBNO", CommonUtil.convertObjToInt(depLienMap.get("DEPOSIT_SUB_NO")));
            cboSubDepositNo.setSelectedItem(CommonUtil.convertObjToStr(depLienMap.get("DEPOSIT_SUB_NO")));
            lblDepositAmtValue.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_AMOUNT")));
            lblLienSumValue.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_AMOUNT")));
            lblClearBalanceValue.setText(CommonUtil.convertObjToStr(lienMap.get("CLEAR_BALANCE")));
            lblShadowLienValue.setText(CommonUtil.convertObjToStr(lienMap.get("LIEN_AMOUNT")));
            viewType = ClientConstants.ACTIONTYPE_EDIT;
            fillData(fillMap);
            closingFlag = true;
            tblLienMouseClicked(null);
            lblDepositLienDesc.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_NO")));
            lblLienAccHDValue.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_AC_HD")));
            txtLienActNum.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_AC_NO")));
            txtLienAmount.setText(CommonUtil.convertObjToStr(depLienMap.get("LIEN_AMOUNT")));
            txtRemark.setText(CommonUtil.convertObjToStr(depLienMap.get("REMARKS")));
        }
        observable.setUnLienRemark("From Deposit");
        btnUnLienActionPerformed(null);
        btnSaveActionPerformed(null);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
    }

    public void depClosingLineAuthorize(HashMap lienMap) {//this method is using for while going for deposit closing incase lien is marked.....
        HashMap fillMap = new HashMap();
        fillMap.put("AMOUNT", lienMap.get("CLEAR_BALANCE"));
        fillMap.put("AUTHORIZEDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
        fillMap.put("DEPOSIT_ACT_NUM", lienMap.get("DEPOSIT_NO"));
        HashMap depMap = new HashMap();
        depMap.put("DEPOSIT_NO", lienMap.get("DEPOSIT_NO"));
        List lst = ClientUtil.executeQuery("getDepositLienDet", depMap);
        if (lst.size() > 0) {
            depMap = (HashMap) lst.get(0);
            fillMap.put("SUBNO", CommonUtil.convertObjToInt(depMap.get("DEPOSIT_SUB_NO")));
            fillMap.put("STATUS", depMap.get("STATUS"));
            fillMap.put("LIENNO", depMap.get("LIEN_NO"));
            fillMap.put("BALANCE", depMap.get("LIEN_AMOUNT"));
            fillData(fillMap);
            btnAuthorizeActionPerformed(null);
        }
    }

    public void depRenewalLineAuthorize(HashMap lienMap, String authorizeStatus) {//this method is using for while going for deposit renewal incase lien is marked.....
        HashMap fillMap = new HashMap();
        fillMap.put("AUTHORIZEDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
        fillMap.put("DEPOSIT_ACT_NUM", lienMap.get("DEPOSIT_NO"));
        HashMap depMap = new HashMap();
        depMap.put("DEPOSIT_NO", lienMap.get("DEPOSIT_NO"));
        List lst = ClientUtil.executeQuery("getDepositLienDet", depMap);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
                depMap = (HashMap) lst.get(i);
                fillMap.put("SUBNO", CommonUtil.convertObjToInt(depMap.get("DEPOSIT_SUB_NO")));
                fillMap.put("STATUS", depMap.get("STATUS"));
                fillMap.put("LIENNO", depMap.get("LIEN_NO"));
                fillMap.put("BALANCE", depMap.get("LIEN_AMOUNT"));
                fillMap.put("AMOUNT", depMap.get("LIEN_AMOUNT"));
                fillData(fillMap);
                if (authorizeStatus.equals("AUTHORIZED")) {
                    btnAuthorizeActionPerformed(null);
                } else {
                    btnRejectActionPerformed(null);
                }
            }
        }
    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }

    private void panLienEnableDisable(boolean value) {
        ClientUtil.clearAll(panLienInfo);
        this.lblLienAccHDValue.setText("");
        this.lblLienCustIdValue.setText("");

        ClientUtil.enableDisable(this.panLienInfo, value);
        this.btnLienActNum.setEnabled(value);
        this.btnLienDelete.setEnabled(value);
        this.btnLienSave.setEnabled(value);
        this.btnUnLien.setEnabled(value);
    }

    private void panLOSLienEnableDisable(boolean value) {
        ClientUtil.clearAll(panLoanotherSociety);
        this.txtLoanOtherSocietyLienAcNo.setText("");
        this.txtLoanOtherSocietyLienCustName.setText("");

        ClientUtil.enableDisable(this.panLoanotherSociety, value);
        chkLoanwithothersociety.setSelected(true);
        this.btnLoanOtherSocietyLienDelete.setEnabled(value);
        this.btnLoanOtherSocietyLienSave.setEnabled(value);
        this.btnLoanOtherSocietyUnLien.setEnabled(value);
    }

    private void panLoanOtherSocietyLienEnableDisable() {
        cboLienLoanType.setEnabled(true);
        txtLoanOtherSocietyLienAcNo.setEnabled(true);
        txtLoanOtherSocietyLienCustName.setEnabled(true);
        txtLoanOtherSocietyLienAmount.setEnabled(true);
        tdtLoanOtherSocietyLienDate.setEnabled(true);
        txtLoanOtherSocietyRemark.setEnabled(true);
    }

    private void updationLien() {
        int selectRow = -1;
        if (closingFlag == true) {
            selectRow = 0;
        } else {
            selectRow = tblLien.getSelectedRow();
        }
        observable.populateLien(selectRow);
        populateLienDetail();
        enableDisableLienInfo();
        _intLienNew = false;
        observable.setLienStatus(CommonConstants.STATUS_MODIFIED);
    }

    private void enableDisableLienInfo() {
        String authorizeStatus = observable.getLienAuthorizeStatus();
        String lienRemarks = CommonUtil.convertObjToStr(observable.getLienRemark());
        if ((authorizeStatus != null && authorizeStatus.length() > 0) || lienRemarks.equals("Lien for LTD")) {
            if (lienRemarks.equals("Lien for LTD")
                    || (authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED) || authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))) {
                ClientUtil.enableDisable(this.panLienInfo, false);
                this.btnLienDelete.setEnabled(false);
                this.btnLienSave.setEnabled(false);
                this.btnLienActNum.setEnabled(false);
                ClientUtil.enableDisable(this.panLoanotherSociety, false);
                this.btnLoanOtherSocietyLienDelete.setEnabled(false);
                this.btnLoanOtherSocietyLienSave.setEnabled(false);
//                this.btnLienActNum.setEnabled(false);
                if (lienRemarks.equals("Lien for LTD")) {
                    loanLienDisable = true;
                }
                return;
            }
        }
        this.btnUnLien.setEnabled(true);
    }

    private void populateLienDetail() {
//        if(observable.getUnLienRemark()!=null && observable.getUnLienRemark().equals("FLEXI_DEPOSITS")){
//            btnSave.setEnabled(false);
//            cboLienProductID.setSelectedItem("SAVINGS BANK(SB)");
//            observable.setCboLienProdID((String)((ComboBoxModel)this.cboLienProductID.getModel()).getKeyForSelected());
//        }else
        ((ComboBoxModel) this.cboLienProductID.getModel()).setKeyForSelected(observable.getCboLienProdID());
        this.txtLienActNum.setText(observable.getLienAccountNo());
        this.lblLienAccHDValue.setText(observable.getLienAccountHD());
        this.txtLienAmount.setText(observable.getLienAmount());
        this.txtRemark.setText(observable.getLienRemark());
        this.tdtLienDate.setDateValue(observable.getLienDate());
        ((ComboBoxModel) this.cboCreditType.getModel()).setKeyForSelected(observable.getLienCreditType());
        this.lblLienCustIdValue.setText(observable.getLienActNumCustomer(observable.getLienAccountNo()));
        lblDepositLienDesc.setText(observable.getLienNo());
        ((ComboBoxModel) this.cboLienLoanType.getModel()).setKeyForSelected(observable.getCboLOSLoanType());
        txtLoanOtherSocietyLienAcNo.setText(observable.getLOSLienAcNo());
        txtLoanOtherSocietyLienCustName.setText(observable.getLOSLienCustName());
        txtLoanOtherSocietyLienAmount.setText(observable.getLOSLienAmount());
        tdtLoanOtherSocietyLienDate.setDateValue(observable.getLOSLienDate());
        txtLoanOtherSocietyRemark.setText(observable.getLOSLienRemarks());
        chkLoanwithothersociety.setSelected(observable.getChkLOS());
    }
    private void cboSubDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubDepositNoActionPerformed
        // Add your handling code here:
        String subDepositNo = (String) ((ComboBoxModel) this.cboSubDepositNo.getModel()).getKeyForSelected();
        ClientUtil.clearAll(this.panLienInfo);
        ClientUtil.clearAll(this.panLoanotherSociety);
        this.tdtLienDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        this.tdtLienDate.setEnabled(true);
        this.lblLienAccHDValue.setText("");
        System.out.println("#@#@#@#@#@# sbu deposit no : "+cboSubDepositNo.getSelectedItem());
        System.out.println("subDepositNo" + subDepositNo);
        if (subDepositNo != null && subDepositNo.length() > 0) {
            if (!isMds) {
                observable.getAmountDetails(subDepositNo);
                updateAmountDetails();
                observable.getLienData(subDepositNo);
            } else {
                if (subDepositNo != null && subDepositNo.length() > 0) {
                    String prodId = (String) ((ComboBoxModel) this.cboProductID.getModel()).getKeyForSelected();
                    String Chit_no = txtDepositNo.getText();

                    observable.getMdsAmountDts(Chit_no, subDepositNo, prodId);
                    updateAmountDetails();
                    observable.getLienData(subDepositNo);
                }
            }
        } else {
            observable.resetTabel();
            resetAmountDetails();
        }
        updateTable();
        btnLienNew.setEnabled(true);
    }//GEN-LAST:event_cboSubDepositNoActionPerformed
    private void resetAmountDetails() {
        this.lblClearBalanceValue.setText("");
        this.lblLienSumValue.setText("");
        this.lblDepositAmtValue.setText("");
        this.lblFreezeSumValue.setText("");
        this.lblShadowLienValue.setText("");
    }

    private void updateTable() {
        this.tblLien.setModel(observable.getTbmLien());
        this.tblLien.revalidate();
    }

    private void updateAmountDetails() {
        this.lblClearBalanceValue.setText(observable.getLblClearBalance());
        this.lblLienSumValue.setText(observable.getLblExistingLienAmt());
        this.lblDepositAmtValue.setText(observable.getLblDepositAmt());
        this.lblFreezeSumValue.setText(observable.getLblExistingFreezeAmt());
        this.lblShadowLienValue.setText(observable.getLblShadowLien());
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_REJECT);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(int actionType) {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            int n = ClientUtil.confirmationAlert("Are you sure want to Reject", 1);
            if (n != 0) {
                return;
            }
        }
        if (!this.btnNew.isEnabled() && (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE
                || viewType == ClientConstants.ACTIONTYPE_EXCEPTION
                || viewType == ClientConstants.ACTIONTYPE_REJECT)) {
            System.out.println("asAS");
            observable.setActionType(actionType);
            this.updateLienOBFields();
            this.updateLOSLienOBFields();
            if (!observable.authorizeStatus()) {
                COptionPane.showMessageDialog(this, resourceBundle.getString("AUTHORIZE_WARNING"));
                return;
            }
            super.setOpenForEditBy(observable.getStatusBy1());
            super.removeEditLock(lblDepositLienDesc.getText());
            this.btnCancelActionPerformed(null);
            observable.setResultStatus();
            this.setButtonEnableDisable();
            btnSave.setEnabled(false);
            viewType = -1;
            return;
        }
        observable.setActionType(actionType);
        observable.setStatus();
        viewType = actionType;

        //__ To Save the data in the Internal Frame...
        setModified(true);

        HashMap mapParam = new HashMap();
        if (closingFlag == true) {
        } else {
            mapParam.put(CommonConstants.MAP_NAME, "getAuthorizeLienEntries");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLienTO");
            String authSts = "";
            if (actionType == 10) {
                authSts = "REJECTED";
            } else if (actionType == 8) {
                authSts = "AUTHORIZED";
            }
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authSts);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
        }
    }

    public void authorize(HashMap screenMap) {
        System.out.println("screenmap#####" + screenMap);
        ArrayList selectedList = (ArrayList) screenMap.get(CommonConstants.AUTHORIZEDATA);
        String authorizeStatus = (String) screenMap.get(CommonConstants.AUTHORIZESTATUS);
        HashMap dataMap;
        String status;
        double amount = 0, shadowLienAmt = 0, bal = 0;
        for (int i = 0, j = selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put("SELECTED_AUTHORIZE_STATUS", authorizeStatus);
            System.out.println("authorizedatamap#####" + dataMap);
            observable.setAuthorizeMap(dataMap);
            if (!observable.authorizeStatus()) {
                COptionPane.showMessageDialog(this, resourceBundle.getString("AUTHORIZE_WARNING"));
                return;
            }
        }
        dataMap = null;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
//         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDepositDetails);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLienInfo);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLoanotherSociety);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDeposit);
        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panDepositDetails) + new MandatoryCheck().checkMandatory(getClass().getName(), panLoanotherSociety);

        System.out.println("mandatoryMessage..." + mandatoryMessage);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            // displayAlert(mandatoryMessage);
        }

        updateOBAmountFields();
        observable.doAction();
        if (callFromOtherModule && observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            setLienChanged(true);
            this.dispose();
        }
        HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("LIENNO");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (observable.getProxyReturnMap() != null) {
                if (observable.getProxyReturnMap().containsKey("LIENNO")) {
                    lst = (ArrayList) observable.getProxyReturnMap().get("LIENNO");
                    for (int i = 0; i < lst.size(); i++) {
                        lockMap.put("LIENNO", lst.get(i));
                        setEditLockMap(lockMap);
                        setEditLock();
                    }
                }
            }
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap unlienMap = new HashMap();
            //System.out.println("actNum 111>>>"+actNum);
            System.out.println("CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected())>>>" + CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));
            unlienMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));
            List unlienList = ClientUtil.executeQuery("getDepositUnlienCheck", unlienMap);
            System.out.println("unlienList>>>" + unlienList);
            if (unlienList != null && unlienList.size() > 0) {
                unlienMap = (HashMap) unlienList.get(0);
                System.out.println("unlienMap>>>>" + unlienMap);
                String unlien = CommonUtil.convertObjToStr(unlienMap.get("DEPOSIT_UNLIEN"));
                System.out.println("unlien asss>>>>" + unlien);
                if (unlien.equals("Y") && chkDepositUnlien.isSelected()) {
                    String depNo = "";
                    String lienAmt = "";
                    unlienMap = new HashMap();
                    depNo = CommonUtil.convertObjToStr(txtDepositNo.getText());
                    unlienMap.put("BATCH_ID", depNo);
                    unlienMap.put("LIEN_DT", currDt.clone());
                    System.out.println("this.tblLien.getSelectedRow()111>>>" + this.tblLien.getSelectedRow());
                    if (this.tblLien.getSelectedRow() != -1) {
                        ArrayList arr = ((TableModel) tblLien.getModel()).getRow(this.tblLien.getSelectedRow());
                        lienAmt = CommonUtil.convertObjToStr(arr.get(1));
                        System.out.println("lienAmt???>>>>>" + lienAmt);
//                this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)),false);
                    } else {
                        ClientUtil.showMessageWindow("Please select the row in the grid before save");
                        return;
                    }
                    unlienMap.put("LIEN_AMT", CommonUtil.convertObjToDouble(lienAmt));
                    ClientUtil.execute("getUpdateDepositSubAcInfoStatusUnlien", unlienMap);
                    ClientUtil.execute("getUpdateDepositLienForDepositUnlien", unlienMap);
                }
            }
        }
        btnCancelActionPerformed(null);
        observable.setResultStatus();
        isMds = false;
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void updateOBAmountFields() {
        observable.setLblShadowLien(this.lblShadowLienValue.getText());
        observable.setChkLOS(chkLoanwithothersociety.isSelected());
    }

    private boolean checkValidLienAmount() {
        double amount = 0, lienAmount = 0;
        Double bal = CommonUtil.convertObjToDouble(this.lblClearBalanceValue.getText());
        if (bal != null) {
            amount = bal.doubleValue();
        }

        bal = CommonUtil.convertObjToDouble(this.txtLienAmount.getText());
        if (bal != null) {
            lienAmount = bal.doubleValue();
        }

        bal = CommonUtil.convertObjToDouble(this.lblShadowLienValue.getText());
        if (bal != null) {
            lienAmount += bal.doubleValue();
        }

        if (lienAmount > amount) {
            return false;
        }
        return true;
    }

    private void resetCustomerDetail() {
        this.lblCustomerIDValue.setText("");
        this.lblCustomerNameValue.setText("");
        this.lblCustomerNameValue.setToolTipText(lblCustomerNameValue.getText());
        this.lblLienCustIdValue.setText("");
        this.lblAccountHDValue.setText("");
        this.lblAccountHDValue.setToolTipText(lblAccountHDValue.getText());
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        isMds = false;
        for (int i = 0; i < tblLien.getRowCount(); i++) {
            String data = CommonUtil.convertObjToStr(tblLien.getValueAt(i, 0));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                setMode(ClientConstants.ACTIONTYPE_EDIT);
            }
            if (tblLien.getValueAt(i, 4) != null) //            if(observable.getAuthorizeStatus1()!=null)
            {
                super.removeEditLock(data);
            }
        }
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        resetCustomerDetail();
        this.panLienEnableDisable(false);
        panLOSLienEnableDisable(false);
        setFromLoanScreen(false);
        //__ Make the Screen Closable..
        setModified(false);
        if (callFromOtherModule) {
            setLienChanged(true);
            this.dispose();
        }
        chkLoanwithothersociety.setSelected(false);

        panLoanotherSociety.setVisible(false);
        panLienInfo.setVisible(true);
        chkDepositUnlien.setSelected(false);

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        setUp(ClientConstants.ACTIONTYPE_DELETE, false);
        callView(ClientConstants.ACTIONTYPE_DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        isMds = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        chkDepositUnlien.setEnabled(true);
        callView(ClientConstants.ACTIONTYPE_EDIT);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        isMds = false;
        setUp(ClientConstants.ACTIONTYPE_NEW, true);
        setButtonEnableDisable();
        enableDisableButtons(true);
        ClientUtil.enableDisable(this.panLienDetail, false);
        ClientUtil.enableDisable(this.panLoanotherSociety, false);
        chkLoanwithothersociety.setSelected(false);
        //__ To Save the data in the Internal Frame...

        setModified(true);
        chkLoanwithothersociety.setSelected(false);

        panLoanotherSociety.setVisible(false);
        panLienInfo.setVisible(true);
        btnLienNew.setEnabled(false);
        chkDepositUnlien.setSelected(false);
        chkDepositUnlien.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        this.btnPrintActionPerformed(evt);

    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        // Add your handling code here:
        callView(DEPOSIT_ACCT);
        this.txtDepositNo.setEnabled(false);
        this.tdtLienDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        this.tdtLienDate.setEnabled(true);
    }//GEN-LAST:event_btnDepositNoActionPerformed
    private void callView(int viewType) {
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        if (viewType == DEPOSIT_ACCT) {
            if (isMds) {
                viewMap.put(CommonConstants.MAP_NAME, "getChits");
                where.put("PRODID", ((ComboBoxModel) this.cboProductID.getModel()).getKeyForSelected());
                //  where.put("DEPFREEZECHECK","");
                where.put("DATE", currDt.clone());
                viewMap.put(CommonConstants.MAP_WHERE, where);
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getDepositAccounts");
                where.put("PRODID", ((ComboBoxModel) this.cboProductID.getModel()).getKeyForSelected());
                where.put("DEPFREEZECHECK", "");
                where.put("DATE", currDt.clone());
               //added by rishad for mantis 0010540 at 10/07/2015
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }

        } else if (viewType == LIEN_ACT_NUM) {
            viewMap.put(CommonConstants.MAP_NAME, "getDepositLienProdActNum");
            where.put("PRODID", ((ComboBoxModel) this.cboLienProductID.getModel()).getKeyForSelected());
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        } else if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "getEditLienEntries");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        } else if (viewType == ClientConstants.ACTIONTYPE_DELETE) {
            viewMap.put(CommonConstants.MAP_NAME, "getDeleteLienEntries");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);

        }
        new ViewAll(this, viewMap).show();
    }

    public void setViewType(int vuType) {
        viewType = vuType;
        callFromOtherModule = true;
        observable.setActionType(viewType);
        observable.setStatus();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            HashMap returnMap = null;
            if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, true);
                this.setButtonEnableDisable();
                this.enableDisableButtons(true);
                ClientUtil.enableDisable(this.panDepositDetails, false);
                this.btnDepositNo.setEnabled(false);
                ClientUtil.enableDisable(this.panLienInfo, false);
                ClientUtil.enableDisable(this.panLoanotherSociety, false);
            }
            if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
                chkDepositUnlien.setEnabled(true);
            }
            if (viewType == ClientConstants.ACTIONTYPE_DELETE) {
                this.setButtonEnableDisable();
                this.enableDisableButtons(false);
            }
            if (viewType == DEPOSIT_ACCT || viewType == ClientConstants.ACTIONTYPE_EDIT
                    || viewType == ClientConstants.ACTIONTYPE_DELETE
                    || viewType == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || viewType == ClientConstants.ACTIONTYPE_EXCEPTION
                    || viewType == ClientConstants.ACTIONTYPE_REJECT
                    || viewType == ClientConstants.ACTIONTYPE_VIEW) {

                this.txtDepositNo.setText((String) hashMap.get("DEPOSIT_ACT_NUM"));
                if (hashMap.containsKey("DEPOSIT_TYPE")) {
                    if (hashMap.get("DEPOSIT_TYPE").toString().equals("MDS")) {
                        isMds = true;
                    } else {
                        isMds = false;
                    }
                }

                observable.setIsMds(isMds);
                //HashMap chittalMap=new HashMap();
                //chittalMap.put("CHITTAL_NO", "")
                // List MdsList=ClientUtil.executeQuery("CheckIsMds", hashMap);
                if (hashMap.containsKey("LOANS_OTHER_SOCIETY")) {
                    if (hashMap.get("LOANS_OTHER_SOCIETY").equals("Y")) {
                        chkLoanwithothersociety.setSelected(true);
                        panLoanotherSociety.setVisible(true);
                        panLienInfo.setVisible(false);
                    } else {
                        chkLoanwithothersociety.setSelected(false);
                        panLoanotherSociety.setVisible(false);
                        panLienInfo.setVisible(true);
                    }
                }
                observable.setTxtDepositNo(txtDepositNo.getText());
                observable.setChkLOS(chkLoanwithothersociety.isSelected());
                if (!isMds) {
                    returnMap = observable.getDepositActIfno();
                } else {
                    returnMap = observable.getMDSActIfno();
                }

                this.lblCustomerIDValue.setText((String) returnMap.get("CUST_ID"));
                this.lblCustomerNameValue.setText((String) returnMap.get("CUSTOMER_NAME"));
                this.lblCustomerNameValue.setToolTipText(lblCustomerNameValue.getText());
                if (!isMds) {
                    observable.getSubDepositNos(this.txtDepositNo.getText());
                }
                returnMap.put("MEMBER_NO", returnMap.get("CUST_ID"));
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", returnMap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    txtDepositNo.setText("");
                    lblCustomerIDValue.setText("");
                    return;
                }
//                if(hashMap.containsKey("DEFAULT"))
//                    this.cboSubDepositNo.setSelectedItem("1");
//                else
                if (isMds) {
                    List subNoList = ClientUtil.executeQuery("getSubNosChits", hashMap);
                    if (subNoList != null && subNoList.size() > 0) {
                        String add[] = new String[subNoList.size() + 1];
//                add[0]=" ";
                        ArrayList key = new ArrayList();
                        ArrayList value = new ArrayList();
                        key.add("");
                        value.add("");
                        for (int k = 0; k < subNoList.size(); k++) {
                            HashMap subListMap = (HashMap) subNoList.get(0);
                            if (subListMap != null && subListMap.containsKey("SUB_NO") && subListMap.get("SUB_NO") != null && !subListMap.get("SUB_NO").toString().equals("")) {
                                key.add(subListMap.get("SUB_NO").toString());
                                value.add(subListMap.get("SUB_NO").toString());

                            }
                        }
                        ComboBoxModel subdepoNo = new ComboBoxModel(key, value);
                        observable.setCbmSubDepositNos(subdepoNo);
                        this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
                        this.cboSubDepositNoActionPerformed(null);
                    } else {
                        ArrayList key = new ArrayList();
                        ArrayList value = new ArrayList();
                        key.add("1");
                        value.add("1");
                        ComboBoxModel subdepoNo = new ComboBoxModel(key, value);
                        observable.setCbmSubDepositNos(subdepoNo);
                        this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
                        this.cboSubDepositNoActionPerformed(null);

                    }
                }
                this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
                this.cboSubDepositNoActionPerformed(null);
                btnLienNew.setEnabled(false);
            }
            if (viewType == ClientConstants.ACTIONTYPE_EDIT
                    || viewType == ClientConstants.ACTIONTYPE_DELETE
                    || viewType == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || viewType == ClientConstants.ACTIONTYPE_EXCEPTION
                    || viewType == ClientConstants.ACTIONTYPE_REJECT
                    || viewType == ClientConstants.ACTIONTYPE_VIEW) {

                ((ComboBoxModel) this.cboProductID.getModel()).setKeyForSelected((String) returnMap.get("PROD_ID"));
                this.lblAccountHDValue.setText((String) returnMap.get("HD_DESC"));
                System.out.println("@#@#@#@# SUBNO : "+CommonUtil.convertObjToStr(hashMap.get("SUBNO")));
                //((ComboBoxModel) this.cboSubDepositNo.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(hashMap.get("SUBNO")));
                ((ComboBoxModel) this.cboSubDepositNo.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(hashMap.get("SUBNO")));
                //observable.setCboSubDepositNo(CommonUtil.convertObjToInt(hashMap.get("SUBNO")));
                observable.setLblDepositLienDesc((String) hashMap.get("LIENNO"));
                this.cboSubDepositNoActionPerformed(null);
            }
            if (viewType == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || viewType == ClientConstants.ACTIONTYPE_EXCEPTION
                    || viewType == ClientConstants.ACTIONTYPE_REJECT) {
                observable.populateLien((String) hashMap.get("LIENNO"));
                observable.setAuthorizeMap(hashMap);
                populateLienDetail();
                setAuthorizeButtons();
            }
            if (viewType == LIEN_ACT_NUM) {
                System.out.println("$$$$$$HASHMAP" + hashMap);
                this.txtLienActNum.setText((String) hashMap.get("ACT_NUM"));
                observable.setLienAccountNo((String) hashMap.get("ACT_NUM"));
                this.lblLienCustIdValue.setText((String) hashMap.get("CUSTOMER_NAME") + " [" + (String) hashMap.get("CUST_ID") + "]");
                this.lblLienCustIdValue.setToolTipText(lblLienCustIdValue.getText());

                if (hashMap.containsKey("DEFAULT")) {
                    String custName = observable.getLienActNumCustomer((String) hashMap.get("ACT_NUM"));
                    lblLienCustIdValue.setText(custName);
                    System.out.println("cussttt" + custName);
                } else {
                    observable.getLienActNumCustomer((String) hashMap.get("ACT_NUM"));
                }
                System.out.println("asdasd" + hashMap.get("AMOUNT"));
                String s = hashMap.get("AMOUNT").toString();
                System.out.println("sss" + s);
                txtLienAmount.setText(s);
                System.out.println("txtLienAmount" + txtLienAmount.getText());
                observable.setLienAmount(s);
                System.out.println(hashMap);
                hashMap.put("MEMBER_NO", hashMap.get("CUST_ID").toString());
                System.out.println("hhhassshhh" + hashMap);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashMap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    txtLienActNum.setText("");
                    lblLienCustIdValue.setText("");
                    return;
                }
            }
//            if(observable.getUnLienRemark()!=null && observable.getUnLienRemark().equals("FLEXI_DEPOSITS"))
//                btnSave.setEnabled(false);

            hashMap = null;
            returnMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //__ To Save the data in the Internal Frame...
        if (!isFromLoanScreen()) {
            setModified(true);
        }
    }

    private void setAuthorizeButtons() {
        this.btnDelete.setEnabled(false);
        this.btnNew.setEnabled(false);
        this.btnEdit.setEnabled(false);
        this.btnSave.setEnabled(false);
        this.btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        if (isFromLoanScreen()) {
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnCancel.setEnabled(false);
            setModified(false);
        }
    }
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        String prodId = (String) ((ComboBoxModel) this.cboProductID.getModel()).getKeyForSelected();
        if (prodId != null && prodId.length() > 0) {
            HashMap mdsMap = new HashMap();
            mdsMap.put("SCHEME_NAME", prodId);
            List isMdsList = ClientUtil.executeQuery("checkIsMds", mdsMap);
            if (isMdsList != null && isMdsList.size() > 0) {
                mdsMap = (HashMap) isMdsList.get(0);
                if (mdsMap.containsKey("RECEIPT_HEAD") && mdsMap.get("RECEIPT_HEAD") != null && !mdsMap.get("RECEIPT_HEAD").toString().equals("")) {
                    isMds = true;
                    observable.setIsMds(isMds);
                    String actHead = mdsMap.get("RECEIPT_HEAD").toString();
                    this.lblAccountHDValue.setText(actHead);
                    this.lblAccountHDValue.setToolTipText(lblAccountHDValue.getText());
                }
            } else {
//        if(prodId!=null && prodId.length()>0){
                isMds = false;
                observable.setIsMds(isMds);
                String actHead = observable.getAccountHead(prodId);
                this.lblAccountHDValue.setText(actHead);
                this.lblAccountHDValue.setToolTipText(lblAccountHDValue.getText());
//        }
            }
        } else {
            observable.resetForm();
        }
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void txtDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepositNoActionPerformed
    /* Auto Generated Method - setMandatoryHashMap()
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("cboSubDepositNo", new Boolean(true));
        mandatoryMap.put("tdtLienDate", new Boolean(true));
        mandatoryMap.put("txtLienAmount", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(true));
        mandatoryMap.put("cboLienProductID", new Boolean(true));
        mandatoryMap.put("txtLienActNum", new Boolean(true));
        mandatoryMap.put("cboCreditType", new Boolean(true));
        mandatoryMap.put("cboLienLoanType", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyLienAmount", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyRemark", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyLienCustName", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyLienAcNo", new Boolean(true));
        mandatoryMap.put("tdtLoanOtherSocietyLienDate", new Boolean(true));

    }
    /* Auto Generated Method - getMandatoryHashMap()
    Getter method for setMandatoryHashMap().*/

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void update(Observable o, Object arg) {
        this.lblStatus.setText(observable.getLblStatus());

        this.cboLienProductID.setModel(observable.getCbmLienProductId());
        this.cboCreditType.setModel(observable.getCbmCreditType());
        this.cboProductID.setModel(observable.getCbmProductId());
        this.cboLienLoanType.setModel(observable.getCbmLOSLoanType());
        this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
        this.txtDepositNo.setText(observable.getTxtDepositNo());
        this.updateTable();
        if (observable.getChkLOS()) {
            panLoanotherSociety.setVisible(true);
            panLienInfo.setVisible(false);
        } else {
            panLienInfo.setVisible(true);
            panLoanotherSociety.setVisible(false);
        }
        this.populateLienDetail();
        this.updateAmountDetails();
        resetCustomerDetail();
    }

    private void updateBalance(String amount, boolean add) {
        double shadowLienValue = 0, lienAmt = 0;
        if (amount != null && amount.length() > 0) {
            Double amt = CommonUtil.convertObjToDouble(amount);
            if (amt != null) {
                lienAmt = amt.doubleValue();

                amt = CommonUtil.convertObjToDouble(this.lblShadowLienValue.getText());
                if (amt != null) {
                    shadowLienValue = amt.doubleValue();
                }

                if (add) {
                    shadowLienValue += lienAmt;
                }
                if (!add) {
                    shadowLienValue -= lienAmt;
                }
                this.lblShadowLienValue.setText(String.valueOf(shadowLienValue));
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLienActNum;
    private com.see.truetransact.uicomponent.CButton btnLienDelete;
    private com.see.truetransact.uicomponent.CButton btnLienNew;
    private com.see.truetransact.uicomponent.CButton btnLienSave;
    private com.see.truetransact.uicomponent.CButton btnLoanOtherSocietyLienDelete;
    private com.see.truetransact.uicomponent.CButton btnLoanOtherSocietyLienNew;
    private com.see.truetransact.uicomponent.CButton btnLoanOtherSocietyLienSave;
    private com.see.truetransact.uicomponent.CButton btnLoanOtherSocietyUnLien;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUnLien;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCreditType;
    private com.see.truetransact.uicomponent.CComboBox cboLienLoanType;
    private com.see.truetransact.uicomponent.CComboBox cboLienProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboSubDepositNo;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositUnlien;
    private com.see.truetransact.uicomponent.CCheckBox chkLoanwithothersociety;
    private com.see.truetransact.uicomponent.CLabel lblAccountHD;
    private com.see.truetransact.uicomponent.CLabel lblAccountHDValue;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditLienAccount;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIDValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmt;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositLienDesc;
    private com.see.truetransact.uicomponent.CLabel lblDepositLienDesc2;
    private com.see.truetransact.uicomponent.CLabel lblDepositLienSLNO;
    private com.see.truetransact.uicomponent.CLabel lblDepositLienSLNO2;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSum;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSumValue;
    private com.see.truetransact.uicomponent.CLabel lblLLoanOtherSocietyLienAcNo;
    private com.see.truetransact.uicomponent.CLabel lblLienAccHDValue;
    private com.see.truetransact.uicomponent.CLabel lblLienAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblLienAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblLienAmount;
    private com.see.truetransact.uicomponent.CLabel lblLienCustId;
    private com.see.truetransact.uicomponent.CLabel lblLienCustIdValue;
    private com.see.truetransact.uicomponent.CLabel lblLienDate;
    private com.see.truetransact.uicomponent.CLabel lblLienProductID;
    private com.see.truetransact.uicomponent.CLabel lblLienSum;
    private com.see.truetransact.uicomponent.CLabel lblLienSumValue;
    private com.see.truetransact.uicomponent.CLabel lblLoanOtherSocietyLienAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanOtherSocietyLienCustName;
    private com.see.truetransact.uicomponent.CLabel lblLoanOtherSocietyLienDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanOtherSocietyLoanType;
    private com.see.truetransact.uicomponent.CLabel lblLoanOtherSocietyRemark;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRemark;
    private com.see.truetransact.uicomponent.CLabel lblShadowLien;
    private com.see.truetransact.uicomponent.CLabel lblShadowLienValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubDepositNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCustomer;
    private com.see.truetransact.uicomponent.CPanel panDeposit;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositNo;
    private com.see.truetransact.uicomponent.CPanel panLien;
    private com.see.truetransact.uicomponent.CPanel panLienActNum;
    private com.see.truetransact.uicomponent.CPanel panLienButtons;
    private com.see.truetransact.uicomponent.CPanel panLienButtons2;
    private com.see.truetransact.uicomponent.CPanel panLienDetail;
    private com.see.truetransact.uicomponent.CPanel panLienInfo;
    private com.see.truetransact.uicomponent.CPanel panLienTable;
    private com.see.truetransact.uicomponent.CPanel panLoanotherSociety;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CSeparator sptDeposit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpLien;
    private com.see.truetransact.uicomponent.CTable tblLien;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtLienDate;
    private com.see.truetransact.uicomponent.CDateField tdtLoanOtherSocietyLienDate;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtLienActNum;
    private com.see.truetransact.uicomponent.CTextField txtLienAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanOtherSocietyLienAcNo;
    private com.see.truetransact.uicomponent.CTextField txtLoanOtherSocietyLienAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanOtherSocietyLienCustName;
    private com.see.truetransact.uicomponent.CTextField txtLoanOtherSocietyRemark;
    private com.see.truetransact.uicomponent.CTextField txtRemark;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DepositLienUI lui = new DepositLienUI();
        JFrame j = new JFrame();
        j.getContentPane().add(lui);
        j.setSize(615, 600);
        j.show();
        lui.show();
    }

    /**
     * Getter for property lienChanged.
     * @return Value of property lienChanged.
     */
    public boolean isLienChanged() {
        return lienChanged;
    }

    /**
     * Setter for property lienChanged.
     * @param lienChanged New value of property lienChanged.
     */
    public void setLienChanged(boolean lienChanged) {
        this.lienChanged = lienChanged;
    }

    /**
     * Getter for property loanLienDisable.
     * @return Value of property loanLienDisable.
     */
    public boolean isLoanLienDisable() {
        return loanLienDisable;
    }

    /**
     * Setter for property loanLienDisable.
     * @param loanLienDisable New value of property loanLienDisable.
     */
    public void setLoanLienDisable(boolean loanLienDisable) {
        this.loanLienDisable = loanLienDisable;
    }

    /**
     * Getter for property fromLoanScreen.
     * @return Value of property fromLoanScreen.
     */
    public boolean isFromLoanScreen() {
        return fromLoanScreen;
    }

    /**
     * Setter for property fromLoanScreen.
     * @param fromLoanScreen New value of property fromLoanScreen.
     */
    public void setFromLoanScreen(boolean fromLoanScreen) {
        this.fromLoanScreen = fromLoanScreen;
    }
}
