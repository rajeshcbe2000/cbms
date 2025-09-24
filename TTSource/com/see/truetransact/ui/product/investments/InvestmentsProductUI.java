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

package com.see.truetransact.ui.product.investments;

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
//import java.util.Date;
import com.see.truetransact.ui.product.investments.InvestmentsProductMRB;
import java.util.Date;
public class InvestmentsProductUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private InvestmentsProductOB observable;
    private InvestmentsProductMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.investments.InvestmentsProductRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public InvestmentsProductUI() {
        initUIComponents();
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        currDt = ClientUtil.getCurrentDate();
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
        cboInvestmentBehaves.setEditable(false);
        panBrokerCommissionAcHead.setVisible(false);
        panDividentPaidAcHead.setVisible(false);
        panPremiumReceivedAcHead.setVisible(false);
        panPremiumPaidAcHead.setVisible(false);
        panPremiumDepreciationAcHead.setVisible(false);
        lblBrokerCommissionAcHead.setVisible(false);
        lblDividentPaidAcHead.setVisible(false);
        lblPremiumReceivedAcHead.setVisible(false);
        lblPremiumPaidAcHead.setVisible(false);
        lblPremiumDepreciationAcHead.setVisible(false);
        disDesc();
    }
    public void disDesc()
    {
        txtInvestmentAcHeadDesc.setEnabled(false);
        txtIntPaidAcHeadDesc.setEnabled(false);
        txtIntReceivedAcHeadDesc.setEnabled(false);
        txtPremiumPaidAcHeadDesc.setEnabled(false);
        txtPremiumDepreciationAcHeadDesc.setEnabled(false);
        txtBrokerCommissionAcHeadDesc.setEnabled(false);
        txtDividentReceivedAcHeadDesc.setEnabled(false);
        txtChargeAcHeadDesc.setEnabled(false);
        txtInterestReceivableAcHeadDesc.setEnabled(false);
        txtServiceTaxAcHeadDesc.setEnabled(false);
        txtDividentPaidAcHeadDesc.setEnabled(false);
        txtPremiumReceivedAcHeadDesc.setEnabled(false);
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
        btnPremiumPaidAcHead.setName("btnPremiumPaidAcHead");
        btnPremiumDepreciationAcHead.setName("btnPremiumDepreciationAcHead");
        btnBrokerCommissionAcHead.setName("btnBrokerCommissionAcHead");
        btnDividentReceivedAcHead.setName("btnDividentReceivedAcHead");
        btnServiceTaxAcHead.setName("btnServiceTaxAcHead");
        btnDividentPaidAcHead.setName("btnDividentPaidAcHead");
        lblInvestmentAcHead.setName("lblInvestmentAcHead");
        lblIntReceivedAcHead.setName("lblIntReceivedAcHead");
        lblChargeAcHead.setName("lblChargeAcHead");
        lblInterestReceivableAcHead.setName("lblInterestReceivableAcHead");
        lblIntPaidAcHead.setName("lblIntPaidAcHead");
        lblPremiumPaidAcHead.setName("lblPremiumPaidAcHead");
        lblPremiumDepreciationAcHead.setName("lblPremiumDepreciationAcHead");
        lblBrokerCommissionAcHead.setName("lblBrokerCommissionAcHead");
        lblDividentReceivedAcHead.setName("lblDividentReceivedAcHead");
        lblServiceTaxAcHead.setName("lblServiceTaxAcHead");
        lblDividentPaidAcHead.setName("lblDividentPaidAcHead");
        lblInvestmentBehaves.setName("lblInvestmentBehaves");
        lblPremiumReceivedAcHead.setName("lblPremiumReceivedAcHead");
        lblProductID.setName("lblProductID");
        lblDesc.setName("lblDesc");
        cboInvestmentBehaves.setName("cboInvestmentBehaves");
        txtProductID.setName("txtProductID");
        txtDesc.setName("txtDesc");
        txtInvestmentAcHead.setName("txtInvestmentAcHead");
        txtIntReceivedAcHead.setName("txtIntReceivedAcHead");
        txtIntPaidAcHead.setName("txtIntPaidAcHead");
        txtChargeAcHead.setName("txtChargeAcHead");
        txtInterestReceivableAcHead.setName("txtInterestReceivableAcHead");
        txtPremiumPaidAcHead.setName("txtPremiumPaidAcHead");
        txtPremiumDepreciationAcHead.setName("txtPremiumDepreciationAcHead");
        txtBrokerCommissionAcHead.setName("txtBrokerCommissionAcHead");
        txtDividentReceivedAcHead.setName("txtDividentReceivedAcHead");
        txtServiceTaxAcHead.setName("txtServiceTaxAcHead");
        txtDividentPaidAcHead.setName("txtDividentPaidAcHead");
        txtPremiumReceivedAcHead.setName("txtPremiumReceivedAcHead");
        lblTDSAcHead.setName("lblTDSAcHead");
        panTDSAcHead.setName("panTDSAcHead");
        txtTDSAcHead.setName("txtTDSAcHead");
        btnTDSAcHead.setName("btnTDSAcHead");
        txtTDSAcHeadDesc.setName("txtTDSAcHeadDesc");
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
        lblPremiumPaidAcHead.setText(resourceBundle.getString("lblPremiumPaidAcHead"));
        lblPremiumReceivedAcHead.setText(resourceBundle.getString("lblPremiumReceivedAcHead"));
        
        lblPremiumDepreciationAcHead.setText(resourceBundle.getString("lblPremiumDepreciationAcHead"));
        lblBrokerCommissionAcHead.setText(resourceBundle.getString("lblBrokerCommissionAcHead"));
        lblDividentReceivedAcHead.setText(resourceBundle.getString("lblDividentReceivedAcHead"));
        lblServiceTaxAcHead.setText(resourceBundle.getString("lblServiceTaxAcHead"));
        lblDividentPaidAcHead.setText(resourceBundle.getString("lblTransServiceTaxAcHead"));     
        lblInvestmentBehaves.setText(resourceBundle.getString("lblInvestmentBehaves"));
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
        mandatoryMap.put("txtIntPaidAcHead", new Boolean(true));
        mandatoryMap.put("txtChargeAcHead", new Boolean(false));
        mandatoryMap.put("txtInterestReceivableAcHead", new Boolean(true));
        mandatoryMap.put("txtPremiumPaidAcHead", new Boolean(false));
        mandatoryMap.put("txtPremiumDepreciationAcHead", new Boolean(false));
        mandatoryMap.put("txtBrokerCommissionAcHead", new Boolean(false));
        mandatoryMap.put("txtDividentReceivedAcHead", new Boolean(false));
        mandatoryMap.put("txtServiceTaxAcHead", new Boolean(false));
        mandatoryMap.put("txtTransServiceTaxAcHead", new Boolean(false));
        mandatoryMap.put("lblPremiumReceivedAcHead",new Boolean(false));
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
            observable = InvestmentsProductOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboInvestmentBehaves.setModel(observable.getCbmInvestmentBehaves());
            
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
        cboInvestmentBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInvestmentBehaves"));
        txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
        txtDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesc"));
        txtInvestmentAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentAcHead"));
        txtIntReceivedAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntReceivedAcHead"));
        txtIntPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntPaidAcHead"));
        txtChargeAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeAcHead"));
        txtInterestReceivableAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestReceivableAcHead"));
        txtPremiumPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumPaidAcHead"));
        txtPremiumDepreciationAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumDepreciationAcHead"));
        txtBrokerCommissionAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBrokerCommissionAcHead"));
        txtDividentReceivedAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDividentReceivedAcHead"));
        txtServiceTaxAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxAcHead"));
        txtServiceTaxAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxAcHead"));
        txtDividentPaidAcHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDividentPaidAcHead"));
        
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
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtProductID.setText(observable.getTxtProductID());
        if(observable.getChkRenewalWithoutTransaction().equals("Y")){
          chkRenewalWithoutTransaction.setSelected(true);  
        }else{
          chkRenewalWithoutTransaction.setSelected(false);    
        }
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
        txtPremiumPaidAcHead.setText(observable.getTxtPremiumPaidAcHead());
        if(!txtPremiumPaidAcHead.getText().equals(""))
        {
          txtPremiumPaidAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumPaidAcHead())); 
         // 
        }
        txtPremiumDepreciationAcHead.setText(observable.getTxtPremiumDepreciationAcHead());
         if(!txtPremiumDepreciationAcHead.getText().equals(""))
        {
          txtPremiumDepreciationAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumDepreciationAcHead())); 
          //
        }
        txtBrokerCommissionAcHead.setText(observable.getTxtBrokerCommissionAcHead());
        if(!txtBrokerCommissionAcHead.getText().equals(""))
        {
          txtBrokerCommissionAcHeadDesc.setText(getAccHdDesc(observable.getTxtBrokerCommissionAcHead())); 
         //
        }
        txtDividentReceivedAcHead.setText(observable.getTxtDividentReceivedAcHead());
         if(!txtDividentReceivedAcHead.getText().equals(""))
        {
          txtDividentReceivedAcHeadDesc.setText(getAccHdDesc(observable.getTxtDividentReceivedAcHead())); 
         //
        }
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
        txtServiceTaxAcHead.setText(observable.getTxtServiceTaxAcHead());
        if(!txtServiceTaxAcHead.getText().equals(""))
        {
          txtServiceTaxAcHeadDesc.setText(getAccHdDesc(observable.getTxtServiceTaxAcHead())); 
          //
        }
        txtDividentPaidAcHead.setText(observable.getTxtDividentPaidAcHead());
        if(!txtDividentPaidAcHead.getText().equals(""))
        {
          txtDividentPaidAcHeadDesc.setText(getAccHdDesc(observable.getTxtDividentPaidAcHead())); 
          //
        }
        txtPremiumReceivedAcHead.setText(observable.getTxtPremiumReceivedAcHead());
        if(!txtPremiumReceivedAcHead.getText().equals(""))
        {
          txtPremiumReceivedAcHeadDesc.setText(getAccHdDesc(observable.getTxtPremiumReceivedAcHead())); 
          //
        }
        txtTDSAcHead.setText(observable.getTxtTDSAcHead());
        if(!txtTDSAcHead.getText().equals(""))
        {
          txtTDSAcHeadDesc.setText(getAccHdDesc(observable.getTxtTDSAcHead())); 
          //
        }
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setTxtProductID(CommonUtil.convertObjToStr(txtProductID.getText()));
        if(chkRenewalWithoutTransaction.isSelected()){
            observable.setChkRenewalWithoutTransaction("Y");
        }else{
            observable.setChkRenewalWithoutTransaction("N");
        }
        observable.setTxtDesc(CommonUtil.convertObjToStr(txtDesc.getText()));
        observable.setTxtInvestmentAcHead(CommonUtil.convertObjToStr(txtInvestmentAcHead.getText()));
        observable.setTxtIntPaidAcHead(CommonUtil.convertObjToStr(txtIntPaidAcHead.getText()));
        observable.setTxtIntReceivedAcHead(CommonUtil.convertObjToStr(txtIntReceivedAcHead.getText()));
//        observable.setTxtIntPaidAcHead(CommonUtil.convertObjToStr(txtIntPaidAcHead.getText()));
        observable.setTxtPremiumPaidAcHead(CommonUtil.convertObjToStr(txtPremiumPaidAcHead.getText()));
        observable.setTxtPremiumDepreciationAcHead(CommonUtil.convertObjToStr(txtPremiumDepreciationAcHead.getText()));
        observable.setTxtBrokerCommissionAcHead(CommonUtil.convertObjToStr(txtBrokerCommissionAcHead.getText()));
        observable.setTxtDividentReceivedAcHead(CommonUtil.convertObjToStr(txtDividentReceivedAcHead.getText()));
        observable.setTxtChargeAcHead(CommonUtil.convertObjToStr(txtChargeAcHead.getText()));
        observable.setTxtInterestReceivableAcHead(CommonUtil.convertObjToStr(txtInterestReceivableAcHead.getText()));
        observable.setTxtServiceTaxAcHead(CommonUtil.convertObjToStr(txtServiceTaxAcHead.getText()));
        observable.setTxtDividentPaidAcHead(CommonUtil.convertObjToStr(txtDividentPaidAcHead.getText()));
        observable.setTxtPremiumReceivedAcHead(CommonUtil.convertObjToStr(txtPremiumReceivedAcHead.getText()));
        observable.setTxtTDSAcHead(CommonUtil.convertObjToStr(txtTDSAcHead.getText()));
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
        txtDesc.setMaxLength(25);
        txtInvestmentAcHead.setMaxLength(16);
        txtIntPaidAcHead.setMaxLength(16);
        txtIntReceivedAcHead.setMaxLength(16);
        txtPremiumDepreciationAcHead.setMaxLength(16);
        txtPremiumPaidAcHead.setMaxLength(16);
        txtBrokerCommissionAcHead.setMaxLength(16);
        txtDividentReceivedAcHead.setMaxLength(16);
        txtServiceTaxAcHead.setMaxLength(16);
        txtDividentPaidAcHead.setMaxLength(16);
        txtProductID.setAllowAll(true);
        txtDesc.setAllowAll(true);
        txtInvestmentAcHead.setAllowAll(true);
        txtIntReceivedAcHead.setAllowAll(true);
        txtIntPaidAcHead.setAllowAll(true);
        txtDividentReceivedAcHead.setAllowAll(true);
        txtServiceTaxAcHead.setAllowAll(true);
        txtChargeAcHead.setAllowAll(true);
        txtInterestReceivableAcHead.setAllowAll(true);
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        btnInvestmentAcHead.setEnabled(enable);
        btnIntPaidAcHead.setEnabled(enable);
        btnIntReceivedAcHead.setEnabled(enable);
        btnDividentReceivedAcHead.setEnabled(enable);
        btnPremiumDepreciationAcHead.setEnabled(enable);
        btnPremiumPaidAcHead.setEnabled(enable);
        btnBrokerCommissionAcHead.setEnabled(enable);
        btnServiceTaxAcHead.setEnabled(enable);
        btnChargeAcHead.setEnabled(enable);
        btnInterestReceivableAcHead.setEnabled(enable);
        btnDividentPaidAcHead.setEnabled(enable);    
        btnPremiumReceivedAcHead.setEnabled(enable); 
        btnTDSAcHead.setEnabled(enable);
        cboInvestmentBehaves.setEnabled(enable);
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
//            final String mandatoryMessage = checkMandatory(panShareProduct);
//            String alertMsg ="";
//            if(mandatoryMessage.length() > 0 ){
//                displayAlert(mandatoryMessage);
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panShareProduct);
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                observable.execute(status);
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
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
            lst.add("INVESTMENT_TYPE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
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
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_PROD_ID"));
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
                    txtPremiumPaidAcHeadDesc.setEnabled(false);
                    txtPremiumDepreciationAcHeadDesc.setEnabled(false);
                    txtBrokerCommissionAcHeadDesc.setEnabled(false);
                    txtDividentReceivedAcHeadDesc.setEnabled(false);
                    txtChargeAcHeadDesc.setEnabled(false);
                    txtInterestReceivableAcHeadDesc.setEnabled(false);
                    txtServiceTaxAcHeadDesc.setEnabled(false);
                    txtDividentPaidAcHeadDesc.setEnabled(false);
                    txtPremiumReceivedAcHeadDesc.setEnabled(false);
                    txtTDSAcHeadDesc.setEnabled(false);                
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
            } else if(viewType.equals("PremiumPaid")){
                txtPremiumPaidAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtPremiumPaidAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtPremiumPaidAcHeadDesc.setEnabled(false);
            } else if(viewType.equals("PremiumDepreciation")){
                txtPremiumDepreciationAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtPremiumDepreciationAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtPremiumDepreciationAcHeadDesc.setEnabled(false);
            }
            else if(viewType.equals("BrokerCommission")){
                txtBrokerCommissionAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtBrokerCommissionAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtBrokerCommissionAcHeadDesc.setEnabled(false);
            }
            else if(viewType.equals("DividentReceived")){
                txtDividentReceivedAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtDividentReceivedAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtDividentReceivedAcHeadDesc.setEnabled(false);
            }
            else if(viewType.equals("ServiceTax")){
                txtServiceTaxAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtServiceTaxAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtServiceTaxAcHeadDesc.setEnabled(false);
            } else if(viewType.equals("TransServiceTax")){
                txtDividentPaidAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtDividentPaidAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtDividentPaidAcHeadDesc.setEnabled(false);
            }
            else if(viewType.equals("PremiumReceived")){
                txtPremiumReceivedAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtPremiumReceivedAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtPremiumReceivedAcHeadDesc.setEnabled(false);
            }else if(viewType.equals("ChargePaid")){
                txtChargeAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtChargeAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtChargeAcHeadDesc.setEnabled(false);
            }else if(viewType.equals("InterestReceivable")){
                txtInterestReceivableAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtInterestReceivableAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtInterestReceivableAcHeadDesc.setEnabled(false);
            } else if(viewType.equals("TDS")){
                txtTDSAcHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtTDSAcHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtTDSAcHeadDesc.setEnabled(false);
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
            mapParam.put(CommonConstants.MAP_NAME, "getInvestmentProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeInvestmentProduct");
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
            singleAuthorizeMap.put("INVESTMENT_PROD_ID",CommonUtil.convertObjToStr(txtProductID.getText()));
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            
            ClientUtil.execute("authorizeInvestmentProduct", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        InvestmentsProductUI ui = new InvestmentsProductUI();
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
        lblInvestmentBehaves = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        lblDesc = new com.see.truetransact.uicomponent.CLabel();
        txtDesc = new com.see.truetransact.uicomponent.CTextField();
        chkRenewalWithoutTransaction = new com.see.truetransact.uicomponent.CCheckBox();
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
        panPremiumPaidAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtPremiumPaidAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnPremiumPaidAcHead = new com.see.truetransact.uicomponent.CButton();
        txtPremiumPaidAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblPremiumPaidAcHead = new com.see.truetransact.uicomponent.CLabel();
        panPremiumDepreciationAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtPremiumDepreciationAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnPremiumDepreciationAcHead = new com.see.truetransact.uicomponent.CButton();
        txtPremiumDepreciationAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblPremiumDepreciationAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblDividentReceivedAcHead = new com.see.truetransact.uicomponent.CLabel();
        panBrokerCommissionAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtBrokerCommissionAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnBrokerCommissionAcHead = new com.see.truetransact.uicomponent.CButton();
        txtBrokerCommissionAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblBrokerCommissionAcHead = new com.see.truetransact.uicomponent.CLabel();
        panDividentReceivedAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtDividentReceivedAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnDividentReceivedAcHead = new com.see.truetransact.uicomponent.CButton();
        txtDividentReceivedAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        panServiceTaxAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtServiceTaxAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnServiceTaxAcHead = new com.see.truetransact.uicomponent.CButton();
        txtServiceTaxAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTaxAcHead = new com.see.truetransact.uicomponent.CLabel();
        panDividentPaidAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtDividentPaidAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnDividentPaidAcHead = new com.see.truetransact.uicomponent.CButton();
        txtDividentPaidAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblDividentPaidAcHead = new com.see.truetransact.uicomponent.CLabel();
        panPremiumReceivedAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtPremiumReceivedAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnPremiumReceivedAcHead = new com.see.truetransact.uicomponent.CButton();
        txtPremiumReceivedAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblPremiumReceivedAcHead = new com.see.truetransact.uicomponent.CLabel();
        panChargeAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtChargeAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnChargeAcHead = new com.see.truetransact.uicomponent.CButton();
        txtChargeAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblChargeAcHead = new com.see.truetransact.uicomponent.CLabel();
        panInterestReceivableAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtInterestReceivableAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnInterestReceivableAcHead = new com.see.truetransact.uicomponent.CButton();
        txtInterestReceivableAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInterestReceivableAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblTDSAcHead = new com.see.truetransact.uicomponent.CLabel();
        panTDSAcHead = new com.see.truetransact.uicomponent.CPanel();
        txtTDSAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnTDSAcHead = new com.see.truetransact.uicomponent.CButton();
        txtTDSAcHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        tbrShareProduct = new javax.swing.JToolBar();
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
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
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

        lblInvestmentBehaves.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblInvestmentBehaves, gridBagConstraints);

        cboInvestmentBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInvestmentBehaves.setNextFocusableComponent(txtInvestmentAcHead);
        cboInvestmentBehaves.setPopupWidth(220);
        cboInvestmentBehaves.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboInvestmentBehavesItemStateChanged(evt);
            }
        });
        cboInvestmentBehaves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInvestmentBehavesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(cboInvestmentBehaves, gridBagConstraints);

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

        chkRenewalWithoutTransaction.setText("Renewal Without Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panShare.add(chkRenewalWithoutTransaction, gridBagConstraints);

        tabShareProduct.addTab("Investments", panShare);

        panAccountDetails.setMinimumSize(new java.awt.Dimension(408, 358));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(408, 358));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panAcctDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAcctDet.setMinimumSize(new java.awt.Dimension(700, 620));
        panAcctDet.setPreferredSize(new java.awt.Dimension(700, 620));
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
        btnInvestmentAcHead.setEnabled(false);
        btnInvestmentAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
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

        lblInvestmentAcHead.setText("Investment Account Head");
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
        btnIntPaidAcHead.setEnabled(false);
        btnIntPaidAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntPaidAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
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

        panPremiumPaidAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panPremiumPaidAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panPremiumPaidAcHead.setLayout(new java.awt.GridBagLayout());

        txtPremiumPaidAcHead.setEditable(false);
        txtPremiumPaidAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPremiumPaidAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtPremiumPaidAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPremiumPaidAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panPremiumPaidAcHead.add(txtPremiumPaidAcHead, gridBagConstraints);

        btnPremiumPaidAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPremiumPaidAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPremiumPaidAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPremiumPaidAcHead.setEnabled(false);
        btnPremiumPaidAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPremiumPaidAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panPremiumPaidAcHead.add(btnPremiumPaidAcHead, gridBagConstraints);

        txtPremiumPaidAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPremiumPaidAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panPremiumPaidAcHead.add(txtPremiumPaidAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panPremiumPaidAcHead, gridBagConstraints);

        lblPremiumPaidAcHead.setText("Premium paid a/c");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblPremiumPaidAcHead, gridBagConstraints);

        panPremiumDepreciationAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panPremiumDepreciationAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panPremiumDepreciationAcHead.setLayout(new java.awt.GridBagLayout());

        txtPremiumDepreciationAcHead.setEditable(false);
        txtPremiumDepreciationAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPremiumDepreciationAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtPremiumDepreciationAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPremiumDepreciationAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panPremiumDepreciationAcHead.add(txtPremiumDepreciationAcHead, gridBagConstraints);

        btnPremiumDepreciationAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPremiumDepreciationAcHead.setEnabled(false);
        btnPremiumDepreciationAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPremiumDepreciationAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPremiumDepreciationAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPremiumDepreciationAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panPremiumDepreciationAcHead.add(btnPremiumDepreciationAcHead, gridBagConstraints);

        txtPremiumDepreciationAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPremiumDepreciationAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panPremiumDepreciationAcHead.add(txtPremiumDepreciationAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panPremiumDepreciationAcHead, gridBagConstraints);

        lblPremiumDepreciationAcHead.setText("Premium depreciation a/c (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblPremiumDepreciationAcHead, gridBagConstraints);

        lblDividentReceivedAcHead.setText("Divident Received A/c (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblDividentReceivedAcHead, gridBagConstraints);

        panBrokerCommissionAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panBrokerCommissionAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panBrokerCommissionAcHead.setLayout(new java.awt.GridBagLayout());

        txtBrokerCommissionAcHead.setEditable(false);
        txtBrokerCommissionAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBrokerCommissionAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtBrokerCommissionAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBrokerCommissionAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panBrokerCommissionAcHead.add(txtBrokerCommissionAcHead, gridBagConstraints);

        btnBrokerCommissionAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBrokerCommissionAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBrokerCommissionAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBrokerCommissionAcHead.setEnabled(false);
        btnBrokerCommissionAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrokerCommissionAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBrokerCommissionAcHead.add(btnBrokerCommissionAcHead, gridBagConstraints);

        txtBrokerCommissionAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtBrokerCommissionAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panBrokerCommissionAcHead.add(txtBrokerCommissionAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panBrokerCommissionAcHead, gridBagConstraints);

        lblBrokerCommissionAcHead.setText("Broker commission paid a/c (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblBrokerCommissionAcHead, gridBagConstraints);

        panDividentReceivedAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panDividentReceivedAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panDividentReceivedAcHead.setLayout(new java.awt.GridBagLayout());

        txtDividentReceivedAcHead.setEditable(false);
        txtDividentReceivedAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDividentReceivedAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtDividentReceivedAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDividentReceivedAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panDividentReceivedAcHead.add(txtDividentReceivedAcHead, gridBagConstraints);

        btnDividentReceivedAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDividentReceivedAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDividentReceivedAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDividentReceivedAcHead.setEnabled(false);
        btnDividentReceivedAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividentReceivedAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDividentReceivedAcHead.add(btnDividentReceivedAcHead, gridBagConstraints);

        txtDividentReceivedAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtDividentReceivedAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panDividentReceivedAcHead.add(txtDividentReceivedAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panDividentReceivedAcHead, gridBagConstraints);

        panServiceTaxAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panServiceTaxAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panServiceTaxAcHead.setLayout(new java.awt.GridBagLayout());

        txtServiceTaxAcHead.setEditable(false);
        txtServiceTaxAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTaxAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtServiceTaxAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panServiceTaxAcHead.add(txtServiceTaxAcHead, gridBagConstraints);

        btnServiceTaxAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnServiceTaxAcHead.setEnabled(false);
        btnServiceTaxAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnServiceTaxAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnServiceTaxAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTaxAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panServiceTaxAcHead.add(btnServiceTaxAcHead, gridBagConstraints);

        txtServiceTaxAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtServiceTaxAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panServiceTaxAcHead.add(txtServiceTaxAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panServiceTaxAcHead, gridBagConstraints);

        lblServiceTaxAcHead.setText("Service Tax A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblServiceTaxAcHead, gridBagConstraints);

        panDividentPaidAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panDividentPaidAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panDividentPaidAcHead.setLayout(new java.awt.GridBagLayout());

        txtDividentPaidAcHead.setEditable(false);
        txtDividentPaidAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDividentPaidAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtDividentPaidAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDividentPaidAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panDividentPaidAcHead.add(txtDividentPaidAcHead, gridBagConstraints);

        btnDividentPaidAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDividentPaidAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDividentPaidAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDividentPaidAcHead.setEnabled(false);
        btnDividentPaidAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividentPaidAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDividentPaidAcHead.add(btnDividentPaidAcHead, gridBagConstraints);

        txtDividentPaidAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtDividentPaidAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panDividentPaidAcHead.add(txtDividentPaidAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panDividentPaidAcHead, gridBagConstraints);

        lblDividentPaidAcHead.setText("DividentPaidAcHead (P & L)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblDividentPaidAcHead, gridBagConstraints);

        panPremiumReceivedAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panPremiumReceivedAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panPremiumReceivedAcHead.setLayout(new java.awt.GridBagLayout());

        txtPremiumReceivedAcHead.setEditable(false);
        txtPremiumReceivedAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPremiumReceivedAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtPremiumReceivedAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPremiumReceivedAcHeadActionPerformed(evt);
            }
        });
        txtPremiumReceivedAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPremiumReceivedAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panPremiumReceivedAcHead.add(txtPremiumReceivedAcHead, gridBagConstraints);

        btnPremiumReceivedAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPremiumReceivedAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPremiumReceivedAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPremiumReceivedAcHead.setEnabled(false);
        btnPremiumReceivedAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPremiumReceivedAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panPremiumReceivedAcHead.add(btnPremiumReceivedAcHead, gridBagConstraints);

        txtPremiumReceivedAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 20));
        txtPremiumReceivedAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panPremiumReceivedAcHead.add(txtPremiumReceivedAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panPremiumReceivedAcHead, gridBagConstraints);

        lblPremiumReceivedAcHead.setText("Premium received a/c");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblPremiumReceivedAcHead, gridBagConstraints);

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
        btnChargeAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChargeAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChargeAcHead.setEnabled(false);
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

        lblChargeAcHead.setText("Charge Paid A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblChargeAcHead, gridBagConstraints);

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
        btnInterestReceivableAcHead.setEnabled(false);
        btnInterestReceivableAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInterestReceivableAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
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

        lblTDSAcHead.setText("TDS A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAcctDet.add(lblTDSAcHead, gridBagConstraints);

        panTDSAcHead.setMinimumSize(new java.awt.Dimension(300, 29));
        panTDSAcHead.setPreferredSize(new java.awt.Dimension(300, 29));
        panTDSAcHead.setLayout(new java.awt.GridBagLayout());

        txtTDSAcHead.setEditable(false);
        txtTDSAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTDSAcHead.setNextFocusableComponent(btnInvestmentAcHead);
        txtTDSAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTDSAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panTDSAcHead.add(txtTDSAcHead, gridBagConstraints);

        btnTDSAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTDSAcHead.setEnabled(false);
        btnTDSAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTDSAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTDSAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTDSAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panTDSAcHead.add(btnTDSAcHead, gridBagConstraints);

        txtTDSAcHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtTDSAcHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panTDSAcHead.add(txtTDSAcHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctDet.add(panTDSAcHead, gridBagConstraints);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace33);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace34);

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

        mbrShareProduct.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
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

    private void txtPremiumDepreciationAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPremiumDepreciationAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtPremiumDepreciationAcHead.getText().equals(""))
        {
          txtPremiumDepreciationAcHeadDesc.setText(getAccHdDesc(txtPremiumDepreciationAcHead.getText())); 
        }
    }//GEN-LAST:event_txtPremiumDepreciationAcHeadFocusLost

    private void txtPremiumPaidAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPremiumPaidAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtPremiumPaidAcHead.getText().equals(""))
        {
          txtPremiumPaidAcHeadDesc.setText(getAccHdDesc(txtPremiumPaidAcHead.getText())); 
        }
    }//GEN-LAST:event_txtPremiumPaidAcHeadFocusLost

    private void txtPremiumReceivedAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPremiumReceivedAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtPremiumReceivedAcHead.getText().equals(""))
        {
          txtPremiumReceivedAcHeadDesc.setText(getAccHdDesc(txtPremiumReceivedAcHead.getText())); 
        }
    }//GEN-LAST:event_txtPremiumReceivedAcHeadFocusLost

    private void txtDividentPaidAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDividentPaidAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtDividentPaidAcHead.getText().equals(""))
        {
          txtDividentPaidAcHeadDesc.setText(getAccHdDesc(txtDividentPaidAcHead.getText())); 
        }
    }//GEN-LAST:event_txtDividentPaidAcHeadFocusLost

    private void txtBrokerCommissionAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerCommissionAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtBrokerCommissionAcHead.getText().equals(""))
        {
          txtBrokerCommissionAcHeadDesc.setText(getAccHdDesc(txtBrokerCommissionAcHead.getText())); 
        }
    }//GEN-LAST:event_txtBrokerCommissionAcHeadFocusLost

    private void txtServiceTaxAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxAcHeadFocusLost
        // TODO add your handling code here:
        if(!txtServiceTaxAcHead.getText().equals(""))
        {
          txtServiceTaxAcHeadDesc.setText(getAccHdDesc(txtServiceTaxAcHead.getText())); 
        }
    }//GEN-LAST:event_txtServiceTaxAcHeadFocusLost

    private void txtDividentReceivedAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDividentReceivedAcHeadFocusLost
        // TODO add your handling code here:
         if(!txtDividentReceivedAcHead.getText().equals(""))
        {
          txtDividentReceivedAcHeadDesc.setText(getAccHdDesc(txtDividentReceivedAcHead.getText())); 
        }
    }//GEN-LAST:event_txtDividentReceivedAcHeadFocusLost

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

    private void btnPremiumReceivedAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPremiumReceivedAcHeadActionPerformed
        // TODO add your handling code here:
          callView("PremiumReceived");
    }//GEN-LAST:event_btnPremiumReceivedAcHeadActionPerformed

    private void txtPremiumReceivedAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPremiumReceivedAcHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPremiumReceivedAcHeadActionPerformed

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            boolean exists = false;
            exists = observable.isInvsetMentTypeExists(CommonUtil.convertObjToStr(txtProductID.getText()));
            if (exists == true) {
                displayAlert("Already This Prod Id Exists");
                txtProductID.setText("");
                txtProductID.requestFocus();
            } else { // Added by nithya on 19-05-2016
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
    
    private void btnDividentPaidAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDividentPaidAcHeadActionPerformed
        // TODO add your handling code here:
        callView("TransServiceTax");
    }//GEN-LAST:event_btnDividentPaidAcHeadActionPerformed
    
    private void btnServiceTaxAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTaxAcHeadActionPerformed
        // TODO add your handling code here:
        callView("ServiceTax");
    }//GEN-LAST:event_btnServiceTaxAcHeadActionPerformed
    
    private void btnDividentReceivedAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDividentReceivedAcHeadActionPerformed
        // TODO add your handling code here:
        callView("DividentReceived");
    }//GEN-LAST:event_btnDividentReceivedAcHeadActionPerformed
    
    private void btnBrokerCommissionAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrokerCommissionAcHeadActionPerformed
        // TODO add your handling code here:
        callView("BrokerCommission");
    }//GEN-LAST:event_btnBrokerCommissionAcHeadActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnPremiumDepreciationAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPremiumDepreciationAcHeadActionPerformed
        // TODO add your handling code here:
        callView("PremiumDepreciation");
    }//GEN-LAST:event_btnPremiumDepreciationAcHeadActionPerformed
    
    private void btnPremiumPaidAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPremiumPaidAcHeadActionPerformed
        // TODO add your handling code here:
        callView("PremiumPaid");
    }//GEN-LAST:event_btnPremiumPaidAcHeadActionPerformed
    
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
    
    private void cboInvestmentBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesItemStateChanged
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboInvestmentBehavesItemStateChanged
    
    private void cboInvestmentBehavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesActionPerformed
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
                lblChargeAcHead.setText("Charge Paid A/c Head");
                if(beheaves.equals("OTHER_BANK_SB") || beheaves.equals("OTHER_BANK_CA") || beheaves.equals("OTHER_BANK_SPD")){
                    lblIntReceivedAcHead.setVisible(true);
                    lblChargeAcHead.setVisible(true);
                    panIntReceivedAcHead.setVisible(true);
                    panChargeAcHead.setVisible(true);
                    txtChargeAcHead.setEnabled(true);
                    txtIntReceivedAcHead.setEnabled(true);
                }
                if(beheaves.equals("OTHER_BANK_FD") || beheaves.equals("OTHER_BANK_CCD") || beheaves.equals("OTHER_BANK_RD") || beheaves.equals("OTHER_BANK_SSD")){
                    txtIntReceivedAcHead.setEnabled(true);
                    lblIntReceivedAcHead.setVisible(true);
                    panIntReceivedAcHead.setVisible(true);
                }
                if(beheaves.equals("OTHER_BANK_CCD")){
                    lblInterestReceivableAcHead.setVisible(true);
                    panInterestReceivableAcHead.setVisible(true);
                    txtInterestReceivableAcHead.setEnabled(true);
                }
                if(beheaves.equals("OTHER_BANK_RD")){
                    lblChargeAcHead.setVisible(true);
                    panChargeAcHead.setVisible(true);
                    txtChargeAcHead.setEnabled(true);
                }
                if(beheaves.equals("RESERVE_FUND_DCB")){
                    lblIntReceivedAcHead.setVisible(true);
                    panIntReceivedAcHead.setVisible(true);
                    txtIntReceivedAcHead.setEnabled(true);
                }
                if(beheaves.equals("SHARES_DCB") || beheaves.equals("SHARE_OTHER_INSTITUTIONS")){
                    lblChargeAcHead.setVisible(true);
                    panChargeAcHead.setVisible(true);
                    txtChargeAcHead.setEnabled(true);
                    txtIntReceivedAcHead.setEnabled(true);
                    txtDividentReceivedAcHead.setEnabled(true);
                    lblChargeAcHead.setText("Fees Paid A/c Head");
                    lblDividentReceivedAcHead.setVisible(true);
                    panDividentReceivedAcHead.setVisible(true);
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_cboInvestmentBehavesActionPerformed
    
    private void enableDisable(boolean flag){
        lblInvestmentAcHead.setVisible(flag);
        lblIntReceivedAcHead.setVisible(flag);
        lblInterestReceivableAcHead.setVisible(flag);
        lblIntPaidAcHead.setVisible(flag);
        lblChargeAcHead.setVisible(flag);
        lblDividentReceivedAcHead.setVisible(flag);
        lblServiceTaxAcHead.setVisible(flag);
        lblBrokerCommissionAcHead.setVisible(flag);
        lblDividentPaidAcHead.setVisible(flag);
        lblPremiumReceivedAcHead.setVisible(flag);
        lblPremiumPaidAcHead.setVisible(flag);
        lblPremiumDepreciationAcHead.setVisible(flag);
        panInvestmentAcHead.setVisible(flag);
        panIntReceivedAcHead.setVisible(flag);
        panInterestReceivableAcHead.setVisible(flag);
        panIntPaidAcHead.setVisible(flag);
        panChargeAcHead.setVisible(flag);
        panDividentReceivedAcHead.setVisible(flag);
        panServiceTaxAcHead.setVisible(flag);
        panBrokerCommissionAcHead.setVisible(flag);
        panDividentPaidAcHead.setVisible(flag);
        panPremiumReceivedAcHead.setVisible(flag);
        panPremiumPaidAcHead.setVisible(flag);
        panPremiumDepreciationAcHead.setVisible(flag);
        txtInvestmentAcHead.setEnabled(flag);
        txtIntReceivedAcHead.setEnabled(flag);
        txtInterestReceivableAcHead.setEnabled(flag);
        txtIntPaidAcHead.setEnabled(flag);
        txtChargeAcHead.setEnabled(flag);
        txtDividentReceivedAcHead.setEnabled(flag);
        txtServiceTaxAcHead.setEnabled(flag);
        txtBrokerCommissionAcHead.setEnabled(flag);
        txtDividentPaidAcHead.setEnabled(flag);
        txtPremiumReceivedAcHead.setEnabled(flag);
        txtPremiumPaidAcHead.setEnabled(flag);
        txtPremiumDepreciationAcHead.setEnabled(flag);
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
        super.removeEditLock(((ComboBoxModel)cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
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
        cboInvestmentBehaves.setEnabled(false);
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

    private void txtTDSAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTDSAcHeadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTDSAcHeadFocusLost

    private void btnTDSAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTDSAcHeadActionPerformed
        // TODO add your handling code here:
        callView("TDS");
    }//GEN-LAST:event_btnTDSAcHeadActionPerformed
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
    private com.see.truetransact.uicomponent.CButton btnBrokerCommissionAcHead;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargeAcHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDividentPaidAcHead;
    private com.see.truetransact.uicomponent.CButton btnDividentReceivedAcHead;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnIntPaidAcHead;
    private com.see.truetransact.uicomponent.CButton btnIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CButton btnInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CButton btnInvestmentAcHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPremiumDepreciationAcHead;
    private com.see.truetransact.uicomponent.CButton btnPremiumPaidAcHead;
    private com.see.truetransact.uicomponent.CButton btnPremiumReceivedAcHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnServiceTaxAcHead;
    private com.see.truetransact.uicomponent.CButton btnTDSAcHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentBehaves;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewalWithoutTransaction;
    private com.see.truetransact.uicomponent.CLabel lblBrokerCommissionAcHead;
    private com.see.truetransact.uicomponent.CLabel lblChargeAcHead;
    private com.see.truetransact.uicomponent.CLabel lblDesc;
    private com.see.truetransact.uicomponent.CLabel lblDividentPaidAcHead;
    private com.see.truetransact.uicomponent.CLabel lblDividentReceivedAcHead;
    private com.see.truetransact.uicomponent.CLabel lblIntPaidAcHead;
    private com.see.truetransact.uicomponent.CLabel lblIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CLabel lblInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentAcHead;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPremiumDepreciationAcHead;
    private com.see.truetransact.uicomponent.CLabel lblPremiumPaidAcHead;
    private com.see.truetransact.uicomponent.CLabel lblPremiumReceivedAcHead;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxAcHead;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTDSAcHead;
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
    private com.see.truetransact.uicomponent.CPanel panBrokerCommissionAcHead;
    private com.see.truetransact.uicomponent.CPanel panChargeAcHead;
    private com.see.truetransact.uicomponent.CPanel panDividentPaidAcHead;
    private com.see.truetransact.uicomponent.CPanel panDividentReceivedAcHead;
    private com.see.truetransact.uicomponent.CPanel panIntPaidAcHead;
    private com.see.truetransact.uicomponent.CPanel panIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CPanel panInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CPanel panInvestmentAcHead;
    private com.see.truetransact.uicomponent.CPanel panPremiumDepreciationAcHead;
    private com.see.truetransact.uicomponent.CPanel panPremiumPaidAcHead;
    private com.see.truetransact.uicomponent.CPanel panPremiumReceivedAcHead;
    private com.see.truetransact.uicomponent.CPanel panServiceTaxAcHead;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private com.see.truetransact.uicomponent.CPanel panShareProduct;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTDSAcHead;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareProduct;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CTextField txtBrokerCommissionAcHead;
    private com.see.truetransact.uicomponent.CTextField txtBrokerCommissionAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtChargeAcHead;
    private com.see.truetransact.uicomponent.CTextField txtChargeAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtDesc;
    private com.see.truetransact.uicomponent.CTextField txtDividentPaidAcHead;
    private com.see.truetransact.uicomponent.CTextField txtDividentPaidAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtDividentReceivedAcHead;
    private com.see.truetransact.uicomponent.CTextField txtDividentReceivedAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntPaidAcHead;
    private com.see.truetransact.uicomponent.CTextField txtIntPaidAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntReceivedAcHead;
    private com.see.truetransact.uicomponent.CTextField txtIntReceivedAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtInterestReceivableAcHead;
    private com.see.truetransact.uicomponent.CTextField txtInterestReceivableAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAcHead;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtPremiumDepreciationAcHead;
    private com.see.truetransact.uicomponent.CTextField txtPremiumDepreciationAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtPremiumPaidAcHead;
    private com.see.truetransact.uicomponent.CTextField txtPremiumPaidAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtPremiumReceivedAcHead;
    private com.see.truetransact.uicomponent.CTextField txtPremiumReceivedAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxAcHead;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxAcHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtTDSAcHead;
    private com.see.truetransact.uicomponent.CTextField txtTDSAcHeadDesc;
    // End of variables declaration//GEN-END:variables
    
}
