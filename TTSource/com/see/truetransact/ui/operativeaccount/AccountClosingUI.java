/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountClosingUI.java
 *
 * Created on August 6, 2003, 10:53 AM
 */
package com.see.truetransact.ui.operativeaccount;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewall.TextUI;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductOB;
import com.see.truetransact.ui.operativeaccount.AccountClosingOB;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctHeadParamTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.termloan.LoanLiabilityUI;
import com.see.truetransact.ui.termloan.TermLoanUI;
import com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI;
import java.util.Calendar;

/**
 * This form is used to manipulate AccountClosing related functionality
 *
 * @author annamalai Modified by Karthik Modified by Sunil, added transaction UI
 */
public class AccountClosingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    final int AUTHORIZE = 3, CANCEL = 0;
    int viewType = -1;
    private HashMap mandatoryMap;
    private HashMap maturityMap;
    private AccountClosingOB observable;
    private final static Logger log = Logger.getLogger(AccountClosingUI.class);
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    AccountClosingRB accountClosingRB = new AccountClosingRB();
    private String prodType = "";
    private double calculateInt = 0;
    private String chargeAmt = "";
    private String balanceAmt = "";
    private String transProdId = "";
    private boolean depositFlag;
    private boolean tableFlag = false;
    private LinkedHashMap transactionDetailsTO = null;
    private HashMap calMap = new HashMap();
    private boolean isAuthRecord = false;
    private double depositAmt = 0.0;
    private double payableBalance = 0.0;
    private double sanctionAmt = 0.0;
    private double incidentAmt = 0.0;
    private double netWt = 0.0;   
    double accountClosingCharge = 0.0;
    private List chargelst = null;
    private JTable table = null;
    public String prodDesc = "";
    private Date actOpenDt = null;
    private Date actToDate = null;
    private Date currDt = null;
    Rounding rd = new Rounding();
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    private double totRecivable = 0;
    boolean intPayableChange = true;
    int searchOpt = 0;
    String memNo = "";
    private int rejectFlag = 0;
    private HashMap mdsLoan = null;
    private String purity = "";
    private Date asOn = null;
 	private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;
    private Double serviceTaxAmt;
  //  private boolean suspanceFlag=false;
    HashMap susTypeMap = new HashMap();
    private double taxamtForGoldLoan = 0;
    private List taxListForGoldLoan = new ArrayList();
    private HashMap ser_TaxGold_Map = new HashMap();
    private boolean goldSerTax = false;
    private HashMap authChargeMap = new HashMap();
    HashMap returnWaiveMap = null;
    RejectionApproveUI rejectionApproveUI=null;
    boolean isTransaction=false;
    private String lockAccount=null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    public HashMap serviceTaxIdMap = new HashMap();// Added by nithya 
    
    public AccountClosingUI(String prodType) {
        currDt = ClientUtil.getCurrentDate();
        this.prodType = prodType;
        initComponents();
        initStartUp();
        if (prodType.equals("TermLoan")) {
            transactionUI.setSourceScreen("LOAN_ACT_CLOSING");
        } else {
            transactionUI.setSourceScreen("ACT_CLOSING");
            btnWaive.setVisible(false);
        }
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        transDetailsUI = new TransDetailsUI(panAccountHead);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        chrgTableEnableDisable();
        btnDelete.setVisible(false);
    }

    /**
     * Creates new form AccountClosingUI
     */
    public AccountClosingUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
         btnWaive.setVisible(false);
        transactionUI.setSourceScreen("ACT_CLOSING");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        transDetailsUI = new TransDetailsUI(panAccountHead);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnDelete.setVisible(false);
        txtGrossWeight.setVisible(false);
        txtNetWeight.setVisible(false);
        lblGrossWeight.setVisible(false);
        lblNetWeight.setVisible(false);
    }

    private void initStartUp() {
        setMandatoryHashMap();
        setFieldNames();
        internationalize();

        observable = new AccountClosingOB();
        observable.setProdType(prodType);
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        btnDelete.setVisible(false);
        if (chkActnAct.isSelected()) {
            ClientUtil.enableDisable(panAuction, true);
        }
        txtGrossWeight.setEnabled(false);
        txtNetWeight.setEnabled(false);
        
    }

    private void setMaxLength() {
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtNoOfUnusedChequeLeafs.setValidation(new NumericValidation(4, 0));
        txtInterestPayable.setValidation(new CurrencyValidation(14, 2));
        txtChargeDetails.setValidation(new CurrencyValidation(14, 2));
        txtAccountClosingCharges.setValidation(new CurrencyValidation(14, 2));
        txtPayableBalance.setValidation(new CurrencyValidation(14, 2));
        txtAuctnAmt.setAllowAll(true);
        txtBalancAmt.setAllowAll(true);
        txtAuctnAmt.setValidation(new CurrencyValidation(14, 2));
        txtBalancAmt.setValidation(new CurrencyValidation(14, 2));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtNoOfUnusedChequeLeafs", new Boolean(true));
        mandatoryMap.put("txtInterestPayable", new Boolean(true));
        mandatoryMap.put("txtChargeDetails", new Boolean(true));
        mandatoryMap.put("txtAccountClosingCharges", new Boolean(true));
        mandatoryMap.put("txtPayableBalance", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /**
     * **************** NEW METHODS ****************
     */
    public void authorizeStatus(String authorizeStatus) {

        updateAuthorizeStatus(authorizeStatus);

    }

    private void updateAuthorizeStatus(String authorizeStatus) {
        btnDelete.setVisible(false);
        mdsLoan = new HashMap();
        if (viewType != AUTHORIZE) {
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            if (prodType.equals("TermLoan")) {
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseCashierAuthorizeTOList");
                } else {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectLoanAccountCloseAuthorizeTOList");
                }
            } else {
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectAccountCloseCashierAuthorizeTOList");
                } else {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectAccountCloseAuthorizeTOList");
                }
            }
           AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            if (!((String) observable.getCbmProductID().getKeyForSelected()).equals("")) {
                taxAmountDisplay();
            }
        } else if (viewType == AUTHORIZE) {
            String prod_Id = isItMDSLoan();
            if (prod_Id.equals("MDS_LOAN") && CommonUtil.convertObjToStr(observable.getLoanBehaves()).equals("LOANS_AGAINST_DEPOSITS")) {
                mdsLoan.put("MDS_PROD_ID", CommonUtil.convertObjToStr(prod_Id));
                mdsLoan.put("ACCOUNTNO", observable.getTxtAccountNumber());
                mdsLoan.put("MDS_BEHAVES", CommonUtil.convertObjToStr(observable.getLoanBehaves()));
            }
            ArrayList arrList = new ArrayList();
            final HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
            if (prodType.equals("TermLoan") && observable.getAvilableLoanSubsidy() > 0) {
                singleAuthorizeMap.put("SUBSIDY_AMT", new Double(observable.getAvilableLoanSubsidy()));
            } else {
                singleAuthorizeMap.put("SUBSIDY_AMT", new Double(0));
            }
            arrList.add(singleAuthorizeMap);
           
            if (mdsLoan != null && mdsLoan.containsKey("MDS_PROD_ID") && mdsLoan.containsKey("ACCOUNTNO") && mdsLoan.containsKey("MDS_BEHAVES")) {
                authorizeMap.put("MDS_LOANS", mdsLoan);
            }
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
            	HashMap serauthMap = new HashMap();
            	serauthMap.put("ACCT_NUM",observable.getTxtAccountNumber());
            	serauthMap.put("STATUS", authorizeStatus);
            	serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
            	serauthMap.put("AUTHORIZEDT", currDt);
            	authorizeMap.put("SER_TAX_AUTH",serauthMap);
			}
            if (isAuthRecord) {
                            //Added by sreekrishnan
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        try {
                            authorize(authorizeMap);
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
                //authorize(authorizeMap);
                isAuthRecord = false;
            } else {
                viewType = -1;
                return;
            }
            super.setOpenForEditBy(observable.getStatusBy());
            
            //super.removeEditLock(observable.getTxtAccountNumber());
//            //added by rishad 16/07/2015 for locking and removing
//            HashMap lockMap = new HashMap();
//            lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
//            lockMap.put("CUR_DATE", currDt.clone());
//            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            ClientUtil.execute("deleteLock", lockMap);
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
        
       
    }

    private String isItMDSLoan() {
        HashMap mdsLoanmap = new HashMap();
        String prodName = "";
        String prod_id = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
        mdsLoanmap.put("PROD_ID", CommonUtil.convertObjToStr(prod_id));
        List mdsRemList = ClientUtil.executeQuery("getMdsAuthorizeRemark", mdsLoanmap);
        if (mdsRemList != null && mdsRemList.size() > 0) {
            mdsLoanmap = (HashMap) mdsRemList.get(0);
            if (mdsLoanmap != null && mdsLoanmap.containsKey("AUTHORIZE_REMARK")) {
                prodName = CommonUtil.convertObjToStr(mdsLoanmap.get("AUTHORIZE_REMARK"));
            }
        }
        return prodName;
    }

    public void authorize(HashMap map) { 
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            if (map.containsKey("AUTHORIZESTATUS") && map.get("AUTHORIZESTATUS") != null) {
                if(map.get("AUTHORIZESTATUS").equals("REJECTED")){
                if (observable.getSusTypeMap() != null) {
                    map.put("AUCTION_BAL_AMOUNT", observable.getSusTypeMap());
                }
                }
            }
            
            observable.setAuthorizeMap(map);
            observable.doAction();
            observable.setAuthorizeMap(null);
            observable.setResultStatus();
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                if (prodType.equalsIgnoreCase("TermLoan")) {
                    newauthorizeListUI.displayDetails("Loan Closing");//Changed By Suresh R 12-Jun-2019 - KDSA-506 : Loan authorizing problem
                } else {
                    newauthorizeListUI.displayDetails("Account Closing"); //Changed By Suresh R 12-Jun-2019 - KDSA-506 : Loan authorizing problem
                }
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                if (prodType.equalsIgnoreCase("TermLoan")) {
                    authorizeListUI.displayDetails("Loan Account Closing");
                } else {
                    authorizeListUI.displayDetails("SB/Current Account Closing");
                }
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            }
        }
    }

    public void taxAmountDisplay() {
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_ID", (String) observable.getCbmProductID().getKeyForSelected());
        String status = "";
        if (!prodType.equals("TermLoan")) {
            status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getSRemarks();
        }
        if (status.equals("NRO")) {
            double taxAmt = observable.taxAmount(Double.parseDouble(txtInterestPayable.getText()));
            double bal = (CommonUtil.convertObjToDouble(observable.getAvailableBalance()).doubleValue()
                    - CommonUtil.convertObjToDouble(observable.getTxtInterestPayable()).doubleValue()) + taxAmt;
            ClientUtil.displayAlert("Account balance = Rs." + " " + bal + "\nInterest Amount = Rs." + " "
                    + observable.getTxtInterestPayable() + "\nTax Deduction = Rs." + " " + taxAmt
                    + "\nNet Payable Amount = Rs." + " " + observable.getTxtPayableBalance());
        }
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfoInner = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfUnusedChequeLeafs = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfUnusedChequeLeafs = new com.see.truetransact.uicomponent.CTextField();
        lblInterestPayable = new com.see.truetransact.uicomponent.CLabel();
        txtInterestPayable = new com.see.truetransact.uicomponent.CTextField();
        lblChargeDetails = new com.see.truetransact.uicomponent.CLabel();
        txtChargeDetails = new com.see.truetransact.uicomponent.CTextField();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        lblBalanceDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblAccountClosingCharges = new com.see.truetransact.uicomponent.CLabel();
        txtAccountClosingCharges = new com.see.truetransact.uicomponent.CTextField();
        lblPayableCharges = new com.see.truetransact.uicomponent.CLabel();
        txtPayableBalance = new com.see.truetransact.uicomponent.CTextField();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblInsuranceCharges = new com.see.truetransact.uicomponent.CLabel();
        txtInsuranceCharges = new com.see.truetransact.uicomponent.CTextField();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        btnWaive = new com.see.truetransact.uicomponent.CButton();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        btncheqDetails = new com.see.truetransact.uicomponent.CButton();
        sptAccountInfo = new com.see.truetransact.uicomponent.CSeparator();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        panAuction = new com.see.truetransact.uicomponent.CPanel();
        chkActnAct = new com.see.truetransact.uicomponent.CCheckBox();
        txtAuctnAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAuctnAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCrDr = new com.see.truetransact.uicomponent.CLabel();
        lblBalancAmt = new com.see.truetransact.uicomponent.CLabel();
        txtBalancAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDrAccHead = new com.see.truetransact.uicomponent.CLabel();
        panSaAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtDrAccHead = new com.see.truetransact.uicomponent.CTextField();
        lblSaAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        panSaAcctNum1 = new com.see.truetransact.uicomponent.CPanel();
        txtSaAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        panAuction1 = new com.see.truetransact.uicomponent.CPanel();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(858, 660));
        setPreferredSize(new java.awt.Dimension(858, 660));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(1000, 500));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(1000, 500));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(728, 340));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(728, 340));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMinimumSize(new java.awt.Dimension(280, 330));
        panProductID.setPreferredSize(new java.awt.Dimension(280, 330));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setNextFocusableComponent(txtAccountNumber);
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        cboProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(cboProductID, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblAccountNumber, gridBagConstraints);

        lblNoOfUnusedChequeLeafs.setText("No of Unused Cheque Leaves");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panProductID.add(lblNoOfUnusedChequeLeafs, gridBagConstraints);

        txtNoOfUnusedChequeLeafs.setMaxLength(4);
        txtNoOfUnusedChequeLeafs.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfUnusedChequeLeafs.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtNoOfUnusedChequeLeafs, gridBagConstraints);

        lblInterestPayable.setText("Interest Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblInterestPayable, gridBagConstraints);

        txtInterestPayable.setMaxLength(16);
        txtInterestPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestPayable.setValidation(new NumericValidation());
        txtInterestPayable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestPayableFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtInterestPayable, gridBagConstraints);

        lblChargeDetails.setText("Misc Service Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblChargeDetails, gridBagConstraints);

        txtChargeDetails.setMaxLength(16);
        txtChargeDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargeDetails.setValidation(new NumericValidation());
        txtChargeDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargeDetailsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtChargeDetails, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblAccountHead, gridBagConstraints);

        lblAccountHeadDisplay.setMaximumSize(new java.awt.Dimension(2250, 21));
        lblAccountHeadDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        lblAccountHeadDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblAccountHeadDisplay, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblCustomerNameDisplay, gridBagConstraints);

        lblBalance.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblBalance, gridBagConstraints);

        lblBalanceDisplay.setMinimumSize(new java.awt.Dimension(125, 21));
        lblBalanceDisplay.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblBalanceDisplay, gridBagConstraints);

        lblAccountClosingCharges.setText("Account Closing Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblAccountClosingCharges, gridBagConstraints);

        txtAccountClosingCharges.setEnabled(false);
        txtAccountClosingCharges.setMaxLength(16);
        txtAccountClosingCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountClosingCharges.setValidation(new NumericValidation());
        txtAccountClosingCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountClosingChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtAccountClosingCharges, gridBagConstraints);

        lblPayableCharges.setText("Payable Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblPayableCharges, gridBagConstraints);

        txtPayableBalance.setEditable(false);
        txtPayableBalance.setEnabled(false);
        txtPayableBalance.setMaxLength(16);
        txtPayableBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPayableBalance.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtPayableBalance, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setAllowAll(true);
        txtAccountNumber.setAllowNumber(true);
        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(110, 21));
        txtAccountNumber.setPreferredSize(new java.awt.Dimension(110, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHouseName.setMaximumSize(new java.awt.Dimension(150, 21));
        lblHouseName.setMinimumSize(new java.awt.Dimension(150, 21));
        lblHouseName.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblHouseName, gridBagConstraints);

        lblInsuranceCharges.setText("Insurance Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblInsuranceCharges, gridBagConstraints);

        txtInsuranceCharges.setMaxLength(16);
        txtInsuranceCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInsuranceCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtInsuranceCharges, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblMemberNo, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(130, 26));
        cPanel1.setPreferredSize(new java.awt.Dimension(130, 26));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemberNoActionPerformed(evt);
            }
        });
        txtMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(txtMemberNo, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMemberNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMemberNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(btnMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(cPanel1, gridBagConstraints);

        btnWaive.setBackground(new java.awt.Color(0, 0, 0));
        btnWaive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/waive4.jpg"))); // NOI18N
        btnWaive.setToolTipText("Waive");
        btnWaive.setFont(new java.awt.Font("MS Sans Serif", 1, 10)); // NOI18N
        btnWaive.setMaximumSize(new java.awt.Dimension(25, 25));
        btnWaive.setMinimumSize(new java.awt.Dimension(25, 25));
        btnWaive.setPreferredSize(new java.awt.Dimension(25, 25));
        btnWaive.setRequestFocusEnabled(false);
        btnWaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        panProductID.add(btnWaive, gridBagConstraints);

        lblServiceTax.setText("Service Tax     ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panProductID.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panProductID.add(lblServiceTaxval, gridBagConstraints);

        btncheqDetails.setText("DET");
        btncheqDetails.setToolTipText("unused cheque leafs");
        btncheqDetails.setMaximumSize(new java.awt.Dimension(25, 27));
        btncheqDetails.setMinimumSize(new java.awt.Dimension(25, 27));
        btncheqDetails.setPreferredSize(new java.awt.Dimension(25, 27));
        btncheqDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncheqDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        panProductID.add(btncheqDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        sptAccountInfo.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAccountInfo.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        panAccountInfoInner.add(sptAccountInfo, gridBagConstraints);

        panAccountHead.setMinimumSize(new java.awt.Dimension(200, 175));
        panAccountHead.setPreferredSize(new java.awt.Dimension(200, 175));
        panAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panAccountHead, gridBagConstraints);

        panChargeDetails.setMinimumSize(new java.awt.Dimension(340, 120));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(340, 120));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        panAccountInfoInner.add(panChargeDetails, gridBagConstraints);

        panAuction.setBorder(javax.swing.BorderFactory.createTitledBorder("Auction Details"));
        panAuction.setMaximumSize(new java.awt.Dimension(300, 160));
        panAuction.setMinimumSize(new java.awt.Dimension(300, 160));
        panAuction.setPreferredSize(new java.awt.Dimension(300, 160));
        panAuction.setLayout(new java.awt.GridBagLayout());

        chkActnAct.setText("Auction Account");
        chkActnAct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActnActActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAuction.add(chkActnAct, gridBagConstraints);

        txtAuctnAmt.setEditable(false);
        txtAuctnAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAuctnAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuctnAmtActionPerformed(evt);
            }
        });
        txtAuctnAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAuctnAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panAuction.add(txtAuctnAmt, gridBagConstraints);

        lblAuctnAmt.setText("Auction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panAuction.add(lblAuctnAmt, gridBagConstraints);

        lblCrDr.setText("Cr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panAuction.add(lblCrDr, gridBagConstraints);

        lblBalancAmt.setText("Balance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panAuction.add(lblBalancAmt, gridBagConstraints);

        txtBalancAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panAuction.add(txtBalancAmt, gridBagConstraints);

        lblDrAccHead.setText("Dr Acc Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panAuction.add(lblDrAccHead, gridBagConstraints);

        panSaAcctNum.setLayout(new java.awt.GridBagLayout());

        txtDrAccHead.setAllowAll(true);
        txtDrAccHead.setMaxLength(15);
        txtDrAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrAccHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrAccHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSaAcctNum.add(txtDrAccHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuction.add(panSaAcctNum, gridBagConstraints);

        lblSaAccountNumber.setText("Suspense Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panAuction.add(lblSaAccountNumber, gridBagConstraints);

        panSaAcctNum1.setLayout(new java.awt.GridBagLayout());

        txtSaAccountNumber.setAllowAll(true);
        txtSaAccountNumber.setMaxLength(15);
        txtSaAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSaAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSaAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSaAcctNum1.add(txtSaAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuction.add(panSaAcctNum1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panAccountInfoInner.add(panAuction, gridBagConstraints);

        panAuction1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panAuction1.setMaximumSize(new java.awt.Dimension(340, 24));
        panAuction1.setMinimumSize(new java.awt.Dimension(340, 24));
        panAuction1.setPreferredSize(new java.awt.Dimension(300, 24));
        panAuction1.setLayout(new java.awt.GridBagLayout());

        lblGrossWeight.setText("Gross Wgt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAuction1.add(lblGrossWeight, gridBagConstraints);

        txtGrossWeight.setAllowAll(true);
        txtGrossWeight.setAllowNumber(true);
        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuction1.add(txtGrossWeight, gridBagConstraints);

        lblNetWeight.setText(" Net Wgt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAuction1.add(lblNetWeight, gridBagConstraints);

        txtNetWeight.setMinimumSize(new java.awt.Dimension(80, 21));
        txtNetWeight.setPreferredSize(new java.awt.Dimension(80, 21));
        txtNetWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetWeightFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuction1.add(txtNetWeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panAccountInfoInner.add(panAuction1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setNextFocusableComponent(btnAuthorize);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N

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
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductIDFocusLost

        btnAccountNumber.grabFocus();
        // TODO add your handling code here:
        //        String prodId = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
        //        if(prodId.length()>0){
        //            resetUIForm();
        //            createChargeTable(prodId);
        //        }else
        //        {
        //            chrgTableEnableDisable();
        //        }
        btnAccountNumber.requestFocus();
    }//GEN-LAST:event_cboProductIDFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtChargeDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargeDetailsFocusLost
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            double oldTxtCharge = CommonUtil.convertObjToDouble(observable.getTxtChargeDetails()).doubleValue();
            double txtCharge = CommonUtil.convertObjToDouble(txtChargeDetails.getText()).doubleValue();
            if (txtCharge != oldTxtCharge) {
                displayAlert("If you want to modify any charges,\neither Delete or Reject this A/c closure and \nrestart the A/c closure process...");
                txtChargeDetails.setText(new Double(oldTxtCharge).toString());
                return;
            }
        }
        calcAndDisplayAvailableBalance();
    }//GEN-LAST:event_txtChargeDetailsFocusLost
    public void modifyTransData(Object objTextUI) {
       
        totRecivable = transDetailsUI.calculatetotalRecivableAmountFromAccountClosing();
//added by rishad 19/03/2014 
        if (observable.isWaiveoffPenal() && observable.getPenalWaiveAmount() > 0) {
            totRecivable -= observable.getInterestWaiveAmount();
        }
        if (observable.isWaiveOffInterest() && observable.getInterestWaiveAmount() > 0) {
            totRecivable -= observable.getInterestWaiveAmount();
        }
        if (observable.isNoticeWaiveoff() && observable.getNoticeWaiveAmount() > 0) {
            totRecivable -= observable.getNoticeWaiveAmount();
        }
        if (observable.isPrincipalwaiveoff() && observable.getPrincipalWaiveAmount() > 0) {
            totRecivable -= observable.getPrincipalWaiveAmount();
        }

        if (CommonConstants.OPERATE_MODE.equals(CommonConstants.IMPLEMENTATION) && prodType.equals("TermLoan")) {
            this.txtInterestPayable.setText(String.valueOf(totRecivable));
            observable.setTxtInterestPayable(String.valueOf(totRecivable));
            observable.setLoanInt(String.valueOf(totRecivable));
        }
        calcAndDisplayAvailableBalance();
        totRecivable = 0;
    }
    private void txtInterestPayableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestPayableFocusLost
        txtInterestPayableFocusLost();
    }//GEN-LAST:event_txtInterestPayableFocusLost

    private void txtInterestPayableFocusLost() {
        //Added By Suresh
        intPayableChange = false;
      
        if (txtInterestPayable.getText().length() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                && (CommonUtil.convertObjToDouble(txtInterestPayable.getText()).doubleValue() != CommonUtil.convertObjToDouble(observable.getTxtInterestPayable()).doubleValue())) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            if (!prodType.equals("TermLoan")) {
                yesNo = COptionPane.showOptionDialog(null, "Interest Amount  has been Modified...  Do you want to continue?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
            } else {
                yesNo = COptionPane.showOptionDialog(null, "Interest/Penal Interest Amount  has been Modified" + "\n" + "Interest Amount Pending" + "\n" + "...  Do you want to continue?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
            }
            if (yesNo == 0) {
                observable.setTxtInterestPayable(txtInterestPayable.getText());
                txtInterestPayable.setText(observable.getTxtInterestPayable());
             
                calcAndDisplayAvailableBalance();
            } else {
                if (observable.getLoanInt() != null) {
                    if (observable.getLoanInt() != "") {
                        txtInterestPayable.setText(observable.getLoanInt());
                        observable.setTxtInterestPayable(observable.getLoanInt());
                    }
                }

                calcAndDisplayAvailableBalance();
            }
        }
       
        intPayableChange = true;
        btnDelete.setVisible(false);
    }

    private void txtAccountClosingChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountClosingChargesFocusLost
        if (intPayableChange) {
            callServiceTaxCalculation(txtAccountClosingCharges.getText());
            calcAndDisplayAvailableBalance();
        }
    }//GEN-LAST:event_txtAccountClosingChargesFocusLost

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);

    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
       

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        ClientUtil.enableDisable(panAccountInfo, false);
        transactionUI.cancelAction(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        lockAccount=null;
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(txtAccountNumber.getText());
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        }
//        if (isTransaction == false || viewType == AUTHORIZE ) {
//            HashMap lockMap = new HashMap();
//            lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
//            lockMap.put("CUR_DATE", currDt.clone());
//            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            ClientUtil.execute("deleteLock", lockMap);
//        }
        viewType = CANCEL;
        isAuthRecord = false;
        setModified(false);
        observable.resetForm();
        txtGrossWeight.setText("");
        txtNetWeight.setText("");
        transDetailsUI.setTransDetails(null, null, null);
        enableDisable(false);
        setButtonEnableDisable();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setStatus();
        }
        returnWaiveMap=new HashMap();
        returnWaiveMap=null;
        setModified(false);
        observable.setAuthorizeMap(new HashMap());
        chrgTableEnableDisable();
        calMap = null;
        btnSave.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI= false;
            newauthorizeListUI.setFocusToTable();
        }
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
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        intPayableChange = true;
        txtMemberNo.setText("");
        cboProductID.setSelectedIndex(0);
        memNo = null;
        ClientUtil.enableDisable(panAuction, false);
        panAuction.setVisible(false);
        chkActnAct.setSelected(false);
        txtAuctnAmt.setText("");
        txtBalancAmt.setText("");
        txtSaAccountNumber.setText("");
        txtDrAccHead.setText("");
        lblServiceTaxval.setText("");
        serviceTax_Map=null;
        goldSerTax = false; 
        taxamtForGoldLoan =0;
        ser_TaxGold_Map = new HashMap();
        txtAuctnAmt.setEnabled(false);
        txtBalancAmt.setEnabled(false);
        rejectionApproveUI = null;
        if(fromSmartCustUI){
            this.dispose();
            fromSmartCustUI = false;
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    private void chrgTableEnableDisable() {
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
    }

    private void resetUIForm() {
        txtAccountNumber.setText("");
        lblCustomerNameDisplay.setText("");
        txtNoOfUnusedChequeLeafs.setText("");
        txtInterestPayable.setText("");
        txtAccountClosingCharges.setText("");
        txtChargeDetails.setText("");
        lblBalanceDisplay.setText("");
        txtPayableBalance.setText("");
        payableBalance = 0;
        observable.setTxtAccountClosingCharges("");
        observable.setTxtPayableBalance("");
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        if (observable.getChargelst() != null) {
            observable.getChargelst().clear();
            observable.setChargelst(null);
        }
        lblHouseName.setText("");
        lblServiceTaxval.setText("");
        serviceTax_Map=null;
        txtPayableBalance.setEnabled(false);
        txtInterestPayable.setEnabled(false);
        txtAccountClosingCharges.setEnabled(false);
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

    private void calcAndDisplayAvailableBalance() {
        String str = "";
        double total = 0;
        double newTot = 0;
        HashMap interestMap = new HashMap();
        str = this.txtInterestPayable.getText();
        double principal=CommonUtil.convertObjToDouble(observable.getAvailableBalance());
        if (str.length() > 0) {
            total = CommonUtil.convertObjToDouble(str).doubleValue();

        }
             if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && //If other than New Mode we need not add the Int Payable
                !prodType.equals("TermLoan")) {
            total = 0;                                                          //Because in New mode already added the Int Payable to Available Balance.
        }        /*
         * TODO Need to verify if required
         */
        str = this.txtChargeDetails.getText();
        observable.setTxtChargeDetails(str);
        if (str.length() > 0) {
            if (prodType.equals("TermLoan")) {
                total += CommonUtil.convertObjToDouble(str).doubleValue();
            } else {
                total -= CommonUtil.convertObjToDouble(str).doubleValue();
            }
        }
     
        str = observable.getTxtInsuranceCharges();
        if (str.length() > 0) {
            if (prodType.equals("TermLoan")) {
                total += CommonUtil.convertObjToDouble(str).doubleValue();
            }
        }
      
        str = observable.getAvailableBalance();
        //        if(str.length()>0){
        newTot = total;
        if (str != null && str.length() > 0) {
            double d = CommonUtil.convertObjToDouble(str).doubleValue();
          
            if (d < 0) {
                d = -d;
            }
            total += d;
        }
        str = txtAccountClosingCharges.getText();
        observable.setTxtAccountClosingCharges(str);
        if (calMap != null && calMap.size() > 0) {
            //service Tax
            double stTax = CommonUtil.convertObjToDouble(calMap.get("SERVICE_TAX")).doubleValue();
            stTax = stTax + CommonUtil.convertObjToDouble(str).doubleValue();
            str = CommonUtil.convertObjToStr(new Double(stTax));
        }
        if (str.length() > 0 && str != null) {
            if (prodType.equals("TermLoan")) {
                total += CommonUtil.convertObjToDouble(str).doubleValue();//babu
                if (getDepositAmt() != 0.0 && getDepositAmt() < total)//fdgdf total+=CommonUtil.convertObjToDouble(str).doubleValue();
                {
                    total = getDepositAmt();
                }
               } else {
                total -= CommonUtil.convertObjToDouble(str).doubleValue();
            }
        }
        if (prodType.equals("TermLoan") && observable.getAvilableLoanSubsidy() > 0) {
            total -= observable.getAvilableLoanSubsidy();
        }
        if(prodType.equals("TermLoan")&&observable.getPenalWaiveAmount()>0)
        {
            total-=observable.getPenalWaiveAmount();
        }
         if(prodType.equals("TermLoan")&&observable.getInterestWaiveAmount()>0)
         {
         
         total-=observable.getInterestWaiveAmount();}
         if(prodType.equals("TermLoan")&&observable.getNoticeWaiveAmount()>0)
         {
         total-=observable.getNoticeWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getArcWaiveAmount()>0)
         {
         total-=observable.getArcWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getArbitarayWaivwAmount()>0)
         {
         total-=observable.getArbitarayWaivwAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getLegalWaiveAmount()>0)
         {
         total-=observable.getLegalWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getInsuranceWaiveAmont()>0)
         {
         total-=observable.getInsuranceWaiveAmont();
         }
         if(prodType.equals("TermLoan")&&observable.getAdvertiseWaiveAmount()>0)
         {
         total-=observable.getAdvertiseWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getMiscellaneousWaiveAmount()>0)
         {
         total-=observable.getMiscellaneousWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getPostageWaiveAmount()>0)
         {
         total-=observable.getPostageWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getRecoveryWaiveAmount()>0)
         {
         total-=observable.getRecoveryWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getMeasurementWaiveAmount()>0)
         {
         total-=observable.getMeasurementWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getEpCostWaiveAmount()>0)
         {
         total-=observable.getEpCostWaiveAmount();
         }
         if(prodType.equals("TermLoan")&&observable.getDecreeWaiveAmount()>0)
         {
         total-=observable.getDecreeWaiveAmount();
         }
       
         if(prodType.equals("TermLoan")&&observable.getPrincipalWaiveAmount()>0)
         {
         total-=observable.getPrincipalWaiveAmount();
         principal+=observable.getPrincipalWaiveAmount();
         observable.setAvailableBalance(CommonUtil.convertObjToStr(principal));
         }
        if (!prodType.equals("TermLoan")) {
            System.out.println("observable.getTxtInterestReceivable():: " + observable.getTxtInterestReceivable());
            if (CommonUtil.convertObjToDouble(observable.getTxtInterestReceivable()) > 0) {
                total = total - (CommonUtil.convertObjToDouble(observable.getTxtInterestReceivable())).doubleValue();
            }
            System.out.println("nitya :: receivable :: "+ (CommonUtil.convertObjToDouble(observable.getTxtInterestReceivable())).doubleValue());
        }   
        if (total != 0) {
 
            payableBalance = total;
            observable.setTxtPayableBalance(CommonUtil.convertObjToStr(new Double(total)));//gh
        } else {
            observable.setTxtPayableBalance("0.0");
        }
      lblBalanceDisplay.setText(observable.getAvailableBalance());
        txtPayableBalance.setText(observable.getTxtPayableBalance());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.setCallingAmount(observable.getTxtPayableBalance());
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            calculateServiceTaxAmt();
        }
        transactionUI.setCallingApplicantName(lblCustomerNameDisplay.getText());
        observable.setCalMap(calMap);
        observable.ttNotifyObservers();
//        if (chkActnAct.isSelected()) {
//            calcDifAuctamt();
//        }
    }

    private HashMap getLoanRenewalMap(){
        HashMap loanRenewalMap =  new HashMap();
        loanRenewalMap.put("CLOSING_LOAN_NO",txtAccountNumber.getText());
        loanRenewalMap.put("DEBIT_ACCT_NUM",transactionUI.getCallingAccNo());
        loanRenewalMap.put("DEBIT_PROD_ID",transactionUI.getCallingProdID());
        loanRenewalMap.put("DEBIT_PROD_TYPE",transactionUI.getCallingTransProdType());
        loanRenewalMap.put("LOAN_OD_RENEWAL","LOAN_OD_RENEWAL");
        return loanRenewalMap;
    }

    private void savePerformed() {

        updateOBFields();
        if (tableFlag == true) {
            finalizeCharges();
        }
       try {
        if((prodType.equals("TermLoan"))&&transactionUI.getSourceScreen().equals("LOAN_CLOSING_FROM_RENEAL")){
            observable.setLoanRenewalMap(getLoanRenewalMap());
        }   
        isTransaction=true;
        observable.doAction();}
       catch(Exception e){
       HashMap lockMap = new HashMap();
            lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
            lockMap.put("CUR_DATE", currDt.clone());
            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("deleteLock", lockMap);
           
       }


        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("ACCOUNT NUMBER");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("ISSUE_ID")) {
                        lockMap.put("ACCOUNT NUMBER", observable.getProxyReturnMap().get("ACT_NUM"));
                    }
                }
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("ACCOUNT NUMBER", observable.getTxtAccountNumber());
            }
            setEditLockMap(lockMap);
            setEditLock();
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            // Commented by nithya on 03-11-2021 -- Loan renewal done for both sal_recovery Y and N
//            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && (prodType.equals("TermLoan"))&&transactionUI.getSourceScreen().equals("LOAN_CLOSING_FROM_RENEAL")) {
//                TermLoanUI.oldloanAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount());
//            }
              
              if ((prodType.equals("TermLoan"))&&transactionUI.getSourceScreen().equals("LOAN_CLOSING_FROM_RENEAL")) {
                TermLoanUI.oldloanAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount());
            }
            
             if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && (prodType.equals("TermLoan"))&&transactionUI.getSourceScreen().equals("DEPOSIT_LOAN_CLOSING_FROM_RENEAL")) {
                DepositLoanUI.oldloanAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount());
            }
            observable.resetForm();
            txtGrossWeight.setText("");
            txtNetWeight.setText("");
            observable.setResultStatus();
            transactionUI.resetObjects();
            transDetailsUI.setTransDetails(null, null, null);
            btnCancelActionPerformed(null);
        } else {
            HashMap lockMap = new HashMap();
            lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
            lockMap.put("CUR_DATE", currDt.clone());
            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("deleteLock", lockMap);
            
            btnNew.enable(false);
            btnEdit.enable(false);
            btnDelete.enable(false);
            btnSave.enable(true);
            btnCancel.enable(true);
        }

    }

    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String cashId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        boolean cashTransfer = false;
        HashMap transIdMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                        cashId = transId;
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
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                cashTransfer = true;
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
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

        /*
         * 1 int yesNo = 0; String[] options = {"Yes", "No"}; yesNo =
         * COptionPane.showOptionDialog(null,"Do you want to print?",
         * CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION,
         * COptionPane.WARNING_MESSAGE, null, options, options[0]);
         * //system.out.println("#$#$$ yesNo : "+yesNo); if (yesNo==0) {
         * TTIntegration ttIntgration = null; HashMap paramMap = new HashMap();
         */
        /*
         * if(transferCount>0 && cashCount>0){ paramMap.put("TransId", cashId);
         * } else{ paramMap.put("TransId", transId); }
         */
        /*
         * 2 paramMap.put("TransDt", currDt); paramMap.put("BranchId",
         * ProxyParameters.BRANCH_ID);
         */
        // ttIntgration.setParam(paramMap);
        //3 String reportName = "";
        //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
        //                ttIntgration.integrationForPrint("ReceiptPayment");
        //            } else {

        /*
         * 4 if(transferCount>0 && cashCount>0){ paramMap.put("TransId",
         * cashId); ttIntgration.setParam(paramMap); reportName = "CashReceipt";
         * ttIntgration.integrationForPrint(reportName, false); reportName = "";
         * } //system.out.println("transferCount>>>"+transferCount);
         * if(transferCount>0){ for (int j=0; j<tempList.size(); j++){
         * //system.out.println("jhgjhfhffd>>>"+transId); transMap = (HashMap)
         * tempList.get(j); transId = (String)transMap.get("BATCH_ID");
         * paramMap.put("TransId", transId); ttIntgration.setParam(paramMap);
         * reportName = "ReceiptPayment"; } } else if
         * (prodType.equals("TermLoan")) { paramMap.put("TransId", transId);
         * ttIntgration.setParam(paramMap); reportName = "CashReceipt"; } else {
         * paramMap.put("TransId", transId); ttIntgration.setParam(paramMap);
         * reportName = "CashPayment"; }
         * ttIntgration.integrationForPrint(reportName, false); }
         */
        //system.out.println("cashTransfer  ======="+cashTransfer); 
        //system.out.println("cashCount  ======="+cashCount); //system.out.println("transferCount  ======="+transferCount); 

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap transTypeMap = new HashMap();
            transMap = new HashMap();
            HashMap transCashMap = new HashMap();
            String reportName = "";
            transCashMap.put("BATCH_ID", observable.getTxtAccountNumber());
            transCashMap.put("TRANS_DT", currDt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            transCashMap.put("INSTRUMENT_NO2", "INSTRUMENT_NO2");
            if (prodType.equals("TermLoan")) {
                transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                transCashMap.put("SCREEN_NAME", "Loan Closing");
            }
            transIdMap = new HashMap();
            List list = null;
            boolean TransferWithCash = false;//Added by sreekrishnan
            boolean rebateCashTrans=false;
            if (cashCount == 0 && transferCount > 0) {
                list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    }                    
                }
            } else {
                list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                         if (prodType.equals("TermLoan")) {
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                         }else{
                             transIdMap.put(transMap.get("SINGLE_TRANS_ID")+"_1", "TRANSFER");
                         }
                         }
                    TransferWithCash = true;
                }
                list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                        if(transMap.containsKey("INSTRUMENT_NO1")&&transMap.get("INSTRUMENT_NO1")!=null&&transMap.get("INSTRUMENT_NO1").equals("REBATE_INTEREST") )
                        {
                        rebateCashTrans=true;
                        }
                        transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                       }
                }
            }
          
            int yesNo = 0;
            String[] voucherOptions = {"Yes", "No"};
            if (list != null && list.size() > 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    Object keys1[] = transIdMap.keySet().toArray();
                    System.out.println("keys1R%@#@#%"+keys1.length);
                    for (int i = 0; i < keys1.length; i++) {
                        String transId1 = CommonUtil.convertObjToStr(keys1[i]);
                        System.out.println("transId1 :"+transId1);
                        if(transId1.contains("_")){
                         transId1 = transId1.substring(0,transId1.indexOf("_"))   ;
                          System.out.println("transId1 aft33333:"+transId1);
                        }
                         System.out.println("transId1 aft:"+transId1);
                       // paramMap.put("TransId", keys1[i]);
                         paramMap.put("TransId",transId1);
                         ttIntgration.setParam(paramMap);
                        System.out.println("transIdMap%@#@#%"+transIdMap.get(keys1[i]));
                       if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER") && keys1.length==1) {
                            reportName = "ReceiptPayment";
                            paramMap.put("TransId",transId1);
                            ttIntgration.setParam(paramMap);
                            // Added condition for solving print duplication issue
                            if(TransferWithCash)
                            ttIntgration.integrationForPrint(reportName, false);
                        } else if (prodType.equals("TermLoan") && observable.getLoanBehaves().equals("OD")) {
                            if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Cr")) {
                                reportName = "CashPayment";
                            }
                            if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Dr")) {
                                reportName = "CashReceipt";
                                if (TransferWithCash)
                                {
                                    paramMap.put("TransId",transId1);
                                    ttIntgration.setParam(paramMap);
                                    ttIntgration.integrationForPrint(reportName, false);
                                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                                }
                            }
                        } else if (prodType.equals("TermLoan")) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("CASH")) {
                                if(rebateCashTrans)
                                {
                                 reportName = "CashPayment"; 
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint(reportName, false);
                                }
                                reportName = "CashReceipt"; 
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                // Added condition for solving print duplication issue
                                if(TransferWithCash)
                                ttIntgration.integrationForPrint(reportName, false);
                            }                                                       
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {  
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                // Added condition for solving print duplication issue
                                if(TransferWithCash)
                                ttIntgration.integrationForPrint("ReceiptPayment", false);                                                         
                            }
                        } else {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("CASH")) {
                                reportName = "CashPayment";
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint(reportName, false);
                            } 
                           if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER"))
                            {
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint("ReceiptPayment", false);
                            }
                        }
                       if(!TransferWithCash)
                        ttIntgration.integrationForPrint(reportName, false);
                    }
                }
            }

        }
        cashTransfer = false;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        btnSave.setEnabled(false);
        String Acno = txtAccountNumber.getText();
        HashMap hashmap = new HashMap();
        hashmap.put("ACNO", Acno);
        observable.setAuthorizeBy2("");
        List lst1 = ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", hashmap);
        if (lst1 != null && lst1.size() > 0) {
            int a = ClientUtil.confirmationAlert("The Account is Death marked, Do you want to continue?");
            int b = 0;
            if (!prodType.equals("TermLoan")) {
                if (a == 0) {
                    rejectionApproveUI = new RejectionApproveUI(this);
                    System.out.println("detail List :" + rejectionApproveUI.getLoginDetails());
                    HashMap reMap = rejectionApproveUI.getLoginDetails();
                    if (reMap != null && reMap.containsKey("USER_ID")) {
                        observable.setAuthorizeBy2(CommonUtil.convertObjToStr(reMap.get("USER_ID")));
                    }
                    if (rejectionApproveUI.isCancelActionKey()) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                if (a != b) {
                    return;
                }
            }
        }
        if (prodType.equals("TermLoan")) {
            System.out.println("ProxyParameters.BRANCH_ID :: "+ ProxyParameters.BRANCH_ID);
                    System.out.println("observable.getSelectedBranchID() :: " + observable.getSelectedBranchID());
                    System.out.println("transactionUI.getTransactionOB().getSelectedTxnBranchId() :: " +transactionUI.getTransactionOB().getSelectedTxnBranchId());
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
        }
        
        //KD-4133 : Gold loan rejection authorized status updation issue
        if (prodType.equals("TermLoan") && !chkAuthorizationPending()) {            
            txtAccountNumber.setText("");
            btnCancelActionPerformed(null);
            return;
        }
        
        // Added by nithya on 05-10-2019 for KD 465 : Gold loan closing issue 
        if (prodType.equals("TermLoan") && tableFlag == true) {
            double totCharge = 0.0;
            for (int i = 0; i < table.getRowCount(); i++) {
                if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
                    totCharge = totCharge + CommonUtil.convertObjToDouble(table.getValueAt(i, 3).toString()).doubleValue();
                }
            }
            double actClosingChrg = CommonUtil.convertObjToDouble(txtAccountClosingCharges.getText());
            if(actClosingChrg < totCharge){
                ClientUtil.showAlertWindow("Mismatch in charge found ! Please check.");
                return;
            }
        }
        // End
//        if (prodType.equals("TermLoan") && chkActnAct.isSelected()) {
//            LinkedHashMap notdeleted = (LinkedHashMap) transactionUI.getOutputTO();
//            TransactionTO tto = null;
//            if (notdeleted.containsKey("1")) {
//                tto = new TransactionTO();
//                tto = (TransactionTO) notdeleted.get("1");
//            }
//            System.out.println("tto.getTransType()"+tto.getTransType());
//            if (tto != null && tto.getTransType().equals("CASH")) {
//                ClientUtil.showMessageWindow("Please do the transaction using Transfer for the auctioned loan");
//                return;
//            }
//        }
        double totalClosingAmt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()).doubleValue();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getShadowCredit()).doubleValue() != 0
                || CommonUtil.convertObjToDouble(transDetailsUI.getShadowDebit()).doubleValue() != 0)) {
            displayAlert(accountClosingRB.getString("shadowCrDr"));
            return;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getFreezeAmount()).doubleValue() > 0)) {
            displayAlert(accountClosingRB.getString("freezeAmount"));
            return;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getLienAmount()).doubleValue() > 0)) {
            displayAlert(accountClosingRB.getString("lienAmount"));
            return;

//        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getTodAmount()).doubleValue() > 0)) {
//            HashMap todMap = new HashMap();
//            todMap.put("VALUE", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
//            List todList = ClientUtil.executeQuery("getSelectCloseAccForTOD", todMap);
//            if(todList.size()>0 && todList!=null){
//              todMap = (HashMap)todList.get(0);
//                System.out.println("mttjjt");
//              if((String.valueOf(todMap.get("CLOSE_ACC_FOR_TOD"))).equals("Y")){
//                  System.out.println("gretjrtr");
//             displayAlert(accountClosingRB.getString("TodAmount"));
//              }else{
//                  System.out.println("12342");
//            displayAlert(accountClosingRB.getString("TodAmount"));
//            return;
//            }
//            }else{
//                System.out.println("986767");
//            displayAlert(accountClosingRB.getString("TodAmount"));
//            return;
//            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getFlexiDepositAmt()).doubleValue() > 0)) {
            displayAlert(accountClosingRB.getString("FlexiAmount"));
            return;
        } else if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (!CommonUtil.convertObjToStr(transDetailsUI.getUnclearBalance()).equals("0.00"))) {
            displayAlert(accountClosingRB.getString("UnclearBalance"));
            return;
        } else {
            int transactionSize = 0;
            if (transactionUI.getOutputTO() == null && totalClosingAmt != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            } else {
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }

            if (transactionSize == 0 && totalClosingAmt != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            } else if (transactionSize != 0 || totalClosingAmt == 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag() && (!prodType.equals("TermLoan"))) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                } else {
                    updateOBFields();
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    //                    double totalClosingAmt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()).doubleValue();
                   // totalClosingAmt=totalClosingAmt+CommonUtil.convertObjToDouble(observable.getLblServiceTaxval());
                    if (ClientUtil.checkTotalAmountTallied(totalClosingAmt, transTotalAmt) == false && totalClosingAmt != 0) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    } else {
                        txtAccountClosingChargesFocusLost(null);
                        updateSave();
                        final AccountClosingRB accountClosingRB = new AccountClosingRB();
                        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
                        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAccountInfo);
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                            displayAlert(mandatoryMessage);
                            return;
                        } else if (this.lblBalanceDisplay.getText().length() < 0 && this.lblBalanceDisplay != null) {
                            if ((CommonUtil.convertObjToDouble(this.lblBalanceDisplay.getText())).doubleValue() < 0) {
                                displayAlert(accountClosingRB.getString("DebitBalance"));
                                return;
                            }
                        }
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getTodAmount()).doubleValue() > 0)) {
                            HashMap todMap = new HashMap();
                            todMap.put("VALUE", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
                            List todList = ClientUtil.executeQuery("getSelectCloseAccForTOD", todMap);
                            if (todList.size() > 0 && todList != null) {
                                todMap = (HashMap) todList.get(0);
                                if ((String.valueOf(todMap.get("CLOSE_ACC_FOR_TOD"))).equals("Y")) {
                                    displayAlert(accountClosingRB.getString("TodAmount"));
                                } else {
                                    displayAlert(accountClosingRB.getString("TodAmount"));
                                    return;
                                }
                            } else {
                                displayAlert(accountClosingRB.getString("TodAmount"));
                                return;
                            }
                        }
                        HashMap loanClosMap = new HashMap();
                        loanClosMap.put("ACT_NUM", txtAccountNumber.getText());
                        java.util.List lstLoanClose = ClientUtil.executeQuery("LoneFacilityDetailAD", loanClosMap);
                        //HashMap supMap1 = new HashMap();
                        if(lstLoanClose != null && lstLoanClose.size() > 0){
                        	loanClosMap = (HashMap) lstLoanClose.get(0);
                        }
                        int balance = (CommonUtil.convertObjToInt(loanClosMap.get("LOAN_BALANCE_PRINCIPAL")));
                        int shadcr=0;
                        if(CommonUtil.convertObjToInt(lblBalanceDisplay.getText())<0){
                            shadcr=CommonUtil.convertObjToInt(lblBalanceDisplay.getText())*-1;
                        }
                        //if (balance == shadcr) {
                          
                            //added by rishad 11/07/2015 for avoiding doubling issue
                            CommonUtil comm = new CommonUtil();
                            final JDialog loading = comm.addProgressBar();
                            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                                @Override
                                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                                {
                                    savePerformed();
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
                         //   suspanceFlag = false;
                        //}
                        //else{
                         //   ClientUtil.displayAlert("Receipt amount is not sufficiant to close the loan!!! ");
                          //  return;
                        //}
                        //                        super.removeEditLock(txtAccountNumber.getText());
                        setModified(false);
                    }
                }
            }
           
        }
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        calMap = null;
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            setMode(ClientConstants.ACTIONTYPE_NEW);
            enableDisable(true);
            observable.fillDropdown();
            cboProductID.setModel(observable.getCbmProductID());
            txtAccountNumber.setEnabled(true);
            txtAccountNumber.setEditable(true);
            setButtonEnableDisable();
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            setModified(true);
            observable.resetForm();
            txtGrossWeight.setText("");
            txtNetWeight.setText("");
            txtGrossWeight.setEnabled(false);
            txtNetWeight.setEnabled(false);
            observable.setStatus();
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            txtNoOfUnusedChequeLeafs.setText("0");
            txtMemberNo.requestFocus();
            txtAuctnAmt.setEnabled(false);
            txtBalancAmt.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        if (tableFlag) {
            chrgTableEnableDisable();
        }
        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            actNum = observable.checkAcNoWithoutProdType(actNum);
            //       actNum=observable.getTxtAccountNumber();
             observable.calculateInterestOnMaturity();
            txtAccountNumber.setText(actNum);
            accountViewMap(actNum);
        }
            //        String prodId = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
            //        if(prodId.length()>0 && txtAccountNumber.getText().length()>0){
            ////            resetUIForm();
            //            createChargeTable(prodId);
            //            accClosingCharges();
            //        }else
            //        {
            //            chrgTableEnableDisable();
            //        }

    }//GEN-LAST:event_txtAccountNumberFocusLost
//10077: Gold Loan Closing - Auctioned Account Closing Transaction Is Not Correct For Printing Purpose.
    private boolean chkAuthorizationPending() {
        boolean chkFlag = true;
        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            actNum = observable.checkAcNoWithoutProdType(actNum);
            if (prodType.equals("TermLoan")) {
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                String authRemarks = "";
                transCashMap.put("BATCH_ID", actNum);
                transCashMap.put("TRANS_DT", currDt);
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                //transCashMap.put("SCREEN_NAME", "Loan Closing"); // commented by nithya on 19-12-2016
                //Commented and added new map By Kannan AR bcz IBT also should consider
                //List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                List transferList = ClientUtil.executeQuery("getPendingTransferDetails", transCashMap);
                if (transferList.size() > 0) {
                    ClientUtil.displayAlert("Transaction is pending for authorization!!");
                    txtAccountNumber.setText("");
                   return false;
                }
                //Commented and changed new map By Kannan AR bcz IBT also should consider
                //List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
                 List cashList = ClientUtil.executeQuery("getPendingCashDetails", transCashMap);
                if (cashList.size() > 0) {
                    ClientUtil.displayAlert("Transaction is pending for authorization!!");
                    txtAccountNumber.setText("");
                 return false;
                }
            }
        }
        return chkFlag;
    }
    // commenting and rewriting the code by nithya for service tax to GST conversion
    //Added by chithra for service tax
//    private void calculateServiceTaxAmt() {
//        double taxAmt = 0;
//        if (prodType.equals("TermLoan")) {
//            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
//            String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
//            taxAmt = observable.calcServiceTaxAmount(actNum, prodId);
//            if (goldSerTax) {
//                taxAmt += taxamtForGoldLoan;
//            }
//        }
//        if (!prodType.equals("TermLoan")) {
//            taxAmt = chkServiceTaxApplicableForAccountClosing();
//        }
//        if (taxAmt > 0) {
//            HashMap ser_Tax_Val = new HashMap();
//            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
//            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
//            try {
//                objServiceTax = new ServiceTaxCalculation();
//                ser_Tax_Val.put("TEXT_BOX","TEXT_BOX");//Added By Kannan AR On 18-Sep-2017
//                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
//                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
//                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                    serviceTaxAmt = CommonUtil.convertObjToDouble(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                    lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                } else {
//                    lblServiceTaxval.setText("0.00");
//                    serviceTax_Map = new HashMap();
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        } else {
//            lblServiceTaxval.setText("0.00");
//            observable.setLblServiceTaxval("0.00");
//            observable.setServiceTax_Map(new HashMap());
//            serviceTax_Map = new HashMap();
//        }
//        //}
//        double tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance());
//        if (!prodType.equals("TermLoan")) {
//            tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()) - CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
//        } else {
//            tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()) + CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
//        }        
//        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(tr_amt));
//        txtPayableBalance.setText(CommonUtil.convertObjToStr(tr_amt));
//        observable.setTxtPayableBalance(CommonUtil.convertObjToStr(tr_amt));
//    }
    // End
    
    // Start
    private void calculateServiceTaxAmt() {
        double taxAmt = 0;
        List taxSettingsList =  new ArrayList();
        if (prodType.equals("TermLoan")) {
            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
            //taxAmt = observable.calcServiceTaxAmount(actNum, prodId);
            taxSettingsList = observable.calcServiceTaxAmount(actNum, prodId);
            System.out.println("taxListForGoldLoan :: " + taxListForGoldLoan);
            if (goldSerTax) {
                //taxAmt += taxamtForGoldLoan;
                if(taxListForGoldLoan != null && taxListForGoldLoan.size() > 0){
                    taxSettingsList.addAll(taxSettingsList.size(), taxListForGoldLoan);
                }
            }
        }
        if (!prodType.equals("TermLoan")) {
            taxSettingsList = chkServiceTaxApplicableForAccountClosing();
        }
        //if (taxAmt > 0) {
         if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
            try {
                objServiceTax = new ServiceTaxCalculation();
                //ser_Tax_Val.put("TEXT_BOX","TEXT_BOX");//Added By Kannan AR On 18-Sep-2017
                ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList); // Added by nithya 
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    serviceTaxAmt = CommonUtil.convertObjToDouble(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    
//                    lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    
                } else {
                    lblServiceTaxval.setText("0.00");
                    serviceTax_Map = new HashMap();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
            observable.setLblServiceTaxval("0.00");
            observable.setServiceTax_Map(new HashMap());
            serviceTax_Map = new HashMap();
        }
        //}
        double tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance());
        if (!prodType.equals("TermLoan")) {
            tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()) - CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
        } else {
            tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()) + CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
        }        
        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(tr_amt));
        txtPayableBalance.setText(CommonUtil.convertObjToStr(tr_amt));
        observable.setTxtPayableBalance(CommonUtil.convertObjToStr(tr_amt));
    }
    
    // End

    // Code rewriting by nithya for service tax to GST changes
//    public String checkServiceTaxApplicable(String accheadId) {
//        String checkFlag = "N";
//        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("AC_HD_ID", accheadId);
//            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
//            if (accHeadList != null && accHeadList.size() > 0) {
//                HashMap accHeadMap = (HashMap) accHeadList.get(0);
//                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
//                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
//                }
//            }
//        }
//        return checkFlag;
//    }
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
   // End
    private List chkServiceTaxApplicableForAccountClosing() {
        HashMap whereMap = new HashMap();
        whereMap.put("value", CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected()));
        String checkFlag = "N";
        String bankChareHead = "";
        double taxAmount = 0;
        List taxSettingsList = new ArrayList();
        HashMap checkForTaxMap = new HashMap();
        HashMap taxMap;
        List resultList = ClientUtil.executeQuery("getSelectOperativeAcctHeadParamTO", whereMap);
        if (resultList != null && resultList.size() > 0) {
            OperativeAcctHeadParamTO newObj = (OperativeAcctHeadParamTO) resultList.get(0);
            if (newObj != null && newObj.getMisserChrg() != null) {
                bankChareHead = CommonUtil.convertObjToStr(newObj.getMisserChrg());
                // checkFlag = checkServiceTaxApplicable(bankChareHead); 
                checkForTaxMap = checkServiceTaxApplicable(bankChareHead);
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxMap = new HashMap();
                        taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToDouble(txtChargeDetails.getText()));
                        taxSettingsList.add(taxMap);
                    }
                }
//                if (checkFlag != null && checkFlag.equals("Y")) {
//                    taxAmount = taxAmount + CommonUtil.convertObjToDouble(txtChargeDetails.getText());
//                }
            }
            if (newObj != null && newObj.getAccloseChrg() != null) {
                bankChareHead = CommonUtil.convertObjToStr(newObj.getAccloseChrg());
                // checkFlag = checkServiceTaxApplicable(bankChareHead);
                checkForTaxMap = checkServiceTaxApplicable(bankChareHead);
//                if (checkFlag != null && checkFlag.equals("Y")) {
//                    taxAmount = taxAmount + CommonUtil.convertObjToDouble(txtAccountClosingCharges.getText());
//                }
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxMap = new HashMap();
                        taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToDouble(txtAccountClosingCharges.getText()));// changed by nithya on 17-09-2018 for KD-250 SB Closing Gst amount showing in correct
                        taxSettingsList.add(taxMap);
                    }
                }
            }
        }
        return taxSettingsList;
    }
    //End.............
    private HashMap getInterestOnMaturityMap() {
        HashMap maturityMap = new HashMap();
        //        prodType="TermLoan";
        HashMap whereMap = new HashMap();
        log.info("productID : " + observable.getLoanBehaves() + "" + prodType);
        whereMap.put("prodId", observable.getCboProductID());
        List InterestOnMaturityList = ClientUtil.executeQuery("getInterestOnMaturity", whereMap);
        if (InterestOnMaturityList != null && InterestOnMaturityList.size() >= 0) {
            maturityMap = (HashMap) InterestOnMaturityList.get(0);
        }
        return maturityMap;
    }

    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed

        String calcOnMaturity = "";
//        if (!prodType.equals("") && prodType.equals("TermLoan")) {
//            HashMap intMaturityMap = getInterestOnMaturityMap();
//            if (intMaturityMap.containsKey("INTEREST_ON_MATURITY")) {
//                if (intMaturityMap != null && intMaturityMap.get("INTEREST_ON_MATURITY") != null) {
//                    calcOnMaturity = CommonUtil.convertObjToStr(intMaturityMap.get("INTEREST_ON_MATURITY"));
//                }
//            }
//        }
        maturityMap = new HashMap();
//        if (calcOnMaturity != null && !calcOnMaturity.equals("") && calcOnMaturity.equals("Y")) {
//            if (observable.getLoanBehaves().equals("LOANS_AGAINST_DEPOSITS")) {
//                int matRes = ClientUtil.confirmationAlert("Calculate Interest On Maturity Date");
//                if (matRes == 0) {
//                    System.out.println("BBBBBBBBBB" + observable.getLoanBehaves());
//                    maturityMap.put("CALC_ON_MATURITY", "Y");
//                } else {
//                    maturityMap.put("CALC_ON_MATURITY", "N");
//                }
//            }
//        }

        if (tableFlag) {
            chrgTableEnableDisable();
        }
        String prodId = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
        if (prodId.length() > 0) {
            observable.calculateInterestOnMaturity();
            //system.out.println("testfinal");
            resetUIForm();
            popUp();
            
        } else {
            chrgTableEnableDisable();
        }
        txtPayableBalance.setEnabled(false);
        if(prodType.equalsIgnoreCase("TermLoan")){
            txtInterestPayable.setEnabled(false);
        	txtAccountClosingCharges.setEnabled(false); 
        }else{
          txtInterestPayable.setEnabled(true);
          txtAccountClosingCharges.setEnabled(true);
        }
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    public void accClosingCharges() {
        if (tableFlag == true) {
          
            //            getSanctionAmount();
            //            chargeAmount();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                calculateTot();
                if (TrueTransactMain.SERVICE_TAX_REQ != null && TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                    taxListForGoldLoan = calculateServiceTaxForGoldLoan();
                    calculateServiceTaxAmt();
                }
            }
            //            finalizeCharges();
            panChargeDetails.setEnabled(true);
            srpChargeDetails.setEnabled(true);
            table.setEnabled(true);
            calcAndDisplayAvailableBalance();
            

            //            double totCharge = 0;
            //            double recBalance = 0;
            //            totCharge = CommonUtil.convertObjToDouble(txtAccountClosingCharges.getText()).doubleValue();    // Acc Closing Charges (Table Values)
            //            recBalance = payableBalance;    // Balance + Product Level ACC Closing Charge(Column Value)
            //            txtPayableBalance.setText(CurrencyValidation.formatCrore(String.valueOf((totCharge+recBalance-accountClosingCharge))));
            //            transactionUI.setCallingAmount(txtPayableBalance.getText());
            //            observable.setTxtPayableBalance(txtPayableBalance.getText());
        }

    }

    private void getSanctionAmount() {
        HashMap hash = new HashMap();
        hash.put("ACCT_NUM", txtAccountNumber.getText());
        List sanctionLst = ClientUtil.executeQuery("getSanctionAmount", hash);
        if (sanctionLst != null && sanctionLst.size() > 0) {
            hash = (HashMap) sanctionLst.get(0);
            sanctionAmt = CommonUtil.convertObjToDouble(hash.get("SANCTION_AMOUNT")).doubleValue();
         
        }
    }

    private void chargeAmount() {
        HashMap hash = new HashMap();
        hash.put("SCHEME_ID", CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));
        hash.put("DEDUCTION_ACCU", "C");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", hash);
        HashMap chargeMap = new HashMap();
        String acHd_id = "";
        taxamtForGoldLoan = 0;
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                //                String accHead="";
                String desc = "";
                String editable = "";
                chargeMap = (HashMap) chargelst.get(i);
                //                accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                editable = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_EDITABLE"));
                acHd_id = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));                
                HashMap selectMap = new HashMap();
                selectMap.put("ACCT_NUM", observable.getTxtAccountNumber());
                selectMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
                StringBuffer buffer = new StringBuffer();                   
                List isExsistList = ClientUtil.executeQuery("getKccSacntionTodate", selectMap);
                if(isExsistList != null && isExsistList.size() > 0){
                   HashMap  goldLoanExsistMap = (HashMap)isExsistList.get(0);
                   if(goldLoanExsistMap.containsKey("TO_DT") && goldLoanExsistMap.get("TO_DT") != null){
                       actToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(goldLoanExsistMap.get("TO_DT")));
                   }
                }
                double loanRunPeriod = 0;
                if (actOpenDt != null) {
                    loanRunPeriod = DateUtil.dateDiff(actOpenDt, (Date) currDt.clone());
                }
                double totalLoanPeriod = 0;
                if (actToDate != null && actOpenDt != null) {
                    totalLoanPeriod = DateUtil.dateDiff(actOpenDt, actToDate);
                }
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc)) {
                        double chargeAmt = 0;
                        
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = sanctionAmt * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                System.out.println("chargeAmt$@#$#@ first"+chargeAmt);  
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }                            
                             //Added by sreekrishnan 
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("Y")) {
                                chargeAmt = Math.round((sanctionAmt*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);                                
                            }
                             //Added by sreekrishnan 
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) { 
                                chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            //Added by sreekrishnan 
                            if (!CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("PREMATURE_RATE"))!=null
                                    && CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE"))>0) { 
                                double firstAmount = Math.round((sanctionAmt*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);
                                double padiEmi = 0;
                                HashMap  emiMap  = new HashMap();
                                List paidList = ClientUtil.executeQuery("getTotalEmiPaid", selectMap);
                                if(paidList != null && paidList.size() > 0){
                                   emiMap = (HashMap)paidList.get(0);
                                   if(emiMap.containsKey("TOTAL_EMI_PAID")){
                                       padiEmi = CommonUtil.convertObjToDouble(emiMap.get("TOTAL_EMI_PAID"));                               
                                   }
                                }
                                chargeAmt = Math.round(((firstAmount+sanctionAmt)-padiEmi)*CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE"))/100);
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                           } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            HashMap slabMap = new HashMap();
                            double sancAmt = sanctionAmt;
                            slabMap.put("CHARGE_ID", chargeMap.get("CHARGE_ID"));
                            slabMap.put("AMOUNT", sancAmt);
                            List slablst = ClientUtil.executeQuery("getSlabAmount", slabMap);
                            if (slablst != null && slablst.size() > 0) {
                                slabMap = (HashMap) slablst.get(0);
                                chargeAmt = sanctionAmt
                                        * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(slabMap.get("CHARGE_RATE"))).doubleValue() / 100;
                                long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(slabMap.get("ROUND_OFF_TYPE")));
                                if (roundOffType != 0) {
                                    chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                                }
                                double minAmt = CommonUtil.convertObjToDouble(slabMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                                double maxAmt = CommonUtil.convertObjToDouble(slabMap.get("MAX_CHARGE_AMOUNT")).doubleValue();                                
                                 //Added by sreekrishnan 
                                if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                                }                                
                                if (chargeAmt < minAmt) {
                                    chargeAmt = minAmt;
                                }
                                if (chargeAmt > maxAmt) {
                                    chargeAmt = maxAmt;
                                }
                                table.setValueAt(String.valueOf(chargeAmt), j, 3);
                            }
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                        }
                        // if condition is added to calculate insurance charge based on total market value, by shihad on 29/09/2014
                        else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Total Market Value")) { 
                            HashMap whereMap = new HashMap();
                            whereMap.put("TODAY_DATE", currDt);
                            whereMap.put("PURITY", purity);                            
                            List rateList = ClientUtil.executeQuery("getSelectTodaysMarketRate", whereMap);
                            if(rateList != null && rateList.size()>0) 
                            {
                            HashMap rateMap = (HashMap) rateList .get(0);
                            double totalMarketValue = netWt * CommonUtil.convertObjToDouble(rateMap.get("PER_GRAM_RATE"));
                            chargeAmt = totalMarketValue * CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_RATE")) / 100; 
                            double oneMonthCharge = chargeAmt/12;
                            int duration = 0;
                            int minDuration = 6;
                            long datDiff = DateUtil.dateDiff(asOn, currDt);
                            Calendar startCalendar = new GregorianCalendar();
                            startCalendar.setTime(asOn);
                            Calendar endCalendar = new GregorianCalendar();
                            endCalendar.setTime(currDt);
                            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                            int monthDiff = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                            chargeAmt = oneMonthCharge * monthDiff;
                          //double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            double minCharge = oneMonthCharge * 6; // min charge is taken as 6 month charge(for kalaikode)                         
                            
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }                            
                             //Added by sreekrishnan 
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            if (chargeAmt < minCharge) {
                                chargeAmt = minCharge;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                           }
                        }                       
                       chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));                        
                    }
                    if (editable.equals("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt1));
                    }
                    String chk = "N";
                    if (TrueTransactMain.SERVICE_TAX_REQ != null && TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y") && acHd_id != null && acHd_id.length() > 0) {
                        HashMap checkForTaxMap = new HashMap();
                        //chk = observable.checkServiceTaxApplicable(acHd_id);
                        checkForTaxMap = observable.checkServiceTaxApplicable(acHd_id);// Added by nithya
                        if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null){
                            if(checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0){
                                chk = CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE"));
                                String serviceTaxId = CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID"));
                                ser_TaxGold_Map.put(desc, chk);
                                serviceTaxIdMap.put(desc,serviceTaxId);
                                goldSerTax = true;
                            }
                        }                        
                    }
                    if (((Boolean) table.getValueAt(j, 0)).booleanValue() && TrueTransactMain.SERVICE_TAX_REQ != null && TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        if (chk != null && chk.equals("Y")) {
                            taxamtForGoldLoan = taxamtForGoldLoan + chargeAmt1;
                        }
                    }
                }
                System.out.println("goldSerTax :"+ goldSerTax);
            }
        }
        table.revalidate();
        table.updateUI();
        System.out.println("chargelst!@$!@$!"+chargelst);
        //set acc head for incidental charges
        if (incidentAmt > 0) {
            hash=new HashMap();
            hash.put("PROD_ID",observable.getCboProductID());
            List lst = ClientUtil.executeQuery("getIncidentalAcHead", hash);
            if(lst!=null && lst.size()>0){
                chargeMap = (HashMap)lst.get(0);
                chargeMap.put("CHARGE_AMOUNT",String.valueOf(incidentAmt));
                chargelst.add(chargeMap);
             }
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
    public List calculateServiceTaxForGoldLoan() {// return type changed from void to List
         List taxSettingsList = new ArrayList();
         System.out.println("ser_TaxGold_Map :: "+ ser_TaxGold_Map);
         System.out.println("serviceTaxIdMap :: "+ serviceTaxIdMap);
        if (ser_TaxGold_Map != null && ser_TaxGold_Map.size() > 0) {           
            String desc = "", chk = "";
            taxamtForGoldLoan = 0;
            double chrgAmtPlus = 0;
            for (int j = 0; j < table.getRowCount(); j++) {
                double chargeAmt1 = 0;
                HashMap serviceTaSettingsMap = new HashMap();
                desc = CommonUtil.convertObjToStr(table.getValueAt(j, 1));
                if (ser_TaxGold_Map.containsKey(desc)) {
                    chk = CommonUtil.convertObjToStr(ser_TaxGold_Map.get(desc));
                }
                if (chk != null && chk.equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(desc)).length() > 0) {
                    chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                    if (((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        //chrgAmtPlus = chrgAmtPlus + CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        if (chargeAmt1 > 0) {
                            serviceTaSettingsMap.put("SETTINGS_ID", serviceTaxIdMap.get(desc));
                            serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chargeAmt1));                             
                            taxSettingsList.add(serviceTaSettingsMap);
                        } 
                    }

                }
            }
            taxamtForGoldLoan = chrgAmtPlus;
        }
        return taxSettingsList;
    }
    public void calculateTot() {
        double totCharge = 0;
        table.revalidate();
        //        totCharge = accountClosingCharge;   // PRODUCT LEVEL ACC_CLOSING CHARGE
        for (int i = 0; i < table.getRowCount(); i++) {
            if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
                totCharge = totCharge + CommonUtil.convertObjToDouble(table.getValueAt(i, 3).toString()).doubleValue();
            }
        }        
        txtAccountClosingCharges.setText(CurrencyValidation.formatCrore(String.valueOf(totCharge)));
    }

    private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String desc = "";
                chargeMap = (HashMap) chargelst.get(i);
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                for (int j = 0; j < table.getRowCount(); j++) {
                     if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    } else {
                        if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc) /*&& CommonUtil.convertObjToStr(table.getValueAt(j, 4)).equals("Y")*/) {
                            String chargeAmt = CommonUtil.convertObjToStr(table.getValueAt(j, 3));
                            chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));

                        }
                    }
                }
            }
                 observable.setChargelst(chargelst);
         }
    }
   
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        //To get the AccountHead details for a proper ProductID
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            log.info("Inside cboProductIDActionPerformed");
            updateOBFields();
        }
        if (observable.getCboProductID().length() > 0) {
            observable.setCboProductID((String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
            observable.getAccountHeadForProduct();
            if (prodType.equals("TermLoan")) {
                HashMap hash = new HashMap();
                hash.put("PROD_ID", observable.getCboProductID());
                List lst = ClientUtil.executeQuery("getLoanBehaves", hash);
                if (lst.size() > 0) {
                    hash = (HashMap) lst.get(0);
                    if((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_REMARK")).equals("GOLD_LOAN") &&!TrueTransactMain.BRANCH_ID.equalsIgnoreCase(TrueTransactMain.selBranch)))
                    {
                    ClientUtil.showAlertWindow("Interbranch Not allowed For Gold Loan");
                    btnCancelActionPerformed(null);
                    return;
                    }
                    observable.setLoanBehaves(CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")));
                }
            }
        }
        if (observable.getActionType()  == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
            String prodId = "";
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
           // txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
           txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.selBranch) + prodId);
        }
    
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void txtMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberNoActionPerformed
        // TODO add your handling code here:
        try {
            if (txtMemberNo.getText().length() > 0) {
                memNo = txtMemberNo.getText();
                observable.setMemNo(memNo);
                searchOpt = 0;
                getGoldLoanLiability();
                if (prodType.equals("TermLoan")) {
                    observable.filterProd();
                }
               // cboProductID.setModel(observable.getCbmProductID());
            } else {
                memNo = null;
                observable.fillDropdown();
                cboProductID.setModel(observable.getCbmProductID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtMemberNoActionPerformed

    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:
        //  phoneExist = false;
        searchOpt = 1;
        //  observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//            tabIndCust.addTab("Cust.360", panCustomerHistory);
////            popUp(ClientConstants.ACTION_STATUS[2]);
//            actionType = ClientConstants.ACTION_STATUS[2];
        HashMap sourceMap = new HashMap();
        sourceMap.put("CUST_TYPE", "LOAN CLOSING");
        new CheckCustomerIdUI(this, sourceMap);


//            btnContactDelete.setEnabled(false);
//            txtBranchId.setEnabled(false);
//            btnContactToMain.setEnabled(false);
//            btnPhoneNew.setEnabled(false);
//            introducerUI.enableDisableBtn(true);
//            if(chkIncParticulars.isSelected())
//            tabIndCust.addTab("Income Details",panIncomeParticulars);
//            else tabIndCust.remove(panIncomeParticulars);
//            if(chkLandDetails.isSelected()) 
//                tabIndCust.addTab("Land Details",panLandDetails);
//            else tabIndCust.remove(panLandDetails);
//            ClientUtil.enableDisable(panLandDetails,false);
//            ClientUtil.enableDisable(panIncomeParticulars,false);
//            btnClearPassport.setEnabled(true);
//            txtCustomerID.setEnabled(false);
//            boolean ck=chkSuspendCust.isSelected();
//            if(ck){
//                chkRevokeCust.setEnabled(true);
//                tdtRevokedCustDate.setEnabled(true);
//                chkSuspendCust.setEnabled(false);
//                tdtSuspendCustFrom.setEnabled(false);
//            }
//            else{
//                chkRevokeCust.setEnabled(false);
//                tdtRevokedCustDate.setEnabled(false);  
//                chkSuspendCust.setEnabled(true);
//                tdtSuspendCustFrom.setEnabled(true);
//            }
    }//GEN-LAST:event_btnMemberNoActionPerformed

    private void txtMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberNoFocusLost

private void txtMemberNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusGained
// TODO add your handling code here:
}//GEN-LAST:event_txtMemberNoFocusGained
    private void txtAuctnAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuctnAmtActionPerformed

        if (chkActnAct.isSelected() && txtAuctnAmt.getText() != null) {
            calcDifAuctamt();
            observable.isCloseLoanThroughGoldAuction();
            if ((observable.getSusActnum() != null && !observable.getSusActnum().equals("")) && (observable.getTxtDrAccHead() != null && !observable.getTxtDrAccHead().equals(""))) {
                txtSaAccountNumber.setText(CommonUtil.convertObjToStr(observable.getSusActnum()));
                txtDrAccHead.setText(CommonUtil.convertObjToStr(observable.getTxtDrAccHead()));
                if (chkActnAct.isSelected()) {
                        observable.updateNEwAccountNo();
                       // suspanceFlag = true;   
                }
            }
        }
        
    }//GEN-LAST:event_txtAuctnAmtActionPerformed
    private void txtAuctnAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAuctnAmtFocusLost
        if (chkActnAct.isSelected()&&txtAuctnAmt.getText() != null) {
            calcDifAuctamt();
            observable.isCloseLoanThroughGoldAuction();
            if ((observable.getSusActnum() != null && !observable.getSusActnum().equals("")) && (observable.getTxtDrAccHead() != null && !observable.getTxtDrAccHead().equals(""))) {
//                if (!suspanceFlag && chkActnAct.isSelected()) {
                    txtSaAccountNumber.setText(CommonUtil.convertObjToStr(observable.getSusActnum()));
//                    suspanceFlag = true;
                    txtDrAccHead.setText(CommonUtil.convertObjToStr(observable.getTxtDrAccHead()));
//                }
            }
            txtAuctnAmtActionPerformed(null);
        }
       
    }//GEN-LAST:event_txtAuctnAmtFocusLost
    private void chkActnActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActnActActionPerformed
        if (chkActnAct.isSelected()) {
            ClientUtil.enableDisable(panAuction, true);
		
            observable.setCbmProdIdCr("SA");
//           cboSaProdId.setModel(observable.getCbmProdIdCr());
          txtAuctnAmt.setSize(100, 21);
          txtBalancAmt.setSize(100, 21);
          txtSaAccountNumber.setEnabled(false);
          txtAuctnAmt.setEnabled(true);
          txtBalancAmt.setEnabled(true);
            txtDrAccHead.setEnabled(false);
//          cboSaProdId.setSize(100,21);
//          cboSaProdId.setPreferredSize(new java.awt.Dimension(100, 21));
//          cboSaProdId.setAutoscrolls(true);
//          cboSaProdId.setMaximumSize(new java.awt.Dimension(100, 21));
//          cboSaProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        } else {
           ClientUtil.enableDisable(panAuction, false);
            txtAuctnAmt.setText("0.0");
            txtBalancAmt.setText("0.0");
            txtSaAccountNumber.setText("");
            txtDrAccHead.setText("");
            txtSaAccountNumber.setEnabled(false);
            txtDrAccHead.setEnabled(false);
            chkActnAct.setEnabled(true);
             txtAuctnAmt.setEnabled(false);
          txtBalancAmt.setEnabled(false);
             //  cboSaProdId.setSize(100,21);
         // cboSaProdId.setPreferredSize(new java.awt.Dimension(100, 21));
        //  cboSaProdId.setAutoscrolls(true);
        //  cboSaProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        //  cboSaProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        }
         transactionUI.removeGoldAuctionProdType(chkActnAct.isSelected());
    }//GEN-LAST:event_chkActnActActionPerformed
    private void txtDrAccHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrAccHeadFocusLost
    }//GEN-LAST:event_txtDrAccHeadFocusLost

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        if (tableFlag) {
            chrgTableEnableDisable();
        }
        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            clearPreviousAccountDetails();
            resetUIForm();
            actNum = observable.checkAcNoWithoutProdType(actNum);
            observable.calculateInterestOnMaturity();
            //       actNum=observable.getTxtAccountNumber();
            txtAccountNumber.setText(actNum);
           // accountViewMap(actNum);
            //        String prodId = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
            //        if(prodId.length()>0 && txtAccountNumber.getText().length()>0){
            ////            resetUIForm();
            //            createChargeTable(prodId);
            //            accClosingCharges();
            //        }else
            //        {
            //            chrgTableEnableDisable();
            //        }
        }
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void btnWaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaiveActionPerformed

                //added by rish for waiving option
        HashMap totalLoanAmount = observable.getTotalLoanAmount();
        int waiveconfirm = -1, penalwaiveconfirm = -1;
        observable.setAvailableBalance(CommonUtil.convertObjToStr(totalLoanAmount.get("CLEAR_BALANCE")));
        returnWaiveMap = new HashMap();        
        if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("ARC_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("ARBITRARY_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("POSTAGE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("LEGAL_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("INSURANCE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("EP_COST_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("DECREE_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("MISCELLANEOUS_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("ADVERTISE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("RECOVERY_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("MEASUREMENT_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y")) {
            waiveconfirm = ClientUtil.confirmationAlert("Do you want to WAIVE");
            if (waiveconfirm == 0) {
                observable.showEditWaiveTableUI(totalLoanAmount);
                returnWaiveMap = observable.waiveOffEditInterestAmt();
                System.out.println("returnWaiveMap$@#$@#$@"+returnWaiveMap);
                double penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && penalWaiveAmt > 0) {
                    observable.setWaiveoffPenal(true);
                    observable.setPenalWaiveAmount(penalWaiveAmt);
                } else {
                    observable.setWaiveoffPenal(false);
                    observable.setPenalWaiveAmount(0);
                }
                double interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") && interestWaiveAmt > 0) {
                    observable.setInterestWaiveoff(true);
                    observable.setInterestWaiveAmount(interestWaiveAmt);
                } else {
                    observable.setInterestWaiveoff(false);
                    observable.setInterestWaiveAmount(0);

                }
                double noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") && noticeWaiveAmt > 0) {
                    observable.setNoticeWaiveoff(true);
                    observable.setNoticeWaiveAmount(noticeWaiveAmt);
                } else {
                    observable.setNoticeWaiveoff(false);
                    observable.setNoticeWaiveAmount(0);
                }
                double principalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PRINCIPAL"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") && principalWaiveAmt > 0) {
                    observable.setPrincipalwaiveoff(true);
                    observable.setPrincipalWaiveAmount(principalWaiveAmt);
                } else {
                    observable.setPrincipalwaiveoff(false);
                    observable.setPrincipalWaiveAmount(0);
                }
                double arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("ARC_WAIVER")).equals("Y") && arcWaiveAmt > 0) {
                    observable.setArcWaiveOff(true);
                    observable.setArcWaiveAmount(arcWaiveAmt);
                } else {
                    observable.setArcWaiveOff(false);
                    observable.setArcWaiveAmount(0);

                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("ARBITRARY_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
                        if (arbitaryWaiveAmt > 0) {
                            observable.setArbitraryWaiveOff(true);
                            observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
                        } else {
                            observable.setArbitraryWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("POSTAGE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
                        if (postageWaiveAmt > 0) {
                            observable.setPostageWaiveOff(true);
                            observable.setPostageWaiveAmount(postageWaiveAmt);
                        } else {
                            observable.setPostageWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("LEGAL_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
                        if (legalWaiveAmt > 0) {
                            observable.setLegalWaiveOff(true);
                            observable.setLegalWaiveAmount(legalWaiveAmt);
                        } else {
                            observable.setLegalWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("INSURANCE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
                        if (insurenceWaiveAmt > 0) {
                            observable.setInsuranceWaiveOff(true);
                            observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
                        } else {
                            observable.setInsuranceWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("EP_COST_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
                        if (epWaiveAmt > 0) {
                            observable.setEpCostWaiveOff(true);
                            observable.setEpCostWaiveAmount(epWaiveAmt);
                        } else {
                            observable.setEpCostWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("DECREE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
                        if (degreeWaiveAmt > 0) {
                            observable.setDecreeWaiveOff(true);
                            observable.setDecreeWaiveAmount(degreeWaiveAmt);
                        } else {
                            observable.setDecreeWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("MISCELLANEOUS_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
                        if (miseWaiveAmt > 0) {
                            observable.setMiscellaneousWaiveOff(true);
                            observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
                        } else {
                            observable.setMiscellaneousWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("ADVERTISE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
                        if (advertiseWaiveAmt > 0) {
                            observable.setAdvertiseWaiveOff(true);
                            observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
                        } else {
                            observable.setAdvertiseWaiveOff(false);
                        }
                    }
                }
                
                 // Added by nithya for 0008470 : overdue interest for EMI Loans
                if (totalLoanAmount != null && totalLoanAmount.containsKey("OVERDUEINT_WAIVER") && totalLoanAmount.get("OVERDUEINT_WAIVER") != null && CommonUtil.convertObjToStr(totalLoanAmount.get("OVERDUEINT_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0 && returnWaiveMap.containsKey("EMI_OVERDUE_CHARGE")) {
                        double overDueIntWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EMI_OVERDUE_CHARGE"));
                        if (overDueIntWaiveAmt > 0) {
                            observable.setOverDueIntWaiveOff(true);
                            observable.setOverDueIntWaiveAmount(overDueIntWaiveAmt);
                        } else {
                            observable.setOverDueIntWaiveOff(false);
                        }
                    }
                }
                // End
                
                
                 if (totalLoanAmount != null && totalLoanAmount.containsKey("RECOVERY_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("RECOVERY_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double recoveryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("RECOVERY CHARGES"));
                        if (recoveryWaiveAmt > 0) {
                            observable.setRecoveryWaiveOff(true);
                            observable.setRecoveryWaiveAmount(recoveryWaiveAmt);
                        } else {
                            observable.setRecoveryWaiveOff(false);
                        }
                    }
                }
                 
               if (totalLoanAmount != null && totalLoanAmount.containsKey("MEASUREMENT_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("MEASUREMENT_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double measurementWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MEASUREMENT CHARGES"));
                        if (measurementWaiveAmt > 0) {
                            observable.setMeasurementWaiveOff(true);
                            observable.setMeasurementWaiveAmount(measurementWaiveAmt);
                        } else {
                            observable.setMeasurementWaiveOff(false);
                        }
                    }
                } 
               
               
                // Kole field expense & operation
                 if (totalLoanAmount != null && totalLoanAmount.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && totalLoanAmount.get("KOLE_FIELD_EXPENSE_WAIVER") != null && CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD EXPENSE"));
                            if (koleFieldExpenseWaiveAmt > 0) {
                                observable.setKoleFieldExpenseWaiveOff(true);
                                observable.setKoleFieldExpenseWaiveAmount(koleFieldExpenseWaiveAmt);
                            } else {
                                observable.setKoleFieldExpenseWaiveOff(false);
                            }
                        }
                    }
                    
                    if (totalLoanAmount != null && totalLoanAmount.containsKey("KOLE_FIELD_OPERATION_WAIVER") && totalLoanAmount.get("KOLE_FIELD_OPERATION_WAIVER") != null && CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldOperationWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD OPERATION"));
                            if (koleFieldOperationWaiveAmt > 0) {
                                observable.setKoleFieldOperationWaiveOff(true);
                                observable.setKoleFieldOperationWaiveAmount(koleFieldOperationWaiveAmt);
                            } else {
                                observable.setKoleFieldOperationWaiveOff(false);
                            }
                        }
                    }
                // End
                
                
            }
        }
        calcAndDisplayAvailableBalance();

        // TODO add your handling code here:
    }//GEN-LAST:event_btnWaiveActionPerformed

    private void txtSaAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSaAccountNumberFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaAccountNumberFocusLost

    private void txtNetWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWeightFocusLost
       
    }//GEN-LAST:event_txtNetWeightFocusLost

    private void btncheqDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncheqDetailsActionPerformed
        // TODO add your handling code here:
        if (txtAccountNumber.getText() != null && !prodType.equals("TermLoan")) {
            LinkedHashMap map = (LinkedHashMap) observable.getUnPaidChequeDetails(txtAccountNumber.getText());
            if (map != null && map.size() > 0) {
                UnUsedChequeDetailsUI surtyTab = new UnUsedChequeDetailsUI("AccountClosing", map, new HashMap());
                surtyTab.show();
                if (surtyTab.getSelCheqNo() != null && surtyTab.getSelCheqNo().size() > 0) {
                    observable.setSbChqueList(surtyTab.getSelCheqNo());
                }
            }
        }
    }//GEN-LAST:event_btncheqDetailsActionPerformed
    private void calcDifAuctamt() {
        if (txtAuctnAmt.getText() != null && txtPayableBalance.getText() != null) {
            double auctamt = CommonUtil.convertObjToDouble(txtAuctnAmt.getText());
            double clsngAmt = CommonUtil.convertObjToDouble(txtPayableBalance.getText());
            if (txtAuctnAmt.getText() != null || (!txtAuctnAmt.getText().equals(""))) {
                if (clsngAmt > auctamt) {
                    txtBalancAmt.setText(String.valueOf(clsngAmt - auctamt));
                    lblCrDr.setText("Dr");
                } else {
                    txtBalancAmt.setText(String.valueOf(auctamt - clsngAmt));
                    lblCrDr.setText("Cr");
                }
                observable.setAuctnAct(true);
                observable.setAuctnAmt(auctamt);
                observable.setAuctnBalAmt(CommonUtil.convertObjToDouble(txtBalancAmt.getText()));
                observable.setAuctnType(lblCrDr.getText());
            }
        }
    }

    private void getGoldLoanLiability() {
        //GoldLoanLiability
          if (!prodType.equals("") && prodType.equals("TermLoan")) {
            HashMap paramMap = new HashMap();
            paramMap.put("MemberNo", txtMemberNo.getText());
            int yesNo1 = 0;
            String[] options = {"Yes", "No"};
          //  yesNo1 = COptionPane.showOptionDialog(null, "Do you want view Gold Loan Liability?", CommonConstants.WARNINGTITLE,
            //        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
             //       null, options, options[0]);
           
            if (yesNo1 == 0) {
              //  com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                String reportName = "";
                paramMap.put("TransDt", currDt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //    ttIntgration.setParam(paramMap);
                //reportName = "GoldLoanLiability";
              //  ttIntgration.integrationForPrint(reportName, true);
            }
            ArrayList liabilityList = new ArrayList();
            List aList = ClientUtil.executeQuery("getLoanLiability", paramMap);
            if (aList != null && aList.size() > 0) {
                for (int i = 0; i < aList.size(); i++) {
                    HashMap hMap = (HashMap) aList.get(i);
                    liabilityList.add(hMap);
                }

                //new LoanLiabilityUI(this, liabilityList).show();
                new LoanLiabilityUI(this, paramMap).show();
            }

        }
    }

    private void createChargeTable(String prodId) {
        HashMap tableMap = buildData(prodId);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(320, 100);
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(320, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(320, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
            table.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                   
                    if (tableFlag == true) {
                     accClosingCharges();
                    }
                }
            });
            table.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (tableFlag == true) {
                        accClosingCharges();
                    }
                }
            });

        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }

    }

    private HashMap buildData(String prodId) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        whereMap.put("DEDUCTION_ACCU", "C");
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
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
            _heading.add("E");
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
            if (!chkAuction()) {
                String desc = CommonUtil.convertObjToStr(colData.get(1));
                HashMap hash = new HashMap();
                hash.put("act_num", observable.getTxtAccountNumber());
                hash.put("charge_Type", desc);
                hash.put("screenName", "Loan Closing");
                List lst = ClientUtil.executeQuery("getSelectTermLoanChargeList", hash);
                if (lst == null || lst.size() <= 0) {
                    data.add(colData);
                }
            }
            else{
              data.add(colData);   
            }
           
        }
        incidentAmt = 0;
        incidentalChrg();
        if (incidentAmt > 0) {
            colData = new ArrayList();
            colData.add(new Boolean(true));
            colData.add("Incidental Charges");
            colData.add(String.valueOf(incidentAmt));
            colData.add("N");
            colData.add("N");
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }
    public boolean chkAuction() {
        boolean authFlag = true;
        HashMap WhereMap = new HashMap();
        WhereMap.put("ACCT_NUM", observable.getTxtAccountNumber());
        List releaseList = ClientUtil.executeQuery("getSecurityReleaseDetails", WhereMap);
        if (releaseList != null && releaseList.size() > 0) {
            HashMap hmap = (HashMap) releaseList.get(0);
            if (hmap != null && hmap.containsKey("IS_RELEASE")) {
                String val = CommonUtil.convertObjToStr(hmap.get("IS_RELEASE"));
                if (val != null && val.equals("Y")) {
                    authFlag = false;
                }
            }
        }
        if (authFlag) {
            WhereMap = new HashMap();
            WhereMap.put("ACCT_NUM", observable.getTxtAccountNumber());
            releaseList = ClientUtil.executeQuery("getSuspeceIsAuction", WhereMap);
            if (releaseList != null && releaseList.size() > 0) {
                HashMap hmap = (HashMap) releaseList.get(0);
                if (hmap != null && hmap.containsKey("ISAUCTION")) {
                    String val = CommonUtil.convertObjToStr(hmap.get("ISAUCTION"));
                    if (val != null && val.equals("Y")) {
                        authFlag = false;
                    }
                }
            }

        }
        return authFlag;
    }
    private void incidentalChrg() {
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
        List list = ClientUtil.executeQuery("getIncidentalCharges", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            String incidentChrg = "";
            double absoluteChrg = 0.0;
            double perChrg = 0.0;
            Date curDt = (Date) currDt.clone();
            incidentChrg = CommonUtil.convertObjToStr(whereMap.get("COMMIT_CHRG"));
            if (incidentChrg.equals("Y")) {
                absoluteChrg = CommonUtil.convertObjToDouble(whereMap.get("COMMIT_CHRG_AMT")).doubleValue();
                if (absoluteChrg > 0) {
                    incidentAmt = absoluteChrg;
                }
                perChrg = CommonUtil.convertObjToDouble(whereMap.get("COMMIT_CHRG_PER")).doubleValue();
                if (perChrg > 0) {
                    //                    whereMap = transDetailsUI.getAsAndWhenMap();
                    //                    accOpenDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("FROM_DATE")));
                    long periodRun = DateUtil.dateDiff(actOpenDt, curDt);
                    long periodMonths = 0;
                    periodMonths = periodRun < 30 ? 1 : ((long) periodRun / 30) + (periodRun % 30 >= 10 ? 1 : 0);
                    //                  long periodMonths = ((long)periodRun/30) + (periodRun%30 >= 10 ? 1 : 0);
                    incidentAmt = (sanctionAmt * (perChrg / 100.0)) * periodMonths + 2;
                    incidentAmt = (long) (incidentAmt + 0.5);
                }
            }
        }

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
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 4)).equals("Y"))) { // (CommonUtil.convertObjToStr(getValueAt(row, headingVector.size() - 1)).equals("Y"))
                return false;
            } else {
                if (col != 0) {
                    if (col == 3 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
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
        //        public void tableChanged(javax.swing.event.TableModelEvent tableModelEvent) {
        //            int row = tableModelEvent.getLastRow();
        //            if(tableFlag == true){
        //                accClosingCharges();
        //            }
        //        }
    }

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        cboProductID.setModel(observable.getCbmProductID());
        cboProductID.setSelectedIndex(0);
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp() {
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
       
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            testMap = accountViewMap();
            clearPreviousAccountDetails();
        } //To display the existing accounts which are set to closed
        else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            testMap = accountEditMap();
        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, testMap).show();

    }

    private void clearPreviousAccountDetails() {
        this.txtAccountNumber.setText("");
        this.lblBalanceDisplay.setText("");
        this.lblCustomerNameDisplay.setText("");
        this.txtAccountClosingCharges.setText("");
        this.txtInterestPayable.setText("");
        observable.setCustomerName("");
        observable.setAvailableBalance("");
        observable.setTxtAccountClosingCharges("");
        observable.setTxtInterestPayable("");
        transDetailsUI.setTransDetails(null, null, null);
    }

    public HashMap btnNull(HashMap transMap) {
        transactionUI.getAccClosingDetails(transMap);
        transactionDetailsTO = new LinkedHashMap();
        if (transMap.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
            observable.setPrematureString(CommonUtil.convertObjToStr(transMap.get("DEPOSIT_PREMATURE_CLOSER")));
        }
        final AccountClosingTO objAccountClosingTO = observable.setAccountClosingLTDData();
        objAccountClosingTO.setCommand(observable.getCommandForLtd());
        objAccountClosingTO.setStatus(observable.getActionForLtd());
        objAccountClosingTO.setStatusBy(TrueTransactMain.USER_ID);
        objAccountClosingTO.setStatusDt(currDt);
        transMap.put("accountclosing", objAccountClosingTO);
        transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", transactionUI.getOutputTO());
        transMap.put("TransactionTO", transactionDetailsTO);
        if (observable.getAsAnWhen() != null && observable.getAsAnWhen().equals("Y")) {
            observable.getTotalLoanAmount().put("CURR_MONTH_INT", observable.getTotalLoanAmount().get("CURR_MONTH_INT"));//INTEREST
            transMap.put("CURR_MONTH_INT", observable.getTotalLoanAmount().get("CURR_MONTH_INT"));//INTEREST
        }
        transMap.putAll(observable.getTotalLoanAmount());
        if (observable.getAsAnWhen() != null && observable.getAsAnWhen().equals("Y")) {
            transMap.put("AS_CUSTOMER_COMES", "Y");
        }
     
        observable.setPrematureString("");
        return transMap;
    }

    public HashMap btnAuthorizeDep(HashMap transMap) {
        viewType = AUTHORIZE;
        transactionDetailsTO = new LinkedHashMap();
        observable.setTxtAccountNumber(CommonUtil.convertObjToStr(transMap.get("ACCOUNTNO")));
        observable.setTotalLoanAmount(transMap);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transMap.put("ACT_NUM", transMap.get("ACCOUNTNO"));
        fillDataEdit(transMap);
        String deposit_authorize_status = CommonUtil.convertObjToStr(transMap.get(CommonConstants.AUTHORIZESTATUS));
        String authorizeStatus = deposit_authorize_status;
        HashMap accountClosingMap = new HashMap();
        accountClosingMap.put("ACT_NUM", transMap.get("ACCOUNTNO"));
        List lst = ClientUtil.executeQuery("getDepositClosingAccounts", accountClosingMap);
        if (lst.size() > 0) {
            accountClosingMap = (HashMap) lst.get(0);
            final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
            objAccountClosingTO.setActNum(observable.getTxtAccountNumber());
            objAccountClosingTO.setUnusedChk(CommonUtil.convertObjToDouble(accountClosingMap.get("UNUSED_CHK")));
            objAccountClosingTO.setActClosingChrg(CommonUtil.convertObjToDouble(accountClosingMap.get("ACT_CLOSING_CHRG")));
            objAccountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(accountClosingMap.get("INT_PAYABLE")));
            objAccountClosingTO.setChrgDetails(CommonUtil.convertObjToDouble(accountClosingMap.get("CHRG_DETAILS")));
            objAccountClosingTO.setPayableBal(CommonUtil.convertObjToDouble(accountClosingMap.get("PAYABLE_BAL")));
            if (authorizeStatus.equals("AUTHORIZED")) {
                objAccountClosingTO.setCommand("AUTHORIZE");
            } else {
                objAccountClosingTO.setCommand("");
            }
            objAccountClosingTO.setStatusBy(TrueTransactMain.USER_ID);
            objAccountClosingTO.setStatusDt(currDt);
            transMap.put("accountclosing", objAccountClosingTO);
        }
        HashMap remitMap = new HashMap();
        TransactionTO transactionTO = new TransactionTO();
        HashMap where = new HashMap();
        String acctNum = CommonUtil.convertObjToStr(transMap.get("ACCOUNTNO"));
        where.put(CommonConstants.MAP_WHERE, acctNum);
        observable.getData(where);
        setCreditIntOD();
        transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", observable.getAllowedTransactionDetailsTO());
        transMap.put("TransactionTO", transactionDetailsTO);
        ArrayList arrList = new ArrayList();
        HashMap authorizeMap = new HashMap();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", authorizeStatus);
        singleAuthorizeMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
        arrList.add(singleAuthorizeMap);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
        authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap serauthMap = new HashMap();
            serauthMap.put("ACCT_NUM", observable.getTxtAccountNumber());
            serauthMap.put("STATUS", authorizeStatus);
            serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
            serauthMap.put("AUTHORIZEDT", currDt);
            authorizeMap.put("SER_TAX_AUTH", serauthMap);
        }
        transMap.put("AUTHORIZEMAP", authorizeMap);
        //        if(deposit_authorize_status.equals(CommonConstants.STATUS_AUTHORIZED))
        //            btnAuthorizeActionPerformed(null);
        //        else
        //            btnRejectActionPerformed(null);
        return transMap;
    }

    public void statusDep() {
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }

    public HashMap transactionActionType(HashMap transMap) {
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        transProdId = CommonUtil.convertObjToStr(transMap.get("PROD_ID"));
        HashMap accountMap = new HashMap();
        accountMap.put("PROD_ID", transProdId);
        List lst = ClientUtil.executeQuery("getAccHeadDesc", accountMap);
        if (lst.size() > 0) {
            accountMap = (HashMap) lst.get(0);
            cboProductID.setSelectedItem(CommonUtil.convertObjToStr(accountMap.get("PROD_DESC")));
            observable.setTransProdId(CommonUtil.convertObjToStr(transMap.get("PROD_ID")));
            observable.setTxtAccountNumber(CommonUtil.convertObjToStr(transMap.get("ACCOUNT NUMBER")));
        }
        return transMap;
    }

    public void doUpdate() {
        initComponentData();
        update1(observable, null);

    }
    private void getSuspenseNumber() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
          
            HashMap transMap = new HashMap();
            HashMap transCashMap = new HashMap();
            String actNum = "";
            String authRemarks = "";
            susTypeMap.clear();
            transCashMap.put("BATCH_ID", observable.getTxtAccountNumber());
            transCashMap.put("TRANS_DT", currDt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            transCashMap.put("INSTRUMENT_NO2", "INSTRUMENT_NO2");
          
            HashMap transIdMap = new HashMap();
            List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
            if (transferList != null && transferList.size() > 0) {
                for (int i = 0; i < transferList.size(); i++) {
                    transMap = (HashMap) transferList.get(i);
                    if (transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE") != null) {
                        if (transMap.get("PROD_TYPE").equals("SA") && (transMap.get("ACT_NUM") != null && transMap.containsKey("ACT_NUM")) 
                                && (transMap.containsKey("AUTHORIZE_REMARKS") && transMap.get("AUTHORIZE_REMARKS") != null)) {
                            if(transMap.get("AUTHORIZE_REMARKS").equals("AUCTION_BAL_AMOUNT")){
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            susTypeMap.put("AUCTION_BAL_AMOUNT","AUCTION_BAL_AMOUNT");
                            susTypeMap.put("ACT_NUM", CommonUtil.convertObjToStr(transMap.get("ACT_NUM")));
                            }
                        }
                    }
                }
            }
            List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
            if (cashList != null && cashList.size() > 0) {
                for (int i = 0; i < cashList.size(); i++) {
                    transMap = (HashMap) cashList.get(i);
                    if (transMap.containsKey("PROD_TYPE") && transMap.get("PROD_TYPE") != null) {
                        if (transMap.get("PROD_TYPE").equals("SA") && (transMap.get("ACT_NUM") != null && transMap.containsKey("ACT_NUM")) 
                                && (transMap.containsKey("AUTHORIZE_REMARKS") && transMap.get("AUTHORIZE_REMARKS") != null)) {
                                                             
                              authRemarks = CommonUtil.convertObjToStr(transMap.get("AUTHORIZE_REMARKS"));
                             if(authRemarks.equals("AUCTION_BAL_AMOUNT")){
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            susTypeMap.put("AUCTION_BAL_AMOUNT","AUCTION_BAL_AMOUNT");
                            susTypeMap.put("ACT_NUM", actNum);
                             }
                        }
                    }
                }
            }
            
            if(susTypeMap != null && susTypeMap.containsKey("ACT_NUM") && susTypeMap.get("ACT_NUM") != null){
                observable.setSusTypeMap(susTypeMap);
            }
        }
        
    }
    
    
     private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object obj) {
       
        try {
            final HashMap hash = (HashMap) obj;
             if(hash.containsKey("PROD_DESC")&& hash.get("PROD_DESC") != null){
                 cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                }
             if (searchOpt == 1) {
                if (hash.containsKey("MEMBER_NO") && hash.get("MEMBER_NO") != null) {
                    memNo = hash.get("MEMBER_NO").toString();
                }
                txtGrossWeight.setText("");
                txtNetWeight.setText("");
                txtMemberNo.setText(memNo);
                observable.setMemNo(memNo);
                searchOpt = 0;
                getGoldLoanLiability();
                if (prodType.equals("TermLoan")) {
                    observable.filterProd();
                }
                // cboProductID.setModel(observable.getCbmProductID());

            } else if (viewType == 25) {
                txtSaAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
//                observable.setSusprodId(CommonUtil.convertObjToStr(((ComboBoxModel) cboSaProdId.getModel()).getKeyForSelected()));
                observable.setSusActnum(txtSaAccountNumber.getText());
            } 
             else if(viewType==66){
                txtDrAccHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                observable.setTxtDrAccHead(txtDrAccHead.getText());
            }
            else {
                 if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                     fromNewAuthorizeUI = true;
                     newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                     hash.remove("PARENT");
                     viewType = AUTHORIZE;
                     observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                     observable.setStatus();
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
                    btnReject.setEnabled(false);
                    rejectFlag = 1;
                }
                if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
                  
                    fromAuthorizeUI = false;
                    fromManagerAuthorizeUI = false;
                    fromCashierAuthorizeUI = false;
                    viewType = AUTHORIZE;
                    observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                    observable.setStatus();
                    //btnSaveDisable();
                }
                if (hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
                    fromSmartCustUI= true;
                    smartUI = (SmartCustomerUI) hash.get("PARENT");
                    hash.remove("PARENT");
                    btnNewActionPerformed(null);
                    //viewType = ACCNO;
                    cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                    txtAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    txtAccountNumberFocusLost(null);
                    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                    observable.setStatus();
                    viewType = -1;
                    //  btnSaveDisable();
                    //fillDataNew(hash);
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || hash.containsKey("FROM_TRANSACTION_SCREEN")) {
                    if (hash.containsKey("FROM_TRANSACTION_SCREEN")) {
                        btnNewActionPerformed(null);
                        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                        cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                        if(hash.containsKey("LOAN_RENEWAL")){
                        transactionUI.setSourceScreen("LOAN_CLOSING_FROM_RENEAL");
                        transactionUI.setCallingTransType("Transfer");
                        transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                        transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("DEBIT_NUMBER")));
                        
                        }
                        if(hash.containsKey("DEPOSIT_LOAN_RENEWAL")){
                        transactionUI.setSourceScreen("DEPOSIT_LOAN_CLOSING_FROM_RENEAL");
                        transactionUI.setCallingTransType("Transfer");
                        transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                        transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("DEBIT_NUMBER")));
                        
                        }
                        
                        //                    setKSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    }
                    if(!hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
                   	fillDataNew(hash);
                    }
//                    if (prodType.equals("TermLoan")) {
//                        String act_num = observable.getTxtAccountNumber();
//                        HashMap lockmap = new HashMap();
//                        lockmap.put("ACCOUNTNO", act_num);
//                        List lockList = ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
//                        lockmap = null;
//                        if (lockList != null && lockList.size() > 0) {
//                            lockmap = (HashMap) lockList.get(0);
//                            String lockStatus = CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
//                            if (lockStatus.equals("Y")) {
//                                ClientUtil.displayAlert("Account is locked");
//                                txtAccountNumber.setText("");
//                            }
//                        }
//                    }
                    //added by rishad 10/07/2015 for avoiding doubling issue at 10/07/2015
//                    if (lockAccount != null && !lockAccount.equals("")) {
//                        HashMap lockMap = new HashMap();
//                        lockMap.put("RECORD_KEY", lockAccount);
//                        lockMap.put("CUR_DATE", currDt.clone());
//                        lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//                        ClientUtil.execute("deleteLock", lockMap);
//                    }
//                    HashMap map = new HashMap();
//                    map.put("SCREEN_ID", getScreenID());
//                    map.put("RECORD_KEY", observable.getTxtAccountNumber());
//                    map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//                    map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//                    map.put("CUR_DATE", currDt.clone());
//                    List lstLock = ClientUtil.executeQuery("selectEditLock", map);
//                    if (lstLock != null && lstLock.size() > 0) {
//                        ClientUtil.displayAlert("Account is locked");
//                        txtAccountNumber.setText("");
//                         btnCancelActionPerformed(null);
//                         return;
//                    } else {
//                        lockAccount=observable.getTxtAccountNumber();
//                        HashMap lockMap = new HashMap();
//                        ArrayList lst = new ArrayList();
//                        lst.add("ACCOUNT NUMBER");
//                        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                        lockMap.put("ACCOUNT NUMBER", observable.getTxtAccountNumber());
//                        setEditLockMap(lockMap);
//                        setEditLock();
//                    }
//                    
                    //end
                    
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    fillDataEdit(hash);
                    setButtonEnableDisable();
                    enableDisable(true);
                    btnAccountNumber.setEnabled(false);
                    observable.setStatus();
                    if (observable.getLinkMap() == null) {
                        checkForEdit();
                    }
                    transactionUI.enableDisableForActClosing(false);
                    cboProductID.setEnabled(false);
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        ClientUtil.enableDisable(panProductID, false);
                    }
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                    hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
                    fillDataEdit(hash);
                    setButton4Authorize();
                    transactionUI.setCallingUiMode(observable.getActionType());
                    isAuthRecord = true;
                    viewType = AUTHORIZE;
                }
                transactionUI.setSourceAccountNumber(txtAccountNumber.getText());
                transactionUI.setCallingApplicantName(lblCustomerNameDisplay.getText());
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                     btnAuthorize.requestDefaultFocus();
                }
                if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")
                        && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    btnSave.setEnabled(false);
                }
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                    getSuspenseNumber();
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    HashMap transTypeMap = new HashMap();
                    HashMap transMap = new HashMap();
                    HashMap transCashMap = new HashMap();
                    String reportName = "";
                    String transferDisplayStr = "";
                    String cashDisplayStr = "";
                    String displayStr = "";
                    String actNum = "";
                    String displayWaive = "";
                    transCashMap.put("BATCH_ID", observable.getTxtAccountNumber());
                    transCashMap.put("TRANS_DT", currDt);
                    transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    //commented by anjuanand for MantisId: 0010357
//                    transCashMap.put("INSTRUMENT_NO2", "INSTRUMENT_NO2");
                    if (prodType.equals("TermLoan")) {
                      transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                      transCashMap.put("SCREEN_NAME", "Loan Closing");
                    }
                    HashMap transIdMap = new HashMap();
                    List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                    if (transferList != null && transferList.size() > 0) {
                        transferDisplayStr += "Transfer Transaction Details...\n";
                        for (int i = 0; i < transferList.size(); i++) {
                            transMap = (HashMap) transferList.get(i);
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
//                            transMap = (HashMap) transferList.get(i);
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
                            //transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                            if(CommonUtil.convertObjToStr(transMap.get("AUTHORIZE_REMARKS")).contains("WAIVE")
                                    && CommonUtil.convertObjToStr(transMap.get("TRANS_TYPE")).equals("CREDIT")){                                
                                         displayWaive += transMap.get("AUTHORIZE_REMARKS")  + " : " + transMap.get("AMOUNT") + "\n";
                            }
                        }
                    }
                    List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
                        if (cashList != null && cashList.size() > 0) {
                            cashDisplayStr += "Cash Transaction Details...\n";
                            for (int i = 0; i < cashList.size(); i++) {
                                transMap = (HashMap) cashList.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                                transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
//                                transMap = (HashMap) cashList.get(i);
                                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
//                                        + "   Batch Id : " + transMap.get("BATCH_ID")
                                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                                if (actNum != null && !actNum.equals("")) {
                                    cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                                } else {
                                    cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                                }
                                //transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                            }
                        }
                    if (!cashDisplayStr.equals("")) {
                        displayStr += cashDisplayStr;
                    }
                    if (!transferDisplayStr.equals("")) {
                        displayStr += transferDisplayStr;
                    }
                    if (!displayStr.equals("")) {
                        ClientUtil.showMessageWindow("" + displayStr);
                    }
                    if (!displayWaive.equals("")) {
                        ClientUtil.showMessageWindow(displayWaive);
                    }
                    int yesNo = 0;
                    String[] voucherOptions = {"Yes", "No"};
                    if ((transferList != null && transferList.size() > 0)||(cashList != null && cashList.size() > 0)) {
                        if(observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT){
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, voucherOptions, voucherOptions[0]);
                        if (yesNo == 0) {
                            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransDt", currDt);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            Object keys[] = transIdMap.keySet().toArray();
                             System.out.println("keys@$@$$@"+keys);
                            for (int i = 0; i < keys.length; i++) {
                                paramMap.put("TransId", keys[i]);
                                ttIntgration.setParam(paramMap);
                                System.out.println("transIdMap$@$@$$@"+(transIdMap.get(keys[i])));
                                if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                    reportName = "ReceiptPayment";
                                } else if (prodType.equals("TermLoan")) {
                                    reportName = "CashReceipt";
                                } else {
                                    reportName = "CashPayment";
                                }
                                ttIntgration.integrationForPrint(reportName, false);
                            }
                        }
                    }
                  }
                }
                if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("LOAN_LIABILITY")) {
                    if (hash.get("ACT_NUM").toString() != null) {
                        txtAccountNumber.setText(hash.get("ACT_NUM").toString()); 
                         HashMap supMap = new HashMap();
                        supMap.put("ACCT_NUM", txtAccountNumber.getText());
                        List lstSupName = ClientUtil.executeQuery("getSelectGoldLoan", supMap);
                        if(lstSupName!=null && lstSupName.size()>0){
                        HashMap supMap1 = new HashMap();
                        supMap1 = (HashMap) lstSupName.get(0);
                        txtGrossWeight.setText(CommonUtil.convertObjToStr(supMap1.get("GROSS_WEIGHT")));
                        txtNetWeight.setText(CommonUtil.convertObjToStr(supMap1.get("NET_WEIGHT")));
                        }
                        txtAccountNumberFocusLost(null);
                        txtSaAccountNumber.setEnabled(false);
                    }
                    txtPayableBalance.setEnabled(false);
                    txtMemberNo.setEnabled(false); //KD-3624
                    btnMemberNo.setEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
            btnAuthorize.requestFocus(true);
       } 
        if (prodType.equals("TermLoan")) {
            btncheqDetails.setVisible(false);
        }
    }

    private void setAsAnCustomerDetails() {
        //AS AN WHEN CUSTOMER
     
        if (prodType.equals("TermLoan")) {
            HashMap asAndWhenMap = new HashMap();
            if (observable.getTxtAccountNumber() != null && observable.getTxtAccountNumber().length() > 0) {
                HashMap mapHash = observable.asAnWhenCustomerComesYesNO(observable.getTxtAccountNumber());
                if (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES") != null && mapHash.get("AS_CUSTOMER_COMES").equals("Y")) {
                    asAndWhenMap = interestCalculationTLAD(observable.getTxtAccountNumber());
                    System.out.println("asAndWhenMap :: "+ asAndWhenMap);
                    if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                        if (mapHash != null && mapHash.size() > 0) {
                            asAndWhenMap.put("INSTALL_TYPE", mapHash.get("INSTALL_TYPE"));
                        }
                        transDetailsUI.setAsAndWhenMap(asAndWhenMap);
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    transactionUI.setSourceScreen("AccountClosingUI");
                    txtInterestPayable.setEnabled(false);
                    txtAccountClosingCharges.setEnabled(false);
                    txtChargeDetails.setEnabled(false);
                }
            }
        }
    }

    private void checkForEdit() {
        // To enable disable controls for EDIT operation
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            enableDisable(true);
            cboProductID.setEnabled(false);
            txtAccountNumber.setEnabled(false);
        }
    }

    private void getAccountClosingChrg() {
        //        double accountClosingCharge = 0.0 ;
        HashMap chargesMap = new HashMap();
        List chargeList = null;
        String prodId = null;
        if (transProdId != null && transProdId.length() > 0) {
            prodId = transProdId;
        } else {
            prodId = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
        }
        HashMap temp = new HashMap();
        temp.put(CommonConstants.PRODUCT_ID, prodId);
        if (prodType.equals("TermLoan")) {

             chargeList = ClientUtil.executeQuery("getLoanAccCloseCharges", temp);//getTermLoanAccountClosingCharge
            if (chargeList.size() > 0) {
                chargesMap = (HashMap) chargeList.get(0);
                accountClosingCharge = CommonUtil.convertObjToDouble(chargesMap.get("AC_CLOSING_CHRG")).doubleValue();
                txtAccountClosingCharges.setText(String.valueOf(accountClosingCharge));
                observable.setTxtAccountClosingCharges(String.valueOf(accountClosingCharge));
                //                       chargesMap.put("CHARGES","TERMLOAN");
                //            //           transDetailsUI.setTermLoanCloseCharge(chargesMap);
            }

        } else {
            chargeList = ClientUtil.executeQuery("getAccountClosingCharge", temp);
            temp = null;
            if (chargeList != null && chargeList.size() > 0) {
                accountClosingCharge = CommonUtil.convertObjToDouble(chargeList.get(0)).doubleValue();
            }
            chargeList = null;
            txtAccountClosingCharges.setText(String.valueOf(accountClosingCharge));
            observable.setTxtAccountClosingCharges(String.valueOf(accountClosingCharge));
        }
    }

    /**
     * To fillData for a new entry
     */
    private void fillDataNew(HashMap hash) {
        if (!hash.isEmpty()) {
            txtAccountNumber.setText((String) hash.get("ACCOUNT NUMBER"));            
            HashMap supMap = new HashMap();
            supMap.put("ACCT_NUM", txtAccountNumber.getText());
             //added by Shany on 8/16/2017 for 7420
            if (!prodType.equals("TermLoan")) {
                List addIntToSbNotifyList = ClientUtil.executeQuery("getIsDepositInterestTransferToAct", supMap);
                String depNo = "";
                HashMap addIntToSbNotifyMap = new HashMap();
                if (addIntToSbNotifyList != null && addIntToSbNotifyList.size() > 0) {
                    for (int i = 0; i < addIntToSbNotifyList.size(); i++) {
                        addIntToSbNotifyMap = (HashMap) addIntToSbNotifyList.get(i);
                        depNo = depNo + addIntToSbNotifyMap.get("DEPOSIT_NO") + "\n";
                    }
                    ClientUtil.displayAlert("SI Exists For This Account!!! for deposit no :" + depNo);
                    return;
                }
            }
                        
                        List lstSupName = ClientUtil.executeQuery("getSelectGoldLoan", supMap);
                        if(lstSupName!=null && lstSupName.size()>0){
                        HashMap supMap1 = new HashMap();
                        supMap1 = (HashMap) lstSupName.get(0);
                        txtGrossWeight.setText(CommonUtil.convertObjToStr(supMap1.get("GROSS_WEIGHT")));
                        txtNetWeight.setText(CommonUtil.convertObjToStr(supMap1.get("NET_WEIGHT")));
                        }
            observable.setTxtAccountNumber(txtAccountNumber.getText());
            if (txtAccountNumber.getText().length() > 0) {
                HashMap where = new HashMap();
                where.put("ACT_NUM", txtAccountNumber.getText());
                List list = ClientUtil.executeQuery("checkForSIDebit", where);
                List lst = ClientUtil.executeQuery("checkForSICredit", where);
                if ((list != null && ((Integer) list.get(0)).intValue() > 0) || (lst != null && ((Integer) lst.get(0)).intValue() > 0)) {
                    ClientUtil.displayAlert("SI Exists For This Account!!! Close The SI First");
                    //return;
                }
                
                // Added by nithya   - KD-3464
                String retireDtAlertRequired = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT"));
                //System.out.println("retireDtAlertRequired :: " + retireDtAlertRequired);
                if (retireDtAlertRequired.equals("Y")) {
                    int retireDtAlertDays = CommonUtil.convertObjToInt(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT_DAYS"));
                    List retireDtList = ClientUtil.executeQuery("getAccountRetirementDate", where);
                    if (retireDtList != null && retireDtList.size() > 0) {
                        HashMap retireDtMap = (HashMap) retireDtList.get(0);
                        if (retireDtMap.containsKey("RETIREMENT_DT") && retireDtMap.get("RETIREMENT_DT") != null) {
                            Date retireDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retireDtMap.get("RETIREMENT_DT")));
                            if (DateUtil.dateDiff(currDt, retireDt) <= retireDtAlertDays) {
                                ClientUtil.showMessageWindow("Retirement Date is : " + DateUtil.getStringDate(retireDt));
                            }
                        }
                    }

                }
                                        // End
                
                
                if (hash.containsKey("DEPOSIT_CLOSING_SCREEN")) {
                    setDepositAmt(CommonUtil.convertObjToDouble(hash.get("DEPOSIT_AMOUNT")).doubleValue());
                } else {
                    setDepositAmt(0.0);
                }
                //Get Account closing charge based on product Id
                getAccountClosingChrg();
                if (CommonUtil.convertObjToDouble(observable.getTxtAccountClosingCharges()).doubleValue() > 0) {
                    callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getTxtAccountClosingCharges()));
                }
                //set interest detail in trans details
                if (!(hash.containsKey("DEPOSIT_PREMATURE_CLOSER") || hash.containsKey("DEPOSIT_CLOSING_SCREEN"))) {
                    setAsAnCustomerDetails();
                }
                int returndCheques = 0;
                returndCheques = observable.getUnPaidCheques(txtAccountNumber.getText());
                txtNoOfUnusedChequeLeafs.setText(String.valueOf(returndCheques));
                if (prodType.equals("TermLoan")) {
                    String od = (String) observable.getCbmProductID().getKeyForSelected();
                    getSanctionAmount();
                    txtInsuranceCharges.setText(String.valueOf(getInsuranceChareges(txtAccountNumber.getText())));
                    observable.setTxtInsuranceCharges(txtInsuranceCharges.getText());
                    createChargeTable(CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));
                 
                    if (observable.getLoanBehaves().equals("OD")) {
                        transDetailsUI.setIsDebitSelect(true);
                        transDetailsUI.setTransDetails("AD", ProxyParameters.BRANCH_ID, txtAccountNumber.getText());
                        transDetailsUI.setSourceFrame(this);
                       
                    } else {
                        transDetailsUI.setIsDebitSelect(true);
                        transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, txtAccountNumber.getText());
                        transDetailsUI.setSourceFrame(this);
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
                            if (!chkAuthorizationPending()) {
                                txtAccountNumber.setText("");
                                return;
                            }
                        }
                    }
                    if (tableFlag == true) {
                        //                        getSanctionAmount();
                        chargeAmount();
                        accClosingCharges();                        
                        calculateTot();
                    }
                } else {
                    transDetailsUI.setTransDetails("OA", ProxyParameters.BRANCH_ID, txtAccountNumber.getText());
                    transDetailsUI.setSourceFrame(this);
                    observable.setLienAmount(CommonUtil.convertObjToDouble(transDetailsUI.getLienAmount()).doubleValue());
                    observable.setFreezeAmount(CommonUtil.convertObjToDouble(transDetailsUI.getFreezeAmount()).doubleValue());
                }
            }
            if (hash.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                observable.setDeposit_premature("DEPOSIT_PREMATURE_CLOSER");
                observable.setDeposit_pre_int(CommonUtil.convertObjToDouble(hash.get("RATE_OF_INT")).doubleValue());
            }
            if (hash.containsKey("NORMAL_CLOSER")) {
                observable.setNormalCloser("NORMAL_CLOSER");
            }
            showCustomerName();
            // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
                      showGoldLoanParicularesDetails();
            }
            if (!prodType.equals("TermLoan")) {
                HashMap todCheckmap = new HashMap();
                todCheckmap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
                List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
                if (todList != null && todList.size() > 0) {
                    HashMap todMap = (HashMap) todList.get(0);
                    if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                        if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                            HashMap odWhrMap = new HashMap();
                            odWhrMap.put("ACT_NUM", (String) hash.get("ACCOUNT NUMBER"));
                            List oldODLimitDetailsLst = ClientUtil.executeQuery("getLatestSBODLimitForAccNum", odWhrMap);
                            if (oldODLimitDetailsLst != null && oldODLimitDetailsLst.size() > 0) {
                                HashMap oldODLimitDetailsMap = (HashMap) oldODLimitDetailsLst.get(0);
                                double clearBal = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("CLEAR_BALANCE"));
                                if (clearBal < 0) {
                                    ClientUtil.showMessageWindow("The account cannot be closed. Please pay the OD balance");
                                    btnSave.setEnabled(false);
                                    return;
                                }else{
                                   double availableBal = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("AVAILABLE_BALANCE"));
                                   if(availableBal < (CommonUtil.convertObjToDouble(observable.getTxtInterestReceivable())).doubleValue()){
                                        ClientUtil.showMessageWindow("Account not have enough balance");
                                        return;
                                   }else{
                                       HashMap SBODIntParamMap = new HashMap();
                                       String prodType = "OA";
                                       double sbODDebitInt = 0;
                                       double sbODCreditInt = 0;
                                       String prodId = CommonUtil.convertObjToStr(todCheckmap.get("PROD_ID"));
                                       Date dt = observable.setFromDate(prodType, prodId);
                                       SBODIntParamMap.put("ACT_NUM", (String) hash.get("ACCOUNT NUMBER"));
                                       SBODIntParamMap.put("START_DATE", dt);
                                       SBODIntParamMap.put("END_DATE", currDt);
                                       List sbOdIntList = ClientUtil.executeQuery("getSBODDailyInterest", SBODIntParamMap);
                                       if (sbOdIntList != null && sbOdIntList.size() > 0) {
                                           HashMap sbOdIntMap = (HashMap) sbOdIntList.get(0);
                                           if (sbOdIntMap.containsKey("CREDITINT")) {
                                               sbODCreditInt = CommonUtil.convertObjToDouble(sbOdIntMap.get("CREDITINT"));
                                           }
                                           if (sbOdIntMap.containsKey("DEBITINT")) {
                                               sbODDebitInt = CommonUtil.convertObjToDouble(sbOdIntMap.get("DEBITINT"));
                                           }
                                       }                                       
                                       ClientUtil.showMessageWindow("account closing possible\nCredit Interest : " + sbODCreditInt +"\nDebit interest :"+sbODDebitInt);
                                       return;
                                   }
                                }
                            }
                        }
                    }
                }
            }
            setCreditIntOD();
            observable.setDeposit_premature("");
            observable.setDeposit_pre_int(0);
            observable.setNormalCloser("");
            if (observable.getTransProdId() != null && observable.getTransProdId().length() > 0) {
                setChargeAmt(observable.getTxtPayableBalance());
                setBalanceAmt(observable.getAvailableBalance());
                if (hash.containsKey("DEPOSIT_PREMATURE_CLOSER") && observable.getTotalLoanAmount() != null) {
                    setCalculateInt(CommonUtil.convertObjToDouble(observable.getTotalLoanAmount().get("CALCULATE_INT")).doubleValue());
                }
                if (observable.getAsAnWhen() != null && observable.getAsAnWhen().equals("Y")) {
                    setCalculateInt(CommonUtil.convertObjToDouble(observable.getTotalLoanAmount().get("INTEREST")).doubleValue());
                }
              
            }
        } else {
            this.txtAccountNumber.setText("");
            this.txtNoOfUnusedChequeLeafs.setText("0");
            this.lblBalance.setText("");
            //            this.lblCustomerName.setText("");
            this.txtAccountClosingCharges.setText("");
            transDetailsUI.setTransDetails(null, null, null);
            transDetailsUI.setSourceFrame(null);
        }
        HashMap where = new HashMap();
        String status = "";
        where.put("PRODUCT_ID", (String) observable.getCbmProductID().getKeyForSelected());
        if (!prodType.equals("TermLoan")) {
            status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", where).get(0)).getSRemarks();
        }
        if (status.equals("NRO")) {
            double taxAmt = observable.taxAmount(Double.parseDouble(txtInterestPayable.getText()));
            double bal = (CommonUtil.convertObjToDouble(observable.getAvailableBalance()).doubleValue()
                    + CommonUtil.convertObjToDouble(observable.getTxtInterestPayable()).doubleValue()) - taxAmt;
            observable.setTxtPayableBalance(String.valueOf(bal));
            txtPayableBalance.setText(CommonUtil.convertObjToStr(observable.getTxtPayableBalance()));
            transactionUI.setCallingAmount(observable.getTxtPayableBalance());
            ClientUtil.displayAlert("Availabe balance = Rs." + " " + observable.getAvailableBalance() + "\nInterest Amount = Rs." + " "
                    + observable.getTxtInterestPayable() + "\nTax Deduction = Rs." + " " + taxAmt
                    + "\nNet Payable Amount = Rs." + " " + bal);
        }
        if (!prodType.equals("TermLoan")) {
            HashMap SponsorBankMap = new HashMap();
            SponsorBankMap.put("ACCT_NUM", CommonUtil.convertObjToStr(txtAccountNumber.getText()));
            List lst = ClientUtil.executeQuery("getSelectCardAcctNumStatus", SponsorBankMap);
            if (lst != null && lst.size() > 0) {
                SponsorBankMap = (HashMap)lst.get(0);
                String cardStatusAction = CommonUtil.convertObjToStr(SponsorBankMap.get("ACTION"));
                Date requestedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(SponsorBankMap.get("REQUESTED_TIME")));
                Date systemDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(SponsorBankMap.get("SYSTEM_DATE")));
//                 if(cardStatusAction != null && cardStatusAction.length()>0 &&cardStatusAction.equals("STOP") && DateUtil.dateDiff(requestedDate, systemDate)<1){
//                    ClientUtil.showAlertWindow("Account can be closed only after 1 day, After marking for closure. Closed on " + SponsorBankMap.get("REQUESTED_TIME"));
//                    txtAccountNumber.setText("");
//                    return;
//                }else {
//                    ClientUtil.showAlertWindow("Selected account is not marked STOP action, Please mark through Account Opening screen");
//                    txtAccountNumber.setText("");
//                    return;
//                }
                
                 // Added by nithya on 11-01-2020 for KD-2599
                 if (!cardStatusAction.equals("STOP")) {
                    ClientUtil.showAlertWindow("Selected account is not marked STOP action, Please mark through Account Opening screen");
                    txtAccountNumber.setText("");
                    return;
                } else if (cardStatusAction.equals("STOP") && DateUtil.dateDiff(requestedDate, systemDate) < 1) {
                    ClientUtil.showAlertWindow("Account can be closed only after 1 day, After marking for closure. Closed on " + SponsorBankMap.get("REQUESTED_TIME"));
                    txtAccountNumber.setText("");
                    return;
                }
                 // End
                 
            }
        }
        //Added by nithya on  10-02-2021  for KD-2614
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT && observable.getLoanBehaves().equals("OD") && observable.getTotalLoanAmount() != null && observable.getAsAnWhen().equals("N")) {
           //System.out.println("Testing totalLoanAmount :: " + observable.getTotalLoanAmount() + "AS CUSTOMER COMES :: " + observable.getAsAnWhen());
           if(observable.getTotalLoanAmount().containsKey("CLEAR_BALANCE") && observable.getTotalLoanAmount().get("CLEAR_BALANCE") != null && CommonUtil.convertObjToDouble(observable.getTotalLoanAmount().get("CLEAR_BALANCE")) > 0){
               if(CommonUtil.convertObjToDouble(observable.getTotalLoanAmount().get("INTEREST")) > 0){
                 ClientUtil.showAlertWindow(" Account is in credit balance. Can be closed after posting interest !!");
                 btnCancelActionPerformed(null);
               }
           }
        }        
             
        // End
        observable.ttNotifyObservers();
    }

    private double getInsuranceChareges(String actNum) {
        HashMap purityMap = new HashMap();
        HashMap accountMap = new HashMap();
        HashMap monthMap = new HashMap();
        double months = 0;
        double netgram = 0;
        double insuraceRate = 0;
        int noofDays = 0;
        Date toDate = null;
        Date fromDate = null;
        String purity = "";
        double perGramAmt = 0;
        double insuranceAmt = 0;
        String prod_id = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
        purityMap.put("PROD_ID", prod_id);
        List lst = ClientUtil.executeQuery("getProdIntDetails", purityMap);
        if (lst != null && lst.size() > 0) {
            purityMap = (HashMap) lst.get(0);
            String applicableSanctionDt = CommonUtil.convertObjToStr(purityMap.get("INSURANCE_SANCTION"));
            if (CommonUtil.convertObjToStr(purityMap.get("INSURANCE_APPLICABLE")).equals("Y") && applicableSanctionDt.length() > 0) {


                purityMap.put("ACCT_NUM", actNum);
                insuraceRate = CommonUtil.convertObjToDouble(purityMap.get("INSURANCE_RATE")).doubleValue();
                lst = ClientUtil.executeQuery("getSanctionDetailsForClosing", purityMap);
                if (lst != null && lst.size() > 0) {
                    accountMap = (HashMap) lst.get(0);
                    fromDate = (Date) accountMap.get("FROM_DT");
                    toDate = (Date) accountMap.get("TO_DT");
                    monthMap.put("FROM_DATE", fromDate);

                    purity = CommonUtil.convertObjToStr(accountMap.get("PURITY"));
                    netgram = CommonUtil.convertObjToDouble(accountMap.get("NET_WEIGHT")).doubleValue();
                }
                if ((toDate != null) && DateUtil.dateDiff(toDate, currDt) >= 0) {
                    monthMap.put("TO_DATE", currDt);
                } else {
                    monthMap.put("TO_DATE", toDate);
                }
                purityMap.put("PURITY", purity);
                if (applicableSanctionDt.equals("Y")) {
                    purityMap.put("TODAY_DATE", fromDate);
                }
                if (applicableSanctionDt.equals("N")) {
                    purityMap.put("TODAY_DATE", currDt);
                }
                lst = ClientUtil.executeQuery("getSelectTodaysMarketRate", purityMap);
                if (lst != null && lst.size() > 0) {
                    purityMap = (HashMap) lst.get(0);
                    perGramAmt = CommonUtil.convertObjToDouble(purityMap.get("PER_GRAM_RATE")).doubleValue();
                    lst = ClientUtil.executeQuery("getNoOfMonths", monthMap);
                    if (lst != null && lst.size() > 0) {
                        Object obj = (Object) lst.get(0);
                        months = CommonUtil.convertObjToDouble(obj).doubleValue();
                    }
                    insuranceAmt = (perGramAmt * netgram) * (months / 12) * (insuraceRate / 100);
                }
            }
        }
        return insuranceAmt;
    }

    private HashMap interestCalculationTLAD(String accountNo) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            map.put("ACT_NUM", accountNo);
            String prod_id = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            map.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN");
            List lst = ClientUtil.executeQuery("IntCalculationDetail", map);
            if (lst == null || lst.isEmpty()) {
                lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (observable.getLoanBehaves().equals("OD")) {   // Added by nithya on 06-11-2019 for KD-681	penal intrest calculation neeeded for od closing in loan closing screen               
                    hash.put("AS_CUSTOMER_COMES", "Y");
                }
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                //                if(hashdeposit!=null)
                //                    map.putAll(hashdeposit);//from deposit closingscreen
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
                map.put("LOAN_EMI_CLOSE", "LOAN_EMI_CLOSE");
                map.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
                map.put("SOURCE_SCREEN", "LOAN_CLOSING");
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                //                    observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
                actOpenDt = (Date) map.get("ACCT_OPEN_DT");
                System.out.println("mapppppppppp$@#$@#"+map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    private void showGoldLoanParicularesDetails() {
      
        HashMap selectMap = new HashMap();
        if (prodType.equals("TermLoan")) {
            boolean goldLoanExsist =false;
            selectMap.put("ACCT_NUM", observable.getTxtAccountNumber());
            selectMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
            StringBuffer buffer = new StringBuffer();
            List lst = ClientUtil.executeQuery("getSelectGoldLoan", selectMap);
            if (lst != null && lst.size() > 0) {
                List isExsistList = ClientUtil.executeQuery("getKccSacntionTodate", selectMap);
                if(isExsistList != null && isExsistList.size() > 0){
                   HashMap  goldLoanExsistMap = (HashMap)isExsistList.get(0);
                   if(goldLoanExsistMap.containsKey("TO_DT") && goldLoanExsistMap.get("TO_DT") != null){
                       Date to_Date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(goldLoanExsistMap.get("TO_DT")));                      
                       long datDiff = DateUtil.dateDiff(to_Date, currDt);
                       long datDiff1 = DateUtil.dateDiff(currDt, to_Date);
//                       if(datDiff > 0){
//                           goldLoanExsist = true;
//                       }else{
//                           goldLoanExsist = false;
//                       }  commented by shihad
                   }
                }
                

                goldLoanExsist = true;    
                selectMap = (HashMap) lst.get(0);
                netWt = CommonUtil.convertObjToDouble(selectMap.get("NET_WEIGHT"));
                purity = CommonUtil.convertObjToStr(selectMap.get("PURITY"));
                asOn = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(selectMap.get("AS_ON")));
                if (CommonUtil.convertObjToStr(selectMap.get("GROSS_WEIGHT")).length() > 0) {
                    buffer.append("GROSS_WEIGHT  ");
                    buffer.append(CommonUtil.convertObjToStr(selectMap.get("GROSS_WEIGHT")));
                    buffer.append("\n");

                }
                if (CommonUtil.convertObjToStr(selectMap.get("NET_WEIGHT")).length() > 0) {
                    buffer.append("NET_WEIGHT  ");
                    buffer.append(CommonUtil.convertObjToStr(selectMap.get("NET_WEIGHT")));
                    buffer.append("\n");

                }
                if (CommonUtil.convertObjToStr(selectMap.get("PARTICULARS")).length() > 0) {
                    buffer.append("PARTICULARS  ");
                    buffer.append(CommonUtil.convertObjToStr(selectMap.get("PARTICULARS")));
                    buffer.append("\n");
                }
                
            }else{
              goldLoanExsist = false;  
            }
                if(goldLoanExsist){                    
                new TextAreaUI(this, "Gold Loan particulares Details", buffer.toString(),observable.getPhotoByteArray());    // Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
                panAuction.setVisible(true);
                    if (chkActnAct.isSelected()) {
                        ClientUtil.enableDisable(panAuction, true);
                    }
                txtDrAccHead.setVisible(false);
                lblDrAccHead.setVisible(false);
                txtSaAccountNumber.setEnabled(false);
                }else{
                panAuction.setVisible(false);
                ClientUtil.enableDisable(panAuction, false);
                txtDrAccHead.setVisible(false);
                lblDrAccHead.setVisible(false);    
                }



        }
    }

    /**
     * To fillData for existing entry
     */
    private void fillDataEdit(HashMap hash) {
        AccountClosingRB resourceBundle = new AccountClosingRB();
        String acctNum = null;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            acctNum = hash.get("ACT_NUM").toString();
        } else {
            acctNum = hash.get("ACCOUNT NUMBER").toString();
        }
        txtAccountNumber.setText(acctNum);
         				HashMap supMap = new HashMap();
                        supMap.put("ACCT_NUM", txtAccountNumber.getText());
                        List lstSupName = ClientUtil.executeQuery("getSelectGoldLoan",supMap);
                        if(lstSupName!=null && lstSupName.size()>0){
                        HashMap supMap1 = new HashMap();
                        supMap1 = (HashMap) lstSupName.get(0);
                        txtGrossWeight.setText(CommonUtil.convertObjToStr(supMap1.get("GROSS_WEIGHT")));
                        txtNetWeight.setText(CommonUtil.convertObjToStr(supMap1.get("NET_WEIGHT")));
                        }
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        setAsAnCustomerDetails();
        if (prodType.equals("TermLoan")) {
            //            LinkedHashMap whereMap = new LinkedHashMap();
            //            whereMap.put("PROD_ID",hash.get("PRODUCT ID"));
            //            List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
            //            if(prodDesclst!=null && prodDesclst.size()>0){
            //                whereMap = (LinkedHashMap)prodDesclst.get(0);
            //                prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
            //            }
            //            panChargeDetails.setVisible(true);
            transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acctNum);

        } else {
         transDetailsUI.setTransDetails("OA", ProxyParameters.BRANCH_ID, acctNum);
        }
        transDetailsUI.setSourceFrame(this);
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, acctNum);
        //         showCustomerName();
        observable.setTotalLoanAmount(transDetailsUI.getTermLoanCloseCharge());
        observable.getData(where);
        setCreditIntOD();
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
            showGoldLoanParicularesDetails();
        }
        setModified(true);
        observable.ttNotifyObservers();
        if (prodType.equals("TermLoan")) {
            getSanctionAmount();
            editChargeTable();
            if (tableFlag) {
                prodDesc = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
                editChargeAmount();
            }
        }
        if (CommonUtil.convertObjToDouble(observable.getTxtAccountClosingCharges()).doubleValue() > 0) {
            callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getTxtAccountClosingCharges()));
        }
        calcAndDisplayAvailableBalance();
        updateOBFields();

        observable.ttNotifyObservers();
        txtNoOfUnusedChequeLeafs.setEnabled(false);
        txtInterestPayable.setEnabled(false);
        //        observable.showTransactionDetails();//transaction show for as an when customer
        if (prodType.equals("TermLoan")) {
            observable.showTransaction();

        }
		btnAuthorize.requestFocus();
		grabFocus();
        if (prodType.equals("TermLoan")) {
            lblPayableCharges.setText(resourceBundle.getString("lblReceivableCharges"));
           if (observable.getAdvancesCreditInterest() > 0) {
                lblPayableCharges.setText(resourceBundle.getString("lblPayableCharges"));
            }
        } else {
            lblPayableCharges.setText(resourceBundle.getString("lblPayableCharges"));

        }
        // Added by nithya on 05-10-2019 for KD 465 : Gold loan closing issue 
        // System.out.println("observable.getLoanBehaves() :: "+ observable.getLoanBehaves());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && prodType.equals("TermLoan")) {
            if (!CommonUtil.convertObjToStr(observable.getLoanBehaves()).equals("LOANS_AGAINST_DEPOSITS") && !CommonUtil.convertObjToStr(observable.getLoanBehaves()).equals("OD")) {
                //getGoldLoanClosingAuctionAmount
                double auctionAmount = 0.0;
                HashMap whereMap = new HashMap();
                whereMap.put("CURR_DT",currDt.clone());
                whereMap.put("ACCT_NUM", txtAccountNumber.getText());
                List auctionList = ClientUtil.executeQuery("getGoldLoanClosingAuctionAmount",whereMap);
                if(auctionList != null && auctionList.size() > 0){
                    whereMap = (HashMap)auctionList.get(0);
                    if(whereMap.containsKey("AUCTION_AMOUNT") && whereMap.get("AUCTION_AMOUNT") != null){
                        auctionAmount = CommonUtil.convertObjToDouble(whereMap.get("AUCTION_AMOUNT"));
                    }
                }
                double loanBalance = -1 * CommonUtil.convertObjToDouble(transDetailsUI.getCBalance());
                double finalBal = CommonUtil.convertObjToDouble(lblBalanceDisplay.getText()) + observable.getRebateInterest() + auctionAmount; // Added by nithya on 21-04-2020 for KD-1665 //Auction amount added by nithya on 13-10-2021
                //System.out.println("principle due :: " + loanBalance);
                //System.out.println("lblBalanceDisplay :: balance :: " + finalBal);
                //System.out.println("rebate ::  interest:: " + observable.getRebateInterest());
                if(finalBal != loanBalance){
                     ClientUtil.showAlertWindow("Balance difference found ! Please check");
                     btnCancelActionPerformed(null);
                     btnAuthorize.setEnabled(false);
                     btnCancel.setEnabled(true);
                }
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    HashMap goldMap = new HashMap();
                    goldMap.put("PROD_ID", (String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
                    goldMap.put("ACCT_NUM", txtAccountNumber.getText());
                    goldMap.put("CURR_DT",currDt.clone());
                    List lst = ClientUtil.executeQuery("getLoanBehaves", goldMap);
                    if (lst.size() > 0) {
                        hash = (HashMap) lst.get(0);
                        if (CommonUtil.convertObjToStr(hash.get("AUTHORIZE_REMARK")).equals("GOLD_LOAN")) {
                            System.out.println("Execute hereeeee for gold loan authorize");
                            List openChrgLst = ClientUtil.executeQuery("getOpeningChargeCreditCnt", goldMap);
                            if (openChrgLst != null && openChrgLst.size() > 0) {
                                goldMap = (HashMap) openChrgLst.get(0);
                                int cnt = CommonUtil.convertObjToInt(goldMap.get("CR_CNT"));
                                if (cnt > 0) {
                                    ClientUtil.showAlertWindow("Please authorize gold loan opening first !!!");
                                    btnCancelActionPerformed(null);
                                    btnAuthorize.setEnabled(false);
                                    btnCancel.setEnabled(true);
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    private void setCreditIntOD() {
        // Need to be changed based on the balance (receivable or payable)//babuv
        // if(observable.getAdvancesCreditInterest()>0){
        if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Dr")) {
            transactionUI.setAdvanceCreditIntAmt(new Double(observable.getAdvancesCreditInterest()));
            transactionUI.setTransactionMode(CommonConstants.CREDIT);
        } else {
            transactionUI.setAdvanceCreditIntAmt(new Double(0));
            transactionUI.setTransactionMode(CommonConstants.DEBIT);
        }
    }

    private void editChargeTable() {
        HashMap tableMap = editBuildData((String) cboProductID.getSelectedItem());
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(320, 100);
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(320, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(320, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
            panChargeDetails.setEnabled(false);
            srpChargeDetails.setEnabled(false);
            table.remove(0);
            table.setEnabled(false);
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }

    }

    private void editChargeAmount() {
        HashMap hash = new HashMap();
        hash.put("SCHEME_ID", prodDesc);
        hash.put("DEDUCTION_ACCU", "C");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", hash);
        HashMap chargeMap = new HashMap();
         System.out.println("actOpenDt$@$!$!@$in edit"+actOpenDt);
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                String desc = "";
                chargeMap = (HashMap) chargelst.get(i);
                //                accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                double loanRunPeriod = 0;   
                if(actOpenDt!=null)
                    loanRunPeriod = DateUtil.dateDiff(actOpenDt, (Date)currDt.clone());  
                HashMap selectMap = new HashMap();
                selectMap.put("ACCT_NUM", observable.getTxtAccountNumber());
                selectMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
                StringBuffer buffer = new StringBuffer();
                List isExsistList = ClientUtil.executeQuery("getKccSacntionTodate", selectMap);
                if(isExsistList != null && isExsistList.size() > 0){
                   HashMap  goldLoanExsistMap = (HashMap)isExsistList.get(0);
                   if(goldLoanExsistMap.containsKey("TO_DT") && goldLoanExsistMap.get("TO_DT") != null){
                       actToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(goldLoanExsistMap.get("TO_DT")));                               
                   }else{
                        actToDate = null;
                    }
                }else{
                    actToDate = null;
                }
                double totalLoanPeriod = 0;
                if(actToDate!=null && actOpenDt!=null)
                 totalLoanPeriod = DateUtil.dateDiff(actOpenDt, actToDate);
                 for (int j = 0; j < table.getRowCount(); j++) {
                      //   System.out.println("chargeAmt   eddddddd--->"+table.getValueAt(i, 1) +" 555==="+table.getValueAt(i, 4) );
                  if (CommonUtil.convertObjToStr(table.getValueAt(j, 0)).equals(desc)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = sanctionAmt * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            //Added by sreekrishnan 
                            if(!CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("Y")) {                                 
                                chargeAmt = Math.round((sanctionAmt*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);
                            }
                            //Added by sreekrishnan 
                            if (!CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("PREMATURE_RATE"))!=null
                                    && CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE"))>0) { 
                                double firstAmount = Math.round((sanctionAmt*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);
                                double padiEmi = 0;
                                HashMap  emiMap  = new HashMap();
                                List paidList = ClientUtil.executeQuery("getTotalEmiPaid", selectMap);
                                if(paidList != null && paidList.size() > 0){
                                   emiMap = (HashMap)paidList.get(0);
                                   if(emiMap.containsKey("TOTAL_EMI_PAID")){
                                       padiEmi = CommonUtil.convertObjToDouble(emiMap.get("TOTAL_EMI_PAID"));                               
                                   }
                                }
                                chargeAmt = Math.round(((firstAmount+sanctionAmt)-padiEmi)*CommonUtil.convertObjToDouble(chargeMap.get("PREMATURE_RATE"))/100);
                            }
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 2);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            HashMap slabMap = new HashMap();
                            double sancAmt = sanctionAmt;
                            slabMap.put("CHARGE_ID", chargeMap.get("CHARGE_ID"));
                            slabMap.put("AMOUNT", sancAmt);
                            List slablst = ClientUtil.executeQuery("getSlabAmount", slabMap);
                            if (slablst != null && slablst.size() > 0) {
                                slabMap = (HashMap) slablst.get(0);
                                chargeAmt = sanctionAmt
                                        * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(slabMap.get("CHARGE_RATE"))).doubleValue() / 100;
                                long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(slabMap.get("ROUND_OFF_TYPE")));
                                if (roundOffType != 0) {
                                    chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                                }
                                double minAmt = CommonUtil.convertObjToDouble(slabMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                                double maxAmt = CommonUtil.convertObjToDouble(slabMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                                
                                if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                                }
                                if (chargeAmt < minAmt) {
                                    chargeAmt = minAmt;
                                }
                                if (chargeAmt > maxAmt) {
                                    chargeAmt = maxAmt;
                                }
                                table.setValueAt(String.valueOf(chargeAmt), j, 2);
                            }
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                        } 
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                }
            }
           
            table.revalidate();
            table.updateUI();
        }

    }

    private HashMap editBuildData(String prodDesc) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "C");
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
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
            
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        } else {
            _heading = new ArrayList();
            _heading.add("DESC");
            _heading.add("AMOUNT");
            _heading.add("M");
            _heading.add("E");
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                colData.add(CommonUtil.convertObjToStr(obj));
            }
            data.add(colData);
        }
        incidentalChrg();
        if (incidentAmt > 0) {
            colData = new ArrayList();
            colData.add("Incidental Charges");
            colData.add(String.valueOf(incidentAmt));
            colData.add("N");
            colData.add("N");
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }

    /**
     * To get popUp data for a new entry
     */
    private HashMap accountViewMap() {
        final HashMap testMap = new HashMap();
        //        prodType="TermLoan";
        final HashMap whereMap = new HashMap();
        if (prodType.equals("TermLoan")) {
            testMap.put("MAPNAME", "getSelectTermLoanList");
             whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch); 
        } else {
            testMap.put("MAPNAME", "getSelectAccountList");
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        log.info("productID : " + observable.getCboProductID());
        whereMap.put("PRODID", observable.getCboProductID());
        if (memNo != null && memNo.length() > 0) {
            whereMap.put("MEM_NO", memNo);
        }
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }

    /**
     * To get popUp data for a new entry
     */
    private HashMap accountViewMap(String actNum) {
        HashMap testMap = new HashMap();
        String checkval = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
            String prodId = "";
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            checkval = CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId;
        }
        //        prodType="TermLoan";
        if (actNum != null && actNum.length() > 0) {
            if (!txtAccountNumber.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                String mapName = null;
               
                if (prodType.equals("TermLoan")) {
                    mapName = "getSelectTermLoanList";
                } else {
                    HashMap SponsorBankMap = new HashMap();
                    SponsorBankMap.put("ACT_NUM",CommonUtil.convertObjToStr(txtAccountNumber.getText()));
                    List lst = ClientUtil.executeQuery("getSponsorBankStatus", SponsorBankMap);
                    if(lst != null && lst.size()>0){
                        SponsorBankMap = (HashMap)lst.get(0);
                        ClientUtil.showAlertWindow("Account can be closed only after 1 day, After marking for closure. Closed on "+SponsorBankMap.get("REQUESTED_TIME"));
                        txtAccountNumber.setText("");
                        return null;
                    }
                    mapName = "getSelectAccountList";
                }
                final HashMap whereMap = new HashMap();
                log.info("productID : " + observable.getCboProductID());
                whereMap.put("PRODID", observable.getCboProductID());
                whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                whereMap.put(CommonConstants.ACT_NUM, actNum);
                List lst = ClientUtil.executeQuery(mapName, whereMap);
                if (lst != null && lst.size() > 0) {
                    testMap = (HashMap) lst.get(0);
                    fillData(testMap);
                } else {
                    if (!actNum.equals(checkval)) {
                        ClientUtil.showMessageWindow("Invalid Account No");
                        txtAccountNumber.setText("");
                        txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                    }
           

                }
            }
             if (prodType.equals("TermLoan")) {
                btncheqDetails.setVisible(false);
            }else{
                 btncheqDetails.setVisible(true); 
             }
        }
        return testMap;
    }

    /**
     * To get popUp data for already existing entries for modification
     */
    private HashMap accountEditMap() {
        final HashMap testMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("ACCOUNT NUMBER");
        testMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lst = null;
        if (prodType.equals("TermLoan")) {
            testMap.put("MAPNAME", "getSelectClosingAccountListTL");
        } else {
            testMap.put("MAPNAME", "getSelectClosingAccountList");
        }
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }

    /**
     * To display customer related details based on account number
     */
    private void showCustomerName() {
        updateOBFields();
        observable.setTotalLoanAmount(transDetailsUI.getTermLoanCloseCharge());
        observable.getAccountClosingCharges();
        calcAndDisplayAvailableBalance();
        ///babu
        if (prodType.equals("TermLoan")) {
            AccountClosingRB resourceBundle = new AccountClosingRB();
            lblPayableCharges.setText(resourceBundle.getString("lblReceivableCharges"));
               if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Cr")) {
                lblPayableCharges.setText(resourceBundle.getString("lblPayableCharges"));
            }
        }
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
        txtAccountNumber.setEnabled(false);
        txtAccountNumber.setEditable(false);
        panAuction.setVisible(false);
        ClientUtil.enableDisable(panAuction, yesno);
//        
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

        btnAccountNumber.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnMemberNo.setEnabled(!btnNew.isEnabled());
    }

    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }

    public void update(Observable observed, Object arg) {
        //cboProductID.setSelectedItem( (observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        if (observable.getActionType() == 1 || observable.getActionType() == 2 || observable.getActionType() == 8 || observable.getActionType() == 10) {
             if(cboProductID.getSelectedItem()==null ||cboProductID.getSelectedItem().equals("") ){
            cboProductID.setSelectedItem((observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
             }
         }
        txtAccountNumber.setText(observable.getTxtAccountNumber());
       if(cboProductID.getSelectedItem()==null ||cboProductID.getSelectedItem().equals("") ){
        cboProductID.setSelectedItem((observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
       }
        if (observable.getTxtNoOfUnusedChequeLeafs().equals("") || observable.getTxtNoOfUnusedChequeLeafs() == null) {
            txtNoOfUnusedChequeLeafs.setText("0");
        } else {
            txtNoOfUnusedChequeLeafs.setText(observable.getTxtNoOfUnusedChequeLeafs());
        }
        txtChargeDetails.setText(observable.getTxtChargeDetails());

        txtAccountClosingCharges.setText(observable.getTxtAccountClosingCharges());
       
        txtPayableBalance.setText(observable.getTxtPayableBalance());

        lblAccountHeadDisplay.setText(CommonUtil.convertObjToStr(observable.getAccountHeadDesc())
                + " (" + CommonUtil.convertObjToStr(observable.getAccountHeadId()) + " )");
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblHouseName.setText(observable.getCustomerStreet());
        lblBalanceDisplay.setText(observable.getAvailableBalance());
        lblStatus.setText(observable.getLblStatus());
        txtInterestPayable.setText(observable.getTxtInterestPayable());
        txtInsuranceCharges.setText(observable.getTxtInsuranceCharges());

        //        if(observable.getLoanInt() !=null)
        //            if(observable.getLoanInt()!="")
        //                txtInterestPayable.setText(observable.getLoanInt()); commented by abi let them modify interest details
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }

    public void update1(Observable observed, Object arg) {
        //cboProductID.setSelectedItem( (observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        if (observable.getTxtNoOfUnusedChequeLeafs().equals("") || observable.getTxtNoOfUnusedChequeLeafs() == null) {
            txtNoOfUnusedChequeLeafs.setText("0");
        } else {
            txtNoOfUnusedChequeLeafs.setText(observable.getTxtNoOfUnusedChequeLeafs());
        }
        txtChargeDetails.setText(observable.getTxtChargeDetails());

        txtAccountClosingCharges.setText(observable.getTxtAccountClosingCharges());
      
        txtPayableBalance.setText(observable.getTxtPayableBalance());

        lblAccountHeadDisplay.setText(CommonUtil.convertObjToStr(observable.getAccountHeadDesc())
                + " (" + CommonUtil.convertObjToStr(observable.getAccountHeadId()) + " )");
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblHouseName.setText(observable.getCustomerStreet());
        lblBalanceDisplay.setText(observable.getAvailableBalance());
        lblStatus.setText(observable.getLblStatus());
        txtInterestPayable.setText(observable.getTxtInterestPayable());
        txtInsuranceCharges.setText(observable.getTxtInsuranceCharges());
        //        if(observable.getLoanInt() !=null)
        //            if(observable.getLoanInt()!="")
        //                txtInterestPayable.setText(observable.getLoanInt()); commented by abi let them modify interest details
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
      
    }

    public void updateOBFields() {
       
        if (CommonUtil.convertObjToStr(cboProductID.getSelectedItem()).length() > 0) {
            observable.setTxtAccountNumber(txtAccountNumber.getText());
            observable.setTxtNoOfUnusedChequeLeafs(txtNoOfUnusedChequeLeafs.getText());
            observable.setTxtChargeDetails(String.valueOf(CommonUtil.convertObjToDouble(txtChargeDetails.getText()).doubleValue()));

            observable.setTxtAccountClosingCharges(txtAccountClosingCharges.getText());
            observable.setTxtPayableBalance(String.valueOf(CommonUtil.convertObjToDouble(txtPayableBalance.getText()).doubleValue()));
            observable.setTxtInsuranceCharges(txtInsuranceCharges.getText());
        }
        System.out.println("$@#$@$@$@#$#@$"+cboProductID.getSelectedItem());
        System.out.println("(String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected()"+(String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
        observable.setCboProductID((String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setScreen(this.getScreen());
    }

    public void updateSave() {
        observable.setTxtAccountClosingCharges(txtAccountClosingCharges.getText());
    }

    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnSave.setName("btnSave");
        cboProductID.setName("cboProductID");
        lblAccountClosingCharges.setName("lblAccountClosingCharges");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadDisplay.setName("lblAccountHeadDisplay");
        lblAccountNumber.setName("lblAccountNumber");
        lblBalance.setName("lblBalance");
        lblBalanceDisplay.setName("lblBalanceDisplay");
        lblChargeDetails.setName("lblChargeDetails");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblInterestPayable.setName("lblInterestPayable");
        lblNoOfUnusedChequeLeafs.setName("lblNoOfUnusedChequeLeafs");
        lblPayableCharges.setName("lblPayableCharges");
        lblProductID.setName("lblProductID");
        mbrCustomer.setName("mbrCustomer");
        panAccountHead.setName("panAccountHead");
        panAccountInfo.setName("panAccountInfo");
        panProductID.setName("panProductID");
        sptAccountInfo.setName("sptAccountInfo");
        txtAccountClosingCharges.setName("txtAccountClosingCharges");
        txtAccountNumber.setName("txtAccountNumber");
        txtChargeDetails.setName("txtChargeDetails");
        txtInterestPayable.setName("txtInterestPayable");
        txtNoOfUnusedChequeLeafs.setName("txtNoOfUnusedChequeLeafs");
        txtPayableBalance.setName("txtPayableBalance");
        lblHouseName.setName("lblHouseName");
    }

    private void internationalize() {
        AccountClosingRB resourceBundle = new AccountClosingRB();
        //        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblAccountHeadDisplay.setText(resourceBundle.getString("lblAccountHeadDisplay"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        if (prodType.equals("TermLoan")) {
            lblPayableCharges.setText(resourceBundle.getString("lblReceivableCharges"));
        } else {
            lblPayableCharges.setText(resourceBundle.getString("lblPayableCharges"));
        }
        if (prodType.equals("TermLoan")) {
            lblInterestPayable.setText(resourceBundle.getString("lblInterestReceivable"));
        } else {
            lblInterestPayable.setText(resourceBundle.getString("lblInterestPayable"));
        }
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblAccountClosingCharges.setText(resourceBundle.getString("lblAccountClosingCharges"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblChargeDetails.setText(resourceBundle.getString("lblChargeDetails"));
        lblBalanceDisplay.setText(resourceBundle.getString("lblBalanceDisplay"));
        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblNoOfUnusedChequeLeafs.setText(resourceBundle.getString("lblNoOfUnusedChequeLeafs"));
        lblCustomerNameDisplay.setText(resourceBundle.getString("lblCustomerNameDisplay"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnWaive;
    private com.see.truetransact.uicomponent.CButton btncheqDetails;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CCheckBox chkActnAct;
    private com.see.truetransact.uicomponent.CLabel lblAccountClosingCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAuctnAmt;
    private com.see.truetransact.uicomponent.CLabel lblBalancAmt;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBalanceDisplay;
    private com.see.truetransact.uicomponent.CLabel lblChargeDetails;
    private com.see.truetransact.uicomponent.CLabel lblCrDr;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblDrAccHead;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceCharges;
    private com.see.truetransact.uicomponent.CLabel lblInterestPayable;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblNoOfUnusedChequeLeafs;
    private com.see.truetransact.uicomponent.CLabel lblPayableCharges;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblSaAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccountInfoInner;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panAuction;
    private com.see.truetransact.uicomponent.CPanel panAuction1;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panSaAcctNum;
    private com.see.truetransact.uicomponent.CPanel panSaAcctNum1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CSeparator sptAccountInfo;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtAccountClosingCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAuctnAmt;
    private com.see.truetransact.uicomponent.CTextField txtBalancAmt;
    private com.see.truetransact.uicomponent.CTextField txtChargeDetails;
    private com.see.truetransact.uicomponent.CTextField txtDrAccHead;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtInsuranceCharges;
    private com.see.truetransact.uicomponent.CTextField txtInterestPayable;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtNoOfUnusedChequeLeafs;
    private com.see.truetransact.uicomponent.CTextField txtPayableBalance;
    private com.see.truetransact.uicomponent.CTextField txtSaAccountNumber;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JScrollPane srpChargeDetails;

    public static void main(String[] args) {
        AccountClosingUI ac = new AccountClosingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.show();
        ac.show();
    }

    
    public java.lang.String getChargeAmt() {
        return chargeAmt;
    }

    
    public void setChargeAmt(java.lang.String chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    
    public java.lang.String getBalanceAmt() {
        return balanceAmt;
    }

    
    public void setBalanceAmt(java.lang.String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    
    public boolean isDepositFlag() {
        return depositFlag;
    }

    
    public void setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
    }

    
    public int getViewType() {
        return viewType;
    }

    
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    
    public double getCalculateInt() {
        return calculateInt;
    }

    
    public void setCalculateInt(double calculateInt) {
        this.calculateInt = calculateInt;
    }

    private void callServiceTaxCalculation(String calAmt) {
        calMap = new HashMap();
        calMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        calMap.put("CAL_AMT", calAmt);
        calMap.put("ST_CAL", "ST_CAL");
        calMap.put("CHARGE_TYPE", "ACT_CLOSING_CHG");
        HashMap viewMap = new HashMap();
        viewMap.put("SERVICE_TAX", calMap);

        List clLst = ClientUtil.executeQuery("", viewMap);
       
        if (clLst != null && clLst.size() > 0) {
            calMap = (HashMap) clLst.get(0);
            
            if (CommonUtil.convertObjToDouble(calMap.get("SERVICE_TAX")).doubleValue() > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS1_TAX")).doubleValue() > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS2_TAX")).doubleValue() > 0) {
                ClientUtil.showAlertWindow("Service Tax :   " + calMap.get("SERVICE_TAX") + "\n"
                        + "Cess1 Tax :    " + calMap.get("CESS1_TAX") + "\n"
                        + "Cess2 Tax :    " + calMap.get("CESS2_TAX"));
            }
           
        }
    }

    
    public double getDepositAmt() {
        return depositAmt;
    }

    
    public void setDepositAmt(double depositAmt) {
        this.depositAmt = depositAmt;
    }
}
