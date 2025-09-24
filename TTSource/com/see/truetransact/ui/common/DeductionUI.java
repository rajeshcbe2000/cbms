/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DeductionUI.java
 *
 * Created on May 25, 2004, 5:18 PM
 */

package com.see.truetransact.ui.common;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
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
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;
import java.util.GregorianCalendar;

/**
 *
 * @author  Sathiya
 *
 */

public class DeductionUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    private DeductionOB observable;
    private DeductionMRB objMandatoryRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.DeductionRB", ProxyParameters.LANGUAGE);
    
    private int viewType = -1;
    private boolean _intHANew = false;
    private boolean _intEDNew = false;
    private boolean isFilled = false;
    private boolean salaryStructureSave = false;
    private boolean selectedSingleRow = false;
    private boolean newButtonEnable = false;
    private boolean mouseClickEnable = false;
    private boolean updateLoanValues = false;
    private boolean earningsFlag = false;
    private boolean deductionFlag = false;
    private boolean lossOfPayFlag = false;
    Date currDt = null;
    private String fromDateAlert = "From date should not be empty";
    private String gradeAlert = "Grade should not be empty";
    private String designationAlert = "Designation should not be empty";
    int rowSelected = -1;
    int pan = -1;
    int panEditDelete = -1;
    private int EARNING = 2,DEDUCTION = 1;
    private int EMPLOYEE_ID = 1,ACCT_HEAD = 2,EDIT = 3,DELETE = 4,VIEW = 5,CREDIT_EMPLOYEE_ID = 6,
    FROM_EMPLOYEE_ID = 7, TO_EMPLOYEE_ID = 8,EDIT_EARNING = 9,DEDUCTION_EMPLOYEE_ID =10;
    /** Creates new form BeanForm */
    public DeductionUI() {
        initComponents();
        initSetUp();
    }
    private void initSetUp(){
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLength();
        initTableData();
        //        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSalaryStructureInfo);
        allScreensDisable(false);
        enableDisableAllscreens(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        initComponentData();
        btnEmployeeId.setEnabled(false);
        btnCreditingACNo.setEnabled(false);
        txtCreditingACNo.setEnabled(false);
        tabMisecllaniousDeductions.resetVisits();
        btnDelete.setEnabled(true);
        btnDeductionSave.setEnabled(false);
        btnDeductionNew.setEnabled(false);
        btnDeductionDelete.setEnabled(false);
        //        panLoanValues.setVisible(false);
        //        panLoanDetails.setVisible(false);
        panDeductionLoans.setVisible(false);
        panFixedDetails.setVisible(false);
        btnEdit.setEnabled(true);
        lblCreditSLNO.setVisible(false);
        lblCreditSLNOValue.setVisible(false);
        btnCreditiEmployeeId.setEnabled(false);
        txtCreditAmtValue.setEnabled(false);
        txtNoOfDaysLOP.setVisible(true);
        ClientUtil.enableDisable(panEarningDetailsinfo,false);
        ClientUtil.enableDisable(panEarningButtons,false);
        ClientUtil.enableDisable(panEarningButtons,false);
        ClientUtil.enableDisable(panEmployeeDetails,false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panLoanDetails,false);
        ClientUtil.enableDisable(panDeductionLoans,false);
        ClientUtil.enableDisable(panEarningButtons,false);
        txtLoanSanctionAmt.setVisible(false);
        lblLoanSanctionAmt.setVisible(false);
        //        btnCancelActionPerformed(null);
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
        
        lblDeductionSLNO.setName("lblDeductionSLNO");
        lblDeductionSLNOValue.setName("lblDeductionSLNOValue");
        lblEmployeeId.setName("lblEmployeeId");
        txtEmployeeId.setName("txtEmployeeId");
        lblEmployeeName.setName("lblEmployeeName");
        lblDesignation.setName("lblDesignation");
        lblEmployeeBranch.setName("lblEmployeeBranch");
        lblDeductionType.setName("lblDeductionType");
        lblAllowanceType.setName("lblAllowanceType");
        lblDeductionTypeFromDate.setName("lblDeductionTypeFromDate");
        lblDeductionTypeToDate.setName("lblDeductionTypeToDate");
        lblPremiumAmt.setName("lblPremiumAmt");
        lblCreditingACNo.setName("lblCreditingACNo");
        txtCreditingACNo.setName("txtCreditingACNo");
        txtPremiumAmtValue.setName("txtPremiumAmtValue");
        txtToDateMMValue.setName("txtToDateMMValue");
        txtToDateYYYYValue.setName("txtToDateYYYYValue");
        txtFromDateMMValue.setName("txtFromDateMMValue");
        txtFromDateYYYYValue.setName("txtFromDateYYYYValue");
        cboDeductionTypeValue.setName("cboDeductionTypeValue");
        lblFromDateFormatValue.setName("lblFromDateFormatValue");
        lblToDateFormatValue.setName("lblToDateFormatValue");
        btnDeductionNew.setName("btnDeductionNew");
        btnDeductionSave.setName("btnDeductionSave");
        btnDeductionDelete.setName("btnDeductionDelete");
        lblCreditDesigValue.setName("lblCreditDesigValue");
    }
    private void initTableData(){
        tblEarning.setModel(observable.getTbmEarning());
        tblDeduction.setModel(observable.getTbmDeduction());
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new DeductionRB();
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
        
        lblDeductionSLNO.setText(resourceBundle.getString("lblDeductionSLNO"));
        lblEmployeeId.setText(resourceBundle.getString("lblEmployeeId"));
        lblEmployeeName.setText(resourceBundle.getString("lblEmployeeName"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblEmployeeBranch.setText(resourceBundle.getString("lblEmployeeBranch"));
        lblDeductionType.setText(resourceBundle.getString("lblDeductionType"));
        lblAllowanceType.setText(resourceBundle.getString("lblAllowanceType"));
        lblDeductionTypeFromDate.setText(resourceBundle.getString("lblDeductionTypeFromDate"));
        lblDeductionTypeToDate.setText(resourceBundle.getString("lblDeductionTypeToDate"));
        lblPremiumAmt.setText(resourceBundle.getString("lblPremiumAmt"));
        lblCreditingACNo.setText(resourceBundle.getString("lblCreditingACNo"));
        lblFromDateFormatValue.setText(resourceBundle.getString("lblFromDateFormatValue"));
        lblToDateFormatValue.setText(resourceBundle.getString("lblToDateFormatValue"));
        lblCreditDesigValue.setText(resourceBundle.getString("lblCreditDesigValue"));
    }
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new DeductionMRB();
        cboDeductionTypeValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDeductionTypeValue"));
        txtEmployeeId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployeeId"));
        txtCreditingACNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditingACNo"));
        txtPremiumAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumAmtValue"));
        txtToDateMMValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToDateMMValue"));
        txtToDateYYYYValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToDateYYYYValue"));
        txtFromDateMMValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromDateMMValue"));
        txtFromDateYYYYValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromDateYYYYValue"));
    }
    private void initComponentData(){
        this.cboDeductionTypeValue.setModel(observable.getCbmDeductionTypeValue());
    }
    
    private void setObservable() {
        observable = DeductionOB.getInstance();
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
        txtFromDateMMValue.setValidation(new NumericValidation(2,0));
        txtFromDateYYYYValue.setValidation(new NumericValidation(4,0));
        txtToDateMMValue.setValidation(new NumericValidation(2,0));
        txtToDateYYYYValue.setValidation(new NumericValidation(4,0));
        txtPremiumAmtValue.setValidation(new CurrencyValidation(14,2));
        
        txtLoanAccNoValue.setAllowAll(true);
        txtLoanDescValue.setAllowAll(true);
        txtLoanAmountValue.setValidation(new CurrencyValidation(14,2));
        txtLoanSanctionAmt.setValidation(new CurrencyValidation(14,2));
        txtInstallmentAmtValue.setValidation(new CurrencyValidation(14,2));
        txtInstIntRate.setValidation(new NumericValidation(3,2));
        txtIntNetAmount.setValidation(new CurrencyValidation(14,2));
        txtNoofInstallmentsValue.setValidation(new NumericValidation(4,0));
        txtLoanAvailedBranchValue.setAllowAll(true);
        txtRemarksValue.setAllowAll(true);
        txtLoanStatusValue.setAllowAll(true);
        
        txtCreditBasicPay.setValidation(new CurrencyValidation(14,2));
        txtCreditAmtValue.setValidation(new CurrencyValidation(14,2));
        txtNoOfDaysLOP.setValidation(new NumericValidation(3,0));
        
        //        txtSalFromDateMMValue.setValidation(new NumericValidation(2,0));
        //        txtSalFromDateYYYYValue.setValidation(new NumericValidation(4,0));
        //        txtSalToDateMMValue.setValidation(new NumericValidation(2,0));
        //        txtSalToDateYYYYValue.setValidation(new NumericValidation(4,0));
    }
    
    private void addRadioButton(){
        rdgDeductionType.add(rdoDeductionTypeFixed);
        rdgDeductionType.add(rdoDeductionTypeInstallments);
        
        //        rdgSalaryDetails.add(rdoEmployeeWise);
        //        rdgSalaryDetails.add(rdoBranchWise);
        //        rdgSalaryDetails.add(rdoRegionalWise);
    }
    
    private void removeRadioButton(){
        rdgDeductionType.remove(rdoDeductionTypeFixed);
        rdgDeductionType.remove(rdoDeductionTypeInstallments);
        
        //        rdgSalaryDetails.remove(rdoEmployeeWise);
        //        rdgSalaryDetails.remove(rdoBranchWise);
        //        rdgSalaryDetails.remove(rdoRegionalWise);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgDeductionType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSalaryDetails = new com.see.truetransact.uicomponent.CButtonGroup();
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
        panDeductions = new com.see.truetransact.uicomponent.CPanel();
        panCreditInfo = new com.see.truetransact.uicomponent.CPanel();
        panDeductionTable = new com.see.truetransact.uicomponent.CPanel();
        srpDeduction = new com.see.truetransact.uicomponent.CScrollPane();
        tblDeduction = new com.see.truetransact.uicomponent.CTable();
        panDeductionDetails = new com.see.truetransact.uicomponent.CPanel();
        panFixedDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDeductionTypeFromDate = new com.see.truetransact.uicomponent.CLabel();
        panFromDateInfo = new com.see.truetransact.uicomponent.CPanel();
        txtFromDateYYYYValue = new com.see.truetransact.uicomponent.CTextField();
        lblHaltingParameterBasedon2 = new com.see.truetransact.uicomponent.CLabel();
        txtFromDateMMValue = new com.see.truetransact.uicomponent.CTextField();
        lblFromDateFormatValue = new com.see.truetransact.uicomponent.CLabel();
        lblDeductionTypeToDate = new com.see.truetransact.uicomponent.CLabel();
        panToDateInfo = new com.see.truetransact.uicomponent.CPanel();
        lblHaltingParameterBasedon3 = new com.see.truetransact.uicomponent.CLabel();
        txtToDateYYYYValue = new com.see.truetransact.uicomponent.CTextField();
        txtToDateMMValue = new com.see.truetransact.uicomponent.CTextField();
        lblToDateFormatValue = new com.see.truetransact.uicomponent.CLabel();
        lblPremiumAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPremiumAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblCreditingACNo = new com.see.truetransact.uicomponent.CLabel();
        panCreditingACNo = new com.see.truetransact.uicomponent.CPanel();
        txtCreditingACNo = new com.see.truetransact.uicomponent.CTextField();
        btnCreditingACNo = new com.see.truetransact.uicomponent.CButton();
        panDeductionButtons = new com.see.truetransact.uicomponent.CPanel();
        btnDeductionNew = new com.see.truetransact.uicomponent.CButton();
        btnDeductionSave = new com.see.truetransact.uicomponent.CButton();
        btnDeductionDelete = new com.see.truetransact.uicomponent.CButton();
        panDeductionLoans = new com.see.truetransact.uicomponent.CPanel();
        panLoanValues = new com.see.truetransact.uicomponent.CPanel();
        lblLoanAvailedBranch = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAvailedBranchValue = new com.see.truetransact.uicomponent.CTextField();
        lblLoanFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblLoanToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblLoanStoppedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanStoppedDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarksValue = new com.see.truetransact.uicomponent.CTextField();
        lblLoanStatus = new com.see.truetransact.uicomponent.CLabel();
        txtLoanStatusValue = new com.see.truetransact.uicomponent.CTextField();
        panLoanDetails = new com.see.truetransact.uicomponent.CPanel();
        txtLoanAccNoValue = new com.see.truetransact.uicomponent.CTextField();
        lblLoanAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblLoanFromDate2 = new com.see.truetransact.uicomponent.CLabel();
        txtLoanDescValue = new com.see.truetransact.uicomponent.CTextField();
        txtLoanAmountValue = new com.see.truetransact.uicomponent.CTextField();
        lblLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentAmt = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentAmtValue = new com.see.truetransact.uicomponent.CTextField();
        txtInstIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblInstIntRate = new com.see.truetransact.uicomponent.CLabel();
        txtNoofInstallmentsValue = new com.see.truetransact.uicomponent.CTextField();
        lblNoofInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtIntNetAmount = new com.see.truetransact.uicomponent.CTextField();
        lblIntNetAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLoanSanctionAmt = new com.see.truetransact.uicomponent.CTextField();
        lblLoanSanctionAmt = new com.see.truetransact.uicomponent.CLabel();
        panEmployeeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDeductionSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblDeductionSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        panEmployeeId = new com.see.truetransact.uicomponent.CPanel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeId = new com.see.truetransact.uicomponent.CButton();
        lblEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblDesignationValue = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeBranch = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeBranchValue = new com.see.truetransact.uicomponent.CLabel();
        lblDeductionType = new com.see.truetransact.uicomponent.CLabel();
        panDeductionType = new com.see.truetransact.uicomponent.CPanel();
        rdoDeductionTypeFixed = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeductionTypeInstallments = new com.see.truetransact.uicomponent.CRadioButton();
        lblAllowanceType = new com.see.truetransact.uicomponent.CLabel();
        cboDeductionTypeValue = new com.see.truetransact.uicomponent.CComboBox();
        panEarningInfo = new com.see.truetransact.uicomponent.CPanel();
        panEarningDetails = new com.see.truetransact.uicomponent.CPanel();
        panEarningTable = new com.see.truetransact.uicomponent.CPanel();
        srpEarning = new com.see.truetransact.uicomponent.CScrollPane();
        tblEarning = new com.see.truetransact.uicomponent.CTable();
        panEarningDetailsinfo = new com.see.truetransact.uicomponent.CPanel();
        cboSubParameterValue = new com.see.truetransact.uicomponent.CComboBox();
        cboCreditTypeValue = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblCreditLastIncDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCreditAllowanceType = new com.see.truetransact.uicomponent.CLabel();
        lblCreditEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        lblCreditDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblCreditLastIncDate = new com.see.truetransact.uicomponent.CLabel();
        lblCreditEmployeeNameValue = new com.see.truetransact.uicomponent.CLabel();
        cboParameterBasedOnValue = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        panCreditEmployeeId = new com.see.truetransact.uicomponent.CPanel();
        txtCreditEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        btnCreditiEmployeeId = new com.see.truetransact.uicomponent.CButton();
        lblCreditnextIncDate = new com.see.truetransact.uicomponent.CLabel();
        lblCreditSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblCreditBasicPay = new com.see.truetransact.uicomponent.CLabel();
        lblCreditEmployeeBranchValue = new com.see.truetransact.uicomponent.CLabel();
        lblParameterBasedon = new com.see.truetransact.uicomponent.CLabel();
        lblCreditEmployeeBranch = new com.see.truetransact.uicomponent.CLabel();
        lblCreditnextIncDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCreditDesignationValue = new com.see.truetransact.uicomponent.CLabel();
        txtCreditBasicPay = new com.see.truetransact.uicomponent.CTextField();
        lblSubParameter = new com.see.truetransact.uicomponent.CLabel();
        txtCreditAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblCreditBasicPay1 = new com.see.truetransact.uicomponent.CLabel();
        lblHaltingFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        tdtToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblHaltingToDate = new com.see.truetransact.uicomponent.CLabel();
        cboCreditDesigValue = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditDesigValue = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfDaysLOP = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfDaysLOP = new com.see.truetransact.uicomponent.CLabel();
        chkLossOfPay = new com.see.truetransact.uicomponent.CCheckBox();
        lblLossOfPay = new com.see.truetransact.uicomponent.CLabel();
        lblAdditionalPay = new com.see.truetransact.uicomponent.CLabel();
        chkAdditionalPay = new com.see.truetransact.uicomponent.CCheckBox();
        panEarningButtons = new com.see.truetransact.uicomponent.CPanel();
        btnEarningNew = new com.see.truetransact.uicomponent.CButton();
        btnEarningSave = new com.see.truetransact.uicomponent.CButton();
        btnEarningDelete = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));

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
        panMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(860, 645));
        panMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(860, 645));
        panMisecllaniousDeductions.setLayout(new java.awt.GridBagLayout());

        tabMisecllaniousDeductions.setMinimumSize(new java.awt.Dimension(840, 640));
        tabMisecllaniousDeductions.setName("");
        tabMisecllaniousDeductions.setPreferredSize(new java.awt.Dimension(840, 640));

        panDeductions.setMinimumSize(new java.awt.Dimension(800, 650));
        panDeductions.setPreferredSize(new java.awt.Dimension(800, 650));
        panDeductions.setLayout(new java.awt.GridBagLayout());

        panCreditInfo.setMinimumSize(new java.awt.Dimension(800, 650));
        panCreditInfo.setPreferredSize(new java.awt.Dimension(800, 650));
        panCreditInfo.setLayout(new java.awt.GridBagLayout());

        panDeductionTable.setMinimumSize(new java.awt.Dimension(350, 300));
        panDeductionTable.setPreferredSize(new java.awt.Dimension(350, 300));
        panDeductionTable.setLayout(new java.awt.GridBagLayout());

        srpDeduction.setMinimumSize(new java.awt.Dimension(350, 250));
        srpDeduction.setPreferredSize(new java.awt.Dimension(350, 250));

        tblDeduction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ));
        tblDeduction.setMinimumSize(new java.awt.Dimension(60, 64));
        tblDeduction.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 250));
        tblDeduction.setPreferredSize(new java.awt.Dimension(350, 250));
        tblDeduction.setOpaque(false);
        tblDeduction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDeductionMouseClicked(evt);
            }
        });
        srpDeduction.setViewportView(tblDeduction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panDeductionTable.add(srpDeduction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 150, 0);
        panCreditInfo.add(panDeductionTable, gridBagConstraints);

        panDeductionDetails.setMinimumSize(new java.awt.Dimension(350, 660));
        panDeductionDetails.setPreferredSize(new java.awt.Dimension(350, 660));
        panDeductionDetails.setLayout(new java.awt.GridBagLayout());

        panFixedDetails.setMinimumSize(new java.awt.Dimension(350, 150));
        panFixedDetails.setPreferredSize(new java.awt.Dimension(350, 150));
        panFixedDetails.setLayout(new java.awt.GridBagLayout());

        lblDeductionTypeFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panFixedDetails.add(lblDeductionTypeFromDate, gridBagConstraints);

        panFromDateInfo.setMinimumSize(new java.awt.Dimension(125, 24));
        panFromDateInfo.setPreferredSize(new java.awt.Dimension(125, 24));
        panFromDateInfo.setLayout(new java.awt.GridBagLayout());

        txtFromDateYYYYValue.setMinimumSize(new java.awt.Dimension(70, 21));
        txtFromDateYYYYValue.setPreferredSize(new java.awt.Dimension(70, 21));
        txtFromDateYYYYValue.setValidation(new CurrencyValidation(14,2));
        txtFromDateYYYYValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromDateYYYYValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panFromDateInfo.add(txtFromDateYYYYValue, gridBagConstraints);

        lblHaltingParameterBasedon2.setText(" - ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panFromDateInfo.add(lblHaltingParameterBasedon2, gridBagConstraints);

        txtFromDateMMValue.setMinimumSize(new java.awt.Dimension(40, 21));
        txtFromDateMMValue.setPreferredSize(new java.awt.Dimension(40, 21));
        txtFromDateMMValue.setValidation(new CurrencyValidation(14,2));
        txtFromDateMMValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromDateMMValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panFromDateInfo.add(txtFromDateMMValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(panFromDateInfo, gridBagConstraints);

        lblFromDateFormatValue.setText("MM      -     YYYY");
        lblFromDateFormatValue.setMinimumSize(new java.awt.Dimension(125, 18));
        lblFromDateFormatValue.setPreferredSize(new java.awt.Dimension(125, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(lblFromDateFormatValue, gridBagConstraints);

        lblDeductionTypeToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panFixedDetails.add(lblDeductionTypeToDate, gridBagConstraints);

        panToDateInfo.setMinimumSize(new java.awt.Dimension(125, 24));
        panToDateInfo.setPreferredSize(new java.awt.Dimension(125, 24));
        panToDateInfo.setLayout(new java.awt.GridBagLayout());

        lblHaltingParameterBasedon3.setText(" - ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panToDateInfo.add(lblHaltingParameterBasedon3, gridBagConstraints);

        txtToDateYYYYValue.setMinimumSize(new java.awt.Dimension(70, 21));
        txtToDateYYYYValue.setPreferredSize(new java.awt.Dimension(70, 21));
        txtToDateYYYYValue.setValidation(new CurrencyValidation(14,2));
        txtToDateYYYYValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToDateYYYYValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panToDateInfo.add(txtToDateYYYYValue, gridBagConstraints);

        txtToDateMMValue.setMinimumSize(new java.awt.Dimension(40, 21));
        txtToDateMMValue.setPreferredSize(new java.awt.Dimension(40, 21));
        txtToDateMMValue.setValidation(new CurrencyValidation(14,2));
        txtToDateMMValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToDateMMValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panToDateInfo.add(txtToDateMMValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(panToDateInfo, gridBagConstraints);

        lblToDateFormatValue.setText("MM      -     YYYY");
        lblToDateFormatValue.setMinimumSize(new java.awt.Dimension(125, 18));
        lblToDateFormatValue.setPreferredSize(new java.awt.Dimension(125, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(lblToDateFormatValue, gridBagConstraints);

        lblPremiumAmt.setText("Amount to be deducted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panFixedDetails.add(lblPremiumAmt, gridBagConstraints);

        txtPremiumAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(txtPremiumAmtValue, gridBagConstraints);

        lblCreditingACNo.setText("Crediting Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panFixedDetails.add(lblCreditingACNo, gridBagConstraints);

        panCreditingACNo.setMinimumSize(new java.awt.Dimension(125, 25));
        panCreditingACNo.setPreferredSize(new java.awt.Dimension(125, 25));
        panCreditingACNo.setLayout(new java.awt.GridBagLayout());

        txtCreditingACNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCreditingACNo.add(txtCreditingACNo, gridBagConstraints);

        btnCreditingACNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCreditingACNo.setMaximumSize(new java.awt.Dimension(25, 25));
        btnCreditingACNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnCreditingACNo.setName("btnAccountNo");
        btnCreditingACNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCreditingACNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditingACNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCreditingACNo.add(btnCreditingACNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 50);
        panFixedDetails.add(panCreditingACNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panDeductionDetails.add(panFixedDetails, gridBagConstraints);

        btnDeductionNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnDeductionNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDeductionNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeductionNewActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnDeductionNew);

        btnDeductionSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnDeductionSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDeductionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeductionSaveActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnDeductionSave);

        btnDeductionDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDeductionDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDeductionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeductionDeleteActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnDeductionDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 74, 0, 0);
        panDeductionDetails.add(panDeductionButtons, gridBagConstraints);

        panDeductionLoans.setMinimumSize(new java.awt.Dimension(350, 300));
        panDeductionLoans.setPreferredSize(new java.awt.Dimension(350, 300));
        panDeductionLoans.setLayout(new java.awt.GridBagLayout());

        panLoanValues.setMinimumSize(new java.awt.Dimension(350, 150));
        panLoanValues.setPreferredSize(new java.awt.Dimension(350, 150));
        panLoanValues.setLayout(new java.awt.GridBagLayout());

        lblLoanAvailedBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanAvailedBranch.setText("Loan availed branch");
        lblLoanAvailedBranch.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblLoanAvailedBranch, gridBagConstraints);

        txtLoanAvailedBranchValue.setEditable(false);
        txtLoanAvailedBranchValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanAvailedBranchValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 35);
        panLoanValues.add(txtLoanAvailedBranchValue, gridBagConstraints);

        lblLoanFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanFromDate.setText("Loan repayment from date");
        lblLoanFromDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblLoanFromDate, gridBagConstraints);

        tdtLoanFromDateValue.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 35);
        panLoanValues.add(tdtLoanFromDateValue, gridBagConstraints);

        lblLoanToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanToDate.setText("Loan repayment to date");
        lblLoanToDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblLoanToDate, gridBagConstraints);

        tdtLoanToDateValue.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLoanToDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtLoanToDateValue.setEnabled(false);
        tdtLoanToDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLoanToDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 35);
        panLoanValues.add(tdtLoanToDateValue, gridBagConstraints);

        lblLoanStoppedDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanStoppedDate.setText("Stop deduction date");
        lblLoanStoppedDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblLoanStoppedDate, gridBagConstraints);

        tdtLoanStoppedDateValue.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 35);
        panLoanValues.add(tdtLoanStoppedDateValue, gridBagConstraints);

        lblRemarks.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRemarks.setText("Remarks");
        lblRemarks.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblRemarks, gridBagConstraints);

        txtRemarksValue.setEditable(false);
        txtRemarksValue.setMinimumSize(new java.awt.Dimension(150, 20));
        txtRemarksValue.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 38);
        panLoanValues.add(txtRemarksValue, gridBagConstraints);

        lblLoanStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanStatus.setText("Loan Status");
        lblLoanStatus.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanValues.add(lblLoanStatus, gridBagConstraints);

        txtLoanStatusValue.setEditable(false);
        txtLoanStatusValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanStatusValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 38);
        panLoanValues.add(txtLoanStatusValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panDeductionLoans.add(panLoanValues, gridBagConstraints);

        panLoanDetails.setMinimumSize(new java.awt.Dimension(350, 150));
        panLoanDetails.setPreferredSize(new java.awt.Dimension(350, 150));
        panLoanDetails.setLayout(new java.awt.GridBagLayout());

        txtLoanAccNoValue.setEditable(false);
        txtLoanAccNoValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanAccNoValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 81);
        panLoanDetails.add(txtLoanAccNoValue, gridBagConstraints);

        lblLoanAccNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanAccNo.setText("Loan Account No");
        lblLoanAccNo.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblLoanAccNo, gridBagConstraints);

        lblLoanFromDate2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanFromDate2.setText("Account head description");
        lblLoanFromDate2.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblLoanFromDate2, gridBagConstraints);

        txtLoanDescValue.setEditable(false);
        txtLoanDescValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanDescValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 81);
        panLoanDetails.add(txtLoanDescValue, gridBagConstraints);

        txtLoanAmountValue.setEditable(false);
        txtLoanAmountValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanAmountValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 78);
        panLoanDetails.add(txtLoanAmountValue, gridBagConstraints);

        lblLoanAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanAmount.setText("Princlipal amount");
        lblLoanAmount.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblLoanAmount, gridBagConstraints);

        lblInstallmentAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstallmentAmt.setText("Installment amount");
        lblInstallmentAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblInstallmentAmt, gridBagConstraints);

        txtInstallmentAmtValue.setEditable(false);
        txtInstallmentAmtValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtInstallmentAmtValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 78);
        panLoanDetails.add(txtInstallmentAmtValue, gridBagConstraints);

        txtInstIntRate.setEditable(false);
        txtInstIntRate.setMinimumSize(new java.awt.Dimension(100, 20));
        txtInstIntRate.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 78);
        panLoanDetails.add(txtInstIntRate, gridBagConstraints);

        lblInstIntRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstIntRate.setText("Interest Rate");
        lblInstIntRate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblInstIntRate, gridBagConstraints);

        txtNoofInstallmentsValue.setEditable(false);
        txtNoofInstallmentsValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtNoofInstallmentsValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panLoanDetails.add(txtNoofInstallmentsValue, gridBagConstraints);

        lblNoofInstallments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoofInstallments.setText("No of installments");
        lblNoofInstallments.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblNoofInstallments, gridBagConstraints);

        txtIntNetAmount.setEditable(false);
        txtIntNetAmount.setMinimumSize(new java.awt.Dimension(100, 20));
        txtIntNetAmount.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 78);
        panLoanDetails.add(txtIntNetAmount, gridBagConstraints);

        lblIntNetAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIntNetAmount.setText("Net Amount");
        lblIntNetAmount.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblIntNetAmount, gridBagConstraints);

        txtLoanSanctionAmt.setEditable(false);
        txtLoanSanctionAmt.setMinimumSize(new java.awt.Dimension(100, 20));
        txtLoanSanctionAmt.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 78);
        panLoanDetails.add(txtLoanSanctionAmt, gridBagConstraints);

        lblLoanSanctionAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanSanctionAmt.setText("Sanctioned Amount");
        lblLoanSanctionAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLoanDetails.add(lblLoanSanctionAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panDeductionLoans.add(panLoanDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panDeductionDetails.add(panDeductionLoans, gridBagConstraints);

        panEmployeeDetails.setMinimumSize(new java.awt.Dimension(350, 150));
        panEmployeeDetails.setPreferredSize(new java.awt.Dimension(350, 150));
        panEmployeeDetails.setLayout(new java.awt.GridBagLayout());

        lblDeductionSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblDeductionSLNO, gridBagConstraints);

        lblDeductionSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblDeductionSLNOValue.setMinimumSize(new java.awt.Dimension(75, 16));
        lblDeductionSLNOValue.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 1, 1);
        panEmployeeDetails.add(lblDeductionSLNOValue, gridBagConstraints);

        lblEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblEmployeeId, gridBagConstraints);

        panEmployeeId.setMinimumSize(new java.awt.Dimension(125, 25));
        panEmployeeId.setPreferredSize(new java.awt.Dimension(125, 25));
        panEmployeeId.setLayout(new java.awt.GridBagLayout());

        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmployeeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmployeeIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmployeeId.add(txtEmployeeId, gridBagConstraints);

        btnEmployeeId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployeeId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnEmployeeId.setMinimumSize(new java.awt.Dimension(25, 25));
        btnEmployeeId.setName("btnAccountNo");
        btnEmployeeId.setPreferredSize(new java.awt.Dimension(25, 25));
        btnEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panEmployeeId.add(btnEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 1);
        panEmployeeDetails.add(panEmployeeId, gridBagConstraints);

        lblEmployeeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmployeeName.setText("Employee Name");
        lblEmployeeName.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblEmployeeName, gridBagConstraints);

        lblEmployeeNameValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmployeeNameValue.setText("Employee Name");
        lblEmployeeNameValue.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 1);
        panEmployeeDetails.add(lblEmployeeNameValue, gridBagConstraints);
        lblEmployeeNameValue.getAccessibleContext().setAccessibleName("");

        lblDesignation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesignation.setText("Designation");
        lblDesignation.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblDesignation, gridBagConstraints);

        lblDesignationValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesignationValue.setText("Designation");
        lblDesignationValue.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 1, 1);
        panEmployeeDetails.add(lblDesignationValue, gridBagConstraints);

        lblEmployeeBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmployeeBranch.setText("Employee Branch");
        lblEmployeeBranch.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblEmployeeBranch, gridBagConstraints);

        lblEmployeeBranchValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmployeeBranchValue.setText("Employee Branch");
        lblEmployeeBranchValue.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 1);
        panEmployeeDetails.add(lblEmployeeBranchValue, gridBagConstraints);

        lblDeductionType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDeductionType.setText("Deduction Type");
        lblDeductionType.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblDeductionType, gridBagConstraints);

        panDeductionType.setMinimumSize(new java.awt.Dimension(150, 18));
        panDeductionType.setPreferredSize(new java.awt.Dimension(150, 18));
        panDeductionType.setLayout(new java.awt.GridBagLayout());

        rdoDeductionTypeFixed.setText("Fixed");
        rdoDeductionTypeFixed.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoDeductionTypeFixed.setMinimumSize(new java.awt.Dimension(65, 18));
        rdoDeductionTypeFixed.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoDeductionTypeFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeductionTypeFixedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDeductionType.add(rdoDeductionTypeFixed, gridBagConstraints);

        rdoDeductionTypeInstallments.setText("Installments");
        rdoDeductionTypeInstallments.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoDeductionTypeInstallments.setMinimumSize(new java.awt.Dimension(100, 18));
        rdoDeductionTypeInstallments.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoDeductionTypeInstallments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeductionTypeInstallmentsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDeductionType.add(rdoDeductionTypeInstallments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 1, 1);
        panEmployeeDetails.add(panDeductionType, gridBagConstraints);

        lblAllowanceType.setText("Deduction towards");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEmployeeDetails.add(lblAllowanceType, gridBagConstraints);

        cboDeductionTypeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDeductionTypeValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 1, 4);
        panEmployeeDetails.add(cboDeductionTypeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panDeductionDetails.add(panEmployeeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 150, 0);
        panCreditInfo.add(panDeductionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panDeductions.add(panCreditInfo, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Deduction Details", panDeductions);

        panEarningInfo.setMinimumSize(new java.awt.Dimension(750, 230));
        panEarningInfo.setPreferredSize(new java.awt.Dimension(750, 230));
        panEarningInfo.setLayout(new java.awt.GridBagLayout());

        panEarningDetails.setMinimumSize(new java.awt.Dimension(800, 400));
        panEarningDetails.setPreferredSize(new java.awt.Dimension(800, 400));
        panEarningDetails.setLayout(new java.awt.GridBagLayout());

        panEarningTable.setMinimumSize(new java.awt.Dimension(450, 350));
        panEarningTable.setPreferredSize(new java.awt.Dimension(450, 350));
        panEarningTable.setLayout(new java.awt.GridBagLayout());

        srpEarning.setMinimumSize(new java.awt.Dimension(200, 404));
        srpEarning.setPreferredSize(new java.awt.Dimension(200, 404));

        tblEarning.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ));
        tblEarning.setMinimumSize(new java.awt.Dimension(60, 64));
        tblEarning.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblEarning.setOpaque(false);
        tblEarning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEarningMouseClicked(evt);
            }
        });
        srpEarning.setViewportView(tblEarning);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panEarningTable.add(srpEarning, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEarningDetails.add(panEarningTable, gridBagConstraints);

        panEarningDetailsinfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panEarningDetailsinfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panEarningDetailsinfo.setLayout(new java.awt.GridBagLayout());

        cboSubParameterValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubParameterValue.setPopupWidth(250);
        cboSubParameterValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubParameterValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(cboSubParameterValue, gridBagConstraints);

        cboCreditTypeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCreditTypeValue.setPopupWidth(250);
        cboCreditTypeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCreditTypeValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(cboCreditTypeValue, gridBagConstraints);

        lblCreditSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblCreditSLNOValue.setMinimumSize(new java.awt.Dimension(75, 16));
        lblCreditSLNOValue.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditSLNOValue, gridBagConstraints);

        lblCreditLastIncDateValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCreditLastIncDateValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditLastIncDateValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreditLastIncDateValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreditLastIncDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditLastIncDateValue, gridBagConstraints);

        lblCreditAllowanceType.setText("Allowance type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditAllowanceType, gridBagConstraints);

        lblCreditEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditEmployeeId, gridBagConstraints);

        lblCreditDesignation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditDesignation.setText("Designation");
        lblCreditDesignation.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditDesignation, gridBagConstraints);

        lblCreditLastIncDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditLastIncDate.setText("Last increment date");
        lblCreditLastIncDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditLastIncDate, gridBagConstraints);

        lblCreditEmployeeNameValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCreditEmployeeNameValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditEmployeeNameValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreditEmployeeNameValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreditEmployeeNameValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditEmployeeNameValue, gridBagConstraints);

        cboParameterBasedOnValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboParameterBasedOnValue.setPopupWidth(250);
        cboParameterBasedOnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboParameterBasedOnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(cboParameterBasedOnValue, gridBagConstraints);

        lblCreditEmployeeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditEmployeeName.setText("Employee name");
        lblCreditEmployeeName.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditEmployeeName, gridBagConstraints);

        panCreditEmployeeId.setMinimumSize(new java.awt.Dimension(125, 25));
        panCreditEmployeeId.setPreferredSize(new java.awt.Dimension(125, 25));
        panCreditEmployeeId.setLayout(new java.awt.GridBagLayout());

        txtCreditEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCreditEmployeeId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCreditEmployeeIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCreditEmployeeId.add(txtCreditEmployeeId, gridBagConstraints);

        btnCreditiEmployeeId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCreditiEmployeeId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnCreditiEmployeeId.setMinimumSize(new java.awt.Dimension(25, 25));
        btnCreditiEmployeeId.setName("btnAccountNo");
        btnCreditiEmployeeId.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCreditiEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditiEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCreditEmployeeId.add(btnCreditiEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(panCreditEmployeeId, gridBagConstraints);

        lblCreditnextIncDate.setText("Next increment date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditnextIncDate, gridBagConstraints);

        lblCreditSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditSLNO, gridBagConstraints);

        lblCreditBasicPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditBasicPay.setText("Present basic pay");
        lblCreditBasicPay.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditBasicPay, gridBagConstraints);

        lblCreditEmployeeBranchValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCreditEmployeeBranchValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditEmployeeBranchValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreditEmployeeBranchValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreditEmployeeBranchValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditEmployeeBranchValue, gridBagConstraints);

        lblParameterBasedon.setText("Parameter Based on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblParameterBasedon, gridBagConstraints);

        lblCreditEmployeeBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditEmployeeBranch.setText("Employee branch");
        lblCreditEmployeeBranch.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditEmployeeBranch, gridBagConstraints);

        lblCreditnextIncDateValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCreditnextIncDateValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditnextIncDateValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreditnextIncDateValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreditnextIncDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditnextIncDateValue, gridBagConstraints);

        lblCreditDesignationValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCreditDesignationValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditDesignationValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreditDesignationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreditDesignationValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(lblCreditDesignationValue, gridBagConstraints);

        txtCreditBasicPay.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(txtCreditBasicPay, gridBagConstraints);

        lblSubParameter.setText("Sub Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblSubParameter, gridBagConstraints);

        txtCreditAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(txtCreditAmtValue, gridBagConstraints);

        lblCreditBasicPay1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditBasicPay1.setText("Amount per Month");
        lblCreditBasicPay1.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditBasicPay1, gridBagConstraints);

        lblHaltingFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblHaltingFromDate, gridBagConstraints);

        tdtFromDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(tdtFromDateValue, gridBagConstraints);

        tdtToDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(tdtToDateValue, gridBagConstraints);

        lblHaltingToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblHaltingToDate, gridBagConstraints);

        cboCreditDesigValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCreditDesigValue.setPopupWidth(250);
        cboCreditDesigValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCreditDesigValueActionPerformed(evt);
            }
        });
        cboCreditDesigValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboCreditDesigValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(cboCreditDesigValue, gridBagConstraints);

        lblCreditDesigValue.setText("Grade");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblCreditDesigValue, gridBagConstraints);

        txtNoOfDaysLOP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(txtNoOfDaysLOP, gridBagConstraints);

        lblNoOfDaysLOP.setText("No Of Days:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panEarningDetailsinfo.add(lblNoOfDaysLOP, gridBagConstraints);

        chkLossOfPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLossOfPayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(chkLossOfPay, gridBagConstraints);

        lblLossOfPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLossOfPay.setText("Loss of pay");
        lblLossOfPay.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panEarningDetailsinfo.add(lblLossOfPay, gridBagConstraints);

        lblAdditionalPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdditionalPay.setText("Additional Pay");
        lblAdditionalPay.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panEarningDetailsinfo.add(lblAdditionalPay, gridBagConstraints);

        chkAdditionalPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAdditionalPayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEarningDetailsinfo.add(chkAdditionalPay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panEarningDetails.add(panEarningDetailsinfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panEarningInfo.add(panEarningDetails, gridBagConstraints);

        btnEarningNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnEarningNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnEarningNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEarningNewActionPerformed(evt);
            }
        });
        panEarningButtons.add(btnEarningNew);

        btnEarningSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnEarningSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnEarningSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEarningSaveActionPerformed(evt);
            }
        });
        panEarningButtons.add(btnEarningSave);

        btnEarningDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnEarningDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnEarningDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEarningDeleteActionPerformed(evt);
            }
        });
        panEarningButtons.add(btnEarningDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 175, 2, 2);
        panEarningInfo.add(panEarningButtons, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Earning Details", panEarningInfo);

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

    private void chkAdditionalPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAdditionalPayActionPerformed
        // TODO add your handling code here:
        if(chkAdditionalPay.isSelected() == true){
            chkLossOfPay.setSelected(false);
            cboCreditTypeValue.setVisible(true);
            lblCreditAllowanceType.setVisible(true);
            lblNoOfDaysLOP.setVisible(true);
            txtNoOfDaysLOP.setVisible(true);
            cboParameterBasedOnValue.setVisible(false);
            lblParameterBasedon.setVisible(false);
            cboSubParameterValue.setVisible(false);
            lblSubParameter.setVisible(false);
            cboCreditTypeValue.setEnabled(false);
            cboCreditTypeValue.setSelectedItem(observable.getCbmCreditTypeValue().getDataForKey("ADDITIONAL PAY"));
            txtCreditAmtValue.setVisible(false);
            lblCreditBasicPay1.setVisible(false);
            txtCreditAmtValue.setText("");
            txtCreditAmtValue.setEnabled(true);
            chkAdditionalPay.setEnabled(true);
            chkLossOfPay.setEnabled(true);
        }else{
            chkLossOfPay.setSelected(false);
            cboCreditTypeValue.setVisible(true);
            lblCreditAllowanceType.setVisible(true);
            lblNoOfDaysLOP.setVisible(true);
            txtNoOfDaysLOP.setVisible(true);
            cboParameterBasedOnValue.setVisible(true);
            lblParameterBasedon.setVisible(true);
            cboSubParameterValue.setVisible(true);
            lblSubParameter.setVisible(true);
            //            cboParameterBasedOnValue.setEnabled(true);
            txtCreditAmtValue.setVisible(true);
            lblCreditBasicPay1.setVisible(true);
            cboCreditTypeValue.setEnabled(true);
            txtCreditAmtValue.setText("");
            txtCreditAmtValue.setEnabled(true);
            txtCreditAmtValue.setEnabled(true);
            chkAdditionalPay.setEnabled(true);
            chkLossOfPay.setEnabled(true);
        }
    }//GEN-LAST:event_chkAdditionalPayActionPerformed
    
    private void tdtFromDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateValueFocusLost
        // TODO add your handling code here:
        Date fromEarningDate = DateUtil.getDateMMDDYYYY(tdtFromDateValue.getDateValue());
        txtNoOfDaysLOP.setText("");
        if(fromEarningDate!= null){
            long noOfDays = 0;
            noOfDays = noOfDays +1;
            txtNoOfDaysLOP.setText(String.valueOf(noOfDays));
            System.out.println("#@$#@#$@#$noOfDays"+noOfDays);
        }
    }//GEN-LAST:event_tdtFromDateValueFocusLost
    
    private void chkLossOfPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLossOfPayActionPerformed
        // TODO add your handling code here:
        if(chkLossOfPay.isSelected() == true){
            chkAdditionalPay.setSelected(false);
            txtCreditAmtValue.setText("");
            cboCreditTypeValue.setVisible(true);
            lblCreditAllowanceType.setVisible(true);
            lblNoOfDaysLOP.setVisible(true);
            txtNoOfDaysLOP.setVisible(true);
            cboParameterBasedOnValue.setVisible(false);
            lblParameterBasedon.setVisible(false);
            cboSubParameterValue.setVisible(false);
            lblSubParameter.setVisible(false);
            cboCreditTypeValue.setEnabled(false);
            cboCreditTypeValue.setSelectedItem(observable.getCbmCreditTypeValue().getDataForKey("LOP"));
            //            cboCreditTypeValue.setSelectedItem("LOSS_OF_PAY");
            txtCreditAmtValue.setVisible(false);
            lblCreditBasicPay1.setVisible(false);
            chkAdditionalPay.setEnabled(true);
            chkLossOfPay.setEnabled(true); 
        }else{
            chkAdditionalPay.setSelected(false);
            txtCreditAmtValue.setText("");
            cboCreditTypeValue.setVisible(true);
            lblCreditAllowanceType.setVisible(true);
            lblNoOfDaysLOP.setVisible(true);
            txtNoOfDaysLOP.setVisible(true);
            cboParameterBasedOnValue.setVisible(true);
            lblParameterBasedon.setVisible(true);
            cboSubParameterValue.setVisible(true);
            lblSubParameter.setVisible(true);
            //            cboParameterBasedOnValue.setEnabled(true);
            txtCreditAmtValue.setVisible(true);
            lblCreditBasicPay1.setVisible(true);
            cboCreditTypeValue.setEnabled(true);
            txtCreditAmtValue.setEnabled(true);
            chkAdditionalPay.setEnabled(true);
            chkLossOfPay.setEnabled(true); 
        }
    }//GEN-LAST:event_chkLossOfPayActionPerformed
    
    private void tdtToDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateValueFocusLost
        // TODO add your handling code here:
        Date fromEarningDate = DateUtil.getDateMMDDYYYY(tdtFromDateValue.getDateValue());
        Date toEarningDate = DateUtil.getDateMMDDYYYY(tdtToDateValue.getDateValue());
        txtNoOfDaysLOP.setText("");
        System.out.println("@#$@#fromEarningDate:"+fromEarningDate+":toEarningDate:"+toEarningDate);
        if(toEarningDate!= null && toEarningDate.before(fromEarningDate)){
            ClientUtil.showAlertWindow("TO Date Cannot be less than From date!!");
            tdtToDateValue.setDateValue("");
        }
        else if(fromEarningDate!= null && toEarningDate == null ){
            txtNoOfDaysLOP.setText(String.valueOf(1));
        }
        else{  long noOfDays = DateUtil.dateDiff(fromEarningDate,toEarningDate);
        noOfDays = noOfDays +1;
        txtNoOfDaysLOP.setText(String.valueOf(noOfDays));
        System.out.println("#@$#@#$@#$noOfDays"+noOfDays);
        }
    }//GEN-LAST:event_tdtToDateValueFocusLost
    
    private void cboCreditDesigValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCreditDesigValueActionPerformed
        // TODO add your handling code here:
        if(earningsFlag == false){
            if(cboCreditDesigValue.getSelectedIndex()>0){
                //                observable.setCbmType();
                cboCreditTypeValue.setModel(observable.getCbmCreditTypeValue());
            }
        }
    }//GEN-LAST:event_cboCreditDesigValueActionPerformed
    
    private void cboCreditDesigValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboCreditDesigValueFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboCreditDesigValueFocusLost
    
    private void tdtLoanToDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLoanToDateValueFocusLost
        // TODO add your handling code here:
        Date repayFromDate = DateUtil.getDateMMDDYYYY(tdtLoanFromDateValue.getDateValue());
        Date repayToDate = DateUtil.getDateMMDDYYYY(tdtLoanToDateValue.getDateValue());
        if(repayFromDate!=null && repayToDate!=null && (DateUtil.dateDiff(repayFromDate,repayToDate)<0)){
            ClientUtil.showAlertWindow("Repayment from date is greater than to date");
            tdtLoanToDateValue.setDateValue("");
            return;
        }
    }//GEN-LAST:event_tdtLoanToDateValueFocusLost
    
    private void cboCreditTypeValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCreditTypeValueActionPerformed
       String allowanceType = CommonUtil.convertObjToStr(cboCreditTypeValue.getSelectedItem()); 
        if(!allowanceType.equals("LOP") || !allowanceType.equals("ADDITIONAL PAY")){
            HashMap allowanceTypeMap = new HashMap();
            allowanceTypeMap.put("OALLOWANCE_TYPE",allowanceType);
            allowanceTypeMap.put("OAGRADE",CommonUtil.convertObjToStr(cboCreditDesigValue.getSelectedItem()));
            List lst = ClientUtil.executeQuery("getOtherAllowanceAmountDetails", allowanceTypeMap);
            System.out.println("@#$@#$@#$lst"+lst);
            if(lst != null && lst.size() > 0){
                allowanceTypeMap = new HashMap();
                allowanceTypeMap = (HashMap)lst.get(0);
                System.out.println("@#$@#$#@$allowanceTypeMap"+allowanceTypeMap);
                if((CommonUtil.convertObjToStr(allowanceTypeMap.get("BASED_ON_PARAMETER"))).equals("Y")){
                    lblParameterBasedon.setVisible(true);
                    cboParameterBasedOnValue.setVisible(true);
                    lblSubParameter.setVisible(true);
                    cboSubParameterValue.setVisible(true);
                }else if((CommonUtil.convertObjToStr(allowanceTypeMap.get("BASED_ON_PARAMETER"))).equals("N")){
                    lblParameterBasedon.setVisible(false);
                    cboParameterBasedOnValue.setVisible(false);
                    lblSubParameter.setVisible(false);
                    cboSubParameterValue.setVisible(false);
                    String fixedOrPercentage = CommonUtil.convertObjToStr(allowanceTypeMap.get("OATYPE"));
                    System.out.println("!@#!@#!@#fixedOrPercentage"+fixedOrPercentage);
                    if(fixedOrPercentage.equals("FIXED")){
                        System.out.println("#@$@#$@#$@#$in here:"+allowanceTypeMap.get("OAFIXED_AMT"));
                        txtCreditAmtValue.setText(CommonUtil.convertObjToStr(allowanceTypeMap.get("OAFIXED_AMT")));
                    }else{
                        txtCreditAmtValue.setText(CommonUtil.convertObjToStr(allowanceTypeMap.get("OAPERCENTAGE_VALUE")));
                    }
                }
            }else{
//                ClientUtil.showAlertWindow("No Amount defined for this particular Allowance Type!!");
            }
       }
    }//GEN-LAST:event_cboCreditTypeValueActionPerformed
    
    private void btnEarningDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEarningDeleteActionPerformed
        // TODO add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteEarningData(tblEarning.getSelectedRow());
                ClientUtil.clearAll(panEarningDetailsinfo);
                lblCreditEmployeeNameValue.setText("");
                lblCreditDesignationValue.setText("");
                lblCreditEmployeeBranchValue.setText("");
                lblCreditLastIncDateValue.setText("");
                lblCreditnextIncDateValue.setText("");
                ClientUtil.enableDisable(panEarningDetailsinfo,false);
                ClientUtil.enableDisable(panCreditEmployeeId,false);
                btnEarningNew.setEnabled(true);
                btnEarningSave.setEnabled(false);
                btnEarningDelete.setEnabled(false);
                earningsFlag = false;
                updateOBFieldsEarning();
                //                if(tblEarning.getRowCount()>0){
                //                    observable.populateKeyValues();
                //                    populateEarningDetails();
                //                    btnCreditiEmployeeId.setEnabled(false);
                //                    enableDisableEarnings(true);
                //                }
                //                else{
                //                    btnCreditiEmployeeId.setEnabled(false);
                //                    enableDisableEarnings(true);
                //                }
            }else{
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEarningDeleteActionPerformed
    private void populateEarningDetails(){
        txtCreditEmployeeId.setText(observable.getTxtCreditEmployeeId());
        lblCreditEmployeeNameValue.setText(observable.getLblCreditEmployeeNameValue());
        lblCreditDesignationValue.setText(observable.getLblCreditDesignationValue());
        lblCreditEmployeeBranchValue.setText(observable.getLblCreditEmployeeBranchValue());
        txtCreditBasicPay.setText(observable.getTxtCreditBasicPay());
        lblCreditLastIncDateValue.setText(observable.getLblCreditLastIncDateValue());
        lblCreditnextIncDateValue.setText(observable.getLblCreditnextIncDateValue());
        cboCreditDesigValue.setSelectedItem(observable.getCboCreditDesigValue());
    }
    private void populateDeductionDetails(){
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        lblEmployeeNameValue.setText(observable.getLblEmployeeNameValue());
        lblDesignationValue.setText(observable.getLblDesignationValue());
        lblEmployeeBranchValue.setText(observable.getLblEmployeeBranchValue());
    }
    private void tblEarningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEarningMouseClicked
        earningsFlag = true;
        updateOBFieldsEarning();
        observable.setNewEarning(false); 
        final String earning = (String) tblEarning.getValueAt(tblEarning.getSelectedRow(),0);
        final String earnAuthStatus = (String) tblEarning.getValueAt(tblEarning.getSelectedRow(),4);
        System.out.println("#@$@#$@#$earnAuthStatus:"+earnAuthStatus);
        observable.populateEarning(tblEarning.getSelectedRow());
        populateEarnings();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panEarningDetailsinfo,true);
            btnEarningDelete.setEnabled(true);
            btnEarningNew.setEnabled(false);
            btnEarningSave.setEnabled(true);
            cboCreditTypeValue.setModel(observable.getCbmCreditTypeValue());
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(panEarningDetailsinfo,false);
                if(earnAuthStatus.equals("AUTHORIZED")){
                    btnEarningNew.setEnabled(true);
                    btnEarningSave.setEnabled(false);
                    btnEarningDelete.setEnabled(false);
                }else{
                    tdtFromDateValue.setEnabled(true);
                    tdtToDateValue.setEnabled(true);
                    btnEarningNew.setEnabled(false);
                    btnEarningSave.setEnabled(true);
                    btnEarningDelete.setEnabled(true);
                }
            }
            if(CommonUtil.convertObjToStr(cboCreditTypeValue.getSelectedItem()).equals("LOP")){
                chkLossOfPay.setSelected(true);
                chkLossOfPayActionPerformed(null);
            }else if(CommonUtil.convertObjToStr(cboCreditTypeValue.getSelectedItem()).equals("ADDITIONAL PAY")){
                chkAdditionalPay.setSelected(true);
                chkAdditionalPayActionPerformed(null);
            }else{
                chkAdditionalPay.setSelected(false);
                chkLossOfPay.setSelected(false);
                lblParameterBasedon.setVisible(true);
                cboParameterBasedOnValue.setVisible(true);
                lblSubParameter.setVisible(true);
                cboSubParameterValue.setVisible(true);
                txtCreditAmtValue.setVisible(true);
                lblCreditBasicPay1.setVisible(true);
                cboCreditTypeValueActionPerformed(null);
            }
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE /*|| actionType.equals("DeletedDetails") */){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            enableDisableEarnings(false);
            ClientUtil.enableDisable(panEarningDetailsinfo,false);
            ClientUtil.enableDisable(panEarningInfo,false);
            //            btnEarningNew.setEnabled(false);
            //            btnEarningSave.setEnabled(false);
            //            btnEarningDelete.setEnabled(false);
        }
    }//GEN-LAST:event_tblEarningMouseClicked
    
    private void btnEarningNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEarningNewActionPerformed
        
        updateOBFieldsEarning();
        observable.setNewEarning(true);
        observable.resetEarning();
        observable.ttNotifyObservers();
        if(tblEarning.getRowCount()>0){
            observable.populateKeyValues();
            populateEarningDetails();
            btnCreditiEmployeeId.setEnabled(false);
            enableDisableEarnings(true);
        }
        else{
            btnCreditiEmployeeId.setEnabled(true);
            enableDisableEarnings(true);
        }
        cboCreditDesigValue.setEnabled(false);
        btnEarningDelete.setEnabled(false);
        btnEarningNew.setEnabled(false);
        btnEarningSave.setEnabled(true);
        earningsFlag = false;
    }//GEN-LAST:event_btnEarningNewActionPerformed
    private void enableDisableEarnings(boolean earnFlag){
        tdtFromDateValue.setEnabled(earnFlag);
        tdtToDateValue.setEnabled(earnFlag);
        cboCreditDesigValue.setEnabled(earnFlag);
        cboCreditTypeValue.setEnabled(earnFlag);
        chkLossOfPay.setEnabled(earnFlag);
        chkAdditionalPay.setEnabled(earnFlag);
        cboParameterBasedOnValue.setEnabled(earnFlag);
        cboSubParameterValue.setEnabled(earnFlag);
    }
    private void enableDisableDeduction(boolean earnFlag){
        txtEmployeeId.setEnabled(earnFlag);
        ClientUtil.enableDisable(panDeductionType,earnFlag);
        //        lblEmployeeNameValue.setEnabled(earnFlag);
        //        lblDesignationValue.setEnabled(earnFlag);
        //        lblEmployeeBranchValue.setEnabled(earnFlag);
        panDeductionType.setEnabled(earnFlag);
        cboDeductionTypeValue.setEnabled(earnFlag);
    }
    
    private void btnEarningSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEarningSaveActionPerformed
        // TODO add your handling code here:
        if(txtCreditEmployeeId.getText().length() == 0){
            ClientUtil.showAlertWindow("Employee id should not be empty");
        }else if(tdtFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("From Date should not be empty");
//        }else if(cboParameterBasedOnValue.getSelectedIndex() == 0 && cboParameterBasedOnValue.getSelectedIndex()<=0 && chkLossOfPay.isSelected() == false){
//            ClientUtil.showAlertWindow("Parameter based value Should not be empty");
//        }else if(cboSubParameterValue.getSelectedIndex() == 0 && cboSubParameterValue.getSelectedIndex()<=0 && chkLossOfPay.isSelected() == false){
//            ClientUtil.showAlertWindow("Sub parameter value Should not be empty");
        }else{
            updateOBFieldsEarning();
            if(earningsFlag == false){
                observable.insertEarningData(-1,earningsFlag);
            }else{
                observable.insertEarningData(tblEarning.getSelectedRow(),earningsFlag);
            }
            ClientUtil.clearAll(panEarningDetailsinfo);
            lblCreditEmployeeNameValue.setText("");
            lblCreditDesignationValue.setText("");
            lblCreditEmployeeBranchValue.setText("");
            lblCreditLastIncDateValue.setText("");
            lblCreditnextIncDateValue.setText("");
            ClientUtil.enableDisable(panEarningDetailsinfo,false);
            ClientUtil.enableDisable(panCreditEmployeeId,false);
            btnCreditiEmployeeId.setEnabled(false);
            cboCreditTypeValue.setSelectedItem("");
            cboParameterBasedOnValue.setSelectedItem("");
            cboSubParameterValue.setSelectedItem("");
            tdtToDateValue.setDateValue("");
            tdtFromDateValue.setDateValue("");
            txtCreditAmtValue.setText("");
            txtNoOfDaysLOP.setText("");
            ClientUtil.enableDisable(panEarningDetailsinfo,false);
            ClientUtil.enableDisable(panCreditEmployeeId,false);
            btnEarningNew.setEnabled(true);
            btnEarningSave.setEnabled(false);
            btnEarningDelete.setEnabled(false);
            updateOBFieldsEarning();
            earningsFlag = false;
        }
    }//GEN-LAST:event_btnEarningSaveActionPerformed
    
    public long roundOffLower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0){
            roundingFactorOdd +=1;
        }
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2))){
            return lower(number,roundingFactor);
        }else{
            return higher(number,roundingFactor);
        }
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0){
            return number;
        }
        return (number-mod) + roundingFactor ;
    }
    private void cboSubParameterValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubParameterValueActionPerformed
        // TODO add your handling code here:
        
        if(earningsFlag == false){
            if(cboSubParameterValue.getSelectedIndex()>0){
                double amount = observable.setDeductionAmt(txtCreditEmployeeId.getText());
                System.out.println("@!#!@#!@#amount "+amount);
                if(amount == 0){
                    ClientUtil.showAlertWindow("There is no allowance for this parameter!!");
                    txtCreditAmtValue.setText("");
                }else{
                    txtCreditAmtValue.setText(String.valueOf(amount));
                }
                currDt = (Date) currDt.clone();
                long currMonth = currDt.getMonth()+1;
                if(tdtFromDateValue.getDateValue().length()>0){
                    //                Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDateValue.getDateValue()));
                    //                long fromMonth = fromDate.getMonth()+1;
                    //                Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDateValue.getDateValue()));
                    //                long count = DateUtil.dateDiff(fromDate, toDate);
                    //                GregorianCalendar firstdaymonth = new GregorianCalendar(1,fromDate.getMonth()+1,fromDate.getYear()+1900);
                    //                long noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                    //                if(fromMonth == currMonth){
                    //                    double deductionAmt = (double)(amount * count)/noOfDays;
                    //                    deductionAmt = (double)getNearest((long)(deductionAmt *100),100)/100;
                    //                    txtCreditAmtValue.setText(String.valueOf(deductionAmt));
                    //                }else{
                    //
                    //                }
                }
            }
        }
    }//GEN-LAST:event_cboSubParameterValueActionPerformed
    
    private void cboParameterBasedOnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboParameterBasedOnValueActionPerformed
        // TODO add your handling code here:
        //        if(earningsFlag == false){
        if(cboParameterBasedOnValue.getSelectedIndex()>0){
            observable.setCbmProd(CommonUtil.convertObjToStr(cboParameterBasedOnValue.getSelectedItem()));
            cboSubParameterValue.setModel(observable.getCbmSubParameterValue());
        }
        //        }
    }//GEN-LAST:event_cboParameterBasedOnValueActionPerformed
    
    private void txtCreditEmployeeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCreditEmployeeIdFocusLost
        // TODO add your handling code here:
        if(txtCreditEmployeeId.getText().length() > 0){
            HashMap viewMap = new HashMap();
            HashMap where = new HashMap();
            where.put("EMPLOYEE_CODE",txtCreditEmployeeId.getText());
            List lst = ClientUtil.executeQuery("getSelectEmployeeDetails",where);
            if(lst!=null && lst.size()>0){
                where = (HashMap)lst.get(0);
                fillData(where);
            }else{
                ClientUtil.showAlertWindow("Employee Id is not proper");
                txtCreditEmployeeId.setText("");
                lblEmployeeNameValue.setText("");
                lblDesignationValue.setText("");
                lblEmployeeBranchValue.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtCreditEmployeeIdFocusLost
    
    private void btnCreditiEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditiEmployeeIdActionPerformed
        // TODO add your handling code here:
        callView(CREDIT_EMPLOYEE_ID,EARNING);
    }//GEN-LAST:event_btnCreditiEmployeeIdActionPerformed
    
    private void btnCreditingACNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditingACNoActionPerformed
        // TODO add your handling code here:
        callView(ACCT_HEAD,DEDUCTION);
    }//GEN-LAST:event_btnCreditingACNoActionPerformed
    private void updateLoanDetails(){
        txtLoanAccNoValue.setText(CommonUtil.convertObjToStr(observable.getLblLoanAccNoValue()));
        tdtLoanFromDateValue.setDateValue(CommonUtil.convertObjToStr(observable.getTdtLoanFromDateValue()));
        tdtLoanToDateValue.setDateValue(CommonUtil.convertObjToStr(observable.getTdtLoanToDateValue()));
        txtLoanAmountValue.setText(CommonUtil.convertObjToStr(observable.getTxtLoanAmountValue()));
        
        txtInstallmentAmtValue.setText(CommonUtil.convertObjToStr(observable.getTxtInstallmentAmtValue()));
        txtInstIntRate.setText(CommonUtil.convertObjToStr(observable.getTxtInstIntRate()));
        txtIntNetAmount.setText(CommonUtil.convertObjToStr(observable.getTxtIntNetAmount()));
        txtNoofInstallmentsValue.setText(CommonUtil.convertObjToStr(observable.getTxtNoofInstallmentsValue()));
        txtLoanAvailedBranchValue.setText(CommonUtil.convertObjToStr(observable.getTxtLoanAvailedBranchValue()));
        txtLoanDescValue.setText(CommonUtil.convertObjToStr(observable.getLblLoanDescValue()));
        txtLoanStatusValue.setText(CommonUtil.convertObjToStr(observable.getTxtLoanStatusValue()));
        tdtLoanStoppedDateValue.setDateValue(CommonUtil.convertObjToStr(observable.getTdtLoanStoppedDateValue()));
        txtRemarksValue.setText(CommonUtil.convertObjToStr(observable.getTxtRemarksValue()));
    }
    private void txtToDateMMValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateMMValueFocusLost
        // TODO add your handling code here:
        if(txtToDateMMValue.getText().length()>0){
            long toMM = CommonUtil.convertObjToLong(txtToDateMMValue.getText());
            if(toMM>12){
                ClientUtil.showAlertWindow("Month should be less than 12");
                txtToDateMMValue.setText("");
            }
        }
    }//GEN-LAST:event_txtToDateMMValueFocusLost
    private void resetLoanDetails(){
        txtLoanAccNoValue.setText("");
        txtLoanDescValue.setText("");
        tdtLoanFromDateValue.setDateValue("");
        tdtLoanToDateValue.setDateValue("");
        txtLoanAmountValue.setText("");
        txtInstallmentAmtValue.setText("");
        txtNoofInstallmentsValue.setText("");
        txtLoanAvailedBranchValue.setText("");
        tdtLoanStoppedDateValue.setDateValue("");
        txtRemarksValue.setText("");
        txtLoanStatusValue.setText("");
        txtInstIntRate.setText("");
        txtIntNetAmount.setText("");
    }
    private void txtFromDateYYYYValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateYYYYValueFocusLost
        // TODO add your handling code here:
        if(txtToDateYYYYValue.getText().length()>0){
            long fromYYYY = CommonUtil.convertObjToLong(txtFromDateYYYYValue.getText());
            long toYYYY = CommonUtil.convertObjToLong(txtToDateYYYYValue.getText());
            currDt = (Date) currDt.clone();
            if(txtToDateYYYYValue.getText().length()<4){
                ClientUtil.showAlertWindow("Year format should be 4 character");
                txtToDateYYYYValue.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtFromDateYYYYValueFocusLost
    
    private void txtToDateYYYYValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateYYYYValueFocusLost
        // TODO add your handling code here:
        if(txtToDateYYYYValue.getText().length()>0){
            if(txtToDateYYYYValue.getText().length() != 4){
                ClientUtil.showAlertWindow("Year should be a Four Digit value!!");
            }
            if(txtFromDateYYYYValue.getText().length() != 4 || txtFromDateMMValue.getText().length() > 2 || txtToDateMMValue.getText().length() > 2){
                ClientUtil.showAlertWindow("Enter All the periods correctly");
                txtToDateYYYYValue.setText("");
            }
            else{
                int fromDateYear = CommonUtil.convertObjToInt(txtFromDateYYYYValue.getText());
                int toDateMonth = CommonUtil.convertObjToInt(txtToDateMMValue.getText());
                int fromDateMonth = CommonUtil.convertObjToInt(txtFromDateMMValue.getText());
                int toDateYear = CommonUtil.convertObjToInt(txtToDateYYYYValue.getText());
                Date fromDeductionDate = (Date) currDt.clone();
                fromDeductionDate.setDate(1);
                fromDeductionDate.setMonth(fromDateMonth-1);
                fromDeductionDate.setYear(fromDateYear-1900);
                fromDeductionDate = setProperDtFormat(fromDeductionDate);
                Date toDeductionDate = (Date) currDt.clone();
                toDeductionDate.setDate(1);
                toDeductionDate.setMonth(toDateMonth-1);
                toDeductionDate.setYear(toDateYear-1900);
                toDeductionDate = setProperDtFormat(toDeductionDate);
                System.out.println("@!#!@#toDeductionDate:"+toDeductionDate+"fromDeductionDate:"+fromDeductionDate);
                if(fromDeductionDate.after(toDeductionDate)){
                    ClientUtil.showAlertWindow("From date cannot be greater than to date!!");
                    txtToDateYYYYValue.setText("");
                }
            }
        }
    }//GEN-LAST:event_txtToDateYYYYValueFocusLost
    private Date setProperDtFormat(Date dt){
        Date tempDt=(Date)currDt.clone();
        if(dt!=null){
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void rdoDeductionTypeInstallmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeductionTypeInstallmentsActionPerformed
        // TODO add your handling code here:
        rdoDeductionTypeInstallments.setSelected(true);
        rdoDeductionTypeFixed.setSelected(false);
        observable.setRdoButtonLoading("DEDUCTIONS");
        cboDeductionTypeValue.setModel(observable.getCbmDeductionTypeValue());
        cboDeductionTypeValue.setEnabled(true);
        txtFromDateMMValue.setEnabled(true);
        txtFromDateYYYYValue.setEnabled(true);
        txtToDateMMValue.setEnabled(true);
        txtToDateYYYYValue.setEnabled(true);
        btnCreditingACNo.setEnabled(true);
        txtPremiumAmtValue.setEnabled(true);
        lblPremiumAmt.setText("Amount to be deducted");
        //        observable.updateLoanDetails();
        radioButtonEnableDisable(false);
        resetLoanDetails();
        loanDetailsEnableDisable(true);
        if(rdoDeductionTypeInstallments.isSelected() == true){
            //            panLoanValues.setVisible(true);
            //            panLoanDetails.setVisible(true);
            panDeductionLoans.setVisible(true);
            panFixedDetails.setVisible(false);
            txtLoanStatusValue.setEnabled(false);
            txtLoanStatusValue.setText("NEW");
        }else{
            panFixedDetails.setVisible(true);
            //            panLoanValues.setVisible(false);
            //            panLoanDetails.setVisible(false);
            panDeductionLoans.setVisible(false);
        }
        panFixedDetailsVisible(false);
        
    }//GEN-LAST:event_rdoDeductionTypeInstallmentsActionPerformed
    private void loanDetailsEnableDisable(boolean val){
        txtLoanAccNoValue.setEnabled(val);
        txtLoanDescValue.setEnabled(val);
        tdtLoanFromDateValue.setEnabled(val);
        tdtLoanToDateValue.setEnabled(val);
        txtLoanAmountValue.setEnabled(val);
        txtInstallmentAmtValue.setEnabled(val);
        txtNoofInstallmentsValue.setEnabled(val);
        txtLoanAvailedBranchValue.setEnabled(val);
        tdtLoanStoppedDateValue.setEnabled(val);
        txtRemarksValue.setEnabled(val);
        txtLoanStatusValue.setEnabled(val);
        txtInstIntRate.setEnabled(val);
        txtIntNetAmount.setEnabled(val);
    }
    private void rdoDeductionTypeFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeductionTypeFixedActionPerformed
        // TODO add your handling code here:
        rdoDeductionTypeInstallments.setSelected(false);
        rdoDeductionTypeFixed.setSelected(true);
        observable.setRdoButtonLoading("DEDUCTIONS");
        cboDeductionTypeValue.setModel(observable.getCbmDeductionTypeValue());
        cboDeductionTypeValue.setEnabled(true);
        txtFromDateMMValue.setEnabled(true);
        txtFromDateYYYYValue.setEnabled(true);
        txtToDateMMValue.setEnabled(true);
        txtToDateYYYYValue.setEnabled(true);
        btnCreditingACNo.setEnabled(true);
        txtPremiumAmtValue.setEnabled(true);
        lblPremiumAmt.setText("Amount to be deducted");
        radioButtonEnableDisable(true);
        //        panLoanValues.setVisible(false);
        //        panLoanDetails.setVisible(false);
        panDeductionLoans.setVisible(false);
        if(rdoDeductionTypeInstallments.isSelected() == true){
            //            panLoanValues.setVisible(true);
            //            panLoanDetails.setVisible(true);
            panDeductionLoans.setVisible(true);
            panFixedDetails.setVisible(false);
        }else{
            panFixedDetails.setVisible(true);
            //            panLoanValues.setVisible(false);
            //            panLoanDetails.setVisible(false);
            panDeductionLoans.setVisible(false);
        }
        panFixedDetailsVisible(true);
        //        srpLoanDetails.setVisible(false);
    }//GEN-LAST:event_rdoDeductionTypeFixedActionPerformed
    private void radioButtonEnableDisable(boolean val){
        lblDeductionTypeFromDate.setVisible(val);
        txtFromDateMMValue.setVisible(val);
        lblHaltingParameterBasedon3.setVisible(val);
        txtFromDateYYYYValue.setVisible(val);
        lblFromDateFormatValue.setVisible(val);
        lblDeductionTypeToDate.setVisible(val);
        txtToDateMMValue.setVisible(val);
        txtToDateYYYYValue.setVisible(val);
        lblHaltingParameterBasedon2.setVisible(val);
        lblToDateFormatValue.setVisible(val);
        lblPremiumAmt.setVisible(val);
        txtPremiumAmtValue.setVisible(val);
    }
    private void txtFromDateMMValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateMMValueFocusLost
        if(txtFromDateMMValue.getText().length()>0){
            long fromMM = CommonUtil.convertObjToLong(txtFromDateMMValue.getText());
            if(fromMM>12){
                ClientUtil.showAlertWindow("Month should be less than 12");
                txtFromDateMMValue.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtFromDateMMValueFocusLost
    private void updationCredit(int selectDARow) {
        observable.populateEarning(selectDARow);
    }
    
    private void txtEmployeeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmployeeIdFocusLost
        // TODO add your handling code here:
        if(txtEmployeeId.getText().length() > 0){
            HashMap viewMap = new HashMap();
            HashMap where = new HashMap();
            where.put("EMPLOYEE_CODE",txtEmployeeId.getText());
            List lst = ClientUtil.executeQuery("getSelectEmployeeDetails",where);
            if(lst!=null && lst.size()>0){
                where = (HashMap)lst.get(0);
                fillData(where);
            }else{
                ClientUtil.showAlertWindow("Employee Id is not proper");
                txtEmployeeId.setText("");
                lblEmployeeNameValue.setText("");
                lblDesignationValue.setText("");
                lblEmployeeBranchValue.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtEmployeeIdFocusLost
    
    private void btnEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeIdActionPerformed
        // TODO add your handling code here:
        callView(DEDUCTION_EMPLOYEE_ID,DEDUCTION);
    }//GEN-LAST:event_btnEmployeeIdActionPerformed
    private void enableDisableCredit(boolean MAVal){
        btnCreditiEmployeeId.setEnabled(MAVal);
        txtCreditEmployeeId.setEnabled(MAVal);
        txtCreditBasicPay.setEnabled(MAVal);
        tdtFromDateValue.setEnabled(MAVal);
        tdtToDateValue.setEnabled(MAVal);
        cboParameterBasedOnValue.setEnabled(MAVal);
        cboSubParameterValue.setEnabled(MAVal);
        txtCreditAmtValue.setEnabled(MAVal);
        
    }
    
    private void btnDeductionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeductionDeleteActionPerformed
        // TODO add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteDeductionData(tblDeduction.getSelectedRow());
                ClientUtil.clearAll(panEmployeeDetails);
                ClientUtil.clearAll(panFixedDetails);
                ClientUtil.clearAll(panLoanValues);
                ClientUtil.clearAll(panLoanDetails);
                ClientUtil.clearAll(panDeductionLoans);
                ClientUtil.enableDisable(panEmployeeDetails,false);
                ClientUtil.enableDisable(panFixedDetails,false);
                ClientUtil.enableDisable(panLoanValues,false);
                ClientUtil.enableDisable(panLoanDetails,false);
                ClientUtil.enableDisable(panDeductionLoans,false);
                btnDeductionNew.setEnabled(true);
                btnDeductionSave.setEnabled(false);
                btnDeductionDelete.setEnabled(false);
                deductionFlag = false;
                updateOBFieldsDeduction();
            }else{
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnDeductionDeleteActionPerformed
    private void resetValues(){
        lblEmployeeNameValue.setText("");
        txtEmployeeId.setText("");
        lblDesignationValue.setText("");
        lblEmployeeBranchValue.setText("");
    }
    private void tblDeductionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDeductionMouseClicked
        deductionFlag = true;
        updateOBFieldsDeduction();
        observable.setNewDeduction(false);
        final String earning = (String) tblDeduction.getValueAt(tblDeduction.getSelectedRow(),0);
        observable.populateDeduction(tblDeduction.getSelectedRow());
        populateDeduction();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panEarningDetailsinfo,true);
            btnDeductionDelete.setEnabled(true);
            btnDeductionNew.setEnabled(false);
            btnDeductionSave.setEnabled(true);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            enableDisableDeduction(false);
            ClientUtil.enableDisable(panFixedDetails,false);
            ClientUtil.enableDisable(panLoanValues,false);
            ClientUtil.enableDisable(panLoanDetails,false);
            ClientUtil.enableDisable(panDeductionLoans,false);
            
            
        }
        //        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        //        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE /*|| actionType.equals("DeletedDetails") */){
        //           ClientUtil.enableDisable(
        //            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
        //            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        //        }
    }//GEN-LAST:event_tblDeductionMouseClicked
    private void enableDisableAllscreens(boolean allScreen){
        lblDeductionSLNO.setVisible(allScreen);
        lblDeductionSLNOValue.setVisible(allScreen);
    }
    private void btnDeductionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeductionSaveActionPerformed
        // TODO add your handling code here:
        if(txtEmployeeId.getText().length() == 0){
            ClientUtil.showAlertWindow("Employee Id should not be empty");
        }else if(rdoDeductionTypeFixed.isSelected() == false && rdoDeductionTypeInstallments.isSelected() == false){
            ClientUtil.showAlertWindow("Deduction type should be select either Fixed or Installments");
        }
        else if(rdoDeductionTypeFixed.isSelected() == true && txtFromDateMMValue.getText().length() == 0 ){
            ClientUtil.showAlertWindow("From month period should not be empty");
        }else if(rdoDeductionTypeFixed.isSelected() == true && txtFromDateYYYYValue.getText().length() == 0){
            ClientUtil.showAlertWindow("From Year period should not be empty");
        }else if(rdoDeductionTypeFixed.isSelected() == true && txtToDateMMValue.getText().length() == 0){
            ClientUtil.showAlertWindow("To month period should not be empty");
        }else if(rdoDeductionTypeFixed.isSelected() == true && txtToDateYYYYValue.getText().length() == 0){
            ClientUtil.showAlertWindow("To year period should not be empty");
        }else if(rdoDeductionTypeFixed.isSelected() == true && txtPremiumAmtValue.getText().length() == 0 && lossOfPayFlag== false){
            ClientUtil.showAlertWindow("Amount should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && txtLoanAccNoValue.getText().length() == 0 && lossOfPayFlag== false){
            ClientUtil.showAlertWindow("Loan account no Should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && txtLoanAmountValue.getText().length() == 0 && lossOfPayFlag== false){
            ClientUtil.showAlertWindow("Loan Amount should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && txtInstallmentAmtValue.getText().length() == 0 && lossOfPayFlag== false){
            ClientUtil.showAlertWindow("Installment amount should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && txtNoofInstallmentsValue.getText().length() == 0){
            ClientUtil.showAlertWindow("No of installment should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && txtLoanAvailedBranchValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Loan availed branch should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && tdtLoanFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("Loan repay from date should not be empty");
        }else if(rdoDeductionTypeInstallments.isSelected() == true && tdtLoanToDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("Loan repay to date should not be empty");
        }else{
            updateOBFieldsDeduction();
            if(deductionFlag == false){
                observable.insertDeductionData(-1,deductionFlag);
            }else{
                observable.insertDeductionData(tblDeduction.getSelectedRow(),deductionFlag);
            }
            ClientUtil.clearAll(panFixedDetails);
            ClientUtil.clearAll(panLoanValues);
            ClientUtil.clearAll(panLoanDetails);
            ClientUtil.clearAll(panDeductionLoans);
            btnEmployeeId.setEnabled(false);
            updateOBFieldsDeduction();
            btnDeductionNew.setEnabled(true);
            btnDeductionSave.setEnabled(false);
            btnDeductionDelete.setEnabled(false);
            rdoDeductionTypeInstallments.setEnabled(true);
            rdoDeductionTypeFixed.setEnabled(true);
            panFixedDetails.setVisible(false);
            //            panLoanValues.setVisible(false);
            //            panLoanDetails.setVisible(false);
            panDeductionLoans.setVisible(false);
            ClientUtil.enableDisable(panEmployeeDetails,false);
            ClientUtil.enableDisable(panFixedDetails,false);
            ClientUtil.enableDisable(panLoanDetails,false);
            ClientUtil.enableDisable(panDeductionLoans,false);
            ClientUtil.clearAll(panEmployeeDetails);
            deductionFlag = false;
            btnSave.setEnabled(true);
        }
        
    }//GEN-LAST:event_btnDeductionSaveActionPerformed
                    private void btnDeductionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeductionNewActionPerformed
                        // TODO add your handling code here:
                        updateOBFieldsDeduction();
                        observable.setNewDeduction(true);
                        observable.resetDeduction();
                        observable.ttNotifyObservers();
                        if(tblDeduction.getRowCount()>0){
                            observable.populateDeductionKeyValues();
                            populateDeductionDetails();
                            btnEmployeeId.setEnabled(false);
                            enableDisableDeduction(false);
                            ClientUtil.enableDisable(panDeductionType,true);
                            
                        }
                        else{
                            btnEmployeeId.setEnabled(true);
                            enableDisableDeduction(true);
                        }
                        panFixedDetails.setVisible(false);
                        btnEmployeeId.setEnabled(true);
                        btnDeductionDelete.setEnabled(false);
                        btnDeductionNew.setEnabled(false);
                        btnDeductionSave.setEnabled(true);
                        deductionFlag = false;
    }//GEN-LAST:event_btnDeductionNewActionPerformed
                    private void updationDeduction(int selectDARow) {
                        observable.populateDeduction(selectDARow);
                        if(newButtonEnable == false){
                            observable.setCboDeductionTypeValue(CommonUtil.convertObjToStr(tblDeduction.getValueAt(tblDeduction.getSelectedRow(),2)));
                            cboDeductionTypeValue.setSelectedItem(observable.getCboDeductionTypeValue());
                        }
                    }
                    private void resetDeductionForm(){
                        cboDeductionTypeValue.setSelectedItem("");
                        txtFromDateMMValue.setText("");
                        txtFromDateYYYYValue.setText("");
                        txtToDateMMValue.setText("");
                        txtCreditingACNo.setText("");
                        txtToDateYYYYValue.setText("");
                        txtPremiumAmtValue.setText("");
                        
                    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(panEarningInfo.isShowing()==true){
            callView(VIEW,EARNING);
        }
        else if(panDeductions.isShowing()==true){
            callView(VIEW,DEDUCTION);
        }
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
    
    private void updateOBFieldsDeduction(){
        observable.setRdoDeductionTypeFixed(rdoDeductionTypeFixed.isSelected());
        observable.setRdoDeductionTypeInstallments(rdoDeductionTypeInstallments.isSelected());
        observable.setTxtEmployeeId(txtEmployeeId.getText());
        observable.setLblEmployeeNameValue(lblEmployeeNameValue.getText());
        observable.setLblDesignationValue(lblDesignationValue.getText());
        observable.setLblEmployeeBranchValue(lblEmployeeBranchValue.getText());
        observable.setRdoDeductionTypeFixed(rdoDeductionTypeFixed.isSelected());
        observable.setRdoDeductionTypeInstallments(rdoDeductionTypeInstallments.isSelected());
        observable.setCboDeductionTypeValue((String)((ComboBoxModel)this.cboDeductionTypeValue.getModel()).getKeyForSelected());
        observable.setTxtFromDateMMValue(CommonUtil.convertObjToStr(txtFromDateMMValue.getText()));
        observable.setTxtFromDateYYYYValue(CommonUtil.convertObjToStr(txtFromDateYYYYValue.getText()));
        observable.setTxtToDateMMValue(CommonUtil.convertObjToStr(txtToDateMMValue.getText()));
        observable.setTxtToDateYYYYValue(CommonUtil.convertObjToStr(txtToDateYYYYValue.getText()));
        observable.setTxtPremiumAmtValue(CommonUtil.convertObjToStr(txtPremiumAmtValue.getText()));
        observable.setTxtCreditingACNo(txtCreditingACNo.getText());
        observable.setLblLoanAccNoValue(CommonUtil.convertObjToStr(txtLoanAccNoValue.getText()));
        observable.setTdtLoanFromDateValue(CommonUtil.convertObjToStr(tdtLoanFromDateValue.getDateValue()));
        observable.setTdtLoanToDateValue(CommonUtil.convertObjToStr(tdtLoanToDateValue.getDateValue()));
        observable.setTxtLoanAmountValue(CommonUtil.convertObjToStr(txtLoanAmountValue.getText()));
        observable.setTxtInstallmentAmtValue(CommonUtil.convertObjToStr(txtInstallmentAmtValue.getText()));
        observable.setTxtNoofInstallmentsValue(CommonUtil.convertObjToStr(txtNoofInstallmentsValue.getText()));
        observable.setTxtLoanAvailedBranchValue(CommonUtil.convertObjToStr(txtLoanAvailedBranchValue.getText()));
        observable.setLblLoanDescValue(CommonUtil.convertObjToStr(txtLoanDescValue.getText()));
        observable.setTxtLoanStatusValue(CommonUtil.convertObjToStr(txtLoanStatusValue.getText()));
        observable.setTxtInstIntRate(CommonUtil.convertObjToStr(txtInstIntRate.getText()));
        observable.setTxtIntNetAmount(CommonUtil.convertObjToStr(txtIntNetAmount.getText()));
        observable.setTdtLoanStoppedDateValue(CommonUtil.convertObjToStr(tdtLoanStoppedDateValue.getDateValue()));
        observable.setTxtRemarksValue(CommonUtil.convertObjToStr(txtRemarksValue.getText()));
    }
    private void updateOBFieldsEarning(){
        if(tblEarning.getSelectedRow()!= -1) {
            observable.setEarningID(CommonUtil.convertObjToStr(tblEarning.getValueAt(tblEarning.getSelectedRow(),0)));
        }
        observable.setTxtCreditEmployeeId(txtCreditEmployeeId.getText());
        observable.setLblCreditEmployeeNameValue(lblCreditEmployeeNameValue.getText());
        observable.setLblCreditDesignationValue(lblCreditDesignationValue.getText());
        observable.setLblCreditEmployeeBranchValue(lblCreditEmployeeBranchValue.getText());
        observable.setTxtCreditBasicPay(txtCreditBasicPay.getText());
        observable.setLblCreditLastIncDateValue(lblCreditLastIncDateValue.getText());
        observable.setLblCreditnextIncDateValue(lblCreditnextIncDateValue.getText());
        if(((ComboBoxModel)this.cboParameterBasedOnValue.getModel()).getSize()>0){
            observable.setCboParameterBasedOnValue((String)((ComboBoxModel)this.cboParameterBasedOnValue.getModel()).getKeyForSelected());
            if(this.cboSubParameterValue.getSelectedIndex()>0){
                observable.setCboSubParameterValue((String)((ComboBoxModel)this.cboSubParameterValue.getModel()).getKeyForSelected());
            }
        }
        if(((ComboBoxModel)this.cboCreditTypeValue.getModel()).getSize()>0){
            observable.setCboCreditTypeValue((String)((ComboBoxModel)this.cboCreditTypeValue.getModel()).getKeyForSelected());
        }
        if(((ComboBoxModel)this.cboCreditDesigValue.getModel()).getSize()>0){
            observable.setCboCreditDesigValue((String)((ComboBoxModel)this.cboCreditDesigValue.getModel()).getKeyForSelected());
        }
        observable.setTdtFromDateValue(tdtFromDateValue.getDateValue());
        observable.setTdtToDateValue(tdtToDateValue.getDateValue());
        observable.setTxtCreditAmtValue(txtCreditAmtValue.getText());
        observable.setTxtNoOfDaysLOP(txtNoOfDaysLOP.getText());
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
    
    private void populateDeduction(){
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        lblEmployeeNameValue.setText(observable.getLblEmployeeNameValue());
        lblDesignationValue.setText(observable.getLblDesignationValue());
        lblEmployeeBranchValue.setText(observable.getLblEmployeeBranchValue());
        rdoDeductionTypeFixed.setSelected(observable.getRdoDeductionTypeFixed());
        rdoDeductionTypeInstallments.setSelected(observable.getRdoDeductionTypeInstallments());
        //        txtFromDateMMValue.setText(observable.getTxtFromDateMMValue());
        //        txtFromDateYYYYValue.setText(observable.getTxtFromDateYYYYValue());
        //        txtToDateMMValue.setText(observable.getTxtToDateMMValue());
        //        txtToDateYYYYValue.setText(observable.getTxtToDateYYYYValue());
        //        txtPremiumAmtValue.setText(observable.getTxtPremiumAmtValue());
        //        txtCreditingACNo.setText(observable.getTxtCreditingACNo());
        if(observable.getRdoDeductionTypeFixed()== true || observable.getRdoDeductionTypeInstallments() == false){
            ClientUtil.enableDisable(panLoanValues,false);
            ClientUtil.enableDisable(panLoanDetails,false);
            ClientUtil.enableDisable(panDeductionLoans,false);
            ClientUtil.enableDisable(panEmployeeDetails,true);
            ClientUtil.enableDisable(panFixedDetails,true);
            //            panLoanValues.setVisible(false);
            //            panLoanDetails.setVisible(false);
            panDeductionLoans.setVisible(false);
            panFixedDetails.setVisible(true);
            panFixedDetailsVisible(true);
            cboDeductionTypeValue.setSelectedItem(observable.getCbmDeductionTypeValue());
            txtFromDateMMValue.setText(observable.getTxtFromDateMMValue());
            txtFromDateYYYYValue.setText(observable.getTxtFromDateYYYYValue());
            txtToDateMMValue.setText(observable.getTxtToDateMMValue());
            txtToDateYYYYValue.setText(observable.getTxtToDateYYYYValue());
            txtPremiumAmtValue.setText(observable.getTxtPremiumAmtValue());
            txtCreditingACNo.setText(observable.getTxtCreditingACNo());
        }
        else if(observable.getRdoDeductionTypeInstallments() == true || observable.getRdoDeductionTypeFixed()== false){
            ClientUtil.enableDisable(panLoanValues,true);
            ClientUtil.enableDisable(panLoanDetails,true);
            ClientUtil.enableDisable(panDeductionLoans,true);
            ClientUtil.enableDisable(panEmployeeDetails,true);
            ClientUtil.enableDisable(panFixedDetails,false);
            panFixedDetails.setVisible(false);
            //            panLoanValues.setVisible(true);
            //            panLoanDetails.setVisible(true);
            panDeductionLoans.setVisible(true);
            panFixedDetailsVisible(false);
            txtLoanAccNoValue.setText(observable.getLblLoanAccNoValue());
            txtLoanDescValue.setText(observable.getLblLoanDescValue());
            txtLoanAmountValue.setText(observable.getTxtLoanAmountValue());
            txtInstallmentAmtValue.setText(observable.getTxtInstallmentAmtValue());
            txtNoofInstallmentsValue.setText(observable.getTxtNoofInstallmentsValue());
            txtLoanAvailedBranchValue.setText(observable.getTxtLoanAvailedBranchValue());
            tdtLoanFromDateValue.setDateValue(observable.getTdtLoanFromDateValue());
            tdtLoanToDateValue.setDateValue(observable.getTdtLoanToDateValue());
            tdtLoanStoppedDateValue.setDateValue(observable.getTdtLoanStoppedDateValue());
            txtRemarksValue.setText(observable.getTxtRemarksValue());
            txtLoanStatusValue.setText(observable.getTxtLoanStatusValue());
            txtInstIntRate.setText(observable.getTxtInstIntRate());
            txtIntNetAmount.setText(observable.getTxtIntNetAmount());
        }
        
    }
    
    private void panFixedDetailsVisible(boolean value){
        panFixedDetails.setVisible(value);
        lblDeductionTypeFromDate.setVisible(value);
        panFromDateInfo.setVisible(value);
        lblFromDateFormatValue.setVisible(value);
        panToDateInfo.setVisible(value);
        lblDeductionTypeToDate.setVisible(value);
        lblToDateFormatValue.setVisible(value);
        lblPremiumAmt.setVisible(value);
        txtPremiumAmtValue.setVisible(value);
        lblCreditingACNo.setVisible(value);
        panCreditingACNo.setVisible(value);
        txtToDateMMValue.setVisible(value);
        lblHaltingParameterBasedon2.setVisible(value);
        txtToDateYYYYValue.setVisible(value);
        txtFromDateMMValue.setVisible(value);
        lblHaltingParameterBasedon3.setVisible(value);
        txtFromDateYYYYValue.setVisible(value);
        txtPremiumAmtValue.setVisible(value);
        
    }
    private void populateEarnings(){
        txtCreditEmployeeId.setText(observable.getTxtCreditEmployeeId());
        lblCreditEmployeeNameValue.setText(observable.getLblCreditEmployeeNameValue());
        lblCreditDesignationValue.setText(observable.getLblCreditDesignationValue());
        lblCreditEmployeeBranchValue.setText(observable.getLblCreditEmployeeBranchValue());
        txtCreditBasicPay.setText(observable.getTxtCreditBasicPay());
        lblCreditLastIncDateValue.setText(observable.getLblCreditLastIncDateValue());
        lblCreditnextIncDateValue.setText(observable.getLblCreditnextIncDateValue());
        tdtFromDateValue.setDateValue(observable.getTdtFromDateValue());
        tdtToDateValue.setDateValue(observable.getTdtToDateValue());
        txtCreditAmtValue.setText(observable.getTxtCreditAmtValue());
        txtNoOfDaysLOP.setText(observable.getTxtNoOfDaysLOP());
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
    private void resetSalaryForm(){
        //        cboBranchwiseValue.setSelectedItem("");
        //        cboRegionalValue.setSelectedItem("");
        //        txtFromEmpIdValue.setText("");
        //        txtToEmpIdValue.setText("");
        //        txtSalFromDateMMValue.setText("");
        //        txtSalFromDateYYYYValue.setText("");
        //        txtSalToDateMMValue.setText("");
        //        txtSalToDateYYYYValue.setText("");
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
        ClientUtil.enableDisable(panEarningButtons,false);
        ClientUtil.enableDisable(panEmployeeDetails,false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panLoanDetails,false);
        ClientUtil.enableDisable(panDeductionLoans,false);
        ClientUtil.enableDisable(panDeductionButtons,false);
        ClientUtil.enableDisable(panEarningButtons,false);
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
            authorizeStatus = CommonConstants.STATUS_REJECTED;
            status = CommonConstants.STATUS_DELETED;
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        HashMap mapParam = new HashMap();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) && isFilled){
            HashMap authorizeMap = new HashMap();
            if(panEarningInfo.isShowing()==true){
                //                To authorize the Increment Details based on the pan selected
                HashMap authEarning = new HashMap();
                ArrayList arrList = new ArrayList();
                //                authIncrement.put("INCREMENT_ID", observable.getIncrementID());
                authEarning.put("EMP_ID",  observable.getTxtCreditEmployeeId());
                authEarning.put("STATUS",status);
                authEarning.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
                authEarning.put("AUTHORIZE_DATE",currDt.clone());
                authEarning.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                authEarning.put("AUTHORIZE_STATUS",authorizeStatus);
                authEarning.put("EARNING_REASON", "EARNING");
                arrList.add(authEarning);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorizeMap.put("EARNING_SCREEN", "EARNING");
                authorize(authorizeMap,observable.getTxtCreditEmployeeId());
                super.setOpenForEditBy(observable.getStatusBy());
                authEarning = null;
                authorizeMap = null;
            }
            else if(panDeductions.isShowing()==true){
                HashMap authDeduction = new HashMap();
                ArrayList arrList = new ArrayList();
                //                authIncrement.put("INCREMENT_ID", observable.getIncrementID());
                authDeduction.put("EMPLOYEE_ID",  observable.getTxtEmployeeId());
                authDeduction.put("STATUS",status);
                authDeduction.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
                authDeduction.put("AUTHORIZE_DATE",currDt.clone());
                authDeduction.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                authDeduction.put("AUTHORIZE_STATUS",authorizeStatus);
                authDeduction.put("DEDUCTION_REASON", "DEDUCTION");
                arrList.add(authDeduction);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorizeMap.put("DEDUCTION_SCREEN", "DEDUCTION");
                authorize(authorizeMap,observable.getTxtEmployeeId());
                super.setOpenForEditBy(observable.getStatusBy());
                authDeduction = null;
                authorizeMap = null;
            }
            
            //            ClientUtil.execute("updateAuthorizeStatusDeduction",authorizeMap);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            isFilled = false;
            //            }
        }else{
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            //
            if(panEarningInfo.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getEarningsAuthorizeMode");
                btnEarningNew.setEnabled(false);
                btnEarningSave.setEnabled(false);
                btnEarningDelete.setEnabled(false);
            }
            else if(panDeductions.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getDeductionAuthorizeMode");
            }
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            lblStatus.setText(observable.getLblStatus());
            //            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //
            //
            //            HashMap whereMap = new HashMap();
            //            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            //            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            //            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            //            whereMap = null;
            //            mapParam.put(CommonConstants.MAP_NAME, "getDeductionAuthorizeMode");
            //            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            //            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            //            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            authorizeUI.show();
            //            isFilled = true;
        }
    }
    
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get("EARNING_SCREEN") != null) {
            observable.set_authorizeMap(map);
            observable.doAction(pan);
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        else if (map.get("DEDUCTION_SCREEN") != null) {
            observable.set_authorizeMap(map);
            observable.doAction(pan);
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            double totalTableAmt = 0.0;
            if(pan!=-1){
                observable.doAction(pan);
            }
            else if(panEditDelete!=-1){
                observable.doAction(panEditDelete);
            }
            
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            //__ Make the Screen Closable..
            setModified(false);
        }else{
            System.out.print("else condition:");
            ClientUtil.showAlertWindow("Input is not Matching");
            return;
        }
        ClientUtil.enableDisable(panEarningButtons,false);
        ClientUtil.enableDisable(panEmployeeDetails,false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panLoanDetails,false);
        ClientUtil.enableDisable(panDeductionLoans,false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void resetEarningForm(){
        txtCreditEmployeeId.setText("");
        lblCreditEmployeeNameValue.setText("");
        lblCreditDesignationValue.setText("");
        lblCreditEmployeeBranchValue.setText("");
        txtCreditBasicPay.setText("");
        lblCreditLastIncDateValue.setText("");
        lblCreditnextIncDateValue.setText("");
        tdtFromDateValue.setDateValue("");
        tdtToDateValue.setDateValue("");
        cboParameterBasedOnValue.setSelectedItem("");
        cboSubParameterValue.setSelectedItem("");
        txtCreditAmtValue.setText("");
        txtNoOfDaysLOP.setText("");
        btnEarningNew.setEnabled(false);
        btnEarningSave.setEnabled(false);
        btnEarningDelete.setEnabled(false);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        resetDeductionForm();
        resetEarningForm();
        resetLoanDetails();
        
        ClientUtil.clearAll(this);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        btnPrint.setEnabled(true);
        enableDisableAllscreens(false);
        btnEmployeeId.setEnabled(false);
        btnCreditingACNo.setEnabled(false);
        btnDeductionNew.setEnabled(false);
        btnDeductionSave.setEnabled(false);
        btnDeductionDelete.setEnabled(false);
        btnCreditiEmployeeId.setEnabled(false);
        updateLoanValues = false;
        panFixedDetails.setVisible(false);
        panDeductionLoans.setVisible(false);
        ClientUtil.enableDisable(panEarningButtons,false);
        ClientUtil.enableDisable(panEmployeeDetails,false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panLoanDetails,false);
        ClientUtil.enableDisable(panDeductionLoans,false);
        btnNew.setEnabled(true);
        isFilled = false;
        btnDelete.setEnabled(true);
        btnEdit.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateTable(){
        this.tblDeduction.setModel(observable.getTbmDeduction());
        this.tblDeduction.revalidate();
        this.tblEarning.setModel(observable.getTbmEarning());
        this.tblEarning.revalidate();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        if(panEarningInfo.isShowing()==true){
            callView(DELETE,EARNING);
        }
        else if(panDeductions.isShowing()==true){
            callView(DELETE,DEDUCTION);
        }
        btnSave.setEnabled(true);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if(panEarningInfo.isShowing()==true){
            panEditDelete = EARNING;
        }
        else if(panDeductions.isShowing()==true){
            panEditDelete = DEDUCTION;
        }
        resetUI(panEditDelete);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT,panEditDelete);
        btnDelete.setEnabled(false);
        btnPrint.setEnabled(false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panLoanDetails,false);
        ClientUtil.enableDisable(panLoanValues,false);
        ClientUtil.enableDisable(panEmployeeDetails,false);
        lblParameterBasedon.setVisible(false);
        cboParameterBasedOnValue.setVisible(false);
        cboSubParameterValue.setVisible(false);
        lblSubParameter.setVisible(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        if(panDeductions.isShowing()==true){
            panDeductionEnable();    // To enable the deduction panel...
            pan=DEDUCTION;
            btnEmployeeId.setEnabled(false);
        }
        else if(panEarningInfo.isShowing()==true){
            panEarningEnable();   //To Enable the Earning Panel
            pan = EARNING;
            ClientUtil.enableDisable(panCreditEmployeeId,false);
            btnCreditiEmployeeId.setEnabled(false);
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
    private void resetUI(int value){
        //observable.resetTable();
        if(value==DEDUCTION){
            System.out.println("reset DEDUCTION");
            observable.resetDeduction();
            ClientUtil.clearAll(panEarningInfo);
            ClientUtil.enableDisable(panEarningInfo, false);
            ClientUtil.enableDisable(panDeductionButtons,false);
            ClientUtil.enableDisable(panEarningButtons,false);
            btnDeductionNew.setEnabled(true);
            btnEarningNew.setEnabled(false);
            btnEarningSave.setEnabled(false);
            btnEarningDelete.setEnabled(false);
        }
        else if(value == EARNING){
            System.out.println("reset EARNING");
            observable.resetEarning();
            ClientUtil.clearAll(panDeductions);
            ClientUtil.enableDisable(panDeductions,false);
            ClientUtil.enableDisable(panDeductionButtons,false);
            ClientUtil.enableDisable(panEarningButtons,false);
            btnEarningNew.setEnabled(true);
            btnDeductionNew.setEnabled(false);
            btnDeductionSave.setEnabled(false);
            btnDeductionDelete.setEnabled(false);
        }
    }
    
    private void panDeductionEnable(){
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panLoanValues, false);
            ClientUtil.enableDisable(panLoanDetails,false);
            ClientUtil.enableDisable(panDeductionLoans,false);
            ClientUtil.enableDisable(panFixedDetails, false);
            ClientUtil.enableDisable(panEmployeeDetails, false);
            txtEmployeeId.setEnabled(false);
            btnEmployeeId.setEnabled(false);
            btnDeductionNew.setEnabled(true);
        }
        else
            txtEmployeeId.setEnabled(false);
        btnEmployeeId.setEnabled(true);
        ClientUtil.enableDisable(panEmployeeDetails, false);
        ClientUtil.enableDisable(panFixedDetails,false);
        ClientUtil.enableDisable(panCreditInfo,false);
        
    }
    private void panEarningEnable(){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panEarningDetailsinfo, false);
            ClientUtil.enableDisable(panEmployeeId, false);
            btnCreditiEmployeeId.setEnabled(false);
            btnEarningNew.setEnabled(true);
            btnEarningSave.setEnabled(false);
            btnEarningDelete.setEnabled(false);
        }
        else
        { ClientUtil.enableDisable(panEmployeeId, false);
          btnCreditiEmployeeId.setEnabled(true);
          ClientUtil.enableDisable(panEarningDetailsinfo, false);
          ClientUtil.enableDisable(panCreditInfo,false);
          
        }
        
    }
    
    private void allScreensDisable(boolean allDisable){
        ClientUtil.enableDisable(panDeductions,allDisable);
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
        if(viewType == EMPLOYEE_ID || viewType == FROM_EMPLOYEE_ID ||
        viewType == TO_EMPLOYEE_ID){
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmployeeDetails");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if(viewType == DEDUCTION_EMPLOYEE_ID){
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmpForDeduction");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        else if(viewType == CREDIT_EMPLOYEE_ID){
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmpForEarning");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if(viewType == ACCT_HEAD){
            viewMap.put(CommonConstants.MAP_NAME,"Cash.getSelectAcctHead");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if (viewType == EDIT || viewType == DELETE || viewType == VIEW ){
            if(panEditDelete == EARNING){
                viewMap.put(CommonConstants.MAP_NAME,"getEarningEditMode");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }
            else if(panEditDelete == DEDUCTION){
                viewMap.put(CommonConstants.MAP_NAME,"getDeductionEditMode");
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
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                if(viewType == EMPLOYEE_ID){
                    txtEmployeeId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    lblEmployeeNameValue.setText(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    lblDesignationValue.setText(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    lblEmployeeBranchValue.setText(CommonUtil.convertObjToStr(hashMap.get("BRANCH_CODE")));
                    observable.setTxtEmployeeId(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    observable.setLblEmployeeNameValue(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    observable.setLblDesignationValue(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    observable.setLblEmployeeBranchValue(CommonUtil.convertObjToStr(hashMap.get("BRANCH_CODE")));
                    observable.setCustomerId(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                    rdoDeductionTypeFixed.setEnabled(true);
                    rdoDeductionTypeInstallments.setEnabled(true);
                    if(updateLoanValues == false){
                        observable.populateLoanDetails();
                        updateLoanValues = true;
                    }
                }else if(viewType == CREDIT_EMPLOYEE_ID){
                    txtCreditEmployeeId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    lblCreditEmployeeNameValue.setText(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    lblCreditDesignationValue.setText(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    lblCreditEmployeeBranchValue.setText(CommonUtil.convertObjToStr(hashMap.get("BRANCH_ID")));
                    cboCreditDesigValue.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("PRESENT_GRADE")));
                    observable.setTxtCreditEmployeeId(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    observable.setLblCreditEmployeeNameValue(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    observable.setLblCreditDesignationValue(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    observable.setLblCreditEmployeeBranchValue(CommonUtil.convertObjToStr(hashMap.get("BRANCH_ID")));
                    observable.setCustomerId(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                    observable.setCboCreditDesigValue(CommonUtil.convertObjToStr(hashMap.get("PRESENT_GRADE")));
                    observable.employeeBasicDetails();
                    observable.populateData(String.valueOf(hashMap.get("EMPLOYEE_CODE")));
                    txtCreditBasicPay.setText(observable.getTxtCreditBasicPay());
                    lblCreditLastIncDateValue.setText(observable.getLblCreditLastIncDateValue());
                    lblCreditnextIncDateValue.setText(observable.getLblCreditnextIncDateValue());
                }else if(viewType == ACCT_HEAD){
                    System.out.println("@#$@#$@#$hashMap"+hashMap);
                    txtCreditingACNo.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                }else if(viewType == DEDUCTION_EMPLOYEE_ID){
                    txtEmployeeId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    lblEmployeeNameValue.setText(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    lblDesignationValue.setText(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    lblEmployeeBranchValue.setText(CommonUtil.convertObjToStr(hashMap.get("BRANCH_ID")));
                    //                    cboCreditDesigValue.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("PRESENT_GRADE")));
                    observable.setTxtEmployeeId(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    observable.setLblEmployeeNameValue(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    observable.setDesignation(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    observable.setLblEmployeeBranchValue(CommonUtil.convertObjToStr(hashMap.get("BRANCH_ID")));
                }else if(viewType == TO_EMPLOYEE_ID){
                    //                    txtToEmpIdValue.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                }
                txtCreditAmtValue.setEnabled(false);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                this.setButtonEnableDisable();
                if(panEarningInfo.isShowing()==true){
                    panEditDelete=EARNING;
                    observable.getData(String.valueOf(hashMap.get("EMP_ID")),panEditDelete);
                    btnCancel.setEnabled(true);
                }
                else if(panDeductions.isShowing()==true){
                    if(viewType == ACCT_HEAD){
                        System.out.println("@#$@#$@#$hashMap"+hashMap);
                        txtCreditingACNo.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                    }else{
                        panEditDelete=DEDUCTION;
                        observable.getData(String.valueOf(hashMap.get("EMPLOYEE_ID")),panEditDelete);
                        btnCancel.setEnabled(true);
                    }
                }
                btnCancel.setEnabled(true);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                if(panEarningInfo.isShowing()==true){
                    panEditDelete=EARNING;
                    ClientUtil.enableDisable(panEarningDetailsinfo,false);
                    observable.setTxtCreditEmployeeId(CommonUtil.convertObjToStr(hashMap.get("EMP_ID")));
                    observable.getData(String.valueOf(hashMap.get("EMP_ID")),panEditDelete);
                }
                else if(panDeductions.isShowing()==true){
                    panEditDelete=DEDUCTION;
                    ClientUtil.enableDisable(panEmployeeDetails,false);
                    ClientUtil.enableDisable(panFixedDetails,false);
                    observable.setTxtEmployeeId(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_ID")));
                    //                    observable.populateData(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_ID")));
                    observable.getData(String.valueOf(hashMap.get("EMPLOYEE_ID")),panEditDelete);
                }
                btnCancel.setEnabled(true);
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
    public HashMap getMandatoryHashMap(){
        return mandatoryMap;
    }
    public void update(Observable o, Object arg) {
        removeRadioButton();
        this.lblStatus.setText(observable.getLblStatus());
        cboDeductionTypeValue.setModel(observable.getCbmDeductionTypeValue());
        cboParameterBasedOnValue.setModel(observable.getCbmParameterBasedOnValue());
        cboCreditTypeValue.setModel(observable.getCbmCreditTypeValue());
        cboCreditDesigValue.setModel(observable.getCbmCreditDesigValue());
        this.updateTable();
        if(panDeductions.isEnabled()== true){
            populateDeduction();
        }
        if(panEarningInfo.isEnabled() == true){
            populateEarnings();
        }
        addRadioButton();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCreditiEmployeeId;
    private com.see.truetransact.uicomponent.CButton btnCreditingACNo;
    private com.see.truetransact.uicomponent.CButton btnDeductionDelete;
    private com.see.truetransact.uicomponent.CButton btnDeductionNew;
    private com.see.truetransact.uicomponent.CButton btnDeductionSave;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEarningDelete;
    private com.see.truetransact.uicomponent.CButton btnEarningNew;
    private com.see.truetransact.uicomponent.CButton btnEarningSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployeeId;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCreditDesigValue;
    private com.see.truetransact.uicomponent.CComboBox cboCreditTypeValue;
    private com.see.truetransact.uicomponent.CComboBox cboDeductionTypeValue;
    private com.see.truetransact.uicomponent.CComboBox cboParameterBasedOnValue;
    private com.see.truetransact.uicomponent.CComboBox cboSubParameterValue;
    private com.see.truetransact.uicomponent.CCheckBox chkAdditionalPay;
    private com.see.truetransact.uicomponent.CCheckBox chkLossOfPay;
    private com.see.truetransact.uicomponent.CLabel lblAdditionalPay;
    private com.see.truetransact.uicomponent.CLabel lblAllowanceType;
    private com.see.truetransact.uicomponent.CLabel lblCreditAllowanceType;
    private com.see.truetransact.uicomponent.CLabel lblCreditBasicPay;
    private com.see.truetransact.uicomponent.CLabel lblCreditBasicPay1;
    private com.see.truetransact.uicomponent.CLabel lblCreditDesigValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditDesignation;
    private com.see.truetransact.uicomponent.CLabel lblCreditDesignationValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditEmployeeBranch;
    private com.see.truetransact.uicomponent.CLabel lblCreditEmployeeBranchValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblCreditEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblCreditEmployeeNameValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditLastIncDate;
    private com.see.truetransact.uicomponent.CLabel lblCreditLastIncDateValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditSLNO;
    private com.see.truetransact.uicomponent.CLabel lblCreditSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditingACNo;
    private com.see.truetransact.uicomponent.CLabel lblCreditnextIncDate;
    private com.see.truetransact.uicomponent.CLabel lblCreditnextIncDateValue;
    private com.see.truetransact.uicomponent.CLabel lblDeductionSLNO;
    private com.see.truetransact.uicomponent.CLabel lblDeductionSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblDeductionType;
    private com.see.truetransact.uicomponent.CLabel lblDeductionTypeFromDate;
    private com.see.truetransact.uicomponent.CLabel lblDeductionTypeToDate;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDesignationValue;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeBranch;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeBranchValue;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeNameValue;
    private com.see.truetransact.uicomponent.CLabel lblFromDateFormatValue;
    private com.see.truetransact.uicomponent.CLabel lblHaltingFromDate;
    private com.see.truetransact.uicomponent.CLabel lblHaltingParameterBasedon2;
    private com.see.truetransact.uicomponent.CLabel lblHaltingParameterBasedon3;
    private com.see.truetransact.uicomponent.CLabel lblHaltingToDate;
    private com.see.truetransact.uicomponent.CLabel lblInstIntRate;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentAmt;
    private com.see.truetransact.uicomponent.CLabel lblIntNetAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanAccNo;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanAvailedBranch;
    private com.see.truetransact.uicomponent.CLabel lblLoanFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanFromDate2;
    private com.see.truetransact.uicomponent.CLabel lblLoanSanctionAmt;
    private com.see.truetransact.uicomponent.CLabel lblLoanStatus;
    private com.see.truetransact.uicomponent.CLabel lblLoanStoppedDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanToDate;
    private com.see.truetransact.uicomponent.CLabel lblLossOfPay;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfDaysLOP;
    private com.see.truetransact.uicomponent.CLabel lblNoofInstallments;
    private com.see.truetransact.uicomponent.CLabel lblParameterBasedon;
    private com.see.truetransact.uicomponent.CLabel lblPremiumAmt;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
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
    private com.see.truetransact.uicomponent.CLabel lblSubParameter;
    private com.see.truetransact.uicomponent.CLabel lblToDateFormatValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCreditEmployeeId;
    private com.see.truetransact.uicomponent.CPanel panCreditInfo;
    private com.see.truetransact.uicomponent.CPanel panCreditingACNo;
    private com.see.truetransact.uicomponent.CPanel panDeductionButtons;
    private com.see.truetransact.uicomponent.CPanel panDeductionDetails;
    private com.see.truetransact.uicomponent.CPanel panDeductionLoans;
    private com.see.truetransact.uicomponent.CPanel panDeductionTable;
    private com.see.truetransact.uicomponent.CPanel panDeductionType;
    private com.see.truetransact.uicomponent.CPanel panDeductions;
    private com.see.truetransact.uicomponent.CPanel panEarningButtons;
    private com.see.truetransact.uicomponent.CPanel panEarningDetails;
    private com.see.truetransact.uicomponent.CPanel panEarningDetailsinfo;
    private com.see.truetransact.uicomponent.CPanel panEarningInfo;
    private com.see.truetransact.uicomponent.CPanel panEarningTable;
    private com.see.truetransact.uicomponent.CPanel panEmployeeDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeId;
    private com.see.truetransact.uicomponent.CPanel panFixedDetails;
    private com.see.truetransact.uicomponent.CPanel panFromDateInfo;
    private com.see.truetransact.uicomponent.CPanel panLoanDetails;
    private com.see.truetransact.uicomponent.CPanel panLoanValues;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToDateInfo;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDeductionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSalaryDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeductionTypeFixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeductionTypeInstallments;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpDeduction;
    private com.see.truetransact.uicomponent.CScrollPane srpEarning;
    private com.see.truetransact.uicomponent.CTabbedPane tabMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CTable tblDeduction;
    private com.see.truetransact.uicomponent.CTable tblEarning;
    private javax.swing.JToolBar tbrMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CDateField tdtFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtLoanFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtLoanStoppedDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtLoanToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtToDateValue;
    private com.see.truetransact.uicomponent.CTextField txtCreditAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtCreditBasicPay;
    private com.see.truetransact.uicomponent.CTextField txtCreditEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtCreditingACNo;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtFromDateMMValue;
    private com.see.truetransact.uicomponent.CTextField txtFromDateYYYYValue;
    private com.see.truetransact.uicomponent.CTextField txtInstIntRate;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtIntNetAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanAccNoValue;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmountValue;
    private com.see.truetransact.uicomponent.CTextField txtLoanAvailedBranchValue;
    private com.see.truetransact.uicomponent.CTextField txtLoanDescValue;
    private com.see.truetransact.uicomponent.CTextField txtLoanSanctionAmt;
    private com.see.truetransact.uicomponent.CTextField txtLoanStatusValue;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDaysLOP;
    private com.see.truetransact.uicomponent.CTextField txtNoofInstallmentsValue;
    private com.see.truetransact.uicomponent.CTextField txtPremiumAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtRemarksValue;
    private com.see.truetransact.uicomponent.CTextField txtToDateMMValue;
    private com.see.truetransact.uicomponent.CTextField txtToDateYYYYValue;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        //        SalaryStructureUI lui = new SalaryStructureUI();
        JFrame j = new JFrame();
        //        j.getContentPane().add(lui);
        j.setSize(615,600);
        j.show();
        //        lui.show();
    }
    
}

