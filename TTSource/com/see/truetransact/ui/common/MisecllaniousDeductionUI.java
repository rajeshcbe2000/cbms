/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MisecllaniousDeductionUI.java
 *
 * Created on May 25, 2004, 5:18 PM
 */

package com.see.truetransact.ui.common;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;

/**
 *
 * @author  Sathiya
 */

public class MisecllaniousDeductionUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    private MisecllaniousDeductionOB observable;
    //    private SalaryStructureOB resourceBundle;
    private MisecllaniousDeductionMRB objMandatoryRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.MisecllaniousDeductionRB", ProxyParameters.LANGUAGE);
    private Date curDate = null;
    private int viewType=-1;
    private boolean _intSalaryNew = false;
    private boolean _intDANew = false;
    private boolean _intHANew = false;
    private boolean _intMDNew = false;
    private boolean _intGNew = false;
    //    private boolean _intHRANew = false;
    private boolean _intTANew = false;
    private boolean _intMANew = false;
    private boolean isFilled = false;
    private boolean salaryStructureSave = false;
    private boolean DASave = false;
    private boolean CCASave = false;
    private boolean HRASave = false;
    private boolean TASave = false;
    private boolean MASave = false;
    private boolean OASave = false;
    private boolean selectedSingleRow = false;
    private String fromDateAlert = "From date should not be empty";
    private String gradeAlert = "Grade should not be empty";
    private String designationAlert = "Designation should not be empty";
    int rowSelected = -1;
    int pan = -1;
    int panEditDelete = -1;
    private String mdFixedOrPercentage = "";
    private int MSCL_DEDUCTION = 2,HALTING_ALLOWANCE = 1,GRATUITY = 3;
    /** Creates new form BeanForm */
    public MisecllaniousDeductionUI() {
        initComponents();
        initSetUp();
    }
    private void initSetUp(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLength();
        allScreensDisable(false);
        ClientUtil.enableDisable(panMisecllaniousDeductionInfo,false);
        enableDisableAllscreens(false);
        chkOAllowanceFixed(false);
        chkOAllowancePercentage(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        initComponentData();
        tabMisecllaniousDeductions.resetVisits();
        btnDelete.setEnabled(true);
        cboGratuityCityType.setVisible(false);
        lblHaltingllowanceType.setVisible(false);
        cboHaltingAllowanceTypeValue.setVisible(false);
        panHaltingDiem.setVisible(false);
        txtHaltingMaximumOfValue.setVisible(false);
        txtHaltingPercentageValue.setVisible(false);
        lblMdFixedAmt.setVisible(false);
        txtMdFixedAmtValue.setVisible(false);
        lblHaltingMaximumOf.setVisible(false);
        lblHaltingPercentage.setVisible(false);
        lblHaltingFixedAmt.setVisible(true);
        txtHaltingFixedAmtValue.setVisible(true);
        curDate = ClientUtil.getCurrentDate();
        rdoUsingBasic_OthersActionPerformed(null);
        chkMdFixedValueActionPerformed(null);
        chkMdFixedValue.setSelected(false);
        ClientUtil.enableDisable(panMdType,true);
        ClientUtil.enableDisable(panMisecllaniousDeductionInfo,false);
        txtMdFixedAmtValue.setVisible(false);
        lblMdFixedAmt.setVisible(false);
        //        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLienInfo);
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
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
        lblHaltingSLNO.setName("lblHaltingSLNO");
        lblHaltingSLNOValue.setName("lblHaltingSLNOValue");
        lblHaltingDesignation.setName("lblHaltingDesignation");
        cboHaltingDesignationValue.setName("cboHaltingDesignationValue");
        lblHaltingFromDate.setName("lblHaltingFromDate");
        tdtHaltingFromDateValue.setName("tdtHaltingFromDateValue");
        lblHaltingToDate.setName("lblHaltingToDate");
        tdtHaltingToDateValue.setName("tdtHaltingToDateValue");
        lblHaltingllowanceType.setName("lblHaltingllowanceType");
        cboHaltingAllowanceTypeValue.setName("cboHaltingAllowanceTypeValue");
        lblHaltingParameterBasedon.setName("lblHaltingParameterBasedon");
        cboHaltingParameterBasedOnValue.setName("cboHaltingParameterBasedOnValue");
        lblHaltingSubParameter.setName("lblHaltingSubParameter");
        cboHaltingSubParameterValue.setName("cboHaltingSubParameterValue");
        lblHaltingFixedAmt.setName("lblHaltingFixedAmt");
        txtHaltingFixedAmtValue.setName("txtHaltingFixedAmtValue");
        lblHaltingPercentage.setName("lblHaltingPercentage");
        txtHaltingMaximumOfValue.setName("txtHaltingMaximumOfValue");
        lblHaltingMaximumOf.setName("lblHaltingMaximumOf");
        txtHaltingPercentageValue.setName("txtHaltingPercentageValue");
        btnHaltingNew.setName("btnHaltingNew");
        btnHaltingSave.setName("btnHaltingSave");
        btnHaltingDelete.setName("btnHaltingDelete");
        
        lblMDSLNO.setName("lblMDSLNO");
        lblMDSLNOValue.setName("lblMDSLNOValue");
        lblMDDeductionType.setName("lblMDDeductionType");
        cboMDDeductionType.setName("cboMDDeductionType");
        lblMisecllaniousDeduction.setName("lblMisecllaniousDeduction");
        cboMisecllaniousDeduction.setName("cboMisecllaniousDeduction");
        lblMDFromDate.setName("lblMDFromDate");
        tdtMDFromDateValue.setName("tdtMDFromDateValue");
        lblMDToDate.setName("lblMDToDate");
        tdtMDToDateValue.setName("tdtMDToDateValue");
        lblMDDeductionType.setName("lblMDDeductionType");
        lblMDMaximumOf.setName("lblMDMaximumOf");
        txtMDMaximumAmtValue.setName("txtMDMaximumAmtValue");
        txtMdFixedAmtValue.setName("txtMdFixedAmtValue");
        txtFromAmount.setName("txtFromAmount");
        txtToAmount.setName("txtToAmount");
        lblMDPercentage.setName("lblMDPercentage");
        txtMDPercentageValue.setName("txtMDPercentageValue");
        lblMDEligibleAllowances.setName("lblMDEligibleAllowances");
        cboMDEligibleAllowances.setName("cboMDEligibleAllowances");
        lblMDEligiblePercentage.setName("lblMDEligiblePercentage");
        txtMDEligiblePercentageValue.setName("txtMDEligiblePercentageValue");
        btnMDNew.setName("btnMDNew");
        btnMDSave.setName("btnMDSave");
        btnMDDelete.setName("btnMDDelete");
        
        lblGratuitySLNO.setName("lblGratuitySLNO");
        lblGratuitySLNOValue.setName("lblGratuitySLNOValue");
        lblGratuityDesignation.setName("lblGratuityDesignation");
        cboGratuityDesignation.setName("cboGratuityDesignation");
        lblGratuityCityType.setName("lblGratuityCityType");
        //        cboGratuityCityType.setName("cboGratuityCityType");
        lblGratuityFromDate.setName("lblGratuityFromDate");
        tdtGratuityFromDateValue.setName("tdtGratuityFromDateValue");
        lblGratuityToDate.setName("lblGratuityToDate");
        tdtGratuityToDateValue.setName("tdtGratuityToDateValue");
        lblGratuityUpto.setName("lblGratuityUpto");
        txtGratuityUptoValue.setName("txtGratuityUptoValue");
        lblGratuityYearofService.setName("lblGratuityYearofService");
        txtGratuityUptoServiceValue.setName("txtGratuityUptoServiceValue");
        lblGratuityMonthPay.setName("lblGratuityMonthPay");
        lblGratuityMaximumOf.setName("lblGratuityMaximumOf");
        txtGratuityMaximumOfValue.setName("txtGratuityMaximumOfValue");
        lblGratuityMonths.setName("lblGratuityMonths");
        lblGratuityBeyond.setName("lblGratuityBeyond");
        txtGratuityBeyondValue.setName("txtGratuityBeyondValue");
        lblYearOfService.setName("lblYearOfService");
        txtGratuityBeyondServiceValue.setName("txtGratuityBeyondServiceValue");
        lblGratuityBeyondMonthPay.setName("lblGratuityBeyondMonthPay");
        lblGratuityMaximumAmtBeyond.setName("lblGratuityMaximumAmtBeyond");
        txtGratuityMaximumAmtBeyongValue.setName("txtGratuityMaximumAmtBeyongValue");
        btnGratuityNew.setName("btnGratuityNew");
        btnGratuitySave.setName("btnGratuitySave");
        btnGratuityDelete.setName("btnGratuityDelete");
        
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new MisecllaniousDeductionRB();
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        
        lblHaltingSLNO.setText(resourceBundle.getString("lblHaltingSLNO"));
        lblHaltingDesignation.setText(resourceBundle.getString("lblHaltingDesignation"));
        lblHaltingFromDate.setText(resourceBundle.getString("lblHaltingFromDate"));
        lblHaltingToDate.setText(resourceBundle.getString("lblHaltingToDate"));
        lblHaltingllowanceType.setText(resourceBundle.getString("lblHaltingllowanceType"));
        lblHaltingParameterBasedon.setText(resourceBundle.getString("lblHaltingParameterBasedon"));
        lblHaltingSubParameter.setText(resourceBundle.getString("lblHaltingSubParameter"));
        lblHaltingFixedAmt.setText(resourceBundle.getString("lblHaltingFixedAmt"));
        lblHaltingPercentage.setText(resourceBundle.getString("lblHaltingPercentage"));
        lblHaltingMaximumOf.setText(resourceBundle.getString("lblHaltingMaximumOf"));
        
        lblMDSLNO.setText(resourceBundle.getString("lblMDSLNO"));
        lblMisecllaniousDeduction.setText(resourceBundle.getString("lblMisecllaniousDeduction"));
        lblMDDeductionType.setText(resourceBundle.getString("lblMDDeductionType"));
        lblMDFromDate.setText(resourceBundle.getString("lblMDFromDate"));
        lblMDToDate.setText(resourceBundle.getString("lblMDToDate"));
        lblMDMaximumOf.setText(resourceBundle.getString("lblMDMaximumOf"));
        lblMDPercentage.setText(resourceBundle.getString("lblMDPercentage"));
        lblMDEligibleAllowances.setText(resourceBundle.getString("lblMDEligibleAllowances"));
        lblMDEligiblePercentage.setText(resourceBundle.getString("lblMDEligiblePercentage"));
        
        lblGratuitySLNO.setText(resourceBundle.getString("lblGratuitySLNO"));
        lblGratuityDesignation.setText(resourceBundle.getString("lblGratuityDesignation"));
        lblGratuityFromDate.setText(resourceBundle.getString("lblGratuityFromDate"));
        lblGratuityToDate.setText(resourceBundle.getString("lblGratuityToDate"));
        lblGratuityCityType.setText(resourceBundle.getString("lblGratuityCityType"));
        lblGratuityUpto.setText(resourceBundle.getString("lblGratuityUpto"));
        lblGratuityYearofService.setText(resourceBundle.getString("lblGratuityYearofService"));
        lblGratuityMonthPay.setText(resourceBundle.getString("lblGratuityMonthPay"));
        lblGratuityMaximumOf.setText(resourceBundle.getString("lblGratuityMaximumOf"));
        lblGratuityMonths.setText(resourceBundle.getString("lblGratuityMonths"));
        lblGratuityBeyond.setText(resourceBundle.getString("lblGratuityBeyond"));
        lblYearOfService.setText(resourceBundle.getString("lblYearOfService"));
        lblGratuityBeyondMonthPay.setText(resourceBundle.getString("lblGratuityBeyondMonthPay"));
        lblGratuityMaximumAmtBeyond.setText(resourceBundle.getString("lblGratuityMaximumAmtBeyond"));
        
        
    }
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new MisecllaniousDeductionMRB();
        cboHaltingDesignationValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHaltingDesignationValue"));
        tdtHaltingFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtHaltingFromDateValue"));
        tdtHaltingToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtHaltingToDateValue"));
        cboHaltingAllowanceTypeValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHaltingAllowanceTypeValue"));
        cboHaltingParameterBasedOnValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHaltingParameterBasedOnValue"));
        cboHaltingSubParameterValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHaltingSubParameterValue"));
        txtHaltingFixedAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHaltingFixedAmtValue"));
        txtHaltingMaximumOfValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHaltingMaximumOfValue"));
        txtHaltingPercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHaltingPercentageValue"));
        
        cboMDDeductionType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMDDeductionType"));
        cboMisecllaniousDeduction.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMisecllaniousDeduction"));
        tdtMDFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMDFromDateValue"));
        tdtMDToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMDToDateValue"));
        txtMDMaximumAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMDMaximumAmtValue"));
        txtMdFixedAmtValue.setHelpMessage(lblMsg,objMandatoryRB.getString("txtMdFixedAmtValue"));
        txtFromAmount.setHelpMessage(lblMsg,objMandatoryRB.getString("txtFromAmount"));
        txtToAmount.setHelpMessage(lblMsg,objMandatoryRB.getString("txtToAmount"));
        txtMDPercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMDPercentageValue"));
        cboMDEligibleAllowances.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMDEligibleAllowances"));
        txtMDEligiblePercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMDEligiblePercentageValue"));
        
        cboGratuityDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGratuityDesignation"));
        //        cboGratuityCityType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGratuityCityType"));
        tdtGratuityFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtGratuityFromDateValue"));
        tdtGratuityToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtGratuityToDateValue"));
        txtGratuityUptoValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityUptoValue"));
        txtGratuityUptoServiceValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityUptoServiceValue"));
        txtGratuityMaximumOfValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityMaximumOfValue"));
        txtGratuityBeyondValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityBeyondValue"));
        txtGratuityBeyondServiceValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityBeyondServiceValue"));
        txtGratuityMaximumAmtBeyongValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGratuityMaximumAmtBeyongValue"));
        
    }
    private void initComponentData(){
        this.cboHaltingDesignationValue.setModel(observable.getCbmHaltingDesignationValue());
        this.cboHaltingAllowanceTypeValue.setModel(observable.getCbmHaltingAllowanceTypeValue());
        this.cboHaltingParameterBasedOnValue.setModel(observable.getCbmHaltingParameterBasedOnValue());
        this.cboHaltingSubParameterValue.setModel(observable.getCbmHaltingSubParameterValue());
        this.cboMisecllaniousDeduction.setModel(observable.getCbmMisecllaniousDeduction());
        this.cboMDDeductionType.setModel(observable.getCbmMDCityType());
        this.cboMDEligibleAllowances.setModel(observable.getCbmMDEligibleAllowances());
        this.cboGratuityDesignation.setModel(observable.getCbmGratuityDesignation());
        //        this.cboGratuityCityType.setModel(observable.getCbmGratuityCityType());
    }
    private void setObservable() {
        observable = MisecllaniousDeductionOB.getInstance();
        observable.addObserver(this);
    }
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        //        btnDelete.setEnabled(btnNew.isEnabled());
        
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
    private void setMaxLength(){
        //MisecllaniousDeduction allowances
        txtMDMaximumAmtValue.setValidation(new CurrencyValidation(14,2));
        txtMdFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
        txtMDPercentageValue.setValidation(new NumericValidation(3,3));
        txtMDEligiblePercentageValue.setValidation(new NumericValidation(3,3));
        //        txtTotalNoofSlabValue.setValidation(new NumericValidation(14,2));
        //        txtDATotalDAPercentageValue.setValidation(new NumericValidation(14,2));
        //Gratuity
        txtGratuityUptoValue.setValidation(new NumericValidation(14,2));
        txtGratuityUptoServiceValue.setValidation(new NumericValidation(14,2));
        txtGratuityMaximumOfValue.setValidation(new NumericValidation(14,2));
        txtGratuityBeyondValue.setValidation(new NumericValidation(14,2));
        txtGratuityBeyondServiceValue.setValidation(new NumericValidation(14,2));
        txtGratuityMaximumAmtBeyongValue.setValidation(new CurrencyValidation(14,2));
        
        //TravellingAllowances
        //other allowances
        txtHaltingFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        txtHaltingPercentageValue.setValidation(new CurrencyValidation(14,2));
        txtHaltingMaximumOfValue.setValidation(new NumericValidation(14,2));
        //        txtHaltingWashingAllownaceValue.setValidation(new CurrencyValidation(14,2));
        //        txtHaltingCycleAllowanceValue.setValidation(new CurrencyValidation(14,2));
        //        txtHaltingShiftDutyAllowanceValue.setValidation(new CurrencyValidation(14,2));
    }
    
    private void addRadioButton(){
    }
    
    private void removeRadioButton(){
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgStagnationIncrement = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgHRAPayable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgUsingBasic = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrMisecllaniousDeductions = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMisecllaniousDeductions = new com.see.truetransact.uicomponent.CPanel();
        tabMisecllaniousDeductions = new com.see.truetransact.uicomponent.CTabbedPane();
        panMisecllaniousDeductionsTab = new com.see.truetransact.uicomponent.CPanel();
        panMisecllaniousDeductionTable = new com.see.truetransact.uicomponent.CPanel();
        srpMisecllaniousDeductions = new com.see.truetransact.uicomponent.CScrollPane();
        tblMisecllaniousDeductions = new com.see.truetransact.uicomponent.CTable();
        panMisecllaniousDeductionInfo = new com.see.truetransact.uicomponent.CPanel();
        lblMDSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblMDSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblMDDeductionType = new com.see.truetransact.uicomponent.CLabel();
        cboMDDeductionType = new com.see.truetransact.uicomponent.CComboBox();
        lblMDFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMDFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblMDToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMDToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblMDEligiblePercentage = new com.see.truetransact.uicomponent.CLabel();
        txtMDEligiblePercentageValue = new com.see.truetransact.uicomponent.CTextField();
        lblMDMaximumOf = new com.see.truetransact.uicomponent.CLabel();
        txtMDMaximumAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblMisecllaniousDeduction = new com.see.truetransact.uicomponent.CLabel();
        cboMisecllaniousDeduction = new com.see.truetransact.uicomponent.CComboBox();
        panMDButtons = new com.see.truetransact.uicomponent.CPanel();
        btnMDNew = new com.see.truetransact.uicomponent.CButton();
        btnMDSave = new com.see.truetransact.uicomponent.CButton();
        btnMDDelete = new com.see.truetransact.uicomponent.CButton();
        lblMDEligibleAllowances = new com.see.truetransact.uicomponent.CLabel();
        cboMDEligibleAllowances = new com.see.truetransact.uicomponent.CComboBox();
        txtMDPercentageValue = new com.see.truetransact.uicomponent.CTextField();
        lblMDPercentage = new com.see.truetransact.uicomponent.CLabel();
        panMdType = new com.see.truetransact.uicomponent.CPanel();
        lblMdFixed = new com.see.truetransact.uicomponent.CLabel();
        chkMdFixedValue = new com.see.truetransact.uicomponent.CCheckBox();
        lblMdPecentage = new com.see.truetransact.uicomponent.CLabel();
        chkMdPercentageValue = new com.see.truetransact.uicomponent.CCheckBox();
        txtMdFixedAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblMdFixedAmt = new com.see.truetransact.uicomponent.CLabel();
        lblUsingBasic = new com.see.truetransact.uicomponent.CLabel();
        panUsinBasic = new com.see.truetransact.uicomponent.CPanel();
        rdoUsingBasic_Basic = new com.see.truetransact.uicomponent.CRadioButton();
        rdoUsingBasic_Others = new com.see.truetransact.uicomponent.CRadioButton();
        rdoUsingBasic_Gross = new com.see.truetransact.uicomponent.CRadioButton();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmount = new com.see.truetransact.uicomponent.CTextField();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        panHaltingAllowances = new com.see.truetransact.uicomponent.CPanel();
        panHaltingAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblHaltingSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblHaltingSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblHaltingDesignation = new com.see.truetransact.uicomponent.CLabel();
        cboHaltingDesignationValue = new com.see.truetransact.uicomponent.CComboBox();
        lblHaltingFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtHaltingFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblHaltingToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtHaltingToDateValue = new com.see.truetransact.uicomponent.CDateField();
        panHaltingButtons = new com.see.truetransact.uicomponent.CPanel();
        btnHaltingNew = new com.see.truetransact.uicomponent.CButton();
        btnHaltingSave = new com.see.truetransact.uicomponent.CButton();
        btnHaltingDelete = new com.see.truetransact.uicomponent.CButton();
        txtHaltingPercentageValue = new com.see.truetransact.uicomponent.CTextField();
        lblHaltingPercentage = new com.see.truetransact.uicomponent.CLabel();
        lblHaltingFixedAmt = new com.see.truetransact.uicomponent.CLabel();
        txtHaltingFixedAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblHaltingParameterBasedon = new com.see.truetransact.uicomponent.CLabel();
        lblHaltingMaximumOf = new com.see.truetransact.uicomponent.CLabel();
        txtHaltingMaximumOfValue = new com.see.truetransact.uicomponent.CTextField();
        cboHaltingParameterBasedOnValue = new com.see.truetransact.uicomponent.CComboBox();
        lblHaltingSubParameter = new com.see.truetransact.uicomponent.CLabel();
        cboHaltingAllowanceTypeValue = new com.see.truetransact.uicomponent.CComboBox();
        lblHaltingllowanceType = new com.see.truetransact.uicomponent.CLabel();
        cboHaltingSubParameterValue = new com.see.truetransact.uicomponent.CComboBox();
        panHaltingDiem = new com.see.truetransact.uicomponent.CPanel();
        lblOAFixed = new com.see.truetransact.uicomponent.CLabel();
        chkHaltingFixedValue = new com.see.truetransact.uicomponent.CCheckBox();
        lblOAPecentage = new com.see.truetransact.uicomponent.CLabel();
        chkHaltingPercentageValue = new com.see.truetransact.uicomponent.CCheckBox();
        panHaltingAllowancesTable = new com.see.truetransact.uicomponent.CPanel();
        srpHaltingAllowances = new com.see.truetransact.uicomponent.CScrollPane();
        tblHaltingAllowances = new com.see.truetransact.uicomponent.CTable();
        panGratuityAllowance = new com.see.truetransact.uicomponent.CPanel();
        panGratuityAllowanceTable = new com.see.truetransact.uicomponent.CPanel();
        srpGratuity = new com.see.truetransact.uicomponent.CScrollPane();
        tblGratuity = new com.see.truetransact.uicomponent.CTable();
        panGratuityAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblGratuitySLNO = new com.see.truetransact.uicomponent.CLabel();
        lblGratuitySLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityDesignation = new com.see.truetransact.uicomponent.CLabel();
        cboGratuityDesignation = new com.see.truetransact.uicomponent.CComboBox();
        lblGratuityFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtGratuityFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblGratuityToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtGratuityToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblGratuityCityType = new com.see.truetransact.uicomponent.CLabel();
        cboGratuityCityType = new com.see.truetransact.uicomponent.CComboBox();
        lblGratuityUpto = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityUptoValue = new com.see.truetransact.uicomponent.CTextField();
        lblGratuityCompleted = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityUptoServiceValue = new com.see.truetransact.uicomponent.CTextField();
        lblGratuityMonthPay = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityMaximumOf = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityMaximumOfValue = new com.see.truetransact.uicomponent.CTextField();
        lblGratuityMonths = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityBeyond = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityBeyondValue = new com.see.truetransact.uicomponent.CTextField();
        lblCompletedYearOfService = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityBeyondServiceValue = new com.see.truetransact.uicomponent.CTextField();
        lblGratuityBeyondMonthPay = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityMaximumAmtBeyond = new com.see.truetransact.uicomponent.CLabel();
        txtGratuityMaximumAmtBeyongValue = new com.see.truetransact.uicomponent.CTextField();
        panGratuityButtons = new com.see.truetransact.uicomponent.CPanel();
        btnGratuityNew = new com.see.truetransact.uicomponent.CButton();
        btnGratuitySave = new com.see.truetransact.uicomponent.CButton();
        btnGratuityDelete = new com.see.truetransact.uicomponent.CButton();
        lblGratuityUptoMonths = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityYearofService = new com.see.truetransact.uicomponent.CLabel();
        lblYearOfService = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityCityType1 = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityCityType2 = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityCityType3 = new com.see.truetransact.uicomponent.CLabel();
        lblGratuityCityType4 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(850, 525));
        setPreferredSize(new java.awt.Dimension(850, 525));

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
        tbrMisecllaniousDeductions.add(btnView);

        lblSpace5.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnDelete);

        lblSpace2.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnCancel);

        lblSpace3.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Close");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Print");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Print");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnReject);

        lblSpace4.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnClose);

        getContentPane().add(tbrMisecllaniousDeductions, java.awt.BorderLayout.NORTH);

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

        panMisecllaniousDeductions.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(860, 400));
        panMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(860, 400));
        panMisecllaniousDeductions.setLayout(new java.awt.GridBagLayout());

        tabMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(840, 650));
        tabMisecllaniousDeductions.setName("");
        tabMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(840, 650));

        panMisecllaniousDeductionsTab.setMinimumSize(new java.awt.Dimension(750, 230));
        panMisecllaniousDeductionsTab.setPreferredSize(new java.awt.Dimension(750, 230));
        panMisecllaniousDeductionsTab.setLayout(new java.awt.GridBagLayout());

        panMisecllaniousDeductionTable.setMinimumSize(new java.awt.Dimension(500, 403));
        panMisecllaniousDeductionTable.setPreferredSize(new java.awt.Dimension(500, 403));
        panMisecllaniousDeductionTable.setLayout(new java.awt.GridBagLayout());

        srpMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(200, 404));
        srpMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(200, 404));

        tblMisecllaniousDeductions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(60, 64));
        tblMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblMisecllaniousDeductions.setOpaque(false);
        tblMisecllaniousDeductions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMisecllaniousDeductionsMouseClicked(evt);
            }
        });
        srpMisecllaniousDeductions.setViewportView(tblMisecllaniousDeductions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panMisecllaniousDeductionTable.add(srpMisecllaniousDeductions, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMisecllaniousDeductionsTab.add(panMisecllaniousDeductionTable, gridBagConstraints);

        panMisecllaniousDeductionInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panMisecllaniousDeductionInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panMisecllaniousDeductionInfo.setLayout(new java.awt.GridBagLayout());

        lblMDSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDSLNO, gridBagConstraints);

        lblMDSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblMDSLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblMDSLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(lblMDSLNOValue, gridBagConstraints);

        lblMDDeductionType.setText("Deduction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDDeductionType, gridBagConstraints);

        cboMDDeductionType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMDDeductionType.setPopupWidth(250);
        cboMDDeductionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMDDeductionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(cboMDDeductionType, gridBagConstraints);

        lblMDFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(tdtMDFromDateValue, gridBagConstraints);

        lblMDToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(tdtMDToDateValue, gridBagConstraints);

        lblMDEligiblePercentage.setText("% of amount eligible");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDEligiblePercentage, gridBagConstraints);

        txtMDEligiblePercentageValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMDEligiblePercentageValue.setValidation(new CurrencyValidation(14,2));
        txtMDEligiblePercentageValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMDEligiblePercentageValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(txtMDEligiblePercentageValue, gridBagConstraints);

        lblMDMaximumOf.setText("Maximum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDMaximumOf, gridBagConstraints);

        txtMDMaximumAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMDMaximumAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(txtMDMaximumAmtValue, gridBagConstraints);

        lblMisecllaniousDeduction.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMisecllaniousDeduction, gridBagConstraints);

        cboMisecllaniousDeduction.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMisecllaniousDeduction.setPopupWidth(250);
        cboMisecllaniousDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMisecllaniousDeductionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(cboMisecllaniousDeduction, gridBagConstraints);

        btnMDNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnMDNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMDNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDNewActionPerformed(evt);
            }
        });
        panMDButtons.add(btnMDNew);

        btnMDSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnMDSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMDSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSaveActionPerformed(evt);
            }
        });
        panMDButtons.add(btnMDSave);

        btnMDDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnMDDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMDDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDDeleteActionPerformed(evt);
            }
        });
        panMDButtons.add(btnMDDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMisecllaniousDeductionInfo.add(panMDButtons, gridBagConstraints);

        lblMDEligibleAllowances.setText("Eligible Allowances");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDEligibleAllowances, gridBagConstraints);

        cboMDEligibleAllowances.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMDEligibleAllowances.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(cboMDEligibleAllowances, gridBagConstraints);

        txtMDPercentageValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMDPercentageValue.setValidation(new CurrencyValidation(14,2));
        txtMDPercentageValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMDPercentageValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(txtMDPercentageValue, gridBagConstraints);

        lblMDPercentage.setText("% of amount to be deducted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMDPercentage, gridBagConstraints);

        panMdType.setMinimumSize(new java.awt.Dimension(160, 18));
        panMdType.setPreferredSize(new java.awt.Dimension(160, 18));
        panMdType.setLayout(new java.awt.GridBagLayout());

        lblMdFixed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMdFixed.setText("Fixed");
        lblMdFixed.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panMdType.add(lblMdFixed, gridBagConstraints);

        chkMdFixedValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMdFixedValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMdType.add(chkMdFixedValue, gridBagConstraints);

        lblMdPecentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMdPecentage.setText("Percentage");
        lblMdPecentage.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panMdType.add(lblMdPecentage, gridBagConstraints);

        chkMdPercentageValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMdPercentageValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMdType.add(chkMdPercentageValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 1, 28);
        panMisecllaniousDeductionInfo.add(panMdType, gridBagConstraints);

        txtMdFixedAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMdFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(txtMdFixedAmtValue, gridBagConstraints);

        lblMdFixedAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMdFixedAmt.setText("Fixed Amount");
        lblMdFixedAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMisecllaniousDeductionInfo.add(lblMdFixedAmt, gridBagConstraints);

        lblUsingBasic.setText("Using ");
        lblUsingBasic.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMisecllaniousDeductionInfo.add(lblUsingBasic, gridBagConstraints);

        panUsinBasic.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panUsinBasic.setMinimumSize(new java.awt.Dimension(190, 27));
        panUsinBasic.setName("panMaritalStatus");
        panUsinBasic.setPreferredSize(new java.awt.Dimension(190, 27));
        panUsinBasic.setLayout(new java.awt.GridBagLayout());

        rdgUsingBasic.add(rdoUsingBasic_Basic);
        rdoUsingBasic_Basic.setText("Basic");
        rdoUsingBasic_Basic.setMaximumSize(new java.awt.Dimension(60, 21));
        rdoUsingBasic_Basic.setMinimumSize(new java.awt.Dimension(60, 21));
        rdoUsingBasic_Basic.setName("rdoMaritalStatus_Single");
        rdoUsingBasic_Basic.setPreferredSize(new java.awt.Dimension(60, 21));
        rdoUsingBasic_Basic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUsingBasic_BasicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panUsinBasic.add(rdoUsingBasic_Basic, gridBagConstraints);

        rdgUsingBasic.add(rdoUsingBasic_Others);
        rdoUsingBasic_Others.setText("Others");
        rdoUsingBasic_Others.setMaximumSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Others.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Others.setName("rdoMaritalStatus_Married");
        rdoUsingBasic_Others.setPreferredSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Others.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUsingBasic_OthersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panUsinBasic.add(rdoUsingBasic_Others, gridBagConstraints);

        rdgUsingBasic.add(rdoUsingBasic_Gross);
        rdoUsingBasic_Gross.setText("Gross");
        rdoUsingBasic_Gross.setMaximumSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Gross.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Gross.setName("rdoMaritalStatus_Single");
        rdoUsingBasic_Gross.setPreferredSize(new java.awt.Dimension(65, 21));
        rdoUsingBasic_Gross.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUsingBasic_GrossActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panUsinBasic.add(rdoUsingBasic_Gross, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 5);
        panMisecllaniousDeductionInfo.add(panUsinBasic, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        lblFromAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
        panMisecllaniousDeductionInfo.add(lblFromAmount, gridBagConstraints);

        txtFromAmount.setMaxLength(128);
        txtFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmount.setName("txtCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panMisecllaniousDeductionInfo.add(txtFromAmount, gridBagConstraints);

        lblToAmount.setText(" To Amount");
        lblToAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(1, 93, 1, 1);
        panMisecllaniousDeductionInfo.add(lblToAmount, gridBagConstraints);

        txtToAmount.setMaxLength(128);
        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.setName("txtCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 51);
        panMisecllaniousDeductionInfo.add(txtToAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMisecllaniousDeductionsTab.add(panMisecllaniousDeductionInfo, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Misecllanious Deductions", panMisecllaniousDeductionsTab);

        panHaltingAllowances.setMinimumSize(new java.awt.Dimension(750, 230));
        panHaltingAllowances.setPreferredSize(new java.awt.Dimension(750, 230));
        panHaltingAllowances.setLayout(new java.awt.GridBagLayout());

        panHaltingAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panHaltingAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panHaltingAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblHaltingSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingSLNO, gridBagConstraints);

        lblHaltingSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblHaltingSLNOValue.setMinimumSize(new java.awt.Dimension(75, 16));
        lblHaltingSLNOValue.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(lblHaltingSLNOValue, gridBagConstraints);

        lblHaltingDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingDesignation, gridBagConstraints);

        cboHaltingDesignationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHaltingDesignationValue.setPopupWidth(250);
        cboHaltingDesignationValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHaltingDesignationValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(cboHaltingDesignationValue, gridBagConstraints);

        lblHaltingFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(tdtHaltingFromDateValue, gridBagConstraints);

        lblHaltingToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(tdtHaltingToDateValue, gridBagConstraints);

        btnHaltingNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnHaltingNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHaltingNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHaltingNewActionPerformed(evt);
            }
        });
        panHaltingButtons.add(btnHaltingNew);

        btnHaltingSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnHaltingSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHaltingSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHaltingSaveActionPerformed(evt);
            }
        });
        panHaltingButtons.add(btnHaltingSave);

        btnHaltingDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnHaltingDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHaltingDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHaltingDeleteActionPerformed(evt);
            }
        });
        panHaltingButtons.add(btnHaltingDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHaltingAllowanceInfo.add(panHaltingButtons, gridBagConstraints);

        txtHaltingPercentageValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHaltingPercentageValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(txtHaltingPercentageValue, gridBagConstraints);

        lblHaltingPercentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHaltingPercentage.setText("Percentage");
        lblHaltingPercentage.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingPercentage, gridBagConstraints);

        lblHaltingFixedAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHaltingFixedAmt.setText("Fixed Amount");
        lblHaltingFixedAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingFixedAmt, gridBagConstraints);

        txtHaltingFixedAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHaltingFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        txtHaltingFixedAmtValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHaltingFixedAmtValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(txtHaltingFixedAmtValue, gridBagConstraints);

        lblHaltingParameterBasedon.setText("Parameter Based on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingParameterBasedon, gridBagConstraints);

        lblHaltingMaximumOf.setText("Maximum of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingMaximumOf, gridBagConstraints);

        txtHaltingMaximumOfValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHaltingMaximumOfValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(txtHaltingMaximumOfValue, gridBagConstraints);

        cboHaltingParameterBasedOnValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHaltingParameterBasedOnValue.setPopupWidth(250);
        cboHaltingParameterBasedOnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHaltingParameterBasedOnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(cboHaltingParameterBasedOnValue, gridBagConstraints);

        lblHaltingSubParameter.setText("Sub Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingSubParameter, gridBagConstraints);

        cboHaltingAllowanceTypeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHaltingAllowanceTypeValue.setPopupWidth(250);
        cboHaltingAllowanceTypeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHaltingAllowanceTypeValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(cboHaltingAllowanceTypeValue, gridBagConstraints);

        lblHaltingllowanceType.setText("Allowance Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHaltingAllowanceInfo.add(lblHaltingllowanceType, gridBagConstraints);

        cboHaltingSubParameterValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHaltingSubParameterValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingAllowanceInfo.add(cboHaltingSubParameterValue, gridBagConstraints);

        panHaltingDiem.setMinimumSize(new java.awt.Dimension(160, 18));
        panHaltingDiem.setPreferredSize(new java.awt.Dimension(160, 18));
        panHaltingDiem.setLayout(new java.awt.GridBagLayout());

        lblOAFixed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAFixed.setText("Fixed");
        lblOAFixed.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panHaltingDiem.add(lblOAFixed, gridBagConstraints);

        chkHaltingFixedValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHaltingFixedValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingDiem.add(chkHaltingFixedValue, gridBagConstraints);

        lblOAPecentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAPecentage.setText("Percentage");
        lblOAPecentage.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panHaltingDiem.add(lblOAPecentage, gridBagConstraints);

        chkHaltingPercentageValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHaltingPercentageValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHaltingDiem.add(chkHaltingPercentageValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 75, 1, 1);
        panHaltingAllowanceInfo.add(panHaltingDiem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panHaltingAllowances.add(panHaltingAllowanceInfo, gridBagConstraints);

        panHaltingAllowancesTable.setMinimumSize(new java.awt.Dimension(500, 403));
        panHaltingAllowancesTable.setPreferredSize(new java.awt.Dimension(500, 403));
        panHaltingAllowancesTable.setLayout(new java.awt.GridBagLayout());

        srpHaltingAllowances.setMinimumSize(new java.awt.Dimension(200, 404));
        srpHaltingAllowances.setPreferredSize(new java.awt.Dimension(200, 404));

        tblHaltingAllowances.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHaltingAllowances.setMinimumSize(new java.awt.Dimension(60, 64));
        tblHaltingAllowances.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblHaltingAllowances.setOpaque(false);
        tblHaltingAllowances.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHaltingAllowancesMouseClicked(evt);
            }
        });
        srpHaltingAllowances.setViewportView(tblHaltingAllowances);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panHaltingAllowancesTable.add(srpHaltingAllowances, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panHaltingAllowances.add(panHaltingAllowancesTable, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Halting / DIEM Allowance", panHaltingAllowances);

        panGratuityAllowance.setMinimumSize(new java.awt.Dimension(750, 230));
        panGratuityAllowance.setPreferredSize(new java.awt.Dimension(750, 230));
        panGratuityAllowance.setLayout(new java.awt.GridBagLayout());

        panGratuityAllowanceTable.setMinimumSize(new java.awt.Dimension(300, 403));
        panGratuityAllowanceTable.setPreferredSize(new java.awt.Dimension(300, 403));
        panGratuityAllowanceTable.setLayout(new java.awt.GridBagLayout());

        srpGratuity.setMinimumSize(new java.awt.Dimension(190, 404));
        srpGratuity.setPreferredSize(new java.awt.Dimension(190, 404));

        tblGratuity.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGratuity.setMinimumSize(new java.awt.Dimension(60, 64));
        tblGratuity.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblGratuity.setOpaque(false);
        tblGratuity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGratuityMouseClicked(evt);
            }
        });
        srpGratuity.setViewportView(tblGratuity);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panGratuityAllowanceTable.add(srpGratuity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGratuityAllowance.add(panGratuityAllowanceTable, gridBagConstraints);

        panGratuityAllowanceInfo.setMinimumSize(new java.awt.Dimension(660, 400));
        panGratuityAllowanceInfo.setPreferredSize(new java.awt.Dimension(660, 400));
        panGratuityAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblGratuitySLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuitySLNO, gridBagConstraints);

        lblGratuitySLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblGratuitySLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblGratuitySLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(lblGratuitySLNOValue, gridBagConstraints);

        lblGratuityDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityDesignation, gridBagConstraints);

        cboGratuityDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGratuityDesignation.setPopupWidth(250);
        cboGratuityDesignation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGratuityDesignationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(cboGratuityDesignation, gridBagConstraints);

        lblGratuityFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(tdtGratuityFromDateValue, gridBagConstraints);

        lblGratuityToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(tdtGratuityToDateValue, gridBagConstraints);

        lblGratuityCityType.setText("Gratuity Rule 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityCityType, gridBagConstraints);

        cboGratuityCityType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGratuityCityType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(cboGratuityCityType, gridBagConstraints);

        lblGratuityUpto.setText("Up to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panGratuityAllowanceInfo.add(lblGratuityUpto, gridBagConstraints);

        txtGratuityUptoValue.setMinimumSize(new java.awt.Dimension(100, 22));
        txtGratuityUptoValue.setPreferredSize(new java.awt.Dimension(100, 22));
        txtGratuityUptoValue.setValidation(new CurrencyValidation(14,2));
        txtGratuityUptoValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGratuityUptoValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 1);
        panGratuityAllowanceInfo.add(txtGratuityUptoValue, gridBagConstraints);

        lblGratuityCompleted.setText("completed year of Service");
        lblGratuityCompleted.setMinimumSize(new java.awt.Dimension(149, 15));
        lblGratuityCompleted.setPreferredSize(new java.awt.Dimension(149, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panGratuityAllowanceInfo.add(lblGratuityCompleted, gridBagConstraints);

        txtGratuityUptoServiceValue.setMinimumSize(new java.awt.Dimension(30, 21));
        txtGratuityUptoServiceValue.setPreferredSize(new java.awt.Dimension(40, 21));
        txtGratuityUptoServiceValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(txtGratuityUptoServiceValue, gridBagConstraints);

        lblGratuityMonthPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGratuityMonthPay.setText("Month/s pay");
        lblGratuityMonthPay.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityMonthPay, gridBagConstraints);

        lblGratuityMaximumOf.setText("With the maximum of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panGratuityAllowanceInfo.add(lblGratuityMaximumOf, gridBagConstraints);

        txtGratuityMaximumOfValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGratuityMaximumOfValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(txtGratuityMaximumOfValue, gridBagConstraints);

        lblGratuityMonths.setText("AND");
        lblGratuityMonths.setFont(new java.awt.Font("MS Sans Serif", 1, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityMonths, gridBagConstraints);

        lblGratuityBeyond.setText("Beyond");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityBeyond, gridBagConstraints);

        txtGratuityBeyondValue.setMinimumSize(new java.awt.Dimension(100, 22));
        txtGratuityBeyondValue.setPreferredSize(new java.awt.Dimension(100, 22));
        txtGratuityBeyondValue.setValidation(new CurrencyValidation(14,2));
        txtGratuityBeyondValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGratuityBeyondValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 1);
        panGratuityAllowanceInfo.add(txtGratuityBeyondValue, gridBagConstraints);

        lblCompletedYearOfService.setText("completed year of Service");
        lblCompletedYearOfService.setMinimumSize(new java.awt.Dimension(149, 15));
        lblCompletedYearOfService.setPreferredSize(new java.awt.Dimension(149, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panGratuityAllowanceInfo.add(lblCompletedYearOfService, gridBagConstraints);

        txtGratuityBeyondServiceValue.setMinimumSize(new java.awt.Dimension(30, 21));
        txtGratuityBeyondServiceValue.setPreferredSize(new java.awt.Dimension(40, 21));
        txtGratuityBeyondServiceValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(txtGratuityBeyondServiceValue, gridBagConstraints);

        lblGratuityBeyondMonthPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGratuityBeyondMonthPay.setText("Month/s pay");
        lblGratuityBeyondMonthPay.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityBeyondMonthPay, gridBagConstraints);

        lblGratuityMaximumAmtBeyond.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGratuityMaximumAmtBeyond.setText("Maximum Gratuity amount Payable");
        lblGratuityMaximumAmtBeyond.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityMaximumAmtBeyond, gridBagConstraints);

        txtGratuityMaximumAmtBeyongValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGratuityMaximumAmtBeyongValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panGratuityAllowanceInfo.add(txtGratuityMaximumAmtBeyongValue, gridBagConstraints);

        btnGratuityNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnGratuityNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnGratuityNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGratuityNewActionPerformed(evt);
            }
        });
        panGratuityButtons.add(btnGratuityNew);

        btnGratuitySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnGratuitySave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnGratuitySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGratuitySaveActionPerformed(evt);
            }
        });
        panGratuityButtons.add(btnGratuitySave);

        btnGratuityDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnGratuityDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnGratuityDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGratuityDeleteActionPerformed(evt);
            }
        });
        panGratuityButtons.add(btnGratuityDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGratuityAllowanceInfo.add(panGratuityButtons, gridBagConstraints);

        lblGratuityUptoMonths.setText("Months pay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityUptoMonths, gridBagConstraints);

        lblGratuityYearofService.setText("Years of Service for each");
        lblGratuityYearofService.setMinimumSize(new java.awt.Dimension(144, 15));
        lblGratuityYearofService.setPreferredSize(new java.awt.Dimension(144, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panGratuityAllowanceInfo.add(lblGratuityYearofService, gridBagConstraints);

        lblYearOfService.setText("Years of Service for each");
        lblYearOfService.setMinimumSize(new java.awt.Dimension(144, 15));
        lblYearOfService.setPreferredSize(new java.awt.Dimension(144, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panGratuityAllowanceInfo.add(lblYearOfService, gridBagConstraints);

        lblGratuityCityType1.setText("Gratuity Rule 2 :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityCityType1, gridBagConstraints);

        lblGratuityCityType2.setText("As per Gratuity Act");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityCityType2, gridBagConstraints);

        lblGratuityCityType3.setText("OR");
        lblGratuityCityType3.setFont(new java.awt.Font("MS Sans Serif", 1, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityCityType3, gridBagConstraints);

        lblGratuityCityType4.setText("Whichever higher");
        lblGratuityCityType4.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panGratuityAllowanceInfo.add(lblGratuityCityType4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGratuityAllowance.add(panGratuityAllowanceInfo, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Gratuity", panGratuityAllowance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMisecllaniousDeductions.add(tabMisecllaniousDeductions, gridBagConstraints);

        getContentPane().add(panMisecllaniousDeductions, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
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
    
    private void rdoUsingBasic_GrossActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoUsingBasic_GrossActionPerformed
        // TODO add your handling code here:
        txtFromAmount.setVisible(true);
        txtToAmount.setVisible(true);
        //        txtToAmount.setText("");
        //        txtFromAmount.setText("");
        lblFromAmount.setVisible(true);
        lblToAmount.setVisible(true);
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
    }//GEN-LAST:event_rdoUsingBasic_GrossActionPerformed
    
    private void rdoUsingBasic_OthersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoUsingBasic_OthersActionPerformed
        // TODO add your handling code here:
        txtFromAmount.setVisible(false);
        txtToAmount.setVisible(false);
        //        txtToAmount.setText("");
        //        txtFromAmount.setText("");
        lblFromAmount.setVisible(false);
        lblToAmount.setVisible(false);
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
    }//GEN-LAST:event_rdoUsingBasic_OthersActionPerformed
    
    private void rdoUsingBasic_BasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoUsingBasic_BasicActionPerformed
        // TODO add your handling code here:
        txtFromAmount.setVisible(true);
        txtToAmount.setVisible(true);
        //        txtToAmount.setText("");
        //        txtFromAmount.setText("");
        lblFromAmount.setVisible(true);
        lblToAmount.setVisible(true);
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
    }//GEN-LAST:event_rdoUsingBasic_BasicActionPerformed
    
    private void chkMdPercentageValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMdPercentageValueActionPerformed
        
        chkMdPercentageValue.setEnabled(true);
        chkMdPercentageValue.setSelected(true);
        chkMdFixedValue.setEnabled(true);
        chkMdFixedValue.setSelected(false);
        txtMdFixedAmtValue.setVisible(false);
        lblMdFixedAmt.setVisible(false);
        txtMDMaximumAmtValue.setVisible(true);
        txtMDMaximumAmtValue.setEnabled(true);
        lblMDMaximumOf.setVisible(true);
        txtMDPercentageValue.setVisible(true);
        txtMDPercentageValue.setEnabled(true);
        lblMDPercentage.setVisible(true);
        cboMDEligibleAllowances.setVisible(true);
        cboMDEligibleAllowances.setEnabled(true);
        lblMDEligibleAllowances.setVisible(true);
        txtMDEligiblePercentageValue.setVisible(true);
        txtMDEligiblePercentageValue.setEnabled(true);
        lblMDEligiblePercentage.setVisible(true);
        
        //        added here on 08-09-2011
        txtMDMaximumAmtValue.setText("");
        txtMdFixedAmtValue.setText("");
        txtMDPercentageValue.setText("");
        txtMDEligiblePercentageValue.setText("");
        cboMDEligibleAllowances.setSelectedIndex(0);
        
    }//GEN-LAST:event_chkMdPercentageValueActionPerformed
    
    private void chkMdFixedValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMdFixedValueActionPerformed
        // TODO add your handling code here:
        //        if(chkMdFixedValue.isSelected() == true){
        chkMdPercentageValue.setEnabled(true);
        chkMdPercentageValue.setSelected(false);
        chkMdFixedValue.setEnabled(true);
        chkMdFixedValue.setSelected(true);
        txtMdFixedAmtValue.setVisible(true);
        lblMdFixedAmt.setVisible(true);
        txtMDMaximumAmtValue.setVisible(false);
        lblMDMaximumOf.setVisible(false);
        txtMDPercentageValue.setVisible(false);
        lblMDPercentage.setVisible(false);
        cboMDEligibleAllowances.setVisible(false);
        lblMDEligibleAllowances.setVisible(false);
        txtMDEligiblePercentageValue.setVisible(false);
        lblMDEligiblePercentage.setVisible(false);
        txtMdFixedAmtValue.setEnabled(true);
        
        //        added here on 08-09-2011
        txtMDMaximumAmtValue.setText("");
        txtMdFixedAmtValue.setText("");
        txtMDEligiblePercentageValue.setText("");
        txtMDPercentageValue.setText("");
        cboMDEligibleAllowances.setSelectedIndex(0);
        
    }//GEN-LAST:event_chkMdFixedValueActionPerformed
    
    private void txtMDPercentageValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMDPercentageValueFocusLost
        // TODO add your handling code here:
        int percentValue = CommonUtil.convertObjToInt(txtMDPercentageValue.getText());
        if(percentValue >100 || percentValue <0){
            ClientUtil.showMessageWindow("Please Enter Valid percentage value!!");
            txtMDPercentageValue.setText("");
        }
    }//GEN-LAST:event_txtMDPercentageValueFocusLost
    
    private void txtMDEligiblePercentageValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMDEligiblePercentageValueFocusLost
        // TODO add your handling code here:
        int percentValue = CommonUtil.convertObjToInt(txtMDEligiblePercentageValue.getText());
        if(percentValue >100 || percentValue <0){
            ClientUtil.showMessageWindow("Please Enter Valid percentage value!!");
            txtMDEligiblePercentageValue.setText("");
        }
    }//GEN-LAST:event_txtMDEligiblePercentageValueFocusLost
    
    private void cboMDDeductionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMDDeductionTypeActionPerformed
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        Date deductionFrmDate = (Date) DateUtil.getDateMMDDYYYY(tdtMDFromDateValue.getDateValue());
        //        System.out.println("@#$@#$@#$deductionFrmDate.toString():"+deductionFrmDate.toString());
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(deductionFrmDate != null){
                deductionFrmDate = setProperDtFormat(deductionFrmDate);
                whereMap.put("DEDUCTION_FROM_DATE",deductionFrmDate);
                whereMap.put("GRADE",CommonUtil.convertObjToStr(cboMisecllaniousDeduction.getSelectedItem().toString().toUpperCase()));
                whereMap.put("DEDUCTION_TYPE",CommonUtil.convertObjToStr(cboMDDeductionType.getSelectedItem().toString().toUpperCase()));
                System.out.println("@#$@#$@#$whereMap:"+whereMap);
                List checkGradeExistsLst = ClientUtil.executeQuery("checkGradeExistsMisDeduction", whereMap);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && checkGradeExistsLst!= null && checkGradeExistsLst.size() > 0){
                    
                    ClientUtil.showMessageWindow("This Grade is already entered!! Go to edit mode for any updation");
                    cboMisecllaniousDeduction.setSelectedItem("");
                    cboMDDeductionType.setSelectedItem("");
                    tdtMDFromDateValue.setDateValue("");
                    checkGradeExistsLst= null;
                }
            }
        }
    }//GEN-LAST:event_cboMDDeductionTypeActionPerformed
    private Date setProperDtFormat(Date dt){
        Date tempDt=(Date)curDate.clone();
        if(dt!=null){
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void cboGratuityDesignationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGratuityDesignationActionPerformed
        // TODO add your handling code here:
        
        HashMap whereMap = new HashMap();
        whereMap.put("GRADE",CommonUtil.convertObjToStr(cboGratuityDesignation.getSelectedItem()));
        List dateList = ClientUtil.executeQuery("getFromAndToDate",whereMap);
        System.out.println("@#$@#$@#$dateList:"+dateList);
        if(dateList != null && dateList.size() >0){
            HashMap dateMap = (HashMap) dateList.get(0);
            tdtGratuityFromDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("FROM_DATE")));
            tdtGratuityToDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("TO_DATE")));
            tdtGratuityFromDateValue.setEnabled(false);
            tdtGratuityToDateValue.setEnabled(false);
        }
    }//GEN-LAST:event_cboGratuityDesignationActionPerformed
    
    private void cboMisecllaniousDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMisecllaniousDeductionActionPerformed
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        //        String gradeVal = CommonUtil.convertObjToStr(cboMisecllaniousDeduction.getSelectedItem());
        whereMap.put("GRADE",CommonUtil.convertObjToStr(cboMisecllaniousDeduction.getSelectedItem()));
        List dateList = ClientUtil.executeQuery("getFromAndToDate",whereMap);
        System.out.println("@#$@#$@#$dateList:"+dateList);
        if(dateList != null && dateList.size() >0){
            HashMap dateMap = (HashMap) dateList.get(0);
            tdtMDFromDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("FROM_DATE")));
            tdtMDToDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("TO_DATE")));
            tdtMDFromDateValue.setEnabled(true);
            tdtMDToDateValue.setEnabled(false);
        }
    }//GEN-LAST:event_cboMisecllaniousDeductionActionPerformed
    
    private void txtGratuityUptoValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGratuityUptoValueFocusLost
        // TODO add your handling code here:
        txtGratuityBeyondValue.setText(txtGratuityUptoValue.getText());
    }//GEN-LAST:event_txtGratuityUptoValueFocusLost
    
    private void txtGratuityBeyondValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGratuityBeyondValueFocusLost
        // TODO add your handling code here:
        //        double uptoValue = CommonUtil.convertObjToDouble(txtGratuityUptoValue.getText()).doubleValue();
        //        double beyondValue = CommonUtil.convertObjToDouble(txtGratuityBeyondValue.getText()).doubleValue();
        //        if(uptoValue!=beyondValue){
        //            ClientUtil.showAlertWindow("Value should be same as ");
        //            txtGratuityBeyondValue.setText("");
        //            return;
        //        }
    }//GEN-LAST:event_txtGratuityBeyondValueFocusLost
    
    private void txtHaltingFixedAmtValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHaltingFixedAmtValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHaltingFixedAmtValueActionPerformed
    
    private void chkHaltingPercentageValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHaltingPercentageValueActionPerformed
        // TODO add your handling code here:
        if(chkHaltingPercentageValue.isSelected() == true){
            chkHaltingFixedValue.setEnabled(false);
            txtHaltingFixedAmtValue.setVisible(false);
            lblHaltingFixedAmt.setVisible(false);
            lblHaltingPercentage.setVisible(true);
            lblHaltingMaximumOf.setVisible(true);
            txtHaltingMaximumOfValue.setVisible(true);
            txtHaltingPercentageValue.setVisible(true);
        }else if(chkHaltingFixedValue.isSelected() == true){
            chkHaltingPercentageValue.setEnabled(false);
            lblHaltingPercentage.setVisible(true);
            lblHaltingMaximumOf.setVisible(true);
            txtHaltingMaximumOfValue.setVisible(false);
            txtHaltingPercentageValue.setVisible(false);
            txtHaltingFixedAmtValue.setVisible(false);
            lblHaltingFixedAmt.setVisible(false);
        }else{
            chkHaltingFixedValue.setEnabled(true);
            txtHaltingFixedAmtValue.setVisible(false);
            lblHaltingFixedAmt.setVisible(false);
            lblHaltingPercentage.setVisible(false);
            lblHaltingMaximumOf.setVisible(false);
            txtHaltingMaximumOfValue.setVisible(false);
            txtHaltingPercentageValue.setVisible(false);
        }
    }//GEN-LAST:event_chkHaltingPercentageValueActionPerformed
    
    private void chkHaltingFixedValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHaltingFixedValueActionPerformed
        // TODO add your handling code here:
        if(chkHaltingFixedValue.isSelected() == true){
            chkHaltingPercentageValue.setEnabled(false);
            txtHaltingFixedAmtValue.setVisible(true);
            lblHaltingFixedAmt.setVisible(true);
            lblHaltingPercentage.setVisible(false);
            lblHaltingMaximumOf.setVisible(false);
            txtHaltingMaximumOfValue.setVisible(false);
            txtHaltingPercentageValue.setVisible(false);
        }else if(chkHaltingPercentageValue.isSelected() == true){
            chkHaltingPercentageValue.setEnabled(false);
            lblHaltingPercentage.setVisible(false);
            lblHaltingMaximumOf.setVisible(false);
            txtHaltingMaximumOfValue.setVisible(true);
            txtHaltingPercentageValue.setVisible(true);
            txtHaltingFixedAmtValue.setVisible(true);
            lblHaltingFixedAmt.setVisible(true);
        }else{
            chkHaltingPercentageValue.setEnabled(true);
            txtHaltingFixedAmtValue.setVisible(false);
            lblHaltingFixedAmt.setVisible(false);
            lblHaltingPercentage.setVisible(false);
            lblHaltingMaximumOf.setVisible(false);
            txtHaltingMaximumOfValue.setVisible(false);
            txtHaltingPercentageValue.setVisible(false);
        }
    }//GEN-LAST:event_chkHaltingFixedValueActionPerformed
    
    private void cboHaltingAllowanceTypeValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHaltingAllowanceTypeValueActionPerformed
        // TODO add your handling code here:
        //        if(cboHaltingAllowanceTypeValue.getSelectedIndex()>0){
        //            observable.setCbmProd(CommonUtil.convertObjToStr(cboHaltingAllowanceTypeValue.getSelectedItem()));
        ////            observable.setCbmProd(CommonUtil.convertObjToStr(observable.getCbmHaltingAllowanceTypeValue()));
        //            cboHaltingParameterBasedOnValue.setModel(observable.getCbmHaltingParameterBasedOnValue());
        //        }
    }//GEN-LAST:event_cboHaltingAllowanceTypeValueActionPerformed
    
    private void cboHaltingDesignationValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHaltingDesignationValueActionPerformed
        // TODO add your handling code here:
        if(cboHaltingDesignationValue.getSelectedIndex()>0){
            observable.setCbmProdId(CommonUtil.convertObjToStr(cboHaltingDesignationValue.getSelectedItem()));
            observable.setCbmProdId(CommonUtil.convertObjToStr(observable.getCbmHaltingDesignationValue()));
            cboHaltingAllowanceTypeValue.setModel(observable.getCbmHaltingAllowanceTypeValue());
        }
        HashMap whereMap = new HashMap();
        whereMap.put("GRADE",CommonUtil.convertObjToStr(cboHaltingDesignationValue.getSelectedItem()));
        List checkGradeExistsLst = ClientUtil.executeQuery("checkGradeExistsHalting", whereMap);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && checkGradeExistsLst!= null && checkGradeExistsLst.size() > 0){
            ClientUtil.showMessageWindow("This Grade is already entered!! Go to edit mode for any updation");
            cboHaltingDesignationValue.setSelectedItem("");
            checkGradeExistsLst= null;
        }
        else{
            //            cboMisecllaniousDeduction.setSelectedItem(cboHaltingDesignationValue.getSelectedItem());
            //            cboGratuityDesignation.setSelectedItem(cboHaltingDesignationValue.getSelectedItem());
            List dateList = ClientUtil.executeQuery("getFromAndToDate",whereMap);
            System.out.println("@#$@#$@#$dateList:"+dateList);
            if(dateList != null && dateList.size() >0){
                HashMap dateMap = (HashMap) dateList.get(0);
                tdtHaltingFromDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("FROM_DATE")));
                tdtHaltingToDateValue.setDateValue(CommonUtil.convertObjToStr(dateMap.get("TO_DATE")));
                tdtHaltingFromDateValue.setEnabled(false);
                tdtHaltingToDateValue.setEnabled(false);
            }
        }
    }//GEN-LAST:event_cboHaltingDesignationValueActionPerformed
    
    private void btnHaltingDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHaltingDeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblHaltingAllowances.getRowCount();
        int rowSelected = tblHaltingAllowances.getSelectedRow();
        if(observable.getHAAuthorizeStatus()!=null && (observable.getHAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getHAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteHAData(this.tblHaltingAllowances.getSelectedRow());
                this.updateTable();
                resetHAForm();
                observable.resetMDValues();
                enableDisableHA(false);
                btnHaltingNew.setEnabled(true);
                btnHaltingSave.setEnabled(false);
                btnHaltingDelete.setEnabled(false);
                MASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnHaltingDeleteActionPerformed
    
    private void tblHaltingAllowancesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHaltingAllowancesMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationHA(tblHaltingAllowances.getSelectedRow());
            enableDisableHA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblHaltingAllowances.getSelectedRow()>=0){
                _intHANew = false;
                updationHA(tblHaltingAllowances.getSelectedRow());
                if(observable.getHAAuthorizeStatus()!=null && (observable.getHAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getHAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panHaltingAllowanceInfo,false);
                    btnHaltingNew.setEnabled(true);
                    btnHaltingSave.setEnabled(false);
                    btnHaltingDelete.setEnabled(false);
                    OASave = false;
                }else{
                    btnHaltingNew.setEnabled(false);
                    btnHaltingSave.setEnabled(true);
                    btnHaltingDelete.setEnabled(true);
                    txtHaltingFixedAmtValue.setEnabled(true);
                    OASave = true;
                    _intHANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblHaltingAllowancesMouseClicked
    
    private void btnHaltingSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHaltingSaveActionPerformed
        // TODO add your handling code here:
        if(cboHaltingDesignationValue.getSelectedIndex()==0 && cboHaltingDesignationValue.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(designationAlert);
        }else if(tdtHaltingFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(cboHaltingParameterBasedOnValue.getSelectedIndex() == 0 && cboHaltingParameterBasedOnValue.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow("Halting Parameter Based Should not be empty");
        }else if(cboHaltingSubParameterValue.getSelectedIndex() == 0 && cboHaltingSubParameterValue.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow("Halting SubParameter Should not be empty");
        }else if(txtHaltingFixedAmtValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Fixed Amount should not be empty");
        }else{
            long OACboSize = 1;
            if(_intHANew == true && tblHaltingAllowances.getRowCount()>0){
                for (int i = 0;i<tblHaltingAllowances.getRowCount();i++){
                    if(cboHaltingSubParameterValue.getSelectedItem().toString().toUpperCase().equals(tblHaltingAllowances.getValueAt(i, 2))){
                        ClientUtil.showAlertWindow("This value already exists please choose another value");
                        return;
                    }else{
                        OACboSize = OACboSize +1;
                    }
                }
            }
            btnSave.setEnabled(true);
            updateOBFields();
            if(!this._intHANew){
                observable.insertHAData(this.tblHaltingAllowances.getSelectedRow());
            }else{
                observable.insertHAData(-1);
            }
            resetHAForm();
            enableDisableHA(false);
            this.updateTable();
            btnHaltingNew.setEnabled(true);
            btnHaltingSave.setEnabled(false);
            btnHaltingDelete.setEnabled(false);
            chkHaltingFixedValue.setEnabled(false);
            chkHaltingPercentageValue.setEnabled(false);
            this._intHANew = false;
            selectedSingleRow = false;
            MASave = false;
        }
        
    }//GEN-LAST:event_btnHaltingSaveActionPerformed
    private void enableDisableHA(boolean MAVal){
        cboHaltingDesignationValue.setEnabled(MAVal);
        tdtHaltingFromDateValue.setEnabled(MAVal);
        tdtHaltingToDateValue.setEnabled(MAVal);
        cboHaltingAllowanceTypeValue.setEnabled(MAVal);
        txtHaltingFixedAmtValue.setEnabled(MAVal);
        txtHaltingMaximumOfValue.setEnabled(MAVal);
        txtHaltingPercentageValue.setEnabled(MAVal);
        //        txtOAWashingAllownaceValue.setEnabled(MAVal);
        //        txtOACycleAllowanceValue.setEnabled(MAVal);
        //        txtOAShiftDutyAllowanceValue.setEnabled(MAVal);
    }
    
    private void btnHaltingNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHaltingNewActionPerformed
        // TODO add your handling code here:
        _intHANew = true;
        ClientUtil.enableDisable(panHaltingAllowanceInfo,true);
        cboHaltingDesignationValue.setEnabled(false);
        tdtHaltingFromDateValue.setEnabled(false);
        if(tblHaltingAllowances.getRowCount() == 0){
            enableDisableHA(true);
        }else if(tblHaltingAllowances.getRowCount() > 0){
            updationHA(tblHaltingAllowances.getRowCount()-1);
            enableDisableHA(false);
            cboHaltingSubParameterValue.setEnabled(true);
            chkHaltingFixedValue.setEnabled(false);
            chkHaltingPercentageValue.setEnabled(false);
            txtHaltingFixedAmtValue.setEnabled(true);
            txtHaltingMaximumOfValue.setEnabled(true);
            txtHaltingPercentageValue.setEnabled(true);
            if(tblHaltingAllowances.getRowCount()>0){
                String haltingParameter = cboHaltingParameterBasedOnValue.getSelectedItem().toString().toUpperCase();
                HashMap haltingMap = new HashMap();
                haltingMap.put("LOOKUP_ID",haltingParameter);
                System.out.println("@!#$@#$haltingMap:"+haltingMap);
                List countList = ClientUtil.executeQuery("getCountOfHaltingParameter",haltingMap);
                haltingMap = (HashMap)countList.get(0);
                int count = 0;
                int countParameter = CommonUtil.convertObjToInt(haltingMap.get("CNT"));
                for (int i = 0;i<tblHaltingAllowances.getRowCount();i++){
                    System.out.println("@#$@#$@#$countParameter:"+countParameter);
                    if(cboHaltingParameterBasedOnValue.getSelectedItem().toString().toUpperCase().equals(tblHaltingAllowances.getValueAt(i, 1))){
                        {
                            count +=1;
                        }
                    }
                }
                if(countParameter == count ){
                    cboHaltingParameterBasedOnValue.setSelectedItem("");
                    cboHaltingParameterBasedOnValue.setEnabled(true);
                }
                else{
                    cboHaltingParameterBasedOnValue.setEnabled(false);
                }
            }
            resetHAForm();
        }
        btnHaltingSave.setEnabled(true);
        btnHaltingNew.setEnabled(false);
    }//GEN-LAST:event_btnHaltingNewActionPerformed
    
    private void cboHaltingParameterBasedOnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHaltingParameterBasedOnValueActionPerformed
        // TODO add your handling code here:
        if(cboHaltingParameterBasedOnValue.getSelectedIndex()>0){
            observable.setCbmProd(CommonUtil.convertObjToStr(cboHaltingParameterBasedOnValue.getSelectedItem()));
            cboHaltingSubParameterValue.setModel(observable.getCbmHaltingSubParameterValue());
        }
    }//GEN-LAST:event_cboHaltingParameterBasedOnValueActionPerformed
    private void updationHA(int selectDARow) {
        observable.populateHA(selectDARow);
        populateDetail();
    }    private void chkOAllowancePercentage(boolean OAValue){
        lblHaltingPercentage.setVisible(OAValue);
        lblHaltingMaximumOf.setVisible(OAValue);
        txtHaltingPercentageValue.setVisible(OAValue);
        txtHaltingMaximumOfValue.setVisible(OAValue);
    }    private void chkOAllowanceFixed(boolean OAValue){
        txtHaltingFixedAmtValue.setVisible(OAValue);
        lblHaltingFixedAmt.setVisible(OAValue);
    }    private void updationMA(int selectDARow) {
        populateDetail();
    }    private void resetHAForm(){
        txtHaltingFixedAmtValue.setText("");
        txtHaltingMaximumOfValue.setText("");
        txtHaltingPercentageValue.setText("");
        cboHaltingAllowanceTypeValue.setSelectedItem("");
        //        cboHaltingParameterBasedOnValue.setSelectedItem("");
        cboHaltingSubParameterValue.setSelectedItem("");
        btnHaltingSave.setEnabled(false);
        btnHaltingDelete.setEnabled(false);
    }
    
    private void btnGratuityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGratuityDeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblGratuity.getRowCount();
        int rowSelected = tblGratuity.getSelectedRow();
        if(observable.getGAuthorizeStatus()!=null && (observable.getGAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getGAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteGratuityData(this.tblGratuity.getSelectedRow());
                this.updateTable();
                resetGratuityForm();
                observable.resetGratuityValues();
                enableDisableGratuity(false);
                btnGratuityNew.setEnabled(true);
                btnGratuitySave.setEnabled(false);
                btnGratuityDelete.setEnabled(false);
                HRASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnGratuityDeleteActionPerformed
    
    private void btnMDDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDDeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblMisecllaniousDeductions.getRowCount();
        int rowSelected = tblMisecllaniousDeductions.getSelectedRow();
        if(observable.getMDAuthorizeStatus()!=null && (observable.getMDAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getMDAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteMDData(this.tblMisecllaniousDeductions.getSelectedRow());
                this.updateTable();
                resetCCAForm();
                observable.resetMDValues();
                enableDisableMD(false);
                CCASave = false;
                btnMDNew.setEnabled(true);
                btnMDSave.setEnabled(false);
                btnMDDelete.setEnabled(false);
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnMDDeleteActionPerformed
    
    private void tblGratuityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGratuityMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationGratuity(tblGratuity.getSelectedRow());
            enableDisableGratuity(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblGratuity.getSelectedRow()>=0){
                updationGratuity(tblGratuity.getSelectedRow());
                enableDisableGratuity(false);
                //                txtHRAllowanceStartingAmtValue.setEnabled(true);
                //                txtHRAllowanceEndingAmtValue.setEnabled(true);
                if(observable.getGAuthorizeStatus()!=null && (observable.getGAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getGAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panGratuityAllowanceInfo,false);
                    btnGratuityNew.setEnabled(true);
                    btnGratuitySave.setEnabled(false);
                    btnGratuityDelete.setEnabled(false);
                    HRASave = false;
                }else{
                    enableDisableGratuity(true);
                    cboGratuityDesignation.setEnabled(false);
                    tdtGratuityFromDateValue.setEnabled(false);
                    tdtGratuityToDateValue.setEnabled(false);
                    btnGratuityNew.setEnabled(false);
                    btnGratuitySave.setEnabled(true);
                    btnGratuityDelete.setEnabled(true);
                    HRASave = true;
                    //                    _intHRANew = false;
                }
            }
            txtGratuityBeyondValue.setEnabled(false);
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblGratuityMouseClicked
    
    private void tblMisecllaniousDeductionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMisecllaniousDeductionsMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationMD(tblMisecllaniousDeductions.getSelectedRow());
            enableDisableMD(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblMisecllaniousDeductions.getSelectedRow()>=0){
                updationMD(tblMisecllaniousDeductions.getSelectedRow());
                enableDisableMD(false);
                //                txtMDStartingAmtValue.setEnabled(true);
                //                txtMDEndingAmtValue.setEnabled(true);
                if(observable.getMDAuthorizeStatus()!=null && (observable.getMDAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getMDAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panMisecllaniousDeductionInfo,false);
                    btnMDNew.setEnabled(true);
                    btnMDSave.setEnabled(false);
                    btnMDDelete.setEnabled(false);
                    CCASave = false;
                }else{
                    if(observable.getMdFixedOrPercentage().equals("FIXED")){
                        chkMdFixedValue.setSelected(true);
                        chkMdPercentageValue.setSelected(false);
                    }else if(observable.getMdFixedOrPercentage().equals("PERCENTAGE")){
                        chkMdFixedValue.setSelected(false);
                        chkMdPercentageValue.setSelected(true);
                    }
                    if(observable.getRdoUsingBasic().equals("O")){
                        rdoUsingBasic_Others.setSelected(true);
                        rdoUsingBasic_Basic.setSelected(false);
                        rdoUsingBasic_Gross.setSelected(false);
                    }else if(observable.getRdoUsingBasic().equals("B")){
                        rdoUsingBasic_Others.setSelected(false);
                        rdoUsingBasic_Basic.setSelected(true);
                        rdoUsingBasic_Gross.setSelected(false);
                    }
                    else if(observable.getRdoUsingBasic().equals("G")){
                        rdoUsingBasic_Others.setSelected(false);
                        rdoUsingBasic_Basic.setSelected(false);
                        rdoUsingBasic_Gross.setSelected(true);
                    }
                    if(rdoUsingBasic_Others.isSelected()== true){
                        rdoUsingBasic_OthersActionPerformed(null);
                    }else if(rdoUsingBasic_Basic.isSelected() == true){
                        rdoUsingBasic_BasicActionPerformed(null);
                    }
                    else if(rdoUsingBasic_Gross.isSelected() == true){
                        rdoUsingBasic_GrossActionPerformed(null);
                    }
                    if(chkMdFixedValue.isSelected() == true){
                        //                        chkMdFixedValueActionPerformed(null);
                        txtMdFixedAmtValue.setVisible(true);
                        txtMdFixedAmtValue.setEnabled(true);
                    }
                    else if(chkMdPercentageValue.isSelected() == true){
                        //                        chkMdPercentageValueActionPerformed(null);
                        txtMDEligiblePercentageValue.setEnabled(true);
                        txtMDEligiblePercentageValue.setVisible(true);
                    }
                    btnMDNew.setEnabled(false);
                    btnMDSave.setEnabled(true);
                    btnMDDelete.setEnabled(true);
                    CCASave = true;
                    _intMDNew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblMisecllaniousDeductionsMouseClicked
    private void enableDisableAllscreens(boolean allScreen){
        btnMDNew.setEnabled(allScreen);
        btnMDSave.setEnabled(allScreen);
        btnMDDelete.setEnabled(allScreen);
        btnHaltingNew.setEnabled(allScreen);
        btnHaltingSave.setEnabled(allScreen);
        btnHaltingDelete.setEnabled(allScreen);
        btnGratuityNew.setEnabled(allScreen);
        btnGratuitySave.setEnabled(allScreen);
        btnGratuityDelete.setEnabled(allScreen);
        
        
        lblMDSLNO.setVisible(allScreen);
        lblMDSLNOValue.setVisible(allScreen);
        lblMDSLNO.setVisible(allScreen);
        
        lblHaltingSLNO.setVisible(allScreen);
        lblHaltingSLNOValue.setVisible(allScreen);
    }    private void updationDA(int selectDARow) {
        //        observable.populateDA(selectDARow);
        populateDetail();
        //        _intDANew = false;
    }
    private void btnMDSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSaveActionPerformed
        // TODO add your handling code here:
        if(cboMisecllaniousDeduction.getSelectedIndex() == 0 || cboMisecllaniousDeduction.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(gradeAlert);
            //        }else if(CommonUtil.convertObjToStr(tdtMDFromDateValue.getDateValue()).length() == 0){
            //            ClientUtil.showAlertWindow(fromDateAlert);
            //        }else if(cboMDDeductionType.getSelectedIndex() == 0 || cboMDDeductionType.getSelectedIndex()<=0){
            //            ClientUtil.showAlertWindow("Deduction Type should not be empty");
            //        }else if(CommonUtil.convertObjToDouble(txtMDMaximumAmtValue.getText()).doubleValue() == 0){
            //            C lientUtil.showAlertWindow("Maximum amount should not be empty");
            //        }else if(CommonUtil.convertObjToDouble(txtMDPercentageValue.getText()).doubleValue() == 0){
            //            ClientUtil.showAlertWindow("percentage Value should not be empty");
            //        }else if(cboMDEligibleAllowances.getSelectedIndex() == 0 && cboMDEligibleAllowances.getSelectedIndex()<=0){
            //            ClientUtil.showAlertWindow("Increments amount should not be empty");
            //        }else if(txtMDEligiblePercentageValue.getText().length() == 0){
            //            ClientUtil.showAlertWindow("This value already exists please choose another value");
            //            return;
        }else{
            long CCACboSize = 1;
            if(this._intMDNew == true && tblMisecllaniousDeductions.getRowCount()>0){
                for (int i = 0;i<tblMisecllaniousDeductions.getRowCount();i++){
                    if(chkMdPercentageValue.isSelected()==true){
                        if(cboMDEligibleAllowances.getSelectedItem().toString().toUpperCase().equals(tblMisecllaniousDeductions.getValueAt(i, 2))){
                            ClientUtil.showAlertWindow("This value already exists please choose another value");
                            return;
                        }else{
                            CCACboSize = CCACboSize + 1;
                        }
                    }
                }
            }
            updateOBFields();
            if(!this._intMDNew){
                observable.insertMDData(this.tblMisecllaniousDeductions.getSelectedRow());
            }else{
                observable.insertMDData(-1);
            }
            resetCCAForm();
            enableDisableMD(false);
            btnMDNew.setEnabled(true);
            btnMDSave.setEnabled(false);
            btnMDDelete.setEnabled(false);
            cboMisecllaniousDeduction.setEnabled(false);
            tdtMDFromDateValue.setEnabled(false);
            tdtMDToDateValue.setEnabled(false);
            System.out.print("else condition:");
            this.updateTable();
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo,false);
            this._intMDNew = false;
            CCASave = false;
            //            }
        }
    }//GEN-LAST:event_btnMDSaveActionPerformed
    private void updationTA(int selectDARow) {
        //        observable.populateTA(selectDARow);
        populateDetail();
        _intDANew = false;
    }
    private void btnGratuitySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGratuitySaveActionPerformed
        // TODO add your handling code here:
        if(cboGratuityDesignation.getSelectedIndex() == 0 || cboGratuityDesignation.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(gradeAlert);
        }else if(CommonUtil.convertObjToStr(tdtGratuityFromDateValue.getDateValue()).length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(CommonUtil.convertObjToDouble(txtGratuityUptoValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Scale Ending amount should not be empty");
        }else if(CommonUtil.convertObjToDouble(txtGratuityUptoServiceValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Increments amount should not be empty");
        }else if(CommonUtil.convertObjToDouble(txtGratuityUptoServiceValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Quarters Provided whether HRA Payable Should be select either yes or no");
        }else if(CommonUtil.convertObjToDouble(txtGratuityMaximumOfValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Quarters Provided whether HRA Payable Should be select either yes or no");
        }else if(CommonUtil.convertObjToDouble(txtGratuityBeyondValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Increments amount should not be empty");
        }else if(CommonUtil.convertObjToDouble(txtGratuityBeyondServiceValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Quarters Provided whether HRA Payable Should be select either yes or no");
        }else if(CommonUtil.convertObjToDouble(txtGratuityMaximumAmtBeyongValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Quarters Provided whether HRA Payable Should be select either yes or no");
        }else{
            long HRACboSize = 1;
            
            updateOBFields();
            //            int selectedRow = tblHRAllowance.getSelectedRow();
            if(!this._intGNew){
                observable.insertGratuityData(this.tblGratuity.getSelectedRow());
            }else{
                observable.insertGratuityData(-1);
            }
            resetGratuityForm();
            btnGratuityNew.setEnabled(true);
            btnGratuitySave.setEnabled(false);
            btnGratuityDelete.setEnabled(false);
            System.out.print("else condition:");
            this.updateTable();
            enableDisableGratuity(false);
            cboGratuityDesignation.setEnabled(false);
            tdtGratuityFromDateValue.setEnabled(false);
            tdtGratuityToDateValue.setEnabled(false);
            this._intGNew = false;
            HRASave = false;
            //            }
        }
    }//GEN-LAST:event_btnGratuitySaveActionPerformed
    private void enableDisableGratuity(boolean HRAVal){
        cboGratuityDesignation.setEnabled(HRAVal);
        tdtGratuityFromDateValue.setEnabled(HRAVal);
        tdtGratuityToDateValue.setEnabled(HRAVal);
        txtGratuityUptoValue.setEnabled(HRAVal);
        txtGratuityUptoServiceValue.setEnabled(HRAVal);
        txtGratuityMaximumOfValue.setEnabled(HRAVal);
        txtGratuityBeyondValue.setEnabled(HRAVal);
        txtGratuityBeyondServiceValue.setEnabled(HRAVal);
        txtGratuityMaximumAmtBeyongValue.setEnabled(HRAVal);
    }   
    private void updationGratuity(int selectDARow) {
        observable.populateGratuity(selectDARow);
        populateDetail();
        _intDANew = false;
    }
    
    private void btnGratuityNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGratuityNewActionPerformed
        // TODO add your handling code here:
        _intGNew = true;
        if(tblGratuity.getRowCount() == 0){
            cboGratuityDesignation.setEnabled(true);
            tdtGratuityFromDateValue.setEnabled(true);
            tdtGratuityToDateValue.setEnabled(true);
            enableDisableGratuity(true);
        }else if(tblGratuity.getRowCount() >0){
            updationGratuity(tblGratuity.getRowCount()-1);
            cboGratuityDesignation.setEnabled(false);
            tdtGratuityFromDateValue.setEnabled(false);
            tdtGratuityToDateValue.setEnabled(false);
            enableDisableGratuity(true);
            resetGratuityForm();;
        }
        btnGratuityDelete.setEnabled(false);
        btnGratuitySave.setEnabled(true);
        btnGratuityNew.setEnabled(false);
        
    }//GEN-LAST:event_btnGratuityNewActionPerformed
    private void updationMD(int selectDARow) {
        observable.populateMD(selectDARow);
        populateDetail();
        //        _intDANew = false;
    }
    private void btnMDNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDNewActionPerformed
        // TODO add your handling code here:
        _intMDNew = true;
        if(tblMisecllaniousDeductions.getRowCount() == 0){
            enableDisableMD(true);
            ClientUtil.enableDisable(panUsinBasic,true);
        }else if(tblMisecllaniousDeductions.getRowCount() >0){
            updationMD(tblMisecllaniousDeductions.getRowCount()-1);
            enableDisableMD(false);
            txtMDEligiblePercentageValue.setEnabled(true);
            cboMDEligibleAllowances.setEnabled(true);
            resetMDForm();
            if(observable.getRdoUsingBasic().equals("G") || observable.getRdoUsingBasic().equals("B")){
                txtFromAmount.setText("");
                txtToAmount.setText("");
                txtMdFixedAmtValue.setText("");
                txtMdFixedAmtValue.setEnabled(true);
                double fromAmt = CommonUtil.convertObjToDouble(observable.getTxtToAmount()).doubleValue();
                fromAmt = fromAmt + 1;
                System.out.println("@#$@#$fromAmt"+fromAmt);
                txtFromAmount.setText(String.valueOf(fromAmt));
                txtFromAmount.setEnabled(true);
                txtToAmount.setEnabled(true);
                ClientUtil.enableDisable(panMdType,true);
                
                
            }
        }
        btnMDDelete.setEnabled(false);
        btnMDSave.setEnabled(true);
        btnMDNew.setEnabled(false);
        
    }//GEN-LAST:event_btnMDNewActionPerformed
    private void enableDisableMD(boolean MDVal){
        cboMisecllaniousDeduction.setEnabled(MDVal);
        tdtMDFromDateValue.setEnabled(MDVal);
        tdtMDToDateValue.setEnabled(MDVal);
        cboMDDeductionType.setEnabled(MDVal);
        txtMDMaximumAmtValue.setEnabled(MDVal);
        txtMdFixedAmtValue.setEnabled(MDVal);
        txtMDPercentageValue.setEnabled(MDVal);
        cboMDEligibleAllowances.setEnabled(MDVal);
        txtMDEligiblePercentageValue.setEnabled(MDVal);
        ClientUtil.enableDisable(panMdType,MDVal);
        ClientUtil.enableDisable(panUsinBasic,MDVal);
        chkMdFixedValue.setEnabled(MDVal);
        chkMdPercentageValue.setEnabled(MDVal);
        txtMdFixedAmtValue.setEnabled(MDVal);
        txtFromAmount.setEnabled(MDVal);
        txtToAmount.setEnabled(MDVal);
    }       
    private void enableDisableButtons(boolean value){
        cboHaltingDesignationValue.setEnabled(value);
        tdtHaltingFromDateValue.setEnabled(value);
        tdtHaltingToDateValue.setEnabled(value);
        cboHaltingAllowanceTypeValue.setEnabled(value);
        cboHaltingParameterBasedOnValue.setEnabled(value);
        cboHaltingSubParameterValue.setEnabled(value);
        txtHaltingFixedAmtValue.setEnabled(value);
        txtHaltingMaximumOfValue.setEnabled(value);
        txtHaltingPercentageValue.setEnabled(value);
    }   
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(panHaltingAllowanceInfo.isShowing()==true){
            panEditDelete = HALTING_ALLOWANCE;
        }
        else if(panMisecllaniousDeductionInfo.isShowing()==true){
            panEditDelete = MSCL_DEDUCTION;
        }
        else if(panGratuityAllowanceInfo.isShowing()==true){
            panEditDelete = GRATUITY;
        }
        callView(ClientConstants.ACTIONTYPE_VIEW,panEditDelete);
        btnCheck();
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void updateOBFields(){
        observable.setCboHaltingAllowanceTypeValue((String)((ComboBoxModel)this.cboHaltingAllowanceTypeValue.getModel()).getKeyForSelected());
        observable.setCboHaltingDesignationValue((String)((ComboBoxModel)this.cboHaltingDesignationValue.getModel()).getKeyForSelected());
        observable.setCboHaltingParameterBasedOnValue((String)((ComboBoxModel)this.cboHaltingParameterBasedOnValue.getModel()).getKeyForSelected());
        observable.setCboHaltingSubParameterValue((String)((ComboBoxModel)this.cboHaltingSubParameterValue.getModel()).getKeyForSelected());
        observable.setChkFixedType(chkHaltingFixedValue.isSelected());
        observable.setChkPercentageType(chkHaltingPercentageValue.isSelected());
        observable.setTdtHaltingFromDateValue(tdtHaltingFromDateValue.getDateValue());
        observable.setTdtHaltingToDateValue(tdtHaltingToDateValue.getDateValue());
        observable.setTxtHaltingFixedAmtValue(txtHaltingFixedAmtValue.getText());
        observable.setTxtHaltingMaximumOfValue(txtHaltingMaximumOfValue.getText());
        observable.setTxtHaltingPercentageValue(txtHaltingPercentageValue.getText());
        observable.setCboMDCityType((String)((ComboBoxModel)this.cboMDDeductionType.getModel()).getKeyForSelected());
        observable.setCboMDEligibleAllowances((String)((ComboBoxModel)this.cboMDEligibleAllowances.getModel()).getKeyForSelected());
        observable.setCboMisecllaniousDeduction((String)((ComboBoxModel)this.cboMisecllaniousDeduction.getModel()).getKeyForSelected());
        observable.setTdtMDFromDateValue(tdtMDFromDateValue.getDateValue());
        observable.setTdtMDToDateValue(tdtMDToDateValue.getDateValue());
        observable.setTxtMDBasedAmtValue(txtMDPercentageValue.getText());
        observable.setTxtMDMaximumAmtValue(txtMDMaximumAmtValue.getText());
        observable.setTxtMdFixedAmtValue(txtMdFixedAmtValue.getText());
        observable.setTxtFromAmount(txtFromAmount.getText());
        observable.setTxtToAmount(txtToAmount.getText());
        if(rdoUsingBasic_Basic.isSelected()){
            observable.setRdoUsingBasic("B");
        }else if(rdoUsingBasic_Others.isSelected()){
            observable.setRdoUsingBasic("O");
        }
        else if(rdoUsingBasic_Gross.isSelected()){
            observable.setRdoUsingBasic("G");
        }
        
        if(chkMdFixedValue.isSelected() == true){
            observable.setMdFixedOrPercentage("FIXED");
        }
        else if(chkMdPercentageValue.isSelected() == true){
            observable.setMdFixedOrPercentage("PERCENTAGE");
        }
        observable.setTxtMDEligiblePercentageValue(txtMDEligiblePercentageValue.getText());
        observable.setCboGratuityDesignation((String)((ComboBoxModel)this.cboGratuityDesignation.getModel()).getKeyForSelected());
        observable.setTdtGratuityFromDateValue(tdtGratuityFromDateValue.getDateValue());
        observable.setTdtGratuityToDateValue(tdtGratuityToDateValue.getDateValue());
        observable.setTxtGratuityUptoValue(txtGratuityUptoValue.getText());
        observable.setTxtGratuityUptoServiceValue(txtGratuityUptoServiceValue.getText());
        observable.setTxtGratuityMaximumOfValue(txtGratuityMaximumOfValue.getText());
        observable.setTxtGratuityBeyondValue(txtGratuityBeyondValue.getText());
        observable.setTxtGratuityBeyondServiceValue(txtGratuityBeyondServiceValue.getText());
        observable.setTxtGratuityMaximumAmtBeyongValue(txtGratuityMaximumAmtBeyongValue.getText());
    }
    private void whenTableRowSelected(HashMap paramMap) {
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("LIENNO"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
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
            String data = getLockDetails(lockedBy, getScreenID()) ;
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            btnSave.setEnabled(false);
            //                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
            
        }
        
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    private void updationLien() {
        int selectRow = -1;
//        selectRow = tblSalaryStructure.getSelectedRow();
        //        observable.populateSalaryStructure(selectRow);
        populateDetail();
        _intSalaryNew = false;
    }
    
    private void populateDetail(){
        ((ComboBoxModel)this.cboHaltingDesignationValue.getModel()).setKeyForSelected(observable.getCboHaltingDesignationValue());
        ((ComboBoxModel)this.cboHaltingAllowanceTypeValue.getModel()).setKeyForSelected(observable.getCboHaltingAllowanceTypeValue());
        ((ComboBoxModel)this.cboHaltingParameterBasedOnValue.getModel()).setKeyForSelected(observable.getCboHaltingParameterBasedOnValue());
        ((ComboBoxModel)this.cboHaltingSubParameterValue.getModel()).setKeyForSelected(observable.getCboHaltingSubParameterValue());
        chkHaltingFixedValue.setSelected(observable.getChkFixedType());
        txtHaltingFixedAmtValue.setText(observable.getTxtHaltingFixedAmtValue());
        chkHaltingPercentageValue.setSelected(observable.getChkPercentageType());
        tdtHaltingFromDateValue.setDateValue(observable.getTdtHaltingFromDateValue());
        tdtHaltingToDateValue.setDateValue(observable.getTdtHaltingToDateValue());
        txtHaltingMaximumOfValue.setText(observable.getTxtHaltingMaximumOfValue());
        txtHaltingPercentageValue.setText(observable.getTxtHaltingPercentageValue());
        
        
        ((ComboBoxModel)this.cboMisecllaniousDeduction.getModel()).setKeyForSelected(observable.getCboMisecllaniousDeduction());
        ((ComboBoxModel)this.cboMDDeductionType.getModel()).setKeyForSelected(observable.getCboMDCityType());
        ((ComboBoxModel)this.cboMDEligibleAllowances.getModel()).setKeyForSelected(observable.getCboMDEligibleAllowances());
        tdtMDFromDateValue.setDateValue(observable.getTdtMDFromDateValue());
        tdtMDToDateValue.setDateValue(observable.getTdtMDToDateValue());
        txtMDPercentageValue.setText(observable.getTxtMDBasedAmtValue());
        txtMDMaximumAmtValue.setText(observable.getTxtMDMaximumAmtValue());
        txtMdFixedAmtValue.setText(observable.getTxtMdFixedAmtValue());
        txtFromAmount.setText(observable.getTxtFromAmount());
        txtToAmount.setText(observable.getTxtToAmount());
        if(CommonUtil.convertObjToStr(observable.getRdoUsingBasic()).equals("B")){
            rdoUsingBasic_Basic.setSelected(true);
            rdoUsingBasic_BasicActionPerformed(null);
            txtFromAmount.setVisible(true);
            lblFromAmount.setVisible(true);
            txtToAmount.setVisible(true);
            lblToAmount.setVisible(true);
        }else if(CommonUtil.convertObjToStr(observable.getRdoUsingBasic()).equals("O")){
            rdoUsingBasic_Others.setSelected(true);
            rdoUsingBasic_OthersActionPerformed(null);
            txtFromAmount.setVisible(false);
            lblFromAmount.setVisible(false);
            txtToAmount.setVisible(false);
            lblToAmount.setVisible(false);
        }else if(CommonUtil.convertObjToStr(observable.getRdoUsingBasic()).equals("G")){
            rdoUsingBasic_Gross.setSelected(true);
            rdoUsingBasic_GrossActionPerformed(null);
            txtFromAmount.setVisible(true);
            lblFromAmount.setVisible(true);
            txtToAmount.setVisible(true);
            lblToAmount.setVisible(true);
        }
        if(CommonUtil.convertObjToStr(observable.getMdFixedOrPercentage()).equals("FIXED")){
            chkMdFixedValue.setSelected(true);
            chkMdPercentageValue.setSelected(false);
            txtMdFixedAmtValue.setVisible(true);
            lblMdFixedAmt.setVisible(true);
            lblMDMaximumOf.setVisible(false);
            txtMDMaximumAmtValue.setVisible(false);
            lblMDPercentage.setVisible(false);
            txtMDPercentageValue.setVisible(false);
            lblMDEligibleAllowances.setVisible(false);
            cboMDEligibleAllowances.setVisible(false);
            lblMDEligiblePercentage.setVisible(false);
            txtMDEligiblePercentageValue.setVisible(false);
        }else if(CommonUtil.convertObjToStr(observable.getMdFixedOrPercentage()).equals("PERCENTAGE")){
            chkMdPercentageValue.setSelected(true);
            chkMdFixedValue.setSelected(false);
            txtMdFixedAmtValue.setVisible(false);
            lblMdFixedAmt.setVisible(false);
            lblMDMaximumOf.setVisible(true);
            txtMDMaximumAmtValue.setVisible(true);
            lblMDPercentage.setVisible(true);
            txtMDPercentageValue.setVisible(true);
            lblMDEligibleAllowances.setVisible(true);
            cboMDEligibleAllowances.setVisible(true);
            lblMDEligiblePercentage.setVisible(true);
            txtMDEligiblePercentageValue.setVisible(true);
        }
        txtMDEligiblePercentageValue.setText(observable.getTxtMDEligiblePercentageValue());
        ((ComboBoxModel)this.cboGratuityDesignation.getModel()).setKeyForSelected(observable.getCboGratuityDesignation());
        tdtGratuityFromDateValue.setDateValue(observable.getTdtGratuityFromDateValue());
        tdtGratuityToDateValue.setDateValue(observable.getTdtGratuityToDateValue());
        txtGratuityUptoValue.setText(observable.getTxtGratuityUptoValue());
        txtGratuityUptoServiceValue.setText(observable.getTxtGratuityUptoServiceValue());
        txtGratuityMaximumOfValue.setText(observable.getTxtGratuityMaximumOfValue());
        txtGratuityBeyondValue.setText(observable.getTxtGratuityBeyondValue());
        txtGratuityBeyondServiceValue.setText(observable.getTxtGratuityBeyondServiceValue());
        txtGratuityMaximumAmtBeyongValue.setText(observable.getTxtGratuityMaximumAmtBeyongValue());
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void resetGratuityForm(){
        txtGratuityUptoValue.setText("");
        txtGratuityUptoServiceValue.setText("");
        txtGratuityMaximumOfValue.setText("");
        txtGratuityBeyondValue.setText("");
        txtGratuityBeyondServiceValue.setText("");
        txtGratuityMaximumAmtBeyongValue.setText("");
    }
    private void resetMDForm(){
        txtMDEligiblePercentageValue.setText("");
    }
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        //        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void resetCCAForm(){
        //        cboCCAllowanceCityType.setSelectedItem("");
        //        cboMisecllaniousDeduction.setSelectedItem("");
        //        tdtCCAllowanceFromDateValue.setDateValue("");
        tdtMDToDateValue.setDateValue("");
        //        txtMDStartingAmtValue.setText("");
        //        txtMDEndingAmtValue.setText("");
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        observable.setStatus();
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_REJECT);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(true);
        btnPrint.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    private void updationNewButton(int selectRow) {
        //        int selectRow = -1;
        //        selectRow = tblLien.getSelectedRow();
        //        observable.populateSalaryStructure(selectRow);
        populateDetail();
        //        enableDisableLienInfo();
        //        _intSalaryNew = false;
        //        observable.setLienStatus(CommonConstants.STATUS_MODIFIED);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnPrint.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(int actionType){
        observable.setActionType(actionType);
        observable.setStatus();
        viewType = actionType;
        String authorizeStatus = "";
        String status = "";
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
            status = CommonConstants.STATUS_CREATED;
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            authorizeStatus = CommonConstants.STATUS_REJECTED;
            status = CommonConstants.STATUS_CREATED;
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            authorizeStatus = CommonConstants.STATUS_DELETED;
            status = CommonConstants.STATUS_DELETED;
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        HashMap mapParam = new HashMap();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) && isFilled){
            String isAllTabsVisited = tabMisecllaniousDeductions.isAllTabsVisited();
            
            //--- If all the tabs are not visited, then show the Message
            //            if(isAllTabsVisited.length()>0){
            //                ClientUtil.displayAlert(isAllTabsVisited);
            //                return;
            //            }else{
            tabMisecllaniousDeductions.resetVisits();
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("STATUS",status);
            authorizeMap.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
            authorizeMap.put("AUTHORIZE_DATE",curDate.clone());
            authorizeMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            authorizeMap.put("AUTHORIZE_STATUS",authorizeStatus);
            if(panHaltingAllowanceInfo.isShowing()==true){
                panEditDelete = HALTING_ALLOWANCE;
                authorizeMap.put("TEMP_SL_NO", observable.getLblHATempSLNoValue());
                authorizeMap.put("GRADE",observable.getCboHaltingDesignationValue());
                ClientUtil.execute("updateAuthorizeStatusHalting",authorizeMap);
                btnHaltingNew.setEnabled(true);
            }
            else if(panMisecllaniousDeductionInfo.isShowing()==true){
                panEditDelete = MSCL_DEDUCTION;
                authorizeMap.put("TEMP_SL_NO", observable.getLblMDTempSLNoValue());
                authorizeMap.put("GRADE",observable.getCboMisecllaniousDeduction());
                authorizeMap.put("MD_DEDUCTION_TYPE",CommonUtil.convertObjToStr(observable.getCboMDCityType()));
                System.out.println("@#$@#$#@authorizeMap:"+authorizeMap);
                ClientUtil.execute("updateAuthorizeStatusMisecllanious",authorizeMap);
                btnMDNew.setEnabled(true);
            }
            else if(panGratuityAllowanceInfo.isShowing()==true){
                panEditDelete = GRATUITY;
                authorizeMap.put("TEMP_SL_NO", observable.getLblGATempSLNoValue());
                authorizeMap.put("GRADE",observable.getCboGratuityDesignation());
                ClientUtil.execute("updateAuthorizeStatusGratuity",authorizeMap);
                btnGratuityNew.setEnabled(true);
            }
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatus.setText(authorizeStatus);
            isFilled = false;
            //            }
        }else{
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if(panHaltingAllowanceInfo.isShowing()==true){
                panEditDelete = HALTING_ALLOWANCE;
                mapParam.put(CommonConstants.MAP_NAME, "getHaltingAllowanceAuthMode");
                btnHaltingNew.setEnabled(true);
            }
            else if(panMisecllaniousDeductionInfo.isShowing()==true){
                panEditDelete = MSCL_DEDUCTION;
                mapParam.put(CommonConstants.MAP_NAME, "getMiscelleniousDeductAuthMode");
                btnMDNew.setEnabled(true);
            }
            else if(panGratuityAllowanceInfo.isShowing()==true){
                panEditDelete = GRATUITY;
                mapParam.put(CommonConstants.MAP_NAME, "getGratuityAuthMode");
                btnMDNew.setEnabled(true);
            }
            
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //            isFilled = true;
        }
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //        double endingAmt = 0.0;
        ////        double tableValue = 0.0;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            double totalTableAmt = 0.0;
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(salaryStructureSave == true){
                    ClientUtil.showAlertWindow("SalaryStructure Details not saved");
                    return;
                }else if(DASave == true){
                    ClientUtil.showAlertWindow("DA Details not saved");
                    return;
                }else if(CCASave == true){
                    ClientUtil.showAlertWindow("Miscellanious deduction Details not saved");
                    return;
                }else if(HRASave == true){
                    ClientUtil.showAlertWindow("HRA Details not saved");
                    return;
                }else if(TASave == true){
                    ClientUtil.showAlertWindow("TA Details not saved");
                    return;
                }else if(MASave == true){
                    ClientUtil.showAlertWindow("MA Details not saved");
                    return;
                }
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                
            }
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            //__ Make the Screen Closable..
            setModified(false);
        }else{
            System.out.print("else condition:");
            ClientUtil.showAlertWindow("Entered Amount is not Matching");
            return;
        }
        //            }
        //        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        resetCCAForm();
        resetGratuityForm();
        enableDisableButtons(false);
        enableDisableMD(false);
        enableDisableGratuity(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        btnDelete.setEnabled(true);
        btnPrint.setEnabled(true);
        enableDisableAllscreens(false);
        isFilled = false;
        DASave = false;
        CCASave = false;
        HRASave = false;
        TASave = false;
        MASave = false;
        _intSalaryNew = false;
        _intDANew = false;
        _intMDNew = false;
        _intGNew = false;
        _intTANew = false;
        _intMANew = false;
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateTable(){
        this.tblHaltingAllowances.setModel(observable.getTbmHalting());
        this.tblHaltingAllowances.revalidate();
        
        this.tblMisecllaniousDeductions.setModel(observable.getTbmMisecllaniousDeduction());
        this.tblMisecllaniousDeductions.revalidate();
        
        this.tblGratuity.setModel(observable.getTbmGratuity());
        this.tblGratuity.revalidate();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setUp(ClientConstants.ACTIONTYPE_DELETE, false);
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_DELETE);
        btnSave.setEnabled(false);
        btnPrint.setEnabled(false);
        btnMDNew.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if(panHaltingAllowanceInfo.isShowing()==true){
            panEditDelete = HALTING_ALLOWANCE;
            btnHaltingNew.setEnabled(true);
        }
        else if(panMisecllaniousDeductionInfo.isShowing()==true){
            panEditDelete = MSCL_DEDUCTION;
            btnMDNew.setEnabled(true);
        }
        else if(panGratuityAllowanceInfo.isShowing()==true){
            panEditDelete = GRATUITY;
            btnMDNew.setEnabled(true);
        }
        resetUI(panEditDelete);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(ClientConstants.ACTIONTYPE_EDIT,panEditDelete);
        btnDelete.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    private void panHaltingAllowanceEnable(){
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo, false);
            ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
            btnHaltingNew.setEnabled(true);
        }
        else
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo, false);
        ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
        
    }
    private void panMsclDeductionEnable(){
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panHaltingAllowanceInfo, false);
            ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
            
            btnMDNew.setEnabled(true);
        }
        else
            ClientUtil.enableDisable(panHaltingAllowanceInfo, false);
        ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
        
    }
    private void panGratuityEnable(){
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panHaltingAllowanceInfo, false);
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo, false);
            btnMDNew.setEnabled(true);
        }
        else
            ClientUtil.enableDisable(panHaltingAllowanceInfo, false);
        ClientUtil.enableDisable(panMisecllaniousDeductionInfo, false);
        
    }
    private void resetUI(int value){
        //observable.resetTable();
        if(value==HALTING_ALLOWANCE){
            System.out.println("reset Halting Allowance");
            observable.resetHA();
            ClientUtil.clearAll(panHaltingAllowanceInfo);
            ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo,false);
            btnHaltingNew.setEnabled(true);
            btnHaltingSave.setEnabled(false);
            btnHaltingDelete.setEnabled(false);
            btnGratuityDelete.setEnabled(false);
            btnGratuitySave.setEnabled(false);
            btnGratuityNew.setEnabled(false);
        }
        else if(value == MSCL_DEDUCTION){
            System.out.println("reset MSCl deduction");
            observable.resetMDValues();
            ClientUtil.clearAll(panMisecllaniousDeductionInfo);
            ClientUtil.enableDisable(panGratuityAllowanceInfo, false);
            ClientUtil.enableDisable(panHaltingAllowanceInfo,false);
            btnHaltingNew.setEnabled(false);
            btnHaltingSave.setEnabled(false);
            btnHaltingDelete.setEnabled(false);
            btnGratuityDelete.setEnabled(false);
            btnGratuitySave.setEnabled(false);
            btnGratuityNew.setEnabled(false);
        }
        else if(value == GRATUITY){
            System.out.println("reset Gratuity");
            observable.resetGratuityValues();
            ClientUtil.clearAll(panGratuityAllowanceInfo);
            ClientUtil.enableDisable(panMisecllaniousDeductionInfo, false);
            ClientUtil.enableDisable(panHaltingAllowanceInfo,false);
            btnHaltingNew.setEnabled(false);
            btnHaltingSave.setEnabled(false);
            btnHaltingDelete.setEnabled(false);
            btnGratuityDelete.setEnabled(false);
            btnGratuitySave.setEnabled(false);
            btnGratuityNew.setEnabled(true);
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        if(panHaltingAllowances.isShowing()==true){
            this._intHANew = true;
            panHaltingAllowanceEnable();    // To enable the Halting allowance panel...
            pan=HALTING_ALLOWANCE;
        }
        else if(panMisecllaniousDeductionsTab.isShowing()==true){
            panMsclDeductionEnable();   //To Enable the misc deduction Panel
            pan = MSCL_DEDUCTION;
            this._intMDNew = true;
        }
        else if(panGratuityAllowance.isShowing()==true){
            panGratuityEnable();   //To Enable the gratuity allowace Panel
            pan = GRATUITY;
            this._intGNew = true;
        }
        resetUI(pan);
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        //        //__ To Save the data in the Internal Frame...
        setModified(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
    private void allScreensDisable(boolean allDisable){
        ClientUtil.enableDisable(panMisecllaniousDeductionInfo,allDisable);
        ClientUtil.enableDisable(panGratuityAllowanceInfo,allDisable);
    }
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
    private void callView(int viewType,int panEditDelete){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        if (viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_VIEW ||
        viewType==ClientConstants.ACTIONTYPE_DELETE || viewType==ClientConstants.ACTIONTYPE_VIEW){
            if(panEditDelete == HALTING_ALLOWANCE){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectHaltingEditMode");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }else if(panEditDelete == MSCL_DEDUCTION){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectMiscDeductionEditMode");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }else if(panEditDelete == GRATUITY){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectGratuityEditMode");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }
            
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void setViewType(int vuType) {
        viewType = vuType;
        observable.setActionType(viewType);
        observable.setStatus();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            HashMap returnMap = null;
            isFilled = true;
            //            tabSalaryStructure.setSelectedComponent(panSalaryDetails);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                if(panHaltingAllowances.isShowing() == true){
                    panEditDelete = HALTING_ALLOWANCE;
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("HALTING_GRADE")),panEditDelete);
                }else if(panMisecllaniousDeductionsTab.isShowing()== true){
                    panEditDelete = MSCL_DEDUCTION;
                    observable.setCboMDCityType(String.valueOf(hashMap.get("MD_DEDUCTION_TYPE")));
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("MD_GRADE")),panEditDelete);
                    //                    observable.setCboMisecllaniousDeduction(CommonUtil.convertObjToStr(hashMap.get("MD_GRADE")));
                    System.out.println("@#$@#$observable.getCboMisecllaniousDeduction:"+observable.getCboMisecllaniousDeduction());
                }else if(panGratuityAllowance.isShowing()== true){
                    panEditDelete = GRATUITY;
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("GRATUITY_GRADE")),panEditDelete);
                }
                ClientUtil.enableDisable(this,true);
                this.setButtonEnableDisable();
                
                ClientUtil.enableDisable(this.panMisecllaniousDeductionInfo,false);
                ClientUtil.enableDisable(this.panGratuityAllowanceInfo,false);
                
                ClientUtil.enableDisable(this.panHaltingAllowanceInfo,false);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                ClientUtil.enableDisable(this,false);
                this.setButtonEnableDisable(); 
                if(panHaltingAllowances.isShowing() == true){ 
                    panEditDelete = HALTING_ALLOWANCE;
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("HALTING_GRADE")),panEditDelete);
                }else if(panMisecllaniousDeductionsTab.isShowing()== true){
                    panEditDelete = MSCL_DEDUCTION;
                    observable.setCboMDCityType(String.valueOf(hashMap.get("MD_DEDUCTION_TYPE")));
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("MD_GRADE")),panEditDelete);
                }else if(panGratuityAllowance.isShowing()== true){
                    panEditDelete = GRATUITY;
                    observable.getSalaryStructureData(String.valueOf(hashMap.get("GRATUITY_GRADE")),panEditDelete);
                }
                //                observable.getSalaryStructureData(String.valueOf(hashMap.get("HALTING_GRADE")),panEditDelete);
            }
            hashMap = null;
            returnMap = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
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
    }
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void update(Observable o, Object arg) {
        removeRadioButton();
        this.lblStatus.setText(observable.getLblStatus());
        this.cboHaltingDesignationValue.setModel(observable.getCbmHaltingDesignationValue());
        this.cboHaltingAllowanceTypeValue.setModel(observable.getCbmHaltingAllowanceTypeValue());
        this.cboHaltingParameterBasedOnValue.setModel(observable.getCbmHaltingParameterBasedOnValue());
        this.cboHaltingSubParameterValue.setModel(observable.getCbmHaltingSubParameterValue());
        this.cboMisecllaniousDeduction.setModel(observable.getCbmMisecllaniousDeduction());
        this.cboMDDeductionType.setModel(observable.getCbmMDCityType());
        cboMDEligibleAllowances.setModel(observable.getCbmMDEligibleAllowances());
        this.cboGratuityDesignation.setModel(observable.getCbmGratuityDesignation());
        this.updateTable();
        this.populateDetail();
        addRadioButton();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGratuityDelete;
    private com.see.truetransact.uicomponent.CButton btnGratuityNew;
    private com.see.truetransact.uicomponent.CButton btnGratuitySave;
    private com.see.truetransact.uicomponent.CButton btnHaltingDelete;
    private com.see.truetransact.uicomponent.CButton btnHaltingNew;
    private com.see.truetransact.uicomponent.CButton btnHaltingSave;
    private com.see.truetransact.uicomponent.CButton btnMDDelete;
    private com.see.truetransact.uicomponent.CButton btnMDNew;
    private com.see.truetransact.uicomponent.CButton btnMDSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboGratuityCityType;
    private com.see.truetransact.uicomponent.CComboBox cboGratuityDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboHaltingAllowanceTypeValue;
    private com.see.truetransact.uicomponent.CComboBox cboHaltingDesignationValue;
    private com.see.truetransact.uicomponent.CComboBox cboHaltingParameterBasedOnValue;
    private com.see.truetransact.uicomponent.CComboBox cboHaltingSubParameterValue;
    private com.see.truetransact.uicomponent.CComboBox cboMDDeductionType;
    private com.see.truetransact.uicomponent.CComboBox cboMDEligibleAllowances;
    private com.see.truetransact.uicomponent.CComboBox cboMisecllaniousDeduction;
    private com.see.truetransact.uicomponent.CCheckBox chkHaltingFixedValue;
    private com.see.truetransact.uicomponent.CCheckBox chkHaltingPercentageValue;
    private com.see.truetransact.uicomponent.CCheckBox chkMdFixedValue;
    private com.see.truetransact.uicomponent.CCheckBox chkMdPercentageValue;
    private com.see.truetransact.uicomponent.CLabel lblCompletedYearOfService;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblGratuityBeyond;
    private com.see.truetransact.uicomponent.CLabel lblGratuityBeyondMonthPay;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCityType;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCityType1;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCityType2;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCityType3;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCityType4;
    private com.see.truetransact.uicomponent.CLabel lblGratuityCompleted;
    private com.see.truetransact.uicomponent.CLabel lblGratuityDesignation;
    private com.see.truetransact.uicomponent.CLabel lblGratuityFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGratuityMaximumAmtBeyond;
    private com.see.truetransact.uicomponent.CLabel lblGratuityMaximumOf;
    private com.see.truetransact.uicomponent.CLabel lblGratuityMonthPay;
    private com.see.truetransact.uicomponent.CLabel lblGratuityMonths;
    private com.see.truetransact.uicomponent.CLabel lblGratuitySLNO;
    private com.see.truetransact.uicomponent.CLabel lblGratuitySLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblGratuityToDate;
    private com.see.truetransact.uicomponent.CLabel lblGratuityUpto;
    private com.see.truetransact.uicomponent.CLabel lblGratuityUptoMonths;
    private com.see.truetransact.uicomponent.CLabel lblGratuityYearofService;
    private com.see.truetransact.uicomponent.CLabel lblHaltingDesignation;
    private com.see.truetransact.uicomponent.CLabel lblHaltingFixedAmt;
    private com.see.truetransact.uicomponent.CLabel lblHaltingFromDate;
    private com.see.truetransact.uicomponent.CLabel lblHaltingMaximumOf;
    private com.see.truetransact.uicomponent.CLabel lblHaltingParameterBasedon;
    private com.see.truetransact.uicomponent.CLabel lblHaltingPercentage;
    private com.see.truetransact.uicomponent.CLabel lblHaltingSLNO;
    private com.see.truetransact.uicomponent.CLabel lblHaltingSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblHaltingSubParameter;
    private com.see.truetransact.uicomponent.CLabel lblHaltingToDate;
    private com.see.truetransact.uicomponent.CLabel lblHaltingllowanceType;
    private com.see.truetransact.uicomponent.CLabel lblMDDeductionType;
    private com.see.truetransact.uicomponent.CLabel lblMDEligibleAllowances;
    private com.see.truetransact.uicomponent.CLabel lblMDEligiblePercentage;
    private com.see.truetransact.uicomponent.CLabel lblMDFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMDMaximumOf;
    private com.see.truetransact.uicomponent.CLabel lblMDPercentage;
    private com.see.truetransact.uicomponent.CLabel lblMDSLNO;
    private com.see.truetransact.uicomponent.CLabel lblMDSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblMDToDate;
    private com.see.truetransact.uicomponent.CLabel lblMdFixed;
    private com.see.truetransact.uicomponent.CLabel lblMdFixedAmt;
    private com.see.truetransact.uicomponent.CLabel lblMdPecentage;
    private com.see.truetransact.uicomponent.CLabel lblMisecllaniousDeduction;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOAFixed;
    private com.see.truetransact.uicomponent.CLabel lblOAPecentage;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblUsingBasic;
    private com.see.truetransact.uicomponent.CLabel lblYearOfService;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panGratuityAllowance;
    private com.see.truetransact.uicomponent.CPanel panGratuityAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panGratuityAllowanceTable;
    private com.see.truetransact.uicomponent.CPanel panGratuityButtons;
    private com.see.truetransact.uicomponent.CPanel panHaltingAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panHaltingAllowances;
    private com.see.truetransact.uicomponent.CPanel panHaltingAllowancesTable;
    private com.see.truetransact.uicomponent.CPanel panHaltingButtons;
    private com.see.truetransact.uicomponent.CPanel panHaltingDiem;
    private com.see.truetransact.uicomponent.CPanel panMDButtons;
    private com.see.truetransact.uicomponent.CPanel panMdType;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductionInfo;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductionTable;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductionsTab;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panUsinBasic;
    private com.see.truetransact.uicomponent.CButtonGroup rdgHRAPayable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStagnationIncrement;
    private com.see.truetransact.uicomponent.CButtonGroup rdgUsingBasic;
    private com.see.truetransact.uicomponent.CRadioButton rdoUsingBasic_Basic;
    private com.see.truetransact.uicomponent.CRadioButton rdoUsingBasic_Gross;
    private com.see.truetransact.uicomponent.CRadioButton rdoUsingBasic_Others;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpGratuity;
    private com.see.truetransact.uicomponent.CScrollPane srpHaltingAllowances;
    private com.see.truetransact.uicomponent.CScrollPane srpMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CTabbedPane tabMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CTable tblGratuity;
    private com.see.truetransact.uicomponent.CTable tblHaltingAllowances;
    private com.see.truetransact.uicomponent.CTable tblMisecllaniousDeductions;
    private javax.swing.JToolBar tbrMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CDateField tdtGratuityFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtGratuityToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtHaltingFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtHaltingToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtMDFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtMDToDateValue;
    private com.see.truetransact.uicomponent.CTextField txtFromAmount;
    private com.see.truetransact.uicomponent.CTextField txtGratuityBeyondServiceValue;
    private com.see.truetransact.uicomponent.CTextField txtGratuityBeyondValue;
    private com.see.truetransact.uicomponent.CTextField txtGratuityMaximumAmtBeyongValue;
    private com.see.truetransact.uicomponent.CTextField txtGratuityMaximumOfValue;
    private com.see.truetransact.uicomponent.CTextField txtGratuityUptoServiceValue;
    private com.see.truetransact.uicomponent.CTextField txtGratuityUptoValue;
    private com.see.truetransact.uicomponent.CTextField txtHaltingFixedAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtHaltingMaximumOfValue;
    private com.see.truetransact.uicomponent.CTextField txtHaltingPercentageValue;
    private com.see.truetransact.uicomponent.CTextField txtMDEligiblePercentageValue;
    private com.see.truetransact.uicomponent.CTextField txtMDMaximumAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtMDPercentageValue;
    private com.see.truetransact.uicomponent.CTextField txtMdFixedAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtToAmount;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        //        SalaryStructureUI lui = new SalaryStructureUI();
        JFrame j = new JFrame();
        //        j.getContentPane().add(lui);
        j.setSize(615,600);
        j.show();
        //        lui.show();
    }
    
    /**
     * Getter for property mdFixedOrPercentage.
     * @return Value of property mdFixedOrPercentage.
     */
    public java.lang.String getMdFixedOrPercentage() {
        return mdFixedOrPercentage;
    }
    
    /**
     * Setter for property mdFixedOrPercentage.
     * @param mdFixedOrPercentage New value of property mdFixedOrPercentage.
     */
    public void setMdFixedOrPercentage(java.lang.String mdFixedOrPercentage) {
        this.mdFixedOrPercentage = mdFixedOrPercentage;
    }
    
    //    public java.awt.Container getContentPane() {
    //    }
    //
    //    public java.awt.Font getFont() {
    //    }
    //
    //    public java.awt.Component getGlassPane() {
    //    }
    //
    //    public javax.swing.JLayeredPane getLayeredPane() {
    //    }
    //
    //    public javax.swing.JRootPane getRootPane() {
    //    }
    //
    //    public void remove(java.awt.MenuComponent comp) {
    //    }
    //
    //    public void setContentPane(java.awt.Container contentPane) {
    //    }
    //
    //    public void setGlassPane(java.awt.Component glassPane) {
    //    }
    //
    //    public void setLayeredPane(javax.swing.JLayeredPane layeredPane) {
    //    }
    
    //    public java.awt.Container getContentPane() {
    //    }
    //
    //    public java.awt.Font getFont() {
    //    }
    //
    //    public java.awt.Component getGlassPane() {
    //    }
    //
    //    public javax.swing.JLayeredPane getLayeredPane() {
    //    }
    //
    //    public javax.swing.JRootPane getRootPane() {
    //    }
    //
    //    public void remove(java.awt.MenuComponent comp) {
    //    }
    //
    //    public void setContentPane(java.awt.Container contentPane) {
    //    }
    //
    //    public void setGlassPane(java.awt.Component glassPane) {
    //    }
    //
    //    public void setLayeredPane(javax.swing.JLayeredPane layeredPane) {
    //    }
    
}

