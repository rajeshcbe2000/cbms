/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.product.accountswithotherbank;

/**
 *
 * @author Ashok
 *  @modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uimandatory.UIMandatoryField;
//import com.see.truetransact.uivalidation.NumericValidation;
//import com.see.truetransact.uivalidation.CurrencyValidation;
//import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
//import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Date;
import com.see.truetransact.ui.product.investments.InvestmentsProductMRB;

public class AccountswithOtherBankProductUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private AccountswithOtherBankProductOB observable;
    private InvestmentsProductMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.accountswithotherbank.AccountswithOtherBankProductRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public AccountswithOtherBankProductUI() {
        initUIComponents();
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panShareProduct, getMandatoryHashMap());
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        ClientUtil.enableDisable(panShareProduct, false);
        setButtonEnableDisable();
        tabShareProduct.resetVisits();
        cboOtherbankacntsBehaves.setEditable(false);
        currDt = ClientUtil.getCurrentDate();
//        panBrokerCommissionAcHead.setVisible(false);
//        panDividentPaidAcHead.setVisible(false);
//        panPremiumReceivedAcHead.setVisible(false);
//        panPremiumPaidAcHead.setVisible(false);
//        panPremiumDepreciationAcHead.setVisible(false);
//        lblBrokerCommissionAcHead.setVisible(false);
//        lblDividentPaidAcHead.setVisible(false);
//        lblPremiumReceivedAcHead.setVisible(false);
//        lblPremiumPaidAcHead.setVisible(false);
//        lblPremiumDepreciationAcHead.setVisible(false);
        disDesc();
    }
    public void disDesc()
    {
        txtInvestmentAcHeadDesc.setEnabled(false);
        txtIntPaidAcHeadDesc.setEnabled(false);
        txtIntReceivedAcHeadDesc.setEnabled(false);
//        txtPremiumPaidAcHeadDesc.setEnabled(false);
//        txtPremiumDepreciationAcHeadDesc.setEnabled(false);
//        txtBrokerCommissionAcHeadDesc.setEnabled(false);
//        txtDividentReceivedAcHeadDesc.setEnabled(false);
        txtChargeAcHeadDesc.setEnabled(false);
        txtInterestReceivableAcHeadDesc.setEnabled(false);
        txtPenalAcHeadDesc.setEnabled(false);
//        txtServiceTaxAcHeadDesc.setEnabled(false);
//        txtDividentPaidAcHeadDesc.setEnabled(false);
//        txtPremiumReceivedAcHeadDesc.setEnabled(false);
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
        btnInvestmentAcHead.setName("btnInvestmentAcHead");
        btnIntReceivedAcHead.setName("btnIntReceivedAcHead");
        btnIntPaidAcHead.setName("btnIntPaidAcHead");
//        btnPremiumPaidAcHead.setName("btnPremiumPaidAcHead");
//        btnPremiumDepreciationAcHead.setName("btnPremiumDepreciationAcHead");
//        btnBrokerCommissionAcHead.setName("btnBrokerCommissionAcHead");
//        btnDividentReceivedAcHead.setName("btnDividentReceivedAcHead");
//        btnServiceTaxAcHead.setName("btnServiceTaxAcHead");
//        btnDividentPaidAcHead.setName("btnDividentPaidAcHead");
        lblInvestmentAcHead.setName("lblInvestmentAcHead");
        lblIntReceivedAcHead.setName("lblIntReceivedAcHead");
        lblPenalAcHead.setName("lblChargeAcHead");
        lblInterestReceivableAcHead.setName("lblInterestReceivableAcHead");
        lblIntPaidAcHead.setName("lblIntPaidAcHead");
//        lblPremiumPaidAcHead.setName("lblPremiumPaidAcHead");
//        lblPremiumDepreciationAcHead.setName("lblPremiumDepreciationAcHead");
//        lblBrokerCommissionAcHead.setName("lblBrokerCommissionAcHead");
//        lblDividentReceivedAcHead.setName("lblDividentReceivedAcHead");
//        lblServiceTaxAcHead.setName("lblServiceTaxAcHead");
//        lblDividentPaidAcHead.setName("lblDividentPaidAcHead");
        lblOtherbankacntsBehaves.setName("lblInvestmentBehaves");
//        lblPremiumReceivedAcHead.setName("lblPremiumReceivedAcHead");
        lblProductID.setName("lblProductID");
        lblDesc.setName("lblDesc");
        cboOtherbankacntsBehaves.setName("cboInvestmentBehaves");
        txtProductID.setName("txtProductID");
        txtDesc.setName("txtDesc");
        txtInvestmentAcHead.setName("txtInvestmentAcHead");
        txtIntReceivedAcHead.setName("txtIntReceivedAcHead");
        txtIntPaidAcHead.setName("txtIntPaidAcHead");
        txtChargeAcHead.setName("txtChargeAcHead");
        txtInterestReceivableAcHead.setName("txtInterestReceivableAcHead");
//        txtPremiumPaidAcHead.setName("txtPremiumPaidAcHead");
//        txtPremiumDepreciationAcHead.setName("txtPremiumDepreciationAcHead");
//        txtBrokerCommissionAcHead.setName("txtBrokerCommissionAcHead");
//        txtDividentReceivedAcHead.setName("txtDividentReceivedAcHead");
//        txtServiceTaxAcHead.setName("txtServiceTaxAcHead");
//        txtDividentPaidAcHead.setName("txtDividentPaidAcHead");
//       txtPremiumReceivedAcHead.setName("txtPremiumReceivedAcHead");
        
    }
    
    
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblInvestmentAcHead.setText(resourceBundle.getString("lblInvestmentAcHead"));
        lblIntReceivedAcHead.setText(resourceBundle.getString("lblIntReceivedAcHead"));
        lblChargeAcHead.setText(resourceBundle.getString("lblChargeAcHead"));
        lblInterestReceivableAcHead.setText(resourceBundle.getString("lblInterestReceivableAcHead"));
        lblIntPaidAcHead.setText(resourceBundle.getString("lblIntPaidAcHead"));
//        lblPremiumPaidAcHead.setText(resourceBundle.getString("lblPremiumPaidAcHead"));
//        lblPremiumReceivedAcHead.setText(resourceBundle.getString("lblPremiumReceivedAcHead"));
//        
//        lblPremiumDepreciationAcHead.setText(resourceBundle.getString("lblPremiumDepreciationAcHead"));
//        lblBrokerCommissionAcHead.setText(resourceBundle.getString("lblBrokerCommissionAcHead"));
//        lblDividentReceivedAcHead.setText(resourceBundle.getString("lblDividentReceivedAcHead"));
//        lblServiceTaxAcHead.setText(resourceBundle.getString("lblServiceTaxAcHead"));
//        lblDividentPaidAcHead.setText(resourceBundle.getString("lblTransServiceTaxAcHead"));     
        lblOtherbankacntsBehaves.setText(resourceBundle.getString("lblInvestmentBehaves"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblDesc.setText(resourceBundle.getString("lblDesc"));
        
        
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInvestmentBehaves", new Boolean(true));
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtDesc", new Boolean(true));
        mandatoryMap.put("txtInvestmentAcHead", new Boolean(true));
        mandatoryMap.put("txtIntReceivedAcHead", new Boolean(true));
        mandatoryMap.put("txtIntPaidAcHead", new Boolean(false));
        mandatoryMap.put("txtChargeAcHead", new Boolean(false));
        mandatoryMap.put("txtInterestReceivableAcHead", new Boolean(false));
      
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /* Creates the insstance of ShareProduct which acts as  Observable to
     *ShareProduct UI */
    private void setObservable() {
        try{
            observable = AccountswithOtherBankProductOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboOtherbankacntsBehaves.setModel(observable.getCbmOtherbankacntsBehaves());
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new InvestmentsProductMRB();
        cboOtherbankacntsBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInvestmentBehaves"));
        txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
        txtDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesc"));
        txtInvestmentAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentAcHead"));
        txtIntReceivedAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntReceivedAcHead"));
        txtIntPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntPaidAcHead"));
        txtChargeAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeAcHead"));
        txtInterestReceivableAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestReceivableAcHead"));
//        txtPremiumPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumPaidAcHead"));
//        txtPremiumDepreciationAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumDepreciationAcHead"));
//        txtBrokerCommissionAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBrokerCommissionAcHead"));
//        txtDividentReceivedAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDividentReceivedAcHead"));
//        txtServiceTaxAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxAcHead"));
//        txtServiceTaxAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxAcHead"));
//        txtDividentPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDividentPaidAcHead"));
        
    }
    public String getAccHdDesc(String accHdId)
    {
        HashMap map1= new HashMap();
        map1.put("ACCHD_ID", accHdId);
        List list1= ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if(!list1.isEmpty())
        {
           HashMap map2= new HashMap();
           map2= (HashMap) list1.get(0);
           String accHdDesc=map2.get("AC_HD_DESC").toString();
           return accHdDesc;
        }
        else
        {
            return "";
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboOtherbankacntsBehaves.setSelectedItem(observable.getCboOtherbankacntsBehaves());
        txtProductID.setText(observable.getTxtProductID());
        txtDesc.setText(observable.getTxtDesc());
        txtInvestmentAcHead.setText(observable.getTxtInvestmentAcHead());
        if(!txtInvestmentAcHead.getText().equals(""))
        {
          txtInvestmentAcHeadDesc.setText(getAccHdDesc(observable.getTxtInvestmentAcHead())); 
          
        }
        txtIntPaidAcHead.setText(observable.getTxtIntPaidAcHead());
        if(!txtIntPaidAcHead.getText().equals(""))
        {
          txtIntPaidAcHeadDesc.setText(getAccHdDesc(observable.getTxtIntPaidAcHead())); 
          //
        }
        txtIntReceivedAcHead.setText(observable.getTxtIntReceivedAcHead());
        if(!txtIntReceivedAcHead.getText().equals(""))
        {
          txtIntReceivedAcHeadDesc.setText(getAccHdDesc(observable.getTxtIntReceivedAcHead())); 
          //
        }
//        txtPremiumPaidAcHead.setText(observable.getTxtPremiumPaidAcHead());
//        if(!txtPremiumPaidAcHead.getText().equals(""))
//        {
//          txtPremiumPaidAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumPaidAcHead())); 
//         // 
//        }
//        txtPremiumDepreciationAcHead.setText(observable.getTxtPremiumDepreciationAcHead());
//         if(!txtPremiumDepreciationAcHead.getText().equals(""))
//        {
//          txtPremiumDepreciationAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumDepreciationAcHead())); 
//          //
//        }
//        txtBrokerCommissionAcHead.setText(observable.getTxtBrokerCommissionAcHead());
//        if(!txtBrokerCommissionAcHead.getText().equals(""))
//        {
//          txtBrokerCommissionAcHeadDesc.setText(getAccHdDesc(observable.getTxtBrokerCommissionAcHead())); 
//         //
//        }
//        txtDividentReceivedAcHead.setText(observable.getTxtDividentReceivedAcHead());
//         if(!txtDividentReceivedAcHead.getText().equals(""))
//        {
//          txtDividentReceivedAcHeadDesc.setText(getAccHdDesc(observable.getTxtDividentReceivedAcHead())); 
//         //
//        }
        txtChargeAcHead.setText(observable.getTxtChargeAcHead());
        if(!txtChargeAcHead.getText().equals(""))
        {
          txtChargeAcHeadDesc.setText(getAccHdDesc(observable.getTxtChargeAcHead())); 
          //
        }
        txtInterestReceivableAcHead.setText(observable.getTxtInterestReceivableAcHead());
        if(!txtInterestReceivableAcHead.getText().equals(""))
        {
          txtInterestReceivableAcHeadDesc.setText(getAccHdDesc(observable.getTxtInterestReceivableAcHead())); 
          //
        }
        txtPenalAcHead.setText(observable.getTxtPenalAcHead());
        if(!txtPenalAcHead.getText().equals(""))
        {
          txtPenalAcHead.setText(getAccHdDesc(observable.getTxtPenalAcHead())); 
          //
        }
//        txtDividentPaidAcHead.setText(observable.getTxtDividentPaidAcHead());
//        if(!txtDividentPaidAcHead.getText().equals(""))
//        {
//          txtDividentPaidAcHeadDesc.setText(getAccHdDesc(observable.getTxtDividentPaidAcHead())); 
//          //
//        }
//        txtPremiumReceivedAcHead.setText(observable.getTxtPremiumReceivedAcHead());
//        if(!txtPremiumReceivedAcHead.getText().equals(""))
//        {
//          txtPremiumReceivedAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumReceivedAcHead())); 
//          //
//        }
        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboOtherbankacntsBehaves(CommonUtil.convertObjToStr(cboOtherbankacntsBehaves.getSelectedItem()));
        observable.setTxtProductID(CommonUtil.convertObjToStr(txtProductID.getText()));
        observable.setTxtDesc(CommonUtil.convertObjToStr(txtDesc.getText()));
        observable.setTxtInvestmentAcHead(CommonUtil.convertObjToStr(txtInvestmentAcHead.getText()));
        observable.setTxtIntPaidAcHead(CommonUtil.convertObjToStr(txtIntPaidAcHead.getText()));
        observable.setTxtIntReceivedAcHead(CommonUtil.convertObjToStr(txtIntReceivedAcHead.getText()));
//        observable.setTxtIntPaidAcHead(CommonUtil.convertObjToStr(txtIntPaidAcHead.getText()));
//        observable.setTxtPremiumPaidAcHead(CommonUtil.convertObjToStr(txtPremiumPaidAcHead.getText()));
//        observable.setTxtPremiumDepreciationAcHead(CommonUtil.convertObjToStr(txtPremiumDepreciationAcHead.getText()));
//        observable.setTxtBrokerCommissionAcHead(CommonUtil.convertObjToStr(txtBrokerCommissionAcHead.getText()));
//        observable.setTxtDividentReceivedAcHead(CommonUtil.convertObjToStr(txtDividentReceivedAcHead.getText()));
        observable.setTxtChargeAcHead(CommonUtil.convertObjToStr(txtChargeAcHead.getText()));
        observable.setTxtInterestReceivableAcHead(CommonUtil.convertObjToStr(txtInterestReceivableAcHead.getText()));
        observable.setTxtPenalAcHead(CommonUtil.convertObjToStr(txtPenalAcHead.getText()));
//        observable.setTxtDividentPaidAcHead(CommonUtil.convertObjToStr(txtDividentPaidAcHead.getText()));
//        observable.setTxtPremiumReceivedAcHead(CommonUtil.convertObjToStr(txtPremiumReceivedAcHead.getText()));
      
    }
    
    /** Enabling and Disabling of Buttons after Save,Edit,Delete operations */
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
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        
    }
    
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtProductID.setMaxLength(6);
        txtDesc.setMaxLength(50);
        txtInvestmentAcHead.setMaxLength(16);
        txtIntPaidAcHead.setMaxLength(16);
        txtIntReceivedAcHead.setMaxLength(16);
//        txtPremiumDepreciationAcHead.setMaxLength(16);
//        txtPremiumPaidAcHead.setMaxLength(16);
//        txtBrokerCommissionAcHead.setMaxLength(16);
//        txtDividentReceivedAcHead.setMaxLength(16);
//        txtServiceTaxAcHead.setMaxLength(16);
//        txtDividentPaidAcHead.setMaxLength(16);
        txtProductID.setAllowAll(true);
        txtDesc.setAllowAll(true);
        txtInvestmentAcHead.setAllowAll(true);
        txtIntReceivedAcHead.setAllowAll(true);
        txtIntPaidAcHead.setAllowAll(true);
//        txtDividentReceivedAcHead.setAllowAll(true);
//        txtServiceTaxAcHead.setAllowAll(true);
        txtChargeAcHead.setAllowAll(true);
        txtPenalAcHead.setAllowAll(true);
        txtInterestReceivableAcHead.setAllowAll(true);
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        btnInvestmentAcHead.setEnabled(enable);
        btnIntPaidAcHead.setEnabled(enable);
        btnIntReceivedAcHead.setEnabled(enable);
//        btnDividentReceivedAcHead.setEnabled(enable);
//        btnPremiumDepreciationAcHead.setEnabled(enable);
//        btnPremiumPaidAcHead.setEnabled(enable);
//        btnBrokerCommissionAcHead.setEnabled(enable);
//        btnServiceTaxAcHead.setEnabled(enable);
        btnChargeAcHead.setEnabled(enable);
        btnInterestReceivableAcHead.setEnabled(enable);
//        btnDividentPaidAcHead.setEnabled(enable);    
//        btnPremiumReceivedAcHead.setEnabled(enable);    
        cboOtherbankacntsBehaves.setEnabled(enable);
//        cboInvestmentBehaves.setEditable(enable);
        txtProductID.setEnabled(enable);
        txtProductID.setEditable(enable);
        txtDesc.setEnabled(enable);
        txtDesc.setEditable(enable);
     
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
    
    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            final String mandatoryMessage = checkMandatory(panShareProduct);
            String alertMsg ="";
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }else{
                observable.execute(status);
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    setButtonEnableDisable();
                    observable.resetForm();
                    btnCancelActionPerformed(null);
                }
                
                //__ if the Action is not Falied, Reset the fields...
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /** This will checks whether the Mandatory fields in the UI are filled up, If not filled up
     *it will retun an MandatoryMessage*/
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
    }
    
    /** This will Display the Mandatory Message in a Dialog Box */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        observable.setResultStatus();
    }
    
    /** This will show a popup screen which shows all tbe Rows.of the table */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            ArrayList lst = new ArrayList();
            lst.add("ACCOUNT_TYPE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOtherBankAccountsProduct");
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
            
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) 
                {
                    ClientUtil.enableDisable(panShareProduct, false);
                    setHelpButtonEnableDisable(false);
                    
                    
                    
                }
                else 
                {
                    ClientUtil.enableDisable(panShareProduct, true);
                    setHelpButtonEnableDisable(true);
                     txtInvestmentAcHeadDesc.setEnabled(false);
                    txtIntPaidAcHeadDesc.setEnabled(false);
                    txtIntReceivedAcHeadDesc.setEnabled(false);
//                    txtPremiumPaidAcHeadDesc.setEnabled(false);
//                    txtPremiumDepreciationAcHeadDesc.setEnabled(false);
//                    txtBrokerCommissionAcHeadDesc.setEnabled(false);
//                    txtDividentReceivedAcHeadDesc.setEnabled(false);
                    txtChargeAcHeadDesc.setEnabled(false);
                    txtInterestReceivableAcHeadDesc.setEnabled(false);
//                    txtServiceTaxAcHeadDesc.setEnabled(false);
//                    txtDividentPaidAcHeadDesc.setEnabled(false);
//                    txtPremiumReceivedAcHeadDesc.setEnabled(false);
                
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            else if(viewType.equals("Investment")){
                txtInvestmentAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtInvestmentAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtInvestmentAcHeadDesc.setEnabled(false);
            } else if(viewType.equals("IntReceived")){
                txtIntReceivedAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtIntReceivedAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtIntReceivedAcHeadDesc.setEnabled(false);
            } else if(viewType.equals("IntPaid")){
                txtIntPaidAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtIntPaidAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtIntPaidAcHeadDesc.setEnabled(false);
            } 
//            else if(viewType.equals("PremiumPaid")){
//                txtPremiumPaidAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtPremiumPaidAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtPremiumPaidAcHeadDesc.setEnabled(false);
//            } else if(viewType.equals("PremiumDepreciation")){
//                txtPremiumDepreciationAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtPremiumDepreciationAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtPremiumDepreciationAcHeadDesc.setEnabled(false);
//            }
//            else if(viewType.equals("BrokerCommission")){
//                txtBrokerCommissionAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtBrokerCommissionAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtBrokerCommissionAcHeadDesc.setEnabled(false);
//            }
//            else if(viewType.equals("DividentReceived")){
//                txtDividentReceivedAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtDividentReceivedAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtDividentReceivedAcHeadDesc.setEnabled(false);
//            }
//            else if(viewType.equals("ServiceTax")){
//                txtServiceTaxAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtServiceTaxAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtServiceTaxAcHeadDesc.setEnabled(false);
//            } else if(viewType.equals("TransServiceTax")){
//                txtDividentPaidAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
//                txtDividentPaidAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
//                 txtDividentPaidAcHeadDesc.setEnabled(false);
//            }
            else if(viewType.equals("penalpaid")){
                txtPenalAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtPenalAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtPenalAcHeadDesc.setEnabled(false);
            }
            else if(viewType.equals("ChargePaid")){
                txtChargeAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtChargeAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtChargeAcHeadDesc.setEnabled(false);
            }else if(viewType.equals("InterestReceivable")){
                txtInterestReceivableAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtInterestReceivableAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtInterestReceivableAcHeadDesc.setEnabled(false);
            }    
        }
    }
    
    /** This will show the alertwindow when the user enters the already existing ShareType **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getOtherBankAccountsProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOtherBankAccountsProduct");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            //Changed By Suresh
//            String strWarnMsg = tabShareProduct.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                displayAlert(strWarnMsg);
//                return;
//            }
//            strWarnMsg = null;
            tabShareProduct.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("PROD_ID",CommonUtil.convertObjToStr(txtProductID.getText()));
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            
            ClientUtil.execute("authorizeOtherBankAccountsProduct", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        AccountswithOtherBankProductUI ui = new AccountswithOtherBankProductUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
        
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panShareProduct = new com.see.truetransact.uicomponent.CPanel();
        tabShareProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panShare = new com.see.truetransact.uicomponent.CPanel();
        lblOtherbankacntsBehaves = new com.see.truetransact.uicomponent.CLabel();
        cboOtherbankacntsBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        lblDesc = new com.see.truetransact.uicomponent.CLabel();
        txtDesc = new com.see.truetransact.uicomponent.CTextField();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panAcctDet = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentAcHead = new com.see.truetransact.uicomponent.CButton();
        txtInvestmentAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInvestmentAcHead = new com.see.truetransact.uicomponent.CLabel();
        panIntReceivedAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtIntReceivedAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntReceivedAcHead = new com.see.truetransact.uicomponent.CButton();
        txtIntReceivedAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblIntReceivedAcHead = new com.see.truetransact.uicomponent.CLabel();
        panIntPaidAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtIntPaidAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntPaidAcHead = new com.see.truetransact.uicomponent.CButton();
        txtIntPaidAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblIntPaidAcHead = new com.see.truetransact.uicomponent.CLabel();
        panChargeAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtChargeAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnChargeAcHead = new com.see.truetransact.uicomponent.CButton();
        txtChargeAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblPenalAcHead = new com.see.truetransact.uicomponent.CLabel();
        panInterestReceivableAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtInterestReceivableAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnInterestReceivableAcHead = new com.see.truetransact.uicomponent.CButton();
        txtInterestReceivableAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInterestReceivableAcHead = new com.see.truetransact.uicomponent.CLabel();
        panPenalAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtPenalAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnpenalAcHead = new com.see.truetransact.uicomponent.CButton();
        txtPenalAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblChargeAcHead = new com.see.truetransact.uicomponent.CLabel();
        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrShareProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(750, 460));
        setPreferredSize(new java.awt.Dimension(750, 460));

        panShareProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShareProduct.setMinimumSize(new java.awt.Dimension(417, 408));
        panShareProduct.setPreferredSize(new java.awt.Dimension(417, 408));
        panShareProduct.setLayout(new java.awt.GridBagLayout());

        panShare.setLayout(new java.awt.GridBagLayout());

        lblOtherbankacntsBehaves.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblOtherbankacntsBehaves, gridBagConstraints);

        cboOtherbankacntsBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOtherbankacntsBehaves.setNextFocusableComponent(txtInvestmentAcHead);
        cboOtherbankacntsBehaves.setPopupWidth(220);
        cboOtherbankacntsBehaves.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboOtherbankacntsBehavesItemStateChanged(evt);
            }
        });
        cboOtherbankacntsBehaves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboOtherbankacntsBehavesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(cboOtherbankacntsBehaves, gridBagConstraints);

        lblProductID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblProductID, gridBagConstraints);

        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtProductID, gridBagConstraints);

        lblDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblDesc, gridBagConstraints);

        txtDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 5, 4);
        panShare.add(txtDesc, gridBagConstraints);

        tabShareProduct.addTab("Accounts with Other Bank", panShare);

        panAccountDetails.setMinimumSize(new java.awt.Dimension(408, 358));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(408, 358));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panAcctDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAcctDet.setMinimumSize(new java.awt.Dimension(700, 350));
        panAcctDet.setPreferredSize(new java.awt.Dimension(700, 350));
        panAcctDet.setLayout(new java.awt.GridBagLayout());

        panInvestmentAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panInvestmentAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panInvestmentAcHead.setLayout(new java.awt.GridBagLayout());

        txtInvestmentAcHead.setEditable(false);
        txtInvestmentAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtInvestmentAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInvestmentAcHead.add(txtInvestmentAcHead, gridBagConstraints);

        btnInvestmentAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentAcHead.setEnabled(false);
        btnInvestmentAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentAcHead.add(btnInvestmentAcHead, gridBagConstraints);

        txtInvestmentAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInvestmentAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panInvestmentAcHead.add(txtInvestmentAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panInvestmentAcHead, gridBagConstraints);

        lblInvestmentAcHead.setText("Principal Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblInvestmentAcHead, gridBagConstraints);

        panIntReceivedAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panIntReceivedAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panIntReceivedAcHead.setLayout(new java.awt.GridBagLayout());

        txtIntReceivedAcHead.setEditable(false);
        txtIntReceivedAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntReceivedAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtIntReceivedAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntReceivedAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panIntReceivedAcHead.add(txtIntReceivedAcHead, gridBagConstraints);

        btnIntReceivedAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntReceivedAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntReceivedAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntReceivedAcHead.setEnabled(false);
        btnIntReceivedAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntReceivedAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panIntReceivedAcHead.add(btnIntReceivedAcHead, gridBagConstraints);

        txtIntReceivedAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntReceivedAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panIntReceivedAcHead.add(txtIntReceivedAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panIntReceivedAcHead, gridBagConstraints);

        lblIntReceivedAcHead.setText("Interest Received A/c (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblIntReceivedAcHead, gridBagConstraints);

        panIntPaidAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panIntPaidAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panIntPaidAcHead.setLayout(new java.awt.GridBagLayout());

        txtIntPaidAcHead.setEditable(false);
        txtIntPaidAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntPaidAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtIntPaidAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntPaidAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panIntPaidAcHead.add(txtIntPaidAcHead, gridBagConstraints);

        btnIntPaidAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntPaidAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntPaidAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntPaidAcHead.setEnabled(false);
        btnIntPaidAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntPaidAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panIntPaidAcHead.add(btnIntPaidAcHead, gridBagConstraints);

        txtIntPaidAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntPaidAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panIntPaidAcHead.add(txtIntPaidAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panIntPaidAcHead, gridBagConstraints);

        lblIntPaidAcHead.setText("Interest Paid A/c (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblIntPaidAcHead, gridBagConstraints);

        panChargeAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panChargeAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panChargeAcHead.setLayout(new java.awt.GridBagLayout());

        txtChargeAcHead.setEditable(false);
        txtChargeAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargeAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtChargeAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargeAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panChargeAcHead.add(txtChargeAcHead, gridBagConstraints);

        btnChargeAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChargeAcHead.setEnabled(false);
        btnChargeAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChargeAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChargeAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panChargeAcHead.add(btnChargeAcHead, gridBagConstraints);

        txtChargeAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtChargeAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panChargeAcHead.add(txtChargeAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panChargeAcHead, gridBagConstraints);

        lblPenalAcHead.setText("Penal Paid A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblPenalAcHead, gridBagConstraints);

        panInterestReceivableAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panInterestReceivableAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panInterestReceivableAcHead.setLayout(new java.awt.GridBagLayout());

        txtInterestReceivableAcHead.setEditable(false);
        txtInterestReceivableAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestReceivableAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtInterestReceivableAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestReceivableAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInterestReceivableAcHead.add(txtInterestReceivableAcHead, gridBagConstraints);

        btnInterestReceivableAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInterestReceivableAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInterestReceivableAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInterestReceivableAcHead.setEnabled(false);
        btnInterestReceivableAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestReceivableAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInterestReceivableAcHead.add(btnInterestReceivableAcHead, gridBagConstraints);

        txtInterestReceivableAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 20));
        txtInterestReceivableAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panInterestReceivableAcHead.add(txtInterestReceivableAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panInterestReceivableAcHead, gridBagConstraints);

        lblInterestReceivableAcHead.setText("Interest Receivable A/c Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblInterestReceivableAcHead, gridBagConstraints);

        panPenalAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panPenalAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panPenalAcHead.setLayout(new java.awt.GridBagLayout());

        txtPenalAcHead.setEditable(false);
        txtPenalAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtPenalAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panPenalAcHead.add(txtPenalAcHead, gridBagConstraints);

        btnpenalAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnpenalAcHead.setEnabled(false);
        btnpenalAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnpenalAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnpenalAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpenalAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panPenalAcHead.add(btnpenalAcHead, gridBagConstraints);

        txtPenalAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPenalAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panPenalAcHead.add(txtPenalAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panPenalAcHead, gridBagConstraints);

        lblChargeAcHead.setText("Charge Paid A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblChargeAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(panAcctDet, gridBagConstraints);

        tabShareProduct.addTab("Account Details", panAccountDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareProduct.add(tabShareProduct, gridBagConstraints);

        getContentPane().add(panShareProduct, java.awt.BorderLayout.CENTER);

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

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

        mbrShareProduct.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShareProduct.add(mnuProcess);

        setJMenuBar(mbrShareProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtChargeAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargeAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtChargeAcHead.getText().equals(""))
        {
          txtChargeAcHeadDesc.setText(getAccHdDesc(txtChargeAcHead.getText())); 
        }
    }//GEN-LAST:event_txtChargeAcHeadFocusLost

    private void txtIntPaidAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntPaidAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtIntPaidAcHead.getText().equals(""))
        {
          txtIntPaidAcHeadDesc.setText(getAccHdDesc(txtIntPaidAcHead.getText())); 
        }
    }//GEN-LAST:event_txtIntPaidAcHeadFocusLost

    private void txtInterestReceivableAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestReceivableAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtInterestReceivableAcHead.getText().equals(""))
        {
          txtInterestReceivableAcHeadDesc.setText(getAccHdDesc(txtInterestReceivableAcHead.getText())); 
        }
    }//GEN-LAST:event_txtInterestReceivableAcHeadFocusLost

    private void txtIntReceivedAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntReceivedAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtIntReceivedAcHead.getText().equals(""))
        {
          txtIntReceivedAcHeadDesc.setText(getAccHdDesc(txtIntReceivedAcHead.getText())); 
        }
    }//GEN-LAST:event_txtIntReceivedAcHeadFocusLost

    private void txtInvestmentAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtInvestmentAcHead.getText().equals(""))
        {
          txtInvestmentAcHeadDesc.setText(getAccHdDesc(txtInvestmentAcHead.getText())); 
        }
    }//GEN-LAST:event_txtInvestmentAcHeadFocusLost

    private void btnInterestReceivableAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestReceivableAcHeadActionPerformed
        // TODO add your handling code here:
        callView("InterestReceivable");
    }//GEN-LAST:event_btnInterestReceivableAcHeadActionPerformed

    private void btnChargeAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeAcHeadActionPerformed
        // TODO add your handling code here:
        callView("ChargePaid");
    }//GEN-LAST:event_btnChargeAcHeadActionPerformed

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            boolean exists=false;
            exists=observable.isInvsetMentTypeExists(CommonUtil.convertObjToStr(txtProductID.getText()));
            if(exists==true){
                displayAlert("Already This Prod Id Exists");
                txtProductID.setText("");
                txtProductID.requestFocus();
            }else{
               HashMap whereMap = new HashMap();
                whereMap.put("PROD_ID", txtProductID.getText());
                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
                System.out.println("getSBODBorrowerEligAmt : " + lst);
                if (lst != null && lst.size() > 0) {
                    HashMap existingProdIdMap = (HashMap) lst.get(0);
                    if (existingProdIdMap.containsKey("PROD_ID")) {
                        ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
                        txtProductID.setText("");
                    }
                } 
            } 
        }
    }//GEN-LAST:event_txtProductIDFocusLost
                    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
            
    private void btnIntPaidAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntPaidAcHeadActionPerformed
        // TODO add your handling code here:
        callView("IntPaid");
    }//GEN-LAST:event_btnIntPaidAcHeadActionPerformed
    
    private void btnIntReceivedAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntReceivedAcHeadActionPerformed
        // TODO add your handling code here:
        callView("IntReceived");
    }//GEN-LAST:event_btnIntReceivedAcHeadActionPerformed
    
    private void btnInvestmentAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentAcHeadActionPerformed
        // TODO add your handling code here:
        callView("Investment");
    }//GEN-LAST:event_btnInvestmentAcHeadActionPerformed
    
    private void cboOtherbankacntsBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboOtherbankacntsBehavesItemStateChanged
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboOtherbankacntsBehavesItemStateChanged
    
    private void cboOtherbankacntsBehavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboOtherbankacntsBehavesActionPerformed
        // TODO add your handling code here:
        try{
            String beheaves="";
            beheaves=observable.callForBehaves();
            System.out.println("##### beheaves : "+beheaves);
            if(beheaves.length()>0){
                enableDisable(false);
                lblInvestmentAcHead.setVisible(true);
                panInvestmentAcHead.setVisible(true);
                txtInvestmentAcHead.setEnabled(true);
                //lblPenalAcHead.setText("Charge Paid A/c Head");
                if(beheaves.equals("OTHER_BANK_SB") || beheaves.equals("OTHER_BANK_CA") || beheaves.equals("OTHER_BANK_SPD")){
                    lblIntReceivedAcHead.setVisible(true);
                    lblChargeAcHead.setVisible(true);
                    panIntReceivedAcHead.setVisible(true);
                    panChargeAcHead.setVisible(true);
                    txtChargeAcHead.setEnabled(true);
                    txtIntReceivedAcHead.setEnabled(true);
                }
                  if(beheaves.equals("CC") || beheaves.equals("OD") || beheaves.equals("BR") ){ // Modified by nithya on 15-11-2019 for KD-621 [ Reported after testing sprint 14 ]  
                      lblIntPaidAcHead.setVisible(true);
                      panIntPaidAcHead.setVisible(true);
                      lblChargeAcHead.setVisible(true);
                       panChargeAcHead.setVisible(true);
                       lblPenalAcHead.setVisible(true);
                       panPenalAcHead.setVisible(true);
                       txtPenalAcHead.setEnabled(true);
                       txtChargeAcHead.setEnabled(true);
                       txtIntPaidAcHead.setEnabled(true);
                       btnpenalAcHead.setEnabled(true);
                  }
              
               
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_cboOtherbankacntsBehavesActionPerformed
    
    private void enableDisable(boolean flag){
        lblInvestmentAcHead.setVisible(flag);
        lblIntReceivedAcHead.setVisible(flag);
        lblInterestReceivableAcHead.setVisible(flag);
        lblIntPaidAcHead.setVisible(flag);
        lblPenalAcHead.setVisible(flag);
        lblChargeAcHead.setVisible(flag);
//        lblDividentReceivedAcHead.setVisible(flag);
//        lblServiceTaxAcHead.setVisible(flag);
//        lblBrokerCommissionAcHead.setVisible(flag);
//        lblDividentPaidAcHead.setVisible(flag);
//        lblPremiumReceivedAcHead.setVisible(flag);
//        lblPremiumPaidAcHead.setVisible(flag);
//        lblPremiumDepreciationAcHead.setVisible(flag);
        panInvestmentAcHead.setVisible(flag);
        panIntReceivedAcHead.setVisible(flag);
        panInterestReceivableAcHead.setVisible(flag);
        panIntPaidAcHead.setVisible(flag);
        panChargeAcHead.setVisible(flag);
        panPenalAcHead.setVisible(flag);
//        panDividentReceivedAcHead.setVisible(flag);
//        panServiceTaxAcHead.setVisible(flag);
//        panBrokerCommissionAcHead.setVisible(flag);
//        panDividentPaidAcHead.setVisible(flag);
//        panPremiumReceivedAcHead.setVisible(flag);
//        panPremiumPaidAcHead.setVisible(flag);
//        panPremiumDepreciationAcHead.setVisible(flag);
        txtInvestmentAcHead.setEnabled(flag);
        txtIntReceivedAcHead.setEnabled(flag);
        txtInterestReceivableAcHead.setEnabled(flag);
        txtIntPaidAcHead.setEnabled(flag);
        txtChargeAcHead.setEnabled(flag);
//        txtDividentReceivedAcHead.setEnabled(flag);
//        txtServiceTaxAcHead.setEnabled(flag);
//        txtBrokerCommissionAcHead.setEnabled(flag);
//        txtDividentPaidAcHead.setEnabled(flag);
//        txtPremiumReceivedAcHead.setEnabled(flag);
//        txtPremiumPaidAcHead.setEnabled(flag);
//        txtPremiumDepreciationAcHead.setEnabled(flag);
    }
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:]
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        disDesc();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        super.removeEditLock(((ComboBoxModel)cboOtherbankacntsBehaves.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panShareProduct, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panAcctDet,false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboOtherbankacntsBehaves.setEnabled(false);
//        cboInvestmentBehaves.setEditable(false);
        txtProductID.setEnabled(false);
        txtProductID.setEditable(false);
        disDesc();
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panShareProduct, true);
        //        ClientUtil.enableDisable(panLoanType,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        //        btnTabNew.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        disDesc();
    }//GEN-LAST:event_btnNewActionPerformed

private void txtPenalAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalAcHeadFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtPenalAcHeadFocusLost

private void btnpenalAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpenalAcHeadActionPerformed
// TODO add your handling code here:
      callView("penalpaid");
}//GEN-LAST:event_btnpenalAcHeadActionPerformed
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargeAcHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnIntPaidAcHead;
    private com.see.truetransact.uicomponent.CButton btnIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CButton btnInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CButton btnInvestmentAcHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnpenalAcHead;
    private com.see.truetransact.uicomponent.CComboBox cboOtherbankacntsBehaves;
    private com.see.truetransact.uicomponent.CLabel lblChargeAcHead;
    private com.see.truetransact.uicomponent.CLabel lblDesc;
    private com.see.truetransact.uicomponent.CLabel lblIntPaidAcHead;
    private com.see.truetransact.uicomponent.CLabel lblIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CLabel lblInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentAcHead;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOtherbankacntsBehaves;
    private com.see.truetransact.uicomponent.CLabel lblPenalAcHead;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAcctDet;
    private com.see.truetransact.uicomponent.CPanel panChargeAcHead;
    private com.see.truetransact.uicomponent.CPanel panIntPaidAcHead;
    private com.see.truetransact.uicomponent.CPanel panIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CPanel panInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CPanel panInvestmentAcHead;
    private com.see.truetransact.uicomponent.CPanel panPenalAcHead;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private com.see.truetransact.uicomponent.CPanel panShareProduct;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareProduct;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtChargeAcHead;
    private com.see.truetransact.uicomponent.CTextField txtChargeAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntPaidAcHead;
    private com.see.truetransact.uicomponent.CTextField txtIntPaidAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CTextField txtIntReceivedAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CTextField txtInterestReceivableAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAcHead;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtPenalAcHead;
    private com.see.truetransact.uicomponent.CTextField txtPenalAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    // End of variables declaration//GEN-END:variables
    
}
