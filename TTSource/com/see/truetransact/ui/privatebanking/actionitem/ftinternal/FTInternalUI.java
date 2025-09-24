/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ActionItemUI.java
 *
 * Created on June 18, 2004, 12:08 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.ftinternal;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.ui.privatebanking.actionitem.ftinternal.FTInternalRB;
import com.see.truetransact.ui.privatebanking.actionitem.ftinternal.FTInternalOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.privatebanking.actionitem.ftinternal.FTInternalMRB;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;


import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;

public class FTInternalUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    
    private FTInternalRB resourceBundle = new FTInternalRB();
    private HashMap mandatoryMap;
    private FTInternalOB observable;
    private FTInternalMRB objMandatoryRB;
    private String viewType = "";
    private String authorizeType = "";
    private final String AUTHORIZE = "Authorize";
    
    
    /** Creates new form ActionItemUI */
    public FTInternalUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLengths();
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panActionItem, false);
        setButtonEnableDisable();
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
        lblMemberName.setName("lblMemberName");
        lblMemberNameValue.setText("lblMemberNameValue");
        lblAdviseType.setName("lblAdviseType");
        lblAdviseTypeValue.setName("lblAdviseTypeValue");
        lblBankOfficeInstructions.setName("lblBankOfficeInstructions");
        lblClientAdvices.setName("lblClientAdvices");
        lblClientContact.setName("lblClientContact");
        lblClientContactValue.setName("lblClientContactValue");
        lblContactDate.setName("lblContactDate");
        lblContactDateValue.setName("lblContactDateValue");
        lblContactMode.setName("lblContactMode");
        lblContactModeValue.setName("lblContactModeValue");
        lblCreditAccount.setName("lblCreditAccount");
        lblCreditAmount.setName("lblCreditAmount");
        lblCreditAssetSubClass.setName("lblCreditAssetSubClass");
        lblCreditEnititlementGroup.setName("lblCreditEnititlementGroup");
        lblCreditNotes.setName("lblCreditNotes");
        lblCreditPortfolioLocation.setName("lblCreditPortfolioLocation");
        lblDebitAccount.setName("lblDebitAccount");
        lblDebitAmount.setName("lblDebitAmount");
        lblDebitAssetSubClass.setName("lblDebitAssetSubClass");
        lblDebitEntitlementGroup.setName("lblDebitEntitlementGroup");
        lblDebitPortfolioLocation.setName("lblDebitPortfolioLocation");
        lblExecutionDate.setName("lblExecutionDate");
        lblLeadBanker.setName("lblLeadBanker");
        lblLeadBankerValue.setName("lblLeadBankerValue");
        lblLeadRSO.setName("lblLeadRSO");
        lblLeadRSOValue.setName("lblLeadRSOValue");
        lblMemberId.setName("lblMemberId");
        lblModeDetails.setName("lblModeDetails");
        lblModeDetailsValue.setName("lblModeDetailsValue");
        lblMsg.setName("lblMsg");
        lblOrderSource.setName("lblOrderSource");
        lblOrderSourceValue.setName("lblOrderSourceValue");
        lblReferenceNumber.setName("lblReferenceNumber");
        lblReferenceNumberValue.setName("lblReferenceNumberValue");
        lblRelationship.setName("lblRelationship");
        lblRelationshipValue.setName("lblRelationshipValue");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblTraderDealerInst.setName("lblTraderDealerInst");
        lblValueDate.setName("lblValueDate");
        mbrActionItem.setName("mbrActionItem");
        panActionDetails.setName("panActionDetails");
        panActionItem.setName("panActionItem");
        panCreditAccount.setName("panCreditAccount");
        panDebitAccount.setName("panDebitAccount");
        panInstAdvice.setName("panInstAdvice");
        panLabels.setName("panLabels");
        panOrderDetails.setName("panOrderDetails");
        panStatus.setName("panStatus");
        sptHorizonta.setName("sptHorizonta");
        tdtExecutionDate.setName("tdtExecutionDate");
        tdtValueDate.setName("tdtValueDate");
        txtBankOfficeInstruction.setName("txtBankOfficeInstruction");
        txtClientAdvices.setName("txtClientAdvices");
        txtCreditAccount.setName("txtCreditAccount");
        txtCreditAmount.setName("txtCreditAmount");
        txtCreditAssetSubClass.setName("txtCreditAssetSubClass");
        txtCreditEntitlementGroup.setName("txtCreditEntitlementGroup");
        txtCreditNotes.setName("txtCreditNotes");
        txtCreditPortfolioLocation.setName("txtCreditPortfolioLocation");
        txtDebitAccount.setName("txtDebitAccount");
        txtDebitAmount.setName("txtDebitAmount");
        txtDebitAssetSubClass.setName("txtDebitAssetSubClass");
        txtDebitEntitlementGroup.setName("txtDebitEntitlementGroup");
        txtDebitPortfolioLocation.setName("txtDebitPortfolioLocation");
        txtTraderDealerInst.setName("txtTraderDealerInst");
        txtMember.setName("txtMember");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        lblMemberNameValue.setText(resourceBundle.getString("lblMemberNameValue"));
        lblExecutionDate.setText(resourceBundle.getString("lblExecutionDate"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblContactMode.setText(resourceBundle.getString("lblContactMode"));
        lblLeadRSOValue.setText(resourceBundle.getString("lblLeadRSOValue"));
        lblAdviseTypeValue.setText(resourceBundle.getString("lblAdviseTypeValue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblOrderSourceValue.setText(resourceBundle.getString("lblOrderSourceValue"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        lblClientContactValue.setText(resourceBundle.getString("lblClientContactValue"));
        ((javax.swing.border.TitledBorder)panOrderDetails.getBorder()).setTitle(resourceBundle.getString("panOrderDetails"));
        lblCreditPortfolioLocation.setText(resourceBundle.getString("lblCreditPortfolioLocation"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        ((javax.swing.border.TitledBorder)panCreditAccount.getBorder()).setTitle(resourceBundle.getString("panCreditAccount"));
        lblValueDate.setText(resourceBundle.getString("lblValueDate"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblDebitEntitlementGroup.setText(resourceBundle.getString("lblDebitEntitlementGroup"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblDebitAccount.setText(resourceBundle.getString("lblDebitAccount"));
        lblReferenceNumber.setText(resourceBundle.getString("lblReferenceNumber"));
        lblCreditAccount.setText(resourceBundle.getString("lblCreditAccount"));
        lblLeadRSO.setText(resourceBundle.getString("lblLeadRSO"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblRelationshipValue.setText(resourceBundle.getString("lblRelationshipValue"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblLeadBanker.setText(resourceBundle.getString("lblLeadBanker"));
        lblCreditEnititlementGroup.setText(resourceBundle.getString("lblCreditEnititlementGroup"));
        lblLeadBankerValue.setText(resourceBundle.getString("lblLeadBankerValue"));
        lblDebitPortfolioLocation.setText(resourceBundle.getString("lblDebitPortfolioLocation"));
        lblAdviseType.setText(resourceBundle.getString("lblAdviseType"));
        lblCreditAssetSubClass.setText(resourceBundle.getString("lblCreditAssetSubClass"));
        lblTraderDealerInst.setText(resourceBundle.getString("lblTraderDealerInst"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblModeDetailsValue.setText(resourceBundle.getString("lblModeDetailsValue"));
        lblContactModeValue.setText(resourceBundle.getString("lblContactModeValue"));
        ((javax.swing.border.TitledBorder)panDebitAccount.getBorder()).setTitle(resourceBundle.getString("panDebitAccount"));
        lblContactDateValue.setText(resourceBundle.getString("lblContactDateValue"));
        lblBankOfficeInstructions.setText(resourceBundle.getString("lblBankOfficeInstructions"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblClientContact.setText(resourceBundle.getString("lblClientContact"));
        lblOrderSource.setText(resourceBundle.getString("lblOrderSource"));
        lblDebitAmount.setText(resourceBundle.getString("lblDebitAmount"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCreditNotes.setText(resourceBundle.getString("lblCreditNotes"));
        lblMemberId.setText(resourceBundle.getString("lblMemberId"));
        lblDebitAssetSubClass.setText(resourceBundle.getString("lblDebitAssetSubClass"));
        lblModeDetails.setText(resourceBundle.getString("lblModeDetails"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCreditAmount.setText(resourceBundle.getString("lblCreditAmount"));
        lblReferenceNumberValue.setText(resourceBundle.getString("lblReferenceNumberValue"));
        lblClientAdvices.setText(resourceBundle.getString("lblClientAdvices"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtCreditEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtCreditPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtCreditAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtCreditAccount", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("tdtValueDate", new Boolean(true));
        mandatoryMap.put("txtDebitAmount", new Boolean(true));
        mandatoryMap.put("txtCreditAmount", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("txtDebitEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtDebtiPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtDebitAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtDebitAccount", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new FTInternalMRB();
        txtMember.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMember"));
        txtCreditEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditEntitlementGroup"));
        txtCreditPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditPortfolioLocation"));
        txtCreditAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditAssetSubClass"));
        txtCreditAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditAccount"));
        tdtExecutionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecutionDate"));
        tdtValueDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtValueDate"));
        txtDebitAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAmount"));
        txtCreditAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditAmount"));
        txtBankOfficeInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankOfficeInstruction"));
        txtTraderDealerInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTraderDealerInst"));
        txtClientAdvices.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClientAdvices"));
        txtCreditNotes.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNotes"));
        txtDebitEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitEntitlementGroup"));
        txtDebitPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitPortfolioLocation"));
        txtDebitAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAssetSubClass"));
        txtDebitAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAccount"));
    }
    
    /** Setting the observable for FTInternalUI */
    private void setObservable(){
        try{
            observable = FTInternalOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            
        }
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        lblReferenceNumberValue.setText(observable.getLblReferenceNumber());
        txtMember.setText(observable.getTxtMember());
        txtCreditEntitlementGroup.setText(observable.getTxtCreditEntitlementGroup());
        txtCreditPortfolioLocation.setText(observable.getTxtCreditPortfolioLocation());
        txtCreditAssetSubClass.setText(observable.getTxtCreditAssetSubClass());
        txtCreditAccount.setText(observable.getTxtCreditAccount());
        tdtExecutionDate.setDateValue(observable.getTdtExecutionDate());
        tdtValueDate.setDateValue(observable.getTdtValueDate());
        txtDebitAmount.setText(observable.getTxtDebitAmount());
        txtCreditAmount.setText(observable.getTxtCreditAmount());
        txtBankOfficeInstruction.setText(observable.getTxtBankOfficeInstruction());
        txtTraderDealerInst.setText(observable.getTxtTraderDealerInst());
        txtClientAdvices.setText(observable.getTxtClientAdvices());
        txtCreditNotes.setText(observable.getTxtCreditNotes());
        txtDebitEntitlementGroup.setText(observable.getTxtDebitEntitlementGroup());
        txtDebitPortfolioLocation.setText(observable.getTxtDebitPortfolioLocation());
        txtDebitAssetSubClass.setText(observable.getTxtDebitAssetSubClass());
        txtDebitAccount.setText(observable.getTxtDebitAccount());
    }
    
 /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setLblReferenceNumber(lblReferenceNumberValue.getText());
        observable.setTxtMember(txtMember.getText());
        observable.setTxtCreditEntitlementGroup(txtCreditEntitlementGroup.getText());
        observable.setTxtCreditPortfolioLocation(txtCreditPortfolioLocation.getText());
        observable.setTxtCreditAssetSubClass(txtCreditAssetSubClass.getText());
        observable.setTxtCreditAccount(txtCreditAccount.getText());
        observable.setTdtExecutionDate(tdtExecutionDate.getDateValue());
        observable.setTdtValueDate(tdtValueDate.getDateValue());
        observable.setTxtDebitAmount(txtDebitAmount.getText());
        observable.setTxtCreditAmount(txtCreditAmount.getText());
        observable.setTxtBankOfficeInstruction(txtBankOfficeInstruction.getText());
        observable.setTxtTraderDealerInst(txtTraderDealerInst.getText());
        observable.setTxtClientAdvices(txtClientAdvices.getText());
        observable.setTxtCreditNotes(txtCreditNotes.getText());
        observable.setTxtDebitEntitlementGroup(txtDebitEntitlementGroup.getText());
        observable.setTxtDebitPortfolioLocation(txtDebitPortfolioLocation.getText());
        observable.setTxtDebitAssetSubClass(txtDebitAssetSubClass.getText());
        observable.setTxtDebitAccount(txtDebitAccount.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /** Enables or Disables the Button */
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
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            btnMember.setEnabled(btnNew.isEnabled());
            
        }
        else  if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            btnMember.setEnabled(!btnNew.isEnabled());
            
        }
        
        else{
            btnMember.setEnabled(!btnNew.isEnabled());
            
        }
        if(observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[2]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[3]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[1])){
            btnMember.setEnabled(!btnClose.isEnabled());
            
        }
    }
    
    /** Sets the Maximum Allowed Length to the TextFields in the UI */
    private void setMaxLengths(){
        txtDebitEntitlementGroup.setMaxLength(32);
        txtDebitPortfolioLocation.setMaxLength(32);
        txtDebitAssetSubClass.setMaxLength(32);
        txtDebitAccount.setMaxLength(32);
        txtCreditEntitlementGroup.setMaxLength(32);
        txtCreditPortfolioLocation.setMaxLength(32);
        txtCreditAssetSubClass.setMaxLength(32);
        txtCreditAccount.setMaxLength(32);
        txtDebitAmount.setMaxLength(16);
        txtCreditAmount.setMaxLength(16);
        txtBankOfficeInstruction.setMaxLength(1024);
        txtTraderDealerInst.setMaxLength(1024);
        txtClientAdvices.setMaxLength(1024);
        txtCreditNotes.setMaxLength(1024);
        
    }
    
    /** This method shows the ViewAll Screen with the rows filled up according to query executed */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtAIFTInternal");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtOrderMaster");
        }
        new ViewAll(this,viewMap).show();
        viewMap = null;
    }
    
    /** This method fill up the uifields with row seleced in the view all Screen */
    public void fillData(Object  map) {
        try{
            HashMap hash = (HashMap) map;
            final String member="ORD_ID";
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) ) {
                    hash.put(CommonConstants.MAP_WHERE, hash.get("REF_NO"));
                    observable.populateData(hash);
                    hash = null;
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panActionItem, false);
                    } else {
                        ClientUtil.enableDisable(panActionItem, true);
                    }
                    setButtonEnableDisable();
                }else if(viewType.equals("Member")){
                    txtMember.setText( CommonUtil.convertObjToStr(hash.get(member)));
                }
                String where = txtMember.getText();
                HashMap lblMap = observable.getLabelMap(where);
                if(lblMap != null){
                    lblMemberNameValue.setText(CommonUtil.convertObjToStr(lblMap.get("MEMBER")));
                    lblRelationshipValue.setText(CommonUtil.convertObjToStr(lblMap.get("MEMBER RELATION")));
                    lblContactModeValue.setText(CommonUtil.convertObjToStr(lblMap.get("CONTACT_MODE")));
                    lblContactDateValue.setText(DateUtil.getStringDate((Date)lblMap.get("CONTACT_DT")));
                    lblClientContactValue.setText(CommonUtil.convertObjToStr(lblMap.get("CLIENT_CONTACT")));
                }
                lblMap = null;
                
            }
        }catch(Exception e){
            
        }
    }
    
    /** This checks the mandatoriness of the UI fields and return the mandaory message
     *if mandatoriness is not satisfied */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Displays the AlertMessage */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of FTInternalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        
        final String mandatoryMessage = checkMandatory(panActionItem);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            observable.execute(status);
            settings();
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panActionItem, false);
        setButtonEnableDisable();
        observable.setResultStatus();
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
    
    public static void main(String args[]){
        javax.swing.JFrame frame=new javax.swing.JFrame();
        FTInternalUI  tui = new FTInternalUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,700);
        frame.show();
        tui.show();
    }
    
    /** Called to do authorize operations like Authorize,Reject,Exception */
    public void authorizeStatus(String authorizeStatus) {
        if (!authorizeType.equals(AUTHORIZE)){
            authorizeType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getFTInternalAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeFTInternal");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            mapParam = null;
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (authorizeType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("REF_NO", lblReferenceNumberValue.getText());
            
            ClientUtil.execute("authorizeFTInternal", singleAuthorizeMap);
            singleAuthorizeMap = null;
            authorizeType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    /** This method clears the labels when new,save or cancel operation is completed */
    private void clearLabels(){
        lblMemberNameValue.setText("");
        lblReferenceNumberValue.setText("");
        lblRelationshipValue.setText("");
        lblLeadBankerValue.setText("");
        lblLeadRSOValue.setText("");
        lblContactModeValue.setText("");
        lblModeDetailsValue.setText("");
        lblContactDateValue.setText("");
        lblOrderSourceValue.setText("");
        lblAdviseTypeValue.setText("");
        lblClientContactValue.setText("");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrActionItem = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panActionItem = new com.see.truetransact.uicomponent.CPanel();
        panLabels = new com.see.truetransact.uicomponent.CPanel();
        lblReferenceNumber = new com.see.truetransact.uicomponent.CLabel();
        lblReferenceNumberValue = new com.see.truetransact.uicomponent.CLabel();
        lblMemberId = new com.see.truetransact.uicomponent.CLabel();
        panTxtMember = new com.see.truetransact.uicomponent.CPanel();
        txtMember = new com.see.truetransact.uicomponent.CTextField();
        btnMember = new com.see.truetransact.uicomponent.CButton();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        lblRelationshipValue = new com.see.truetransact.uicomponent.CLabel();
        lblLeadBanker = new com.see.truetransact.uicomponent.CLabel();
        lblLeadBankerValue = new com.see.truetransact.uicomponent.CLabel();
        lblModeDetails = new com.see.truetransact.uicomponent.CLabel();
        lblModeDetailsValue = new com.see.truetransact.uicomponent.CLabel();
        lblContactMode = new com.see.truetransact.uicomponent.CLabel();
        lblContactModeValue = new com.see.truetransact.uicomponent.CLabel();
        lblLeadRSO = new com.see.truetransact.uicomponent.CLabel();
        lblLeadRSOValue = new com.see.truetransact.uicomponent.CLabel();
        lblContactDate = new com.see.truetransact.uicomponent.CLabel();
        lblContactDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblClientContact = new com.see.truetransact.uicomponent.CLabel();
        lblClientContactValue = new com.see.truetransact.uicomponent.CLabel();
        lblOrderSource = new com.see.truetransact.uicomponent.CLabel();
        lblOrderSourceValue = new com.see.truetransact.uicomponent.CLabel();
        lblAdviseType = new com.see.truetransact.uicomponent.CLabel();
        lblAdviseTypeValue = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameValue = new com.see.truetransact.uicomponent.CLabel();
        panActionDetails = new com.see.truetransact.uicomponent.CPanel();
        panCreditAccount = new com.see.truetransact.uicomponent.CPanel();
        txtCreditEntitlementGroup = new com.see.truetransact.uicomponent.CTextField();
        lblCreditEnititlementGroup = new com.see.truetransact.uicomponent.CLabel();
        lblCreditPortfolioLocation = new com.see.truetransact.uicomponent.CLabel();
        txtCreditPortfolioLocation = new com.see.truetransact.uicomponent.CTextField();
        lblCreditAccount = new com.see.truetransact.uicomponent.CLabel();
        txtCreditAccount = new com.see.truetransact.uicomponent.CTextField();
        lblCreditAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtCreditAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        panOrderDetails = new com.see.truetransact.uicomponent.CPanel();
        panExecutionDate = new com.see.truetransact.uicomponent.CPanel();
        tdtExecutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        panValueDate = new com.see.truetransact.uicomponent.CPanel();
        lblValueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtValueDate = new com.see.truetransact.uicomponent.CDateField();
        panDebit = new com.see.truetransact.uicomponent.CPanel();
        lblDebitAmount = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAmount = new com.see.truetransact.uicomponent.CTextField();
        panCredit = new com.see.truetransact.uicomponent.CPanel();
        lblCreditAmount = new com.see.truetransact.uicomponent.CLabel();
        txtCreditAmount = new com.see.truetransact.uicomponent.CTextField();
        panInstAdvice = new com.see.truetransact.uicomponent.CPanel();
        lblBankOfficeInstructions = new com.see.truetransact.uicomponent.CLabel();
        txtBankOfficeInstruction = new com.see.truetransact.uicomponent.CTextField();
        lblTraderDealerInst = new com.see.truetransact.uicomponent.CLabel();
        txtTraderDealerInst = new com.see.truetransact.uicomponent.CTextField();
        lblCreditNotes = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNotes = new com.see.truetransact.uicomponent.CTextField();
        lblClientAdvices = new com.see.truetransact.uicomponent.CLabel();
        txtClientAdvices = new com.see.truetransact.uicomponent.CTextField();
        sptHorizonta = new com.see.truetransact.uicomponent.CSeparator();
        panDebitAccount = new com.see.truetransact.uicomponent.CPanel();
        lblDebitEntitlementGroup = new com.see.truetransact.uicomponent.CLabel();
        txtDebitEntitlementGroup = new com.see.truetransact.uicomponent.CTextField();
        lblDebitPortfolioLocation = new com.see.truetransact.uicomponent.CLabel();
        txtDebitPortfolioLocation = new com.see.truetransact.uicomponent.CTextField();
        lblDebitAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblDebitAccount = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAccount = new com.see.truetransact.uicomponent.CTextField();
        mbrActionItem = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        sptEdit = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(550, 650));
        setPreferredSize(new java.awt.Dimension(550, 650));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnDelete);

        lblSpace2.setText("     ");
        tbrActionItem.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnCancel);

        lblSpace3.setText("     ");
        tbrActionItem.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnReject);

        lblSpace4.setText("     ");
        tbrActionItem.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrActionItem.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrActionItem.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrActionItem.add(btnClose);

        getContentPane().add(tbrActionItem, java.awt.BorderLayout.NORTH);

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

        panActionItem.setLayout(new java.awt.GridBagLayout());

        panLabels.setLayout(new java.awt.GridBagLayout());

        lblReferenceNumber.setText("Reference Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblReferenceNumber, gridBagConstraints);

        lblReferenceNumberValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblReferenceNumberValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblReferenceNumberValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblReferenceNumberValue, gridBagConstraints);

        lblMemberId.setText("Member Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblMemberId, gridBagConstraints);

        panTxtMember.setLayout(new java.awt.GridBagLayout());

        txtMember.setEditable(false);
        txtMember.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMember.setMinimumSize(new java.awt.Dimension(100, 21));
        panTxtMember.add(txtMember, new java.awt.GridBagConstraints());

        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMember.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMember.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMember.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMember.setEnabled(false);
        btnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTxtMember.add(btnMember, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLabels.add(panTxtMember, gridBagConstraints);

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panLabels.add(lblRelationship, gridBagConstraints);

        lblRelationshipValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblRelationshipValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRelationshipValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblRelationshipValue, gridBagConstraints);

        lblLeadBanker.setText("Lead Banker");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblLeadBanker, gridBagConstraints);

        lblLeadBankerValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblLeadBankerValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblLeadBankerValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLabels.add(lblLeadBankerValue, gridBagConstraints);

        lblModeDetails.setText("Mode Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblModeDetails, gridBagConstraints);

        lblModeDetailsValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblModeDetailsValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblModeDetailsValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblModeDetailsValue, gridBagConstraints);

        lblContactMode.setText("Contact Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblContactMode, gridBagConstraints);

        lblContactModeValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblContactModeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblContactModeValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblContactModeValue, gridBagConstraints);

        lblLeadRSO.setText("Lead RSO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblLeadRSO, gridBagConstraints);

        lblLeadRSOValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblLeadRSOValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblLeadRSOValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLabels.add(lblLeadRSOValue, gridBagConstraints);

        lblContactDate.setText("Contact Date / Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblContactDate, gridBagConstraints);

        lblContactDateValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblContactDateValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblContactDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblContactDateValue, gridBagConstraints);

        lblClientContact.setText("Client Contact");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblClientContact, gridBagConstraints);

        lblClientContactValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblClientContactValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblClientContactValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblClientContactValue, gridBagConstraints);

        lblOrderSource.setText("Order Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblOrderSource, gridBagConstraints);

        lblOrderSourceValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblOrderSourceValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblOrderSourceValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblOrderSourceValue, gridBagConstraints);

        lblAdviseType.setText("Advise Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblAdviseType, gridBagConstraints);

        lblAdviseTypeValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblAdviseTypeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAdviseTypeValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLabels.add(lblAdviseTypeValue, gridBagConstraints);

        lblMemberName.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblMemberName, gridBagConstraints);

        lblMemberNameValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblMemberNameValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblMemberNameValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLabels.add(lblMemberNameValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
        panActionItem.add(panLabels, gridBagConstraints);

        panActionDetails.setLayout(new java.awt.GridBagLayout());

        panCreditAccount.setBorder(javax.swing.BorderFactory.createTitledBorder("Credit Account"));
        panCreditAccount.setLayout(new java.awt.GridBagLayout());

        txtCreditEntitlementGroup.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCreditEntitlementGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(txtCreditEntitlementGroup, gridBagConstraints);

        lblCreditEnititlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(lblCreditEnititlementGroup, gridBagConstraints);

        lblCreditPortfolioLocation.setText("Portfolio-Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(lblCreditPortfolioLocation, gridBagConstraints);

        txtCreditPortfolioLocation.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCreditPortfolioLocation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(txtCreditPortfolioLocation, gridBagConstraints);

        lblCreditAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(lblCreditAccount, gridBagConstraints);

        txtCreditAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(txtCreditAccount, gridBagConstraints);

        lblCreditAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(lblCreditAssetSubClass, gridBagConstraints);

        txtCreditAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditAccount.add(txtCreditAssetSubClass, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        panActionDetails.add(panCreditAccount, gridBagConstraints);

        panOrderDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Details"));
        panOrderDetails.setLayout(new java.awt.GridBagLayout());

        panExecutionDate.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panExecutionDate.add(tdtExecutionDate, gridBagConstraints);

        lblExecutionDate.setText("Execution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 4, 4);
        panExecutionDate.add(lblExecutionDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        panOrderDetails.add(panExecutionDate, gridBagConstraints);

        panValueDate.setLayout(new java.awt.GridBagLayout());

        lblValueDate.setText("Value Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 4, 0);
        panValueDate.add(lblValueDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValueDate.add(tdtValueDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 32);
        panOrderDetails.add(panValueDate, gridBagConstraints);

        panDebit.setLayout(new java.awt.GridBagLayout());

        lblDebitAmount.setText("Debit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebit.add(lblDebitAmount, gridBagConstraints);

        txtDebitAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebit.add(txtDebitAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panOrderDetails.add(panDebit, gridBagConstraints);

        panCredit.setLayout(new java.awt.GridBagLayout());

        lblCreditAmount.setText("Credit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panCredit.add(lblCreditAmount, gridBagConstraints);

        txtCreditAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCreditAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCredit.add(txtCreditAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrderDetails.add(panCredit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panActionDetails.add(panOrderDetails, gridBagConstraints);

        panInstAdvice.setLayout(new java.awt.GridBagLayout());

        lblBankOfficeInstructions.setText("Bank-Office Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(lblBankOfficeInstructions, gridBagConstraints);

        txtBankOfficeInstruction.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankOfficeInstruction.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(txtBankOfficeInstruction, gridBagConstraints);

        lblTraderDealerInst.setText("Trader / Dealer Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(lblTraderDealerInst, gridBagConstraints);

        txtTraderDealerInst.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTraderDealerInst.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(txtTraderDealerInst, gridBagConstraints);

        lblCreditNotes.setText("Credit Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(lblCreditNotes, gridBagConstraints);

        txtCreditNotes.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(txtCreditNotes, gridBagConstraints);

        lblClientAdvices.setText("Client Advices");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(lblClientAdvices, gridBagConstraints);

        txtClientAdvices.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstAdvice.add(txtClientAdvices, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 16);
        panActionDetails.add(panInstAdvice, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panActionDetails.add(sptHorizonta, gridBagConstraints);

        panDebitAccount.setBorder(javax.swing.BorderFactory.createTitledBorder("Debit Account"));
        panDebitAccount.setLayout(new java.awt.GridBagLayout());

        lblDebitEntitlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(lblDebitEntitlementGroup, gridBagConstraints);

        txtDebitEntitlementGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(txtDebitEntitlementGroup, gridBagConstraints);

        lblDebitPortfolioLocation.setText("Portfolio-Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(lblDebitPortfolioLocation, gridBagConstraints);

        txtDebitPortfolioLocation.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDebitPortfolioLocation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(txtDebitPortfolioLocation, gridBagConstraints);

        lblDebitAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(lblDebitAssetSubClass, gridBagConstraints);

        txtDebitAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(txtDebitAssetSubClass, gridBagConstraints);

        lblDebitAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(lblDebitAccount, gridBagConstraints);

        txtDebitAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitAccount.add(txtDebitAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        panActionDetails.add(panDebitAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panActionItem.add(panActionDetails, gridBagConstraints);

        getContentPane().add(panActionItem, java.awt.BorderLayout.CENTER);

        mbrActionItem.setName("mbrCustomer");

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

        sptEdit.setName("sptNew");
        mnuProcess.add(sptEdit);

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
        sptSave.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                sptSaveAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
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

        mbrActionItem.add(mnuProcess);

        setJMenuBar(mbrActionItem);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void sptSaveAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_sptSaveAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_sptSaveAncestorAdded
    
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
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panActionItem, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
        callView("Member");
    }//GEN-LAST:event_btnMemberActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panActionItem, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMember;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel lblAdviseType;
    private com.see.truetransact.uicomponent.CLabel lblAdviseTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblBankOfficeInstructions;
    private com.see.truetransact.uicomponent.CLabel lblClientAdvices;
    private com.see.truetransact.uicomponent.CLabel lblClientContact;
    private com.see.truetransact.uicomponent.CLabel lblClientContactValue;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactDateValue;
    private com.see.truetransact.uicomponent.CLabel lblContactMode;
    private com.see.truetransact.uicomponent.CLabel lblContactModeValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditAccount;
    private com.see.truetransact.uicomponent.CLabel lblCreditAmount;
    private com.see.truetransact.uicomponent.CLabel lblCreditAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblCreditEnititlementGroup;
    private com.see.truetransact.uicomponent.CLabel lblCreditNotes;
    private com.see.truetransact.uicomponent.CLabel lblCreditPortfolioLocation;
    private com.see.truetransact.uicomponent.CLabel lblDebitAccount;
    private com.see.truetransact.uicomponent.CLabel lblDebitAmount;
    private com.see.truetransact.uicomponent.CLabel lblDebitAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblDebitEntitlementGroup;
    private com.see.truetransact.uicomponent.CLabel lblDebitPortfolioLocation;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblLeadBanker;
    private com.see.truetransact.uicomponent.CLabel lblLeadBankerValue;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSO;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSOValue;
    private com.see.truetransact.uicomponent.CLabel lblMemberId;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameValue;
    private com.see.truetransact.uicomponent.CLabel lblModeDetails;
    private com.see.truetransact.uicomponent.CLabel lblModeDetailsValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrderSource;
    private com.see.truetransact.uicomponent.CLabel lblOrderSourceValue;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumber;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumberValue;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTraderDealerInst;
    private com.see.truetransact.uicomponent.CLabel lblValueDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrActionItem;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panActionDetails;
    private com.see.truetransact.uicomponent.CPanel panActionItem;
    private com.see.truetransact.uicomponent.CPanel panCredit;
    private com.see.truetransact.uicomponent.CPanel panCreditAccount;
    private com.see.truetransact.uicomponent.CPanel panDebit;
    private com.see.truetransact.uicomponent.CPanel panDebitAccount;
    private com.see.truetransact.uicomponent.CPanel panExecutionDate;
    private com.see.truetransact.uicomponent.CPanel panInstAdvice;
    private com.see.truetransact.uicomponent.CPanel panLabels;
    private com.see.truetransact.uicomponent.CPanel panOrderDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTxtMember;
    private com.see.truetransact.uicomponent.CPanel panValueDate;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CSeparator sptHorizonta;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrActionItem;
    private com.see.truetransact.uicomponent.CDateField tdtExecutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtValueDate;
    private com.see.truetransact.uicomponent.CTextField txtBankOfficeInstruction;
    private com.see.truetransact.uicomponent.CTextField txtClientAdvices;
    private com.see.truetransact.uicomponent.CTextField txtCreditAccount;
    private com.see.truetransact.uicomponent.CTextField txtCreditAmount;
    private com.see.truetransact.uicomponent.CTextField txtCreditAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtCreditEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtCreditNotes;
    private com.see.truetransact.uicomponent.CTextField txtCreditPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtDebitAccount;
    private com.see.truetransact.uicomponent.CTextField txtDebitAmount;
    private com.see.truetransact.uicomponent.CTextField txtDebitAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtDebitEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtDebitPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtTraderDealerInst;
    // End of variables declaration//GEN-END:variables
    
}
