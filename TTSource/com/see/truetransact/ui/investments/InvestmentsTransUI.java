/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */
package com.see.truetransact.ui.investments;

/**
 *
 * @author Ashok
 * @modified : Sunil Added Edit Locking - 08-07-2005
 */
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.ui.common.report.PrintClass;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;
import com.see.truetransact.ui.investments.InvestmentsTransMRB;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;

public class InvestmentsTransUI extends CInternalFrame implements UIMandatoryField, Observer {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private InvestmentsTransOB observable;
    private InvestmentsTransMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsTransRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE = "Authorize";
    private TransactionUI transactionUI = new TransactionUI();
    private Date curDate = null;
    String withOrWithoutInterest = "";
    String intWithPrincipal = "";
    private ArrayList tableList = new ArrayList();
    private HashMap finalMap = new HashMap();
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    PrintClass print=new PrintClass();

    /**
     * Creates new form ShareProductUI
     */
    public InvestmentsTransUI() {
        initUIComponents();
        transactionUI.setSourceScreen("INVESTMENT_TRANS");
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        panAmortizationDetails.setName("Transaction Details");
//        //Added By Suresh
//        transactionUI.setProdType();
    }

    /**
     * Initialsises the UIComponents
     */
    private void initUIComponents() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInvestmentTrans, getMandatoryHashMap());
        setHelpMessage();
        setObservable();
        observable.resetForm();
        rdoSelectectedTrueOrFalse();
        resetTransactionUI();
        lblPeriodValue.setText("");
        initComponentData();
        ClientUtil.enableDisable(panInvestmentTrans, false);
        setButtonEnableDisable();
        txtInvestmentName.setEnabled(false);
        rdoCloseYesOrNo.setVisible(false);
        rdoCloseYesOrNo.setSelected(false);
        btnInvestmentID.setEnabled(false);
        lblCloserTypeValue.setText("");
        ClientUtil.enableDisable(panAmortizationDetails, false);
        panPurchaseOrSale1.setVisible(false);
        btnAmortization.setVisible(false);
        reSetClose();
        cboInvestmentBehaves.setEditable(false);
        txtInternalAccNo.setEnabled(false);
        panInvestSBorCATrans.setVisible(false);
        btnAccRefNo.setEnabled(false);
        panPurchaseMode.setVisible(false);
        panPurchaseThrough.setVisible(false);
        rdoCharges.setVisible(false);
        lblTotalInterestPaid.setVisible(false);
        lblTotalInterestPaidValue.setVisible(false);
        lblMaturityAmt.setVisible(false);
        lblMaturityAmtValue.setVisible(false);
        lblTotalPremiumCollected.setVisible(false);
        lblTotalPremiumCollectedValue.setVisible(false);
        commonDeatilsEnableDisable(false);
        lblInterestPaymentFrequency.setVisible(false);
        lblInterestPaymentFrequencyValue.setVisible(false);
        lblTotalInvestmentAmount.setVisible(false);
        lblTotalInvestmentAmountValue.setVisible(false);
        lblTotalPremiumPaid.setVisible(false);
        lblPurchaseRate.setVisible(false);
        txtPurchaseRate.setVisible(false);
        lblPurchaseorSalBy.setVisible(false);
        txtPurchaseorSalBy.setVisible(false);
        lblNoOfUnits.setVisible(false);
        txtNoOfUnits.setVisible(false);
        panPurchaseThrough.setVisible(false);
        panPurchaseMode.setVisible(false);
        lblBrokerName.setVisible(false);
        txtBrokerName.setVisible(false);
        lblCloserTypeValue.setVisible(false);
        lblCloserType.setVisible(false);
        lblPreMatureCloserRate.setVisible(false);
        txtPreMatureCloserRate.setVisible(false);
        rdoCloseYesOrNo.setVisible(false);
        lblBrokerCommission.setVisible(false);
        txtBrokerCommission.setVisible(false);
        txtTotalInvestmentAmount.setVisible(false);
        lblTransTotalInvestmentAmount.setVisible(false);
        txtPremiumAmount.setVisible(false);
        lblPremiumAmount.setVisible(false);
        txtDiscount.setVisible(false);
        lblDiscount.setVisible(false);
        lblNarration.setVisible(true);
        txtNarration.setVisible(true);
        lblDebitChequeNo.setVisible(false);
        txtDebitChequeNo.setVisible(false);
        btnTranAccRefNo.setEnabled(false);
        btnInvestmentIDTrans.setEnabled(false);
        txtPrematureROI.setVisible(false);
        txtPrematureIntAmt.setVisible(false);
        lblIPrematureROI.setVisible(false);
        lblPrematureIntAmt.setVisible(false);
        txtInvestmentAmount.setEnabled(false);
        initTableData();
        //tblMultiTransTable.setModel(observable.getTblCheckBookTable());
    }

    private void rdoSelectectedTrueOrFalse() {
        ClientUtil.enableDisable(panPurchaseThrough, false);
        rdoMarket.setSelected(false);
        rdoOffMarket.setSelected(false);
        txtBrokerName.setVisible(false);
        rdoPrimary.setSelected(false);
        rdoSecondary.setSelected(false);
        lblBrokerName.setVisible(false);
        txtBrokerCommission.setVisible(false);
        lblBrokerCommission.setVisible(false);
        txtInvestmentAmount.setEnabled(false);
        txtDiscount.setEnabled(false);
        txtDiscount.setEditable(false);
        txtPremiumAmount.setEnabled(false);
        txtPremiumAmount.setEditable(false);
        txtTotalInvestmentAmount.setEnabled(false);
        txtTotalInvestmentAmount.setEditable(false);
        lblTotalAmountValue.setText("");

        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            rdoPurchase.setSelected(false);
            rdoSale.setSelected(false);
            rdoInterest.setSelected(false);
            rdoCharges.setSelected(false);
        }
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
        lblInvestmentID.setName("lblInvestmentID");
        lblInvestmentName.setName("lblInvestmentName");
        lblInvestmentBehaves.setName("lblInvestmentBehaves");
        lblIssueDate.setName("lblIssueDate");
        lblFaceValue.setName("lblFaceValue");
        lblDebitChequeNo.setName("lblDebitChequeNo");
        lblLastIntPaidDate.setName("lblLastIntPaidDate");
        lblInterestPaymentFrequency.setName("lblInterestPaymentFrequency");
        lblPeriod.setName("lblPeriod");
        lblCoupenRate.setName("lblCoupenRate");
        lblMaturityDate.setName("lblMaturityDate");
        lblTotalInvestmentAmount.setName("lblTotalInvestmentAmount");
        lblTotalPremiumPaid.setName("lblTotalPremiumPaid");
        lblTotalPremiumCollected.setName("lblTotalPremiumCollected");
        lblTotalInterestPaid.setName("lblTotalInterestPaid");
        lblTotalInterestCollected.setName("lblTotalInterestCollected");
        lblPurchaseDt.setName("lblPurchaseDt");
        rdoPrimary.setName("rdoPrimary");
        rdoSecondary.setName("rdoSecondary");
        rdoPurchase.setName("rdoPurchase");
        rdoSale.setName("rdoSale");
        rdoMarket.setName("rdoMarket");
        rdoOffMarket.setName("rdoOffMarket");
        lblBrokerName.setName("lblBrokerName");
        lblPurchaseRate.setName("lblPurchaseRate");
        lblNoOfUnits.setName("lblNoOfUnits");
        lblInvestmentAmount.setName("lblInvestmentAmount");
        lblNarration.setName("lblNarration");
        lblIPrematureROI.setName("lblIPrematureROI");
        lblPrematureIntAmt.setName("lblPrematureIntAmt");
        lblPremiumAmount.setName("lblPremiumAmount");
        lblTransTotalInvestmentAmount.setName("lblTransTotalInvestmentAmount");
        lblBrokenPeriodIntAmount.setName("lblBrokenPeriodIntAmount");
        lblBrokerCommission.setName("lblBrokerCommission");
        lblTotalAmount.setName("lblTotalAmount");
        lblCoupenRate.setName("lblCoupenRate");
        cboInvestmentBehaves.setName("cboInvestmentBehaves");
        txtInvestmentID.setName("txtInvestmentID");
        txtInvestmentName.setName("txtInvestmentName");
        tdtPurchaseDt.setName("tdtPurchaseDt");
        rdoPurchase.setName("rdoPurchase");
        rdoSale.setName("rdoSale");
        rdoPrimary.setName("rdoPrimary");
        rdoSecondary.setName("rdoSecondary");
        rdoMarket.setName("rdoMarket");
        rdoOffMarket.setName("rdoOffMarket");
        txtPurchaseRate.setName("txtPurchaseRate");
        txtNoOfUnits.setName("txtNoOfUnits");
        txtInvestmentAmount.setName("txtInvestmentAmount");
        txtNarration.setName("txtNarration");
        txtPrematureROI.setName("txtPrematureROI");
        txtPrematureIntAmt.setName("txtPrematureIntAmt");
        txtDiscount.setName("txtDiscount");
        txtPremiumAmount.setName("txtPremiumAmount");
        txtBrokenPeriodIntAmount.setName("txtPremiumAmount");
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
        lblInvestmentID.setText(resourceBundle.getString("lblInvestmentID"));
        lblInvestmentName.setText(resourceBundle.getString("lblInvestmentName"));
        lblInvestmentBehaves.setText(resourceBundle.getString("lblInvestmentBehaves"));
        lblIssueDate.setText(resourceBundle.getString("lblIssueDate"));
        lblFaceValue.setText(resourceBundle.getString("lblFaceValue"));
        lblDebitChequeNo.setText(resourceBundle.getString("lblDebitChequeNo"));
        lblLastIntPaidDate.setText(resourceBundle.getString("lblLastIntPaidDate"));
        lblInterestPaymentFrequency.setText(resourceBundle.getString("lblInterestPaymentFrequency"));
        lblPeriod.setText(resourceBundle.getString("lblPeriod"));
        lblCoupenRate.setText(resourceBundle.getString("lblCoupenRate"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblTotalInvestmentAmount.setText(resourceBundle.getString("lblTotalInvestmentAmount"));
        lblTotalPremiumPaid.setText(resourceBundle.getString("lblTotalPremiumPaid"));
        lblTotalPremiumCollected.setText(resourceBundle.getString("lblTotalPremiumCollected"));
        lblTotalInterestPaid.setText(resourceBundle.getString("lblTotalInterestPaid"));
        lblTotalInterestCollected.setText(resourceBundle.getString("lblTotalInterestCollected"));
        lblPurchaseDt.setText(resourceBundle.getString("lblPurchaseDt"));
        rdoPrimary.setText(resourceBundle.getString("rdoPrimary"));
        rdoSecondary.setText(resourceBundle.getString("rdoSecondary"));
        rdoPurchase.setText(resourceBundle.getString("rdoPurchase"));
        rdoSale.setText(resourceBundle.getString("rdoSale"));
        rdoMarket.setText(resourceBundle.getString("rdoMarket"));
        rdoOffMarket.setText(resourceBundle.getString("rdoOffMarket"));
        lblBrokerName.setText(resourceBundle.getString("lblBrokerName"));
        lblPurchaseRate.setText(resourceBundle.getString("lblPurchaseRate"));
        lblNoOfUnits.setText(resourceBundle.getString("lblNoOfUnits"));
        lblInvestmentAmount.setText(resourceBundle.getString("lblInvestmentAmount"));
        lblNarration.setText(resourceBundle.getString("lblNarration"));
        lblIPrematureROI.setText(resourceBundle.getString("lblIPrematureROI"));
        lblPrematureIntAmt.setText(resourceBundle.getString("lblPrematureIntAmt"));
        lblPremiumAmount.setText(resourceBundle.getString("lblPremiumAmount"));
        lblTransTotalInvestmentAmount.setText(resourceBundle.getString("lblTransTotalInvestmentAmount"));
        lblBrokenPeriodIntAmount.setText(resourceBundle.getString("lblBrokenPeriodIntAmount"));
        lblBrokerCommission.setText(resourceBundle.getString("lblBrokerCommission"));
        lblTotalAmount.setText(resourceBundle.getString("lblTotalAmount"));
        lblCoupenRate.setText(resourceBundle.getString("lblCoupenRate"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInvestmentBehaves", new Boolean(true));
        mandatoryMap.put("txtInvestmentID", new Boolean(true));
        mandatoryMap.put("txtInvestmentName", new Boolean(false));
        mandatoryMap.put("tdtPurchaseDt", new Boolean(true));
        mandatoryMap.put("txtPurchaseRate", new Boolean(false));
        mandatoryMap.put("txtNoOfUnits", new Boolean(false));
        mandatoryMap.put("lblFaceValueValue", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Creates the insstance of ShareProduct which acts as  Observable to
     *ShareProduct UI */
    private void setObservable() {
        try {
            observable = InvestmentsTransOB.getInstance();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try {
            cboInvestmentBehaves.setModel(observable.getCbmInvestmentBehaves());
            cboInvestmentBehavesTrans.setModel(observable.getCbmInvestmentBehavesTrans());
            cboInvestmentType.setModel(observable.getCbmInvestmentTypeSBorCA());

        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new InvestmentsTransMRB();
        cboInvestmentBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInvestmentBehaves"));
        txtInvestmentID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentID"));
        txtInvestmentName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentName"));
        tdtPurchaseDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPurchaseDt"));
        txtBrokerName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBrokerName"));
        txtPurchaseRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseRate"));
        txtNoOfUnits.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfUnits"));
        txtInvestmentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentAmount"));
        txtNarration.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNarration"));
        txtPrematureROI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrematureROI"));
        txtPrematureIntAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrematureIntAmt"));
        txtPremiumAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumAmount"));
        txtTotalInvestmentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalInvestmentAmount"));
        txtBrokenPeriodIntAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBrokenPeriodIntAmount"));
        txtBrokerCommission.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBrokerCommission"));
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtAccRefNo.setText(observable.getTxtAccRefNo());
        txtInternalAccNo.setText(observable.getTxtInternalAccNo());
        cboInvestmentType.setSelectedItem(observable.getCboInvestmentTypeSBorCA());
        txtInvestmentIDTransSBorCA.setText(observable.getTxtInvestmentIDTransSBorCA());
        txtInvestmentRefNoTrans.setText(observable.getTxtInvestmentRefNoTrans());
        txtInvestmentInternalNoTrans.setText(observable.getTxtInvestmentInternalNoTrans());
        txtChequeNo.setText(observable.getTxtChequeNo());
        if (observable.getTxtNarration1() != null) {
            txtNarration1.setText(observable.getTxtNarration1());
        }
        String transTypeSBorCA = CommonUtil.convertObjToStr(observable.getRdoSBorCA());
        if (transTypeSBorCA.length() > 0 && transTypeSBorCA.equals("Y")) {
            rdoSBorCA_yes.setSelected(true);
            rdoSBorCA_no.setSelected(false);
            panInvestSBorCATrans.setVisible(true);
        } else {
            panInvestSBorCATrans.setVisible(false);
            rdoSBorCA_yes.setSelected(false);
            rdoSBorCA_no.setSelected(true);
        }
        txtInvestmentName.setText(observable.getInvestmentName());
        tdtPurchaseDt.setDateValue(CommonUtil.convertObjToStr(observable.getPurchas_Date()));
        if (observable.getPurchase_Mode().equals("Primary")) {
            rdoPrimary.setSelected(true);
            rdoSecondary.setSelected(false);
            rdoMarket.setSelected(false);
            rdoOffMarket.setSelected(false);
        } else {
            rdoSecondary.setSelected(true);
            if (observable.getPurchse_Through().equals("Market")) {
                rdoMarket.setSelected(true);
                txtBrokerName.setText(observable.getBroker_Name());
            } else {
                rdoOffMarket.setSelected(true);
            }
        }
        txtPurchaseRate.setText(CommonUtil.convertObjToStr(observable.getPurchase_Rate()));
        txtNoOfUnits.setText(CommonUtil.convertObjToStr(observable.getNo_Of_Units()));  //No Of Shares

        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(observable.getInvestment_amount()));
        txtDiscount.setText(CommonUtil.convertObjToStr(observable.getDiscount_Amount()));

        txtPremiumAmount.setText(CommonUtil.convertObjToStr(observable.getPremium_Amount()));
        txtBrokenPeriodIntAmount.setText(CommonUtil.convertObjToStr(observable.getBroken_Period_Interest()));
        txtBrokerCommission.setText(CommonUtil.convertObjToStr(observable.getBroken_Commession()));// up to investment Trans Details
        lblIssueDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getIssueDt())));
        lblFaceValueValue.setText(CommonUtil.convertObjToStr(observable.getFaceValue()));
        //        lblAvailableNoOfUnitsValue.setText(CommonUtil.convertObjToStr(observable.getAvailableNoOfUnits()));
        lblInterestPaymentFrequencyValue.setText(CommonUtil.convertObjToStr(observable.getCboIntPayFreq()));
        lblCoupenValue.setText(CommonUtil.convertObjToStr(observable.getCouponRate()));
        lblMaturityDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getMaturityDate())));
        if (CommonUtil.convertObjToDouble(observable.getOutstandingAmount()).doubleValue() == 0) {
            lblTotalInvestmentAmountValue.setText("");
        } else {
            lblTotalInvestmentAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.getOutstandingAmount())));
        }
        lblTotalPremiumPaidValue.setText(CommonUtil.convertObjToStr(observable.getTotalPremiumPaid()));
        lblTotalPremiumCollectedValue.setText(CommonUtil.convertObjToStr(observable.getTotalPremiumCollected()));
        lblTotalInterestPaidValue.setText(CommonUtil.convertObjToStr(observable.getTotalInterestPaid()));
        lblTotalInterestCollectedValue.setText(CommonUtil.convertObjToStr(observable.getTotalInterestCollected()));
        lblLastIntPaidDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getLastIntPaidDate())));
        tdtUpToIntRecivedDt.setDateValue(CommonUtil.convertObjToStr(observable.getLastIntPaidDate()));
        lblMaturityAmtValue.setText(CommonUtil.convertObjToStr(observable.getMaturityAmount()));

        lblPeriodValue.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(observable.getYears()) + " " + "Years" + " " + CommonUtil.convertObjToInt(observable.getMonths()) + " " + "Months")
                + " " + CommonUtil.convertObjToInt(observable.getDays()) + " " + "Days");

        if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && (observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_SSD")
                || observable.callForBehaves().equals("OTHER_BANK_RD") || observable.callForBehaves().equals("OTHER_BANK_CA") || observable.callForBehaves().equals("OTHER_BANK_SPD")
                || observable.callForBehaves().equals("OTHER_BANK_SB") || observable.callForBehaves().equals("RESERVE_FUND_DCB")
                || CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).equals("OTHER_BANK_CDD"))) {

            if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Deposit")) {
                rdoPurchase.setSelected(true);
            }
            if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Withdrawal")) {
                rdoSale.setSelected(true);
            }
            if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Closure")) {
                rdoSale.setSelected(true);
            }
            if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Renewal")) {
                rdoCharges.setSelected(true);
            }
        } else {
            if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && (observable.callForBehaves().equals("SHARES_DCB") || observable.callForBehaves().equals("SHARE_OTHER_INSTITUTIONS"))) {
                if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Purchase")) {
                    rdoPurchase.setSelected(true);
                }
                if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Withdrawal")) {
                    rdoSale.setSelected(true);
                }
                if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Divident")) {
                    rdoInterest.setSelected(true);
                }
            }
        }
        if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Interest")) {
            rdoInterest.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Charges")) {
            rdoCharges.setSelected(true);
        }
        //        lblTotalInvestmentAmountValue.setText(CommonUtil.convertObjToStr(observable.getOutstandingAmount()));
        tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(observable.getUptoIntDate()));
        if (observable.getTran_Code().equals("Sale")) {
            rdoCloseYesOrNo.setVisible(true);
        }

        if (observable.getCloseStatus().equals("CLOSED")) {
            rdoCloseYesOrNo.setSelected(true);
        }
        tblAmortizationDetails.setModel(observable.getTblInvestmentTransDet());
        txtPurchaseorSalBy.setText(observable.getPurchaseSaleBy());
        if (observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_CCD")) {
            txtPreMatureCloserRate.setText(observable.getPurchaseSaleBy());
        }
        if ((observable.callForBehaves().equals("OTHER_BANK_CA") || observable.callForBehaves().equals("OTHER_BANK_SB") || observable.callForBehaves().equals("OTHER_BANK_SPD")) && rdoSale.isSelected() == true) {
            txtDebitChequeNo.setText(observable.getTxtChequeNo());
        }

        if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && rdoSale.isSelected() == true && (observable.callForBehaves().equals("OTHER_BANK_FD")
                || observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_SSD") || observable.callForBehaves().equals("OTHER_BANK_RD"))) {
            txtPrematureROI.setText(CommonUtil.convertObjToStr(observable.getTxtPrematureROI()));
            txtPrematureIntAmt.setText(CommonUtil.convertObjToStr(observable.getTxtPrematureIntAmt()));
        } else {
            txtPrematureROI.setText("");
            txtPrematureIntAmt.setText("");
        }
        tdtTransactionDt.setDateValue(DateUtil.getStringDate(observable.getTdtTransactionDt()));
        txtInvestTDS.setText(CommonUtil.convertObjToStr(observable.getTxtInvestTDS()));
    }

    private void setVisibleClosingDetails() {
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && rdoSale.isSelected() == true && (observable.callForBehaves().equals("OTHER_BANK_FD")
                || observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_RD") || observable.callForBehaves().equals("OTHER_BANK_SSD"))) {
            Date maturityDt = null;
            if (lblMaturityDateValue.getText().length() > 0) {
                setClosingDetailsVisible(true);
                maturityDt = DateUtil.getDateMMDDYYYY(lblMaturityDateValue.getText());
                if (DateUtil.dateDiff(curDate, maturityDt) > 0) {
                    lblCloserTypeValue.setText("Premature Closure");
                } else {
                    lblCloserTypeValue.setText("Normal Closure");
                    setClosingDetailsVisible(true);
                    lblIPrematureROI.setVisible(false);
                    txtPrematureROI.setVisible(false);
                    lblPrematureCalculateAmt.setVisible(false);
                    lblPrematureCalculateAmtVal.setVisible(false);
                    txtInvestmentAmount.setEnabled(false);
                    if (CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText()).doubleValue()
                            - CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue() > 0) {
                        lblPrematureIntAmt.setText("Interest Receivable Amount");
                        //                        txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText()).doubleValue()-
                        //                        CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()));
                        txtPrematureIntAmt.setEnabled(true);
                    } else {
                        //                        txtPrematureIntAmt.setText("0");
                    }
                }
            }
            if (observable.callForBehaves().equals("OTHER_BANK_FD")) {
                calcPrematureFixedInterest();
            } else if (observable.callForBehaves().equals("OTHER_BANK_RD")) {
                calcPrematureRecurringInterest();
            } else if (observable.callForBehaves().equals("OTHER_BANK_CCD")) {
                calcPrematureCummulativeInterest();
            }
            lblTotalAmountValue.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
            if (CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue() < CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()) {
                //                txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue() -
                //                CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue()));
                lblPrematureIntAmt.setText("Interest Payable Amount");
                observable.setInterestType("Interest Payable");
            } else {
                //                txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue() -
                //                CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()));
                lblPrematureIntAmt.setText("Interest Receivable Amount");
                observable.setInterestType("Interest Receivable");
            }
            if (CommonUtil.convertObjToStr(observable.getInterestType()).equals("Interest Payable")) {
                lblTotalAmountValue.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
            }
        } else {
            setClosingDetailsVisible(false);
        }
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
//        System.out.println("txtPrematureIntAmt@@@111@@@1111abcd>>>" + txtPrematureIntAmt.getText());
//        System.out.println("txtPrematureIntAmt@@@111@@@1111abcd>>>" + observable.getTxtPrematureIntAmt());
        observable.setTxtNarration1(txtNarration1.getText());
        //observable.setTxtCreditParticulars(txtCreditParticulars.getText());
        observable.setTxtInvestTDS(CommonUtil.convertObjToDouble(txtInvestTDS.getText()));
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setTxtAccRefNo(CommonUtil.convertObjToStr(txtAccRefNo.getText()));
        observable.setTxtInternalAccNo(CommonUtil.convertObjToStr(txtInternalAccNo.getText()));
        observable.setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboInvestmentType.getSelectedItem()));
        observable.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(txtInvestmentIDTransSBorCA.getText()));
        observable.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(txtInvestmentRefNoTrans.getText()));
        observable.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(txtInvestmentInternalNoTrans.getText()));
        observable.setTxtChequeNo(CommonUtil.convertObjToStr(txtChequeNo.getText()));
        if ((observable.callForBehaves().equals("OTHER_BANK_CA") || observable.callForBehaves().equals("OTHER_BANK_SB") || observable.callForBehaves().equals("OTHER_BANK_SPD")) && rdoSale.isSelected() == true) {
            observable.setTxtChequeNo(CommonUtil.convertObjToStr(txtDebitChequeNo.getText()));
        }
        observable.setTxtNarration(CommonUtil.convertObjToStr(txtNarration.getText()));
        if (rdoPurchase.isSelected() == true || rdoInterest.isSelected() == true || rdoCharges.isSelected() == true || rdoSale.isSelected() == true) {
            if (rdoSBorCA_yes.isSelected() == true) {
                observable.setRdoSBorCA(CommonUtil.convertObjToStr("Y"));
            }
        } else {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("N"));
        }

        if ((observable.callForBehaves().equals("OTHER_BANK_CA") || observable.callForBehaves().equals("OTHER_BANK_SB") || observable.callForBehaves().equals("OTHER_BANK_SPD")) && (rdoSale.isSelected() == true || rdoPurchase.isSelected() == true)) {
            if (rdoSBorCA_yes.isSelected() == true) {
                observable.setRdoSBorCA(CommonUtil.convertObjToStr("Y"));
            }
        } else {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("N"));
        }
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setPurchas_Date(DateUtil.getDateMMDDYYYY(tdtPurchaseDt.getDateValue()));
        if (rdoPrimary.isSelected() == true || rdoSecondary.isSelected() == true) {
            if (rdoPrimary.isSelected() == true) {
                observable.setPurchase_Mode("Primary");
            } else {
                observable.setPurchase_Mode("Secondary");
            }
        }
        if (rdoPurchase.isSelected() == true || rdoInterest.isSelected() == true || rdoCharges.isSelected() == true || rdoSale.isSelected() == true) {
            if (rdoSBorCA_yes.isSelected() == true) {
                observable.setRdoSBorCA(CommonUtil.convertObjToStr("Y"));
            }
        }
        if (rdoMarket.isSelected() == true || rdoOffMarket.isSelected() == true) {
            if (rdoMarket.isSelected() == true) {
                observable.setPurchase_Mode("Market");
            } else {
                observable.setPurchase_Mode("OffMarket");
                observable.setBroker_Name(CommonUtil.convertObjToStr(txtBrokerName.getText()));
            }

        }
        if (rdoPurchase.isSelected() == true || rdoSale.isSelected() == true || rdoInterest.isSelected() == true || rdoCharges.isSelected() == true) {

            if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && (observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_SSD")
                    || observable.callForBehaves().equals("OTHER_BANK_RD") || observable.callForBehaves().equals("OTHER_BANK_CA") || observable.callForBehaves().equals("OTHER_BANK_SPD")
                    || observable.callForBehaves().equals("OTHER_BANK_SB") || observable.callForBehaves().equals("RESERVE_FUND_DCB")
                    || CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).equals("OTHER_BANK_CDD"))) {
                if (rdoPurchase.isSelected() == true) {
                    observable.setTran_Code("Deposit");
                    observable.setTrans_type(CommonConstants.DEBIT);
                } else if (rdoSale.isSelected() == true) {
                    observable.setTran_Code("Withdrawal");
                    observable.setTrans_type(CommonConstants.CREDIT);
                } else if (rdoInterest.isSelected() == true) {
                    observable.setTran_Code("Interest");
                    observable.setTrans_type(CommonConstants.CREDIT);
                } else if (rdoCharges.isSelected() == true && observable.callForBehaves().equals("OTHER_BANK_CCD")) {
                    observable.setTran_Code("Renewal");
                    observable.setTrans_type(CommonConstants.CREDIT);
                } else if (rdoCharges.isSelected() == true) {
                    observable.setTran_Code("Charges");
                    observable.setTrans_type(CommonConstants.CREDIT);
                }
                if (rdoSale.isSelected() == true && (observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_CCD")
                        || observable.callForBehaves().equals("OTHER_BANK_SSD") || observable.callForBehaves().equals("OTHER_BANK_RD"))) {
                    observable.setTran_Code("Closure");
                    observable.setTrans_type(CommonConstants.CREDIT);
                }
            } else {
                if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && (observable.callForBehaves().equals("SHARES_DCB") || observable.callForBehaves().equals("SHARE_OTHER_INSTITUTIONS"))) {
                    if (rdoPurchase.isSelected() == true) {
                        observable.setTran_Code("Purchase");
                        observable.setTrans_type(CommonConstants.DEBIT);
                    } else if (rdoSale.isSelected() == true) {
                        observable.setTran_Code("Withdrawal");
                        observable.setTrans_type(CommonConstants.CREDIT);
                    } else if (rdoInterest.isSelected() == true) {
                        observable.setTran_Code("Divident");
                        observable.setTrans_type(CommonConstants.CREDIT);
                    }
                }
            }
        }
        observable.setPurchase_Rate(CommonUtil.convertObjToDouble(txtPurchaseRate.getText()));
        observable.setNo_Of_Units(CommonUtil.convertObjToDouble(txtNoOfUnits.getText()));   //No of Shares
        observable.setInvestment_amount(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()));//changed investmentAmt totransAmt
        observable.setDiscount_Amount(CommonUtil.convertObjToDouble(txtDiscount.getText()));
        observable.setPremium_Amount(CommonUtil.convertObjToDouble(txtPremiumAmount.getText()));
        observable.setBroken_Period_Interest(CommonUtil.convertObjToDouble(txtBrokenPeriodIntAmount.getText()));
        observable.setBroken_Commession(CommonUtil.convertObjToDouble(txtBrokerCommission.getText()));
        observable.setTrans_Dt((Date) curDate.clone());
        observable.setUptoIntDate(DateUtil.getDateMMDDYYYY(tdtUpToIntRecivedDt.getDateValue()));
        observable.setPurchaseSaleBy(txtPurchaseorSalBy.getText());
        if (observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_CCD")) {
            observable.setPurchaseSaleBy(txtPreMatureCloserRate.getText());
        }
        if (rdoCloseYesOrNo.isSelected() == true) {
            observable.setCloseStatus("CLOSED");
        }

        if (txtPrematureIntAmt.getText().equals(0.00) || txtPrematureIntAmt.getText().equals("")) {
            //System.out.println("lllajjff");
            observable.setTxtPrematureIntAmt(0.00);
        }
        //System.out.println("txtPrematureIntAmt.getText()>>" + txtPrematureIntAmt.getText() + "observable.getTxtPrematureIntAmt()>>>" + observable.getTxtPrematureIntAmt());
        if ((CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) && rdoSale.isSelected() == true && (observable.callForBehaves().equals("OTHER_BANK_FD")
                || observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_SSD") || observable.callForBehaves().equals("OTHER_BANK_RD"))) {
            observable.setTxtPrematureROI(CommonUtil.convertObjToDouble(txtPrematureROI.getText()));
          //  System.out.println("prematureint1111111");
            observable.setTxtPrematureIntAmt(CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()));
            txtPrematureROI.setText(CommonUtil.convertObjToStr(observable.getTxtPrematureROI()));
            txtPrematureIntAmt.setText(CommonUtil.convertObjToStr(observable.getTxtPrematureIntAmt()));
        } else {
            observable.setTxtPrematureROI(0.0);
            observable.setTxtPrematureIntAmt(0.00);
        }

        if (txtPrematureIntAmt.getText().equals(0.00) || txtPrematureIntAmt.getText().equals("")) {
            observable.setTxtPrematureIntAmt(0.00);
        }
         //Added by sreekrishnan        
        if ((rdoSale.isSelected() == true) && CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText())-CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText())>0) {
            observable.setCloseWithInterest(true);
            observable.setBroken_Period_Interest(CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText())-CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()));
        }//else{
          //  observable.setCloseWithInterest(false);
          //  observable.setBroken_Period_Interest(0.0);
        //}
        if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
            observable.setTdtTransactionDt(getProperDateFormat(tdtTransactionDt.getDateValue()));
        }
        observable.setScreen(this.getScreen());
//        System.out.println("brokenIn"+observable.getBroken_Period_Interest());
//        System.out.println("txtPrematureIntAmt@@@111@@@2222abcd>>>" + txtPrematureIntAmt.getText());
//        System.out.println("txtPrematureIntAmt@@@111@@@2222abcd>>>" + observable.getTxtPrematureIntAmt());

    }

    /**
     * Enabling and Disabling of Buttons after Save,Edit,Delete operations
     */
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

    /**
     * Setting up Lengths for the TextFields in then UI
     */
    private void setMaxLength() {
        txtPrematureROI.setValidation(new NumericValidation(3, 3));
        txtInvestmentName.setMaxLength(50);
        txtBrokerName.setMaxLength(16);
        txtPurchaseRate.setValidation(new CurrencyValidation(16, 2));
        txtPremiumAmount.setValidation(new CurrencyValidation(16, 2));
        txtNoOfUnits.setValidation(new NumericValidation(16, 0));
        txtInvestmentAmount.setValidation(new CurrencyValidation(16, 2));
        txtTotalInvestmentAmount.setValidation(new CurrencyValidation(16, 2));
        txtBrokenPeriodIntAmount.setValidation(new CurrencyValidation(16, 2));
        txtBrokerCommission.setValidation(new CurrencyValidation(16, 2));
        txtPrematureIntAmt.setValidation(new CurrencyValidation(16, 2));
        txtDiscount.setValidation(new CurrencyValidation(16, 2));
        txtPreMatureCloserRate.setValidation(new NumericValidation(3, 2));
        txtAccRefNo.setAllowAll(true);
        txtChequeNo.setAllowAll(true);
        txtDebitChequeNo.setAllowAll(true);
        txtTransAmt.setAllowNumber(true);
        txtInvestTDS.setValidation(new CurrencyValidation(16, 2));
    }

    /**
     * Making the btnShareAccount enable or disable according to the actiontype *
     */
    private void setHelpButtonEnableDisable(boolean enable) {

        cboInvestmentBehaves.setEnabled(enable);
        //        cboInvestmentBehaves.setEditable(enable);


    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) curDate.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    /* Does necessary operaion when user clicks the save button */
    private void savePerformed() {
        updateOBFields();
        double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
        double trn_amt = CommonUtil.convertObjToDouble(lblTotalAmountValue.getText()).doubleValue();
        double investAmt = CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue();
        double outAmt = CommonUtil.convertObjToDouble(lblTotalInvestmentAmountValue.getText()).doubleValue();
        String behavs = observable.callForBehaves();
        System.out.println("behavs--------------------->" + behavs);
        if (rdoPurchase.isSelected() == false && rdoSale.isSelected() == false && rdoInterest.isSelected() == false && rdoCharges.isSelected() == false) {
            ClientUtil.displayAlert("Please Select  Puchase or Sale");
        } else if (rdoPrimary.isSelected() == false && rdoSecondary.isSelected() == false && rdoInterest.isSelected() == false && behavs.equals("BONDS")) {
            ClientUtil.displayAlert("Please Select  Primary  or Secondary");
        } else if (rdoSale.isSelected() == true && rdoCloseYesOrNo.isSelected() == true && investAmt < outAmt) {
            ClientUtil.displayAlert("Please Un Select  Close");

        } //        else if(rdoSBorCA_yes.isSelected()==true && rdoPurchase.isSelected()==true && txtChequeNo.getText().length()<=0){
        //            ClientUtil.displayAlert("Please Enter Cheque No !!! ");
        //        }
        else if (rdoSecondary.isSelected() == true && (rdoMarket.isSelected() == false && rdoOffMarket.isSelected() == false)) {
            ClientUtil.displayAlert("Please Select  Market  or OffMarket");
        } else if (rdoMarket.isSelected() == true && CommonUtil.convertObjToStr(txtBrokerName.getText()).length() <= 0
                && CommonUtil.convertObjToStr(txtBrokerName.getText()).equals("")) {
            ClientUtil.displayAlert("Please Select  Enter the broker Name");
        } else if ((transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)
                && ((!behavs.equals("OTHER_BANK_CCD") || !behavs.equals("OTHER_BANK_RD") || !behavs.equals("OTHER_BANK_SB") || !behavs.equals("OTHER_BANK_CA") || !behavs.equals("OTHER_BANK_SPD")) && rdoInterest.isSelected() == false)
                && ((!behavs.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true))) {
            ClientUtil.displayAlert("Transaction Records Not Available");
        } else if (trn_amt != transTotalAmt && (((!behavs.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true)))
                && ((!behavs.equals("OTHER_BANK_CCD") || !behavs.equals("OTHER_BANK_RD") || !behavs.equals("OTHER_BANK_SB") || !behavs.equals("OTHER_BANK_CA") || !behavs.equals("OTHER_BANK_SPD")) && rdoInterest.isSelected() == false)) {
            ClientUtil.displayAlert("Transaction Amount Not Tailed");
        } else {
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            String action;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                action = CommonConstants.TOSTATUS_INSERT;
                saveAction(action);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                action = CommonConstants.TOSTATUS_UPDATE;
                saveAction(action);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                action = CommonConstants.TOSTATUS_DELETE;
                saveAction(action);
            }
        }
    }

    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try {
            final String mandatoryMessage = checkMandatory(panInvestmentTrans);
            String alertMsg = "";
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                observable.setExcessOrShort(new Double(0.0));
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && rdoSale.isSelected() == true && rdoCloseYesOrNo.isSelected() == true) {
                }
                if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
                    int count = 0;
                    if((count = tblMultiTransTable.getRowCount())>0){
                        for(int i = 0;i<count;i++){
                            String acno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(i, 2));
                            InvestmentsTransTO obj = (InvestmentsTransTO) finalMap.get(acno);
                            System.out.println("obj^#^#^#^^#^^#"+obj);
                            obj.setTransDT(observable.getTdtTransactionDt());
                            finalMap.put(acno, obj);
                        }
                    }
                }
                System.out.println("finalMap^##^#^#^#^"+finalMap);
                observable.setFinalMap(finalMap);
                observable.execute(status);
                rdoCloseYesOrNo.setSelected(false);
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() != null) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                    setButtonEnableDisable();
                    observable.resetForm();
                    rdoSelectectedTrueOrFalse();
                    resetTransactionUI();
                    lblPeriodValue.setText("");
                    ClientUtil.clearAll(panInvestmentTrans);
                    changeFieldNamesAccordingTOBehavesAndRdo();
                    ClientUtil.enableDisable(panInvestmentTrans, false);
                    btnCancelActionPerformed(null);
                }

                //__ if the Action is not Falied, Reset the fields...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
                printParamMap.put("TransDt", DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()));
            }else{
                printParamMap.put("TransDt", curDate.clone());
            }
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }

    /**
     * This will checks whether the Mandatory fields in the UI are filled up, If
     * not filled up it will retun an MandatoryMessage
     */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
    }

    /**
     * This will Display the Mandatory Message in a Dialog Box
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /* set the screen after the updation,insertion, deletion */
    private void settings() {
        observable.resetForm();
        rdoSelectectedTrueOrFalse();
        resetTransactionUI();
        lblPeriodValue.setText("");
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        observable.setResultStatus();
    }

    /**
     * This will show a popup screen which shows all tbe Rows.of the table
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])
                || currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            ArrayList lst = new ArrayList();
            lst.add("INVESTMENT_NAME");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentTrans");
        } else if (viewType.equals("InvestmentProduct") || viewType.equals("InvestmentProductTrans") || viewType.equals("InvestmentProductSBorCATrans")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
            String bheaves = "";
            if (viewType.equals("InvestmentProduct")) {
                observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
                bheaves = observable.callForBehaves();
            } else if (viewType.equals("InvestmentProductTrans")) {
                observable.setCboInvestmentBehavesTrans(CommonUtil.convertObjToStr(cboInvestmentBehavesTrans.getSelectedItem()));
                bheaves = observable.callForBehavesTrans();
            } else if (viewType.equals("InvestmentProductSBorCATrans")) {
                observable.setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboInvestmentType.getSelectedItem()));
                bheaves = observable.callForBehavesSBorCATrans();
            }
            HashMap whereMap = new HashMap();
            whereMap.put("INVESTMENT_TYPE", bheaves);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("InvestmentName") || viewType.equals("InvestmentNameTrans")) {
            String bheaves = "";
            HashMap whereMap = new HashMap();
            if (viewType.equals("InvestmentName")) {
                observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
                bheaves = observable.callForBehaves();
                whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(txtInvestmentID.getText()));
                whereMap.put("INVESTMENT_STATUS", "INVESTMENT_STATUS");
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_SSD")) {

                    if (rdoSale.isSelected() == true || rdoInterest.isSelected() == true) {
                        whereMap.put("TRANSACTIONMADE", "TRANSACTIONMADE");
                    }
                    if (rdoPurchase.isSelected() == true) {
                        whereMap.put("NONTRANSACTIONMADE", "NONTRANSACTIONMADE");
                    }
                }
            } else if (viewType.equals("InvestmentNameTrans")) {
                observable.setCboInvestmentBehavesTrans(CommonUtil.convertObjToStr(cboInvestmentBehavesTrans.getSelectedItem()));
                bheaves = observable.callForBehavesTrans();
                whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(txtInvestmentIDTrans.getText()));
            }
            whereMap.put("INVESTMENT_TYPE", bheaves);
            whereMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentMaster");
            whereMap = null;
        } else if (viewType.equals("InvestmentAccNo")) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtInvestmentID.getText());
             if (rdoSale.isSelected() == true && rdoSale.getText().equals("Closure"))
                 whereMap.put("CLOSURE", "Y");
             //else
            //    whereMap.put("CLOSURE", "N"); 
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
            String bheaves = "";
            bheaves = observable.callForBehaves();
            if (bheaves.length() > 0) {
                if (bheaves.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestRenewalDepAccNos");
                } else if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransDepAccNos");
                } else if (bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransOperativeAccNos");
                } else if (bheaves.equals("RESERVE_FUND_DCB")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransReserveFundAccNos");
                } else if (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransShareAccNos");
                }
            }
        } else if (viewType.equals("InvestmentRefNoTrans")) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtInvestmentIDTransSBorCA.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getInvestTransOtherBankOpperativeAccNos");
        } else if (viewType.equals("InvestmentTranAccNo")) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtInvestmentIDTrans.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehavesTrans.getSelectedItem()));
            String bheaves = "";
            bheaves = observable.callForBehavesTrans();
            if (bheaves.length() > 0) {
                if (bheaves.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestRenewalDepAccNos");
                } else if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransDepAccNos");
                } else if (bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransOperativeAccNos");
                } else if (bheaves.equals("RESERVE_FUND_DCB")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransReserveFundAccNos");
                } else if (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                    viewMap.put(CommonConstants.MAP_NAME, "getInvestTransShareAccNos");
                }
            }
        }
        new ViewAll(this, viewMap).show();
    }

    private void btnSaveDisable() {
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
        btnCancel.setEnabled(true);
        mitCancel.setEnabled(true);
    }

    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object map) {
        setModified(true);
        HashMap hash1 = (HashMap) map;
        System.out.println("hashhashhashhashhash" + hash1);
         if (hash1.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            btnSaveDisable();
        }
        if (hash1.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            btnSaveDisable();
        }
        if (hash1.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            btnSaveDisable();
        }
        if (hash1.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash1.get("PARENT");
            hash1.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            btnSaveDisable();
        }

        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                    || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                String investMentType = "";
                investMentType = CommonUtil.convertObjToStr(hash.get("INVESTMENT_TYPE"));
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_NAME"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("BATCH_ID", hash.get("BATCH_ID"));
                System.out.println("#@##@#@#@#@#@#@#@#@#@  hash : "+hash);
//                if (hash.containsKey("TRANS_DT")) {
//                    hash.put("TRANS_DT", hash.get("TRANS_DT"));
//                } else {
//                    hash.put("TRANS_DT", ClientUtil.getCurrentDate());
//                }
                hash.put("TRANS_DT", hash.get("TRANS_DT"));
                observable.populateData(hash);
                tblMultiTransTable.setModel(observable.getTblCheckBookTable());
                panCheckBookBtn.setEnabled(false);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                        || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panInvestmentTrans, false);
                    setHelpButtonEnableDisable(false);
                    //ClientUtil.enableDisable(panMutipleTrans, false);

                } else {
                    ClientUtil.enableDisable(panInvestmentTrans, true);
                    setHelpButtonEnableDisable(true);
                }
                setButtonEnableDisable();
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                }
                //                checkPrematureCloser();
                HashMap whereMap = new HashMap();
                whereMap.put("INVESTMENT_ID", CommonUtil.convertObjToStr(txtInternalAccNo.getText()));

                if (investMentType.equals("OTHER_BANK_FD") || investMentType.equals("OTHER_BANK_CCD") || investMentType.equals("OTHER_BANK_RD") || investMentType.equals("OTHER_BANK_SSD")) {
                    depositDetails(whereMap);
                    setVisibleClosingDetails();
                } else if (investMentType.equals("OTHER_BANK_SB") || investMentType.equals("OTHER_BANK_CA") || investMentType.equals("OTHER_BANK_SPD")) {
                    operativeDetails(whereMap);
                } else if (investMentType.equals("RESERVE_FUND_DCB")) {
                    reserveFundDetails(whereMap);
                } else if (investMentType.equals("SHARES_DCB") || investMentType.equals("SHARE_OTHER_INSTITUTIONS")) {
                    shareDetails(whereMap);
                }
                HashMap transTypeMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                int cashCount = 0;
                int transferCount = 0;
                String transId = "";
                String displayStr = "";
                HashMap transTypMap = new HashMap();
                String actNum = "";
                String cashDisplayStr = "Cash Transaction Details...\n";
                String transferDisplayStr = "Transfer Transaction Details...\n";
                transCashMap.put("BATCH_ID", CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
                transCashMap.put("singleTrasnId", CommonUtil.convertObjToStr(hash.get("SINGLE_TRANS_ID")));
                transCashMap.put("TRANS_DT", curDate.clone());
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                HashMap transIdMap = new HashMap();
                List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        if (i == 0) {
                            transId = (String) transMap.get("BATCH_ID");
                        }
                        transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                + "   Batch Id : " + transMap.get("BATCH_ID")
                                + "   Trans Type : " + transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if (actNum != null && !actNum.equals("")) {
                            transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                    }
                    transferCount++;
                }
                List list1 = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (list1 != null && list1.size() > 0) {
                    for (int i = 0; i < list1.size(); i++) {
                        transMap = (HashMap) list1.get(i);
                        if (i == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
                        }
                        cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                + "   Trans Type : " + transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if (actNum != null && !actNum.equals("")) {
                            cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        transTypMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                    }
                    cashCount++;
                }
                if (cashCount > 0) {
                    displayStr += cashDisplayStr;
                }
                if (transferCount > 0) {
                    displayStr += transferDisplayStr;
                }
                if (!displayStr.equals("")) {
                    ClientUtil.showMessageWindow("" + displayStr);
                }
                int yesNo = 0;
                String[] voucherOptions = {"Yes", "No"};
                if ((list != null && list.size() > 0) ||(list1 != null && list1.size() > 0)) {
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, voucherOptions, voucherOptions[0]);
                    if (yesNo == 0) {
                        com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("TransDt", ClientUtil.getCurrentDate());
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
                transCashMap.clear();
                transCashMap = null;
                transIdMap = null;
                transTypeMap = null;
            } else if (viewType.equals("InvestmentProduct")) {
                txtInvestmentID.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtInvestmentName.setText(CommonUtil.convertObjToStr(hash.get("IINVESTMENT_PROD_DESC")));
                txtInvestmentName.setEnabled(false);
                btnAccRefNo.setEnabled(true);
                btnInvestmentID.setEnabled(true);
                String bheaves = observable.callForBehaves();
                if ((bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) && rdoCharges.isSelected() == true) {
                    setTransChargeDetails();
                } else {
                    transactionUI.setSourceScreen("INVESTMENT_TRANS");
                }
            } else if (viewType.equals("InvestmentProductSBorCATrans")) {
                txtInvestmentIDTransSBorCA.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtInvestmentRefNoTrans.setText("");
                txtInvestmentInternalNoTrans.setText("");
                txtChequeNo.setText("");
                setTransOtherBankDetails();
            } else if (viewType.equals("InvestmentTranAccNo")) {
                txtTranAccRefNo.setText(CommonUtil.convertObjToStr(hash.get("INVES_REF_NO")));
                txtTranInternalAccNo.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
                txtTranInternalAccNo.setEnabled(false);

            } else if (viewType.equals("InvestmentAccNo")) {
                txtAccRefNo.setText(CommonUtil.convertObjToStr(hash.get("INVES_REF_NO")));
                lblIssueDateValue.setText(CommonUtil.convertObjToStr(hash.get("ISSUE_DT")));
                if (hash.containsKey("AMOUNT") && rdoPurchase.isSelected() == true) {
                    lblFaceValueValue.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    txtInvestmentAmount.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    lblTotalAmountValue.setText(CommonUtil.convertObjToStr(txtInvestmentAmount.getText()));
                }
                txtInternalAccNo.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
                txtInternalAccNo.setEnabled(false);
                String bheaves = observable.callForBehaves();
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                    depositDetails(hash);
                } else if (bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) {
                    operativeDetails(hash);
                } else if (bheaves.equals("RESERVE_FUND_DCB")) {
                    reserveFundDetails(hash);
                } else if (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                    shareDetails(hash);
                }

                //FD_PERIODIC_INTEREST
                if (rdoInterest.isSelected() == true && bheaves.equals("OTHER_BANK_FD")) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
                    List depIntList = ClientUtil.executeQuery("getPeriodicInterestAmt", whereMap);
                    if (depIntList != null && depIntList.size() > 0) {
                        whereMap = (HashMap) depIntList.get(0);
                        txtBrokenPeriodIntAmount.setText(CommonUtil.convertObjToStr(whereMap.get("PERIODIC_INTEREST")));
                        lblLastIntPaidDateValue.setText(CommonUtil.convertObjToStr(whereMap.get("INT_REC_TILL_DT")));
                        lblFaceValueValue.setText(CommonUtil.convertObjToStr(whereMap.get("PRINCIPAL_AMOUNT")));
                        txtBrokenPeriodIntAmountFocusLost(null);
                    }
                }
                if (rdoSale.isSelected() == true && (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_SSD") || bheaves.equals("OTHER_BANK_CCD"))) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
                    List balanceList = ClientUtil.executeQuery("getAvailableBalanceFromMaster", whereMap);
                    if (balanceList != null && balanceList.size() > 0) {
                        whereMap = (HashMap) balanceList.get(0);
                        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(whereMap.get("AVAILABLE_BALANCE")));
                        txtInvestmentAmountFocusLost(null);
                    }
                }
                //Set Transaction Amount
                SetTransAmount();
                lblCloserTypeValue.setText("");
                //Before Closure Check Interest Amount
                if (rdoSale.isSelected() == true && (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD")
                        || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD"))) {
                    Date maturityDt = null;
                    if (lblMaturityDateValue.getText().length() > 0) {
                        setClosingDetailsVisible(true);
                        maturityDt = DateUtil.getDateMMDDYYYY(lblMaturityDateValue.getText());
                        if (DateUtil.dateDiff(curDate, maturityDt) > 0) {
                            //                            ClientUtil.showMessageWindow("Whether this is the Premature Closure !!! ");
                            lblCloserTypeValue.setText("Premature Closure");
                            observable.setClosingType("Premature Closure");
                            txtTransAmt.setText(lblFaceValueValue.getText());
//                            txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText()).doubleValue()-
//                                CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()));
//                            observable.setInterestType("Interest Receivable");
//                            System.out.println("txtPrematureIntAmt4344>>>>"+txtPrematureIntAmt.getText());
                        } else {
                            lblCloserTypeValue.setText("Normal Closure");
                            observable.setClosingType("Normal Closure");
                            observable.setInterestType("Interest Receivable");
                            txtPrematureIntAmt.setEnabled(true);
                            txtTransAmt.setText(lblFaceValueValue.getText());
                            //Set Interest Receivable Amount
                            setClosingDetailsVisible(true);
                            lblIPrematureROI.setVisible(false);
                            txtPrematureROI.setVisible(false);
                            lblPrematureCalculateAmt.setVisible(false);
                            lblPrematureCalculateAmtVal.setVisible(false);
                            txtInvestmentAmount.setEnabled(false);
                            if (CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText()).doubleValue()
                                    - CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue() > 0) {
                                lblPrematureIntAmt.setText("Interest Receivable Amount");
                                txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblMaturityAmtValue.getText()).doubleValue()
                                        - CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()));
                                txtPrematureIntAmtFocusLost(null);
                            } else {
                                txtPrematureIntAmt.setText("0.00");
                            }
                        }
                        //                            HashMap whereMap = new HashMap();
                        //                            whereMap.put("INVESTMENT_ID",txtInternalAccNo.getText());
                        //                            List depIntList = ClientUtil.executeQuery("checkInterestReceivabeAndReceived", whereMap);
                        //                            if(depIntList!=null && depIntList.size()>0){
                        //                                ClientUtil.showMessageWindow("Interest Fully not Received... Put through Interest Transaction, Then Go for Closure");
                        //                                btnCancelActionPerformed(null);
                        //                                setClosingDetailsVisible(false);
                        //                                lblCloserTypeValue.setText("");
                        //                            }
                    }
                } else {
                    setClosingDetailsVisible(false);
                }
                //Renewal Details
                if (rdoCharges.isSelected() == true && bheaves.equals("OTHER_BANK_CCD")) {
                    HashMap whereMap = new HashMap();
                    HashMap renewalMap = new HashMap();
                    whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
                    List renewalList = ClientUtil.executeQuery("gerInvestmentRenewalIntWithPrincipal", whereMap);
                    if (renewalList != null && renewalList.size() > 0) {
                        intWithPrincipal = "";
                        whereMap = (HashMap) renewalList.get(0);
                        intWithPrincipal = CommonUtil.convertObjToStr(whereMap.get("INT_WITH_PRINCIPAL"));
                        System.out.println("####intWithPrincipal : " + intWithPrincipal);
                        whereMap = new HashMap();
                        whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
                        List depList = ClientUtil.executeQuery("gerInvestmentWithOrWithoutInterest", whereMap);
                        if (depList != null && depList.size() > 0) {
                            withOrWithoutInterest = "";
                            whereMap = (HashMap) depList.get(0);
                            withOrWithoutInterest = CommonUtil.convertObjToStr(whereMap.get("WITH_INTEREST"));
                            System.out.println("####withOrWithoutInterest : " + withOrWithoutInterest);
                            whereMap = new HashMap();
                            double intReceivable = 0.0;
                            whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
                            List intReceivableList = ClientUtil.executeQuery("gerInvestmentRenewalInterestReceivable", whereMap);
                            if (intReceivableList != null && intReceivableList.size() > 0) {
                                whereMap = (HashMap) intReceivableList.get(0);
                                txtBrokenPeriodIntAmount.setText(CommonUtil.convertObjToStr(whereMap.get("INTEREST_RECEIVABLE")));
                                txtBrokenPeriodIntAmountFocusLost(null);
                                rdoSBorCA_noActionPerformed(null);
                            }
                            if (intWithPrincipal.length() > 0 && withOrWithoutInterest.length() > 0) {
                                if (intWithPrincipal.equals("Y") && withOrWithoutInterest.equals("Y")) {
                                    ClientUtil.showMessageWindow("No Transaction Of this Renewal Account !!!");
                                    btnCancelActionPerformed(null);
                                } else if (intWithPrincipal.equals("N") && withOrWithoutInterest.equals("Y")) {
                                    renewalMap = new HashMap();
                                    renewalMap.put("INT_WITH_PRINCIPAL", intWithPrincipal);
                                    renewalMap.put("WITH_INTEREST", withOrWithoutInterest);
                                    observable.setRenewalMap(renewalMap);
                                } else {
                                    renewalMap = new HashMap();
                                    renewalMap.put("INT_WITH_PRINCIPAL", intWithPrincipal);
                                    renewalMap.put("WITH_INTEREST", withOrWithoutInterest);
                                    observable.setRenewalMap(renewalMap);
                                }
                                btnSave.setEnabled(true);
                            } else {
                                tabClosingType.remove(panTransaction);
                                btnSave.setEnabled(false);
                            }
                        }
                    }
                }

                if (rdoInterest.isSelected() == true && (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS"))) {
                    rdoSBorCA_noActionPerformed(null);
                }
                tabClosingType.setSelectedIndex(0);
                ClientUtil.enableDisable(panTransAdd, true);                
            } else if (viewType.equals("InvestmentRefNoTrans")) {
                txtInvestmentRefNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVES_REF_NO")));
                txtInvestmentInternalNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
            } else if (viewType.equals("InvestmentName")) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_NAME"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("MASTER", "MASTER");
                observable.populateData(hash);
                rdoSelectectedTrueOrFalse();
                resetTransactionUI();
            } else if (viewType.equals("InvestmentProductTrans")) {
                txtInvestmentIDTrans.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtInvestmentNameTrans.setText(CommonUtil.convertObjToStr(hash.get("IINVESTMENT_PROD_DESC")));
            }
//            else if(viewType.equals("InvestmentNameTrans")){
//                txtInvestmentNameTrans.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_NAME")));
//            }
        }
        if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            ClientUtil.enableDisable(panInvestmentTrans, false);
            setHelpButtonEnableDisable(false);
            btnInvestmentID.setEnabled(false);
        }
        changeFieldNamesAccordingTOBehavesAndRdo();
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        System.out.println("txtInvestmentAmount???");
        txtInvestmentAmount.setEnabled(false);
    }
   private void setClosingDetailsVisible(boolean flag) {
        lblCloserType.setVisible(flag);
        lblCloserTypeValue.setVisible(flag);
        lblIPrematureROI.setVisible(flag);
        txtPrematureROI.setVisible(flag);
        lblPrematureIntAmt.setVisible(flag);
        txtPrematureIntAmt.setVisible(flag);
        lblPrematureCalculateAmt.setVisible(flag);
        lblPrematureCalculateAmtVal.setVisible(flag);
    }

    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    /**
     * This will do necessary operation for authorization *
     */
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();

            System.out.println("curDate>>>>" + curDate.clone());
            HashMap whereMap = new HashMap();
            whereMap.put("TRANS_DT", curDate.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            //  HashMap whereMap=new HashMap();
            whereMap.put("TRANS_DT", curDate.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getInvestmentTransCashierAuthorizeList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getInvestmentTransAuthorizeList");
            }



            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            String isAllTabsVisited = "";
            if (isAllTabsVisited.length() > 0) {
            } else {
                HashMap singleAuthorizeMap = new HashMap();
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authDataMap.put("BATCH_ID", observable.getBatch_Id());
                authDataMap.put("INVESTMENT_NAME", observable.getInvestmentName());
                authDataMap.put("INVESTMENT_ID", observable.getInvestmentID());
                System.out.println("authDataMap : " + authDataMap);
                arrList.add(authDataMap);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                //                observable.setExcessOrShort(new Double(callPremiumExcessOrShort()));

                if (observable.getExcessOrShort() != null && observable.getExcessOrShort().doubleValue() != 0.0 && observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && rdoCloseYesOrNo.isSelected() == true) {
                    singleAuthorizeMap.put("EXCESS_SHORT", String.valueOf(observable.getExcessOrShort()));
                }
                authorize(singleAuthorizeMap);
                viewType = ClientConstants.VIEW_TYPE_CANCEL;
                changeFieldNamesAccordingTOBehavesAndRdo();
                ClientUtil.enableDisable(panTransaction, false);

            }
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }

    public void authorize(HashMap map) {
        String txtBehaves = "";
        txtBehaves = observable.callForBehaves();
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        map.put("InvestmentsTransTO", observable.getInvestmentsTransTO(CommonConstants.STATUS_AUTHORIZED));
        if (rdoSale.isSelected() == true && (txtBehaves.equals("SHARES_DCB") || txtBehaves.equals("SHARE_OTHER_INSTITUTIONS"))) {
            double availableBalance = 0.0;
            HashMap whereMap = new HashMap();
            whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            List masterLst = ClientUtil.executeQuery("getAvailableBalanceFromMaster", whereMap);
            if (masterLst != null && masterLst.size() > 0) {
                whereMap = (HashMap) masterLst.get(0);
                availableBalance = CommonUtil.convertObjToDouble(whereMap.get("AVAILABLE_BALANCE")).doubleValue();
                if (availableBalance == CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue()) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want Close The Account !", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        map.put("CLOSED_STATUS", "YES");
                    } else {
                        map.put("CLOSED_STATUS", "NO");
                    }
                }

            }
        }
        observable.setAuthorizeMap(map);
        try {
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.execute("AUTHORIZE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            observable.setResultStatus();
            setModified(false);
            super.removeEditLock(((ComboBoxModel) cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
            observable.resetForm();
            rdoSelectectedTrueOrFalse();
            resetTransactionUI();
            lblPeriodValue.setText("");
            ClientUtil.clearAll(this);
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            ClientUtil.enableDisable(panInvestmentTrans, false);
            setButtonEnableDisable();
            setHelpButtonEnableDisable(false);
            viewType = "";
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);

        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
             if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Investment Trans");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Investment Trans");
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
        }
    }

    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        InvestmentsMasterUI ui = new InvestmentsMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600, 600);
        frame.show();
        ui.show();


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgSBorCA = new com.see.truetransact.uicomponent.CButtonGroup();
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
        tabClosingType1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panTransaction1 = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentTrans = new com.see.truetransact.uicomponent.CPanel();
        tabClosingType = new com.see.truetransact.uicomponent.CTabbedPane();
        panClosingPosition = new com.see.truetransact.uicomponent.CPanel();
        lblPurchaseDt = new com.see.truetransact.uicomponent.CLabel();
        tdtPurchaseDt = new com.see.truetransact.uicomponent.CDateField();
        panPurchaseThrough = new com.see.truetransact.uicomponent.CPanel();
        lblTotNoOfSharesCount2 = new com.see.truetransact.uicomponent.CLabel();
        rdoMarket = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOffMarket = new com.see.truetransact.uicomponent.CRadioButton();
        panPurchaseMode = new com.see.truetransact.uicomponent.CPanel();
        lblTotNoOfSharesCount3 = new com.see.truetransact.uicomponent.CLabel();
        rdoPrimary = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSecondary = new com.see.truetransact.uicomponent.CRadioButton();
        lblBrokerName = new com.see.truetransact.uicomponent.CLabel();
        txtBrokerName = new com.see.truetransact.uicomponent.CTextField();
        lblPreMatureCloserRate = new com.see.truetransact.uicomponent.CLabel();
        txtPreMatureCloserRate = new com.see.truetransact.uicomponent.CTextField();
        panInvestmentSBorCATrans = new com.see.truetransact.uicomponent.CPanel();
        panSBorCA = new com.see.truetransact.uicomponent.CPanel();
        rdoSBorCA_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSBorCA_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransCashOrTransfer = new com.see.truetransact.uicomponent.CLabel();
        panInvestSBorCATrans = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentIDTrans = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentIDTransSBorCA = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentIDTrans1 = new com.see.truetransact.uicomponent.CButton();
        lblInvestmentType = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentType = new com.see.truetransact.uicomponent.CComboBox();
        lblInvestmentIDTrans1 = new com.see.truetransact.uicomponent.CLabel();
        lblInvestmentRefNoTrans = new com.see.truetransact.uicomponent.CLabel();
        txtInvestmentRefNoTrans = new com.see.truetransact.uicomponent.CTextField();
        txtInvestmentInternalNoTrans = new com.see.truetransact.uicomponent.CTextField();
        lblInvestmentInternalNoTrans = new com.see.truetransact.uicomponent.CLabel();
        btnInvestmentIDTransSBorCA = new com.see.truetransact.uicomponent.CButton();
        lblChequeNo = new com.see.truetransact.uicomponent.CLabel();
        txtChequeNo = new com.see.truetransact.uicomponent.CTextField();
        panInvestAmountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInvestmentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblDiscount = new com.see.truetransact.uicomponent.CLabel();
        txtDiscount = new com.see.truetransact.uicomponent.CTextField();
        lblPremiumAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPremiumAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTransTotalInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInvestmentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblBrokenPeriodIntAmount = new com.see.truetransact.uicomponent.CLabel();
        txtBrokenPeriodIntAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountValue = new com.see.truetransact.uicomponent.CLabel();
        rdoCloseYesOrNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblBrokerCommission = new com.see.truetransact.uicomponent.CLabel();
        txtBrokerCommission = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseRate = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseRate = new com.see.truetransact.uicomponent.CTextField();
        txtNoOfUnits = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfUnits = new com.see.truetransact.uicomponent.CLabel();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        srpTxtNarration = new com.see.truetransact.uicomponent.CScrollPane();
        txtNarration = new com.see.truetransact.uicomponent.CTextArea();
        lblIPrematureROI = new com.see.truetransact.uicomponent.CLabel();
        txtPrematureROI = new com.see.truetransact.uicomponent.CTextField();
        lblPrematureIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPrematureIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCloserTypeValue = new com.see.truetransact.uicomponent.CLabel();
        lblCloserType = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureCalculateAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureCalculateAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseorSalBy = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseorSalBy = new com.see.truetransact.uicomponent.CTextField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInterestPaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaymentFrequencyValue = new com.see.truetransact.uicomponent.CLabel();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCoupenRate = new com.see.truetransact.uicomponent.CLabel();
        lblCoupenValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInvestmentAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestCollected = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumPaidValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumCollected = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPaidValue = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityAmt = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityAmtValue = new com.see.truetransact.uicomponent.CLabel();
        panDepositDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblInvestmentID = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentID = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentID = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentID = new com.see.truetransact.uicomponent.CButton();
        lblInvestmentName = new com.see.truetransact.uicomponent.CLabel();
        lblLastIntPaidDate = new com.see.truetransact.uicomponent.CLabel();
        lblFaceValue = new com.see.truetransact.uicomponent.CLabel();
        lblFaceValueValue = new com.see.truetransact.uicomponent.CLabel();
        lblLastIntPaidDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblDebitChequeNo = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDate = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDateValue = new com.see.truetransact.uicomponent.CLabel();
        txtInvestmentName = new com.see.truetransact.uicomponent.CTextField();
        panAccRefNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccRefNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccRefNo = new com.see.truetransact.uicomponent.CButton();
        lblAccRefNo = new com.see.truetransact.uicomponent.CLabel();
        lblInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtDebitChequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblUpToIntRecivedDt = new com.see.truetransact.uicomponent.CLabel();
        tdtUpToIntRecivedDt = new com.see.truetransact.uicomponent.CDateField();
        panPurchaseOrSale = new com.see.truetransact.uicomponent.CPanel();
        rdoPurchase = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSale = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        rdoInterest = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCharges = new com.see.truetransact.uicomponent.CRadioButton();
        lblInvestmentBehaves = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblTransactionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtTransactionDt = new com.see.truetransact.uicomponent.CDateField();
        panMutipleTrans = new com.see.truetransact.uicomponent.CPanel();
        srpMultiTransTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblMultiTransTable = new com.see.truetransact.uicomponent.CTable();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        panTransAdd = new com.see.truetransact.uicomponent.CPanel();
        panCheckBookBtn = new com.see.truetransact.uicomponent.CPanel();
        btnaddSave = new com.see.truetransact.uicomponent.CButton();
        btnAddDelete = new com.see.truetransact.uicomponent.CButton();
        btnaddNew = new com.see.truetransact.uicomponent.CButton();
        lblTransAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTransAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTdsAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInvestTDS = new com.see.truetransact.uicomponent.CTextField();
        lblNarration1 = new com.see.truetransact.uicomponent.CLabel();
        txtNarration1 = new com.see.truetransact.uicomponent.CTextArea();
        panAmortizationDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTblAmortizationDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAmortizationDetails = new com.see.truetransact.uicomponent.CTable();
        panTransactionDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInvestmentBehavesTrans = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentBehavesTrans = new com.see.truetransact.uicomponent.CComboBox();
        lblInvestmentIDTrans = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentID1 = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentIDTrans = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentIDTrans = new com.see.truetransact.uicomponent.CButton();
        lblInvestmentNameTrans = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentName1 = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentNameTrans = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseOrSale1 = new com.see.truetransact.uicomponent.CPanel();
        rdoPurchaseTrans = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSaleTrans = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransTypeTrans = new com.see.truetransact.uicomponent.CLabel();
        rdoInterestTrans = new com.see.truetransact.uicomponent.CRadioButton();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseOrSale2 = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnAmortization = new com.see.truetransact.uicomponent.CButton();
        btnViewTrans = new com.see.truetransact.uicomponent.CButton();
        lblAccRefNo1 = new com.see.truetransact.uicomponent.CLabel();
        panAccRefNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtTranAccRefNo = new com.see.truetransact.uicomponent.CTextField();
        btnTranAccRefNo = new com.see.truetransact.uicomponent.CButton();
        lblInternalAccNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtTranInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
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
        setMaximumSize(new java.awt.Dimension(1000, 670));
        setMinimumSize(new java.awt.Dimension(1000, 670));
        setPreferredSize(new java.awt.Dimension(1000, 670));

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
        btnNew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnNewFocusLost(evt);
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
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
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
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
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

        tabClosingType1.setMaximumSize(new java.awt.Dimension(800, 600));
        tabClosingType1.setMinimumSize(new java.awt.Dimension(800, 600));
        tabClosingType1.setPreferredSize(new java.awt.Dimension(800, 600));

        panTransaction1.setMinimumSize(new java.awt.Dimension(980, 520));
        panTransaction1.setPreferredSize(new java.awt.Dimension(980, 520));
        panTransaction1.setLayout(new java.awt.GridBagLayout());

        panInvestmentTrans.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panInvestmentTrans.setMinimumSize(new java.awt.Dimension(980, 560));
        panInvestmentTrans.setPreferredSize(new java.awt.Dimension(980, 560));
        panInvestmentTrans.setLayout(new java.awt.GridBagLayout());

        tabClosingType.setMinimumSize(new java.awt.Dimension(950, 260));
        tabClosingType.setPreferredSize(new java.awt.Dimension(950, 260));

        panClosingPosition.setMaximumSize(new java.awt.Dimension(795, 240));
        panClosingPosition.setMinimumSize(new java.awt.Dimension(795, 240));
        panClosingPosition.setPreferredSize(new java.awt.Dimension(795, 240));
        panClosingPosition.setLayout(new java.awt.GridBagLayout());

        lblPurchaseDt.setText("Purchase Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 1, 1, 2);
        panClosingPosition.add(lblPurchaseDt, gridBagConstraints);

        tdtPurchaseDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPurchaseDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 2, 4);
        panClosingPosition.add(tdtPurchaseDt, gridBagConstraints);

        panPurchaseThrough.setMaximumSize(new java.awt.Dimension(270, 23));
        panPurchaseThrough.setMinimumSize(new java.awt.Dimension(270, 10));
        panPurchaseThrough.setPreferredSize(new java.awt.Dimension(270, 20));
        panPurchaseThrough.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPurchaseThrough.add(lblTotNoOfSharesCount2, gridBagConstraints);

        rdoMarket.setText("Market");
        rdoMarket.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoMarket.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoMarket.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMarketActionPerformed(evt);
            }
        });
        rdoMarket.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoMarketFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseThrough.add(rdoMarket, gridBagConstraints);

        rdoOffMarket.setText("Off  Market");
        rdoOffMarket.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoOffMarket.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoOffMarket.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoOffMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOffMarketActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseThrough.add(rdoOffMarket, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        panClosingPosition.add(panPurchaseThrough, gridBagConstraints);

        panPurchaseMode.setMaximumSize(new java.awt.Dimension(270, 23));
        panPurchaseMode.setMinimumSize(new java.awt.Dimension(270, 10));
        panPurchaseMode.setPreferredSize(new java.awt.Dimension(270, 20));
        panPurchaseMode.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPurchaseMode.add(lblTotNoOfSharesCount3, gridBagConstraints);

        rdoPrimary.setText("Primary");
        rdoPrimary.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoPrimary.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoPrimary.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoPrimary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPrimaryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseMode.add(rdoPrimary, gridBagConstraints);

        rdoSecondary.setText("Secondary");
        rdoSecondary.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoSecondary.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoSecondary.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSecondaryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseMode.add(rdoSecondary, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        panClosingPosition.add(panPurchaseMode, gridBagConstraints);

        lblBrokerName.setText("Broker Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblBrokerName, gridBagConstraints);

        txtBrokerName.setEnabled(false);
        txtBrokerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(txtBrokerName, gridBagConstraints);

        lblPreMatureCloserRate.setText("PreMature ROI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblPreMatureCloserRate, gridBagConstraints);

        txtPreMatureCloserRate.setEnabled(false);
        txtPreMatureCloserRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(txtPreMatureCloserRate, gridBagConstraints);

        panInvestmentSBorCATrans.setMinimumSize(new java.awt.Dimension(290, 30));
        panInvestmentSBorCATrans.setPreferredSize(new java.awt.Dimension(290, 30));
        panInvestmentSBorCATrans.setLayout(new java.awt.GridBagLayout());

        panSBorCA.setMinimumSize(new java.awt.Dimension(102, 18));
        panSBorCA.setPreferredSize(new java.awt.Dimension(102, 18));
        panSBorCA.setLayout(new java.awt.GridBagLayout());

        rdgSBorCA.add(rdoSBorCA_yes);
        rdoSBorCA_yes.setText("Yes");
        rdoSBorCA_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoSBorCA_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoSBorCA_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoSBorCA_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSBorCA_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSBorCA.add(rdoSBorCA_yes, gridBagConstraints);

        rdgSBorCA.add(rdoSBorCA_no);
        rdoSBorCA_no.setText("No");
        rdoSBorCA_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoSBorCA_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoSBorCA_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSBorCA_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSBorCA.add(rdoSBorCA_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panInvestmentSBorCATrans.add(panSBorCA, gridBagConstraints);

        lblTransCashOrTransfer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransCashOrTransfer.setText("Whether Debit From SB/CA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentSBorCATrans.add(lblTransCashOrTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestmentSBorCATrans, gridBagConstraints);

        panInvestSBorCATrans.setMinimumSize(new java.awt.Dimension(270, 135));
        panInvestSBorCATrans.setPreferredSize(new java.awt.Dimension(270, 135));
        panInvestSBorCATrans.setLayout(new java.awt.GridBagLayout());

        panInvestmentIDTrans.setLayout(new java.awt.GridBagLayout());

        txtInvestmentIDTransSBorCA.setEnabled(false);
        txtInvestmentIDTransSBorCA.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panInvestmentIDTrans.add(txtInvestmentIDTransSBorCA, gridBagConstraints);

        btnInvestmentIDTrans1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentIDTrans1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTrans1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTrans1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentIDTrans1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentIDTrans.add(btnInvestmentIDTrans1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panInvestSBorCATrans.add(panInvestmentIDTrans, gridBagConstraints);

        lblInvestmentType.setText("Investment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(lblInvestmentType, gridBagConstraints);

        cboInvestmentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInvestmentType.setPopupWidth(220);
        cboInvestmentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInvestmentTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestSBorCATrans.add(cboInvestmentType, gridBagConstraints);

        lblInvestmentIDTrans1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentIDTrans1.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(lblInvestmentIDTrans1, gridBagConstraints);

        lblInvestmentRefNoTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentRefNoTrans.setText("Account Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(lblInvestmentRefNoTrans, gridBagConstraints);

        txtInvestmentRefNoTrans.setEnabled(false);
        txtInvestmentRefNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestSBorCATrans.add(txtInvestmentRefNoTrans, gridBagConstraints);

        txtInvestmentInternalNoTrans.setEnabled(false);
        txtInvestmentInternalNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestSBorCATrans.add(txtInvestmentInternalNoTrans, gridBagConstraints);

        lblInvestmentInternalNoTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentInternalNoTrans.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(lblInvestmentInternalNoTrans, gridBagConstraints);

        btnInvestmentIDTransSBorCA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentIDTransSBorCA.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTransSBorCA.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTransSBorCA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentIDTransSBorCAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(btnInvestmentIDTransSBorCA, gridBagConstraints);

        lblChequeNo.setText("Cheque No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestSBorCATrans.add(lblChequeNo, gridBagConstraints);

        txtChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChequeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChequeNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestSBorCATrans.add(txtChequeNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestSBorCATrans, gridBagConstraints);

        panInvestAmountDetails.setMinimumSize(new java.awt.Dimension(320, 235));
        panInvestAmountDetails.setPreferredSize(new java.awt.Dimension(320, 235));
        panInvestAmountDetails.setLayout(new java.awt.GridBagLayout());

        lblInvestmentAmount.setText("Investment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblInvestmentAmount, gridBagConstraints);

        txtInvestmentAmount.setEnabled(false);
        txtInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtInvestmentAmount, gridBagConstraints);

        lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscount.setText("Discount Amount");
        lblDiscount.setMaximumSize(new java.awt.Dimension(100, 8));
        lblDiscount.setMinimumSize(new java.awt.Dimension(100, 8));
        lblDiscount.setPreferredSize(new java.awt.Dimension(256, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblDiscount, gridBagConstraints);

        txtDiscount.setEnabled(false);
        txtDiscount.setMinimumSize(new java.awt.Dimension(100, 10));
        txtDiscount.setPreferredSize(new java.awt.Dimension(100, 10));
        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtDiscount, gridBagConstraints);

        lblPremiumAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPremiumAmount.setText("Premium Amount");
        lblPremiumAmount.setMaximumSize(new java.awt.Dimension(100, 8));
        lblPremiumAmount.setMinimumSize(new java.awt.Dimension(100, 8));
        lblPremiumAmount.setPreferredSize(new java.awt.Dimension(100, 8));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblPremiumAmount, gridBagConstraints);

        txtPremiumAmount.setMinimumSize(new java.awt.Dimension(100, 10));
        txtPremiumAmount.setPreferredSize(new java.awt.Dimension(100, 10));
        txtPremiumAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtPremiumAmount, gridBagConstraints);

        lblTransTotalInvestmentAmount.setText("Total Investment Amount");
        lblTransTotalInvestmentAmount.setMaximumSize(new java.awt.Dimension(100, 10));
        lblTransTotalInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 10));
        lblTransTotalInvestmentAmount.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblTransTotalInvestmentAmount, gridBagConstraints);

        txtTotalInvestmentAmount.setEnabled(false);
        txtTotalInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 10));
        txtTotalInvestmentAmount.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtTotalInvestmentAmount, gridBagConstraints);

        lblBrokenPeriodIntAmount.setText("Broken Period Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblBrokenPeriodIntAmount, gridBagConstraints);

        txtBrokenPeriodIntAmount.setEnabled(false);
        txtBrokenPeriodIntAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBrokenPeriodIntAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBrokenPeriodIntAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtBrokenPeriodIntAmount, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestAmountDetails.add(lblTotalAmount, gridBagConstraints);

        lblTotalAmountValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountValue.setPreferredSize(new java.awt.Dimension(100, 16));
        lblTotalAmountValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lblTotalAmountValueFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestAmountDetails.add(lblTotalAmountValue, gridBagConstraints);

        rdoCloseYesOrNo.setText("Close");
        rdoCloseYesOrNo.setMaximumSize(new java.awt.Dimension(90, 9));
        rdoCloseYesOrNo.setMinimumSize(new java.awt.Dimension(90, 9));
        rdoCloseYesOrNo.setPreferredSize(new java.awt.Dimension(90, 9));
        rdoCloseYesOrNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCloseYesOrNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInvestAmountDetails.add(rdoCloseYesOrNo, gridBagConstraints);

        lblBrokerCommission.setText("Broker Commission");
        lblBrokerCommission.setMaximumSize(new java.awt.Dimension(100, 9));
        lblBrokerCommission.setMinimumSize(new java.awt.Dimension(100, 9));
        lblBrokerCommission.setPreferredSize(new java.awt.Dimension(100, 9));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblBrokerCommission, gridBagConstraints);

        txtBrokerCommission.setMinimumSize(new java.awt.Dimension(100, 10));
        txtBrokerCommission.setPreferredSize(new java.awt.Dimension(100, 10));
        txtBrokerCommission.setEnabled(false);
        txtBrokerCommission.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBrokerCommissionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtBrokerCommission, gridBagConstraints);

        lblPurchaseRate.setText("Fees Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblPurchaseRate, gridBagConstraints);

        txtPurchaseRate.setEnabled(false);
        txtPurchaseRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtPurchaseRate, gridBagConstraints);

        txtNoOfUnits.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfUnits.setEnabled(false);
        txtNoOfUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfUnitsActionPerformed(evt);
            }
        });
        txtNoOfUnits.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfUnitsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtNoOfUnits, gridBagConstraints);

        lblNoOfUnits.setText("No Of Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblNoOfUnits, gridBagConstraints);

        lblNarration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarration.setText("Narration");
        lblNarration.setMinimumSize(new java.awt.Dimension(72, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblNarration, gridBagConstraints);

        srpTxtNarration.setMinimumSize(new java.awt.Dimension(160, 40));
        srpTxtNarration.setPreferredSize(new java.awt.Dimension(160, 40));

        txtNarration.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtNarration.setLineWrap(true);
        txtNarration.setMinimumSize(new java.awt.Dimension(2, 8));
        txtNarration.setPreferredSize(new java.awt.Dimension(2, 8));
        srpTxtNarration.setViewportView(txtNarration);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panInvestAmountDetails.add(srpTxtNarration, gridBagConstraints);

        lblIPrematureROI.setText("Premature ROI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblIPrematureROI, gridBagConstraints);

        txtPrematureROI.setEnabled(false);
        txtPrematureROI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrematureROI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrematureROIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtPrematureROI, gridBagConstraints);

        lblPrematureIntAmt.setText("Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblPrematureIntAmt, gridBagConstraints);

        txtPrematureIntAmt.setEnabled(false);
        txtPrematureIntAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrematureIntAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrematureIntAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestAmountDetails.add(txtPrematureIntAmt, gridBagConstraints);

        lblCloserTypeValue.setForeground(new java.awt.Color(51, 51, 255));
        lblCloserTypeValue.setText("Closer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInvestAmountDetails.add(lblCloserTypeValue, gridBagConstraints);

        lblCloserType.setText("Closer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInvestAmountDetails.add(lblCloserType, gridBagConstraints);

        lblPrematureCalculateAmt.setText("Premature Int Amount Rs.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestAmountDetails.add(lblPrematureCalculateAmt, gridBagConstraints);

        lblPrematureCalculateAmtVal.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPrematureCalculateAmtVal.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPrematureCalculateAmtVal.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestAmountDetails.add(lblPrematureCalculateAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestAmountDetails, gridBagConstraints);

        lblPurchaseorSalBy.setText("Purchase/Sale By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblPurchaseorSalBy, gridBagConstraints);

        txtPurchaseorSalBy.setEnabled(false);
        txtPurchaseorSalBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(txtPurchaseorSalBy, gridBagConstraints);

        tabClosingType.addTab("Investment Transaction Details", panClosingPosition);

        panTransaction.setMinimumSize(new java.awt.Dimension(795, 235));
        panTransaction.setPreferredSize(new java.awt.Dimension(795, 235));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        tabClosingType.addTab("Transaction", panTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 17, 2);
        panInvestmentTrans.add(tabClosingType, gridBagConstraints);

        panDepositDetails.setMaximumSize(new java.awt.Dimension(400, 225));
        panDepositDetails.setMinimumSize(new java.awt.Dimension(400, 225));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(400, 225));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblInterestPaymentFrequency.setText("Interest Payment Frequency : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequency, gridBagConstraints);

        lblInterestPaymentFrequencyValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblInterestPaymentFrequencyValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblInterestPaymentFrequencyValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequencyValue, gridBagConstraints);

        lblPeriod.setText("Period : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriod, gridBagConstraints);

        lblPeriodValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblPeriodValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblPeriodValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriodValue, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDate, gridBagConstraints);

        lblMaturityDateValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblMaturityDateValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblMaturityDateValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDateValue, gridBagConstraints);

        lblCoupenRate.setText("Coupen Rate : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblCoupenRate, gridBagConstraints);

        lblCoupenValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblCoupenValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblCoupenValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblCoupenValue, gridBagConstraints);

        lblTotalInvestmentAmount.setText("Total Investment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInvestmentAmount, gridBagConstraints);

        lblTotalInvestmentAmountValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblTotalInvestmentAmountValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblTotalInvestmentAmountValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInvestmentAmountValue, gridBagConstraints);

        lblTotalInterestCollected.setText("Total Interest Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestCollected, gridBagConstraints);

        lblTotalInterestCollectedValue.setMaximumSize(new java.awt.Dimension(50, 16));
        lblTotalInterestCollectedValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblTotalInterestCollectedValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestCollectedValue, gridBagConstraints);

        lblTotalPremiumPaid.setText("Total Premium Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumPaid, gridBagConstraints);

        lblTotalPremiumPaidValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumPaidValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumPaidValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumPaidValue, gridBagConstraints);

        lblTotalPremiumCollected.setText("Total Premium Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumCollected, gridBagConstraints);

        lblTotalPremiumCollectedValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblTotalPremiumCollectedValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblTotalPremiumCollectedValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumCollectedValue, gridBagConstraints);

        lblTotalInterestPaid.setText("Total Interest Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestPaid, gridBagConstraints);

        lblTotalInterestPaidValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblTotalInterestPaidValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblTotalInterestPaidValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestPaidValue, gridBagConstraints);

        lblMaturityAmt.setText("Total Premium Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityAmt, gridBagConstraints);

        lblMaturityAmtValue.setMaximumSize(new java.awt.Dimension(50, 16));
        lblMaturityAmtValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblMaturityAmtValue.setPreferredSize(new java.awt.Dimension(200, 16));
        lblMaturityAmtValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMaturityAmtValueMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityAmtValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestmentTrans.add(panDepositDetails, gridBagConstraints);

        panDepositDetails1.setMaximumSize(new java.awt.Dimension(350, 225));
        panDepositDetails1.setMinimumSize(new java.awt.Dimension(350, 225));
        panDepositDetails1.setPreferredSize(new java.awt.Dimension(350, 225));
        panDepositDetails1.setLayout(new java.awt.GridBagLayout());

        lblInvestmentID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentID.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panDepositDetails1.add(lblInvestmentID, gridBagConstraints);

        panInvestmentID.setLayout(new java.awt.GridBagLayout());

        txtInvestmentID.setEditable(false);
        txtInvestmentID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentID.setEnabled(false);
        txtInvestmentID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panInvestmentID.add(txtInvestmentID, gridBagConstraints);

        btnInvestmentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentIDActionPerformed(evt);
            }
        });
        btnInvestmentID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnInvestmentIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentID.add(btnInvestmentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panDepositDetails1.add(panInvestmentID, gridBagConstraints);

        lblInvestmentName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentName.setText("A/c Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panDepositDetails1.add(lblInvestmentName, gridBagConstraints);

        lblLastIntPaidDate.setText("Last Interest Paid Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panDepositDetails1.add(lblLastIntPaidDate, gridBagConstraints);

        lblFaceValue.setText("Face value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panDepositDetails1.add(lblFaceValue, gridBagConstraints);

        lblFaceValueValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblFaceValueValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblFaceValueValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(lblFaceValueValue, gridBagConstraints);

        lblLastIntPaidDateValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblLastIntPaidDateValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblLastIntPaidDateValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panDepositDetails1.add(lblLastIntPaidDateValue, gridBagConstraints);

        lblDebitChequeNo.setText("Cheque No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(lblDebitChequeNo, gridBagConstraints);

        lblIssueDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblIssueDate, gridBagConstraints);

        lblIssueDateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblIssueDateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblIssueDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblIssueDateValue, gridBagConstraints);

        txtInvestmentName.setEditable(false);
        txtInvestmentName.setEnabled(false);
        txtInvestmentName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtInvestmentName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panDepositDetails1.add(txtInvestmentName, gridBagConstraints);

        panAccRefNo.setLayout(new java.awt.GridBagLayout());

        txtAccRefNo.setEditable(false);
        txtAccRefNo.setEnabled(false);
        txtAccRefNo.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAccRefNo.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAccRefNo.add(txtAccRefNo, gridBagConstraints);

        btnAccRefNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccRefNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccRefNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccRefNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccRefNo.add(btnAccRefNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panDepositDetails1.add(panAccRefNo, gridBagConstraints);

        lblAccRefNo.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblAccRefNo, gridBagConstraints);

        lblInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblInternalAccNo, gridBagConstraints);

        txtInternalAccNo.setBackground(new java.awt.Color(220, 220, 220));
        txtInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        panDepositDetails1.add(txtInternalAccNo, gridBagConstraints);

        txtDebitChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitChequeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitChequeNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositDetails1.add(txtDebitChequeNo, gridBagConstraints);

        lblUpToIntRecivedDt.setText("Int Received Upto");
        lblUpToIntRecivedDt.setMaximumSize(new java.awt.Dimension(133, 18));
        lblUpToIntRecivedDt.setMinimumSize(new java.awt.Dimension(133, 18));
        lblUpToIntRecivedDt.setPreferredSize(new java.awt.Dimension(133, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panDepositDetails1.add(lblUpToIntRecivedDt, gridBagConstraints);

        tdtUpToIntRecivedDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtUpToIntRecivedDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(tdtUpToIntRecivedDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestmentTrans.add(panDepositDetails1, gridBagConstraints);

        panPurchaseOrSale.setMaximumSize(new java.awt.Dimension(650, 45));
        panPurchaseOrSale.setMinimumSize(new java.awt.Dimension(650, 45));
        panPurchaseOrSale.setPreferredSize(new java.awt.Dimension(650, 45));
        panPurchaseOrSale.setLayout(new java.awt.GridBagLayout());

        rdoPurchase.setText("Purchase");
        rdoPurchase.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoPurchase.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoPurchase.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPurchaseActionPerformed(evt);
            }
        });
        rdoPurchase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoPurchaseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoPurchase, gridBagConstraints);

        rdoSale.setText("Sale");
        rdoSale.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoSale.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoSale.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSaleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoSale, gridBagConstraints);

        lblTransType.setText("TransType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPurchaseOrSale.add(lblTransType, gridBagConstraints);

        rdoInterest.setText("Interest");
        rdoInterest.setMaximumSize(new java.awt.Dimension(70, 18));
        rdoInterest.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoInterest.setPreferredSize(new java.awt.Dimension(70, 18));
        rdoInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestActionPerformed(evt);
            }
        });
        rdoInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoInterestFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoInterest, gridBagConstraints);

        rdoCharges.setText("Charges/Other Debits");
        rdoCharges.setMaximumSize(new java.awt.Dimension(150, 18));
        rdoCharges.setMinimumSize(new java.awt.Dimension(150, 18));
        rdoCharges.setPreferredSize(new java.awt.Dimension(150, 18));
        rdoCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoCharges, gridBagConstraints);

        lblInvestmentBehaves.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panPurchaseOrSale.add(lblInvestmentBehaves, gridBagConstraints);

        cboInvestmentBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
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
        cboInvestmentBehaves.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInvestmentBehavesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseOrSale.add(cboInvestmentBehaves, gridBagConstraints);

        lblTransactionDt.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 31, 0, 3);
        panPurchaseOrSale.add(lblTransactionDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 38);
        panPurchaseOrSale.add(tdtTransactionDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(41, 0, 0, 0);
        panInvestmentTrans.add(panPurchaseOrSale, gridBagConstraints);

        panMutipleTrans.setMinimumSize(new java.awt.Dimension(280, 225));
        panMutipleTrans.setPreferredSize(new java.awt.Dimension(280, 225));
        panMutipleTrans.setLayout(new java.awt.GridBagLayout());

        srpMultiTransTable.setMinimumSize(new java.awt.Dimension(250, 150));
        srpMultiTransTable.setPreferredSize(new java.awt.Dimension(250, 150));

        tblMultiTransTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Investment  ID", "Int.A/C No", "Amount"
            }
        ));
        tblMultiTransTable.setMinimumSize(new java.awt.Dimension(225, 200));
        tblMultiTransTable.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 250));
        tblMultiTransTable.setPreferredSize(new java.awt.Dimension(225, 400));
        tblMultiTransTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMultiTransTableMousePressed(evt);
            }
        });
        srpMultiTransTable.setViewportView(tblMultiTransTable);

        panMutipleTrans.add(srpMultiTransTable, new java.awt.GridBagConstraints());

        cPanel1.setMinimumSize(new java.awt.Dimension(250, 30));
        cPanel1.setPreferredSize(new java.awt.Dimension(250, 30));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblTotAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(lblTotAmt, gridBagConstraints);

        txtTotAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(txtTotAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMutipleTrans.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        panInvestmentTrans.add(panMutipleTrans, gridBagConstraints);

        panTransAdd.setMinimumSize(new java.awt.Dimension(900, 30));
        panTransAdd.setPreferredSize(new java.awt.Dimension(900, 30));
        panTransAdd.setLayout(new java.awt.GridBagLayout());

        panCheckBookBtn.setMinimumSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setPreferredSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setLayout(new java.awt.GridBagLayout());

        btnaddSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnaddSave.setToolTipText("New");
        btnaddSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddSave, gridBagConstraints);

        btnAddDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAddDelete.setToolTipText("Delete");
        btnAddDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAddDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnAddDelete, gridBagConstraints);

        btnaddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnaddNew.setToolTipText("New");
        btnaddNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransAdd.add(panCheckBookBtn, gridBagConstraints);

        lblTransAmt.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransAdd.add(lblTransAmt, gridBagConstraints);

        txtTransAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransAmtActionPerformed(evt);
            }
        });
        txtTransAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransAdd.add(txtTransAmt, gridBagConstraints);

        lblTdsAmount.setText("TDS Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 0, 6);
        panTransAdd.add(lblTdsAmount, gridBagConstraints);

        txtInvestTDS.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestTDS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvestTDSActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panTransAdd.add(txtInvestTDS, gridBagConstraints);

        lblNarration1.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 27, 0, 1);
        panTransAdd.add(lblNarration1, gridBagConstraints);

        txtNarration1.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtNarration1.setLineWrap(true);
        txtNarration1.setMinimumSize(new java.awt.Dimension(180, 40));
        txtNarration1.setPreferredSize(new java.awt.Dimension(120, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        panTransAdd.add(txtNarration1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        panInvestmentTrans.add(panTransAdd, gridBagConstraints);

        panTransaction1.add(panInvestmentTrans, new java.awt.GridBagConstraints());

        tabClosingType1.addTab("Transaction", panTransaction1);

        panAmortizationDetails.setMinimumSize(new java.awt.Dimension(780, 358));
        panAmortizationDetails.setPreferredSize(new java.awt.Dimension(800, 390));
        panAmortizationDetails.setLayout(new java.awt.GridBagLayout());

        srpTblAmortizationDetails.setMaximumSize(new java.awt.Dimension(780, 100));
        srpTblAmortizationDetails.setMinimumSize(new java.awt.Dimension(780, 100));
        srpTblAmortizationDetails.setPreferredSize(new java.awt.Dimension(780, 100));

        tblAmortizationDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial No.", "Issue Date ", "No of Shares", "Share Value", "Status", "add/withdrawl"
            }
        ));
        tblAmortizationDetails.setMinimumSize(new java.awt.Dimension(225, 16));
        tblAmortizationDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 380));
        srpTblAmortizationDetails.setViewportView(tblAmortizationDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 13, 0);
        panAmortizationDetails.add(srpTblAmortizationDetails, gridBagConstraints);

        panTransactionDetails1.setMaximumSize(new java.awt.Dimension(200, 240));
        panTransactionDetails1.setMinimumSize(new java.awt.Dimension(200, 240));
        panTransactionDetails1.setPreferredSize(new java.awt.Dimension(200, 240));
        panTransactionDetails1.setLayout(new java.awt.GridBagLayout());

        panTransactionDetails.setMaximumSize(new java.awt.Dimension(385, 250));
        panTransactionDetails.setMinimumSize(new java.awt.Dimension(385, 250));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(385, 250));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());

        lblInvestmentBehavesTrans.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionDetails.add(lblInvestmentBehavesTrans, gridBagConstraints);

        cboInvestmentBehavesTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInvestmentBehavesTrans.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransactionDetails.add(cboInvestmentBehavesTrans, gridBagConstraints);

        lblInvestmentIDTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentIDTrans.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panTransactionDetails.add(lblInvestmentIDTrans, gridBagConstraints);

        panInvestmentID1.setLayout(new java.awt.GridBagLayout());

        txtInvestmentIDTrans.setEditable(false);
        txtInvestmentIDTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentIDTrans.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentID1.add(txtInvestmentIDTrans, gridBagConstraints);

        btnInvestmentIDTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentIDTrans.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTrans.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentIDTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentIDTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentID1.add(btnInvestmentIDTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panTransactionDetails.add(panInvestmentID1, gridBagConstraints);

        lblInvestmentNameTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentNameTrans.setText("Investment Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panTransactionDetails.add(lblInvestmentNameTrans, gridBagConstraints);

        panInvestmentName1.setLayout(new java.awt.GridBagLayout());

        txtInvestmentNameTrans.setEditable(false);
        txtInvestmentNameTrans.setMinimumSize(new java.awt.Dimension(200, 21));
        txtInvestmentNameTrans.setPreferredSize(new java.awt.Dimension(200, 21));
        txtInvestmentNameTrans.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentName1.add(txtInvestmentNameTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panTransactionDetails.add(panInvestmentName1, gridBagConstraints);

        panPurchaseOrSale1.setMaximumSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale1.setMinimumSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale1.setPreferredSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale1.setLayout(new java.awt.GridBagLayout());

        rdoPurchaseTrans.setText("Purchase");
        rdoPurchaseTrans.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoPurchaseTrans.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoPurchaseTrans.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale1.add(rdoPurchaseTrans, gridBagConstraints);

        rdoSaleTrans.setText("Sale");
        rdoSaleTrans.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoSaleTrans.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoSaleTrans.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale1.add(rdoSaleTrans, gridBagConstraints);

        lblTransTypeTrans.setText("TransType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPurchaseOrSale1.add(lblTransTypeTrans, gridBagConstraints);

        rdoInterestTrans.setText("Interest");
        rdoInterestTrans.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoInterestTrans.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoInterestTrans.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale1.add(rdoInterestTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panTransactionDetails.add(panPurchaseOrSale1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransactionDetails.add(tdtToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransactionDetails.add(tdtFromDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(68, 18));
        lblFromDate.setMinimumSize(new java.awt.Dimension(68, 18));
        lblFromDate.setPreferredSize(new java.awt.Dimension(68, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTransactionDetails.add(lblFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        lblToDate.setMaximumSize(new java.awt.Dimension(68, 18));
        lblToDate.setMinimumSize(new java.awt.Dimension(68, 18));
        lblToDate.setPreferredSize(new java.awt.Dimension(68, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTransactionDetails.add(lblToDate, gridBagConstraints);

        panPurchaseOrSale2.setMaximumSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale2.setMinimumSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale2.setPreferredSize(new java.awt.Dimension(335, 23));
        panPurchaseOrSale2.setLayout(new java.awt.GridBagLayout());

        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(75, 23));
        btnClear.setMinimumSize(new java.awt.Dimension(75, 23));
        btnClear.setPreferredSize(new java.awt.Dimension(75, 23));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseOrSale2.add(btnClear, gridBagConstraints);

        btnAmortization.setText("Amortization");
        btnAmortization.setMaximumSize(new java.awt.Dimension(63, 23));
        btnAmortization.setMinimumSize(new java.awt.Dimension(120, 23));
        btnAmortization.setPreferredSize(new java.awt.Dimension(120, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseOrSale2.add(btnAmortization, gridBagConstraints);

        btnViewTrans.setText("Transactions");
        btnViewTrans.setMaximumSize(new java.awt.Dimension(120, 23));
        btnViewTrans.setMinimumSize(new java.awt.Dimension(120, 23));
        btnViewTrans.setPreferredSize(new java.awt.Dimension(100, 23));
        btnViewTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseOrSale2.add(btnViewTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panTransactionDetails.add(panPurchaseOrSale2, gridBagConstraints);

        lblAccRefNo1.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionDetails.add(lblAccRefNo1, gridBagConstraints);

        panAccRefNo1.setLayout(new java.awt.GridBagLayout());

        txtTranAccRefNo.setEditable(false);
        txtTranAccRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTranAccRefNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAccRefNo1.add(txtTranAccRefNo, gridBagConstraints);

        btnTranAccRefNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTranAccRefNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTranAccRefNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTranAccRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranAccRefNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccRefNo1.add(btnTranAccRefNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panTransactionDetails.add(panAccRefNo1, gridBagConstraints);

        lblInternalAccNo1.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionDetails.add(lblInternalAccNo1, gridBagConstraints);

        txtTranInternalAccNo.setBackground(new java.awt.Color(220, 220, 220));
        txtTranInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        panTransactionDetails.add(txtTranInternalAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTransactionDetails1.add(panTransactionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmortizationDetails.add(panTransactionDetails1, gridBagConstraints);

        tabClosingType1.addTab("Transaction  Details Query", panAmortizationDetails);

        getContentPane().add(tabClosingType1, java.awt.BorderLayout.CENTER);

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

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
        System.out.println("btnPrintActionPerformed ====== " + getScreenID());

    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtPrematureIntAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrematureIntAmtFocusLost
        // TODO add your handling code here:
        if (txtPrematureIntAmt.getText().length() > 0) {
            txtInvestmentAmount.setEnabled(false);
            SetPeriodicTransAmount();
            if (CommonUtil.convertObjToStr(observable.getInterestType()).equals("Interest Payable")) {
                SetPayablePeriodicTransAmount();
            }
        }
        if (CommonUtil.convertObjToInt(txtPrematureIntAmt.getText()) == 0) {
            System.out.println("hhuweyhuir1111" + observable.getInvestment_amount());
            transactionUI.setCallingAmount(String.valueOf(observable.getInvestment_amount()));
            transactionUI.setCallingApplicantName(observable.getInvestmentName());
            observable.setTxtPrematureIntAmt(0.00);
            txtPrematureIntAmt.setText("0.00");

        }
    }//GEN-LAST:event_txtPrematureIntAmtFocusLost

    private void txtPrematureROIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrematureROIFocusLost
        // TODO add your handling code here:
        if (rdoSale.isSelected() == true) {
            txtInvestmentAmount.setEnabled(false);
            String behaves = observable.callForBehaves();
            if (behaves.equals("OTHER_BANK_FD")) {
                calcPrematureFixedInterest();
            } else if (behaves.equals("OTHER_BANK_RD")) {
                calcPrematureRecurringInterest();
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                calcPrematureCummulativeInterest();
            }
            if (CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue() < CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()) {
                txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()
                        - CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue()));
                lblPrematureIntAmt.setText("Interest Payable Amount");
                observable.setInterestType("Interest Payable");
            } else {
                txtPrematureIntAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(lblPrematureCalculateAmtVal.getText()).doubleValue()
                        - CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue()));
                lblPrematureIntAmt.setText("Interest Receivable Amount");
                observable.setInterestType("Interest Receivable");
            }
            SetPeriodicTransAmount();
            if (CommonUtil.convertObjToStr(observable.getInterestType()).equals("Interest Payable")) {
                SetPayablePeriodicTransAmount();
            }
            txtPrematureIntAmt.setEnabled(true);
        }
        if (CommonUtil.convertObjToInt(txtPrematureROI.getText()) == 0 || txtPrematureROI.getText().equals("")) {
            System.out.println("hhuweyhuir22222" + observable.getInvestment_amount());
            transactionUI.setCallingAmount(String.valueOf(observable.getInvestment_amount()));
            transactionUI.setCallingApplicantName(observable.getInvestmentName());
            observable.setTxtPrematureROI(CommonUtil.convertObjToDouble("0"));
            observable.setTxtPrematureIntAmt(0.00);
            txtPrematureIntAmt.setText("0.00");
            txtPrematureROI.setText("0");
            System.out.println("jdhfjhh3333" + txtPrematureIntAmt.getText() + "iiiii" + observable.getTxtPrematureIntAmt());
        }

    }//GEN-LAST:event_txtPrematureROIFocusLost

    private void SetPeriodicTransAmount() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String bheaves = observable.callForBehaves();
            if (bheaves.length() > 0) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                    transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
                    transactionUI.setCallingApplicantName(txtInvestmentName.getText());
                    if (rdoSBorCA_yes.isSelected() == true) {
                        transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                    }
                }
            }
            lblTotalAmountValue.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
           // txtTransAmt.setText(lblTotalAmountValue.getText());
        }
    }

    private void SetPayablePeriodicTransAmount() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String bheaves = observable.callForBehaves();
            if (bheaves.length() > 0) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                    transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
                    transactionUI.setCallingApplicantName(txtInvestmentName.getText());
                    if (rdoSBorCA_yes.isSelected() == true) {
                        transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                    }
                }
            }
            lblTotalAmountValue.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtPrematureIntAmt.getText()).doubleValue()));
            // txtTransAmt.setText(lblTotalAmountValue.getText());
        }
    }

    private void calcPrematureFixedInterest() {
        if (lblFaceValueValue.getText().length() > 0 && txtPrematureROI.getText().length() > 0 && lblInterestPaymentFrequencyValue.getText().length() > 0 && lblMaturityDateValue.getText().length() > 0) {
            double intAmt = 0.0;
            double amt = 0.0;
            double period = 0.0;
            HashMap hashmap = new HashMap();
            hashmap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            List depList = ClientUtil.executeQuery("getDepDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                if (CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("RATE_OF_INTEREST"))).doubleValue()
                        < CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(txtPrematureROI.getText())).doubleValue()) {
                    ClientUtil.showMessageWindow("Premature Rate of Interest Should be Less than or Equal to " + hashmap.get("RATE_OF_INTEREST") + "%");
                    txtPrematureROI.setText("");
                    lblPrematureCalculateAmtVal.setText("");
                    return;
                }
                java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
                java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
//                period = DateUtil.dateDiff(effectiveDate, matDate);
                period = DateUtil.dateDiff(effectiveDate, curDate);
                System.out.println("#############  period " + period);
                if (lblInterestPaymentFrequencyValue.getText().equals("Monthly") && period > 29) {
                    System.out.println("#############  Monthly ");
                    double roi = 0.0;
                    double amount = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                    roi = CommonUtil.convertObjToDouble(txtPrematureROI.getText()).doubleValue();
                    period = period / 30;
                    intAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                    System.out.println("intAmt: " + intAmt);
                    double calcAmt = amount / 100;
                    intAmt = intAmt * calcAmt;
                    intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                    intAmt = intAmt * period;
                } else {
                    System.out.println("#############  Not_Monthly ");
                    double principal = 0.0;
                    double rateOfInterest = 0.0;
                    double days = 0.0;
                    days = DateUtil.dateDiff(effectiveDate, curDate);
                    System.out.println("First #########diffDay : " + days);
                    principal = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                    rateOfInterest = CommonUtil.convertObjToDouble(txtPrematureROI.getText()).doubleValue();
                    intAmt = (principal * rateOfInterest * days / 36500);
                }
                intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                System.out.println("Premature intAmt : " + intAmt);
                lblPrematureCalculateAmtVal.setText(String.valueOf(intAmt));
            }
        }
    }

    private void calcPrematureRecurringInterest() {
        if (lblFaceValueValue.getText().length() > 0 && txtPrematureROI.getText().length() > 0 && lblMaturityDateValue.getText().length() > 0) {
            double period = 0.0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            HashMap hashmap = new HashMap();
            hashmap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            List depList = ClientUtil.executeQuery("getDepDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                //             java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
                java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                period = DateUtil.dateDiff(effectiveDate, curDate);
                principal = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                double rateOfInterest = CommonUtil.convertObjToDouble(txtPrematureROI.getText()).doubleValue() / 100;
                rateOfInterest = rateOfInterest / 12;
                double ibaInterest = (rateOfInterest * 100 * 12);
                double installment = 0.0;
                installment = period / 30;
                period = installment / 3;
                amount = principal * (Math.pow((1 + ibaInterest / 400), period) - 1) / (1 - Math.pow((1 + ibaInterest / 400), -1 / 3.0));
                interest = amount - (principal * installment);
                interest = (double) getNearest((long) (interest * 100), 100) / 100;
                System.out.println("intAmt: " + interest);
                lblPrematureCalculateAmtVal.setText(String.valueOf(interest));
            }
        }
    }

    private void calcPrematureCummulativeInterest() {
        if (lblFaceValueValue.getText().length() > 0 && txtPrematureROI.getText().length() > 0 && lblMaturityDateValue.getText().length() > 0) {
            double period = 0.0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            HashMap hashmap = new HashMap();
            hashmap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            List depList = ClientUtil.executeQuery("getDepDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                //            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
                java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                period = DateUtil.dateDiff(effectiveDate, curDate);
                principal = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                period = period / 30;
                period = (double) roundOffLower((long) (period * 100), 100) / 100;
                double rateOfInterest = CommonUtil.convertObjToDouble(txtPrematureROI.getText()).doubleValue() / 100;
                amount = principal * (Math.pow((1 + rateOfInterest / 4.0), period / 12 * 4.0));
                interest = amount - principal;
                interest = (double) getNearest((long) (interest * 100), 100) / 100;
                System.out.println("intAmt: " + interest);
                lblPrematureCalculateAmtVal.setText(String.valueOf(interest));
            }
        }
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    private void btnTranAccRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranAccRefNoActionPerformed
        // TODO add your handling code here:

        callView("InvestmentTranAccNo");
        String bheaves = "";
        bheaves = observable.callForBehaves();
        if (rdoInterest.isSelected() == true && tdtUpToIntRecivedDt.getDateValue().length() > 0 && (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD"))) {
            Date maturityDate = null;
            tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(curDate));
            maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
            if (DateUtil.dateDiff(curDate, maturityDate) < 0) {
                tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(maturityDate));
                tdtUpToIntRecivedDt.setEnabled(false);
            } else {
                tdtUpToIntRecivedDt.setEnabled(true);
            }
            calcFixedInterest();
            txtBrokenPeriodIntAmountFocusLost(null);
        }
    }//GEN-LAST:event_btnTranAccRefNoActionPerformed

    private void tdtUpToIntRecivedDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtUpToIntRecivedDtFocusLost
        // TODO add your handling code here:
        if (tdtUpToIntRecivedDt.getDateValue().length() > 0) {
            Date maturityDate = null;
            Date interestRecDate = null;
            maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
            interestRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtUpToIntRecivedDt.getDateValue()));
            if (DateUtil.dateDiff(interestRecDate, curDate) < 0) {
                ClientUtil.showMessageWindow("Date Should not be Greater than Current Date !!! ");
                tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(curDate));
            }
            calcFixedInterest();
            txtBrokenPeriodIntAmountFocusLost(null);
        }
    }//GEN-LAST:event_tdtUpToIntRecivedDtFocusLost

    private void txtDebitChequeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitChequeNoFocusLost
        // TODO add your handling code here:
        if (txtDebitChequeNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            whereMap.put("CHEQUE_NO", txtDebitChequeNo.getText());
            List chequeList = ClientUtil.executeQuery("getChequeNoFromInvestMaster", whereMap);
            if (chequeList != null && chequeList.size() > 0) {
            } else {
                ClientUtil.showMessageWindow("Invalid Cheque No !!! ");
                txtDebitChequeNo.setText("");
            }
        }
    }//GEN-LAST:event_txtDebitChequeNoFocusLost

    private void rdoChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChargesActionPerformed
        // TODO add your handling code here:
        interestMethod(false);
        if (rdoCharges.isSelected() == true) {
            rdoSale.setSelected(false);
            rdoPurchase.setSelected(false);
            rdoInterest.setSelected(false);
            rdoCloseYesOrNo.setVisible(false);
            rdoCloseYesOrNo.setSelected(false);
            ClientUtil.enableDisable(panInvestmentTrans, true);
            ClientUtil.enableDisable(panDepositDetails1, false);
            ClientUtil.enableDisable(panPurchaseOrSale, false);
            txtInvestmentID.setEditable(false);
            txtInvestmentName.setEditable(false);
            panInvestmentSBorCATrans.setVisible(false);
        }
        changeFieldNamesAccordingTOBehavesAndRdo();
        String txtBehaves = "";
        txtBehaves = observable.callForBehaves();
        if (rdoInterest.isSelected() == true && (txtBehaves.equals("OTHER_BANK_FD") || txtBehaves.equals("OTHER_BANK_SSD"))) {
            panInvestmentSBorCATrans.setVisible(true);
            rdoSBorCA_noActionPerformed(null);
        } else {
            panInvestmentSBorCATrans.setVisible(false);
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
        }
    }//GEN-LAST:event_rdoChargesActionPerformed

    private void txtChequeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeNoFocusLost
        // TODO add your handling code here:
        if (txtChequeNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("INVESTMENT_ID", txtInvestmentInternalNoTrans.getText());
            whereMap.put("CHEQUE_NO", txtChequeNo.getText());
            List chequeList = ClientUtil.executeQuery("getChequeNoFromInvestMaster", whereMap);
            if (chequeList != null && chequeList.size() > 0) {
            } else {
                ClientUtil.showMessageWindow("Invalid Cheque No !!! ");
                txtChequeNo.setText("");
            }
        }
    }//GEN-LAST:event_txtChequeNoFocusLost

    private void cboInvestmentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInvestmentTypeFocusLost
        // TODO add your handling code here:
        txtInvestmentIDTransSBorCA.setText("");
        txtInvestmentRefNoTrans.setText("");
        txtInvestmentInternalNoTrans.setText("");
    }//GEN-LAST:event_cboInvestmentTypeFocusLost

    private void btnInvestmentIDTransSBorCAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDTransSBorCAActionPerformed
        // TODO add your handling code here:
        callView("InvestmentRefNoTrans");
    }//GEN-LAST:event_btnInvestmentIDTransSBorCAActionPerformed

    private void btnInvestmentIDTrans1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDTrans1ActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboInvestmentType.getSelectedItem()).length() > 0) {
            callView("InvestmentProductSBorCATrans");
        } else {
            ClientUtil.displayAlert("Please Select Investment Type");
        }
    }//GEN-LAST:event_btnInvestmentIDTrans1ActionPerformed
    private void setTransOtherBankDetails() {
        transactionUI.setSourceScreen("INVESTMENT");
        HashMap otherBankachdMap = new HashMap();
        String otherBankAccHead = "";
        otherBankachdMap.put("INVESTMENT_PROD_ID", txtInvestmentIDTransSBorCA.getText());
        List otherBankAchdLst = ClientUtil.executeQuery("getSelectinvestmentAccountHead", otherBankachdMap);
        if (otherBankAchdLst != null && otherBankAchdLst.size() > 0) {
            otherBankachdMap = (HashMap) otherBankAchdLst.get(0);
            otherBankAccHead = CommonUtil.convertObjToStr(otherBankachdMap.get("IINVESTMENT_AC_HD"));
            SetTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
            observable.setCallingTransAcctNo(otherBankAccHead);
            transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
        }
    }

    private void setTransChargeDetails() {
        transactionUI.setSourceScreen("INVESTMENT_CHARGE");
        HashMap otherBankachdMap = new HashMap();
        String chargeAccHead = "";
        otherBankachdMap.put("INVESTMENT_PROD_ID", txtInvestmentID.getText());
        List otherBankAchdLst = ClientUtil.executeQuery("getSelectinvestmentAccountHead", otherBankachdMap);
        if (otherBankAchdLst != null && otherBankAchdLst.size() > 0) {
            otherBankachdMap = (HashMap) otherBankAchdLst.get(0);
            chargeAccHead = CommonUtil.convertObjToStr(otherBankachdMap.get("CHARGE_PAID_AC_HD"));
            SetTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
            observable.setCallingTransAcctNo(chargeAccHead);
            transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
        }
    }
    private void rdoSBorCA_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSBorCA_noActionPerformed
        // TODO add your handling code here:
        if (rdoSBorCA_no.isSelected() == true) {
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
            panInvestSBorCATrans.setVisible(false);
            cboInvestmentType.setSelectedItem("");
            txtInvestmentIDTrans.setText("");
            txtInvestmentIDTransSBorCA.setText("");
            txtInvestmentRefNoTrans.setText("");
            txtInvestmentInternalNoTrans.setText("");
            SetTransAmount();
            transactionUI.setCallingTransType("CASH");
            String txtBehaves = observable.callForBehaves();
            if (rdoInterest.isSelected() == true && txtBehaves.equals("RESERVE_FUND_DCB")) {
                transactionUI.cancelAction(false);
                transactionUI.resetObjects();
                tabClosingType.remove(panTransaction);
            }
        }
    }//GEN-LAST:event_rdoSBorCA_noActionPerformed

    private void rdoSBorCA_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSBorCA_yesActionPerformed
        // TODO add your handling code here:
        String bheaves = observable.callForBehaves();
        if (rdoSBorCA_yes.isSelected() == true) {
            panInvestSBorCATrans.setVisible(true);
            if (rdoInterest.isSelected() == true || rdoCharges.isSelected() == true
                    || (rdoSale.isSelected() == true && (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")))) {
                txtChequeNo.setText("");
                txtChequeNo.setVisible(false);
                lblChequeNo.setVisible(false);
            } else {
                txtChequeNo.setVisible(true);
                lblChequeNo.setVisible(true);
            }
            if (rdoSale.isSelected() == true && (bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_SPD"))) {
                txtChequeNo.setText("");
                txtChequeNo.setVisible(false);
                lblChequeNo.setVisible(false);
            }
            ClientUtil.enableDisable(panInvestSBorCATrans, false);
            txtChequeNo.setEnabled(true);
            cboInvestmentType.setEnabled(true);
            btnInvestmentIDTrans.setEnabled(true);
            SetTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
        } else {
            panInvestSBorCATrans.setVisible(false);
            SetTransAmount();
            transactionUI.setCallingTransType("CASH");
        }
    }//GEN-LAST:event_rdoSBorCA_yesActionPerformed

    private void txtDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountFocusLost

    private void btnAccRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccRefNoActionPerformed
        // TODO add your handling code here:
        callView("InvestmentAccNo");
        String bheaves = "";
        bheaves = observable.callForBehaves();
//        btnAdd.setVisible(true);
        if (rdoInterest.isSelected() == true && tdtUpToIntRecivedDt.getDateValue().length() > 0 && (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD"))) {
            Date maturityDate = null;
            tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(curDate));
            maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
            if (DateUtil.dateDiff(curDate, maturityDate) < 0) {
                tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(maturityDate));
                tdtUpToIntRecivedDt.setEnabled(false);
                ClientUtil.showMessageWindow("Investment is matured. Cannot change interest received up to date ");
            } else {
                tdtUpToIntRecivedDt.setEnabled(true);
            }
            calcFixedInterest();
            txtBrokenPeriodIntAmountFocusLost(null);
            if (rdoInterest.isSelected() == true && bheaves.equals("OTHER_BANK_FD") && lblLastIntPaidDateValue.getText().length() > 0) {// Added by nithya on 17-12-2020 for KD-2462
              ClientUtil.showMessageWindow("Last Interest Paid Date : " + lblLastIntPaidDateValue.getText());
            }
        }
    }//GEN-LAST:event_btnAccRefNoActionPerformed

    private void calcFixedInterest() {
        if (lblFaceValueValue.getText().length() > 0 && lblCoupenValue.getText().length() > 0 && txtInternalAccNo.getText().length() > 0) {
            HashMap hashmap = new HashMap();
            hashmap.put("INVESTMENT_ID", txtInternalAccNo.getText());
            List depList = ClientUtil.executeQuery("getDepDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                double intAmt = 0.0;
                double finalIntAmt = 0.0;
                double amt = 0.0;
                double period = 0.0;
                java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtUpToIntRecivedDt.getDateValue()));
                java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                period = DateUtil.dateDiff(effectiveDate, matDate);
                System.out.println("#############  period " + period);
                if (lblInterestPaymentFrequencyValue.getText().equals("Monthly") && period > 29) {
                    System.out.println("#############  Monthly ");
                    double roi = 0.0;
                    double amount = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                    roi = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("RATE_OF_INTEREST"))).doubleValue();
                    period = period / 30;
                    intAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                    System.out.println("intAmt: " + intAmt);
                    double calcAmt = amount / 100;
                    intAmt = intAmt * calcAmt;
                    intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                    intAmt = intAmt * period;
                } else {
                    System.out.println("#############  Not_Monthly ");
                    double principal = 0.0;
                    double rateOfInterest = 0.0;
                    double years = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_YY"))).doubleValue();
                    double months = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_MM"))).doubleValue();
                    principal = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
                    rateOfInterest = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("RATE_OF_INTEREST"))).doubleValue();
                    years = years * 12;
                    double total = years + months;
                    double greateramount = principal + (principal * rateOfInterest * total / 1200);
                    double interestgreater = greateramount - principal;
                    double days = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_DD"))).doubleValue();
                    double lessamount = principal + (principal * rateOfInterest * days / 36500);
                    double interestless = lessamount - principal;
                    intAmt = interestgreater + interestless;
                }
                intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                System.out.println("intAmt: " + intAmt);
                finalIntAmt = intAmt - CommonUtil.convertObjToDouble(hashmap.get("INTEREST_RECEIVED")).doubleValue();
                txtBrokenPeriodIntAmount.setText(String.valueOf(finalIntAmt));
            }
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    private void btnInvestmentIDTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDTransActionPerformed
        // TODO add your handling code here:
        callView("InvestmentProductTrans");
    }//GEN-LAST:event_btnInvestmentIDTransActionPerformed

    private void btnViewTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTransActionPerformed
        // TODO add your handling code here:
        if (tdtFromDate.getDateValue().equals("") || tdtToDate.getDateValue().equals("") || CommonUtil.convertObjToStr(txtInvestmentNameTrans.getText()).length() <= 0
                || CommonUtil.convertObjToStr(txtInvestmentIDTrans.getText()).length() <= 0) {
            ClientUtil.displayAlert("Inveatment Type ,Investment Id ,Investment Name ,From Date , To Date Should be proper Value");
        } else {
            if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())) > 0) {
                tdtToDate.setDateValue("");
                ClientUtil.displayAlert(" To Date Should be Greater or Equal to From Date ");
            } else {
                HashMap whereMap = new HashMap();
                observable.resetTable();
                observable.resetForm();
                reSetClose();
                whereMap.put("INVESTMENT_NAME", CommonUtil.convertObjToStr(txtInvestmentNameTrans.getText()));
                whereMap.put("INVESTMENT_REF_NO", CommonUtil.convertObjToStr(txtTranAccRefNo.getText()));
                whereMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(txtInvestmentIDTrans.getText()));
                whereMap.put("TRANSACTION", "TRANSACTION");
                whereMap.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                whereMap.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
                whereMap.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(txtInvestmentNameTrans.getText()));
                observable.populateData(whereMap);
                reSetValue();
                //                   lblBalanceValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.getOutstandingAmount())));
                ClientUtil.enableDisable(panInvestmentTrans, false);

            }

        }
    }//GEN-LAST:event_btnViewTransActionPerformed
    private void reSetClose() {
//        lblBalanceValue.setText("");
//        lblClosedDateValue.setText("");
//        lblCloserTypeValue.setText("");
//        lbltrnCloserTypeValue.setText("");
//        lblInvestStatusValue.setText("");
        visbileFalseTrueClose(false);

    }

    private void depositDetails(HashMap hashmap) {
        if (hashmap.containsKey("INVESTMENT_ID")) {
            List depList = ClientUtil.executeQuery("getDepDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                commonDeatilsEnableDisable(true);
                double availableBalance = 0.0;
                lblInterestPaymentFrequency.setVisible(true);
                lblInterestPaymentFrequencyValue.setVisible(true);
                lblTotalInvestmentAmount.setVisible(true);
                lblTotalInvestmentAmountValue.setVisible(true);
                lblMaturityAmt.setVisible(true);
                lblMaturityAmtValue.setVisible(true);
                lblFaceValue.setVisible(true);
                lblFaceValueValue.setVisible(true);
                lblIssueDate.setText("Deposit Date");
                lblFaceValue.setText("Deposit Amount Rs.");
                lblCoupenRate.setText("Rate Of Interest :");
                lblTotalInvestmentAmount.setText("Outstanding Balance Rs.");
                lblPeriod.setText("Deposit Period :");
                lblTotalInterestCollected.setText("Total Interest Received :");
                int intPayFreq = 0;
                String year = "";
                String month = "";
                String days = "";
                lblIssueDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                lblFaceValueValue.setText(CommonUtil.convertObjToStr(hashmap.get("PRINCIPAL_AMOUNT")));
                lblLastIntPaidDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("INT_REC_TILL_DT")));
                intPayFreq = CommonUtil.convertObjToInt(hashmap.get("INTPAY_FREQ"));
                if (intPayFreq == 0) {
                    lblInterestPaymentFrequencyValue.setText("Date Of Maturity");
                } else if (intPayFreq == 30) {
                    lblInterestPaymentFrequencyValue.setText("Monthly");
                } else if (intPayFreq == 90) {
                    lblInterestPaymentFrequencyValue.setText("Quarterly");
                } else if (intPayFreq == 180) {
                    lblInterestPaymentFrequencyValue.setText("Half Yeary");
                } else if (intPayFreq > 180) {
                    lblInterestPaymentFrequencyValue.setText("Yeary");
                }
                year = CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_YY"));
                month = CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_MM"));
                days = CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_PERIOD_DD"));
                String period = year + " Years  " + month + " Months  " + days + " Days";
                lblPeriodValue.setText(period);
                lblCoupenValue.setText(CommonUtil.convertObjToStr(hashmap.get("RATE_OF_INTEREST") + " %"));
                lblMaturityDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("MATURITY_DT")));
                availableBalance = CommonUtil.convertObjToDouble(hashmap.get("AVAILABLE_BALANCE")).doubleValue();
                lblTotalInvestmentAmountValue.setText(String.valueOf(availableBalance));
                lblTotalInterestCollectedValue.setText(CommonUtil.convertObjToStr(hashmap.get("INTEREST_RECEIVED")));
                lblTotalInterestCollected.setText("Total Interest Received Rs.");
                lblMaturityAmt.setText("Interest Receivable Rs.");
                lblMaturityAmtValue.setText(CommonUtil.convertObjToStr(hashmap.get("INTEREST_RECEIVABLE")));
            }
        }
    }

    private void operativeDetails(HashMap hashmap) {
        if (hashmap.containsKey("INVESTMENT_ID")) {
            List depList = ClientUtil.executeQuery("getOperativeDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                double availableBalance = 0.0;
                lblInterestPaymentFrequency.setVisible(true);
                lblInterestPaymentFrequencyValue.setVisible(true);
                lblTotalInvestmentAmount.setVisible(true);
                lblTotalInvestmentAmountValue.setVisible(true);
                lblFaceValue.setVisible(true);
                lblFaceValueValue.setVisible(true);
                lblIssueDate.setText("A/c Open Date");
                lblTotalInvestmentAmount.setText("Outstanding Balance Rs.");
                lblIssueDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                lblInterestPaymentFrequency.setText("Operator Details :");
                lblInterestPaymentFrequencyValue.setText(CommonUtil.convertObjToStr(hashmap.get("OPERATOR_DETAILS")));
                availableBalance = CommonUtil.convertObjToDouble(hashmap.get("AVAILABLE_BALANCE")).doubleValue();
                lblTotalInvestmentAmountValue.setText(String.valueOf(availableBalance));
                lblFaceValue.setVisible(false);
                lblFaceValueValue.setVisible(false);
                commonDeatilsEnableDisable(false);
            }
        }
    }

    private void reserveFundDetails(HashMap hashmap) {
        if (hashmap.containsKey("INVESTMENT_ID")) {
            List depList = ClientUtil.executeQuery("getReserveFundDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                double availableBalance = 0.0;
                lblInterestPaymentFrequency.setVisible(false);
                lblInterestPaymentFrequencyValue.setVisible(false);
                lblTotalInvestmentAmount.setVisible(true);
                lblTotalInvestmentAmountValue.setVisible(true);
                lblFaceValue.setVisible(true);
                lblFaceValueValue.setVisible(true);
                lblFaceValue.setText("Face Value Rs.");
                lblIssueDate.setText("A/c Open Date");
                lblTotalInvestmentAmount.setText("Outstanding Balance Rs.");
                lblIssueDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                lblFaceValueValue.setText(CommonUtil.convertObjToStr(hashmap.get("AMOUNT")));
                availableBalance = CommonUtil.convertObjToDouble(hashmap.get("AVAILABLE_BALANCE")).doubleValue();
                lblTotalInvestmentAmountValue.setText(String.valueOf(availableBalance));
                commonDeatilsEnableDisable(false);
            }
        }
    }

    private void shareDetails(HashMap hashmap) {
        if (hashmap.containsKey("INVESTMENT_ID")) {
            List depList = ClientUtil.executeQuery("getShareDetailsFromMaster", hashmap);
            if (depList != null && depList.size() > 0) {
                hashmap = (HashMap) depList.get(0);
                double availableBalance = 0.0;
                double faceValue = 0.0;
                double totalShareValue = 0.0;
                commonDeatilsEnableDisable(false);
                lblInterestPaymentFrequency.setVisible(true);
                lblInterestPaymentFrequencyValue.setVisible(true);
                lblTotalInvestmentAmount.setVisible(true);
                lblTotalInvestmentAmountValue.setVisible(true);
                lblFaceValue.setVisible(false);
                lblFaceValueValue.setVisible(false);
                lblCoupenRate.setVisible(true);
                lblPeriod.setVisible(true);
                lblPeriodValue.setVisible(true);
                lblCoupenValue.setVisible(true);
                lblIssueDate.setText("Share A/c Open Date");
                lblInterestPaymentFrequency.setText("No Of Shares :");
                lblInterestPaymentFrequencyValue.setText(CommonUtil.convertObjToStr(hashmap.get("NO_OF_SHARES")));
                lblPeriod.setText("Share Face Value Rs.");
                faceValue = CommonUtil.convertObjToDouble(hashmap.get("FACE_VALUE")).doubleValue();
                lblPeriodValue.setText(String.valueOf(faceValue));
                lblCoupenRate.setText("Total Share Value Rs.");
                totalShareValue = CommonUtil.convertObjToDouble(hashmap.get("SHARE_VALUE")).doubleValue();
                lblCoupenValue.setText(String.valueOf(totalShareValue));
                lblTotalInvestmentAmount.setText("Outstanding Balance Rs.");
                lblIssueDateValue.setText(CommonUtil.convertObjToStr(hashmap.get("INVESTMENT_ISSUE_DT")));
                availableBalance = CommonUtil.convertObjToDouble(hashmap.get("AVAILABLE_BALANCE")).doubleValue();
                lblTotalInvestmentAmountValue.setText(String.valueOf(availableBalance));
                if (rdoPurchase.isSelected() == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    txtNoOfUnits.setText(CommonUtil.convertObjToStr(hashmap.get("NO_OF_SHARES")));
                    txtPurchaseRate.setText(CommonUtil.convertObjToStr(hashmap.get("FEES_PAID")));
                    txtNoOfUnitsFocusLost(null);
                }
            }
        }
    }

    private void commonDeatilsEnableDisable(boolean flag) {
        lblPeriod.setVisible(flag);
        lblPeriodValue.setVisible(flag);
        lblCoupenRate.setVisible(flag);
        lblMaturityAmt.setVisible(flag);
        lblCoupenValue.setVisible(flag);
        lblMaturityDate.setVisible(flag);
        lblMaturityAmtValue.setVisible(flag);
        lblMaturityDateValue.setVisible(flag);
        lblTotalInterestCollected.setVisible(flag);
        lblTotalInterestCollectedValue.setVisible(flag);

    }

    private void visbileFalseTrueClose(boolean val) {
//        lblClosedDateValue.setVisible(val);
//        lbltrnCloserTypeValue.setVisible(val);
//        lblCloserRateValue.setVisible(val);
//        lblClosedDate.setVisible(val);
//        lblTrnCloserType.setVisible(val);
//        lblCloserRate.setVisible(val);
    }

    private void reSetValue() {
//        if((observable.callForBehaves().equals("OTHER_BANK_CCD") || observable.callForBehaves().equals("OTHER_BANK_FD") || observable.callForBehaves().equals("OTHER_BANK_SSD")) && observable.getCloserDate()!=null){
//            visbileFalseTrueClose(true);
//            lblClosedDateValue.setText(DateUtil.getStringDate(observable.getCloserDate()));
//            lbltrnCloserTypeValue.setText(observable.getCloserType());
//            if(observable.getPreCloserRate().length()>0)
//                lblCloserRateValue.setText(observable.getPreCloserRate());
//            else
//                lblCloserRateValue.setText(CommonUtil.convertObjToStr(observable.getCouponRate()));
//            lblInvestStatusValue.setText(observable.getInvestmentStatus());
//        }
//        lblInvestStatusValue.setText(observable.getInvestmentStatus());
//        if(CommonUtil.convertObjToStr(observable.getOutstandingAmount()).length()>0)
//            lblBalanceValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.getOutstandingAmount())));
//        
    }
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetTable();
        observable.resetForm();
        reSetClose();
        cboInvestmentBehavesTrans.setSelectedItem("");
        txtInvestmentIDTrans.setText("");
        txtInvestmentNameTrans.setText("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        txtTranAccRefNo.setText("");
        txtTranInternalAccNo.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void rdoPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPurchaseActionPerformed
        // TODO add your handling code here:
        interestMethod(true);
        if (rdoPurchase.isSelected() == true) {
            rdoSale.setSelected(false);
            rdoInterest.setSelected(false);
            rdoCloseYesOrNo.setVisible(false);
            rdoCloseYesOrNo.setSelected(false);
            ClientUtil.enableDisable(panInvestmentTrans, true);
            ClientUtil.enableDisable(panDepositDetails1, false);
            ClientUtil.enableDisable(panPurchaseOrSale, false);
            txtInvestmentID.setEditable(false);
            txtInvestmentName.setEditable(false);
            changeFieldNamesAccordingTOBehavesAndRdo();
            String txtBehaves = observable.callForBehaves();
            if (txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) {
                panInvestmentSBorCATrans.setVisible(false);
                clearTransDetails();
            } else {
                panInvestmentSBorCATrans.setVisible(true);
                rdoSBorCA_noActionPerformed(null);
            }
            if ((txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) && rdoPurchase.isSelected() == true) {
                panInvestmentSBorCATrans.setVisible(true);
            }
        }
    }//GEN-LAST:event_rdoPurchaseActionPerformed

    private void rdoPurchaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoPurchaseFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPurchaseFocusLost

    private void rdoSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSaleActionPerformed
        // TODO add your handling code here:
        interestMethod(true);
        if (rdoSale.isSelected() == true) {
            rdoPurchase.setSelected(false);
            rdoInterest.setSelected(false);
            rdoCloseYesOrNo.setVisible(true);
            rdoCloseYesOrNo.setVisible(false);
            ClientUtil.enableDisable(panInvestmentTrans, true);
            ClientUtil.enableDisable(panDepositDetails1, false);
            ClientUtil.enableDisable(panPurchaseOrSale, false);
            txtInvestmentID.setEditable(false);
            txtInvestmentName.setEditable(false);
            panInvestmentSBorCATrans.setVisible(false);
        }
        changeFieldNamesAccordingTOBehavesAndRdo();
    }//GEN-LAST:event_rdoSaleActionPerformed

    private void rdoInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestActionPerformed
        // TODO add your handling code here:
        interestMethod(false);
        if (rdoInterest.isSelected() == true) {
            rdoSale.setSelected(false);
            rdoPurchase.setSelected(false);
            rdoCloseYesOrNo.setVisible(false);
            rdoCloseYesOrNo.setSelected(false);
            ClientUtil.enableDisable(panInvestmentTrans, true);
            ClientUtil.enableDisable(panDepositDetails1, false);
            ClientUtil.enableDisable(panPurchaseOrSale, false);
            txtInvestmentID.setEditable(false);
            txtInvestmentName.setEditable(false);
            panInvestmentSBorCATrans.setVisible(false);
            tdtTransactionDt.setEnabled(true);
            if(!(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals(""))){
                tdtTransactionDt.setDateValue(DateUtil.getStringDate(curDate));
            }
            txtInvestTDS.setVisible(true);
            lblTdsAmount.setVisible(true);
            ClientUtil.enableDisable(panTransAdd, false);
        }
        changeFieldNamesAccordingTOBehavesAndRdo();
        String txtBehaves = "";
        txtBehaves = observable.callForBehaves();
        if (rdoInterest.isSelected() == true && (txtBehaves.equals("OTHER_BANK_FD") || txtBehaves.equals("OTHER_BANK_SSD"))) {
            panInvestmentSBorCATrans.setVisible(true);
            rdoSBorCA_noActionPerformed(null);
        } else if (rdoInterest.isSelected() == true && txtBehaves.equals("RESERVE_FUND_DCB")) {
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            tabClosingType.remove(panTransaction);
        } else {
            panInvestmentSBorCATrans.setVisible(false);
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
        }
    }//GEN-LAST:event_rdoInterestActionPerformed

    private void rdoInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoInterestFocusLost
        // TODO add your handling code here:
        interestMethod(false);
        changeFieldNamesAccordingTOBehavesAndRdo();
    }//GEN-LAST:event_rdoInterestFocusLost

    private void txtPurchaseRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseRateFocusLost
        // TODO add your handling code here:
        if (txtPurchaseRate.getText().length() > 0) {
            double totalShareAmt = 0.0;
            totalShareAmt = (CommonUtil.convertObjToDouble(txtNoOfUnits.getText()).doubleValue() * CommonUtil.convertObjToDouble(lblPeriodValue.getText()).doubleValue())
                    + CommonUtil.convertObjToDouble(txtPurchaseRate.getText()).doubleValue();
            txtInvestmentAmount.setText(String.valueOf(totalShareAmt));
            txtInvestmentAmountFocusLost(null);
        }
    }//GEN-LAST:event_txtPurchaseRateFocusLost

    private void txtNoOfUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfUnitsActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtPurchaseRate.getText()).length() < 0 && CommonUtil.convertObjToStr(txtPurchaseRate.getText()).equals("")) {
            ClientUtil.displayAlert("Please Enter Purchase Rate");
        }
    }//GEN-LAST:event_txtNoOfUnitsActionPerformed

    private void txtNoOfUnitsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfUnitsFocusLost
        // TODO add your handling code here:
        if (txtNoOfUnits.getText().length() > 0) {
            double totalShareAmt = 0.0;
            totalShareAmt = (CommonUtil.convertObjToDouble(txtNoOfUnits.getText()).doubleValue() * CommonUtil.convertObjToDouble(lblPeriodValue.getText()).doubleValue())
                    + CommonUtil.convertObjToDouble(txtPurchaseRate.getText()).doubleValue();
            txtInvestmentAmount.setText(String.valueOf(totalShareAmt));
            txtInvestmentAmountFocusLost(null);
        }
        if (txtNoOfUnits.getText().length() > 0 && rdoSale.isSelected() == true && rdoSBorCA_no.isSelected() == true) {
            rdoSBorCA_noActionPerformed(null);
        }
        if (txtNoOfUnits.getText().length() > 0 && rdoSale.isSelected() == true && rdoSBorCA_yes.isSelected() == true) {
            rdoSBorCA_yesActionPerformed(null);
        }
    }//GEN-LAST:event_txtNoOfUnitsFocusLost

    private void txtInvestmentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentAmountFocusLost
        // TODO add your handling code here:
        SetTransAmount();
        String bheaves = observable.callForBehaves();
        lblTotalAmountValue.setText(CommonUtil.convertObjToStr(txtInvestmentAmount.getText()));
    }//GEN-LAST:event_txtInvestmentAmountFocusLost

    private void txtBrokenPeriodIntAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokenPeriodIntAmountFocusLost
        // TODO add your handling code here:
        lblTotalAmountValue.setText(CommonUtil.convertObjToStr(txtBrokenPeriodIntAmount.getText()));
        String bheaves = observable.callForBehaves();
        if ((bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) && rdoInterest.isSelected() == true) {
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setMainEnableDisable(false);
            tabClosingType.remove(panTransaction);
        } else if (bheaves.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true && intWithPrincipal.equals("N") && withOrWithoutInterest.equals("Y")) {
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setMainEnableDisable(false);
            tabClosingType.remove(panTransaction);
        } else if (rdoInterest.isSelected() == true && bheaves.equals("RESERVE_FUND_DCB") && rdoSBorCA_no.isSelected() == true) {
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            tabClosingType.remove(panTransaction);
        } else {
            tabClosingType.add(panTransaction, "Transaction");
            SetTransAmount();
        }
    }//GEN-LAST:event_txtBrokenPeriodIntAmountFocusLost

    private void tdtPurchaseDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPurchaseDtFocusLost
        // TODO add your handling code here:
        if (tdtPurchaseDt.getDateValue().length() > 0 && !tdtPurchaseDt.getDateValue().equals("") && lblIssueDateValue.getText().length() > 0 && lblMaturityDateValue.getText().length() > 0) {
            Date mtDat = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
            Date issueDat = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblIssueDateValue.getText()));
            Date purchaseDate = DateUtil.getDateMMDDYYYY(tdtPurchaseDt.getDateValue());
            if (DateUtil.dateDiff(issueDat, purchaseDate) < 0) {
                if (rdoPurchase.isSelected() == true) {
                    ClientUtil.displayAlert("Deposit Date Should be Greater Than issue Date");
                }
                if (rdoSale.isSelected() == true) {
                    ClientUtil.displayAlert("Sale Date Should be Greater Than issue Date");
                }
                if (rdoInterest.isSelected() == true) {
                    ClientUtil.displayAlert("Interest Date Should be Greater Than issue Date");
                }
                tdtPurchaseDt.setDateValue(null);
                tdtPurchaseDt.requestFocus();
            }

            if (DateUtil.dateDiff(curDate, purchaseDate) > 0) {
                if (rdoPurchase.isSelected() == true) {
                    ClientUtil.displayAlert("Purchase Date Should be Less than or equal Application Date");
                }
                if (rdoSale.isSelected() == true) {
                    ClientUtil.displayAlert("Sale Date Should be Less than or equal Application Date");
                }
                if (rdoInterest.isSelected() == true) {
                    ClientUtil.displayAlert("Interest Date Should be Less than or equal Application Date");
                }
                tdtPurchaseDt.setDateValue(null);
                tdtPurchaseDt.requestFocus();
                purchaseDate = null;

            }
            if (purchaseDate != null && DateUtil.dateDiff(purchaseDate, mtDat) > 0) {
                String behaves = observable.callForBehaves();
                if (behaves.length() > 0) {
                    if (rdoSale.isSelected() == true && (behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_SSD"))) {
                        lblCloserTypeValue.setText("Premature Closure");
                        lblCloserType.setText("Closer Type :");
                    } else {
                        lblCloserTypeValue.setText("");
                    }
                }

            } else {
                if (purchaseDate != null) {
                    String behaves = observable.callForBehaves();
                    if (behaves.length() > 0) {
                        if (rdoSale.isSelected() == true && (behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_SSD"))) {
                            lblCloserTypeValue.setText("Normal Closure");
                            lblCloserType.setText("Closer Type :");
                        } else {
                            lblCloserTypeValue.setText("");
                        }

                    }
                } else {
                    lblCloserTypeValue.setText("");
                }
            }
        }
    }//GEN-LAST:event_tdtPurchaseDtFocusLost

    private void rdoMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMarketActionPerformed
        // TODO add your handling code here:
        if (rdoMarket.isSelected() == true) {
            txtBrokerName.setText("");
            txtBrokerCommission.setText("");
            txtBrokerName.setVisible(true);
            txtBrokerName.setEnabled(true);
            txtBrokerCommission.setVisible(false);
            lblBrokerName.setVisible(true);
            lblBrokerCommission.setVisible(false);
            rdoOffMarket.setSelected(false);
        }
    }//GEN-LAST:event_rdoMarketActionPerformed

    private void rdoMarketFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoMarketFocusLost
        // TODO add your handling code here:
        if (rdoMarket.isSelected() == true && CommonUtil.convertObjToStr(txtBrokerName.getText()).length() < 0
                && CommonUtil.convertObjToStr(txtBrokerName.getText()).equals("")) {
            ClientUtil.displayAlert("Please Enter The Broker Name");
            txtBrokerName.setEnabled(true);
        }
    }//GEN-LAST:event_rdoMarketFocusLost

    private void rdoOffMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOffMarketActionPerformed
        // TODO add your handling code here:
        if (rdoOffMarket.isSelected() == true) {
            txtBrokerName.setText("");
            txtBrokerCommission.setText("");
            txtBrokerName.setVisible(false);
            txtBrokerCommission.setVisible(false);
            lblBrokerName.setVisible(false);
            lblBrokerCommission.setVisible(false);
            rdoMarket.setSelected(false);
        }
    }//GEN-LAST:event_rdoOffMarketActionPerformed

    private void rdoPrimaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPrimaryActionPerformed
        // TODO add your handling code here:
        if (rdoPrimary.isSelected() == true) {
            rdoSecondary.setSelected(false);
            ClientUtil.enableDisable(panPurchaseThrough, false);
            rdoMarket.setSelected(false);
            rdoOffMarket.setSelected(false);
            txtBrokerName.setText("");
            txtBrokerCommission.setText("");
            txtBrokerName.setVisible(false);
            txtBrokerCommission.setVisible(false);
            lblBrokerName.setVisible(false);
            lblBrokerCommission.setVisible(false);
        }
    }//GEN-LAST:event_rdoPrimaryActionPerformed

    private void rdoSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSecondaryActionPerformed
        // TODO add your handling code here:
        if (rdoSecondary.isSelected() == true) {
            rdoPrimary.setSelected(false);
            ClientUtil.enableDisable(panPurchaseThrough, true);
        }
    }//GEN-LAST:event_rdoSecondaryActionPerformed

    private void txtBrokerCommissionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerCommissionFocusLost
        // TODO add your handling code here:
        callCulation();
    }//GEN-LAST:event_txtBrokerCommissionFocusLost

    private void rdoCloseYesOrNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCloseYesOrNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCloseYesOrNoActionPerformed
    private boolean rdoClosedCheck() {
        double investAmt = CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue();
        double outAmt = CommonUtil.convertObjToDouble(lblTotalInvestmentAmountValue.getText()).doubleValue();
        boolean check = true;
        if (rdoSale.isSelected() == true) {
            if (investAmt > outAmt) {
                rdoCloseYesOrNo.setSelected(false);
                observable.setCloseStatus("");
                check = true;
            } else if (investAmt == outAmt) {
                rdoCloseYesOrNo.setSelected(true);
                observable.setCloseStatus("");
                check = false;
            } else if (investAmt < outAmt) {
                rdoCloseYesOrNo.setSelected(false);
                observable.setCloseStatus("");
                check = false;
            }
        }
        return check;
    }
    private void cboInvestmentBehavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesActionPerformed
        //        // TODO add your handling code here:
        String bheaves = "";
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        bheaves = observable.callForBehaves();
        if (bheaves.length() > 0) {
            if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")) {
                rdoPurchase.setText("Deposit");
                rdoSale.setText("Closure");
                rdoInterest.setText("Interest");
                rdoCharges.setVisible(false);
                if (bheaves.equals("OTHER_BANK_CCD")) {
                    rdoCharges.setVisible(true);
                    rdoCharges.setText("Renewal");
                }
            } else if (bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) {
                rdoPurchase.setText("Deposit");
                rdoSale.setText("Withdrawal");
                rdoInterest.setText("Interest");
                rdoCharges.setText("Charges/Other Debits");
                rdoCharges.setVisible(true);
            } else if (bheaves.equals("RESERVE_FUND_DCB")) {
                rdoPurchase.setText("Deposit");
                rdoSale.setText("Withdrawal");
                rdoInterest.setText("Interest");
                rdoCharges.setVisible(false);
            } else if (bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                rdoPurchase.setText("Purchase");
                rdoSale.setText("Withdrawal");
                rdoInterest.setText("Divident");
                rdoCharges.setVisible(false);
            }
            lblDebitChequeNo.setVisible(false);
            txtDebitChequeNo.setVisible(false);
            lblTotalPremiumPaid.setVisible(false);
            lblTotalPremiumPaidValue.setVisible(false);
            lblTotalPremiumCollected.setVisible(false);
            lblTotalPremiumCollectedValue.setVisible(false);
            cboInvestmentBehaves.setEnabled(false);
            txtInvestmentID.setText("");
            btnInvestmentID.setEnabled(false);
            ClientUtil.enableDisable(panPurchaseOrSale, true);
            tdtTransactionDt.setEnabled(false);
        }
    }//GEN-LAST:event_cboInvestmentBehavesActionPerformed

    private void cboInvestmentBehavesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesFocusLost
        // TODO add your handling code here:
        callCulation();

    }//GEN-LAST:event_cboInvestmentBehavesFocusLost

    private void changeFieldNamesAccordingTOBehavesAndRdo() {
        String txtBehaves = "";
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        txtBehaves = observable.callForBehaves();
        if (txtBehaves.equals("OTHER_BANK_FD") || txtBehaves.equals("OTHER_BANK_CCD") || txtBehaves.equals("OTHER_BANK_RD") || txtBehaves.equals("OTHER_BANK_SSD")
                || txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD") || txtBehaves.equals("RESERVE_FUND_DCB")) {
            visibleTrueOrfalse(false);
            txtInvestmentAmount.setVisible(true);
            lblInvestmentAmount.setVisible(true);
            lblUpToIntRecivedDt.setText("Int Received Upto");
            if (rdoPurchase.isSelected() == true) {
                lblPurchaseDt.setText("Deposit Date");
                lblUpToIntRecivedDt.setVisible(false);
                tdtUpToIntRecivedDt.setVisible(false);
                txtBrokenPeriodIntAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setVisible(false);
                lblInvestmentAmount.setText("Deposit Amount");
            } else if (rdoSale.isSelected() == true) {
                lblUpToIntRecivedDt.setVisible(false);
                tdtUpToIntRecivedDt.setVisible(false);
                txtBrokenPeriodIntAmount.setVisible(true);
                lblBrokenPeriodIntAmount.setVisible(true);
                lblPurchaseDt.setText("Withdrawal Date");
                lblBrokenPeriodIntAmount.setText("Total interest received");
                lblInvestmentAmount.setText("Withdrawal Amount");
                txtBrokenPeriodIntAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setVisible(false);
            } else if (rdoInterest.isSelected() == true) {
                lblPurchaseDt.setText("Interest Date");
                lblUpToIntRecivedDt.setVisible(true);
                tdtUpToIntRecivedDt.setVisible(true);
                txtBrokenPeriodIntAmount.setVisible(true);
                lblBrokenPeriodIntAmount.setVisible(true);
                txtInvestmentAmount.setVisible(false);
                lblInvestmentAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setText("Interest Received");
                txtChequeNo.setText("");
                txtChequeNo.setVisible(false);
                lblChequeNo.setVisible(false);
            } else if (rdoCharges.isSelected() == true) {
                if (txtBehaves.equals("OTHER_BANK_CCD")) {
                    lblPurchaseDt.setText("Renewal Date");
                    lblBrokenPeriodIntAmount.setText("Renewal Amount");
                    lblUpToIntRecivedDt.setVisible(false);
                    tdtUpToIntRecivedDt.setVisible(false);
                    txtChequeNo.setVisible(false);
                    lblChequeNo.setVisible(false);
                } else {
                    lblPurchaseDt.setText("Charge Date");
                    lblBrokenPeriodIntAmount.setText("Charge Amount");
                    lblUpToIntRecivedDt.setVisible(true);
                    tdtUpToIntRecivedDt.setVisible(true);
                }
                txtBrokenPeriodIntAmount.setVisible(true);
                lblBrokenPeriodIntAmount.setVisible(true);
                txtInvestmentAmount.setVisible(false);
                lblInvestmentAmount.setVisible(false);
                panInvestmentSBorCATrans.setVisible(false);
            }
            if (txtBehaves.equals("OTHER_BANK_FD") || txtBehaves.equals("OTHER_BANK_CCD") || txtBehaves.equals("OTHER_BANK_RD") || txtBehaves.equals("OTHER_BANK_SSD")) {
                rdoSale.setText("Closure");
            }
        }
        if (!txtBehaves.equals("") && rdoPurchase.isSelected() == true) {
            lblTransCashOrTransfer.setText("Whether Debit From SB/CA");
        } else if (!txtBehaves.equals("") && (rdoSale.isSelected() == true || rdoInterest.isSelected() == true || rdoCharges.isSelected() == true)) {
            lblTransCashOrTransfer.setText("Whether Credit To SB/CA");
        }

        if ((txtBehaves.equals("OTHER_BANK_CCD") || txtBehaves.equals("OTHER_BANK_RD") || txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) && rdoInterest.isSelected() == true) {
            tabClosingType.remove(panTransaction);
            panInvestmentSBorCATrans.setVisible(true);
        } else {
            tabClosingType.add(panTransaction, "Transaction");
            panInvestmentSBorCATrans.setVisible(true);
        }
        if (txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) {
            rdoCharges.setVisible(true);
            panInvestmentSBorCATrans.setVisible(false);
        } else {
            rdoCharges.setVisible(false);
        }
        if ((txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) && (rdoSale.isSelected() == true || rdoPurchase.isSelected() == true)) {
            panInvestmentSBorCATrans.setVisible(true);
        }
        if ((txtBehaves.equals("OTHER_BANK_SB") || txtBehaves.equals("OTHER_BANK_CA") || txtBehaves.equals("OTHER_BANK_SPD")) && rdoSale.isSelected() == true) {
            lblDebitChequeNo.setVisible(true);
            txtDebitChequeNo.setVisible(true);
            txtChequeNo.setVisible(false);
            lblChequeNo.setVisible(false);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                txtDebitChequeNo.setEnabled(true);
            } else {
                txtDebitChequeNo.setEnabled(false);
            }
        } else {
            txtDebitChequeNo.setText("");
            lblDebitChequeNo.setVisible(false);
            txtDebitChequeNo.setVisible(false);
        }
        if (txtBehaves.equals("OTHER_BANK_CCD")) {
            rdoCharges.setVisible(true);
        }
        if (txtBehaves.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true && intWithPrincipal.equals("N") && withOrWithoutInterest.equals("Y")) {
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setMainEnableDisable(false);
            tabClosingType.remove(panTransaction);
            panInvestmentSBorCATrans.setVisible(false);
        }
        if (txtBehaves.equals("RESERVE_FUND_DCB")) {
            rdoPurchase.setText("Deposit");
            rdoSale.setText("Withdrawal");
            rdoInterest.setText("Interest");
            rdoCharges.setVisible(false);
            lblIssueDate.setText("Issue Date");
            lblFaceValue.setText("Amount Remitted");
        }
        if (txtBehaves.equals("SHARES_DCB") || txtBehaves.equals("SHARE_OTHER_INSTITUTIONS")) {
            txtInvestmentAmount.setVisible(true);
            lblInvestmentAmount.setVisible(true);
            rdoPurchase.setText("Purchase");
            rdoSale.setText("Withdrawal");
            rdoInterest.setText("Divident");
            rdoCharges.setVisible(false);
            visibleTrueOrfalse(false);
            if (rdoPurchase.isSelected() == true) {
                lblPurchaseDt.setText("Share Purchase Date");
                lblUpToIntRecivedDt.setVisible(false);
                tdtUpToIntRecivedDt.setVisible(false);
                txtBrokenPeriodIntAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setVisible(false);
                txtPurchaseRate.setVisible(true);
                lblPurchaseRate.setVisible(true);
                lblInvestmentAmount.setText("Share Amount");
                lblPurchaseRate.setText("Fees Paid");
                lblNoOfUnits.setText("No Of Shares");
                lblPurchaseRate.setVisible(true);
                lblNoOfUnits.setVisible(true);
                txtPurchaseRate.setVisible(true);
                txtNoOfUnits.setVisible(true);
                txtChequeNo.setVisible(true);
                lblChequeNo.setVisible(true);
            } else if (rdoSale.isSelected() == true) {
                lblUpToIntRecivedDt.setVisible(false);
                tdtUpToIntRecivedDt.setVisible(false);
                txtBrokenPeriodIntAmount.setVisible(true);
                lblBrokenPeriodIntAmount.setVisible(true);
                lblPurchaseDt.setText("Withdrawal Date");
                lblBrokenPeriodIntAmount.setText("Total interest received");
                lblInvestmentAmount.setText("Withdrawal Amount");
                txtBrokenPeriodIntAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setVisible(false);
                txtPurchaseRate.setVisible(false);
                lblPurchaseRate.setVisible(false);
                lblNoOfUnits.setText("No Of Shares Withdrawen");
                txtChequeNo.setVisible(false);
                lblChequeNo.setVisible(false);
                txtNoOfUnits.setVisible(true);
                lblNoOfUnits.setVisible(true);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    txtNoOfUnits.setEnabled(true);
                }
                txtInvestmentAmount.setEnabled(false);
            } else if (rdoInterest.isSelected() == true) {
                lblPurchaseDt.setText("Divident Date");
                lblUpToIntRecivedDt.setVisible(true);
                tdtUpToIntRecivedDt.setVisible(true);
                txtBrokenPeriodIntAmount.setVisible(true);
                lblBrokenPeriodIntAmount.setVisible(true);
                txtInvestmentAmount.setVisible(false);
                lblInvestmentAmount.setVisible(false);
                lblBrokenPeriodIntAmount.setText("Divident Amount Received");
                txtChequeNo.setText("");
                txtChequeNo.setVisible(false);
                lblChequeNo.setVisible(false);
                lblUpToIntRecivedDt.setText("Divident Received Upto");
            }
        }
        if ((rdoInterest.isSelected() == true || rdoSale.isSelected() == true) && (txtBehaves.equals("OTHER_BANK_FD")
                || txtBehaves.equals("OTHER_BANK_SSD"))) {
            transactionUI.setProdType();
        } else {
            transactionUI.setResetProdType();
        }
        if ((txtBehaves.equals("OTHER_BANK_FD") || txtBehaves.equals("OTHER_BANK_CCD") || txtBehaves.equals("OTHER_BANK_RD") || txtBehaves.equals("OTHER_BANK_SSD")) && rdoSale.isSelected() == true) {
            lblCloserType.setVisible(true);
            lblCloserTypeValue.setVisible(true);
        } else {
            lblCloserType.setVisible(false);
            lblCloserTypeValue.setVisible(false);
            lblCloserTypeValue.setText("");
        }
    }

    private void visibleTrueOrfalse(boolean value) {
        rdoPrimary.setVisible(value);
        rdoSecondary.setVisible(value);
        rdoMarket.setVisible(value);
        rdoOffMarket.setVisible(value);
        txtNoOfUnits.setVisible(value);
        lblNoOfUnits.setVisible(value);
        txtPurchaseRate.setVisible(value);
        lblPurchaseRate.setVisible(value);
        lblPurchaseorSalBy.setVisible(value);
        txtPurchaseorSalBy.setVisible(value);
        lblDiscount.setVisible(value);
        txtDiscount.setVisible(value);
        lblPremiumAmount.setVisible(value);
        txtPremiumAmount.setVisible(value);
        lblTransTotalInvestmentAmount.setVisible(value);
        txtTotalInvestmentAmount.setVisible(value);
    }
    private void cboInvestmentBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInvestmentBehavesItemStateChanged

    private void txtInvestmentIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentIDFocusLost
        // TODO add your handling code here:
        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_txtInvestmentIDFocusLost

    private void btnInvestmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).length() > 0) {
            callView("InvestmentProduct");
            btnInvestmentID.setEnabled(false);
        } else {
            ClientUtil.displayAlert("Please Select Investment Type");
        }
    }//GEN-LAST:event_btnInvestmentIDActionPerformed

    private void btnInvestmentIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnInvestmentIDFocusLost
        // TODO add your handling code here:
        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_btnInvestmentIDFocusLost

    private void btnNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNewFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewFocusLost
    private void interestMethod(boolean value) {
        panPurchaseMode.setEnabled(value);
        txtBrokerName.setEnabled(value);
        txtPurchaseRate.setEnabled(value);
        txtNoOfUnits.setEnabled(value);
        txtBrokerName.setEditable(value);
        txtPurchaseRate.setEditable(value);
        txtNoOfUnits.setEditable(value);

    }

    private double callPremiumExcessOrShort() {
        double excessOrShort = 0.0;
        double presentPremium = CommonUtil.convertObjToDouble(txtPremiumAmount.getText()).doubleValue();
        double totPremiumCollected = observable.getTotalPremiumCollected().doubleValue();
        double totPremiumPaid = observable.getTotalPremiumPaid().doubleValue();
        excessOrShort = totPremiumCollected + presentPremium - totPremiumPaid;
        return excessOrShort;
    }

    private void callCulation() {
        double faceValue = 0.0;
        if (rdoInterest.isSelected() != true) {
            faceValue = CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
        }
        double purchaseRate = CommonUtil.convertObjToDouble(txtPurchaseRate.getText()).doubleValue();
        double noOfUnits = CommonUtil.convertObjToDouble(txtNoOfUnits.getText()).doubleValue();
        double brokenInt = CommonUtil.convertObjToDouble(txtBrokenPeriodIntAmount.getText()).doubleValue();
        double brokeRComm = CommonUtil.convertObjToDouble(txtBrokerCommission.getText()).doubleValue();
        if (CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()).equals("Bonds") && (noOfUnits == 0)) {
            noOfUnits = 0;
        } else if (noOfUnits == 0) {
            noOfUnits = 1;
        }
        double insAmt = 0.0;
        double disAmt = 0.0;
        double PremiumAmt = 0.0;
        double totInst = 0.0;
        double totAmt = 0.0;
        insAmt = noOfUnits * faceValue;
        if (purchaseRate < faceValue) {
            double disCount = faceValue - purchaseRate;
            disAmt = noOfUnits * disCount;
        } else if (purchaseRate > faceValue) {
            double premium = purchaseRate - faceValue;
            PremiumAmt = premium * noOfUnits;
        }
        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(new Double(insAmt)));
        txtDiscount.setText(CommonUtil.convertObjToStr(new Double(disAmt)));
        txtPremiumAmount.setText(CommonUtil.convertObjToStr(new Double(PremiumAmt)));
        totInst = insAmt + PremiumAmt - disAmt;
        txtTotalInvestmentAmount.setText(CommonUtil.convertObjToStr(new Double(totInst)));
        totAmt = totInst + brokenInt + brokeRComm;
        lblTotalAmountValue.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(lblTotalAmountValue.getText()));


    }

    private void SetTransAmount() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String bheaves = observable.callForBehaves();
            if (bheaves.length() > 0) {
                if ((bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD"))
                        && rdoSale.isSelected() == true) {
                    SetPeriodicTransAmount();
                    if (CommonUtil.convertObjToStr(observable.getInterestType()).equals("Interest Payable")) {
                        SetPayablePeriodicTransAmount();
                    }
                } else {
                    transactionUI.cancelAction(false);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.resetObjects();
                    if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")
                            || bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD") || bheaves.equals("RESERVE_FUND_DCB")
                            || bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                        if (rdoPurchase.isSelected() == true || rdoSale.isSelected() == true) {
                            transactionUI.setCallingAmount(txtInvestmentAmount.getText());
                            txtTransAmt.setText(txtInvestmentAmount.getText());
                            if (rdoSBorCA_yes.isSelected() == true) {
                                transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                            }
                        }
                        if (rdoInterest.isSelected() == true) {
                            transactionUI.setCallingAmount(txtBrokenPeriodIntAmount.getText());
                            txtTransAmt.setText(txtBrokenPeriodIntAmount.getText());
                            if (rdoSBorCA_yes.isSelected() == true) {
                                transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                            }
                        }
                        if (bheaves.equals("OTHER_BANK_CCD") && rdoCharges.isSelected() == true) {
                            transactionUI.setCallingAmount(txtBrokenPeriodIntAmount.getText());
                            txtTransAmt.setText(txtBrokenPeriodIntAmount.getText());
                        }
                    }
                    if ((bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD")) && rdoCharges.isSelected() == true) {
                        transactionUI.setCallingAmount(txtBrokenPeriodIntAmount.getText());
                        txtTransAmt.setText(txtBrokenPeriodIntAmount.getText());
                    }
                    transactionUI.setCallingApplicantName(txtInvestmentName.getText());
                }
            }
        }
    }

    private void clearTransDetails() {
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:]
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnSave.setEnabled(false);
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
        btnAuthorize.setVisible(true);
        btnReject.setVisible(true);
        btnException.setVisible(true);
        setModified(false);
        super.removeEditLock(((ComboBoxModel) cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        rdoSelectectedTrueOrFalse();
        resetTransactionUI();
        lblPeriodValue.setText("");
        ClientUtil.clearAll(this);
        transactionUI.setSourceScreen("INVESTMENT_TRANS");
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panInvestmentTrans, false);
        ClientUtil.enableDisable(panAmortizationDetails, false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        rdoCloseYesOrNo.setSelected(false);
        changeFieldNamesAccordingTOBehavesAndRdo();
        reSetClose();
        observable.setCboInvestmentBehaves("");
        btnInvestmentID.setEnabled(false);
        btnAccRefNo.setEnabled(false);
        ClientUtil.enableDisable(panInvestmentTrans, false);
        panInvestSBorCATrans.setVisible(false);
        btnSave.setEnabled(false);
        btnInvestmentIDTrans.setEnabled(false);
        btnTranAccRefNo.setEnabled(false);
        lblCloserTypeValue.setText("");
        lblPrematureCalculateAmtVal.setText("");
        tableList = null;
        observable.insertTable(tableList);
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
        finalMap = null;
        txtNarration1.setText("");
        txtInvestTDS.setText("");
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable(); 
        }
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
        txtInvestmentName.setEditable(false);
        txtInvestmentName.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboInvestmentBehaves.setEnabled(false);
        transactionUI.setSourceScreen("INVESTMENT_TRANS");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        ClientUtil.enableDisable(panPurchaseOrSale, false);
        panPurchaseOrSale.setEnabled(false);
        btnSave.setEnabled(false);
        ClientUtil.enableDisable(panDepositDetails1, false);
        btnInvestmentID.setEnabled(false);
        btnAccRefNo.setEnabled(false);
        ClientUtil.enableDisable(panInvestmentSBorCATrans, false);
        ClientUtil.enableDisable(panInvestSBorCATrans, false);
        btnInvestmentIDTrans1.setEnabled(false);
        btnInvestmentIDTransSBorCA.setEnabled(false);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void checkPrematureCloser() {
        String behaves = observable.callForBehaves();
        if (behaves.length() > 0) {
            if (behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_SSD")) {
                if (CommonUtil.convertObjToStr(observable.getTran_Code()).equals("Withdrawal") && CommonUtil.convertObjToStr(observable.getPurchaseSaleBy()).length() > 0) {
                    lblCloserTypeValue.setText("Premature Closure");
                    lblCloserType.setText("Closer Type :");
                } else {
                    lblCloserTypeValue.setText("");
                }
            }
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (tblMultiTransTable.getRowCount() <= 0) {
            ClientUtil.showAlertWindow("Please Save Value in the grid...");
            return;
        }
        if (transactionUI.getOutputTO().size() == 0) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            return;
        }
        if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()), curDate) < 0){
                ClientUtil.showMessageWindow("Can't Allow Future Date As Transaction Date!!!"); 
                return;
            }
        }        
        
        if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()), curDate) != 0){                                    
                if (transactionUI.getOutputTO() != null || (transactionUI.getOutputTO()).size() != 0) {
                    if(transactionUI.getCallingTransType()!=null && transactionUI.getCallingTransType().equals("CASH") ){
                        ClientUtil.showMessageWindow("Cash Transaction Not Allowed as BackDated!!Please check the Transaction Date!!");
                        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
                        return;
                    }
                }
            }
        }       
        setModified(false);
        savePerformed();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);

    }//GEN-LAST:event_btnSaveActionPerformed
    private void resetTransactionUI() {
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
//        btnAdd.setVisible(false);
        if (panInvestmentTrans.isShowing() == true) {
            btnAuthorize.setVisible(true);
            btnReject.setVisible(true);
            btnException.setVisible(true);
            setModified(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            observable.resetForm();
            lblPeriodValue.setText("");
            ClientUtil.enableDisable(panInvestmentTrans, false);
            rdoSelectectedTrueOrFalse();
            setButtonEnableDisable();
            setHelpButtonEnableDisable(true);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            btnInvestmentID.setEnabled(false);            
            resetTransactionUI();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            ClientUtil.enableDisable(panInvestmentTrans, false);
            rdoCloseYesOrNo.setSelected(false);
            cboInvestmentBehaves.setEnabled(true);
            lblCloserTypeValue.setText("");
            ClientUtil.enableDisable(panAmortizationDetails, false);
            panInvestSBorCATrans.setVisible(false);
            tdtPurchaseDt.setDateValue(DateUtil.getStringDate(curDate));
            tdtUpToIntRecivedDt.setDateValue(DateUtil.getStringDate(curDate));
        } else if (panAmortizationDetails.isShowing() == true) {
            setButtonEnableDisable();
            ClientUtil.enableDisable(panAmortizationDetails, true);
            ClientUtil.enableDisable(panPurchaseOrSale1, false);
            ClientUtil.enableDisable(panInvestmentTrans, false);
            btnAuthorize.setVisible(false);
            btnReject.setVisible(false);
            btnException.setVisible(false);
            btnInvestmentIDTrans.setEnabled(true);
            btnTranAccRefNo.setEnabled(true);
        }
        txtNarration1.setText("");
        txtInvestTDS.setVisible(false);
        lblTdsAmount.setVisible(false);
    }//GEN-LAST:event_btnNewActionPerformed

private void tblMultiTransTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTableMousePressed
    // TODO add your handling code here:
    String acno = "";
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
        if (tblMultiTransTable.getRowCount() > 0) {
            acno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 2));
            observable.showData(acno);
        }
    } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        acno = CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 2));
        ArrayList aList = new ArrayList();
        InvestmentsTransTO obj = (InvestmentsTransTO) finalMap.get(acno);
        System.out.println("obj" + obj);
        aList.add(obj);
        observable.showDatanew(aList);
    }
    txtTransAmt.setText(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 3)));
}//GEN-LAST:event_tblMultiTransTableMousePressed

private void btnaddSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddSaveActionPerformed
    // TODO add your handling code here:
    if ((CommonUtil.convertObjToDouble(txtTransAmt.getText())) > 0.0) {
        HashMap transMap = new HashMap();
        ArrayList newList = new ArrayList();
        newList.add(txtInvestmentID.getText());
        newList.add(txtInternalAccNo.getText());
        newList.add(txtTransAmt.getText());
        newList.add(txtNarration1.getText());
        newList.add(txtInvestTDS.getText());
        //newList.add(txtInvestTDS.getText());
        if (tableList == null) {
            tableList = new ArrayList();
        }
        for (int i = 0; i < tblMultiTransTable.getRowCount(); i++) {
            if (txtInternalAccNo.getText().equals(tblMultiTransTable.getValueAt(i, 2))) {
                tableList.remove(i);
            }
        }
        tableList.add(newList);
        observable.insertTable(tableList);
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
        updateOBFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String action = CommonConstants.TOSTATUS_INSERT;
            InvestmentsTransTO obj = observable.getInvestmentsTransTO(action);
            if (finalMap == null) {
                finalMap = new HashMap();
            }
            finalMap.put(txtInternalAccNo.getText(), obj);

        }
        System.out.println("fmapppp" + finalMap);
        calcTotal();
        clearNextadd();
    }
    if (lblCloserTypeValue.getText().equals("Premature Closure")) {
        if (txtPrematureROI.getText() == null || txtPrematureROI.getText().equals("")) {
            txtPrematureROIFocusLost(null);
            System.out.println("focuslosttt>>>");
        }
    }
    //txtCreditParticulars.setText("");
    txtInvestTDS.setText("");
}//GEN-LAST:event_btnaddSaveActionPerformed
    private void initTableData() {
        //System.out.println("sssssss3333"+ observable.getTblCheckBookTable().getDataArrayList());
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
    }

    private void calcTotal() {
        double total = 0.0;
        if (tblMultiTransTable.getRowCount() > 0) {
            for (int i = 0; i < tblMultiTransTable.getRowCount(); i++) {
                total += CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 3));
            }
            txtTotAmt.setText(CommonUtil.convertObjToStr(total));
            transactionUI.setCallingAmount(txtTotAmt.getText());
        }
    }

    private void clearNextadd() {
        ClientUtil.enableDisable(panDepositDetails1, false);
        ClientUtil.enableDisable(panTransAdd, false);
        ClientUtil.clearAll(panDepositDetails1);
        //panDepositDetails.setVisible(false);
         ClientUtil.clearAll(panDepositDetails);
        txtTransAmt.setText("0");
        txtInvestmentAmount.setText("0");
        btnAccRefNo.setEnabled(false);
        lblTotalAmountValue.setText("0");
        txtNarration1.setText("");
        lblCloserTypeValue.setText("");
        lblPrematureCalculateAmtVal.setText("");
       // txtCreditParticulars.setText("");
       // txtDebitParticulars.setText("");
        clearDepositDetais();
        //ClientUtil.clearAll(this);
    }
    public void clearDepositDetais(){
        lblIssueDateValue.setText("");
        lblFaceValueValue.setText("");
        lblLastIntPaidDateValue.setText("");
        lblInterestPaymentFrequencyValue.setText("");
        lblPeriodValue.setText("");
        lblCoupenValue.setText("");
        lblMaturityDateValue.setText("");
        lblTotalInvestmentAmountValue.setText("");
        lblTotalPremiumPaidValue.setText("");
        lblTotalPremiumCollectedValue.setText("");
        lblTotalInterestPaidValue.setText("");
        lblTotalInterestCollectedValue.setText("");
        lblMaturityAmtValue.setText("");
         ClientUtil.clearAll(panInvestAmountDetails);
        
        
    }
private void btnAddDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDeleteActionPerformed
    // TODO add your handling code here:
    int s = -1;
    s = tblMultiTransTable.getSelectedRow();
    System.out.println("sssde" + s);
    if (s != -1) {
        tableList.remove(s);
        finalMap.remove(tblMultiTransTable.getValueAt(s, 2));
        observable.insertTable(tableList);
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
        calcTotal();
        clearNextadd();
        System.out.println("fmapppp" + finalMap);
    } else {
        ClientUtil.showAlertWindow("Please Select a row");
    }
}//GEN-LAST:event_btnAddDeleteActionPerformed

private void btnaddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddNewActionPerformed
// TODO add your handling code here:
    if ((CommonUtil.convertObjToDouble(txtTransAmt.getText()) > 0)) {
        ClientUtil.showAlertWindow("Please Save the Data First");
        return;
    } else {
        btnInvestmentID.setEnabled(true);
        changeFieldNamesAccordingTOBehavesAndRdo();
    }
}//GEN-LAST:event_btnaddNewActionPerformed

private void lblTotalAmountValueFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblTotalAmountValueFocusGained
// TODO add your handling code here:
    txtTransAmt.setText(lblTotalAmountValue.getText());
}//GEN-LAST:event_lblTotalAmountValueFocusGained

private void txtTotAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotAmtFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtTotAmtFocusLost

private void txtTransAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransAmtActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTransAmtActionPerformed

private void txtTransAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransAmtFocusLost
// TODO add your handling code here:
       if (rdoInterest.isSelected() == true) {
            txtBrokenPeriodIntAmount.setText(txtTransAmt.getText());//txtTransAmt   
            txtBrokenPeriodIntAmountFocusLost(null);
         }
       txtInvestmentAmount.setText(txtTransAmt.getText());
}//GEN-LAST:event_txtTransAmtFocusLost

private void txtInvestTDSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvestTDSActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtInvestTDSActionPerformed

    private void lblMaturityAmtValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMaturityAmtValueMouseClicked
        // TODO add your handling code here:
           // Added by nithya on 16-12-2016
        String intReceivableTxt = COptionPane.showInputDialog("Interest receivable");
        System.out.println("intReceivableTxt :: " + intReceivableTxt);
        System.out.println("interest received :: " + lblTotalInterestCollectedValue.getText());

        double interestReceived = CommonUtil.convertObjToDouble(lblTotalInterestCollectedValue.getText()).doubleValue();
        double interestReceivable = CommonUtil.convertObjToDouble(intReceivableTxt).doubleValue();

        if (interestReceivable >= interestReceived) {
            lblMaturityAmtValue.setText(intReceivableTxt);
        } else {
            ClientUtil.showMessageWindow("Interest receivable should not be less than interest received");
        }
    }//GEN-LAST:event_lblMaturityAmtValueMouseClicked
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccRefNo;
    private com.see.truetransact.uicomponent.CButton btnAddDelete;
    private com.see.truetransact.uicomponent.CButton btnAmortization;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInvestmentID;
    private com.see.truetransact.uicomponent.CButton btnInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CButton btnInvestmentIDTrans1;
    private com.see.truetransact.uicomponent.CButton btnInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTranAccRefNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewTrans;
    private com.see.truetransact.uicomponent.CButton btnaddNew;
    private com.see.truetransact.uicomponent.CButton btnaddSave;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentBehaves;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentBehavesTrans;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentType;
    private com.see.truetransact.uicomponent.CLabel lblAccRefNo;
    private com.see.truetransact.uicomponent.CLabel lblAccRefNo1;
    private com.see.truetransact.uicomponent.CLabel lblBrokenPeriodIntAmount;
    private com.see.truetransact.uicomponent.CLabel lblBrokerCommission;
    private com.see.truetransact.uicomponent.CLabel lblBrokerName;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblCloserType;
    private com.see.truetransact.uicomponent.CLabel lblCloserTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblCoupenRate;
    private com.see.truetransact.uicomponent.CLabel lblCoupenValue;
    private com.see.truetransact.uicomponent.CLabel lblDebitChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblDiscount;
    private com.see.truetransact.uicomponent.CLabel lblFaceValue;
    private com.see.truetransact.uicomponent.CLabel lblFaceValueValue;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblIPrematureROI;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequencyValue;
    private com.see.truetransact.uicomponent.CLabel lblInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblInternalAccNo1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentBehavesTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentID;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentIDTrans1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentName;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentNameTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentType;
    private com.see.truetransact.uicomponent.CLabel lblIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblIssueDateValue;
    private com.see.truetransact.uicomponent.CLabel lblLastIntPaidDate;
    private com.see.truetransact.uicomponent.CLabel lblLastIntPaidDateValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaturityAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDateValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblNarration1;
    private com.see.truetransact.uicomponent.CLabel lblNoOfUnits;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblPreMatureCloserRate;
    private com.see.truetransact.uicomponent.CLabel lblPrematureCalculateAmt;
    private com.see.truetransact.uicomponent.CLabel lblPrematureCalculateAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblPrematureIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblPremiumAmount;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseDt;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseRate;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseorSalBy;
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
    private com.see.truetransact.uicomponent.CLabel lblTdsAmount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfSharesCount2;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfSharesCount3;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestCollected;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPaidValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalInvestmentAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumCollected;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumPaidValue;
    private com.see.truetransact.uicomponent.CLabel lblTransAmt;
    private com.see.truetransact.uicomponent.CLabel lblTransCashOrTransfer;
    private com.see.truetransact.uicomponent.CLabel lblTransTotalInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblTransTypeTrans;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDt;
    private com.see.truetransact.uicomponent.CLabel lblUpToIntRecivedDt;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccRefNo;
    private com.see.truetransact.uicomponent.CPanel panAccRefNo1;
    private com.see.truetransact.uicomponent.CPanel panAmortizationDetails;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn;
    private com.see.truetransact.uicomponent.CPanel panClosingPosition;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails1;
    private com.see.truetransact.uicomponent.CPanel panInvestAmountDetails;
    private com.see.truetransact.uicomponent.CPanel panInvestSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID1;
    private com.see.truetransact.uicomponent.CPanel panInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CPanel panInvestmentName1;
    private com.see.truetransact.uicomponent.CPanel panInvestmentSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panInvestmentTrans;
    private com.see.truetransact.uicomponent.CPanel panMutipleTrans;
    private com.see.truetransact.uicomponent.CPanel panPurchaseMode;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale1;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale2;
    private com.see.truetransact.uicomponent.CPanel panPurchaseThrough;
    private com.see.truetransact.uicomponent.CPanel panSBorCA;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransAdd;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransaction1;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails1;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSBorCA;
    private com.see.truetransact.uicomponent.CRadioButton rdoCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoCloseYesOrNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestTrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoMarket;
    private com.see.truetransact.uicomponent.CRadioButton rdoOffMarket;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrimary;
    private com.see.truetransact.uicomponent.CRadioButton rdoPurchase;
    private com.see.truetransact.uicomponent.CRadioButton rdoPurchaseTrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBorCA_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBorCA_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSale;
    private com.see.truetransact.uicomponent.CRadioButton rdoSaleTrans;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecondary;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpMultiTransTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTblAmortizationDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtNarration;
    private com.see.truetransact.uicomponent.CTabbedPane tabClosingType;
    private com.see.truetransact.uicomponent.CTabbedPane tabClosingType1;
    private com.see.truetransact.uicomponent.CTable tblAmortizationDetails;
    private com.see.truetransact.uicomponent.CTable tblMultiTransTable;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtPurchaseDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CDateField tdtTransactionDt;
    private com.see.truetransact.uicomponent.CDateField tdtUpToIntRecivedDt;
    private com.see.truetransact.uicomponent.CTextField txtAccRefNo;
    private com.see.truetransact.uicomponent.CTextField txtBrokenPeriodIntAmount;
    private com.see.truetransact.uicomponent.CTextField txtBrokerCommission;
    private com.see.truetransact.uicomponent.CTextField txtBrokerName;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtDebitChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtDiscount;
    private com.see.truetransact.uicomponent.CTextField txtInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtInvestTDS;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentID;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentName;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentNameTrans;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CTextArea txtNarration;
    private com.see.truetransact.uicomponent.CTextArea txtNarration1;
    private com.see.truetransact.uicomponent.CTextField txtNoOfUnits;
    private com.see.truetransact.uicomponent.CTextField txtPreMatureCloserRate;
    private com.see.truetransact.uicomponent.CTextField txtPrematureIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtPrematureROI;
    private com.see.truetransact.uicomponent.CTextField txtPremiumAmount;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseRate;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseorSalBy;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalInvestmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtTranAccRefNo;
    private com.see.truetransact.uicomponent.CTextField txtTranInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtTransAmt;
    // End of variables declaration//GEN-END:variables
}
