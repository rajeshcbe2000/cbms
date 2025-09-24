/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalaryDeductionMappingUI.java
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
//import java.util.Date;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class SalaryDeductionMappingUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    SalaryDeductionMappingOB observable;
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
    private final static Logger log = Logger.getLogger(SalaryDeductionMappingUI.class);     //Logger
    List accountNoList=null;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private String mandatoryMessage;
    /** Creates new form CashTransaction */
    public SalaryDeductionMappingUI() {
        initComponents();
        initSetup();
        btnException.setVisible(false);
        btnVer.setVisible(false);
        btnDebitDetails.setVisible(false);
        btnEmployerRefNo.setEnabled(false);
        btnAccNo.setEnabled(false);
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
        btnDelete.setEnabled(true);
        // To disable the buttons(folder) in the Starting...
        observable.resetForm();                 // To reset all the fields in UI...
        observable.resetLable();                // To reset all the Lables in UI...
        observable.resetStatus();               // To reset the Satus in the UI...
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.salaryrecovery.SalaryDeductionMappingMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSalaryDeductionMapping);
        
        setHelpMessage();
        
        // Hide unwanted fields
        /*lblInputAmt.setVisible(false);
        lblInputCurrency.setVisible(false);
        txtInputAmt.setVisible(false);
        cboInputCurrency.setVisible(false);
         
        lbl
         .setVisible(false);
        cboProdType.setVisible(false);
        btnAccHdId.setVisible(false);*/
        
        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
        getUserDesignation();
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        
        try{
            observable = new SalaryDeductionMappingOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Authorize Button to be added...
    private void setFieldNames() {
        cboProdType.setName("cboProdType");
        lblProdType.setName("lblProdType");
        lblCustName.setName("lblCustName");
        lblAccountName.setName("lblAccountName");
        lblAmount.setName("lblAmount");
        txtAmount.setName("txtAmount");
        lblRemarks.setName("lblRemarks");
        txtRemarks.setName("txtRemarks");
        btnAccNo.setName("btnAccNo");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtAccNo.setName("txtAccNo");
        cboProdId.setName("cboProdId");
        lblAccNo.setName("lblAccNo");
        lblDeleteFlag.setName("lblDeleteFlag");
        chkDeleteFlag.setName("chkDeleteFlag");
        lblEmployerRefNo.setName("lblEmployerRefNo");
        txtEmployerRefNo.setName("txtEmployerRefNo");
        btnEmployerRefNo.setName("btnEmployerRefNo");
        lblCustName.setName("lblCustName");
        lblAccountName.setName("lblAccountName");
    }
    
    private void internationalize() {
        //CashTransactionRB resourceBundle = new CashTransactionRB();
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.salaryrecovery.SalaryDeductionMappingRB", ProxyParameters.LANGUAGE);
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblEmployerRefNo.setText(resourceBundle.getString("lblEmployerRefNo"));
        //        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        btnVer.setText(resourceBundle.getString("btnVer"));
        lblDeleteFlag.setText(resourceBundle.getString("lblDeleteFlag"));
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
        rdoTransactionType = new CButtonGroup();
        
    }
    
    public void update(Observable observed, Object arg) {
        
        //        observable.setSelectedBranchID(getSelectedBranchID());
        setSelectedBranchID(observable.getSelectedBranchID());
        if (observable.getCbmProdId()!=null)
            cboProdId.setModel(observable.getCbmProdId());
        
        txtAccNo.setText(observable.getTxtAccNo());
        txtEmployerRefNo.setText(observable.getEmployerRefNo());
        txtAmount.setText(observable.getTxtAmount());
        txtRemarks.setText(observable.getTxtRemarks());
        lblAccountName.setText("");
        lblCustName.setText("");
        
        if(observable.getDeleteFlag().equals("Y")){
            chkDeleteFlag.setSelected(true);
        }
        else{
            chkDeleteFlag.setSelected(false);
        }
        
        
        //        cboInputCurrency.setSelectedItem(observable.getCboInputCurrency());
        //        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        
        //
        //        //To set the  Name of the Account Holder...
        
        // Added by Rajesh
        
        
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
        
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setEmployerRefNo(txtEmployerRefNo.getText());
        if(chkDeleteFlag.isSelected()==true){
            observable.setDeleteFlag("Y");
            
        }else{
            observable.setDeleteFlag("N");
        }
    }
    
    public void setHelpMessage() {
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        
        
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //               cboProdId.setModel(observable.getCbmProdId());
        cboProdType.setModel(observable.getCbmProdType());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        
        txtAmount.setValidation(new CurrencyValidation(16,2));
        txtAccNo.setMaxLength(32);
        txtAccNo.setAllowAll(true);
        txtEmployerRefNo.setMaxLength(32);
        txtEmployerRefNo.setAllowAll(true);
        
        //        txtTokenNo.setValidation(new NumericValidation());
        
        
    }
    
    
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        
        btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnSave.isEnabled());
        btnReject.setEnabled(!btnSave.isEnabled());
        btnException.setEnabled(!btnSave.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnVer.setEnabled(!btnSave.isEnabled());
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    public void setButtonEnableDisable(boolean flag) {
        
        btnNew.setEnabled(flag);
        btnSave.setEnabled(flag);
        btnEdit.setEnabled(flag);
        btnDelete.setEnabled(!flag);
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
        btnException.setEnabled(flag);
        mitNew.setEnabled(flag);
        mitEdit.setEnabled(flag);
        mitDelete.setEnabled(!flag);
        btnCancel.setEnabled(!flag);
        mitSave.setEnabled(!flag);
        mitCancel.setEnabled(!flag);
        btnView.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSalaryDeductionMapping = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblEmployerRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        panEmployerRefNo = new com.see.truetransact.uicomponent.CPanel();
        txtEmployerRefNo = new com.see.truetransact.uicomponent.CTextField();
        btnEmployerRefNo = new com.see.truetransact.uicomponent.CButton();
        lblDeleteFlag = new com.see.truetransact.uicomponent.CLabel();
        chkDeleteFlag = new com.see.truetransact.uicomponent.CCheckBox();
        lblAccountName = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnVer = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDebitDetails = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(651, 450));
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

        panSalaryDeductionMapping.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalaryDeductionMapping.setMinimumSize(new java.awt.Dimension(600, 360));
        panSalaryDeductionMapping.setPreferredSize(new java.awt.Dimension(605, 370));
        panSalaryDeductionMapping.setLayout(new java.awt.GridBagLayout());

        panData.setMinimumSize(new java.awt.Dimension(340, 400));
        panData.setPreferredSize(new java.awt.Dimension(415, 150));
        panData.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblProductId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboProdId, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccNo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblRemarks, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        cboProdType.setPreferredSize(new java.awt.Dimension(21, 200));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboProdType, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(21, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtRemarks, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panAcctNo, gridBagConstraints);

        lblEmployerRefNo.setText("Employer Ref.No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblEmployerRefNo, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setPreferredSize(new java.awt.Dimension(21, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtAmount, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAmount, gridBagConstraints);

        panEmployerRefNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panEmployerRefNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panEmployerRefNo.setLayout(new java.awt.GridBagLayout());

        txtEmployerRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmployerRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployerRefNoActionPerformed(evt);
            }
        });
        txtEmployerRefNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmployerRefNoFocusLost(evt);
            }
        });
        panEmployerRefNo.add(txtEmployerRefNo, new java.awt.GridBagConstraints());

        btnEmployerRefNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployerRefNo.setToolTipText("Account No.");
        btnEmployerRefNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmployerRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployerRefNoActionPerformed(evt);
            }
        });
        panEmployerRefNo.add(btnEmployerRefNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panEmployerRefNo, gridBagConstraints);

        lblDeleteFlag.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblDeleteFlag, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panData.add(chkDeleteFlag, gridBagConstraints);

        lblAccountName.setForeground(new java.awt.Color(0, 51, 255));
        lblAccountName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountName.setMaximumSize(new java.awt.Dimension(300, 13));
        lblAccountName.setMinimumSize(new java.awt.Dimension(300, 13));
        lblAccountName.setPreferredSize(new java.awt.Dimension(300, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccountName, gridBagConstraints);

        lblCustName.setForeground(new java.awt.Color(0, 51, 255));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustName.setMaximumSize(new java.awt.Dimension(300, 13));
        lblCustName.setMinimumSize(new java.awt.Dimension(300, 13));
        lblCustName.setPreferredSize(new java.awt.Dimension(300, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panSalaryDeductionMapping.add(panData, gridBagConstraints);

        getContentPane().add(panSalaryDeductionMapping, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_VER.gif"))); // NOI18N
        btnVer.setToolTipText("Second Authorization");
        btnVer.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVer.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVer.setPreferredSize(new java.awt.Dimension(29, 27));
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        tbrHead.add(btnVer);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace57);

        btnDebitDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDebitDetails.setToolTipText("Enquiry Of Debit transactions");
        btnDebitDetails.setEnabled(false);
        btnDebitDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitDetailsActionPerformed(evt);
            }
        });
        tbrHead.add(btnDebitDetails);
        btnDebitDetails.getAccessibleContext().setAccessibleDescription("Enquiry Of Debit Transactions");

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtEmployerRefNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmployerRefNoFocusLost
        // TODO add your handling code here:
        String empno= txtEmployerRefNo.getText();
        HashMap hmap=new HashMap();
        hmap.put("EMP_REFNO_NEW",empno);
        List lst=ClientUtil.executeQuery("getName", hmap);
        hmap=null;
        hmap=(HashMap)lst.get(0);
        String name=CommonUtil.convertObjToStr(hmap.get("NAME"));
        lblCustName.setText(name);
    }//GEN-LAST:event_txtEmployerRefNoFocusLost
    
    private void txtEmployerRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployerRefNoActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_txtEmployerRefNoActionPerformed
    
    private void btnEmployerRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployerRefNoActionPerformed
        // TODO add your handling code here:
        callView("EMPREFNO");
        
    }//GEN-LAST:event_btnEmployerRefNoActionPerformed
    
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
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // Add your handling code here:
        callView("CREDIT_ACC_NO");// TODO add your handling code here:
        
        //        popUp(ACCNO);
        //        termLoanDetailsFlag = false;
        //        termLoansDetailsMap = null;
    }//GEN-LAST:event_btnAccNoActionPerformed
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        //        btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened
    
    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_btnVerActionPerformed
    
    
    private void callView(String currField) {
        viewType = currField;
        
        if(currField == "CREDIT_ACC_NO"){
            HashMap viewMap = new HashMap();
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if(!prodType.equals("GL")){
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
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
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else   if(currField == "EMPREFNO" ){
            HashMap statusmap=new HashMap();
            String status="DELETED";
            
            statusmap.put(CommonConstants.MAP_NAME,"getEmployerRefNo");
            
            HashMap wheremap=new HashMap();
            wheremap.put("STATUS",status);
            wheremap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            statusmap.put(CommonConstants.MAP_WHERE,wheremap);
            new ViewAll(this, statusmap).show();
            
        } else if(currField == "EDIT" ){
            HashMap editmap=new HashMap();
            editmap.put(CommonConstants.MAP_NAME,"getEditList");
            HashMap whereMap1=new HashMap();
            whereMap1.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            editmap.put(CommonConstants.MAP_WHERE,whereMap1);
            new ViewAll(this,editmap).show();
        }else if(currField == "DELETE"){
            HashMap deletemap=new HashMap();
            deletemap.put(CommonConstants.MAP_NAME,"getDeleteList");
            HashMap whereMap2=new HashMap();
            whereMap2.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            deletemap.put(CommonConstants.MAP_WHERE,whereMap2);
            new ViewAll(this, deletemap).show();
        } else if(currField =="VIEW"){
            HashMap viewmap=new HashMap();
            viewmap.put(CommonConstants.MAP_NAME,"getViewList");
            HashMap whereMap3=new HashMap();
            whereMap3.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            viewmap.put(CommonConstants.MAP_WHERE,whereMap3);
            new ViewAll(this,  viewmap).show();
        }
        
    }
    
    
    
    private void btnDebitDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        //        observable.setStatus();
        //        lblStatus.setText(observable.getLblStatus());
        
        
    }//GEN-LAST:event_btnDebitDetailsActionPerformed
    
    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        if(txtAccNo.getText().length() > 0){
            txtAccNoActionPerformed();
        }
    }//GEN-LAST:event_txtAccNoFocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        lblDeleteFlag.setVisible(true);
        chkDeleteFlag.setVisible(true);
        btnCheck();
        callView("VIEW");
    }//GEN-LAST:event_btnViewActionPerformed
    private boolean moreThanLoanAmountAlert(){
        
        return false;
    }
            private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
                //        // TODO add your handling code here:
                //        HashMap hash = new HashMap();
                //        String ACCOUNTNO = (String) txtAccNo.getText();
                //        if( observable.getProdType().equals("TD")){
                //            if (ACCOUNTNO.lastIndexOf("_")!=-1){
                //                hash.put("ACCOUNTNO", txtAccNo.getText());
                //            }else
                //                hash.put("ACCOUNTNO", txtAccNo.getText()+"_1");
                //        }else{
                //            hash.put("ACCOUNTNO", txtAccNo.getText());
                //        }
                //        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
                //        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                //        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                //        List actlst=null;
                //        List lst=null;
                //        HashMap notClosedMap = new HashMap();
                //        if( observable.getProdType().equals("TD")){
                //            actlst=ClientUtil.executeQuery("getNotClosedDeposits",hash);
                //            if(actlst!=null && actlst.size()>0){
                //                notClosedMap =(HashMap)actlst.get(0);
                //            }
                //        }
                //
                //        if( observable.getProdType().equals("TL"))
                //            actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
                //
                //        if( observable.getProdType().equals("OA"))
                //            observable.setAccountName(ACCOUNTNO);
                //
                //        if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")){
                //            if(rdoTransactionType_Debit.isSelected() || rdoTransactionType_Credit.isSelected()){
                //                if(observable.getProdType().equals("TL")){
                //                    if(actlst!=null && actlst.size()>0){
                //                        viewType = ACCNO;
                //                        updateOBFields();
                //                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                //                        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                //                        if( observable.getProdType().equals("TL")) {
                //                            if(rdoTransactionType_Debit.isSelected()) {
                //                                hash.put("PAYMENT","PAYMENT");
                //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                //                            }else if(rdoTransactionType_Credit.isSelected()){
                //                                if(observable.getProdType().equals("TL"))
                //                                    hash.put("RECEIPT","RECEIPT");
                //                                System.out.println("hash"+hash);
                //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                //                            }
                //                            fillData(hash);
                //                        }
                //                    }else{
                //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                //                        txtAccNo.setText("");
                ////                        txtAccNo.requestFocus();
                //                    }
                //                }else if(observable.getProdType().equals("TD")){
                //                    viewType = ACCNO;
                //                    updateOBFields();
                //                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                //                    hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                //                    if(actlst!=null && actlst.size()>0){
                //                        if(observable.getProdType().equals("TD")){
                //                            hash.put("RECEIPT","RECEIPT");
                //                            if(rdoTransactionType_Debit.isSelected()) {
                //                                //                                if(observable.getProdType().equals("TD")){
                //                                //                                    hash.put("PAYMENT","PAYMENT");
                //                                //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
                //                                //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                //                                //                                }else{
                //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
                //                                transDetails.setIsDebitSelect(true);
                //                            }else if(rdoTransactionType_Credit.isSelected()){
                //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                //                            }
                //                            hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
                //                            hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
                //                            hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
                //                            fillData(hash);
                //                        }
                //                    }else{
                //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                //                        txtAccNo.setText("");
                //                    }
                //                }
                //            }else{
                //                ClientUtil.showMessageWindow("Select Payment or Receipt ");
                //                txtAccNo.setText("");
                ////                txtAccNo.requestFocus();
                //                return;
                //            }
                //        }else if(observable.getProdType().equals("OA")){
                //            viewType = ACCNO;
                //            HashMap listMap = new HashMap();
                //            if(observable.getLblAccName().length()>0){
                //                updateOBFields();
                //                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                //                hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                //                lst=ClientUtil.executeQuery("Cash.getAccountList"
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                //                fillData(hash);
                //                observable.setLblAccName("");
                //            }else{
                //                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                //                txtAccNo.setText("");
                //            }
                //        }
            }//GEN-LAST:event_txtAccNoActionPerformed
            
            
            public void btnCheck(){
                btnCancel.setEnabled(true);
                btnSave.setEnabled(false);
                btnNew.setEnabled(false);
                btnDelete.setEnabled(false);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnException.setEnabled(false);
                btnEdit.setEnabled(false);
                btnView.setEnabled(false);
            }
            
            
            
            
            
            
            
	private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
            // Add your handling code here:
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
        
	private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
            // Add your handling code here:
            viewType="REJECT";
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            authorizeStatus(CommonConstants.STATUS_REJECTED);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
            lblDeleteFlag.setVisible(true);
            chkDeleteFlag.setVisible(true);
    }//GEN-LAST:event_btnRejectActionPerformed
        
        
        
        
        
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
                
                btnCancelActionPerformed(null);
                observable.setResultStatus();
            }
        }
        
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        viewType="AUTHORIZE";
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnDelete.setEnabled(false);
        btnView.setEnabled(false);
        lblDeleteFlag.setVisible(true);
        chkDeleteFlag.setVisible(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        
        if(isFilled){
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            java.util.Date curdt=ClientUtil.getCurrentDate();
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
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
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
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    
    
    
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        if (cboProdId.getSelectedIndex() > 0) {
            
            
            observable.setCboProdId((String) cboProdId.getSelectedItem());
            if( observable.getCboProdId().length() > 0){
                
                //When the selected Product Id is not empty string
                observable.setAccountHead();
                
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
                observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
                    
                    productBased();
                }
            }
            if(!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))){
                HashMap InterBranMap = new HashMap();
                InterBranMap.put("AC_HD_ID",observable.getTxtAccHd());
                List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
                InterBranMap=null;
                if(lst!=null && lst.size()>0){
                    InterBranMap=(HashMap)lst.get(0);
                    String IbAllowed=CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
                    if(IbAllowed.equals("N")){
                        ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
                        observable.resetForm();
                        
                    }
                }
            }
            //                ADDED HERE BY NIKHIL FOR PARTIAL WITHDRAWAL
            String prodId ="";
            prodId = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID",prodId.toString());
            List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            System.out.println("######## BEHAVES :"+behavesLiklst);
            if(behavesLiklst!=null && behavesLiklst.size()>0){
                prodMap= (HashMap)behavesLiklst.get(0);
                depBehavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                depPartialWithDrawalAllowed = CommonUtil.convertObjToStr(prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED"));
                System.out.println("$#%#$%#$%behavesLike:"+depBehavesLike);
                if(depBehavesLike.equals("RECURRING")){
                    
                }else{
                    
                }
            }else{
                depBehavesLike = "";
                depPartialWithDrawalAllowed = "";
                
            }
        }
    }//GEN-LAST:event_cboProdIdActionPerformed
    
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
    
	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
            // Add your handling code here:
            cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
        
	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
            //System.out.println("Came in Print Button Click");
            HashMap reportParamMap = new HashMap();
            //System.out.println("Screen ID in UI "+getScreenID());
            com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
            //                HashMap map = new HashMap();
            //                map.put("ACT_NUM",com.see.truetransact.clientutil.ttrintegration.TTIntegration.getActNum());
            //                System.out.println("&&&AC_NUM"+map);
            //                ClientUtil.executeQuery("updatingPassBookFlag",map);
    }//GEN-LAST:event_btnPrintActionPerformed
        
	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
            // Add your handling code here:
            observable.resetForm();
            observable.setStatus();
            lblStatus.setText("");
            setModified(false);
            txtEmployerRefNo.setEnabled(false);
            //            txtMemberName.setEnabled(false);
            cboProdType.setEnabled(false);
            cboProdId.setEnabled(false);
            txtAccNo.setEnabled(false);
            txtAmount.setEnabled(false);
            txtRemarks.setEnabled(false);
            chkDeleteFlag.setEnabled(false);
            btnNew.setEnabled(true);
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnSave.setEnabled(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(true);
            btnEmployerRefNo.setEnabled(false);
            btnAccNo.setEnabled(false);
            btnCancel.setEnabled(false);
            isFilled=false;
	}//GEN-LAST:event_btnCancelActionPerformed
        
	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
            // Add your handling code here:
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panData);
            String accno=observable.getTxtAccNo();
            
            updateOBFields();
            savePerformed() ;
            
}//GEN-LAST:event_btnSaveActionPerformed
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
            map.put("TRANS_DT", ClientUtil.getCurrentDate());
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
                return;
            }else{
                observable.doAction();
            }
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                btnCancelActionPerformed(null);
            }
        }
        
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        observable.resetForm();                 // Reset the fields in the UI to null...
        //        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        lblDeleteFlag.setVisible(true);
        chkDeleteFlag.setVisible(true);
        callView("DELETE");
        //        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
            // Add your handling code here:
            
            observable.resetForm();                 // Reset the fields in the UI to null...
            observable.resetLable();                // Reset the Editable Lables in the UI to null...
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
            //            popUp(EDIT);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnNew.setEnabled(false);
            btnDelete.setEnabled(false);
            chkDeleteFlag.setEnabled(true);
            btnAccNo.setEnabled(true);
            btnView.setEnabled(false);
            btnEmployerRefNo.setEnabled(true);
            lblDeleteFlag.setVisible(true);
            chkDeleteFlag.setVisible(true);
            callView("EDIT");
            
    }//GEN-LAST:event_btnEditActionPerformed
        
        // To display the All the Product Id's which r having status as
        // created or updated, in a table...
        
        private void setEnable(){
            cboProdType.setEnabled(false);
            cboProdId.setEnabled(false);
            txtAccNo.setEnabled(false);
            btnAccNo.setEnabled(false);
            txtAmount.setEnabled(true);
            txtRemarks.setEnabled(true);
            btnEmployerRefNo.setEnabled(false);
        }
        
        
        public void btnDepositCancel(){
            afterSaveCancel = true;
            btnCancelActionPerformed(null);
            btnCloseActionPerformed(null);
        }
        
        
        
        
        
        
        // this method is called automatically from ViewAll...
        public void fillData(Object param) {
            HashMap hash = (HashMap) param;
            System.out.println("fill data  :"+hash);
            if(viewType.equals("CREDIT_ACC_NO")){
                HashMap accountMap=new HashMap();
                String accno="";
                String hashAccountno="";
                String empno=txtEmployerRefNo.getText();
                String prodid="";
                String prodType="";
                if(hash.containsKey("ACCOUNTNO")){
                    hashAccountno=CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    prodid= ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
                    prodType = (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
                    HashMap hmap=new HashMap();
                    String Accountno=CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    hmap.put("EMP_REFNO_NEW",empno);
                    hmap.put("ACT_NUM",Accountno);
                    List list=ClientUtil.executeQuery("getAccountNumberListForEmp", hmap);
                    hmap=null;
                    if(list.size()>0 && list!=null){
                        for(int j=0;j<list.size();j++){
                            hmap=(HashMap)list.get(j);
                            String actNo=CommonUtil.convertObjToStr(hmap.get("ACT_NUM"));
                            lblAccountName.setText(CommonUtil.convertObjToStr(hmap.get("FNAME")));
                        }
                    }else{
                        int a= ClientUtil.confirmationAlert("Account does not belong to this Customer, Do you want to continue?");
                        int b=0;
                        if(a!=b){
                            txtAccNo.setText("");
                            return;
                        }
                        else
                            lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                    }
                }
                else{
                    hashAccountno=CommonUtil.convertObjToStr(hash.get("A/C HEAD"));
                    lblAccountName.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
                }
                HashMap hashmap=new HashMap();
                if(prodType.equals("AD")){
                    hashmap.put("ACCT_NUM",hashAccountno);
                    List locklist=ClientUtil.executeQuery("getLockStatusforAdvances", hashmap);
                    hashmap=null;
                    hashmap=(HashMap)locklist.get(0);
                    String salrecovery=CommonUtil.convertObjToStr(hashmap.get("SALARY_RECOVERY"));
                    if(salrecovery.equals("Y")){
                        ClientUtil.displayAlert("Account already under salary recovery scheme");
                        txtAccNo.setText("");
                        return;
                    }
                }
                if(accountNoList!=null && accountNoList.size()>0){
                    for(int i=0;i<accountNoList.size();i++){
                        accountMap=(HashMap)accountNoList.get(i);
                        accno=CommonUtil.convertObjToStr(accountMap.get("MAP_ACT_NUM"));
                        if(accno.equals(hashAccountno)){
                            ClientUtil.displayAlert("Record Already Exists for this Account");
                            lblAccountName.setText("");
                            return;
                        }
                    }
                }if(!accno.equals(hashAccountno)){
                    prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                    if( prodType != null && !prodType.equals("GL")){
                        if(prodType.equals("TD")){
                            hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                        }
                        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                        
                    }else{
                        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                        
                    }
                    
                }
                
            }
            if(viewType.equals("EMPREFNO")){
                txtEmployerRefNo.setText(CommonUtil.convertObjToStr(hash.get("EMP_REFNO_NEW")));
                lblCustName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                String refno=txtEmployerRefNo.getText();
                HashMap hmap=new HashMap();
                hmap.put("EMP_REF_NO",refno);
                accountNoList=ClientUtil.executeQuery("getsalaryDeductionmappingAccountNo", hmap);
                
            }if(viewType.equals("EDIT") || viewType.equals("DELETE")|| viewType.equals("AUTHORIZE") || viewType.equals("REJECT") || viewType.equals("VIEW")){
                String emprefno=CommonUtil.convertObjToStr(hash.get("EMP_REFNO_NEW"));
                HashMap hmap=new HashMap();
                if((hash.containsKey("MAP_PROD_ID")) && (!CommonUtil.convertObjToStr(hash.get("MAP_PROD_ID")).equals(""))){
                    List lst=ClientUtil.executeQuery("getSalaryDeductionMapping", hash);
                    hmap=(HashMap)lst.get(0);
                }
                else{
                    List lst1=ClientUtil.executeQuery("getSalaryDeductionMappingForGeneralLedger", hash);
                    hmap=(HashMap)lst1.get(0);
                }
                observable.populateData(hmap);
                lblCustName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                if(hmap.containsKey("FNAME")){
                    lblAccountName.setText(CommonUtil.convertObjToStr(hmap.get("FNAME")));
                }else{
                    lblAccountName.setText(CommonUtil.convertObjToStr(hmap.get("AC_HD_DESC")));
                }
                isFilled = true;
                if(viewType.equals("EDIT")){
                    setEnable();
                    btnEdit.setEnabled(false);
                }
                if(viewType.equals("AUTHORIZE")|| viewType.equals("DELETE") || viewType.equals("VIEW")){
                    cboProdId.setEnabled(false);
                    btnDelete.setEnabled(false);
                }
            }
        }
        
        
        private void txtAccNoActionPerformed() {
            HashMap hash = new HashMap();
            String ACCOUNTNO = (String) txtAccNo.getText();
            //        observable.setProdType("");
            if (/*(!(observable.getProdType().length()>0)) && */ACCOUNTNO.length()>0) {
                
                if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                    txtAccNo.setText(observable.getTxtAccNo());
                    ACCOUNTNO = (String) txtAccNo.getText();
                    cboProdId.setModel(observable.getCbmProdId());
                    cboProdIdActionPerformed(null);
//                    txtAccNo.setText(ACCOUNTNO);
                    String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                    observable. setAccountName(ACCOUNTNO,prodType);
                    
                    HashMap hmap=new HashMap();
                    String acno= txtAccNo.getText();
                    String empno= txtEmployerRefNo.getText();
                    hmap.put("ACT_NUM",acno);
                    hmap.put("EMP_REFNO_NEW",empno);
                    
                    
                    List list=ClientUtil.executeQuery("getAccountNumberListForEmp", hmap);
                    hmap=null;
                    if(list.size()>0 && list!=null){
                        for(int j=0;j<list.size();j++){
                            hmap=(HashMap)list.get(j);
                            String actNo=CommonUtil.convertObjToStr(hmap.get("ACT_NUM"));
                            lblAccountName.setText(CommonUtil.convertObjToStr(hmap.get("FNAME")));
                        }
                    }else{
                        int a= ClientUtil.confirmationAlert("Account No is not belongs to this Customer, Do you want to continue?");
                        int b=0;
                        if(a!=b){
                            txtAccNo.setText("");
                            return;
                        }else
                            lblAccountName.setText(observable.getLblAccName());
                    }
                    prodType = (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
                    if(prodType.equals("AD")){
                        HashMap hashmap =new HashMap();
                        hashmap.put("ACCT_NUM",ACCOUNTNO);
                        List locklist=ClientUtil.executeQuery("getLockStatusforAdvances", hashmap);
                        hashmap=null;
                        hashmap=(HashMap)locklist.get(0);
                        String lckstatus=CommonUtil.convertObjToStr(hashmap.get("SALARY_RECOVERY"));
                        if(lckstatus.equals("Y")){
                            ClientUtil.displayAlert(" it is locked");
                            txtAccNo.setText("");
                            return;
                        }
                    }
                } else {
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtAccNo.setText("");
                    return;
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
        
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        observable.resetForm();                 // Reset the fields in the UI to null...
        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        setMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(this, true);   // Enables the panel...
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();                 // To set the Value of lblStatus...
        lblStatus.setText(observable.getLblStatus());
        // To enable the buttons(folder) in the UI...
        observable.setInitiatorChannelValue();  // To Set Initiator Type as Cashier...
        //        textDisable();                          // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
        setModified(true);
        
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        
        observable.setCbmProdId("");
        cboProdId.setModel(observable.getCbmProdId());
        btnEmployerRefNo.setEnabled(true);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnAccNo.setEnabled(true);
        lblDeleteFlag.setVisible(false);
        chkDeleteFlag.setVisible(false);
        btnNew.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
    private void textDisable(){
        
        txtAccNo.setEnabled(false);             //To make this textBox non editable...
        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
	private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
            // Add your handling code here:
            btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
        
	private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
            // Add your handling code here:
            btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
        
	private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
            // Add your handling code here:
            btnDeleteActionPerformed(evt);
	}//GEN-LAST:event_mitDeleteActionPerformed
        
	private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
            // Add your handling code here:
            btnEditActionPerformed(evt);
	}//GEN-LAST:event_mitEditActionPerformed
        
	private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
            // Add your handling code here:
            btnNewActionPerformed(evt);
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
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDebitDetails;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployerRefNo;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnVer;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkDeleteFlag;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountName;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblDeleteFlag;
    private com.see.truetransact.uicomponent.CLabel lblEmployerRefNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panEmployerRefNo;
    private com.see.truetransact.uicomponent.CPanel panSalaryDeductionMapping;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtEmployerRefNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
