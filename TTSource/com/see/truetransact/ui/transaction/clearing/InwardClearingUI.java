/*
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingUI.java
 *
 * Created on January 6, 2004, 11:05 AM
 */

package com.see.truetransact.ui.transaction.clearing;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Logger;

import javax.swing.UIManager;
import javax.swing.JFrame;
import java.util.Date;
/**
 *
 * @author  rahul
 */
public class InwardClearingUI extends CInternalFrame implements java.util.Observer {//, UIMandatoryField{
    
    //    private final InwardClearingRB resourceBundle = new InwardClearingRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.clearing.InwardClearingRB", ProxyParameters.LANGUAGE);
                                                                                   
    private HashMap mandatoryMap;
    InwardClearingOB observable;
    final int EDIT=0, DELETE=1, ACCNUMBER=2,AUTHORIZE=3, BANKCODE=7,BRANCH_CODE=16, VIEW = 4;
    int viewType=-1;
    boolean isFilled = false;
    String view="";
    //    final String BRANCHID = TrueTransactMain.BRANCH_ID;
    //    final String BRANCHID = getSelectedBranchID();
    int callBank = 0;
    private TransDetailsUI transDetails = null;
    //Logger
    private final static Logger log = Logger.getLogger(InwardClearingUI.class);
    private Date currDt = null;
    /** Creates new form InwardClearingUI */
    public InwardClearingUI(String selectedBranch) {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setSelectedBranchID(selectedBranch);
        initSetup();
    }
    
    private void initSetup(){
        //        btnShadowCredit.setVisible(false);
        //        btnShadowDebit.setVisible(false);
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();    // Fill all the combo boxes...
        setMaxLenths(); // To set the Numeric Validation and the Maximum length of the Text fields...
        
        observable.resetStatus();   // to reset the status
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();   // Enables/Disables the necessary buttons and menu items...
        enableDisableButtons(); // To disable the buttons(folder) in the Starting...
        observable.resetForm(); // To reset all the fields in UI...
        observable.resetLable();    // To reset all the Lables in UI...
        setHelpMessage();
        setInvisible(); // To set Currency and Converted Amount to Invisible...
        transDetails = new TransDetailsUI(panInwardTransDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInwardClearing);
        observable.ttNotifyObservers();
        log.info("The Constructor is Working fine...");
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        try {
            observable = new InwardClearingOB(getSelectedBranchID());
            observable.addObserver(this);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
/* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
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
        btnView.setName("btnView");
        panInwardTransDetails.setName("panInwardTransDetails");
        panAcctHd.setName("panAcctHd");
        //        cboBankCode.setName("cboBankCode");
        txtBankCodeID.setName("txtBankCodeID");
        //        cboBranchCode.setName("cboBranchCode");
        txtBranchCodeID.setName("txtBranchCodeID");
        cboClearingType.setName("cboClearingType");
        cboCurrency.setName("cboCurrency");
        cboInstrumentTypeID.setName("cboInstrumentTypeID");
        cboProdId.setName("cboProdId");
        cboProductType.setName("cboProductType");
        cboScheduleNo.setName("cboScheduleNo");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadDesc.setName("lblAccountHeadDesc");
        lblAccountHeadProdId.setName("lblAccountHeadProdId");
        lblAccountNumber.setName("lblAccountNumber");
        lblAccountNumberName.setName("lblAccountNumberName");
        lblAmount.setName("lblAmount");
        lblBankCode.setName("lblBankCode");
        lblBookedAmount.setName("lblBookedAmount");
        lblBookedAmount_2.setName("lblBookedAmount_2");
        lblBookedInstrument.setName("lblBookedInstrument");
        lblBookedInstrument_2.setName("lblBookedInstrument_2");
        lblBranchCode.setName("lblBranchCode");
        lblClearingDate.setName("lblClearingDate");
        lblClearingDate_2.setName("lblClearingDate_2");
        lblClearingType.setName("lblClearingType");
        lblCovtAmount.setName("lblCovtAmount");
        lblCurrency.setName("lblCurrency");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentNumber.setName("lblInstrumentNumber");
        lblInstrumentTypeID.setName("lblInstrumentTypeID");
        lblMsg.setName("lblMsg");
        lblPayeeName.setName("lblPayeeName");
        lblProdCurrency.setName("lblProdCurrency");
        lblProdCurrency_2.setName("lblProdCurrency_2");
        lblProdId.setName("lblProdId");
        lblProductType.setName("lblProductType");
        lblScheduleNumber.setName("lblScheduleNumber");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblTotalAmt.setName("lblTotalAmt");
        lblTotalAmt_2.setName("lblTotalAmt_2");
        lblTotalInstrument.setName("lblTotalInstrument");
        lblTransactionDate.setName("lblTransactionDate");
        lblTransactionDate_2.setName("lblTransactionDate_2");
        lblTransactionId.setName("lblTransactionId");
        lblTransactionId_2.setName("lblTransactionId_2");
        mbrMain.setName("mbrMain");
        panClearingData.setName("panClearingData");
        panInstrumentNumber.setName("panInstrumentNumber");
        panInwardClearing.setName("panInwardClearing");
        panProdData.setName("panProdData");
        panProductData.setName("panProductData");
        panStatus.setName("panStatus");
        tdtInstrumentDate.setName("tdtInstrumentDate");
        txtAccountNumber.setName("txtAccountNumber");
        txtAmount.setName("txtAmount");
        txtCovtAmount.setName("txtCovtAmount");
        txtInstrumentNo1.setName("txtInstrumentNo1");
        txtInstrumentNo2.setName("txtInstrumentNo2");
        txtPayeeName.setName("txtPayeeName");
        //        txtScheduleNumber.setName("txtScheduleNumber");
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ((javax.swing.border.TitledBorder)panClearingData.getBorder()).setTitle(resourceBundle.getString("panClearingData"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblCovtAmount.setText(resourceBundle.getString("lblCovtAmount"));
        lblTransactionId_2.setText(resourceBundle.getString("lblTransactionId_2"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblInstrumentTypeID.setText(resourceBundle.getString("lblInstrumentTypeID"));
        lblTotalAmt_2.setText(resourceBundle.getString("lblTotalAmt_2"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblAccountNumberName.setText(resourceBundle.getString("lblAccountNumberName"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblCurrency.setText(resourceBundle.getString("lblCurrency"));
        lblInstrumentNumber.setText(resourceBundle.getString("lblInstrumentNumber"));
        lblProdCurrency.setText(resourceBundle.getString("lblProdCurrency"));
        ((javax.swing.border.TitledBorder)panProdData.getBorder()).setTitle(resourceBundle.getString("panProdData"));
        lblPayeeName.setText(resourceBundle.getString("lblPayeeName"));
        ((javax.swing.border.TitledBorder)panProductData.getBorder()).setTitle(resourceBundle.getString("panProductData"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblBookedAmount_2.setText(resourceBundle.getString("lblBookedAmount_2"));
        lblTransactionDate_2.setText(resourceBundle.getString("lblTransactionDate_2"));
        lblAccountHeadProdId.setText(resourceBundle.getString("lblAccountHeadProdId"));
        lblAccountHeadDesc.setText(resourceBundle.getString("lblAccountHeadDesc"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblTotalAmt.setText(resourceBundle.getString("lblTotalAmt"));
        lblTotalInstrument.setText(resourceBundle.getString("lblTotalInstrument"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblTotalInstrument_2.setText(resourceBundle.getString("lblTotalInstrument_2"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblBookedInstrument.setText(resourceBundle.getString("lblBookedInstrument"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblClearingDate.setText(resourceBundle.getString("lblClearingDate"));
        lblBookedInstrument_2.setText(resourceBundle.getString("lblBookedInstrument_2"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblBookedAmount.setText(resourceBundle.getString("lblBookedAmount"));
        lblTransactionDate.setText(resourceBundle.getString("lblTransactionDate"));
        lblScheduleNumber.setText(resourceBundle.getString("lblScheduleNumber"));
        lblProdCurrency_2.setText(resourceBundle.getString("lblProdCurrency_2"));
        lblClearingDate_2.setText(resourceBundle.getString("lblClearingDate_2"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblTransactionId.setText(resourceBundle.getString("lblTransactionId"));
    }
    /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("txtScheduleNumber", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtPayeeName", new Boolean(true));
        mandatoryMap.put("cboInstrumentTypeID", new Boolean(true));
        //        mandatoryMap.put("cboBankCode", new Boolean(true));
        mandatoryMap.put("txtBankCodeID",new Boolean(false));
        //        mandatoryMap.put("cboBranchCode", new Boolean(true));
        mandatoryMap.put("txtBranchCodeID",new Boolean(false));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtCovtAmount", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
       /* Auto Generated Method - update()
       This method called by Observable. It updates the UI with
       Observable's data. If needed add/Remove RadioButtons
       method need to be added.*/
    public void update(Observable observed, Object arg) {
        //Product Type
        this.cboProductType.setSelectedItem(
        ((ComboBoxModel)this.cboProductType.getModel()).getDataForKey(observable.getCboProductType())
        );
        cboProdId.setSelectedItem(observable.getCboProdId());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        cboClearingType.setSelectedItem(observable.getCboClearingType());
        //        this.cboScheduleNo.setselectedItem(((ComboBoxModel)this.cboScheduleNo.getModel()).getDataForKey(observable.getCboScheduleNo()));
        //         this.cboScheduleNo.setSelectedItem(((ComboBoxModel)this.cboScheduleNo.getModel()).getDataForKey(observable.getCboScheduleNo()));
        cboScheduleNo.setSelectedItem(observable.getCboScheduleNo());
        
        //        txtScheduleNumber.setText(observable.getTxtScheduleNumber());
        txtInstrumentNo1.setText(observable.getTxtInstrumentNo1());
        txtInstrumentNo2.setText(observable.getTxtInstrumentNo2());
        tdtInstrumentDate.setDateValue(observable.getTdtInstrumentDate());
        txtAmount.setText(observable.getTxtAmount());
        txtPayeeName.setText(observable.getTxtPayeeName());
        //        cboBankCode.setSelectedItem(observable.getCboBankCode());
        txtBankCodeID.setText(observable.getTxtBankCodeID());
        //__ Testing Code...
        //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
        if((observable.getActionType() != ClientConstants.ACTIONTYPE_NEW)
        && (observable.getActionType() != ClientConstants.ACTIONTYPE_CANCEL)){
            //            cboBranchCode.setModel(observable.getCbmBranchCode());
            
        }
        cboScheduleNo.setModel(observable.getCbmScheduleNo());
        //        cboBranchCode.setSelectedItem(observable.getCboBranchCode());
        txtBranchCodeID.setText(observable.getTxtBranchCodeID());
        cboInstrumentTypeID.setSelectedItem(observable.getCboInstrumentTypeID());
        //        cboCurrency.setSelectedItem(observable.getCboCurrency());
        txtCovtAmount.setText(observable.getTxtCovtAmount());
        
        /**
         * UNGENERATED CODE
         */
        lblAccountHeadProdId.setText(observable.getLblAccountHeadProdId());
        lblAccountHeadDesc.setText(observable.getLblAccountHeadDesc());
        //        lblProdCurrency_2.setText(observable.getLblProdCurrency());
        //To set the  Name of the Account Holder...
        lblAccountNumberName.setText(observable.getLblAccountNumberName());
        //To set the  Transaction ID and Transaction Date...
        lblTransactionId_2.setText(observable.getLblTransactionId());
        lblTransactionDate_2.setText(observable.getLblTransactionDate());
        
        //To set the  Clearing Date...
        lblClearingDate_2.setText(observable.getLblClearingDate());
        
        lblTotalInstrument_2.setText(observable.getLblTotalInstrument());
        lblTotalAmt_2.setText(observable.getLblTotalAmount());
        /**Warning Message*/
        lblCountWarning.setText(observable.getLblCountWarning());
        
        lblBookedInstrument_2.setText(observable.getLblBookedInstrument());
        lblBookedAmount_2.setText(observable.getLblBookedAmount());
        
        lblStatus.setText(observable.getLblStatus());
    }
       /* Auto Generated Method - updateOBFields()
       This method called by Save option of UI.
       It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProductType((String)((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());
        //        observable.setTxtScheduleNumber(txtScheduleNumber.getText());
        observable.setCboScheduleNo((String)cboScheduleNo.getSelectedItem());
        observable.setTxtInstrumentNo1(txtInstrumentNo1.getText());
        observable.setTxtInstrumentNo2(txtInstrumentNo2.getText());
        observable.setTdtInstrumentDate(tdtInstrumentDate.getDateValue());
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtPayeeName(txtPayeeName.getText());
        //        observable.setCboBankCode((String) cboBankCode.getSelectedItem());
        observable.setTxtBankCodeID(txtBankCodeID.getText());
        //        observable.setCboBranchCode((String) cboBranchCode.getSelectedItem());
        observable.setTxtBranchCodeID(txtBranchCodeID.getText());
        observable.setCboInstrumentTypeID((String) cboInstrumentTypeID.getSelectedItem());
        //        observable.setCboCurrency((String) cboCurrency.getSelectedItem());
        observable.setTxtCovtAmount(txtCovtAmount.getText());
        /**
         * UNGENERATED CODE
         */
        //To set the Value of Account Head, Account Desc, Prod Currency ...
        //        observable.setLblAccountHeadProdId(lblAccountHeadProdId.getText());
        //        observable.setLblAccountHeadDesc(lblAccountHeadDesc.getText());
        //        observable.setLblProdCurrency(lblProdCurrency_2.getText());
        //To set the  Name of the Account Holder...
        observable.setLblAccountNumberName(lblAccountNumberName.getText());
        //To set the  Transaction ID and Transaction Date...
        observable.setLblTransactionId(lblTransactionId_2.getText());
        observable.setLblTransactionDate(lblTransactionDate_2.getText());
        //To set the  Clearing Date...
        observable.setLblClearingDate(lblClearingDate_2.getText());
        observable.setLblTotalInstruments(lblTotalInstrument_2.getText());
        observable.setLblTotalAmount(lblTotalAmt_2.getText());
        observable.setLblBookedInstrument(lblBookedInstrument_2.getText());
    }
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        InwardClearingMRB objMandatoryRB = new InwardClearingMRB();
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboScheduleNo.setHelpMessage(lblMsg,objMandatoryRB.getString("cboScheduleNo"));
        txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
        cboClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingType"));
        //        txtScheduleNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtScheduleNumber"));
        txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtPayeeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeName"));
        cboInstrumentTypeID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentTypeID"));
        //        cboBankCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBankCode"));
        txtBankCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankCodeID"));
        txtBranchCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCodeID"));
        //        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        txtCovtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCovtAmount"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
    }
    
    
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtInstrumentNo1.setAllowNumber(true);
        txtInstrumentNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtInstrumentNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        
        //        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(14,2));
        
        txtPayeeName.setMaxLength(64);
        
        txtCovtAmount.setMaxLength(16);
        txtCovtAmount.setValidation(new NumericValidation());
        txtBankCodeID.setValidation(new NumericValidation(6,0));
        txtBankCodeID.setAllowAll(false);
        txtBankCodeID.setAllowNumber(true);
        txtBranchCodeID.setValidation(new NumericValidation(6,0));
        txtBranchCodeID.setAllowAll(false);
        txtBranchCodeID.setAllowNumber(true);
        
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //        cboProdId.setModel(observable.getCbmProdId());
        cboClearingType.setModel(observable.getCbmClearingType());
        cboInstrumentTypeID.setModel(observable.getCbmInstrumentTypeID());
        //        cboBankCode.setModel(observable.getCbmBankCode());
        //        cboCurrency.setModel(observable.getCbmCurrency());
        this.cboProductType.setModel(observable.getCbmProductType());
        cboScheduleNo.setModel(observable.getCbmScheduleNo());
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView1.setEnabled(!btnView1.isEnabled());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panInwardClearing = new com.see.truetransact.uicomponent.CPanel();
        panProductData = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panAcctHd = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHeadProdId = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblAccountNumberName = new com.see.truetransact.uicomponent.CLabel();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        cboClearingType = new com.see.truetransact.uicomponent.CComboBox();
        lblScheduleNumber = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentNumber = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNumber = new com.see.truetransact.uicomponent.CPanel();
        txtInstrumentNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPayeeName = new com.see.truetransact.uicomponent.CLabel();
        txtPayeeName = new com.see.truetransact.uicomponent.CTextField();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentTypeID = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentTypeID = new com.see.truetransact.uicomponent.CComboBox();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblProdCurrency_2 = new com.see.truetransact.uicomponent.CLabel();
        lblCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblCovtAmount = new com.see.truetransact.uicomponent.CLabel();
        txtCovtAmount = new com.see.truetransact.uicomponent.CTextField();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboScheduleNo = new com.see.truetransact.uicomponent.CComboBox();
        btnBankCodeID = new com.see.truetransact.uicomponent.CButton();
        btnBranchCodeID = new com.see.truetransact.uicomponent.CButton();
        txtBankCodeID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchCodeID = new com.see.truetransact.uicomponent.CTextField();
        panProdData = new com.see.truetransact.uicomponent.CPanel();
        lblTransactionId = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionId_2 = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDate = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDate_2 = new com.see.truetransact.uicomponent.CLabel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        panClearingData = new com.see.truetransact.uicomponent.CPanel();
        lblClearingDate = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDate_2 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInstrument = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInstrument_2 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt_2 = new com.see.truetransact.uicomponent.CLabel();
        lblBookedInstrument = new com.see.truetransact.uicomponent.CLabel();
        lblBookedInstrument_2 = new com.see.truetransact.uicomponent.CLabel();
        lblBookedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblBookedAmount_2 = new com.see.truetransact.uicomponent.CLabel();
        lblCountWarning = new com.see.truetransact.uicomponent.CLabel();
        panInwardTransDetails = new com.see.truetransact.uicomponent.CPanel();
        tbrHead = new javax.swing.JToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(550, 590));
        setPreferredSize(new java.awt.Dimension(550, 590));

        panInwardClearing.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInwardClearing.setMinimumSize(new java.awt.Dimension(465, 400));
        panInwardClearing.setPreferredSize(new java.awt.Dimension(465, 400));
        panInwardClearing.setLayout(new java.awt.GridBagLayout());

        panProductData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductData.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        cboProdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboProdId, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblAccountHead, gridBagConstraints);

        panAcctHd.setLayout(new java.awt.GridBagLayout());

        lblAccountHeadProdId.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadProdId.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadProdId.setPreferredSize(new java.awt.Dimension(20, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        panAcctHd.add(lblAccountHeadProdId, gridBagConstraints);

        lblAccountHeadDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDesc.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDesc.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panAcctHd.add(lblAccountHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panAcctHd, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblAccountNumber, gridBagConstraints);

        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setToolTipText("Account Number");
        btnAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        btnAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductData.add(btnAccountNumber, gridBagConstraints);

        lblAccountNumberName.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountNumberName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountNumberName.setMaximumSize(new java.awt.Dimension(100, 50));
        lblAccountNumberName.setMinimumSize(new java.awt.Dimension(100, 15));
        lblAccountNumberName.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 0);
        panProductData.add(lblAccountNumberName, gridBagConstraints);

        lblClearingType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblClearingType, gridBagConstraints);

        cboClearingType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClearingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClearingTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboClearingType, gridBagConstraints);

        lblScheduleNumber.setText("Schedule Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblScheduleNumber, gridBagConstraints);

        lblInstrumentNumber.setText("Instrument Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblInstrumentNumber, gridBagConstraints);

        panInstrumentNumber.setLayout(new java.awt.GridBagLayout());

        txtInstrumentNo1.setMinimumSize(new java.awt.Dimension(65, 21));
        txtInstrumentNo1.setPreferredSize(new java.awt.Dimension(65, 21));
        panInstrumentNumber.add(txtInstrumentNo1, new java.awt.GridBagConstraints());

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(80, 21));
        txtInstrumentNo2.setPreferredSize(new java.awt.Dimension(80, 21));
        txtInstrumentNo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentNo2FocusLost(evt);
            }
        });
        panInstrumentNumber.add(txtInstrumentNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panInstrumentNumber, gridBagConstraints);

        lblInstrumentDate.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblInstrumentDate, gridBagConstraints);

        tdtInstrumentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(tdtInstrumentDate, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblAmount, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtAmount, gridBagConstraints);

        lblPayeeName.setText("Payee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblPayeeName, gridBagConstraints);

        txtPayeeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtPayeeName, gridBagConstraints);

        lblBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblBankCode, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblBranchCode, gridBagConstraints);

        lblInstrumentTypeID.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblInstrumentTypeID, gridBagConstraints);

        cboInstrumentTypeID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboInstrumentTypeID, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblProdCurrency, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblProdCurrency_2, gridBagConstraints);

        lblCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblCurrency, gridBagConstraints);

        cboCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboCurrency, gridBagConstraints);

        lblCovtAmount.setText("Converted Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblCovtAmount, gridBagConstraints);

        txtCovtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtCovtAmount, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(115);
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        cboProductType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboProductType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(lblProductType, gridBagConstraints);

        cboScheduleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        cboScheduleNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboScheduleNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(cboScheduleNo, gridBagConstraints);

        btnBankCodeID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCodeID.setToolTipText("Account Number");
        btnBankCodeID.setMaximumSize(new java.awt.Dimension(51, 21));
        btnBankCodeID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCodeID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCodeID.setEnabled(false);
        btnBankCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductData.add(btnBankCodeID, gridBagConstraints);

        btnBranchCodeID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCodeID.setToolTipText("Account Number");
        btnBranchCodeID.setMaximumSize(new java.awt.Dimension(51, 21));
        btnBranchCodeID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBranchCodeID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCodeID.setEnabled(false);
        btnBranchCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductData.add(btnBranchCodeID, gridBagConstraints);

        txtBankCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBankCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtBankCodeID, gridBagConstraints);

        txtBranchCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBranchCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductData.add(txtBranchCodeID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearing.add(panProductData, gridBagConstraints);

        panProdData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProdData.setLayout(new java.awt.GridBagLayout());

        lblTransactionId.setText("Transaction Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdData.add(lblTransactionId, gridBagConstraints);

        lblTransactionId_2.setMinimumSize(new java.awt.Dimension(50, 15));
        lblTransactionId_2.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdData.add(lblTransactionId_2, gridBagConstraints);

        lblTransactionDate.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdData.add(lblTransactionDate, gridBagConstraints);

        lblTransactionDate_2.setMinimumSize(new java.awt.Dimension(110, 15));
        lblTransactionDate_2.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdData.add(lblTransactionDate_2, gridBagConstraints);

        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(63, 23));
        btnView.setMinimumSize(new java.awt.Dimension(63, 23));
        btnView.setPreferredSize(new java.awt.Dimension(63, 23));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdData.add(btnView, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 1, 2);
        panInwardClearing.add(panProdData, gridBagConstraints);

        panClearingData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panClearingData.setLayout(new java.awt.GridBagLayout());

        lblClearingDate.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblClearingDate, gridBagConstraints);

        lblClearingDate_2.setMinimumSize(new java.awt.Dimension(75, 15));
        lblClearingDate_2.setPreferredSize(new java.awt.Dimension(75, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblClearingDate_2, gridBagConstraints);

        lblTotalInstrument.setText("Total No. of Instrument");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblTotalInstrument, gridBagConstraints);

        lblTotalInstrument_2.setMinimumSize(new java.awt.Dimension(50, 15));
        lblTotalInstrument_2.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblTotalInstrument_2, gridBagConstraints);

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblTotalAmt, gridBagConstraints);

        lblTotalAmt_2.setMinimumSize(new java.awt.Dimension(50, 15));
        lblTotalAmt_2.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblTotalAmt_2, gridBagConstraints);

        lblBookedInstrument.setText("Booked Instrument");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblBookedInstrument, gridBagConstraints);

        lblBookedInstrument_2.setMinimumSize(new java.awt.Dimension(50, 15));
        lblBookedInstrument_2.setPreferredSize(new java.awt.Dimension(50, 15));
        lblBookedInstrument_2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblBookedInstrument_2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblBookedInstrument_2, gridBagConstraints);

        lblBookedAmount.setText("Booked Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblBookedAmount, gridBagConstraints);

        lblBookedAmount_2.setMinimumSize(new java.awt.Dimension(50, 15));
        lblBookedAmount_2.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblBookedAmount_2, gridBagConstraints);

        lblCountWarning.setForeground(new java.awt.Color(0, 0, 204));
        lblCountWarning.setFont(new java.awt.Font("MS Sans Serif", 1, 11)); // NOI18N
        lblCountWarning.setMinimumSize(new java.awt.Dimension(200, 0));
        lblCountWarning.setPreferredSize(new java.awt.Dimension(200, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingData.add(lblCountWarning, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInwardClearing.add(panClearingData, gridBagConstraints);

        panInwardTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInwardTransDetails.setMinimumSize(new java.awt.Dimension(240, 290));
        panInwardTransDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearing.add(panInwardTransDetails, gridBagConstraints);

        getContentPane().add(panInwardClearing, java.awt.BorderLayout.CENTER);

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        tbrHead.add(btnView1);

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace21);

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

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

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
    
    private void cboProductTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductTypeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductTypeFocusLost
    
    private void cboProdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdIdFocusLost
        // TODO add your handling code here:
        //        txtAccountNumber.setText("");
    }//GEN-LAST:event_cboProdIdFocusLost
    
    private void btnAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnAccountNumberFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAccountNumberFocusLost
    
    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        // TODO add your handling code here:
        observable.setProdTypeTO("");
        String ACCOUNTNO = txtAccountNumber.getText();
        if ((!(observable.getProdTypeTO().length()>0)) && ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                txtAccountNumber.setText(observable.getTxtAccountNumber());
                cboProdId.setModel(observable.getCbmProdId());
                cboProdIdActionPerformed(null);
                String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccountNumber.setText("");
                return;
            }
        }
        HashMap hash = new HashMap();
        hash.put("ACCOUNTNO", txtAccountNumber.getText());
        viewType = ACCNUMBER;
        fillData(hash);
        System.out.println("lblAccountNumberName+++"+observable.getLblAccountNumberName());
        if (observable.getLblAccountNumberName()==null || observable.getLblAccountNumberName().length()<=0){
            txtAccountNumber.setText("");
            ClientUtil.displayAlert("Account Number not valied");
            
        }
    }//GEN-LAST:event_txtAccountNumberFocusLost
    
    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        // TODO add your handling code here:
        
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
        
        
    }//GEN-LAST:event_btnView1ActionPerformed
    
    private void txtInstrumentNo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentNo2FocusLost
        // TODO add your handling code here:
        HashMap instChkMap=new HashMap();
        instChkMap.put("INSTRUMENT_NO1",CommonUtil.convertObjToStr(txtInstrumentNo1.getText()));
        instChkMap.put("INSTRUMENT_NO2",CommonUtil.convertObjToStr(txtInstrumentNo2.getText()));
        instChkMap.put("SCHEDULE_NO",CommonUtil.convertObjToStr(cboScheduleNo.getSelectedItem()));
        List lst=ClientUtil.executeQuery("inward.InstrumentAlreadyEntered", instChkMap);
        if(lst!=null && lst.size()>0){
            ClientUtil.displayAlert(" This Instrument is Already entered  in this Schedule");
            txtInstrumentNo1.setText("");
            txtInstrumentNo2.setText("");
            
        }
        //       String inst_type=((String)cboInstrumentTypeID.getSelectedItem());
        //       if(inst_type.equals("ECS")){
        //           HashMap ecsMap = new HashMap();
        //           ecsMap.put("INST1",txtInstrumentNo1.getText());
        //           ecsMap.put("INST2",txtInstrumentNo2.getText());
        //           List ecsStopList=ClientUtil.executeQuery("checkForEcsStopPayment", ecsMap);
        //           if(ecsStopList.size()>0){
        //               int a=CommonUtil.convertObjToInt(ecsStopList.get(0));
        //              if(a>=1){
        //                  ClientUtil.displayAlert("This Ecs Has Been Stopped");
        //                  txtInstrumentNo1.setText("");
        //                  txtInstrumentNo2.setText("");
        //              }
        //           }
        //       }
        
    }//GEN-LAST:event_txtInstrumentNo2FocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(view.equals("view")){
            HashMap ViewMap=new HashMap();
            HashMap where=new HashMap();
            ViewMap.put(CommonConstants.MAP_NAME, "getInwardClearngView");
            if(cboClearingType.getSelectedItem()!=null && !cboClearingType.getSelectedItem().equals(""))
                where.put("CLEARING_TYPE", cboClearingType.getSelectedItem());
            if(cboScheduleNo.getSelectedItem()!=null && (!cboScheduleNo.getSelectedItem().equals("")))
                where.put("SCHEDULE_NO",cboScheduleNo.getSelectedItem());
            where.put("CLEARING_DATE",DateUtil.getDateMMDDYYYY(lblClearingDate_2.getText()));
            
            ViewMap.put(CommonConstants.MAP_WHERE,where);
            
            new ViewAll(this,ViewMap).show();
            ClientUtil.enableDisable(this,false);
            btnCancel.setEnabled(true);
            cboScheduleNo.setEnabled(true);
            cboClearingType.setEnabled(true);
            List lst =null;
            HashMap BouMap=new HashMap();
            String bounceView="getBouncingDetails";
            BouMap.put("INWARD_ID", observable.getLblTransactionId());
            BouMap.put("INITIATED_BRANCH", TrueTransactMain.BANK_ID);
            BouMap.put("TRANS_DT",currDt.clone());
            lst=ClientUtil.executeQuery(bounceView, BouMap);
            if(lst!=null && lst.size()>0){
                String msg="";
                HashMap bouDetMap=new HashMap();
                bouDetMap=(HashMap)lst.get(0);
                msg=msg+"Instrument is Bounced for the Resion of:  "+bouDetMap.get("BOUNCING_REASON")+"\n";
                msg=msg+"Outward Clearing Type:  "+bouDetMap.get("CLEARING_TYPE")+"\n";
                msg=msg+"Outward SCHEDULE_NO:  "+bouDetMap.get("SCHEDULE_NO")+"\n";
                msg=msg+"Return Charge :  "+bouDetMap.get("CHARGE")+"\n";
                //               ClientUtil.displayAlert(msg);
                ClientUtil.showMessageWindow(msg);
                lst=null;
                //               view="";
            }
            
        }else{
            //            btnCancelActionPerformed(null);
            //             btnCancelActionPerformed(null);
            view="view";
            cboScheduleNo.setEnabled(true);
            cboClearingType.setEnabled(true);
            
            
        }
        btnNew.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        btnCancel.setEnabled(true);
        
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void lblBookedInstrument_2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblBookedInstrument_2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblBookedInstrument_2FocusLost
    
    private void txtBranchCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBranchCodeIDActionPerformed
        // TODO add your handling code here:
        String branchCode=txtBranchCodeID.getText();
        
        if(branchCode !=null && branchCode.length()>0){
            observable.setTxtBranchCodeID(txtBranchCodeID.getText());
            HashMap hash=new HashMap();
            hash.put("BANK_CODE",observable.getTxtBankCodeID());
            hash.put("OTHER_BRANCH_CODE",observable.getTxtBranchCodeID());
            List lst =ClientUtil.executeQuery("Outward.getSelectBankBranch", hash);
            if(lst==null || lst.size()==0){
                ClientUtil.displayAlert("Enter the valid Branch Code");
                txtBranchCodeID.setText("");
            }
        }
        
    }//GEN-LAST:event_txtBranchCodeIDActionPerformed
    
    private void txtBankCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankCodeIDActionPerformed
        // TODO add your handling code here:
        String bankCode=txtBankCodeID.getText();
        if(bankCode !=null && bankCode.length()>0){
            observable.setTxtBankCodeID(bankCode);
            HashMap hash=new HashMap();
            hash.put("BANK_CODE",bankCode);
            List lst=ClientUtil.executeQuery("Outward.getSelectBankCode", hash);
            if(lst ==null && lst.size()==0){
                ClientUtil.displayAlert("Enter the Correct BankCode ");
                txtBankCodeID.setText("");
                
            }
            
        }
    }//GEN-LAST:event_txtBankCodeIDActionPerformed
    
    private void btnBranchCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeIDActionPerformed
        // TODO add your handling code here:
        
        viewType=BRANCH_CODE;
        final HashMap viewMap=new HashMap();
        HashMap hashwhere=new HashMap();
        hashwhere.put("BANK_CODE",observable.getTxtBankCodeID());
        viewMap.put(CommonConstants.MAP_WHERE,hashwhere);
        viewMap.put(CommonConstants.MAP_NAME,"Outward.getSelectBankBranch");
        new ViewAll(this,viewMap).show();
    }//GEN-LAST:event_btnBranchCodeIDActionPerformed
    
    private void btnBankCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeIDActionPerformed
        // TODO add your handling code here:
        viewType=BANKCODE;
        final HashMap viewMap=new HashMap();
        viewMap.put(CommonConstants.MAP_NAME,"Outward.getSelectBankCode");
        new ViewAll(this,viewMap).show();
        
        
    }//GEN-LAST:event_btnBankCodeIDActionPerformed
    
    private void cboScheduleNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboScheduleNoActionPerformed
        // TODO add your handling code here:
        if(cboScheduleNo.getSelectedItem()!=null && !cboScheduleNo.getSelectedItem().equals("") ){
            observable.setCboScheduleNo((String)cboScheduleNo.getSelectedItem());
            if(observable.getCboScheduleNo().length()>0){
                //         if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)
                //            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
                this.scheduleNumberChecking();
                observable.getSchudeledate();
                lblClearingDate_2.setText(observable.getLblClearingDate());
            }
        }
    }//GEN-LAST:event_cboScheduleNoActionPerformed
    
    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_txtAmountActionPerformed
    
    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAccountNumberActionPerformed
    
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // Add your handling code here:
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setCbmProdId(new ComboBoxModel(key,value));
        cboProdId.setModel(observable.getCbmProdId());
        String productType = (String)((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected();
        if(productType!=null && productType.length() > 0) {
            try{
                observable.setCboProductType(productType);
                observable.setProdTypeTO(productType);
                //__ To get the Data for the Product-ID depending on the Product-Type..
                observable.getProdIdData();
                System.out.println("productType: " + productType);
                cboProdId.setModel(observable.getCbmProdId());
                if (productType.equals("RM"))
                    lblAccountNumber.setText("Variable Number");
                else
                    lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
            }catch(Exception E){
                System.out.println("Error in cboProductTypeActionPerformed()");
                E.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            observable.setCbmProdId(new ComboBoxModel(key,value));
            
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed
    
    private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
        // Add your handling code here:
        //        ClientUtil.validateLTDate(tdtInstrumentDate);
    }//GEN-LAST:event_tdtInstrumentDateFocusLost
    
    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // Add your handling code here:
        if(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){             
             txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));             
         }else{
             txtAmount.setToolTipText("Zero");
         }
        
    }//GEN-LAST:event_txtAmountFocusLost
        /*
         *observable.setCboBankCode((String) cboBankCode.getSelectedItem());
        //--- If an item is selected from cbo BankCode, set appropriate the Branch Code
        //--- to that Bank Code else set it to nothing
        if(observable.getCboBankCode().length() > 0){
            if(fireBranch != 1){
                observable.getBranchData();
            }
            cboBranchCode.setModel(observable.getCbmBranchCode());
        }*/
    private void cboClearingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClearingTypeActionPerformed
        // Add your handling code here:
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());
        
        //--- If an item is selected from cboClearingTypeID set the Schedule No appropriate
        //--- to that Clearing Type  Id else set it to nothing
        if(observable.getCboClearingType().length() > 0){
            if(view.equals("view")){
                observable.getScheduleNoForView();
            } else{
                observable.getScheduleNoForProd();
            }
            observable.resetClearingDetailsLable();
            //            final String scheduleNo = observable.getScheduleNoForProd();
            lblClearingDate_2.setText(observable.getLblClearingDate());
            lblTotalInstrument_2.setText(observable.getLblTotalInstrument());
            lblTotalAmt_2.setText(observable.getLblTotalAmount());
            lblBookedInstrument_2.setText(observable.getLblBookedInstrument());
            lblBookedAmount_2.setText(observable.getLblBookedAmount());
            lblCountWarning.setText(observable.getLblCountWarning());
            cboScheduleNo.setModel(observable.getCbmScheduleNo());
            
            //            this.setCountWarning("");
            //            observable.ttNotifyObservers();
            //            txtScheduleNumber.setText(scheduleNo);
            //txtScheduleNumberFocusLost(null);
            
            //__ IF the ViewType is not Delete of Authorize, Check for the
            
            //__ Schedule Number...
            //            if(viewType != DELETE && viewType != AUTHORIZE){
            //                this.scheduleNumberChecking();
            //            }
        }else if(observable.getCboClearingType().length() == 0){
            //            observable.setTxtScheduleNumber("");
            //            txtScheduleNumber.setText("");
        }
    }//GEN-LAST:event_cboClearingTypeActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    public void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    private void scheduleNumberChecking() {
        //viewType != DELETE && viewType != AUTHORIZE
        
        boolean value = observable.getClearingDetails();
        lblClearingDate_2.setText(observable.getLblClearingDate());
        lblTotalInstrument_2.setText(observable.getLblTotalInstrument());
        lblTotalAmt_2.setText(observable.getLblTotalAmount());
        lblBookedInstrument_2.setText(observable.getLblBookedInstrument());
        lblBookedAmount_2.setText(observable.getLblBookedAmount());
        lblCountWarning.setText(observable.getLblCountWarning());
        if (!value && viewType != DELETE ){     //&& viewType != AUTHORIZE
            if(observable.getCountWarning() != null && observable.getCountWarning().length() > 0){
                displayAlert(observable.getCountWarning());
                
            }else{
                displayAlert(resourceBundle.getString("existScheduleNo"));
            }
        }
    }
    /*
     * To authorize the tranasaction
     */
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        ///observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        String remark = COptionPane.showInputDialog(this, CommonUtil.convertObjToStr(map.get(CommonConstants.AUTHORIZESTATUS)) + resourceBundle.getString("REMARK"),"");
        map.put("REMARKS", remark);
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblTransactionId_2.getText());
        }
        btnCancelActionPerformed(null);
        observable.setResultStatus();
    }
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            String remark = "";
            
            //__ Is the Task Selected is: Rejection
            if(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)){
                remark = CommonUtil.convertObjToStr(COptionPane.showInputDialog(
                null, null, resourceBundle.getString("TITLE"),
                COptionPane.OK_CANCEL_OPTION, null,
                observable.getBouncingReasons(), resourceBundle.getString("Note")));
            }
            
            
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("ACCOUNT NO", txtAccountNumber.getText());
            authDataMap.put("PRODUCT ID", (String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            authDataMap.put("INWARD ID", lblTransactionId_2.getText());
            authDataMap.put("AUTHORIZE STATUS", CommonUtil.convertObjToStr(observable.getAuthStatus()));
            authDataMap.put("BRANCH_ID", getSelectedBranchID());
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            authDataMap.put("CLEARING TYPE", CommonUtil.convertObjToStr(((ComboBoxModel)this.cboClearingType.getModel()).getKeyForSelected()));//
            authDataMap.put("CLEARING_DATE",DateUtil.getDateMMDDYYYY(lblClearingDate_2.getText()));
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            isFilled = false;
        } else {
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            //            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInwardClearingAuthorizeTOList");
            
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...
            //            if(!isModified()){
            //                setButtonEnableDisable();
            //                btnCancelActionPerformed(null);
            //            }
        }
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.resetProdIdDepLable();
        if( observable.getCboProdId().length() > 0){
            //__ When the selected Product Id is not empty string
            observable.setAccountHead();
            lblAccountHeadProdId.setText(observable.getLblAccountHeadProdId());
            lblAccountHeadDesc.setText(observable.getLblAccountHeadDesc());
            //            lblProdCurrency_2.setText(observable.getLblProdCurrency());
            //            cboCurrency.setSelectedItem(lblProdCurrency_2.getText());
        }else{
            //            observable.resetProdIdDepLable();
            //                observable.setTxtAccountNumber("");
            txtAccountNumber.setText("");
        }
        //        observable.ttNotifyObservers();
    }//GEN-LAST:event_cboProdIdActionPerformed
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // Add your handling code here:
        popUp(ACCNUMBER);
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    //To enable and/or Disable buttons(folder)...
    private void enableDisableBankbtn(boolean var){
        btnBankCodeID.setEnabled(var);
        btnBranchCodeID.setEnabled(var);
    }
    private void enableDisableButtons() {
        btnAccountNumber.setEnabled(!btnAccountNumber.isEnabled());
        
        //        btnShadowCredit.setEnabled(!btnShadowCredit.isEnabled());
        //        btnShadowDebit.setEnabled(!btnShadowDebit.isEnabled());
        
        //        btnPhoto.setEnabled(!btnPhoto.isEnabled());
    }
    
    // called from cancel button, to make all the buttons as disabled...
    private void disableButtons() {
        btnAccountNumber.setEnabled(false);
        btnBankCodeID.setEnabled(false);
        btnBranchCodeID.setEnabled(false);
        //        btnShadowCredit.setEnabled(false);
        //        btnShadowDebit.setEnabled(false);
        
        //        btnPhoto.setEnabled(false);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if( viewType == EDIT){
            super.removeEditLock(lblTransactionId_2.getText());
        }
        
        observable.resetForm();                                      //__ Reset the fields in the UI to null...
        observable.resetLable();                                     //__ Reset the Editable Lables in the UI to null...
        ClientUtil.enableDisable(this, false);                       //__ Disables the panel...
        disableButtons();                                            //__ To Disable the folder buttons in the UI...
        setButtonEnableDisable();        //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL); //__ Sets the Action Type to be performed...
        observable.setStatus();                                      //__ To set the Value of lblStatus...
        
        //__ Make the Screen Closable..
        setModified(false);
        
        viewType = -1;
        isFilled = false;
        
        //__ To Put the Value of the Flag...
        callBank = 0;
        transDetails.setTransDetails(null,null,null);
        // To make the value of the Transaction Id = null...
        //lblTransactionId_2.setText("");
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        view="";
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private boolean checkExpiryDate(){
        java.util.Date expiryDt=DateUtil.getDateMMDDYYYY(transDetails.getExpiryDate());
        if(DateUtil.dateDiff(DateUtil.getDateWithoutMinitues((Date) currDt.clone()),expiryDt)<0){
            int yesno=ClientUtil.confirmationAlert("Limit has Expired Do You Want allow Transaction");
            if(yesno==1)
                return true;
        }
        return false;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        
        String Acno=txtAccountNumber.getText();
        HashMap hashmap=new HashMap();
        hashmap.put("ACNO",Acno);
        List lst1=ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", hashmap);
        if(lst1!=null && lst1.size()>0){
            int a=ClientUtil.confirmationAlert("The Account is Death marked, Do you want to continue?");
            int b=0;
            if(a!=b)
                return;
        }
        if(observable.getProdTypeTO().equals("AD")){
            boolean expirydate=checkExpiryDate();
            if(expirydate)
                return;
        }
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInwardClearing);
        /**To Validate The Schedule No.*/
        if (!observable.scheduleNoExistance()){
            mandatoryMessage = mandatoryMessage+ "\n" +resourceBundle.getString("existScheduleNo");
        }
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
        if(enteredAmt>(avbal-shadowDeb)){
            int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Available Balance,Do you want to continue?");
            int d= 0;
            if(c!=d)
                return;
        }
        /**If Total No of instruments are Less than the No of Instruments Entered
         * for the particular Schedule No...
         */
        //        if (Double.parseDouble(lblTotalInstrument_2.getText()) < Double.parseDouble(lblBookedInstrument_2.getText())) {
        //            mandatoryMessage = mandatoryMessage + "\n" + resourceBundle.getString("TOTALINSTRU");
        //        }
        /**If total Amount is Less Than The Instrument Amount for the Particular Schdule No.*/
        //        if (Double.parseDouble(lblTotalAmt_2.getText()) < Double.parseDouble(lblBookedAmount_2.getText())) {
        //            mandatoryMessage = mandatoryMessage + "\n" + resourceBundle.getString("TOTALAMT");
        //        }
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            observable.doAction();// To perform the necessary operation depending on the Action type...
            
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap= new HashMap();
                ArrayList lst=new ArrayList();
                lst.add("INWARD ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL,lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey(CommonConstants.TRANS_ID)){
                        lockMap.put("INWARD ID", observable.getProxyReturnMap().get(CommonConstants.TRANS_ID));
                    }
                    
                }
                
                if( viewType == EDIT){
                    lockMap.put("INWARD ID", observable.getLblTransactionId());
                }
                setEditLockMap(lockMap);
                setEditLock();
                //                    super.removeEditLock(lblTransactionId_2.getText());
                
                
                observable.resetForm();    //__ Reset the fields in the UI to null...
                observable.resetLable();   //__ Reset the Editable Lables in the UI to null...
                enableDisableButtons();    //__ To disable the buttons(folder) in the UI...
                ClientUtil.enableDisable(this, false);// Disables the panel...
                setButtonEnableDisable();  //__ Enables or Disables the buttons and menu Items depending on their previous state...
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                observable.setResultStatus();  //__ To Reset the Value of lblStatus...
                
                //__ Make the Screen Closable..
                setModified(false);
                
                //__ To Put the Value of the Flag...
                callBank = 0;
                transDetails.setTransDetails(null,null,null);
                //lblTransactionId_2.setText("");
            }
            
            if (observable.getActionType()==ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();                   //__ Reset the fields in the UI to null...
        observable.resetLable();                  //__ Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.resetForm();             //__ Reset the fields in the UI to null...
        observable.resetLable();            //__ Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){   //__   Edit=0 and Delete=1
            HashMap whereMap = new HashMap();
            
            ArrayList lst = new ArrayList();
            lst.add("INWARD ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            whereMap.put("BANK_CODE", TrueTransactMain.BANK_ID);
            whereMap.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewInwardClearing");
            new ViewAll(this, viewMap).show();
        }else{
            updateOBFields();
            viewMap.put("MAPNAME", "Inward.getAccountList"+observable.getCboProductType());
            if (CommonUtil.convertObjToStr(observable.getCboProductType()).equals("RM")|| CommonUtil.convertObjToStr(observable.getCboProductType()).equals("DD")){
                 
                viewMap.put("MAPNAME", "getPayableIssues");
            }
            
            
            HashMap hash = new HashMap();
            hash.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProdId.getModel()).getKeyForSelected()));
            hash.put("SELECTED_BRANCH",getSelectedBranchID());
            
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            new ViewAll(this, viewMap, true).show();
        }
        //        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE ||view.equals("view") || viewType==VIEW) {
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
                              String accountNo=CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
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
            hash.put(CommonConstants.MAP_WHERE, hash.get("INWARD ID"));
            hash.put("BRANCH_ID", getSelectedBranchID());
            hash.put("INWARD_ID", CommonUtil.convertObjToStr(hash.get("INWARD ID")));
            hash.put("TRANS_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("TRANS_DT"))));
            hash.put("INITIATED_BRANCH",CommonUtil.convertObjToStr(hash.get("INITIATED_BRANCH")));
            System.out.println("hash: " + hash);
            observable.populateData(hash);// Called to display the Data in the UI fields...
            // To set the Value of Transaction Id...
            final String INWARDID = CommonUtil.convertObjToStr(hash.get("INWARD ID"));
            final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
            System.out.println("ACCOUNTNO: " + ACCOUNTNO);
            
            // To set the Value of Transaction Id in UI...
            observable.setLblTransactionId(INWARDID);
            //To Set the Value of Account holder Name in UI...
            if(!ACCOUNTNO.equals("") && ACCOUNTNO.length()>0){
                if(observable.getCboProductType().equals("RM")){
                    HashMap remitMap=new HashMap();
                    remitMap.put("PROD_ID", "PO");
                    List lst=ClientUtil.executeQuery("getAccountHeadForProductId", remitMap);
                    if(lst!=null && lst.size()>0){
                        remitMap=new HashMap();
                        remitMap=(HashMap)lst.get(0);
                        transDetails.setTransDetails("GL", getSelectedBranchID(), CommonUtil.convertObjToStr(remitMap.get("ISSUE_HD")));
                        remitMap=new HashMap();
                        
                    }
                }else{
                    observable.setAccountName(ACCOUNTNO);
                    transDetails.setTransDetails(observable.getCboProductType(), getSelectedBranchID(), ACCOUNTNO);
                }
            }
            //for the Inward Clearing Date...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||viewType==AUTHORIZE ||viewType==VIEW) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            }else{
                ClientUtil.enableDisable(this, true);     // Enables the panel...
                enableDisableButtons();                   // To Enable the Buttons(folder) buttons in UI...
                txtAccountNumber.setEnabled(true);       //To make this textBox non editable...
                //txtCovtAmount.setEnabled(false);        //To make this textBox non editable...
            }
            setInvisible();
            
            observable.setStatus();             // To set the Value of lblStatus...
            
            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            this.isFilled = true;
            
            //__ To Put the Value of the Flag...
            callBank = 1;
            
        }else if (viewType==ACCNUMBER) {
            System.out.println("HASH from fillDATA"+hash);
            
            updateOBFields();
            
            final String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
            observable.setTxtAccountNumber(ACCOUNTNO);
            String productType = (String)((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected();
            System.out.println("observable.getProdTypeTO(): " + observable.getProdTypeTO());
            if (!productType.equals("RM")) {
                observable.setAccountName(ACCOUNTNO);
                if (observable.getLblAccountNumberName()!=null & observable.getLblAccountNumberName().length()>0) {
                    transDetails.setTransDetails(observable.getProdTypeTO(), getSelectedBranchID(), ACCOUNTNO);
                } //else {
                //                    displayAlert("Invalid Account Number...");
                //                }
            } else {
                
                observable.setCboInstrumentTypeID("Pay Order");
                observable.setTxtInstrumentNo1(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO1")));
                observable.setTxtInstrumentNo2(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO2")));
                observable.setTdtInstrumentDate(CommonUtil.convertObjToStr(hash.get("BATCH_DT")));
                observable.setTxtAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                
                String Msg=CommonUtil.convertObjToStr(hash.get("FAVOURING"));
                if(Msg.length()>60) { 
                    Msg=Msg.substring(1,60);                
                }
                observable.setTxtPayeeName("To "+Msg);
                ClientUtil.showMessageWindow("PayeeName :      "+CommonUtil.convertObjToStr(hash.get("FAVOURING")));
                HashMap remitMap=new HashMap();
                remitMap.put("PROD_ID", "PO");
                List lst=ClientUtil.executeQuery("getAccountHeadForProductId", remitMap);
                if(lst!=null && lst.size()>0){
                    remitMap=new HashMap();
                    remitMap=(HashMap)lst.get(0);
                    transDetails.setTransDetails("GL", getSelectedBranchID(), CommonUtil.convertObjToStr(remitMap.get("ISSUE_HD")));
                    
                }
                lst=null;
                remitMap.put("INSTRUMENT_NO1",CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO1")));
                remitMap.put("INSTRUMENT_NO2",CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO2")));                
                lst=ClientUtil.executeQuery("remitanceExistance", remitMap);
               if(lst!=null && lst.size()>0){
                    remitMap=new HashMap();
                    remitMap=(HashMap)lst.get(0);
                    String msg ="";
                    msg="This Instrument pending for authorization or  already paid\n";
                    msg=msg+"Instrument Type   :"+hash.get("INSTRUMENT_NO1")+"\n";
                    msg=msg+"Instrument No     :"+hash.get("INSTRUMENT_NO2")+"\n";
                    msg=msg+"INWARD_ID         :"+remitMap.get("INWARD_ID")+"\n";
                    msg=msg+"SCHEDULE_NO       :"+remitMap.get("SCHEDULE_NO")+"\n";
                    msg=msg+"CLEARING_DT       :"+remitMap.get("CLEARING_DT")+"\n";
                    msg=msg+"AMOUNT            :"+remitMap.get("AMOUNT")+"\n";
                    
                    
                    ClientUtil.showMessageWindow(msg);
                    msg=null;
                    btnCancelActionPerformed(null);
               }
                
            }
            //            observable.setActData(ACCOUNTNO);
            //            observable.setBalance(ACCOUNTNO);
            
            
            //            transDetails.setTransDetails(observable.getProdTypeTO(), TrueTransactMain.BRANCH_ID, ACCOUNTNO);
            observable.ttNotifyObservers();
            //            cboCurrency.setSelectedItem("");
        }
        else if(viewType == BANKCODE){
            observable.setTxtBankCodeID(CommonUtil.convertObjToStr( hash.get("BANK_CODE")));
            txtBankCodeID.setText(CommonUtil.convertObjToStr( hash.get("BANK_CODE")));
        }
        else if (viewType==BRANCH_CODE){
            observable.setTxtBranchCodeID(CommonUtil.convertObjToStr( hash.get("BRANCH_CODE")));
            txtBranchCodeID.setText(CommonUtil.convertObjToStr( hash.get("BRANCH_CODE")));
        }
        if(viewType==AUTHORIZE){
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    /*
     * To set the Fields as Invisible...
     */
    private void setInvisible(){
        lblCurrency. setVisible(false);
        cboCurrency.setVisible(false);
        lblCovtAmount.setVisible(false);
        txtCovtAmount.setVisible(false);
        lblProdCurrency.setVisible(false);
        lblProdCurrency_2.setVisible(false);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();              //__ Reset the fields in the UI to null...
        observable.resetLable();             //__ Reset the Editable Lables in the UI to null...
        ClientUtil.enableDisable(this, true);//__ Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //__ Sets the Action Type to be performed...
        setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();             //__ To set the Value of lblStatus...
        enableDisableButtons();             //__ To enable the buttons(folder) in the UI...
        enableDisableBankbtn(true);
        setInvisible();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
        txtAccountNumber.setEnabled(true); //__ To make this textBox non editable...
        lblTransactionId_2.setText("");     //__ To make the value of the Transaction Id = null...
        
        //__ To Put the Value of the Flag...
        callBank = 1;
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtPayeeName.setText("To,");
        
        //txtCovtCurrency.setEnabled(false); //To make this textBox non editable...
    }//GEN-LAST:event_btnNewActionPerformed
    //Start of the menu items...
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
    //End of menu items...
    
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        InwardClearingUI  gui = new InwardClearingUI("");
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCodeID;
    private com.see.truetransact.uicomponent.CButton btnBranchCodeID;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CComboBox cboClearingType;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentTypeID;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboScheduleNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadProdId;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumberName;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBookedAmount;
    private com.see.truetransact.uicomponent.CLabel lblBookedAmount_2;
    private com.see.truetransact.uicomponent.CLabel lblBookedInstrument;
    private com.see.truetransact.uicomponent.CLabel lblBookedInstrument_2;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate_2;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblCountWarning;
    private com.see.truetransact.uicomponent.CLabel lblCovtAmount;
    private com.see.truetransact.uicomponent.CLabel lblCurrency;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentTypeID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPayeeName;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency_2;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblScheduleNumber;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt_2;
    private com.see.truetransact.uicomponent.CLabel lblTotalInstrument;
    private com.see.truetransact.uicomponent.CLabel lblTotalInstrument_2;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDate;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDate_2;
    private com.see.truetransact.uicomponent.CLabel lblTransactionId;
    private com.see.truetransact.uicomponent.CLabel lblTransactionId_2;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctHd;
    private com.see.truetransact.uicomponent.CPanel panClearingData;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNumber;
    private com.see.truetransact.uicomponent.CPanel panInwardClearing;
    private com.see.truetransact.uicomponent.CPanel panInwardTransDetails;
    private com.see.truetransact.uicomponent.CPanel panProdData;
    private com.see.truetransact.uicomponent.CPanel panProductData;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtBankCodeID;
    private com.see.truetransact.uicomponent.CTextField txtBranchCodeID;
    private com.see.truetransact.uicomponent.CTextField txtCovtAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtPayeeName;
    // End of variables declaration//GEN-END:variables
    
}
