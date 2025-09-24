  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * editMigratedDataUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */

package com.see.truetransact.ui.transaction.editMigratedData;

import java.util.*;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author Suresh R
 *
 */

public class EditMigratedDataUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.editMigratedData.EditMigratedDataRB", ProxyParameters.LANGUAGE);
    final EditMigratedDataMRB objMandatoryMRB = new EditMigratedDataMRB();
    private HashMap mandatoryMap;
    private String viewType = "";
    private Date curr_dt=null;
    int updateTab=-1;
    
    private int totalMonths = 12;
    private int totalDays = 365;
    private int daysInMonth = 31;
    private int moreDays = 32;
    private int perHalfYear = 2;
    private int perQuarterYear = 4;
    private int perMonth = 12;
    private double yr = 0;
    
    private int yearTobeAdded = 1900;
    private boolean updateMode = false;
    private EditMigratedDataOB observable;
    /** Creates new form BeanForm */
    public EditMigratedDataUI() {
        initComponents();
        tabRemittanceProduct.resetVisits();
        settingupUI();
    }
    
    private void settingupUI(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setMaximumLength();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panAccountDetails);
        tabRemittanceProduct.remove(panDepositInterestDetails);
        tblDepositTable.setModel(observable.getTblDepositTable());
        lblSBAccountNumberVal.setVisible(false);
        lblSBAccountNumber.setVisible(false);
        ClientUtil.enableDisable(this,false);
        curr_dt=ClientUtil.getCurrentDate();
        panDepositDetails.setVisible(true);
        panLoanDetails.setVisible(false);
        panMDSDetails.setVisible(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panAccountDetails, true);
        txtCustomerIdCr.setAllowAll(true);
        txtCustomerIdCr.setEnabled(true);
        lblNetTransID.setVisible(false);
        panNetTransID.setVisible(false);
        panFreeze.setVisible(false);
        lbDepositlFreeze.setVisible(false);
        btnNew.setVisible(false);
        btnDelete.setVisible(false);
        btnSave.setVisible(false);
        setSizeTableData();
    }
    
    /* Creates the insstance of ShareProduct which acts as  Observable to
     *ShareProduct UI */
    private void setObservable() {
        try{
            observable = EditMigratedDataOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void update(Observable observed, Object arg) {
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnClear.setName("btnClear");
        btnCustomerIdFileOpenCr.setName("btnCustomerIdFileOpenCr");
        btnReprintClose.setName("btnReprintClose");
        btnUpdate.setName("btnUpdate");
        cboProdId.setName("cboProdId");
        cboProdType.setName("cboProdType");
        lblCustomerIdCr.setName("lblCustomerIdCr");
        lblLoanIntCalcDt.setName("lblLoanIntCalcDt");
        lblMemberName.setName("lblMemberName");
        lblMsg.setName("lblMsg");
        lblProdId.setName("lblProdId");
        lblProdType.setName("lblProdType");
        lblSpace.setName("lblSpace");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panAccountDetails.setName("panAccountDetails");
        panCustomerNO.setName("panCustomerNO");
        panDepositDetails.setName("panDepositDetails");
        panInsideMigratedDetails.setName("panInsideMigratedDetails");
        panLoanDetails.setName("panLoanDetails");
        panMDSDetails.setName("panMDSDetails");
        panMigratedDetails.setName("panMigratedDetails");
        panReprintBtn.setName("panReprintBtn");
        panStatus.setName("panStatus");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tdtLoanIntCalcDt.setName("tdtLoanIntCalcDt");
        txtCustomerIdCr.setName("txtCustomerIdCr");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblCustomerIdCr.setText(resourceBundle.getString("lblCustomerIdCr"));
        ((javax.swing.border.TitledBorder)panDepositDetails.getBorder()).setTitle(resourceBundle.getString("panDepositDetails"));
        btnClear.setText(resourceBundle.getString("btnClear"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        ((javax.swing.border.TitledBorder)panLoanDetails.getBorder()).setTitle(resourceBundle.getString("panLoanDetails"));
        btnReprintClose.setText(resourceBundle.getString("btnReprintClose"));
        btnUpdate.setText(resourceBundle.getString("btnUpdate"));
        btnCustomerIdFileOpenCr.setText(resourceBundle.getString("btnCustomerIdFileOpenCr"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblLoanIntCalcDt.setText(resourceBundle.getString("lblLoanIntCalcDt"));
        ((javax.swing.border.TitledBorder)panMDSDetails.getBorder()).setTitle(resourceBundle.getString("panMDSDetails"));
        ((javax.swing.border.TitledBorder)panAccountDetails.getBorder()).setTitle(resourceBundle.getString("panAccountDetails"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerIdCr", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        try{
            cboProdType.setModel(observable.getCbmProdType());
            cboIntFrequency.setModel(observable.getCbmIntFrequency());
            cboInterestType.setModel(observable.getCbmInterestType());
            cboSanRepaymentType.setModel(observable.getCbmSanRepaymentType());
            cboRepayFreq.setModel(observable.getCbmRepayFreq());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        txtDepositAmount.setValidation(new CurrencyValidation(14,2));
        txtMaturityAmt.setValidation(new CurrencyValidation(14,2));
        txtRateOfInterest.setValidation(new NumericValidation(2,2));
        txtTotalIntAmt.setValidation(new CurrencyValidation(14,2));
        txtPeriodicIntAmt.setValidation(new CurrencyValidation(14,2));
        txtTotalIntDrawn.setValidation(new CurrencyValidation(14,2));
        txtTotalCredit.setValidation(new CurrencyValidation(14,2));
        txtInstallmentAmount.setValidation(new CurrencyValidation(14,2));
        txtInterestAmt.setValidation(new CurrencyValidation(14,2));
        txtBonusAmt.setValidation(new CurrencyValidation(14,2));
        txtDiscountAmt.setValidation(new CurrencyValidation(14,2));
        txtNetAmt.setValidation(new CurrencyValidation(14,2));
        txtInstPayable.setValidation(new CurrencyValidation(14,2));
        txtNoOfInstPay.setValidation(new NumericValidation(2,0));
        txtLoanROI.setValidation(new NumericValidation(2,2));
        txtInterestAmount.setValidation(new CurrencyValidation(14,2));
        txtIntAmount.setValidation(new CurrencyValidation(14,2));
        txtNoOfInstallPaid.setValidation(new NumericValidation(3,0));
        txtPeriodOfDeposit_Months.setMaxLength(3);
        txtPeriodOfDeposit_Months.setValidation(new NumericValidation());
        txtPeriodOfDeposit_Days.setMaxLength(4);
        txtPeriodOfDeposit_Days.setValidation(new NumericValidation());
        txtPeriodOfDeposit_Years.setMaxLength(2);
        txtPeriodOfDeposit_Years.setValidation(new NumericValidation());
        txtNoInstallments.setMaxLength(3);
        txtNoInstallments.setValidation(new NumericValidation());
        txtLimit.setMaxLength(16);
        txtLimit.setValidation(new CurrencyValidation(14, 2));
    }
    
    //    /** Auto Generated Method - update()
    //     * This method called by Observable. It updates the UI with
    //     * Observable's data. If needed add/Remove RadioButtons
    //     * method need to be added.*/
    //    public void update(Observable observed, Object arg) {
    //        //        tblMemberReceipt.setModel(observable.getTblMemberRecord());
    //    }
    
    public void updateOBFields() {
        observable.setAct_Num(CommonUtil.convertObjToStr(txtCustomerIdCr.getText()));
        observable.setCboProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()));
        observable.setCboProdId(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString()));
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        EditMigratedDataMRB objMandatoryRB = new EditMigratedDataMRB();
        txtCustomerIdCr.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerIdCr"));
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgFreeze = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panMigratedDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideMigratedDetails = new com.see.truetransact.uicomponent.CPanel();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        panDepositDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        txtRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        lblIntFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboIntFrequency = new com.see.truetransact.uicomponent.CComboBox();
        panPeriodOfDeposit = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodOfDeposit_Years = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Years = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodOfDeposit_Months = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Months = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodOfDeposit_Days = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod_Days = new com.see.truetransact.uicomponent.CLabel();
        lblDepositPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtDepositDate = new com.see.truetransact.uicomponent.CDateField();
        lblDepositDate = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        lblDepositAmount = new com.see.truetransact.uicomponent.CLabel();
        txtDepositAmount = new com.see.truetransact.uicomponent.CTextField();
        panDepositDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblDepLastInterestAppDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDepLastInterestAppDt = new com.see.truetransact.uicomponent.CDateField();
        lblDepNextIntAppDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDepNextIntAppDt = new com.see.truetransact.uicomponent.CDateField();
        lblTotalIntDrawn = new com.see.truetransact.uicomponent.CLabel();
        txtTotalIntDrawn = new com.see.truetransact.uicomponent.CTextField();
        lbDepositlFreeze = new com.see.truetransact.uicomponent.CLabel();
        panFreeze = new com.see.truetransact.uicomponent.CPanel();
        rdoFreeze_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFreeze_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtTotalCredit = new com.see.truetransact.uicomponent.CTextField();
        lblTotalCredit = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaturityAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTotalIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodicIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodicIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDepositNomineeName = new com.see.truetransact.uicomponent.CLabel();
        lblDepositNomineeNameVal = new com.see.truetransact.uicomponent.CLabel();
        panLoanDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLoanIntCalcDt = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanIntCalcDt = new com.see.truetransact.uicomponent.CDateField();
        lblLoanROI = new com.see.truetransact.uicomponent.CLabel();
        txtLoanROI = new com.see.truetransact.uicomponent.CTextField();
        lblLimit = new com.see.truetransact.uicomponent.CLabel();
        txtLimit = new com.see.truetransact.uicomponent.CTextField();
        lblSanRepaymentType = new com.see.truetransact.uicomponent.CLabel();
        cboSanRepaymentType = new com.see.truetransact.uicomponent.CComboBox();
        lblRepayFreq = new com.see.truetransact.uicomponent.CLabel();
        cboRepayFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblNoInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtNoInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblFDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFDate = new com.see.truetransact.uicomponent.CDateField();
        lblTDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTDate = new com.see.truetransact.uicomponent.CDateField();
        lblSanctionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSanctionDate = new com.see.truetransact.uicomponent.CDateField();
        lblBorrowNo = new com.see.truetransact.uicomponent.CLabel();
        lblBorrowNoVal = new com.see.truetransact.uicomponent.CLabel();
        panMDSDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInstallmentAmount = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfInstPay = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentAmount = new com.see.truetransact.uicomponent.CTextField();
        txtDiscountAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNetAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstPay = new com.see.truetransact.uicomponent.CTextField();
        lblTransactionDt = new com.see.truetransact.uicomponent.CLabel();
        lblInterestAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtTransactionDt = new com.see.truetransact.uicomponent.CDateField();
        lblDiscountAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNetAmt = new com.see.truetransact.uicomponent.CTextField();
        txtInterestAmt = new com.see.truetransact.uicomponent.CTextField();
        lblBonusAmt = new com.see.truetransact.uicomponent.CLabel();
        txtBonusAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInstPayable = new com.see.truetransact.uicomponent.CLabel();
        txtInstPayable = new com.see.truetransact.uicomponent.CTextField();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        panCustomerNO = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        lblCustomerIdCr = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        panNetTransID = new com.see.truetransact.uicomponent.CPanel();
        txtNetTransID = new com.see.truetransact.uicomponent.CTextField();
        btnNetTransID = new com.see.truetransact.uicomponent.CButton();
        lblNetTransID = new com.see.truetransact.uicomponent.CLabel();
        lblSBAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblSBAccountNumberVal = new com.see.truetransact.uicomponent.CLabel();
        panReprintBtn = new com.see.truetransact.uicomponent.CPanel();
        btnReprintClose = new com.see.truetransact.uicomponent.CButton();
        btnUpdate = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        panDepositInterestDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideDepositInterestDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInterestDt = new com.see.truetransact.uicomponent.CLabel();
        lblInterestAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInterestAmount = new com.see.truetransact.uicomponent.CTextField();
        panDepositBtn = new com.see.truetransact.uicomponent.CPanel();
        btnDepositNew = new com.see.truetransact.uicomponent.CButton();
        btnDepositSave = new com.see.truetransact.uicomponent.CButton();
        btnDepositDelete = new com.see.truetransact.uicomponent.CButton();
        tdtInterestDt = new com.see.truetransact.uicomponent.CDateField();
        cboInterestType = new com.see.truetransact.uicomponent.CComboBox();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIntAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalIntAmountVal = new com.see.truetransact.uicomponent.CLabel();
        panDepositInterestTable = new com.see.truetransact.uicomponent.CPanel();
        srpInterestTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositTable = new com.see.truetransact.uicomponent.CTable();
        panAccountDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        panDepositAddTableDetails = new com.see.truetransact.uicomponent.CPanel();
        lblIntAmount = new com.see.truetransact.uicomponent.CLabel();
        txtIntAmount = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfInstallPaid = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstallPaid = new com.see.truetransact.uicomponent.CTextField();
        lbStartDate = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDate = new com.see.truetransact.uicomponent.CDateField();
        btnAddTable = new com.see.truetransact.uicomponent.CButton();
        panDepositInterestDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerIdCrNo = new com.see.truetransact.uicomponent.CLabel();
        lblDepositMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblIntDepositNo = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(820, 650));
        setPreferredSize(new java.awt.Dimension(820, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(850, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(850, 480));

        panMigratedDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMigratedDetails.setMinimumSize(new java.awt.Dimension(850, 500));
        panMigratedDetails.setPreferredSize(new java.awt.Dimension(850, 500));
        panMigratedDetails.setLayout(new java.awt.GridBagLayout());

        panInsideMigratedDetails.setMinimumSize(new java.awt.Dimension(740, 360));
        panInsideMigratedDetails.setPreferredSize(new java.awt.Dimension(740, 360));
        panInsideMigratedDetails.setLayout(new java.awt.GridBagLayout());

        panDepositDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Account Details"));
        panDepositDetails.setMinimumSize(new java.awt.Dimension(720, 345));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(720, 345));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepositDetails1.setMinimumSize(new java.awt.Dimension(350, 250));
        panDepositDetails1.setPreferredSize(new java.awt.Dimension(350, 250));
        panDepositDetails1.setLayout(new java.awt.GridBagLayout());

        lblRateOfInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(lblRateOfInterest, gridBagConstraints);

        txtRateOfInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateOfInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRateOfInterestFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(txtRateOfInterest, gridBagConstraints);

        lblIntFrequency.setText("Int Pay Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(lblIntFrequency, gridBagConstraints);

        cboIntFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboIntFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntFrequency.setPopupWidth(125);
        cboIntFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntFrequencyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(cboIntFrequency, gridBagConstraints);

        panPeriodOfDeposit.setLayout(new java.awt.GridBagLayout());

        txtPeriodOfDeposit_Years.setMinimumSize(new java.awt.Dimension(20, 21));
        txtPeriodOfDeposit_Years.setPreferredSize(new java.awt.Dimension(20, 21));
        txtPeriodOfDeposit_Years.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPeriodOfDeposit_YearsFocusLost(evt);
            }
        });
        panPeriodOfDeposit.add(txtPeriodOfDeposit_Years, new java.awt.GridBagConstraints());

        lblPeriod_Years.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panPeriodOfDeposit.add(lblPeriod_Years, gridBagConstraints);

        txtPeriodOfDeposit_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtPeriodOfDeposit_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        txtPeriodOfDeposit_Months.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPeriodOfDeposit_MonthsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(txtPeriodOfDeposit_Months, gridBagConstraints);

        lblPeriod_Months.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(lblPeriod_Months, gridBagConstraints);

        txtPeriodOfDeposit_Days.setAllowAll(true);
        txtPeriodOfDeposit_Days.setMinimumSize(new java.awt.Dimension(45, 21));
        txtPeriodOfDeposit_Days.setPreferredSize(new java.awt.Dimension(45, 21));
        txtPeriodOfDeposit_Days.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPeriodOfDeposit_DaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(txtPeriodOfDeposit_Days, gridBagConstraints);

        lblPeriod_Days.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(lblPeriod_Days, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panDepositDetails1.add(panPeriodOfDeposit, gridBagConstraints);

        lblDepositPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositPeriod.setText("Period Of Deposit");
        lblDepositPeriod.setMaximumSize(new java.awt.Dimension(115, 18));
        lblDepositPeriod.setMinimumSize(new java.awt.Dimension(115, 18));
        lblDepositPeriod.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        panDepositDetails1.add(lblDepositPeriod, gridBagConstraints);

        tdtDepositDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDepositDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(tdtDepositDate, gridBagConstraints);

        lblDepositDate.setText("Date Of Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(lblDepositDate, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(lblMaturityDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(tdtMaturityDate, gridBagConstraints);

        lblDepositAmount.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(lblDepositAmount, gridBagConstraints);

        txtDepositAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepositAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails1.add(txtDepositAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 4);
        panDepositDetails.add(panDepositDetails1, gridBagConstraints);

        panDepositDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepositDetails2.setMinimumSize(new java.awt.Dimension(325, 250));
        panDepositDetails2.setPreferredSize(new java.awt.Dimension(325, 250));
        panDepositDetails2.setLayout(new java.awt.GridBagLayout());

        lblDepLastInterestAppDt.setText("Last Int Application Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblDepLastInterestAppDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(tdtDepLastInterestAppDt, gridBagConstraints);

        lblDepNextIntAppDt.setText("Next Int Application Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblDepNextIntAppDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(tdtDepNextIntAppDt, gridBagConstraints);

        lblTotalIntDrawn.setText("Total Interest Drawn");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblTotalIntDrawn, gridBagConstraints);

        txtTotalIntDrawn.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalIntDrawn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalIntDrawnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(txtTotalIntDrawn, gridBagConstraints);

        lbDepositlFreeze.setText("Freeze");
        lbDepositlFreeze.setMaximumSize(new java.awt.Dimension(0, 0));
        lbDepositlFreeze.setMinimumSize(new java.awt.Dimension(0, 0));
        lbDepositlFreeze.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lbDepositlFreeze, gridBagConstraints);

        panFreeze.setMinimumSize(new java.awt.Dimension(0, 0));
        panFreeze.setPreferredSize(new java.awt.Dimension(0, 0));
        panFreeze.setLayout(new java.awt.GridBagLayout());

        rdgFreeze.add(rdoFreeze_Yes);
        rdoFreeze_Yes.setText("Yes");
        rdoFreeze_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoFreeze_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoFreeze_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panFreeze.add(rdoFreeze_Yes, gridBagConstraints);

        rdgFreeze.add(rdoFreeze_No);
        rdoFreeze_No.setText("No");
        rdoFreeze_No.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoFreeze_No.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoFreeze_No.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panFreeze.add(rdoFreeze_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        panDepositDetails2.add(panFreeze, gridBagConstraints);

        txtTotalCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(txtTotalCredit, gridBagConstraints);

        lblTotalCredit.setText("Total Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblTotalCredit, gridBagConstraints);

        lblMaturityAmt.setText("Maturity Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblMaturityAmt, gridBagConstraints);

        txtMaturityAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(txtMaturityAmt, gridBagConstraints);

        lblTotalIntAmt.setText("Total Int Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblTotalIntAmt, gridBagConstraints);

        txtTotalIntAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalIntAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalIntAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(txtTotalIntAmt, gridBagConstraints);

        lblPeriodicIntAmt.setText("Periodic Int Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(lblPeriodicIntAmt, gridBagConstraints);

        txtPeriodicIntAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panDepositDetails2.add(txtPeriodicIntAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 15, 0);
        panDepositDetails.add(panDepositDetails2, gridBagConstraints);

        lblDepositNomineeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositNomineeName.setText("Nominee Name : ");
        lblDepositNomineeName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDepositNomineeName.setMaximumSize(new java.awt.Dimension(115, 18));
        lblDepositNomineeName.setMinimumSize(new java.awt.Dimension(115, 18));
        lblDepositNomineeName.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 14, 3);
        panDepositDetails.add(lblDepositNomineeName, gridBagConstraints);

        lblDepositNomineeNameVal.setForeground(new java.awt.Color(0, 0, 255));
        lblDepositNomineeNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDepositNomineeNameVal.setMaximumSize(new java.awt.Dimension(320, 18));
        lblDepositNomineeNameVal.setMinimumSize(new java.awt.Dimension(320, 18));
        lblDepositNomineeNameVal.setPreferredSize(new java.awt.Dimension(320, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 14, 3);
        panDepositDetails.add(lblDepositNomineeNameVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMigratedDetails.add(panDepositDetails, gridBagConstraints);

        panLoanDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan Account Details"));
        panLoanDetails.setMinimumSize(new java.awt.Dimension(720, 345));
        panLoanDetails.setPreferredSize(new java.awt.Dimension(720, 345));
        panLoanDetails.setLayout(new java.awt.GridBagLayout());

        lblLoanIntCalcDt.setText("Last Interest Calc Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 135, 4, 4);
        panLoanDetails.add(lblLoanIntCalcDt, gridBagConstraints);

        tdtLoanIntCalcDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLoanIntCalcDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(tdtLoanIntCalcDt, gridBagConstraints);

        lblLoanROI.setText("ROI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblLoanROI, gridBagConstraints);

        txtLoanROI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(txtLoanROI, gridBagConstraints);

        lblLimit.setText("Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblLimit, gridBagConstraints);

        txtLimit.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimitFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(txtLimit, gridBagConstraints);

        lblSanRepaymentType.setText("Repayment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblSanRepaymentType, gridBagConstraints);

        cboSanRepaymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSanRepaymentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanRepaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSanRepaymentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(cboSanRepaymentType, gridBagConstraints);

        lblRepayFreq.setText("Repay Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblRepayFreq, gridBagConstraints);

        cboRepayFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepayFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(cboRepayFreq, gridBagConstraints);

        lblNoInstallments.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblNoInstallments, gridBagConstraints);

        txtNoInstallments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoInstallments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoInstallmentsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(txtNoInstallments, gridBagConstraints);

        lblFDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblFDate, gridBagConstraints);

        tdtFDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tdtFDateMouseExited(evt);
            }
        });
        tdtFDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(tdtFDate, gridBagConstraints);

        lblTDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblTDate, gridBagConstraints);

        tdtTDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtTDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(tdtTDate, gridBagConstraints);

        lblSanctionDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblSanctionDate, gridBagConstraints);

        tdtSanctionDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSanctionDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSanctionDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSanctionDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(tdtSanctionDate, gridBagConstraints);

        lblBorrowNo.setText("Borrower No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblBorrowNo, gridBagConstraints);

        lblBorrowNoVal.setForeground(new java.awt.Color(0, 0, 255));
        lblBorrowNoVal.setText("Borrower No");
        lblBorrowNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(lblBorrowNoVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMigratedDetails.add(panLoanDetails, gridBagConstraints);

        panMDSDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Chittal Details"));
        panMDSDetails.setMinimumSize(new java.awt.Dimension(720, 345));
        panMDSDetails.setPreferredSize(new java.awt.Dimension(720, 345));
        panMDSDetails.setLayout(new java.awt.GridBagLayout());

        lblInstallmentAmount.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblInstallmentAmount, gridBagConstraints);

        lblNoOfInstPay.setText("No Of Installments to Pay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblNoOfInstPay, gridBagConstraints);

        txtInstallmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtInstallmentAmount, gridBagConstraints);

        txtDiscountAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDiscountAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtDiscountAmt, gridBagConstraints);

        lblNetAmt.setText("Net Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblNetAmt, gridBagConstraints);

        txtNoOfInstPay.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfInstPay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfInstPayFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtNoOfInstPay, gridBagConstraints);

        lblTransactionDt.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblTransactionDt, gridBagConstraints);

        lblInterestAmt.setText("Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblInterestAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(tdtTransactionDt, gridBagConstraints);

        lblDiscountAmt.setText("Discount Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblDiscountAmt, gridBagConstraints);

        txtNetAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtNetAmt, gridBagConstraints);

        txtInterestAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtInterestAmt, gridBagConstraints);

        lblBonusAmt.setText("Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblBonusAmt, gridBagConstraints);

        txtBonusAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBonusAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtBonusAmt, gridBagConstraints);

        lblInstPayable.setText("Installment Amount Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(lblInstPayable, gridBagConstraints);

        txtInstPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSDetails.add(txtInstPayable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideMigratedDetails.add(panMDSDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panMigratedDetails.add(panInsideMigratedDetails, gridBagConstraints);

        panAccountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Product Details"));
        panAccountDetails.setMinimumSize(new java.awt.Dimension(720, 135));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(720, 135));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        lblMemberName.setForeground(new java.awt.Color(0, 0, 255));
        lblMemberName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberName.setMaximumSize(new java.awt.Dimension(350, 18));
        lblMemberName.setMinimumSize(new java.awt.Dimension(350, 18));
        lblMemberName.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails.add(lblMemberName, gridBagConstraints);

        panCustomerNO.setMinimumSize(new java.awt.Dimension(122, 21));
        panCustomerNO.setPreferredSize(new java.awt.Dimension(122, 21));
        panCustomerNO.setLayout(new java.awt.GridBagLayout());

        txtCustomerIdCr.setEditable(false);
        txtCustomerIdCr.setMinimumSize(new java.awt.Dimension(97, 21));
        txtCustomerIdCr.setPreferredSize(new java.awt.Dimension(97, 21));
        txtCustomerIdCr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdCrFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(txtCustomerIdCr, gridBagConstraints);

        btnCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdFileOpenCr.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(btnCustomerIdFileOpenCr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountDetails.add(panCustomerNO, gridBagConstraints);

        lblCustomerIdCr.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblCustomerIdCr, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(230);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(cboProdId, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
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
        panAccountDetails.add(cboProdType, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblProdType, gridBagConstraints);

        panNetTransID.setMinimumSize(new java.awt.Dimension(122, 21));
        panNetTransID.setPreferredSize(new java.awt.Dimension(122, 21));
        panNetTransID.setLayout(new java.awt.GridBagLayout());

        txtNetTransID.setEditable(false);
        txtNetTransID.setMinimumSize(new java.awt.Dimension(97, 21));
        txtNetTransID.setPreferredSize(new java.awt.Dimension(97, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNetTransID.add(txtNetTransID, gridBagConstraints);

        btnNetTransID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNetTransID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnNetTransID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNetTransID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNetTransID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetTransIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNetTransID.add(btnNetTransID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountDetails.add(panNetTransID, gridBagConstraints);

        lblNetTransID.setText("Net Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblNetTransID, gridBagConstraints);

        lblSBAccountNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSBAccountNumber.setText("SB Account No  : ");
        lblSBAccountNumber.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSBAccountNumber.setMaximumSize(new java.awt.Dimension(115, 18));
        lblSBAccountNumber.setMinimumSize(new java.awt.Dimension(115, 18));
        lblSBAccountNumber.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblSBAccountNumber, gridBagConstraints);

        lblSBAccountNumberVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSBAccountNumberVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblSBAccountNumberVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSBAccountNumberVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panAccountDetails.add(lblSBAccountNumberVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 4, 4);
        panMigratedDetails.add(panAccountDetails, gridBagConstraints);

        panReprintBtn.setMinimumSize(new java.awt.Dimension(600, 35));
        panReprintBtn.setPreferredSize(new java.awt.Dimension(600, 35));
        panReprintBtn.setLayout(new java.awt.GridBagLayout());

        btnReprintClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnReprintClose.setText("Close");
        btnReprintClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnReprintClose, gridBagConstraints);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnUpdate, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnClear, gridBagConstraints);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(95, 27));
        btnDelete.setMinimumSize(new java.awt.Dimension(95, 27));
        btnDelete.setPreferredSize(new java.awt.Dimension(95, 27));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnDelete, gridBagConstraints);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setText("New");
        btnNew.setToolTipText("New");
        btnNew.setMaximumSize(new java.awt.Dimension(80, 27));
        btnNew.setMinimumSize(new java.awt.Dimension(80, 27));
        btnNew.setPreferredSize(new java.awt.Dimension(80, 27));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnNew, gridBagConstraints);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setToolTipText("Save");
        btnSave.setMaximumSize(new java.awt.Dimension(85, 27));
        btnSave.setMinimumSize(new java.awt.Dimension(85, 27));
        btnSave.setName("btnContactNoAdd");
        btnSave.setPreferredSize(new java.awt.Dimension(85, 27));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 15, 4);
        panMigratedDetails.add(panReprintBtn, gridBagConstraints);

        tabRemittanceProduct.addTab("Edit Migrated Data", panMigratedDetails);

        panDepositInterestDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Interest Details"));
        panDepositInterestDetails.setMinimumSize(new java.awt.Dimension(810, 430));
        panDepositInterestDetails.setPreferredSize(new java.awt.Dimension(810, 430));
        panDepositInterestDetails.setLayout(new java.awt.GridBagLayout());

        panInsideDepositInterestDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsideDepositInterestDetails.setMinimumSize(new java.awt.Dimension(790, 160));
        panInsideDepositInterestDetails.setPreferredSize(new java.awt.Dimension(790, 165));
        panInsideDepositInterestDetails.setLayout(new java.awt.GridBagLayout());

        lblInterestDt.setText("Interest Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 89, 4, 4);
        panInsideDepositInterestDetails.add(lblInterestDt, gridBagConstraints);

        lblInterestAmount.setText("Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 89, 4, 4);
        panInsideDepositInterestDetails.add(lblInterestAmount, gridBagConstraints);

        txtInterestAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositInterestDetails.add(txtInterestAmount, gridBagConstraints);

        panDepositBtn.setMinimumSize(new java.awt.Dimension(110, 40));
        panDepositBtn.setPreferredSize(new java.awt.Dimension(110, 40));
        panDepositBtn.setLayout(new java.awt.GridBagLayout());

        btnDepositNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnDepositNew.setToolTipText("New");
        btnDepositNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositBtn.add(btnDepositNew, gridBagConstraints);

        btnDepositSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnDepositSave.setToolTipText("Save");
        btnDepositSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setName("btnContactNoAdd");
        btnDepositSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositBtn.add(btnDepositSave, gridBagConstraints);

        btnDepositDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDepositDelete.setToolTipText("Delete");
        btnDepositDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositBtn.add(btnDepositDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 2, 1);
        panInsideDepositInterestDetails.add(panDepositBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositInterestDetails.add(tdtInterestDt, gridBagConstraints);

        cboInterestType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInterestType.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositInterestDetails.add(cboInterestType, gridBagConstraints);

        lblInterestType.setText("Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 89, 4, 4);
        panInsideDepositInterestDetails.add(lblInterestType, gridBagConstraints);

        lblTotalIntAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalIntAmount.setText("Total Interest Amount  : ");
        lblTotalIntAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalIntAmount.setMaximumSize(new java.awt.Dimension(160, 18));
        lblTotalIntAmount.setMinimumSize(new java.awt.Dimension(160, 18));
        lblTotalIntAmount.setPreferredSize(new java.awt.Dimension(160, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 19, 4, 4);
        panInsideDepositInterestDetails.add(lblTotalIntAmount, gridBagConstraints);

        lblTotalIntAmountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalIntAmountVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblTotalIntAmountVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTotalIntAmountVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panInsideDepositInterestDetails.add(lblTotalIntAmountVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInterestDetails.add(panInsideDepositInterestDetails, gridBagConstraints);

        panDepositInterestTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepositInterestTable.setMinimumSize(new java.awt.Dimension(790, 220));
        panDepositInterestTable.setPreferredSize(new java.awt.Dimension(790, 220));
        panDepositInterestTable.setLayout(new java.awt.GridBagLayout());

        srpInterestTable.setMinimumSize(new java.awt.Dimension(770, 200));
        srpInterestTable.setPreferredSize(new java.awt.Dimension(770, 200));

        tblDepositTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Interest Dt", "Interest Amount", "Interest Type"
            }
        ));
        tblDepositTable.setMinimumSize(new java.awt.Dimension(810, 3000));
        tblDepositTable.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 296));
        tblDepositTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDepositTableMousePressed(evt);
            }
        });
        srpInterestTable.setViewportView(tblDepositTable);

        panDepositInterestTable.add(srpInterestTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInterestDetails.add(panDepositInterestTable, gridBagConstraints);

        panAccountDepositDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panAccountDepositDetails.setMinimumSize(new java.awt.Dimension(790, 130));
        panAccountDepositDetails.setPreferredSize(new java.awt.Dimension(790, 130));
        panAccountDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositAddTableDetails.setMinimumSize(new java.awt.Dimension(220, 130));
        panDepositAddTableDetails.setPreferredSize(new java.awt.Dimension(220, 130));
        panDepositAddTableDetails.setLayout(new java.awt.GridBagLayout());

        lblIntAmount.setText("Int Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(lblIntAmount, gridBagConstraints);

        txtIntAmount.setEditable(false);
        txtIntAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(txtIntAmount, gridBagConstraints);

        lblNoOfInstallPaid.setText("No.Of Inst Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(lblNoOfInstallPaid, gridBagConstraints);

        txtNoOfInstallPaid.setEditable(false);
        txtNoOfInstallPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(txtNoOfInstallPaid, gridBagConstraints);

        lbStartDate.setText("Int Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(lbStartDate, gridBagConstraints);

        tdtStartDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(tdtStartDate, gridBagConstraints);

        btnAddTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAddTable.setText("Add Table");
        btnAddTable.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnAddTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAddTableDetails.add(btnAddTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountDepositDetails.add(panDepositAddTableDetails, gridBagConstraints);

        panDepositInterestDetails2.setMinimumSize(new java.awt.Dimension(500, 130));
        panDepositInterestDetails2.setPreferredSize(new java.awt.Dimension(500, 130));
        panDepositInterestDetails2.setLayout(new java.awt.GridBagLayout());

        lblCustomerIdCrNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInterestDetails2.add(lblCustomerIdCrNo, gridBagConstraints);

        lblDepositMemberName.setForeground(new java.awt.Color(0, 51, 204));
        lblDepositMemberName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDepositMemberName.setMaximumSize(new java.awt.Dimension(350, 18));
        lblDepositMemberName.setMinimumSize(new java.awt.Dimension(350, 18));
        lblDepositMemberName.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositInterestDetails2.add(lblDepositMemberName, gridBagConstraints);

        lblIntDepositNo.setForeground(new java.awt.Color(0, 51, 204));
        lblIntDepositNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblIntDepositNo.setMaximumSize(new java.awt.Dimension(350, 18));
        lblIntDepositNo.setMinimumSize(new java.awt.Dimension(350, 18));
        lblIntDepositNo.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositInterestDetails2.add(lblIntDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountDepositDetails.add(panDepositInterestDetails2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInterestDetails.add(panAccountDepositDetails, gridBagConstraints);

        tabRemittanceProduct.addTab("Deposit Interest Details", panDepositInterestDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRemittanceProduct, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTotalIntAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalIntAmtFocusLost
        // TODO add your handling code here:
        if(cboProdId.getSelectedIndex()>0){
            String prodID=CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            List behavesLikeList = ClientUtil.executeQuery("getBehavesLike", whereMap);
            if(behavesLikeList!=null && behavesLikeList.size()>0){
                whereMap = (HashMap) behavesLikeList.get(0);
                if(whereMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")){
                    txtMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText())+CommonUtil.convertObjToDouble(txtTotalIntAmt.getText())));
                }
            }
        }
    }//GEN-LAST:event_txtTotalIntAmtFocusLost

    private void btnAddTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTableActionPerformed
        // TODO add your handling code here:
        if(tdtStartDate.getDateValue().length()>0 && txtIntAmount.getText().length()>0 && txtNoOfInstallPaid.getText().length()>0){
            if(cboIntFrequency.getSelectedIndex()>0){
                int intFreq = CommonUtil.convertObjToInt((((ComboBoxModel)cboIntFrequency.getModel()).getKeyForSelected().toString()));
                java.util.Date startDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtStartDate.getDateValue());
                double interestAmount = CommonUtil.convertObjToDouble(txtIntAmount.getText());
                int totalRow = CommonUtil.convertObjToInt(txtNoOfInstallPaid.getText());
                observable.resetTableValues();
                observable.addInterestTable(startDate,interestAmount,totalRow,intFreq);
                tblDepositTable.setModel(observable.getTblDepositTable());
                observable.resetDepositInterestDetails();
                resetDepositInterestDetails();
                ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
                enableDisableButton(false);
                btnDepositNew.setEnabled(true);
                setSizeTableData();
                calcTotalIntAmount();
                btnAddTable.setEnabled(false);
                ClientUtil.enableDisable(panDepositAddTableDetails,false);
            }else{
                ClientUtil.showAlertWindow("Interest Frequency Should not be Empty !!!...");
                return;
            }
        }else{
            ClientUtil.showAlertWindow("Int Start Date/ Int Amount/ No.Of InstPaid Should not be Empty !!!...");
            return;
        }
    }//GEN-LAST:event_btnAddTableActionPerformed

    private void txtCustomerIdCrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdCrFocusLost
        // TODO add your handling code here:
        if(txtCustomerIdCr.getText().length()>0){
            if(cboProdType.getSelectedIndex()>0){
                if(cboProdId.getSelectedIndex()>0){
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM",txtCustomerIdCr.getText());
                    whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                    whereMap.put("PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
                    String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                    
                    List accountList=ClientUtil.executeQuery("getMigratedAccountList"+ prodType,whereMap);
                    if(accountList !=null && accountList.size()>0){
                        whereMap =(HashMap)accountList.get(0);
                        viewType = "ACCOUNT_NO";
                        fillData(whereMap);
                    }else{
                        ClientUtil.showAlertWindow("Invalid Account Number...!!! ");
                        txtCustomerIdCr.setText("");
                        observable.resetForm();
                        observable.resetTableValues();
                        tblDepositTable.setModel(observable.getTblDepositTable());
                        clearUI();
                        return;
                    }
                }else{
                    ClientUtil.showAlertWindow("Product ID Should not be Empty !!!...");
                    txtCustomerIdCr.setText("");
                    return;
                }
            }else{
                ClientUtil.showAlertWindow("Product Type Should not be Empty !!!...");
                txtCustomerIdCr.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtCustomerIdCrFocusLost

    private void btnDepositDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblDepositTable.getValueAt(tblDepositTable.getSelectedRow(),0));
        observable.deleteTableData(s,tblDepositTable.getSelectedRow());
        observable.resetDepositInterestDetails();
        resetDepositInterestDetails();
        ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
        enableDisableButton(false);
        btnDepositNew.setEnabled(true);
        setSizeTableData();
        calcTotalIntAmount();
    }//GEN-LAST:event_btnDepositDeleteActionPerformed

    private void tblDepositTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositTableMousePressed
        // TODO add your handling code here:
        updateDepositInterestOBFields();
        updateMode = true;
        updateTab= tblDepositTable.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblDepositTable.getValueAt(tblDepositTable.getSelectedRow(),0));
        observable.populateDepositInterestDetails(st);
        populateDepositIntDetails();
        enableDisableButton(true);
        btnDepositNew.setEnabled(false);
        ClientUtil.enableDisable(panInsideDepositInterestDetails,true);
    }//GEN-LAST:event_tblDepositTableMousePressed
    public void populateDepositIntDetails() {
        tdtInterestDt.setDateValue(observable.getTdtInterestDt());
        txtInterestAmount.setText(observable.getTxtInterestAmount());
        cboInterestType.setSelectedItem(observable.getCboInterestType());
    }
    private void btnDepositSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositSaveActionPerformed
        // TODO add your handling code here:
        try{
            if(tdtInterestDt.getDateValue().length()<=0){
                ClientUtil.showAlertWindow("Interest Date Should not be Empty !!!...");
                return;
            }else if(CommonUtil.convertObjToDouble(txtInterestAmount.getText()).doubleValue()<=0){
                ClientUtil.showAlertWindow("Interest Amount Should not be Zero Or Empty !!!...");
                return;
            }else if(cboInterestType.getSelectedIndex()<=0){
                ClientUtil.showAlertWindow("Interest Type Should not be Empty !!!...");
                return;
            }else{
                updateDepositInterestOBFields();
                observable.addToTable(updateTab,updateMode);
                tblDepositTable.setModel(observable.getTblDepositTable());
                observable.resetDepositInterestDetails();
                resetDepositInterestDetails();
                ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
                enableDisableButton(false);
                btnDepositNew.setEnabled(true);
                setSizeTableData();
                calcTotalIntAmount();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDepositSaveActionPerformed
    public void updateDepositInterestOBFields() {
        observable.setTdtInterestDt(tdtInterestDt.getDateValue());
        observable.setTxtInterestAmount(txtInterestAmount.getText());
        observable.setCboInterestType(CommonUtil.convertObjToStr(cboInterestType.getSelectedItem()));
    }
    private void resetDepositInterestDetails(){
        tdtInterestDt.setDateValue("");
        txtInterestAmount.setText("");
        cboInterestType.setSelectedItem("");
    }
    private void setSizeTableData(){
        tblDepositTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblDepositTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblDepositTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblDepositTable.getColumnModel().getColumn(3).setPreferredWidth(100);
    }    
    
    public void calcTotalIntAmount() {
        double totalIntAmount = 0.0;
        if(tblDepositTable.getRowCount()>0){
            double creditAmount=0.0;
            double debitAmount=0.0;
            for(int i=0;i<tblDepositTable.getRowCount();i++){
                String intType="";
                intType = CommonUtil.convertObjToStr(tblDepositTable.getValueAt(i, 3));
                if(intType.equals("Credit")){
                    creditAmount+=CommonUtil.convertObjToDouble(tblDepositTable.getValueAt(i, 2));
                }else{
                    debitAmount+=CommonUtil.convertObjToDouble(tblDepositTable.getValueAt(i, 2));
                }
            }
            totalIntAmount = creditAmount-debitAmount;
        }
        lblTotalIntAmountVal.setText(CurrencyValidation.formatCrore(String.valueOf(totalIntAmount)));
    }
    private void btnDepositNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        enableDisableButton(false);
        btnDepositSave.setEnabled(true);
        ClientUtil.enableDisable(panInsideDepositInterestDetails,true);
        cboInterestType.setSelectedItem("Credit");
        lblStatus.setText("                      ");
    }//GEN-LAST:event_btnDepositNewActionPerformed
    private void enableDisableButton(boolean flag){
        btnDepositNew.setEnabled(flag);
        btnDepositSave.setEnabled(flag);
        btnDepositDelete.setEnabled(flag);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAccountDetails);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
            return;
        }else {
            if(cboProdType.getSelectedIndex() > 0 && txtCustomerIdCr.getText().length()>0){
                if(txtNoOfInstPay.getText().length()>0){
                    String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                    if(prodType.equals("MDS")){
                        updateOBFields();
                        updateMDSOBFields();
                    }
                    String status = "NEW";
                    saveAction(status);
                }else{
                    ClientUtil.showMessageWindow("Please Enter No of Installments to Pay !!!");
                }
            }else{
                ClientUtil.showMessageWindow("Please Select Chittal No !!!");
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if(txtCustomerIdCr.getText().length()>0){
            lblNetTransID.setVisible(false);
            panNetTransID.setVisible(false);
            btnDelete.setVisible(false);
            btnUpdate.setVisible(false);
            btnSave.setVisible(true);
            ClientUtil.enableDisable(panMDSDetails,true);
            txtNetAmt.setEnabled(false);
            txtNetTransID.setEnabled(false);
            tdtTransactionDt.setEnabled(false);
            txtInstallmentAmount.setEnabled(false);
            txtInstPayable.setEnabled(false);
            tdtTransactionDt.setDateValue(DateUtil.getStringDate(curr_dt));
            lblStatus.setText("             ");
        }else{
            ClientUtil.showMessageWindow("Please Select Chittal No !!!");
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if(txtNetTransID.getText().length()>0){
            String status = "DELETE"; 
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if(prodType.equals("MDS")){
                updateOBFields();
                updateMDSOBFields();
                saveAction(status);
            }
        }else{
            ClientUtil.showMessageWindow("Please Select Chittal Transaction No !!!");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtNoOfInstPayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfInstPayFocusLost
        // TODO add your handling code here:
        if(txtNoOfInstPay.getText().length()>0 && txtInstallmentAmount.getText().length()>0){
            double instAmtPayable = CommonUtil.convertObjToDouble(txtNoOfInstPay.getText()).doubleValue() *
            CommonUtil.convertObjToDouble(txtInstallmentAmount.getText()).doubleValue();
            txtInstPayable.setText(String.valueOf(instAmtPayable));
            calcNetAmt();
        }
    }//GEN-LAST:event_txtNoOfInstPayFocusLost

    private void btnNetTransIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetTransIDActionPerformed
        // TODO add your handling code here:
        if(txtCustomerIdCr.getText().length()>0){
            callView("NET_TRANS_ID");
        }
    }//GEN-LAST:event_btnNetTransIDActionPerformed

    private void txtDiscountAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountAmtFocusLost
        // TODO add your handling code here:
            calcNetAmt();
    }//GEN-LAST:event_txtDiscountAmtFocusLost

    private void txtBonusAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusAmtFocusLost
        // TODO add your handling code here:
            calcNetAmt();
    }//GEN-LAST:event_txtBonusAmtFocusLost

    private void txtInterestAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestAmtFocusLost
        // TODO add your handling code here:
            calcNetAmt();
    }//GEN-LAST:event_txtInterestAmtFocusLost
    private void calcNetAmt(){
        txtNetAmt.setText(String.valueOf((CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue()+
        CommonUtil.convertObjToDouble(txtInterestAmt.getText()).doubleValue())-
        (CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() +
        CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())));
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
        if(cboProdType.getSelectedIndex() > 0 && cboProdId.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if(prodType.equals("TD")){
                observable.getProductBehaveLike(null);
                if (observable.productBehavesLike.equals("DAILY")) {
                    panDepositDetails2.setVisible(false);
                    txtMaturityAmt.setText("");
                    txtTotalIntAmt.setText("");
                    txtTotalCredit.setText("");
                    txtTotalIntDrawn.setText("");
                    txtPeriodicIntAmt.setText("");
                    tdtDepNextIntAppDt.setDateValue("");
                    tdtDepLastInterestAppDt.setDateValue("");
                }else{
                    panDepositDetails2.setVisible(true);
                }
                if (observable.productBehavesLike.equals("RECURRING") || observable.productBehavesLike.equals("DAILY")) {
                    cboIntFrequency.setEnabled(false);
                    cboIntFrequency.setSelectedItem("Date of Maturity");
                }else{
                    cboIntFrequency.setEnabled(true);
                }
                
                if (observable.productBehavesLike.equals("FIXED")){
                    tabRemittanceProduct.add(panDepositInterestDetails,"Deposit Interest Details",1);
                }else{
                    tabRemittanceProduct.remove(panDepositInterestDetails);
                    observable.clearInterestMap();
                }
                tabRemittanceProduct.resetVisits();
            }
        }else{
            cboProdId.setEnabled(true);
            cboProdType.setEnabled(true);

        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAccountDetails);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
            return;
        }else {
            if (cboProdType.getSelectedIndex() > 0) {
                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                if(txtCustomerIdCr.getText().length()<=0){
                    if(prodType.equals("MDS")){
                        ClientUtil.showMessageWindow("Please Select Chittal Number !!!");
                    }else{
                        ClientUtil.showMessageWindow("Please Select Account Number !!!");
                    }
                    return;
                }
                updateOBFields();
                
                if(prodType.equals("TD")){
                    updateDepositOBFields();
                    String tableTotalIntAmount ="";
                    tableTotalIntAmount = lblTotalIntAmountVal.getText();
                    tableTotalIntAmount = tableTotalIntAmount.replaceAll(",","" );
                    if((CommonUtil.convertObjToDouble(txtTotalIntDrawn.getText()) > CommonUtil.convertObjToDouble(tableTotalIntAmount)) ||
                    (CommonUtil.convertObjToDouble(txtTotalIntDrawn.getText()) < CommonUtil.convertObjToDouble(tableTotalIntAmount))){
                        int yesNo = 0;
                        String[] voucherOptions = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null,"Total Interest Amount and Total Interest Drawn, Difference is there.... \n Do you want to Continue?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                        if (yesNo==1) {
                            return;
                        }
                    }
                }else if(prodType.equals("TL") || prodType.equals("AD")){
                    if(tdtLoanIntCalcDt.getDateValue().length()<=0){
                        ClientUtil.showMessageWindow("Please Enter Last Interest Calc Date !!!");
                        return;
                    }else if(lblLoanROI.getText().length()<=0){
                        ClientUtil.showMessageWindow("Please Enter ROI !!!");
                        return;
                    }else if(tdtSanctionDate.getDateValue().length()<=0){
                        ClientUtil.showMessageWindow("Please Enter Sanction Date !!!");
                        return;
                    }else if(CommonUtil.convertObjToInt(txtLimit.getText())<=0){
                        ClientUtil.showMessageWindow("Please Enter Limit Amount !!!");
                        return;
                    }else if(CommonUtil.convertObjToInt(txtNoInstallments.getText())<=0){
                        ClientUtil.showMessageWindow("Please Enter No. Of Installments !!!");
                        return;
                    }else if(cboRepayFreq.getSelectedIndex()<=0){
                        ClientUtil.showMessageWindow("Please Select Repayment Frequency !!!");
                        return;
                    }else if(tdtFDate.getDateValue().length()<=0){
                        ClientUtil.showMessageWindow("From Date Should not be Empty!!!");
                        return;
                    }else if(tdtTDate.getDateValue().length()<=0){
                        ClientUtil.showMessageWindow("To Date Should not be Empty!!!");
                        return;
                    }else if(lblBorrowNoVal.getText().length()<=0){
                        ClientUtil.showMessageWindow("Borrow Number Should not be Empty!!!");
                        return;
                    }  
                    if(prodType.equals("TL") && cboSanRepaymentType.getSelectedIndex()<=0){
                        ClientUtil.showMessageWindow("Please Select Repayment Type !!!");
                        return;
                    }
                    updateLoanOBFields();
                }else if(prodType.equals("MDS")){
                    updateMDSOBFields();
                    if(txtNetTransID.getText().length()<=0){
                        ClientUtil.showMessageWindow("Please Select Chittal Transaction No !!!");
                        return;
                    }
                }
                String status = "UPDATE";
                saveAction(status);
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed
    
    public void saveAction(String status){
        observable.doActionPerform(status);
        if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("STATUS")) {
            observable.resetForm();
            lblStatus.setText(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("STATUS")));
            clearUI();
        }
    }
    
    public void clearUI(){
        ClientUtil.enableDisable(panInsideMigratedDetails,false);
        String prodType=CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        String prodID=CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
        ClientUtil.clearAll(this);
        btnCustomerIdFileOpenCr.setEnabled(true);
        btnUpdate.setEnabled(true);
        lblMemberName.setText("");
        lblDepositMemberName.setText("");
        lblIntDepositNo.setText("");
        lblSBAccountNumberVal.setText("");
        lblDepositNomineeNameVal.setText("");
        lblTotalIntAmountVal.setText("");
        lblBorrowNoVal.setText("");
        enableDisableButton(false);
        cboProdType.setSelectedItem(prodType);
        cboProdId.setSelectedItem(prodID);
        lblSBAccountNumberVal.setVisible(false);
        lblSBAccountNumber.setVisible(false);
    }
    //Update Loan Fields
    public void updateLoanOBFields() {
        observable.setLblBorrowNo(lblBorrowNoVal.getText());
        observable.setTdtSanctionDate(DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
        observable.setTdtFDate(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
        observable.setTdtTDate(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
        observable.setTxtLimit(txtLimit.getText());
        observable.setTxtNoInstallments(txtNoInstallments.getText());
        observable.setCboSanRepaymentType(CommonUtil.convertObjToStr(((ComboBoxModel) cboSanRepaymentType.getModel()).getKeyForSelected().toString()));
        observable.setCboRepayFreq(CommonUtil.convertObjToStr(((ComboBoxModel) cboRepayFreq.getModel()).getKeyForSelected().toString()));
        observable.setTdtLoanIntCalcDt(DateUtil.getDateMMDDYYYY(tdtLoanIntCalcDt.getDateValue()));
        observable.setLoanROI(txtLoanROI.getText());        
    }
    //Update Deposit Fields
    public void updateDepositOBFields() {
        observable.setTdtDepositOpenDt(DateUtil.getDateMMDDYYYY(tdtDepositDate.getDateValue()));
        observable.setTdtMaturityDt(DateUtil.getDateMMDDYYYY(tdtMaturityDate.getDateValue()));
        observable.setTxtPeriodOfDeposit_Days(String.valueOf(CommonUtil.convertObjToInt(txtPeriodOfDeposit_Days.getText())));
        observable.setTxtPeriodOfDeposit_Months(String.valueOf(CommonUtil.convertObjToInt(txtPeriodOfDeposit_Months.getText())));
        observable.setTxtPeriodOfDeposit_Years(String.valueOf(CommonUtil.convertObjToInt(txtPeriodOfDeposit_Years.getText())));
        observable.setTdtDepLastInterestAppDt(DateUtil.getDateMMDDYYYY(tdtDepLastInterestAppDt.getDateValue()));
        observable.setTdtDepNextIntAppDt(DateUtil.getDateMMDDYYYY(tdtDepNextIntAppDt.getDateValue()));
        observable.setTxtMaturityAmt(txtMaturityAmt.getText());
        observable.setTxtRateOfInterest(txtRateOfInterest.getText());
        observable.setTxtTotalIntAmt(txtTotalIntAmt.getText());
        observable.setTxtPeriodicIntAmt(txtPeriodicIntAmt.getText());
        observable.setCboIntFrequency(CommonUtil.convertObjToStr(((ComboBoxModel) cboIntFrequency.getModel()).getKeyForSelected().toString()));
        observable.setTxtTotalIntDrawn(txtTotalIntDrawn.getText());
        observable.setTxtTotalCredit(txtTotalCredit.getText());
        if (rdoFreeze_Yes.isSelected()) {
            observable.setFreeze("Y");
        } else {
            observable.setFreeze("N");
        }
    }
    //Update MDS Fields
    public void updateMDSOBFields() {
        observable.setTxtNetTransID(txtNetTransID.getText());
        observable.setTdtTransactionDt(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()));
        double instAmtPayable = CommonUtil.convertObjToDouble(txtInstallmentAmount.getText()).doubleValue()-
        (CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue());
        observable.setTxtInstallmentAmount(txtInstallmentAmount.getText());
        observable.setTxtNoOfInstPay(txtNoOfInstPay.getText());
        observable.setTxtInstPayable(String.valueOf(instAmtPayable));
        observable.setTxtInterestAmt(txtInterestAmt.getText());
        observable.setTxtBonusAmt(txtBonusAmt.getText());
        observable.setTxtDiscountAmt(txtDiscountAmt.getText());
        observable.setTxtNetAmt(txtNetAmt.getText());
        observable.setMemberName(lblMemberName.getText());
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
        ClientUtil.enableDisable(panInsideMigratedDetails,false);
        ClientUtil.clearAll(this);
        btnCustomerIdFileOpenCr.setEnabled(true);
        btnUpdate.setEnabled(true);
        btnSave.setVisible(false);
        btnNew.setVisible(false);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(false);
        lblMemberName.setText("");
        lblTotalIntAmountVal.setText("");
        lblStatus.setText("             ");
        lblDepositMemberName.setText("");
        lblIntDepositNo.setText("");
        lblSBAccountNumberVal.setText("");
        lblBorrowNoVal.setText("");
        //lblDepositPeriodVal.setText("");
        lblDepositNomineeNameVal.setText("");
        lblSBAccountNumberVal.setVisible(false);
        lblSBAccountNumber.setVisible(false);
        enableDisableButton(false);
        setModified(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnReprintCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnReprintCloseActionPerformed

    private void btnCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenCrActionPerformed
        // TODO add your handling code here:
        callView("ACCOUNT_NO");
        if(cboProdType.getSelectedIndex() > 0 && cboProdId.getSelectedIndex() > 0 && txtCustomerIdCr.getText().length()>0) {
            cboProdId.setEnabled(false);
            cboProdType.setEnabled(false);
        }else{
            cboProdId.setEnabled(true);
            cboProdType.setEnabled(true);
        }
    }//GEN-LAST:event_btnCustomerIdFileOpenCrActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmProdId(prodType);
            btnUpdate.setVisible(true);
            if(prodType.equals("MDS")){
                lblCustomerIdCr.setText("Chittal No");
                lblNetTransID.setVisible(true);
                panNetTransID.setVisible(true);
                btnNew.setVisible(true);
                btnDelete.setVisible(true);
            }else{
                lblCustomerIdCr.setText("Account No");
                lblNetTransID.setVisible(false);
                panNetTransID.setVisible(false);
                btnNew.setVisible(false);
                btnDelete.setVisible(false);
            }
            btnSave.setVisible(false);
            if(prodType.equals("TD")){
                panDepositDetails.setVisible(true);
                tabRemittanceProduct.add(panDepositInterestDetails,"Deposit Interest Details",1);
                panLoanDetails.setVisible(false);
                panMDSDetails.setVisible(false);
                ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
                ClientUtil.enableDisable(panDepositDetails,true);
                txtMaturityAmt.setEnabled(false);
                txtTotalIntAmt.setEnabled(false);
                txtPeriodicIntAmt.setEnabled(false);
                tdtMaturityDate.setEnabled(false);
                txtDepositAmount.setEnabled(true);
            }else if(prodType.equals("TL") || prodType.equals("AD")){
                tabRemittanceProduct.remove(panDepositInterestDetails);
                panDepositDetails.setVisible(false);
                panMDSDetails.setVisible(false);
                panLoanDetails.setVisible(true);
                ClientUtil.enableDisable(panLoanDetails, true);
                tdtFDate.setEnabled(false);
                tdtTDate.setEnabled(false);
                if(prodType.equals("AD")){
                    cboSanRepaymentType.setEnabled(false);
                    cboSanRepaymentType.setSelectedItem("");
                }else{
                    cboSanRepaymentType.setEnabled(true);
                }
            }else if(prodType.equals("MDS")){
                tabRemittanceProduct.remove(panDepositInterestDetails);
                panDepositDetails.setVisible(false);
                panLoanDetails.setVisible(false);
                panMDSDetails.setVisible(true );
                ClientUtil.enableDisable(panMDSDetails,true);
                txtNetAmt.setEnabled(false);
                txtNetTransID.setEnabled(false);
                tdtTransactionDt.setEnabled(false);
                txtInstallmentAmount.setEnabled(false);
                txtInstPayable.setEnabled(false);
            }
            tabRemittanceProduct.resetVisits();
            cboProdId.setModel(observable.getCbmProdId());
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed
                           
                            
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed

    private void txtPeriodOfDeposit_YearsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPeriodOfDeposit_YearsFocusLost
        calculateMatDate();
    }//GEN-LAST:event_txtPeriodOfDeposit_YearsFocusLost
    
    private void txtPeriodOfDeposit_MonthsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPeriodOfDeposit_MonthsFocusLost
        calculateMatDate();
    }//GEN-LAST:event_txtPeriodOfDeposit_MonthsFocusLost
    private void calculateMatDate() {
        if (tdtDepositDate.getDateValue().length() > 0) {
            java.util.Date depDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtDepositDate.getDateValue());
            System.out.println("####calculateMatDate : " + depDate);
            if (depDate != null) {
                GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
                if ((txtPeriodOfDeposit_Years.getText() != null) && (!txtPeriodOfDeposit_Years.getText().equals(""))) {
                    cal.add(GregorianCalendar.YEAR, CommonUtil.convertObjToInt(txtPeriodOfDeposit_Years.getText()));
                } else {
                    cal.add(GregorianCalendar.YEAR, 0);
                }
                if ((txtPeriodOfDeposit_Months.getText() != null) && (!txtPeriodOfDeposit_Months.getText().equals(""))) {
                    cal.add(GregorianCalendar.MONTH, CommonUtil.convertObjToInt(txtPeriodOfDeposit_Months.getText()));
                } else {
                    cal.add(GregorianCalendar.MONTH, 0);
                }
                if ((txtPeriodOfDeposit_Days.getText() != null) && (!txtPeriodOfDeposit_Days.getText().equals(""))) {
                    cal.add(GregorianCalendar.DAY_OF_MONTH, CommonUtil.convertObjToInt(txtPeriodOfDeposit_Days.getText()));
                } else {
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
                }
                observable.setTdtMaturityDt(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime())));
                tdtMaturityDate.setDateValue(DateUtil.getStringDate(observable.getTdtMaturityDt()));
            }
            if (txtPeriodOfDeposit_Years.getText().length() == 0) {
                txtPeriodOfDeposit_Years.setText("0");
            }
            if (txtPeriodOfDeposit_Months.getText().length() == 0) {
                txtPeriodOfDeposit_Months.setText("0");
            }
            if (txtPeriodOfDeposit_Days.getText().length() == 0) {
                txtPeriodOfDeposit_Days.setText("0");
            }
            txtDepositAmtFocusLost();
        }
    }
    private void txtPeriodOfDeposit_DaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPeriodOfDeposit_DaysFocusLost
        calculateMatDate();
    }//GEN-LAST:event_txtPeriodOfDeposit_DaysFocusLost

    private void txtDepositAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositAmountFocusLost
        // TODO add your handling code here:
        if (txtDepositAmount.getText().length() > 0) {
                txtDepositAmtFocusLost();
        }
    }//GEN-LAST:event_txtDepositAmountFocusLost

    private void tdtDepositDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDepositDateFocusLost
        // TODO add your handling code here:
        calculateMatDate();
    }//GEN-LAST:event_tdtDepositDateFocusLost

    private void cboIntFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntFrequencyActionPerformed
        // TODO add your handling code here:
        calculateMatDate();
    }//GEN-LAST:event_cboIntFrequencyActionPerformed

    private void txtRateOfInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateOfInterestFocusLost
        // TODO add your handling code here:
        txtDepositAmtFocusLost();
    }//GEN-LAST:event_txtRateOfInterestFocusLost

    private void txtTotalIntDrawnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalIntDrawnFocusLost
        // TODO add your handling code here:
        if(txtTotalIntDrawn.getText().length()>0){
            if(CommonUtil.convertObjToDouble(txtTotalCredit.getText())<CommonUtil.convertObjToDouble(txtTotalIntDrawn.getText())){
                ClientUtil.showMessageWindow("Total Interest Drawn Should not be Greater than the Total Credit !!! ");
                txtTotalIntDrawn.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtTotalIntDrawnFocusLost

    private void txtLimitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimitFocusLost
        
    }//GEN-LAST:event_txtLimitFocusLost

    private void cboSanRepaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSanRepaymentTypeActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cboSanRepaymentTypeActionPerformed

    private void cboRepayFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayFreqActionPerformed
        // Add your handling code here:
        if (cboRepayFreq.getSelectedItem().equals("User Defined")) {
            tdtFDate.setEnabled(true);
            tdtTDate.setEnabled(true);
        }else{
            tdtFDate.setEnabled(false);
            tdtTDate.setEnabled(false);
        }
        calculateToDate();
    }//GEN-LAST:event_cboRepayFreqActionPerformed
    private void calculateToDate() {
        if (tdtFDate.getDateValue().length() > 0) {
            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
            java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar();
            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
            gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
            gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
            int dateVal = observable.getIncrementType();
            int incVal = observable.getInstallNo(txtNoInstallments.getText(), dateVal);
            Date date = new java.util.Date();
            date = DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue());
            if (txtNoInstallments.getText().equals("1")) {
                date = DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue());
            }
            if (dateVal <= 7) {
                gCalendar.add(gCalendar.DATE, incVal);
            } else if (dateVal >= 30) {
                gCalendar.add(gCalendar.MONTH, incVal);
                int firstInstall = dateVal / 30;
                gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
            }
            tdtTDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
            observable.setTdtTDate(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
        }
    }
    private void txtNoInstallmentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoInstallmentsFocusLost
        // TODO add your handling code here:
        calculateToDate();
    }//GEN-LAST:event_txtNoInstallmentsFocusLost

    private void tdtFDateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtFDateMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFDateMouseExited

    private void tdtFDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFDateFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtFDateFocusLost

    private void tdtTDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTDateFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtTDateFocusLost

    private void tdtSanctionDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSanctionDateFocusLost
        // TODO add your handling code here:
        if(tdtSanctionDate.getDateValue().length()>0){
            tdtFDate.setDateValue(tdtSanctionDate.getDateValue());
            calculateToDate();
        }
    }//GEN-LAST:event_tdtSanctionDateFocusLost
    
    
    private void txtDepositAmtFocusLost() {
        if ((txtDepositAmount.getText() != null) && (!txtDepositAmount.getText().equals(""))) {
            HashMap detailsHash = new HashMap();
            updateDepositOBFields();
            observable.getProductBehaveLike(null);
            if (txtPeriodOfDeposit_Years.getText().length() == 0) {
                observable.setTxtPeriodOfDeposit_Years("0");
                txtPeriodOfDeposit_Years.setText(observable.getTxtPeriodOfDeposit_Years());
            }
            if (txtPeriodOfDeposit_Months.getText().length() == 0) {
                observable.setTxtPeriodOfDeposit_Months("0");
                txtPeriodOfDeposit_Months.setText(observable.getTxtPeriodOfDeposit_Months());
            }
            if (txtPeriodOfDeposit_Days.getText().length() == 0) {
                observable.setTxtPeriodOfDeposit_Days("0");
                txtPeriodOfDeposit_Days.setText(observable.getTxtPeriodOfDeposit_Days());
            }
            if (observable.productBehavesLike.equals("FIXED")) {
                String prodType = ((ComboBoxModel) cboIntFrequency.getModel()).getKeyForSelected().toString();
                int freq = CommonUtil.convertObjToInt(prodType);
                if (freq == 30 && cboIntFrequency.getSelectedItem().equals("Monthly")) {
                    detailsHash.put("INTEREST_TYPE", "MONTHLY");
                } else if (freq == 90) {
                    detailsHash.put("INTEREST_TYPE", "QUATERLY");
                } else if (freq == 180) {
                    detailsHash.put("INTEREST_TYPE", "HALF YEARLY");
                } else if (freq == 0) {
                    detailsHash.put("INTEREST_TYPE", "DATE OF MATURITY");
                } else if (freq == 360) {
                    detailsHash.put("INTEREST_TYPE", "YEARLY");
                }
                int days = CommonUtil.convertObjToInt(txtPeriodOfDeposit_Days.getText());
                if (days > 0) {
                    cboInterestPaymentFreqActionPerformed();
                }
            }
            if (!observable.productBehavesLike.equals("DAILY")) {
                double maturityAmt = 0.0;
                double depositAmt = 0;
                double interestAmt = 0;
                detailsHash.put("AMOUNT", txtDepositAmount.getText());
                detailsHash.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(tdtDepositDate.getDateValue()));
                detailsHash.put("PERIOD_DAYS", txtPeriodOfDeposit_Days.getText());
                detailsHash.put("PERIOD_MONTHS", txtPeriodOfDeposit_Months.getText());
                detailsHash.put("PERIOD_YEARS", txtPeriodOfDeposit_Years.getText());
                detailsHash.put("MATURITY_DT", DateUtil.getDateMMDDYYYY(tdtMaturityDate.getDateValue()));
                detailsHash.put("ROI", txtRateOfInterest.getText());
                detailsHash.put("DISCOUNTED_RATE", "0");
                detailsHash.put("BEHAVES_LIKE", observable.productBehavesLike);
                detailsHash = observable.setAmountsAccROI(detailsHash, null);

                if (observable.productBehavesLike.equals("FIXED")) {
                    observable.setTxtMaturityAmt(txtDepositAmount.getText());
                    interestAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                } else if (observable.productBehavesLike.equals("CUMMULATIVE")) {
                    depositAmt = CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue();
                    maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                    maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
                    interestAmt = maturityAmt - depositAmt;
                    observable.setTxtMaturityAmt(String.valueOf(maturityAmt));
                    
//                        if (depositAmt > interestAmt) {
//                            ClientUtil.showAlertWindow("Interest amount should be greater or equal to Deposit amount" + "\n" + "         " + "Please change the Period in product level.");
//                        } else {
//                            interestAmt = depositAmt;
//                            maturityAmt = interestAmt + depositAmt;
//                            maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
//                            observable.setTxtMaturityAmt(String.valueOf(maturityAmt));
//                        }
                    
                } else if (observable.productBehavesLike.equals("FLOATING_RATE")) {
                    maturityAmt = CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue();
                    interestAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                    observable.setTxtMaturityAmt(String.valueOf(maturityAmt));
                } else {
                        maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                        maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
                        if (detailsHash.containsKey("INTEREST") && CommonUtil.convertObjToStr(detailsHash.get("INTEREST")).length() > 0) {
                            interestAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                        }
                        observable.setTxtMaturityAmt(String.valueOf(maturityAmt));
                }
                txtMaturityAmt.setText(observable.getTxtMaturityAmt());
                observable.setTxtTotalIntAmt(String.valueOf(interestAmt));
                txtTotalIntAmt.setText(observable.getTxtTotalIntAmt());
            }
        }
        cboInterestPaymentFreqActionPerformed();
    }
    
    private void cboInterestPaymentFreqActionPerformed() {
        if ((!cboIntFrequency.getSelectedItem().equals("")) && (cboIntFrequency.getSelectedItem() != null) && cboIntFrequency.getSelectedIndex() > 0) {
            if ((txtTotalIntAmt.getText() != null) && (!txtTotalIntAmt.getText().equals(""))) {
                double perIntAmt = 0;
                setYr();
                double totalIntAmtPerYear = (CommonUtil.convertObjToDouble(txtTotalIntAmt.getText()) / yr);
                if (cboIntFrequency.getSelectedItem().equals("Half Yearly")) {
                    perIntAmt = totalIntAmtPerYear / perHalfYear;
                } else if (cboIntFrequency.getSelectedItem().equals("Monthly")) {
                    perIntAmt = totalIntAmtPerYear / perMonth;
                    double depositAmt = CommonUtil.convertObjToDouble(txtDepositAmount.getText());
                    //--- Calculation for Period as No.Of Days
                    int YrsToDay = 0;
                    int MonToDay = 0;
                    int daysEntered = 0;
                    int periodInDays = 0;
                    if ((txtPeriodOfDeposit_Years.getText() != null) && (!txtPeriodOfDeposit_Years.getText().equals(""))) {
                        YrsToDay = (CommonUtil.convertObjToInt(txtPeriodOfDeposit_Years.getText())) * 365;
                    }
                    if ((txtPeriodOfDeposit_Months.getText() != null) && (!txtPeriodOfDeposit_Months.getText().equals(""))) {
                        MonToDay = ((CommonUtil.convertObjToInt(txtPeriodOfDeposit_Months.getText())) * 30);
                    }
                    if ((txtPeriodOfDeposit_Days.getText() != null) && (!txtPeriodOfDeposit_Days.getText().equals(""))) {
                        daysEntered = CommonUtil.convertObjToInt(txtPeriodOfDeposit_Days.getText());
                    }
                    periodInDays = (YrsToDay + MonToDay + daysEntered);
                    //--- End 0f Calculation Period as No.Of Days
                } else if (cboIntFrequency.getSelectedItem().equals("Yearly")) {
                    perIntAmt = totalIntAmtPerYear;
                } else if (cboIntFrequency.getSelectedItem().equals("Quaterly")) {
                    perIntAmt = totalIntAmtPerYear / perQuarterYear;
                } else if (cboIntFrequency.getSelectedItem().equals("Date of Maturity")) {
                    perIntAmt = 0;
                }
                try {
                    perIntAmt = (double) getNearest((long) (perIntAmt * 100), 100) / 100;
                } catch (Exception e) {
                    System.out.println(e);
                }
                observable.setTxtPeriodicIntAmt(String.valueOf(perIntAmt));
                txtPeriodicIntAmt.setText(observable.getTxtPeriodicIntAmt());
            }
        } else if ((cboIntFrequency.getSelectedItem().equals("")) || (cboIntFrequency.getSelectedItem() == null)) {
            observable.setTxtPeriodicIntAmt("");
            txtPeriodicIntAmt.setText(observable.getTxtPeriodicIntAmt());
        }
    }
    
    private void setYr() {
        if ((txtPeriodOfDeposit_Years.getText() != null) && (!txtPeriodOfDeposit_Years.getText().equals(""))) {
            yr = CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Years.getText());
        }

        if ((txtPeriodOfDeposit_Months.getText() != null) && (!txtPeriodOfDeposit_Months.getText().equals(""))) {
            yr = yr + (CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Months.getText()) / totalMonths);

        }

        if ((txtPeriodOfDeposit_Days.getText() != null) && (!txtPeriodOfDeposit_Days.getText().equals(""))) {
            yr = yr + (CommonUtil.convertObjToDouble(txtPeriodOfDeposit_Days.getText()) / totalDays);
        }
    }
    
    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }
    
    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }
    /** This method helps in popoualting the data from the data base
     * @param currField Action the argument is passed according to the command issued
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals("ACCOUNT_NO")){
            if (cboProdType.getSelectedIndex() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
                whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                viewMap.put(CommonConstants.MAP_NAME, "getMigratedAccountList" + prodType);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            }
        }else if(currField.equals("NET_TRANS_ID")){
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            whereMap.put("CHITTAL_NO", txtCustomerIdCr.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getMDSChittalsTransDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        int remitProduBranchRow = 0;
        if (viewType != null) {
            if(viewType.equals("ACCOUNT_NO")){
                txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                observable.setAct_Num(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                lblMemberName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                if(prodType.equals("TL") || prodType.equals("AD")){
                    tdtLoanIntCalcDt.setDateValue(CommonUtil.convertObjToStr(hash.get("LAST_INT_CALC_DT")));
                    txtLoanROI.setText(CommonUtil.convertObjToStr(hash.get("INTEREST")));
                    lblBorrowNoVal.setText("");
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM",hash.get("ACT_NUM"));
                    List accountLst = ClientUtil.executeQuery("getLoanAccountDetails", whereMap);
                    if(accountLst!=null && accountLst.size()>0){
                        whereMap = (HashMap)accountLst.get(0);
                        lblBorrowNoVal.setText(CommonUtil.convertObjToStr(whereMap.get("BORROW_NO")));
                        tdtSanctionDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("FROM_DT")));
                        tdtFDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("FROM_DT")));
                        tdtTDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("TO_DT")));
                        txtLimit.setText(CommonUtil.convertObjToStr(whereMap.get("LIMIT")));
                        txtNoInstallments.setText(CommonUtil.convertObjToStr(whereMap.get("NO_INSTALL")));
                        if(whereMap.get("REPAYMENT_FREQUENCY")!=null && CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_FREQUENCY")).equals("0")){
                            observable.setCboRepayFreq("User Defined");
                        }else if(whereMap.get("REPAYMENT_FREQUENCY")!=null && CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_FREQUENCY")).equals("30")){
                            observable.setCboRepayFreq("Monthly");
                        }else if(whereMap.get("REPAYMENT_FREQUENCY")!=null && CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_FREQUENCY")).equals("90")){
                            observable.setCboRepayFreq("Quaterly");
                        }else if(whereMap.get("REPAYMENT_FREQUENCY")!=null && CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_FREQUENCY")).equals("180")){
                            observable.setCboRepayFreq("Half Yearly");
                        } else if (whereMap.get("REPAYMENT_FREQUENCY") != null && (CommonUtil.convertObjToInt(whereMap.get("REPAYMENT_FREQUENCY"))>=360)) {
                            observable.setCboRepayFreq("Yearly");
                        }
                        cboRepayFreq.setSelectedItem(observable.getCboRepayFreq());
                        whereMap.put("ACT_NUM", hash.get("ACT_NUM"));
                        List accountLstRepayType = ClientUtil.executeQuery("getLoanAccountRepayType", whereMap);
                        if (accountLstRepayType != null && accountLstRepayType.size() > 0) {
                            whereMap = (HashMap) accountLstRepayType.get(0);
                            cboSanRepaymentType.setSelectedItem(observable.getCbmSanRepaymentType().getDataForKey(whereMap.get("INSTALL_TYPE")));
                        }
                    }
                }else if(prodType.equals("TD")){
                    observable.resetForm();
                    observable.resetTableValues();
                    tblDepositTable.setModel(observable.getTblDepositTable());
                    clearUI();
                    rdoFreeze_No.setSelected(true);
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    observable.setAct_Num(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    lblMemberName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
                    HashMap whereMap = new HashMap();
                    whereMap.put("DEPOSIT NO",hash.get("ACT_NUM"));
                    lblDepositMemberName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
                    lblIntDepositNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    List accountLst = ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", whereMap);
                    ClientUtil.enableDisable(panInsideDepositInterestDetails,false);
                    ClientUtil.enableDisable(panDepositAddTableDetails,true);
                    btnAddTable.setEnabled(true);
                    enableDisableButton(false);
                    btnDepositNew.setEnabled(true);
                    if(accountLst!=null && accountLst.size()>0){
                        whereMap = (HashMap)accountLst.get(0);
                        tdtDepositDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT")));
                        tdtMaturityDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("MATURITY_DT")));
                        txtPeriodOfDeposit_Years.setText(String.valueOf(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_YY"))));
                        txtPeriodOfDeposit_Months.setText(String.valueOf(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_MM"))));
                        txtPeriodOfDeposit_Days.setText(String.valueOf(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_DD"))));
                        tdtDepLastInterestAppDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("LAST_INT_APPL_DT")));
                        tdtDepNextIntAppDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("NEXT_INT_APPL_DT")));
                        if(tdtDepNextIntAppDt.getDateValue().length()<=0){
                            tdtDepNextIntAppDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("LAST_INT_APPL_DT")));
                        }
                        txtDepositAmount.setText(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_AMT")));
                        txtMaturityAmt.setText(CommonUtil.convertObjToStr(whereMap.get("MATURITY_AMT")));
                        txtRateOfInterest.setText(CommonUtil.convertObjToStr(whereMap.get("RATE_OF_INT")));
                        txtTotalIntAmt.setText(CommonUtil.convertObjToStr(whereMap.get("TOT_INT_AMT")));
                        txtPeriodicIntAmt.setText(CommonUtil.convertObjToStr(whereMap.get("PERIODIC_INT_AMT")));
                        txtTotalIntDrawn.setText(CommonUtil.convertObjToStr(whereMap.get("TOTAL_INT_DRAWN")));
                        txtTotalCredit.setText(CommonUtil.convertObjToStr(whereMap.get("TOTAL_INT_CREDIT")));
                        if(whereMap.get("INTPAY_FREQ")!=null && CommonUtil.convertObjToStr(whereMap.get("INTPAY_FREQ")).equals("0")){
                            observable.setCboIntFrequency("Date of Maturity");
                        }else if(whereMap.get("INTPAY_FREQ")!=null && CommonUtil.convertObjToStr(whereMap.get("INTPAY_FREQ")).equals("30")){
                            observable.setCboIntFrequency("Monthly");
                        }else if(whereMap.get("INTPAY_FREQ")!=null && CommonUtil.convertObjToStr(whereMap.get("INTPAY_FREQ")).equals("90")){
                            observable.setCboIntFrequency("Quaterly");
                        }else if(whereMap.get("INTPAY_FREQ")!=null && CommonUtil.convertObjToStr(whereMap.get("INTPAY_FREQ")).equals("180")){
                            observable.setCboIntFrequency("Half Yearly");
                        } else if (whereMap.get("INTPAY_FREQ") != null && CommonUtil.convertObjToStr(whereMap.get("INTPAY_FREQ")).equals("360")) {
                            observable.setCboIntFrequency("Yearly");
                        }
                        cboIntFrequency.setSelectedItem(observable.getCboIntFrequency());
//                        String period="";
//                        period = CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_DD")))+" Days ";
//                        period = period + CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_MM")))+" Months ";
//                        period = period + CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(whereMap.get("DEPOSIT_PERIOD_YY")))+" Years ";
                        //lblDepositPeriodVal.setText(period);
                    } else {
                        //lblDepositPeriodVal.setText("");
                    }
                    observable.getData(hash);
                    setSizeTableData();
                    calcTotalIntAmount();
                    displaySBAccountNumber(hash);
                    displayNomineeName(hash);
                }else if(prodType.equals("MDS")){
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                    txtInstallmentAmount.setText(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                }
            }else if(viewType.equals("NET_TRANS_ID")){
                txtNetTransID.setText(CommonUtil.convertObjToStr(hash.get("NET_TRANS_ID")));
                HashMap whereMap = new HashMap();
                Date transDate = null;
                transDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("TRANS_DT")));
                transDate = setProperDtFormat(transDate);
                btnNew.setVisible(false);
                whereMap.put("CHITTAL_NO", txtCustomerIdCr.getText());
                whereMap.put("PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
                whereMap.put("TRANS_DT", transDate);
                whereMap.put("NET_TRANS_ID", txtNetTransID.getText());
                List mdsLst = ClientUtil.executeQuery("getMDSTransIDDetails", whereMap);
                if(mdsLst!=null && mdsLst.size()>0){
                    whereMap = (HashMap)mdsLst.get(0);
                    tdtTransactionDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("TRANS_DT")));
                    txtInstallmentAmount.setText(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                    txtNoOfInstPay.setText(CommonUtil.convertObjToStr(whereMap.get("NO_OF_INST")));
                    double instAmtPayable = CommonUtil.convertObjToDouble(whereMap.get("NO_OF_INST")).doubleValue() *
                    CommonUtil.convertObjToDouble(txtInstallmentAmount.getText()).doubleValue();
                    txtInstPayable.setText(String.valueOf(instAmtPayable));
                    txtInterestAmt.setText(CommonUtil.convertObjToStr(whereMap.get("PENAL_AMT")));
                    txtBonusAmt.setText(CommonUtil.convertObjToStr(whereMap.get("BONUS_AMT")));
                    txtDiscountAmt.setText(CommonUtil.convertObjToStr(whereMap.get("DISCOUNT_AMT")));
                    txtNetAmt.setText(CommonUtil.convertObjToStr(whereMap.get("NET_AMT")));
                }
            }
        }
        setModified(true);
    }
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)curr_dt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    private void displaySBAccountNumber(HashMap hash){
        List AccountLst = ClientUtil.executeQuery("getSBAccountNumberFromDepositNo", hash);
        if(AccountLst!=null && AccountLst.size()>0){
            HashMap whereMap = (HashMap)AccountLst.get(0);
            lblSBAccountNumberVal.setText(CommonUtil.convertObjToStr(whereMap.get("INT_PAY_ACC_NO")));
            lblSBAccountNumberVal.setVisible(true);
            lblSBAccountNumber.setVisible(true);
        }else{
            lblSBAccountNumberVal.setText("  ");
            lblSBAccountNumberVal.setVisible(false);
            lblSBAccountNumber.setVisible(false);
        }
    }
    
    private void displayNomineeName(HashMap hash){
        List nomineeLst = ClientUtil.executeQuery("getDepositNomineeDetails", hash);
        if(nomineeLst!=null && nomineeLst.size()>0){
            HashMap whereMap = (HashMap)nomineeLst.get(0);
            lblDepositNomineeNameVal.setText(CommonUtil.convertObjToStr(whereMap.get("NOMINEE_NAME")));
            lblDepositNomineeNameVal.setVisible(true);
            lblDepositNomineeName.setVisible(true);
        }else{
            lblDepositNomineeNameVal.setText("  ");
            lblDepositNomineeNameVal.setVisible(false);
            lblDepositNomineeName.setVisible(false);
        }
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddTable;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNew;
    private com.see.truetransact.uicomponent.CButton btnDepositSave;
    private com.see.truetransact.uicomponent.CButton btnNetTransID;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnReprintClose;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUpdate;
    private com.see.truetransact.uicomponent.CComboBox cboIntFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboInterestType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboRepayFreq;
    private com.see.truetransact.uicomponent.CComboBox cboSanRepaymentType;
    private com.see.truetransact.uicomponent.CLabel lbDepositlFreeze;
    private com.see.truetransact.uicomponent.CLabel lbStartDate;
    private com.see.truetransact.uicomponent.CLabel lblBonusAmt;
    private com.see.truetransact.uicomponent.CLabel lblBorrowNo;
    private com.see.truetransact.uicomponent.CLabel lblBorrowNoVal;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdCr;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdCrNo;
    private com.see.truetransact.uicomponent.CLabel lblDepLastInterestAppDt;
    private com.see.truetransact.uicomponent.CLabel lblDepNextIntAppDt;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmount;
    private com.see.truetransact.uicomponent.CLabel lblDepositDate;
    private com.see.truetransact.uicomponent.CLabel lblDepositMemberName;
    private com.see.truetransact.uicomponent.CLabel lblDepositNomineeName;
    private com.see.truetransact.uicomponent.CLabel lblDepositNomineeNameVal;
    private com.see.truetransact.uicomponent.CLabel lblDepositPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDiscountAmt;
    private com.see.truetransact.uicomponent.CLabel lblFDate;
    private com.see.truetransact.uicomponent.CLabel lblInstPayable;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblIntAmount;
    private com.see.truetransact.uicomponent.CLabel lblIntDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblIntFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInterestAmount;
    private com.see.truetransact.uicomponent.CLabel lblInterestAmt;
    private com.see.truetransact.uicomponent.CLabel lblInterestDt;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblLimit;
    private com.see.truetransact.uicomponent.CLabel lblLoanIntCalcDt;
    private com.see.truetransact.uicomponent.CLabel lblLoanROI;
    private com.see.truetransact.uicomponent.CLabel lblMaturityAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetAmt;
    private com.see.truetransact.uicomponent.CLabel lblNetTransID;
    private com.see.truetransact.uicomponent.CLabel lblNoInstallments;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstPay;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstallPaid;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Days;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Months;
    private com.see.truetransact.uicomponent.CLabel lblPeriod_Years;
    private com.see.truetransact.uicomponent.CLabel lblPeriodicIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblRepayFreq;
    private com.see.truetransact.uicomponent.CLabel lblSBAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblSBAccountNumberVal;
    private com.see.truetransact.uicomponent.CLabel lblSanRepaymentType;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDate;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalCredit;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntAmountVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalIntDrawn;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDt;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
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
    private com.see.truetransact.uicomponent.CPanel panAccountDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerNO;
    private com.see.truetransact.uicomponent.CPanel panDepositAddTableDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositBtn;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails1;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails2;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestDetails2;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestTable;
    private com.see.truetransact.uicomponent.CPanel panFreeze;
    private com.see.truetransact.uicomponent.CPanel panInsideDepositInterestDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideMigratedDetails;
    private com.see.truetransact.uicomponent.CPanel panLoanDetails;
    private com.see.truetransact.uicomponent.CPanel panMDSDetails;
    private com.see.truetransact.uicomponent.CPanel panMigratedDetails;
    private com.see.truetransact.uicomponent.CPanel panNetTransID;
    private com.see.truetransact.uicomponent.CPanel panPeriodOfDeposit;
    private com.see.truetransact.uicomponent.CPanel panReprintBtn;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgFreeze;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CRadioButton rdoFreeze_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFreeze_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpInterestTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblDepositTable;
    private com.see.truetransact.uicomponent.CDateField tdtDepLastInterestAppDt;
    private com.see.truetransact.uicomponent.CDateField tdtDepNextIntAppDt;
    private com.see.truetransact.uicomponent.CDateField tdtDepositDate;
    private com.see.truetransact.uicomponent.CDateField tdtFDate;
    private com.see.truetransact.uicomponent.CDateField tdtInterestDt;
    private com.see.truetransact.uicomponent.CDateField tdtLoanIntCalcDt;
    private com.see.truetransact.uicomponent.CDateField tdtMaturityDate;
    private com.see.truetransact.uicomponent.CDateField tdtSanctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtStartDate;
    private com.see.truetransact.uicomponent.CDateField tdtTDate;
    private com.see.truetransact.uicomponent.CDateField tdtTransactionDt;
    private com.see.truetransact.uicomponent.CTextField txtBonusAmt;
    private com.see.truetransact.uicomponent.CTextField txtCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtDepositAmount;
    private com.see.truetransact.uicomponent.CTextField txtDiscountAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstPayable;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtIntAmount;
    private com.see.truetransact.uicomponent.CTextField txtInterestAmount;
    private com.see.truetransact.uicomponent.CTextField txtInterestAmt;
    private com.see.truetransact.uicomponent.CTextField txtLimit;
    private com.see.truetransact.uicomponent.CTextField txtLoanROI;
    private com.see.truetransact.uicomponent.CTextField txtMaturityAmt;
    private com.see.truetransact.uicomponent.CTextField txtNetAmt;
    private com.see.truetransact.uicomponent.CTextField txtNetTransID;
    private com.see.truetransact.uicomponent.CTextField txtNoInstallments;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstPay;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstallPaid;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Days;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Months;
    private com.see.truetransact.uicomponent.CTextField txtPeriodOfDeposit_Years;
    private com.see.truetransact.uicomponent.CTextField txtPeriodicIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtRateOfInterest;
    private com.see.truetransact.uicomponent.CTextField txtTotalCredit;
    private com.see.truetransact.uicomponent.CTextField txtTotalIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalIntDrawn;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        EditMigratedDataUI gui = new EditMigratedDataUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}