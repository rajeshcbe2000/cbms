/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * KCCRenewalUI.java
 *
 * Created on June 21, 2018, 1:46 PM
  *created by  nithya
 */
package com.see.truetransact.ui.termloan.kcc;

import com.see.truetransact.ui.termloan.repayment.*;
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
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.termloan.TermLoanUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentRB;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.awt.GridBagConstraints;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import javax.swing.*;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 * @author Nithya
 */
public class KCCRenewalUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {

    HashMap mandatoryMap;
    HashMap data;
    HashMap paramMap = null;
    CInternalFrame parentFrame = null;
    final TermLoanInstallmentRB resourceBundle = new TermLoanInstallmentRB();
    KCCRenewalOB observable;      
    private Date curr_dt = null;
    String[] strMandatory = {"INTEREST_TYPE", "NO_INSTALL", "DURATION_YY", "REPAYMENT_TYPE",
    "PRINCIPAL_AMOUNT", "COMPOUNDING_PERIOD", "REPAYMENT_FREQUENCY", "INTEREST"};
    String[] strUserDefMandatory = {"FROM_DATE", "TO_DATE"};        
    DecimalFormat df = new DecimalFormat("0.##");
    private boolean updateMode = false;
    int updateTab = -1;
    private String viewType = "";
    private int rowCount=0;
    private String docGenIdValue = "";
    int salarytblSelectedRow = -1;
    private String prodDesc = "";
    private boolean tableFlag = false;
    private JTable table = null;
    private TransactionUI transactionUI = new TransactionUI();
    private List chargelst = null;
    Rounding rd = new Rounding();
    HashMap chargeMap = new HashMap();     // Added by nithya on 30-12-2019 for KD-1131
    double totChargeAmount = 0.0;
    HashMap serviceTax_Map=new HashMap();
    ServiceTaxCalculation objServiceTax;
    
    /**
     * Creates new form AuthorizeUI
     */
    public KCCRenewalUI() {       
       
        setupInit();
        setupScreen();  
        initRunSecurityComponents();
        setMaxLength();
        tblMemberType.setModel(observable.getTblMemberTypeDetails());
        tblCollateral.setModel(observable.getTblCollateralDetails());
        tblJointCollateral.setModel(observable.getTblJointCollateral());
        cboNature.setModel(observable.getCbmNature());
        cboCity.setModel(observable.getCbmSecurityCity());
        cboRight.setModel(observable.getCbmRight());
        cboPledge.setModel(observable.getCbmPledge());
        cboDocumentType.setModel(observable.getCbmDocumentType());
        cboProductTypeSecurity.setModel(observable.getCbmProdTypeSecurity());
        cboDepProdType.setModel(observable.getCbmDepProdID());        
        cTabbedPane1.remove(panRiskFundDetails);
        chkRenew.setEnabled(false);
        chkRiskFund.setEnabled(false);
        btnOK.setEnabled(false);
        btnClear.setEnabled(false);
        transactionUI.setSourceScreen("KCC_RENEWAL");
        rdoGoldSecurityExitsNo.setSelected(true); // Added by nithya on 07-03-2020 for KD-1379
        txtGoldSecurityId.setEnabled(false);
        btnGoldSecurityIdSearch.setEnabled(false);
    } 

    /**
     * Creates new form AuthorizeUI
     */
    public KCCRenewalUI(CInternalFrame parent, HashMap paramMap) {
        this.parentFrame = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        transactionUI.setSourceScreen("KCC_RENEWAL");
    }

    private void setupInit() {
        initComponents();
        setObservable(); 
        curr_dt = ClientUtil.getCurrentDate();
        tdtFromDt.setEnabled(false);
        tdtTodt.setEnabled(false);
        txtNoOfYears.setEnabled(false);
        txtLimitAmount.setEnabled(false);        
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
    
    private void setObservable(){      
        observable = KCCRenewalOB.getInstance();
        observable.addObserver(this);
        observable.resetInstallmentDetails();              
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() { 
        lblNoOfInstall.setName("lblNoOfInstall");  
        lblAcctNo.setName("lblAcctNo");  
        lblInterestRate.setName("lblInterestRate");
        txtNoOfYears.setName("txtInterestRate");      
        lblLoanAmt.setName("lblLoanAmt");     
        txtLimitAmount.setName("txtLoanAmt");            
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {       
        lblLoanAmt.setText(resourceBundle.getString("lblLoanAmt"));       
        lblNoOfInstall.setText(resourceBundle.getString("lblNoOfInstall"));       
        lblInterestRate.setText(resourceBundle.getString("lblInterestRate"));   
        txtNoOfYears.setText(resourceBundle.getString("txtInterestRate"));
        lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
    }
    
    public void update(java.util.Observable o, Object arg) {      
        txtNoOfYears.setText(observable.getTxtInterestRate());
        txtLimitAmount.setText(observable.getTxtLimitAmount());
        tdtFromDt.setDateValue(observable.getTdtFromDt());
        tdtTodt.setDateValue(observable.getTdtToDt());
        txtBorrowerNo.setText(observable.getTxtBorrowerNo());
        txtAcctNo.setText(observable.getTxtAcctNo());
        
        txtJewelleryDetails.setText(observable.getTxtJewelleryDetails());
        txtGoldRemarks.setText(observable.getTxtGoldRemarks());
        txtNetWeight.setText(observable.getTxtNetWeight());
        txtValueOfGold.setText(observable.getTxtValueOfGold());
        txtGrossWeight.setText(observable.getTxtGrossWeight());
        
        if(observable.getChkRenew().equalsIgnoreCase("Y")){
            chkRenew.setSelected(true);
        }else{
            chkRenew.setSelected(false);
        }
        if(observable.getRdoGoldSecurityStockExists().equalsIgnoreCase("Y")){ // Added by nithya on 07-03-2020 for KD-1379
            rdoGoldSecurityExitsYes.setSelected(true);
            rdoGoldSecurityExitsNo.setSelected(false);
            rdoGoldSecurityExitsYesActionPerformed(null);
        }else{
            rdoGoldSecurityExitsYes.setSelected(false);
        }
        txtGoldSecurityId.setText(observable.getTxtGoldSecurityId());
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(this.getScreen());
        observable.setTxtNoOfYears(txtNoOfYears.getText());
        observable.setTxtAcctNo(txtAcctNo.getText());
        observable.setTxtLimitAmount(txtLimitAmount.getText());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTdtToDt(tdtTodt.getDateValue());
        observable.setTxtBorrowerNo(txtBorrowerNo.getText());
        
        observable.setTxtJewelleryDetails(txtJewelleryDetails.getText());
        observable.setTxtGrossWeight(txtGrossWeight.getText());
        observable.setTxtGoldRemarks(txtGoldRemarks.getText());
        observable.setTxtValueOfGold(txtValueOfGold.getText());
        observable.setTxtNetWeight(txtNetWeight.getText()); 
        
        if(chkRenew.isSelected()){
            observable.setChkRenew("Y");
        }else{
            observable.setChkRenew("N");
        }
        observable.setServiceTax_Map(serviceTax_Map); // Added by nithya on 30-12-2019 for KD-1131
        if(rdoGoldSecurityExitsYes.isSelected()){  // Added by nithya on 07-03-2020 for KD-1379
            observable.setRdoGoldSecurityStockExists("Y");
        }else{
            observable.setRdoGoldSecurityStockExists("N"); 
        }if(rdoGoldSecurityExitsNo.isSelected()){
           observable.setRdoGoldSecurityStockExists("N"); 
        }        
        observable.setTxtGoldSecurityId(txtGoldSecurityId.getText());
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

    }
    private void setMaxLength() {
        txtContactNo.setAllowAll(true);
        txtMemNetworth.setAllowAll(true);
        txtMemPriority.setAllowAll(true);
        txtPledgeNo.setAllowAll(true);
        txtPledgeAmount.setValidation(new CurrencyValidation(14, 2));
        txtVillage.setAllowAll(true);
        txtSurveyNo.setAllowAll(true);
        txtTotalArea.setAllowAll(true);
        txtSalaryCertificateNo.setAllowAll(true);
        txtEmployerName.setAllowAll(true);
        txtAddress.setAllowAll(true);
        txtPinCode.setAllowAll(true);
        txtDesignation.setAllowAll(true);
        txtContactNo.setAllowAll(true);
        txtMemberNum.setAllowAll(true);
        txtTotalSalary.setAllowAll(true);
        txtNetWorth1.setAllowAll(true);
        txtSalaryRemark.setAllowAll(true);
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
        panOuter = new com.see.truetransact.uicomponent.CPanel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        panBtns = new com.see.truetransact.uicomponent.CPanel();
        btnOK = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        chkRenew = new com.see.truetransact.uicomponent.CCheckBox();
        chkRiskFund = new com.see.truetransact.uicomponent.CCheckBox();
        panRenew1 = new com.see.truetransact.uicomponent.CPanel();
        lblNoOfInstall = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblInterestRate = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        panRenew2 = new com.see.truetransact.uicomponent.CPanel();
        lblLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtLimitAmount = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        tdtTodt = new com.see.truetransact.uicomponent.CDateField();
        panRenew3 = new com.see.truetransact.uicomponent.CPanel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnSearchAcctNo = new com.see.truetransact.uicomponent.CButton();
        txtBorrowerNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panRiskFundDetails = new com.see.truetransact.uicomponent.CPanel();
        panRiskFund = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();

        lblSpace5.setText("     ");

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("KCC Renewal");
        setMaximumSize(new java.awt.Dimension(900, 725));
        setMinimumSize(new java.awt.Dimension(900, 725));
        setPreferredSize(new java.awt.Dimension(900, 725));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panOuter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panOuter.setMaximumSize(new java.awt.Dimension(800, 725));
        panOuter.setMinimumSize(new java.awt.Dimension(800, 725));
        panOuter.setName(""); // NOI18N
        panOuter.setPreferredSize(new java.awt.Dimension(800, 725));
        panOuter.setLayout(new java.awt.GridBagLayout());

        cPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info"));
        cPanel3.setMinimumSize(new java.awt.Dimension(600, 130));
        cPanel3.setPreferredSize(new java.awt.Dimension(600, 130));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        panBtns.setMaximumSize(new java.awt.Dimension(400, 35));
        panBtns.setMinimumSize(new java.awt.Dimension(400, 35));
        panBtns.setName(""); // NOI18N
        panBtns.setPreferredSize(new java.awt.Dimension(400, 35));
        panBtns.setLayout(new java.awt.GridBagLayout());

        btnOK.setText("Ok");
        btnOK.setMaximumSize(new java.awt.Dimension(45, 27));
        btnOK.setMinimumSize(new java.awt.Dimension(45, 27));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtns.add(btnOK, gridBagConstraints);

        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(60, 27));
        btnClear.setMinimumSize(new java.awt.Dimension(60, 27));
        btnClear.setPreferredSize(new java.awt.Dimension(49, 27));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtns.add(btnClear, gridBagConstraints);

        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(60, 307));
        btnClose.setMinimumSize(new java.awt.Dimension(60, 30));
        btnClose.setPreferredSize(new java.awt.Dimension(49, 30));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtns.add(btnClose, gridBagConstraints);

        chkRenew.setText("Renew");
        chkRenew.setPreferredSize(new java.awt.Dimension(65, 30));
        chkRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtns.add(chkRenew, gridBagConstraints);

        chkRiskFund.setText("RiskFund");
        chkRiskFund.setMaximumSize(new java.awt.Dimension(80, 30));
        chkRiskFund.setMinimumSize(new java.awt.Dimension(80, 30));
        chkRiskFund.setPreferredSize(new java.awt.Dimension(80, 30));
        chkRiskFund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRiskFundActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtns.add(chkRiskFund, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(panBtns, gridBagConstraints);

        panRenew1.setMaximumSize(new java.awt.Dimension(300, 30));
        panRenew1.setMinimumSize(new java.awt.Dimension(350, 30));
        panRenew1.setName(""); // NOI18N
        panRenew1.setPreferredSize(new java.awt.Dimension(350, 30));
        panRenew1.setLayout(new java.awt.GridBagLayout());

        lblNoOfInstall.setText("Renew From Dt");
        lblNoOfInstall.setMaximumSize(new java.awt.Dimension(90, 21));
        lblNoOfInstall.setMinimumSize(new java.awt.Dimension(90, 21));
        lblNoOfInstall.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew1.add(lblNoOfInstall, gridBagConstraints);

        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew1.add(tdtFromDt, gridBagConstraints);

        lblInterestRate.setText("No Of Years");
        lblInterestRate.setMaximumSize(new java.awt.Dimension(67, 21));
        lblInterestRate.setMinimumSize(new java.awt.Dimension(67, 21));
        lblInterestRate.setPreferredSize(new java.awt.Dimension(67, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew1.add(lblInterestRate, gridBagConstraints);

        txtNoOfYears.setAllowAll(true);
        txtNoOfYears.setAllowNumber(true);
        txtNoOfYears.setMinimumSize(new java.awt.Dimension(6, 21));
        txtNoOfYears.setPreferredSize(new java.awt.Dimension(6, 21));
        txtNoOfYears.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfYearsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew1.add(txtNoOfYears, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(panRenew1, gridBagConstraints);

        panRenew2.setMaximumSize(new java.awt.Dimension(300, 30));
        panRenew2.setMinimumSize(new java.awt.Dimension(300, 30));
        panRenew2.setName(""); // NOI18N
        panRenew2.setPreferredSize(new java.awt.Dimension(350, 30));
        panRenew2.setLayout(new java.awt.GridBagLayout());

        lblLoanAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanAmt.setText("Limit Amount");
        lblLoanAmt.setMaximumSize(new java.awt.Dimension(90, 21));
        lblLoanAmt.setMinimumSize(new java.awt.Dimension(90, 21));
        lblLoanAmt.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        panRenew2.add(lblLoanAmt, gridBagConstraints);

        txtLimitAmount.setAllowAll(true);
        txtLimitAmount.setAllowNumber(true);
        txtLimitAmount.setMaximumSize(new java.awt.Dimension(101, 21));
        txtLimitAmount.setMinimumSize(new java.awt.Dimension(101, 21));
        txtLimitAmount.setPreferredSize(new java.awt.Dimension(101, 21));
        txtLimitAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimitAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 5);
        panRenew2.add(txtLimitAmount, gridBagConstraints);

        cLabel1.setText("To Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        panRenew2.add(cLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        panRenew2.add(tdtTodt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(panRenew2, gridBagConstraints);

        panRenew3.setMaximumSize(new java.awt.Dimension(270, 90));
        panRenew3.setMinimumSize(new java.awt.Dimension(270, 90));
        panRenew3.setName(""); // NOI18N
        panRenew3.setPreferredSize(new java.awt.Dimension(270, 90));
        panRenew3.setLayout(new java.awt.GridBagLayout());

        panAcctNo.setMaximumSize(new java.awt.Dimension(160, 30));
        panAcctNo.setMinimumSize(new java.awt.Dimension(160, 30));
        panAcctNo.setPreferredSize(new java.awt.Dimension(160, 30));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAcctNo.setAllowAll(true);
        txtAcctNo.setAllowNumber(true);
        txtAcctNo.setPreferredSize(new java.awt.Dimension(36, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 114;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcctNo.add(txtAcctNo, gridBagConstraints);

        btnSearchAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSearchAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAcctNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcctNo.add(btnSearchAcctNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew3.add(panAcctNo, gridBagConstraints);

        txtBorrowerNo.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        panRenew3.add(txtBorrowerNo, gridBagConstraints);

        lblCustName.setForeground(new java.awt.Color(0, 0, 153));
        lblCustName.setText("Customer Name");
        lblCustName.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 134;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew3.add(lblCustName, gridBagConstraints);

        lblAcctNo.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew3.add(lblAcctNo, gridBagConstraints);

        cLabel2.setText("Borrower No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenew3.add(cLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(panRenew3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 171;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOuter.add(cPanel3, gridBagConstraints);

        cTabbedPane1.setMaximumSize(new java.awt.Dimension(790, 550));
        cTabbedPane1.setMinimumSize(new java.awt.Dimension(790, 550));
        cTabbedPane1.setPreferredSize(new java.awt.Dimension(790, 550));

        panSecurityDetails.setPreferredSize(new java.awt.Dimension(849, 530));
        panSecurityDetails.setLayout(new java.awt.GridBagLayout());
        cTabbedPane1.addTab("Surety Details", panSecurityDetails);

        panRiskFundDetails.setPreferredSize(new java.awt.Dimension(849, 530));
        panRiskFundDetails.setLayout(new java.awt.GridBagLayout());

        panRiskFund.setBorder(javax.swing.BorderFactory.createTitledBorder("RiskFund"));
        panRiskFund.setMaximumSize(new java.awt.Dimension(750, 130));
        panRiskFund.setMinimumSize(new java.awt.Dimension(750, 130));
        panRiskFund.setName(""); // NOI18N
        panRiskFund.setPreferredSize(new java.awt.Dimension(750, 130));
        panRiskFund.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRiskFundDetails.add(panRiskFund, gridBagConstraints);

        panTransaction.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panTransaction.setMaximumSize(new java.awt.Dimension(750, 250));
        panTransaction.setMinimumSize(new java.awt.Dimension(750, 250));
        panTransaction.setName(""); // NOI18N
        panTransaction.setPreferredSize(new java.awt.Dimension(750, 250));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRiskFundDetails.add(panTransaction, gridBagConstraints);
        panTransaction.getAccessibleContext().setAccessibleName("Transaction");

        cPanel4.setMinimumSize(new java.awt.Dimension(750, 30));
        cPanel4.setPreferredSize(new java.awt.Dimension(750, 30));
        cPanel4.setLayout(new java.awt.GridBagLayout());

        cLabel3.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        cPanel4.add(cLabel3, gridBagConstraints);

        lblServiceTaxval.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblServiceTaxval.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(100, 21));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(100, 21));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        cPanel4.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        panRiskFundDetails.add(cPanel4, gridBagConstraints);

        cTabbedPane1.addTab("Risk Fund Details", panRiskFundDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOuter.add(cTabbedPane1, gridBagConstraints);
        cTabbedPane1.getAccessibleContext().setAccessibleName("Account Info");
        cTabbedPane1.getAccessibleContext().setAccessibleDescription("Account Info");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(panOuter, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed() {
        updateOBFields();
        observable.resetInstallmentDetails();
        setUserDefinedFieldsEnableDisable(true);       
        observable.ttNotifyObservers();     
    }
    
   private void btnDepositNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setDepositTypeData(true);
        enableDisableDepositPanButton(false);
        btnDepositSave.setEnabled(true);
        ClientUtil.enableDisable(panDepositType, false);
        btnDepNo.setEnabled(true);
        cboProductTypeSecurity.setEnabled(true);
        cboDepProdType.setEnabled(true);
    }
   
   private void btnDepositSaveActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            HashMap whereMap = new HashMap();
            String prodtype = CommonUtil.convertObjToStr(cboProductTypeSecurity.getSelectedItem());
            whereMap.put("DEPOSIT_NO", txtDepNo.getText());
            List recordList = null;
            List depList = null;
            double depAmount = 0.0;
            if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                depList = ClientUtil.executeQuery("getAvailableBalForDep", whereMap);
                HashMap hmap = (HashMap) depList.get(0);
                depAmount = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
            } else {
                recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinLoansDeposit", whereMap);
            }
            if (((recordList != null && recordList.size() > 0) || (depList != null && depList.size() > 0 && depAmount <= 0.0)) && !updateMode) {
                ClientUtil.showMessageWindow("This Deposit has Already been Used as Security !!!");
            } else {
                updateDepositTypeFields();
                observable.addDepositTypeTable(updateTab, updateMode);
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                observable.resetDepositTypeDetails();
            }
            resetDepositTypeDetails();
            ClientUtil.enableDisable(panDepositType, false);
            enableDisableDepositPanButton(false);
            btnDepositNew.setEnabled(true);
            btnDepNo.setEnabled(false);
            calculateTot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public void btnDepNoActionPerformed(java.awt.event.ActionEvent evt) {
        viewType = "DEPOSIT_ACC_NO";
        popUp("DEPOSIT_ACC_NO");
    }
   
   private void cboProductTypeSecurityActionPerformed(java.awt.event.ActionEvent evt) {
        if (cboProductTypeSecurity.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            observable.setCbmProdTypeSecurity(prodType);
            cboDepProdType.setModel(observable.getCbmDepProdID());
            if (prodType.equals("TD")) {
                lblProductId2.setText("Product Id");
                lblDepNo.setText("Deposit No");
                lblDepDt.setText("Dep Date");
                lblDepAmount.setText("Dep Amount");
                lblMaturityValue.setText("Maturity Value");
                lblMaturityDt.setText("Maturity Date");
                txtRateOfInterest.setVisible(true);
                lblRateOfInterest.setVisible(true);
            } else {
                lblProductId2.setText("Scheme Name");
                lblDepNo.setText("Chittal No");
                lblDepDt.setText("Scheme StartDt");
                lblDepAmount.setText("Inst Amount");
                lblMaturityValue.setText("Paid Amount");
                lblMaturityDt.setText("Scheme EndDt");
                txtRateOfInterest.setText("");
                txtRateOfInterest.setVisible(false);
                lblRateOfInterest.setVisible(false);
            }
        }

    }
   
   public void calculateTot() {
        double totDeposit = 0;
        for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
            totDeposit = totDeposit + CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 2).toString()).doubleValue();
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf(totDeposit)));
        }
        setSizeTableData();
    }

    private void setSizeTableData() {
        tblDepositDetails.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDepositDetails.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblDepositDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDepositDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
    }
   
    private void resetDepositTypeDetails() {
        txtDepNo.setText("");
        cboProductTypeSecurity.setSelectedItem("");
        cboDepProdType.setSelectedItem("");
        tdtDepDt.setDateValue("");
        txtDepAmount.setText("");
        txtRateOfInterest.setText("");
        txtMaturityValue.setText("");
        txtMaturityDt.setDateValue("");
    }
   
   private void btnDepositDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String s = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.deleteDepositTableData(s, tblDepositDetails.getSelectedRow());
        observable.resetDepositTypeDetails();
        resetDepositTypeDetails();
        ClientUtil.enableDisable(panDepositType, false);
        enableDisableDepositPanButton(false);
        btnDepositNew.setEnabled(true);
        btnDepNo.setEnabled(false);
        calculateTot();
        if (tblDepositDetails.getRowCount() == 0) {
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf("0")));
        }
    }
    
    private void btnSearchAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAcctNoActionPerformed
        // TODO add your handling code here:
        viewType = "AccountSearch";
        popUp("AccountSearch");        
    }//GEN-LAST:event_btnSearchAcctNoActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        double memNetWorth = 0.0;
        double pledgeAmount = 0.0;
        double depAmount = 0.0;
        double netWorth = 0.0;
        Date dd = DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue());
        long datediff =  DateUtil.dateDiff(dd, curr_dt);
        //System.out.println("datediff txtNoOfYearsFocusLost :: " + datediff);        
        double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText()).doubleValue();
        HashMap hashmap = new HashMap();
        for (int j = 0; j < tblMemberType.getRowCount(); j++) {
                memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
        }
        for (int k = 0; k < tblCollateral.getRowCount(); k++) {
            pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
            String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(k, 0));
            hashmap.put("CUST_ID", custid);
            hashmap.put("MEMBER_NO", custid);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }
        for (int i = 0; i < tblSalary.getRowCount(); i++) {
            netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalary.getValueAt(i, 5)).doubleValue();
            String custid = CommonUtil.convertObjToStr(tblSalary.getValueAt(i, 2));
            hashmap.put("CUST_ID", custid);
            hashmap.put("MEMBER_NO", custid);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }

        for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
                String securityAmt = txtLimitAmount.getText();                
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                List lst = null;
                List EditLst = null;
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {                  
                    lst = ClientUtil.executeQuery("getAvailableBalForDepEditMode", hmap);
                    EditLst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);                    
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    if (EditLst != null && EditLst.size() > 0) {
                        hmap = (HashMap) EditLst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount += CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                }
            }
        double tot = memNetWorth + valuOfGold + pledgeAmount + depAmount + netWorth ;        
        double sanctionAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue();
        if (chkRenew.isSelected() && CommonUtil.convertObjToStr(txtNoOfYears.getText()).length() <= 0) {
            ClientUtil.displayAlert("Enter the number of years for Renewal");
            return;
        } else if (tot < sanctionAmt) {
            ClientUtil.displayAlert("Security Amount is lesser than the Sanctioned amount");
            return;
        } else {
            int yesNo = 0;
            String[] voucherOptions = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to Renew Account / Edit Suretys?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, voucherOptions, voucherOptions[0]);
            if (yesNo == 0) {
                if (chkRenew.isSelected()) {
                    if (datediff < 0) {
                        int yesNoDt = 0;
                        String[] yesNoDtOptions = {"Yes", "No"};
                        yesNoDt = COptionPane.showOptionDialog(null, "Future Date renewal.. Transaction Should no be allowed till renewal date :" + tdtFromDt.getDateValue() + "\n Do you want to Renew Account in future date ?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, yesNoDtOptions, yesNoDtOptions[0]);
                        if (yesNoDt == 0) {
                           saveAction(); 
                        }else{
                            return;
                        }
                    }else{
                       saveAction(); 
                    }
                } else {
                    saveAction();
                }
            } else {
                return;
            }            
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void txtNoOfYearsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfYearsFocusLost
        // TODO add your handling code here:
        int noOfYears = 0;
        if(CommonUtil.convertObjToStr(txtNoOfYears.getText()).length() > 0){
            noOfYears = CommonUtil.convertObjToInt(txtNoOfYears.getText());
        }else{
            noOfYears = 1;
        }
        calculateMaturityDate(noOfYears);        
    }//GEN-LAST:event_txtNoOfYearsFocusLost

    private void txtLimitAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimitAmountFocusLost
        // TODO add your handling code here:
        Double enteredLimit = CommonUtil.convertObjToDouble(txtLimitAmount.getText());
        double oldLimit = CommonUtil.convertObjToDouble(observable.getTxtLimitAmount());
        if(enteredLimit < oldLimit){
            ClientUtil.showMessageWindow("New Limit Should Not be Less than Old Limit "+ oldLimit);
            txtLimitAmount.setText(String.valueOf(oldLimit));
        }
    }//GEN-LAST:event_txtLimitAmountFocusLost

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        txtAcctNo.setText("");
        txtLimitAmount.setText("");
        txtNoOfYears.setText("");
        txtBorrowerNo.setText("");
        observable.resetSecurityMemberTableValues();
        txtJewelleryDetails.setText("");
        txtGrossWeight.setText("");
        txtGoldRemarks.setText("");
        txtValueOfGold.setText("");
        txtNetWeight.setText("");
        observable.resetGoldSecurity();
        observable.resetSecurityCollateralTableValues();
        observable.resetDepositTypeTableValues();
        observable.resetCalarySecurity();
        chkRenew.setSelected(false);
        chkRiskFund.setSelected(false);
        chrgTableEnableDisable();
        cTabbedPane1.remove(panRiskFundDetails);
        panTransaction.setVisible(false);
        chkRenew.setEnabled(false);
        chkRiskFund.setEnabled(false);        
        lblCustName.setText("Customer Name");
        tdtTodt.setDateValue(null);
        tdtFromDt.setEnabled(false);
        tdtTodt.setEnabled(false);
        txtLimitAmount.setEnabled(false);
        tdtFromDt.setEnabled(false);
        tdtFromDt.setDateValue(null);
        rdoGoldSecurityExitsNo.setSelected(true);// Added by nithya on 07-03-2020 for KD-1379
        txtGoldSecurityId.setText("");
        chargelst =  null;
        chargeMap = null;
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        btnClearActionPerformed(null);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewActionPerformed
        // TODO add your handling code here:
        if(chkRenew.isSelected()){
            tdtFromDt.setEnabled(true);
            tdtTodt.setEnabled(true);
            txtNoOfYears.setEnabled(true);
            txtLimitAmount.setEnabled(true);
        }else{
            tdtFromDt.setEnabled(false);
            tdtTodt.setEnabled(false);
            txtNoOfYears.setEnabled(false);
            txtLimitAmount.setEnabled(false);
            txtNoOfYears.setText("");
            tdtTodt.setDateValue(null);
        }
    }//GEN-LAST:event_chkRenewActionPerformed

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
       
    }//GEN-LAST:event_tdtFromDtFocusLost

    private void chkRiskFundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRiskFundActionPerformed
        // TODO add your handling code here:
        if(chkRiskFund.isSelected()){
            cTabbedPane1.addTab("Risk Fund Details", panRiskFundDetails);
            if(prodDesc.length()>0){
                addRiskFundCharge(prodDesc);
                transactionUI.addToScreen(panTransaction);
                observable.setTransactionOB(transactionUI.getTransactionOB());
                setTotalAmount();
                panTransaction.setVisible(true);
            }
        }else{
            cTabbedPane1.remove(panRiskFundDetails);
        }        
    }//GEN-LAST:event_chkRiskFundActionPerformed
 
    private void addRiskFundCharge(String prod) {
        chrgTableEnableDisable();
        createChargeTable(prod);
        chargeAmount();
    }
    
     private void chrgTableEnableDisable() {
        tableFlag = false;
        panRiskFund.removeAll();
        panRiskFund.setVisible(false);
        panTransaction.removeAll();        
    }
    
     private void createChargeTable(String prod) {
        HashMap tableMap = buildData(prod);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");        
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panRiskFund.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(480, 110);
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tableMouseClicked(evt);
                }
            });                       
            srpChargeDetails = new javax.swing.JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);           
            Dimension d = new Dimension(460, 800);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setPreferredSize(d);
            table.setPreferredScrollableViewportSize(d);            
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(480, 100));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(480, 100));
            panRiskFund.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.getColumnModel().getColumn(0).setPreferredWidth(15);
            table.getColumnModel().getColumn(1).setPreferredWidth(35);
            table.getColumnModel().getColumn(2).setPreferredWidth(80);
            table.getColumnModel().getColumn(3).setPreferredWidth(30);
            table.getColumnModel().getColumn(4).setPreferredWidth(20);
            table.getColumnModel().getColumn(5).setPreferredWidth(20);
            table.revalidate();
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }
    }
     
      public class SimpleTableModel extends AbstractTableModel {
        private ArrayList dataVector;
        private ArrayList headingVector;
        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }

        public int getColumnCount() {
            return headingVector.size();
        }

        public int getRowCount() {
            return dataVector.size();
        }

        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 4)).equals("Y"))) {
                return false;
            } else {
                if (col != 0) {
                    //return false;
                    if (col == 3 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }

        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }
     
      private HashMap buildData(String prodDesc) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        whereMap.put("KCC_RENEWAL", "KCC_RENEWAL");
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                //                if (obj != null) {
                colData.add(CommonUtil.convertObjToStr(obj));
                //                } else {
                //                    colData.add("");
                //                }
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }

      private void chargeAmount() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        appraiserMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        appraiserMap.put("KCC_RENEWAL", "KCC_RENEWAL");       
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        chargeMap = new HashMap();        
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                String editable = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                editable = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_EDITABLE"));               
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue()
                                    * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            float newchrgAmt = (float)chargeAmt;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (newchrgAmt * roundOffType), roundOffType) / roundOffType;
                            }else{
                                chargeAmt = newchrgAmt;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {

                            List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue();
                            if (chargeslabLst != null && chargeslabLst.size() > 0) {
                                double minAmt = 0;
                                double maxAmt = 0;
                                for (int k = 0; k < chargeslabLst.size(); k++) {
                                    LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);
                                    double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
                                    double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
                                    if (limit >= minAmtRange && limit <= maxAmtRange) {
                                        double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
                                        minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
                                        maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();

                                        chargeAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue() * chargeRate / 100;
                                        if (chargeAmt < minAmt) {
                                            chargeAmt = minAmt;
                                        }
                                        if (chargeAmt > maxAmt) {
                                            chargeAmt = maxAmt;
                                        }
                                        break;
                                    }
                                }
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);

                        }else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                    if (editable.equals("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt1));
                    }
                }
            }
            table.revalidate();
            table.updateUI();   
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                calculateServiceTax();
            }
        }
    }
      
      
       private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        totChargeAmount = 0.0;
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                //System.out.println("$#@@$ accHead" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    } else {
                        if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) /*&& CommonUtil.convertObjToStr(table.getValueAt(j, 4)).equals("Y")*/) {
                            String chargeAmt = CommonUtil.convertObjToStr(table.getValueAt(j, 3));
                            chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                            totChargeAmount =+ CommonUtil.convertObjToDouble(chargeAmt);
                        }
                    }
                }
            }
            if (totChargeAmount > 0.0) {
                transactionUI.setCallingAmount(CommonUtil.convertObjToStr(totChargeAmount));
                transactionUI.setCallingApplicantName(lblCustName.getText());
                System.out.println("#$#$$# final chargelst:" + chargelst);
                observable.setChargelst(chargelst);
            }
        }
    }
     
    private void setTotalAmount() {
        double totChargeAmount = 0.0;
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        if (chargelst != null && chargelst.size() > 0 && table.getRowCount() > 0) {
            for (int j = 0; j < table.getRowCount(); j++) {
                if (((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                    totChargeAmount = totChargeAmount+CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                }
            }           
        }
        //System.out.println("########### totChargeAmount : "+totChargeAmount);
        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(totChargeAmount));
        transactionUI.setCallingApplicantName(lblCustName.getText());
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            calculateServiceTax();
        }
    }
       
      private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }
      
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {
        //No service tax calculation for riskfund 
        setTotalAmount();
    }
      
     private void tdtRetirementDtFocusLost(java.awt.event.FocusEvent evt) {
        String rtDate = CommonUtil.convertObjToStr(tdtRetirementDt.getDateValue());
        if (rtDate.length() > 0 && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(rtDate), (Date) curr_dt.clone()) > 0) {
            ClientUtil.displayAlert("Retirement Date Should be in Future Date");
            tdtRetirementDt.setDateValue("");
        }
    } 
    
    public void updateDepositTypeFields() {
        observable.setTxtDepNo(txtDepNo.getText());
        observable.setCboProductTypeSecurity((String) cboProductTypeSecurity.getSelectedItem());
        observable.setCboDepProdID((String) cboDepProdType.getSelectedItem());
        observable.setTdtDepDt(tdtDepDt.getDateValue());
        observable.setTxtDepAmount(txtDepAmount.getText());
        observable.setTxtMaturityDt(txtMaturityDt.getDateValue());
        observable.setTxtMaturityValue(txtMaturityValue.getText());
        observable.setTxtRateOfInterest(txtRateOfInterest.getText());
    }
   
   public void populateDepositTypeFields() {
        txtDepNo.setText(observable.getTxtDepNo());
        //        txtProductId.setText(observable.getTxtProductId());
        cboProductTypeSecurity.setSelectedItem(observable.getCboProductTypeSecurity());
        cboDepProdType.setSelectedItem(observable.getCboDepProdID());
        tdtDepDt.setDateValue(observable.getTdtDepDt());
        txtDepAmount.setText(observable.getTxtDepAmount());
        txtRateOfInterest.setText(observable.getTxtRateOfInterest());
        txtMaturityValue.setText(observable.getTxtMaturityValue());
        txtMaturityDt.setDateValue(observable.getTxtMaturityDt());
    }
            
    private void tblDepositDetailsMousePressed(java.awt.event.MouseEvent evt) {
        updateDepositTypeFields();
        updateMode = true;
        updateTab = tblDepositDetails.getSelectedRow();
        observable.setDepositTypeData(false);
        String depProdType = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 0));
        observable.setCbmProdTypeSecurity(depProdType);
        cboDepProdType.setModel(observable.getCbmDepProdID());
        String st = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.populateDepositTypeDetails(st);
        populateDepositTypeFields();      
        btnDepNo.setEnabled(true);
        txtDepAmount.setEnabled(false);
        txtRateOfInterest.setEnabled(false);
        txtMaturityValue.setEnabled(false);
        txtMaturityDt.setEnabled(false);
        tdtDepDt.setEnabled(false);
        enableDisableDepositPanButton(true);
        btnDepositNew.setEnabled(false);        
    }
    
    private void enableDisableDepositPanButton(boolean flag) {
        btnDepositNew.setEnabled(flag);
        btnDepositSave.setEnabled(flag);
        btnDepositDelete.setEnabled(flag);
    }
    
    private void btnCollateralDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //added  by jiby
        int selRow=tblCollateral.getSelectedRow();
        //
        String s = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        //observable.deleteCollateralTableData(s, tblCollateral.getSelectedRow());
        observable.deleteCollateralTableData(s+"_"+(selRow+1), tblCollateral.getSelectedRow());
        updateTab = -1;
        observable.resetCollateralDetails();
        resetCollateralDetails();
        ClientUtil.enableDisable(panCollatetalDetails, false);
        btnSecurityCollateral(false);
        btnCollateralNew.setEnabled(true);
    }
    
    private StringBuffer verifyDocNo() {
        StringBuffer addExistDoc = new StringBuffer();
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    addExistDoc.append("'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 2)) + "'");
                } else {
                    addExistDoc.append("," + "'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 2)) + "'");
                }
            }
        }
        return addExistDoc;
    }
    
     private void btnCollateralSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            HashMap securityLandMap = new HashMap();
            if (txtOwnerMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("OwnerMember Number should not be empty");           
                 }
            //added by jiby
            else if(txtSurveyNo.getText().length()==0)
            {
                ClientUtil.showAlertWindow("Survey Number should not be empty");
            }
            else {
                if(observable.getOldSurvyNo()==null ||observable.getOldSurvyNo().equals(""))
            {
                //System.out.println("getOldSurvyNo"); 
                observable.setOldSurvyNo(txtSurveyNo.getText().toString());
            }
                ///////
                updateCollateralFields();
                //added by jiby
                 rowCount=tblCollateral.getRowCount();
                //System.out.println("rowCount innnnnnnnn"+rowCount+"tblCollateral.getSelectedRow()"+tblCollateral.getSelectedRow()+"btnNew.isEnabled()"+btnNew.isEnabled());
               
                if(tblCollateral.getSelectedRow()>=0 && (btnCollateralNew.isEnabled()))
                {
                    //System.out.println("kiiiii");
                   rowCount= tblCollateral.getSelectedRow()+1;
                  
                }
                else
                {
                    //System.out.println("miiiii");
                if(rowCount==0)
                {
                    rowCount=1;
                }
                else
                {
                    rowCount=rowCount+1;
                }
                }
                //.out.println("rowCount====="+rowCount);
                observable.setRowCoun(rowCount);
                ///
                observable.addCollateralTable(updateTab, updateMode);
                tblCollateral.setModel(observable.getTblCollateralDetails());
                observable.resetCollateralDetails();
                resetCollateralDetails();
                ClientUtil.enableDisable(panCollatetalDetails, false);
                btnSecurityCollateral(false);
                btnCollateralNew.setEnabled(true);
                btnOwnerMemNo.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
   private void btnSecurityCollateral(boolean flag) {
        btnCollateralNew.setEnabled(flag);
        btnCollateralSave.setEnabled(flag);
        btnCollateralDelete.setEnabled(flag);
    }
     
     
      public void updateCollateralFields() {
        observable.setRdoGahanYes(rdoGahanYes.isSelected());
        observable.setRdoGahanNo(rdoGahanNo.isSelected());
        observable.setTxtOwnerMemNo(txtOwnerMemNo.getText());
        observable.setTxtOwnerMemberNname(txtOwnerMemberNname.getText());
        observable.setTxtDocumentNo(txtDocumentNo.getText());        
        observable.setCboDocumentType(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()));
        observable.setTdtDocumentDate(tdtDocumentDate.getDateValue());
        observable.setTxtRegisteredOffice(txtRegisteredOffice.getText());
        observable.setCboPledge(CommonUtil.convertObjToStr(cboPledge.getSelectedItem()));
        observable.setTxtPledgeType(txtPledgeType.getText());
        observable.setTdtPledgeDate(tdtPledgeDate.getDateValue());
        observable.setTxtPledgeNo(txtPledgeNo.getText());
        observable.setTxtPledgeAmount(txtPledgeAmount.getText());
        observable.setTxtVillage(txtVillage.getText());
        observable.setTxtSurveyNo(txtSurveyNo.getText());
        observable.setTxtTotalArea(txtTotalArea.getText());
        observable.setCboNature(CommonUtil.convertObjToStr(cboNature.getSelectedItem()));
        observable.setCboRight(CommonUtil.convertObjToStr(cboRight.getSelectedItem()));     
        observable.setTxtAreaParticular(txtAreaParticular.getText());
    }
   
    public void calculateMaturityDate(int noOfYears) {
        int yearTobeAdded = 1900;       
        Date depDt = DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue());        
        depDt.setYear(depDt.getYear() + noOfYears);
        GregorianCalendar cal = new GregorianCalendar(depDt.getYear(), depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, depDt.getYear() + noOfYears);
        cal.add(GregorianCalendar.MONTH, depDt.getMonth());
        cal.add(GregorianCalendar.DAY_OF_MONTH, depDt.getDate());
        String matDt = DateUtil.getStringDate(cal.getTime());
        //System.out.println("maturity date :: " + depDt) ;   
        tdtTodt.setDateValue(DateUtil.getStringDate(depDt));
    }
    
   
    
     private void btnMemberDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.deleteMemberTableData(s, tblMemberType.getSelectedRow());
        observable.resetMemberTypeDetails();
        resetMemberTypeDetails();
        ClientUtil.enableDisable(panMemberDetails, false);
        btnSecurityMember(false);
        btnMemberNew.setEnabled(true);
    }
   
 

    private void exitForm(java.awt.event.WindowEvent evt) {                          
        observable.destroyObjects();
        observable = null;
        this.dispose();
    }                         
           
    private void btnDocumentNoActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("btnDocumentNoActionPerformed   ");
        viewType = "DOCUMENT_NO";
        popUp("DOCUMENT_NO");
    }
    
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void setUserDefinedFieldsEnableDisable(boolean val){
       
    }
    
    private void setPopulateFieldsEnableDisable(boolean val){
        txtLimitAmount.setEnabled(val);     
        txtNoOfYears.setEnabled(val);    
    }
  
     private void btnCollateralNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setCollateralTypeData(true);
        btnSecurityCollateral(false);
        btnCollateralSave.setEnabled(true);
        //        ClientUtil.enableDisable(panCollatetalDetails,true);
        if (rdoGahanYes.isSelected()) {
            rdoGahanYesActionPerformed(null);
        }
        if(rdoGahanNo.isSelected()){
            rdoGahanNoActionPerformed(null);
            btnOwnerMemNo.setEnabled(true);
        }
        ClientUtil.enableDisable(panGahanYesNo, true);
        txtOwnerMemberNname.setEnabled(false);
        //added by jiby
         btnOwnerMemNo.setEnabled(true);
        //System.out.println("updateMode in collateral new"+updateMode);
        observable.setOldSurvyNo("");
        updateTab=-1;
    }
    
     private void rdoGahanNoActionPerformed(java.awt.event.ActionEvent evt) {      
        if (rdoGahanNo.isSelected()) {
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            resetCollateralDetails();          
        } else {
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            ClientUtil.enableDisable(panCollatetalDetails, false);
        }

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
        new KCCRenewalUI().show();
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
            editMapCondition.put("BEHAVES_LIKE","BEHAVES_LIKE");
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForNewScheduleCreation");
        }else if (field.equalsIgnoreCase("SUB_MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
        }else if (field.equalsIgnoreCase("OWNER_MEMBER_NO_GAHAN")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetailsFromGahan");
        } else if (field.equalsIgnoreCase("OWNER_MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
        } else if (field.equalsIgnoreCase("DOCUMENT_NO")) {
            HashMap whereListMap = new HashMap();
            whereListMap.put("ENTERED_DOCUMENT_NO", verifyDocNo().toString());
            //Added By Suresh
            if (txtOwnerMemNo.getText().length() > 0) {
                whereListMap.put("MEMBERSHIP_NO", txtOwnerMemNo.getText());
            }
            viewMap.put(CommonConstants.MAP_NAME, "getGahanDetailsforLoan");
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }else if (field.equalsIgnoreCase("GOLD_SECURITY_STOCK")) {
             viewMap.put(CommonConstants.MAP_NAME, "getCustomerGoldSecurityStock");
        }else if (field.equalsIgnoreCase("DEPOSIT_ACC_NO")) {
            HashMap whereMap = new HashMap();
            whereMap.put("SELECTED_BRANCH",(((ComboBoxModel) TrueTransactMain.cboBranchList.getModel()).getKeyForSelected()));
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepProdID().getKeyForSelected()));
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            if (prodType.equals("TD")) {
                viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterDepositNo");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getMDSChittalNo");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
         new ViewAll(this, viewMap).show();
    }
    
    public void populateCollateralFields() {
        if (observable.isRdoGahanYes()) {
            rdoGahanYes.setSelected(observable.isRdoGahanYes());
        } else {
            rdoGahanNo.setSelected(observable.isRdoGahanNo());
        }
        txtOwnerMemNo.setText(observable.getTxtOwnerMemNo());
        txtOwnerMemberNname.setText(observable.getTxtOwnerMemberNname());
        txtDocumentNo.setText(observable.getTxtDocumentNo());
        cboDocumentType.setSelectedItem(observable.getCboDocumentType());
        tdtDocumentDate.setDateValue(observable.getTdtDocumentDate());
        txtRegisteredOffice.setText(observable.getTxtRegisteredOffice());
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboPledge()));
        tdtPledgeDate.setDateValue(observable.getTdtPledgeDate());
        txtPledgeNo.setText(observable.getTxtPledgeNo());
        txtPledgeAmount.setText(observable.getTxtPledgeAmount());
        txtVillage.setText(observable.getTxtVillage());
        txtSurveyNo.setText(observable.getTxtSurveyNo());
        txtTotalArea.setText(observable.getTxtTotalArea());
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboNature()));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboRight()));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
    }
    
    private void tblCollateralMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        //        updateCollateralFields();
        updateMode = true;
        updateTab = tblCollateral.getSelectedRow();
        observable.setCollateralTypeData(false);
        String st = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        //comm by jiby
        //observable.populateCollateralDetails(st);
        observable.populateCollateralDetails(st+"_"+(updateTab+1));
        populateCollateralFields();
        collateralJointAccountDisplay(txtOwnerMemNo.getText());
      
            btnSecurityCollateral(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnCollateralNew.setEnabled(false);
       
        txtOwnerMemNo.setEnabled(false);
        txtOwnerMemberNname.setEnabled(false);
        //added by jiby
         btnCollateralNew.setEnabled(true);
       // //System.out.println("iiiiiiiiiiiiii"+CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 4)));
        observable.setOldSurvyNo(CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 4)));
    }

    public void fillData(Object param) {          
        HashMap hash = (HashMap) param;
        //System.out.println("hash "+hash);
        prodDesc = "";
        if(viewType.equalsIgnoreCase("AccountSearch")){          
          txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCT_NUM"))); 
          lblCustName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER NAME")));
          observable.setTxtAcctNo(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));  
          txtLimitAmount.setText(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));   
          observable.setTotalBaseAmount(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));
          observable.setOutstandingAmnt(CommonUtil.convertObjToStr(hash.get("OUTSTANDING_AMNT")));
          observable.setSanctionedAmount(CommonUtil.convertObjToStr(hash.get("SANCTION_AMT")));
          observable.setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
          hash.put("WHERE", hash.get("ACCT_NUM"));
          tblMemberType.setModel(observable.getTblMemberTypeDetails());
          tblCollateral.setModel(observable.getTblCollateralDetails());
          tblJointCollateral.setModel(observable.getTblJointCollateral());
          tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
          tblSalary.setModel(observable.getTblSalarySecrityTable());
          hash.put("BORROW_NO",hash.get("BORROWER NO"));
          observable.populateData(hash);
          prodDesc = CommonUtil.convertObjToStr(hash.get("PROD_DESC"));
          chkRenew.setEnabled(true);
          chkRiskFund.setEnabled(true);
          btnOK.setEnabled(true);
          btnClear.setEnabled(true);
        }else if (viewType == "SUB_MEMBER_NO") {
                String memberNo = txtMemNo.getText();
                if (tblMemberType.getRowCount() > 0) {
                    for (int i = 0; i < tblMemberType.getRowCount(); i++) {
                        String membNo = CommonUtil.convertObjToStr(tblMemberType.getValueAt(i, 0));
                    }
                }
                txtMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                observable.setTxtMemNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                txtMemName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                observable.setTxtMemName(CommonUtil.convertObjToStr(hash.get("NAME")));
                hash.put("SHARE ACCOUNT NO", hash.get("MEMBER_NO"));
                List lst = ClientUtil.executeQuery("getShareAccInfoTO", hash);
                if (lst != null && lst.size() > 0) {
                    HashMap resultMap = (HashMap) lst.get(0);
                    txtMemType.setText(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                    observable.setTxtMemNo(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                }
            }else if (viewType == "OWNER_MEMBER_NO_GAHAN") {
                String memberNo = txtOwnerMemNo.getText();
                if (tblCollateral.getRowCount() > 0) {
                    for (int i = 0; i < tblCollateral.getRowCount(); i++) {
                        String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 0));                      
                    }
                }
                txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                observable.setDocGenId(CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                observable.setRdoGahanYes(true);
                collateralJointAccountDisplay(txtOwnerMemNo.getText());
                //observable.setTxtOwnerMemNo(txtExistingAcctNo.getText());//CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                observable.setDocGenId("");
            }else if (viewType == "OWNER_MEMBER_NO") {
                String memberNo = txtOwnerMemNo.getText();
                if (tblCollateral.getRowCount() > 0) {
                    for (int i = 0; i < tblCollateral.getRowCount(); i++) {
                        String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 0));
                    }
                }
                txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                observable.setDocGenId(CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                collateralJointAccountDisplay(txtOwnerMemNo.getText());
                //observable.setTxtOwnerMemNo(txtExistingAcctNo.getText());//CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                observable.setDocGenId("");
            }else if (viewType == "DOCUMENT_NO") {
                HashMap documentMap = new HashMap();
                String documentNo = txtDocumentNo.getText();
                double sanctionAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue();
                String loanExpiryDt = CommonUtil.convertObjToStr(tdtTodt.getDateValue());
                String loanStartDt = CommonUtil.convertObjToStr(tdtFromDt.getDateValue());
                docGenIdValue = hash.get("DOCUMENT_GEN_ID").toString();
                if (loanExpiryDt.length() == 0 || sanctionAmt <= 0) {
                    //ClientUtil.displayAlert("Enter the Sanction Details ");
                    //return;
                }
                documentMap.put("DOCUMENT_GEN_ID", CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                documentMap.put("DOCUMENT_NO", CommonUtil.convertObjToStr(hash.get("DOCUMENT_NO")));
                documentMap.put("SANCTION_AMT", new Double(sanctionAmt));
                documentMap.put("LOAN_EXPIRY_DT", DateUtil.getDateMMDDYYYY(loanExpiryDt));
                documentMap.put("ACCT_OPEN_DT", DateUtil.getDateMMDDYYYY(loanStartDt));
                if (getDocumentDetails(documentMap)) {
                    return;
                }                
                txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                observable.setTxtOwnerMemNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
            }else if (viewType == "GOLD_SECURITY_STOCK") {
                System.out.println("GOLD_SECURITY_STOCK :: " + hash);
                txtJewelleryDetails.setText(CommonUtil.convertObjToStr(hash.get("PARTICULARS")));
                txtGrossWeight.setText(CommonUtil.convertObjToStr(hash.get("GROSS_WEIGHT")));
                txtNetWeight.setText(CommonUtil.convertObjToStr(hash.get("NET_WEIGHT")));                
                UpdateCalculatedGoldSecurityValue(hash);
                txtGoldSecurityId.setText(CommonUtil.convertObjToStr(hash.get("GOLD_SECURITY_ID")));
               
            }else if (viewType.equals("DEPOSIT_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
                if (prodType.equals("TD")) {
                    txtDepNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    HashMap whereMap = new HashMap();
                    whereMap.put("DEPOSIT NO", hash.get("ACT_NUM"));
                    List accountLst = ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", whereMap);
                    if (accountLst != null && accountLst.size() > 0) {
                        whereMap = (HashMap) accountLst.get(0);
                        tdtDepDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT")));
                        txtDepAmount.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                        txtRateOfInterest.setText(CommonUtil.convertObjToStr(whereMap.get("RATE_OF_INT")));
                        txtMaturityValue.setText(CommonUtil.convertObjToStr(whereMap.get("MATURITY_AMT")));
                        txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("MATURITY_DT")));
                    }
                } else {
                    txtDepNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                    tdtDepDt.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                    txtDepAmount.setText(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                    txtMaturityValue.setText(CommonUtil.convertObjToStr(hash.get("PAID_AMOUNT")));
                    txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                }
                calculateTot();
            }
       
    }
    
     private void tblSalaryMousePressed(java.awt.event.MouseEvent evt) {
        salarytblSelectedRow = tblSalary.getSelectedRow();
        observable.showSalaryTableValues(salarytblSelectedRow);
        enableDisableSalaryBtnsNew(false);       
        updateSalaryUI();
    }
     
    private void updateSalaryUI() {
        txtSalaryCertificateNo.setText(observable.getTxtSalaryCertificateNo());
        txtEmployerName.setText(observable.getTxtEmployerName());
        txtAddress.setText(observable.getTxtAddress());
        cboCity.setSelectedItem(observable.getCboSecurityCity());
        txtPinCode.setText(observable.getTxtPinCode());
        txtDesignation.setText(observable.getTxtDesignation());
        txtContactNo.setText(observable.getTxtContactNo());
        tdtRetirementDt.setDateValue(observable.getTdtRetirementDt());
        txtMemberNum.setText(observable.getTxtMemberNum());
        txtTotalSalary.setText(observable.getTxtTotalSalary());
        txtNetWorth1.setText(observable.getTxtNetWorth());
        txtSalaryRemark.setText(observable.getTxtSalaryRemark());
    } 
     
    private void enableDisableSalaryBtnsNew(boolean isNewEnable) {

        if (isNewEnable) {
            btnSalaryNew.setEnabled(isNewEnable);
            btnSalarySave.setEnabled(false);
            btnSalaryDelete.setEnabled(false);
        } else {
            btnSalaryNew.setEnabled(isNewEnable);
            btnSalarySave.setEnabled(true);
            btnSalaryDelete.setEnabled(true);
        }
    } 
     
    private void btnSalaryDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        observable.deleteSalarySecrityTableValue(salarytblSelectedRow);
        resetSalaryDetails();
        enableDisableSalaryBtnsNew(true);
        salarytblSelectedRow = -1;
    }

     private void resetSalaryDetails() {
        txtSalaryCertificateNo.setText("");
        txtEmployerName.setText("");
        txtAddress.setText("");
        cboCity.setSelectedItem("");
        txtPinCode.setText("");
        txtDesignation.setText("");
        txtContactNo.setText("");
        tdtRetirementDt.setDateValue("");
        txtMemberNum.setText("");
        txtTotalSalary.setText("");
        txtNetWorth1.setText("");
        txtSalaryRemark.setText("");
    }
     
      private void updateSalaryOBFields() {
        observable.setTxtSalaryCertificateNo(txtSalaryCertificateNo.getText());
        observable.setTxtEmployerName(txtEmployerName.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setCboSecurityCity(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTxtDesignation(txtDesignation.getText());
        observable.setTxtContactNo(txtContactNo.getText());
        observable.setTdtRetirementDt(tdtRetirementDt.getDateValue());
        observable.setTxtMemberNum(txtMemberNum.getText());
        observable.setTxtTotalSalary(txtTotalSalary.getText());
        observable.setTxtNetWorth(txtNetWorth1.getText());
        observable.setTxtSalaryRemark(txtSalaryRemark.getText());
    }
    
   private void enableDisableSalaryDetails(boolean flag) {
        ClientUtil.enableDisable(panSalaryDetails, flag);
    }
      
     private void btnSalarySaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (CommonUtil.convertObjToStr(txtSalaryCertificateNo.getText()).length() > 0) {
            updateSalaryOBFields();
            int rowcount = tblSalary.getRowCount();
            observable.setSalarySecrityTableValue(salarytblSelectedRow, rowcount);
            enableDisableSalaryDetails(false);
            resetSalaryDetails();
            enableDisableSalaryBtnsNew(true);
            salarytblSelectedRow = -1;
        } else {
            ClientUtil.displayAlert("Please Enter Salary Certificate No");
        }
    }
     
    
    private void btnSalaryNewActionPerformed(java.awt.event.ActionEvent evt) {
        resetSalaryDetails();
        enableDisableSalaryDetails(true);
        enableDisableSalaryBtns(true);
        salarytblSelectedRow = -1;
    }
    
     private void enableDisableSalaryBtns(boolean flag) {
        btnSalaryNew.setEnabled(flag);
        btnSalarySave.setEnabled(flag);
        btnSalaryDelete.setEnabled(flag);
    }
    
    private void updateGahandetails(HashMap map) {
        //System.out.println("map####" + map);
        double pledgeAmt = 0;
        double actualPledge = 0;
        List lst = ClientUtil.executeQuery("getUnAuthLoanSecurityLandDetails", map);
        if (lst != null && lst.size() > 0) {
            ClientUtil.displayAlert("Already record is pending for authorization for this member");
            return;
        }

        txtOwnerMemNo.setText(CommonUtil.convertObjToStr(map.get("NATURE")));
        txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(map.get("NATURE")));
        txtDocumentNo.setText(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")));
        //        txtDocumentType.setText(CommonUtil.convertObjToStr(map.get("DOCUMENT_TYPE")));
        cboDocumentType.setSelectedItem(CommonUtil.convertObjToStr(map.get("DOCUMENT_TYPE")));
        tdtDocumentDate.setDateValue(CommonUtil.convertObjToStr(map.get("DOCUMENT_DT")));
        txtRegisteredOffice.setText(CommonUtil.convertObjToStr(map.get("REGISTRED_OFFICE")));
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(map.get("PLEDGE")));
        tdtPledgeDate.setDateValue(CommonUtil.convertObjToStr(map.get("PLEDGE_DT")));
        txtPledgeNo.setText(CommonUtil.convertObjToStr(map.get("PLEDGE_NO")));
        actualPledge = checkAvailableSecurity(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        //System.out.println("actualPledge===" + actualPledge);
        Double gahanLnAmt = getPldgeAmountForLoan(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        //System.out.println("gahanLnAmt====" + gahanLnAmt);
        actualPledge = actualPledge + gahanLnAmt;
        Double gahanMdsAmt = getPldgeAmtForMds(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        //System.out.println("gahanMdsAmt====" + gahanMdsAmt);
        actualPledge = actualPledge - gahanMdsAmt;
        pledgeAmt = getGahanAvailableSecurity(actualPledge);

        Double sanAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText().toString());
        //System.out.println("sanAmtsanAmt=====" + sanAmt);
        if (sanAmt < pledgeAmt) {
            txtPledgeAmount.setText(String.valueOf(sanAmt));
        } else {
            txtPledgeAmount.setText(String.valueOf(pledgeAmt));
        }
        //getPldgeAmount();
        txtVillage.setText(CommonUtil.convertObjToStr(map.get("VILLAGE")));
        txtSurveyNo.setText(CommonUtil.convertObjToStr(map.get("SARVEY_NO")));
        txtTotalArea.setText(CommonUtil.convertObjToStr(map.get("TOTAL_AREA")));
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(map.get("NATURE")));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(map.get("RIGHT")));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
        observable.setDocGenId(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));

        observable.addPledgeAmountMap(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")), actualPledge);

    }
    
    private double getGahanAvailableSecurity(double maxsecurityAmt) {
        double availableSecuirty = 0;
        double sumGahanTableValue = 0;
        double loanAmt = 0;
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                sumGahanTableValue += CommonUtil.convertObjToDouble(tblCollateral.getValueAt(i, 3)).doubleValue();
            }
        }
        loanAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue();
        availableSecuirty = loanAmt - sumGahanTableValue;

//        if(maxsecurityAmt>=availableSecuirty)
//            return availableSecuirty;
//        else
        return maxsecurityAmt;
    }
    
    public double getPldgeAmtForMds(String doc_gen_id) {
        Double gahanForMDS = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("DOC_GEN_ID", doc_gen_id);
        List lstGahanForMds = ClientUtil.executeQuery("getSelGahanForMds", mapDocGenId);
        if (!lstGahanForMds.isEmpty() && !lstGahanForMds.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForMds.get(0);
//            if(mapDocGenId.containsValue("AMOUNT"))
//            {
            gahanForMDS = CommonUtil.convertObjToDouble(mapDocGenId.get("AMOUNT"));
//            }   
        }
        return gahanForMDS;
    }

    private double checkAvailableSecurity(String docGenId) {
        HashMap map = new HashMap();
        double availableSecurity = 0;
        if (docGenId != null) {
            map.put("DOC_GEN_ID", docGenId);
            List lst = ClientUtil.executeQuery("getGahanAvailableSecurityforLoan", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                availableSecurity = CommonUtil.convertObjToDouble(map.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            }
        }
        return availableSecurity;
    }
    
     public double getPldgeAmountForLoan(String doc_gen_id) {
        Double gahanForLn = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("DOC_GEN_ID", doc_gen_id);
        List lstGahanForLn = ClientUtil.executeQuery("getSelGahanForLn", mapDocGenId);
        if (!lstGahanForLn.isEmpty() && !lstGahanForLn.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForLn.get(0);
//            if(mapDocGenId.containsValue("CLEARBAL"))
//            {
            gahanForLn = CommonUtil.convertObjToDouble(mapDocGenId.get("CLEARBAL"));
//            }   
        }
        return gahanForLn;
    }

    
    
    private boolean getDocumentDetails(HashMap documentMap) {
        //System.out.println("document Map" + documentMap);
        List lst = ClientUtil.executeQuery("getSelectGahanDocumentDetails", documentMap);
        if (lst != null && lst.size() > 0) {
            HashMap dataMap = (HashMap) lst.get(0);
            Date gahanExpDt = (Date) dataMap.get("GAHAN_EXP_DT");
            Date gahanReleaseDt = (Date) dataMap.get("GAHAN_RELEASE_DT");
            double sanctionAmt = CommonUtil.convertObjToDouble(documentMap.get("SANCTION_AMT")).doubleValue();
            double pledgeAmt = CommonUtil.convertObjToDouble(dataMap.get("PLEDGE_AMT")).doubleValue();
            if (gahanExpDt != null) {
                if (DateUtil.dateDiff((Date) documentMap.get("ACCT_OPEN_DT"), gahanExpDt) <= 0) {
                    ClientUtil.displayAlert("Gahan has expired.. " + "\n" + " Select another Gahan number");
                    return true;
                }
                if (DateUtil.dateDiff((Date) documentMap.get("LOAN_EXPIRY_DT"), gahanExpDt) <= 0) {
                    ClientUtil.displayAlert("Gahan expires before the Loan Expiry Date. " + "\n" + " Choose another Gahan number");
                    return true;
                }
            }
            if (gahanReleaseDt != null) {
                if (DateUtil.dateDiff((Date) documentMap.get("ACCT_OPEN_DT"), gahanReleaseDt) <= 0) {
                    ClientUtil.displayAlert("Gahan has been Released on   :" + DateUtil.getStringDate(gahanReleaseDt) + "\n" + "Choose  another Gahan Number");
                    return true;
                }
            }

            updateGahandetails(dataMap);
            //            else if(sanctionAmt>pledgeAmt){
            //                  ClientUtil.displayAlert("Document Security not sufficient for loan");
            //                return true;
            //            }
        } else {
            return true;
        }
        return false;
    }
    
     private void collateralJointAccountDisplay(String memNo) {
        observable.updateCollateralJointDetails(memNo);
        tblJointCollateral.setModel(observable.getTblJointCollateral());

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
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOK;
    private com.see.truetransact.uicomponent.CButton btnSearchAcctNo;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CCheckBox chkRenew;
    private com.see.truetransact.uicomponent.CCheckBox chkRiskFund;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstall;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panBtns;
    private com.see.truetransact.uicomponent.CPanel panOuter;
    private com.see.truetransact.uicomponent.CPanel panRenew1;
    private com.see.truetransact.uicomponent.CPanel panRenew2;
    private com.see.truetransact.uicomponent.CPanel panRenew3;
    private com.see.truetransact.uicomponent.CPanel panRiskFund;
    private com.see.truetransact.uicomponent.CPanel panRiskFundDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtTodt;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtBorrowerNo;
    private com.see.truetransact.uicomponent.CTextField txtLimitAmount;
    private com.see.truetransact.uicomponent.CTextField txtNoOfYears;
    // End of variables declaration//GEN-END:variables

    private void saveAction() {       
       updateOBFields();
        if (chkRiskFund.isSelected()) {
            finalizeCharges();
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                transactionSize = (transactionUI.getOutputTO()).size();
            }
            if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
        }else{
            observable.setChargelst(null);
        }
       observable.doAction();  
       if(observable.getResult().equalsIgnoreCase(ClientConstants.RESULT_STATUS[1]) && chkRenew.isSelected()){
           ClientUtil.showMessageWindow("OD - "+ txtAcctNo.getText() + " Renewed : from " + tdtFromDt.getDateValue() + " To " + tdtTodt.getDateValue() );
       }else if(observable.getResult().equalsIgnoreCase(ClientConstants.RESULT_STATUS[1])){
           ClientUtil.showMessageWindow("Suretys Updated For Acct No : " + txtAcctNo.getText());
       }
        if (chkRiskFund.isSelected()) {//Added By Suresh R  on 5-Sep-2019 Ref By Jithesh
            displayTransDetail();
        }
       observable.resetOBFields();
       lblCustName.setText("");
       observable.ttNotifyObservers();
       btnClearActionPerformed(null);
    }
    
    private void displayTransDetail() {//Added By Suresh R  on 5-Sep-2019 Ref By Jithesh
        String displayStr = "";
        String oldBatchId = "";
        String newBatchId = "";
        String transType = "";
        int CreditcashCount = 0;
        int DebitcashCount = 0;
        String debitCashSingleId = "";
        String creditCashSingleId = "";
        String actNum = CommonUtil.convertObjToStr(txtAcctNo.getText());
        HashMap transMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        HashMap transIdMap = new HashMap();
        int cashCount = 0;
        int transferCount = 0;
        transMap.put("LOAN_NO", actNum);
        transMap.put("CURR_DT", curr_dt);
        transMap.put("AUTH_STATUS", "AUTH_STATUS");
        List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
        if (lst != null && lst.size() > 0) {
            displayStr += "Transfer Transaction Details...\n";
            for (int i = 0; i < lst.size(); i++) {
                transMap = (HashMap) lst.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Batch Id : " + transMap.get("BATCH_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                }
                oldBatchId = newBatchId;
                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
            }
            transferCount++;
            transType = "TRANSFER";
        } else {
            actNum = CommonUtil.convertObjToStr(txtAcctNo.getText());
            transMap = new HashMap();
            transMap.put("LOAN_NO", actNum);
            transMap.put("AUTH_STATUS", "AUTH_STATUS");
            transMap.put("CURR_DT", curr_dt);
            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Cash Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {
                    transMap = (HashMap) lst.get(i);
                    displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                                + "   Amount :  " + transMap.get("AMOUNT") + "\n";
                    } else {
                        displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID");
                        displayStr += "   Amount :  " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                    if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
                        DebitcashCount++;
                        debitCashSingleId = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                    }
                    if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                        CreditcashCount++;
                        creditCashSingleId = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                    }
                }
                cashCount++;
                transType = "CASH";
            }
        }
    
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", curr_dt);
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            int yesNo = 0;
            if (transType.equals("TRANSFER")) {
                printParamMap.put("TransId", keys1[0]);
                ttIntgration.setParam(printParamMap);
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                if (yesNo == 0) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                }
            } else if (transType.equals("CASH")) {
                if (CreditcashCount > 0) {
                    printParamMap.put("TransId", creditCashSingleId);
                    ttIntgration.setParam(printParamMap);
                    int yesNoCredit = 0;
                    String[] options = {"Yes", "No"};
                    yesNoCredit = COptionPane.showOptionDialog(null, "Do you want to print Receipt?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    if (yesNoCredit == 0) {
                        ttIntgration.integrationForPrint("CashReceipt", false);
                    }
                }
                if (DebitcashCount > 0) {
                    printParamMap.put("TransId", debitCashSingleId);
                    ttIntgration.setParam(printParamMap);
                    int yesNoDebit = 0;
                    String[] options = {"Yes", "No"};
                    yesNoDebit = COptionPane.showOptionDialog(null, "Do you want to print Voucher?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    if (yesNoDebit == 0) {
                        ttIntgration.integrationForPrint("CashPayment", false);
                    }
                }
            }
        }
    }

   private void clearUIComponents(){
       txtLimitAmount.setText("");    
       txtNoOfYears.setText("");     
       ClientUtil.clearAll(this);
   }
   
   public static double MonthDiff(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) ;
    }
   
    public void updateMemberTypeFields() {
        observable.setTxtMemNo(txtMemNo.getText());
        observable.setTxtMemName(txtMemName.getText());
        observable.setTxtMemType(txtMemType.getText());
        observable.setTxtContactNum(txtContactNum.getText());
        observable.setTxtMemNetworth(txtMemNetworth.getText());        
        observable.setTxtMemPriority(txtMemPriority.getText()); 
    }
    
    public void populateMemberTypeFields() {
        txtMemNo.setText(observable.getTxtMemNo());
        txtMemName.setText(observable.getTxtMemName());
        txtMemType.setText(observable.getTxtMemType());
        txtContactNum.setText(observable.getTxtContactNum());
        txtMemNetworth.setText(observable.getTxtMemNetworth());
        txtMemPriority.setText(observable.getTxtMemPriority());
    }
    
    private void btnSecurityMember(boolean flag) {
        btnMemberNew.setEnabled(flag);
        btnMemberSave.setEnabled(flag);
        btnMemberDelete.setEnabled(flag);
    }
    
    private void tblMemberTypeMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateMemberTypeFields();
        updateMode = true;
        updateTab = tblMemberType.getSelectedRow();
        observable.setMemberTypeData(false);
        String st = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.populateMemberTypeDetails(st);
        populateMemberTypeFields();
       
            btnSecurityMember(true);
            ClientUtil.enableDisable(panMemberDetails, true);
            btnMemberNew.setEnabled(false);
        
        txtMemName.setEnabled(false);
        txtMemType.setEnabled(false);
        txtMemNo.setEnabled(false);
    }
     private void resetMemberTypeDetails() {
        txtMemNo.setText("");
        txtMemName.setText("");
        txtMemType.setText("");
        txtContactNum.setText("");
        txtMemNetworth.setText("");
        txtMemPriority.setText("");
    }
     private void btnMemberSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (txtMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Member number should not be empty");
            } else {
                int maxLoanNo = 0;
                double maxSuretyAmount = 0.0;
                int loanNo = 0;
                HashMap memMap = new HashMap();
                memMap.put("MEMBER_NO", txtMemNo.getText());
                List suretyList = ClientUtil.executeQuery("getMaxSurety", memMap);
                if (suretyList != null && suretyList.size() > 0) {
                    HashMap surMap = (HashMap) suretyList.get(0);
                    if (surMap.containsKey("MAXIMUM_LOAN_PER_SURETY") && surMap.get("MAXIMUM_LOAN_PER_SURETY") != null) {
                        maxLoanNo = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_LOAN_PER_SURETY"));
                    }
                    //Added by nithya for KD-2966
                    if (surMap.containsKey("MAXIMUM_SURETY_AMT") && surMap.get("MAXIMUM_SURETY_AMT") != null) {
                        maxSuretyAmount = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_SURETY_AMT"));
                    }
                    List lonCountList = ClientUtil.executeQuery("getMaxNumberOfLoaneeOfMember", memMap);
                    if (lonCountList != null && lonCountList.size() > 0) {
                        HashMap loanCountMap = (HashMap) lonCountList.get(0);
                        loanNo = CommonUtil.convertObjToInt(loanCountMap.get("TOT_NO_LOAN"));
                        if (maxLoanNo > 0 && loanNo > 0 && loanNo >= maxLoanNo) {
                            ClientUtil.showMessageWindow("This Member already Stand  surety For Maximum no of Loan !!!!");
                            return;
                        }
                    }

                    List loanSuertyAmtLst = ClientUtil.executeQuery("getTotAmtSetAsSuretyForMember", memMap);
                    if (loanSuertyAmtLst != null && loanSuertyAmtLst.size() > 0) {
                        HashMap suretyAmtMap = (HashMap) loanSuertyAmtLst.get(0);
                        double totSuretyAmt = CommonUtil.convertObjToDouble(suretyAmtMap.get("TOT_SURETY_AMT"));
                        double networthGiven = CommonUtil.convertObjToDouble(txtMemNetworth.getText());
                        if (maxSuretyAmount > 0 && (totSuretyAmt + networthGiven) > maxSuretyAmount) {
                            ClientUtil.showMessageWindow("The surety amount exceeds maximum surety amount Rs." + maxSuretyAmount + "/-for this member !!!"
                                    + "\nThis Member Stand Surety of Amt Rs. " + totSuretyAmt + "/-");
                            txtMemNetworth.setText("");
                            return;
                        }
                    }
                }
                updateMemberTypeFields();
                observable.addMemberTypeTable(updateTab, updateMode);
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                observable.resetMemberTypeDetails();
                resetMemberTypeDetails();
                ClientUtil.enableDisable(panMemberDetails, false);
                btnSecurityMember(false);
                btnMemberNew.setEnabled(true);
                btnMemNo.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
       private void btnMemberNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setMemberTypeData(true);
        btnSecurityMember(false);
        btnMemberSave.setEnabled(true);
        ClientUtil.enableDisable(panMemberDetails, true);
        btnMemNo.setEnabled(true);
        txtMemName.setEnabled(false);
        txtMemType.setEnabled(false);
        rdoGahanYesActionPerformed(null);
      }
       
       private void btnMemNoActionPerformed(java.awt.event.ActionEvent evt) {       
        viewType = "SUB_MEMBER_NO";
        new CheckCustomerIdUI(this);

    }
       
       private void resetCollateralDetails() {
        txtOwnerMemNo.setText("");
        txtOwnerMemberNname.setText("");
        txtDocumentNo.setText("");
        //        txtDocumentType.setText("");
        cboDocumentType.setSelectedItem("");
        tdtDocumentDate.setDateValue("");
        txtRegisteredOffice.setText("");
        cboPledge.setSelectedItem("");
        tdtPledgeDate.setDateValue("");
        txtPledgeNo.setText("");
        txtPledgeAmount.setText("");
        txtVillage.setText("");
        txtSurveyNo.setText("");
        txtTotalArea.setText("");
        cboNature.setSelectedItem("");
        cboRight.setSelectedItem("");
        txtAreaParticular.setText("");
    }
       
       private void rdoGahanYesActionPerformed(java.awt.event.ActionEvent evt) {       
        if (rdoGahanYes.isSelected()) {
            ClientUtil.enableDisable(panCollatetalDetails, false);
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            panGahanYesNo.setEnabled(true);
            txtPledgeAmount.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            //Added By Suresh
            txtOwnerMemNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(true);
            resetCollateralDetails();
        } else {
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
        }

    }
   
  private void enableDisableGoldType(boolean flag) {
        txtJewelleryDetails.setEnabled(true);
        txtGoldRemarks.setEnabled(flag);
        txtValueOfGold.setEnabled(flag);
        txtNetWeight.setEnabled(flag);
        txtGrossWeight.setEnabled(flag);
    }
  
   private void btnOwnerMemNoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (rdoGahanYes.isSelected()) {
            viewType = "OWNER_MEMBER_NO_GAHAN";
            popUp("OWNER_MEMBER_NO_GAHAN");
        } else {
            viewType = "OWNER_MEMBER_NO";
            popUp("OWNER_MEMBER_NO");
        }
    }
 
    private void txtOwnerMemNoFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if (txtOwnerMemNo.getText().length() > 0) {
            HashMap listMap = new HashMap();
            listMap.put("MEMBERSHIP_NO", txtOwnerMemNo.getText());
            java.util.List lst = ClientUtil.executeQuery("getMemeberShipDetails", listMap);
            if (lst != null && lst.size() > 0) {
                listMap = (HashMap) lst.get(0);
                viewType = "OWNER_MEMBER_NO";
                fillData(listMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                resetCollateralDetails();
                observable.setTxtOwnerMemNo("");
            }
        }
    }
    
    
   private HashMap serviceTaxAmount(String desc) { // Added by nithya on 30-12-2019 for KD-1131
        HashMap checkForTaxMap = new HashMap();       
            String scheme = prodDesc;
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", scheme);
            whereMap.put("CHARGE_DESC", desc);
            String retStr = "";
            List resultList = ClientUtil.executeQuery("getCheckServiceTaxApplicable", whereMap);
            HashMap checkMap = new HashMap();
            if (resultList != null && resultList.size() > 0) {
                checkMap = (HashMap) resultList.get(0);
                if (checkMap != null && checkMap.containsKey("SERVICE_TAX_APPLICABLE") && checkMap.containsKey("SERVICE_TAX_ID")) {
                    retStr = CommonUtil.convertObjToStr(checkMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE", checkMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID", checkMap.get("SERVICE_TAX_ID"));
                }
            }        
        return checkForTaxMap;
    }
    
    private void calculateServiceTax() { // Added by nithya on 30-12-2019 for KD-1131
        List taxSettingsList = new ArrayList();
        HashMap checkForTaxMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0 && table.getRowCount() > 0) {
            for (int i = 0; i < table.getRowCount(); i++) {
                if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
                    double chrgamt = 0;
                    checkForTaxMap = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
                    if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                        if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                            HashMap serviceTaSettingsMap = new HashMap();
                            chrgamt = CommonUtil.convertObjToDouble(table.getValueAt(i, 3));
                            if (chrgamt > 0) {
                                serviceTaSettingsMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                                serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                                taxSettingsList.add(serviceTaSettingsMap);
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("taxSettingsList :: " + taxSettingsList);
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curr_dt.clone());
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    lblServiceTaxval.setText(amt);
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    double transAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()) + CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    transactionUI.setCallingAmount(CommonUtil.convertObjToStr(transAmt));               
                } else {
                    lblServiceTaxval.setText("0.00");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            lblServiceTaxval.setText("0.00");
        }
    }
    
    
     // Added by nithya on 07-03-2020 for KD-1379
     private void btnGoldSecurityIdSearchActionPerformed(java.awt.event.ActionEvent evt) {       
       viewType = "GOLD_SECURITY_STOCK";
       popUp("GOLD_SECURITY_STOCK");
    }
     
     private void UpdateCalculatedGoldSecurityValue(HashMap map) {
        System.out.println("map####" + map);
        double pledgeAmt = 0;
        double actualPledge = 0;
        List lst = ClientUtil.executeQuery("getUnAuthGoldStockSecurityDetails", map);
        if (lst != null && lst.size() > 0) {
            ClientUtil.displayAlert("Already record is pending for authorization for this member");
            return;
        }        
        actualPledge = checkAvailableGoldStockSecurity(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        Double gahanLnAmt = getGoldStockPldgeAmountForLoan(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        actualPledge = actualPledge - gahanLnAmt;
        Double gahanMdsAmt = getGoldStockPldgeAmtForMds(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        actualPledge = actualPledge - gahanMdsAmt;
        pledgeAmt = getGoldStockAvailableSecurity(actualPledge);
        Double sanAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText().toString());   
        double totalSecurity = calculateSecurityAmount();
        sanAmt = sanAmt - totalSecurity;
        if (sanAmt < pledgeAmt) {
            txtValueOfGold.setText(String.valueOf(sanAmt));
        } else {
            txtValueOfGold.setText(String.valueOf(pledgeAmt));
        }       
    }
     
     private double checkAvailableGoldStockSecurity(String goldSecurityId) {
        HashMap map = new HashMap();
        double availableSecurity = 0;
        if (goldSecurityId != null) {
            map.put("GOLD_SECURITY_ID", goldSecurityId);
            List lst = ClientUtil.executeQuery("getGoldStockAvailableSecurityforLoan", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                availableSecurity = CommonUtil.convertObjToDouble(map.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            }
        }
        return availableSecurity;
    }
     
     public double getGoldStockPldgeAmountForLoan(String goldStockId) {
        Double gahanForLn = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("GOLD_SECURITY_ID", goldStockId);
        List lstGahanForLn = ClientUtil.executeQuery("getSelectGoldStockExistsForLoan", mapDocGenId);
        if (!lstGahanForLn.isEmpty() && !lstGahanForLn.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForLn.get(0);
            gahanForLn = CommonUtil.convertObjToDouble(mapDocGenId.get("PLEDGE_AMOUNT"));
        }
        return gahanForLn;
    }
     
     private double calculateSecurityAmount() {
        double netWorth = 0.0;
        double memNetWorth = 0.0;
        double pledgeAmount = 0.0;
        double losAmount = 0.0;
        double vehicleNetworth = 0.0;
        HashMap hashmap = new HashMap();
        double depAmount = 0.0;
        for (int i = 0; i < tblSalary.getRowCount(); i++) {
            netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalary.getValueAt(i, 5)).doubleValue();
        }
        for (int j = 0; j < tblMemberType.getRowCount(); j++) {
            memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
        }
        for (int k = 0; k < tblCollateral.getRowCount(); k++) {
            pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
        }
        for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
            String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
            String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
            String securityAmt = txtLimitAmount.getText();            
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", depNo);
            List lst = null;
            List EditLst = null;
            if (prodtype.equals("TD") || prodtype.equals("Deposits")) {              
                lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);               
                if (lst != null && lst.size() > 0) {
                    hmap = (HashMap) lst.get(0);
                    depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                }
                if (EditLst != null && EditLst.size() > 0) {
                    hmap = (HashMap) EditLst.get(0);
                    depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                }
            } else {
                depAmount += CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
            }
        }
        for (int m = 0; m < tblLosDetails.getRowCount(); m++) {
            losAmount = losAmount + CommonUtil.convertObjToDouble(tblLosDetails.getValueAt(m, 4)).doubleValue();
        }
        for (int j = 0; j < tblVehicleType.getRowCount(); j++) {
            vehicleNetworth = vehicleNetworth + CommonUtil.convertObjToDouble(tblVehicleType.getValueAt(j, 5)).doubleValue();

        }
        double tot = netWorth + memNetWorth + pledgeAmount + depAmount + losAmount + vehicleNetworth;
        return tot;
    }
     
     
     public double getGoldStockPldgeAmtForMds(String goldSecurityId) {
        Double gahanForMDS = 0.0;
        HashMap mapgoldSecurityId = new HashMap();
        mapgoldSecurityId.put("GOLD_SECURITY_ID", goldSecurityId);
        List lstGoldSecurityForMds = ClientUtil.executeQuery("getSelectGoldStockExistsForMds", mapgoldSecurityId);
        if (!lstGoldSecurityForMds.isEmpty() && !lstGoldSecurityForMds.equals(null)) {
            mapgoldSecurityId = new HashMap();
            mapgoldSecurityId = (HashMap) lstGoldSecurityForMds.get(0);
            gahanForMDS = CommonUtil.convertObjToDouble(mapgoldSecurityId.get("AMOUNT"));
        }
        return gahanForMDS;
    }
     
    private double getGoldStockAvailableSecurity(double maxsecurityAmt) {
        double availableSecuirty = 0;
        double sumGahanTableValue = 0;
        double loanAmt = 0;        
        sumGahanTableValue += CommonUtil.convertObjToDouble(txtValueOfGold.getText());          
        loanAmt = CommonUtil.convertObjToDouble(txtLimitAmount.getText()).doubleValue();
        availableSecuirty = loanAmt - sumGahanTableValue;
        return maxsecurityAmt;
    } 
    private void clearGoldSecurityFields(){
        txtGoldSecurityId.setText("");
        txtGrossWeight.setText("");
        txtNetWeight.setText("");
        txtValueOfGold.setText("");
        txtJewelleryDetails.setText("");
        txtGoldRemarks.setText("");
        
        txtGoldSecurityId.setEnabled(false);
        txtGrossWeight.setEnabled(true);
        txtNetWeight.setEnabled(true);
        txtValueOfGold.setEnabled(true);
        txtJewelleryDetails.setEnabled(true);
        txtGoldRemarks.setEnabled(true);
    }
     
    private void rdoGoldSecurityExitsYesActionPerformed(java.awt.event.ActionEvent evt) {
        if (rdoGoldSecurityExitsYes.isSelected()) {
            rdoGoldSecurityExitsNo.setSelected(false);   
            txtGoldRemarks.setText("CUSTOMER GOLD STOCK");
            ClientUtil.enableDisable(panGoldTypeDetails, false);
            btnGoldSecurityIdSearch.setEnabled(true); 
            rdoGoldSecurityExitsNo.setEnabled(true);
            txtJewelleryDetails.setText(observable.getTxtJewelleryDetails());
            txtGrossWeight.setText(observable.getTxtGrossWeight());
            txtNetWeight.setText(observable.getTxtNetWeight());
            txtValueOfGold.setText(observable.getTxtValueOfGold());            
        } else {
           rdoGoldSecurityExitsNo.setSelected(true);
           btnGoldSecurityIdSearch.setEnabled(false); 
           clearGoldSecurityFields();
        }
    } 
    
    private void rdoGoldSecurityExitsNoActionPerformed(java.awt.event.ActionEvent evt) {       
        if (rdoGoldSecurityExitsNo.isSelected()) {
            rdoGoldSecurityExitsYes.setSelected(false);
            rdoGoldSecurityExitsYes.setEnabled(true);
            btnGoldSecurityIdSearch.setEnabled(false);  
            clearGoldSecurityFields();
        } else {
           rdoGoldSecurityExitsYes.setSelected(true);      
           txtGoldRemarks.setText("CUSTOMER GOLD STOCK");
           ClientUtil.enableDisable(panGoldTypeDetails, false);
           btnGoldSecurityIdSearch.setEnabled(true);
           rdoGoldSecurityExitsYes.setEnabled(true);           
        }
    }
    
    private void enabledisableGoldStockData(){ // Added by nithya on 07-03-2020 for KD-1379
        if(observable.getRdoGoldSecurityStockExists().equalsIgnoreCase("Y")){
            rdoGoldSecurityExitsYesActionPerformed(null);
        }
    }   
     
    // End  

     
   private void initRunSecurityComponents() {
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        tabMasterMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panAllSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalaryCertificateNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryRemark = new com.see.truetransact.uicomponent.CLabel();
        lblEmployerName = new com.see.truetransact.uicomponent.CLabel();
        lblTotalSalary = new com.see.truetransact.uicomponent.CLabel();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNum = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblRetirementDt = new com.see.truetransact.uicomponent.CLabel();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        lblNetWorth1 = new com.see.truetransact.uicomponent.CLabel();
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        txtMemberNum = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryRemark = new com.see.truetransact.uicomponent.CTextField();
        txtDesignation = new com.see.truetransact.uicomponent.CTextField();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        txtEmployerName = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryCertificateNo = new com.see.truetransact.uicomponent.CTextField();
        txtTotalSalary = new com.see.truetransact.uicomponent.CTextField();
        txtNetWorth1 = new com.see.truetransact.uicomponent.CTextField();
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        tdtRetirementDt = new com.see.truetransact.uicomponent.CDateField();
        panMemberTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberTypeTable = new com.see.truetransact.uicomponent.CPanel();
        panSalaryTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberType = new com.see.truetransact.uicomponent.CScrollPane();
        srpSalary = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberType = new com.see.truetransact.uicomponent.CTable();
        tblSalary = new com.see.truetransact.uicomponent.CTable();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGahanYesNo = new com.see.truetransact.uicomponent.CLabel();
        panGahanYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoGahanYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanNo = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        lblMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemName = new com.see.truetransact.uicomponent.CLabel();
        lblMemType = new com.see.truetransact.uicomponent.CLabel();
        lblMemNetworth = new com.see.truetransact.uicomponent.CLabel();
        txtMemNetworth = new com.see.truetransact.uicomponent.CTextField();
        lblMemPriority = new com.see.truetransact.uicomponent.CLabel();
        txtMemPriority = new com.see.truetransact.uicomponent.CTextField();
        txtContactNum = new com.see.truetransact.uicomponent.CTextField();
        txtContactNum.setAllowNumber(true);
        lblContactNum = new com.see.truetransact.uicomponent.CLabel();
        txtMemType = new com.see.truetransact.uicomponent.CTextField();
        txtMemName = new com.see.truetransact.uicomponent.CTextField();
        panMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        txtMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemNo = new com.see.truetransact.uicomponent.CButton();
        panBtnMemberType = new com.see.truetransact.uicomponent.CPanel();
        panBtnSalaryType = new com.see.truetransact.uicomponent.CPanel();
        btnMemberNew = new com.see.truetransact.uicomponent.CButton();
        btnMemberSave = new com.see.truetransact.uicomponent.CButton();
        btnMemberDelete = new com.see.truetransact.uicomponent.CButton();
        btnSalaryNew = new com.see.truetransact.uicomponent.CButton();
        btnSalarySave = new com.see.truetransact.uicomponent.CButton();
        btnSalaryDelete = new com.see.truetransact.uicomponent.CButton();
        panCollateralTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panCollateralTable = new com.see.truetransact.uicomponent.CPanel();
        panCollateralJointTable = new com.see.truetransact.uicomponent.CPanel();
        srpCollateralTable = new com.see.truetransact.uicomponent.CScrollPane();
        srpCollateralJointTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCollateral = new com.see.truetransact.uicomponent.CTable();
        tblJointCollateral = new com.see.truetransact.uicomponent.CTable();
        panCollatetalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblOwnerMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtDocumentNo = new com.see.truetransact.uicomponent.CTextField();
        lblDocumentType = new com.see.truetransact.uicomponent.CLabel();
        cboDocumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblDocumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblRegisteredOffice = new com.see.truetransact.uicomponent.CLabel();
        txtRegisteredOffice = new com.see.truetransact.uicomponent.CTextField();
        lblOwnerMemberNname = new com.see.truetransact.uicomponent.CLabel();
        txtOwnerMemberNname = new com.see.truetransact.uicomponent.CTextField();
        tdtPledgeDate = new com.see.truetransact.uicomponent.CDateField();
        lblPledgeDate = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeNo = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeNo = new com.see.truetransact.uicomponent.CTextField();
        lblPledge = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        txtVillage = new com.see.truetransact.uicomponent.CTextField();
        lblSurveyNo = new com.see.truetransact.uicomponent.CLabel();
        txtSurveyNo = new com.see.truetransact.uicomponent.CTextField();
        lblRight = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeType = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeType = new com.see.truetransact.uicomponent.CTextField();
        lblPledgeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtDocumentDate = new com.see.truetransact.uicomponent.CDateField();

        lblGoldSecurityExists = new com.see.truetransact.uicomponent.CLabel();
        lblGoldSecurityId = new com.see.truetransact.uicomponent.CLabel();
        lblJewelleryDetails = new com.see.truetransact.uicomponent.CLabel();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        lblValueOfGold = new com.see.truetransact.uicomponent.CLabel();
        lblGoldRemarks = new com.see.truetransact.uicomponent.CLabel();
        
        rdoGoldSecurityExitsYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGoldSecurityExitsNo = new com.see.truetransact.uicomponent.CRadioButton();

        txtGoldSecurityId = new com.see.truetransact.uicomponent.CTextField();
        txtJewelleryDetails = new com.see.truetransact.uicomponent.CTextArea();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        txtInstallAmount = new com.see.truetransact.uicomponent.CTextField();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        txtValueOfGold = new com.see.truetransact.uicomponent.CTextField();
        txtGoldRemarks = new com.see.truetransact.uicomponent.CTextField();
        
        btnGoldSecurityIdSearch = new com.see.truetransact.uicomponent.CButton();
        panGoldSecurityYesNo = new com.see.truetransact.uicomponent.CPanel();
        panGoldSecurityId = new com.see.truetransact.uicomponent.CPanel(); 
        
        panGoldTypeDetails = new com.see.truetransact.uicomponent.CPanel();
//        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboNature = new com.see.truetransact.uicomponent.CComboBox();
        lblTotalArea = new com.see.truetransact.uicomponent.CLabel();
        txtTotalArea = new com.see.truetransact.uicomponent.CTextField();
        panBtnCollateralType = new com.see.truetransact.uicomponent.CPanel();
        btnCollateralNew = new com.see.truetransact.uicomponent.CButton();
        btnCollateralSave = new com.see.truetransact.uicomponent.CButton();
        btnCollateralDelete = new com.see.truetransact.uicomponent.CButton();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panOwnerMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        panDocumentNumber = new com.see.truetransact.uicomponent.CPanel();
        txtOwnerMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnOwnerMemNo = new com.see.truetransact.uicomponent.CButton();
        btnDocumentNo = new com.see.truetransact.uicomponent.CButton();
        cboPledge = new com.see.truetransact.uicomponent.CComboBox();
        lblNature = new com.see.truetransact.uicomponent.CLabel();
        cboRight = new com.see.truetransact.uicomponent.CComboBox();

        panOtherSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDepAmount = new com.see.truetransact.uicomponent.CLabel();
        lblProductId2 = new com.see.truetransact.uicomponent.CLabel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblDepDt = new com.see.truetransact.uicomponent.CLabel();
        txtMaturityValue = new com.see.truetransact.uicomponent.CTextField();
        txtDepAmount = new com.see.truetransact.uicomponent.CTextField();
        txtRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        lblMaturityDt = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDepDt = new com.see.truetransact.uicomponent.CDateField();
        btnDepositNew = new com.see.truetransact.uicomponent.CButton();
        btnDepositSave = new com.see.truetransact.uicomponent.CButton();
        btnDepositDelete = new com.see.truetransact.uicomponent.CButton();
        panBtnDeposit = new com.see.truetransact.uicomponent.CPanel();
        txtMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        cboDepProdType = new com.see.truetransact.uicomponent.CComboBox();
        panDepNo = new com.see.truetransact.uicomponent.CPanel();
        btnDepNo = new com.see.truetransact.uicomponent.CButton();
        lblProductTypeSecurity = new com.see.truetransact.uicomponent.CLabel();
        cboProductTypeSecurity = new com.see.truetransact.uicomponent.CComboBox();
        panDepositType = new com.see.truetransact.uicomponent.CPanel();
        panDepositTable = new com.see.truetransact.uicomponent.CPanel();
        srpTableDeposit = new com.see.truetransact.uicomponent.CScrollPane();
        lblTotalDeposit = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDepositValue = new com.see.truetransact.uicomponent.CLabel();
        txtDepNo = new com.see.truetransact.uicomponent.CTextField();
        tblDepositDetails = new com.see.truetransact.uicomponent.CTable();


        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLosName = new com.see.truetransact.uicomponent.CLabel();
        lblLosOtherInstitution = new com.see.truetransact.uicomponent.CLabel();
        lblLosSecurityNo = new com.see.truetransact.uicomponent.CLabel();
        lblLosSecurityType = new com.see.truetransact.uicomponent.CLabel();
        txtLosName = new com.see.truetransact.uicomponent.CTextField();
        txtLosSecurityNo = new com.see.truetransact.uicomponent.CTextField();
        txtLosMaturityValue = new com.see.truetransact.uicomponent.CTextField();
        lblLosIssueDate = new com.see.truetransact.uicomponent.CLabel();
        lblLosMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblLosMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        tdtLosIssueDate = new com.see.truetransact.uicomponent.CDateField();
        btnLosNew = new com.see.truetransact.uicomponent.CButton();
        btnLosSave = new com.see.truetransact.uicomponent.CButton();
        btnLosDelete = new com.see.truetransact.uicomponent.CButton();
        panLosDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtLosMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        cboLosOtherInstitution = new com.see.truetransact.uicomponent.CComboBox();

        lblLosRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboLosSecurityType = new com.see.truetransact.uicomponent.CComboBox();
        panLosBtn = new com.see.truetransact.uicomponent.CPanel();
        panLosDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtLosMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        srpTableLos = new com.see.truetransact.uicomponent.CScrollPane();
        tblLosDetails = new com.see.truetransact.uicomponent.CTable();
        lblLosAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLosAmount = new com.see.truetransact.uicomponent.CTextField();
        txtLosRemarks = new com.see.truetransact.uicomponent.CTextField();
        cboLosOtherInstitution = new com.see.truetransact.uicomponent.CComboBox();
        panLosTable = new com.see.truetransact.uicomponent.CPanel();
        tblVehicleType = new com.see.truetransact.uicomponent.CTable();
        panVehicleDetails = new com.see.truetransact.uicomponent.CPanel();
        panVehicleNumber = new com.see.truetransact.uicomponent.CPanel();
        txtVehicleContactNum = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleContactNum.setAllowNumber(true);
        lblVehicleContactNum = new com.see.truetransact.uicomponent.CLabel();
        btnVehicleMemNo = new com.see.truetransact.uicomponent.CButton();
        btnVehicleSave = new com.see.truetransact.uicomponent.CButton();
        btnVehicleNew = new com.see.truetransact.uicomponent.CButton();
        btnVehicleDelete = new com.see.truetransact.uicomponent.CButton();
        txtVehicleDetals = new com.see.truetransact.uicomponent.CTextArea();
        txtVehicleType = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNo = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNo.setAllowAll(true);
        txtVehicleRcBookNo = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleRcBookNo.setAllowAll(true);
        txtVehicleDate = new com.see.truetransact.uicomponent.CDateField(); 
        srpTxtAreaVehicledtails = new com.see.truetransact.uicomponent.CScrollPane();
        lblVehicleMemType = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleNo = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleType= new com.see.truetransact.uicomponent.CLabel();
        lblVehicleRcBookNo= new com.see.truetransact.uicomponent.CLabel();
        lblVehicleDate = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleDetails = new com.see.truetransact.uicomponent.CLabel();
        panBtnVehicleType = new com.see.truetransact.uicomponent.CPanel();
        lblVehicleMemRetireDate = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemberNum = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemName = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleNetWorth = new com.see.truetransact.uicomponent.CLabel();
        lblTotalVehicleMemSal = new com.see.truetransact.uicomponent.CLabel();
        txtVehicleMemberNum = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemberNum.setAllowNumber(true);
        txtVehicleNetWorth = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNetWorth.setAllowNumber(true);
        txtVehicleMemSal = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemSal.setAllowNumber(true);
        txtVehicleMemberName=new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemType=new com.see.truetransact.uicomponent.CTextField();
        srpVehicleType = new com.see.truetransact.uicomponent.CScrollPane();
        panVehicleTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panVehicleTypeTable = new com.see.truetransact.uicomponent.CPanel();
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 520));
        tabMasterMaintenance.setTabPlacement(javax.swing.JTabbedPane.TOP);
        tabMasterMaintenance.setMinimumSize(new java.awt.Dimension(400, 520));
        tabMasterMaintenance.setPreferredSize(new java.awt.Dimension(400, 520));
        //--------------------------------------------------------------------------salary
        panAllSalaryDetails.setLayout(new java.awt.GridBagLayout());
        panAllSalaryDetails.setMinimumSize(new java.awt.Dimension(400, 650));
        panAllSalaryDetails.setPreferredSize(new java.awt.Dimension(400, 650));

        panSalaryDetails.setLayout(new java.awt.GridBagLayout());
        panSalaryDetails.setMinimumSize(new java.awt.Dimension(250, 650));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(200, 650));

        lblSalaryCertificateNo.setText("Salary Certificate No");
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblSalaryCertificateNo, gridBagConstraints);

        txtSalaryCertificateNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtSalaryCertificateNo, gridBagConstraints);



        lblEmployerName.setText("Employer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblEmployerName, gridBagConstraints);

        txtEmployerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtEmployerName, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblAddress, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtAddress, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(cboCity, gridBagConstraints);

        lblPinCode.setText("PinCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblPinCode, gridBagConstraints);

        txtPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtPinCode, gridBagConstraints);

        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblDesignation, gridBagConstraints);

        txtDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtDesignation, gridBagConstraints);

        lblContactNo.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblContactNo, gridBagConstraints);

        txtContactNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtContactNo, gridBagConstraints);

        lblRetirementDt.setText("Date Of Retirement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblRetirementDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(tdtRetirementDt, gridBagConstraints);

        tdtRetirementDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRetirementDtFocusLost(evt);
            }
        });


        lblMemberNum.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblMemberNum, gridBagConstraints);

        txtMemberNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtMemberNum, gridBagConstraints);


        lblTotalSalary.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblTotalSalary, gridBagConstraints);

        txtTotalSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtTotalSalary, gridBagConstraints);

        lblNetWorth1.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblNetWorth1, gridBagConstraints);

        txtNetWorth1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtNetWorth1, gridBagConstraints);

        lblSalaryRemark.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblSalaryRemark, gridBagConstraints);

        txtSalaryRemark.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtSalaryRemark, gridBagConstraints);


        panBtnSalaryType.setLayout(new java.awt.GridBagLayout());

        panBtnSalaryType.setMinimumSize(new java.awt.Dimension(105, 35));
        panBtnSalaryType.setPreferredSize(new java.awt.Dimension(105, 35));
        btnSalaryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnSalaryNew.setToolTipText("New");
        btnSalaryNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalaryNew, gridBagConstraints);

        btnSalarySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSalarySave.setToolTipText("Save");
        btnSalarySave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalarySave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalarySave.setName("btnContactNoAdd");
        btnSalarySave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalarySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalarySaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalarySave, gridBagConstraints);

        btnSalaryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnSalaryDelete.setToolTipText("Delete");
        btnSalaryDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalaryDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = gridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 150, 0);
        panSalaryDetails.add(panBtnSalaryType, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panAllSalaryDetails.add(panSalaryDetails, gridBagConstraints);

        //------------------table
//                panMemberTypeDetails.setLayout(new java.awt.GridBagLayout());
//
//        panMemberTypeDetails.setMinimumSize(new java.awt.Dimension(300, 300));
//        panMemberTypeDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panSalaryTable.setLayout(new java.awt.GridBagLayout());

        panSalaryTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panSalaryTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpSalary.setMinimumSize(new java.awt.Dimension(450, 200));
        srpSalary.setPreferredSize(new java.awt.Dimension(450, 200));
        tblSalary.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Slno", "Certificate No", "Member No", "Name", "Contact No", "Networth"
        }));
        tblSalary.setMinimumSize(new java.awt.Dimension(375, 750));
        tblSalary.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblSalary.setPreferredSize(new java.awt.Dimension(375, 750));
        tblSalary.setOpaque(false);
        tblSalary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalaryMousePressed(evt);
            }
        });

        srpSalary.setViewportView(tblSalary);

        panSalaryTable.add(srpSalary, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panAllSalaryDetails.add(panSalaryTable, gridBagConstraints);
//        panSalaryDetails.add(panBtnSalaryType);
        tabMasterMaintenance.addTab("Salary Details", panAllSalaryDetails);
//-------------------------------------------------------------------------------------------------------------------------------salary
        panMemberTypeDetails.setLayout(new java.awt.GridBagLayout());

        panMemberTypeDetails.setMinimumSize(new java.awt.Dimension(300, 300));
        panMemberTypeDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panMemberTypeTable.setLayout(new java.awt.GridBagLayout());

        panMemberTypeTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panMemberTypeTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpMemberType.setMinimumSize(new java.awt.Dimension(450, 200));
        srpMemberType.setPreferredSize(new java.awt.Dimension(450, 200));
        tblMemberType.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Member No", "Name", "Type of Member", "Contact No", "Networth"
        }));
        tblMemberType.setMinimumSize(new java.awt.Dimension(375, 750));
        tblMemberType.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblMemberType.setPreferredSize(new java.awt.Dimension(375, 750));
        tblMemberType.setOpaque(false);
        tblMemberType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMemberTypeMousePressed(evt);
            }
        });

        srpMemberType.setViewportView(tblMemberType);

        panMemberTypeTable.add(srpMemberType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panMemberTypeDetails.add(panMemberTypeTable, gridBagConstraints);

        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        panMemberDetails.setBorder(new javax.swing.border.TitledBorder("Member Details"));
        panMemberDetails.setMinimumSize(new java.awt.Dimension(275, 200));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(275, 210));
        panMemberDetails.setRequestFocusEnabled(false);
        lblMemNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNo, gridBagConstraints);

        lblMemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemName, gridBagConstraints);

        lblMemType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemType, gridBagConstraints);

        lblMemNetworth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNetworth, gridBagConstraints);

        lblMemPriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemPriority, gridBagConstraints);        
        
        txtMemPriority.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemPriority, gridBagConstraints);

        txtMemNetworth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemNetworth, gridBagConstraints);

        txtContactNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtContactNum, gridBagConstraints);

        lblContactNum.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblContactNum, gridBagConstraints);

        txtMemType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemType, gridBagConstraints);

        txtMemName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemName, gridBagConstraints);

        panMemberNumber.setLayout(new java.awt.GridBagLayout());

        txtMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(txtMemNo, gridBagConstraints);

        btnMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemNo.setEnabled(false);
        btnMemNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(btnMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(panMemberNumber, gridBagConstraints);

        panBtnMemberType.setLayout(new java.awt.GridBagLayout());

        panBtnMemberType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnMemberType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnMemberNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnMemberNew.setToolTipText("New");
        btnMemberNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberNew, gridBagConstraints);

        btnMemberSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnMemberSave.setToolTipText("Save");
        btnMemberSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setName("btnContactNoAdd");
        btnMemberSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberSave, gridBagConstraints);

        btnMemberDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnMemberDelete.setToolTipText("Delete");
        btnMemberDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panMemberDetails.add(panBtnMemberType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 12, 4);
        panMemberTypeDetails.add(panMemberDetails, gridBagConstraints);

        tabMasterMaintenance.addTab("Member Type", panMemberTypeDetails);

        panCollateralTypeDetails.setLayout(new java.awt.GridBagLayout());
        //-------------------------------------------------table started-----------------------------one more table started
        panCollateralJointTable.setLayout(new java.awt.GridBagLayout());

        panCollateralJointTable.setMinimumSize(new java.awt.Dimension(460, 220));
        panCollateralJointTable.setPreferredSize(new java.awt.Dimension(460, 220));
        srpCollateralJointTable.setMinimumSize(new java.awt.Dimension(450, 180));
        srpCollateralJointTable.setPreferredSize(new java.awt.Dimension(450, 180));
        tblJointCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Cust Id", "Name", "Constitution"
        }));
        tblJointCollateral.setMinimumSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblJointCollateral.setPreferredSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setOpaque(false);
        tblCollateral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCollateralMousePressed(evt);
            }
        });

        srpCollateralJointTable.setViewportView(tblJointCollateral);

        panCollateralJointTable.add(srpCollateralJointTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panCollateralTypeDetails.add(panCollateralJointTable, gridBagConstraints);
        ////////////////// above mine
        panCollateralTable.setLayout(new java.awt.GridBagLayout());

        panCollateralTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panCollateralTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpCollateralTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpCollateralTable.setPreferredSize(new java.awt.Dimension(450, 200));
        tblCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Member No", "Name", "Doc No", "PledgeAmt", "SurveyNo", "TotalArea"
        }));
        tblCollateral.setMinimumSize(new java.awt.Dimension(375, 750));
        tblCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblCollateral.setPreferredSize(new java.awt.Dimension(375, 750));
        tblCollateral.setOpaque(false);
        tblCollateral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCollateralMousePressed(evt);
            }
        });

        srpCollateralTable.setViewportView(tblCollateral);

        panCollateralTable.add(srpCollateralTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panCollateralTypeDetails.add(panCollateralTable, gridBagConstraints);

        panCollatetalDetails.setLayout(new java.awt.GridBagLayout());
        /*  ------------------------------------------------------------- gahan --------------------------------------------------------------------------------------------------------------*/
        panCollatetalDetails.setMinimumSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setPreferredSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setRequestFocusEnabled(false);
        lblGahanYesNo.setText("Gahan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(lblGahanYesNo, gridBagConstraints);

        panGahanYesNo.setLayout(new java.awt.GridBagLayout());
        rdoGahanYes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanYes, gridBagConstraints);
//        rdoGahanYes.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                rdoGahanYesActionPerformed(evt);
//            }
//        });
        rdoGahanNo.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanNo, gridBagConstraints);
//        rdoGahanNo.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                rdoGahanNoActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(panGahanYesNo, gridBagConstraints);

        //        panCollatetalDetails.setMinimumSize(new java.awt.Dimension(400, 500));
        //        panCollatetalDetails.setPreferredSize(new java.awt.Dimension(400, 500));
        //        panCollatetalDetails.setRequestFocusEnabled(false);
        lblOwnerMemberNo.setText("Owner Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNo, gridBagConstraints);


        lblOwnerMemberNname.setText("Owner Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNname, gridBagConstraints);

        txtOwnerMemberNname.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtOwnerMemberNname, gridBagConstraints);


        lblDocumentNo.setText("Document No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentNo, gridBagConstraints);

        panDocumentNumber.setLayout(new java.awt.GridBagLayout());

        txtDocumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;//1
        gridBagConstraints.gridy = 0;//3
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDocumentNumber.add(txtDocumentNo, gridBagConstraints);
//        txtDocumentNo.addFocusListener(new java.awt.event.FocusAdapter() {
//            public void focusLost(java.awt.event.FocusEvent evt) {
//                txtDocumentNoFocusLost(evt);
//            }
//        });

//        txtDocumentNo.addActionListener(new java.awt.event. ActionListener(){
//            public void actionPerformed(java.awt.event.ActionEvent evt){
//                txtDocumentNoActionPerformed(evt);
//            }
//        });

        btnDocumentNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDocumentNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setEnabled(false);
        btnDocumentNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panDocumentNumber.add(btnDocumentNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panCollatetalDetails.add(panDocumentNumber, gridBagConstraints);

        lblDocumentType.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentType, gridBagConstraints);

        cboDocumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboDocumentType, gridBagConstraints);

        lblDocumentDate.setText("Document Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentDate, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtDocumentDate, gridBagConstraints);

        lblRegisteredOffice.setText("Registered Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblRegisteredOffice, gridBagConstraints);

        txtRegisteredOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtRegisteredOffice, gridBagConstraints);


        // ------------------------------------------
        lblPledgeNo.setText("Pledge No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeNo, gridBagConstraints);

        txtPledgeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeNo, gridBagConstraints);

        lblPledgeDate.setText("Pledge Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeDate, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtPledgeDate, gridBagConstraints);


        // ------------------------end----------------- -->


        lblPledgeType.setText("Pledge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeType, gridBagConstraints);

        cboPledge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboPledge, gridBagConstraints);






        //            lblPledge.setText("Pledge");
        //            gridBagConstraints = new java.awt.GridBagConstraints();
        //            gridBagConstraints.gridx = 0;
        //            gridBagConstraints.gridy = 9;
        //            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        //            gridBagConstraints.insets =  new java.awt.Insets(2, 2, 2, 2);
        //            panCollatetalDetails.add(lblPledge, gridBagConstraints);

        lblPledgeAmount.setText("Pledge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeAmount, gridBagConstraints);

        txtPledgeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
//        txtPledgeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
//            public void focusLost(java.awt.event.FocusEvent evt) {
//                txtPledgeAmountFocusLost(evt);
//            }
//        });
//        txtPledgeAmount.addActionListener( new java.awt.event.ActionListener(){
//           public void actionPerformed(java.awt.event.ActionEvent evt) {
////                btnDocumentNoActionPerformed(evt);
//                txtPledgeAmountActionPerformed(evt);
//            }
//        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeAmount, gridBagConstraints);


        //            -----------------

        lblVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblVillage, gridBagConstraints);

        txtVillage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtVillage, gridBagConstraints);

        lblSurveyNo.setText("Survey No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblSurveyNo, gridBagConstraints);

        txtSurveyNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtSurveyNo, gridBagConstraints);


        lblTotalArea.setText("Total Area (In Cents)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblTotalArea, gridBagConstraints);

        txtTotalArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtTotalArea, gridBagConstraints);





//        lblRemarks.setText("Remarks");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 15;
//        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
//        panCollatetalDetails.add(lblRemarks, gridBagConstraints);

        lblNature.setText("Nature");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblNature, gridBagConstraints);


        cboNature.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboNature, gridBagConstraints);

        lblRight.setText("Right");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblRight, gridBagConstraints);

        cboRight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboRight, gridBagConstraints);

        //       ---------------------------------------------------------------------------------------------------
        panBtnCollateralType.setLayout(new java.awt.GridBagLayout());

        panBtnCollateralType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnCollateralType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnCollateralNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnCollateralNew.setToolTipText("New");
        btnCollateralNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralNew, gridBagConstraints);

        btnCollateralSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnCollateralSave.setToolTipText("Save");
        btnCollateralSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setName("btnContactNoAdd");
        btnCollateralSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralSave, gridBagConstraints);

        btnCollateralDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnCollateralDelete.setToolTipText("Delete");
        btnCollateralDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panCollatetalDetails.add(panBtnCollateralType, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(150, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(150, 45));
        txtAreaParticular.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setMaximumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(2, 14));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        panCollatetalDetails.add(srpTxtAreaParticulars, gridBagConstraints);

        panOwnerMemberNumber.setLayout(new java.awt.GridBagLayout());

        txtOwnerMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(txtOwnerMemNo, gridBagConstraints);

        btnOwnerMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnOwnerMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setEnabled(false);
        btnOwnerMemNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(btnOwnerMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(panOwnerMemberNumber, gridBagConstraints);

//        cboPledge.setMinimumSize(new java.awt.Dimension(100, 21));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 6;
//        gridBagConstraints.ipady = 2;
//        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
//        panCollatetalDetails.add(cboPledge, gridBagConstraints);




        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCollateralTypeDetails.add(panCollatetalDetails, gridBagConstraints);

        tabMasterMaintenance.addTab("Collateral Type", panCollateralTypeDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEmpTransfer.add(tabMasterMaintenance, gridBagConstraints);

        panSecurityDetails.add(panEmpTransfer, gridBagConstraints);

//        txtMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
//            public void focusLost(java.awt.event.FocusEvent evt) {
//                txtMemNoFocusLost(evt);
//            }
//        });
//
//        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
//            public void focusLost(java.awt.event.FocusEvent evt) {
//                txtOwnerMemNoFocusLost(evt);
//            }
//        });

        //Gold Type Security
        panGoldTypeDetails.setLayout(new java.awt.GridBagLayout());

        panGoldTypeDetails.setMinimumSize(new java.awt.Dimension(250, 300));
        panGoldTypeDetails.setPreferredSize(new java.awt.Dimension(250, 300));
        
         // Added by nithya on 07-03-2020 for KD-1379
        lblGoldSecurityExists.setText("Gold Security Exists");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGoldSecurityExists, gridBagConstraints);
        
        lblGoldSecurityId.setText("Gold Security Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGoldSecurityId, gridBagConstraints);
        
        /*End for gold stock details */        
        
        lblJewelleryDetails.setText("Jewellery Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblJewelleryDetails, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight(grams");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGrossWeight, gridBagConstraints);

        lblNetWeight.setText("Net Weight(grams)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblNetWeight, gridBagConstraints);

        lblValueOfGold.setText("Value of the Gold");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblValueOfGold, gridBagConstraints);

        lblGoldRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGoldRemarks, gridBagConstraints);

        txtGoldRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtGoldRemarks, gridBagConstraints);

        txtValueOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtValueOfGold, gridBagConstraints);
        txtValueOfGold.setValidation(new CurrencyValidation());

        txtNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtNetWeight, gridBagConstraints);

        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtGrossWeight, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(400, 60));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(400, 60));
        txtJewelleryDetails.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtJewelleryDetails.setLineWrap(true);

        srpTxtAreaParticulars.setViewportView(txtJewelleryDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panGoldTypeDetails.add(srpTxtAreaParticulars, gridBagConstraints);
        
        // For gold security stock
        panGoldSecurityId.setLayout(new java.awt.GridBagLayout()); 
        txtGoldSecurityId.setSize(100,21);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panGoldSecurityId.add(txtGoldSecurityId, gridBagConstraints); 
        btnGoldSecurityIdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnGoldSecurityIdSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGoldSecurityIdSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGoldSecurityIdSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panGoldSecurityId.add(btnGoldSecurityIdSearch, gridBagConstraints);
        btnGoldSecurityIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldSecurityIdSearchActionPerformed(evt);
            }
        });
        panGoldSecurityId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(panGoldSecurityId, gridBagConstraints);
        
        panGoldSecurityYesNo.setLayout(new java.awt.GridBagLayout());        
        rdoGoldSecurityExitsYes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panGoldSecurityYesNo.add(rdoGoldSecurityExitsYes, gridBagConstraints); 
        rdoGoldSecurityExitsYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGoldSecurityExitsYesActionPerformed(evt);
            }
        });
        rdoGoldSecurityExitsNo.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panGoldSecurityYesNo.add(rdoGoldSecurityExitsNo, gridBagConstraints);   
        rdoGoldSecurityExitsNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGoldSecurityExitsNoActionPerformed(evt);
            }
        });
        panGoldSecurityYesNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(panGoldSecurityYesNo, gridBagConstraints);
        
        // End // Added by nithya on 07-03-2020 for KD-1379

        txtGrossWeight.setValidation(new NumericValidation(3, 2));
        txtNetWeight.setValidation(new NumericValidation(3, 2));
        txtValueOfGold.setAllowAll(true);
        txtGoldRemarks.setAllowAll(true);
        txtInstallAmount.setAllowNumber(true);
        tabMasterMaintenance.addTab("Gold Type", panGoldTypeDetails);



        tabMasterMaintenance.addTab("Gold Type", panGoldTypeDetails);


        //DepositType security

        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panDepositType.setLayout(new java.awt.GridBagLayout());

        panDepositType.setMinimumSize(new java.awt.Dimension(440, 225));
        panDepositType.setPreferredSize(new java.awt.Dimension(440, 225));
        panDepositType.setRequestFocusEnabled(false);
        lblProductId2.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblProductId2, gridBagConstraints);

        lblDepAmount.setText("Dep Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 4);
        panDepositType.add(lblDepAmount, gridBagConstraints);

        lblRateOfInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblRateOfInterest, gridBagConstraints);

        lblDepDt.setText("Dep Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblDepDt, gridBagConstraints);

        txtDepAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 2);
        panDepositType.add(txtDepAmount, gridBagConstraints);

        txtMaturityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtMaturityValue, gridBagConstraints);

        txtRateOfInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtRateOfInterest, gridBagConstraints);

        lblMaturityDt.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblMaturityDt, gridBagConstraints);

        lblMaturityValue.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblMaturityValue, gridBagConstraints);

        lblDepNo.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblDepNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(tdtDepDt, gridBagConstraints);

        panBtnDeposit.setLayout(new java.awt.GridBagLayout());

        panBtnDeposit.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnDeposit.setPreferredSize(new java.awt.Dimension(95, 35));
        btnDepositNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
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
        panBtnDeposit.add(btnDepositNew, gridBagConstraints);

        btnDepositSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
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
        panBtnDeposit.add(btnDepositSave, gridBagConstraints);

        btnDepositDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
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
        panBtnDeposit.add(btnDepositDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        panDepositType.add(panBtnDeposit, gridBagConstraints);

        txtMaturityDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                //txtMaturityDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtMaturityDt, gridBagConstraints);

        cboDepProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboDepProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepProdType.setPopupWidth(165);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(cboDepProdType, gridBagConstraints);

        panDepNo.setLayout(new java.awt.GridBagLayout());

        txtDepNo.setAllowAll(true);
        txtDepNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepNo.add(txtDepNo, gridBagConstraints);

        btnDepNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDepNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepNo.setEnabled(false);
        btnDepNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panDepNo.add(btnDepNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDepositType.add(panDepNo, gridBagConstraints);

        lblProductTypeSecurity.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblProductTypeSecurity, gridBagConstraints);

        cboProductTypeSecurity.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboProductTypeSecurity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeSecurity.setPopupWidth(120);
        cboProductTypeSecurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeSecurityActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(cboProductTypeSecurity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDepositDetails.add(panDepositType, gridBagConstraints);

        panDepositTable.setLayout(new java.awt.GridBagLayout());

        panDepositTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panDepositTable.setPreferredSize(new java.awt.Dimension(380, 220));
        srpTableDeposit.setMinimumSize(new java.awt.Dimension(375, 160));
        srpTableDeposit.setPreferredSize(new java.awt.Dimension(375, 160));
        tblDepositDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Prod Type", "Dep No", "Dep Amt", "Matur Val"
        }));
        tblDepositDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblDepositDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.setOpaque(false);
        tblDepositDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDepositDetailsMousePressed(evt);
            }
        });

        srpTableDeposit.setViewportView(tblDepositDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panDepositTable.add(srpTableDeposit, gridBagConstraints);

        lblTotalDeposit.setText("Total Deposit Value ");
        lblTotalDeposit.setMinimumSize(new java.awt.Dimension(160, 18));
        lblTotalDeposit.setPreferredSize(new java.awt.Dimension(160, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 6);
        panDepositTable.add(lblTotalDeposit, gridBagConstraints);

        lblTotalDepositValue.setText("                          ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 5);
        panDepositTable.add(lblTotalDepositValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(panDepositTable, gridBagConstraints);

        tabMasterMaintenance.addTab("Deposit Type", panDepositDetails);
        //Other Security Details


        panOtherSecurityDetails.setLayout(new java.awt.GridBagLayout());

        panOtherSecurityDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panOtherSecurityDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panOtherSecurityDetails.setLayout(new java.awt.GridBagLayout());
        panLosDetails.setLayout(new java.awt.GridBagLayout());
        panLosDetails.setMinimumSize(new java.awt.Dimension(440, 225));
        panLosDetails.setPreferredSize(new java.awt.Dimension(440, 225));
        panLosDetails.setRequestFocusEnabled(false);
        lblLosOtherInstitution.setText("Other Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosOtherInstitution, gridBagConstraints);

        cboLosOtherInstitution.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboLosOtherInstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLosOtherInstitution.setPopupWidth(165);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(cboLosOtherInstitution, gridBagConstraints);

        lblLosName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosName, gridBagConstraints);

        txtLosName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panLosDetails.add(txtLosName, gridBagConstraints);

        lblLosSecurityType.setText("Security Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosSecurityType, gridBagConstraints);



        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panLosDetails.add(cboLosSecurityType, gridBagConstraints);

        lblLosSecurityNo.setText("Security No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosSecurityNo, gridBagConstraints);

        txtLosSecurityNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panLosDetails.add(txtLosSecurityNo, gridBagConstraints);
        txtLosSecurityNo.setAllowAll(true);

        lblLosAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 4);
        panLosDetails.add(lblLosAmount, gridBagConstraints);

        txtLosAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 2);
        panLosDetails.add(txtLosAmount, gridBagConstraints);
        txtLosAmount.setValidation(new CurrencyValidation());

        lblLosIssueDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panLosDetails.add(lblLosIssueDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(tdtLosIssueDate, gridBagConstraints);

        lblLosMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panLosDetails.add(lblLosMaturityDate, gridBagConstraints);

        tdtLosMaturityDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                //tdtLosMaturityDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(tdtLosMaturityDt, gridBagConstraints);

        lblLosMaturityValue.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosMaturityValue, gridBagConstraints);

        txtLosMaturityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(txtLosMaturityValue, gridBagConstraints);
        txtLosMaturityValue.setValidation(new CurrencyValidation());

        lblLosRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(lblLosRemarks, gridBagConstraints);



        txtLosRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(txtLosRemarks, gridBagConstraints);

        panLosBtn.setLayout(new java.awt.GridBagLayout());

        panLosBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panLosBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        btnLosNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnLosNew.setToolTipText("New");
        btnLosNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosNew.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnLosNew.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnLosNewActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosNew, gridBagConstraints);

        btnLosSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnLosSave.setToolTipText("Save");
        btnLosSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosSave.setName("btnContactNoAdd");
        btnLosSave.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnLosSave.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnLosSaveActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosSave, gridBagConstraints);

        btnLosDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnLosDelete.setToolTipText("Delete");
        btnLosDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosDelete.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnLosDelete.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnLosDeleteActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        panLosDetails.add(panLosBtn, gridBagConstraints);

        cboLosSecurityType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboLosSecurityType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLosSecurityType.setPopupWidth(120);
        cboLosSecurityType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // cboLoseSecurityTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOtherSecurityDetails.add(panLosDetails, gridBagConstraints);

        panLosTable.setLayout(new java.awt.GridBagLayout());

        panLosTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panLosTable.setPreferredSize(new java.awt.Dimension(380, 220));
        srpTableLos.setMinimumSize(new java.awt.Dimension(375, 160));
        srpTableLos.setPreferredSize(new java.awt.Dimension(375, 160));
        tblLosDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "OtherInstitution", "Name", "Security No", "SecurityType", "Amount"
        }));
        tblLosDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblLosDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblLosDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblLosDetails.setOpaque(false);
//        tblLosDetails.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                tblLosDetailsMousePressed(evt);
//            }
//        });

        srpTableLos.setViewportView(tblLosDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panLosTable.add(srpTableLos, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOtherSecurityDetails.add(panLosTable, gridBagConstraints);

        //tabMasterMaintenance.addTab("Other Society Details", panOtherSecurityDetails);
         // *****************************************************************************Vehichle********************************************
        panVehicleTypeDetails.setLayout(new java.awt.GridBagLayout());

        panVehicleTypeDetails.setMinimumSize(new java.awt.Dimension(900, 650));
        panVehicleTypeDetails.setPreferredSize(new java.awt.Dimension(900, 650));
        panVehicleTypeTable.setLayout(new java.awt.GridBagLayout());

        panVehicleTypeTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panVehicleTypeTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpVehicleType.setMinimumSize(new java.awt.Dimension(450, 200));
        srpVehicleType.setPreferredSize(new java.awt.Dimension(450, 200));
        tblVehicleType.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Member No", "Name", "Vehicle number","Vehicle type", "Contact No", "RC book number"
                }));
        tblVehicleType.setMinimumSize(new java.awt.Dimension(375, 750));
        tblVehicleType.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblVehicleType.setPreferredSize(new java.awt.Dimension(375, 750));
        tblVehicleType.setOpaque(false);
//        tblVehicleType.addMouseListener(new java.awt.event.MouseAdapter() {
//
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                tblVehicleTypeMousePressed(evt);
//            }
//        });

        srpVehicleType.setViewportView(tblVehicleType);

        panVehicleTypeTable.add(srpVehicleType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panVehicleTypeDetails.add(panVehicleTypeTable, gridBagConstraints);

        panVehicleDetails.setLayout(new java.awt.GridBagLayout());

        panVehicleDetails.setBorder(new javax.swing.border.TitledBorder("Vehicle Details"));
        panVehicleDetails.setMinimumSize(new java.awt.Dimension(300, 700));
        panVehicleDetails.setPreferredSize(new java.awt.Dimension(300, 700));
        panVehicleDetails.setRequestFocusEnabled(false);
        lblVehicleMemNo.setText("Vehicle Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemNo, gridBagConstraints);

        lblVehicleMemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemName, gridBagConstraints);
        lblVehicleMemRetireDate.setText("RetireMentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemRetireDate, gridBagConstraints);
        lblVehicleMemType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemType, gridBagConstraints);

        lblVehicleNo.setText("VehicleNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleNo, gridBagConstraints);
        
        txtVehicleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleNo, gridBagConstraints);
        
        lblVehicleType.setText("Vehicle Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleType, gridBagConstraints);
        
        txtVehicleType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleType, gridBagConstraints);
     
        lblVehicleDetails.setText("Vehicle Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleDetails, gridBagConstraints);
         
        srpTxtAreaVehicledtails.setMinimumSize(new java.awt.Dimension(150, 60));
        srpTxtAreaVehicledtails.setPreferredSize(new java.awt.Dimension(150, 60));
        txtVehicleDetals.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtVehicleDetals.setLineWrap(true);
        srpTxtAreaVehicledtails.setViewportView(txtVehicleDetals);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panVehicleDetails.add(srpTxtAreaVehicledtails, gridBagConstraints);

        
        lblVehicleRcBookNo.setText("Rc Book No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleRcBookNo, gridBagConstraints);
        
        txtVehicleRcBookNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleRcBookNo, gridBagConstraints);
        
        lblVehicleDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleDate, gridBagConstraints);
        
        txtVehicleDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleDate, gridBagConstraints);
        
        
        txtVehicleContactNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleContactNum, gridBagConstraints);

        lblVehicleContactNum.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleContactNum, gridBagConstraints);

        txtVehicleMemType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemType, gridBagConstraints);

        txtVehicleMemberName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemberName, gridBagConstraints);

        panVehicleNumber.setLayout(new java.awt.GridBagLayout());
        lblVehicleMemRetireDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(lblVehicleMemRetireDate, gridBagConstraints);

        panVehicleNumber.setLayout(new java.awt.GridBagLayout());
        txtVehicleMemberNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panVehicleNumber.add(txtVehicleMemberNum, gridBagConstraints);
        lblTotalVehicleMemSal.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblTotalVehicleMemSal, gridBagConstraints);

        txtVehicleMemSal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemSal, gridBagConstraints);
//        txtVehicleMemSal.addFocusListener(new java.awt.event.FocusAdapter() {
//
//            public void focusLost(java.awt.event.FocusEvent evt) {
//                txtVehicleMemSalFocusLost(evt);
//            }
//        });
        
        lblVehicleNetWorth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleNetWorth, gridBagConstraints);

        txtVehicleNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleNetWorth, gridBagConstraints);

        btnVehicleMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnVehicleMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setEnabled(false);
//        btnVehicleMemNo.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnVehicleMemNoActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panVehicleNumber.add(btnVehicleMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(panVehicleNumber, gridBagConstraints);

        panBtnVehicleType.setLayout(new java.awt.GridBagLayout());

        panBtnVehicleType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnVehicleType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnVehicleNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnVehicleNew.setToolTipText("New");
        btnVehicleNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleNew.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnVehicleNew.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnVehicleNewActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleNew, gridBagConstraints);

        btnVehicleSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnVehicleSave.setToolTipText("Save");
        btnVehicleSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleSave.setName("btnContactNoAdd");
        btnVehicleSave.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnVehicleSave.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnVehicleSaveActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleSave, gridBagConstraints);

        btnVehicleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnVehicleDelete.setToolTipText("Delete");
        btnVehicleDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleDelete.setPreferredSize(new java.awt.Dimension(29, 27));
//        btnVehicleDelete.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnVehicleDeleteActionPerformed(evt);
//            }
//        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panVehicleDetails.add(panBtnVehicleType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 12, 4);
        panVehicleTypeDetails.add(panVehicleDetails, gridBagConstraints);

       //tabMasterMaintenance.addTab("Vehicle Type", panVehicleTypeDetails);   

    }
   
    private com.see.truetransact.uicomponent.CButton btnCollateralDelete;
    private com.see.truetransact.uicomponent.CButton btnCollateralNew;
    private com.see.truetransact.uicomponent.CButton btnCollateralSave;
    private com.see.truetransact.uicomponent.CButton btnMemNo;
    private com.see.truetransact.uicomponent.CButton btnMemberDelete;
    private com.see.truetransact.uicomponent.CButton btnMemberNew;
    private com.see.truetransact.uicomponent.CButton btnMemberSave;
    private com.see.truetransact.uicomponent.CButton btnSalaryDelete;
    private com.see.truetransact.uicomponent.CButton btnSalaryNew;
    private com.see.truetransact.uicomponent.CButton btnSalarySave;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboNature;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblContactNum;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDate;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblEmployerName;
    private com.see.truetransact.uicomponent.CLabel lblGahanYesNo;
    private com.see.truetransact.uicomponent.CPanel panGahanYesNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanNo;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGahanGroup;
    private com.see.truetransact.uicomponent.CLabel lblMemName;
    private com.see.truetransact.uicomponent.CLabel lblMemNetworth;
    private com.see.truetransact.uicomponent.CLabel lblMemNo;
    private com.see.truetransact.uicomponent.CLabel lblMemType;
    private com.see.truetransact.uicomponent.CLabel lblMemberNum;
    private com.see.truetransact.uicomponent.CPanel panOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panDocumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblNature;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth1;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNname;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblPledge;
    private com.see.truetransact.uicomponent.CLabel lblPledgeAmount;
    private com.see.truetransact.uicomponent.CLabel lblPledgeDate;
    private com.see.truetransact.uicomponent.CLabel lblPledgeNo;
    private com.see.truetransact.uicomponent.CLabel lblRegisteredOffice;
    private com.see.truetransact.uicomponent.CLabel lblRetirementDt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRemark;
    private com.see.truetransact.uicomponent.CLabel lblSurveyNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalArea;
    private com.see.truetransact.uicomponent.CLabel lblTotalSalary;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CLabel lblPledgeType;
    private com.see.truetransact.uicomponent.CTextField txtPledgeType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private com.see.truetransact.uicomponent.CPanel panBtnCollateralType;
    private com.see.truetransact.uicomponent.CPanel panBtnMemberType;
    private com.see.truetransact.uicomponent.CPanel panBtnSalaryType;
    private com.see.truetransact.uicomponent.CPanel panCollateralTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralJointTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panCollatetalDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeTable;
    private com.see.truetransact.uicomponent.CPanel panSalaryTable;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panAllSalaryDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralTable;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralJointTable;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberType;
    private com.see.truetransact.uicomponent.CScrollPane srpSalary;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabMasterMaintenance;
    private com.see.truetransact.uicomponent.CTable tblCollateral;
    private com.see.truetransact.uicomponent.CTable tblJointCollateral;
    private com.see.truetransact.uicomponent.CTable tblMemberType;
    private com.see.truetransact.uicomponent.CTable tblSalary;
    private com.see.truetransact.uicomponent.CLabel lblRight;
    private com.see.truetransact.uicomponent.CComboBox cboRight;
    private com.see.truetransact.uicomponent.CDateField tdtDocumentDate;
    private com.see.truetransact.uicomponent.CDateField tdtPledgeDate;
    private com.see.truetransact.uicomponent.CDateField tdtRetirementDt;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtContactNum;
    private com.see.truetransact.uicomponent.CTextField txtDesignation;
    private com.see.truetransact.uicomponent.CTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblJewelleryDetails;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblValueOfGold;
    private com.see.truetransact.uicomponent.CLabel lblGoldRemarks;
    private com.see.truetransact.uicomponent.CTextArea txtJewelleryDetails;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtValueOfGold;
    private com.see.truetransact.uicomponent.CTextField txtGoldRemarks;
    private com.see.truetransact.uicomponent.CTextField txtInstallAmount;
    private com.see.truetransact.uicomponent.CLabel lblGoldSecurityExists;
    private com.see.truetransact.uicomponent.CPanel panGoldSecurityYesNo;
    private com.see.truetransact.uicomponent.CPanel panGoldSecurityId;
    private com.see.truetransact.uicomponent.CRadioButton rdoGoldSecurityExitsYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoGoldSecurityExitsNo;
    private com.see.truetransact.uicomponent.CLabel lblGoldSecurityId;
    private com.see.truetransact.uicomponent.CTextField txtGoldSecurityId;
    private com.see.truetransact.uicomponent.CButton btnGoldSecurityIdSearch;
    private com.see.truetransact.uicomponent.CPanel panGoldTypeDetails;
//    private com.see.truetransact.uicomponent.CTextField txtDocumentType;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CTextField txtEmployerName;
    private com.see.truetransact.uicomponent.CTextField txtMemName;
    private com.see.truetransact.uicomponent.CTextField txtMemNetworth;
    private com.see.truetransact.uicomponent.CTextField txtMemPriority;
    private com.see.truetransact.uicomponent.CLabel lblMemPriority;
    private com.see.truetransact.uicomponent.CTextField txtMemNo;
    private com.see.truetransact.uicomponent.CTextField txtMemType;
    private com.see.truetransact.uicomponent.CTextField txtMemberNum;
    private com.see.truetransact.uicomponent.CTextField txtNetWorth1;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemberNname;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CComboBox cboPledge;
    private com.see.truetransact.uicomponent.CTextField txtPledgeAmount;
    private com.see.truetransact.uicomponent.CTextField txtPledgeNo;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnDocumentNo;
    private com.see.truetransact.uicomponent.CTextField txtRegisteredOffice;
    private com.see.truetransact.uicomponent.CTextField txtSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CTextField txtSalaryRemark;
    private com.see.truetransact.uicomponent.CTextField txtSurveyNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalArea;
    private com.see.truetransact.uicomponent.CTextField txtTotalSalary;
    private com.see.truetransact.uicomponent.CTextField txtVillage;
    private javax.swing.JScrollPane srpChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CLabel lblDepAmount;
    private com.see.truetransact.uicomponent.CLabel lblProductId2;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblDepDt;
    private com.see.truetransact.uicomponent.CTextField txtDepAmount;
    private com.see.truetransact.uicomponent.CTextField txtMaturityValue;
    private com.see.truetransact.uicomponent.CTextField txtRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDt;
    private com.see.truetransact.uicomponent.CLabel lblMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblDepNo;
    private com.see.truetransact.uicomponent.CDateField tdtDepDt;
    private com.see.truetransact.uicomponent.CButton btnDepositNew;
    private com.see.truetransact.uicomponent.CButton btnDepositSave;
    private com.see.truetransact.uicomponent.CButton btnDepositDelete;
    private com.see.truetransact.uicomponent.CPanel panBtnDeposit;
    private com.see.truetransact.uicomponent.CDateField txtMaturityDt;
    private com.see.truetransact.uicomponent.CComboBox cboDepProdType;
    private com.see.truetransact.uicomponent.CPanel panDepNo;
    private com.see.truetransact.uicomponent.CButton btnDepNo;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeSecurity;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeSecurity;
    private com.see.truetransact.uicomponent.CPanel panDepositType;
    private com.see.truetransact.uicomponent.CPanel panDepositTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTableDeposit;
    private com.see.truetransact.uicomponent.CLabel lblTotalDeposit;
    private com.see.truetransact.uicomponent.CLabel lblTotalDepositValue;
    private com.see.truetransact.uicomponent.CTextField txtDepNo;
    private com.see.truetransact.uicomponent.CTable tblDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherSecurityDetails;
    private com.see.truetransact.uicomponent.CLabel lblLosName;
    private com.see.truetransact.uicomponent.CLabel lblLosSecurityType;
    private com.see.truetransact.uicomponent.CLabel lblLosSecurityNo;
    private com.see.truetransact.uicomponent.CLabel lblLosAmount;
    private com.see.truetransact.uicomponent.CTextField txtLosName;
    private com.see.truetransact.uicomponent.CTextField txtLosSecurityNo;
    private com.see.truetransact.uicomponent.CTextField txtLosMaturityvalue;
    private com.see.truetransact.uicomponent.CLabel lblLosIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblLosMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblLosMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblLosRemarks;
    private com.see.truetransact.uicomponent.CDateField tdtLosMaturityDate;
    private com.see.truetransact.uicomponent.CButton btnLosNew;
    private com.see.truetransact.uicomponent.CButton btnLosSave;
    private com.see.truetransact.uicomponent.CButton btnLosDelete;
    private com.see.truetransact.uicomponent.CTextField txtLosAmount;
    private com.see.truetransact.uicomponent.CTextField txtLosRemarks;
    private com.see.truetransact.uicomponent.CDateField tdtLosIsuueDate;
    private com.see.truetransact.uicomponent.CTextField txtLosMaturityValue;
    private com.see.truetransact.uicomponent.CDateField tdtLosIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblLosOtherInstitution;
    private com.see.truetransact.uicomponent.CComboBox cboLosOtherInstitution;
    private com.see.truetransact.uicomponent.CComboBox cboLosSecurityType;
    private com.see.truetransact.uicomponent.CPanel panLosTable;
    private com.see.truetransact.uicomponent.CPanel panLosDetails;
    private com.see.truetransact.uicomponent.CPanel panLosBtn;
    private com.see.truetransact.uicomponent.CDateField tdtLosMaturityDt;
    private com.see.truetransact.uicomponent.CScrollPane srpTableLos;
    private com.see.truetransact.uicomponent.CTable tblLosDetails;
    private com.see.truetransact.uicomponent.CPanel panDirectRepayment;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepayment;
    private com.see.truetransact.uicomponent.CPanel panDirectPayment;
    private com.see.truetransact.uicomponent.CRadioButton DirectRepayment_Yes;
    private com.see.truetransact.uicomponent.CRadioButton DirectRepayment_No;
    private com.see.truetransact.uicomponent.CComboBox cboDirectRepaymentProdType;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepaymentProdType;
    private com.see.truetransact.uicomponent.CComboBox cboDirectRepaymentProdId;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepaymentProdId;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepaymentAcctHead;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepaymentAcctNo;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CTextField txtDirectRepaymentAcctHead;
    private com.see.truetransact.uicomponent.CButton btnDirectRepaymentAcctHead;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel panDirectRepaymentLoanPeriod;
    private com.see.truetransact.uicomponent.CTextField txtDirectRepaymentLoanPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboDirectRepaymentLoanPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDirectRepaymentLoanPeriod;
    private com.see.truetransact.uicomponent.CLabel lblFacility_Repay_Date2;
    private com.see.truetransact.uicomponent.CDateField tdtDirect_Repay_Date;
    private com.see.truetransact.uicomponent.CButton btnDirectRepaymentAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtDirectRepaymentAcctNo;
    private com.see.truetransact.uicomponent.CButton btnVehicleMemNo;
    private com.see.truetransact.uicomponent.CButton btnVehicleDelete;
    private com.see.truetransact.uicomponent.CButton btnVehicleNew;
    private com.see.truetransact.uicomponent.CButton btnVehicleSave;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemRetireDate;
    private com.see.truetransact.uicomponent.CLabel lblVehicleNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleType;
    private com.see.truetransact.uicomponent.CLabel lblVehicleDetails;
    private com.see.truetransact.uicomponent.CLabel lblVehicleRcBookNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleDate;
    private com.see.truetransact.uicomponent.CLabel lblVehicleContactNum;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemName;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemberName;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemberNum;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemType;
    private com.see.truetransact.uicomponent.CPanel panBtnVehicleType;
    private com.see.truetransact.uicomponent.CPanel panVehicleDetails;
    private com.see.truetransact.uicomponent.CPanel panVehicleNumber;
    private com.see.truetransact.uicomponent.CScrollPane srpVehicleType;
    private com.see.truetransact.uicomponent.CPanel panVehicleTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panVehicleTypeTable;
    private com.see.truetransact.uicomponent.CTable tblVehicleType;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaVehicledtails;
    private com.see.truetransact.uicomponent.CTextArea txtVehicleDetals;
    private com.see.truetransact.uicomponent.CTextField txtVehicleNo;
    private com.see.truetransact.uicomponent.CTextField txtVehicleRcBookNo;
    private com.see.truetransact.uicomponent.CTextField txtVehicleType;
    private com.see.truetransact.uicomponent.CDateField txtVehicleDate;
    private com.see.truetransact.uicomponent.CTextField txtVehicleContactNum;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemType;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemberNum;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemberName;
    private com.see.truetransact.uicomponent.CLabel lblVehicleNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblTotalVehicleMemSal;
    private com.see.truetransact.uicomponent.CTextField txtVehicleNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemSal;
    // private  com.see.truetransact.uicomponent.CScrollPane sptFacilityDetails_Vert;
    // private com.see.truetransact.uicomponent.CDateField tdtDirect_Repay_Date;
    GridBagConstraints gridBagConstraints;
}
