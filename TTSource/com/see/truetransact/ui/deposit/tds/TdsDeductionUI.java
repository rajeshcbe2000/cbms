/******************************************************************************
 * 1> Proper data to be filled in Collection Type                             *
 *    (Lookup_Master --> LOOKUP_ID = DEPOSIT.TDS )                            *
 * 2>  Debit Account Number has to be Corrected  (Button)                     *
 * 3>  TDS Till date and TDS Interest Till Date has to be Corrected (Lables)  *
 *****************************************************************************/


/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsDeductionUI.java
 *
 * Created on March 22, 2004, 12:29 PM
 */

package com.see.truetransact.ui.deposit.tds;

import com.see.truetransact.ui.deposit.tds.TdsDeductionRB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.deposit.tds.TdsDeductionOB;
import com.see.truetransact.ui.deposit.tds.TdsDeductionMRB;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;

import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.interestcalc.InterestCalculation;
import com.see.truetransact.commonutil.interestcalc.DateDifference;
import com.see.truetransact.commonutil.interestcalc.Rounding;

import com.see.truetransact.ui.common.interestcalc.InterestCalculationOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.deposit.tds.TdsDeductionTO;

/**
 *
 * @author  Lohith R.
 */

public class TdsDeductionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField {
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap mandatoryMap;
    private TdsDeductionOB observable;
    final int EDIT=0,DELETE=1,DEPOSITNUMBER=2,DEBITACCOUNTHEAD=3,DEBITACCOUNTNUMBER=4, VIEW=5,CUST_ID=6,AUTH=7,REJECT=8;
    int ACTION=-1;
    String depositNumber = new String();
    String prodType="";
    String   behaviesLike="";
    String   intPayFreq="";
    private Date matDt = null;
    private String custType="";
    String viewType="";
    boolean isFilled=false;
    private Date currDt = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.tds.TdsDeductionRB", ProxyParameters.LANGUAGE);
    
    /** Creates new form TdsDeductionUI */
    public TdsDeductionUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUP();
        
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();
        visibleFalseFields();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setMaximumLength();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMain);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        btnCustId.setEnabled(true);
        observable.resetStatus();
        observable.resetForm();
    }
    
    /** Implementing Singleton pattern */
    private void setObservable() {
        observable = TdsDeductionOB.getInstance();
        observable.addObserver(this);
    }
    
    private void  visibleFalseFields(){
        cboCollectionType.setVisible(true);
        cboDepositSubNo.setVisible(true);
        cboProductID.setVisible(true);
        dateMaturityDate.setVisible(true);
        dateTDSEndDate.setVisible(false);
        dateTDSStartDate.setVisible(false);
        dateDepositDate.setVisible(true);
        lblAccountHead.setVisible(false);
        lblCollectionType.setVisible(true);
        lblDebitAccHead.setVisible(false);
        lblDebitAccNum.setVisible(true);
        lblDepositNo.setVisible(true);
        btnDepositNo.setVisible(true);
        lblDepositAmount.setVisible(true);
        lblDepositDate.setVisible(true);
        lblDepositSubNo.setVisible(true);
        lblInterestAccrued.setVisible(false);
        lblInterestAccruedPercentage.setVisible(false);
        lblInterestPaid.setVisible(false);
        lblInterestPaidPercentage.setVisible(false);
        lblInterestPayable.setVisible(false);
        lblInterestPayablePercentage.setVisible(false);
        lblMaturityDate.setVisible(true);
        lblMsg.setVisible(true);
        lblProductID.setVisible(true);
        lblRemarks.setVisible(false);
        lblSpace1.setVisible(false);
        lblSpace2.setVisible(false);
        lblSpace3.setVisible(true);
        lblStatus.setVisible(true);
        lblTDSEndDate.setVisible(false);
        lblTDSInterestTillDate.setVisible(false);
        lblTDSInterestTillDt.setVisible(true);
        lblTDSStartDate.setVisible(false);
        lblTDSTillDate.setVisible(true);
        lblTDSTillDt.setVisible(true);
        lblTdsAmount.setVisible(false);
        lblTDSInterestTillDate.setVisible(true);
        
        //        mbrMain.setVisible(false);
        //        panInterestAccrued.setVisible(false);
        //        panInterestPaid.setVisible(false);
        //        panInterestPayable.setVisible(false);
        //        panMain.setVisible(false);
        //        panProductID.setVisible(false);
        //        panStatus.setVisible(false);
        txtDebitAccHead.setVisible(false);
        txtDebitAccNum.setVisible(true);
        txtDepositNo.setVisible(true);
        cLabel1.setVisible(false);
        btnDebitAccHead.setVisible(false);
        txtDepositAmount.setVisible(true);
        txtInterestAccrued.setVisible(false);
        txtInterestPaid.setVisible(false);
        txtInterestPayable.setVisible(false);
        txtRemarks.setVisible(false);
        txtTdsAmount.setVisible(false);
        
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDebitAccHead.setName("btnDebitAccHead");
        btnDebitAccNum.setName("btnDebitAccNum");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cLabel1.setName("cLabel1");
        cboCollectionType.setName("cboCollectionType");
        cboDepositSubNo.setName("cboDepositSubNo");
        cboProductID.setName("cboProductID");
        dateMaturityDate.setName("dateMaturityDate");
        dateTDSEndDate.setName("dateTDSEndDate");
        dateTDSStartDate.setName("dateTDSStartDate");
        dateDepositDate.setName("datelDepositDate");
        lblAccountHead.setName("lblAccountHead");
        lblCollectionType.setName("lblCollectionType");
        lblDebitAccHead.setName("lblDebitAccHead");
        lblDebitAccNum.setName("lblDebitAccNum");
        lblDepositNo.setName("lblDepositNo");
        btnDepositNo.setName("btnDepositNo");
        lblDepositAmount.setName("lblDepositAmount");
        lblDepositDate.setName("lblDepositeDate");
        lblDepositSubNo.setName("lblDepositSubNo");
        lblInterestAccrued.setName("lblInterestAccrued");
        lblInterestAccruedPercentage.setName("lblInterestAccruedPercentage");
        lblInterestPaid.setName("lblInterestPaid");
        lblInterestPaidPercentage.setName("lblInterestPaidPercentage");
        lblInterestPayable.setName("lblInterestPayable");
        lblInterestPayablePercentage.setName("lblInterestPayablePercentage");
        lblMaturityDate.setName("lblMaturityDate");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTDSEndDate.setName("lblTDSEndDate");
        lblTDSInterestTillDate.setName("lblTDSInterestTillDate");
        lblTDSInterestTillDt.setName("lblTDSInterestTillDt");
        lblTDSStartDate.setName("lblTDSStartDate");
        lblTDSTillDate.setName("lblTDSTillDate");
        lblTDSTillDt.setName("lblTDSTillDt");
        lblTdsAmount.setName("lblTdsAmount");
        mbrMain.setName("mbrMain");
        panInterestAccrued.setName("panInterestAccrued");
        panInterestPaid.setName("panInterestPaid");
        panInterestPayable.setName("panInterestPayable");
        panMain.setName("panMain");
        panProductID.setName("panProductID");
        panStatus.setName("panStatus");
        txtDebitAccHead.setName("txtDebitAccHead");
        txtDebitAccNum.setName("txtDebitAccNum");
        txtDepositNo.setName("txtDepositNo");
        txtDepositAmount.setName("txtDepositAmount");
        txtInterestAccrued.setName("txtInterestAccrued");
        txtInterestPaid.setName("txtInterestPaid");
        txtInterestPayable.setName("txtInterestPayable");
        txtRemarks.setName("txtRemarks");
        txtTdsAmount.setName("txtTdsAmount");
    }
    
    private void internationalize() {
        //        TdsDeductionRB resourceBundle = new TdsDeductionRB();
        lblTDSEndDate.setText(resourceBundle.getString("lblTDSEndDate"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTDSInterestTillDt.setText(resourceBundle.getString("lblTDSInterestTillDt"));
        lblDebitAccNum.setText(resourceBundle.getString("lblDebitAccNum"));
        lblInterestPayablePercentage.setText(resourceBundle.getString("lblInterestPayablePercentage"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblTDSTillDt.setText(resourceBundle.getString("lblTDSTillDt"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnDebitAccHead.setText(resourceBundle.getString("btnDebitAccHead"));
        ((javax.swing.border.TitledBorder)panProductID.getBorder()).setTitle(resourceBundle.getString("panProductID"));
        lblInterestPaid.setText(resourceBundle.getString("lblInterestPaid"));
        lblDepositDate.setText(resourceBundle.getString("lblDepositDate"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
        btnDepositNo.setText(resourceBundle.getString("btnDepositNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblInterestPaidPercentage.setText(resourceBundle.getString("lblInterestPaidPercentage"));
        lblTdsAmount.setText(resourceBundle.getString("lblTdsAmount"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblInterestAccrued.setText(resourceBundle.getString("lblInterestAccrued"));
        lblTDSInterestTillDate.setText(resourceBundle.getString("lblTDSInterestTillDate"));
        lblInterestAccruedPercentage.setText(resourceBundle.getString("lblInterestAccruedPercentage"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblDepositAmount.setText(resourceBundle.getString("lblDepositAmount"));
        btnDebitAccNum.setText(resourceBundle.getString("btnDebitAccNum"));
        lblTDSTillDate.setText(resourceBundle.getString("lblTDSTillDate"));
        lblCollectionType.setText(resourceBundle.getString("lblCollectionType"));
        lblDebitAccHead.setText(resourceBundle.getString("lblDebitAccHead"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDepositSubNo.setText(resourceBundle.getString("lblDepositSubNo"));
        lblTDSStartDate.setText(resourceBundle.getString("lblTDSStartDate"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblInterestPayable.setText(resourceBundle.getString("lblInterestPayable"));
        cLabel1.setText(resourceBundle.getString("cLabel1"));
    }
    
    /** To set the fields as Compulsory input. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboDepositSubNo", new Boolean(true));
        mandatoryMap.put("txtDebitAccNum", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtTdsAmount", new Boolean(false));
        mandatoryMap.put("txtDepositAmount", new Boolean(true));
        mandatoryMap.put("dateDepositDate", new Boolean(true));
        mandatoryMap.put("dateTDSStartDate", new Boolean(false));
        mandatoryMap.put("dateMaturityDate", new Boolean(true));
        mandatoryMap.put("dateTDSEndDate", new Boolean(false));
        mandatoryMap.put("txtDebitAccHead", new Boolean(false));
        mandatoryMap.put("cboCollectionType", new Boolean(false));
        mandatoryMap.put("txtInterestRate", new Boolean(false));
        mandatoryMap.put("txtInterestAccured", new Boolean(false));
        mandatoryMap.put("txtInterestPaid", new Boolean(false));
    }
    
    /** To get fields which are Compulsory. */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void update(Observable observed, Object arg) {
        cboProductID.setSelectedItem(observable.getCboProductID());
        cboDepositSubNo.setSelectedItem(observable.getCboDepositSubNo());
        cboDpBheaviesLike.setSelectedItem(observable.getCboDpBehiveLike());
        txtDebitAccNum.setText(observable.getTxtDebitAccNum());
        txtRemarks.setText(observable.getTxtRemarks());
        txtTdsAmount.setText(observable.getTxtTdsAmount());
        txtDepositAmount.setText(observable.getTxtDepositAmount());
        txtDebitAccHead.setText(observable.getTxtDebitAccHead());
        txtInterestPayable.setText(observable.getTxtInterestPayable());
        txtInterestAccrued.setText(observable.getTxtInterestAccrued());
        txtInterestPaid.setText(observable.getTxtInterestPaid());
        dateDepositDate.setDateValue(observable.getDateDepositDate());
        dateTDSStartDate.setDateValue(observable.getDateTDSStartDate());
        dateTDSEndDate.setDateValue(observable.getDateTDSEndDate());
        dateMaturityDate.setDateValue(observable.getDateMaturityDate());
        txtDepositNo.setText(observable.getTxtDepositNo());
        cboCollectionType.setSelectedItem(observable.getCboCollectionType());
        txtTotalInt.setText(observable.getTotIntAmt());
        txtIntForFin.setText(observable.getFinIntAmt());
        lblStatus.setText(observable.getLblStatus());
        tblData.setModel(observable.getTblInterestDet());
        lblTdsIdValue.setText(observable.getTdsId());
    }
    
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        observable.setCboCollectionType(((ComboBoxModel)cboCollectionType.getModel()).getKeyForSelected().toString());
        observable.setTxtDebitAccNum(txtDebitAccNum.getText());
        observable.setTxtDepositNo(txtDepositNo.getText());
        observable.setCboDepositSubNo(((ComboBoxModel)cboDepositSubNo.getModel()).getKeyForSelected().toString());
        //        observable.setCboDepositSubNo((String) cboDepositSubNo.getSelectedItem());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtTdsAmount(txtTdsAmount.getText());
        observable.setTxtDepositAmount(txtDepositAmount.getText());
        observable.setTxtDebitAccHead(txtDebitAccHead.getText());
        observable.setTxtInterestPayable(txtInterestPayable.getText());
        observable.setTxtInterestAccrued(txtInterestAccrued.getText());
        observable.setTxtInterestPaid(txtInterestPaid.getText());
        observable.setDateDepositDate(dateDepositDate.getDateValue());
        observable.setDateTDSStartDate(dateTDSStartDate.getDateValue());
        observable.setDateTDSEndDate(dateTDSEndDate.getDateValue());
        observable.setDateMaturityDate(dateMaturityDate.getDateValue());
        observable.setTdaId(CommonUtil.convertObjToStr(lblTdsIdValue.getText()));
        observable.setCboDpBehiveLike(((ComboBoxModel)cboDpBheaviesLike.getModel()).getKeyForSelected().toString());
        
    }
    
    private void initComponentData() {
        cboCollectionType.setModel(observable.getCbmCollectionType());
        cboProductID.setModel(observable.getCbmProductID());
        cboDepositSubNo.setModel(observable.getCbmDepositSubNo());
        cboDpBheaviesLike.setModel(observable.getCbmDpBehiveLike());
    }
    
    private void setMaximumLength(){
        txtDepositNo.setMaxLength(16);
        txtDebitAccHead.setMaxLength(16);
        txtDebitAccNum.setMaxLength(16);
        txtRemarks.setMaxLength(512);
        
        //        txtDepositAmount.setMaxLength(4);
        txtDepositAmount.setValidation(new NumericValidation(14,2));
        
        //        txtInterestAccrued.setMaxLength(6);
        txtInterestAccrued.setValidation(new PercentageValidation());
        //        txtInterestPayable.setMaxLength(6);
        txtInterestPayable.setValidation(new PercentageValidation());
        //        txtInterestPaid.setMaxLength(6);
        txtInterestPaid.setValidation(new PercentageValidation());
        //        txtTdsAmount.setMaxLength(16);
        txtTdsAmount.setValidation(new NumericValidation(14,2));
        txtDpAMt.setValidation(new CurrencyValidation(14, 2));
        txtInterestRate.setValidation(new PercentageValidation());
        txtPeriodOfDeposit_Days.setValidation(new NumericValidation(14,2));
        txtPeriodOfDeposit_Months.setValidation(new NumericValidation(14,2));
        txtPeriodOfDeposit_Years.setValidation(new NumericValidation(14,2));
        txtTotalInt.setValidation(new CurrencyValidation(14, 2));
        txtIntForFin.setValidation(new CurrencyValidation(14, 2));
        txTottIntForFin.setValidation(new CurrencyValidation(14, 2));
        txtTdsAmountValue.setValidation(new CurrencyValidation(14, 2));
        txtCustId.setAllowAll(true);
        
    }
    
    /** To set Help messages according to the fields. */
    public void setHelpMessage() {
        TdsDeductionMRB objMandatoryRB = new TdsDeductionMRB();
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        cboDepositSubNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepositSubNo"));
        txtDebitAccNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAccNum"));
        txtDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtTdsAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTdsAmount"));
        txtDepositAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositAmount"));
        dateDepositDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateDepositDate"));
        dateTDSStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateTDSStartDate"));
        dateMaturityDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateMaturityDate"));
        dateTDSEndDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateTDSEndDate"));
        txtDebitAccHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAccHead"));
        cboCollectionType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCollectionType"));
        txtInterestPayable.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestPayable"));
        txtInterestAccrued.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestAccrued"));
        txtInterestPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestPaid"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrMain = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblDebitAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        cboDepositSubNo = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositDate = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmount = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPayable = new com.see.truetransact.uicomponent.CLabel();
        lblTDSStartDate = new com.see.truetransact.uicomponent.CLabel();
        lblTDSTillDt = new com.see.truetransact.uicomponent.CLabel();
        lblCollectionType = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTDSEndDate = new com.see.truetransact.uicomponent.CLabel();
        lblTDSInterestTillDt = new com.see.truetransact.uicomponent.CLabel();
        lblTdsAmount = new com.see.truetransact.uicomponent.CLabel();
        lblDebitAccNum = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtTdsAmount = new com.see.truetransact.uicomponent.CTextField();
        txtDepositAmount = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblDepositSubNo = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblInterestAccrued = new com.see.truetransact.uicomponent.CLabel();
        dateDepositDate = new com.see.truetransact.uicomponent.CDateField();
        dateTDSStartDate = new com.see.truetransact.uicomponent.CDateField();
        dateMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        dateTDSEndDate = new com.see.truetransact.uicomponent.CDateField();
        txtDebitAccHead = new com.see.truetransact.uicomponent.CTextField();
        cboCollectionType = new com.see.truetransact.uicomponent.CComboBox();
        panInterestPayable = new com.see.truetransact.uicomponent.CPanel();
        lblInterestPayablePercentage = new com.see.truetransact.uicomponent.CLabel();
        txtInterestPayable = new com.see.truetransact.uicomponent.CTextField();
        panInterestAccrued = new com.see.truetransact.uicomponent.CPanel();
        lblInterestAccruedPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtInterestAccrued = new com.see.truetransact.uicomponent.CTextField();
        panInterestPaid = new com.see.truetransact.uicomponent.CPanel();
        lblInterestPaidPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtInterestPaid = new com.see.truetransact.uicomponent.CTextField();
        lblTDSTillDate = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblTDSInterestTillDate = new com.see.truetransact.uicomponent.CLabel();
        btnDebitAccHead = new com.see.truetransact.uicomponent.CButton();
        panDepositNo = new com.see.truetransact.uicomponent.CPanel();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        panDebitAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtDebitAccNum = new com.see.truetransact.uicomponent.CTextField();
        btnDebitAccNum = new com.see.truetransact.uicomponent.CButton();
        lblTdsIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblTdsId = new com.see.truetransact.uicomponent.CLabel();
        panProductID1 = new com.see.truetransact.uicomponent.CPanel();
        panNewDepositdetails = new com.see.truetransact.uicomponent.CPanel();
        lblCust_id = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmount1 = new com.see.truetransact.uicomponent.CLabel();
        lblInterestRateId = new com.see.truetransact.uicomponent.CLabel();
        lblDeBehaviesLike = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaid1 = new com.see.truetransact.uicomponent.CLabel();
        lblTdsAmount1 = new com.see.truetransact.uicomponent.CLabel();
        txTottIntForFin = new com.see.truetransact.uicomponent.CTextField();
        txtDpAMt = new com.see.truetransact.uicomponent.CTextField();
        lblInterestAccrued1 = new com.see.truetransact.uicomponent.CLabel();
        cboDpBheaviesLike = new com.see.truetransact.uicomponent.CComboBox();
        panInterestPayable1 = new com.see.truetransact.uicomponent.CPanel();
        txtInterestRate = new com.see.truetransact.uicomponent.CTextField();
        lblInterestPayablePercentage1 = new com.see.truetransact.uicomponent.CLabel();
        lblCust_NameID = new com.see.truetransact.uicomponent.CLabel();
        lblCust_Name = new com.see.truetransact.uicomponent.CLabel();
        lblTdsAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTdsAmountValue = new com.see.truetransact.uicomponent.CTextField();
        panPeriodOfDeposit = new com.see.truetransact.uicomponent.CPanel();
        lblPeriodOfDeposit = new com.see.truetransact.uicomponent.CLabel();
        panCustomerId = new com.see.truetransact.uicomponent.CPanel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        panPeriodOfDeposit1 = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodOfDeposit_Years = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Years = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodOfDeposit_Months = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Months = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodOfDeposit_Days = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Days = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInt = new com.see.truetransact.uicomponent.CTextField();
        txtIntForFin = new com.see.truetransact.uicomponent.CTextField();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        cboIntPayFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblIntPayFreq = new com.see.truetransact.uicomponent.CLabel();
        lblDisCountYesNo = new com.see.truetransact.uicomponent.CLabel();
        panDisCountYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoStandingInstruction_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStandingInstruction_No = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestAccrued2 = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
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
        setTitle("Mutual TDS Deduction");
        setMaximumSize(new java.awt.Dimension(750, 550));
        setMinimumSize(new java.awt.Dimension(750, 550));
        setModified(true);
        setPreferredSize(new java.awt.Dimension(750, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

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
        tbrMain.add(btnView);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace22);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMain.add(btnDelete);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMain.add(btnSave);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace23);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMain.add(btnAuthorize);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace24);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrMain.add(btnPrint);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace25);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        panMain.setMinimumSize(new java.awt.Dimension(700, 450));
        panMain.setPreferredSize(new java.awt.Dimension(700, 450));
        panMain.setLayout(new java.awt.GridBagLayout());

        cTabbedPane1.setMaximumSize(new java.awt.Dimension(685, 450));
        cTabbedPane1.setMinimumSize(new java.awt.Dimension(685, 450));
        cTabbedPane1.setPreferredSize(new java.awt.Dimension(685, 450));

        panProductID.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductID.setMaximumSize(new java.awt.Dimension(675, 400));
        panProductID.setMinimumSize(new java.awt.Dimension(675, 400));
        panProductID.setPreferredSize(new java.awt.Dimension(675, 400));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblProductID, gridBagConstraints);

        lblDepositNo.setText("Deposit No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblDepositNo, gridBagConstraints);

        lblDebitAccHead.setText("Debit Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblDebitAccHead, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblRemarks, gridBagConstraints);

        cboProductID.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(cboProductID, gridBagConstraints);

        cboDepositSubNo.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDepositSubNo.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositSubNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDepositSubNoActionPerformed(evt);
            }
        });
        cboDepositSubNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboDepositSubNoFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(cboDepositSubNo, gridBagConstraints);

        lblDepositDate.setText("Deposit Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblDepositDate, gridBagConstraints);

        lblDepositAmount.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblDepositAmount, gridBagConstraints);

        lblInterestPayable.setText("Interest Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblInterestPayable, gridBagConstraints);

        lblTDSStartDate.setText("TDS Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblTDSStartDate, gridBagConstraints);

        lblTDSTillDt.setText("Name Of Depositholder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblTDSTillDt, gridBagConstraints);

        lblCollectionType.setText("Collection Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblCollectionType, gridBagConstraints);

        lblInterestPaid.setText("Interest Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblInterestPaid, gridBagConstraints);

        lblTDSEndDate.setText("TDS End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblTDSEndDate, gridBagConstraints);

        lblTDSInterestTillDt.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblTDSInterestTillDt, gridBagConstraints);

        lblTdsAmount.setText("TDS Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblTdsAmount, gridBagConstraints);

        lblDebitAccNum.setText("Debit Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblDebitAccNum, gridBagConstraints);

        txtRemarks.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(txtRemarks, gridBagConstraints);

        txtTdsAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTdsAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(txtTdsAmount, gridBagConstraints);

        txtDepositAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDepositAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepositAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(txtDepositAmount, gridBagConstraints);

        cLabel1.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(cLabel1, gridBagConstraints);

        lblDepositSubNo.setText("Deposit Sub. No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblDepositSubNo, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblMaturityDate, gridBagConstraints);

        lblInterestAccrued.setText("Interest Accrued");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblInterestAccrued, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(dateDepositDate, gridBagConstraints);

        dateTDSStartDate.setMinimumSize(new java.awt.Dimension(100, 21));
        dateTDSStartDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(dateTDSStartDate, gridBagConstraints);

        dateMaturityDate.setMinimumSize(new java.awt.Dimension(100, 21));
        dateMaturityDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(dateMaturityDate, gridBagConstraints);

        dateTDSEndDate.setMinimumSize(new java.awt.Dimension(100, 21));
        dateTDSEndDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(dateTDSEndDate, gridBagConstraints);

        txtDebitAccHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDebitAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(txtDebitAccHead, gridBagConstraints);

        cboCollectionType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCollectionType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCollectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCollectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(cboCollectionType, gridBagConstraints);

        panInterestPayable.setLayout(new java.awt.GridBagLayout());

        lblInterestPayablePercentage.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(lblInterestPayablePercentage, gridBagConstraints);

        txtInterestPayable.setMinimumSize(new java.awt.Dimension(80, 21));
        txtInterestPayable.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInterestPayable.add(txtInterestPayable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(panInterestPayable, gridBagConstraints);

        panInterestAccrued.setLayout(new java.awt.GridBagLayout());

        lblInterestAccruedPercentage.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestAccrued.add(lblInterestAccruedPercentage, gridBagConstraints);

        txtInterestAccrued.setMinimumSize(new java.awt.Dimension(80, 21));
        txtInterestAccrued.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInterestAccrued.add(txtInterestAccrued, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(panInterestAccrued, gridBagConstraints);

        panInterestPaid.setLayout(new java.awt.GridBagLayout());

        lblInterestPaidPercentage.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPaid.add(lblInterestPaidPercentage, gridBagConstraints);

        txtInterestPaid.setMinimumSize(new java.awt.Dimension(80, 21));
        txtInterestPaid.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInterestPaid.add(txtInterestPaid, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(panInterestPaid, gridBagConstraints);

        lblTDSTillDate.setMaximumSize(new java.awt.Dimension(150, 18));
        lblTDSTillDate.setMinimumSize(new java.awt.Dimension(150, 18));
        lblTDSTillDate.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(lblTDSTillDate, gridBagConstraints);

        lblAccountHead.setMaximumSize(new java.awt.Dimension(78, 18));
        lblAccountHead.setMinimumSize(new java.awt.Dimension(78, 18));
        lblAccountHead.setPreferredSize(new java.awt.Dimension(78, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(lblAccountHead, gridBagConstraints);

        lblTDSInterestTillDate.setMaximumSize(new java.awt.Dimension(120, 18));
        lblTDSInterestTillDate.setMinimumSize(new java.awt.Dimension(120, 18));
        lblTDSInterestTillDate.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(lblTDSInterestTillDate, gridBagConstraints);

        btnDebitAccHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitAccHead.setMinimumSize(new java.awt.Dimension(28, 21));
        btnDebitAccHead.setPreferredSize(new java.awt.Dimension(28, 21));
        btnDebitAccHead.setEnabled(false);
        btnDebitAccHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitAccHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 15);
        panProductID.add(btnDebitAccHead, gridBagConstraints);

        panDepositNo.setLayout(new java.awt.GridBagLayout());

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setMinimumSize(new java.awt.Dimension(28, 21));
        btnDepositNo.setPreferredSize(new java.awt.Dimension(28, 21));
        btnDepositNo.setEnabled(false);
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 15);
        panDepositNo.add(btnDepositNo, gridBagConstraints);

        txtDepositNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDepositNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositNoActionPerformed(evt);
            }
        });
        txtDepositNo.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtDepositNoCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panDepositNo.add(txtDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panProductID.add(panDepositNo, gridBagConstraints);

        panDebitAccNo.setLayout(new java.awt.GridBagLayout());

        txtDebitAccNum.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDebitAccNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panDebitAccNo.add(txtDebitAccNum, gridBagConstraints);

        btnDebitAccNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitAccNum.setMinimumSize(new java.awt.Dimension(28, 21));
        btnDebitAccNum.setPreferredSize(new java.awt.Dimension(28, 21));
        btnDebitAccNum.setEnabled(false);
        btnDebitAccNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitAccNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panDebitAccNo.add(btnDebitAccNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panProductID.add(panDebitAccNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID.add(lblTdsIdValue, gridBagConstraints);

        lblTdsId.setText("TDS Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panProductID.add(lblTdsId, gridBagConstraints);

        cTabbedPane1.addTab("TDS Deduction", panProductID);

        panProductID1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductID1.setMinimumSize(new java.awt.Dimension(650, 200));
        panProductID1.setPreferredSize(new java.awt.Dimension(750, 200));
        panProductID1.setLayout(new java.awt.GridBagLayout());

        panNewDepositdetails.setMaximumSize(new java.awt.Dimension(675, 250));
        panNewDepositdetails.setMinimumSize(new java.awt.Dimension(675, 250));
        panNewDepositdetails.setPreferredSize(new java.awt.Dimension(700, 250));
        panNewDepositdetails.setLayout(new java.awt.GridBagLayout());

        lblCust_id.setText("Cust ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblCust_id, gridBagConstraints);

        lblDepositAmount1.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblDepositAmount1, gridBagConstraints);

        lblInterestRateId.setText("Rate Of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblInterestRateId, gridBagConstraints);

        lblDeBehaviesLike.setText("Deposit Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblDeBehaviesLike, gridBagConstraints);

        lblInterestPaid1.setText("Int for Fin Yr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panNewDepositdetails.add(lblInterestPaid1, gridBagConstraints);

        lblTdsAmount1.setText("Tot Int for Fin Yr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panNewDepositdetails.add(lblTdsAmount1, gridBagConstraints);

        txTottIntForFin.setMaximumSize(new java.awt.Dimension(100, 21));
        txTottIntForFin.setMinimumSize(new java.awt.Dimension(100, 21));
        txTottIntForFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txTottIntForFinActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(txTottIntForFin, gridBagConstraints);

        txtDpAMt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDpAMt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(txtDpAMt, gridBagConstraints);

        lblInterestAccrued1.setText("Total Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panNewDepositdetails.add(lblInterestAccrued1, gridBagConstraints);

        cboDpBheaviesLike.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDpBheaviesLike.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDpBheaviesLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDpBheaviesLikeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(cboDpBheaviesLike, gridBagConstraints);

        panInterestPayable1.setLayout(new java.awt.GridBagLayout());

        txtInterestRate.setMinimumSize(new java.awt.Dimension(80, 21));
        txtInterestRate.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInterestPayable1.add(txtInterestRate, gridBagConstraints);

        lblInterestPayablePercentage1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable1.add(lblInterestPayablePercentage1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(panInterestPayable1, gridBagConstraints);

        lblCust_NameID.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblCust_NameID, gridBagConstraints);

        lblCust_Name.setMaximumSize(new java.awt.Dimension(200, 20));
        lblCust_Name.setMinimumSize(new java.awt.Dimension(200, 20));
        lblCust_Name.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblCust_Name, gridBagConstraints);

        lblTdsAmt.setText("TDS Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panNewDepositdetails.add(lblTdsAmt, gridBagConstraints);

        txtTdsAmountValue.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTdsAmountValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(txtTdsAmountValue, gridBagConstraints);

        panPeriodOfDeposit.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panNewDepositdetails.add(panPeriodOfDeposit, gridBagConstraints);

        lblPeriodOfDeposit.setText("Period of Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panNewDepositdetails.add(lblPeriodOfDeposit, gridBagConstraints);

        panCustomerId.setLayout(new java.awt.GridBagLayout());

        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCustomerId.add(txtCustId, gridBagConstraints);

        btnCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustId.setMinimumSize(new java.awt.Dimension(28, 28));
        btnCustId.setPreferredSize(new java.awt.Dimension(18, 18));
        btnCustId.setEnabled(false);
        btnCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerId.add(btnCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panNewDepositdetails.add(panCustomerId, gridBagConstraints);

        panPeriodOfDeposit1.setLayout(new java.awt.GridBagLayout());

        txtPeriodOfDeposit_Years.setMinimumSize(new java.awt.Dimension(20, 21));
        txtPeriodOfDeposit_Years.setPreferredSize(new java.awt.Dimension(20, 21));
        panPeriodOfDeposit1.add(txtPeriodOfDeposit_Years, new java.awt.GridBagConstraints());

        lblPeriod_Years.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panPeriodOfDeposit1.add(lblPeriod_Years, gridBagConstraints);

        txtPeriodOfDeposit_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtPeriodOfDeposit_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit1.add(txtPeriodOfDeposit_Months, gridBagConstraints);

        lblPeriod_Months.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit1.add(lblPeriod_Months, gridBagConstraints);

        txtPeriodOfDeposit_Days.setAllowAll(true);
        txtPeriodOfDeposit_Days.setMinimumSize(new java.awt.Dimension(45, 21));
        txtPeriodOfDeposit_Days.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit1.add(txtPeriodOfDeposit_Days, gridBagConstraints);

        lblPeriod_Days.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit1.add(lblPeriod_Days, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panNewDepositdetails.add(panPeriodOfDeposit1, gridBagConstraints);

        txtTotalInt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalInt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(txtTotalInt, gridBagConstraints);

        txtIntForFin.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntForFin.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(txtIntForFin, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(500, 35));
        panSearch.setPreferredSize(new java.awt.Dimension(500, 35));
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

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panNewDepositdetails.add(panSearch, gridBagConstraints);

        cboIntPayFreq.setMaximumSize(new java.awt.Dimension(100, 21));
        cboIntPayFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntPayFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntPayFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panNewDepositdetails.add(cboIntPayFreq, gridBagConstraints);

        lblIntPayFreq.setText("Interest Pay Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewDepositdetails.add(lblIntPayFreq, gridBagConstraints);

        lblDisCountYesNo.setText("DisCount");
        lblDisCountYesNo.setMaximumSize(new java.awt.Dimension(80, 18));
        lblDisCountYesNo.setMinimumSize(new java.awt.Dimension(80, 18));
        lblDisCountYesNo.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panNewDepositdetails.add(lblDisCountYesNo, gridBagConstraints);

        panDisCountYesNo.setLayout(new java.awt.GridBagLayout());

        rdoStandingInstruction_Yes.setText("Yes");
        rdoStandingInstruction_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoStandingInstruction_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoStandingInstruction_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoStandingInstruction_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStandingInstruction_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDisCountYesNo.add(rdoStandingInstruction_Yes, gridBagConstraints);

        rdoStandingInstruction_No.setText("No");
        rdoStandingInstruction_No.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoStandingInstruction_No.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoStandingInstruction_No.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoStandingInstruction_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStandingInstruction_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDisCountYesNo.add(rdoStandingInstruction_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panNewDepositdetails.add(panDisCountYesNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID1.add(panNewDepositdetails, gridBagConstraints);

        panInterestAccrued2.setMaximumSize(new java.awt.Dimension(675, 150));
        panInterestAccrued2.setMinimumSize(new java.awt.Dimension(675, 150));
        panInterestAccrued2.setPreferredSize(new java.awt.Dimension(675, 150));
        panInterestAccrued2.setLayout(new java.awt.GridBagLayout());

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestAccrued2.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panProductID1.add(panInterestAccrued2, gridBagConstraints);

        cTabbedPane1.addTab("TDS Calculator", panProductID1);

        panMain.add(cTabbedPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 4.0;
        getContentPane().add(panMain, gridBagConstraints);

        mnuProcess.setText("Process");
        mnuProcess.setToolTipText("Menu");

        mitNew.setText("New");
        mitNew.setToolTipText("");
        mitNew.setEnabled(false);
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptDelete.setEnabled(false);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancle");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptCancel.setEnabled(false);
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
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        ACTION=AUTH;
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        
        comboDepositSubNumberSetModel();
        btnReject.setEnabled(true);
        ClientUtil.enableDisable(panProductID, false);
        btnDebitAccNum.setEnabled(false);
        btnDepositNo.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        ACTION=AUTH;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        
        comboDepositSubNumberSetModel();
        btnAuthorize.setEnabled(true);
        ClientUtil.enableDisable(panProductID, false);
        btnDebitAccNum.setEnabled(false);
        btnDepositNo.setEnabled(false);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void rdoStandingInstruction_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStandingInstruction_NoActionPerformed
        // TODO add your handling code here:
        rdoStandingInstruction_Yes.setSelected(false);
    }//GEN-LAST:event_rdoStandingInstruction_NoActionPerformed
    
    private void rdoStandingInstruction_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStandingInstruction_YesActionPerformed
        // TODO add your handling code here:
        rdoStandingInstruction_No.setSelected(false);
    }//GEN-LAST:event_rdoStandingInstruction_YesActionPerformed
    
    private void cboIntPayFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntPayFreqActionPerformed
        // TODO add your handling code here:
        intPayFreq = ((ComboBoxModel)cboIntPayFreq.getModel()).getKeyForSelected().toString();
        if(intPayFreq.equals("30")){
            rdoStandingInstruction_No.setSelected(false);
            rdoStandingInstruction_Yes.setSelected(false);
            lblDisCountYesNo.setVisible(true);
            panDisCountYesNo.setVisible(true);
            
        }else{
            rdoStandingInstruction_No.setSelected(false);
            rdoStandingInstruction_Yes.setSelected(false);
            lblDisCountYesNo.setVisible(false);
            panDisCountYesNo.setVisible(false);
        }
    }//GEN-LAST:event_cboIntPayFreqActionPerformed
    
    private void cboDpBheaviesLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDpBheaviesLikeActionPerformed
        // TODO add your handling code here:
        
        
        behaviesLike = ((ComboBoxModel)cboDpBheaviesLike.getModel()).getKeyForSelected().toString();
        if(behaviesLike.length()>0 && behaviesLike.equals("FIXED")){
            rdoStandingInstruction_No.setSelected(false);
            rdoStandingInstruction_Yes.setSelected(false);
            cboIntPayFreq.setVisible(true);
            panDisCountYesNo.setVisible(false);
            lblIntPayFreq.setVisible(true);
            lblDisCountYesNo.setVisible(false);
            cboIntPayFreq.setModel(observable.getCbmIntPayFreq());
            
        } else{
            cboIntPayFreq.setVisible(false);
            panDisCountYesNo.setVisible(false);
            lblIntPayFreq.setVisible(false);
            lblDisCountYesNo.setVisible(false);
            rdoStandingInstruction_No.setSelected(false);
            rdoStandingInstruction_Yes.setSelected(false);
            
        }
        
    }//GEN-LAST:event_cboDpBheaviesLikeActionPerformed
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        long period=0;
        
        String   behaviesLike = ((ComboBoxModel)cboDpBheaviesLike.getModel()).getKeyForSelected().toString();
        observable.setCboDpBehiveLike((String)cboDpBheaviesLike.getSelectedItem());
        String CustId=CommonUtil.convertObjToStr(txtCustId.getText());
        double dpAmt=CommonUtil.convertObjToDouble(txtDpAMt.getText()).doubleValue();
        double roi=CommonUtil.convertObjToDouble(txtInterestRate.getText()).doubleValue();
        double dd=CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Days.getText()).doubleValue();
        double mm=CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Months.getText()).doubleValue();
        double yy=CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Years.getText()).doubleValue();
        if(  CustId.length()<=0 && CustId.equals("") ){
            ClientUtil.displayAlert("Please Select The Customer Id");
        } else   if(  behaviesLike.length()<=0 && behaviesLike.equals("") ){
            ClientUtil.displayAlert("Please Select Deposit Type");
        } else   if(  dpAmt<=0 ){
            ClientUtil.displayAlert("Please Enter The Deposit Amount");
        } else   if(  roi<=0 ){
            ClientUtil.displayAlert("Please Enter The Rate Of Interest");
        }
        else   if(  dd<=0 &&  mm<=0 &&  yy<=0 ){
            ClientUtil.displayAlert("Please Enter The Period");
        }
        
        HashMap detailsHash = new HashMap();
        detailsHash.put("AMOUNT", txtDpAMt.getText());
        detailsHash.put("DEPOSIT_DT",currDt.clone());
        detailsHash.put("DEPOSIT_DT",currDt.clone());
        detailsHash.put("LST_PROV_DT",currDt.clone());
        detailsHash.put("LAST_INT_APPL_DT",currDt.clone());
        detailsHash.put("MATURITY_DT",DateUtil.getDateMMDDYYYY(calculateMatDate()));
        detailsHash.put("CLOSE_DT",DateUtil.getDateMMDDYYYY(calculateMatDate()));
        detailsHash.put("RATE_OF_INT",new Double(roi));
        detailsHash.put("DEPOSIT_AMT",new Double(dpAmt));
        detailsHash.put("OTAL_INT_CREDIT",new Double(0));
        detailsHash.put("TDS_AMT",new Double(0));
        
        detailsHash.put("BEHAVES_LIKE",behaviesLike);
        detailsHash.put("ACCT_STATUS","NEW");
        detailsHash.put("START1", currDt.clone());
        detailsHash.put("END1", DateUtil.getDateMMDDYYYY(calculateMatDate()));
        detailsHash.put("LSTPROVDT", currDt.clone());
        detailsHash.put("OTHERDAO", "OTHERDAO");
        if(behaviesLike.equals("FIXED")){
            intPayFreq = ((ComboBoxModel)cboIntPayFreq.getModel()).getKeyForSelected().toString();
            detailsHash.put("INTPAY_FREQ",new Double(intPayFreq));
            if(rdoStandingInstruction_Yes.isSelected() == false && rdoStandingInstruction_No.isSelected() == false &&
            !intPayFreq.equals("") && intPayFreq.equals("30")){
                ClientUtil.displayAlert("discounted Rate choose...");
                return;
            }
            if(intPayFreq.equals("")){
                ClientUtil.displayAlert("Interest Payment mode choose...");
                return;
            }
            if(rdoStandingInstruction_Yes.isSelected() == true){
                detailsHash.put("DISCOUNTED_RATE","Y");
            }
            else if(rdoStandingInstruction_No.isSelected() == true){
                detailsHash.put("DISCOUNTED_RATE","N");
            }
        }
        
        
        
        List lst=ClientUtil.executeQuery("getLastFinYear",detailsHash);
        if (lst!=null && lst.size()>0){
            HashMap finMap=new HashMap();
            finMap=(HashMap)lst.get(0);
            Date lstFinyear=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finMap.get("LAST_FINANCIAL_YEAR_END")));
            lstFinyear=DateUtil.addDays(lstFinyear,360);
            period =DateUtil.dateDiff((Date) currDt.clone(), lstFinyear);
            detailsHash.put("PEROID", new Double(period));
            //            amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            System.out.println("$$$$$detailshash :"+detailsHash);
            
            //            double finIntAmt=CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
            
            //            finIntAmt = (double)rod.getNearest((long)(finIntAmt *100),100)/100;
            //            txtIntForFin.setText(CommonUtil.convertObjToStr(new Double(finIntAmt)));
            detailsHash.put("CUSTID",CommonUtil.convertObjToStr(txtCustId.getText()));
            detailsHash.put("ALLTDSACCOUNT","ALLTDSACCOUNT");
            detailsHash.put("LASTFIN", lstFinyear);
            detailsHash.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            if(custType.equals("Individual"))
                custType="INDIVIDUAL";
            else
                custType="CORPORATE";
            
            detailsHash.put("CUST_TYPE", custType);
            detailsHash.put("INT_DATE", currDt.clone());
            
            
            
            observable.populateData(detailsHash);
            
            double allFinInt= totalFinIntAmt()+CommonUtil.convertObjToDouble(observable.getFinIntAmt()).doubleValue();
            txTottIntForFin.setText(CommonUtil.convertObjToStr(new Double(allFinInt)));
            double cutAmt=CommonUtil.convertObjToDouble(observable.getCutAmt()).doubleValue();
            double tdsRate =CommonUtil.convertObjToDouble(observable.getTdsRate()).doubleValue();
            double tdsAmt=0.0;
            if(allFinInt>=cutAmt){
                tdsAmt=(allFinInt*tdsRate)/100;
            }else{
                tdsAmt=0.0;
            }
            Rounding rod =new Rounding();
            
            tdsAmt = (double)rod.getNearest((long)(tdsAmt *100),100)/100;
            txtTdsAmountValue.setText(CommonUtil.convertObjToStr(new Double(tdsAmt)));
            txtTdsAmountValue.setEnabled(false);
            txTottIntForFin.setEnabled(false);
            txtTotalInt.setEnabled(false);
            txtIntForFin.setEnabled(false);
        }else{
            ClientUtil.displayAlert("Last Financial year Not Their in parameter");
            return;
        }
        
        
        
        
    }//GEN-LAST:event_btnOkActionPerformed
    
    public double totalFinIntAmt(){
        double totFinIntAmt=0;
        if(tblData.getRowCount()>0){
            for (int i=0;i<tblData.getRowCount();i++){
                totFinIntAmt=totFinIntAmt+CommonUtil.convertObjToDouble(tblData.getValueAt(i,5)).doubleValue();
                
                
            }
        }
        return totFinIntAmt;
    }
    
    
    
    private String  calculateMatDate(){
        String maturityDate = null;
        java.util.Date depDate = (java.util.Date)currDt.clone();
        System.out.println("####calculateMatDate : "+depDate);
        if(depDate !=null){
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear()+1900),depDate.getMonth(),depDate.getDate());
            if((txtPeriodOfDeposit_Years.getText() != null) && (!txtPeriodOfDeposit_Years.getText().equals(""))){
                cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtPeriodOfDeposit_Years.getText()));
            }else{
                txtPeriodOfDeposit_Years.setText(String.valueOf(0));
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if((txtPeriodOfDeposit_Months.getText() != null) && (!txtPeriodOfDeposit_Months.getText().equals(""))){
                cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtPeriodOfDeposit_Months.getText()));
            }else{
                txtPeriodOfDeposit_Months.setText(String.valueOf(0));
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if((txtPeriodOfDeposit_Days.getText() != null) && (!txtPeriodOfDeposit_Days.getText().equals(""))){
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtPeriodOfDeposit_Days.getText()));
            }else{
                txtPeriodOfDeposit_Days.setText(String.valueOf(0));
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            maturityDate = (DateUtil.getStringDate(cal.getTime()));
        }
        return maturityDate;
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearTdsCalculateFields();
        observable.resetForm();
    }//GEN-LAST:event_btnClearActionPerformed
    
    private void txTottIntForFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txTottIntForFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txTottIntForFinActionPerformed
    
    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
        popUpItems(CUST_ID);
    }//GEN-LAST:event_btnCustIdActionPerformed
    
    private void cboDepositSubNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboDepositSubNoFocusGained
        // TODO add your handling code here:
        observable.setTxtDebitAccNum("");
        lblTDSInterestTillDate.setText("");
        txtDebitAccNum.setText("");
    }//GEN-LAST:event_cboDepositSubNoFocusGained
    
    private void cboDepositSubNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDepositSubNoActionPerformed
        // TODO add your handling code here:
        
        //           observable.setTxtDebitAccNum("");
        //           lblTDSInterestTillDate.setText("");
        //            txtDebitAccNum.setText("");
    }//GEN-LAST:event_cboDepositSubNoActionPerformed
    
    private void txtDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepositNoActionPerformed
    
    private void cboCollectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCollectionTypeActionPerformed
        // TODO add your handling code here:
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        
        
        observable.setCbmDepositSubNo(new ComboBoxModel(key,value));
        cboDepositSubNo.setModel(observable.getCbmDepositSubNo());
        
        prodType = ((ComboBoxModel)cboCollectionType.getModel()).getKeyForSelected().toString();
        if(prodType!=null && prodType.length() > 0){
            
            //             if( (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)
            //             && prodType.e )
            cboCollectionType.setEnabled(true);
            observable.callProdIdsForProductType(prodType);
            cboDepositSubNo.setModel(observable.getCbmDepositSubNo());
            
            
        }else{
            //            ClientUtil.displayAlert("Select Operative Or Advance");
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            observable.setCbmDepositSubNo(new ComboBoxModel(key,value));
            observable.setTxtDebitAccNum("");
            lblTDSInterestTillDate.setText("");
            txtDebitAccNum.setText("");
            
        }
        
        
    }//GEN-LAST:event_cboCollectionTypeActionPerformed
    
    private void txtDepositAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepositAmountActionPerformed
    
    private void btnDebitAccNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitAccNumActionPerformed
        // Add your handling code here:
        observable.setCboDepositSubNo((String) cboDepositSubNo.getSelectedItem());
        
        popUpItems(DEBITACCOUNTNUMBER);
        //        comboDepositSubNumberSetModel();
    }//GEN-LAST:event_btnDebitAccNumActionPerformed
    
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:);
        lblAccountHead.setText("");
        txtDepositNo.setText("");
        try{
            final HashMap prdouctIDdMap = new HashMap();
            HashMap hash = new HashMap();
            if(cboProductID.getSelectedItem().toString().length() > 0){
                prdouctIDdMap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected().toString());
                final List resultList = ClientUtil.executeQuery("Deposit_TDS.getProductID", prdouctIDdMap);
                final HashMap resultMap = (HashMap)resultList.get(0);
                lblAccountHead.setText(resultMap.get("ACCT_HEAD").toString());
                observable.setCboProductID(cboProductID.getSelectedItem().toString());
            }
            //            cboDepositSubNo.setModel(new ComboBoxModel());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    private void txtDepositNoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtDepositNoCaretUpdate
        // Add your handling code here:
        depositNumber = observable.getTxtDepositNo();
        observable.populateComboDepositSubNumber(depositNumber);
        cboDepositSubNo.setSelectedItem(observable.getCboDepositSubNo());
    }//GEN-LAST:event_txtDepositNoCaretUpdate
    
    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        // Add your handling code here:
        popUpItems(DEPOSITNUMBER);
        //        cboDepositSubNo.setModel(observable.getCbmDepositSubNo());
    }//GEN-LAST:event_btnDepositNoActionPerformed
    
    private void btnDebitAccHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitAccHeadActionPerformed
        // Add your handling code here:
        observable.setCboDepositSubNo((String) cboDepositSubNo.getSelectedItem());
        popUpItems(DEBITACCOUNTHEAD);
        comboDepositSubNumberSetModel();
    }//GEN-LAST:event_btnDebitAccHeadActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUpItems(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        observable.resetForm();
        setButtonEnableDisable();
        setHelpButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        clearTdsCalculateFields();
        
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void clearTdsCalculateFields(){
        txtCustId.setText("");
        txtDpAMt.setText("");
        txtIntForFin.setText("");
        txtTdsAmountValue.setText("");
        txtTotalInt.setText("");
        txTottIntForFin.setText("");
        txtPeriodOfDeposit_Days.setText("");
        txtPeriodOfDeposit_Months.setText("");
        txtPeriodOfDeposit_Years.setText("");
        txtInterestRate.setText("");
        observable.resetTable();
        txtTotalInt.setText("");
        txtIntForFin.setText("");
        txTottIntForFin.setText("");
        txtTdsAmountValue.setText("");
        lblTDSTillDate.setText("");
    }
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductID);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
        
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUpItems(DELETE);
        comboDepositSubNumberSetModel();
        ClientUtil.enableDisable(this, false);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        popUpItems(EDIT);
        comboDepositSubNumberSetModel();
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
        setHelpButtonEnableDisable();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        ClientUtil.enableDisable( panProductID,false);
        
        btnDepositNo.setEnabled(true);
        btnDebitAccNum.setEnabled(true);
        cboProductID.setEnabled(true);
        cboCollectionType.setEnabled(true);
        cboDepositSubNo.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    /**
     * @param args the command line arguments
     *
     * public static void main(String args[]) {
     * new TdsDeductionUI().show();
     * }
     **/
    
    /** This method helps in popoualting the data from the data base
     * @param Action argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        updateOBFields();
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        ACTION=Action;
        if ( Action == EDIT || Action == DELETE || Action == VIEW ||Action==AUTH ||Action==REJECT){
            viewMap.put(CommonConstants.MAP_NAME, "Deposit_TDS.ViewAllDepositTDSTO");
        }
        if(Action == EDIT){
            whereMap.put("STATUS",CommonConstants.STATUS_MODIFIED);
        }
        // else if (Action==ClientConstants.ACTIONTYPE_AUTHORIZE){
        //            viewMap.put(CommonConstants.MAP_NAME, "getTdsDeductionAuthorizeList");
        //            HashMap hash = new HashMap();
        //            hash.put("USER_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboDepositSubNo.getModel()).getKeyForSelected()));
        //
        //
        //            viewMap.put(CommonConstants.MAP_WHERE, hash);
        //
        //        }
        
        else if(Action == DEBITACCOUNTHEAD){
            viewMap.put(CommonConstants.MAP_NAME, "Deposit_TDS.getSelectAccoutHead");
        }else if(Action == DEBITACCOUNTNUMBER){
            if(!prodType.equals("") && !prodType.equals("")){
                
                viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountList"+prodType);
                HashMap hash = new HashMap();
                hash.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboDepositSubNo.getModel()).getKeyForSelected()));
                hash.put("SELECTED_BRANCH",getSelectedBranchID());
                
                viewMap.put(CommonConstants.MAP_WHERE, hash);
                
            }
            
        }else if(Action == DEPOSITNUMBER){
            whereMap.put("PROD_ID", cboProductID.getSelectedItem().toString());
            viewMap.put(CommonConstants.MAP_NAME, "Deposit_TDS.ViewAllDepositNo");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        else if(Action == CUST_ID){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccInfoTOList");
            whereMap.put("SELECTED_BRANCH_ID",  getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == ClientConstants.VIEW_TYPE_AUTHORIZE && isFilled){
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authDataMap.put("TDS_ID", CommonUtil.convertObjToStr(lblTdsIdValue.getText()));
            authDataMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(txtDepositNo.getText()));
            if(observable.getModified().length()>0 && observable.getModified().equals("1")){
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
                    ClientUtil.execute("authorizeTdsDeduction", authDataMap);
                    ClientUtil.execute("authorizeTdsDeductionTempDeletion", authDataMap);
                }
                else{
                    
                    observable.setRejMap(authDataMap);
                    observable.doAction();
                    
                    
                    
                }
                
            }
            else {
                ClientUtil.execute("authorizeTdsDeduction", authDataMap);
            }
            viewType = "";
            btnCancelActionPerformed(null);
            //            ClientUtil.
            //            viewType = ClientConstants.VIEW_TYPE_CANCEL;
            ACTION=-1;
            isFilled = false;
            lblStatus.setText(authorizeStatus);
            
        } else {
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getTdsDeductionAuthorizeList");
            
            
            
            isFilled = true;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            btnSave.setEnabled(false);
            
            //            setAuthBtnEnableDisable();
            //            btnDelete.setEnabled(false);
        }
    }
    
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        ClientUtil.enableDisable(this, true);
        if(ACTION == EDIT || ACTION == DELETE || ACTION == VIEW || ACTION == AUTH || ACTION == REJECT){
            setButtonEnableDisable();
            actionEditDelete(hash);
            
            
        }else if(ACTION == DEBITACCOUNTHEAD){
            observable.setTxtDebitAccHead((String)hash.get("ACCOUNT HEAD ID"));
        }else if(ACTION == DEBITACCOUNTNUMBER){
            if(prodType.equals("TD")){
                hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
            }
            observable.setTxtDebitAccNum((String)hash.get("ACCOUNTNO"));
            lblTDSInterestTillDate.setText((String)hash.get("CUSTOMERNAME"));
            
        }else if(ACTION == DEPOSITNUMBER){
            observable.setTxtDepositNo((String)hash.get("DEPOSIT NUMBER"));
            observable.setTxtDepositAmount(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT")));
            //            observable.setTxtDepositAmount((String)hash.get("DEPOSIT_AMT"));
            lblTDSTillDate.setText((String)hash.get("CUSTOMER NAME"));
            observable.setDateMaturityDate(CommonUtil.convertObjToStr(hash.get("MATURITY_DT")));
            observable.setDateDepositDate(CommonUtil.convertObjToStr(hash.get("DEPOSIT_DT")));
            HashMap existMap=new HashMap();
            existMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(hash.get("DEPOSIT NUMBER")));
            List existsLst = ClientUtil.executeQuery("getDepositNoExists",existMap );
            if(existsLst!=null && existsLst.size()>0){
                ClientUtil.displayAlert("Allready This No Exists");
                observable.resetForm();
                clearTdsCalculateFields();
                btnDepositNo.setEnabled(true);
                btnDebitAccNum.setEnabled(true);
                ClientUtil.enableDisable( panProductID,false);
                //                return;
            }
            
            
        }
        
        if (ACTION == EDIT ){
            setHelpButtonEnableDisable();
        }
        if(ACTION == VIEW) {
            ClientUtil.enableDisable(panProductID,false);
        }
        if(ACTION==CUST_ID){
            txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            lblCust_Name.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            custType=CommonUtil.convertObjToStr(hash.get("CUST_TYPE"));
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        ClientUtil.enableDisable( panProductID,false);
        
        
        btnDepositNo.setEnabled(true);
        btnDebitAccNum.setEnabled(true);
        cboProductID.setEnabled(true);
        cboCollectionType.setEnabled(true);
        cboDepositSubNo.setEnabled(true);
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW ){
            HashMap queryWhereMap= new HashMap();
            queryWhereMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtDepositNo.getText()));
            List custListData = ClientUtil.executeQuery("getTdsDeductionName",queryWhereMap );
            if(custListData!=null && custListData.size()>0){
                queryWhereMap= new HashMap();
                queryWhereMap=(HashMap)custListData.get(0);
                lblTDSTillDate.setText(CommonUtil.convertObjToStr(queryWhereMap.get("NAME")));
            }
            queryWhereMap= new HashMap();
            queryWhereMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtDebitAccNum.getText()));
            custListData = ClientUtil.executeQuery("getTdsDeductionName",queryWhereMap );
            if(custListData!=null && custListData.size()>0){
                queryWhereMap= new HashMap();
                queryWhereMap=(HashMap)custListData.get(0);
                lblTDSInterestTillDate.setText(CommonUtil.convertObjToStr(queryWhereMap.get("NAME")));
            }
        }
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ){
            btnDepositNo.setEnabled(false);
            cboProductID.setEnabled(false);
        }
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ){
            ClientUtil.enableDisable(panProductID,false);
            btnDepositNo.setEnabled(false);
            btnDebitAccNum.setEnabled(false);;
        }
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
            
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            isFilled=true;
            
        }
    }
    
    /** This method displays the alert message if any of the text fields are empty */
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            ClientUtil.enableDisable(this, false);
            observable.setResultStatus();
            observable.resetForm();
            setButtonEnableDisable();
            setHelpButtonEnableDisable();
            ClientUtil.enableDisable(this, false);
        }
    }
    
    /** Executes query when EDIT or DELETE is called according to the TDS DEDUCTION ID selected */
    private void actionEditDelete(HashMap hash){
        observable.setStatus();
        cboDepositSubNo.setModel(new ComboBoxModel());
        hash.put(CommonConstants.MAP_WHERE, hash.get("TDS DEDUCTION ID"));
        observable.setTdsId((String) hash.get("TDS DEDUCTION ID"));
        observable.populateData(hash);
    }
    
    /** sets combo Deposit Sub Number model */
    private void comboDepositSubNumberSetModel(){
        cboDepositSubNo.setModel(observable.getCbmDepositSubNo());
        cboDepositSubNo.setSelectedItem(observable.getCboDepositSubNo());
        
    }
    
    
    /** This method performs enable and the disable of the necessary buttons */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        
        
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void setHelpButtonEnableDisable(){
        btnDepositNo.setEnabled(!btnNew.isEnabled());
        btnDebitAccHead.setEnabled(!btnNew.isEnabled());
        btnDebitAccNum.setEnabled(!btnNew.isEnabled());
        txtDepositNo.setEnabled(!btnDepositNo.isEnabled());
        txtDepositNo.setEditable(!btnDepositNo.isEnabled());
        txtDebitAccHead.setEnabled(!btnDebitAccHead.isEnabled());
        txtDebitAccHead.setEditable(!btnDebitAccHead.isEnabled());
        txtDebitAccNum.setEnabled(!btnDebitAccNum.isEnabled());
        txtDebitAccNum.setEditable(!btnDebitAccNum.isEnabled());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustId;
    private com.see.truetransact.uicomponent.CButton btnDebitAccHead;
    private com.see.truetransact.uicomponent.CButton btnDebitAccNum;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboCollectionType;
    private com.see.truetransact.uicomponent.CComboBox cboDepositSubNo;
    private com.see.truetransact.uicomponent.CComboBox cboDpBheaviesLike;
    private com.see.truetransact.uicomponent.CComboBox cboIntPayFreq;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CDateField dateDepositDate;
    private com.see.truetransact.uicomponent.CDateField dateMaturityDate;
    private com.see.truetransact.uicomponent.CDateField dateTDSEndDate;
    private com.see.truetransact.uicomponent.CDateField dateTDSStartDate;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblCollectionType;
    private com.see.truetransact.uicomponent.CLabel lblCust_Name;
    private com.see.truetransact.uicomponent.CLabel lblCust_NameID;
    private com.see.truetransact.uicomponent.CLabel lblCust_id;
    private com.see.truetransact.uicomponent.CLabel lblDeBehaviesLike;
    private com.see.truetransact.uicomponent.CLabel lblDebitAccHead;
    private com.see.truetransact.uicomponent.CLabel lblDebitAccNum;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmount;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmount1;
    private com.see.truetransact.uicomponent.CLabel lblDepositDate;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblDepositSubNo;
    private com.see.truetransact.uicomponent.CLabel lblDisCountYesNo;
    private com.see.truetransact.uicomponent.CLabel lblIntPayFreq;
    private com.see.truetransact.uicomponent.CLabel lblInterestAccrued;
    private com.see.truetransact.uicomponent.CLabel lblInterestAccrued1;
    private com.see.truetransact.uicomponent.CLabel lblInterestAccruedPercentage;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaid;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaid1;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaidPercentage;
    private com.see.truetransact.uicomponent.CLabel lblInterestPayable;
    private com.see.truetransact.uicomponent.CLabel lblInterestPayablePercentage;
    private com.see.truetransact.uicomponent.CLabel lblInterestPayablePercentage1;
    private com.see.truetransact.uicomponent.CLabel lblInterestRateId;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPeriodOfDeposit;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Days;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Months;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Years;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTDSEndDate;
    private com.see.truetransact.uicomponent.CLabel lblTDSInterestTillDate;
    private com.see.truetransact.uicomponent.CLabel lblTDSInterestTillDt;
    private com.see.truetransact.uicomponent.CLabel lblTDSStartDate;
    private com.see.truetransact.uicomponent.CLabel lblTDSTillDate;
    private com.see.truetransact.uicomponent.CLabel lblTDSTillDt;
    private com.see.truetransact.uicomponent.CLabel lblTdsAmount;
    private com.see.truetransact.uicomponent.CLabel lblTdsAmount1;
    private com.see.truetransact.uicomponent.CLabel lblTdsAmt;
    private com.see.truetransact.uicomponent.CLabel lblTdsId;
    private com.see.truetransact.uicomponent.CLabel lblTdsIdValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCustomerId;
    private com.see.truetransact.uicomponent.CPanel panDebitAccNo;
    private com.see.truetransact.uicomponent.CPanel panDepositNo;
    private com.see.truetransact.uicomponent.CPanel panDisCountYesNo;
    private com.see.truetransact.uicomponent.CPanel panInterestAccrued;
    private com.see.truetransact.uicomponent.CPanel panInterestAccrued2;
    private com.see.truetransact.uicomponent.CPanel panInterestPaid;
    private com.see.truetransact.uicomponent.CPanel panInterestPayable;
    private com.see.truetransact.uicomponent.CPanel panInterestPayable1;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panNewDepositdetails;
    private com.see.truetransact.uicomponent.CPanel panPeriodOfDeposit;
    private com.see.truetransact.uicomponent.CPanel panPeriodOfDeposit1;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductID1;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoStandingInstruction_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStandingInstruction_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txTottIntForFin;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtDebitAccHead;
    private com.see.truetransact.uicomponent.CTextField txtDebitAccNum;
    private com.see.truetransact.uicomponent.CTextField txtDepositAmount;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtDpAMt;
    private com.see.truetransact.uicomponent.CTextField txtIntForFin;
    private com.see.truetransact.uicomponent.CTextField txtInterestAccrued;
    private com.see.truetransact.uicomponent.CTextField txtInterestPaid;
    private com.see.truetransact.uicomponent.CTextField txtInterestPayable;
    private com.see.truetransact.uicomponent.CTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Days;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Months;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Years;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTdsAmount;
    private com.see.truetransact.uicomponent.CTextField txtTdsAmountValue;
    private com.see.truetransact.uicomponent.CTextField txtTotalInt;
    // End of variables declaration//GEN-END:variables
}