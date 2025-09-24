/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmpSalaryStructureUI.java
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
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;
import java.util.GregorianCalendar;

/**
 *
 * @author Nikhil
 *
 */

public class EmpSalaryStructureUI extends CInternalFrame implements Observer, UIMandatoryField {
    private String actionType = "";
    private HashMap mandatoryMap;
    private EmpSalaryStructureOB observable;
    private EmpSalaryStructureMRB objMandatoryRB;
    private boolean salDetailsFlag= false;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    private int viewType=-1;
    boolean isFilled = false;
    private final String CLASSNAME = this.getClass().getName();
    boolean flag = false;
    private boolean salSaveButton =  false;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.EmpSalaryStructureRB", ProxyParameters.LANGUAGE);
    
    public EmpSalaryStructureUI() {
        initComponents();
        initSetUp();
    }
    private void initSetUp(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        observable = new EmpSalaryStructureOB();
        btnNew.setEnabled(true);
        setMaxLength();
        initTableData();
        txtAllowanceType.setEnabled(false);
        txtAllowanceID.setEnabled(false);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnEdit.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        this.panSalaryDetails.setEnabled(false);
        
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
        btnTabDelete.setName("btnTabDelete");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        btnView.setName("btnView");
        lbSalToDate.setName("lbSalToDate");
        lblAllowanceAmount.setName("lblAllowanceAmount");
        lblAllowanceID.setName("lblAllowanceID");
        lblAllowanceType.setName("lblAllowanceType");
        lblBasedOnBasicYesNo.setName("lblBasedOnBasicYesNo");
        lblEarnOrDeduction.setName("lblEarnOrDeduction");
        lblFromAmount.setName("lblFromAmount");
        lblMaxAmount.setName("lblMaxAmount");
        lblMsg.setName("lblMsg");
        lblPercentOrFixed.setName("lblPercentOrFixed");
        lblSalFromDate.setName("lblSalFromDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblToAmount.setName("lblToAmount");
        mbrMain.setName("mbrMain");
        panBasedOnBasicYesNo.setName("panBasedOnBasicYesNo");
        panBranchDetailGroup.setName("panBranchDetailGroup");
        panButtons.setName("panButtons");
        panEarnOrDeductionYesNo.setName("panEarnOrDeductionYesNo");
        panGroupData.setName("panGroupData");
        panPercentOrFixed.setName("panPercentOrFixed");
        panSalDetails.setName("panSalDetails");
        panSalStructure.setName("panSalStructure");
        panSalaryDetails.setName("panSalaryDetails");
        panSalaryDetailsTable.setName("panSalaryDetailsTable");
        panSalaryMaster.setName("panSalaryMaster");
        panSalaryMasterDetails.setName("panSalaryMasterDetails");
        panStatus.setName("panStatus");
        rdoBasedOnBasic_No.setName("rdoBasedOnBasic_No");
        rdoBasedOnBasic_Yes.setName("rdoBasedOnBasic_Yes");
        rdoEarnOrDed_Deduction.setName("rdoEarnOrDed_Deduction");
        rdoEarnOrDed_Earning.setName("rdoEarnOrDed_Earning");
        rdoPercentOrFixed_Fixed.setName("rdoPercentOrFixed_Fixed");
        rdoPercentOrFixed_Percent.setName("rdoPercentOrFixed_Percent");
        srpBranch.setName("srpBranch");
        srpGrade.setName("srpGrade");
        srpPopulation.setName("srpPopulation");
        srpSalStructureTable.setName("srpSalStructureTable");
        srpZonal.setName("srpZonal");
        tabBranchDetails.setName("tabBranchDetails");
        tblBranch.setName("tblBranch");
        tblGrade.setName("tblGrade");
        tblPopulation.setName("tblPopulation");
        tblSalStructureTable.setName("tblSalStructureTable");
        tblZonal.setName("tblZonal");
        tdtSalFromDate.setName("tdtSalFromDate");
        tdtSalToDate.setName("tdtSalToDate");
        txtAllowanceAmount.setName("txtAllowanceAmount");
        txtAllowanceID.setName("txtAllowanceID");
        txtAllowanceType.setName("txtAllowanceType");
        txtFromAmount.setName("txtFromAmount");
        txtMaxAmount.setName("txtMaxAmount");
        txtToAmount.setName("txtToAmount");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        rdoPercentOrFixed_Fixed.setText(resourceBundle.getString("rdoPercentOrFixed_Fixed"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblSalFromDate.setText(resourceBundle.getString("lblSalFromDate"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        lblPercentOrFixed.setText(resourceBundle.getString("lblPercentOrFixed"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        rdoBasedOnBasic_Yes.setText(resourceBundle.getString("rdoBasedOnBasic_Yes"));
        lblFromAmount.setText(resourceBundle.getString("lblFromAmount"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        rdoBasedOnBasic_No.setText(resourceBundle.getString("rdoBasedOnBasic_No"));
        ((javax.swing.border.TitledBorder)panSalaryDetails.getBorder()).setTitle(resourceBundle.getString("panSalaryDetails"));
        lblEarnOrDeduction.setText(resourceBundle.getString("lblEarnOrDeduction"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblAllowanceType.setText(resourceBundle.getString("lblAllowanceType"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        lblToAmount.setText(resourceBundle.getString("lblToAmount"));
        lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount1"));
        rdoPercentOrFixed_Percent.setText(resourceBundle.getString("rdoPercentOrFixed_Percent"));
        lbSalToDate.setText(resourceBundle.getString("lbSalToDate"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAllowanceID.setText(resourceBundle.getString("lblAllowanceID"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        ((javax.swing.border.TitledBorder)panSalaryMasterDetails.getBorder()).setTitle(resourceBundle.getString("panSalaryMasterDetails"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        rdoEarnOrDed_Earning.setText(resourceBundle.getString("rdoEarnOrDed_Earning"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblBasedOnBasicYesNo.setText(resourceBundle.getString("lblBasedOnBasicYesNo"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        ((javax.swing.border.TitledBorder)panGroupData.getBorder()).setTitle(resourceBundle.getString("panGroupData"));
        lblMaxAmount.setText(resourceBundle.getString("lblMaxAmount"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        rdoEarnOrDed_Deduction.setText(resourceBundle.getString("rdoEarnOrDed_Deduction"));
    }
    private void initComponentData(){
        
    }
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoEarnOrDed_Earning", new Boolean(true));
        mandatoryMap.put("txtAllowanceType", new Boolean(true));
        mandatoryMap.put("txtAllowanceID", new Boolean(true));
        mandatoryMap.put("rdoPercentOrFixed_Percent", new Boolean(true));
        mandatoryMap.put("txtToAmount", new Boolean(true));
        mandatoryMap.put("txtAllowanceAmount", new Boolean(true));
        mandatoryMap.put("txtMaxAmount", new Boolean(true));
        mandatoryMap.put("txtFromAmount", new Boolean(true));
        mandatoryMap.put("tdtSalFromDate", new Boolean(true));
        //        mandatoryMap.put("tdtSalToDate", new Boolean(true));
        mandatoryMap.put("rdoBasedOnBasic_Yes", new Boolean(true));
    }
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new EmpSalaryStructureMRB();
        rdoEarnOrDed_Earning.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoEarnOrDed_Earning"));
        txtAllowanceType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAllowanceType"));
        txtAllowanceID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAllowanceID"));
        rdoPercentOrFixed_Percent.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPercentOrFixed_Percent"));
        txtToAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToAmount"));
        txtAllowanceAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAllowanceAmount"));
        txtMaxAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmount"));
        txtFromAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromAmount"));
        tdtSalFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSalFromDate"));
        rdoBasedOnBasic_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoBasedOnBasic_Yes"));
    }
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private void setButtonEnableDisable() {
    }
    private void setMaxLength(){
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
        rdoPercentOrFixed_Fixed.setSelected(true);
        rdoPercentOrFixed_Percent.setSelected(false);
        txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
        txtMaxAmount.setValidation(new CurrencyValidation(14,2));
    }
    
    private void removeRadioButtons() {
        rdoBasedOnBasic.remove(rdoBasedOnBasic_No);
        rdoBasedOnBasic.remove(rdoBasedOnBasic_Yes);
        rdoEarnOrDed.remove(rdoEarnOrDed_Deduction);
        rdoEarnOrDed.remove(rdoEarnOrDed_Earning);
        rdoPercentOrFixed.remove(rdoPercentOrFixed_Fixed);
        rdoPercentOrFixed.remove(rdoPercentOrFixed_Percent);
    }
    
    private void addRadioButtons() {// these r all radio button purpose adding...
        rdoBasedOnBasic.add(rdoBasedOnBasic_No);
        rdoBasedOnBasic.add(rdoBasedOnBasic_Yes);
        rdoEarnOrDed.add(rdoEarnOrDed_Deduction);
        rdoEarnOrDed.add(rdoEarnOrDed_Earning);
        rdoPercentOrFixed.add(rdoPercentOrFixed_Fixed);
        rdoPercentOrFixed.add(rdoPercentOrFixed_Percent);
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        if(tblSalStructureTable.getSelectedRow()!= -1) {
            observable.setSlNo(CommonUtil.convertObjToStr(tblSalStructureTable.getValueAt(tblSalStructureTable.getSelectedRow(),0)));
        }
        observable.setTxtAllowanceType(txtAllowanceType.getText());
        observable.setTxtAllowanceID(txtAllowanceID.getText());
        observable.setTxtToAmount(txtToAmount.getText());
        observable.setTxtAllowanceAmount(txtAllowanceAmount.getText());
        observable.setTxtMaxAmount(txtMaxAmount.getText());
        observable.setTxtFromAmount(txtFromAmount.getText());
        observable.setTdtSalFromDate(tdtSalFromDate.getDateValue());
        observable.setTdtSalToDate(tdtSalToDate.getDateValue());
        if(rdoEarnOrDed_Earning.isSelected()==true)
            observable.setRdoEarnOrDed(CommonUtil.convertObjToStr("EARNING"));
        if(rdoEarnOrDed_Deduction.isSelected()==true)
            observable.setRdoEarnOrDed(CommonUtil.convertObjToStr("DEDUCTION"));
        if(rdoBasedOnBasic_No.isSelected()==true)
            observable.setRdoBasedOnBasic(CommonUtil.convertObjToStr("N"));
        if(rdoBasedOnBasic_Yes.isSelected()==true)
            observable.setRdoBasedOnBasic(CommonUtil.convertObjToStr("Y"));
        if(rdoPercentOrFixed_Fixed.isSelected()==true)
            observable.setRdoPercentOrFixed(CommonUtil.convertObjToStr("FIXED"));
        if(rdoPercentOrFixed_Percent.isSelected()==true)
            observable.setRdoPercentOrFixed(CommonUtil.convertObjToStr("PERCENT"));
    }
    public void updateAllowanceDetails(){
        if(observable.getRdoEarnOrDed().equals("EARNING")){
            rdoEarnOrDed_Earning.setSelected(true);
            rdoEarnOrDed_Deduction.setSelected(false);
        }else{
            rdoEarnOrDed_Earning.setSelected(false);
            rdoEarnOrDed_Deduction.setSelected(true);
        }
        if(observable.getRdoPercentOrFixed().equals("PERCENT")){
            rdoPercentOrFixed_Fixed.setSelected(false);
            rdoPercentOrFixed_Percent.setSelected(true);
            txtAllowanceAmount.setValidation(new NumericValidation(2,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount2"));
        }else{
            rdoPercentOrFixed_Fixed.setSelected(true);
            rdoPercentOrFixed_Percent.setSelected(false);
            txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount1"));
        }
        if(observable.getRdoBasedOnBasic().equals("Y")){
            rdoBasedOnBasic_Yes.setSelected(true);
            rdoBasedOnBasic_No.setSelected(false);
        }else{
            rdoBasedOnBasic_Yes.setSelected(false);
            rdoBasedOnBasic_No.setSelected(true);
        }
        txtAllowanceType.setText(observable.getTxtAllowanceType());
        txtAllowanceID.setText(observable.getTxtAllowanceID());
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtAllowanceType.setText(observable.getTxtAllowanceType());
        txtAllowanceID.setText(observable.getTxtAllowanceID());
        txtToAmount.setText(observable.getTxtToAmount());
        txtAllowanceAmount.setText(observable.getTxtAllowanceAmount());
        txtMaxAmount.setText(observable.getTxtMaxAmount());
        txtFromAmount.setText(observable.getTxtFromAmount());
        tdtSalFromDate.setDateValue(observable.getTdtSalFromDate());
        tdtSalToDate.setDateValue(observable.getTdtSalToDate());
        tblBranch.setModel(observable.getTblBranch());
        tblGrade.setModel(observable.getTblGrade());
        tblZonal.setModel(observable.getTblZonal());
        tblPopulation.setModel(observable.getTblPopulation());
        tblSalStructureTable.setModel(observable.getTblSalStruct());
        if(observable.getRdoPercentOrFixed().equals("PERCENT")){
            rdoPercentOrFixed_Fixed.setSelected(false);
            rdoPercentOrFixed_Percent.setSelected(true);
            txtAllowanceAmount.setValidation(new NumericValidation(2,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount2"));
        }else{
            rdoPercentOrFixed_Fixed.setSelected(true);
            rdoPercentOrFixed_Percent.setSelected(false);
            txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount1"));
        }
        if(observable.getRdoBasedOnBasic().equals("Y")){
            rdoBasedOnBasic_Yes.setSelected(true);
            rdoBasedOnBasic_No.setSelected(false);
        }else{
            txtFromAmount.setEnabled(false);
            txtToAmount.setEnabled(false);
            rdoBasedOnBasic_Yes.setSelected(false);
            rdoBasedOnBasic_No.setSelected(true);
        }
        if(observable.getRdoEarnOrDed().equals("EARNING")){
            rdoEarnOrDed_Earning.setSelected(true);
            rdoEarnOrDed_Deduction.setSelected(false);
        }else{
            rdoEarnOrDed_Earning.setSelected(false);
            rdoEarnOrDed_Deduction.setSelected(true);
        }
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoEarnOrDed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoBasedOnBasic = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPercentOrFixed = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrMisecllaniousDeductions = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panSalStructure = new com.see.truetransact.uicomponent.CPanel();
        tabBranchDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panBranchDetailGroup = new com.see.truetransact.uicomponent.CPanel();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpZonal = new com.see.truetransact.uicomponent.CScrollPane();
        tblZonal = new com.see.truetransact.uicomponent.CTable();
        srpBranch = new com.see.truetransact.uicomponent.CScrollPane();
        tblBranch = new com.see.truetransact.uicomponent.CTable();
        srpGrade = new com.see.truetransact.uicomponent.CScrollPane();
        tblGrade = new com.see.truetransact.uicomponent.CTable();
        srpPopulation = new com.see.truetransact.uicomponent.CScrollPane();
        tblPopulation = new com.see.truetransact.uicomponent.CTable();
        panSalaryMasterDetails = new com.see.truetransact.uicomponent.CPanel();
        panEarnOrDeductionYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoEarnOrDed_Earning = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEarnOrDed_Deduction = new com.see.truetransact.uicomponent.CRadioButton();
        lblEarnOrDeduction = new com.see.truetransact.uicomponent.CLabel();
        lblAllowanceType = new com.see.truetransact.uicomponent.CLabel();
        txtAllowanceType = new com.see.truetransact.uicomponent.CTextField();
        lblAllowanceID = new com.see.truetransact.uicomponent.CLabel();
        txtAllowanceID = new com.see.truetransact.uicomponent.CTextField();
        lblBasedOnBasicYesNo = new com.see.truetransact.uicomponent.CLabel();
        panBasedOnBasicYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoBasedOnBasic_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBasedOnBasic_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPercentOrFixed = new com.see.truetransact.uicomponent.CPanel();
        rdoPercentOrFixed_Percent = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPercentOrFixed_Fixed = new com.see.truetransact.uicomponent.CRadioButton();
        lblPercentOrFixed = new com.see.truetransact.uicomponent.CLabel();
        panSalDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalaryDetailsTable = new com.see.truetransact.uicomponent.CPanel();
        panSalaryMaster = new com.see.truetransact.uicomponent.CPanel();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        txtAllowanceAmount = new com.see.truetransact.uicomponent.CTextField();
        txtMaxAmount = new com.see.truetransact.uicomponent.CTextField();
        txtFromAmount = new com.see.truetransact.uicomponent.CTextField();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        lblAllowanceAmount = new com.see.truetransact.uicomponent.CLabel();
        lblMaxAmount = new com.see.truetransact.uicomponent.CLabel();
        lblSalFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSalFromDate = new com.see.truetransact.uicomponent.CDateField();
        lbSalToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSalToDate = new com.see.truetransact.uicomponent.CDateField();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        srpSalStructureTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalStructureTable = new com.see.truetransact.uicomponent.CTable();
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
        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif")));
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

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnCancel);

        lblSpace3.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Close");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Print");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Print");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnException);

        lblSpace4.setText("     ");
        tbrMisecllaniousDeductions.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tbrMisecllaniousDeductions.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
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

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        panSalStructure.setLayout(new java.awt.GridBagLayout());

        panSalStructure.setMinimumSize(new java.awt.Dimension(900, 1400));
        tabBranchDetails.setMinimumSize(new java.awt.Dimension(860, 534));
        tabBranchDetails.setPreferredSize(new java.awt.Dimension(860, 534));
        panBranchDetailGroup.setLayout(new java.awt.GridBagLayout());

        panGroupData.setLayout(new java.awt.GridBagLayout());

        panGroupData.setBorder(new javax.swing.border.TitledBorder("Bank/Branch Details"));
        panGroupData.setMinimumSize(new java.awt.Dimension(844, 300));
        panGroupData.setPreferredSize(new java.awt.Dimension(844, 300));
        srpZonal.setMinimumSize(new java.awt.Dimension(200, 250));
        srpZonal.setPreferredSize(new java.awt.Dimension(200, 250));
        tblZonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "ZonalID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpZonal.setViewportView(tblZonal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpZonal, gridBagConstraints);

        srpBranch.setMinimumSize(new java.awt.Dimension(200, 250));
        srpBranch.setPreferredSize(new java.awt.Dimension(200, 250));
        tblBranch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "BranchID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpBranch.setViewportView(tblBranch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpBranch, gridBagConstraints);

        srpGrade.setMinimumSize(new java.awt.Dimension(200, 250));
        srpGrade.setPreferredSize(new java.awt.Dimension(200, 250));
        tblGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "GradeID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpGrade.setViewportView(tblGrade);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpGrade, gridBagConstraints);

        srpPopulation.setMinimumSize(new java.awt.Dimension(200, 250));
        srpPopulation.setPreferredSize(new java.awt.Dimension(200, 250));
        tblPopulation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Population Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpPopulation.setViewportView(tblPopulation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpPopulation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDetailGroup.add(panGroupData, gridBagConstraints);

        panSalaryMasterDetails.setLayout(new java.awt.GridBagLayout());

        panSalaryMasterDetails.setBorder(new javax.swing.border.TitledBorder("Allowance/Deduction  Details"));
        panSalaryMasterDetails.setMinimumSize(new java.awt.Dimension(800, 130));
        panSalaryMasterDetails.setPreferredSize(new java.awt.Dimension(800, 130));
        panEarnOrDeductionYesNo.setLayout(new java.awt.GridBagLayout());

        panEarnOrDeductionYesNo.setMinimumSize(new java.awt.Dimension(102, 20));
        panEarnOrDeductionYesNo.setName("panMaritalStatus");
        panEarnOrDeductionYesNo.setPreferredSize(new java.awt.Dimension(164, 20));
        rdoEarnOrDed_Earning.setText("Earn");
        rdoEarnOrDed.add(rdoEarnOrDed_Earning);
        rdoEarnOrDed_Earning.setMaximumSize(new java.awt.Dimension(61, 20));
        rdoEarnOrDed_Earning.setMinimumSize(new java.awt.Dimension(61, 20));
        rdoEarnOrDed_Earning.setName("rdoMaritalStatus_Single");
        rdoEarnOrDed_Earning.setPreferredSize(new java.awt.Dimension(61, 15));
        rdoEarnOrDed_Earning.setRolloverEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 28);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panEarnOrDeductionYesNo.add(rdoEarnOrDed_Earning, gridBagConstraints);

        rdoEarnOrDed_Deduction.setText("Ded");
        rdoEarnOrDed.add(rdoEarnOrDed_Deduction);
        rdoEarnOrDed_Deduction.setMaximumSize(new java.awt.Dimension(49, 20));
        rdoEarnOrDed_Deduction.setMinimumSize(new java.awt.Dimension(49, 20));
        rdoEarnOrDed_Deduction.setName("rdoMaritalStatus_Married");
        rdoEarnOrDed_Deduction.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEarnOrDeductionYesNo.add(rdoEarnOrDed_Deduction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panSalaryMasterDetails.add(panEarnOrDeductionYesNo, gridBagConstraints);

        lblEarnOrDeduction.setText("Earn/Ded");
        lblEarnOrDeduction.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 80, 1, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryMasterDetails.add(lblEarnOrDeduction, gridBagConstraints);

        lblAllowanceType.setText("AllowanceType");
        lblAllowanceType.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryMasterDetails.add(lblAllowanceType, gridBagConstraints);

        txtAllowanceType.setMaxLength(128);
        txtAllowanceType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAllowanceType.setName("txtCompany");
        txtAllowanceType.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        panSalaryMasterDetails.add(txtAllowanceType, gridBagConstraints);

        lblAllowanceID.setText("Allowance ID");
        lblAllowanceID.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panSalaryMasterDetails.add(lblAllowanceID, gridBagConstraints);

        txtAllowanceID.setMaxLength(128);
        txtAllowanceID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAllowanceID.setName("txtCompany");
        txtAllowanceID.setValidation(new DefaultValidation()
        );
        txtAllowanceID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAllowanceIDFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryMasterDetails.add(txtAllowanceID, gridBagConstraints);

        lblBasedOnBasicYesNo.setText("Using Basic");
        lblBasedOnBasicYesNo.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryMasterDetails.add(lblBasedOnBasicYesNo, gridBagConstraints);

        panBasedOnBasicYesNo.setLayout(new java.awt.GridBagLayout());

        panBasedOnBasicYesNo.setMinimumSize(new java.awt.Dimension(190, 27));
        panBasedOnBasicYesNo.setName("panMaritalStatus");
        panBasedOnBasicYesNo.setPreferredSize(new java.awt.Dimension(190, 27));
        rdoBasedOnBasic_Yes.setText("Yes");
        rdoBasedOnBasic.add(rdoBasedOnBasic_Yes);
        rdoBasedOnBasic_Yes.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoBasedOnBasic_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoBasedOnBasic_Yes.setName("rdoMaritalStatus_Single");
        rdoBasedOnBasic_Yes.setPreferredSize(new java.awt.Dimension(61, 21));
        rdoBasedOnBasic_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnBasic_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 13);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnBasicYesNo.add(rdoBasedOnBasic_Yes, gridBagConstraints);

        rdoBasedOnBasic_No.setText("No");
        rdoBasedOnBasic.add(rdoBasedOnBasic_No);
        rdoBasedOnBasic_No.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoBasedOnBasic_No.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoBasedOnBasic_No.setName("rdoMaritalStatus_Married");
        rdoBasedOnBasic_No.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoBasedOnBasic_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnBasic_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnBasicYesNo.add(rdoBasedOnBasic_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSalaryMasterDetails.add(panBasedOnBasicYesNo, gridBagConstraints);

        panPercentOrFixed.setLayout(new java.awt.GridBagLayout());

        panPercentOrFixed.setMinimumSize(new java.awt.Dimension(190, 27));
        panPercentOrFixed.setName("panMaritalStatus");
        panPercentOrFixed.setPreferredSize(new java.awt.Dimension(190, 27));
        rdoPercentOrFixed_Percent.setText("PERCENT");
        rdoPercentOrFixed.add(rdoPercentOrFixed_Percent);
        rdoPercentOrFixed_Percent.setMaximumSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.setName("rdoMaritalStatus_Single");
        rdoPercentOrFixed_Percent.setPreferredSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPercentOrFixed_PercentActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPercentOrFixed.add(rdoPercentOrFixed_Percent, gridBagConstraints);

        rdoPercentOrFixed_Fixed.setText("FIXED");
        rdoPercentOrFixed.add(rdoPercentOrFixed_Fixed);
        rdoPercentOrFixed_Fixed.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoPercentOrFixed_Fixed.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoPercentOrFixed_Fixed.setName("rdoMaritalStatus_Married");
        rdoPercentOrFixed_Fixed.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoPercentOrFixed_Fixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPercentOrFixed_FixedActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPercentOrFixed.add(rdoPercentOrFixed_Fixed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panSalaryMasterDetails.add(panPercentOrFixed, gridBagConstraints);

        lblPercentOrFixed.setText("Allowance In");
        lblPercentOrFixed.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryMasterDetails.add(lblPercentOrFixed, gridBagConstraints);

        panBranchDetailGroup.add(panSalaryMasterDetails, new java.awt.GridBagConstraints());

        tabBranchDetails.addTab("Salary Branch Detail", panBranchDetailGroup);

        panSalDetails.setLayout(new java.awt.GridBagLayout());

        panSalDetails.setMinimumSize(new java.awt.Dimension(855, 508));
        panSalDetails.setPreferredSize(new java.awt.Dimension(855, 508));
        panSalaryDetailsTable.setLayout(new java.awt.GridBagLayout());

        panSalaryDetailsTable.setMinimumSize(new java.awt.Dimension(550, 500));
        panSalaryDetailsTable.setPreferredSize(new java.awt.Dimension(550, 500));
        panSalaryMaster.setLayout(new java.awt.GridBagLayout());

        panSalaryMaster.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        panSalaryMaster.setMinimumSize(new java.awt.Dimension(450, 400));
        panSalaryMaster.setPreferredSize(new java.awt.Dimension(450, 400));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        panSalaryDetails.setBorder(new javax.swing.border.TitledBorder("SalaryDetails"));
        panSalaryDetails.setFocusable(false);
        panSalaryDetails.setMinimumSize(new java.awt.Dimension(450, 400));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(450, 400));
        txtToAmount.setMaxLength(128);
        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.setName("txtCompany");
        txtToAmount.setValidation(new DefaultValidation());
        txtToAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 51);
        panSalaryDetails.add(txtToAmount, gridBagConstraints);

        txtAllowanceAmount.setMaxLength(128);
        txtAllowanceAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAllowanceAmount.setName("txtCompany");
        txtAllowanceAmount.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryDetails.add(txtAllowanceAmount, gridBagConstraints);

        txtMaxAmount.setMaxLength(128);
        txtMaxAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxAmount.setName("txtCompany");
        txtMaxAmount.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 51);
        panSalaryDetails.add(txtMaxAmount, gridBagConstraints);

        txtFromAmount.setMaxLength(128);
        txtFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmount.setName("txtCompany");
        txtFromAmount.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryDetails.add(txtFromAmount, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        lblFromAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
        panSalaryDetails.add(lblFromAmount, gridBagConstraints);

        lblToAmount.setText(" To Amount");
        lblToAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(1, 15, 1, 1);
        panSalaryDetails.add(lblToAmount, gridBagConstraints);

        lblAllowanceAmount.setText("Amount");
        lblAllowanceAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryDetails.add(lblAllowanceAmount, gridBagConstraints);

        lblMaxAmount.setText("Max Amount");
        lblMaxAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        panSalaryDetails.add(lblMaxAmount, gridBagConstraints);

        lblSalFromDate.setText("From Date");
        lblSalFromDate.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSalaryDetails.add(lblSalFromDate, gridBagConstraints);

        tdtSalFromDate.setName("tdtPassportValidUpto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 4);
        panSalaryDetails.add(tdtSalFromDate, gridBagConstraints);

        lbSalToDate.setText("To Date");
        lbSalToDate.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryDetails.add(lbSalToDate, gridBagConstraints);

        tdtSalToDate.setName("tdtPassportValidUpto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 7);
        panSalaryDetails.add(tdtSalToDate, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(35, 0, 0, 0);
        panSalaryDetails.add(panButtons, gridBagConstraints);

        panSalaryMaster.add(panSalaryDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 46, 17);
        panSalaryDetailsTable.add(panSalaryMaster, gridBagConstraints);

        srpSalStructureTable.setMinimumSize(new java.awt.Dimension(350, 380));
        srpSalStructureTable.setPreferredSize(new java.awt.Dimension(400, 380));
        tblSalStructureTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AllowanceID", "From Date", "To Date", "From Amount", "To Amount"
            }
        ));
        tblSalStructureTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalStructureTableMousePressed(evt);
            }
        });

        srpSalStructureTable.setViewportView(tblSalStructureTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 46, 0);
        panSalaryDetailsTable.add(srpSalStructureTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalDetails.add(panSalaryDetailsTable, gridBagConstraints);

        tabBranchDetails.addTab("Salary Details", panSalDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalStructure.add(tabBranchDetails, gridBagConstraints);

        getContentPane().add(panSalStructure, java.awt.BorderLayout.CENTER);

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
    }//GEN-END:initComponents
    
    private void txtAllowanceIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAllowanceIDFocusLost
        txtAllowanceID.setText(CommonUtil.convertObjToStr(txtAllowanceID.getText()).toUpperCase());
        HashMap  existMap=new HashMap();
        existMap.put("ALLOWANCE_ID",CommonUtil.convertObjToStr(txtAllowanceID.getText()));
        List lst=ClientUtil.executeQuery("allowaceIdExistanceCheck",existMap);
        if(lst!=null && lst.size()>0){
            existMap=new HashMap();
            existMap=(HashMap)lst.get(0);
            ClientUtil.showMessageWindow("Allowance already Exists: " + CommonUtil.convertObjToStr(existMap.get("ALLOWANCE_ID")));
            txtAllowanceID.setText("");
            existMap=null;
        }
        if(existMap.containsValue("")) {
            ClientUtil.showMessageWindow("ALLOWANCE ID CANNOT BE NULL!!");
        }
    }//GEN-LAST:event_txtAllowanceIDFocusLost
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnAuthorize.setEnabled(false);
        ClientUtil.enableDisable(panSalaryMasterDetails,false);
        disableTable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnView.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    //    To disable the table during Authorization
    private void disableTable(){
        tblGrade.setEnabled(false);
        tblPopulation.setEnabled(false);
        tblZonal.setEnabled(false);
        tblBranch.setEnabled(false);
    }
    //    To Enable tables during Cancelation
    private void enableTable(){
        tblGrade.setEnabled(true);
        tblPopulation.setEnabled(true);
        tblZonal.setEnabled(true);
        tblBranch.setEnabled(true);
    }
    private void txtToAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmountFocusLost
        double fromAmount = CommonUtil.convertObjToDouble(txtFromAmount.getText()).doubleValue();
        double toAmount = CommonUtil.convertObjToDouble(txtToAmount.getText()).doubleValue();
        if(fromAmount > toAmount ){
            ClientUtil.showAlertWindow("To Amount should be greater than From Amount!!!");
            txtToAmount.setText("");
        }
    }//GEN-LAST:event_txtToAmountFocusLost
    
    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        try{
            int rowCount = tblSalStructureTable.getRowCount();
            int rowSelected = tblSalStructureTable.getSelectedRow();
            if((rowCount-1) == rowSelected){
                String alertMsg = "deleteWarningMsg";
                observable.deleteSalaryDetails(tblSalStructureTable.getSelectedRow());
                ClientUtil.clearAll(panSalaryDetails);
                ClientUtil.enableDisable(panSalaryDetails,false);
                btnTabNew.setEnabled(true);
                btnTabSave.setEnabled(false);
                btnTabDelete.setEnabled(false);
                updateOBFields();
                salDetailsFlag = false ;
                salSaveButton = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTabDeleteActionPerformed
    
    private void tblSalStructureTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalStructureTableMousePressed
        updateOBFields();
        salDetailsFlag = true;
        observable.setNewSalaryDetails(false);
        observable.populateSalaryDetails(tblSalStructureTable.getSelectedRow());
        populateSalaryDet();
        observable.ttNotifyObservers();
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panSalaryDetails,true);
            if(observable.getRdoBasedOnBasic().equals("Y")){
                txtFromAmount.setEnabled(true);
                txtToAmount.setEnabled(true);
                btnTabNew.setEnabled(true);
            }else{
                btnTabNew.setEnabled(false);
                txtFromAmount.setEnabled(false);
                txtToAmount.setEnabled(false);
            }
            btnTabDelete.setEnabled(true);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(true);
            salSaveButton = true;
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
        }
    }//GEN-LAST:event_tblSalStructureTableMousePressed
    private void populateSalaryDet() {
        txtAllowanceType.setText(observable.getTxtAllowanceType());
        txtAllowanceID.setText(observable.getTxtAllowanceID());
        txtToAmount.setText(observable.getTxtToAmount());
        txtAllowanceAmount.setText(observable.getTxtAllowanceAmount());
        txtMaxAmount.setText(observable.getTxtMaxAmount());
        txtFromAmount.setText(observable.getTxtFromAmount());
        tdtSalFromDate.setDateValue(observable.getTdtSalFromDate());
        tdtSalToDate.setDateValue(observable.getTdtSalToDate());
        tblBranch.setModel(observable.getTblBranch());
        tblGrade.setModel(observable.getTblGrade());
        tblZonal.setModel(observable.getTblZonal());
        tblPopulation.setModel(observable.getTblPopulation());
        tblSalStructureTable.setModel(observable.getTblSalStruct());
        if(observable.getRdoPercentOrFixed().equals("PERCENT")){
            txtAllowanceAmount.setValidation(new NumericValidation(2,2));
            rdoPercentOrFixed_Fixed.setSelected(false);
            rdoPercentOrFixed_Percent.setSelected(true);
            txtAllowanceAmount.setValidation(new NumericValidation(2,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount2"));
        }else{
            txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
            rdoPercentOrFixed_Fixed.setSelected(true);
            rdoPercentOrFixed_Percent.setSelected(false);
            txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
            lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount1"));
        }
        if(observable.getRdoBasedOnBasic().equals("Y")){
            rdoBasedOnBasic_Yes.setSelected(true);
            rdoBasedOnBasic_No.setSelected(false);
        }else{
            txtFromAmount.setEnabled(false);
            txtToAmount.setEnabled(false);
            rdoBasedOnBasic_Yes.setSelected(false);
            rdoBasedOnBasic_No.setSelected(true);
        }
        if(observable.getRdoEarnOrDed().equals("EARNING")){
            rdoEarnOrDed_Earning.setSelected(true);
            rdoEarnOrDed_Deduction.setSelected(false);
        }else{
            rdoEarnOrDed_Earning.setSelected(false);
            rdoEarnOrDed_Deduction.setSelected(true);
        }
    }
    private void rdoPercentOrFixed_FixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPercentOrFixed_FixedActionPerformed
        txtAllowanceAmount.setValidation(new CurrencyValidation(14,2));
        lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount1"));
        txtAllowanceAmount.setText("");
    }//GEN-LAST:event_rdoPercentOrFixed_FixedActionPerformed
    
    private void rdoPercentOrFixed_PercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPercentOrFixed_PercentActionPerformed
        txtAllowanceAmount.setValidation(new NumericValidation(2,2));
        lblAllowanceAmount.setText(resourceBundle.getString("lblAllowanceAmount2"));
        txtAllowanceAmount.setText("");
    }//GEN-LAST:event_rdoPercentOrFixed_PercentActionPerformed
    
    private void rdoBasedOnBasic_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnBasic_NoActionPerformed
        txtFromAmount.setEnabled(false);
        txtToAmount.setEnabled(false);
        txtFromAmount.setText("");
        txtToAmount.setText("");
    }//GEN-LAST:event_rdoBasedOnBasic_NoActionPerformed
    
    private void rdoBasedOnBasic_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnBasic_YesActionPerformed
        txtFromAmount.setEnabled(true);
        txtToAmount.setEnabled(true);
        txtFromAmount.setText("");
        txtToAmount.setText("");
    }//GEN-LAST:event_rdoBasedOnBasic_YesActionPerformed
    
    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        try{
            updateOBFields();
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSalaryDetails);
            StringBuffer strBAlert = new StringBuffer();
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                strBAlert.append(mandatoryMessage+"\n");
            }else{
                if(salDetailsFlag == false){
                    //if the row is empty
                    observable.salaryDetails(-1,salDetailsFlag);
                }else{
                    observable.salaryDetails(tblSalStructureTable.getSelectedRow(),salDetailsFlag);
                }
                ClientUtil.clearAll(panSalaryDetails);
                ClientUtil.enableDisable(panSalaryDetails,false);
                ClientUtil.enableDisable(panSalaryMasterDetails,true);
                if(observable.getRdoBasedOnBasic().equals("Y")){
                    txtFromAmount.setEnabled(true);
                    txtToAmount.setEnabled(true);
                    btnTabNew.setEnabled(true);
                }else{
                    btnTabNew.setEnabled(false);
                    txtFromAmount.setEnabled(false);
                    txtToAmount.setEnabled(false);
                }
//                btnTabNew.setEnabled(true);
                btnTabSave.setEnabled(false);
                btnTabDelete.setEnabled(false);
                txtAllowanceType.setEditable(false);
                txtAllowanceID.setEditable(false);
                updateOBFields();
                salDetailsFlag = false;
                salSaveButton = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed
    
    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        String allowanceId = CommonUtil.convertObjToStr(txtAllowanceID.getText());
        String allowanceType = CommonUtil.convertObjToStr(txtAllowanceType.getText());
        if((allowanceId.length() >0 && allowanceType.length() > 0) && (rdoEarnOrDed_Deduction.isSelected() || rdoEarnOrDed_Earning.isSelected())){
            updateOBFields();
            
            if(tblSalStructureTable.getRowCount()!= 0){
                double fromAmt = 0.0;
                String fromDate = null;
                String toDate = null;
                for(int i=0;i< tblSalStructureTable.getRowCount() ; i++){
                    fromAmt = CommonUtil.convertObjToDouble(tblSalStructureTable.getValueAt(i, 4)).doubleValue();
                    fromDate = CommonUtil.convertObjToStr(tblSalStructureTable.getValueAt(i, 1));
                    toDate = CommonUtil.convertObjToStr(tblSalStructureTable.getValueAt(i, 2));
                }
                System.out.println("@#$@#$#@$ from AMount:"+fromAmt);
                fromAmt += 1;
                tdtSalFromDate.setDateValue(fromDate);
                tdtSalToDate.setDateValue(toDate);
                tdtSalFromDate.setEnabled(false);
                tdtSalToDate.setEnabled(false);
                txtFromAmount.setText(String.valueOf(fromAmt));
                txtFromAmount.setEnabled(false);
                txtToAmount.setEnabled(true);
                rdoBasedOnBasic_Yes.setSelected(true);
                ClientUtil.enableDisable(panSalDetails,true);
                tdtSalFromDate.setEnabled(false);
                tdtSalToDate.setEnabled(false);
            }
            else{
                txtFromAmount.setText(String.valueOf(1.0));
                txtFromAmount.setEnabled(false);
                txtToAmount.setEnabled(true);
                rdoBasedOnBasic_Yes.setSelected(true);
                ClientUtil.enableDisable(panSalDetails,true);
            }
            salSaveButton = true;
            observable.setNewSalaryDetails(true);
            observable.resetForm();
            observable.ttNotifyObservers();
            txtFromAmount.setEnabled(false);
            btnTabSave.setEnabled(true);
            txtToAmount.setEnabled(true);
            txtFromAmount.setEnabled(false);
            btnTabDelete.setEnabled(false);
            btnTabNew.setEnabled(false);
        }
        else{
            if(allowanceId.length() <=0 || allowanceType.length() <= 0){
                ClientUtil.showAlertWindow("Enter the Allowance ID/Allowance Type before entering new Records!!!");
            }
            else if(rdoEarnOrDed_Deduction.isSelected() == false || rdoEarnOrDed_Earning.isSelected() == false){
                ClientUtil.showAlertWindow("Enter weather it is a Earning Or Deduction!!!");
            }
        }
    }//GEN-LAST:event_btnTabNewActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp("ViewDetails");
        btnCheck();
        disableTable();
    }//GEN-LAST:event_btnViewActionPerformed
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnView.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        ClientUtil.enableDisable(panSalaryMasterDetails,false);
        ClientUtil.enableDisable(panGroupData,false);
        disableTable();
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnView.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        ClientUtil.enableDisable(panSalaryMasterDetails,false);
        ClientUtil.enableDisable(panGroupData,false);
        disableTable();
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnView.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
            singleAuthorizeMap.put("ALLOWANCE_ID",observable.getTxtAllowanceID());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = -1;
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        }else {
            viewType = AUTHORIZE;
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.MAP_NAME, "getSelectSalDetailsAuthorize");
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, whereMap);
            authorizeUI.show();
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            whereMap = null;
        }
    }
    //    This method is used to send the authorize map to the observable
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        String allowanceId = CommonUtil.convertObjToStr(txtAllowanceID.getText());
        String allowanceType = CommonUtil.convertObjToStr(txtAllowanceType.getText());
        if(allowanceId.length() <=0 || allowanceType.length() <= 0){
            ClientUtil.showAlertWindow("Enter the Allowance ID/Allowance Type before entering new Records!!!");
            return;
        }
        else if(rdoEarnOrDed_Deduction.isSelected() == false && rdoEarnOrDed_Earning.isSelected() == false){
            ClientUtil.showAlertWindow("Enter weather it is a Earning Or Deduction!!!");
            return;
        }
        if(observable.checkZonal() == true){
            
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                // clearButton();
                btnCancelActionPerformed(null);
                btnCancel.setEnabled(true);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
            }
        }
        else{
            ClientUtil.showAlertWindow("Please enter the Zonal Branch before Saving!!");
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        //__ To reset the value of the visited tabs...
        tabBranchDetails.resetVisits();
        resetUI();
        //__ New Code...
        ClientUtil.enableDisable(panSalaryDetails,false);
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();   //__ Enables or Disables the buttons and menu Items depending on their previous state...
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(false);
        observable.resetDataList();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);    //__ Sets the Action Type to be performed...
        observable.setStatus(); //__ To set the Value of lblStatus...
        isFilled = false;
        viewType = -1;
        //__ Make the Screen Closable..
        txtAllowanceID.setEditable(true);
        txtAllowanceType.setEditable(true);
        ClientUtil.clearAll(panSalaryMasterDetails);
        tdtSalFromDate.setDateValue("");
        tdtSalToDate.setDateValue("");
        rdoBasedOnBasic_Yes.setSelected(false);
        rdoBasedOnBasic_No.setSelected(false);
        txtFromAmount.setText("");
        txtToAmount.setText("");
        rdoEarnOrDed_Deduction.setSelected(false);
        rdoEarnOrDed_Earning.setSelected(false);
        rdoPercentOrFixed_Percent.setSelected(false);
        rdoPercentOrFixed_Fixed.setSelected(false);
        txtAllowanceAmount.setText("");
        txtMaxAmount.setText("");
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnEdit.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(true);
        lblStatus.setText("          ");
        enableTable();
        salSaveButton = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUI(){
        observable.resetStatus();   //__ To reset the status
        observable.resetForm();     //__ Reset the fields in the UI to null...
        //        observable.resetLable();    //__ Reset the Editable Lables in the UI to null...
        observable.resetBranchTab();
        observable.resetZonalTab();
        observable.resetGradeTab();
        observable.resetPopulationTab();
        observable.resetSalStructureTab();
        observable.setIsTableSet(false);
        enableTable();
    }
    private void tabSaveDeleteButtons(){
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(panBranchDetailGroup,false);
        tblBranch.setEnabled(false);
        tblZonal.setEnabled(false);
        tblGrade.setEnabled(false);
        tblPopulation.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText(observable.getLblStatus());
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(ClientConstants.ACTION_STATUS[2]);
        setModified(true);
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        btnCancel.setEnabled(true);
        ClientUtil.clearAll(panSalDetails);
        ClientUtil.enableDisable(panSalDetails,false);
        btnTabNew.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    
    /** To display customer list popup for Edit & Delete options */
    private void popUp(String actionType){
        this.actionType = actionType;
        if(actionType != null){
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            //            for Edit
            if(actionType.equals(ClientConstants. ACTION_STATUS[2]))  {
                ArrayList lst = new ArrayList();
                lst.add("ALLOWANCE ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSalDetailsList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            //            for Delete
            else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSalDetailsList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            //            for View Button
            else if(actionType.equals("ViewDetails")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSalDetailsViewList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
        }
        
    }
    
    
    /** To get data based on Allowance ID received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash"+hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if(st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
        actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
        getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            HashMap map = new HashMap();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                map.put("ALLOWANCE_ID",hash.get("ALLOWANCE_ID"));
                map.put(CommonConstants.MAP_WHERE, hash.get("ALLOWANCE_ID"));
            }else{
                map.put("ALLOWANCE_ID",hash.get("ALLOWANCE_ID"));
                map.put(CommonConstants.MAP_WHERE, hash.get("ALLOWANCE_ID"));
            }
            //            these methods are called to get all the details during Edit or delete or during Authorize
            observable.getBranchData();
            observable.getGradeData();
            observable.getZonalData();
            observable.getPopulationData();
            observable.getData(map);
            
            updateAllowanceDetails();
            setButtonEnableDisable();
            observable.setStatus();
            if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
            }
            
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panSalDetails, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        observable.resetForm();
        ClientUtil.enableDisable(this, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();
        observable.getBranchData();
        observable.getGradeData();
        observable.getZonalData();
        observable.getPopulationData();
        observable.ttNotifyObservers();
        btnNew.setEnabled(false);
        setModified(true);
        ClientUtil.enableDisable(panButtons,false);
        ClientUtil.enableDisable(panSalaryDetails,false);
        btnTabNew.setEnabled(true);
        txtAllowanceType.setEnabled(true);
        txtAllowanceID.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void initTableData(){
        tblBranch.setModel(observable.getTblBranch());
        tblGrade.setModel(observable.getTblGrade());
        tblZonal.setModel(observable.getTblZonal());
        tblPopulation.setModel(observable.getTblPopulation());
        tblSalStructureTable.setModel(observable.getTblSalStruct());
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        this.btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
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
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSalToDate;
    private com.see.truetransact.uicomponent.CLabel lblAllowanceAmount;
    private com.see.truetransact.uicomponent.CLabel lblAllowanceID;
    private com.see.truetransact.uicomponent.CLabel lblAllowanceType;
    private com.see.truetransact.uicomponent.CLabel lblBasedOnBasicYesNo;
    private com.see.truetransact.uicomponent.CLabel lblEarnOrDeduction;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPercentOrFixed;
    private com.see.truetransact.uicomponent.CLabel lblSalFromDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBasedOnBasicYesNo;
    private com.see.truetransact.uicomponent.CPanel panBranchDetailGroup;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panEarnOrDeductionYesNo;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panPercentOrFixed;
    private com.see.truetransact.uicomponent.CPanel panSalDetails;
    private com.see.truetransact.uicomponent.CPanel panSalStructure;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetailsTable;
    private com.see.truetransact.uicomponent.CPanel panSalaryMaster;
    private com.see.truetransact.uicomponent.CPanel panSalaryMasterDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBasedOnBasic;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnBasic_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnBasic_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEarnOrDed;
    private com.see.truetransact.uicomponent.CRadioButton rdoEarnOrDed_Deduction;
    private com.see.truetransact.uicomponent.CRadioButton rdoEarnOrDed_Earning;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPercentOrFixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoPercentOrFixed_Fixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoPercentOrFixed_Percent;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpBranch;
    private com.see.truetransact.uicomponent.CScrollPane srpGrade;
    private com.see.truetransact.uicomponent.CScrollPane srpPopulation;
    private com.see.truetransact.uicomponent.CScrollPane srpSalStructureTable;
    private com.see.truetransact.uicomponent.CScrollPane srpZonal;
    private com.see.truetransact.uicomponent.CTabbedPane tabBranchDetails;
    private com.see.truetransact.uicomponent.CTable tblBranch;
    private com.see.truetransact.uicomponent.CTable tblGrade;
    private com.see.truetransact.uicomponent.CTable tblPopulation;
    private com.see.truetransact.uicomponent.CTable tblSalStructureTable;
    private com.see.truetransact.uicomponent.CTable tblZonal;
    private javax.swing.JToolBar tbrMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CDateField tdtSalFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalToDate;
    private com.see.truetransact.uicomponent.CTextField txtAllowanceAmount;
    private com.see.truetransact.uicomponent.CTextField txtAllowanceID;
    private com.see.truetransact.uicomponent.CTextField txtAllowanceType;
    private com.see.truetransact.uicomponent.CTextField txtFromAmount;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmount;
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
    
    //    public java.awt.Container getContentPane() {
    //    }
    //
    //    public java.awt.Font getFont() {
    //        Font retValue;
    //
    //        retValue = super.getFont();
    //        return retValue;
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

