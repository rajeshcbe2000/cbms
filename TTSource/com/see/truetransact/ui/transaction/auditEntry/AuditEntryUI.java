/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuditEntryUI.java
 *
 * Created on February 25, 2004, 11:55 AM
 */

package com.see.truetransact.ui.transaction.auditEntry;

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
import com.see.truetransact.ui.common.viewall.ViewOrgOrRespUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Logger;
//import java.util.Date;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.ViewOrgOrRespUI;
import com.see.truetransact.ui.common.viewall.ViewRespUI;
import java.util.*;

/**
 *
 * @author  rahul, bala
 * @todoh Add other modules into the transaction
 *
 */
public class AuditEntryUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    AuditEntryOB observable;
    final int EDIT=0, DELETE=1, ACCNO=2, AUTHORIZE=3, ACCTHDID = 4, VIEW = 5, LINK_BATCH_TD=6, LINK_BATCH=7, DEBIT_DETAILS=8,PAN_NUM=9,TELLER_ENTRY_DETIALS=10;
    int viewType=-1;
    boolean isFilled = false;
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
    private final static Logger log = Logger.getLogger(AuditEntryUI.class);     //Logger
    List chqBalList=null;
    private HashMap asAndWhenMap =null;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private double orgOrRespAmout=0.0;
    private String orgOrRespBranchId="";
    private String orgOrRespBranchName="";
    private String orgBranch="";
    private String orgBranchName="";
    private String orgOrRespCategory="";
    private String orgOrRespAdviceNo="";
    private java.util.Date orgOrRespAdviceDt=null;
    private String orgOrRespTransType="";
    private String orgOrRespDetails="";
    ViewOrgOrRespUI vieworgOrRespUI=null;
    ViewRespUI viewRespUI=null;
    private Date currDt = null;
    /** Creates new form CashTransaction */
    public AuditEntryUI() {
        initComponents();
        initSetup();
        
        //transDetails = new TransDetailsUI(panLableValues);
        
        //lblInputCurrency.setVisible(false);
        //cboInputCurrency.setVisible(false);
        //lblInputAmt.setVisible(false);
       // txtInputAmt.setVisible(false);
        btnCurrencyInfo.setVisible(false);
        
//        txtAccHdId.setEditable(false);
        btnDelete.setEnabled(false);
        //        txtAccNo.setEditable(false);
        btnDebitDetails.setEnabled(true);
        btnDebitDetails.setVisible(true);
//        txtPanNo.setEditable(false);
       // btnPanNo.setEnabled(false);
        btnViewTermLoanDetails.setVisible(true);
        btnViewTermLoanDetails.setEnabled(false);
      //  btnDenomination.setVisible(false);
        currDt = ClientUtil.getCurrentDate();
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
        enableDisableButtons(false);                 // To disable the buttons(folder) in the Starting...
        observable.resetForm();                 // To reset all the fields in UI...
        observable.resetLable();                // To reset all the Lables in UI...
        observable.resetStatus();               // To reset the Satus in the UI...
        
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.auditEntry.AuditEntryMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCashTransaction);
        
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
       // btnAccHdId.setEnabled(false);
        btnAccNo.setEnabled(false);
      //  btnOrgOrResp.setVisible(false);
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        //        observable = CashTransactionOB.getInstance(this);
        try{
            observable = new AuditEntryOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Authorize Button to be added...
    private void setFieldNames() {
        cboProdType.setName("cboProdType");
        lblProdType.setName("lblProdType");
       // btnAccHdId.setName("btnAccHdId");
       // txtAccHdId.setName("txtAccHdId");
        btnAccNo.setName("btnAccNo");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtAccNo.setName("txtAccNo");
        //txtInitiatorChannel.setName("txtInitiatorChannel");
        //cboInputCurrency.setName("cboInputCurrency");
        //cboInstrumentType.setName("cboInstrumentType");
        cboProdId.setName("cboProdId");
       // lblAccHd.setName("lblAccHd");
       // lblAccHdDesc.setName("lblAccHdDesc");
        //lblAccHdId.setName("lblAccHdId");
       // lblAccName.setName("lblAccName");
        lblAccNo.setName("lblAccNo");
        lblAmount.setName("lblAmount");
        ///lblInitiatorChannel.setName("lblInitiatorChannel");
        //lblInitiatorID.setName("lblInitiatorID");
        //lblInitiatorIDDesc.setName("lblInitiatorIDDesc");
        //lblInputAmt.setName("lblInputAmt");
        //lblInputCurrency.setName("lblInputCurrency");
        //lblInstrumentDate.setName("lblInstrumentDate");
        //lblInstrumentNo.setName("lblInstrumentNo");
        //lblInstrumentType.setName("lblInstrumentType");
        lblMsg.setName("lblMsg");
        lblParticulars.setName("lblParticulars");
        lblNarration.setName("lblNarration");
        lblProdId.setName("lblProdId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        //lblTokenNo.setName("lblTokenNo");
        //lblTransactionDate.setName("lblTransactionDate");
        //lblTransactionDateDesc.setName("lblTransactionDateDesc");
        //lblTransactionID.setName("lblTransactionID");
        //blTransactionIDDesc.setName("lblTransactionIDDesc");
        lblTransactionType.setName("lblTransactionType");
        mbrMain.setName("mbrMain");
        //panAccHd.setName("panAccHd");
        panCashTransaction.setName("panCashTransaction");
        panData.setName("panData");
        //panLableValues.setName("panLableValues");
        //panLables.setName("panLables");
        //panInstrumentNo.setName("panInstrumentNo");
        panStatus.setName("panStatus");
        //panTransaction.setName("panTransaction");
        panTransactionType.setName("panTransactionType");
        rdoTransactionType_Credit.setName("rdoTransactionType_Credit");
        rdoTransactionType_Debit.setName("rdoTransactionType_Debit");
        tdtDate.setName("tdtInstrumentDate");
        txtAmount.setName("txtAmount");
        //txtInputAmt.setName("txtInputAmt");
        //txtInstrumentNo1.setName("txtInstrumentNo1");
        //txtInstrumentNo2.setName("txtInstrumentNo2");
        txtParticulars.setName("txtParticulars");
        txtNarration.setName("txtNarration");
        //txtTokenNo.setName("txtTokenNo");
        //lblPanNo.setName("lblPanNo");
        //txtPanNo.setName("txtPanNo");
        //lblAuthBy.setName("lblAuthBy");
        //lblAuthBydesc.setName("lblAuthBydesc");
        btnVer.setName("btnVer");
    }
    
    private void internationalize() {
        //CashTransactionRB resourceBundle = new CashTransactionRB();
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.auditEntry.AuditEntryRB", ProxyParameters.LANGUAGE);
        btnClose.setText(resourceBundle.getString("btnClose"));
        //lblTokenNo.setText(resourceBundle.getString("lblTokenNo"));
        //lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //((javax.swing.border.TitledBorder)panTransaction.getBorder()).setTitle(resourceBundle.getString("panTransaction"));
        //lblInitiatorChannel.setText(resourceBundle.getString("lblInitiatorChannel"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //lblTransactionID.setText(resourceBundle.getString("lblTransactionID"));
        //lblInitiatorID.setText(resourceBundle.getString("lblInitiatorID"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //lblAccName.setText(resourceBundle.getString("lblAccName"));
        //lblInputAmt.setText(resourceBundle.getString("lblInputAmt"));
        //lblTransactionDate.setText(resourceBundle.getString("lblTransactionDate"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //lblTransactionDateDesc.setText(resourceBundle.getString("lblTransactionDateDesc"));
        //lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
        //lblAccHdDesc.setText(resourceBundle.getString("lblAccHdDesc"));
        //lblAccHd.setText(resourceBundle.getString("lblAccHd"));
        rdoTransactionType_Debit.setText(resourceBundle.getString("rdoTransactionType_Debit"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        rdoTransactionType_Credit.setText(resourceBundle.getString("rdoTransactionType_Credit"));
        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
        //lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        //lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        ((javax.swing.border.TitledBorder)panData.getBorder()).setTitle(resourceBundle.getString("panData"));
        //lblInputCurrency.setText(resourceBundle.getString("lblInputCurrency"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblNarration.setText(resourceBundle.getString("lblNarration"));
        //((javax.swing.border.TitledBorder)panLableValues.getBorder()).setTitle(resourceBundle.getString("panLableValues"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        //lblTransactionIDDesc.setText(resourceBundle.getString("lblTransactionIDDesc"));
        //lblInitiatorIDDesc.setText(resourceBundle.getString("lblInitiatorIDDesc"));
        //lblPanNo.setText(resourceBundle.getString("lblPanNo"));
        //lblAuthBy.setText(resourceBundle.getString("lblAuthBy"));
        btnVer.setText(resourceBundle.getString("btnVer"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboAccNo", new Boolean(true));
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
        rdoTransactionType.remove(rdoTransactionType_Credit);
        rdoTransactionType.remove(rdoTransactionType_Debit);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        rdoTransactionType = new CButtonGroup();
        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType.add(rdoTransactionType_Debit);
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
//        observable.setSelectedBranchID(getSelectedBranchID());
         setSelectedBranchID(observable.getSelectedBranchID());
        if (observable.getCbmProdId()!=null)
            cboProdId.setModel(observable.getCbmProdId());
   //   cboProdType.setSelectedItem(observable.getCboProdType());
//        cboProdId.setSelectedItem(observable.getCboProdId());
        txtAccNo.setText(observable.getTxtAccNo());
        //txtInitiatorChannel.setText(observable.getTxtInitiatorChannel());
        rdoTransactionType_Debit.setSelected(observable.getRdoTransactionType_Debit());
        rdoTransactionType_Credit.setSelected(observable.getRdoTransactionType_Credit());
        //txtInputAmt.setText(observable.getTxtInputAmt());
//        cboInputCurrency.setSelectedItem(observable.getCboInputCurrency());
//        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        //txtInstrumentNo1.setText(observable.getTxtInstrumentNo1());
        //txtInstrumentNo2.setText(observable.getTxtInstrumentNo2());
        //tdtInstrumentDate.setDateValue(observable.getTdtInstrumentDate());
        //txtTokenNo.setText(observable.getTxtTokenNo());
        txtAmount.setText(observable.getTxtAmount());
        txtParticulars.setText(observable.getTxtParticulars());
        txtNarration.setText(observable.getTxtNarration());
        // To set the value of Account Head...
        //txtAccHdId.setText(observable.getTxtAccHd());
        //        lblAccHdDesc.setText(observable.getLblAccHdDesc());
        //
        //        //To set the  Name of the Account Holder...
  //String pType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        // Added by Rajesh
        String prevActNum = observable.getLblAccName();
//        if (pType.length()>0) {
//            if (!pType.equals("GL")) {
//                observable.setAccountName(observable.getTxtAccNo());
//                //lblAccName.setText(observable.getLblAccName());
//                //lblHouseName.setText(observable.getLblHouseName());
//                observable.setAccountHead();
//                //lblAccHdDesc.setText(observable.getLblAccHdDesc());
//            } else {
//                observable.setAccountName(observable.getTxtAccHd());
//                //lblAccHdDesc.setText(observable.getLblAccName());
//            }
//            if (pType.equals("GL") && txtAccNo.getText().length()>0) {
//                //lblAccName.setText(prevActNum);
//            }
//        } else {
//            //lblAccName.setText(observable.getLblAccName());
//            //lblAccHdDesc.setText(observable.getLblAccName());
//        }
        
        
        //To set the  Transaction ID, Transaction Date and Initator ID...
        //lblTransactionIDDesc.setText(observable.getLblTransactionId());
        //lblTransactionDateDesc.setText(observable.getLblTransDate());
        //lblInitiatorIDDesc.setText(observable.getLblInitiatorId());
        //lblAuthBydesc.setText(observable.getAuthorizeBy());
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());
        
        addRadioButtons();
        //        rdoTransactionType_DebitActionPerformed(null);
        //txtPanNo.setText(observable.getTxtPanNo());
       // populateInstrumentType();
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
                    if (rdoTransactionType_Debit.isSelected())
                ClientUtil.displayAlert("MINOR ACCOUNT");
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
        observable.setTxtScreenName("Cash Transactions");
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtAccNo((String) txtAccNo.getText());
        //observable.setTxtInitiatorChannel((String) txtInitiatorChannel.getText());
        observable.setRdoTransactionType_Debit(rdoTransactionType_Debit.isSelected());
        observable.setRdoTransactionType_Credit(rdoTransactionType_Credit.isSelected());
        //observable.setTxtInputAmt(txtInputAmt.getText());
       // observable.setCboInputCurrency((String) cboInputCurrency.getSelectedItem());
       // observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
       // observable.setTxtInstrumentNo1(txtInstrumentNo1.getText());
       // observable.setTxtInstrumentNo2(txtInstrumentNo2.getText());
        //observable.setTdtInstrumentDate(tdtInstrumentDate.getDateValue());
       // observable.setTxtTokenNo(txtTokenNo.getText());
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtParticulars(txtParticulars.getText());
        observable.setTxtNarration(txtNarration.getText());
        //To set the Value of Account HeadAccount Desc...
        //observable.setTxtAccHd(txtAccHdId.getText());
       // observable.setLblAccHdDesc(lblAccHdDesc.getText());
        
        //To set the  Name of the Account Holder...
        //observable.setLblAccName(lblAccName.getText());
        
        //To set the  Transaction ID, Transaction Date and Initator ID...
        //observable.setLblTransactionId(lblTransactionIDDesc.getText());
        //observable.setLblTransDate(lblTransactionDateDesc.getText());
       // observable.setLblInitiatorId(lblInitiatorIDDesc.getText());
       // observable.setTxtPanNo(txtPanNo.getText());
    }
    
    public void setHelpMessage() {
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
       // txtInitiatorChannel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInitiatorChannel"));
        rdoTransactionType_Debit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransactionType_Debit"));
        //txtInputAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInputAmt"));
        //cboInputCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInputCurrency"));
       // cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        //txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        //txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        //tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        //txtTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenNo"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
        txtNarration.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNarration"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //        cboProdId.setModel(observable.getCbmProdId());
       // cboProdType.setModel(observable.getCbmProdType());
        //cboInputCurrency.setModel(observable.getCbmInputCurrency());
        //cboInstrumentType.setModel(observable.getCbmInstrumentType());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        //        txtInputAmt.setMaxLength(16);
        //txtInputAmt.setValidation(new CurrencyValidation(14, 2));
        
        //        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        
        //txtInstrumentNo1.setAllowNumber(true);
        //txtInstrumentNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        //txtInstrumentNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        
        //txtTokenNo.setAllowNumber(true);
        //txtTokenNo.setMaxLength(16);
        //        txtTokenNo.setValidation(new NumericValidation());
        
        txtParticulars.setMaxLength(64);
        txtAccNo.setAllowAll(true);
        txtParticulars.setAllowAll(true);
        txtNarration.setMaxLength(128);
        txtNarration.setAllowAll(true);
    }
    
    private void setEditFieldsEnable(boolean yesno) {
        rdoTransactionType_Credit.setEnabled(yesno);
        rdoTransactionType_Debit.setEnabled(yesno);
        txtAccNo.setEnabled(yesno);
        btnAccNo.setEnabled(yesno);
        //btnAccHdId.setEnabled(yesno);
        cboProdId.setEnabled(yesno);
        cboProdType.setEnabled(yesno);
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
        panCashTransaction = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        panAuditDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoTransactionType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransactionType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAmt = new com.see.truetransact.uicomponent.CPanel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        btnCurrencyInfo = new com.see.truetransact.uicomponent.CButton();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        btnViewTermLoanDetails = new com.see.truetransact.uicomponent.CButton();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnSaveNew = new com.see.truetransact.uicomponent.CButton();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        panTransInfo = new com.see.truetransact.uicomponent.CPanel();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransList = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
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
        btnVer = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(680, 675));
        setPreferredSize(new java.awt.Dimension(680, 675));
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

        panCashTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTransaction.setMinimumSize(new java.awt.Dimension(625, 580));
        panCashTransaction.setPreferredSize(new java.awt.Dimension(625, 580));
        panCashTransaction.setLayout(new java.awt.GridBagLayout());

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panData.setMinimumSize(new java.awt.Dimension(510, 480));
        panData.setPreferredSize(new java.awt.Dimension(510, 480));
        panData.setLayout(new java.awt.GridBagLayout());

        panAuditDetails.setMinimumSize(new java.awt.Dimension(450, 242));
        panAuditDetails.setPreferredSize(new java.awt.Dimension(450, 242));
        panAuditDetails.setLayout(new java.awt.GridBagLayout());

        lblTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblTransactionType, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(panAcctNo, gridBagConstraints);

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblDate, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblProdType, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(cboProdId, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "General Ledger ", "Operative Account", "Suspense Account" }));
        cboProdType.setSelectedIndex(-1);
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(cboProdType, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(160, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(150, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoTransactionType.add(rdoTransactionType_Debit);
        rdoTransactionType_Debit.setText("Payment");
        rdoTransactionType_Debit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_DebitActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Debit, new java.awt.GridBagConstraints());

        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType_Credit.setText("Receipt");
        rdoTransactionType_Credit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTransactionType_Credit.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoTransactionType_Credit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_CreditActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Credit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panAuditDetails.add(panTransactionType, gridBagConstraints);

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblParticulars, gridBagConstraints);

        txtNarration.setMinimumSize(new java.awt.Dimension(200, 21));
        txtNarration.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(txtNarration, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblProdId, gridBagConstraints);

        txtParticulars.setMinimumSize(new java.awt.Dimension(200, 21));
        txtParticulars.setPreferredSize(new java.awt.Dimension(100, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(txtParticulars, gridBagConstraints);

        lblNarration.setText("Member Name/Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblNarration, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblAmount, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(lblAccNo, gridBagConstraints);

        panAmt.setMaximumSize(new java.awt.Dimension(500, 29));
        panAmt.setMinimumSize(new java.awt.Dimension(250, 29));
        panAmt.setPreferredSize(new java.awt.Dimension(250, 29));
        panAmt.setLayout(new java.awt.GridBagLayout());

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(txtAmount, gridBagConstraints);

        btnCurrencyInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCurrencyInfo.setToolTipText("Save");
        btnCurrencyInfo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnCurrencyInfo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCurrencyInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurrencyInfoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(btnCurrencyInfo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAmt.add(lblSpace, gridBagConstraints);

        btnViewTermLoanDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewTermLoanDetails.setText("View");
        btnViewTermLoanDetails.setMaximumSize(new java.awt.Dimension(72, 27));
        btnViewTermLoanDetails.setMinimumSize(new java.awt.Dimension(82, 27));
        btnViewTermLoanDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTermLoanDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmt.add(btnViewTermLoanDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(panAmt, gridBagConstraints);

        tdtDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuditDetails.add(tdtDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panData.add(panAuditDetails, gridBagConstraints);

        btnAdd.setText("Add");
        btnAdd.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panButton.add(btnAdd);

        btnSaveNew.setText("Save");
        btnSaveNew.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnSaveNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNewActionPerformed(evt);
            }
        });
        panButton.add(btnSaveNew);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panData.add(panButton, gridBagConstraints);

        panTransInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Info"));
        panTransInfo.setMinimumSize(new java.awt.Dimension(450, 150));
        panTransInfo.setName("panTransInfo"); // NOI18N
        panTransInfo.setPreferredSize(new java.awt.Dimension(450, 150));
        panTransInfo.setLayout(new java.awt.GridBagLayout());

        tblTransList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Account No", "Batch ID", "Trans ID", "Amount", "Type"
            }
        ));
        tblTransList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransListMouseClicked(evt);
            }
        });
        srpTransDetails.setViewportView(tblTransList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransInfo.add(srpTransDetails, gridBagConstraints);

        cPanel3.add(panTransInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panData.add(cPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panCashTransaction.add(panData, gridBagConstraints);

        getContentPane().add(panCashTransaction, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace33);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace35);

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

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
         btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        observable.setStatus();
//        lblStatus.setText(observable.getLblStatus());
          if (viewType!=10){
            viewType = TELLER_ENTRY_DETIALS;
        popUp(TELLER_ENTRY_DETIALS);
        btnCheck();
        btnVer.setEnabled(true);
        btnDebitDetails.setEnabled(false);
          }
          else if (viewType==10){
              HashMap where =new HashMap();
              //where.put("TRANS_ID",lblTransactionIDDesc.getText());
              where.put("USER",TrueTransactMain.USER_ID);
              where.put("TRANS_DT",currDt.clone());
              where.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
              ClientUtil.execute("updateTellerEntryDetails", where);
              viewType = -1;
              btnCancelActionPerformed(null);
          }
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnDebitDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitDetailsActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        observable.setStatus();
//        lblStatus.setText(observable.getLblStatus());
          if (viewType!=8){
            viewType = DEBIT_DETAILS;
        popUp(DEBIT_DETAILS);
        btnCheck();
          }
          else if (viewType==8){
              HashMap where =new HashMap();
              //where.put("TRANS_ID",lblTransactionIDDesc.getText());
              where.put("PAYMENT_BY",TrueTransactMain.USER_ID);
              where.put("TRANS_DT",currDt.clone());
              where.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
              ClientUtil.execute("updateDebitDetails", where);
              viewType = -1;
              btnCancelActionPerformed(null);
          }
    }//GEN-LAST:event_btnDebitDetailsActionPerformed
        
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
            private boolean moreThanLoanAmountAlert(){
         if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(rdoTransactionType_Credit.isSelected()==true  && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW && prodType.equals("TL")){
            double totalLoanAmt=setEMIAmount();
            HashMap allAmtMap=observable.getALL_LOAN_AMOUNT();
             if(allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue()>0)
                 totalLoanAmt-=CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
            if(allAmtMap.containsKey("CLEAR_BALANCE") && CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue()<0)
                totalLoanAmt+=-CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue();
                double actualAmt=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
            if(actualAmt>=totalLoanAmt){
                  int message =ClientUtil.confirmationAlert("Entered Transaction Amount is equal to/more than the Outstanding Loan Amount,"+"\n"+"Do You Want to Close the A/c?");
                    if(message ==0){
                        HashMap hash =new HashMap();
                        CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setSelectedBranchID(getSelectedBranchID());
                        TrueTransactMain.showScreen(frm);
                        hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                        hash.put("ACCOUNT NUMBER",txtAccNo.getText());
//                        hash.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected()));
                          hash.put("PROD_ID",CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
                        frm.fillData(hash);
                    }
                return true;
            }
        }
        }
         return false;
    }        
    private void txtAccNoActionPerformed() {
        if(txtAccNo.getText().length()>0){
            if(!txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))){
        // TODO add your handling code here:
        boolean debit = rdoTransactionType_Debit.isSelected();
        boolean credit = rdoTransactionType_Credit.isSelected();
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtAccNo.getText();
//        observable.setProdType("");
        if (/*(!(observable.getProdType().length()>0)) && */ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProdId.setModel(observable.getCbmProdId());
                cboProdIdActionPerformed(null);
//                txtAccNo.setText(ACCOUNTNO);
                txtAccNo.setText(observable.getTxtAccNo());
                ACCOUNTNO = (String) txtAccNo.getText();
                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                if(debit)
                    rdoTransactionType_Debit.setSelected(true);
                if(credit)
                    rdoTransactionType_Credit.setSelected(true);
                setSelectedBranchID(observable.getSelectedBranchID());
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccNo.setText("");
                        //Added BY Suresh
                        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                            txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                        }
                return;
            }
        }
        if( observable.getProdType().equals("TD")){
            if (ACCOUNTNO.lastIndexOf("_")!=-1){
                hash.put("ACCOUNTNO", txtAccNo.getText());
            }else
                hash.put("ACCOUNTNO", txtAccNo.getText()+"_1");
        }else{
            hash.put("ACCOUNTNO", txtAccNo.getText());
        }
        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        List actlst=null;
//        List lst=null;
        HashMap notClosedMap = new HashMap();
        if( observable.getProdType().equals("TD")){
            if(credit == true)
                hash.put("CREDIT_TRANS","CREDIT_TRANS");
            actlst=ClientUtil.executeQuery("getNotClosedDeposits",hash);
            if(actlst!=null && actlst.size()>0){
                notClosedMap =(HashMap)actlst.get(0);
            }
        }
        
        if( observable.getProdType().equals("TL") || observable.getProdType().equals("AD"))
            actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
        
//        if( observable.getProdType().equals("OA"))
            observable.setAccountName(ACCOUNTNO);
            //lblHouseName.setText(observable.getLblHouseName());
            if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AD")){
                if(debit || credit){
                    if(observable.getProdType().equals("TL") || observable.getProdType().equals("AD")){
                        if(actlst!=null && actlst.size()>0){
                            viewType = ACCNO;
                            updateOBFields();
                            hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                            hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                            //                        if( observable.getProdType().equals("TL")) {
                            if(debit) {
                                hash.put("PAYMENT","PAYMENT");
                                //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                            }else if(credit){
                                if(observable.getProdType().equals("TL"))
                                    hash.put("RECEIPT","RECEIPT");
                                System.out.println("hash"+hash);
                                //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                            }
                            fillData(hash);
                            //                        }
                        }else{
                            ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                            txtAccNo.setText("");
                            //                        txtAccNo.requestFocus();
                        }
                    }else if(observable.getProdType().equals("TD")){
                        viewType = ACCNO;
                        updateOBFields();
                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                        hash.put("SELECTED_BRANCH",TrueTransactMain.selBranch);
                        if(actlst!=null && actlst.size()>0){
                            if(observable.getProdType().equals("TD")){
                                hash.put("RECEIPT","RECEIPT");
                                if(debit) {
                                    rdoTransactionType_Debit.setSelected(true);
                                    //                                if(observable.getProdType().equals("TD")){
                                    //                                    hash.put("PAYMENT","PAYMENT");
                                    //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
                                    //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                    //                                }else{
                                    //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
                                    transDetails.setIsDebitSelect(true);
                                }else if(credit){
                                    rdoTransactionType_Credit.setSelected(true);
                                    //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                    //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                }
                                hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
                                hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
                                hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
                                fillData(hash);
                                if(debit) {
                                    rdoTransactionType_Debit.setSelected(true);
                                }else if(credit) {
                                    rdoTransactionType_Credit.setSelected(true);
                                }
                            }
                        }else if(actlst.isEmpty() && credit == true){
                            ClientUtil.showAlertWindow(" Already Transaction Completed...");
                            rdoTransactionType_Credit.setSelected(true);
                            txtAccNo.setText("");
                            txtAmount.setText("");
                            return;
                        }else{
                            ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                            txtAccNo.setText("");
                        }
                    }
                }else{
                    ClientUtil.showMessageWindow("Select Payment or Receipt ");
                    txtAccNo.setText("");
                    return;
                }
            }else if(observable.getProdType().equals("OA")){
                viewType = ACCNO;
                HashMap listMap = new HashMap();
                if(observable.getLblAccName().length()>0){
                    updateOBFields();
                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                    hash.put("SELECTED_BRANCH",TrueTransactMain.selBranch);
                    //                lst=ClientUtil.executeQuery("Cash.getAccountList"
                    //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                    fillData(hash);
                    observable.setLblAccName("");
                }else{
                    ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                    txtAccNo.setText("");
                }
            }
    }
    }
    }    
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
//    private void instrumentTypeFocus() {
//        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
//        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboInstrumentType.getSelectedIndex() > 0) {
//            
//            if (instrumentType.equals("VOUCHER") || instrumentType.equals("WITHDRAW_SLIP")) {
//                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurrentDate()));
//                tdtInstrumentDate.setEnabled(false);
//            } else {
//                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurrentDate()));
//                tdtInstrumentDate.setEnabled(true);
//            }
//        }
//        String chkbook="";
//        int count=0;
//        HashMap hmap=new HashMap();
//        if(chqBalList!=null && chqBalList.size()>0){
//            hmap=(HashMap)chqBalList.get(0);
//            chkbook=CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
//            count=CommonUtil.convertObjToInt(hmap.get("CNT"));
//            hmap=null;
//        }
//        if((chkbook.equals("Y"))){
//            if(!instrumentType.equals("CHEQUE")){
//                int yesNo = 0;
//                String[] options = {"Yes", "No"};
//                yesNo = COptionPane.showOptionDialog(null,"Cheque book is issued to the customer. Do you want to continue?", CommonConstants.WARNINGTITLE,
//                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                null, options, options[0]);
//                System.out.println("#$#$$ yesNo : "+yesNo);
//                if (yesNo!=0) {
//                    cboInstrumentType.setSelectedItem(null);
//                    
//                }
//            }
//        }
//    }
                
//        private void tokenChecking() {
//            // Validate Token in New Entry
//            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//             //   String tokenNo = CommonUtil.convertObjToStr(txtTokenNo.getText());
//                if (!tokenNo.equals("")) {
//                    HashMap tokenWhereMap = new HashMap();
//                    
//                    // Separating Serias No and Token No
//                    char[] chrs = tokenNo.toCharArray();
//                    StringBuffer seriesNo = new StringBuffer();
//                    int i=0;
//                    for (int j= chrs.length; i < j; i++ ) {
//                        if (Character.isDigit(chrs[i]))
//                            break;
//                        else
//                            seriesNo.append(chrs[i]);
//                    }
//                    
//                    tokenWhereMap.put("SERIES_NO", seriesNo.toString());
//                    tokenWhereMap.put("TOKEN_NO", tokenNo.substring(i));
//                    tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
//                    tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//                    tokenWhereMap.put("CURRENT_DT", observable.getCurrentDate());
//                    
//                    List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
//                    
//                    if (((Integer) lst.get(0)).intValue() == 0) {
//                 //       txtTokenNo.setText("");
//                        COptionPane.showMessageDialog(this, resourceBundle.getString("tokenMsg"));
//                    }
//                }
//            } // Token validation in New
//        }
                                
        private void denomination() {
            // Add your handling code here:
            HashMap map = new HashMap();
            if (observable.getDenominationList() != null) {
                map.put("DENOMINATION_LIST", observable.getDenominationList());
            }
            if (rdoTransactionType_Credit.isSelected()) {
                map.put("TRANS_TYPE", "Deposit");
                map.put("CURRENCY_TYPE", LocaleConstants.DEFAULT_CURRENCY); //(String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected());
                map.put("AMOUNT", txtAmount.getText());//txtInputAmt.getText());
            } else if (rdoTransactionType_Debit.isSelected()) {
                map.put("TRANS_TYPE", "Withdrawal");
                map.put("CURRENCY_TYPE", LocaleConstants.DEFAULT_CURRENCY); //(String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected());
                map.put("AMOUNT", txtAmount.getText());//txtInputAmt.getText());
                //map.put("CURRENCY_TYPE", ClientUtil.getMainCurrency());
                //map.put("AMOUNT", txtAmount.getText());
            }
            
            DenominationUI dui = new DenominationUI(map);
            ClientUtil.showDialog(dui);
            Double fltDenomination[] = dui.getFltDenomination();
            com.see.truetransact.uicomponent.CTextField txtCount[] = dui.getTxtCount();
            String denominationType[] = dui.getDenominationType();
            
            String strCnt = "";
            HashMap denominationMap = null;
            ArrayList denominationList = new ArrayList();
            for (int i=0; i < fltDenomination.length; i++) {
                strCnt = txtCount[i].getText().trim();
                if (strCnt != null && !strCnt.equals("0") && !strCnt.equals("")) {
                    denominationMap = new HashMap();
                    denominationMap.put("CURRENCY", LocaleConstants.DEFAULT_CURRENCY);
                    denominationMap.put("COUNT", new Double(txtCount[i].getText()));
                    denominationMap.put("DENOMINATION", fltDenomination[i]);
                    denominationMap.put("DENOMINATION_TYPE", denominationType[i]);
                    denominationList.add(denominationMap);
                }
            }
            System.out.println("denominationList:" + denominationList);
            observable.setDenominationList(denominationList);
            
        }
        
        private void clearProdFields() {
            //txtAccHdId.setText("");
            txtAccNo.setText("");
           // lblAccHdDesc.setText("");
           // lblAccName.setText("");
           // lblHouseName.setText("");
        }
            
    private void setProdEnable(boolean isEnable) {
        cboProdId.setEnabled(isEnable);
        txtAccNo.setEnabled(isEnable);
        btnAccNo.setEnabled(isEnable);
        
      //  btnAccHdId.setEnabled(!isEnable);
        
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
        //        btnPhoto.setEnabled(isEnable);
    }
            
	private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
            // Add your handling code here:
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
        
	private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
            // Add your handling code here:
            if(termDepositUI !=null){
                if(!termDepositUI.getRenewalTransMap().equals("")){
                    if(txtAccNo.getText().length()>0)
                        termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH","DEPOSIT_CASH_REJECTED");
                    else
                        termDepositUI.getRenewalTransMap().put("INTEREST_AMT_CASH","INTEREST_CASH_REJECTED");
                }
            }
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
            public boolean depositAuthorizationValidation(){
        boolean auth = false;
        if (this.observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE &&
        rdoTransactionType_Credit.isSelected() == true && observable.getProdType()!=null && observable.getProdType().equals("TD")) {
            HashMap depAuthMap = new HashMap();
            String AcNo = "";
            if (observable.getTxtAccNo().lastIndexOf("_")!=-1){
                AcNo = observable.getTxtAccNo().substring(0,observable.getTxtAccNo().lastIndexOf("_"));
            }
            depAuthMap.put("DEPOSIT_NO",AcNo);
            List lst = ClientUtil.executeQuery("getSelectRemainingBalance", depAuthMap);
            if(lst != null && lst.size()>0){
                depAuthMap = (HashMap)lst.get(0);
                double depositAmt = CommonUtil.convertObjToDouble(depAuthMap.get("DEPOSIT_AMT")).doubleValue();
                double totalBalance = CommonUtil.convertObjToDouble(depAuthMap.get("TOTAL_BALANCE")).doubleValue();
                double remainingAmt = depositAmt - totalBalance;
                if(remainingAmt<CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue()){
                    if(remainingAmt>0){
                        ClientUtil.showAlertWindow("Can not authorize, amount exceeding the deposit amount"+
                        "\n Deposit Amount is :"+depositAmt+
                        "\n Balance Amount to be collected :"+remainingAmt);
                    }else{
                        ClientUtil.showAlertWindow("Can not authorize, amount exceeding the deposit amount"+
                        "\n Deposit Amount is :"+depositAmt+
                        "Please Reject the Transaction ");
                    }
                    auth = true;
                }
            }
        }
        return auth;
    }        
        
        
        private void populateInstrumentType() {
       /*     ComboBoxModel objModel = new ComboBoxModel();
            objModel.addKeyAndElement("", "");
          //  String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
            if (!prodType.equals("")) {
                if (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AAD") ||prodType.equals("TD")) {
                    objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
                    objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
                    objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
                } else if (prodType.equals("TL") || prodType.equals("GL") || prodType.equals("ATL")) {
                    objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
                }
               // cboInstrumentType.setModel(objModel);
                System.out.println("#$#$#$^% Instrument type : "+observable.getCboInstrumentType());
               // cboInstrumentType.setSelectedItem(
               // CommonUtil.convertObjToStr(observable.getCboInstrumentType()));
            }*/
        }
        
        public void authorize(HashMap map) {
            
            if (map.get(CommonConstants.AUTHORIZEDATA) != null) {

//                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);commented by bala 10-aug-2010
                 if(observable.isHoAccount()){
                    ArrayList alist=setOrgOrRespDetails();
                    if(alist!=null && alist.size()>0){
                    observable.setOrgRespList(alist);
                    }
                }
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                map.put("CORP_LOAN_MAP", transDetails.getCorpDetailMap());  // For Corporate Loan purpose added by Rajesh
                observable.setAuthorizeMap(map);
                observable.doAction();
                if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                    super.setOpenForEditBy(observable.getStatusBy());
                 //   super.removeEditLock(lblTransactionIDDesc.getText());
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
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        if(termDepositUI !=null){
            if(!termDepositUI.getRenewalTransMap().equals("")){
                if(txtAccNo.getText().length()>0)
                    termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH","DEPOSIT_CASH_AUTHORIZED");
                else
                    termDepositUI.getRenewalTransMap().put("INTEREST_AMT_CASH","INTEREST_CASH_AUTHORIZED");
            }
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        //        observable.setStatus();
        if ((viewType == AUTHORIZE && isFilled)|| viewType == LINK_BATCH || viewType==LINK_BATCH_TD) {
            //            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            HashMap transMap = new HashMap();
//            transMap.put("TRANS_ID", lblTransactionIDDesc.getText());
//            List lst = ClientUtil.executeQuery("getTranAuthStatus", transMap);
//            if(lst!=null && lst.size()>0){
//                transMap = (HashMap)lst.get(0);
//                if(transMap.get("AUTHORIZE_STATUS")!=null)
//                    if(transMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")||transMap.get("AUTHORIZE_STATUS").equals("REJECTED")){
//                    ClientUtil.showMessageWindow("This Transaction has been already "+transMap.get("AUTHORIZE_STATUS")
//                    +" "+transMap.get("AUTHORIZE_BY"));        
//                    btnCancelActionPerformed(null);
//                    return;
//                }
//            }
            
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                int n=ClientUtil.confirmationAlert("Are you sure want to Reject", 1);
                if(n!=0){
                    return;
                }
            }
            boolean auth = depositAuthorizationValidation();
            if(auth == true){
                btnCancelActionPerformed(null);
            }
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            String remarks = "";
            if(flag != true){
                if(authorizeStatus.equals("REJECTED")){
                remarks = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_TITLE_REJE"));
                }else if(authorizeStatus.equals("AUTHORIZED")){
                    remarks = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_TITLE_AUTH"));
                }else if(authorizeStatus.equals("EXCEPTION")){
                    remarks = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_TITLE_EXCE"));
                }
            }
            
            authDataMap.put("ACCOUNT NO", txtAccNo.getText());
            
            if (cboProdId.isEnabled())
                authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()));
            else
                authDataMap.put("PRODUCT ID", "");
            //            if(observable.getLinkBathList()!=null && observable.getLinkBathList().size()>0){
            //                for(int i=0;i<observable.getLinkBathList().size();i++){
            //                    HashMap authTransMap=(HashMap)observable.getLinkBathList().get(i);
            //                    authDataMap.put("TRANS_ID", authTransMap.get("TRANS_ID"));
            //                    authDataMap.put("REMARKS", remarks);
            //                    arrList.add(authDataMap);
            //                    authDataMap=new HashMap();
            //                }
            //            }else {
           // authDataMap.put("TRANS_ID", lblTransactionIDDesc.getText());
            authDataMap.put("USER_ID",ProxyParameters.USER_ID);
            authDataMap.put("TRANS_DT", currDt.clone());
            authDataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            List lst=ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
            if(lst !=null && lst.size()>0) {
                HashMap map=new HashMap();
                StringBuffer open=new StringBuffer();
                for(int i=0;i<lst.size();i++){
                    map=(HashMap)lst.get(i);
                    open.append ("\n"+"User Id  :"+" ");
                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
                    open.append("Mode Of Operation  :" +" ");
                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");                
                }
                ClientUtil.showMessageWindow("already open by"+open);           
                return;
            }
            authDataMap.put("REMARKS", remarks);
            arrList.add(authDataMap);
            //            }
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if(observable.getLinkMap() !=null && observable.getLinkMap() .containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap() .get("AS_CUSTOMER_COMES").equals("Y"))
                singleAuthorizeMap.put("DAILY","DAILY");
            
            authorize(singleAuthorizeMap);
            
            //             if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED)
            //            lblStatus.setText(authorizeStatus);
            //             else
            
        } else {
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCashTransactionAuthorizeTOList");
            
            HashMap whereParam = new HashMap();
            whereParam.put("USER_ID", ProxyParameters.USER_ID);
            whereParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereParam.put("DB_DRIVER_NAME", ProxyParameters.dbDriverName);
            mapParam.put(CommonConstants.MAP_WHERE, whereParam);
            System.out.println("mapparam###"+mapParam);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeWFUI authorizeUI = new AuthorizeWFUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            //            lblStatus.setText(observable.getLblStatus());
            observable.setStatus();
            
        }
    }
    
    private void setRadioButtons() {
        this.removeRadioButtons();
        this.rdoTransactionType_Credit.setEnabled(true);
        this.rdoTransactionType_Debit.setEnabled(true);
        this.rdoTransactionType_Debit.setSelected(false);
        this.rdoTransactionType_Credit.setSelected(false);
        this.rdoTransactionType_DebitActionPerformed(null);
        
        if(observable.getCr_cash().equalsIgnoreCase("Y") &&
        observable.getDr_cash().equalsIgnoreCase("Y")) {
            this.addRadioButtons();
            return;
        }
        else if(observable.getCr_cash().equalsIgnoreCase("Y")){
            this.rdoTransactionType_Credit.setSelected(true);
            this.rdoTransactionType_CreditActionPerformed(null);
            this.rdoTransactionType_Debit.setEnabled(false);
        }
        else if(observable.getDr_cash().equalsIgnoreCase("Y")) {
            this.rdoTransactionType_Debit.setSelected(true);
            this.rdoTransactionType_DebitActionPerformed(null);
            this.rdoTransactionType_Credit.setEnabled(false);
        }
        
        this.addRadioButtons();
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
            this.rdoTransactionType_Debit.setEnabled(true);
            this.rdoTransactionType_Credit.setSelected(false);
            this.txtAmount.setEnabled(true);
//            this.txtAmount.setEditable(true);
        } else {
            //            this.txtAmount.setEnabled(false);
//            this.txtAmount.setEditable(true);
        }
        
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
    }
            
	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
            // Add your handling code here:
            this.dispose();
            //cifClosingAlert();
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
            ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panData, false);
//            deletescreenLock();
//            asAndWhenMap=null;
//         //   transDetails.setTransDetails(null,null,null);
//            //transDetails.setSourceFrame(null);
//            //		super.removeEditLock(lblTransactionIDDesc.getText());
//            setEditFieldsEnable(true);
//            observable.resetLable();                // Reset the Editable Lables in the UI to null...
//            setSelectedBranchID(ProxyParameters.BRANCH_ID);
//            observable.resetForm();                 // Reset the fields in the UI to null...
//            ClientUtil.enableDisable(this, false);  // Disables the panel...
//            enableDisableButtons(false);
//            setModified(false);
//            if(termDepositUI !=null){
//                btnCloseActionPerformed(null);
//                if(afterSaveCancel == false)
//                    termDepositUI.disableSaveButton();
//            }
//            //                        disableButtons();                       // To Disable the folder buttons in the UI...
//            setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
//            if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE)
//                observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
//            observable.setCbmProdId("");
//            cboProdId.setModel(observable.getCbmProdId());
//            observable.setStatus();                 // To set the Value of lblStatus...
//            viewType = -1;
//            btnDelete.setEnabled(false);
//            if(transCommonUI != null)
//                transCommonUI.dispose();
//            transCommonUI = null;
//             custStatus="";
////             btnViewTermLoanDetails.setVisible(false);
////             btnViewTermLoanDetails.setEnabled(false);
//             termLoansDetailsMap = null;
//             termLoanDetails = null;
//             termLoanDetailsFlag = false;
//             
//             if (fromAuthorizeUI) {
//                this.dispose();
//                fromAuthorizeUI = false;
//            }
//           //  lblHouseName.setText("");
//             btnViewTermLoanDetails.setEnabled(false);
//             btnOrgOrResp.setVisible(false);
//             setOrgBranch("");
//             setOrgBranchName("");
//             setOrgOrRespAdviceDt(null);
//             setOrgOrRespAdviceNo("");
//             setOrgOrRespAmout(0.0);
//             setOrgOrRespBranchId("");
//             setOrgOrRespBranchName("");
//             setOrgOrRespCategory("");
//             setOrgOrRespDetails("");
//             setOrgOrRespTransType("");
//             
////            ArrayList key = new ArrayList();
////            ArrayList value = new ArrayList();
////            key.add("");
////            value.add("");
////            observable.setCbmProdType(new ComboBoxModel(key,value));            
//            
	}//GEN-LAST:event_btnCancelActionPerformed
        
	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
            // Add your handling code here:
            String Acno=txtAccNo.getText();
            HashMap hashmap=new HashMap();
            hashmap.put("ACNO",Acno);
            List lst1=ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", hashmap);
            if(lst1!=null && lst1.size()>0){
                int a=ClientUtil.confirmationAlert("The Account is Death marked, Do you want to continue?");
                int b=0;
                if(a!=b)
                    return;
            }
            String prodTypes = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            String prodId ="";
            observable.setTxtAmount(txtAmount.getText());
            if(!prodTypes.equals("GL"))
                prodId = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            boolean save = true;
            if(prodTypes.equals("TD")){
                save = observable.calcRecurringDates(prodId);
            }
            if(prodTypes.equals("TD")){
                HashMap recurrMap = new HashMap();
                recurrMap.put("PROD_ID",prodId);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
                if(lst!=null && lst.size()>0){
                    HashMap recurringMap = (HashMap)lst.get(0);
                    if(!recurringMap.get("BEHAVES_LIKE").equals("RECURRING") && rdoTransactionType_Credit.isSelected()){
                        HashMap validationMap = new HashMap();
                        validationMap.put("DEPOSIT_NO",txtAccNo.getText());
                        List lstDep = ClientUtil.executeQuery("getDepositAmountForTrans", validationMap);
                        if(lstDep !=null && lstDep.size()>0){
                            validationMap =(HashMap)lstDep.get(0);
                            double totBal = CommonUtil.convertObjToDouble(validationMap.get("TOTAL_BALANCE")).doubleValue();
                            double depAmt = CommonUtil.convertObjToDouble(validationMap.get("DEPOSIT_AMT")).doubleValue();
                            if(totBal == 0){
                                
                            }else if(depAmt<=totBal){
                                ClientUtil.displayAlert("Transaction has been already done...");
                                btnCancelActionPerformed(null);
                                return;
                            }
                        }
                    }
                    //                    if(recurringMap.get("BEHAVES_LIKE").equals("RECURRING") && rdoTransactionType_Credit.isSelected()){
                    //                        HashMap validationMap = new HashMap();
                    //                        validationMap.put("DEPOSIT_NO",txtAccNo.getText());
                    //                        List lstDep = ClientUtil.executeQuery("getDepositAmountForTrans", validationMap);
                    //                        if(lstDep !=null && lstDep.size()>0){
                    //                            validationMap =(HashMap)lstDep.get(0);
                    //                            double depAmt = CommonUtil.convertObjToDouble(validationMap.get("DEPOSIT_AMT")).doubleValue();
                    //                            double penalAmt =  CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                    //                            double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    //                            double finalAmount = 0.0;
                    //                            if(depAmt>penalAmt){
                    //                                finalAmount = amount % depAmt;
                    //                                if(finalAmount == penalAmt ){
                    //                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                    //                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                    //                                }
                    //                            }else{
                    //                                amount = amount - penalAmt;
                    //                                finalAmount = amount % depAmt;
                    //                                if(finalAmount == 0 && penalAmt>0){
                    //                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                    //                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                    //                                }
                    //                            }
                    //                        }
                    //                    }
                }
            }
            if(prodTypes.equals("TD")){
                if(flag == true){
                    String dep = termDepositUI.getTransSomeAmt();
                    if(dep != null && dep.equals("DEP_INTEREST_AMT")){
                        observable.setDepInterestAmt("DEP_INTEREST_AMT");
                    }
                }
            }
            
            if(!prodTypes.equals("GL") && txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID)) && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.displayAlert("Please Enter Account Number...!!!");
                return;
            }
            
            if (save) {
                if(prodTypes.equals("TL")|| prodTypes.equals("AD")) {
                    checkDocumentDetail();
                    if(moreThanLoanAmountAlert())
                        return;
                    //                    if(prodTypes.equals("AD") && rdoTransactionType_Debit.isSelected()){
                    //                         boolean expirydate=checkExpiryDate();
                    //                         if(expirydate)
                    //                             return; //NOW NOT  NEEDED
                    //                    }
                }
                updateOBFields();
                StringBuffer mandatoryMessage = new StringBuffer();
                mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCashTransaction));
                
                System.out.println("observable.getProdType(" + observable.getProdType());
                if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("GL") &&
                CommonUtil.convertObjToStr(observable.getCboProdId()).length()== 0) {
                    mandatoryMessage.append(objMandatoryRB.getString("cboProdId") + "\n") ;
                }
                
//                if (CommonUtil.convertObjToStr(observable.getProdType()).equals("GL") &&//commented by abi
//                CommonUtil.convertObjToStr(observable.getTxtAccHd()).length()== 0) {
//                    mandatoryMessage.append(objMandatoryRB.getString("txtAccHdId") + "\n") ;
//                }
                
                if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("GL") &&
                CommonUtil.convertObjToStr(observable.getTxtAccNo()).length() == 0) {
                    mandatoryMessage.append(objMandatoryRB.getString("txtAccNo") + "\n") ;
                }
                
                if (observable.getRdoTransactionType_Debit() == false && observable.getRdoTransactionType_Credit() == false) {
                    mandatoryMessage.append(objMandatoryRB.getString("rdoTransactionType_Debit") + "\n") ;
                }
                //Commented By Suresh
//                if (CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() <= 0){
//                    mandatoryMessage.append(objMandatoryRB.getString("txtAmount") + "\n") ;
//                }
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                    if(rdoTransactionType_Debit.isSelected()) {
//                        if(txtTokenNo.getText().equals("") && !(designation.equals("Teller")))
//                            mandatoryMessage.append(objMandatoryRB.getString("txtTokenNo") + "\n") ;
                        double avbal=0.0;
                        double shadowDeb=0.0;
                        double enteredAmt= CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        ArrayList tableVal=transDetails.getTblDataArrayList();
                        System.out.println("tableVal"+tableVal);
                        for(int k=0;k<tableVal.size();k++){
                            ArrayList balList=((ArrayList)tableVal.get(k));
                            if(balList.get(0).equals("Available Balance")){
                                String str= CommonUtil.convertObjToStr(balList.get(1));
                                str = str.replaceAll(",","");
                                avbal=CommonUtil.convertObjToDouble(str).doubleValue();
                            }
                            if(balList.get(0).equals("Shadow Debit")){
                                String shawdowStr= CommonUtil.convertObjToStr(balList.get(1));
                                shawdowStr = shawdowStr.replaceAll(",","");
                                shadowDeb=CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
                            }
                        }
                        HashMap hmap=new HashMap();
                        
                        
                        if(chqBalList!=null && chqBalList.size()>0){
                            hmap=(HashMap)chqBalList.get(0);
                            double minbalWChk=CommonUtil.convertObjToDouble(hmap.get("MIN_WITH_CHQ")).doubleValue() ;
                            double  minimumbalanceWtChk=CommonUtil.convertObjToDouble(hmap.get("MIN_WITHOUT__CHQ")).doubleValue() ;
                            String chkbook=CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
                            int count= CommonUtil.convertObjToInt(hmap.get("CNT"));
                            if((chkbook.equals("Y"))){
                                if(enteredAmt==(avbal-shadowDeb)){
                                    int a=ClientUtil.confirmationAlert("A/c balance will become Zero, Do you want to continue?");
                                    int b=0;
                                    if(a!=b)
                                        return;
                                }
                                else if(((avbal-shadowDeb)-enteredAmt)< minbalWChk){
                                    int m=ClientUtil.confirmationAlert("Minimum Balance not maintained, Do you want to continue?");
                                    int n=0;
                                    if(m!=n)
                                        return;
                                }
                            }else {
                                if(enteredAmt==(avbal-shadowDeb)){
                                    int a=ClientUtil.confirmationAlert("A/c balance will become Zero, Do you want to continue?");
                                    int b=0;
                                    if(a!=b)
                                        return;
                                }
                                else if(((avbal-shadowDeb)-enteredAmt)< minimumbalanceWtChk){
                                    int m=ClientUtil.confirmationAlert("Minimum Balance not maintained, Do you want to continue?");
                                    int n=0;
                                    if(m!=n)
                                        return;
                                }
                            }
                        }
                           String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
                           if (!prodType.equals("GL")){
                               System.out.println("prodtypeeeeee"+prodType);
                               if(!prodType.equals("SA")){
                        if(enteredAmt>(avbal-shadowDeb)){
                            //                            if(avbal==0.0){
                            ClientUtil.displayAlert("Entered Amount Exceeds The Available Balance");
                                return;
                        }
                               }
                           }
                        if(rdoTransactionType_Debit.isSelected() && observable.getBalanceType().equals("DEBIT")){
                         //   String  acchead=txtAccHdId.getText();
                            double limit=0.0;
                            double annualLimit=0.0;
                            double percentage=0.0;
                            double enteredAmount=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            HashMap hashmap1=new HashMap();
                          //  hashmap1.put("ACHEAD",acchead);
                            hashmap1.put("CUR_DATE",currDt.clone());
                            List lmtList=ClientUtil.executeQuery("getLimitAmt",hashmap1);
                            if(lmtList!=null && lmtList.size()>0){
                                hashmap1=(HashMap)lmtList.get(0);
                                limit=CommonUtil.convertObjToDouble(hashmap1.get("LIMIT_AMT")).doubleValue();
                                annualLimit=CommonUtil.convertObjToDouble(hashmap1.get("ANNUAL_LIMIT_AMT")).doubleValue();
                                percentage=CommonUtil.convertObjToDouble(hashmap1.get("OVER_DRAW_PER")).doubleValue();
                                annualLimit=annualLimit+(annualLimit*percentage/100);
                            if(enteredAmount+avbal>annualLimit){
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                                int d= 0;
                                if(c!=d)
                                    return;
                            } if(enteredAmt>limit){
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                                int d= 0;
                                if(c!=d)
                                    return;
                            }
                        }
                            
                        }
                    }
                    if(rdoTransactionType_Credit.isSelected() && observable.getBalanceType().equals("CREDIT")){
                        //String  acchead=txtAccHdId.getText();
                        double limit=0.0;
                        double annualLimit=0.0;
                        double avbal=0.0;
                        double shadowDeb=0.0;
                        double percentage=0.0;
                        double enteredAmount=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        HashMap hashmap1=new HashMap();
                      //  hashmap1.put("ACHEAD",acchead);
                        hashmap1.put("CUR_DATE",currDt.clone());
                        ArrayList tableVal=transDetails.getTblDataArrayList();
                        System.out.println("tableVal"+tableVal);
                        for(int k=0;k<tableVal.size();k++){
                            ArrayList balList=((ArrayList)tableVal.get(k));
                            if(balList.get(0).equals("Available Balance")){
                                String str= CommonUtil.convertObjToStr(balList.get(1));
                                str = str.replaceAll(",","");
                                avbal=CommonUtil.convertObjToDouble(str).doubleValue();
                            }
                            if(balList.get(0).equals("Shadow Debit")){
                                String shawdowStr= CommonUtil.convertObjToStr(balList.get(1));
                                shawdowStr = shawdowStr.replaceAll(",","");
                                shadowDeb=CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
                            }
                        }
                            List lmtList=ClientUtil.executeQuery("getLimitAmt",hashmap1);
                            if(lmtList!=null && lmtList.size()>0){
                                hashmap1=(HashMap)lmtList.get(0);
                                limit=CommonUtil.convertObjToDouble(hashmap1.get("LIMIT_AMT")).doubleValue();
                                annualLimit=CommonUtil.convertObjToDouble(hashmap1.get("ANNUAL_LIMIT_AMT")).doubleValue();
                                percentage=CommonUtil.convertObjToDouble(hashmap1.get("OVER_DRAW_PER")).doubleValue();
                                annualLimit=annualLimit+(annualLimit*percentage/100);
                            if(enteredAmount+avbal>annualLimit){
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                                int d= 0;
                                if(c!=d)
                                    return;
                            } if(enteredAmount>limit){
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                                int d= 0;
                                if(c!=d)
                                    return;
                            }
                            }
                        
                    }
                    
                }
//                if (!prodTypes.equals("GL")){
//                    if(rdoTransactionType_Credit.isSelected() && CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() >=TrueTransactMain.PANAMT
//                    && txtPanNo.getText().equals("")){
//                        //                        mandatoryMessage.append(objMandatoryRB.getString("txtPanNo") + "\n") ;
//                        int a = ClientUtil.confirmationAlert("PAN Number Not Entered.. Do You Want To Continue Without Entering?");
//                        int b= 0;
//                        if(b!=a){
//                            return;
//                        }
//                    }
//                }
                if(observable.isHoAccount()){
                    ArrayList alist=setOrgOrRespDetails();
                   
                    observable.setOrgRespList(alist);
                     if(rdoTransactionType_Credit.isSelected()){
                         if(getOrgOrRespBranchId().equals("") || getOrgOrRespDetails().equals("")){
                             ClientUtil.displayAlert("Enter Orginating Details");
                             return;
                         }
                     }
                    else if(rdoTransactionType_Debit.isSelected()){
                        if(getOrgBranch().equals("") || getOrgOrRespAdviceNo().equals("") || getOrgOrRespAdviceDt().equals("")){
                             ClientUtil.displayAlert("Enter Responding Details");
                             return;
                         }
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
                            return;
                        }
                    }
                }
                if (prodTypes.equals("OA") && rdoTransactionType_Debit.isSelected() == true){
                    double transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    observable.setCreatingFlexi("");
                    observable.setFlexiAmount(0.0);
                    HashMap flexiMap = new HashMap();
                    flexiMap.put("ACCOUNTNO",txtAccNo.getText());
                    List getList = ClientUtil.executeQuery("getFlexiDetails", flexiMap);
                    if(getList !=null && getList.size()>0){
                        System.out.println("getList : "+getList);
                        flexiMap = (HashMap)getList.get(0);
                        double lienAmt = 0.0;
                        double alreadyTransAmt = 0.0;
                        double depAvaialAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_BALANCE")).doubleValue();
                        double depositAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_AMT")).doubleValue();
                        double availBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                        double totalBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                        double minBal2 = CommonUtil.convertObjToDouble(flexiMap.get("MIN_BAL2_FLEXI")).doubleValue();
                        alreadyTransAmt = CommonUtil.convertObjToDouble(flexiMap.get("SHADOW_DEBIT")).doubleValue();
                        HashMap depositProdMap = new HashMap();
                        double amtMultiples = 0.0;
                        depositProdMap.put("PROD_ID",flexiMap.get("PROD_ID"));
                        List lstProd = ClientUtil.executeQuery("getSchemeIntroDate", depositProdMap);
                        if(lstProd!=null && lstProd.size()>0){
                            depositProdMap = (HashMap)lstProd.get(0);
                            amtMultiples = CommonUtil.convertObjToDouble(depositProdMap.get("WITHDRAWAL_MULTI")).doubleValue();
                        }
                        HashMap lienMap = new HashMap();
                        lienMap.put("LIEN_AC_NO",txtAccNo.getText());
                        lienMap.put("LIEN_DT", observable.getCurrentDate());
                        List lst =ClientUtil.executeQuery("getDetailsForSBLienActDetails", lienMap);
                        if(lst!=null && lst.size()>0){
                            lienMap = (HashMap)lst.get(0);
                            HashMap sumOfDepMap = new HashMap();
                            sumOfDepMap.put("FLEXI_ACT_NUM",txtAccNo.getText());
                            List lstSum = ClientUtil.executeQuery("getSelectSumOfDepAmount", sumOfDepMap);
                            if(lstSum!=null && lstSum.size()>0){
                                sumOfDepMap = (HashMap)lstSum.get(0);
                                depositAmt = CommonUtil.convertObjToDouble(sumOfDepMap.get("TOTAL_BALANCE")).doubleValue();
                            }
                            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                                lienAmt = CommonUtil.convertObjToDouble(lienMap.get("SUM(LIEN_AMOUNT)")).doubleValue();
                                if(lienAmt>0){
                                    if(availBal < transAmt){
                                        ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                        return;
                                    }else if(availBal == transAmt)
                                        lienAmt = depAvaialAmt;
                                    else
                                        lienAmt = transAmt;
                                }else{
                                    if(availBal < transAmt){
                                        ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                        return;
                                    }else{
                                        if(transAmt == availBal)
                                            lienAmt = depAvaialAmt;
                                        else if(alreadyTransAmt == 0 && minBal2 >= transAmt){
                                            if(minBal2>(availBal-transAmt)){
                                                if(depositAmt>transAmt)
                                                    lienAmt = transAmt;
                                                else
                                                    lienAmt = depositAmt;
                                            }else
                                                lienAmt = 0.0;
                                        }else if(alreadyTransAmt == 0 && minBal2 < transAmt){
                                            lienAmt = availBal - transAmt;
                                            lienAmt = depositAmt - lienAmt;
                                        }else if(alreadyTransAmt>0 && minBal2 >= (transAmt+alreadyTransAmt)){
                                            lienAmt = 0.0;
                                        }else if(alreadyTransAmt > 0 && minBal2 < (transAmt+alreadyTransAmt)){
                                            if(availBal == (transAmt+alreadyTransAmt))
                                                lienAmt = depositAmt;
                                            else{
                                                lienAmt = availBal - (transAmt+alreadyTransAmt);
                                                lienAmt = depositAmt - lienAmt;
                                            }
                                        }
                                    }
                                }
                                if(availBal > transAmt){
                                    double balancelienAmt = lienAmt % amtMultiples;
                                    if(balancelienAmt != 0)
                                        lienAmt = (lienAmt - balancelienAmt)+amtMultiples;
                                    if(depositAmt<lienAmt)
                                        lienAmt = depAvaialAmt;
                                }else
                                    lienAmt = depAvaialAmt;
                            }
                            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                                ClientUtil.showAlertWindow("Can not Edit this Transaction Please Reject or Delete, and do it fresh transaction...");
                                btnCancelActionPerformed(null);
                                return;
                            }
                            //                        if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT){
                            //                            HashMap cashMap = new HashMap();
                            //                            cashMap.put("TRANS_ID",lblTransactionIDValue.getText());
                            //                            System.out.println("cashMap "+cashMap);
                            //                            lst = ClientUtil.executeQuery("getSBLienTransferAccountNo",cashMap);
                            //                            if(lst !=null && lst.size()>0){
                            //                                cashMap = (HashMap)lst.get(0);
                            //                                double transactionAmt = CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")).doubleValue();
                            //                                String lienNo = CommonUtil.convertObjToStr(cashMap.get("AUTHORIZE_REMARKS"));
                            //                                cashMap.put("LIEN_AC_NO",txtAccountNo.getText());
                            //                                lst = ClientUtil.executeQuery("getDetailsForEditModeSBLienAct",cashMap);
                            //                                if(lst !=null && lst.size()>0){
                            //                                    for(int i = 0; i<lst.size(); i++){
                            //                                        cashMap = (HashMap)lst.get(i);
                            //                                        lienAmt += CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                            //                                    }
                            //                                }
                            //                                if(!lienNo.equals("")){
                            //                                    double balance = availBal - alreadyTransAmt;
                            //                                    if(transAmt>(transactionAmt+balance)){
                            //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                            //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                            //                                        return;
                            //                                    }else{
                            //                                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            //                                        transAmt = alreadyTransAmt - lienAmt;
                            //                                        if(alreadyTransAmt>minBal2){
                            //                                            if(txtAmt>transAmt)
                            //                                                lienAmt = txtAmt - transAmt;
                            //                                            else{
                            //                                                cashMap.put("LIEN_NO",lienNo);
                            //                                                lst = ClientUtil.executeQuery("getDetailsForEditModeSingleSBLienAct",cashMap);
                            //                                                if(lst !=null && lst.size()>0){
                            //                                                    cashMap = (HashMap)lst.get(0);
                            //                                                    lienAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                            //                                                }
                            //                                                if(transactionAmt>transAmt){
                            //                                                    if(txtAmt>minBal2)
                            //                                                        lienAmt = transactionAmt - (txtAmt + lienAmt);
                            //                                                    else
                            //                                                        lienAmt = 0.0;
                            //                                                }else
                            //                                                    lienAmt = txtAmt;
                            //                                            }
                            //
                            //                                        }else
                            //                                            lienAmt = 0.0;
                            //                                    }
                            //                                }else{//ok d'nt change anything...Sathiya.
                            //                                    double balance = availBal - alreadyTransAmt;
                            //                                    if(transAmt>balance){
                            //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                            //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                            //                                        return;
                            //                                    }else{
                            //                                        transAmt = transAmt - transactionAmt + alreadyTransAmt;
                            //                                        if((transAmt+transactionAmt) == availBal)
                            //                                            lienAmt = depositAmt;
                            //                                        else if(transAmt>minBal2){
                            //                                            lienAmt = availBal - transAmt + lienAmt;
                            //                                            lienAmt = depositAmt - lienAmt;
                            //                                        }else
                            //                                            lienAmt = 0.0;
                            //                                    }
                            //                                }
                            //                            }
                            //                        }
                        }
                        lst = null;
                        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||
                        observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                            if(lienAmt>0){
                                String[] obj ={"Ok","Cancel"};
                                int options =COptionPane.showOptionDialog(null,("Lien Marked for Rs. : "+lienAmt + " on Flexi Deposit"), ("Flexi Account"),
                                COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                                if(options == 0){
                                    observable.setCreatingFlexi("FLEXI_LIEN_CREATION");
                                    observable.setFlexiAmount(lienAmt);
                                }else
                                    return;
                            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                                observable.setCreatingFlexi("FLEXI_LIEN_DELETION");
                            }
                        }
                    }
                    getList = null;
                }
                //                if (prodTypes.equals("OA") && rdoTransactionType_Credit.isSelected() == true &&
                //                txtAccNo.getText().length()>0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                //                    HashMap lienReducingMap = new HashMap();
                //                    lienReducingMap.put("LIEN_AC_NO",txtAccNo.getText());
                //                    List lst = ClientUtil.executeQuery("getSelectReducingLienAmount", lienReducingMap);
                //                    if(lst!=null && lst.size()>0){
                //                        lienReducingMap = (HashMap)lst.get(0);
                //                        if(!lienReducingMap.get("AUTHORIZE_STATUS").equals("") && lienReducingMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                //                            double lienAmt = CommonUtil.convertObjToDouble(lienReducingMap.get("LIEN_AMOUNT")).doubleValue();
                //                            observable.setCreatingFlexi("FLEXI_LIEN_REDUCING");
                //                            observable.setFlexiAmount(lienAmt);
                //                        }else{
                //                            ClientUtil.showAlertWindow("Authorize Cash Transaction where Lien is Marked...");
                ////                            return;
                //                        }
                //                    }
                //                }
                if(!prodTypes.equals("") && prodTypes.equals("GL")){
                    HashMap acctMap = new HashMap();
                    acctMap.put("ACCT_HEAD",observable.getTxtAccHd());
                    List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                    if(head!=null && head.size()>0){
                        acctMap = (HashMap)head.get(0);
                        if(!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")){
                            if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true){
                                observable.setReconcile("N");
                                observable.reconcileMap = new HashMap();
                                double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                                if(transCommonUI == null){
                                    ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                                    return;
                                }else {
                                    double reconcileAmt = transCommonUI.getReconciledAmt();
                                    if(reconcileAmt == txtAmt){
                                        if(transCommonUI.getReturnMap().size()>0){
                                            observable.reconcileMap = transCommonUI.getReturnMap();
                                        }
                                    }else{
                                        ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                        return;
                                    }
                                }
                            }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true){
                                observable.setReconcile("N");
                                observable.reconcileMap = new HashMap();
                                double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                                if(transCommonUI == null){
                                    ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                                    return;
                                }else {
                                    double reconcileAmt = transCommonUI.getReconciledAmt();
                                    if(reconcileAmt == txtAmt){
                                        if(transCommonUI.getReturnMap().size()>0){
                                            observable.reconcileMap = transCommonUI.getReturnMap();
                                        }
                                    }else{
                                        ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                        return;
                                    }
                                }
                            }else if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true)
                                observable.setReconcile("Y");
                            else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true)
                                observable.setReconcile("Y");
                            observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                        }
                    }
                }
                if (observable.getRdoTransactionType_Debit() == true) {
                 //   String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
//                    System.out.println("observable.getCboInstrumentType():" + observable.getCboInstrumentType());
//                    if (instrumentType.length() == 0) {
//                        mandatoryMessage.append(objMandatoryRB.getString("cboInstrumentType") + "\n") ;
//                    } else if ((instrumentType.equals("DD") || instrumentType.equals("CHEQUE")) &&
//                    (CommonUtil.convertObjToStr(observable.getTxtInstrumentNo1()).length() == 0 ||
//                    CommonUtil.convertObjToStr(observable.getTxtInstrumentNo2()).length() == 0)) {
//                        mandatoryMessage.append(objMandatoryRB.getString("txtInstrumentNo1") + "\n") ;
//                    }
                    
//                    if (tdtInstrumentDate.getDateValue().equals("")) {
//                        mandatoryMessage.append(objMandatoryRB.getString("tdtInstrumentDate") + "\n") ;
//                    }
                    //                                                                                                                                                                                                                                    if (CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() <= 0){
                    //                                                                                                                                                                                                                                        mandatoryMessage.append(objMandatoryRB.getString("txtAmount") + "\n") ;
                    //                                                                                                                                                                                                                                    }
                }
                //final String mandatoryMessage = new MandatoryDBCheck().checkMandatory(getClass().getName(), panCashTransaction);
                if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                    displayAlert(mandatoryMessage.toString());
                }
                else{
                    if(flagDeposit == true){
                        double intAmt = CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue();
                        txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
                        observable.depositRenewalFlag = true;
                        System.out.println("transfer UI interestAmount : "+intAmt);
                    }
                    if(flag == true){
                        double intAmt = CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue();
                        txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                        observable.depositRenewalFlag = true;
                        System.out.println("transfer UI interestAmount : "+intAmt);
                    }
                    if(prodTypes.equals("TD") && rdoTransactionType_Debit.isSelected() && flagDeposit == true){
                        double totAmt = CommonUtil.convertObjToDouble(transDetails.getDepositAmt()).doubleValue();
                        double depAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if(depAmt > totAmt ){
                            ClientUtil.showAlertWindow("Amount is exceeding for that Available Balance ...");
                            txtAmount.setText(String.valueOf(0.0));
                            return;
                        }
                    }
                    
                    // For Corporate Loan purpose added by Rajesh
                    if (prodTypes.equals("TL") && transDetails.getCorpDetailMap()!=null && transDetails.getCorpDetailMap().size()>0) {
                        observable.setCorpLoanMap(transDetails.getCorpDetailMap());
                    }
                    
                    observable.doAction();              // To perform the necessary operation depending on the Action type...
                    if (prodTypes.equals("OA") && rdoTransactionType_Debit.isSelected() == true){
                        HashMap desigMap = new HashMap();
                        if(designation.equals("Teller")){
                            desigMap.put("DESIGNATION",designation);
                            List list= ClientUtil.executeQuery("getTellerAmount",desigMap);
                            HashMap TellerAmt = new HashMap();
                            TellerAmt=(HashMap)list.get(0);
                            double cashDeAmt= CommonUtil.convertObjToDouble(TellerAmt.get("CASH_DEBIT")).doubleValue();
                            double enteredAmt=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            if(enteredAmt<=cashDeAmt){
                                int a = ClientUtil.confirmationAlert("Continue With Authorization?");
                                int b= 0;
                                if(b==a){
                                    viewType=3;
                                    isFilled =true;
                                    if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                               //         lblTransactionIDDesc.setText(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("TRANS_ID")));
                                    authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                }
                            }
                        }
                    }
                    // For Varappetty Voucher & Receipt Printing Purpose added by Rajesh
                    String transType = "";
                    if (rdoTransactionType_Debit.isSelected() == true){
                        transType = "DEBIT";
                    } else {
                        transType = "CREDIT";
                    }
                    
                    if(observable.getResult() == ClientConstants.ACTIONTYPE_FAILED){
                        return;
                    }else{
                        observable.resetForm();
                    }
                    if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                        HashMap lockMap = new HashMap();
                        ArrayList lst = new ArrayList();
                        
                        lst.add("TRANS_ID");
                        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                        if (observable.getProxyReturnMap()!=null) {
                            if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                                lockMap.put("TRANS_ID", observable.getProxyReturnMap().get("TRANS_ID"));
                            }
                        }
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                       //     lockMap.put("TRANS_ID",lblTransactionIDDesc.getText());
                        }
                        if (lockMap.containsKey("TRANS_ID") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                            System.out.println("#$#$$ yesNo : "+yesNo);
                            if (yesNo==0) {
                                TTIntegration ttIntgration = null;
                                lockMap.put("TransId", lockMap.get("TRANS_ID"));
                                lockMap.put("TransDt", observable.getCurrentDate());
                                lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                ttIntgration.setParam(lockMap);
                                //                                if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                                //                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                //                                } else {
                                if (transType.equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment");
                                } else {
                                    ttIntgration.integrationForPrint("CashReceipt");
                                }
                                //                                }
                            }
                        }
                        setEditLockMap(lockMap);
                        setEditLock();
                        
                        observable.resetForm();             // Reset the fields in the UI to null...
                        observable.resetLable();            // Reset the Editable Lables in the UI to null...
                        transDetails.setTransDetails(null,null,null,null);
                        transDetails.setSourceFrame(null);
                        setEditFieldsEnable(true);
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
                            enableDisableButtons(false);        // To disable the buttons(folder) in the UI...
                        ClientUtil.enableDisable(this, false);// Disables the panel...
                        setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
                        observable.setResultStatus();// To Reset the Value of lblStatus...
                        enableDisableButtons(false);
                        setModified(false);
                        if(termDepositUI !=null){
                            afterSaveCancel = true;
                            btnCloseActionPerformed(null);
                            if(!termDepositUI.getRenewalTransMap().equals("")){
                                HashMap renewalMap = termDepositUI.getRenewalTransMap();
                                System.out.println("transfer UI renewalMap : "+renewalMap);
                                if(renewalMap.containsKey("INT_AMT")){
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.changePeriod();
                                }else if(renewalMap.containsKey("INTEREST_AMT_CASH")){
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.addingSomeAmt();
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH","");
                                }else
                                    if(renewalMap.containsKey("DEPOSIT_AMT_CASH")){
                                        termDepositUI.transactionCalling();
                                    }
                            }
                        }
                        //                                    this.disableButtons();
                    }
                    if (observable.getActionType()==ClientConstants.ACTIONTYPE_FAILED)
                        btnCancelActionPerformed(null);
                }
                viewType = -1;
                observable.setResultStatus();
            }
            else
                btnCancelActionPerformed(null);
            btnDelete.setEnabled(false);
            deletescreenLock();
            if(transCommonUI != null)
                transCommonUI.dispose();
            transCommonUI = null;
            
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
            map.put("TRANS_DT", currDt.clone());
            map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            ClientUtil.execute("DELETE_SCREEN_LOCK", map);
        }
        private void checkDocumentDetail() {
            String AccNo=null;
            if(txtAccNo.getText().lastIndexOf("_")!=-1)
                AccNo = txtAccNo.getText().substring(0,txtAccNo.getText().lastIndexOf("_"));
            else
                AccNo = txtAccNo.getText();
            List lst = observable.getDocumentDetails("getSelectTermLoanDocumentTO",AccNo);
            String str="";
            String doc_form_no=null;
            String is_submited=null;
            HashMap hash=new HashMap();
            for(int i=0;i<lst.size();i++){
                hash=(HashMap)lst.get(i);
                is_submited=(String)hash.get("IS_SUBMITTED")==null ?"N":(String)hash.get("IS_SUBMITTED");
                doc_form_no=(String)hash.get("DOC_DESC");
                if(is_submited!=null)
                    if(!is_submited.equals("Y")) {
                        str=str+doc_form_no+"\n";
                    }
            }
            if(str.length()>=1)
                ClientUtil.displayAlert(str+"notsubmited");
        }
        
        private void displayAlert(String message){
            CMandatoryDialog cmd = new CMandatoryDialog();
            cmd.setMessage(message);
            cmd.show();
        }
        
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        observable.resetForm();                 // Reset the fields in the UI to null...
        //        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        if (ClientUtil.deleteAlert()==0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
            btnSaveActionPerformed(evt);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
        }
        //        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
            // Add your handling code here:
            
            observable.resetForm();                 // Reset the fields in the UI to null...
            observable.resetLable();                // Reset the Editable Lables in the UI to null...
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
            popUp(EDIT);
            if (rdoTransactionType_Credit.isSelected()) {
//                txtTokenNo.setText("");
////                txtTokenNo.setEditable(false);
////                txtTokenNo.setEditable(false);
//                cboInstrumentType.setSelectedIndex(0);
//                txtInstrumentNo1.setText("");
//                txtInstrumentNo2.setText("");
//                tdtInstrumentDate.setDateValue("");
//                
//                cboInstrumentType.setEnabled(false);
////                txtInstrumentNo1.setEditable(false);
//                txtInstrumentNo1.setEnabled(false);
////                txtInstrumentNo2.setEditable(false);
//                txtInstrumentNo2.setEnabled(false);
//                tdtInstrumentDate.setEnabled(false);
//                txtParticulars.setEditable(false);
               
            }
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
           // btnPanNo.setEnabled(false);
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")){
                     double amount=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                if(rdoTransactionType_Credit.isSelected() && amount>=TrueTransactMain.PANAMT) {
                 //       btnPanNo.setEnabled(true);
                     }
                else if(amount<TrueTransactMain.PANAMT) {
                 //      btnPanNo.setEnabled(false);
                     }
            }
    }//GEN-LAST:event_btnEditActionPerformed
        
        // To display the All the Product Id's which r having status as
        // created or updated, in a table...
        private void popUp(int field) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            viewType = field;
            if(field==EDIT || field==DELETE || field==VIEW ){   //Edit=0 and Delete=1
                ArrayList lst = new ArrayList();
                lst.add("TRANS_ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                viewMap.put(CommonConstants.MAP_NAME, "viewCashTransaction");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
                whereMap.put("INIT_BRANCH", TrueTransactMain.selBranch);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            } else if (field == ACCNO ) {
                updateOBFields();
                if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
                    if(rdoTransactionType_Debit.isSelected()) {
                        if(observable.getProdType().equals("TL")){
                            whereMap.put("PAYMENT","PAYMENT");
                            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                        }
                        else{
                            viewMap.put(CommonConstants.MAP_NAME, "getDepositHoldersInterest");
                            
                            transDetails.setIsDebitSelect(true);
                        }
                    } else if(rdoTransactionType_Credit.isSelected()){
                        if(observable.getProdType().equals("TL"))
                            whereMap.put("RECEIPT","RECEIPT");                        
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                    } else {
                        ClientUtil.showMessageWindow("Select Payment or Receipt ");
                        return;
                    }
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                }
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }
            else if(field == PAN_NUM) {
                String act_num=CommonUtil.convertObjToStr(txtAccNo.getText());
                if(act_num.lastIndexOf("_")!=-1)
                    act_num=act_num.substring(0,act_num.lastIndexOf("_"));                    
                whereMap.put("ACT_NUM",act_num);
                viewMap.put(CommonConstants.MAP_NAME, "getPanNumber");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            }
            else if(field==DEBIT_DETAILS) {
                whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                whereMap.put("CURRENT_DT", observable.getCurrentDate());
                viewMap.put(CommonConstants.MAP_NAME, "DebitTransDetails");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        
            }
             else{
                 whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                whereMap.put("CURRENT_DT", observable.getCurrentDate());
                viewMap.put(CommonConstants.MAP_NAME, "TellerEntryDetails");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
             }
//            if(whereMap.get("SELECTED_BRANCH")==null)
//                whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//            else
            if (field == ACCNO ){
                whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                setSelectedBranchID(TrueTransactMain.selBranch);
            }
            else
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
           
        }
        
        public void btnDepositClose(){
            flag = true;
            afterSaveCancel = true;
            if(!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON"))
                btnAuthorizeActionPerformed(null);
            if(!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON"))
                btnRejectActionPerformed(null);
            btnCloseActionPerformed(null);
        }
        
        public void btnDepositCancel(){
            afterSaveCancel = true;
            btnCancelActionPerformed(null);
            btnCloseActionPerformed(null);
        }
        
        public void depositCashAuth(HashMap depAuthMap,TermDepositUI termDepositUI){
            this.termDepositUI = termDepositUI;
            flagDepositAuthorize = true;
            String actNum = CommonUtil.convertObjToStr(depAuthMap.get("ACT_NUM"));
            if(actNum != null && !actNum.equals("")){
                flagDepAuth = false;
                observable.setDepLinkBatchId("");
            }else{
                flagDepAuth = true;
                observable.setDepLinkBatchId("DEP_LINK");
            }
            setViewType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
        }
        
        public HashMap transferingIntAmt(CTextField txtDepositAmount, HashMap intMap,TermDepositUI termDepositUI){
            if(intMap !=null){
                this.termDepositUI = termDepositUI;
                this.txtDepositAmount = txtDepositAmount;
                flag = true;
                flagDepLink = true;
                System.out.println("%%%%%%intMap :"+intMap);
                HashMap subNoMap = new HashMap();
                btnNewActionPerformed(null);
                subNoMap.put("DEPOSIT_NO",intMap.get("ACCOUNTNO"));
                subNoMap.put("CUST_ID",intMap.get("CUST_ID"));
                List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
                if(lst.size()>0){
                    subNoMap = (HashMap)lst.get(0);
                    String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                    String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                    String accountNo = depositNo + "_" + subNo;
                    intMap.put("ACCOUNTNO",accountNo);
                    cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                    cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                    txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                    lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                    txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                    lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                    cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                    setViewType(2);
                    intMap.put("PROD_TYPE","TD");
                    observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                    fillData(intMap);
                    cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                    txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                    cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                    txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                    lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                    txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                    lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                    txtInitiatorChannel.setText("CASHIER");
                    observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
//                    cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
//                    tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getCurrentDate()));
                    txtParticulars.setText(txtAccNo.getText());
                    txtParticulars.setEnabled(false);
                   //cboInstrumentType.setEnabled(false);
                    if(flagDepLink == true)
                        observable.setDepLinkBatchId("DEP_LINK");
                    rdoTransactionType_Debit.setSelected(true);
                    rdoTransactionType_Credit.setEnabled(false);
                    rdoTransactionType_Debit.setEnabled(false);
                    btnAccNo.setEnabled(false);
                    txtAmount.setEnabled(false);
                    cboProdType.setEnabled(false);
                    cboProdId.setEnabled(false);
                    txtAccNo.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnEdit.setEnabled(false);
                    btnSave.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnCancel.setEnabled(false);
                    btnView.setEnabled(false);
                }
                intMap.put("CASH_AMT",txtAmount.getText());
            }
            return intMap;
        }
        
        public HashMap transferingDepAmt(CTextField txtDepositAmount, HashMap intMap,TermDepositUI termDepositUI){
            if(intMap !=null){
                this.termDepositUI = termDepositUI;
                this.txtDepositAmount = txtDepositAmount;
                flagDeposit = true;
                flagDepLink = true;
                System.out.println("%%%%%%intMap :"+intMap);
                HashMap subNoMap = new HashMap();
                btnNewActionPerformed(null);
                subNoMap.put("DEPOSIT_NO",intMap.get("ACCOUNTNO"));
                subNoMap.put("CUST_ID",intMap.get("CUST_ID"));
                List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
                if(lst.size()>0){
                    subNoMap = (HashMap)lst.get(0);
                    String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                    String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                    String accountNo = depositNo + "_" + subNo;
                    intMap.put("ACCOUNTNO",accountNo);
                    cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                    cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                    txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                    lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                    txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                    lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                    cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                    setViewType(2);
                    intMap.put("PROD_TYPE","TD");
                    observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                    fillData(intMap);
                    observable.setDepInterestAmt("");
                    cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                    txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                    cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
//                    txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
//                    lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                    txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
//                    lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
//                    txtInitiatorChannel.setText("CASHIER");
                    txtParticulars.setText(txtAccNo.getText());
                    txtParticulars.setEnabled(false);
                   // cboInstrumentType.setEnabled(false);
                    observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
//                    cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
//                    tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getCurrentDate()));
                    if(flagDepLink == true)
                        observable.setDepLinkBatchId("");
                    rdoTransactionType_Debit.setSelected(true);
                    rdoTransactionType_Credit.setEnabled(false);
                    rdoTransactionType_Debit.setEnabled(false);
                    txtAmount.setEnabled(true);
                    cboProdType.setEnabled(false);
                    cboProdId.setEnabled(false);
                    txtAccNo.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnEdit.setEnabled(false);
                    btnSave.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnCancel.setEnabled(false);
                    btnView.setEnabled(false);
                }
                intMap.put("CASH_AMT",txtAmount.getText());
            }
            return intMap;
        }
        // this method is called automatically from ViewAll...
        public void fillData(Object param) {
            final HashMap hash = (HashMap) param;
            System.out.println("param DATE ======"+param);
            System.out.println("HASH DATE ======"+hash);
            double depAmt = 0.0;
            hash.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
            if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
            }
             if(hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")){
             System.out.println("HASH DATE ======"+hash);
             fromAuthorizeUI = false;
             viewType =AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
          //  btnSaveDisable();
    }
            asAndWhenMap=new HashMap();
            if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW || viewType==LINK_BATCH || viewType==LINK_BATCH_TD || viewType==DEBIT_DETAILS|| viewType==TELLER_ENTRY_DETIALS) {
                setModified(true);
                isFilled = true;
                hash.put("USER_ID",ProxyParameters.USER_ID);
                hash.put("WHERE", hash.get("TRANS_ID"));
                if(hash.containsKey("TRANS_DT")){
                    hash.put("TRANS_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("TRANS_DT"))));
                }
               log.info("Hash: " + hash);
               if(viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE) {
                  if(viewType==EDIT || viewType==AUTHORIZE){
                      HashMap hashmap1=new HashMap();
                      hashmap1.put("TRANS_ID",hash.get("TRANS_ID"));
                      hashmap1.put("BRANCH_ID",hash.get("BRANCH_ID"));
                      System.out.println("hash  "+hash);
                      List lst=ClientUtil.executeQuery("getOrgRespDetails", hashmap1);
                   
                      if(lst!=null && lst.size()>0){
                          hashmap1=(HashMap)lst.get(0);
                          setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
                          setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
                          setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
                          setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH")));
                          setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH_NAME")));
                          setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
                          setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH_NAME")));
                          setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
                          setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
                          setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE"))); 
                          //btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("TYPE"))); 
                          //btnOrgOrResp.setVisible(true);
                          observable.setHoAccount(true);
                      }
                       
                  }

                   if(viewType==AUTHORIZE){
                       HashMap hmap=new HashMap();
                       
                       List mainList=ClientUtil.executeQuery("getSelectNominalMemFee", hash);
                       if(mainList!=null && mainList.size()>0){
                           hmap=(HashMap)mainList.get(0);
                           String allowAuth=CommonUtil.convertObjToStr(hmap.get("ALLOW_AUTH_BY_STAFF"));
                           if(allowAuth.equals("N")){
                              hmap.put("USER_ID",TrueTransactMain.USER_ID);
                              hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                              hash.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                              System.out.println("hash"+hash);
                              String accountNo=CommonUtil.convertObjToStr(hash.get("ACCOUNT NO"));
                              System.out.println("hash accountNo"+accountNo);
                              hmap.put("ACCOUNT NO",accountNo);
                              List lst=ClientUtil.executeQuery("getStaffIdForAccount", hmap);
                              List lst1=ClientUtil.executeQuery("getStaffIdForLoggedUser", hmap);
                              String staffId="";
                              String loggedStaffId="";
                              if(lst!=null && lst.size()>0){
                                 hmap=(HashMap)lst.get(0);
                                 staffId=CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                                  }
                              if(lst1!=null && lst1.size()>0){
                                 hmap=(HashMap)lst1.get(0);
                                 loggedStaffId=CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                                 }
                              if(!staffId.equals("") || !loggedStaffId.equals("")){
                                if(staffId.equals(loggedStaffId)){
                                    ClientUtil.displayAlert("Authorization not allowed in own account");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                            }
                          }
                       }
                   }
                   if(viewType==EDIT)
                       hash.put("MODE_OF_OPERATION","EDIT");
                   if(viewType==DELETE)
                       hash.put("MODE_OF_OPERATION","DELETE");
                   if(viewType==AUTHORIZE)
                       hash.put("MODE_OF_OPERATION","AUTHORIZE");
                 hash.put("TRANS_DT", currDt.clone());
                 hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                   ClientUtil.execute("insertauthorizationLock", hash);
               }
               observable.populateData(hash);// Called to display the Data in the UI fields...
//             if(viewType==AUTHORIZE){
//               if(observable.isHoAccount()){
//                   if(observable.getRdoTransactionType_Credit()){
//                       btnOrgOrResp.setText("O");
//                   }else if(observable.getRdoTransactionType_Debit()){
//                       btnOrgOrResp.setText("R");
//                   }
//               }
//             }
               //                as an whencustomerADTL
               if(observable.getLinkMap() !=null && observable.getLinkMap().size()>0 && ( viewType!=LINK_BATCH)){
                   HashMap map=observable.getLinkMap();
                   viewType=LINK_BATCH;
                   if(map.containsKey("AS_CUSTOMER_COMES") && map.get("AS_CUSTOMER_COMES")!=null && map.get("AS_CUSTOMER_COMES").equals("Y")
                   && rdoTransactionType_Credit.isSelected()) {
                       map.put("TRANS_DT",currDt.clone());
                       map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
//                       TableDialogUI  tableDialogUI= new TableDialogUI("getCashTransactionTOForAuthorzationLinkBatch",map,this);
//                       
//                       tableDialogUI.setTitle("Loan/Advances Authorize Transaction");
//                       tableDialogUI.show();
                       
                    HashMap selectMap=new HashMap();
                    selectMap.put("LINK_BATCH_ID",map.get("ACCT_NUM"));
                    selectMap.put("TODAY_DT", currDt.clone());
                    selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                    TableDialogUI tableDialogUI= new TableDialogUI("getAllTransactionViewAD",selectMap,"");
                       tableDialogUI.setTitle("Loan/Advances Authorize Transaction");
                       tableDialogUI.show();
                   }
               }
               if(observable.getLinkMap() !=null && observable.getLinkMap().size()>0 && observable.getLinkMap().get("ACCT_NUM")!=null){
                    if(observable.getProdType().equals("TL") && observable.getLinkMap().containsKey("BEHAVES_LIKE") && 
                      CommonUtil.convertObjToStr(observable.getLinkMap().get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
                        HashMap map=new HashMap();
                        map.put("ACT_NUM", observable.getLinkMap().get("ACCT_NUM"));
                        map.put("CURR_DT", observable.getCurrentDate());
                        List lst=ClientUtil.executeQuery("getDisbursementTrenchDetailsCorpTL", map);
                        if(lst !=null && lst.size()>0){
                            map=(HashMap)lst.get(0);
                            map.put("BEHAVES_LIKE",observable.getLinkMap().get("BEHAVES_LIKE"));
                            transDetails.setCorpDetailMap(map);
                        } else {
                            ClientUtil.showAlertWindow("Trench details not found...");
                            btnCancelActionPerformed(null);
                            return;
                        }
                    }
                   
                   long no_of_installment=0;
                   no_of_installment=CommonUtil.convertObjToLong(observable.getLinkMap().get("NO_OF_INSTALLMENT"));
                   asAndWhenMap =  interestCalculationTLAD((String)observable.getLinkMap().get("ACCT_NUM"),no_of_installment);
                   if(asAndWhenMap!=null && asAndWhenMap.size()>0){
                       asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                       transDetails.setAsAndWhenMap(asAndWhenMap);
                   }
               }
               //AS END
               // To set the Value of Transaction Id...
               if(observable.getProdType().equals("TD"))
                   if(flagDepAuth == true){
                       hash.put("ACCOUNT NO","");
                       observable.setTxtAccNo("");
                       observable.setTxtAccHd(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                       observable.setCboProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                       observable.setProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                       observable.setCboProdId(CommonUtil.convertObjToStr(""));
                   }
               final String TRANSID = (String) hash.get("TRANS_ID");
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    System.out.println("#$#$$ yesNo : "+yesNo);
                    if (yesNo==0) {
                        TTIntegration ttIntgration = null;
                        HashMap reportTransIdMap = new HashMap();
                        reportTransIdMap.put("TransId", TRANSID);
                        reportTransIdMap.put("TransDt", observable.getCurrentDate());
                        reportTransIdMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(reportTransIdMap);
                        String transType = "";
                        if (rdoTransactionType_Debit.isSelected()) {
                            transType = "DEBIT";
                        } else {
                            transType = "CREDIT";
                        }
//                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//                            ttIntgration.integrationForPrint("ReceiptPayment");
//                        } else {
                            if (transType.equals("DEBIT")) {
                                ttIntgration.integrationForPrint("CashPayment");
                            } else {
                                ttIntgration.integrationForPrint("CashReceipt");
                            }
//                        }
                    }
                }
               
               String ACCOUNTNO="";
               if(hash.containsKey("ACCOUNT NO")&& hash.get("ACCOUNT NO")!=null)
                   ACCOUNTNO = (String) hash.get("ACCOUNT NO");
               //                if(observable.getProdType().equals("TL"))  //For Processing Charges
               //                    observable.getLinkBatchId();
               if(observable.getProdType().equals("TD"))
                   transDetails.setIsDebitSelect(true);
               //To Set the Value of Account holder Name and the Balances in UI...
               if (viewType!=LINK_BATCH) {
                   if(!ACCOUNTNO.equals("")){
                       observable.setAccountName(ACCOUNTNO);
                      // lblHouseName.setText(observable.getLblHouseName());
                       if (observable.getProdType().equals("GL")) {
                           //                        observable.setAccountName(observable.getTxtAccHd());
                           ACCOUNTNO = observable.getTxtAccHd();
                       }
                       transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                       transDetails.setSourceFrame(this);
                   } else if (!observable.getTxtAccHd().equals("")) {
                       transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccHd());
                       transDetails.setSourceFrame(this);
                   }
//                   btnViewTermLoanDetails.setVisible(false);
//                   btnViewTermLoanDetails.setEnabled(false);
               } if (viewType==LINK_BATCH) {
                   transDetails.setTransDetails("TL", ProxyParameters.BRANCH_ID, ACCOUNTNO);
                   transDetails.setSourceFrame(this);
//                   btnViewTermLoanDetails.setVisible(true);
//                   btnViewTermLoanDetails.setEnabled(true);
               }
               setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
               if(observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES")!=null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y"))
                   setButtonEnableDisable(false);
               if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || viewType==LINK_BATCH || viewType==LINK_BATCH_TD) {
                   ClientUtil.enableDisable(this, false);
                   // Disables the panel...
               }else{
                   ClientUtil.enableDisable(this, true);// Enables the panel...
                   enableDisableButtons(true);         // To Enable the Buttons(folder) buttons in UI...
                   textDisable();                  // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
                   this.btnDelete.setEnabled(true);
               }
               setEditFieldsEnable(false);
               observable.setStatus();             // To set the Value of lblStatus...
               
               if(hash.get("INSTRUMENT_NO2")!= null && hash.get("INSTRUMENT_NO2").equals("DEPOSIT_PENAL")){
                   viewType=LINK_BATCH_TD;
                   HashMap penalMap = new HashMap();
                   penalMap.put("ACCT_NUM",hash.get("ACCOUNT NO"));
                   penalMap.put("TRANS_ID",hash.get("TRANS_ID"));
                   penalMap.put("TRANS_DT",currDt.clone());
                   penalMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                 //  TableDialogUI  tableDialogUI= new TableDialogUI("getCashTransactionTOForAuthorzationLinkBatch",penalMap,this);
//                   tableDialogUI.setTitle("Deposit Transaction");
//                   tableDialogUI.show();
                   observable.setDepositPenalAmt(transDetails.getPenalAmount());
                   observable.setDepositPenalMonth(transDetails.getPenalMonth());
                   ClientUtil.enableDisable(this, false);
                   btnCancel.setEnabled(true);
               }
               if(viewType==AUTHORIZE||viewType==LINK_BATCH || viewType==LINK_BATCH_TD) {
                   btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                   btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                   btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
               }
               if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                   ClientUtil.enableDisable(this, false);// Disables the panel...
                   enableDisableButtons(false);
               }
            }else if (viewType==ACCNO) {
                observable.setAsAnWhenCustomer("");
                //                if(rdoTransactionType_Debit.isSelected()) {
                //                    String DEPOSIT_NO = CommonUtil.convertObjToStr(hash.get("ACT_NUM"));
                //                    txtAccNo.setText(DEPOSIT_NO);
                //                    observable.setAccountName(DEPOSIT_NO);
                //                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, DEPOSIT_NO);
                //                    txtAmount.setText("");
                //                    txtAccNo.setEditable(false);
                //                } else if(rdoTransactionType_Credit.isSelected()){
                String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
               // lblHouseName.setText(CommonUtil.convertObjToStr(hash.get("HOUSENAME")));
                if(observable.getProdType().equals("TD") && ACCOUNTNO.lastIndexOf("_")==-1){
                    ACCOUNTNO=ACCOUNTNO+"_1";
                }
                System.out.println("filldata###"+ACCOUNTNO);
                observable.setTxtAccNo(ACCOUNTNO);
                HashMap mapHash=observable.asAnWhenCustomerComesYesNO(ACCOUNTNO);
                //            observable.setLblAccName(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                long noofInstallment=0;
                if(rdoTransactionType_Credit.isSelected() && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
                    HashMap transMap=new HashMap();
                    transMap.put("LINK_BATCH_ID",ACCOUNTNO);
                    transMap.put("TRANS_DT",currDt.clone());
                    transMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                    List pendingList=ClientUtil.executeQuery("getPendingTransactionTL",transMap);
                    if(pendingList!=null && pendingList.size()>0){
                        HashMap hashTrans=(HashMap)pendingList.get(0);
                        String trans_actnum=CommonUtil.convertObjToStr(hashTrans.get("LINK_BATCH_ID"));
                        if(trans_actnum.equals(ACCOUNTNO)){
                            ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first  ");
                            hashTrans=null;
                            pendingList=null;
                            observable.setTxtAccNo("");
                            txtAccNo.setText("");
                            return;
                        }
                    }
                 if(observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES")!=null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y"))
                    if(mapHash !=null && mapHash.containsKey("INSTALL_TYPE")  && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") ||
                    mapHash.get("INSTALL_TYPE").equals("LUMP_SUM") || mapHash.get("INSTALL_TYPE").equals("EYI"))) && rdoTransactionType_Credit.isSelected()==true && viewType!=AUTHORIZE){
                        
                        String remark = COptionPane.showInputDialog(this,"NO OF INSTALLMENT WANT TO PAY");
                        //                        int remark=ClientUtil.confirmationAlert("NO OF INSTALLMENT WANT TO PAY");
                        System.out.println("remark"+remark);
                        if(CommonUtil.convertObjToStr(remark).equals(""))
                            remark="0";
                        try {
                            noofInstallment=Long.parseLong(remark);
                            //                            observable.setNoofInstallment(noofInstallment);
                        } catch (java.lang.NumberFormatException e) {
                            ClientUtil.displayAlert("Invalid Number...");
                            txtAccNoFocusLost(null);
                            return;
                        }
                        //                        noofInstallment=0;
                    }
                }
                observable.setAccountName(ACCOUNTNO);
                String act_name=setAccountName(ACCOUNTNO);
               // lblHouseName.setText(observable.getLblHouseName());
                System.out.println("fill###"+observable.getLblAccName());
                transDetails.setCorpDetailMap(new HashMap());
                if(ACCOUNTNO !=null && ACCOUNTNO.length()>0 && rdoTransactionType_Credit.isSelected()) {
                    if((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES")!=null && mapHash.get("AS_CUSTOMER_COMES").equals("Y"))){
                        asAndWhenMap =  interestCalculationTLAD(ACCOUNTNO,noofInstallment);
                        if(asAndWhenMap!=null && asAndWhenMap.size()>0){
                            asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                            transDetails.setAsAndWhenMap(asAndWhenMap);
                            if(asAndWhenMap.containsKey("NO_OF_INSTALLMENT") && CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"))>0)
                                noofInstallment=CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"));
                        }
                    }
                } else if(ACCOUNTNO !=null && ACCOUNTNO.length()>0 && rdoTransactionType_Debit.isSelected()) {  // For Corporate Loan Disbursement added by Rajesh
                    if(observable.getProdType().equals("TL") && mapHash.containsKey("BEHAVES_LIKE") && 
                      CommonUtil.convertObjToStr(mapHash.get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
                        HashMap map=new HashMap();
                        map.put("ACT_NUM", ACCOUNTNO);
                        map.put("CURR_DT", observable.getCurrentDate());
                        List lst=ClientUtil.executeQuery("getDisbursementTrenchDetailsCorpTL", map);
                        if(lst !=null && lst.size()>0){
                            map=(HashMap)lst.get(0);
                            map.put("BEHAVES_LIKE",mapHash.get("BEHAVES_LIKE"));
                            transDetails.setCorpDetailMap(map);
                        } else {
                            ClientUtil.showAlertWindow("All trenches have been disbursed...");
                            observable.setLblAccName("");
                            txtAccNo.setText("");
                            return;
                        }
                    }
                }
                if (observable.getLblAccName().length() > 1) {
                    transDetails.setTransDetails(null, null, null);
                    transDetails.setSourceFrame(null);
                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                    transDetails.setSourceFrame(this);
                    if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")){
                        observable.setTxtAmount("");
                    } else {
                        observable.setTxtAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    }
                    observable.setLimitAmount(transDetails.getLimitAmount());
                    System.out.println("limitAmount"+observable.getLimitAmount());
                    if(observable.getProdType().equals("TL") || observable.getProdType().equals("AD") ){
                        observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                        observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT",new Long(noofInstallment));
                        if(rdoTransactionType_Credit.isSelected() && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW )
                            if(mapHash !=null && mapHash.containsKey("INSTALL_TYPE")  && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") ||
                            mapHash.get("INSTALL_TYPE").equals("LUMP_SUM") || mapHash.get("INSTALL_TYPE").equals("EYI")))){
                                double totalEMIAmt=setEMIAmount();
                                observable.setTxtAmount(String.valueOf(totalEMIAmt));
                                txtAmount.setEnabled(false);
                                
                                
                            }
                        
                        //                            observable.checkBalanceOperativeAccount(); dont delete becase  if product level enable means its working
                        
                        //                            observable.checkBalanceOperativeAccount(); dont deleted
                        
                    }
                    //                        if(observable.isFlag()){
                    //                            transDetails.setLimitAmount(observable.getLimitAmount());
                    //                        }
                    if(rdoTransactionType_Credit.isSelected() && observable.getProdType().equals("TD")){
                        double shadowCredit = CommonUtil.convertObjToDouble(transDetails.getShadowCredit()).doubleValue();
                        if(shadowCredit>0){
                            ClientUtil.showAlertWindow("Already Transaction is completed, pending for authorization...");
                            btnCancelActionPerformed(null);
                            return;
                        }else{
                            double txtAmt = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
                            depAmt = CommonUtil.convertObjToDouble(transDetails.getDepositAmt()).doubleValue();
                            double balance = txtAmt - depAmt;
                            if(balance>0){
                                observable.setTxtAmount(String.valueOf(balance));
                                txtAmount.setText(String.valueOf(balance));
                            }
                        }
                    }
                    if(rdoTransactionType_Debit.isSelected() && observable.getProdType().equals("TD")){
                        if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")){
                            observable.setTxtAmount("");
                        } else {
                            observable.setTxtAmount(transDetails.getDepInterestAmt());
                            //                            observable.setTxtAmount("");
                            //                            transDetails.setDepInterestAmt()
                            double intAmt = CommonUtil.convertObjToDouble(transDetails.getDepInterestAmt()).doubleValue();
                            if(intAmt>0){
                                txtAmount.setText(transDetails.getDepInterestAmt());
                                txtAmount.setEnabled(false);
                                observable.setDepInterestAmt(txtAmount.getText());
                            }else{
                                ClientUtil.showMessageWindow("Upto Date Interest is Paid...");
                                btnCancelActionPerformed(null);
                                txtAmount.setText("");
                            }
                        }
                    }
                    
                    observable.ttNotifyObservers();
                    if(rdoTransactionType_Debit.isSelected() && observable.getProdType().equals("TD"))
                        if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")){
                            txtParticulars.setText("");
                        } else {
                            txtParticulars.setText("To,Fd Interest " + CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                        }
                    checkLoanTransaction(hash);
                    //Added By Suresh
                    if(observable.getProdType().equals("TD")){
                        if (depBehavesLike.equals("RECURRING")) {
                            if(transDetails.isClearTransFlag() ==true){ 
                                txtAccNo.setText("");
                                txtAmount.setText("");
//                                lblAccName.setText("");
//                                lblHouseName.setText("");
                            }
                        }
                    }
                }
                // Added By kannan
                if(observable.getProdType().equals("OA")){
                boolean OverDue = false;
                HashMap operativeMap = new HashMap();
                operativeMap.put("ACT_NUM", observable.getTxtAccNo());
                List allCustLst = ClientUtil.executeQuery("getCustIdfromMembershipLiability", operativeMap);
                if (allCustLst != null && allCustLst.size() > 0) {
                    HashMap SingleMap = new HashMap();
                    SingleMap = (HashMap) allCustLst.get(0);                    
                    SingleMap.put(CommonConstants.BRANCH_ID,ProxyParameters.BRANCH_ID);
                    List dueList = ClientUtil.executeQuery("getSelectMembershipLiabilityDetails", SingleMap);
                    double dueAmt = 0.0;
                    if (dueList != null && dueList.size() > 0) {
                        for (int i = 0; i < dueList.size(); i++) {
                            HashMap OverDueMap = new HashMap();
                            OverDueMap = (HashMap) dueList.get(i);
                            dueAmt = CommonUtil.convertObjToDouble(OverDueMap.get("PRINC_DUE"));
                            if (dueAmt > 0) {
                                i = dueList.size();
                                OverDue = true;
                            }
                        }
                    }
                }
                if (OverDue) {
                    ClientUtil.showAlertWindow("Overdue Loans In A/C, Click Membership Liability Button For More Details");
                }
                }

                //                } else {
                //                    ClientUtil.displayAlert("Invalid A/c Number or Closed Account...");
                //                }
                String act_num=observable.getTxtAccNo();
                HashMap inputMap=new HashMap();
                inputMap.put("ACCOUNTNO",act_num);
                chqBalList = ClientUtil.executeQuery("getMinBalance", inputMap);
                HashMap lockmap=new HashMap();
                lockmap.put("ACCOUNTNO",act_num);
                List lockList=ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
                lockmap=null;
                if(lockList!=null && lockList.size()>0){
                lockmap=(HashMap)lockList.get(0);
                String lockStatus=CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
                if(lockStatus.equals("Y")){
                    ClientUtil.displayAlert("Account is locked");
                    txtAccNo.setText("");
                }
                }
            } else if (viewType == ACCTHDID) {
                String acHdId = hash.get("A/C HEAD").toString();
                String bankType=CommonConstants.BANK_TYPE;
                System.out.println("bankType"+bankType);
                String customerAllow="";
                String hoAc="";
                cboProdId.setSelectedItem("");
                this.txtAccNo.setText("");
            //    txtAccHdId.setText(acHdId);
                
                HashMap hmap=new HashMap();
                hmap.put("ACHEAD",acHdId);
                List list=ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
                if(list!=null && list.size()>0){
                    hmap=(HashMap)list.get(0);
                    customerAllow=CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                    hoAc=CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
                }
                if(bankType.equals("DCCB")){
                  if(hoAc.equals("Y")){
                    //btnOrgOrResp.setVisible(true);
                    observable.setHoAccount(true); 
                }else{
                     observable.setHoAccount(false);
                   //  btnOrgOrResp.setVisible(false);
                  }
                }
                if(customerAllow.equals("Y")){
                    String AccNo = COptionPane.showInputDialog(this,"Enter Acc no");
                    hmap.put("ACC_NUM",AccNo);
                    List chkList=ClientUtil.executeQuery("checkAccStatus", hmap);
                    if(chkList!=null && chkList.size()>0){
                        hmap=(HashMap)chkList.get(0);
                       observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
                         observable.setTxtAccNo(AccNo);
                        observable.setClosedAccNo(AccNo);
                    }else{
                        ClientUtil.displayAlert("Invalid Account number");
                      //  txtAccHdId.setText("");
                        return;
                    }
                }
                observable.setCr_cash(hash.get("CR_CASH").toString());
                observable.setDr_cash(hash.get("DR_CASH").toString());
                observable.setBalanceType((String) hash.get("BALANCETYPE"));
                observable.setTxtAccHd(acHdId);
                observable.setCboProdType((String) cboProdType.getSelectedItem());
                System.out.println("observable.getProdType(), acHdId " + observable.getProdType() + ":" + acHdId);
                transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, acHdId);
                transDetails.setSourceFrame(this);
                observable.ttNotifyObservers();
                
                this.setRadioButtons();
            }
            else if( viewType==PAN_NUM) {
                String panNumber = CommonUtil.convertObjToStr(hash.get("PAN_NUMBER"));
             //   txtPanNo.setText(panNumber);
                //                observable.setTxtPanNo(panNumber);
            }
            if(rdoTransactionType_Credit.isSelected() && observable.getLinkMap() !=null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")&&  observable.getLinkMap().get("AS_CUSTOMER_COMES")!=null){
                observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr( observable.getLinkMap().get("AS_CUSTOMER_COMES")));
            }else
                observable.setAsAnWhenCustomer("");
            if(termDepositUI !=null && flagDepositAuthorize == true){
                if(termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")){
                    btnAuthorize.setEnabled(true);
                    btnReject.setEnabled(false);
                    btnView.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnCancel.setEnabled(false);
                }
                if(termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")){
                    btnReject.setEnabled(true);
                    btnAuthorize.setEnabled(false);
                    btnView.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnCancel.setEnabled(false);
                }
            }
            if(termDepositUI ==null){
                if(observable.getProdType().equals("TD")){
                    if(rdoTransactionType_Credit.isSelected() ){
                        if(depAmt != 0 && hash.get("PRODUCTTYPE").equals("RECURRING"))
                            txtAmount.setEnabled(true);
                        else
                            txtAmount.setEnabled(false);
                    }
                    if(rdoTransactionType_Debit.isSelected()) {
                        if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")){
                            txtAmount.setEnabled(true);
                        } else {
                            txtAmount.setEnabled(false);
                        }
                    }
                    if(viewType==EDIT ||viewType==LINK_BATCH || viewType==LINK_BATCH_TD){
                        ClientUtil.enableDisable(this, false);
                        btnCancel.setEnabled(true);
                        btnSave.setEnabled(false);
                    }
                }
            }
            if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) && 
            !observable.getProdType().equals("") && observable.getProdType().equals("GL")){
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD",observable.getTxtAccHd());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if(head!=null && head.size()>0){
                    acctMap = (HashMap)head.get(0);
                    if(!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")){
                        if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true){
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true){
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        }else if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true){
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true){
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    }else{
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                }
            }else{
                btnCurrencyInfo.setVisible(false);
                btnCurrencyInfo.setEnabled(false);
            }
            
            if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
                btnDelete.setEnabled(false);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnView.setEnabled(false);
            }
        }
        private double setEMIAmount(){
            HashMap allAmtMap=new HashMap();
            double totEmiAmount=0.0;
            allAmtMap=observable.getALL_LOAN_AMOUNT();
            if(allAmtMap!=null && allAmtMap .size()>0){
                if(allAmtMap.containsKey("POSTAGE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue()>0)
                    totEmiAmount=CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue();
                if(allAmtMap.containsKey("ARBITRARY CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue();
                if(allAmtMap.containsKey("LEGAL CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue();
                if(allAmtMap.containsKey("INSURANCE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue();
                if(allAmtMap.containsKey("MISCELLANEOUS CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue();
                if(allAmtMap.containsKey("EXECUTION DECREE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue();
                if(allAmtMap.containsKey("ADVERTISE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue();
                if(allAmtMap.containsKey("PENAL_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue();
                if(allAmtMap.containsKey("CURR_MONTH_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue();
                if(allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue()>0)
                    totEmiAmount+=CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                
            }
            return totEmiAmount;
        }
        private HashMap interestCalculationTLAD(String accountNo,long noOfInstallment){
            HashMap map=new HashMap();
            HashMap hash=null;
            try{
                String prod_id="";
                map.put("ACT_NUM",accountNo);
                //		if((ComboBoxModel)cboProdId.getModel()!=null)
                //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
                //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
                map.put("PROD_ID",prod_id);
                map.put("TRANS_DT", currDt.clone());
                map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                String mapNameForCalcInt = "IntCalculationDetail";
                if (observable.getProdType().equals("AD")) {
                    mapNameForCalcInt = "IntCalculationDetailAD";
                }
                List lst=ClientUtil.executeQuery(mapNameForCalcInt, map);
                if(lst !=null && lst.size()>0){
                    hash=(HashMap)lst.get(0);
                    if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                        hash=new HashMap();
                        return hash;
                    }
                    map.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                    map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    if(noOfInstallment>0)
                        map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                    
                    //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                    map.putAll(hash);
                    map.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                    map.put("CURR_DATE", observable.getCurrentDate());
                    System.out.println("map before intereest###"+map);
                    //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                    observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                    hash=observable.loanInterestCalculationAsAndWhen(map);
                    if(hash==null)
                        hash=new HashMap();
                    hash.putAll(map);
                    System.out.println("hashinterestoutput###"+hash);
                    hash.put("AS_CUSTOMER_COMES",map.get("AS_CUSTOMER_COMES"));
                }
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
            return hash;
        }
        //To enable and/or Disable buttons(folder)...
        private void enableDisableButtons(boolean yesno) {
//            btnAccNo.setEnabled(yesno);
//            btnAccHdId.setEnabled(yesno);
//            btnCurrencyInfo.setEnabled(yesno);
//            btnDenomination.setEnabled(yesno);
//            btnPanNo.setEnabled(yesno);
        }
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
        enableDisableButtons(true);                 // To enable the buttons(folder) in the UI...
        observable.setInitiatorChannelValue();  // To Set Initiator Type as Cashier...
        setModified(true);
        btnDelete.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        //btnPanNo.setEnabled(false);
        observable.setCbmProdId("");
        cboProdId.setModel(observable.getCbmProdId());
       // btnAccHdId.setEnabled(false);
        btnAccNo.setEnabled(false);
        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
    }//GEN-LAST:event_btnNewActionPerformed
    
    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
    private void textDisable(){
       // txtInitiatorChannel.setEnabled(false);  //To make this textBox non editable...
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

    private void btnViewTermLoanDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTermLoanDetailsActionPerformed
        // TODO add your handling code here:
        String transType = "";
        boolean showDueTable = false;
        boolean penalDepositFlag = false;

        //        payment radio button
        boolean debit = rdoTransactionType_Debit.isSelected();
        //        reciept radio button
        boolean credit = rdoTransactionType_Credit.isSelected();
        if(debit){
            transType = CommonConstants.DEBIT;
        }else if(credit){
            transType = CommonConstants.CREDIT;
        }
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if((prodType.equals("AD")||prodType.equals("TL")) && credit){
            showDueTable = true;
        }else{
            showDueTable = false;
        }
        double penalAmt = CommonUtil.convertObjToDouble(observable.getDepositPenalAmt()).doubleValue();
        if(penalAmt > 0){
            penalDepositFlag = true;
        }else{
            penalDepositFlag = false;
        }
        termLoanDetails = new ArrayList();
        termLoansDetailsMap = new HashMap();
        termLoanDetails = transDetails.getTblDataArrayList();
        termLoansDetailsMap.put("DATA",termLoanDetails);
        System.out.println("tableVal"+termLoanDetails);
        String transViewAmount = CommonUtil.convertObjToStr(txtAmount.getText());
        new ViewLoansTransUI(termLoansDetailsMap,transViewAmount,transType,showDueTable,penalDepositFlag,"","").show();
    }//GEN-LAST:event_btnViewTermLoanDetailsActionPerformed

    private void btnCurrencyInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurrencyInfoActionPerformed
        // Add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            HashMap paramMap = new HashMap();
            HashMap whereMap = new HashMap();
            String type = "";
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                paramMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
                whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
                paramMap.put("NEW_MODE","NEW_MODE");
                whereMap.put("NEW_MODE","NEW_MODE");
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                paramMap.put("EDIT_MODE","EDIT_MODE");
                whereMap.put("EDIT_MODE","EDIT_MODE");
            }
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD",observable.getTxtAccHd());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if(head!=null && head.size()>0){
                acctMap = (HashMap)head.get(0);
                if(!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")){
                    if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true){
                        type = "DEBIT";
                    }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true){
                        type = "CREDIT";
                    }else if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true)
                    observable.setReconcile("Y");
                    else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true)
                    observable.setReconcile("Y");
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                }
            }
            whereMap.put("TRANS_TYPE",type);
            paramMap.put("TRANS_TYPE",type);
//            whereMap.put("AC_HEAD_ID",txtAccHdId.getText());
//            paramMap.put("AC_HEAD_ID",txtAccHdId.getText());
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
////                whereMap.put("TRANS_ID",lblTransactionIDDesc.getText());
////                paramMap.put("TRANS_ID",lblTransactionIDDesc.getText());
            }
            paramMap.put(CommonConstants.MAP_NAME,"getSelectReconciliationTransaction");
            paramMap.put(CommonConstants.MAP_WHERE,whereMap);
            if (transCommonUI==null)
            transCommonUI = new TransCommonUI(paramMap);
            transCommonUI.show();
            transCommonUI.setTransAmount(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
            paramMap = null;
            whereMap = null;
        }
    }//GEN-LAST:event_btnCurrencyInfoActionPerformed

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        double transAmt =0;
        if(CommonUtil.convertObjToStr(txtAmount.getText()).length()>0){
            transAmt =CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        }
        if(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        }else{
            txtAmount.setToolTipText("Zero");
        }

        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(prodType.equals("GL")){
//            btnPanNo.setEnabled(false);
//            txtPanNo.setText("");
        }
        /* added by nikhil for the view TermLoan details operation */
        if((prodType.equals("TL") || prodType.equals("AD") )&& txtAmount.getText().length() > 0){
            btnViewTermLoanDetails.setVisible(true);
            btnViewTermLoanDetails.setEnabled(true);

            if(!(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT)){

                if(asAndWhenMap !=null && asAndWhenMap.size()>0){
                    double penalInt= CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if(asAndWhenMap !=null && asAndWhenMap.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PENAL_WAIVER")).equals("Y")&& penalInt>0){
                        int result=ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
                        if(result==0){
                            observable.setPenalWaiveOff(true);
                        }else{
                            observable.setPenalWaiveOff(false);
                        }
                    }else{
                        double totalDue=transDetails.calculatetotalRecivableAmountFromAccountClosing();
                        if(asAndWhenMap !=null && asAndWhenMap.containsKey("INTEREST") && CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue()>0
                            && CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue()>0 && transAmt>=(totalDue-CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue())){
                            int result=ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                            if(result==0){
                                observable.setRebateInterest(true);
                            }else{
                                observable.setRebateInterest(false);
                            }
                        }
                    }

                }
            }
        }else{
            //             btnViewTermLoanDetails.setVisible(false);
            //             btnViewTermLoanDetails.setEnabled(false);

        }
        if(rdoTransactionType_Credit.isSelected()== true){
            String prodId ="";
            if(!((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("GL"))
            prodId = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            System.out.println("#####ProdId :"+prodId+"penalAmt :"+penalAmt);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID",prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            System.out.println("######## BHEAVES :"+lst);
            if(lst!=null && lst.size()>0){
                prodMap= (HashMap)lst.get(0);
                if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
                    HashMap recurringMap = new HashMap();
                    double amount =CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccNo.getText();
                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                    depNo = depNo.substring(0,depNo.lastIndexOf("_"));
                    System.out.println("######## BHEAVES :"+depNo);
                    recurringMap.put("DEPOSIT_NO",depNo);
                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if(lst!=null && lst.size()>0) {
                        recurringMap = (HashMap)lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        double finalAmount = 0.0;
                        if(penalAmt>0){
                            String[] obj ={"Penalty with Installments","Installments Only."};
                            int option =COptionPane.showOptionDialog(null,("Select The Desired Option"), ("Receiving Penal with Installments..."),
                                COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                            if(option ==0){
                                if(amount>penalAmt){
                                    amount = amount - penalAmt;
                                    finalAmount = amount % depAmount;
                                }else
                                finalAmount = amount % (penalAmt+depAmount);
                                if(finalAmount != 0){
                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..."+(depAmount+penalAmt));
                                    txtAmount.setText("");
                                }else{
                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                }
                            }else{
                                finalAmount = amount % depAmount;
                                if(finalAmount != 0){
                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"+
                                        "Deposit Amount is : "+depAmount);
                                    txtAmount.setText("");
                                }
                                observable.setDepositPenalAmt(String.valueOf(0.0));
                                observable.setDepositPenalMonth(String.valueOf(0.0));
                            }
                        }else{
                            finalAmount = amount % depAmount;
                            observable.setDepositPenalAmt(String.valueOf(0.0));
                            observable.setDepositPenalMonth(String.valueOf(0.0));
                            if(finalAmount != 0){
                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"+
                                    "Deposit Amount is : "+depAmount);
                                txtAmount.setText("");
                            }
                        }
                        System.out.println("######## BHEAVES REMAINING :"+finalAmount);
                    }
                    recurringMap = null;
                }
                prodMap = null;
                lst = null;
            }
            moreThanLoanAmountAlert();
            if (!prodType.equals("GL")){
//                double amount=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
//                if(amount>=TrueTransactMain.PANAMT) {
//                    btnPanNo.setEnabled(true);
//                }
//                else if(amount<TrueTransactMain.PANAMT) {
//                    btnPanNo.setEnabled(false);
//                    txtPanNo.setText("");
//                }
            }
        }
        if(rdoTransactionType_Debit.isSelected()== true){
            String prodId ="";
            if(!((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("GL"))
            prodId = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            System.out.println("#####ProdId :"+prodId);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID",prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            System.out.println("######## BHEAVES :"+lst);
            if(lst.size()>0){
                prodMap= (HashMap)lst.get(0);
                if(prodMap.get("BEHAVES_LIKE").equals("FIXED")){
                    HashMap recurringMap = new HashMap();
                    double amount =CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccNo.getText();
                    //                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                    depNo = depNo.substring(0,depNo.lastIndexOf("_"));
                    //                    System.out.println("######## BHEAVES :"+depNo);
                    recurringMap.put("DEPOSIT_NO",depNo);
                    lst = ClientUtil.executeQuery("getInterestDeptIntTable", recurringMap);
                    if(lst.size()>0) {
                        recurringMap = (HashMap)lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("PERIODIC_INT_AMT")).doubleValue();
                        double finalAmount = amount % depAmount;
                        System.out.println("######## BHEAVES REMAINING :"+finalAmount);
                        if(finalAmount == 0){
                            txtAmount.setEnabled(false);
                            System.out.println("######## BHEAVES :"+finalAmount);
                        }else{
                            //                            ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount... ");
                            //                            txtAmount.setText("");
                        }
                    }
                    recurringMap = null;
                }
                else if(prodMap.get("BEHAVES_LIKE").equals("DAILY") && prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED").equals("Y")){
                    HashMap dailyDepMap = new HashMap();
                    double amount =CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccNo.getText();
                    //                    System.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                    depNo = depNo.substring(0,depNo.lastIndexOf("_"));
                    //                    System.out.println("######## BHEAVES :"+depNo);
                    dailyDepMap.put("DEPOSIT_NO",depNo);
                    lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                    if(lst.size()>0) {
                        dailyDepMap = (HashMap)lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                        if(depAmount < amount){
                            System.out.println("@#$@#$%$^$%^amount" +amount+ " :depAmount: "+depAmount);
                            ClientUtil.displayAlert("Amount Greater than available balance!!");
                            txtAmount.setText("");
                            return;
                        }
                    }
                    dailyDepMap = null;

                }
                prodMap = null;
                lst = null;
            }
        }
        if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0){
            ClientUtil.displayAlert("amount should not be zero or empty");
            return;
        }
    }//GEN-LAST:event_txtAmountFocusLost

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
        //        System.out.println("callchange");
        //        observable.setTxtAmount(txtAmount.getText());
        //        observable.changeAmount();NOW ONLY COMMANT
        //        txtAmount.setText(observable.getTxtAmount());

    }//GEN-LAST:event_txtAmountActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        //To Set the Value of Account Head in UI...
//        if (cboProdType.getSelectedIndex() > 0) {
//            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
//            System.out.println("***************"+prodType);
//            clearProdFields();
//            //Added BY Suresh
//            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
//                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
//            }
//            populateInstrumentType();
//            observable.setProdType(prodType);
//            //this is for depoists
//
//            if (prodType.equals("GL")) {
////                if(TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))){
////                    //                observable.setCbmProdId(prodType);
////                    //                cboProdId.setModel(observable.getCbmProdId());
////                    txtAccNo.setText("");
////                    txtPanNo.setText("");
////                    btnPanNo.setEnabled(false);
////                    btnAccHdId.setEnabled(true);
////                    if (!( viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW || viewType==LINK_BATCH || viewType==LINK_BATCH_TD))
////                    observable.setTxtAccNo("");
////                    setProdEnable(false);
////                    cboProdId.setSelectedItem("");
////                }
////                else{
////                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
////                    observable.resetForm();
////                }
////                txtAccHdId.setEnabled(true);
//            } else {
//                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
//                    observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
//                    productBased();
//                }
//                setProdEnable(true);
//                observable.setCbmProdId(prodType);
//                cboProdId.setModel(observable.getCbmProdId());
//                //                observable.getCbmProdId().setKeyForSelected(observable.getCbmProdId().getDataForKey(observable.getCboProdId()));
//                //txtAccHdId.setEnabled(false);
//                btnAccNo.setEnabled(false);
//            }
//            btnViewTermLoanDetails. setEnabled(true);
//        }
//        if(viewType==AUTHORIZE || viewType==LINK_BATCH || viewType==LINK_BATCH_TD) {
//            cboProdId.setEnabled(false);
//            txtAccNo.setEnabled(false);
//            btnAccNo.setEnabled(false);
//        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // Add your handling code here:
        popUp(ACCNO);
        termLoanDetailsFlag = false;
        termLoansDetailsMap = null;
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        txtAccNoActionPerformed();
    }//GEN-LAST:event_txtAccNoFocusLost

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

    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // Add your handling code here:
        //ClientUtil.validateLTDate(tdtInstrumentDate);
    }//GEN-LAST:event_tdtDateFocusLost

    private void rdoTransactionType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_CreditActionPerformed
        // Add your handling code here:
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
            if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
                txtAccNo.setText("");
            }
        }
        if (rdoTransactionType_Credit.isSelected()) {
            String status="";
            //btnOrgOrResp.setText("O");
            String prodId = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            HashMap where = new HashMap();
            where.put("PRODUCT_ID",prodId);
            List lst= ClientUtil.executeQuery("getOpAcctProductTOByProdId", where);//OperativeAcctProductTO
            if(lst !=null && lst.size()>0){
                OperativeAcctProductTO map=(OperativeAcctProductTO)lst.get(0);
                status=map.getSRemarks();
            }

            if(status.equals("NRE")){
                ClientUtil.displayAlert("Credit Transactions Not Allowed For This Product!!!");
                rdoTransactionType_Debit.setSelected(true);
            }
//            txtTokenNo.setText("");
//            //                txtTokenNo.setEditable(false);
//            //                txtTokenNo.setEditable(false);
//            cboInstrumentType.setSelectedIndex(0);
//            txtInstrumentNo1.setText("");
//            txtInstrumentNo2.setText("");
//            tdtInstrumentDate.setDateValue("");
//
//            cboInstrumentType.setEnabled(false);
            //                txtInstrumentNo1.setEditable(false);
//            txtInstrumentNo1.setEnabled(false);
//            //                txtInstrumentNo2.setEditable(false);
//            txtInstrumentNo2.setEnabled(false);
//            tdtInstrumentDate.setEnabled(false);
            txtParticulars.setText("BY CASH");
            //                txtParticulars.setEditable(true);
        }
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(!prodType.equals("") && prodType.equals("GL")){
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD",observable.getTxtAccHd());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if(head!=null && head.size()>0){
                acctMap = (HashMap)head.get(0);
                if (transCommonUI!=null){
                    transCommonUI.dispose();
                    transCommonUI = null;
                }
                rdoTransactionType_Debit.setSelected(false);
                if(!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")){
                    if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true){
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true){
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    }else if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true){
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true){
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                }else{
                    btnCurrencyInfo.setVisible(false);
                    btnCurrencyInfo.setEnabled(false);
                }
            }
        }else{
            btnCurrencyInfo.setVisible(false);
            btnCurrencyInfo.setEnabled(false);
        }
        //                    rdoTransactionType_DebitActionPerformed(evt);

    }//GEN-LAST:event_rdoTransactionType_CreditActionPerformed

    private void rdoTransactionType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_DebitActionPerformed
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
            if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
                txtAccNo.setText("");
            }
            //                String token="";
            //                List list1;
            //                boolean tkn= false;
            if (rdoTransactionType_Debit.isSelected()) {
                //btnOrgOrResp.setText("R");
                if(custStatus.equals("Y") && txtAccNo.getText().length()>0)
                ClientUtil.displayAlert("MINOR ACCOUNT");
                //                        txtTokenNo.setEditable(false);
                //                        txtTokenNo.setEnabled(false);
                //                        HashMap tokenIssue = new HashMap();
                //                        tokenIssue.put("CURRENT_DT", currDt);
                //                        tokenIssue.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                //                        tokenIssue.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                //                        System.out.println("rdotransaction tokenissue"+tokenIssue);
                //                        List lst = ClientUtil.executeQuery("getStartTokenNo", tokenIssue);
                //                        if (lst.size()>0) {
                    //                            for(int i=0;i<lst.size();i++) {
                        //                                if(tkn==true) {
                            //                                    break;
                            //                                }
                        //                                HashMap tokenIssue1 = new HashMap();
                        //                                tokenIssue1 =(HashMap)lst.get(i);
                        //                                tokenIssue1.put("CURRENT_DT", currDt);
                        //                                tokenIssue1.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                        //                                tokenIssue1.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                        //                                if(tokenIssue1.get("SERIES_NO")==null) {
                            //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNoForPapeToken", tokenIssue1);
                            //                                }
                        //                                else {
                            //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNo", tokenIssue1);
                            //                                }
                        //                                HashMap map = new HashMap();
                        //                                if(list1!=null && list1.size()>0) {
                            //
                            //                                    map=(HashMap)list1.get(0);
                            //                                    if (CommonUtil.convertObjToInt(map.get("TOKEN_NO"))>=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")))) {
                                //                                        continue;
                                //                                    }
                            //                                    else {
                                //                                        token=CommonUtil.convertObjToStr(map.get("SERIES_NO"));
                                //                                        int t= CommonUtil.convertObjToInt(map.get("TOKEN_NO"));
                                //                                        long tk=t+1;
                                //                                        int a=0;
                                //                                        int b=0;
                                //                                        while(a==b) {
                                    //                                            HashMap where = new HashMap();
                                    //                                            where.put("SERIES_NO",token);
                                    //                                            where.put("TOKEN_NO",new Long(tk));
                                    //                                            where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                                    //                                            where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                                    //                                            System.out.println("@@where"+where);
                                    //                                            List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                                    //                                            HashMap where1 = new HashMap();
                                    //                                            where1=(HashMap)lists.get(0);
                                    //                                            a= CommonUtil.convertObjToInt(where1.get("CNT"));
                                    //                                            if(a==b) {
                                        //                                                token+=tk;
                                        //                                                a=1;
                                        //                                                tkn=true;
                                        //                                                break;
                                        //                                            }
                                    //                                            else{
                                        //                                                tk=tk+1;
                                        //                                                int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                                        //                                                if(tk>ten) {
                                            //                                                    ClientUtil.displayAlert("No tokens are available.");
                                            //                                                    btnCancelActionPerformed(null);
                                            //                                                    return;
                                            //                                                }
                                        //                                                a=0;
                                        //                                            }
                                    //                                        }
                                //                                    }
                            //                                }
                        //                                else{
                            //                                    token=CommonUtil.convertObjToStr(tokenIssue1.get("SERIES_NO"));
                            //                                    int t= CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_START_NO"));
                            //                                    int a=0;
                            //                                    int b=0;
                            //                                    while(a==b) {
                                //                                        HashMap where = new HashMap();
                                //                                        where.put("SERIES_NO",token);
                                //                                        where.put("TOKEN_NO",new Long(t));
                                //                                        where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                                //                                        where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                                //                                        System.out.println("@@where"+where);
                                //                                        List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                                //                                        HashMap where1 = new HashMap();
                                //                                        where1=(HashMap)lists.get(0);
                                //                                        a= CommonUtil.convertObjToInt(where1.get("CNT"));
                                //                                        if(a==b) {
                                    //                                            token+=t;
                                    //                                            a=1;
                                    //                                            tkn=true;
                                    //                                            break;
                                    //                                        }
                                //                                        else{
                                    //                                            t=t+1;
                                    //                                            int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                                    //                                            if(t>ten) {
                                        //                                                ClientUtil.displayAlert("No tokens are available.");
                                        //                                                btnCancelActionPerformed(null);
                                        //                                                return;
                                        //                                            }
                                    //                                            a=0;
                                    //                                        }
                                //                                    }
                            //
                            //                                }
                        //
                        //                            }
                    //
                    //
                    //                        }
                //
                //
                //                        else {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                //                        if(tkn==false) {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                //                    txtTokenNo.setText(token);
//                cboInstrumentType.setEnabled(true);
//                //                    txtInstrumentNo1.setEditable(true);
//                txtInstrumentNo1.setEnabled(true);
//                //                    txtInstrumentNo2.setEditable(true);
//                txtInstrumentNo2.setEnabled(true);
//                tdtInstrumentDate.setEnabled(true);
//                txtAmount.setEnabled(true);
//                txtPanNo.setText("");
//                btnPanNo.setEnabled(false);
                if(observable.getProdType().equals("TD")){
                    txtParticulars.setText("");
                    txtAmount.setText("");
                    if(depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")){
                        txtParticulars.setEnabled(true);
                    }else{
                        txtParticulars.setEnabled(false);
                    }
                }else{
                    txtParticulars.setText("To ");
                    txtParticulars.setEnabled(true);
                    //                        txtParticulars.setEditable(true);
                }
                if (designation.equals("Teller") && observable.getProdType().equals("OA")){
                    //                    txtTokenNo.setEditable(false);
                 //   txtTokenNo.setEnabled(false);
                } else{
                    //                        txtTokenNo.setEditable(true);
                  //  txtTokenNo.setEnabled(true);
                }
            }
        }
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(!prodType.equals("") && prodType.equals("GL")){
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD",observable.getTxtAccHd());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if(head!=null && head.size()>0){
                acctMap = (HashMap)head.get(0);
                if (transCommonUI!=null){
                    transCommonUI.dispose();
                    transCommonUI = null;
                }
                rdoTransactionType_Credit.setSelected(false);
                if(!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")){
                    if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true){
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true){
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    }else if(acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true){
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }else if(acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true){
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                }else{
                    btnCurrencyInfo.setVisible(false);
                    btnCurrencyInfo.setEnabled(false);
                }
            }
        }else{
            btnCurrencyInfo.setVisible(false);
            btnCurrencyInfo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoTransactionType_DebitActionPerformed

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        if (cboProdId.getSelectedIndex() > 0) {
            clearProdFields();
            //Added BY Suresh
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
            btnAccNo.setEnabled(true);
            observable.setCboProdId((String) cboProdId.getSelectedItem());
            if( observable.getCboProdId().length() > 0){

                //When the selected Product Id is not empty string
                observable.setAccountHead();
//                txtAccHdId.setText(observable.getTxtAccHd());
//                lblAccHdDesc.setText(observable.getLblAccHdDesc());
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
                    observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
                    this.setRadioButtons();
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
                        clearProdFields();
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
                    btnViewTermLoanDetails.setVisible(true);
                }else{
                    btnViewTermLoanDetails.setVisible(false);
                }
            }else{
                depBehavesLike = "";
                depPartialWithDrawalAllowed = "";
                btnViewTermLoanDetails.setVisible(true);
            }
        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void tblTransListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransListMouseClicked
//        // Add your handling code here:
//        if(observable.getOperation()!=ClientConstants.ACTIONTYPE_DELETE &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_REJECT &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_VIEW) {
//            panTransferEnableDisable(true);
//            cboProductType.setEnabled(false);
//            cboProductID.setEnabled(false);
//            txtAccountHeadValue.setEnabled(false);
//            btnAccountHead.setEnabled(false);
//
//        }
//        alreadyExistDeposit = true;
//        reconcilebtnDisable = true;
//        updationTransfer();
//        if(observable.getOperation()!=ClientConstants.ACTIONTYPE_DELETE &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_REJECT &&
//            observable.getOperation()!=ClientConstants.ACTIONTYPE_VIEW) {
//            isRowClicked = true;
//            cboProductType.setEnabled(false);
//            cboProductID.setEnabled(false);
//            txtAccountHeadValue.setEnabled(false);
//            btnAccountHead.setEnabled(false);
//            tdtValueDt.setEnabled(true);
//        }
//        if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT ||
//            observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
//            observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT){
//            txtAccountNo.setEnabled(false);
//        }
//        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
//        if (prodType.equals("TD") && flagDepLink == true) {
//            btnAccountNo.setEnabled(false);
//            btnTransDelete.setEnabled(false);
//            cboInstrumentType.setEnabled(false);
//            txtParticulars.setEnabled(false);
//        }
//        reconcilebtnDisable = false;
//        if((observable.getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE
//            ||observable.getOperation()==ClientConstants.ACTIONTYPE_EXCEPTION) && prodType.equals("OA") && observable.getTransType().equals(CommonConstants.DEBIT)){
//        String act_num=observable.getAccountNo();
//        HashMap inputMap = new HashMap();
//        double avbal=0.0;
//        HashMap balMap = new HashMap();
//        balMap=getAvailableAndShadowBal();
//        inputMap.put("ACCOUNTNO",act_num);
//        double tblSumAmt=0.0;
//        for(int i=0;i<tblTransList.getRowCount(); i++){
//            String tblActNum=CommonUtil.convertObjToStr(tblTransList.getValueAt(i,0));
//            if(act_num.equalsIgnoreCase(tblActNum) && act_num!=null){
//                double tblAmt=CommonUtil.convertObjToDouble(tblTransList.getValueAt(i, 3)).doubleValue();
//                tblSumAmt+=tblAmt;
//            }
//        }
//        boolean  cont = checkForMinAmt(inputMap,0,0,CommonUtil.convertObjToDouble(balMap.get("AV_BAL")).doubleValue(),0,tblSumAmt);
//        if(!cont){
//            btnAuthorize.setEnabled(false);
//            btnRejection.setEnabled(false);
//            btnException.setEnabled(false);
//            return;
//        }
//        }
//        //Added By Suresh
//        if(!prodType.equals("GL")){
//            String act_num=observable.getAccountNo();
//            setCustHouseName(act_num);
//        }else{
//            if (txtAccountHeadValue.getText().length() > 0 || observable.getAccountHeadId().length() > 0) {
//                observable.getAccountHead();
//                this.lblAccountHeadDescValue.setText(observable.getAccountHeadDesc());
//            }
//        }

    }//GEN-LAST:event_tblTransListMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnSaveNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveNewActionPerformed
         public ArrayList setOrgOrRespDetails() {
             HashMap hmap=new HashMap();
             ArrayList orgOrRespList=new ArrayList();
        
                hmap.put("OrgOrRespAdviceDt",getOrgOrRespAdviceDt());
                hmap.put("ADVICE_NO",getOrgOrRespAdviceNo());
                hmap.put("OrgOrRespAmout",getOrgOrRespAmout());
                hmap.put("OrgOrRespBranchId",getOrgOrRespBranchId());
                hmap.put("OrgOrRespBranchName",getOrgOrRespBranchName());
                hmap.put("OrgBranchName",getOrgBranchName());
                hmap.put("ORG_BRANCH",getOrgBranch());
                hmap.put("OrgOrRespCategory",getOrgOrRespCategory());
                hmap.put("OrgOrRespDetails",getOrgOrRespDetails());
                hmap.put("OrgOrRespTransType",getOrgOrRespTransType());
                orgOrRespList.add(hmap);
            
             return orgOrRespList;
         }
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
            AuditEntryUI gui = new AuditEntryUI();
            jf.getContentPane().add(gui);
            jf.setSize(536, 566);
            jf.show();
            gui.show();
        }
        
        /**
         * Getter for property viewType.
         * @return Value of property viewType.
         */
        public int getViewType() {
            return viewType;
        }
        
        /**
         * Setter for property viewType.
         * @param viewType New value of property viewType.
         */
        public void setViewType(int viewType) {
            this.viewType = viewType;
        }
        
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

    public double getOrgOrRespAmout() {
        return orgOrRespAmout;
    }

    public void setOrgOrRespAmout(double orgOrRespAmout) {
        this.orgOrRespAmout = orgOrRespAmout;
    }

    public String getOrgOrRespBranchName() {
        return orgOrRespBranchName;
    }

    public void setOrgOrRespBranchName(String orgOrRespBranchName) {
        this.orgOrRespBranchName = orgOrRespBranchName;
    }

    public String getOrgOrRespBranchId() {
        return orgOrRespBranchId;
    }

    public void setOrgOrRespBranchId(String orgOrRespBranchId) {
        this.orgOrRespBranchId = orgOrRespBranchId;
    }

    public String getOrgOrRespCategory() {
        return orgOrRespCategory;
    }

    public void setOrgOrRespCategory(String orgOrRespCategory) {
        this.orgOrRespCategory = orgOrRespCategory;
    }

    public Date getOrgOrRespAdviceDt() {
        return orgOrRespAdviceDt;
    }

    public void setOrgOrRespAdviceDt(Date orgOrRespAdviceDt) {
        this.orgOrRespAdviceDt = orgOrRespAdviceDt;
    }

    public String getOrgOrRespAdviceNo() {
        return orgOrRespAdviceNo;
    }

    public void setOrgOrRespAdviceNo(String orgOrRespAdviceNo) {
        this.orgOrRespAdviceNo = orgOrRespAdviceNo;
    }

    public String getOrgOrRespTransType() {
        return orgOrRespTransType;
    }

    public void setOrgOrRespTransType(String orgOrRespTransType) {
        this.orgOrRespTransType = orgOrRespTransType;
    }

    public String getOrgOrRespDetails() {
        return orgOrRespDetails;
    }

    public void setOrgOrRespDetails(String orgOrRespDetails) {
        this.orgOrRespDetails = orgOrRespDetails;
    }

    public String getOrgBranch() {
        return orgBranch;
    }

    public void setOrgBranch(String orgBranch) {
        this.orgBranch = orgBranch;
    }

    public String getOrgBranchName() {
        return orgBranchName;
    }

    public void setOrgBranchName(String orgBranchName) {
        this.orgBranchName = orgBranchName;
    }
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCurrencyInfo;
    private com.see.truetransact.uicomponent.CButton btnDebitDetails;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveNew;
    private com.see.truetransact.uicomponent.CButton btnVer;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewTermLoanDetails;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAmt;
    private com.see.truetransact.uicomponent.CPanel panAuditDetails;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panCashTransaction;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransInfo;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Debit;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private com.see.truetransact.uicomponent.CTable tblTransList;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    // End of variables declaration//GEN-END:variables
}
