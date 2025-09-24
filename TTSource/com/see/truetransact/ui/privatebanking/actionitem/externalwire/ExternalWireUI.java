/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireUI2.java
 *
 * Created on July 1, 2004, 4:33 PM
 */

package com.see.truetransact.ui.privatebanking.actionitem.externalwire;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.ui.privatebanking.actionitem.externalwire.ExternalWireRB;
import com.see.truetransact.ui.privatebanking.actionitem.externalwire.ExternalWireOB;
import com.see.truetransact.ui.privatebanking.actionitem.externalwire.ExternalWireMRB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

public class ExternalWireUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private ExternalWireRB resourceBundle = new ExternalWireRB();
    private HashMap mandatoryMap;
    private ExternalWireOB observable;
    private ExternalWireMRB objMandatoryRB;
    private String viewType = "";
    private String authorizeType = "";
    private final String AUTHORIZE = "Authorize";
    
    /** Creates new form ExternalWireUI2 */
    public ExternalWireUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaximumLengths();
        setObservable();
        setHelpMessage();
        observable.resetForm();
        clearLabels();
        initComponentData();
        ClientUtil.enableDisable(panExternalWire, false);
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
        cboBenBankCity.setName("cboBenBankCity");
        cboBenBankCountry.setName("cboBenBankCountry");
        cboBenBankState.setName("cboBenBankState");
        cboChargesCcy.setName("cboChargesCcy");
        cboChargesPaidBy.setName("cboChargesPaidBy");
        cboCorBankCity.setName("cboCorBankCity");
        cboCorBankCountry.setName("cboCorBankCountry");
        cboCorBankState.setName("cboCorBankState");
        cboCurrency.setName("cboCurrency");
        lblAdviseType.setName("lblAdviseType");
        lblAdviseTypeValue.setName("lblAdviseTypeValue");
        lblBankOfficeInstructions.setName("lblBankOfficeInstructions");
        lblBenBankPin.setName("lblBenBankPin");
        lblBenBankState.setName("lblBenBankState");
        lblBenCity.setName("lblBenCity");
        lblBeneficiaryCountry.setName("lblBeneficiaryCountry");
        lblBenificiaryAcNo.setName("lblBenificiaryAcNo");
        lblBenificiaryBank.setName("lblBenificiaryBank");
        lblBenificiaryName.setName("lblBenificiaryName");
        lblByOrderOf.setName("lblByOrderOf");
        lblChargesAmount.setName("lblChargesAmount");
        lblChargesCcy.setName("lblChargesCcy");
        lblChargesPaidBy.setName("lblChargesPaidBy");
        lblClientAdvices.setName("lblClientAdvices");
        lblClientContact.setName("lblClientContact");
        lblClientContactValue.setName("lblClientContactValue");
        lblContactDate.setName("lblContactDate");
        lblContactDateValue.setName("lblContactDateValue");
        lblContactMode.setName("lblContactMode");
        lblContactModeValue.setName("lblContactModeValue");
        lblCorBankCountry.setName("lblCorBankCountry");
        lblCorBankPin.setName("lblCorBankPin");
        lblCorBankState.setName("lblCorBankState");
        lblCorCity.setName("lblCorCity");
        lblCorrespondentBank.setName("lblCorrespondentBank");
        lblCreditAmount.setName("lblCreditAmount");
        lblCreditNotes.setName("lblCreditNotes");
        lblCurrency.setName("lblCurrency");
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
        lblMemberName.setName("lblMemberName");
        lblMemberNameValue.setName("lblMemberNameValue");
        lblModeDetails.setName("lblModeDetails");
        lblModeDetailsValue.setName("lblModeDetailsValue");
        lblMsg.setName("lblMsg");
        lblOrderSource.setName("lblOrderSource");
        lblOrderSourceValue.setName("lblOrderSourceValue");
        lblPaymentDetails.setName("lblPaymentDetails");
        lblReferenceNumber.setName("lblReferenceNumber");
        lblReferenceNumberValue.setName("lblReferenceNumberValue");
        lblRelationship.setName("lblRelationship");
        lblRelationshipValue.setName("lblRelationshipValue");
        lblRoutingCode.setName("lblRoutingCode");
        lblSettlementDate.setName("lblSettlementDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStandardCharges.setName("lblStandardCharges");
        lblStatus.setName("lblStatus");
        lblSwiftCode.setName("lblSwiftCode");
        lblTraderDealerInst.setName("lblTraderDealerInst");
        panCurrency.setName("panCurrency");
        panDebitAccount.setName("panDebitAccount");
        panExternalWire.setName("panExternalWire");
        panExternalWires.setName("panExternalWires");
        panInstructions.setName("panInstructions");
        panLabels.setName("panLabels");
        panOrder.setName("panOrder");
        panOrderDetails.setName("panOrderDetails");
        panStandardCharges.setName("panStandardCharges");
        panStatus.setName("panStatus");
        panTxtMember.setName("panTxtMember");
        rdoStandardCharges_No.setName("rdoStandardCharges_No");
        rdoStandardCharges_Yes.setName("rdoStandardCharges_Yes");
        sptHorizonta.setName("sptHorizonta");
        tabExternalWire.setName("tabExternalWire");
        tdtExecutionDate.setName("tdtExecutionDate");
        tdtSettlementDate.setName("tdtSettlementDate");
        txtBankOfficeInstruction.setName("txtBankOfficeInstruction");
        txtBenPin.setName("txtBenPin");
        txtBenificiartAcNo.setName("txtBenificiartAcNo");
        txtBenificiaryBank.setName("txtBenificiaryBank");
        txtBenificiaryName.setName("txtBenificiaryName");
        txtByOrderOf.setName("txtByOrderOf");
        txtChargesAmount.setName("txtChargesAmount");
        txtClientAdvices.setName("txtClientAdvices");
        txtCorPin.setName("txtCorPin");
        txtCorrespondentBank.setName("txtCorrespondentBank");
        txtCreditAmount.setName("txtCreditAmount");
        txtCreditNotes.setName("txtCreditNotes");
        txtDebitAccount.setName("txtDebitAccount");
        txtDebitAmount.setName("txtDebitAmount");
        txtDebitAssetSubClass.setName("txtDebitAssetSubClass");
        txtDebitEntitlementGroup.setName("txtDebitEntitlementGroup");
        txtDebitPortfolioLocation.setName("txtDebitPortfolioLocation");
        txtMember.setName("txtMember");
        txtPaymentDetails.setName("txtPaymentDetails");
        txtRoutingCode.setName("txtRoutingCode");
        txtSwiftCode.setName("txtSwiftCode");
        txtTraderDealerInst.setName("txtTraderDealerInst");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblExecutionDate.setText(resourceBundle.getString("lblExecutionDate"));
        ((javax.swing.border.TitledBorder)panOrder.getBorder()).setTitle(resourceBundle.getString("panOrder"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblContactMode.setText(resourceBundle.getString("lblContactMode"));
        lblLeadRSOValue.setText(resourceBundle.getString("lblLeadRSOValue"));
        lblAdviseTypeValue.setText(resourceBundle.getString("lblAdviseTypeValue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblOrderSourceValue.setText(resourceBundle.getString("lblOrderSourceValue"));
        lblClientContactValue.setText(resourceBundle.getString("lblClientContactValue"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        lblCorrespondentBank.setText(resourceBundle.getString("lblCorrespondentBank"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblBenCity.setText(resourceBundle.getString("lblBenCity"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblDebitEntitlementGroup.setText(resourceBundle.getString("lblDebitEntitlementGroup"));
        lblMemberId.setText(resourceBundle.getString("lblMemberId"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSettlementDate.setText(resourceBundle.getString("lblSettlementDate"));
        lblBenificiaryBank.setText(resourceBundle.getString("lblBenificiaryBank"));
        lblDebitAccount.setText(resourceBundle.getString("lblDebitAccount"));
        lblReferenceNumber.setText(resourceBundle.getString("lblReferenceNumber"));
        lblLeadRSO.setText(resourceBundle.getString("lblLeadRSO"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRelationshipValue.setText(resourceBundle.getString("lblRelationshipValue"));
        lblMemberNameValue.setText(resourceBundle.getString("lblMemberNameValue"));
        lblLeadBanker.setText(resourceBundle.getString("lblLeadBanker"));
        lblLeadBankerValue.setText(resourceBundle.getString("lblLeadBankerValue"));
        lblDebitPortfolioLocation.setText(resourceBundle.getString("lblDebitPortfolioLocation"));
        lblAdviseType.setText(resourceBundle.getString("lblAdviseType"));
        lblCorBankState.setText(resourceBundle.getString("lblCorBankState"));
        lblChargesPaidBy.setText(resourceBundle.getString("lblChargesPaidBy"));
        lblTraderDealerInst.setText(resourceBundle.getString("lblTraderDealerInst"));
        lblChargesCcy.setText(resourceBundle.getString("lblChargesCcy"));
        rdoStandardCharges_No.setText(resourceBundle.getString("rdoStandardCharges_No"));
        lblBenificiaryAcNo.setText(resourceBundle.getString("lblBenificiaryAcNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblModeDetailsValue.setText(resourceBundle.getString("lblModeDetailsValue"));
        lblBeneficiaryCountry.setText(resourceBundle.getString("lblBeneficiaryCountry"));
        lblBenBankPin.setText(resourceBundle.getString("lblBenBankPin"));
        ((javax.swing.border.TitledBorder)panCurrency.getBorder()).setTitle(resourceBundle.getString("panCurrency"));
        lblStandardCharges.setText(resourceBundle.getString("lblStandardCharges"));
        lblPaymentDetails.setText(resourceBundle.getString("lblPaymentDetails"));
        lblContactModeValue.setText(resourceBundle.getString("lblContactModeValue"));
        ((javax.swing.border.TitledBorder)panDebitAccount.getBorder()).setTitle(resourceBundle.getString("panDebitAccount"));
        lblContactDateValue.setText(resourceBundle.getString("lblContactDateValue"));
        lblCorBankCountry.setText(resourceBundle.getString("lblCorBankCountry"));
        lblBankOfficeInstructions.setText(resourceBundle.getString("lblBankOfficeInstructions"));
        lblCurrency.setText(resourceBundle.getString("lblCurrency"));
        lblRoutingCode.setText(resourceBundle.getString("lblRoutingCode"));
        lblByOrderOf.setText(resourceBundle.getString("lblByOrderOf"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblClientContact.setText(resourceBundle.getString("lblClientContact"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblOrderSource.setText(resourceBundle.getString("lblOrderSource"));
        lblDebitAmount.setText(resourceBundle.getString("lblDebitAmount"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnMember.setText(resourceBundle.getString("btnMember"));
        rdoStandardCharges_Yes.setText(resourceBundle.getString("rdoStandardCharges_Yes"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCorCity.setText(resourceBundle.getString("lblCorCity"));
        lblBenBankState.setText(resourceBundle.getString("lblBenBankState"));
        lblCreditNotes.setText(resourceBundle.getString("lblCreditNotes"));
        lblChargesAmount.setText(resourceBundle.getString("lblChargesAmount"));
        lblDebitAssetSubClass.setText(resourceBundle.getString("lblDebitAssetSubClass"));
        lblModeDetails.setText(resourceBundle.getString("lblModeDetails"));
        lblBenificiaryName.setText(resourceBundle.getString("lblBenificiaryName"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSwiftCode.setText(resourceBundle.getString("lblSwiftCode"));
        lblCreditAmount.setText(resourceBundle.getString("lblCreditAmount"));
        lblReferenceNumberValue.setText(resourceBundle.getString("lblReferenceNumberValue"));
        lblCorBankPin.setText(resourceBundle.getString("lblCorBankPin"));
        lblClientAdvices.setText(resourceBundle.getString("lblClientAdvices"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtDebitEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtDebitPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtDebitAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtDebitAccount", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("tdtSettlementDate", new Boolean(true));
        mandatoryMap.put("txtDebitAmount", new Boolean(true));
        mandatoryMap.put("txtCreditAmount", new Boolean(true));
        mandatoryMap.put("cboChargesPaidBy", new Boolean(true));
        mandatoryMap.put("rdoStandardCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtChargesAmount", new Boolean(true));
        mandatoryMap.put("cboChargesCcy", new Boolean(true));
        mandatoryMap.put("txtByOrderOf", new Boolean(true));
        mandatoryMap.put("txtSwiftCode", new Boolean(true));
        mandatoryMap.put("txtRoutingCode", new Boolean(true));
        mandatoryMap.put("txtBenificiaryName", new Boolean(true));
        mandatoryMap.put("txtBenificiartAcNo", new Boolean(true));
        mandatoryMap.put("txtBenificiaryBank", new Boolean(true));
        mandatoryMap.put("txtCorrespondentBank", new Boolean(true));
        mandatoryMap.put("txtBenPin", new Boolean(true));
        mandatoryMap.put("txtPaymentDetails", new Boolean(true));
        mandatoryMap.put("txtCorPin", new Boolean(true));
        mandatoryMap.put("cboBenBankCountry", new Boolean(true));
        mandatoryMap.put("cboBenBankCity", new Boolean(true));
        mandatoryMap.put("cboCorBankCity", new Boolean(true));
        mandatoryMap.put("cboBenBankState", new Boolean(true));
        mandatoryMap.put("cboCorBankState", new Boolean(true));
        mandatoryMap.put("cboCorBankCountry", new Boolean(true));
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
        objMandatoryRB = new ExternalWireMRB();
        txtMember.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMember"));
        txtDebitEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitEntitlementGroup"));
        txtDebitPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitPortfolioLocation"));
        txtDebitAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAssetSubClass"));
        txtDebitAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAccount"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        tdtExecutionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecutionDate"));
        tdtSettlementDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSettlementDate"));
        txtDebitAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAmount"));
        txtCreditAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditAmount"));
        cboChargesPaidBy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargesPaidBy"));
        rdoStandardCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStandardCharges_Yes"));
        txtChargesAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargesAmount"));
        cboChargesCcy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargesCcy"));
        txtByOrderOf.setHelpMessage(lblMsg, objMandatoryRB.getString("txtByOrderOf"));
        txtSwiftCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSwiftCode"));
        txtRoutingCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRoutingCode"));
        txtBenificiaryName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBenificiaryName"));
        txtBenificiartAcNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBenificiartAcNo"));
        txtBenificiaryBank.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBenificiaryBank"));
        txtCorrespondentBank.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCorrespondentBank"));
        txtBenPin.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBenPin"));
        txtPaymentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaymentDetails"));
        txtCorPin.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCorPin"));
        cboBenBankCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBenBankCountry"));
        cboBenBankCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBenBankCity"));
        cboCorBankCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCorBankCity"));
        cboBenBankState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBenBankState"));
        cboCorBankState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCorBankState"));
        cboCorBankCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCorBankCountry"));
        txtBankOfficeInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankOfficeInstruction"));
        txtTraderDealerInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTraderDealerInst"));
        txtCreditNotes.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNotes"));
        txtClientAdvices.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClientAdvices"));
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtMember(txtMember.getText());
        observable.setTxtDebitEntitlementGroup(txtDebitEntitlementGroup.getText());
        observable.setTxtDebitPortfolioLocation(txtDebitPortfolioLocation.getText());
        observable.setTxtDebitAssetSubClass(txtDebitAssetSubClass.getText());
        observable.setTxtDebitAccount(txtDebitAccount.getText());
        observable.setCboCurrency(CommonUtil.convertObjToStr(cboCurrency.getSelectedItem()));
        observable.setTdtExecutionDate(tdtExecutionDate.getDateValue());
        observable.setTdtSettlementDate(tdtSettlementDate.getDateValue());
        observable.setTxtDebitAmount(txtDebitAmount.getText());
        observable.setTxtCreditAmount(txtCreditAmount.getText());
        observable.setCboChargesPaidBy(CommonUtil.convertObjToStr(cboChargesPaidBy.getSelectedItem()));
        observable.setRdoStandardCharges_Yes(rdoStandardCharges_Yes.isSelected());
        observable.setRdoStandardCharges_No(rdoStandardCharges_No.isSelected());
        observable.setTxtChargesAmount(txtChargesAmount.getText());
        observable.setCboChargesCcy(CommonUtil.convertObjToStr(cboChargesCcy.getSelectedItem()));
        observable.setTxtByOrderOf(txtByOrderOf.getText());
        observable.setTxtSwiftCode(txtSwiftCode.getText());
        observable.setTxtRoutingCode(txtRoutingCode.getText());
        observable.setTxtBenificiaryName(txtBenificiaryName.getText());
        observable.setTxtBenificiaryAcNo(txtBenificiartAcNo.getText());
        observable.setTxtBenificiaryBank(txtBenificiaryBank.getText());
        observable.setTxtCorrespondentBank(txtCorrespondentBank.getText());
        observable.setTxtBenPin(txtBenPin.getText());
        observable.setTxtPaymentDetails(txtPaymentDetails.getText());
        observable.setTxtCorPin(txtCorPin.getText());
        observable.setCboBenBankCountry(CommonUtil.convertObjToStr(cboBenBankCountry.getSelectedItem()));
        observable.setCboBenBankCity(CommonUtil.convertObjToStr(cboBenBankCity.getSelectedItem()));
        observable.setCboCorBankCity(CommonUtil.convertObjToStr(cboCorBankCity.getSelectedItem()));
        observable.setCboBenBankState(CommonUtil.convertObjToStr(cboBenBankState.getSelectedItem()));
        observable.setCboCorBankState(CommonUtil.convertObjToStr(cboCorBankState.getSelectedItem()));
        observable.setCboCorBankCountry(CommonUtil.convertObjToStr(cboCorBankCountry.getSelectedItem()));
        observable.setTxtBankOfficeInstruction(txtBankOfficeInstruction.getText());
        observable.setTxtTraderDealerInst(txtTraderDealerInst.getText());
        observable.setTxtCreditNotes(txtCreditNotes.getText());
        observable.setTxtClientAdvices(txtClientAdvices.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /** Setting the observable for ExteranlWireUI */
    private void setObservable(){
        try{
            observable = ExternalWireOB.getInstance();
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
        txtDebitEntitlementGroup.setText(observable.getTxtDebitEntitlementGroup());
        txtDebitPortfolioLocation.setText(observable.getTxtDebitPortfolioLocation());
        txtDebitAssetSubClass.setText(observable.getTxtDebitAssetSubClass());
        txtDebitAccount.setText(observable.getTxtDebitAccount());
        cboCurrency.setSelectedItem(observable.getCboCurrency());
        tdtExecutionDate.setDateValue(observable.getTdtExecutionDate());
        tdtSettlementDate.setDateValue(observable.getTdtSettlementDate());
        txtDebitAmount.setText(observable.getTxtDebitAmount());
        txtCreditAmount.setText(observable.getTxtCreditAmount());
        cboChargesPaidBy.setSelectedItem(observable.getCboChargesPaidBy());
        rdoStandardCharges_Yes.setSelected(observable.getRdoStandardCharges_Yes());
        rdoStandardCharges_No.setSelected(observable.getRdoStandardCharges_No());
        txtChargesAmount.setText(observable.getTxtChargesAmount());
        cboChargesCcy.setSelectedItem(observable.getCboChargesCcy());
        txtByOrderOf.setText(observable.getTxtByOrderOf());
        txtSwiftCode.setText(observable.getTxtSwiftCode());
        txtRoutingCode.setText(observable.getTxtRoutingCode());
        txtBenificiaryName.setText(observable.getTxtBenificiaryName());
        txtBenificiartAcNo.setText(observable.getTxtBenificiaryAcNo());
        txtBenificiaryBank.setText(observable.getTxtBenificiaryBank());
        txtCorrespondentBank.setText(observable.getTxtCorrespondentBank());
        txtBenPin.setText(observable.getTxtBenPin());
        txtPaymentDetails.setText(observable.getTxtPaymentDetails());
        txtCorPin.setText(observable.getTxtCorPin());
        cboBenBankCountry.setSelectedItem(observable.getCboBenBankCountry());
        cboBenBankCity.setSelectedItem(observable.getCboBenBankCity());
        cboCorBankCity.setSelectedItem(observable.getCboCorBankCity());
        cboBenBankState.setSelectedItem(observable.getCboBenBankState());
        cboCorBankState.setSelectedItem(observable.getCboCorBankState());
        cboCorBankCountry.setSelectedItem(observable.getCboCorBankCountry());
        txtBankOfficeInstruction.setText(observable.getTxtBankOfficeInstruction());
        txtTraderDealerInst.setText(observable.getTxtTraderDealerInst());
        txtCreditNotes.setText(observable.getTxtCreditNotes());
        txtClientAdvices.setText(observable.getTxtClientAdvices());
    }
    
    /** This method sets the Maximum allowed lenght to the TextFields in the UI */
    private void setMaximumLengths(){
        txtMember.setMaxLength(16);
        txtDebitEntitlementGroup.setMaxLength(32);
        txtDebitPortfolioLocation.setMaxLength(32);
        txtDebitAssetSubClass.setMaxLength(32);
        txtDebitAccount.setMaxLength(32);
        txtDebitAmount.setMaxLength(16);
        txtCreditAmount.setMaxLength(16);
        txtChargesAmount.setMaxLength(16);
        txtByOrderOf.setMaxLength(64);
        txtRoutingCode.setMaxLength(32);
        txtSwiftCode.setMaxLength(32);
        txtBenificiaryName.setMaxLength(64);
        txtBenificiaryBank.setMaxLength(64);
        txtBenPin.setMaxLength(64);
        txtCorrespondentBank.setMaxLength(64);
        txtCorPin.setMaxLength(64);
        txtBankOfficeInstruction.setMaxLength(1024);
        txtTraderDealerInst.setMaxLength(1024);
        txtCreditNotes.setMaxLength(1024);
        txtClientAdvices.setMaxLength(1024);
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
    
    /* Initialising the Data in the combobox in the ui    */
    private void initComponentData() {
        try{
            cboCurrency.setModel(observable.getCbmCurrency());
            cboChargesCcy.setModel(observable.getCbmChargesCcy());
            cboChargesPaidBy.setModel(observable.getCbmChargesPaidBy());
            cboBenBankCountry.setModel(observable.getCbmBenBankCountry());
            cboBenBankState.setModel(observable.getCbmBenBankState());
            cboBenBankCity.setModel(observable.getCbmBenBankCity());
            cboCorBankCountry.setModel(observable.getCbmCorBankCountry());
            cboCorBankState.setModel(observable.getCbmCorBankState());
            cboCorBankCity.setModel(observable.getCbmCorBankCity());
        }catch(ClassCastException e){
            e.printStackTrace();
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
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtAIExternalWire");
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
                        ClientUtil.enableDisable(panExternalWire, false);
                    } else {
                        ClientUtil.enableDisable(panExternalWire, true);
                    }
                    setButtonEnableDisable();
                }else if(viewType.equals("Member")){
                    txtMember.setText( CommonUtil.convertObjToStr(hash.get(member)));
                }
                String where = txtMember.getText();
                HashMap lblMap = observable.getLabelMap(where);
                lblMemberNameValue.setText(CommonUtil.convertObjToStr(lblMap.get("MEMBER")));
                lblRelationshipValue.setText(CommonUtil.convertObjToStr(lblMap.get("MEMBER RELATION")));
                lblContactModeValue.setText(CommonUtil.convertObjToStr(lblMap.get("CONTACT_MODE")));
                lblContactDateValue.setText(DateUtil.getStringDate((Date)lblMap.get("CONTACT_DT")));
                lblClientContactValue.setText(CommonUtil.convertObjToStr(lblMap.get("CLIENT_CONTACT")));
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
        
        final String mandatoryMessage = checkMandatory(panExternalWire);
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
        ClientUtil.enableDisable(panExternalWire, false);
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
            mapParam.put(CommonConstants.MAP_NAME, "getExternalWireAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeExternalWire");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            mapParam = null;
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (authorizeType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("REF_ID", lblReferenceNumberValue.getText());
            
            ClientUtil.execute("authorizeExternalWire", singleAuthorizeMap);
            singleAuthorizeMap = null;
            authorizeType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        ExternalWireUI ext = new ExternalWireUI();
        frame.getContentPane().add(ext);
        frame.setSize(600,650);
        frame.setVisible(true);
        frame.show();
        ext.show();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStandardCharges = new com.see.truetransact.uicomponent.CButtonGroup();
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
        panExternalWire = new com.see.truetransact.uicomponent.CPanel();
        tabExternalWire = new com.see.truetransact.uicomponent.CTabbedPane();
        panExternalWires = new com.see.truetransact.uicomponent.CPanel();
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
        panDebitAccount = new com.see.truetransact.uicomponent.CPanel();
        lblDebitEntitlementGroup = new com.see.truetransact.uicomponent.CLabel();
        txtDebitEntitlementGroup = new com.see.truetransact.uicomponent.CTextField();
        lblDebitPortfolioLocation = new com.see.truetransact.uicomponent.CLabel();
        txtDebitPortfolioLocation = new com.see.truetransact.uicomponent.CTextField();
        lblDebitAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        lblDebitAccount = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAccount = new com.see.truetransact.uicomponent.CTextField();
        panCurrency = new com.see.truetransact.uicomponent.CPanel();
        lblCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        panOrderDetails = new com.see.truetransact.uicomponent.CPanel();
        panOrder = new com.see.truetransact.uicomponent.CPanel();
        lblExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtExecutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblSettlementDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSettlementDate = new com.see.truetransact.uicomponent.CDateField();
        lblDebitAmount = new com.see.truetransact.uicomponent.CLabel();
        txtDebitAmount = new com.see.truetransact.uicomponent.CTextField();
        lblCreditAmount = new com.see.truetransact.uicomponent.CLabel();
        txtCreditAmount = new com.see.truetransact.uicomponent.CTextField();
        lblChargesPaidBy = new com.see.truetransact.uicomponent.CLabel();
        cboChargesPaidBy = new com.see.truetransact.uicomponent.CComboBox();
        lblStandardCharges = new com.see.truetransact.uicomponent.CLabel();
        panStandardCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoStandardCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStandardCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblChargesAmount = new com.see.truetransact.uicomponent.CLabel();
        txtChargesAmount = new com.see.truetransact.uicomponent.CTextField();
        lblChargesCcy = new com.see.truetransact.uicomponent.CLabel();
        cboChargesCcy = new com.see.truetransact.uicomponent.CComboBox();
        lblByOrderOf = new com.see.truetransact.uicomponent.CLabel();
        txtByOrderOf = new com.see.truetransact.uicomponent.CTextField();
        lblSwiftCode = new com.see.truetransact.uicomponent.CLabel();
        txtSwiftCode = new com.see.truetransact.uicomponent.CTextField();
        lblRoutingCode = new com.see.truetransact.uicomponent.CLabel();
        txtRoutingCode = new com.see.truetransact.uicomponent.CTextField();
        lblBenificiaryName = new com.see.truetransact.uicomponent.CLabel();
        txtBenificiaryName = new com.see.truetransact.uicomponent.CTextField();
        lblBenificiaryAcNo = new com.see.truetransact.uicomponent.CLabel();
        txtBenificiartAcNo = new com.see.truetransact.uicomponent.CTextField();
        lblBenificiaryBank = new com.see.truetransact.uicomponent.CLabel();
        txtBenificiaryBank = new com.see.truetransact.uicomponent.CTextField();
        lblBeneficiaryCountry = new com.see.truetransact.uicomponent.CLabel();
        lblCorrespondentBank = new com.see.truetransact.uicomponent.CLabel();
        txtCorrespondentBank = new com.see.truetransact.uicomponent.CTextField();
        lblBenCity = new com.see.truetransact.uicomponent.CLabel();
        lblCorCity = new com.see.truetransact.uicomponent.CLabel();
        lblBenBankState = new com.see.truetransact.uicomponent.CLabel();
        lblCorBankState = new com.see.truetransact.uicomponent.CLabel();
        lblBenBankPin = new com.see.truetransact.uicomponent.CLabel();
        txtBenPin = new com.see.truetransact.uicomponent.CTextField();
        lblCorBankCountry = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentDetails = new com.see.truetransact.uicomponent.CLabel();
        txtPaymentDetails = new com.see.truetransact.uicomponent.CTextField();
        lblCorBankPin = new com.see.truetransact.uicomponent.CLabel();
        txtCorPin = new com.see.truetransact.uicomponent.CTextField();
        cboBenBankCountry = new com.see.truetransact.uicomponent.CComboBox();
        cboBenBankCity = new com.see.truetransact.uicomponent.CComboBox();
        cboCorBankCity = new com.see.truetransact.uicomponent.CComboBox();
        cboBenBankState = new com.see.truetransact.uicomponent.CComboBox();
        cboCorBankState = new com.see.truetransact.uicomponent.CComboBox();
        cboCorBankCountry = new com.see.truetransact.uicomponent.CComboBox();
        sptHorizonta = new com.see.truetransact.uicomponent.CSeparator();
        panInstructions = new com.see.truetransact.uicomponent.CPanel();
        lblBankOfficeInstructions = new com.see.truetransact.uicomponent.CLabel();
        txtBankOfficeInstruction = new com.see.truetransact.uicomponent.CTextField();
        lblTraderDealerInst = new com.see.truetransact.uicomponent.CLabel();
        txtTraderDealerInst = new com.see.truetransact.uicomponent.CTextField();
        lblCreditNotes = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNotes = new com.see.truetransact.uicomponent.CTextField();
        lblClientAdvices = new com.see.truetransact.uicomponent.CLabel();
        txtClientAdvices = new com.see.truetransact.uicomponent.CTextField();
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
        setMinimumSize(new java.awt.Dimension(550, 600));
        setPreferredSize(new java.awt.Dimension(550, 600));

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
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
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

        panExternalWire.setLayout(new java.awt.GridBagLayout());

        panExternalWires.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panExternalWires.add(panLabels, gridBagConstraints);

        panDebitAccount.setBorder(javax.swing.BorderFactory.createTitledBorder("Debit Account"));
        panDebitAccount.setLayout(new java.awt.GridBagLayout());

        lblDebitEntitlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panExternalWires.add(panDebitAccount, gridBagConstraints);

        panCurrency.setBorder(javax.swing.BorderFactory.createTitledBorder("Currency for Wire Transfer"));
        panCurrency.setLayout(new java.awt.GridBagLayout());

        lblCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCurrency.add(lblCurrency, gridBagConstraints);

        cboCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 162);
        panCurrency.add(cboCurrency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        panExternalWires.add(panCurrency, gridBagConstraints);

        tabExternalWire.addTab("External Wire", panExternalWires);

        panOrderDetails.setLayout(new java.awt.GridBagLayout());

        panOrder.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Details"));
        panOrder.setMinimumSize(new java.awt.Dimension(494, 377));
        panOrder.setPreferredSize(new java.awt.Dimension(494, 377));
        panOrder.setLayout(new java.awt.GridBagLayout());

        lblExecutionDate.setText("Execution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panOrder.add(lblExecutionDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(tdtExecutionDate, gridBagConstraints);

        lblSettlementDate.setText("Settlement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblSettlementDate, gridBagConstraints);

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
        panOrder.add(tdtSettlementDate, gridBagConstraints);

        lblDebitAmount.setText("Debit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblDebitAmount, gridBagConstraints);

        txtDebitAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitAmount.setValidation(new CurrencyValidation());
        txtDebitAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDebitAmountFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtDebitAmount, gridBagConstraints);

        lblCreditAmount.setText("Credit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panOrder.add(lblCreditAmount, gridBagConstraints);

        txtCreditAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCreditAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtCreditAmount, gridBagConstraints);

        lblChargesPaidBy.setText("Charges Paid By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblChargesPaidBy, gridBagConstraints);

        cboChargesPaidBy.setMaximumSize(new java.awt.Dimension(100, 21));
        cboChargesPaidBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(cboChargesPaidBy, gridBagConstraints);

        lblStandardCharges.setText("Standard Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblStandardCharges, gridBagConstraints);

        panStandardCharges.setLayout(new java.awt.GridBagLayout());

        rdoStandardCharges.add(rdoStandardCharges_Yes);
        rdoStandardCharges_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStandardCharges.add(rdoStandardCharges_Yes, gridBagConstraints);

        rdoStandardCharges.add(rdoStandardCharges_No);
        rdoStandardCharges_No.setText("No");
        panStandardCharges.add(rdoStandardCharges_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(panStandardCharges, gridBagConstraints);

        lblChargesAmount.setText("Charges Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblChargesAmount, gridBagConstraints);

        txtChargesAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChargesAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargesAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtChargesAmount, gridBagConstraints);

        lblChargesCcy.setText("Charges Ccy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblChargesCcy, gridBagConstraints);

        cboChargesCcy.setMaximumSize(new java.awt.Dimension(100, 21));
        cboChargesCcy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(cboChargesCcy, gridBagConstraints);

        lblByOrderOf.setText("By Order Of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblByOrderOf, gridBagConstraints);

        txtByOrderOf.setMaximumSize(new java.awt.Dimension(100, 21));
        txtByOrderOf.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtByOrderOf, gridBagConstraints);

        lblSwiftCode.setText("Swift Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblSwiftCode, gridBagConstraints);

        txtSwiftCode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSwiftCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtSwiftCode, gridBagConstraints);

        lblRoutingCode.setText("Routing Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblRoutingCode, gridBagConstraints);

        txtRoutingCode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRoutingCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtRoutingCode, gridBagConstraints);

        lblBenificiaryName.setText("Benificiary Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenificiaryName, gridBagConstraints);

        txtBenificiaryName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBenificiaryName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtBenificiaryName, gridBagConstraints);

        lblBenificiaryAcNo.setText("Beneficiary A/c No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenificiaryAcNo, gridBagConstraints);

        txtBenificiartAcNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBenificiartAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtBenificiartAcNo, gridBagConstraints);

        lblBenificiaryBank.setText("Beneficiary Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenificiaryBank, gridBagConstraints);

        txtBenificiaryBank.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBenificiaryBank.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtBenificiaryBank, gridBagConstraints);

        lblBeneficiaryCountry.setText("Beneficiary Bank Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBeneficiaryCountry, gridBagConstraints);

        lblCorrespondentBank.setText("Correspondent Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblCorrespondentBank, gridBagConstraints);

        txtCorrespondentBank.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCorrespondentBank.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtCorrespondentBank, gridBagConstraints);

        lblBenCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenCity, gridBagConstraints);

        lblCorCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblCorCity, gridBagConstraints);

        lblBenBankState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenBankState, gridBagConstraints);

        lblCorBankState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblCorBankState, gridBagConstraints);

        lblBenBankPin.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblBenBankPin, gridBagConstraints);

        txtBenPin.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBenPin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBenPin.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtBenPin, gridBagConstraints);

        lblCorBankCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblCorBankCountry, gridBagConstraints);

        lblPaymentDetails.setText("Payment Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblPaymentDetails, gridBagConstraints);

        txtPaymentDetails.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPaymentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtPaymentDetails, gridBagConstraints);

        lblCorBankPin.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(lblCorBankPin, gridBagConstraints);

        txtCorPin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCorPin.setValidation(new PincodeValidation_IN()
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOrder.add(txtCorPin, gridBagConstraints);

        cboBenBankCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panOrder.add(cboBenBankCountry, gridBagConstraints);

        cboBenBankCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        panOrder.add(cboBenBankCity, gridBagConstraints);

        cboCorBankCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        panOrder.add(cboCorBankCity, gridBagConstraints);

        cboBenBankState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        panOrder.add(cboBenBankState, gridBagConstraints);

        cboCorBankState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        panOrder.add(cboCorBankState, gridBagConstraints);

        cboCorBankCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        panOrder.add(cboCorBankCountry, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOrderDetails.add(panOrder, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panOrderDetails.add(sptHorizonta, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 8, 0);
        panOrderDetails.add(panInstructions, gridBagConstraints);

        tabExternalWire.addTab("Order Details", panOrderDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panExternalWire.add(tabExternalWire, gridBagConstraints);

        getContentPane().add(panExternalWire, java.awt.BorderLayout.CENTER);

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
    
    private void tdtSettlementDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSettlementDateFocusLost
        // TODO add your handling code here:
        String startDate = tdtExecutionDate.getDateValue();
        ClientUtil.validateToDate(tdtSettlementDate, startDate);
    }//GEN-LAST:event_tdtSettlementDateFocusLost
    
    private void txtDebitAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitAmountFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtDebitAmountFocusGained
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
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
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panExternalWire, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panExternalWire, true);
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
    private com.see.truetransact.uicomponent.CComboBox cboBenBankCity;
    private com.see.truetransact.uicomponent.CComboBox cboBenBankCountry;
    private com.see.truetransact.uicomponent.CComboBox cboBenBankState;
    private com.see.truetransact.uicomponent.CComboBox cboChargesCcy;
    private com.see.truetransact.uicomponent.CComboBox cboChargesPaidBy;
    private com.see.truetransact.uicomponent.CComboBox cboCorBankCity;
    private com.see.truetransact.uicomponent.CComboBox cboCorBankCountry;
    private com.see.truetransact.uicomponent.CComboBox cboCorBankState;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CLabel lblAdviseType;
    private com.see.truetransact.uicomponent.CLabel lblAdviseTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblBankOfficeInstructions;
    private com.see.truetransact.uicomponent.CLabel lblBenBankPin;
    private com.see.truetransact.uicomponent.CLabel lblBenBankState;
    private com.see.truetransact.uicomponent.CLabel lblBenCity;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryCountry;
    private com.see.truetransact.uicomponent.CLabel lblBenificiaryAcNo;
    private com.see.truetransact.uicomponent.CLabel lblBenificiaryBank;
    private com.see.truetransact.uicomponent.CLabel lblBenificiaryName;
    private com.see.truetransact.uicomponent.CLabel lblByOrderOf;
    private com.see.truetransact.uicomponent.CLabel lblChargesAmount;
    private com.see.truetransact.uicomponent.CLabel lblChargesCcy;
    private com.see.truetransact.uicomponent.CLabel lblChargesPaidBy;
    private com.see.truetransact.uicomponent.CLabel lblClientAdvices;
    private com.see.truetransact.uicomponent.CLabel lblClientContact;
    private com.see.truetransact.uicomponent.CLabel lblClientContactValue;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactDateValue;
    private com.see.truetransact.uicomponent.CLabel lblContactMode;
    private com.see.truetransact.uicomponent.CLabel lblContactModeValue;
    private com.see.truetransact.uicomponent.CLabel lblCorBankCountry;
    private com.see.truetransact.uicomponent.CLabel lblCorBankPin;
    private com.see.truetransact.uicomponent.CLabel lblCorBankState;
    private com.see.truetransact.uicomponent.CLabel lblCorCity;
    private com.see.truetransact.uicomponent.CLabel lblCorrespondentBank;
    private com.see.truetransact.uicomponent.CLabel lblCreditAmount;
    private com.see.truetransact.uicomponent.CLabel lblCreditNotes;
    private com.see.truetransact.uicomponent.CLabel lblCurrency;
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
    private com.see.truetransact.uicomponent.CLabel lblPaymentDetails;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumber;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNumberValue;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipValue;
    private com.see.truetransact.uicomponent.CLabel lblRoutingCode;
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
    private com.see.truetransact.uicomponent.CLabel lblStandardCharges;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSwiftCode;
    private com.see.truetransact.uicomponent.CLabel lblTraderDealerInst;
    private com.see.truetransact.uicomponent.CMenuBar mbrActionItem;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCurrency;
    private com.see.truetransact.uicomponent.CPanel panDebitAccount;
    private com.see.truetransact.uicomponent.CPanel panExternalWire;
    private com.see.truetransact.uicomponent.CPanel panExternalWires;
    private com.see.truetransact.uicomponent.CPanel panInstructions;
    private com.see.truetransact.uicomponent.CPanel panLabels;
    private com.see.truetransact.uicomponent.CPanel panOrder;
    private com.see.truetransact.uicomponent.CPanel panOrderDetails;
    private com.see.truetransact.uicomponent.CPanel panStandardCharges;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTxtMember;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStandardCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoStandardCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStandardCharges_Yes;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CSeparator sptHorizonta;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabExternalWire;
    private javax.swing.JToolBar tbrActionItem;
    private com.see.truetransact.uicomponent.CDateField tdtExecutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtSettlementDate;
    private com.see.truetransact.uicomponent.CTextField txtBankOfficeInstruction;
    private com.see.truetransact.uicomponent.CTextField txtBenPin;
    private com.see.truetransact.uicomponent.CTextField txtBenificiartAcNo;
    private com.see.truetransact.uicomponent.CTextField txtBenificiaryBank;
    private com.see.truetransact.uicomponent.CTextField txtBenificiaryName;
    private com.see.truetransact.uicomponent.CTextField txtByOrderOf;
    private com.see.truetransact.uicomponent.CTextField txtChargesAmount;
    private com.see.truetransact.uicomponent.CTextField txtClientAdvices;
    private com.see.truetransact.uicomponent.CTextField txtCorPin;
    private com.see.truetransact.uicomponent.CTextField txtCorrespondentBank;
    private com.see.truetransact.uicomponent.CTextField txtCreditAmount;
    private com.see.truetransact.uicomponent.CTextField txtCreditNotes;
    private com.see.truetransact.uicomponent.CTextField txtDebitAccount;
    private com.see.truetransact.uicomponent.CTextField txtDebitAmount;
    private com.see.truetransact.uicomponent.CTextField txtDebitAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtDebitEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtDebitPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtPaymentDetails;
    private com.see.truetransact.uicomponent.CTextField txtRoutingCode;
    private com.see.truetransact.uicomponent.CTextField txtSwiftCode;
    private com.see.truetransact.uicomponent.CTextField txtTraderDealerInst;
    // End of variables declaration//GEN-END:variables
    
}
