/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountClosingOB.java
 *
 * Created on August 13, 2003, 4:30 PM
 */
package com.see.truetransact.ui.operativeaccount;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewall.TextAreaUI;
import com.see.truetransact.ui.common.viewall.EditTableUI;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountMasterTO;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountProductTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.EditWaiveTableUI;

/**
 *
 * @author Administrator Modified by Karthik
 */
public class AccountClosingOB extends CObservable {

    private double lienAmount;
    private double freezeAmount;
    private String cboProductID = "";
    private String txtAccountNumber = "";
    private String txtNoOfUnusedChequeLeafs = "";
    private String txtInterestPayable = "";
    private String txtInterestReceivable = "";
    private String txtChargeDetails = "";
    private String txtAccountClosingCharges = "";
    private String txtPayableBalance = "";
    private ComboBoxModel cbmProductID;
    private final String AUTHORIZE = "AUTHORIZE";
    private String accountHeadDesc;
    private String accountHeadId;
    private String customerName;
    private String availableBalance;
    private String VarNum = "";
    private int actionType;
    private int result;
    private HashMap linkMap = new HashMap();
    private HashMap calMap = new HashMap();
    private String loanBehaves = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private final static Logger log = Logger.getLogger(AccountClosingOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType = "";
    private String loanInt;
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap totalLoanAmount;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    AccountClosingRB objAccountClosingRB = new AccountClosingRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private double deposit_pre_int = 0;
    private String deposit_premature = null;
    private String normalCloser = null;
    private HashMap _authorizeMap;
    private String prematureString = "";
    private HashMap oldTransDetMap = null;
    private String asAnWhen = "";
    private String transProdId = "";
    private List chargelst = null;
    private String customerStreet = "";
    private double advancesCreditInterest = 0;
    private double subsidyEditTransAmt = 0;
    TableDialogUI tableDialogUI = null;
    private double avilableLoanSubsidy = 0;
    private double rebateInterest = 0;
    private double rebateEditTransAmt = 0;
    private double waiveEditTransAmt = 0;
    private double waiveEditPenalTransAmt = 0;
    private String txtDrAccHead="";
    HashMap susTypeMap = new HashMap();
    private Date currDt = null;
    private int nxtAccNo ;    
    private String startChequeNo;
    private ArrayList sbChqueList = new ArrayList();
    private boolean rebateInterestflag = false;
    private String rebate_mode="";
    private byte[] photoByteArray;

    public String getTxtInterestReceivable() {
        return txtInterestReceivable;
    }

    public void setTxtInterestReceivable(String txtInterestReceivable) {
        this.txtInterestReceivable = txtInterestReceivable;
    }

    public String getRebate_mode() {
        return rebate_mode;
    }

    public void setRebate_mode(String rebate_mode) {
        this.rebate_mode = rebate_mode;
    }
    
     
    public boolean isRebateInterestflag() {
        return rebateInterestflag;
    }

    public void setRebateInterestflag(boolean rebateInterestflag) {
        this.rebateInterestflag = rebateInterestflag;
    }
    
    public ArrayList getSbChqueList() {
        return sbChqueList;
    }

    public void setSbChqueList(ArrayList sbChqueList) {
        this.sbChqueList = sbChqueList;
    }
    
    
    public String getStartChequeNo() {
        return startChequeNo;
    }

    public void setStartChequeNo(String startChequeNo) {
        this.startChequeNo = startChequeNo;
    }
    
    public HashMap getSusTypeMap() {
        return susTypeMap;
    }

    public void setSusTypeMap(HashMap susTypeMap) {
        this.susTypeMap = susTypeMap;
    }

    public String getTxtDrAccHead() {
        return txtDrAccHead;
    }

    public void setTxtDrAccHead(String txtDrAccHead) {
        this.txtDrAccHead = txtDrAccHead;
    }
    private EditTableUI editableUI = null;
    private boolean waiveOffInterest = false;
    private boolean waiveoffPenal = false;
    private String txtInsuranceCharges = "";
    private String balCrDR = "";
    private String memNo = "";
//    TextAreaUI      objTextAreaUI=null;
    //    private boolean depTrans;
    private boolean auctnAct = false;
    private Double auctnAmt = null;
    private Double auctnBalAmt = null;
    private String auctnType;
    private String susprodId;
    private String susActnum;
    private ComboBoxModel cbmProdIdCr;
    HashMap lookUpHash;
    //added by rishad for seting waive amount 14/03/2014
    private boolean interestWaiveoff = false;
    private boolean noticeWaiveoff = false;
    private boolean principalwaiveoff = false;
    private boolean arcWaiveOff=false;
    private double arcWaiveAmount=0.0;
    private double penalWaiveAmount = 0.0;
    private double noticeWaiveAmount = 0.0;
    private double interestWaiveAmount = 0.0;
    private double principalWaiveAmount = 0.0;
    private boolean decreeWaiveOff = false;
    private boolean arbitraryWaiveOff = false;
    private boolean epCostWaiveOff = false;
    private boolean postageWaiveOff = false;
    private boolean advertiseWaiveOff = false;
    private boolean legalWaiveOff = false;
    private boolean insuranceWaiveOff = false;
    private boolean miscellaneousWaiveOff = false;
    private double epCostWaiveAmount = 0;
    private double postageWaiveAmount = 0;
    private double advertiseWaiveAmount = 0;
    private double legalWaiveAmount = 0;
    private double insuranceWaiveAmont = 0;
    private double miscellaneousWaiveAmount = 0;
    private double arbitarayWaivwAmount=0;
    private double decreeWaiveAmount = 0;
    private boolean recoveryWaiveOff = false;
    private double recoveryWaiveAmount = 0.0;
    private boolean measurementWaiveOff = false;
    private double  measurementWaiveAmount = 0.0;
    //added by rishad for listing waive detales and enter waive in this grid
    private EditWaiveTableUI editableWaiveUI = null;
    HashMap returnWaiveMap = null;
    HashMap productRecord = null;
    private HashMap maturityMap;
    String calcOn_Maturity = "";
    private HashMap serviceTax_Map = null;
    private String lblServiceTaxval = "";
    private ServiceTaxDetailsTO objservicetaxDetTo;
    private String authorizeBy2 ="";
    private boolean overDueIntWaiveOff = false; // for overdue interest
    private double overDueIntWaiveAmount = 0; // for overdue interest
    private HashMap loanRenewalMap = null;
    private boolean koleFieldOperationWaiveOff = false;
    private boolean koleFieldExpenseWaiveOff = false;
    private double koleFieldOperationWaiveAmount=0.0;
    private double koleFieldExpenseWaiveAmount=0.0;

    
    // for overdue interest
    public double getOverDueIntWaiveAmount() {
        return overDueIntWaiveAmount;
    }

    public void setOverDueIntWaiveAmount(double overDueIntWaiveAmount) {
        this.overDueIntWaiveAmount = overDueIntWaiveAmount;
    }

    public boolean isOverDueIntWaiveOff() {
        return overDueIntWaiveOff;
    }

    public void setOverDueIntWaiveOff(boolean overDueIntWaiveOff) {
        this.overDueIntWaiveOff = overDueIntWaiveOff;
    }
    
    // end

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public double getAdvertiseWaiveAmount() {
        return advertiseWaiveAmount;
    }

    public void setAdvertiseWaiveAmount(double advertiseWaiveAmount) {
        this.advertiseWaiveAmount = advertiseWaiveAmount;
    }

    public boolean isAdvertiseWaiveOff() {
        return advertiseWaiveOff;
    }

    public void setAdvertiseWaiveOff(boolean advertiseWaiveOff) {
        this.advertiseWaiveOff = advertiseWaiveOff;
    }

    public double getArbitarayWaivwAmount() {
        return arbitarayWaivwAmount;
    }

    public void setArbitarayWaivwAmount(double arbitarayWaivwAmount) {
        this.arbitarayWaivwAmount = arbitarayWaivwAmount;
    }

    public boolean isArbitraryWaiveOff() {
        return arbitraryWaiveOff;
    }

    public void setArbitraryWaiveOff(boolean arbitraryWaiveOff) {
        this.arbitraryWaiveOff = arbitraryWaiveOff;
    }

    public double getDecreeWaiveAmount() {
        return decreeWaiveAmount;
    }

    public void setDecreeWaiveAmount(double decreeWaiveAmount) {
        this.decreeWaiveAmount = decreeWaiveAmount;
    }

    public boolean isDecreeWaiveOff() {
        return decreeWaiveOff;
    }

    public void setDecreeWaiveOff(boolean decreeWaiveOff) {
        this.decreeWaiveOff = decreeWaiveOff;
    }

    public double getEpCostWaiveAmount() {
        return epCostWaiveAmount;
    }

    public void setEpCostWaiveAmount(double epCostWaiveAmount) {
        this.epCostWaiveAmount = epCostWaiveAmount;
    }

    public boolean isEpCostWaiveOff() {
        return epCostWaiveOff;
    }

    public void setEpCostWaiveOff(boolean epCostWaiveOff) {
        this.epCostWaiveOff = epCostWaiveOff;
    }

    public double getInsuranceWaiveAmont() {
        return insuranceWaiveAmont;
    }

    public void setInsuranceWaiveAmont(double insuranceWaiveAmont) {
        this.insuranceWaiveAmont = insuranceWaiveAmont;
    }

    public boolean isInsuranceWaiveOff() {
        return insuranceWaiveOff;
    }

    public void setInsuranceWaiveOff(boolean insuranceWaiveOff) {
        this.insuranceWaiveOff = insuranceWaiveOff;
    }

    public double getLegalWaiveAmount() {
        return legalWaiveAmount;
    }

    public void setLegalWaiveAmount(double legalWaiveAmount) {
        this.legalWaiveAmount = legalWaiveAmount;
    }

    public boolean isLegalWaiveOff() {
        return legalWaiveOff;
    }

    public void setLegalWaiveOff(boolean legalWaiveOff) {
        this.legalWaiveOff = legalWaiveOff;
    }

    public double getMiscellaneousWaiveAmount() {
        return miscellaneousWaiveAmount;
    }

    public void setMiscellaneousWaiveAmount(double miscellaneousWaiveAmount) {
        this.miscellaneousWaiveAmount = miscellaneousWaiveAmount;
    }

    public boolean isMiscellaneousWaiveOff() {
        return miscellaneousWaiveOff;
    }

    public void setMiscellaneousWaiveOff(boolean miscellaneousWaiveOff) {
        this.miscellaneousWaiveOff = miscellaneousWaiveOff;
    }

    public double getPostageWaiveAmount() {
        return postageWaiveAmount;
    }

    public void setPostageWaiveAmount(double postageWaiveAmount) {
        this.postageWaiveAmount = postageWaiveAmount;
    }

    public boolean isPostageWaiveOff() {
        return postageWaiveOff;
    }

    public void setPostageWaiveOff(boolean postageWaiveOff) {
        this.postageWaiveOff = postageWaiveOff;
    }
    
    public double getArcWaiveAmount() {
        return arcWaiveAmount;
    }

    public void setArcWaiveAmount(double arcWaiveAmount) {
        this.arcWaiveAmount = arcWaiveAmount;
    }

    public boolean isArcWaiveOff() {
        return arcWaiveOff;
    }

    public void setArcWaiveOff(boolean arcWaiveOff) {
        this.arcWaiveOff = arcWaiveOff;
    }
    
    public ComboBoxModel getCbmProdIdCr() {
        return cbmProdIdCr;
    }

    public double getInterestWaiveAmount() {
        return interestWaiveAmount;
    }

    public void setInterestWaiveAmount(double interestWaiveAmount) {
        this.interestWaiveAmount = interestWaiveAmount;
    }

    public boolean isInterestWaiveoff() {
        return interestWaiveoff;
    }

    public void setInterestWaiveoff(boolean interestWaiveoff) {
        this.interestWaiveoff = interestWaiveoff;
    }

    public double getNoticeWaiveAmount() {
        return noticeWaiveAmount;
    }

    public void setNoticeWaiveAmount(double noticeWaiveAmount) {
        this.noticeWaiveAmount = noticeWaiveAmount;
    }

    public boolean isNoticeWaiveoff() {
        return noticeWaiveoff;
    }

    public void setNoticeWaiveoff(boolean noticeWaiveoff) {
        this.noticeWaiveoff = noticeWaiveoff;
    }

    public double getPenalWaiveAmount() {
        return penalWaiveAmount;
    }

    public void setPenalWaiveAmount(double penalWaiveAmount) {
        this.penalWaiveAmount = penalWaiveAmount;
    }

    public double getPrincipalWaiveAmount() {
        return principalWaiveAmount;
    }

    public void setPrincipalWaiveAmount(double principalWaiveAmount) {
        this.principalWaiveAmount = principalWaiveAmount;
    }

    public boolean isPrincipalwaiveoff() {
        return principalwaiveoff;
    }

    public void setPrincipalwaiveoff(boolean principalwaiveoff) {
        this.principalwaiveoff = principalwaiveoff;
    }

    public void setCbmProdIdCr(ComboBoxModel cbmProdIdCr) {
        this.cbmProdIdCr = cbmProdIdCr;
    }

    public String getSusprodId() {
        return susprodId;
    }

    public void setSusprodId(String susprodId) {
        this.susprodId = susprodId;
    }

    public String getSusActnum() {
        return susActnum;
    }

    public void setSusActnum(String susActnum) {
        this.susActnum = susActnum;
    }

    public String getAuctnType() {
        return auctnType;
    }

    public void setAuctnType(String auctnType) {
        this.auctnType = auctnType;
    }

    public boolean isAuctnAct() {
        return auctnAct;
    }

    public void setAuctnAct(boolean auctnAct) {
        this.auctnAct = auctnAct;
    }

    public Double getAuctnAmt() {
        return auctnAmt;
    }

    public void setAuctnAmt(Double auctnAmt) {
        this.auctnAmt = auctnAmt;
    }

    public Double getAuctnBalAmt() {
        return auctnBalAmt;
    }

    public void setAuctnBalAmt(Double auctnBalAmt) {
        this.auctnBalAmt = auctnBalAmt;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }

    public String getBalCrDR() {
        return balCrDR;
    }

    public void setBalCrDR(String balCrDR) {
        this.balCrDR = balCrDR;
    }

    /**
     * Creates a new instance of AccountClosingOB
     */
    public AccountClosingOB() {
        try {
            proxy = ProxyFactory.createProxy();

            map = new HashMap();
            map.put(CommonConstants.JNDI, "AccountClosingJNDI");
            map.put(CommonConstants.HOME, "operativeaccount.AccountClosingHome");
            map.put(CommonConstants.REMOTE, "operativeaccount.AccountClosing");
            currDt = ClientUtil.getCurrentDate();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To fill the comboboxes
     */
    public void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        param = new java.util.HashMap();

        if (prodType.equals("TermLoan")) {
            param.put(CommonConstants.MAP_NAME, "getLoanProducts");
        } else {
            param.put(CommonConstants.MAP_NAME, "getAccProducts");
        }
        param.put(CommonConstants.PARAMFORQUERY, null);

        final HashMap lookupValues = ClientUtil.populateLookupData(param);

        fillData((HashMap) lookupValues.get("DATA"));
        cbmProductID = new ComboBoxModel(key, value);
    }

    /**
     * To set the key & value for comboboxes
     */
    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        // System.out.println("obj in OB : " + obj);
        obj.put(CommonConstants.MAP_WHERE, getTxtAccountNumber());
        if (prodType.equals("TermLoan")) {
            if (getTransProdId() != null && getTransProdId().length() > 0) {
                obj.put("PROD_ID", getTransProdId());
            } else {
                obj.put("PROD_ID", getCbmProductID().getKeyForSelected());
            }
        }
        obj.put("PROD_TYPE", prodType);

        obj.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
        HashMap where = proxy.executeQuery(obj, map);
        //        keyValue = (HashMap)where.get("AccountDetailsTO");
        //        transactionOB.setDetails((List)where.get("TransactionTO"));
        //        log.info("Got HashMap");
        return where;
    }

    public void filterProd() {
  
      HashMap whereMap=new HashMap();
        //System.out.println("getMemNo().toString()" + getMemNo().toString());
       // param.put("MEMBERNO",CommonUtil.convertObjToStr(getMemNo()));
//            param.put(CommonConstants.MAP_NAME,"getLoanProductsSearch");
       // List prodDes = ClientUtil.executeQuery("getLoanProductsSearch", param);
        whereMap.put("MEMBERNO",CommonUtil.convertObjToStr(getMemNo()));
         List prodDes = ClientUtil.executeQuery("getLoanProductsSearch", whereMap);
        if (prodDes.size() <= 0 || prodDes.isEmpty()) {
            ClientUtil.showAlertWindow("This member does not have a loan");
            return;
        }
        param = new HashMap();
        key = new ArrayList();
        value = new ArrayList();
        param.put("KEY", "");
        param.put("VALUE", " ");

//             fillData(param);
        // param= new HashMap();
        key.add("");
        value.add("");
        for (int i = 0; i < prodDes.size(); i++) {

            param = (HashMap) prodDes.get(i);
            key.add(param.get("KEY"));
            value.add(param.get("VALUE"));
        }
        //   cbmProductID=new ComboBoxModel();
        cbmProductID = new ComboBoxModel(key, value);

    }

    public void setCbmProdIdCr(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdIdCr = new ComboBoxModel(key, value);
        this.cbmProdIdCr = cbmProdIdCr;
        setChanged();
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    void setCboProductID(String cboProductID) {
        this.cboProductID = cboProductID;
        setChanged();
    }

    String getCboProductID() {
        return this.cboProductID;
    }

    void setTxtAccountNumber(String txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }

    String getTxtAccountNumber() {
        return this.txtAccountNumber;
    }

    void setTxtNoOfUnusedChequeLeafs(String txtNoOfUnusedChequeLeafs) {
        this.txtNoOfUnusedChequeLeafs = txtNoOfUnusedChequeLeafs;
        setChanged();
    }

    String getTxtNoOfUnusedChequeLeafs() {
        return this.txtNoOfUnusedChequeLeafs;
    }

    void setTxtInterestPayable(String txtInterestPayable) {
        this.txtInterestPayable = txtInterestPayable;
        setChanged();
    }

    String getTxtInterestPayable() {
        return this.txtInterestPayable;
    }

    void setTxtChargeDetails(String txtChargeDetails) {
        this.txtChargeDetails = txtChargeDetails;
        setChanged();
    }

    String getTxtChargeDetails() {
        return this.txtChargeDetails;
    }

    public void setTxtAccountClosingCharges(String txtAccountClosingCharges) {
        this.txtAccountClosingCharges = txtAccountClosingCharges;
        setChanged();
    }

    public String getTxtAccountClosingCharges() {
        return this.txtAccountClosingCharges;
    }

    void setTxtPayableBalance(String txtPayableBalance) {
        this.txtPayableBalance = txtPayableBalance;
        setChanged();
    }

    String getTxtPayableBalance() {
        return this.txtPayableBalance;
    }

    void setCbmProductID(ComboBoxModel cbmProductID) {
        this.cbmProductID = cbmProductID;
        setChanged();
    }

    ComboBoxModel getCbmProductID() {
        return this.cbmProductID;
    }

    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }

    public void setAccountHeadDesc(String accountHeadDesc) {
        this.accountHeadDesc = accountHeadDesc;
        setChanged();
    }

    public String getAccountHeadId() {
        return this.accountHeadId;
    }

    public void setAccountHeadId(String accountHeadId) {
        this.accountHeadId = accountHeadId;
        setChanged();
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        setChanged();
    }

    public String getAvailableBalance() {
        return this.availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
        setChanged();
    }

    public int getActionType() {
        return this.actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public String getLblStatus() {
        return this.lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    /**
     * set the value of Account head ID and description based on the product
     * selected in the UI
     */
    public void getAccountHeadForProduct() {

        /*
         * based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen same LookUp
         * bean will be used for this purpose
         */
        param.put(CommonConstants.MAP_NAME, "getAccHead");
        param.put(CommonConstants.PARAMFORQUERY, getCboProductID());
        try {
            final HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap) lookupValues.get("DATA"));
            //If proper value is returned, then the size will be more than 1, else do nothing
            if (value.size() > 1) {
                setAccountHeadId((String) value.get(1));
                setAccountHeadDesc((String) key.get(1));
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }

    }

    /**
     * To get customername & balance info
     */
    //    public void getCustomerNameForAccountNumber() {
    /*
     * based on the selection from the product combo box, one accound head will
     * be fetched from database and displayed on screen same LookUp bean will be
     * used for this purpose
     *
     * param.put(CommonConstants.MAP_NAME,"getCustomerName");
     * param.put(CommonConstants.PARAMFORQUERY, getTxtAccountNumber()); try {
     * HashMap lookupValues = ClientUtil.populateLookupData(param);
     * fillData((HashMap)lookupValues.get("DATA"));
     * setCustomerName((String)value.get(1));
     * setAvailableBalance((String)key.get(1));
     * param.put(CommonConstants.MAP_NAME,"getAccountClosingCharges");
     * lookupValues = ClientUtil.populateLookupData(param);
     * fillData((HashMap)lookupValues.get("DATA"));
     * this.txtAccountClosingCharges=(String)(value.get(1));
     * setTxtAccountClosingCharges((String)(value.get(1))); //
     * System.out.println(this.getTxtAccountClosingCharges());
     * ttNotifyObservers(); } catch (Exception e) {
     * parseException.logException(e,true); // }
     */
    //        getAccountClosingCharges();
    //    }
    public void getAccountClosingCharges() {
        param = new HashMap();
        param.put("MODE", getCommand());
        // System.out.println("param ... : " + param);
        advancesCreditInterest = 0;
        subsidyEditTransAmt = 0;
        rebateEditTransAmt = 0;
        //for premature deposit closer
        if (getDeposit_pre_int() > 0 && getDeposit_premature() != null && getDeposit_premature().length() > 0) {
            param.put("DEPOSIT INT", new Double(getDeposit_pre_int()));

            param.put(getDeposit_premature(), getDeposit_premature());
        }
        if (normalCloser != null && normalCloser.length() > 0) {
            param.put("NORMAL_CLOSER", "NORMAL_CLOSER");
        }
        try {

            param.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
            HashMap dataMap = populateData(param);
            HashMap chargeMap = (HashMap) dataMap.get("AccountDetailsTO");
            //            List interest=(List)dataMap.get("AccountDetailsTO");
            //            HashMap loneInt=(HashMap)interest.get(0);
            HashMap intMap = new HashMap();
            if (prodType.equals("TermLoan")&& chargeMap!=null && chargeMap.containsKey("AS_CUSTOMER_COMES")) {
                setAsAnWhen(CommonUtil.convertObjToStr(chargeMap.get("AS_CUSTOMER_COMES")));
            }
            //                 intMap = (HashMap) dataMap.get("AccountInterest");
            //System.out.println("chargeMap :: " + chargeMap);
            if (prodType.equals("TermLoan")) {
                double totalAmt = 0;
                double totalPenalAmt = 0;
                //                double clearBalance=CommonUtil.convertObjToDouble(chargeMap.get("CLEAR_BALANCE")).doubleValue();
                double LOAN_BALANCE_PRINCIPAL = CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                if (LOAN_BALANCE_PRINCIPAL < 0) {
                    if(getLoanBehaves().equals("OD")){ // Added by nithya on 15-04-2020 for KD 889
                       totalLoanAmount.put("OD_CREDIT_BALANCE","OD_CREDIT_BALANCE");
                    }
                    LOAN_BALANCE_PRINCIPAL = LOAN_BALANCE_PRINCIPAL * (-1);
                    totalLoanAmount.put("LOAN_BALANCE_PRINCIPAL", new Double(LOAN_BALANCE_PRINCIPAL));
                    setAvailableBalance(String.valueOf(LOAN_BALANCE_PRINCIPAL));
                }

                avilableLoanSubsidy = CommonUtil.convertObjToDouble(totalLoanAmount.get("AVAILABLE_SUBSIDY")).doubleValue();
                if (avilableLoanSubsidy > 0) {
                    int confirm = ClientUtil.confirmationAlert("Subsidy Amount Available for this Account!!!!" + "\n" + "Do you want to adjust the Subsidy.");
                    if (confirm == 0) {
                        totalLoanAmount.put("AVAILABLE_SUBSIDY", new Double(avilableLoanSubsidy));
                        subsidyEditTransAmt = avilableLoanSubsidy;
                    } else {
                        avilableLoanSubsidy = 0;
                        subsidyEditTransAmt = avilableLoanSubsidy;
                        totalLoanAmount.put("AVAILABLE_SUBSIDY", new Double(avilableLoanSubsidy));

                    }
                }
                int waiveconfirm = -1, penalwaiveconfirm = -1;
//                //added by rish for waiving option
//                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y")
//                        || CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y")) {
//                    waiveconfirm = ClientUtil.confirmationAlert("Do you want to WAIVE");
//                    if (waiveconfirm == 0) {
//                        showEditWaiveTableUI(totalLoanAmount);
//                        returnWaiveMap = waiveOffEditInterestAmt();
//                        double penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
//                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && penalWaiveAmt > 0) {
//                            waiveoffPenal = true;
//                            penalWaiveAmount = penalWaiveAmt;
//                        } else {
//                            waiveoffPenal = true;
//                            penalWaiveAmount = 0;
//                        }
//                        double interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
//                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") && interestWaiveAmt > 0) {
//                            interestWaiveoff = true;
//                            interestWaiveAmount = interestWaiveAmt;
//                        } else {
//                            interestWaiveoff = false;
//                            interestWaiveAmount = 0;
//
//                        }
//                        double noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
//                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") && noticeWaiveAmt > 0) {
//                            noticeWaiveoff = true;
//                            noticeWaiveAmount = noticeWaiveAmt;
//                        } else {
//                            noticeWaiveoff = false;
//                            noticeWaiveAmount = 0;
//                        }
//                        double principalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PRINCIPAL"));
//                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") && principalWaiveAmt > 0) {
//                            principalwaiveoff = true;
//                            principalWaiveAmount = principalWaiveAmt;
//                        } else {
//                            principalwaiveoff = false;
//                            principalWaiveAmount = 0;
//                        }
//
//                    }
//                }

//                 if(CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y")){
//                     waiveconfirm=ClientUtil.confirmationAlert("Do you want to waiveoff the Interest.");
//                if(waiveconfirm==0){
//                    waiveOffInterest=true;
//                    showEditTableUI(totalLoanAmount);
//                   waiveEditTransAmt= waiveOffEditInterestAmt();
//                    
//                }else{
//                    waiveOffInterest=false;
//                    waiveEditTransAmt=-1;
//                }
//                 }

//                   if(CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_CLOSING_PENAL_INT")).doubleValue()>0){
//                     penalwaiveconfirm=ClientUtil.confirmationAlert("Do you want to waiveoff the Penal Interest.");
//                if(penalwaiveconfirm==0){
//                    waiveoffPenal=true;
////                    showEditTableUI(totalLoanAmount);
//                   waiveEditPenalTransAmt= CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")).doubleValue();//waiveOffEditInterestAmt();
//                    
//                }else{
//                    waiveoffPenal=false;
//                    waiveEditPenalTransAmt=0;
//                }
//                 }
                if (waiveconfirm != 0 && CommonUtil.convertObjToDouble(totalLoanAmount.get("ACCOUNT_CLOSING_PENAL_INT")).doubleValue() == 0) {
                    rebateInterest = CommonUtil.convertObjToDouble(totalLoanAmount.get("REBATE_INTEREST")).doubleValue();
                    if (rebateInterest > 0) {
                        setRebate_mode(CommonUtil.convertObjToStr(totalLoanAmount.get("REBATE_MODE")));
                        if(totalLoanAmount.get("REBATE_MODE").equals("Transfer")){
                        int confirm = ClientUtil.confirmationAlert("Rebate Interest Amount Available for this Account!!!!" + "\n" + "Do you want to adjust the Rebate Interest.");
                        if (confirm == 0) {
                            setRebateInterestflag(true);
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));
                            rebateEditTransAmt = rebateInterest;
                        } else {
                            setRebateInterestflag(false);
                            rebateInterest = 0;
                            rebateEditTransAmt = rebateInterest;
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));

                        }
                        }
                        else{
                         int confirm = ClientUtil.confirmationAlert("Rebate Interest Amount Available for this Account!!!!" + "\n" + "Do you want to give the Rebate Interest.");
                        if (confirm == 0) {
                            setRebateInterestflag(true);
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));
                          //  rebateEditTransAmt = rebateInterest;
                        } else {
                            setRebateInterestflag(false);
                            rebateInterest = 0;
                            rebateEditTransAmt = rebateInterest;
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));

                        }
                        }
                    }
                }

                //editableUI.show();

                //               setAvailableBalance(CommonUtil.convertObjToStr(chargeMap.get("CLEAR_BALANCE")));
                //            setTxtPayableBalance(CommonUtil.convertObjToStr(chargeMap.get("CLEAR_BALANCE")));
                setLoanInt(CommonUtil.convertObjToStr(dataMap.get("AccountInterest")));
                if (param.containsKey("NORMAL_CLOSER")) {
                    totalLoanAmount.put("CURR_MONTH_INT", new Double(getLoanInt()));
                }
                //System.out.println("getLinkMap :: " + getLinkMap());
                if (getLinkMap() != null && getLinkMap().containsKey("AS_CUSTOMER_COMES") && getLinkMap().get("AS_CUSTOMER_COMES").equals("N")) {
                    totalAmt = CommonUtil.convertObjToDouble(dataMap.get("AccountInterest")).doubleValue();
                    if (dataMap.containsKey("AccountPenalInterest") && dataMap.get("AccountPenalInterest") != null) {
                        totalPenalAmt = CommonUtil.convertObjToDouble(dataMap.get("AccountPenalInterest")).doubleValue();
                    }
                    totalLoanAmount.put("CALCULATE_INT", new Double(totalAmt));
//                      commented by Chithra For Showing Correct popup in DepositClosing (LTD)
//                    if (getDeposit_premature() != null && getDeposit_premature().length() > 0)//premature deposit closeer only
//                    {
//                        totalAmt = 0;
//                    }

                    totalLoanAmount.put("CALCULATE_PENAL_INT", new Double(totalPenalAmt));
                    totalAmt += totalPenalAmt;
                } else {
                    if(totalLoanAmount.containsKey("OD_CREDIT_BALANCE") && totalLoanAmount.get("OD_CREDIT_BALANCE") != null && CommonUtil.convertObjToStr(totalLoanAmount.get("OD_CREDIT_BALANCE")).equalsIgnoreCase("OD_CREDIT_BALANCE")){ // Added by nithya on 15-04-2020 for KD 889
                       totalLoanAmount.put("CALCULATE_INT", totalLoanAmount.get("CURR_MONTH_INT"));
                       totalLoanAmount.put("CALCULATE_PENAL_INT", totalLoanAmount.get("PENAL_INT"));                    
                    }
                    String emi = CommonUtil.convertObjToStr((totalLoanAmount.get("POSTAGE CHARGES") == null) ? "0" : totalLoanAmount.get("POSTAGE CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("MISCELLANEOUS CHARGES") == null) ? "0" : totalLoanAmount.get("MISCELLANEOUS CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("LEGAL CHARGES") == null) ? "0" : totalLoanAmount.get("LEGAL CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("INSURANCE CHARGES") == null) ? "0" : totalLoanAmount.get("INSURANCE CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("EXECUTION DECREE CHARGES") == null) ? "0" : totalLoanAmount.get("EXECUTION DECREE CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("ARBITRARY CHARGES") == null) ? "0" : totalLoanAmount.get("ARBITRARY CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("ADVERTISE CHARGES") == null) ? "0" : totalLoanAmount.get("ADVERTISE CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("NOTICE CHARGES") == null) ? "0" : totalLoanAmount.get("NOTICE CHARGES"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("ARC_COST") == null) ? "0" : totalLoanAmount.get("ARC_COST"));
                    totalAmt += Double.parseDouble(emi);
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("EP_COST") == null) ? "0" : totalLoanAmount.get("EP_COST"));
                    totalAmt += Double.parseDouble(emi);
                    if(totalLoanAmount.containsKey("EMI_OVERDUE_CHARGE") && totalLoanAmount.get("EMI_OVERDUE_CHARGE") != null){// Added by nithya for 0008470
                    emi = CommonUtil.convertObjToStr((totalLoanAmount.get("EMI_OVERDUE_CHARGE") == null) ? "0" : totalLoanAmount.get("EMI_OVERDUE_CHARGE"));//For overdue charge
                    totalAmt += Double.parseDouble(emi);
                    }
                    if (totalLoanAmount.containsKey("RECOVERY CHARGES") && totalLoanAmount.get("RECOVERY CHARGES") != null) {
                        emi = CommonUtil.convertObjToStr((totalLoanAmount.get("RECOVERY CHARGES") == null) ? "0" : totalLoanAmount.get("RECOVERY CHARGES"));
                        totalAmt += Double.parseDouble(emi);
                    }
                    if (totalLoanAmount.containsKey("MEASUREMENT CHARGES") && totalLoanAmount.get("MEASUREMENT CHARGES") != null) {
                        emi = CommonUtil.convertObjToStr((totalLoanAmount.get("MEASUREMENT CHARGES") == null) ? "0" : totalLoanAmount.get("MEASUREMENT CHARGES"));
                        totalAmt += Double.parseDouble(emi);
                    }
                    
                    if (totalLoanAmount.containsKey("KOLEFIELD EXPENSE") && totalLoanAmount.get("KOLEFIELD EXPENSE") != null) {
                        emi = CommonUtil.convertObjToStr((totalLoanAmount.get("KOLEFIELD EXPENSE") == null) ? "0" : totalLoanAmount.get("KOLEFIELD EXPENSE"));
                        totalAmt += Double.parseDouble(emi);
                    }
                    
                    if (totalLoanAmount.containsKey("KOLEFIELD OPERATION") && totalLoanAmount.get("KOLEFIELD OPERATION") != null) {
                        emi = CommonUtil.convertObjToStr((totalLoanAmount.get("KOLEFIELD OPERATION") == null) ? "0" : totalLoanAmount.get("KOLEFIELD OPERATION"));
                        totalAmt += Double.parseDouble(emi);
                    }
                    
                    
                }
                totalAmt -= rebateEditTransAmt;
//                if(waiveEditTransAmt>0){
//                totalAmt-=waiveEditTransAmt;
//                }
                //  added by rishad 19/04/2014 for deduction for waive amount from payable amount
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") && interestWaiveAmount > 0) {
                    totalAmt -= interestWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && penalWaiveAmount > 0) {
                    totalAmt -= penalWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") && noticeWaiveAmount > 0) {
                    totalAmt -= noticeWaiveAmount;
                }
               if (CommonUtil.convertObjToStr(totalLoanAmount.get("ARC_WAIVER")).equals("Y") && arcWaiveAmount > 0) {
                    totalAmt -=arcWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("EP_COST_WAIVER")).equals("Y") && epCostWaiveAmount> 0) {
                    totalAmt -= epCostWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("POSTAGE_WAIVER")).equals("Y") && postageWaiveAmount > 0) {
                    totalAmt -= postageWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("ADVERTISE_WAIVER")).equals("Y") && advertiseWaiveAmount > 0) {
                    totalAmt -= advertiseWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("LEGAL_WAIVER")).equals("Y") && legalWaiveAmount > 0) {
                    totalAmt -= legalWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INSURANCE_WAIVER")).equals("Y") && insuranceWaiveAmont > 0) {
                    totalAmt -=  insuranceWaiveAmont;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("MISCELLANEOUS_WAIVER")).equals("Y") && miscellaneousWaiveAmount > 0) {
                    totalAmt -= miscellaneousWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("DECREE_WAIVER")).equals("Y") && decreeWaiveAmount> 0) {
                    totalAmt -= decreeWaiveAmount;
                }
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("ARBITRARY_WAIVER")).equals("Y") && arbitarayWaivwAmount > 0) {
                    totalAmt -= arbitarayWaivwAmount;
                }
                
                if (totalLoanAmount.containsKey("OVERDUEINT_WAIVER") && totalLoanAmount.get("OVERDUEINT_WAIVER") != null) {
                    if (CommonUtil.convertObjToStr(totalLoanAmount.get("OVERDUEINT_WAIVER")).equals("Y") && overDueIntWaiveAmount > 0) {
                        totalAmt -= overDueIntWaiveAmount;
                    }
                }
                
                if (totalLoanAmount.containsKey("RECOVERY_WAIVER") && totalLoanAmount.get("RECOVERY_WAIVER") != null) {
                    if (CommonUtil.convertObjToStr(totalLoanAmount.get("RECOVERY_WAIVER")).equals("Y") && recoveryWaiveAmount > 0) {
                        totalAmt -= recoveryWaiveAmount;
                    }
                }
                if (totalLoanAmount.containsKey("MEASUREMENT_WAIVER") && totalLoanAmount.get("MEASUREMENT_WAIVER") != null) {
                    if (CommonUtil.convertObjToStr(totalLoanAmount.get("MEASUREMENT_WAIVER")).equals("Y") && measurementWaiveAmount > 0) {
                        totalAmt -= measurementWaiveAmount;
                    }
                }
                
                if (totalLoanAmount.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && totalLoanAmount.get("KOLE_FIELD_EXPENSE_WAIVER") != null) {
                    if (CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y") && koleFieldOperationWaiveAmount > 0) {
                        totalAmt -= koleFieldOperationWaiveAmount;
                    }
                }
                
                if (totalLoanAmount.containsKey("KOLE_FIELD_OPERATION_WAIVER") && totalLoanAmount.get("KOLE_FIELD_OPERATION_WAIVER") != null) {
                    if (CommonUtil.convertObjToStr(totalLoanAmount.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y") && koleFieldExpenseWaiveAmount > 0) {
                        totalAmt -= koleFieldExpenseWaiveAmount;
                    }
                }
                 
                 
                
                

//                 if(waiveEditPenalTransAmt>0){
//                    totalAmt-=CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")).doubleValue();
//                }
                String emi = CommonUtil.convertObjToStr((totalLoanAmount.get("CURR_MONTH_INT") == null) ? "0" : totalLoanAmount.get("CURR_MONTH_INT"));
                //                if( getDeposit_premature()!=null && getDeposit_premature().length()>0)
                totalAmt += Double.parseDouble(emi);// COMMENTED BY ABI FOR NOT TAKING INT FROM LOAN INSTALLMENT
                //                String prince=CommonUtil.convertObjToStr((totalLoanAmount.get("CURR_MONTH_PRINCEPLE")==null)? "0.00":totalLoanAmount.get("CURR_MONTH_PRINCEPLE"));
                //                totalAmt+=Double.parseDouble(prince);
                String Overprince = CommonUtil.convertObjToStr((totalLoanAmount.get("OVER_DUE_PRINCIPAL") == null) ? "0.00" : totalLoanAmount.get("OVER_DUE_PRINCIPAL"));
                totalAmt += Double.parseDouble(Overprince);
                String overDueInt = CommonUtil.convertObjToStr((totalLoanAmount.get("OVER_DUE_INTEREST") == null) ? "0.00" : totalLoanAmount.get("OVER_DUE_INTEREST"));
                totalAmt += Double.parseDouble(overDueInt);
                String penal = CommonUtil.convertObjToStr((totalLoanAmount.get("PENAL_INT") == null) ? "0.00" : totalLoanAmount.get("PENAL_INT"));
                totalAmt += Double.parseDouble(penal);
                HashMap otherChrgMap = new HashMap();
                double otherChrges = 0.0;
                if (totalLoanAmount.containsKey("OTHER_CHARGES") && totalLoanAmount.get("OTHER_CHARGES") != null) {
                    otherChrgMap = (HashMap) totalLoanAmount.get("OTHER_CHARGES");
                }
                if (otherChrgMap.containsKey("OTHER CHARGES") && otherChrgMap.get("OTHER CHARGES") != null) {
                    otherChrges = CommonUtil.convertObjToDouble(otherChrgMap.get("OTHER CHARGES"));
                }
                totalAmt = totalAmt + otherChrges;
                setLoanInt(String.valueOf(totalAmt));
                setTxtInterestPayable(getLoanInt());
                if (getDeposit_premature() != null && getDeposit_premature().length() > 0) {
                    double calculateInt = CommonUtil.convertObjToDouble(totalLoanAmount.get("CALCULATE_INT")).doubleValue();
                    totalAmt = CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                    totalAmt -= calculateInt;
                } else {

                    if (totalLoanAmount.containsKey("OTS") && CommonUtil.convertObjToStr(totalLoanAmount.get("OTS")).equals("Y")) {   //modified by rishad for deduction on 19/04/2014 for duction of principal waive amount from principal
                        totalAmt = CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") && principalWaiveAmount > 0) {
                            totalAmt -= principalWaiveAmount;
                        }

                    } else {
                        totalAmt = CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                        //modified by rishad for deduction on 19/04/2014 for duction of principal waive amount from principal
                        if (CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") && principalWaiveAmount > 0) {
                            totalAmt -= principalWaiveAmount;
                        }
                        String clrBlnc="";
                        if(totalLoanAmount!=null && totalLoanAmount.containsKey("CLEAR_BALANCE")){
                            clrBlnc=CommonUtil.convertObjToStr(totalLoanAmount.get("CLEAR_BALANCE"));
                        }
                        if (clrBlnc!=null && clrBlnc.startsWith("-")) {
                            totalAmt = -1 * totalAmt;
                            setBalCrDR("Dr");
                        } else {
                            setBalCrDR("Cr");
                        }

                      
                        
//                        totalAmt=CommonUtil.convertObjToDouble(totalLoanAmount.get("CLEAR_BALANCE")).doubleValue();
                    }
                    if (dataMap.containsKey("ADVANCES_CREDIT_INT") && dataMap.get("ADVANCES_CREDIT_INT") != null) {
                        double advCreditInterest = CommonUtil.convertObjToDouble(dataMap.get("ADVANCES_CREDIT_INT")).doubleValue();
                        //  totalAmt+=-advCreditInterest;//babu
                        totalAmt += +advCreditInterest;
                        if (CommonUtil.convertObjToDouble(getTxtInterestPayable()) > 0) {
                            totalAmt = totalAmt - CommonUtil.convertObjToDouble(getTxtInterestPayable());
                            totalAmt = Math.abs(totalAmt);
                        }
                        advancesCreditInterest = advCreditInterest;
                        ClientUtil.showMessageWindow("Advances  Out Standing Amount :" + CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue() + "\n"
                                + "Advances Credit Interest :" + advCreditInterest + "\n"
                                + "Advances Debit Interest :" + getTxtInterestPayable() + "\n"
                                + "Remaining Due            :" + totalAmt);
                    }
                }
                setTxtPayableBalance(CommonUtil.convertObjToStr(totalAmt));//babu
                txtPayableBalance = CommonUtil.convertObjToStr(totalAmt);
                setAvailableBalance(String.valueOf(totalAmt));
                //            setLoanInt(CommonUtil.convertObjToStr(dataMap.get("AccountInterest")));
                if (chargeMap != null && chargeMap.containsKey("CUSTOMER NAME")) {
                    setCustomerName(CommonUtil.convertObjToStr(chargeMap.get("CUSTOMER NAME")));
                }
                if (chargeMap != null && chargeMap.containsKey("HOUSE_NAME")) {
                    setCustomerStreet(CommonUtil.convertObjToStr(chargeMap.get("HOUSE_NAME")));
                }
                populateStockPhoto(dataMap);  // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option              
            } else {

                intMap = (HashMap) dataMap.get("AccountInterest");
                setAccountHeadDesc(CommonUtil.convertObjToStr(chargeMap.get("ac_hd_desc")));
                //System.out.println("act_closing_chrg :::"+chargeMap.get("act_closing_chrg")+" cc:"+chargeMap.get("ACT_CLOSING_CHRG"));
                setTxtAccountClosingCharges(String.valueOf(CommonUtil.convertObjToDouble(chargeMap.get("ACT_CLOSING_CHRG")).doubleValue()));
                setCustomerName(CommonUtil.convertObjToStr(chargeMap.get("CUSTOMER NAME")));
                setCustomerStreet(CommonUtil.convertObjToStr(chargeMap.get("HOUSE_NAME")));
                setTxtNoOfUnusedChequeLeafs(String.valueOf(CommonUtil.convertObjToDouble(chargeMap.get("unused_chk")).doubleValue()));
                setAvailableBalance(CommonUtil.convertObjToStr(chargeMap.get("AVAILABLE_BALANCE")));
                setTxtInterestPayable(String.valueOf(CommonUtil.convertObjToDouble(intMap.get(getTxtAccountNumber())).doubleValue()));
                setTxtChargeDetails(CommonUtil.convertObjToStr(chargeMap.get("MISC_SERV_CHRG")));
                setTxtPayableBalance(CommonUtil.convertObjToStr(chargeMap.get("payable_bal")));
                // Added by nithya on 27-03-2017                
                HashMap todCheckmap = new HashMap();
                todCheckmap.put("PROD_ID",getCboProductID());
                List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
                if (todList != null && todList.size() > 0) {
                    HashMap todMap = (HashMap) todList.get(0);
                    if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                        if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                            // calculate sb od debit and credit interest
                            setAvailableBalance(CommonUtil.convertObjToStr(chargeMap.get("CLEAR_BALANCE")));
                            String prodType = "OA";
                            String prodId = CommonUtil.convertObjToStr(todCheckmap.get("PROD_ID"));
                            Date dt = setFromDate(prodType, prodId);
                            double sbODDebitInt = 0;
                            double sbODCreditInt = 0;
                            HashMap SBODIntParamMap = new HashMap();
                            SBODIntParamMap.put("ACT_NUM", getTxtAccountNumber());
                            SBODIntParamMap.put("START_DATE",dt);
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
                            System.out.println("sbODCreditInt :: "+ sbODCreditInt);
                            System.out.println("sbODDebitInt :: "+ sbODDebitInt);
                            if(sbODCreditInt > 0){
                                setTxtInterestPayable(CommonUtil.convertObjToStr(sbODCreditInt));
                            }
                            if(sbODDebitInt > 0){
                                setTxtInterestReceivable(CommonUtil.convertObjToStr(sbODDebitInt));
                            }                                               
                        }
                    }
                }
            }
            // setTxtPayableBalance(CommonUtil.convertObjToStr(totalAmt));
            // System.out.println("intMap.get(getTxtAccountNumber())" + intMap.get(getTxtAccountNumber()));
            //for as an premature
            if (getAsAnWhen() != null && getAsAnWhen().equals("Y")) {
                totalLoanAmount.put("INTEREST", dataMap.get("AccountInterest"));
            }
            if(getServiceTax_Map()!=null && getServiceTax_Map().containsKey("TOT_TAX_AMT")){
                totalLoanAmount.put("ServiceTaxAmt",serviceTax_Map.get("TOT_TAX_AMT"));
            }
            ttNotifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public HashMap waiveOffEditInterestAmt() {
        double totalWaiveamt = 0;
        double editWaiveOffTransAmt = 0;
        HashMap resultWaiveMap = new HashMap();
        ArrayList singleList = new ArrayList();
        if (editableWaiveUI != null) {
            ArrayList list = editableWaiveUI.getTableData();
            for (int i = 0; i < list.size(); i++) {
                singleList = (ArrayList) list.get(i);
                totalWaiveamt += CommonUtil.convertObjToDouble(singleList.get(2));
                resultWaiveMap.put(singleList.get(0), singleList.get(2));
            }
        }
        resultWaiveMap.put("Total_WaiveAmt", CommonUtil.convertObjToStr(totalWaiveamt));
        return resultWaiveMap;
    }

    public void showEditWaiveTableUI(HashMap totalLoanAmount) {
        ArrayList singleList = new ArrayList();
        HashMap listMap = new HashMap();
        listMap.put("PENAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")));
        listMap.put("INTEREST", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_INT")));
        listMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_PRINCEPLE")));
        listMap.put("NOTICE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("NOTICE CHARGES")));
        listMap.put("ARC_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARC_COST")));
        listMap.put("ARBITRARY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARBITRARY CHARGES")));
        listMap.put("EXECUTION DECREE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("EXECUTION DECREE CHARGES")));
        listMap.put("POSTAGE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("POSTAGE CHARGES")));
        listMap.put("ADVERTISE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ADVERTISE CHARGES")));
        listMap.put("LEGAL CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("LEGAL CHARGES")));
        listMap.put("INSURANCE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("INSURANCE CHARGES")));
        listMap.put("EP_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("EP_COST")));
        listMap.put("MISCELLANEOUS CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MISCELLANEOUS CHARGES")));
        if(totalLoanAmount.containsKey("RECOVERY CHARGES") && totalLoanAmount.get("RECOVERY CHARGES") != null){
            listMap.put("RECOVERY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("RECOVERY CHARGES")));
        }
        if(totalLoanAmount.containsKey("MEASUREMENT CHARGES") && totalLoanAmount.get("MEASUREMENT CHARGES") != null){
            listMap.put("MEASUREMENT CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MEASUREMENT CHARGES")));
        }
        
        if(totalLoanAmount.containsKey("KOLEFIELD EXPENSE") && totalLoanAmount.get("KOLEFIELD EXPENSE") != null){
            listMap.put("KOLEFIELD EXPENSE", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD EXPENSE")));
        }        
        if(totalLoanAmount.containsKey("KOLEFIELD OPERATION") && totalLoanAmount.get("KOLEFIELD OPERATION") != null){
            listMap.put("KOLEFIELD OPERATION", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD OPERATION")));
        }
        
        singleList.add(listMap);
        editableWaiveUI = new EditWaiveTableUI("ACT_CLOSING", listMap);
        editableWaiveUI.show();
//        TrueTransactMain.showScreen(editableUI);
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {
            // System.out.println("Coming here ...........");
            //If actionType such as NEW, EDIT, DELETE is not 0, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                //                if( getCommand() != null ){
                doActionPerform();
                //                }
                //                else{
                //                    final AccountClosingRB accountClosingRB = new AccountClosingRB();
                //                    throw new TTException(accountClosingRB.getString("TOCommandError"));
                //                }
            }
        } catch (Exception e) {
            // System.out.println("Error in doAction of A/c Closing OB....");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        try{
        final AccountClosingTO objAccountClosingTO = setAccountClosingData();
        objAccountClosingTO.setCommand(getCommand());
        objAccountClosingTO.setStatus(getAction());
        objAccountClosingTO.setStatusBy(TrueTransactMain.USER_ID);
        objAccountClosingTO.setStatusDt(currDt);
        final HashMap data = new HashMap();

        if (transactionDetailsTO == null) {
            transactionDetailsTO = new LinkedHashMap();
        }

        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0 && allowedTransactionDetailsTO.containsKey("1")) {
            TransactionTO objTransactionTO = (TransactionTO) allowedTransactionDetailsTO.get("1");
            if (prodType.equals("TermLoan")) {
                objTransactionTO.setScreenName("Loan Closing");
            } else {
                objTransactionTO.setScreenName("Account Closing");
            }
            allowedTransactionDetailsTO.put("1",objTransactionTO);
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        data.put("TransactionTO", transactionDetailsTO);

        data.put("accountclosing", objAccountClosingTO);

        //Added by nithya on 03-11-2021
        if(getLoanRenewalMap() != null && getLoanRenewalMap().containsKey("LOAN_OD_RENEWAL")){
            data.put("LOAN_RENEWAL_MAP",getLoanRenewalMap());
        }
        // End

        if (getChargelst() != null) {
            data.put("Charge List Data", getChargelst());
        }

        //DEPOSIT PREMATURE
        if (prematureString != null && prematureString.length() > 0) {
            data.put(prematureString, prematureString);
        }
        if (prodType.equals("TermLoan")) {
            data.put("BEHAVE_LIKE", getLoanBehaves());
//            if(waiveOffInterest){
//                totalLoanAmount.put("INTEREST_WAIVE_OFF" ,"Y");
//                totalLoanAmount.put("WAIVE_OFF_INTEREST" ,new Double(waiveOffEditInterestAmt()));
//        }
//            if(waiveoffPenal){
//                totalLoanAmount.put("PENAL_WAIVE_OFF" ,"Y");
////                totalLoanAmount.put("WAIVE_OFF_INTEREST" ,new Double(waiveOffEditInterestAmt()));
//            }else {
//                totalLoanAmount.put("PENAL_WAIVER", "N");
//                //totalLoanAmount.put("PENAL_INT", "0");
//            }
            //added by rishad 18/04/2014
            if (waiveoffPenal && penalWaiveAmount > 0) {
                totalLoanAmount.put("PENAL_WAIVE_OFF", "Y");
                totalLoanAmount.put("PENAL_WAIVE_AMT", penalWaiveAmount);

            } else {
                totalLoanAmount.put("PENAL_WAIVE_OFF", "N");
            }
            if (isInterestWaiveoff() && interestWaiveAmount > 0) {
                totalLoanAmount.put("WAIVE_OFF_INTEREST", "Y");
                totalLoanAmount.put("INTEREST_WAIVE_AMT", interestWaiveAmount);
            } else {
                totalLoanAmount.put("INTEREST_WAIVE_OFF", "N");
            }
            if (isNoticeWaiveoff() && noticeWaiveAmount > 0) {
                totalLoanAmount.put("NOTICE_WAIVE_OFF", "Y");
                totalLoanAmount.put("NOTICE_WAIVE_AMT", noticeWaiveAmount);
            } else {
                totalLoanAmount.put("NOTICE_WAIVE_OFF", "N");
            }
            if (isPrincipalwaiveoff() && principalWaiveAmount > 0) {
                totalLoanAmount.put("PRINCIPAL_WAIVE_OFF", "Y");
                totalLoanAmount.put("PRINCIPAL_WAIVE_AMT", principalWaiveAmount);
            } else {
                totalLoanAmount.put("PRINCIPAL_WAIVE_OFF", "N");
            }
            if (isArcWaiveOff() && arcWaiveAmount > 0) {
                totalLoanAmount.put("ARC_WAIVE_OFF", "Y");
                totalLoanAmount.put("ARC_WAIVE_AMT", arcWaiveAmount);
            } else {
                totalLoanAmount.put("ARC_WAIVE_OFF", "N");
            }
            if (isArbitraryWaiveOff()) {
                totalLoanAmount.put("ARBITRAY_WAIVE_OFF", "Y");
                totalLoanAmount.put("ARBITRAY_WAIVE_AMT", arbitarayWaivwAmount);
            } else {
                totalLoanAmount.put("ARBITRAY_WAIVE_OFF", "N");
            }
            if (isDecreeWaiveOff()) {
                totalLoanAmount.put("DECREE_WAIVE_OFF", "Y");
                totalLoanAmount.put("DECREE_WAIVE_AMT", decreeWaiveAmount);
            } else {
                totalLoanAmount.put("DECREE_WAIVE_OFF", "N");
            }
            if (isEpCostWaiveOff()) {
                totalLoanAmount.put("EP_WAIVE_OFF", "Y");
                totalLoanAmount.put("EP_WAIVE_AMT", epCostWaiveAmount);
            } else {
                totalLoanAmount.put("EP_WAIVE_OFF", "N");
            }
            if (isAdvertiseWaiveOff()) {
                totalLoanAmount.put("ADVERTISE_WAIVE_OFF", "Y");
                totalLoanAmount.put("ADVERTISE_WAIVE_AMT", advertiseWaiveAmount);
            } else {
                totalLoanAmount.put("ADVERTISE_WAIVE_OFF", "N");
            }
            if (isInsuranceWaiveOff()) {
                totalLoanAmount.put("INSURENCE_WAIVE_OFF", "Y");
                totalLoanAmount.put("INSURENCE_WAIVE_AMT", insuranceWaiveAmont);
            } else {
                totalLoanAmount.put("INSURENCE_WAIVE_OFF", "N");
            }
            if (isLegalWaiveOff()) {
                totalLoanAmount.put("LEGAL_WAIVE_OFF", "Y");
                totalLoanAmount.put("LEGAL_WAIVE_AMT", legalWaiveAmount);
            } else {
                totalLoanAmount.put("LEGAL_WAIVE_OFF", "N");
            }
            if (isMiscellaneousWaiveOff()) {
                totalLoanAmount.put("MISCELLANEOUS_WAIVE_OFF", "Y");
                totalLoanAmount.put("MISCELLANEOUS_WAIVE_AMT", miscellaneousWaiveAmount);
            } else {
                totalLoanAmount.put("MISCELLANEOUS_WAIVE_OFF", "N");
            }
            if (isPostageWaiveOff()) {
                totalLoanAmount.put("POSTAGE_WAIVE_OFF", "Y");
                totalLoanAmount.put("POSTAGE_WAIVE_AMT", postageWaiveAmount);
            } else {
                totalLoanAmount.put("POSTAGE_WAIVE_OFF", "N");
            }
            
            if (isRecoveryWaiveOff()) {
                totalLoanAmount.put("RECOVERY_WAIVE_OFF", "Y");
                totalLoanAmount.put("RECOVERY_WAIVE_AMT", recoveryWaiveAmount);
            } else {
                totalLoanAmount.put("RECOVERY_WAIVE_OFF", "N");
            }
            
            if (isMeasurementWaiveOff()) {
                totalLoanAmount.put("MEASUREMENT_WAIVE_OFF", "Y");
                totalLoanAmount.put("MEASUREMENT_WAIVE_AMT", measurementWaiveAmount);
            } else {
                totalLoanAmount.put("MEASUREMENT_WAIVE_OFF", "N");
            }
            
                        //Kole field operation expense
            if (isKoleFieldExpenseWaiveOff()) {
                totalLoanAmount.put("KOLE_FIELD_EXPENSE_WAIVE_OFF", "Y");
                totalLoanAmount.put("KOLE_FIELD_EXPENSE_WAIVE_AMT", koleFieldExpenseWaiveAmount);
            } else {
                totalLoanAmount.put("KOLE_FIELD_EXPENSE_WAIVE_OFF", "N");
            }
            
           if (isKoleFieldOperationWaiveOff()) {
                totalLoanAmount.put("KOLE_FIELD_OPERATION_WAIVE_OFF", "Y");
                totalLoanAmount.put("KOLE_FIELD_OPERATION_WAIVE_AMT", koleFieldOperationWaiveAmount);
            } else {
                totalLoanAmount.put("KOLE_FIELD_OPERATION_WAIVE_OFF", "N");
            }
            
            //End            
            
            
            if (isRebateInterestflag()) {
                totalLoanAmount.put("REBATE_MODE", getRebate_mode());
                if (getRebate_mode().equals("Cash")) {
                totalLoanAmount.put("REBATE_INTEREST_WAIVE_OFF", "Y");
                }
            } else {
                totalLoanAmount.put("REBATE_INTEREST_WAIVE_OFF", "N");
            }
            if(serviceTax_Map!=null && serviceTax_Map.size()>0){
                 totalLoanAmount.put("TOT_SER_TAX_AMT", serviceTax_Map.get("TOT_TAX_AMT"));
                 totalLoanAmount.put("SER_TAX_HEAD", serviceTax_Map.get("TAX_HEAD_ID"));
                 totalLoanAmount.put("SER_TAX_MAP", serviceTax_Map);
            }
            data.put("TOTAL_AMOUNT", getTotalLoanAmount());
            data.put("PROD_TYPE", prodType);
            data.put("PROD_ID", getCboProductID());
            if(getLoanBehaves().equals("OD")){  // Added by nithya on 06-11-2019 for KD-681	penal intrest calculation neeeded for od closing in loan closing screen  
                setAsAnWhen("Y");
            }
            data.put("AS_CUSTOMER_COMES", getAsAnWhen());
            if (advancesCreditInterest > 0)//babuv
            {
                data.put("ADVANCES_CREDIT_INT", new Double(advancesCreditInterest));
            }
            //            if(loanBehaves !=null && loanBehaves.equals("LOANS_AGAINST_DEPOSITS"))
            //		lienCancel(data);
            data.put("BAL_DR_CR", getBalCrDR());
            if(totalLoanAmount.containsKey("OD_CREDIT_BALANCE") && totalLoanAmount.get("OD_CREDIT_BALANCE") != null && CommonUtil.convertObjToStr(totalLoanAmount.get("OD_CREDIT_BALANCE")).equalsIgnoreCase("OD_CREDIT_BALANCE")){ // Added by nithya on 15-04-2020 for KD 889
                data.put("OD_CREDIT_BALANCE","OD_CREDIT_BALANCE");
            }
            
            if (isAuctnAct()) {
                data.put("AUCTN_ACT", "AUCTN_ACT");
                data.put("AUCTN_AMT", getAuctnAmt());
                data.put("AUCTN_BALNC_AMT", getAuctnBalAmt());
                data.put("AUCTION_TYPE", getAuctnType());
                data.put("SUS_PROD_ID", getSusprodId());
                data.put("SUS_ACT_NUM", getSusActnum());
                data.put("DR_AC_HEAD",getTxtDrAccHead());
                data.put("RETURN_MAP",isCloseLoanThroughGoldAuction());
            }
             
        }
        //Added by chithra for service tax
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                data.put("serviceTaxDetails", getServiceTax_Map());
                data.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
        } 
        data.put("LIEN", new Double(getLienAmount())); //Get this from the common screen
        data.put("FREEZE", new Double(getFreezeAmount())); //Get this from the common screen
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap()); //For Authorization added 28 Apr 2005
        data.put("MODE", getCommand());
        if (oldTransDetMap != null) {
            if (oldTransDetMap.size() > 0) {
                if (oldTransDetMap.containsKey("ACT_CLOSE_CHRG_TRANS_DETAILS")) {
                    data.put("ACT_CLOSE_CHRG_TRANS_DETAILS", oldTransDetMap.get("ACT_CLOSE_CHRG_TRANS_DETAILS"));
                }
                if (oldTransDetMap.containsKey("CHRG_DETAILS_TRANS_DETAILS")) {
                    data.put("CHRG_DETAILS_TRANS_DETAILS", oldTransDetMap.get("CHRG_DETAILS_TRANS_DETAILS"));
                }
                if (oldTransDetMap.containsKey("CASH_TRANS_DETAILS")) {
                    data.put("CASH_TRANS_DETAILS", oldTransDetMap.get("CASH_TRANS_DETAILS"));
                }
                if (oldTransDetMap.containsKey("TRANSFER_TRANS_DETAILS")) {
                    data.put("TRANSFER_TRANS_DETAILS", oldTransDetMap.get("TRANSFER_TRANS_DETAILS"));
                }
            }
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        data.put("NEWACCOUNT_NUM",nxtAccNo);
        data.put("SUSPANCE_PRODID",getSusprodId());
        data.put("stMap", getCalMap());
         if (!prodType.equals("TermLoan")) {
            if(getAuthorizeBy2()!=null  && getAuthorizeBy2().length()>0){
                  data.put("AUTHORIZE_BY_2", getAuthorizeBy2())  ;
            }
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        oldTransDetMap = null;
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        //        if(actionType !=8)
        //        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        //        resetForm();
        }
        catch(Exception e)
        {
       e.printStackTrace();
       throw e;
        }
    }
    //    private void lienCancel(HashMap lienCancel){
    //        DepositLienUI depositLien=new DepositLienUI();
    //        depositLien.fillData(lienCancel);
    //
    //    }

//    private double  waiveOffEditInterestAmt(){
//        double editWaiveOffTransAmt=0;
//        ArrayList singleList =new ArrayList();
//        if(editableUI !=null){
//         ArrayList list =editableUI.getTableData();
//         for(int i=0;i<list.size();i++){
//             singleList=(ArrayList)list.get(i);
//             editWaiveOffTransAmt+=CommonUtil.convertObjToDouble(singleList.get(4)).doubleValue();
//         }}
//        
//         return editWaiveOffTransAmt;
//    }
    protected void updateNEwAccountNo() {
        boolean isUpdated = false;
        HashMap where = new HashMap();
        int dif = 0;
        where.put("PROD_ID", getSusprodId());
        where.put("PRODUCT_ID", getSusprodId());
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        List lsts = (List) ClientUtil.executeQuery("getSelectNextAccNo", where);
        String strPrefix = "";
        int len = 13;
        String genID = null;
        if (lsts != null && lsts.size() > 0) {
        	nxtAccNo = 0;
            String accountClosingNo = CommonUtil.convertObjToStr((lsts.get(0)));
            accountClosingNo = accountClosingNo.substring(8, accountClosingNo.length());
            int nxtIDs = CommonUtil.convertObjToInt(accountClosingNo)+1;
            String nxtID = CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(accountClosingNo));
            dif = nxtIDs - CommonUtil.convertObjToInt(nxtID);
            nxtAccNo = CommonUtil.convertObjToInt(nxtIDs);            
            where.put("VALUE", CommonUtil.convertObjToStr(nxtIDs));
          //  ClientUtil.execute("updateCoreBankNextActNum", where);
        }
    }
    
    protected HashMap isCloseLoanThroughGoldAuction(){
        HashMap returnMap = new HashMap();
        try{
        String auctionType = CommonUtil.convertObjToStr(getAuctnType());
        String isLoanCloseThroughGldLnAlwd = "";
        String loanAcctNo = CommonUtil.convertObjToStr(getTxtAccountNumber());
        HashMap loanClosemap = new HashMap();
        HashMap crDrHeadmap = new HashMap();
        HashMap susHeadmap = new HashMap();
        HashMap susCrAcctNomap = new HashMap();
        String suspenseAcHd = "",suspenseAcctNo = "";
        String suspenseProdId = "",suspenseDesc = "";
        String suspensePrefix = "";
        String custName="",custId="",strMemNo="";
        if(getCboProductID() != null && !getCboProductID().equals("")){
        loanClosemap.put("prodId",getCboProductID());
        List crProdList = ClientUtil.executeQuery("getSuspenseCreditDebitProductId", loanClosemap);
        List lst  = ClientUtil.executeQuery("getInterestOnMaturity", loanClosemap);
        loanClosemap.put("ACC_NUM", loanAcctNo);
        List customerList = ClientUtil.executeQuery("getAccountNumberNameAD", loanClosemap);
        if(customerList != null && customerList.size() > 0){
            HashMap custMap = (HashMap)customerList.get(0);
            custName = CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME"));
            custId = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
            strMemNo=CommonUtil.convertObjToStr(custMap.get("MEMBERSHIP_NO"));
        }
            if (lst != null && lst.size() > 0) {
                loanClosemap = (HashMap) lst.get(0);
                if (loanClosemap.containsKey("IS_LOANCLOSING_ALLOWED") && loanClosemap.get("IS_LOANCLOSING_ALLOWED") != null) {
                    isLoanCloseThroughGldLnAlwd = CommonUtil.convertObjToStr(loanClosemap.get("IS_LOANCLOSING_ALLOWED"));
                } else {
                    isLoanCloseThroughGldLnAlwd = "";
                }
                if (isAuctnAct()) {
                    if (crProdList != null && crProdList.size() > 0) {
                        crDrHeadmap = (HashMap) crProdList.get(0);
                        if ((crDrHeadmap.containsKey("SUSPENSE_CREDIT_PROD_ID") && crDrHeadmap.get("SUSPENSE_CREDIT_PROD_ID") != null) ||
                            (crDrHeadmap.containsKey("SUSPENSE_DEBIT_PROD_ID") && crDrHeadmap.get("SUSPENSE_DEBIT_PROD_ID") != null)    
                                ) {
                            if (auctionType != null && !auctionType.equals("") && auctionType.equalsIgnoreCase("Cr")) {
                            suspenseProdId = CommonUtil.convertObjToStr(crDrHeadmap.get("SUSPENSE_CREDIT_PROD_ID"));
                            }else{
                            suspenseProdId = CommonUtil.convertObjToStr(crDrHeadmap.get("SUSPENSE_DEBIT_PROD_ID"));
                            }
                            if (auctionType != null && !auctionType.equals("") && auctionType.equalsIgnoreCase("Cr") || auctionType.equalsIgnoreCase("Dr")) {
                                susCrAcctNomap.put("PROD_ID", suspenseProdId);
                                susHeadmap.put("prodId", suspenseProdId);
                                susHeadmap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                                susHeadmap.put("PRODUCT_ID", suspenseProdId);
                            }
                            List susAcHdList = ClientUtil.executeQuery("getSelectSuspenseProductTO", susCrAcctNomap);
                            if (susAcHdList != null && susAcHdList.size() > 0) {
                                SuspenseAccountProductTO suspenseProdTo = (SuspenseAccountProductTO) susAcHdList.get(0);
                                if (suspenseProdTo.getTxtSuspenseProductHead() != null && !suspenseProdTo.getTxtSuspenseProductHead().equals("")) {
                                    suspenseAcHd = CommonUtil.convertObjToStr(suspenseProdTo.getTxtSuspenseProductHead());
                                    suspenseDesc = CommonUtil.convertObjToStr(suspenseProdTo.getTxtSuspenseProdName());
                                    suspensePrefix = CommonUtil.convertObjToStr(suspenseProdTo.getTxtPrefix());
                                }
                                List susAccnoList = ClientUtil.executeQuery("getSelectNextAccNo", susHeadmap);

                                if (susAccnoList != null && susAccnoList.size() > 0) {
                                    String accountClosingNo = CommonUtil.convertObjToStr((susAccnoList.get(0)));
                                    if (!accountClosingNo.equals("") && accountClosingNo != null) {
                                        suspenseAcctNo = CommonUtil.convertObjToStr(accountClosingNo);
                                        setSusActnum(suspenseAcctNo);
                                        setTxtDrAccHead(suspenseAcHd);
                                        setSusprodId(suspenseProdId);
                                        SuspenseAccountMasterTO suspenseAcctMaserTo = new SuspenseAccountMasterTO();
                                        suspenseAcctMaserTo.setCboSuspenseProdID(suspenseDesc);
                                        suspenseAcctMaserTo.setTxtSuspenseActNum(suspenseAcctNo);
                                        suspenseAcctMaserTo.setTxtSuspenseProdDescription(suspenseProdId);
                                        suspenseAcctMaserTo.setTxtCustomerId(custId);
                                        suspenseAcctMaserTo.setTxtName(custName);
                                        suspenseAcctMaserTo.setTxtMemberNumber(strMemNo);
                                        suspenseAcctMaserTo.setTxtPrefix(suspensePrefix);
                                        suspenseAcctMaserTo.setStatusBy(TrueTransactMain.USER_ID);
                                        suspenseAcctMaserTo.setTdtSuspenseOpenDate(currDt);
                                        suspenseAcctMaserTo.setStatusDt(currDt);
                                        suspenseAcctMaserTo.setAuthorizeStatus("AUTHORIZED");
                                        suspenseAcctMaserTo.setStatus("CREATED");
                                        suspenseAcctMaserTo.setAuthorizeBy(TrueTransactMain.USER_ID);
                                        suspenseAcctMaserTo.setAuthorizeDt(currDt);
                                        suspenseAcctMaserTo.setCboAgentID("0000");
                                        suspenseAcctMaserTo.setTxtAccRefNo(CommonUtil.convertObjToStr(getTxtAccountNumber()));
                                        suspenseAcctMaserTo.setIsAuction("Y");
                                        suspenseAcctMaserTo.setBranchCode(TrueTransactMain.BRANCH_ID);
                                        returnMap.clear();
                                        returnMap.put("SuspenseAccountMasterTO", suspenseAcctMaserTo);
                                        returnMap.put("suspenseAcHd", suspenseAcHd);
                                        returnMap.put("suspenseAcctNo", suspenseAcctNo);
                                        returnMap.put("SUS_PROD_ID", CommonUtil.convertObjToStr(suspenseProdTo.getTxtSuspenseProdID()));
                                        returnMap.put("isLoanCloseThroughGldLnAlwd", isLoanCloseThroughGldLnAlwd);
                                        returnMap.put("AUCTN_ACT", "AUCTN_ACT");
                                        returnMap.put("AUCTN_AMT", getAuctnAmt());
                                        returnMap.put("AUCTN_BALNC_AMT", getAuctnBalAmt());
                                        returnMap.put("AUCTION_TYPE", getAuctnType());
                                        
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }   
        
        }
        catch(Exception ex){
             parseException.logException(ex, true);
            ex.printStackTrace();
        }
        return returnMap;
        }
    private String getCommand() {
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = AUTHORIZE;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    private AccountClosingTO setAccountClosingData() throws Exception {
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
        objAccountClosingTO.setActNum(getTxtAccountNumber());
        objAccountClosingTO.setUnusedChk(CommonUtil.convertObjToDouble(this.getTxtNoOfUnusedChequeLeafs()));
        objAccountClosingTO.setActClosingChrg(CommonUtil.convertObjToDouble(this.getTxtAccountClosingCharges()));
        objAccountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(this.getTxtInterestPayable()));
        objAccountClosingTO.setChrgDetails(CommonUtil.convertObjToDouble(this.getTxtChargeDetails()));
        objAccountClosingTO.setPayableBal(CommonUtil.convertObjToDouble(this.getTxtPayableBalance()));
        objAccountClosingTO.setInsuranceCharges(CommonUtil.convertObjToDouble(this.getTxtInsuranceCharges()));
        if (advancesCreditInterest > 0) {
            objAccountClosingTO.setCreditIntAD(new Double(advancesCreditInterest));
        } else {
            objAccountClosingTO.setCreditIntAD(new Double(0));
        }
        if (subsidyEditTransAmt > 0) {
            objAccountClosingTO.setSusbsidyAmt(new Double(subsidyEditTransAmt));
        } else {
            objAccountClosingTO.setSusbsidyAmt(new Double(0));
        }

        if (rebateEditTransAmt > 0) {
            objAccountClosingTO.setRebateInterest(new Double(rebateEditTransAmt));
        } else {
            objAccountClosingTO.setRebateInterest(new Double(0));
        }
        
        objAccountClosingTO.setPrincipalWaiveAmt(getPrincipalWaiveAmount());

        objAccountClosingTO.setProdId(getCboProductID());
        objAccountClosingTO.setVariableNo(VarNum);
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_ID", getCboProductID());
        String status = "";
        if (!prodType.equals("TermLoan")) {
            status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getSRemarks();
        }
        if (status.equals("NRO")) {
            objAccountClosingTO.setTaxPayable(new Double(taxAmount(Double.parseDouble(this.getTxtInterestPayable()))));
        }
        VarNum = "";
        return objAccountClosingTO;
    }

    public AccountClosingTO setAccountClosingLTDData() {//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();//last line continution for the purpose of LTD deposits.
        objAccountClosingTO.setActNum(getTxtAccountNumber());
        objAccountClosingTO.setUnusedChk(CommonUtil.convertObjToDouble(this.getTxtNoOfUnusedChequeLeafs()));
        objAccountClosingTO.setActClosingChrg(CommonUtil.convertObjToDouble(this.getTxtAccountClosingCharges()));
        objAccountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(this.getTxtInterestPayable()));
        objAccountClosingTO.setChrgDetails(CommonUtil.convertObjToDouble(this.getTxtChargeDetails()));
        objAccountClosingTO.setPayableBal(CommonUtil.convertObjToDouble(this.getTxtPayableBalance()));
        return objAccountClosingTO;
    }

    public String getCommandForLtd() {//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        String command = null;//last line continution for the purpose of LTD deposits.
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = AUTHORIZE;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }

    public String getActionForLtd() {//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        String action = null;//last line continution for the purpose of LTD deposits.
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            // System.out.println("Ob.getActionType() : " + getActionType());
            // System.out.println("whereMap in getData of OB : " + whereMap);
            // System.out.println("map in getData of OB : " + map);
            whereMap.put("MODE", getCommand());
            if (prodType.equals("TermLoan")) {
                whereMap.put("PROD_TYPE", prodType);
            }
            map.put("DB_DRIVER_NAME",ProxyParameters.dbDriverName);// Added by nithya on 21-02-2019 for KD 393 - Sb closing issue (interest pay at daily product)
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            populateAccountClosingData(data);           
            List list = (List) data.get("TransactionTO");
            transactionOB.setDetails(list);
            allowedTransactionDetailsTO = transactionOB.getAllowedTransactionDetailsTO();
            //ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }
    
    // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
    private void populateStockPhoto(HashMap mapData) throws Exception {
        if (mapData.containsKey("STOCK_PHOTO_FILE") && mapData.get("STOCK_PHOTO_FILE") != null) {
            HashMap photoMap = (HashMap) mapData.get("STOCK_PHOTO_FILE");
            if (photoMap.containsKey("PHOTO") && photoMap.get("PHOTO") != null) {
                setPhotoByteArray((byte[]) photoMap.get("PHOTO"));
            }
        }
    }

    /**
     * To populate data into the screen
     */
    private void populateAccountClosingData(HashMap data) throws Exception {
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
        HashMap accTo = (HashMap) data.get("AccountDetailsTO");
        //        HashMap accountInterest = (HashMap)data.get("AccountInterest");
        setStatusBy(CommonUtil.convertObjToStr(accTo.get("STATUS_BY")));
        setAuthorizeStatus(CommonUtil.convertObjToStr(accTo.get("AUTHORIZE_STATUS")));
        setCboProductID(CommonUtil.convertObjToStr(accTo.get("PROD_ID")));
        setCustomerName(CommonUtil.convertObjToStr(accTo.get("CUSTOMER NAME")));
        setCustomerStreet(CommonUtil.convertObjToStr(accTo.get("HOUSE_NAME")));
        setAccountHeadId(CommonUtil.convertObjToStr(accTo.get("AC_HD_ID")));
        setTxtInsuranceCharges(CommonUtil.convertObjToStr(accTo.get("INSURANCE_CHARGE")));

        if (prodType.equals("TermLoan")) {
//            setAvailableBalance(CommonUtil.convertObjToStr(accTo.get("LOAN_BALANCE_PRINCIPAL")));
            setAvailableBalance(CommonUtil.convertObjToStr(showTransactionTLBalance(data)));

//            CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_PRINCEPLE")).doubleValue()
            setTxtInterestPayable(CommonUtil.convertObjToStr(accTo.get("INT_PAYABLE")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(accTo.get("ACCT_NUM")));
        } else {
            setAvailableBalance(CommonUtil.convertObjToStr(accTo.get("AVAILABLE_BALANCE")));
            //            setTxtInterestPayable(CommonUtil.convertObjToStr(accountInterest.get(getTxtAccountNumber())));
            setTxtInterestPayable(CommonUtil.convertObjToStr(accTo.get("INT_PAYABLE")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(accTo.get("ACT_NUM")));
        }
        setTxtAccountClosingCharges(CommonUtil.convertObjToStr(accTo.get("ACT_CLOSING_CHRG")));

        int returndCheques = 0;
        returndCheques = getUnPaidCheques(getTxtAccountNumber());
        setTxtNoOfUnusedChequeLeafs(String.valueOf(returndCheques));

        setTxtChargeDetails(CommonUtil.convertObjToStr(accTo.get("CHRG_DETAILS")));
        setTxtPayableBalance(CommonUtil.convertObjToStr(accTo.get("PAYABLE_BAL")));

        advancesCreditInterest = CommonUtil.convertObjToDouble(accTo.get("AD_CREDIT_INT_AMT")).doubleValue();
        if (advancesCreditInterest > 0) {
            double loanPrincipal = CommonUtil.convertObjToDouble(accTo.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
            loanPrincipal += -advancesCreditInterest;
            setAvailableBalance(String.valueOf(loanPrincipal));
        }

        subsidyEditTransAmt = CommonUtil.convertObjToDouble(accTo.get("SUBSIDY_AMT")).doubleValue();
        if (subsidyEditTransAmt > 0) {
            double loanPrincipal = CommonUtil.convertObjToDouble(accTo.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
//            loanPrincipal+=-subsidyEditTransAmt;
            setAvilableLoanSubsidy(subsidyEditTransAmt);
        }

        rebateEditTransAmt = CommonUtil.convertObjToDouble(accTo.get("REBATE_AMT")).doubleValue();
        if (rebateEditTransAmt > 0) {
            setRebateInterest(rebateEditTransAmt);
        }
        
        if(accTo.containsKey("PRINCIPAL_WAIVE_AMT") && accTo.get("PRINCIPAL_WAIVE_AMT") != null){
            setPrincipalWaiveAmount(CommonUtil.convertObjToDouble(accTo.get("PRINCIPAL_WAIVE_AMT")));
        }
        
        oldTransDetMap = new HashMap();
        if (data.containsKey("ACT_CLOSE_CHRG_TRANS_DETAILS")) {
            oldTransDetMap.put("ACT_CLOSE_CHRG_TRANS_DETAILS", data.get("ACT_CLOSE_CHRG_TRANS_DETAILS"));
        }
        if (data.containsKey("CHRG_DETAILS_TRANS_DETAILS")) {
            oldTransDetMap.put("CHRG_DETAILS_TRANS_DETAILS", data.get("CHRG_DETAILS_TRANS_DETAILS"));
        }
        if (data.containsKey("CASH_TRANS_DETAILS")) {
            oldTransDetMap.put("CASH_TRANS_DETAILS", data.get("CASH_TRANS_DETAILS"));
        }
        if (data.containsKey("TRANSFER_TRANS_DETAILS")) {
            oldTransDetMap.put("TRANSFER_TRANS_DETAILS", data.get("TRANSFER_TRANS_DETAILS"));
        }
        if (data.containsKey("ADVANCES_CREDIT_INT_TRANS_DETAILS")) {
            oldTransDetMap.put("ADVANCES_CREDIT_INT_TRANS_DETAILS", data.get("ADVANCES_CREDIT_INT_TRANS_DETAILS"));
        }

        if (data.containsKey("VARIABLE_NO")) {
            VarNum = CommonUtil.convertObjToStr(data.get("VARIABLE_NO"));
        }
        showTransactionDetails(data);
//        showGoldLoanParicularesDetails(data);
        accTo = null;
    }

    private void showTransactionDetails(HashMap map) {
        tableDialogUI = null;
        if (prodType.equals("TermLoan")) {
            map.put("ACCT_NUM", ((HashMap) map.get("AccountDetailsTO")).get("ACT_NUM"));

            //CALENDER FREQUENCY CUSTOMER ONLY AD
//         if(((map !=null && map.containsKey("ADVANCES_CREDIT_INT_TRANS_DETAILS") && map.get("ADVANCES_CREDIT_INT_TRANS_DETAILS")!=null) || advancesCreditInterest>0)
//              || ((map !=null && map.containsKey("AVAILABLE_SUBSIDY_TRANS_AMT") && map.get("AVAILABLE_SUBSIDY_TRANS_AMT")!=null)
//              || subsidyEditTransAmt>0)){
            HashMap selectMap = new HashMap();
            selectMap.put("LINK_BATCH_ID", ((HashMap) map.get("AccountDetailsTO")).get("ACCT_NUM"));
            selectMap.put("TODAY_DT", currDt.clone());
            selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            tableDialogUI = new TableDialogUI("getAllTransactionViewAD", selectMap, "");
//        }
            //END 
//         else
//        if(map !=null && map.containsKey("CASH_TRANS_DETAILS") && map.get("CASH_TRANS_DETAILS")!=null){
//            HashMap selectMap=new HashMap();
//            selectMap.put("ACCT_NUM",((HashMap)map.get("AccountDetailsTO")).get("ACCT_NUM"));
//            selectMap.put("TRANS_ID",((HashMap)map.get("CASH_TRANS_DETAILS")).get("TRANS_ID"));
//            selectMap.put("TRANS_DT",(currDt.clone()));
//            selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
//            tableDialogUI= new TableDialogUI("getCashTransactionTOForAuthorzationLinkBatch",selectMap,"");
//        }
//        else
//        if(map !=null && map.containsKey("TRANSFER_TRANS_DETAILS") && map.get("TRANSFER_TRANS_DETAILS")!=null){
//            HashMap selectMap=new HashMap();
//            selectMap.put("ACCT_NUM",((HashMap)map.get("AccountDetailsTO")).get("ACCT_NUM"));
//            selectMap.put("BATCH_ID",((HashMap)map.get("TRANSFER_TRANS_DETAILS")).get("BATCH_ID"));
////            selectMap.put("TODAY_DT",currDt.clone());
//            selectMap.put("TRANS_DT",currDt.clone());
//            selectMap.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
//            tableDialogUI= new TableDialogUI("getTransferTransBatchTOForAuthorzationLinkBatch",selectMap,"");
//        }
            if (tableDialogUI != null) {
                tableDialogUI.setTitle("Loan/Advances Transaction Details");
            }
        }
    }

    private double showTransactionTLBalance(HashMap map) {
        tableDialogUI = null;
        HashMap dataMap = new HashMap();
        double princpalAmt = 0;
        if (prodType.equals("TermLoan")) {
            map.put("ACCT_NUM", ((HashMap) map.get("AccountDetailsTO")).get("ACT_NUM"));
            HashMap selectMap = new HashMap();
            selectMap.put("LINK_BATCH_ID", ((HashMap) map.get("AccountDetailsTO")).get("ACCT_NUM"));
            selectMap.put("TODAY_DT", currDt.clone());
            selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            selectMap.put("INST_TYPE", "INST_TYPE");// Added by nithya on 26-05-2020 for KD-1665
            List lst = ClientUtil.executeQuery("getAllTransactionViewAD", selectMap);
            if (lst != null && lst.size() > 0) {

                for (int i = 0; i < lst.size(); i++) {
                    dataMap = (HashMap) lst.get(i);
                    if (dataMap.get("PROD_TYPE").equals("TL") || dataMap.get("PROD_TYPE").equals("AD")) {
                        princpalAmt = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                        break;
                    }
                }


            }

            //        }

        }
        return princpalAmt;
    }

    public void showTransaction() {
        if (tableDialogUI != null) {
            tableDialogUI.show();
        }
    }

    public HashMap asAnWhenCustomerComesYesNO(String acct_no) {
        HashMap map = new HashMap();
        map.put("ACT_NUM", acct_no);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        if (lst == null || lst.isEmpty()) {
            lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            setLinkMap(map);
            setAsAnWhen(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));             
            if(getLoanBehaves().equals("OD")){      // Added by nithya on 06-11-2019 for KD-681	penal intrest calculation neeeded for od closing in loan closing screen 
                setAsAnWhen("Y");
                map.put("AS_CUSTOMER_COMES","Y");
            }
        }
        return map;
    }
    protected HashMap getInterestOnMaturityMap() {
        HashMap maturityMap = new HashMap();
        //        prodType="TermLoan";
        //system.out.println("prodTypeprodType in accountViewMap"+prodType);
        HashMap whereMap = new HashMap();
        log.info("productID--- : " + getLoanBehaves() +""+prodType +"  loanBehaves  "+loanBehaves);
        whereMap.put("prodId", getCboProductID());
        List InterestOnMaturityList = ClientUtil.executeQuery("getInterestOnMaturity", whereMap);
        if (InterestOnMaturityList != null && InterestOnMaturityList.size() > 0) {
            maturityMap = (HashMap) InterestOnMaturityList.get(0);
    }
        return maturityMap;
    }
    public void calculateInterestOnMaturity() {
        String calcOnMaturity = "";
        productRecord = new HashMap();
        calcOn_Maturity = "";
        if (prodType.equals("TermLoan")) {
            HashMap intMaturityMap = getInterestOnMaturityMap();
            if (intMaturityMap.containsKey("INTEREST_ON_MATURITY")) {
                if (intMaturityMap != null && intMaturityMap.get("INTEREST_ON_MATURITY") != null) {
                    calcOnMaturity = CommonUtil.convertObjToStr(intMaturityMap.get("INTEREST_ON_MATURITY"));
                    calcOn_Maturity = CommonUtil.convertObjToStr(intMaturityMap.get("INTEREST_ON_MATURITY"));
                }
            }
        }
        maturityMap = new HashMap();
        if (calcOnMaturity != null && !calcOnMaturity.equals("") && calcOnMaturity.equals("Y")) {
            //commented by rishad 28/feb/2017 for mantis 	0009214: Group Deposit Loan Closing - Needed to take interest upto Group Deposit Maturity Date.
            // if (getLoanBehaves().equals("LOANS_AGAINST_DEPOSITS")) {
            int matRes = ClientUtil.confirmationAlert("Calculate Interest On Maturity Date");
            if (matRes == 0) {
                maturityMap.put("CALC_ON_MATURITY", "Y");
            } else {
                maturityMap.put("CALC_ON_MATURITY", "N");
            }
            // }
        }
    }

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        if (calcOn_Maturity != null && !calcOn_Maturity.equals("") && calcOn_Maturity.equals("Y")) {
            //commented by rishad 28/feb/2017 for mantis 	0009214: Group Deposit Loan Closing - Needed to take interest upto Group Deposit Maturity Date.
            // if (getLoanBehaves().equals("LOANS_AGAINST_DEPOSITS")) {
            HashMap where = new HashMap();
            where.put("ACCT_NUM", getTxtAccountNumber());
            List closeDatelist = ClientUtil.executeQuery("getLoanCloseDate", where);
            if (closeDatelist != null && closeDatelist.size() > 0) {
                HashMap each = (HashMap) closeDatelist.get(0);
                if (each != null && each.containsKey("TO_DT") && each.get("TO_DT") != null) {
                    Date todate = (Date) each.get("TO_DT");
//                        Date currDt = currDt.clone();
                    if (DateUtil.dateDiff(todate, currDt) < 0 && maturityMap.containsKey("CALC_ON_MATURITY")) {
                        maturityMap.put("CALC_ON_MATURITY", "N");
                    }
                }
            }
            // }
        }
        whereMap.put("MATURITY_MAP", maturityMap);
        try {//dont delete this methode check select dao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapData;
    }

    public void resetForm() {
        setCboProductID((String) cbmProductID.getElementAt(0));
        setAccountHeadId("");
        setAccountHeadDesc("");
        setTxtAccountNumber("");
        setCustomerName("");
        setCustomerStreet("");
        setTxtNoOfUnusedChequeLeafs("");
        setAvailableBalance("");
        setTxtInterestPayable("");
        setTxtAccountClosingCharges("");
        setTxtChargeDetails("");
        setTxtPayableBalance("");
        setLoanInt("");
        setTxtInsuranceCharges("");
        setOldTransDetMap(null);
        waiveOffInterest = false;
        setBalCrDR("");
        setChanged();
        if (chargelst != null) {
            chargelst.clear();
            chargelst = null;
        }
        setAuctnAct(false);
        setAuctnAmt(0.0);
        setAuctnBalAmt(0.0);
        setAuctnType("Cr");
        setSusprodId("");
        setSusActnum("");
        setTxtDrAccHead("");
        setLblServiceTaxval("");
        setServiceTax_Map(null);
        setStartChequeNo(""); 
        ttNotifyObservers();
        setPenalWaiveAmount(0);
        setInterestWaiveAmount(0);
        setPrincipalWaiveAmount(0);
        setNoticeWaiveAmount(0);
        setArbitarayWaivwAmount(0);
        setArcWaiveAmount(0);
        setAdvertiseWaiveAmount(0);
        setInsuranceWaiveAmont(0);
        setLegalWaiveAmount(0);
        setMiscellaneousWaiveAmount(0);
        setDecreeWaiveAmount(0);
        setEpCostWaiveAmount(0);
        setPostageWaiveAmount(0);
        setRecoveryWaiveAmount(0.0);
        setMeasurementWaiveAmount(0.0);
        setRecoveryWaiveOff(false);
        setMeasurementWaiveOff(false);
        setWaiveoffPenal(false);
        setWaiveOffInterest(false);
        setPrincipalwaiveoff(false);
        setNoticeWaiveoff(false);
        setArbitraryWaiveOff(false);
        setArcWaiveOff(false);
        setAdvertiseWaiveOff(false);
        setLegalWaiveOff(false);
        setInsuranceWaiveOff(false);
        setDecreeWaiveOff(false);
        setInsuranceWaiveOff(false);
        setPostageWaiveOff(false);
        setAuthorizeBy2("");
        setMiscellaneousWaiveOff(false);
        setPhotoByteArray(null); // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        totalLoanAmount = null; // Added by nithya on 10-03-2020 for KD-1422
        setLoanRenewalMap(null); // 03-11-2021
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void updateAuthorizeStatus() {
        HashMap hash = null;
        String status = null;
        // System.out.println("Records'll be updated... ");
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            status = CommonConstants.STATUS_AUTHORIZED;
        } else if (getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            status = CommonConstants.STATUS_REJECTED;
        } else if (getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            status = CommonConstants.STATUS_EXCEPTION;
        }

        hash = new HashMap();
        hash.put("ACCOUNTNO", txtAccountNumber);
        hash.put("STATUS", status);
        hash.put("USER_ID", TrueTransactMain.USER_ID);
        hash.put("ACCOUNT_STATUS", "CLOSED");
        ClientUtil.execute("authorizeUpdateAccountCloseTO", hash);
        ClientUtil.execute("authorizeAcctStatus", hash);
        ClientUtil.execute("authorizeAcctStatusTL", hash);
        // System.out.println("Record Updated : " +hash);

    }
    public LinkedHashMap getUnPaidChequeDetails(String acctNo) {
        LinkedHashMap cheqdetMap = new LinkedHashMap();
        HashMap chequesMap = new HashMap();
        int fromNo = 0, toNo = 0, used = 0;
        chequesMap.put("ACCTNO", acctNo);
        List resultList = ClientUtil.executeQuery("getChequeIssueDetails", chequesMap);
        if (resultList != null && resultList.size() > 0) {
            final HashMap resultMap = (HashMap) resultList.get(0);
            fromNo = CommonUtil.convertObjToInt(resultMap.get("START_CHQ_NO2"));
            toNo = CommonUtil.convertObjToInt(resultMap.get("END_CHQ_NO2"));
            startChequeNo = CommonUtil.convertObjToStr(resultMap.get("START_CHQ_NO1"));
        }
        resultList = ClientUtil.executeQuery("getUsedChequeDetails", chequesMap);
        if (resultList != null && resultList.size() > 0) {
            List valueList = new ArrayList();
            for (int k = fromNo; k <= toNo; k++) {
                int val = k;
                cheqdetMap.put(startChequeNo + val, startChequeNo + val);
                chequesMap.put("INSTRUMENT1", startChequeNo);
                chequesMap.put("INSTRUMENT2", val);
                chequesMap.put("ACCOUNTNO", acctNo);
                chequesMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);

                List rsltList = ClientUtil.executeQuery("getStopInstrumentNoRule", chequesMap);
                if (rsltList != null && rsltList.size() > 0) {
                    HashMap stopMap = (HashMap) rsltList.get(0);
                    if (CommonUtil.convertObjToStr(stopMap.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")) {
                        cheqdetMap.remove(startChequeNo + val);
                    }
                }
            }
            for (int i = 0; i < resultList.size(); i++) {
                int chqnum = 0;
                HashMap resultMap = (HashMap) resultList.get(i);
                chqnum = CommonUtil.convertObjToInt(resultMap.get("CHQNUM"));
                if (cheqdetMap.containsKey(startChequeNo + chqnum)) {
                    cheqdetMap.remove(startChequeNo + chqnum);
                }
            }

            
        }


        return cheqdetMap;
    }
    public int getUnPaidCheques(String acctNo) {
        HashMap chequesMap = new HashMap();
        int issued = 0, returned = 0, used = 0;

        chequesMap.put("ACCTNO", acctNo);
        // System.out.println("chequesMap: "+ chequesMap);
        final List resultList = ClientUtil.executeQuery("getNoChequesIssued", chequesMap);
        if (resultList != null && resultList.size() > 0) {
            final HashMap resultMap = (HashMap) resultList.get(0);
            issued = CommonUtil.convertObjToInt(resultMap.get("ISSUED_LEAVES"));
        }

        final List returnedList = ClientUtil.executeQuery("getNoChequesUsed", chequesMap);
        if (returnedList != null && returnedList.size() > 0) {
            int size = returnedList.size();
            for (int i = 0; i < size; i++) {
                HashMap returnedMap = (HashMap) returnedList.get(i);
                used = used + CommonUtil.convertObjToInt(returnedMap.get("COUNT"));
            }
        }

        returned = issued - used;
        // System.out.println("returned: "+ returned);
        // System.out.println("issued: " + issued);
        // System.out.println("used: " +used);
        return returned;
    }

    public String checkAcNoWithoutProdType(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
//        String actNum="";
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                actNum = CommonUtil.convertObjToStr(mapData.get("ACT_NUM"));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
//                cbmProductIDProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
//                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
//                setCbmProductID(getProdType());
//                getProducts();
                cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
//                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return actNum;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try {
            String[] options = {objCommonRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null, amtLimit, CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return option;
    }

    /**
     * Getter for property lienAmount.
     *
     * @return Value of property lienAmount.
     */
    public double getLienAmount() {
        return lienAmount;
    }

    /**
     * Setter for property lienAmount.
     *
     * @param lienAmount New value of property lienAmount.
     */
    public void setLienAmount(double lienAmount) {
        this.lienAmount = lienAmount;
    }

    /**
     * Getter for property freezeAmount.
     *
     * @return Value of property freezeAmount.
     */
    public double getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * Setter for property freezeAmount.
     *
     * @param freezeAmount New value of property freezeAmount.
     */
    public void setFreezeAmount(double freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this._authorizeMap = authorizeMap;
    }

    /**
     * Getter for property loanInt.
     *
     * @return Value of property loanInt.
     */
    public java.lang.String getLoanInt() {
        return loanInt;
    }

    /**
     * Setter for property loanInt.
     *
     * @param loanInt New value of property loanInt.
     */
    public void setLoanInt(java.lang.String loanInt) {
        this.loanInt = loanInt;
    }

    /**
     * Getter for property loanBehaves.
     *
     * @return Value of property loanBehaves.
     */
    public java.lang.String getLoanBehaves() {
        return loanBehaves;
    }

    /**
     * Setter for property loanBehaves.
     *
     * @param loanBehaves New value of property loanBehaves.
     */
    public void setLoanBehaves(java.lang.String loanBehaves) {
        this.loanBehaves = loanBehaves;
    }

    /**
     * Getter for property totalLoanAmount.
     *
     * @return Value of property totalLoanAmount.
     */
    public java.util.HashMap getTotalLoanAmount() {
        return totalLoanAmount;
    }

    /**
     * Setter for property totalLoanAmount.
     *
     * @param totalLoanAmount New value of property totalLoanAmount.
     */
    public void setTotalLoanAmount(java.util.HashMap totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
        try {
            fillDropdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for property oldTransDetMap.
     *
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }

    /**
     * Setter for property oldTransDetMap.
     *
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }

    /**
     * Getter for property transProdId.
     *
     * @return Value of property transProdId.
     */
    public java.lang.String getTransProdId() {
        return transProdId;
    }

    /**
     * Setter for property transProdId.
     *
     * @param transProdId New value of property transProdId.
     */
    public void setTransProdId(java.lang.String transProdId) {
        this.transProdId = transProdId;
    }

    /**
     * Getter for property deposit_premature.
     *
     * @return Value of property deposit_premature.
     */
    public java.lang.String getDeposit_premature() {
        return deposit_premature;
    }

    /**
     * Setter for property deposit_premature.
     *
     * @param deposit_premature New value of property deposit_premature.
     */
    public void setDeposit_premature(java.lang.String deposit_premature) {
        this.deposit_premature = deposit_premature;
    }

    /**
     * Getter for property deposit_pre_int.
     *
     * @return Value of property deposit_pre_int.
     */
    public double getDeposit_pre_int() {
        return deposit_pre_int;
    }

    /**
     * Setter for property deposit_pre_int.
     *
     * @param deposit_pre_int New value of property deposit_pre_int.
     */
    public void setDeposit_pre_int(double deposit_pre_int) {
        this.deposit_pre_int = deposit_pre_int;
    }

    /**
     * Getter for property prematureString.
     *
     * @return Value of property prematureString.
     */
    public java.lang.String getPrematureString() {
        return prematureString;
    }

    /**
     * Setter for property prematureString.
     *
     * @param prematureString New value of property prematureString.
     */
    public void setPrematureString(java.lang.String prematureString) {
        this.prematureString = prematureString;
    }

    /**
     * Getter for property asAnWhen.
     *
     * @return Value of property asAnWhen.
     */
    public java.lang.String getAsAnWhen() {
        return asAnWhen;
    }

    /**
     * Setter for property asAnWhen.
     *
     * @param asAnWhen New value of property asAnWhen.
     */
    public void setAsAnWhen(java.lang.String asAnWhen) {
        this.asAnWhen = asAnWhen;
    }

    /**
     * Getter for property linkMap.
     *
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }

    /**
     * Setter for property linkMap.
     *
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }

    /**
     * Getter for property calMap.
     *
     * @return Value of property calMap.
     */
    public java.util.HashMap getCalMap() {
        return calMap;
    }

    /**
     * Setter for property calMap.
     *
     * @param calMap New value of property calMap.
     */
    public void setCalMap(java.util.HashMap calMap) {
        this.calMap = calMap;
    }

    public double taxAmount(double amt) {
        double tamt = 0.0;
        HashMap map = new HashMap();
        map.put("PROD_ID", getCboProductID());
        List lst = ClientUtil.executeQuery("getTaxDetailsNRO", map);
        if (lst != null && lst.size() > 0) {
            map = null;
            map = (HashMap) lst.get(0);
            double intAmt = CommonUtil.convertObjToDouble(map.get("RATE_OF_INT")).doubleValue();
            if (intAmt > 0) {
                Rounding rd = new Rounding();
                double taxAmts = (amt * intAmt) / 100;
                long taxAmt = (long) taxAmts;
                taxAmt = rd.getNearest(taxAmt, 1);
                tamt = taxAmt;
            }
        }
        return tamt;
    }

    /**
     * Getter for property normalCloser.
     *
     * @return Value of property normalCloser.
     */
    public java.lang.String getNormalCloser() {
        return normalCloser;
    }

    /**
     * Setter for property normalCloser.
     *
     * @param normalCloser New value of property normalCloser.
     */
    public void setNormalCloser(java.lang.String normalCloser) {
        this.normalCloser = normalCloser;
    }

    /**
     * Getter for property chargelst.
     *
     * @return Value of property chargelst.
     */
    public java.util.List getChargelst() {
        return chargelst;
    }

    /**
     * Setter for property chargelst.
     *
     * @param chargelst New value of property chargelst.
     */
    public void setChargelst(java.util.List chargelst) {
        this.chargelst = chargelst;
    }

    /**
     * Getter for property customerStreet.
     *
     * @return Value of property customerStreet.
     */
    public java.lang.String getCustomerStreet() {
        return customerStreet;
    }

    /**
     * Setter for property customerStreet.
     *
     * @param customerStreet New value of property customerStreet.
     */
    public void setCustomerStreet(java.lang.String customerStreet) {
        this.customerStreet = customerStreet;
    }

    /**
     * Getter for property advancesCreditInterest.
     *
     * @return Value of property advancesCreditInterest.
     */
    public double getAdvancesCreditInterest() {
        return advancesCreditInterest;
    }

    /**
     * Setter for property advancesCreditInterest.
     *
     * @param advancesCreditInterest New value of property
     * advancesCreditInterest.
     */
    public void setAdvancesCreditInterest(double advancesCreditInterest) {
        this.advancesCreditInterest = advancesCreditInterest;
    }

    /**
     * Getter for property avilableLoanSubsidy.
     *
     * @return Value of property avilableLoanSubsidy.
     */
    public double getAvilableLoanSubsidy() {
        return avilableLoanSubsidy;
    }

    /**
     * Setter for property avilableLoanSubsidy.
     *
     * @param avilableLoanSubsidy New value of property avilableLoanSubsidy.
     */
    public void setAvilableLoanSubsidy(double avilableLoanSubsidy) {
        this.avilableLoanSubsidy = avilableLoanSubsidy;
    }

    /**
     * Getter for property subsidyEditTransAmt.
     *
     * @return Value of property subsidyEditTransAmt.
     */
    public double getSubsidyEditTransAmt() {
        return subsidyEditTransAmt;
    }

    /**
     * Setter for property subsidyEditTransAmt.
     *
     * @param subsidyEditTransAmt New value of property subsidyEditTransAmt.
     */
    public void setSubsidyEditTransAmt(double subsidyEditTransAmt) {
        this.subsidyEditTransAmt = subsidyEditTransAmt;
    }

    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public double getRebateInterest() {
        return rebateInterest;
    }

    public boolean isWaiveoffPenal() {
        return waiveoffPenal;
    }

    public void setWaiveoffPenal(boolean waiveoffPenal) {
        this.waiveoffPenal = waiveoffPenal;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(double rebateInterest) {
        this.rebateInterest = rebateInterest;
    }

    /**
     * Getter for property waiveOffInterest.
     *
     * @return Value of property waiveOffInterest.
     */
    public boolean isWaiveOffInterest() {
        return waiveOffInterest;
    }

    /**
     * Setter for property waiveOffInterest.
     *
     * @param waiveOffInterest New value of property waiveOffInterest.
     */
    public void setWaiveOffInterest(boolean waiveOffInterest) {
        this.waiveOffInterest = waiveOffInterest;
    }

    /**
     * Getter for property waiveEditPenalTransAmt.
     *
     * @return Value of property waiveEditPenalTransAmt.
     */
    public double getWaiveEditPenalTransAmt() {
        return waiveEditPenalTransAmt;
    }

    /**
     * Setter for property waiveEditPenalTransAmt.
     *
     * @param waiveEditPenalTransAmt New value of property
     * waiveEditPenalTransAmt.
     */
    public void setWaiveEditPenalTransAmt(double waiveEditPenalTransAmt) {
        this.waiveEditPenalTransAmt = waiveEditPenalTransAmt;
    }

    /**
     * Getter for property txtInsuranceCharges.
     *
     * @return Value of property txtInsuranceCharges.
     */
    public String getTxtInsuranceCharges() {
        return txtInsuranceCharges;
    }

    /**
     * Setter for property txtInsuranceCharges.
     *
     * @param txtInsuranceCharges New value of property txtInsuranceCharges.
     */
    public void setTxtInsuranceCharges(String txtInsuranceCharges) {
        this.txtInsuranceCharges = txtInsuranceCharges;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
     public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(getAction());
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtAccountNumber());
            objservicetaxDetTo.setParticulars("Loan Closing");

            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDt);
            objservicetaxDetTo.setTrans_type("C");
            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDt);
            }
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
     public List calcServiceTaxAmount(String accNum,String prodId){// Changed the retyrn type from double to list
               HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", accNum);
        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
        double taxAmt=0;
        HashMap taxMap; // Added by nithya
        List taxSettingsList = new ArrayList();// Added by nithya
        if (chargeAmtList != null && chargeAmtList.size() > 0 ) {
             String checkFlag = "N";
             HashMap checkForTaxMap = new HashMap();
             whereMap = new HashMap();
             whereMap.put("value", prodId);
                List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
                if (accHeadList != null && accHeadList.size() > 0) {
                for (int i = 0; i < chargeAmtList.size(); i++) {
                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
                    if (chargeMap != null && chargeMap.size() > 0) {

                        String accId = "";

                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
                        String chargetype="";
                        if(chargeMap.containsKey("CHARGE_TYPE")){
                            chargetype=CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                        }
                        if (chargetype!=null&&chargetype.equals("EP CHARGE") && getEpCostWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype!=null&&chargetype.equals("ARC CHARGE") && getArcWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype!=null&&chargetype.equals("MISCELLANEOUS CHARGES") && getMiscellaneousWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype!=null&&chargetype.equals("POSTAGE CHARGES") && getPostageWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("ADVERTISE CHARGES") && getAdvertiseWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype!=null&&chargetype.equals("ARBITRARY CHARGES") && getArbitarayWaivwAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("LEGAL CHARGES") && getLegalWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("NOTICE CHARGES") && getNoticeWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("INSURANCE CHARGES") && getInsuranceWaiveAmont() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("EXECUTION DECREE CHARGES") && getDecreeWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        // Added by nithya on 13-04-2018 for handling GST for other charges 
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        if (chargetype != null && chargetype.equals("RECOVERY CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getRecoveryCharges());
                        }
                        if (chargetype != null && chargetype.equals("MEASUREMENT CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMeasurementCharges());
                        }
                        
                        if (chargetype != null && chargetype.equals("KOLEFIELD EXPENSE") && getKoleFieldExpenseWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getKoleFieldexpense());
                        }
                        
                        if (chargetype != null && chargetype.equals("KOLEFIELD OPERATION") && getKoleFieldOperationWaiveAmount() == 0) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getKoleFieldOperation());
                        }
                        
                        //checkFlag = checkServiceTaxApplicable(accId);
                        checkForTaxMap = checkServiceTaxApplicable(accId);// Added by nithya
//                        if (checkFlag != null && checkFlag.equals("Y")) {// Commented by nithya 
//                            String charge_amt = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_AMT"));
//                            taxAmt = taxAmt + CommonUtil.convertObjToDouble(charge_amt);
//                        }
                         if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")){
                            if(checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0){
                                if(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")) > 0){
                                   taxMap = new HashMap();
                                   taxMap.put("SETTINGS_ID",checkForTaxMap.get("SERVICE_TAX_ID"));
                                   taxMap.put(ServiceTaxCalculation.TOT_AMOUNT,chargeMap.get("CHARGE_AMT"));
                                   taxSettingsList.add(taxMap);   
                                }                                     
                            }
                        }
                    }

                }
            }
        }
       // return taxAmt;
        return taxSettingsList;
     }
//      public String checkServiceTaxApplicable(String accheadId) {
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
//      }
     
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
      
      
       public Date setFromDate(String Prod_Type, String prod_ID) {
         System.out.println("inside setFromDate :: " + Prod_Type + "-" + prod_ID);
         Date fromDt = null;
         try {
             if (Prod_Type.length() > 0 && prod_ID.length() > 0) {
                 HashMap provMap = new HashMap();
                 provMap.put("PROD_ID", prod_ID);
                 provMap.put("PROD_TYPE", Prod_Type);
                 provMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                 provMap.put("ACCT_NUM",getTxtAccountNumber());
                 List provList = ClientUtil.executeQuery("getSBODlastIntCalcDt", provMap);
                 if (provList != null && provList.size() > 0) {
                     String from_date = "";
                     provMap = (HashMap) provList.get(0);
                     if(provMap.get("LAST_CR_INT_APPLDT") != null){
                       from_date = CommonUtil.convertObjToStr(provMap.get("LAST_CR_INT_APPLDT"));  
                     }else{
                       from_date = CommonUtil.convertObjToStr(provMap.get("CREATE_DT"));  
                     }
                     fromDt = setProperDtFormat(DateUtil.addDays(DateUtil.getDateMMDDYYYY(from_date), 1));
                 }
             }
         } catch (Exception e) {
         }

         return fromDt;
    }       
       
    private Date setProperDtFormat(Date dt) {   //Added By Suresh
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public byte[] getPhotoByteArray() {
        return photoByteArray;
    }

    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
    }
    
    public HashMap getLoanRenewalMap() {
        return loanRenewalMap;
    }

    public void setLoanRenewalMap(HashMap loanRenewalMap) {
        this.loanRenewalMap = loanRenewalMap;
    }

    public double getMeasurementWaiveAmount() {
        return measurementWaiveAmount;
    }

    public void setMeasurementWaiveAmount(double measurementWaiveAmount) {
        this.measurementWaiveAmount = measurementWaiveAmount;
    }

    public boolean isMeasurementWaiveOff() {
        return measurementWaiveOff;
    }

    public void setMeasurementWaiveOff(boolean measurementWaiveOff) {
        this.measurementWaiveOff = measurementWaiveOff;
    }

    public double getRecoveryWaiveAmount() {
        return recoveryWaiveAmount;
    }

    public void setRecoveryWaiveAmount(double recoveryWaiveAmount) {
        this.recoveryWaiveAmount = recoveryWaiveAmount;
    }

    public boolean isRecoveryWaiveOff() {
        return recoveryWaiveOff;
    }

    public void setRecoveryWaiveOff(boolean recoveryWaiveOff) {
        this.recoveryWaiveOff = recoveryWaiveOff;
    }

    public boolean isKoleFieldOperationWaiveOff() {
        return koleFieldOperationWaiveOff;
    }

    public void setKoleFieldOperationWaiveOff(boolean koleFieldOperationWaiveOff) {
        this.koleFieldOperationWaiveOff = koleFieldOperationWaiveOff;
    }

    public boolean isKoleFieldExpenseWaiveOff() {
        return koleFieldExpenseWaiveOff;
    }

    public void setKoleFieldExpenseWaiveOff(boolean koleFieldExpenseWaiveOff) {
        this.koleFieldExpenseWaiveOff = koleFieldExpenseWaiveOff;
    }

    public double getKoleFieldOperationWaiveAmount() {
        return koleFieldOperationWaiveAmount;
    }

    public void setKoleFieldOperationWaiveAmount(double koleFieldOperationWaiveAmount) {
        this.koleFieldOperationWaiveAmount = koleFieldOperationWaiveAmount;
    }

    public double getKoleFieldExpenseWaiveAmount() {
        return koleFieldExpenseWaiveAmount;
    }

    public void setKoleFieldExpenseWaiveAmount(double koleFieldExpenseWaiveAmount) {
        this.koleFieldExpenseWaiveAmount = koleFieldExpenseWaiveAmount;
    }
    
}
