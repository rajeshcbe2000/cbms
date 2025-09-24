/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEnquitiesUI.java
 *
 * Created on July 7, 2004, 12:29 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.purchaseequities;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.ui.privatebanking.actionitem.purchaseequities.PurchaseEquitiesOB;
import com.see.truetransact.ui.privatebanking.actionitem.purchaseequities.PurchaseEquitiesRB;
import com.see.truetransact.ui.privatebanking.actionitem.purchaseequities.PurchaseEquitiesMRB;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientUtil ;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;

public class PurchaseEquitiesUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField {
    
    private PurchaseEquitiesOB observable;
    private PurchaseEquitiesMRB objMandatoryRB;
    private PurchaseEquitiesRB resourceBundle = new PurchaseEquitiesRB();
    private HashMap mandatoryMap;
    private String viewType = "";
    private String authorizeType = "";
    private final String AUTHORIZE = "Authorize";
    
    /** Creates new form PurchaseEnquitiesUI */
    public PurchaseEquitiesUI() {
        initGUI();
    }
    
    /** Intialiseds the GUI Components */
    private void initGUI(){
        initComponents();
        setFieldNames();
        setObservable();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLengths();
        observable.resetForm();
        clearLabels();
        initComponentData();
        ClientUtil.enableDisable(panPurchaseEnquity, false);
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
        cboCommission.setName("cboCommission");
        cboCurrency.setName("cboCurrency");
        cboLodgementFee.setName("cboLodgementFee");
        cboMinAmount.setName("cboMinAmount");
        cboOrderSubType.setName("cboOrderSubType");
        cboOrderType.setName("cboOrderType");
        lblAdviseType.setName("lblAdviseType");
        lblAdviseTypeValue.setName("lblAdviseTypeValue");
        lblApproxAmount.setName("lblApproxAmount");
        lblBankOfficeInstructions.setName("lblBankOfficeInstructions");
        lblClientAdvices.setName("lblClientAdvices");
        lblClientContact.setName("lblClientContact");
        lblClientContactValue.setName("lblClientContactValue");
        lblCommRate.setName("lblCommRate");
        lblCommType.setName("lblCommType");
        lblCommission.setName("lblCommission");
        lblContactDate.setName("lblContactDate");
        lblContactDateValue.setName("lblContactDateValue");
        lblContactMode.setName("lblContactMode");
        lblContactModeValue.setName("lblContactModeValue");
        lblCreditNotes.setName("lblCreditNotes");
        lblCurrency.setName("lblCurrency");
        lblDealerName.setName("lblDealerName");
        lblEdtsEligible.setName("lblEdtsEligible");
        lblEntitlementGroup.setName("lblEntitlementGroup");
        lblExchange.setName("lblExchange");
        lblExecutionDate.setName("lblExecutionDate");
        lblLeadBanker.setName("lblLeadBanker");
        lblLeadBankerValue.setName("lblLeadBankerValue");
        lblLeadRSO.setName("lblLeadRSO");
        lblLeadRSOValue.setName("lblLeadRSOValue");
        lblLodgementFee.setName("lblLodgementFee");
        lblLotSize.setName("lblLotSize");
        lblMemberId.setName("lblMemberId");
        lblMemberName.setName("lblMemberName");
        lblMemberNameValue.setName("lblMemberNameValue");
        lblMinCommissionAmount.setName("lblMinCommissionAmount");
        lblModeDetails.setName("lblModeDetails");
        lblModeDetailsValue.setName("lblModeDetailsValue");
        lblMsg.setName("lblMsg");
        lblOrderSource.setName("lblOrderSource");
        lblOrderSourceValue.setName("lblOrderSourceValue");
        lblOrderSubType.setName("lblOrderSubType");
        lblOrderType.setName("lblOrderType");
        lblPhoneOrder.setName("lblPhoneOrder");
        lblPortfolioAccount.setName("lblPortfolioAccount");
        lblPortfolioAssetSubClass.setName("lblPortfolioAssetSubClass");
        lblPortfolioLocation.setName("lblPortfolioLocation");
        lblPrice.setName("lblPrice");
        lblProcessThruEdts.setName("lblProcessThruEdts");
        lblReferenceNumber.setName("lblReferenceNumber");
        lblReferenceNumberValue.setName("lblReferenceNumberValue");
        lblRelationship.setName("lblRelationship");
        lblRelationshipValue.setName("lblRelationshipValue");
        lblSMIInfo.setName("lblSMIInfo");
        lblSettlementAccount.setName("lblSettlementAccount");
        lblSettlementAssetSubClass.setName("lblSettlementAssetSubClass");
        lblSettlementDate.setName("lblSettlementDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblTillDate.setName("lblTillDate");
        lblTraderDealerInst.setName("lblTraderDealerInst");
        lblUnits.setName("lblUnits");
        mbrPurchaseEnquities.setName("mbrPurchaseEnquities");
        panCommission.setName("panCommission");
        panEdtsEligible.setName("panEdtsEligible");
        panEnquities.setName("panEnquities");
        panInstruction.setName("panInstruction");
        panLabels.setName("panLabels");
        panLodgementFee.setName("panLodgementFee");
        panMinCommissionAmount.setName("panMinCommissionAmount");
        panOrderDetails.setName("panOrderDetails");
        panOrders.setName("panOrders");
        panPhoneOrder.setName("panPhoneOrder");
        panProcessThruEdts.setName("panProcessThruEdts");
        panPurchaseEnquity.setName("panPurchaseEnquity");
        panSafeKeepingPortfolio.setName("panSafeKeepingPortfolio");
        panSettlementAccount.setName("panSettlementAccount");
        panStatus.setName("panStatus");
        panTxtMember.setName("panTxtMember");
        rdoEDTSEligible_No.setName("rdoEDTSEligible_No");
        rdoEDTSEligible_Yes.setName("rdoEDTSEligible_Yes");
        rdoPhoneOrder_No.setName("rdoPhoneOrder_No");
        rdoPhoneOrder_Yes.setName("rdoPhoneOrder_Yes");
        rdoProcessthruEdts_No.setName("rdoProcessthruEdts_No");
        rdoProcessthruEdts_Yes.setName("rdoProcessthruEdts_Yes");
        sptHorizonta.setName("sptHorizonta");
        tabEnquities.setName("tabEnquities");
        tdtExecutionDate.setName("tdtExecutionDate");
        tdtSettlementDate.setName("tdtSettlementDate");
        tdtTillDate.setName("tdtTillDate");
        txtApproxAmount.setName("txtApproxAmount");
        txtBankOfficeInstruction.setName("txtBankOfficeInstruction");
        txtClientAdvices.setName("txtClientAdvices");
        txtCommRate.setName("txtCommRate");
        cboCommType.setName("cboCommType");
        txtCommission.setName("txtCommission");
        txtCreditNotes.setName("txtCreditNotes");
        txtDealerName.setName("txtDealerName");
        txtEntitlementGroup.setName("txtEntitlementGroup");
        txtExchange.setName("txtExchange");
        txtLodgementFee.setName("txtLodgementFee");
        txtLotSize.setName("txtLotSize");
        txtMember.setName("txtMember");
        txtMinCommAmount.setName("txtMinCommAmount");
        txtPortfolioAccount.setName("txtPortfolioAccount");
        txtPortfolioAssetSubClass.setName("txtPortfolioAssetSubClass");
        txtPortfolioLocation.setName("txtPortfolioLocation");
        txtPrice.setName("txtPrice");
        txtSMIInfo.setName("txtSMIInfo");
        txtSettlementAccount.setName("txtSettlementAccount");
        txtSettlementAssetSubClass.setName("txtSettlementAssetSubClass");
        txtTraderDealerInst.setName("txtTraderDealerInst");
        txtUnits.setName("txtUnits");
    }
    
    /** Setting the observable for PurchaseEquitiesUI */
    private void setObservable(){
        try{
            observable = PurchaseEquitiesOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            
        }
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblExecutionDate.setText(resourceBundle.getString("lblExecutionDate"));
        lblSettlementAccount.setText(resourceBundle.getString("lblSettlementAccount"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTillDate.setText(resourceBundle.getString("lblTillDate"));
        lblContactMode.setText(resourceBundle.getString("lblContactMode"));
        lblLeadRSOValue.setText(resourceBundle.getString("lblLeadRSOValue"));
        lblAdviseTypeValue.setText(resourceBundle.getString("lblAdviseTypeValue"));
        lblCommRate.setText(resourceBundle.getString("lblCommRate"));
        lblMinCommissionAmount.setText(resourceBundle.getString("lblMinCommissionAmount"));
        lblPortfolioAccount.setText(resourceBundle.getString("lblPortfolioAccount"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        rdoEDTSEligible_Yes.setText(resourceBundle.getString("rdoEDTSEligible_Yes"));
        lblLodgementFee.setText(resourceBundle.getString("lblLodgementFee"));
        lblOrderSourceValue.setText(resourceBundle.getString("lblOrderSourceValue"));
        lblClientContactValue.setText(resourceBundle.getString("lblClientContactValue"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblMemberId.setText(resourceBundle.getString("lblMemberId"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSettlementDate.setText(resourceBundle.getString("lblSettlementDate"));
        rdoEDTSEligible_No.setText(resourceBundle.getString("rdoEDTSEligible_No"));
        lblExchange.setText(resourceBundle.getString("lblExchange"));
        lblOrderType.setText(resourceBundle.getString("lblOrderType"));
        lblReferenceNumber.setText(resourceBundle.getString("lblReferenceNumber"));
        lblLeadRSO.setText(resourceBundle.getString("lblLeadRSO"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblEdtsEligible.setText(resourceBundle.getString("lblEdtsEligible"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRelationshipValue.setText(resourceBundle.getString("lblRelationshipValue"));
        lblSettlementAssetSubClass.setText(resourceBundle.getString("lblSettlementAssetSubClass"));
        lblPortfolioAssetSubClass.setText(resourceBundle.getString("lblPortfolioAssetSubClass"));
        lblMemberNameValue.setText(resourceBundle.getString("lblMemberNameValue"));
        lblLeadBanker.setText(resourceBundle.getString("lblLeadBanker"));
        lblLeadBankerValue.setText(resourceBundle.getString("lblLeadBankerValue"));
        lblPhoneOrder.setText(resourceBundle.getString("lblPhoneOrder"));
        lblAdviseType.setText(resourceBundle.getString("lblAdviseType"));
        lblTraderDealerInst.setText(resourceBundle.getString("lblTraderDealerInst"));
        lblUnits.setText(resourceBundle.getString("lblUnits"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblModeDetailsValue.setText(resourceBundle.getString("lblModeDetailsValue"));
        lblLotSize.setText(resourceBundle.getString("lblLotSize"));
        lblOrderSubType.setText(resourceBundle.getString("lblOrderSubType"));
        lblSMIInfo.setText(resourceBundle.getString("lblSMIInfo"));
        lblContactModeValue.setText(resourceBundle.getString("lblContactModeValue"));
        ((javax.swing.border.TitledBorder)panSettlementAccount.getBorder()).setTitle(resourceBundle.getString("panSettlementAccount"));
        lblEntitlementGroup.setText(resourceBundle.getString("lblEntitlementGroup"));
        rdoProcessthruEdts_No.setText(resourceBundle.getString("rdoProcessthruEdts_No"));
        lblContactDateValue.setText(resourceBundle.getString("lblContactDateValue"));
        lblBankOfficeInstructions.setText(resourceBundle.getString("lblBankOfficeInstructions"));
        lblCurrency.setText(resourceBundle.getString("lblCurrency"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblCommType.setText(resourceBundle.getString("lblCommType"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblApproxAmount.setText(resourceBundle.getString("lblApproxAmount"));
        lblClientContact.setText(resourceBundle.getString("lblClientContact"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblOrderSource.setText(resourceBundle.getString("lblOrderSource"));
        lblDealerName.setText(resourceBundle.getString("lblDealerName"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblProcessThruEdts.setText(resourceBundle.getString("lblProcessThruEdts"));
        rdoProcessthruEdts_Yes.setText(resourceBundle.getString("rdoProcessthruEdts_Yes"));
        btnMember.setText(resourceBundle.getString("btnMember"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCreditNotes.setText(resourceBundle.getString("lblCreditNotes"));
        lblPortfolioLocation.setText(resourceBundle.getString("lblPortfolioLocation"));
        lblModeDetails.setText(resourceBundle.getString("lblModeDetails"));
        lblCommission.setText(resourceBundle.getString("lblCommission"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblPrice.setText(resourceBundle.getString("lblPrice"));
        rdoPhoneOrder_Yes.setText(resourceBundle.getString("rdoPhoneOrder_Yes"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        rdoPhoneOrder_No.setText(resourceBundle.getString("rdoPhoneOrder_No"));
        ((javax.swing.border.TitledBorder)panSafeKeepingPortfolio.getBorder()).setTitle(resourceBundle.getString("panSafeKeepingPortfolio"));
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
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("tdtSettlementDate", new Boolean(true));
        mandatoryMap.put("rdoEDTSEligible_Yes", new Boolean(true));
        mandatoryMap.put("txtSMIInfo", new Boolean(true));
        mandatoryMap.put("cboOrderType", new Boolean(true));
        mandatoryMap.put("cboOrderSubType", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtDealerName", new Boolean(true));
        mandatoryMap.put("txtLotSize", new Boolean(true));
        mandatoryMap.put("txtUnits", new Boolean(true));
        mandatoryMap.put("txtPrice", new Boolean(true));
        mandatoryMap.put("cboCommType", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("cboCommission", new Boolean(true));
        mandatoryMap.put("tdtTillDate", new Boolean(true));
        mandatoryMap.put("rdoProcessthruEdts_Yes", new Boolean(true));
        mandatoryMap.put("txtExchange", new Boolean(true));
        mandatoryMap.put("txtLodgementFee", new Boolean(true));
        mandatoryMap.put("cboLodgementFee", new Boolean(true));
        mandatoryMap.put("txtCommRate", new Boolean(true));
        mandatoryMap.put("txtApproxAmount", new Boolean(true));
        mandatoryMap.put("txtMinCommAmount", new Boolean(true));
        mandatoryMap.put("cboMinAmount", new Boolean(true));
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
        objMandatoryRB = new PurchaseEquitiesMRB();
        txtMember.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMember"));
        txtEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEntitlementGroup"));
        txtPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioLocation"));
        txtPortfolioAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioAssetSubClass"));
        txtPortfolioAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioAccount"));
        txtSettlementAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSettlementAssetSubClass"));
        txtSettlementAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSettlementAccount"));
        tdtExecutionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecutionDate"));
        tdtSettlementDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSettlementDate"));
        rdoEDTSEligible_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoEDTSEligible_Yes"));
        txtSMIInfo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSMIInfo"));
        cboOrderType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOrderType"));
        cboOrderSubType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOrderSubType"));
        rdoPhoneOrder_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPhoneOrder_Yes"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        txtDealerName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDealerName"));
        txtLotSize.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLotSize"));
        txtUnits.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUnits"));
        txtPrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrice"));
        cboCommType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommType"));
        txtCommission.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommission"));
        cboCommission.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommission"));
        tdtTillDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTillDate"));
        rdoProcessthruEdts_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoProcessthruEdts_Yes"));
        txtExchange.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExchange"));
        txtLodgementFee.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLodgementFee"));
        cboLodgementFee.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLodgementFee"));
        txtCommRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommRate"));
        txtApproxAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApproxAmount"));
        txtMinCommAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinCommAmount"));
        cboMinAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinAmount"));
        txtBankOfficeInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankOfficeInstruction"));
        txtTraderDealerInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTraderDealerInst"));
        txtCreditNotes.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNotes"));
        txtClientAdvices.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClientAdvices"));
    }
    
    /** Sets the Maximum allowed Lengths for the Textfields */
    private void setMaximumLengths(){
        txtMember.setMaxLength(16);
        txtEntitlementGroup.setMaxLength(32);
        txtPortfolioLocation.setMaxLength(32);
        txtPortfolioAssetSubClass.setMaxLength(32);
        txtPortfolioAccount.setMaxLength(32);
        txtSettlementAssetSubClass.setMaxLength(32);
        txtSettlementAccount.setMaxLength(32);
        txtSMIInfo.setMaxLength(1024);
        txtDealerName.setMaxLength(64);
        txtLotSize.setMaxLength(16);
        txtUnits.setMaxLength(16);
        txtPrice.setMaxLength(16);
        txtCommission.setMaxLength(16);
        txtExchange.setMaxLength(32);
        txtLodgementFee.setMaxLength(16);
        txtCommRate.setMaxLength(5);
        txtApproxAmount.setMaxLength(16);
        txtMinCommAmount.setMaxLength(16);
        txtBankOfficeInstruction.setMaxLength(1024);
        txtCreditNotes.setMaxLength(1024);
        txtTraderDealerInst.setMaxLength(1024);
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
        tdtExecutionDate.setDateValue(observable.getTdtExecutionDate());
        tdtSettlementDate.setDateValue(observable.getTdtSettlementDate());
        rdoEDTSEligible_Yes.setSelected(observable.getRdoEDTSEligible_Yes());
        rdoEDTSEligible_No.setSelected(observable.getRdoEDTSEligible_No());
        txtSMIInfo.setText(observable.getTxtSMIInfo());
        cboOrderType.setSelectedItem(observable.getCboOrderType());
        cboOrderSubType.setSelectedItem(observable.getCboOrderSubType());
        rdoPhoneOrder_Yes.setSelected(observable.getRdoPhoneOrder_Yes());
        rdoPhoneOrder_No.setSelected(observable.getRdoPhoneOrder_No());
        cboCurrency.setSelectedItem(observable.getCboCurrency());
        txtDealerName.setText(observable.getTxtDealerName());
        txtLotSize.setText(observable.getTxtLotSize());
        txtUnits.setText(observable.getTxtUnits());
        txtPrice.setText(observable.getTxtPrice());
        cboCommType.setSelectedItem(observable.getCboCommType());
        txtCommission.setText(observable.getTxtCommission());
        cboCommission.setSelectedItem(observable.getCboCommission());
        tdtTillDate.setDateValue(observable.getTdtTillDate());
        rdoProcessthruEdts_Yes.setSelected(observable.getRdoProcessthruEdts_Yes());
        rdoProcessthruEdts_No.setSelected(observable.getRdoProcessthruEdts_No());
        txtExchange.setText(observable.getTxtExchange());
        txtLodgementFee.setText(observable.getTxtLodgementFee());
        cboLodgementFee.setSelectedItem(observable.getCboLodgementFee());
        txtCommRate.setText(observable.getTxtCommRate());
        txtApproxAmount.setText(observable.getTxtApproxAmount());
        txtMinCommAmount.setText(observable.getTxtMinCommAmount());
        cboMinAmount.setSelectedItem(observable.getCboMinAmount());
        txtBankOfficeInstruction.setText(observable.getTxtBankOfficeInstruction());
        txtTraderDealerInst.setText(observable.getTxtTraderDealerInst());
        txtCreditNotes.setText(observable.getTxtCreditNotes());
        txtClientAdvices.setText(observable.getTxtClientAdvices());
    }
    
    /** Sets the model for the Comboboxes in the UI */
    private void initComponentData(){
        cboOrderType.setModel(observable.getCbmOrderType());
        cboOrderSubType.setModel(observable.getCbmOrderSubType());
        cboCurrency.setModel(observable.getCbmCurrency());
        cboCommType.setModel(observable.getCbmCommType());
        cboCommission.setModel(observable.getCbmCommission());
        cboLodgementFee.setModel(observable.getCbmLodgementFee());
        cboMinAmount.setModel(observable.getCbmMinAmount());
        
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
        observable.setTdtExecutionDate(tdtExecutionDate.getDateValue());
        observable.setTdtSettlementDate(tdtSettlementDate.getDateValue());
        observable.setRdoEDTSEligible_Yes(rdoEDTSEligible_Yes.isSelected());
        observable.setRdoEDTSEligible_No(rdoEDTSEligible_No.isSelected());
        observable.setTxtSMIInfo(txtSMIInfo.getText());
        observable.setCboOrderType(CommonUtil.convertObjToStr(cboOrderType.getSelectedItem()));
        observable.setCboOrderSubType(CommonUtil.convertObjToStr(cboOrderSubType.getSelectedItem()));
        observable.setRdoPhoneOrder_Yes(rdoPhoneOrder_Yes.isSelected());
        observable.setRdoPhoneOrder_No(rdoPhoneOrder_No.isSelected());
        observable.setCboCurrency(CommonUtil.convertObjToStr(cboCurrency.getSelectedItem()));
        observable.setTxtDealerName(txtDealerName.getText());
        observable.setTxtLotSize(txtLotSize.getText());
        observable.setTxtUnits(txtUnits.getText());
        observable.setTxtPrice(txtPrice.getText());
        observable.setCboCommType(CommonUtil.convertObjToStr(cboCommType.getSelectedItem()));
        observable.setTxtCommission(txtCommission.getText());
        observable.setCboCommission(CommonUtil.convertObjToStr(cboCommission.getSelectedItem()));
        observable.setTdtTillDate(tdtTillDate.getDateValue());
        observable.setRdoProcessthruEdts_Yes(rdoProcessthruEdts_Yes.isSelected());
        observable.setRdoProcessthruEdts_No(rdoProcessthruEdts_No.isSelected());
        observable.setTxtExchange(txtExchange.getText());
        observable.setTxtLodgementFee(txtLodgementFee.getText());
        observable.setCboLodgementFee(CommonUtil.convertObjToStr(cboLodgementFee.getSelectedItem()));
        observable.setTxtCommRate(txtCommRate.getText());
        observable.setTxtApproxAmount(txtApproxAmount.getText());
        observable.setTxtMinCommAmount(txtMinCommAmount.getText());
        observable.setCboMinAmount(CommonUtil.convertObjToStr(cboMinAmount.getSelectedItem()));
        observable.setTxtBankOfficeInstruction(txtBankOfficeInstruction.getText());
        observable.setTxtTraderDealerInst(txtTraderDealerInst.getText());
        observable.setTxtCreditNotes(txtCreditNotes.getText());
        observable.setTxtClientAdvices(txtClientAdvices.getText());
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
    
    /** This method shows the ViewAll Screen with the rows filled up according to query executed */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtAIPurchaseEquity");
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
                    hash.put(CommonConstants.MAP_WHERE, hash.get("PURCHASE_ID"));
                    observable.populateData(hash);
                    hash = null;
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panPurchaseEnquity, false);
                    } else {
                        ClientUtil.enableDisable(panPurchaseEnquity, true);
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
        
        final String mandatoryMessage = checkMandatory(panPurchaseEnquity);
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
        ClientUtil.enableDisable(panPurchaseEnquity, false);
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
            mapParam.put(CommonConstants.MAP_NAME, "getPurchaseEquityAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizePurchaseEquity");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            mapParam = null;
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (authorizeType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("PURCHASE_ID", lblReferenceNumberValue.getText());
            
            ClientUtil.execute("authorizePurchaseEquity", singleAuthorizeMap);
            singleAuthorizeMap = null;
            authorizeType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame=new javax.swing.JFrame();
        PurchaseEquitiesUI  tui = new PurchaseEquitiesUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,700);
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

        rdoEDTSEligible = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPhoneOrder = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoProcessthruEDTS = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrPurchaseEnquities = new javax.swing.JToolBar();
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
        panPurchaseEnquity = new com.see.truetransact.uicomponent.CPanel();
        tabEnquities = new com.see.truetransact.uicomponent.CTabbedPane();
        panEnquities = new com.see.truetransact.uicomponent.CPanel();
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
        panSafeKeepingPortfolio = new com.see.truetransact.uicomponent.CPanel();
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
        panOrderDetails = new com.see.truetransact.uicomponent.CPanel();
        panOrders = new com.see.truetransact.uicomponent.CPanel();
        lblExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtExecutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblSettlementDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSettlementDate = new com.see.truetransact.uicomponent.CDateField();
        lblEdtsEligible = new com.see.truetransact.uicomponent.CLabel();
        panEdtsEligible = new com.see.truetransact.uicomponent.CPanel();
        rdoEDTSEligible_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEDTSEligible_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSMIInfo = new com.see.truetransact.uicomponent.CLabel();
        txtSMIInfo = new com.see.truetransact.uicomponent.CTextField();
        lblOrderType = new com.see.truetransact.uicomponent.CLabel();
        cboOrderType = new com.see.truetransact.uicomponent.CComboBox();
        lblOrderSubType = new com.see.truetransact.uicomponent.CLabel();
        cboOrderSubType = new com.see.truetransact.uicomponent.CComboBox();
        lblPhoneOrder = new com.see.truetransact.uicomponent.CLabel();
        panPhoneOrder = new com.see.truetransact.uicomponent.CPanel();
        rdoPhoneOrder_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPhoneOrder_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblDealerName = new com.see.truetransact.uicomponent.CLabel();
        txtDealerName = new com.see.truetransact.uicomponent.CTextField();
        lblLotSize = new com.see.truetransact.uicomponent.CLabel();
        txtLotSize = new com.see.truetransact.uicomponent.CTextField();
        lblUnits = new com.see.truetransact.uicomponent.CLabel();
        txtUnits = new com.see.truetransact.uicomponent.CTextField();
        lblPrice = new com.see.truetransact.uicomponent.CLabel();
        txtPrice = new com.see.truetransact.uicomponent.CTextField();
        lblCommType = new com.see.truetransact.uicomponent.CLabel();
        lblCommission = new com.see.truetransact.uicomponent.CLabel();
        panCommission = new com.see.truetransact.uicomponent.CPanel();
        txtCommission = new com.see.truetransact.uicomponent.CTextField();
        cboCommission = new com.see.truetransact.uicomponent.CComboBox();
        lblTillDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTillDate = new com.see.truetransact.uicomponent.CDateField();
        lblProcessThruEdts = new com.see.truetransact.uicomponent.CLabel();
        panProcessThruEdts = new com.see.truetransact.uicomponent.CPanel();
        rdoProcessthruEdts_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoProcessthruEdts_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblExchange = new com.see.truetransact.uicomponent.CLabel();
        txtExchange = new com.see.truetransact.uicomponent.CTextField();
        lblLodgementFee = new com.see.truetransact.uicomponent.CLabel();
        panLodgementFee = new com.see.truetransact.uicomponent.CPanel();
        txtLodgementFee = new com.see.truetransact.uicomponent.CTextField();
        cboLodgementFee = new com.see.truetransact.uicomponent.CComboBox();
        lblCommRate = new com.see.truetransact.uicomponent.CLabel();
        txtCommRate = new com.see.truetransact.uicomponent.CTextField();
        lblApproxAmount = new com.see.truetransact.uicomponent.CLabel();
        txtApproxAmount = new com.see.truetransact.uicomponent.CTextField();
        lblMinCommissionAmount = new com.see.truetransact.uicomponent.CLabel();
        panMinCommissionAmount = new com.see.truetransact.uicomponent.CPanel();
        txtMinCommAmount = new com.see.truetransact.uicomponent.CTextField();
        cboMinAmount = new com.see.truetransact.uicomponent.CComboBox();
        sptHorizonta = new com.see.truetransact.uicomponent.CSeparator();
        cboCommType = new com.see.truetransact.uicomponent.CComboBox();
        panInstruction = new com.see.truetransact.uicomponent.CPanel();
        lblBankOfficeInstructions = new com.see.truetransact.uicomponent.CLabel();
        txtBankOfficeInstruction = new com.see.truetransact.uicomponent.CTextField();
        lblTraderDealerInst = new com.see.truetransact.uicomponent.CLabel();
        txtTraderDealerInst = new com.see.truetransact.uicomponent.CTextField();
        lblCreditNotes = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNotes = new com.see.truetransact.uicomponent.CTextField();
        lblClientAdvices = new com.see.truetransact.uicomponent.CLabel();
        txtClientAdvices = new com.see.truetransact.uicomponent.CTextField();
        mbrPurchaseEnquities = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(650, 530));
        setPreferredSize(new java.awt.Dimension(650, 530));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnDelete);

        lblSpace2.setText("     ");
        tbrPurchaseEnquities.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnCancel);

        lblSpace3.setText("     ");
        tbrPurchaseEnquities.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnReject);

        lblSpace4.setText("     ");
        tbrPurchaseEnquities.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrPurchaseEnquities.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPurchaseEnquities.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrPurchaseEnquities.add(btnClose);

        getContentPane().add(tbrPurchaseEnquities, java.awt.BorderLayout.NORTH);

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

        panPurchaseEnquity.setLayout(new java.awt.GridBagLayout());

        panEnquities.setLayout(new java.awt.GridBagLayout());

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

        panEnquities.add(panLabels, new java.awt.GridBagConstraints());

        panSafeKeepingPortfolio.setBorder(javax.swing.BorderFactory.createTitledBorder("Safe keeping Portfolio"));
        panSafeKeepingPortfolio.setLayout(new java.awt.GridBagLayout());

        lblEntitlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panSafeKeepingPortfolio.add(lblEntitlementGroup, gridBagConstraints);

        txtEntitlementGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(txtEntitlementGroup, gridBagConstraints);

        lblPortfolioLocation.setText("Portfolio-Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panSafeKeepingPortfolio.add(lblPortfolioLocation, gridBagConstraints);

        txtPortfolioLocation.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPortfolioLocation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(txtPortfolioLocation, gridBagConstraints);

        lblPortfolioAssetSubClass.setText("Asset Sub - Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(lblPortfolioAssetSubClass, gridBagConstraints);

        txtPortfolioAssetSubClass.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(txtPortfolioAssetSubClass, gridBagConstraints);

        lblPortfolioAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(lblPortfolioAccount, gridBagConstraints);

        txtPortfolioAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSafeKeepingPortfolio.add(txtPortfolioAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panEnquities.add(panSafeKeepingPortfolio, gridBagConstraints);

        panSettlementAccount.setBorder(javax.swing.BorderFactory.createTitledBorder("Settlement Account"));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEnquities.add(panSettlementAccount, gridBagConstraints);

        tabEnquities.addTab("Purchase Enquity", panEnquities);

        panOrderDetails.setLayout(new java.awt.GridBagLayout());

        panOrders.setLayout(new java.awt.GridBagLayout());

        lblExecutionDate.setText("Execution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panOrders.add(lblExecutionDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(tdtExecutionDate, gridBagConstraints);

        lblSettlementDate.setText("Settlement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblSettlementDate, gridBagConstraints);

        tdtSettlementDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSettlementDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(tdtSettlementDate, gridBagConstraints);

        lblEdtsEligible.setText("EDTS Eligible");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblEdtsEligible, gridBagConstraints);

        panEdtsEligible.setLayout(new java.awt.GridBagLayout());

        rdoEDTSEligible.add(rdoEDTSEligible_Yes);
        rdoEDTSEligible_Yes.setText("Yes");
        panEdtsEligible.add(rdoEDTSEligible_Yes, new java.awt.GridBagConstraints());

        rdoEDTSEligible.add(rdoEDTSEligible_No);
        rdoEDTSEligible_No.setText("No");
        panEdtsEligible.add(rdoEDTSEligible_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panEdtsEligible, gridBagConstraints);

        lblSMIInfo.setText("SMI Info");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblSMIInfo, gridBagConstraints);

        txtSMIInfo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSMIInfo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSMIInfoFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtSMIInfo, gridBagConstraints);

        lblOrderType.setText("Order Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblOrderType, gridBagConstraints);

        cboOrderType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(cboOrderType, gridBagConstraints);

        lblOrderSubType.setText("Order Sub Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblOrderSubType, gridBagConstraints);

        cboOrderSubType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(cboOrderSubType, gridBagConstraints);

        lblPhoneOrder.setText("Phone Order");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblPhoneOrder, gridBagConstraints);

        panPhoneOrder.setLayout(new java.awt.GridBagLayout());

        rdoPhoneOrder.add(rdoPhoneOrder_Yes);
        rdoPhoneOrder_Yes.setText("Yes");
        panPhoneOrder.add(rdoPhoneOrder_Yes, new java.awt.GridBagConstraints());

        rdoPhoneOrder.add(rdoPhoneOrder_No);
        rdoPhoneOrder_No.setText("No");
        panPhoneOrder.add(rdoPhoneOrder_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panPhoneOrder, gridBagConstraints);

        lblCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblCurrency, gridBagConstraints);

        cboCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(cboCurrency, gridBagConstraints);

        lblDealerName.setText("Dealer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblDealerName, gridBagConstraints);

        txtDealerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtDealerName, gridBagConstraints);

        lblLotSize.setText("Lot Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblLotSize, gridBagConstraints);

        txtLotSize.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtLotSize, gridBagConstraints);

        lblUnits.setText("Units");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblUnits, gridBagConstraints);

        txtUnits.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtUnits, gridBagConstraints);

        lblPrice.setText("Price");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblPrice, gridBagConstraints);

        txtPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtPrice, gridBagConstraints);

        lblCommType.setText("Comm Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblCommType, gridBagConstraints);

        lblCommission.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblCommission, gridBagConstraints);

        panCommission.setLayout(new java.awt.GridBagLayout());

        txtCommission.setMinimumSize(new java.awt.Dimension(46, 21));
        txtCommission.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panCommission.add(txtCommission, gridBagConstraints);

        cboCommission.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panCommission.add(cboCommission, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panCommission, gridBagConstraints);

        lblTillDate.setText("Good Till Date / Good Till Cancel");
        lblTillDate.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblTillDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(tdtTillDate, gridBagConstraints);

        lblProcessThruEdts.setText("Process thru EDTS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblProcessThruEdts, gridBagConstraints);

        panProcessThruEdts.setLayout(new java.awt.GridBagLayout());

        rdoProcessthruEDTS.add(rdoProcessthruEdts_Yes);
        rdoProcessthruEdts_Yes.setText("Yes");
        panProcessThruEdts.add(rdoProcessthruEdts_Yes, new java.awt.GridBagConstraints());

        rdoProcessthruEDTS.add(rdoProcessthruEdts_No);
        rdoProcessthruEdts_No.setText("No");
        panProcessThruEdts.add(rdoProcessthruEdts_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panProcessThruEdts, gridBagConstraints);

        lblExchange.setText("Exchange");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblExchange, gridBagConstraints);

        txtExchange.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtExchange, gridBagConstraints);

        lblLodgementFee.setText(" Lodgement Withdrawal Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblLodgementFee, gridBagConstraints);

        panLodgementFee.setLayout(new java.awt.GridBagLayout());

        txtLodgementFee.setMinimumSize(new java.awt.Dimension(46, 21));
        txtLodgementFee.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLodgementFee.add(txtLodgementFee, gridBagConstraints);

        cboLodgementFee.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLodgementFee.add(cboLodgementFee, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panLodgementFee, gridBagConstraints);

        lblCommRate.setText("Comm Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblCommRate, gridBagConstraints);

        txtCommRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtCommRate, gridBagConstraints);

        lblApproxAmount.setText("Approx Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblApproxAmount, gridBagConstraints);

        txtApproxAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(txtApproxAmount, gridBagConstraints);

        lblMinCommissionAmount.setText("Min Commission Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(lblMinCommissionAmount, gridBagConstraints);

        panMinCommissionAmount.setLayout(new java.awt.GridBagLayout());

        txtMinCommAmount.setMinimumSize(new java.awt.Dimension(46, 21));
        txtMinCommAmount.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panMinCommissionAmount.add(txtMinCommAmount, gridBagConstraints);

        cboMinAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panMinCommissionAmount.add(cboMinAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOrders.add(panMinCommissionAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panOrders.add(sptHorizonta, gridBagConstraints);

        cboCommType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrders.add(cboCommType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panOrderDetails.add(panOrders, gridBagConstraints);

        panInstruction.setLayout(new java.awt.GridBagLayout());

        lblBankOfficeInstructions.setText("Bank-Office Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panInstruction.add(lblBankOfficeInstructions, gridBagConstraints);

        txtBankOfficeInstruction.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankOfficeInstruction.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(txtBankOfficeInstruction, gridBagConstraints);

        lblTraderDealerInst.setText("Trader / Dealer Instructions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 56, 4, 4);
        panInstruction.add(lblTraderDealerInst, gridBagConstraints);

        txtTraderDealerInst.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTraderDealerInst.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(txtTraderDealerInst, gridBagConstraints);

        lblCreditNotes.setText("Credit Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(lblCreditNotes, gridBagConstraints);

        txtCreditNotes.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(txtCreditNotes, gridBagConstraints);

        lblClientAdvices.setText("Client Advices");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(lblClientAdvices, gridBagConstraints);

        txtClientAdvices.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstruction.add(txtClientAdvices, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panOrderDetails.add(panInstruction, gridBagConstraints);

        tabEnquities.addTab("Order Details", panOrderDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPurchaseEnquity.add(tabEnquities, gridBagConstraints);

        getContentPane().add(panPurchaseEnquity, java.awt.BorderLayout.CENTER);

        mbrPurchaseEnquities.setName("mbrCustomer");

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

        mbrPurchaseEnquities.add(mnuProcess);

        setJMenuBar(mbrPurchaseEnquities);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tdtSettlementDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSettlementDateFocusLost
        // TODO add your handling code here:
        String startDate = tdtExecutionDate.getDateValue();
        ClientUtil.validateToDate(tdtSettlementDate, startDate);
    }//GEN-LAST:event_tdtSettlementDateFocusLost
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void txtSMIInfoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSMIInfoFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtSMIInfoFocusGained
    
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
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panPurchaseEnquity, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panPurchaseEnquity, true);
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
    private com.see.truetransact.uicomponent.CComboBox cboCommType;
    private com.see.truetransact.uicomponent.CComboBox cboCommission;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboLodgementFee;
    private com.see.truetransact.uicomponent.CComboBox cboMinAmount;
    private com.see.truetransact.uicomponent.CComboBox cboOrderSubType;
    private com.see.truetransact.uicomponent.CComboBox cboOrderType;
    private com.see.truetransact.uicomponent.CLabel lblAdviseType;
    private com.see.truetransact.uicomponent.CLabel lblAdviseTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblApproxAmount;
    private com.see.truetransact.uicomponent.CLabel lblBankOfficeInstructions;
    private com.see.truetransact.uicomponent.CLabel lblClientAdvices;
    private com.see.truetransact.uicomponent.CLabel lblClientContact;
    private com.see.truetransact.uicomponent.CLabel lblClientContactValue;
    private com.see.truetransact.uicomponent.CLabel lblCommRate;
    private com.see.truetransact.uicomponent.CLabel lblCommType;
    private com.see.truetransact.uicomponent.CLabel lblCommission;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactDateValue;
    private com.see.truetransact.uicomponent.CLabel lblContactMode;
    private com.see.truetransact.uicomponent.CLabel lblContactModeValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditNotes;
    private com.see.truetransact.uicomponent.CLabel lblCurrency;
    private com.see.truetransact.uicomponent.CLabel lblDealerName;
    private com.see.truetransact.uicomponent.CLabel lblEdtsEligible;
    private com.see.truetransact.uicomponent.CLabel lblEntitlementGroup;
    private com.see.truetransact.uicomponent.CLabel lblExchange;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblLeadBanker;
    private com.see.truetransact.uicomponent.CLabel lblLeadBankerValue;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSO;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSOValue;
    private com.see.truetransact.uicomponent.CLabel lblLodgementFee;
    private com.see.truetransact.uicomponent.CLabel lblLotSize;
    private com.see.truetransact.uicomponent.CLabel lblMemberId;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameValue;
    private com.see.truetransact.uicomponent.CLabel lblMinCommissionAmount;
    private com.see.truetransact.uicomponent.CLabel lblModeDetails;
    private com.see.truetransact.uicomponent.CLabel lblModeDetailsValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrderSource;
    private com.see.truetransact.uicomponent.CLabel lblOrderSourceValue;
    private com.see.truetransact.uicomponent.CLabel lblOrderSubType;
    private com.see.truetransact.uicomponent.CLabel lblOrderType;
    private com.see.truetransact.uicomponent.CLabel lblPhoneOrder;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioAccount;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioLocation;
    private com.see.truetransact.uicomponent.CLabel lblPrice;
    private com.see.truetransact.uicomponent.CLabel lblProcessThruEdts;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumber;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumberValue;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipValue;
    private com.see.truetransact.uicomponent.CLabel lblSMIInfo;
    private com.see.truetransact.uicomponent.CLabel lblSettlementAccount;
    private com.see.truetransact.uicomponent.CLabel lblSettlementAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblSettlementDate;
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
    private com.see.truetransact.uicomponent.CLabel lblTillDate;
    private com.see.truetransact.uicomponent.CLabel lblTraderDealerInst;
    private com.see.truetransact.uicomponent.CLabel lblUnits;
    private com.see.truetransact.uicomponent.CMenuBar mbrPurchaseEnquities;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCommission;
    private com.see.truetransact.uicomponent.CPanel panEdtsEligible;
    private com.see.truetransact.uicomponent.CPanel panEnquities;
    private com.see.truetransact.uicomponent.CPanel panInstruction;
    private com.see.truetransact.uicomponent.CPanel panLabels;
    private com.see.truetransact.uicomponent.CPanel panLodgementFee;
    private com.see.truetransact.uicomponent.CPanel panMinCommissionAmount;
    private com.see.truetransact.uicomponent.CPanel panOrderDetails;
    private com.see.truetransact.uicomponent.CPanel panOrders;
    private com.see.truetransact.uicomponent.CPanel panPhoneOrder;
    private com.see.truetransact.uicomponent.CPanel panProcessThruEdts;
    private com.see.truetransact.uicomponent.CPanel panPurchaseEnquity;
    private com.see.truetransact.uicomponent.CPanel panSafeKeepingPortfolio;
    private com.see.truetransact.uicomponent.CPanel panSettlementAccount;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTxtMember;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEDTSEligible;
    private com.see.truetransact.uicomponent.CRadioButton rdoEDTSEligible_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoEDTSEligible_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPhoneOrder;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoProcessthruEDTS;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessthruEdts_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessthruEdts_Yes;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CSeparator sptHorizonta;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabEnquities;
    private javax.swing.JToolBar tbrPurchaseEnquities;
    private com.see.truetransact.uicomponent.CDateField tdtExecutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtSettlementDate;
    private com.see.truetransact.uicomponent.CDateField tdtTillDate;
    private com.see.truetransact.uicomponent.CTextField txtApproxAmount;
    private com.see.truetransact.uicomponent.CTextField txtBankOfficeInstruction;
    private com.see.truetransact.uicomponent.CTextField txtClientAdvices;
    private com.see.truetransact.uicomponent.CTextField txtCommRate;
    private com.see.truetransact.uicomponent.CTextField txtCommission;
    private com.see.truetransact.uicomponent.CTextField txtCreditNotes;
    private com.see.truetransact.uicomponent.CTextField txtDealerName;
    private com.see.truetransact.uicomponent.CTextField txtEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtExchange;
    private com.see.truetransact.uicomponent.CTextField txtLodgementFee;
    private com.see.truetransact.uicomponent.CTextField txtLotSize;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtMinCommAmount;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioAccount;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtPrice;
    private com.see.truetransact.uicomponent.CTextField txtSMIInfo;
    private com.see.truetransact.uicomponent.CTextField txtSettlementAccount;
    private com.see.truetransact.uicomponent.CTextField txtSettlementAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtTraderDealerInst;
    private com.see.truetransact.uicomponent.CTextField txtUnits;
    // End of variables declaration//GEN-END:variables
    
}
