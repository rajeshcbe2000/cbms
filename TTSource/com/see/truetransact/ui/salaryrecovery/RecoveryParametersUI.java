/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RecoveryParametersUI.java
 *
 * Created on February 25, 2004, 11:55 AM
 */

package com.see.truetransact.ui.salaryrecovery;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
//import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFUI;
//import com.see.truetransact.ui.common.authorizewf.AuthorizeWFCheckUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
//import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.common.viewall.ViewLoansTransUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
//import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Logger;
//import java.util.Date;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class RecoveryParametersUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    RecoveryParametersOB observable;
    final int EDIT=0, DELETE=1, ACCNO=2, AUTHORIZE=8, ACCTHDID = 4, VIEW = 5, LINK_BATCH_TD=6, LINK_BATCH=7,TELLER_ENTRY_DETIALS=10;
    String viewType = ClientConstants.VIEW_TYPE_CANCEL;
    boolean isFilled = false;
    
    AuthorizedSignatoryUI authSignUI;
    PowerOfAttorneyUI poaUI;
    private TransDetailsUI transDetails = null;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private TransCommonUI transCommonUI = null;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepAuth = false;
    private boolean flagDepLink = false;
    private boolean flagDepositAuthorize = false;
    private boolean afterSaveCancel = false;
    private CTextField txtDepositAmount;
    private double intAmtDep =0.0;
    TermDepositUI termDepositUI;
    public String designation="";
    private String custStatus="";
    
    private String depBehavesLike = "";
    private HashMap intMap = new HashMap();
    private boolean reconcilebtnDisable = false;
    ArrayList termLoanDetails= null;
    private boolean termLoanDetailsFlag = false;
    HashMap termLoansDetailsMap = null;
    private String depPartialWithDrawalAllowed = "";
    private final static Logger log = Logger.getLogger(LockUI.class);     //Logger
    List accountNoList=null;
    boolean fromAuthorizeUI = false;
    List recoveryParameterList=null;
    AuthorizeListUI authorizeListUI = null;
    private String mandatoryMessage;
    /** Creates new form CashTransaction */
    public RecoveryParametersUI() {
        initComponents();
        initSetup();
        
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();                    // Fill all the combo boxes...
        setMaxLenths();                         // To set the Numeric Validation and the Maximum length of the Text fields...
        
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();               // Enables/Disables the necessary buttons and menu items...
        setHelpMessage();
        
        btnSave.setEnabled(true);
        
        txtFirstDay.setEnabled(true);
        txtLastDay. setEnabled(false);
        txtSalarySuspenseProdId.setEnabled(true);
        txtMDSSuspenseProdId.setEnabled(true);
        txtRDSuspenseProdId.setEnabled(true);
        getUserDesignation();
        HashMap data=new HashMap();
        recoveryParameterList=ClientUtil.executeQuery("getRecoveryParameters", data);
        if(recoveryParameterList!=null & recoveryParameterList.size()>0){
            data=(HashMap)recoveryParameterList.get(0);
            txtFirstDay.setText(CommonUtil.convertObjToStr(data.get("FIRST_DAY")));
            txtLastDay.setText(CommonUtil.convertObjToStr(data.get("LAST_DAY")));
            txtSalarySuspenseProdId.setText(CommonUtil.convertObjToStr(data.get("SUSPENSE_PRODUCT_ID")));
            txtMDSSuspenseProdId.setText(CommonUtil.convertObjToStr(data.get("MDS_SUSPENSE_PROD_ID")));
            txtRDSuspenseProdId.setText(CommonUtil.convertObjToStr(data.get("RD_SUSPENSE_PROD_ID")));
            if(!txtFirstDay.getText().equals("")){
                observable.setRecovery(true);
            }
        }
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        
        try{
            observable = new RecoveryParametersOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Authorize Button to be added...
    private void setFieldNames() {
        lblFromDate.setName("lblFromDate");
        lblToDate.setName("lblToDate");
        txtFirstDay.setName("txtFirstDay");
        txtLastDay.setName("txtLastDay");
        lblFirstDay.setName("lblFirstDay");
        lblLastDay.setName("lblLastDay");
        btnSave.setName("btnSave");
        lblSalarySuspenseProdId.setName("lblSalarySuspenseProdId");
        lblMDSSuspenseProdId.setName("lblMDSSuspenseProdId");
        lblRDSuspenseProdId.setName("lblRDSuspenseProdId");
        txtSalarySuspenseProdId.setName("txtSalarySuspenseProdId");
        txtMDSSuspenseProdId.setName("txtMDSSuspenseProdId");
        txtRDSuspenseProdId.setName("txtRDSuspenseProdId");
    }
    
    private void internationalize() {
        //CashTransactionRB resourceBundle = new CashTransactionRB();
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.salaryrecovery.RecoveryParametersRB", ProxyParameters.LANGUAGE);
        lblFirstDay.setText(resourceBundle.getString("lblFirstDay"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblLastDay.setText(resourceBundle.getString("lblLastDay"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        
        //        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        
        
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("txtEmployerRefNo", new Boolean(true));
        mandatoryMap.put("txtEmployerRefNo", new Boolean(true));
        mandatoryMap.put("cboInitiatorChannel", new Boolean(true));
        mandatoryMap.put("rdoTransactionType_Debit", new Boolean(true));
        mandatoryMap.put("txtInputAmt", new Boolean(true));
        mandatoryMap.put("cboInputCurrency", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccHdId", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("txtNarration", new Boolean(false));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        
        
        
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        
    }
    
    public void update(Observable observed, Object arg) {
        txtFirstDay.setText(observable.getFirstDay());
        txtLastDay.setText(observable.getLastDay());
    }
    
    
    
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable. setFirstDay(txtFirstDay.getText());
        observable. setLastDay(txtLastDay.getText());
        observable.setTxtSalarySuspenseProdId(txtSalarySuspenseProdId.getText());
        observable.setTxtMDSSuspenseProdId(txtMDSSuspenseProdId.getText());
        observable.setTxtRDSuspenseProdId(txtRDSuspenseProdId.getText());
        
        
        
        
        
    }
    
    public void setHelpMessage() {
        //        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        //        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        //        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        
        
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //               cboProdId.setModel(observable.getCbmProdId());
        
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtFirstDay.setMaxLength(5);
        txtFirstDay.setValidation(new NumericValidation());
        txtLastDay.setMaxLength(5);
        txtLastDay.setValidation(new NumericValidation());
        txtSalarySuspenseProdId.setAllowAll(true);
        txtMDSSuspenseProdId.setAllowAll(true);
        txtRDSuspenseProdId.setAllowAll(true);
        
        
        //        txtTokenNo.setValidation(new NumericValidation());
        
        
    }
    
    
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        
        
        
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    public void setButtonEnableDisable(boolean flag) {
        
        
        
        mitNew.setEnabled(flag);
        mitEdit.setEnabled(flag);
        mitDelete.setEnabled(!flag);
        
        mitSave.setEnabled(!flag);
        mitCancel.setEnabled(!flag);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoLockGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccounts = new com.see.truetransact.uicomponent.CButtonGroup();
        panSalaryDeductionMapping = new com.see.truetransact.uicomponent.CPanel();
        panFreqency = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        txtFirstDay = new com.see.truetransact.uicomponent.CTextField();
        txtLastDay = new com.see.truetransact.uicomponent.CTextField();
        lblFirstDay = new com.see.truetransact.uicomponent.CLabel();
        lblLastDay = new com.see.truetransact.uicomponent.CLabel();
        papProcess = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblSalarySuspenseProdId = new com.see.truetransact.uicomponent.CLabel();
        lblMDSSuspenseProdId = new com.see.truetransact.uicomponent.CLabel();
        lblRDSuspenseProdId = new com.see.truetransact.uicomponent.CLabel();
        txtSalarySuspenseProdId = new com.see.truetransact.uicomponent.CTextField();
        txtMDSSuspenseProdId = new com.see.truetransact.uicomponent.CTextField();
        txtRDSuspenseProdId = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(651, 400));
        setPreferredSize(new java.awt.Dimension(900, 400));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        panSalaryDeductionMapping.setLayout(new java.awt.GridBagLayout());

        panSalaryDeductionMapping.setBorder(new javax.swing.border.EtchedBorder());
        panSalaryDeductionMapping.setMinimumSize(new java.awt.Dimension(600, 340));
        panSalaryDeductionMapping.setPreferredSize(new java.awt.Dimension(605, 340));
        panFreqency.setLayout(new java.awt.GridBagLayout());

        panFreqency.setBorder(new javax.swing.border.TitledBorder(""));
        panFreqency.setMinimumSize(new java.awt.Dimension(275, 130));
        panFreqency.setPreferredSize(new java.awt.Dimension(760, 70));
        lblFromDate.setText("day of the month ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblFromDate, gridBagConstraints);

        lblToDate.setText("and   last day of the month ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblToDate, gridBagConstraints);

        txtFirstDay.setPreferredSize(new java.awt.Dimension(50, 21));
        txtFirstDay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFirstDayFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panFreqency.add(txtFirstDay, gridBagConstraints);

        txtLastDay.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panFreqency.add(txtLastDay, gridBagConstraints);

        lblFirstDay.setText("In the Current month Recovery List include Loans Sanctioned between  1st   and  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblFirstDay, gridBagConstraints);

        lblLastDay.setText("In the Next Month Recovery List include Loans  Sanctioned  between  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblLastDay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalaryDeductionMapping.add(panFreqency, gridBagConstraints);

        papProcess.setLayout(new java.awt.GridBagLayout());

        papProcess.setBorder(new javax.swing.border.TitledBorder(""));
        papProcess.setMinimumSize(new java.awt.Dimension(275, 120));
        papProcess.setPreferredSize(new java.awt.Dimension(90, 30));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        papProcess.add(btnSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSalaryDeductionMapping.add(papProcess, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        cPanel1.setBorder(new javax.swing.border.TitledBorder("Suspense Product"));
        cPanel1.setMaximumSize(new java.awt.Dimension(300, 90));
        cPanel1.setMinimumSize(new java.awt.Dimension(300, 90));
        cPanel1.setPreferredSize(new java.awt.Dimension(300, 90));
        lblSalarySuspenseProdId.setText("Salary Suspense Prod Id");
        lblSalarySuspenseProdId.setMaximumSize(new java.awt.Dimension(150, 21));
        lblSalarySuspenseProdId.setMinimumSize(new java.awt.Dimension(150, 21));
        lblSalarySuspenseProdId.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblSalarySuspenseProdId, gridBagConstraints);

        lblMDSSuspenseProdId.setText("MDS Suspense Prod Id");
        lblMDSSuspenseProdId.setMaximumSize(new java.awt.Dimension(150, 21));
        lblMDSSuspenseProdId.setMinimumSize(new java.awt.Dimension(150, 21));
        lblMDSSuspenseProdId.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblMDSSuspenseProdId, gridBagConstraints);

        lblRDSuspenseProdId.setText("RD Suspense Prod Id");
        lblRDSuspenseProdId.setMaximumSize(new java.awt.Dimension(150, 21));
        lblRDSuspenseProdId.setMinimumSize(new java.awt.Dimension(150, 21));
        lblRDSuspenseProdId.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblRDSuspenseProdId, gridBagConstraints);

        txtSalarySuspenseProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalarySuspenseProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalarySuspenseProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalarySuspenseProdIdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        cPanel1.add(txtSalarySuspenseProdId, gridBagConstraints);

        txtMDSSuspenseProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMDSSuspenseProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        cPanel1.add(txtMDSSuspenseProdId, gridBagConstraints);

        txtRDSuspenseProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRDSuspenseProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        cPanel1.add(txtRDSuspenseProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSalaryDeductionMapping.add(cPanel1, gridBagConstraints);

        getContentPane().add(panSalaryDeductionMapping, java.awt.BorderLayout.CENTER);

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
    }//GEN-END:initComponents
    
    private void txtSalarySuspenseProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalarySuspenseProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalarySuspenseProdIdActionPerformed
    
    private void txtFirstDayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFirstDayFocusLost
        // TODO add your handling code here:
        int firstday= CommonUtil.convertObjToInt(txtFirstDay.getText());
        int n=31;
        int a=1;
        if(firstday>0)
            if(firstday>=n ){
                ClientUtil.displayAlert("Enter a valid date lesser than 31");
                txtLastDay.setText("");
                txtFirstDay.setText("");
                txtFirstDay.requestDefaultFocus();
            }else if(firstday<=a){
                ClientUtil.displayAlert("Enter a valid date greater than 1");
                txtLastDay.setText("");
                txtFirstDay.setText("");
                txtFirstDay.requestDefaultFocus();
            }
            else{
                txtLastDay.setText(CommonUtil.convertObjToStr(new Integer(firstday+1)));
            }
    }//GEN-LAST:event_txtFirstDayFocusLost
    
    private void txtAccNoActionPerformed() {
        
        
        
        //        observable.setProdType("");
        
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        String lstday=txtLastDay.getText();
        if(lstday.length()>0){
            observable.doAction();
        }
        cleareActionPerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        //        btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened
    
    
    
    
    
    private boolean moreThanLoanAmountAlert(){
        
        return false;
    }
    
    public void btnCheck(){
        
        
    }
    
    public void cleareActionPerformed(){
        observable.resetForm();
        txtSalarySuspenseProdId.setText("");
        txtMDSSuspenseProdId.setText("");
        txtRDSuspenseProdId.setText("");
    }
    
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    
    private void setEnable(){
        
        
    }
    
    
    public void btnDepositCancel(){
        afterSaveCancel = true;
        
    }
    
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        
    }
    
    
    
    
    //To enable and/or Disable buttons(folder)...
    
    public void getUserDesignation(){
        HashMap desigMap = new HashMap();
        desigMap.put("USER",TrueTransactMain.USER_ID);
        List lst= ClientUtil.executeQuery("getUserDesignation",desigMap);
        desigMap= null;
        if(lst!=null && lst.size()>0){
            desigMap=(HashMap)lst.get(0);
            designation= CommonUtil.convertObjToStr(desigMap.get("DESIGNATION"));
        }
    }
    
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
    private void textDisable(){
        
        
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
        
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
            //System.exit(0);
            this.dispose();
    }//GEN-LAST:event_exitForm
        
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            try {
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Throwable th) {
                th.printStackTrace();
            }
            
            javax.swing.JFrame jf = new javax.swing.JFrame();
            SalaryDeductionMappingUI gui = new SalaryDeductionMappingUI();
            jf.getContentPane().add(gui);
            jf.setSize(536, 566);
            jf.show();
            gui.show();
        }
        
        /**
         * Getter for property viewType.
         * @return Value of property viewType.
         */
        //        public int getViewType() {
        //            return viewType;
        //        }
        //
        /**
         * Setter for property viewType.
         * @param viewType New value of property viewType.
         */
        //        public void setViewType(int viewType) {
        //            this.viewType = viewType;
        //        }
        
        /**
         * Getter for property intAmtDep.
         * @return Value of property intAmtDep.
         */
        public double getIntAmtDep() {
            return intAmtDep;
        }
        
        /**
         * Setter for property intAmtDep.
         * @param intAmtDep New value of property intAmtDep.
         */
        public void setIntAmtDep(double intAmtDep) {
            this.intAmtDep = intAmtDep;
        }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CLabel lblFirstDay;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLastDay;
    private com.see.truetransact.uicomponent.CLabel lblMDSSuspenseProdId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRDSuspenseProdId;
    private com.see.truetransact.uicomponent.CLabel lblSalarySuspenseProdId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panFreqency;
    private com.see.truetransact.uicomponent.CPanel panSalaryDeductionMapping;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel papProcess;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccounts;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLockGroup;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CTextField txtFirstDay;
    private com.see.truetransact.uicomponent.CTextField txtLastDay;
    private com.see.truetransact.uicomponent.CTextField txtMDSSuspenseProdId;
    private com.see.truetransact.uicomponent.CTextField txtRDSuspenseProdId;
    private com.see.truetransact.uicomponent.CTextField txtSalarySuspenseProdId;
    // End of variables declaration//GEN-END:variables
}
