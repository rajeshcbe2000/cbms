/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriTermLoanInstallmentUI.java
 *
 * Created on January 25, 2005, 3:49 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agriemicalculator;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.termloan.agritermloan.AgriTermLoanUI;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
/**
 *
 * @author  152713
 */
public class AgriTermLoanInstallmentUI extends CDialog implements Observer, UIMandatoryField{
    HashMap mandatoryMap;
    HashMap data;
    AgriTermLoanUI parent = null;
    final AgriTermLoanInstallmentRB resourceBundle = new AgriTermLoanInstallmentRB();
    AgriTermLoanInstallmentOB observable;
    private int selectedInstallmentRow = -1;
    private boolean canShow = false;
    private boolean isUserDefinedPanelVisible = false;
    private boolean updateInstallment = false;
    String[] strMandatory = {"INTEREST_TYPE", "NO_INSTALL", "DURATION_YY", "REPAYMENT_TYPE",
    "PRINCIPAL_AMOUNT", "COMPOUNDING_PERIOD", "REPAYMENT_FREQUENCY", "INTEREST"};
    String[] strUserDefMandatory = {"FROM_DATE", "TO_DATE"};
    /**
     * Creates new form TermLoanInstallmentUI
     * @param data is a HashMap
     * It must have the following keys and values
     *
     *        data.put("FROM_DATE", "01/01/2005");
     *        data.put("TO_DATE", "05/01/2009");
     *        data.put("NO_INSTALL", "12");// No. of installments
     *        data.put("PRINCIPAL_AMOUNT", "100000");
     *        data.put("INTEREST", "8");// Interest Rate
     *        data.put("REPAYMENT_FREQUENCY", "30"); // 1,7,30/31,90,180,360/365.
     *        data.put("COMPOUNDING_TYPE", "REPAYMENT");
     *        data.put("INTEREST_TYPE", "COMPOUND");
     *        data.put("ROUNDING_TYPE", "HIGHER");
     *        data.put("MONTH_OPTION", "30");
     *        data.put("YEAR_OPTION", "360");
     *        data.put("ROUNDING_FACTOR", "50_PAISE");
     *        data.put("COMPOUNDING_PERIOD", "MONTHLY");
     *        data.put("FLOAT_PRECISION", "2");
     *        data.put("ISDURATION_DDMMYY", "YES");
     *        data.put("DURATION_YY", "12");
     *        // Only if the interest rate is floating
     *        interestRateAndDuration.put("INTEREST", "7");
     *        interestRateAndDuration.put("FROM_DATE", "5/1/2005");
     *        principalIntRate.add(interestRateAndDuration);
     *        interestRateAndDuration = null;
     *        interestRateAndDuration = new HashMap();
     *        interestRateAndDuration.put("INTEREST", "9");
     *        interestRateAndDuration.put("FROM_DATE", "5/3/2005");
     *        principalIntRate.add(interestRateAndDuration);
     *        interestRateAndDuration = null;
     *        interestRateAndDuration = new HashMap();
     *        interestRateAndDuration.put("INTEREST", "11");
     *        interestRateAndDuration.put("FROM_DATE", "5/5/2005");
     *        principalIntRate.add(interestRateAndDuration);
     *        interestRateAndDuration = null;
     *        interestRateAndDuration = new HashMap();
     *        interestRateAndDuration.put("INTEREST", "13");
     *        interestRateAndDuration.put("FROM_DATE", "5/7/2005");
     *        principalIntRate.add(interestRateAndDuration);
     *        interestRateAndDuration = null;
     *        // Only if the interest rate is floating
     *        data.put("VARIOUS_INTEREST_RATE", principalIntRate);
     */
    public AgriTermLoanInstallmentUI(AgriTermLoanUI parent, HashMap data, boolean hideSubmit) {
        this.parent = parent;
        setupinit(data);
        if (data !=null && (!data.containsKey("LTD")))
            setupScreen();
        if (data == null){
            canShow = true;
        }
        if (hideSubmit == true){
            btnSubmit.setVisible(false);
        }
        populateData();
    }
    
    public AgriTermLoanInstallmentUI(AgriTermLoanUI parent, HashMap data) {
        this(parent, data, false);
//        populateData();
    }
    
    public AgriTermLoanInstallmentUI() {
        this(null, null);
        show();
    }
    
    public void show(){
        super.show();
    }
    
    private void populateData() {
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
                btnOk.setVisible(false);
                populateCommonFields();
            }else{
                setPopulateFieldsEnableDisable(true);//Enable Populating fields
                panSubmitBtn.setVisible(false);
                panInstallmentFields.setVisible(false);
                isUserDefinedPanelVisible = false;
            }
            observable.ttNotifyObservers();
            if (data !=null && (!data.containsKey("LTD"))) {
                show();
            } else if (data !=null) {
                btnSubmitActionPerformed();
            }
        }
    }
    
    
    private boolean mandatoryKeyValueCheck(HashMap data){
        String strKeyWarn = resourceBundle.getString("keyWarning");
        StringBuffer stbKeyHelpMessage;
        if (data != null){
            stbKeyHelpMessage = checkFields(strMandatory);
            if (data.containsKey("REPAYMENT_TYPE") && data.get("REPAYMENT_TYPE").equals("USER_DEFINED")){
                stbKeyHelpMessage = stbKeyHelpMessage.append(checkFields(strUserDefMandatory));
            }
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
    
    private void populateCommonFields(){
        observable.setTxtInterestRate(CommonUtil.convertObjToStr(data.get("INTEREST")));
        observable.setLblAcctNo_Disp(CommonUtil.convertObjToStr(data.get("ACT_NO")));
        observable.setTxtNoOfInstall(CommonUtil.convertObjToStr(data.get("NO_INSTALL")));
        observable.setTxtLoanAmt(CommonUtil.convertObjToStr(data.get("PRINCIPAL_AMOUNT")));
        observable.setCboFrequency(CommonUtil.convertObjToStr(observable.getCbmFrequency().getDataForKey(data.get("REPAYMENT_FREQUENCY"))));
        lblAcctNo_Disp.setText(observable.getLblAcctNo_Disp());
        txtInterestRate.setText(observable.getTxtInterestRate());
        txtLoanAmt.setText(observable.getTxtLoanAmt());
        cboFrequency.setSelectedItem(observable.getCbmFrequency());
        cboroundOfType.setSelectedItem(observable.getCbmroundOfType());
        cboRepaymentType.setSelectedItem(observable.getCbmRepaymentType());
        txtNoOfInstall.setText(observable.getTxtNoOfInstall());
    }
    
    private void setObservable(){
        observable = new AgriTermLoanInstallmentOB();
        this.observable.addObserver(this);
        observable.resetInstallmentDetails();
        System.out.println("itssetobserv");
        cboroundOfType.addItem("Round off Rupee");
        cboroundOfType.addItem("Round off 50paise");
        cboroundOfType.addItem("Round off nearest 5 paise");
        cboroundOfType.addItem("Round of nearest paise");
        
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
        lblAcctNo_Disp.setName("lblAcctNo_Disp");
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
        panInstallmentLeft.setName("panInstallmentLeft");
        panInstallmentRight.setName("panInstallmentRight");
        panInstallmentTable.setName("panInstallmentTable");
        panInstallmentToolBtns.setName("panInstallmentToolBtns");
        panInstallment_MainPan.setName("panInstallment_MainPan");
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
        ((javax.swing.border.TitledBorder)panInstallmentFields.getBorder()).setTitle(resourceBundle.getString("panInstallmentFields"));
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
        lblAcctNo_Disp.setText(resourceBundle.getString("lblAcctNo_Disp"));
        lblTotal.setText(resourceBundle.getString("lblTotal"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnOk.setText(resourceBundle.getString("btnOk"));
        btnSubmit.setText(resourceBundle.getString("btnSubmit"));
        lblInstallmentDate.setText(resourceBundle.getString("lblInstallmentDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        txtInterestRate.setText(resourceBundle.getString("txtInterestRate"));
        lblFrequency.setText(resourceBundle.getString("lblFrequency"));
        lblroundOfType.setText(resourceBundle.getString("lblroundOfType"));
        lblRepaymentType.setText(resourceBundle.getString("lblRepaymentType"));
        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
    }
    
    public void update(java.util.Observable o, Object arg) {
        lblAcctNo_Disp.setText(observable.getLblAcctNo_Disp());
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
        observable.setLblAcctNo_Disp(lblAcctNo_Disp.getText());
        observable.setTxtLoanAmt(txtLoanAmt.getText());
        observable.setTdtInstallmentDate(tdtInstallmentDate.getDateValue());
        observable.setTxtPrincipalAmt(txtPrincipalAmt.getText());
        observable.setTxtInterest(txtInterest.getText());
        observable.setTxtTotal(txtTotal.getText());
        observable.setTxtBalance(txtBalance.getText());
        observable.setTxtNoOfInstall(txtNoOfInstall.getText());
        observable.setCboFrequency(CommonUtil.convertObjToStr(cboFrequency.getSelectedItem()));
        observable.setCboRepaymentType(CommonUtil.convertObjToStr(cboRepaymentType.getSelectedItem()));
        observable.setCboroundOfType(CommonUtil.convertObjToStr(cboroundOfType.getSelectedItem()));
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
        txtInterestRate.setMaxLength(4);
        txtInterestRate.setValidation(new NumericValidation(2, 2));
        txtLoanAmt.setMaxLength(16);
        txtLoanAmt.setValidation(new CurrencyValidation(14,2));
        txtNoOfInstall.setMaxLength(3);
        txtNoOfInstall.setValidation(new NumericValidation());
        txtPrincipalAmt.setMaxLength(16);
        txtPrincipalAmt.setValidation(new CurrencyValidation(14,2));
        txtInterest.setMaxLength(16);
        txtInterest.setValidation(new CurrencyValidation(14,2));
        txtTotal.setMaxLength(16);
        txtTotal.setValidation(new CurrencyValidation(14,2));
        txtBalance.setMaxLength(16);
        txtBalance.setValidation(new CurrencyValidation(14,2));
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panInstallment_MainPan = new com.see.truetransact.uicomponent.CPanel();
        panAcctInfo = new com.see.truetransact.uicomponent.CPanel();
        lblAcctNo_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        lblLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        lblInterestRate = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        txtInterestRate = new com.see.truetransact.uicomponent.CTextField();
        lblRepaymentType = new com.see.truetransact.uicomponent.CLabel();
        cboFrequency = new com.see.truetransact.uicomponent.CComboBox();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        lblNoOfInstall = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstall = new com.see.truetransact.uicomponent.CTextField();
        lblroundOfType = new com.see.truetransact.uicomponent.CLabel();
        cboroundOfType = new com.see.truetransact.uicomponent.CComboBox();
        lblFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboRepaymentType = new com.see.truetransact.uicomponent.CComboBox();
        panInstallmentTable = new com.see.truetransact.uicomponent.CPanel();
        srpInstallmentTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstallmentTable = new com.see.truetransact.uicomponent.CTable();
        panInstallmentFields = new com.see.truetransact.uicomponent.CPanel();
        panInstallmentToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        panInstallmentLeft = new com.see.truetransact.uicomponent.CPanel();
        lblInstallmentDate = new com.see.truetransact.uicomponent.CLabel();
        lblPrincipalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblInterest = new com.see.truetransact.uicomponent.CLabel();
        tdtInstallmentDate = new com.see.truetransact.uicomponent.CDateField();
        txtPrincipalAmt = new com.see.truetransact.uicomponent.CTextField();
        txtInterest = new com.see.truetransact.uicomponent.CTextField();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        txtTotal = new com.see.truetransact.uicomponent.CTextField();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        txtBalance = new com.see.truetransact.uicomponent.CTextField();
        panInstallmentRight = new com.see.truetransact.uicomponent.CPanel();
        panSubmitBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSubmit = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panInstallment_MainPan.setLayout(new java.awt.GridBagLayout());

        panInstallment_MainPan.setMaximumSize(new java.awt.Dimension(825, 575));
        panInstallment_MainPan.setMinimumSize(new java.awt.Dimension(825, 575));
        panInstallment_MainPan.setPreferredSize(new java.awt.Dimension(825, 575));
        panAcctInfo.setLayout(new java.awt.GridBagLayout());

        panAcctInfo.setMinimumSize(new java.awt.Dimension(750, 110));
        panAcctInfo.setPreferredSize(new java.awt.Dimension(750, 110));
        lblAcctNo_Disp.setText("LA00000000000201");
        lblAcctNo_Disp.setMaximumSize(new java.awt.Dimension(125, 21));
        lblAcctNo_Disp.setMinimumSize(new java.awt.Dimension(125, 21));
        lblAcctNo_Disp.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(lblAcctNo_Disp, gridBagConstraints);

        lblAcctNo.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panAcctInfo.add(lblAcctNo, gridBagConstraints);

        lblLoanAmt.setText("Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panAcctInfo.add(lblLoanAmt, gridBagConstraints);

        lblInterestRate.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panAcctInfo.add(lblInterestRate, gridBagConstraints);

        txtLoanAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLoanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(txtLoanAmt, gridBagConstraints);

        txtInterestRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(txtInterestRate, gridBagConstraints);

        lblRepaymentType.setText("RepaymentType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panAcctInfo.add(lblRepaymentType, gridBagConstraints);

        cboFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFrequencyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(cboFrequency, gridBagConstraints);

        btnOk.setText("Ok");
        btnOk.setMaximumSize(new java.awt.Dimension(59, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(59, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(59, 25));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 35, 4, 4);
        panAcctInfo.add(btnOk, gridBagConstraints);

        lblNoOfInstall.setText("No of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panAcctInfo.add(lblNoOfInstall, gridBagConstraints);

        txtNoOfInstall.setMaximumSize(new java.awt.Dimension(100, 21));
        txtNoOfInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(txtNoOfInstall, gridBagConstraints);

        lblroundOfType.setText("Round off Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panAcctInfo.add(lblroundOfType, gridBagConstraints);

        cboroundOfType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboroundOfType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboroundOfType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboroundOfTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(cboroundOfType, gridBagConstraints);

        lblFrequency.setText("Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panAcctInfo.add(lblFrequency, gridBagConstraints);

        cboRepaymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepaymentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo.add(cboRepaymentType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panInstallment_MainPan.add(panAcctInfo, gridBagConstraints);

        panInstallmentTable.setLayout(new java.awt.GridBagLayout());

        panInstallmentTable.setMinimumSize(new java.awt.Dimension(305, 375));
        panInstallmentTable.setPreferredSize(new java.awt.Dimension(305, 375));
        srpInstallmentTable.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        srpInstallmentTable.setMinimumSize(new java.awt.Dimension(305, 375));
        srpInstallmentTable.setPreferredSize(new java.awt.Dimension(305, 375));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInstallmentTable.add(srpInstallmentTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallment_MainPan.add(panInstallmentTable, gridBagConstraints);

        panInstallmentFields.setLayout(new java.awt.GridBagLayout());

        panInstallmentFields.setBorder(new javax.swing.border.TitledBorder("Installment Details"));
        panInstallmentFields.setMaximumSize(new java.awt.Dimension(210, 375));
        panInstallmentFields.setMinimumSize(new java.awt.Dimension(210, 375));
        panInstallmentFields.setPreferredSize(new java.awt.Dimension(210, 375));
        panInstallmentToolBtns.setLayout(new java.awt.GridBagLayout());

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentToolBtns.add(btnNew, gridBagConstraints);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentToolBtns.add(btnSave, gridBagConstraints);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentToolBtns.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panInstallmentFields.add(panInstallmentToolBtns, gridBagConstraints);

        panInstallmentLeft.setLayout(new java.awt.GridBagLayout());

        panInstallmentLeft.setMaximumSize(new java.awt.Dimension(200, 150));
        panInstallmentLeft.setMinimumSize(new java.awt.Dimension(200, 150));
        panInstallmentLeft.setPreferredSize(new java.awt.Dimension(200, 150));
        lblInstallmentDate.setText("Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInstallmentLeft.add(lblInstallmentDate, gridBagConstraints);

        lblPrincipalAmt.setText("Principal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(lblPrincipalAmt, gridBagConstraints);

        lblInterest.setText("Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(lblInterest, gridBagConstraints);

        tdtInstallmentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstallmentDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInstallmentLeft.add(tdtInstallmentDate, gridBagConstraints);

        txtPrincipalAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPrincipalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrincipalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrincipalAmtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(txtPrincipalAmt, gridBagConstraints);

        txtInterest.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(txtInterest, gridBagConstraints);

        lblTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(lblTotal, gridBagConstraints);

        txtTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(txtTotal, gridBagConstraints);

        lblBalance.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(lblBalance, gridBagConstraints);

        txtBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentLeft.add(txtBalance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInstallmentFields.add(panInstallmentLeft, gridBagConstraints);

        panInstallmentRight.setLayout(new java.awt.GridBagLayout());

        panInstallmentRight.setMaximumSize(new java.awt.Dimension(0, 0));
        panInstallmentRight.setMinimumSize(new java.awt.Dimension(0, 0));
        panInstallmentRight.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInstallmentFields.add(panInstallmentRight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallment_MainPan.add(panInstallmentFields, gridBagConstraints);

        panSubmitBtn.setLayout(new java.awt.GridBagLayout());

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubmitBtn.add(btnSubmit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 4, 4);
        panInstallment_MainPan.add(panSubmitBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panInstallment_MainPan, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
//        if (btnSubmit.isEnabled())
//            btnSubmitActionPerformed();
//        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
//        if (btnSubmit.isEnabled()) {
//            displayAlert("Click Submit Button");
//            return;
//        }
    }//GEN-LAST:event_formWindowClosing
    
    private void cboroundOfTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboroundOfTypeActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboroundOfTypeActionPerformed
    
    private void cboFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFrequencyActionPerformed
        // TODO add your handling code here:
        cboFrequencyActionPerformed();
    }//GEN-LAST:event_cboFrequencyActionPerformed
    private void cboFrequencyActionPerformed() {
        // TODO add your handling code here:
        if (this.data == null && (cboFrequency.getSelectedItem().equals("User Defined") || cboFrequency.getSelectedItem().equals("Lump Sum"))){
            btnOk.setEnabled(false);
        }else{
            btnOk.setEnabled(true);
        }
    }
    private void txtInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestFocusLost
        // TODO add your handling code here:
        txtInterestFocusLost();
    }//GEN-LAST:event_txtInterestFocusLost
    private void txtInterestFocusLost() {
        txtPrincipalAmtFocusLost();
    }
    private void txtPrincipalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrincipalAmtFocusLost
        // TODO add your handling code here:
        txtPrincipalAmtFocusLost();
    }//GEN-LAST:event_txtPrincipalAmtFocusLost
    private void txtPrincipalAmtFocusLost() {
        if (txtPrincipalAmt.getText().length() != 0 && txtInterest.getText().length() != 0){
            double principalAmt = CommonUtil.convertObjToDouble(txtPrincipalAmt.getText()).doubleValue();
            double interestAmt = CommonUtil.convertObjToDouble(txtInterest.getText()).doubleValue();
            txtTotal.setText(CommonUtil.convertObjToStr(new Double((principalAmt + interestAmt))));
            observable.setTxtTotal(txtTotal.getText());
        }
    }
    private void tdtInstallmentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstallmentDateFocusLost
        // TODO add your handling code here:
        tdtInstallmentDateFocusLost();
    }//GEN-LAST:event_tdtInstallmentDateFocusLost
    private void tdtInstallmentDateFocusLost() {
        ClientUtil.validateFromDate(tdtInstallmentDate, CommonUtil.convertObjToStr(data.get("TO_DATE")));
        ClientUtil.validateToDate(tdtInstallmentDate, CommonUtil.convertObjToStr(data.get("FROM_DATE")));
    }
    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        btnSubmitActionPerformed();
    }//GEN-LAST:event_btnSubmitActionPerformed
    private void btnSubmitActionPerformed() {
        System.out.print("##########"+data);
        if (data !=null&& data.containsKey("REPAYMENT_TYPE") && data.get("REPAYMENT_TYPE").equals("USER_DEFINED")){
            observable.validateLoanLimitAmount();
        }
        if (observable.checkLoanLimit()){
            //            observable.storeEmiAmount();
            this.dispose();
            if (parent != null) ((AgriTermLoanUI) parent).repaymentFillData(observable.getInstallmentAll(), observable.getTotalRepayAmount());
        }else{
            displayAlert(resourceBundle.getString("limitWarning"));
        }
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
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
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInstallmentLeft);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            btnSaveActionPerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
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
    private void tblInstallmentTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstallmentTableMousePressed
        // TODO add your handling code here:
        if (isUserDefinedPanelVisible){
            tblInstallmentTableMousePressed();
        }
    }//GEN-LAST:event_tblInstallmentTableMousePressed
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
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAcctInfo);
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            btnOkActionPerformed();
        }
    }//GEN-LAST:event_btnOkActionPerformed
    private void btnOkActionPerformed() {
        HashMap data = new HashMap();
        data.put("NO_INSTALL", txtNoOfInstall.getText());// No. of installments
        data.put("PRINCIPAL_AMOUNT", txtLoanAmt.getText());
        data.put("INTEREST", txtInterestRate.getText());// Interest Rate
        data.put("REPAYMENT_FREQUENCY", observable.getCbmFrequency().getKeyForSelected());
        String roundingType=CommonUtil.convertObjToStr(observable.getCbmroundOfType().getKeyForSelected());
        System.out.println("rounding type####"+roundingType);
        if(roundingType!=null && roundingType.length()>0)
            if(! roundingType.equals("NO_ROUND_OFF"))
            data.put("ROUNDING_TYPE",roundingType);
        //       int a=cboRepaymentType.getSelectedIndex();
        //        switch(a)
        //        {
        //            case 1:
        //            {
        //                data.put("REPAYMENT_TYPE","EMI");
        //                break;
        //            }
        //            case 2:
        //            {
        //
        //            }
        //            case 3:
        //            {
        //
        //            }
        //            case 4:
        //            {
        //                data.put("REPAYMENT_TYPE","UNIFORM_PRINCIPLE_EMI");
        //                break;
        //
        //            }
        //                   default :
        //                       data.put("EMI","EMI");
        //
        //        }
        
    /*    if(rdoUniformPrincipalEMI.isSelected())
     
     
        {
            data.put("EMI_TYPE","UNIFORM_PRINCIPLE_EMI");
        }
        else
        {
            data.put("EMI_TYPE","UNIFORM_EMI");  //its only for radio button
        }
        int roundType=cboroundOfType.getSelectedIndex();*/
        
    /*  switch(roundType)
        {
            case 1:
            {
                data.put("ROUNDING_FACTOR", "1_RUPEE");
                break;
            }
            case 2:
            {
                data.put("ROUNDING_FACTOR","50_PAISE");
                break;
            }
            case 3:
            {
                data.put("ROUNDING_FACTOR","5_PAISE");
                break;
            }
            default : data.put("ROUNDING_FACTOR", "1_RUPEE");
        }*/
        data.put("ROUNDING_FACTOR", "1_RUPEE");
        data.put("COMPOUNDING_PERIOD", observable.getCbmFrequency().getKeyForSelected());
        data.put("COMPOUNDING_TYPE", "REPAYMENT");
        String s = (String)observable.getCbmRepaymentType().getKeyForSelected();
        if (observable.getCbmFrequency().getKeyForSelected().equals("30")){
            if(s.equals("EMI"))
                data.put("REPAYMENT_TYPE", s);
            else
                data.put("REPAYMENT_TYPE", s);
        }else if (observable.getCbmFrequency().getKeyForSelected().equals("90")){
            data.put("REPAYMENT_TYPE", "EQI");
        }else if (observable.getCbmFrequency().getKeyForSelected().equals("180")){
            data.put("REPAYMENT_TYPE", "EHI");
        }else if (observable.getCbmFrequency().getKeyForSelected().equals("365")){
            data.put("REPAYMENT_TYPE", "EYI");
        }
        //        System.out.println("#### REPAYMENT_TYPE : "+observable.getCbmRepaymentType().getKeyForSelected());
        data.put("ISDURATION_DDMMYY", "YES");
        data.put("INTEREST_TYPE", "COMPOUND");
        data.put("DURATION_YY", txtNoOfInstall.getText());
        populateData(data);
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        observable.destroyObjects();
        observable = null;
        this.dispose();
    }//GEN-LAST:event_exitForm
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
        cboroundOfType.setEnabled(val);
        txtLoanAmt.setEnabled(val);
        txtNoOfInstall.setEnabled(val);
        txtInterestRate.setEnabled(val);
        btnOk.setEnabled(val);
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
        new AgriTermLoanInstallmentUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private javax.swing.JButton btnSubmit;
    private com.see.truetransact.uicomponent.CComboBox cboFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentType;
    private com.see.truetransact.uicomponent.CComboBox cboroundOfType;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Disp;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentDate;
    private com.see.truetransact.uicomponent.CLabel lblInterest;
    private com.see.truetransact.uicomponent.CLabel lblInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstall;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalAmt;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentType;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblroundOfType;
    private com.see.truetransact.uicomponent.CPanel panAcctInfo;
    private com.see.truetransact.uicomponent.CPanel panInstallmentFields;
    private com.see.truetransact.uicomponent.CPanel panInstallmentLeft;
    private com.see.truetransact.uicomponent.CPanel panInstallmentRight;
    private com.see.truetransact.uicomponent.CPanel panInstallmentTable;
    private com.see.truetransact.uicomponent.CPanel panInstallmentToolBtns;
    private com.see.truetransact.uicomponent.CPanel panInstallment_MainPan;
    private com.see.truetransact.uicomponent.CPanel panSubmitBtn;
    private com.see.truetransact.uicomponent.CScrollPane srpInstallmentTable;
    private com.see.truetransact.uicomponent.CTable tblInstallmentTable;
    private com.see.truetransact.uicomponent.CDateField tdtInstallmentDate;
    private com.see.truetransact.uicomponent.CTextField txtBalance;
    private com.see.truetransact.uicomponent.CTextField txtInterest;
    private com.see.truetransact.uicomponent.CTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstall;
    private com.see.truetransact.uicomponent.CTextField txtPrincipalAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotal;
    // End of variables declaration//GEN-END:variables
    
}
