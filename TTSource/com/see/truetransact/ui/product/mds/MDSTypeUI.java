  /*
 * RemittanceProductUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.product.mds;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author Hemant Modification Lohith R.
 * @modified : Sunil Added Edit Locking - 08-07-2005
 */
public class MDSTypeUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.mds.MDSTypeRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private MDSTypeMRB objMandatoryRB = new MDSTypeMRB();
    private String accountHead = "ACCOUNT HEAD";
    private String accountHeadDesc = "ACCOUNT HEAD DESCRIPTION";
    private String viewType = new String();
    private boolean schemeStDtFlag = false;
    private boolean instAmtFlag = false;
    private boolean updateMode = false;
    final String AUTHORIZE = "Authorize";
    private boolean isFilled = false;
    HashMap mandatoryMap = null;
    MDSTypeOB observable = null;
    int updateTab = -1;
    private boolean tableNoticeChargeClicked = false;
    private String isASpecialScheme = "";
    private Date currDt = null;
    /**
     * Creates new form BeanForm
     */
    public MDSTypeUI() {
        initComponents();
        initForm();
    }

    private void initForm() {
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        observable = new MDSTypeOB();
        setMaxLengths();
        initTableData();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panSchemeDetails, false);
        ClientUtil.enableDisable(panInsSchedule, true);
        ClientUtil.enableDisable(panAccountHead, false);
        ClientUtil.enableDisable(panNoticeCharges, false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), tabMdsType);
        enableDisableButton(false);
        enableDisableNoticeCharge_SaveDelete(false);
        txtInstallments.setEnabled(false);
        txtDay.setEnabled(false);
        txtChittalNumberPattern.setEnabled(false);
        tdtSchemeEndDt.setEnabled(false);
        panCoChittalDetails.setVisible(false);
        suspenseACCNoEnableDisable(false);
        lblPredefinitonInst.setVisible(true);
        panPrintServicesGR5.setVisible(true);
        lblApplicableDivision.setVisible(true);
        panApplicableDivision.setVisible(true);
        lblApplicable1.setVisible(false);
        lblApplicable2.setVisible(false);
        lblApplicable3.setVisible(false);
        lblApplicable4.setVisible(false);
        chkApplicable1.setVisible(false);
        chkApplicable2.setVisible(false);
        chkApplicable3.setVisible(false);
        chkApplicable4.setVisible(false);
        panInsSchedule.setVisible(true);
        disDesc();
        btnCreateHead.setEnabled(false);
    }

    public void disDesc() {
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnBankingHead.setName("btnBankingHead");
        btnBonusPayableHead.setName("btnBonusPayableHead");
        btnBonusReceivableHead.setName("btnBonusReceivableHead");
        btnCancel.setName("btnCancel");
        btnCaseExpensesHead.setName("btnCaseExpensesHead");
        btnClose.setName("btnClose");
        btnCommisionHead.setName("btnCommisionHead");
        btnCopy.setName("btnCopy");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnMiscellaneousHead.setName("btnMiscellaneousHead");
        btnMunnalBonusHead.setName("btnMunnalBonusHead");
        btnMunnalReceiptsHead.setName("btnMunnalReceiptsHead");
        btnNew.setName("btnNew");
        btnNoticeChargesHead.setName("btnNoticeChargesHead");
        btnChargeHead.setName("btnChargeHead");
        btnPaymentHead.setName("btnPaymentHead");
        btnPenalHead.setName("btnPenalHead");
        btnPrint.setName("btnPrint");
        btnReceiptHead.setName("btnReceiptHead");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchedDelete.setName("btnSchedDelete");
        btnSchedNew.setName("btnSchedNew");
        btnSchedSave.setName("btnSchedSave");
        btnSuspenseHead.setName("btnSuspenseHead");
        btnThalayalBonusHead.setName("btnThalayalBonusHead");
        btnThalayalReceiptsHead.setName("btnThalayalReceiptsHead");
        btnView.setName("btnView");
        cboAuctionDay.setName("cboAuctionDay");
        cboInstFreq.setName("cboInstFreq");
        cboInstallmentDay.setName("cboInstallmentDay");
        cboProductId.setName("cboProductId");
        chkApplicable1.setName("chkApplicable1");
        chkApplicable2.setName("chkApplicable2");
        chkApplicable3.setName("chkApplicable3");
        chkApplicable4.setName("chkApplicable4");
        lblAmount.setName("lblAmount");
        lblNoticeType.setName("lblNoticeType");
        lblNoticeChargeAmt.setName("lblNoticeChargeAmt");
        lblPostageAmt.setName("lblPostageAmt");
        lblApplicable1.setName("lblApplicable1");
        lblApplicable2.setName("lblApplicable2");
        lblApplicable3.setName("lblApplicable3");
        lblApplicable4.setName("lblApplicable4");
        lblApplicableDivision.setName("lblApplicableDivision");
        lblAuctionDay.setName("lblAuctionDay");
        lblBankingHead.setName("lblBankingHead");
        lblBonus.setName("lblBonus");
        lblBonusPayableHead.setName("lblBonusPayableHead");
        lblBonusReceivableHead.setName("lblBonusReceivableHead");
        lblCaseExpensesHead.setName("lblCaseExpensesHead");
        lblDiscountHead.setName("lblDiscountHead");
        lblStampAdvanceHead.setName("lblStampAdvanceHead");
        lblARCCostHead.setName("lblARCCostHead");
        lblARCExpenseHead.setName("lblARCExpenseHead");
        lblEACostHead.setName("lblEACostHead");
        lblEAExpenseHead.setName("lblEAExpenseHead");
        lblEPCostHead.setName("lblEPCostHead");
        lblEPExpenseHead.setName("lblEPExpenseHead");
        lblPostageHead.setName("lblPostageHead");
        lblMDSPayableHead.setName("lblMDSPayableHead");
        lblMDSReceivableHead.setName("lblMDSReceivableHead");
        lblSundryReceiptHead.setName("lblSundryReceiptHead");
        lblSundryPaymentHead.setName("lblSundryPaymentHead");
	lblForFeitedHead.setName("lblForFeitedHead");
        lblChittalNumberPattern.setName("lblChittalNumberPattern");
        lblCommisionHead.setName("lblCommisionHead");
        lblInstAmt.setName("lblInstAmt");
        lblInstFreq.setName("lblInstFreq");
        lblInstallmentDay.setName("lblInstallmentDay");
        lblInstallmentDt.setName("lblInstallmentDt");
        lblInstallmentNo.setName("lblInstallmentNo");
        lblInstallmentNumber.setName("lblInstallmentNumber");
        lblMiscellaneousHead.setName("lblMiscellaneousHead");
        lblMsg.setName("lblMsg");
        lblMunnalAllowed.setName("lblMunnalAllowed");
        lblMunnalBonusHead.setName("lblMunnalBonusHead");
        lblMunnalReceiptsHead.setName("lblMunnalReceiptsHead");
        lblNextChittalNumber.setName("lblNextChittalNumber");
        lblNoofAuctions.setName("lblNoofAuctions");
        lblNoofDivision.setName("lblNoofDivision");
        lblNoofDraws.setName("lblNoofDraws");
        lblNoofInst.setName("lblNoofInst");
        lblNoofMemberPer.setName("lblNoofMemberPer");
        lblNoofMemberScheme.setName("lblNoofMemberScheme");
        lblNoticeChargesHead.setName("lblNoticeChargesHead");
        lblChargeHead.setName("lblChargeHead");
        lblPaymentAmount.setName("lblPaymentAmount");
        lblPaymentDone.setName("lblPaymentDone");
        lblPaymentHead.setName("lblPaymentHead");
        lblPenalHead.setName("lblPenalHead");
        lblPredefinitonInst.setName("lblPredefinitonInst");
        lblProductDesc.setName("lblProductDesc");
        lblProductDescVal.setName("lblProductDescVal");
        lblProductId.setName("lblProductId");
        lblReceiptHead.setName("lblReceiptHead");
        lblResolutionNo.setName("lblResolutionNo");
        lblSchemeEndDt.setName("lblSchemeEndDt");
        lblSchemeName.setName("lblSchemeName");
        lblMultipleMembers.setName("lblMultipleMembers");
        rdoMultipleMembers_no.setName("rdoMultipleMembers_no");
        rdoMultipleMembers_yes.setName("rdoMultipleMembers_yes");
        lblNoofCoChittals.setName("lblNoofCoChittals");
        lblNoofCoInstallments.setName("lblNoofCoInstallments");
        lblMaxNoofMemberCoChittals.setName("lblMaxNoofMemberCoChittals");
        lblCoChittalInstAmount.setName("lblCoChittalInstAmount");
        lblSchemeStDt.setName("lblSchemeStDt");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblSuspenseHead.setName("lblSuspenseHead");
        lblThalayalAllowed.setName("lblThalayalAllowed");
        lblThalayalBonusHead.setName("lblThalayalBonusHead");
        lblThalayalReceiptsHead.setName("lblThalayalReceiptsHead");
        lblTotAmtPerDivision.setName("lblTotAmtPerDivision");
        lblTotAmtUnderScheme.setName("lblTotAmtUnderScheme");
        lbllResolutionDate.setName("lbllResolutionDate");
        mbrMain.setName("mbrMain");
        panAccountHead.setName("panAccountHead");
        panAccountHeadDetails.setName("panAccountHeadDetails");
        panApplicableDivision.setName("panApplicableDivision");
        panInsSchedule.setName("panInsSchedule");
        //        panInsideSchemeDetails.setName("panInsideSchemeDetails");
        panInstallSchedule.setName("panInstallSchedule");
        panInstallmentSchedule.setName("panInstallmentSchedule");
        panNumberPattern.setName("panNumberPattern");
        panPrintServicesGR2.setName("panPrintServicesGR2");
        panPrintServicesGR5.setName("panPrintServicesGR5");
        panSchedBtn.setName("panSchedBtn");
        panSchedule.setName("panSchedule");
        panScheduleDetails.setName("panScheduleDetails");
        panScheduleTable.setName("panScheduleTable");
        panSchemeDetails.setName("panSchemeDetails");
        panStatus.setName("panStatus");
        panThalayalAllowed.setName("panThalayalAllowed");
        rdoApplicableDivision_no.setName("rdoApplicableDivision_no");
        rdoApplicableDivision_yes.setName("rdoApplicableDivision_yes");
        rdoMunnalAllowed_Yes.setName("rdoMunnalAllowed_Yes");
        rdoMunnalAllowed_no.setName("rdoMunnalAllowed_no");
        rdoPaymentDone_no.setName("rdoPaymentDone_no");
        rdoPaymentDone_yes.setName("rdoPaymentDone_yes");
        rdoPredefinitonInst_no.setName("rdoPredefinitonInst_no");
        rdoPredefinitonInst_yes.setName("rdoPredefinitonInst_yes");
        rdoThalayalAllowed_no.setName("rdoThalayalAllowed_no");
        rdoThalayalAllowed_yes.setName("rdoThalayalAllowed_yes");
        srpScheduleTable.setName("srpScheduleTable");
        tabMdsType.setName("tabMdsType");
        tblSchedule.setName("tblSchedule");
        tdtInstallmentDt.setName("tdtInstallmentDt");
        tdtSchemeEndDt.setName("tdtSchemeEndDt");
        tdtSchemeStDt.setName("tdtSchemeStDt");
        tdtlResolutionDate.setName("tdtlResolutionDate");
        txtAmount.setName("txtAmount");
        txtBankingHead.setName("txtBankingHead");
        txtBonus.setName("txtBonus");
        txtBonusPayableHead.setName("txtBonusPayableHead");
        txtBonusReceivableHead.setName("txtBonusReceivableHead");
        txtCaseExpensesHead.setName("txtCaseExpensesHead");
        txtDiscountHead.setName("txtDiscountHead");
        txtMDSPayableHead.setName("txtMDSPayableHead");
        txtStampAdvanceHead.setName("txtStampAdvanceHead");
        txtARCCostHead.setName("txtARCCostHead");
        txtARCExpenseHead.setName("txtARCExpenseHead");
        txtEACostHead.setName("txtEACostHead");
        txtEAExpenseHead.setName("txtEAExpenseHead");
        txtEPCostHead.setName("txtEPCostHead");
        txtEPExpenseHead.setName("txtEPExpenseHead");
        txtPostageHead.setName("txtPostageHead");
        txtMDSReceivableHead.setName("txtMDSReceivableHead");
        txtSundryReceiptHead.setName("txtSundryReceiptHead");
        txtSundryPaymentHead.setName("txtSundryPaymentHead");
        txtForFeitedPaymentHead.setName("txtForFeitedPaymentHead");
        txtChittalNumberPattern.setName("txtChittalNumberPattern");
        txtCommisionHead.setName("txtCommisionHead");
        txtDay.setName("txtDay");
        txtInstAmt.setName("txtInstAmt");
        txtInstallments.setName("txtInstallments");
        txtMiscellaneousHead.setName("txtMiscellaneousHead");
        txtMunnalBonusHead.setName("txtMunnalBonusHead");
        txtMunnalReceiptsHead.setName("txtMunnalReceiptsHead");
        txtNextChittalNumber.setName("txtNextChittalNumber");
        txtNoofAuctions.setName("txtNoofAuctions");
        txtNoofDivision.setName("txtNoofDivision");
        txtNoofDraws.setName("txtNoofDraws");
        txtNoofInst.setName("txtNoofInst");
        txtNoofMemberPer.setName("txtNoofMemberPer");
        txtNoofMemberScheme.setName("txtNoofMemberScheme");
        txtNoticeChargesHead.setName("txtNoticeChargesHead");
        txtChargeHead.setName("txtChargeHead");
        txtPaymentAmount.setName("txtPaymentAmount");
        txtPaymentHead.setName("txtPaymentHead");
        txtPenalHead.setName("txtPenalHead");
        txtReceiptHead.setName("txtReceiptHead");
        txtResolutionNo.setName("txtResolutionNo");
        txtSchemeName.setName("txtSchemeName");
        txtNoofCoChittals.setName("txtNoofCoChittals");
        txtNoofCoInstallments.setName("txtNoofCoInstallments");
        txtMaxNoofMemberCoChittals.setName("txtMaxNoofMemberCoChittals");
        txtCoChittalInstAmount.setName("txtCoChittalInstAmount");
        txtSuffix.setName("txtSuffix");
        txtSuspenseHead.setName("txtSuspenseHead");
        txtThalayalBonusHead.setName("txtThalayalBonusHead");
        txtThalayalReceiptsHead.setName("txtThalayalReceiptsHead");
        txtTotAmtPerDivision.setName("txtTotAmtPerDivision");
        txtTotAmtUnderScheme.setName("txtTotAmtUnderScheme");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        lblInstAmt.setText(resourceBundle.getString("lblInstAmt"));
        lblNoofInst.setText(resourceBundle.getString("lblNoofInst"));
        btnPaymentHead.setText(resourceBundle.getString("btnPaymentHead"));
        rdoMunnalAllowed_no.setText(resourceBundle.getString("rdoMunnalAllowed_no"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblPenalHead.setText(resourceBundle.getString("lblPenalHead"));
        rdoApplicableDivision_yes.setText(resourceBundle.getString("rdoApplicableDivision_yes"));
        btnBonusPayableHead.setText(resourceBundle.getString("btnBonusPayableHead"));
        rdoPaymentDone_no.setText(resourceBundle.getString("rdoPaymentDone_no"));
        btnBankingHead.setText(resourceBundle.getString("btnBankingHead"));
        lblNextChittalNumber.setText(resourceBundle.getString("lblNextChittalNumber"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMunnalAllowed.setText(resourceBundle.getString("lblMunnalAllowed"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblNoticeChargesHead.setText(resourceBundle.getString("lblNoticeChargesHead"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblBonusPayableHead.setText(resourceBundle.getString("lblBonusPayableHead"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblPredefinitonInst.setText(resourceBundle.getString("lblPredefinitonInst"));
        lblAuctionDay.setText(resourceBundle.getString("lblAuctionDay"));
        lblApplicableDivision.setText(resourceBundle.getString("lblApplicableDivision"));
        btnSuspenseHead.setText(resourceBundle.getString("btnSuspenseHead"));
        btnCaseExpensesHead.setText(resourceBundle.getString("btnCaseExpensesHead"));
        lblApplicable1.setText(resourceBundle.getString("lblApplicable1"));
        rdoMunnalAllowed_Yes.setText(resourceBundle.getString("rdoMunnalAllowed_Yes"));
        rdoThalayalAllowed_no.setText(resourceBundle.getString("rdoThalayalAllowed_no"));
        lblNoofMemberScheme.setText(resourceBundle.getString("lblNoofMemberScheme"));
        lblSchemeEndDt.setText(resourceBundle.getString("lblSchemeEndDt"));
        lblThalayalAllowed.setText(resourceBundle.getString("lblThalayalAllowed"));
        rdoPredefinitonInst_yes.setText(resourceBundle.getString("rdoPredefinitonInst_yes"));
        btnReceiptHead.setText(resourceBundle.getString("btnReceiptHead"));
        lblSchemeStDt.setText(resourceBundle.getString("lblSchemeStDt"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblPaymentAmount.setText(resourceBundle.getString("lblPaymentAmount"));
        lblApplicable4.setText(resourceBundle.getString("lblApplicable4"));
        rdoThalayalAllowed_yes.setText(resourceBundle.getString("rdoThalayalAllowed_yes"));
        btnThalayalReceiptsHead.setText(resourceBundle.getString("btnThalayalReceiptsHead"));
        lblInstallmentNo.setText(resourceBundle.getString("lblInstallmentNo"));
        btnBonusReceivableHead.setText(resourceBundle.getString("btnBonusReceivableHead"));
        rdoPredefinitonInst_no.setText(resourceBundle.getString("rdoPredefinitonInst_no"));
        btnPenalHead.setText(resourceBundle.getString("btnPenalHead"));
        lblInstallmentDay.setText(resourceBundle.getString("lblInstallmentDay"));
        lblChittalNumberPattern.setText(resourceBundle.getString("lblChittalNumberPattern"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblBonus.setText(resourceBundle.getString("lblBonus"));
        lblNoofAuctions.setText(resourceBundle.getString("lblNoofAuctions"));
        lblThalayalBonusHead.setText(resourceBundle.getString("lblThalayalBonusHead"));
        lblNoofMemberPer.setText(resourceBundle.getString("lblNoofMemberPer"));
        lblMunnalReceiptsHead.setText(resourceBundle.getString("lblMunnalReceiptsHead"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblNoofDraws.setText(resourceBundle.getString("lblNoofDraws"));
        lblTotAmtPerDivision.setText(resourceBundle.getString("lblTotAmtPerDivision"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSuspenseHead.setText(resourceBundle.getString("lblSuspenseHead"));
        lblSuspenseProdID.setText(resourceBundle.getString("lblSuspenseProdID"));
        lblSuspenseAccNo.setText(resourceBundle.getString("lblSuspenseAccNo"));
        lblReceiptHead.setText(resourceBundle.getString("lblReceiptHead"));
        btnCopy.setText(resourceBundle.getString("btnCopy"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblCaseExpensesHead.setText(resourceBundle.getString("lblCaseExpensesHead"));
        lblDiscountHead.setText(resourceBundle.getString("lblDiscountHead"));
        lblStampAdvanceHead.setText(resourceBundle.getString("lblStampAdvanceHead"));
        lblARCCostHead.setText(resourceBundle.getString("lblARCCostHead"));
        lblARCExpenseHead.setText(resourceBundle.getString("lblARCExpenseHead"));
        lblEACostHead.setText(resourceBundle.getString("lblEACostHead"));
        lblEAExpenseHead.setText(resourceBundle.getString("lblEAExpenseHead"));
        lblEPCostHead.setText(resourceBundle.getString("lblEPCostHead"));
        lblEPExpenseHead.setText(resourceBundle.getString("lblEPExpenseHead"));
        lblPostageHead.setText(resourceBundle.getString("lblPostageHead"));
        lblMDSPayableHead.setText(resourceBundle.getString("lblMDSPayableHead"));
        lblMDSReceivableHead.setText(resourceBundle.getString("lblMDSReceivableHead"));
        lblSundryReceiptHead.setText(resourceBundle.getString("lblSundryReceiptHead"));
        lblSundryPaymentHead.setText(resourceBundle.getString("lblSundryPaymentHead"));
        lblBonusReceivableHead.setText(resourceBundle.getString("lblBonusReceivableHead"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnMunnalReceiptsHead.setText(resourceBundle.getString("btnMunnalReceiptsHead"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblMultipleMembers.setText(resourceBundle.getString("lblMultipleMembers"));
        rdoMultipleMembers_no.setText(resourceBundle.getString("rdoMultipleMembers_no"));
        rdoMultipleMembers_yes.setText(resourceBundle.getString("rdoMultipleMembers_yes"));
        lblNoofCoChittals.setText(resourceBundle.getString("lblNoofCoChittals"));
        lblNoofCoInstallments.setText(resourceBundle.getString("lblNoofCoInstallments"));
        lblMaxNoofMemberCoChittals.setText(resourceBundle.getString("lblMaxNoofMemberCoChittals"));
        lblCoChittalInstAmount.setText(resourceBundle.getString("lblCoChittalInstAmount"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblNoticeType.setText(resourceBundle.getString("lblNoticeType"));
        lblNoticeChargeAmt.setText(resourceBundle.getString("lblNoticeChargeAmt"));
        lblPostageAmt.setText(resourceBundle.getString("lblPostageAmt"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnMiscellaneousHead.setText(resourceBundle.getString("btnMiscellaneousHead"));
        lblResolutionNo.setText(resourceBundle.getString("lblResolutionNo"));
        lbllResolutionDate.setText(resourceBundle.getString("lbllResolutionDate"));
        rdoApplicableDivision_no.setText(resourceBundle.getString("rdoApplicableDivision_no"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblCommisionHead.setText(resourceBundle.getString("lblCommisionHead"));
        lblInstFreq.setText(resourceBundle.getString("lblInstFreq"));
        btnThalayalBonusHead.setText(resourceBundle.getString("btnThalayalBonusHead"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnNoticeChargesHead.setText(resourceBundle.getString("btnNoticeChargesHead"));
        btnChargeHead.setText(resourceBundle.getString("btnChargeHead"));
        lblPaymentHead.setText(resourceBundle.getString("lblPaymentHead"));
        lblThalayalReceiptsHead.setText(resourceBundle.getString("lblThalayalReceiptsHead"));
        lblApplicable2.setText(resourceBundle.getString("lblApplicable2"));
        lblNoofDivision.setText(resourceBundle.getString("lblNoofDivision"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        lblMiscellaneousHead.setText(resourceBundle.getString("lblMiscellaneousHead"));
        lblBankingHead.setText(resourceBundle.getString("lblBankingHead"));
        lblMunnalBonusHead.setText(resourceBundle.getString("lblMunnalBonusHead"));
        lblInstallmentNumber.setText(resourceBundle.getString("lblInstallmentNumber"));
        lblInstallmentDt.setText(resourceBundle.getString("lblInstallmentDt"));
        lblPaymentDone.setText(resourceBundle.getString("lblPaymentDone"));
        lblApplicable3.setText(resourceBundle.getString("lblApplicable3"));
        lblTotAmtUnderScheme.setText(resourceBundle.getString("lblTotAmtUnderScheme"));
        btnCommisionHead.setText(resourceBundle.getString("btnCommisionHead"));
    }

    private void initComponentData() {
        try {
            cboInstallmentDay.setModel(observable.getCbmInstallmentDay());
            cboAuctionDay.setModel(observable.getCbmAuctionDay());
            cboProductId.setModel(observable.getCbmProductId());
            cboInstFreq.setModel(observable.getCbmInstFreq());
            cboNoticeType.setModel(observable.getCbmNoticeType());
            cboSuspenseProdID.setModel(observable.getCbmSuspenseProdID());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void initTableData() {
        tblSchedule.setModel(observable.getTblScheduleDetails());
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());
    }

    private void setMaxLengths() {
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtBonus.setValidation(new CurrencyValidation(14, 2));
        txtPaymentAmount.setValidation(new CurrencyValidation(14, 2));
        txtNoofDivision.setValidation(new NumericValidation(3, 1));
        txtNoofAuctions.setValidation(new NumericValidation(3, 1));
        txtNoofDraws.setValidation(new NumericValidation(3, 1));
        txtNoofMemberPer.setValidation(new NumericValidation(3, 1));
        txtNoofMemberScheme.setValidation(new NumericValidation(3, 1));
        txtInstAmt.setValidation(new CurrencyValidation(14, 2));
        txtTotAmtPerDivision.setValidation(new CurrencyValidation(14, 2));
        txtTotAmtUnderScheme.setValidation(new CurrencyValidation(14, 2));
        txtNoofInst.setValidation(new NumericValidation(3, 1));
        txtInstallments.setValidation(new NumericValidation(3, 1));
        txtChittalNumberPattern.setMaxLength(8);
        txtNextChittalNumber.setValidation(new NumericValidation(2, 0));
        txtNextChittalNumber.setMaxLength(2);
        txtSuffix.setMaxLength(2);
        txtSchemeName.setMaxLength(8);
        txtNoofCoChittals.setValidation(new NumericValidation(1, 0));
        txtNoofCoInstallments.setValidation(new NumericValidation(3, 0));
        txtMaxNoofMemberCoChittals.setValidation(new NumericValidation(3, 0));
        txtCoChittalInstAmount.setValidation(new CurrencyValidation(14, 2));
        txtSuffix.setValidation(new NumericValidation(2, 0));
        txtDay.setValidation(new NumericValidation(3, 0));
        txtInstallments.setValidation(new NumericValidation(3, 0));
        txtReceiptHead.setAllowAll(true);
        txtPaymentHead.setAllowAll(true);
        txtSuspenseHead.setAllowAll(true);
        txtMiscellaneousHead.setAllowAll(true);
        txtCommisionHead.setAllowAll(true);
        txtBonusPayableHead.setAllowAll(true);
        txtBonusReceivableHead.setAllowAll(true);
        txtPenalHead.setAllowAll(true);
        txtThalayalReceiptsHead.setAllowAll(true);
        txtThalayalBonusHead.setAllowAll(true);
        txtMunnalBonusHead.setAllowAll(true);
        txtMunnalReceiptsHead.setAllowAll(true);
        txtBankingHead.setAllowAll(true);
        txtNoticeChargesHead.setAllowAll(true);
        txtChargeHead.setAllowAll(true);
        txtCaseExpensesHead.setAllowAll(true);
        txtDiscountHead.setAllowAll(true);
        txtMDSPayableHead.setAllowAll(true);
        txtMDSReceivableHead.setAllowAll(true);
        txtSundryReceiptHead.setAllowAll(true);
        txtSundryPaymentHead.setAllowAll(true);
        txtNoticeChargeAmt.setValidation(new CurrencyValidation(14, 2));
        txtPostageChargeAmt.setValidation(new CurrencyValidation(14, 2));
        txtStampAdvanceHead.setAllowAll(true);
        txtARCCostHead.setAllowAll(true);
        txtARCExpenseHead.setAllowAll(true);
        txtEACostHead.setAllowAll(true);
        txtEAExpenseHead.setAllowAll(true);
        txtEPCostHead.setAllowAll(true);
        txtEPExpenseHead.setAllowAll(true);
        txtPostageHead.setAllowAll(true);
    }

    /* Auto Generated Method - setMandatoryHashMap()
     
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNoofDivision", new Boolean(false));
        mandatoryMap.put("txtNoofMemberPer", new Boolean(false));
        mandatoryMap.put("txtNoofAuctions", new Boolean(false));
        mandatoryMap.put("txtNoofInst", new Boolean(false));
        mandatoryMap.put("txtNoofDraws", new Boolean(false));
        mandatoryMap.put("txtInstAmt", new Boolean(false));
        mandatoryMap.put("txtNoofMemberScheme", new Boolean(false));
        mandatoryMap.put("txtTotAmtPerDivision", new Boolean(false));
        mandatoryMap.put("tdtSchemeStDt", new Boolean(true));
        mandatoryMap.put("tdtSchemeEndDt", new Boolean(true));
        mandatoryMap.put("cboInstFreq", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("tdtlResolutionDate", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtNoofCoChittals", new Boolean(false));
        mandatoryMap.put("txtNoofCoInstallments", new Boolean(false));
        mandatoryMap.put("txtMaxNoofMemberCoChittals", new Boolean(false));
        mandatoryMap.put("txtCoChittalInstAmount", new Boolean(false));
        mandatoryMap.put("txtTotAmtUnderScheme", new Boolean(true));
        mandatoryMap.put("txtInstallments", new Boolean(true));
        mandatoryMap.put("cboInstallmentDay", new Boolean(false));
        mandatoryMap.put("cboAuctionDay", new Boolean(false));
        mandatoryMap.put("txtChittalNumberPattern", new Boolean(true));
        mandatoryMap.put("txtNextChittalNumber", new Boolean(true));
        mandatoryMap.put("txtDay", new Boolean(true));
        mandatoryMap.put("tdtInstallmentDt", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtNoticeChargeAmt", new Boolean(true));
        mandatoryMap.put("txtPostageChargeAmt", new Boolean(true));
        mandatoryMap.put("txtBonus", new Boolean(true));
        mandatoryMap.put("txtPaymentAmount", new Boolean(true));
        mandatoryMap.put("txtReceiptHead", new Boolean(true));
        mandatoryMap.put("txtPaymentHead", new Boolean(true));
        mandatoryMap.put("txtSuspenseHead", new Boolean(false));
        mandatoryMap.put("txtPenalHead", new Boolean(true));
        mandatoryMap.put("txtBonusReceivableHead", new Boolean(true));
        mandatoryMap.put("txtBonusPayableHead", new Boolean(true));
        mandatoryMap.put("txtCommisionHead", new Boolean(true));
        mandatoryMap.put("txtMiscellaneousHead", new Boolean(true));
        mandatoryMap.put("txtMunnalReceiptsHead", new Boolean(true));
        mandatoryMap.put("txtBankingHead", new Boolean(true));
        mandatoryMap.put("txtChargeHead", new Boolean(true));
        mandatoryMap.put("txtNoticeChargesHead", new Boolean(true));
        mandatoryMap.put("txtCaseExpensesHead", new Boolean(true));
        mandatoryMap.put("txtDiscountHead", new Boolean(true));
        mandatoryMap.put("txtMDSPayableHead", new Boolean(true));
        mandatoryMap.put("txtStampAdvanceHead", new Boolean(true));
        mandatoryMap.put("txtARCCostHead", new Boolean(true));
        mandatoryMap.put("txtARCExpenseHead", new Boolean(true));
        mandatoryMap.put("txtEACostHead", new Boolean(true));
        mandatoryMap.put("txtEAExpenseHead", new Boolean(true));
        mandatoryMap.put("txtEPCostHead", new Boolean(true));
        mandatoryMap.put("txtEPExpenseHead", new Boolean(true));
        mandatoryMap.put("txtPostageHead", new Boolean(true));
        mandatoryMap.put("txtMDSReceivableHead", new Boolean(true));
        mandatoryMap.put("txtSundryReceiptHead", new Boolean(true));
        mandatoryMap.put("txtSundryPaymentHead", new Boolean(true));
        mandatoryMap.put("txtThalayalBonusHead", new Boolean(true));
        mandatoryMap.put("txtThalayalReceiptsHead", new Boolean(true));
        mandatoryMap.put("txtMunnalBonusHead", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public String getAccHdDesc(String accHdId) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHdId);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHdDesc = map2.get("AC_HD_DESC").toString();
            return accHdDesc;
        } else {
            return "";
        }
    }

    public void update() {
        String YesNo = CommonUtil.convertObjToStr(observable.getIsRevPostAdv());
        if (YesNo.equals("Y")) {
            IS_REV_POST_ADV.setSelected(true);
        } else if (YesNo.equals("N") && YesNo.equals("")) {
            IS_REV_POST_ADV.setSelected(false);
        }
        txtNoofDivision.setText(CommonUtil.convertObjToStr(observable.getTxtNoofDivision())); //AJITH Changed From setText(observable.getTxtNoofDivision());
        lblProductDescVal.setText(observable.getProd_Desc());
        txtChittalNumberPattern.setText(observable.getTxtChittalNumberPattern());
        txtSuffix.setText(CommonUtil.convertObjToStr(observable.getTxtSuffix()));  //AJITH
        txtNextChittalNumber.setText(CommonUtil.convertObjToStr(observable.getTxtNextChittalNumber()));  //AJITH
        txtNoofMemberPer.setText(CommonUtil.convertObjToStr(observable.getTxtNoofMemberPer()));  //AJITH
        txtNoofAuctions.setText(CommonUtil.convertObjToStr(observable.getTxtNoofAuctions())); //AJITH
        txtNoofInst.setText(CommonUtil.convertObjToStr(observable.getTxtNoofInst()));  //AJITH
        txtNoofDraws.setText(CommonUtil.convertObjToStr(observable.getTxtNoofDraws())); //AJITH
        txtNoofMemberScheme.setText(CommonUtil.convertObjToStr(observable.getTxtNoofMemberScheme()));  //AJITH
        txtTotAmtPerDivision.setText(CommonUtil.convertObjToStr(observable.getTxtTotAmtPerDivision()));  //AJITH
        tdtSchemeStDt.setDateValue(observable.getTdtSchemeStDt());
        tdtSchemeEndDt.setDateValue(observable.getTdtSchemeEndDt());
        txtClosureRate.setText(observable.getTxtClosureRate());
        txtSancNo.setText(observable.getTxtSancNo());
        tdtSancDt.setDateValue(observable.getTdtSancDt());
        txaRemarks.setText(observable.getTxaRemarks());
        txtMdsType.setText(observable.getMdsType());
        System.out.println("observable.getMdsType()========="+  observable.getMdsType());
        txtMdsType.setText(observable.getAutionTime());
        String instFreq = CommonUtil.convertObjToStr(observable.getCboInstFreq());
        if (instFreq.length() > 0 && instFreq.equals("365")) {
            cboInstFreq.setSelectedItem("Yearly");
        } else if (instFreq.length() > 0 && instFreq.equals("180")) {
            cboInstFreq.setSelectedItem("Half Yearly");
        } else if (instFreq.length() > 0 && instFreq.equals("90")) {
            cboInstFreq.setSelectedItem("Quaterly");
        } else if (instFreq.length() > 0 && instFreq.equals("30")) {
            cboInstFreq.setSelectedItem("Monthly");
        } else if (instFreq.length() > 0 && instFreq.equals("7")) {
            cboInstFreq.setSelectedItem("Weekly");
        }
        cboProductId.setSelectedItem(observable.getCboProductId());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
        tdtlResolutionDate.setDateValue(observable.getTdtlResolutionDate());
        txtSchemeName.setText(observable.getTxtSchemeName());
        txtSchemeDesc.setText(observable.getTxtSchemeDesc());
        txtTotAmtUnderScheme.setText(CommonUtil.convertObjToStr(observable.getTxtTotAmtUnderScheme()));  //AJITH
        cboInstallmentDay.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboInstallmentDay()));   //AJITH
        cboAuctionDay.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboAuctionDay()));   //AJITH

        String applicable1 = CommonUtil.convertObjToStr(observable.getTxtApplicable1());
        if (applicable1.length() > 0 && applicable1.equals("Y")) {
            chkApplicable1.setSelected(true);
        } else {
            chkApplicable1.setSelected(false);
        }

        String applicable2 = CommonUtil.convertObjToStr(observable.getTxtApplicable2());
        if (applicable2.length() > 0 && applicable2.equals("Y")) {
            chkApplicable2.setSelected(true);
        } else {
            chkApplicable2.setSelected(false);
        }

        String applicable3 = CommonUtil.convertObjToStr(observable.getTxtApplicable3());
        if (applicable3.length() > 0 && applicable3.equals("Y")) {
            chkApplicable3.setSelected(true);
        } else {
            chkApplicable3.setSelected(false);
        }

        String applicable4 = CommonUtil.convertObjToStr(observable.getTxtApplicable4());
        if (applicable4.length() > 0 && applicable4.equals("Y")) {
            chkApplicable4.setSelected(true);
        } else {
            chkApplicable4.setSelected(false);
        }

        String thalayal = CommonUtil.convertObjToStr(observable.getRdoThalayalAllowed());
        if (thalayal.length() > 0 && thalayal.equals("Y")) {
            rdoThalayalAllowed_yes.setSelected(true);
            rdoThalayalAllowed_no.setSelected(false);
        } else {
            rdoThalayalAllowed_yes.setSelected(false);
            rdoThalayalAllowed_no.setSelected(true);
        }

        String munnal = CommonUtil.convertObjToStr(observable.getRdoMunnalAllowed());
        if (munnal.length() > 0 && munnal.equals("Y")) {
            rdoMunnalAllowed_Yes.setSelected(true);
            rdoMunnalAllowed_no.setSelected(false);
        } else {
            rdoMunnalAllowed_Yes.setSelected(false);
            rdoMunnalAllowed_no.setSelected(true);
        }

        String preIns = CommonUtil.convertObjToStr(observable.getRdoPredefinitonInst());
        if (preIns.length() > 0 && preIns.equals("Y")) {
            rdoPredefinitonInst_yes.setSelected(true);
            rdoPredefinitonInst_no.setSelected(false);
            lblApplicableDivision.setVisible(true);
            panApplicableDivision.setVisible(true);
            String appliDiv = CommonUtil.convertObjToStr(observable.getRdoApplicableDivision());
            if (appliDiv.length() > 0 && appliDiv.equals("Y")) {
                rdoApplicableDivision_yes.setSelected(true);
                rdoApplicableDivision_no.setSelected(false);
                lblApplicable1.setVisible(false);
                chkApplicable1.setVisible(false);
                lblApplicable2.setVisible(false);
                chkApplicable2.setVisible(false);
                lblApplicable3.setVisible(false);
                chkApplicable3.setVisible(false);
                lblApplicable4.setVisible(false);
                chkApplicable4.setVisible(false);
                chkApplicable1.setSelected(false);
                chkApplicable2.setSelected(false);
                chkApplicable3.setSelected(false);
                chkApplicable4.setSelected(false);
            } else {
                rdoApplicableDivision_yes.setSelected(false);
                rdoApplicableDivision_no.setSelected(true);
                lblApplicable1.setVisible(true);
                chkApplicable1.setVisible(true);
                lblApplicable2.setVisible(true);
                chkApplicable2.setVisible(true);
                lblApplicable3.setVisible(true);
                chkApplicable3.setVisible(true);
                lblApplicable4.setVisible(true);
                chkApplicable4.setVisible(true);
            }
        } else {
            rdoPredefinitonInst_yes.setSelected(false);
            rdoPredefinitonInst_no.setSelected(true);
            lblApplicableDivision.setVisible(false);
            panApplicableDivision.setVisible(false);
            lblApplicable1.setVisible(false);
            chkApplicable1.setVisible(false);
            lblApplicable2.setVisible(false);
            chkApplicable2.setVisible(false);
            lblApplicable3.setVisible(false);
            chkApplicable3.setVisible(false);
            lblApplicable4.setVisible(false);
            chkApplicable4.setVisible(false);
        }

        String payDone = CommonUtil.convertObjToStr(observable.getRdoPaymentDone());
        if (payDone.length() > 0 && payDone.equals("Y")) {
            rdoPaymentDone_yes.setSelected(true);
            rdoPaymentDone_no.setSelected(false);
            txtInstallments.setText(CommonUtil.convertObjToStr(observable.getTxtInstallments()));
            txtInstallments.setEnabled(true);
           txtDay.setEnabled(false);
        } else {
            rdoPaymentDone_yes.setSelected(false);
            rdoPaymentDone_no.setSelected(true);
            txtDay.setText(CommonUtil.convertObjToStr(observable.getDay()));
            txtDay.setEnabled(true);
          txtInstallments.setEnabled(false);
        }
        txtReceiptHead.setText(observable.getTxtReceiptHead());
        if (!txtReceiptHead.getText().equals("")) {
            btnReceiptHead.setToolTipText(getAccHdDesc(observable.getTxtReceiptHead()));
        }

        txtPaymentHead.setText(observable.getTxtPaymentHead());
        if (!txtPaymentHead.getText().equals("")) {
            btnPaymentHead.setToolTipText(getAccHdDesc(observable.getTxtPaymentHead()));
        }
        txtPostAdvHead.setText(observable.getTxtPostageAdvHead());

        txtSuspenseHead.setText(observable.getTxtSuspenseHead());
        if (!txtSuspenseHead.getText().equals("")) {
            btnSuspenseHead.setToolTipText(getAccHdDesc(observable.getTxtSuspenseHead()));
        }
        txtSuspenseAccNo.setText(observable.getTxtSuspenseAccNo());
        if (!txtSuspenseAccNo.getText().equals("")) {
            btnSuspenseAccNo.setToolTipText(getAccHdDesc(observable.getTxtSuspenseAccNo()));
        }
        cboSuspenseProdID.setSelectedItem(observable.getCboSuspenseProdID());
        String suspenceGLorAcc = CommonUtil.convertObjToStr(observable.getRdoGLorAccNo());
        if (suspenceGLorAcc.length() > 0 && suspenceGLorAcc.equals("GL")) {
            rdoSuspenseGL.setSelected(true);
            suspenseGLenableDisable(true);
            suspenseACCNoEnableDisable(false);
        } else {
            rdoSuspenseAccNo.setSelected(true);
            suspenseGLenableDisable(false);
            suspenseACCNoEnableDisable(true);
        }
        txtMiscellaneousHead.setText(observable.getTxtMiscellaneousHead());
        if (!txtMiscellaneousHead.getText().equals("")) {
            btnMiscellaneousHead.setToolTipText(getAccHdDesc(observable.getTxtMiscellaneousHead()));
        }
        txtCommisionHead.setText(observable.getTxtCommisionHead());
        if (!txtCommisionHead.getText().equals("")) {
            btnCommisionHead.setToolTipText(getAccHdDesc(observable.getTxtCommisionHead()));
        }
        txtBonusPayableHead.setText(observable.getTxtBonusPayableHead());
        if (!txtBonusPayableHead.getText().equals("")) {
            btnBonusPayableHead.setToolTipText(getAccHdDesc(observable.getTxtBonusPayableHead()));
        }
        txtBonusReceivableHead.setText(observable.getTxtBonusReceivableHead());
        if (!txtBonusReceivableHead.getText().equals("")) {
            btnBonusReceivableHead.setToolTipText(getAccHdDesc(observable.getTxtBonusReceivableHead()));
        }
        txtPenalHead.setText(observable.getTxtPenalHead());
        if (!txtPenalHead.getText().equals("")) {
            btnPenalHead.setToolTipText(getAccHdDesc(observable.getTxtPenalHead()));
        }
        txtThalayalReceiptsHead.setText(observable.getTxtThalayalReceiptsHead());
        if (!txtThalayalReceiptsHead.getText().equals("")) {
            btnThalayalReceiptsHead.setToolTipText(getAccHdDesc(observable.getTxtThalayalReceiptsHead()));
        }
        txtThalayalBonusHead.setText(observable.getTxtThalayalBonusHead());
        if (!txtThalayalBonusHead.getText().equals("")) {
            btnThalayalBonusHead.setToolTipText(getAccHdDesc(observable.getTxtThalayalBonusHead()));
        }
        txtMunnalBonusHead.setText(observable.getTxtMunnalBonusHead());
        if (!txtMunnalBonusHead.getText().equals("")) {
            btnMunnalBonusHead.setToolTipText(getAccHdDesc(observable.getTxtMunnalBonusHead()));
        }
        txtMunnalReceiptsHead.setText(observable.getTxtMunnalReceiptsHead());
        if (!txtMunnalReceiptsHead.getText().equals("")) {
            btnMunnalReceiptsHead.setToolTipText(getAccHdDesc(observable.getTxtMunnalReceiptsHead()));
        }
        txtBankingHead.setText(observable.getTxtBankingHead());
        if (!txtBankingHead.getText().equals("")) {
            btnBankingHead.setToolTipText(getAccHdDesc(observable.getTxtBankingHead()));
        }
        txtNoticeChargesHead.setText(observable.getTxtNoticeChargesHead());
        if (!txtNoticeChargesHead.getText().equals("")) {
            btnNoticeChargesHead.setToolTipText(getAccHdDesc(observable.getTxtNoticeChargesHead()));
        }
        txtChargeHead.setText(observable.getTxtChargeHead());
        if (!txtChargeHead.getText().equals("")) {
            btnChargeHead.setToolTipText(getAccHdDesc(observable.getTxtChargeHead()));
        }
        txtStampAdvanceHead.setText(observable.getTxtStampAdvanceHead());
        if (!txtStampAdvanceHead.getText().equals("")) {
            btnStampAdvanceHead.setToolTipText(getAccHdDesc(observable.getTxtStampAdvanceHead()));
        }
        txtARCCostHead.setText(observable.getTxtARCCostHead());
        if (!txtARCCostHead.getText().equals("")) {
            btnARCCostHead.setToolTipText(getAccHdDesc(observable.getTxtARCCostHead()));
        }
        txtARCExpenseHead.setText(observable.getTxtARCExpenseHead());
        if (!txtARCExpenseHead.getText().equals("")) {
            btnARCExpenseHead.setToolTipText(getAccHdDesc(observable.getTxtARCExpenseHead()));
        }
        txtEACostHead.setText(observable.getTxtEACostHead());
        if (!txtEACostHead.getText().equals("")) {
            btnEACostHead.setToolTipText(getAccHdDesc(observable.getTxtEACostHead()));
        }
        txtEAExpenseHead.setText(observable.getTxtEAExpenseHead());
        if (!txtEAExpenseHead.getText().equals("")) {
            btnEAExpenseHead.setToolTipText(getAccHdDesc(observable.getTxtEAExpenseHead()));
        }
        txtEPCostHead.setText(observable.getTxtEPCostHead());
        if (!txtEPCostHead.getText().equals("")) {
            btnEPCostHead.setToolTipText(getAccHdDesc(observable.getTxtEPCostHead()));
        }
        txtEPExpenseHead.setText(observable.getTxtEPExpenseHead());
        if (!txtEPExpenseHead.getText().equals("")) {
            btnEPExpenseHead.setToolTipText(getAccHdDesc(observable.getTxtEPExpenseHead()));
        }
        txtPostageHead.setText(observable.getTxtPostageHead());
        if (!txtPostageHead.getText().equals("")) {
            btnPostageHead.setToolTipText(getAccHdDesc(observable.getTxtPostageHead()));
        }
        txtCaseExpensesHead.setText(observable.getTxtCaseExpensesHead());
        if (!txtCaseExpensesHead.getText().equals("")) {
            btnCaseExpensesHead.setToolTipText(getAccHdDesc(observable.getTxtCaseExpensesHead()));
        }
        txtDiscountHead.setText(observable.getTxtDiscountHead());
        if (!txtDiscountHead.getText().equals("")) {
            btnDiscountHead.setToolTipText(getAccHdDesc(observable.getTxtDiscountHead()));
        }
        txtMDSPayableHead.setText(observable.getTxtMDSPayableHead());
        if (!txtMDSPayableHead.getText().equals("")) {
            btnMDSPayableHead.setToolTipText(getAccHdDesc(observable.getTxtMDSPayableHead()));
        }
        txtMDSReceivableHead.setText(observable.getTxtMDSReceivableHead());
        if (!txtMDSReceivableHead.getText().equals("")) {
            btnMDSReceivableHead.setToolTipText(getAccHdDesc(observable.getTxtMDSReceivableHead()));
        }
        txtSundryReceiptHead.setText(observable.getTxtSundryReceiptHead());
        if (!txtSundryReceiptHead.getText().equals("")) {
            btnSundryReceiptHead.setToolTipText(getAccHdDesc(observable.getTxtSundryReceiptHead()));
        }
        txtSundryPaymentHead.setText(observable.getTxtSundryPaymentHead());
        if (!txtSundryPaymentHead.getText().equals("")) {
            btnSundryPaymentHead.setToolTipText(getAccHdDesc(observable.getTxtSundryPaymentHead()));
        }
                txtForFeitedPaymentHead.setText(observable.gettxtForFeitedPaymentHead());
        if (!txtForFeitedPaymentHead.getText().equals("")) {
            btnForFietedPaymentHead.setToolTipText(getAccHdDesc(observable.gettxtForFeitedPaymentHead()));
        }
        txtNoofCoChittals.setText(CommonUtil.convertObjToStr(observable.getTxtNoofCoChittals())); //AJITH
        txtNoofCoInstallments.setText(CommonUtil.convertObjToStr(observable.getTxtNoofCoInstallments())); //AJITH
        txtMaxNoofMemberCoChittals.setText(CommonUtil.convertObjToStr(observable.getTxtMaxNoofMemberCoChittals())); //AJITH
        txtLegalChrgHead.setText(observable.getTxtLegalChrgHead()); // Added by nithya
        txtPartPayBonusRecoveryHead.setText(observable.getTxtPartPayBonusRecoveryHead());// Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
        txtOtherChrgeAcHd.setText(observable.getTxtOtherChrgeAcHd());
        String multipleMember = CommonUtil.convertObjToStr(observable.getRdoMultipleMembers());
        if (multipleMember.length() > 0 && multipleMember.equals("Y")) {
            rdoMultipleMembers_yes.setSelected(true);
            rdoMultipleMembers_no.setSelected(false);
            panCoChittalDetails.setVisible(true);
            panDivisionDetails.setVisible(false);
            txtCoChittalInstAmount.setText(CommonUtil.convertObjToStr(observable.getTxtInstAmt()));  //AJITH
            txtMaxNoofMemberCoChittals.setEnabled(false);
        } else {
            rdoMultipleMembers_yes.setSelected(false);
            rdoMultipleMembers_no.setSelected(true);
            panCoChittalDetails.setVisible(false);
            panDivisionDetails.setVisible(true);
            txtInstAmt.setText(CommonUtil.convertObjToStr(observable.getTxtInstAmt()));  //AJITH
        }
        txtMdsType.setText(observable.getMdsType()); 
        if (CommonUtil.convertObjToStr(observable.getChkIsASpecialScheme()).equalsIgnoreCase("Y")) {
            chkSpecialScheme.setSelected(true);
        }else{
            chkSpecialScheme.setSelected(false);
        }
        if (observable.getChkSMSAlert() != null && CommonUtil.convertObjToStr(observable.getChkSMSAlert()).equalsIgnoreCase("Y")) {
            chkSMSAlert.setSelected(true);
        } else {
            chkSMSAlert.setSelected(false);
        }
        if (observable.getChkBonusPrint() != null && CommonUtil.convertObjToStr(observable.getChkBonusPrint()).equalsIgnoreCase("Y")) {
            chkBonusPrint.setSelected(true);
        } else {
            chkBonusPrint.setSelected(false);
        }
        // Added by nithya on 11-08-2017 for 7145
        if(observable.getChkBankSettlement() != null && CommonUtil.convertObjToStr(observable.getChkBankSettlement()).equalsIgnoreCase("Y")){
            chkBankSettlement.setSelected(true);
        }else{
            chkBankSettlement.setSelected(false);
        }
        cboGroupName.setSelectedItem(observable.getCboGroupNo());
        txtSchemeGraceperiod.setText(CommonUtil.convertObjToStr(observable.getTxtSchemeGraceperiod()));  //AJITH  // Added by nithya on 21-04-2020 for KD-1611
        if(observable.getChkDiscountFirstInst() != null && CommonUtil.convertObjToStr(observable.getChkDiscountFirstInst()).equalsIgnoreCase("Y")){
            chkDiscountFirstInst.setSelected(true);
        }else{
            chkDiscountFirstInst.setSelected(false);
        }
        txtDiscountAmt.setText(CommonUtil.convertObjToStr(observable.getTxtDiscountAmt()));  //AJITH
        if(observable.getChkCreditStampAdvance().equalsIgnoreCase("Y")){
            chkCreditStampAdvance.setSelected(true);
        }else{
            chkCreditStampAdvance.setSelected(false);
        }
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        tblSchedule.setModel(observable.getTblScheduleDetails());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateSchemeFields() {
        //added by rish
        observable.setTxtPostageHead(txtPostAdvHead.getText());
        if (IS_REV_POST_ADV.isSelected()) {
            observable.setIsRevPostAdv("Y");
        } else {
            observable.setIsRevPostAdv("N");
        }
        observable.settxtForFeitedPaymentHead(txtForFeitedPaymentHead.getText());
        observable.setTxtNoofDivision(CommonUtil.convertObjToInt(txtNoofDivision.getText())); //AJITH Changed From setTxtNoofDivision(txtNoofDivision.getText());
        observable.setProd_Desc(lblProductDescVal.getText());
        observable.setTxtChittalNumberPattern(txtChittalNumberPattern.getText());
        observable.setTxtSuffix(CommonUtil.convertObjToInt(txtSuffix.getText()));  //AJITH
        observable.setTxtNextChittalNumber(CommonUtil.convertObjToInt(txtNextChittalNumber.getText()));  //AJITH
        observable.setTxtNoofMemberPer(CommonUtil.convertObjToInt(txtNoofMemberPer.getText()));  //AJITH
        observable.setTxtNoofAuctions(CommonUtil.convertObjToInt(txtNoofAuctions.getText()));  //AJITH
        observable.setTxtNoofInst(CommonUtil.convertObjToInt(txtNoofInst.getText()));  //AJITH
        observable.setTxtNoofDraws(CommonUtil.convertObjToInt(txtNoofDraws.getText()));  //AJITH

        observable.setTxtTotAmtPerDivision(CommonUtil.convertObjToDouble(txtTotAmtPerDivision.getText()));  //AJITH
        observable.setTdtSchemeStDt(tdtSchemeStDt.getDateValue());
        observable.setTdtSchemeEndDt(tdtSchemeEndDt.getDateValue());
        observable.setCboInstFreq((String) cboInstFreq.getSelectedItem());
        //        observable.setTxtAcctHead(txtAcctHead.getText());
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setTdtlResolutionDate(tdtlResolutionDate.getDateValue());
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTxtSchemeDesc(txtSchemeDesc.getText());
        observable.setTxtTotAmtUnderScheme(CommonUtil.convertObjToDouble(txtTotAmtUnderScheme.getText()));  //AJITH
        observable.setCboInstallmentDay(CommonUtil.convertObjToInt(cboInstallmentDay.getSelectedItem()));  //AJITH
        observable.setCboAuctionDay(CommonUtil.convertObjToInt(cboAuctionDay.getSelectedItem()));  //AJITH
        observable.setTxtInstallments(CommonUtil.convertObjToInt(txtInstallments.getText()));
        observable.setDay(CommonUtil.convertObjToInt(txtDay.getText()));
        if (chkApplicable1.isSelected() == true) {
            observable.setTxtApplicable1(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setTxtApplicable1(CommonUtil.convertObjToStr("N"));
        }

        if (chkApplicable2.isSelected() == true) {
            observable.setTxtApplicable2(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setTxtApplicable2(CommonUtil.convertObjToStr("N"));
        }

        if (chkApplicable3.isSelected() == true) {
            observable.setTxtApplicable3(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setTxtApplicable3(CommonUtil.convertObjToStr("N"));
        }

        if (chkApplicable4.isSelected() == true) {
            observable.setTxtApplicable4(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setTxtApplicable4(CommonUtil.convertObjToStr("N"));
        }

        if (rdoThalayalAllowed_yes.isSelected() == true) {
            observable.setRdoThalayalAllowed(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoThalayalAllowed(CommonUtil.convertObjToStr("N"));
        }

        if (rdoMultipleMembers_yes.isSelected() == true) {
            observable.setRdoMultipleMembers(CommonUtil.convertObjToStr("Y"));
            observable.setTxtInstAmt(CommonUtil.convertObjToDouble(txtCoChittalInstAmount.getText()));  //AJITH
            observable.setTxtNoofMemberScheme(CommonUtil.convertObjToInt(txtNoofCoInstallments.getText()));  //AJITH
            observable.setTxtNoofInst(CommonUtil.convertObjToInt(txtNoofCoInstallments.getText()));  //AJITH
            observable.setTxtNoofDivision(1); //AJITH Changed From "1"
            observable.setTxtTotAmtPerDivision(CommonUtil.convertObjToDouble(txtTotAmtUnderScheme.getText()));  //AJITH
            observable.setTxtNoofMemberPer(CommonUtil.convertObjToInt(txtNoofCoInstallments.getText())); //AJITH
        } else {
            observable.setRdoMultipleMembers(CommonUtil.convertObjToStr("N"));
            observable.setTxtInstAmt(CommonUtil.convertObjToDouble(txtInstAmt.getText()));  //AJITH
            observable.setTxtNoofMemberScheme(CommonUtil.convertObjToInt(txtNoofMemberScheme.getText()));  //AJITH
        }

        if (rdoMunnalAllowed_Yes.isSelected() == true) {
            observable.setRdoMunnalAllowed(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoMunnalAllowed(CommonUtil.convertObjToStr("N"));
        }

        if (rdoPredefinitonInst_yes.isSelected() == true) {
            observable.setRdoPredefinitonInst(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoPredefinitonInst(CommonUtil.convertObjToStr("N"));
        }

        if (rdoApplicableDivision_yes.isSelected() == true) {
            observable.setRdoApplicableDivision(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoApplicableDivision(CommonUtil.convertObjToStr("N"));
        }

        if (rdoPaymentDone_yes.isSelected() == true) {
            observable.setRdoPaymentDone(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoPaymentDone(CommonUtil.convertObjToStr("N"));
        }
        observable.setTxtReceiptHead(txtReceiptHead.getText());
        observable.setTxtPaymentHead(txtPaymentHead.getText());
        observable.setTxtSuspenseHead(txtSuspenseHead.getText());
        observable.setTxtSuspenseAccNo(txtSuspenseAccNo.getText());
        observable.setCboSuspenseProdID((String) cboSuspenseProdID.getSelectedItem());
        if (rdoSuspenseGL.isSelected() == true) {
            observable.setRdoGLorAccNo("GL");
        } else {
            observable.setRdoGLorAccNo("ACC_NO");
        }
        observable.setTxtMiscellaneousHead(txtMiscellaneousHead.getText());
        observable.setTxtCommisionHead(txtCommisionHead.getText());
        observable.setTxtBonusPayableHead(txtBonusPayableHead.getText());
        observable.setTxtBonusReceivableHead(txtBonusReceivableHead.getText());
        observable.setTxtPenalHead(txtPenalHead.getText());
        observable.setTxtThalayalReceiptsHead(txtThalayalReceiptsHead.getText());
        observable.setTxtThalayalBonusHead(txtThalayalBonusHead.getText());
        observable.setTxtMunnalBonusHead(txtMunnalBonusHead.getText());
        observable.setTxtMunnalReceiptsHead(txtMunnalReceiptsHead.getText());
        observable.setTxtBankingHead(txtBankingHead.getText());
        observable.setTxtNoticeChargesHead(txtNoticeChargesHead.getText());
        observable.setTxtChargeHead(txtChargeHead.getText());
        observable.setTxtStampAdvanceHead(txtStampAdvanceHead.getText());
        observable.setTxtARCCostHead(txtARCCostHead.getText());
        observable.setTxtARCExpenseHead(txtARCExpenseHead.getText());
        observable.setTxtEACostHead(txtEACostHead.getText());
        observable.setTxtEAExpenseHead(txtEAExpenseHead.getText());
        observable.setTxtEPCostHead(txtEPCostHead.getText());
        observable.setTxtEPExpenseHead(txtEPExpenseHead.getText());
        observable.setTxtPostageHead(txtPostageHead.getText());
        observable.setTxtCaseExpensesHead(txtCaseExpensesHead.getText());
        observable.setTxtDiscountHead(txtDiscountHead.getText());
        observable.setTxtMDSPayableHead(txtMDSPayableHead.getText());
        observable.setTxtMDSReceivableHead(txtMDSReceivableHead.getText());
        observable.setTxtSundryReceiptHead(txtSundryReceiptHead.getText());
        observable.setTxtSundryPaymentHead(txtSundryPaymentHead.getText());
        observable.setTxtPostageAdvHead(txtPostAdvHead.getText());
        observable.setTxtNoofCoChittals(CommonUtil.convertObjToDouble(txtNoofCoChittals.getText())); //AJITH
        observable.setTxtNoofCoInstallments(CommonUtil.convertObjToInt(txtNoofCoInstallments.getText())); //AJITH
        observable.setTxtMaxNoofMemberCoChittals(CommonUtil.convertObjToInt(txtMaxNoofMemberCoChittals.getText())); //AJITH
        observable.setTxtClosureRate(txtClosureRate.getText());
        observable.setTxtSancNo(txtSancNo.getText());
        observable.setTdtSancDt(tdtSancDt.getDateValue());
        observable.setTxaRemarks(txaRemarks.getText());
        observable.setAutionTime(txtAutionTime.getText());  //AJITH Changed from setAutionTime(txtMdsType.getText());
        observable.settxtForFeitedPaymentHead(txtForFeitedPaymentHead.getText());
        observable.setMdsType(txtMdsType.getText());
        if (chkBonusPrint.isSelected() == true) {
            observable.setChkBonusPrint(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setChkBonusPrint(CommonUtil.convertObjToStr("N"));
        }
        observable.setTxtLegalChrgHead(txtLegalChrgHead.getText()); // Added by nithya on 02-08-2017
        observable.setTxtPartPayBonusRecoveryHead(txtPartPayBonusRecoveryHead.getText());// Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
        observable.setTxtOtherChrgeAcHd(txtOtherChrgeAcHd.getText());
        if(chkBankSettlement.isSelected()){ // Added by nithya on 11-08-2017 for 7145
            observable.setChkBankSettlement("Y");
        }else{
            observable.setChkBankSettlement("N");
        } 
        observable.setCboGroupNo((String)cboGroupName.getSelectedItem()); 
        observable.setTxtSchemeGraceperiod(CommonUtil.convertObjToInt(txtSchemeGraceperiod.getText())); //AJITH  // Added by nithya on 21-04-2020 fpr KD-1611
        if(chkDiscountFirstInst.isSelected()){ // Added by nithya on 11-08-2017 for 7145
            observable.setChkDiscountFirstInst("Y");
        }else{
            observable.setChkDiscountFirstInst("N");
        } 
        observable.setTxtDiscountAmt(CommonUtil.convertObjToDouble(txtDiscountAmt.getText())); //AJITH
        if(chkCreditStampAdvance.isSelected()){
            observable.setChkCreditStampAdvance("Y");
        }else{
            observable.setChkCreditStampAdvance("N");
        }
        System.out.println("observable,,," + observable);
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        MDSTypeMRB objMandatoryRB = new MDSTypeMRB();
        txtNoofDivision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofDivision"));
        txtNoofMemberPer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofMemberPer"));
        txtNoofAuctions.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofAuctions"));
        txtNoofInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofInst"));
        txtNoofDraws.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofDraws"));
        txtInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstAmt"));
        txtNoofMemberScheme.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofMemberScheme"));
        txtTotAmtPerDivision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotAmtPerDivision"));
        tdtSchemeStDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSchemeStDt"));
        tdtSchemeEndDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSchemeEndDt"));
        cboInstFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstFreq"));
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtResolutionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtResolutionNo"));
        tdtlResolutionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtlResolutionDate"));
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtNoofCoChittals.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofCoChittals"));
        txtNoofCoInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoofCoInstallments"));
        txtMaxNoofMemberCoChittals.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxNoofMemberCoChittals"));
        txtCoChittalInstAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCoChittalInstAmount"));
        txtTotAmtUnderScheme.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotAmtUnderScheme"));
        txtInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallments"));
        cboInstallmentDay.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstallmentDay"));
        cboAuctionDay.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAuctionDay"));
        txtChittalNumberPattern.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNumberPattern"));
        txtNextChittalNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNextChittalNumber"));
        chkApplicable2.setHelpMessage(lblMsg, objMandatoryRB.getString("chkApplicable2"));
        chkApplicable3.setHelpMessage(lblMsg, objMandatoryRB.getString("chkApplicable3"));
        chkApplicable4.setHelpMessage(lblMsg, objMandatoryRB.getString("chkApplicable4"));
        chkApplicable1.setHelpMessage(lblMsg, objMandatoryRB.getString("chkApplicable1"));
        txtDay.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDay"));
        tdtInstallmentDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstallmentDt"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtNoticeChargeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoticeChargeAmt"));
        txtPostageChargeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostageChargeAmt"));
        txtBonus.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonus"));
        txtPaymentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaymentAmount"));
        txtReceiptHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReceiptHead"));
        txtPaymentHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaymentHead"));
        txtSuspenseHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSuspenseHead"));
        txtPenalHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalHead"));
        txtBonusReceivableHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusReceivableHead"));
        txtBonusPayableHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusPayableHead"));
        txtCommisionHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionHead"));
        txtMiscellaneousHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscellaneousHead"));
        txtMunnalReceiptsHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMunnalReceiptsHead"));
        txtBankingHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankingHead"));
        txtNoticeChargesHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoticeChargesHead"));
        txtChargeHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeHead"));
        txtCaseExpensesHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCaseExpensesHead"));
        txtDiscountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscountHead"));
        txtMDSPayableHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMDSPayableHead"));
        txtStampAdvanceHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStampAdvanceHead"));
        txtARCCostHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtARCCostHead"));
        txtARCExpenseHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtARCExpenseHead"));
        txtEACostHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEACostHead"));
        txtEAExpenseHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEAExpenseHead"));
        txtEPCostHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEPCostHead"));
        txtEPExpenseHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEPExpenseHead"));
        txtPostageHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostageHead"));
        txtMDSReceivableHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMDSReceivableHead"));
        txtSundryReceiptHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSundryReceiptHead"));
        txtSundryPaymentHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSundryPaymentHead"));
        txtThalayalBonusHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtThalayalBonusHead"));
        txtThalayalReceiptsHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtThalayalReceiptsHead"));
        txtMunnalBonusHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMunnalBonusHead"));
        //txtForFeitedPaymentHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtForFeitedPaymentHead"));
    }

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

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoThalayalAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMunnalAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPredefinitonInst = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoApplicableDivision = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPaymentDone = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultipleMembers = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSuspense = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace46 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabMdsType = new com.see.truetransact.uicomponent.CTabbedPane();
        panSchemeDetails = new com.see.truetransact.uicomponent.CPanel();
        panChitDetails = new com.see.truetransact.uicomponent.CPanel();
        panNumberPattern = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNumberPattern = new com.see.truetransact.uicomponent.CTextField();
        txtSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblChittalNumberPattern = new com.see.truetransact.uicomponent.CLabel();
        lblNextChittalNumber = new com.see.truetransact.uicomponent.CLabel();
        txtNextChittalNumber = new com.see.truetransact.uicomponent.CTextField();
        lblInstallmentDay = new com.see.truetransact.uicomponent.CLabel();
        cboInstallmentDay = new com.see.truetransact.uicomponent.CComboBox();
        cboAuctionDay = new com.see.truetransact.uicomponent.CComboBox();
        lblAuctionDay = new com.see.truetransact.uicomponent.CLabel();
        lblThalayalAllowed = new com.see.truetransact.uicomponent.CLabel();
        panThalayalAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoThalayalAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoThalayalAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblMunnalAllowed = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR2 = new com.see.truetransact.uicomponent.CPanel();
        rdoMunnalAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMunnalAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblPaymentDone = new com.see.truetransact.uicomponent.CLabel();
        rdoPaymentDone_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPaymentDone_yes = new com.see.truetransact.uicomponent.CRadioButton();
        txtInstallments = new com.see.truetransact.uicomponent.CTextField();
        txtDay = new com.see.truetransact.uicomponent.CTextField();
        lblPredefinitonInst = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR5 = new com.see.truetransact.uicomponent.CPanel();
        rdoPredefinitonInst_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPredefinitonInst_yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblApplicableDivision = new com.see.truetransact.uicomponent.CLabel();
        panApplicableDivision = new com.see.truetransact.uicomponent.CPanel();
        rdoApplicableDivision_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoApplicableDivision_yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblApplicable1 = new com.see.truetransact.uicomponent.CLabel();
        lblApplicable2 = new com.see.truetransact.uicomponent.CLabel();
        lblApplicable3 = new com.see.truetransact.uicomponent.CLabel();
        lblApplicable4 = new com.see.truetransact.uicomponent.CLabel();
        chkApplicable1 = new com.see.truetransact.uicomponent.CCheckBox();
        chkApplicable2 = new com.see.truetransact.uicomponent.CCheckBox();
        chkApplicable3 = new com.see.truetransact.uicomponent.CCheckBox();
        chkApplicable4 = new com.see.truetransact.uicomponent.CCheckBox();
        lblClosureRate = new com.see.truetransact.uicomponent.CLabel();
        txtClosureRate = new com.see.truetransact.uicomponent.CTextField();
        lblSacnNo = new com.see.truetransact.uicomponent.CLabel();
        txtSancNo = new com.see.truetransact.uicomponent.CTextField();
        lblSacncDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSancDt = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        lblMDSType = new com.see.truetransact.uicomponent.CLabel();
        txtMdsType = new com.see.truetransact.uicomponent.CTextField();
        lblAutionTime = new com.see.truetransact.uicomponent.CLabel();
        txtAutionTime = new com.see.truetransact.uicomponent.CTextField();
        chkBankSettlement = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cboGroupName = new com.see.truetransact.uicomponent.CComboBox();
        panInsideSchemeDetail = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        lbllResolutionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtlResolutionDate = new com.see.truetransact.uicomponent.CDateField();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        lblProductDescVal = new com.see.truetransact.uicomponent.CLabel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        txtSchemeDesc = new com.see.truetransact.uicomponent.CTextField();
        lblSchemeDesc = new com.see.truetransact.uicomponent.CLabel();
        lblMultipleMembers = new com.see.truetransact.uicomponent.CLabel();
        panMultipleMembers = new com.see.truetransact.uicomponent.CPanel();
        rdoMultipleMembers_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMultipleMembers_yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblTotAmtUnderScheme = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmtUnderScheme = new com.see.truetransact.uicomponent.CTextField();
        lblInstFreq = new com.see.truetransact.uicomponent.CLabel();
        cboInstFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblSchemeStDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSchemeStDt = new com.see.truetransact.uicomponent.CDateField();
        tdtSchemeEndDt = new com.see.truetransact.uicomponent.CDateField();
        lblSchemeEndDt = new com.see.truetransact.uicomponent.CLabel();
        panDivisionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblNoofDivision = new com.see.truetransact.uicomponent.CLabel();
        txtNoofDivision = new com.see.truetransact.uicomponent.CTextField();
        lblNoofAuctions = new com.see.truetransact.uicomponent.CLabel();
        txtNoofAuctions = new com.see.truetransact.uicomponent.CTextField();
        lblNoofDraws = new com.see.truetransact.uicomponent.CLabel();
        txtNoofDraws = new com.see.truetransact.uicomponent.CTextField();
        lblNoofMemberPer = new com.see.truetransact.uicomponent.CLabel();
        txtNoofMemberPer = new com.see.truetransact.uicomponent.CTextField();
        lblNoofMemberScheme = new com.see.truetransact.uicomponent.CLabel();
        txtNoofMemberScheme = new com.see.truetransact.uicomponent.CTextField();
        lblInstAmt = new com.see.truetransact.uicomponent.CLabel();
        txtInstAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNoofInst = new com.see.truetransact.uicomponent.CLabel();
        txtNoofInst = new com.see.truetransact.uicomponent.CTextField();
        lblTotAmtPerDivision = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmtPerDivision = new com.see.truetransact.uicomponent.CTextField();
        panCoChittalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMaxNoofMemberCoChittals = new com.see.truetransact.uicomponent.CLabel();
        txtNoofCoInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblNoofCoChittals = new com.see.truetransact.uicomponent.CLabel();
        lblCoChittalInstAmount = new com.see.truetransact.uicomponent.CLabel();
        lblNoofCoInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtCoChittalInstAmount = new com.see.truetransact.uicomponent.CTextField();
        txtMaxNoofMemberCoChittals = new com.see.truetransact.uicomponent.CTextField();
        txtNoofCoChittals = new com.see.truetransact.uicomponent.CTextField();
        lblSpecialScheme = new com.see.truetransact.uicomponent.CLabel();
        chkSpecialScheme = new com.see.truetransact.uicomponent.CCheckBox();
        lblSmsAlert = new com.see.truetransact.uicomponent.CLabel();
        chkSMSAlert = new com.see.truetransact.uicomponent.CCheckBox();
        chkBonusPrint = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtSchemeGraceperiod = new com.see.truetransact.uicomponent.CTextField();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        panInstallmentSchedule = new com.see.truetransact.uicomponent.CPanel();
        panInsSchedule = new com.see.truetransact.uicomponent.CPanel();
        panInstallSchedule = new com.see.truetransact.uicomponent.CPanel();
        panSchedule = new com.see.truetransact.uicomponent.CPanel();
        panScheduleTable = new com.see.truetransact.uicomponent.CPanel();
        srpScheduleTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSchedule = new com.see.truetransact.uicomponent.CTable();
        panScheduleDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInstallmentDt = new com.see.truetransact.uicomponent.CLabel();
        tdtInstallmentDt = new com.see.truetransact.uicomponent.CDateField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblBonus = new com.see.truetransact.uicomponent.CLabel();
        txtBonus = new com.see.truetransact.uicomponent.CTextField();
        lblPaymentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPaymentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblInstallmentNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentNumber = new com.see.truetransact.uicomponent.CLabel();
        panSchedBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSchedNew = new com.see.truetransact.uicomponent.CButton();
        btnSchedSave = new com.see.truetransact.uicomponent.CButton();
        btnSchedDelete = new com.see.truetransact.uicomponent.CButton();
        panNoticeCharges = new com.see.truetransact.uicomponent.CPanel();
        panNoticecharge_Amt = new com.see.truetransact.uicomponent.CPanel();
        lblNoticeType = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeChargeAmt = new com.see.truetransact.uicomponent.CLabel();
        panNoticeButton = new com.see.truetransact.uicomponent.CPanel();
        btnNotice_Charge_New = new com.see.truetransact.uicomponent.CButton();
        btnNotice_Charge_Save = new com.see.truetransact.uicomponent.CButton();
        btnNotice_Charge_Delete = new com.see.truetransact.uicomponent.CButton();
        cboNoticeType = new com.see.truetransact.uicomponent.CComboBox();
        lblPostageAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeChargeAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPostageChargeAmt = new com.see.truetransact.uicomponent.CTextField();
        panNoticeCharges_Table = new com.see.truetransact.uicomponent.CPanel();
        srpNoticeCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblNoticeCharges = new com.see.truetransact.uicomponent.CTable();
        panDiscountDetails = new com.see.truetransact.uicomponent.CPanel();
        chkDiscountFirstInst = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        txtDiscountAmt = new com.see.truetransact.uicomponent.CTextField();
        panOtherChrg = new com.see.truetransact.uicomponent.CPanel();
        lblOtherChrg = new com.see.truetransact.uicomponent.CLabel();
        txtOtherChrgeAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnOtherChrgAcHd = new com.see.truetransact.uicomponent.CButton();
        chkCreditStampAdvance = new com.see.truetransact.uicomponent.CCheckBox();
        panAccountHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        btnMiscellaneousHead = new com.see.truetransact.uicomponent.CButton();
        btnCommisionHead = new com.see.truetransact.uicomponent.CButton();
        btnBonusPayableHead = new com.see.truetransact.uicomponent.CButton();
        btnBonusReceivableHead = new com.see.truetransact.uicomponent.CButton();
        btnPenalHead = new com.see.truetransact.uicomponent.CButton();
        btnSuspenseHead = new com.see.truetransact.uicomponent.CButton();
        btnPaymentHead = new com.see.truetransact.uicomponent.CButton();
        btnReceiptHead = new com.see.truetransact.uicomponent.CButton();
        txtReceiptHead = new com.see.truetransact.uicomponent.CTextField();
        lblReceiptHead = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentHead = new com.see.truetransact.uicomponent.CLabel();
        txtPaymentHead = new com.see.truetransact.uicomponent.CTextField();
        lblSuspenseHead = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseHead = new com.see.truetransact.uicomponent.CTextField();
        txtPenalHead = new com.see.truetransact.uicomponent.CTextField();
        lblPenalHead = new com.see.truetransact.uicomponent.CLabel();
        lblBonusReceivableHead = new com.see.truetransact.uicomponent.CLabel();
        txtBonusReceivableHead = new com.see.truetransact.uicomponent.CTextField();
        txtBonusPayableHead = new com.see.truetransact.uicomponent.CTextField();
        lblBonusPayableHead = new com.see.truetransact.uicomponent.CLabel();
        lblCommisionHead = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionHead = new com.see.truetransact.uicomponent.CTextField();
        txtMiscellaneousHead = new com.see.truetransact.uicomponent.CTextField();
        lblMiscellaneousHead = new com.see.truetransact.uicomponent.CLabel();
        lblCaseExpensesHead = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeChargesHead = new com.see.truetransact.uicomponent.CLabel();
        lblBankingHead = new com.see.truetransact.uicomponent.CLabel();
        lblMunnalReceiptsHead = new com.see.truetransact.uicomponent.CLabel();
        txtMunnalReceiptsHead = new com.see.truetransact.uicomponent.CTextField();
        btnMunnalReceiptsHead = new com.see.truetransact.uicomponent.CButton();
        btnBankingHead = new com.see.truetransact.uicomponent.CButton();
        txtBankingHead = new com.see.truetransact.uicomponent.CTextField();
        btnNoticeChargesHead = new com.see.truetransact.uicomponent.CButton();
        txtNoticeChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnCaseExpensesHead = new com.see.truetransact.uicomponent.CButton();
        txtCaseExpensesHead = new com.see.truetransact.uicomponent.CTextField();
        btnThalayalBonusHead = new com.see.truetransact.uicomponent.CButton();
        txtThalayalBonusHead = new com.see.truetransact.uicomponent.CTextField();
        lblThalayalBonusHead = new com.see.truetransact.uicomponent.CLabel();
        lblThalayalReceiptsHead = new com.see.truetransact.uicomponent.CLabel();
        txtThalayalReceiptsHead = new com.see.truetransact.uicomponent.CTextField();
        btnThalayalReceiptsHead = new com.see.truetransact.uicomponent.CButton();
        lblMunnalBonusHead = new com.see.truetransact.uicomponent.CLabel();
        txtMunnalBonusHead = new com.see.truetransact.uicomponent.CTextField();
        btnMunnalBonusHead = new com.see.truetransact.uicomponent.CButton();
        lblDiscountHead = new com.see.truetransact.uicomponent.CLabel();
        txtDiscountHead = new com.see.truetransact.uicomponent.CTextField();
        btnDiscountHead = new com.see.truetransact.uicomponent.CButton();
        lblMDSPayableHead = new com.see.truetransact.uicomponent.CLabel();
        txtMDSPayableHead = new com.see.truetransact.uicomponent.CTextField();
        btnMDSPayableHead = new com.see.truetransact.uicomponent.CButton();
        lblMDSReceivableHead = new com.see.truetransact.uicomponent.CLabel();
        txtMDSReceivableHead = new com.see.truetransact.uicomponent.CTextField();
        btnMDSReceivableHead = new com.see.truetransact.uicomponent.CButton();
        lblSundryReceiptHead = new com.see.truetransact.uicomponent.CLabel();
        txtSundryReceiptHead = new com.see.truetransact.uicomponent.CTextField();
        btnSundryReceiptHead = new com.see.truetransact.uicomponent.CButton();
        lblSundryPaymentHead = new com.see.truetransact.uicomponent.CLabel();
        txtSundryPaymentHead = new com.see.truetransact.uicomponent.CTextField();
        btnSundryPaymentHead = new com.see.truetransact.uicomponent.CButton();
        panSuspenceDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoSuspenseGL = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSuspenseAccNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblSuspenseProdID = new com.see.truetransact.uicomponent.CLabel();
        cboSuspenseProdID = new com.see.truetransact.uicomponent.CComboBox();
        lblSuspenseAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnSuspenseAccNo = new com.see.truetransact.uicomponent.CButton();
        txtARCExpenseHead = new com.see.truetransact.uicomponent.CTextField();
        lblARCExpenseHead = new com.see.truetransact.uicomponent.CLabel();
        btnARCExpenseHead = new com.see.truetransact.uicomponent.CButton();
        txtEACostHead = new com.see.truetransact.uicomponent.CTextField();
        lblEACostHead = new com.see.truetransact.uicomponent.CLabel();
        btnEACostHead = new com.see.truetransact.uicomponent.CButton();
        txtEAExpenseHead = new com.see.truetransact.uicomponent.CTextField();
        lblEAExpenseHead = new com.see.truetransact.uicomponent.CLabel();
        btnEAExpenseHead = new com.see.truetransact.uicomponent.CButton();
        txtEPCostHead = new com.see.truetransact.uicomponent.CTextField();
        lblEPCostHead = new com.see.truetransact.uicomponent.CLabel();
        btnEPCostHead = new com.see.truetransact.uicomponent.CButton();
        txtEPExpenseHead = new com.see.truetransact.uicomponent.CTextField();
        lblEPExpenseHead = new com.see.truetransact.uicomponent.CLabel();
        btnEPExpenseHead = new com.see.truetransact.uicomponent.CButton();
        txtARCCostHead = new com.see.truetransact.uicomponent.CTextField();
        lblARCCostHead = new com.see.truetransact.uicomponent.CLabel();
        btnARCCostHead = new com.see.truetransact.uicomponent.CButton();
        lblStampAdvanceHead = new com.see.truetransact.uicomponent.CLabel();
        txtStampAdvanceHead = new com.see.truetransact.uicomponent.CTextField();
        btnStampAdvanceHead = new com.see.truetransact.uicomponent.CButton();
        lblPostageHead = new com.see.truetransact.uicomponent.CLabel();
        txtPostageHead = new com.see.truetransact.uicomponent.CTextField();
        btnPostageHead = new com.see.truetransact.uicomponent.CButton();
        lblChargeHead = new com.see.truetransact.uicomponent.CLabel();
        txtChargeHead = new com.see.truetransact.uicomponent.CTextField();
        btnChargeHead = new com.see.truetransact.uicomponent.CButton();
        txtPostAdvHead = new com.see.truetransact.uicomponent.CTextField();
        lblMdsPostAdv = new com.see.truetransact.uicomponent.CLabel();
        btnPostAdvHead = new com.see.truetransact.uicomponent.CButton();
        IS_REV_POST_ADV = new com.see.truetransact.uicomponent.CCheckBox();
        txtForFeitedPaymentHead = new com.see.truetransact.uicomponent.CTextField();
        lblForFeitedHead = new com.see.truetransact.uicomponent.CLabel();
        btnForFietedPaymentHead = new com.see.truetransact.uicomponent.CButton();
        btnCreateHead = new com.see.truetransact.uicomponent.CButton();
        lblLegalChrgHead = new com.see.truetransact.uicomponent.CLabel();
        txtLegalChrgHead = new com.see.truetransact.uicomponent.CTextField();
        btnLegalChrgAcHead = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtPartPayBonusRecoveryHead = new com.see.truetransact.uicomponent.CTextField();
        btnPartPayBonusRecoveryHead = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace40);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace41.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace41.setText("     ");
        lblSpace41.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace41);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCopy);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace46.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace46.setText("     ");
        lblSpace46.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace46);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabMdsType.setMinimumSize(new java.awt.Dimension(680, 480));
        tabMdsType.setPreferredSize(new java.awt.Dimension(680, 480));

        panSchemeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Scheme Details"));
        panSchemeDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panSchemeDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panSchemeDetails.setLayout(new java.awt.GridBagLayout());

        panChitDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panChitDetails.setMinimumSize(new java.awt.Dimension(380, 550));
        panChitDetails.setPreferredSize(new java.awt.Dimension(380, 550));
        panChitDetails.setRequestFocusEnabled(false);
        panChitDetails.setLayout(new java.awt.GridBagLayout());

        panNumberPattern.setMinimumSize(new java.awt.Dimension(130, 21));
        panNumberPattern.setPreferredSize(new java.awt.Dimension(130, 21));
        panNumberPattern.setLayout(new java.awt.GridBagLayout());

        txtChittalNumberPattern.setMinimumSize(new java.awt.Dimension(72, 21));
        txtChittalNumberPattern.setPreferredSize(new java.awt.Dimension(72, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNumberPattern.add(txtChittalNumberPattern, gridBagConstraints);

        txtSuffix.setMinimumSize(new java.awt.Dimension(45, 21));
        txtSuffix.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panNumberPattern.add(txtSuffix, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 33;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panChitDetails.add(panNumberPattern, gridBagConstraints);

        lblChittalNumberPattern.setText("Chittal Number Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 25, 0, 0);
        panChitDetails.add(lblChittalNumberPattern, gridBagConstraints);

        lblNextChittalNumber.setText("Next Chittal Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 128, 0, 0);
        panChitDetails.add(lblNextChittalNumber, gridBagConstraints);

        txtNextChittalNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panChitDetails.add(txtNextChittalNumber, gridBagConstraints);

        lblInstallmentDay.setText("Installment Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 155, 0, 0);
        panChitDetails.add(lblInstallmentDay, gridBagConstraints);

        cboInstallmentDay.setMinimumSize(new java.awt.Dimension(50, 21));
        cboInstallmentDay.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        panChitDetails.add(cboInstallmentDay, gridBagConstraints);

        cboAuctionDay.setMinimumSize(new java.awt.Dimension(50, 21));
        cboAuctionDay.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(cboAuctionDay, gridBagConstraints);

        lblAuctionDay.setText("Draw / Auction Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 133, 0, 0);
        panChitDetails.add(lblAuctionDay, gridBagConstraints);

        lblThalayalAllowed.setText("Thalayal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 148, 0, 0);
        panChitDetails.add(lblThalayalAllowed, gridBagConstraints);

        panThalayalAllowed.setLayout(new java.awt.GridBagLayout());

        rdoThalayalAllowed.add(rdoThalayalAllowed_no);
        rdoThalayalAllowed_no.setText("No");
        rdoThalayalAllowed_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoThalayalAllowed_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoThalayalAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoThalayalAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panThalayalAllowed.add(rdoThalayalAllowed_no, gridBagConstraints);

        rdoThalayalAllowed.add(rdoThalayalAllowed_yes);
        rdoThalayalAllowed_yes.setText("Yes");
        rdoThalayalAllowed_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoThalayalAllowed_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoThalayalAllowed_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panThalayalAllowed.add(rdoThalayalAllowed_yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(panThalayalAllowed, gridBagConstraints);

        lblMunnalAllowed.setText("Munnal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 154, 0, 0);
        panChitDetails.add(lblMunnalAllowed, gridBagConstraints);

        panPrintServicesGR2.setLayout(new java.awt.GridBagLayout());

        rdoMunnalAllowed.add(rdoMunnalAllowed_Yes);
        rdoMunnalAllowed_Yes.setText("Yes");
        rdoMunnalAllowed_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoMunnalAllowed_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoMunnalAllowed_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR2.add(rdoMunnalAllowed_Yes, gridBagConstraints);

        rdoMunnalAllowed.add(rdoMunnalAllowed_no);
        rdoMunnalAllowed_no.setText("No");
        rdoMunnalAllowed_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoMunnalAllowed_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoMunnalAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMunnalAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR2.add(rdoMunnalAllowed_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(panPrintServicesGR2, gridBagConstraints);

        lblPaymentDone.setText("Payment will be done after");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panChitDetails.add(lblPaymentDone, gridBagConstraints);

        rdoPaymentDone.add(rdoPaymentDone_no);
        rdoPaymentDone_no.setText("Day");
        rdoPaymentDone_no.setMaximumSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_no.setMinimumSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_no.setPreferredSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPaymentDone_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 145, 0, 0);
        panChitDetails.add(rdoPaymentDone_no, gridBagConstraints);

        rdoPaymentDone.add(rdoPaymentDone_yes);
        rdoPaymentDone_yes.setText("Installments");
        rdoPaymentDone_yes.setMaximumSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_yes.setMinimumSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_yes.setPreferredSize(new java.awt.Dimension(98, 18));
        rdoPaymentDone_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPaymentDone_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 145, 0, 0);
        panChitDetails.add(rdoPaymentDone_yes, gridBagConstraints);

        txtInstallments.setText("0");
        txtInstallments.setMinimumSize(new java.awt.Dimension(30, 21));
        txtInstallments.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(txtInstallments, gridBagConstraints);

        txtDay.setText("0");
        txtDay.setMinimumSize(new java.awt.Dimension(30, 21));
        txtDay.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(txtDay, gridBagConstraints);

        lblPredefinitonInst.setText("Predefinition of  Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 89, 0, 0);
        panChitDetails.add(lblPredefinitonInst, gridBagConstraints);

        panPrintServicesGR5.setMinimumSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR5.setPreferredSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR5.setLayout(new java.awt.GridBagLayout());

        rdoPredefinitonInst.add(rdoPredefinitonInst_no);
        rdoPredefinitonInst_no.setText("No");
        rdoPredefinitonInst_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoPredefinitonInst_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoPredefinitonInst_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPredefinitonInst_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR5.add(rdoPredefinitonInst_no, gridBagConstraints);

        rdoPredefinitonInst.add(rdoPredefinitonInst_yes);
        rdoPredefinitonInst_yes.setText("Yes");
        rdoPredefinitonInst_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoPredefinitonInst_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoPredefinitonInst_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoPredefinitonInst_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPredefinitonInst_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR5.add(rdoPredefinitonInst_yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(panPrintServicesGR5, gridBagConstraints);

        lblApplicableDivision.setText("If Yes, Whether applicable to all Divisions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panChitDetails.add(lblApplicableDivision, gridBagConstraints);

        panApplicableDivision.setMinimumSize(new java.awt.Dimension(102, 18));
        panApplicableDivision.setPreferredSize(new java.awt.Dimension(102, 18));
        panApplicableDivision.setLayout(new java.awt.GridBagLayout());

        rdoApplicableDivision.add(rdoApplicableDivision_no);
        rdoApplicableDivision_no.setText("No");
        rdoApplicableDivision_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoApplicableDivision_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoApplicableDivision_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoApplicableDivision_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panApplicableDivision.add(rdoApplicableDivision_no, gridBagConstraints);

        rdoApplicableDivision.add(rdoApplicableDivision_yes);
        rdoApplicableDivision_yes.setText("Yes");
        rdoApplicableDivision_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoApplicableDivision_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoApplicableDivision_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoApplicableDivision_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoApplicableDivision_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panApplicableDivision.add(rdoApplicableDivision_yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(panApplicableDivision, gridBagConstraints);

        lblApplicable1.setText("If Not, Applicable to Division1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 77, 0, 0);
        panChitDetails.add(lblApplicable1, gridBagConstraints);

        lblApplicable2.setText(" Applicable to Division2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 111, 0, 0);
        panChitDetails.add(lblApplicable2, gridBagConstraints);

        lblApplicable3.setText("Applicable to Division3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 115, 0, 0);
        panChitDetails.add(lblApplicable3, gridBagConstraints);

        lblApplicable4.setText("Applicable to Division4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 115, 0, 0);
        panChitDetails.add(lblApplicable4, gridBagConstraints);

        chkApplicable1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkApplicable1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        panChitDetails.add(chkApplicable1, gridBagConstraints);

        chkApplicable2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkApplicable2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        panChitDetails.add(chkApplicable2, gridBagConstraints);

        chkApplicable3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkApplicable3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panChitDetails.add(chkApplicable3, gridBagConstraints);

        chkApplicable4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkApplicable4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panChitDetails.add(chkApplicable4, gridBagConstraints);

        lblClosureRate.setText("Closure Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 13, 0, 0);
        panChitDetails.add(lblClosureRate, gridBagConstraints);

        txtClosureRate.setAllowNumber(true);
        txtClosureRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.gridwidth = 26;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panChitDetails.add(txtClosureRate, gridBagConstraints);

        lblSacnNo.setText("Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 17, 0, 0);
        panChitDetails.add(lblSacnNo, gridBagConstraints);

        txtSancNo.setAllowAll(true);
        txtSancNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panChitDetails.add(txtSancNo, gridBagConstraints);

        lblSacncDt.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        panChitDetails.add(lblSacncDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panChitDetails.add(tdtSancDt, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 36, 0, 0);
        panChitDetails.add(lblRemarks, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 80));

        txaRemarks.setColumns(20);
        txaRemarks.setRows(5);
        jScrollPane1.setViewportView(txaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = -66;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panChitDetails.add(jScrollPane1, gridBagConstraints);

        lblMDSType.setText("MDS Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 27, 0, 0);
        panChitDetails.add(lblMDSType, gridBagConstraints);

        txtMdsType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.gridwidth = 26;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panChitDetails.add(txtMdsType, gridBagConstraints);

        lblAutionTime.setText("Aution Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 19, 0, 0);
        panChitDetails.add(lblAutionTime, gridBagConstraints);

        txtAutionTime.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.gridwidth = 26;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panChitDetails.add(txtAutionTime, gridBagConstraints);

        chkBankSettlement.setText("Change Member Bank Settlement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 15, 2, 0);
        panChitDetails.add(chkBankSettlement, gridBagConstraints);

        cLabel1.setText("Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        panChitDetails.add(cLabel1, gridBagConstraints);

        cboGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGroupNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.gridwidth = 34;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panChitDetails.add(cboGroupName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 6, 9, 10);
        panSchemeDetails.add(panChitDetails, gridBagConstraints);

        panInsideSchemeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsideSchemeDetail.setMinimumSize(new java.awt.Dimension(435, 520));
        panInsideSchemeDetail.setPreferredSize(new java.awt.Dimension(435, 570));
        panInsideSchemeDetail.setRequestFocusEnabled(false);

        lblResolutionNo.setText("Resolution No");

        txtResolutionNo.setAllowAll(true);
        txtResolutionNo.setMinimumSize(new java.awt.Dimension(100, 21));

        lbllResolutionDate.setText("Resolution Date");

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });

        lblProductId.setText("Product Id");

        lblProductDesc.setText("Product Description");

        lblProductDescVal.setForeground(new java.awt.Color(0, 51, 204));
        lblProductDescVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblProductDescVal.setMaximumSize(new java.awt.Dimension(130, 21));
        lblProductDescVal.setMinimumSize(new java.awt.Dimension(130, 21));
        lblProductDescVal.setPreferredSize(new java.awt.Dimension(130, 21));

        lblSchemeName.setText("MDS Scheme Name");

        txtSchemeName.setAllowAll(true);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSchemeNameActionPerformed(evt);
            }
        });
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });

        txtSchemeDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSchemeDescFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeDescFocusLost(evt);
            }
        });

        lblSchemeDesc.setText("MDS Scheme Description");

        lblMultipleMembers.setText("Allow  Multiple Members to Own a Chit Number");

        panMultipleMembers.setMinimumSize(new java.awt.Dimension(102, 18));
        panMultipleMembers.setPreferredSize(new java.awt.Dimension(102, 18));
        panMultipleMembers.setLayout(new java.awt.GridBagLayout());

        rdoMultipleMembers.add(rdoMultipleMembers_no);
        rdoMultipleMembers_no.setText("No");
        rdoMultipleMembers_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoMultipleMembers_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoMultipleMembers_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultipleMembers_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMultipleMembers.add(rdoMultipleMembers_no, gridBagConstraints);

        rdoMultipleMembers.add(rdoMultipleMembers_yes);
        rdoMultipleMembers_yes.setText("Yes");
        rdoMultipleMembers_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoMultipleMembers_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoMultipleMembers_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoMultipleMembers_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultipleMembers_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMultipleMembers.add(rdoMultipleMembers_yes, gridBagConstraints);

        lblTotAmtUnderScheme.setText("Total amount under the Scheme");

        txtTotAmtUnderScheme.setMinimumSize(new java.awt.Dimension(100, 21));

        lblInstFreq.setText("Installment frequency");

        cboInstFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstFreqActionPerformed(evt);
            }
        });
        cboInstFreq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInstFreqFocusLost(evt);
            }
        });

        lblSchemeStDt.setText("Scheme Start Date");

        tdtSchemeStDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSchemeStDtFocusLost(evt);
            }
        });

        tdtSchemeEndDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSchemeEndDtFocusLost(evt);
            }
        });

        lblSchemeEndDt.setText("Scheme End Date");

        panDivisionDetails.setMinimumSize(new java.awt.Dimension(420, 200));
        panDivisionDetails.setPreferredSize(new java.awt.Dimension(420, 200));
        panDivisionDetails.setRequestFocusEnabled(false);
        panDivisionDetails.setLayout(new java.awt.GridBagLayout());

        lblNoofDivision.setText("No.of Divisions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDivisionDetails.add(lblNoofDivision, gridBagConstraints);

        txtNoofDivision.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofDivision.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofDivision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofDivisionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofDivision, gridBagConstraints);

        lblNoofAuctions.setText("No. of Auctions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblNoofAuctions, gridBagConstraints);

        txtNoofAuctions.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofAuctions.setNextFocusableComponent(txtNoofDraws);
        txtNoofAuctions.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofAuctions.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofAuctionsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofAuctions, gridBagConstraints);

        lblNoofDraws.setText("No. of Draws");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblNoofDraws, gridBagConstraints);

        txtNoofDraws.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofDraws.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofDraws.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofDrawsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofDraws, gridBagConstraints);

        lblNoofMemberPer.setText("No. of members per division");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblNoofMemberPer, gridBagConstraints);

        txtNoofMemberPer.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofMemberPer.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofMemberPer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofMemberPerFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofMemberPer, gridBagConstraints);

        lblNoofMemberScheme.setText("Total no. of members in the scheme");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 14, 2, 2);
        panDivisionDetails.add(lblNoofMemberScheme, gridBagConstraints);

        txtNoofMemberScheme.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofMemberScheme.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofMemberScheme, gridBagConstraints);

        lblInstAmt.setText("Installment amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblInstAmt, gridBagConstraints);

        txtInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtInstAmt, gridBagConstraints);

        lblNoofInst.setText("No. of installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblNoofInst, gridBagConstraints);

        txtNoofInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtNoofInst, gridBagConstraints);

        lblTotAmtPerDivision.setText("Total amount per Division");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDivisionDetails.add(lblTotAmtPerDivision, gridBagConstraints);

        txtTotAmtPerDivision.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panDivisionDetails.add(txtTotAmtPerDivision, gridBagConstraints);

        panCoChittalDetails.setMinimumSize(new java.awt.Dimension(435, 103));
        panCoChittalDetails.setPreferredSize(new java.awt.Dimension(435, 103));
        panCoChittalDetails.setRequestFocusEnabled(false);
        panCoChittalDetails.setLayout(new java.awt.GridBagLayout());

        lblMaxNoofMemberCoChittals.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxNoofMemberCoChittals.setText("Max No of Members Including Co-Chittals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCoChittalDetails.add(lblMaxNoofMemberCoChittals, gridBagConstraints);

        txtNoofCoInstallments.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofCoInstallments.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofCoInstallments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofCoInstallmentsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panCoChittalDetails.add(txtNoofCoInstallments, gridBagConstraints);

        lblNoofCoChittals.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoofCoChittals.setText("No of Co-Chittals Allowed for Each Chit No");
        lblNoofCoChittals.setMaximumSize(new java.awt.Dimension(272, 18));
        lblNoofCoChittals.setMinimumSize(new java.awt.Dimension(272, 18));
        lblNoofCoChittals.setPreferredSize(new java.awt.Dimension(272, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panCoChittalDetails.add(lblNoofCoChittals, gridBagConstraints);

        lblCoChittalInstAmount.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panCoChittalDetails.add(lblCoChittalInstAmount, gridBagConstraints);

        lblNoofCoInstallments.setText("Number Of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCoChittalDetails.add(lblNoofCoInstallments, gridBagConstraints);

        txtCoChittalInstAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCoChittalInstAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCoChittalInstAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 62);
        panCoChittalDetails.add(txtCoChittalInstAmount, gridBagConstraints);

        txtMaxNoofMemberCoChittals.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxNoofMemberCoChittals.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panCoChittalDetails.add(txtMaxNoofMemberCoChittals, gridBagConstraints);

        txtNoofCoChittals.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoofCoChittals.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoofCoChittals.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoofCoChittalsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 2);
        panCoChittalDetails.add(txtNoofCoChittals, gridBagConstraints);

        lblSpecialScheme.setText("Is a Special Scheme");

        chkSpecialScheme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSpecialSchemeItemStateChanged(evt);
            }
        });

        lblSmsAlert.setText("SMS Alert");

        chkSMSAlert.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSMSAlertItemStateChanged(evt);
            }
        });

        chkBonusPrint.setText("Bonus Transaction Print");
        chkBonusPrint.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBonusPrint.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cLabel3.setText("Scheme Grace Period");

        txtSchemeGraceperiod.setAllowNumber(true);
        txtSchemeGraceperiod.setMinimumSize(new java.awt.Dimension(100, 21));

        cLabel4.setText("Days");

        javax.swing.GroupLayout panInsideSchemeDetailLayout = new javax.swing.GroupLayout(panInsideSchemeDetail);
        panInsideSchemeDetail.setLayout(panInsideSchemeDetailLayout);
        panInsideSchemeDetailLayout.setHorizontalGroup(
            panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(lblResolutionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtResolutionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addComponent(lbllResolutionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(tdtlResolutionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(215, 215, 215)
                .addComponent(lblProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cboProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(lblProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblProductDescVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(lblSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(txtSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(lblSpecialScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(chkSpecialScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(lblSchemeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(txtSchemeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addComponent(lblMultipleMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(panMultipleMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panCoChittalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(panDivisionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(lblTotAmtUnderScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtTotAmtUnderScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(lblInstFreq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cboInstFreq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(lblSchemeStDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(tdtSchemeStDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                        .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSchemeGraceperiod, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                                .addComponent(lblSmsAlert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(chkSMSAlert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(chkBonusPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                                .addComponent(lblSchemeEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(tdtSchemeEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panInsideSchemeDetailLayout.setVerticalGroup(
            panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblResolutionNo, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtResolutionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lbllResolutionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtlResolutionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblProductDescVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSpecialScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkSpecialScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSchemeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSchemeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMultipleMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panMultipleMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(panCoChittalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panDivisionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTotAmtUnderScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTotAmtUnderScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblInstFreq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboInstFreq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSchemeStDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtSchemeStDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSchemeEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtSchemeEndDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(chkBonusPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panInsideSchemeDetailLayout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(lblSmsAlert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(chkSMSAlert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(panInsideSchemeDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSchemeGraceperiod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 9, 0, 0);
        panSchemeDetails.add(panInsideSchemeDetail, gridBagConstraints);

        tabMdsType.addTab("Scheme Details", panSchemeDetails);

        panInstallmentSchedule.setMinimumSize(new java.awt.Dimension(790, 700));
        panInstallmentSchedule.setPreferredSize(new java.awt.Dimension(790, 700));
        panInstallmentSchedule.setLayout(new java.awt.GridBagLayout());

        panInsSchedule.setMinimumSize(new java.awt.Dimension(808, 330));
        panInsSchedule.setPreferredSize(new java.awt.Dimension(808, 330));
        panInsSchedule.setLayout(new java.awt.GridBagLayout());

        panInstallSchedule.setMinimumSize(new java.awt.Dimension(800, 300));
        panInstallSchedule.setPreferredSize(new java.awt.Dimension(800, 300));
        panInstallSchedule.setLayout(new java.awt.GridBagLayout());

        panSchedule.setBorder(javax.swing.BorderFactory.createTitledBorder("Installment Schedule Details"));
        panSchedule.setMinimumSize(new java.awt.Dimension(790, 280));
        panSchedule.setPreferredSize(new java.awt.Dimension(790, 280));
        panSchedule.setLayout(new java.awt.GridBagLayout());

        panScheduleTable.setMinimumSize(new java.awt.Dimension(450, 200));
        panScheduleTable.setPreferredSize(new java.awt.Dimension(450, 200));
        panScheduleTable.setLayout(new java.awt.GridBagLayout());

        srpScheduleTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpScheduleTable.setPreferredSize(new java.awt.Dimension(450, 200));

        tblSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Installments No", "Installment Date", "Amount", "Bonus", "Payment Amount"
            }
        ));
        tblSchedule.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblScheduleMousePressed(evt);
            }
        });
        srpScheduleTable.setViewportView(tblSchedule);

        panScheduleTable.add(srpScheduleTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSchedule.add(panScheduleTable, gridBagConstraints);

        panScheduleDetails.setMinimumSize(new java.awt.Dimension(225, 250));
        panScheduleDetails.setPreferredSize(new java.awt.Dimension(275, 250));
        panScheduleDetails.setRequestFocusEnabled(false);
        panScheduleDetails.setLayout(new java.awt.GridBagLayout());

        lblInstallmentDt.setText("Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panScheduleDetails.add(lblInstallmentDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 0);
        panScheduleDetails.add(tdtInstallmentDt, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panScheduleDetails.add(lblAmount, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 0);
        panScheduleDetails.add(txtAmount, gridBagConstraints);

        lblBonus.setText("Bonus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panScheduleDetails.add(lblBonus, gridBagConstraints);

        txtBonus.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBonus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 0);
        panScheduleDetails.add(txtBonus, gridBagConstraints);

        lblPaymentAmount.setText("Payment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panScheduleDetails.add(lblPaymentAmount, gridBagConstraints);

        txtPaymentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 0);
        panScheduleDetails.add(txtPaymentAmount, gridBagConstraints);

        lblInstallmentNo.setText("Installment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panScheduleDetails.add(lblInstallmentNo, gridBagConstraints);

        lblInstallmentNumber.setText("               ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 9, 1, 0);
        panScheduleDetails.add(lblInstallmentNumber, gridBagConstraints);

        panSchedBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panSchedBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panSchedBtn.setLayout(new java.awt.GridBagLayout());

        btnSchedNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSchedNew.setToolTipText("New");
        btnSchedNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedNew, gridBagConstraints);

        btnSchedSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSchedSave.setToolTipText("Save");
        btnSchedSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedSave.setName("btnContactNoAdd"); // NOI18N
        btnSchedSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedSave, gridBagConstraints);

        btnSchedDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSchedDelete.setToolTipText("Delete");
        btnSchedDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSchedDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedBtn.add(btnSchedDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panScheduleDetails.add(panSchedBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSchedule.add(panScheduleDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 35, 0);
        panInstallSchedule.add(panSchedule, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsSchedule.add(panInstallSchedule, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentSchedule.add(panInsSchedule, gridBagConstraints);

        panNoticeCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("Notice Charges"));
        panNoticeCharges.setMinimumSize(new java.awt.Dimension(750, 300));
        panNoticeCharges.setPreferredSize(new java.awt.Dimension(750, 300));
        panNoticeCharges.setLayout(new java.awt.GridBagLayout());

        panNoticecharge_Amt.setMinimumSize(new java.awt.Dimension(240, 150));
        panNoticecharge_Amt.setPreferredSize(new java.awt.Dimension(240, 150));
        panNoticecharge_Amt.setLayout(new java.awt.GridBagLayout());

        lblNoticeType.setText("Notiice Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblNoticeType, gridBagConstraints);

        lblNoticeChargeAmt.setText("Notice Charge Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblNoticeChargeAmt, gridBagConstraints);

        panNoticeButton.setLayout(new java.awt.GridBagLayout());

        btnNotice_Charge_New.setText("New");
        btnNotice_Charge_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_NewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_New, gridBagConstraints);

        btnNotice_Charge_Save.setText("Save");
        btnNotice_Charge_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_Save, gridBagConstraints);

        btnNotice_Charge_Delete.setText("Delete");
        btnNotice_Charge_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_DeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_Delete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNoticecharge_Amt.add(panNoticeButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNoticecharge_Amt.add(cboNoticeType, gridBagConstraints);

        lblPostageAmt.setText("Postage Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblPostageAmt, gridBagConstraints);

        txtNoticeChargeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeChargeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNoticecharge_Amt.add(txtNoticeChargeAmt, gridBagConstraints);

        txtPostageChargeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageChargeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNoticecharge_Amt.add(txtPostageChargeAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeCharges.add(panNoticecharge_Amt, gridBagConstraints);

        panNoticeCharges_Table.setMinimumSize(new java.awt.Dimension(320, 130));
        panNoticeCharges_Table.setPreferredSize(new java.awt.Dimension(320, 130));
        panNoticeCharges_Table.setLayout(new java.awt.GridBagLayout());

        srpNoticeCharges.setMinimumSize(new java.awt.Dimension(310, 105));
        srpNoticeCharges.setPreferredSize(new java.awt.Dimension(310, 105));

        tblNoticeCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NoticeType", "NoticeChargeAmt", "Postage_Amt"
            }
        ));
        tblNoticeCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblNoticeChargesMousePressed(evt);
            }
        });
        srpNoticeCharges.setViewportView(tblNoticeCharges);

        panNoticeCharges_Table.add(srpNoticeCharges, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 4);
        panNoticeCharges.add(panNoticeCharges_Table, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(40, 4, 4, 4);
        panInstallmentSchedule.add(panNoticeCharges, gridBagConstraints);

        panDiscountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Discount Details"));
        panDiscountDetails.setLayout(new java.awt.GridBagLayout());

        chkDiscountFirstInst.setText("Discount For First Installment");
        chkDiscountFirstInst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDiscountFirstInstActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDiscountDetails.add(chkDiscountFirstInst, gridBagConstraints);

        cLabel5.setText("First Installment Discount Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 59, 0, 0);
        panDiscountDetails.add(cLabel5, gridBagConstraints);

        txtDiscountAmt.setAllowNumber(true);
        txtDiscountAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panDiscountDetails.add(txtDiscountAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 148, 0, 0);
        panInstallmentSchedule.add(panDiscountDetails, gridBagConstraints);

        panOtherChrg.setLayout(new java.awt.GridBagLayout());

        lblOtherChrg.setText("Other Charge A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 24, 0, 0);
        panOtherChrg.add(lblOtherChrg, gridBagConstraints);

        txtOtherChrgeAcHd.setMinimumSize(new java.awt.Dimension(120, 21));
        txtOtherChrgeAcHd.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 0, 0);
        panOtherChrg.add(txtOtherChrgeAcHd, gridBagConstraints);

        btnOtherChrgAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOtherChrgAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherChrgAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panOtherChrg.add(btnOtherChrgAcHd, gridBagConstraints);

        chkCreditStampAdvance.setText("Credit Stamp Advance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panOtherChrg.add(chkCreditStampAdvance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panInstallmentSchedule.add(panOtherChrg, gridBagConstraints);

        tabMdsType.addTab("Notice Charges", panInstallmentSchedule);

        panAccountHeadDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountHeadDetails.setMinimumSize(new java.awt.Dimension(405, 425));
        panAccountHeadDetails.setPreferredSize(new java.awt.Dimension(405, 425));
        panAccountHeadDetails.setRequestFocusEnabled(false);
        panAccountHeadDetails.setLayout(new java.awt.GridBagLayout());

        panAccountHead.setBorder(javax.swing.BorderFactory.createTitledBorder("Acc Head Details"));
        panAccountHead.setMaximumSize(new java.awt.Dimension(800, 530));
        panAccountHead.setMinimumSize(new java.awt.Dimension(800, 630));
        panAccountHead.setPreferredSize(new java.awt.Dimension(800, 630));
        panAccountHead.setRequestFocusEnabled(false);
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        btnMiscellaneousHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMiscellaneousHead.setEnabled(false);
        btnMiscellaneousHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMiscellaneousHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMiscellaneousHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiscellaneousHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnMiscellaneousHead, gridBagConstraints);

        btnCommisionHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommisionHead.setEnabled(false);
        btnCommisionHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommisionHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommisionHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommisionHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnCommisionHead, gridBagConstraints);

        btnBonusPayableHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBonusPayableHead.setEnabled(false);
        btnBonusPayableHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBonusPayableHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBonusPayableHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBonusPayableHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnBonusPayableHead, gridBagConstraints);

        btnBonusReceivableHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBonusReceivableHead.setEnabled(false);
        btnBonusReceivableHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBonusReceivableHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBonusReceivableHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBonusReceivableHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnBonusReceivableHead, gridBagConstraints);

        btnPenalHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalHead.setEnabled(false);
        btnPenalHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPenalHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnPenalHead, gridBagConstraints);

        btnSuspenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseHead.setEnabled(false);
        btnSuspenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSuspenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnSuspenseHead, gridBagConstraints);

        btnPaymentHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPaymentHead.setEnabled(false);
        btnPaymentHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPaymentHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPaymentHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnPaymentHead, gridBagConstraints);

        btnReceiptHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReceiptHead.setEnabled(false);
        btnReceiptHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnReceiptHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnReceiptHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiptHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnReceiptHead, gridBagConstraints);

        txtReceiptHead.setEnabled(false);
        txtReceiptHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtReceiptHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtReceiptHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReceiptHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtReceiptHead, gridBagConstraints);

        lblReceiptHead.setText("Receipt A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblReceiptHead, gridBagConstraints);

        lblPaymentHead.setText("Payment A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPaymentHead, gridBagConstraints);

        txtPaymentHead.setEnabled(false);
        txtPaymentHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtPaymentHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtPaymentHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPaymentHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtPaymentHead, gridBagConstraints);

        lblSuspenseHead.setText("Suspense A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSuspenseHead, gridBagConstraints);

        txtSuspenseHead.setEnabled(false);
        txtSuspenseHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtSuspenseHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtSuspenseHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuspenseHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtSuspenseHead, gridBagConstraints);

        txtPenalHead.setEnabled(false);
        txtPenalHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtPenalHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtPenalHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtPenalHead, gridBagConstraints);

        lblPenalHead.setText("Penal Interest A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPenalHead, gridBagConstraints);

        lblBonusReceivableHead.setText("Bonus Receivable A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblBonusReceivableHead, gridBagConstraints);

        txtBonusReceivableHead.setEnabled(false);
        txtBonusReceivableHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtBonusReceivableHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtBonusReceivableHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusReceivableHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtBonusReceivableHead, gridBagConstraints);

        txtBonusPayableHead.setEnabled(false);
        txtBonusPayableHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtBonusPayableHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtBonusPayableHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusPayableHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtBonusPayableHead, gridBagConstraints);

        lblBonusPayableHead.setText("Bonus Payable A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblBonusPayableHead, gridBagConstraints);

        lblCommisionHead.setText("Commission A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCommisionHead, gridBagConstraints);

        txtCommisionHead.setEnabled(false);
        txtCommisionHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtCommisionHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtCommisionHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommisionHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtCommisionHead, gridBagConstraints);

        txtMiscellaneousHead.setEnabled(false);
        txtMiscellaneousHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtMiscellaneousHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtMiscellaneousHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscellaneousHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtMiscellaneousHead, gridBagConstraints);

        lblMiscellaneousHead.setText("Miscellaneous A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMiscellaneousHead, gridBagConstraints);

        lblCaseExpensesHead.setText("ARC,EA,EP SuspenseA/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCaseExpensesHead, gridBagConstraints);

        lblNoticeChargesHead.setText("Notice Charges A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblNoticeChargesHead, gridBagConstraints);

        lblBankingHead.setText("Bank Advance A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblBankingHead, gridBagConstraints);

        lblMunnalReceiptsHead.setText("Munnal Receipts/Payments A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 19, 4, 4);
        panAccountHead.add(lblMunnalReceiptsHead, gridBagConstraints);

        txtMunnalReceiptsHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtMunnalReceiptsHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtMunnalReceiptsHead.setEnabled(false);
        txtMunnalReceiptsHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMunnalReceiptsHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtMunnalReceiptsHead, gridBagConstraints);

        btnMunnalReceiptsHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMunnalReceiptsHead.setEnabled(false);
        btnMunnalReceiptsHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMunnalReceiptsHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMunnalReceiptsHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMunnalReceiptsHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnMunnalReceiptsHead, gridBagConstraints);

        btnBankingHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankingHead.setEnabled(false);
        btnBankingHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankingHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankingHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankingHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnBankingHead, gridBagConstraints);

        txtBankingHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtBankingHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtBankingHead.setEnabled(false);
        txtBankingHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankingHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtBankingHead, gridBagConstraints);

        btnNoticeChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticeChargesHead.setEnabled(false);
        btnNoticeChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNoticeChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNoticeChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticeChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnNoticeChargesHead, gridBagConstraints);

        txtNoticeChargesHead.setEnabled(false);
        txtNoticeChargesHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtNoticeChargesHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtNoticeChargesHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeChargesHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtNoticeChargesHead, gridBagConstraints);

        btnCaseExpensesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCaseExpensesHead.setEnabled(false);
        btnCaseExpensesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCaseExpensesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCaseExpensesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaseExpensesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnCaseExpensesHead, gridBagConstraints);

        txtCaseExpensesHead.setEnabled(false);
        txtCaseExpensesHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtCaseExpensesHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtCaseExpensesHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCaseExpensesHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtCaseExpensesHead, gridBagConstraints);

        btnThalayalBonusHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnThalayalBonusHead.setEnabled(false);
        btnThalayalBonusHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnThalayalBonusHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnThalayalBonusHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThalayalBonusHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnThalayalBonusHead, gridBagConstraints);

        txtThalayalBonusHead.setEnabled(false);
        txtThalayalBonusHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtThalayalBonusHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtThalayalBonusHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtThalayalBonusHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtThalayalBonusHead, gridBagConstraints);

        lblThalayalBonusHead.setText("Thalayal VP Suspense A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblThalayalBonusHead, gridBagConstraints);

        lblThalayalReceiptsHead.setText("Thalayal MDS A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblThalayalReceiptsHead, gridBagConstraints);

        txtThalayalReceiptsHead.setEnabled(false);
        txtThalayalReceiptsHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtThalayalReceiptsHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtThalayalReceiptsHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtThalayalReceiptsHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtThalayalReceiptsHead, gridBagConstraints);

        btnThalayalReceiptsHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnThalayalReceiptsHead.setEnabled(false);
        btnThalayalReceiptsHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnThalayalReceiptsHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnThalayalReceiptsHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThalayalReceiptsHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnThalayalReceiptsHead, gridBagConstraints);

        lblMunnalBonusHead.setText("Munnal MDS A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMunnalBonusHead, gridBagConstraints);

        txtMunnalBonusHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtMunnalBonusHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtMunnalBonusHead.setEnabled(false);
        txtMunnalBonusHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMunnalBonusHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtMunnalBonusHead, gridBagConstraints);

        btnMunnalBonusHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMunnalBonusHead.setEnabled(false);
        btnMunnalBonusHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMunnalBonusHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMunnalBonusHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMunnalBonusHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnMunnalBonusHead, gridBagConstraints);

        lblDiscountHead.setText("Discount A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblDiscountHead, gridBagConstraints);

        txtDiscountHead.setEnabled(false);
        txtDiscountHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtDiscountHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtDiscountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtDiscountHead, gridBagConstraints);

        btnDiscountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDiscountHead.setEnabled(false);
        btnDiscountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDiscountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDiscountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnDiscountHead, gridBagConstraints);

        lblMDSPayableHead.setText("MDS Payable A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMDSPayableHead, gridBagConstraints);

        txtMDSPayableHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtMDSPayableHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtMDSPayableHead.setEnabled(false);
        txtMDSPayableHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMDSPayableHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtMDSPayableHead, gridBagConstraints);

        btnMDSPayableHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMDSPayableHead.setEnabled(false);
        btnMDSPayableHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMDSPayableHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMDSPayableHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSPayableHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnMDSPayableHead, gridBagConstraints);

        lblMDSReceivableHead.setText("MDS Receivable A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMDSReceivableHead, gridBagConstraints);

        txtMDSReceivableHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtMDSReceivableHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtMDSReceivableHead.setEnabled(false);
        txtMDSReceivableHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMDSReceivableHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtMDSReceivableHead, gridBagConstraints);

        btnMDSReceivableHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMDSReceivableHead.setEnabled(false);
        btnMDSReceivableHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMDSReceivableHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMDSReceivableHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMDSReceivableHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnMDSReceivableHead, gridBagConstraints);

        lblSundryReceiptHead.setText("Sundry Receipts A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSundryReceiptHead, gridBagConstraints);

        txtSundryReceiptHead.setEnabled(false);
        txtSundryReceiptHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtSundryReceiptHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtSundryReceiptHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSundryReceiptHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtSundryReceiptHead, gridBagConstraints);

        btnSundryReceiptHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSundryReceiptHead.setEnabled(false);
        btnSundryReceiptHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSundryReceiptHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSundryReceiptHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSundryReceiptHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnSundryReceiptHead, gridBagConstraints);

        lblSundryPaymentHead.setText("Sundry Payments A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSundryPaymentHead, gridBagConstraints);

        txtSundryPaymentHead.setEnabled(false);
        txtSundryPaymentHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtSundryPaymentHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtSundryPaymentHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSundryPaymentHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtSundryPaymentHead, gridBagConstraints);

        btnSundryPaymentHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSundryPaymentHead.setEnabled(false);
        btnSundryPaymentHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSundryPaymentHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSundryPaymentHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSundryPaymentHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnSundryPaymentHead, gridBagConstraints);

        panSuspenceDetails.setMinimumSize(new java.awt.Dimension(302, 18));
        panSuspenceDetails.setPreferredSize(new java.awt.Dimension(302, 18));
        panSuspenceDetails.setLayout(new java.awt.GridBagLayout());

        rdoSuspense.add(rdoSuspenseGL);
        rdoSuspenseGL.setText("Suspense GL");
        rdoSuspenseGL.setMaximumSize(new java.awt.Dimension(130, 18));
        rdoSuspenseGL.setMinimumSize(new java.awt.Dimension(130, 18));
        rdoSuspenseGL.setPreferredSize(new java.awt.Dimension(130, 18));
        rdoSuspenseGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSuspenseGLActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSuspenceDetails.add(rdoSuspenseGL, gridBagConstraints);

        rdoSuspense.add(rdoSuspenseAccNo);
        rdoSuspenseAccNo.setText("Suspense Account No");
        rdoSuspenseAccNo.setMaximumSize(new java.awt.Dimension(160, 18));
        rdoSuspenseAccNo.setMinimumSize(new java.awt.Dimension(160, 18));
        rdoSuspenseAccNo.setPreferredSize(new java.awt.Dimension(160, 18));
        rdoSuspenseAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSuspenseAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSuspenceDetails.add(rdoSuspenseAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 4, 0);
        panAccountHead.add(panSuspenceDetails, gridBagConstraints);

        lblSuspenseProdID.setText("Suspense Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSuspenseProdID, gridBagConstraints);

        cboSuspenseProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSuspenseProdID.setPopupWidth(130);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(cboSuspenseProdID, gridBagConstraints);

        lblSuspenseAccNo.setText("Suspense A/c Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSuspenseAccNo, gridBagConstraints);

        txtSuspenseAccNo.setEnabled(false);
        txtSuspenseAccNo.setMinimumSize(new java.awt.Dimension(125, 21));
        txtSuspenseAccNo.setPreferredSize(new java.awt.Dimension(125, 21));
        txtSuspenseAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuspenseAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtSuspenseAccNo, gridBagConstraints);

        btnSuspenseAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseAccNo.setEnabled(false);
        btnSuspenseAccNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSuspenseAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnSuspenseAccNo, gridBagConstraints);

        txtARCExpenseHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtARCExpenseHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtARCExpenseHead.setEnabled(false);
        txtARCExpenseHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtARCExpenseHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtARCExpenseHead, gridBagConstraints);

        lblARCExpenseHead.setText("ARC Expense A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblARCExpenseHead, gridBagConstraints);

        btnARCExpenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnARCExpenseHead.setEnabled(false);
        btnARCExpenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnARCExpenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnARCExpenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnARCExpenseHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnARCExpenseHead, gridBagConstraints);

        txtEACostHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtEACostHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtEACostHead.setEnabled(false);
        txtEACostHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEACostHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtEACostHead, gridBagConstraints);

        lblEACostHead.setText("EA Cost A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblEACostHead, gridBagConstraints);

        btnEACostHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEACostHead.setEnabled(false);
        btnEACostHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEACostHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEACostHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEACostHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnEACostHead, gridBagConstraints);

        txtEAExpenseHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtEAExpenseHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtEAExpenseHead.setEnabled(false);
        txtEAExpenseHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEAExpenseHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtEAExpenseHead, gridBagConstraints);

        lblEAExpenseHead.setText("EA Expense A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblEAExpenseHead, gridBagConstraints);

        btnEAExpenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEAExpenseHead.setEnabled(false);
        btnEAExpenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEAExpenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEAExpenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEAExpenseHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnEAExpenseHead, gridBagConstraints);

        txtEPCostHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtEPCostHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtEPCostHead.setEnabled(false);
        txtEPCostHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEPCostHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtEPCostHead, gridBagConstraints);

        lblEPCostHead.setText("EP Cost A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblEPCostHead, gridBagConstraints);

        btnEPCostHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEPCostHead.setEnabled(false);
        btnEPCostHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEPCostHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEPCostHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEPCostHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnEPCostHead, gridBagConstraints);

        txtEPExpenseHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtEPExpenseHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtEPExpenseHead.setEnabled(false);
        txtEPExpenseHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEPExpenseHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtEPExpenseHead, gridBagConstraints);

        lblEPExpenseHead.setText("EP Expense A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblEPExpenseHead, gridBagConstraints);

        btnEPExpenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEPExpenseHead.setEnabled(false);
        btnEPExpenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEPExpenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEPExpenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEPExpenseHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnEPExpenseHead, gridBagConstraints);

        txtARCCostHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtARCCostHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtARCCostHead.setEnabled(false);
        txtARCCostHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtARCCostHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtARCCostHead, gridBagConstraints);

        lblARCCostHead.setText("ARC Cost A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblARCCostHead, gridBagConstraints);

        btnARCCostHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnARCCostHead.setEnabled(false);
        btnARCCostHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnARCCostHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnARCCostHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnARCCostHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnARCCostHead, gridBagConstraints);

        lblStampAdvanceHead.setText("Stamp Advance A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblStampAdvanceHead, gridBagConstraints);

        txtStampAdvanceHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtStampAdvanceHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtStampAdvanceHead.setEnabled(false);
        txtStampAdvanceHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStampAdvanceHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtStampAdvanceHead, gridBagConstraints);

        btnStampAdvanceHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStampAdvanceHead.setEnabled(false);
        btnStampAdvanceHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnStampAdvanceHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStampAdvanceHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStampAdvanceHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnStampAdvanceHead, gridBagConstraints);

        lblPostageHead.setText("Postage A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPostageHead, gridBagConstraints);

        txtPostageHead.setEnabled(false);
        txtPostageHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtPostageHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtPostageHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtPostageHead, gridBagConstraints);

        btnPostageHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageHead.setEnabled(false);
        btnPostageHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostageHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnPostageHead, gridBagConstraints);

        lblChargeHead.setText("Charge Payment A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblChargeHead, gridBagConstraints);

        txtChargeHead.setEnabled(false);
        txtChargeHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtChargeHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtChargeHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargeHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtChargeHead, gridBagConstraints);

        btnChargeHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChargeHead.setEnabled(false);
        btnChargeHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChargeHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChargeHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnChargeHead, gridBagConstraints);

        txtPostAdvHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtPostAdvHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtPostAdvHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostAdvHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtPostAdvHead, gridBagConstraints);

        lblMdsPostAdv.setText("Postage Adv A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMdsPostAdv, gridBagConstraints);

        btnPostAdvHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostAdvHead.setEnabled(false);
        btnPostAdvHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostAdvHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostAdvHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostAdvHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnPostAdvHead, gridBagConstraints);

        IS_REV_POST_ADV.setText("Is RevPostAdv");
        IS_REV_POST_ADV.setMaximumSize(new java.awt.Dimension(147, 27));
        IS_REV_POST_ADV.setMinimumSize(new java.awt.Dimension(140, 20));
        IS_REV_POST_ADV.setPreferredSize(new java.awt.Dimension(140, 20));
        IS_REV_POST_ADV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IS_REV_POST_ADVActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        panAccountHead.add(IS_REV_POST_ADV, gridBagConstraints);

        txtForFeitedPaymentHead.setEnabled(false);
        txtForFeitedPaymentHead.setMinimumSize(new java.awt.Dimension(125, 21));
        txtForFeitedPaymentHead.setPreferredSize(new java.awt.Dimension(125, 21));
        txtForFeitedPaymentHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtForFeitedPaymentHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccountHead.add(txtForFeitedPaymentHead, gridBagConstraints);

        lblForFeitedHead.setText("Forfeited A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblForFeitedHead, gridBagConstraints);

        btnForFietedPaymentHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnForFietedPaymentHead.setEnabled(false);
        btnForFietedPaymentHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnForFietedPaymentHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnForFietedPaymentHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForFietedPaymentHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountHead.add(btnForFietedPaymentHead, gridBagConstraints);

        btnCreateHead.setText("Create A/c Heads");
        btnCreateHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panAccountHead.add(btnCreateHead, gridBagConstraints);

        lblLegalChrgHead.setText("Legal Charge A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.insets = new java.awt.Insets(0, 104, 0, 4);
        panAccountHead.add(lblLegalChrgHead, gridBagConstraints);

        txtLegalChrgHead.setEnabled(false);
        txtLegalChrgHead.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 17;
        panAccountHead.add(txtLegalChrgHead, gridBagConstraints);

        btnLegalChrgAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLegalChrgAcHead.setEnabled(false);
        btnLegalChrgAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLegalChrgAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLegalChrgAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLegalChrgAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 17;
        panAccountHead.add(btnLegalChrgAcHead, gridBagConstraints);

        cLabel2.setText("Part Pay Bonus Recovery Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panAccountHead.add(cLabel2, gridBagConstraints);

        txtPartPayBonusRecoveryHead.setEnabled(false);
        txtPartPayBonusRecoveryHead.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        panAccountHead.add(txtPartPayBonusRecoveryHead, gridBagConstraints);

        btnPartPayBonusRecoveryHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPartPayBonusRecoveryHead.setEnabled(false);
        btnPartPayBonusRecoveryHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPartPayBonusRecoveryHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPartPayBonusRecoveryHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartPayBonusRecoveryHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 18;
        panAccountHead.add(btnPartPayBonusRecoveryHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHeadDetails.add(panAccountHead, gridBagConstraints);

        tabMdsType.addTab("Account Head", panAccountHeadDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabMdsType, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPostageHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageHeadFocusLost
        // TODO add your handling code here:
        if (!txtPostageHead.getText().equals("")) {
            btnPostageHead.setToolTipText(getAccHdDesc(txtPostageHead.getText()));
        }
    }//GEN-LAST:event_txtPostageHeadFocusLost

    private void txtEPExpenseHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEPExpenseHeadFocusLost
        // TODO add your handling code here:
        if (!txtEPExpenseHead.getText().equals("")) {
            btnEPExpenseHead.setToolTipText(getAccHdDesc(txtEPExpenseHead.getText()));
        }
    }//GEN-LAST:event_txtEPExpenseHeadFocusLost

    private void txtEPCostHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEPCostHeadFocusLost
        // TODO add your handling code here:
        if (!txtEPCostHead.getText().equals("")) {
            btnEPCostHead.setToolTipText(getAccHdDesc(txtEPCostHead.getText()));
        }
    }//GEN-LAST:event_txtEPCostHeadFocusLost

    private void txtEAExpenseHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEAExpenseHeadFocusLost
        // TODO add your handling code here:
        if (!txtEAExpenseHead.getText().equals("")) {
            btnEAExpenseHead.setToolTipText(getAccHdDesc(txtEAExpenseHead.getText()));
        }
    }//GEN-LAST:event_txtEAExpenseHeadFocusLost

    private void txtEACostHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEACostHeadFocusLost
        // TODO add your handling code here:
        if (!txtEACostHead.getText().equals("")) {
            btnEACostHead.setToolTipText(getAccHdDesc(txtEACostHead.getText()));
        }
    }//GEN-LAST:event_txtEACostHeadFocusLost

    private void txtARCExpenseHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtARCExpenseHeadFocusLost
        // TODO add your handling code here:
        if (!txtARCExpenseHead.getText().equals("")) {
            btnARCExpenseHead.setToolTipText(getAccHdDesc(txtARCExpenseHead.getText()));
        }
    }//GEN-LAST:event_txtARCExpenseHeadFocusLost

    private void txtARCCostHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtARCCostHeadFocusLost
        // TODO add your handling code here:
        if (!txtARCCostHead.getText().equals("")) {
            btnARCCostHead.setToolTipText(getAccHdDesc(txtARCCostHead.getText()));
        }
    }//GEN-LAST:event_txtARCCostHeadFocusLost

    private void txtCaseExpensesHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCaseExpensesHeadFocusLost
        // TODO add your handling code here:
        if (!txtCaseExpensesHead.getText().equals("")) {
            btnCaseExpensesHead.setToolTipText(getAccHdDesc(txtCaseExpensesHead.getText()));
        }
    }//GEN-LAST:event_txtCaseExpensesHeadFocusLost

    private void txtMDSReceivableHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMDSReceivableHeadFocusLost
        // TODO add your handling code here:
        if (!txtMDSReceivableHead.getText().equals("")) {
            btnMDSReceivableHead.setToolTipText(getAccHdDesc(txtMDSReceivableHead.getText()));
        }
    }//GEN-LAST:event_txtMDSReceivableHeadFocusLost

    private void txtMDSPayableHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMDSPayableHeadFocusLost
        // TODO add your handling code here:
        if (!txtMDSPayableHead.getText().equals("")) {
            btnMDSPayableHead.setToolTipText(getAccHdDesc(txtMDSPayableHead.getText()));
        }
    }//GEN-LAST:event_txtMDSPayableHeadFocusLost

    private void txtStampAdvanceHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStampAdvanceHeadFocusLost
        // TODO add your handling code here:
        if (!txtStampAdvanceHead.getText().equals("")) {
            btnStampAdvanceHead.setToolTipText(getAccHdDesc(txtStampAdvanceHead.getText()));
        }
    }//GEN-LAST:event_txtStampAdvanceHeadFocusLost

    private void txtNoticeChargesHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeChargesHeadFocusLost
        // TODO add your handling code here:
        if (!txtNoticeChargesHead.getText().equals("")) {
            btnNoticeChargesHead.setToolTipText(getAccHdDesc(txtNoticeChargesHead.getText()));
        }
    }//GEN-LAST:event_txtNoticeChargesHeadFocusLost

    private void txtBankingHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankingHeadFocusLost
        // TODO add your handling code here:
        if (!txtBankingHead.getText().equals("")) {
            btnBankingHead.setToolTipText(getAccHdDesc(txtBankingHead.getText()));
        }
    }//GEN-LAST:event_txtBankingHeadFocusLost

    private void txtMunnalBonusHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMunnalBonusHeadFocusLost
        // TODO add your handling code here:
        if (!txtMunnalBonusHead.getText().equals("")) {
            btnMunnalBonusHead.setToolTipText(getAccHdDesc(txtMunnalBonusHead.getText()));
        }
    }//GEN-LAST:event_txtMunnalBonusHeadFocusLost

    private void txtSuspenseAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuspenseAccNoFocusLost
        // TODO add your handling code here:
        if (!txtSuspenseAccNo.getText().equals("")) {
            btnSuspenseAccNo.setToolTipText(getAccHdDesc(txtSuspenseAccNo.getText()));
        }
    }//GEN-LAST:event_txtSuspenseAccNoFocusLost

    private void txtSundryPaymentHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSundryPaymentHeadFocusLost
        // TODO add your handling code here:
        if (!txtSundryPaymentHead.getText().equals("")) {
            btnSundryPaymentHead.setToolTipText(getAccHdDesc(txtSundryPaymentHead.getText()));
        }
    }//GEN-LAST:event_txtSundryPaymentHeadFocusLost

    private void txtSundryReceiptHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSundryReceiptHeadFocusLost
        // TODO add your handling code here:
        if (!txtSundryReceiptHead.getText().equals("")) {
            btnSundryReceiptHead.setToolTipText(getAccHdDesc(txtSundryReceiptHead.getText()));
        }
    }//GEN-LAST:event_txtSundryReceiptHeadFocusLost

    private void txtThalayalBonusHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtThalayalBonusHeadFocusLost
        // TODO add your handling code here:
        if (!txtThalayalBonusHead.getText().equals("")) {
            btnThalayalBonusHead.setToolTipText(getAccHdDesc(txtThalayalBonusHead.getText()));
        }
    }//GEN-LAST:event_txtThalayalBonusHeadFocusLost

    private void txtPenalHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalHeadFocusLost
        // TODO add your handling code here:
        if (!txtPenalHead.getText().equals("")) {
            btnPenalHead.setToolTipText(getAccHdDesc(txtPenalHead.getText()));
        }
    }//GEN-LAST:event_txtPenalHeadFocusLost

    private void txtDiscountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountHeadFocusLost
        // TODO add your handling code here:
        if (!txtDiscountHead.getText().equals("")) {
            btnDiscountHead.setToolTipText(getAccHdDesc(txtDiscountHead.getText()));
        }
    }//GEN-LAST:event_txtDiscountHeadFocusLost

    private void txtBonusReceivableHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusReceivableHeadFocusLost
        // TODO add your handling code here:
        if (!txtBonusReceivableHead.getText().equals("")) {
            btnBonusReceivableHead.setToolTipText(getAccHdDesc(txtBonusReceivableHead.getText()));
        }
    }//GEN-LAST:event_txtBonusReceivableHeadFocusLost

    private void txtBonusPayableHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusPayableHeadFocusLost
        // TODO add your handling code here:
        if (!txtBonusPayableHead.getText().equals("")) {
            btnBonusPayableHead.setToolTipText(getAccHdDesc(txtBonusPayableHead.getText()));
        }
    }//GEN-LAST:event_txtBonusPayableHeadFocusLost

    private void txtCommisionHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommisionHeadFocusLost
        // TODO add your handling code here:
        if (!txtCommisionHead.getText().equals("")) {
            btnCommisionHead.setToolTipText(getAccHdDesc(txtCommisionHead.getText()));
        }
    }//GEN-LAST:event_txtCommisionHeadFocusLost

    private void txtMiscellaneousHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscellaneousHeadFocusLost
        // TODO add your handling code here:
        if (!txtMiscellaneousHead.getText().equals("")) {
            btnMiscellaneousHead.setToolTipText(getAccHdDesc(txtMiscellaneousHead.getText()));
        }
    }//GEN-LAST:event_txtMiscellaneousHeadFocusLost

    private void btnPostageHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageHeadActionPerformed
        // TODO add your handling code here:
        popUp("POSTAGE_HEAD");
    }//GEN-LAST:event_btnPostageHeadActionPerformed

    private void btnEPExpenseHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEPExpenseHeadActionPerformed
        // TODO add your handling code here:
        popUp("EP_EXPENSE_HEAD");
    }//GEN-LAST:event_btnEPExpenseHeadActionPerformed

    private void btnEPCostHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEPCostHeadActionPerformed
        // TODO add your handling code here:
        popUp("EP_COST_HEAD");
    }//GEN-LAST:event_btnEPCostHeadActionPerformed

    private void btnEAExpenseHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEAExpenseHeadActionPerformed
        // TODO add your handling code here:
        popUp("EA_EXPENSE_HEAD");
    }//GEN-LAST:event_btnEAExpenseHeadActionPerformed

    private void btnEACostHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEACostHeadActionPerformed
        // TODO add your handling code here:
        popUp("EA_COST_HEAD");
    }//GEN-LAST:event_btnEACostHeadActionPerformed

    private void btnARCExpenseHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnARCExpenseHeadActionPerformed
        // TODO add your handling code here:
        popUp("ARC_EXPENSE_HEAD");
    }//GEN-LAST:event_btnARCExpenseHeadActionPerformed

    private void btnARCCostHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnARCCostHeadActionPerformed
        // TODO add your handling code here:
        popUp("ARC_COST_HEAD");
    }//GEN-LAST:event_btnARCCostHeadActionPerformed

    private void btnStampAdvanceHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStampAdvanceHeadActionPerformed
        // TODO add your handling code here:
        popUp("STAMP_HEAD");
    }//GEN-LAST:event_btnStampAdvanceHeadActionPerformed

    private void btnSuspenseAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseAccNoActionPerformed
        // TODO add your handling code here:
        popUp("SUSPENSE_ACC_NO");
    }//GEN-LAST:event_btnSuspenseAccNoActionPerformed

    private void rdoSuspenseAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSuspenseAccNoActionPerformed
        // TODO add your handling code here:
        suspenseACCNoEnableDisable(true);
        suspenseGLenableDisable(false);
        txtSuspenseHead.setText("");
        txtSuspenseAccNo.setEnabled(false);
    }//GEN-LAST:event_rdoSuspenseAccNoActionPerformed

    private void rdoSuspenseGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSuspenseGLActionPerformed
        // TODO add your handling code here:
        suspenseGLenableDisable(true);
        suspenseACCNoEnableDisable(false);
        cboSuspenseProdID.setSelectedItem("");
        txtSuspenseAccNo.setText("");
    }//GEN-LAST:event_rdoSuspenseGLActionPerformed
    private void suspenseGLenableDisable(boolean flag) {
        lblSuspenseHead.setVisible(flag);
        txtSuspenseHead.setVisible(flag);
        btnSuspenseHead.setVisible(flag);
    }

    private void suspenseACCNoEnableDisable(boolean flag) {
        lblSuspenseProdID.setVisible(flag);
        cboSuspenseProdID.setVisible(flag);
        lblSuspenseAccNo.setVisible(flag);
        txtSuspenseAccNo.setVisible(flag);
        btnSuspenseAccNo.setVisible(flag);
    }
    private void cboInstFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstFreqActionPerformed
        // TODO add your handling code here:
        if (cboInstFreq.getSelectedIndex() > 0) {
            if (cboInstFreq.getSelectedItem().equals("Weekly")) {
                weeklySetVisible(false);
                cboInstallmentDay.setSelectedItem("");
                cboAuctionDay.setSelectedItem("");
            } else {
                weeklySetVisible(true);
            }
        }
    }//GEN-LAST:event_cboInstFreqActionPerformed
    private void weeklySetVisible(boolean flag) {
        lblInstallmentDay.setVisible(flag);
        lblAuctionDay.setVisible(flag);
        cboInstallmentDay.setVisible(flag);
        cboAuctionDay.setVisible(flag);
    }
    private void cboInstFreqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstFreqFocusLost
        // TODO add your handling code here:
        if (cboInstFreq.getSelectedIndex() > 0 && tdtSchemeStDt.getDateValue().length() > 0 && txtNoofMemberPer.getText().length() > 0) {
            tdtSchemeStDtFocusLost(null);
        }
    }//GEN-LAST:event_cboInstFreqFocusLost

    private void txtPostageChargeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageChargeAmtFocusLost
        // TODO add your handling code here:
        if (txtPostageChargeAmt.getText().length() == 0) {
            txtPostageChargeAmt.setText("0");
        }
        if (txtNoticeChargeAmt.getText().length() == 0) {
            txtNoticeChargeAmt.setText("0");
        }
    }//GEN-LAST:event_txtPostageChargeAmtFocusLost

    private void txtNoticeChargeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeChargeAmtFocusLost
        // TODO add your handling code here:
        if (txtPostageChargeAmt.getText().length() == 0) {
            txtPostageChargeAmt.setText("0");
        }
        if (txtNoticeChargeAmt.getText().length() == 0) {
            txtNoticeChargeAmt.setText("0");
        }
    }//GEN-LAST:event_txtNoticeChargeAmtFocusLost

    private void btnNotice_Charge_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_DeleteActionPerformed
        // TODO add your handling code here:
        if (tblNoticeCharges.getSelectedRow() >= 0) {
            int index = tblNoticeCharges.getSelectedRow();
            observable.deleteNoticeCharge(index);
            observable.resetNoticeChargeValues();
            ClientUtil.enableDisable(panNoticecharge_Amt, false);
            cboNoticeType.setSelectedItem("");
            txtNoticeChargeAmt.setText("");
            txtPostageChargeAmt.setText("");
        }
    }//GEN-LAST:event_btnNotice_Charge_DeleteActionPerformed

    private void tblNoticeChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNoticeChargesMousePressed
        // TODO add your handling code here:
        tableNoticeChargeClicked = true;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            ClientUtil.enableDisable(panNoticecharge_Amt, true);
            enableDisableNoticeCharge_SaveDelete(true);
        } else {
            ClientUtil.enableDisable(panNoticecharge_Amt, false);
            enableDisableNoticeCharge_SaveDelete(false);
        }
        observable.populateNoticeCharge(tblNoticeCharges.getSelectedRow());
        updateNoticeCharge();
    }//GEN-LAST:event_tblNoticeChargesMousePressed

    private void btnNotice_Charge_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_SaveActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panNoticeCharges, false);
        String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
        if (tblNoticeCharges.getRowCount() > 0) {
            for (int i = 0; i < tblNoticeCharges.getRowCount(); i++) {
                String notType = CommonUtil.convertObjToStr(tblNoticeCharges.getValueAt(i, 0));
                if (noticeType.equalsIgnoreCase(notType) && !tableNoticeChargeClicked) {
                    ClientUtil.displayAlert("Notice Type Already Exists in this Table");
                    observable.resetNoticeChargeValues();
                    updateNoticeCharge();
                    tableNoticeChargeClicked = false;
                    enableDisableNoticeCharge_SaveDelete(false);
                    btnNotice_Charge_New.setEnabled(true);
                    return;
                }
            }
        }
        ClientUtil.enableDisable(panNoticeCharges, false);
        updateNoticeChargeOB();
        observable.saveNoticeCharge(tableNoticeChargeClicked, tblNoticeCharges.getSelectedRow());
        observable.resetNoticeChargeValues();
        updateNoticeCharge();
        tableNoticeChargeClicked = false;
        enableDisableNoticeCharge_SaveDelete(false);
        btnNotice_Charge_New.setEnabled(true);
    }//GEN-LAST:event_btnNotice_Charge_SaveActionPerformed
    private void updateNoticeCharge() {
        cboNoticeType.setSelectedItem(observable.getCboNoticeType());
        txtNoticeChargeAmt.setText((String) observable.getTxtNoticeChargeAmt());
        txtPostageChargeAmt.setText((String) observable.getTxtPostageChargeAmt());
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());
    }

    private void updateNoticeChargeOB() {
        observable.setCboNoticeType((String) cboNoticeType.getSelectedItem());
        observable.setTxtNoticeChargeAmt(txtNoticeChargeAmt.getText());
        observable.setTxtPostageChargeAmt(txtPostageChargeAmt.getText());
    }
    private void btnNotice_Charge_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_NewActionPerformed
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0) {
            ClientUtil.enableDisable(panNoticeCharges, true);
            enableDisableNoticeCharge_SaveDelete(false);
            btnNotice_Charge_Save.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("MDS Scheme Name Should Not be Empty !!!");
        }
    }//GEN-LAST:event_btnNotice_Charge_NewActionPerformed
    private void enableDisableNoticeCharge_SaveDelete(boolean flag) {
        btnNotice_Charge_Delete.setEnabled(flag);
        btnNotice_Charge_Save.setEnabled(flag);
        btnNotice_Charge_New.setEnabled(flag);
    }
    private void txtCoChittalInstAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCoChittalInstAmountFocusLost
        // TODO add your handling code here:
        if (txtCoChittalInstAmount.getText().length() > 0) {
            setTotalAmtUnderScheme();
        }
    }//GEN-LAST:event_txtCoChittalInstAmountFocusLost

    private void txtNoofCoChittalsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofCoChittalsFocusLost
        // TODO add your handling code here:
        if (txtNoofCoChittals.getText().length() > 0) {
            setMaximumMember();
        }
    }//GEN-LAST:event_txtNoofCoChittalsFocusLost

    private void txtNoofCoInstallmentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofCoInstallmentsFocusLost
        // TODO add your handling code here:
        if (txtNoofCoInstallments.getText().length() > 0) {
            setMaximumMember();
            setTotalAmtUnderScheme();
        }
    }//GEN-LAST:event_txtNoofCoInstallmentsFocusLost
    private void setMaximumMember() {
        txtMaxNoofMemberCoChittals.setText(String.valueOf(CommonUtil.convertObjToInt(txtNoofCoChittals.getText())
                * CommonUtil.convertObjToInt(txtNoofCoInstallments.getText())));
        txtMaxNoofMemberCoChittals.setEnabled(false);
    }

    private void setTotalAmtUnderScheme() {
        txtTotAmtUnderScheme.setText(String.valueOf(CommonUtil.convertObjToInt(txtNoofCoInstallments.getText())
                * CommonUtil.convertObjToInt(txtCoChittalInstAmount.getText())));
        txtTotAmtUnderScheme.setEnabled(false);
    }
    private void rdoMultipleMembers_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultipleMembers_yesActionPerformed
        // TODO add your handling code here:
        if (rdoMultipleMembers_yes.isSelected() == true) {
            panDivisionDetails.setVisible(false);
            panCoChittalDetails.setVisible(true);
            txtNoofDivision.setText("0");
            txtNoofAuctions.setText("0");
            txtNoofDraws.setText("0");
            txtNoofMemberPer.setText("0");
            txtNoofMemberScheme.setText("0");
            txtInstAmt.setText("0");
            txtNoofInst.setText("0");
            txtTotAmtPerDivision.setText("0");
            txtTotAmtUnderScheme.setText("0");
        }
    }//GEN-LAST:event_rdoMultipleMembers_yesActionPerformed

    private void rdoMultipleMembers_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultipleMembers_noActionPerformed
        // TODO add your handling code here:
        if (rdoMultipleMembers_no.isSelected() == true) {
            panDivisionDetails.setVisible(true);
            panCoChittalDetails.setVisible(false);
            txtNoofCoChittals.setText("0");
            txtNoofCoInstallments.setText("0");
            txtMaxNoofMemberCoChittals.setText("0");
            txtCoChittalInstAmount.setText("0");
            txtTotAmtUnderScheme.setText("0");
        }
    }//GEN-LAST:event_rdoMultipleMembers_noActionPerformed

    private void rdoMunnalAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMunnalAllowed_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMunnalAllowed_noActionPerformed

    private void rdoThalayalAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoThalayalAllowed_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoThalayalAllowed_noActionPerformed

    private void btnSundryPaymentHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSundryPaymentHeadActionPerformed
        // TODO add your handling code here:
        popUp("SUNDRY_PAYMENT_HEAD");
    }//GEN-LAST:event_btnSundryPaymentHeadActionPerformed

    private void btnSundryReceiptHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSundryReceiptHeadActionPerformed
        // TODO add your handling code here:
        popUp("SUNDRY_RECEIPT_HEAD");
    }//GEN-LAST:event_btnSundryReceiptHeadActionPerformed

    private void btnMDSReceivableHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSReceivableHeadActionPerformed
        // TODO add your handling code here:
        popUp("MDS_RECEIVABLE_HEAD");
    }//GEN-LAST:event_btnMDSReceivableHeadActionPerformed

    private void btnMDSPayableHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMDSPayableHeadActionPerformed
        // TODO add your handling code here:
        popUp("MDS_PAYABLE_HEAD");
    }//GEN-LAST:event_btnMDSPayableHeadActionPerformed

    private void btnDiscountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscountHeadActionPerformed
        // TODO add your handling code here:
        popUp("DISCOUNT_HEAD");
    }//GEN-LAST:event_btnDiscountHeadActionPerformed

    private void chkApplicable4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkApplicable4ActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 2) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any one of the two records");
                chkApplicable2.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 3) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any two of the three records");
                chkApplicable3.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 4) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true && chkApplicable4.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any three of the four records");
                chkApplicable4.setSelected(false);
            }
        }
    }//GEN-LAST:event_chkApplicable4ActionPerformed

    private void chkApplicable3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkApplicable3ActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 2) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any one of the two records");
                chkApplicable2.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 3) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any two of the three records");
                chkApplicable3.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 4) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true && chkApplicable4.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any three of the four records");
                chkApplicable4.setSelected(false);
            }
        }
    }//GEN-LAST:event_chkApplicable3ActionPerformed

    private void chkApplicable2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkApplicable2ActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 2) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any one of the two records");
                chkApplicable2.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 3) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any two of the three records");
                chkApplicable3.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 4) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true && chkApplicable4.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any three of the four records");
                chkApplicable4.setSelected(false);
            }
        }
    }//GEN-LAST:event_chkApplicable2ActionPerformed

    private void chkApplicable1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkApplicable1ActionPerformed
        // TODO add your handling code here:
//        if(CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 1 ){
//            chkApplicable1.setEnabled(true);
//            chkApplicable2.setEnabled(false);
//            chkApplicable3.setEnabled(false);
//            chkApplicable4.setEnabled(false);
//        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 2) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any one of the two records");
                chkApplicable2.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 3) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any two of the three records");
                chkApplicable2.setSelected(false);
            }
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 4) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
            if (chkApplicable1.isSelected() == true && chkApplicable2.isSelected() == true && chkApplicable3.isSelected() == true && chkApplicable4.isSelected() == true) {
                ClientUtil.showAlertWindow("Select any three of the four records");
                chkApplicable2.setSelected(false);
            }
        }
    }//GEN-LAST:event_chkApplicable1ActionPerformed

    private void txtSuspenseHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuspenseHeadFocusLost
        // TODO add your handling code here:
        if (!(txtSuspenseHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtSuspenseHead, "Cash.getSelectAcctHead");
            btnSuspenseHead.setToolTipText(getAccHdDesc(txtSuspenseHead.getText()));
        }
    }//GEN-LAST:event_txtSuspenseHeadFocusLost

    private void txtMunnalReceiptsHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMunnalReceiptsHeadFocusLost
        // TODO add your handling code here:
        if (!(txtMunnalReceiptsHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtMunnalReceiptsHead, "Cash.getSelectAcctHead");
            btnMunnalReceiptsHead.setToolTipText(getAccHdDesc(txtMunnalReceiptsHead.getText()));
        }
    }//GEN-LAST:event_txtMunnalReceiptsHeadFocusLost

    private void txtThalayalReceiptsHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtThalayalReceiptsHeadFocusLost
        // TODO add your handling code here:
        if (!(txtThalayalReceiptsHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtThalayalReceiptsHead, "Cash.getSelectAcctHead");
            btnThalayalReceiptsHead.setToolTipText(getAccHdDesc(txtThalayalReceiptsHead.getText()));
        }
    }//GEN-LAST:event_txtThalayalReceiptsHeadFocusLost

    private void txtPaymentHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPaymentHeadFocusLost
        // TODO add your handling code here:
        if (!(txtPaymentHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtPaymentHead, "Cash.getSelectAcctHead");
            btnPaymentHead.setToolTipText(getAccHdDesc(txtPaymentHead.getText()));
        }
    }//GEN-LAST:event_txtPaymentHeadFocusLost

    private void txtReceiptHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceiptHeadFocusLost
        // TODO add your handling code here:
        if (!(txtReceiptHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtReceiptHead, "Cash.getSelectAcctHead");
            btnReceiptHead.setToolTipText(getAccHdDesc(txtReceiptHead.getText()));
        }
    }//GEN-LAST:event_txtReceiptHeadFocusLost

    private void btnCaseExpensesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCaseExpensesHeadActionPerformed
        // TODO add your handling code here:
        popUp("CASE_EXPENSE_HEAD");
    }//GEN-LAST:event_btnCaseExpensesHeadActionPerformed

    private void btnNoticeChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoticeChargesHeadActionPerformed
        // TODO add your handling code here:
        popUp("NOTICE_HEAD");
    }//GEN-LAST:event_btnNoticeChargesHeadActionPerformed

    private void btnBankingHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankingHeadActionPerformed
        // TODO add your handling code here:
        popUp("BANKING_HEAD");
    }//GEN-LAST:event_btnBankingHeadActionPerformed

    private void btnMunnalReceiptsHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMunnalReceiptsHeadActionPerformed
        // TODO add your handling code here:
        popUp("MUNNAL_RECEIPT_HEAD");
    }//GEN-LAST:event_btnMunnalReceiptsHeadActionPerformed

    private void btnMunnalBonusHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMunnalBonusHeadActionPerformed
        // TODO add your handling code here:
        popUp("MUNNAL_BONUS_HEAD");
    }//GEN-LAST:event_btnMunnalBonusHeadActionPerformed

    private void btnThalayalBonusHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThalayalBonusHeadActionPerformed
        // TODO add your handling code here:
        popUp("THALAYAL_BONUS_HEAD");
    }//GEN-LAST:event_btnThalayalBonusHeadActionPerformed

    private void btnThalayalReceiptsHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThalayalReceiptsHeadActionPerformed
        // TODO add your handling code here:
        popUp("THALAYAL_RECEIPT_HEAD");
    }//GEN-LAST:event_btnThalayalReceiptsHeadActionPerformed

    private void btnPenalHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalHeadActionPerformed
        // TODO add your handling code here:
        popUp("PENAL_HEAD");
    }//GEN-LAST:event_btnPenalHeadActionPerformed

    private void btnBonusReceivableHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBonusReceivableHeadActionPerformed
        // TODO add your handling code here:
        popUp("BONUS_RECEIVABLE_HEAD");
    }//GEN-LAST:event_btnBonusReceivableHeadActionPerformed

    private void btnBonusPayableHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBonusPayableHeadActionPerformed
        // TODO add your handling code here:
        popUp("BONUS_PAYABLE_HEAD");
    }//GEN-LAST:event_btnBonusPayableHeadActionPerformed

    private void btnCommisionHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommisionHeadActionPerformed
        // TODO add your handling code here:
        popUp("COMMISION_HEAD");
    }//GEN-LAST:event_btnCommisionHeadActionPerformed

    private void btnMiscellaneousHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiscellaneousHeadActionPerformed
        // TODO add your handling code here:
        popUp("MISCELLANEOUS_HEAD");
    }//GEN-LAST:event_btnMiscellaneousHeadActionPerformed

    private void btnSuspenseHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseHeadActionPerformed
        // TODO add your handling code here:
        popUp("SUSPENSE_HEAD");
    }//GEN-LAST:event_btnSuspenseHeadActionPerformed

    private void btnPaymentHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentHeadActionPerformed
        // TODO add your handling code here:
        popUp("PAYMENT_HEAD");
    }//GEN-LAST:event_btnPaymentHeadActionPerformed

    private void btnReceiptHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiptHeadActionPerformed
        // TODO add your handling code here:
        popUp("RECEIPT_HEAD");
    }//GEN-LAST:event_btnReceiptHeadActionPerformed

    private void txtBonusFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusFocusLost
        // TODO add your handling code here:
        txtPaymentAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtBonus.getText()).doubleValue()));
    }//GEN-LAST:event_txtBonusFocusLost

    private void txtInstAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstAmtFocusLost
        // TODO add your handling code here:
        int countMembers = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
        int countTotMemScheme = CommonUtil.convertObjToInt(txtNoofMemberScheme.getText());
        int instAmount = CommonUtil.convertObjToInt(txtInstAmt.getText());
        txtTotAmtPerDivision.setText(String.valueOf(countMembers * instAmount));
        txtTotAmtUnderScheme.setText(String.valueOf(countTotMemScheme * instAmount));
        txtTotAmtPerDivision.setEnabled(false);
        txtTotAmtUnderScheme.setEnabled(false);
        if (rdoPredefinitonInst_yes.isSelected() == true && tblSchedule.getRowCount() > 0) {
            String insAmount = CommonUtil.convertObjToStr(txtInstAmt.getText());
            int rowCount = tblSchedule.getRowCount();
            observable.setInstAmount(insAmount, rowCount);
            tblSchedule.setModel(observable.getTblScheduleDetails());
        }
    }//GEN-LAST:event_txtInstAmtFocusLost

    private void txtNoofDivisionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofDivisionFocusLost
        // TODO add your handling code here:
        if (txtNoofDivision.getText().length() > 0 && txtNoofMemberPer.getText().length() > 0) {
            txtNoofMemberScheme.setText(String.valueOf(CommonUtil.convertObjToInt(txtNoofMemberPer.getText()) * CommonUtil.convertObjToInt(txtNoofDivision.getText())));
        }
        if (txtInstAmt.getText().length() > 0) {
            int countMembers = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
            int countTotMemScheme = CommonUtil.convertObjToInt(txtNoofMemberScheme.getText());
            int instAmount = CommonUtil.convertObjToInt(txtInstAmt.getText());
            txtTotAmtPerDivision.setText(String.valueOf(countMembers * instAmount));
            txtTotAmtUnderScheme.setText(String.valueOf(countTotMemScheme * instAmount));
        }
    }//GEN-LAST:event_txtNoofDivisionFocusLost

    private void txtNoofMemberPerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofMemberPerFocusLost
        // TODO add your handling code here:
        if(isASpecialScheme != null && !isASpecialScheme.equals("Y")){
        txtNoofMemberScheme.setText(String.valueOf(CommonUtil.convertObjToInt(txtNoofMemberPer.getText()) * CommonUtil.convertObjToInt(txtNoofDivision.getText())));
        }
        txtNoofInst.setText(txtNoofMemberPer.getText());
        if (txtInstAmt.getText().length() > 0) {
            int countMembers = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
            int countTotMemScheme = CommonUtil.convertObjToInt(txtNoofMemberScheme.getText());
            int instAmount = CommonUtil.convertObjToInt(txtInstAmt.getText());
            txtTotAmtPerDivision.setText(String.valueOf(countMembers * instAmount));
            txtTotAmtUnderScheme.setText(String.valueOf(countTotMemScheme * instAmount));
        }
        if (tdtSchemeStDt.getDateValue().length() > 0 && txtNoofMemberPer.getText().length() > 0) {
            tdtSchemeStDtFocusLost(null);
            //            Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
            //            int noOfMember = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
            //            GregorianCalendar cal = new GregorianCalendar((stDt.getYear()+1900),stDt.getMonth(),stDt.getDate());
            //            if(noOfMember>0){
            //                cal.add(GregorianCalendar.MONTH, noOfMember-1);
            //            }else{
            //                cal.add(GregorianCalendar.MONTH, 0);
            //            }
            //            tdtSchemeEndDt.setDateValue(DateUtil.getStringDate(cal.getTime()));
        }
    }//GEN-LAST:event_txtNoofMemberPerFocusLost

    private void txtNoofDrawsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofDrawsFocusLost
        // TODO add your handling code here:
        if (txtNoofDraws.getText().length() > 0 && txtNoofAuctions.getText().length() > 0 && txtNoofDivision.getText().length() > 0) {
            int auctions = CommonUtil.convertObjToInt(txtNoofAuctions.getText());
            int draws = CommonUtil.convertObjToInt(txtNoofDraws.getText());
            int totalDiv = CommonUtil.convertObjToInt(txtNoofDivision.getText());
            int total = auctions + draws;
            if (totalDiv != total) {
                ClientUtil.showMessageWindow("Total No Of Auctions & Draws Should Be Equal to No Of Division!!!");
                txtNoofAuctions.setText("");
                txtNoofDraws.setText("");
            }
        }
        if (txtNoofDraws.getText().length() > 0 && txtNoofDivision.getText().length() > 0) {
            int auctions = CommonUtil.convertObjToInt(txtNoofAuctions.getText());
            int draws = CommonUtil.convertObjToInt(txtNoofDraws.getText());
            int totalDiv = CommonUtil.convertObjToInt(txtNoofDivision.getText());
            if (totalDiv < draws) {
                ClientUtil.showMessageWindow("No Of Draws Should Not Be Greater Than  No Of Divisions!!!");
                txtNoofAuctions.setText("");
                txtNoofDraws.setText("");
            }
        }
    }//GEN-LAST:event_txtNoofDrawsFocusLost

    private void txtNoofAuctionsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoofAuctionsFocusLost
        // TODO add your handling code here:
        if (txtNoofDraws.getText().length() > 0 && txtNoofAuctions.getText().length() > 0 && txtNoofDivision.getText().length() > 0) {
            int auctions = CommonUtil.convertObjToInt(txtNoofAuctions.getText());
            int draws = CommonUtil.convertObjToInt(txtNoofDraws.getText());
            int totalDiv = CommonUtil.convertObjToInt(txtNoofDivision.getText());
            int total = auctions + draws;
            if (totalDiv != total) {
                ClientUtil.showMessageWindow("Total No Of Auctions & Draws Should Be Equal to No Of Division !!!!");
                txtNoofAuctions.setText("");
                txtNoofDraws.setText("");
            }
        }
        if (txtNoofAuctions.getText().length() > 0 && txtNoofDivision.getText().length() > 0) {
            int auctions = CommonUtil.convertObjToInt(txtNoofAuctions.getText());
            int draws = CommonUtil.convertObjToInt(txtNoofDraws.getText());
            int totalDiv = CommonUtil.convertObjToInt(txtNoofDivision.getText());
            if (totalDiv < auctions) {
                ClientUtil.showMessageWindow("No Of Auctions Should Not Be Greater Than  No Of Divisions !!!!");
                txtNoofAuctions.setText("");
                txtNoofDraws.setText("");
            }
        }
    }//GEN-LAST:event_txtNoofAuctionsFocusLost

    private void rdoApplicableDivision_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoApplicableDivision_yesActionPerformed
        // TODO add your handling code here:
        lblApplicable1.setVisible(false);
        chkApplicable1.setVisible(false);
        lblApplicable2.setVisible(false);
        chkApplicable2.setVisible(false);
        lblApplicable3.setVisible(false);
        chkApplicable3.setVisible(false);
        lblApplicable4.setVisible(false);
        chkApplicable4.setVisible(false);
        chkApplicable1.setSelected(false);
        chkApplicable2.setSelected(false);
        chkApplicable3.setSelected(false);
        chkApplicable4.setSelected(false);
    }//GEN-LAST:event_rdoApplicableDivision_yesActionPerformed

    private void rdoApplicableDivision_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoApplicableDivision_noActionPerformed
        // TODO add your handling code here:
        lblApplicable1.setVisible(true);
        chkApplicable1.setVisible(true);
        lblApplicable2.setVisible(true);
        chkApplicable2.setVisible(true);
        lblApplicable3.setVisible(true);
        chkApplicable3.setVisible(true);
        lblApplicable4.setVisible(true);
        chkApplicable4.setVisible(true);
        chkApplicable1.setEnabled(true);
        chkApplicable2.setEnabled(true);
        chkApplicable3.setEnabled(true);
        chkApplicable4.setEnabled(true);
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 1) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(false);
            chkApplicable3.setEnabled(false);
            chkApplicable4.setEnabled(false);
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 2) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(false);
            chkApplicable4.setEnabled(false);
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 3) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(false);
        }
        if (CommonUtil.convertObjToDouble(txtNoofDivision.getText()).doubleValue() == 4) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
        }

    }//GEN-LAST:event_rdoApplicableDivision_noActionPerformed

    private void rdoPredefinitonInst_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPredefinitonInst_yesActionPerformed
        // TODO add your handling code here:
        lblApplicableDivision.setVisible(true);
        panApplicableDivision.setVisible(true);
        lblApplicable1.setVisible(true);
        chkApplicable1.setVisible(true);
        lblApplicable2.setVisible(true);
        chkApplicable2.setVisible(true);
        lblApplicable3.setVisible(true);
        chkApplicable3.setVisible(true);
        lblApplicable4.setVisible(true);
        chkApplicable4.setVisible(true);
        btnSchedNew.setEnabled(true);
        if (rdoApplicableDivision_yes.isSelected() == true) {
            chkApplicable1.setEnabled(false);
            chkApplicable2.setEnabled(false);
            chkApplicable3.setEnabled(false);
            chkApplicable4.setEnabled(false);
        }
        if (rdoApplicableDivision_no.isSelected() == true) {
            chkApplicable1.setEnabled(true);
            chkApplicable2.setEnabled(true);
            chkApplicable3.setEnabled(true);
            chkApplicable4.setEnabled(true);
        }
        btnSchedDelete.setEnabled(false);
        tdtInstallmentDt.setEnabled(false);
        txtAmount.setEnabled(false);
        txtPaymentAmount.setEnabled(false);

    }//GEN-LAST:event_rdoPredefinitonInst_yesActionPerformed

    private void rdoPredefinitonInst_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPredefinitonInst_noActionPerformed
        // TODO add your handling code here:
        if (tblSchedule.getRowCount() > 0) {
            ClientUtil.showMessageWindow("Installment Schedule Should Be Empty !!!!");
        }
        lblApplicableDivision.setVisible(false);
        panApplicableDivision.setVisible(false);
        lblApplicable1.setVisible(false);
        chkApplicable1.setVisible(false);
        lblApplicable2.setVisible(false);
        chkApplicable2.setVisible(false);
        lblApplicable3.setVisible(false);
        chkApplicable3.setVisible(false);
        lblApplicable4.setVisible(false);
        chkApplicable4.setVisible(false);
        btnSchedNew.setEnabled(false);
        chkApplicable1.setSelected(false);
        chkApplicable2.setSelected(false);
        chkApplicable3.setSelected(false);
        chkApplicable4.setSelected(false);
        btnSchedDelete.setEnabled(true);
    }//GEN-LAST:event_rdoPredefinitonInst_noActionPerformed

    private void tdtSchemeEndDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSchemeEndDtFocusLost
        // TODO add your handling code here:

        if (tdtSchemeEndDt.getDateValue().length() > 0) {
            Date endDt = DateUtil.getDateMMDDYYYY(tdtSchemeEndDt.getDateValue());
            Date startDt = DateUtil.getDateMMDDYYYY(tdtSchemeStDt.getDateValue());
            if (endDt.before(startDt)) {
                ClientUtil.showMessageWindow("Scheme End Date Should Be Greater Than Scheme Start Date");
                tdtSchemeEndDt.setDateValue("");
                return;
            }
        }
    }//GEN-LAST:event_tdtSchemeEndDtFocusLost

    
    private boolean isStartDateEditPosiible() {
        boolean editable = false;
        int chitCnt = 0;
        Date curDt = (Date) currDt.clone();
        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
       if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            List chitList = (List) ClientUtil.executeQuery("getSchemeCommencementStatus", whereMap);
            if (chitList != null && chitList.size() > 0) {
                whereMap = (HashMap) chitList.get(0);
                if (whereMap.containsKey("COMMENCEMENT_CNT") && whereMap.get("COMMENCEMENT_CNT") != null) {
                    chitCnt = CommonUtil.convertObjToInt(whereMap.get("COMMENCEMENT_CNT"));
                    if(chitCnt > 0){
                        editable = false;
                    }else{
                        editable = true;
                    }
                }
            }else{
                editable = true;
            }
        }else{
            editable = true;
       }
        return editable;
    }
    
    
    private void tdtSchemeStDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSchemeStDtFocusLost
        // TODO add your handling code here:
        if (cboInstFreq.getSelectedIndex() > 0 && tdtSchemeStDt.getDateValue().length() > 0) {
            Date curDt = (Date) currDt.clone();
            Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
            boolean editable =  isStartDateEditPosiible();
            if (DateUtil.dateDiff(stDt, curDt) > 0) {
                ClientUtil.showAlertWindow("Start date should be greater than or equal to current date");
                tdtSchemeStDt.setDateValue("");
            } else if(!editable){
                ClientUtil.showAlertWindow("Commencement already done. Cannot change date");
                tdtSchemeStDt.setDateValue(DateUtil.getStringDate(stDt));
            } else {
                int noOfMember = 0;
                String freq = CommonUtil.convertObjToStr(cboInstFreq.getSelectedItem());
                if (rdoMultipleMembers_yes.isSelected() == true) {
                    noOfMember = CommonUtil.convertObjToInt(txtNoofCoInstallments.getText());
                } else {
                    noOfMember = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
                }
                if (freq.equals("Yearly")) {
                    noOfMember = (noOfMember - 1) * 12;
                } else if (freq.equals("Half Yearly")) {
                    noOfMember = (noOfMember - 1) * 6;
                } else if (freq.equals("Quaterly")) {
                    noOfMember = (noOfMember - 1) * 3;
                } else if (freq.equals("Monthly")) {
                    noOfMember = noOfMember - 1;
                } else if (freq.equals("Weekly")) {
                    noOfMember = (noOfMember - 1) * 7;
                }
                GregorianCalendar cal = new GregorianCalendar((stDt.getYear() + 1900), stDt.getMonth(), stDt.getDate());
                if (freq.equals("Weekly")) {
                    if (noOfMember > 0) {
                        cal.add(GregorianCalendar.DATE, noOfMember);
                    } else {
                        cal.add(GregorianCalendar.DATE, 0);
                    }
                } else {
                    if (noOfMember > 0) {
                        cal.add(GregorianCalendar.MONTH, noOfMember);
                    } else {
                        cal.add(GregorianCalendar.MONTH, 0);
                    }
                }
                tdtSchemeEndDt.setDateValue(DateUtil.getStringDate(cal.getTime()));
                if (rdoPredefinitonInst_yes.isSelected() == true && tblSchedule.getRowCount() > 0) {
                    String StartDt = CommonUtil.convertObjToStr(stDt);
                    int rowCount = tblSchedule.getRowCount();
                    observable.setInstallmentDate(StartDt, rowCount);
                    tblSchedule.setModel(observable.getTblScheduleDetails());
                }
            }
        }
    }//GEN-LAST:event_tdtSchemeStDtFocusLost

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0) {
            txtChittalNumberPattern.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + txtSchemeName.getText());
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) { // Added by nithya on 15-02-2020 for KD-1434
                HashMap whereMap = new HashMap();
                whereMap.put("PROD_ID", txtSchemeName.getText());
                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
                if (lst != null && lst.size() > 0) {
                    HashMap existingProdIdMap = (HashMap) lst.get(0);
                    if (existingProdIdMap.containsKey("PROD_ID")) {
                        ClientUtil.showMessageWindow("Scheme Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change scheme Id first");
                        txtSchemeName.setText("");
                    }
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                txtChittalNumberPattern.setEnabled(true);
                // Added by nithya on 19-05-2016
//                HashMap whereMap = new HashMap();
//                whereMap.put("PROD_ID", txtSchemeName.getText());
//                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
//                System.out.println("getSBODBorrowerEligAmt : " + lst);
//                if (lst != null && lst.size() > 0) {
//                    HashMap existingProdIdMap = (HashMap) lst.get(0);
//                    if (existingProdIdMap.containsKey("PROD_ID")) {
//                        ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
//                        txtSchemeName.setText("");
//                    }
//                }
            } else {
                txtChittalNumberPattern.setEnabled(false);
            }
        } else {
            txtChittalNumberPattern.setText("");
            txtChittalNumberPattern.setEnabled(false);
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost

    private void txtSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSchemeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSchemeNameActionPerformed

    private void rdoPaymentDone_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPaymentDone_noActionPerformed
        // TODO add your handling code here:
        txtDay.setEnabled(true);
        txtInstallments.setText("");
        txtInstallments.setEnabled(false);
    }//GEN-LAST:event_rdoPaymentDone_noActionPerformed

    private void rdoPaymentDone_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPaymentDone_yesActionPerformed
        // TODO add your handling code here:
        txtInstallments.setEnabled(true);
        txtDay.setText("");
        txtDay.setEnabled(false);
    }//GEN-LAST:event_rdoPaymentDone_yesActionPerformed

    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
        if (cboProductId.getSelectedIndex() > 0) {
            HashMap hashMap = new HashMap();
            hashMap.put("PROD_ID", cboProductId.getSelectedItem());
            List lst = ClientUtil.executeQuery("getMDSProdDescription", hashMap);
            hashMap = new HashMap();
            hashMap = (HashMap) lst.get(0);
            String prod_Desc = (String) hashMap.get("PROD_DESC");
            String is_gds = (String) hashMap.get("IS_GDS");
            String prodId = (String) hashMap.get("PROD_ID");
            if(is_gds.equalsIgnoreCase("Y")){
                Boolean groupDeposit=observable.populateGroupDepositCombo(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
//                HashMap param = new HashMap();
//                param.put(CommonConstants.MAP_NAME,"getSelectGroupMDSDepositTO");
                if(groupDeposit){
                    cboGroupName.setModel(observable.getCbmGroupNo());
                }else{
                    cboGroupName.removeAllItems();
                }
                
            }
            lblProductDescVal.setText(prod_Desc);
        } else {
            lblProductDescVal.setText("");
        }
    }//GEN-LAST:event_cboProductIdActionPerformed

    private void btnSchedDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedDeleteActionPerformed
        // TODO add your handling code here:
        if (!updateShedule()) {
            ClientUtil.showAlertWindow("Chitty Auction Process already Started. Rescheduling not allowed !!!");
            btnSchedDelete.setEnabled(false);
            tdtInstallmentDt.setDateValue("");
            txtAmount.setText("");
            btnSchedDelete.setEnabled(false);
            tdtInstallmentDt.setEnabled(false);
            txtAmount.setEnabled(false);           
            txtBonus.setEnabled(false);
            txtPaymentAmount.setEnabled(false);
            txtPaymentAmount.setText("");
            txtAmount.setText("");
            txtBonus.setText("");
            return;
        } else {
        int s = CommonUtil.convertObjToInt(tblSchedule.getValueAt(tblSchedule.getSelectedRow(), 0));
        observable.deleteTableData(s, tblSchedule.getSelectedRow());
        observable.resetScheduleDetails();
        resetScheduleDetails();
        enableDisablePanScheduleDetails(false);
        enableDisableButton(false);
        btnSchedNew.setEnabled(true);
        }
    }//GEN-LAST:event_btnSchedDeleteActionPerformed

    private void tblScheduleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblScheduleMousePressed
        // TODO add your handling code here:
        updateScheduleOBFields();
        updateMode = true;
        updateTab = tblSchedule.getSelectedRow();
        observable.setNewData(false);
        String st = CommonUtil.convertObjToStr(tblSchedule.getValueAt(tblSchedule.getSelectedRow(), 0));
        observable.populateSchedileDetails(CommonUtil.convertObjToInt(st));
        populateScheduleDetails();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableButton(false);
            enableDisablePanScheduleDetails(false);
        } else {
            enableDisableButton(true);
            btnSchedNew.setEnabled(false);
            enableDisablePanScheduleDetails(true);
            if (rdoPredefinitonInst_yes.isSelected() == true) {
                btnSchedDelete.setEnabled(false);
                tdtInstallmentDt.setEnabled(false);
                txtAmount.setEnabled(false);
                txtPaymentAmount.setEnabled(false);
                int rowcnt = tblSchedule.getSelectedRow();
                if (CommonUtil.convertObjToInt(txtNoofMemberPer.getText()) < rowcnt + 1) {
                    btnSchedDelete.setEnabled(true);
                } else {
                    btnSchedDelete.setEnabled(false);
                }
            } else if (rdoPredefinitonInst_no.isSelected() == true) {
                btnSchedDelete.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblScheduleMousePressed
    public void populateScheduleDetails() {
        lblInstallmentNumber.setText(CommonUtil.convertObjToStr(observable.getTxtSlNo())); //AJITH
        tdtInstallmentDt.setDateValue(observable.getTdtInstallmentDt());
        txtAmount.setText(CommonUtil.convertObjToStr(observable.getTxtAmount())); //AJITH 
        txtBonus.setText(CommonUtil.convertObjToStr(observable.getTxtBonus())); //AJITH 
        txtPaymentAmount.setText(CommonUtil.convertObjToStr(observable.getTxtPaymentAmount())); //AJITH 
    }

    public void updateScheduleOBFields() {
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setTdtInstallmentDt(tdtInstallmentDt.getDateValue());
        observable.setTxtAmount(CommonUtil.convertObjToDouble(txtAmount.getText())); //AJITH
        observable.setTxtBonus(CommonUtil.convertObjToDouble(txtBonus.getText())); //AJITH
        observable.setTxtPaymentAmount(CommonUtil.convertObjToDouble(txtPaymentAmount.getText())); //AJITH
        observable.setTxtNoofDivision(CommonUtil.convertObjToInt(txtNoofDivision.getText()));  //AJITH
        observable.setTxtNoofMemberPer(CommonUtil.convertObjToInt(txtNoofMemberPer.getText()));  //AJITH
    }

    private void btnSchedSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedSaveActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        try {
            if (!updateShedule()) {
                ClientUtil.showAlertWindow("Chitty Auction Process already Started. Rescheduling not allowed !!!");
                btnSchedDelete.setEnabled(false);
                tdtInstallmentDt.setDateValue("");
                txtAmount.setText("");
                btnSchedDelete.setEnabled(false);
                tdtInstallmentDt.setEnabled(false);
                txtAmount.setEnabled(false);
                txtBonus.setEnabled(false);
                txtPaymentAmount.setEnabled(false);
                txtPaymentAmount.setText("");
                txtAmount.setText("");
                txtBonus.setText("");
                return;
            }else{
            if(txtBonus.getText()!=null && txtBonus.getText().length()>0){
                updateScheduleOBFields();
                observable.addToTable(updateTab, updateMode);
                tblSchedule.setModel(observable.getTblScheduleDetails());
                observable.resetScheduleDetails();
                resetScheduleDetails();
                enableDisablePanScheduleDetails(false);
                enableDisableButton(false);
                btnSchedNew.setEnabled(true);
            }else{
                ClientUtil.showAlertWindow("Bonus amount shouldn't be empty!!");
            }
          } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSchedSaveActionPerformed

     private boolean updateShedule(){
        boolean updateFlag = false;
        int chitCnt = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME",txtSchemeName.getText());
        List chitList = (List) ClientUtil.executeQuery("getPredefinedChittalcount", whereMap);
        if(chitList != null && chitList.size() > 0){
            whereMap = (HashMap)chitList.get(0);
            if(whereMap.containsKey("CHIT_CNT") && whereMap.get("CHIT_CNT") != null){
                chitCnt = CommonUtil.convertObjToInt(whereMap.get("CHIT_CNT"));
            }
        }
        if(chitCnt == 0){
            updateFlag = true;
        }
        return updateFlag;
    }
    
    
    private void btnSchedNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedNewActionPerformed
        // TODO add your handling code here:
        if (cboProductId.getSelectedIndex() > 0) {
            if (!updateShedule()) {
                ClientUtil.showAlertWindow("Chitty Auction Process already Started. Rescheduling not allowed !!!");
                btnSchedDelete.setEnabled(false);
                tdtInstallmentDt.setDateValue("");
                txtAmount.setText("");
                btnSchedDelete.setEnabled(false);
                tdtInstallmentDt.setEnabled(false);
                txtAmount.setEnabled(false);
                txtBonus.setEnabled(false);
                txtPaymentAmount.setEnabled(false);
                txtPaymentAmount.setText("");
                txtAmount.setText("");
                txtBonus.setText("");
                return;
            }else{
            updateMode = false;
            observable.setNewData(true);
            enableDisableButton(false);
            btnSchedSave.setEnabled(true);
            enableDisablePanScheduleDetails(true);
            tdtInstallmentDt.setDateValue(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
            txtAmount.setText(txtInstAmt.getText());
            if (tblSchedule.getRowCount() == 0) {
                tdtInstallmentDt.setDateValue(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
            } else {
                int count = CommonUtil.convertObjToInt(txtNoofMemberPer.getText());
                int noOfMember = tblSchedule.getRowCount();
                if (count > noOfMember) {
                    Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
                    GregorianCalendar cal = new GregorianCalendar((stDt.getYear() + 1900), stDt.getMonth(), stDt.getDate());
                    if (noOfMember > 0) {
                        cal.add(GregorianCalendar.MONTH, noOfMember);
                    } else {
                        cal.add(GregorianCalendar.MONTH, 0);
                    }
                    tdtInstallmentDt.setDateValue(DateUtil.getStringDate(cal.getTime()));
                } else {
                    ClientUtil.showAlertWindow("Installmet schedule is completed");
                    btnSchedDelete.setEnabled(false);
                    tdtInstallmentDt.setDateValue("");
                    txtAmount.setText("0");
                    btnSchedDelete.setEnabled(false);
                    tdtInstallmentDt.setEnabled(false);
                    txtAmount.setEnabled(false);
                    txtBonus.setEnabled(false);
                    txtPaymentAmount.setEnabled(false);
                    return;
                }
            }
            tdtInstallmentDt.setEnabled(false);
            txtAmount.setEnabled(false);
            txtPaymentAmount.setEnabled(false);
          }  
        } else {
            ClientUtil.showMessageWindow("Product Id Not Selected !!!!");
        }
    }//GEN-LAST:event_btnSchedNewActionPerformed

    private void resetScheduleDetails() {
        lblInstallmentNumber.setText("");
        tdtInstallmentDt.setDateValue("");
        txtAmount.setText("0");
        txtBonus.setText("0");
        txtPaymentAmount.setText("0");
    }

    private void enableDisablePanScheduleDetails(boolean flag) {
        tdtInstallmentDt.setEnabled(flag);
        txtAmount.setEnabled(flag);
        txtBonus.setEnabled(flag);
        txtPaymentAmount.setEnabled(flag);
    }
    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        popUp("Edit");
        lblStatus.setText("Copy");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panSchemeDetails, true);
        ClientUtil.enableDisable(panSchedule, false);
        ClientUtil.enableDisable(panNumberPattern, false);
        ClientUtil.enableDisable(panAccountHead, true);
        txtNextChittalNumber.setEnabled(false);
        acctHeadEnableDisable(true);
        txtNextChittalNumber.setText("1");
        txtSuffix.setText("1");
        btnCopy.setEnabled(false);
        btnNotice_Charge_New.setEnabled(true);
        enableDisableButton(false);
        btnSchedNew.setEnabled(true);
        chkDiscountFirstInst.setEnabled(true);
        txtDiscountAmt.setEnabled(false);
//        //added by nithya on 09-12-2016 for 5521
//        HashMap getProdIdMap = new HashMap();
//        getProdIdMap.put("MODULE", "M");
//        List getProdIdLst = ClientUtil.executeQuery("getgeneratedProductID", getProdIdMap);
//        if (getProdIdLst != null && getProdIdLst.size() > 0) {
//            HashMap generatedProdIdMap = (HashMap) getProdIdLst.get(0);
//            String genProdId = CommonUtil.convertObjToStr(generatedProdIdMap.get("PROD_ID"));
//            txtSchemeName.setText(genProdId);
//        }
//        // End
    }//GEN-LAST:event_btnCopyActionPerformed
    private void enableDisableButton(boolean flag) {
        btnSchedNew.setEnabled(flag);
        btnSchedSave.setEnabled(flag);
        btnSchedDelete.setEnabled(flag);
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panSchemeDetails, false);
        ClientUtil.enableDisable(panSchedule, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
        btnCopy.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void updateAuthorizeStatus(String authorizeStatus) {

        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("PROD_ID", observable.getCboProductId());
            singleAuthorizeMap.put("SCHEME_NAME", observable.getTxtSchemeName());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getCboProductId());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(observable.getCboProductId());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getMDSProductAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        System.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panSchemeDetails, true);
        ClientUtil.enableDisable(panAccountHead, true);
        ClientUtil.clearAll(this);
        txtPostAdvHead.setEnabled(false);
        btnPostAdvHead.setEnabled(false);
        btnLegalChrgAcHead.setEnabled(false);// Added by nithya on 02-08-2017
        btnPartPayBonusRecoveryHead.setEnabled(false);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnSchedNew.setEnabled(true);
        btnNotice_Charge_New.setEnabled(true);
        acctHeadEnableDisable(true);
        btnCopy.setEnabled(false);
        txtInstallments.setEnabled(false);
        txtDay.setEnabled(false);
        txtChittalNumberPattern.setEnabled(false);
        txtSuffix.setText("1");
        txtSuffix.setEnabled(false);
        txtNextChittalNumber.setText("1");
        txtNextChittalNumber.setEnabled(false);
        txtNoofMemberScheme.setEditable(false);
        txtNoofInst.setEnabled(false);
        tdtSchemeEndDt.setEnabled(false);
        rdoMultipleMembers_no.setSelected(true);
//        //added by nithya on 09-12-2016 for 5521
//        HashMap getProdIdMap = new HashMap();
//        getProdIdMap.put("MODULE", "M");
//        List getProdIdLst = ClientUtil.executeQuery("getgeneratedProductID", getProdIdMap);
//        if (getProdIdLst != null && getProdIdLst.size() > 0) {
//            HashMap generatedProdIdMap = (HashMap) getProdIdLst.get(0);
//            String genProdId = CommonUtil.convertObjToStr(generatedProdIdMap.get("PROD_ID"));
//            txtSchemeName.setText(genProdId);
//        }
//        // End
        txtLegalChrgHead.setEnabled(false);
        btnLegalChrgAcHead.setEnabled(true);
        txtPartPayBonusRecoveryHead.setEnabled(false);
        btnPartPayBonusRecoveryHead.setEnabled(true);
        txtOtherChrgeAcHd.setEnabled(false);
        btnOtherChrgAcHd.setEnabled(true);
        txtDiscountAmt.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        ClientUtil.enableDisable(panSchemeDetails, true);
        ClientUtil.enableDisable(panAccountHead, true);
        popUp("Edit");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panSchedule, false);
        ClientUtil.enableDisable(panNumberPattern, false);
        acctHeadEnableDisable(true);
        if (IS_REV_POST_ADV.isSelected()) {
            txtPostAdvHead.setEnabled(true);
            btnPostAdvHead.setEnabled(true);
        } else {
            txtPostAdvHead.setEnabled(false);
            btnPostAdvHead.setEnabled(false);
        }

        txtNextChittalNumber.setEnabled(false);
        cboProductId.setEnabled(false);
        txtSchemeName.setEnabled(false);
        btnCopy.setEnabled(false);
        txtNoofMemberScheme.setEditable(false);
        txtNoofInst.setEnabled(false);
        txtTotAmtPerDivision.setEnabled(false);
        txtTotAmtUnderScheme.setEnabled(false);
        tdtSchemeEndDt.setEnabled(false);
        btnNotice_Charge_New.setEnabled(true);
        enableDisableButton(false);
        btnSchedNew.setEnabled(true);
        txtLegalChrgHead.setEnabled(false);
        txtPartPayBonusRecoveryHead.setEnabled(false);
        txtOtherChrgeAcHd.setEnabled(false);
        chkDiscountFirstInst.setEnabled(true);
        txtDiscountAmt.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panSchemeDetails, false);
        ClientUtil.enableDisable(panSchedule, false);
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void acctHeadEnableDisable(boolean val) {
        btnReceiptHead.setEnabled(val);
        btnPaymentHead.setEnabled(val);
        btnSuspenseHead.setEnabled(val);
        btnMiscellaneousHead.setEnabled(val);
        btnCommisionHead.setEnabled(val);
        btnBonusPayableHead.setEnabled(val);
        btnBonusReceivableHead.setEnabled(val);
        btnPenalHead.setEnabled(val);
        btnThalayalReceiptsHead.setEnabled(val);
        btnThalayalBonusHead.setEnabled(val);
        btnMunnalBonusHead.setEnabled(val);
        btnStampAdvanceHead.setEnabled(val);
        btnARCCostHead.setEnabled(val);
        btnARCExpenseHead.setEnabled(val);
        btnEACostHead.setEnabled(val);
        btnEAExpenseHead.setEnabled(val);
        btnEPCostHead.setEnabled(val);
        btnEPExpenseHead.setEnabled(val);
        btnPostageHead.setEnabled(val);
        btnMunnalReceiptsHead.setEnabled(val);
        btnBankingHead.setEnabled(val);
        btnNoticeChargesHead.setEnabled(val);
        btnChargeHead.setEnabled(val);
        btnCaseExpensesHead.setEnabled(val);
        btnDiscountHead.setEnabled(val);
        btnMDSPayableHead.setEnabled(val);
        btnMDSReceivableHead.setEnabled(val);
        btnSundryReceiptHead.setEnabled(val);
        btnSundryPaymentHead.setEnabled(val);
        btnSuspenseAccNo.setEnabled(val);
        btnForFietedPaymentHead.setEnabled(val);
        btnLegalChrgAcHead.setEnabled(val); // Added by nithya 
        btnPartPayBonusRecoveryHead.setEnabled(val); // Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        if (IS_REV_POST_ADV.isSelected()) {
            if (txtPostAdvHead.getText().equals("")) {
                ClientUtil.displayAlert("Postage Adv Head Should Not Empty");
            }
        }
        updateSchemeFields();
        Date curDt = null;
        Date stDt = null;
        if (tdtSchemeStDt.getDateValue().length() > 0) {
            curDt = (Date) currDt.clone();
            stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSchemeStDt.getDateValue()));
        }
        Date endDt = null;
        Date startDt = null;
        if (tdtSchemeEndDt.getDateValue().length() > 0) {
            endDt = DateUtil.getDateMMDDYYYY(tdtSchemeEndDt.getDateValue());
            startDt = DateUtil.getDateMMDDYYYY(tdtSchemeStDt.getDateValue());
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) { // Added by nithya on 15-02-2020 for KD-1434
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);            
            if (lst != null && lst.size() > 0) {
                HashMap existingProdIdMap = (HashMap) lst.get(0);
                if (existingProdIdMap.containsKey("PROD_ID")) {
                    ClientUtil.showMessageWindow("Scheme Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change scheme Id first");
                    txtSchemeName.setText("");
                }
            }
        }
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), tabMdsType);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else if (rdoPredefinitonInst_no.isSelected() == true && tblSchedule.getRowCount() > 0) {
            ClientUtil.showMessageWindow("Installment Schedule Should Be Empty !!!!");
        } else if (rdoPredefinitonInst_yes.isSelected() == true && tblSchedule.getRowCount() == 0) {
            ClientUtil.showMessageWindow("Installment Schedule Should Not Be Empty !!!!");
            btnSchedNew.setEnabled(true);
        } else if (curDt != null && stDt != null && DateUtil.dateDiff(stDt, curDt) > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            ClientUtil.showAlertWindow("Start date should be greater than or equal to current date");
            tdtSchemeStDt.setDateValue("");
        } else if (endDt.before(startDt)) {
            ClientUtil.showMessageWindow("Scheme End Date Should Be Greater Than Scheme Start Date");
            tdtSchemeEndDt.setDateValue("");
        } else if (rdoPredefinitonInst_yes.isSelected() == true && CommonUtil.convertObjToInt(txtNoofInst.getText()) != tblSchedule.getRowCount()) {
            ClientUtil.showMessageWindow("Installments in Installment schedule should be equal to No.of installments");
        } else {
            if (rdoMultipleMembers_no.isSelected() == true) {
                int auctions = CommonUtil.convertObjToInt(txtNoofAuctions.getText());
                int draws = CommonUtil.convertObjToInt(txtNoofDraws.getText());
                int totalDiv = CommonUtil.convertObjToInt(txtNoofDivision.getText());
                if (totalDiv > (auctions + draws)) {
                    ClientUtil.showMessageWindow("No Of Division Should Be Equal to Total No Of Auctions & Draws !!!!");
                    return;
                }
                if (txtNoofDivision.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Division No should not be empty...!!!");
                    return;
                }
                if (txtNoofAuctions.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No of Auctions should not be empty...!!!");
                    return;
                }
                if (txtNoofDraws.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No of Draws should not be empty...!!!");
                    return;
                }
                if (txtNoofMemberPer.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No of Member per Division should not be empty...!!!");
                    return;
                }
                if (txtNoofMemberScheme.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Toatal No of Member should not be empty...!!!");
                    return;
                }
                if (txtInstAmt.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Installment Amount should not be empty...!!!");
                    return;
                }
                if (txtNoofInst.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No Of Installment should not be empty...!!!");
                    return;
                }
                if (txtTotAmtPerDivision.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Total Amount per Division should not be empty...!!!");
                    return;
                }
            } else {
                if (txtNoofCoChittals.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No Of Co-Chittals should not be empty...!!!");
                    return;
                }
                if (txtNoofCoInstallments.getText().length() == 0) {
                    ClientUtil.showMessageWindow("No Of Installments should not be empty...!!!");
                    return;
                }
                if (txtMaxNoofMemberCoChittals.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Maximum No of Members should not be empty...!!!");
                    return;
                }
                if (txtCoChittalInstAmount.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Installment Amount should not be empty...!!!");
                    return;
                }
            }

            if (cboInstFreq.getSelectedItem().equals("Monthly")) {
                if (cboInstallmentDay.getSelectedIndex() <= 0) {
                    ClientUtil.showMessageWindow("Installment Day Should not be empty...!!! ");
                    return;
                }
                if (cboAuctionDay.getSelectedIndex() <= 0) {
                    ClientUtil.showMessageWindow("Draw/Auction Day Should not be empty...!!! ");
                    return;
                }
            }
            if (rdoSuspenseAccNo.isSelected() == true) {
                if (cboSuspenseProdID.getSelectedIndex() < 1 || txtSuspenseAccNo.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Suspense Product ID and Suspense Account Number Should not be empty...!!! ");
                    return;
                }
            } else {
                if (txtSuspenseHead.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Suspense Account Head Should not be empty...!!! ");
                    return;
                }
            }
            savePerformed();
            btnCancel.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap() != null) {
                if (observable.getProxyReturnMap().containsKey("PROD_ID")) {
                    lockMap.put("PROD_ID", observable.getProxyReturnMap().get("PROD_ID"));
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                super.removeEditLock(observable.getCboProductId());
            }
            setEditLockMap(lockMap);
            setEditLock();
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(observable.getCboProductId());
        viewType = "CANCEL";
        lblStatus.setText("               ");
        lblProductDescVal.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        acctHeadEnableDisable(false);
        btnReceiptHead.setText("");
        btnPaymentHead.setText("");
        btnSuspenseHead.setText("");
        btnMiscellaneousHead.setText("");
        btnCommisionHead.setText("");
        btnBonusPayableHead.setText("");
        btnBonusReceivableHead.setText("");
        btnPenalHead.setText("");
        btnThalayalReceiptsHead.setText("");
        btnThalayalBonusHead.setText("");
        btnMunnalBonusHead.setText("");
        btnMunnalReceiptsHead.setText("");
        btnBankingHead.setText("");
        btnNoticeChargesHead.setText("");
        btnChargeHead.setText("");
        btnCaseExpensesHead.setText("");
        btnDiscountHead.setText("");
        btnMDSPayableHead.setText("");
        btnMDSReceivableHead.setText("");
        btnSundryReceiptHead.setText("");
        btnSundryPaymentHead.setText("");
        enableDisableButton(false);
        enableDisableNoticeCharge_SaveDelete(false);
        resetScheduleDetails();
        btnCopy.setEnabled(true);
        btnCreateHead.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getMDSProductEditDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getMDSProductView");
        } else if (viewType.equals("ACCT_HEAD") || viewType.equals("RECEIPT_HEAD") || viewType.equals("PAYMENT_HEAD")
                || viewType.equals("SUSPENSE_HEAD") || viewType.equals("MISCELLANEOUS_HEAD") || viewType.equals("COMMISION_HEAD")
                || viewType.equals("BONUS_PAYABLE_HEAD") || viewType.equals("BONUS_RECEIVABLE_HEAD") || viewType.equals("PENAL_HEAD")
                || viewType.equals("THALAYAL_BONUS_HEAD") || viewType.equals("MUNNAL_BONUS_HEAD") || viewType.equals("THALAYAL_RECEIPT_HEAD")
                || viewType.equals("MUNNAL_RECEIPT_HEAD") || viewType.equals("BANKING_HEAD") || viewType.equals("NOTICE_HEAD") || viewType.equals("CHARGE_HEAD") || viewType.equals("STAMP_HEAD")
                || viewType.equals("DISCOUNT_HEAD") || viewType.equals("ARC_COST_HEAD") || viewType.equals("ARC_EXPENSE_HEAD") || viewType.equals("EA_COST_HEAD")
                || viewType.equals("EA_EXPENSE_HEAD") || viewType.equals("EP_COST_HEAD") || viewType.equals("EP_EXPENSE_HEAD")
                || viewType.equals("CASE_EXPENSE_HEAD") || viewType.equals("MDS_PAYABLE_HEAD") || viewType.equals("MDS_RECEIVABLE_HEAD") || viewType.equals("SUNDRY_RECEIPT_HEAD")
                || viewType.equals("SUNDRY_PAYMENT_HEAD") || viewType.equals("POSTAGE_HEAD") || viewType.equals("POST_ADV_HEAD") ||viewType.equals("FORFEITED_PAYMENT_HEAD") || viewType.equals("LEGAL_CHRG_HEAD") || viewType.equals("PART_PAY_BONUS_RECOVERY_HEAD") || viewType.equals("OTHER_CHARGE_HEAD")) {
            viewMap.put(CommonConstants.MAP_NAME, "MDS.getSelectAcctHeadTOList");
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (currAction.equalsIgnoreCase("SUSPENSE_ACC_NO")) {
            viewMap = new HashMap();
            whereMap.put("PROD_ID", ((ComboBoxModel) cboSuspenseProdID.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountListSA");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (viewType == "RECEIPT_HEAD") {
                txtReceiptHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnReceiptHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "PAYMENT_HEAD") {
                txtPaymentHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnPaymentHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "SUSPENSE_HEAD") {
                txtSuspenseHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnSuspenseHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "MISCELLANEOUS_HEAD") {
                txtMiscellaneousHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnMiscellaneousHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "COMMISION_HEAD") {
                txtCommisionHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnCommisionHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "BONUS_PAYABLE_HEAD") {
                txtBonusPayableHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnBonusPayableHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "BONUS_RECEIVABLE_HEAD") {
                txtBonusReceivableHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnBonusReceivableHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "PENAL_HEAD") {
                txtPenalHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnPenalHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "THALAYAL_RECEIPT_HEAD") {
                txtThalayalReceiptsHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnThalayalReceiptsHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "THALAYAL_BONUS_HEAD") {
                txtThalayalBonusHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnThalayalBonusHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "MUNNAL_BONUS_HEAD") {
                txtMunnalBonusHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnMunnalBonusHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "MUNNAL_RECEIPT_HEAD") {
                txtMunnalReceiptsHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnMunnalReceiptsHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "BANKING_HEAD") {
                txtBankingHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnBankingHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "NOTICE_HEAD") {
                txtNoticeChargesHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnNoticeChargesHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "CHARGE_HEAD") {
                txtChargeHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnChargeHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "STAMP_HEAD") {
                txtStampAdvanceHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnStampAdvanceHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "ARC_COST_HEAD") {
                txtARCCostHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnARCCostHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "ARC_EXPENSE_HEAD") {
                txtARCExpenseHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnARCExpenseHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "EA_COST_HEAD") {
                txtEACostHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnEACostHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "EA_EXPENSE_HEAD") {
                txtEAExpenseHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnEAExpenseHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "EP_COST_HEAD") {
                txtEPCostHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnEPCostHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "EP_EXPENSE_HEAD") {
                txtEPExpenseHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnEPExpenseHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "POSTAGE_HEAD") {
                txtPostageHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnPostageHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "CASE_EXPENSE_HEAD") {
                txtCaseExpensesHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnCaseExpensesHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "DISCOUNT_HEAD") {
                txtDiscountHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnDiscountHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "MDS_PAYABLE_HEAD") {
                txtMDSPayableHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnMDSPayableHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "MDS_RECEIVABLE_HEAD") {
                txtMDSReceivableHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnMDSReceivableHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "SUNDRY_RECEIPT_HEAD") {
                txtSundryReceiptHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnSundryReceiptHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "SUNDRY_PAYMENT_HEAD") {
                txtSundryPaymentHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnSundryPaymentHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "FORFEITED_PAYMENT_HEAD") {
                txtForFeitedPaymentHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnForFietedPaymentHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "POST_ADV_HEAD") {
                txtPostAdvHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnPostAdvHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if(viewType == "LEGAL_CHRG_HEAD"){
               // LEGAL_CHRG_HEAD
                txtLegalChrgHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead))); 
                btnLegalChrgAcHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
            } else if(viewType == "PART_PAY_BONUS_RECOVERY_HEAD"){              
                txtPartPayBonusRecoveryHead.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead))); 
                btnPartPayBonusRecoveryHead.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
            } else if (viewType == "OTHER_CHARGE_HEAD") {
                txtOtherChrgeAcHd.setText(CommonUtil.convertObjToStr(hashMap.get(accountHead)));
                btnOtherChrgAcHd.setToolTipText(CommonUtil.convertObjToStr(hashMap.get(accountHeadDesc)));
                return;
            } else if (viewType == "SUSPENSE_ACC_NO") {
                txtSuspenseAccNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                return;
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                this.setButtonEnableDisable();
                observable.setCboProductId(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                observable.getData(hashMap);
                tblSchedule.setModel(observable.getTblScheduleDetails());
                update();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    List applicationList = ClientUtil.executeQuery("getSelectRecordForEnquiryDetails", hashMap);
                    if (applicationList != null && applicationList.size() > 0) {
                        ClientUtil.enableDisable(panMultipleMembers, false);
                    } else {
                        ClientUtil.enableDisable(panMultipleMembers, true);
                    }
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                observable.setCboProductId(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.getData(hashMap);
                tblSchedule.setModel(observable.getTblScheduleDetails());
                update();
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this, false);
            }
            txtSuspenseAccNo.setEnabled(false);
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

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

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void txtPostAdvHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostAdvHeadFocusLost
        // TODO add your handling code here:
        if (!txtPostAdvHead.getText().equals("")) {
            btnPostAdvHead.setToolTipText(getAccHdDesc(txtPostAdvHead.getText()));
        }
    }//GEN-LAST:event_txtPostAdvHeadFocusLost

    private void txtChargeHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargeHeadFocusLost
        // TODO add your handling code here:
        if (!txtChargeHead.getText().equals("")) {
            btnChargeHead.setToolTipText(getAccHdDesc(txtChargeHead.getText()));
        }
    }//GEN-LAST:event_txtChargeHeadFocusLost

    private void btnChargeHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeHeadActionPerformed
        // TODO add your handling code here:
        popUp("CHARGE_HEAD");
    }//GEN-LAST:event_btnChargeHeadActionPerformed

    private void btnPostAdvHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostAdvHeadActionPerformed
        // TODO add your handling code here:
        popUp("POST_ADV_HEAD");
    }//GEN-LAST:event_btnPostAdvHeadActionPerformed

    private void IS_REV_POST_ADVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IS_REV_POST_ADVActionPerformed
        // TODO add your handling code here:
        if (IS_REV_POST_ADV.isSelected()) {

            txtPostAdvHead.setEnabled(true);
            btnPostAdvHead.setEnabled(true);
        } else {
            txtPostAdvHead.setEnabled(false);
            btnPostAdvHead.setEnabled(false);
        }
    }//GEN-LAST:event_IS_REV_POST_ADVActionPerformed

    private void txtForFeitedPaymentHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtForFeitedPaymentHeadFocusLost
        // TODO add your handling code here:
     if (!txtForFeitedPaymentHead.getText().equals("")) {
            btnForFietedPaymentHead.setToolTipText(getAccHdDesc(txtForFeitedPaymentHead.getText()));
    }                                                 
}//GEN-LAST:event_txtForFeitedPaymentHeadFocusLost

    private void btnForFietedPaymentHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForFietedPaymentHeadActionPerformed
        // TODO add your handling code here:
    popUp("FORFEITED_PAYMENT_HEAD");
    }//GEN-LAST:event_btnForFietedPaymentHeadActionPerformed

    private void chkSpecialSchemeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSpecialSchemeItemStateChanged
        // TODO add your handling code here:
        if(chkSpecialScheme.isSelected()){
            isASpecialScheme = "Y";
            txtNoofMemberScheme.setEditable(true);
        }else{
            isASpecialScheme = "N";
            txtNoofMemberScheme.setEditable(false);
        }
    }//GEN-LAST:event_chkSpecialSchemeItemStateChanged

    private void chkSMSAlertItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSMSAlertItemStateChanged
        // TODO add your handling code here:
        if (chkSMSAlert.isSelected()) {
            observable.setChkSMSAlert("Y");
        } else {
            observable.setChkSMSAlert("N");
        }
    }//GEN-LAST:event_chkSMSAlertItemStateChanged

private void btnCreateHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateHeadActionPerformed
// TODO add your handling code here:
        if(txtSchemeDesc.getText()!=null && txtSchemeDesc.getText().length()>0){
        btnCreateHead.setEnabled(false);
        HashMap headMap = new HashMap();
        headMap.put("CREATE_HEAD", txtSchemeDesc.getText());
        headMap.put("MODUL", "M");
            try {
                observable.createHead(headMap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){              
                java.awt.Component[] children = panAccountHead.getComponents();                
                List createdHeads = ClientUtil.executeQuery("getSelectCreatedHeads", headMap);
                if (createdHeads != null && createdHeads.size() > 0) {
                    for(int i=0;i<createdHeads.size();i++){
                        headMap = (HashMap) createdHeads.get(i);
                        for (int j = 0; j < children.length; j++) {
                            if ((children[j] != null)) {
                                if (children[j] instanceof javax.swing.JTextField) {
                                    if(((javax.swing.JTextField) children[j]).getText()!=null && ((javax.swing.JTextField) children[j]).getText().length()>0){                                    
                                    if(((javax.swing.JTextField) children[j]).getText().substring(0, 4).equalsIgnoreCase(CommonUtil.convertObjToStr(headMap.get("MJR_AC_HD_ID")))){
                                       ((javax.swing.JTextField) children[j]).setText(CommonUtil.convertObjToStr(headMap.get("AC_HD_CODE")));
                                    }
                                }
                                }
                            }
                        }                        
                    }
                }
            }
    }else{
        ClientUtil.showMessageWindow("Please fill Product Description!!!");
    }
}//GEN-LAST:event_btnCreateHeadActionPerformed

private void txtSchemeDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeDescFocusLost
// TODO add your handling code here:
    if(txtSchemeDesc.getText()!=null && txtSchemeDesc.getText().length()>0)
        btnCreateHead.setEnabled(true);
}//GEN-LAST:event_txtSchemeDescFocusLost

    private void txtSchemeDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeDescFocusGained
        // TODO add your handling code here:
        // Added by nithya : w.r.t. feedback after testing
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
            System.out.println("getSBODBorrowerEligAmt : " + lst);
            if (lst != null && lst.size() > 0) {
                HashMap existingProdIdMap = (HashMap) lst.get(0);
                if (existingProdIdMap.containsKey("PROD_ID")) {
                    ClientUtil.showMessageWindow("Scheme Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change scheme Id first");
                    txtSchemeName.setText("");
                }
            }
        }
    }//GEN-LAST:event_txtSchemeDescFocusGained

    private void btnLegalChrgAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLegalChrgAcHeadActionPerformed
        // TODO add your handling code here:
          popUp("LEGAL_CHRG_HEAD");// Added by nithya on 02-08-2017 for mantis 7319
    }//GEN-LAST:event_btnLegalChrgAcHeadActionPerformed

    private void cboGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGroupNameActionPerformed
        // TODO add your handling code here:      
    }//GEN-LAST:event_cboGroupNameActionPerformed

    private void btnPartPayBonusRecoveryHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartPayBonusRecoveryHeadActionPerformed
        // TODO add your handling code here:
        popUp("PART_PAY_BONUS_RECOVERY_HEAD");
    }//GEN-LAST:event_btnPartPayBonusRecoveryHeadActionPerformed

    private void chkDiscountFirstInstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDiscountFirstInstActionPerformed
        // TODO add your handling code here:
        if(chkDiscountFirstInst.isSelected()){
            txtDiscountAmt.setEnabled(true);
        }else{
            txtDiscountAmt.setEnabled(false);
            txtDiscountAmt.setText("0");
        }
    }//GEN-LAST:event_chkDiscountFirstInstActionPerformed

    private void btnOtherChrgAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherChrgAcHdActionPerformed
        // TODO add your handling code here:
        popUp("OTHER_CHARGE_HEAD");
    }//GEN-LAST:event_btnOtherChrgAcHdActionPerformed

    /**
     * This method helps in popoualting the data from the data base
     *
     * @param currField Action the argument is passed according to the command
     * issued
     */
    private void callView(String currField) {
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CCheckBox IS_REV_POST_ADV;
    private com.see.truetransact.uicomponent.CButton btnARCCostHead;
    private com.see.truetransact.uicomponent.CButton btnARCExpenseHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankingHead;
    private com.see.truetransact.uicomponent.CButton btnBonusPayableHead;
    private com.see.truetransact.uicomponent.CButton btnBonusReceivableHead;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCaseExpensesHead;
    private com.see.truetransact.uicomponent.CButton btnChargeHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommisionHead;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnCreateHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDiscountHead;
    private com.see.truetransact.uicomponent.CButton btnEACostHead;
    private com.see.truetransact.uicomponent.CButton btnEAExpenseHead;
    private com.see.truetransact.uicomponent.CButton btnEPCostHead;
    private com.see.truetransact.uicomponent.CButton btnEPExpenseHead;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnForFietedPaymentHead;
    private com.see.truetransact.uicomponent.CButton btnLegalChrgAcHead;
    private com.see.truetransact.uicomponent.CButton btnMDSPayableHead;
    private com.see.truetransact.uicomponent.CButton btnMDSReceivableHead;
    private com.see.truetransact.uicomponent.CButton btnMiscellaneousHead;
    private com.see.truetransact.uicomponent.CButton btnMunnalBonusHead;
    private com.see.truetransact.uicomponent.CButton btnMunnalReceiptsHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNoticeChargesHead;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Delete;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_New;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Save;
    private com.see.truetransact.uicomponent.CButton btnOtherChrgAcHd;
    private com.see.truetransact.uicomponent.CButton btnPartPayBonusRecoveryHead;
    private com.see.truetransact.uicomponent.CButton btnPaymentHead;
    private com.see.truetransact.uicomponent.CButton btnPenalHead;
    private com.see.truetransact.uicomponent.CButton btnPostAdvHead;
    private com.see.truetransact.uicomponent.CButton btnPostageHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReceiptHead;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchedDelete;
    private com.see.truetransact.uicomponent.CButton btnSchedNew;
    private com.see.truetransact.uicomponent.CButton btnSchedSave;
    private com.see.truetransact.uicomponent.CButton btnStampAdvanceHead;
    private com.see.truetransact.uicomponent.CButton btnSundryPaymentHead;
    private com.see.truetransact.uicomponent.CButton btnSundryReceiptHead;
    private com.see.truetransact.uicomponent.CButton btnSuspenseAccNo;
    private com.see.truetransact.uicomponent.CButton btnSuspenseHead;
    private com.see.truetransact.uicomponent.CButton btnThalayalBonusHead;
    private com.see.truetransact.uicomponent.CButton btnThalayalReceiptsHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CComboBox cboAuctionDay;
    private com.see.truetransact.uicomponent.CComboBox cboGroupName;
    private com.see.truetransact.uicomponent.CComboBox cboInstFreq;
    private com.see.truetransact.uicomponent.CComboBox cboInstallmentDay;
    private com.see.truetransact.uicomponent.CComboBox cboNoticeType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboSuspenseProdID;
    private com.see.truetransact.uicomponent.CCheckBox chkApplicable1;
    private com.see.truetransact.uicomponent.CCheckBox chkApplicable2;
    private com.see.truetransact.uicomponent.CCheckBox chkApplicable3;
    private com.see.truetransact.uicomponent.CCheckBox chkApplicable4;
    private com.see.truetransact.uicomponent.CCheckBox chkBankSettlement;
    private com.see.truetransact.uicomponent.CCheckBox chkBonusPrint;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditStampAdvance;
    private com.see.truetransact.uicomponent.CCheckBox chkDiscountFirstInst;
    private com.see.truetransact.uicomponent.CCheckBox chkSMSAlert;
    private com.see.truetransact.uicomponent.CCheckBox chkSpecialScheme;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblARCCostHead;
    private com.see.truetransact.uicomponent.CLabel lblARCExpenseHead;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblApplicable1;
    private com.see.truetransact.uicomponent.CLabel lblApplicable2;
    private com.see.truetransact.uicomponent.CLabel lblApplicable3;
    private com.see.truetransact.uicomponent.CLabel lblApplicable4;
    private com.see.truetransact.uicomponent.CLabel lblApplicableDivision;
    private com.see.truetransact.uicomponent.CLabel lblAuctionDay;
    private com.see.truetransact.uicomponent.CLabel lblAutionTime;
    private com.see.truetransact.uicomponent.CLabel lblBankingHead;
    private com.see.truetransact.uicomponent.CLabel lblBonus;
    private com.see.truetransact.uicomponent.CLabel lblBonusPayableHead;
    private com.see.truetransact.uicomponent.CLabel lblBonusReceivableHead;
    private com.see.truetransact.uicomponent.CLabel lblCaseExpensesHead;
    private com.see.truetransact.uicomponent.CLabel lblChargeHead;
    private com.see.truetransact.uicomponent.CLabel lblChittalNumberPattern;
    private com.see.truetransact.uicomponent.CLabel lblClosureRate;
    private com.see.truetransact.uicomponent.CLabel lblCoChittalInstAmount;
    private com.see.truetransact.uicomponent.CLabel lblCommisionHead;
    private com.see.truetransact.uicomponent.CLabel lblDiscountHead;
    private com.see.truetransact.uicomponent.CLabel lblEACostHead;
    private com.see.truetransact.uicomponent.CLabel lblEAExpenseHead;
    private com.see.truetransact.uicomponent.CLabel lblEPCostHead;
    private com.see.truetransact.uicomponent.CLabel lblEPExpenseHead;
    private com.see.truetransact.uicomponent.CLabel lblForFeitedHead;
    private com.see.truetransact.uicomponent.CLabel lblInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblInstFreq;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentDay;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentDt;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNumber;
    private com.see.truetransact.uicomponent.CLabel lblLegalChrgHead;
    private com.see.truetransact.uicomponent.CLabel lblMDSPayableHead;
    private com.see.truetransact.uicomponent.CLabel lblMDSReceivableHead;
    private com.see.truetransact.uicomponent.CLabel lblMDSType;
    private com.see.truetransact.uicomponent.CLabel lblMaxNoofMemberCoChittals;
    private com.see.truetransact.uicomponent.CLabel lblMdsPostAdv;
    private com.see.truetransact.uicomponent.CLabel lblMiscellaneousHead;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMultipleMembers;
    private com.see.truetransact.uicomponent.CLabel lblMunnalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblMunnalBonusHead;
    private com.see.truetransact.uicomponent.CLabel lblMunnalReceiptsHead;
    private com.see.truetransact.uicomponent.CLabel lblNextChittalNumber;
    private com.see.truetransact.uicomponent.CLabel lblNoofAuctions;
    private com.see.truetransact.uicomponent.CLabel lblNoofCoChittals;
    private com.see.truetransact.uicomponent.CLabel lblNoofCoInstallments;
    private com.see.truetransact.uicomponent.CLabel lblNoofDivision;
    private com.see.truetransact.uicomponent.CLabel lblNoofDraws;
    private com.see.truetransact.uicomponent.CLabel lblNoofInst;
    private com.see.truetransact.uicomponent.CLabel lblNoofMemberPer;
    private com.see.truetransact.uicomponent.CLabel lblNoofMemberScheme;
    private com.see.truetransact.uicomponent.CLabel lblNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoticeChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblNoticeType;
    private com.see.truetransact.uicomponent.CLabel lblOtherChrg;
    private com.see.truetransact.uicomponent.CLabel lblPaymentAmount;
    private com.see.truetransact.uicomponent.CLabel lblPaymentDone;
    private com.see.truetransact.uicomponent.CLabel lblPaymentHead;
    private com.see.truetransact.uicomponent.CLabel lblPenalHead;
    private com.see.truetransact.uicomponent.CLabel lblPostageAmt;
    private com.see.truetransact.uicomponent.CLabel lblPostageHead;
    private com.see.truetransact.uicomponent.CLabel lblPredefinitonInst;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CLabel lblProductDescVal;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblReceiptHead;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblSacnNo;
    private com.see.truetransact.uicomponent.CLabel lblSacncDt;
    private com.see.truetransact.uicomponent.CLabel lblSchemeDesc;
    private com.see.truetransact.uicomponent.CLabel lblSchemeEndDt;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSchemeStDt;
    private com.see.truetransact.uicomponent.CLabel lblSmsAlert;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace46;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpecialScheme;
    private com.see.truetransact.uicomponent.CLabel lblStampAdvanceHead;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSundryPaymentHead;
    private com.see.truetransact.uicomponent.CLabel lblSundryReceiptHead;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseAccNo;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseHead;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProdID;
    private com.see.truetransact.uicomponent.CLabel lblThalayalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblThalayalBonusHead;
    private com.see.truetransact.uicomponent.CLabel lblThalayalReceiptsHead;
    private com.see.truetransact.uicomponent.CLabel lblTotAmtPerDivision;
    private com.see.truetransact.uicomponent.CLabel lblTotAmtUnderScheme;
    private com.see.truetransact.uicomponent.CLabel lbllResolutionDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panApplicableDivision;
    private com.see.truetransact.uicomponent.CPanel panChitDetails;
    private com.see.truetransact.uicomponent.CPanel panCoChittalDetails;
    private com.see.truetransact.uicomponent.CPanel panDiscountDetails;
    private com.see.truetransact.uicomponent.CPanel panDivisionDetails;
    private com.see.truetransact.uicomponent.CPanel panInsSchedule;
    private com.see.truetransact.uicomponent.CPanel panInsideSchemeDetail;
    private com.see.truetransact.uicomponent.CPanel panInstallSchedule;
    private com.see.truetransact.uicomponent.CPanel panInstallmentSchedule;
    private com.see.truetransact.uicomponent.CPanel panMultipleMembers;
    private com.see.truetransact.uicomponent.CPanel panNoticeButton;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges_Table;
    private com.see.truetransact.uicomponent.CPanel panNoticecharge_Amt;
    private com.see.truetransact.uicomponent.CPanel panNumberPattern;
    private com.see.truetransact.uicomponent.CPanel panOtherChrg;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR2;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR5;
    private com.see.truetransact.uicomponent.CPanel panSchedBtn;
    private com.see.truetransact.uicomponent.CPanel panSchedule;
    private com.see.truetransact.uicomponent.CPanel panScheduleDetails;
    private com.see.truetransact.uicomponent.CPanel panScheduleTable;
    private com.see.truetransact.uicomponent.CPanel panSchemeDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspenceDetails;
    private com.see.truetransact.uicomponent.CPanel panThalayalAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplicableDivision;
    private com.see.truetransact.uicomponent.CRadioButton rdoApplicableDivision_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoApplicableDivision_yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultipleMembers;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultipleMembers_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultipleMembers_yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMunnalAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoMunnalAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMunnalAllowed_no;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPaymentDone;
    private com.see.truetransact.uicomponent.CRadioButton rdoPaymentDone_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoPaymentDone_yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPredefinitonInst;
    private com.see.truetransact.uicomponent.CRadioButton rdoPredefinitonInst_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoPredefinitonInst_yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSuspense;
    private com.see.truetransact.uicomponent.CRadioButton rdoSuspenseAccNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoSuspenseGL;
    private com.see.truetransact.uicomponent.CButtonGroup rdoThalayalAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoThalayalAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoThalayalAllowed_yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpNoticeCharges;
    private com.see.truetransact.uicomponent.CScrollPane srpScheduleTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabMdsType;
    private com.see.truetransact.uicomponent.CTable tblNoticeCharges;
    private com.see.truetransact.uicomponent.CTable tblSchedule;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtInstallmentDt;
    private com.see.truetransact.uicomponent.CDateField tdtSancDt;
    private com.see.truetransact.uicomponent.CDateField tdtSchemeEndDt;
    private com.see.truetransact.uicomponent.CDateField tdtSchemeStDt;
    private com.see.truetransact.uicomponent.CDateField tdtlResolutionDate;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtARCCostHead;
    private com.see.truetransact.uicomponent.CTextField txtARCExpenseHead;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtAutionTime;
    private com.see.truetransact.uicomponent.CTextField txtBankingHead;
    private com.see.truetransact.uicomponent.CTextField txtBonus;
    private com.see.truetransact.uicomponent.CTextField txtBonusPayableHead;
    private com.see.truetransact.uicomponent.CTextField txtBonusReceivableHead;
    private com.see.truetransact.uicomponent.CTextField txtCaseExpensesHead;
    private com.see.truetransact.uicomponent.CTextField txtChargeHead;
    private com.see.truetransact.uicomponent.CTextField txtChittalNumberPattern;
    private com.see.truetransact.uicomponent.CTextField txtClosureRate;
    private com.see.truetransact.uicomponent.CTextField txtCoChittalInstAmount;
    private com.see.truetransact.uicomponent.CTextField txtCommisionHead;
    private com.see.truetransact.uicomponent.CTextField txtDay;
    private com.see.truetransact.uicomponent.CTextField txtDiscountAmt;
    private com.see.truetransact.uicomponent.CTextField txtDiscountHead;
    private com.see.truetransact.uicomponent.CTextField txtEACostHead;
    private com.see.truetransact.uicomponent.CTextField txtEAExpenseHead;
    private com.see.truetransact.uicomponent.CTextField txtEPCostHead;
    private com.see.truetransact.uicomponent.CTextField txtEPExpenseHead;
    private com.see.truetransact.uicomponent.CTextField txtForFeitedPaymentHead;
    private com.see.truetransact.uicomponent.CTextField txtInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstallments;
    private com.see.truetransact.uicomponent.CTextField txtLegalChrgHead;
    private com.see.truetransact.uicomponent.CTextField txtMDSPayableHead;
    private com.see.truetransact.uicomponent.CTextField txtMDSReceivableHead;
    private com.see.truetransact.uicomponent.CTextField txtMaxNoofMemberCoChittals;
    private com.see.truetransact.uicomponent.CTextField txtMdsType;
    private com.see.truetransact.uicomponent.CTextField txtMiscellaneousHead;
    private com.see.truetransact.uicomponent.CTextField txtMunnalBonusHead;
    private com.see.truetransact.uicomponent.CTextField txtMunnalReceiptsHead;
    private com.see.truetransact.uicomponent.CTextField txtNextChittalNumber;
    private com.see.truetransact.uicomponent.CTextField txtNoofAuctions;
    private com.see.truetransact.uicomponent.CTextField txtNoofCoChittals;
    private com.see.truetransact.uicomponent.CTextField txtNoofCoInstallments;
    private com.see.truetransact.uicomponent.CTextField txtNoofDivision;
    private com.see.truetransact.uicomponent.CTextField txtNoofDraws;
    private com.see.truetransact.uicomponent.CTextField txtNoofInst;
    private com.see.truetransact.uicomponent.CTextField txtNoofMemberPer;
    private com.see.truetransact.uicomponent.CTextField txtNoofMemberScheme;
    private com.see.truetransact.uicomponent.CTextField txtNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoticeChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtOtherChrgeAcHd;
    private com.see.truetransact.uicomponent.CTextField txtPartPayBonusRecoveryHead;
    private com.see.truetransact.uicomponent.CTextField txtPaymentAmount;
    private com.see.truetransact.uicomponent.CTextField txtPaymentHead;
    private com.see.truetransact.uicomponent.CTextField txtPenalHead;
    private com.see.truetransact.uicomponent.CTextField txtPostAdvHead;
    private com.see.truetransact.uicomponent.CTextField txtPostageChargeAmt;
    private com.see.truetransact.uicomponent.CTextField txtPostageHead;
    private com.see.truetransact.uicomponent.CTextField txtReceiptHead;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtSancNo;
    private com.see.truetransact.uicomponent.CTextField txtSchemeDesc;
    private com.see.truetransact.uicomponent.CTextField txtSchemeGraceperiod;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtStampAdvanceHead;
    private com.see.truetransact.uicomponent.CTextField txtSuffix;
    private com.see.truetransact.uicomponent.CTextField txtSundryPaymentHead;
    private com.see.truetransact.uicomponent.CTextField txtSundryReceiptHead;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseAccNo;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseHead;
    private com.see.truetransact.uicomponent.CTextField txtThalayalBonusHead;
    private com.see.truetransact.uicomponent.CTextField txtThalayalReceiptsHead;
    private com.see.truetransact.uicomponent.CTextField txtTotAmtPerDivision;
    private com.see.truetransact.uicomponent.CTextField txtTotAmtUnderScheme;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSTypeUI gui = new MDSTypeUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}