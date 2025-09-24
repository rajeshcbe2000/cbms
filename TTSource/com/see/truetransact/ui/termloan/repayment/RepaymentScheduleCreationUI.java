/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * RepaymentScheduleCreationUI.java
 *
 * Created on March 31, 2016, 1:46 PM
  *created by  nithya
 */
package com.see.truetransact.ui.termloan.repayment;

import com.see.truetransact.ui.termloan.arbitration.*;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.ParseException;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.termloan.TermLoanUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentRB;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 * @author Nithya
 */
public class RepaymentScheduleCreationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {

    HashMap mandatoryMap;
    HashMap data;
    HashMap paramMap = null;
    CInternalFrame parentFrame = null;
//    TermLoanUI parent = null;
//    GoldLoanUI parentGoldLoanUI = null;
//    TransDetailsUI parantTransUI =null; 
    final TermLoanInstallmentRB resourceBundle = new TermLoanInstallmentRB();
    RepaymentScheduleCreationOB observable;
    private int selectedInstallmentRow = -1;
    private boolean canShow = false;
    private boolean isUserDefinedPanelVisible = false;
    private boolean updateInstallment = false;
    private Date fromDate = null;    
    private Date toDate = null;
    private Date repayDate = null;
    private Date disbursmentDate = null;
    private Date sanctionDate = null;
    private Date curr_dt = null;
    String[] strMandatory = {"INTEREST_TYPE", "NO_INSTALL", "DURATION_YY", "REPAYMENT_TYPE",
    "PRINCIPAL_AMOUNT", "COMPOUNDING_PERIOD", "REPAYMENT_FREQUENCY", "INTEREST"};
    String[] strUserDefMandatory = {"FROM_DATE", "TO_DATE"};
    private boolean updateSchedule = false;
    private Date date;
    private boolean moratoriumAvailable = false;
    DecimalFormat df = new DecimalFormat("0.##");
    /**
     * Creates new form AuthorizeUI
     */
    public RepaymentScheduleCreationUI() {       
       
        setupInit();
        setupScreen();    
    } 

    /**
     * Creates new form AuthorizeUI
     */
    public RepaymentScheduleCreationUI(CInternalFrame parent, HashMap paramMap) {
        this.parentFrame = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        initComponents();
        setObservable();
        panInstallmentFields.setVisible(false);
        panSalaryRecovery.setVisible(false);
        cboFrequency.setModel(observable.getCbmFrequency());
        cboRepaymentType.setModel(observable.getCbmRepaymentType());
        cboroundOfType.setModel(observable.getCbmroundOfType());
        curr_dt = ClientUtil.getCurrentDate();
        txtInterestRate.setEditable(false);
        txtInterestRate.setEnabled(false);
        lblMoratoriumPeriodInterst.setVisible(false);
        lblloanBalance.setVisible(false);
        txtLoanBalance.setVisible(false);
        txtMoratoriumInt.setVisible(false);
        
    }

//   public RepaymentScheduleCreationUI(TermLoanUI parent, HashMap data, boolean hideSubmit) {
//        this.parent = parent;
//        setupinit(data);
//        if (data !=null && (!data.containsKey("LTD")))
//            setupScreen();
//        if (data == null){
//            canShow = true;
//        }
//        if (hideSubmit == true){
//            btnSubmit.setVisible(false);
//        }
//        populateData();
//        if (observable.isHideSubmit() == true){
//            btnSubmit.setVisible(false);
//        }
//         txtLoanBalance.setVisible(false);
//        lblloanBalance.setVisible(false);
//        //if(data != null && data.containsKey("EDIT_SCHEDULE"))
//        //if(data.containsKey("EDIT_SCHEDULE"))
//            //btnOk.setVisible(false);
//        //else
//            //btnOk.setVisible(true);
//       }
//    
//    public RepaymentScheduleCreationUI(GoldLoanUI parent, HashMap data, boolean hideSubmit) {
//        this.parentGoldLoanUI = parent;
//        setupinit(data);
//		canShow = true;
//        data.put("LTD", "");
////        if (data !=null && (!data.containsKey("LTD")))
////            setupScreen();
//        if (data == null){
//            canShow = true;
//        }
//        if (hideSubmit == true){
//            btnSubmit.setVisible(false);
//        }
//        populateData();
//        if (observable.isHideSubmit() == true){
//            btnSubmit.setVisible(false);
//        }
//        txtLoanBalance.setVisible(false);
//        lblloanBalance.setVisible(false);
//    }
//    
//    
//    public RepaymentScheduleCreationUI(TermLoanUI parent, HashMap data) {
//        this(parent, data, false);
//        txtLoanBalance.setVisible(false);
//        lblloanBalance.setVisible(false);
//        
////        populateData();
//    }
//    
//    public RepaymentScheduleCreationUI(boolean goldLoanUI, GoldLoanUI parent, HashMap data) {
//        this(parent, data, false);
////        populateData();
//    }
//   
//     public RepaymentScheduleCreationUI(TransDetailsUI parent, HashMap data, boolean hideSubmit) {
//        this.parantTransUI = parent;
//        setupinit(data);
//        if (data !=null && (!data.containsKey("LTD")))
//            setupScreen();
//        if (data == null){
//            canShow = true;
//        }
//        if (hideSubmit == true){
//            btnSubmit.setVisible(false);
//        }
//        populateData();
//        if (observable.isHideSubmit() == true){
//            btnSubmit.setVisible(false);
//        }
//        lblMoratoriumPeriodInterst.setVisible(false);
//        txtMoratoriumInt.setVisible(false);
//        txtLoanBalance.setVisible(true);
//        lblloanBalance.setVisible(true);
//        
////        populateData();
//    }
     
    private void setupinit(HashMap data){
        this.data = data;
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaxLength();
        setObservable();
        initComponentData();
        observable.ttNotifyObservers();
    }
    
    private void populateData() {
//       cboroundOfType.setSelectedIndex(3);
//       btnOkActionPerformed();
       
        if (!canShow && (mandatoryKeyValueCheck(data) == true)){
            this.dispose();
        }else{
            observable.destroyObjects();
            observable.createObject();
            if (data != null){
                data.put("COMPOUNDING_TYPE", "REPAYMENT");
                if (data.containsKey("REPAYMENT_TYPE")){
                    if (data.get("REPAYMENT_TYPE").equals("USER_DEFINED")){
                        setPopulateFieldsEnableDisable(false);//Disable Populating fields
                        panInstallmentFields.setVisible(true);
                        setOnlyNewBtnEnable();
                        setUserDefinedFieldsEnableDisable(false);
                        isUserDefinedPanelVisible = true;
                         populateData(data); // Populate Data
                    }else{
                        setPopulateFieldsEnableDisable(false);//Disable Populating fields
                        panInstallmentFields.setVisible(false);
                        isUserDefinedPanelVisible = false;
                        populateData(data); // Populate Data
                    }
                }else{
                    setPopulateFieldsEnableDisable(true);//Enable Populating fields
                    panInstallmentFields.setVisible(false);
                    isUserDefinedPanelVisible = false;
                    populateData(data);// Populate Data
                }
                panSubmitBtn.setVisible(true);
                btnOk.setVisible(true);
                populateCommonFields();
            }else{
                setPopulateFieldsEnableDisable(true);//Enable Populating fields
                panSubmitBtn.setVisible(false);
                panInstallmentFields.setVisible(false);
                isUserDefinedPanelVisible = false;
            }
            observable.ttNotifyObservers();
            if (data !=null && (!data.containsKey("LTD"))) {
                //show();
                 btnSubmitActionPerformed();
            } else if (data !=null) {
                btnSubmitActionPerformed();
            }
        }
    }
    
    
    private boolean mandatoryKeyValueCheck(HashMap data){
        String strKeyWarn = resourceBundle.getString("keyWarning");
        StringBuffer stbKeyHelpMessage =new StringBuffer();
        if (data != null){
            stbKeyHelpMessage = checkFields(strMandatory);
     //       if (data.containsKey("REPAYMENT_TYPE") && data.get("REPAYMENT_TYPE").equals("USER_DEFINED")){
              //  stbKeyHelpMessage = stbKeyHelpMessage.append(checkFields(strUserDefMandatory));
         //   }
            if (stbKeyHelpMessage.length() > 0){
                displayAlert(strKeyWarn+stbKeyHelpMessage.toString());
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    private StringBuffer checkFields(String[] strKeys){
        StringBuffer stbKeyHelpMessage = new StringBuffer("");
        for (int i = strKeys.length - 1;i >= 0;--i){
            if (data.containsKey(strKeys[i])){
                if (data.get(strKeys[i]) == null || data.get(strKeys[i]).equals("")){
                    stbKeyHelpMessage.append("\n"+strKeys[i]);
                }
            }else{
                stbKeyHelpMessage.append("\n"+strKeys[i]);
            }
        }
        return stbKeyHelpMessage;
    }
    
    
    
    private void populateCommonFields(){
         double morInt=0.;
        observable.setTxtInterestRate(CommonUtil.convertObjToStr(data.get("INTEREST")));
        observable.setLblAcctNo_Disp(CommonUtil.convertObjToStr(data.get("ACT_NO")));
        observable.setTxtNoOfInstall(CommonUtil.convertObjToStr(data.get("NO_INSTALL")));
        observable.setTxtLoanAmt(CommonUtil.convertObjToStr(data.get("PRINCIPAL_AMOUNT")));
        observable.setCboFrequency(CommonUtil.convertObjToStr(observable.getCbmFrequency().getDataForKey(data.get("REPAYMENT_FREQUENCY"))));
        observable.setCboRepaymentType(CommonUtil.convertObjToStr(observable.getCbmRepaymentType().getDataForKey(data.get("REPAYMENT_TYPE"))));
        observable.setCboroundOfType(CommonUtil.convertObjToStr(observable.getCbmroundOfType().getDataForKey(data.get("ROUNDOFF_TYPE"))));
        //lblAcctNo_Disp.setText(observable.getLblAcctNo_Disp());
        txtInterestRate.setText(observable.getTxtInterestRate());
        txtLoanAmt.setText(observable.getTxtLoanAmt());
        cboFrequency.setSelectedItem(observable.getCbmFrequency());
        cboroundOfType.setSelectedItem(observable.getCbmroundOfType());
        cboRepaymentType.setSelectedItem(observable.getCbmRepaymentType());
        txtNoOfInstall.setText(observable.getTxtNoOfInstall());
        morInt=(Math.round(CommonUtil.convertObjToDouble(observable.getTxtLoanAmt()))*(CommonUtil.convertObjToDouble(observable.getTxtInterestRate())))/1200;
        txtMoratoriumInt.setText(CommonUtil.convertObjToStr(morInt));
        txtLoanBalance.setText(CommonUtil.convertObjToStr(data.get("BALANCE_AMT")));
    }
    
    private void setObservable(){
        // observable = LoanRepaymentScheduleOB.getInstance();
        observable = RepaymentScheduleCreationOB.getInstance();
        observable.addObserver(this);
        observable.resetInstallmentDetails();
        System.out.println("itssetobserv");
//        cboroundOfType.addItem("Round off Rupee");
//        cboroundOfType.addItem("Round off 50paise");
//        cboroundOfType.addItem("Round off nearest 5 paise");
//        cboroundOfType.addItem("Round of nearest paise");
        
    }
    
    private void populateData(HashMap data){
        observable.populateData(data, tblInstallmentTable);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnDelete.setName("btnDelete");
        btnNew.setName("btnNew");
        btnSave.setName("btnSave");
        btnOk.setName("btnOk");
        lblNoOfInstall.setName("lblNoOfInstall");
        txtNoOfInstall.setName("txtNoOfInstall");
        lblAcctNo.setName("lblAcctNo");
        //lblAcctNo_Disp.setName("lblAcctNo_Disp");
        lblBalance.setName("lblBalance");
        lblInstallmentDate.setName("lblInstallmentDate");
        lblInterest.setName("lblInterest");
        lblInterestRate.setName("lblInterestRate");
        txtInterestRate.setName("txtInterestRate");
        lblFrequency.setName("lblFrequency");
        lblRepaymentType.setName("lblRepaymentType");
        lblroundOfType.setName("lblroundOfType");
        lblLoanAmt.setName("lblLoanAmt");
        lblPrincipalAmt.setName("lblPrincipalAmt");
        lblTotal.setName("lblTotal");
        cboFrequency.setName("cboFrequency");
        cboRepaymentType.setName("cboRepaymentType");
        cboroundOfType.setName("cboroundOfType");
        panAcctInfo.setName("panAcctInfo");
        panInstallmentFields.setName("panInstallmentFields");
        //panInstallmentLeft.setName("panInstallmentLeft");
        //panInstallmentRight.setName("panInstallmentRight");
        panInstallmentTable.setName("panInstallmentTable");
        panInstallmentToolBtns.setName("panInstallmentToolBtns");
        //panInstallment_MainPan.setName("panInstallment_MainPan");
        srpInstallmentTable.setName("srpInstallmentTable");
        tblInstallmentTable.setName("tblInstallmentTable");
        tdtInstallmentDate.setName("tdtInstallmentDate");
        txtBalance.setName("txtBalance");
        txtInterest.setName("txtInterest");
        txtLoanAmt.setName("txtLoanAmt");
        txtPrincipalAmt.setName("txtPrincipalAmt");
        txtTotal.setName("txtTotal");
        panSubmitBtn.setName("panSubmitBtn");
        btnSubmit.setName("btnSubmit");
        //((javax.swing.border.TitledBorder)panInstallmentFields.getBorder()).setTitle(resourceBundle.getString("panInstallmentFields"));
        cboroundOfType.setName("cboroundOfType");
        //   rdoUniformPrincipalEMI.setName("rdoUniformPrincipalEMI");
        //  rdoUniformEMI.setName("rdoUniformEMI");
        
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblPrincipalAmt.setText(resourceBundle.getString("lblPrincipalAmt"));
        lblLoanAmt.setText(resourceBundle.getString("lblLoanAmt"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblNoOfInstall.setText(resourceBundle.getString("lblNoOfInstall"));
        lblInterest.setText(resourceBundle.getString("lblInterest"));
        lblInterestRate.setText(resourceBundle.getString("lblInterestRate"));
        //lblAcctNo_Disp.setText(resourceBundle.getString("lblAcctNo_Disp"));
        lblTotal.setText(resourceBundle.getString("lblTotal"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnOk.setText(resourceBundle.getString("btnOk"));
        btnSubmit.setText(resourceBundle.getString("btnSubmit"));
        lblInstallmentDate.setText(resourceBundle.getString("lblInstallmentDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        txtInterestRate.setText(resourceBundle.getString("txtInterestRate"));
        lblFrequency.setText(resourceBundle.getString("lblFrequency"));
        //lblroundOfType.setText(resourceBundle.getString("lblroundOfType"));
        lblRepaymentType.setText(resourceBundle.getString("lblRepaymentType"));
        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
    }
    
    public void update(java.util.Observable o, Object arg) {
        //lblAcctNo_Disp.setText(observable.getLblAcctNo_Disp());
        txtInterestRate.setText(observable.getTxtInterestRate());
        txtLoanAmt.setText(observable.getTxtLoanAmt());
        tdtInstallmentDate.setDateValue(observable.getTdtInstallmentDate());
        txtPrincipalAmt.setText(observable.getTxtPrincipalAmt());
        txtInterest.setText(observable.getTxtInterest());
        txtTotal.setText(observable.getTxtTotal());
        txtBalance.setText(observable.getTxtBalance());
        txtNoOfInstall.setText(observable.getTxtNoOfInstall());
        cboFrequency.setSelectedItem(observable.getCboFrequency());
        cboRepaymentType.setSelectedItem(observable.getCboRepaymentType());
        cboroundOfType.setSelectedItem(observable. getCboroundOfType());//note it change
        tblInstallmentTable.setModel(observable.getTblInstallment().getModel());
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtInterestRate(txtInterestRate.getText());
        observable.setTxtAcctNo(txtAcctNo.getText());
        observable.setTxtLoanAmt(txtLoanAmt.getText());
        //observable.setTdtInstallmentDate(tdtInstallmentDate.getDateValue());
        //observable.setTxtPrincipalAmt(txtPrincipalAmt.getText());
        //observable.setTxtInterest(txtInterest.getText());
        //observable.setTxtTotal(txtTotal.getText());
        //observable.setTxtBalance(txtBalance.getText());
        observable.setTxtNoOfInstall(txtNoOfInstall.getText());
        observable.setTxtMoratorium(txtMoratorium.getText());
        observable.setCboFrequency(CommonUtil.convertObjToStr(cboFrequency.getSelectedItem()));
        observable.setCboRepaymentType(CommonUtil.convertObjToStr(cboRepaymentType.getSelectedItem()));
        observable.setCboroundOfType(CommonUtil.convertObjToStr(cboroundOfType.getSelectedItem()));
        observable.setRepayScheduleMode("REGULAR");
        observable.setDisbusmentId("");
        observable.setActiveStatus("Y");
        if (rdoSalaryRecovery_Yes.isSelected()) {
            observable.setSalaryRecovery(true);
        } else {
            observable.setSalaryRecovery(false);
        }
        
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLoanAmt", new Boolean(true));
        mandatoryMap.put("txtInterestRate", new Boolean(true));
        mandatoryMap.put("tdtInstallmentDate", new Boolean(true));
        mandatoryMap.put("txtPrincipalAmt", new Boolean(true));
        mandatoryMap.put("txtInterest", new Boolean(true));
        mandatoryMap.put("txtTotal", new Boolean(true));
        mandatoryMap.put("txtBalance", new Boolean(true));
        mandatoryMap.put("cboFrequency", new Boolean(true));
        mandatoryMap.put("cboRepaymentType",new Boolean(true));
        mandatoryMap.put("cboroundOfType",new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setupScreen() {
        setTitle(resourceBundle.getString("screenTitle"));
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void initComponentData(){
        cboFrequency.setModel(observable.getCbmFrequency());
        cboRepaymentType.setModel(observable.getCbmRepaymentType());
        cboroundOfType.setModel(observable.getCbmroundOfType());
        //   cboroundOfType.setModel(observable. getcbmroundOfType());//will change
    }
    private void setMaxLength(){
//        txtInterestRate.setMaxLength(7);
//        txtInterestRate.setValidation(new NumericValidation(2,4));
//        txtLoanAmt.setMaxLength(16);
//        txtLoanAmt.setValidation(new CurrencyValidation(14,2));
//        txtNoOfInstall.setMaxLength(3);
//        txtNoOfInstall.setValidation(new NumericValidation());
//        txtPrincipalAmt.setMaxLength(16);
//        txtPrincipalAmt.setValidation(new CurrencyValidation(14,2));
//        txtInterest.setMaxLength(16);
//        txtInterest.setValidation(new CurrencyValidation(14,2));
//        txtTotal.setMaxLength(16);
//        txtTotal.setValidation(new CurrencyValidation(14,2));
//        txtBalance.setMaxLength(16);
//        txtBalance.setValidation(new CurrencyValidation(14,2));
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdbArbit = new com.see.truetransact.uicomponent.CButtonGroup();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        rdoSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        panAcctInfo = new com.see.truetransact.uicomponent.CPanel();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnSearchAcctNo = new com.see.truetransact.uicomponent.CButton();
        lblLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRepaymentType = new com.see.truetransact.uicomponent.CLabel();
        txtLoanBalance = new com.see.truetransact.uicomponent.CTextField();
        cboRepaymentType = new com.see.truetransact.uicomponent.CComboBox();
        lblloanBalance = new com.see.truetransact.uicomponent.CLabel();
        lblInterestRate = new com.see.truetransact.uicomponent.CLabel();
        txtInterestRate = new com.see.truetransact.uicomponent.CTextField();
        lblFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfInstall = new com.see.truetransact.uicomponent.CLabel();
        lblroundOfType = new com.see.truetransact.uicomponent.CLabel();
        lblMoratorium = new com.see.truetransact.uicomponent.CLabel();
        txtMoratorium = new com.see.truetransact.uicomponent.CTextField();
        lblMoratoriumPeriodInterst = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstall = new com.see.truetransact.uicomponent.CTextField();
        txtMoratoriumInt = new com.see.truetransact.uicomponent.CTextField();
        cboroundOfType = new com.see.truetransact.uicomponent.CComboBox();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        rdoSanctionAmt = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOutstandingAmt = new com.see.truetransact.uicomponent.CRadioButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtInstAmount = new com.see.truetransact.uicomponent.CTextField();
        panSalaryRecovery = new com.see.truetransact.uicomponent.CPanel();
        panSalaryRecoveryValue = new com.see.truetransact.uicomponent.CPanel();
        rdoSalaryRecovery_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSalaryRecovery_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        panInstallmentFields = new com.see.truetransact.uicomponent.CPanel();
        lblInstallmentDate = new com.see.truetransact.uicomponent.CLabel();
        lblPrincipalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblInterest = new com.see.truetransact.uicomponent.CLabel();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        tdtInstallmentDate = new com.see.truetransact.uicomponent.CDateField();
        txtPrincipalAmt = new com.see.truetransact.uicomponent.CTextField();
        txtInterest = new com.see.truetransact.uicomponent.CTextField();
        txtTotal = new com.see.truetransact.uicomponent.CTextField();
        txtBalance = new com.see.truetransact.uicomponent.CTextField();
        panInstallmentToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        panInstallmentTable = new com.see.truetransact.uicomponent.CPanel();
        srpInstallmentTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstallmentTable = new com.see.truetransact.uicomponent.CTable();
        panSubmitBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSubmit = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();

        lblSpace5.setText("     ");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Loan Repayment Schedule");
        setMaximumSize(new java.awt.Dimension(850, 630));
        setMinimumSize(new java.awt.Dimension(850, 630));
        setPreferredSize(new java.awt.Dimension(850, 630));

        lblAcctNo.setText("Account Number");

        txtAcctNo.setAllowAll(true);
        txtAcctNo.setAllowNumber(true);
        txtAcctNo.setPreferredSize(new java.awt.Dimension(36, 21));

        btnSearchAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSearchAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAcctNoActionPerformed(evt);
            }
        });

        lblLoanAmt.setText("Loan Amount");

        txtLoanAmt.setAllowAll(true);
        txtLoanAmt.setAllowNumber(true);
        txtLoanAmt.setPreferredSize(new java.awt.Dimension(27, 21));

        lblRepaymentType.setText("Repayment Type");

        txtLoanBalance.setAllowAll(true);
        txtLoanBalance.setAllowNumber(true);

        cboRepaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepaymentTypeActionPerformed(evt);
            }
        });

        lblloanBalance.setText("Loan Balance");

        lblInterestRate.setText("Interest Rate");

        txtInterestRate.setAllowAll(true);
        txtInterestRate.setAllowNumber(true);

        lblFrequency.setText("Frequency");

        cboFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFrequencyActionPerformed(evt);
            }
        });

        lblNoOfInstall.setText("No of Installments");

        lblroundOfType.setText("Round Off Type");

        lblMoratorium.setText("Moratorium");

        txtMoratorium.setAllowAll(true);
        txtMoratorium.setAllowNumber(true);

        lblMoratoriumPeriodInterst.setText("Moratorium Period Interest");

        txtNoOfInstall.setAllowAll(true);
        txtNoOfInstall.setAllowNumber(true);

        txtMoratoriumInt.setAllowAll(true);
        txtMoratoriumInt.setAllowNumber(true);

        btnOk.setText("Prepare Schedule");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        rdoSanctionAmt.setText("Sanction Amount");
        rdoSanctionAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSanctionAmtActionPerformed(evt);
            }
        });

        rdoOutstandingAmt.setText("Outstanding Amount");
        rdoOutstandingAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOutstandingAmtActionPerformed(evt);
            }
        });

        cLabel1.setText("Installment Amount");

        txtInstAmount.setAllowNumber(true);

        panSalaryRecovery.setMinimumSize(new java.awt.Dimension(305, 22));
        panSalaryRecovery.setPreferredSize(new java.awt.Dimension(305, 22));
        panSalaryRecovery.setLayout(new java.awt.GridBagLayout());

        panSalaryRecoveryValue.setMinimumSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setPreferredSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setLayout(new java.awt.GridBagLayout());

        rdoSalaryRecovery.add(rdoSalaryRecovery_Yes);
        rdoSalaryRecovery_Yes.setText("Yes");
        rdoSalaryRecovery_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoSalaryRecovery_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoSalaryRecovery_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_Yes, gridBagConstraints);

        rdoSalaryRecovery.add(rdoSalaryRecovery_No);
        rdoSalaryRecovery_No.setText("No");
        rdoSalaryRecovery_No.setMaximumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setMinimumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setPreferredSize(new java.awt.Dimension(44, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSalaryRecovery.add(panSalaryRecoveryValue, gridBagConstraints);

        lblSalaryRecovery.setText("Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSalaryRecovery.add(lblSalaryRecovery, gridBagConstraints);

        javax.swing.GroupLayout panAcctInfoLayout = new javax.swing.GroupLayout(panAcctInfo);
        panAcctInfo.setLayout(panAcctInfoLayout);
        panAcctInfoLayout.setHorizontalGroup(
            panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAcctInfoLayout.createSequentialGroup()
                .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panAcctInfoLayout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addComponent(lblLoanAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtLoanAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(83, 83, 83)
                            .addComponent(lblFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(28, 28, 28)
                            .addComponent(cboFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblroundOfType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboroundOfType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panAcctInfoLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(lblAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(txtAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(btnSearchAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblNoOfInstall, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNoOfInstall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAcctInfoLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblRepaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAcctInfoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblloanBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboRepaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLoanBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAcctInfoLayout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(lblMoratorium, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMoratorium, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(lblMoratoriumPeriodInterst, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(txtMoratoriumInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panAcctInfoLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtInstAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(panAcctInfoLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(rdoSanctionAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoOutstandingAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panSalaryRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        panAcctInfoLayout.setVerticalGroup(
            panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAcctInfoLayout.createSequentialGroup()
                .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnSearchAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNoOfInstall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNoOfInstall, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblLoanAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtLoanAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblroundOfType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboroundOfType, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAcctInfoLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(lblMoratoriumPeriodInterst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMoratoriumInt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRepaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboRepaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblloanBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLoanBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panAcctInfoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMoratorium, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMoratorium, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInstAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAcctInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoSanctionAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoOutstandingAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panSalaryRecovery, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        lblInstallmentDate.setText("Installment Date");

        lblPrincipalAmt.setText("Principal");

        lblInterest.setText("Interest");

        lblTotal.setText("Total");

        lblBalance.setText("Balance");

        tdtInstallmentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstallmentDateFocusLost(evt);
            }
        });

        txtPrincipalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrincipalAmtFocusLost(evt);
            }
        });

        txtInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestFocusLost(evt);
            }
        });

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panInstallmentToolBtnsLayout = new javax.swing.GroupLayout(panInstallmentToolBtns);
        panInstallmentToolBtns.setLayout(panInstallmentToolBtnsLayout);
        panInstallmentToolBtnsLayout.setHorizontalGroup(
            panInstallmentToolBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstallmentToolBtnsLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panInstallmentToolBtnsLayout.setVerticalGroup(
            panInstallmentToolBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstallmentToolBtnsLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panInstallmentToolBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout panInstallmentFieldsLayout = new javax.swing.GroupLayout(panInstallmentFields);
        panInstallmentFields.setLayout(panInstallmentFieldsLayout);
        panInstallmentFieldsLayout.setHorizontalGroup(
            panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblInstallmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(tdtInstallmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(lblPrincipalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtPrincipalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(lblInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(panInstallmentToolBtns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panInstallmentFieldsLayout.setVerticalGroup(
            panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblInstallmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtInstallmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblPrincipalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPrincipalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtInterest, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panInstallmentFieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInstallmentFieldsLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panInstallmentToolBtns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblInstallmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblInstallmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstallmentTableMousePressed(evt);
            }
        });
        srpInstallmentTable.setViewportView(tblInstallmentTable);

        javax.swing.GroupLayout panInstallmentTableLayout = new javax.swing.GroupLayout(panInstallmentTable);
        panInstallmentTable.setLayout(panInstallmentTableLayout);
        panInstallmentTableLayout.setHorizontalGroup(
            panInstallmentTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srpInstallmentTable, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panInstallmentTableLayout.setVerticalGroup(
            panInstallmentTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srpInstallmentTable, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSubmitBtnLayout = new javax.swing.GroupLayout(panSubmitBtn);
        panSubmitBtn.setLayout(panSubmitBtnLayout);
        panSubmitBtnLayout.setHorizontalGroup(
            panSubmitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSubmitBtnLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panSubmitBtnLayout.setVerticalGroup(
            panSubmitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSubmitBtnLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panSubmitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panInstallmentFields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(panSubmitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36)
                        .addComponent(panInstallmentTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(panAcctInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(panAcctInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panInstallmentFields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(panSubmitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panInstallmentTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panInstallmentFields.getAccessibleContext().setAccessibleName("Installment Details");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFrequencyActionPerformed
        // TODO add your handling code here:
        cboFrequencyActionPerformed();
    }//GEN-LAST:event_cboFrequencyActionPerformed

    private void txtInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestFocusLost
        // TODO add your handling code here:
        txtInterestFocusLost();
    }//GEN-LAST:event_txtInterestFocusLost

    private void txtPrincipalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrincipalAmtFocusLost
        // TODO add your handling code here:
        txtPrincipalAmtFocusLost();
    }//GEN-LAST:event_txtPrincipalAmtFocusLost

    private void tdtInstallmentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstallmentDateFocusLost
        // TODO add your handling code here:
        //tdtInstallmentDateFocusLost();
    }//GEN-LAST:event_tdtInstallmentDateFocusLost

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        btnSubmitActionPerformed();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(); 
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInstallmentFields);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            btnSaveActionPerformed();
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
       btnNewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewActionPerformed() {
        updateOBFields();
        observable.resetInstallmentDetails();
        setUserDefinedFieldsEnableDisable(true);
        setOnlyNewSaveBtnEnable();
        observable.ttNotifyObservers();
        selectedInstallmentRow = -1;
        updateInstallment = false;
    }
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
       try
        {   
            tblInstallmentTable.print();
        }
        catch(Exception e)
        {
            System.out.println("Exception"+e);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    
        String mandatoryMessage = "\n" ;
        boolean errorFlag = false;
        if(cboRepaymentType.getSelectedItem().equals("EMI")){
            errorFlag = true;
            mandatoryMessage = "Rescheduling not possible for EMI loans\n" ;
        }else if(cboRepaymentType.getSelectedItem().equals("")){
            errorFlag = true;
            mandatoryMessage = mandatoryMessage +  "Please enter repayment type\n" ;  
        }else if(cboFrequency.getSelectedItem().equals("")){
            errorFlag = true;
            mandatoryMessage  = mandatoryMessage + "Please enter repayment frequency\n" ;
        }else if(txtNoOfInstall.getText().length() == 0){
            errorFlag = true;
            mandatoryMessage  = mandatoryMessage + "Please enter number of installments\n" ;
        }else if(!rdoOutstandingAmt.isSelected() && !rdoSanctionAmt.isSelected()){
            errorFlag = true;
            mandatoryMessage  = mandatoryMessage + "Please select either sanction amount or outstanding amount\n" ;
        }        
        if(errorFlag == true){
            displayAlert(mandatoryMessage);
        }
        else{
            errorFlag = false;
            btnOkActionPerformed(); 
        }        
    }//GEN-LAST:event_btnOkActionPerformed

    private void tblInstallmentTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstallmentTableMousePressed
       
        if (isUserDefinedPanelVisible){
            tblInstallmentTableMousePressed();
        }
    }//GEN-LAST:event_tblInstallmentTableMousePressed

    private void btnSearchAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAcctNoActionPerformed
        // TODO add your handling code here:
        popUp("AccountSearch");
    }//GEN-LAST:event_btnSearchAcctNoActionPerformed

    private void cboRepaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepaymentTypeActionPerformed
        // TODO add your handling code here:
        // add code for validation        
        
    }//GEN-LAST:event_cboRepaymentTypeActionPerformed

    private void rdoOutstandingAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOutstandingAmtActionPerformed
        // TODO add your handling code here:
          txtLoanAmt.setText(observable.getOutstandingAmnt().replace("-", ""));
          observable.setTotalBaseAmount(txtLoanAmt.getText());
          rdoSanctionAmt.setSelected(false); 
    }//GEN-LAST:event_rdoOutstandingAmtActionPerformed

    private void rdoSanctionAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSanctionAmtActionPerformed
        // TODO add your handling code here:
        txtLoanAmt.setText(observable.getSanctionedAmount().replace("-", ""));
        observable.setTotalBaseAmount(txtLoanAmt.getText());
        rdoOutstandingAmt.setSelected(false);
    }//GEN-LAST:event_rdoSanctionAmtActionPerformed

    private void tblInstallmentTableMousePressed() {
        if (tblInstallmentTable.getSelectedRow() >= 0){
            updateOBFields();
            observable.populateInstallmentDetails(tblInstallmentTable.getSelectedRow());
            selectedInstallmentRow = tblInstallmentTable.getSelectedRow();
            updateInstallment = true;
            setUserDefinedFieldsEnableDisable(true);
            setAllToolBtnsEnableDisable(true);
        }
    }
    private void btnOkActionPerformed() {       
        Date loanDueDt = null;
        updateOBFields();
        HashMap acctNumMap = new HashMap();
        HashMap data = new HashMap();
        String loanNumber = txtAcctNo.getText();
        acctNumMap.put("ACCT_NUM", loanNumber);
        HashMap loanRepayDetailsMap = observable.getLoansDetailsForScheduleCreation(acctNumMap);        
        //System.out.println("repayDetailsMap :: " + loanRepayDetailsMap);  
        if(txtMoratorium.getText().length() > 0){
            moratoriumAvailable = true;
        }
        fromDate = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("FROM_DT"))));
        loanDueDt = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("TO_DT"))));
        toDate = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("TO_DT"))));
        repayDate = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("REPAYMENT_DT"))));
        disbursmentDate = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("DISBURSEMENT_DT"))));
        sanctionDate = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanRepayDetailsMap.get("SANCTION_DT"))));
        
        
        observable.setFromDate(DateUtil.getStringDate(fromDate));
        observable.setDisbusmentDate(DateUtil.getStringDate(disbursmentDate));
        observable.setSanctionDate(DateUtil.getStringDate(sanctionDate));
        if (toDate != null) {
            data.put("TO_DATE", toDate);
        }
        txtNoInstallmentsFocusLost();
        
        //System.out.println("inside btnOkActionPerformed : fromDate = " + fromDate +" , toDate = " + toDate +" ,repayDate = " + repayDate);
        
        data.put("ACCT_NUM",loanNumber);        
        data.put("NO_INSTALL", txtNoOfInstall.getText());// No. of installments
        data.put("PRINCIPAL_AMOUNT", txtLoanAmt.getText());
        data.put("INTEREST", txtInterestRate.getText());// Interest Rate
        data.put("REPAYMENT_FREQUENCY", observable.getCbmFrequency().getKeyForSelected());
        String roundingType=CommonUtil.convertObjToStr(observable.getCbmroundOfType().getKeyForSelected());
        //System.out.println("rounding type####"+roundingType);
        if(roundingType!=null && roundingType.length()>0)
            if(! roundingType.equals("NO_ROUND_OFF"))
            data.put("ROUNDING_TYPE",roundingType);        
        data.put("ROUNDING_FACTOR", "1_RUPEE");
        data.put("COMPOUNDING_PERIOD", observable.getCbmFrequency().getKeyForSelected());
        data.put("COMPOUNDING_TYPE", "REPAYMENT");
        String s = (String)observable.getCbmRepaymentType().getKeyForSelected();
        if (observable.getCbmFrequency().getKeyForSelected().equals("30")) {
            if (s.equals("EMI")) {
                data.put("REPAYMENT_TYPE", s);
            } else {
                data.put("REPAYMENT_TYPE", s);
            }
        } else if (observable.getCbmFrequency().getKeyForSelected().equals("90")) {
            data.put("REPAYMENT_TYPE", "EQI");
        } else if (observable.getCbmFrequency().getKeyForSelected().equals("180")) {
            data.put("REPAYMENT_TYPE", "EHI");
        } else if (observable.getCbmFrequency().getKeyForSelected().equals("365")) {
            data.put("REPAYMENT_TYPE", "EYI");
        }else{
             data.put("REPAYMENT_TYPE", "USER_DEFINED");
        }
        data.put("ISDURATION_DDMMYY", "YES");
        data.put("INTEREST_TYPE", "COMPOUND");
        data.put("DURATION_YY", txtNoOfInstall.getText());
        if(moratoriumAvailable){
           if(repayDate != null){
            data.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(observable.getRepayFromDate())));
           } 
        } 
        else{
           if(fromDate != null){
            data.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(observable.getFromDate())));
           }
        }
        //check for RBI           
        System.out.println("Salary recovery :: " + CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE));
        if ((CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && rdoOutstandingAmt.isSelected())||rdoSalaryRecovery_Yes.isSelected()==true) {
            int noInstallments = 0;
            int noOfInstalmnts = 0;
            Date sanctioDate =  DateUtil.getDateMMDDYYYY(observable.getRepayFromDate());
            java.util.GregorianCalendar sanCal = new java.util.GregorianCalendar();
            sanCal.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(sanctioDate)));
            sanCal.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(sanctioDate)));
            int currdat = sanctioDate.getDate();
            Date fromDate = new Date();
            HashMap whereMap = new HashMap();
            List recoveryParameterList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
            if (recoveryParameterList != null & recoveryParameterList.size() > 0) {
                whereMap = (HashMap) recoveryParameterList.get(0);
                if (whereMap != null && whereMap.size() > 0) {
                    int firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                    System.out.println("currdat outside:: " + currdat);    
                    System.out.println("firstDay outside:: " + firstDay);
                    if (currdat <= firstDay) {
                        System.out.println("currdat :: " + currdat);    
                        System.out.println("firstDay :: " + firstDay);
                        sanCal.set(sanCal.MONTH, sanCal.get(sanCal.MONTH) - 1);
                        sanCal.set(sanCal.DAY_OF_MONTH, sanCal.getActualMaximum(sanCal.DAY_OF_MONTH));
                        fromDate = sanCal.getTime();
                    } else {
                        sanCal.set(sanCal.DAY_OF_MONTH, 1);
                        sanCal.set(sanCal.MONTH, sanCal.get(sanCal.MONTH));
                        int lastDayOfMonth = sanCal.getActualMaximum(sanCal.DAY_OF_MONTH);
                        fromDate = sanCal.getTime();
                        fromDate.setDate(lastDayOfMonth);
                    }
                }
            }
            data.put("FROM_DATE", fromDate);
            data.put("SALARY_RECOVERY", "Y");
            // Populate automatically nithya
            HashMap detailMap = new HashMap();
            detailMap.put("FROM_DT", fromDate);
            detailMap.put("TO_DT", loanDueDt);
            int monthsBetween = 0;
            List lst = ClientUtil.executeQuery("getInstallmentsRemainingForOutstandingAmnt", detailMap);
            if (lst != null && lst.size() > 0) {
                HashMap instMap = (HashMap) lst.get(0);
                monthsBetween = CommonUtil.convertObjToInt(instMap.get("PENDING"));
            }
            if(CommonUtil.convertObjToInt(txtNoOfInstall.getText()) > monthsBetween){
              displayAlert("Number of installments should not exceed " + monthsBetween); 
              txtNoOfInstall.setText(CommonUtil.convertObjToStr(monthsBetween));
              return;
            }           
            // End
            if ((CommonUtil.convertObjToInt(txtMoratorium.getText()) > 0) && ((txtMoratorium.getText()) != null)) {
                noOfInstalmnts = CommonUtil.convertObjToInt(txtNoOfInstall.getText()) + CommonUtil.convertObjToInt(txtMoratorium.getText());
            } else {
                noOfInstalmnts = CommonUtil.convertObjToInt(txtNoOfInstall.getText());
            }
            
            double amountCalculated = noOfInstalmnts * CommonUtil.convertObjToDouble(txtInstAmount.getText());
            if(amountCalculated > CommonUtil.convertObjToDouble(txtLoanAmt.getText())){
                 displayAlert("Cannot reschedule for the given installment amount "+txtInstAmount.getText()+"\nThe outstanding balance is only  " + txtLoanAmt.getText()); 
                 double maxInstAmnt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()) / noOfInstalmnts ;
                 txtInstAmount.setText(CommonUtil.convertObjToStr(Math.round(maxInstAmnt)));
                 return;
            }
            if (noOfInstalmnts > 0) {
                int no_mnths = 0;
                if (cboRepaymentType.getSelectedIndex() > 0) {
                    if (cboRepaymentType.getSelectedItem().equals("EMI")) {
                        no_mnths = noOfInstalmnts;
                    }
                    if (cboRepaymentType.getSelectedItem().equals("Lump Sum")) {
                        no_mnths = noOfInstalmnts;
                    }
                    if (cboRepaymentType.getSelectedItem().equals("EQI")) {
                        no_mnths = noOfInstalmnts * 4;
                    }
                    if (cboRepaymentType.getSelectedItem().equals("EYI")) {
                        no_mnths = noOfInstalmnts * 12;
                    }
                    if (cboRepaymentType.getSelectedItem().equals("EHI")) {
                        no_mnths = noOfInstalmnts * 6;
                    }
                    if (cboRepaymentType.getSelectedItem().equals("Uniform Principle EMI")) {
                        no_mnths = noOfInstalmnts;
                    }
                    System.out.println("no_mnths" + no_mnths);
                }
                int installmentsToBeset = 1;
                for (int i = 0; i < no_mnths - 1; i++) {
                    java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                    gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                    gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                    gCalendar.set(gCalendar.DATE, 1);
                    int curMonth = gCalendar.get(gCalendar.MONTH);
                    int nxtMonth = curMonth + 1;
                    gCalendar.set(gCalendar.MONTH, nxtMonth);
                    gCalendar.set(gCalendar.DATE, gCalendar.getActualMaximum(gCalendar.DATE));
                    fromDate = gCalendar.getTime();
                    if (DateUtil.dateDiff(fromDate, loanDueDt) >= 0) {
                        installmentsToBeset++;
                    }
                }
                System.out.println("installmentsToBeset :: " + installmentsToBeset);
                System.out.println("installment date final :: " + fromDate);

            } else {
                displayAlert("Please enter installment number");
            }

        }  
        // End
        if(repayDate != null){
            data.put("REPAY_DATE", repayDate);
        }
        if (CommonUtil.convertObjToDouble(txtInstAmount.getText()) > 0) {
            data.put("INSTALLMENT_AMOUNT", CommonUtil.convertObjToDouble(txtInstAmount.getText()));
        }
        //System.out.println("data for schedule creation :: " + data);
        populateData(data);
    }
    
    private void btnSaveActionPerformed() {
        updateOBFields();
        if (observable.addInstallmentDetails(selectedInstallmentRow, updateInstallment) == 1){
            setUserDefinedFieldsEnableDisable(true);
        }else{
            observable.resetInstallmentDetails();
            setUserDefinedFieldsEnableDisable(false);
            setOnlyNewBtnEnable();
            updateInstallment = false;
            selectedInstallmentRow = -1;
        }
        observable.ttNotifyObservers();
    }
    private void btnDeleteActionPerformed() {
        // TODO add your handling code here:
        updateOBFields();
        observable.deleteInstallmentTabRecord(selectedInstallmentRow);
        observable.resetInstallmentDetails();
        setOnlyNewBtnEnable();
        updateInstallment = false;
        selectedInstallmentRow = -1;
        observable.ttNotifyObservers();
    }
    private void btnSubmitActionPerformed() {              
       saveAction();
       //observable.doAction();
    }
    
    private void tdtInstallmentDateFocusLost() {
        ClientUtil.validateFromDate(tdtInstallmentDate, CommonUtil.convertObjToStr(data.get("TO_DATE")));
        ClientUtil.validateToDate(tdtInstallmentDate, CommonUtil.convertObjToStr(data.get("FROM_DATE")));
    }
    
    private void txtPrincipalAmtFocusLost() {
        if (txtPrincipalAmt.getText().length() != 0 && txtInterest.getText().length() != 0){
            double principalAmt = CommonUtil.convertObjToDouble(txtPrincipalAmt.getText()).doubleValue();
            double interestAmt = CommonUtil.convertObjToDouble(txtInterest.getText()).doubleValue();
            txtTotal.setText(CommonUtil.convertObjToStr(new Double((principalAmt + interestAmt))));
            observable.setTxtTotal(txtTotal.getText());
        }
    }
    private void txtInterestFocusLost() {
        txtPrincipalAmtFocusLost();
    }
    
   private void cboFrequencyActionPerformed() {
        // TODO add your handling code here:
//        if (this.data == null && (cboFrequency.getSelectedItem().equals("User Defined") || cboFrequency.getSelectedItem().equals("Lump Sum"))){
//           // btnOk.setEnabled(false);
//        }else{
//            btnOk.setEnabled(true);
//        }
    }
   
   
    private void exitForm(java.awt.event.WindowEvent evt) {                          
        observable.destroyObjects();
        observable = null;
        this.dispose();
    }                         
           
   
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void setUserDefinedFieldsEnableDisable(boolean val){
        tdtInstallmentDate.setEnabled(val);
        txtInterest.setEnabled(val);
        txtBalance.setEnabled(val);
        txtPrincipalAmt.setEnabled(val);
        txtTotal.setEditable(false);
    }
    
    private void setPopulateFieldsEnableDisable(boolean val){
        cboFrequency.setEnabled(val);
        cboRepaymentType.setEnabled(val);
       // cboroundOfType.setEnabled(val);
        txtLoanAmt.setEnabled(val);
        txtNoOfInstall.setEnabled(val);
        txtInterestRate.setEnabled(val);
     //   btnOk.setEnabled(val);
    }
    
    private void setAllToolBtnsEnableDisable(boolean val){
        btnDelete.setEnabled(val);
        btnNew.setEnabled(val);
        btnSave.setEnabled(val);
    }
    
    private void setOnlyNewBtnEnable(){
        btnDelete.setEnabled(false);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
    }
    
    private void setOnlyNewSaveBtnEnable(){
        btnDelete.setEnabled(false);
        btnNew.setEnabled(true);
        btnSave.setEnabled(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        ArrayList principalIntRate = new ArrayList();
        HashMap interestRateAndDuration = new HashMap();
        HashMap data = new HashMap();
        data.put("ACT_NO", "LA0000000000001021");
        data.put("FROM_DATE", "5/1/2005");
        data.put("TO_DATE", "05/01/2009");
        data.put("NO_INSTALL", "5");// No. of installments
        data.put("PRINCIPAL_AMOUNT", "100000");
        data.put("INTEREST", "8");// Interest Rate
        data.put("REPAYMENT_FREQUENCY", "365");
        data.put("COMPOUNDING_TYPE", "COMPOUND");
        data.put("INTEREST_TYPE", "COMPOUND");
        data.put("ROUNDING_TYPE", "HIGHER");
        data.put("MONTH_OPTION", "30");
        data.put("YEAR_OPTION", "360");
        data.put("ROUNDING_FACTOR", "50_PAISE");
        data.put("COMPOUNDING_PERIOD", "ANNUALLY");
        data.put("FLOAT_PRECISION", "2");
        data.put("DURATION_YY", "5");
        data.put("REPAYMENT_TYPE", "USER_DEFINED");
        data.put("ISDURATION_DDMMYY", "NO");
        new RepaymentScheduleCreationUI().show();
    }
    
    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        if(field.equalsIgnoreCase("AccountSearch")){
       
            ArrayList lst = new ArrayList();
            lst.add("BORROWER NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            editMapCondition.put("AUTHORIZE_REMARK", "= 'GOLD_LOAN'");
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForNewScheduleCreation");//
//            HashMap whereMap = new HashMap();
//            viewMap.put(CommonConstants.MAP_NAME, "viewTermLoan");             
//            //whereMap.put("PROD_DESC", productType);
//            whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            //whereMap.put("CURRENT_DT", curr_dt);
//            //whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
//            setSelectedBranchID(TrueTransactMain.selBranch);
//            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
         new ViewAll(this, viewMap).show();
    }

    public void fillData(Object param) {        
        //System.out.println("inside fill data :: " + param);        
        HashMap hash = (HashMap) param;
        if(hash.containsKey("INSTALLMENT")){
            
        }else{
          //System.out.println("inside fill data ::  account num :: " + hash.get("ACCT_NUM"));
            txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
            observable.setTxtAcctNo(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
            txtLoanAmt.setText(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));
            observable.setTotalBaseAmount(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));
            observable.setOutstandingAmnt(CommonUtil.convertObjToStr(hash.get("OUTSTANDING_AMNT")));
            observable.setSanctionedAmount(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));
            if (hash.containsKey("PROD_SALARY_RECOVERY") && hash.get("PROD_SALARY_RECOVERY").equals("Y")) {
                panSalaryRecovery.setVisible(true);
                if (hash.containsKey("SALARY_RECOVERY") && hash.get("SALARY_RECOVERY").equals("Y")) {
                    rdoSalaryRecovery_Yes.setSelected(true);
                } else {
                    rdoSalaryRecovery_No.setSelected(false);
                }

            } else {
                panSalaryRecovery.setVisible(false);
            }

            populateRepaymentDetails();
        }
       
    }
    
    private void populateRepaymentDetails(){
        HashMap acctNumMap = new HashMap();
        HashMap loanNoMap = new HashMap();
        String loanNumber = txtAcctNo.getText();
        acctNumMap.put("ACCT_NUM", loanNumber);
        loanNoMap.put("value", loanNumber);
        HashMap repayDetailsMap = observable.getRepaymentDetailsForAcctno(acctNumMap);
        //System.out.println("repayDetailsMap :" +repayDetailsMap);
        String interestAmount =observable.getInterestDetailsConditions(loanNoMap);
        //System.out.println("interestAmount :: " + interestAmount);
        //{INTEREST_RATE=13, REPAYMENT_FREQ=365, REPAYMENT_TYPE=EYI}
        // cboRepayFreq.setSelectedItem(((ComboBoxModel) cboRepayFreq.getModel()).getDataForKey("1"));
        observable.setTxtInterest(interestAmount);
        observable.setCboFrequency(repayDetailsMap.get("REPAYMENT_FREQ").toString());
        observable.setCboRepaymentType(repayDetailsMap.get("REPAYMENT_TYPE").toString());
        txtInterestRate.setText(observable.getTxtInterest());
        cboFrequency.setSelectedItem(((ComboBoxModel) cboFrequency.getModel()).getDataForKey(observable.getCboFrequency()));
        cboRepaymentType.setSelectedItem(((ComboBoxModel) cboRepaymentType.getModel()).getDataForKey(observable.getCboRepaymentType()));
        //observable.resetOB();
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curr_dt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        
    }
    
   
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearchAcctNo;
    private com.see.truetransact.uicomponent.CButton btnSubmit;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentType;
    private com.see.truetransact.uicomponent.CComboBox cboroundOfType;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentDate;
    private com.see.truetransact.uicomponent.CLabel lblInterest;
    private com.see.truetransact.uicomponent.CLabel lblInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMoratorium;
    private com.see.truetransact.uicomponent.CLabel lblMoratoriumPeriodInterst;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstall;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalAmt;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentType;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblloanBalance;
    private com.see.truetransact.uicomponent.CLabel lblroundOfType;
    private com.see.truetransact.uicomponent.CPanel panAcctInfo;
    private com.see.truetransact.uicomponent.CPanel panInstallmentFields;
    private com.see.truetransact.uicomponent.CPanel panInstallmentTable;
    private com.see.truetransact.uicomponent.CPanel panInstallmentToolBtns;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecovery;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryValue;
    private com.see.truetransact.uicomponent.CPanel panSubmitBtn;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CRadioButton rdoOutstandingAmt;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSalaryRecovery;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSanctionAmt;
    private com.see.truetransact.uicomponent.CScrollPane srpInstallmentTable;
    private com.see.truetransact.uicomponent.CTable tblInstallmentTable;
    private com.see.truetransact.uicomponent.CDateField tdtInstallmentDate;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtBalance;
    private com.see.truetransact.uicomponent.CTextField txtInstAmount;
    private com.see.truetransact.uicomponent.CTextField txtInterest;
    private com.see.truetransact.uicomponent.CTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtLoanBalance;
    private com.see.truetransact.uicomponent.CTextField txtMoratorium;
    private com.see.truetransact.uicomponent.CTextField txtMoratoriumInt;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstall;
    private com.see.truetransact.uicomponent.CTextField txtPrincipalAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    private void saveAction() {       
       observable.repaymentFillData(observable.getInstallmentAll(), observable.getTotalRepayAmount());   
       //System.out.println("observable.getInstallmentAll() :: " + observable.getInstallmentAll());
       updateInstallment = true;      
       observable.doAction();
       if (observable.getResult().equals(ClientConstants.RESULT_STATUS[1])) {
           ClientUtil.showMessageWindow("schedule updated for Acct No  :" + observable.getTxtAcctNo());
           observable.resetOBFields();
           clearUIComponents();
       }else{
           ClientUtil.showMessageWindow("Updation failed" + observable.getTxtAcctNo());
           observable.resetOBFields();
       }
    }

   private void txtNoInstallmentsFocusLost() {
        //System.out.println("cboRepayFreq.getSelectedItem()&$$$&"+cboFrequency.getSelectedItem());
        if (cboFrequency.getSelectedItem().equals("User Defined") || cboFrequency.getSelectedItem().equals("Lump Sum")) {
            moratorium_Given_Calculation();
        } else {
            calculateSanctionToDate();
        }        
    } 
   
   private void moratorium_Given_Calculation() {
        if (!fromDate.equals("") && !cboFrequency.getSelectedItem().equals("") && !(txtNoOfInstall.getText().length() == 0)) {
            if (txtMoratorium.getText().length() > 0) {
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                gCalendar.setGregorianChange(fromDate);
                gCalendar.setTime(fromDate);
                int incVal = CommonUtil.convertObjToInt(txtMoratorium.getText());
                gCalendar.add(gCalendar.MONTH, incVal);
                repayDate = gCalendar.getTime();
                //observable.setFirstInstallDate(DateUtil.getStringDate(repayDate));  
                observable.setRepayFromDate(DateUtil.getStringDate(repayDate));
                gCalendar = null;
            } else {                
                repayDate =fromDate;
                observable.setRepayFromDate(DateUtil.getStringDate(repayDate));
                //observable.setFirstInstallDate(DateUtil.getStringDate(repayDate));
                //observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue());                
            }
            //System.out.println("inside moratorium_Given_Calculation() repayDate :: " + repayDate);
        }
    }
   private void calculateSanctionToDate() {
   
            if (!fromDate.equals("") && !cboFrequency.getSelectedItem().equals("") && !txtNoOfInstall.getText().equals("")) {
                moratorium_Given_Calculation();
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
                if (repayDate == null) {
                    repayDate = fromDate ;
                }
                if (txtMoratorium.getText().length() > 0) {
                    moratoriumAvailable = true;
                }
                gCalendar.setGregorianChange(repayDate);
                gCalendar.setTime(repayDate);
                gCalendarrepaydt.setGregorianChange(repayDate);
                gCalendarrepaydt.setTime(repayDate);
                int dateVal = observable.getIncrementType();
                int incVal = observable.getInstallNo(txtNoOfInstall.getText(), dateVal);
                date = new java.util.Date();
                date = repayDate ;
                if (txtNoOfInstall.getText().equals("1")) {
                    date = fromDate ;
                }
                //System.out.println("Date##" + date);
                if (dateVal <= 7) {
                    gCalendar.add(gCalendar.DATE, incVal);
                } else if (dateVal >= 30) {
                    gCalendar.add(gCalendar.MONTH, incVal);
                    int firstInstall = dateVal / 30;
                    gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
                }
                //added by rishad 17/12/2019
                if (rdoSalaryRecovery_Yes.isSelected() == true) {
                    HashMap whereMap = new HashMap();
                    List recoveryParameterList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
                    if (recoveryParameterList != null & recoveryParameterList.size() > 0) {
                        whereMap = (HashMap) recoveryParameterList.get(0);
                        int firstDay = 0;
                        int sanctionDay = 0;
                        int installmentStartDay = 0;
                        Date sancDt = repayDate;
                        Date instDate = repayDate;
                        firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                        sanctionDay = sancDt.getDate();
                        installmentStartDay = instDate.getDate();

                        GregorianCalendar cal = new GregorianCalendar((sancDt.getYear() + 1900), sancDt.getMonth(), sancDt.getDate());
                        GregorianCalendar instCal = new GregorianCalendar((instDate.getYear() + 1900), instDate.getMonth(), instDate.getDate());
                        int lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                        int lastDayOfInstMonth = instCal.getActualMaximum(instCal.DAY_OF_MONTH);
                        Date dt = cal.getTime();
                        Date insDt = instCal.getTime();
                        if (moratoriumAvailable == false) {
                            if (sanctionDay <= firstDay) {
                                cal.set(dt.getYear() + 1900, dt.getMonth(), lastDayOfMonth);
                                dt = cal.getTime();
                            } else {
                                cal.set(dt.getYear() + 1900, dt.getMonth() + 1, dt.getDate());
                                lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                                dt = cal.getTime();
                                dt.setDate(lastDayOfMonth);
                            }
                            repayDate = dt;
                            gCalendarrepaydt.set(dt.getYear() + 1900, dt.getMonth(), dt.getDate());
                            observable.setTdtInstallmentDate(repayDate.toString());
                            repayDate = (Date) dt;
                            //To Date
                            gCalendar = new java.util.GregorianCalendar();
                            gCalendar.setGregorianChange(repayDate);
                            gCalendar.setTime(repayDate);
                            dateVal = observable.getIncrementType();
                            incVal = observable.getInstallNo(String.valueOf(CommonUtil.convertObjToDouble(txtNoOfInstall.getText()).doubleValue() - 1), dateVal);
                            date = new java.util.Date();
                            date = repayDate;
                            if (txtNoOfInstall.getText().equals("1")) {
                                date = fromDate;
                            }
                            if (dateVal <= 7) {
                                gCalendar.add(gCalendar.DATE, incVal);
                            } else if (dateVal >= 30) {
                                gCalendar.add(gCalendar.MONTH, incVal);
                            }
                        }

                        if (moratoriumAvailable == true) {
                            if (installmentStartDay <= firstDay) {
                                instCal.set(insDt.getYear() + 1900, insDt.getMonth(), lastDayOfInstMonth);
                                insDt = instCal.getTime();
                            } else {
                                instCal.set(insDt.getYear() + 1900, insDt.getMonth() + 1, insDt.getDate());
                                lastDayOfInstMonth = instCal.getActualMaximum(instCal.DAY_OF_MONTH);
                                insDt = instCal.getTime();
                                insDt.setDate(lastDayOfInstMonth);
                            }
                            gCalendarrepaydt.set(insDt.getYear() + 1900, insDt.getMonth(), insDt.getDate());
                            repayDate = (Date) insDt;
                            //To Date
                            gCalendar = new java.util.GregorianCalendar();
                            gCalendar.setGregorianChange(repayDate);
                            gCalendar.setTime(repayDate);
                            dateVal = observable.getIncrementType();
                            incVal = observable.getInstallNo(String.valueOf(CommonUtil.convertObjToDouble(txtNoOfInstall.getText()).doubleValue() - 1), dateVal);
                            date = new java.util.Date();
                            date = repayDate;
                            if (txtNoOfInstall.getText().equals("1")) {
                                date = fromDate;
                            }
                            if (dateVal <= 7) {
                                gCalendar.add(gCalendar.DATE, incVal);
                            } else if (dateVal >= 30) {
                                gCalendar.add(gCalendar.MONTH, incVal);
                            }
                        }
                    } else {
                        ClientUtil.showMessageWindow("Pls Enter Recovery Parameter Details");
                        return;
                    }
                }
                toDate = gCalendar.getTime();
                observable.setToDate(DateUtil.getStringDate(toDate));
                repayDate = gCalendarrepaydt.getTime();
                observable.setFirstInstallDate(DateUtil.getStringDate(repayDate));
                gCalendar = null;
                gCalendarrepaydt = null;
                
                
//                toDate = gCalendar.getTime();
//                observable.setToDate(DateUtil.getStringDate(toDate));
//                observable.setRepayFromDate(DateUtil.getStringDate(repayDate)); 
//                //repayDate = gCalendarrepaydt.getTime();
//                observable.setFirstInstallDate(DateUtil.getStringDate(gCalendarrepaydt.getTime()));               
//                gCalendar = null;
//                gCalendarrepaydt = null;
            }       
      }
   private void clearUIComponents(){
       txtLoanAmt.setText("");
       txtLoanBalance.setText("");
       txtInterestRate.setText("");
       txtMoratorium.setText(""); 
       ClientUtil.clearAll(this);
   }
   public static double MonthDiff(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) ;
    }
}
