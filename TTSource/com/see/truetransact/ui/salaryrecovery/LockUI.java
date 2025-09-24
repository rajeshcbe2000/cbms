/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockUI.java
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
import java.util.ArrayList;
//import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Logger;
import java.util.Date;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class LockUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    LockOB observable;
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
    AuthorizeListUI authorizeListUI = null;
    private String mandatoryMessage;
    private Date currDt = null;
    /** Creates new form CashTransaction */
    public LockUI() {
        currDt = ClientUtil.getCurrentDate();
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
        rdoLock.setEnabled(true);
        rdoUnLock.setEnabled(true);
        rdoLock.setSelected(true);
        rdoSpecificAccount.setSelected(true);
        rdoAllAccounts.setEnabled(true);
        rdoSpecificAccount.setEnabled(true);
        
        cboProdType.setEnabled(true);
        cboProdId.setEnabled(true);
        txtAccNo.setEnabled(true);
        btnAccNo.setEnabled(true);
        btnProcess.setEnabled(true);
        btnClear.setEnabled(true);
        lockTableScrollPan.setVisible(false);
        getUserDesignation();
        
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        
        try{
            observable = new LockOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Authorize Button to be added...
    private void setFieldNames() {
        cboProdType.setName("cboProdType");
        lblProdType.setName("lblProdType");
        rdoUnLock.setName("rdoUnLock");
        rdoLock.setName("rdoLock");
        txtAccNo.setName("txtAccNo");
        cboProdId.setName("cboProdId");
        lblAccNo.setName("lblAccNo");
        rdoSpecificAccount.setName("rdoSpecificAccount");
        rdoAllAccounts.setName("rdoAllAccounts");
        btnProcess.setName("btnProcess");
        btnClear.setName("btnClear");
    }
    
    private void internationalize() {
        //CashTransactionRB resourceBundle = new CashTransactionRB();
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.salaryrecovery.LockRB", ProxyParameters.LANGUAGE);
        
        
        //        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        rdoLock.setText(resourceBundle.getString("rdoLock"));
        rdoUnLock.setText(resourceBundle.getString("rdoUnLock"));
        rdoSpecificAccount.setText(resourceBundle.getString("rdoSpecificAccount"));
        rdoAllAccounts.setText(resourceBundle.getString("rdoAllAccounts"));
        btnProcess.setText(resourceBundle.getString("btnProcess"));
        btnClear.setText(resourceBundle.getString("btnClear"));
        
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
        rdoLockGroup.remove(rdoLock);
        rdoLockGroup.remove(rdoUnLock);
        
        rdoAccounts.remove(rdoAllAccounts);
        rdoAccounts.remove(rdoSpecificAccount);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        rdoLockGroup = new CButtonGroup();
        rdoLockGroup.add(rdoLock);
        rdoLockGroup.add(rdoUnLock);
        rdoAccounts=new CButtonGroup();
        rdoAccounts.add(rdoAllAccounts);
        rdoAccounts.add(rdoSpecificAccount);
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        //        observable.setSelectedBranchID(getSelectedBranchID());
        setSelectedBranchID(observable.getSelectedBranchID());
        if (observable.getCbmProdId()!=null)
            cboProdId.setModel(observable.getCbmProdId());
        
        txtAccNo.setText(observable.getTxtAccNo());
        
        
        if(observable.getDeleteFlag().equals("Y")){
            
        }
        else{
            
        }
        
        
        //        cboInputCurrency.setSelectedItem(observable.getCboInputCurrency());
        //        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        
        //
        //        //To set the  Name of the Account Holder...
        
        // Added by Rajesh
        
        addRadioButtons();
    }
    
    public String setAccountName(String AccountNo){
        final HashMap accountNameMap = new HashMap();
        HashMap resultMap = new HashMap();
        String prodType = (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
        String pID = !prodType.equals("GL") ? ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString() : "";
        try {
            accountNameMap.put("ACC_NUM",AccountNo);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName"+prodType,accountNameMap);
            if(resultList != null)
                if(resultList.size() > 0){
                    if(!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodType,accountNameMap);
                        if(lst != null)
                            if(lst.size() > 0){
                                dataMap = (HashMap) lst.get(0);
                                if(dataMap.get("PROD_ID").equals(pID)){
                                    resultMap = (HashMap)resultList.get(0);
                                }
                            }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                    
                }
        }catch(Exception e){
            
        }
        if(resultMap.containsKey("CUSTOMER_NAME")){
            if(prodType.equals("OA")){
                custStatus= CommonUtil.convertObjToStr(resultMap.get("MINOR"));
                String actStatus= CommonUtil.convertObjToStr(resultMap.get("ACT_STATUS_ID"));
                if(custStatus.equals("Y"))
                    
                    if(actStatus.equals("DORMANT"))
                        ClientUtil.displayAlert("DORMANT ACCOUNT");
            }
            return resultMap.get("CUSTOMER_NAME").toString();
            
        }
        else
            return String.valueOf("");
    }
    
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtAccNo((String) txtAccNo.getText());
        
        
        observable.setDeleteFlag("Y");
        
        
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
        cboProdType.setModel(observable.getCbmProdType());
        lockTable.setModel(observable.getLockTable());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        
        
        txtAccNo.setMaxLength(32);
        txtAccNo.setAllowAll(true);
        
        
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
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountName = new com.see.truetransact.uicomponent.CLabel();
        papProcess = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        rdoLock = new com.see.truetransact.uicomponent.CRadioButton();
        rdoUnLock = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAllAccounts = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSpecificAccount = new com.see.truetransact.uicomponent.CRadioButton();
        lockTableScrollPan = new com.see.truetransact.uicomponent.CScrollPane();
        lockTable = new com.see.truetransact.uicomponent.CTable();
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
        setPreferredSize(new java.awt.Dimension(651, 400));
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

        panFreqency.setMinimumSize(new java.awt.Dimension(275, 130));
        panFreqency.setPreferredSize(new java.awt.Dimension(300, 110));
        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPreferredSize(new java.awt.Dimension(21, 200));
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
        panFreqency.add(cboProdType, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblProductId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(cboProdId, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblAccNo, gridBagConstraints);

        panAcctNo.setLayout(new java.awt.GridBagLayout());

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });

        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });

        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(panAcctNo, gridBagConstraints);

        lblAccountName.setForeground(new java.awt.Color(0, 51, 255));
        lblAccountName.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblAccountName.setMaximumSize(new java.awt.Dimension(300, 13));
        lblAccountName.setMinimumSize(new java.awt.Dimension(300, 13));
        lblAccountName.setPreferredSize(new java.awt.Dimension(300, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreqency.add(lblAccountName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSalaryDeductionMapping.add(panFreqency, gridBagConstraints);

        papProcess.setLayout(new java.awt.GridBagLayout());

        papProcess.setBorder(new javax.swing.border.TitledBorder(""));
        papProcess.setMinimumSize(new java.awt.Dimension(275, 120));
        papProcess.setPreferredSize(new java.awt.Dimension(230, 30));
        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        papProcess.add(btnProcess, gridBagConstraints);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        papProcess.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panSalaryDeductionMapping.add(papProcess, gridBagConstraints);

        rdoLock.setText("LOCK");
        rdoLockGroup.add(rdoLock);
        rdoLock.setMaximumSize(new java.awt.Dimension(55, 18));
        rdoLock.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoLock.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panSalaryDeductionMapping.add(rdoLock, gridBagConstraints);

        rdoUnLock.setText("UNLOCK");
        rdoLockGroup.add(rdoUnLock);
        rdoUnLock.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoUnLock.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoUnLock.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalaryDeductionMapping.add(rdoUnLock, gridBagConstraints);

        rdoAllAccounts.setText("ALL ACCOUNTS");
        rdoAccounts.add(rdoAllAccounts);
        rdoAllAccounts.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoAllAccounts.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoAllAccounts.setPreferredSize(new java.awt.Dimension(130, 18));
        rdoAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllAccountsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 19, 0);
        panSalaryDeductionMapping.add(rdoAllAccounts, gridBagConstraints);

        rdoSpecificAccount.setText("SPECIFIC ACCOUNT");
        rdoAccounts.add(rdoSpecificAccount);
        rdoSpecificAccount.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoSpecificAccount.setMinimumSize(new java.awt.Dimension(40, 18));
        rdoSpecificAccount.setPreferredSize(new java.awt.Dimension(150, 18));
        rdoSpecificAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSpecificAccountActionPerformed(evt);
            }
        });
        rdoSpecificAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoSpecificAccountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 19, 0);
        panSalaryDeductionMapping.add(rdoSpecificAccount, gridBagConstraints);

        lockTableScrollPan.setMaximumSize(new java.awt.Dimension(300, 140));
        lockTableScrollPan.setMinimumSize(new java.awt.Dimension(390, 100));
        lockTableScrollPan.setPreferredSize(new java.awt.Dimension(340, 150));
        lockTable.setModel(new javax.swing.table.DefaultTableModel(
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
        lockTableScrollPan.setViewportView(lockTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(19, 29, 19, 29);
        panSalaryDeductionMapping.add(lockTableScrollPan, gridBagConstraints);

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
    
    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        if(txtAccNo.getText().length() > 0){
            txtAccNoActionPerformed();
        }
    }//GEN-LAST:event_txtAccNoFocusLost
    
    private void txtAccNoActionPerformed() {
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtAccNo.getText();
        String lockStatus="";
        String msg="";
        if(rdoLock.isSelected()==true){
            lockStatus="Y";
            msg="Locked";
        }else{
            lockStatus="N";
            msg="UnLocked";
        }
        
        //        observable.setProdType("");
        if (/*(!(observable.getProdType().length()>0)) && */ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO+"_1")) {
                cboProdId.setModel(observable.getCbmProdId());
                
                txtAccNo.setText(ACCOUNTNO);
                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                observable.setAccountName(ACCOUNTNO,prodType);
                lblAccountName.setText(observable.getLblAccName());
                String salrecovery=observable.getSalaryRecovery();
                String lckStatus=observable.getLockStatus();
                if(!salrecovery.equals("Y")){
                    ClientUtil.showAlertWindow("Account not under salary recovery scheme");
                    txtAccNo.setText("");
                    lblAccountName.setText("");
                    return;
                }if(lckStatus.equals(lockStatus)){
                    ClientUtil.showAlertWindow("Account is already "+ msg);
                    txtAccNo.setText("");
                    lblAccountName.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccNo.setText("");
                lblAccountName.setText("");
                return;
            }
        }
    }
    
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        String lockStatus="";
        String brachcode=getSelectedBranchID();
        if(rdoLock.isSelected()==true){
            lockStatus="Y";
        }else{
            lockStatus="N";
        }
        HashMap hmap=new HashMap();
        ArrayList rowList=new ArrayList();
        if(rdoSpecificAccount.isSelected()==true){
            String accountNo=txtAccNo.getText();
            if(accountNo.length()>0){
                String prodType = (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
                hmap.put("ACT_NUM",accountNo);
                hmap.put("LOCK_STATUS",lockStatus);
                if(prodType.equals("TD")){
                    
                    ClientUtil.execute("setLockStatusForTD",hmap);
                    
                }else if(prodType.equals("TL") || prodType.equals("AD")){
                    
                    ClientUtil.execute("setLockStatusForTL",hmap);
                    
                }else{
                    ClientUtil.execute("setLockStatusForMDS",hmap);
                }
                ClientUtil.showMessageWindow("Process completed");
               
            }else
                ClientUtil.displayAlert("Please select the AccountNo");
        }
        else{
            hmap.put("LOCK_STATUS",lockStatus);
            hmap.put("BRANCH_CODE",brachcode);
            ClientUtil.execute("setLockStatusForAllTD",hmap);
            ClientUtil.execute("setLockStatusForAllTL",hmap);
            ClientUtil.execute("setLockStatusForAllMDS",hmap);
            ClientUtil.showMessageWindow("Process completed the list is...");
            panFreqency.setVisible(false);
            lockTableScrollPan.setVisible(true);
        }
        
        btnClearActionPerformed(null);
        btnProcess.setEnabled(false);
        cboProdType.setEnabled(false);
        cboProdId.setEnabled(false);
        txtAccNo.setEnabled(false);
        btnAccNo.setEnabled(false);
        if(rdoAllAccounts.isSelected()==true){
            
            String salrecovery ="Y";
            HashMap hashmap=new HashMap();
            hashmap.put("SALARY_RECOVERY",salrecovery);
            hashmap.put("BRANCH_CODE",brachcode);
            List acclist=ClientUtil.executeQuery("getAccountNos",hashmap);
            hashmap=null;
            String prodType="";
            String prodId="";
            String actNum="";
            String name="";
            for(int i=0;i<acclist.size();i++){
                hashmap=(HashMap)acclist.get(i);
                prodType=CommonUtil.convertObjToStr(hashmap.get("PROD_TYPE"));
                prodId=CommonUtil.convertObjToStr(hashmap.get("PROD_ID"));
                actNum=CommonUtil.convertObjToStr(hashmap.get("ACT_NUM"));
                name=CommonUtil.convertObjToStr(hashmap.get("NAME"));
                ArrayList alist=new ArrayList();
                alist.add(prodType);
                alist.add(prodId);
                alist.add(actNum);
                alist.add(name);
                rowList.add(alist);
                observable.insertTableData(rowList);
                lockTable.setModel(observable.getLockTable());
            }
        }
        
    }//GEN-LAST:event_btnProcessActionPerformed
    
    private void rdoSpecificAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSpecificAccountActionPerformed
        // TODO add your handling code here:
        cboProdType.setEnabled(true);
        cboProdId.setEnabled(true);
        txtAccNo.setEnabled(true);
        btnAccNo.setEnabled(true);
        lockTableScrollPan.setVisible(false);
        panFreqency.setVisible(true);
    }//GEN-LAST:event_rdoSpecificAccountActionPerformed
    
    private void rdoSpecificAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoSpecificAccountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoSpecificAccountFocusLost
    
    private void rdoAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllAccountsActionPerformed
        // TODO add your handling code here:
        cboProdType.setEnabled(false);
        cboProdId.setEnabled(false);
        txtAccNo.setEnabled(false);
        btnAccNo.setEnabled(false);
        //               lockTableScrollPan.setVisible(true);
        //               panFreqency.setVisible(false);
    }//GEN-LAST:event_rdoAllAccountsActionPerformed
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        callView("CREDIT_ACC_NO");        // TODO add your handling code here:
    }//GEN-LAST:event_btnAccNoActionPerformed
    
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {        // TODO add your handling code here:
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmProdId(prodType);
            //            if(prodType.equals("TD")){
            //                ClientUtil.displayAlert("Not allowing for crediting this Account No...");
            //                cboProdType.setSelectedItem("");
            //                cboProdId.setSelectedItem("");
            //            }
            if(prodType.equals("GL")){
                cboProdId.setSelectedItem("");
                cboProdId.setEnabled(false);
                txtAccNo.setText("");
                
            }else if(prodType.equals("RM")){
                
                txtAccNo.setText("");
                
                txtAccNo.setEnabled(true);
            }else{
                cboProdId.setEnabled(true);
                
                txtAccNo.setText("");
                
            }
            if(!prodType.equals("GL"))
                cboProdId.setModel(observable.getCbmProdId());
            //            }else
            //                cboProdId.setSelectedItem("PAY ORDR");
            if((observable.getActionType() == ClientConstants.ACTIONTYPE_RENEW) ||
            (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE)){
                
                cboProdType.setEnabled(false);
                cboProdId.setEnabled(false);
                txtAccNo.setEnabled(false);
                
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed
    
    
    private void callView(String currField) {
        viewType = currField;
        String lockStatus="";
        if(rdoLock.isSelected()==true){
            lockStatus="Y";
        }else{
            lockStatus="N";
        }
        if(currField == "CREDIT_ACC_NO"){
            HashMap viewMap = new HashMap();
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if(!prodType.equals("GL")){
                viewMap.put(CommonConstants.MAP_NAME, "Lock.getAccountList"
                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            }
            HashMap whereMap = new HashMap();
            if(cboProdId.getModel() != null && cboProdId.getModel().getSize()>0){
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }
            if(whereMap.get("SELECTED_BRANCH")==null)
                whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
            else
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            whereMap.put("LOCK_STATUS",lockStatus);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
    }
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        
        rdoLock.setSelected(false);
        rdoUnLock.setSelected(false);
        rdoAllAccounts.setSelected(false);
        rdoSpecificAccount.setSelected(false);
        cboProdType.setSelectedItem("");
        cboProdId.setSelectedItem("");
        txtAccNo.setText("");
        lblAccountName.setText("");
        observable.setSalaryRecovery("");
        observable.setLblAccName("");
        btnProcess.setEnabled(true);
        cboProdType.setEnabled(true);
        cboProdId.setEnabled(true);
        txtAccNo.setEnabled(true);
        btnAccNo.setEnabled(true);
        observable. setTableReset();
        
    }//GEN-LAST:event_btnClearActionPerformed
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        //        btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened
    
    private boolean moreThanLoanAmountAlert(){
        
        return false;
    }
    
    public void btnCheck(){
        
        
    }
    
    public void authorize(HashMap map) {
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            
            //                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);commented by bala 10-aug-2010
            
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            map.put("CORP_LOAN_MAP", transDetails.getCorpDetailMap());  // For Corporate Loan purpose added by Rajesh
            observable.setAuthorizeMap(map);
            observable.doAction();
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                }
            }
            
            
            observable.setResultStatus();
        }
    }
    
    public void authorizeStatus(String authorizeStatus) {
        
        if(isFilled){
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            java.util.Date curdt=(Date) currDt.clone();
            String empno=  observable.getEmployerRefNo();
            String accno=observable.getTxtAccNo();
            whereMap.put("EMP_REF_NO", empno);
            whereMap.put("MAP_ACT_NUM",accno);
            whereMap.put("AUTHORIZEDT",curdt);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            
            System.out.println("wheremap is"+  whereMap);
            if(viewType.equals("AUTHORIZE")){
                ClientUtil.execute("authorizeSalaryDeductionMapping" ,whereMap);
            }
            else{
                ClientUtil.execute("rejectSalaryDeductionMapping" ,whereMap);
            }
            
            
            isFilled=false;
        }else{
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            //mapParam.put(CommonConstants.MAP_NAME, "getSelectCorpCustAuthorizeTOList");
            // mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCorpCust");
            mapParam.put(CommonConstants.MAP_NAME,"getSalaryDeductionMappingAuthList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME,"authorizeSalaryDeductionMapping");
            //            isFilled = true;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    
    
    
    void checkLoanTransaction(HashMap hash){
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("TL")) {
            String actnum="";
            if(observable.getTxtAccNo().lastIndexOf("_")!=-1)
                actnum=observable.getTxtAccNo().substring(0,observable.getTxtAccNo().lastIndexOf("_"));
            else
                actnum=observable.getTxtAccNo();
            HashMap map=new HashMap();
            map.put("ACCOUNTNO",new String(actnum));
            List lst=ClientUtil.executeQuery("getDisbursementAmtDetailsTL", map);
            double disburseAmt=0;
            if(lst.size()>0){
                
                map=(HashMap)lst.get(0);
                disburseAmt=CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
            }
            else{
                
            }
            String availBalance=transDetails.getAvBalance();  //getAvailableBalance();change by abi
            double availableBalnce=Double.parseDouble(availBalance.replaceAll(",",""));
            String clearBalance=transDetails.getCBalance();
            double clearBal=Double.parseDouble(clearBalance);
            clearBal=-1* clearBal;
            String limitAmt=observable.getLimitAmount();
            double amtLimit=Double.parseDouble(limitAmt.replaceAll(",",""));
            String sDebit=transDetails.getShadowDebit();
            //double debit_shadowAmt=Double.parseDouble(sDebit);
            String multiDisburse=transDetails.getIsMultiDisburse();
            double debit_shadowamt=Double.parseDouble(sDebit.replaceAll(",",""));
            double shadow=debit_shadowamt;
            debit_shadowamt+=clearBal+availableBalnce;
            System.out.println("###clearbalance"+clearBal+"###limitamount"+amtLimit+"shadowdebit"+debit_shadowamt+"     :"+multiDisburse);
            //            if(shadow==availableBalnce)
            //            if( shadow==availableBalnce|| amtLimit ==debit_shadowamt && multiDisburse.equals("N")){
            if(disburseAmt>=amtLimit){
                //                System.out.println("if condition inside");
                //                rdoTransactionType_Debit.setSelected(false);
                //                rdoTransactionType_Credit.setSelected(true);
                //                rdoTransactionType_Debit.setEnabled(false);
                //                rdoTransactionType_Credit.setEnabled(false);
            }}
    }
    
    private void productBased() {
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("TD")) {
            
            //            this.txtAmount.setEnabled(true);
            //            this.txtAmount.setEditable(true);
        } else {
            //            this.txtAmount.setEnabled(false);
            //            this.txtAmount.setEditable(true);
        }
        
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
            //            setEditFieldsEnable(false);
        }
    }
    private boolean checkExpiryDate(){
        java.util.Date expiryDt=DateUtil.getDateMMDDYYYY(transDetails.getExpiryDate());
        if(expiryDt !=null)
            if(DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(observable.getCurrentDate()),expiryDt)<0){
                int yesno=ClientUtil.confirmationAlert("Limit has Expired Do You Want allow Transaction");
                if(yesno==1){
                    observable.setOdExpired(true);
                    return true;
                    
                }
            }
        return false;
    }
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void savePerformed(){
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            ClientUtil.displayAlert(mandatoryMessage);
        }else{
            observable.doAction();
        }
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            
        }
    }
    
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    
    private void setEnable(){
        cboProdType.setEnabled(true);
        cboProdId.setEnabled(true);
        txtAccNo.setEnabled(true);
        btnAccNo.setEnabled(true);
        
    }
    
    
    public void btnDepositCancel(){
        afterSaveCancel = true;
        
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        HashMap hash = (HashMap) param;
        System.out.println("fill data  :"+hash);
        if(viewType.equals("CREDIT_ACC_NO")){
            HashMap accountMap=new HashMap();
            String accno="";
            String hashAccountno="";
            String msg="";
            String status=CommonUtil.convertObjToStr(hash.get("LOCKSTATUS"));
            String lockStatus="";
            
            if(rdoLock.isSelected()==true){
                lockStatus="Y";
                msg="Locked";
            }else{
                lockStatus="N";
                msg="UnLocked";
            }
            
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if( prodType != null && !prodType.equals("GL")){
                if(prodType.equals("MDS")) {
                    if(!lockStatus.equals(status)){
                        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTALNO")));
                        lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("MEMBERNAME")));
                    }else{
                        ClientUtil.showMessageWindow("Account is already"+msg);
                    }
                }else{
                    if(!lockStatus.equals(status)){
                        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                        lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                    }else{
                        ClientUtil.showMessageWindow("Account is already"+msg);
                    }
                }
            }else{
                
                txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            }
        }
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
        
        txtAccNo.setEnabled(false);             //To make this textBox non editable...
        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
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
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CTable lockTable;
    private com.see.truetransact.uicomponent.CScrollPane lockTableScrollPan;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panFreqency;
    private com.see.truetransact.uicomponent.CPanel panSalaryDeductionMapping;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel papProcess;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccounts;
    private com.see.truetransact.uicomponent.CRadioButton rdoAllAccounts;
    private com.see.truetransact.uicomponent.CRadioButton rdoLock;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLockGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoSpecificAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoUnLock;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    // End of variables declaration//GEN-END:variables
}
