/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IncrementsPromotionsUI.java
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
 * @author  
 *
 */

public class IncrementsPromotionsUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    private IncrementsPromotionsOB observable;
    private IncrementsPromotionsMRB objMandatoryRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.IncrementsPromotionsRB", ProxyParameters.LANGUAGE);
    private Date curDate = null;
    private int viewType = -1;
    private boolean _intHANew = false;
    private boolean _intEDNew = false;
    private boolean isFilled = false;
    private boolean salaryStructureSave = false;
    private boolean selectedSingleRow = false;
    private boolean newButtonEnable = false;
    private boolean mouseClickEnable = false;
    private boolean updateLoanValues = false;
    private String SalaryMasterStatus = "";
    private String basedOn = "";
    private String fromDateAlert = "From date should not be empty";
    private String gradeAlert = "Grade should not be empty";
    private String designationAlert = "Designation should not be empty";
    int rowSelected = -1;
    int pan = -1;
    int panEditDelete = -1;
    private int INCREMENT = 1,PROMOTION = 2,ARREARS = 3;
    private int INCREMENT_ID = 1,ACCT_HEAD = 2,EDIT = 3,DELETE = 4,VIEW = 5,PROMOTION_ID = 6,
    FROM_EMPLOYEE_ID = 7, TO_EMPLOYEE_ID = 8;
    /** Creates new instance of class*/
    public IncrementsPromotionsUI() {
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
        //        if(panArrearCalculationInfo.isShowing()==true){
        panTable.setVisible(false);
        ClientUtil.clearAll(panFinalAmount);
        panFinalAmount.setVisible(false);
        txtEmployeeDesig.setVisible(false);
        lblEmployeeDesig.setVisible(false);
        txtEmployeeName.setVisible(false);
        lblEmployeeName.setVisible(false);
        panToEmpId.setVisible(false);
        lblToEmpId.setVisible(false);
        btnIncrementDelete.setEnabled(false);
        btnIncrementNew.setEnabled(false);
        btnIncrementSave.setEnabled(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        initComponentData();
        btnEmployeeId.setEnabled(false);
        txtFromEmpIdValue.setEnabled(false);
        txtEmployeeName.setEnabled(false);
        txtEmployeeDesig.setEnabled(false);
        txtToEmpIdValue.setEnabled(false);
        tdtSalToDateValue.setEnabled(false);
        btnFromEmpId.setEnabled(false);
        btnToEmpId.setEnabled(false);
        btnSalView.setEnabled(false);
        btnSalProcess.setEnabled(false);
        btnClear.setEnabled(false);
        panSalFromDateInfo.setVisible(true);
        lblSalFromDateFormatValue.setVisible(true);;
        curDate = ClientUtil.getCurrentDate();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        resetArrersForm();
        resetForStart();
        btnNew.setEnabled(false);
        btnCancelActionPerformed(null);
    }
    
    
    private void resetForStart(){
        ClientUtil.enableDisable(panArrearCalculationInfo, false);
            ClientUtil.enableDisable(panSalaryInfmn, false);
            ClientUtil.enableDisable(panProcessType,true);
            ClientUtil.enableDisable(panSalFromDateInfo,true);
            Date currDt = (Date) curDate.clone();
            tdtSalToDateValue.setDateValue(CommonUtil.convertObjToStr(currDt));
            tdtSalToDateValue.setEnabled(false);
            panBasedOnCriteria.setVisible(false);
            lblEarnOrDeduction.setVisible(false);
            cboRegionalValue.setVisible(false);
            lblRegional.setVisible(false);
            lblBranchWise.setVisible(false);
            cboBranchwiseValue.setVisible(false);
            lblFromEmpId.setVisible(false);
            panFromEmpId.setVisible(false);
            lblToEmpId.setVisible(false);
            panToEmpId.setVisible(false);
            btnSalView.setVisible(false);
            btnTrialProcess.setVisible(false);
            btnSalProcess.setVisible(false);
            btnClear.setVisible(true);
            btnSalProcess.setEnabled(true);
            lblSalaryID.setVisible(false);
            txtSalaryID.setVisible(false);
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
        lblIncrementEmpId.setName("lblIncrementEmpId");
        txtEmployeeId.setName("txtEmployeeId");
        lblIncrementEmpName.setName("lblIncrementEmpName");
        txtIncrementEmpName.setName("txtIncrementEmpName");
        lblIncrementDesignation.setName("lblIncrementDesignation");
        lblBasicSalary.setName("lblBasicSalary");
        txtIncrementDesignation.setName("txtIncrementDesignation");
        txtBasicSalary.setName("txtBasicSalary");
        lblIncrementDate.setName("lblIncrementDate");
        tdtIncrementDate.setName("tdtIncrementDate");
        lblIncrementEffectiveDate.setName("lblIncrementEffectiveDate");
        tdtIncrementEffectiveDateValue.setName("tdtIncrementEffectiveDateValue");
        lblIncrementCreatedDate.setName("lblIncrementCreatedDate");
        tdtIncrementCreatedDateValue.setName("tdtIncrementCreatedDateValue");
        lblEmployeeGrade.setName("lblEmployeeGrade");
        lblIncrementAmount.setName("lblIncrementAmount");
        lblNewBasic.setName("lblNewBasic");
        lblIncrementNo.setName("lblIncrementNo");
        txtEmployeeGrade.setName("txtEmployeeGrade");
        txtIncrementAmount.setName("txtIncrementAmount");
        txtNewBasic.setName("txtNewBasic");
        txtIncrementNo.setName("txtIncrementNo");
        btnIncrementNew.setName("btnIncrementNew");
        btnIncrementSave.setName("btnIncrementSave");
        btnIncrementDelete.setName("btnIncrementDelete");
        
        lblTotalDeduction.setName("lblTotalDeduction");
        txtTotalDeduction.setName("txtTotalDeduction");
        lblTotalEarnings.setName("lblTotalEarnings");
        txtTotalEarnings.setName("txtTotalEarnings");
        lblNetSalary.setName("lblNetSalary");
        txtNetSalary.setName("txtNetSalary");
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new IncrementsPromotionsRB();
        lblIncrementEmpId.setText(resourceBundle.getString("lblIncrementEmpId"));
        lblIncrementEmpName.setText(resourceBundle.getString("lblIncrementEmpName"));
        lblIncrementDesignation.setText(resourceBundle.getString("lblIncrementDesignation"));
        lblBasicSalary.setText(resourceBundle.getString("lblBasicSalary"));
        lblIncrementDate.setText(resourceBundle.getString("lblIncrementDate"));
        lblIncrementEffectiveDate.setText(resourceBundle.getString("lblIncrementEffectiveDate"));
        lblIncrementCreatedDate.setText(resourceBundle.getString("lblIncrementCreatedDate"));
        lblEmployeeGrade.setText(resourceBundle.getString("lblEmployeeGrade"));
        lblIncrementAmount.setText(resourceBundle.getString("lblIncrementAmount"));
        lblNewBasic.setText(resourceBundle.getString("lblNewBasic"));
        lblIncrementNo.setText(resourceBundle.getString("lblIncrementNo"));
        
    }
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new IncrementsPromotionsMRB();
        tdtIncrementEffectiveDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtIncrementEffectiveDateValue"));
        tdtIncrementCreatedDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtIncrementCreatedDateValue"));
        txtEmployeeGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployeeGrade"));
        txtIncrementAmount.setHelpMessage(lblMsg,objMandatoryRB.getString("txtIncrementAmount"));
        txtNewBasic.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNewBasic"));
        //        txtIncrementNo.setHelpMessage(lblMsg, objMandatoryRB.getSring("txtIncrementNo"));
        tdtIncrementDate.setHelpMessage(lblMsg,objMandatoryRB.getString("tdtIncrementDate"));
    }
    private void initComponentData(){
    }
    
    private void setObservable() {
        observable = IncrementsPromotionsOB.getInstance();
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
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void setMaxLength(){
        txtBasicSalary.setValidation(new CurrencyValidation(10,0));
        txtNetSalary.setValidation(new CurrencyValidation(10,0));
        txtTotalDeduction.setValidation(new CurrencyValidation(10,0));
        txtTotalEarnings.setValidation(new CurrencyValidation(10,0));
        txtIncrementAmount.setValidation(new CurrencyValidation(10,0));
        txtNewBasic.setValidation(new CurrencyValidation(10,0));
        txtSalFromDateMMValue.setMaxLength(2);
        txtSalFromDateYYYYValue.setMaxLength(4);
        txtSalFromDateYYYYValue.setValidation(new NumericValidation());
        txtSalFromDateMMValue.setValidation(new NumericValidation());
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
        rdgTypeOfMode = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgProcessType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBasedOn = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrMisecllaniousDeductions = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMisecllaniousDeductions = new com.see.truetransact.uicomponent.CPanel();
        tabMisecllaniousDeductions = new com.see.truetransact.uicomponent.CTabbedPane();
        panArrearCalculationInfo = new com.see.truetransact.uicomponent.CPanel();
        panEarningInfo1 = new com.see.truetransact.uicomponent.CPanel();
        panProductId1 = new com.see.truetransact.uicomponent.CPanel();
        btnSalView = new com.see.truetransact.uicomponent.CButton();
        btnSalProcess = new com.see.truetransact.uicomponent.CButton();
        btnTrialProcess = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panSalaryInfmn = new com.see.truetransact.uicomponent.CPanel();
        lblRegional = new com.see.truetransact.uicomponent.CLabel();
        cboRegionalValue = new com.see.truetransact.uicomponent.CComboBox();
        lblFromEmpId = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblToEmpId = new com.see.truetransact.uicomponent.CLabel();
        tdtSalToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblBranchWise = new com.see.truetransact.uicomponent.CLabel();
        cboBranchwiseValue = new com.see.truetransact.uicomponent.CComboBox();
        lbSalaryTypeFromDate = new com.see.truetransact.uicomponent.CLabel();
        panSalFromDateInfo = new com.see.truetransact.uicomponent.CPanel();
        lblHaltingParameterBasedon4 = new com.see.truetransact.uicomponent.CLabel();
        txtSalFromDateYYYYValue = new com.see.truetransact.uicomponent.CTextField();
        txtSalFromDateMMValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalFromDateFormatValue = new com.see.truetransact.uicomponent.CLabel();
        panToEmpId = new com.see.truetransact.uicomponent.CPanel();
        txtToEmpIdValue = new com.see.truetransact.uicomponent.CTextField();
        btnToEmpId = new com.see.truetransact.uicomponent.CButton();
        panFromEmpId = new com.see.truetransact.uicomponent.CPanel();
        txtFromEmpIdValue = new com.see.truetransact.uicomponent.CTextField();
        btnFromEmpId = new com.see.truetransact.uicomponent.CButton();
        txtSalaryID = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryID = new com.see.truetransact.uicomponent.CLabel();
        lblEarnOrDeduction = new com.see.truetransact.uicomponent.CLabel();
        lblEarnOrDeduction1 = new com.see.truetransact.uicomponent.CLabel();
        panProcessType = new com.see.truetransact.uicomponent.CPanel();
        rdoProcessTypeTrial = new com.see.truetransact.uicomponent.CRadioButton();
        rdoProcessTypeTrialProcess = new com.see.truetransact.uicomponent.CRadioButton();
        rdoProcessTypeFinal = new com.see.truetransact.uicomponent.CRadioButton();
        panBasedOnCriteria = new com.see.truetransact.uicomponent.CPanel();
        rdoBasedOnZonal = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBasedOnBranch = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBasedOnEmp = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBasedOnAll = new com.see.truetransact.uicomponent.CRadioButton();
        txtEmployeeName = new com.see.truetransact.uicomponent.CTextField();
        lblEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeDesig = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeDesig = new com.see.truetransact.uicomponent.CTextField();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        scrOutputMsg = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryMaster = new com.see.truetransact.uicomponent.CTable();
        scrOutputMsg1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryMasterDtails = new com.see.truetransact.uicomponent.CTable();
        panFinalAmount = new com.see.truetransact.uicomponent.CPanel();
        lblTotalDeduction = new com.see.truetransact.uicomponent.CLabel();
        txtTotalDeduction = new com.see.truetransact.uicomponent.CTextField();
        txtNetSalary = new com.see.truetransact.uicomponent.CTextField();
        lblTotalEarnings = new com.see.truetransact.uicomponent.CLabel();
        lblNetSalary = new com.see.truetransact.uicomponent.CLabel();
        txtTotalEarnings = new com.see.truetransact.uicomponent.CTextField();
        panIncrementDetails = new com.see.truetransact.uicomponent.CPanel();
        panIncrementInfo = new com.see.truetransact.uicomponent.CPanel();
        panIncrementTable = new com.see.truetransact.uicomponent.CPanel();
        srpIncrement = new com.see.truetransact.uicomponent.CScrollPane();
        tblIncrement = new com.see.truetransact.uicomponent.CTable();
        panIncrementDetailsInfo = new com.see.truetransact.uicomponent.CPanel();
        lblIncrementEmpId = new com.see.truetransact.uicomponent.CLabel();
        panIncrementEmpId = new com.see.truetransact.uicomponent.CPanel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeId = new com.see.truetransact.uicomponent.CButton();
        lblIncrementEmpName = new com.see.truetransact.uicomponent.CLabel();
        lblIncrementDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblIncrementDate = new com.see.truetransact.uicomponent.CLabel();
        lblIncrementEffectiveDate = new com.see.truetransact.uicomponent.CLabel();
        tdtIncrementEffectiveDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblIncrementCreatedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtIncrementCreatedDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblEmployeeGrade = new com.see.truetransact.uicomponent.CLabel();
        txtIncrementEmpName = new com.see.truetransact.uicomponent.CTextField();
        txtIncrementDesignation = new com.see.truetransact.uicomponent.CTextField();
        tdtIncrementDate = new com.see.truetransact.uicomponent.CDateField();
        txtBasicSalary = new com.see.truetransact.uicomponent.CTextField();
        lblBasicSalary = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeGrade = new com.see.truetransact.uicomponent.CTextField();
        txtIncrementAmount = new com.see.truetransact.uicomponent.CTextField();
        lblIncrementAmount = new com.see.truetransact.uicomponent.CLabel();
        txtNewBasic = new com.see.truetransact.uicomponent.CTextField();
        lblNewBasic = new com.see.truetransact.uicomponent.CLabel();
        txtIncrementNo = new com.see.truetransact.uicomponent.CTextField();
        lblIncrementNo = new com.see.truetransact.uicomponent.CLabel();
        panIncrementFromDateInfo = new com.see.truetransact.uicomponent.CPanel();
        lblHaltingParameterBasedon5 = new com.see.truetransact.uicomponent.CLabel();
        txtSalFromDateYYYYValue1 = new com.see.truetransact.uicomponent.CTextField();
        txtSalFromDateMMValue1 = new com.see.truetransact.uicomponent.CTextField();
        lblIncrementFromDate = new com.see.truetransact.uicomponent.CLabel();
        panBasedOnCriteria1 = new com.see.truetransact.uicomponent.CPanel();
        rdoIncrementForSingle = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIncrementForAll = new com.see.truetransact.uicomponent.CRadioButton();
        lblIncrementFor = new com.see.truetransact.uicomponent.CLabel();
        panDeductionButtons = new com.see.truetransact.uicomponent.CPanel();
        btnIncrementNew = new com.see.truetransact.uicomponent.CButton();
        btnIncrementSave = new com.see.truetransact.uicomponent.CButton();
        btnIncrementDelete = new com.see.truetransact.uicomponent.CButton();
        btnIncrementProcess = new com.see.truetransact.uicomponent.CButton();
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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMisecllaniousDeductions.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace35);

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

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace36);

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

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Print");
        tbrMisecllaniousDeductions.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace38);

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

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMisecllaniousDeductions.add(lblSpace39);

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

        panArrearCalculationInfo.setMinimumSize(new java.awt.Dimension(750, 230));
        panArrearCalculationInfo.setPreferredSize(new java.awt.Dimension(750, 230));
        panArrearCalculationInfo.setLayout(new java.awt.GridBagLayout());

        panEarningInfo1.setMinimumSize(new java.awt.Dimension(875, 500));
        panEarningInfo1.setPreferredSize(new java.awt.Dimension(875, 500));
        panEarningInfo1.setLayout(new java.awt.GridBagLayout());

        panProductId1.setMinimumSize(new java.awt.Dimension(850, 35));
        panProductId1.setName("panProductId");
        panProductId1.setPreferredSize(new java.awt.Dimension(850, 35));
        panProductId1.setLayout(new java.awt.GridBagLayout());

        btnSalView.setText("Enquiry");
        btnSalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductId1.add(btnSalView, gridBagConstraints);

        btnSalProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnSalProcess.setToolTipText("Start Process");
        btnSalProcess.setMinimumSize(new java.awt.Dimension(63, 27));
        btnSalProcess.setPreferredSize(new java.awt.Dimension(63, 27));
        btnSalProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalProcessActionPerformed(evt);
            }
        });
        btnSalProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSalProcessMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 17);
        panProductId1.add(btnSalProcess, gridBagConstraints);

        btnTrialProcess.setText("Trial Process");
        btnTrialProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrialProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(btnTrialProcess, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setToolTipText("Cancel");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        panProductId1.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        panEarningInfo1.add(panProductId1, gridBagConstraints);

        panSalaryInfmn.setMinimumSize(new java.awt.Dimension(850, 235));
        panSalaryInfmn.setName("panProductId");
        panSalaryInfmn.setPreferredSize(new java.awt.Dimension(850, 235));
        panSalaryInfmn.setLayout(new java.awt.GridBagLayout());

        lblRegional.setText("RO/ZO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panSalaryInfmn.add(lblRegional, gridBagConstraints);

        cboRegionalValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRegionalValue.setPopupWidth(200);
        cboRegionalValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRegionalValueActionPerformed(evt);
            }
        });
        cboRegionalValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRegionalValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryInfmn.add(cboRegionalValue, gridBagConstraints);

        lblFromEmpId.setText("From employee id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryInfmn.add(lblFromEmpId, gridBagConstraints);

        lblToDate.setText("Current date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryInfmn.add(lblToDate, gridBagConstraints);

        lblToEmpId.setText("To employee id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panSalaryInfmn.add(lblToEmpId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryInfmn.add(tdtSalToDateValue, gridBagConstraints);

        lblBranchWise.setText("Branch wise");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryInfmn.add(lblBranchWise, gridBagConstraints);

        cboBranchwiseValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranchwiseValue.setPopupWidth(200);
        cboBranchwiseValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchwiseValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryInfmn.add(cboBranchwiseValue, gridBagConstraints);

        lbSalaryTypeFromDate.setText("Salary for the month :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryInfmn.add(lbSalaryTypeFromDate, gridBagConstraints);

        panSalFromDateInfo.setMinimumSize(new java.awt.Dimension(125, 24));
        panSalFromDateInfo.setPreferredSize(new java.awt.Dimension(125, 24));
        panSalFromDateInfo.setLayout(new java.awt.GridBagLayout());

        lblHaltingParameterBasedon4.setText(" - ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panSalFromDateInfo.add(lblHaltingParameterBasedon4, gridBagConstraints);

        txtSalFromDateYYYYValue.setMinimumSize(new java.awt.Dimension(70, 21));
        txtSalFromDateYYYYValue.setPreferredSize(new java.awt.Dimension(70, 21));
        txtSalFromDateYYYYValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalFromDateYYYYValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panSalFromDateInfo.add(txtSalFromDateYYYYValue, gridBagConstraints);

        txtSalFromDateMMValue.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSalFromDateMMValue.setPreferredSize(new java.awt.Dimension(40, 21));
        txtSalFromDateMMValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalFromDateMMValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panSalFromDateInfo.add(txtSalFromDateMMValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryInfmn.add(panSalFromDateInfo, gridBagConstraints);

        lblSalFromDateFormatValue.setText("MM      -     YYYY");
        lblSalFromDateFormatValue.setMinimumSize(new java.awt.Dimension(125, 18));
        lblSalFromDateFormatValue.setPreferredSize(new java.awt.Dimension(125, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryInfmn.add(lblSalFromDateFormatValue, gridBagConstraints);

        panToEmpId.setMinimumSize(new java.awt.Dimension(130, 21));
        panToEmpId.setPreferredSize(new java.awt.Dimension(130, 21));
        panToEmpId.setLayout(new java.awt.GridBagLayout());

        txtToEmpIdValue.setEditable(false);
        txtToEmpIdValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panToEmpId.add(txtToEmpIdValue, gridBagConstraints);

        btnToEmpId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToEmpId.setToolTipText("From Account");
        btnToEmpId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToEmpId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToEmpId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToEmpIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToEmpId.add(btnToEmpId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        panSalaryInfmn.add(panToEmpId, gridBagConstraints);

        panFromEmpId.setMinimumSize(new java.awt.Dimension(130, 21));
        panFromEmpId.setPreferredSize(new java.awt.Dimension(130, 21));
        panFromEmpId.setLayout(new java.awt.GridBagLayout());

        txtFromEmpIdValue.setEditable(false);
        txtFromEmpIdValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFromEmpId.add(txtFromEmpIdValue, gridBagConstraints);

        btnFromEmpId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromEmpId.setToolTipText("From Account");
        btnFromEmpId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromEmpId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromEmpId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromEmpIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromEmpId.add(btnFromEmpId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panSalaryInfmn.add(panFromEmpId, gridBagConstraints);

        txtSalaryID.setMinimumSize(new java.awt.Dimension(70, 21));
        txtSalaryID.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 77);
        panSalaryInfmn.add(txtSalaryID, gridBagConstraints);

        lblSalaryID.setText("Salary ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 7);
        panSalaryInfmn.add(lblSalaryID, gridBagConstraints);

        lblEarnOrDeduction.setText("Based ON:");
        lblEarnOrDeduction.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 80, 1, 4);
        panSalaryInfmn.add(lblEarnOrDeduction, gridBagConstraints);

        lblEarnOrDeduction1.setText("Process Type:");
        lblEarnOrDeduction1.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 80, 1, 4);
        panSalaryInfmn.add(lblEarnOrDeduction1, gridBagConstraints);

        panProcessType.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcessType.setMinimumSize(new java.awt.Dimension(350, 25));
        panProcessType.setName("panMaritalStatus");
        panProcessType.setPreferredSize(new java.awt.Dimension(350, 25));
        panProcessType.setLayout(new java.awt.GridBagLayout());

        rdgProcessType.add(rdoProcessTypeTrial);
        rdoProcessTypeTrial.setText("TRIAL");
        rdoProcessTypeTrial.setMaximumSize(new java.awt.Dimension(61, 20));
        rdoProcessTypeTrial.setMinimumSize(new java.awt.Dimension(61, 20));
        rdoProcessTypeTrial.setName("rdoMaritalStatus_Single");
        rdoProcessTypeTrial.setPreferredSize(new java.awt.Dimension(61, 15));
        rdoProcessTypeTrial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProcessTypeTrialActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        panProcessType.add(rdoProcessTypeTrial, gridBagConstraints);

        rdgProcessType.add(rdoProcessTypeTrialProcess);
        rdoProcessTypeTrialProcess.setText("TRIAL PROCESS");
        rdoProcessTypeTrialProcess.setMaximumSize(new java.awt.Dimension(250, 20));
        rdoProcessTypeTrialProcess.setMinimumSize(new java.awt.Dimension(130, 20));
        rdoProcessTypeTrialProcess.setName("rdoMaritalStatus_Married");
        rdoProcessTypeTrialProcess.setPreferredSize(new java.awt.Dimension(130, 21));
        rdoProcessTypeTrialProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProcessTypeTrialProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panProcessType.add(rdoProcessTypeTrialProcess, gridBagConstraints);

        rdgProcessType.add(rdoProcessTypeFinal);
        rdoProcessTypeFinal.setText("FINAL");
        rdoProcessTypeFinal.setMaximumSize(new java.awt.Dimension(70, 20));
        rdoProcessTypeFinal.setMinimumSize(new java.awt.Dimension(70, 20));
        rdoProcessTypeFinal.setName("rdoMaritalStatus_Married");
        rdoProcessTypeFinal.setPreferredSize(new java.awt.Dimension(70, 20));
        rdoProcessTypeFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProcessTypeFinalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panProcessType.add(rdoProcessTypeFinal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 5, 0);
        panSalaryInfmn.add(panProcessType, gridBagConstraints);

        panBasedOnCriteria.setMinimumSize(new java.awt.Dimension(350, 25));
        panBasedOnCriteria.setName("panMaritalStatus");
        panBasedOnCriteria.setPreferredSize(new java.awt.Dimension(350, 25));
        panBasedOnCriteria.setLayout(new java.awt.GridBagLayout());

        rdgBasedOn.add(rdoBasedOnZonal);
        rdoBasedOnZonal.setText("Zonal");
        rdoBasedOnZonal.setMaximumSize(new java.awt.Dimension(61, 20));
        rdoBasedOnZonal.setMinimumSize(new java.awt.Dimension(61, 20));
        rdoBasedOnZonal.setName("rdoMaritalStatus_Single");
        rdoBasedOnZonal.setPreferredSize(new java.awt.Dimension(61, 15));
        rdoBasedOnZonal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnZonalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        panBasedOnCriteria.add(rdoBasedOnZonal, gridBagConstraints);

        rdgBasedOn.add(rdoBasedOnBranch);
        rdoBasedOnBranch.setText("Branch");
        rdoBasedOnBranch.setMaximumSize(new java.awt.Dimension(70, 20));
        rdoBasedOnBranch.setMinimumSize(new java.awt.Dimension(70, 20));
        rdoBasedOnBranch.setName("rdoMaritalStatus_Married");
        rdoBasedOnBranch.setPreferredSize(new java.awt.Dimension(70, 20));
        rdoBasedOnBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnCriteria.add(rdoBasedOnBranch, gridBagConstraints);

        rdgBasedOn.add(rdoBasedOnEmp);
        rdoBasedOnEmp.setText("Employee");
        rdoBasedOnEmp.setMaximumSize(new java.awt.Dimension(90, 20));
        rdoBasedOnEmp.setMinimumSize(new java.awt.Dimension(90, 20));
        rdoBasedOnEmp.setName("rdoMaritalStatus_Married");
        rdoBasedOnEmp.setPreferredSize(new java.awt.Dimension(90, 20));
        rdoBasedOnEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnCriteria.add(rdoBasedOnEmp, gridBagConstraints);

        rdgBasedOn.add(rdoBasedOnAll);
        rdoBasedOnAll.setText("All");
        rdoBasedOnAll.setMaximumSize(new java.awt.Dimension(70, 20));
        rdoBasedOnAll.setMinimumSize(new java.awt.Dimension(70, 20));
        rdoBasedOnAll.setName("rdoMaritalStatus_Married");
        rdoBasedOnAll.setPreferredSize(new java.awt.Dimension(70, 20));
        rdoBasedOnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnCriteria.add(rdoBasedOnAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 0);
        panSalaryInfmn.add(panBasedOnCriteria, gridBagConstraints);

        txtEmployeeName.setEditable(false);
        txtEmployeeName.setMinimumSize(new java.awt.Dimension(120, 21));
        txtEmployeeName.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryInfmn.add(txtEmployeeName, gridBagConstraints);

        lblEmployeeName.setText("Employee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryInfmn.add(lblEmployeeName, gridBagConstraints);

        lblEmployeeDesig.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panSalaryInfmn.add(lblEmployeeDesig, gridBagConstraints);

        txtEmployeeDesig.setEditable(false);
        txtEmployeeDesig.setMinimumSize(new java.awt.Dimension(120, 21));
        txtEmployeeDesig.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSalaryInfmn.add(txtEmployeeDesig, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEarningInfo1.add(panSalaryInfmn, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(850, 115));
        panTable.setPreferredSize(new java.awt.Dimension(850, 115));
        panTable.setLayout(new java.awt.GridBagLayout());

        scrOutputMsg.setMinimumSize(new java.awt.Dimension(30, 60));
        scrOutputMsg.setPreferredSize(new java.awt.Dimension(30, 60));

        tblSalaryMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "EMP ID", "BASIC", "SAL FOR MONTH", "CREATED ON", "STATUS"
            }
        ));
        tblSalaryMaster.setMinimumSize(new java.awt.Dimension(200, 2000));
        tblSalaryMaster.setPreferredScrollableViewportSize(new java.awt.Dimension(26, 56));
        tblSalaryMaster.setReorderingAllowed(false);
        tblSalaryMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalaryMasterMousePressed(evt);
            }
        });
        scrOutputMsg.setViewportView(tblSalaryMaster);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 9, 0);
        panTable.add(scrOutputMsg, gridBagConstraints);

        scrOutputMsg1.setMinimumSize(new java.awt.Dimension(10, 30));
        scrOutputMsg1.setPreferredSize(new java.awt.Dimension(10, 30));

        tblSalaryMasterDtails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "SALARY TYPE", "AMOUNT", "EARN/DED"
            }
        ));
        tblSalaryMasterDtails.setMinimumSize(new java.awt.Dimension(200, 400));
        tblSalaryMasterDtails.setPreferredScrollableViewportSize(new java.awt.Dimension(6, 26));
        tblSalaryMasterDtails.setReorderingAllowed(false);
        scrOutputMsg1.setViewportView(tblSalaryMasterDtails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 56, 8, 25);
        panTable.add(scrOutputMsg1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panEarningInfo1.add(panTable, gridBagConstraints);

        panFinalAmount.setMinimumSize(new java.awt.Dimension(850, 35));
        panFinalAmount.setName("panProductId");
        panFinalAmount.setPreferredSize(new java.awt.Dimension(850, 35));
        panFinalAmount.setLayout(new java.awt.GridBagLayout());

        lblTotalDeduction.setText("Total Deduction :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panFinalAmount.add(lblTotalDeduction, gridBagConstraints);

        txtTotalDeduction.setMinimumSize(new java.awt.Dimension(150, 21));
        txtTotalDeduction.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 9);
        panFinalAmount.add(txtTotalDeduction, gridBagConstraints);

        txtNetSalary.setMinimumSize(new java.awt.Dimension(150, 21));
        txtNetSalary.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panFinalAmount.add(txtNetSalary, gridBagConstraints);

        lblTotalEarnings.setText("Total Earnings :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 0);
        panFinalAmount.add(lblTotalEarnings, gridBagConstraints);

        lblNetSalary.setText("Net Salary :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panFinalAmount.add(lblNetSalary, gridBagConstraints);

        txtTotalEarnings.setMinimumSize(new java.awt.Dimension(70, 21));
        txtTotalEarnings.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 9);
        panFinalAmount.add(txtTotalEarnings, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 24, 0);
        panEarningInfo1.add(panFinalAmount, gridBagConstraints);

        panArrearCalculationInfo.add(panEarningInfo1, new java.awt.GridBagConstraints());

        tabMisecllaniousDeductions.addTab("Salary Calculation", panArrearCalculationInfo);

        panIncrementDetails.setMinimumSize(new java.awt.Dimension(800, 640));
        panIncrementDetails.setPreferredSize(new java.awt.Dimension(800, 640));
        panIncrementDetails.setLayout(new java.awt.GridBagLayout());

        panIncrementInfo.setMinimumSize(new java.awt.Dimension(850, 350));
        panIncrementInfo.setPreferredSize(new java.awt.Dimension(850, 350));
        panIncrementInfo.setLayout(new java.awt.GridBagLayout());

        panIncrementTable.setMinimumSize(new java.awt.Dimension(450, 350));
        panIncrementTable.setPreferredSize(new java.awt.Dimension(450, 350));
        panIncrementTable.setLayout(new java.awt.GridBagLayout());

        srpIncrement.setMinimumSize(new java.awt.Dimension(200, 404));
        srpIncrement.setPreferredSize(new java.awt.Dimension(200, 404));

        tblIncrement.setModel(new javax.swing.table.DefaultTableModel(
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
        tblIncrement.setMinimumSize(new java.awt.Dimension(60, 64));
        tblIncrement.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblIncrement.setOpaque(false);
        tblIncrement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblIncrementMouseClicked(evt);
            }
        });
        srpIncrement.setViewportView(tblIncrement);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panIncrementTable.add(srpIncrement, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIncrementInfo.add(panIncrementTable, gridBagConstraints);

        panIncrementDetailsInfo.setMinimumSize(new java.awt.Dimension(350, 300));
        panIncrementDetailsInfo.setPreferredSize(new java.awt.Dimension(350, 300));
        panIncrementDetailsInfo.setLayout(new java.awt.GridBagLayout());

        lblIncrementEmpId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementEmpId, gridBagConstraints);

        panIncrementEmpId.setMinimumSize(new java.awt.Dimension(125, 25));
        panIncrementEmpId.setPreferredSize(new java.awt.Dimension(125, 25));
        panIncrementEmpId.setLayout(new java.awt.GridBagLayout());

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
        panIncrementEmpId.add(txtEmployeeId, gridBagConstraints);

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
        panIncrementEmpId.add(btnEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panIncrementDetailsInfo.add(panIncrementEmpId, gridBagConstraints);

        lblIncrementEmpName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncrementEmpName.setText("Employee Name");
        lblIncrementEmpName.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementEmpName, gridBagConstraints);

        lblIncrementDesignation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncrementDesignation.setText("Designation");
        lblIncrementDesignation.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementDesignation, gridBagConstraints);

        lblIncrementDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncrementDate.setText("Last Increment date");
        lblIncrementDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementDate, gridBagConstraints);

        lblIncrementEffectiveDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncrementEffectiveDate.setText("Effective date");
        lblIncrementEffectiveDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementEffectiveDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panIncrementDetailsInfo.add(tdtIncrementEffectiveDateValue, gridBagConstraints);

        lblIncrementCreatedDate.setText("Created date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementCreatedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panIncrementDetailsInfo.add(tdtIncrementCreatedDateValue, gridBagConstraints);

        lblEmployeeGrade.setText("Employees Grade");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblEmployeeGrade, gridBagConstraints);

        txtIncrementEmpName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtIncrementEmpName, gridBagConstraints);

        txtIncrementDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtIncrementDesignation, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panIncrementDetailsInfo.add(tdtIncrementDate, gridBagConstraints);

        txtBasicSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtBasicSalary, gridBagConstraints);

        lblBasicSalary.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBasicSalary.setText("Basic");
        lblBasicSalary.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblBasicSalary, gridBagConstraints);

        txtEmployeeGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtEmployeeGrade, gridBagConstraints);

        txtIncrementAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtIncrementAmount, gridBagConstraints);

        lblIncrementAmount.setText("Increment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementAmount, gridBagConstraints);

        txtNewBasic.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtNewBasic, gridBagConstraints);

        lblNewBasic.setText("New Basic");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblNewBasic, gridBagConstraints);

        txtIncrementNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIncrementDetailsInfo.add(txtIncrementNo, gridBagConstraints);

        lblIncrementNo.setText("Increment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementNo, gridBagConstraints);

        panIncrementFromDateInfo.setMinimumSize(new java.awt.Dimension(125, 24));
        panIncrementFromDateInfo.setPreferredSize(new java.awt.Dimension(125, 24));
        panIncrementFromDateInfo.setLayout(new java.awt.GridBagLayout());

        lblHaltingParameterBasedon5.setText(" - ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panIncrementFromDateInfo.add(lblHaltingParameterBasedon5, gridBagConstraints);

        txtSalFromDateYYYYValue1.setMinimumSize(new java.awt.Dimension(70, 21));
        txtSalFromDateYYYYValue1.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panIncrementFromDateInfo.add(txtSalFromDateYYYYValue1, gridBagConstraints);

        txtSalFromDateMMValue1.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSalFromDateMMValue1.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panIncrementFromDateInfo.add(txtSalFromDateMMValue1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panIncrementDetailsInfo.add(panIncrementFromDateInfo, gridBagConstraints);

        lblIncrementFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncrementFromDate.setText("Increment Release Date");
        lblIncrementFromDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementFromDate, gridBagConstraints);

        panBasedOnCriteria1.setMinimumSize(new java.awt.Dimension(125, 24));
        panBasedOnCriteria1.setName("panMaritalStatus");
        panBasedOnCriteria1.setPreferredSize(new java.awt.Dimension(125, 24));
        panBasedOnCriteria1.setLayout(new java.awt.GridBagLayout());

        rdgBasedOn.add(rdoIncrementForSingle);
        rdoIncrementForSingle.setText("Single");
        rdoIncrementForSingle.setMaximumSize(new java.awt.Dimension(70, 20));
        rdoIncrementForSingle.setMinimumSize(new java.awt.Dimension(70, 20));
        rdoIncrementForSingle.setName("rdoMaritalStatus_Married");
        rdoIncrementForSingle.setPreferredSize(new java.awt.Dimension(70, 20));
        rdoIncrementForSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIncrementForSingleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnCriteria1.add(rdoIncrementForSingle, gridBagConstraints);

        rdgBasedOn.add(rdoIncrementForAll);
        rdoIncrementForAll.setText("All");
        rdoIncrementForAll.setMaximumSize(new java.awt.Dimension(60, 20));
        rdoIncrementForAll.setMinimumSize(new java.awt.Dimension(60, 20));
        rdoIncrementForAll.setName("rdoMaritalStatus_Married");
        rdoIncrementForAll.setPreferredSize(new java.awt.Dimension(60, 20));
        rdoIncrementForAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIncrementForAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBasedOnCriteria1.add(rdoIncrementForAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 0);
        panIncrementDetailsInfo.add(panBasedOnCriteria1, gridBagConstraints);

        lblIncrementFor.setText("Increment For :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIncrementDetailsInfo.add(lblIncrementFor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIncrementInfo.add(panIncrementDetailsInfo, gridBagConstraints);

        panDeductionButtons.setMinimumSize(new java.awt.Dimension(200, 40));
        panDeductionButtons.setPreferredSize(new java.awt.Dimension(200, 40));

        btnIncrementNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnIncrementNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnIncrementNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncrementNewActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnIncrementNew);

        btnIncrementSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnIncrementSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnIncrementSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncrementSaveActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnIncrementSave);

        btnIncrementDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnIncrementDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnIncrementDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncrementDeleteActionPerformed(evt);
            }
        });
        panDeductionButtons.add(btnIncrementDelete);

        btnIncrementProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnIncrementProcess.setToolTipText("Start Process");
        btnIncrementProcess.setMinimumSize(new java.awt.Dimension(63, 27));
        btnIncrementProcess.setPreferredSize(new java.awt.Dimension(63, 27));
        panDeductionButtons.add(btnIncrementProcess);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIncrementInfo.add(panDeductionButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        panIncrementDetails.add(panIncrementInfo, gridBagConstraints);

        tabMisecllaniousDeductions.addTab("Increment Details", panIncrementDetails);

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

    private void btnSalProcessMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalProcessMousePressed
        // TODO add your handling code here:
        lblStatus.setText("Process Started...");
    }//GEN-LAST:event_btnSalProcessMousePressed
    private void rdoIncrementForAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIncrementForAllActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panIncrementDetailsInfo,false);
        ClientUtil.enableDisable(panDeductionButtons,true);
        panIncrementFromDateInfo.setVisible(true);
        ClientUtil.enableDisable(panIncrementFromDateInfo,true);
        lblIncrementFromDate.setVisible(true);
        btnEmployeeId.setEnabled(false);
        btnIncrementProcess.setEnabled(true);
    }//GEN-LAST:event_rdoIncrementForAllActionPerformed
    private void rdoIncrementForSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIncrementForSingleActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panIncrementDetailsInfo,true);
        ClientUtil.enableDisable(panDeductionButtons,true);
        panIncrementFromDateInfo.setVisible(false);
        lblIncrementFromDate.setVisible(false);
        btnEmployeeId.setEnabled(true);
        btnIncrementProcess.setEnabled(false);
        
    }//GEN-LAST:event_rdoIncrementForSingleActionPerformed
    
    private void rdoBasedOnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnAllActionPerformed
        basedOn = "ALL";
        cboRegionalValue.setEnabled(false);
        cboRegionalValue.setVisible(false);
        lblRegional.setVisible(false);
        cboBranchwiseValue.setEnabled(false);
        cboBranchwiseValue.setVisible(false);
        lblBranchWise.setVisible(false);
        lblFromEmpId.setVisible(false);
        panFromEmpId.setVisible(false);
        lblToEmpId.setVisible(false);
        panToEmpId.setVisible(false);
        cboBranchwiseValue.setSelectedItem("");
        cboRegionalValue.setSelectedItem("");
        txtToEmpIdValue.setText("");
        txtFromEmpIdValue.setText("");
    }//GEN-LAST:event_rdoBasedOnAllActionPerformed
    
    private void rdoBasedOnEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnEmpActionPerformed
        basedOn = "EMPLOYEE";
        cboRegionalValue.setEnabled(false);
        cboRegionalValue.setVisible(false);
        lblRegional.setVisible(false);
        cboBranchwiseValue.setEnabled(false);
        cboBranchwiseValue.setVisible(false);
        lblBranchWise.setVisible(false);
        lblFromEmpId.setVisible(true);
        panFromEmpId.setVisible(true);
        lblToEmpId.setVisible(false);
        panToEmpId.setVisible(false);
        ClientUtil.enableDisable(panFromEmpId,true);
        btnFromEmpId.setEnabled(true);
        cboBranchwiseValue.setSelectedItem("");
        cboRegionalValue.setSelectedItem("");
//        txtToEmpIdValue.setText("");
//        txtFromEmpIdValue.setText("");
    }//GEN-LAST:event_rdoBasedOnEmpActionPerformed
    
    private void rdoBasedOnBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnBranchActionPerformed
        basedOn = "BRANCH";
        cboRegionalValue.setEnabled(false);
        cboRegionalValue.setVisible(false);
        lblRegional.setVisible(false);
        cboBranchwiseValue.setEnabled(true);
        cboBranchwiseValue.setVisible(true);
        lblBranchWise.setVisible(true);
        lblFromEmpId.setVisible(false);
        panFromEmpId.setVisible(false);
        lblToEmpId.setVisible(false);
        panToEmpId.setVisible(false);
//        cboBranchwiseValue.setSelectedItem("");
        cboRegionalValue.setSelectedItem("");
        txtToEmpIdValue.setText("");
        txtFromEmpIdValue.setText("");
    }//GEN-LAST:event_rdoBasedOnBranchActionPerformed
    
    private void rdoBasedOnZonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnZonalActionPerformed
        // TODO add your handling code here:
        basedOn = "ZONAL";
        cboRegionalValue.setEnabled(true);
        cboRegionalValue.setVisible(true);
        lblRegional.setVisible(true);
        cboBranchwiseValue.setEnabled(false);
        cboBranchwiseValue.setVisible(false);
        lblBranchWise.setVisible(false);
        lblFromEmpId.setVisible(false);
        panFromEmpId.setVisible(false);
        lblToEmpId.setVisible(false);
        panToEmpId.setVisible(false);
        cboBranchwiseValue.setSelectedItem("");
//        cboRegionalValue.setSelectedItem("");
        txtToEmpIdValue.setText("");
        txtFromEmpIdValue.setText("");
        
    }//GEN-LAST:event_rdoBasedOnZonalActionPerformed
    
    private void rdoProcessTypeFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProcessTypeFinalActionPerformed
        // TODO add your handling code here:
        SalaryMasterStatus = "FINAL";
        basedOn = "ALL";
        btnSalView.setEnabled(false);
        btnTrialProcess.setEnabled(false);
        btnSalProcess.setEnabled(true);
        btnSalView.setVisible(false);
        btnTrialProcess.setVisible(false);
        btnSalProcess.setVisible(true);
        btnClear.setEnabled(true);
        panBasedOnCriteria.setVisible(true);
        ClientUtil.enableDisable(panBasedOnCriteria,false);
        lblEarnOrDeduction.setVisible(true);
        int month = CommonUtil.convertObjToInt(this.txtSalFromDateMMValue.getText());
        int year = CommonUtil.convertObjToInt(this.txtSalFromDateYYYYValue.getText());
        HashMap salaryIdMap = null;
        if(salaryIdMap == null){
            salaryIdMap = new HashMap();
        }
        salaryIdMap= observable.checkIfAlreadyCalculated(month,year);
        if(salaryIdMap!= null && salaryIdMap.size()>0){
            String salaryId = (String) CommonUtil.convertObjToStr(salaryIdMap.get("SALARY_ID"));
            ClientUtil.showAlertWindow("Salary already calculated, SALARY_ID:"+salaryId);
            btnSave.setEnabled(false);
            btnView.setEnabled(true);
            btnSalProcess.setVisible(false);
            btnClear.setEnabled(true);
        }
    }//GEN-LAST:event_rdoProcessTypeFinalActionPerformed
    
    private void rdoProcessTypeTrialProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProcessTypeTrialProcessActionPerformed
        // TODO add your handling code here:
        SalaryMasterStatus = "TRIAL_PROCESS";
        btnSalView.setEnabled(false);
        btnTrialProcess.setEnabled(false);
        btnSalProcess.setEnabled(true);
        btnSalView.setVisible(false);
        btnTrialProcess.setVisible(false);
        btnSalProcess.setVisible(true);
        btnClear.setEnabled(true);
        panBasedOnCriteria.setVisible(true);
        ClientUtil.enableDisable(panBasedOnCriteria,true);
        lblEarnOrDeduction.setVisible(true);
        int month =CommonUtil.convertObjToInt(this.txtSalFromDateMMValue.getText());
        int year = CommonUtil.convertObjToInt(this.txtSalFromDateYYYYValue.getText());
        HashMap salaryIdMap= observable.checkIfAlreadyCalculated(month,year);
        if(salaryIdMap!= null && salaryIdMap.size()>0){
            String salaryId = (String) CommonUtil.convertObjToStr(salaryIdMap.get("SALARY_ID"));
            ClientUtil.showAlertWindow("Salary Has Already Been Calculated -> SALARY_ID:"+salaryId);
            btnSave.setEnabled(false);
            btnView.setEnabled(true);
            btnSalProcess.setVisible(false);
            btnClear.setEnabled(true);
        }
    }//GEN-LAST:event_rdoProcessTypeTrialProcessActionPerformed
    
    private void rdoProcessTypeTrialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProcessTypeTrialActionPerformed
        SalaryMasterStatus = "TRIAL";
        btnSalView.setEnabled(false);
        btnTrialProcess.setEnabled(false);
        btnSalProcess.setEnabled(true);
        btnSalView.setVisible(false);
        btnTrialProcess.setVisible(false);
        btnSalProcess.setVisible(true);
        btnClear.setEnabled(true);
        ClientUtil.enableDisable(panBasedOnCriteria,true);
        panBasedOnCriteria.setVisible(true);
        lblEarnOrDeduction.setVisible(true);
        int month =CommonUtil.convertObjToInt(this.txtSalFromDateMMValue.getText());
        int year = CommonUtil.convertObjToInt(this.txtSalFromDateYYYYValue.getText());
        HashMap salaryIdMap= observable.checkIfAlreadyCalculated(month,year);
        if(salaryIdMap!= null && salaryIdMap.size()>0){
            String salaryId = (String) CommonUtil.convertObjToStr(salaryIdMap.get("SALARY_ID"));
            ClientUtil.showAlertWindow("Salary Has Already Been Calculated please check in enquiry mode for the same period:");
            btnSalProcess.setVisible(false);
            btnClear.setEnabled(true);
            btnSave.setEnabled(false);
            btnView.setEnabled(true);
        }
    }//GEN-LAST:event_rdoProcessTypeTrialActionPerformed
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        btnCancelActionPerformed(null);
        btnNewActionPerformed(null);
        btnView.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed
    
    private void btnSalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalViewActionPerformed
        String fromMMValue = CommonUtil.convertObjToStr(txtSalFromDateMMValue.getText());
        String fromYYYYValue = CommonUtil.convertObjToStr(txtSalFromDateYYYYValue.getText());
        if(fromMMValue.length()<=0 || fromYYYYValue.length() <=0){
            ClientUtil.showMessageWindow("Please enter the Month and year of Salary!!");
            btnSave.setEnabled(false);
            return;
        }
        else{
            SalaryMasterStatus = "ENQUIRY";
            updateOBFields(ARREARS);
            observable.ArrearCalculations(SalaryMasterStatus,basedOn);
            panTable.setVisible(true);
            panFinalAmount.setVisible(true);
            tblSalaryMaster.setEnabled(true);
            
            tblSalaryMaster.setModel(observable.getTblSalaryMaster());
            System.out.println("!@#$tblSalaryMaster.getRowCount()"+tblSalaryMaster.getRowCount());
            if(tblSalaryMaster.getRowCount() == 0 ){
                //                btnCancelActionPerformed(null);
                ClientUtil.clearAll(this);
                cboBranchwiseValue.setSelectedItem("");
                cboBranchwiseValue.setEnabled(false);
                txtFromEmpIdValue.setText("");
                txtEmployeeName.setText("");
                txtEmployeeDesig.setText("");
                observable.setResultStatus();
                btnClear.setEnabled(true);
            }
            btnView.setEnabled(true);
            btnSave.setEnabled(false);
            btnView.setEnabled(true);
            //__ Make the Screen Closable..
            setModified(false);
        }
        txtTotalDeduction.setText("");
        txtTotalEarnings.setText("");
        txtNetSalary.setText("");
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnSalViewActionPerformed
    
    private void btnSalProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalProcessActionPerformed
        // TODO add your handling code here:
        String fromMMValue = CommonUtil.convertObjToStr(txtSalFromDateMMValue.getText());
        String fromYYYYValue = CommonUtil.convertObjToStr(txtSalFromDateYYYYValue.getText());
        txtTotalEarnings.setText("");
        txtTotalDeduction.setText("");
        txtNetSalary.setText("");
        if(fromMMValue.length()<=0 || fromYYYYValue.length() <=0){
            ClientUtil.showMessageWindow("Please enter the Month and year of Salary!!");
            return;
        }
        else{
            if(SalaryMasterStatus.equals("TRIAL")){
                updateOBFields(ARREARS);
                observable.ArrearCalculations(SalaryMasterStatus,basedOn);
                panTable.setVisible(true);
                panFinalAmount.setVisible(true);
                tblSalaryMaster.setEnabled(true);
                tblSalaryMaster.setModel(observable.getTblSalaryMaster());
                System.out.println("!@#$tblSalaryMaster.getRowCount()"+tblSalaryMaster.getRowCount());
                if(tblSalaryMaster.getRowCount() == 0 ){
                    btnCancelActionPerformed(null);
                    ClientUtil.clearAll(this);
                    cboBranchwiseValue.setSelectedItem("");
                    cboBranchwiseValue.setEnabled(false);
                    txtFromEmpIdValue.setText("");
                    txtEmployeeName.setText("");
                    txtEmployeeDesig.setText("");
                    observable.setResultStatus();
                    btnClear.setEnabled(true);
                }
                lblStatus.setText("Trial Completed !!");
            }
            else{
                updateOBFields(ARREARS);
                observable.ArrearCalculations(SalaryMasterStatus,basedOn);
                panTable.setVisible(true);
                panFinalAmount.setVisible(true);
                tblSalaryMaster.setEnabled(true);
                tblSalaryMaster.setModel(observable.getTblSalaryMaster());
                btnCancelActionPerformed(null);
                btnNewActionPerformed(null);
                lblStatus.setText("Process Completed!!!");
                //__ Make the Screen Closable..
                setModified(false);
            }
        }
        
    }//GEN-LAST:event_btnSalProcessActionPerformed
    
    private void btnTrialProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrialProcessActionPerformed
        // TODO add your handling code here:
        String fromMMValue = CommonUtil.convertObjToStr(txtSalFromDateMMValue.getText());
        String fromYYYYValue = CommonUtil.convertObjToStr(txtSalFromDateYYYYValue.getText());
        if(fromMMValue.length()<=0 || fromYYYYValue.length() <=0){
            ClientUtil.showMessageWindow("Please enter the Month and year of Salary!!");
            return;
        }
        else{
            SalaryMasterStatus = "ENQUIRY";
            updateOBFields(ARREARS);
            observable.ArrearCalculations(SalaryMasterStatus,basedOn);
            panTable.setVisible(true);
            panFinalAmount.setVisible(true);
            tblSalaryMaster.setEnabled(true);
            tblSalaryMaster.setModel(observable.getTblSalaryMaster());
            btnCancelActionPerformed(null);
            btnNewActionPerformed(null);
            lblStatus.setText("TRIAL PROCESSED");
            //__ Make the Screen Closable..
            setModified(false);
        }
    }//GEN-LAST:event_btnTrialProcessActionPerformed
    
    private void cboRegionalValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRegionalValueActionPerformed
        cboBranchwiseValue.setEnabled(true);
    }//GEN-LAST:event_cboRegionalValueActionPerformed
    
    private void cboRegionalValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRegionalValueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRegionalValueFocusLost
    
    private void cboBranchwiseValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchwiseValueActionPerformed
        if(cboBranchwiseValue.getSelectedIndex()>0){
            HashMap branchMap = new HashMap();
            branchMap.put("BRANCH_ID",cboBranchwiseValue.getSelectedItem());
            List lst = ClientUtil.executeQuery("getAllBranchesList", branchMap);
            btnFromEmpId.setEnabled(true);
            if(lst!=null && lst.size()>0){
                branchMap = (HashMap)lst.get(0);
                //                lblBranchWiseValue.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
            }
        }else{
            //            lblBranchWiseValue.setText("");
        }
    }//GEN-LAST:event_cboBranchwiseValueActionPerformed
    
    private void txtSalFromDateYYYYValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalFromDateYYYYValueFocusLost
        int month =CommonUtil.convertObjToInt(this.txtSalFromDateMMValue.getText());
        if(month < 0 || month >12){
            ClientUtil.showAlertWindow("Month Value Not Correct!!!");
            txtSalFromDateMMValue.setText("");
        }
        //        else{
        //            int year = CommonUtil.convertObjToInt(this.txtSalFromDateYYYYValue.getText());
        //            HashMap salaryIdMap= observable.checkIfAlreadyCalculated(month,year);
        //            if(salaryIdMap!= null){
        //                String salaryId = (String) CommonUtil.convertObjToStr(salaryIdMap.get(""));
        //                ClientUtil.showAlertWindow("Salary already calculated SALARY_ID:"+salaryId);
        //            }
        //        }
    }//GEN-LAST:event_txtSalFromDateYYYYValueFocusLost
    
    private void txtSalFromDateMMValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalFromDateMMValueFocusLost
        // TODO add your handling code here:
        int month =CommonUtil.convertObjToInt(this.txtSalFromDateMMValue.getText());
        //       int month = Integer.parseInt(val);
        if(month < 0 || month >12){
            ClientUtil.showAlertWindow("Month Value Not Correct!!!");
            txtSalFromDateMMValue.setText("");
        }
    }//GEN-LAST:event_txtSalFromDateMMValueFocusLost
    
    private void btnToEmpIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToEmpIdActionPerformed
        callView(TO_EMPLOYEE_ID,ARREARS);
    }//GEN-LAST:event_btnToEmpIdActionPerformed
    
    private void btnFromEmpIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromEmpIdActionPerformed
        callView(FROM_EMPLOYEE_ID,ARREARS);
        txtSalFromDateMMValue.setEnabled(true);
        panToEmpId.setVisible(true);
        lblToEmpId.setVisible(true);
        txtToEmpIdValue.setEnabled(true);
        btnToEmpId.setEnabled(true);
        lblToEmpId.setEnabled(true);
        txtSalFromDateYYYYValue.setEnabled(true);
        //        txtSalToDateMMValue.setEnabled(true);
        //        txtSalToDateYYYYValue.setEnabled(true);
    }//GEN-LAST:event_btnFromEmpIdActionPerformed
    
    private void tblSalaryMasterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryMasterMousePressed
        int row = tblSalaryMaster.getSelectedRow();
        observable.insertSalaryData(row);
        txtTotalDeduction.setText(observable.getTxtTotalDeduction());
        txtTotalEarnings.setText(observable.getTxtTotalEarnings());
        txtNetSalary.setText(observable.getTxtNetSalary());
        txtEmployeeDesig.setVisible(true);
        lblEmployeeDesig.setVisible(true);
        txtEmployeeName.setVisible(true);
        lblEmployeeName.setVisible(true);
        txtEmployeeDesig.setText(observable.getTxtEmployeeDesig());
        txtEmployeeName.setText(observable.getTxtEmployeeName());
        tblSalaryMasterDtails.setModel(observable.getTblSalaryMasterDtails());
    }//GEN-LAST:event_tblSalaryMasterMousePressed
    
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
    
    private void updateIncrement(int selectDARow) {
        observable.populateIncrement(selectDARow);
        populateDetail();
    }
    
    private void txtEmployeeIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmployeeIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeIdFocusLost
    
    private void btnEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeIdActionPerformed
        //        This Invokes the CallView Function,which is used to view the records for Increment
        callView(INCREMENT_ID,INCREMENT);
        btnIncrementSave.setEnabled(true);
        HashMap  existMap=new HashMap();
        //            existMap.put("EMPLOYEEID",CommonUtil.convertObjToStr(txtEmployeeId.getText()));
        //            List lst=ClientUtil.executeQuery("empIdExistanceCheckIncrement",existMap);
        //            if(lst!=null && lst.size()>0){
        //                existMap=new HashMap();
        //                existMap=(HashMap)lst.get(0);
        //                ClientUtil.showMessageWindow("Increment detail for the Employee " + CommonUtil.convertObjToStr(existMap.get("EMP_NAME")) + " already exists!!");
        //                btnIncrementSave.setEnabled(false);
        //                existMap=null;
        //            }
    }//GEN-LAST:event_btnEmployeeIdActionPerformed
    
    private void btnIncrementDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncrementDeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblIncrement.getRowCount();
        int rowSelected = tblIncrement.getSelectedRow();
        if((rowCount-1) == rowSelected){
            observable.deleteIncrementData(this.tblIncrement.getSelectedRow());
            this.updateTable();
            resetIncrementForm();
            observable.resetIncrement();
            enableDisableIncrement(false);
        }else{
            ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
            return;
        }
    }//GEN-LAST:event_btnIncrementDeleteActionPerformed
    private void resetValues(){
    }
    private void tblIncrementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIncrementMouseClicked
        // TODO add your handling code here:
        panEditDelete=INCREMENT;
        if(tblIncrement.getRowCount()>0){
            selectedSingleRow = true;
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                newButtonEnable = false;
                updationIncrement(tblIncrement.getSelectedRow());
                enableDisableIncrement(false);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                String EmployeeId = CommonUtil.convertObjToStr(tblIncrement.getValueAt(tblIncrement.getSelectedRow(),0));
                if(tblIncrement.getSelectedRow()>=0 && !EmployeeId.equals("")){
                    newButtonEnable = false;
                    updationIncrement(tblIncrement.getSelectedRow());
                    ClientUtil.enableDisable(panIncrementDetails,true);
                    tdtIncrementCreatedDateValue.setDateValue(CommonUtil.convertObjToStr(curDate.clone()));
                    tdtIncrementCreatedDateValue.setEnabled(false);
                    txtIncrementEmpName.setEnabled(false);
                    txtIncrementDesignation.setEnabled(false);
                    txtBasicSalary.setEnabled(false);
                    tdtIncrementDate.setEnabled(false);
                    txtEmployeeGrade.setEnabled(false);
                    txtIncrementAmount.setEnabled(false);
                    txtNewBasic.setEnabled(false);
                    txtIncrementNo.setEnabled(false);
                    txtEmployeeId.setEnabled(false);
                    btnEmployeeId.setEnabled(false);
                    btnIncrementNew.setEnabled(false);
                    btnIncrementSave.setEnabled(true);
                    btnIncrementDelete.setEnabled(true);
                    _intHANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
        
    }//GEN-LAST:event_tblIncrementMouseClicked
    
    private void btnIncrementSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncrementSaveActionPerformed
        // TODO add your handling code here:
        pan=INCREMENT;
        
        if(txtEmployeeId.getText().length() == 0){
            ClientUtil.showAlertWindow("Employee Id should not be empty");
        }else if(tdtIncrementEffectiveDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("Effective date should not be empty");
        }else if(tdtIncrementCreatedDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("Created date should not be empty");
        }else{
            long OACboSize = 1;
            btnSave.setEnabled(true);
            updateOBFields(pan);
            if(!this._intHANew){
                observable.insertIncrementData(this.tblIncrement.getSelectedRow());
            }else{
                observable.insertIncrementData(-1);
            }
            resetIncrementForm();
            enableDisableIncrement(false);
            this.updateTable();
            btnIncrementNew.setEnabled(true);
            btnIncrementSave.setEnabled(false);
            btnIncrementDelete.setEnabled(false);
            btnEmployeeId.setEnabled(false);
            this._intHANew = false;
            selectedSingleRow = false;
        }
        
    }//GEN-LAST:event_btnIncrementSaveActionPerformed
    private void enableDisableIncrement(boolean MAVal){
        txtEmployeeId.setEnabled(MAVal);
        tdtIncrementEffectiveDateValue.setEnabled(MAVal);
        tdtIncrementCreatedDateValue.setEnabled(MAVal);
        txtIncrementEmpName.setEnabled(MAVal);
        txtIncrementDesignation.setEnabled(MAVal);
        tdtIncrementDate.setEnabled(MAVal);
        txtBasicSalary.setEnabled(MAVal);
        txtEmployeeGrade.setEnabled(MAVal);
        txtIncrementAmount.setEnabled(MAVal);
        txtNewBasic.setEnabled(MAVal);
        txtIncrementNo.setEnabled(MAVal);
        tdtIncrementDate.setEnabled(MAVal);
        btnIncrementNew.setEnabled(MAVal);
        btnIncrementSave.setEnabled(MAVal);
        btnIncrementDelete.setEnabled(MAVal);
    }
    
    private void btnIncrementNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncrementNewActionPerformed
        // TODO add your handling code here:
        _intHANew = true;
        if(tblIncrement.getRowCount() == 0){
            ClientUtil.enableDisable(panIncrementDetails,true);
            btnEmployeeId.setEnabled(true);
            tdtIncrementCreatedDateValue.setDateValue(CommonUtil.convertObjToStr(curDate.clone()));
            tdtIncrementCreatedDateValue.setEnabled(false);
            txtIncrementEmpName.setEnabled(false);
            txtIncrementDesignation.setEnabled(false);
            txtBasicSalary.setEnabled(false);
            tdtIncrementDate.setEnabled(false);
            txtEmployeeGrade.setEnabled(false);
            txtIncrementAmount.setEnabled(false);
            txtNewBasic.setEnabled(false);
            txtIncrementNo.setEnabled(false);
            txtEmployeeId.setEnabled(false);
        }else if(tblIncrement.getRowCount() > 0){
            newButtonEnable = true;
            btnEmployeeId.setEnabled(true);
            ClientUtil.enableDisable(panIncrementDetails,true);
            tdtIncrementCreatedDateValue.setDateValue(CommonUtil.convertObjToStr(curDate.clone()));
            tdtIncrementCreatedDateValue.setEnabled(false);
            txtIncrementEmpName.setEnabled(false);
            txtIncrementDesignation.setEnabled(false);
            txtBasicSalary.setEnabled(false);
            tdtIncrementDate.setEnabled(false);
            txtEmployeeGrade.setEnabled(false);
            txtIncrementAmount.setEnabled(false);
            txtNewBasic.setEnabled(false);
            txtIncrementNo.setEnabled(false);
            txtEmployeeId.setEnabled(false);
        }
        btnIncrementNew.setEnabled(false);
    }//GEN-LAST:event_btnIncrementNewActionPerformed
    private void updationIncrement(int selectDARow) {
        observable.populateIncrement(selectDARow);
        populateIncrementDetails();
    }
    private void resetArrersForm(){
        txtFromEmpIdValue.setText("");
        txtEmployeeName.setText("");
        txtEmployeeDesig.setText("");
        txtToEmpIdValue.setText("");
        cboBranchwiseValue.setSelectedItem("");
        cboRegionalValue.setSelectedItem("");
        txtSalFromDateMMValue.setText("");
        txtSalFromDateYYYYValue.setText("");
        txtNetSalary.setText("");
        txtTotalEarnings.setText("");
        txtTotalDeduction.setText("");
        //        txtSalToDateMMValue.setText("");
        //        txtSalToDateYYYYValue.setText("");
        //        tdtSalFromDateValue.setDateValue("");
        tdtSalToDateValue.setDateValue("");
        //        rdoEmployeeWise.setEnabled(false);
        //        rdoBranchWise.setEnabled(false);
        //        rdoRegionalWise.setEnabled(false);
        panTable.setVisible(false);
        ClientUtil.clearAll(panFinalAmount);
        panFinalAmount.setVisible(false);
        txtEmployeeDesig.setVisible(false);
        lblEmployeeDesig.setVisible(false);
        txtEmployeeName.setVisible(false);
        lblEmployeeName.setVisible(false);
        ClientUtil.clearAll(panFromEmpId);
        ClientUtil.clearAll(panToEmpId);
        //        ClientUtil.clearAll(panTypeofMode);
    }
    private void resetIncrementForm(){
        txtEmployeeGrade.setText("");
        txtIncrementAmount.setText("");
        txtNewBasic.setText("");
        txtIncrementNo.setText("");
        txtEmployeeId.setText("");
        txtIncrementEmpName.setText("");
        txtIncrementDesignation.setText("");
        txtBasicSalary.setText("");
        tdtIncrementDate.setDateValue("");
        tdtIncrementEffectiveDateValue.setDateValue("");
        tdtIncrementCreatedDateValue.setDateValue("");
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(panIncrementDetails.isShowing()==true){
            panEditDelete = INCREMENT;
        }
        else if(panArrearCalculationInfo.isShowing()==true){
            panEditDelete = ARREARS;
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(panEditDelete == ARREARS){
            btnNewActionPerformed(null);
            btnCheck();
            tdtSalToDateValue.setDateValue(CommonUtil.convertObjToStr(curDate));
            ClientUtil.enableDisable(panProcessType,false);
            ClientUtil.enableDisable(panBasedOnCriteria,true);
            panBasedOnCriteria.setVisible(true);
            SalaryMasterStatus = "ENQUIRY";
            btnSalView.setVisible(true);
            btnSalView.setEnabled(true);
            btnSalProcess.setVisible(false);
            btnSalProcess.setEnabled(false);
        }
        else{
            callView(VIEW,panEditDelete);
            btnCheck();
        }
        
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
    
    private void updateOBFields(int pan){
        
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        if(pan==INCREMENT){
            observable.setTxtEmployeeId(txtEmployeeId.getText());
            observable.setTxtIncrementEmpName(txtIncrementEmpName.getText());
            observable.setTxtIncrementDesignation(txtIncrementDesignation.getText());
            observable.setTxtBasicSalary(txtBasicSalary.getText());
            observable.setTdtIncrementDate(tdtIncrementDate.getDateValue());
            observable.setTdtIncrementEffectiveDateValue(tdtIncrementEffectiveDateValue.getDateValue());
            observable.setTdtIncrementCreatedDateValue(tdtIncrementCreatedDateValue.getDateValue());
            observable.setTxtEmployeeGrade(txtEmployeeGrade.getText());
            observable.setTxtIncrementAmount(txtIncrementAmount.getText());
            observable.setTxtNewBasic(txtNewBasic.getText());
            observable.setTxtIncrementNo(txtIncrementNo.getText());
        }
        else if(pan == ARREARS){
            //            observable.setRdoEmployeeWise(rdoEmployeeWise.isSelected());
            //            observable.setRdoBranchWise(rdoBranchWise.isSelected());
            //            observable.setRdoRegionalWise(rdoRegionalWise.isSelected());
            observable.setTxtFromEmpIdValue(txtFromEmpIdValue.getText());
            observable.setTxtEmployeeDesig(txtEmployeeDesig.getText());
            observable.setTxtEmployeeName(txtEmployeeName.getText());
            observable.setTxtSalFromDateMMValue(txtSalFromDateMMValue.getText());
            observable.setTxtSalFromDateYYYYValue(txtSalFromDateYYYYValue.getText());
            observable.setTxtSalaryID(txtSalaryID.getText());
            //            observable.setTxtSalToDateMMValue(txtSalToDateMMValue.getText());
            //            observable.setTxtSalToDateYYYYValue(txtSalToDateYYYYValue.getText());
            observable.setTdtSalToDateValue(tdtSalToDateValue.getDateValue());
            observable.setTxtToEmpIdValue(txtToEmpIdValue.getText());
            //            observable.setTdtSalFromDateValue(tdtSalFromDateValue.getDateValue());
            observable.setCboBranchwiseValue((String)((ComboBoxModel)this.cboBranchwiseValue.getModel()).getKeyForSelected());
            observable.setCboRegionalValue((String)((ComboBoxModel)this.cboRegionalValue.getModel()).getKeyForSelected());
        }
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
                btnSave.setEnabled(false);
            } else {
                btnSave.setEnabled(true);
            }
        } else {
            btnSave.setEnabled(true);
        }
        
        setOpenForEditBy(lockedBy);
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID()) ;
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            btnSave.setEnabled(false);
            
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
    
    private void populateDetail(){
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        
        txtIncrementEmpName.setText(observable.getTxtIncrementEmpName());
        txtIncrementDesignation.setText(observable.getTxtIncrementDesignation());
        txtBasicSalary.setText(observable.getTxtBasicSalary());
        tdtIncrementDate.setDateValue(observable.getTdtIncrementDate());
        tdtIncrementEffectiveDateValue.setDateValue(observable.getTdtIncrementEffectiveDateValue());
        tdtIncrementCreatedDateValue.setDateValue(observable.getTdtIncrementCreatedDateValue());
        txtEmployeeGrade.setText(observable.getTxtEmployeeGrade());
        txtIncrementAmount.setText(observable.getTxtIncrementAmount());
        txtNewBasic.setText(observable.getTxtNewBasic());
        txtIncrementNo.setText(observable.getTxtIncrementNo());
        txtFromEmpIdValue.setText(observable.getTxtFromEmpIdValue());
        txtEmployeeDesig.setText(observable.getTxtEmployeeDesig());
        txtEmployeeName.setText(observable.getTxtEmployeeName());
        cboBranchwiseValue.setSelectedItem(observable.getCboBranchwiseValue());
        txtToEmpIdValue.setText(observable.getTxtToEmpIdValue());
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void populateIncrementDetails(){
        txtEmployeeId.setText(observable.getTxtEmployeeId());
        txtIncrementEmpName.setText(observable.getTxtIncrementEmpName());
        System.out.println("obj.getEmpId()@#@#"+observable.getTdtIncrementDate());
        txtIncrementDesignation.setText(observable.getTxtIncrementDesignation());
        txtBasicSalary.setText(observable.getTxtBasicSalary());
        tdtIncrementDate.setDateValue(observable.getTdtIncrementDate());
        tdtIncrementEffectiveDateValue.setDateValue(observable.getTdtIncrementEffectiveDateValue());
        tdtIncrementCreatedDateValue.setDateValue(observable.getTdtIncrementCreatedDateValue());
        txtEmployeeGrade.setText(observable.getTxtEmployeeGrade());
        txtIncrementAmount.setText(observable.getTxtIncrementAmount());
        txtNewBasic.setText(observable.getTxtNewBasic());
        txtIncrementNo.setText(observable.getTxtIncrementNo());
        ((ComboBoxModel)cboBranchwiseValue.getModel()).setKeyForSelected(observable.getCbmBranchwiseValue());
        ((ComboBoxModel)cboRegionalValue.getModel()).setKeyForSelected(observable.getCbmRegionalValue());
    }
    
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    private void resetSalaryForm(){
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        observable.setStatus();
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_REJECT);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
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
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnPrint.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
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
            if(panIncrementDetails.isShowing()==true){
                //                To authorize the Increment Details based on the pan selected
                HashMap authIncrement = new HashMap();
                ArrayList arrList = new ArrayList();
                authIncrement.put("INCREMENT_ID", observable.getIncrementID());
                authIncrement.put("EMP_ID", this.txtEmployeeId.getText());
                authIncrement.put("LAST_BASIC",this.txtBasicSalary.getText());
                authIncrement.put("INCREMENT_AMOUNT",this.txtIncrementAmount.getText());
                authIncrement.put("NEWBASIC", this.txtNewBasic.getText());
                authIncrement.put("STATUS",status);
                authIncrement.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
                authIncrement.put("AUTHORIZE_DATE",ClientUtil.getCurrentDate());
                authIncrement.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                authIncrement.put("AUTHORIZE_STATUS",authorizeStatus);
                Date effectiveDate = (Date) DateUtil.getDateMMDDYYYY(observable.getTdtIncrementEffectiveDateValue());
                effectiveDate = setProperDtFormat(effectiveDate);
                System.out.println("!@#!@#!@#effectiveDate b4:"+effectiveDate);
                authIncrement.put("EFFECTIVEDATE", effectiveDate);
                Date newIncrementDate = setProperDtFormat(effectiveDate);
                System.out.println("!@#!@#!@#!@#!@#!@#authIncrementb4:"+authIncrement);
                Date nextIncrementDate = newIncrementDate;
                nextIncrementDate.setYear(nextIncrementDate.getYear() +1);
                System.out.println("!@#!@#!@#!@#!@#!@#authIncrementAfer:"+authIncrement);
                System.out.println("!@#!@#!@#effectiveDate:"+effectiveDate);
                System.out.println("!@#!@#!@#nextIncrementDate:"+nextIncrementDate);
                authIncrement.put("NEW_INCREMENT_DATE", nextIncrementDate);
                authIncrement.put("INCREMENT_REASON", "INCREMENT");
                Date createdDate = (Date) DateUtil.getDateMMDDYYYY(observable.getTdtIncrementCreatedDateValue());
                createdDate = setProperDtFormat(createdDate);
                authIncrement.put("CREATED_DATE", createdDate);
                authIncrement.put("NO_OF_INCREMENTS", this.txtIncrementNo.getText());
                arrList.add(authIncrement);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorizeMap.put("INCREMENT_SCREEN", "INCREMENT");
                authorize(authorizeMap,observable.getIncrementID());
                super.setOpenForEditBy(observable.getStatusBy());
                authIncrement = null;
                authorizeMap = null;
            }
            else if(panArrearCalculationInfo.isShowing()==true){
                //ClientUtil.execute("updateAuthorizeStatusArrears",authorizeMap);
            }
            observable.setResult(observable.getActionType());
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            isFilled = false;
        }else{
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            //
            if(panIncrementDetails.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getIncrementAuthorizeMode");
            }
            else if(panArrearCalculationInfo.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getPromotionAuthorizeMode");
            }
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            lblStatus.setText(observable.getLblStatus());
            //            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
        }
    }
    
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get("INCREMENT_SCREEN") != null) {
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
        // Add your handling code here:
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
            ClientUtil.showAlertWindow("Entered Amount is not Matching");
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        resetArrersForm();
        resetIncrementForm();
        ClientUtil.clearAll(this);
        cboBranchwiseValue.setSelectedItem("");
        txtFromEmpIdValue.setText("");
        txtEmployeeDesig.setText("");
        txtEmployeeName.setText("");
        btnSalView.setEnabled(false);
        btnSalProcess.setEnabled(false);
        btnClear.setEnabled(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        btnPrint.setEnabled(true);
        btnView.setEnabled(true);
        
        btnEmployeeId.setEnabled(false);
        btnIncrementNew.setEnabled(false);
        btnIncrementSave.setEnabled(false);
        btnIncrementDelete.setEnabled(false);
        btnFromEmpId.setEnabled(false);
        btnToEmpId.setEnabled(false);
        isFilled = false;
        txtFromEmpIdValue.setText("");
        txtEmployeeDesig.setText("");
        txtEmployeeName.setText("");
        txtToEmpIdValue.setText("");
        ClientUtil.clearAll(panFromEmpId);
        cboBranchwiseValue.setSelectedItem("");
        txtFromEmpIdValue.setText("");
        txtEmployeeDesig.setText("");
        txtEmployeeName.setText("");
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateTable(){
        this.tblIncrement.setModel(observable.getTbmIncrement());
        this.tblIncrement.revalidate();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        if(panArrearCalculationInfo.isShowing()==true){
            panEditDelete=INCREMENT;
        }
        else if(panArrearCalculationInfo.isShowing()==true){
            panEditDelete=ARREARS;
        }
        callView(DELETE,panEditDelete);
        btnSave.setEnabled(true);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if(panIncrementDetails.isShowing()==true){
            panEditDelete = INCREMENT;
        }
        else if(panArrearCalculationInfo.isShowing()==true){
            panEditDelete = ARREARS;
        }
        resetUI(panEditDelete);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT,panEditDelete);
        btnSalView.setEnabled(true);
        btnSalProcess.setEnabled(true);
        btnDelete.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    private void panIncrementEnable(){
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panIncrementDetails, false);
            btnEmployeeId.setEnabled(false);
            btnIncrementNew.setEnabled(true);
        }
        else
            btnEmployeeId.setEnabled(true);
        ClientUtil.enableDisable(panIncrementDetails, false);
        ClientUtil.enableDisable(panIncrementDetailsInfo,false);
        
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        if(panIncrementDetails.isShowing()==true){
            panIncrementEnable();    // To enable the Increment panel...
            pan=INCREMENT;
        }
        else if(panArrearCalculationInfo.isShowing()==true){
            panArrearCalcEnable();   //To Enable the Arrear Calculation Panel
            pan = ARREARS;
        }
        resetUI(pan);
        btnSalView.setEnabled(true);
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        //        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void resetUI(int value){
        //observable.resetTable();
        if(value==INCREMENT){
            System.out.println("reset Increment");
            observable.resetIncrement();
        }
        else if(value == ARREARS){
            System.out.println("reset Arrears");
            observable.resetArrears();
        }
    }
    private void panArrearCalcEnable(){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panArrearCalculationInfo, false);
            ClientUtil.enableDisable(panSalaryInfmn, false);
            ClientUtil.enableDisable(panProcessType,true);
            ClientUtil.enableDisable(panSalFromDateInfo,true);
            Date currDt = (Date) curDate.clone();
            tdtSalToDateValue.setDateValue(CommonUtil.convertObjToStr(currDt));
            tdtSalToDateValue.setEnabled(false);
            panBasedOnCriteria.setVisible(false);
            lblEarnOrDeduction.setVisible(false);
            cboRegionalValue.setVisible(false);
            lblRegional.setVisible(false);
            lblBranchWise.setVisible(false);
            cboBranchwiseValue.setVisible(false);
            lblFromEmpId.setVisible(false);
            panFromEmpId.setVisible(false);
            lblToEmpId.setVisible(false);
            panToEmpId.setVisible(false);
            btnSalView.setVisible(false);
            btnTrialProcess.setVisible(false);
            btnSalProcess.setVisible(false);
            btnClear.setVisible(true);
            btnSalProcess.setEnabled(true);
            lblSalaryID.setVisible(false);
            txtSalaryID.setVisible(false);
            
            ClientUtil.enableDisable(panProductId1,true);
            cboRegionalValue.setEnabled(true);
            btnClear.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            
        }
        else {
            ClientUtil.enableDisable(panArrearCalculationInfo, false);
        }
    }
    private void allScreensDisable(boolean allDisable){
        ClientUtil.enableDisable(panIncrementDetails,allDisable);
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
        if(viewType == INCREMENT_ID /*||viewType == FROM_EMPLOYEE_ID || viewType == TO_EMPLOYEE_ID*/){
            //            To select employee values for Increment and salary calculation for employees
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmployeeDetails");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        else if(viewType == FROM_EMPLOYEE_ID) {
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmployeeFromDetails");
            //            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            where.put("CURRENT_DT",curDate);
            where.put(CommonConstants.BRANCH_ID, this.cboBranchwiseValue.getSelectedItem());
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        else if(viewType == TO_EMPLOYEE_ID) {
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmployeeFromDetails");
            //            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            where.put("CURRENT_DT",curDate);
            where.put(CommonConstants.BRANCH_ID, this.cboBranchwiseValue.getSelectedItem());
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        else if(viewType == ACCT_HEAD){
            viewMap.put(CommonConstants.MAP_NAME,"Cash.getSelectAcctHead");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if (viewType == EDIT || viewType == DELETE || viewType == VIEW ){
            if(viewType== VIEW){
                //                To View already Inserted records based on the pan selected
                if(panEditDelete==INCREMENT){
                    viewMap.put(CommonConstants.MAP_NAME,"getSelectIncrementViewDetails");
                    where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                }
            }
            //            TO Select values for EDIT and DELETE
            else if(panEditDelete==INCREMENT){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectIncrementDetails");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }
            else if(panEditDelete==ARREARS){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectSalaryMasterDetails");
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
                if(viewType == INCREMENT_ID && panIncrementDetails.isShowing()==true){
                    panEditDelete=INCREMENT;
                    txtEmployeeId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                    txtIncrementEmpName.setText(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                    tdtIncrementDate.setDateValue(CommonUtil.convertObjToStr(hashMap.get("LAST_INCREMENT_DATE")));
                    tdtIncrementEffectiveDateValue.setDateValue(CommonUtil.convertObjToStr(hashMap.get("NEXT_INCREMENT_DATE")));
                    txtEmployeeGrade.setText(CommonUtil.convertObjToStr(hashMap.get("PRESENT_GRADE")));
                    txtIncrementDesignation.setText(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                    txtBasicSalary.setText(CommonUtil.convertObjToStr(hashMap.get("BASICS")));
                    txtIncrementAmount.setText(CommonUtil.convertObjToStr(hashMap.get("INCREMENT_AMT")));
                    txtIncrementNo.setText(CommonUtil.convertObjToStr(hashMap.get("NO_OF_INCREMENTS")));
                    int newIncrement =CommonUtil.convertObjToInt(txtIncrementNo.getText()) + 1;
                    double newBasic = CommonUtil.convertObjToDouble(txtIncrementAmount.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtBasicSalary.getText()).doubleValue();
                    txtNewBasic.setText(String.valueOf(newBasic));
                    txtIncrementNo.setText(String.valueOf(newIncrement));
                    if(updateLoanValues == false){
                        updateLoanValues = true;
                    }
                }
                else if(viewType == FROM_EMPLOYEE_ID){
                    HashMap where = new HashMap();
                    hashMap.put("CURRENT_DT",curDate);
                    List maxEbid =(List) ClientUtil.executeQuery("getMaxEbidFrmEmpID", hashMap);
                    where = (HashMap) maxEbid.get(0);
                    System.out.println("where map@#$@#$ :"+where);
                    observable.setTxtFromEmpIdValue(CommonUtil.convertObjToStr(where.get("EMPLOYEE_ID")));
                    txtFromEmpIdValue.setText(CommonUtil.convertObjToStr(where.get("EMPLOYEE_ID")));
                }else if(viewType == TO_EMPLOYEE_ID){
                    HashMap where = new HashMap();
                    hashMap.put("CURRENT_DT",curDate);
                    List maxEbid =(List) ClientUtil.executeQuery("getMaxEbidFrmEmpID", hashMap);
                    where = (HashMap) maxEbid.get(0);
                    observable.setTxtToEmpIdValue(CommonUtil.convertObjToStr(where.get("EMPLOYEE_ID")));
                    txtToEmpIdValue.setText(CommonUtil.convertObjToStr(where.get("EMPLOYEE_ID")));
                }
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                this.setButtonEnableDisable();
                if(panIncrementDetails.isShowing()==true){
                    panEditDelete=INCREMENT;
                    observable.populateIncrementPromotionData(String.valueOf(hashMap.get("EMP_ID")),panEditDelete);
                }
                else if(panArrearCalculationInfo.isShowing()==true){
                    panEditDelete=ARREARS;
                    String salCalcType = "TRIAL_PROCESS";
                    observable.populateSalaryDetails(hashMap,salCalcType);
                    //                    added from here
                    Date salaryForDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashMap.get("SALARY_FROM_DT")));
                    int months = salaryForDt.getMonth();
                    txtSalFromDateMMValue.setText(String.valueOf(months+1));
                    int year = salaryForDt.getYear();
                    txtSalFromDateYYYYValue.setText(String.valueOf(year+1900));
                    HashMap salIdMap = new HashMap();
                    salIdMap.put("SALARY_ID",observable.getTxtSalaryID());
                    List salaryStatus =(List) ClientUtil.executeQuery("getSalaryStatus", salIdMap);
                    salIdMap = new HashMap();
                    salIdMap = (HashMap)salaryStatus.get(0);
                    panTable.setVisible(true);
                    panFinalAmount.setVisible(true);
                    panFromEmpId.setVisible(true);
                    lblFromEmpId.setVisible(true);
                    panToEmpId.setVisible(true);
                    lblToEmpId.setVisible(true);
                    txtSalaryID.setText(observable.getTxtSalaryID());
                    cboBranchwiseValue.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmBranchwiseValue().getDataForKey(observable.getCboBranchwiseValue())));
                    cboRegionalValue.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmRegionalValue().getDataForKey(observable.getCboRegionalValue())));
                    txtFromEmpIdValue.setText(observable.getTxtFromEmpIdValue());
                    txtEmployeeDesig.setText(observable.getTxtEmployeeDesig());
                    txtEmployeeName.setText(observable.getTxtEmployeeName());
                    txtToEmpIdValue.setText(observable.getTxtToEmpIdValue());
                    tdtSalToDateValue.setDateValue(CommonUtil.convertObjToStr(curDate));
                    tblSalaryMaster.setModel(observable.getTblSalaryMaster());
                    btnSalView.setEnabled(false);
                    btnSave.setEnabled(false);
                    if(salIdMap.get("SALARY_STATUS").equals("PROCESS")){
                        btnSalProcess.setEnabled(false);
                    }
                    else{
                        btnSalProcess.setEnabled(true);
                    }
                }
                btnCancel.setEnabled(true);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                if(panIncrementDetails.isShowing()==true){
                    panEditDelete=INCREMENT;
                }
                else if(panArrearCalculationInfo.isShowing()==true){
                    panEditDelete=ARREARS;
                }
                observable.populateIncrementPromotionData(String.valueOf(hashMap.get("EMP_ID")),panEditDelete);
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
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void update(Observable o, Object arg) {
        //        removeRadioButton();
        this.lblStatus.setText(observable.getLblStatus());
        cboBranchwiseValue.setModel(observable.getCbmBranchwiseValue());
        cboRegionalValue.setModel(observable.getCbmRegionalValue());
        this.updateTable();
        this.populateDetail();
        //        addRadioButton();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployeeId;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromEmpId;
    private com.see.truetransact.uicomponent.CButton btnIncrementDelete;
    private com.see.truetransact.uicomponent.CButton btnIncrementNew;
    private com.see.truetransact.uicomponent.CButton btnIncrementProcess;
    private com.see.truetransact.uicomponent.CButton btnIncrementSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSalProcess;
    private com.see.truetransact.uicomponent.CButton btnSalView;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToEmpId;
    private com.see.truetransact.uicomponent.CButton btnTrialProcess;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchwiseValue;
    private com.see.truetransact.uicomponent.CComboBox cboRegionalValue;
    private com.see.truetransact.uicomponent.CLabel lbSalaryTypeFromDate;
    private com.see.truetransact.uicomponent.CLabel lblBasicSalary;
    private com.see.truetransact.uicomponent.CLabel lblBranchWise;
    private com.see.truetransact.uicomponent.CLabel lblEarnOrDeduction;
    private com.see.truetransact.uicomponent.CLabel lblEarnOrDeduction1;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeDesig;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeGrade;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblFromEmpId;
    private com.see.truetransact.uicomponent.CLabel lblHaltingParameterBasedon4;
    private com.see.truetransact.uicomponent.CLabel lblHaltingParameterBasedon5;
    private com.see.truetransact.uicomponent.CLabel lblIncrementAmount;
    private com.see.truetransact.uicomponent.CLabel lblIncrementCreatedDate;
    private com.see.truetransact.uicomponent.CLabel lblIncrementDate;
    private com.see.truetransact.uicomponent.CLabel lblIncrementDesignation;
    private com.see.truetransact.uicomponent.CLabel lblIncrementEffectiveDate;
    private com.see.truetransact.uicomponent.CLabel lblIncrementEmpId;
    private com.see.truetransact.uicomponent.CLabel lblIncrementEmpName;
    private com.see.truetransact.uicomponent.CLabel lblIncrementFor;
    private com.see.truetransact.uicomponent.CLabel lblIncrementFromDate;
    private com.see.truetransact.uicomponent.CLabel lblIncrementNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetSalary;
    private com.see.truetransact.uicomponent.CLabel lblNewBasic;
    private com.see.truetransact.uicomponent.CLabel lblRegional;
    private com.see.truetransact.uicomponent.CLabel lblSalFromDateFormatValue;
    private com.see.truetransact.uicomponent.CLabel lblSalaryID;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToEmpId;
    private com.see.truetransact.uicomponent.CLabel lblTotalDeduction;
    private com.see.truetransact.uicomponent.CLabel lblTotalEarnings;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panArrearCalculationInfo;
    private com.see.truetransact.uicomponent.CPanel panBasedOnCriteria;
    private com.see.truetransact.uicomponent.CPanel panBasedOnCriteria1;
    private com.see.truetransact.uicomponent.CPanel panDeductionButtons;
    private com.see.truetransact.uicomponent.CPanel panEarningInfo1;
    private com.see.truetransact.uicomponent.CPanel panFinalAmount;
    private com.see.truetransact.uicomponent.CPanel panFromEmpId;
    private com.see.truetransact.uicomponent.CPanel panIncrementDetails;
    private com.see.truetransact.uicomponent.CPanel panIncrementDetailsInfo;
    private com.see.truetransact.uicomponent.CPanel panIncrementEmpId;
    private com.see.truetransact.uicomponent.CPanel panIncrementFromDateInfo;
    private com.see.truetransact.uicomponent.CPanel panIncrementInfo;
    private com.see.truetransact.uicomponent.CPanel panIncrementTable;
    private com.see.truetransact.uicomponent.CPanel panMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CPanel panProcessType;
    private com.see.truetransact.uicomponent.CPanel panProductId1;
    private com.see.truetransact.uicomponent.CPanel panSalFromDateInfo;
    private com.see.truetransact.uicomponent.CPanel panSalaryInfmn;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panToEmpId;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBasedOn;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDeductionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgProcessType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTypeOfMode;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnAll;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnBranch;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnEmp;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnZonal;
    private com.see.truetransact.uicomponent.CRadioButton rdoIncrementForAll;
    private com.see.truetransact.uicomponent.CRadioButton rdoIncrementForSingle;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessTypeFinal;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessTypeTrial;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessTypeTrialProcess;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg1;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpIncrement;
    private com.see.truetransact.uicomponent.CTabbedPane tabMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CTable tblIncrement;
    private com.see.truetransact.uicomponent.CTable tblSalaryMaster;
    private com.see.truetransact.uicomponent.CTable tblSalaryMasterDtails;
    private javax.swing.JToolBar tbrMisecllaniousDeductions;
    private com.see.truetransact.uicomponent.CDateField tdtIncrementCreatedDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtIncrementDate;
    private com.see.truetransact.uicomponent.CDateField tdtIncrementEffectiveDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtSalToDateValue;
    private com.see.truetransact.uicomponent.CTextField txtBasicSalary;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeDesig;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeGrade;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeName;
    private com.see.truetransact.uicomponent.CTextField txtFromEmpIdValue;
    private com.see.truetransact.uicomponent.CTextField txtIncrementAmount;
    private com.see.truetransact.uicomponent.CTextField txtIncrementDesignation;
    private com.see.truetransact.uicomponent.CTextField txtIncrementEmpName;
    private com.see.truetransact.uicomponent.CTextField txtIncrementNo;
    private com.see.truetransact.uicomponent.CTextField txtNetSalary;
    private com.see.truetransact.uicomponent.CTextField txtNewBasic;
    private com.see.truetransact.uicomponent.CTextField txtSalFromDateMMValue;
    private com.see.truetransact.uicomponent.CTextField txtSalFromDateMMValue1;
    private com.see.truetransact.uicomponent.CTextField txtSalFromDateYYYYValue;
    private com.see.truetransact.uicomponent.CTextField txtSalFromDateYYYYValue1;
    private com.see.truetransact.uicomponent.CTextField txtSalaryID;
    private com.see.truetransact.uicomponent.CTextField txtToEmpIdValue;
    private com.see.truetransact.uicomponent.CTextField txtTotalDeduction;
    private com.see.truetransact.uicomponent.CTextField txtTotalEarnings;
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

