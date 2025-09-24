/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewTimeDepositUI.java
 *
 * Created on July 12, 2004, 12:24 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit;

/**
 *
 * @author  Ashok
 */

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

import com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit.NewTimeDepositRB;
import com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit.NewTimeDepositOB;
import com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit.NewTimeDepositMRB;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;

public class NewTimeDepositUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField {
    
    private NewTimeDepositRB resourceBundle = new NewTimeDepositRB();
    private NewTimeDepositOB observable;
    private NewTimeDepositMRB objMandatoryRB;
    private HashMap mandatoryMap;
    private String viewType = "";
    private String authorizeType = "";
    private final String AUTHORIZE = "Authorize";
    
    /** Creates new form NewTimeDepositUI */
    public NewTimeDepositUI() {
        initGUI();
    }
    
    /** Initialises the GUI */
    private void initGUI(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaximumLengths();
        observable.resetForm();
        clearLabels();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panNewTimeDeposit,false);
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
        btnMember.setName("btnMember");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        sptHorizontal.setName("sptHorizontal");
        cboProductType.setName("cboProductType");
        cboSettlementType.setName("cboSettlementType");
        lblAccount.setName("lblAccount");
        lblAdviseType.setName("lblAdviseType");
        lblAdviseTypeValue.setName("lblAdviseTypeValue");
        lblAssetSubClass.setName("lblAssetSubClass");
        lblAutorollInd.setName("lblAutorollInd");
        lblBankOfficeInstructions.setName("lblBankOfficeInstructions");
        lblClientAdvices.setName("lblClientAdvices");
        lblClientContact.setName("lblClientContact");
        lblClientContactValue.setName("lblClientContactValue");
        lblClientRate.setName("lblClientRate");
        lblContactDate.setName("lblContactDate");
        lblContactDateValue.setName("lblContactDateValue");
        lblContactMode.setName("lblContactMode");
        lblContactModeValue.setName("lblContactModeValue");
        lblCreditNotes.setName("lblCreditNotes");
        lblEntitlementGroup.setName("lblEntitlementGroup");
        lblExecutionDate.setName("lblExecutionDate");
        lblLeadBanker.setName("lblLeadBanker");
        lblLeadBankerValue.setName("lblLeadBankerValue");
        lblLeadRSO.setName("lblLeadRSO");
        lblLeadRSOValue.setName("lblLeadRSOValue");
        lblMaturityDate.setName("lblMaturityDate");
        lblMemberId.setName("lblMemberId");
        lblMemberName.setName("lblMemberName");
        lblMemberNameValue.setName("lblMemberNameValue");
        lblModeDetails.setName("lblModeDetails");
        lblModeDetailsValue.setName("lblModeDetailsValue");
        lblMsg.setName("lblMsg");
        lblOrderAmount.setName("lblOrderAmount");
        lblOrderSource.setName("lblOrderSource");
        lblOrderSourceValue.setName("lblOrderSourceValue");
        lblPhoneOrder.setName("lblPhoneOrder");
        lblPortfolioAccount.setName("lblPortfolioAccount");
        lblPortfolioAssetSubClass.setName("lblPortfolioAssetSubClass");
        lblPortfolioLocation.setName("lblPortfolioLocation");
        lblPrincipalAccount.setName("lblPrincipalAccount");
        lblPrincipalAssetSubClass.setName("lblPrincipalAssetSubClass");
        lblProductType.setName("lblProductType");
        lblReferenceNumber.setName("lblReferenceNumber");
        lblReferenceNumberValue.setName("lblReferenceNumberValue");
        lblRelationship.setName("lblRelationship");
        lblRelationshipValue.setName("lblRelationshipValue");
        lblSettlementAccount.setName("lblSettlementAccount");
        lblSettlementAssetSubClass.setName("lblSettlementAssetSubClass");
        lblSettlementType.setName("lblSettlementType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpread.setName("lblSpread");
        lblStartDate.setName("lblStartDate");
        lblStatus.setName("lblStatus");
        lblTenor.setName("lblTenor");
        lblTraderDealerInst.setName("lblTraderDealerInst");
        mbrNewTimeDeposit.setName("mbrNewTimeDeposit");
        panInstructions.setName("panInstructions");
        panLabels.setName("panLabels");
        panNewTimeDeposit.setName("panNewTimeDeposit");
        panOrder.setName("panOrder");
        panOrderDetails.setName("panOrderDetails");
        panPhoneOrder.setName("panPhoneOrder");
        panPrincipalSettlement.setName("panPrincipalSettlement");
        panSafeKeepingPortFolio.setName("panSafeKeepingPortFolio");
        panSettlementAccount.setName("panSettlementAccount");
        panStatus.setName("panStatus");
        panTimeDeposit.setName("panTimeDeposit");
        panTxtMember.setName("panTxtMember");
        rdoPhoneOrder_No.setName("rdoPhoneOrder_No");
        rdoPhoneOrder_Yes.setName("rdoPhoneOrder_Yes");
        tabNewTimeDeposit.setName("tabNewTimeDeposit");
        tdtExecutionDate.setName("tdtExecutionDate");
        tdtMaturityDate.setName("tdtMaturityDate");
        tdtStartDate.setName("tdtStartDate");
        txtAccount.setName("txAccount");
        txtPrincipalAccount.setName("txPrincipalAccount");
        txtAssetSubClass.setName("txtAssetSubClass");
        txtAutorollInd.setName("txtAutorollInd");
        txtBankOfficeInstruction.setName("txtBankOfficeInstruction");
        txtClientAdvices.setName("txtClientAdvices");
        txtClientRate.setName("txtClientRate");
        txtCreditNotes.setName("txtCreditNotes");
        txtEntitlementGroup.setName("txtEntitlementGroup");
        txtMember.setName("txtMember");
        txtOrderAmount.setName("txtOrderAmount");
        txtPortfolioAccount.setName("txtPortfolioAccount");
        txtPortfolioAssetSubClass.setName("txtPortfolioAssetSubClass");
        txtPortfolioLocation.setName("txtPortfolioLocation");
        txtPrincipalAssetSubClass.setName("txtPrincipalAssetSubClass");
        txtSettlementAccount.setName("txtSettlementAccount");
        txtSettlementAssetSubClass.setName("txtSettlementAssetSubClass");
        txtSpread.setName("txtSpread");
        txtTenor1.setName("txtTenor1");
        txtTenor2.setName("txtTenor2");
        txtTraderDealerInst.setName("txtTraderDealerInst");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblExecutionDate.setText(resourceBundle.getString("lblExecutionDate"));
        lblSettlementAccount.setText(resourceBundle.getString("lblSettlementAccount"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblContactMode.setText(resourceBundle.getString("lblContactMode"));
        lblLeadRSOValue.setText(resourceBundle.getString("lblLeadRSOValue"));
        lblAdviseTypeValue.setText(resourceBundle.getString("lblAdviseTypeValue"));
        lblSpread.setText(resourceBundle.getString("lblSpread"));
        lblPortfolioAccount.setText(resourceBundle.getString("lblPortfolioAccount"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblAutorollInd.setText(resourceBundle.getString("lblAutorollInd"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblOrderSourceValue.setText(resourceBundle.getString("lblOrderSourceValue"));
        lblClientContactValue.setText(resourceBundle.getString("lblClientContactValue"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblPrincipalAssetSubClass.setText(resourceBundle.getString("lblPrincipalAssetSubClass"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblMemberId.setText(resourceBundle.getString("lblMemberId"));
        lblOrderAmount.setText(resourceBundle.getString("lblOrderAmount"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblReferenceNumber.setText(resourceBundle.getString("lblReferenceNumber"));
        lblLeadRSO.setText(resourceBundle.getString("lblLeadRSO"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblTenor.setText(resourceBundle.getString("lblTenor"));
        ((javax.swing.border.TitledBorder)panPrincipalSettlement.getBorder()).setTitle(resourceBundle.getString("panPrincipalSettlement"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRelationshipValue.setText(resourceBundle.getString("lblRelationshipValue"));
        lblClientRate.setText(resourceBundle.getString("lblClientRate"));
        lblSettlementAssetSubClass.setText(resourceBundle.getString("lblSettlementAssetSubClass"));
        lblPortfolioAssetSubClass.setText(resourceBundle.getString("lblPortfolioAssetSubClass"));
        lblMemberNameValue.setText(resourceBundle.getString("lblMemberNameValue"));
        lblLeadBanker.setText(resourceBundle.getString("lblLeadBanker"));
        lblLeadBankerValue.setText(resourceBundle.getString("lblLeadBankerValue"));
        lblPhoneOrder.setText(resourceBundle.getString("lblPhoneOrder"));
        lblAdviseType.setText(resourceBundle.getString("lblAdviseType"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblSettlementType.setText(resourceBundle.getString("lblSettlementType"));
        lblAccount.setText(resourceBundle.getString("lblAccount"));
        lblTraderDealerInst.setText(resourceBundle.getString("lblTraderDealerInst"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblModeDetailsValue.setText(resourceBundle.getString("lblModeDetailsValue"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lblPrincipalAccount.setText(resourceBundle.getString("lblPrincipalAccount"));
        ((javax.swing.border.TitledBorder)panSafeKeepingPortFolio.getBorder()).setTitle(resourceBundle.getString("panSafeKeepingPortFolio"));
        lblContactModeValue.setText(resourceBundle.getString("lblContactModeValue"));
        ((javax.swing.border.TitledBorder)panSettlementAccount.getBorder()).setTitle(resourceBundle.getString("panSettlementAccount"));
        lblEntitlementGroup.setText(resourceBundle.getString("lblEntitlementGroup"));
        lblContactDateValue.setText(resourceBundle.getString("lblContactDateValue"));
        lblBankOfficeInstructions.setText(resourceBundle.getString("lblBankOfficeInstructions"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblClientContact.setText(resourceBundle.getString("lblClientContact"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblOrderSource.setText(resourceBundle.getString("lblOrderSource"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnMember.setText(resourceBundle.getString("btnMember"));
        lblStartDate.setText(resourceBundle.getString("lblStartDate"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblAssetSubClass.setText(resourceBundle.getString("lblAssetSubClass"));
        lblCreditNotes.setText(resourceBundle.getString("lblCreditNotes"));
        lblPortfolioLocation.setText(resourceBundle.getString("lblPortfolioLocation"));
        lblModeDetails.setText(resourceBundle.getString("lblModeDetails"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        rdoPhoneOrder_Yes.setText(resourceBundle.getString("rdoPhoneOrder_Yes"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        rdoPhoneOrder_No.setText(resourceBundle.getString("rdoPhoneOrder_No"));
        lblReferenceNumberValue.setText(resourceBundle.getString("lblReferenceNumberValue"));
        lblClientAdvices.setText(resourceBundle.getString("lblClientAdvices"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtPortfolioAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtPortfolioAccount", new Boolean(true));
        mandatoryMap.put("txtSettlementAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtSettlementAccount", new Boolean(true));
        mandatoryMap.put("txtAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtAccount", new Boolean(true));
        mandatoryMap.put("txtPrincipalAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtPrincipalAccount", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("txtOrderAmount", new Boolean(true));
        mandatoryMap.put("txtSpread", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboSettlementType", new Boolean(true));
        mandatoryMap.put("txtTenor1", new Boolean(true));
        mandatoryMap.put("txtTenor2", new Boolean(true));
        mandatoryMap.put("tdtMaturityDate", new Boolean(true));
        mandatoryMap.put("txtAutorollInd", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("txtClientRate", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
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
        objMandatoryRB = new NewTimeDepositMRB();
        txtMember.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMember"));
        txtEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEntitlementGroup"));
        txtPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioLocation"));
        txtPortfolioAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioAssetSubClass"));
        txtPortfolioAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioAccount"));
        txtSettlementAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSettlementAssetSubClass"));
        txtSettlementAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSettlementAccount"));
        txtAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetSubClass"));
        txtAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccount"));
        txtPrincipalAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrincipalAssetSubClass"));
        txtPrincipalAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrincipalAccount"));
        tdtExecutionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecutionDate"));
        tdtStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtStartDate"));
        txtOrderAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOrderAmount"));
        txtSpread.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSpread"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboSettlementType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSettlementType"));
        txtTenor1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTenor1"));
        txtTenor2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTenor2"));
        tdtMaturityDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMaturityDate"));
        txtAutorollInd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAutorollInd"));
        rdoPhoneOrder_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPhoneOrder_Yes"));
        txtClientRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClientRate"));
        txtBankOfficeInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankOfficeInstruction"));
        txtTraderDealerInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTraderDealerInst"));
        txtCreditNotes.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNotes"));
        txtClientAdvices.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClientAdvices"));
    }
    
    /** Setting the observable for NewTimeDepositUI */
    private void setObservable(){
        try{
            observable = NewTimeDepositOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            
        }
    }
    
    /** Sets the Maximum Allowed Lenghths to the TextFields */
    private void setMaximumLengths(){
        txtMember.setMaxLength(16);
        txtEntitlementGroup.setMaxLength(32);
        txtPortfolioLocation.setMaxLength(32);
        txtPortfolioAssetSubClass.setMaxLength(32);
        txtSettlementAccount.setMaxLength(32);
        txtSettlementAssetSubClass.setMaxLength(32);
        txtAssetSubClass.setMaxLength(32);
        txtSettlementAccount.setMaxLength(32);
        txtAccount.setMaxLength(32);
        txtPrincipalAssetSubClass.setMaxLength(32);
        txtPrincipalAccount.setMaxLength(32);
        txtOrderAmount.setMaxLength(16);
        txtSpread.setMaxLength(16);
        txtTenor1.setMaxLength(32);
        txtTenor2.setMaxLength(32);
        txtAutorollInd.setMaxLength(32);
        txtBankOfficeInstruction.setMaxLength(1024);
        txtTraderDealerInst.setMaxLength(1024);
        txtCreditNotes.setMaxLength(1024);
        txtClientAdvices.setMaxLength(1024);
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        lblReferenceNumberValue.setText(observable.getLblReferenceNumber());
        txtMember.setText(observable.getTxtMember());
        txtEntitlementGroup.setText(observable.getTxtEntitlementGroup());
        txtPortfolioLocation.setText(observable.getTxtPortfolioLocation());
        txtPortfolioAssetSubClass.setText(observable.getTxtPortfolioAssetSubClass());
        txtPortfolioAccount.setText(observable.getTxtPortfolioAccount());
        txtSettlementAssetSubClass.setText(observable.getTxtSettlementAssetSubClass());
        txtSettlementAccount.setText(observable.getTxtSettlementAccount());
        txtAssetSubClass.setText(observable.getTxtAssetSubClass());
        txtAccount.setText(observable.getTxtAccount());
        txtPrincipalAssetSubClass.setText(observable.getTxtPrincipalAssetSubClass());
        txtPrincipalAccount.setText(observable.getTxtPrincipalAccount());
        tdtExecutionDate.setDateValue(observable.getTdtExecutionDate());
        tdtStartDate.setDateValue(observable.getTdtStartDate());
        txtOrderAmount.setText(observable.getTxtOrderAmount());
        txtSpread.setText(observable.getTxtSpread());
        cboProductType.setSelectedItem(observable.getCboProductType());
        cboSettlementType.setSelectedItem(observable.getCboSettlementType());
        txtTenor1.setText(observable.getTxtTenor1());
        txtTenor2.setText(observable.getTxtTenor2());
        tdtMaturityDate.setDateValue(observable.getTdtMaturityDate());
        txtAutorollInd.setText(observable.getTxtAutorollInd());
        rdoPhoneOrder_Yes.setSelected(observable.getRdoPhoneOrder_Yes());
        rdoPhoneOrder_No.setSelected(observable.getRdoPhoneOrder_No());
        txtClientRate.setText(observable.getTxtClientRate());
        txtBankOfficeInstruction.setText(observable.getTxtBankOfficeInstruction());
        txtTraderDealerInst.setText(observable.getTxtTraderDealerInst());
        txtCreditNotes.setText(observable.getTxtCreditNotes());
        txtClientAdvices.setText(observable.getTxtClientAdvices());
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setLblReferenceNumber(lblReferenceNumberValue.getText());
        observable.setTxtMember(txtMember.getText());
        observable.setTxtEntitlementGroup(txtEntitlementGroup.getText());
        observable.setTxtPortfolioLocation(txtPortfolioLocation.getText());
        observable.setTxtPortfolioAssetSubClass(txtPortfolioAssetSubClass.getText());
        observable.setTxtPortfolioAccount(txtPortfolioAccount.getText());
        observable.setTxtSettlementAssetSubClass(txtSettlementAssetSubClass.getText());
        observable.setTxtSettlementAccount(txtSettlementAccount.getText());
        observable.setTxtAssetSubClass(txtAssetSubClass.getText());
        observable.setTxtAccount(txtAccount.getText());
        observable.setTxtPrincipalAssetSubClass(txtPrincipalAssetSubClass.getText());
        observable.setTxtPrincipalAccount(txtPrincipalAccount.getText());
        observable.setTdtExecutionDate(tdtExecutionDate.getDateValue());
        observable.setTdtStartDate(tdtStartDate.getDateValue());
        observable.setTxtOrderAmount(txtOrderAmount.getText());
        observable.setTxtSpread(txtSpread.getText());
        observable.setCboProductType((String) cboProductType.getSelectedItem());
        observable.setCboSettlementType((String) cboSettlementType.getSelectedItem());
        observable.setTxtTenor1(txtTenor1.getText());
        observable.setTxtTenor2(txtTenor2.getText());
        observable.setTdtMaturityDate(tdtMaturityDate.getDateValue());
        observable.setTxtAutorollInd(txtAutorollInd.getText());
        observable.setRdoPhoneOrder_Yes(rdoPhoneOrder_Yes.isSelected());
        observable.setRdoPhoneOrder_No(rdoPhoneOrder_No.isSelected());
        observable.setTxtClientRate(txtClientRate.getText());
        observable.setTxtBankOfficeInstruction(txtBankOfficeInstruction.getText());
        observable.setTxtTraderDealerInst(txtTraderDealerInst.getText());
        observable.setTxtCreditNotes(txtCreditNotes.getText());
        observable.setTxtClientAdvices(txtClientAdvices.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
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
    
    /** Sets the model for the Comboboxes in the UI */
    private void initComponentData(){
        cboProductType.setModel(observable.getCbmProductType());
        cboSettlementType.setModel(observable.getCbmSettlementType());
    }
    
    /** This method shows the ViewAll Screen with the rows filled up according to query executed */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtTimeDeposit");
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
                    hash.put(CommonConstants.MAP_WHERE, hash.get("REF_ID"));
                    observable.populateData(hash);
                    hash = null;
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panNewTimeDeposit, false);
                    } else {
                        ClientUtil.enableDisable(panNewTimeDeposit, true);
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
    
    /* Calls the execute method of ExteranalWireOB to do insertion or updation or deletion */
    private void saveAction(String status){
        
        final String mandatoryMessage = checkMandatory(panNewTimeDeposit);
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
        ClientUtil.enableDisable(panNewTimeDeposit, false);
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
    
    /** Called to do authorize operations like Authorize,Reject,Exception */
    public void authorizeStatus(String authorizeStatus) {
        if (!authorizeType.equals(AUTHORIZE)){
            authorizeType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getNewTimeDepositAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeNewTimeDeposit");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            mapParam = null;
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (authorizeType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("REF_ID", lblReferenceNumberValue.getText());
            
            ClientUtil.execute("authorizeNewTimeDeposit", singleAuthorizeMap);
            singleAuthorizeMap = null;
            authorizeType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame=new javax.swing.JFrame();
        NewTimeDepositUI  tui = new NewTimeDepositUI();
        frame.getContentPane().add(tui);
        frame.setSize(550,600);
        frame.show();
        tui.show();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoPhoneOrder = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrNewTimeDeposit = new javax.swing.JToolBar();
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
        panNewTimeDeposit = new com.see.truetransact.uicomponent.CPanel();
        tabNewTimeDeposit = new com.see.truetransact.uicomponent.CTabbedPane();
        panTimeDeposit = new com.see.truetransact.uicomponent.CPanel();
        panLabels = new com.see.truetransact.uicomponent.CPanel();
        lblReferenceNumber = new com.see.truetransact.uicomponent.CLabel();
        lblReferenceNumberValue = new com.see.truetransact.uicomponent.CLabel();
        lblMemberId = new com.see.truetransact.uicomponent.CLabel();
        panTxtMember = new com.see.truetransact.uicomponent.CPanel();
        txtMember = new com.see.truetransact.uicomponent.CTextField();
        btnMember = new com.see.truetransact.uicomponent.CButton();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        lblRelationshipValue = new com.see.truetransact.uicomponent.CLabel();
        lblLeadBanker = new com.see.truetransact.uicomponent.CLabel();
        lblLeadBankerValue = new com.see.truetransact.uicomponent.CLabel();
        lblLeadRSO = new com.see.truetransact.uicomponent.CLabel();
        lblLeadRSOValue = new com.see.truetransact.uicomponent.CLabel();
        lblContactMode = new com.see.truetransact.uicomponent.CLabel();
        lblContactModeValue = new com.see.truetransact.uicomponent.CLabel();
        lblModeDetails = new com.see.truetransact.uicomponent.CLabel();
        lblModeDetailsValue = new com.see.truetransact.uicomponent.CLabel();
        lblContactDate = new com.see.truetransact.uicomponent.CLabel();
        lblContactDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblOrderSource = new com.see.truetransact.uicomponent.CLabel();
        lblOrderSourceValue = new com.see.truetransact.uicomponent.CLabel();
        lblAdviseType = new com.see.truetransact.uicomponent.CLabel();
        lblAdviseTypeValue = new com.see.truetransact.uicomponent.CLabel();
        lblClientContact = new com.see.truetransact.uicomponent.CLabel();
        lblClientContactValue = new com.see.truetransact.uicomponent.CLabel();
        panSafeKeepingPortFolio = new com.see.truetransact.uicomponent.CPanel();
        lblEntitlementGroup = new com.see.truetransact.uicomponent.CLabel();
        txtEntitlementGroup = new com.see.truetransact.uicomponent.CTextField();
        lblPortfolioLocation = new com.see.truetransact.uicomponent.CLabel();
        txtPortfolioLocation = new com.see.truetransact.uicomponent.CTextField();
        lblPortfolioAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtPortfolioAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblPortfolioAccount = new com.see.truetransact.uicomponent.CLabel();
        txtPortfolioAccount = new com.see.truetransact.uicomponent.CTextField();
        panSettlementAccount = new com.see.truetransact.uicomponent.CPanel();
        lblSettlementAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtSettlementAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblSettlementAccount = new com.see.truetransact.uicomponent.CLabel();
        txtSettlementAccount = new com.see.truetransact.uicomponent.CTextField();
        lblAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblAccount = new com.see.truetransact.uicomponent.CLabel();
        txtAccount = new com.see.truetransact.uicomponent.CTextField();
        panPrincipalSettlement = new com.see.truetransact.uicomponent.CPanel();
        lblPrincipalAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtPrincipalAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblPrincipalAccount = new com.see.truetransact.uicomponent.CLabel();
        txtPrincipalAccount = new com.see.truetransact.uicomponent.CTextField();
        panOrderDetails = new com.see.truetransact.uicomponent.CPanel();
        panOrder = new com.see.truetransact.uicomponent.CPanel();
        lblExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtExecutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblStartDate = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDate = new com.see.truetransact.uicomponent.CDateField();
        lblOrderAmount = new com.see.truetransact.uicomponent.CLabel();
        txtOrderAmount = new com.see.truetransact.uicomponent.CTextField();
        lblSpread = new com.see.truetransact.uicomponent.CLabel();
        txtSpread = new com.see.truetransact.uicomponent.CTextField();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblSettlementType = new com.see.truetransact.uicomponent.CLabel();
        cboSettlementType = new com.see.truetransact.uicomponent.CComboBox();
        lblTenor = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        lblAutorollInd = new com.see.truetransact.uicomponent.CLabel();
        txtAutorollInd = new com.see.truetransact.uicomponent.CTextField();
        lblPhoneOrder = new com.see.truetransact.uicomponent.CLabel();
        panPhoneOrder = new com.see.truetransact.uicomponent.CPanel();
        rdoPhoneOrder_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPhoneOrder_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblClientRate = new com.see.truetransact.uicomponent.CLabel();
        txtClientRate = new com.see.truetransact.uicomponent.CTextField();
        panTenor = new com.see.truetransact.uicomponent.CPanel();
        txtTenor1 = new com.see.truetransact.uicomponent.CTextField();
        txtTenor2 = new com.see.truetransact.uicomponent.CTextField();
        panInstructions = new com.see.truetransact.uicomponent.CPanel();
        lblBankOfficeInstructions = new com.see.truetransact.uicomponent.CLabel();
        txtBankOfficeInstruction = new com.see.truetransact.uicomponent.CTextField();
        lblTraderDealerInst = new com.see.truetransact.uicomponent.CLabel();
        txtTraderDealerInst = new com.see.truetransact.uicomponent.CTextField();
        lblCreditNotes = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNotes = new com.see.truetransact.uicomponent.CTextField();
        lblClientAdvices = new com.see.truetransact.uicomponent.CLabel();
        txtClientAdvices = new com.see.truetransact.uicomponent.CTextField();
        sptHorizontal = new com.see.truetransact.uicomponent.CSeparator();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrNewTimeDeposit = new com.see.truetransact.uicomponent.CMenuBar();
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

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnDelete);

        lblSpace2.setText("     ");
        tbrNewTimeDeposit.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnCancel);

        lblSpace3.setText("     ");
        tbrNewTimeDeposit.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnReject);

        lblSpace4.setText("     ");
        tbrNewTimeDeposit.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrNewTimeDeposit.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrNewTimeDeposit.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrNewTimeDeposit.add(btnClose);

        getContentPane().add(tbrNewTimeDeposit, java.awt.BorderLayout.NORTH);

        panNewTimeDeposit.setLayout(new java.awt.GridBagLayout());

        panTimeDeposit.setLayout(new java.awt.GridBagLayout());

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

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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

        lblAdviseType.setText("               Advise Type");
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

        lblClientContact.setText("         Client Contact");
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panTimeDeposit.add(panLabels, gridBagConstraints);

        panSafeKeepingPortFolio.setBorder(javax.swing.BorderFactory.createTitledBorder("Safe keeping Portfolio"));
        panSafeKeepingPortFolio.setLayout(new java.awt.GridBagLayout());

        lblEntitlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panSafeKeepingPortFolio.add(lblEntitlementGroup, gridBagConstraints);

        txtEntitlementGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(txtEntitlementGroup, gridBagConstraints);

        lblPortfolioLocation.setText("Portfolio-Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panSafeKeepingPortFolio.add(lblPortfolioLocation, gridBagConstraints);

        txtPortfolioLocation.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPortfolioLocation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(txtPortfolioLocation, gridBagConstraints);

        lblPortfolioAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(lblPortfolioAssetSubClass, gridBagConstraints);

        txtPortfolioAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(txtPortfolioAssetSubClass, gridBagConstraints);

        lblPortfolioAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(lblPortfolioAccount, gridBagConstraints);

        txtPortfolioAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortFolio.add(txtPortfolioAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTimeDeposit.add(panSafeKeepingPortFolio, gridBagConstraints);

        panSettlementAccount.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Settlement Account"));
        panSettlementAccount.setLayout(new java.awt.GridBagLayout());

        lblSettlementAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panSettlementAccount.add(lblSettlementAssetSubClass, gridBagConstraints);

        txtSettlementAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(txtSettlementAssetSubClass, gridBagConstraints);

        lblSettlementAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 68, 4, 4);
        panSettlementAccount.add(lblSettlementAccount, gridBagConstraints);

        txtSettlementAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(txtSettlementAccount, gridBagConstraints);

        lblAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(lblAssetSubClass, gridBagConstraints);

        txtAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(txtAssetSubClass, gridBagConstraints);

        lblAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(lblAccount, gridBagConstraints);

        txtAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSettlementAccount.add(txtAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panTimeDeposit.add(panSettlementAccount, gridBagConstraints);

        panPrincipalSettlement.setBorder(javax.swing.BorderFactory.createTitledBorder("Principal Settlement Account"));
        panPrincipalSettlement.setLayout(new java.awt.GridBagLayout());

        lblPrincipalAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panPrincipalSettlement.add(lblPrincipalAssetSubClass, gridBagConstraints);

        txtPrincipalAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPrincipalSettlement.add(txtPrincipalAssetSubClass, gridBagConstraints);

        lblPrincipalAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 68, 4, 4);
        panPrincipalSettlement.add(lblPrincipalAccount, gridBagConstraints);

        txtPrincipalAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPrincipalSettlement.add(txtPrincipalAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panTimeDeposit.add(panPrincipalSettlement, gridBagConstraints);

        tabNewTimeDeposit.addTab("NewTimeDeposit", panTimeDeposit);

        panOrderDetails.setLayout(new java.awt.GridBagLayout());

        panOrder.setLayout(new java.awt.GridBagLayout());

        lblExecutionDate.setText("Execution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblExecutionDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(tdtExecutionDate, gridBagConstraints);

        lblStartDate.setText("Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblStartDate, gridBagConstraints);

        tdtStartDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtStartDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(tdtStartDate, gridBagConstraints);

        lblOrderAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblOrderAmount, gridBagConstraints);

        txtOrderAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOrderAmount.setValidation(new CurrencyValidation());
        txtOrderAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtOrderAmountFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtOrderAmount, gridBagConstraints);

        lblSpread.setText("Spread");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblSpread, gridBagConstraints);

        txtSpread.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSpread.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtSpread, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblProductType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(cboProductType, gridBagConstraints);

        lblSettlementType.setText("Settlement Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblSettlementType, gridBagConstraints);

        cboSettlementType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(cboSettlementType, gridBagConstraints);

        lblTenor.setText("Tenor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblTenor, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblMaturityDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(tdtMaturityDate, gridBagConstraints);

        lblAutorollInd.setText("Autoroll Ind");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblAutorollInd, gridBagConstraints);

        txtAutorollInd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtAutorollInd, gridBagConstraints);

        lblPhoneOrder.setText("Phone Order");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblPhoneOrder, gridBagConstraints);

        panPhoneOrder.setLayout(new java.awt.GridBagLayout());

        rdoPhoneOrder.add(rdoPhoneOrder_Yes);
        rdoPhoneOrder_Yes.setText("Yes");
        panPhoneOrder.add(rdoPhoneOrder_Yes, new java.awt.GridBagConstraints());

        rdoPhoneOrder.add(rdoPhoneOrder_No);
        rdoPhoneOrder_No.setText("No");
        panPhoneOrder.add(rdoPhoneOrder_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrder.add(panPhoneOrder, gridBagConstraints);

        lblClientRate.setText("Client Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblClientRate, gridBagConstraints);

        txtClientRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtClientRate.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtClientRate, gridBagConstraints);

        panTenor.setLayout(new java.awt.GridBagLayout());

        txtTenor1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTenor1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTenor.add(txtTenor1, gridBagConstraints);

        txtTenor2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTenor2.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panTenor.add(txtTenor2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrder.add(panTenor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panOrderDetails.add(panOrder, gridBagConstraints);

        panInstructions.setLayout(new java.awt.GridBagLayout());

        lblBankOfficeInstructions.setText("Bank-Office Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(lblBankOfficeInstructions, gridBagConstraints);

        txtBankOfficeInstruction.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankOfficeInstruction.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(txtBankOfficeInstruction, gridBagConstraints);

        lblTraderDealerInst.setText("Trader / Dealer Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(lblTraderDealerInst, gridBagConstraints);

        txtTraderDealerInst.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTraderDealerInst.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(txtTraderDealerInst, gridBagConstraints);

        lblCreditNotes.setText("Credit Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(lblCreditNotes, gridBagConstraints);

        txtCreditNotes.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(txtCreditNotes, gridBagConstraints);

        lblClientAdvices.setText("Client Advices");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(lblClientAdvices, gridBagConstraints);

        txtClientAdvices.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructions.add(txtClientAdvices, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panOrderDetails.add(panInstructions, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panOrderDetails.add(sptHorizontal, gridBagConstraints);

        tabNewTimeDeposit.addTab("OrderDetails", panOrderDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panNewTimeDeposit.add(tabNewTimeDeposit, gridBagConstraints);

        getContentPane().add(panNewTimeDeposit, java.awt.BorderLayout.CENTER);

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

        mbrNewTimeDeposit.setName("mbrCustomer");

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

        mbrNewTimeDeposit.add(mnuProcess);

        setJMenuBar(mbrNewTimeDeposit);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tdtStartDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtStartDateFocusLost
        // TODO add your handling code here:
        String startDate = tdtExecutionDate.getDateValue();
        ClientUtil.validateToDate(tdtStartDate, startDate);
    }//GEN-LAST:event_tdtStartDateFocusLost
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
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
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
        callView("Member");
    }//GEN-LAST:event_btnMemberActionPerformed
    
    private void txtOrderAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOrderAmountFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtOrderAmountFocusGained
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panNewTimeDeposit, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panNewTimeDeposit, true);
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
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboSettlementType;
    private com.see.truetransact.uicomponent.CLabel lblAccount;
    private com.see.truetransact.uicomponent.CLabel lblAdviseType;
    private com.see.truetransact.uicomponent.CLabel lblAdviseTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblAutorollInd;
    private com.see.truetransact.uicomponent.CLabel lblBankOfficeInstructions;
    private com.see.truetransact.uicomponent.CLabel lblClientAdvices;
    private com.see.truetransact.uicomponent.CLabel lblClientContact;
    private com.see.truetransact.uicomponent.CLabel lblClientContactValue;
    private com.see.truetransact.uicomponent.CLabel lblClientRate;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactDateValue;
    private com.see.truetransact.uicomponent.CLabel lblContactMode;
    private com.see.truetransact.uicomponent.CLabel lblContactModeValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditNotes;
    private com.see.truetransact.uicomponent.CLabel lblEntitlementGroup;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblLeadBanker;
    private com.see.truetransact.uicomponent.CLabel lblLeadBankerValue;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSO;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSOValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMemberId;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameValue;
    private com.see.truetransact.uicomponent.CLabel lblModeDetails;
    private com.see.truetransact.uicomponent.CLabel lblModeDetailsValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrderAmount;
    private com.see.truetransact.uicomponent.CLabel lblOrderSource;
    private com.see.truetransact.uicomponent.CLabel lblOrderSourceValue;
    private com.see.truetransact.uicomponent.CLabel lblPhoneOrder;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioAccount;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioLocation;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalAccount;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumber;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumberValue;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipValue;
    private com.see.truetransact.uicomponent.CLabel lblSettlementAccount;
    private com.see.truetransact.uicomponent.CLabel lblSettlementAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblSettlementType;
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
    private com.see.truetransact.uicomponent.CLabel lblSpread;
    private com.see.truetransact.uicomponent.CLabel lblStartDate;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTenor;
    private com.see.truetransact.uicomponent.CLabel lblTraderDealerInst;
    private com.see.truetransact.uicomponent.CMenuBar mbrNewTimeDeposit;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panInstructions;
    private com.see.truetransact.uicomponent.CPanel panLabels;
    private com.see.truetransact.uicomponent.CPanel panNewTimeDeposit;
    private com.see.truetransact.uicomponent.CPanel panOrder;
    private com.see.truetransact.uicomponent.CPanel panOrderDetails;
    private com.see.truetransact.uicomponent.CPanel panPhoneOrder;
    private com.see.truetransact.uicomponent.CPanel panPrincipalSettlement;
    private com.see.truetransact.uicomponent.CPanel panSafeKeepingPortFolio;
    private com.see.truetransact.uicomponent.CPanel panSettlementAccount;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTenor;
    private com.see.truetransact.uicomponent.CPanel panTimeDeposit;
    private com.see.truetransact.uicomponent.CPanel panTxtMember;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPhoneOrder;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_Yes;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CSeparator sptHorizontal;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabNewTimeDeposit;
    private javax.swing.JToolBar tbrNewTimeDeposit;
    private com.see.truetransact.uicomponent.CDateField tdtExecutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtMaturityDate;
    private com.see.truetransact.uicomponent.CDateField tdtStartDate;
    private com.see.truetransact.uicomponent.CTextField txtAccount;
    private com.see.truetransact.uicomponent.CTextField txtAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtAutorollInd;
    private com.see.truetransact.uicomponent.CTextField txtBankOfficeInstruction;
    private com.see.truetransact.uicomponent.CTextField txtClientAdvices;
    private com.see.truetransact.uicomponent.CTextField txtClientRate;
    private com.see.truetransact.uicomponent.CTextField txtCreditNotes;
    private com.see.truetransact.uicomponent.CTextField txtEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtOrderAmount;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioAccount;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtPrincipalAccount;
    private com.see.truetransact.uicomponent.CTextField txtPrincipalAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtSettlementAccount;
    private com.see.truetransact.uicomponent.CTextField txtSettlementAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtSpread;
    private com.see.truetransact.uicomponent.CTextField txtTenor1;
    private com.see.truetransact.uicomponent.CTextField txtTenor2;
    private com.see.truetransact.uicomponent.CTextField txtTraderDealerInst;
    // End of variables declaration//GEN-END:variables
    
}
