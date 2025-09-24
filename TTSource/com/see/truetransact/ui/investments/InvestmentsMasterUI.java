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
import java.util.List;
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
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.report.PrintClass;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import javax.swing.table.TableModel;

public class InvestmentsMasterUI extends CInternalFrame implements UIMandatoryField, Observer {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private InvestmentsMasterOB observable;
    private InvestmentsMasterMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsMasterRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    private String statusType = new String();
    final String AUTHORIZE = "Authorize";
    private int yearTobeAdded = 1900;
    private Date curDate = null;
    private String investmentId = "";
    private boolean updateMode = false;
    private double yr = 0;
    private double period = 0;
    private int ok = 0;
    private int yes = 0;
    private int no = 1;
    private int cancel = 2;
    private int totalMonths = 12;
    private int totalDays = 365;
    private int daysInMonth = 31;
    private int moreDays = 32;
    private int perHalfYear = 2;
    private int perQuarterYear = 4;
    private int perMonth = 12;
    private double interest = 6;
    int updateTab = -1;
    private TransactionUI transactionUI = new TransactionUI();
    private int rejectFlag = 0;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    PrintClass print=new PrintClass();
    double renInterestAmt=0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    //   TransactionUI transactionUI = new TransactionUI();

    /**
     * Creates new form ShareProductUI
     */
    public InvestmentsMasterUI() {
        initUIComponents();
        transactionUI.setSourceScreen("INVESTMENT_TRANS");
//        transactionUI.addToScreen(panRenewalTransaction);
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
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
        txtInvestmentID.setEnabled(true);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInvestment, getMandatoryHashMap());
        txtInvestmentID.setEnabled(false);
        setHelpMessage();
        setObservable();
        observable.resetForm();
        lblBranchNameIDValue.setText("");
        lblBankNameIDValue.setText("");
        initComponentData();
        ClientUtil.enableDisable(panInvestmentDetails1, false);
        ClientUtil.enableDisable(panInvestmentDetails, false);
        setButtonEnableDisable();
        panInvestmentDetails1.setVisible(false);
        panInvestmentDetails.setVisible(false);
        panSecurityType.setVisible(false);
        panBankCodeAndBranchCode.setVisible(false);
        txtInvestmentID.setEnabled(false);
        observable.resetForm();
        cboInvestmentBehaves.setEditable(false);
        panInsideDepositDetails.setVisible(false);
        panInsideDepositDetails1.setVisible(false);
        panCheckBookTable.setVisible(false);
        panShareDetails.setVisible(false);
        panReserveFund.setVisible(false);
        btnUpdate.setEnabled(false);
        btnReset.setEnabled(false);
        lblInvestmentName.setText("A/c Description");
        ClientUtil.enableDisable(panInvestment, false);
        btnInvestmentID.setEnabled(false);
        txtSBInternalAccNo.setEnabled(false);
        txtFDInternalAccNo.setEnabled(false);
        txtShareInternalAccNo.setEnabled(false);
        txtRFInternalAccNo.setEnabled(false);
        btnCheckBookNew.setEnabled(false);
        btnCheckBookSave.setEnabled(false);
        btnCheckBookDelete.setEnabled(false);
        panRenewalDetails.setVisible(false);
        txtFDPeriodicIntrest.setVisible(false);
        lblFDPeriodicIntrest.setVisible(false);
        btnInvestmentIDTrans1.setEnabled(false);
        btnInvestmentIDTransSBorCA.setEnabled(false);
        tabInvestment.remove(panInsideRenewalDetails);
        resetTransactionUI();
        panWithdrawalIntAmount.setVisible(false);
        renInterestAmt=0;
        txtInvestTDS.setEnabled(false);
    }

    private void resetTransactionUI() {
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }

    private void callEnabledisableTxt() {
        txtCallOptionNoOfYears.setVisible(false);
        txtPutOptionNoOfYears.setVisible(false);
        txtSetUpOptionNoOfYears.setVisible(false);
        lblCallOptionNoOfYears.setVisible(false);
        lblPutOptionNoOfYears.setVisible(false);
        lblSetUpOptionNoOfYears.setVisible(false);
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
        lblPutOption.setName("lblPutOption");
        lblCallOption.setName("lblCallOption");
        lblSetupOption.setName("lblSetupOption");
        cboInvestmentBehaves.setName("cboInvestmentBehaves");
        txtInvestmentID.setName("txtInvestmentID");
        txtInvestmentName.setName("txtInvestmentName");
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
        lblPutOption.setText(resourceBundle.getString("lblPutOption"));
        lblCallOption.setText(resourceBundle.getString("lblCallOption"));
        lblSetupOption.setText(resourceBundle.getString("lblSetupOption"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInvestmentBehaves", new Boolean(true));
        mandatoryMap.put("txtInvestmentID", new Boolean(true));
        mandatoryMap.put("txtInvestmentName", new Boolean(true));
        mandatoryMap.put("tdtIssueDt", new Boolean(true));
        mandatoryMap.put("txtFaceValue", new Boolean(true));
        mandatoryMap.put("cboInterestPaymentFrequency", new Boolean(true));
        mandatoryMap.put("txtCouponRate", new Boolean(false));
        mandatoryMap.put("tdtMaturityDate", new Boolean(true));
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
            observable = InvestmentsMasterOB.getInstance();
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
            cboRenewalInvestmentBehaves.setModel(observable.getCbmRenewalInvestmentBehaves());
            cboFDInterestPaymentFrequency.setModel(observable.getCbmFDInterestPaymentFrequency());
            cboRenewalFDInterestPaymentFrequency.setModel(observable.getCbmRenewalFDInterestPaymentFrequency());
            cboState.setModel(observable.getCbmState());
            cboInvestmentType.setModel(observable.getCbmInvestmentTypeSBorCA());
            cboRenewalInvestmentType.setModel(observable.getCbmRenewalInvestmentTypeSBorCA());
            tblCheckBookTable.setModel(observable.getTblCheckBookTable());
            cboRenewalDepTransProdType.setModel(observable.getCboRenewalDepTransProdType());
            cboRenewalDepTransMode.setModel(observable.getCboRenewalDepTransModel());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new InvestmentsMasterMRB();
        cboInvestmentBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInvestmentBehaves"));
        txtInvestmentID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentID"));
        txtInvestmentName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentName"));
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        txtBankCodeID.setText(CommonUtil.convertObjToStr(observable.getTxtBankCode()));
        txtBranchCodeID.setText(CommonUtil.convertObjToStr(observable.getTxtBranchCode()));
        if (CommonUtil.convertObjToStr(observable.getCallOption()).equals("Y")) {
            chkCallOption.setSelected(true);
        } else {
            chkCallOption.setSelected(false);
        }
        if (CommonUtil.convertObjToStr(observable.getPutOption()).equals("Y")) {
            chkPutOption.setSelected(true);
        } else {
            chkPutOption.setSelected(false);
        }
        if (CommonUtil.convertObjToStr(observable.getSetUpOption()).equals("Y")) {
            chkSetupOption.setSelected(true);
        } else {
            chkSetupOption.setSelected(false);
        }

        if (observable.getSLR().equals("Y")) {
            rdoSlr.setSelected(true);
        }
        if (observable.getSLR().equals("N")) {
            rdoNonSlr.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getClassification()).equals("HFM")) {
            rdoHfm.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getClassification()).equals("HFS")) {
            rdoHfs.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getClassification()).equals("HFT")) {
            rdoHft.setSelected(true);
        }
        if (CommonUtil.convertObjToDouble(observable.getCallOptionNoofYears()).doubleValue() > 0.0) {
            txtCallOptionNoOfYears.setText(CommonUtil.convertObjToStr(observable.getCallOptionNoofYears()));
            txtCallOptionNoOfYears.setVisible(true);
        }
        if (CommonUtil.convertObjToDouble(observable.getPutOptionNoofYears()).doubleValue() > 0) {
            txtPutOptionNoOfYears.setText(CommonUtil.convertObjToStr(observable.getPutOptionNoofYears()));
            txtPutOptionNoOfYears.setVisible(true);
        }
        if (CommonUtil.convertObjToDouble(observable.getSetUpOptionNoofYears()).doubleValue() > 0.0) {
            txtSetUpOptionNoOfYears.setText(CommonUtil.convertObjToStr(observable.getSetUpOptionNoofYears()));
            txtSetUpOptionNoOfYears.setVisible(true);
        }
        if (CommonUtil.convertObjToStr(observable.getRdoSecurityType()).length() > 0) {

            if (observable.getRdoSecurityType().equals("C")) {
                rdoCentralSecurity.setSelected(true);
            }
            if (observable.getRdoSecurityType().equals("S")) {
                rdoStateSecurity.setSelected(true);
                cboState.setSelectedItem(observable.getCboSecurityTypeCode());
                cboState.setVisible(true);

            }
            if (observable.getRdoSecurityType().equals("O")) {
                rdoOtherSecurity.setSelected(true);
                txtOthersName.setText(observable.getTxtOtherName());
                txtOthersName.setVisible(true);

            }
        }
        txtInvestmentIDTransSBorCA.setText(observable.getTxtInvestmentIDTransSBorCA());
        txtInvestmentRefNoTrans.setText(observable.getTxtInvestmentRefNoTrans());
        txtInvestmentInternalNoTrans.setText(observable.getTxtInvestmentInternalNoTrans());
        txtChequeNo.setText(observable.getTxtChequeNo());
        txtNarration.setText(observable.getTxtNarration());
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
        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(observable.getTxtInvestmentAmount()));
        cboInvestmentType.setSelectedItem(observable.getCboInvestmentTypeSBorCA());
//cboRenewalDepTransProdId.setModel(observable.getCbmRenewalDepTransProdId());

        if (observable.getCbmRenewalDepTransProdId() != null) {
            if (observable.getCboRenewalDepTransProdType() != null) {
                if (!observable.getCboRenewalDepTransProdType().equals("General Ledger") && !cboRenewalDepTransProdId.getSelectedItem().equals("")) {
                    cboRenewalDepTransProdId.setModel(observable.getCbmRenewalDepTransProdId());
                    cboRenewalDepTransProdId.setSelectedItem(observable.getCbmRenewalDepTransProdId().getDataForKey(observable.getCboRenewalDepTransProdId()));
                }
            }
        }
    }

    private void updateRenewalTransDetail() {
        cboRenewalInvestmentType.setSelectedItem(observable.getCboInvestmentTypeSBorCA());
        observable.setCboRenewalInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboRenewalInvestmentType.getSelectedItem()));
        txtRenewalInvestmentIDTransSBorCA.setText(observable.getTxtInvestmentIDTransSBorCA());
        txtRenewalInvestmentRefNoTrans.setText(observable.getTxtInvestmentRefNoTrans());
        txtRenewalInvestmentInternalNoTrans.setText(observable.getTxtInvestmentInternalNoTrans());
        txtRenewalNarration.setText(observable.getTxtNarration());
        String transRenewalTypeSBorCA = CommonUtil.convertObjToStr(observable.getRdoSBorCA());
        if (transRenewalTypeSBorCA.length() > 0 && transRenewalTypeSBorCA.equals("Y")) {
            rdoRenewalSBorCA_yes.setSelected(true);
            rdoRenewalSBorCA_no.setSelected(false);
            panRenewalInvestSBorCATrans.setVisible(true);
        } else {
            panRenewalInvestSBorCATrans.setVisible(false);
            rdoRenewalSBorCA_yes.setSelected(false);
            rdoRenewalSBorCA_no.setSelected(true);
        }
    }

    public void updateOperative() {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        txtSBAgencyName.setText(observable.getTxtSBAgencyName());
        txtSBAccountRefNO.setText(observable.getTxtSBAccountRefNO());
        txtSBInternalAccNo.setText(observable.getTxtSBInternalAccNo());
        txtSBAreaOperatorDetails.setText(observable.getTxtSBAreaOperatorDetails());
        tdtSBAccOpenDt.setDateValue(DateUtil.getStringDate(observable.getTdtSBAccOpenDt()));
        String checkBookAllowed = CommonUtil.convertObjToStr(observable.getRdoCheckBookAllowed());
        if (checkBookAllowed.length() > 0 && checkBookAllowed.equals("Y")) {
            rdoCheckBookAllowed_yes.setSelected(true);
            rdoCheckBookAllowed_no.setSelected(false);
            panCheckBookTable.setVisible(true);
        } else {
            rdoCheckBookAllowed_yes.setSelected(false);
            rdoCheckBookAllowed_no.setSelected(true);
            panCheckBookTable.setVisible(false);
        }
    }

    public void updateDeposit() {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        txtFDAgencyName.setText(observable.getTxtFDAgencyName());
        txtFDAccountRefNO.setText(observable.getTxtFDAccountRefNO());
        txtFDInternalAccNo.setText(observable.getTxtFDInternalAccNo());
        txtFDPricipalAmt.setText(observable.getTxtFDPricipalAmt());

        txtFDInvestmentPeriod_Years.setText(observable.getTxtFDInvestmentPeriod_Years());
        txtFDInvestmentPeriod_Months.setText(observable.getTxtFDInvestmentPeriod_Months());
        txtFDInvestmentPeriod_Days.setText(observable.getTxtFDInvestmentPeriod_Days());
        txtFDRateOfInt.setText(observable.getTxtFDRateOfInt());
        txtFDMaturityAmt.setText(observable.getTxtFDMaturityAmt());
        txtFDInterestReceivable.setText(observable.getTxtFDInterestReceivable());
        txtFDInterestReceived.setText(observable.getTxtFDInterestReceived());
        txtFDPeriodicIntrest.setText(observable.getTxtFDPeriodicIntrest());
        tdtFDAccOpenDt.setDateValue(DateUtil.getStringDate(observable.getTdtFDAccOpenDt()));
        tdtFDEffectiveDt.setDateValue(DateUtil.getStringDate(observable.getTdtFDEffectiveDt()));
        tdtFDMaturityDt.setDateValue(DateUtil.getStringDate(observable.getTdtFDMaturityDt()));
        tdtFDIntReceivedTillDt.setDateValue(DateUtil.getStringDate(observable.getTdtFDIntReceivedTillDt()));
        cboFDInterestPaymentFrequency.setSelectedItem(observable.getCboFDInterestPaymentFrequency());
        String WithPrincipal = CommonUtil.convertObjToStr(observable.getRdoWithPrincipal());
        if (WithPrincipal.length() > 0 && WithPrincipal.equals("Y")) {
            rdoWithPrincipal_yes.setSelected(true);
            rdoWithPrincipal_no.setSelected(false);
        } else {
            rdoWithPrincipal_yes.setSelected(false);
            rdoWithPrincipal_no.setSelected(true);
        }

        String WithInterest = CommonUtil.convertObjToStr(observable.getRdoWithInterest());
        if (WithInterest.length() > 0 && WithInterest.equals("Y")) {
            //            panWithInterestYesNo.setVisible(true);
            rdoWithInterest_yes.setSelected(true);
            rdoWithInterest_no.setSelected(false);
        } else {
            rdoWithInterest_yes.setSelected(false);
            rdoWithInterest_no.setSelected(true);
        }
        String renewal = CommonUtil.convertObjToStr(observable.getRdoRenewal());
        if (renewal.length() > 0 && renewal.equals("Y")) {
            transactionUI.addToScreen(panRenewalTransaction);
            txtRenewalWithdrawalAmt.setText(observable.getTxtRenewalWithdrawalAmt());
            rdoRenewal_yes.setSelected(true);
            rdoRenewal_no.setSelected(false);
            panInsideRenewalDetails.setVisible(true);
            panRenewalDetails.setVisible(true);
            tabInvestment.add(panInsideRenewalDetails, "Renewal Details", 1);
            tabInvestment.setSelectedIndex(1);
            updateRenewalDeposit();
            //            panWithInterestYesNo.setVisible(true);
        } else {
            tabInvestment.setSelectedIndex(0);
            panInsideRenewalDetails.setVisible(false);
            tabInvestment.remove(panInsideRenewalDetails);
            rdoWithInterest_yes.setSelected(false);
            rdoWithInterest_no.setSelected(false);
            rdoRenewal_yes.setSelected(false);
            rdoRenewal_no.setSelected(true);
            panWithInterestYesNo.setVisible(false);
        }
        String renewalSameNo = CommonUtil.convertObjToStr(observable.getRdoRenewalSameNumber());
        if (renewalSameNo.length() > 0 && renewalSameNo.equals("Y")) {
            rdoRenewalWithSameNo.setSelected(true);
        }
        String renewalNewNo = CommonUtil.convertObjToStr(observable.getRdoRenewalNewNumber());
        if (renewalNewNo.length() > 0 && renewalNewNo.equals("Y")) {
            rdoRenewalWithNewNo.setSelected(true);
        }
        String renewalDiffNo = CommonUtil.convertObjToStr(observable.getRdoRenewalDiffProdNumber());
        if (renewalDiffNo.length() > 0 && renewalDiffNo.equals("Y")) {
            rdoRenewalWithDiffProdID.setSelected(true);
        }
        String renewalWithInt = CommonUtil.convertObjToStr(observable.getRdoRenewalWithInterest());
        if (renewalWithInt.length() > 0 && renewalWithInt.equals("Y")) {
            rdoRenewalWithInterest.setSelected(true);
        }
        String renewalwithoutint = CommonUtil.convertObjToStr(observable.getRdoRenewalWithOutInterest());
        if (renewalwithoutint.length() > 0 && renewalwithoutint.equals("Y")) {
            rdoRenewalWithoutInterest.setSelected(true);
        }
        String renewalParInt = CommonUtil.convertObjToStr(observable.getRdoRenewalpartialInterest());
        if (renewalParInt.length() > 0 && renewalParInt.equals("Y")) {
            rdoRenewalWithPartialInterest.setSelected(true);
        }
    }

    public void updateRenewalDeposit() {
        cboRenewalInvestmentBehaves.setSelectedItem(observable.getCboRenewalInvestmentBehaves());
        txtRenewalInvestmentID.setText(observable.getRenewalInvestmentID());
        txtRenewalInvestmentName.setText(observable.getRenewalInvestmentName());
        txtRenewalFDAgencyName.setText(observable.getTxtRenewalFDAgencyName());
        txtRenewalFDAccountRefNO.setText(observable.getTxtRenewalFDAccountRefNO());
        txtRenewalFDInternalAccNo.setText(observable.getTxtRenewalFDInternalAccNo());
        txtRenewalFDPricipalAmt.setText(observable.getTxtRenewalFDPricipalAmt());
        txtRenewalFDInvestmentPeriod_Years.setText(observable.getTxtRenewalFDInvestmentPeriod_Years());
        txtRenewalFDInvestmentPeriod_Months.setText(observable.getTxtRenewalFDInvestmentPeriod_Months());
        txtRenewalFDInvestmentPeriod_Days.setText(observable.getTxtRenewalFDInvestmentPeriod_Days());
        txtRenewalFDRateOfInt.setText(observable.getTxtRenewalFDRateOfInt());
        txtRenewalFDMaturityAmt.setText(observable.getTxtRenewalFDMaturityAmt());
        txtRenewalFDInterestReceivable.setText(observable.getTxtRenewalFDInterestReceivable());
        System.out.println("INVEST TDS:"+observable.getTxtInvestTDS());
        txtInvestTDS.setText(CommonUtil.convertObjToStr(observable.getTxtInvestTDS()));
        txtRenewalFDPeriodicIntrest.setText(observable.getTxtRenewalFDPeriodicIntrest());
        tdtRenewalFDAccOpenDt.setDateValue(DateUtil.getStringDate(observable.getTdtRenewalFDAccOpenDt()));
        tdtRenewalFDEffectiveDt.setDateValue(DateUtil.getStringDate(observable.getTdtRenewalFDEffectiveDt()));
        tdtRenewalFDMaturityDt.setDateValue(DateUtil.getStringDate(observable.getTdtRenewalFDMaturityDt()));
        cboRenewalFDInterestPaymentFrequency.setSelectedItem(observable.getCboRenewalFDInterestPaymentFrequency());        
        tdtTransactionDt.setDateValue(DateUtil.getStringDate(observable.getTdtTransactionDt()));
    }

    public void updateShare() {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        txtShareAgencyName.setText(observable.getTxtShareAgencyName());
        txtShareType.setText(observable.getTxtShareType());
        txtShareMemberID.setText(observable.getTxtShareMemberID());
        txtShareInternalAccNo.setText(observable.getTxtShareInternalAccNo());
        txtNoOfShares.setText(observable.getTxtNoOfShares());
        txtShareValue.setText(observable.getTxtShareValue());
        txtShareFaceValue.setText(observable.getTxtShareFaceValue());
        txtFeesPaid.setText(observable.getTxtFeesPaid());
        tdtShareAccOpenDt.setDateValue(DateUtil.getStringDate(observable.getTdtShareAccOpenDt()));
    }

    public void updateReserveFund() {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        txtRFAgencyName.setText(observable.getTxtRFAgencyName());
        txtRFAccountRefNO.setText(observable.getTxtRFAccountRefNO());
        txtRFInternalAccNo.setText(observable.getTxtRFInternalAccNo());
        txtRFPricipalAmt.setText(observable.getTxtRFPricipalAmt());
        tdtRFAccOpenDt.setDateValue(DateUtil.getStringDate(observable.getTdtRFAccOpenDt()));
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(this.getScreen());
        observable.setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboInvestmentType.getSelectedItem()));
        observable.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(txtInvestmentIDTransSBorCA.getText()));
        observable.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(txtInvestmentRefNoTrans.getText()));
        observable.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(txtInvestmentInternalNoTrans.getText()));
        observable.setTxtChequeNo(CommonUtil.convertObjToStr(txtChequeNo.getText()));
        observable.setTxtNarration(CommonUtil.convertObjToStr(txtNarration.getText()));
        observable.setTxtInvestmentAmount(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()));
        if (rdoSBorCA_yes.isSelected() == true) {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("N"));
        }
    }

    public void updateRenewalOBFields() {
        observable.setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboRenewalInvestmentType.getSelectedItem()));
        observable.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(txtRenewalInvestmentIDTransSBorCA.getText()));
        observable.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(txtRenewalInvestmentRefNoTrans.getText()));
        observable.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(txtRenewalInvestmentInternalNoTrans.getText()));
        observable.setTxtNarration(CommonUtil.convertObjToStr(txtRenewalNarration.getText()));
        observable.setTxtInvestmentAmount(CommonUtil.convertObjToDouble(txtRenewalInvestmentAmount.getText()));
        if (rdoRenewalSBorCA_yes.isSelected() == true) {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoSBorCA(CommonUtil.convertObjToStr("N"));
        }
    }

    public void updateOperativeOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setTxtSBAgencyName(CommonUtil.convertObjToStr(txtSBAgencyName.getText()));
        observable.setTxtSBAccountRefNO(CommonUtil.convertObjToStr(txtSBAccountRefNO.getText()));
        observable.setTxtSBInternalAccNo(CommonUtil.convertObjToStr(txtSBInternalAccNo.getText()));
        observable.setTxtSBAreaOperatorDetails(CommonUtil.convertObjToStr(txtSBAreaOperatorDetails.getText()));
        observable.setTdtSBAccOpenDt(DateUtil.getDateMMDDYYYY(tdtSBAccOpenDt.getDateValue()));
        if (rdoCheckBookAllowed_yes.isSelected() == true) {
            observable.setRdoCheckBookAllowed(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoCheckBookAllowed(CommonUtil.convertObjToStr("N"));
        }
    }

    public void updateDepositOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setTxtFDAgencyName(CommonUtil.convertObjToStr(txtFDAgencyName.getText()));
        observable.setTxtFDAccountRefNO(CommonUtil.convertObjToStr(txtFDAccountRefNO.getText()));
        observable.setTxtFDInternalAccNo(CommonUtil.convertObjToStr(txtFDInternalAccNo.getText()));
        observable.setTxtFDPricipalAmt(CommonUtil.convertObjToStr(txtFDPricipalAmt.getText()));
        observable.setTxtFDInvestmentPeriod_Years(CommonUtil.convertObjToStr(txtFDInvestmentPeriod_Years.getText()));
        observable.setTxtFDInvestmentPeriod_Months(CommonUtil.convertObjToStr(txtFDInvestmentPeriod_Months.getText()));
        observable.setTxtFDInvestmentPeriod_Days(CommonUtil.convertObjToStr(txtFDInvestmentPeriod_Days.getText()));
        observable.setTxtFDRateOfInt(CommonUtil.convertObjToStr(txtFDRateOfInt.getText()));
        observable.setTxtFDMaturityAmt(CommonUtil.convertObjToStr(txtFDMaturityAmt.getText()));
        observable.setTdtFDAccOpenDt(DateUtil.getDateMMDDYYYY(tdtFDAccOpenDt.getDateValue()));
        observable.setTdtFDEffectiveDt(DateUtil.getDateMMDDYYYY(tdtFDEffectiveDt.getDateValue()));
        observable.setTdtFDMaturityDt(DateUtil.getDateMMDDYYYY(tdtFDMaturityDt.getDateValue()));
        observable.setCboFDInterestPaymentFrequency(CommonUtil.convertObjToStr(cboFDInterestPaymentFrequency.getSelectedItem()));
        observable.setTxtFDInterestReceivable(CommonUtil.convertObjToStr(txtFDInterestReceivable.getText()));
        observable.setTxtFDInterestReceived(CommonUtil.convertObjToStr(txtFDInterestReceived.getText()));
        observable.setTxtFDPeriodicIntrest(CommonUtil.convertObjToStr(txtFDPeriodicIntrest.getText()));
        observable.setTdtFDIntReceivedTillDt(DateUtil.getDateMMDDYYYY(tdtFDIntReceivedTillDt.getDateValue()));
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_CCD")) {
            if (rdoWithPrincipal_yes.isSelected() == true) {
                observable.setRdoWithPrincipal(CommonUtil.convertObjToStr("Y"));
            } else {
                observable.setRdoWithPrincipal(CommonUtil.convertObjToStr("N"));
            }
        } else {
            observable.setRdoWithPrincipal("");
        }

        if (rdoWithInterest_yes.isSelected() == true) {
            observable.setRdoWithInterest(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoWithInterest(CommonUtil.convertObjToStr("N"));
        }
        if (rdoRenewal_yes.isSelected() == true) {
            observable.setRdoRenewal(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewal(CommonUtil.convertObjToStr("N"));
            observable.setRdoWithInterest("");
        }

        if (rdoRenewalWithSameNo.isSelected() == true) {
            observable.setRdoRenewalSameNumber(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewalSameNumber("");
        }

        if (rdoRenewalWithNewNo.isSelected() == true) {
            observable.setRdoRenewalNewNumber(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewalNewNumber("");
        }

        if (rdoRenewalWithDiffProdID.isSelected() == true) {
            observable.setRdoRenewalDiffProdNumber(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewalDiffProdNumber("");
        }

        if (rdoRenewalWithInterest.isSelected() == true) {
            observable.setRdoRenewalWithInterest(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewalWithInterest("");
        }

        if (rdoRenewalWithoutInterest.isSelected() == true) {
            observable.setRdoRenewalWithOutInterest(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoRenewalWithOutInterest("");
        }

        if (rdoRenewalWithPartialInterest.isSelected() == true) {
            observable.setTxtRenewalWithdrawalAmt(CommonUtil.convertObjToStr(txtRenewalWithdrawalAmt.getText()));
            observable.setRdoRenewalpartialInterest(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setTxtRenewalWithdrawalAmt("");
            observable.setRdoRenewalpartialInterest("");
        }
        if(chkAddMountToDeposit.isSelected()){
            if(cboRenewalDepTransMode.getSelectedItem().equals("Cash")){
                observable.setCbmRenewalDepTransProdType("");
                observable.setCboRenewalDepTransProdId("");
                observable.setTxtRenewalDepositID("");
            }
        }
    }

    public void updateRenewalDepositOBFields() {
        observable.setCboRenewalInvestmentBehaves(CommonUtil.convertObjToStr(cboRenewalInvestmentBehaves.getSelectedItem()));
        observable.setRenewalInvestmentID(CommonUtil.convertObjToStr(txtRenewalInvestmentID.getText()));
        observable.setRenewalInvestmentName(CommonUtil.convertObjToStr(txtRenewalInvestmentName.getText()));
        observable.setTxtRenewalFDAgencyName(CommonUtil.convertObjToStr(txtRenewalFDAgencyName.getText()));
        observable.setTxtRenewalFDAccountRefNO(CommonUtil.convertObjToStr(txtRenewalFDAccountRefNO.getText()));
        observable.setTxtRenewalFDInternalAccNo(CommonUtil.convertObjToStr(txtRenewalFDInternalAccNo.getText()));
        observable.setTxtRenewalFDPricipalAmt(CommonUtil.convertObjToStr(txtRenewalFDPricipalAmt.getText()));
        observable.setTxtRenewalFDInvestmentPeriod_Years(CommonUtil.convertObjToStr(txtRenewalFDInvestmentPeriod_Years.getText()));
        observable.setTxtRenewalFDInvestmentPeriod_Months(CommonUtil.convertObjToStr(txtRenewalFDInvestmentPeriod_Months.getText()));
        observable.setTxtRenewalFDInvestmentPeriod_Days(CommonUtil.convertObjToStr(txtRenewalFDInvestmentPeriod_Days.getText()));
        observable.setTxtRenewalFDRateOfInt(CommonUtil.convertObjToStr(txtRenewalFDRateOfInt.getText()));
        observable.setTxtRenewalFDMaturityAmt(CommonUtil.convertObjToStr(txtRenewalFDMaturityAmt.getText()));
        observable.setTdtRenewalFDAccOpenDt(DateUtil.getDateMMDDYYYY(tdtRenewalFDAccOpenDt.getDateValue()));
        observable.setTdtRenewalFDEffectiveDt(DateUtil.getDateMMDDYYYY(tdtRenewalFDEffectiveDt.getDateValue()));
        observable.setTdtRenewalFDMaturityDt(DateUtil.getDateMMDDYYYY(tdtRenewalFDMaturityDt.getDateValue()));
        observable.setCboRenewalFDInterestPaymentFrequency(CommonUtil.convertObjToStr(cboRenewalFDInterestPaymentFrequency.getSelectedItem()));
        observable.setTxtRenewalFDInterestReceivable(CommonUtil.convertObjToStr(txtRenewalFDInterestReceivable.getText()));
        observable.setTxtInvestTDS(CommonUtil.convertObjToDouble(txtInvestTDS.getText()));
        observable.setTxtRenewalFDPeriodicIntrest(CommonUtil.convertObjToStr(txtRenewalFDPeriodicIntrest.getText()));
        observable.setTdtTransactionDt(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()));
    }

    public void updateShareOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setTxtShareAgencyName(CommonUtil.convertObjToStr(txtShareAgencyName.getText()));
        observable.setTxtShareType(CommonUtil.convertObjToStr(txtShareType.getText()));
        observable.setTxtShareMemberID(CommonUtil.convertObjToStr(txtShareMemberID.getText()));
        observable.setTxtShareInternalAccNo(CommonUtil.convertObjToStr(txtShareInternalAccNo.getText()));
        observable.setTxtNoOfShares(CommonUtil.convertObjToStr(txtNoOfShares.getText()));
        observable.setTxtShareValue(CommonUtil.convertObjToStr(txtShareValue.getText()));
        observable.setTxtShareFaceValue(CommonUtil.convertObjToStr(txtShareFaceValue.getText()));
        observable.setTxtFeesPaid(CommonUtil.convertObjToStr(txtFeesPaid.getText()));
        observable.setTdtShareAccOpenDt(DateUtil.getDateMMDDYYYY(tdtShareAccOpenDt.getDateValue()));
    }

    public void updateReserveFundOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setTxtRFAgencyName(CommonUtil.convertObjToStr(txtRFAgencyName.getText()));
        observable.setTxtRFAccountRefNO(CommonUtil.convertObjToStr(txtRFAccountRefNO.getText()));
        observable.setTxtRFInternalAccNo(CommonUtil.convertObjToStr(txtRFInternalAccNo.getText()));
        observable.setTxtRFPricipalAmt(CommonUtil.convertObjToStr(txtRFPricipalAmt.getText()));
        observable.setTdtRFAccOpenDt(DateUtil.getDateMMDDYYYY(tdtRFAccOpenDt.getDateValue()));
    }
public void updateAddAmountToDepositOBFileds(){
            if(cboRenewalDepTransMode.getSelectedItem().equals("Transfer")){
            observable.setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(txtRenewalDepTransAmtValue.getText()));
            observable.setCbmRenewalDepTransModel(CommonUtil.convertObjToStr(cboRenewalDepTransMode.getSelectedItem()));
            String prodType = ((ComboBoxModel) cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmRenewalDepTransProdType(prodType);
            //observable.setCbmRenewalDepTransProdId(CommonUtil.convertObjToStr(cboRenewalDepTransProdId.getSelectedItem()));
        }else{
            observable.setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(txtRenewalDepTransAmtValue.getText()));
            observable.setCbmRenewalDepTransModel(CommonUtil.convertObjToStr(cboRenewalDepTransMode.getSelectedItem()));
        }
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
        callEnabledisableTxt();

    }

    /**
     * Setting up Lengths for the TextFields in theu UI
     */
    private void setMaxLength() {
        txtCallOptionNoOfYears.setValidation(new NumericValidation(4, 0));
        txtPutOptionNoOfYears.setValidation(new NumericValidation(4, 0));
        txtSetUpOptionNoOfYears.setValidation(new NumericValidation(4, 0));
        txtInvestmentName.setMaxLength(50);
        txtBankCodeID.setAllowNumber(true);
        txtBranchCodeID.setAllowNumber(true);
        txtBankCodeID.setAllowAll(false);
        txtBranchCodeID.setAllowAll(false);
        txtInvestmentName.setAllowAll(true);
        txtSBAgencyName.setAllowAll(true);
        txtSBAccountRefNO.setAllowAll(true);
        txtSBInternalAccNo.setAllowAll(true);
        txtStopPayment.setAllowAll(true);
        txtFromNO.setValidation(new NumericValidation());
        txtToNO.setValidation(new NumericValidation());
        txtFDAgencyName.setAllowAll(true);
        txtFDAccountRefNO.setAllowAll(true);
        txtFDInternalAccNo.setAllowAll(true);
        txtFDPricipalAmt.setValidation(new CurrencyValidation(16, 2));
        txtRenewalFDPricipalAmt.setValidation(new CurrencyValidation(16, 2));
        txtRenewalWithdrawalAmt.setValidation(new CurrencyValidation(16, 2));
        txtFDInvestmentPeriod_Years.setValidation(new NumericValidation());
        txtFDInvestmentPeriod_Months.setValidation(new NumericValidation());
        txtFDInvestmentPeriod_Days.setValidation(new NumericValidation());
        txtRenewalFDInvestmentPeriod_Years.setValidation(new NumericValidation());
        txtRenewalFDInvestmentPeriod_Months.setValidation(new NumericValidation());
        txtRenewalFDInvestmentPeriod_Days.setValidation(new NumericValidation());
        txtFDRateOfInt.setValidation(new NumericValidation(3, 3));
        txtRenewalFDRateOfInt.setValidation(new NumericValidation(3, 3));
        txtFDMaturityAmt.setValidation(new CurrencyValidation(16, 2));
        txtFDInterestReceivable.setValidation(new CurrencyValidation(16, 2));
        txtFDInterestReceived.setValidation(new CurrencyValidation(16, 2));
        txtFDPeriodicIntrest.setValidation(new CurrencyValidation(16, 2));
        txtRenewalFDInterestReceivable.setValidation(new CurrencyValidation(16, 2));
        txtInvestTDS.setValidation(new CurrencyValidation(16, 2));
        txtRenewalFDPeriodicIntrest.setValidation(new CurrencyValidation(16, 2));
        txtRenewalFDMaturityAmt.setValidation(new CurrencyValidation(16, 2));
        txtShareAgencyName.setAllowAll(true);
        txtShareType.setAllowAll(true);
        txtShareMemberID.setAllowAll(true);
        txtShareInternalAccNo.setAllowAll(true);
        txtNoOfShares.setValidation(new NumericValidation());
        txtShareValue.setValidation(new CurrencyValidation(16, 2));
        txtShareFaceValue.setValidation(new CurrencyValidation(16, 2));
        txtFeesPaid.setValidation(new CurrencyValidation(16, 2));
        txtRFAgencyName.setAllowAll(true);
        txtRFAccountRefNO.setAllowAll(true);
        txtRFInternalAccNo.setAllowAll(true);
        txtRFPricipalAmt.setValidation(new CurrencyValidation(16, 2));
        txtInvestmentAmount.setValidation(new CurrencyValidation(16, 2));
        txtRenewalInvestmentAmount.setValidation(new CurrencyValidation(16, 2));
        txtChequeNo.setAllowAll(true);
    }

    /**
     * Making the btnShareAccount enable or disable according to the actiontype *
     */
    private void setHelpButtonEnableDisable(boolean enable) {
        //        cboInvestmentBehaves.setEditable(enable);
        ClientUtil.enableDisable(panInvestmentDetails, enable);
        ClientUtil.enableDisable(panInvestmentDetails1, enable);
    }

    /* Does necessary operaion when user clicks the save button */
    private void savePerformed() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_SB") || behaves.equals("OTHER_BANK_CA") || behaves.equals("OTHER_BANK_SPD")) {
            if (txtSBAccountRefNO.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Please Enter Account Ref/No... !!!");
                return;
            }
            updateOperativeOBFields();
        } else if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_RD")
                || behaves.equals("OTHER_BANK_SSD")) {
            if (txtFDAccountRefNO.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Please Enter Account Ref/No... !!!");
                return;
            }
            updateDepositOBFields();
            if (rdoRenewalWithNewNo.isSelected() == true) {
                if (txtRenewalFDAccountRefNO.getText().length() <= 0) {
                    ClientUtil.showMessageWindow("Please Enter Account Ref/No... !!!");
                    return;
                }
            }
            if (rdoRenewal_yes.isSelected() == true) {
                double interestAmt = 0.0;
                interestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue();
//                if(interestAmt>0){
//                    interestAmt= interestAmt + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue();
//                }else{
//                    interestAmt= CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue();
//                }
                if (interestAmt > 0) {
                    observable.setRenewalInterestAmount(String.valueOf(interestAmt));
                } else {
                    observable.setRenewalInterestAmount("");
                }
                if (rdoRenewalWithPartialInterest.isSelected() == true) {
//                    observable.setRenewalInterestAmount(txtRenewalFDInterestReceivable.getText());
                }
                updateRenewalDepositOBFields();
                updateRenewalOBFields();
            }
        } else if (behaves.equals("SHARES_DCB") || behaves.equals("SHARE_OTHER_INSTITUTIONS")) {
            if (txtShareMemberID.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Please Enter Member Number... !!!");
                return;
            }
            updateShareOBFields();
        } else if (behaves.equals("RESERVE_FUND_DCB")) {
            if (txtRFAccountRefNO.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Please Enter Account Ref/No... !!!");
                return;
            }
            updateReserveFundOBFields();
        }

        String action;
        if (txtInvestmentID.getText().length() <= 0) {
            ClientUtil.displayAlert("Please select Investment Id");
            btnInvestmentID.setEnabled(true);
            return;
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            updateOBFields();
            action = CommonConstants.TOSTATUS_INSERT;
            double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
            double trn_amt = CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()).doubleValue();
             if((observable.callForBehaves().equals("SHARES_DCB") || observable.callForBehaves().equals("SHARE_OTHER_INSTITUTIONS")) && txtFeesPaid.getText().length() > 0){
                 trn_amt = trn_amt + CommonUtil.convertObjToDouble(txtFeesPaid.getText()).doubleValue();
             }
            if (transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0) {
                ClientUtil.displayAlert("Transaction Records Not Available");
                return;
            } else if (trn_amt != transTotalAmt) {
                ClientUtil.displayAlert("Transaction Amount Not Tailed");
                return;
            } else {               
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                saveAction(action);
            }       
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            System.out.println("Editing");
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
            action = CommonConstants.TOSTATUS_UPDATE;
            if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_SSD")) {
                System.out.println("enetrtryuririwr#%#R#%#%#%111");
                if (rdoRenewal_yes.isSelected() == true && (rdoRenewalWithoutInterest.isSelected() == true || rdoRenewalWithPartialInterest.isSelected() == true)) {
                    System.out.println("enetrtryuririwr#%#R#%#%#%2222");
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    double trn_amt = CommonUtil.convertObjToDouble(txtRenewalInvestmentAmount.getText()).doubleValue();
                    if (transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0) {
//                        ClientUtil.displayAlert("Transaction Records Not Available");
//                        return;
                        saveAction(action);
                        System.out.println("enetrtryuririwr#%#R#%#%#%333");
                    } else if (trn_amt != transTotalAmt) {
                        System.out.println("enetrtryuririwr#%#R#%#%#%444");
                        ClientUtil.displayAlert("Transaction Amount Not Tailed");
                        return;
                    } else {
                        //observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        System.out.println("enetrtryuririwr#%#R#%#%#%");
                            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()), curDate) != 0){
                                if (rdoRenewalWithPartialInterest.isSelected() == true || rdoRenewalWithoutInterest.isSelected() == true) {                                    
                                        if (transactionUI.getOutputTO() != null || (transactionUI.getOutputTO()).size() != 0) {
                                            if(transactionUI.getCallingTransType()!=null && transactionUI.getCallingTransType().equals("CASH") ){
                                                ClientUtil.showMessageWindow("Cash Transaction Not Allowed as BackDated!!Please check the Transaction Date!!");
                                                return;
                                            }else{
                                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                                saveAction(action);
                                            }
                                        }else{
                                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                            saveAction(action);
                                        }
                                    } else{
                                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                        saveAction(action);
                                    }
                                }else{
                                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                    saveAction(action);
                                }
                        }
                    }else{
                        saveAction(action);
                    }
                }
            //}            
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Are you sure, you want to Delete this Account? ", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo == 0) {
                saveAction(action);
            } else {
                btnCancelActionPerformed(null);
            }
        }
    }

    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try {
            final String mandatoryMessage = checkMandatory(panInvestmentTypesDetails);
            String alertMsg = "";
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                observable.execute(status);
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    if (observable.getProxyReturnMap() != null && (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST"))) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                    //Added by nithya on 04-02-2022 for KD-3287
                    if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("RENEWAL_COMPLETED") && observable.getProxyReturnMap().get("RENEWAL_COMPLETED") != null) {
                        ClientUtil.showMessageWindow("Investment Renewed");
                    }
                    int yesNo = 0;
                    String behaves = observable.callForBehaves();
                    if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_RD") || behaves.equals("OTHER_BANK_SSD"))) {
                        yesNo = 0;
                        String[] option = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Do You Want to Create One More Deposit?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, option, option[0]);
                        if (yesNo == 0) {
                            btnNew.setEnabled(false);
                            btnSave.setEnabled(true);
                            txtFDAccountRefNO.setText("");
                            observable.setTxtFDAccountRefNO("");
                        } else {
                            clearScreen();
                            this.btnCancelActionPerformed(null);
                        }
                    } else {
                        clearScreen();
                        btnCancelActionPerformed(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearScreen() {
        btnSave.setEnabled(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        this.btnCancelActionPerformed(null);
        setButtonEnableDisable();
        observable.resetForm();
        lblBranchNameIDValue.setText("");
        lblBankNameIDValue.setText("");
        ClientUtil.clearAll(panInvestment);
        ClientUtil.enableDisable(panInvestment, false);
        panInvestmentDetails.setVisible(false);
        renInterestAmt=0;
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
        lblBranchNameIDValue.setText("");
        lblBankNameIDValue.setText("");
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
        HashMap whereMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])
                || currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            if (viewType.equals("Delete")) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentEditDeleteDetails");
            }
            if (currField.equals(ClientConstants.ACTION_STATUS[17]) || viewType.equals("Edit")) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentEnqueryDetails");
            }
        } else if (viewType.equals("Closed_Details")) {
            viewMap.put(CommonConstants.MAP_NAME, "getClosedInvDepositDetails");
        } else if (viewType.equals("InvestmentProductSBorCATrans")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
            String bheaves = "";
            observable.setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboInvestmentType.getSelectedItem()));
            bheaves = observable.callForBehavesSBorCATrans();
            whereMap.put("INVESTMENT_TYPE", bheaves);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("RenewalInvestmentProductSBorCATrans")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProductRenewal");//getSelectInvestmentProduct");
            String bheaves = "";
            observable.setCboRenewalInvestmentTypeSBorCA(CommonUtil.convertObjToStr(cboRenewalInvestmentType.getSelectedItem()));
            bheaves = observable.callForRenewalBehavesSBorCATrans();
            whereMap.put("INVESTMENT_TYPE", bheaves);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (viewType.equals("InvestmentRefNoTrans")) {
            whereMap.put("PROD_ID", txtInvestmentIDTransSBorCA.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getInvestTransOtherBankOpperativeAccNos");
        } else if (viewType.equals("RenewalInvestmentRefNoTrans")) {
            whereMap.put("PROD_ID", txtRenewalInvestmentIDTransSBorCA.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getInvestTransOtherBankOpperativeAccNosReneal");//getInvestTransOtherBankOpperativeAccNos");
        } else if (currField == "RENEWAL_DEP_TRANS_ACC_NO") {
            String prodType = ((ComboBoxModel) cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            }
            whereMap.put("PROD_ID", ((ComboBoxModel) cboRenewalDepTransProdId.getModel()).getKeyForSelected());
            if (whereMap.get("SELECTED_BRANCH") == null) {
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            } else {
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
//            new ViewAll(this, viewMap).show();
        }
        else {
            whereMap.put("INVESTMENT_TYPE", ((ComboBoxModel) cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
            if (viewType.equals("RenewalInvestmentProduct")) {
                whereMap.put("INVESTMENT_TYPE", ((ComboBoxModel) cboRenewalInvestmentBehaves.getModel()).getKeyForSelected().toString());
            }
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this, viewMap).show();
    }

    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object map) {
        System.out.println("maps"+map);
        System.out.println("viewType"+viewType);
        setModified(true);
        HashMap hash = (HashMap) map;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) { // Added by nithya on 07-04-2020 for KD 1643
        HashMap transMap1 = new HashMap();
        transMap1.put("INVESTMENT_ID", CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
        List pendingList = ClientUtil.executeQuery("getSelectInvestmentTrans", transMap1);
        if (pendingList != null && pendingList.size() > 0) {
            HashMap hashTrans = (HashMap) pendingList.get(0);
            if (hashTrans.size()>0 && hashTrans!=null) {
                ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first  ");
                hashTrans = null;
                pendingList = null;
                return;
            }
        }
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI= (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                    || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                setButtonEnableDisable();
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("ACTION_TYPE", viewType);
                hash.put("TRANS_DT", hash.get("TRANS_DT"));
                observable.populateData(hash);
                tblCheckBookTable.setModel(observable.getTblCheckBookTable());
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                        || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panInvestment, false);
                    setHelpButtonEnableDisable(false);

                } else {
                    if (CommonUtil.convertObjToDouble(observable.getOutstandingAmount()).doubleValue() > 0) {
                        ClientUtil.enableDisable(panInvestment, false);
                        ClientUtil.enableDisable(panInvestmentDetails1, false);

                    } else {
                        ClientUtil.enableDisable(panInvestment, true);
                        ClientUtil.enableDisable(panInvestmentDetails1, true);
                        setHelpButtonEnableDisable(true);
                    }

                }
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();                
                }
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                    String renewal = CommonUtil.convertObjToStr(observable.getRdoRenewal());
                    if (renewal.length() > 0 && renewal.equals("Y")) {
                        statusType = "AUTHORIZED";
                        btnSave.setEnabled(false);
                        panRenewalDetails.setVisible(true);
                        panWithInterestYesNo.setVisible(false);
                        ClientUtil.enableDisable(panRenewalDetails, false);
                        ClientUtil.enableDisable(panInvestment, false);
                        ClientUtil.enableDisable(panInsideRenewalDetails, false);
                        if (CommonUtil.convertObjToStr(observable.getRdoRenewalWithInterest()).equals("Y")) {
                            tabRenewalTransDetails.setVisible(false);
                        } else {
                            tabRenewalTransDetails.setVisible(true);
                        }
                        if (viewType.equals(AUTHORIZE)) {
                            updateRenewalTransDetail();
                            panRenewalInvestAmountDetails.setVisible(false);
                        } else {
                            panRenewalInvestAmountDetails.setVisible(true);
                        }
                    } else {
                        statusType = "";
                        panRenewalDetails.setVisible(true);
                        ClientUtil.enableDisable(panRenewalDetails, true);
                        ClientUtil.enableDisable(panInsideRenewalDetails, true);
                    }
                    btnRenewalInvestmentID.setEnabled(false);
                }
                if (hash.containsKey("INVESTMENT_TYPE")) {
                    String InvestmentType = "";
                    InvestmentType = CommonUtil.convertObjToStr(hash.get("INVESTMENT_TYPE"));
                    if (InvestmentType.equals("OTHER_BANK_CA") || InvestmentType.equals("OTHER_BANK_SB") || InvestmentType.equals("OTHER_BANK_SPD")) {
                        updateOperative();
                    } else if (InvestmentType.equals("OTHER_BANK_CCD") || InvestmentType.equals("OTHER_BANK_FD") || InvestmentType.equals("OTHER_BANK_SSD") || InvestmentType.equals("OTHER_BANK_RD")) {
                        updateDeposit();
                    } else if (InvestmentType.equals("SHARES_DCB") || InvestmentType.equals("SHARE_OTHER_INSTITUTIONS")) {
                        updateShare();
                    } else if (InvestmentType.equals("RESERVE_FUND_DCB")) {
                        updateReserveFund();
                    }
                    cboInvestmentBehavesActionPerformed(null);
                    txtSBInternalAccNo.setEnabled(false);
                    txtFDInternalAccNo.setEnabled(false);
                    txtShareInternalAccNo.setEnabled(false);
                    txtRFInternalAccNo.setEnabled(false);
                    investmentId = "";
                    investmentId = CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID"));
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
                transCashMap.put("TRANS_DT", curDate);
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
//                int yesNo = 0;
//                String[] voucherOptions = {"Yes", "No"};
//                if ((list != null && list.size() > 0) || (list1 != null && list1.size() > 0)) {
//                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
//                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                            null, voucherOptions, voucherOptions[0]);
//                    if (yesNo == 0) {
//                        com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
//                        HashMap paramMap = new HashMap();
//                        paramMap.put("TransDt", ClientUtil.getCurrentDate());
//                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
//                        Object keys[] = transIdMap.keySet().toArray();
//                        for (int i = 0; i < keys.length; i++) {
//                            paramMap.put("TransId", keys[i]);
//                            ttIntgration.setParam(paramMap);
//                            if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
//                                ttIntgration.integrationForPrint("ReceiptPayment");
//                            } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
//                                ttIntgration.integrationForPrint("CashPayment", false);
//                            } else {
//                                ttIntgration.integrationForPrint("CashReceipt", false);
//                            }
//                        }
//                    }
//                }
                transCashMap.clear();
                transCashMap = null;
                transIdMap = null;
                transTypeMap = null;
            } else if (viewType.equals("Closed_Details")) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("ACTION_TYPE", viewType);
                observable.populateData(hash);
                updateDeposit();
                cboInvestmentBehavesActionPerformed(null);
                txtSBInternalAccNo.setEnabled(false);
                txtFDInternalAccNo.setEnabled(false);
                txtShareInternalAccNo.setEnabled(false);
                txtRFInternalAccNo.setEnabled(false);
                txtFDInterestReceived.setText(txtFDInterestReceivable.getText());
            } else if (viewType.equals("InvestmentProduct")) {
                txtInvestmentID.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtInvestmentName.setText(CommonUtil.convertObjToStr(hash.get("IINVESTMENT_PROD_DESC")));
                txtInvestmentName.setEnabled(false);
            } else if (viewType.equals("BANK_CODE")) {
                System.out.println("hash" + hash);
                observable.setTxtBankCode(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                txtBankCodeID.setText(observable.getTxtBankCode());
                lblBankNameIDValue.setText(CommonUtil.convertObjToStr(hash.get("BANK_NAME")));
            } else if (viewType.equals("BANK_BRANCH")) {
                observable.setTxtBranchCode(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtBranchCodeID.setText(observable.getTxtBranchCode());
                lblBranchNameIDValue.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
            } else if (viewType.equals("InvestmentProductSBorCATrans")) {
                txtInvestmentIDTransSBorCA.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtInvestmentRefNoTrans.setText("");
                txtInvestmentInternalNoTrans.setText("");
                txtChequeNo.setText("");
                setTransOtherBankDetails();
            } else if (viewType.equals("RenewalInvestmentProductSBorCATrans")) {
                txtRenewalInvestmentIDTransSBorCA.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtRenewalInvestmentRefNoTrans.setText("");
                txtRenewalInvestmentInternalNoTrans.setText("");
                setRenewalTransOtherBankDetails();
            } else if (viewType.equals("InvestmentRefNoTrans")) {
                txtInvestmentRefNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVES_REF_NO")));
                txtInvestmentInternalNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
            } else if (viewType.equals("RenewalInvestmentRefNoTrans")) {
                txtRenewalInvestmentRefNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVES_REF_NO")));
               // txtRenewalInvestmentInternalNoTrans.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_ID")));
                 txtRenewalInvestmentInternalNoTrans.setText(CommonUtil.convertObjToStr(hash.get("ACT_REF_NO")));
            } else if (viewType.equals("RenewalInvestmentProduct")) {
                txtRenewalInvestmentID.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
                txtRenewalInvestmentName.setText(CommonUtil.convertObjToStr(hash.get("IINVESTMENT_PROD_DESC")));
                txtRenewalInvestmentName.setEnabled(false);
            }else if (viewType.equals("RENEWAL_DEP_TRANS_ACC_NO")) {
//            renewalSubNo = true;
                System.out.println("hash.get(A/C HEAD)  "+hash);
            String prodType = ((ComboBoxModel) cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString();
            if (prodType != null && !prodType.equals("GL")) {
                if (prodType.equals("TD") && hash.containsKey("ACCOUNTNO")) {
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                }
                txtRenewalDepositID.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                lblRenewalCustNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                observable.setTxtRenewalDepositID(txtRenewalDepositID.getText());
            } else {
                txtRenewalDepositID.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblRenewalCustNameValue.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
            }
        }
            if (viewType.equals(AUTHORIZE) || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                String renewal = CommonUtil.convertObjToStr(observable.getRdoRenewal());
                if (renewal.length() > 0 && renewal.equals("Y")) {
                    tabClosingType.setVisible(false);
                } else {
                    tabClosingType.setVisible(true);
                }
            } else {
                tabClosingType.setVisible(false);
            }

            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                String behaves = observable.callForBehaves();
                if (tdtFDMaturityDt.getDateValue().length() > 0) {
                    long daydiff = 0;
                    java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
                    daydiff = DateUtil.dateDiff(matDate, curDate);
                    System.out.println("#############  daydiff " + daydiff);
                    if (daydiff < 0) {
                        panRenewalDetails.setVisible(false);
                    } else {
                        panRenewalDetails.setVisible(true);
                    }
                    if (behaves.equals("OTHER_BANK_RD")) {
                        panRenewalDetails.setVisible(false);
                    }
                }
            }
            if (hash.containsKey("MATURITY_DT")) {
                if (DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("MATURITY_DT"))).before(curDate)) {
                    rdoRenewal_yes.setSelected(true);
                    rdoRenewal_yesActionPerformed(null);
                    btnSave.setEnabled(true);
                }
            }

        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && observable.getAvailableNoOfUnits() > 0) {
            ClientUtil.enableDisable(panInvestment, false);
            ClientUtil.enableDisable(panInvestmentDetails, true);
            //            cboInterestPaymentFrequency.setEnabled(true);
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
    }

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

    private void setRenewalTransOtherBankDetails() {
        transactionUI.setSourceScreen("INVESTMENT");
        HashMap otherBankachdMap = new HashMap();
        String otherBankAccHead = "";
        otherBankachdMap.put("INVESTMENT_PROD_ID", txtRenewalInvestmentIDTransSBorCA.getText());
        List otherBankAchdLst = ClientUtil.executeQuery("getSelectinvestmentAccountHead", otherBankachdMap);
        if (otherBankAchdLst != null && otherBankAchdLst.size() > 0) {
            otherBankachdMap = (HashMap) otherBankAchdLst.get(0);
            otherBankAccHead = CommonUtil.convertObjToStr(otherBankachdMap.get("IINVESTMENT_AC_HD"));
            SetRenewalTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
            observable.setCallingTransAcctNo(otherBankAccHead);
            transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
        }
    }

    /**
     * This will show the alertwindow when the user enters the already existing
     * ShareType *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    //Changed BY Suresh
    private void calculateMatDate() {
        java.util.Date depDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFDEffectiveDt.getDateValue());
        System.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
            if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
                cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtFDInvestmentPeriod_Years.getText()));
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
                cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtFDInvestmentPeriod_Months.getText()));
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if ((txtFDInvestmentPeriod_Days.getText() != null) && (!txtFDInvestmentPeriod_Days.getText().equals(""))) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtFDInvestmentPeriod_Days.getText()));
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            observable.setTdtFDMaturityDt(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime())));
            tdtFDMaturityDt.setDateValue(DateUtil.getStringDate(observable.getTdtFDMaturityDt()));
        }
        if (txtFDInvestmentPeriod_Years.getText().length() == 0) {
            txtFDInvestmentPeriod_Years.setText("0");
        }
        if (txtFDInvestmentPeriod_Months.getText().length() == 0) {
            txtFDInvestmentPeriod_Months.setText("0");
        }
        if (txtFDInvestmentPeriod_Days.getText().length() == 0) {
            txtFDInvestmentPeriod_Days.setText("0");
        }
    }

    /**
     * This will do necessary operation for authorization *
     */
    public void authorizeStatus(String authorizeStatus) {
        try {
            if (!viewType.equals(AUTHORIZE)) {
                viewType = AUTHORIZE;
                HashMap mapParam = new HashMap();
                HashMap whereMap = new HashMap();
                whereMap.put("TRANS_DT", curDate.clone());
                whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                mapParam.put(CommonConstants.MAP_WHERE, whereMap);
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectInvestmentCashierAuthDetails");
                } else {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectInvestmentAuthDetails");
                }
                //            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeInvestmentMaster");
                mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
                authorizeUI.show();
                setModified(true);
                btnCancel.setEnabled(true);
                btnSave.setEnabled(false);
            } else if (viewType.equals(AUTHORIZE) && investmentId.length() > 0) {

//                if(rdoRenewal_yes.isSelected()==true){
//                    double interestAmt = 0.0;
//                    interestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue();
//                    if(interestAmt>0){
//                        observable.setRenewalInterestAmount(String.valueOf(interestAmt));
//                    }else{
//                        observable.setRenewalInterestAmount("");
//                    }
//                    updateRenewalDepositOBFields();
//                    updateRenewalOBFields();
//                }


                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("CLOSE_DT", ClientUtil.getCurrentDate());
                singleAuthorizeMap.put("INVESTMENT_ID", investmentId);
                System.out.println("singleAuthorizeMap----------->" + singleAuthorizeMap);
                if (transactionUI.getOutputTO().size() > 0 || (cboRenewalInvestmentBehaves.getSelectedIndex() > 0 && txtRenewalFDInternalAccNo.getText().length() > 0 && rdoRenewal_yes.isSelected())) {
                    HashMap singleAuthMap = new HashMap();
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                    authDataMap.put("BATCH_ID", observable.getBatch_Id());
                    authDataMap.put("INVESTMENT_NAME", observable.getInvestmentName());
                    authDataMap.put("INVESTMENT_ID", investmentId);
                    System.out.println("authDataMap : " + authDataMap);
                    arrList.add(authDataMap);
                    singleAuthMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                    singleAuthMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    singleAuthMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);//KD-4023
                    singleAuthMap.put("INVESTMENT_ID", investmentId);
                    observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
                    String behaves = observable.callForBehaves();
                    if (behaves.equals("OTHER_BANK_SB") || behaves.equals("OTHER_BANK_CA") || behaves.equals("OTHER_BANK_SPD")) {
                        updateOperativeOBFields();
                    } else if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_RD")
                            || behaves.equals("OTHER_BANK_SSD")) {
                        updateDepositOBFields();
                        if (rdoRenewal_yes.isSelected() == true && txtRenewalFDInternalAccNo.getText().length() > 0 && !behaves.equals("OTHER_BANK_RD")) {
                            updateRenewalOBFields();
                            updateRenewalDepositOBFields();
                            calcRenewalTransAmount();
                            singleAuthMap.put("RENEWAL", "RENEWAL");
                        }
                    } else if (behaves.equals("SHARES_DCB") || behaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                        updateShareOBFields();
                    } else if (behaves.equals("RESERVE_FUND_DCB")) {
                        updateReserveFundOBFields();
                    }
                    updateOBFields();
                    if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                    observable.setAuthorizeMap(singleAuthMap);

                    if (rdoRenewal_yes.isSelected() == true) {
                        double interestAmt = 0.0;
                        interestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue();
                        if (interestAmt > 0) {
                            observable.setRenewalInterestAmount(String.valueOf(interestAmt));
                        } else {
                            observable.setRenewalInterestAmount("");
                        }
                        updateRenewalDepositOBFields();
                        updateRenewalOBFields();
                    }
                    observable.execute("AUTHORIZE");
                } else  {
                    ClientUtil.execute("authorizeInvestMasterDetails", singleAuthorizeMap);
                    btnCancelActionPerformed(null);
                    lblStatus.setText(authorizeStatus);
                }
                if (observable.getProxyReturnMap() != null && (observable.getProxyReturnMap().containsKey("COMPLETED"))) {
                    String behaves = observable.callForBehaves();
                    if (!behaves.equals("") && behaves.length() > 0) {
                        if (behaves.equals("OTHER_BANK_SB") || behaves.equals("OTHER_BANK_CA") || behaves.equals("OTHER_BANK_SPD")) {
                            ClientUtil.execute("authorizeInvestmentOperative", singleAuthorizeMap);
                            ClientUtil.execute("authorizeChequeBookDetails", singleAuthorizeMap);
                            if (authorizeStatus != "AUTHORIZED") {
                                ClientUtil.execute("rejectOldInvestmentsDetails", singleAuthorizeMap);
                            }
                        } else if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_SSD")
                                || behaves.equals("OTHER_BANK_RD")) {
                            String renewal = CommonUtil.convertObjToStr(observable.getRdoRenewal());
                            if (renewal.length() > 0 && renewal.equals("Y")) {
                                ClientUtil.execute("authorizeInvestmentRenewalDeposit", singleAuthorizeMap);
                            }
                            if (cboRenewalInvestmentBehaves.getSelectedIndex() > 0 && txtRenewalFDInternalAccNo.getText().length() > 0 && rdoRenewal_yes.isSelected()) {
                                singleAuthorizeMap.put("STATUS", "AUTHORIZED");
                            }
                            ClientUtil.execute("authorizeInvestmentDeposit", singleAuthorizeMap);
                        } else if (behaves.equals("SHARES_DCB") || behaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                            ClientUtil.execute("authorizeInvestmentShare", singleAuthorizeMap);
                        } else if (behaves.equals("RESERVE_FUND_DCB")) {
                            ClientUtil.execute("authorizeInvestmentRD", singleAuthorizeMap);
                        }
                        ClientUtil.execute("authorizeInvestMasterDetails", singleAuthorizeMap);
                        viewType = "";
                         if (fromNewAuthorizeUI) {
                            newauthorizeListUI.removeSelectedRow();
                            this.dispose();
                            newauthorizeListUI.setFocusToTable();
                            newauthorizeListUI.displayDetails("Investment Master");
                        }
                        if (fromAuthorizeUI) {
                            authorizeListUI.removeSelectedRow();
                            this.dispose();
                            authorizeListUI.setFocusToTable();
                            authorizeListUI.displayDetails("Investment Master");
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
                        lblStatus.setText(authorizeStatus);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            e.printStackTrace();
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

        rdgSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgCheckBookAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgWithPrincipal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRenewal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgWithInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSBorCA = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRenewalWithNo = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRenewalWithInt = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnClosedDetails = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabInvestment = new com.see.truetransact.uicomponent.CTabbedPane();
        panInvestment = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCallOption = new com.see.truetransact.uicomponent.CLabel();
        chkCallOption = new com.see.truetransact.uicomponent.CCheckBox();
        lblPutOption = new com.see.truetransact.uicomponent.CLabel();
        chkPutOption = new com.see.truetransact.uicomponent.CCheckBox();
        lblSetupOption = new com.see.truetransact.uicomponent.CLabel();
        chkSetupOption = new com.see.truetransact.uicomponent.CCheckBox();
        rdoHfm = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHft = new com.see.truetransact.uicomponent.CRadioButton();
        lblClassifaction = new com.see.truetransact.uicomponent.CLabel();
        rdoHfs = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSlr = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNonSlr = new com.see.truetransact.uicomponent.CRadioButton();
        txtSetUpOptionNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        lblSetUpOptionNoOfYears = new com.see.truetransact.uicomponent.CLabel();
        lblCallOptionNoOfYears = new com.see.truetransact.uicomponent.CLabel();
        txtCallOptionNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        lblPutOptionNoOfYears = new com.see.truetransact.uicomponent.CLabel();
        txtPutOptionNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        panBankCodeAndBranchCode = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCodeID = new com.see.truetransact.uicomponent.CLabel();
        lblBankCodeID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameID = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameIDValue = new com.see.truetransact.uicomponent.CLabel();
        panBankCode = new com.see.truetransact.uicomponent.CPanel();
        txtBankCodeID = new com.see.truetransact.uicomponent.CTextField();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        panInvestmentID2 = new com.see.truetransact.uicomponent.CPanel();
        txtBranchCodeID = new com.see.truetransact.uicomponent.CTextField();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        panSecurityType = new com.see.truetransact.uicomponent.CPanel();
        panType = new com.see.truetransact.uicomponent.CPanel();
        rdoCentralSecurity = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStateSecurity = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOtherSecurity = new com.see.truetransact.uicomponent.CRadioButton();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        txtOthersName = new com.see.truetransact.uicomponent.CTextField();
        lblOthersName = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentTypesDetails = new com.see.truetransact.uicomponent.CPanel();
        panInvestmentID = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentID = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentID = new com.see.truetransact.uicomponent.CButton();
        lblInvestmentBehaves = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblInvestmentID = new com.see.truetransact.uicomponent.CLabel();
        lblInvestmentName = new com.see.truetransact.uicomponent.CLabel();
        txtInvestmentName = new com.see.truetransact.uicomponent.CTextField();
        panInsideDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFDInvestmentPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblFDAccountRefNO = new com.see.truetransact.uicomponent.CLabel();
        lblFDInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtFDInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtFDAccountRefNO = new com.see.truetransact.uicomponent.CTextField();
        lblFDAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFDAccOpenDt = new com.see.truetransact.uicomponent.CDateField();
        lblFDAgencyName = new com.see.truetransact.uicomponent.CLabel();
        txtFDAgencyName = new com.see.truetransact.uicomponent.CTextField();
        lblFDRateOfInt = new com.see.truetransact.uicomponent.CLabel();
        txtFDRateOfInt = new com.see.truetransact.uicomponent.CTextField();
        panFDInvestmentPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtFDInvestmentPeriod_Years = new com.see.truetransact.uicomponent.CTextField();
        lblInvestmentPeriod_Years1 = new com.see.truetransact.uicomponent.CLabel();
        txtFDInvestmentPeriod_Months = new com.see.truetransact.uicomponent.CTextField();
        lblInvestmentPeriod_Months1 = new com.see.truetransact.uicomponent.CLabel();
        txtFDInvestmentPeriod_Days = new com.see.truetransact.uicomponent.CTextField();
        lblInvestmentPeriod_Days1 = new com.see.truetransact.uicomponent.CLabel();
        tdtFDEffectiveDt = new com.see.truetransact.uicomponent.CDateField();
        lblFDEffectiveDt = new com.see.truetransact.uicomponent.CLabel();
        lblFDInterestPaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboFDInterestPaymentFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblFDPricipalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFDPricipalAmt = new com.see.truetransact.uicomponent.CTextField();
        panShareDetails = new com.see.truetransact.uicomponent.CPanel();
        lblShareAgencyName = new com.see.truetransact.uicomponent.CLabel();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        lblShareAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtShareType = new com.see.truetransact.uicomponent.CTextField();
        txtShareAgencyName = new com.see.truetransact.uicomponent.CTextField();
        tdtShareAccOpenDt = new com.see.truetransact.uicomponent.CDateField();
        lblNoOfShares = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfShares = new com.see.truetransact.uicomponent.CTextField();
        lblShareValue = new com.see.truetransact.uicomponent.CLabel();
        txtShareValue = new com.see.truetransact.uicomponent.CTextField();
        lblFeesPaid = new com.see.truetransact.uicomponent.CLabel();
        txtFeesPaid = new com.see.truetransact.uicomponent.CTextField();
        lblShareInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtShareInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        panShareMemberID = new com.see.truetransact.uicomponent.CPanel();
        txtShareMemberID = new com.see.truetransact.uicomponent.CTextField();
        lblShareFaceValue = new com.see.truetransact.uicomponent.CLabel();
        txtShareFaceValue = new com.see.truetransact.uicomponent.CTextField();
        panCheckBookTable = new com.see.truetransact.uicomponent.CPanel();
        panSBCheckBookTable = new com.see.truetransact.uicomponent.CPanel();
        srpCheckBookTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCheckBookTable = new com.see.truetransact.uicomponent.CTable();
        panChequeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromNO = new com.see.truetransact.uicomponent.CLabel();
        txtFromNO = new com.see.truetransact.uicomponent.CTextField();
        lblToNO = new com.see.truetransact.uicomponent.CLabel();
        txtToNO = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfCheques = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfCheques = new com.see.truetransact.uicomponent.CTextField();
        panCheckBookBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCheckBookNew = new com.see.truetransact.uicomponent.CButton();
        btnCheckBookSave = new com.see.truetransact.uicomponent.CButton();
        btnCheckBookDelete = new com.see.truetransact.uicomponent.CButton();
        panInsideSBDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSBAgencyName = new com.see.truetransact.uicomponent.CLabel();
        lblSBOperatorParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblSBAccountRefNO = new com.see.truetransact.uicomponent.CLabel();
        lblSBAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        lblSBInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblCheckBookAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtSBInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtSBAccountRefNO = new com.see.truetransact.uicomponent.CTextField();
        txtSBAgencyName = new com.see.truetransact.uicomponent.CTextField();
        tdtSBAccOpenDt = new com.see.truetransact.uicomponent.CDateField();
        panCheckBookAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoCheckBookAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCheckBookAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        srpSBTxtAreaOperatorDetails = new com.see.truetransact.uicomponent.CScrollPane();
        txtSBAreaOperatorDetails = new com.see.truetransact.uicomponent.CTextArea();
        lblStopPayment = new com.see.truetransact.uicomponent.CLabel();
        txtStopPayment = new com.see.truetransact.uicomponent.CTextField();
        btnReset = new com.see.truetransact.uicomponent.CButton();
        btnUpdate = new com.see.truetransact.uicomponent.CButton();
        panReserveFund = new com.see.truetransact.uicomponent.CPanel();
        lblRFAccountRefNO = new com.see.truetransact.uicomponent.CLabel();
        lblRFInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtRFInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtRFAccountRefNO = new com.see.truetransact.uicomponent.CTextField();
        lblRFAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRFAccOpenDt = new com.see.truetransact.uicomponent.CDateField();
        lblRFAgencyName = new com.see.truetransact.uicomponent.CLabel();
        txtRFAgencyName = new com.see.truetransact.uicomponent.CTextField();
        lblRFPricipalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRFPricipalAmt = new com.see.truetransact.uicomponent.CTextField();
        tabClosingType = new com.see.truetransact.uicomponent.CTabbedPane();
        panClosingPosition = new com.see.truetransact.uicomponent.CPanel();
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
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        srpTxtNarration = new com.see.truetransact.uicomponent.CScrollPane();
        txtNarration = new com.see.truetransact.uicomponent.CTextArea();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panInsideDepositDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panInsideDepositDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblFDMaturityDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFDMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        lblWithPrincipal = new com.see.truetransact.uicomponent.CLabel();
        panWithPrincipalYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoWithPrincipal_no = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWithPrincipal_yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblFDInterestReceivable = new com.see.truetransact.uicomponent.CLabel();
        txtFDInterestReceivable = new com.see.truetransact.uicomponent.CTextField();
        lblFDMaturityAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFDMaturityAmt = new com.see.truetransact.uicomponent.CTextField();
        lblFDInterestReceived = new com.see.truetransact.uicomponent.CLabel();
        txtFDInterestReceived = new com.see.truetransact.uicomponent.CTextField();
        lblFDPeriodicIntrest = new com.see.truetransact.uicomponent.CLabel();
        txtFDPeriodicIntrest = new com.see.truetransact.uicomponent.CTextField();
        tdtFDIntReceivedTillDt = new com.see.truetransact.uicomponent.CDateField();
        lblFDIntReceivedTillDt = new com.see.truetransact.uicomponent.CLabel();
        panRenewalDetails = new com.see.truetransact.uicomponent.CPanel();
        panRenewallYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewal_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewal_no = new com.see.truetransact.uicomponent.CRadioButton();
        panWithInterestYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoWithInterest_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWithInterest_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblRenewal = new com.see.truetransact.uicomponent.CLabel();
        panInsideRenewalDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideRenewalDetailsData = new com.see.truetransact.uicomponent.CPanel();
        panRenewalNoDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalWithDiffProdID = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountAllowed3 = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalWithSameNo = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalWithNewNo = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalWithPartialInterest = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalWithInterest = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalWithoutInterest = new com.see.truetransact.uicomponent.CRadioButton();
        panRenewalInsideDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalFDInvestmentPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalFDAccountRefNO = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalFDInternalAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDInternalAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtRenewalFDAccountRefNO = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalFDAgencyName = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDAgencyName = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalFDRateOfInt = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDRateOfInt = new com.see.truetransact.uicomponent.CTextField();
        panRenewalFDInvestmentPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalFDInvestmentPeriod_Years = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInvestmentPeriod_Years = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDInvestmentPeriod_Months = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInvestmentPeriod_Months = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDInvestmentPeriod_Days = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInvestmentPeriod_Days = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalFDInterestPaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalFDInterestPaymentFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalFDPricipalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDPricipalAmt = new com.see.truetransact.uicomponent.CTextField();
        panRenewalInvestmentID = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalInvestmentID = new com.see.truetransact.uicomponent.CTextField();
        btnRenewalInvestmentID = new com.see.truetransact.uicomponent.CButton();
        cboRenewalInvestmentBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalInvestmentBehaves = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInvestmentID = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInvestmentName = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalInvestmentName = new com.see.truetransact.uicomponent.CTextField();
        panInsideDepositDetailsRenewal = new com.see.truetransact.uicomponent.CPanel();
        panInsideRenewalDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalFDMaturityDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalFDMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        lblRenewalFDInterestReceivable = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDInterestReceivable = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalFDMaturityAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDMaturityAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalFDPeriodicIntrest = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalFDPeriodicIntrest = new com.see.truetransact.uicomponent.CTextField();
        panWithdrawalIntAmount = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalWithdrawalAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalWithdrawalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalTotalInrestAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalTotalInrest = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalFDAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalFDAccOpenDt = new com.see.truetransact.uicomponent.CDateField();
        lblRenewalFDEffectiveDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalFDEffectiveDt = new com.see.truetransact.uicomponent.CDateField();
        lblInvestTDS = new com.see.truetransact.uicomponent.CLabel();
        txtInvestTDS = new com.see.truetransact.uicomponent.CTextField();
        lblTransactionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtTransactionDt = new com.see.truetransact.uicomponent.CDateField();
        panAddAmountPanel = new com.see.truetransact.uicomponent.CPanel();
        panAddAmountMainpanel = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalAddingDepTrans = new com.see.truetransact.uicomponent.CLabel();
        chkAddMountToDeposit = new com.see.truetransact.uicomponent.CCheckBox();
        tabRenewalTransDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panRenewalInvTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panRenewalInvestmentSBorCATrans = new com.see.truetransact.uicomponent.CPanel();
        panRenewalSBorCA = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalSBorCA_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalSBorCA_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblRenewalTransCashOrTransfer = new com.see.truetransact.uicomponent.CLabel();
        panRenewalInvestSBorCATrans = new com.see.truetransact.uicomponent.CPanel();
        panRenewalInvestmentIDTrans = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalInvestmentIDTransSBorCA = new com.see.truetransact.uicomponent.CTextField();
        btnRenewalInvestmentIDTrans = new com.see.truetransact.uicomponent.CButton();
        lblRenewalInvestmentType = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalInvestmentType = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalInvestmentIDTrans = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInvestmentRefNoTrans = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalInvestmentRefNoTrans = new com.see.truetransact.uicomponent.CTextField();
        txtRenewalInvestmentInternalNoTrans = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInvestmentInternalNoTrans = new com.see.truetransact.uicomponent.CLabel();
        btnRenewalInvestmentIDTransSBorCA = new com.see.truetransact.uicomponent.CButton();
        panRenewalInvestAmountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalInvestmentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalNarration = new com.see.truetransact.uicomponent.CLabel();
        srpTxtNarration1 = new com.see.truetransact.uicomponent.CScrollPane();
        txtRenewalNarration = new com.see.truetransact.uicomponent.CTextArea();
        panRenewalTransaction = new com.see.truetransact.uicomponent.CPanel();
        cPanAddAmountToDeposits = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalDepTransAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalDepositTransMode = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalDepTransAmtValue = new com.see.truetransact.uicomponent.CTextField();
        cboRenewalDepTransMode = new com.see.truetransact.uicomponent.CComboBox();
        cboRenewalDepTransProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboRenewalDepTransProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalDepositTransProdId = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalDepositTransProdType = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalDepositTransAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalDepositID = new com.see.truetransact.uicomponent.CTextField();
        btnRenewalInvestmentsID = new com.see.truetransact.uicomponent.CButton();
        lblRenewalDepositTransCustName = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalCustNameValue = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(860, 665));
        setPreferredSize(new java.awt.Dimension(860, 665));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
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
        tbrShareProduct.add(btnNew);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace27);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace28);

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
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace29);

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

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace30);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace32);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        lblSpace8.setText("     ");
        tbrShareProduct.add(lblSpace8);

        btnClosedDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClosedDetails.setToolTipText("Closed Deposits");
        btnClosedDetails.setMinimumSize(new java.awt.Dimension(29, 27));
        btnClosedDetails.setPreferredSize(new java.awt.Dimension(29, 27));
        btnClosedDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosedDetailsActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClosedDetails);

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

        tabInvestment.setMinimumSize(new java.awt.Dimension(850, 480));
        tabInvestment.setPreferredSize(new java.awt.Dimension(850, 480));

        panInvestment.setMinimumSize(new java.awt.Dimension(547, 480));
        panInvestment.setPreferredSize(new java.awt.Dimension(547, 480));
        panInvestment.setLayout(new java.awt.GridBagLayout());

        panInvestmentDetails1.setMaximumSize(new java.awt.Dimension(10, 10));
        panInvestmentDetails1.setMinimumSize(new java.awt.Dimension(0, 0));
        panInvestmentDetails1.setPreferredSize(new java.awt.Dimension(0, 0));
        panInvestmentDetails1.setLayout(new java.awt.GridBagLayout());

        panInvestmentDetails.setMaximumSize(new java.awt.Dimension(10, 10));
        panInvestmentDetails.setLayout(new java.awt.GridBagLayout());

        lblCallOption.setText("Call Option");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblCallOption, gridBagConstraints);

        chkCallOption.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkCallOption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkCallOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCallOptionActionPerformed(evt);
            }
        });
        chkCallOption.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chkCallOptionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(chkCallOption, gridBagConstraints);

        lblPutOption.setText("Put Option");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblPutOption, gridBagConstraints);

        chkPutOption.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPutOption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkPutOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPutOptionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(chkPutOption, gridBagConstraints);

        lblSetupOption.setText("Setup Option");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblSetupOption, gridBagConstraints);

        chkSetupOption.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSetupOption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkSetupOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSetupOptionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(chkSetupOption, gridBagConstraints);

        rdoHfm.setText("HFM");
        rdoHfm.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoHfm.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoHfm.setPreferredSize(new java.awt.Dimension(60, 18));
        rdoHfm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHfmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInvestmentDetails.add(rdoHfm, gridBagConstraints);

        rdoHft.setText("HFT");
        rdoHft.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoHft.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoHft.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoHft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHftActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInvestmentDetails.add(rdoHft, gridBagConstraints);

        lblClassifaction.setText("Classification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panInvestmentDetails.add(lblClassifaction, gridBagConstraints);

        rdoHfs.setText("HFS");
        rdoHfs.setMaximumSize(new java.awt.Dimension(72, 18));
        rdoHfs.setMinimumSize(new java.awt.Dimension(72, 18));
        rdoHfs.setPreferredSize(new java.awt.Dimension(68, 18));
        rdoHfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHfsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInvestmentDetails.add(rdoHfs, gridBagConstraints);

        rdoSlr.setText("SLR");
        rdoSlr.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoSlr.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoSlr.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoSlr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSlrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInvestmentDetails.add(rdoSlr, gridBagConstraints);

        rdoNonSlr.setText("Non SLR");
        rdoNonSlr.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoNonSlr.setMinimumSize(new java.awt.Dimension(77, 18));
        rdoNonSlr.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoNonSlr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonSlrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 5);
        panInvestmentDetails.add(rdoNonSlr, gridBagConstraints);

        txtSetUpOptionNoOfYears.setMaximumSize(new java.awt.Dimension(40, 21));
        txtSetUpOptionNoOfYears.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSetUpOptionNoOfYears.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        panInvestmentDetails.add(txtSetUpOptionNoOfYears, gridBagConstraints);

        lblSetUpOptionNoOfYears.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblSetUpOptionNoOfYears, gridBagConstraints);

        lblCallOptionNoOfYears.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblCallOptionNoOfYears, gridBagConstraints);

        txtCallOptionNoOfYears.setMaximumSize(new java.awt.Dimension(40, 21));
        txtCallOptionNoOfYears.setMinimumSize(new java.awt.Dimension(40, 21));
        txtCallOptionNoOfYears.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panInvestmentDetails.add(txtCallOptionNoOfYears, gridBagConstraints);

        lblPutOptionNoOfYears.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblPutOptionNoOfYears, gridBagConstraints);

        txtPutOptionNoOfYears.setMaximumSize(new java.awt.Dimension(40, 21));
        txtPutOptionNoOfYears.setMinimumSize(new java.awt.Dimension(40, 21));
        txtPutOptionNoOfYears.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panInvestmentDetails.add(txtPutOptionNoOfYears, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInvestmentDetails1.add(panInvestmentDetails, gridBagConstraints);

        panBankCodeAndBranchCode.setMaximumSize(new java.awt.Dimension(10, 10));
        panBankCodeAndBranchCode.setLayout(new java.awt.GridBagLayout());

        lblBranchCodeID.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBankCodeAndBranchCode.add(lblBranchCodeID, gridBagConstraints);

        lblBankCodeID.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBankCodeAndBranchCode.add(lblBankCodeID, gridBagConstraints);

        lblBranchNameID.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBankCodeAndBranchCode.add(lblBranchNameID, gridBagConstraints);

        lblBranchNameIDValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblBranchNameIDValue.setMaximumSize(new java.awt.Dimension(180, 21));
        lblBranchNameIDValue.setMinimumSize(new java.awt.Dimension(180, 21));
        lblBranchNameIDValue.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBankCodeAndBranchCode.add(lblBranchNameIDValue, gridBagConstraints);

        lblBankNameID.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBankCodeAndBranchCode.add(lblBankNameID, gridBagConstraints);

        lblBankNameIDValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblBankNameIDValue.setMaximumSize(new java.awt.Dimension(180, 21));
        lblBankNameIDValue.setMinimumSize(new java.awt.Dimension(180, 21));
        lblBankNameIDValue.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBankCodeAndBranchCode.add(lblBankNameIDValue, gridBagConstraints);

        panBankCode.setMinimumSize(new java.awt.Dimension(131, 25));
        panBankCode.setPreferredSize(new java.awt.Dimension(131, 25));
        panBankCode.setLayout(new java.awt.GridBagLayout());

        txtBankCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBankCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panBankCode.add(txtBankCodeID, gridBagConstraints);

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBankCode.add(btnBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panBankCodeAndBranchCode.add(panBankCode, gridBagConstraints);

        panInvestmentID2.setMinimumSize(new java.awt.Dimension(131, 25));
        panInvestmentID2.setPreferredSize(new java.awt.Dimension(131, 25));
        panInvestmentID2.setLayout(new java.awt.GridBagLayout());

        txtBranchCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBranchCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panInvestmentID2.add(txtBranchCodeID, gridBagConstraints);

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentID2.add(btnBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panBankCodeAndBranchCode.add(panInvestmentID2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInvestmentDetails1.add(panBankCodeAndBranchCode, gridBagConstraints);

        panSecurityType.setLayout(new java.awt.GridBagLayout());

        panType.setMinimumSize(new java.awt.Dimension(200, 20));
        panType.setPreferredSize(new java.awt.Dimension(200, 20));
        panType.setLayout(new java.awt.GridBagLayout());

        rdgSecurityType.add(rdoCentralSecurity);
        rdoCentralSecurity.setText("Centeral");
        rdoCentralSecurity.setMaximumSize(new java.awt.Dimension(75, 18));
        rdoCentralSecurity.setMinimumSize(new java.awt.Dimension(75, 18));
        rdoCentralSecurity.setPreferredSize(new java.awt.Dimension(75, 18));
        rdoCentralSecurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCentralSecurityActionPerformed(evt);
            }
        });
        rdoCentralSecurity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoCentralSecurityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panType.add(rdoCentralSecurity, gridBagConstraints);

        rdgSecurityType.add(rdoStateSecurity);
        rdoStateSecurity.setText("State");
        rdoStateSecurity.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoStateSecurity.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoStateSecurity.setPreferredSize(new java.awt.Dimension(60, 18));
        rdoStateSecurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStateSecurityActionPerformed(evt);
            }
        });
        rdoStateSecurity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoStateSecurityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panType.add(rdoStateSecurity, gridBagConstraints);

        rdgSecurityType.add(rdoOtherSecurity);
        rdoOtherSecurity.setText("Others");
        rdoOtherSecurity.setMaximumSize(new java.awt.Dimension(65, 18));
        rdoOtherSecurity.setMinimumSize(new java.awt.Dimension(65, 18));
        rdoOtherSecurity.setPreferredSize(new java.awt.Dimension(75, 18));
        rdoOtherSecurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOtherSecurityActionPerformed(evt);
            }
        });
        rdoOtherSecurity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoOtherSecurityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panType.add(rdoOtherSecurity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        panSecurityType.add(panType, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panSecurityType.add(lblType, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboState.setName("cboState"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType.add(cboState, gridBagConstraints);

        lblState.setText("State");
        lblState.setName("lblState"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSecurityType.add(lblState, gridBagConstraints);

        txtOthersName.setMinimumSize(new java.awt.Dimension(240, 21));
        txtOthersName.setPreferredSize(new java.awt.Dimension(240, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType.add(txtOthersName, gridBagConstraints);

        lblOthersName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panSecurityType.add(lblOthersName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInvestmentDetails1.add(panSecurityType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panInvestment.add(panInvestmentDetails1, gridBagConstraints);

        panInvestmentTypesDetails.setMinimumSize(new java.awt.Dimension(475, 85));
        panInvestmentTypesDetails.setPreferredSize(new java.awt.Dimension(475, 85));
        panInvestmentTypesDetails.setLayout(new java.awt.GridBagLayout());

        panInvestmentID.setLayout(new java.awt.GridBagLayout());

        txtInvestmentID.setEnabled(false);
        txtInvestmentID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panInvestmentTypesDetails.add(panInvestmentID, gridBagConstraints);

        lblInvestmentBehaves.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 2);
        panInvestmentTypesDetails.add(lblInvestmentBehaves, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        panInvestmentTypesDetails.add(cboInvestmentBehaves, gridBagConstraints);

        lblInvestmentID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentID.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentTypesDetails.add(lblInvestmentID, gridBagConstraints);

        lblInvestmentName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentName.setText("Investment Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panInvestmentTypesDetails.add(lblInvestmentName, gridBagConstraints);

        txtInvestmentName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtInvestmentName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtInvestmentName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 86);
        panInvestmentTypesDetails.add(txtInvestmentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInvestment.add(panInvestmentTypesDetails, gridBagConstraints);

        panInsideDepositDetails.setMinimumSize(new java.awt.Dimension(475, 220));
        panInsideDepositDetails.setPreferredSize(new java.awt.Dimension(475, 220));
        panInsideDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblFDInvestmentPeriod.setText("Investment Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDInvestmentPeriod, gridBagConstraints);

        lblFDAccountRefNO.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDAccountRefNO, gridBagConstraints);

        lblFDInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDInternalAccNo, gridBagConstraints);

        txtFDInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(txtFDInternalAccNo, gridBagConstraints);

        txtFDAccountRefNO.setMinimumSize(new java.awt.Dimension(150, 21));
        txtFDAccountRefNO.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(txtFDAccountRefNO, gridBagConstraints);

        lblFDAccOpenDt.setText("A/c Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDAccOpenDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(tdtFDAccOpenDt, gridBagConstraints);

        lblFDAgencyName.setText("Bank/Agency Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 2);
        panInsideDepositDetails.add(lblFDAgencyName, gridBagConstraints);

        txtFDAgencyName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFDAgencyName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 0);
        panInsideDepositDetails.add(txtFDAgencyName, gridBagConstraints);

        lblFDRateOfInt.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panInsideDepositDetails.add(lblFDRateOfInt, gridBagConstraints);

        txtFDRateOfInt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFDRateOfInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDRateOfIntFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 1);
        panInsideDepositDetails.add(txtFDRateOfInt, gridBagConstraints);

        panFDInvestmentPeriod.setMinimumSize(new java.awt.Dimension(248, 29));
        panFDInvestmentPeriod.setPreferredSize(new java.awt.Dimension(248, 29));
        panFDInvestmentPeriod.setLayout(new java.awt.GridBagLayout());

        txtFDInvestmentPeriod_Years.setMinimumSize(new java.awt.Dimension(23, 21));
        txtFDInvestmentPeriod_Years.setPreferredSize(new java.awt.Dimension(23, 21));
        txtFDInvestmentPeriod_Years.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDInvestmentPeriod_YearsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFDInvestmentPeriod.add(txtFDInvestmentPeriod_Years, gridBagConstraints);

        lblInvestmentPeriod_Years1.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panFDInvestmentPeriod.add(lblInvestmentPeriod_Years1, gridBagConstraints);

        txtFDInvestmentPeriod_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtFDInvestmentPeriod_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        txtFDInvestmentPeriod_Months.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDInvestmentPeriod_MonthsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 0);
        panFDInvestmentPeriod.add(txtFDInvestmentPeriod_Months, gridBagConstraints);

        lblInvestmentPeriod_Months1.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panFDInvestmentPeriod.add(lblInvestmentPeriod_Months1, gridBagConstraints);

        txtFDInvestmentPeriod_Days.setAllowAll(true);
        txtFDInvestmentPeriod_Days.setMinimumSize(new java.awt.Dimension(45, 21));
        txtFDInvestmentPeriod_Days.setPreferredSize(new java.awt.Dimension(45, 21));
        txtFDInvestmentPeriod_Days.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDInvestmentPeriod_DaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 2);
        panFDInvestmentPeriod.add(txtFDInvestmentPeriod_Days, gridBagConstraints);

        lblInvestmentPeriod_Days1.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panFDInvestmentPeriod.add(lblInvestmentPeriod_Days1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 55);
        panInsideDepositDetails.add(panFDInvestmentPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(tdtFDEffectiveDt, gridBagConstraints);

        lblFDEffectiveDt.setText("Effective Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDEffectiveDt, gridBagConstraints);

        lblFDInterestPaymentFrequency.setText("Int Pay Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDInterestPaymentFrequency, gridBagConstraints);

        cboFDInterestPaymentFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboFDInterestPaymentFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFDInterestPaymentFrequency.setPopupWidth(100);
        cboFDInterestPaymentFrequency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboFDInterestPaymentFrequencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(cboFDInterestPaymentFrequency, gridBagConstraints);

        lblFDPricipalAmt.setText("Principal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideDepositDetails.add(lblFDPricipalAmt, gridBagConstraints);

        txtFDPricipalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFDPricipalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDPricipalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideDepositDetails.add(txtFDPricipalAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInvestment.add(panInsideDepositDetails, gridBagConstraints);

        panShareDetails.setMinimumSize(new java.awt.Dimension(475, 220));
        panShareDetails.setPreferredSize(new java.awt.Dimension(475, 220));
        panShareDetails.setLayout(new java.awt.GridBagLayout());

        lblShareAgencyName.setText("Bank/Agency Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panShareDetails.add(lblShareAgencyName, gridBagConstraints);

        lblShareType.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblShareType, gridBagConstraints);

        lblShareAccOpenDt.setText("Share A/c Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblShareAccOpenDt, gridBagConstraints);

        lblMemberNo.setText("Member Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblMemberNo, gridBagConstraints);

        txtShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(txtShareType, gridBagConstraints);

        txtShareAgencyName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtShareAgencyName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 1, 110);
        panShareDetails.add(txtShareAgencyName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(tdtShareAccOpenDt, gridBagConstraints);

        lblNoOfShares.setText("Number Of Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblNoOfShares, gridBagConstraints);

        txtNoOfShares.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfShares.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfSharesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(txtNoOfShares, gridBagConstraints);

        lblShareValue.setText("Total Share Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblShareValue, gridBagConstraints);

        txtShareValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(txtShareValue, gridBagConstraints);

        lblFeesPaid.setText("Fees Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 4);
        panShareDetails.add(lblFeesPaid, gridBagConstraints);

        txtFeesPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFeesPaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFeesPaidFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 1);
        panShareDetails.add(txtFeesPaid, gridBagConstraints);

        lblShareInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblShareInternalAccNo, gridBagConstraints);

        txtShareInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(txtShareInternalAccNo, gridBagConstraints);

        panShareMemberID.setLayout(new java.awt.GridBagLayout());

        txtShareMemberID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareMemberID.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panShareMemberID.add(txtShareMemberID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 2);
        panShareDetails.add(panShareMemberID, gridBagConstraints);

        lblShareFaceValue.setText("Share Face Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panShareDetails.add(lblShareFaceValue, gridBagConstraints);

        txtShareFaceValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareFaceValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareFaceValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 1);
        panShareDetails.add(txtShareFaceValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInvestment.add(panShareDetails, gridBagConstraints);

        panCheckBookTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque Book Details"));
        panCheckBookTable.setMinimumSize(new java.awt.Dimension(350, 175));
        panCheckBookTable.setPreferredSize(new java.awt.Dimension(350, 175));
        panCheckBookTable.setLayout(new java.awt.GridBagLayout());

        panSBCheckBookTable.setMinimumSize(new java.awt.Dimension(250, 155));
        panSBCheckBookTable.setPreferredSize(new java.awt.Dimension(250, 155));
        panSBCheckBookTable.setLayout(new java.awt.GridBagLayout());

        srpCheckBookTable.setMinimumSize(new java.awt.Dimension(250, 150));
        srpCheckBookTable.setPreferredSize(new java.awt.Dimension(250, 150));

        tblCheckBookTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Issue Dt", "From No", "To  No", "Quantity"
            }
        ));
        tblCheckBookTable.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 250));
        tblCheckBookTable.setPreferredSize(new java.awt.Dimension(225, 800));
        tblCheckBookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCheckBookTableMousePressed(evt);
            }
        });
        srpCheckBookTable.setViewportView(tblCheckBookTable);

        panSBCheckBookTable.add(srpCheckBookTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCheckBookTable.add(panSBCheckBookTable, gridBagConstraints);

        panChequeDetails.setMinimumSize(new java.awt.Dimension(195, 150));
        panChequeDetails.setPreferredSize(new java.awt.Dimension(195, 150));
        panChequeDetails.setRequestFocusEnabled(false);
        panChequeDetails.setLayout(new java.awt.GridBagLayout());

        lblFromNO.setText("From No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panChequeDetails.add(lblFromNO, gridBagConstraints);

        txtFromNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panChequeDetails.add(txtFromNO, gridBagConstraints);

        lblToNO.setText("To No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panChequeDetails.add(lblToNO, gridBagConstraints);

        txtToNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToNOFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panChequeDetails.add(txtToNO, gridBagConstraints);

        lblNoOfCheques.setText("No Of Cheques");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panChequeDetails.add(lblNoOfCheques, gridBagConstraints);

        txtNoOfCheques.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panChequeDetails.add(txtNoOfCheques, gridBagConstraints);

        panCheckBookBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panCheckBookBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panCheckBookBtn.setLayout(new java.awt.GridBagLayout());

        btnCheckBookNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnCheckBookNew.setToolTipText("New");
        btnCheckBookNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnCheckBookNew, gridBagConstraints);

        btnCheckBookSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnCheckBookSave.setToolTipText("Save");
        btnCheckBookSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.setName("btnContactNoAdd"); // NOI18N
        btnCheckBookSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnCheckBookSave, gridBagConstraints);

        btnCheckBookDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnCheckBookDelete.setToolTipText("Delete");
        btnCheckBookDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCheckBookDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckBookDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnCheckBookDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(10, 9, 0, 0);
        panChequeDetails.add(panCheckBookBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 36);
        panCheckBookTable.add(panChequeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInvestment.add(panCheckBookTable, gridBagConstraints);

        panInsideSBDetails.setMinimumSize(new java.awt.Dimension(475, 200));
        panInsideSBDetails.setPreferredSize(new java.awt.Dimension(475, 200));
        panInsideSBDetails.setLayout(new java.awt.GridBagLayout());

        lblSBAgencyName.setText("Bank/Agency Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panInsideSBDetails.add(lblSBAgencyName, gridBagConstraints);

        lblSBOperatorParticulars.setText("Operator Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(lblSBOperatorParticulars, gridBagConstraints);

        lblSBAccountRefNO.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(lblSBAccountRefNO, gridBagConstraints);

        lblSBAccOpenDt.setText("A/c Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(lblSBAccOpenDt, gridBagConstraints);

        lblSBInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(lblSBInternalAccNo, gridBagConstraints);

        lblCheckBookAllowed.setText("Cheque Book Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(lblCheckBookAllowed, gridBagConstraints);

        txtSBInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(txtSBInternalAccNo, gridBagConstraints);

        txtSBAccountRefNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(txtSBAccountRefNO, gridBagConstraints);

        txtSBAgencyName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtSBAgencyName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panInsideSBDetails.add(txtSBAgencyName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(tdtSBAccOpenDt, gridBagConstraints);

        panCheckBookAllowed.setMinimumSize(new java.awt.Dimension(102, 18));
        panCheckBookAllowed.setPreferredSize(new java.awt.Dimension(102, 18));
        panCheckBookAllowed.setLayout(new java.awt.GridBagLayout());

        rdgCheckBookAllowed.add(rdoCheckBookAllowed_yes);
        rdoCheckBookAllowed_yes.setText("Yes");
        rdoCheckBookAllowed_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoCheckBookAllowed_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoCheckBookAllowed_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoCheckBookAllowed_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCheckBookAllowed_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCheckBookAllowed.add(rdoCheckBookAllowed_yes, gridBagConstraints);

        rdgCheckBookAllowed.add(rdoCheckBookAllowed_no);
        rdoCheckBookAllowed_no.setText("No");
        rdoCheckBookAllowed_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoCheckBookAllowed_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoCheckBookAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCheckBookAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCheckBookAllowed.add(rdoCheckBookAllowed_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSBDetails.add(panCheckBookAllowed, gridBagConstraints);

        srpSBTxtAreaOperatorDetails.setMinimumSize(new java.awt.Dimension(232, 45));
        srpSBTxtAreaOperatorDetails.setPreferredSize(new java.awt.Dimension(232, 45));

        txtSBAreaOperatorDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtSBAreaOperatorDetails.setLineWrap(true);
        txtSBAreaOperatorDetails.setMinimumSize(new java.awt.Dimension(3, 18));
        txtSBAreaOperatorDetails.setPreferredSize(new java.awt.Dimension(3, 14));
        srpSBTxtAreaOperatorDetails.setViewportView(txtSBAreaOperatorDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panInsideSBDetails.add(srpSBTxtAreaOperatorDetails, gridBagConstraints);

        lblStopPayment.setText("Cheque Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panInsideSBDetails.add(lblStopPayment, gridBagConstraints);

        txtStopPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panInsideSBDetails.add(txtStopPayment, gridBagConstraints);

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setMaximumSize(new java.awt.Dimension(90, 27));
        btnReset.setMinimumSize(new java.awt.Dimension(90, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panInsideSBDetails.add(btnReset, gridBagConstraints);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.setMaximumSize(new java.awt.Dimension(100, 27));
        btnUpdate.setMinimumSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panInsideSBDetails.add(btnUpdate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestment.add(panInsideSBDetails, gridBagConstraints);

        panReserveFund.setMinimumSize(new java.awt.Dimension(475, 150));
        panReserveFund.setPreferredSize(new java.awt.Dimension(475, 150));
        panReserveFund.setLayout(new java.awt.GridBagLayout());

        lblRFAccountRefNO.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(lblRFAccountRefNO, gridBagConstraints);

        lblRFInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(lblRFInternalAccNo, gridBagConstraints);

        txtRFInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(txtRFInternalAccNo, gridBagConstraints);

        txtRFAccountRefNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(txtRFAccountRefNO, gridBagConstraints);

        lblRFAccOpenDt.setText("A/c Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(lblRFAccOpenDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(tdtRFAccOpenDt, gridBagConstraints);

        lblRFAgencyName.setText("Bank/Agency Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panReserveFund.add(lblRFAgencyName, gridBagConstraints);

        txtRFAgencyName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRFAgencyName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 120);
        panReserveFund.add(txtRFAgencyName, gridBagConstraints);

        lblRFPricipalAmt.setText("Amount Remitted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(lblRFPricipalAmt, gridBagConstraints);

        txtRFPricipalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRFPricipalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRFPricipalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReserveFund.add(txtRFPricipalAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        panInvestment.add(panReserveFund, gridBagConstraints);

        tabClosingType.setMinimumSize(new java.awt.Dimension(835, 255));
        tabClosingType.setPreferredSize(new java.awt.Dimension(835, 255));

        panClosingPosition.setMaximumSize(new java.awt.Dimension(795, 235));
        panClosingPosition.setMinimumSize(new java.awt.Dimension(795, 235));
        panClosingPosition.setPreferredSize(new java.awt.Dimension(795, 235));
        panClosingPosition.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestmentSBorCATrans, gridBagConstraints);

        panInvestSBorCATrans.setMinimumSize(new java.awt.Dimension(270, 135));
        panInvestSBorCATrans.setPreferredSize(new java.awt.Dimension(270, 135));
        panInvestSBorCATrans.setLayout(new java.awt.GridBagLayout());

        panInvestmentIDTrans.setLayout(new java.awt.GridBagLayout());

        txtInvestmentIDTransSBorCA.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentIDTransSBorCA.setEnabled(false);
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

        txtInvestmentRefNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentRefNoTrans.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInvestSBorCATrans.add(txtInvestmentRefNoTrans, gridBagConstraints);

        txtInvestmentInternalNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentInternalNoTrans.setEnabled(false);
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestSBorCATrans, gridBagConstraints);

        panInvestAmountDetails.setMinimumSize(new java.awt.Dimension(300, 105));
        panInvestAmountDetails.setPreferredSize(new java.awt.Dimension(300, 105));
        panInvestAmountDetails.setLayout(new java.awt.GridBagLayout());

        lblInvestmentAmount.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestAmountDetails.add(lblInvestmentAmount, gridBagConstraints);

        txtInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentAmount.setEnabled(false);
        txtInvestmentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestAmountDetails.add(txtInvestmentAmount, gridBagConstraints);

        lblNarration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarration.setText("Narration");
        lblNarration.setMinimumSize(new java.awt.Dimension(72, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestAmountDetails.add(srpTxtNarration, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panClosingPosition.add(panInvestAmountDetails, gridBagConstraints);

        tabClosingType.addTab("Investment Transaction Details", panClosingPosition);

        panTransaction.setMinimumSize(new java.awt.Dimension(795, 235));
        panTransaction.setPreferredSize(new java.awt.Dimension(795, 235));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        tabClosingType.addTab("Transaction", panTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panInvestment.add(tabClosingType, gridBagConstraints);

        panInsideDepositDetails1.setMinimumSize(new java.awt.Dimension(425, 278));
        panInsideDepositDetails1.setPreferredSize(new java.awt.Dimension(425, 278));
        panInsideDepositDetails1.setLayout(new java.awt.GridBagLayout());

        panInsideDepositDetails2.setMinimumSize(new java.awt.Dimension(425, 220));
        panInsideDepositDetails2.setPreferredSize(new java.awt.Dimension(425, 220));
        panInsideDepositDetails2.setLayout(new java.awt.GridBagLayout());

        lblFDMaturityDt.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDMaturityDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 44);
        panInsideDepositDetails2.add(tdtFDMaturityDt, gridBagConstraints);

        lblWithPrincipal.setText("To Add Interest With Pricipal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblWithPrincipal, gridBagConstraints);

        panWithPrincipalYesNo.setMinimumSize(new java.awt.Dimension(102, 18));
        panWithPrincipalYesNo.setPreferredSize(new java.awt.Dimension(102, 18));
        panWithPrincipalYesNo.setLayout(new java.awt.GridBagLayout());

        rdgWithPrincipal.add(rdoWithPrincipal_no);
        rdoWithPrincipal_no.setText("No");
        rdoWithPrincipal_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoWithPrincipal_no.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWithPrincipalYesNo.add(rdoWithPrincipal_no, gridBagConstraints);

        rdgWithPrincipal.add(rdoWithPrincipal_yes);
        rdoWithPrincipal_yes.setText("Yes");
        rdoWithPrincipal_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoWithPrincipal_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoWithPrincipal_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWithPrincipalYesNo.add(rdoWithPrincipal_yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideDepositDetails2.add(panWithPrincipalYesNo, gridBagConstraints);

        lblFDInterestReceivable.setText("Interest Receivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDInterestReceivable, gridBagConstraints);

        txtFDInterestReceivable.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFDInterestReceivable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFDInterestReceivableFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(txtFDInterestReceivable, gridBagConstraints);

        lblFDMaturityAmt.setText("Maturity Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDMaturityAmt, gridBagConstraints);

        txtFDMaturityAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(txtFDMaturityAmt, gridBagConstraints);

        lblFDInterestReceived.setText("Interest Received");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDInterestReceived, gridBagConstraints);

        txtFDInterestReceived.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFDInterestReceived.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFDInterestReceivedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(txtFDInterestReceived, gridBagConstraints);

        lblFDPeriodicIntrest.setText("Periodic Int Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDPeriodicIntrest, gridBagConstraints);

        txtFDPeriodicIntrest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(txtFDPeriodicIntrest, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(tdtFDIntReceivedTillDt, gridBagConstraints);

        lblFDIntReceivedTillDt.setText("Interest Received till Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideDepositDetails2.add(lblFDIntReceivedTillDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 50);
        panInsideDepositDetails1.add(panInsideDepositDetails2, gridBagConstraints);

        panRenewalDetails.setMinimumSize(new java.awt.Dimension(420, 50));
        panRenewalDetails.setPreferredSize(new java.awt.Dimension(420, 50));
        panRenewalDetails.setLayout(new java.awt.GridBagLayout());

        panRenewallYesNo.setMinimumSize(new java.awt.Dimension(102, 18));
        panRenewallYesNo.setPreferredSize(new java.awt.Dimension(102, 18));
        panRenewallYesNo.setLayout(new java.awt.GridBagLayout());

        rdgRenewal.add(rdoRenewal_yes);
        rdoRenewal_yes.setText("Yes");
        rdoRenewal_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoRenewal_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoRenewal_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoRenewal_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewal_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panRenewallYesNo.add(rdoRenewal_yes, gridBagConstraints);

        rdgRenewal.add(rdoRenewal_no);
        rdoRenewal_no.setText("No");
        rdoRenewal_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoRenewal_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoRenewal_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewal_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panRenewallYesNo.add(rdoRenewal_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panRenewalDetails.add(panRenewallYesNo, gridBagConstraints);

        panWithInterestYesNo.setMinimumSize(new java.awt.Dimension(230, 25));
        panWithInterestYesNo.setPreferredSize(new java.awt.Dimension(230, 25));
        panWithInterestYesNo.setLayout(new java.awt.GridBagLayout());

        rdgWithInterest.add(rdoWithInterest_yes);
        rdoWithInterest_yes.setText("With Interest");
        rdoWithInterest_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoWithInterest_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoWithInterest_yes.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoWithInterest_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWithInterest_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWithInterestYesNo.add(rdoWithInterest_yes, gridBagConstraints);

        rdgWithInterest.add(rdoWithInterest_no);
        rdoWithInterest_no.setText("Without Interest");
        rdoWithInterest_no.setMaximumSize(new java.awt.Dimension(120, 21));
        rdoWithInterest_no.setMinimumSize(new java.awt.Dimension(120, 21));
        rdoWithInterest_no.setPreferredSize(new java.awt.Dimension(120, 21));
        rdoWithInterest_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWithInterest_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWithInterestYesNo.add(rdoWithInterest_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panRenewalDetails.add(panWithInterestYesNo, gridBagConstraints);

        lblRenewal.setText("Renewal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 104, 4, 4);
        panRenewalDetails.add(lblRenewal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 50);
        panInsideDepositDetails1.add(panRenewalDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestment.add(panInsideDepositDetails1, gridBagConstraints);

        tabInvestment.addTab("Investment Master Details", panInvestment);

        panInsideRenewalDetails.setMinimumSize(new java.awt.Dimension(814, 320));
        panInsideRenewalDetails.setPreferredSize(new java.awt.Dimension(814, 320));
        panInsideRenewalDetails.setLayout(new java.awt.GridBagLayout());

        panInsideRenewalDetailsData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideRenewalDetailsData.setMinimumSize(new java.awt.Dimension(835, 355));
        panInsideRenewalDetailsData.setPreferredSize(new java.awt.Dimension(835, 355));
        panInsideRenewalDetailsData.setLayout(new java.awt.GridBagLayout());

        panRenewalNoDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRenewalNoDetails.setMinimumSize(new java.awt.Dimension(700, 30));
        panRenewalNoDetails.setPreferredSize(new java.awt.Dimension(700, 30));
        panRenewalNoDetails.setLayout(new java.awt.GridBagLayout());

        rdgRenewalWithNo.add(rdoRenewalWithDiffProdID);
        rdoRenewalWithDiffProdID.setText("Renewal Under  a Different Product ID");
        rdoRenewalWithDiffProdID.setMaximumSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithDiffProdID.setMinimumSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithDiffProdID.setPreferredSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithDiffProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithDiffProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panRenewalNoDetails.add(rdoRenewalWithDiffProdID, gridBagConstraints);

        panDiscountAllowed3.setLayout(new java.awt.GridBagLayout());

        rdgRenewalWithNo.add(rdoRenewalWithSameNo);
        rdoRenewalWithSameNo.setText("Renewal with Same Number");
        rdoRenewalWithSameNo.setMaximumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithSameNo.setMinimumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithSameNo.setPreferredSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithSameNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithSameNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDiscountAllowed3.add(rdoRenewalWithSameNo, gridBagConstraints);

        rdgRenewalWithNo.add(rdoRenewalWithNewNo);
        rdoRenewalWithNewNo.setText("Renewal with a New Number");
        rdoRenewalWithNewNo.setMaximumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithNewNo.setMinimumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithNewNo.setPreferredSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithNewNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithNewNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDiscountAllowed3.add(rdoRenewalWithNewNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panRenewalNoDetails.add(panDiscountAllowed3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsideRenewalDetailsData.add(panRenewalNoDetails, gridBagConstraints);

        panInterestDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInterestDetails.setMinimumSize(new java.awt.Dimension(700, 30));
        panInterestDetails.setPreferredSize(new java.awt.Dimension(700, 30));
        panInterestDetails.setLayout(new java.awt.GridBagLayout());

        rdgRenewalWithInt.add(rdoRenewalWithPartialInterest);
        rdoRenewalWithPartialInterest.setText("Renewal with Partial Interest");
        rdoRenewalWithPartialInterest.setMaximumSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithPartialInterest.setMinimumSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithPartialInterest.setPreferredSize(new java.awt.Dimension(245, 21));
        rdoRenewalWithPartialInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithPartialInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 4);
        panInterestDetails.add(rdoRenewalWithPartialInterest, gridBagConstraints);

        rdgRenewalWithInt.add(rdoRenewalWithInterest);
        rdoRenewalWithInterest.setText("Renewal with Interest");
        rdoRenewalWithInterest.setMaximumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithInterest.setMinimumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithInterest.setPreferredSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 4);
        panInterestDetails.add(rdoRenewalWithInterest, gridBagConstraints);

        rdgRenewalWithInt.add(rdoRenewalWithoutInterest);
        rdoRenewalWithoutInterest.setText("Renewal without Interest");
        rdoRenewalWithoutInterest.setMaximumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithoutInterest.setMinimumSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithoutInterest.setPreferredSize(new java.awt.Dimension(190, 21));
        rdoRenewalWithoutInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithoutInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInterestDetails.add(rdoRenewalWithoutInterest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideRenewalDetailsData.add(panInterestDetails, gridBagConstraints);

        panRenewalInsideDepositDetails.setMinimumSize(new java.awt.Dimension(380, 265));
        panRenewalInsideDepositDetails.setPreferredSize(new java.awt.Dimension(380, 250));
        panRenewalInsideDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblRenewalFDInvestmentPeriod.setText("Investment Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDInvestmentPeriod, gridBagConstraints);

        lblRenewalFDAccountRefNO.setText("A/c Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDAccountRefNO, gridBagConstraints);

        lblRenewalFDInternalAccNo.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDInternalAccNo, gridBagConstraints);

        txtRenewalFDInternalAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRenewalInsideDepositDetails.add(txtRenewalFDInternalAccNo, gridBagConstraints);

        txtRenewalFDAccountRefNO.setAllowAll(true);
        txtRenewalFDAccountRefNO.setAllowNumber(true);
        txtRenewalFDAccountRefNO.setMinimumSize(new java.awt.Dimension(150, 21));
        txtRenewalFDAccountRefNO.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRenewalInsideDepositDetails.add(txtRenewalFDAccountRefNO, gridBagConstraints);

        lblRenewalFDAgencyName.setText("Bank/Agency Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDAgencyName, gridBagConstraints);

        txtRenewalFDAgencyName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRenewalFDAgencyName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRenewalInsideDepositDetails.add(txtRenewalFDAgencyName, gridBagConstraints);

        lblRenewalFDRateOfInt.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDRateOfInt, gridBagConstraints);

        txtRenewalFDRateOfInt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalFDRateOfInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalFDRateOfIntFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 1);
        panRenewalInsideDepositDetails.add(txtRenewalFDRateOfInt, gridBagConstraints);

        panRenewalFDInvestmentPeriod.setMinimumSize(new java.awt.Dimension(248, 29));
        panRenewalFDInvestmentPeriod.setPreferredSize(new java.awt.Dimension(248, 29));
        panRenewalFDInvestmentPeriod.setLayout(new java.awt.GridBagLayout());

        txtRenewalFDInvestmentPeriod_Years.setMinimumSize(new java.awt.Dimension(23, 21));
        txtRenewalFDInvestmentPeriod_Years.setPreferredSize(new java.awt.Dimension(23, 21));
        txtRenewalFDInvestmentPeriod_Years.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalFDInvestmentPeriod_YearsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRenewalFDInvestmentPeriod.add(txtRenewalFDInvestmentPeriod_Years, gridBagConstraints);

        lblRenewalInvestmentPeriod_Years.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panRenewalFDInvestmentPeriod.add(lblRenewalInvestmentPeriod_Years, gridBagConstraints);

        txtRenewalFDInvestmentPeriod_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtRenewalFDInvestmentPeriod_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        txtRenewalFDInvestmentPeriod_Months.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalFDInvestmentPeriod_MonthsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 0);
        panRenewalFDInvestmentPeriod.add(txtRenewalFDInvestmentPeriod_Months, gridBagConstraints);

        lblRenewalInvestmentPeriod_Months.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panRenewalFDInvestmentPeriod.add(lblRenewalInvestmentPeriod_Months, gridBagConstraints);

        txtRenewalFDInvestmentPeriod_Days.setAllowAll(true);
        txtRenewalFDInvestmentPeriod_Days.setMinimumSize(new java.awt.Dimension(45, 21));
        txtRenewalFDInvestmentPeriod_Days.setPreferredSize(new java.awt.Dimension(45, 21));
        txtRenewalFDInvestmentPeriod_Days.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalFDInvestmentPeriod_DaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 2);
        panRenewalFDInvestmentPeriod.add(txtRenewalFDInvestmentPeriod_Days, gridBagConstraints);

        lblRenewalInvestmentPeriod_Days.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panRenewalFDInvestmentPeriod.add(lblRenewalInvestmentPeriod_Days, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panRenewalInsideDepositDetails.add(panRenewalFDInvestmentPeriod, gridBagConstraints);

        lblRenewalFDInterestPaymentFrequency.setText("Int Pay Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDInterestPaymentFrequency, gridBagConstraints);

        cboRenewalFDInterestPaymentFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalFDInterestPaymentFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalFDInterestPaymentFrequency.setPopupWidth(100);
        cboRenewalFDInterestPaymentFrequency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRenewalFDInterestPaymentFrequencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRenewalInsideDepositDetails.add(cboRenewalFDInterestPaymentFrequency, gridBagConstraints);

        lblRenewalFDPricipalAmt.setText("Principal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInsideDepositDetails.add(lblRenewalFDPricipalAmt, gridBagConstraints);

        txtRenewalFDPricipalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panRenewalInsideDepositDetails.add(txtRenewalFDPricipalAmt, gridBagConstraints);

        panRenewalInvestmentID.setLayout(new java.awt.GridBagLayout());

        txtRenewalInvestmentID.setEnabled(false);
        txtRenewalInvestmentID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panRenewalInvestmentID.add(txtRenewalInvestmentID, gridBagConstraints);

        btnRenewalInvestmentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalInvestmentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalInvestmentIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestmentID.add(btnRenewalInvestmentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panRenewalInsideDepositDetails.add(panRenewalInvestmentID, gridBagConstraints);

        cboRenewalInvestmentBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalInvestmentBehaves.setPopupWidth(220);
        cboRenewalInvestmentBehaves.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboRenewalInvestmentBehavesItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 1);
        panRenewalInsideDepositDetails.add(cboRenewalInvestmentBehaves, gridBagConstraints);

        lblRenewalInvestmentBehaves.setText("Investment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        panRenewalInsideDepositDetails.add(lblRenewalInvestmentBehaves, gridBagConstraints);

        lblRenewalInvestmentID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalInvestmentID.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInsideDepositDetails.add(lblRenewalInvestmentID, gridBagConstraints);

        lblRenewalInvestmentName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalInvestmentName.setText("A/c Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panRenewalInsideDepositDetails.add(lblRenewalInvestmentName, gridBagConstraints);

        txtRenewalInvestmentName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRenewalInvestmentName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 1);
        panRenewalInsideDepositDetails.add(txtRenewalInvestmentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panInsideRenewalDetailsData.add(panRenewalInsideDepositDetails, gridBagConstraints);

        panInsideDepositDetailsRenewal.setMinimumSize(new java.awt.Dimension(420, 280));
        panInsideDepositDetailsRenewal.setPreferredSize(new java.awt.Dimension(420, 280));
        panInsideDepositDetailsRenewal.setLayout(new java.awt.GridBagLayout());

        panInsideRenewalDepositDetails.setMinimumSize(new java.awt.Dimension(420, 280));
        panInsideRenewalDepositDetails.setPreferredSize(new java.awt.Dimension(420, 280));
        panInsideRenewalDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblRenewalFDMaturityDt.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideRenewalDepositDetails.add(lblRenewalFDMaturityDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(tdtRenewalFDMaturityDt, gridBagConstraints);

        lblRenewalFDInterestReceivable.setText("Interest Receivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideRenewalDepositDetails.add(lblRenewalFDInterestReceivable, gridBagConstraints);

        txtRenewalFDInterestReceivable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(txtRenewalFDInterestReceivable, gridBagConstraints);

        lblRenewalFDMaturityAmt.setText("Maturity Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideRenewalDepositDetails.add(lblRenewalFDMaturityAmt, gridBagConstraints);

        txtRenewalFDMaturityAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(txtRenewalFDMaturityAmt, gridBagConstraints);

        lblRenewalFDPeriodicIntrest.setText("Periodic Int Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideRenewalDepositDetails.add(lblRenewalFDPeriodicIntrest, gridBagConstraints);

        txtRenewalFDPeriodicIntrest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(txtRenewalFDPeriodicIntrest, gridBagConstraints);

        panWithdrawalIntAmount.setMinimumSize(new java.awt.Dimension(280, 60));
        panWithdrawalIntAmount.setPreferredSize(new java.awt.Dimension(280, 60));
        panWithdrawalIntAmount.setLayout(new java.awt.GridBagLayout());

        txtRenewalWithdrawalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalWithdrawalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalWithdrawalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 10);
        panWithdrawalIntAmount.add(txtRenewalWithdrawalAmt, gridBagConstraints);

        lblRenewalWithdrawalAmt.setText("Interest Amount Withdrawal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWithdrawalIntAmount.add(lblRenewalWithdrawalAmt, gridBagConstraints);

        lblRenewalTotalInrestAmt.setMaximumSize(new java.awt.Dimension(100, 18));
        lblRenewalTotalInrestAmt.setMinimumSize(new java.awt.Dimension(100, 18));
        lblRenewalTotalInrestAmt.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWithdrawalIntAmount.add(lblRenewalTotalInrestAmt, gridBagConstraints);

        lblRenewalTotalInrest.setText("Total Interest Receivable Rs.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWithdrawalIntAmount.add(lblRenewalTotalInrest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsideRenewalDepositDetails.add(panWithdrawalIntAmount, gridBagConstraints);

        lblRenewalFDAccOpenDt.setText("A/c Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideRenewalDepositDetails.add(lblRenewalFDAccOpenDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(tdtRenewalFDAccOpenDt, gridBagConstraints);

        lblRenewalFDEffectiveDt.setText("Effective Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInsideRenewalDepositDetails.add(lblRenewalFDEffectiveDt, gridBagConstraints);

        tdtRenewalFDEffectiveDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRenewalFDEffectiveDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(tdtRenewalFDEffectiveDt, gridBagConstraints);

        lblInvestTDS.setText("TDS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        panInsideRenewalDepositDetails.add(lblInvestTDS, gridBagConstraints);

        txtInvestTDS.setAllowAll(true);
        txtInvestTDS.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInvestTDS.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestTDS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvestTDSActionPerformed(evt);
            }
        });
        txtInvestTDS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestTDSFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(txtInvestTDS, gridBagConstraints);

        lblTransactionDt.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInsideRenewalDepositDetails.add(lblTransactionDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideRenewalDepositDetails.add(tdtTransactionDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 50);
        panInsideDepositDetailsRenewal.add(panInsideRenewalDepositDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideRenewalDetailsData.add(panInsideDepositDetailsRenewal, gridBagConstraints);

        panAddAmountPanel.setLayout(new java.awt.GridBagLayout());

        panAddAmountMainpanel.setMinimumSize(new java.awt.Dimension(251, 20));
        panAddAmountMainpanel.setPreferredSize(new java.awt.Dimension(251, 20));

        lblRenewalAddingDepTrans.setText("Want to add amount to this deposit ?");
        panAddAmountMainpanel.add(lblRenewalAddingDepTrans);

        chkAddMountToDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAddMountToDepositActionPerformed(evt);
            }
        });
        panAddAmountMainpanel.add(chkAddMountToDeposit);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAddAmountPanel.add(panAddAmountMainpanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panInsideRenewalDetailsData.add(panAddAmountPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideRenewalDetails.add(panInsideRenewalDetailsData, gridBagConstraints);

        tabRenewalTransDetails.setMinimumSize(new java.awt.Dimension(835, 255));
        tabRenewalTransDetails.setPreferredSize(new java.awt.Dimension(835, 255));

        panRenewalInvTransDetails.setMaximumSize(new java.awt.Dimension(795, 235));
        panRenewalInvTransDetails.setMinimumSize(new java.awt.Dimension(795, 235));
        panRenewalInvTransDetails.setPreferredSize(new java.awt.Dimension(795, 235));
        panRenewalInvTransDetails.setLayout(new java.awt.GridBagLayout());

        panRenewalInvestmentSBorCATrans.setMinimumSize(new java.awt.Dimension(290, 30));
        panRenewalInvestmentSBorCATrans.setPreferredSize(new java.awt.Dimension(290, 30));
        panRenewalInvestmentSBorCATrans.setLayout(new java.awt.GridBagLayout());

        panRenewalSBorCA.setMinimumSize(new java.awt.Dimension(102, 18));
        panRenewalSBorCA.setPreferredSize(new java.awt.Dimension(102, 18));
        panRenewalSBorCA.setLayout(new java.awt.GridBagLayout());

        rdgSBorCA.add(rdoRenewalSBorCA_yes);
        rdoRenewalSBorCA_yes.setText("Yes");
        rdoRenewalSBorCA_yes.setMaximumSize(new java.awt.Dimension(46, 18));
        rdoRenewalSBorCA_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoRenewalSBorCA_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoRenewalSBorCA_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalSBorCA_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panRenewalSBorCA.add(rdoRenewalSBorCA_yes, gridBagConstraints);

        rdgSBorCA.add(rdoRenewalSBorCA_no);
        rdoRenewalSBorCA_no.setText("No");
        rdoRenewalSBorCA_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoRenewalSBorCA_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoRenewalSBorCA_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalSBorCA_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panRenewalSBorCA.add(rdoRenewalSBorCA_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panRenewalInvestmentSBorCATrans.add(panRenewalSBorCA, gridBagConstraints);

        lblRenewalTransCashOrTransfer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalTransCashOrTransfer.setText("Whether Debit From SB/CA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestmentSBorCATrans.add(lblRenewalTransCashOrTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRenewalInvTransDetails.add(panRenewalInvestmentSBorCATrans, gridBagConstraints);

        panRenewalInvestSBorCATrans.setMinimumSize(new java.awt.Dimension(270, 135));
        panRenewalInvestSBorCATrans.setPreferredSize(new java.awt.Dimension(270, 135));
        panRenewalInvestSBorCATrans.setLayout(new java.awt.GridBagLayout());

        panRenewalInvestmentIDTrans.setLayout(new java.awt.GridBagLayout());

        txtRenewalInvestmentIDTransSBorCA.setEnabled(false);
        txtRenewalInvestmentIDTransSBorCA.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panRenewalInvestmentIDTrans.add(txtRenewalInvestmentIDTransSBorCA, gridBagConstraints);

        btnRenewalInvestmentIDTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalInvestmentIDTrans.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentIDTrans.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentIDTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalInvestmentIDTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestmentIDTrans.add(btnRenewalInvestmentIDTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panRenewalInvestSBorCATrans.add(panRenewalInvestmentIDTrans, gridBagConstraints);

        lblRenewalInvestmentType.setText("Investment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestSBorCATrans.add(lblRenewalInvestmentType, gridBagConstraints);

        cboRenewalInvestmentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalInvestmentType.setPopupWidth(220);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenewalInvestSBorCATrans.add(cboRenewalInvestmentType, gridBagConstraints);

        lblRenewalInvestmentIDTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalInvestmentIDTrans.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestSBorCATrans.add(lblRenewalInvestmentIDTrans, gridBagConstraints);

        lblRenewalInvestmentRefNoTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalInvestmentRefNoTrans.setText("Account Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestSBorCATrans.add(lblRenewalInvestmentRefNoTrans, gridBagConstraints);

        txtRenewalInvestmentRefNoTrans.setEnabled(false);
        txtRenewalInvestmentRefNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenewalInvestSBorCATrans.add(txtRenewalInvestmentRefNoTrans, gridBagConstraints);

        txtRenewalInvestmentInternalNoTrans.setEnabled(false);
        txtRenewalInvestmentInternalNoTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRenewalInvestSBorCATrans.add(txtRenewalInvestmentInternalNoTrans, gridBagConstraints);

        lblRenewalInvestmentInternalNoTrans.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalInvestmentInternalNoTrans.setText("Internal A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestSBorCATrans.add(lblRenewalInvestmentInternalNoTrans, gridBagConstraints);

        btnRenewalInvestmentIDTransSBorCA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalInvestmentIDTransSBorCA.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentIDTransSBorCA.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentIDTransSBorCA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalInvestmentIDTransSBorCAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panRenewalInvestSBorCATrans.add(btnRenewalInvestmentIDTransSBorCA, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRenewalInvTransDetails.add(panRenewalInvestSBorCATrans, gridBagConstraints);

        panRenewalInvestAmountDetails.setMinimumSize(new java.awt.Dimension(300, 105));
        panRenewalInvestAmountDetails.setPreferredSize(new java.awt.Dimension(300, 105));
        panRenewalInvestAmountDetails.setLayout(new java.awt.GridBagLayout());

        lblRenewalInvestmentAmount.setText("Trans Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewalInvestAmountDetails.add(lblRenewalInvestmentAmount, gridBagConstraints);

        txtRenewalInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalInvestmentAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewalInvestAmountDetails.add(txtRenewalInvestmentAmount, gridBagConstraints);

        lblRenewalNarration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalNarration.setText("Narration");
        lblRenewalNarration.setMinimumSize(new java.awt.Dimension(72, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewalInvestAmountDetails.add(lblRenewalNarration, gridBagConstraints);

        srpTxtNarration1.setMinimumSize(new java.awt.Dimension(160, 40));
        srpTxtNarration1.setPreferredSize(new java.awt.Dimension(160, 40));

        txtRenewalNarration.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtRenewalNarration.setLineWrap(true);
        txtRenewalNarration.setMinimumSize(new java.awt.Dimension(2, 8));
        txtRenewalNarration.setPreferredSize(new java.awt.Dimension(2, 8));
        srpTxtNarration1.setViewportView(txtRenewalNarration);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRenewalInvestAmountDetails.add(srpTxtNarration1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRenewalInvTransDetails.add(panRenewalInvestAmountDetails, gridBagConstraints);

        tabRenewalTransDetails.addTab("Investment Transaction Details", panRenewalInvTransDetails);

        panRenewalTransaction.setMinimumSize(new java.awt.Dimension(795, 235));
        panRenewalTransaction.setPreferredSize(new java.awt.Dimension(795, 235));
        panRenewalTransaction.setLayout(new java.awt.GridBagLayout());
        tabRenewalTransDetails.addTab("Transaction", panRenewalTransaction);

        cPanAddAmountToDeposits.setLayout(new java.awt.GridBagLayout());

        lblRenewalDepTransAmt.setText("Amount");
        lblRenewalDepTransAmt.setMinimumSize(new java.awt.Dimension(72, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepTransAmt, gridBagConstraints);

        lblRenewalDepositTransMode.setText("Mode of Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepositTransMode, gridBagConstraints);

        txtRenewalDepTransAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalDepTransAmtValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRenewalDepTransAmtValueActionPerformed(evt);
            }
        });
        txtRenewalDepTransAmtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalDepTransAmtValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        cPanAddAmountToDeposits.add(txtRenewalDepTransAmtValue, gridBagConstraints);

        cboRenewalDepTransMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalDepTransMode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalDepTransMode.setPopupWidth(100);
        cboRenewalDepTransMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboRenewalDepTransModeItemStateChanged(evt);
            }
        });
        cboRenewalDepTransMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalDepTransModeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        cPanAddAmountToDeposits.add(cboRenewalDepTransMode, gridBagConstraints);

        cboRenewalDepTransProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalDepTransProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalDepTransProdType.setPopupWidth(200);
        cboRenewalDepTransProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalDepTransProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        cPanAddAmountToDeposits.add(cboRenewalDepTransProdType, gridBagConstraints);

        cboRenewalDepTransProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalDepTransProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalDepTransProdId.setPopupWidth(200);
        cboRenewalDepTransProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalDepTransProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        cPanAddAmountToDeposits.add(cboRenewalDepTransProdId, gridBagConstraints);

        lblRenewalDepositTransProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepositTransProdId, gridBagConstraints);

        lblRenewalDepositTransProdType.setText("Product Type");
        lblRenewalDepositTransProdType.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepositTransProdType, gridBagConstraints);

        lblRenewalDepositTransAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepositTransAccNo, gridBagConstraints);

        txtRenewalDepositID.setEnabled(false);
        txtRenewalDepositID.setMinimumSize(new java.awt.Dimension(120, 18));
        txtRenewalDepositID.setName(""); // NOI18N
        txtRenewalDepositID.setOpaque(false);
        txtRenewalDepositID.setPreferredSize(new java.awt.Dimension(120, 18));
        txtRenewalDepositID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRenewalDepositIDActionPerformed(evt);
            }
        });
        txtRenewalDepositID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalDepositIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        cPanAddAmountToDeposits.add(txtRenewalDepositID, gridBagConstraints);

        btnRenewalInvestmentsID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalInvestmentsID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentsID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRenewalInvestmentsID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalInvestmentsIDActionPerformed(evt);
            }
        });
        btnRenewalInvestmentsID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnRenewalInvestmentsIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanAddAmountToDeposits.add(btnRenewalInvestmentsID, gridBagConstraints);

        lblRenewalDepositTransCustName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRenewalDepositTransCustName.setText("Customer Name");
        lblRenewalDepositTransCustName.setMinimumSize(new java.awt.Dimension(110, 18));
        lblRenewalDepositTransCustName.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalDepositTransCustName, gridBagConstraints);

        lblRenewalCustNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblRenewalCustNameValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblRenewalCustNameValue.setName(""); // NOI18N
        lblRenewalCustNameValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        cPanAddAmountToDeposits.add(lblRenewalCustNameValue, gridBagConstraints);

        tabRenewalTransDetails.addTab("Add Amount To Deposit", cPanAddAmountToDeposits);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panInsideRenewalDetails.add(tabRenewalTransDetails, gridBagConstraints);

        tabInvestment.addTab("Renewal Details", panInsideRenewalDetails);

        getContentPane().add(tabInvestment, java.awt.BorderLayout.CENTER);

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

    private void btnClosedDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosedDetailsActionPerformed
        // TODO add your handling code here:
        lblStatus.setText("Closed_Details");
        callView("Closed_Details");
        btnCheck();
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnClosedDetailsActionPerformed

    private void txtRenewalWithdrawalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalWithdrawalAmtFocusLost
        // TODO add your handling code here:
        if (txtRenewalWithdrawalAmt.getText().length() > 0) {
            double principalAmt = (CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()
                    - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue()) - CommonUtil.convertObjToDouble(txtRenewalWithdrawalAmt.getText()).doubleValue();
            if(rdoRenewalWithPartialInterest.isSelected()){
                double totInt =CommonUtil.convertObjToDouble(lblRenewalTotalInrestAmt.getText()); 
                double withdraw =CommonUtil.convertObjToDouble(txtRenewalWithdrawalAmt.getText()); 
                if(withdraw<=totInt){
                    double newamt=totInt-withdraw;
                   // System.out.println("principalAmt-->"+principalAmt +"newamt"+newamt);
                    txtRenewalFDPricipalAmt.setText(String.valueOf(principalAmt));
                }
                else{
                    ClientUtil.showAlertWindow("Amount Grater than the interest receivable!");
                    txtRenewalWithdrawalAmt.setText(CommonUtil.convertObjToStr(0));
                    return;
                }
            }
            else{
                 txtRenewalFDPricipalAmt.setText(String.valueOf(principalAmt));
            }
            txtRenewalInvestmentAmount.setText(txtRenewalWithdrawalAmt.getText());
            txtRenewalFDInvestmentPeriod_YearsFocusLost(null);
            txtRenewalInvestmentAmount.setText(txtRenewalWithdrawalAmt.getText());
            SetRenewalTransAmount();
            String behaves = observable.callForRenewalBehaves();
            if (behaves.equals("OTHER_BANK_FD")) {
                txtRenewalFDMaturityAmt.setText(txtRenewalFDPricipalAmt.getText());
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
            }
        }
    }//GEN-LAST:event_txtRenewalWithdrawalAmtFocusLost

    private void btnRenewalInvestmentIDTransSBorCAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalInvestmentIDTransSBorCAActionPerformed
        // TODO add your handling code here:
        callView("RenewalInvestmentRefNoTrans");
    }//GEN-LAST:event_btnRenewalInvestmentIDTransSBorCAActionPerformed

    private void btnRenewalInvestmentIDTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalInvestmentIDTransActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboRenewalInvestmentType.getSelectedItem()).length() > 0) {
            callView("RenewalInvestmentProductSBorCATrans");
        } else {
            ClientUtil.displayAlert("Please Select Investment Type");
        }
    }//GEN-LAST:event_btnRenewalInvestmentIDTransActionPerformed

    private void rdoRenewalSBorCA_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalSBorCA_noActionPerformed
        // TODO add your handling code here:
        if (rdoRenewalSBorCA_no.isSelected() == true) {
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
            panRenewalInvestSBorCATrans.setVisible(false);
            cboRenewalInvestmentType.setSelectedItem("");
            txtRenewalInvestmentIDTransSBorCA.setText("");
            txtRenewalInvestmentRefNoTrans.setText("");
            txtRenewalInvestmentInternalNoTrans.setText("");
            SetRenewalTransAmount();
            transactionUI.setCallingTransType("CASH");
        }
    }//GEN-LAST:event_rdoRenewalSBorCA_noActionPerformed

    private void rdoRenewalSBorCA_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalSBorCA_yesActionPerformed
        // TODO add your handling code here:
        String bheaves = observable.callForRenewalBehaves();
        if (rdoRenewalSBorCA_yes.isSelected() == true) {
            panRenewalInvestSBorCATrans.setVisible(true);
            ClientUtil.enableDisable(panRenewalInvestSBorCATrans, false);
            cboRenewalInvestmentType.setEnabled(true);
            SetRenewalTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
        } else {
            panRenewalInvestSBorCATrans.setVisible(false);
            SetRenewalTransAmount();
            transactionUI.setCallingTransType("CASH");
        }
    }//GEN-LAST:event_rdoRenewalSBorCA_yesActionPerformed
    private void SetRenewalTransAmount() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            String bheaves = observable.callForRenewalBehaves();
            if (bheaves.length() > 0) {
//                transactionUI.cancelAction(false);
//                transactionUI.setButtonEnableDisable(true);
//                transactionUI.resetObjects();
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_SSD")) {
                    transactionUI.setCallingAmount(txtRenewalInvestmentAmount.getText());
                    if (rdoSBorCA_yes.isSelected() == true) {
                        transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                    } else {
                        transactionUI.setCallingTransType("CASH");
                    }
                }
            }
        }
    }
    private void btnRenewalInvestmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalInvestmentIDActionPerformed
        // TODO add your handling code here:
        if (((ComboBoxModel) cboRenewalInvestmentBehaves.getModel()).getKeyForSelected().toString().length() > 0) {
            callView("RenewalInvestmentProduct");
        } else {
            ClientUtil.displayAlert("Select Investment Type !!!");
            cboRenewalInvestmentBehaves.setEnabled(true);
        }
    }//GEN-LAST:event_btnRenewalInvestmentIDActionPerformed

    private void txtRenewalFDRateOfIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalFDRateOfIntFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_txtRenewalFDRateOfIntFocusLost

    private void cboRenewalFDInterestPaymentFrequencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRenewalFDInterestPaymentFrequencyFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_cboRenewalFDInterestPaymentFrequencyFocusLost

    private void txtRenewalFDInvestmentPeriod_DaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalFDInvestmentPeriod_DaysFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_txtRenewalFDInvestmentPeriod_DaysFocusLost

    private void txtRenewalFDInvestmentPeriod_MonthsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalFDInvestmentPeriod_MonthsFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_txtRenewalFDInvestmentPeriod_MonthsFocusLost

    private void txtRenewalFDInvestmentPeriod_YearsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalFDInvestmentPeriod_YearsFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_txtRenewalFDInvestmentPeriod_YearsFocusLost

    private void calcRenewalTransAmount() {
        if (rdoRenewal_yes.isSelected() == true && (rdoRenewalWithoutInterest.isSelected() == true || rdoRenewalWithPartialInterest.isSelected() == true)) {
            double interestAmt = 0.0;
            interestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue();
//            if(interestAmt>0){
//                interestAmt= interestAmt + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue();
//            }else{
//                interestAmt= CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue();
//            }
            if (interestAmt > 0) {
                if (rdoRenewalWithPartialInterest.isSelected() == true) {
                    observable.setRenewalInterestAmount(txtRenewalWithdrawalAmt.getText());
                    txtRenewalInvestmentAmount.setText(txtRenewalWithdrawalAmt.getText());
                } else {
                    observable.setRenewalInterestAmount(String.valueOf(interestAmt));
                    txtRenewalInvestmentAmount.setText(String.valueOf(interestAmt));
                }
            } else {
                observable.setRenewalInterestAmount("");
                txtRenewalInvestmentAmount.setText("");
            }
            SetRenewalTransAmount();
        } else {
            txtRenewalInvestmentAmount.setText("");
        }
    }
    private void rdoRenewalWithDiffProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithDiffProdIDActionPerformed
        // TODO add your handling code here:
        if (rdoRenewalWithDiffProdID.isSelected() == true) {
            setRenewalDetails();
            txtRenewalFDAccountRefNO.setText("");
            btnRenewalInvestmentID.setEnabled(true);
            txtRenewalFDAccountRefNO.setEnabled(true);
            cboRenewalInvestmentBehaves.setEnabled(true);
        }
    }//GEN-LAST:event_rdoRenewalWithDiffProdIDActionPerformed

    private void rdoRenewalWithNewNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithNewNoActionPerformed
        // TODO add your handling code here:
        if (rdoRenewalWithNewNo.isSelected() == true) {
            setRenewalDetails();
            btnRenewalInvestmentID.setEnabled(false);
            txtRenewalFDAccountRefNO.setEnabled(true);
            txtRenewalFDAccountRefNO.setText("");
            cboRenewalInvestmentBehaves.setEnabled(false);
        }
    }//GEN-LAST:event_rdoRenewalWithNewNoActionPerformed
private String getCashOrTransfer(){
 int rowCount = transactionUI.getTransactionOB().getTblTransDetails().getRowCount();
 String cashOrTransfer = "";
 if(rowCount > 0){
 TableModel tb = transactionUI.getTransactionOB().getTblTransDetails();
 cashOrTransfer = CommonUtil.convertObjToStr(tb.getValueAt(0, 1));
 }
 return cashOrTransfer;
}
    private void rdoRenewalWithSameNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithSameNoActionPerformed
        // TODO add your handling code here:
        if (rdoRenewalWithSameNo.isSelected() == true) {
            setRenewalDetails();
            btnRenewalInvestmentID.setEnabled(false);
            txtRenewalFDAccountRefNO.setEnabled(false);
            txtRenewalFDAccountRefNO.setText(txtFDAccountRefNO.getText());
            cboRenewalInvestmentBehaves.setEnabled(false);
        }
    }//GEN-LAST:event_rdoRenewalWithSameNoActionPerformed
private void enablingTabbedPanel(){
//    tabRenewalTransDetails.setEnabledAt(0, true);
    tabRenewalTransDetails.setEnabledAt(1, true);
}
    private void rdoRenewalWithInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithInterestActionPerformed
        // TODO add your handling code here:
        renInterestAmt=0;
        if (rdoRenewalWithInterest.isSelected() == true) {
            //Added for Mantis - 10146 
            //transactionUI.setVisible(false);
            transactionUI.setMainEnableDisable(false);
            ClientUtil.enableDisable(panRenewalTransaction, false);
            //Ends here
            txtRenewalWithdrawalAmt.setText("");
            panWithdrawalIntAmount.setVisible(false);
            tabRenewalTransDetails.setVisible(true);
//            tabRenewalTransDetails.setEnabledAt(0, false);
            tabRenewalTransDetails.setEnabledAt(1, false);
            txtRenewalWithdrawalAmt.setEnabled(false);
            txtRenewalWithdrawalAmt.setText("");
            enableDisableRenewal();
            double principalAmt = (CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()
                    - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue());
            txtRenewalFDPricipalAmt.setText(String.valueOf(principalAmt));
            String behaves = observable.callForRenewalBehaves();
            calculateRenewalMatDate();
            if (behaves.equals("OTHER_BANK_FD")) {
                calcRenewalFixedInterest();
                System.out.println("txtRenewalFDPricipalAmt.getText()111>>>" + txtRenewalFDPricipalAmt.getText());
                System.out.println("txtRenewalFDInterestReceivable111>>>" + txtRenewalFDInterestReceivable.getText());
                System.out.println("@@@111CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue())>>>" + CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue());
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()));
//                calcRenewalFixedInterest();
                cboRenewalFDInterestPaymentActionPerform();
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                calcRenewalCummulativeInterest();
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
            }
            if(chkAddMountToDeposit.isSelected() == true){
            tabRenewalTransDetails.setSelectedIndex(2);
            }else{
            tabRenewalTransDetails.setSelectedIndex(1);
            }
            tdtTransactionDt.setEnabled(true);  
            if(!(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals(""))){
                tdtTransactionDt.setDateValue(DateUtil.getStringDate(curDate));
            }
        }else{
//            tabRenewalTransDetails.setEnabledAt(0, true);
            tabRenewalTransDetails.setEnabledAt(1, true);
        }
        renInterestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText());
        txtInvestTDS.setEnabled(true);//added by sreekrishnan
    }//GEN-LAST:event_rdoRenewalWithInterestActionPerformed

    private void calcRenewalCummulativeInterest() {
        if (txtRenewalFDPricipalAmt.getText().length() > 0 && txtRenewalFDRateOfInt.getText().length() > 0 && tdtRenewalFDMaturityDt.getDateValue().length() > 0) {
            double period = 0.0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtRenewalFDMaturityDt.getDateValue()));
            java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtRenewalFDEffectiveDt.getDateValue()));
            period = DateUtil.dateDiff(effectiveDate, matDate);
            if(rdoRenewalWithInterest.isSelected()){
                 principal = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()-
                             CommonUtil.convertObjToDouble(txtInvestTDS.getText());
            }else{
                 principal = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue();
            }
            period = period / 30;
            period = (double) roundOffLower((long) (period * 100), 100) / 100;
            double rateOfInterest = CommonUtil.convertObjToDouble(txtRenewalFDRateOfInt.getText()).doubleValue() / 100;
            amount = principal * (Math.pow((1 + rateOfInterest / 4.0), period / 12 * 4.0));
            interest = amount - principal;
            interest = (double) getNearest((long) (interest * 100), 100) / 100;
            System.out.println("intAmt: " + interest);
            txtRenewalFDInterestReceivable.setText(String.valueOf(interest));
        }
    }

    private void calcRenewalFixedInterest() {
        if (txtRenewalFDPricipalAmt.getText().length() > 0 && txtRenewalFDRateOfInt.getText().length() > 0 && cboRenewalFDInterestPaymentFrequency.getSelectedIndex() > 0 && tdtRenewalFDMaturityDt.getDateValue().length() > 0) {
            double intAmt = 0.0;
            double amt = 0.0;
            double period = 0.0;
            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtRenewalFDMaturityDt.getDateValue()));
            java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtRenewalFDEffectiveDt.getDateValue()));
            period = DateUtil.dateDiff(effectiveDate, matDate);
            System.out.println("#############  period " + period);
           /* if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Monthly") && period > 29) { // Commented by nithya on 09-02-2020 for KD-2460
                System.out.println("#############  Monthly ");
                double roi = 0.0; 
                double amount = 0.0;
                if(rdoRenewalWithInterest.isSelected()){
                    amount = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()-
                         CommonUtil.convertObjToDouble(txtInvestTDS.getText());
                }else{
                    amount = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue();
                }
                roi = CommonUtil.convertObjToDouble(txtRenewalFDRateOfInt.getText()).doubleValue();
                period = period / 30;
                intAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                System.out.println("intAmt: " + intAmt);
                double calcAmt = amount / 100;
                intAmt = intAmt * calcAmt;
                intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                intAmt = intAmt * period;
            } else { */
                System.out.println("#############  Not_Monthly ");
                double principal = 0.0;
                double rateOfInterest = 0.0;
                double years = CommonUtil.convertObjToDouble(txtRenewalFDInvestmentPeriod_Years.getText()).doubleValue();
                double months = CommonUtil.convertObjToDouble(txtRenewalFDInvestmentPeriod_Months.getText()).doubleValue();                               
                if(rdoRenewalWithInterest.isSelected()){
                    principal = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()-
                         CommonUtil.convertObjToDouble(txtInvestTDS.getText());
                }else{
                    principal = CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue();
                }
                rateOfInterest = CommonUtil.convertObjToDouble(txtRenewalFDRateOfInt.getText()).doubleValue();
                years = years * 12;
                double total = years + months;
                double greateramount = principal + (principal * rateOfInterest * total / 1200);
                System.out.println("grt "+greateramount+"prin "+principal +"rate "+rateOfInterest+"total  "+total);
                double interestgreater = greateramount - principal;
                double days = CommonUtil.convertObjToDouble(txtRenewalFDInvestmentPeriod_Days.getText()).doubleValue();
                double lessamount = principal + (principal * rateOfInterest * days / 36500);
                double interestless = lessamount - principal;
                intAmt = interestgreater + interestless;
           // }
            intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
            System.out.println("intAmt: " + intAmt);
            txtRenewalFDInterestReceivable.setText(String.valueOf(intAmt));
        }
    }

    private void cboRenewalFDInterestPaymentActionPerform() {
        if ((!cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("")) && (cboRenewalFDInterestPaymentFrequency.getSelectedItem() != null)) {
            if ((txtRenewalFDInterestReceivable.getText() != null) && (!txtRenewalFDInterestReceivable.getText().equals(""))) {
                double perIntAmt = 0;
                setYr();
                double totalIntAmtPerYear = (CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()) / yr);
                if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Half Yearly")) {
                    perIntAmt = totalIntAmtPerYear / perHalfYear;
                } else if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Monthly")) {
                    perIntAmt = totalIntAmtPerYear / perMonth;
                    //--- Calculation for Period as No.Of Days
                    int YrsToDay = 0;
                    int MonToDay = 0;
                    int daysEntered = 0;
                    int periodInDays = 0;
                    if ((txtRenewalFDInvestmentPeriod_Years.getText() != null) && (!txtRenewalFDInvestmentPeriod_Years.getText().equals(""))) {
                        YrsToDay = (Integer.parseInt(txtRenewalFDInvestmentPeriod_Years.getText())) * 365;
                    }
                    if ((txtRenewalFDInvestmentPeriod_Months.getText() != null) && (!txtRenewalFDInvestmentPeriod_Months.getText().equals(""))) {
                        MonToDay = ((Integer.parseInt(txtRenewalFDInvestmentPeriod_Months.getText())) * 30);
                    }
                    if ((txtRenewalFDInvestmentPeriod_Days.getText() != null) && (!txtRenewalFDInvestmentPeriod_Days.getText().equals(""))) {
                        daysEntered = Integer.parseInt(txtRenewalFDInvestmentPeriod_Days.getText());
                    }
                    periodInDays = (YrsToDay + MonToDay + daysEntered);

                    System.out.println("perIntAmt" + perIntAmt);
                } else if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Yearly")) {
                    perIntAmt = totalIntAmtPerYear;
                } else if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Quaterly")) {
                    perIntAmt = totalIntAmtPerYear / perQuarterYear;
                } else if (cboRenewalFDInterestPaymentFrequency.getSelectedItem().equals("Date of Maturity")) {
                    perIntAmt = 0;
                }
                System.out.println("perIntAmt" + perIntAmt);
                try {
                    perIntAmt = (double) getNearest((long) (perIntAmt * 100), 100) / 100;
                    System.out.println("#### cboRenewalInterestPaymentFreqActionPerformed " + perIntAmt);
                } catch (Exception e) {
                    System.out.println(e);
                }
                observable.setTxtRenewalFDPeriodicIntrest(String.valueOf(perIntAmt));
                txtRenewalFDPeriodicIntrest.setText(observable.getTxtRenewalFDPeriodicIntrest());
            }
        }
    }

    private void calculateRenewalMatDate() {
        java.util.Date depDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtRenewalFDEffectiveDt.getDateValue());
        System.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
            if ((txtRenewalFDInvestmentPeriod_Years.getText() != null) && (!txtRenewalFDInvestmentPeriod_Years.getText().equals(""))) {
                cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtRenewalFDInvestmentPeriod_Years.getText()));
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if ((txtRenewalFDInvestmentPeriod_Months.getText() != null) && (!txtRenewalFDInvestmentPeriod_Months.getText().equals(""))) {
                cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtRenewalFDInvestmentPeriod_Months.getText()));
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if ((txtRenewalFDInvestmentPeriod_Days.getText() != null) && (!txtRenewalFDInvestmentPeriod_Days.getText().equals(""))) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtRenewalFDInvestmentPeriod_Days.getText()));
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            observable.setTdtRenewalFDMaturityDt(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime())));
            tdtRenewalFDMaturityDt.setDateValue(DateUtil.getStringDate(observable.getTdtRenewalFDMaturityDt()));
        }
        if (txtRenewalFDInvestmentPeriod_Years.getText().length() == 0) {
            txtRenewalFDInvestmentPeriod_Years.setText("0");
        }
        if (txtRenewalFDInvestmentPeriod_Months.getText().length() == 0) {
            txtRenewalFDInvestmentPeriod_Months.setText("0");
        }
        if (txtFDInvestmentPeriod_Days.getText().length() == 0) {
            txtRenewalFDInvestmentPeriod_Days.setText("0");
        }
    }

    private void enableDisableRenewal() {
        ClientUtil.enableDisable(panRenewalFDInvestmentPeriod, true);
        cboRenewalFDInterestPaymentFrequency.setEnabled(true);
        txtRenewalFDRateOfInt.setEnabled(true);
        tdtRenewalFDEffectiveDt.setEnabled(true);
    }
    private void rdoRenewalWithPartialInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithPartialInterestActionPerformed
        // TODO add your handling code here:
        renInterestAmt=0;
        if (rdoRenewalWithPartialInterest.isSelected() == true) {
            transactionUI.setVisible(true);
            //Added for Mantis - 10146 
            transactionUI.setButtonEnableDisable(true);
            //Ends here
            //rdoRenewalWithPartialInterest.setEnabled(false);
            panWithdrawalIntAmount.setVisible(true);
            tabRenewalTransDetails.setVisible(true);
            txtRenewalWithdrawalAmt.setEnabled(true);
            enableDisableRenewal();
            txtRenewalInvestmentAmount.setEnabled(false);
            double principalAmt = (CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()
                    - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue());
            txtRenewalFDPricipalAmt.setText(String.valueOf(principalAmt));
            String behaves = observable.callForRenewalBehaves();
            calculateRenewalMatDate();
            if (behaves.equals("OTHER_BANK_FD")) {
                txtRenewalFDMaturityAmt.setText(txtRenewalFDPricipalAmt.getText());
                calcRenewalFixedInterest();
                cboRenewalFDInterestPaymentActionPerform();
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                calcRenewalCummulativeInterest();
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
            }
            double totalIntRec = (CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()
                    - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue());
            lblRenewalTotalInrestAmt.setText(String.valueOf(totalIntRec));
            calcRenewalTransAmount();
            enablingTabbedPanel();
            tdtTransactionDt.setEnabled(true);    
            if(!(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals(""))){
                tdtTransactionDt.setDateValue(DateUtil.getStringDate(curDate));
            }
        }
        renInterestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText());
        txtInvestTDS.setEnabled(true);//added by sreekrishnan
    }//GEN-LAST:event_rdoRenewalWithPartialInterestActionPerformed

    private void rdoRenewalWithoutInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithoutInterestActionPerformed
        // TODO add your handling code here:
        renInterestAmt=0;
        if (rdoRenewalWithoutInterest.isSelected() == true) {
            txtRenewalWithdrawalAmt.setText("");
            //Added by sreekrishnan for renewal with out intrerest     
            System.out.println("interest $#$#$#$#$#$#"+(CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceivable())-
                    CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceived())));
            if((CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceivable())-
                    CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceived()))<=0){
               transactionUI.setMainEnableDisable(false);
               ClientUtil.enableDisable(panRenewalTransaction, false);                
            }else{
                transactionUI.setVisible(true);
                //Added for Mantis - 10146 
                transactionUI.setButtonEnableDisable(true);
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setCallingAmount(observable.getTxtFDInterestReceivable());
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                   transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW); 
                }
                //Ends here
            }
            panWithdrawalIntAmount.setVisible(false);
            tabRenewalTransDetails.setVisible(true);
            txtRenewalInvestmentAmount.setEnabled(false);
            txtRenewalWithdrawalAmt.setEnabled(false);
            txtRenewalWithdrawalAmt.setText("");
            enableDisableRenewal();
//            double principalAmt =(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()-
//            CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue());
            double principalAmt = (CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue());
            txtRenewalFDPricipalAmt.setText(String.valueOf(principalAmt));
            String behaves = observable.callForRenewalBehaves();
            calculateRenewalMatDate();
            if (behaves.equals("OTHER_BANK_FD")) {
                calcRenewalFixedInterest();
                System.out.println("txtRenewalFDPricipalAmt.getText()>>>" + txtRenewalFDPricipalAmt.getText());
                System.out.println("txtRenewalFDInterestReceivable>>>" + txtRenewalFDInterestReceivable.getText());
                System.out.println("CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue())>>>" + CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue());
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()));
//                calcRenewalFixedInterest();
                cboRenewalFDInterestPaymentActionPerform();
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                calcRenewalCummulativeInterest();
                txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
            }
            calcRenewalTransAmount();
            enablingTabbedPanel();
            tdtTransactionDt.setEnabled(true);    
            if(!(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals(""))){
                tdtTransactionDt.setDateValue(DateUtil.getStringDate(curDate));
            }
        }
        renInterestAmt = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText());
        txtInvestTDS.setEnabled(true);//added by sreekrishnan
    }//GEN-LAST:event_rdoRenewalWithoutInterestActionPerformed

    private void txtRFPricipalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRFPricipalAmtFocusLost
        // TODO add your handling code here:
        if (txtRFPricipalAmt.getText().length() > 0) {
            txtInvestmentAmount.setText(txtRFPricipalAmt.getText());
            txtInvestmentAmount.setEnabled(false);
            txtInvestmentAmountFocusLost(null);
        }
    }//GEN-LAST:event_txtRFPricipalAmtFocusLost

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

    private void txtInvestmentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentAmountFocusLost
        // TODO add your handling code here:
        SetTransAmount();
    }//GEN-LAST:event_txtInvestmentAmountFocusLost

    private void rdoSBorCA_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSBorCA_yesActionPerformed
        // TODO add your handling code here:
        String bheaves = observable.callForBehaves();
        if (rdoSBorCA_yes.isSelected() == true) {
            panInvestSBorCATrans.setVisible(true);
            txtChequeNo.setVisible(true);
            lblChequeNo.setVisible(true);
            ClientUtil.enableDisable(panInvestSBorCATrans, false);
            txtChequeNo.setEnabled(true);
            cboInvestmentType.setEnabled(true);
            SetTransAmount();
            transactionUI.setCallingTransType("TRANSFER");
        } else {
            panInvestSBorCATrans.setVisible(false);
            SetTransAmount();
            transactionUI.setCallingTransType("CASH");
        }
    }//GEN-LAST:event_rdoSBorCA_yesActionPerformed

    private void rdoSBorCA_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSBorCA_noActionPerformed
        // TODO add your handling code here:
        if (rdoSBorCA_no.isSelected() == true) {
            transactionUI.setSourceScreen("INVESTMENT_TRANS");
            panInvestSBorCATrans.setVisible(false);
            cboInvestmentType.setSelectedItem("");
            txtInvestmentIDTransSBorCA.setText("");
            txtInvestmentRefNoTrans.setText("");
            txtInvestmentInternalNoTrans.setText("");
            SetTransAmount();
            transactionUI.setCallingTransType("CASH");
            String txtBehaves = observable.callForBehaves();
        }
    }//GEN-LAST:event_rdoSBorCA_noActionPerformed
    private void SetTransAmount() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String bheaves = observable.callForBehaves();
            if (bheaves.length() > 0) {
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                if (bheaves.equals("OTHER_BANK_FD") || bheaves.equals("OTHER_BANK_CCD") || bheaves.equals("OTHER_BANK_RD") || bheaves.equals("OTHER_BANK_SSD")
                        || bheaves.equals("OTHER_BANK_SB") || bheaves.equals("OTHER_BANK_CA") || bheaves.equals("OTHER_BANK_SPD") || bheaves.equals("RESERVE_FUND_DCB")
                        || bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) {
                    transactionUI.setCallingAmount(txtInvestmentAmount.getText());
                    if((bheaves.equals("SHARES_DCB") || bheaves.equals("SHARE_OTHER_INSTITUTIONS")) && txtFeesPaid.getText().length() > 0){
                        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText())+ CommonUtil.convertObjToDouble(txtFeesPaid.getText())));
                    }
                    if (rdoSBorCA_yes.isSelected() == true) {
                        transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(observable.getCallingTransAcctNo()));
                    } else {
                        transactionUI.setCallingTransType("CASH");
                    }
                }
            }
        }
    }
    private void txtNoOfSharesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfSharesFocusLost
        // TODO add your handling code here:
        if (txtNoOfShares.getText().length() > 0 && txtShareFaceValue.getText().length() > 0) {
            double totalShareAmount = (CommonUtil.convertObjToDouble(txtNoOfShares.getText()).doubleValue()
                    * CommonUtil.convertObjToDouble(txtShareFaceValue.getText()).doubleValue());
            txtShareValue.setText(String.valueOf(totalShareAmount));
            txtShareValue.setEnabled(false);
            txtInvestmentAmount.setText(String.valueOf(totalShareAmount));
            txtInvestmentAmount.setEnabled(false);
            txtInvestmentAmountFocusLost(null);
        } else {
            txtShareValue.setText("");
            txtInvestmentAmount.setText("");
        }
    }//GEN-LAST:event_txtNoOfSharesFocusLost

    private void txtShareFaceValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareFaceValueFocusLost
        // TODO add your handling code here:
        if (txtNoOfShares.getText().length() > 0 && txtShareFaceValue.getText().length() > 0) {
            double totalShareAmount = (CommonUtil.convertObjToDouble(txtNoOfShares.getText()).doubleValue()
                    * CommonUtil.convertObjToDouble(txtShareFaceValue.getText()).doubleValue());
            txtShareValue.setText(String.valueOf(totalShareAmount));
            txtShareValue.setEnabled(false);
            txtInvestmentAmount.setText(String.valueOf(totalShareAmount));
            txtInvestmentAmount.setEnabled(false);
            txtInvestmentAmountFocusLost(null);
        } else {
            txtShareValue.setText("");
            txtInvestmentAmount.setText("");
        }
    }//GEN-LAST:event_txtShareFaceValueFocusLost

    private void rdoWithInterest_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWithInterest_noActionPerformed
        // TODO add your handling code here:
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_CCD")) {
            txtFDPricipalAmt.setEnabled(true);
            ClientUtil.enableDisable(panFDInvestmentPeriod, true);
            cboFDInterestPaymentFrequency.setEnabled(true);
            txtFDRateOfInt.setEnabled(true);
            txtFDInterestReceivable.setEnabled(true);
            tdtFDEffectiveDt.setDateValue(tdtFDMaturityDt.getDateValue());
            tdtFDEffectiveDt.setEnabled(true);
            txtFDPricipalAmt.setText(observable.getTxtFDPricipalAmt());
        }
    }//GEN-LAST:event_rdoWithInterest_noActionPerformed

    private void rdoWithInterest_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWithInterest_yesActionPerformed
        // TODO add your handling code here:
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_CCD")) {
            txtFDPricipalAmt.setEnabled(true);
            ClientUtil.enableDisable(panFDInvestmentPeriod, true);
            cboFDInterestPaymentFrequency.setEnabled(true);
            txtFDRateOfInt.setEnabled(true);
            txtFDInterestReceivable.setEnabled(true);
            tdtFDEffectiveDt.setDateValue(tdtFDMaturityDt.getDateValue());
            tdtFDEffectiveDt.setEnabled(true);
            txtFDPricipalAmt.setText(txtFDMaturityAmt.getText());
        }
    }//GEN-LAST:event_rdoWithInterest_yesActionPerformed

    private void cboFDInterestPaymentFrequencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboFDInterestPaymentFrequencyFocusLost
        // TODO add your handling code here:
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcFixedInterest();
            cboFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_RD")) {
            calcRecurringInterest();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcCummulativeInterest();
            txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
        }

    }//GEN-LAST:event_cboFDInterestPaymentFrequencyFocusLost

    private void cboFDInterestPaymentActionPerform() {
        if ((!cboFDInterestPaymentFrequency.getSelectedItem().equals("")) && (cboFDInterestPaymentFrequency.getSelectedItem() != null)) {
            if ((txtFDInterestReceivable.getText() != null) && (!txtFDInterestReceivable.getText().equals(""))) {
                double perIntAmt = 0;
                setYr();
                double totalIntAmtPerYear = (CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()) / yr);
                if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Half Yearly")) {
                    perIntAmt = totalIntAmtPerYear / perHalfYear;
                } else if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Monthly")) {
                    perIntAmt = totalIntAmtPerYear / perMonth;
                    //--- Calculation for Period as No.Of Days
                    int YrsToDay = 0;
                    int MonToDay = 0;
                    int daysEntered = 0;
                    int periodInDays = 0;
                    if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
                        YrsToDay = (Integer.parseInt(txtFDInvestmentPeriod_Years.getText())) * 365;
                    }
                    if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
                        MonToDay = ((Integer.parseInt(txtFDInvestmentPeriod_Months.getText())) * 30);
                    }
                    if ((txtFDInvestmentPeriod_Days.getText() != null) && (!txtFDInvestmentPeriod_Days.getText().equals(""))) {
                        daysEntered = Integer.parseInt(txtFDInvestmentPeriod_Days.getText());
                    }
                    periodInDays = (YrsToDay + MonToDay + daysEntered);

                    System.out.println("perIntAmt" + perIntAmt);
                } else if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Yearly")) {
                    perIntAmt = totalIntAmtPerYear;
                } else if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Quaterly")) {
                    perIntAmt = totalIntAmtPerYear / perQuarterYear;
                } else if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Date of Maturity")) {
                    perIntAmt = 0;
                }
                System.out.println("perIntAmt" + perIntAmt);
                try {
                    perIntAmt = (double) getNearest((long) (perIntAmt * 100), 100) / 100;
                    System.out.println("#### cboInterestPaymentFreqActionPerformed " + perIntAmt);
                } catch (Exception e) {
                    System.out.println(e);
                }
                observable.setTxtFDPeriodicIntrest(String.valueOf(perIntAmt));
                txtFDPeriodicIntrest.setText(observable.getTxtFDPeriodicIntrest());
            }
        }
    }

    private void setYr() {
        if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
            yr = CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Years.getText());
        }
        if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
            yr = yr + (CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Months.getText()) / totalMonths);
        }
        if ((txtFDInvestmentPeriod_Days.getText() != null) && (!txtFDInvestmentPeriod_Days.getText().equals(""))) {
            yr = yr + (CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Days.getText()) / totalDays);
        }
    }
    private void txtFDRateOfIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDRateOfIntFocusLost
        // TODO add your handling code here:
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcFixedInterest();
            cboFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_RD")) {
            calcRecurringInterest();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcCummulativeInterest();
            txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
        }
    }//GEN-LAST:event_txtFDRateOfIntFocusLost

    private void txtFromNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromNOFocusLost
        // TODO add your handling code here:
        if (txtFromNO.getText().length() > 0) {
            if (tblCheckBookTable.getRowCount() > 0) {
                int currentFromNo = CommonUtil.convertObjToInt(txtFromNO.getText());
                for (int i = 0; i < tblCheckBookTable.getRowCount(); i++) {
                    int fromNo = 0;
                    int toNo = 0;
                    fromNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i, 2));
                    toNo = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i, 3));
                    if (fromNo <= currentFromNo && currentFromNo <= toNo) {
                        ClientUtil.showMessageWindow("Cheque No Already Issued !!!");
                        txtFromNO.setText("");
                    }
                }
            }
            txtToNO.setText("");
            txtNoOfCheques.setText("");
        }
    }//GEN-LAST:event_txtFromNOFocusLost

    private void rdoRenewal_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewal_noActionPerformed
        // TODO add your handling code here:
        if (rdoRenewal_no.isSelected() == true) {
            tabInvestment.setSelectedIndex(0);
            panWithInterestYesNo.setVisible(false);
            panInsideRenewalDetails.setVisible(false);
            tabInvestment.remove(panInsideRenewalDetails);
            btnSave.setEnabled(false);
            ClientUtil.enableDisable(panInsideDepositDetails, false);
            ClientUtil.enableDisable(panInsideDepositDetails2, false);
        }
    }//GEN-LAST:event_rdoRenewal_noActionPerformed

    private void rdoRenewal_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewal_yesActionPerformed
        // TODO add your handling code here:
        tabRenewalTransDetails.setEnabledAt(0, false);
        tabRenewalTransDetails.setSelectedIndex(1);
        long daydiff = 0;
        java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
        daydiff = DateUtil.dateDiff(matDate, curDate);
        System.out.println("#############  daydiff " + daydiff);
        rdgRenewalWithNo.clearSelection();
        rdgRenewalWithInt.clearSelection();
        
        if (daydiff >= 0) {
                if (rdoRenewal_yes.isSelected() == true) {
                transactionUI.addToScreen(panRenewalTransaction);
                rdoRenewal_yes.setEnabled(false);
                rdoRenewal_no.setEnabled(false);
                panInsideRenewalDetails.setVisible(true);
                tabInvestment.add(panInsideRenewalDetails, "Renewal Details", 1);
                tabInvestment.setSelectedIndex(1);
                btnSave.setEnabled(true);
                ClientUtil.enableDisable(panRenewalInsideDepositDetails, false);
                ClientUtil.enableDisable(panInsideRenewalDepositDetails, false);
                ClientUtil.enableDisable(panRenewalNoDetails, true);
                ClientUtil.enableDisable(panInterestDetails, true);
                removeRadioButtons();
                addRadioButtons();
                if((CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceivable())-
                    CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceived()))>0){
                    ClientUtil.enableDisable(panInsideDepositDetails2, true);
                    txtFDInterestReceivable.setEditable(true);//06-02-2014
                    txtFDInterestReceivable.setEnabled(true);
                    txtFDInterestReceived.setEnabled(false);
                    tdtFDMaturityDt.setEnabled(false);
                    txtFDMaturityAmt.setEnabled(false);
                    txtFDPeriodicIntrest.setEnabled(false);
                    tdtFDIntReceivedTillDt.setEnabled(false);
                }else{
                    ClientUtil.enableDisable(panInsideDepositDetails2, false);
                }
            } else {
                panInsideRenewalDetails.setVisible(false);
                btnSave.setEnabled(false);
                ClientUtil.enableDisable(panInsideDepositDetails2, false);
            }
            txtInvestTDS.setEnabled(true);                
        } else {
            rdoRenewal_no.setSelected(true);
            ClientUtil.showMessageWindow("Can not Renew the Deposit, as the Maturity Date is : " + DateUtil.getStringDate(matDate));
        }
        boolean checkTab = tabRenewalTransDetails.isEnabledAt(2);
        if(checkTab){
        	tabRenewalTransDetails.remove(2);
        }
        tdtRenewalFDEffectiveDt.setEnabled(true); 
    }//GEN-LAST:event_rdoRenewal_yesActionPerformed

    private void removeRadioButtons() {
        rdgRenewalWithNo.remove(rdoRenewalWithSameNo);
        rdgRenewalWithNo.remove(rdoRenewalWithNewNo);
        rdgRenewalWithNo.remove(rdoRenewalWithDiffProdID);

        rdgRenewalWithInt.remove(rdoRenewalWithInterest);
        rdgRenewalWithInt.remove(rdoRenewalWithoutInterest);
        rdgRenewalWithInt.remove(rdoRenewalWithPartialInterest);        
        
        rdgRenewal.remove(rdoRenewal_yes);
        rdgRenewal.remove(rdoRenewal_no);
    }

    private void addRadioButtons() {// these r all radio button purpose adding...
        rdgRenewalWithNo = new CButtonGroup();
        rdgRenewalWithNo.add(rdoRenewalWithSameNo);
        rdgRenewalWithNo.add(rdoRenewalWithNewNo);
        rdgRenewalWithNo.add(rdoRenewalWithDiffProdID);

        rdgRenewalWithInt = new CButtonGroup();
        rdgRenewalWithInt.add(rdoRenewalWithInterest);
        rdgRenewalWithInt.add(rdoRenewalWithoutInterest);
        rdgRenewalWithInt.add(rdoRenewalWithPartialInterest);
        
        rdgRenewal = new CButtonGroup();
        rdgRenewal.add(rdoRenewal_yes);
        rdgRenewal.add(rdoRenewal_no);
    }

    private void setRenewalDetails() {
        cboRenewalInvestmentBehaves.setSelectedItem(cboInvestmentBehaves.getSelectedItem());
        cboRenewalFDInterestPaymentFrequency.setSelectedItem(cboFDInterestPaymentFrequency.getSelectedItem());
        txtRenewalInvestmentID.setText(txtInvestmentID.getText());
        txtRenewalInvestmentName.setText(txtInvestmentName.getText());
        txtRenewalFDAgencyName.setText(txtFDAgencyName.getText());
        txtRenewalFDAccountRefNO.setText(txtFDAccountRefNO.getText());
        txtRenewalFDInternalAccNo.setText(txtFDInternalAccNo.getText());
        txtRenewalFDInvestmentPeriod_Years.setText(txtFDInvestmentPeriod_Years.getText());
        txtRenewalFDInvestmentPeriod_Months.setText(txtFDInvestmentPeriod_Months.getText());
        txtRenewalFDInvestmentPeriod_Days.setText(txtFDInvestmentPeriod_Days.getText());
        txtRenewalFDRateOfInt.setText(txtFDRateOfInt.getText());
        txtRenewalFDPeriodicIntrest.setText(txtFDPeriodicIntrest.getText());
        tdtRenewalFDAccOpenDt.setDateValue(DateUtil.getStringDate(curDate));
        tdtRenewalFDEffectiveDt.setDateValue(tdtFDMaturityDt.getDateValue());
        tdtRenewalFDEffectiveDt.setEnabled(true);
        tdtTransactionDt.setEnabled(true);
    }
    private void tblCheckBookTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCheckBookTableMousePressed
        // TODO add your handling code here:
        updateChequeBookOBFields();
        updateMode = true;
        updateTab = tblCheckBookTable.getSelectedRow();
        observable.setNewData(false);
        String st = CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(), 0));
        observable.populateChequeDetails(st);
        populateChequeDetails();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableButton(false);
            ClientUtil.enableDisable(panChequeDetails, false);
        } else {
            enableDisableButton(true);
            btnCheckBookNew.setEnabled(false);
            ClientUtil.enableDisable(panChequeDetails, true);
            txtNoOfCheques.setEnabled(false);
            if (statusType.equals("AUTHORIZED")) {
                enableDisableButton(false);
                ClientUtil.enableDisable(panChequeDetails, false);
            }
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && tblCheckBookTable.getSelectedRowCount() > 0 && evt.getClickCount() == 2) {
            HashMap whereMap = new HashMap();
            whereMap.put("INVESTMENT_ID", txtSBInternalAccNo.getText());
            String slNo = CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(), 0));
            whereMap.put("SL_NO", slNo);
            TableDialogUI tableData = new TableDialogUI("getInvestmentIssuedChequeDetails", whereMap);
            tableData.setTitle("Cheque Book Details");
            tableData.show();
        }
    }//GEN-LAST:event_tblCheckBookTableMousePressed

    private void txtToNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToNOFocusLost
        // TODO add your handling code here:
        if (txtToNO.getText().length() > 0) {
            if (CommonUtil.convertObjToDouble(txtToNO.getText()).doubleValue() < CommonUtil.convertObjToDouble(txtFromNO.getText()).doubleValue()) {
                ClientUtil.showMessageWindow("To No Should be Greater Than From No");
                txtNoOfCheques.setText("");
                txtToNO.setText("");
            } else {
                int noOfCheques = 0;
                noOfCheques = CommonUtil.convertObjToInt(txtToNO.getText()) - CommonUtil.convertObjToInt(txtFromNO.getText());
                noOfCheques += 1;
                txtNoOfCheques.setText(String.valueOf(noOfCheques));
            }
        }
    }//GEN-LAST:event_txtToNOFocusLost

    private void btnCheckBookDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookDeleteActionPerformed
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(tblCheckBookTable.getSelectedRow(), 0));
        observable.deleteTableData(s, tblCheckBookTable.getSelectedRow());
        observable.resetChequeDetails();
        resetChequeDetails();
        ClientUtil.enableDisable(panChequeDetails, false);
        enableDisableButton(false);
        btnCheckBookNew.setEnabled(true);
    }//GEN-LAST:event_btnCheckBookDeleteActionPerformed

    private void btnCheckBookSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookSaveActionPerformed
        // TODO add your handling code here:
        try {
            if (txtFromNO.getText().length() <= 0) {
                ClientUtil.showMessageWindow("From No Should Not be Empty !!! ");
            } else if (txtToNO.getText().length() <= 0) {
                ClientUtil.showMessageWindow("To No Should Not be Empty !!! ");
            } else {
                updateChequeBookOBFields();
                observable.addToTable(updateTab, updateMode);
                tblCheckBookTable.setModel(observable.getTblCheckBookTable());
                observable.resetChequeDetails();
                resetChequeDetails();
                ClientUtil.enableDisable(panChequeDetails, false);
                enableDisableButton(false);
                btnCheckBookNew.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCheckBookSaveActionPerformed

    private void btnCheckBookNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckBookNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setNewData(true);
        enableDisableButton(false);
        btnCheckBookSave.setEnabled(true);
        ClientUtil.enableDisable(panChequeDetails, true);
        txtNoOfCheques.setEnabled(false);
    }//GEN-LAST:event_btnCheckBookNewActionPerformed

    private void resetRenewalInsideDepositDetails(){
        txtRenewalInvestmentID.setText("");
        txtRenewalInvestmentName.setText("");
        txtRenewalFDAgencyName.setText("");
        txtRenewalFDAccountRefNO.setText("");
        txtRenewalFDInternalAccNo.setText("");
        txtRenewalFDPricipalAmt.setText("");
        txtRenewalFDRateOfInt.setText("");
        txtRenewalFDInterestReceivable.setText("");
        txtInvestTDS.setText("");
        txtRenewalFDMaturityAmt.setText("");                
        txtRenewalFDPeriodicIntrest.setText("");
    }
    private void resetChequeDetails() {
        txtFromNO.setText("");
        txtNoOfCheques.setText("");
        txtToNO.setText("");
    }

    public void updateChequeBookOBFields() {
        observable.setTxtSBInternalAccNo(txtSBInternalAccNo.getText());
        observable.setTdtChequeIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curDate.clone())));
        observable.setTxtFromNO(txtFromNO.getText());
        observable.setTxtToNO(txtToNO.getText());
        observable.setTxtNoOfCheques(txtNoOfCheques.getText());
    }

    public void populateChequeDetails() {
        txtFromNO.setText(observable.getTxtFromNO());
        txtToNO.setText(observable.getTxtToNO());
        txtNoOfCheques.setText(observable.getTxtNoOfCheques());
    }

    private void enableDisableButton(boolean flag) {
        btnCheckBookNew.setEnabled(flag);
        btnCheckBookSave.setEnabled(flag);
        btnCheckBookDelete.setEnabled(flag);
    }
    private void txtFDInterestReceivableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDInterestReceivableFocusLost
        // TODO add your handling code here:
        System.out.println("FDInterestReceivable===="+txtFDInterestReceivable.getText());
        if (txtFDInterestReceivable.getText().length() > 0) {
            String behaves = observable.callForBehaves();
             System.out.println("behaves    ===="+behaves);
            if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD")) {
                //txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
            }
            if (behaves.equals("OTHER_BANK_RD")) {
                int monthCount = 0;
                if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
                    monthCount = CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Years.getText()) * 12;
                }
                if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
                    monthCount += CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Months.getText());
                }
                txtFDMaturityAmt.setText(String.valueOf((CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() * monthCount) + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
            }
            txtFDMaturityAmt.setEnabled(false);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){             
                observable.setTxtFDInterestReceivable(txtFDInterestReceivable.getText());
                if(rdoRenewalWithoutInterest.isSelected()){
                    rdoRenewalWithoutInterestActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_txtFDInterestReceivableFocusLost

    private void txtFDPricipalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDPricipalAmtFocusLost
        // TODO add your handling code here:
        if (txtFDPricipalAmt.getText().length() > 0) {
            String behaves = observable.callForBehaves();
            if (behaves.equals("OTHER_BANK_FD")) {
                txtFDMaturityAmt.setText(txtFDPricipalAmt.getText());
                calcFixedInterest();
                cboFDInterestPaymentActionPerform();
            }
            if (behaves.equals("OTHER_BANK_CCD")) {
                calcCummulativeInterest();
                txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
            }
            if (behaves.equals("OTHER_BANK_RD")) {
                int monthCount = 0;
                if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
                    monthCount = CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Years.getText()) * 12;
                }
                if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
                    monthCount += CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Months.getText());
                }
                txtFDMaturityAmt.setText(String.valueOf((CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() * monthCount) + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
                calcRecurringInterest();
            }
            txtFDMaturityAmt.setEnabled(false);
            //Set Trans_Amt
            txtInvestmentAmount.setText(txtFDPricipalAmt.getText());
            txtInvestmentAmount.setEnabled(false);
            txtInvestmentAmountFocusLost(null);
        }
    }//GEN-LAST:event_txtFDPricipalAmtFocusLost
    private void calcFixedInterest() {
        if (txtFDPricipalAmt.getText().length() > 0 && txtFDRateOfInt.getText().length() > 0 && cboFDInterestPaymentFrequency.getSelectedIndex() > 0 && tdtFDMaturityDt.getDateValue().length() > 0) {
            double intAmt = 0.0;
            double amt = 0.0;
            double period = 0.0;
            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
            java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDEffectiveDt.getDateValue()));
            period = DateUtil.dateDiff(effectiveDate, matDate);
            System.out.println("#############  period " + period);
            /*if (cboFDInterestPaymentFrequency.getSelectedItem().equals("Monthly") && period > 29) { // Commented by nithya on 25-11-2020 for KD-2460
                System.out.println("#############  Monthly ");
                double roi = 0.0;
                double amount = CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue();
                roi = CommonUtil.convertObjToDouble(txtFDRateOfInt.getText()).doubleValue();
                period = period / 30;
                intAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                System.out.println("intAmt: " + intAmt);
                double calcAmt = amount / 100;
                intAmt = intAmt * calcAmt;
                intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
                intAmt = intAmt * period;
            } else {
                System.out.println("#############  Not_Monthly "); */
                double principal = 0.0;
                double rateOfInterest = 0.0;
                double years = CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Years.getText()).doubleValue();
                double months = CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Months.getText()).doubleValue();
                principal = CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue();
                rateOfInterest = CommonUtil.convertObjToDouble(txtFDRateOfInt.getText()).doubleValue();
                years = years * 12;
                double total = years + months;
                double greateramount = principal + (principal * rateOfInterest * total / 1200);
                double interestgreater = greateramount - principal;
                double days = CommonUtil.convertObjToDouble(txtFDInvestmentPeriod_Days.getText()).doubleValue();
                double lessamount = principal + (principal * rateOfInterest * days / 36500);
                double interestless = lessamount - principal;
                intAmt = interestgreater + interestless;
           // }
            intAmt = (double) getNearest((long) (intAmt * 100), 100) / 100;
            System.out.println("intAmt: " + intAmt);
            txtFDInterestReceivable.setText(String.valueOf(intAmt));
        }
    }

    private void calcRecurringInterest() {
        if (txtFDPricipalAmt.getText().length() > 0 && txtFDRateOfInt.getText().length() > 0 && tdtFDMaturityDt.getDateValue().length() > 0) {
            double period = 0.0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
            java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDEffectiveDt.getDateValue()));
            period = DateUtil.dateDiff(effectiveDate, matDate);
            principal = CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue();
            double rateOfInterest = CommonUtil.convertObjToDouble(txtFDRateOfInt.getText()).doubleValue() / 100;
            rateOfInterest = rateOfInterest / 12;
            double ibaInterest = (rateOfInterest * 100 * 12);
            double installment = 0.0;
            installment = period / 30;
            period = installment / 3;
            amount = principal * (Math.pow((1 + ibaInterest / 400), period) - 1) / (1 - Math.pow((1 + ibaInterest / 400), -1 / 3.0));
            interest = amount - (principal * installment);
            interest = (double) getNearest((long) (interest * 100), 100) / 100;
            System.out.println("intAmt: " + interest);
            txtFDInterestReceivable.setText(String.valueOf(interest));
        }
    }

    private void calcCummulativeInterest() {
        if (txtFDPricipalAmt.getText().length() > 0 && txtFDRateOfInt.getText().length() > 0 && tdtFDMaturityDt.getDateValue().length() > 0) {
            double period = 0.0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            java.util.Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDMaturityDt.getDateValue()));
            java.util.Date effectiveDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFDEffectiveDt.getDateValue()));
            period = DateUtil.dateDiff(effectiveDate, matDate);
            principal = CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue();
            period = period / 30;
            period = (double) roundOffLower((long) (period * 100), 100) / 100;
            double rateOfInterest = CommonUtil.convertObjToDouble(txtFDRateOfInt.getText()).doubleValue() / 100;
            amount = principal * (Math.pow((1 + rateOfInterest / 4.0), period / 12 * 4.0));
            interest = amount - principal;
            interest = (double) getNearest((long) (interest * 100), 100) / 100;
            System.out.println("intAmt: " + interest);
            txtFDInterestReceivable.setText(String.valueOf(interest));
        }
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }
    private void txtFDInvestmentPeriod_DaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDInvestmentPeriod_DaysFocusLost
        // TODO add your handling code here:
        calculateMatDate();
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcFixedInterest();
            cboFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_RD")) {
            calcRecurringInterest();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcCummulativeInterest();
            txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));

        }
    }//GEN-LAST:event_txtFDInvestmentPeriod_DaysFocusLost

    private void txtFDInvestmentPeriod_MonthsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDInvestmentPeriod_MonthsFocusLost
        // TODO add your handling code here:
        calculateMatDate();

        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcFixedInterest();
            cboFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_RD")) {
            calcRecurringInterest();
            setMaturityAmount();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcCummulativeInterest();
            txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
        }
    }//GEN-LAST:event_txtFDInvestmentPeriod_MonthsFocusLost

    private void txtFDInvestmentPeriod_YearsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFDInvestmentPeriod_YearsFocusLost
        // TODO add your handling code here:
        calculateMatDate();
        String behaves = observable.callForBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcFixedInterest();
            cboFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_RD")) {
            calcRecurringInterest();
            setMaturityAmount();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcCummulativeInterest();
            txtFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
        }
    }//GEN-LAST:event_txtFDInvestmentPeriod_YearsFocusLost
    private void setMaturityAmount() {
        String behaves = observable.callForBehaves();
        if (txtFDPricipalAmt.getText().length() > 0 || txtFDInterestReceivable.getText().length() > 0) {
            if (behaves.equals("OTHER_BANK_RD")) {
                int monthCount = 0;
                if ((txtFDInvestmentPeriod_Years.getText() != null) && (!txtFDInvestmentPeriod_Years.getText().equals(""))) {
                    monthCount = CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Years.getText()) * 12;
                }
                if ((txtFDInvestmentPeriod_Months.getText() != null) && (!txtFDInvestmentPeriod_Months.getText().equals(""))) {
                    monthCount += CommonUtil.convertObjToInt(txtFDInvestmentPeriod_Months.getText());
                }
                txtFDMaturityAmt.setText(String.valueOf((CommonUtil.convertObjToDouble(txtFDPricipalAmt.getText()).doubleValue() * monthCount) + CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()));
            }
            txtFDMaturityAmt.setEnabled(false);
        }
    }
    private void rdoCheckBookAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCheckBookAllowed_noActionPerformed
        // TODO add your handling code here:
        if (rdoCheckBookAllowed_no.isSelected() == true) {
            if (tblCheckBookTable.getRowCount() > 0) {
                ClientUtil.showMessageWindow("First Delete Cheque Book Details");
                rdoCheckBookAllowed_yes.setSelected(true);
            } else {
                panCheckBookTable.setVisible(false);
                btnCheckBookNew.setEnabled(false);
                ClientUtil.enableDisable(panChequeDetails, false);
            }
        }
    }//GEN-LAST:event_rdoCheckBookAllowed_noActionPerformed

    private void rdoCheckBookAllowed_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCheckBookAllowed_yesActionPerformed
        // TODO add your handling code here:
        if (rdoCheckBookAllowed_yes.isSelected() == true) {
            panCheckBookTable.setVisible(true);
            btnCheckBookNew.setEnabled(true);
        }
    }//GEN-LAST:event_rdoCheckBookAllowed_yesActionPerformed

    private void rdoOtherSecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOtherSecurityActionPerformed
        // TODO add your handling code here:
        if (rdoOtherSecurity.isSelected() == true) {
            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(true);
            txtOthersName.setVisible(true);
            cboState.setSelectedItem("");
        } else {

            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");

        }
    }//GEN-LAST:event_rdoOtherSecurityActionPerformed

    private void rdoStateSecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStateSecurityActionPerformed
        // TODO add your handling code here:
        if (rdoStateSecurity.isSelected() == true) {
            cboState.setVisible(true);
            lblState.setVisible(true);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            txtOthersName.setText("");
        } else {

            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");

        }
    }//GEN-LAST:event_rdoStateSecurityActionPerformed

    private void rdoCentralSecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCentralSecurityActionPerformed
        // TODO add your handling code here:
        if (rdoCentralSecurity.isSelected() == true) {
            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");
        }
    }//GEN-LAST:event_rdoCentralSecurityActionPerformed

    private void rdoOtherSecurityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoOtherSecurityFocusLost
        // TODO add your handling code here:
        if (rdoOtherSecurity.isSelected() == true) {
            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(true);
            txtOthersName.setVisible(true);
            cboState.setSelectedItem("");

        } else {

            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");

        }
    }//GEN-LAST:event_rdoOtherSecurityFocusLost

    private void rdoStateSecurityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoStateSecurityFocusLost
        // TODO add your handling code here:
        if (rdoStateSecurity.isSelected() == true) {
            cboState.setVisible(true);
            lblState.setVisible(true);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            txtOthersName.setText("");
        } else {

            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");

        }
    }//GEN-LAST:event_rdoStateSecurityFocusLost

    private void rdoCentralSecurityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoCentralSecurityFocusLost
        // TODO add your handling code here:
        if (rdoCentralSecurity.isSelected() == true) {
            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
            cboState.setSelectedItem("");
        }
    }//GEN-LAST:event_rdoCentralSecurityFocusLost

    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // TODO add your handling code here:
        viewType = "BANK_CODE";
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Outward.getSelectBankCode");
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnBankCodeActionPerformed

    private void txtBankCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankCodeIDActionPerformed
        // TODO add your handling code here:
        String txtBankCodes = txtBankCodeID.getText();
        observable.setTxtBankCode(txtBankCodes);
        //         viewType="BANK_CODE";
        if (txtBankCodes != null && txtBankCodes.length() > 0) {
            HashMap Map = new HashMap();
            Map.put("BANK_CODE", txtBankCodes);
            List lst = ClientUtil.executeQuery("Outward.getSelectBankCode", Map);
            System.out.println("lst####" + lst);
            if (lst == null || lst.size() == 0) {
                ClientUtil.displayAlert("Invalide BankCode");
                txtBankCodeID.setText("");
            } else {
                lblBankNameIDValue.setText(CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BANK_NAME")));
            }
        }
    }//GEN-LAST:event_txtBankCodeIDActionPerformed

    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // TODO add your handling code here:
        viewType = "BANK_BRANCH";
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        hash.put("BANK_CODE", observable.getTxtBankCode());
        viewMap.put(CommonConstants.MAP_NAME, "Outward.getSelectBankBranch");
        viewMap.put(CommonConstants.MAP_WHERE, hash);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnBranchCodeActionPerformed

    private void txtBranchCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBranchCodeIDActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        String otherBranchCode = txtBranchCodeID.getText();
        observable.setTxtBranchCode(otherBranchCode);
        if (otherBranchCode != null && otherBranchCode.length() > 0) {
            HashMap hash = new HashMap();
            hash.put("BANK_CODE", observable.getTxtBankCode());
            hash.put("OTHER_BRANCH_CODE", otherBranchCode);
            List lst = ClientUtil.executeQuery("Outward.getSelectBankBranch", hash);
            if (lst == null || lst.size() == 0) {
                ClientUtil.displayAlert("Enter Valide Branch Code");
                txtBranchCodeID.setText("");
            } else {
                lblBranchNameIDValue.setText(CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BRANCH_NAME")));
            }

        }
    }//GEN-LAST:event_txtBranchCodeIDActionPerformed

    private void btnInvestmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDActionPerformed
        // TODO add your handling code here:
        if (((ComboBoxModel) cboInvestmentBehaves.getModel()).getKeyForSelected().toString().length() > 0) {
            callView("InvestmentProduct");
        } else {
            ClientUtil.displayAlert("Select Investment Type !!!");
            cboInvestmentBehaves.setEnabled(true);
        }
    }//GEN-LAST:event_btnInvestmentIDActionPerformed

    private void btnInvestmentIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnInvestmentIDFocusLost
        // TODO add your handling code here:
//        cboInvestmentBehaves.setEditable(false);
        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_btnInvestmentIDFocusLost
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
    private void chkCallOptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCallOptionFocusLost
        //        // TODO add your handling code here:
        //           if(chkSetupOption.isSelected()==true){
        //            txtSetUpOptionNoOfYears.setVisible(true);
        //            lblCallOptionNoOfYears.setVisible(true);
        //            txtSetUpOptionNoOfYears.setEnabled(true);
        //            txtSetUpOptionNoOfYears.setEditable(true);
        //        }
        //        else{
        //            txtSetUpOptionNoOfYears.setVisible(false);
        //            lblCallOptionNoOfYears.setVisible(false);
        //            txtSetUpOptionNoOfYears.setEnabled(false);
        //            txtSetUpOptionNoOfYears.setEditable(false);
        //            txtSetUpOptionNoOfYears.setText("");
        //        }
    }//GEN-LAST:event_chkCallOptionFocusLost

    private void chkSetupOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSetupOptionActionPerformed
        // TODO add your handling code here:
        if (chkSetupOption.isSelected() == true) {
            txtSetUpOptionNoOfYears.setVisible(true);
            txtSetUpOptionNoOfYears.setEnabled(true);
            txtSetUpOptionNoOfYears.setEditable(true);
            lblSetUpOptionNoOfYears.setVisible(true);
        } else {
            txtSetUpOptionNoOfYears.setVisible(false);
            lblSetUpOptionNoOfYears.setVisible(false);
            txtSetUpOptionNoOfYears.setEnabled(false);
            txtSetUpOptionNoOfYears.setEditable(false);
            txtSetUpOptionNoOfYears.setText("");

        }

    }//GEN-LAST:event_chkSetupOptionActionPerformed

    private void chkPutOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPutOptionActionPerformed
        // TODO add your handling code here:
        if (chkPutOption.isSelected() == true) {
            txtPutOptionNoOfYears.setVisible(true);
            lblPutOptionNoOfYears.setVisible(true);
            txtPutOptionNoOfYears.setEnabled(true);
            txtPutOptionNoOfYears.setEditable(true);


        } else {
            txtPutOptionNoOfYears.setVisible(false);
            lblPutOptionNoOfYears.setVisible(false);
            txtPutOptionNoOfYears.setEnabled(false);
            txtPutOptionNoOfYears.setEditable(false);
            txtPutOptionNoOfYears.setText("");
        }
    }//GEN-LAST:event_chkPutOptionActionPerformed

    private void chkCallOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCallOptionActionPerformed
        // TODO add your handling code here:
        if (chkCallOption.isSelected() == true) {
            txtCallOptionNoOfYears.setVisible(true);
            lblCallOptionNoOfYears.setVisible(true);
            txtCallOptionNoOfYears.setEnabled(true);
            txtCallOptionNoOfYears.setEditable(true);
        } else {
            txtCallOptionNoOfYears.setVisible(false);
            lblCallOptionNoOfYears.setVisible(false);
            txtCallOptionNoOfYears.setEnabled(false);
            txtCallOptionNoOfYears.setEditable(false);
            txtCallOptionNoOfYears.setText("");
        }

    }//GEN-LAST:event_chkCallOptionActionPerformed

    private void cboInvestmentBehavesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesFocusLost
        // TODO add your handling code here:
//        System.out.println("Inside cbo Focus Lost");
//        String behaves=observable.callForBehaves();
//        if(behaves.equals("") && behaves.length()<=0){
//            ClientUtil.displayAlert("Please Select The Investment Type");
//            cboInvestmentBehaves.requestFocus();
//        }
    }//GEN-LAST:event_cboInvestmentBehavesFocusLost

    private void rdoNonSlrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonSlrActionPerformed
        // TODO add your handling code here:
        if (rdoNonSlr.isSelected() == true) {
            rdoSlr.setSelected(false);
        }
    }//GEN-LAST:event_rdoNonSlrActionPerformed

    private void rdoSlrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSlrActionPerformed
        // TODO add your handling code here:
        if (rdoSlr.isSelected() == true) {
            rdoNonSlr.setSelected(false);
        }
    }//GEN-LAST:event_rdoSlrActionPerformed

    private void rdoHfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHfsActionPerformed
        // TODO add your handling code here:
        if (rdoHfs.isSelected() == true) {
            rdoHft.setSelected(false);
            rdoHfm.setSelected(false);
            rdoSlr.setSelected(false);
            rdoNonSlr.setSelected(true);
            rdoSlr.setEnabled(false);
            rdoNonSlr.setEnabled(false);
            //            ClientUtil.enableDisable(panSlrNonSlr,true);
        } else {
//            rdoNonSlr.setSelected(false);
        }
    }//GEN-LAST:event_rdoHfsActionPerformed

    private void rdoHftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHftActionPerformed
        // TODO add your handling code here:
        if (rdoHft.isSelected() == true) {
            rdoHfs.setSelected(false);
            rdoHfm.setSelected(false);
            rdoSlr.setSelected(false);
            rdoNonSlr.setSelected(true);
            rdoSlr.setEnabled(false);
            rdoNonSlr.setEnabled(false);
            //            ClientUtil.enableDisable(panSlrNonSlr,true);
        }
    }//GEN-LAST:event_rdoHftActionPerformed

    private void rdoHfmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHfmActionPerformed
        // TODO add your handling code here:
        if (rdoHfm.isSelected() == true) {
            rdoHfs.setSelected(false);
            rdoHft.setSelected(false);
            rdoNonSlr.setSelected(false);
            rdoSlr.setSelected(true);
            rdoSlr.setEnabled(false);
            rdoNonSlr.setEnabled(false);
            //            ClientUtil.enableDisable(panSlrNonSlr,false);


        } else {
//            rdoSlr.setSelected(false);
        }
    }//GEN-LAST:event_rdoHfmActionPerformed

    private void txtInvestmentNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentNameFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            boolean exists = false;
            exists = observable.isInvsetMentMasterTypeExists(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
            if (exists == true) {
                displayAlert("Already This Prod Id Exists");
                txtInvestmentName.setText("");
                txtInvestmentName.requestFocus();
                btnInvestmentID.setEnabled(true);
            } else {
                btnInvestmentID.setEnabled(false);
            }
        }
    }//GEN-LAST:event_txtInvestmentNameFocusLost

    private void cboInvestmentBehavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesActionPerformed
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        String behaves = observable.callForBehaves();
        lblInvestmentAmount.setText("Deposit Amount");
//        setFieldNamesBasedOnBehaves(behaves);
        if (behaves.equals("BONDS")) {
//            txtMaturityAmount.setVisible(false);
//            lblMaturityAmount.setVisible(false);
//            cboInterestPaymentFrequency.setSelectedItem("");
//            cboInterestPaymentFrequency.setEnabled(true);
//            tdtLastIntAppldt.setVisible(true);
//            lblLstIntApplDt.setVisible(true);
            panInvestmentDetails.setVisible(true);
        }
        //Added By Suresh
        if (behaves.equals("OTHER_BANK_SB") || behaves.equals("OTHER_BANK_CA") || behaves.equals("OTHER_BANK_SPD")) {
//            panDepositDetails.setVisible(false);
            panInsideDepositDetails.setVisible(false);
            panInsideDepositDetails1.setVisible(false);
            panReserveFund.setVisible(false);
            panShareDetails.setVisible(false);
            panRenewalDetails.setVisible(false);
            lblInvestmentName.setText("A/c Description");
            panInsideSBDetails.setVisible(true);
        }
        if (behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_RD") || behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_SPFD")
                || behaves.equals("OTHER_BANK_SSD")) {
            if (rdoRenewalWithPartialInterest.isSelected() == true) {
                double totalIntRec = (CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText()).doubleValue()
                        - CommonUtil.convertObjToDouble(txtFDInterestReceived.getText()).doubleValue());
                lblRenewalTotalInrestAmt.setText(String.valueOf(totalIntRec));
                panWithdrawalIntAmount.setVisible(true);
            } else {
                panWithdrawalIntAmount.setVisible(false);
            }
            panInsideDepositDetails.setVisible(true);
            panInsideDepositDetails1.setVisible(true);
            panInsideSBDetails.setVisible(false);
            panShareDetails.setVisible(false);
            panReserveFund.setVisible(false);
            lblInvestmentName.setText("A/c Description");
            panCheckBookTable.setVisible(false);
            if (behaves.equals("OTHER_BANK_FD")) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    cboFDInterestPaymentFrequency.setSelectedItem("");
                    panRenewalDetails.setVisible(false);
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    cboFDInterestPaymentFrequency.setEnabled(true);
                }
            }
            if (behaves.equals("OTHER_BANK_CCD")) {
                cboFDInterestPaymentFrequency.setSelectedItem("Date of Maturity");
                cboFDInterestPaymentFrequency.setEnabled(false);
                panWithPrincipalYesNo.setVisible(true);
                lblWithPrincipal.setVisible(true);
            } else {
                panWithPrincipalYesNo.setVisible(false);
                lblWithPrincipal.setVisible(false);
            }
            if (behaves.equals("OTHER_BANK_RD")) {
                lblFDPricipalAmt.setText("Installment Amount");
                txtFDInvestmentPeriod_Days.setVisible(false);
                lblInvestmentPeriod_Days1.setVisible(false);
                panRenewalDetails.setVisible(false);
            } else {
                lblFDPricipalAmt.setText("Principal Amount");
                txtFDInvestmentPeriod_Days.setVisible(true);
                lblInvestmentPeriod_Days1.setVisible(true);
            }
            if (behaves.equals("OTHER_BANK_FD")) {
                ClientUtil.enableDisable(panWithInterestYesNo, false);
                txtFDPeriodicIntrest.setVisible(true);
                lblFDPeriodicIntrest.setVisible(true);
            } else {
                txtFDPeriodicIntrest.setText("");
                txtFDPeriodicIntrest.setVisible(false);
                lblFDPeriodicIntrest.setVisible(false);
            }
        }
        if (behaves.equals("SHARES_DCB") || behaves.equals("SHARE_OTHER_INSTITUTIONS")) {
            panShareDetails.setVisible(true);
            panInsideDepositDetails.setVisible(false);
            panInsideDepositDetails1.setVisible(false);
            panInsideSBDetails.setVisible(false);
            panReserveFund.setVisible(false);
            lblInvestmentName.setText("Share Description");
            panCheckBookTable.setVisible(false);
            panRenewalDetails.setVisible(false);
            lblInvestmentAmount.setText("Share Amount");
        }
        if (behaves.equals("RESERVE_FUND_DCB")) {
            lblInvestmentName.setText("RF Description");
            panInsideDepositDetails.setVisible(false);
            panInsideDepositDetails1.setVisible(false);
            panInsideSBDetails.setVisible(false);
            panShareDetails.setVisible(false);
            panCheckBookTable.setVisible(false);
            panReserveFund.setVisible(true);
            panRenewalDetails.setVisible(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (behaves.equals("OTHER_BANK_SB") || behaves.equals("OTHER_BANK_CA") || behaves.equals("OTHER_BANK_SPD")) {
                txtInvestmentAmount.setEnabled(true);
            } else {
                txtInvestmentAmount.setEnabled(false);
            }
        }
    }//GEN-LAST:event_cboInvestmentBehavesActionPerformed

    private void setFieldNamesBasedOnBehaves(String behaves) {
        if (behaves.equals("OTHER_BANK_CCD") || behaves.equals("OTHER_BANK_FD") || behaves.equals("OTHER_BANK_SSD")) {
//            lblIssueDt.setText("Deposit Date");
//            lblFaceValue.setText("Deposit Amount");
//            lblInvestmentPeriod.setText("Deposit Period");
            panBankCodeAndBranchCode.setVisible(true);
            panSecurityType.setVisible(false);
//            lblInitiatedDt.setVisible(false);
//            tdtInitiated.setVisible(false);
            panType.setVisible(false);
            lblType.setVisible(false);
            cboState.setSelectedItem("");
            txtOthersName.setText("");
            rdoCentralSecurity.setSelected(false);
            rdoOtherSecurity.setSelected(false);
            rdoStateSecurity.setSelected(false);
        }

        if (behaves.equals("BONDS")) {
//            lblIssueDt.setText("Issue Date");
//            lblFaceValue.setText("Face Value");
//            lblInvestmentPeriod.setText("Investment Period");
//            lblInitiatedDt.setVisible(true);
//            tdtInitiated.setVisible(true);
            panType.setVisible(true);
            lblType.setVisible(true);
            panBankCodeAndBranchCode.setVisible(false);
            panSecurityType.setVisible(true);
            cboState.setVisible(false);
            lblState.setVisible(false);
            lblOthersName.setVisible(false);
            txtOthersName.setVisible(false);
        }
    }
    private void cboInvestmentBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesItemStateChanged
        //        // TODO add your handling code here:
        //        String behaves=observable.callForBehaves();
        //        if(behaves.equals("") && behaves.length()<=0 && observable.getActionType()!=ClientConstants.ACTIONTYPE_CANCEL){
        //            ClientUtil.displayAlert("Please Select The Investment Type");
        //            cboInvestmentBehaves.requestFocus();
        //        }
        
    }//GEN-LAST:event_cboInvestmentBehavesItemStateChanged

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
        try {
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
            ClientUtil.enableDisable(panInvestment, false);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        try {
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
            authorizeStatus(CommonConstants.STATUS_REJECTED);
            btnInvestmentID.setEnabled(false);
            ClientUtil.enableDisable(panInvestment, false);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:]
        try {
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            btnInvestmentID.setEnabled(false);
            ClientUtil.enableDisable(panInvestment, false);
        } catch (Exception ex) {
        }
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
        super.removeEditLock(((ComboBoxModel) cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        observable.resetTableValues();
        lblBranchNameIDValue.setText("");
        lblBankNameIDValue.setText("");
        panInvestmentDetails.setVisible(false);
        panSecurityType.setVisible(false);
        panBankCodeAndBranchCode.setVisible(false);
        ClientUtil.clearAll(this);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panInvestment, false);
        ClientUtil.enableDisable(panInsideRenewalDetails, false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnInvestmentID.setEnabled(false);
        btnCheckBookNew.setEnabled(false);
        btnCheckBookSave.setEnabled(false);
        btnCheckBookDelete.setEnabled(false);
        panCheckBookTable.setVisible(false);
        panRenewalDetails.setVisible(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnCancel.setEnabled(true);
        statusType = "";
        resetTransactionUI();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        btnInvestmentIDTrans1.setEnabled(false);
        btnInvestmentIDTransSBorCA.setEnabled(false);
        removeRadioButtons();
        addRadioButtons();
        lblRenewalTotalInrestAmt.setText("");
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
        ClientUtil.enableDisable(panInvestment, false);
        ClientUtil.enableDisable(panInvestmentDetails, false);
        btnInvestmentID.setEnabled(false);


    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
//        tdtMaturityDate.setEnabled(false);
        txtInvestmentName.setEditable(false);
        txtInvestmentName.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboInvestmentBehaves.setEnabled(false);
//        cboInvestmentBehaves.setEditable(false);
        btnInvestmentID.setEnabled(false);
        txtInvestmentID.setEnabled(false);
        txtShareValue.setEnabled(false);
        btnSave.setEnabled(true);
        ClientUtil.enableDisable(panInsideDepositDetails, false);
        //ClientUtil.enableDisable(panInsideDepositDetails2, false); commented by sreekrishnan
 //babu added 11-02-2014
         transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        resetTransactionUI();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        txtInvestTDS.setEnabled(false);//added by sreekrishnan
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here
        boolean isSameNo=false;
        if(rdoRenewalWithSameNo.isSelected()==true){
            isSameNo=true;
        }
        if(!isSameNo){
            HashMap refMap=new HashMap();
            if (rdoRenewal_yes.isSelected() == true) {
               refMap.put("REF_NO",txtRenewalFDAccountRefNO.getText());
            }
            else{
                refMap.put("REF_NO",txtFDAccountRefNO.getText());
            }
            List refNoLst =ClientUtil.executeQuery("getDuplicateRefNo", refMap);
            if(refNoLst.size()>0 && refNoLst!=null){
                HashMap refNoMap=(HashMap)refNoLst.get(0);
                if(refNoMap!=null && refNoMap.get("COUNT")!=null && refNoMap.containsKey("COUNT")){
                    int count = CommonUtil.convertObjToInt(refNoMap.get("COUNT"));
                    if(count>0){
                        ClientUtil.showMessageWindow("This Ref No already exists!!!");
                        if (rdoRenewal_yes.isSelected() == true) {
                            txtRenewalFDAccountRefNO.requestFocus(true);
                        }
                        else{
                            txtFDAccountRefNO.requestFocus(true);
                        }
                        return;
                    }
                }
            }
        }
        if(tdtTransactionDt.getDateValue()!=null && !tdtTransactionDt.getDateValue().equals("")){
            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtTransactionDt.getDateValue()), curDate) < 0){
                ClientUtil.showMessageWindow("Can't Allow Future Date As Transaction Date!!!"); 
                return;
            }
        }
        if(tabInvestment.getSelectedIndex() == 1){
        int rowCount = transactionUI.getTransactionOB().getTblTransDetails().getRowCount();
        if (rowCount > 0 && rdoRenewalWithoutInterest.isSelected() == true || rdoRenewalWithPartialInterest.isSelected() == true) {
            String cashOrTransfer = getCashOrTransfer();
            //Added by sreekrishnan for renewal with out intrerest
            if((CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceivable())-
                    CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceived()))>0){
                if (cashOrTransfer.equals("Transfer")) {
                    rdoRenewalSBorCA_yes.setSelected(true);
                    setModified(false);
                    savePerformed();
                } else if (cashOrTransfer.equals("Cash")) {
                    rdoRenewalSBorCA_no.setSelected(true);
                    setModified(false);
                    savePerformed();
                } else {
                    ClientUtil.showAlertWindow("Select Balance Interest PayMode Cash/Transfer");
                    tabRenewalTransDetails.setSelectedIndex(1);
                }
            }else{
                setModified(false);
                savePerformed();
            }

        } else if (rdoRenewalWithInterest.isSelected() == true) {
            setModified(false);
            savePerformed();
        } else {
            //Added by sreekrishnan for renewal with out intrerest
            if((CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceivable())-
                    CommonUtil.convertObjToDouble(observable.getTxtFDInterestReceived()))>0){
                ClientUtil.showAlertWindow("Select Balance Interest PayMode Cash/Transfer !");
                tabRenewalTransDetails.setSelectedIndex(1);
            }else{
                setModified(false);
                savePerformed();
            }
        }
        }else{
            setModified(false);
            savePerformed();
        }
//        if (rdoRenewalWithoutInterest.isSelected() == true || rdoRenewalWithPartialInterest.isSelected() == true) {
//            if ((rdoRenewalSBorCA_yes.isSelected() == true)) {
//                if(rowCount > 0){
//                setModified(false); 
//                savePerformed();
//                }else{
//                tabRenewalTransDetails.setSelectedIndex(1);
//                ClientUtil.showMessageWindow("Transaction Details Not Entered.... ");    
//                }
//            }else if(rdoRenewalSBorCA_no.isSelected() == true){
//                transactionUI.setCallingTransAcctNo("CASH");
//                setModified(false); 
//                savePerformed();  
//            }
//            else{
//                ClientUtil.showMessageWindow("Select Interest Transaction From SB/CA ");
//                tabRenewalTransDetails.setSelectedIndex(0);
//            }
//        }else if(rdoRenewalWithInterest.isSelected() == true){
//            setModified(false);
//           savePerformed();  
//        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        transactionUI.addToScreen(panTransaction);
        lblBranchNameIDValue.setText("");
        lblBankNameIDValue.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panInvestment, true);
        ClientUtil.enableDisable(panChequeDetails, false);
        ClientUtil.enableDisable(panInsideDepositDetails1, false);
//        tdtMaturityDate.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtInvestmentName.setEnabled(true);
        txtInvestmentName.setEditable(true);
        btnInvestmentID.setEnabled(true);
        txtInvestmentID.setEnabled(false);
        txtSBInternalAccNo.setEnabled(false);
        txtFDInternalAccNo.setEnabled(false);
        txtShareInternalAccNo.setEnabled(false);
        txtRFInternalAccNo.setEnabled(false);
        txtFDInterestReceived.setEnabled(false);
        tdtSBAccOpenDt.setDateValue(DateUtil.getStringDate(curDate));
        tdtFDAccOpenDt.setDateValue(DateUtil.getStringDate(curDate));
        tdtShareAccOpenDt.setDateValue(DateUtil.getStringDate(curDate));
        tdtRFAccOpenDt.setDateValue(DateUtil.getStringDate(curDate));
        tdtFDEffectiveDt.setDateValue(DateUtil.getStringDate(curDate));
        rdoCheckBookAllowed_no.setSelected(true);
        rdoWithPrincipal_yes.setSelected(true);
        panRenewalDetails.setVisible(true);
        rdoRenewal_no.setSelected(true);
        statusType = "";
        tdtFDIntReceivedTillDt.setEnabled(false);
        txtShareValue.setEnabled(false);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        resetTransactionUI();
         transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        rdoSBorCA_no.setSelected(true);
        rdoSBorCA_noActionPerformed(null);
        btnInvestmentIDTrans1.setEnabled(true);
        btnInvestmentIDTransSBorCA.setEnabled(true);
        tabClosingType.setVisible(true);
        tabInvestment.remove(panInsideRenewalDetails);
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtRenewalDepositIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRenewalDepositIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRenewalDepositIDActionPerformed

    private void txtRenewalDepTransAmtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalDepTransAmtValueFocusLost
        // TODO add your handling code here:
    boolean isAmountChages = false;
    double oldAmount = 0.0;
    String behaves = observable.callForRenewalBehaves();
    double intAmount = CommonUtil.convertObjToDouble(txtFDInterestReceivable.getText());
    if(rdoRenewalWithInterest.isSelected()){
    oldAmount = CommonUtil.convertObjToDouble(observable.getTxtFDPricipalAmt()) + CommonUtil.convertObjToDouble(intAmount);
        System.out.println("old a"+oldAmount);
    }else if (rdoRenewalWithoutInterest.isSelected()){
    oldAmount = CommonUtil.convertObjToDouble(observable.getTxtFDPricipalAmt());
    System.out.println("old a1"+oldAmount);
    }else{
    oldAmount = CommonUtil.convertObjToDouble(observable.getTxtFDPricipalAmt());
    System.out.println("old a2"+oldAmount);
    }
    System.out.println("FD pric Amout" + observable.getTxtFDPricipalAmt());
    System.out.println("Renewwal Amount" + observable.getTxtRenewalFDPricipalAmt());
    double enteredAmt = CommonUtil.convertObjToDouble(txtRenewalDepTransAmtValue.getText()).doubleValue();

    if (enteredAmt > 0) {
        if (behaves.equals("OTHER_BANK_FD")) {
            String str = String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(enteredAmt));
            // + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue())); + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText()).doubleValue()));
            System.out.println("str" + str);
            txtRenewalFDPricipalAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(enteredAmt + oldAmount)));
            txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()));
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
            calcRenewalTransAmount();
            updateDepositOBFields();
        }
    } else {
        txtRenewalFDPricipalAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(enteredAmt + oldAmount)));
        txtRenewalFDMaturityAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()).doubleValue()));
        ClientUtil.showAlertWindow("Amount Should not be empty...");
        return;
    }
    }//GEN-LAST:event_txtRenewalDepTransAmtValueFocusLost

    private void txtRenewalDepTransAmtValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRenewalDepTransAmtValueActionPerformed
        // TODO add your handling code here:
        txtRenewalDepTransAmtValue.setValidation(new CurrencyValidation(13, 2));
        lblRenewalDepositTransMode.setVisible(true);
        cboRenewalDepTransMode.setVisible(true);
        observable.setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(txtRenewalDepTransAmtValue.getText()));
        
    }//GEN-LAST:event_txtRenewalDepTransAmtValueActionPerformed

    private void cboRenewalDepTransModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalDepTransModeActionPerformed
        // TODO add your handling code here:
updateRenewalDepositOBFields();
updateDepositOBFields();
    }//GEN-LAST:event_cboRenewalDepTransModeActionPerformed

    private void cboRenewalDepTransProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalDepTransProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboRenewalDepTransProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmRenewalDepTransProdId(prodType);
            cboRenewalDepTransProdId.setModel(observable.getCbmRenewalDepTransProdId());
            if (!prodType.equals("GL") && cboRenewalDepTransProdId.getSelectedItem().equals("")) {
                cboRenewalDepTransProdId.setSelectedItem(observable.CallForRenewalDepProductID());
            }
        }
        
    }//GEN-LAST:event_cboRenewalDepTransProdTypeActionPerformed

    private void btnRenewalInvestmentsIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalInvestmentsIDActionPerformed
        // TODO add your handling code here:
                if (cboRenewalDepTransProdType.getSelectedIndex() > 0) {
            callView("RENEWAL_DEP_TRANS_ACC_NO");
        } else {
            ClientUtil.showAlertWindow("Product Type should not be empty...");
            return;
        }
    }//GEN-LAST:event_btnRenewalInvestmentsIDActionPerformed

    private void cboRenewalDepTransProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalDepTransProdIdActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboRenewalDepTransProdIdActionPerformed

    private void btnRenewalInvestmentsIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnRenewalInvestmentsIDFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_btnRenewalInvestmentsIDFocusLost

    private void txtRenewalDepositIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalDepositIDFocusLost
        // TODO add your handling code here:
                System.out.println("Focus Lost ");
        updateAddAmountToDepositOBFileds();
        System.out.println("1 Model "+observable.CallForRenewalProdModel());
        System.out.println("1 Type "+observable.CallForRenewalDepProductType());
        System.out.println("1 ID "+observable.getCbmRenewalDepTransProdId());
    }//GEN-LAST:event_txtRenewalDepositIDFocusLost

    private void cboRenewalDepTransModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboRenewalDepTransModeItemStateChanged
        // TODO add your handling code here:
        System.out.println("ddddddd"+observable.getRenewalInvestmentsDepositTO("UPDATE"));
        if (cboRenewalDepTransMode.getSelectedItem().equals("Cash") || cboRenewalDepTransMode.getSelectedItem().equals("")) {
            dropBtnVisible(false);
            observable.setCbmRenewalDepTransModel(CommonUtil.convertObjToStr(cboRenewalDepTransMode.getSelectedItem()));
            updateRenewalDepositOBFields();
        } else {
            btnVisible(true);
            
            observable.setCbmRenewalDepTransProdType(observable.CallForRenewalProdModel());
            
            observable.setCbmRenewalDepTransModel(CommonUtil.convertObjToStr(cboRenewalDepTransMode.getSelectedItem()));
            updateRenewalDepositOBFields();
        }
    }//GEN-LAST:event_cboRenewalDepTransModeItemStateChanged

    private void cboRenewalInvestmentBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboRenewalInvestmentBehavesItemStateChanged
        // TODO add your handling code here:
        //resetRenewalInsideDepositDetails();
    }//GEN-LAST:event_cboRenewalInvestmentBehavesItemStateChanged

    private void chkAddMountToDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAddMountToDepositActionPerformed
        // TODO add your handling code here:
        if (chkAddMountToDeposit.isSelected() == true) {
            updateRenewalDepositOBFields();
//            ClientUtil.showMessageWindow("If Additional Amount is added... It will be Renewed From Today....\n"
//            + "Deposit Date is :" + curDate.clone());
            dropBtnVisible(false);
            observable.setChkAddMountToDeposit("Y");
            tabRenewalTransDetails.add(cPanAddAmountToDeposits);
            tabRenewalTransDetails.setSelectedIndex(2);
            tabRenewalTransDetails.setTitleAt(2, "Add Amount To Deposit");
        } else {
            observable.setChkAddMountToDeposit("N");
            tabRenewalTransDetails.remove(2);
            dropBtnVisible(false);
        }
    }//GEN-LAST:event_chkAddMountToDepositActionPerformed

    private void rdoSBorCA_yesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoSBorCA_yesItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_rdoSBorCA_yesItemStateChanged

    private void rdoRenewalWithSameNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoRenewalWithSameNoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoRenewalWithSameNoItemStateChanged

    private void rdoRenewalWithSameNoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoRenewalWithSameNoStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoRenewalWithSameNoStateChanged

    private void tdtRenewalFDEffectiveDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRenewalFDEffectiveDtFocusLost
        // TODO add your handling code here:
        calculateRenewalMatDate();
        String behaves = observable.callForRenewalBehaves();
        if (behaves.equals("OTHER_BANK_FD")) {
            calcRenewalFixedInterest();
            cboRenewalFDInterestPaymentActionPerform();
        } else if (behaves.equals("OTHER_BANK_CCD")) {
            calcRenewalCummulativeInterest();
            txtRenewalFDMaturityAmt.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(txtRenewalFDPricipalAmt.getText()) + CommonUtil.convertObjToDouble(txtRenewalFDInterestReceivable.getText())));
        }
        calcRenewalTransAmount();
    }//GEN-LAST:event_tdtRenewalFDEffectiveDtFocusLost

private void txtFDInterestReceivedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFDInterestReceivedActionPerformed
// TODO add your handling code here:    
}//GEN-LAST:event_txtFDInterestReceivedActionPerformed
private void resetInterestReceivableTDS(){
   /* double intReceivable=CommonUtil.convertObjToDouble(renInterestAmt);
    if(intReceivable>0){
        double TDSAmt=CommonUtil.convertObjToDouble(txtInvestTDS.getText());
        double newRenewalIntAmt=intReceivable-TDSAmt;
        System.out.println("newRenewalIntAmt->"+newRenewalIntAmt +"intReceivable->"+intReceivable +"TDSAmt->"+TDSAmt);
        if(rdoRenewalWithPartialInterest.isSelected()){
            double amtRec= CommonUtil.convertObjToDouble(lblRenewalTotalInrestAmt.getText());
            if(newRenewalIntAmt>0){
                 txtFDInterestReceivable.setText(CommonUtil.convertObjToStr(newRenewalIntAmt));
                 txtFDInterestReceivableFocusLost(null);
             // txtFDInterestReceivable.setText(CommonUtil.convertObjToStr(amtRec-TDSAmt));
             // txtFDInterestReceivableFocusLost(null);
              lblRenewalTotalInrestAmt.setText(CommonUtil.convertObjToStr(newRenewalIntAmt));
              txtRenewalWithdrawalAmtFocusLost(null);
            }
            else{
              //  ClientUtil.showAlertWindow("TDS grater than Renewal Interest amount!!!");
               // return;
                
            }
        }
        else{
            if(newRenewalIntAmt>0){
              txtFDInterestReceivable.setText(CommonUtil.convertObjToStr(newRenewalIntAmt));
              txtFDInterestReceivableFocusLost(null);
            }
            else{
                ClientUtil.showAlertWindow("TDS grater than Renewal Interest amount!!!");
                return;
            }
        }
    }
    else{
        ClientUtil.showAlertWindow("TDS not applicable!!!");
        return;
    }*/
    String behaves = observable.callForRenewalBehaves();
           if (behaves.equals("OTHER_BANK_FD")) {
                calcRenewalFixedInterest();
                cboRenewalFDInterestPaymentActionPerform();
            } else if (behaves.equals("OTHER_BANK_CCD")) {
                calcRenewalCummulativeInterest();
            }
}
private void txtInvestTDSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvestTDSActionPerformed
    // TODO add your handling code here:
    resetInterestReceivableTDS();
}//GEN-LAST:event_txtInvestTDSActionPerformed

private void txtInvestTDSFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestTDSFocusLost
    // TODO add your handling code here:
    resetInterestReceivableTDS();
}//GEN-LAST:event_txtInvestTDSFocusLost

    private void txtFeesPaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFeesPaidFocusLost
        // TODO add your handling code here:
         if (txtNoOfShares.getText().length() > 0 && txtShareFaceValue.getText().length() > 0) {
            double totalShareAmount = (CommonUtil.convertObjToDouble(txtNoOfShares.getText()).doubleValue()
                    * CommonUtil.convertObjToDouble(txtShareFaceValue.getText()).doubleValue());
            txtShareValue.setText(String.valueOf(totalShareAmount));
            txtShareValue.setEnabled(false);
            txtInvestmentAmount.setText(String.valueOf(totalShareAmount));
            txtInvestmentAmount.setEnabled(false);
            txtInvestmentAmountFocusLost(null);
        } else {
            txtShareValue.setText("");
            txtInvestmentAmount.setText("");
        }
    }//GEN-LAST:event_txtFeesPaidFocusLost
        private void dropBtnVisible(boolean flag)
    {
        lblRenewalDepositTransProdType.setVisible(flag);
        cboRenewalDepTransProdType.setVisible(flag);
        lblRenewalDepositTransProdId.setVisible(flag);
        cboRenewalDepTransProdId.setVisible(flag);
        lblRenewalDepositTransAccNo.setVisible(flag);
        txtRenewalDepositID.setVisible(flag);
        btnRenewalInvestmentsID.setVisible(flag);
        lblRenewalDepositTransCustName.setVisible(flag);
        lblRenewalCustNameValue.setVisible(flag);
    }

    private void btnVisible(boolean flag)
    {
        lblRenewalDepositTransMode.setVisible(flag);
        cboRenewalDepTransMode.setVisible(flag);
        lblRenewalDepositTransProdType.setVisible(flag);
        lblRenewalDepositTransProdId.setVisible(flag);
        lblRenewalDepositTransAccNo.setVisible(flag);
        cboRenewalDepTransProdType.setVisible(flag);
        cboRenewalDepTransProdId.setVisible(flag);
        txtRenewalDepositID.setVisible(flag);
        btnRenewalInvestmentsID.setVisible(flag);
        lblRenewalDepositTransCustName.setVisible(flag);
        lblRenewalCustNameValue.setVisible(flag);
    }
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
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCheckBookDelete;
    private com.see.truetransact.uicomponent.CButton btnCheckBookNew;
    private com.see.truetransact.uicomponent.CButton btnCheckBookSave;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClosedDetails;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInvestmentID;
    private com.see.truetransact.uicomponent.CButton btnInvestmentIDTrans1;
    private com.see.truetransact.uicomponent.CButton btnInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRenewalInvestmentID;
    private com.see.truetransact.uicomponent.CButton btnRenewalInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CButton btnRenewalInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CButton btnRenewalInvestmentsID;
    private com.see.truetransact.uicomponent.CButton btnReset;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUpdate;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanAddAmountToDeposits;
    private com.see.truetransact.uicomponent.CComboBox cboFDInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentBehaves;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentType;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalDepTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalDepTransProdId;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalDepTransProdType;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalFDInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalInvestmentBehaves;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalInvestmentType;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CCheckBox chkAddMountToDeposit;
    private com.see.truetransact.uicomponent.CCheckBox chkCallOption;
    private com.see.truetransact.uicomponent.CCheckBox chkPutOption;
    private com.see.truetransact.uicomponent.CCheckBox chkSetupOption;
    private com.see.truetransact.uicomponent.CLabel lblBankCodeID;
    private com.see.truetransact.uicomponent.CLabel lblBankNameID;
    private com.see.truetransact.uicomponent.CLabel lblBankNameIDValue;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeID;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameID;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameIDValue;
    private com.see.truetransact.uicomponent.CLabel lblCallOption;
    private com.see.truetransact.uicomponent.CLabel lblCallOptionNoOfYears;
    private com.see.truetransact.uicomponent.CLabel lblCheckBookAllowed;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblClassifaction;
    private com.see.truetransact.uicomponent.CLabel lblFDAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblFDAccountRefNO;
    private com.see.truetransact.uicomponent.CLabel lblFDAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblFDEffectiveDt;
    private com.see.truetransact.uicomponent.CLabel lblFDIntReceivedTillDt;
    private com.see.truetransact.uicomponent.CLabel lblFDInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblFDInterestReceivable;
    private com.see.truetransact.uicomponent.CLabel lblFDInterestReceived;
    private com.see.truetransact.uicomponent.CLabel lblFDInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFDInvestmentPeriod;
    private com.see.truetransact.uicomponent.CLabel lblFDMaturityAmt;
    private com.see.truetransact.uicomponent.CLabel lblFDMaturityDt;
    private com.see.truetransact.uicomponent.CLabel lblFDPeriodicIntrest;
    private com.see.truetransact.uicomponent.CLabel lblFDPricipalAmt;
    private com.see.truetransact.uicomponent.CLabel lblFDRateOfInt;
    private com.see.truetransact.uicomponent.CLabel lblFeesPaid;
    private com.see.truetransact.uicomponent.CLabel lblFromNO;
    private com.see.truetransact.uicomponent.CLabel lblInvestTDS;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentID;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentIDTrans1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentName;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentPeriod_Days1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentPeriod_Months1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentPeriod_Years1;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentType;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblNoOfCheques;
    private com.see.truetransact.uicomponent.CLabel lblNoOfShares;
    private com.see.truetransact.uicomponent.CLabel lblOthersName;
    private com.see.truetransact.uicomponent.CLabel lblPutOption;
    private com.see.truetransact.uicomponent.CLabel lblPutOptionNoOfYears;
    private com.see.truetransact.uicomponent.CLabel lblRFAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblRFAccountRefNO;
    private com.see.truetransact.uicomponent.CLabel lblRFAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblRFInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblRFPricipalAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewal;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAddingDepTrans;
    private com.see.truetransact.uicomponent.CLabel lblRenewalCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepTransAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepositTransAccNo;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepositTransCustName;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepositTransMode;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepositTransProdId;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDepositTransProdType;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDAccountRefNO;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDEffectiveDt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDInterestReceivable;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDInvestmentPeriod;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDMaturityAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDMaturityDt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDPeriodicIntrest;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDPricipalAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalFDRateOfInt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentID;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentName;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentPeriod_Days;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentPeriod_Months;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentPeriod_Years;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInvestmentType;
    private com.see.truetransact.uicomponent.CLabel lblRenewalNarration;
    private com.see.truetransact.uicomponent.CLabel lblRenewalTotalInrest;
    private com.see.truetransact.uicomponent.CLabel lblRenewalTotalInrestAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalTransCashOrTransfer;
    private com.see.truetransact.uicomponent.CLabel lblRenewalWithdrawalAmt;
    private com.see.truetransact.uicomponent.CLabel lblSBAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblSBAccountRefNO;
    private com.see.truetransact.uicomponent.CLabel lblSBAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblSBInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblSBOperatorParticulars;
    private com.see.truetransact.uicomponent.CLabel lblSetUpOptionNoOfYears;
    private com.see.truetransact.uicomponent.CLabel lblSetupOption;
    private com.see.truetransact.uicomponent.CLabel lblShareAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblShareAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblShareFaceValue;
    private com.see.truetransact.uicomponent.CLabel lblShareInternalAccNo;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
    private com.see.truetransact.uicomponent.CLabel lblShareValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStopPayment;
    private com.see.truetransact.uicomponent.CLabel lblToNO;
    private com.see.truetransact.uicomponent.CLabel lblTransCashOrTransfer;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDt;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CLabel lblWithPrincipal;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAddAmountMainpanel;
    private javax.swing.JPanel panAddAmountPanel;
    private com.see.truetransact.uicomponent.CPanel panBankCode;
    private com.see.truetransact.uicomponent.CPanel panBankCodeAndBranchCode;
    private com.see.truetransact.uicomponent.CPanel panCheckBookAllowed;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn;
    private com.see.truetransact.uicomponent.CPanel panCheckBookTable;
    private com.see.truetransact.uicomponent.CPanel panChequeDetails;
    private com.see.truetransact.uicomponent.CPanel panClosingPosition;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed3;
    private com.see.truetransact.uicomponent.CPanel panFDInvestmentPeriod;
    private com.see.truetransact.uicomponent.CPanel panInsideDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideDepositDetails1;
    private com.see.truetransact.uicomponent.CPanel panInsideDepositDetails2;
    private com.see.truetransact.uicomponent.CPanel panInsideDepositDetailsRenewal;
    private com.see.truetransact.uicomponent.CPanel panInsideRenewalDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideRenewalDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideRenewalDetailsData;
    private com.see.truetransact.uicomponent.CPanel panInsideSBDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestDetails;
    private com.see.truetransact.uicomponent.CPanel panInvestAmountDetails;
    private com.see.truetransact.uicomponent.CPanel panInvestSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panInvestment;
    private com.see.truetransact.uicomponent.CPanel panInvestmentDetails;
    private com.see.truetransact.uicomponent.CPanel panInvestmentDetails1;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID2;
    private com.see.truetransact.uicomponent.CPanel panInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CPanel panInvestmentSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panInvestmentTypesDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalFDInvestmentPeriod;
    private com.see.truetransact.uicomponent.CPanel panRenewalInsideDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvTransDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvestAmountDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvestSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvestmentID;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvestmentIDTrans;
    private com.see.truetransact.uicomponent.CPanel panRenewalInvestmentSBorCATrans;
    private com.see.truetransact.uicomponent.CPanel panRenewalNoDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalSBorCA;
    private com.see.truetransact.uicomponent.CPanel panRenewalTransaction;
    private com.see.truetransact.uicomponent.CPanel panRenewallYesNo;
    private com.see.truetransact.uicomponent.CPanel panReserveFund;
    private com.see.truetransact.uicomponent.CPanel panSBCheckBookTable;
    private com.see.truetransact.uicomponent.CPanel panSBorCA;
    private com.see.truetransact.uicomponent.CPanel panSecurityType;
    private com.see.truetransact.uicomponent.CPanel panShareDetails;
    private com.see.truetransact.uicomponent.CPanel panShareMemberID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panType;
    private com.see.truetransact.uicomponent.CPanel panWithInterestYesNo;
    private com.see.truetransact.uicomponent.CPanel panWithPrincipalYesNo;
    private com.see.truetransact.uicomponent.CPanel panWithdrawalIntAmount;
    private com.see.truetransact.uicomponent.CButtonGroup rdgCheckBookAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRenewal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRenewalWithInt;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRenewalWithNo;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSBorCA;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgWithInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdgWithPrincipal;
    private com.see.truetransact.uicomponent.CRadioButton rdoCentralSecurity;
    private com.see.truetransact.uicomponent.CRadioButton rdoCheckBookAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoCheckBookAllowed_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoHfm;
    private com.see.truetransact.uicomponent.CRadioButton rdoHfs;
    private com.see.truetransact.uicomponent.CRadioButton rdoHft;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonSlr;
    private com.see.truetransact.uicomponent.CRadioButton rdoOtherSecurity;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalSBorCA_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalSBorCA_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithDiffProdID;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithNewNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithPartialInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithSameNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithoutInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewal_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewal_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBorCA_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBorCA_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSlr;
    private com.see.truetransact.uicomponent.CRadioButton rdoStateSecurity;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithInterest_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithInterest_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithPrincipal_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithPrincipal_yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpCheckBookTable;
    private com.see.truetransact.uicomponent.CScrollPane srpSBTxtAreaOperatorDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtNarration;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtNarration1;
    private com.see.truetransact.uicomponent.CTabbedPane tabClosingType;
    private com.see.truetransact.uicomponent.CTabbedPane tabInvestment;
    private com.see.truetransact.uicomponent.CTabbedPane tabRenewalTransDetails;
    private com.see.truetransact.uicomponent.CTable tblCheckBookTable;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFDAccOpenDt;
    private com.see.truetransact.uicomponent.CDateField tdtFDEffectiveDt;
    private com.see.truetransact.uicomponent.CDateField tdtFDIntReceivedTillDt;
    private com.see.truetransact.uicomponent.CDateField tdtFDMaturityDt;
    private com.see.truetransact.uicomponent.CDateField tdtRFAccOpenDt;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalFDAccOpenDt;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalFDEffectiveDt;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalFDMaturityDt;
    private com.see.truetransact.uicomponent.CDateField tdtSBAccOpenDt;
    private com.see.truetransact.uicomponent.CDateField tdtShareAccOpenDt;
    private com.see.truetransact.uicomponent.CDateField tdtTransactionDt;
    private com.see.truetransact.uicomponent.CTextField txtBankCodeID;
    private com.see.truetransact.uicomponent.CTextField txtBranchCodeID;
    private com.see.truetransact.uicomponent.CTextField txtCallOptionNoOfYears;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtFDAccountRefNO;
    private com.see.truetransact.uicomponent.CTextField txtFDAgencyName;
    private com.see.truetransact.uicomponent.CTextField txtFDInterestReceivable;
    private com.see.truetransact.uicomponent.CTextField txtFDInterestReceived;
    private com.see.truetransact.uicomponent.CTextField txtFDInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtFDInvestmentPeriod_Days;
    private com.see.truetransact.uicomponent.CTextField txtFDInvestmentPeriod_Months;
    private com.see.truetransact.uicomponent.CTextField txtFDInvestmentPeriod_Years;
    private com.see.truetransact.uicomponent.CTextField txtFDMaturityAmt;
    private com.see.truetransact.uicomponent.CTextField txtFDPeriodicIntrest;
    private com.see.truetransact.uicomponent.CTextField txtFDPricipalAmt;
    private com.see.truetransact.uicomponent.CTextField txtFDRateOfInt;
    private com.see.truetransact.uicomponent.CTextField txtFeesPaid;
    private com.see.truetransact.uicomponent.CTextField txtFromNO;
    private com.see.truetransact.uicomponent.CTextField txtInvestTDS;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentID;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentName;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CTextArea txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtNoOfCheques;
    private com.see.truetransact.uicomponent.CTextField txtNoOfShares;
    private com.see.truetransact.uicomponent.CTextField txtOthersName;
    private com.see.truetransact.uicomponent.CTextField txtPutOptionNoOfYears;
    private com.see.truetransact.uicomponent.CTextField txtRFAccountRefNO;
    private com.see.truetransact.uicomponent.CTextField txtRFAgencyName;
    private com.see.truetransact.uicomponent.CTextField txtRFInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtRFPricipalAmt;
    private com.see.truetransact.uicomponent.CTextField txtRenewalDepTransAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtRenewalDepositID;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDAccountRefNO;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDAgencyName;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDInterestReceivable;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDInvestmentPeriod_Days;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDInvestmentPeriod_Months;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDInvestmentPeriod_Years;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDMaturityAmt;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDPeriodicIntrest;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDPricipalAmt;
    private com.see.truetransact.uicomponent.CTextField txtRenewalFDRateOfInt;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentID;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentIDTransSBorCA;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentInternalNoTrans;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentName;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInvestmentRefNoTrans;
    private com.see.truetransact.uicomponent.CTextArea txtRenewalNarration;
    private com.see.truetransact.uicomponent.CTextField txtRenewalWithdrawalAmt;
    private com.see.truetransact.uicomponent.CTextField txtSBAccountRefNO;
    private com.see.truetransact.uicomponent.CTextField txtSBAgencyName;
    private com.see.truetransact.uicomponent.CTextArea txtSBAreaOperatorDetails;
    private com.see.truetransact.uicomponent.CTextField txtSBInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtSetUpOptionNoOfYears;
    private com.see.truetransact.uicomponent.CTextField txtShareAgencyName;
    private com.see.truetransact.uicomponent.CTextField txtShareFaceValue;
    private com.see.truetransact.uicomponent.CTextField txtShareInternalAccNo;
    private com.see.truetransact.uicomponent.CTextField txtShareMemberID;
    private com.see.truetransact.uicomponent.CTextField txtShareType;
    private com.see.truetransact.uicomponent.CTextField txtShareValue;
    private com.see.truetransact.uicomponent.CTextField txtStopPayment;
    private com.see.truetransact.uicomponent.CTextField txtToNO;
    // End of variables declaration//GEN-END:variables
}
