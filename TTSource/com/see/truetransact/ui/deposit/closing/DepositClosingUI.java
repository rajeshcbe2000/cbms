/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingUI.java
 *
 * Created on May 19, 2004, 12:38 PM
 */

package com.see.truetransact.ui.deposit.closing;
//import com.see.truetransact.clientutil.EnhancedTableModel;
//import javax.swing.table.DefaultTableCellRenderer;
//import java.awt.Component;
//import java.awt.Color;
//import javax.swing.JTable;
import java.text.DecimalFormat;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.TextUI;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.see.truetransact.ui.TrueTransactMain ;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.customer.CustDetailsUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...

import com.see.truetransact.ui.operativeaccount.AccountClosingUI;//incase deposits having LTD loan means that time will be using.
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.ui.remittance.RemittanceIssueUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import com.see.truetransact.ui.customer.SmartCustomerUI;

/**
 *
 * @author  Pinky
 * @modified Sunil : Added Transaction UI, Removed Partial Withdrawl
 *                   Added Authorization
 *
 */

public class DepositClosingUI extends CInternalFrame implements Observer,UIMandatoryField{
    
    private DepositClosingRB resourceBundle;
    Rounding rd = new Rounding();
    private List chargelst = null;
    private HashMap mandatoryMap;
    private DepositClosingOB observable;
    private DepositClosingMRB objMandatoryRB;
    private TransDetailsUI transDetailsUI = null;
    private CustDetailsUI custDetailsUI = null;
    private int viewType=-1;
    private final int AUTHORIZE=8, CANCEL=0;
    private final int DEPOSIT_ACCT=100, CLOSED_ACCT=200,LTD_DEP_LIST=250,TRANSFERRING_BRANCH = 300;
    private final String PREMATURE_CLOSING="PREMATURE";
    private final String PARTIAL_WITHDRAWAL="PARTIAL_WITHDRAWAL";
    private final String NEAREST="NEAREST_VALUE";
    private final String HIGHER="HIGHER_VALUE";
    private final String LOWER="LOWER_VALUE";
    private final String NO_ROUND_OFF="NO_ROUND_OFF";
    private boolean partialWithdrawalNew;
    private boolean penalty;
    boolean isFilled = false;
    private boolean LTDflag = false;
    private boolean OKbutton = false;
    private TransactionUI transactionUI = new TransactionUI();    
    AccountClosingUI accountClosing = null;
    DepositLienUI depositLien = null;
    RemittanceIssueUI remittance = null;
    private String behavesLike = "";
    private boolean buttonVisit = false;
    private Date currDt = null;
    private boolean flPtWithoutPeriod = false;
    private double maturityAmount = 0.0;
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    String premClos="";
    private final static double Avg_Millis_Per_Month=365.24*24*60*60*1000 / 12;
    DecimalFormat df =new DecimalFormat("##.00");
    private int rejectFlag=0;
    private boolean transNew = true;
    private boolean tableFlag = false;
    private JTable table = null;
    private String prodDesc = "";
    private HashMap chargeMap = new HashMap();
    private double tableCharge = 0.0;
    private String Lien_Accno="";
    private HashMap amountMap = new HashMap();
    HashMap serviceTax_Map=new HashMap();
    ServiceTaxCalculation objServiceTax;
    RejectionApproveUI rejectionApproveUI=null;
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private List taxSettingsListForCharges = new ArrayList();
    /** Creates new form BeanForm */
    public DepositClosingUI() {
        initComponents();
        
        currDt = ClientUtil.getCurrentDate();
        
        //initTable();
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleYes);
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleNo);
        
        rdgTypesOf_Deposit.add(rdoLTDDeposit);
        rdgTypesOf_Deposit.add(rdoRegularDeposit);
        
        custDetailsUI =  new CustDetailsUI(panAcctDetails);
        initSetUp();
        transDetailsUI =  new TransDetailsUI(panAccountHead);
        transDetailsUI.setSourceScreen("DEPOSIT_CLOSING");
    }
    private void initSetUp(){
        //setFieldNames();
        internationalize();
        // setMandatoryHashMap();
        setHelpMessage();
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("DEPOSITS");
        transactionUI.setParantUI(this);
        tabClosingType.addTab("Transaction", panTransaction);
        setObservable();
        setMaxLength();
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        pwButtonsDisable();
        initComponentData();
        tabClosingType.remove(panPartial); //Partial Withdrawl Logic Changed. Hence remove this tab. Delete it later.
        tabClosingType.resetVisits();
        btnDepositNo.setVisible(false);
        txtDepositNo.setAllowAll(true);
        lblTransferringBranch.setVisible(false);
        panTransferringBranch.setVisible(false);
        panTransferringBranch.setEnabled(false);
        lblBranch.setVisible(false);
        srpSubDeposits.setVisible(false);
        btnTransferringBranch.setEnabled(false);
        tabClosingType.remove(chargePan);
        setPenaltyRateIntApplicable();
    }
    
    
    private void setPenaltyRateIntApplicable(){
        String penaltyRateApplicable = "N";                
        CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("DEP_PENALTY_RATE_APPLICABLE")); 
        if(TrueTransactMain.CBMSPARAMETERS.containsKey("DEP_PENALTY_RATE_APPLICABLE") && TrueTransactMain.CBMSPARAMETERS.get("DEP_PENALTY_RATE_APPLICABLE") != null){
            penaltyRateApplicable = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("DEP_PENALTY_RATE_APPLICABLE"));
        }       
        if(penaltyRateApplicable.equals("Y")){
            rdoPenaltyRateApplicbleNo.setEnabled(false);
        }
    }
    
    
    private void initComponentData(){
        this.cboProductId.setModel(observable.getCbmProductId());
        this.tblSubDeposits.setModel(observable.getTbmSubDeposit());
        this.tblPartialWithdrawal.setModel(observable.getTbmPartialWithdrawal());
    }
    private void setObservable() {
        try{
            observable = DepositClosingOB.getInstance();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void enableDisableButtons(boolean enableDisable) {
        this.btnDepositNo.setEnabled(enableDisable);
    }
    private void enableDisableDepositOut(boolean flag){
        panTransferOut .setEnabled(flag);
        txtTransferringBranch.setEnabled(flag);
        btnTransferringBranch.setEnabled(flag);
        rdoTransfer_Out_Yes.setEnabled(flag);
        rdoTransfer_In_No.setEnabled(flag);
    }
    private void setMaxLength(){
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDepositNo.setName("btnDepositNo");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboProductId.setName("cboProductId");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadValue.setName("lblAccountHeadValue");
        lblBalance.setName("lblBalance");
        lblBalanceDeposit.setName("lblBalanceDeposit");
        lblBalanceDepositValue.setName("lblBalanceDepositValue");
        lblBalanceValue.setName("lblBalanceValue");
        lblCategory.setName("lblCategory");
        lblCategoryValue.setName("lblCategoryValue");
        lblClDisbursal.setName("lblClDisbursal");
        lblClDisbursalValue.setName("lblClDisbursalValue");
        lblDelayedAmount.setName("lblDelayedAmount");
        lblDelayedInstallments.setName("lblDelayedInstallments");
        //2lines added 26.03.2007
        lblRateApplicableValue.setName("lblRateApplicableValue");
        lblPenaltyPenalRateValue.setName("lblPenaltyPenalRateValue");
        lblClTDSCollected.setName("lblClTDSCollected");
        lblClTDSCollectedValue.setName("lblClTDSCollectedValue");
        lblConstitution.setName("lblConstitution");
        lblConstitutionValue.setName("lblConstitutionValue");
//        lblDepositAccountName.setName("lblDepositAccountName");
        lblDepositAccountNameValue.setName("lblDepositAccountNameValue");
        lblDepositDate.setName("lblDepositDate");
        lblDepositDateValue.setName("lblDepositDateValue");
        lblDepositNo.setName("lblDepositNo");
        lblInstDue.setName("lblInstDue");
        lblInstDueValue.setName("lblInstDueValue");
        lblInstPaid.setName("lblInstPaid");
        lblInstPaidValue.setName("lblInstPaidValue");
        lblInterestCr.setName("lblInterestCr");
        lblInterestCrValue.setName("lblInterestCrValue");
        lblInterestCredited.setName("lblInterestCredited");
        lblInterestCreditedValue.setName("lblInterestCreditedValue");
        lblActualPeriodRun.setName("lblActualPeriodRun");
        lblActualPeriodRunValue.setName("lblActualPeriodRunValue");
        lblInterestDr.setName("lblInterestDr");
        lblInterestDrValue.setName("lblInterestDrValue");
        lblInterestDrawn.setName("lblInterestDrawn");
        lblInterestDrawnValue.setName("lblInterestDrawnValue");
        lblInterestPaymentFrequency.setName("lblInterestPaymentFrequency");
        lblInterestPaymentFrequencyValue.setName("lblInterestPaymentFrequencyValue");
        lblLastInterestApplDate.setName("lblLastInterestApplDate");
        lblLastInterestApplDateValue.setName("lblLastInterestApplDateValue");
        lblLienFreezeAmount.setName("lblLienFreezeAmount");
        lblLienFreezeAmountValue.setName("lblLienFreezeAmountValue");
        lblMV.setName("lblMV");
        lblMVvalue.setName("lblMVvalue");
        lblMaturityDate.setName("lblMaturityDate");
        lblMaturityDateValue.setName("lblMaturityDateValue");
        lblMsg.setName("lblMsg");
        lblPayRec.setName("lblPayRec");
        lblPayRecValue.setName("lblPayRecValue");
        lblPeriod.setName("lblPeriod");
        lblPeriodValue.setName("lblPeriodValue");
        lblPrincipal.setName("lblPrincipal");
        lblPrincipalValue.setName("lblPrincipalValue");
        lblProductId.setName("lblProductId");
        lblRateOfInterest.setName("lblRateOfInterest");
        lblRateOfInterestValue.setName("lblRateOfInterestValue");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTDSCollected.setName("lblTDSCollected");
        lblTDSCollectedValue.setName("lblTDSCollectedValue");
        lblWithDrawnAmount.setName("lblWithDrawnAmount");
        lblWithDrawnAmountValue.setName("lblWithDrawnAmountValue");
        mbrMain.setName("mbrMain");
        panClosingPosition.setName("panClosingPosition");
        //        panCustomer.setName("panCustomer");
        panDeposit.setName("panDeposit");
        panDepositAcctInfo.setName("panDepositAcctInfo");
        panDepositDetails.setName("panDepositDetails");
        panDepositNo.setName("panDepositNo");
        panButtons.setName("panEmpty0");
        //        panEmpty1.setName("panEmpty1");
        panMain.setName("panMain");
        panNormalClosure.setName("panNormalClosure");
        panStatus.setName("panStatus");
        panSubDeposit.setName("panSubDeposit");
        panSubDepositInfo.setName("panSubDepositInfo");
        srpSubDeposits.setName("srpSubDeposits");
        tblSubDeposits.setName("tblSubDeposits");
        txtDepositNo.setName("txtDepositNo");
          //Added by Chithra on 21-04-14
        lblAdditionalIntrstHed .setName("lblAdditionalIntrstHed");
        lblPeriodOfMaturity.setName(("lblPeriodOfMaturity"));
        lblMaturityPeriod.setName("lblMaturityPeriod");
        lblAddIntrstRate.setName("lblAddIntrstRate");
        lblAddIntRteAmt.setName("lblAddIntRteAmt");
        lblAddIntRtAmtVal.setName("lblAddIntRtAmtVal");
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new DepositClosingRB();
        lblCategoryValue.setText(resourceBundle.getString("lblCategoryValue"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblInterestPaymentFrequencyValue.setText(resourceBundle.getString("lblInterestPaymentFrequencyValue"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblBalanceDeposit.setText(resourceBundle.getString("lblBalanceDeposit"));
        lblInterestCredited.setText(resourceBundle.getString("lblInterestCredited"));
        lblActualPeriodRun.setText(resourceBundle.getString("lblActualPeriodRun"));
        lblRateOfInterest.setText(resourceBundle.getString("lblRateOfInterest"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTDSCollectedValue.setText(resourceBundle.getString("lblTDSCollectedValue"));
        lblPayRecValue.setText(resourceBundle.getString("lblPayRecValue"));
        lblMVvalue.setText(resourceBundle.getString("lblMVvalue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
        lblWithDrawnAmount.setText(resourceBundle.getString("lblWithDrawnAmount"));
        lblClTDSCollected.setText(resourceBundle.getString("lblClTDSCollected"));
        lblInterestDrValue.setText(resourceBundle.getString("lblInterestDrValue"));
        lblMV.setText(resourceBundle.getString("lblMV"));
        lblTDSCollected.setText(resourceBundle.getString("lblTDSCollected"));
        lblBalanceDepositValue.setText(resourceBundle.getString("lblBalanceDepositValue"));
        lblInterestCreditedValue.setText(resourceBundle.getString("lblInterestCreditedValue"));
        lblActualPeriodRunValue.setText(resourceBundle.getString("lblActualPeriodRunValue"));
        lblDelayedAmount.setText(resourceBundle.getString("lblDelayedAmount"));
        lblDelayedInstallments.setText(resourceBundle.getString("lblDelayedInstallments"));
        //2lines added 26.03.2007
        lblRateApplicableValue.setName("lblRateApplicableValue");
        lblPenaltyPenalRateValue.setName("lblPenaltyPenalRateValue");
        lblDepositDate.setText(resourceBundle.getString("lblDepositDate"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        lblBalanceValue.setText(resourceBundle.getString("lblBalanceValue"));
        lblInstPaidValue.setText(resourceBundle.getString("lblInstPaidValue"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        //        ((TitledBorder)panDepositDetails.getBorder()).setTitle(resourceBundle.getString("panDepositDetails"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblMaturityDateValue.setText(resourceBundle.getString("lblMaturityDateValue"));
        lblDepositDateValue.setText(resourceBundle.getString("lblDepositDateValue"));
        lblRateOfInterestValue.setText(resourceBundle.getString("lblRateOfInterestValue"));
        lblPrincipalValue.setText(resourceBundle.getString("lblPrincipalValue"));
        lblInstDue.setText(resourceBundle.getString("lblInstDue"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        lblDepositAccountName.setText(resourceBundle.getString("lblDepositAccountName"));
        lblInterestDr.setText(resourceBundle.getString("lblInterestDr"));
        lblWithDrawnAmountValue.setText(resourceBundle.getString("lblWithDrawnAmountValue"));
        lblLienFreezeAmount.setText(resourceBundle.getString("lblLienFreezeAmount"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //	((javax.swing.border.TitledBorder)panNormalClosure.getBorder()).setTitle(resourceBundle.getString("panPresentPosition"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblLastInterestApplDateValue.setText(resourceBundle.getString("lblLastInterestApplDateValue"));
        lblInterestCrValue.setText(resourceBundle.getString("lblInterestCrValue"));
        lblActualPeriodRunValue.setText(resourceBundle.getString("lblActualPeriodRunValue"));
        lblDepositAccountNameValue.setText(resourceBundle.getString("lblDepositAccountNameValue"));
        //        ((TitledBorder)panClosingPosition.getBorder()).setTitle(resourceBundle.getString("panClosingPosition"));
        lblConstitutionValue.setText(resourceBundle.getString("lblConstitutionValue"));
        lblPayRec.setText(resourceBundle.getString("lblPayRec"));
        //        ((TitledBorder)panCustomer.getBorder()).setTitle(resourceBundle.getString("panCustomer"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblAccountHeadValue.setText(resourceBundle.getString("lblAccountHeadValue"));
        lblPeriodValue.setText(resourceBundle.getString("lblPeriodValue"));
        lblInstDueValue.setText(resourceBundle.getString("lblInstDueValue"));
        lblPrincipal.setText(resourceBundle.getString("lblPrincipal"));
        lblPeriod.setText(resourceBundle.getString("lblPeriod"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblInterestPaymentFrequency.setText(resourceBundle.getString("lblInterestPaymentFrequency"));
        lblLastInterestApplDate.setText(resourceBundle.getString("lblLastInterestApplDate"));
        lblInterestCr.setText(resourceBundle.getString("lblInterestCr"));
        lblClDisbursal.setText(resourceBundle.getString("lblClDisbursal"));
        lblClTDSCollectedValue.setText(resourceBundle.getString("lblClTDSCollectedValue"));
        lblInterestDrawn.setText(resourceBundle.getString("lblInterestDrawn"));
        ((TitledBorder)panSubDepositInfo.getBorder()).setTitle(resourceBundle.getString("panSubDepositInfo"));
        lblInstPaid.setText(resourceBundle.getString("lblInstPaid"));
        lblInterestDrawnValue.setText(resourceBundle.getString("lblInterestDrawnValue"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblLienFreezeAmountValue.setText(resourceBundle.getString("lblLienFreezeAmountValue"));
        btnDepositNo.setText(resourceBundle.getString("btnDepositNo"));
        lblClDisbursalValue.setText(resourceBundle.getString("lblClDisbursalValue"));
        lblTransferringBranch.setText(resourceBundle.getString("lblTransferringBranch"));
        lblBranch.setText(resourceBundle.getString("lblBranch"));        
        lblAgentCommisionRecovered.setText(resourceBundle.getString("lblAgentCommisionRecovered"));
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgPenaltyRateApplicable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTypesOf_Deposit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTransferOut = new com.see.truetransact.uicomponent.CButtonGroup();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrTermDeposit = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panDepositAcctInfo = new com.see.truetransact.uicomponent.CPanel();
        panDeposit = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        panDepositNo = new com.see.truetransact.uicomponent.CPanel();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        lblDepositAccountNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        lblConstitutionValue = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        lblCategoryValue = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementMode = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementModeValue = new com.see.truetransact.uicomponent.CLabel();
        panDepositType = new com.see.truetransact.uicomponent.CPanel();
        rdoRegularDeposit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLTDDeposit = new com.see.truetransact.uicomponent.CRadioButton();
        lblDepositAccountName1 = new com.see.truetransact.uicomponent.CLabel();
        panTransferOut = new com.see.truetransact.uicomponent.CPanel();
        rdoTransfer_Out_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransfer_In_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransferOut = new com.see.truetransact.uicomponent.CLabel();
        lblTransferringBranch = new com.see.truetransact.uicomponent.CLabel();
        panTransferringBranch = new com.see.truetransact.uicomponent.CPanel();
        txtTransferringBranch = new com.see.truetransact.uicomponent.CTextField();
        btnTransferringBranch = new com.see.truetransact.uicomponent.CButton();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        lblBranchValue = new com.see.truetransact.uicomponent.CLabel();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panAcctDetails = new com.see.truetransact.uicomponent.CPanel();
        panSubDepositInfo = new com.see.truetransact.uicomponent.CPanel();
        srpSubDeposits = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubDeposits = new com.see.truetransact.uicomponent.CTable();
        panSubDeposit = new com.see.truetransact.uicomponent.CPanel();
        tabClosingType = new com.see.truetransact.uicomponent.CTabbedPane();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPrincipal = new com.see.truetransact.uicomponent.CLabel();
        lblPrincipalValue = new com.see.truetransact.uicomponent.CLabel();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepositDate = new com.see.truetransact.uicomponent.CLabel();
        lblDepositDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblRateOfInterestValue = new com.see.truetransact.uicomponent.CLabel();
        lblMV = new com.see.truetransact.uicomponent.CLabel();
        lblMVvalue = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaymentFrequencyValue = new com.see.truetransact.uicomponent.CLabel();
        panNormalClosure = new com.see.truetransact.uicomponent.CPanel();
        lblBalanceDeposit = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceDepositValue = new com.see.truetransact.uicomponent.CLabel();
        lblWithDrawnAmount = new com.see.truetransact.uicomponent.CLabel();
        lblWithDrawnAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblInterestCredited = new com.see.truetransact.uicomponent.CLabel();
        lblInterestCreditedValue = new com.see.truetransact.uicomponent.CLabel();
        lblInterestDrawn = new com.see.truetransact.uicomponent.CLabel();
        lblInterestDrawnValue = new com.see.truetransact.uicomponent.CLabel();
        lblTDSCollected = new com.see.truetransact.uicomponent.CLabel();
        lblTDSCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblLastInterestApplDate = new com.see.truetransact.uicomponent.CLabel();
        lblLastInterestApplDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblLienFreezeAmount = new com.see.truetransact.uicomponent.CLabel();
        lblLienFreezeAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblInstPaid = new com.see.truetransact.uicomponent.CLabel();
        lblInstPaidValue = new com.see.truetransact.uicomponent.CLabel();
        lblInstDue = new com.see.truetransact.uicomponent.CLabel();
        lblInstDueValue = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceValue = new com.see.truetransact.uicomponent.CLabel();
        panPartial = new com.see.truetransact.uicomponent.CPanel();
        panPartialWithDrawal = new com.see.truetransact.uicomponent.CPanel();
        lblUnitsAvailable = new com.see.truetransact.uicomponent.CLabel();
        lblUnitsAvailableValue = new com.see.truetransact.uicomponent.CLabel();
        lblUnitsDrawn = new com.see.truetransact.uicomponent.CLabel();
        lblUnitsDrawnValue = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureClosureRate = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureClosureRateValue = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureClosurePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureClosurePeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblAmountWithDrawn = new com.see.truetransact.uicomponent.CLabel();
        txtAmountWithDrawn = new com.see.truetransact.uicomponent.CTextField();
        panProrataPosition = new com.see.truetransact.uicomponent.CPanel();
        lblPresentUnitInt = new com.see.truetransact.uicomponent.CLabel();
        lblPresentUnitIntValue = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementUnitInt = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementUnitIntValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepositRunPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblDepositRunPeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfUnits = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfUnits = new com.see.truetransact.uicomponent.CTextField();
        panMainPW = new com.see.truetransact.uicomponent.CPanel();
        srpPartialWithdrawal = new com.see.truetransact.uicomponent.CScrollPane();
        tblPartialWithdrawal = new com.see.truetransact.uicomponent.CTable();
        panPWButtons = new com.see.truetransact.uicomponent.CPanel();
        btnPWNew = new com.see.truetransact.uicomponent.CButton();
        btnPWSave = new com.see.truetransact.uicomponent.CButton();
        btnPWDelete = new com.see.truetransact.uicomponent.CButton();
        panClosingPosition = new com.see.truetransact.uicomponent.CPanel();
        lblInterestCr = new com.see.truetransact.uicomponent.CLabel();
        lblInterestCrValue = new com.see.truetransact.uicomponent.CLabel();
        lblInterestDr = new com.see.truetransact.uicomponent.CLabel();
        lblInterestDrValue = new com.see.truetransact.uicomponent.CLabel();
        lblClTDSCollected = new com.see.truetransact.uicomponent.CLabel();
        lblClTDSCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblPayRec = new com.see.truetransact.uicomponent.CLabel();
        lblPayRecValue = new com.see.truetransact.uicomponent.CLabel();
        lblClDisbursal = new com.see.truetransact.uicomponent.CLabel();
        lblClDisbursalValue = new com.see.truetransact.uicomponent.CLabel();
        lblPreClosingPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPreClosingPeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblPreClosingRate = new com.see.truetransact.uicomponent.CLabel();
        lblPreClosingRateValue = new com.see.truetransact.uicomponent.CLabel();
        lblRateApplicable = new com.see.truetransact.uicomponent.CLabel();
        lblRateApplicableValue = new com.see.truetransact.uicomponent.CLabel();
        lblPenaltyPenalRate = new com.see.truetransact.uicomponent.CLabel();
        lblPenaltyPenalRateValue = new com.see.truetransact.uicomponent.CLabel();
        lblActualPeriodRun = new com.see.truetransact.uicomponent.CLabel();
        lblActualPeriodRunValue = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedInstallments = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedInstallmentValue = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblDeathClaim = new com.see.truetransact.uicomponent.CLabel();
        lblDeathClaimInterest = new com.see.truetransact.uicomponent.CLabel();
        lblDeathClaimInterestValue = new com.see.truetransact.uicomponent.CLabel();
        lblDeathClaimValue = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCommisionRecovered = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCommisionRecoveredValue = new com.see.truetransact.uicomponent.CLabel();
        lblPreIntPayable = new com.see.truetransact.uicomponent.CLabel();
        lblPreIntValue = new com.see.truetransact.uicomponent.CLabel();
        lblAdditionalIntrstHed = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodOfMaturity = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblAddIntrstRate = new com.see.truetransact.uicomponent.CLabel();
        lblAddIntrstRteVal = new com.see.truetransact.uicomponent.CLabel();
        lblAddIntRteAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAddIntRtAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        chargePan = new com.see.truetransact.uicomponent.CPanel();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        servicTaxPanel = new com.see.truetransact.uicomponent.CPanel();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        btnApplyPenal = new com.see.truetransact.uicomponent.CButton();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        lblRateApplicble = new com.see.truetransact.uicomponent.CLabel();
        rdoPenaltyRateApplicbleYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenaltyRateApplicbleNo = new com.see.truetransact.uicomponent.CRadioButton();
        btnTableOk = new com.see.truetransact.uicomponent.CButton();
        btnTableCancel = new com.see.truetransact.uicomponent.CButton();
        lblClosureType = new com.see.truetransact.uicomponent.CLabel();
        lblClosureTypeValue = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 690));
        setPreferredSize(new java.awt.Dimension(850, 690));

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
        tbrTermDeposit.add(btnView);

        lblSpace4.setText("     ");
        tbrTermDeposit.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermDeposit.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermDeposit.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace27);

        lblSpace6.setText("     ");
        tbrTermDeposit.add(lblSpace6);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnReject);

        lblSpace5.setText("     ");
        tbrTermDeposit.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermDeposit.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermDeposit.add(btnClose);

        getContentPane().add(tbrTermDeposit, java.awt.BorderLayout.NORTH);

        panMain.setPreferredSize(new java.awt.Dimension(800, 650));
        panMain.setLayout(new java.awt.GridBagLayout());

        panDepositAcctInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Customer Details"));
        panDepositAcctInfo.setMinimumSize(new java.awt.Dimension(481, 246));
        panDepositAcctInfo.setPreferredSize(new java.awt.Dimension(481, 220));
        panDepositAcctInfo.setLayout(new java.awt.GridBagLayout());

        panDeposit.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panDeposit.setMinimumSize(new java.awt.Dimension(310, 220));
        panDeposit.setPreferredSize(new java.awt.Dimension(310, 225));
        panDeposit.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDeposit.add(lblProductId, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(200);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panDeposit.add(cboProductId, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        lblAccountHead.setMinimumSize(new java.awt.Dimension(82, 15));
        lblAccountHead.setPreferredSize(new java.awt.Dimension(82, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDeposit.add(lblAccountHead, gridBagConstraints);

        lblAccountHeadValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblAccountHeadValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAccountHeadValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panDeposit.add(lblAccountHeadValue, gridBagConstraints);

        lblDepositNo.setText("Deposit No.");
        lblDepositNo.setMinimumSize(new java.awt.Dimension(68, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDeposit.add(lblDepositNo, gridBagConstraints);

        panDepositNo.setMinimumSize(new java.awt.Dimension(145, 23));
        panDepositNo.setPreferredSize(new java.awt.Dimension(128, 23));
        panDepositNo.setLayout(new java.awt.GridBagLayout());

        txtDepositNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtDepositNo.setNextFocusableComponent(btnTableOk);
        txtDepositNo.setPreferredSize(new java.awt.Dimension(150, 21));
        txtDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositNoActionPerformed(evt);
            }
        });
        txtDepositNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositNoFocusLost(evt);
            }
        });
        panDepositNo.add(txtDepositNo, new java.awt.GridBagConstraints());

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setNextFocusableComponent(rdoPenaltyRateApplicbleYes);
        btnDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        panDepositNo.add(btnDepositNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panDeposit.add(panDepositNo, gridBagConstraints);

        lblDepositAccountNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblDepositAccountNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDepositAccountNameValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDepositAccountNameValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblDepositAccountNameValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 80, 1, 2);
        panDeposit.add(lblDepositAccountNameValue, gridBagConstraints);

        lblConstitution.setText("Constitution");
        lblConstitution.setMinimumSize(new java.awt.Dimension(69, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDeposit.add(lblConstitution, gridBagConstraints);

        lblConstitutionValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblConstitutionValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblConstitutionValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panDeposit.add(lblConstitutionValue, gridBagConstraints);

        lblCategory.setText("Category");
        lblCategory.setMinimumSize(new java.awt.Dimension(52, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDeposit.add(lblCategory, gridBagConstraints);

        lblCategoryValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblCategoryValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCategoryValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panDeposit.add(lblCategoryValue, gridBagConstraints);

        lblSettlementMode.setText("Settlement Mode");
        lblSettlementMode.setMinimumSize(new java.awt.Dimension(99, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDeposit.add(lblSettlementMode, gridBagConstraints);

        lblSettlementModeValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblSettlementModeValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblSettlementModeValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panDeposit.add(lblSettlementModeValue, gridBagConstraints);

        panDepositType.setMinimumSize(new java.awt.Dimension(172, 20));
        panDepositType.setPreferredSize(new java.awt.Dimension(170, 20));
        panDepositType.setLayout(new java.awt.GridBagLayout());

        rdoRegularDeposit.setText("Regular");
        rdoRegularDeposit.setMaximumSize(new java.awt.Dimension(66, 18));
        rdoRegularDeposit.setMinimumSize(new java.awt.Dimension(69, 15));
        rdoRegularDeposit.setPreferredSize(new java.awt.Dimension(69, 15));
        rdoRegularDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRegularDepositActionPerformed(evt);
            }
        });
        panDepositType.add(rdoRegularDeposit, new java.awt.GridBagConstraints());

        rdoLTDDeposit.setText("LTD Deposit");
        rdoLTDDeposit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoLTDDeposit.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoLTDDeposit.setMinimumSize(new java.awt.Dimension(103, 15));
        rdoLTDDeposit.setPreferredSize(new java.awt.Dimension(103, 15));
        rdoLTDDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLTDDepositActionPerformed(evt);
            }
        });
        panDepositType.add(rdoLTDDeposit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDeposit.add(panDepositType, gridBagConstraints);

        lblDepositAccountName1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositAccountName1.setText("Type of Deposit");
        lblDepositAccountName1.setMaximumSize(new java.awt.Dimension(97, 16));
        lblDepositAccountName1.setMinimumSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDeposit.add(lblDepositAccountName1, gridBagConstraints);

        panTransferOut.setMinimumSize(new java.awt.Dimension(100, 20));
        panTransferOut.setPreferredSize(new java.awt.Dimension(100, 20));
        panTransferOut.setLayout(new java.awt.GridBagLayout());

        rdoTransfer_Out_Yes.setText("Yes");
        rdoTransfer_Out_Yes.setMaximumSize(new java.awt.Dimension(50, 16));
        rdoTransfer_Out_Yes.setMinimumSize(new java.awt.Dimension(50, 16));
        rdoTransfer_Out_Yes.setPreferredSize(new java.awt.Dimension(50, 15));
        rdoTransfer_Out_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransfer_Out_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panTransferOut.add(rdoTransfer_Out_Yes, gridBagConstraints);

        rdoTransfer_In_No.setText("No");
        rdoTransfer_In_No.setMaximumSize(new java.awt.Dimension(45, 15));
        rdoTransfer_In_No.setMinimumSize(new java.awt.Dimension(45, 15));
        rdoTransfer_In_No.setPreferredSize(new java.awt.Dimension(45, 15));
        rdoTransfer_In_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransfer_In_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panTransferOut.add(rdoTransfer_In_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panDeposit.add(panTransferOut, gridBagConstraints);

        lblTransferOut.setText("Closure on Transfer Out");
        lblTransferOut.setMaximumSize(new java.awt.Dimension(115, 18));
        lblTransferOut.setMinimumSize(new java.awt.Dimension(137, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDeposit.add(lblTransferOut, gridBagConstraints);

        lblTransferringBranch.setText("TransferOut Branch Code");
        lblTransferringBranch.setMinimumSize(new java.awt.Dimension(146, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDeposit.add(lblTransferringBranch, gridBagConstraints);

        panTransferringBranch.setMinimumSize(new java.awt.Dimension(128, 23));
        panTransferringBranch.setPreferredSize(new java.awt.Dimension(128, 23));
        panTransferringBranch.setLayout(new java.awt.GridBagLayout());

        txtTransferringBranch.setEditable(false);
        txtTransferringBranch.setEnabled(false);
        txtTransferringBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransferringBranch.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panTransferringBranch.add(txtTransferringBranch, gridBagConstraints);

        btnTransferringBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransferringBranch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTransferringBranch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTransferringBranch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransferringBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferringBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panTransferringBranch.add(btnTransferringBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panDeposit.add(panTransferringBranch, gridBagConstraints);

        lblBranch.setText("TransferOut Branch Name");
        lblBranch.setMaximumSize(new java.awt.Dimension(149, 15));
        lblBranch.setMinimumSize(new java.awt.Dimension(151, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDeposit.add(lblBranch, gridBagConstraints);

        lblBranchValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBranchValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblBranchValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panDeposit.add(lblBranchValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panDepositAcctInfo.add(panDeposit, gridBagConstraints);

        panAccountHead.setMinimumSize(new java.awt.Dimension(303, 220));
        panAccountHead.setPreferredSize(new java.awt.Dimension(303, 240));
        panAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panDepositAcctInfo.add(panAccountHead, gridBagConstraints);

        panAcctDetails.setPreferredSize(new java.awt.Dimension(24, 24));
        panAcctDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositAcctInfo.add(panAcctDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMain.add(panDepositAcctInfo, gridBagConstraints);

        panSubDepositInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Sub Deposit Information"));
        panSubDepositInfo.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panSubDepositInfo.setMinimumSize(new java.awt.Dimension(1050, 660));
        panSubDepositInfo.setPreferredSize(new java.awt.Dimension(1050, 660));
        panSubDepositInfo.setLayout(new java.awt.GridBagLayout());

        srpSubDeposits.setMinimumSize(new java.awt.Dimension(453, 42));
        srpSubDeposits.setPreferredSize(new java.awt.Dimension(453, 50));

        tblSubDeposits.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSubDeposits.setMinimumSize(new java.awt.Dimension(60, 16));
        tblSubDeposits.setPreferredSize(new java.awt.Dimension(60, 16));
        tblSubDeposits.setEnabled(false);
        tblSubDeposits.setOpaque(false);
        srpSubDeposits.setViewportView(tblSubDeposits);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panSubDepositInfo.add(srpSubDeposits, gridBagConstraints);

        panSubDeposit.setMinimumSize(new java.awt.Dimension(1050, 580));
        panSubDeposit.setPreferredSize(new java.awt.Dimension(1050, 580));
        panSubDeposit.setLayout(new java.awt.GridBagLayout());

        tabClosingType.setMinimumSize(new java.awt.Dimension(1050, 300));
        tabClosingType.setPreferredSize(new java.awt.Dimension(1050, 300));

        panDepositDetails.setMinimumSize(new java.awt.Dimension(250, 300));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblPrincipal.setText("Principal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPrincipal, gridBagConstraints);

        lblPrincipalValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPrincipalValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPrincipalValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPrincipalValue, gridBagConstraints);

        lblPeriod.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriod, gridBagConstraints);

        lblPeriodValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPeriodValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblPeriodValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriodValue, gridBagConstraints);

        lblDepositDate.setText("Date of Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblDepositDate, gridBagConstraints);

        lblDepositDateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDepositDateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDepositDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblDepositDateValue, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDate, gridBagConstraints);

        lblMaturityDateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblMaturityDateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblMaturityDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDateValue, gridBagConstraints);

        lblRateOfInterest.setText("Rate Of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblRateOfInterest, gridBagConstraints);

        lblRateOfInterestValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblRateOfInterestValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblRateOfInterestValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblRateOfInterestValue, gridBagConstraints);

        lblMV.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMV, gridBagConstraints);

        lblMVvalue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblMVvalue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblMVvalue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMVvalue, gridBagConstraints);

        lblInterestPaymentFrequency.setText("Interest Payment Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequency, gridBagConstraints);

        lblInterestPaymentFrequencyValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblInterestPaymentFrequencyValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblInterestPaymentFrequencyValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequencyValue, gridBagConstraints);

        tabClosingType.addTab("Deposit Details", panDepositDetails);

        panNormalClosure.setMinimumSize(new java.awt.Dimension(300, 300));
        panNormalClosure.setPreferredSize(new java.awt.Dimension(300, 300));
        panNormalClosure.setLayout(new java.awt.GridBagLayout());

        lblBalanceDeposit.setText("Balance Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblBalanceDeposit, gridBagConstraints);

        lblBalanceDepositValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBalanceDepositValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBalanceDepositValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblBalanceDepositValue, gridBagConstraints);

        lblWithDrawnAmount.setText("WithDrawn Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblWithDrawnAmount, gridBagConstraints);

        lblWithDrawnAmountValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblWithDrawnAmountValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblWithDrawnAmountValue, gridBagConstraints);

        lblInterestCredited.setText("Interest Credited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInterestCredited, gridBagConstraints);

        lblInterestCreditedValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInterestCreditedValue, gridBagConstraints);

        lblInterestDrawn.setText("Interest Drawn");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInterestDrawn, gridBagConstraints);

        lblInterestDrawnValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInterestDrawnValue, gridBagConstraints);

        lblTDSCollected.setText("TDS Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblTDSCollected, gridBagConstraints);

        lblTDSCollectedValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblTDSCollectedValue, gridBagConstraints);

        lblLastInterestApplDate.setText("Last Interest Applied Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblLastInterestApplDate, gridBagConstraints);

        lblLastInterestApplDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblLastInterestApplDateValue, gridBagConstraints);

        lblLienFreezeAmount.setText("Lien/Freeze Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblLienFreezeAmount, gridBagConstraints);

        lblLienFreezeAmountValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblLienFreezeAmountValue, gridBagConstraints);

        lblInstPaid.setText("Number of Installments Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInstPaid, gridBagConstraints);

        lblInstPaidValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInstPaidValue, gridBagConstraints);

        lblInstDue.setText("Number of Installments Due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInstDue, gridBagConstraints);

        lblInstDueValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblInstDueValue, gridBagConstraints);

        lblBalance.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblBalance, gridBagConstraints);

        lblBalanceValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNormalClosure.add(lblBalanceValue, gridBagConstraints);

        tabClosingType.addTab("Present Position", panNormalClosure);

        panPartial.setLayout(new java.awt.GridBagLayout());

        panPartialWithDrawal.setMinimumSize(new java.awt.Dimension(350, 251));
        panPartialWithDrawal.setPreferredSize(new java.awt.Dimension(350, 252));
        panPartialWithDrawal.setLayout(new java.awt.GridBagLayout());

        lblUnitsAvailable.setText("No of Units Available");
        lblUnitsAvailable.setMaximumSize(new java.awt.Dimension(119, 16));
        lblUnitsAvailable.setMinimumSize(new java.awt.Dimension(119, 11));
        lblUnitsAvailable.setPreferredSize(new java.awt.Dimension(119, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPartialWithDrawal.add(lblUnitsAvailable, gridBagConstraints);

        lblUnitsAvailableValue.setMaximumSize(new java.awt.Dimension(100, 12));
        lblUnitsAvailableValue.setMinimumSize(new java.awt.Dimension(100, 14));
        lblUnitsAvailableValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPartialWithDrawal.add(lblUnitsAvailableValue, gridBagConstraints);

        lblUnitsDrawn.setText("No of Units Drawn");
        lblUnitsDrawn.setMaximumSize(new java.awt.Dimension(104, 16));
        lblUnitsDrawn.setMinimumSize(new java.awt.Dimension(104, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPartialWithDrawal.add(lblUnitsDrawn, gridBagConstraints);

        lblUnitsDrawnValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblUnitsDrawnValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblUnitsDrawnValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPartialWithDrawal.add(lblUnitsDrawnValue, gridBagConstraints);

        lblPrematureClosureRate.setText("Premature Closing Rate");
        lblPrematureClosureRate.setMaximumSize(new java.awt.Dimension(138, 16));
        lblPrematureClosureRate.setMinimumSize(new java.awt.Dimension(138, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPartialWithDrawal.add(lblPrematureClosureRate, gridBagConstraints);

        lblPrematureClosureRateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPrematureClosureRateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPrematureClosureRateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPartialWithDrawal.add(lblPrematureClosureRateValue, gridBagConstraints);

        lblPrematureClosurePeriod.setText("Premature Closing Period");
        lblPrematureClosurePeriod.setMinimumSize(new java.awt.Dimension(148, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPartialWithDrawal.add(lblPrematureClosurePeriod, gridBagConstraints);

        lblPrematureClosurePeriodValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPrematureClosurePeriodValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblPrematureClosurePeriodValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPartialWithDrawal.add(lblPrematureClosurePeriodValue, gridBagConstraints);

        lblAmountWithDrawn.setText("Amount Being WithDrawn");
        lblAmountWithDrawn.setMinimumSize(new java.awt.Dimension(149, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPartialWithDrawal.add(lblAmountWithDrawn, gridBagConstraints);

        txtAmountWithDrawn.setMinimumSize(new java.awt.Dimension(100, 20));
        txtAmountWithDrawn.setPreferredSize(new java.awt.Dimension(100, 18));
        txtAmountWithDrawn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountWithDrawnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPartialWithDrawal.add(txtAmountWithDrawn, gridBagConstraints);

        panProrataPosition.setBorder(javax.swing.BorderFactory.createTitledBorder("ProrataPosition"));
        panProrataPosition.setMinimumSize(new java.awt.Dimension(358, 100));
        panProrataPosition.setPreferredSize(new java.awt.Dimension(358, 135));
        panProrataPosition.setLayout(new java.awt.GridBagLayout());

        lblPresentUnitInt.setText("Present Interest On the Unit");
        lblPresentUnitInt.setMinimumSize(new java.awt.Dimension(162, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panProrataPosition.add(lblPresentUnitInt, gridBagConstraints);

        lblPresentUnitIntValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPresentUnitIntValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPresentUnitIntValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 1);
        panProrataPosition.add(lblPresentUnitIntValue, gridBagConstraints);

        lblSettlementUnitInt.setText("Settlement Interest On the Unit");
        lblSettlementUnitInt.setMinimumSize(new java.awt.Dimension(180, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panProrataPosition.add(lblSettlementUnitInt, gridBagConstraints);

        lblSettlementUnitIntValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblSettlementUnitIntValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblSettlementUnitIntValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 1);
        panProrataPosition.add(lblSettlementUnitIntValue, gridBagConstraints);

        lblDepositRunPeriod.setText("Deposit Run Period");
        lblDepositRunPeriod.setMinimumSize(new java.awt.Dimension(112, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panProrataPosition.add(lblDepositRunPeriod, gridBagConstraints);

        lblDepositRunPeriodValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDepositRunPeriodValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDepositRunPeriodValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 1);
        panProrataPosition.add(lblDepositRunPeriodValue, gridBagConstraints);

        lblNoOfUnits.setText("No Of Units");
        lblNoOfUnits.setMinimumSize(new java.awt.Dimension(67, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panProrataPosition.add(lblNoOfUnits, gridBagConstraints);

        txtNoOfUnits.setEditable(false);
        txtNoOfUnits.setMinimumSize(new java.awt.Dimension(100, 20));
        txtNoOfUnits.setPreferredSize(new java.awt.Dimension(100, 18));
        txtNoOfUnits.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 1);
        panProrataPosition.add(txtNoOfUnits, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panPartialWithDrawal.add(panProrataPosition, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPartial.add(panPartialWithDrawal, gridBagConstraints);

        panMainPW.setLayout(new java.awt.GridBagLayout());

        tblPartialWithdrawal.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPartialWithdrawal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartialWithdrawalMouseClicked(evt);
            }
        });
        srpPartialWithdrawal.setViewportView(tblPartialWithdrawal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMainPW.add(srpPartialWithdrawal, gridBagConstraints);

        btnPWNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnPWNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPWNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPWNewActionPerformed(evt);
            }
        });
        panPWButtons.add(btnPWNew);

        btnPWSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnPWSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPWSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPWSaveActionPerformed(evt);
            }
        });
        panPWButtons.add(btnPWSave);

        btnPWDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnPWDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPWDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPWDeleteActionPerformed(evt);
            }
        });
        panPWButtons.add(btnPWDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panMainPW.add(panPWButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPartial.add(panMainPW, gridBagConstraints);

        tabClosingType.addTab("Partial Withdrawal", panPartial);

        panClosingPosition.setMinimumSize(new java.awt.Dimension(1000, 300));
        panClosingPosition.setPreferredSize(new java.awt.Dimension(1000, 300));
        panClosingPosition.setRequestFocusEnabled(false);
        panClosingPosition.setLayout(new java.awt.GridBagLayout());

        lblInterestCr.setText("Interest Credited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 4);
        panClosingPosition.add(lblInterestCr, gridBagConstraints);

        lblInterestCrValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblInterestCrValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblInterestCrValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblInterestCrValue, gridBagConstraints);

        lblInterestDr.setText("Interest Debited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblInterestDr, gridBagConstraints);

        lblInterestDrValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblInterestDrValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblInterestDrValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblInterestDrValue, gridBagConstraints);

        lblClTDSCollected.setText("TDS Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblClTDSCollected, gridBagConstraints);

        lblClTDSCollectedValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblClTDSCollectedValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblClTDSCollectedValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblClTDSCollectedValue, gridBagConstraints);

        lblPayRec.setText("Payable/Receivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblPayRec, gridBagConstraints);

        lblPayRecValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPayRecValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPayRecValue.setPreferredSize(new java.awt.Dimension(100, 16));
        lblPayRecValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPayRecValueMouseClicked(evt);
            }
        });
        lblPayRecValue.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                lblPayRecValuePropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblPayRecValue, gridBagConstraints);

        lblClDisbursal.setText("Closing Disbursal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblClDisbursal, gridBagConstraints);

        lblClDisbursalValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblClDisbursalValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblClDisbursalValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblClDisbursalValue, gridBagConstraints);

        lblPreClosingPeriod.setText("Closing Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblPreClosingPeriod, gridBagConstraints);

        lblPreClosingPeriodValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPreClosingPeriodValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPreClosingPeriodValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblPreClosingPeriodValue, gridBagConstraints);

        lblPreClosingRate.setText("Closing Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblPreClosingRate, gridBagConstraints);

        lblPreClosingRateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPreClosingRateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPreClosingRateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblPreClosingRateValue, gridBagConstraints);

        lblRateApplicable.setText("Rate Applicable For The Period Run");
        lblRateApplicable.setMaximumSize(new java.awt.Dimension(180, 12));
        lblRateApplicable.setMinimumSize(new java.awt.Dimension(210, 12));
        lblRateApplicable.setPreferredSize(new java.awt.Dimension(350, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panClosingPosition.add(lblRateApplicable, gridBagConstraints);

        lblRateApplicableValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblRateApplicableValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblRateApplicableValue.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblRateApplicableValue, gridBagConstraints);

        lblPenaltyPenalRate.setText("Penalty Penal Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblPenaltyPenalRate, gridBagConstraints);

        lblPenaltyPenalRateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPenaltyPenalRateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPenaltyPenalRateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblPenaltyPenalRateValue, gridBagConstraints);

        lblActualPeriodRun.setText("Actual Period Run");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblActualPeriodRun, gridBagConstraints);

        lblActualPeriodRunValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblActualPeriodRunValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblActualPeriodRunValue.setPreferredSize(new java.awt.Dimension(165, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblActualPeriodRunValue, gridBagConstraints);

        lblDelayedInstallments.setText("Delayed Installments");
        lblDelayedInstallments.setMaximumSize(new java.awt.Dimension(122, 18));
        lblDelayedInstallments.setMinimumSize(new java.awt.Dimension(122, 18));
        lblDelayedInstallments.setPreferredSize(new java.awt.Dimension(122, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblDelayedInstallments, gridBagConstraints);

        lblDelayedInstallmentValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDelayedInstallmentValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDelayedInstallmentValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblDelayedInstallmentValue, gridBagConstraints);

        lblDelayedAmount.setText("Delayed Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblDelayedAmount, gridBagConstraints);

        lblDelayedAmountValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDelayedAmountValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDelayedAmountValue.setPreferredSize(new java.awt.Dimension(100, 16));
        lblDelayedAmountValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDelayedAmountValueMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblDelayedAmountValue, gridBagConstraints);

        lblDeathClaim.setText("Death Claim Applied");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panClosingPosition.add(lblDeathClaim, gridBagConstraints);

        lblDeathClaimInterest.setText("Death Claim Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblDeathClaimInterest, gridBagConstraints);

        lblDeathClaimInterestValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDeathClaimInterestValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDeathClaimInterestValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblDeathClaimInterestValue, gridBagConstraints);

        lblDeathClaimValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblDeathClaimValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDeathClaimValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblDeathClaimValue, gridBagConstraints);

        lblAgentCommisionRecovered.setText("Commision Recovered Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblAgentCommisionRecovered, gridBagConstraints);

        lblAgentCommisionRecoveredValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblAgentCommisionRecoveredValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAgentCommisionRecoveredValue.setPreferredSize(new java.awt.Dimension(165, 16));
        lblAgentCommisionRecoveredValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAgentCommisionRecoveredValueMouseClicked(evt);
            }
        });
        lblAgentCommisionRecoveredValue.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                lblAgentCommisionRecoveredValuePropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblAgentCommisionRecoveredValue, gridBagConstraints);

        lblPreIntPayable.setText("Previous Interest Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panClosingPosition.add(lblPreIntPayable, gridBagConstraints);

        lblPreIntValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPreIntValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPreIntValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 2);
        panClosingPosition.add(lblPreIntValue, gridBagConstraints);

        lblAdditionalIntrstHed.setText("Ad Int Det:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 4);
        panClosingPosition.add(lblAdditionalIntrstHed, gridBagConstraints);

        lblPeriodOfMaturity.setText("Pd Run Frm Dt of Mtrty.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panClosingPosition.add(lblPeriodOfMaturity, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        panClosingPosition.add(lblMaturityPeriod, gridBagConstraints);

        lblAddIntrstRate.setText("Ad Int Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panClosingPosition.add(lblAddIntrstRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        panClosingPosition.add(lblAddIntrstRteVal, gridBagConstraints);

        lblAddIntRteAmt.setText("Ad Int Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panClosingPosition.add(lblAddIntRteAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        panClosingPosition.add(lblAddIntRtAmtVal, gridBagConstraints);

        tabClosingType.addTab("Closing Position", panClosingPosition);

        panTransaction.setPreferredSize(new java.awt.Dimension(800, 380));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        tabClosingType.addTab("Transaction", panTransaction);

        chargePan.setPreferredSize(new java.awt.Dimension(800, 380));
        chargePan.setLayout(new java.awt.GridBagLayout());

        panChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 9;
        chargePan.add(panChargeDetails, gridBagConstraints);

        servicTaxPanel.setMaximumSize(new java.awt.Dimension(275, 30));
        servicTaxPanel.setMinimumSize(new java.awt.Dimension(275, 30));
        servicTaxPanel.setPreferredSize(new java.awt.Dimension(275, 30));
        servicTaxPanel.setLayout(new java.awt.GridBagLayout());

        lblServiceTax.setText("ServiceTax");
        lblServiceTax.setMaximumSize(new java.awt.Dimension(100, 20));
        lblServiceTax.setMinimumSize(new java.awt.Dimension(100, 20));
        lblServiceTax.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 16);
        servicTaxPanel.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(100, 20));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(100, 20));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        servicTaxPanel.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        chargePan.add(servicTaxPanel, gridBagConstraints);

        btnApplyPenal.setText("Apply Penal");
        btnApplyPenal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyPenalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        chargePan.add(btnApplyPenal, gridBagConstraints);

        tabClosingType.addTab("Charge Details", chargePan);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubDeposit.add(tabClosingType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSubDepositInfo.add(panSubDeposit, gridBagConstraints);

        panButtons.setMinimumSize(new java.awt.Dimension(467, 35));
        panButtons.setPreferredSize(new java.awt.Dimension(5, 5));

        lblRateApplicble.setText("Penalty Rate Applicable");
        panButtons.add(lblRateApplicble);

        rdoPenaltyRateApplicbleYes.setText("Yes");
        rdoPenaltyRateApplicbleYes.setMaximumSize(new java.awt.Dimension(21, 18));
        rdoPenaltyRateApplicbleYes.setMinimumSize(new java.awt.Dimension(21, 18));
        rdoPenaltyRateApplicbleYes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoPenaltyRateApplicbleYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyRateApplicbleYesActionPerformed(evt);
            }
        });
        panButtons.add(rdoPenaltyRateApplicbleYes);

        rdoPenaltyRateApplicbleNo.setText("No");
        rdoPenaltyRateApplicbleNo.setMaximumSize(new java.awt.Dimension(41, 18));
        rdoPenaltyRateApplicbleNo.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoPenaltyRateApplicbleNo.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoPenaltyRateApplicbleNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyRateApplicbleNoActionPerformed(evt);
            }
        });
        panButtons.add(rdoPenaltyRateApplicbleNo);

        btnTableOk.setText("Ok");
        btnTableOk.setMaximumSize(new java.awt.Dimension(73, 28));
        btnTableOk.setMinimumSize(new java.awt.Dimension(73, 28));
        btnTableOk.setPreferredSize(new java.awt.Dimension(73, 28));
        btnTableOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableOkActionPerformed(evt);
            }
        });
        panButtons.add(btnTableOk);

        btnTableCancel.setText("Cancel");
        btnTableCancel.setMaximumSize(new java.awt.Dimension(77, 28));
        btnTableCancel.setMinimumSize(new java.awt.Dimension(77, 28));
        btnTableCancel.setPreferredSize(new java.awt.Dimension(75, 28));
        btnTableCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableCancelActionPerformed(evt);
            }
        });
        panButtons.add(btnTableCancel);

        lblClosureType.setText("Closure Type");
        panButtons.add(lblClosureType);
        panButtons.add(lblClosureTypeValue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panSubDepositInfo.add(panButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panSubDepositInfo, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblPayRecValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPayRecValueMouseClicked
        // TODO add your handling code here:
       	//Added By Chithra		  
	if(observable.getDepositPeriodWK()!=null && CommonUtil.convertObjToDouble(observable.getDepositPeriodWK())>0){
        }else{
              calculateToleranceAmt();
        }
       
    }//GEN-LAST:event_lblPayRecValueMouseClicked
private void editCommisionAmount(){
    
         amountMap=new HashMap();
       if(CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)){
            String tolerance_amt=CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
            if(tolerance_amt.length()==0)
            {
                ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                return;
            }
            String selectedAmt=CommonUtil.convertObjToStr(lblAgentCommisionRecoveredValue.getText());
            
            selectedAmt=selectedAmt.replaceAll(",", "");
            amountMap.put("TOLERANCE_AMT",CommonConstants.TOLERANCE_AMT);
            amountMap.put( "SELECTED_AMT",selectedAmt);
            amountMap.put( "TITLE",lblAgentCommisionRecovered.getText());
            amountMap.put("CALCULATED_AMT",observable.getAgentCommisionRecoveredValue());
            
            System.out.println("Commision Amounta "+amountMap);
             TextUI textUI =new TextUI(this,this, amountMap);
             
        }
    
}
    private void calculateToleranceAmt(){
     amountMap=new HashMap();
       if(CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)){
            String tolerance_amt=CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
            if(tolerance_amt.length()==0)
            {
                ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                return;
            }
            String selectedAmt=CommonUtil.convertObjToStr(lblPayRecValue.getText());
            
            selectedAmt=selectedAmt.replaceAll(",", "");
            amountMap.put("TOLERANCE_AMT",CommonConstants.TOLERANCE_AMT);
            amountMap.put( "SELECTED_AMT",selectedAmt);
            amountMap.put( "TITLE",lblPayRec.getText());
            amountMap.put("CALCULATED_AMT",observable.getPermanentPayReceivable());
            
            System.out.println("amountMap####"+amountMap);
             TextUI textUI =new TextUI(this,this, amountMap);
             
        }
        
    }
    
    public void modifyTransData(Object objData){
        TextUI obj=(TextUI)objData;
         String enteredData =obj.getTxtData();
         System.out.println("amountMap"+amountMap);
         if(amountMap.containsKey("TITLE") && amountMap.get("TITLE")!= null && amountMap.get("TITLE").equals("Commision Recovered Amount")){
             lblAgentCommisionRecoveredValue.setText( String.valueOf(enteredData));
             observable.setAgentCommisionRecoveredValue(enteredData);
         }else if(amountMap.containsKey("TITLE") && amountMap.get("TITLE")!= null && amountMap.get("TITLE").equals("Delayed Amount")){  // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation. 
             lblDelayedAmountValue.setText(String.valueOf(enteredData));
             observable.setChargeAmount(enteredData);
         }else{
        lblPayRecValue.setText( String.valueOf(enteredData));
        System.out.println("123");
        lblInterestDrValue.setText( String.valueOf(enteredData));
        observable.setPayReceivable(enteredData);
        if(behavesLike!=null && behavesLike.equals("RECURRING")){
        	observable.setClosingIntDb(enteredData);
        }
        if(lblPayRec.getText().equals("Payable")){
            String deposit_amt= lblPrincipalValue.getText();
            String prevInt="0";
            if(lblPreIntValue.getText()!=null && !(lblPreIntValue.getText().equals(""))){
                 prevInt=lblPreIntValue.getText();
            }else{
                prevInt="0";
            }
            lblClDisbursalValue.setText(String.valueOf(Double.parseDouble(deposit_amt)+Double.parseDouble(enteredData)+Double.parseDouble(prevInt)));
            observable.setClosingDisbursal(lblClDisbursalValue.getText());
            lblClDisbursalValue.setText(lblClDisbursalValue.getText());
            System.out.println("amtStr3"+lblClDisbursalValue.getText());
           
            transactionUI.setCallingAmount(lblClDisbursalValue.getText());
            
        }else if(lblPayRec.getText().equals("Receivable")){
            
            String deposit_amt= lblPrincipalValue.getText();
            lblClDisbursalValue.setText(String.valueOf(Double.parseDouble(deposit_amt)-Double.parseDouble(enteredData)));
            observable.setClosingDisbursal(lblClDisbursalValue.getText());
            lblClDisbursalValue.setText(lblClDisbursalValue.getText());
            System.out.println("amtStr4"+lblClDisbursalValue.getText());
            transactionUI.setCallingAmount(lblClDisbursalValue.getText());
        }
         }
         amountMap.clear();
        
    }
    private void rdoTransfer_In_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransfer_In_NoActionPerformed
        // TODO add your handling code here:
        if(rdoRegularDeposit.isSelected() == true && rdoTransfer_In_No.isSelected() == true){
            downCancel();
            rdoTransfer_Out_Yes.setSelected(false);
            rdoTransfer_In_No.setSelected(true);
            txtTransferringBranch.setText("");
            lblBranchValue.setText("");
            lblTransferringBranch.setVisible(false);
            panTransferringBranch.setVisible(false);
            panTransferringBranch.setEnabled(false);     
            lblBranch.setVisible(false);
            lblBranchValue.setVisible(false);
            btnTransferringBranch.setEnabled(false);
            lblRateApplicble.setVisible(true);
            rdoPenaltyRateApplicbleYes.setVisible(true);
            rdoPenaltyRateApplicbleNo.setVisible(true);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                viewType = DEPOSIT_ACCT;
                observable.setViewTypeDet(viewType);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                viewType = CLOSED_ACCT;
                observable.setViewTypeDet(viewType);
            }
            lblClosureTypeValue.setText("");
            calculateMaturity();//new mode setting closingtype...
        }
    }//GEN-LAST:event_rdoTransfer_In_NoActionPerformed

    private void btnTransferringBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferringBranchActionPerformed
        // TODO add your handling code here:
        callView(TRANSFERRING_BRANCH);
    }//GEN-LAST:event_btnTransferringBranchActionPerformed

    private void rdoTransfer_Out_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransfer_Out_YesActionPerformed
        // TODO add your handling code here:
        if(!behavesLike.equals("") && behavesLike.equals("DAILY")){
            ClientUtil.showMessageWindow("Daiy Deposit can not TransferOut");
            rdoTransfer_In_No.setSelected(true);            
            return;
        }else if(rdoRegularDeposit.isSelected() == true && rdoTransfer_Out_Yes.isSelected() == true){
            downCancel();
            rdoTransfer_In_No.setSelected(false);
            rdoTransfer_Out_Yes.setSelected(true);
            txtTransferringBranch.setText("");
            lblBranchValue.setText("");
            lblTransferringBranch.setVisible(true);
            panTransferringBranch.setVisible(true);
            panTransferringBranch.setEnabled(true);
            lblBranch.setVisible(true);
            lblBranchValue.setVisible(true);
            btnTransferringBranch.setEnabled(true);
            lblRateApplicble.setVisible(false);
            rdoPenaltyRateApplicbleYes.setVisible(false);
            rdoPenaltyRateApplicbleNo.setVisible(false);
            lblClosureTypeValue.setText("");
            calculateMaturity();//new mode setting closingtype...
        }else if(rdoLTDDeposit.isSelected() == true && rdoTransfer_Out_Yes.isSelected() == true){
            ClientUtil.showMessageWindow("Loan Exists can not TransferOut");
            rdoTransfer_In_No.setSelected(true);            
            return;
        }

    }//GEN-LAST:event_rdoTransfer_Out_YesActionPerformed

    private void txtDepositNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositNoFocusLost
        // TODO add your handling code here:
         String txtDeposit = CommonUtil.convertObjToStr(txtDepositNo.getText());  
         System.out.println("txtDeposit%$%$"+txtDeposit);
         if(txtDeposit.length()>0){         
           txtDeposit= observable.checkAcNoWithoutProdType(txtDeposit);
           txtDepositNo.setText(txtDeposit);
           if(txtDeposit.length()>0 ){
               if(observable.getTypeOfDep()!=null && observable.getTypeOfDep().equals("LTD"))
                rdoRegularDeposit.setSelected(true);
               else 
                rdoLTDDeposit.setSelected(true);
           }
        if(rdoRegularDeposit.isSelected() == true || rdoLTDDeposit.isSelected() == true){
            txtDeposit = txtDepositNo.getText();         //        TODO add your handling code here:
            HashMap txtDepositNum=new HashMap();
            txtDepositNum.put("DEPOSIT_ACT_NUM",txtDeposit);
            txtDepositNum.put("PROD_ID",observable.getProdID());
            viewType = DEPOSIT_ACCT;
            fillData(txtDepositNum);
        }else{
            ClientUtil.displayAlert("Choose Types of Deposit");
            txtDepositNo.setText("");
        }
        }
         rdoPenaltyRateApplicbleYes.setSelected(true);
    }//GEN-LAST:event_txtDepositNoFocusLost

    private void rdoLTDDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLTDDepositActionPerformed
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && rdoLTDDeposit.isSelected() == true){        // TODO add your handling code here:
            rdoRegularDeposit.setSelected(false);
            rdoRegularDeposit.setEnabled(false);
            rdoLTDDeposit.setSelected(true);
            observable.setRdoTypeOfDeposit_No(true);
            observable.setRdoTypeOfDeposit_Yes(false);
            callView(LTD_DEP_LIST);     
            rdoTransfer_In_No.setSelected(true);
            txtTransferringBranch.setText("");
            lblBranchValue.setText("");
            lblTransferringBranch.setVisible(false);
            panTransferringBranch.setVisible(false);
            panTransferringBranch.setEnabled(false);
            lblBranch.setVisible(false);
            lblBranchValue.setVisible(false);
            btnTransferringBranch.setEnabled(false);
            lblRateApplicble.setVisible(true);
            rdoPenaltyRateApplicbleYes.setVisible(true);
            rdoPenaltyRateApplicbleNo.setVisible(true);
            observable.setLtdDeposit("true");
       }        
    }//GEN-LAST:event_rdoLTDDepositActionPerformed
    
    /*private String serviceTaxAmount(String desc) {//Commemted by nithya for rewriting : For Service tax to GST conversion
        String retStr = "";
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            String scheme = CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", scheme);
            whereMap.put("CHARGE_DESC", desc);
            List resultList = ClientUtil.executeQuery("getCheckServiceTaxApplicable", whereMap);
            HashMap checkMap = new HashMap();
            if (resultList != null && resultList.size() > 0) {
                checkMap = (HashMap) resultList.get(0);
                if (checkMap != null && checkMap.containsKey("SERVICE_TAX_APPLICABLE")) {
                    retStr = CommonUtil.convertObjToStr(checkMap.get("SERVICE_TAX_APPLICABLE"));
                }
            }
        }
        return retStr;
    }*/
    
    private HashMap serviceTaxAmount(String desc) {// Function modified by nithya 
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            String scheme = CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", scheme);
            whereMap.put("CHARGE_DESC", desc);
            String retStr = "";
            List resultList = ClientUtil.executeQuery("getCheckServiceTaxApplicable", whereMap);
            HashMap checkMap = new HashMap();
            if (resultList != null && resultList.size() > 0) {
                checkMap = (HashMap) resultList.get(0);
                if (checkMap != null && checkMap.containsKey("SERVICE_TAX_APPLICABLE") && checkMap.containsKey("SERVICE_TAX_ID")) {
                    retStr = CommonUtil.convertObjToStr(checkMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE", checkMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID", checkMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
    
    //Added By Nidhin 2-06-2014
        public void calculateTot() {
            taxSettingsListForCharges = new ArrayList();
            double totCharge =0.0,taxAmt=0.0;
            tableCharge = 0.0;
            table.revalidate();
        //        totCharge = accountClosingCharge;   // PRODUCT LEVEL ACC_CLOSING CHARGE
        //Commenting and rewriting the code for Service tax to GST conversion - Start
//        for (int i = 0; i < table.getRowCount(); i++) {
//            if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
//                totCharge = totCharge + CommonUtil.convertObjToDouble(table.getValueAt(i, 2).toString()).doubleValue();
//                System.out.println("table.getValueAt(i, 1)"+table.getValueAt(i, 1));
//                String val= serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 1))); 
//               if(val!=null&&val.equals("Y")){
//                    taxAmt=taxAmt+CommonUtil.convertObjToDouble(table.getValueAt(i, 2));
//                }
//            }
//        }
         List taxSettingsList = new ArrayList(); 
         HashMap checkForTaxMap = new HashMap();
         for (int i = 0; i < table.getRowCount(); i++) {
             if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
                 double chrgamt = 0;
                 totCharge = totCharge + CommonUtil.convertObjToDouble(table.getValueAt(i, 2).toString()).doubleValue();
                 checkForTaxMap = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 1)));
                 // String val = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
                 if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                     if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                         HashMap serviceTaSettingsMap = new HashMap();
                         chrgamt = CommonUtil.convertObjToDouble(table.getValueAt(i, 2));
                         if (chrgamt > 0) {
                             serviceTaSettingsMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                             serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                             taxSettingsListForCharges.add(serviceTaSettingsMap);
                         }
                     }
                 }
             }
         }        
        // End
        tableCharge = totCharge;
        getTotalRenewalAmt();
        updateOBFields();
        table.setRowSelectionAllowed(false);
        // commented the below code block by nithya on 16-06-2018 for calculating ServiceTax/GST implementation for charges and agent commission
		//added by chithra for service Tax
//        double serviceTaxAmt=0;
//       // if (taxAmt > 0) {
//        if(taxSettingsList != null && taxSettingsList.size() > 0){
//            HashMap ser_Tax_Val = new HashMap();
//            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
//            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CurrencyValidation.formatCrore(String.valueOf(taxAmt)));
//            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
//            try {
//                objServiceTax = new ServiceTaxCalculation();
//                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
//                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
//                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                    
////                    serviceTaxAmt=CommonUtil.convertObjToDouble(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
////                    lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
////                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                    
//                      serviceTaxAmt=CommonUtil.convertObjToDouble(amt);
//                      lblServiceTaxval.setText(amt);
//                      serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);                    
//                    System.out.println("calling amount :: "+ CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()));
//                    double transAmt=CommonUtil.convertObjToDouble(transactionUI.getCallingAmount())-CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
//                    transactionUI.setCallingAmount(CommonUtil.convertObjToStr(transAmt));
//                    lblClDisbursalValue.setText(CommonUtil.convertObjToStr(transAmt));
//                    System.out.println("transAmt :: "+ transAmt);
//                    System.out.println("getClosingDisbursal :: "+ observable.getClosingDisbursal());
//                } else {
//                    lblServiceTaxval.setText("0.00");
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }
//        else{
//             lblServiceTaxval.setText("0.00");
//        }
       
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                calculateServiceTaxAmt();
            }
        //
        // Settings table focus to false completely remove selection
        // capability from the table component.
        //
        table.setFocusable(false);

    }
    private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        observable.setTotalCharge(0);
        double totalClosingCharge = 0;
        System.out.println("chargelst@!#!@#!@"+chargelst);
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String desc = "";
                chargeMap = (HashMap) chargelst.get(i);
                if(chargeMap != null && chargeMap.containsKey("CHARGE_DESC")){  
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                //System.out.println("$#@@$ accHead" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    } else {
                        if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc) && CommonUtil.convertObjToStr(table.getValueAt(j, 4)).equals("Y")) {
                            String chargeAmt = CommonUtil.convertObjToStr(table.getValueAt(j, 2));
                            chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                        }
                        totalClosingCharge = totalClosingCharge+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                    }
                }
            }
            }
            observable.setTotalCharge(totalClosingCharge);
            System.out.println("setTotalCharge.setTotalCharge()"+observable.getTotalCharge());
            observable.setChargelst(chargelst);
        }

    }
    public void accClosingCharges() {
        if (tableFlag == true) {
            System.out.println("observable.getActionType()"+observable.getActionType());
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                calculateTot();
                //this fn add charges
            }
            srpChargeDetails.setEnabled(true);
            table.setEnabled(true);
        }

    }
    
    private void tableExsistOrNot() {
        System.out.println("sssss"+behavesLike);
        System.out.println("transNew"+transNew);
        if (behavesLike.equals("DAILY") || behavesLike.equals("RECURRING")) {
            if (transNew) {
                tabClosingType.add(chargePan);
                chargePan.setName("Charge Details");
                createChargeTable(); //bb
                // System.out.println("tableFlag =33========"+tableFlag);
                if (tableFlag) {
                    editChargeTable();
                chargeAmount();
                }
            }
        }else{
            tabClosingType.remove(chargePan);
        }
    }
        private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }
        private void chargeAmount() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        if (transNew) {
            System.out.println("inside chargeamount opening");
            appraiserMap.put("DEDUCTION_ACCU", "C");
        } else {
            System.out.println("inside chargeamount closing");
           // appraiserMap.put("DEDUCTION_ACCU", "C");//20-04-2014
            appraiserMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
            
        }
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                double depositAmount = CommonUtil.convertObjToDouble(lblBalanceValue.getText());
                System.out.println("$#@@$ depositAmount" + depositAmount);
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                                chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue();
                                chargeAmt = chargeAmt*depositAmount/100;
                                long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                                if (roundOffType != 0) {
                                    chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                                }
                                double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                                double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                                if (chargeAmt < minAmt) {
                                    chargeAmt = minAmt;
                                }
                                if (chargeAmt > maxAmt) {
                                    chargeAmt = maxAmt;
                                }
                                table.setValueAt(String.valueOf(chargeAmt), j, 2);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = 0;
                            if (chargeslabLst != null && chargeslabLst.size() > 0) {
                                double minAmt = 0;
                                double maxAmt = 0;
                                for (int k = 0; k < chargeslabLst.size(); k++) {
                                    LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);

                                    double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
                                    double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
                                    if (limit >= minAmtRange && limit <= maxAmtRange) {
                                        double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
                                        minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
                                        maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();

//                                        chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() * chargeRate / 100;
                                        if (chargeAmt < minAmt) {
                                            chargeAmt = minAmt;
                                        }
                                        if (chargeAmt > maxAmt) {
                                            chargeAmt = maxAmt;
                                        }
                                        break;
                                    }
                                }

                            }


                            table.setValueAt(String.valueOf(chargeAmt), j, 2);
//                            chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() *
//                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
//                            table.setValueAt(String.valueOf(chargeAmt),j, 2);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                }
            }
            //System.out.println("#$#$$# chargeMap:" + chargeMap);
            //System.out.println("#$#$$# chargelst:" + chargelst);
            table.revalidate();
            table.updateUI();
            //            observable.setChargelst(chargelst);
            System.out.println("#$#$$# chargelst@3333333333333333333333 :" + chargelst);
        }

    }
    
    private void rdoRegularDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRegularDepositActionPerformed
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && rdoRegularDeposit.isSelected() == true){        // TODO add your handling code here:
            rdoRegularDeposit.setSelected(true);
            rdoLTDDeposit.setSelected(false);
            rdoLTDDeposit.setEnabled(false);
            observable.setRdoTypeOfDeposit_No(false);
            observable.setRdoTypeOfDeposit_Yes(true);
            callView(DEPOSIT_ACCT); 
        }
    }//GEN-LAST:event_rdoRegularDepositActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void rdoPenaltyRateApplicbleNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyRateApplicbleNoActionPerformed
        observable.setRdoPenaltyPenalRate_no(rdoPenaltyRateApplicbleNo.isSelected());        // TODO add your handling code here:
        observable.setRdoPenaltyPenalRate_yes(rdoPenaltyRateApplicbleYes.isSelected());
        if(rdoPenaltyRateApplicbleNo.isSelected()){
            observable.setRdoNoButton(true);
            observable.setRdoYesButton(false);
        }
    }//GEN-LAST:event_rdoPenaltyRateApplicbleNoActionPerformed
    
    private void rdoPenaltyRateApplicbleYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyRateApplicbleYesActionPerformed
        observable.setRdoPenaltyPenalRate_no(rdoPenaltyRateApplicbleNo.isSelected());
        observable.setRdoPenaltyPenalRate_yes(rdoPenaltyRateApplicbleYes.isSelected());        // TODO add your handling code here:
        if(rdoPenaltyRateApplicbleYes.isSelected()){
            observable.setRdoYesButton(true);
            observable.setRdoNoButton(false);
        }
    }//GEN-LAST:event_rdoPenaltyRateApplicbleYesActionPerformed
    
    private void txtDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositNoActionPerformed

    }//GEN-LAST:event_txtDepositNoActionPerformed
    
    private void tblPartialWithdrawalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartialWithdrawalMouseClicked
        // Add your handling code here:
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(this.panPartialWithDrawal,true);
            this.pwButtonsEnable();
        }
        updationPW();
        txtAmountWithDrawnFocusLost(null);
    }//GEN-LAST:event_tblPartialWithdrawalMouseClicked
    private void updationPW() {
        observable.populatePWTableRow(this.tblPartialWithdrawal.getSelectedRow());
        populatePWDetail();
        this.partialWithdrawalNew = false;
        observable.setWithdrawalTOStatus(CommonConstants.STATUS_MODIFIED);
    }
    private void populatePWDetail(){
        this.txtAmountWithDrawn.setText(observable.getTxtAmtWithDrawn());
        this.txtNoOfUnits.setText(observable.getTxtPWNoOfUnits());
    }
    
    private void btnPWDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWDeleteActionPerformed
        // Add your handling code here:
        observable.setWithdrawalTOStatus(CommonConstants.STATUS_DELETED);
        observable.deletePWData(this.tblPartialWithdrawal.getSelectedRow());
        this.updatePWTable();
        this.updateBalance(observable.getTxtAmtWithDrawn(),false);
        
        this.setUpPWPan(false);
        /*
        ClientUtil.clearAll(this.panPartialWithDrawal);
        ClientUtil.enableDisable(this.panPartialWithDrawal,false);
        //this.panPartialWithDrawal.setEnabled(false);
        this.pwButtonsDisable();
        this.btnPWNew.setEnabled(true);*/
    }//GEN-LAST:event_btnPWDeleteActionPerformed
    private void updatePWTable(){
        this.tblPartialWithdrawal.setModel(observable.getTbmPartialWithdrawal());
        this.tblPartialWithdrawal.revalidate();
    }
    private boolean checkZeroAmount(double amt){
        if(amt==0)
            return true;
        return false;
    }
    private void btnPWSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWSaveActionPerformed
        // Add your handling code here:
        String amount =  this.txtAmountWithDrawn.getText();
        if(amount!=null && amount.length()>0) {
            Double amt = CommonUtil.convertObjToDouble(amount);
            if(amt!=null){
                if(!this.checkValidWithDrawnAmount(amt.doubleValue())) {
                    COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_WITHDRAWALAMT_INVALIDMUL")+" "+String.valueOf(observable.getUnitAmt())+"!!!");
                    return;
                }
                if(this.checkZeroAmount(amt.doubleValue())) {
                    COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_WITHDRAWALAMT_ZERO"));
                    return;
                }
                if(!this.partialWithdrawalNew){
                    if(this.tblPartialWithdrawal.getSelectedRow()!=-1) {
                        ArrayList arr =((TableModel)tblPartialWithdrawal.getModel()).getRow(this.tblPartialWithdrawal.getSelectedRow());
                        this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)),false);
                    }
                }
                if(!this.checkAmountGreater(amt.doubleValue())) {
                    COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_WITHDRAWALAMT_GREATER"));
                    if(!this.partialWithdrawalNew){
                        if(this.tblPartialWithdrawal.getSelectedRow()!=-1) {
                            ArrayList arr =((TableModel)tblPartialWithdrawal.getModel()).getRow(this.tblPartialWithdrawal.getSelectedRow());
                            this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)),false);
                        }
                    }
                    return;
                }
            }
            this.txtNoOfUnits.setText(String.valueOf(amt.doubleValue()/observable.getUnitAmt()));
        } else {
            COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_CLOSURETYPE_NULL"));
            return;
        }
        this.updateOBPartialWithDrawal();
        if(!this.partialWithdrawalNew)
            observable.insertPWData(this.tblPartialWithdrawal.getSelectedRow());
        else
            observable.insertPWData(-1);
        this.updatePWTable();
        this.updateBalance(this.txtAmountWithDrawn.getText(),true);
        this.setUpPWPan(false);
        
        /*ClientUtil.enableDisable(this.panPartialWithDrawal,false);
        ClientUtil.clearAll(this.panPartialWithDrawal);
        this.pwButtonsDisable();
        this.btnPWNew.setEnabled(true);*/
    }//GEN-LAST:event_btnPWSaveActionPerformed
    
    private void btnPWNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWNewActionPerformed
        // Add your handling code here:
        this.setUpPWPan(true);
        /*
        ClientUtil.enableDisable(this.panPartialWithDrawal,true);
        ClientUtil.clearAll(this.panPartialWithDrawal);
        this.pwButtonsEnable();
        this.btnPWDelete.setEnabled(false);   */
        this.partialWithdrawalNew = true;
        observable.setWithdrawalTOStatus(CommonConstants.STATUS_CREATED);
    }//GEN-LAST:event_btnPWNewActionPerformed
    
    private void txtAmountWithDrawnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountWithDrawnFocusLost
        
        String amt = this.txtAmountWithDrawn.getText();
        if(amt!=null && amt.length()>0) {
            double amount = CommonUtil.convertObjToDouble(amt).doubleValue();
            double totalUnit = CommonUtil.convertObjToDouble(this.lblUnitsAvailableValue.getText()).doubleValue()+
            CommonUtil.convertObjToDouble(this.lblUnitsDrawnValue.getText()).doubleValue();
            double unitInt = (CommonUtil.convertObjToDouble(lblInterestCreditedValue.getText()).doubleValue()/totalUnit)*(amount/observable.getUnitAmt());
            double prematureInterest = (CommonUtil.convertObjToDouble(lblPrematureClosureRateValue.getText()).doubleValue()/100)*amount;
            Date lastDate = (Date)DateUtil.getDateMMDDYYYY(observable.getLastIntAppDate());
            lblLastInterestApplDateValue.setText(CommonUtil.convertObjToStr(lastDate));
            this.lblPresentUnitIntValue.setText(String.valueOf(unitInt));
            this.lblSettlementUnitIntValue.setText(String.valueOf(unitInt-prematureInterest));
            
        }
    }//GEN-LAST:event_txtAmountWithDrawnFocusLost
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
    
    private boolean checkValidWithDrawnAmount(double amount){
        if((amount%observable.getUnitAmt())!=0.0)
            return false;
        return true;
    }
    private boolean checkAmountGreater(double amount){
        double unitsAvailable=0;
        Double avaiUnits = CommonUtil.convertObjToDouble(this.lblUnitsAvailableValue.getText());
        if(avaiUnits!=null)
            unitsAvailable = avaiUnits.doubleValue();
        if(amount>(unitsAvailable*observable.getUnitAmt()))
            return false;
        return true;
    }
    private void pwButtonsDisable(){
        this.btnPWDelete.setEnabled(true);
        this.btnPWNew.setEnabled(false);
        this.btnPWSave.setEnabled(false);
    }
    private void pwButtonsEnable(){
        this.btnPWDelete.setEnabled(true);
        this.btnPWNew.setEnabled(true);
        this.btnPWSave.setEnabled(true);
    }
    private void updatePartialWithDrawal(){
        this.lblPrematureClosurePeriodValue.setText(observable.getPrematureClosingDate());
        this.lblPrematureClosureRateValue.setText(observable.getPrematureClosingRate());
        this.lblUnitsAvailableValue.setText(observable.getNoOfUnitsAvai());
        this.lblUnitsDrawnValue.setText(observable.getNoOfUnitsWithDrawn());
        this.lblSettlementUnitIntValue.setText(observable.getSettlementUnitInt());
        this.lblPresentUnitIntValue.setText(observable.getPresentUnitInt());
        this.lblDepositRunPeriodValue.setText(observable.getDepositRunPeriod());
        
        this.tblPartialWithdrawal.setModel(observable.getTbmPartialWithdrawal());
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        this.btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    public void authorizeStatus(String auth)
    {
       updateAuthorizeStatus(auth);  
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnView.setEnabled(false);
        //removeEditLockScreen(txtDepositNo.getText());
    }//GEN-LAST:event_btnRejectActionPerformed
    private void ltdClosingRecords(HashMap balanceMap,String status){
        HashMap lienMap = new HashMap();
        HashMap authMap = new HashMap();
        HashMap headMap = new HashMap();
        HashMap accHeadMap = new HashMap();
        //                        HashMap depositMap = new HashMap();
        //                        depositMap.put("DEPOSIT_NO",txtDepositNo.getText());
        //                        List depList = ClientUtil.executeQuery("getLoanDepNo", depositMap);
        //                        if(depList.size()>0){
        //                            depositMap = (HashMap)depList.get(0);
        //                            headMap.put("LIEN_AC_NO",balanceMap.get("LIEN_AC_NO"));
        accHeadMap.put("DEPOSIT_NO",txtDepositNo.getText());
        List lst = ClientUtil.executeQuery("getAccountHeadForLTD",accHeadMap);
        if(lst != null && lst.size()>0){
            accHeadMap = (HashMap)lst.get(0);
            if(accHeadMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS")){
                status = CommonUtil.convertObjToStr("LIEN");
                lienMap.put("DEPOSIT_NO",txtDepositNo.getText());
                lienMap.put("PROD_ID",balanceMap.get("PROD_ID"));
                lienMap.put("SHADOW_LIEN",balanceMap.get("SHADOW_LIEN"));
                lienMap.put("CLEAR_BALANCE",balanceMap.get("CLEAR_BALANCE"));
                lienMap.put("STATUS", status);
                lienMap.put("LTD_CLOSING_STATUS",transactionUI.getExcessLoanAmt());
                lienMap.put("FULLY_CLOSING_STATUS",transactionUI.getClosingDepositStatus());
                authMap.put("DEPOSIT_NO",txtDepositNo.getText());
                authMap.put("PROD_ID",balanceMap.get("PROD_ID"));
                authMap.put("SHADOW_LIEN",balanceMap.get("SHADOW_LIEN"));
                authMap.put("CLEAR_BALANCE",balanceMap.get("CLEAR_BALANCE"));
                authMap.put("STATUS", status);
                authMap.put("LTD_CLOSING_STATUS",transactionUI.getExcessLoanAmt());
                lienMap.put("FULLY_CLOSING_STATUS",transactionUI.getClosingDepositStatus());
                headMap.put("PROD_ID",balanceMap.get("PROD_ID"));
                lst = ClientUtil.executeQuery("getDepositAccHeadId", headMap);
                if(lst != null && lst.size()>0){
                    headMap = (HashMap)lst.get(0);
                    lienMap.put("HEAD_DESC",headMap.get("PROD_DESC"));
                    authMap.put("HEAD_DESC",headMap.get("PROD_DESC"));
                    //                                            depositLien.depClosingLineMarked(lienMap);
                    //                                            depositLien.depClosingLineAuthorize(authMap);
                    observable.setTransStatus(status);
                    //                                            accountClosing = new AccountClosingUI("TermLoan");
                    accountClosing = transactionUI.getAccountClosing();
                    HashMap transMap = new HashMap();
                    transMap.put("PROD_ID",transactionUI.getCallingProdID());
                    transMap.put("ACCOUNT NUMBER",transactionUI.getCallingAccNo());
                    transMap.put("AMOUNT",transactionUI.getCallingAmount());
                    transMap.put("NAME",transactionUI.getCallingApplicantName());
                    if(lblClosureTypeValue.getText().equals(CommonConstants.PREMATURE_CLOSURE)){
                        transMap.put("DEPOSIT_PREMATURE_CLOSER","DEPOSIT_PREMATURE_CLOSER");
                    }
                    HashMap prodMap = accountClosing.transactionActionType(transMap);
                    HashMap closingMap = new HashMap();
                    closingMap.put("ACCOUNT NUMBER",transactionUI.getCallingAccNo());
                    closingMap.put("PROD_ID",transactionUI.getCallingProdID());
                    //                                            accountClosing.fillData(closingMap);
                    HashMap accountMap = accountClosing.btnNull(transMap);
                    accountMap.put("LTD_CLOSING_STATUS",transactionUI.getExcessLoanAmt());                    
                    accountMap.put("FULLY_CLOSING_STATUS",transactionUI.getClosingDepositStatus());
                    transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, transactionUI.getCallingAccNo());
                    HashMap totAmtMap = new HashMap();
                    totAmtMap = transDetailsUI.getTermLoanCloseCharge();
					//added by chithra for service Tax
                    HashMap loanSerMap = (HashMap)transactionUI.getServiceTax_Map();
                    System.out.println("loanSerMap  :::"+loanSerMap);
                    //System.out.println("observable.getTransStatus() :: " + observable.getTransStatus()+" behavesLike :: "+ behavesLike);
                    if (observable.getTransStatus().equalsIgnoreCase("LIEN") && behavesLike.equals("RECURRING")) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                        calculateServiceTaxAmt();
                        observable.setServiceTax_Map(serviceTax_Map);
                    }
                    if (loanSerMap != null && loanSerMap.containsKey("TOT_TAX_AMT")) {
                        double seramt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(loanSerMap.get("TOT_TAX_AMT")));
                        if (seramt > 0) {
                            totAmtMap.put("TOT_SER_TAX_AMT", loanSerMap.get("TOT_TAX_AMT"));
                            totAmtMap.put("SER_TAX_HEAD", loanSerMap.get("TAX_HEAD_ID"));
                            accountMap.put("SERVICETAXDETAILSTO_DEP",observable.setServiceTaxDetails(loanSerMap));
                        }
                    }
                    if(accountMap.containsKey("AS_CUSTOMER_COMES") && accountMap.get("AS_CUSTOMER_COMES").equals("Y"))
                        totAmtMap.put("CURR_MONTH_INT",accountMap.get("CURR_MONTH_INT"));
                    accountMap.put("TOTAL_AMOUNT",totAmtMap);
                    System.out.println("$$$$ accountmap "+accountMap);
                    System.out.println("$$$$ totAmtMap "+totAmtMap);
                    observable.setLtdClosingMap(accountMap);
                    observable.setTransProdId(transactionUI.getCallingProdID());
                    observable.setPrev_interest(lblPreIntValue.getText());
                    observable.doAction();
                    accountMap = new HashMap();
                    observable.setLtdClosingMap(accountMap);
                }
                lienMap = null;
                authMap = null;
                headMap = null;
                accHeadMap = null;
                }else{
                    //Accounts with complete Lien marking cannot be closed
                    ClientUtil.showAlertWindow("Lien is Marked.... remove the Lien");
                    btnCancelActionPerformed(null);
                }
            }
        }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //        lblActualPeriodRunValue.setText("");
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnView.setEnabled(false);
       // removeEditLockScreen(txtDepositNo.getText());
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void removeRadioButtons() {
        //---Account---
        rdgPenaltyRateApplicable.remove(rdoPenaltyRateApplicbleYes);
        rdgPenaltyRateApplicable.remove(rdoPenaltyRateApplicbleNo);
        
        rdgTypesOf_Deposit.remove(rdoLTDDeposit);
        rdgTypesOf_Deposit.remove(rdoRegularDeposit);
        
        rdgTransferOut.remove(rdoTransfer_Out_Yes);
        rdgTransferOut.remove(rdoTransfer_In_No);        
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = CANCEL ;
        //removeEditLockScreen(txtDepositNo.getText());
        //        super.removeEditLock(txtDepositNo.getText() + observable.getSubDepositNo());
        //        rdoPenaltyRateApplicbleYes.setEnabled(true);
        //        rdoPenaltyRateApplicbleNo.setEnabled(true);
      lblAccountHeadValue.setText("");
        observable.resetForm();
        observable.setAuthorizeMap(null);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        ClientUtil.enableDisable(this.panPartialWithDrawal,false);
        //        observable.notifyObservers();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transDetailsUI.setTransDetails(null, null, null);
        custDetailsUI.updateCustomerInfo("");
        lblActualPeriodRunValue.setText("");
        lblAccountHeadValue.setText("");
        lblTDSCollectedValue.setText("");
        txtTransferringBranch.setText("");
        lblBranchValue.setText("");
        lblClosureTypeValue.setText("");
        btnView.setEnabled(true);
        isFilled = false;
        setModified(false);
        LTDflag = false;
        rdoRegularDeposit.setSelected(false);
        rdoLTDDeposit.setSelected(false);
        rdoTransfer_Out_Yes.setSelected(false);
        rdoTransfer_In_No.setSelected(false);
        btnTransferringBranch.setEnabled(false);
        txtDepositNo.setText("");
        cboProductId.setEnabled(false);
        btnTableCancel.setEnabled(true);
        btnCancel.setEnabled(true);
        btnView.setEnabled(true);
        flPtWithoutPeriod = false;
        lblPreIntValue.setText("");
        observable.setFlPtWithoutPeriod(flPtWithoutPeriod);
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
    }
        btnAuthorize.requestFocus(false);
        rejectionApproveUI=null;
        chargelst = null;
        taxSettingsListForCharges = null;
    }//GEN-LAST:event_btnCancelActionPerformed
    private void addRadioButtons() {
        //---Account---
        rdgPenaltyRateApplicable = new CButtonGroup();
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleYes);
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleNo);
        
        rdgTypesOf_Deposit = new CButtonGroup();
        rdgTypesOf_Deposit.add(rdoLTDDeposit);
        rdgTypesOf_Deposit.add(rdoRegularDeposit);
        
        rdgTransferOut = new CButtonGroup();
        rdgTransferOut.add(rdoTransfer_Out_Yes);
        rdgTransferOut.add(rdoTransfer_In_No);
    }
    
    private double getTotalChargeAmounts(){
    double depositClosingCharge = 0.0;
    List chargelists  = observable.getChargelst();
    HashMap chargeMaps = new HashMap();
    if (chargelists != null && chargelists.size() > 0) {
        for (int i = 0; i < chargelists.size(); i++) {
            chargeMaps = (HashMap) chargelists.get(i);
            if (chargeMaps.containsKey("CHARGE_AMOUNT") && chargeMaps.get("CHARGE_AMOUNT") != null) {
                depositClosingCharge += CommonUtil.convertObjToDouble(chargeMaps.get("CHARGE_AMOUNT"));
            }
        }
    }
    return CommonUtil.convertObjToDouble(depositClosingCharge);
}

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        observable.setAuthrize2("");
        
        if (observable.getSpecialRDCompleted().equalsIgnoreCase("Y")) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Special RD installments Completed. Do you want to close?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);            
            if (yesNo != 0) {
                return;
            }
        }
        
        if (txtDepositNo.getText() != null && !txtDepositNo.getText().equals("")) {
            HashMap hmap = new HashMap();
            hmap.put("ACCOUNTNO", (txtDepositNo.getText() + "_1"));
            List deathList = ClientUtil.executeQuery("getDeathMarkedCustomerTD", hmap);
            if (deathList.size() > 0) {
                String[] obj = {"Yes", "No"};
                int option = COptionPane.showOptionDialog(null, ("Owner of this account is death marked. Do you want to really close..."),
                        ("Select The Desired Option"),
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                if (option == 0) {
                    rejectionApproveUI = new RejectionApproveUI(this);
                    HashMap reMap = rejectionApproveUI.getLoginDetails();
                    if (reMap != null && reMap.containsKey("USER_ID")) {
                        observable.setAuthrize2(CommonUtil.convertObjToStr(reMap.get("USER_ID")));
                    }
                    if (rejectionApproveUI.isCancelActionKey()) {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        if (transactionUI.getTransactionOB().getSelectedTxnType() != null && observable.getSelectedBranchID() != null) {
            boolean interbranchFlag = false;
            if (transactionUI.getTransactionOB().getSelectedTxnType().equals(CommonConstants.TX_TRANSFER)) {
                if (ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())) {
                    interbranchFlag = false;
                } else if (ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())) {
                    interbranchFlag = false;
                } else if (ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())) {
                    interbranchFlag = false;
                } else {
                    interbranchFlag = true;
                }
            } else {
                interbranchFlag = false;
            }
            if (interbranchFlag) {
                ClientUtil.showAlertWindow("Incase of interbranch transaction either " + "\n" + "Dr or Cr account of the transaction should be of own branch");
                return;
            } else {
                System.out.println("Continue for transactions...");
            }
        }
        String status = transactionUI.getCallingStatus();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", (txtDepositNo.getText() + "_1"));
            List authList = ClientUtil.executeQuery("getPendingTransAuthList", hmap);
            if (authList != null && authList.size() > 0) {
                hmap = (HashMap) authList.get(0);
                String tranMode = CommonUtil.convertObjToStr(hmap.get("TRANS_MODE"));
                ClientUtil.displayAlert(tranMode + "transaction is pending for authorization. Can not proceed");
                btnCancelActionPerformed(null);
            }
        }
        //added by Rishad 17/dec/2018 for deposit to Loan case
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && transactionUI.getCallingTransType().equalsIgnoreCase("TRANSFER")
                && transactionUI.getCallingTransProdType().equalsIgnoreCase("TL")) {

            if (!behavesLike.equals("DAILY")) {
                ClientUtil.displayAlert("Loan Recipt allow only for Daily product");
                btnCancelActionPerformed(null);
            }
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", transactionUI.getCallingTransAcctNo());
            List authList = ClientUtil.executeQuery("getPendingTransAuthList", hmap);
            if (authList != null && authList.size() > 0) {
                hmap = (HashMap) authList.get(0);
                String tranMode = CommonUtil.convertObjToStr(hmap.get("TRANS_MODE"));
                ClientUtil.displayAlert(tranMode + " Transaction is pending for authorization. Can not proceed");
                btnCancelActionPerformed(null);
            }
            double totalBalanceAmount = 0.0;
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", transactionUI.getCallingTransAcctNo());
            whereMap.put("CURRDATE", currDt);
            List creditList = ClientUtil.executeQuery("getTotalLoanBalance", whereMap);
            if (creditList != null && creditList.size() > 0) {
                HashMap totBalMap = (HashMap) creditList.get(0);
                if (totBalMap != null && totBalMap.containsKey("TOTAL_LOAN_BALANCE") && totBalMap.get("TOTAL_LOAN_BALANCE") != null) {
                    totalBalanceAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_LOAN_BALANCE"));
                }
            }
            if (CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()) > totalBalanceAmount) {
                ClientUtil.displayAlert("Transaction amount should be lessthan  Loan Balance :" + totalBalanceAmount + " . Can not proceed");
                btnCancelActionPerformed(null);
            }
        }
        String transProdType = transactionUI.getCallingTransProdType();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //if(behavesLike.equals("DAILY")){
            finalizeCharges();
            //}
            if (CommonUtil.convertObjToDouble(transDetailsUI.getShadowCredit()).doubleValue() != 0
                    || CommonUtil.convertObjToDouble(transDetailsUI.getShadowDebit()).doubleValue() != 0) {
                ClientUtil.showAlertWindow(resourceBundle.getString("shadowCrDr"));
                return;
            } else if (CommonUtil.convertObjToDouble(transDetailsUI.getFreezeAmount()).doubleValue() > 0) {
                //Account cannot be closed if freeze exists
                ClientUtil.showAlertWindow(resourceBundle.getString("freezeAmount"));
                return;
            }
            //            else if(CommonUtil.convertObjToDouble(transDetailsUI.getAvailableBalance()).doubleValue() <= 0){
            //                //Accounts with complete Lien marking cannot be closed
            //                ClientUtil.showAlertWindow(resourceBundle.getString("lienAmount"));
            //                return ;
            //            }
        }
        if (viewType == CLOSED_ACCT && observable.getLblStatus().equals("Edit") && rdoLTDDeposit.isSelected() == true) {
            ClientUtil.showAlertWindow("Can not Edit this Deposit Please Reject and do it fresh transaction...");
            return;
        }
        if (rdoTransfer_Out_Yes.isSelected() == true && txtTransferringBranch.getText().length() == 0) {
            ClientUtil.showAlertWindow(resourceBundle.getString("TransferOut"));
            return;
        }
        int transactionSize = 0;
        if (rdoRegularDeposit.isSelected() == true && transactionUI.getOutputTO().size() == 0
                && CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue() > 0) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            return;
        } else {
            if (rdoRegularDeposit.isSelected() == true && CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue() > 0) {
                transactionSize = (transactionUI.getOutputTO()).size();
                if (transactionSize != 1 && CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                    return;
                } else {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            } else if (rdoLTDDeposit.isSelected() == true && transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
        }
        if (transactionSize == 0 && CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue() > 0
                && rdoRegularDeposit.isSelected() == true) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            return;
        } else if (transactionSize != 0 && rdoRegularDeposit.isSelected() == true
                && CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue() > 0 && buttonVisit) {
            if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                return;
            } else {
                updateOBFields();
                double depositTransAmt = 0; //Stores Partial Withdrawl Amount or Closing Amount
                if (rdoTransfer_In_No.isSelected() == true) {
                    if (observable.getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)
                            || observable.getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                        depositTransAmt = CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue(); //Normal Or Premature Closure
                    } else {
                        depositTransAmt = CommonUtil.convertObjToDouble(txtAmountWithDrawn.getText()).doubleValue(); //Partial Withdrawl
                    }
                } else if (rdoTransfer_Out_Yes.isSelected() == true) {
                    depositTransAmt = CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue(); //Normal Or Premature Closure
                }
                double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                if (observable.getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    if (rdoPenaltyRateApplicbleYes.isSelected() == true || rdoPenaltyRateApplicbleNo.isSelected() == true) {
                        if (rdoPenaltyRateApplicbleYes.isSelected()) {
                            observable.setPenaltyInt("Y");
                        }
                        if (rdoPenaltyRateApplicbleNo.isSelected()) {
                            observable.setPenaltyInt("N");
                        }
                    } else {
                        ClientUtil.displayAlert("Choose Penalty Rate Applicable Yes or No...");
                        return;
                    }
                }
//                if(rdoLTDDeposit.isSelected())
//                    observable.setTypeOfDep("LTD");
//                else
                observable.setTypeOfDep("NOLTD");

//                HashMap balanceMap = new HashMap();
//                balanceMap.put("DEPOSIT_NO",txtDepositNo.getText());
//                List lst = ClientUtil.executeQuery("getAvailableBalanceForDep", balanceMap);
//                if(lst != null && lst.size()>0){
//                    balanceMap = (HashMap)lst.get(0);
//                    ltdClosingRecords(balanceMap,status);
//                }else{
                if (ClientUtil.checkTotalAmountTallied(depositTransAmt, transTotalAmt) == true) {
                    observable.setPrev_interest(lblPreIntValue.getText());
                    observable.doAction();
                } else {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    return;
                }
                observable.setResultStatus();
//                }
            }
            lblActualPeriodRunValue.setText("");
            lblAccountHeadValue.setText("");
        } else if (rdoRegularDeposit.isSelected() == true
                && CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue() == 0 && buttonVisit) {
            String[] obj = {"Ok", "Cancel"};
            int option = COptionPane.showOptionDialog(null, ("No Balance in the account. Do you want to really close..."),
                    ("Select The Desired Option"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
            if (option == 0) {
                updateOBFields();
                observable.setTypeOfDep("NOLTD");
                observable.setPrev_interest(lblPreIntValue.getText());
                observable.doAction();
                setModified(false);
                observable.setResultStatus();
            } else {
                btnCancelActionPerformed(null);
            }
            buttonVisit = false;
        } else if (rdoLTDDeposit.isSelected() == true && buttonVisit && transactionUI.getExcessLoanAmt().length() > 0) {
            //This code is repeating. It needs to be removed after putting
            //a flag in the if conditions above. Below code //is for cases other than Normal
//            if(transactionUI.getExcessLoanAmt()!=null && transactionUI.getClosingDepositStatus()!=null){
//                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
//                return;                            
//            }
//            if(transactionUI.getExcessLoanAmt()!=null && transactionUI.getExcessLoanAmt().equals("DONT_CLOSE") &&
//            transactionUI.getClosingDepositStatus()!=null && transactionUI.getClosingDepositStatus().equals("PARTIALLY_CLOSING")){
//                
//            }
            observable.setTypeOfDep("LTD");
            HashMap balanceMap = new HashMap();
            balanceMap.put("DEPOSIT_NO", txtDepositNo.getText());
            List lst = ClientUtil.executeQuery("getAvailableBalanceForDep", balanceMap);
            if (lst != null && lst.size() > 0) {
                balanceMap = (HashMap) lst.get(0);
                ltdClosingRecords(balanceMap, status);
                Lien_Accno = CommonUtil.convertObjToStr(balanceMap.get("LIEN_AC_NO"));
            }
            buttonVisit = false;
        } else {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
            return;
        }
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_FAILED){
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            int CreditcashCount = 0;
            int DebitcashCount = 0;
            String actNum = txtDepositNo.getText();
            HashMap transMap = new HashMap();
            HashMap transDetMap = new HashMap();
            transMap.put("DEPOSIT_NO",txtDepositNo.getText()+"_1");
            transMap.put("CURR_DT", currDt);
            List lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
            HashMap transIdMap = new HashMap();
            if(lst !=null && lst.size()>0){
                displayStr += "Transfer Transaction Details...\n";
                for(int i = 0;i<lst.size();i++){
                    transMap = (HashMap)lst.get(i);
                    displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                    oldBatchId = newBatchId;
                }
                transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
            }

            //The following block added by Rajesh (in case total amount credited to TL or AD A/cs.
            //code modified by rishad 19/dec/2018 for satisfying Ltd and Regular Loan recipt
            if ((transactionUI.getCallingTransProdType() != null && (transactionUI.getCallingTransProdType().equals("TL") || transactionUI.getCallingTransProdType().equals("AD"))) || (Lien_Accno != null && Lien_Accno.length() > 0)) {
                String loanNumber = "";
                if (Lien_Accno != null && Lien_Accno.length() > 0) {
                    loanNumber = Lien_Accno;
                } else if ((transactionUI.getCallingTransProdType().equals("TL")|| transactionUI.getCallingTransProdType().equals("AD")) && transactionUI.getCallingTransAcctNo() != null && transactionUI.getCallingTransAcctNo().length() > 0) {
                    loanNumber = transactionUI.getCallingTransAcctNo();
                }

                if (loanNumber != null && loanNumber.length() > 0) {
                    transMap.put("DEPOSIT_NO", loanNumber);
                    transMap.put("CURR_DT", currDt);
                    lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                    // transIdMap = new HashMap();
                    if (lst != null && lst.size() > 0) {
                        displayStr += "Loan Transaction Details...\n";
                        for (int i = 0; i < lst.size(); i++) {
                            transMap = (HashMap) lst.get(i);
                            displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                    + "   Batch Id : " + transMap.get("BATCH_ID")
                                    + "   Trans Type : " + transMap.get("TRANS_TYPE");
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            if (actNum != null && !actNum.equals("")) {
                                displayStr += "   Account No : " + transMap.get("ACT_NUM")
                                        + "   Loan Amount : " + transMap.get("AMOUNT") + "\n";
                            } else {
                                displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                        + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                            }
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                            oldBatchId = newBatchId;
                        }
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
           }
            transMap.put("DEPOSIT_NO",txtDepositNo.getText()+"_1");
            lst = ClientUtil.executeQuery("getRemitIssueAuthDetails", transMap);
            if(lst!=null && lst.size()>0){
                transMap = (HashMap)lst.get(0);
                transMap.put("CURR_DT", currDt);
                transMap.put("DEPOSIT_NO",transMap.get("VARIABLE_NO"));
                lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                if(lst !=null && lst.size()>0){
                    for(int i = 0;i<lst.size();i++){
                        transMap = (HashMap)lst.get(i);
                        displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                        "   Batch Id : "+transMap.get("BATCH_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                            "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                        }else{
                            displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                            "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                        }
                        oldBatchId = newBatchId;
                    }
                }
            }
            actNum = txtDepositNo.getText();
            transMap = new HashMap();
            //            transMap.put("DEPOSIT_NO",actNum+"_1");
            //            transMap.put("CURR_DT", currDt);
            //            lst = ClientUtil.executeQuery("getCashTransAuthDetails", transMap);
            transMap.put("BATCH_ID",txtDepositNo.getText()+"_1");
            transMap.put("TRANS_DT", currDt);
            transMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            transMap.put("AUTH_REJ_STATUS","AUTH_REJ_STATUS");
            lst = ClientUtil.executeQuery("getCashDetails", transMap);
            if(lst !=null && lst.size()>0){
                displayStr += "Cash Transaction Details...\n";
                for(int i = 0;i<lst.size();i++){
                    transMap = (HashMap)lst.get(i);
                    displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
                        "   Deposit Amount :  "+transMap.get("AMOUNT")+"\n";
                    }else{
                        if(transMap.get("TRANS_TYPE").equals("DEBIT")){
                        displayStr +="   Ac Hd Desc :  "+transMap.get("AC_HD_ID")+
                        "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
                        }else{
                            displayStr +="   Ac Hd Desc :  "+transMap.get("AC_HD_ID")+
                        "  Interest Amount : :  "+transMap.get("AMOUNT")+"\n";
                        }
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                    transDetMap.put(transMap.get("SINGLE_TRANS_ID"),transMap.get("TRANS_TYPE"));
                    if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
                            DebitcashCount++;
                    }
                    if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                            CreditcashCount++;
                    }
                }
            }
            if(!displayStr.equals("")) {
                ClientUtil.showMessageWindow(""+displayStr);
            }
            
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
            System.out.println("#$#$$ yesNo : "+yesNo);
            if (yesNo==0) {
                String reportName = null;
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("TransDt", currDt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                //System.out.println("transIdMap............"+transIdMap);
                Object keys[] = transIdMap.keySet().toArray();
                //System.out.println("");
                for (int i=0; i<keys.length; i++) {
                    paramMap.put("TransId", keys[i]);
                    ttIntgration.setParam(paramMap);
                    //                    if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                         reportName = "ReceiptPayment";
                         ttIntgration.integrationForPrint(reportName, false);
                    } 
                    //else if (CommonUtil.convertObjToStr(transDetMap.get(keys[i])).equals("CREDIT")) {
                     //   reportName = "CashReceipt";
                    //} else {
                        //reportName = "CashPayment";
                    //}
                    //ttIntgration.integrationForPrint(reportName, false);
                    if (CreditcashCount > 0) {
                           reportName = "CashReceipt";
                           System.out.println("CashReceipt@@@@@" + reportName);
                           ttIntgration.integrationForPrint(reportName, false);
                    }

                    if (DebitcashCount > 0) {
                           reportName = "CashPayment";
                           System.out.println("CashPayment@@@@@" + reportName);
                           ttIntgration.integrationForPrint(reportName, false);
                    }
                }
            }
            this.btnCancelActionPerformed(null);
        }else
            this.btnCancelActionPerformed(null);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            this.btnCancelActionPerformed(null);
            panChargeDetails.setVisible(false);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if(fromSmartCustUI){
                    this.dispose();
                    fromSmartCustUI = false;
                }
            }
        }
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void updateOBPartialWithDrawal(){
        observable.setTxtAmtWithDrawn(this.txtAmountWithDrawn.getText());
        observable.setTxtPWNoOfUnits(this.txtNoOfUnits.getText());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        this.setUp(ClientConstants.ACTIONTYPE_DELETE,true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        callView(CLOSED_ACCT);
        btnDepositNo.setEnabled(true);
        transactionUI.cancelAction(false);
        setButtonEnableDisable();
        enableInEdit(false);
        rdoPenaltyRateApplicbleYes.setEnabled(false);
        rdoPenaltyRateApplicbleNo.setEnabled(false);
        rdoLTDDeposit.setEnabled(false);
        rdoRegularDeposit.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        this.setUp(ClientConstants.ACTIONTYPE_EDIT,true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        callView(CLOSED_ACCT);
        btnDepositNo.setEnabled(true);
        transactionUI.cancelAction(true);
        setButtonEnableDisable();
        enableInEdit(false);
        btnView.setEnabled(false);
        btnSave.setEnabled(false);
        enableDisableDepositOut(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void enableInEdit(boolean flag){
        btnDepositNo.setEnabled(flag);
        cboProductId.setEnabled(flag);
        txtDepositNo.setEnabled(flag);
    }
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        this.setUp(ClientConstants.ACTIONTYPE_NEW,true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setModified(true);
        this.setButtonEnableDisable();
        this.enableDisableButtons(true);
        ClientUtil.enableDisable(this.panPartialWithDrawal,true);
        ClientUtil.enableDisable(panChargeDetails, false);
        pwButtonsDisable();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        rdoTransfer_Out_Yes.setVisible(true);
        rdoTransfer_In_No.setVisible(true);
        lblTransferOut.setVisible(true);
        lblActualPeriodRunValue.setText("");
        rdoTransfer_In_No.setSelected(true);
        txtTransferringBranch.setText("");
        cboProductId.setEnabled(true);
        btnTableCancel.setEnabled(true);
        flPtWithoutPeriod = false;
        observable.setFlPtWithoutPeriod(flPtWithoutPeriod);
        enableDisableDepositOut(false);
        txtDepositNo.setText(CommonUtil.convertObjToStr(getSelectedBranchID()));
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
        cboProductId.requestFocus();
        setPenaltyRateIntApplicable();
    }//GEN-LAST:event_btnNewActionPerformed
    private void downCancel(){   
        buttonVisit = false;
        transDetailsUI.setTransDetails(null,null,null);        
        observable.resetDetails();
        observable.resetDepositDetails();
        updateDepositDetails();
        updatePresentDepositDetails();
        updateClosingPositionDetails();
        updatePartialWithDrawal();
        transactionUI.resetObjects();
        transactionUI.cancelAction(true);
        this.populatePWDetail();
        lblActualPeriodRunValue.setText("");
        lblDeathClaimValue.setText("");
        lblDeathClaimInterestValue.setText("");
        observable.setRdoPenaltyPenalRate_no(false);
        observable.setRdoPenaltyPenalRate_yes(false);
        rdgPenaltyRateApplicable.remove(rdoPenaltyRateApplicbleYes);
        rdgPenaltyRateApplicable.remove(rdoPenaltyRateApplicbleNo);
        rdoPenaltyRateApplicbleYes.setSelected(false);
        rdoPenaltyRateApplicbleNo.setSelected(false);
        rdgPenaltyRateApplicable = new CButtonGroup();
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleYes);
        rdgPenaltyRateApplicable.add(rdoPenaltyRateApplicbleNo);
        rdoPenaltyRateApplicbleYes.setEnabled(true);
        rdoPenaltyRateApplicbleNo.setEnabled(true);
    }
    private void btnTableCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableCancelActionPerformed
        // Add your handling code here:
        downCancel();
        btnTableOk.setEnabled(true);
    }//GEN-LAST:event_btnTableCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    //Added By Nidhin 
    public void setCallingAmt(String amt) {
        //transactionUI.okAction(true);
        transactionUI.setSourceAccountNumber(observable.getTxtDepositNo() + "_" + observable.getSubDepositNo());
        transactionUI.setCallingApplicantName(lblDepositAccountNameValue.getText());
        transactionUI.setCallingProdID(observable.getProdID());
        transactionUI.setCallingAccNo(observable.getTxtDepositNo());
        transactionUI.setCallingLtdDepositNo(observable.getTxtDepositNo());
        if(observable.getDailyDepositLoanPreClose().equals("Y")){
          transactionUI.setCallingDepIntRate(observable.getDailyDepositLoanPreCloseROI());
        }else{
        transactionUI.setCallingDepIntRate(lblPreClosingRateValue.getText());
        }
        transactionUI.setCallingIntAmt(observable.getPayReceivable());
        transactionUI.setCallingClosingType(observable.getLblClosingType());
        transactionUI.setCallingAmount(amt);
        observable.setClosingDisbursal(CommonUtil.convertObjToStr(amt));

    }
    private void calculatingDetails(){
        try{
            updateOBFields();
            rdoPenaltyRateApplicbleYes.setEnabled(false);
            rdoPenaltyRateApplicbleNo.setEnabled(false);
//            observable.setTbmSubDeposit((TableModel)tblSubDeposits.getModel());
            observable.getSubDepositDetails();
            transDetailsUI.setTransDetails("TD", ProxyParameters.BRANCH_ID, observable.getTxtDepositNo() + "_" + observable.getSubDepositNo());
            if(viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST)
                transactionUI.okAction(true);
            transactionUI.setSourceAccountNumber(observable.getTxtDepositNo() + "_" + observable.getSubDepositNo());
            updateDepositDetails();
            updatePresentDepositDetails();
            updateClosingPositionDetails();
            HashMap getDataMap = new HashMap();
            calculateMaturity();//setting closingtype...
            getDataMap.put("DEPOSITNO", observable.getTxtDepositNo());
            getDataMap.put("DEPOSITSUBNO", observable.getSubDepositNo());
            Date deposit_Date = (Date)DateUtil.getDateMMDDYYYY(observable.getDepositDate());
            Date mat_Date = (Date)DateUtil.getDateMMDDYYYY(observable.getMaturityDate());
            getDataMap.put("DEPOSIT_DT",deposit_Date);
            getDataMap.put("MATURITY_DT", mat_Date);
            getDataMap.put(CommonConstants.PRODUCT_ID, observable.getCbmProductId().getKeyForSelected());
            getDataMap.put("CUSTID", observable.getCustomerID());
            getDataMap.put("CLOSING_TYPE",observable.getLblClosingType());
            getDataMap.put("AMOUNT",lblPrincipalValue.getText());
            getDataMap.put("CATEGORY_ID",observable.getCategory());//lblCategoryValue.getText()
            if (behavesLike.equals("FLOATING_RATE")) {
                getDataMap.put("DEPOSIT_NO", observable.getTxtDepositNo());
                List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", getDataMap);
                HashMap getCustDataMap = new HashMap();
                getCustDataMap = (HashMap) list.get(0);
                getDataMap.put("PRODUCT_TYPE", "TD");
                getDataMap.put("CATEGORY_ID", getCustDataMap.get("CATEGORY"));
                getDataMap.put("MATURITY_AMT", String.valueOf(maturityAmount));
                getDataMap.put("CLOSING_DT", currDt);
                getDataMap.put("FLOATING_RATE", "FLOATING_RATE");
                if (flPtWithoutPeriod) {
                    getDataMap.put("FLOATING_TYPE", "WITHOUT_PERIOD");
                } else {
                    getDataMap.put("FLOATING_TYPE", "WITH_PERIOD");
                }
            }
            getDataMap.put("CURRENT_DT",currDt);        
            observable.getData(getDataMap);
            String round="";
            HashMap whereMap= new HashMap();
            whereMap.put("PROD_ID",((ComboBoxModel)this.cboProductId.getModel()).getKeyForSelected());
            List list = ClientUtil.executeQuery("getSelectRoundOff",whereMap);
            if(list!=null && list.size()>0){
                whereMap = (HashMap)list.get(0);
                round=CommonUtil.convertObjToStr(whereMap.get("ROUND_TERMS"));
            }
            // Setting name for lable..... changed by sathiya....
            double disburse = CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue();
            //            double disburse = (CommonUtil.convertObjToDouble(lblClDisbursalValue.getText()).doubleValue());
            double bal = (CommonUtil.convertObjToDouble(observable.getPrinicipal()).doubleValue());
            //            System.out.println("disburse : "+ disburse);
            //            System.out.println("disbursebalance : "+ observable.getBalanceAmt());
            //            System.out.println("observable.getBalance : "+ observable.getPrinicipal());
            //            System.out.println("disbursebal : "+ bal);
            //            System.out.println("disburse : "+ disburse);
            HashMap behavesLikeMap = new HashMap();
            behavesLikeMap.put("PROD_ID",observable.getProdID());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesLikeMap);
            if(lst != null && lst.size()>0)
                behavesLikeMap = (HashMap)lst.get(0);
            double taxAmt = CommonUtil.convertObjToDouble(observable.getClosingTds()).doubleValue();
            double closingAmt = CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue();
            double tdsColAmt =  CommonUtil.convertObjToDouble(observable.getTdsCollected()).doubleValue();
            double balTdsAmt = taxAmt - tdsColAmt;
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                lblMVvalue.setText(observable.getMaturityValue());
                lblInterestCrValue.setText(observable.getClosingIntCr());
            }else{
                lblMVvalue.setText(observable.getMaturityValue());
            }
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")){
                lblMVvalue.setText(observable.getMaturityValue());
                double drawn = (CommonUtil.convertObjToDouble(observable.getIntDrawn()).doubleValue());
                bal = bal + drawn;
                if( disburse > bal){
                    lblPayRec.setText("Payable");
                    double remainDisburseAmt = disburse - balTdsAmt;
                    observable.setClosingDisbursal(String.valueOf(remainDisburseAmt));
                }else{
                    lblPayRec.setText("Receivable");
                    observable.setClosingTds("0.0");
                }
            }
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("FIXED")){
                if( disburse > bal){
                    lblPayRec.setText("Payable");
                    double remainDisburseAmt = closingAmt - balTdsAmt;
                    observable.setClosingDisbursal(String.valueOf(remainDisburseAmt));
                }else{
                    lblPayRec.setText("Receivable");
                    observable.setClosingTds("0.0");
                }
            }
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")){
                double remainDisburseAmt = 0.0;
                if( disburse > bal){
                    lblPayRec.setText("Payable");
                    remainDisburseAmt = closingAmt - balTdsAmt;//tdsamount
                    observable.setClosingDisbursal(String.valueOf(remainDisburseAmt));
                }else{
                    lblPayRec.setText("Receivable");
                    observable.setClosingTds("0.0");
                }
            }
            String amtStr = "";
            transactionUI.setCallingApplicantName(lblDepositAccountNameValue.getText());
            transactionUI.setCallingProdID(observable.getProdID());
            transactionUI.setCallingAccNo(observable.getTxtDepositNo());
            transactionUI.setCallingLtdDepositNo(observable.getTxtDepositNo());
            if (observable.getDailyDepositLoanPreClose().equals("Y")) { //Added by nithya for KD-3211
                transactionUI.setCallingDepIntRate(observable.getDailyDepositLoanPreCloseROI());
            } else {
            transactionUI.setCallingDepIntRate(lblPreClosingRateValue.getText());
            }
           transactionUI.setCallingIntAmt(observable.getPayReceivable());
            transactionUI.setCallingClosingType(observable.getLblClosingType());
            if (rdoLTDDeposit.isSelected() == true) {
                if (observable.getAddIntLoanAmt() != null && !observable.getAddIntLoanAmt().equals("")) {
                    transactionUI.setAddIntLoanAmt(roundOffAmt(observable.getAddIntLoanAmt(), round));
                } else {
                    transactionUI.setAddIntLoanAmt("0");
                }

            }
            if(observable.getLblAddIntRtAmtVal()!=null && observable.getLblAddIntRtAmtVal().length()>0){
             observable.setLblAddIntRtAmtVal(roundOffAmt(CommonUtil.convertObjToStr(observable.getLblAddIntRtAmtVal()),round));
            } //added by chithra on 16-05-14 For additional int amt
             if(observable.getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)){
                if(observable.getDeathClaim().equals("Y")){
                    lblDeathClaim.setEnabled(true);
                    lblDeathClaim.setVisible(true);
                    lblDeathClaimInterest.setEnabled(true);
                    lblDeathClaimInterest.setVisible(true);
                    lblDeathClaimValue.setVisible(true);
                    lblDeathClaimValue.setEnabled(true);
                    lblDeathClaimValue.setText("Yes");
                    lblDeathClaimInterestValue.setEnabled(true);
                    lblDeathClaimInterestValue.setVisible(true);
                    lblDeathClaimInterestValue.setText(String.valueOf(observable.getDeathClaimAmt()));
                }else{
                    lblDeathClaim.setEnabled(false);
                    lblDeathClaim.setVisible(false);
                    lblDeathClaimInterest.setEnabled(false);
                    lblDeathClaimInterest.setVisible(false);
                    lblDeathClaimValue.setVisible(false);
                    lblDeathClaimValue.setEnabled(false);
                    lblDeathClaimInterestValue.setEnabled(false);
                    lblDeathClaimInterestValue.setVisible(false);
                }
            }
            //	0008098: Time Deposit closing time delayed amount wrongly debiting from customer
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                double remainDisburseAmt = 0.0;
                double disAmt = CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue();
                double penalAmt = CommonUtil.convertObjToDouble(observable.getChargeAmount()).doubleValue();
                double payable = CommonUtil.convertObjToDouble(observable.getClosingIntDb()).doubleValue();
                remainDisburseAmt = disAmt;//delayed chargeamount
                observable.setClosingDisbursal(String.valueOf(remainDisburseAmt));
                amtStr = String.valueOf(remainDisburseAmt);
            }else{
                amtStr = observable.getClosingDisbursal();
            }
            amtStr = roundOffAmt(amtStr,round);
            if(behavesLikeMap.get("BEHAVES_LIKE").equals("DAILY")) {
                double balance = CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue()
                - CommonUtil.convertObjToDouble(observable.getAgentCommisionRecoveredValue()).doubleValue();
                observable.setClosingDisbursal(String.valueOf(balance));
                amtStr = observable.getClosingDisbursal();
                amtStr = roundOffAmt(amtStr,round);
            }
            double previous_interest = 0.0;
            if (!lblPreIntValue.getText().equals("")) {
                previous_interest = CommonUtil.convertObjToDouble(lblPreIntValue.getText());
            }
            double d = Double.parseDouble(amtStr);
            double addAmt = 0;// added by chithra 17-05-14
            if (observable.getLblAddIntRtAmtVal() != null && observable.getLblAddIntRtAmtVal().length() > 0) {
                addAmt = CommonUtil.convertObjToDouble(observable.getLblAddIntRtAmtVal());
            }
            d = d + previous_interest + addAmt;
            amtStr = (new Double(d)).toString();
            amtStr = roundOffAmt(String.valueOf(d), round);
            transactionUI.setCallingAmount(roundOffAmt(amtStr, round));
            //---
            if(rdoTransfer_Out_Yes.isSelected() == true){
                lblInterestDrValue.setText(String.valueOf(0.0));
                lblInterestCrValue.setText(String.valueOf(0.0));
                lblActualPeriodRunValue.setVisible(false);
                lblPreClosingPeriod.setText("Transfer Out Date");
                lblClDisbursal.setText("Transfer Out Disbursal");
            }else if(rdoTransfer_In_No.isSelected() == true){
                
                if (observable.getMaturityDate() != null && !observable.getMaturityDate().equals("")
                        && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getMaturityDate()), currDt) >= 0) {
                    lblPreClosingRate.setText("Closing Rate");
                    lblPreClosingPeriod.setText("Closing Date");
                } else {
                    lblPreClosingRate.setText("Premature Closing Rate");
                    lblPreClosingPeriod.setText("Premature Closing Date");
                }
                lblActualPeriodRunValue.setVisible(true);
                lblClDisbursal.setText("Closing Disbursal");
                lblInterestCrValue.setText(roundOffAmt(lblInterestCrValue.getText(), round));
                lblInterestCrValue.setText(observable.getIntCr());
                lblInterestDrValue.setText(roundOffAmt(observable.getClosingIntDb(), round));
             
                lblClDisbursalValue.setText(roundOffAmt(amtStr,round));
                lblClTDSCollectedValue.setText(roundOffAmt(observable.getClosingTds(),round));
                lblPayRecValue.setText(roundOffAmt(observable.getPayReceivable(),round));
                lblPreClosingRateValue.setText(observable.getPrematureClosingRate());
                lblActualPeriodRunValue.setText(observable.getActualPeriodRun());
            }
            lblRateApplicableValue.setText(observable.getRateApplicable());//full interest
            this.lblPreClosingRateValue.setText(observable.getPrematureClosingRate());
            lblPenaltyPenalRateValue.setText(observable.getPenaltyPenalRate());
            lblPreClosingRateValue.setText(observable.getPrematureClosingRate());
            lblActualPeriodRunValue.setText(observable.getActualPeriodRun());
            lblAgentCommisionRecoveredValue.setText(roundOffAmt(observable.getAgentCommisionRecoveredValue(),round));
           //Added By Chithra on 12-06-14
            HashMap renewedIntMap = new HashMap();
            renewedIntMap.put("DEPOSIT NO", observable.getTxtDepositNo());
            List intValue = ClientUtil.executeQuery("getPreviousBalInt", renewedIntMap);
            if (intValue != null && intValue.size() > 0) {
                renewedIntMap = (HashMap) intValue.get(0);
                if (renewedIntMap!=null && renewedIntMap.containsKey("PREVIOUS_INT_PAYABLE") && renewedIntMap.get("PREVIOUS_INT_PAYABLE") != null) {
                    lblPreIntValue.setText(CommonUtil.convertObjToStr(renewedIntMap.get("PREVIOUS_INT_PAYABLE")));
                } else {
                    lblPreIntValue.setText("0"); //lblPreBalIntVal
                }
            }
            //End.....
            getDataMap.put("DEPOSITNO", observable.getTxtDepositNo());
            List lastApplDt = (List) ClientUtil.executeQuery("getLastIntApplDtForDeposit", getDataMap);
            HashMap lastMap = new HashMap();
            if (lastApplDt != null && lastApplDt.size() > 0) {
                lastMap = (HashMap) lastApplDt.get(0);
                lblLastInterestApplDateValue.setText(CommonUtil.convertObjToStr(lastMap.get("LAST_INT_APPL_DT")));
                observable.setLastIntAppDate(CommonUtil.convertObjToStr(lblLastInterestApplDateValue.getText()));
                if (lastMap.get("ACCT_STATUS").equals("MATURED")) {
                    rdoPenaltyRateApplicbleYes.setEnabled(false);
                    rdoPenaltyRateApplicbleNo.setEnabled(false);
                    double balInt = 0.0;
                    double drawnInt = 0.0;
                    HashMap behavesMap = new HashMap();
                    behavesMap.put("ACT_NUM", observable.getTxtDepositNo());
                    lst = (List) ClientUtil.executeQuery("getBehavesLikeForDepositNo", behavesMap);
                    if (lst != null && lst.size() > 0) {
                        behavesMap = (HashMap) lst.get(0);
                        if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                            balInt = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INT_CREDIT")).doubleValue();
                            drawnInt = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INT_DRAWN")).doubleValue();
                            balInt = balInt - drawnInt;
                        } else {
                            balInt = 0.0;
                        }
                    }
                    lblRateApplicableValue.setText(String.valueOf(0.0));
                    lblPreClosingRateValue.setText(String.valueOf(0.0));
                    lblInterestDrValue.setText(String.valueOf(balInt));
                    lblInterestCrValue.setText(String.valueOf(balInt));
                    lblPayRecValue.setText(String.valueOf(balInt));
                    lblBalanceDepositValue.setText(CommonUtil.convertObjToStr(lastMap.get("TOTAL_BALANCE")));
                    lblBalanceValue.setText(CommonUtil.convertObjToStr(lastMap.get("TOTAL_BALANCE")));
                    lblInterestDrValue.setText(String.valueOf(balInt));
                    observable.setPayReceivable(String.valueOf(balInt));
                    observable.setPermanentPayReceivable(String.valueOf(balInt));
                    observable.setClosingTds(String.valueOf(0.0));
                    double totbal = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_BALANCE")).doubleValue();
                    previous_interest = 0.0;
                    if (!lblPreIntValue.getText().equals("")) {
                        previous_interest = CommonUtil.convertObjToDouble(lblPreIntValue.getText());
                    }
                    addAmt = 0;
                    if (observable.getLblAddIntRtAmtVal() != null && observable.getLblAddIntRtAmtVal().length() > 0) {//added by chithra on 16-05-14
                        addAmt = CommonUtil.convertObjToDouble(roundOffAmt(observable.getLblAddIntRtAmtVal(), round));
                    }
                    totbal = totbal + balInt+previous_interest+addAmt;
                    lblClDisbursalValue.setText(String.valueOf(totbal));
                    lblClTDSCollectedValue.setText(String.valueOf(0.0));
                    amtStr = roundOffAmt(String.valueOf(totbal),round);
                    transactionUI.setCallingAmount(amtStr);
                    behavesMap = null;
                }
            }
            if(viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST){
                transactionUI.displayMode();
            }
            lastMap = null;
            getDataMap = null;
            whereMap = null;
            behavesLikeMap = null;
            if (premClos.equals("Y") && observable.getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                Date applnDat = (Date) currDt.clone();
                Double N = (double) DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getDepositDate()), applnDat);
//               Double N= monthDiff(applnDat,DateUtil.getDateMMDDYYYY(observable.getDepositDate()));
                Rounding rod = new Rounding();
                //  N=(Double)(rod.getNearest(((long)N*100),100)/100;
//                N=(double)rod.getNearest(N,2);
//                System.out.println("NN"+N);
                //Double N=(double)DateUtil.get
                //Double N= (double)DateUtii.m();
                Double P = Double.parseDouble(observable.getPrinicipal());
                Double R = Double.parseDouble(observable.getPrematureClosingRate());
                Double I = (P * N * R) / 36500;
//            String round1="";
//            HashMap whereMap1= new HashMap();
//            whereMap1.put("PROD_ID",((ComboBoxModel)this.cboProductId.getModel()).getKeyForSelected());
//            List list1 = ClientUtil.executeQuery("getSelectRoundOff",whereMap1);
//            
//            if(list1!=null && list1.size()>0){
//                whereMap1 = (HashMap)list1.get(0);
//                round1=CommonUtil.convertObjToStr(whereMap1.get("ROUND_TERMS"));
//            }
//           
//                System.out.println("round=[==="+round);
//           String ii=I.toString();
                // System.out.println("iiiiiiiiiii"+ii);
                Double II = observable.getRound(I, round);
                //String II=roundOffAmt(ii, round);
//                String ii=df.format(I);
//              I=Double.parseDouble(ii);
                lblInterestDrValue.setText("" + II);
                lblPayRecValue.setText("" + II);
                double prin1 = CommonUtil.convertObjToDouble(lblPrincipalValue.getText());
                double closDis = prin1 + II;
                lblClDisbursalValue.setText("" + closDis);
                transactionUI.setCallingAmount(lblClDisbursalValue.getText());
                observable.setPayReceivable(II.toString());
                observable.setIntDrawn(II.toString());
                observable.setClosingDisbursal(String.valueOf(closDis));
            }
            HashMap commisionMap = observable.getSlabWiseCommision();
            if (commisionMap != null && commisionMap.containsKey("COMMISION")) {
                if (commisionMap != null && commisionMap.get("COMMISION") != null) {
                    lblAgentCommisionRecoveredValue.setText(CommonUtil.convertObjToStr(commisionMap.get("COMMISION")));
                    observable.setAgentCommisionRecoveredValue(CommonUtil.convertObjToStr(commisionMap.get("COMMISION")));
                    if (observable.getGroupDepositProd().equalsIgnoreCase("Y")) { // Added by nithya on 09-10-2017 for group deposit changes
                        lblAgentCommisionRecoveredValue.setText(observable.getGroupDepositRecInt());
                        observable.setAgentCommisionRecoveredValue(observable.getGroupDepositRecInt());
                    }
                } else {
                    lblAgentCommisionRecoveredValue.setText(CommonUtil.convertObjToStr(0.0));
                    observable.setAgentCommisionRecoveredValue(CommonUtil.convertObjToStr(0.0));
                }
            }
//           //Added by Chithra on 21-04-14
            lblMaturityPeriod.setText(observable.getLblMaturityPeriod());
            lblAddIntrstRteVal.setText(observable.getLblAddIntrstRteVal());
            lblAddIntRtAmtVal.setText(roundOffAmt(observable.getLblAddIntRtAmtVal(), round));
            observable.setBothRecPayMap(null);
            if (observable.getLblPayRecDet() != null && observable.getLblPayRecDet().equals("Receivable") && observable.getPrev_interest() != null && CommonUtil.convertObjToDouble(observable.getPrev_interest()) > 0
                    && observable.getPayReceivable() != null && CommonUtil.convertObjToDouble(observable.getPayReceivable()) > 0) {
                HashMap tempMap = new HashMap();
                tempMap.put("PAYABLE_REC_INT", observable.getPrev_interest());
                tempMap.put("RECEIVABLE_AMT", observable.getPayReceivable());
                observable.setBothRecPayMap(tempMap);
            } else if (observable.getLblPayRecDet() != null && observable.getLblPayRecDet().equals("Receivable") && observable.getLblAddIntRtAmtVal() != null && CommonUtil.convertObjToDouble(observable.getLblAddIntRtAmtVal()) > 0
                    && observable.getPayReceivable() != null && CommonUtil.convertObjToDouble(observable.getPayReceivable()) > 0) {
                HashMap tempMap = new HashMap();
                tempMap.put("PAYABLE_REC_INT", observable.getLblAddIntRtAmtVal());
                tempMap.put("RECEIVABLE_AMT", observable.getPayReceivable());
                observable.setBothRecPayMap(tempMap);
            }
            if (CommonUtil.convertObjToDouble(lblAgentCommisionRecoveredValue.getText()) > 0) {// Addded by nithya on 16-06-2018 for calculating ServiceTax/GST implementation for charges and agent commission
                if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                    calculateServiceTaxAmt();
                }
            }
            observable.setLblServiceTaxval(lblServiceTaxval.getText());
            observable.setServiceTax_Map(serviceTax_Map);
        } catch (Exception Error) {
            Error.printStackTrace();
        }
    }
     
public static double monthDiff(Date d1,Date d2)
    {
        return (d1.getTime()-d2.getTime())/Avg_Millis_Per_Month;
    }
    private void btnTableOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableOkActionPerformed
        // Add your handling code here:
        HashMap renewedIntMap = new HashMap();
        renewedIntMap.put("DEPOSIT NO", txtDepositNo.getText());
        List intValue = ClientUtil.executeQuery("getPreviousBalInt", renewedIntMap);
        if (intValue != null && intValue.size() > 0) {
            renewedIntMap = (HashMap) intValue.get(0);
            if (renewedIntMap.containsKey("PREVIOUS_INT_PAYABLE") && renewedIntMap.get("PREVIOUS_INT_PAYABLE") != null) {
                lblPreIntValue.setText(CommonUtil.convertObjToStr(renewedIntMap.get("PREVIOUS_INT_PAYABLE")));
            } else {
                lblPreIntValue.setText("0"); //lblPreBalIntVal
            }
        }
        if (observable.getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
            buttonVisit = true;
            if (rdoTransfer_In_No.isSelected() == true && rdoPenaltyRateApplicbleYes.isSelected() != true
                    && rdoPenaltyRateApplicbleNo.isSelected() != true && (viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST)) {
                ClientUtil.displayAlert("Choose Penalty Rate Applicable Yes or No...");
            } else {
                calculatingDetails();
            }
            if (behavesLike != null && !behavesLike.equals("DAILY")) {
                if (transNew) {
                    tabClosingType.add(chargePan);
                    chargePan.setName("Charge Details");
                    createChargeTable(); //bb
                    if (tableFlag) {
                        editChargeTable();
                        chargeAmount();
                    }
                }
            }
        }else if(observable.getLblClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)){
            buttonVisit = true;
            calculatingDetails();
        }else if(observable.getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)){
            buttonVisit = true;
            calculatingDetails();
        }
         if(CommonUtil.convertObjToDouble(lblAgentCommisionRecoveredValue.getText()) > 0){
             double commAmount = CommonUtil.convertObjToDouble(lblAgentCommisionRecoveredValue.getText());
             double disbAmount = CommonUtil.convertObjToDouble(lblClDisbursalValue.getText());
             disbAmount-=commAmount;
             lblClDisbursalValue.setText(CommonUtil.convertObjToStr(disbAmount));
             calculatingDetails();
         }
         // Added by nithya on 11-10-2018 for KD 229 - Rd closing misc charge collecting issue
         if (behavesLike != null && (behavesLike.equals("RECURRING") || behavesLike.equals("DAILY"))) { // Added daily type by nithya on 09-12-2019 for KD-1046 Related to daily deposit closing with closing charge
            if (tableFlag == true) {                
                accClosingCharges();
                transactionUI.setButtonEnableDisable(true);
                transactionUI.cancelAction(false);
                transactionUI.resetObjects();
            }
        }
         // End
         
//         lockAccounts();
         
    }//GEN-LAST:event_btnTableOkActionPerformed
    
    private String roundOffAmt(String amtStr, String method) throws Exception {
           if (amtStr != null) {
            String amt = amtStr;
            DecimalFormat d = new DecimalFormat();
            d.setMaximumFractionDigits(2);
            d.setDecimalSeparatorAlwaysShown(true);
            if (amtStr != null && !amtStr.equals("")) {
                amtStr = d.parse(d.format(new Double(amtStr).doubleValue())).toString();
            }
            Rounding rd = new Rounding();
            int pos = amtStr.indexOf(".");
            long intPart = 0;
            long decPart = 0;
            if (pos >= 0) {
                intPart = new Long(amtStr.substring(0, pos)).longValue();
                decPart = new Long(amtStr.substring(pos + 1)).longValue();
            } else {
                if (amtStr != null && !amtStr.equals("")) {
                    intPart = new Long(amtStr).longValue();
                }
            }
            if (method.equals(NEAREST)) {
                decPart = rd.getNearest(decPart, 10);
                amtStr = intPart + "." + decPart;
            } else if (method.equals(LOWER)) {
                decPart = rd.lower(decPart, 10);
                amtStr = intPart + "." + decPart;
            } else if (method.equals(HIGHER)) {
                decPart = rd.higher(decPart, 10);
                amtStr = intPart + "." + decPart;
            } else if (method.equals(NO_ROUND_OFF)) {
//            decPart = rd.higher(decPart,10);
                // amtStr = intPart+"."+decPart;
                if (!amt.equals("")) {
                    amtStr = df.format(Double.parseDouble(amt));
                } else {
                    amtStr = amt;
                }
            }
        }
        return amtStr;
    }
    private void updateDepositDetails(){
        this.lblInterestPaymentFrequencyValue.setText(observable.getIntPaymentFreq());
        this.lblRateOfInterestValue.setText(observable.getRateOfInterest());
//        this.lblMVvalue.setText(observable.getPrinicipal());
        this.lblMVvalue.setText(observable.getMaturityValue());
        this.lblPeriodValue.setText(observable.getPeriod());
        this.lblPrincipalValue.setText(observable.getPrinicipal());
        this.lblMaturityDateValue.setText(observable.getMaturityDate());
        this.lblDepositDateValue.setText(observable.getDepositDate());
    }
    private void updatePresentDepositDetails(){
        this.lblBalanceDepositValue.setText(observable.getBalanceDeposit());
        this.lblBalanceValue.setText(observable.getBalance());
        this.lblInterestCreditedValue.setText(observable.getIntCr());
        this.lblInterestDrawnValue.setText(observable.getIntDrawn());
        this.lblLienFreezeAmountValue.setText(observable.getLienFreezeAmt());
        this.lblTDSCollectedValue.setText(observable.getTdsCollected());
        this.lblWithDrawnAmountValue.setText(observable.getWithDrawn());
        this.lblLastInterestApplDateValue.setText(observable.getLastIntAppDate());
        this.lblInstPaidValue.setText(observable.getNoInstPaid());
        this.lblInstDueValue.setText(observable.getNoInstDue());
    }
    private void updateClosingPositionDetails(){
        this.lblInterestCrValue.setText(observable.getClosingIntCr());
        this.lblActualPeriodRunValue.setText(observable.getActualPeriodRun());
        this.lblInterestDrValue.setText(observable.getClosingIntDb());
        String addIntamt = observable.getLblAddIntRtAmtVal();
        double totamt = 0;
        if (addIntamt != null) {
            totamt = CommonUtil.convertObjToDouble(addIntamt) + CommonUtil.convertObjToDouble(observable.getClosingDisbursal());
            this.lblClDisbursalValue.setText(CommonUtil.convertObjToStr(totamt));
        } else {
            this.lblClDisbursalValue.setText(observable.getClosingDisbursal());
        }
        this.lblClTDSCollectedValue.setText(observable.getClosingTds());
        this.lblPayRecValue.setText(observable.getPayReceivable());
        observable.setLblReceive(lblPayRec.getText());
        this.lblRateApplicableValue.setText(observable.getRateApplicable());
        this.lblPenaltyPenalRateValue.setText(observable.getPenaltyPenalRate());
        this.lblDelayedAmountValue.setText(observable.getChargeAmount());
        this.lblDelayedInstallmentValue.setText(observable.getDelayedInstallments());
        this.rdoPenaltyRateApplicbleYes.setSelected(observable.isRdoPenaltyPenalRate_yes());
        this.rdoPenaltyRateApplicbleNo.setSelected(observable.isRdoPenaltyPenalRate_no());
        this.rdoTransfer_Out_Yes.setSelected(observable.isRdoTransfer_Yes());
        this.rdoTransfer_In_No.setSelected(observable.isRdoTransfer_No());
        this.rdoLTDDeposit.setSelected(observable.isRdoTypeOfDeposit_No());
        this.rdoRegularDeposit.setSelected(observable.isRdoTypeOfDeposit_Yes());
        if (rdoTransfer_In_No.isSelected() == true) {
            if (observable.getMaturityDate() != null && !observable.getMaturityDate().equals("")
                    && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getMaturityDate()), currDt) >= 0) {
                lblPreClosingRate.setText("Closing Rate");
                lblPreClosingPeriod.setText("Closing Date");
            } else {
                lblPreClosingRate.setText("Premature Closing Rate");
                lblPreClosingPeriod.setText("Premature Closing Date");
            }
        } else if (rdoTransfer_Out_Yes.isSelected() == true) {
            lblPreClosingPeriod.setText("Transfer Out Date");
            lblClDisbursal.setText("Transfer Out Disbursal");
        }
        // lblPreClosingRateValue
        this.lblPreClosingRateValue.setText(observable.getPrematureClosingRate());
        this.lblPreClosingPeriodValue.setText(observable.getPrematureClosingDate());
        lblAgentCommisionRecoveredValue.setText(observable.getAgentCommisionRecoveredValue());
        //Added by Chithra on 21-04-14
        this.lblAddIntRtAmtVal.setText(observable.getLblAddIntRtAmtVal());
        this.lblAddIntrstRteVal.setText(observable.getLblAddIntrstRteVal());
        this.lblMaturityPeriod.setText(observable.getLblMaturityPeriod());
        //End on 21-04-14
    }
    
    private void updateDepositInfo(){
        this.lblCategoryValue.setText(observable.getCategory());
        this.lblConstitutionValue.setText(observable.getConstitution());
        this.lblDepositAccountNameValue.setText(observable.getDepositActName());
        
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        HashMap whereMap = new HashMap();
        //if (behavesLike.equals("DAILY")) {
        tabClosingType.add(chargePan);
        chargePan.setName("Charge Details");
        //}
        tableExsistOrNot();
        String prod = (String) cboProductId.getSelectedItem();
        String prodId = (String) ((ComboBoxModel) this.cboProductId.getModel()).getKeyForSelected();
        List prodDesclst = ClientUtil.executeQuery("getSchemeNameTD", whereMap);
        if (prodDesclst != null && prodDesclst.size() > 0) {
            whereMap = (LinkedHashMap) prodDesclst.get(0);
            prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
        }
        prodDesc = prod;
        if (prodId != null && prodId.length() > 0) {
            HashMap chargeHead = new HashMap();
            chargeHead.put("SCHEME_ID", prodDesc);
            if (transNew) {
                chargeHead.put("DEDUCTION_ACCU", "C");
            } else {
                //whereMap.put("DEDUCTION_ACCU", "C");//20-04-2014
                chargeHead.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
            }
            HashMap roundMap = new HashMap();
            roundMap.put("PROD_ID", prodId);
            List roundgList = ClientUtil.executeQuery("getRoungOffTypeInterest", roundMap);
            if (!roundgList.isEmpty()) {
                roundMap = (HashMap) roundgList.get(0);
                observable.setInterestRound(roundMap.get("INT_ROUNDOFF_TERMS").toString());
            }
            observable.setProdID(prodId);
            String actHead = observable.getAccountHead(prodId);
            this.lblAccountHeadValue.setText(actHead);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID",prodId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            if(lst != null && lst.size()>0){
                prodMap = (HashMap)lst.get(0);
                behavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
                    lblDelayedAmount.setVisible(true);
                    lblDelayedInstallments.setVisible(true);
                    lblDelayedInstallmentValue.setVisible(true);
                    lblDelayedAmountValue.setVisible(true);
                    lblBalanceDeposit.setText("Installment Amount");
                }else{
                    lblDelayedAmount.setVisible(false);
                    lblDelayedInstallments.setVisible(false);
                    lblBalanceDeposit.setText("Balance Deposit");
                    lblDelayedInstallmentValue.setVisible(false);
                    lblDelayedAmountValue.setVisible(false);
                }
                if ((prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE"))) {
                    HashMap mapPremClo = new HashMap();
                    mapPremClo.put("PROD_ID", prodId);
                    List lstPremClos = ClientUtil.executeQuery("getSelPremClos", mapPremClo);
                    if (lstPremClos.size() > 0 && lstPremClos != null) {
                        mapPremClo = new HashMap();
                        mapPremClo = (HashMap) lstPremClos.get(0);
                        premClos = mapPremClo.get("PREMATURE_CLOSURE_WITH_SI").toString();
                        observable.setPremClos(premClos);
                    }
                } else {
                    observable.setPremClos("N");
                }
                if(prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")|| prodMap.get("BEHAVES_LIKE").equals("RECURRING"))
                    lblInterestCredited.setText("Interest Credited");
                else
                    lblInterestCredited.setText("Interest Provision");
                
                if(prodMap.get("BEHAVES_LIKE").equals("DAILY")){
                    lblAgentCommisionRecovered.setVisible(true);
                    lblAgentCommisionRecoveredValue.setVisible(true);
                }else{
                    lblAgentCommisionRecovered.setVisible(false);
                    lblAgentCommisionRecoveredValue.setVisible(false);
                }
              ///  cboProductId.setEnabled(false);
            }
            String atr = txtDepositNo.getText();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                atr = CommonUtil.convertObjToStr(getSelectedBranchID()) + observable.getProdID();
                txtDepositNo.setText(atr);
            }
        } else
            observable.resetForm();
    }//GEN-LAST:event_cboProductIdActionPerformed
    
    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        this.txtDepositNo.setEnabled(true);
    }//GEN-LAST:event_btnDepositNoActionPerformed

    private void lblAgentCommisionRecoveredValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAgentCommisionRecoveredValueMouseClicked
        // TODO add your handling code here:
        editCommisionAmount();
    }//GEN-LAST:event_lblAgentCommisionRecoveredValueMouseClicked

    private void lblAgentCommisionRecoveredValuePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_lblAgentCommisionRecoveredValuePropertyChange
        // TODO add your handling code here:lblBalanceValue  lblAgentCommisionRecoveredValue
        if (amountMap.containsKey("TITLE") && amountMap.get("TITLE") != null) {
            if (amountMap.get("TITLE").equals("Commision Recovered Amount")) {
                double commAmount = CommonUtil.convertObjToDouble(lblAgentCommisionRecoveredValue.getText());
                double disbAmount = CommonUtil.convertObjToDouble(lblBalanceValue.getText());
                disbAmount -= commAmount;
                lblClDisbursalValue.setText(CommonUtil.convertObjToStr(disbAmount));
                observable.setClosingDisbursal(CommonUtil.convertObjToStr(disbAmount));
                setCallingAmt(CommonUtil.convertObjToStr(disbAmount));
            }
        }
    }//GEN-LAST:event_lblAgentCommisionRecoveredValuePropertyChange

    private void lblPayRecValuePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_lblPayRecValuePropertyChange
        // TODO add your handling code here:
        if(!behavesLike.equals("") && behavesLike != null && behavesLike.equals("DAILY")){
        if (amountMap.containsKey("TITLE") && amountMap.get("TITLE") != null) {
            if (amountMap.get("TITLE").equals("Payable/Receivable")) {
                double payRecAmount = CommonUtil.convertObjToDouble(lblPayRecValue.getText());
                double disbAmount = CommonUtil.convertObjToDouble(lblBalanceValue.getText());
                disbAmount += payRecAmount;
                lblClDisbursalValue.setText(CommonUtil.convertObjToStr(disbAmount));
                observable.setClosingDisbursal(CommonUtil.convertObjToStr(disbAmount));
                setCallingAmt(CommonUtil.convertObjToStr(disbAmount));
            }
        }
        }
    }//GEN-LAST:event_lblPayRecValuePropertyChange
// Added by nithya on 03-10-2017 for group deposit changes
    private void btnApplyPenalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyPenalActionPerformed
        // TODO add your handling code here:
        //System.out.println("Txt mmmmmmmmmmmmmmm===============");
        if (tableFlag == true) {
            accClosingCharges();          
            transactionUI.setButtonEnableDisable(true);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();           
        }     
    }//GEN-LAST:event_btnApplyPenalActionPerformed

    private void lblDelayedAmountValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDelayedAmountValueMouseClicked
        // TODO add your handling code here:
        editRDDelayedAmount();
    }//GEN-LAST:event_lblDelayedAmountValueMouseClicked
    
    private void updateCustomerInfo(){
        lblSettlementModeValue.setText(observable.getModeOfSettlement());
        if(observable.getCustomerID() != null && !observable.getCustomerID().equals(""))
            custDetailsUI.updateCustomerInfo(observable.getCustomerID()) ;
    }

    private void updateAuthorizeStatus(String authorizeStatus) {
        //        String status = transactionUI.getCallingStatus();
        if(authorizeStatus.equals("AUTHORIZED"))
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
        
        if(authorizeStatus.equals("REJECTED"))
            observable.setResult(ClientConstants.ACTIONTYPE_REJECT);
        
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        //Changed By Suresh
//        String isAllTabsVisited = tabClosingType.isAllTabsVisited();
        
        //--- If all the tabs are not visited, then show the Message
        if (viewType == AUTHORIZE && isFilled){
//            if(isAllTabsVisited.length()>0){
//                ClientUtil.displayAlert(isAllTabsVisited);
//                return;
//            }else{
                tabClosingType.resetVisits();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("ACCOUNTNO", observable.getTxtDepositNo() + "_" + observable.getSubDepositNo()) ;
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorizeMap.put("SINGLE_TRANS_ID", observable.getTxtSingleTransId()) ;
                HashMap getStatusMap = new HashMap();
                HashMap accountMap = new HashMap();
                getStatusMap.put("DEPOSIT_NO",observable.getTxtDepositNo());
                List lst = ClientUtil.executeQuery("getLoanAccNoForDeposit", getStatusMap);
                if(lst !=null && lst.size()>0){
                    getStatusMap = (HashMap)lst.get(0);
                    String status = CommonUtil.convertObjToStr(getStatusMap.get("STATUS"));
                    HashMap loanMap = new HashMap();
                    loanMap.put("ACCT_NUM",getStatusMap.get("LIEN_AC_NO"));
                    lst = ClientUtil.executeQuery("getLoanAmountForLoans", loanMap);
                    if(lst != null && lst.size()>0 && status.equals("LIEN")){
                        loanMap = (HashMap)lst.get(0);
                        HashMap closingMap = new HashMap();
                        accountClosing = new AccountClosingUI("TermLoan");
                        closingMap.put("TOTAL_AMOUNT",loanMap.get("LOAN_BALANCE_PRINCIPAL"));
                        closingMap.put("ACCOUNTNO",getStatusMap.get("LIEN_AC_NO"));
                        closingMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                        accountMap = accountClosing.btnAuthorizeDep(closingMap);
                        observable.setLtdClosingMap(accountMap);
                    }
                }
                authorize(authorizeMap);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                accountMap = new HashMap();
                observable.setLtdClosingMap(accountMap);
//            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getDepositAccountCloseCashierAuthorizeTOList");
            }
             else
            mapParam.put(CommonConstants.MAP_NAME, "getDepositAccountCloseAuthorizeTOList");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //            lblStatus.setText(observable.getLblStatus());
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            btnSave.setEnabled(false);
        } 
    }
    
    public void authorize(HashMap map) {
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            //observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
                        //Added by sreekrishnan
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                {
                    try {
                        observable.doAction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Deposit Account Closing");
                }
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                }
                btnCancelActionPerformed(null);
            }
        }
    }
    
    private void callView(int viewType){
        HashMap viewMap = new HashMap();
        this.viewType=viewType;
        if(observable.getActionType() ==ClientConstants.ACTIONTYPE_NEW){
            String prodId=(String)((ComboBoxModel)this.cboProductId.getModel()).getKeyForSelected();
            if(prodId !=null && prodId.length()>0 && !prodId.equals("")){
                if(rdoRegularDeposit.isSelected() == true && viewType==DEPOSIT_ACCT){
                    viewMap.put(CommonConstants.MAP_NAME,"getRegularDepositAccounts");
                }
                if(rdoLTDDeposit.isSelected() == true && viewType == LTD_DEP_LIST){
                    viewMap.put(CommonConstants.MAP_NAME,"getSelectLTDDeposits");  
                }
    //                viewMap.put("CURR_DT",currDt.clone());
    //            }
            }else{
                ClientUtil.displayAlert("Choose Product Id First...");
                return;
            }
        }
        if (viewType==CLOSED_ACCT || viewType==ClientConstants.ACTIONTYPE_VIEW){
            ArrayList lst = new ArrayList();
            lst.add("DEPOSIT_ACT_NUM");
            //            lst.add("DEPOSIT_SUB_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //            HashMap whereMap = new HashMap();
            //            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            //            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            //            whereMap = null;
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getClosedDepositAccounts");
        }
        HashMap where = new HashMap();
        if (viewType == TRANSFERRING_BRANCH) {
            where.put("CURRENT_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBranchList");
        }
        where.put("PRODID", ((ComboBoxModel) this.cboProductId.getModel()).getKeyForSelected());
        if (rdoLTDDeposit.isSelected() == true && viewType == LTD_DEP_LIST) {
            where.put("CURR_DT", currDt);
        }
        viewMap.put(CommonConstants.MAP_WHERE, where);
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object obj){
        System.out.println("fillData Starting : " + obj);
        try {
            HashMap hashMap = (HashMap) obj;
            Lien_Accno = "";
            setModified(true);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", hashMap.get("DEPOSIT_ACT_NUM") + "_1");
                List authList = ClientUtil.executeQuery("getPendingTransAuthList", hmap);
                if (authList != null && authList.size() > 0) {
                    hmap = (HashMap) authList.get(0);
                    String tranMode = CommonUtil.convertObjToStr(hmap.get("TRANS_MODE"));
                    ClientUtil.displayAlert(tranMode + "transaction is pending for authorization. Can not proceed");
                    btnCancelActionPerformed(null);
                }
                // Added by nithya on 06-10-2016
                HashMap lockerMap = new HashMap();
                lockerMap.put("ACT_NUM", hashMap.get("DEPOSIT_ACT_NUM"));
                List lockerlst = ClientUtil.executeQuery("getLockerDetails", lockerMap);
                if (lockerlst != null && lockerlst.size() > 0) {
                    lockerMap = (HashMap) lockerlst.get(0);
                    ClientUtil.showMessageWindow("Locker is Marked Closing is Not allowing...");
                    btnCancelActionPerformed(null);
                    return;
                }
                // End  
                //Added by srekrishnan
                HashMap freezeMap = new HashMap();
                freezeMap.put("DEPOSIT_NO",hashMap.get("DEPOSIT_ACT_NUM"));
                List lst = ClientUtil.executeQuery("getFreezeAccNoForDep", freezeMap);
                if(lst!=null && lst.size()>0){
                    freezeMap = (HashMap)lst.get(0);
                    ClientUtil.showMessageWindow("Account frozen. Closer not allowed");
                    btnCancelActionPerformed(null);
                    return;
                }
                
                HashMap remarksMap = new HashMap();
                remarksMap.put("DEPOSIT_NO", hashMap.get("DEPOSIT_ACT_NUM"));
                List remLst = ClientUtil.executeQuery("getremarksForDepositClosing", remarksMap);
                if (remLst != null && remLst.size() > 0) {
                    remarksMap = (HashMap) remLst.get(0);
                    if (remarksMap.containsKey("REMARKS") && remarksMap.get("REMARKS") != null) {
                        if (CommonUtil.convertObjToStr(remarksMap.get("REMARKS")).length() > 0) {
                            ClientUtil.showMessageWindow("Remarks : " + CommonUtil.convertObjToStr(remarksMap.get("REMARKS")));
                        }
                    }
                }
                
                // Added by nithya on 28-05-2019 for KD-508 MINOR DEPOSIT ACCOUNT CLOSING- NEED A WARNING MESSAGE
                HashMap actTypeMap = new HashMap();
                actTypeMap.put("DEPOSIT_NO", hashMap.get("DEPOSIT_ACT_NUM"));
                actTypeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                List actTypeLst = ClientUtil.executeQuery("checkMinorDeposit", actTypeMap);
                if (actTypeLst != null && actTypeLst.size() > 0) {
                    actTypeMap = (HashMap) actTypeLst.get(0);
                    if (actTypeMap.containsKey("ACT_TYPE") && actTypeMap.get("ACT_TYPE") != null && CommonUtil.convertObjToStr(actTypeMap.get("ACT_TYPE")).equalsIgnoreCase("MINOR")) {
                        ClientUtil.showMessageWindow("Customer is Minor");
                    }
                }
                //End
                
            }     
            if(hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag=1;
                            
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag=1;
                            
            }
            if (hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                    fromManagerAuthorizeUI = true;
                    ManagerauthorizeListUI = (AuthorizeListDebitUI) hashMap.get("PARENT");
                    hashMap.remove("PARENT");
                    viewType = AUTHORIZE;
                    observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                    observable.setStatus();
                    btnReject.setEnabled(false);
                    rejectFlag=1;
            }
            
            if(hashMap.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")){
            fromAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            viewType =AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            //btnSaveDisable();
            }
            if (hashMap.containsKey("FROM_SMART_CUSTOMER_UI")) {
                fromSmartCustUI = true;
                smartUI = (SmartCustomerUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                btnNewActionPerformed(null);
                //cboProductId.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                //cboProductIdActionPerformed(null); 
                txtDepositNo.setText(CommonUtil.convertObjToStr(hashMap.get("DEPOSIT_ACT_NUM")));
                txtDepositNoFocusLost(null);
                observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                observable.setStatus();
                rdoTransfer_In_No.setSelected(true);
                rdoTransfer_In_NoActionPerformed(null);
                observable.setSelectedBranchID(getSelectedBranchID());
                //btnTableOkActionPerformed(null);
                //btnSaveDisable();
            }
//            Changed for implementiung floating rates for call deposit
            if(behavesLike.equals("FLOATING_RATE")){
                HashMap schemeMap = new HashMap();
                schemeMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                List floatingRateLst = ClientUtil.executeQuery("getDepProdDetails", schemeMap);
                if(floatingRateLst != null && floatingRateLst.size() > 0){
                    HashMap floatingRtMap = new HashMap();
                    floatingRtMap = (HashMap)floatingRateLst.get(0);
                    if(floatingRtMap.containsKey("FLOATING_WITH_PERIOD")){
                        if(CommonUtil.convertObjToStr(floatingRtMap.get("FLOATING_WITH_PERIOD")).equals("N")){
                            flPtWithoutPeriod = true;
                            observable.setFlPtWithoutPeriod(flPtWithoutPeriod);
                        }
                    }
                }
            }
            observable.setViewTypeDet(viewType);
            if (viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST || viewType == TRANSFERRING_BRANCH) {
                downCancel();
            }
            HashMap paramMap = new HashMap();
            if (viewType == DEPOSIT_ACCT || viewType == CLOSED_ACCT || viewType == LTD_DEP_LIST || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
                if (hashMap.containsKey("SINGLE_TRANS_ID") && hashMap.get("SINGLE_TRANS_ID") != null) {
                    observable.setTxtSingleTransId(CommonUtil.convertObjToStr(hashMap.get("SINGLE_TRANS_ID")));
                }
                this.txtDepositNo.setText(CommonUtil.convertObjToStr(hashMap.get("DEPOSIT_ACT_NUM")));
                paramMap.put("DEPOSITNO", this.txtDepositNo.getText());
                if (hashMap.containsKey("MATURITY_AMT")) {
                    maturityAmount = CommonUtil.convertObjToDouble(hashMap.get("MATURITY_AMT")).doubleValue();
                }
                paramMap.put(CommonConstants.PRODUCT_ID, hashMap.get("PROD_ID"));
                observable.setTxtDepositNo(CommonUtil.convertObjToStr(hashMap.get("DEPOSIT_ACT_NUM")));
                cboProductId.setSelectedItem(observable.getCbmProductId().getDataForKey(hashMap.get("PROD_ID")));
                observable.setLienAmount(CommonUtil.convertObjToDouble(transDetailsUI.getLienAmount()).doubleValue());
                observable.setFreezeAmount(CommonUtil.convertObjToDouble(transDetailsUI.getFreezeAmount()).doubleValue());              
                if(hashMap.containsKey("DEPOSIT_SUB_NO")){
                    paramMap.put("DEPOSITSUBNO", CommonUtil.convertObjToInt(hashMap.get("DEPOSIT_SUB_NO")));
                }
                observable.setTxtDepositNo(txtDepositNo.getText());
                boolean acNoFlag = observable.getSubDepositNos(paramMap);
                if (acNoFlag == true) {
                    btnCancelActionPerformed(null);
                }
                //                this.tblSubDeposits.setModel(observable.getTbmSubDeposit());
                HashMap freezeMap = new HashMap();
                freezeMap.put("DEPOSIT_NO", observable.getTxtDepositNo());
                List lst = ClientUtil.executeQuery("getFreezeAccNoForDep", freezeMap);
                if (lst != null && lst.size() > 0) {
                    freezeMap = (HashMap) lst.get(0);
                    ClientUtil.showMessageWindow("Account frozen. Closer not allowed");
                    btnCancelActionPerformed(null);
                    return;
                }
                HashMap lockerMap = new HashMap();
                lockerMap.put("ACT_NUM", observable.getTxtDepositNo());
                lst = ClientUtil.executeQuery("getLockerDetails", lockerMap);
                if (lst != null && lst.size() > 0) {
                    lockerMap = (HashMap) lst.get(0);
                    ClientUtil.showMessageWindow("Locker is Marked Closing is Not allowing...");
                    btnCancelActionPerformed(null);
                    return;
                }
            }
            if(viewType == DEPOSIT_ACCT || viewType == CLOSED_ACCT || viewType == LTD_DEP_LIST || viewType == AUTHORIZE ||
            viewType == ClientConstants.ACTIONTYPE_VIEW || viewType == TRANSFERRING_BRANCH){
                if(viewType != TRANSFERRING_BRANCH){
                    if(viewType == CLOSED_ACCT || viewType == AUTHORIZE || viewType ==ClientConstants.ACTIONTYPE_VIEW){
                        HashMap editMap = new HashMap();
                        editMap.put("DEPOSIT_NO",txtDepositNo.getText());
                        List lstTrans = ClientUtil.executeQuery("getTransferOutDepositFlag", editMap);
                        if(lstTrans !=null && lstTrans.size()>0){
                            editMap = (HashMap)lstTrans.get(0);
                            observable.setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                            if(editMap.get("TRANS_OUT").equals("N"))
                                observable.setRdoTransfer_No(true);
                            if(editMap.get("TRANS_OUT").equals("Y"))
                                observable.setRdoTransfer_Yes(true);
                            if(editMap.get("TRANS_OUT").equals("NO")){
                                observable.normalClosing = true;
                            }
                        }
                    }
                }
                transactionUI.displayMode();
            }
            if(viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW){
                this.updateDepositInfo();
                this.updateCustomerInfo();
                paramMap.put("DEPOSITNO", this.txtDepositNo.getText());
                paramMap.put(CommonConstants.PRODUCT_ID, hashMap.get("PROD_ID"));
                txtDepositNo.setText(CommonUtil.convertObjToStr(hashMap.get("DEPOSIT_ACT_NUM")));
                System.out.println("fill Data : " +paramMap);
            }
            if(viewType == LTD_DEP_LIST || viewType == TRANSFERRING_BRANCH){
                if(viewType == TRANSFERRING_BRANCH){
                    txtTransferringBranch.setText(CommonUtil.convertObjToStr(hashMap.get("BRANCH CODE")));
                    lblBranchValue.setText(CommonUtil.convertObjToStr(hashMap.get("BRANCH NAME")));
                }
                calculateMaturity();//new mode setting closing type...
            }//this.tblSubDeposits.getRowCount() > 0 &&
            if((viewType == CLOSED_ACCT || viewType == AUTHORIZE) || viewType == ClientConstants.ACTIONTYPE_VIEW){
                calculateMaturity();//authorize_mode,edit_mode,delete_mode,enquiry_mode setting closingtype...
                isFilled = true;
                HashMap editMap = new HashMap();
                editMap.put("DEPOSIT_NO",observable.getTxtDepositNo());
                List lst = ClientUtil.executeQuery("getPenalYesorNoDetails", editMap);
                if(lst !=null && lst.size()>0){
                    editMap = (HashMap)lst.get(0);
                    observable.setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                    double penalAmt = CommonUtil.convertObjToDouble(editMap.get("PENAL_RATE")).doubleValue();
                    double penalInt = CommonUtil.convertObjToDouble(editMap.get("CURR_RATE_OF_INT")).doubleValue();
                    observable.setPrematureClosingRate(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                    observable.setRateApplicable(CommonUtil.convertObjToStr(editMap.get("PENAL_RATE")));
                    observable.setPenaltyPenalRate(String.valueOf(penalAmt));
                    if(penalAmt == 0){
                        observable.setRateApplicable(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                    }else{
                        penalInt = penalInt + penalAmt;
                        observable.setRateApplicable(String.valueOf(penalInt));
                    }
                    observable.setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                }
                btnTableOkActionPerformed(null);
                
                // For printing added by Rajesh
                String displayStr = "";
                String oldBatchId = "";
                String newBatchId = "";
                int CreditcashCount = 0;
                int DebitcashCount = 0;
                String actNum = observable.getTxtDepositNo();
                HashMap transDetMap = new HashMap();
                HashMap transMap = new HashMap();
                transMap.put("DEPOSIT_NO",actNum+"_1");
                transMap.put("CURR_DT", currDt);
                lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                HashMap transIdMap = new HashMap();
                if(lst !=null && lst.size()>0){
                    displayStr += "Transfer Transaction Details...\n";
                    for(int i = 0;i<lst.size();i++){
                        transMap = (HashMap)lst.get(i);
                        displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                        "   Batch Id : "+transMap.get("BATCH_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                            "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                        }else{
                            displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                            "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                        }
                        //transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                       transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                        oldBatchId = newBatchId;
                    }
                }
                
                // In case of Closing amount credited to TL or AD
                System.out.println("observable.getTermLoanAdvanceActNum()" + observable.getTermLoanAdvanceActNum());
                HashMap lienMap1 = new HashMap();
                String loanNum = "";
                lienMap1.put("DEPOSIT_NO", txtDepositNo.getText());
                lst = ClientUtil.executeQuery("getUnlienedDeposits", lienMap1);
                if (lst != null && lst.size() > 0 && viewType == AUTHORIZE) {
                    lienMap1 = (HashMap) lst.get(0);
                    loanNum = CommonUtil.convertObjToStr(lienMap1.get("LIEN_AC_NO"));
                }
                if (CommonUtil.convertObjToStr(observable.getTermLoanAdvanceActNum()).length()>0 || (loanNum!=null && loanNum.length()>0) ) {
                    transMap.put("DEPOSIT_NO",observable.getTermLoanAdvanceActNum());
                    if(loanNum!=null && loanNum.length()>0 )
                        transMap.put("DEPOSIT_NO",loanNum);
                    transMap.put("CURR_DT", currDt);
                    lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                    if(lst !=null && lst.size()>0){
                        for(int i = 0;i<lst.size();i++){
                            transMap = (HashMap)lst.get(i);
                            displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                            "   Batch Id : "+transMap.get("BATCH_ID")+
                            "   Trans Type : "+transMap.get("TRANS_TYPE");
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            if(actNum != null && !actNum.equals("")){
                                displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                                "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                            }else{
                                displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                                "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                            }
                          //  transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                            oldBatchId = newBatchId;
                        }
                    }
                }
                
                transMap.put("DEPOSIT_NO",observable.getTxtDepositNo()+"_1");
                lst = ClientUtil.executeQuery("getRemitIssueAuthDetails", transMap);
                if(lst!=null && lst.size()>0){
                    transMap = (HashMap)lst.get(0);
                    transMap.put("CURR_DT", currDt);
                    transMap.put("DEPOSIT_NO",transMap.get("VARIABLE_NO"));
                    lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                    if(lst !=null && lst.size()>0){
                        for(int i = 0;i<lst.size();i++){
                            transMap = (HashMap)lst.get(i);
                            displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                            "   Batch Id : "+transMap.get("BATCH_ID")+
                            "   Trans Type : "+transMap.get("TRANS_TYPE");
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            if(actNum != null && !actNum.equals("")){
                                displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                                "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
                            }else{
                                displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                                "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                            }
                            oldBatchId = newBatchId;
                        }
                    }
                }
                transMap = new HashMap();
                //            transMap.put("DEPOSIT_NO",actNum+"_1");
                //            transMap.put("CURR_DT", currDt);
                //            lst = ClientUtil.executeQuery("getCashTransAuthDetails", transMap);
                transMap.put("BATCH_ID",observable.getTxtDepositNo()+"_1");
                transMap.put("TRANS_DT", currDt);
                transMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                transMap.put("AUTH_REJ_STATUS","AUTH_REJ_STATUS");
                lst = ClientUtil.executeQuery("getCashDetails", transMap);
                if(lst !=null && lst.size()>0){
                    displayStr += "Cash Transaction Details...\n";
                    for(int i = 0;i<lst.size();i++){
                        transMap = (HashMap)lst.get(i);
                        displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
                            "   Deposit Amount :  "+transMap.get("AMOUNT")+"\n";
                        }else{
                            if(transMap.get("TRANS_TYPE").equals("DEBIT")){
                                displayStr +="   Ac Hd Desc :  "+transMap.get("AC_HD_ID")+
                                "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
                            }else{
                                displayStr +="   Ac Hd Desc :  "+transMap.get("AC_HD_ID")+
                                "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
                            }
                        }
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                        transDetMap.put(transMap.get("SINGLE_TRANS_ID"),transMap.get("TRANS_TYPE"));
                        if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
                            DebitcashCount++;
                        }
                        if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                            CreditcashCount++;
                        }
                    }
                }
                if (viewType == AUTHORIZE) {
                    if (!displayStr.equals("")) {
                        ClientUtil.showMessageWindow("" + displayStr);
                    }
                }
                if(viewType != AUTHORIZE ){
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        String reportName = null;
                        TTIntegration ttIntgration = null;
                        HashMap printParamMap = new HashMap();
                        printParamMap.put("TransDt", currDt);
                        printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        Object keys[] = transIdMap.keySet().toArray();
                        for (int i = 0; i < keys.length; i++) {
                            printParamMap.put("TransId", keys[i]);
                            ttIntgration.setParam(printParamMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                reportName = "ReceiptPayment";
                                ttIntgration.integrationForPrint(reportName, false);
                            }
                            //else if (CommonUtil.convertObjToStr(transDetMap.get(keys[i])).equals("CREDIT")) {
                            //    reportName = "CashReceipt";
                            //} else {
                            //    reportName = "CashPayment";
                            //}
                            //ttIntgration.integrationForPrint(reportName, false);


                            if (CreditcashCount > 0) {
                                reportName = "CashReceipt";
                                System.out.println("CashReceipt@@@@@" + reportName);
                                ttIntgration.integrationForPrint(reportName, false);
                            }

                            if (DebitcashCount > 0) {
                                reportName = "CashPayment";
                                System.out.println("CashPayment@@@@@" + reportName);
                                ttIntgration.integrationForPrint(reportName, false);
                            }
                        }
                    }
                }  
                if(viewType == AUTHORIZE){
                    transactionUI.setCallingUiMode(AUTHORIZE);
                    btnTableOk.setEnabled(true);
                    HashMap lienMap = new HashMap();
                    lienMap.put("DEPOSIT_NO",txtDepositNo.getText());
                    lst = ClientUtil.executeQuery("getUnlienedDeposits", lienMap);
                    if(lst !=null && lst.size()>0){
                        lienMap = (HashMap)lst.get(0);
                        String loanNo = CommonUtil.convertObjToStr(lienMap.get("LIEN_AC_NO"));
                        lienMap.put("ACCT_NUM",lienMap.get("LIEN_AC_NO"));
                        lst = ClientUtil.executeQuery("getLoanAmountForLTDDep", lienMap);
                        if(lst !=null && lst.size()>0){
                            lienMap = (HashMap)lst.get(0);
                            double balanceAmt = CommonUtil.convertObjToDouble(lienMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                            double credit = CommonUtil.convertObjToDouble(lienMap.get("SHADOW_CREDIT")).doubleValue();
                            double intAmt =CommonUtil.convertObjToDouble(lienMap.get("EXCESS_AMT")).doubleValue();
                            HashMap lonasBorrowMap = new HashMap();
                            lonasBorrowMap.put("ACT_NUM", loanNo);
                            lst = ClientUtil.executeQuery("getActDataTL", lonasBorrowMap);
                            if(lst!=null && lst.size()>0){
                                lonasBorrowMap = (HashMap)lst.get(0);
                                HashMap intMainMap = new HashMap();
                                intMainMap.put("PROD_ID", lienMap.get("PROD_ID"));
                                intMainMap.put("CATEGORY_ID", lonasBorrowMap.get("CATEGORY"));
                                intMainMap.put("AMOUNT", CommonUtil.convertObjToDouble(lonasBorrowMap.get("AMOUNT")));
                                intMainMap.put("DEPOSIT_DT", lonasBorrowMap.get("CREATE_DT"));
                                intMainMap.put("PRODUCT_TYPE","TL");
                                double penalInt = 0.0;
                                double rateDepInt = 0.0;
                                double rateOfInt = 0.0;
                                lst = ClientUtil.executeQuery("icm.getInterestRates", intMainMap);
                                if(lst!=null && lst.size()>0){
                                    intMainMap = (HashMap)lst.get(0);
                                    penalInt = CommonUtil.convertObjToDouble(intMainMap.get("ROI")).doubleValue();
                                    if(lblClosureTypeValue.equals(CommonConstants.PREMATURE_CLOSURE)){
                                        rateDepInt = CommonUtil.convertObjToDouble(lblPreClosingRateValue.getText()).doubleValue();
                                        rateOfInt = penalInt + rateDepInt;
                                    }else{
                                        rateDepInt = CommonUtil.convertObjToDouble(lblPreClosingRateValue.getText()).doubleValue();
                                        rateOfInt = rateDepInt + penalInt;
                                    }
                                }
                                double balance = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()).doubleValue();
                                double remain = balance - (credit+intAmt);
                                if(remain<=0){
                                    ClientUtil.showMessageWindow("<html>"+"Loan Account No : "+loanNo+
                                    "\nLoan Principal Amount : "+balanceAmt+
                                    "\nRate Of Interest      : "+rateOfInt+
                                    "\nInterest Amount       : "+intAmt+
                                    "\nLoan Total Amount     : "+(balanceAmt+intAmt)+
                                    "\nDeposit Amount on Maturity: "+CommonUtil.convertObjToDouble(observable.getClosingDisbursal()).doubleValue()+
                                    "\nRemaining Loan Amount : "+((credit+intAmt)-(balanceAmt+intAmt))*-1+
                                    "<b>\nLoan is not closed...</b>"+"</html>");
                                }else{
                                    ClientUtil.showMessageWindow("Loan Account No       : "+loanNo+
                                    "\nLoan Principal Amount : "+balanceAmt+
                                    "\nRate Of Interest      : "+rateOfInt+
                                    "\nInterest Amount       : "+intAmt+
                                    "\nLoan Total Amount     : "+(credit+intAmt)+
                                    "\nRemaining Amount      : "+remain);
                                }
                            }
                        }
                    }
                    observable.setDepositPenalAmt(transDetailsUI.getPenalAmount());
                    observable.setDepositPenalMonth(transDetailsUI.getPenalMonth());
                }
                tblSubDeposits.setRowSelectionInterval(0,0);
                observable.notifyObservers();
                if(observable.getPenaltyInt()!= null && observable.getTypeOfDep() !=null){
                    if(observable.getPenaltyInt().equals("Y")){
                        rdoPenaltyRateApplicbleYes.setSelected(true);
                        rdoPenaltyRateApplicbleYes.isSelected();
                    }
                    if(observable.getPenaltyInt().equals("N")){
                        rdoPenaltyRateApplicbleNo.setSelected(true);
                        rdoPenaltyRateApplicbleNo.isSelected();
                    }
                    if(observable.getTypeOfDep().equals("LTD")){
                        rdoLTDDeposit.setSelected(true);
                        rdoLTDDeposit.isSelected();
                        rdoLTDDeposit.setEnabled(false);
                        rdoRegularDeposit.setEnabled(false);
                    }
                    if(observable.getTypeOfDep().equals("NOLTD")){
                        rdoRegularDeposit.setSelected(true);
                        rdoRegularDeposit.isSelected();
                        rdoLTDDeposit.setEnabled(false);
                        rdoRegularDeposit.setEnabled(false);
                    }
                    if(observable.isRdoTransfer_Yes() == true && observable.normalClosing == false){
                        rdoTransfer_Out_Yes.setSelected(true);
                        rdoTransfer_In_No.setSelected(false);
                        txtTransferringBranch.setText("");
                        lblBranchValue.setText("");
                        lblTransferringBranch.setVisible(true);
                        panTransferringBranch.setVisible(true);
                        panTransferringBranch.setEnabled(true);
                        lblBranch.setVisible(true);
                        lblBranchValue.setVisible(true);
                        if(viewType == AUTHORIZE)
                            btnTransferringBranch.setEnabled(false);
                        lblRateApplicble.setVisible(false);
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                        HashMap transOutMap = new HashMap();
                        transOutMap.put("DEPOSIT_NO",txtDepositNo.getText());
                        List lstTrans = ClientUtil.executeQuery("getTransferOutDeposit", transOutMap);
                        if(lstTrans!=null && lstTrans.size()>0){
                            transOutMap = (HashMap)lstTrans.get(0);
                            txtTransferringBranch.setText(CommonUtil.convertObjToStr(transOutMap.get("TRANS_BRANCH_CODE")));
                            lblBranchValue.setText(CommonUtil.convertObjToStr(transOutMap.get("BRANCH_NAME")));
                        }
                    }else if(observable.isRdoTransfer_No() == true && observable.normalClosing == false){
                        rdoTransfer_In_No.setSelected(true);
                        rdoTransfer_Out_Yes.setSelected(false);
                        txtTransferringBranch.setText("");
                        lblBranchValue.setText("");
                        lblTransferringBranch.setVisible(false);
                        panTransferringBranch.setVisible(false);
                        panTransferringBranch.setEnabled(false);
                        lblBranch.setVisible(false);
                        lblBranchValue.setVisible(false);
                    }else if(observable.normalClosing == true){
                        rdoTransfer_In_No.setSelected(false);
                        rdoTransfer_Out_Yes.setSelected(false);
                        txtTransferringBranch.setText("");
                        lblBranchValue.setText("");
                        lblTransferringBranch.setVisible(false);
                        panTransferringBranch.setVisible(false);
                        panTransferringBranch.setEnabled(false);
                        lblBranch.setVisible(false);
                        lblBranchValue.setVisible(false);
                        lblRateApplicble.setVisible(false);
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                    }
                    //                    btnTableOkActionPerformed(null);
                }
                observable.setPenaltyInt("");
            }else if(viewType == DEPOSIT_ACCT){
                if(observable.getTypeOfDep().equals("LTD")){
                    rdoLTDDeposit.setSelected(true);
                    rdoLTDDeposit.isSelected();
                    rdoLTDDeposit.setEnabled(false);
                    rdoRegularDeposit.setEnabled(false);
                }else if(observable.getTypeOfDep().equals("NOLTD")){
                    rdoRegularDeposit.setSelected(true);
                    rdoRegularDeposit.isSelected();
                    rdoLTDDeposit.setEnabled(false);
                    rdoRegularDeposit.setEnabled(false);
                }
                HashMap lockmap = new HashMap();
                lockmap.put("ACCOUNTNO", observable.getTxtDepositNo());
                List lockList = ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
                lockmap = null;
                if (lockList != null && lockList.size() > 0) {
                    lockmap = (HashMap) lockList.get(0);
                    String lockStatus = CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
                    if (lockStatus.equals("Y")) {
                        ClientUtil.displayAlert("Account is locked");
                        txtDepositNo.setText("");
                    }
                }
            }
            if(rdoLTDDeposit.isSelected() ==true){
                HashMap balanceMap = new HashMap();
                balanceMap.put("DEPOSIT_NO",hashMap.get("DEPOSIT_ACT_NUM"));
                List lst = ClientUtil.executeQuery("getAvailableBalanceForDep", balanceMap);
                if(lst != null && lst.size()>0){
                    balanceMap = (HashMap)lst.get(0);
                    double availBal = CommonUtil.convertObjToDouble(balanceMap.get("AVAILABLE_BALANCE")).doubleValue();
                    double totalBal = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_BALANCE")).doubleValue();
                    HashMap accHeadMap = new HashMap();
                    accHeadMap.put("DEPOSIT_NO",hashMap.get("DEPOSIT_ACT_NUM"));
                    lst = ClientUtil.executeQuery("getAccountHeadForLTD",accHeadMap);
                    if(lst != null && lst.size()>0){
                        accHeadMap = (HashMap)lst.get(0);
                        if(accHeadMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS") && availBal<totalBal){
                            ClientUtil.showMessageWindow(resourceBundle.getString("lienAmount"));
                        }else if(!accHeadMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS")){
                            ClientUtil.showAlertWindow("Lien is marked. Cancel the lien and close the deposit...");
                            btnCancelActionPerformed(null);
                        }
                    }
                }
                if(viewType == CLOSED_ACCT){
                    ClientUtil.showAlertWindow("Can not Edit this Deposit Please Reject and do it fresh transaction...");
                    btnCancelActionPerformed(null);
                }
            }
            if(rdoRegularDeposit.isSelected() == true){
                HashMap ltdAccountsMap = new HashMap();
                ltdAccountsMap.put("DEPOSIT_NO",hashMap.get("DEPOSIT_ACT_NUM"));
                List lst = ClientUtil.executeQuery("getSelectUnAuthorizedRecords", ltdAccountsMap);
                if(lst!=null && lst.size()>0){
                    ClientUtil.showAlertWindow("Authorization pending in LTD Module either authorize or reject it");
                    btnCancelActionPerformed(null);
                }
            }
            if(viewType == DEPOSIT_ACCT && rdoTransfer_In_No.isSelected()== true)
                rdoTransfer_In_NoActionPerformed(null);
            if(viewType==CLOSED_ACCT){
                ClientUtil.enableDisable(this,false);
                btnSave.setEnabled(false);
                rdoPenaltyRateApplicbleYes.setEnabled(true);
                rdoPenaltyRateApplicbleNo.setEnabled(true);
            }
            if(viewType == ClientConstants.ACTIONTYPE_VIEW)
                ClientUtil.enableDisable(this,false);
            if(viewType == CLOSED_ACCT || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW){
                btnTableCancel.setEnabled(false);
                rdoPenaltyRateApplicbleYes.setEnabled(false);
                rdoPenaltyRateApplicbleNo.setEnabled(false);
                btnSave.setEnabled(false);
            }
            //Changed By Suresh
            if(viewType == DEPOSIT_ACCT || viewType == LTD_DEP_LIST || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                tabClosingType.setSelectedIndex(2);
            }
            if(viewType == DEPOSIT_ACCT)
            {
            
            rdoPenaltyRateApplicbleYes.setSelected(true);
            setPenaltyRateIntApplicable();
            
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnException.setEnabled(false);
                btnCancel.setEnabled(true);
                btnView.setEnabled(false);
            }
            if(hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnException.setEnabled(false);
                btnCancel.setEnabled(true);
                btnView.setEnabled(false);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        if(rejectFlag==1){
          btnReject.setEnabled(false);  
        }
         if (viewType == AUTHORIZE) {
             btnAuthorize.setEnabled(true);
             btnAuthorize.setFocusable(true);
             btnAuthorize.setFocusPainted(true);
             btnAuthorize.requestFocus(true);
         }
         rdoPenaltyRateApplicbleYes.setSelected(true);
         lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        cboProductId.setSelectedItem(observable.getCboProductId());
        this.tblSubDeposits.setModel(observable.getTbmSubDeposit());
        txtDepositNo.setText(observable.getTxtDepositNo());
        this.lblStatus.setText(observable.getLblStatus());
        txtTransferringBranch.setText(observable.getTransferBranch_code());
        this.populatePWDetail();
        
        updateDepositDetails();
        updatePresentDepositDetails();
        updateClosingPositionDetails();
        updatePartialWithDrawal();
        updateCustomerInfo();
        updateDepositInfo();
        addRadioButtons();
        rdoPenaltyRateApplicbleYes.setSelected(true);
    }
    
   /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setTxtDepositNo(txtDepositNo.getText());
        observable.setRdoYesButton(rdoPenaltyRateApplicbleYes.isSelected());
        observable.setRdoNoButton(rdoPenaltyRateApplicbleNo.isSelected());
        observable.setRdoTypeOfDeposit_No(rdoLTDDeposit.isSelected());
        observable.setRdoTypeOfDeposit_Yes(rdoRegularDeposit.isSelected());
        observable.setRdoTransfer_No(rdoTransfer_In_No.isSelected());
        observable.setRdoTransfer_Yes(rdoTransfer_Out_Yes.isSelected());
        observable.setTransferBranch_code(txtTransferringBranch.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setScreen(this.getScreen());
    }
    
    private void updateBalance(String amount,boolean add){
        double unitsAvailable=0,pwUnits=0,pwAmt=0,unitsDrawn=0;
        if(amount!=null && amount.length()>0){
            Double amt = CommonUtil.convertObjToDouble(amount);
            if(amt!=null){
                pwAmt = amt.doubleValue();
                pwUnits=pwAmt/observable.getUnitAmt();
                
                amt = CommonUtil.convertObjToDouble(this.lblUnitsAvailableValue.getText());
                if(amt!=null)
                    unitsAvailable = amt.doubleValue();
                
                amt = CommonUtil.convertObjToDouble(this.lblUnitsDrawnValue.getText());
                if(amt!=null)
                    unitsDrawn = amt.doubleValue();
                
                
                if(add){
                    unitsAvailable-=pwUnits;
                    unitsDrawn+=pwUnits;
                }
                if(!add){
                    unitsAvailable+=pwUnits;
                    unitsDrawn-=pwUnits;
                }
                this.lblUnitsAvailableValue.setText(String.valueOf(unitsAvailable));
                this.lblUnitsDrawnValue.setText(String.valueOf(unitsDrawn));
            }
        }
    }
    
    private void setUpPWPan(boolean enable){
        ClientUtil.enableDisable(this.panPartialWithDrawal,enable);
        ClientUtil.clearAll(this.panPartialWithDrawal);
        if(enable==false) {
            this.pwButtonsDisable();
            this.btnPWNew.setEnabled(true);
        }else if(enable==true) {
            this.pwButtonsEnable();
            this.btnPWDelete.setEnabled(false);
        }
    }
    
    /**
     *Write methods here to populate the closing tab
     */
    
    /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
    }
    
    /* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    /* Auto Generated Method - setHelpMessage(
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new DepositClosingMRB();
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositNo"));
    }
    
    private void calculateMaturity(){
        HashMap maturityMap = new HashMap();
        if(observable.getTxtDepositNo().length()>0){
            maturityMap.put("DEPOSIT_NO",txtDepositNo.getText());
            List lst = ClientUtil.executeQuery("getMaturityDateForDep", maturityMap);
            if(lst!= null && lst.size()>0){
                maturityMap = (HashMap)lst.get(0);
                observable.setMaturityDate(CommonUtil.convertObjToStr(maturityMap.get("MATURITY_DT")));
                observable.setDepositDate(CommonUtil.convertObjToStr(maturityMap.get("DEPOSIT_DT")));
                observable.setCustomerID(CommonUtil.convertObjToStr(maturityMap.get("CUST_ID")));
            }
        }
        this.lblClosureTypeValue.setText("");
        if(behavesLike.equals("FLOATING_RATE")){
            if(flPtWithoutPeriod){
                observable.setMaturityDate(CommonUtil.convertObjToStr(currDt));
                System.out.println("#$%#$%observable.getMaturityDate()"+observable.getMaturityDate());
            }
            if(viewType == DEPOSIT_ACCT || viewType == TRANSFERRING_BRANCH || viewType == LTD_DEP_LIST){
                if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getMaturityDate()),currDt) >= 0){
                    this.lblClosureTypeValue.setText(CommonConstants.NORMAL_CLOSURE) ;
                    //                    observable.setLblClosingType(CommonConstants.NORMAL_CLOSURE) ;
                    rdoPenaltyRateApplicbleYes.setVisible(false);
                    rdoPenaltyRateApplicbleNo.setVisible(false);
                    lblRateApplicble.setVisible(false);
                    observable.setPenaltyPenalRate("0.0");
                    observable.setRdoPenaltyPenalRate_no(false);
                    observable.setRdoPenaltyPenalRate_yes(false);
                    txtTransferringBranch.setText("");
                    lblBranchValue.setText("");
                    lblTransferringBranch.setVisible(false);
                    panTransferringBranch.setVisible(false);
                    panTransferringBranch.setEnabled(false);
                    lblTransferOut.setVisible(false);
                    panTransferOut.setVisible(false);
                    lblBranch.setVisible(false);
                    lblBranchValue.setVisible(false);
                    btnTransferringBranch.setEnabled(false);
                }else{
                    lblTransferOut.setVisible(true);
                    panTransferOut.setVisible(true);
                    //Changed By Suresh
                    if(rdoTransfer_Out_Yes.isSelected() == true && (viewType == DEPOSIT_ACCT || viewType == TRANSFERRING_BRANCH || viewType == CLOSED_ACCT)){
                        //                if(observable.isRdoTransfer_Yes() == true && (viewType == DEPOSIT_ACCT || viewType == TRANSFERRING_BRANCH || viewType == CLOSED_ACCT ||  viewType == AUTHORIZE)){
                        this.lblClosureTypeValue.setText(CommonConstants.TRANSFER_OUT_CLOSURE) ;
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                        lblRateApplicble.setVisible(false);
                        observable.setPenaltyPenalRate("0.0");
                        observable.setRdoPenaltyPenalRate_no(false);
                        observable.setRdoPenaltyPenalRate_yes(false);
                        rdoTransfer_Out_Yes.setSelected(true);
                        //Changed By Suresh
                    }else if(rdoTransfer_In_No.isSelected()== true && (viewType == DEPOSIT_ACCT ||
                    //                }else if(observable.isRdoTransfer_No() == true && (viewType == DEPOSIT_ACCT ||
                    viewType == TRANSFERRING_BRANCH || viewType == CLOSED_ACCT || viewType == LTD_DEP_LIST)){
                        this.lblClosureTypeValue.setText(CommonConstants.PREMATURE_CLOSURE) ;
                        rdoPenaltyRateApplicbleYes.setVisible(true);
                        rdoPenaltyRateApplicbleNo.setVisible(true);
                        lblRateApplicble.setVisible(true);
                        rdoTransfer_In_No.setSelected(true);
                    }
                }
            }
            if(viewType == CLOSED_ACCT || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW){
                if(observable.isRdoTransfer_Yes() == true){
                    this.lblClosureTypeValue.setText(CommonConstants.TRANSFER_OUT_CLOSURE) ;
                    rdoTransfer_Out_Yes.setVisible(true);
                    rdoTransfer_In_No.setVisible(true);
                    lblTransferOut.setVisible(true);
                    rdoTransfer_Out_Yes.setSelected(true);
                    rdoPenaltyRateApplicbleYes.setVisible(false);
                    rdoPenaltyRateApplicbleNo.setVisible(false);
                    lblRateApplicble.setVisible(false);
                    observable.setPenaltyPenalRate("0.0");
                    observable.setRdoPenaltyPenalRate_no(false);
                    observable.setRdoPenaltyPenalRate_yes(false);
                }else if(observable.isRdoTransfer_No()== true){
                    this.lblClosureTypeValue.setText(CommonConstants.PREMATURE_CLOSURE) ;
                    rdoTransfer_Out_Yes.setVisible(true);
                    rdoTransfer_In_No.setVisible(true);
                    lblTransferOut.setVisible(true);
                    rdoTransfer_In_No.setSelected(true);
                    rdoPenaltyRateApplicbleYes.setVisible(true);
                    rdoPenaltyRateApplicbleNo.setVisible(true);
                    lblRateApplicble.setVisible(true);
                }else if(observable.normalClosing == true){
                    this.lblClosureTypeValue.setText(CommonConstants.NORMAL_CLOSURE) ;
                    rdoTransfer_In_No.setSelected(false);
                    rdoTransfer_Out_Yes.setSelected(false);
                    rdoPenaltyRateApplicbleYes.setVisible(false);
                    rdoPenaltyRateApplicbleNo.setVisible(false);
                    rdoTransfer_Out_Yes.setVisible(false);
                    rdoTransfer_In_No.setVisible(false);
                    lblTransferOut.setVisible(false);
                }
            }
            
            observable.setLblClosingType(this.lblClosureTypeValue.getText()) ;
        }else{
            if(!observable.getMaturityDate().equals("")){
                if(viewType == DEPOSIT_ACCT || viewType == TRANSFERRING_BRANCH || viewType == LTD_DEP_LIST){
                    if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getMaturityDate()),currDt) >= 0){
                        this.lblClosureTypeValue.setText(CommonConstants.NORMAL_CLOSURE) ;
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                        lblRateApplicble.setVisible(false);
                        observable.setPenaltyPenalRate("0.0");
                        observable.setRdoPenaltyPenalRate_no(false);
                        observable.setRdoPenaltyPenalRate_yes(false);
                        txtTransferringBranch.setText("");
                        lblBranchValue.setText("");
                        lblTransferringBranch.setVisible(false);
                        panTransferringBranch.setVisible(false);
                        panTransferringBranch.setEnabled(false);
                        lblTransferOut.setVisible(false);
                        panTransferOut.setVisible(false);
                        lblBranch.setVisible(false);
                        lblBranchValue.setVisible(false);
                        btnTransferringBranch.setEnabled(false);
                    } else {
                        lblTransferOut.setVisible(true);
                        panTransferOut.setVisible(true);
                        if(rdoTransfer_Out_Yes.isSelected() == true && (viewType == DEPOSIT_ACCT || viewType == TRANSFERRING_BRANCH || viewType == CLOSED_ACCT)){
                            this.lblClosureTypeValue.setText(CommonConstants.TRANSFER_OUT_CLOSURE) ;
                            rdoPenaltyRateApplicbleYes.setVisible(false);
                            rdoPenaltyRateApplicbleNo.setVisible(false);
                            lblRateApplicble.setVisible(false);
                            observable.setPenaltyPenalRate("0.0");
                            observable.setRdoPenaltyPenalRate_no(false);
                            observable.setRdoPenaltyPenalRate_yes(false);
                        }else if(rdoTransfer_In_No.isSelected()== true && (viewType == DEPOSIT_ACCT ||
                        viewType == TRANSFERRING_BRANCH || viewType == CLOSED_ACCT || viewType == LTD_DEP_LIST)){
                            this.lblClosureTypeValue.setText(CommonConstants.PREMATURE_CLOSURE) ;
                            rdoPenaltyRateApplicbleYes.setVisible(true);
                            rdoPenaltyRateApplicbleNo.setVisible(true);
                            lblRateApplicble.setVisible(true);
                        }
                    }
                }
                if(viewType == CLOSED_ACCT || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW){
                    if(observable.isRdoTransfer_Yes() == true){
                        this.lblClosureTypeValue.setText(CommonConstants.TRANSFER_OUT_CLOSURE) ;
                        rdoTransfer_Out_Yes.setVisible(true);
                        rdoTransfer_In_No.setVisible(true);
                        lblTransferOut.setVisible(true);
                        rdoTransfer_Out_Yes.setSelected(true);
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                        lblRateApplicble.setVisible(false);
                        observable.setPenaltyPenalRate("0.0");
                        observable.setRdoPenaltyPenalRate_no(false);
                        observable.setRdoPenaltyPenalRate_yes(false);
                    }else if(observable.isRdoTransfer_No()== true){
                        this.lblClosureTypeValue.setText(CommonConstants.PREMATURE_CLOSURE) ;
                        rdoTransfer_Out_Yes.setVisible(true);
                        rdoTransfer_In_No.setVisible(true);
                        lblTransferOut.setVisible(true);
                        rdoTransfer_In_No.setSelected(true);
                        rdoPenaltyRateApplicbleYes.setVisible(true);
                        rdoPenaltyRateApplicbleNo.setVisible(true);
                        lblRateApplicble.setVisible(true);
                    }else if(observable.normalClosing == true){
                        this.lblClosureTypeValue.setText(CommonConstants.NORMAL_CLOSURE) ;
                        rdoTransfer_In_No.setSelected(false);
                        rdoTransfer_Out_Yes.setSelected(false);
                        rdoPenaltyRateApplicbleYes.setVisible(false);
                        rdoPenaltyRateApplicbleNo.setVisible(false);
                        rdoTransfer_Out_Yes.setVisible(false);
                        rdoTransfer_In_No.setVisible(false);
                        lblTransferOut.setVisible(false);
                    }
                }
                observable.setLblClosingType(this.lblClosureTypeValue.getText()) ;
                rdoPenaltyRateApplicbleYes.setSelected(true);
                btnTableOk.requestFocus();
               
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnApplyPenal;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPWDelete;
    private com.see.truetransact.uicomponent.CButton btnPWNew;
    private com.see.truetransact.uicomponent.CButton btnPWSave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    public com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTableCancel;
    private com.see.truetransact.uicomponent.CButton btnTableOk;
    private com.see.truetransact.uicomponent.CButton btnTransferringBranch;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CPanel chargePan;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadValue;
    private com.see.truetransact.uicomponent.CLabel lblActualPeriodRun;
    private com.see.truetransact.uicomponent.CLabel lblActualPeriodRunValue;
    private com.see.truetransact.uicomponent.CLabel lblAddIntRtAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblAddIntRteAmt;
    private com.see.truetransact.uicomponent.CLabel lblAddIntrstRate;
    private com.see.truetransact.uicomponent.CLabel lblAddIntrstRteVal;
    private com.see.truetransact.uicomponent.CLabel lblAdditionalIntrstHed;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommisionRecovered;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommisionRecoveredValue;
    private com.see.truetransact.uicomponent.CLabel lblAmountWithDrawn;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalanceDeposit;
    private com.see.truetransact.uicomponent.CLabel lblBalanceDepositValue;
    private com.see.truetransact.uicomponent.CLabel lblBalanceValue;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblBranchValue;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCategoryValue;
    private com.see.truetransact.uicomponent.CLabel lblClDisbursal;
    private com.see.truetransact.uicomponent.CLabel lblClDisbursalValue;
    private com.see.truetransact.uicomponent.CLabel lblClTDSCollected;
    private com.see.truetransact.uicomponent.CLabel lblClTDSCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblClosureType;
    private com.see.truetransact.uicomponent.CLabel lblClosureTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionValue;
    private com.see.truetransact.uicomponent.CLabel lblDeathClaim;
    private com.see.truetransact.uicomponent.CLabel lblDeathClaimInterest;
    private com.see.truetransact.uicomponent.CLabel lblDeathClaimInterestValue;
    private com.see.truetransact.uicomponent.CLabel lblDeathClaimValue;
    private com.see.truetransact.uicomponent.CLabel lblDelayedAmount;
    private com.see.truetransact.uicomponent.CLabel lblDelayedAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblDelayedInstallmentValue;
    private com.see.truetransact.uicomponent.CLabel lblDelayedInstallments;
    private com.see.truetransact.uicomponent.CLabel lblDepositAccountName1;
    private com.see.truetransact.uicomponent.CLabel lblDepositAccountNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositDate;
    private com.see.truetransact.uicomponent.CLabel lblDepositDateValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblDepositRunPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDepositRunPeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblInstDue;
    private com.see.truetransact.uicomponent.CLabel lblInstDueValue;
    private com.see.truetransact.uicomponent.CLabel lblInstPaid;
    private com.see.truetransact.uicomponent.CLabel lblInstPaidValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestCr;
    private com.see.truetransact.uicomponent.CLabel lblInterestCrValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestCredited;
    private com.see.truetransact.uicomponent.CLabel lblInterestCreditedValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestDr;
    private com.see.truetransact.uicomponent.CLabel lblInterestDrValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestDrawn;
    private com.see.truetransact.uicomponent.CLabel lblInterestDrawnValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequencyValue;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestApplDate;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestApplDateValue;
    private com.see.truetransact.uicomponent.CLabel lblLienFreezeAmount;
    private com.see.truetransact.uicomponent.CLabel lblLienFreezeAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblMV;
    private com.see.truetransact.uicomponent.CLabel lblMVvalue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDateValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfUnits;
    private com.see.truetransact.uicomponent.CLabel lblPayRec;
    private com.see.truetransact.uicomponent.CLabel lblPayRecValue;
    private com.see.truetransact.uicomponent.CLabel lblPenaltyPenalRate;
    private com.see.truetransact.uicomponent.CLabel lblPenaltyPenalRateValue;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPeriodOfMaturity;
    private com.see.truetransact.uicomponent.CLabel lblPeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblPreClosingPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPreClosingPeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblPreClosingRate;
    private com.see.truetransact.uicomponent.CLabel lblPreClosingRateValue;
    private com.see.truetransact.uicomponent.CLabel lblPreIntPayable;
    private com.see.truetransact.uicomponent.CLabel lblPreIntValue;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosurePeriod;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosurePeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosureRate;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosureRateValue;
    private com.see.truetransact.uicomponent.CLabel lblPresentUnitInt;
    private com.see.truetransact.uicomponent.CLabel lblPresentUnitIntValue;
    private com.see.truetransact.uicomponent.CLabel lblPrincipal;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalValue;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRateApplicable;
    private com.see.truetransact.uicomponent.CLabel lblRateApplicableValue;
    private com.see.truetransact.uicomponent.CLabel lblRateApplicble;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterestValue;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSettlementMode;
    private com.see.truetransact.uicomponent.CLabel lblSettlementModeValue;
    private com.see.truetransact.uicomponent.CLabel lblSettlementUnitInt;
    private com.see.truetransact.uicomponent.CLabel lblSettlementUnitIntValue;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTDSCollected;
    private com.see.truetransact.uicomponent.CLabel lblTDSCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblTransferOut;
    private com.see.truetransact.uicomponent.CLabel lblTransferringBranch;
    private com.see.truetransact.uicomponent.CLabel lblUnitsAvailable;
    private com.see.truetransact.uicomponent.CLabel lblUnitsAvailableValue;
    private com.see.truetransact.uicomponent.CLabel lblUnitsDrawn;
    private com.see.truetransact.uicomponent.CLabel lblUnitsDrawnValue;
    private com.see.truetransact.uicomponent.CLabel lblWithDrawnAmount;
    private com.see.truetransact.uicomponent.CLabel lblWithDrawnAmountValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAcctDetails;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panClosingPosition;
    private com.see.truetransact.uicomponent.CPanel panDeposit;
    private com.see.truetransact.uicomponent.CPanel panDepositAcctInfo;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositNo;
    private com.see.truetransact.uicomponent.CPanel panDepositType;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panMainPW;
    private com.see.truetransact.uicomponent.CPanel panNormalClosure;
    private com.see.truetransact.uicomponent.CPanel panPWButtons;
    private com.see.truetransact.uicomponent.CPanel panPartial;
    private com.see.truetransact.uicomponent.CPanel panPartialWithDrawal;
    private com.see.truetransact.uicomponent.CPanel panProrataPosition;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubDeposit;
    private com.see.truetransact.uicomponent.CPanel panSubDepositInfo;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransferOut;
    private com.see.truetransact.uicomponent.CPanel panTransferringBranch;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPenaltyRateApplicable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransferOut;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTypesOf_Deposit;
    private com.see.truetransact.uicomponent.CRadioButton rdoLTDDeposit;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyRateApplicbleNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyRateApplicbleYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRegularDeposit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransfer_In_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransfer_Out_Yes;
    private com.see.truetransact.uicomponent.CPanel servicTaxPanel;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpPartialWithdrawal;
    private com.see.truetransact.uicomponent.CScrollPane srpSubDeposits;
    private com.see.truetransact.uicomponent.CTabbedPane tabClosingType;
    private com.see.truetransact.uicomponent.CTable tblPartialWithdrawal;
    private com.see.truetransact.uicomponent.CTable tblSubDeposits;
    private javax.swing.JToolBar tbrTermDeposit;
    private com.see.truetransact.uicomponent.CTextField txtAmountWithDrawn;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOfUnits;
    private com.see.truetransact.uicomponent.CTextField txtTransferringBranch;
    // End of variables declaration//GEN-END:variables
   private javax.swing.JScrollPane srpChargeDetails;
   SimpleTableModel stm = null;
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            JFrame jf = new JFrame();
            DepositClosingUI gui = new DepositClosingUI();
            jf.getContentPane().add(gui);
            jf.setSize(750,700);
            jf.show();
            gui.show();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
    
    public void tableChanged(TableModelEvent e) {  
        int row = e.getFirstRow();  
        int column = e.getColumn();  
        Object data = stm.getValueAt(row, column);  
        
//now you have the data in the cell and the place in the grid where the   
   
//cell is so you can use the data as you want  
    } 
private void editChargeTable(){
                table.addMouseListener(new java.awt.event.MouseAdapter() {
                String chargeValue;
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    System.out.println("Txt mmmmmmmmmmmmmmm===============");
                    if (tableFlag == true) {
                        accClosingCharges();
                        // added by shihad for resetting transaction ui after clicking charge details grid - mantis 9985 
                        transactionUI.setButtonEnableDisable(true);
                        transactionUI.cancelAction(false);
                        transactionUI.resetObjects();
                        // on 11 dec 2014
                    }               

                }
            }); 
       // getTotalRenewalAmt();
    
}
    public void getTotalRenewalAmt() {
        double totCharge = 0;
        double oldValue = CommonUtil.convertObjToDouble(lblBalanceValue.getText());
        double disbAmount = CommonUtil.convertObjToDouble(lblClDisbursalValue.getText());
        double commAmount = CommonUtil.convertObjToDouble(lblAgentCommisionRecoveredValue.getText());
        double payRecAmount = CommonUtil.convertObjToDouble(lblPayRecValue.getText());
        //Added by sreekrishn 
        double interestPaid = CommonUtil.convertObjToDouble(lblInterestCrValue.getText());
        double interestPayable = CommonUtil.convertObjToDouble(lblInterestDrValue.getText());
        System.out.println("interestPaid$@$@#"+interestPaid+"interestPayable$#@$#@"+interestPayable); 
        if (observable.getIsSlabWiseDailyDeposit().equalsIgnoreCase("Y")) {// Added by nithya on 27-10-2018 for KD 305 - 0018081: CLOSIING AMOUNT DIFFERING WHEN PREMATURE CLOSING OF DAILY DEPOSIT.
            if (observable.getLblPayRecDet().equals("Receivable")) {
                payRecAmount = payRecAmount * -1;
            } else if (observable.getLblPayRecDet().equals("Payable")) {
                payRecAmount = payRecAmount;
            }
        } else {
            if (interestPaid > interestPayable) {
                payRecAmount = payRecAmount * -1;
            } else {
                payRecAmount = payRecAmount;
            }
        }
        disbAmount = oldValue-tableCharge-commAmount+payRecAmount;
            lblClDisbursalValue.setText(CommonUtil.convertObjToStr(disbAmount));
            setCallingAmt(CommonUtil.convertObjToStr(disbAmount));
    }
        private void createChargeTable() {
        if (transNew) {
            chargeMap.clear();
            String prodId = prodDesc;
            // Added by nithya on 03-10-2017 for group deposit penal calculation
             boolean isDailyGroupDeposit = false;
            HashMap checkMap = new HashMap();
            checkMap.put("PROD_ID", ((ComboBoxModel)this.cboProductId.getModel()).getKeyForSelected());
            List groupDepositProdList = ClientUtil.executeQuery("getIsGroupDepositProduct", checkMap);
            if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
                HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
                if (groupDepositProdMap != null && groupDepositProdMap.containsKey("IS_GROUP_DEPOSIT") && groupDepositProdMap.get("IS_GROUP_DEPOSIT") != null) {
                    if (CommonUtil.convertObjToStr(groupDepositProdMap.get("IS_GROUP_DEPOSIT")).equalsIgnoreCase("Y")) {
                       isDailyGroupDeposit = true;
                       observable.setGroupDepositProd("Y");
                    }
                }
            }   
           System.out.println("prodId 0000 nnnn======1 " + prodId);
           HashMap tableMap = new HashMap();
            if(isDailyGroupDeposit){
                String productId = CommonUtil.convertObjToStr(((ComboBoxModel)this.cboProductId.getModel()).getKeyForSelected());
                //prodId = "NITHYA NIDHI DEPOSIT"; // Added for testing
                tableMap = buildGroupDepositData(prodId,productId);
            }else{
                tableMap = buildData(prodId);
            }     
            System.out.println("tableMap 0000 nnnn====== " + tableMap);
            ArrayList dataList = new ArrayList();
            dataList = (ArrayList) tableMap.get("DATA");
            panChargeDetails.removeAll();
            if (dataList != null && dataList.size() > 0) {
                // });
                tableFlag = true;
                panChargeDetails.setVisible(true);
                stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
                table = new JTable(stm);
                table.setSize(430, 110);
                srpChargeDetails = new JScrollPane(table);
                srpChargeDetails.setMinimumSize(new Dimension(430, 110));
                srpChargeDetails.setPreferredSize(new Dimension(430, 110));
                panChargeDetails.add(srpChargeDetails, new GridBagConstraints());
                table.revalidate();

            } else {
                tableFlag = false;
                chrgTableEnableDisable();
            }
        } else {
            HashMap tableMap = editBuildData(prodDesc);
            ArrayList dataList = new ArrayList();
            dataList = (ArrayList) tableMap.get("DATA");
            if (dataList != null && dataList.size() > 0) {
                tableFlag = true;
                ArrayList headers;
//                panRenewalChargeDetails.setVisible(true);
                stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
                table = new JTable(stm);
                table.setSize(430, 110);
                srpChargeDetails = new javax.swing.JScrollPane(table);
                srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
                srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
//                panRenewalChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
                table.revalidate();
            } else {
                tableFlag = false;
                chrgTableEnableDisable();
            }
            System.out.println("tableFlag  0000 nnnn======2 " + tableFlag);
        }
    }
        
private HashMap buildData(String prodId) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        whereMap.put("IS_DEPOSIT_OR_LOAN", CommonConstants.DEPOSITS);
        if (transNew) {
            whereMap.put("DEDUCTION_ACCU", "C");
        } else {
            //whereMap.put("DEDUCTION_ACCU", "C");//20-04-2014
            whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        }
        List list = ClientUtil.executeQuery("getDepositChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        } else {
            _heading = new ArrayList();
            _heading.add("Select");
            _heading.add("DESC");
            _heading.add("AMOUNT");
            _heading.add("M");
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                //                if (obj != null) {
                colData.add(CommonUtil.convertObjToStr(obj));
                //                } else {
                //                    colData.add("");
                //                }
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }

    private void chrgTableEnableDisable() {
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
//        panRenewalChargeDetails.removeAll();
//        panRenewalChargeDetails.setVisible(false);
    }
    private HashMap editBuildData(String prodDesc) {
        String prodId = prodDesc;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        // whereMap.put("DEDUCTION_ACCU", "O");
       // whereMap.put("DEDUCTION_ACCU", "C");//20-03-2014
        whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");//20-03-2014
        whereMap.put("IS_DEPOSIT_OR_LOAN", CommonConstants.DEPOSITS);
        List list = ClientUtil.executeQuery("getDepositChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        } else {
            _heading = new ArrayList();
            _heading.add("Select");
            _heading.add("DESC");
            _heading.add("AMOUNT");
            _heading.add("M");
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                colData.add(CommonUtil.convertObjToStr(obj));
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }

    public class SimpleTableModel extends AbstractTableModel {

        private ArrayList dataVector;
        private ArrayList headingVector;

        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }

        public int getColumnCount() {
            return headingVector.size();
        }

        public int getRowCount() {
            return dataVector.size();
        }

        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 3)).equals("Y"))) { // (CommonUtil.convertObjToStr(getValueAt(row, headingVector.size() - 1)).equals("Y"))
                System.out.println("getValueAt(row, col + 3)");
                return false;
            } else {
                if (col != 0) {
                    if (col == 2 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }
    // Added by nithya on 03-10-2017 for group deposit penal calculation   
    
    private HashMap buildGroupDepositData(String prodId,String productId) {
         HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        whereMap.put("IS_DEPOSIT_OR_LOAN", CommonConstants.DEPOSITS);
        if (transNew) {
            whereMap.put("DEDUCTION_ACCU", "C");
        } else {           
            whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        }
        List list = ClientUtil.executeQuery("getDepositChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        } else {
            _heading = new ArrayList();
            _heading.add("Select");
            _heading.add("DESC");
            _heading.add("AMOUNT");
            _heading.add("M");
        }      
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            if (CommonUtil.convertObjToStr(map.get("DESC")).equalsIgnoreCase("Penal")) {
                double penalAmount = getPenalAmountForGroupDeposit();
                map.put("AMOUNT", penalAmount);
            }
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                colData.add(CommonUtil.convertObjToStr(obj));
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }
    
    private double getPenalAmountForGroupDeposit(){
        String depositNo = txtDepositNo.getText();        
        double intCalcAmount = 0;
        double penalAmount = 0;
        double loanPenalRate = 0;
        double normalPenalRate = 0;
        boolean isLienExists = false;
        String penalCalcType = "";
        double penalCalcRate = 0;
        int sctDepoPeriod = 0;
        int sctDepoPeriodYr = 0;
        int sctDepoPerioddd = 0;
        int sctDepoPeriodMon = 0;
        HashMap actDetailMap = new HashMap();
        actDetailMap.put("DEPOSIT_NO", depositNo);
        List actDetailLst = ClientUtil.executeQuery("getActDetailsForDailyPrdct", actDetailMap);
        HashMap actDetailsMap = new HashMap();
        actDetailsMap = (HashMap) actDetailLst.get(0);
        if(actDetailsMap.get("DEPOSIT_PERIOD_DD") != null){
           sctDepoPerioddd = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_DD")); 
        }if(actDetailsMap.get("DEPOSIT_PERIOD_YY") != null){
           sctDepoPeriodYr = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_YY"));
        }if(actDetailsMap.get("DEPOSIT_PERIOD_MM") != null){
            sctDepoPeriodMon = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_MM")); 
        }      
        sctDepoPeriod = sctDepoPeriodMon + (sctDepoPeriodYr * 12);
        System.out.println("sctDepoPeriod :: " + sctDepoPeriod);   
        HashMap checkMap = new HashMap();       
        checkMap.put("DEPOSIT_NO", depositNo);
        checkMap.put("START_DATE",actDetailsMap.get("DEPOSIT_DT"));
        checkMap.put("END_DATE",actDetailsMap.get("MATURITY_DT"));
        checkMap.put("SL_NO",sctDepoPeriod);
        List depositBalList = ClientUtil.executeQuery("getSumAmountDelayedForGroupDeposit", checkMap);
        if (depositBalList != null && depositBalList.size() > 0) {
            HashMap depositBalMap = (HashMap) depositBalList.get(0);
            intCalcAmount = CommonUtil.convertObjToDouble(depositBalMap.get("INT_CALC_AMT"));
        }
        List checkForLienList = ClientUtil.executeQuery("getIsGroupDepositExistsForLoan", checkMap);
        if (checkForLienList != null && checkForLienList.size() > 0) {
            isLienExists = true;
        }
        List chkLst = ClientUtil.executeQuery("getInterestRateForDepositGroup", checkMap);
        if (chkLst != null && chkLst.size() > 0) {
            HashMap groupIdMap = (HashMap) chkLst.get(0);
            System.out.println("groupIdMap :: " + groupIdMap);
            if (groupIdMap != null) {
                penalCalcType = CommonUtil.convertObjToStr(groupIdMap.get("PENAL_CALCULATION_TYPE"));
                normalPenalRate = CommonUtil.convertObjToDouble(groupIdMap.get("NON_PRIZED_PENAL"));
                loanPenalRate = CommonUtil.convertObjToDouble(groupIdMap.get("PRIZED_PENAL"));
                if (isLienExists) {
                    penalCalcRate = loanPenalRate;
                } else {
                    penalCalcRate = normalPenalRate;
                }
                if (penalCalcType.equalsIgnoreCase("Percent")) {
                    penalAmount = intCalcAmount * (penalCalcRate / 1200);
                } else if (penalCalcType.equalsIgnoreCase("Absolute")) {
                    penalAmount = penalCalcRate;
                }
            }
        }
        penalAmount = Math.round(penalAmount);
        if (penalAmount < 0) {
            penalAmount = 0;
        }
        return penalAmount;
    }
    
    
    // Addded by nithya on 16-06-2018 for calculating ServiceTax/GST implementation for charges and agent commission
    
    private void calculateServiceTaxAmt() {
        System.out.println("executing in calculateServiceTaxAmt");
        //taxSettingsListForCharges =  new ArrayList();
        double serviceTaxAmt = 0;
        double taxAmt = 0.0;
        List taxSettingsList = new ArrayList();
        if (CommonUtil.convertObjToDouble(observable.getAgentCommisionRecoveredValue()) > 0) {
            HashMap CommProdMap = new HashMap();
            CommProdMap.put("PROD_ID", observable.getProdID());
            List commisionHeadLst = (List) ClientUtil.executeQuery("getSelectCommissionRecoveredHeadForGST", CommProdMap);//COMMISION_HEAD
            HashMap commHeadMap ;
            String commissionHead = "";
            if(commisionHeadLst != null && commisionHeadLst.size() > 0){
                commHeadMap = (HashMap)commisionHeadLst.get(0);
                if(commHeadMap.containsKey("COMMISION_HEAD") && commHeadMap.get("COMMISION_HEAD") != null)
                    commissionHead = CommonUtil.convertObjToStr(commHeadMap.get("COMMISION_HEAD"));
            }
            String achd = commissionHead;
            HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
            if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")){
               HashMap taxMap = new HashMap();
               taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
               taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToDouble(observable.getAgentCommisionRecoveredValue()));
               taxSettingsList.add(taxMap); 
            }            
        }
        if (taxSettingsListForCharges != null && taxSettingsListForCharges.size() > 0) {
            taxSettingsList.addAll(taxSettingsList.size(), taxSettingsListForCharges);
        }            
        HashMap ser_Tax_Val = new HashMap();
        ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
        ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CurrencyValidation.formatCrore(String.valueOf(taxAmt)));
        ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
        try {
            objServiceTax = new ServiceTaxCalculation();
            serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                serviceTaxAmt = CommonUtil.convertObjToDouble(amt);
                lblServiceTaxval.setText(amt);
                serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                //System.out.println("calling amount :: "+ CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()));
                double transAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()) - CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                transactionUI.setCallingAmount(CommonUtil.convertObjToStr(transAmt));
                lblClDisbursalValue.setText(CommonUtil.convertObjToStr(transAmt));
                //System.out.println("transAmt :: "+ transAmt);
                //System.out.println("getClosingDisbursal :: "+ observable.getClosingDisbursal());
            } else {
                lblServiceTaxval.setText("0.00");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void editRDDelayedAmount() {  // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation. [ RD Closing code lines rewritten and created new function ]
        amountMap = new HashMap();
        if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
            String tolerance_amt = CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
            if (tolerance_amt.length() == 0) {
                ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                return;
            }
            String selectedAmt = CommonUtil.convertObjToStr(lblDelayedAmountValue.getText());
            selectedAmt = selectedAmt.replaceAll(",", "");
            amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
            amountMap.put("SELECTED_AMT", selectedAmt);
            amountMap.put("TITLE", "Delayed Amount");
            amountMap.put("CALCULATED_AMT", observable.getChargeAmount());
            System.out.println("Delayed Amount " + amountMap);
            TextUI textUI = new TextUI(this, this, amountMap);

        }
    }
    
    
//    private void lockAccounts() {
//        boolean lock = false;
//        String lockedBy = "";
//        HashMap map = new HashMap();
//        map.put("SCREEN_ID", getScreenID());
//        map.put("RECORD_KEY", txtDepositNo.getText());
//        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        map.put("CUR_DATE", currDt);
//        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
//        if (lstLock != null && lstLock.size() > 0) {
//            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
//            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
//                btnSave.setEnabled(false);
//            } else {
//                btnSave.setEnabled(true);
//            }
//        } else {
//            btnSave.setEnabled(true);
//        }
//        setOpenForEditBy(lockedBy);
//        if (lockedBy.equals("")) {
//            ClientUtil.execute("insertEditLock", map);
//        }
//        
//        if (lockedBy.length() > 0) {
//            String data = getLockDetails(lockedBy, getScreenID());
//            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//            btnSave.setEnabled(false);
//        }
//    }
    
    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock != null && lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }
    
       
//     public void removeEditLockScreen(String recordKey) {
//        if (recordKey.length() > 0) {
//            HashMap map = new HashMap();
//            map.put("SCREEN_ID", getScreenID());
//            map.put("RECORD_KEY", recordKey);
//            map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//            map.put("CUR_DATE", currDt);
//            ClientUtil.execute("deleteEditLock", map);
//        }
//        setMode(ClientConstants.ACTIONTYPE_CANCEL);
//    }
}
