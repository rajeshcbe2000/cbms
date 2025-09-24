/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MultipleAccountLodgementUI.java
 *
 * Created on February 4, 2005, 11:32 AM
 */

package com.see.truetransact.ui.bills.lodgement.multipleaccountlodgement;

/**
 *
 * @author  ashokvijayakumar
 */

import com.see.truetransact.ui.bills.lodgement.*;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import java.util.ResourceBundle;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.transaction.clearing.outward.OutwardClearingUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;


public class MultipleAccountLodgementUI extends CInternalFrame implements UIMandatoryField,Observer{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.bills.lodgement.LodgementBillsRB", ProxyParameters.LANGUAGE);
    private MultipleAccountLodgementMRB objMandatoryRB = new MultipleAccountLodgementMRB();
    private HashMap mandatoryMap;
    private MultipleAccountLodgementOB observable;
    private TransDetailsUI transDetails = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = "";
    private int selectedData = 0; //Variable to indicate whether the data is selected in the table or not
   private int selectedDataInst = 0; //Variable to indicate whether the data is selected in the table or not
    //if 0 not selected if 1 selected
   private Date currDt = null;
    private final String AUTHORIZE = "Authorize";
    private final String QBM = "Query Branch Master";
    private final String QOB = "Query Other Bank";
    private final String QOBB = "Query Other Bank Branch";
    private final String AN = "Account Number";
    private final String CPDAN = "CPD Account Number";
    private final String LODGEMENT_ID = "LODGEMENT_ID";
    private final String QDB = "Query Drawee Bank";
    private final String QDBB = "Query Drawee Bank Branch";
    private int result;
    private int resultInstr;
    private int selectedRow = -1;//Variable to identify whether a row is selected or not.
    private int selectedRowInst = -1;//Variable to identify whether a row is selected or not
    private String LodgeID = "";
    private String productId = "";
    private String bankCode = "";
    private boolean doSave = true;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    int btnNew1=0;
    int selRow=0;
    private int rejectFlag=0;
    String strChkClear="N";
    private ServiceTaxCalculation objServiceTax;
    String chargeStatus ; 
    /** Creates new form LodgementBillsUI */
    public MultipleAccountLodgementUI() {
        initForm();
        transDetails = new TransDetailsUI(panLableValues);
        txtNextAccNo.setEnabled(false);
    }
    
    private void initForm(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaxLengths();
        setObservable();
        observable.removeTbmInstructionsRow();
        observable.removeTbmInstrRow();
        observable.resetForm();
        initComponentData();
        //initTableData();
        setHelpMessage();
        ClientUtil.enableDisable(panLodgementBIlls,false);
        setPanOperationsEnable(false);
        setButtonEnableDisable();
        lblDraweeBankName.setVisible(false);
        lblDraweeBranchName.setVisible(false);
        lblPayeeName.setVisible(false);
        txtDraweeBankName.setVisible(false);
        txtDraweeBranchName.setVisible(false);
        txtPayeeName.setVisible(false);
        txtTotalAmt.setEnabled(false);
        txtTotalAmt.setEditable(false);
        txtTotalServTax.setEditable(false);
        txtTotalServTax.setEnabled(false);
        tabLodgementBils.resetVisits();
        panRemitList.setVisible(false);
        txtAccountNo1.setEnabled(false);
        cboTranstype.setVisible(false);
        lblTranstype.setVisible(false);
        panOverdueRateBills8.setVisible(false);
        lblRateForDelay.setVisible(false);
        cboProductID.setVisible(false);
        lblProductID.setVisible(false);
        panAccountNo1.setVisible(false);
        lblAccountNum.setVisible(false);
        lblCurStatus1.setVisible(false);
        lblCurStatus.setVisible(false);
        lblLodgementId.setVisible(false);
        txtLodgementId.setVisible(false);
        btnView.setEnabled(true);
        ClientUtil.enableDisable(panRRLRARPPDetails1,false);
        btnDelete.setVisible(false);
        txtAccountHeadValue.setEnabled(false);
        lblSendingTo.setVisible(false);
        txtSendingTo.setVisible(false);
        panOtherDetails.setVisible(false);
        panBranchDetails.setVisible(false);
        panOtherBankDetails.setVisible(true);    
        tabLodgementBils.remove(panInstrumentDetails);
        tabLodgementBils.remove(panDocumentDetails);
        txtLodgementId.setEnabled(false);
        tdtRemittedDt.setDateValue(DateUtil.getStringDate(currDt));
        //new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLodgementDetails); commented by nithya
//        observable.setCombo();
//        ComboBoxModel objDepModel = new ComboBoxModel();
//        cboStdInstruction.setModel(objDepModel);
//        observable.setCbmStdInstruction(objDepModel);
        chkClering.setVisible(false);
         lblServiceTaxval.setEditable(false);
        lblServiceTaxval.setEnabled(false);        
        enableDisableOtherBankChrgFields(false);        
        enableAccountMappingFields(false);
        txtInstAmt.setText("");
    }
    
    
    
    private void enableDisableOtherBankChrgFields(boolean yesNo){
        txtRemitFavour.setEnabled(yesNo);
        txtInstAmt.setEnabled(yesNo);
    }
    
   
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        panLableValues.setName("panLableValues");
        btnAccountNo1.setName("btnAccountNo");
        btnAuthorize.setName("btnAuthorize");
        btnBranchCode.setName("btnBranchCode");
        btnBankCode.setName("btnBankCode");
        btnOtherBranchCode.setName("btnOtherBranchCode");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnInstDelete.setName("btnInstDelete");
        btnInstNew.setName("btnInstNew");
        btnInstSave.setName("btnInstSave");
        btnView.setName("btnView");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboBillTenor.setName("cboBillTenor");
        cboBillsType.setName("cboBillsType");
        cboProductID.setName("cboProductID");
        txtAccountNum.setName("txtAccountNum");
        cboActivities.setName("cboActivities");
        cboCustCategory.setName("cboCustCategory");
        cboTranstype.setName("cboTranstype");
        cboDraweeCity.setName("cboDraweeCity");
        cboDraweeCountry.setName("cboDraweeCountry");
        cboDraweeState.setName("cboDraweeState");
        cboInstrumentType.setName("cboInstrumentType");
        cboOtherCity.setName("cboOtherCity");
        cboOtherCountry.setName("cboOtherCountry");
        cboOtherState.setName("cboOtherState");
        cboProductId1.setName("cboProductId");
        cboProductType1.setName("cboProductType");
        cboReceivedFrom.setName("cboReceivedFrom");
        cboStdInstruction.setName("cboStdInstruction");
        cboInstruction.setName("cboInstruction");
        cboRemitProdID.setName("cboRemitProdID");
        cboRemitCity.setName("cboRemitCity");
        cboRemitDraweeBank.setName("cboRemitDraweeBank");
        cboRemitBranchCode.setName("cboRemitBranchCode");
        cboIntDays.setName("cboIntDays");
        cboTransitPeriod.setName("cboTransitPeriod");
        lbSpace2.setName("lbSpace2");
        lblAcceptanceDate.setName("lblAcceptanceDate");
        lblAccountNo1.setName("lblAccountNo");
        lblAdditionalInstruction.setName("lblAdditionalInstruction");
        lblBankCode.setName("lblBankCode");
        lblBankName.setName("lblBankName");
        lblBankNameValue.setName("lblBankNameValue");
        lblDrawBnk.setName("lblDrawBnk");
        lblDrawBran.setName("lblDrawBran");
        lblBillAcceptance.setName("lblBillAcceptance");
        lblBillTenor.setName("lblBillTenor");
        lblBillsType.setName("lblBillsType");
        lblActivities.setName("lblActivities");
        lblCustCategory.setName("lblCustCategory");
        lblCreatDt.setName("lblCreatDt");
        
        lblBranchCode.setName("lblBranchCode");
        lblBranchCodeValue.setName("lblBranchCodeValue");
        lblBranchName.setName("lblBranchName");
        lblCustName.setName("lblCustomerName");
        //lblCustomerNameValue.setName("lblCustomerNameValue");
        lblDraweeAddress.setName("lblDraweeAddress");
        lblDraweeBankCode.setName("lblDraweeBankCode");
        lblDraweeBankName.setName("lblDraweeBankName");
        lblDraweeBranchCode.setName("lblDraweeBranchCode");
        lblDraweeBranchName.setName("lblDraweeBranchName");
        lblDraweeCity.setName("lblDraweeCity");
        lblDraweeCountry.setName("lblDraweeCountry");
        lblDraweeHundi.setName("lblDraweeHundi");
        lblDraweeName.setName("lblDraweeName");
        lblDraweeNo.setName("lblDraweeNo");
        lblDraweeBankNamee.setName("lblDraweeBankNamee");
        lblAccountHead.setName("lblAccountHead");
        
        
        
        
        
        lblDraweePinCode.setName("lblDraweePinCode");
        lblDraweeState.setName("lblDraweeState");
        lblDueDate.setName("lblDueDate");
        lblGoodsAssigned.setName("lblGoodsAssigned");
        lblGoodsValue.setName("lblGoodsValue");
        lblHundiAmount.setName("lblHundiAmount");
        lblHundiDate.setName("lblHundiDate");
        lblHundiNo.setName("lblHundiNo");
        lblHundiRemarks.setName("lblHundiRemarks");
        lblInstrumentAmount.setName("lblInstrumentAmount");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentNo.setName("lblInstrumentNo");
        lblInstrumentType.setName("lblInstrumentType");
        lblInvoiceAmount.setName("lblInvoiceAmount");
        lblInvoiceDate.setName("lblInvoiceDate");
        lblInvoiceNumber.setName("lblInvoiceNumber");
        lblMICR.setName("lblMICR");
        lblMsg.setName("lblMsg");
        lblOperatesLike.setName("lblOperatesLike");
        lblOperatesLikeValue.setName("lblOperatesLikeValue");
        lblOtherAddress.setName("lblOtherAddress");
        lblOtherBranchCode.setName("lblOtherBranchCode");
        lblOtherBranchName.setName("lblOtherBranchName");
        lblIntICC.setName("lblIntICC");
        lblOtherBranchNameValue.setName("lblOtherBranchNameValue");
        lblOtherCity.setName("lblOtherCity");
        lblOtherCountry.setName("lblOtherCountry");
        lblOtherName.setName("lblOtherName");
        lblOtherPinCode.setName("lblOtherPinCode");
        lblPayable.setName("lblPayable");
        lblPayeeName.setName("lblPayeeName");
        lblProductId1.setName("lblProductId");
        lblProductType1.setName("lblProductType");
        lblRRLRDate.setName("lblRRLRDate");
        lblRRLRNumber.setName("lblRRLRNumber");
        lblReceivedFrom.setName("lblReceivedFrom");
        lblReference1.setName("lblReference");
        lblRemarks.setName("lblRemarks");
        lblSendingTo.setName("lblSendingTo");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStdInstruction.setName("lblStdInstruction");
        lblTransportCompany.setName("lblTransportCompany");
        mbrLodgemntBills.setName("mbrLodgemntBills");
        panAcceptanceBill.setName("panAcceptanceBill");
        panAccountNo1.setName("panAccountNo");
        panBankCode.setName("panBankCode");
        panBillTenor.setName("panBillTenor");
        panBillofExchange.setName("panBillofExchange");
        panBillsType.setName("panBillsType");
        panRemitList.setName("panRemitList");
        panBranchBankDetails.setName("panBranchBankDetails");
        panBranchCode.setName("panBranchCode");
        panBranchDetails.setName("panBranchDetails");
        panChequeDetails.setName("panChequeDetails");
        panDocumentDetails.setName("panDocumentDetails");
//        panDraweeDetails.setName("panDraweeDetails");
        panDraweeHundi.setName("panDraweeHundi");
        panInstEntry.setName("panInstEntry");
        panInstructions.setName("panInstructions");
        panInstrumentDetails.setName("panInstrumentDetails");
        panInstrumentType.setName("panInstrumentType");
        panInvoiceDetails.setName("panInvoiceDetails");
        panLodgementBIlls.setName("panLodgementBIlls");
        panLodgementDetails.setName("panLodgementDetails");
        panOperations.setName("panOperations");
        panOtherBankDetails.setName("panOtherBankDetails");
        panOtherBranchCode.setName("panOtherBranchCode");
        panOtherDetails.setName("panOtherDetails");
        panProductDetails1.setName("panProductDetails");
        panRRLRARPPDetails.setName("panRRLRARPPDetails");
        panStatus.setName("panStatus");
        panTblInstruction.setName("panTblInstruction");
        panTblInstruction1.setName("panTblInstruction1");
        panIntICC.setName("panIntICC");
        rdoBillAcceptance_No.setName("rdoBillAcceptance_No");
        rdoBillAcceptance_Yes.setName("rdoBillAcceptance_Yes");
        rdoDraweeHundi_No.setName("rdoDraweeHundi_No");
        rdoDraweeHundi_Yes.setName("rdoDraweeHundi_Yes");
        cRadio_ICC_Yes.setName("cRadio_ICC_Yes");
        cRadio_ICC_No.setName("cRadio_ICC_No");
        srpInstructions.setName("srpInstructions");
        tabLodgementBils.setName("tabLodgementBils");
        tblInstruction.setName("tblInstruction");
        tblInstruction1.setName("tblInstruction1");
        tblInstruction2.setName("tblInstruction2");
        tdtAcceptanceDate.setName("tdtAcceptanceDate");
        tdtDueDate.setName("tdtDueDate");
        tdtHundiDate.setName("tdtHundiDate");
        tdtInstrumentDate.setName("tdtInstrumentDate");
        tdtRemitInstDate.setName("tdtRemitInstDate");
        tdtInvoiceDate.setName("tdtInvoiceDate");
        tdtRRLRDate.setName("tdtRRLRDate");
        txtAccountNo1.setName("txtAccountNo");
        txtBankCode.setName("txtBankCode");
        txtBillTenor.setName("txtBillTenor");
        txtBranchCode.setName("txtBranchCode");
        txtDraweeAddress.setName("txtDraweeAddress");
        txtDraweeBankCode.setName("txtDraweeBankCode");
        txtDraweeBankName.setName("txtDraweeBankName");
        txtDraweeBranchCode.setName("txtDraweeBranchCode");
        txtDraweeBranchName.setName("txtDraweeBranchName");
        txtDraweeName.setName("txtDraweeName");
        txtDraweeNo.setName("txtDraweeNo");
        txtDraweeBankNameVal.setName("txtDraweeBankNameVal");
        txtAccountHeadValue.setName("txtAccountHeadValue");
        txtDraweePinCode.setName("txtDraweePinCode");
        txtGoodsAssigned.setName("txtGoodsAssigned");
        txtGoodsValue.setName("txtGoodsValue");
        txtHundiAmount.setName("txtHundiAmount");
        txtHundiNo.setName("txtHundiNo");
        txtHundiRemarks.setName("txtHundiRemarks");
        txtInstrumentAmount.setName("txtInstrumentAmount");
        txtInstrumentNo.setName("txtInstrumentNo");
        txtInstPrefix.setName("txtInstPrefix");
        txtInvoiceAmount.setName("txtInvoiceAmount");
        txtInvoiceNumber.setName("txtInvoiceNumber");
        txtMICR.setName("txtMICR");
        txtOtherAddress.setName("txtOtherAddress");
        txtOtherBranchCode.setName("txtOtherBranchCode");
        txtOtherName.setName("txtOtherName");
        txtOtherPinCode.setName("txtOtherPinCode");
        txtPayable.setName("txtPayable");
        txtPayeeName.setName("txtPayeeName");
        txtRRLRNumber.setName("txtRRLRNumber");
        txtReference1.setName("txtReference");
        txtRateForDelay.setName("txtRateForDelay");
        txaRemarks.setName("txaRemarks");
        txtSendingTo.setName("txtSendingTo");
        txtStdInstruction.setName("txtStdInstruction");
        txtAreaParticular.setName("txtAreaParticular");
        txtAmount.setName("txtAmount");
        txtServiceTax.setName("txtServiceTax");
        txtTotalServTax.setName("txtTotalServTax");
        txtTotalAmt.setName("txtTotalAmt");
        txtTransportCompany.setName("txtTransportCompany");
        lblLodgementId.setName("lblLodgementId");
        lblCurStatus1.setName("lblCurStatus1");
        lblCurStatus.setName("lblCurStatus");
        txtCreatDt.setName("txtCreatDt");
        lblCreatDt.setName("lblCreatDt");
        lblRemitProdID.setName("lblRemitProdID");
        lblRemitCity.setName("lblRemitCity");
        lblRemitDraweeBank.setName("lblRemitDraweeBank");
        lblRemitBranchCode.setName("lblRemitBranchCode");
        lblRemitFavour.setName("lblRemitFavour");
        lblRemitAmt.setName("lblRemitAmt");
        lblInstNo.setName("lblInstNo");
        txtLodgementId.setName("lblLodgementId");
        txtRemitFavour.setName("txtRemitFavour");
        txtRemitFavour1.setName("txtRemitFavour1");
        txtInstAmt.setName("txtInstAmt");
        txtInst1.setName("txtInst1");
        txtInst2.setName("txtInst2");
        txtRateForDelay1.setName("txtRateForDelay1");
        txtIntDays.setName("txtIntDays");
//        cboIntDays
        txtDiscountRateBills.setName("txtDiscountRateBills");
        txtOverdueRateBills.setName("txtOverdueRateBills");
        txtRateForCBP.setName("txtRateForCBP");
        txtAtParLimit.setName("txtAtParLimit");
        txtCleanBills.setName("txtCleanBills");
        txtTransitPeriod.setName("txtTransitPeriod");
//        cboTransitPeriod
        txtDefaultPostage.setName("txtDefaultPostage");
        lblRateForDelay1.setName("lblRateForDelay1");
        lblIntDays.setName("lblIntDays");
        lblDiscountRateOfBD.setName("lblDiscountRateOfBD");
        lblOverdueInterestForBD.setName("lblOverdueInterestForBD");
        lblOverdueRateCBP.setName("lblOverdueRateCBP");
        lblAtParLimit.setName("lblAtParLimit");
        lblCleanBillsPurchased.setName("lblCleanBillsPurchased");
        lblTransitPeriod.setName("lblTransitPeriod");
        lblDefaultPostage.setName("lblDefaultPostage");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ((javax.swing.border.TitledBorder)panLableValues.getBorder()).setTitle(resourceBundle.getString("panLableValues"));
        lblBankName.setText(resourceBundle.getString("lblBankName"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblOperatesLike.setText(resourceBundle.getString("lblOperatesLike"));
        btnInstNew.setText(resourceBundle.getString("btnInstNew"));
        lblDraweeHundi.setText(resourceBundle.getString("lblDraweeHundi"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        ((javax.swing.border.TitledBorder)panRRLRARPPDetails.getBorder()).setTitle(resourceBundle.getString("panRRLRARPPDetails"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblHundiRemarks.setText(resourceBundle.getString("lblHundiRemarks"));
        lblOtherCountry.setText(resourceBundle.getString("lblOtherCountry"));
        lblBillTenor.setText(resourceBundle.getString("lblBillTenor"));
        lblDraweePinCode.setText(resourceBundle.getString("lblDraweePinCode"));
        rdoDraweeHundi_Yes.setText(resourceBundle.getString("rdoDraweeHundi_Yes"));
        lblDraweeBankCode.setText(resourceBundle.getString("lblDraweeBankCode"));
        lblOtherPinCode.setText(resourceBundle.getString("lblOtherPinCode"));
        ((javax.swing.border.TitledBorder)panOtherBankDetails.getBorder()).setTitle(resourceBundle.getString("panOtherBankDetails"));
        rdoBillAcceptance_Yes.setText(resourceBundle.getString("rdoBillAcceptance_Yes"));
        lblAcceptanceDate.setText(resourceBundle.getString("lblAcceptanceDate"));
        btnInstSave.setText(resourceBundle.getString("btnInstSave"));
        lblDraweeState.setText(resourceBundle.getString("lblDraweeState"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblOtherBranchNameValue.setText(resourceBundle.getString("lblOtherBranchNameValue"));
        lblOtherAddress.setText(resourceBundle.getString("lblOtherAddress"));
        lblOtherCity.setText(resourceBundle.getString("lblOtherCity"));
        lblDraweeBranchName.setText(resourceBundle.getString("lblDraweeBranchName"));
        rdoDraweeHundi_No.setText(resourceBundle.getString("rdoDraweeHundi_No"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblProductType1.setText(resourceBundle.getString("lblProductType"));
        lblDraweeName.setText(resourceBundle.getString("lblDraweeName"));
        lblInstrumentAmount.setText(resourceBundle.getString("lblInstrumentAmount"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblPayable.setText(resourceBundle.getString("lblPayable"));
        btnException.setText(resourceBundle.getString("btnException"));
        ((javax.swing.border.TitledBorder)panChequeDetails.getBorder()).setTitle(resourceBundle.getString("panChequeDetails"));
        lblReceivedFrom.setText(resourceBundle.getString("lblReceivedFrom"));
        lblBillsType.setText(resourceBundle.getString("lblBillsType"));
        lblActivities.setText(resourceBundle.getString("lblActivities"));
        lblCustCategory.setText(resourceBundle.getString("lblCustCategory"));
        lblCreatDt.setText(resourceBundle.getString("lblCreatDt"));
        
        
        
        lblOtherName.setText(resourceBundle.getString("lblOtherName"));
        lblProductId1.setText(resourceBundle.getString("lblProductId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblHundiNo.setText(resourceBundle.getString("lblHundiNo"));
        btnBranchCode.setText(resourceBundle.getString("btnBranchCode"));
        lblTransportCompany.setText(resourceBundle.getString("lblTransportCompany"));
        lblOtherBranchName.setText(resourceBundle.getString("lblOtherBranchName"));
        lblRRLRNumber.setText(resourceBundle.getString("lblRRLRNumber"));
        lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        lblDraweeBankName.setText(resourceBundle.getString("lblDraweeBankName"));
        lblCustName.setText(resourceBundle.getString("lblCustomerName"));
        lblAdditionalInstruction.setText(resourceBundle.getString("lblAdditionalInstruction"));
        ((javax.swing.border.TitledBorder)panOtherDetails.getBorder()).setTitle(resourceBundle.getString("panOtherDetails"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblRRLRDate.setText(resourceBundle.getString("lblRRLRDate"));
        lblBranchCodeValue.setText(resourceBundle.getString("lblBranchCodeValue"));
        btnInstDelete.setText(resourceBundle.getString("btnInstDelete"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        lblInvoiceNumber.setText(resourceBundle.getString("lblInvoiceNumber"));
        lblInvoiceDate.setText(resourceBundle.getString("lblInvoiceDate"));
        lblGoodsAssigned.setText(resourceBundle.getString("lblGoodsAssigned"));
        lblBillAcceptance.setText(resourceBundle.getString("lblBillAcceptance"));
        ((javax.swing.border.TitledBorder)panBranchDetails.getBorder()).setTitle(resourceBundle.getString("panBranchDetails"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblInvoiceAmount.setText(resourceBundle.getString("lblInvoiceAmount"));
        lblOtherBranchCode.setText(resourceBundle.getString("lblOtherBranchCode"));
//        ((javax.swing.border.TitledBorder)panDraweeDetails.getBorder()).setTitle(resourceBundle.getString("panDraweeDetails"));
        rdoBillAcceptance_No.setText(resourceBundle.getString("rdoBillAcceptance_No"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblReference1.setText(resourceBundle.getString("lblReference"));
        lblDueDate.setText(resourceBundle.getString("lblDueDate"));
        ((javax.swing.border.TitledBorder)panInvoiceDetails.getBorder()).setTitle(resourceBundle.getString("panInvoiceDetails"));
        lblStdInstruction.setText(resourceBundle.getString("lblStdInstruction"));
        lblAccountNo1.setText(resourceBundle.getString("lblAccountNo"));
        btnOtherBranchCode.setText(resourceBundle.getString("btnOtherBranchCode"));
        btnAccountNo1.setText(resourceBundle.getString("btnAccountNo"));
        lblBankNameValue.setText(resourceBundle.getString("lblBankNameValue"));
        lblDrawBnk.setText(resourceBundle.getString("lblDrawBnk"));
        lblDrawBran.setText(resourceBundle.getString("lblDrawBran"));
        //lblCustomerNameValue.setText(resourceBundle.getString("lblCustomerNameValue"));
        lblPayeeName.setText(resourceBundle.getString("lblPayeeName"));
        lblDraweeCountry.setText(resourceBundle.getString("lblDraweeCountry"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblDraweeAddress.setText(resourceBundle.getString("lblDraweeAddress"));
        lblGoodsValue.setText(resourceBundle.getString("lblGoodsValue"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblDraweeNo.setText(resourceBundle.getString("lblDraweeNo"));
        lblDraweeBankNamee.setText(resourceBundle.getString("lblDraweeBankNamee"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblHundiAmount.setText(resourceBundle.getString("lblHundiAmount"));
        lblDraweeCity.setText(resourceBundle.getString("lblDraweeCity"));
        lblSendingTo.setText(resourceBundle.getString("lblSendingTo"));
        lblMICR.setText(resourceBundle.getString("lblMICR"));
        lblOperatesLikeValue.setText(resourceBundle.getString("lblOperatesLikeValue"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        ((javax.swing.border.TitledBorder)panBillofExchange.getBorder()).setTitle(resourceBundle.getString("panBillofExchange"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnBankCode.setText(resourceBundle.getString("btnBankCode"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        lblHundiDate.setText(resourceBundle.getString("lblHundiDate"));
        lblDraweeBranchCode.setText(resourceBundle.getString("lblDraweeBranchCode"));
        lblLodgementId.setText(resourceBundle.getString("lblLodgementId"));
        lblCurStatus.setText(resourceBundle.getString("lblCurStatus"));
        lblCurStatus1.setText(resourceBundle.getString("lblCurStatus1"));
        txtCreatDt.setText(resourceBundle.getString("txtCreatDt"));
        lblCreatDt.setText(resourceBundle.getString("lblCreatDt"));
        lblRemitProdID.setText(resourceBundle.getString("lblRemitProdID"));
        lblRemitCity.setText(resourceBundle.getString("lblRemitCity"));
        lblRemitDraweeBank.setText(resourceBundle.getString("lblRemitDraweeBank"));
        lblRemitBranchCode.setText(resourceBundle.getString("lblRemitBranchCode"));
        lblRemitFavour.setText(resourceBundle.getString("lblRemitFavour"));
        lblRemitAmt.setText(resourceBundle.getString("lblRemitAmt"));
        lblInstNo.setText(resourceBundle.getString("lblInstNo"));
        lblRateForDelay1.setText(resourceBundle.getString("lblRateForDelay1"));
        lblIntDays.setText(resourceBundle.getString("lblIntDays"));
        lblDiscountRateOfBD.setText(resourceBundle.getString("lblDiscountRateOfBD"));
        lblOverdueInterestForBD.setText(resourceBundle.getString("lblOverdueInterestForBD"));
        lblOverdueRateCBP.setText(resourceBundle.getString("lblOverdueRateCBP"));
        lblAtParLimit.setText(resourceBundle.getString("lblAtParLimit"));
        lblCleanBillsPurchased.setText(resourceBundle.getString("lblCleanBillsPurchased"));
        lblTransitPeriod.setText(resourceBundle.getString("lblTransitPeriod"));
        lblDefaultPostage.setText(resourceBundle.getString("lblDefaultPostage"));
        lblRateForDelay.setText(resourceBundle.getString("lblRateForDelay"));
        lblIntICC.setText(resourceBundle.getString("lblIntICC"));
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtOtherName", new Boolean(true));
        mandatoryMap.put("txtOtherAddress", new Boolean(true));
        mandatoryMap.put("cboOtherCity", new Boolean(true));
        mandatoryMap.put("cboOtherState", new Boolean(true));
        mandatoryMap.put("cboOtherCountry", new Boolean(true));
        mandatoryMap.put("txtOtherPinCode", new Boolean(true));
        mandatoryMap.put("cboReceivedFrom", new Boolean(true));
        mandatoryMap.put("txtCreatDt", new Boolean(true));
        mandatoryMap.put("cboBillsType", new Boolean(true));
        mandatoryMap.put("cboActivities", new Boolean(true));
        mandatoryMap.put("cboCustCategory", new Boolean(true));
        mandatoryMap.put("txtReference", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(false));
        mandatoryMap.put("txtAccountNo", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtOtherBranchCode", new Boolean(true));
        mandatoryMap.put("txtDraweeName", new Boolean(true));
        mandatoryMap.put("txtDraweeAddress", new Boolean(true));
        mandatoryMap.put("cboDraweeCity", new Boolean(true));
        mandatoryMap.put("cboDraweeState", new Boolean(true));
        mandatoryMap.put("cboDraweeCountry", new Boolean(true));
        mandatoryMap.put("txtDraweePinCode", new Boolean(true));
        mandatoryMap.put("txtDraweeNo", new Boolean(true));
        mandatoryMap.put("txtAccountHeadValue", new Boolean(true));
        mandatoryMap.put("txtSendingTo", new Boolean(true));
        mandatoryMap.put("txtDraweeBankCode", new Boolean(true));
        mandatoryMap.put("txtDraweeBranchCode", new Boolean(true));
        mandatoryMap.put("txtDraweeBankName", new Boolean(true));
        mandatoryMap.put("txtDraweeBranchName", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo", new Boolean(true));
        mandatoryMap.put("txtInstrumentAmount", new Boolean(true));
        mandatoryMap.put("txtMICR", new Boolean(true));
        mandatoryMap.put("txtPayeeName", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(true));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        mandatoryMap.put("txtBillTenor", new Boolean(true));
        mandatoryMap.put("cboBillTenor", new Boolean(true));
        mandatoryMap.put("tdtDueDate", new Boolean(true));
        mandatoryMap.put("tdtAcceptanceDate", new Boolean(true));
        mandatoryMap.put("txtHundiNo", new Boolean(true));
        mandatoryMap.put("rdoBillAcceptance_Yes", new Boolean(true));
        mandatoryMap.put("tdtHundiDate", new Boolean(true));
        mandatoryMap.put("rdoDraweeHundi_Yes", new Boolean(true));
        mandatoryMap.put("txtHundiAmount", new Boolean(true));
        mandatoryMap.put("txtPayable", new Boolean(true));
        mandatoryMap.put("txtHundiRemarks", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtInvoiceNumber", new Boolean(true));
        mandatoryMap.put("tdtInvoiceDate", new Boolean(true));
        mandatoryMap.put("txtInvoiceAmount", new Boolean(true));
        mandatoryMap.put("txtTransportCompany", new Boolean(true));
        mandatoryMap.put("tdtRRLRDate", new Boolean(true));
        mandatoryMap.put("txtRRLRNumber", new Boolean(true));
        mandatoryMap.put("txtGoodsValue", new Boolean(true));
        mandatoryMap.put("txtGoodsAssigned", new Boolean(true));
        mandatoryMap.put("cboStdInstruction", new Boolean(true));
        mandatoryMap.put("txtStdInstruction", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtLodgementId", new Boolean(false));
    }
    
        /* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Creates an instance of Observable and adds up this ui as an Observer **/
    private void setObservable(){
        try{
            observable = MultipleAccountLodgementOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void removeRadioButtons(){
        //rdoContraAccountHead.remove(rdoContraAccountHead_Yes);
        //rdoContraAccountHead.remove(rdoContraAccountHead_No);
        rdoIntICC.remove(cRadio_ICC_Yes);
        rdoIntICC.remove(cRadio_ICC_No);
        //rdoPostDtdCheqAllowed.remove(rdoPostDtdCheqAllowed_No);
        //rdoPostDtdCheqAllowed.remove(rdoPostDtdCheqAllowed_Yes);
    }

    private void addRadioButtons(){
//        rdoContraAccountHead = new CButtonGroup();
//        rdoContraAccountHead.add(rdoContraAccountHead_Yes);
//        rdoContraAccountHead.add(rdoContraAccountHead_No);
//        
//        rdoPostDtdCheqAllowed = new CButtonGroup();
//        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_No);
//        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_Yes);
        
        rdoIntICC = new CButtonGroup();
        rdoIntICC.add(cRadio_ICC_Yes);
        rdoIntICC.add(cRadio_ICC_No);
    }
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        cboProductID.setSelectedItem(observable.getCboProductID());
        txtAccountNum.setText(observable.getTxtAccountNum());        
        cboBillsType.setSelectedItem(observable.getCboBillsType());
        cboActivities.setSelectedItem(observable.getCboActivities());
        cboCustCategory.setSelectedItem(observable.getCboCustCategory());
        cboTranstype.setSelectedItem(observable.getCboTranstype());
        txtOtherName.setText(observable.getTxtOtherName());
        txtOtherAddress.setText(observable.getTxtOtherAddress());
        cboOtherCity.setSelectedItem(observable.getCboOtherCity());
        cboOtherState.setSelectedItem(observable.getCboOtherState());
        cboOtherCountry.setSelectedItem(observable.getCboOtherCountry());
        txtOtherPinCode.setText(observable.getTxtOtherPinCode());
        cboReceivedFrom.setSelectedItem(observable.getCboReceivedFrom());
        txtReference1.setText(observable.getTxtReference());
        txtRateForDelay.setText(observable.getTxtRateForDelay());
        cboProductType1.setSelectedItem(observable.getCboProductType());
        cboProductId1.setSelectedItem(observable.getCboProductId());
        txtAccountNo1.setText(observable.getTxtAccountNo());
        txtBranchCode.setText(observable.getTxtBranchCode());
        txtBankCode.setText(observable.getTxtBankCode());
        tdtRemittedDt.setDateValue(observable.getTdtRemittedDt());
        txtOtherBranchCode.setText(observable.getTxtOtherBranchCode());
        txtDraweeName.setText(observable.getTxtDraweeName());
        txtDraweeAddress.setText(observable.getTxtDraweeAddress());
        cboDraweeCity.setSelectedItem(observable.getCboDraweeCity());
        cboDraweeState.setSelectedItem(observable.getCboDraweeState());
        cboDraweeCountry.setSelectedItem(observable.getCboDraweeCountry());
        txtDraweePinCode.setText(observable.getTxtDraweePinCode());
        txtDraweeNo.setText(observable.getTxtDraweeNo());
        txtDraweeBankNameVal.setText(observable.getTxtDraweeBankNameVal());
        txtSendingTo.setText(observable.getTxtSendingTo());
        txtDraweeBankCode.setText(observable.getTxtDraweeBankCode());
        txtDraweeBranchCode.setText(observable.getTxtDraweeBranchCode());
        txtDraweeBankName.setText(observable.getTxtDraweeBankName());
        txtDraweeBranchName.setText(observable.getTxtDraweeBranchName());
        txtInstrumentNo.setText(observable.getTxtInstrumentNo());
        txtInstPrefix.setText(observable.getTxtInstPrefix());
        txtInstrumentAmount.setText(observable.getTxtInstrumentAmount());
        txtMICR.setText(observable.getTxtMICR());
        txtPayeeName.setText(observable.getTxtPayeeName());
        txaRemarks.setText(observable.getTxtRemarks());
        tdtInstrumentDate.setDateValue(observable.getTdtInstrumentDate());
        tdtRemitInstDate.setDateValue(observable.getTdtRemitInstDate());
        txtBillTenor.setText(observable.getTxtBillTenor());
        tdtDueDate.setDateValue(observable.getTdtDueDate());
        tdtAcceptanceDate.setDateValue(observable.getTdtAcceptanceDate());
        txtHundiNo.setText(observable.getTxtHundiNo());
        rdoBillAcceptance_Yes.setSelected(observable.getRdoBillAcceptance_Yes());
        rdoBillAcceptance_No.setSelected(observable.getRdoBillAcceptance_No());
        cRadio_ICC_Yes.setSelected(observable.isCRadio_ICC_Yes());
        cRadio_ICC_No.setSelected(observable.isCRadio_ICC_No());
        tdtHundiDate.setDateValue(observable.getTdtHundiDate());
        rdoDraweeHundi_Yes.setSelected(observable.getRdoDraweeHundi_Yes());
        rdoDraweeHundi_No.setSelected(observable.getRdoDraweeHundi_No());
        txtHundiAmount.setText(observable.getTxtHundiAmount());
        txtPayable.setText(observable.getTxtPayable());
        txtHundiRemarks.setText(observable.getTxtHundiRemarks());
        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        txtInvoiceNumber.setText(observable.getTxtInvoiceNumber());
        tdtInvoiceDate.setDateValue(observable.getTdtInvoiceDate());
        txtInvoiceAmount.setText(observable.getTxtInvoiceAmount());
        txtTransportCompany.setText(observable.getTxtTransportCompany());
        tdtRRLRDate.setDateValue(observable.getTdtRRLRDate());
        txtRRLRNumber.setText(observable.getTxtRRLRNumber());
        txtGoodsValue.setText(observable.getTxtGoodsValue());
        txtGoodsAssigned.setText(observable.getTxtGoodsAssigned());
        cboStdInstruction.setSelectedItem(observable.getCboStdInstruction());
        cboInstruction.setSelectedItem(observable.getCboInstruction());
        cboRemitProdID.setSelectedItem(observable.getCboRemitProdID());
        cboRemitCity.setSelectedItem(observable.getCboRemitCity());
        cboRemitDraweeBank.setSelectedItem(observable.getCboRemitDraweeBank());
        cboRemitBranchCode.setSelectedItem(observable.getCboRemitBranchCode());
       // txtNextAccNo.setText(observable.getBillsNo());
        System.out.println("hrerere222  "+observable.getTxtLodgementId());
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW)
           txtNextAccNo.setText(observable.getTxtLodgementId());
        System.out.println("ACC numm444===="+txtNextAccNo.getText());
        txtRemitFavour.setText(observable.getTxtRemitFavour());
        txtRemitFavour1.setText(observable.getTxtRemitFavour1());
        //txtInstAmt.setText(observable.getTxtInstAmt());
        txtInst1.setText(observable.getTxtInst1());
        txtInst2.setText(observable.getTxtInst2());
        txtStdInstruction.setText(observable.getTxtStdInstruction());
        txtAreaParticular.setText(observable.getTxtAreaParticular());
        txtAmount.setText(observable.getTxtAmount());
        txtServiceTax.setText(observable.getTxtServiceTax());
	txtTotalServTax.setText(observable.getTxttotalServTax());
        txtTotalAmt.setText(observable.getTxtTotalAmt());
        cboBillTenor.setSelectedItem(observable.getCboBillTenor());
        tblInstruction.setModel(observable.getTbmInstructions());
        tblInstruction1.setModel(observable.getTbmInstructions1());
        tblInstruction2.setModel(observable.getTbmInstructions2());
        txtLodgementId.setText(observable.getTxtLodgementId());
        txtRateForDelay1.setText(observable.getTxtRateForDelay1());
        txtIntDays.setText(observable.getTxtIntDays());
        cboIntDays.setSelectedItem(observable.getCboIntDays());
        txtDiscountRateBills.setText(observable.getTxtDiscountRateBills());
        txtOverdueRateBills.setText(observable.getTxtOverdueRateBills());
        txtRateForCBP.setText(observable.getTxtRateForCBP());
        txtAtParLimit.setText(observable.getTxtAtParLimit());
        txtCleanBills.setText(observable.getTxtCleanBills());
        txtTransitPeriod.setText(observable.getTxtTransitPeriod());
        cboTransitPeriod.setSelectedItem(observable.getCboTransitPeriod());
        txtDefaultPostage.setText(observable.getTxtDefaultPostage());
        addRadioButtons();
        txtLodgementId.setEnabled(false);
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
        cboProductType1.setSelectedItem(observable.getCboProductType());
        cboProductId1.setSelectedItem(observable.getCboProductId());
        //lblCustName.setText(observable.getLblCustName());
        txtSplitAmnt.setText(observable.getTxtSplitAmnt());
        
        //tblActNo.setModel(observable.getTbmActNo());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTxtOtherName(txtOtherName.getText());
        observable.setTxtOtherAddress(txtOtherAddress.getText());
        observable.setCboOtherCity((String) cboOtherCity.getSelectedItem());
        observable.setCboOtherState((String) cboOtherState.getSelectedItem());
        observable.setCboOtherCountry((String) cboOtherCountry.getSelectedItem());
        observable.setTxtOtherPinCode(txtOtherPinCode.getText());
        observable.setCboReceivedFrom((String) cboReceivedFrom.getSelectedItem());
        observable.setCboBillsType((String) cboBillsType.getSelectedItem());
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        observable.setTxtAccountNum(txtAccountNum.getText());
        observable.setCboActivities((String) cboActivities.getSelectedItem());
        observable.setCboCustCategory((String) cboCustCategory.getSelectedItem());
        observable.setCboTranstype((String) cboTranstype.getSelectedItem());
        observable.setTxtReference(txtReference1.getText());
        observable.setTxtRateForDelay(txtRateForDelay.getText());
////        observable.setCboProductType((String) cboProductType.getSelectedItem());
////        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setCboProductType((String) cboProductType1.getSelectedItem());
        observable.setCboProductId((String) cboProductId1.getSelectedItem());
        observable.setTxtAccountNo(txtAccountNo1.getText());
        observable.setTxtBranchCode(txtBranchCode.getText());
        observable.setTxtBankCode(txtBankCode.getText());
        observable.setTdtRemittedDt(tdtRemittedDt.getDateValue());
        observable.setTxtOtherBranchCode(txtOtherBranchCode.getText());
        observable.setTxtAccountHeadValue(txtAccountHeadValue.getText());
        observable.setTxtDraweeName(txtDraweeName.getText());
        observable.setTxtDraweeAddress(txtDraweeAddress.getText());
        observable.setCboDraweeCity((String) cboDraweeCity.getSelectedItem());
        observable.setCboDraweeState((String) cboDraweeState.getSelectedItem());
        observable.setCboDraweeCountry((String) cboDraweeCountry.getSelectedItem());
        observable.setTxtDraweePinCode(txtDraweePinCode.getText());
        observable.setTxtDraweeNo(txtDraweeNo.getText());
        observable.setTxtDraweeBankNameVal(txtDraweeBankNameVal.getText());
        observable.setTxtSendingTo(txtSendingTo.getText());
        observable.setTxtDraweeBankCode(txtDraweeBankCode.getText());
        observable.setTxtDraweeBranchCode(txtDraweeBranchCode.getText());
        observable.setTxtDraweeBankName(txtDraweeBankName.getText());
        observable.setTxtDraweeBranchName(txtDraweeBranchName.getText());
        observable.setTxtInstrumentNo(txtInstrumentNo.getText());
        observable.setTxtInstPrefix(txtInstPrefix.getText());
        observable.setTxtInstrumentAmount(txtInstrumentAmount.getText());
        observable.setTxtMICR(txtMICR.getText());
        observable.setTxtPayeeName(txtPayeeName.getText());
        observable.setTxtRemarks(txaRemarks.getText());
        observable.setTdtInstrumentDate(tdtInstrumentDate.getDateValue());
        observable.setTdtRemitInstDate(tdtRemitInstDate.getDateValue());
        observable.setTxtBillTenor(txtBillTenor.getText());
        observable.setTdtDueDate(tdtDueDate.getDateValue());
        observable.setTdtAcceptanceDate(tdtAcceptanceDate.getDateValue());
        observable.setTxtHundiNo(txtHundiNo.getText());
        observable.setRdoBillAcceptance_Yes(rdoBillAcceptance_Yes.isSelected());
        observable.setRdoBillAcceptance_No(rdoBillAcceptance_No.isSelected());
        observable.setCRadio_ICC_Yes(cRadio_ICC_Yes.isSelected());
        observable.setCRadio_ICC_No(cRadio_ICC_No.isSelected());
        observable.setTdtHundiDate(tdtHundiDate.getDateValue());
        observable.setRdoDraweeHundi_Yes(rdoDraweeHundi_Yes.isSelected());
        observable.setRdoDraweeHundi_No(rdoDraweeHundi_No.isSelected());
        observable.setTxtHundiAmount(txtHundiAmount.getText());
        observable.setTxtPayable(txtPayable.getText());
        observable.setTxtHundiRemarks(txtHundiRemarks.getText());
        observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
        observable.setTxtInvoiceNumber(txtInvoiceNumber.getText());
        observable.setTdtInvoiceDate(tdtInvoiceDate.getDateValue());
        observable.setTxtInvoiceAmount(txtInvoiceAmount.getText());
        observable.setTxtTransportCompany(txtTransportCompany.getText());
        observable.setTdtRRLRDate(tdtRRLRDate.getDateValue());
        observable.setTxtRRLRNumber(txtRRLRNumber.getText());
        observable.setTxtGoodsValue(txtGoodsValue.getText());
        observable.setTxtGoodsAssigned(txtGoodsAssigned.getText());
        observable.setCboStdInstruction((String) cboStdInstruction.getSelectedItem());
        observable.setCboInstruction((String) cboInstruction.getSelectedItem());
        observable.setTxtStdInstruction(txtStdInstruction.getText());
        observable.setTxtAreaParticular(txtAreaParticular.getText());
        observable.setTxtAmount(txtAmount.getText());
        System.out.println("txtTotalAmt.getText()"+txtTotalAmt.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxttotalServTax(txtTotalServTax.getText());
//        observable.setTxtTotalAmt(calTltAmt(txtAmount.getText()));
        observable.setTxtTotalAmt(observable.getTxtTotalAmt());
        observable.setCboBillTenor((String)cboBillTenor.getSelectedItem());
        observable.setTxtLodgementId(txtLodgementId.getText());
        observable.setBillsNo(txtNextAccNo.getText());//bbb
        observable.setCboRemitProdID((String) cboRemitProdID.getSelectedItem());
        observable.setCboRemitCity((String) cboRemitCity.getSelectedItem());
        observable.setCboRemitDraweeBank((String) cboRemitDraweeBank.getSelectedItem());
        observable.setCboRemitBranchCode((String) cboRemitBranchCode.getSelectedItem());
        observable.setTxtRemitFavour(txtRemitFavour.getText());
        observable.setTxtRemitFavour1(txtRemitFavour1.getText());
        observable.setTxtInstAmt(txtInstAmt.getText());
        observable.setTxtInst1(txtInst1.getText());
        observable.setTxtInst2(txtInst2.getText());
//        observable.setTxtAreaParticular(txtAreaParticular.getText());
        observable.setTxtRateForDelay1(txtRateForDelay1.getText());
        observable.setTxtIntDays(txtIntDays.getText());
        observable.setCboIntDays((String)(cboIntDays.getSelectedItem()));
        observable.setTxtDiscountRateBills(txtDiscountRateBills.getText());
        observable.setTxtOverdueRateBills(txtOverdueRateBills.getText());
        observable.setTxtRateForCBP(txtRateForCBP.getText());
        observable.setTxtAtParLimit(txtAtParLimit.getText());
        observable.setTxtCleanBills(txtCleanBills.getText());
        observable.setTxtTransitPeriod(txtTransitPeriod.getText());
        observable.setCboTransitPeriod((String)cboTransitPeriod.getSelectedItem());
        observable.setTxtDefaultPostage(txtDefaultPostage.getText());
        System.out.println("11111111============="+chkClering.isSelected());
        if(strChkClear.equals("Y"))
             observable.setChkClering("Y");
        else
          observable.setChkClering("N");
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setTxtSplitAmnt((txtSplitAmnt.getText()));
        observable.setLblCustName(lblCustName.getText());
       
    }
    private String calTltAmt(String amt){
        double amount = 0.0;
        double tltAmt = 0.0;
        if(!txtTotalAmt.getText().equals("")){
            tltAmt = CommonUtil.convertObjToDouble(txtTotalAmt.getText()).doubleValue();
        }else{
            tltAmt = 0.0;
        }
        amount = CommonUtil.convertObjToDouble(amt).doubleValue();
        if(selectedData == 1)
            tltAmt = (tltAmt-CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
        else
            tltAmt = tltAmt+amount;
        String TotAmt = "";
        return TotAmt = CommonUtil.convertObjToStr(new Double(tltAmt));
        
    }
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        txtOtherName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherName"));
        txtOtherAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherAddress"));
        cboOtherCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOtherCity"));
        cboOtherState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOtherState"));
        cboOtherCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOtherCountry"));
        txtOtherPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherPinCode"));
        cboReceivedFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReceivedFrom"));
        txtCreatDt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreatDt"));
        cboBillsType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBillsType"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        txtAccountNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNum"));
        cboActivities.setHelpMessage(lblMsg, objMandatoryRB.getString("cboActivities"));
        cboCustCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustCategory"));
        cboTranstype.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTranstype"));
        txtReference1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReference"));
        txtRateForDelay.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateForDelay"));
        cboProductType1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboProductId1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtAccountNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNo"));
        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        txtBankCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankCode"));
        txtOtherBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherBranchCode"));
        txtDraweeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeName"));
        txtDraweeAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeAddress"));
        cboDraweeCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeCity"));
        cboDraweeState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeState"));
        cboDraweeCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeCountry"));
        txtDraweePinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweePinCode"));
        txtDraweeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeNo"));
        txtSendingTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSendingTo"));
        txtAccountHeadValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountHeadValue"));
        txtDraweeBankCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeBankCode"));
        txtDraweeBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeBranchCode"));
        txtDraweeBankName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeBankName"));
        txtDraweeBranchName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeBranchName"));
        txtInstrumentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo"));
        txtInstrumentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentAmount"));
        txtMICR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMICR"));
        txtPayeeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeName"));
        txaRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txaRemarks"));
        tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        tdtRemitInstDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRemitInstDate"));
        txtBillTenor.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillTenor"));
        cboBillTenor.setHelpMessage(lblMsg, objMandatoryRB.getString("cbBillTenor"));
        tdtDueDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDueDate"));
        tdtAcceptanceDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAcceptanceDate"));
        txtHundiNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHundiNo"));
        rdoBillAcceptance_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoBillAcceptance_Yes"));
        tdtHundiDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtHundiDate"));
        rdoDraweeHundi_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDraweeHundi_Yes"));
        txtHundiAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHundiAmount"));
        txtPayable.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayable"));
        txtHundiRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHundiRemarks"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        txtInvoiceNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvoiceNumber"));
        tdtInvoiceDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInvoiceDate"));
        txtInvoiceAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvoiceAmount"));
        txtTransportCompany.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransportCompany"));
        tdtRRLRDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRRLRDate"));
        txtRRLRNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRRLRNumber"));
        txtGoodsValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGoodsValue"));
        txtGoodsAssigned.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGoodsAssigned"));
        cboStdInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStdInstruction"));
        cboInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstruction"));
        txtStdInstruction.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStdInstruction"));
        txtAreaParticular.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAreaParticular"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtLodgementId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLodgementId"));
    }
    
    private void setMaxLengths(){
        /** Setting up Maximum allowed length to the fiels related to
         *BILLS_LODGEMENT_MASTER */
        txtAccountNo1.setMaxLength(16);
        txtDraweeAddress.setMaxLength(128);
        txtDraweeBankCode.setMaxLength(32);
        txtDraweeBranchCode.setMaxLength(32);
        txtDraweeName.setMaxLength(64);
        txtDraweeNo.setMaxLength(16);
        txtDraweePinCode.setMaxLength(64);
        txtLodgementId.setMaxLength(16);
        txtOtherAddress.setMaxLength(128);
        txtBranchCode.setMaxLength(16);
        txtOtherName.setMaxLength(64);
        txtBankCode.setMaxLength(16);
        txtOtherBranchCode.setMaxLength(16);
        txtOtherPinCode.setMaxLength(32);
        txtReference1.setMaxLength(32);
        txtSendingTo.setMaxLength(32);
        txtDraweeBankNameVal.setAllowAll(true);
        txtAreaParticular.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    // Add your handling code here:
                    /*if( uiComponentValidation != null ){
                        uiComponentValidation.validateEvent(evt);
                    }*/
                    int length = txtAreaParticular.getText().length();
                    if( length >= 250 ){
                        if( checkInValidCharacter(evt.getKeyChar())){
                            evt.consume();
                        }
                    } 
                }
            }
        );
        /** Setting up the Maximum allowed length to the fileds
         *related to BILLS_LODGEMENT_INSTRUCTION */
        txtStdInstruction.setMaxLength(64);
        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(16,2));
        txtServiceTax.setMaxLength(16);
        txtServiceTax.setValidation(new CurrencyValidation(16,2));
//        txtTotalServTax.setValidation(new CurrencyValidation());
        txtTotalServTax.setMaxLength(16);
        /** Setting up the Maximum allowed length to the fields
         *related to BILLS_LODGEMENT_CHEQUE **/
        txtDraweeBankName.setMaxLength(64);
        txtDraweeBranchName.setMaxLength(64);
        txtInstrumentAmount.setMaxLength(16);
        txtInstrumentNo.setMaxLength(32);
        txtMICR.setMaxLength(32);
        txtPayeeName.setMaxLength(64);
       // txaRemarks.setMaxLength(256);
        
        /** Setting up the Maximum allowed length to the fields related to
         * BILLS_LODGEMENT_HUNDI **/
        txtGoodsAssigned.setMaxLength(64);
        txtGoodsValue.setMaxLength(16);
        txtHundiAmount.setMaxLength(16);
        txtHundiNo.setMaxLength(32);
        txtInvoiceAmount.setMaxLength(16);
        txtInvoiceNumber.setMaxLength(32);
        txtRRLRNumber.setMaxLength(32);
        txtPayable.setMaxLength(32);
       // txaRemarks.setMaxLength(256);
        txtBillTenor.setMaxLength(6);
        txtBillTenor.setAllowNumber(true);
        txtTransportCompany.setMaxLength(32);
        txtInst2.setMaxLength(32);
        txtInst2.setValidation(new NumericValidation());
        txtInstAmt.setMaxLength(16);
        txtInstAmt.setValidation(new CurrencyValidation(16,2));
        txtRemitFavour.setMaxLength(16);
        txtRemitFavour.setValidation(new CurrencyValidation(16,2));
        txtRateForDelay.setMaxLength(8);
        txtRateForDelay.setValidation(new PercentageValidation());
        txtSplitAmnt.setMaxLength(16);
        txtSplitAmnt.setValidation(new CurrencyValidation(16,2));
    }
    
    /** Setting models to all the comboBoxes in the Ui **/
    private void initComponentData(){
        cboProductID.setModel(observable.getCbmProductID());
        cboProductId1.setModel(observable.getCbmProductId());
        cboBillsType.setModel(observable.getCbmBillsType());
        cboActivities.setModel(observable.getCbmActivities());
        cboCustCategory.setModel(observable.getCbmCustCategory());
        cboTranstype.setModel(observable.getCbmTrantype());
        cboReceivedFrom.setModel(observable.getCbmReceivedFrom());
        cboOtherState.setModel(observable.getCbmOtherState());
        cboOtherCity.setModel(observable.getCbmOtherCity());
        cboOtherCountry.setModel(observable.getCbmOtherCountry());
        cboDraweeState.setModel(observable.getCbmDraweeState());
        cboDraweeCity.setModel(observable.getCbmDraweeCity());
        cboDraweeCountry.setModel(observable.getCbmDraweeCountry());
        cboInstrumentType.setModel(observable.getCbmInstrumentType());
        cboBillTenor.setModel(observable.getCbmBillTenor());
        cboStdInstruction.setModel(observable.getCbmStdInstruction());
        cboInstruction.setModel(observable.getCbmInstruction());
        cboProductType1.setModel(observable.getCbmProductType());
        tblInstruction.setModel(observable.getTbmInstructions());
        tblInstruction1.setModel(observable.getTbmInstructions1());
        tblInstruction2.setModel(observable.getTbmInstructions2());
        cboRemitProdID.setModel(observable.getCbmRemitProdID());
        cboRemitCity.setModel(observable.getCbmRemitCity());
        cboRemitDraweeBank.setModel(observable.getCbmRemitDraweeBank());
        cboRemitBranchCode.setModel(observable.getCbmRemitBranchCode());
        cboIntDays.setModel(observable.getCbmIntDays());
        cboTransitPeriod.setModel(observable.getCbmTransitPeriod());
        // For multiple lodgement
        cboProductType1.setModel(observable.getCbmProductType());        
        cboProductId1.setModel(observable.getCbmProductId());
        //tblActNo.setModel(observable.getTbmActNo());
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    private void setButtonEnableDisable() {
       /* btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        lblStatus.setText(observable.getLblStatus());*/
        btnNew.setEnabled(true);
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnSave.isEnabled());
        btnReject.setEnabled(!btnSave.isEnabled());
        btnException.setEnabled(!btnSave.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(true);
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
       // btnVer.setEnabled(!btnSave.isEnabled());
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        MultipleAccountLodgementUI ui = new MultipleAccountLodgementUI();
        frame.getContentPane().add(ui);
        frame.setSize(600, 600);
        frame.setVisible(true);
        ui.setVisible(true);
    }
    
    /** To enable or disable btnBankCode and btnOtherBranchCode **/
    private void setOtherBranchBankBtnEnableDisable(boolean flag){
        btnBankCode.setEnabled(flag);
        btnOtherBranchCode.setEnabled(flag);
    }
    
    /** To enable or disable btnBranchCode **/
    private void setBranchBtnEnableDisable(boolean flag){
        btnBranchCode.setEnabled(flag);
    }
    
    /** To enable or disable btnAccountNo **/
    private void setHelpBtnEnableDisable(boolean flag){
        btnAccountNo1.setEnabled(flag);
        btnAccountNo1.setEnabled(flag); // Added for multiple lodgement
    }
     private boolean checkInValidCharacter(char keyChar){
        if (!((keyChar == java.awt.event.KeyEvent.VK_BACK_SPACE) ||
             (keyChar == java.awt.event.KeyEvent.VK_DELETE))) {
            return true;
        }
        return false;
    }
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if(currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            HashMap where = new HashMap();
            where.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectMultiLodgementMaster");
             viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }else if(currField.equals(QBM)){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBranch_Master");
        }else if(currField.equals(QOB) || currField.equals(QDB)){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank");
        }else if(currField.equals(QOBB)){
            HashMap where = new HashMap();
            where.put("BANK_CODE", txtBankCode.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank_Branch");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }else if(currField.equals(QDBB)){
            HashMap where = new HashMap();
            where.put("BANK_CODE", txtDraweeBankCode.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank_Branch");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }else if(currField.equals(AN)){
                HashMap where = new HashMap();
                where.put("PROD_ID", observable.getCbmProductId().getKeyForSelected());
                where.put("SELECTED_BRANCH",((ComboBoxModel) TrueTransactMain.cboBranchList.getModel()).getKeyForSelected());
                String prodType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
            if(!prodType.equals("") && prodType!= null && !prodType.equals("GL")){
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"+prodType);
            }else{
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
            System.out.println("inside MultipleAccountLodgementUI :: " + where);
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
        }else if(currField.equals(CPDAN)){
            if(cboProductID.getSelectedIndex()>0 && cboProductID.getSelectedIndex()>0){
                HashMap where = new HashMap();
                where.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
                where.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
                String prodType = "BILLS";
                if(!prodType.equals("") && prodType!= null){
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"+prodType);
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                }
                where = null;
            }
        }
        new ViewAll(this,viewMap).show();
    }
        /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
//        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
//            ArrayList lst = new ArrayList();
//            lst.add("BANK_CODE");
//            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
//            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewBillsLodgementHistory");
//        }
        new ViewAll(this, viewMap).show();
    }
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
     
     private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
        btnCancel.setEnabled(true);
        mitCancel.setEnabled(true);
    }
     public HashMap asAnWhenCustomerComesYesNO(String acct_no) {
        HashMap map = new HashMap();
        map.put("ACT_NUM", acct_no);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        System.out.println("mmmmm====="+map);
        List lst = null;
            lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
        }
        return map;
    }
     private HashMap interestCalculationTLAD(String accountNo, long noOfInstallment,String prod_Type) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt.clone());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prod_Type.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", getSelectedBranchID());
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                if (noOfInstallment > 0) {
                    map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                }
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt.clone());
                hash = loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
     }
      public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {//dont delete this methode check select dao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapData;
    }
     public void getTransdetailsFill(String acc_No,String productType)
      {
           if(productType.equals("TL"))
                {
                    HashMap map1=new HashMap();
                    map1.put("ACT_NUM", acc_No);
                    System.out.println("acc_Noacc_Noacc_No ======="+acc_No);
                    map1 = asAnWhenCustomerComesYesNO(acc_No);
                    String prod_id=CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId1.getModel()).getKeyForSelected());
            if (map1 != null && map1.containsKey("AS_CUSTOMER_COMES") && map1.get("AS_CUSTOMER_COMES").equals("Y")) {
                map1.put("ACCT_NUM", acc_No);
                map1.put("PROD_TYPE",productType);
                map1.put("PROD_ID",prod_id);
                map1.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                }
                if (productType.equals("TL") && map1.containsKey("BEHAVES_LIKE")
                        && CommonUtil.convertObjToStr(map1.get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
                    HashMap map2 = new HashMap();
                    map2.put("ACT_NUM", acc_No);
                    map2.put("CURR_DT", currDt);
                    List lst = ClientUtil.executeQuery("getDisbursementTrenchDetailsCorpTL", map2);
                    if (lst != null && lst.size() > 0) {
                        map2 = (HashMap) lst.get(0);
                        map2.put("BEHAVES_LIKE", map1.get("BEHAVES_LIKE"));
                        transDetails.setCorpDetailMap(map2);
                    } else {
                    }
                }
                long no_of_installment = 0;
                no_of_installment = CommonUtil.convertObjToLong(map1.get("NO_OF_INSTALLMENT"));
                HashMap asAndWhenMap = interestCalculationTLAD((String) map1.get("ACCT_NUM"), no_of_installment,productType);
                if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                    asAndWhenMap.put("INSTALL_TYPE", map1.get("INSTALL_TYPE"));
                    transDetails.setAsAndWhenMap(asAndWhenMap);
                }
                }
      }
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash1 = (HashMap) map;
        System.out.println("hashhashhashhashhash"+hash1);
        if(hash1.containsKey("FROM_AUTHORIZE_LIST_UI")){
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSaveDisable();
            btnReject.setEnabled(false);
            rejectFlag=1;
        }
        if (hash1.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSaveDisable();
            btnReject.setEnabled(false);
            rejectFlag=1;
        }
        if (hash1.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSaveDisable();
            btnReject.setEnabled(false);
            rejectFlag=1;
        }
        setModified(true);
        double delayRate = 0;
        HashMap hash = (HashMap) map;
         HashMap hashData = new HashMap();
        hashData.put("PROD_ID", CommonUtil.convertObjToStr(hash.get("BILLS_TYPE")));
        List lstData = ClientUtil.executeQuery("getOperatingType", hashData);
        hashData = null;
        if(lstData != null && lstData.size() > 0){
            hashData = (HashMap) lstData.get(0);
        }

        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[16])) {
                LodgeID = CommonUtil.convertObjToStr(hash.get("LODGEMENT_ID"));
                delayRate = CommonUtil.convertObjToDouble(hash.get("DELAY_RATE")).doubleValue();
                hash.put(CommonConstants.MAP_WHERE, hash.get("LODGEMENT_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("BILL_STATUS", hash.get("BILL_STATUS"));
                if(viewType.equals(ClientConstants.ACTION_STATUS[16]))
                    hash.put("HISTORY", hash.get("BILL_STATUS"));
                System.out.println("hashhashjjj"+hash);
                observable.populateData(hash);
                if(((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("REALIZE")) || 
                (CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("DISHONOUR")) ||
                (CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("CLOSURE"))) || 
                ((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("ICC"))) ||
                ((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("CPD"))))
                    populateTransData(hash);
                 String productType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
                getTransdetailsFill(txtAccountNo1.getText(),productType);
                System.out.println("aswaaswaaswaaswaaswa");
                if ((observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE) || (observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)){
                    ClientUtil.enableDisable(panLodgementBIlls, false);
                    setPanOperationsEnable(false);
                    setHelpBtnEnableDisable(false);
                    setBranchBtnEnableDisable(false);
                    setOtherBranchBankBtnEnableDisable(false);
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
                        btnView.setEnabled(false);
                    }
                }

                    if((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("")) &&
                    ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("LODGEMENT"))) && 
                    (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){

                        ClientUtil.enableDisable(panLodgementBIlls,true);
                        cboActivities.setEnabled(false);

                    }
                if(((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("AUTHORIZED")) &&
                    ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("LODGEMENT"))) &&
                    (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)) || ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("LODGEMENT")) &&
                    (((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("ICC"))) || ((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("CPD")))))){
                      System.out.println("SUB_REG_TYPE=====cpd");
                        ClientUtil.enableDisable(panLodgementBIlls,true);
                        cboActivities.setEnabled(true);
                        txtRateForDelay.setEnabled(false);    
                        enableDisablePan(false);
                        if((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("LODGEMENT")) &&
                    (((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("ICC"))) || ((CommonUtil.convertObjToStr(hashData.get("SUB_REG_TYPE")).equals("CPD")))) &&
                    (CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals(""))){
                        cboActivities.setEnabled(false);
                        cboCustCategory.setEnabled(false);
                        }

                    }
                 if((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("")) &&
                    ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("PROCEEDS_RECEIVED"))) && 
                    (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
                          System.out.println("SUB_REG_TYPE=====cpd111111111111111");
                        ClientUtil.enableDisable(panLodgementBIlls,true);
                        enableDisablePan(false);
                        cboActivities.setEnabled(false);
                        cboCustCategory.setEnabled(false);
                        ClientUtil.enableDisable(panStdInstructions,false);
                        ClientUtil.enableDisable(panOperations,false);
//                        setPanOperationsEnable(false);

                    }
                  if((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("AUTHORIZED")) &&
                    ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("PROCEEDS_RECEIVED"))) &&
                    (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
                           System.out.println("SUB_REG_TYPE=====cpd22222222222222");
                        ClientUtil.enableDisable(panLodgementBIlls,true);
                        cboActivities.setEnabled(true);
                        enableDisablePan(false);
                        ClientUtil.enableDisable(panRemitList,false);

                    }
                 if(((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("AUTHORIZED")) &&
                    ((CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("REALIZE")) || 
                    (CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("DISHONOUR")))) || ((observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW))){
                          System.out.println("SUB_REG_TYPE=====cpd3333333333333333");
                        ClientUtil.enableDisable(panLodgementBIlls,false);

                        ClientUtil.enableDisable(panRemitList,false);
                        btnSave.setEnabled(false);
                        btnBranchCode.setEnabled(false);
                        btnAccountNo1.setEnabled(false);
                        
                        

                    }

                if(viewType.equals(AUTHORIZE)){
                      System.out.println("SUB_REG_TYPE=====cpd44444444444444444");
                    ClientUtil.enableDisable(panLodgementBIlls, false);
                    setPanOperationsEnable(false);
                    setHelpBtnEnableDisable(false);
                    setBranchBtnEnableDisable(false);
                    setOtherBranchBankBtnEnableDisable(false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    String productType1 = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
                    getTransdetailsFill(txtAccountNo1.getText(),productType1);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    
                }
                setButtonEnableDisable();
                fillBankBranchName(txtBranchCode.getText(),txtBankCode.getText(),lblBranchCodeValue,"getBranchName");
                fillBankBranchName(txtBankCode.getText(),txtBankCode.getText(),lblBankNameValue,"getBankName");
                fillBankBranchName(txtOtherBranchCode.getText(),txtBankCode.getText(),lblOtherBranchNameValue,"getOtherBankBranchName");
                if(observable.getTxtAccountNo()!=null && observable.getTxtAccountNo().length()>0)
                lblCustName.setText(observable.getCustomerName());
                
                if((observable.getSubRegType().equals("CPD")) && (hashData.get("OPERATES_LIKE").equals("OUTWARD"))){
                    //Added By Suresh
                      System.out.println("SUB_REG_TYPE=====cpd555555555555555");
                    if(observable.getTxtAccountNum()!=null && observable.getTxtAccountNum().length()>0){
                    transDetails.setTransDetails("BILLS", ProxyParameters.BRANCH_ID, observable.getTxtAccountNum());
                    }else{
                        observable.setTxtAccountNum("");
                        observable.setTxtAccountNo("");
                    }
                    HashMap mapData = new HashMap();
                    mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected()));
                    mapData.put("ACT_NUM", observable.getTxtAccountNum());
                    mapData.put("ACT_NAME", observable.getCustomerNameCPD());
                    observable.populateTblActData(mapData,observable.getSubRegType());
                    mapData = new HashMap();
                    mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId1.getModel()).getKeyForSelected()));
                    mapData.put("ACT_NUM", observable.getTxtAccountNo());
                    mapData.put("ACT_NAME", lblCustName.getText());
                    observable.populateTblActData(mapData,observable.getSubRegType());
                    System.out.println("gggggggggyyyyyyyyyyyyuuuuuuuuuu");
                    mapData = null;
                }
                 if(!(observable.getSubRegType().equals("CPD"))){
                       System.out.println("SUB_REG_TYPE=====cpd478");
                    String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
                    if(observable.getTxtAccountNo()!=null && observable.getTxtAccountNo().length()>0){
                    transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, observable.getTxtAccountNo());
                    }else{
                        observable.setTxtAccountNum("");
                        observable.setTxtAccountNo("");
                    }
                    HashMap mapData = new HashMap();
                    mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId1.getModel()).getKeyForSelected()));
                    mapData.put("ACT_NUM", observable.getTxtAccountNo());
                    mapData.put("ACT_NAME", lblCustName.getText());
                    observable.populateTblActData(mapData,observable.getSubRegType());
                    mapData = null;
                }
                    lblCurStatus1.setVisible(true);
                    lblCurStatus.setVisible(true);
                    lblLodgementId.setVisible(true);
                    txtLodgementId.setVisible(true);    
                    lblCurStatus.setText(CommonUtil.convertObjToStr(hash.get("BILL_STATUS")));
                    txtCreatDt.setText(CommonUtil.convertObjToStr(hash.get("CREATED_DT")));
                    if(delayRate > 0){
                        lblRateForDelay.setVisible(true);
                        panOverdueRateBills8.setVisible(true);
                    }else{
                        lblRateForDelay.setVisible(false);
                        panOverdueRateBills8.setVisible(false);
                    }
                    if(txtBankCode.getText().length()>0 && txtOtherBranchCode.getText().length()>0){
                        System.out.println("uuuuuuuuuuuuuuuuuiiiiiiiiiiii");
                        HashMap accHeadMap = new HashMap();
                        accHeadMap.put("BANK_CODE",txtBankCode.getText());
                        accHeadMap.put("BRANCH_CODE",txtOtherBranchCode.getText());
                        System.out.println("getSelectOtherBankBranchAccHead"+accHeadMap);
                        List accHeadLst = ClientUtil.executeQuery("getSelectOtherBankBranchAccHead", accHeadMap);
                       
                        System.out.println("accHeadLst"+accHeadLst);
                        if(accHeadLst!=null &&accHeadLst.size()>0){
                            accHeadMap = (HashMap)accHeadLst.get(0);
                             observable.setOtherBankProdType(CommonUtil.convertObjToStr(accHeadMap.get("PROD_TYPE")));
                            txtAccountHeadValue.setText(CommonUtil.convertObjToStr(accHeadMap.get("ACCOUNT_HEAD")));
                            observable.setTxtAccountHeadValue(txtAccountHeadValue.getText());
                            String prodTyp=CommonUtil.convertObjToStr(accHeadMap.get("PROD_TYPE"));
                            System.out.println("prodTypprodTyp"+prodTyp);
                            if(prodTyp.equals("INV"))
                            {
                            List accHeadLst1 = ClientUtil.executeQuery("getSelInvAcHd", accHeadMap);
                            System.out.println("accHeadLst1"+accHeadLst1);
                            if(accHeadLst1!=null &&accHeadLst1.size()>0){
                            accHeadMap = (HashMap)accHeadLst1.get(0);
                                System.out.println("accHeadMap======"+accHeadMap);
                            observable.setOtherBankProdType(CommonUtil.convertObjToStr(accHeadMap.get("PROD_TYPE")));
                            txtAccountHeadValue.setText(CommonUtil.convertObjToStr(accHeadMap.get("IINVESTMENT_AC_HD")));
                            observable.setTxtAccountHeadValue(txtAccountHeadValue.getText());
                            }
                            }
                            if(prodTyp.equals("AB"))
                            {
                              List accHeadLst1 = ClientUtil.executeQuery("getSelRefNo", accHeadMap);
                            System.out.println("accHeadLst1"+accHeadLst1);
                            if(accHeadLst1!=null &&accHeadLst1.size()>0){
                            accHeadMap= new HashMap();
                            accHeadMap = (HashMap)accHeadLst1.get(0);
                            lblActRefNo.setText(CommonUtil.convertObjToStr(accHeadMap.get("ACT_REF_NO")));
                            }
                            
                            }
                            
                        }
                    }
                  
            }else if(viewType.equals(QBM)){
                txtBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtSendingTo.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
                txtDraweeBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                txtDraweeBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtDraweeName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
                txtDraweeAddress.setText(CommonUtil.convertObjToStr(hash.get("STREET")));
                cboDraweeCity.setSelectedItem(CommonUtil.convertObjToStr(hash.get("CITY")));
                cboDraweeState.setSelectedItem(CommonUtil.convertObjToStr(hash.get("STATE")));
                cboDraweeCountry.setSelectedItem(CommonUtil.convertObjToStr(hash.get("COUNTRY")));
                txtDraweePinCode.setText(CommonUtil.convertObjToStr(hash.get("PIN_CODE")));
                txtMICR.setText(CommonUtil.convertObjToStr(hash.get("MICR_CODE")));
                fillBankBranchName(txtBranchCode.getText(),txtBankCode.getText(),lblBranchCodeValue,"getBranchName");       
            }else if(viewType.equals(QOB)){
                txtBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                fillBankBranchName(txtBankCode.getText(),txtBankCode.getText(),lblBankNameValue,"getBankName");
            }else if(viewType.equals(QDB)){
                txtDraweeBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                txtDraweeBankNameVal.setText(CommonUtil.convertObjToStr(hash.get("BANK_NAME")));
            }else if(viewType.equals(QDBB)){
                txtDraweeBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtDraweeName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                txtDraweeAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
                cboDraweeCity.setSelectedItem(CommonUtil.convertObjToStr(hash.get("CITY")));
                cboDraweeState.setSelectedItem(CommonUtil.convertObjToStr(hash.get("STATE")));
                cboDraweeCountry.setSelectedItem(CommonUtil.convertObjToStr(hash.get("COUNTRY")));
                txtDraweePinCode.setText(CommonUtil.convertObjToStr(hash.get("PINCODE")));
            }else if(viewType.equals(QOBB)){
                txtOtherBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtSendingTo.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
//                txtDraweeBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
//                txtDraweeBranchCode.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
//                txtDraweeName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
//                txtDraweeAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
//                cboDraweeCity.setSelectedItem(CommonUtil.convertObjToStr(hash.get("CITY")));
//                cboDraweeState.setSelectedItem(CommonUtil.convertObjToStr(hash.get("STATE")));
//                cboDraweeCountry.setSelectedItem(CommonUtil.convertObjToStr(hash.get("COUNTRY")));
//                txtDraweePinCode.setText(CommonUtil.convertObjToStr(hash.get("PINCODE")));
                txtMICR.setText(CommonUtil.convertObjToStr(hash.get("MICR")));
                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT_HEAD")));
                lblActRefNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_REF_NO")));
                txtAccountHeadValue.setEnabled(false);
                fillBankBranchName(txtOtherBranchCode.getText(),txtBankCode.getText(),lblOtherBranchNameValue,"getOtherBankBranchName");
            }else if(viewType.equals(AN)){
                String productType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
                String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
                if( productType != null && !productType.equals("GL")){
                if(productType.equals("TD") && ACCOUNTNO.lastIndexOf("_")==-1){
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                }
                txtAccountNo1.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                txtAccountNo1.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    observable.setTxtAccountNo(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                lblCustName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                }else{
                    txtAccountNo1.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                    txtAccountNo1.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    observable.setTxtAccountNo(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                    lblCustName.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));//Modified by Chithra on 11-04-14
                }
                System.out.println("productType ======="+productType+" aaa==="+hash.get("ACCOUNTNO"));
               getTransdetailsFill(ACCOUNTNO,productType);
                if(!(observable.getSubRegType().equals("CPD"))){
                    String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
                    if(txtAccountNo1.getText().length()>0){
                    transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, txtAccountNo1.getText());
                    }
                    HashMap mapData = new HashMap();
                    mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId1.getModel()).getKeyForSelected()));
                    mapData.put("ACT_NUM", hash.get("ACCOUNTNO"));
                    mapData.put("ACT_NAME", hash.get("CUSTOMERNAME"));
                    observable.populateTblActData(mapData,observable.getSubRegType());
                    tblInstruction2.setModel(observable.getTbmInstructions2());
                    mapData = null;
                    // For multiple bills lodgement nithya 
                    // Checking for available balance
                    HashMap availableBalCheckMap = new HashMap();
                    availableBalCheckMap.put("ACCOUNTNO",CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    List availableBalLst = ClientUtil.executeQuery("getOABalance", availableBalCheckMap);
                    if(availableBalLst != null && availableBalLst.size() > 0){
                        HashMap balMap = (HashMap)availableBalLst.get(0);
                        if(balMap.containsKey("AVAILABLE_BALANCE") && null != balMap.get("AVAILABLE_BALANCE") && !balMap.get("AVAILABLE_BALANCE").equals("")){
                            Double availableActBal = CommonUtil.convertObjToDouble(balMap.get("AVAILABLE_BALANCE"));
                            Double enteredAmt = CommonUtil.convertObjToDouble(txtSplitAmnt.getText());
                            if(availableActBal < enteredAmt){
                                displayAlert("Available Balance( "+ balMap.get("AVAILABLE_BALANCE") +" )is less than the input Amount");
                                txtAccountNo1.setText("");
                                lblCustName.setText("");
                            }else{
                                txtAccountNo1.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                                observable.setTxtAccountNo(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                                lblCustName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                            }
                        }
                    }
                    //
                    
                    // End
                }
                if((observable.getSubRegType().equals("CPD"))){
                    HashMap mapData = new HashMap();
                    mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId1.getModel()).getKeyForSelected()));
                    mapData.put("ACT_NUM", hash.get("ACCOUNTNO"));
                    mapData.put("ACT_NAME", hash.get("CUSTOMERNAME"));
                    observable.populateTblActData(mapData,observable.getSubRegType());
                    tblInstruction2.setModel(observable.getTbmInstructions2());
                    mapData = null;
                }
            }else if(viewType.equals(CPDAN)){
                txtAccountNum.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                if(txtAccountNum.getText().length()>0){
                transDetails.setTransDetails("BILLS", ProxyParameters.BRANCH_ID, txtAccountNum.getText());
                }
                HashMap mapData = new HashMap();
                mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected()));
                mapData.put("ACT_NUM", hash.get("ACCOUNTNO"));
                mapData.put("ACT_NAME", hash.get("CUSTOMERNAME"));
                observable.populateTblActData(mapData,observable.getSubRegType());
                tblInstruction2.setModel(observable.getTbmInstructions2());
                mapData = null;
            }
            txtLodgementId.setEnabled(false);
        }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
         if(hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if(hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
         System.out.println("viewType-->"+viewType +"AUTHORIZE"+AUTHORIZE +"GGG-->"+ClientConstants.ACTION_STATUS[2]) ;  
       if((viewType == AUTHORIZE || viewType==ClientConstants.ACTION_STATUS[2] ) && !CommonUtil.convertObjToStr(hash.get("BILL_STATUS")).equals("LODGEMENT")) {
          System.out.println("TTTTTTTTTTTTT");
          editModeTransDetail(null);
       }
       if (viewType.equals(ClientConstants.ACTION_STATUS[2])){
           btnSave.setEnabled(true);
           if(hash1.containsKey("AUTHORIZE_STATUS")){
               if(CommonUtil.convertObjToStr(hash1.get("AUTHORIZE_STATUS")).equals("AUTHORIZED")){
                   cboProductType1.setEnabled(true);
                   cboProductId1.setEnabled(true);
                   txtAccountNo1.setEnabled(true);
                   btnAccountNo1.setEnabled(true);
               }
           }
       }
    }
    private void printIfContraHeadExsist() {
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_FAILED) {
            String activity = observable.getCboActivities();
            if ((activity != null && activity.equalsIgnoreCase("REALIZE")) && observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                HashMap transTypeMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType1.getModel()).getKeyForSelected());
                if (prodType != null && prodType.equals("TL")) {
                    transCashMap.put("BATCH_ID", txtAccountNo1.getText());
                } else {
                    transCashMap.put("BATCH_ID", txtLodgementId.getText());
                }
                transCashMap.put("TRANS_DT", currDt.clone());
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                transCashMap.put("AUTHORIZE", "AUTHORIZED");
                HashMap transIdMap = new HashMap();
                List lst = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (lst != null && lst.size() > 0) {
//                    for (int i = 0; i < lst.size(); i++) {
                        transMap = (HashMap) lst.get(lst.size()-1);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    }
//                }
                lst = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        transMap = (HashMap) lst.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                        transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    }
                }
                if (transIdMap != null && transIdMap.size() > 0) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("TransDt", currDt.clone());
                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        Object keys[] = transIdMap.keySet().toArray();
                        for (int i = 0; i < keys.length; i++) {
                            paramMap.put("TransId", keys[i]);
                            ttIntgration.setParam(paramMap);
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                ttIntgration.integrationForPrint("ReceiptPayment");
                            } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
                                ttIntgration.integrationForPrint("CashPayment", false);
                            } else {
                                ttIntgration.integrationForPrint("CashReceipt", false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    //    private void setEnableDisable(boolean flag){
    //                        ClientUtil.enableDisable(panBranchBankDetails, false);
    //                        ClientUtil.enableDisable(panOtherDetails, false);
    //                        ClientUtil.enableDisable(panProductDetails, false);
    //                        ClientUtil.enableDisable(panDraweeDetails, false);
    //                        ClientUtil.enableDisable(panInstrumentDetails, false);
    //                        ClientUtil.enableDisable(panDocumentDetails, false);
    //                        ClientUtil.enableDisable(panAccountNo, false);
    //                        ClientUtil.enableDisable(panRemitList, false);
    //                        btnBranchCode.setEnabled(false);
    //                        btnBankCode.setEnabled(false);
    //                        btnOtherBranchCode.setEnabled(false);
    //                        btnAccountNo.setEnabled(false);
    //                        cboCustCategory.setEnabled(false);
    //                        cboBillsType.setEnabled(false);
    //                        cboReceivedFrom.setEnabled(false);
    //                        cboActivities.setEnabled(false);
    //    }
    private void editModeTransDetail(HashMap authMap) {
        HashMap transMap = new HashMap();
        String displayStr = "";
     //   if (viewType == AUTHORIZE) {

            String oldBatchId = "";
            String newBatchId = "";

            String actNum = CommonUtil.convertObjToStr(txtLodgementId.getText());
            transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", actNum);
            transMap.put("CURR_DT", currDt.clone());
            transMap.put("TRANS_DT", currDt.clone());
            transMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            if (authMap != null && authMap.size() > 0 && authMap.containsKey("AUTHORIZE")) {
                transMap.put("AUTHORIZE", "AUTHORIZED");
            }
            List lst = ClientUtil.executeQuery("getAuthBatchTxTransferTOs", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Transfer Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {

                    TxTransferTO objTxTransferTO = (TxTransferTO) lst.get(i);
                   	HashMap glMap = new HashMap();
                    glMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                    glMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                    String acc_Hd = "";
                    List gllst = ClientUtil.executeQuery("getGlhead", glMap);
                    if (gllst != null && gllst.size() > 0) {
                        glMap = (HashMap) gllst.get(0);
                        if (glMap != null && glMap.containsKey("AC_HD_DESC")) {
                            acc_Hd = CommonUtil.convertObjToStr(glMap.get("AC_HD_DESC"));
                        }
                    }
                    displayStr += "Trans Id : " + objTxTransferTO.getTransId()
                            + "   Batch Id : " + objTxTransferTO.getBatchId()
                            + "   Trans Type : " + objTxTransferTO.getTransType();
                    actNum = CommonUtil.convertObjToStr(objTxTransferTO.getActNum());
                    displayStr += "   Ac Hd Desc : " + objTxTransferTO.getAcHdId()
                            + " " + acc_Hd + " :" + objTxTransferTO.getAmount() + "\n";

                }
            }
            ///////////////////////////////
            transMap.put("BATCH_ID", CommonUtil.convertObjToStr(txtLodgementId.getText()));
                lst = ClientUtil.executeQuery("getCashDetails", transMap);
                        if (lst != null && lst.size() > 0) {
                            displayStr += "Cash Transaction Details...\n";
                            for (int i = 0; i < lst.size(); i++) {
                                transMap = (HashMap) lst.get(i);
                                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                                String actNumber = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                                if (actNumber != null && !actNumber.equals("")) {
                                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                                            + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                                } else {
                                    displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                                            + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                                }
                               // transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                              //  transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                            }
                        }
            ///////////////////////////////

      //  }
        System.out.println("displayStr ---->"+displayStr) ;               
        if(displayStr!=null && !displayStr.equals("")){
            ClientUtil.showMessageWindow(displayStr);
        }
    }
    private void enableDisablePan(boolean flag){
        cboTranstype.setEnabled(flag);
        cboBillsType.setEnabled(flag);
        cboProductID.setEnabled(flag);
        btnAccountNum.setEnabled(flag);
        ClientUtil.enableDisable(panAccountNo1,flag);
        //        panAccountNo1.setEnabled(flag);
        cboReceivedFrom.setEnabled(flag);
        ClientUtil.enableDisable(panOtherDetails,flag);
        //        panOtherDetails.setEnabled(flag);
        btnBranchCode.setEnabled(flag);
        btnBankCode.setEnabled(flag);
        btnOtherBranchCode.setEnabled(flag);
        //ClientUtil.enableDisable(panProductDetails,flag);
        //        ClientUtil.enableDisable(panDraweeDetails,flag);
        //        ClientUtil.enableDisable(panInstrumentDetails,flag);
        
        ClientUtil.enableDisable(panInstructions,flag);
        ClientUtil.enableDisable(panInstructions2,flag);
        ClientUtil.enableDisable(panRRLRARPPDetails1,flag);
        btnAccountNo1.setEnabled(flag);
        ClientUtil.enableDisable(panInvoiceDetails,flag);
        ClientUtil.enableDisable(panRRLRARPPDetails,flag);
        ClientUtil.enableDisable(panInstrumentType,flag);
        ClientUtil.enableDisable(panChequeDetails,flag);
        ClientUtil.enableDisable(panBillofExchange,flag);
    }
    public void populateTransData(HashMap hash) {
        //        updateOBFields();
        System.out.println("11111111111111@@@@");
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        //        observable.setTxtLodgementId(CommonUtil.convertObjToStr(hash.get()
        viewMap.put(CommonConstants.MAP_NAME, "getAllBillsTransactions");
        whereMap.put("LODGEMENT_ID", hash.get("LODGEMENT_ID"));
        whereMap.put("AUTHORIZE_REMARKS","INVESTMENT_CONTRA");
        if(hash.containsKey("HISTORY"))
            whereMap.put("BILL_STATUS", hash.get("BILL_STATUS"));
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            //            log.info("populateData...");
            ArrayList heading = observable.populateTransData(viewMap, tblData);
            System.out.println("headingheading"+heading);
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    
    /** Method to fill up the Bank or Branch Names in the labels provided according to branch
     *code or bankcode selected **/
    private void fillBankBranchName(String code, String bankCode,CLabel lblName, String mapName){
        if(code.length() != 0){
            HashMap map = new HashMap();
            map.put(CommonConstants.MAP_NAME, mapName);
            HashMap where = new HashMap();
            where.put("BRANCH_CODE", code);
            where.put("BANK_CODE", bankCode);
            map.put(CommonConstants.MAP_WHERE, where);
            where= null;
            lblName.setText(observable.getName(map));
        }
    }
    
    /** Enabling or disabling buttons in panOperations in InstructionsTab **/
    private void setPanOperationsEnable(boolean flag){
        btnInstNew.setEnabled(flag);
        btnInstSave.setEnabled(flag);
        btnInstDelete.setEnabled(flag);
    }
    private void setPanOperationsEnableInst(boolean flag){
        btnParNew.setEnabled(flag);
        btnParSave.setEnabled(flag);
        btnParDelete.setEnabled(flag);
    }
    /** Method to display to alert when the mandatory fields are not filled up **/
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** Method to addup a row to tblInstruction when btnInstSave button is clicked **/
    private void addRow(){
        System.out.println("selectedData...."+selectedData);
        if(selectedData==1){
            System.out.println("selectedRow====="+selectedRow);
            observable.setTableValueAt(selectedRow);
            observable.resetInstructions();
        }
        else{
            /** when clicked on the new button related to tblInstruction **/
            result=-1;
            result = observable.addTblInstructionData();
            System.out.println("resultresult======"+result);
            if(result==0){
                ClientUtil.enableDisable(panStdInstructions,true);
                btnInstSave.setEnabled(true);
                btnInstDelete.setEnabled(false);
            }
            if(result==1){
                observable.resetInstructions();
                ClientUtil.enableDisable(panStdInstructions,false);
                btnInstSave.setEnabled(false);
                btnInstDelete.setEnabled(false);
            }
            if (result == 2){
                /** The action taken for the Cancel option **/
                ClientUtil.enableDisable(panStdInstructions,true);
                btnInstSave.setEnabled(true);
                btnInstDelete.setEnabled(false);
            }
            if(result==-1){
                observable.resetInstructions();
                ClientUtil.enableDisable(panInstEntry,false);
                btnInstNew.setEnabled(true);
            }
            
        }
    }
    
    private void addRowInstr(){
        if(selectedDataInst==1){
            observable.setTableValueAtInstr(selectedRowInst);
            observable.resetInstr();
        }
        else{
            /** when clicked on the new button related to tblInstruction **/
            resultInstr=-1;
            resultInstr = observable.addTblInstrData();
            if(resultInstr==0){
                ClientUtil.enableDisable(panOperations3,true);
                btnParSave.setEnabled(true);
                btnParDelete.setEnabled(false);
            }
            if(resultInstr==1){
                observable.resetInstr();
                ClientUtil.enableDisable(panOperations3,false);
                btnParSave.setEnabled(false);
                btnParDelete.setEnabled(false);
            }
            if (resultInstr == 2){
                /** The action taken for the Cancel option **/
                ClientUtil.enableDisable(panOperations3,true);
                btnParSave.setEnabled(true);
                btnParDelete.setEnabled(false);
            }
            if(result==-1){
                observable.resetInstr();
                //                ClientUtil.enableDisable(panInstEntry,false);
                btnParNew.setEnabled(true);
            }
            
        }
    }
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        System.out.println("aaaakkkkkkk");
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            //validation
         
            if((observable.getSubRegType().equals("CPD")) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) &&
            (lblOperatesLikeValue.getText().equals("OUTWARD"))){
                System.out.println("New..................");
                String[] obj ={"Yes","No"};
                String displayDetailsStr = "";
                double margin = CommonUtil.convertObjToDouble(observable.getTxtRateForDelay1()).doubleValue();
                long intDays = observable.calcIntDays();
                double cheqAmt = CommonUtil.convertObjToDouble(observable.getTxtInstrumentAmount()).doubleValue();
                displayDetailsStr += "   Cheque Amount : "+cheqAmt+"\n";
                double marginAmt = 0.0;
                double rateOfInt = 0.0;
                if(margin > 0){
                    displayDetailsStr += "   Margin % : "+margin+"\n";
                    marginAmt = (cheqAmt * margin)/100;
                    marginAmt = observable.roundInterest(marginAmt);
                    displayDetailsStr += "   Margin Amount : "+marginAmt+"\n";
                    
                }
                double intAmt = 0.0;
                if(intDays > 0){
                    
                    HashMap getInterestMap = new HashMap();
                    //                        System.out.println("#####period : "+matperiod);
                    getInterestMap.put("LIMIT", String.valueOf(cheqAmt-marginAmt));
                    getInterestMap.put("CATEGORY", observable.getCbmCustCategory().getKeyForSelected());
                    getInterestMap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
                    //                     getInterestMap.put("PRODUCT_TYPE","AD");
                    getInterestMap.put("DEPOSIT_DT", currDt);
                    getInterestMap.put("ACT_NUM", observable.getTxtAccountNum());
                    HashMap intMap = new HashMap();
                    intMap = observable.getInterestDetails(getInterestMap);
                    getInterestMap = null;
                    System.out.println("###intMap : "+intMap);
                    rateOfInt = CommonUtil.convertObjToDouble(intMap.get("INTEREST")).doubleValue();
                    intMap = null;
                    double yearDays = 36500;
                    System.out.println("(cheqAmt-marginAmt)"+(cheqAmt-marginAmt));
                    intAmt = ((cheqAmt-marginAmt) * rateOfInt * intDays) / yearDays;
                    System.out.println("##BEFOREintAmt : "+intAmt);
                    //                     intAmt = (double) observable.getNearest((long)(intAmt, 100),100)/100;
                    intAmt = observable.roundInterest(intAmt);
                    displayDetailsStr += "   Rate Of Int % : "+rateOfInt+"\n";
                    displayDetailsStr += "   No. Of Int Days : "+intDays+"\n";
                    displayDetailsStr += "   Interest Amount : "+intAmt+"\n";
                    System.out.println("###intAmt : "+intAmt);
                }
                double otherBnkChr = observable.calcOtherCharges();
                System.out.println("###BEfotherBnkChr : "+otherBnkChr);
                double charges = CommonUtil.convertObjToDouble(txtTotalAmt.getText()).doubleValue();
                double serTax = CommonUtil.convertObjToDouble(txtTotalServTax.getText()).doubleValue();
                double custCredit = ((cheqAmt)-(marginAmt+intAmt+charges+serTax));
                System.out.println("###BEfcustCredit : "+custCredit);
                custCredit = observable.roundInterest(custCredit);
                System.out.println("###custCredit : "+custCredit);
                
                if((charges+serTax) > 0){
                    displayDetailsStr += "   Bank Charges + Service Tax : "+((charges+serTax)-(otherBnkChr))+"\n";
                }
                if((otherBnkChr) > 0){
                    displayDetailsStr += "   Other Bank Charges + Service Tax : "+(otherBnkChr)+"\n";
                }
                displayDetailsStr += "   Amt to be debited from "+observable.getTxtAccountNum()+" : "+((cheqAmt)-(marginAmt+otherBnkChr))+"\n";
                displayDetailsStr += "   Amt to be credited to "+observable.getTxtAccountNo()+" : "+custCredit+"\n";
                int options =COptionPane.showOptionDialog(null,(displayDetailsStr+"\n"+" Select Yes to Continue Or No to Cancel"), ("Lodge Transaction Details"),
                COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
                if(options == 0){
                    System.out.println("doSave11111"+doSave);
                    doSave = true;
                    //                     observable.clearData();
                }else{
                    System.out.println("doSave222222"+doSave);
                    doSave = false;
                    //                     observable.clearData();
                }
                //
                
            }
            //txtAccountNo1FocusLost(null); // Commened for testing duplication issue
            if(getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(getSelectedBranchID())){
                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(getSelectedBranchID());
                Date currentDate = currDt;
                System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                if(selectedBranchDt == null){
                    ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    //txtAccNo.setText("");
                    return;
                }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){
                    ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    //txtAccNo.setText("");
                    return;
                }else {
                    System.out.println("Continue for interbranch trasactions ...");
                }
            }
            //            final String mandatoryMessage = checkMandatory(panLodgementBIlls);
            //
            //            StringBuffer message = new StringBuffer(mandatoryMessage);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && lblOperatesLikeValue.getText().equals("INWARD")){
                String instPrefix=txtInstPrefix.getText();
                String instrumentNo=txtInstrumentNo.getText();
                String instrumentDate=tdtInstrumentDate.getDateValue();
                if(instPrefix==null || instPrefix.equals("")){
                    displayAlert("Enter Instrument Prefix!!!");
                    return;
                }
                if(instrumentNo==null || instrumentNo.equals("")){
                    displayAlert("Enter Instrument No!!!");
                    return;
                }
                if(instrumentDate==null || instrumentDate.equals("")){
                    displayAlert("Enter Instrument Date!!!");
                    return;
                }
            }
            String mandatoryMessage = "" ;
            mandatoryMessage+=validateSelection(); 
            StringBuffer message = new StringBuffer(mandatoryMessage);
            
            if(message.length() > 0 ){
                displayAlert(message.toString());
            }           
            else if(doSave){
                if(cboBillTenor.getSelectedIndex()!=0){
                    if(!ClientUtil.validPeriodMaxLength(txtBillTenor, CommonUtil.convertObjToStr(observable.getCbmBillTenor().getKeyForSelected()))){
                        String alertMsg = "tenorMsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                boolean isTotalAmountExceedFlag = validateTotalBillAmountExceeds();
                if (isTotalAmountExceedFlag) {
                    ClientUtil.showMessageWindow("The entered amount is greater than total Bill amount :" + CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()));
                    return;
                }
                boolean isTotalLessAmountFlag = validateTotalBillAmountLess();
                if (isTotalLessAmountFlag) {
                    ClientUtil.showMessageWindow("The entered amount is less than total Bill amount :" + CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()));
                    return;
                }
                
                observable.populateInstructionList();
                observable.populateInstr();
                
                String productType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
                String billActivity=((ComboBoxModel)cboActivities.getModel()).getKeyForSelected().toString();
                if(billActivity.equalsIgnoreCase("REALIZE")&& productType.equals("TL") )
                {
                    HashMap transaction=new HashMap();
                     transaction.put("ALL_AMOUNT",transDetails.getTermLoanCloseCharge());
                      transaction.putAll(checkLoanDebit(productType));
                      observable.setTransaction(transaction);
                }
                if(billActivity.equalsIgnoreCase("REALIZE")){
                    boolean actNoFlag = validateOtherBankCharges();
                    if(actNoFlag){                        
                        //mandatoryMessage = mandatoryMessage + "Please enter other bank charges for all accounts";
                        displayAlert("Please enter other bank charges for all accounts" );
                       
                    }else{
                        observable.execute(status);
                    }
                 }else{
                    observable.execute(status);
                    System.out.println("executng here");
                }
        
                if(observable.getActionType()!=ClientConstants.ACTIONTYPE_FAILED){
                    System.out.println("jjjjjjjjjooooooooooo");
                    //Added By Suresh
                    String activity = observable.getCboActivities();
                    if((activity.equalsIgnoreCase("REALIZE")|| activity.equalsIgnoreCase("DISHONOUR")) && observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        HashMap transTypeMap = new HashMap();
                        HashMap transMap = new HashMap();
                        HashMap transCashMap = new HashMap();
                         String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
                         if(prodType!=null && prodType.equals("TL"))
                         {
                              transCashMap.put("BATCH_ID",txtAccountNo1.getText());
                         }
                         else
                         {
                        transCashMap.put("BATCH_ID",txtLodgementId.getText());
                         }
                        transCashMap.put("TRANS_DT", currDt);
                        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                        HashMap transIdMap = new HashMap();
                        List lst = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                        if(lst !=null && lst.size()>0){
                            for(int i = 0;i<lst.size();i++){
                                transMap = (HashMap)lst.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                            }
                        }
                        lst = ClientUtil.executeQuery("getCashDetails", transCashMap);
                        if(lst !=null && lst.size()>0){
                            for(int i = 0;i<lst.size();i++){
                                transMap = (HashMap)lst.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                                transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
                            }
                        }
                        System.out.println("ssssssssssss====="+lst.size());
                        editModeTransDetail(null); 
                        if(transIdMap !=null && transIdMap.size()>0){
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                        System.out.println("#$#$$ yesNo : "+yesNo);
                        System.out.println("#$#$$##### transIdMap : "+transIdMap);
                        if (yesNo==0) {
                            TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransDt", currDt);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            Object keys[] = transIdMap.keySet().toArray();
                            for (int i=0; i<keys.length; i++) {
                                paramMap.put("TransId", keys[i]);
                                ttIntgration.setParam(paramMap);
                                if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                                } else {
                                    ttIntgration.integrationForPrint("CashReceipt", false);
                                }
                            }
                        }
                    }
                    }
                    settings();
                }
                lblCurStatus.setText("");
                txtCreatDt.setText("");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     private HashMap checkLoanDebit(String prodType) {
        HashMap loanDebitType = new HashMap();
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        if (prodType.equals("TL") ) {
            String[] debitType = {"Debit Interest", "DebitPrinciple", "Debit_Penal_Int", "Other_Charges"};
                loanDebitType.put("DEBIT_LOAN_TYPE", "DI");
                loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
                loanDebitType.put("DEBIT_LOAN_TYPE", "DPI");
                loanDebitType.put("DEBIT_LOAN_TYPE", "OTHERCHARGES");
            if (prodType.equals("TL") || prodType.equals("ATL")) {
                String multiDisburse = transDetails.getIsMultiDisburse();
                HashMap closeCharge = transDetails.getTermLoanCloseCharge();
                double clearBalance = CommonUtil.convertObjToDouble(closeCharge.get("CLEAR_BALANCE")).doubleValue();
            }
    
        }
        return loanDebitType;
    }
    private String validateSelection(){

        MandatoryCheck objMandatory = new MandatoryCheck();
        LodgementBillsHashMap objMap = new LodgementBillsHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
        String billActivity = ((ComboBoxModel) cboActivities.getModel()).getKeyForSelected().toString();
        String operatesLike = CommonUtil.convertObjToStr(lblOperatesLikeValue.getText());
        System.out.println("billActivity : " + billActivity + " ......... billActivity = " + billActivity);
        String mandatoryMessage = "";

        if (billActivity.equals("REALIZE") && operatesLike.equals("OUTWARD")) {            
            //mandatoryMap.put("txtRemitFavour", new Boolean(true));
            //mandatoryMessage = mandatoryMessage + objMandatory.checkMandatory(getClass().getName(), panOtherBnkChrg, mandatoryMap);
        }
        if (operatesLike.equals("OUTWARD")) {
            //mandatoryMessage = objMandatory.checkMandatory(getClass().getName(), panLodgementBIlls, mandatoryMap);
        }
        if (cboReceivedFrom.getSelectedItem().equals("Other Bank")) {
            mandatoryMap.put("txtDraweeNo", new Boolean(true));
            mandatoryMap.put("txtDraweeBankCode", new Boolean(true));
            mandatoryMap.put("txtAccountHeadValue", new Boolean(true));
            mandatoryMap.put("txtDraweeBankNameVal", new Boolean(true));
            mandatoryMap.put("txtDraweeBranchCode", new Boolean(true));
            mandatoryMap.put("txtDraweeName", new Boolean(true));
            mandatoryMap.put("txtBankCode", new Boolean(true));
            mandatoryMap.put("txtOtherBranchCode", new Boolean(true));
            mandatoryMessage = mandatoryMessage + objMandatory.checkMandatory(getClass().getName(), panDraweeDetail, mandatoryMap);
        }
        if (cboReceivedFrom.getSelectedItem().equals("Branch")) {
            mandatoryMap.put("txtBranchCode", new Boolean(true));           
            mandatoryMessage = mandatoryMessage + objMandatory.checkMandatory(getClass().getName(), panBranchCode, mandatoryMap);            
        }
        System.out.println("mandatoryMessage : " + mandatoryMessage);
        return mandatoryMessage;

    }
    
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.removeTbmInstructionsRow();
        observable.removeTbmInstrRow();
        observable.removeTbmActRow();
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        setBranchBtnEnableDisable(false);
        setOtherBranchBankBtnEnableDisable(false);
        resetLabels();
        btnInstNew.setEnabled(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panLodgementBIlls, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        transDetails.setTransDetails(null,null,null);
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
    
    /** Method used to empty Label fields in the ui **/
    private void resetLabels(){
        lblBranchCodeValue.setText("");
        lblBankNameValue.setText("");
        lblOtherBranchNameValue.setText("");
        lblCustName.setText("");
        lblOperatesLikeValue.setText("");
    }
    
    /** Makes the panels panChequeDetails, panBillofExchange, panDocumentDetails enable or disable
     *according to the cboInstrumentType selected **/
    private void setPanEnableDisable(){
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            if((observable.getCbmInstrumentType().getKeyForSelected().equals("CHEQUE")) ||
            (observable.getCbmInstrumentType().getKeyForSelected().equals("PAYORDER")) ||
            (observable.getCbmInstrumentType().getKeyForSelected().equals("DEMAND DRAFT"))){
                ClientUtil.enableDisable(panBillofExchange,false);
                ClientUtil.enableDisable(panDocumentDetails,false);
                ClientUtil.enableDisable(panChequeDetails,true);
                ClientUtil.clearAll(panBillofExchange);
                cboInstrumentType.setEnabled(true);
            }else if(observable.getCbmInstrumentType().getKeyForSelected().equals("HUNDI")){
                ClientUtil.enableDisable(panChequeDetails,false);
                ClientUtil.enableDisable(panBillofExchange,true);
                ClientUtil.enableDisable(panDocumentDetails,true);
                ClientUtil.clearAll(panChequeDetails);
                cboInstrumentType.setEnabled(true);
            }
        }
    }
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getMultiLodgementMasterAuthorizeList");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLodgementMaster");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAuthorize.setEnabled(true);
        } else if (viewType.equals(AUTHORIZE)){
            //            HashMap singleAuthorizeMap = new HashMap();
            String chkAuth="";
            HashMap mapChkAuth = new HashMap();
            String billStatus=cboActivities.getSelectedItem().toString();
            System.out.println("billStatus====="+billStatus);
            mapChkAuth.put("LOD_ID",txtLodgementId.getText());
           // cboActivities.get
            if(billStatus.equals("Lodgement")){
                List lstChkAuth = ClientUtil.executeQuery("getSelLodMatAuth", mapChkAuth);
                if(null != lstChkAuth && lstChkAuth.size()>0){
                    mapChkAuth= new HashMap();
                    mapChkAuth=(HashMap)lstChkAuth.get(0);
                    chkAuth=CommonUtil.convertObjToStr(mapChkAuth.get("AUTHORIZE_STATUS"));
                    System.out.println("chkAuth===="+chkAuth);
                }
            }
            if(billStatus.equals("Realize")){
                mapChkAuth.put("REALIZE","REALIZE");
                List lstChkAuth= ClientUtil.executeQuery("getSelTranRealAuth", mapChkAuth);
                if(null != lstChkAuth && lstChkAuth.size()>0){
                    mapChkAuth= new HashMap();
                    mapChkAuth=(HashMap)lstChkAuth.get(0);
                    chkAuth=CommonUtil.convertObjToStr(mapChkAuth.get("AUTHORIZE_STATUS"));
                    System.out.println("chkAuth===="+chkAuth);    
                }
            }
            if(billStatus.equals("Dishonour")) {
                mapChkAuth.put("REALIZE","DISHONOUR");
                List lstChkAuth= ClientUtil.executeQuery("getSelTranRealAuth", mapChkAuth);
                if(null != lstChkAuth && lstChkAuth.size()>0){
                    mapChkAuth= new HashMap();
                    mapChkAuth=(HashMap)lstChkAuth.get(0);
                    chkAuth=CommonUtil.convertObjToStr(mapChkAuth.get("AUTHORIZE_STATUS"));
                    System.out.println("chkAuth===="+chkAuth);    
                }
            }
        
            if(chkAuth != null && !chkAuth.isEmpty() && chkAuth.length()>0 && (chkAuth.equals("AUTHORIZED") || chkAuth.equals("REJECTED"))){
                ClientUtil.showAlertWindow("Already authorized");
                chkAuth="";
                observable.resetForm();
                observable.setResultStatus();
             }else{
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
                singleAuthorizeMap.put(LODGEMENT_ID, txtLodgementId.getText());
                singleAuthorizeMap.put("BILL_STATUS", lblCurStatus.getText());
                //            String strWarnMsg = tabLodgementBils.isAllTabsVisited();
                //            if (strWarnMsg.length() > 0){
                //            displayAlert(strWarnMsg);
                //            return;
                //            }
                //           strWarnMsg = null;
                //           tabLodgementBils.resetVisits();
                //           ClientUtil.execute("authorizeLodgementMaster", singleAuthorizeMap);
                //            singleAuthorizeMap.put("STATUS", authorizeStatus);
                //            singleAuthorizeMap.put("LODGE_ID", LodgeID);
                System.out.println("singleAuthorizeMap^^^^"+singleAuthorizeMap);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap);
                if(observable.getProxyReturnMap()!=null){
                    System.out.println("getProxyReturnMap######"+observable.getProxyReturnMap());
                    //reMap=observable.getProxyReturnMap();
                    //reMap1.put()
                   // String currStatus =lblCurStatus.getText();
                   // if(currStatus!=null && !currStatus.equals("LODGEMENT")){
                        displayTransDetail(observable.getProxyReturnMap()); 
                    //}
                }
                viewType = "";
                btnCancelActionPerformed(null);
               chkAuth="";
               btnCancel.setEnabled(true);
            }        
        }
    }
 public void  displayTransDetail(HashMap returnMap){
        System.out.println("returnMap##########"+returnMap);
//        String transId = CommonUtil.convertObjToStr(returnMap.get("SINGLE_TRANS_ID"));
//        String transType = "";
//      //  System.out.println("jhj>>>>>>>");
//        Object keys[] = returnMap.keySet().toArray();
//        System.out.println("jhj>>>>>>>adad");
//        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
//        int cashCount = 0;
//        int transferCount = 0;
//        List tempList = null;
//        HashMap transMap = null;
//        String actNum = "";
//        HashMap transIdMap = new HashMap();
//        HashMap transTypeMap = new HashMap();
//        int yesNo = 0;
//        String[] options = {"Yes", "No"};
//        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
//        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//        null, options, options[0]);
//        System.out.println("#$#$$ yesNo : "+yesNo);
//        if (yesNo==0) {
//            TTIntegration ttIntgration = null;
//            HashMap paramMap = new HashMap();
//            paramMap.put("TransId", transId);
//            paramMap.put("TransDt", currDt.clone());
//            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
//            ttIntgration.setParam(paramMap);
//            if(CommonUtil.convertObjToStr(returnMap.get("SINGLE_TRANS_ID"))!= null) {
//                    ttIntgration.integrationForPrint("ReceiptPayment");
//           }
//       }
        HashMap transMap = new HashMap();
        HashMap transIdMap = new HashMap();
        HashMap transCashMap = new HashMap();
        transCashMap.put("BATCH_ID", returnMap.get("LODGEMENT_ID"));
        transCashMap.put("TRANS_DT", currDt.clone());
        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//                    transCashMap.put("AUTHORIZE", "AUTHORIZED");
        List lst = ClientUtil.executeQuery("getTransferDetails", transCashMap);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                transMap = (HashMap) lst.get(i);
                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
            }
        }
        HashMap authMap = new HashMap();
        authMap.put("LINK_BATCH_ID",returnMap.get("LODGEMENT_ID"));
//        authMap.put("AUTHORIZE", "AUTHORIZED");
        editModeTransDetail(authMap);
        if (transIdMap != null && transIdMap.size() > 0) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("TransDt", currDt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                Object keys[] = transIdMap.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    paramMap.put("TransId", keys[i]);
                    ttIntgration.setParam(paramMap);
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    }
                }
            }
        }
    }
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            //            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                map.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
            }
            observable.set_authorizeMap(map);
            String action=CommonConstants.STATUS_AUTHORIZED;
            observable.execute(action);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                //            super.setOpenForEditBy(observable.getStatusBy());
                //            super.removeEditLock(txtVariableNo.getText());
                String billStatus = cboActivities.getSelectedItem().toString();
                String billType = CommonUtil.convertObjToStr(((ComboBoxModel) cboBillsType.getModel()).getKeyForSelected());
                boolean flag = false;
                if (billType != null && !billType.equals("")) {
                    flag = observable.getBillsProduct(billType);
                    if (flag) {
                        printIfContraHeadExsist();
                    }
                }
                if(billStatus.equals("Lodgement")) {
                    HashMap transMap = new HashMap();
                    HashMap transCashMap = new HashMap();
                    String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType1.getModel()).getKeyForSelected());
                    if (prodType != null && prodType.equals("TL")) {
                        transCashMap.put("BATCH_ID", txtAccountNo1.getText());
                    } else {
                        transCashMap.put("BATCH_ID", txtLodgementId.getText());
                    }
                    transCashMap.put("TRANS_DT", currDt.clone());
                    transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    transCashMap.put("AUTHORIZE", "AUTHORIZED");
                    HashMap transIdMap = new HashMap();
                    List lst = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            transMap = (HashMap) lst.get(i);
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                        }
                    }
                    HashMap authMap = new HashMap();
                    authMap.put("AUTHORIZE", "AUTHORIZED");
                    editModeTransDetail(authMap);
                    if (transIdMap != null && transIdMap.size() > 0) {
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        if (yesNo == 0) {
                            TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransDt", currDt);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            Object keys[] = transIdMap.keySet().toArray();
                            for (int i = 0; i < keys.length; i++) {
                                paramMap.put("TransId", keys[i]);
                                ttIntgration.setParam(paramMap);
                                if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                }
                            }
                        }
                    }
                }
                observable.resetForm();
                observable.setResultStatus();
               
            }
            //            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        }
        if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Bills Lodgement");
                }
                if (fromCashierAuthorizeUI) {
                    CashierauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    CashierauthorizeListUI.setFocusToTable();
                } 
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                }
                 btnCancelActionPerformed(null);
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoBillAcceptance = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDraweeHundi = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIntICC = new com.see.truetransact.uicomponent.CButtonGroup();
        panLodgementBIlls = new com.see.truetransact.uicomponent.CPanel();
        tabLodgementBils = new com.see.truetransact.uicomponent.CTabbedPane();
        panLodgementDetails = new com.see.truetransact.uicomponent.CPanel();
        panBillsType = new com.see.truetransact.uicomponent.CPanel();
        lblBillsType = new com.see.truetransact.uicomponent.CLabel();
        lblOperatesLike = new com.see.truetransact.uicomponent.CLabel();
        lblReceivedFrom = new com.see.truetransact.uicomponent.CLabel();
        lblOperatesLikeValue = new com.see.truetransact.uicomponent.CLabel();
        cboReceivedFrom = new com.see.truetransact.uicomponent.CComboBox();
        cboBillsType = new com.see.truetransact.uicomponent.CComboBox();
        lblLodgementId = new com.see.truetransact.uicomponent.CLabel();
        txtLodgementId = new com.see.truetransact.uicomponent.CTextField();
        cboActivities = new com.see.truetransact.uicomponent.CComboBox();
        lblActivities = new com.see.truetransact.uicomponent.CLabel();
        cboCustCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblCustCategory = new com.see.truetransact.uicomponent.CLabel();
        cboTranstype = new com.see.truetransact.uicomponent.CComboBox();
        lblTranstype = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills8 = new com.see.truetransact.uicomponent.CPanel();
        txtRateForDelay = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per1 = new com.see.truetransact.uicomponent.CLabel();
        lblRateForDelay = new com.see.truetransact.uicomponent.CLabel();
        lblCurStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblCurStatus = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNum = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo1 = new com.see.truetransact.uicomponent.CPanel();
        btnAccountNum = new com.see.truetransact.uicomponent.CButton();
        txtAccountNum = new com.see.truetransact.uicomponent.CTextField();
        lblCreatDt = new com.see.truetransact.uicomponent.CLabel();
        txtCreatDt = new com.see.truetransact.uicomponent.CTextField();
        panBranchBankDetails = new com.see.truetransact.uicomponent.CPanel();
        panBranchDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        panBranchCode = new com.see.truetransact.uicomponent.CPanel();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCodeValue = new com.see.truetransact.uicomponent.CLabel();
        panOtherBankDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        panBankCode = new com.see.truetransact.uicomponent.CPanel();
        txtBankCode = new com.see.truetransact.uicomponent.CTextField();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblOtherBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameValue = new com.see.truetransact.uicomponent.CLabel();
        panOtherBranchCode = new com.see.truetransact.uicomponent.CPanel();
        txtOtherBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnOtherBranchCode = new com.see.truetransact.uicomponent.CButton();
        lblOtherBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblOtherBranchNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtAccountHeadValue = new com.see.truetransact.uicomponent.CTextField();
        lblRemittedDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRemittedDt = new com.see.truetransact.uicomponent.CDateField();
        lblActRefNo = new com.see.truetransact.uicomponent.CLabel();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        lblOtherName = new com.see.truetransact.uicomponent.CLabel();
        txtOtherName = new com.see.truetransact.uicomponent.CTextField();
        lblOtherAddress = new com.see.truetransact.uicomponent.CLabel();
        txtOtherAddress = new com.see.truetransact.uicomponent.CTextField();
        lblOtherCity = new com.see.truetransact.uicomponent.CLabel();
        cboOtherCity = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboOtherState = new com.see.truetransact.uicomponent.CComboBox();
        lblOtherCountry = new com.see.truetransact.uicomponent.CLabel();
        cboOtherCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblOtherPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtOtherPinCode = new com.see.truetransact.uicomponent.CTextField();
        panRemitList = new com.see.truetransact.uicomponent.CPanel();
        lblRemitCity = new com.see.truetransact.uicomponent.CLabel();
        lblInstNo = new com.see.truetransact.uicomponent.CLabel();
        cboRemitBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        cboRemitCity = new com.see.truetransact.uicomponent.CComboBox();
        cboRemitProdID = new com.see.truetransact.uicomponent.CComboBox();
        lblRemitProdID = new com.see.truetransact.uicomponent.CLabel();
        lblRemitDraweeBank = new com.see.truetransact.uicomponent.CLabel();
        cboRemitDraweeBank = new com.see.truetransact.uicomponent.CComboBox();
        panBillTenor1 = new com.see.truetransact.uicomponent.CPanel();
        txtInst1 = new com.see.truetransact.uicomponent.CTextField();
        txtInst2 = new com.see.truetransact.uicomponent.CTextField();
        lblRemitBranchCode = new com.see.truetransact.uicomponent.CLabel();
        tdtRemitInstDate = new com.see.truetransact.uicomponent.CDateField();
        lblRemitInst = new com.see.truetransact.uicomponent.CLabel();
        txtRemitFavour1 = new com.see.truetransact.uicomponent.CTextField();
        lblRemitFavour1 = new com.see.truetransact.uicomponent.CLabel();
        lblDrawBnk = new com.see.truetransact.uicomponent.CLabel();
        lblDrawBran = new com.see.truetransact.uicomponent.CLabel();
        chkClering = new com.see.truetransact.uicomponent.CCheckBox();
        panDraweeDetail = new com.see.truetransact.uicomponent.CPanel();
        lblDraweeName = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeName = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeAddress = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeAddress = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeCity = new com.see.truetransact.uicomponent.CLabel();
        cboDraweeCity = new com.see.truetransact.uicomponent.CComboBox();
        lblDraweeState = new com.see.truetransact.uicomponent.CLabel();
        cboDraweeState = new com.see.truetransact.uicomponent.CComboBox();
        lblDraweeCountry = new com.see.truetransact.uicomponent.CLabel();
        cboDraweeCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblDraweePinCode = new com.see.truetransact.uicomponent.CLabel();
        txtDraweePinCode = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeNo = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeNo = new com.see.truetransact.uicomponent.CTextField();
        lblSendingTo = new com.see.truetransact.uicomponent.CLabel();
        txtSendingTo = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeBankCode = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeBankCode = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeBranchCode = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnDraweeBranchName = new com.see.truetransact.uicomponent.CButton();
        txtDraweeBankNameVal = new com.see.truetransact.uicomponent.CTextField();
        lblDraweeBankNamee = new com.see.truetransact.uicomponent.CLabel();
        btnDraweeBankCode = new com.see.truetransact.uicomponent.CButton();
        sptDraweeDetails = new com.see.truetransact.uicomponent.CSeparator();
        panNxtAccNo = new com.see.truetransact.uicomponent.CPanel();
        lblNxtAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        panChequeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDraweeBankName = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentAmount = new com.see.truetransact.uicomponent.CLabel();
        lblMICR = new com.see.truetransact.uicomponent.CLabel();
        lblPayeeName = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeBankName = new com.see.truetransact.uicomponent.CTextField();
        txtDraweeBranchName = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentAmount = new com.see.truetransact.uicomponent.CTextField();
        txtMICR = new com.see.truetransact.uicomponent.CTextField();
        txtPayeeName = new com.see.truetransact.uicomponent.CTextField();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        panInstrumentType = new com.see.truetransact.uicomponent.CPanel();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        txtInstPrefix = new com.see.truetransact.uicomponent.CTextField();
        panInstrumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panBillofExchange = new com.see.truetransact.uicomponent.CPanel();
        lblBillTenor = new com.see.truetransact.uicomponent.CLabel();
        panBillTenor = new com.see.truetransact.uicomponent.CPanel();
        txtBillTenor = new com.see.truetransact.uicomponent.CTextField();
        cboBillTenor = new com.see.truetransact.uicomponent.CComboBox();
        lblDueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDueDate = new com.see.truetransact.uicomponent.CDateField();
        lblAcceptanceDate = new com.see.truetransact.uicomponent.CLabel();
        tdtAcceptanceDate = new com.see.truetransact.uicomponent.CDateField();
        lblHundiNo = new com.see.truetransact.uicomponent.CLabel();
        txtHundiNo = new com.see.truetransact.uicomponent.CTextField();
        lblBillAcceptance = new com.see.truetransact.uicomponent.CLabel();
        panAcceptanceBill = new com.see.truetransact.uicomponent.CPanel();
        rdoBillAcceptance_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBillAcceptance_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHundiDate = new com.see.truetransact.uicomponent.CLabel();
        tdtHundiDate = new com.see.truetransact.uicomponent.CDateField();
        lblDraweeHundi = new com.see.truetransact.uicomponent.CLabel();
        panDraweeHundi = new com.see.truetransact.uicomponent.CPanel();
        rdoDraweeHundi_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDraweeHundi_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHundiAmount = new com.see.truetransact.uicomponent.CLabel();
        txtHundiAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPayable = new com.see.truetransact.uicomponent.CLabel();
        txtPayable = new com.see.truetransact.uicomponent.CTextField();
        lblHundiRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtHundiRemarks = new com.see.truetransact.uicomponent.CTextField();
        panDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panInvoiceDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInvoiceNumber = new com.see.truetransact.uicomponent.CLabel();
        lblInvoiceDate = new com.see.truetransact.uicomponent.CLabel();
        lblInvoiceAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInvoiceNumber = new com.see.truetransact.uicomponent.CTextField();
        tdtInvoiceDate = new com.see.truetransact.uicomponent.CDateField();
        txtInvoiceAmount = new com.see.truetransact.uicomponent.CTextField();
        panRRLRARPPDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransportCompany = new com.see.truetransact.uicomponent.CLabel();
        lblRRLRNumber = new com.see.truetransact.uicomponent.CLabel();
        lblRRLRDate = new com.see.truetransact.uicomponent.CLabel();
        lblGoodsValue = new com.see.truetransact.uicomponent.CLabel();
        lblGoodsAssigned = new com.see.truetransact.uicomponent.CLabel();
        txtTransportCompany = new com.see.truetransact.uicomponent.CTextField();
        tdtRRLRDate = new com.see.truetransact.uicomponent.CDateField();
        txtRRLRNumber = new com.see.truetransact.uicomponent.CTextField();
        txtGoodsValue = new com.see.truetransact.uicomponent.CTextField();
        txtGoodsAssigned = new com.see.truetransact.uicomponent.CTextField();
        panInstructions1 = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panInstructions2 = new com.see.truetransact.uicomponent.CPanel();
        panInstEntry1 = new com.see.truetransact.uicomponent.CPanel();
        panStdInstructions1 = new com.see.truetransact.uicomponent.CPanel();
        srpInstructions1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstruction1 = new com.see.truetransact.uicomponent.CTable();
        panOperations3 = new com.see.truetransact.uicomponent.CPanel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblInstruction = new com.see.truetransact.uicomponent.CLabel();
        cboInstruction = new com.see.truetransact.uicomponent.CComboBox();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panParticulars = new com.see.truetransact.uicomponent.CPanel();
        btnParNew = new com.see.truetransact.uicomponent.CButton();
        btnParSave = new com.see.truetransact.uicomponent.CButton();
        btnParDelete = new com.see.truetransact.uicomponent.CButton();
        panTblInstruction1 = new com.see.truetransact.uicomponent.CPanel();
        panRate = new com.see.truetransact.uicomponent.CPanel();
        panLableValues = new com.see.truetransact.uicomponent.CPanel();
        panIntICC3 = new com.see.truetransact.uicomponent.CPanel();
        panRRLRARPPDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills9 = new com.see.truetransact.uicomponent.CPanel();
        txtRateForDelay1 = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per2 = new com.see.truetransact.uicomponent.CLabel();
        lblRateForDelay1 = new com.see.truetransact.uicomponent.CLabel();
        panIntDays = new com.see.truetransact.uicomponent.CPanel();
        txtIntDays = new com.see.truetransact.uicomponent.CTextField();
        cboIntDays = new com.see.truetransact.uicomponent.CComboBox();
        lblIntDays = new com.see.truetransact.uicomponent.CLabel();
        panDiscountRate = new com.see.truetransact.uicomponent.CPanel();
        txtDiscountRateBills = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountRateBills_Per = new com.see.truetransact.uicomponent.CLabel();
        lblDiscountRateOfBD = new com.see.truetransact.uicomponent.CLabel();
        lblOverdueInterestForBD = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills1 = new com.see.truetransact.uicomponent.CPanel();
        txtOverdueRateBills = new com.see.truetransact.uicomponent.CTextField();
        lblOverdueRateBills_Per = new com.see.truetransact.uicomponent.CLabel();
        lblOverdueRateCBP = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills2 = new com.see.truetransact.uicomponent.CPanel();
        txtRateForCBP = new com.see.truetransact.uicomponent.CTextField();
        lblRateForCBP_Per = new com.see.truetransact.uicomponent.CLabel();
        lblAtParLimit = new com.see.truetransact.uicomponent.CLabel();
        txtAtParLimit = new com.see.truetransact.uicomponent.CTextField();
        panOverdueRateBills3 = new com.see.truetransact.uicomponent.CPanel();
        txtCleanBills = new com.see.truetransact.uicomponent.CTextField();
        lblCleanBills_Per = new com.see.truetransact.uicomponent.CLabel();
        lblCleanBillsPurchased = new com.see.truetransact.uicomponent.CLabel();
        panTransitPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtTransitPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboTransitPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblTransitPeriod = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills = new com.see.truetransact.uicomponent.CPanel();
        txtDefaultPostage = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per = new com.see.truetransact.uicomponent.CLabel();
        lblDefaultPostage = new com.see.truetransact.uicomponent.CLabel();
        panIntICC = new com.see.truetransact.uicomponent.CPanel();
        cRadio_ICC_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        cRadio_ICC_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntICC = new com.see.truetransact.uicomponent.CLabel();
        panIntICC2 = new com.see.truetransact.uicomponent.CPanel();
        srpInstructions2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstruction2 = new com.see.truetransact.uicomponent.CTable();
        panInstructions = new com.see.truetransact.uicomponent.CPanel();
        panInstEntry = new com.see.truetransact.uicomponent.CPanel();
        panTblInstruction = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panStdInstructions = new com.see.truetransact.uicomponent.CPanel();
        lblStdInstruction = new com.see.truetransact.uicomponent.CLabel();
        lblAdditionalInstruction = new com.see.truetransact.uicomponent.CLabel();
        cboStdInstruction = new com.see.truetransact.uicomponent.CComboBox();
        txtStdInstruction = new com.see.truetransact.uicomponent.CTextField();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        panOperations1 = new com.see.truetransact.uicomponent.CPanel();
        txtTotalAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalServTax = new com.see.truetransact.uicomponent.CTextField();
        lblTotalServTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CTextField();
        srpInstructions = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstruction = new com.see.truetransact.uicomponent.CTable();
        panOtherBnkChrg = new com.see.truetransact.uicomponent.CPanel();
        lblRemitFavour = new com.see.truetransact.uicomponent.CLabel();
        txtRemitFavour = new com.see.truetransact.uicomponent.CTextField();
        lblRemitAmt = new com.see.truetransact.uicomponent.CLabel();
        txtInstAmt = new com.see.truetransact.uicomponent.CTextField();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblProductType1 = new com.see.truetransact.uicomponent.CLabel();
        cboProductType1 = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId1 = new com.see.truetransact.uicomponent.CLabel();
        cboProductId1 = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtSplitAmnt = new com.see.truetransact.uicomponent.CTextField();
        lblAccountNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNo1 = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo1 = new com.see.truetransact.uicomponent.CButton();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblReference1 = new com.see.truetransact.uicomponent.CLabel();
        txtReference1 = new com.see.truetransact.uicomponent.CTextField();
        panOperations = new com.see.truetransact.uicomponent.CPanel();
        btnInstNew = new com.see.truetransact.uicomponent.CButton();
        btnInstSave = new com.see.truetransact.uicomponent.CButton();
        btnInstDelete = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        tbrLodgmentBills = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrLodgemntBills = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(825, 670));
        setMinimumSize(new java.awt.Dimension(825, 670));
        setPreferredSize(new java.awt.Dimension(825, 690));

        tabLodgementBils.setMinimumSize(new java.awt.Dimension(805, 630));
        tabLodgementBils.setPreferredSize(new java.awt.Dimension(805, 630));

        panLodgementDetails.setMinimumSize(new java.awt.Dimension(620, 630));
        panLodgementDetails.setPreferredSize(new java.awt.Dimension(620, 630));
        panLodgementDetails.setLayout(new java.awt.GridBagLayout());

        panBillsType.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panBillsType.setMaximumSize(new java.awt.Dimension(320, 295));
        panBillsType.setMinimumSize(new java.awt.Dimension(320, 295));
        panBillsType.setPreferredSize(new java.awt.Dimension(320, 295));
        panBillsType.setLayout(new java.awt.GridBagLayout());

        lblBillsType.setText("Bills Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblBillsType, gridBagConstraints);

        lblOperatesLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblOperatesLike, gridBagConstraints);

        lblReceivedFrom.setText("Received From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblReceivedFrom, gridBagConstraints);

        lblOperatesLikeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblOperatesLikeValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(lblOperatesLikeValue, gridBagConstraints);

        cboReceivedFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReceivedFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReceivedFromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(cboReceivedFrom, gridBagConstraints);

        cboBillsType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBillsType.setPopupWidth(300);
        cboBillsType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBillsTypeActionPerformed(evt);
            }
        });
        cboBillsType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboBillsTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(cboBillsType, gridBagConstraints);

        lblLodgementId.setText("Lodgement Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblLodgementId, gridBagConstraints);

        txtLodgementId.setEditable(false);
        txtLodgementId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLodgementId.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(txtLodgementId, gridBagConstraints);

        cboActivities.setMinimumSize(new java.awt.Dimension(100, 21));
        cboActivities.setPopupWidth(160);
        cboActivities.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboActivitiesItemStateChanged(evt);
            }
        });
        cboActivities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActivitiesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(cboActivities, gridBagConstraints);

        lblActivities.setText("Activity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblActivities, gridBagConstraints);

        cboCustCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustCategory.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(cboCustCategory, gridBagConstraints);

        lblCustCategory.setText("Customer Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblCustCategory, gridBagConstraints);

        cboTranstype.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTranstype.setPopupWidth(160);
        cboTranstype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTranstypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(cboTranstype, gridBagConstraints);

        lblTranstype.setText("Transtype");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblTranstype, gridBagConstraints);

        panOverdueRateBills8.setLayout(new java.awt.GridBagLayout());

        txtRateForDelay.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateForDelay.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRateForDelay.setValidation(new PercentageValidation());
        panOverdueRateBills8.add(txtRateForDelay, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per1.setText("%");
        panOverdueRateBills8.add(lblDefaultPostage_Per1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(panOverdueRateBills8, gridBagConstraints);

        lblRateForDelay.setText("Rate For Delayed Realization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblRateForDelay, gridBagConstraints);

        lblCurStatus1.setText("Current Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblCurStatus1, gridBagConstraints);

        lblCurStatus.setText("Current Status");
        lblCurStatus.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCurStatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblCurStatusFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(lblCurStatus, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(180);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBillsType.add(cboProductID, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panBillsType.add(lblProductID, gridBagConstraints);

        lblAccountNum.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panBillsType.add(lblAccountNum, gridBagConstraints);

        panAccountNo1.setLayout(new java.awt.GridBagLayout());

        btnAccountNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNum.setEnabled(false);
        btnAccountNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountNo1.add(btnAccountNum, gridBagConstraints);

        txtAccountNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNum.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAccountNo1.add(txtAccountNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(panAccountNo1, gridBagConstraints);

        lblCreatDt.setText("Created Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBillsType.add(lblCreatDt, gridBagConstraints);

        txtCreatDt.setEditable(false);
        txtCreatDt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBillsType.add(txtCreatDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panLodgementDetails.add(panBillsType, gridBagConstraints);

        panBranchBankDetails.setMinimumSize(new java.awt.Dimension(320, 180));
        panBranchBankDetails.setPreferredSize(new java.awt.Dimension(320, 180));
        panBranchBankDetails.setLayout(new java.awt.GridBagLayout());

        panBranchDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Branch Details"));
        panBranchDetails.setMinimumSize(new java.awt.Dimension(320, 180));
        panBranchDetails.setPreferredSize(new java.awt.Dimension(320, 180));
        panBranchDetails.setLayout(new java.awt.GridBagLayout());

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panBranchDetails.add(lblBranchCode, gridBagConstraints);

        panBranchCode.setMinimumSize(new java.awt.Dimension(123, 21));
        panBranchCode.setLayout(new java.awt.GridBagLayout());

        txtBranchCode.setEditable(false);
        txtBranchCode.setAllowAll(true);
        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCode.setValidation(new DefaultValidation());
        txtBranchCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchCodeFocusLost(evt);
            }
        });
        panBranchCode.add(txtBranchCode, new java.awt.GridBagConstraints());

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setEnabled(false);
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBranchCode.add(btnBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panBranchDetails.add(panBranchCode, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 6, 4);
        panBranchDetails.add(lblBranchName, gridBagConstraints);

        lblBranchCodeValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBranchCodeValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBranchCodeValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 6, 1);
        panBranchDetails.add(lblBranchCodeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panBranchBankDetails.add(panBranchDetails, gridBagConstraints);

        panOtherBankDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank Details"));
        panOtherBankDetails.setMinimumSize(new java.awt.Dimension(320, 180));
        panOtherBankDetails.setPreferredSize(new java.awt.Dimension(320, 180));
        panOtherBankDetails.setLayout(new java.awt.GridBagLayout());

        lblBankCode.setText("Other Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblBankCode, gridBagConstraints);

        panBankCode.setLayout(new java.awt.GridBagLayout());

        txtBankCode.setEditable(false);
        txtBankCode.setAllowAll(true);
        txtBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankCode.setValidation(new DefaultValidation());
        txtBankCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankCodeFocusLost(evt);
            }
        });
        panBankCode.add(txtBankCode, new java.awt.GridBagConstraints());

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.setEnabled(false);
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBankCode.add(btnBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(panBankCode, gridBagConstraints);

        lblBankName.setText("Other Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblBankName, gridBagConstraints);

        lblOtherBranchCode.setText("Other Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblOtherBranchCode, gridBagConstraints);

        lblBankNameValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBankNameValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBankNameValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(lblBankNameValue, gridBagConstraints);

        panOtherBranchCode.setLayout(new java.awt.GridBagLayout());

        txtOtherBranchCode.setEditable(false);
        txtOtherBranchCode.setAllowAll(true);
        txtOtherBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherBranchCode.setValidation(new DefaultValidation());
        txtOtherBranchCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOtherBranchCodeFocusLost(evt);
            }
        });
        panOtherBranchCode.add(txtOtherBranchCode, new java.awt.GridBagConstraints());

        btnOtherBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOtherBranchCode.setEnabled(false);
        btnOtherBranchCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOtherBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOtherBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panOtherBranchCode.add(btnOtherBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(panOtherBranchCode, gridBagConstraints);

        lblOtherBranchName.setText("Other Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblOtherBranchName, gridBagConstraints);

        lblOtherBranchNameValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblOtherBranchNameValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(lblOtherBranchNameValue, gridBagConstraints);

        lblAccountHead.setText("A/c Head");
        lblAccountHead.setName("lblAccountHead"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 4);
        panOtherBankDetails.add(lblAccountHead, gridBagConstraints);

        txtAccountHeadValue.setEditable(false);
        txtAccountHeadValue.setAllowAll(true);
        txtAccountHeadValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHeadValue.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 11, 0);
        panOtherBankDetails.add(txtAccountHeadValue, gridBagConstraints);

        lblRemittedDt.setText("RemittedDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherBankDetails.add(lblRemittedDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(tdtRemittedDt, gridBagConstraints);

        lblActRefNo.setMaximumSize(new java.awt.Dimension(100, 16));
        lblActRefNo.setMinimumSize(new java.awt.Dimension(100, 16));
        lblActRefNo.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherBankDetails.add(lblActRefNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panBranchBankDetails.add(panOtherBankDetails, gridBagConstraints);

        panOtherDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Details"));
        panOtherDetails.setMinimumSize(new java.awt.Dimension(320, 180));
        panOtherDetails.setPreferredSize(new java.awt.Dimension(320, 180));
        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        lblOtherName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblOtherName, gridBagConstraints);

        txtOtherName.setAllowAll(true);
        txtOtherName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(txtOtherName, gridBagConstraints);

        lblOtherAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblOtherAddress, gridBagConstraints);

        txtOtherAddress.setAllowAll(true);
        txtOtherAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherAddress.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(txtOtherAddress, gridBagConstraints);

        lblOtherCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblOtherCity, gridBagConstraints);

        cboOtherCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(cboOtherCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblState, gridBagConstraints);

        cboOtherState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(cboOtherState, gridBagConstraints);

        lblOtherCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblOtherCountry, gridBagConstraints);

        cboOtherCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(cboOtherCountry, gridBagConstraints);

        lblOtherPinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOtherDetails.add(lblOtherPinCode, gridBagConstraints);

        txtOtherPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherPinCode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherDetails.add(txtOtherPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panBranchBankDetails.add(panOtherDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panLodgementDetails.add(panBranchBankDetails, gridBagConstraints);

        panRemitList.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRemitList.setMinimumSize(new java.awt.Dimension(250, 295));
        panRemitList.setPreferredSize(new java.awt.Dimension(250, 295));
        panRemitList.setLayout(new java.awt.GridBagLayout());

        lblRemitCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitCity, gridBagConstraints);

        lblInstNo.setText("Inst No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblInstNo, gridBagConstraints);

        cboRemitBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRemitBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRemitBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(cboRemitBranchCode, gridBagConstraints);

        cboRemitCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRemitCity.setPopupWidth(160);
        cboRemitCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRemitCityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(cboRemitCity, gridBagConstraints);

        cboRemitProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRemitProdID.setPopupWidth(160);
        cboRemitProdID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboRemitProdIDItemStateChanged(evt);
            }
        });
        cboRemitProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRemitProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(cboRemitProdID, gridBagConstraints);

        lblRemitProdID.setText("Advice Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitProdID, gridBagConstraints);

        lblRemitDraweeBank.setText("Drawee Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitDraweeBank, gridBagConstraints);

        cboRemitDraweeBank.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRemitDraweeBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRemitDraweeBankActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(cboRemitDraweeBank, gridBagConstraints);

        panBillTenor1.setLayout(new java.awt.GridBagLayout());

        txtInst1.setMinimumSize(new java.awt.Dimension(30, 21));
        txtInst1.setPreferredSize(new java.awt.Dimension(30, 21));
        txtInst1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInst1FocusLost(evt);
            }
        });
        panBillTenor1.add(txtInst1, new java.awt.GridBagConstraints());

        txtInst2.setMinimumSize(new java.awt.Dimension(68, 21));
        txtInst2.setPreferredSize(new java.awt.Dimension(68, 21));
        txtInst2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInst2ActionPerformed(evt);
            }
        });
        txtInst2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInst2FocusLost(evt);
            }
        });
        panBillTenor1.add(txtInst2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(panBillTenor1, gridBagConstraints);

        lblRemitBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitBranchCode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(tdtRemitInstDate, gridBagConstraints);

        lblRemitInst.setText("Inst Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitInst, gridBagConstraints);

        txtRemitFavour1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemitFavour1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(txtRemitFavour1, gridBagConstraints);

        lblRemitFavour1.setText("Favouring");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRemitList.add(lblRemitFavour1, gridBagConstraints);

        lblDrawBnk.setText("Bnk name");
        lblDrawBnk.setFont(new java.awt.Font("MS Sans Serif", 0, 10)); // NOI18N
        lblDrawBnk.setMaximumSize(new java.awt.Dimension(150, 14));
        lblDrawBnk.setMinimumSize(new java.awt.Dimension(150, 14));
        lblDrawBnk.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(lblDrawBnk, gridBagConstraints);

        lblDrawBran.setText("Brn name");
        lblDrawBran.setFont(new java.awt.Font("MS Sans Serif", 0, 10)); // NOI18N
        lblDrawBran.setMaximumSize(new java.awt.Dimension(150, 14));
        lblDrawBran.setMinimumSize(new java.awt.Dimension(150, 14));
        lblDrawBran.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRemitList.add(lblDrawBran, gridBagConstraints);

        chkClering.setText("By Cash");
        chkClering.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCleringActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        panRemitList.add(chkClering, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panLodgementDetails.add(panRemitList, gridBagConstraints);

        panDraweeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Drawee Details"));
        panDraweeDetail.setMinimumSize(new java.awt.Dimension(410, 180));
        panDraweeDetail.setPreferredSize(new java.awt.Dimension(410, 180));
        panDraweeDetail.setLayout(new java.awt.GridBagLayout());

        lblDraweeName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeName, gridBagConstraints);

        txtDraweeName.setAllowAll(true);
        txtDraweeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweeName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        panDraweeDetail.add(txtDraweeName, gridBagConstraints);

        lblDraweeAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDraweeDetail.add(lblDraweeAddress, gridBagConstraints);

        txtDraweeAddress.setAllowAll(true);
        txtDraweeAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDraweeDetail.add(txtDraweeAddress, gridBagConstraints);

        lblDraweeCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeCity, gridBagConstraints);

        cboDraweeCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(cboDraweeCity, gridBagConstraints);

        lblDraweeState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeState, gridBagConstraints);

        cboDraweeState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(cboDraweeState, gridBagConstraints);

        lblDraweeCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeCountry, gridBagConstraints);

        cboDraweeCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(cboDraweeCountry, gridBagConstraints);

        lblDraweePinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweePinCode, gridBagConstraints);

        txtDraweePinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweePinCode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(txtDraweePinCode, gridBagConstraints);

        lblDraweeNo.setText("Drawee No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDraweeDetail.add(lblDraweeNo, gridBagConstraints);

        txtDraweeNo.setAllowAll(true);
        txtDraweeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweeNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 0);
        panDraweeDetail.add(txtDraweeNo, gridBagConstraints);

        lblSendingTo.setText("Sending To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblSendingTo, gridBagConstraints);

        txtSendingTo.setAllowAll(true);
        txtSendingTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSendingTo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        panDraweeDetail.add(txtSendingTo, gridBagConstraints);

        lblDraweeBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeBankCode, gridBagConstraints);

        txtDraweeBankCode.setAllowAll(true);
        txtDraweeBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweeBankCode.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        panDraweeDetail.add(txtDraweeBankCode, gridBagConstraints);

        lblDraweeBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeBranchCode, gridBagConstraints);

        txtDraweeBranchCode.setAllowAll(true);
        txtDraweeBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweeBranchCode.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        panDraweeDetail.add(txtDraweeBranchCode, gridBagConstraints);

        btnDraweeBranchName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDraweeBranchName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDraweeBranchName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDraweeBranchName.setEnabled(false);
        btnDraweeBranchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDraweeBranchNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDraweeDetail.add(btnDraweeBranchName, gridBagConstraints);

        txtDraweeBankNameVal.setAllowAll(true);
        txtDraweeBankNameVal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDraweeBankNameVal.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        panDraweeDetail.add(txtDraweeBankNameVal, gridBagConstraints);

        lblDraweeBankNamee.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panDraweeDetail.add(lblDraweeBankNamee, gridBagConstraints);

        btnDraweeBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDraweeBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDraweeBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDraweeBankCode.setEnabled(false);
        btnDraweeBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDraweeBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDraweeDetail.add(btnDraweeBankCode, gridBagConstraints);

        sptDraweeDetails.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 13, 7);
        panDraweeDetail.add(sptDraweeDetails, gridBagConstraints);

        panNxtAccNo.setMinimumSize(new java.awt.Dimension(290, 25));
        panNxtAccNo.setLayout(new java.awt.GridBagLayout());

        lblNxtAccNo.setForeground(new java.awt.Color(51, 102, 255));
        lblNxtAccNo.setText("Bills Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNxtAccNo.add(lblNxtAccNo, gridBagConstraints);

        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setMaximumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNxtAccNo.add(txtNextAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        panDraweeDetail.add(panNxtAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panLodgementDetails.add(panDraweeDetail, gridBagConstraints);
        panDraweeDetail.getAccessibleContext().setAccessibleName("DraweeDetails");

        panChequeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque "));
        panChequeDetails.setLayout(new java.awt.GridBagLayout());

        lblDraweeBankName.setText("Drawee Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblDraweeBankName, gridBagConstraints);

        lblDraweeBranchName.setText("Drawee Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblDraweeBranchName, gridBagConstraints);

        lblInstrumentNo.setText("Instrument No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblInstrumentNo, gridBagConstraints);

        lblInstrumentDate.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblInstrumentDate, gridBagConstraints);

        lblInstrumentAmount.setText("Instrument Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblInstrumentAmount, gridBagConstraints);

        lblMICR.setText("MICR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblMICR, gridBagConstraints);

        lblPayeeName.setText("Payee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblPayeeName, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(lblRemarks, gridBagConstraints);

        txtDraweeBankName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtDraweeBankName, gridBagConstraints);

        txtDraweeBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtDraweeBranchName, gridBagConstraints);

        txtInstrumentNo.setAllowAll(true);
        txtInstrumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtInstrumentNo, gridBagConstraints);

        txtInstrumentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstrumentAmount.setValidation(new CurrencyValidation(14,2));
        txtInstrumentAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstrumentAmountActionPerformed(evt);
            }
        });
        txtInstrumentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtInstrumentAmount, gridBagConstraints);

        txtMICR.setAllowAll(true);
        txtMICR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtMICR, gridBagConstraints);

        txtPayeeName.setAllowAll(true);
        txtPayeeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(txtPayeeName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(tdtInstrumentDate, gridBagConstraints);

        panInstrumentType.setLayout(new java.awt.GridBagLayout());

        lblInstrumentType.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstrumentType.add(lblInstrumentType, gridBagConstraints);

        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstrumentType.add(cboInstrumentType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        panChequeDetails.add(panInstrumentType, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 60));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 60));
        jScrollPane1.setRequestFocusEnabled(false);

        txaRemarks.setColumns(20);
        txaRemarks.setLineWrap(true);
        txaRemarks.setRows(5);
        txaRemarks.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(txaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(jScrollPane1, gridBagConstraints);

        txtInstPrefix.setMaximumSize(new java.awt.Dimension(50, 21));
        txtInstPrefix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstPrefix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChequeDetails.add(txtInstPrefix, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panLodgementDetails.add(panChequeDetails, gridBagConstraints);

        tabLodgementBils.addTab("LodgementDetails", panLodgementDetails);

        panInstrumentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument Details"));
        panInstrumentDetails.setLayout(new java.awt.GridBagLayout());

        panBillofExchange.setBorder(javax.swing.BorderFactory.createTitledBorder("Bill of Exchange (Hundi)"));
        panBillofExchange.setMinimumSize(new java.awt.Dimension(251, 340));
        panBillofExchange.setPreferredSize(new java.awt.Dimension(294, 340));
        panBillofExchange.setLayout(new java.awt.GridBagLayout());

        lblBillTenor.setText("Tenor of the Bill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblBillTenor, gridBagConstraints);

        panBillTenor.setLayout(new java.awt.GridBagLayout());

        txtBillTenor.setPreferredSize(new java.awt.Dimension(50, 21));
        panBillTenor.add(txtBillTenor, new java.awt.GridBagConstraints());

        cboBillTenor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboBillTenorFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBillTenor.add(cboBillTenor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillofExchange.add(panBillTenor, gridBagConstraints);

        lblDueDate.setText("Due Date of Bill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblDueDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(tdtDueDate, gridBagConstraints);

        lblAcceptanceDate.setText("Date of Acceptance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblAcceptanceDate, gridBagConstraints);

        tdtAcceptanceDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAcceptanceDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(tdtAcceptanceDate, gridBagConstraints);

        lblHundiNo.setText("Hundi No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblHundiNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(txtHundiNo, gridBagConstraints);

        lblBillAcceptance.setText("Acceptance Bill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblBillAcceptance, gridBagConstraints);

        panAcceptanceBill.setLayout(new java.awt.GridBagLayout());

        rdoBillAcceptance.add(rdoBillAcceptance_Yes);
        rdoBillAcceptance_Yes.setText("Yes");
        panAcceptanceBill.add(rdoBillAcceptance_Yes, new java.awt.GridBagConstraints());

        rdoBillAcceptance.add(rdoBillAcceptance_No);
        rdoBillAcceptance_No.setText("No");
        panAcceptanceBill.add(rdoBillAcceptance_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillofExchange.add(panAcceptanceBill, gridBagConstraints);

        lblHundiDate.setText("Hundi Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblHundiDate, gridBagConstraints);

        tdtHundiDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtHundiDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(tdtHundiDate, gridBagConstraints);

        lblDraweeHundi.setText("Drawee Hundi");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblDraweeHundi, gridBagConstraints);

        panDraweeHundi.setLayout(new java.awt.GridBagLayout());

        rdoDraweeHundi.add(rdoDraweeHundi_Yes);
        rdoDraweeHundi_Yes.setText("Yes");
        panDraweeHundi.add(rdoDraweeHundi_Yes, new java.awt.GridBagConstraints());

        rdoDraweeHundi.add(rdoDraweeHundi_No);
        rdoDraweeHundi_No.setText("No");
        panDraweeHundi.add(rdoDraweeHundi_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillofExchange.add(panDraweeHundi, gridBagConstraints);

        lblHundiAmount.setText("Hundi Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblHundiAmount, gridBagConstraints);

        txtHundiAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(txtHundiAmount, gridBagConstraints);

        lblPayable.setText("Where Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblPayable, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(txtPayable, gridBagConstraints);

        lblHundiRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(lblHundiRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillofExchange.add(txtHundiRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panInstrumentDetails.add(panBillofExchange, gridBagConstraints);

        tabLodgementBils.addTab("Instrument Details", panInstrumentDetails);

        panDocumentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Document Details"));
        panDocumentDetails.setLayout(new java.awt.GridBagLayout());

        panInvoiceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Invoice Details"));
        panInvoiceDetails.setLayout(new java.awt.GridBagLayout());

        lblInvoiceNumber.setText("Invoice Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(lblInvoiceNumber, gridBagConstraints);

        lblInvoiceDate.setText("Invoice Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(lblInvoiceDate, gridBagConstraints);

        lblInvoiceAmount.setText("Invoice Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(lblInvoiceAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(txtInvoiceNumber, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(tdtInvoiceDate, gridBagConstraints);

        txtInvoiceAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvoiceDetails.add(txtInvoiceAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDocumentDetails.add(panInvoiceDetails, gridBagConstraints);

        panRRLRARPPDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("RR/LR/AR/PP Details"));
        panRRLRARPPDetails.setLayout(new java.awt.GridBagLayout());

        lblTransportCompany.setText("Transport Company");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(lblTransportCompany, gridBagConstraints);

        lblRRLRNumber.setText("RR/LR Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(lblRRLRNumber, gridBagConstraints);

        lblRRLRDate.setText("RR/LR Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(lblRRLRDate, gridBagConstraints);

        lblGoodsValue.setText("Goods Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(lblGoodsValue, gridBagConstraints);

        lblGoodsAssigned.setText("Goods Assigned");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(lblGoodsAssigned, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(txtTransportCompany, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(tdtRRLRDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(txtRRLRNumber, gridBagConstraints);

        txtGoodsValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(txtGoodsValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails.add(txtGoodsAssigned, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDocumentDetails.add(panRRLRARPPDetails, gridBagConstraints);

        tabLodgementBils.addTab("Document Details", panDocumentDetails);

        panInstructions1.setLayout(new java.awt.GridBagLayout());

        srcTable.setAutoscrolls(true);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInstructions1.add(srcTable, gridBagConstraints);

        tabLodgementBils.addTab("Transaction Details", panInstructions1);

        panInstructions2.setBorder(javax.swing.BorderFactory.createTitledBorder("Instructions"));
        panInstructions2.setLayout(new java.awt.GridBagLayout());

        panInstEntry1.setLayout(new java.awt.GridBagLayout());

        panStdInstructions1.setLayout(new java.awt.GridBagLayout());

        srpInstructions1.setMinimumSize(new java.awt.Dimension(250, 250));
        srpInstructions1.setPreferredSize(new java.awt.Dimension(400, 300));

        tblInstruction1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "S.No.", "Instructions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInstruction1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstruction1MousePressed(evt);
            }
        });
        srpInstructions1.setViewportView(tblInstruction1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panStdInstructions1.add(srpInstructions1, gridBagConstraints);

        panOperations3.setMinimumSize(new java.awt.Dimension(400, 150));
        panOperations3.setPreferredSize(new java.awt.Dimension(400, 150));
        panOperations3.setLayout(new java.awt.GridBagLayout());

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 44, 7);
        panOperations3.add(lblParticulars, gridBagConstraints);

        lblInstruction.setText("Instruction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panOperations3.add(lblInstruction, gridBagConstraints);

        cboInstruction.setPopupWidth(180);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOperations3.add(cboInstruction, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(300, 95));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(300, 95));

        txtAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(50, 30));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(50, 30));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 6, 0);
        panOperations3.add(srpTxtAreaParticulars, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 177, 0);
        panStdInstructions1.add(panOperations3, gridBagConstraints);

        panParticulars.setLayout(new java.awt.GridBagLayout());

        btnParNew.setText("New");
        btnParNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParticulars.add(btnParNew, gridBagConstraints);

        btnParSave.setText("Save");
        btnParSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParticulars.add(btnParSave, gridBagConstraints);

        btnParDelete.setText("Delete");
        btnParDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParticulars.add(btnParDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panStdInstructions1.add(panParticulars, gridBagConstraints);

        panInstEntry1.add(panStdInstructions1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panInstructions2.add(panInstEntry1, gridBagConstraints);

        panTblInstruction1.setLayout(new java.awt.GridBagLayout());
        panInstructions2.add(panTblInstruction1, new java.awt.GridBagConstraints());

        tabLodgementBils.addTab("Instructions", panInstructions2);

        panRate.setLayout(new java.awt.GridBagLayout());

        panLableValues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLableValues.setMinimumSize(new java.awt.Dimension(200, 308));
        panLableValues.setPreferredSize(new java.awt.Dimension(350, 400));
        panLableValues.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRate.add(panLableValues, gridBagConstraints);

        panIntICC3.setMinimumSize(new java.awt.Dimension(450, 406));
        panIntICC3.setPreferredSize(new java.awt.Dimension(450, 400));
        panIntICC3.setLayout(new java.awt.GridBagLayout());

        panRRLRARPPDetails1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRRLRARPPDetails1.setMinimumSize(new java.awt.Dimension(440, 324));
        panRRLRARPPDetails1.setPreferredSize(new java.awt.Dimension(440, 324));
        panRRLRARPPDetails1.setLayout(new java.awt.GridBagLayout());

        panOverdueRateBills9.setLayout(new java.awt.GridBagLayout());

        txtRateForDelay1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateForDelay1.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRateForDelay1.setValidation(new PercentageValidation());
        panOverdueRateBills9.add(txtRateForDelay1, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per2.setText("%");
        panOverdueRateBills9.add(lblDefaultPostage_Per2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panOverdueRateBills9, gridBagConstraints);

        lblRateForDelay1.setText("Margin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblRateForDelay1, gridBagConstraints);

        panIntDays.setLayout(new java.awt.GridBagLayout());

        txtIntDays.setEnabled(false);
        txtIntDays.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntDays.setMinimumSize(new java.awt.Dimension(25, 21));
        txtIntDays.setPreferredSize(new java.awt.Dimension(25, 21));
        txtIntDays.setValidation(new NumericValidation()
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntDays.add(txtIntDays, gridBagConstraints);

        cboIntDays.setEnabled(false);
        cboIntDays.setMinimumSize(new java.awt.Dimension(70, 25));
        cboIntDays.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panIntDays.add(cboIntDays, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRRLRARPPDetails1.add(panIntDays, gridBagConstraints);

        lblIntDays.setText("No. Of Days Int Cheque Disc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblIntDays, gridBagConstraints);

        panDiscountRate.setLayout(new java.awt.GridBagLayout());

        txtDiscountRateBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDiscountRateBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDiscountRateBills.setValidation(new PercentageValidation());
        panDiscountRate.add(txtDiscountRateBills, new java.awt.GridBagConstraints());

        lblDiscountRateBills_Per.setText("%");
        panDiscountRate.add(lblDiscountRateBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panDiscountRate, gridBagConstraints);

        lblDiscountRateOfBD.setText("Discount Rate For Bills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblDiscountRateOfBD, gridBagConstraints);

        lblOverdueInterestForBD.setText("Overdue Interest Rate For Bills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblOverdueInterestForBD, gridBagConstraints);

        panOverdueRateBills1.setLayout(new java.awt.GridBagLayout());

        txtOverdueRateBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtOverdueRateBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtOverdueRateBills.setValidation(new PercentageValidation());
        panOverdueRateBills1.add(txtOverdueRateBills, new java.awt.GridBagConstraints());

        lblOverdueRateBills_Per.setText("%");
        panOverdueRateBills1.add(lblOverdueRateBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panOverdueRateBills1, gridBagConstraints);

        lblOverdueRateCBP.setText("Overdue Rate For CBP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblOverdueRateCBP, gridBagConstraints);

        panOverdueRateBills2.setLayout(new java.awt.GridBagLayout());

        txtRateForCBP.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateForCBP.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRateForCBP.setValidation(new PercentageValidation());
        panOverdueRateBills2.add(txtRateForCBP, new java.awt.GridBagConstraints());

        lblRateForCBP_Per.setText("%");
        panOverdueRateBills2.add(lblRateForCBP_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panOverdueRateBills2, gridBagConstraints);

        lblAtParLimit.setText("At Par Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblAtParLimit, gridBagConstraints);

        txtAtParLimit.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAtParLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(txtAtParLimit, gridBagConstraints);

        panOverdueRateBills3.setLayout(new java.awt.GridBagLayout());

        txtCleanBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCleanBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtCleanBills.setValidation(new PercentageValidation());
        panOverdueRateBills3.add(txtCleanBills, new java.awt.GridBagConstraints());

        lblCleanBills_Per.setText("%");
        panOverdueRateBills3.add(lblCleanBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panOverdueRateBills3, gridBagConstraints);

        lblCleanBillsPurchased.setText("Clean Bills Purchased");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblCleanBillsPurchased, gridBagConstraints);

        panTransitPeriod.setLayout(new java.awt.GridBagLayout());

        txtTransitPeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTransitPeriod.setMinimumSize(new java.awt.Dimension(25, 21));
        txtTransitPeriod.setPreferredSize(new java.awt.Dimension(25, 21));
        txtTransitPeriod.setValidation(new NumericValidation()
        );
        txtTransitPeriod.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransitPeriod.add(txtTransitPeriod, gridBagConstraints);

        cboTransitPeriod.setEnabled(false);
        cboTransitPeriod.setMinimumSize(new java.awt.Dimension(70, 25));
        cboTransitPeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panTransitPeriod.add(cboTransitPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRRLRARPPDetails1.add(panTransitPeriod, gridBagConstraints);

        lblTransitPeriod.setText("Transit Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblTransitPeriod, gridBagConstraints);

        panOverdueRateBills.setLayout(new java.awt.GridBagLayout());

        txtDefaultPostage.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDefaultPostage.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDefaultPostage.setValidation(new PercentageValidation());
        panOverdueRateBills.add(txtDefaultPostage, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per.setText("%");
        panOverdueRateBills.add(lblDefaultPostage_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRRLRARPPDetails1.add(panOverdueRateBills, gridBagConstraints);

        lblDefaultPostage.setText("Default Postage Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panRRLRARPPDetails1.add(lblDefaultPostage, gridBagConstraints);

        panIntICC.setLayout(new java.awt.GridBagLayout());

        rdoIntICC.add(cRadio_ICC_Yes);
        cRadio_ICC_Yes.setText("Yes");
        cRadio_ICC_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRadio_ICC_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panIntICC.add(cRadio_ICC_Yes, gridBagConstraints);

        rdoIntICC.add(cRadio_ICC_No);
        cRadio_ICC_No.setText("NO");
        cRadio_ICC_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRadio_ICC_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panIntICC.add(cRadio_ICC_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panRRLRARPPDetails1.add(panIntICC, gridBagConstraints);

        lblIntICC.setText("Collect Int during Lodgement for ICC");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRRLRARPPDetails1.add(lblIntICC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntICC3.add(panRRLRARPPDetails1, gridBagConstraints);

        panIntICC2.setLayout(new java.awt.GridBagLayout());

        srpInstructions2.setMinimumSize(new java.awt.Dimension(350, 80));
        srpInstructions2.setPreferredSize(new java.awt.Dimension(350, 74));

        tblInstruction2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Product ID", "Account No", "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInstruction2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstruction2MousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInstruction2MouseClicked(evt);
            }
        });
        srpInstructions2.setViewportView(tblInstruction2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panIntICC2.add(srpInstructions2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panIntICC3.add(panIntICC2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRate.add(panIntICC3, gridBagConstraints);

        tabLodgementBils.addTab("Bills Rates", panRate);

        panInstructions.setBorder(javax.swing.BorderFactory.createTitledBorder("Charges"));

        panInstEntry.setLayout(new java.awt.GridBagLayout());

        panTblInstruction.setLayout(new java.awt.GridBagLayout());

        panProductDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductDetails1.setMinimumSize(new java.awt.Dimension(600, 81));
        panProductDetails1.setPreferredSize(new java.awt.Dimension(600, 81));

        panStdInstructions.setLayout(new java.awt.GridBagLayout());

        lblStdInstruction.setText("Standard Instruction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        panStdInstructions.add(lblStdInstruction, gridBagConstraints);

        lblAdditionalInstruction.setText("Additional Instruction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 0, 0);
        panStdInstructions.add(lblAdditionalInstruction, gridBagConstraints);

        cboStdInstruction.setPopupWidth(180);
        cboStdInstruction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboStdInstructionItemStateChanged(evt);
            }
        });
        cboStdInstruction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStdInstructionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        panStdInstructions.add(cboStdInstruction, gridBagConstraints);

        txtStdInstruction.setAllowAll(true);
        txtStdInstruction.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panStdInstructions.add(txtStdInstruction, gridBagConstraints);

        txtAmount.setValidation(new DefaultValidation());
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panStdInstructions.add(txtAmount, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 88, 0, 0);
        panStdInstructions.add(lblAmount, gridBagConstraints);

        txtServiceTax.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panStdInstructions.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 66, 0, 0);
        panStdInstructions.add(lblServiceTax, gridBagConstraints);

        panOperations1.setLayout(new java.awt.GridBagLayout());

        txtTotalAmt.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations1.add(txtTotalAmt, gridBagConstraints);

        lblTotalAmt.setText("Total Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations1.add(lblTotalAmt, gridBagConstraints);

        txtTotalServTax.setPreferredSize(new java.awt.Dimension(50, 21));
        txtTotalServTax.setValidation(new DefaultValidation());
        txtTotalServTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalServTaxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panOperations1.add(txtTotalServTax, gridBagConstraints);

        lblTotalServTax.setText("Total Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations1.add(lblTotalServTax, gridBagConstraints);

        lblServiceTaxval.setToolTipText("Service Tax + Education cess + Higer Education Cess");
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panOperations1.add(lblServiceTaxval, gridBagConstraints);

        srpInstructions.setMinimumSize(new java.awt.Dimension(250, 250));
        srpInstructions.setPreferredSize(new java.awt.Dimension(300, 100));

        tblInstruction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "S.No.", "Instructions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInstruction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblInstructionMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstructionMousePressed(evt);
            }
        });
        srpInstructions.setViewportView(tblInstruction);

        panOtherBnkChrg.setLayout(new java.awt.GridBagLayout());

        lblRemitFavour.setText("Other Bank Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 27, 0, 0);
        panOtherBnkChrg.add(lblRemitFavour, gridBagConstraints);

        txtRemitFavour.setAllowNumber(true);
        txtRemitFavour.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemitFavour.setValidation(new DefaultValidation());
        txtRemitFavour.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRemitFavourFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 7, 0, 0);
        panOtherBnkChrg.add(txtRemitFavour, gridBagConstraints);

        lblRemitAmt.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 100, 0, 0);
        panOtherBnkChrg.add(lblRemitAmt, gridBagConstraints);

        txtInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstAmt.setValidation(new CurrencyValidation(14,2));
        txtInstAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstAmtActionPerformed(evt);
            }
        });
        txtInstAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 10, 9);
        panOtherBnkChrg.add(txtInstAmt, gridBagConstraints);

        lblProductType1.setText("Product Type");

        cboProductType1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType1.setPopupWidth(120);
        cboProductType1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductType1ActionPerformed(evt);
            }
        });

        lblProductId1.setText("Product Id");

        cboProductId1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId1.setPopupWidth(130);
        cboProductId1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductId1ActionPerformed(evt);
            }
        });

        cLabel1.setText("Individual Amount");

        txtSplitAmnt.setAllowAll(true);
        txtSplitAmnt.setAllowNumber(true);
        txtSplitAmnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSplitAmntFocusLost(evt);
            }
        });

        lblAccountNo1.setText("Account No.");

        txtAccountNo1.setAllowAll(true);
        txtAccountNo1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo1.setValidation(new DefaultValidation());
        txtAccountNo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNo1ActionPerformed(evt);
            }
        });
        txtAccountNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNo1FocusLost(evt);
            }
        });

        btnAccountNo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo1.setEnabled(false);
        btnAccountNo1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNo1ActionPerformed(evt);
            }
        });

        lblCustName.setText("Name");

        lblReference1.setText("Reference");

        txtReference1.setAllowAll(true);
        txtReference1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReference1.setValidation(new DefaultValidation());
        txtReference1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReference1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cPanel2Layout = new javax.swing.GroupLayout(cPanel2);
        cPanel2.setLayout(cPanel2Layout);
        cPanel2Layout.setHorizontalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel2Layout.createSequentialGroup()
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lblProductType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(cboProductType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(lblProductId1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(cboProductId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(lblAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(lblReference1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtReference1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cPanel2Layout.createSequentialGroup()
                                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(txtSplitAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(70, 70, 70))
        );
        cPanel2Layout.setVerticalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboProductType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboProductId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSplitAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAccountNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCustName, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblReference1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtReference1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panOperations.setLayout(new java.awt.GridBagLayout());

        btnInstNew.setText("New");
        btnInstNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstNewActionPerformed(evt);
            }
        });
        panOperations.add(btnInstNew, new java.awt.GridBagConstraints());

        btnInstSave.setText("Save");
        btnInstSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstSaveActionPerformed(evt);
            }
        });
        panOperations.add(btnInstSave, new java.awt.GridBagConstraints());

        btnInstDelete.setText("Delete");
        btnInstDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstDeleteActionPerformed(evt);
            }
        });
        panOperations.add(btnInstDelete, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout panProductDetails1Layout = new javax.swing.GroupLayout(panProductDetails1);
        panProductDetails1.setLayout(panProductDetails1Layout);
        panProductDetails1Layout.setHorizontalGroup(
            panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProductDetails1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panProductDetails1Layout.createSequentialGroup()
                        .addComponent(cPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panOtherBnkChrg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panStdInstructions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39))
                    .addGroup(panProductDetails1Layout.createSequentialGroup()
                        .addComponent(srpInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panProductDetails1Layout.createSequentialGroup()
                        .addComponent(panOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panOperations1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );
        panProductDetails1Layout.setVerticalGroup(
            panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panProductDetails1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panProductDetails1Layout.createSequentialGroup()
                        .addComponent(panStdInstructions, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                        .addGap(49, 49, 49)
                        .addComponent(panOtherBnkChrg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panProductDetails1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(68, 68, 68)
                .addComponent(srpInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panProductDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panOperations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panOperations1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout panInstructionsLayout = new javax.swing.GroupLayout(panInstructions);
        panInstructions.setLayout(panInstructionsLayout);
        panInstructionsLayout.setHorizontalGroup(
            panInstructionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstructionsLayout.createSequentialGroup()
                .addGroup(panInstructionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panTblInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panInstEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInstructionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panInstructionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panProductDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, 748, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        panInstructionsLayout.setVerticalGroup(
            panInstructionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInstructionsLayout.createSequentialGroup()
                .addGroup(panInstructionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panTblInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panInstEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panProductDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabLodgementBils.addTab("Charges", panInstructions);

        javax.swing.GroupLayout panLodgementBIllsLayout = new javax.swing.GroupLayout(panLodgementBIlls);
        panLodgementBIlls.setLayout(panLodgementBIllsLayout);
        panLodgementBIllsLayout.setHorizontalGroup(
            panLodgementBIllsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLodgementBIllsLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(tabLodgementBils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panLodgementBIllsLayout.setVerticalGroup(
            panLodgementBIllsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabLodgementBils, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(panLodgementBIlls, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry On History");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnView);

        lbSpace3.setText("     ");
        tbrLodgmentBills.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnNew);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace22);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnDelete);

        lbSpace2.setText("     ");
        tbrLodgmentBills.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnSave);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace23);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLodgmentBills.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnAuthorize);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace24);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnException);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace25);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnReject);

        lblSpace5.setText("     ");
        tbrLodgmentBills.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnPrint);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLodgmentBills.add(lblSpace26);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLodgmentBills.add(btnClose);

        getContentPane().add(tbrLodgmentBills, java.awt.BorderLayout.NORTH);

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
        mnuProcess.add(sptView);

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

        mbrLodgemntBills.add(mnuProcess);

        setJMenuBar(mbrLodgemntBills);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
        System.out.println("btnPrintActionPerformed ====== "+getScreenID());
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnDraweeBranchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDraweeBranchNameActionPerformed
        // TODO add your handling code here:
        callView(QDBB);
    }//GEN-LAST:event_btnDraweeBranchNameActionPerformed

    private void btnDraweeBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDraweeBankCodeActionPerformed
        // TODO add your handling code here:
        callView(QDB);
    }//GEN-LAST:event_btnDraweeBankCodeActionPerformed

    private void tblInstruction2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstruction2MousePressed
        // TODO add your handling code here:
        ArrayList aryLst = observable.populateSelectedRowAct(tblInstruction2.getSelectedRow());
        if(observable.getSubRegType().equals("CPD")){
        if(tblInstruction2.getSelectedRow() == 0){
            String actNum = (CommonUtil.convertObjToStr(aryLst.get(1)));
            if(actNum.length()>0)
            transDetails.setTransDetails("BILLS", ProxyParameters.BRANCH_ID, actNum);
        }else{
            String actNum = (CommonUtil.convertObjToStr(aryLst.get(1)));
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
            if(actNum.length()>0)
            transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, actNum);
        }
        }else{
            String actNum = (CommonUtil.convertObjToStr(aryLst.get(1)));
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
            if(actNum.length()>0)
            transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, actNum);
        }
    }//GEN-LAST:event_tblInstruction2MousePressed

    private void tblInstruction2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstruction2MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblInstruction2MouseClicked

    private void cRadio_ICC_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRadio_ICC_NoActionPerformed
        // TODO add your handling code here:
        if(observable.getSubRegType().equals("ICC")){
        if(cRadio_ICC_No.isSelected()){
            panOverdueRateBills8.setVisible(false);
            lblRateForDelay.setVisible(false);
        }else{
            panOverdueRateBills8.setVisible(true);
            lblRateForDelay.setVisible(true);
            lblRateForDelay.setText("Interest Rate Applied");
          }
        }
    }//GEN-LAST:event_cRadio_ICC_NoActionPerformed

    private void cRadio_ICC_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRadio_ICC_YesActionPerformed
        // TODO add your handling code here:
        if(observable.getSubRegType().equals("ICC")){
        if(cRadio_ICC_Yes.isSelected()){
                     panOverdueRateBills8.setVisible(true);
                     lblRateForDelay.setVisible(true);
                     lblRateForDelay.setText("Interest Rate Applied");
                }else{
                    panOverdueRateBills8.setVisible(false);
                     lblRateForDelay.setVisible(false);
                }
        }
    }//GEN-LAST:event_cRadio_ICC_YesActionPerformed

    private void txtInstrumentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentAmountFocusLost
        // TODO add your handling code here:
        if((!txtInstrumentAmount.getText().equals("")) && (observable.getSubRegType().equals("ICC"))){
            double instAmt = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()).doubleValue();
            double atParlmt = CommonUtil.convertObjToDouble(txtAtParLimit.getText()).doubleValue();
            if(instAmt > atParlmt){
                ClientUtil.showAlertWindow("Instrument Amount cannot be greater than At Par Limit "+atParlmt);
                txtInstrumentAmount.setText("");
            }
        }
    }//GEN-LAST:event_txtInstrumentAmountFocusLost

    private void txtRemitFavourFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemitFavourFocusLost
        // TODO add your handling code here:
        double otherChrgAmt = CommonUtil.convertObjToDouble(txtRemitFavour.getText()).doubleValue();
        if(otherChrgAmt >= 0){
            double instAmt = CommonUtil.convertObjToDouble(txtSplitAmnt.getText()).doubleValue();
            double remitAmt = instAmt-otherChrgAmt;
            System.out.println("remitAmtremitAmt====="+remitAmt);
            txtInstAmt.setText(String.valueOf(remitAmt));
            if(otherChrgAmt>instAmt){
                ClientUtil.showAlertWindow("Other Charge Amount cannot be greater than Instrument Amount");
                txtRemitFavour.setText("");
                txtInstAmt.setText("");
            }
        }
        
    }//GEN-LAST:event_txtRemitFavourFocusLost

    private void txtInst2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInst2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInst2ActionPerformed

    private void cboRemitProdIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboRemitProdIDItemStateChanged
        // TODO add your handling code here:
        clearRemitTab();
    }//GEN-LAST:event_cboRemitProdIDItemStateChanged

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        viewType = ClientConstants.ACTION_STATUS[16];
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtInstAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstAmtActionPerformed

    private void btnAccountNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumActionPerformed
        // TODO add your handling code here:
        if(cboProductID.getSelectedIndex()>0 && cboProductID.getSelectedIndex()>0){
//            if(observable.getCbmProdID()).getKeyForSelected().equals("GL")){
                callView(CPDAN);
//            }
        }
    }//GEN-LAST:event_btnAccountNumActionPerformed

    private void cboRemitBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRemitBranchCodeActionPerformed
        // TODO add your handling code here:
        String bankCode = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitBranchCode).getModel())).getKeyForSelected());
//        fillBankBranchName(bankCode,lblBranchCodeValue,"getBillsBranchName");
        fillBankBranchName(bankCode,txtBankCode.getText(),lblDrawBran,"getBillsBranchName");
    }//GEN-LAST:event_cboRemitBranchCodeActionPerformed

    private void tblInstruction1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstruction1MousePressed
        // TODO add your handling code here:
           selectedRowInst = -1;
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            selectedDataInst = 1;
            selectedRowInst = tblInstruction1.getSelectedRow();
            ClientUtil.clearAll(panOperations3);
            updateOBFields();
            observable.populateSelectedRowInst(tblInstruction1.getSelectedRow());
            setPanOperationsEnableInst(true);
            ClientUtil.enableDisable(panOperations3,true);
            cboInstruction.setEnabled(false);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            setPanOperationsEnableInst(false);
        }
        if(viewType.equals(AUTHORIZE)){
            setPanOperationsEnableInst(false);
            ClientUtil.enableDisable(panOperations3,false);
            btnBranchCode.setEnabled(false);
        }
    }//GEN-LAST:event_tblInstruction1MousePressed

    private void btnParDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParDeleteActionPerformed
        // TODO add your handling code here:
          updateOBFields();
        observable.deleteSelectedRowInstr(selectedRowInst);
        btnParDelete.setEnabled(false);
        btnParSave.setEnabled(false);
    }//GEN-LAST:event_btnParDeleteActionPerformed

    private void btnParSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParSaveActionPerformed
        // TODO add your handling code here:
        
        updateOBFields();
        addRowInstr();
        observable.resetInstr();
    }//GEN-LAST:event_btnParSaveActionPerformed

    private void btnParNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        selectedDataInst = 0;
        observable.resetInstr();
        ClientUtil.enableDisable(panOperations3, true);
        btnParSave.setEnabled(true);
    }//GEN-LAST:event_btnParNewActionPerformed

    private void lblCurStatusFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblCurStatusFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lblCurStatusFocusLost

    private void cboActivitiesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboActivitiesItemStateChanged
        // TODO add your handling code here:
        
         String activity = "";
        String operatesLike = "";
        operatesLike = lblOperatesLikeValue.getText();
        observable.setCboActivities(CommonUtil.convertObjToStr(cboActivities.getSelectedItem()));
        activity = observable.getCboActivities();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)){
         if((activity.equalsIgnoreCase("REALIZE") || (activity.equalsIgnoreCase("DISHONOUR"))) && (operatesLike.equalsIgnoreCase("OUTWARD"))){
                    enableDisableOtherBankChrgFields(true);
                    HashMap overDue = new HashMap();
                    overDue.put("LODGEMENT_ID", txtLodgementId.getText());
                    overDue.put("CUR_DATE", currDt);
                    List dueLst = ClientUtil.executeQuery("getBillsOverDueIntDetails", overDue);
                    overDue = null;
                    if(dueLst != null && dueLst.size() > 0){
                        panOverdueRateBills8.setVisible(true);
                        lblRateForDelay.setVisible(true);
                        txtRateForDelay.setEditable(true);
                        txtRateForDelay.setEnabled(true);
                        lblRateForDelay.setText(resourceBundle.getString("lblRateForDelay"));
                        if(observable.getSubRegType().equals("CPD")){
                                     HashMap getInterestMap = new HashMap();
                             //                        System.out.println("#####period : "+matperiod);
                             getInterestMap.put("LIMIT", observable.getTxtInstrumentAmount());
                             getInterestMap.put("CATEGORY", observable.getCbmCustCategory().getKeyForSelected());
                             getInterestMap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
        //                     getInterestMap.put("PRODUCT_TYPE","AD");
                             getInterestMap.put("DEPOSIT_DT", currDt);
                             getInterestMap.put("ACT_NUM", observable.getTxtAccountNum());
                             HashMap intMap = new HashMap();
                             intMap = observable.getInterestDetails(getInterestMap);
                             getInterestMap = null;
                             System.out.println("###intMap : "+intMap);
                             double rateOfInt = CommonUtil.convertObjToDouble(intMap.get("INTEREST")).doubleValue();
                             double penal = CommonUtil.convertObjToDouble(intMap.get("PENAL_INTEREST")).doubleValue();
                             double totint = rateOfInt+penal;
                             txtRateForDelay.setText(String.valueOf(totint));
                             intMap = null;
                        }
                        dueLst = null;
                    } 
               
            }
        
//         cboRemitProdID.setSelectedItem("");commented for testin
//         clearRemitTab();
//         if(activity.equalsIgnoreCase("DISHONOUR")){
//             txtInstAmt.setText(txtInstrumentAmount.getText());
//             tdtRemitInstDate.setDateValue(tdtInstrumentDate.getDateValue());
//         }else{
//             txtInstAmt.setText("");
//             tdtRemitInstDate.setDateValue("");
//         }
         if((observable.getSubRegType().equals("ICC")) && (activity.equalsIgnoreCase("DISHONOUR"))){
             panOverdueRateBills8.setVisible(true);
             lblRateForDelay.setVisible(true);
             txtRateForDelay.setEditable(true);
             txtRateForDelay.setEnabled(true);
             lblRateForDelay.setText("Rate of Int for Dishonour");
         }
        }
        txtRemitFavour.setText("");
        txtInstAmt.setText("");
    }//GEN-LAST:event_cboActivitiesItemStateChanged
private void clearRemitTab(){
//        cboRemitProdID.setSelectedItem("");
        cboRemitCity.setSelectedItem("");
        cboRemitDraweeBank.setSelectedItem("");
        lblDrawBnk.setText("");
        cboRemitBranchCode.setSelectedItem("");
        lblDrawBran.setText("");
//        if(txtInstAmt.getText().equals(""))
//        txtInstAmt.setText("");
        txtRemitFavour.setText("");
        txtRemitFavour1.setText("");
        tdtRemitInstDate.setDateValue("");
        txtInst1.setText("");
        txtInst2.setText("");
//        txtInstAmt.setText("");
}
    private void txtInstAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstAmtFocusLost
        // TODO add your handling code here:
//        double remitAmt = CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue();
//        if(remitAmt > 0){
//            double instAmt = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()).doubleValue();
//            double otherBankCharge = instAmt-remitAmt;
//            txtRemitFavour.setText(String.valueOf(otherBankCharge));
//            String activity = "";
//            String operatesLike = "";
//            operatesLike = lblOperatesLikeValue.getText();
//            observable.setCboActivities(CommonUtil.convertObjToStr(cboActivities.getSelectedItem()));
//            activity = observable.getCboActivities();
//            if((activity.equalsIgnoreCase("REALIZE")) && (operatesLike.equalsIgnoreCase("INWARD"))){
//                txtRemitFavour.setText("");
//            }
//            if(remitAmt>instAmt){
//                ClientUtil.showAlertWindow("Remit Amount cannot be greater than Instrument Amount");
//                txtRemitFavour.setText("");
//                txtInstAmt.setText("");
//            }
//        }
    }//GEN-LAST:event_txtInstAmtFocusLost

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        txtAmountFocusLost();
    }//GEN-LAST:event_txtAmountFocusLost

    private void cboStdInstructionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboStdInstructionItemStateChanged
        // TODO add your handling code here:
      
        txtAmount.setText(observable.executeQueryForCharge(CommonUtil.convertObjToStr(observable.getCbmBillsType().getKeyForSelected()),CommonUtil.convertObjToStr((((ComboBoxModel)(cboCustCategory).getModel())).getKeyForSelected()),txtSplitAmnt.getText(), CommonUtil.convertObjToStr((((ComboBoxModel)(cboStdInstruction).getModel())).getKeyForSelected())));
          System.out.println("TXTT 222-->"+txtAmount.getText());
        txtAmountFocusLost();
    }//GEN-LAST:event_cboStdInstructionItemStateChanged
    
    private void txtInst1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInst1FocusLost
        // TODO add your handling code here:
//        txtInstrumentNoFocusLost();
        
    }//GEN-LAST:event_txtInst1FocusLost
    
    private void txtInst2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInst2FocusLost
        // TODO add your handling code here:
//        txtInstrumentNoFocusLost();
        String prodId = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitProdID).getModel())).getKeyForSelected());
        String act = CommonUtil.convertObjToStr((((ComboBoxModel)(cboActivities).getModel())).getKeyForSelected());
        String op = CommonUtil.convertObjToStr(lblOperatesLikeValue.getText());
        if(((prodId.equals("PO")) || (prodId.equals("DD"))) && (act.equals("PROCEEDS_RECEIVED")) && (op.equals("OUTWARD"))){
            boolean check = false;
            String authStatus = "";
            HashMap outwardHash = new HashMap();
//            OutwardClearingUI outwardUI = new OutwardClearingUI();
//            com.see.truetransact.ui.TrueTransactMain.showScreen(outwardUI);
            outwardHash.put("INSTRUMENT_TYPE", cboRemitProdID.getSelectedItem());
            outwardHash.put("INST_NO2", txtInst2.getText());
            outwardHash.put("AMOUNT", txtInstAmt.getText());
            outwardHash.put("BANK_CODE", cboRemitDraweeBank.getSelectedItem());
            outwardHash.put("BANK_NAME", lblDrawBnk.getText());
            outwardHash.put("BRANCH_CODE", cboRemitBranchCode.getSelectedItem());
            outwardHash.put("REMARKS", cboActivities.getSelectedItem());
            outwardHash.put("LODGE_ID", txtLodgementId.getText());
            outwardHash.put("REMIT_INST_DT", tdtRemitInstDate.getDateValue());
            outwardHash.put("BRANCH_NAME", lblDrawBran.getText());
            outwardHash.put("PROD_ID", CommonUtil.convertObjToStr((((ComboBoxModel)(cboBillsType).getModel())).getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getBillsRealHead", outwardHash);
            if(lst.size()>0 && lst != null){
                HashMap head = (HashMap)lst.get(0);
                outwardHash.put("BILLSHEAD", head.get("BILLS_REALISED_HD"));
                outwardHash.put("BILLSHEADDESC", head.get("AC_HD_DESC"));
                lst = null;
                head = null;
            }
            lst = ClientUtil.executeQuery("checkOutwardLodged", outwardHash);
            if(lst.size()>0 && lst != null){
                HashMap head = (HashMap)lst.get(0);
                authStatus = CommonUtil.convertObjToStr(head.get("AUTHORIZE_STATUS"));
                check = true;
                lst = null;
                head = null;
            }
            if(!check){
                OutwardClearingUI outwardUI = new OutwardClearingUI();
                com.see.truetransact.ui.TrueTransactMain.showScreen(outwardUI);
                outwardUI.setTitle("Outward Clearing");
                outwardUI.callingFromLodgement(outwardHash);
            }else{
                if((authStatus.equals("")) || (authStatus == null)){
                    ClientUtil.showAlertWindow("Instrument already Lodged in Outward Clearing and pending for Authorization");
                }else if(authStatus.equals("AUTHORIZED")){
                    ClientUtil.showAlertWindow("Instrument already Lodged in Outward Clearing");
                }else if(authStatus.equals("REJECTED")){
                    OutwardClearingUI outwardUI = new OutwardClearingUI();
                    com.see.truetransact.ui.TrueTransactMain.showScreen(outwardUI);
                    outwardUI.callingFromLodgement(outwardHash);
                }
                check = false;
            }
        }
    }//GEN-LAST:event_txtInst2FocusLost
    private void txtInstrumentNoFocusLost(){
        HashMap dataMap = new HashMap();
        dataMap.put("INSTRUMENT_NO1", txtInst1.getText());
        dataMap.put("INSTRUMENT_NO2", txtInst2.getText());
        List lst = ClientUtil.executeQuery("checkValidInsNum", dataMap);
        dataMap = null;
        if(lst != null && lst.size() > 0){
            dataMap = (HashMap) lst.get(0);
            String authStatus = CommonUtil.convertObjToStr(dataMap.get("AUTHORIZE_STATUS"));
            if(authStatus.equalsIgnoreCase("")){
                ClientUtil.showAlertWindow("Instrument "+txtInst1.getText()+"-"+txtInst2.getText()+" already issued and pending for Authorization");
                txtInst2.setText("");
            }else if(dataMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                ClientUtil.showAlertWindow("Instrument "+txtInst1.getText()+"-"+txtInst2.getText()+" already issued");
                txtInst2.setText("");
            }
        }else{
            //DO NOTHING
        }
    }
    private void txtAmountFocusLost(){
//     if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){   
         double amountPayable = 0 ;
        if(txtAmount.getText().length() > 0  && !txtAmount.getText().equals("")){
            txtServiceTax.setText(observable.calServiceTax(txtAmount.getText(),CommonUtil.convertObjToStr(observable.getCbmBillsType().getKeyForSelected()),CommonUtil.convertObjToStr((((ComboBoxModel)(cboCustCategory).getModel())).getKeyForSelected()), txtSplitAmnt.getText(), CommonUtil.convertObjToStr((((ComboBoxModel)(cboStdInstruction).getModel())).getKeyForSelected())));
            System.out.println("TXTTT SERV --->"+txtServiceTax.getText());
            //            amountPayable = CommonUtil.convertObjToDouble(txtPayAmount.getText()).doubleValue() - 
//            (CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() + 
//            CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue());
//            lblPayableAmountValue.setText(String.valueOf(amountPayable));
        }else{
//            lblPayableAmountValue.setText(String.valueOf(amountPayable));
        }
//        observable.setLblPayableAmount(lblPayableAmountValue.getText());
//        transactionUI.setCallingAmount(lblPayableAmountValue.getText());
//        System.out.println("txtNumber1.getText() : " + txtNumber1.getText());
//        transactionUI.setCallingInst1(txtNumber1.getText());
//        transactionUI.setCallingInst2(txtNumber2.getText());
//     }
    }
    private void cboActivitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActivitiesActionPerformed
        // TODO add your handling code here:
        String activity = "";
        String operatesLike = "";
        txtLodgementId.setEnabled(false);
        operatesLike = lblOperatesLikeValue.getText();
        observable.setCboActivities(CommonUtil.convertObjToStr(cboActivities.getSelectedItem()));
        activity = observable.getCboActivities();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) || (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) ||
        (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) || ((observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)) || ((observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW))){ 
//            clearRemitTab();
        if((activity.equalsIgnoreCase("REALIZE")) || (activity.equalsIgnoreCase("PROCEEDS RECEIVED"))){
            
            panRemitList.setEnabled(true);
            panRemitList.setVisible(true);
            if(lblOperatesLikeValue.getText().equals("OUTWARD")){
            cboRemitProdID.setVisible(true);
            cboRemitCity.setVisible(true);
            cboRemitDraweeBank.setVisible(true);
            cboRemitBranchCode.setVisible(true);
            lblRemitProdID.setVisible(true);
            lblRemitCity.setVisible(true);
            lblRemitDraweeBank.setVisible(true);
            lblRemitBranchCode.setVisible(true);
            lblInstNo.setVisible(true);
            txtInst2.setVisible(true);
            txtInst1.setVisible(true);
            txtRemitFavour1.setVisible(false);
            lblRemitFavour1.setVisible(false);
            }
            if((activity.equalsIgnoreCase("REALIZE")) && (operatesLike.equalsIgnoreCase("OUTWARD"))){
                cboTranstype.setVisible(true);
                lblTranstype.setVisible(true);
                txtRemitFavour.setVisible(true);
                lblRemitFavour.setVisible(true);
                txtInstAmt.setVisible(true);
                lblRemitAmt.setVisible(true);
                tdtRemitInstDate.setVisible(true);
                lblRemitInst.setVisible(true);
                //added by rishad 15/01/2015 
                 chkClering.setVisible(true);
            }else{
                 cboTranstype.setVisible(false);
                lblTranstype.setVisible(false);
                txtRemitFavour1.setVisible(false);
                lblRemitFavour1.setVisible(false);
            }
            if((activity.equalsIgnoreCase("REALIZE")) && (operatesLike.equalsIgnoreCase("INWARD"))){
                txtRemitFavour1.setVisible(true);
                lblRemitFavour1.setVisible(true);
                txtInstAmt.setVisible(true);
                lblRemitAmt.setVisible(true);
                tdtRemitInstDate.setVisible(false);
                lblRemitInst.setVisible(false);
                txtInst2.setVisible(false);
                txtInst1.setVisible(false);
                lblInstNo.setVisible(false);
             //   txtRemitFavour.setVisible(false);
             //   lblRemitFavour.setVisible(false);
             //   lblRemitFavour1.setText("Favouring");
//                double remitAmt = CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue();
                txtRemitFavour.setText("0.00");
                double instAmt = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()).doubleValue();
                double instTotAmt = CommonUtil.convertObjToDouble(txtTotalAmt.getText()).doubleValue();
                double servTotAmt = CommonUtil.convertObjToDouble(txtTotalServTax.getText()).doubleValue();
                double remitAmt = instAmt-(instTotAmt+servTotAmt);
                txtInstAmt.setText(String.valueOf(remitAmt));
                chkClering.setVisible(true);
                
                //       if(billActivity.equalsIgnoreCase("REALIZE") && lblOperatesLikeValue.getText().equals("INWARD")){
            double otherChrgAmt = CommonUtil.convertObjToDouble(txtRemitFavour.getText()).doubleValue();
      //  if(otherChrgAmt >= 0){
            double instAmt1 = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()).doubleValue();
            double remitAmt1 = instAmt1-otherChrgAmt;
            System.out.println("remitAmtremitAmt====="+remitAmt1);
            txtInstAmt.setText(String.valueOf(remitAmt1));
            if(otherChrgAmt>instAmt1){
                ClientUtil.showAlertWindow("Other Charge Amount cannot be greater than Instrument Amount");
                txtRemitFavour.setText("");
                txtInstAmt.setText("");
            }
      //  }
           // }
            }
            if((activity.equalsIgnoreCase("PROCEEDS RECEIVED")) && (operatesLike.equalsIgnoreCase("OUTWARD"))){
//                txtRemitFavour1.setVisible(true);
//                lblRemitFavour1.setVisible(true);
                txtInstAmt.setVisible(true);
                lblRemitAmt.setVisible(true);
                tdtRemitInstDate.setVisible(true);
                lblRemitInst.setVisible(true);
            }
         //   System.out.println("strChkClear 11====="+strChkClear);
         //   if(strChkClear.equals("Y"))
         //         cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("CASH"));
         //   else
         //     cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("TRANSFER"));
            System.out.println("c22222222222222===="+cboTranstype.getSelectedItem());
        }else if(activity.equalsIgnoreCase("DISHONOUR") || activity.equalsIgnoreCase("CLOSURE")){//babu1ff
            
            panRemitList.setVisible(true);
            cboRemitProdID.setVisible(false);
            cboRemitCity.setVisible(false);
            cboRemitDraweeBank.setVisible(false);
            cboRemitBranchCode.setVisible(false);
            lblRemitProdID.setVisible(false);
            lblRemitCity.setVisible(false);
            lblRemitDraweeBank.setVisible(false);
            lblRemitBranchCode.setVisible(false);
            
            txtInstAmt.setVisible(false);
            txtRemitFavour.setVisible(true);
            tdtRemitInstDate.setVisible(false);
            lblRemitAmt.setVisible(false);
            lblRemitFavour.setVisible(true);
            lblRemitInst.setVisible(false);
            lblInstNo.setVisible(false);
            txtInst2.setVisible(false);
            txtInst1.setVisible(false);
            cboTranstype.setVisible(true);
            lblTranstype.setVisible(true);
            txtRemitFavour1.setVisible(true);
            lblRemitFavour1.setVisible(true);
            if(activity.equalsIgnoreCase("DISHONOUR"))
                lblRemitFavour1.setText("Dishonour Remarks");
            else
                lblRemitFavour1.setText("Closure Remarks");
              System.out.println("strChkClear222===="+strChkClear);
         //   if(strChkClear.equals("Y"))
        //          cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("CASH"));
        //     else
            
        ///    cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("TRANSFER"));
        //    System.out.println("c11111111111111111===="+cboTranstype.getSelectedItem());
        }else{
            
            panRemitList.setVisible(false);
             cboTranstype.setVisible(false);
            lblTranstype.setVisible(false);
        }
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if((activity.equalsIgnoreCase("REALIZE"))){
                String sendingTo ="";
                sendingTo = observable.getCboReceivedFrom();
                if(sendingTo.equals("Other Bank")){//babu1
                    cboRemitProdID.setSelectedItem(observable.getCbmRemitProdID().getDataForKey("OBADV"));
                }else{
                    cboRemitProdID.setSelectedItem(observable.getCbmRemitProdID().getDataForKey("IBR"));
                }
                cboRemitProdID.setEnabled(false);
             //   txtInstAmt.setText("");
            }
            else if((activity.equalsIgnoreCase("DISHONOUR"))){
                  String sendingTo ="";
                sendingTo = observable.getCboReceivedFrom();
                if(sendingTo.equals("Other Bank")){//babu1
                    cboRemitProdID.setSelectedItem(observable.getCbmRemitProdID().getDataForKey("OBADV"));
                }else{
                    cboRemitProdID.setSelectedItem(observable.getCbmRemitProdID().getDataForKey("IBR"));
                }
            }
            
        }
        
        if(activity.equalsIgnoreCase("REALIZE") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            //ClientUtil.enableDisable(panProductDetails,true);
            btnAccountNo1.setEnabled(true);
            txtAccountNo1.setEnabled(true);
        }
        if(activity.equalsIgnoreCase("DISHONOUR") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                && operatesLike.equalsIgnoreCase("INWARD")){
            txtRemitFavour.setVisible(false);
            txtRemitFavour.setText("0.00");
            lblRemitFavour.setVisible(false);
            //lblRemitFavour1.setVisible(false);
            //txtRemitFavour1.setVisible(false);
            lblRemitFavour1.setVisible(true);
            lblRemitFavour1.setText("Dishonour Remarks");
            txtRemitFavour1.setVisible(true);
            chkClering.setVisible(false);
        }
        
    }//GEN-LAST:event_cboActivitiesActionPerformed
  
    private void cboRemitDraweeBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRemitDraweeBankActionPerformed
        // TODO add your handling code here:
        setValuesFor(productId,null,bankCode,null,null);
        String city = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitCity).getModel())).getKeyForSelected());
        if(city.length()>0 && bankCode.length()>0){
            if(lblOperatesLikeValue.getText().equals("OUTWARD")){
                observable.populateBranchCode(city,bankCode);
            }else{
                observable.populateBranchCodeForInward(city,bankCode);
            }
            cboRemitBranchCode.setModel(observable.getCbmRemitBranchCode());
        }else if(productId.length()==0 || bankCode.length()==0){
            cboRemitBranchCode.setModel(new ComboBoxModel());
        }
//         txtBankCode.setText(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
         fillBankBranchName(bankCode,txtBankCode.getText(),lblDrawBnk, "getBankName");
    }//GEN-LAST:event_cboRemitDraweeBankActionPerformed
    
    private void cboRemitCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRemitCityActionPerformed
        // TODO add your handling code here:
        if(lblOperatesLikeValue.getText().equals("OUTWARD")){
            if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)||(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) ||
            (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) || (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) ||
            (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)){
                String city = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitCity).getModel())).getKeyForSelected());
                if(city.length()>0){
                    ComboBoxModel objDepModel = new ComboBoxModel();
                    objDepModel.addKeyAndElement(txtDraweeBankCode.getText(), txtDraweeBankCode.getText());
                    cboRemitDraweeBank.setModel(objDepModel);
                    cboRemitDraweeBank.setSelectedItem(((ComboBoxModel)cboRemitDraweeBank.getModel()).getDataForKey(observable.getCboRemitDraweeBank()));
                    observable.setCbmRemitDraweeBank(objDepModel);
                }else {
                    cboRemitDraweeBank.setModel(new ComboBoxModel());
                    cboRemitBranchCode.setModel(new ComboBoxModel());
                }
            }
        }
        if(lblOperatesLikeValue.getText().equals("INWARD")){
            if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)||(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) ||
            (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) || (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) ||
            (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)){
                String city = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitCity).getModel())).getKeyForSelected());
                if(city.length()>0){
                    setValuesFor(productId,null,null,null,null);
                    if(productId.length()>0) {
                        observable.populateDraweeBrank(productId,city);
                        cboRemitDraweeBank.setModel(observable.getCbmRemitDraweeBank());
                        cboRemitBranchCode.setModel(new ComboBoxModel());
                    }else if(observable.getCboRemitProdID().length() == 0){
                        cboRemitDraweeBank.setModel(new ComboBoxModel());
                        cboRemitBranchCode.setModel(new ComboBoxModel());
                    }
                }else {
                    cboRemitDraweeBank.setModel(new ComboBoxModel());
                    cboRemitBranchCode.setModel(new ComboBoxModel());
                }
            }
        }
    }//GEN-LAST:event_cboRemitCityActionPerformed
    
    private void cboRemitProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRemitProdIDActionPerformed
        // TODO add your handling code here:
     
        String activity = "";
        String sendingTo = "";
        String operatesLike = "";
        String drawBank = "";
        String bankCode = "";
        operatesLike = lblOperatesLikeValue.getText();
        observable.setCboActivities(CommonUtil.convertObjToStr(cboActivities.getSelectedItem()));
        activity = observable.getCboActivities();
        observable.populateCity();
        cboRemitCity.setModel(observable.getCbmRemitCity());
        String city = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitCity).getModel())).getKeyForSelected());
        observable.setCboRemitProdID(CommonUtil.convertObjToStr(cboRemitProdID.getSelectedItem()));
        productId = observable.getCboRemitProdID();
        System.out.println("##### Product ID : " + productId);
        setValuesFor(productId,null,null,null,null);
        if( productId.length() > 0){
            if((activity.equalsIgnoreCase("REALIZE")) || (activity.equalsIgnoreCase("PROCEEDS RECEIVED"))){
                
                if(operatesLike.equalsIgnoreCase("OUTWARD")){
//                    String drawBank="", bankCode="";  //This line added by Rajesh
                    observable.setCboRemitProdID(CommonUtil.convertObjToStr(cboRemitProdID.getSelectedItem()));
                    System.out.println("##### Product ID : " + productId);
                    setValuesFor(productId,null,null,null,null);
                    if( productId.length() > 0){
                        cboRemitCity.setModel(observable.getCbmRemitCity());
                        if(productId.equals("PO")) {
                            enableDisableRemitPan(true);
                            txtInst1.setVisible(true);
                            System.out.println("City : " + city + "  Drawee Bank : " + drawBank + "  Branch Code : " + bankCode);
                            lblInstNo.setText(resourceBundle.getString("lblInstNo"));
                        }else if(productId.equals("IBR") || productId.equals("OBADV")){
                            enableDisableRemitPan(false);
                            //txtInstAmt.setText(txtInstrumentAmount.getText());
                            tdtRemitInstDate.setDateValue(tdtInstrumentDate.getDateValue());
                        }
                        else {
                            enableDisableRemitPan(true);
                            txtInst1.setVisible(true);
                            cboRemitCity.setEnabled(true);
                            cboRemitCity.setEditable(true);
                            cboRemitDraweeBank.setEnabled(true);
                            cboRemitDraweeBank.setEditable(true);
                            cboRemitBranchCode.setEnabled(true);
                            cboRemitBranchCode.setEditable(true);
                            lblInstNo.setText(resourceBundle.getString("lblInstNo"));
                        }
                    }else if(observable.getCboProductId().length() == 0){
                        
                    }
                }
                if(operatesLike.equalsIgnoreCase("INWARD")){
                    if((activity.equalsIgnoreCase("REALIZE")) && (operatesLike.equalsIgnoreCase("INWARD"))){
                        txtRemitFavour1.setVisible(true);
                        lblRemitFavour1.setVisible(true);
                        txtInst2.setVisible(false);
                        txtInst1.setVisible(false);
                        lblInstNo.setVisible(false);
                        operatesLike = null;
                    }
                    observable.populateCityforInward();
                    cboRemitCity.setModel(observable.getCbmRemitCity());
//                    cboRemitCity.setSelectedItem(cboRemitBranchCode.getItemAt(1));
                    observable.populateDraweeBrank(productId,city);
                    cboRemitDraweeBank.setModel(observable.getCbmRemitDraweeBank());
                    if(cboRemitDraweeBank.getItemCount()>=1) {
//                        cboRemitDraweeBank.setSelectedItem(cboRemitDraweeBank.getItemAt(1));
                        drawBank=CommonUtil.convertObjToStr(observable.getCbmRemitDraweeBank().getKey(1));
                    }
                    observable.populateBranchCodeForInward(productId, drawBank);
                    cboRemitBranchCode.setModel(observable.getCbmRemitBranchCode());
                    if(cboRemitBranchCode.getItemCount()>=1) {
//                        cboRemitBranchCode.setSelectedItem(cboRemitBranchCode.getItemAt(1));
                        bankCode=CommonUtil.convertObjToStr(cboRemitBranchCode.getSelectedItem());
                    }
                }
            }
        }
        
    }//GEN-LAST:event_cboRemitProdIDActionPerformed
    private  void enableDisableRemitPan(boolean value){
        cboRemitCity.setVisible(value);
        cboRemitDraweeBank.setVisible(value);
        cboRemitBranchCode.setVisible(value);
//        txtRemitFavour.setVisible(value);
//        txtInstAmt.setVisible(value);
//        panBillTenor1.setVisible(value);
        lblRemitCity.setVisible(value);
        lblRemitDraweeBank.setVisible(value);
        lblRemitBranchCode.setVisible(value);
//        lblRemitFavour.setVisible(value);
//        lblRemitAmt.setVisible(value);
//        lblInstNo.setVisible(value);
        txtInst1.setVisible(false);
        txtInst2.setVisible(true);
        lblInstNo.setText("Advice No.");
    }
    private void setValuesFor(String productId, String category, String bankCode, String branchCode, String amount) {
        if( productId != null ){
            this.productId = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitProdID).getModel())).getKeyForSelected());
        }
        //        if( category != null ){
        //            this.category = CommonUtil.convertObjToStr((((ComboBoxModel)(cboCategory).getModel())).getKeyForSelected());
        //        }
        if( bankCode != null ){
            this.bankCode = CommonUtil.convertObjToStr((((ComboBoxModel)(cboRemitDraweeBank).getModel())).getKeyForSelected());
        }
        //        if( branchCode != null ){
        //            this.branchCode = CommonUtil.convertObjToStr((((ComboBoxModel)(cboBranchCode).getModel())).getKeyForSelected());
        //        }
        //        if( amount != null ){
        //            this.amount = CommonUtil.convertObjToStr(txtAmt.getText());
        //        }
    }
    private void tdtHundiDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtHundiDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtHundiDate);
    }//GEN-LAST:event_tdtHundiDateFocusLost
    
    private void tdtAcceptanceDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAcceptanceDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtAcceptanceDate);
    }//GEN-LAST:event_tdtAcceptanceDateFocusLost
    
    private void cboBillTenorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBillTenorFocusLost
        // TODO add your handling code here:
        if(cboBillTenor.getSelectedIndex()!=0){
            ClientUtil.validPeriodMaxLength(txtBillTenor,CommonUtil.convertObjToStr(observable.getCbmBillTenor().getKeyForSelected()));
        }
        
    }//GEN-LAST:event_cboBillTenorFocusLost
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
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
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //btnNew.setEnabled(false);
       // btnSave.setEnabled(false);
        //btnEdit.setEnabled(false);
        //btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        strChkClear="N";
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
//        transDetails.setTransDetails(null,null,null);
        settings();
//        panOverdueRateBills8.setVisible(false);
//        lblRateForDelay.setVisible(false);
        lblActRefNo.setText("");
        lblCurStatus.setText("");
        lblDrawBnk.setText("");
        lblDrawBran.setText("");
        panRemitList.setVisible(false);
        observable.clearData();
        txtCreatDt.setText("");
        cancelVisible(false);
        clearRemitTab();
        btnView.setEnabled(true);
        observable.billRatesClear();
         btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnDraweeBankCode.setEnabled(false);
        btnDraweeBranchName.setEnabled(false);
        txtNextAccNo.setText("");
        System.out.println("ACC numm555===="+txtNextAccNo.getText());
        txtNextAccNo.setEnabled(false);
//        cboActivities.setSelectedItem("");
//        observable.makeComboBoxKeyValuesNull();
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable(); 
        }
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }

         cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("TRANSFER"));
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void cancelVisible(boolean flag){
        lblCurStatus1.setVisible(flag);
        lblCurStatus.setVisible(flag);
        lblLodgementId.setVisible(flag);
        txtLodgementId.setVisible(flag);
        lblTranstype.setVisible(flag);
        cboTranstype.setVisible(flag);
        lblProductID.setVisible(flag);
        cboProductID.setVisible(flag);
        lblAccountNum.setVisible(flag);
        panAccountNo1.setVisible(flag);
        lblRateForDelay.setVisible(flag);
        panOverdueRateBills8.setVisible(flag);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setHelpBtnEnableDisable(false);
        setBranchBtnEnableDisable(false);
        setOtherBranchBankBtnEnableDisable(false);
        callView(ClientConstants.ACTION_STATUS[3]);
//           if(CommonUtil.convertObjToStr(lblCurStatus.getText()).equals("PROCEEDS_RECEIVED")){
//             ClientUtil.enableDisable(panStdInstructions,false);
//             ClientUtil.enableDisable(panOperations,false);
//        }
         btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
//        setHelpBtnEnableDisable(false);
        cboInstrumentType.setEnabled(false);
        observable.existingData();
        btnInstNew.setEnabled(true);
//        if(CommonUtil.convertObjToStr(lblCurStatus.getText()).equals("PROCEEDS_RECEIVED")){
//             ClientUtil.enableDisable(panStdInstructions,false);
//             ClientUtil.enableDisable(panOperations,false);
//        }
         btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnDraweeBankCode.setEnabled(true);
        btnDraweeBranchName.setEnabled(true);
//        ClientUtil.enableDisable(panStdInstructions,false);
    //    txtBillsNo.setEnabled(false);
          txtNextAccNo.setEnabled(false);
         // btnInstDelete.setVisible(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        String activity = "";
        observable.setCboActivities(CommonUtil.convertObjToStr(cboActivities.getSelectedItem()));
        activity = observable.getCboActivities();
        if(activity.equalsIgnoreCase("REALIZE") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
////            if(txtAccountNo1.getText().length()<=0){
////                ClientUtil.showMessageWindow("Please Enter Account Number !!!");
////                return;
////            }
        }
        savePerformed();
        if(observable.getProxyReturnMap() != null){
            displayTransDetail(observable.getProxyReturnMap());// nithya
            try {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print Advice?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    HashMap returnMap = observable.getProxyReturnMap();
                    System.out.println("returnMap" + returnMap);
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("LodgeID", CommonUtil.convertObjToStr(returnMap.get("LODGEMENT_ID")));
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("BillsAdvice");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
//        lblCurStatus.setText("");
//        txtCreatDt.setText("");    
    }//GEN-LAST:event_btnSaveActionPerformed
   
    private void tblInstructionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstructionMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblInstructionMouseExited
    
    private void cboBillsTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBillsTypeActionPerformed
        // TODO add your handling code here:
        //Gets the Key for Selected item and then fill up label lblOperatesLikeValue **/
//        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
        if(cboBillsType.getSelectedIndex()!=0){
            String prodID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboBillsType).getModel())).getKeyForSelected());
            String lodgeID = CommonUtil.convertObjToStr(txtLodgementId.getText());
            tdtRemittedDt.setDateValue(DateUtil.getStringDate(currDt));
            lblOperatesLikeValue.setText(observable.getOperatesLike(CommonUtil.convertObjToStr(observable.getCbmBillsType().getKeyForSelected())));
            if(lblOperatesLikeValue.getText().equalsIgnoreCase("INWARD")){
                lblReceivedFrom.setText("Received From");
            }else{
                lblReceivedFrom.setText("Sending To");
            }
            observable.setCboBillsType(CommonUtil.convertObjToStr(cboBillsType.getSelectedItem()));
//             if (cboRegType.getSelectedIndex() > 0) {
//              if(observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT){
            String billsType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboBillsType.getModel())).getKeyForSelected());
            System.out.println("***************"+billsType);
            System.out.println("***********"+observable.getSubRegType());
//             observable.setCombo();
                if (observable.getCboBillsType().length() > 0){
                    observable.setBillsType();
                }else{
                    observable.setStdInstructionAsBlank();
                }
//            observable.setInstructionValue(billsType);
            cboStdInstruction.setModel(observable.getCbmStdInstruction());
//            cboStdInstruction.setSelectedItem(observable.getCboStdInstruction());
            if((observable.getSubRegType().equals("ICC")) && (lblOperatesLikeValue.getText().equalsIgnoreCase("OUTWARD"))){
//                 panOverdueRateBills8.setVisible(true);
//                 lblRateForDelay.setVisible(true);
//                 lblRateForDelay.setText("Interest Rate Applied");
            }else{
                panOverdueRateBills8.setVisible(false);
                lblRateForDelay.setVisible(false);
                lblRateForDelay.setText(resourceBundle.getString("lblRateForDelay"));
            }
            
            if(((observable.getSubRegType().equals("CPD")) && (lblOperatesLikeValue.getText().equalsIgnoreCase("OUTWARD")))){
//                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                cboProductID.setVisible(true);
                lblProductID.setVisible(true);
                panAccountNo1.setVisible(true);
                lblAccountNum.setVisible(true);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    btnAccountNum.setEnabled(true);
                }
                observable.setProductID();
                cboProductID.setModel(observable.getCbmProductID());
                cboProductID.setSelectedItem(observable.getCboProductID());
//                observable.setBillRates(observable.getSubRegType(),prodID);
//                updateBillRates();
//                }

            }else{
               cboProductID.setVisible(false);
                lblProductID.setVisible(false);
                panAccountNo1.setVisible(false);
                lblAccountNum.setVisible(false);
            }
            if(lblOperatesLikeValue.getText().equalsIgnoreCase("OUTWARD")){
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                observable.setBillRates(observable.getSubRegType(),prodID,lodgeID);
                updateBillRates();
                ClientUtil.enableDisable(panRRLRARPPDetails1,true);
                enableDisableBillRates(false);
                if(!observable.getSubRegType().equals("CPD")){
//                    panIntDays.setEnabled(false);
                    txtIntDays.setEnabled(false);
                    cboIntDays.setEnabled(false);
                    txtRateForDelay1.setEnabled(false);
                }
                if(observable.getSubRegType().equals("CPD") || observable.getSubRegType().equals("ICC")){
                    cRadio_ICC_Yes.setSelected(true);
                }
                if(cRadio_ICC_Yes.isSelected()){
                     panOverdueRateBills8.setVisible(true);
                     lblRateForDelay.setVisible(true);
                     lblRateForDelay.setText("Interest Rate Applied");
                }else{
                    panOverdueRateBills8.setVisible(false);
                     lblRateForDelay.setVisible(false);
                }
                }else{
                     observable.setBillRates(observable.getSubRegType(),prodID,lodgeID);
                updateBillRates();
                ClientUtil.enableDisable(panRRLRARPPDetails1,true);
                enableDisableBillRates(false);
                if(!observable.getSubRegType().equals("CPD")){
//                    panIntDays.setEnabled(false);
                    txtIntDays.setEnabled(false);
                    cboIntDays.setEnabled(false);
                    txtRateForDelay1.setEnabled(false);
                }
                }
            }else{
                clearBillRates();
                ClientUtil.enableDisable(panRRLRARPPDetails1,false);
            }
            
        }
//        }
        
       // generateID();
        HashMap accNoMap=generateID();
       txtNextAccNo.setText(generateBillsNoBranchAc());//(String) accNoMap.get(CommonConstants.DATA));
       System.out.println("ACC numm111===="+txtNextAccNo.getText());
       if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
           txtNextAccNo.setEditable(true);
           txtNextAccNo.setEnabled(true);
       }
       else
       {
            txtNextAccNo.setEditable(false);
            txtNextAccNo.setEnabled(false);
       }
       if(lblOperatesLikeValue.getText().equals("INWARD")){
            cboRemitProdID.setVisible(false);
            cboRemitCity.setVisible(false);
            cboRemitDraweeBank.setVisible(false);
            cboRemitBranchCode.setVisible(false);
            lblRemitProdID.setVisible(false);
            lblRemitCity.setVisible(false);
            lblRemitDraweeBank.setVisible(false);
            lblRemitBranchCode.setVisible(false);
            lblInstNo.setVisible(false);
            txtInst2.setVisible(false);
            txtInst1.setVisible(false);
            txtRemitFavour1.setVisible(false);
            lblRemitFavour1.setVisible(false);
            txtRemitFavour.setVisible(false);
            lblRemitFavour.setVisible(false);   
            tdtRemitInstDate.setVisible(false);
            lblRemitInst.setVisible(false);
        } 
    }//GEN-LAST:event_cboBillsTypeActionPerformed
   public String generateBillsNoBranchAc()
   {
       String billsNo="",strPrefix="",nextAccno="";
       HashMap hash = null;
       HashMap where = new HashMap();
        String billsType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboBillsType.getModel())).getKeyForSelected());
            System.out.println("****1111***********"+billsType);
       where.put("BILLS_TYPE",billsType);
        where.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
       List list = (List) ClientUtil.executeQuery("getBillsId", where);
         if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                 if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) strPrefix = "";
                }
              nextAccno=   (String) hash.get("NEXT_AC_NO");
              billsNo=strPrefix+nextAccno;
         }
            System.out.println("****billsNo*****"+billsNo);
       return billsNo;
   }
   
    public HashMap generateID() {
            HashMap hash = null,result=null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "LODGEMENT_ID"); //Here u have to pass LODGEMENT_ID or something else
            List list = null;
//            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) ClientUtil.executeQuery(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix="", strLen="";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) strPrefix = "";
                }

                // Maximum Length for the ID
                int len=10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) len = 10;
                    else len = Integer.parseInt(strLen.trim());
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));
                System.out.println("newID"+newID);
                long d=(long)Double.parseDouble(newID)+1;
                System.out.println("d.."+d);
                newID="";
                newID=""+d;
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);                
                result = new HashMap();
                result.put(CommonConstants.DATA, genID);
            }
        }catch(Exception e) {
              e.printStackTrace();
        }
        return result;
       }
    
    
    private void updateBillRates(){
         txtRateForDelay1.setText(observable.getTxtRateForDelay1());
                txtIntDays.setText(observable.getTxtIntDays());
                cboIntDays.setSelectedItem(observable.getCboIntDays());
                txtDiscountRateBills.setText(observable.getTxtDiscountRateBills());
                txtOverdueRateBills.setText(observable.getTxtOverdueRateBills());
                txtRateForCBP.setText(observable.getTxtRateForCBP());
                txtAtParLimit.setText(observable.getTxtAtParLimit());
                txtCleanBills.setText(observable.getTxtCleanBills());
                txtTransitPeriod.setText(observable.getTxtTransitPeriod());
                cboTransitPeriod.setSelectedItem(observable.getCboTransitPeriod());
                txtDefaultPostage.setText(observable.getTxtDefaultPostage());
                if(observable.isCRadio_ICC_Yes()){
                    cRadio_ICC_Yes.setSelected(true);
                }else{
                    cRadio_ICC_No.setSelected(true);
                }
    }
    private void clearBillRates(){
         txtRateForDelay1.setText("");
                txtIntDays.setText("");
                cboIntDays.setSelectedItem("");
                txtDiscountRateBills.setText("");
                txtOverdueRateBills.setText("");
                txtRateForCBP.setText("");
                txtAtParLimit.setText("");
                txtCleanBills.setText("");
                txtTransitPeriod.setText("");
                cboTransitPeriod.setSelectedItem("");
                txtDefaultPostage.setText("");
    }
    private void enableDisableBillRates(boolean flag){
         txtRateForDelay1.setEnabled(!flag);
                txtIntDays.setEnabled(!flag);
                cboIntDays.setEnabled(!flag);
                txtDiscountRateBills.setEnabled(flag);
                txtOverdueRateBills.setEnabled(flag);
                txtRateForCBP.setEnabled(flag);
                txtAtParLimit.setEnabled(flag);
                txtCleanBills.setEnabled(flag);
                txtTransitPeriod.setEnabled(!flag);
                cboTransitPeriod.setEnabled(!flag);
                txtDefaultPostage.setEnabled(flag);
    }
    private void btnInstDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstDeleteActionPerformed
        // TODO add your handling code here:
        selRow=0;
        updateOBFields();
        observable.deleteSelectedRow(selectedRow);
        btnInstDelete.setEnabled(false);
        btnInstSave.setEnabled(false);
    }//GEN-LAST:event_btnInstDeleteActionPerformed
    
    private void tblInstructionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstructionMousePressed
        // TODO add your handling code here:
        enableAccountMappingFields(true);
        enableInstructionFields(true);
        txtInstAmt.setText("");
        txtRemitFavour.setText("");
        selRow=-1;
        selectedRow = -1;
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            selectedData = 1;
            selectedRow = tblInstruction.getSelectedRow();
            ClientUtil.clearAll(panStdInstructions);
            updateOBFields();
            cboProductId1.setModel(observable.getCbmProductId());
            observable.populateSelectedRow(tblInstruction.getSelectedRow());
            setPanOperationsEnable(true);
            //ClientUtil.enableDisable(panStdInstructions,true);
            //cboStdInstruction.setEnabled(false);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                //tblInstruction.getModel().removeTableModelListener(tblData);
                //observable.deleteSelectedRow(selectedRow);
            }
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            setPanOperationsEnable(false);
        }
        if(viewType.equals(AUTHORIZE)){
            setPanOperationsEnable(false);
            ClientUtil.enableDisable(panStdInstructions,false);
            btnBranchCode.setEnabled(false);
        }
        if(cboStdInstruction.getSelectedItem().toString().equals("Bank Charges"))
        {
            txtStdInstruction.setEnabled(false);
        }
    }//GEN-LAST:event_tblInstructionMousePressed
   
    
    private void btnInstSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstSaveActionPerformed
        // TODO add your handling code here:
        boolean existFlag = false;
        String newChrg=cboStdInstruction.getSelectedItem().toString();
        String newC=((ComboBoxModel)cboStdInstruction.getModel()).getKeyForSelected().toString();
        String selectedActNo = txtAccountNo1.getText();
        System.out.println("newChrgnewChrg===="+newChrg);
        System.out.println("newCnewCnewC"+newC);
        int tblChrgCount=tblInstruction.getRowCount();
        System.out.println("tblChrgCount===="+tblChrgCount);
        if(selRow!=-1 && btnNew1==1)
        {
        if(tblChrgCount>0)
        {          
          for(int i=0;i<tblChrgCount;i++)  
          {
            String chrg_inst= CommonUtil.convertObjToStr(tblInstruction.getValueAt(i, 1));
            String tblActNo = CommonUtil.convertObjToStr(tblInstruction.getValueAt(i, 4));
            System.out.println("chrg_inst=="+chrg_inst);
            if(newC.equals(chrg_inst) && selectedActNo.equalsIgnoreCase(tblActNo))
            {
              ClientUtil.showAlertWindow(chrg_inst+" already inserted for Act :" + tblActNo);
              btnNew1=0;
              selRow=0;
              cboStdInstruction.setSelectedIndex(0);
              txtAmount.setText("");
              txtServiceTax.setText("");
              cboStdInstruction.setEnabled(false);
              txtAmount.setEnabled(false);
              txtServiceTax.setEnabled(false);
              return;
            }
          }
        }
        }
        
        updateOBFields();
        String mandatoryMessage = new String();
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if(cboProductType1.getSelectedItem().equals("") || cboProductType1.getSelectedItem() == null){
            mandatoryMessage = mandatoryMessage + "Please select product type\n";
        }
        if(cboProductId1.getSelectedItem().equals("") || cboProductId1.getSelectedItem() == null){
           mandatoryMessage = mandatoryMessage + "Please select product id\n";
        }
        if(txtSplitAmnt.getText().length() == 0){
           mandatoryMessage = mandatoryMessage + "Please enter individual amount\n"; 
        }
        if(txtAccountNo1.getText().length() == 0){
           mandatoryMessage = mandatoryMessage + "Please select account no\n"; 
        }
        
        //valistaTotalBillAmount();
        
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            if (tblChrgCount > 0) {
                for (int i = 0; i < tblChrgCount; i++) {
                    String chrg_inst = CommonUtil.convertObjToStr(tblInstruction.getValueAt(i, 1));
                    String tblActNo = CommonUtil.convertObjToStr(tblInstruction.getValueAt(i, 4));
                    System.out.println("chrg_inst==" + chrg_inst);
                    if ( (newC.equals(chrg_inst) || (chrg_inst.equalsIgnoreCase("")|| chrg_inst == null) ) && selectedActNo.equalsIgnoreCase(tblActNo)) {
                        selectedData = 1;
                        selectedRow = i;                       
                        existFlag = true;
                        break;
                    }
                }
                if(!existFlag){                    
                        addRow();
                }else{
                  observable.setTableValueAt(selectedRow);                   
                }
            }else{
                addRow();
            }
          
        }
        
        btnNew1=0;
		
       if(lblOperatesLikeValue.getText().equalsIgnoreCase("INWARD")){
           lblServiceTaxval.setText("0.00");
           observable.setServiceTax_Map(null);
           observable.setLblServiceTaxval("0.00");
       }
      //populateAccountDetails();
       
    }//GEN-LAST:event_btnInstSaveActionPerformed
 
    private void btnInstNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstNewActionPerformed
        // .settTODO add your handling code here:
        enableAccountMappingFields(true);
        enableInstructionFields(true);
        updateOBFields();
        selectedData = 0;
        observable.resetInstructions();
        observable.resetAccountMappingFields();
        btnInstSave.setEnabled(true);
        btnNew1 = 1;
        selRow=0;
    }//GEN-LAST:event_btnInstNewActionPerformed
    
   private void enableAccountMappingFields(boolean yesNo){
        cboProductType1.setEnabled(yesNo);
        cboProductId1.setEnabled(yesNo);
        txtSplitAmnt.setEnabled(yesNo);
        txtAccountNo1.setEnabled(yesNo);
        btnAccountNo1.setEnabled(yesNo);
        txtReference1.setEnabled(yesNo);        
////        txtSplitAmnt.setText("");
////        txtAccountNo1.setText("");        
////        txtReference1.setText("");
   }
   
   private void enableInstructionFields(boolean yesNo){
       
       txtStdInstruction.setEnabled(yesNo);
       cboStdInstruction.setEnabled(yesNo);
       txtAmount.setEnabled(yesNo);
       txtServiceTax.setEnabled(yesNo);       
      
   }
           
   
    private void cboStdInstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStdInstructionActionPerformed
        // TODO add your handling code here:
        observable.setCboStdInstruction(CommonUtil.convertObjToStr(cboStdInstruction.getSelectedItem()));
        if(observable.getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            txtStdInstruction.setEnabled(true);
            txtStdInstruction.setEditable(true);
        }else{
            txtStdInstruction.setEnabled(false);
            txtStdInstruction.setText("");
            txtStdInstruction.setEditable(false);
        }
    }//GEN-LAST:event_cboStdInstructionActionPerformed
    
    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        // TODO add your handling code here:
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            if(!viewType.equals(AUTHORIZE)){
                setPanEnableDisable();
            }
        }
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed
        
    private void txtOtherBranchCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOtherBranchCodeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtOtherBranchCodeFocusLost
    
    private void txtBankCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankCodeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtBankCodeFocusLost
    
    private void txtBranchCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchCodeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtBranchCodeFocusLost
        
    private void btnOtherBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherBranchCodeActionPerformed
        // TODO add your handling code here:
        callView(QOBB);
    }//GEN-LAST:event_btnOtherBranchCodeActionPerformed
    
    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // TODO add your handling code here:
        callView(QOB);
    }//GEN-LAST:event_btnBankCodeActionPerformed
    
    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // TODO add your handling code here:
        callView(QBM);
    }//GEN-LAST:event_btnBranchCodeActionPerformed
    
    private void cboReceivedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReceivedFromActionPerformed
        // TODO add your handling code here:
        if(cboReceivedFrom.getSelectedIndex()>0){
            if(observable.getCbmReceivedFrom().getKeyForSelected().equals("BRANCH")){
                ClientUtil.enableDisable(panBranchDetails,true);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                    setBranchBtnEnableDisable(true);
                setOtherBranchBankBtnEnableDisable(false);
                ClientUtil.clearAll(panOtherBankDetails);
                lblBankNameValue.setText("");
                lblOtherBranchNameValue.setText("");
                ClientUtil.enableDisable(panOtherDetails, false);
                ClientUtil.enableDisable(panOtherBankDetails, false);
                panBranchDetails.setVisible(true);
                panOtherBankDetails.setVisible(false);
                panOtherDetails.setVisible(false);
            }else if(observable.getCbmReceivedFrom().getKeyForSelected().equals("OTHER_BANK")){
                ClientUtil.enableDisable(panOtherBankDetails,true);
                setBranchBtnEnableDisable(false);
                ClientUtil.clearAll(panBranchDetails);
                lblBranchCodeValue.setText("");
                txtAccountHeadValue.setEnabled(false);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                    setOtherBranchBankBtnEnableDisable(true);
                ClientUtil.enableDisable(panOtherDetails, false);
                clearDraweeDetails();
                panBranchDetails.setVisible(false);
                panOtherBankDetails.setVisible(true);
                panOtherDetails.setVisible(false);
                if(lblOperatesLikeValue.getText().equals("INWARD")){
                   panOtherBankDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Coll.Bank Details"));
                   lblBankCode.setText("Coll.Bank Code"); 
                   lblBankName.setText("Coll.Bank Name");
                   lblOtherBranchCode.setText("Coll.Branch Code");
                    lblOtherBranchName.setText("Coll.Branch Name");
                }else{
                  panOtherBankDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank Details"));
                  lblBankCode.setText("Other Bank Code");  
           lblBankName.setText("Other Bank Name");
           lblOtherBranchCode.setText("Other Branch Code");
            lblOtherBranchName.setText("Other Branch Name");
                }
            }else if(observable.getCbmReceivedFrom().getKeyForSelected().equals("OTHERS")){
                ClientUtil.enableDisable(panBranchBankDetails, false);
                ClientUtil.enableDisable(panOtherDetails, true);
                setBranchBtnEnableDisable(false);
                setOtherBranchBankBtnEnableDisable(false);
                ClientUtil.clearAll(panBranchBankDetails);
                clearDraweeDetails();
                panBranchDetails.setVisible(false);
                panOtherBankDetails.setVisible(false);
                panOtherDetails.setVisible(true);
            }
        }
    }//GEN-LAST:event_cboReceivedFromActionPerformed
    private void clearDraweeDetails(){
        lblBankNameValue.setText("");
        lblOtherBranchNameValue.setText("");
        lblBranchCodeValue.setText("");
        txtSendingTo.setText("");
        txtDraweeBankCode.setText("");
        txtDraweeBranchCode.setText("");
        txtDraweeName.setText("");
        txtDraweeBankNameVal.setText("");
        txtDraweeAddress.setText("");
        cboDraweeCity.setSelectedItem("");
        cboDraweeState.setSelectedItem("");
        cboDraweeCountry.setSelectedItem("");
        txtDraweePinCode.setText("");
    }    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        strChkClear="N";
        observable.resetForm();
        setHelpBtnEnableDisable(true);
//        addingComboValues();
//        observable.setCombo();
//        ComboBoxModel objDepModel = new ComboBoxModel();
//        cboStdInstruction.setModel(objDepModel);
//        objDepModel.addKeyAndElement("LODGEMENT", observable.getCbmActivities().getDataForKey("LODGEMENT"));
//        cboStdInstruction.setSelectedItem(((ComboBoxModel)cboStdInstruction.getModel()).getDataForKey(observable.getCboStdInstruction()));
//        observable.setCbmStdInstruction(objDepModel);
        ClientUtil.enableDisable(panLodgementBIlls, true);
        ClientUtil.enableDisable(panStdInstructions,false);
        ClientUtil.enableDisable(panBranchBankDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
//        observable.setCombo();
//        ComboBoxModel objDepModel = new ComboBoxModel();
//        objDepModel.addKeyAndElement("LODGEMENT", observable.getCbmActivities().getDataForKey("LODGEMENT"));
//        cboActivities.setModel(objDepModel);
//        cboActivities.setSelectedItem(((ComboBoxModel)cboActivities.getModel()).getDataForKey(observable.getCboActivities()));
//        observable.setCbmActivities(objDepModel);
        cboActivities.setSelectedItem(observable.getCbmActivities().getDataForKey("LODGEMENT"));
        cboInstrumentType.setSelectedItem(observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
        cboCustCategory.setSelectedItem(observable.getCbmCustCategory().getDataForKey("GENERAL_CATEGORY"));
        btnInstNew.setEnabled(true);
        btnInstSave.setEnabled(false);
        btnInstDelete.setEnabled(false);
        panOverdueRateBills8.setVisible(false);
        lblRateForDelay.setVisible(false);
        cboActivities.setEnabled(false);
        panRemitList.setVisible(false);
        Date curDt = (Date)currDt.clone();
        txtCreatDt.setText(CommonUtil.convertObjToStr(curDt));
        cancelVisible(false);
        curDt = null;
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnDraweeBankCode.setEnabled(true);
        btnDraweeBranchName.setEnabled(true);
        
        txtNextAccNo.setText(generateBillsNoBranchAc());
        System.out.println("ACC numm222===="+txtNextAccNo.getText());
        txtNextAccNo.setEnabled(true);
 		btnSave.setEnabled(true);
      cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("TRANSFER"));
     
    // cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("CASH"));
//      txtBillsNo.setEnabled(true);
  //      txtBillsNo.setText(generateBillsNoBranchAc());
      
      enableDisableOtherBankChrgFields(false);
      enableAccountMappingFields(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void cboBillsTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBillsTypeFocusLost
        // TODO add your handling code here:
      //  generateID();
        HashMap accNoMap=generateID();
       txtNextAccNo.setText(generateBillsNoBranchAc());//(String) accNoMap.get(CommonConstants.DATA));
       System.out.println("ACC numm333===="+txtNextAccNo.getText());
    }//GEN-LAST:event_cboBillsTypeFocusLost

    private void chkCleringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCleringActionPerformed
        // TODO add your handling code here:
        System.out.println("is selected===================="+chkClering.isSelected());
        if(chkClering.isSelected())
        {
            strChkClear="Y";
             cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("CASH"));
        }
        else
        {
           strChkClear="N";
            cboTranstype.setSelectedItem(observable.getCbmTrantype().getDataForKey("TRANSFER"));
        }   
                   
           
    }//GEN-LAST:event_chkCleringActionPerformed

    private void cboTranstypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTranstypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTranstypeActionPerformed

    private void cboProductType1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductType1ActionPerformed
        // TODO add your handling code here:
        try{
            if(cboProductType1.getSelectedIndex()>0){
                observable.setCboProductType((String)cboProductType1.getSelectedItem());
                observable.getProductIdByType(CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected()));
                cboProductId1.setModel(observable.getCbmProductId());
                String prodType = ((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected().toString();
                if(prodType.equals("GL")){
                    cboProductId1.setEnabled(false);
                    txtAccountNo1.setText("");
                    lblAccountNo1.setText("Account Head Id");
                    btnAccountNo1.setEnabled(true);
                }else{
                    cboProductId1.setEnabled(true);
                    lblAccountNo1.setText("Account No");
                    btnAccountNo1.setEnabled(true);
                    //txtAccountNo.setEnabled(false);
                    txtAccountNo1.setEnabled(true);
                }
            }else{
                cboProductId1.setSelectedItem("");
            }
        }catch(Exception e){
            parseException.logException(e);
        }
    }//GEN-LAST:event_cboProductType1ActionPerformed

    private void cboProductId1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductId1ActionPerformed
        // TODO add your handling code here:
        List chargeList = null;
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        System.out.println("TrueTransactMain.BRANCH_ID>>>"+TrueTransactMain.BRANCH_ID);
        System.out.println("(String) ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()>>>"+(String) ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        whereMap.put("PRODUCT_ID", (String) ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
//        chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap));
//        if (chargeList != null && chargeList.size() > 0) {
//            String accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
//            txtNextAccNo.setText(String.valueOf(accountClosingCharge));
//        }
//        chargeList = null;
    }//GEN-LAST:event_cboProductId1ActionPerformed

    private void btnAccountNo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNo1ActionPerformed
        // TODO add your handling code here:
        
////        TableSorter tblSorter = (TableSorter) tblInstruction.getModel();
////        TableModel tblModel = (TableModel) tblSorter.getModel();
         if(cboProductType1.getSelectedIndex()>0){
               // observable.removeTbmInstructionsRow();
//               for (int i = 0; i < tblInstruction.getRowCount(); i++) {
//                   tblModel.removeRow(i);
//               }
                callView(AN);
        }
    }//GEN-LAST:event_btnAccountNo1ActionPerformed

    private void txtAccountNo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNo1ActionPerformed

    private void txtAccountNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNo1FocusLost
        // TODO add your handling code here:
        // Added for multiple lodgement
        observable.setCboProductType("");
        String ACCOUNTNO = txtAccountNo1.getText();
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType1.getModel()).getKeyForSelected());
        if(prodType!=null && !prodType.equals("GL")){
            if ((!(observable.getCboProductType().length()>0)) && ACCOUNTNO.length()>0) {
                if (observable.checkAcNoWithoutProdType(ACCOUNTNO)){
                    txtAccountNo1.setText(observable.getTxtAccountNo());
                    cboProductId1.setModel(observable.getCbmProductId());
                    setSelectedBranchID(observable.getSelectedBranchID());
                    txtAccountNo1.setEnabled(true);
                }else{
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtAccountNo1.setText("");
                    return;
                }
            }
        }
        if(txtAccountNo1.getText().length()>0){
            observable.setTxtAccountNo(txtAccountNo1.getText());
            lblCustName.setText("");
            viewType = AN;
            HashMap fillMap = new HashMap();
            //String prodType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
            if(prodType.equals("TD")){
                fillMap.put("ACCOUNTNO",txtAccountNo1.getText()+"_1");
                txtAccountNo1.setText(txtAccountNo1.getText()+"_1");
                observable.setTxtAccountNo(txtAccountNo1.getText());                
            }else
                fillMap.put("ACCOUNTNO",txtAccountNo1.getText());
            lblCustName.setText(observable.getCustomerName());            
            fillMap.put("CUSTOMERNAME",lblCustName.getText());
            if(prodType!=null && !prodType.equals("GL")){
                if(lblCustName.getText().length()>0)
                    fillData(fillMap);
                else{
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtAccountNo1.setText("");
                    return;
                }   
            }
        }
    }//GEN-LAST:event_txtAccountNo1FocusLost

    private void txtInstrumentAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstrumentAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstrumentAmountActionPerformed

    private void txtSplitAmntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSplitAmntFocusLost
        // TODO add your handling code here:
       // validateTotalBillAmountExceeds();
    }//GEN-LAST:event_txtSplitAmntFocusLost

    private boolean validateOtherBankCharges(){
        ArrayList selectedRow;
        boolean acctNoFlag = false;
        ArrayList newList = observable.listAfterDuplicateRempval();
        System.out.println("UI newList :: " + newList);
        if (newList != null && newList.size() > 0) {
                for (int i = 0; i < newList.size(); i++) {
                    selectedRow = (ArrayList) (newList.get(i));
                    if(selectedRow.get(8).equals("") || null == selectedRow.get(8)){
                        acctNoFlag = true;
                        break;
                    }
                }
        }       
        
        return acctNoFlag;
    }
    
    private boolean validateTotalBillAmountExceeds() {
        ArrayList selectedRow;
        double splitBillAmount = 0;        
        boolean amountExceedFlag = false;
            double actualBillAmount = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText());
            ArrayList newList = observable.listAfterDuplicateRempval();
            System.out.println("UI newList :: " + newList);
            if (newList != null && newList.size() > 0) {
                for (int i = 0; i < newList.size(); i++) {
                    selectedRow = (ArrayList) (newList.get(i));
                    Double amount = CommonUtil.convertObjToDouble(selectedRow.get(7));
                    splitBillAmount = splitBillAmount + amount;
                }
                //splitBillAmount = splitBillAmount + CommonUtil.convertObjToDouble(txtSplitAmnt.getText()); 4
                if (splitBillAmount > actualBillAmount) {
                    //ClientUtil.showMessageWindow("The amount is greater than total Bill amount :" + CommonUtil.convertObjToDouble(txtInstrumentAmount.getText()));
                    amountExceedFlag = true;
                }
            }
        return(amountExceedFlag);
    }
    
    
    private boolean validateTotalBillAmountLess() {
        ArrayList selectedRow;
        double splitBillAmount = 0;
        boolean lessAmountFlag = false;
            double actualBillAmount = CommonUtil.convertObjToDouble(txtInstrumentAmount.getText());
            ArrayList newList = observable.listAfterDuplicateRempval();
            System.out.println("UI newList :: " + newList);
            if (newList != null && newList.size() > 0) {
                for (int i = 0; i < newList.size(); i++) {
                    selectedRow = (ArrayList) (newList.get(i));
                    Double amount = CommonUtil.convertObjToDouble(selectedRow.get(7));
                    splitBillAmount = splitBillAmount + amount;
                }                
                if (splitBillAmount < actualBillAmount) {                    
                    lessAmountFlag = true;
                }
            }
        return(lessAmountFlag);
    }
    
    private void txtTotalServTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalServTaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalServTaxActionPerformed

    private void txtReference1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReference1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReference1ActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed
    private void addingComboValues(){
//        if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
        observable.setCombo();
            ComboBoxModel objDepModel = new ComboBoxModel();
//            objDepModel.addKeyAndElement("LODGEMENT", observable.getCbmActivities().getDataForKey("LODGEMENT"));
            objDepModel.addKeyAndElement("REALIZE", observable.getCbmActivities().getDataForKey("REALIZE"));
            objDepModel.addKeyAndElement("DISHONOUR", observable.getCbmActivities().getDataForKey("DISHONOUR"));
            objDepModel.addKeyAndElement("PROCEEDS_RECEIVED", observable.getCbmActivities().getDataForKey("PROCEEDS_RECEIVED"));
//            objDepModel.addKeyAndElement("GL", observable.getCbmProductType().getDataForKey("GL"));
//            objDepModel.addKeyAndElement("AD", observable.getCbmProductType().getDataForKey("AD"));
//            objDepModel.addKeyAndElement("RM", "Remittance");
//            objDepModel.addKeyAndElement("RM", observable.getCbmProductType().getDataForKey("RM"));
            cboActivities.setModel(objDepModel);
            cboActivities.setSelectedItem(((ComboBoxModel)cboActivities.getModel()).getDataForKey(observable.getCboActivities()));
            observable.setCbmActivities(objDepModel);
//        }
    } 
    
    private void addingComboValuesForPro(){
//        if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
        observable.setCombo();
            ComboBoxModel objDepModel = new ComboBoxModel();
//            objDepModel.addKeyAndElement("LODGEMENT", observable.getCbmActivities().getDataForKey("LODGEMENT"));
//            objDepModel.addKeyAndElement("REALIZE", observable.getCbmActivities().getDataForKey("REALIZE"));
//            objDepModel.addKeyAndElement("DISHONOUR", observable.getCbmActivities().getDataForKey("DISHONOUR"));
            objDepModel.addKeyAndElement("PROCEEDS_RECEIVED", observable.getCbmActivities().getDataForKey("PROCEEDS_RECEIVED"));
//            objDepModel.addKeyAndElement("GL", observable.getCbmProductType().getDataForKey("GL"));
//            objDepModel.addKeyAndElement("AD", observable.getCbmProductType().getDataForKey("AD"));
//            objDepModel.addKeyAndElement("RM", "Remittance");
//            objDepModel.addKeyAndElement("RM", observable.getCbmProductType().getDataForKey("RM"));
            cboActivities.setModel(objDepModel);
            cboActivities.setSelectedItem(((ComboBoxModel)cboActivities.getModel()).getDataForKey(observable.getCboActivities()));
            observable.setCbmActivities(objDepModel);
//        }
    } 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNo1;
    private com.see.truetransact.uicomponent.CButton btnAccountNum;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDraweeBankCode;
    private com.see.truetransact.uicomponent.CButton btnDraweeBranchName;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInstDelete;
    private com.see.truetransact.uicomponent.CButton btnInstNew;
    private com.see.truetransact.uicomponent.CButton btnInstSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOtherBranchCode;
    private com.see.truetransact.uicomponent.CButton btnParDelete;
    private com.see.truetransact.uicomponent.CButton btnParNew;
    private com.see.truetransact.uicomponent.CButton btnParSave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_ICC_No;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_ICC_Yes;
    private com.see.truetransact.uicomponent.CComboBox cboActivities;
    private com.see.truetransact.uicomponent.CComboBox cboBillTenor;
    private com.see.truetransact.uicomponent.CComboBox cboBillsType;
    private com.see.truetransact.uicomponent.CComboBox cboCustCategory;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeCity;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeState;
    private com.see.truetransact.uicomponent.CComboBox cboInstruction;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboIntDays;
    private com.see.truetransact.uicomponent.CComboBox cboOtherCity;
    private com.see.truetransact.uicomponent.CComboBox cboOtherCountry;
    private com.see.truetransact.uicomponent.CComboBox cboOtherState;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductId1;
    private com.see.truetransact.uicomponent.CComboBox cboProductType1;
    private com.see.truetransact.uicomponent.CComboBox cboReceivedFrom;
    private com.see.truetransact.uicomponent.CComboBox cboRemitBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboRemitCity;
    private com.see.truetransact.uicomponent.CComboBox cboRemitDraweeBank;
    private com.see.truetransact.uicomponent.CComboBox cboRemitProdID;
    private com.see.truetransact.uicomponent.CComboBox cboStdInstruction;
    private com.see.truetransact.uicomponent.CComboBox cboTransitPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboTranstype;
    private com.see.truetransact.uicomponent.CCheckBox chkClering;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAcceptanceDate;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo1;
    private com.see.truetransact.uicomponent.CLabel lblAccountNum;
    private com.see.truetransact.uicomponent.CLabel lblActRefNo;
    private com.see.truetransact.uicomponent.CLabel lblActivities;
    private com.see.truetransact.uicomponent.CLabel lblAdditionalInstruction;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAtParLimit;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBankNameValue;
    private com.see.truetransact.uicomponent.CLabel lblBillAcceptance;
    private com.see.truetransact.uicomponent.CLabel lblBillTenor;
    private com.see.truetransact.uicomponent.CLabel lblBillsType;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeValue;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCleanBillsPurchased;
    private com.see.truetransact.uicomponent.CLabel lblCleanBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblCreatDt;
    private com.see.truetransact.uicomponent.CLabel lblCurStatus;
    private com.see.truetransact.uicomponent.CLabel lblCurStatus1;
    private com.see.truetransact.uicomponent.CLabel lblCustCategory;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per1;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per2;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRateBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRateOfBD;
    private com.see.truetransact.uicomponent.CLabel lblDrawBnk;
    private com.see.truetransact.uicomponent.CLabel lblDrawBran;
    private com.see.truetransact.uicomponent.CLabel lblDraweeAddress;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBankCode;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBankName;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBankNamee;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBranchName;
    private com.see.truetransact.uicomponent.CLabel lblDraweeCity;
    private com.see.truetransact.uicomponent.CLabel lblDraweeCountry;
    private com.see.truetransact.uicomponent.CLabel lblDraweeHundi;
    private com.see.truetransact.uicomponent.CLabel lblDraweeName;
    private com.see.truetransact.uicomponent.CLabel lblDraweeNo;
    private com.see.truetransact.uicomponent.CLabel lblDraweePinCode;
    private com.see.truetransact.uicomponent.CLabel lblDraweeState;
    private com.see.truetransact.uicomponent.CLabel lblDueDate;
    private com.see.truetransact.uicomponent.CLabel lblGoodsAssigned;
    private com.see.truetransact.uicomponent.CLabel lblGoodsValue;
    private com.see.truetransact.uicomponent.CLabel lblHundiAmount;
    private com.see.truetransact.uicomponent.CLabel lblHundiDate;
    private com.see.truetransact.uicomponent.CLabel lblHundiNo;
    private com.see.truetransact.uicomponent.CLabel lblHundiRemarks;
    private com.see.truetransact.uicomponent.CLabel lblInstNo;
    private com.see.truetransact.uicomponent.CLabel lblInstruction;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblIntDays;
    private com.see.truetransact.uicomponent.CLabel lblIntICC;
    private com.see.truetransact.uicomponent.CLabel lblInvoiceAmount;
    private com.see.truetransact.uicomponent.CLabel lblInvoiceDate;
    private com.see.truetransact.uicomponent.CLabel lblInvoiceNumber;
    private com.see.truetransact.uicomponent.CLabel lblLodgementId;
    private com.see.truetransact.uicomponent.CLabel lblMICR;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNxtAccNo;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLike;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLikeValue;
    private com.see.truetransact.uicomponent.CLabel lblOtherAddress;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchName;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchNameValue;
    private com.see.truetransact.uicomponent.CLabel lblOtherCity;
    private com.see.truetransact.uicomponent.CLabel lblOtherCountry;
    private com.see.truetransact.uicomponent.CLabel lblOtherName;
    private com.see.truetransact.uicomponent.CLabel lblOtherPinCode;
    private com.see.truetransact.uicomponent.CLabel lblOverdueInterestForBD;
    private com.see.truetransact.uicomponent.CLabel lblOverdueRateBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblOverdueRateCBP;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblPayable;
    private com.see.truetransact.uicomponent.CLabel lblPayeeName;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductId1;
    private com.see.truetransact.uicomponent.CLabel lblProductType1;
    private com.see.truetransact.uicomponent.CLabel lblRRLRDate;
    private com.see.truetransact.uicomponent.CLabel lblRRLRNumber;
    private com.see.truetransact.uicomponent.CLabel lblRateForCBP_Per;
    private com.see.truetransact.uicomponent.CLabel lblRateForDelay;
    private com.see.truetransact.uicomponent.CLabel lblRateForDelay1;
    private com.see.truetransact.uicomponent.CLabel lblReceivedFrom;
    private com.see.truetransact.uicomponent.CLabel lblReference1;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemitAmt;
    private com.see.truetransact.uicomponent.CLabel lblRemitBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblRemitCity;
    private com.see.truetransact.uicomponent.CLabel lblRemitDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblRemitFavour;
    private com.see.truetransact.uicomponent.CLabel lblRemitFavour1;
    private com.see.truetransact.uicomponent.CLabel lblRemitInst;
    private com.see.truetransact.uicomponent.CLabel lblRemitProdID;
    private com.see.truetransact.uicomponent.CLabel lblRemittedDt;
    private com.see.truetransact.uicomponent.CLabel lblSendingTo;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CTextField lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStdInstruction;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalServTax;
    private com.see.truetransact.uicomponent.CLabel lblTransitPeriod;
    private com.see.truetransact.uicomponent.CLabel lblTransportCompany;
    private com.see.truetransact.uicomponent.CLabel lblTranstype;
    private com.see.truetransact.uicomponent.CMenuBar mbrLodgemntBills;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcceptanceBill;
    private com.see.truetransact.uicomponent.CPanel panAccountNo1;
    private com.see.truetransact.uicomponent.CPanel panBankCode;
    private com.see.truetransact.uicomponent.CPanel panBillTenor;
    private com.see.truetransact.uicomponent.CPanel panBillTenor1;
    private com.see.truetransact.uicomponent.CPanel panBillofExchange;
    private com.see.truetransact.uicomponent.CPanel panBillsType;
    private com.see.truetransact.uicomponent.CPanel panBranchBankDetails;
    private com.see.truetransact.uicomponent.CPanel panBranchCode;
    private com.see.truetransact.uicomponent.CPanel panBranchDetails;
    private com.see.truetransact.uicomponent.CPanel panChequeDetails;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate;
    private com.see.truetransact.uicomponent.CPanel panDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panDraweeDetail;
    private com.see.truetransact.uicomponent.CPanel panDraweeHundi;
    private com.see.truetransact.uicomponent.CPanel panInstEntry;
    private com.see.truetransact.uicomponent.CPanel panInstEntry1;
    private com.see.truetransact.uicomponent.CPanel panInstructions;
    private com.see.truetransact.uicomponent.CPanel panInstructions1;
    private com.see.truetransact.uicomponent.CPanel panInstructions2;
    private com.see.truetransact.uicomponent.CPanel panInstrumentDetails;
    private com.see.truetransact.uicomponent.CPanel panInstrumentType;
    private com.see.truetransact.uicomponent.CPanel panIntDays;
    private com.see.truetransact.uicomponent.CPanel panIntICC;
    private com.see.truetransact.uicomponent.CPanel panIntICC2;
    private com.see.truetransact.uicomponent.CPanel panIntICC3;
    private com.see.truetransact.uicomponent.CPanel panInvoiceDetails;
    private com.see.truetransact.uicomponent.CPanel panLableValues;
    private com.see.truetransact.uicomponent.CPanel panLodgementBIlls;
    private com.see.truetransact.uicomponent.CPanel panLodgementDetails;
    private com.see.truetransact.uicomponent.CPanel panNxtAccNo;
    private com.see.truetransact.uicomponent.CPanel panOperations;
    private com.see.truetransact.uicomponent.CPanel panOperations1;
    private com.see.truetransact.uicomponent.CPanel panOperations3;
    private com.see.truetransact.uicomponent.CPanel panOtherBankDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherBnkChrg;
    private com.see.truetransact.uicomponent.CPanel panOtherBranchCode;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills1;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills2;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills3;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills8;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills9;
    private com.see.truetransact.uicomponent.CPanel panParticulars;
    private com.see.truetransact.uicomponent.CPanel panProductDetails1;
    private com.see.truetransact.uicomponent.CPanel panRRLRARPPDetails;
    private com.see.truetransact.uicomponent.CPanel panRRLRARPPDetails1;
    private com.see.truetransact.uicomponent.CPanel panRate;
    private com.see.truetransact.uicomponent.CPanel panRemitList;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions1;
    private com.see.truetransact.uicomponent.CPanel panTblInstruction;
    private com.see.truetransact.uicomponent.CPanel panTblInstruction1;
    private com.see.truetransact.uicomponent.CPanel panTransitPeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBillAcceptance;
    private com.see.truetransact.uicomponent.CRadioButton rdoBillAcceptance_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBillAcceptance_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDraweeHundi;
    private com.see.truetransact.uicomponent.CRadioButton rdoDraweeHundi_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDraweeHundi_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIntICC;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptDraweeDetails;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructions;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructions1;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructions2;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabLodgementBils;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblInstruction;
    private com.see.truetransact.uicomponent.CTable tblInstruction1;
    private com.see.truetransact.uicomponent.CTable tblInstruction2;
    private com.see.truetransact.uicomponent.CToolBar tbrLodgmentBills;
    private com.see.truetransact.uicomponent.CDateField tdtAcceptanceDate;
    private com.see.truetransact.uicomponent.CDateField tdtDueDate;
    private com.see.truetransact.uicomponent.CDateField tdtHundiDate;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CDateField tdtInvoiceDate;
    private com.see.truetransact.uicomponent.CDateField tdtRRLRDate;
    private com.see.truetransact.uicomponent.CDateField tdtRemitInstDate;
    private com.see.truetransact.uicomponent.CDateField tdtRemittedDt;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadValue;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo1;
    private com.see.truetransact.uicomponent.CTextField txtAccountNum;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtAtParLimit;
    private com.see.truetransact.uicomponent.CTextField txtBankCode;
    private com.see.truetransact.uicomponent.CTextField txtBillTenor;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtCleanBills;
    private com.see.truetransact.uicomponent.CTextField txtCreatDt;
    private com.see.truetransact.uicomponent.CTextField txtDefaultPostage;
    private com.see.truetransact.uicomponent.CTextField txtDiscountRateBills;
    private com.see.truetransact.uicomponent.CTextField txtDraweeAddress;
    private com.see.truetransact.uicomponent.CTextField txtDraweeBankCode;
    private com.see.truetransact.uicomponent.CTextField txtDraweeBankName;
    private com.see.truetransact.uicomponent.CTextField txtDraweeBankNameVal;
    private com.see.truetransact.uicomponent.CTextField txtDraweeBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtDraweeBranchName;
    private com.see.truetransact.uicomponent.CTextField txtDraweeName;
    private com.see.truetransact.uicomponent.CTextField txtDraweeNo;
    private com.see.truetransact.uicomponent.CTextField txtDraweePinCode;
    private com.see.truetransact.uicomponent.CTextField txtGoodsAssigned;
    private com.see.truetransact.uicomponent.CTextField txtGoodsValue;
    private com.see.truetransact.uicomponent.CTextField txtHundiAmount;
    private com.see.truetransact.uicomponent.CTextField txtHundiNo;
    private com.see.truetransact.uicomponent.CTextField txtHundiRemarks;
    private com.see.truetransact.uicomponent.CTextField txtInst1;
    private com.see.truetransact.uicomponent.CTextField txtInst2;
    private com.see.truetransact.uicomponent.CTextField txtInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstPrefix;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo;
    private com.see.truetransact.uicomponent.CTextField txtIntDays;
    private com.see.truetransact.uicomponent.CTextField txtInvoiceAmount;
    private com.see.truetransact.uicomponent.CTextField txtInvoiceNumber;
    private com.see.truetransact.uicomponent.CTextField txtLodgementId;
    private com.see.truetransact.uicomponent.CTextField txtMICR;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtOtherAddress;
    private com.see.truetransact.uicomponent.CTextField txtOtherBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtOtherName;
    private com.see.truetransact.uicomponent.CTextField txtOtherPinCode;
    private com.see.truetransact.uicomponent.CTextField txtOverdueRateBills;
    private com.see.truetransact.uicomponent.CTextField txtPayable;
    private com.see.truetransact.uicomponent.CTextField txtPayeeName;
    private com.see.truetransact.uicomponent.CTextField txtRRLRNumber;
    private com.see.truetransact.uicomponent.CTextField txtRateForCBP;
    private com.see.truetransact.uicomponent.CTextField txtRateForDelay;
    private com.see.truetransact.uicomponent.CTextField txtRateForDelay1;
    private com.see.truetransact.uicomponent.CTextField txtReference1;
    private com.see.truetransact.uicomponent.CTextField txtRemitFavour;
    private com.see.truetransact.uicomponent.CTextField txtRemitFavour1;
    private com.see.truetransact.uicomponent.CTextField txtSendingTo;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtSplitAmnt;
    private com.see.truetransact.uicomponent.CTextField txtStdInstruction;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalServTax;
    private com.see.truetransact.uicomponent.CTextField txtTransitPeriod;
    private com.see.truetransact.uicomponent.CTextField txtTransportCompany;
    // End of variables declaration//GEN-END:variables
    
}
