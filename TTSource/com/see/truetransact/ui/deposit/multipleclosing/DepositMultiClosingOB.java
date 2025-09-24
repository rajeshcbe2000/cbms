/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositMultiClosingOB.java
 *
 * Created on Mon Jan 13 18:24:58 IST 2015
 */
package com.see.truetransact.ui.deposit.multipleclosing;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.ui.transaction.agentCommisionDisbursal.AgentCommisionDisbursalOB;
import com.see.truetransact.uicomponent.CObservable;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.DefaultListModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Shihad
 */
public class DepositMultiClosingOB extends CObservable {

    private TransactionOB transactionOB;
    private TransactionTO transactionTO;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblDepositInterestApplication;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(DepositMultiClosingOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private int yearTobeAdded = 1900;
    private List finalList = null;
    private List finalTableList = null;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList rdList;
    private String txtProductID = "";
    private String txtTokenNo = "";
    final String prodBehavesLikeRecurr = "RECURRING";
    final String prodBehavesLikeDaily = "DAILY";
    final String prodBehavesLikeFixed = "FIXED";
    final String prodBehavesLikeCummulative = "CUMMULATIVE";
    private List calFreqAccountList = null;
    private String cboSIProductId = "";
    private ComboBoxModel cbmSIProductId;
    private ComboBoxModel cbmOAProductID;
    private ComboBoxModel cbmCategory;
    LinkedHashMap depSubNoTOMap;
    private DefaultListModel lstSelectedTransaction = new DefaultListModel();
    private LinkedHashMap newTransactionMap = new LinkedHashMap();
    HashMap cashtoTransferMap = new HashMap();
    private ComboBoxModel cbmProdType, cboClosingTransMode;
    private ComboBoxModel cbmClosingTransMode;
    private ComboBoxModel cbmClosingTransProdId;
    private String productBehavesLike = "";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private LinkedHashMap transactionDetailsTO;
    HashMap finalMap = new HashMap();
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private String productInterestType = "";
//    private EnhancedTableModel tblJointAccnt;
    public final String YES_STR = "Y";
    public final String NO_STR = "N";
    AccInfoTO objAccInfoTO;
    LinkedHashMap jntAcctTOMap;
    public LinkedHashMap jntAcctAll;
//    JointAcctHolderManipulation objJointAcctHolderManipulation = new JointAcctHolderManipulation();
    AgentCommisionDisbursalOB agentCommisionDisbursalOB = null;
//    private final String YES_FULL_STR = "Yes";
//    private final String NO_FULL_STR = "No";
    private String cboCategory = "";
//    HashMap jntAcctSingleRec;
    private String lblStatus;
    private String customerID;
    private String constitution;
    private String category;
    private String modeOfSettlement;
    private String depositActName;
    private String prinicipal;
    private String period;
    private String depositDate;
    private String maturityDate;
    private String maturityValue;
    private String rateOfInterest;
    private String intPaymentFreq;
    private String balanceDeposit;
    private String withDrawn;
    private String intCr;
    private String intDrawn;
    private String tdsCollected;
    private String lastIntAppDate;
    private String lienFreezeAmt;
    private String noInstPaid;
    private String noInstDue;
    private String balance;
    private String lblClosingType = "";
    private String DOUBLING_SCHEME_BEHAVES_LIKE = "";
    public HashMap oldTransactionMap = new HashMap();
    private String termLoanAdvanceActNum = "";
    private boolean rdoTypeOfDeposit_No = false;
    private String cboProductId = "";
    private String txtDepositNo = "";
    private String subDepositNo = "";
    private String prev_interest = "";
    private String closingIntCr = "";
    private String closingIntDb = "";
    private String payReceivable = "";
    private String permanentPayReceivable = "";
    private String closingTds = "";
    private String closingDisbursal = "";
    //2lines added 26.03.2007
    private String RateApplicable = "";
    private String PenaltyPenalRate = "";
    private double cummWithDrawnAmount = 0;
    private double cummIntCredit = 0;
    private double cummIntDebit = 0;
    private double cummTDCollected = 0;
    private double cummLienFreezeAmount = 0;
    private double cummBalance = 0;
    private double cummClosingCr = 0;
    private double cummClosingDb = 0;
    private double cummClosingTdsCollected = 0;
    private double cummClosingDisbursal = 0;
    private double cummClosingRateApplicable = 0;
    private double cummClosingPenaltyPenalRate = 0;
    private String tdsAcHd = null;
    private ComboBoxModel cbmProductId;
    private double unitAmt = 0;
    private double prematureRunPeriod = 0;
    private long periodNoOfDays = 0;
    private String partialAllowed = "";
    private String noOfUnitsWithDrawn = "";
    private String noOfUnitsAvai = "";
    private String prematureClosingRate = "";
    private String prematureClosingDate = "";
    private String txtAmtWithDrawn = "";
    private String presentUnitInt = "";
    private String settlementUnitInt = "";
    private String noOfUnits = "";
    private String depositRunPeriod = "";
    private String prodID = "";
    private String noOfWithDrawalUnits = "";
    private String withdrawalTOStatus = "";
    private String txtPWNoOfUnits = "";
    //one line added 27.03.2007
    private String setPenaltyPenalRate = "";
    private String typeOfDep = "";
    private boolean rdoPenaltyPenalRate_yes = false;
    private boolean rdoPenaltyPenalRate_no = false;
    private boolean rdoTypeOfDeposit_Yes = false;
    private boolean rdoTransfer_Yes = false;
    private boolean rdoTransfer_No = false;
    private String transfer_out_mode = null;
    private String transferBranch_code = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private ArrayList withdrawalTOs, deletePWList;
    private HashMap authorizeMap;
    private HashMap behavesMap = new HashMap();
    private HashMap oldTransMap = new HashMap();
    private HashMap ltdClosingMap = new HashMap();
    private double disburseAmt = 0.0;
    private double balanceAmt = 0.0;
    private String lblReceive = null;
    private String lblBalanceDeposit = null;
    private boolean rateOfIntCalculated = false;
    private String transStatus = "";
    private String penaltyInt = null;
    private String transProdId = "";
    private String actualPeriodRun = "";
    private String delayedInstallments = "";
    private String chargeAmount = "";
    private String lblPayRecDet;
    private boolean rdoYesButton;
    private boolean rdoNoButton;
    private int ViewTypeDet = -1;
    private String totalBalance = "";
    private Date curDate = null;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private String deathClaim = "";
    private boolean deathFlag = false;
    private String deathClaimAmt = "";
    private String agentCommisionRecoveredValue = "";
    private String lstProvDt = "";
    public boolean normalClosing = false;
    private boolean flPtWithoutPeriod = false;
    private HashMap serviceChargeMap = new HashMap();
    //The following variable added by Rajesh
    private String ltdDeposit = "";
    private String interestRound = "";
    DecimalFormat df = new DecimalFormat("##.00");
    //Added by chithra on 22-04-14 for addittional Interest
    private String lblMaturityPeriod;
    private String lblAddIntrstRteVal;
    private String lblAddIntRtAmtVal;
    private String addIntLoanAmt;
    private String premClos;
    private HashMap chargeMap;
    private List Chargelst;
    private HashMap bothRecPayMap = null;
    private HashMap serviceTax_Map = null;
    private Date currDt = null; // Added by nithya on 01-04-2016
//    private String lblServiceTaxval = "";
    private String creditBranch="";
    private String penaltyRateApplicatble = "Y";
    
//    AgentCommisionDisbursalOB agentCommisionDisbursalOB = new AgentCommisionDisbursalOB();
    private final static double Avg_Millis_Per_Month = 365.24 * 24 * 60 * 60 * 1000 / 12;

    public String getCreditBranch() {
        return creditBranch;
    }

    public void setCreditBranch(String creditBranch) {
        this.creditBranch = creditBranch;
    }
   
    void setCboCategory(String cboCategory) {
       this.cboCategory = cboCategory;
        setChanged();
    }

    String getCboCategory() {
        return this.cboCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
        setChanged();
    }

    ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCbmClosingTransMode() {
        return cbmClosingTransMode;
    }

    /**
     * Setter for property cbmRenewalInterestTransMode.
     *
     * @param cbmRenewalInterestTransMode New value of property
     * cbmRenewalInterestTransMode.
     */
    public void setCbmRenewalInterestTransMode(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransMode) {
        this.cbmClosingTransMode = cbmRenewalInterestTransMode;
    }

    public ComboBoxModel getCboRenewalInterestTransMode() {
        return cboClosingTransMode;
    }

    public void setCboRenewalInterestTransMode(ComboBoxModel cboRenewalInterestTransMode) {
        this.cboClosingTransMode = cboRenewalInterestTransMode;
    }
    private HashMap lookUpHash;

    public void setCbmRenewalDepTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdId) {
        this.cbmClosingTransProdId = cbmRenewalInterestTransProdId;
    }

    /**
     * Getter for property cbmRenewalInterestTransProdId.
     *
     * @return Value of property cbmRenewalInterestTransProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransProdId() {
        return cbmClosingTransProdId;
    }

    public void setCbmRenewalInterestTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdId) {
        this.cbmClosingTransProdId = cbmRenewalInterestTransProdId;
    }

    public DepositMultiClosingOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositMultiClosingJNDI");
            map.put(CommonConstants.HOME, "deposit.multipleclosing.DepositMultiClosingHome");
            map.put(CommonConstants.REMOTE, "deposit.multipleclosing.DepositMultiClosing");
            curDate = ClientUtil.getCurrentDate();
            agentCommisionDisbursalOB = new AgentCommisionDisbursalOB();

            setDepositInterestTableTitle();
            fillDropdown();
            tblDepositInterestApplication = new EnhancedTableModel(null, tableTitle);
            transactionOB = new TransactionOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setDepositInterestTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Account No");
        tableTitle.add("Customer");
        tableTitle.add("Deposit Amount");
        tableTitle.add("Deposit Date");
        tableTitle.add("Closing Type");
        tableTitle.add("Mat Date");            
        tableTitle.add("Rate of Interest");
        tableTitle.add("Int.paid");
        tableTitle.add("Int.Payable");
        tableTitle.add("Payable");
        tableTitle.add("Receivable");
        tableTitle.add("Closing Amount");
        tableTitle.add("Total Days Run"); // Added by nithya on 01-04-2016    
        tableTitle.add("TDS"); // Added by nithya on 06-02-2020 for KD-1090
        IncVal = new ArrayList();
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue()");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    param = new HashMap();
                    param.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    param.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(param);
                    fillData((HashMap) keyValue.get(CommonConstants.DATA));
                    cbmOAProductID = new ComboBoxModel(key, value);
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
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
        setChanged();
    }

    ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmRenewalInterestTransProdId(String prodType) {
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
        cbmClosingTransProdId = new ComboBoxModel(key, value);
        this.cbmClosingTransProdId = cbmClosingTransProdId;
        setChanged();
    }

    private void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            cbmProdType.removeKeyAndElement("AD");
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            cbmProdType.removeKeyAndElement("MDS");// Added by nithya
            cbmProdType.removeKeyAndElement("GL"); // Added by nithya
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getSIProducts");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmSIProductId = new ComboBoxModel(key, value);
            lookup_keys.add("REMITTANCE_PAYMENT.PAY_MODE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("REMITTANCE_PAYMENT.PAY_MODE"));
            cbmClosingTransMode = new ComboBoxModel(key, value);
            lstSelectedTransaction = new DefaultListModel();
            lookup_keys.add("CATEGORY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("CATEGORY"));
            cbmCategory = new ComboBoxModel(key, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
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

    public String getProductBehaveLike(String param) {
        final HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", param);
        final List resultList = ClientUtil.executeQuery("getProductBehavesLike", whereMap);
        HashMap resultProductBehavesLike = (HashMap) resultList.get(0);
        productBehavesLike = CommonUtil.convertObjToStr(resultProductBehavesLike.get("BEHAVES_LIKE"));
        productInterestType = CommonUtil.convertObjToStr(resultProductBehavesLike.get("INT_TYPE"));
        return productBehavesLike;
    }
    
    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            finalMap = new HashMap();
            List lst = null;
            if (whereMap.containsKey("AUTHORIZE")) {
                lst = ClientUtil.executeQuery("listAllMultipleClosingAuthorize", whereMap);
            } else {
                lst = ClientUtil.executeQuery("listAllMultipleClosing", whereMap);
            }         
            System.out.println("sssss--->" + lst.size());
            System.out.println("whereMap  --->" + whereMap);          
            DepSubNoAccInfoTO objDepSubNoAccInfoTO = null;
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    dataMap = (HashMap) lst.get(i);
                    HashMap renewalMap = new HashMap();
                    System.out.println("dataMapffffff--->" + dataMap);
                    //String accNo = CommonUtil.convertObjToStr(dataMap.get("Deposit No"));
                    String accNo = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO"));
                    String customer = CommonUtil.convertObjToStr(dataMap.get("CUSTOMER"));
                    String cust_id = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                    //String depAmt = CommonUtil.convertObjToStr(dataMap.get("Deposit amount"));
                    String depAmt = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT AMOUNT"));
                    String categ = "";
                    if (whereMap.containsKey("CATEGORY") && whereMap.get("CATEGORY") != null) {
                        categ = CommonUtil.convertObjToStr(whereMap.get("CATEGORY"));
                    } else {
                        categ = CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));
                    }
                    System.out.println("categ------->" + categ);
                    String currCategory = CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));
                    String prodid = CommonUtil.convertObjToStr(dataMap.get("PRODUCT_ID"));
                    //String opMode = CommonUtil.convertObjToStr(dataMap.get("Opening Mode"));
                    String opMode = CommonUtil.convertObjToStr(dataMap.get("OPENING MODE"));
                    String MatDate = CommonUtil.convertObjToStr(dataMap.get("MATURITY_DATE"));
                    String prodBevaves = getProductBehaveLike(prodid);
                    setDepositDate(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_DT")));
                    HashMap data = new HashMap();
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(accNo);
                    rowList.add(customer);
                    rowList.add(depAmt);
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_DT")));
                    Date matDate = DateUtil.getDateMMDDYYYY(MatDate);
                    HashMap getDataMap = new HashMap();
                    if ((DateUtil.dateDiff(matDate, curDate)) >= 0) {
                        rowList.add("normal");
                        getDataMap.put("CLOSING_TYPE", "NORMAL");
                        setLblClosingType("NORMAL");
                    } 
                    else {
                        getDataMap.put("CLOSING_TYPE", "PREMATURE");
                        setLblClosingType("PREMATURE");
                        rowList.add("premature");
                    }
//                  rowList.add(opMode);
                    rowList.add(MatDate);                  
//                    calculateMaturity();//setting closingtype...
                    getDataMap.put("DEPOSITNO", accNo);
                    getDataMap.put("DEPOSITSUBNO", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_SUB_NO")));
                    Date deposit_Date = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_DT")));
//                    Date mat_Date = (Date) DateUtil.getDateMMDDYYYY(observable.getMaturityDate());
                    getDataMap.put("DEPOSIT_DT", deposit_Date);
                    getDataMap.put("MATURITY_DT", matDate);
                    getDataMap.put(CommonConstants.PRODUCT_ID, CommonUtil.convertObjToStr(dataMap.get("PRODUCT_ID")));
                    getDataMap.put("CUSTID", cust_id);
                    getDataMap.put("AMOUNT", depAmt);
                    getDataMap.put("CATEGORY_ID", CommonUtil.convertObjToStr("CATEGORY"));//lblCategoryValue.getText()
                    getDataMap.put("CURRENT_DT", curDate);
                    getData(getDataMap);
                    System.out.println("prod id hr"+getProdID());
                    HashMap dMap = new HashMap();
                    dMap.put("DEPOSIT NO", accNo);
                    List list = (List) ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", dMap);
                    HashMap calcDepSubNo;
                    String oldDepDate = "";
                    System.out.println("objDepSubNoAccInfoTO-->" + objDepSubNoAccInfoTO);
                    System.out.println("roi here shi" + getPrematureClosingRate());
                    double renIntAmt = 0;
                    double renewedDepAmt = 0, matAmt = 0, balIntAmt = 0;
                    rowList.add(getPrematureClosingRate());//ROI
                    System.out.println("get tot balnc" + getTotalBalance());
                    System.out.println("pay recv "+getPayReceivable());
                    System.out.println("int payable "+getClosingIntDb());
                    System.out.println("closing disb "+getClosingDisbursal());               
                    setPayReceivable(CommonUtil.convertObjToStr(Math.round(CommonUtil.convertObjToDouble(getPayReceivable()))));
                    //modified by rishad 09/07/2015
                    setClosingIntDb(CommonUtil.convertObjToStr(Math.round(CommonUtil.convertObjToDouble(getClosingIntDb()))+Math.round(CommonUtil.convertObjToDouble(getLblAddIntRtAmtVal()))));              
                    setClosingDisbursal(CommonUtil.convertObjToStr(Math.round(CommonUtil.convertObjToDouble(getClosingDisbursal()))+Math.round(CommonUtil.convertObjToDouble(getLblAddIntRtAmtVal()))));              
                    System.out.println("pay recv1 "+getPayReceivable());
                    System.out.println("int payable1 "+getClosingIntDb());
                    rowList.add(getIntCr());
                    rowList.add(getClosingIntDb());  
                    System.out.println("intr hr shi"+getPayReceivable()); 
                    if (CommonUtil.convertObjToDouble(getClosingDisbursal()) > CommonUtil.convertObjToDouble(getTotalBalance())) {
                        rowList.add(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getPayReceivable()) + Math.round(CommonUtil.convertObjToDouble(getLblAddIntRtAmtVal()))));
                        rowList.add("0.0");
                    } else {
                        rowList.add("0.0");
                        rowList.add(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getPayReceivable())));
                    }
                    rowList.add(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingDisbursal()) - CommonUtil.convertObjToDouble(getClosingTds()))); // Added by nithya on 06-02-2020 for KD-1090
                    // Added by nithya on 01-04-2016
                    currDt = ClientUtil.getCurrentDate();
                    if(getDepositDate() != null && !getDepositDate().equals("")){
                        if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(getDepositDate()),currDt) >= 0){                           
                           String monthDiff =  CommonUtil.convertObjToStr(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(getDepositDate()),currDt));
                           rowList.add(monthDiff);
                        }else{
                           rowList.add("Prematured");  
                        }
                    }                    
                    // End
                    rowList.add(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingTds()))); // Added by nithya on 06-02-2020 for KD-1090
                    tableList.add(rowList);
                    HashMap objMap = new HashMap();
                    setBalanceDeposit(depAmt);
                    objMap.put("MODE", getLblClosingType());
                    System.out.println("getlbl close type"+getLblClosingType());
                    objMap.put("COMMAND", getCommand());
                    objMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    objMap.put(CommonConstants.USER_ID,whereMap.get(CommonConstants.USER_ID));
                    System.out.println("Customer id" + getCustomerID());
                    if (((String) objMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)
                            || ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)
                            || ((String) objMap.get("MODE")).equals(CommonConstants.TRANSFER_OUT_CLOSURE)) {
                        HashMap closureData = new HashMap();
                        closureData.put("DEPOSIT_NO", getTxtDepositNo());
//                        closureData.put("DEPOSIT_SUB_NO", getSubDepositNo());
                        closureData.put("DEPOSIT_SUB_NO",CommonUtil.convertObjToInt("1"));
                        System.out.println("getClosingIntCr()=====" + getClosingIntCr());
                        closureData.put("CR_INTEREST", getClosingIntCr());
                        System.out.println("getClosingIntDb()======" + getClosingIntDb());
                        closureData.put("DR_INTEREST", getClosingIntDb());
                        System.out.println("getClosingIntDb()===" + getClosingIntDb());
                        closureData.put("INTEREST_DRAWN", getIntDrawn());
                        System.out.println("getIntCr()======" + getIntCr());  
                        closureData.put("PAID_INTEREST", CommonUtil.convertObjToStr(Math.round(CommonUtil.convertObjToDouble(getIntCr()))));
                        closureData.put("TDS_SHARE", getClosingTds());
                        closureData.put("TDS_ACHD", getTdsAcHd());
                        closureData.put("PROD_ID", getProdID());
                        closureData.put("INT_DISPLAY", getLblReceive());
                        closureData.put("PAY_AMT", getPayReceivable());
//                    closureData.put("LIEN", new Double(getLienAmount())); //Get this from the common screen
                        closureData.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                        closureData.put("DEPOSIT_AMT", getBalanceDeposit());
                        closureData.put("ROI", getRateApplicable());
                        System.out.println("getPenaltyPenalRate()====" + getPenaltyPenalRate());
                        closureData.put("PENAL_INT", getPenaltyPenalRate());
                        closureData.put("CUST_ID", getCustomerID());
                        closureData.put("DEPOSIT_DT", getDepositDate());
                        closureData.put("MATURITY_DT", getMaturityDate());
                        closureData.put("MODE", getLblClosingType());
                        System.out.println("getPenaltyInt()===" + getPenaltyInt());
                        closureData.put("PENALTY_INT", getPenaltyInt());
                        closureData.put("LIEN_STATUS", getTransStatus());
                        closureData.put("TRANS_PROD_ID", getTransProdId());
                        closureData.put("CLEAR_BALANCE", getBalance());
                        closureData.put("CURR_RATE_OF_INT", getPrematureClosingRate());
                        closureData.put("PENAL_RATE", getPenaltyPenalRate());
                        System.out.println("getChargeAmount()====" + getChargeAmount());
//                    closureData.put("DELAYED_AMOUNT", getChargeAmount());
                        closureData.put("DELAYED_AMOUNT", "0");
                        System.out.println("getPrev_interest====" + getPrev_interest());
                        double d = 0.0;
                        if (!getPrev_interest().equals("")) {
                            d = Double.parseDouble(getPrev_interest());
                            System.out.println("dddd" + d);

                        }
                        closureData.put("PREV_INTEREST_AMT", d);
                        closureData.put("INTEREST_AMT", CommonUtil.convertObjToDouble(getPayReceivable()));
                        double finalclosingamt = CommonUtil.convertObjToDouble(getClosingDisbursal()) - CommonUtil.convertObjToDouble(getClosingTds());
                        closureData.put("TOTAL_AMT", finalclosingamt);
                        closureData.put("LAST_INT_APPL_DT", getLastIntAppDate());
                        closureData.put("TOTAL_BALANCE", getTotalBalance());
//                        closureData.put("TYPES_OF_DEPOSIT", getTypeOfDep()); 
                        closureData.put("TYPES_OF_DEPOSIT", "NOLTD");
                        closureData.put("AGENT_COMMISION_AMT", getAgentCommisionRecoveredValue());
                        closureData.put("TRANSFER_OUT_MODE", transfer_out_mode);
                        closureData.put("TRANSFER_OUT_BRANCH_CODE", getTransferBranch_code());

                        if (oldTransactionMap != null && oldTransactionMap.size() > 0) {
                            closureData.put("OLDTRANSACTION", oldTransactionMap);
                        }
                        if (oldTransMap.containsKey("TRANS_DETAILS")) {
                            closureData.put("TRANS_DETAILS", oldTransMap);
                        }
                        if (getLtdClosingMap() != null && getLtdClosingMap().size() > 0) {
                            closureData.put("LTDCLOSINGDATA", getLtdClosingMap());
                        }
                        if (getLtdDeposit().equals("true")) {
                            objMap.put("LTD", "LTD");
                        }
                        if (getChargelst() != null) {
                            closureData.put("Charge List Data", getChargelst());
                            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0
                                    && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT")) > 0) {
                                closureData.put("serviceTax_Details", getServiceTax_Map());
                                closureData.put("serviceTax_DetailsTo", setServiceTaxDetails(serviceTax_Map));
                            }
                        }
                        //Added By Suresh
                        if (serviceChargeMap != null && serviceChargeMap.size() > 0) {
                            closureData.put("SERVICE_CHARGE_DETAILS", getServiceChargeMap());
                            serviceChargeMap = null;
                        }
                        closureData.put("ADD_INT_AMOUNT", Math.round(CommonUtil.convertObjToDouble(getLblAddIntRtAmtVal())));//added by chithra on 16-05-14 For additional int amt

                        closureData.put("ADD_LOAN_INT_AMOUNT", getAddIntLoanAmt());//added by chithra on 16-05-14 For additional Loan int amt
                        closureData.put("ADD_INT_DAYS", getLblMaturityPeriod());
                        closureData.put("ADD_INT_RATE", getLblAddIntrstRteVal());

                        if (getLastIntAppDate() != null && !getLastIntAppDate().equals("")) {
                            closureData.put("FROM_INT_DATE", getLastIntAppDate());
                        } else {
                            closureData.put("FROM_INT_DATE", getDepositDate());
                        }
                        if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                            closureData.put("TO_INT_DATE", curDate);
                        } else {
                            closureData.put("TO_INT_DATE", getMaturityDate());
                        }
                        if (getBothRecPayMap() != null && getBothRecPayMap().size() > 0) {
                            closureData.put("BOTH_PAY_REC", getBothRecPayMap());
                        }                        
                        objMap.put("CLOSUREMAP", closureData);                        
                        dataMap.put("TRANS_MODE", whereMap.get("TRANS_MODE"));
                        //dataMap.put(CommonConstants.BRANCH_ID,whereMap.get(CommonConstants.BRANCH_ID));
                        if(CommonUtil.convertObjToStr(whereMap.get("TRANS_MODE")).equals("TRANSFER")){
                            dataMap.put("PROD_ID", whereMap.get("PROD_ID"));
                            dataMap.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
                            dataMap.put("AC_NO", whereMap.get("AC_NO"));
                            dataMap.put("Customer", whereMap.get("Customer"));
                            dataMap.put(CommonConstants.BRANCH_ID,getCreditBranch());
                        }
                        if (CommonUtil.convertObjToDouble(getClosingDisbursal()).doubleValue() > 0) {
                            allowedTransactionDetailsTO = new LinkedHashMap();
                            if (transactionDetailsTO == null) {
                                transactionDetailsTO = new LinkedHashMap();
                            }
                            if (deletedTransactionDetailsTO != null) {
                                transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                                deletedTransactionDetailsTO = null;
                            }
                            allowedTransactionDetailsTO.put("1", setTransactionTO(dataMap));
                            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                            allowedTransactionDetailsTO = null;
                            objMap.put("TransactionTO", transactionDetailsTO);
                        }
                       
                        if (getAuthorizeMap() != null) {
                            double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                            double penalMonth = CommonUtil.convertObjToDouble(getDepositPenalMonth()).doubleValue();
                            if (penalAmt > 0) {
                                objMap.put("DEPOSIT_PENAL_AMT", getDepositPenalAmt());
                                objMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
                            }
                            objMap.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                                objMap.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
                            }
                        }
                        //========= END OF TRANSACTION BLOCK ======================
//                HashMap proxyResultMap = new HashMap();
                        //added by vivek
                        System.out.println("payorrecjcijifw>>>" + getLblPayRecDet());
                        if (getLblPayRecDet() != null) {
                            objMap.put("PAYORRECEIVABLE", getLblPayRecDet());
                        }
                        System.out.println("#######doAction######### : " + closureData);
                            renewalMap.put("PREV_INT_AMT", "0");
//                        objMap.put("USER_ID", TrueTransactMain.USER_ID);
//                        objMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);    
                            
                        //Added by sreekrishnan for interbranch transaction
                        objMap.put("BRANCH_ID", getSelectedBranchID());
                        System.out.println("branch%#%#%%#@"+getSelectedBranchID());
                        objMap.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
                        finalMap.put(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")), objMap);
                        //endddd
                    }
                    setLblAddIntRtAmtVal("");
                }
                setFinalMap(finalMap);
                System.out.println("#$# tableList:" + tableList);
                depSubNoTOMap = null;
                tblDepositInterestApplication = new EnhancedTableModel((ArrayList) tableList, tableTitle);
                lst = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public ServiceTaxDetailsTO setServiceTaxDetails(HashMap serviceTax_Map) {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtDepositNo());
            objservicetaxDetTo.setParticulars("Deposit Closing");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(ClientUtil.getCurrentDate());

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(ClientUtil.getCurrentDate());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    public TransactionTO setTransactionTO(HashMap hash) {
        System.out.println("hash here asd"+hash);
     transactionTO = new TransactionTO();
     transactionTO.setCommand(getCommand());
     transactionTO.setTransType(CommonUtil.convertObjToStr(hash.get("TRANS_MODE")));
     transactionTO.setTransAmt(CommonUtil.convertObjToDouble(getClosingDisbursal()) - CommonUtil.convertObjToDouble(getClosingTds()));
     transactionTO.setApplName(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
     transactionTO.setProductId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
     transactionTO.setProductType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
     transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(hash.get("AC_NO")));
     transactionTO.setInstType("WITHDRAW_SLIP");
     transactionTO.setChequeDt(curDate); 
     transactionTO.setBranchId(CommonUtil.convertObjToStr(hash.get(CommonConstants.BRANCH_ID)));
     return transactionTO;
    }
    public void getData(HashMap paramMap) throws Exception {
        System.out.println("paramMap......................." + paramMap);
        HashMap mapData = new HashMap();
        HashMap tdsData = new HashMap();
        HashMap closureData = new HashMap();
        mapData = proxy.executeQuery(paramMap, map); 
        System.out.println("getDataMapData : " + mapData);
        HashMap depositMap = new HashMap();
        setProdID(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_ID")));
        setTxtDepositNo(CommonUtil.convertObjToStr(paramMap.get("DEPOSITNO")));
        if (mapData != null && !mapData.equals("") && mapData.size() > 0) {
            setDepositDetails((HashMap) ((List) mapData.get("DEPOSIT_DETAILS")).get(0));
            if (mapData.containsKey("DEPOSIT_DETAILS")) {
                depositMap = (HashMap) ((List) mapData.get("DEPOSIT_DETAILS")).get(0);
            }
            //            setCustomerDetails((HashMap)((List)mapData.get("DEPOSIT_DETAILS")).get(0)) ;
            if (mapData.containsKey("DEPOSIT_CLOSE_DETAILS")) {
                //added by Chithra on 09-06-14
                Date depDt = null;
                long diffDays = 0;
                depDt = DateUtil.getDateMMDDYYYY(getDepositDate());
                diffDays = DateUtil.dateDiff(depDt, curDate);
                HashMap whrMap = new HashMap();
                List lst = (List) mapData.get("DEPOSIT_CLOSE_DETAILS");
                if (lst != null && lst.size() > 0) {
                    HashMap depMap = (HashMap) lst.get(0);
                    whrMap.put("PROD_ID", depMap.get("PROD_ID"));
                } else {
                    whrMap.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                }
                List lstScheme = ClientUtil.executeQuery("getSchemeIntroDate", whrMap);
                long mindte = 0;
                if (lstScheme != null && lstScheme.size() > 0) {
                    HashMap schemeMap = (HashMap) lstScheme.get(0);
                    if (schemeMap != null && schemeMap.containsKey("MIN_DEPOSIT_PERIOD")) {
                        mindte = CommonUtil.convertObjToLong(CommonUtil.convertObjToStr(schemeMap.get("MIN_DEPOSIT_PERIOD")));
                    }

                }
                //Added by Chithra 
                if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    HashMap con_map = new HashMap();
                    con_map.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                    lstScheme = ClientUtil.executeQuery("checkIntRateEditable", con_map);
                    if (lstScheme != null && lstScheme.size() > 0) {
                        HashMap schemeMap = (HashMap) lstScheme.get(0);
                        if (schemeMap != null && schemeMap.containsKey("DIFF_ROI_YES_NO")) {
                            String diffRoi = CommonUtil.convertObjToStr(schemeMap.get("DIFF_ROI_YES_NO"));
                            if (diffRoi != null && diffRoi.equals("Y") && schemeMap.containsKey("PREMATURE_CLOSURE_APPLY")) {
                                String preMatAppl = CommonUtil.convertObjToStr(schemeMap.get("PREMATURE_CLOSURE_APPLY"));
                                if (preMatAppl != null && schemeMap.containsKey("PRE_MAT_INT_TYPE")) {
                                    String preMatIntType = CommonUtil.convertObjToStr(schemeMap.get("PRE_MAT_INT_TYPE"));
                                    if (preMatIntType != null && preMatIntType.equals("COMPOUND")) {
                                        DOUBLING_SCHEME_BEHAVES_LIKE = "CUMMULATIVE";
                                    } else if (preMatIntType != null && preMatIntType.equals("SIMPLE")) {
                                        DOUBLING_SCHEME_BEHAVES_LIKE = "FIXED";
                                    }
                                }
                            }
                        }
                    }

                }
                if (diffDays >= mindte) {
                    setClosingIntRate((List) mapData.get("DEPOSIT_CLOSE_DETAILS"));
                    System.out.println("prem closing rate"+getPrematureClosingRate());
                }
            } else if (mapData.containsKey("DEPOSIT_CLOSE_DETAILS_FLOATING")) {
                setClosingFloatingDetails((HashMap) mapData.get("DEPOSIT_CLOSE_DETAILS_FLOATING"));
            }
            List list = (List) mapData.get("TransactionTO");
            if (mapData.containsKey("TRANS_DETAILS")) {
                //commented by rishad 06/12/2016
             //   oldTransactionMap.put("TRANS_DETAILS", mapData.get("TRANS_DETAILS"));
                //The following block added by Rajesh
                if (mapData.get("TRANS_DETAILS") instanceof String) {
                    termLoanAdvanceActNum = CommonUtil.convertObjToStr(mapData.get("TRANS_DETAILS"));
                }
            }
            if (mapData.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_CASH"))//depositamount giving cash.
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_AMT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_AMT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_INT_DETAILS_CASH"))//interest amount paid to payable
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_INT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_INT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH"))//interest amount payable to cash
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER"))//deposit amount giving transfer
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER"))//interest amount paid to payable
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER"))//interest amount payable to transfer
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER"))//recurring delayed chargeamount
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER"))//TDS deducted amount.
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER"));
            }

            mapData.put("DEPOSIT_NO", paramMap.get("DEPOSITNO"));
            if (!mapData.containsKey("DEPOSIT_CLOSE_DETAILS_FLOATING")) {
                setClosingDetails(mapData);
            }
            transactionOB.setDetails(list);
            //added By Chithra on 22-04-14

            Date currDt = null, strMatDt = null;
            long diff = 0;
            strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT")));
            currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT")));
            diff = DateUtil.dateDiff(strMatDt, currDt);
            if (mapData != null && mapData.containsKey("RENEWAL_AND_MATURED_DETAILS") && diff > 0) {
                String renewalOpt = "";
                String matuOpt = "";
                String notRenewMatu = "";
                HashMap addIntdet = new HashMap();
                String lienPrdId = "";
                String lienAmt = "";
                if (rdoTypeOfDeposit_No) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("DEPOSIT_NO", this.getTxtDepositNo());
                    List list1 = ClientUtil.executeQuery("getLoanAccountDetailsForClosing", whereMap);
                    if (list1 != null && list1.size() > 0) {
                        HashMap newMap = (HashMap) list1.get(0);
                        lienPrdId = CommonUtil.convertObjToStr(newMap.get("LIEN_PROD_ID"));
                        lienAmt = CommonUtil.convertObjToStr(newMap.get("LIEN_AMOUNT"));
                    }
                }
                addIntdet = (HashMap) mapData.get("RENEWAL_AND_MATURED_DETAILS");//addDetList.get(0);
                renewalOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_RENEWAL"));
                matuOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_MATURITY"));
                notRenewMatu = CommonUtil.convertObjToStr(addIntdet.get("ELIGIBLE_TWO_RATE"));
                String SBorDep = CommonUtil.convertObjToStr(addIntdet.get("INT_RATE_APPLIED_OVERDUE"));
                String closureYn = CommonUtil.convertObjToStr(addIntdet.get("CLOSURE_INT_YN"));
                if (closureYn != null && closureYn.equalsIgnoreCase("Y")) {
                    if (SBorDep != null && SBorDep.equalsIgnoreCase("Y")) {// added by chithra 17-05-14
                        setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");
                        HashMap ROIDet = new HashMap();
                        if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                            List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                            if (DataList != null && DataList.size() > 0) {
                                ROIDet = (HashMap) DataList.get(0);
                                if (ROIDet != null) {
                                    setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                }
                                if (rdoTypeOfDeposit_No) {
                                    HashMap whereAddIntdet = new HashMap();
                                    whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                    whereAddIntdet.put("PRODID", lienPrdId);
                                    whereAddIntdet.put("CUSTID", getCustomerID());
                                    whereAddIntdet.put("PERIOD", diff);
                                    whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                    whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                    List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                    if (addInterest != null && addInterest.size() > 0) {
                                        HashMap intDet = (HashMap) addInterest.get(0);
                                        if (intDet != null && intDet.containsKey("ROI")) {
                                            String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                            setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                        }
                                    }
                                }

                                setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt, currDt));

                            } else {
                                setLblAddIntrstRteVal("");
                                setLblMaturityPeriod("");
                                setLblAddIntRtAmtVal("");
                                setAddIntLoanAmt("");
                            }
                        }
                    } else if (SBorDep != null && SBorDep.equalsIgnoreCase("N")) {
                        String DateVAl = "";
                        if (renewalOpt != null && renewalOpt.length() > 0 && renewalOpt.equalsIgnoreCase("Y")) {
                            DateVAl = CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap ROIDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                                if (DataList != null && DataList.size() > 0) {
                                    ROIDet = (HashMap) DataList.get(0);
                                    if (ROIDet != null) {
                                        setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                    }
                                    if (rdoTypeOfDeposit_No) {
                                        HashMap whereAddIntdet = new HashMap();
                                        whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                        whereAddIntdet.put("PRODID", lienPrdId);
                                        whereAddIntdet.put("CUSTID", getCustomerID());
                                        whereAddIntdet.put("PERIOD", diff);
                                        whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                        whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                        List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                        if (addInterest != null && addInterest.size() > 0) {
                                            HashMap intDet = (HashMap) addInterest.get(0);
                                            if (intDet != null && intDet.containsKey("ROI")) {
                                                String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                                setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                            }
                                        }
                                    }

                                    setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt, currDt));
                                } else {
                                    setLblAddIntrstRteVal("");
                                    setLblMaturityPeriod("");
                                    setLblAddIntRtAmtVal("");
                                    setAddIntLoanAmt("");
                                }
                            }

                        } else if (matuOpt != null && matuOpt.length() > 0 && matuOpt.equalsIgnoreCase("Y")) {
                            DateVAl = CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap ROIDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS");
                                if (DataList != null && DataList.size() > 0) {
                                    ROIDet = (HashMap) DataList.get(0);
                                    if (ROIDet != null) {
                                        setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                    }
                                    if (rdoTypeOfDeposit_No) {
                                        HashMap whereAddIntdet = new HashMap();
                                        whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                        whereAddIntdet.put("PRODID", lienPrdId);
                                        whereAddIntdet.put("CUSTID", getCustomerID());
                                        whereAddIntdet.put("PERIOD", diff);
                                        whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                        whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                        List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                        if (addInterest != null && addInterest.size() > 0) {
                                            HashMap intDet = (HashMap) addInterest.get(0);
                                            if (intDet != null && intDet.containsKey("ROI")) {
                                                String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                                setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                            }
                                        }
                                    }
                                    setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt, currDt));
                                } else {
                                    setLblAddIntrstRteVal("");
                                    setLblMaturityPeriod("");
                                    setLblAddIntRtAmtVal("");
                                    setAddIntLoanAmt("");
                                }
                            }
                        } else if (notRenewMatu != null && notRenewMatu.trim().length() > 0) {

                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap RnwDet = new HashMap();
                            double rnwInt = 0.0, matInt = 0.0;
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                                if (DataList != null && DataList.size() > 0) {
                                    RnwDet = (HashMap) DataList.get(0);
                                    if (RnwDet != null) {
                                        rnwInt = CommonUtil.convertObjToDouble(RnwDet.get("ROI"));
                                    }
                                } else {
                                    rnwInt = 0.0;
                                }
                            }
                            HashMap MatDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS");
                                if (DataList != null && DataList.size() > 0) {
                                    MatDet = (HashMap) DataList.get(0);
                                    matInt = CommonUtil.convertObjToDouble(MatDet.get("ROI"));
                                } else {
                                    matInt = 0.0;
                                }
                            }
                            if (notRenewMatu.equalsIgnoreCase("N")) {

                                if (matInt < rnwInt) {
                                    DateVAl = CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(matInt));
                                } else {
                                    DateVAl = CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(rnwInt));
                                }
                            } else if (notRenewMatu.equalsIgnoreCase("Y")) {
                                if (matInt > rnwInt) {
                                    DateVAl = CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(matInt));
                                } else {
                                    DateVAl = CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(rnwInt));
                                }
                            }
                            if (matInt == 0 && rnwInt == 0) {
                                setLblAddIntRtAmtVal("");
                                setLblMaturityPeriod("");
                                setLblAddIntrstRteVal("");
                                setAddIntLoanAmt("");
                            } else {
                                if (rdoTypeOfDeposit_No) {
                                    HashMap whereAddIntdet = new HashMap();
                                    whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                    whereAddIntdet.put("PRODID", lienPrdId);
                                    whereAddIntdet.put("CUSTID", getCustomerID());
                                    whereAddIntdet.put("PERIOD", diff);
                                    whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                    whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                    List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                    if (addInterest != null && addInterest.size() > 0) {
                                        HashMap intDet = (HashMap) addInterest.get(0);
                                        if (intDet != null && intDet.containsKey("ROI")) {
                                            String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));

                                            setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                        }
                                    }

                                    setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt, currDt));
                                }
                            }
                        } else {
                            setLblAddIntRtAmtVal("");
                            setLblMaturityPeriod("");
                            setLblAddIntrstRteVal("");
                            setAddIntLoanAmt("");
                        }
                    }
                }
            }
            //End on 22-04-14
            double paid = CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue();
            double payable = CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue();
            String payAmt = CommonUtil.convertObjToStr(getPayReceivable());
            //System.out.println("payable :: " + payable +" paid ::" + paid);
            if (payable > paid) { // Added by nithya on 06-02-2020 for KD-1090
                double tdsAmt = CommonUtil.convertObjToDouble(payAmt).doubleValue();
                closureData.put("TDS_CALCULATION", "TDS_CALCULATION");
                closureData.put("CUST_ID", getCustomerID());
                closureData.put("PROD_ID", getProdID());
                closureData.put("DEPOSIT_NO", getTxtDepositNo());
                closureData.put("RATE_OF_INT", getPrematureClosingRate());
                closureData.put("TDS_AMOUNT", new Double(tdsAmt));
                tdsData = proxy.executeQuery(closureData, map);
                if (tdsData != null && tdsData.size() > 0) {
                   // System.out.println("#######tdsData : " + tdsData);
                    setClosingTds(CommonUtil.convertObjToStr(tdsData.get("TDSDRAMT")));
                    setTdsAcHd(CommonUtil.convertObjToStr(tdsData.get("TDSCrACHdId")));
                } else {
                    setClosingTds("0.0");
                }
            } else {
                setClosingTds("0.0");
            }
            list = null;
            mapData = null;
            tdsData = null;
            closureData = null;
        } else {
            ClientUtil.displayAlert("This Deposit Account is Already Closed");
            resetDepositDetails();
            resetCustomerDetails();
        }
    }

    private void resetCustomerDetails() {
        this.setCustomerID("");
    }

    public void resetDepositDetails() {
        this.setConstitution("");
        this.setCategory("");
        this.setModeOfSettlement("");
        this.setDepositActName("");
    }

    public String calcAddIntAmt(Date startDt, Date currDt) {
        double prin_Amt = CommonUtil.convertObjToDouble(this.getPrinicipal());
        double addIntrst = CommonUtil.convertObjToDouble(this.getLblAddIntrstRteVal());
        String days = this.getLblMaturityPeriod().replace("Days", "").trim();
        int rem_days = CommonUtil.convertObjToInt(days);
        double AddIntAmt = 0;
        double totalMonths = 0;
        AddIntAmt = (prin_Amt * addIntrst * rem_days) / 36500;
        return CommonUtil.convertObjToStr(getRound(AddIntAmt, getInterestRound()));
        // return CommonUtil.convertObjToStr((prin_Amt * addIntrst * rem_days) / 36500);

    }

    public double getRound(double amount, String mode) {  // double result=0.0;
        System.out.println("in getRounddd " + amount + "  " + mode);
        if (mode != null && !mode.equals("")) {
            if (mode.equals("NO_ROUND_OFF")) {
                System.out.println(" in no round");
                String amt = df.format(amount);
                amount = Double.parseDouble(amt);
                System.out.println("interestAmt " + amount);
            } else if (mode.equals("NEAREST_VALUE")) {
                System.out.println(" in nearest roundingg");
                amount = (double) getNearest((long) (amount * 100), 100) / 100;
            } else if (mode.equals("HIGHER_VALUE")) {
                System.out.println("in higher valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    System.out.println("mode valuee  " + d % 1.0);
                    double c = d % 1.0;
                    d = d - c;
                    d = d + 1;
                    System.out.println("Higher valuuuee " + d);
                } else {
                    System.out.println("dsf  " + d % 1.0);
                    System.out.println("ggggg " + d);
                }
                amount = d;
                System.out.println("Higher valuuuee reall " + d);

                //interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            } else if (mode.equals("LOWER_VALUE")) {
                System.out.println("in lower round valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    System.out.println("mode valuee  " + d % 1.0);
                    double c = d % 1.0;
                    d = d - c;

                    System.out.println("Higher valuuuee " + d);
                } else {
                    System.out.println("dsf  " + d % 1.0);
                    System.out.println("ggggg " + d);
                }
                amount = d;
                System.out.println("Higher valuuuee reall " + d);

            } else {
                System.out.println(" in no round");
                amount = new Double(amount);
                System.out.println("interestAmt " + amount);
            }
        } else {
            System.out.println(" i else  in no round");
            amount = new Double(amount);
            System.out.println("interestAmt " + amount);

        }
        return amount;
    }

    public String calcAddIntAmtForLoan(Date startDt, Date currDt, String lienRoi, String lienAmt) {
        double addIntrst = CommonUtil.convertObjToDouble(this.getLblAddIntrstRteVal());
        String days = this.getLblMaturityPeriod().replace("Days", "").trim();
        int rem_days = CommonUtil.convertObjToInt(days);
        double lRoi = 0, lAmt = 0;
        if (lienRoi != null && !lienRoi.equals("")) {
            lRoi = CommonUtil.convertObjToDouble(lienRoi) + addIntrst;
        }
        if (lienAmt != null && !lienAmt.equals("")) {
            lAmt = CommonUtil.convertObjToDouble(lienAmt);
        }
        double loanAmt = (lAmt * lRoi * rem_days) / 36500;

        return CommonUtil.convertObjToStr(getRound(loanAmt, getInterestRound()));
    }

    private void setClosingFloatingDetails(HashMap mapData) {
        System.out.println("setClosingDetails : " + mapData);
        System.out.println("floating shi");
        HashMap custMap = new HashMap();
        double amount = 0.0;
        double principal = 0.0;
        double interest = 0.0;
        double interestAmt = 0.0;
        double intDrawn = 0.0;
        double intCredit = 0.0;
        double balIntAmt = 0.0;
        double totAmt = 0.0;
        double commPeriod = 0.0;
        double prematureMinPeriod = 0.0;
        int weeklycalcNo = 0;
        double interestNotPaying = 0.0;
        String interestNotPayingMode = "";
        HashMap withPeriodMap = new HashMap();
        if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITHOUT_PERIOD")) {
            withPeriodMap = (HashMap) mapData.get("DEPOSIT_CLOSE_DETAILS_MAP");
        } else {
            withPeriodMap = mapData;
        }
        custMap.put("DEPOSIT_NO", withPeriodMap.get("DEPOSITNO"));
        List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", custMap);
        if (list != null && list.size() > 0) {
            if (isRdoTransfer_Yes() == true) {
                transfer_out_mode = "Y";
            } else {
                transfer_out_mode = "N";
            }

            custMap = (HashMap) list.get(0);
            behavesMap.put("BEHAVES_LIKE", custMap.get("BEHAVES_LIKE"));
            String holidayProv = null;
            String commisionMode = null;
            setLstProvDt(CommonUtil.convertObjToStr(custMap.get("LST_PROV_DT")));
            interestNotPaying = CommonUtil.convertObjToDouble(custMap.get("INTEREST_NOT_PAYING")).doubleValue();
            interestNotPayingMode = CommonUtil.convertObjToStr(custMap.get("INTEREST_NOT_PAYING_MODE"));
            holidayProv = CommonUtil.convertObjToStr(custMap.get("PAYINT_DEP_MATURITY"));
            commisionMode = CommonUtil.convertObjToStr(custMap.get("AGENT_COMMISION_MODE"));
            setDeathClaimAmt(String.valueOf("0"));
            setTotalBalance(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
            double recurringAmount = CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue();
            intDrawn = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
            intCredit = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_CREDIT")).doubleValue();
            totAmt = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
            // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
            balIntAmt = getRound(balIntAmt, getInterestRound());
            //  intDrawn = (double)getNearest((long)(intDrawn *100),100)/100;
            intDrawn = getRound(intDrawn, getInterestRound());

            //  intCredit = (double)getNearest((long)(intCredit *100),100)/100;
            intCredit = getRound(intCredit, getInterestRound());
            balIntAmt = intCredit - intDrawn;
            this.setClosingIntCr(String.valueOf(balIntAmt));
            this.setIntDrawn(String.valueOf(intDrawn));
            this.setIntCr(String.valueOf(intCredit));
            this.setLastIntAppDate(CommonUtil.convertObjToStr(map.get("INT_LAST_APPL_DT")));
            this.setTdsCollected(CommonUtil.convertObjToStr(new Double(CommonUtil.convertObjToDouble(getTdsCollected()).doubleValue())));
            this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
            this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
            setBalanceAmt(CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue());
            setBalance(CommonUtil.convertObjToStr(custMap.get("CLEAR_BALANCE")));
            setDelayedInstallments("");
            setChargeAmount("");

            Date depDate = null;
            double interestAmount = 0.0;
            if (mapData.containsKey("FLOATING_TYPE")) {
                if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITHOUT_PERIOD")) {
                    List withoutPeriodDtList = (List) mapData.get("DEPOSIT_CLOSE_DETAILS_WITHOUT_PERIOD");
                    System.out.println("#$#%#$%withoutPeriodDtList:" + withoutPeriodDtList);
                    for (int i = 0; i < withoutPeriodDtList.size(); i++) {
                        HashMap withPeriodDt = (HashMap) withoutPeriodDtList.get(i);
                        Date nextInterestDt = null;
                        if (i + 1 < withoutPeriodDtList.size()) {
                            HashMap nextIntDtMap = (HashMap) withoutPeriodDtList.get(i + 1);
                            nextInterestDt = (Date) nextIntDtMap.get("ROI_DT");
                        }
                        Date interestDt = (Date) withPeriodDt.get("ROI_DT");
                        System.out.println("@#$%#$%interestDt:" + interestDt);
                        withPeriodMap.put("DEPOSIT_DT", interestDt);
                        withPeriodMap.put("AMOUNT", CommonUtil.convertObjToDouble(withPeriodMap.get("MATURITY_AMT")));
                        withPeriodMap.put("PROD_ID", withPeriodMap.get("PRODUCT_ID"));
                        double maturityAmount = CommonUtil.convertObjToDouble(withPeriodMap.get("AMOUNT")).doubleValue();
                        setBalanceDeposit(String.valueOf(maturityAmount));
                        System.out.println("#$#%#$%withPeriodMap:" + withPeriodMap);
                        List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                        if (lstInt != null && lstInt.size() > 0) {
                            HashMap lstIntMap = (HashMap) lstInt.get(0);
                            System.out.println("#$%#$%lstIntMap: " + i + " : " + lstIntMap);
                            double roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                            double noOfDays = 0;
                            if (nextInterestDt != null) {
                                noOfDays = DateUtil.dateDiff(interestDt, nextInterestDt);
                            }
                            interestAmount += (roi * maturityAmount * noOfDays) / 36500;
                            System.out.println("@#$%#$%interestAmount" + interestAmount);
                            // interestAmount = (double)getNearest((long)(interestAmount *100),100)/100;
                            interestAmount = getRound(interestAmount, getInterestRound());
                            System.out.println("jjjjjj9999999999999999999");
                            setClosingIntDb(String.valueOf(interestAmount));
                            double closingDisbursal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue() + interestAmount;
                            setClosingDisbursal(String.valueOf(closingDisbursal));
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(interestAmount));
                            this.setPermanentPayReceivable(String.valueOf(interestAmount));

                        }
                    }
                    System.out.println("@#$%#$%interestAmountfinal" + interestAmount);
                } else if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITH_PERIOD")) {
                    System.out.println("$%#$%$inside with period:" + withPeriodMap);
                    Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("DEPOSIT_DT")));
                    Date closingDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("CLOSING_DT")));
                    Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("MATURITY_DT")));

                    //                        for normal closure, i.e.,within the period
                    withPeriodMap.put("DEPOSIT_DT", depositDate);
                    withPeriodMap.put("AMOUNT", CommonUtil.convertObjToDouble(withPeriodMap.get("MATURITY_AMT")));
                    withPeriodMap.put("PROD_ID", withPeriodMap.get("PRODUCT_ID"));
                    withPeriodMap.put("PERIOD", new Double(DateUtil.dateDiff(depositDate, maturityDate)));
                    double maturityAmount = CommonUtil.convertObjToDouble(withPeriodMap.get("AMOUNT")).doubleValue();
                    setBalanceDeposit(String.valueOf(maturityAmount));
                    double roi = 0.0;
                    double penal = 0.0;
                    List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                    if (lstInt != null && lstInt.size() > 0) {
                        HashMap lstIntMap = (HashMap) lstInt.get(0);
                        System.out.println("#$%#$%lstIntMap: " + lstIntMap);
                        roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(lstIntMap.get("PENAL_INT")).doubleValue();
                    }
                    int period = 0;
                    if (closingDate.after(maturityDate) || DateUtil.dateDiff(closingDate, maturityDate) == 0) {
//                        System.out.println("!!!! Closing date is after maturity date is true");
                        period = (int) DateUtil.dateDiff(depositDate, maturityDate);
                    } else if (closingDate.before(maturityDate)) {

//                        System.out.println("!!!! Closing date is before maturity date is true");
                        period = (int) DateUtil.dateDiff(depositDate, closingDate);
//                        double penal = CommonUtil.convertObjToDouble(custMap.get("PENAL_INT")).doubleValue();
                        String closingPenal = CommonUtil.convertObjToStr(custMap.get("PENAL_INT"));
                        String closingRoi = CommonUtil.convertObjToStr(custMap.get("ROI"));
                        this.setRateApplicable(String.valueOf(roi));
                        if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                            withPeriodMap.put("PERIOD", new Double(period));
                            lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                            if (lstInt != null && lstInt.size() > 0) {
                                HashMap lstIntMap = (HashMap) lstInt.get(0);
                                System.out.println("#$%#$%lstIntMap: " + lstIntMap);
                                roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                                penal = CommonUtil.convertObjToDouble(lstIntMap.get("PENAL_INT")).doubleValue();
                            }



                            System.out.println("getPremClosNNNNN" + getPremClos());
                            this.setRateApplicable(String.valueOf(roi));
                            //if (isRdoYesButton()) {
                            //  this.setPenaltyPenalRate(String.valueOf(penal));
                            //  setPrematureClosingRate(String.valueOf(roi - penal));
                            //  roi = roi - penal;
                            //} else if (isRdoNoButton()) {
                            setPrematureClosingRate(String.valueOf(roi));
                            this.setPenaltyPenalRate(String.valueOf(0.0));
                            // }

                        } else if (getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                            setPrematureClosingRate(closingRoi);
                            this.setPenaltyPenalRate(String.valueOf(0.0));
                            roi = new Double(closingRoi).doubleValue();
                        }
                    }
                    interestAmount += (roi * maturityAmount * period) / 36500;
                    System.out.println("interest for the 1st period:" + interestAmount);
                    if (closingDate.after(maturityDate)) {
                        Date startDt = (Date) maturityDate.clone();
                        Date endDt = (Date) maturityDate.clone();
                        int noOfDays = 0;
                        int i = 2;
                        while (DateUtil.dateDiff(endDt, closingDate) > 0) {
                            endDt = DateUtil.addDays(endDt, period, true);
                            if (DateUtil.dateDiff(endDt, closingDate) > 0) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, endDt);
                            } else {
                                noOfDays = (int) DateUtil.dateDiff(startDt, closingDate);
                            }
                            withPeriodMap.put("DEPOSIT_DT", startDt);
//                                withPeriodMap.put("PERIOD", new Double(noOfDays));
                            System.out.println("#$#%#$%withPeriodMap inside while loop:" + withPeriodMap);
                            List lstInterst = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                            if (lstInterst != null && lstInterst.size() > 0) {
                                HashMap lstInterstMap = (HashMap) lstInterst.get(0);
                                System.out.println("#$%#$%lstInterstMap: " + i + " : " + lstInterstMap);
                                roi = CommonUtil.convertObjToDouble(lstInterstMap.get("ROI")).doubleValue();
                                interestAmount += (roi * maturityAmount * noOfDays) / 36500;
                                System.out.println("@#$%#$%interestAmount for " + i + " Period :" + roi + "*" + maturityAmount + "*"
                                        + +noOfDays + "/" + ")/36500 :" + (roi * maturityAmount * noOfDays) / 36500);
                                System.out.println("@#$%#$%tot interestAmount for " + i + " Period :" + interestAmount);
                            }
                            startDt = (Date) endDt.clone();
                            i++;
                        }
                    }
                    //interestAmount = (double)getNearest((long)(interestAmount *100),100)/100;
                    interestAmount = getRound(interestAmount, getInterestRound());
                    System.out.println("jjjjjj10000000000000000");
                    setClosingIntDb(String.valueOf(interestAmount));
                    double closingDisbursal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue() + interestAmount;
                    setClosingDisbursal(String.valueOf(closingDisbursal));
                    setLblPayRecDet("Payable");
                    this.setPayReceivable(String.valueOf(interestAmount));
                    this.setPermanentPayReceivable(String.valueOf(interestAmount));
                }
            }

            setPenaltyInt(CommonUtil.convertObjToStr(custMap.get("PENAL_INT")));
        }
        custMap = null;
        list = null;
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            notifyObservers();
        }
    }

    private void setClosingDetails(HashMap mapData) {
        System.out.println("setClosingDetails : " + mapData);
        System.out.println("closing shi");
        if (mapData.containsKey("INTEREST_DETAILS")) {
            HashMap interestCalcMap = (HashMap) mapData.get("INTEREST_DETAILS");
            HashMap intDrawnMap = (HashMap) mapData.get("INTEREST_DRAWN");
            HashMap custMap = new HashMap();
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            double interestAmt = 0.0;
            double intDrawn = 0.0;
            double intCredit = 0.0;
            double balIntAmt = 0.0;
            double totAmt = 0.0;
            double commPeriod = 0.0;
            double prematureMinPeriod = 0.0;
            int weeklycalcNo = 0;
            double interestNotPaying = 0.0;
            String interestNotPayingMode = "";
            custMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO"));
            List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", custMap);
            if (list != null && list.size() > 0) {
                custMap = (HashMap) list.get(0);
                behavesMap.put("BEHAVES_LIKE", custMap.get("BEHAVES_LIKE"));
                System.out.println("doubling scheme : "+DOUBLING_SCHEME_BEHAVES_LIKE+"behvs like "+custMap.get("BEHAVES_LIKE"));
                if (DOUBLING_SCHEME_BEHAVES_LIKE != null && DOUBLING_SCHEME_BEHAVES_LIKE.length()>0 && !DOUBLING_SCHEME_BEHAVES_LIKE.equals("") && CommonUtil.convertObjToStr(custMap.get("BEHAVES_LIKE")).equals("CUMMULATIVE")
                        && getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    behavesMap.put("BEHAVES_LIKE", DOUBLING_SCHEME_BEHAVES_LIKE);
                }
                String holidayProv = null;
                String commisionMode = null;
                //                HashMap holidayProvMap = new HashMap();
                //                holidayProvMap.put("PROD_ID",custMap.get("PROD_ID"));
                //                List holidayList = ClientUtil.executeQuery("getHolidayInterestYesNo", holidayProvMap);
                //                if(holidayList!=null && holidayList.size()>0){
                //                    holidayProvMap = (HashMap)holidayList.get(0);
                //                }
                setLstProvDt(CommonUtil.convertObjToStr(custMap.get("LST_PROV_DT")));
                interestNotPaying = CommonUtil.convertObjToDouble(custMap.get("INTEREST_NOT_PAYING")).doubleValue();
                interestNotPayingMode = CommonUtil.convertObjToStr(custMap.get("INTEREST_NOT_PAYING_MODE"));
                holidayProv = CommonUtil.convertObjToStr(custMap.get("PAYINT_DEP_MATURITY"));
                commisionMode = CommonUtil.convertObjToStr(custMap.get("AGENT_COMMISION_MODE"));
                setDeathClaimAmt(String.valueOf("0"));
                setTotalBalance(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                double recurringAmount = CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue();
                intDrawn = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
                intCredit = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_CREDIT")).doubleValue();
                totAmt = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                setPrinicipal(CommonUtil.convertObjToStr(custMap.get("CLEAR_BALANCE")));
                setBalance(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                setMaturityDate(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                System.out.println("bal innntt in ob" + balIntAmt + "  f  " + intDrawn + " rg " + intCredit);
                // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                balIntAmt = getRound(balIntAmt, getInterestRound());
                //  intDrawn = (double)getNearest((long)(intDrawn *100),100)/100;
                intDrawn = getRound(intDrawn, getInterestRound());
                // intCredit = (double)getNearest((long)(intCredit *100),100)/100;
                intCredit = getRound(intCredit, getInterestRound());
                balIntAmt = intCredit - intDrawn;
                this.setClosingIntCr(String.valueOf(balIntAmt));
                this.setIntDrawn(String.valueOf(intDrawn));
                this.setIntCr(String.valueOf(intCredit));
                this.setLastIntAppDate(CommonUtil.convertObjToStr(map.get("INT_LAST_APPL_DT")));
                this.setTdsCollected(CommonUtil.convertObjToStr(new Double(CommonUtil.convertObjToDouble(getTdsCollected()).doubleValue())));
                this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
                this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
                setBalanceAmt(CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue());
                setDelayedInstallments("");
                setChargeAmount("");
                Date depDate = null;
                double totalMonths = 0.0;
                double holidayAmt = 0.0;
                //                System.out.println("######bal : "+getBalanceAmt());
                //                String behave = CommonUtil.convertObjToStr(getProdID());
                //                HashMap behavesLikeMap = new HashMap();
                //                behavesLikeMap.put("PROD_ID", behave);
                //                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit",  behavesLikeMap);
                //                if(lst != null && lst.size() > 0) {
                //                    behavesMap = new HashMap();
                //                    behavesMap = (HashMap)lst.get(0);
                //After Maturing Deposit we have to calculate Operative Account Interest Rate we have to give the customer
                double rateOfInt = 0.0;
                double difference = 0.0;
                double uncompletedInterest = 0.0;
                double completedInterest = 0.0;
                double monthPeriod = 0.0;
                double yearPeriod = 0.0;
                long diffDay = 0;
                double freq = CommonUtil.convertObjToDouble(custMap.get("INTPAY_FREQ")).doubleValue();
                HashMap objMap = new HashMap();
                objMap.put("MODE", getLblClosingType());
                objMap.put("COMMAND", getCommand());
                System.out.println("command here shi" + getCommand());
                System.out.println("mode here ad"+getLblClosingType()+"and behvs map: "+behavesMap);
                if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                    depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                    Date currDate = (Date) curDate.clone();
                    Date depDt = (Date) curDate.clone();
                    if (depDate.getDate() > 0) {
                        depDt.setDate(depDate.getDate());
                        depDt.setMonth(depDate.getMonth());
                        depDt.setYear(depDate.getYear());
                    }
                    HashMap prematureDateMap = new HashMap();
                    double period = 0;
                    String temp;
                    String mthPeriod = null;
                    String yrPeriod = null;
                    String dysPeriod = null;
                    double completedQuarter = 0.0;
                    int count = 0;
                    StringBuffer periodLetters = new StringBuffer();
                    if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {
                        periodLetters.append(String.valueOf(0) + " Years ");
                        prematureDateMap.put("FROM_DATE", depDt);
                        prematureDateMap.put("TO_DATE", currDate);
                        Date deptDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        List lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                        if (lst != null && lst.size() > 0) {
                            prematureDateMap = (HashMap) lst.get(0);
                            monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                            totalMonths = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                            diffDay = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                        }
                        temp = "0";
                        mthPeriod = String.valueOf(monthPeriod);
                        if (mthPeriod != null) {
                            temp = mthPeriod.toString();
                        }
                        periodLetters.append(mthPeriod + " Months ");
                        //                        diffDay = DateUtil.dateDiff(depDt,currDate);
                    } else {
                        System.out.println("inside else part");
                        prematureDateMap.put("FROM_DATE", depDt);
                        prematureDateMap.put("TO_DATE", currDate);
                        int j = 0;
                        Date nextDate = depDt;
                        Date k = DateUtil.nextCalcDate(depDt, depDt, 30);
                        for (; DateUtil.dateDiff(k, currDate) > 0; k = DateUtil.nextCalcDate(depDt, k, 30)) {
                            j = j + 1;
                            nextDate = k;
                        }
                        monthPeriod = j;
                        totalMonths = j;
                        diffDay = DateUtil.dateDiff(nextDate, currDate);
                        if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {  
                            Date deptDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            List lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                            if (lst != null && lst.size() > 0) {
                                prematureDateMap = (HashMap) lst.get(0);
                                monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                totalMonths = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                diffDay = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                            }
                            long totCompPeriod = 0;
                            if (freq == 30) {
                                yrPeriod = String.valueOf(completedQuarter);
                                mthPeriod = String.valueOf(monthPeriod);
                            } else if (freq == 90) {
                                completedQuarter = monthPeriod / 3;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 3);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 3);
                                totalMonths = totCompPeriod;
                            } else if (freq == 180) {
                                completedQuarter = monthPeriod / 6;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 6);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 6);
                                totalMonths = totCompPeriod;
                            } else if (freq == 360) {
                                completedQuarter = monthPeriod / 12;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 12);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 12);
                                totalMonths = totCompPeriod;
                            } else {
                                yrPeriod = String.valueOf(completedQuarter);
                                mthPeriod = String.valueOf("0.0");
                                totalMonths = 0; // Changed 27-jan-2012
                                diffDay = DateUtil.dateDiff(depDt, currDate);
                            }
                            if ((monthPeriod != 0) && (freq == 90 || freq == 180 || freq == 360)) {
                                for (int i = 0; i < totCompPeriod; i++) {
                                    if (deptDate.getDate() == 28) {
                                        deptDate = DateUtil.addDays(deptDate, 30, 2, true);
                                    } else {
                                        deptDate = DateUtil.addDays(deptDate, 30);
                                    }
                                }
                                diffDay = DateUtil.dateDiff(deptDate, currDate);
                                mthPeriod = String.valueOf("0.0");
                            }
                        } 
                        else if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")
                                || behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {  
                            System.out.println("CUMMULATIVE jjjj1");
                            System.out.println("month period : "+monthPeriod);
                            if (monthPeriod >= 3) {
                                System.out.println("2222222222222222");
                                completedQuarter = monthPeriod / 3;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 3);
                                if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                    completedQuarter = completedQuarter * 3;
                                    Date cummDepDate = null;
                                    cummDepDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                    for (int i = 0; i < completedQuarter; i++) {
                                        if (cummDepDate.getDate() == 28) {
                                            cummDepDate = DateUtil.addDays(cummDepDate, 30, 2, true);
                                        } else {
                                            cummDepDate = DateUtil.addDays(cummDepDate, 30);
                                        }
                                    }
                                    diffDay = DateUtil.dateDiff(cummDepDate, currDate);
                                    mthPeriod = String.valueOf("0.0");
                                }
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    mthPeriod = String.valueOf(monthPeriod);
                                }
                            } else {
                                yrPeriod = "0.0";
                                if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                    Date cummDepDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                    diffDay = DateUtil.dateDiff(cummDepDate, currDate);
                                    mthPeriod = String.valueOf("0.0");
                                }
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    mthPeriod = String.valueOf(monthPeriod);
                                }
                                //                                mthPeriod = String.valueOf(monthPeriod);
                            }
                        }
                        temp = "0";
                        if (yrPeriod != null) {
                            temp = yrPeriod.toString();
                        }
                        if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                            if (freq == 90) {
                                periodLetters.append(yrPeriod + " Qters ");
                            } else if (freq == 180) {
                                periodLetters.append(yrPeriod + " Half Yearly ");
                            } else if (freq == 360) {
                                periodLetters.append(yrPeriod + " Yearly ");
                            } else {
                                periodLetters.append(yrPeriod + " Qters ");
                            }
                        } else {
                            periodLetters.append(yrPeriod + " Qters ");
                        }
                        temp = "0";
                        if (mthPeriod != null) {
                            temp = mthPeriod.toString();
                        }
                        periodLetters.append(mthPeriod + " Months ");
                    }
                    dysPeriod = String.valueOf(diffDay);
                    temp = "0";
                    if (dysPeriod != null) {
                        temp = dysPeriod.toString();
                    }
                    periodLetters.append(dysPeriod + " Days ");
                    setActualPeriodRun(periodLetters.toString());
                } else {
                    setActualPeriodRun(CommonUtil.convertObjToStr(getPeriod()));
                }
                double interestgreater = 0.0;

                if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                    //babu
                    Date depositedDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                    Date currentDate = (Date) curDate.clone();
                    long diffDD = DateUtil.dateDiff(depositedDate, currentDate);//bbb
                    System.out.println("diffDD IN====" + diffDD);
//                    if (isRdoTransfer_Yes() == true) {
//                        transfer_out_mode = "Y";
//                        this.setIntDrawn(getIntDrawn());
//                        System.out.println("interest in ob1" + interest);
//                        // interest = (double)getNearest((long)(interest *100),100)/100;
//                        interest = getRound(interest, getInterestRound());
//                        //    balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
//                        balIntAmt = getRound(balIntAmt, getInterestRound());
//                        System.out.println("jjjjjjj123========");
//                        setClosingIntDb(String.valueOf(interest));
//                        setClosingIntCr(String.valueOf(balIntAmt));
//                        setClosingDisbursal(getPrinicipal()); //Maturity Amt
//                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
//                        if (getDisburseAmt() > principal) {
//                            setLblPayRecDet("Payable");
//                            this.setPayReceivable(String.valueOf(0.0));
//                            this.setPermanentPayReceivable(String.valueOf(0.0));
//                        } else {
//                            setLblPayRecDet("Receivable");
//                            this.setPayReceivable(String.valueOf(0.0));
//                            this.setPermanentPayReceivable(String.valueOf(0.0));
//                        }
//                        if (totAmt == intDrawn) {
//                            this.setClosingDisbursal(getBalance());
//                            this.setPayReceivable(String.valueOf(0.0));
//                            this.setPermanentPayReceivable(String.valueOf(0.0));
//                        }
//                    }

                    // else {
                    System.out.println("jjjjjj22222222222222222");
                    this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(intDrawnMap.get("INT_AMT")).doubleValue()));
                    System.out.println("getBalance-----------------  :" + getBalance());
                    System.out.println("getPrinicipal()--------------- :" + getPrinicipal());
                    System.out.println("getPrinicipal()--------------- :" + getPrematureClosingRate());
                    principal = new Double(getBalance().length() > 1 ? getPrinicipal() : "0").doubleValue();
                    double rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    this.setMaturityValue(getPrinicipal());
                    freq = CommonUtil.convertObjToDouble(custMap.get("INTPAY_FREQ")).doubleValue();
                    double matAmt = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                    double period = 0.0;
                    System.out.println("bbxvcvxbcvb nzvxcbzx :" + objMap.get("MODE") + " :" + getLblClosingType());
                    if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        transfer_out_mode = "N";
                        Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        Date currDate = (Date) curDate.clone();
                        period = DateUtil.dateDiff(fromDate, currDate);
                        Date deathDt = null;
                        String category = "";
                        String discounted = CommonUtil.convertObjToStr(custMap.get("DISCOUNTED_RATE"));
                        HashMap hmap = new HashMap();
                        hmap.put("CUST_ID", getCustomerID());
                        hmap.put("DEPOSIT_NO", getTxtDepositNo());
                        List seniorList = ClientUtil.executeQuery("getSeniorDetails", hmap);
                        if (seniorList != null && seniorList.size() > 0) {
                            hmap = (HashMap) seniorList.get(0);
                            category = CommonUtil.convertObjToStr(hmap.get("CATEGORY"));
                            deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
                        }
                        if (category.equals("SENIOR_CITIZENS") && custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            //                                ClientUtil.showMessageWindow(""
                            if (DateUtil.dateDiff(deathDt, DateUtil.getDateMMDDYYYY(getMaturityDate())) > 0) {
                                HashMap calculationMap = new HashMap();
                                calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                calculationMap.put("DEATH_DT", deathDt);
                                calculationMap.put("RATE_OF_INTEREST", new Double(rateOfInterest));
                                calculationMap.put("NORMAL", "PREMATURE");
                                interest = CalculationForSeniorCitizenDeath(calculationMap);
                            }
                            System.out.println("interest senior IN====" + interest);
                        } else {
                            if (freq == 30 && discounted != null && discounted.equals("Y")) {
                                double intAmt = 0.0;
                                System.out.println("totalMonths IN====" + totalMonths);
                                if (totalMonths > 0) {
                                    intAmt = rateOfInterest / 4 / (Math.pow((1 + (rateOfInterest / 1200)), 2) + (1 + (rateOfInterest / 1200)) + 1);//bb
                                    //   intAmt = rateOfInterest/4 / (Math.pow((1+(rateOfInterest/36500)),2) + (1+(rateOfInterest/36500)) +1);
                                    double calcAmt = principal / 100;
                                    intAmt = intAmt * calcAmt;
                                    // intAmt = (double)getNearest((long)(intAmt *100),100)/100;
                                    intAmt = getRound(intAmt, getInterestRound());
                                    intAmt = intAmt * totalMonths;//bb
                                    //    intAmt = intAmt * diffDD;
                                }
                                if (diffDay > 0) {
                                    principal = principal;// + intAmt;
                                    amount = principal + (principal * rateOfInterest * diffDay) / (36500);//bbb
                                    interestgreater = amount - principal;
                                }
                                interest = intAmt + interestgreater;
                            } else if (freq == 0 && diffDay > 0 && discounted != null && discounted.equals("Y")) {
                                double lessamount = principal + (principal * rateOfInterest * diffDay) / (36500);
                                interest = lessamount - principal;

                            } else {
                                if (totalMonths > 0 || diffDay > 0) {
                                    // double greateramount = principal+(principal * rateOfInterest * totalMonths) /1200;
                                    System.out.println("principal IN====" + principal + " rateOfInterest==" + rateOfInterest + " diffDay==" + diffDay);
                                    double greateramount = principal + (principal * rateOfInterest * diffDD) / 36500;//bbb
                                    interestgreater = greateramount - principal;
                                }
                                double interestless = 0.0;
                                /*
                                if(diffDay>0){
                                System.out.println("principal11 IN===="+principal+" rateOfInterest="+rateOfInterest+" diffDay="+diffDay);
                                double lessamount = principal+(principal * rateOfInterest * diffDay) /(36500);
                                interestless = lessamount - principal;
                                }*/
                                interest = interestgreater + interestless;

                            }
                        }
                    } else {
                        transfer_out_mode = "NO";
                        interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                        Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                        amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        Date matDt = (Date) curDate.clone();
                        Date deathDt = null;
                        Date dob = null;
                        if (matDate.getDate() > 0) {
                            matDt.setYear(matDate.getYear());
                            matDt.setMonth(matDate.getMonth());
                            matDt.setDate(matDate.getDate());
                        }
                        holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);

                        HashMap hmap = new HashMap();
                        hmap.put("CUST_ID", getCustomerID());
                        hmap.put("DEPOSIT_NO", getTxtDepositNo());
                        List seniorList = ClientUtil.executeQuery("getSeniorDetails", hmap);
                        if (seniorList != null && seniorList.size() > 0) {
                            hmap = (HashMap) seniorList.get(0);
                            category = CommonUtil.convertObjToStr(hmap.get("CATEGORY"));
                            deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
                        }
                        if (category.equals("SENIOR_CITIZENS") && custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            //                                ClientUtil.showMessageWindow(""
                            if (DateUtil.dateDiff(deathDt, DateUtil.getDateMMDDYYYY(getMaturityDate())) > 0) {
                                HashMap calculationMap = new HashMap();
                                calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                calculationMap.put("DEATH_DT", deathDt);
                                calculationMap.put("RATE_OF_INTEREST", new Double(rateOfInterest));
                                calculationMap.put("NORMAL", "NORMAL");

                                interest = CalculationForSeniorCitizenDeath(calculationMap);
                            }
                        }

                        if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            HashMap calculationMap = new HashMap();
                            calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                            calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                            calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                            calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                            calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                            double deathAmt = simpleInterestCalculation(calculationMap);
                            interest = interest + deathAmt;
                            setDeathClaim("Y");
                            calculationMap = null;
                        }
                    }
                    this.setIntDrawn(getIntDrawn());
                    interest = interest + holidayAmt;
                    //  interest = (double)getNearest((long)(interest *100),100)/100;
                    interest = getRound(interest, getInterestRound());
                    if (holidayAmt > 0) {
//                        ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                    }

                    double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                    if (availableBal == 0) {
                        setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                        this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                        this.setPermanentPayReceivable(String.valueOf(0.0));
                        setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                        ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                    } else {
                        //  balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj333333333333333");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(
                                CommonUtil.convertObjToStr(new Double(
                                CommonUtil.convertObjToDouble(getBalance()).doubleValue()
                                + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()))); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                    - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));

                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                    - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()
                                    - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));

                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()
                                    - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                        if (negValue < 0) {
                            negValue = negValue * -1;
                            this.setPayReceivable(String.valueOf(negValue));
                            this.setPermanentPayReceivable(String.valueOf(negValue));
                        }
                        if (CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue() == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                    }
                    // }
                } else if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    setLblBalanceDeposit("Installment Amount");
                    this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                    double rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    this.setBalanceAmt(recurringAmount);
                    long period = 0;
                    int count = 0;
                    long diff = 0;
                    double rdApplyRate = 0.0;
                    String rdApply = "";

                    Date mat = null;
                    Date frm = DateUtil.getDateMMDDYYYY(getDepositDate());
                    if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        mat = (Date) curDate.clone();
                    } else {
                        mat = DateUtil.getDateMMDDYYYY(getMaturityDate());
                    }

                    periodNoOfDays = DateUtil.dateDiff(frm, mat);
                    period = periodNoOfDays - diff;
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        principal = new Double(getBalance().length() > 1 ? getBalance() : "0").doubleValue();
                        //                            System.out.println("######interest : "+this.getClosingIntDb());
                        //                            System.out.println("######interest : "+interest);
                        this.setIntDrawn(getIntDrawn());
//                        interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj44444444444444");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getBalance()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        //                            System.out.println("######DisburseAmt : "+getDisburseAmt());
                        //                            System.out.println("###### principal: "+principal);
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                        //                            System.out.println("intPayFreq :"+custMap.get("INTPAY_FREQ"));
                    } else {
                        //                            System.out.println("######recurring this.setBalanceAmt : "+this.getMaturityValue());
                        //                            System.out.println("###### periodNoOfDays : "+periodNoOfDays);
                        //                        if(periodNoOfDays >=46){
                        double yrPeriod = 0.0;
                        double mnPeriod = 0.0;
                        double diffDays = 0.0;
                        double calcIntAmt = 0.0;
                        double balSimpAmt = 0.0;
                        double amount1 = 0.0;
                        double fdMonths = 0.0;
                        double greateramount = 0.0;
                        double compoundInt = 0.0;
                        double interestless = 0.0;
                        double calcFDMonthSecondIntAmt = 0.0;
                        double calcFDDaysSecondIntAmt = 0.0;
                        double calcMnIntAmt = 0.0;
                        double calcFDMonthIntAmt = 0.0;
                        double calcFDDaysIntAmt = 0.0;
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {//Premature closure calculation..
                            transfer_out_mode = "N";
                            long totInstallments = CommonUtil.convertObjToLong(custMap.get("TOTAL_INSTALL_PAID"));
                            if (totInstallments >= 3) { // if installment is more than three means calcualting for recurring formula...
                                if (totInstallments > totalMonths) {
                                    period = (long) totalMonths / 3;
                                } else {
                                    period = totInstallments / 3;
                                }
                                period = (long) roundOffLower((long) (period * 100), 100) / 100;
                                amount = principal * (Math.pow((1 + rateOfInterest / 400), period) - 1) / (1 - Math.pow((1 + rateOfInterest / 400), -1 / 3.0));
                                amount1 = amount;
                                if (totInstallments > totalMonths) {
                                    mnPeriod = totalMonths - (period * 3);
                                } else {
                                    mnPeriod = totInstallments - (period * 3);
                                }
                                interest = amount - (principal * period * 3);
                                Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                //                                    Date nextDate = fromDate;
                                //                                    Date k = DateUtil.nextCalcDate(fromDate,fromDate,30);
                                //                                    for(int i=0; i<period; i++)
                                //                                        fromDate = DateUtil.nextCalcDate(fromDate,k, 30);
                                //                                    diffDay = DateUtil.dateDiff(nextDate, currDate);
                                period = period * 3;
                                for (int i = 0; i < period; i++) {
                                    fromDate = DateUtil.addDays(fromDate, 30);
                                }
                                Date currDate = (Date) curDate.clone();
                                mnPeriod = totInstallments - period;
                                HashMap dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                double dayendBalance = 0.0;
                                double dayendBal = 0.0;
                                if (mnPeriod > 0 && mnPeriod <= 2) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period + 1));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        //                                            diffDays = DateUtil.dateDiff(dueDate,firstTransDt);
                                        //                                            if(diffDays>0){
                                        //                                                principal = new Double(getPrinicipal().length()>1 ? getPrinicipal() : "0").doubleValue();
                                        //                                                if(mnPeriod == 2)
                                        //                                                    principal = principal * (totInstallments-mnPeriod);
                                        //                                                else
                                        //                                                    principal = principal * (totInstallments - 1);
                                        //                                                greateramount = dayendBalance+(dayendBalance * rateOfInterest * diffDays) /(36500);
                                        //                                                calcMnIntAmt = greateramount - dayendBal;
                                        //                                            }
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(period + 2));
                                            lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstRecord != null && lstRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - dayendBal;
                                                }
                                                for (int i = 0; i < mnPeriod; i++) {
                                                    fromDate = DateUtil.addDays(fromDate, 30);
                                                }
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                System.out.println("###### : " + DateUtil.dateDiff(secondTransDt, dueDate));
                                                if (secondTransDt != null && (DateUtil.dateDiff(secondTransDt, dueDate) > 0)) {
                                                    secondTransDt = dueDate;
                                                } else {
                                                    secondTransDt = secondTransDt;
                                                }
                                                if (DateUtil.dateDiff(firstTransDt, secondTransDt) == 0) {
                                                    firstTransDt = fromDate;
                                                }
                                                dueDate = secondTransDt;
                                                diffDays = DateUtil.dateDiff(dueDate, currDate);
                                                dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                                dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                dayendMap.put("DAY_END_DT", secondTransDt);
                                                lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                if (lstDay != null && lstDay.size() > 0) {
                                                    dayendMap = (HashMap) lstDay.get(0);
                                                    dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                    dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                }
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                }
                                            }
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                            }
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                } else if (mnPeriod == 0) {//incase only 3 installments paid,from 3rd inst paid dt to till now simple interest.
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        dueDate = DateUtil.nextCalcDate(dueDate, dueDate, 30);
                                        //                                            dueDate = DateUtil.addDays(dueDate, 30);
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                        dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        if (diffDays > 0) {
                                            greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                            calcFDDaysIntAmt = greateramount - dayendBal;
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                } else if (mnPeriod >= 3) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period + 1));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        dayendMap = new HashMap();
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap = new HashMap();
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(period + 2));
                                            lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstRecord != null && lstRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, secondTransDt) > 0)) {
                                                    secondTransDt = dueDate;
                                                } else {
                                                    secondTransDt = secondTransDt;
                                                }
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - dayendBal;
                                                }
                                                dayendMap = new HashMap();
                                                dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                dayendMap.put("DAY_END_DT", secondTransDt);
                                                lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                if (lstDay != null && lstDay.size() > 0) {
                                                    dayendMap = (HashMap) lstDay.get(0);
                                                    dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                    dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                }
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthIntAmt = greateramount - dayendBal;
                                                }
                                                mnPeriod = mnPeriod - 1;
                                                if (mnPeriod > 0) {
                                                    for (int i = 0; i < mnPeriod; i++) {
                                                        fromDate = DateUtil.addDays(fromDate, 30);
                                                    }
                                                    Date thirdTransDt = null;
                                                    depositRecMap = new HashMap();
                                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                    depositRecMap.put("START_DT", fromDate);
                                                    depositRecMap.put("SL_NO", new Double(period + 3));
                                                    lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                    if (lstRecord != null && lstRecord.size() > 0) {
                                                        depositRecMap = (HashMap) lstRecord.get(0);
                                                        dayendMap = new HashMap();
                                                        thirdTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                        dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                        dayendMap.put("DAY_END_DT", thirdTransDt);
                                                        if (thirdTransDt == null) {
                                                            thirdTransDt = dueDate;
                                                        }
                                                        if (firstTransDt != null && (DateUtil.dateDiff(secondTransDt, thirdTransDt) > 0)) {
                                                            thirdTransDt = dueDate;
                                                        } else {
                                                            thirdTransDt = thirdTransDt;
                                                        }
                                                        lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                        if (lstDay != null && lstDay.size() > 0) {
                                                            dayendMap = (HashMap) lstDay.get(0);
                                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                        }
                                                        diffDays = DateUtil.dateDiff(thirdTransDt, curDate);
                                                        if (diffDays > 0) {
                                                            greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                            calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                        }
                                                    }
                                                } else {
                                                    diffDays = DateUtil.dateDiff(secondTransDt, currDate);
                                                    if (diffDays > 0) {
                                                        greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                    }
                                                }
                                            }
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                            }
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                }
                                interest += calcFDDaysIntAmt + calcFDMonthIntAmt + calcFDDaysSecondIntAmt + calcFDMonthSecondIntAmt + calcMnIntAmt;
                            } else { // if installment is less than three means calcualting for simple interest formula...
                                Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date currDate = (Date) curDate.clone();
                                mnPeriod = totInstallments;
                                if (mnPeriod > 0) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    if (totInstallments == 2) {
                                        totInstallments = totInstallments - 1;
                                    }
                                    depositRecMap.put("SL_NO", new Double(totInstallments));
                                    List lstFirstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstFirstRecord != null && lstFirstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstFirstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(totInstallments + 1));
                                            List lstSecondRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstSecondRecord != null && lstSecondRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstSecondRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - principal;
                                                }
                                                diffDays = DateUtil.dateDiff(secondTransDt, currDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    principal = principal * 2;
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthIntAmt = greateramount - principal;
                                                }
                                            }
                                            lstSecondRecord = null;
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                calcFDDaysIntAmt = greateramount - principal;
                                            }
                                        }
                                    }
                                    depositRecMap = null;
                                    lstFirstRecord = null;
                                }
                                interest = calcFDDaysIntAmt + calcFDMonthIntAmt;
                            }
                            //                                    System.out.println("Pre Mature uncompletedInterest : "+uncompletedInterest +"balSimpAmt :"+balSimpAmt+"interest :"+interest);
                        } else {//Normal closure calculation...DOWN CODING R ALL COREECT DONT CHANGE...
                            transfer_out_mode = "NO";
                            Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
                            int periodYear = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_YY"));
                            int periodMonth = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_MM"));
                            int totalInstall = CommonUtil.convertObjToInt(custMap.get("TOTAL_INSTALL_PAID"));
                            //                                    System.out.println("##### period year : "+periodYear);
                            if (periodYear >= 1) {
                                periodMonth = periodMonth + (periodYear * 12);
                            }
                            if (periodMonth == totalInstall) {
                                interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                            } else {
                                long totInstallments = CommonUtil.convertObjToLong(custMap.get("TOTAL_INSTALL_PAID"));
                                if (totInstallments >= 3) { // if installment is more than three means calcualting for recurring formula...
                                    if (totInstallments > periodMonth) {
                                        period = (long) periodMonth / 3;
                                        mnPeriod = periodMonth;
                                    } else {
                                        period = totInstallments / 3;
                                        mnPeriod = totInstallments;
                                    }
                                    period = (long) roundOffLower((long) (period * 100), 100) / 100;
                                    amount = principal * (Math.pow((1 + rateOfInterest / 400), period) - 1) / (1 - Math.pow((1 + rateOfInterest / 400), -1 / 3.0));
                                    amount1 = amount;
                                    if (totInstallments > totalMonths) {
                                        mnPeriod = totalMonths - (period * 3);
                                    } else {
                                        mnPeriod = totInstallments - (period * 3);
                                    }
                                    interest = amount - (principal * period * 3);
                                    Date nextDate = fromDate;
                                    Date k = DateUtil.nextCalcDate(fromDate, fromDate, 30);
                                    period = period * 3;
                                    for (int i = 0; i < period; i++) {
                                        fromDate = DateUtil.nextCalcDate(fromDate, k, 30);
                                    }
                                    //                                        period = period *3;
                                    //                                        for(int i=0; i<period; i++)
                                    //                                            fromDate = DateUtil.addDays(fromDate, 30);
                                    Date currDate = (Date) curDate.clone();
                                    mnPeriod = totInstallments - period;
                                    if (mnPeriod > 0 && mnPeriod <= 2) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        depositRecMap.put("SL_NO", new Double(period + 1));
                                        List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstRecord != null && lstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            mnPeriod = mnPeriod - 1;
                                            if (mnPeriod > 0) {
                                                depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                depositRecMap.put("START_DT", fromDate);
                                                depositRecMap.put("SL_NO", new Double(period + 2));
                                                lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                if (lstRecord != null && lstRecord.size() > 0) {
                                                    depositRecMap = (HashMap) lstRecord.get(0);
                                                    Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                    dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                    if (secondTransDt != null && (DateUtil.dateDiff(secondTransDt, dueDate) > 0)) {
                                                        secondTransDt = dueDate;
                                                    } else {
                                                        secondTransDt = secondTransDt;
                                                    }
                                                    if (secondTransDt == null) {
                                                        secondTransDt = dueDate;
                                                    }
                                                    diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * (totInstallments - 2);
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDDaysIntAmt = greateramount - principal;
                                                    }
                                                    diffDays = DateUtil.dateDiff(secondTransDt, matDate);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * totInstallments;
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthSecondIntAmt = greateramount - principal;
                                                    }
                                                }
                                            } else {
                                                diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    principal = principal * totInstallments;
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthSecondIntAmt = greateramount - principal;
                                                }
                                            }
                                        }
                                        lstRecord = null;
                                        depositRecMap = null;
                                    } else if (mnPeriod == 0) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        depositRecMap.put("SL_NO", new Double(period));
                                        List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstRecord != null && lstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            dueDate = DateUtil.nextCalcDate(dueDate, dueDate, 30);
                                            //                                                dueDate = DateUtil.addDays(dueDate, 30);
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                            if (diffDays > 0) {
                                                principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                principal = principal * period;
                                                greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                calcFDDaysIntAmt = greateramount - principal;
                                            }
                                        }
                                        lstRecord = null;
                                        depositRecMap = null;
                                    }
                                    interest += calcFDDaysIntAmt + calcFDMonthIntAmt + calcFDDaysSecondIntAmt + calcFDMonthSecondIntAmt;
                                } else {
                                    mnPeriod = totInstallments;
                                    if (mnPeriod > 0) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        if (totInstallments == 2) {
                                            totInstallments = totInstallments - 1;
                                        }
                                        depositRecMap.put("SL_NO", new Double(totInstallments));
                                        List lstFirstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstFirstRecord != null && lstFirstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstFirstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            mnPeriod = mnPeriod - 1;
                                            if (mnPeriod > 0) {
                                                depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                depositRecMap.put("START_DT", fromDate);
                                                depositRecMap.put("SL_NO", new Double(totInstallments + 1));
                                                List lstSecondRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                if (lstSecondRecord != null && lstSecondRecord.size() > 0) {
                                                    depositRecMap = (HashMap) lstSecondRecord.get(0);
                                                    Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                    dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                    if (secondTransDt == null) {
                                                        secondTransDt = dueDate;
                                                    }
                                                    diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDDaysIntAmt = greateramount - principal;
                                                    }
                                                    diffDays = DateUtil.dateDiff(secondTransDt, matDate);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * 2;
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthIntAmt = greateramount - principal;
                                                    }
                                                }
                                                lstSecondRecord = null;
                                            } else {
                                                diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - principal;
                                                }
                                            }
                                        }
                                        depositRecMap = null;
                                        lstFirstRecord = null;
                                    }
                                    interest = calcFDDaysIntAmt + calcFDMonthIntAmt;
                                }
                            }
                            //                                matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                            //                                amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                            //                                rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                            //                                Date matDt = (Date)curDate.clone();
                            //                                if(matDate.getDate()>0){
                            //                                    matDt.setYear(matDate.getYear());
                            //                                    matDt.setMonth(matDate.getMonth());
                            //                                    matDt.setDate(matDate.getDate());
                            //                                }
                            //                                interest = holidayProvision(matDt,amount,interest,rateOfInterest,holidayProv);
                            //                                if(custMap.get("DEATH_CLAIM")!=null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false){
                            //                                    HashMap calculationMap = new HashMap();
                            //                                    calculationMap.put("DEP_AMOUNT",custMap.get("DEPOSIT_AMT"));
                            //                                    calculationMap.put("CLEAR_BALANCE",custMap.get("CLEAR_BALANCE"));
                            //                                    calculationMap.put("MAT_AMOUNT",custMap.get("MATURITY_AMT"));
                            //                                    calculationMap.put("CATEGORY_ID",custMap.get("CATEGORY"));
                            //                                    calculationMap.put("BEHAVES_LIKE",behavesMap.get("BEHAVES_LIKE"));
                            //                                    double deathAmt = simpleInterestCalculation(calculationMap);
                            //                                    interest = interest + deathAmt;
                            //                                    setDeathClaim("Y");
                            //                                }
                        }//upcoming codings r delayed installment calculation...
                        double delayAmt = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT")).doubleValue();
                        double chargeAmt = depAmt / 100;
                        HashMap delayMap = new HashMap();
                        delayMap.put("PROD_ID", custMap.get("PROD_ID"));
                        delayMap.put("DEPOSIT_AMT", custMap.get("DEPOSIT_AMT"));
                        List lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                        if (lst != null && lst.size() > 0) {
                            delayMap = (HashMap) lst.get(0);
                            delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                            delayAmt = delayAmt * chargeAmt;
                        }
                        delayMap = null;
                        lst = null;
                        long totalDelay = 0;
                        HashMap depRecMap = new HashMap();
                        depRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                        List lstRec = ClientUtil.executeQuery("getDepTransactionRecurring", depRecMap);
                        if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                            for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                depRecMap = (HashMap) lstRec.get(i);
                                Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                int transMonth = transDt.getMonth() + 1;
                                int dueMonth = dueDate.getMonth() + 1;
                                int dueYear = dueDate.getYear() + 1900;
                                int transYear = transDt.getYear() + 1900;
                                int delayeInstallment = transMonth - dueMonth;
                                if (dueYear == transYear) {
                                    delayeInstallment = transMonth - dueMonth;
                                } else {
                                    int diffYear = transYear - dueYear;
                                    delayeInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                }
                                if (delayeInstallment < 0) {
                                    delayeInstallment = 0;
                                }
                                totalDelay = totalDelay + delayeInstallment;
                            }
                            delayAmt = delayAmt * totalDelay;
                            double tblDelayMonth = CommonUtil.convertObjToDouble(custMap.get("DELAYED_MONTH")).doubleValue();
                            double tblDelayAmt = CommonUtil.convertObjToDouble(custMap.get("DELAYED_AMOUNT")).doubleValue();
                            totalDelay = totalDelay - (long) tblDelayMonth;
                            if (totalDelay > 0) {
                                delayAmt = delayAmt - tblDelayAmt;
                                setDelayedInstallments(String.valueOf(totalDelay) + " Month");
                                delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                setChargeAmount(String.valueOf(delayAmt));
                            }
                        }
                        depRecMap = null;
                        lstRec = null;
                        amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        Date matDt = (Date) curDate.clone();
                        Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
                        if (matDate.getDate() > 0) {
                            matDt.setYear(matDate.getYear());
                            matDt.setMonth(matDate.getMonth());
                            matDt.setDate(matDate.getDate());
                        }
                        holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                        if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            HashMap calculationMap = new HashMap();
                            calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                            calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                            calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                            calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                            calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                            double deathAmt = simpleInterestCalculation(calculationMap);
                            interest = interest + deathAmt;
                            calculationMap = null;
                            setDeathClaim("Y");
                        }
                        //                        }
                        List rdApplyList = ClientUtil.executeQuery("getIrregularRDApply", custMap);
                        if (rdApplyList != null && rdApplyList.size() > 0) {
                            HashMap rdMap = (HashMap) rdApplyList.get(0);
                            rdApply = CommonUtil.convertObjToStr(rdMap.get("IRREGULAR_RD_APPLY"));
                        }
                        if (rdApply.equals("SB Rate") && ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                            Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                            int actualMonths = 0;
                            int paidMonths = 0;
                            double instAmount = 0.0;
                            if (DateUtil.dateDiff(curDate, matdt) > 0) {
                                matdt = curDate;
                            }
                            HashMap rdPaidInst = new HashMap();
                            rdPaidInst.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO"));

                            List rdPaidInstList = ClientUtil.executeQuery("getRdPaidInstllments", rdPaidInst);
                            if (rdPaidInstList != null && rdPaidInstList.size() > 0) {
                                rdPaidInst = (HashMap) rdPaidInstList.get(0);
                                actualMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALLMENTS"));
                                paidMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALL_PAID"));
                                instAmount = CommonUtil.convertObjToDouble(rdPaidInst.get("DEPOSIT_AMT")).doubleValue();
                            }
                            Date maturityDate = (Date) curDate.clone();
                            maturityDate.setDate(matdt.getDate());
                            maturityDate.setMonth(matdt.getMonth());
                            maturityDate.setYear(matdt.getYear());

                            Date acopeningDt = (Date) curDate.clone();
                            acopeningDt.setDate(acopdate.getDate());
                            acopeningDt.setMonth(acopdate.getMonth());
                            acopeningDt.setYear(acopdate.getYear());

                            int acmonth = acopeningDt.getMonth() + 1;
                            int acyear = acopeningDt.getYear() + 1900;

                            int curmon = curDate.getMonth() + 1;
                            int curYear = curDate.getYear() + 1900;

                            int year = curYear - acyear;
                            int mon = curmon - acmonth;

                            mon = (year / 360) / 30 + mon + 1;
                            paidMonths = mon - paidMonths;
                            mon = mon * (mon + 1) / 2;
                            paidMonths = paidMonths * (paidMonths + 1) / 2;


                            double sbrate = 0.0;
                            long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);


                            rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                            rdPaidInst.put("PRODID", getProdID());
                            rdPaidInst.put("CUSTID", getCustomerID());
                            rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                            rdPaidInst.put("PERIOD", new Long(periodSB));
                            List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                            if (SbRateList != null && SbRateList.size() > 0) {
                                rdPaidInst = (HashMap) SbRateList.get(0);
                                sbrate = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();
                                setRateApplicable(String.valueOf(sbrate));
                                setPrematureClosingRate(String.valueOf(sbrate));

                            }
                            int unPaid = mon - paidMonths;
                            interest = instAmount * (sbrate / 100) * unPaid / 12;

                        }
                        interest = interest + holidayAmt;
                        //  interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
//                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }
                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr("0"));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            System.out.println("jjjjjj5555555555555");
                            this.setClosingIntDb(String.valueOf(interest));
                            double disburse = 0.0;
                            setPrinicipal(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                            this.setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());

                            principal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue();
                            disburse = CommonUtil.convertObjToDouble(getClosingDisbursal()).doubleValue();
                            if (disburse > principal) {
                                setLblPayRecDet("Payable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                            } else {
                                setLblPayRecDet("Receivable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                        }
                    }
                } else if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjkkkkkkkkk");
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        //                            System.out.println("######interest : "+this.getClosingIntDb());
                        //                            System.out.println("######interest : "+interest);
                        this.setIntDrawn(getIntDrawn());
                        //  interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj6666666666666666");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getBalance()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        //                            System.out.println("######DisburseAmt : "+getDisburseAmt());
                        //                            System.out.println("###### principal: "+principal);
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                    } else {
                        double period = 0;
                        double rateOfInterest = 0.0;
                        double diff = 0;
                        uncompletedInterest = 0.0;
                        String PrematureApply = "";
                        String seniorBenifitApply = "";
                        double seniorBenifitRate = 0.0;
                        //String  seniorBenifitApply="";
                        double seniorBenifit = 0.0;
                        System.out.println("jjjjjj777777777777");
                        this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(intDrawnMap.get("INT_AMT")).doubleValue()));
                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                        this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                            transfer_out_mode = "N";
                            List prematureApplyList = ClientUtil.executeQuery("getPrematureCloserApply", custMap);
                            if (prematureApplyList != null && prematureApplyList.size() > 0) {
                                HashMap cumMap = (HashMap) prematureApplyList.get(0);
                                PrematureApply = CommonUtil.convertObjToStr(cumMap.get("PREMATURE_CLOSURE_APPLY"));
                                seniorBenifitApply = CommonUtil.convertObjToStr(cumMap.get("NORMAL_RATE_FOR_SENIOR_CITIZEN"));
                                seniorBenifitRate = CommonUtil.convertObjToDouble(cumMap.get("SENIOR_BENIFIT_RATE"));

                            }
                            if (PrematureApply.equals("SB Rate")) {
                                System.out.println("inisde her");
                                depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                                if (DateUtil.dateDiff(curDate, matdt) > 0) {
                                    matdt = curDate;
                                }
                                Date maturityDate = (Date) curDate.clone();
                                maturityDate.setDate(matdt.getDate());
                                maturityDate.setMonth(matdt.getMonth());
                                maturityDate.setYear(matdt.getYear());

                                Date acopeningDt = (Date) curDate.clone();
                                acopeningDt.setDate(acopdate.getDate());
                                acopeningDt.setMonth(acopdate.getMonth());
                                acopeningDt.setYear(acopdate.getYear());
                                long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
                                HashMap rdPaidInst = new HashMap();
                                rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                                rdPaidInst.put("PRODID", getProdID());
                                rdPaidInst.put("CUSTID", getCustomerID());
                                rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                                rdPaidInst.put("PERIOD", new Long(periodSB));
                                List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                                if (SbRateList != null && SbRateList.size() > 0) {
                                    rdPaidInst = (HashMap) SbRateList.get(0);
                                    rateOfInterest = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();

                                    if (custMap.containsKey("CATEGORY") && custMap.get("CATEGORY").equals("SENIOR_CITIZENS")) {

                                        if (seniorBenifitApply.equals("N")) {
                                            rateOfInterest = rateOfInterest + seniorBenifitRate;

                                        } else {
                                            rateOfInterest = rateOfInterest;
                                        }
                                    }


                                    if (custMap.containsKey("CATEGORY") && custMap.get("CATEGORY").equals("SENIOR_CITIZENS")) {

                                        if (seniorBenifitApply.equals("N")) {
                                            rateOfInterest = rateOfInterest + .50;

                                        } else {
                                            rateOfInterest = rateOfInterest;
                                        }
                                    }

                                    setRateApplicable(String.valueOf(rateOfInterest));
                                    setPrematureClosingRate(String.valueOf(rateOfInterest));

                                }
                            }
                            double yrPeriod = 0.0;
                            double mnPeriod = 0.0;
                            double amount1 = 0.0;
                            period = totalMonths / 3;
                            period = (long) roundOffLower((long) (period * 100), 100) / 100;
                            System.out.println("period hr"+period+" "+rateOfInterest+"princ "+principal+"mont "+totalMonths);
                            if (period > 0 && rateOfInterest > 0) {
                                double depositAmt = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT"));
                                double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                                if (availableBal > depositAmt) {
                                    setPrinicipal(CommonUtil.convertObjToStr(custMap.get("DEPOSIT_AMT")));
                                    principal = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT"));
                                }
                                amount = principal * (Math.pow((1 + rateOfInterest / 400), period));// Completed quarters.....
                                completedInterest = amount - principal;
                                amount1 = amount;
                                //                                Date nextDate = depDate;
                                //                                Date k = DateUtil.nextCalcDate(depDate,depDate,30);
                                //                                period = period * 3;
                                //                                for(int i=0; i<period; i++)
                                //                                    depDate = DateUtil.nextCalcDate(depDate,k, 30);
                                //                                period = period *3;
                                //                                for(int i =0; i<period;i++)
                                //                                    depDate = DateUtil.addDays(depDate,30);
                                //                                Date currDate = (Date)curDate.clone();
                                //                                diffDay = DateUtil.dateDiff(depDate,currDate);
                                //                                double leftMonth = (double)totalMonths - period * 3;
                                //                                diffDay = (long)(leftMonth * 30 + diffDay);
                                if (diffDay > 0) {
                                    amount = amount + (amount * rateOfInterest * diffDay) / (36500);// unCompleted quarters.....
                                    uncompletedInterest = amount - amount1;
                                }
                            } else if (diffDay > 0 && rateOfInterest > 0) {
                                principal = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT")).doubleValue();
                                double depositAmt = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT"));
                                double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                                if (availableBal > depositAmt) {
                                    setPrinicipal(CommonUtil.convertObjToStr(custMap.get("DEPOSIT_AMT")));                                    
                                    principal = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT"));
                                }
                                amount = principal + (principal * rateOfInterest * diffDay) / (36500);// unCompleted quarters.....
                                uncompletedInterest = amount - principal;
                            }
                            interest = completedInterest + uncompletedInterest + interestgreater;
                            System.out.println("compl"+completedInterest+" "+uncompletedInterest+" "+interestgreater);
                            System.out.println("####CUMMULATIVE interest : " + interest);
                        } else {
                            transfer_out_mode = "NO";
                            interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                            System.out.println("interest=======" + interest);
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                            amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                            rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                            Date matDt = (Date) curDate.clone();
                            if (matDate.getDate() > 0) {
                                matDt.setYear(matDate.getYear());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setDate(matDate.getDate());
                            }
                            holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                            if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                HashMap calculationMap = new HashMap();
                                calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                double deathAmt = simpleInterestCalculation(calculationMap);
                                interest = interest + deathAmt;
                                calculationMap = null;
                                setDeathClaim("Y");
                            }
                        }
                        interest = interest + holidayAmt;
                        // interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
//                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }
                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr("0"));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            System.out.println("jjjjjj8888888888888");
                            
                            double depositAmt = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT"));
                            if(availableBal > depositAmt){                                
                              setPrinicipal(CommonUtil.convertObjToStr(custMap.get("DEPOSIT_AMT")));
                            }                            
                            
                            this.setClosingIntDb(String.valueOf(interest));
                            double intAmt = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
                            // intAmt = (double)getNearest((long)(intAmt *100),100)/100;
                            intAmt = getRound(intAmt, getInterestRound());
                            double amt = principal + intAmt;
                            amt = (double) getNearest((long) (amt * 100), 100) / 100;
                            setBalance(String.valueOf(amt));
                            this.setIntDrawn(getIntDrawn());
                            this.setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            //                        + CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()))); //Maturity Amt
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                            //                            System.out.println("#########payRecValue behavesLikeMap else :" +this.getDisburseAmt());

                            if (getDisburseAmt() > amt) {
                                setLblPayRecDet("Payable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                            } else {
                                setLblPayRecDet("Receivable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                        }
                    }

                } else if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {//DAILY DEPOSIT CALCULATION STARTING
                    DepositsProductIntPayTO depositsProductIntPayTO = null;
                    custMap.put("MODE", objMap.get("MODE"));
                    Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                    amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                    double rateOfInterest = 0;
                    //Change ROI rate 
                    String rdApply = "";
                    List rdApplyList = ClientUtil.executeQuery("getIrregularRDApply", custMap);
                    if (rdApplyList != null && rdApplyList.size() > 0) {
                        HashMap rdMap = (HashMap) rdApplyList.get(0);
                        rdApply = CommonUtil.convertObjToStr(rdMap.get("IRREGULAR_RD_APPLY"));
                    }
                    if ((rdApply != null && rdApply.equals("SB Rate")) && ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        System.out.println("acopdate------->" + acopdate);
                        Date acopeningDt = (Date) curDate.clone();
                        acopeningDt.setDate(acopdate.getDate());
                        acopeningDt.setMonth(acopdate.getMonth());
                        acopeningDt.setYear(acopdate.getYear());
                        System.out.println("acopeningDt  77------->" + acopeningDt);
                        Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                        System.out.println("matdt  77------->" + matdt);
                        if (DateUtil.dateDiff(curDate, matdt) > 0) {
                            matdt = curDate;
                        }
                        System.out.println("matdt  88------->" + matdt);
                        Date maturityDate = (Date) curDate.clone();
                        maturityDate.setDate(matdt.getDate());
                        maturityDate.setMonth(matdt.getMonth());
                        maturityDate.setYear(matdt.getYear());
                        System.out.println("maturityDate  88------->" + maturityDate);
                        double sbrate = 0.0;
                        long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
                        HashMap rdPaidInst = new HashMap();
                        rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                        rdPaidInst.put("PRODID", getProdID());
                        rdPaidInst.put("CUSTID", getCustomerID());
                        rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                        rdPaidInst.put("PERIOD", new Long(periodSB));
                        System.out.println("rdPaidInst  88------->" + rdPaidInst);
                        HashMap intCommMap = new HashMap();
                        intCommMap.put("value", getProdID());
                        List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", intCommMap);
                        depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);
                        if (depositsProductIntPayTO != null && depositsProductIntPayTO.getSlabWiseInterest() != null && depositsProductIntPayTO.getSlabWiseInterest().equals("Y")) {
                            HashMap dtMap = new HashMap();
                            dtMap.put("PROD_ID", getProdID());
                            double periodMonths = monthDiff(acopeningDt, maturityDate);
                            periodMonths = getRound(periodMonths, "HIGHER_VALUE");
                            if (periodMonths < 0) {
                                periodMonths = -1 * periodMonths;
                            }
                            long period = Math.round(periodMonths);
                            dtMap.put("PERIOD", CommonUtil.convertObjToDouble(period));
                            List roiLst = ClientUtil.executeQuery("getDailyROIRate", dtMap);
                            if (roiLst != null && roiLst.size() > 0) {
                                HashMap roiMap = (HashMap) roiLst.get(0);
                                if (roiMap != null && roiMap.containsKey("ROI")) {
                                    double roiDaily = CommonUtil.convertObjToDouble(roiMap.get("ROI"));
                                    setRateApplicable(String.valueOf(roiDaily));
                                    setPrematureClosingRate(String.valueOf(roiDaily));
                                    rateOfInterest = roiDaily;
                                }
                            }
                        } else {
                            List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                            if (SbRateList != null && SbRateList.size() > 0) {
                                rdPaidInst = (HashMap) SbRateList.get(0);
                                sbrate = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();
                                System.out.println("sbrate  88------->" + sbrate);
                                setRateApplicable(String.valueOf(sbrate));
                                setPrematureClosingRate(String.valueOf(sbrate));
                                rateOfInterest = sbrate;
                            }
                        }
                    } else {
                        //End
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    }
                    Date matDt = (Date) curDate.clone();
                    if (matDate.getDate() > 0) {
                        matDt.setYear(matDate.getYear());
                        matDt.setMonth(matDate.getMonth());
                        matDt.setDate(matDate.getDate());
                    }
                    custMap.put("INT_AMT", intDrawnMap.get("INT_AMT"));
                    dailyDepositInterestCalc(custMap, matDt, amount, interest, rateOfInterest, holidayProv, commisionMode, totalMonths, diffDay,
                            interestNotPaying, interestNotPayingMode, depositsProductIntPayTO);
                }
                setPenaltyInt(CommonUtil.convertObjToStr(custMap.get("PENAL_INT")));
            }
            custMap = null;
            list = null;
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                notifyObservers();
            }




        }

    }

    public void dailyDepositInterestCalc(HashMap custMap, Date matDt, double amount, double interest,
            double rateOfInterest, String holidayProv, String commisionMode, double totalMonths, double diffDay,
            double interestNotPaying, String interestNotPayingMode, DepositsProductIntPayTO depositsProductIntPayTO) {
        double period = 0;
        double diff = 0;
        double uncompletedInterest = 0.0;
        //        double totalMonths = 0.0;
        //        double diffDay = 0.0;
        double completedInterest = 0.0;
        double sumOfAmt = 0.0;
        double weekEndAmt = 0.0;
        double weekTransAmt = 0.0;
        int weeklycalcNo = 0;
        double commPeriod = 0.0;
        double prematureMinPeriod = 0.0;
        if (isRdoTransfer_Yes() == true) {
            transfer_out_mode = "Y";
        } else {
            transfer_out_mode = "N";
            commPeriod = CommonUtil.convertObjToDouble(custMap.get("AGENT_COMMISION_PERIOD")).doubleValue();
            prematureMinPeriod = CommonUtil.convertObjToDouble(custMap.get("PREMATURE_MIN_PERIOD")).doubleValue();
            String dailyIntCalc = CommonUtil.convertObjToStr(custMap.get("DAILY_INT_CALC"));
            if (custMap.get("BEHAVES_LIKE").equals("DAILY")) {
                if (dailyIntCalc.equals("WEEKLY")) {
                    weeklycalcNo = CommonUtil.convertObjToInt(custMap.get("WEEKLY_BASIS"));
                } else {
                    weeklycalcNo = 0;
                }
            }
            System.out.println("jjjjjj1111111111122222222");
            this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("INT_AMT")).doubleValue()));
            double principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
            System.out.println("####DAILY period : " + principal);
            HashMap customerMap = agentsCommisionAmt(custMap);
            HashMap dayendBalMap = new HashMap();
            HashMap weekTransMap = new HashMap();
            this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
            rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
            Date depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
            int depDay = depDate.getDay() + 1;
            Date depositDate = DateUtil.nextCalcDate(depDate, depDate, Math.abs(7 - depDay + weeklycalcNo));
            //            Date depositDate = DateUtil.addDays(depDate, Math.abs(7-depDay + weeklycalcNo));
            if (((String) custMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)
                    || ((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                double totalPeriod = 0;
                long totPeriod = 0;
                double transactionAmt = 0.0;
                if ((DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) >= prematureMinPeriod) {
                    if (dailyIntCalc.equals("WEEKLY")) {
                        long Period = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totPeriod = Period / 7;
                        for (int i = 0; i < totPeriod; i++) {
                            dayendBalMap.put("DAY_END_DT", depositDate);
                            dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                            List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                            if (lst != null && lst.size() > 0) {
                                dayendBalMap = (HashMap) lst.get(0);
                                sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                            }
                            depositDate = DateUtil.addDays(depositDate, 7);
                        }
                        depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        Period = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totalPeriod = Period / 30;
                        Date lastDay = null;
                        for (int i = 0; i < totalPeriod; i++) {
                            int noOfDays = 0;
                            weekTransMap.put("ACC_NUM", getTxtDepositNo() + "_1");
                            weekTransMap.put("TRN_DT", depositDate);
                            if (i == 0) {
                                //                            GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth(),depDate.getYear()+1900);
                                GregorianCalendar firstdaymonth = new GregorianCalendar((depDate.getYear() + 1900), depDate.getMonth(), depDate.getDate());
                                noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                                lastDay = depDate;
                                lastDay.setDate(noOfDays);
                                weekTransMap.put("LAST_TRN_DT", lastDay);
                            }
                            if (i != 0) {
                                //                            GregorianCalendar firstdaymonth = new GregorianCalendar(1,depositDate.getMonth(),depositDate.getYear()+1900);
                                GregorianCalendar firstdaymonth = new GregorianCalendar((depositDate.getYear() + 1900), depositDate.getMonth(), depositDate.getDate());
                                noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                                GregorianCalendar lastdaymonth = new GregorianCalendar(depositDate.getYear() + 1900, depositDate.getMonth(), noOfDays);
                                weekTransMap.put("LAST_TRN_DT", lastdaymonth.getTime());
                                depDate = lastdaymonth.getTime();
                            }
                            List lstTrans = ClientUtil.executeQuery("getSelectDailyDepositWeekendTrans", weekTransMap);
                            if (lstTrans != null && lstTrans.size() > 0) {
                                weekTransMap = (HashMap) lstTrans.get(0);
                                weekEndAmt = CommonUtil.convertObjToDouble(weekTransMap.get("AMOUNT")).doubleValue();
                                weekTransAmt = principal * noOfDays;
                                if (weekEndAmt > weekTransAmt) {
                                    transactionAmt += weekEndAmt - weekTransAmt;
                                }
                            }
                            lstTrans = null;
                            depositDate = DateUtil.addDays(depDate, 1);
                        }
                        interest += (sumOfAmt - transactionAmt) * 7 * rateOfInterest / 36500;
                    } else if (dailyIntCalc.equals("MONTHLY")) {
                        depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        double Perioddays = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totalPeriod = Perioddays / 30;
                        totalPeriod = (double) getNearest((long) (totalPeriod * 100), 100) / 100;
                        int noOfDays = 0;
                        Date lastDay = null;
                        //                    GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth()+1,depDate.getYear()+1900);
                        GregorianCalendar firstdaymonth = new GregorianCalendar((depDate.getYear() + 1900), depDate.getMonth(), depDate.getDate());
                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                        lastDay = depDate;
                        if (depDate.getMonth() == 1) {
                            noOfDays = 28;
                        }
                        if (((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                            int periodYear = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_YY"));
                            int periodMonth = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_MM"));
                            if (periodYear > 0) {
                                totalPeriod = (periodYear * 12) + periodMonth;
                            } else if (periodMonth > 0) {
                                totalPeriod = periodMonth;
                            }
                        }
                        lastDay.setDate(noOfDays);
                        if (custMap.containsKey("DAILY_INT_CALC_METHOD") && CommonUtil.convertObjToStr(custMap.get("DAILY_INT_CALC_METHOD")).equals("MINIMUM_BALANCE")) {
                            //                            for minumum balance based interest calculation
                            //                            for(int i = 0;i<=totalPeriod;i++){
                            double count = 0.0;
                            HashMap weeklyOff = new HashMap();
                            HashMap holidayMap = new HashMap();
                            lastDay.setDate(noOfDays);
                            weeklyOff.put("NEXT_DATE", setProperDtFormat(lastDay));
                            weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                            weeklyOff.put("CURR_DATE", lastDay);
                            boolean week = false;
                            boolean holiday = false;

                            //                                if(i != (totalPeriod)){
                            List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                            if (lst != null && lst.size() > 0) {
                                for (int j = 0; j < lst.size(); j++) {
                                    count = count + 1;
                                }
                                holidayMap.put("NEXT_DATE", setProperDtFormat(lastDay));
                                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                if (lst != null && lst.size() > 0) {
                                    count = count + 1;
                                }
                            } else {
                                lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                                if (lst != null && lst.size() > 0) {
                                    count = count + 1;
                                }
                                lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                if (lst != null && lst.size() > 0) {
                                    for (int j = 0; j < lst.size(); j++) {
                                        count = count + 1;
                                    }
                                }
                            }
                            int remainDays = noOfDays - (int) count;
                            lastDay.setDate(remainDays);
                            dayendBalMap.put("TODAY_DT", curDate);
                            if (depositDate.getDate() <= 10) {
                                depositDate.setDate(1);
                                dayendBalMap.put("START_DT", depositDate);
                            } else {
                                dayendBalMap.put("START_DT", depDate);
                            }
                            dayendBalMap.put("PROD_ID", custMap.get("PROD_ID"));
                            dayendBalMap.put("ADD_MONTHS", "-1");
                            dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                            System.out.println("@!#$@#$dayendBalMap:" + dayendBalMap + " :depDate " + depDate);
                            lst = ClientUtil.executeQuery("getDailyBalanceDD", dayendBalMap);
                            int lstSize = lst.size();
                            System.out.println("Amount List:" + lst);
                            for (int i = 0; i < lstSize; i++) {
                                HashMap sumOfAmtMap = new HashMap();
                                sumOfAmtMap = (HashMap) lst.get(i);
                                sumOfAmt += CommonUtil.convertObjToDouble(sumOfAmtMap.get("AMT")).doubleValue();
                                System.out.println("@#$%#$%sumOfAmt:" + sumOfAmt);
                            }

                            interest += (sumOfAmt - transactionAmt) * rateOfInterest / 1200;
                            System.out.println("@#%#$%#$%sumOfAmt:" + sumOfAmt + " :transactionAmt: " + transactionAmt + "  :rateOfInterest:" + rateOfInterest);
                            System.out.println("#@$@#$@#$interest:" + interest);
                        } else {
                            for (int i = 0; i <= totalPeriod; i++) {
                                double count = 0.0;
                                HashMap weeklyOff = new HashMap();
                                HashMap holidayMap = new HashMap();
                                lastDay.setDate(noOfDays);
                                weeklyOff.put("NEXT_DATE", setProperDtFormat(lastDay));
                                weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                weeklyOff.put("CURR_DATE", lastDay);
                                boolean week = false;
                                boolean holiday = false;
                                if (i != (totalPeriod)) {
                                    List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                    if (lst != null && lst.size() > 0) {
                                        for (int j = 0; j < lst.size(); j++) {
                                            count = count + 1;
                                        }
                                        holidayMap.put("NEXT_DATE", setProperDtFormat(lastDay));
                                        holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                        lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        if (lst != null && lst.size() > 0) {
                                            count = count + 1;
                                        }
                                    } else {
                                        lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                                        if (lst != null && lst.size() > 0) {
                                            count = count + 1;
                                        }
                                        lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                        if (lst != null && lst.size() > 0) {
                                            for (int j = 0; j < lst.size(); j++) {
                                                count = count + 1;
                                            }
                                        }
                                    }
                                    int remainDays = noOfDays - (int) count;
                                    lastDay.setDate(remainDays);
                                    dayendBalMap.put("DAY_END_DT", lastDay);
                                    dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                                    lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                                    if (lst != null && lst.size() > 0) {
                                        dayendBalMap = (HashMap) lst.get(0);
                                        sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                                        if (lastDay.getMonth() == 1) {
                                            noOfDays = 31;
                                        } else {
                                            if (noOfDays == 31 && (lastDay.getMonth() == 6 || lastDay.getMonth() == 11)) {
                                                noOfDays = 31;
                                            } else if (noOfDays == 31 && lastDay.getMonth() == 0) {
                                                noOfDays = 28;
                                            } else if (noOfDays == 31) {
                                                noOfDays = 30;
                                            } else if (noOfDays == 30) {
                                                noOfDays = 31;
                                            }
                                        }
                                        lastDay = DateUtil.addDays(lastDay, noOfDays);
                                    }
                                }
                                //                        }else{
                                //                            dayendBalMap.put("DAY_END_DT",lastDay);
                                //                            dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                                //                            List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                                //                            if(lst!=null && lst.size()>0){
                                //                                dayendBalMap = (HashMap)lst.get(0);
                                //                                double leftAmt = CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                                //                                long differ = DateUtil.dateDiff(lastDay, curDate);
                                //                                interest = leftAmt * differ * rateOfInterest / 36500;
                                //                            }
                                //                        }
                            }
                            //                    double leftAmt = 0.0;
                            //                    long remainingDay = DateUtil.dateDiff((Date)lastDay, (Date)curDate.clone());
                            //                    for(int j=(int)remainingDay;j<=remainingDay;j++){
                            //                        dayendBalMap.put("DAY_END_DT",lastDay);
                            //                        dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                            //                        List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                            //                        if(lst!=null && lst.size()>0){
                            //                            dayendBalMap = (HashMap)lst.get(0);
                            //                            leftAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                            ////                            lastDay = DateUtil.addDays(lastDay, 1);
                            //                        }
                            //                    }
                            //                    double leftdayinterest = leftAmt * remainingDay * rateOfInterest / 36500;
                            interest += (sumOfAmt - transactionAmt) * rateOfInterest / 1200;
                            //by Nidhin
                            /*HashMap intCommMap = new HashMap();
                            intCommMap.put("value", getProdID());
                            List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", intCommMap);
                            DepositsProductIntPayTO depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);*/
                            if (behavesMap.containsKey("BEHAVES_LIKE")) {
                                if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                    if (depositsProductIntPayTO != null && depositsProductIntPayTO.getSlabWiseInterest() != null && depositsProductIntPayTO.getSlabWiseInterest().equals("Y")) {
                                        HashMap intMap = new HashMap();
                                        intMap.put("ACNUM", getTxtDepositNo());
                                        intMap.put("ASON", curDate);
                                        List intList = ClientUtil.executeQuery("getSlabWiseInterest", intMap);
                                        System.out.println("intLisDAILYt" + intList + "  intMap  " + intMap);
                                        if (intList != null) {
                                            intMap = (HashMap) intList.get(0);
                                        }
                                        if (intMap != null && intMap.get("INTEREST") != null) {
                                            interest = CommonUtil.convertObjToDouble(intMap.get("INTEREST"));
                                        } else {
                                            interest = CommonUtil.convertObjToDouble(0.0);
                                        }
                                        System.out.println("set to slabwise Interst");
                                        setClosingIntDb(CommonUtil.convertObjToStr(interest));
                                    }
                                    System.out.println("INTERETST" + interest);
                                }
                            }
                            //                    interest += leftdayinterest;
                        }
                    }
                    if (((String) custMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        boolean commisionFlag = false;
                        System.out.println("before commPeriod :" + commPeriod + "totalMonths :" + totalMonths);
                        if (commisionMode != null && commisionMode.equals("Months")) {
                            //                            commPeriod = commPeriod * 30;
                            System.out.println("after commPeriod :" + commPeriod + "totalMonths :" + totalMonths);
                        } else {
                            ClientUtil.showAlertWindow("Product level parameter not properly set");
                            return;
                        }
                        if (commisionMode != null && commisionMode.equals("Months") && totalMonths < commPeriod) {
                            if (totalMonths == commPeriod && diffDay == 0) {
                                commisionFlag = true;
                                interest = 0.0;
                                setRateApplicable(String.valueOf("0.0"));
                                setPrematureClosingRate(String.valueOf("0.0"));
                            } else if (totalMonths == commPeriod && diffDay > 0) {
                                commisionFlag = false;
                            } else if (totalMonths <= commPeriod && diffDay > 0) {
                                commisionFlag = true;
                                interest = 0.0;
                                setRateApplicable(String.valueOf("0.0"));
                                setPrematureClosingRate(String.valueOf("0.0"));
                            }
                        } else if (commisionMode != null && commisionMode.equals("Daily")
                                && (DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) <= commPeriod) {
                            commisionFlag = true;
                            //                    }else if(commisionMode!=null && commisionMode.equals("Years")){
                            //                        commisionFlag = true;
                        }
                        if (interestNotPayingMode != null && interestNotPayingMode.equals("Months") && interestNotPaying < commPeriod) {
                            commisionFlag = false;
                            interest = 0.0;
                            setRateApplicable(String.valueOf("0.0"));
                            setPrematureClosingRate(String.valueOf("0.0"));
                        } else if (commisionMode != null && commisionMode.equals("Daily")
                                && (DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) <= commPeriod) {
                            commisionFlag = false;
                            interest = 0.0;
                            setRateApplicable(String.valueOf("0.0"));
                            setPrematureClosingRate(String.valueOf("0.0"));
                        }
                        if (commisionFlag == true) {
                            String agentId = "";
                            agentId = CommonUtil.convertObjToStr(custMap.get("AGENT_ID"));
//                            agentCommisionDisbursalOB.setDeposit_closing("CLOSING_SCREEN");
//                            agentCommisionDisbursalOB.setDeposit_No(getTxtDepositNo());
//                            agentCommisionDisbursalOB.setAgentId(String.valueOf(agentId));
//                            agentCommisionDisbursalOB.setDepositDate(getDepositDate());
//                            agentCommisionDisbursalOB.setPrematurePeriod(new Double(totalPeriod));
//                            double AgentCommAmt = agentCommisionDisbursalOB.commisionCalculation();
//                            AgentCommAmt = (double) getNearest((long) (AgentCommAmt * 100), 100) / 100;
//                            setAgentCommisionRecoveredValue(String.valueOf(AgentCommAmt));
                            commisionFlag = false;
                        }
                    }
                } else {
                    ClientUtil.displayAlert("This Daily Deposit Not Eligible for Closing...");
                    return;
                }
                if (((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                    //                    double holidayAmt = 0.0;
                    double holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                    interest = interest + holidayAmt;
                    if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                        HashMap calculationMap = new HashMap();
                        calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                        calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                        calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                        calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                        calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                        double deathAmt = simpleInterestCalculation(calculationMap);
                        interest = interest + deathAmt;
                        calculationMap = null;
                        setDeathClaim("Y");
                    }
                }
                //            }else{
                //                long Period = DateUtil.dateDiff((Date)depDate,(Date)matDt);
                //                long totPeriod = Period /7;
                //                for(int i = 0;i<totPeriod;i++){
                //                    dayendBalMap.put("DAY_END_DT",depositDate);
                //                    dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                //                    List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                //                    if(lst!=null && lst.size()>0){
                //                        dayendBalMap = (HashMap)lst.get(0);
                //                        sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                //                    }
                //                    depositDate = DateUtil.addDays(depositDate, 7);
                //                }
                //                depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //                depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //                Period = DateUtil.dateDiff((Date)depDate,(Date)(Date)matDt);
                //                long totalPeriod = Period /30;
                //                double transactionAmt = 0.0;
                //                Date lastDay = null;
                //                for(int i = 0;i<totalPeriod;i++){
                //                    int noOfDays = 0;
                //                    weekTransMap.put("ACC_NUM",getTxtDepositNo()+"_1");
                //                    weekTransMap.put("TRN_DT",depositDate);
                //                    if(i == 0){
                //                        GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth(),depDate.getYear()+1900);
                //                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                //                        lastDay = depDate;
                //                        lastDay.setDate(noOfDays);
                //                        weekTransMap.put("LAST_TRN_DT",lastDay);
                //                    }
                //                    if(i != 0){
                //                        GregorianCalendar firstdaymonth = new GregorianCalendar(1,depositDate.getMonth(),depositDate.getYear()+1900);
                //                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                //                        GregorianCalendar lastdaymonth = new GregorianCalendar(depositDate.getYear()+1900,depositDate.getMonth(),noOfDays);
                //                        weekTransMap.put("LAST_TRN_DT",lastdaymonth.getTime());
                //                        depDate = lastdaymonth.getTime();
                //                    }
                //                    List lstTrans = ClientUtil.executeQuery("getSelectDailyDepositWeekendTrans", weekTransMap);
                //                    if(lstTrans!=null && lstTrans.size()>0){
                //                        weekTransMap = (HashMap)lstTrans.get(0);
                //                        weekEndAmt = CommonUtil.convertObjToDouble(weekTransMap.get("AMOUNT")).doubleValue();
                //                        weekTransAmt = principal * noOfDays;
                //                        if(weekEndAmt>weekTransAmt){
                //                            transactionAmt += weekEndAmt - weekTransAmt;
                //                        }
                //                    }
                //                    lstTrans = null;
                //                    depositDate = DateUtil.addDays(depDate, 1);
                //                }
                //                interest += (sumOfAmt - transactionAmt) * 7 * rateOfInterest /36500;

            }
            //interest = (double)getNearest((long)(interest *100),100)/100;
            interest = getRound(interest, getInterestRound());
            System.out.println("uncompletedInterest:" + uncompletedInterest + "interest:" + interest);
            System.out.println("jjjjiiiiibbbb");
            this.setClosingIntDb(String.valueOf(interest));
            System.out.println("this.setClosingIntDb : " + this.getClosingIntDb());
            double intAmt = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
            //  intAmt = (double)getNearest((long)(intAmt *100),100)/100;
            intAmt = getRound(intAmt, getInterestRound());
            double amt = CommonUtil.convertObjToDouble(getBalance()).doubleValue() + intAmt;
            amt = (double) getNearest((long) (amt * 100), 100) / 100;
            this.setIntDrawn(getIntDrawn());
            System.out.println("setBalance : " + getBalanceAmt());

            this.setClosingDisbursal(CommonUtil.convertObjToStr(new Double(
                    CommonUtil.convertObjToDouble(getBalance()).doubleValue() + interest - intAmt)));
            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
            System.out.println("#########payRecValue behavesLikeMap else :" + this.getDisburseAmt());

            if (getDisburseAmt() > amt) {
                setLblPayRecDet("Payable");
                this.setPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                this.setPermanentPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                System.out.println("#########payRecValue :" + this.getPayReceivable());
            } else {
                setLblPayRecDet("Receivable");
                this.setPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                this.setPermanentPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                System.out.println("#########payRecValue else :" + this.getPayReceivable());// i have to change
            }
            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
            if (negValue < 0) {
                negValue = negValue * -1;
                this.setPayReceivable(String.valueOf(negValue));
                this.setPermanentPayReceivable(String.valueOf(negValue));
            }
            //Added By Suresh
            if (custMap.get("BEHAVES_LIKE").equals("DAILY")) {
                if (serviceChargeMap == null) {
                    serviceChargeMap = new HashMap();
                }
                if (sumOfAmt > 0) {
                    serviceChargeMap.put("TOTAL_DEP_DAY_END_AMT", String.valueOf(sumOfAmt));
                    serviceChargeMap.put("DAILY_INT_CALC", dailyIntCalc);
                }
            }
            weekTransMap = null;
            dayendBalMap = null;
            customerMap = null;
        }
    }

    public static double monthDiff(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) / Avg_Millis_Per_Month;
    }

    public HashMap agentsCommisionAmt(HashMap custMap) {
        return custMap;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private double holidayProvision(Date matDt, double amount, double interest, double rateOfInterest, String holidayProv) {
        double holidayAmt = 0.0;
        if (holidayProv.equals("Y")) {
            double diffholiday = 0.0;
            double diffweekday = 0.0;
            double count = 0;
            Date matutiryDt = (Date) curDate.clone();
            if (matDt != null && matDt.getDate() > 0) {
                matutiryDt.setDate(matDt.getDate());
                matutiryDt.setMonth(matDt.getMonth());
                matutiryDt.setYear(matDt.getYear());
            }
            Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ClientUtil.getCurrentDate()));
            HashMap weeklyOff = new HashMap();
            HashMap holidayMap = new HashMap();
            weeklyOff.put("NEXT_DATE", setProperDtFormat(matutiryDt));
            weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            weeklyOff.put("CURR_DATE", currDt.clone());
            boolean week = false;
            boolean holiday = false;
            List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    count = count + 1;
                }
                holidayMap.put("NEXT_DATE", setProperDtFormat(matutiryDt));
                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
            } else {
                lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
                lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        count = count + 1;
                    }
                }
            }
            diffweekday = DateUtil.dateDiff(matutiryDt, currDt);
            if (count == diffweekday) {
                count = count;
                //            }else if(count>1 && count == diffweekday-1)
                //                count = count;
            } else {
                count = 0;
            }
            if (count > 0) {
                deathFlag = true;
                double diff = count + diffholiday;
                holidayAmt = amount + (amount * rateOfInterest * diff) / (36500);
                holidayAmt = holidayAmt - amount;
                //                interest = interest + holidayAmt;
            }
            weeklyOff = null;
            holidayMap = null;
        }
        return holidayAmt;
    }

    private double simpleInterestCalculation(HashMap calculationMap) {
        System.out.println("Simple Calculation : " + calculationMap);
        double rateOfInt = 0.0;
        double sbCalcAmt = 0.0;
        double principal = 0.0;
        long difference = 0;
        Date depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
        Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
        long dateDiffMat = DateUtil.dateDiff(depositDate, maturityDate);
        System.out.println("########dateDiffMat : " + dateDiffMat);
        long period = dateDiffMat;
        Date currentDate = (Date) curDate.clone();
        difference = DateUtil.dateDiff(maturityDate, currentDate);
        HashMap sbInterestMap = new HashMap();
        sbInterestMap.put("PRODUCT_TYPE", "OA");
        HashMap sbProdIdMap = new HashMap();
        sbProdIdMap.put("BEHAVIOR", "SB");
        List lstProd = ClientUtil.executeQuery("getProdIdForOperative", sbProdIdMap);
        if (lstProd != null && lstProd.size() > 0) {
            HashMap prodMap = new HashMap();
            prodMap = (HashMap) lstProd.get(0);
            sbInterestMap.put("PROD_ID", prodMap.get("PROD_ID"));
        }
        sbInterestMap.put("CATEGORY_ID", calculationMap.get("CATEGORY_ID"));
        sbInterestMap.put("DEPOSIT_DT", maturityDate);
        sbInterestMap.put("AMOUNT", CommonUtil.convertObjToDouble(calculationMap.get("DEP_AMOUNT")));
        sbInterestMap.put("PERIOD", new Double(difference));
        System.out.println("########lstInt : " + sbInterestMap);
        List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", sbInterestMap);
        System.out.println("########lstInt : " + lstInt);
        if (lstInt != null && lstInt.size() > 0) {
            HashMap sbRateOfInt = (HashMap) lstInt.get(0);
            rateOfInt = CommonUtil.convertObjToDouble(sbRateOfInt.get("ROI")).doubleValue();
            System.out.println("########dateDiffMat : " + difference);
        }   //up to this
        if (calculationMap.get("BEHAVES_LIKE").equals("FIXED") || calculationMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            principal = CommonUtil.convertObjToDouble(calculationMap.get("MAT_AMOUNT")).doubleValue();
        }
        if (calculationMap.get("BEHAVES_LIKE").equals("RECURRING")) {
            principal = CommonUtil.convertObjToDouble(calculationMap.get("CLEAR_BALANCE")).doubleValue();
        }
        sbCalcAmt = principal + (principal * rateOfInt * difference) / (36500);
        sbCalcAmt = sbCalcAmt - principal;
        sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
        setDeathClaimAmt(String.valueOf(sbCalcAmt));
//        if (!calculationMap.get("BEHAVES_LIKE").equals("RECURRING")) {
//            ClientUtil.showMessageWindow("Death Claim Interest Details....\n"
//                    + "\n Matured Date is : " + maturityDate
//                    + "\n CurrentDate is : " + curDate
//                    + "\n Principal : " + principal
//                    + "\n Period : " + difference
//                    + "\n RateOf Interest : " + rateOfInt
//                    + "\n Interest Amount is : " + sbCalcAmt);
//        }
        lstInt = null;
        lstProd = null;
        sbInterestMap = null;
        sbProdIdMap = null;
        return sbCalcAmt;
    }

    private double CalculationForSeniorCitizenDeath(HashMap calculationMap) {
        String cid = getCustomerID();
        double interestAmount = 0.0;
        double DepAmount = CommonUtil.convertObjToDouble(calculationMap.get("DEP_AMOUNT")).doubleValue();
        HashMap hmap = new HashMap();
        hmap.put("CUST_ID", cid);

        Date deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("DEATH_DT")));

        Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
        Date maturityDt = (Date) matdt.clone();
        maturityDt.setDate(matdt.getDate());
        maturityDt.setMonth(matdt.getMonth());
        maturityDt.setYear(matdt.getYear());
        String periodRun = getActualPeriodRun();
        Date depositDt = DateUtil.getDateMMDDYYYY(getDepositDate());

        Date depdt = (Date) curDate.clone();
        Date deathDate = (Date) curDate.clone();
        depdt.setDate(depositDt.getDate());
        depdt.setMonth(depositDt.getMonth());
        depdt.setYear(depositDt.getYear());
        int days = depdt.getDate();
        int month = depdt.getMonth() + 1;
        int year = depdt.getYear() + 1900;

        deathDate.setDate(deathDt.getDate());
        deathDate.setMonth(deathDt.getMonth());
        deathDate.setYear(deathDt.getYear());
        int Deathdays = deathDate.getDate();
        int Deathmonth = deathDate.getMonth() + 1;
        int Deathyear = deathDate.getYear() + 1900;



        int diffNormalDays = Deathdays - days;
        int diffNormalMonths = Deathmonth - month;
        int diffNormalYears = Deathyear - year;


        diffNormalMonths = (diffNormalMonths * 30) + (diffNormalYears * 360);
        diffNormalDays = diffNormalMonths + diffNormalDays;

        int curdate = 0;
        int curmonth = 0;
        int curYear = 0;
        int count = 0;
        long period = 0;
        if (!calculationMap.get("NORMAL").equals("NORMAL")) {
            curdate = curDate.getDate();
            curmonth = curDate.getMonth() + 1;
            curYear = curDate.getYear() + 1900;
            period = DateUtil.dateDiff(depositDt, curDate);
            while (DateUtil.dateDiff(depositDt, curDate) > 0) {
                int mon = depositDt.getMonth();
                int startYear = depositDt.getYear() + 1900;
                if (mon == 1 && startYear % 4 == 0) {
                    count++;
                }
                depositDt = DateUtil.addDays(depositDt, 30);
            }
            period -= count;


        } else {
            curdate = maturityDt.getDate();
            curmonth = maturityDt.getMonth() + 1;
            curYear = maturityDt.getYear() + 1900;
            period = DateUtil.dateDiff(depositDt, curDate);
        }

        Date SeniorDate = (Date) curDate.clone();

        SeniorDate.setDate(deathDt.getDate());
        SeniorDate.setMonth(deathDt.getMonth());
        SeniorDate.setYear(deathDt.getYear());
        int sdate = SeniorDate.getDate();
        int smonth = SeniorDate.getMonth() + 1;
        int syear = SeniorDate.getYear() + 1900;

        int diffdays = curdate - sdate;
        int diffMon = curmonth - smonth;
        int diffYear = curYear - syear;

        diffMon = (diffMon * 30) + (diffYear * 360);
        diffdays = diffMon + diffdays;

        Date endDt = curDate;
        long diffnormaldate = DateUtil.dateDiff(depositDt, curDate);
        hmap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
        hmap.put("PRODID", getProdID());
        hmap.put("CUSTID", cid);
        hmap.put("DEPOSITNO", getTxtDepositNo());
        hmap.put("PERIOD", new Long(period));


        long diffnormalmnth = diffNormalDays / 30;
        long diffnormalDays = diffNormalDays % 30;

        long difSeniormonth = diffdays / 30;
        long difSeniorDays = diffdays % 30;


        if (deathDt != null) {
            List RateList = ClientUtil.executeQuery("getDepositClosingDetailsForSeniorCitizen", hmap);
            if (RateList != null && RateList.size() > 0) {
                hmap = (HashMap) RateList.get(0);
                double roi = CommonUtil.convertObjToDouble(hmap.get("ROI")).doubleValue();
                double rateOfInterest = CommonUtil.convertObjToDouble(calculationMap.get("RATE_OF_INTEREST")).doubleValue();



                if (!getPenaltyPenalRate().equals("")) {
                    double penal = CommonUtil.convertObjToDouble(getPenaltyPenalRate()).doubleValue();
                    roi = roi - penal;
                }
                interestAmount = (DepAmount * difSeniormonth * roi) / 1200;
                interestAmount = interestAmount + (DepAmount * difSeniorDays * roi) / 36500;
                //  double namount=(double)getNearest((long)(interestAmount *100),100)/100;
                double namount = getRound(interestAmount, getInterestRound());
                interestAmount = interestAmount + (DepAmount * diffnormalmnth * rateOfInterest) / 1200;
                interestAmount = interestAmount + (DepAmount * diffnormalDays * rateOfInterest) / 36500;
                double Samount = interestAmount - namount;
                //  namount=(double)getNearest((long)(namount *100),100)/100;
                namount = getRound(namount, getInterestRound());
                // Samount=(double)getNearest((long)(Samount *100),100)/100;
                Samount = getRound(Samount, getInterestRound());
//                ClientUtil.showMessageWindow("SeniorCitizen Death Claim Details....\n"
//                        + "\n Matured Date : " + matdt
//                        + "\n CurrentDate : " + curDate
//                        + "\n Senior Citizen Rate Applicable for: " + diffnormalmnth + "months" + diffnormalDays + "days"
//                        + "\n Normal Rate Applicable for: " + difSeniormonth + "months" + difSeniorDays + "days"
//                        + "\n Senior Citizen rate of Interest : " + rateOfInterest
//                        + "\n Senior Citizen Interest Amount : " + "Rs" + Samount
//                        + "\n Normal rate of Interest : " + roi
//                        + "\n Normal Interest Amount : " + "Rs" + namount);

            }
        }
        return interestAmount;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    private void setClosingIntRate(List lst) {
        if (lst != null && lst.size() > 0) {
            if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)
                    || getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                HashMap hash = new HashMap();
                hash = (HashMap) lst.get(0);
                //            int size = lst.size();
                //            Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //            Date toDate = new Date();
                //            if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE))
                //                toDate = (Date)curDate.clone();
                //            else
                //                toDate = DateUtil.getDateMMDDYYYY(getMaturityDate());
                //            //        if(size>0){
                //            System.out.println("From Date : "+fromDate);
                //            System.out.println("To Date : "+toDate);
                //            long frPeriod = 0, toPeriod = 0;
                //            double roi = 0.0, penal = 0.0 ;
                //            long dateDiff = 0;
                //            boolean foundIntRate=false;
                //            for (int i=0; i<size; i++) {
                //                hash = (HashMap) lst.get(i);
                //                frPeriod = CommonUtil.convertObjToLong(hash.get("FROM_PERIOD"));
                //                toPeriod = CommonUtil.convertObjToLong(hash.get("TO_PERIOD"));
                //                roi = CommonUtil.convertObjToDouble(hash.get("ROI")).doubleValue();
                //                penal = CommonUtil.convertObjToDouble(hash.get("PENAL_INT")).doubleValue();
                //                System.out.println("From period : "+frPeriod);
                //                System.out.println("To period : "+toPeriod);
                //                dateDiff = DateUtil.dateDiff(fromDate,toDate);
                //                periodNoOfDays=dateDiff -1;
                //                System.out.println("Date diff : "+dateDiff);
                //                if(dateDiff>=frPeriod && dateDiff<=toPeriod) {
                //                    i=size;
                //                    foundIntRate=true;
                //                }
                //            }
                //            System.out.println("From roi : "+roi);
                //            String closingRoi = "";
                //            String closingPenal = "";
                //            if (foundIntRate) {
                //                closingRoi = CommonUtil.convertObjToStr(hash.get("ROI"));
                //                closingPenal = CommonUtil.convertObjToStr(hash.get("PENAL_INT"));
                //            } else {
                //                closingRoi = "0";
                //                closingPenal = "0";
                //                roi = 0;
                //                penal = 0;
                //            }
                //            if(getViewTypeDet() == 100){



                double penal = CommonUtil.convertObjToDouble(hash.get("PENAL_INT")).doubleValue();
                if(getPenaltyRateApplicatble().equals("N")){
                    penal = 0.0;
                }
                double roi = CommonUtil.convertObjToDouble(hash.get("ROI")).doubleValue();
                String closingPenal = CommonUtil.convertObjToStr(hash.get("PENAL_INT"));
//                String closingRoi = CommonUtil.convertObjToStr(hash.get("ROI"));
                String closingRoi = CommonUtil.convertObjToStr(hash.get("ROI"));
                this.setRateApplicable(String.valueOf(roi));

                if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    //added by jjjj
                    System.out.println("jjjjj test");
//                    if (getPremClos().equals("Y")) {
                    System.out.println("getPremClos" + getPremClos());
                    // this.setRateApplicable(String.valueOf(roi));
//                        if (isRdoYesButton()) {
//                            System.out.println("premature closs" + penal);
//                            this.setPenaltyPenalRate(String.valueOf(penal));
//                            setPrematureClosingRate(String.valueOf(penal));
//                            //  roi = penal;
//                        } 
//                        else if (isRdoNoButton()) {
//                            setPrematureClosingRate(String.valueOf(penal)); 
                    setPrematureClosingRate(CommonUtil.convertObjToStr(roi - penal));
                    this.setPenaltyPenalRate(String.valueOf(0.0));
//                        }
//                    } else {

//                        if (isRdoYesButton()) {
//                            this.setPenaltyPenalRate(String.valueOf(penal));
//                            setPrematureClosingRate(String.valueOf(roi - penal));
//                        } else if (isRdoNoButton()) {
//                            setPrematureClosingRate(String.valueOf(roi));
//                            this.setPenaltyPenalRate(String.valueOf(0.0));
//                        }
//                    }
                } else if (getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                    HashMap whereAddIntdet = new HashMap();
                    whereAddIntdet.put("DEPOSIT_NO", this.getTxtDepositNo());
                    List accIntLst = ClientUtil.executeQuery("getIntCrIntDrawn", whereAddIntdet);
                    if (accIntLst != null && accIntLst.size() > 0) {
                        HashMap intDet = (HashMap) accIntLst.get(0);
                        if (intDet != null && intDet.containsKey("RATE_OF_INT")) {
                            roi = CommonUtil.convertObjToDouble(intDet.get("RATE_OF_INT")).doubleValue();
                            closingRoi = CommonUtil.convertObjToStr(intDet.get("RATE_OF_INT"));

                        }
                    }
                    this.setRateApplicable(String.valueOf(roi));
                    setPrematureClosingRate(String.valueOf(roi));
                    this.setPenaltyPenalRate(String.valueOf(0.0));
                }
                //            }
                //            setPrematureClosingRate(String.valueOf(roi-penal));
                //            HashMap editMap = new HashMap();
                //            if(getViewTypeDet() == 100 || getViewTypeDet() == 250){//editmode and new mode
                //                if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                //                    if(isRdoYesButton() == true){
                //                        setPrematureClosingRate(String.valueOf(roi- penal));
                //                        setPenaltyPenalRate(String.valueOf(penal));
                //                        setRateApplicable(String.valueOf(roi));
                //                    }else if(isRdoNoButton() == true){
                //                        setPrematureClosingRate(String.valueOf(roi));
                //                        setRateApplicable(String.valueOf(roi));
                //                        setPenaltyPenalRate(String.valueOf("0.0"));
                //                    }
                //                }else{
                //                    setPrematureClosingRate(String.valueOf(roi));
                //                }
                //            }
                //            if(getViewTypeDet() == 200){//editmode only...
                //                editMap.put("DEPOSIT_NO",getTxtDepositNo());
                //                lst = ClientUtil.executeQuery("getPenalYesorNoDetails", editMap);
                //                if(lst !=null && lst.size()>0){
                //                    editMap = (HashMap)lst.get(0);
                //                    setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                    setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                //                }
                //                setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                double calcPenalInt = 0.0;
                //                double penalAmt = CommonUtil.convertObjToDouble(editMap.get("PENAL_RATE")).doubleValue();
                //                double currInt = CommonUtil.convertObjToDouble(editMap.get("CURR_RATE_OF_INT")).doubleValue();
                //                if(isRdoYesButton() != true && isRdoNoButton() != true){
                //                    setPrematureClosingRate(String.valueOf(currInt));
                //                    setPenaltyPenalRate(String.valueOf(penalAmt));
                //                    setRateApplicable(String.valueOf(currInt + penalAmt));
                //                }
                //                setViewTypeDet(100);
                //            }
                //            if(getViewTypeDet() == 8){//authorize mode
                //                editMap = new HashMap();
                //                editMap.put("DEPOSIT_NO",getTxtDepositNo());
                //                lst = ClientUtil.executeQuery("getPenalYesorNoDetails", editMap);
                //                if(lst !=null && lst.size()>0){
                //                    editMap = (HashMap)lst.get(0);
                //                    setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                    double penalAmt = CommonUtil.convertObjToDouble(editMap.get("PENAL_RATE")).doubleValue();
                //                    double penalInt = CommonUtil.convertObjToDouble(editMap.get("CURR_RATE_OF_INT")).doubleValue();
                //                    setPrematureClosingRate(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                //                    setRateApplicable(CommonUtil.convertObjToStr(editMap.get("PENAL_RATE")));
                //                    setPenaltyPenalRate(String.valueOf(penalAmt));
                //                    if(penalAmt == 0){
                //                        setRateApplicable(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                //                        setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                //                    }else{
                //                        penalInt = penalInt + penalAmt;
                //                        setRateApplicable(String.valueOf(penalInt));
                //                    }
                //                }
                //            }
                //            if(DateUtil.dateDiff(fromDate,toDate )==0){
                //                setPrematureClosingRate("0.0");
                //                setRateApplicable("0.0");
                //                setPenaltyPenalRate("0.0");
                //            }
                //        }else{
                //            if(getViewTypeDet() == 100){
                //                double penal = 0.0;
                //                double roi = 0.0;
                //                this.setRateApplicable(String.valueOf(roi));
                //                if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                //                    if(rdoPenaltyPenalRate_yes){
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                    }else if(rdoPenaltyPenalRate_no) {
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                    }
                //                }else if(getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                }
                //            }
            } else if (getLblClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)) {
                double penal = 0.0;
                double roi = 0.0;
                setPrematureClosingRate(String.valueOf(penal));
                setRateApplicable(String.valueOf(roi));
                setPenaltyPenalRate(String.valueOf(roi));
            }
            lst = null;
        } else if (lst.isEmpty()) {
            double penal = 0.0;
            double roi = 0.0;
            setPrematureClosingRate(String.valueOf(penal));
            setRateApplicable(String.valueOf(roi));
            setPenaltyPenalRate(String.valueOf(roi));
        }
    }

    private void setDepositDetails(HashMap hashMap) {
        System.out.println("#########hashMap" + hashMap);
        this.setConstitution((String) hashMap.get("CONSTITUTION"));
        this.setCategory((String) hashMap.get("CATEGORY"));
        this.setModeOfSettlement((String) hashMap.get("SETTLEMENT_MODE"));
        this.setDepositActName((String) hashMap.get("CUST_NAME"));
        this.setCustomerID((String) hashMap.get("CUST_ID"));
    }

//    private HashMap insertJntAcctSingleRec(HashMap custMapData, String dbYesOrNo) {
//        jntAcctSingleRec = new HashMap();
//        jntAcctSingleRec.put("CUST_ID", CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
//        jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, dbYesOrNo);
//        jntAcctSingleRec.put("STATUS", "CREATED");
//        return jntAcctSingleRec;
//    }

//    public HashMap setJointAccntData(HashMap dataMap) {
//        HashMap singleRecordJntAcct;
//        jntAcctTOMap = new LinkedHashMap();
//        try {
//            JointAccntTO objJointAccntTO;
//            HashMap hash = new HashMap();
//            hash.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
//            // jntAcctAll = objJointAcctHolderManipulation.populateJointAccntTable(hash, jntAcctAll, tblJointAccnt);
//            if (jntAcctAll == null) { //--- If jointAcctAll Hashmap is null, initialize it.
//                jntAcctAll = new LinkedHashMap();
//            }
//            HashMap custMapData;
//            List custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay", dataMap);
//            custMapData = (HashMap) custListData.get(0);
//            String keyCustId = CommonUtil.convertObjToStr(custMapData.get("CUST_ID"));
//            /* If there is No Customer Id, insert the all the data with dbYesOrNo having "No" value
//             * else, insert the all the data with dbYesOrNo having "Yes" value */
//            if (jntAcctAll.get(keyCustId) == null) {
//                custMapData = insertJntAcctSingleRec(custMapData, NO_FULL_STR);
//            } else {
//                custMapData = insertJntAcctSingleRec(custMapData, YES_FULL_STR);
//            }
//            jntAcctAll.put(keyCustId, custMapData);
//            custListData = null;
//            custMapData = null;
//            int jntAcctSize = jntAcctAll.size();
//            for (int i = 0; i < jntAcctSize; i++) {
//                singleRecordJntAcct = (HashMap) jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
//                objJointAccntTO = new JointAccntTO();
//                objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
//                objJointAccntTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
//                objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
//                objJointAccntTO.setCommand("RENEW");
//                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
//                objJointAccntTO = null;
//                singleRecordJntAcct = null;
//            }
//        } catch (Exception e) {
//            parseException.logException(e, true);
//        }
//        return jntAcctTOMap;
//    }

    private String getCommand() {
        if (this.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            return CommonConstants.TOSTATUS_INSERT;
        } else if (this.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            return CommonConstants.TOSTATUS_UPDATE;
        } else if (this.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            return CommonConstants.TOSTATUS_DELETE;
        }
        return "";
    }

    public HashMap setDepSubNoAccInfoData(HashMap whereMap, HashMap dataMap, HashMap renewalMap, String prodBevaves) {
        HashMap depSubNoAccInfoSingleRec;
        depSubNoTOMap = new LinkedHashMap();
        System.out.println("renewalMap-------->" + renewalMap);
        try {
            DepSubNoAccInfoTO objDepSubNoAccInfoTO;
            //  int depSubNoSize = depSubNoAll.size();
            //  for (int i = 0; i < renewalMap.size(); i++) {
            // depSubNoAccInfoSingleRec = (HashMap) depSubNoAll.get(String.valueOf(i));
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
            objDepSubNoAccInfoTO.setCommand("RENEW");
            objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(dataMap.get("Deposit amount")));
            objDepSubNoAccInfoTO.setDepositDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("OLD_MATURITY_DATE"))));
            objDepSubNoAccInfoTO.setDepositNo("Renewal");
            objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_DAYS")));
            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_MONTHS")));
            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_YEARS")));
            //mm  
            //objDepSubNoAccInfoTO.setDepositPeriodWk("");
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
            objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_INT_FREQ")));
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(renewalMap.get("INTPAY_MODE")));
            objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentDay(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
            System.out.println("prodBevaves->" + prodBevaves);
            if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                // if(!whereMap.containsKey("INT_WITHDRAWING")){
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(dataMap.get("Deposit amount")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                if (whereMap.containsKey("INT_WITHDRAWING")) {

                    objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                } else {
                    objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                }
                //  }
            } else {
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_MATURITY_AMT")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
            }
            objDepSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_RATE_OF_INT")));
            objDepSubNoAccInfoTO.setCreateBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusDt(ClientUtil.getCurrentDate());
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));

            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_TYPE")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_ACC_NO")));
            System.out.println("ACNO====" + CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_ACC_NO")));
            System.out.println("PPTYYY====" + CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_TYPE")));
            System.out.println("IDDDDD====" + CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_ID")) + " In-->" + objDepSubNoAccInfoTO.getIntPayAcNo());
            // if (depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ) != null && depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ).equals("Y")) {
            //      objDepSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ)));
            //      objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DATE))));
            //      objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DAY)));
            // } else {
            objDepSubNoAccInfoTO.setCalender_freq("N");
            objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(""));
            objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(""));
            //  }
            if ("RENEW".equals("RENEW")) {
                objDepSubNoAccInfoTO.setFlexi_status("NR");
            } else {
                objDepSubNoAccInfoTO.setFlexi_status("N");
            }
            objDepSubNoAccInfoTO.setSalaryRecovery("N");

            String productId = CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID"));
            objDepSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRenewPostageAmt(CommonUtil.convertObjToDouble(""));
            //if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            // calculateNextAppDate(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_INT_PAY_FREQ)));
            // objDepSubNoAccInfoTO.setNextIntAppDate(getNextInterestApplDate());
            // }
            //system.out.println("############Status****** :" +objDepSubNoAccInfoTO.getAcctStatus());
            System.out.println("objDepSubNoAccInfoTO==11111==" + objDepSubNoAccInfoTO.getIntPayAcNo());
            HashMap recurringMap = new HashMap();
            recurringMap.put("PROD_ID", productId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
            if (lst.size() > 0) {
                recurringMap = (HashMap) lst.get(0);
                if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    double period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodYy());
                    double periodMm = 0.0;
                    if (period >= 1) {
                        period = period * 12.0;
                        periodMm = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                        period = period + periodMm;
                    } else {
                        period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                    }
                    objDepSubNoAccInfoTO.setTotalInstallments(new Double(period));
                }
                //  if (recurringMap.get("BEHAVES_LIKE").equals("DAILY")) {
                //   double yearPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_YY)).doubleValue();
                //   double monthPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_MM)).doubleValue();
                //   if (yearPeriod > 1) {
                //       yearPeriod = yearPeriod * 12;
                //       yearPeriod = yearPeriod + monthPeriod;
                //       objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(new Double(yearPeriod)));
                //       objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(new Double(0.0)));
                //   }
                //}
            }
            System.out.println("objDepSubNoAccInfoTO==222==" + objDepSubNoAccInfoTO);
            depSubNoTOMap.put("0", objDepSubNoAccInfoTO);
            System.out.println("objDepSubNoAccInfoTO==555==" + objDepSubNoAccInfoTO);
            System.out.println("depSubNoTOMap==555==" + depSubNoTOMap);
            //objDepSubNoAccInfoTO = null;
            // depSubNoAccInfoSingleRec = null;
            // }
        } catch (Exception e) {
            parseException.logException(e);
        }
        return depSubNoTOMap;
    }
    /* To set Account Transfer data in the Transfer Object*/

    public AccInfoTO setAccInfoData(HashMap whereMap, HashMap dataMap, String cust_id, String categ) {
        objAccInfoTO = new AccInfoTO();
        try {
            /* Sets the Authroized signatory to "Y" if it is checked
            else it assigns "N" */
            objAccInfoTO.setAuthorizedSignatory("N");
            objAccInfoTO.setCommAddress("");
            objAccInfoTO.setCommand("RENEW");
            objAccInfoTO.setCustId(CommonUtil.convertObjToStr(cust_id));
            objAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
            objAccInfoTO.setRenewalFromDeposit(CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setDepositNo("Renewal");//CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
            objAccInfoTO.setRenewalCount(CommonUtil.convertObjToDouble("1"));
            objAccInfoTO.setFifteenhDeclare("N");
            objAccInfoTO.setNomineeDetails(CommonUtil.convertObjToStr(dataMap.get("NOMINEE_DETAILS")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setTransOut("NR");
            objAccInfoTO.setDeathClaim("N");
            objAccInfoTO.setAutoRenewal("N");

            //  if (getRdowithIntRenewal_Yes() == true) {
            objAccInfoTO.setRenewWithInt("Y");
            // } else {
            objAccInfoTO.setRenewWithInt("N");
            //  }

            objAccInfoTO.setMatAlertRep("N");

            objAccInfoTO.setStandingInstruct("N");

            objAccInfoTO.setPanNumber("");

            objAccInfoTO.setPoa("N");

            objAccInfoTO.setAgentId("");
            objAccInfoTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PRODUCT_ID")));
            objAccInfoTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
            objAccInfoTO.setMdsGroup(CommonUtil.convertObjToStr(dataMap.get("MDS_GROUP")));
            objAccInfoTO.setMdsRemarks(CommonUtil.convertObjToStr(dataMap.get("MDS_REMARKS")));
            objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr("0"));
            objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(dataMap.get("CONSTITUTION")));
            objAccInfoTO.setAddressType(CommonUtil.convertObjToStr("Home"));
            objAccInfoTO.setCategory(CommonUtil.convertObjToStr(categ));
            //objAccInfoTO.setCustType("");
            objAccInfoTO.setBranchId(ProxyParameters.BRANCH_ID);
            objAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setCreatedDt(ClientUtil.getCurrentDate());
            objAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt(ClientUtil.getCurrentDate());
            objAccInfoTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objAccInfoTO.setPrintingNo(CommonUtil.convertObjToInt(0));
            objAccInfoTO.setReferenceNo("");
            objAccInfoTO.setMember("N");
            objAccInfoTO.setTaxDeductions(CommonUtil.convertObjToStr(dataMap.get("TAX_DEDUCTIONS")));//"TAX_DEDUCTIONS");
            objAccInfoTO.setAccZeroBalYN("N");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objAccInfoTO;
    }

//    private String calculateRenewalMatDate(String depMatDate, int pYear, int pMonth, int pDays) {
//        java.util.Date depDate = DateUtil.getDateWithoutMinitues(depMatDate);
//        //system.out.println("####calculateMatDate : " + depDate);
//        if (depDate != null) {
//            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
//            if (pYear > 0) {
//                cal.add(GregorianCalendar.YEAR, pYear);
//            } else {
//                cal.add(GregorianCalendar.YEAR, 0);
//            }
//            if (pMonth > 0) {
//                cal.add(GregorianCalendar.MONTH, pMonth);
//            } else {
//                cal.add(GregorianCalendar.MONTH, 0);
//            }
//            if (pDays > 0) {
//                double txtBoxPeriod = CommonUtil.convertObjToDouble(pDays).doubleValue();
//                String totMonths = String.valueOf(txtBoxPeriod / 365);
//                long totyears = new Long(totMonths.substring(0, totMonths.indexOf("."))).longValue();
//                double leftOverMth = new Double(totMonths.substring(totMonths.indexOf("."))).doubleValue();
//                java.text.DecimalFormat df = new java.text.DecimalFormat("#####");
//                leftOverMth = new Double(df.format(leftOverMth * 365)).doubleValue();
//                if (totyears >= 1) {
//                    cal.add(GregorianCalendar.YEAR, (int) totyears);
//                    cal.add(GregorianCalendar.DAY_OF_MONTH, (int) leftOverMth);
//                } else {
//                    cal.add(GregorianCalendar.DAY_OF_MONTH, pDays);
//                }
//            } else {
//                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
//            }
//            //  observable.setRenewaltdtMaturityDate(DateUtil.getStringDate(cal.getTime()));
//            ///tdtRenewalMaturityDate.setDateValue(observable.getRenewaltdtMaturityDate());
//            return DateUtil.getStringDate(cal.getTime());
//        }
//        return null;
//    }

    public double setRenewalRateOfInterset(String prodiD, String category, double renewedDepAmt, String depdate, String renMatDate) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", "TD");
        String sourceProdId = prodiD;
        String prodId = prodiD;

        whereMap.put("CATEGORY_ID", category);
        whereMap.put("PROD_ID", prodiD);
        whereMap.put("AMOUNT", renewedDepAmt);
        whereMap.put("PRODUCT_TYPE", "TD");
        whereMap.put("DEPOSIT_DT", depdate);
        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depdate));
        Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renMatDate));
        whereMap.put("DEPOSIT_DT", startDt);
        if (startDt != null && endDt != null) {
            period = DateUtil.dateDiff(startDt, endDt);
        }
        whereMap.put("PERIOD", period);
        List dataList = (List) ClientUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
        } else {
            retInt = 0;
        }
        return retInt;
    }

    public HashMap setRenewalAmountsAccROI(HashMap detailsHash, HashMap param) {
        //system.out.println("########setAmountsAccROI : "+detailsHash );
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.containsKey("INTEREST_TYPE") && detailsHash.get("INTEREST_TYPE") != null && detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    period = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                } else {
                    Date startDt = null;
                    Date endDt = null;
                    if (detailsHash.containsKey("DEPOSIT_DT") && detailsHash.containsKey("MATURITY_DT")) {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    } else {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    }
                    period = DateUtil.dateDiff(startDt, endDt);
                    int count = 0;
                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
                        int month = startDt.getMonth();
                        int startYear = startDt.getYear() + 1900;
                        if (month == 1 && startYear % 4 == 0) {
                            count++;
                        }
                        startDt = DateUtil.addDays(startDt, 30);
                    }
                    period -= count;
                }
            } else if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                if (detailsHash.containsKey("PERIOD_DAYS") && detailsHash.get("PERIOD_DAYS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_DAYS"));
                    cummPeriod = period;
                }
                if (detailsHash.containsKey("PERIOD_MONTHS") && detailsHash.get("PERIOD_MONTHS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    cummMonth = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                }
                if (detailsHash.containsKey("PERIOD_YEARS") && detailsHash.get("PERIOD_YEARS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                }
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                    detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS"));
                    yearPer = yearPer * 12;
                    int monthPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    int tot = 0;
                    Date endDt = null;
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", detailsHash.get("CATEGORY_ID"));
                    detailsHash.put("PROD_ID", detailsHash.get("PROD_ID"));
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        detailsHash.put("CALC_OPENING_MODE", "CALC_OPENING_MODE");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                //system.out.println("******** : "+detailsHash);
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            detailsHash.put("PEROID", String.valueOf(period));
            if (param == null) {
                detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list != null && list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        return amtDetHash;
    }

    public void updateInterestData() {
        tblDepositInterestApplication = new EnhancedTableModel((ArrayList) finalList, tableTitle);
    }

    public void setAccountsList(ArrayList rdList) {
        this.rdList = rdList;
    }

    public ArrayList getAccountsList() {
        return rdList;
    }

    /** To perform the necessary operation */
    public void doAction(HashMap finalMapUI) {
        TTException exception = null;
        System.out.println("hash in ob"+finalMapUI);
        log.info("In doAction()" + finalMapUI);
        try {
            doActionPerform(finalMapUI);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    public void addTargetTransactionList(String selectTransaction) {
//        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction.getElementAt(selected));
        System.out.println("selectDeposit###" + selectTransaction);
        lstSelectedTransaction.addElement(selectTransaction);
//        lstAvailableTransaction.removeElementAt(selected);
        HashMap singleMap = new HashMap();
        singleMap.put("SELECTED_DEPOSITS", selectTransaction);
        newTransactionMap.put(selectTransaction, singleMap);
    }

    public void removeTargetALLTransactionList() {
        lstSelectedTransaction.removeAllElements();
    }
//
    public void removeTargetTransactionList(int selectTransaction) {
        //        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction.getElementAt(selected));
        System.out.println("selectDeposit###" + selectTransaction);
        lstSelectedTransaction.removeElementAt(selectTransaction);
        //        lstAvailableTransaction.removeElementAt(selected);
        HashMap singleMap = new HashMap();
        singleMap.put("SELECTED_DEPOSITS", selectTransaction);
        newTransactionMap.put(selectTransaction, singleMap);
    }
//
    public String getListDeposits() {
        StringBuffer buffer = new StringBuffer();
        if (lstSelectedTransaction != null && lstSelectedTransaction.size() > 0) {
            for (int i = 0; i < lstSelectedTransaction.size(); i++) {
                buffer.append("'" + CommonUtil.convertObjToStr(lstSelectedTransaction.get(i)) + "'");
                if (i != lstSelectedTransaction.size() - 1) {
                    buffer.append(",");
                }
            }
        }
        System.out.println("buffer.toString()" + buffer.toString());
        return buffer.toString();
    }

    /** To perform the necessary action */
    private void doActionPerform(HashMap finalTableList) throws Exception {
        map.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(finalTableList, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm() {
        resetTableValues();
        setChanged();
    }

    public void resetTableValues() {
        tblDepositInterestApplication.setDataArrayList(null, tableTitle);
    }

    /**
     * Getter for property tblDepositInterestApplication.
     * @return Value of property tblDepositInterestApplication.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestApplication() {
        return tblDepositInterestApplication;
    }

    /**
     * Setter for property tblDepositInterestApplication.
     * @param tblDepositInterestApplication New value of property tblDepositInterestApplication.
     */
    public void setTblDepositInterestApplication(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestApplication) {
        this.tblDepositInterestApplication = tblDepositInterestApplication;
    }

    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalMap(HashMap finalList) {
        this.finalMap = finalList;
    }

    public HashMap getFinalMap() {
        return finalMap;
    }

    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }

    /**
     * Setter for property txtProductID.
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }

    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    /**
     * Getter for property txtTokenNo.
     * @return Value of property txtTokenNo.
     */
    public java.lang.String getTxtTokenNo() {
        return txtTokenNo;
    }

    /**
     * Setter for property txtTokenNo.
     * @param txtTokenNo New value of property txtTokenNo.
     */
    public void setTxtTokenNo(java.lang.String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
    }

    /**
     * Getter for property calFreqAccountList.
     * @return Value of property calFreqAccountList.
     */
//    public java.util.List getCalFreqAccountList() {
//        return calFreqAccountList;
//    }
//
//    /**
//     * Setter for property calFreqAccountList.
//     * @param calFreqAccountList New value of property calFreqAccountList.
//     */
//    public void setCalFreqAccountList(java.util.List calFreqAccountList) {
//        this.calFreqAccountList = calFreqAccountList;
//    }

    /**
     * Getter for property cboSIProductId.
     * @return Value of property cboSIProductId.
     */
    public java.lang.String getCboSIProductId() {
        return cboSIProductId;
    }

    /**
     * Setter for property cboSIProductId.
     * @param cboSIProductId New value of property cboSIProductId.
     */
    public void setCboSIProductId(java.lang.String cboSIProductId) {
        this.cboSIProductId = cboSIProductId;
    }

    /**
     * Getter for property cbmSIProductId.
     * @return Value of property cbmSIProductId.
     */
    public ComboBoxModel getCbmSIProductId() {
        return cbmSIProductId;
    }

    /**
     * Getter for property lstSelectedTransaction.
     * @return Value of property lstSelectedTransaction.
     */
    public javax.swing.DefaultListModel getLstSelectedTransaction() {
        return lstSelectedTransaction;
    }

    /**
     * Setter for property lstSelectedTransaction.
     * @param lstSelectedTransaction New value of property lstSelectedTransaction.
     */
    public void setLstSelectedTransaction(javax.swing.DefaultListModel lstSelectedTransaction) {
        this.lstSelectedTransaction = lstSelectedTransaction;
    }

    /**
     * Getter for property cashtoTransferMap.
     * @return Value of property cashtoTransferMap.
     */
    public HashMap getCashtoTransferMap() {
        return cashtoTransferMap;
    }

    /**
     * Setter for property cashtoTransferMap.
     * @param cashtoTransferMap New value of property cashtoTransferMap.
     */
    public void setCashtoTransferMap(HashMap cashtoTransferMap) {
        this.cashtoTransferMap = cashtoTransferMap;
    }

    /**
     * Getter for property cbmOAProductID.
     * @return Value of property cbmOAProductID.
     */
    public ComboBoxModel getCbmOAProductID() {
        return cbmOAProductID;
    }

    /**
     * Setter for property cbmOAProductID.
     * @param cbmOAProductID New value of property cbmOAProductID.
     */
    public void setCbmOAProductID(ComboBoxModel cbmOAProductID) {
        this.cbmOAProductID = cbmOAProductID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConstitution() {
        return constitution;
    }

    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceDeposit() {
        return balanceDeposit;
    }

    public void setBalanceDeposit(String balanceDeposit) {
        this.balanceDeposit = balanceDeposit;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDepositActName() {
        return depositActName;
    }

    public void setDepositActName(String depositActName) {
        this.depositActName = depositActName;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getLastIntAppDate() {
        return lastIntAppDate;
    }

    public void setLastIntAppDate(String lastIntAppDate) {
        this.lastIntAppDate = lastIntAppDate;
    }

    public String getIntCr() {
        return intCr;
    }

    public void setIntCr(String intCr) {
        this.intCr = intCr;
    }

    public String getIntDrawn() {
        return intDrawn;
    }

    public void setIntDrawn(String intDrawn) {
        this.intDrawn = intDrawn;
    }

    public String getIntPaymentFreq() {
        return intPaymentFreq;
    }

    public void setIntPaymentFreq(String intPaymentFreq) {
        this.intPaymentFreq = intPaymentFreq;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getMaturityValue() {
        return maturityValue;
    }

    public void setMaturityValue(String maturityValue) {
        this.maturityValue = maturityValue;
    }

    public String getModeOfSettlement() {
        return modeOfSettlement;
    }

    public void setModeOfSettlement(String modeOfSettlement) {
        this.modeOfSettlement = modeOfSettlement;
    }

    public String getNoInstDue() {
        return noInstDue;
    }

    public void setNoInstDue(String noInstDue) {
        this.noInstDue = noInstDue;
    }

    public String getNoInstPaid() {
        return noInstPaid;
    }

    public void setNoInstPaid(String noInstPaid) {
        this.noInstPaid = noInstPaid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPrinicipal() {
        return prinicipal;
    }

    public void setPrinicipal(String prinicipal) {
        this.prinicipal = prinicipal;
    }

    public String getLienFreezeAmt() {
        return lienFreezeAmt;
    }

    public void setLienFreezeAmt(String lienFreezeAmt) {
        this.lienFreezeAmt = lienFreezeAmt;
    }

    public String getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public String getWithDrawn() {
        return withDrawn;
    }

    public void setWithDrawn(String withDrawn) {
        this.withDrawn = withDrawn;
    }

    public String getLblClosingType() {
        return lblClosingType;
    }

    public void setLblClosingType(String lblClosingType) {
        this.lblClosingType = lblClosingType;
    }

    public String getTermLoanAdvanceActNum() {
        return termLoanAdvanceActNum;
    }

    public void setTermLoanAdvanceActNum(String termLoanAdvanceActNum) {
        this.termLoanAdvanceActNum = termLoanAdvanceActNum;
    }

    public TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public boolean isRdoTypeOfDeposit_No() {
        return rdoTypeOfDeposit_No;
    }

    public void setRdoTypeOfDeposit_No(boolean rdoTypeOfDeposit_No) {
        this.rdoTypeOfDeposit_No = rdoTypeOfDeposit_No;
    }

    public String getSubDepositNo() {
        return subDepositNo;
    }

    public void setSubDepositNo(String subDepositNo) {
        this.subDepositNo = subDepositNo;
    }

    public String getTxtDepositNo() {
        return txtDepositNo;
    }

    public void setTxtDepositNo(String txtDepositNo) {
        this.txtDepositNo = txtDepositNo;
    }

    public String getCboProductId() {
        return cboProductId;
    }

    public void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
    }

    public String getDOUBLING_SCHEME_BEHAVES_LIKE() {
        return DOUBLING_SCHEME_BEHAVES_LIKE;
    }

    public void setDOUBLING_SCHEME_BEHAVES_LIKE(String DOUBLING_SCHEME_BEHAVES_LIKE) {
        this.DOUBLING_SCHEME_BEHAVES_LIKE = DOUBLING_SCHEME_BEHAVES_LIKE;
    }

    public ArrayList getIncVal() {
        return IncVal;
    }

    public void setIncVal(ArrayList IncVal) {
        this.IncVal = IncVal;
    }

    public String getPenaltyPenalRate() {
        return PenaltyPenalRate;
    }

    public void setPenaltyPenalRate(String PenaltyPenalRate) {
        this.PenaltyPenalRate = PenaltyPenalRate;
    }

    public String getRateApplicable() {
        return RateApplicable;
    }

    public void setRateApplicable(String RateApplicable) {
        this.RateApplicable = RateApplicable;
    }

    public int getViewTypeDet() {
        return ViewTypeDet;
    }

    public void setViewTypeDet(int ViewTypeDet) {
        this.ViewTypeDet = ViewTypeDet;
    }

    public String getActualPeriodRun() {
        return actualPeriodRun;
    }

    public void setActualPeriodRun(String actualPeriodRun) {
        this.actualPeriodRun = actualPeriodRun;
    }

    public String getAddIntLoanAmt() {
        return addIntLoanAmt;
    }

    public void setAddIntLoanAmt(String addIntLoanAmt) {
        this.addIntLoanAmt = addIntLoanAmt;
    }

    public String getAgentCommisionRecoveredValue() {
        return agentCommisionRecoveredValue;
    }

    public void setAgentCommisionRecoveredValue(String agentCommisionRecoveredValue) {
        this.agentCommisionRecoveredValue = agentCommisionRecoveredValue;
    }

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public double getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public HashMap getBehavesMap() {
        return behavesMap;
    }

    public void setBehavesMap(HashMap behavesMap) {
        this.behavesMap = behavesMap;
    }

    public ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }

    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getClosingDisbursal() {
        return closingDisbursal;
    }

    public void setClosingDisbursal(String closingDisbursal) {
        this.closingDisbursal = closingDisbursal;
    }

    public String getClosingIntCr() {
        return closingIntCr;
    }

    public void setClosingIntCr(String closingIntCr) {
        this.closingIntCr = closingIntCr;
    }

    public String getClosingIntDb() {
        return closingIntDb;
    }

    public void setClosingIntDb(String closingIntDb) {
        this.closingIntDb = closingIntDb;
    }

    public String getClosingTds() {
        return closingTds;
    }

    public void setClosingTds(String closingTds) {
        this.closingTds = closingTds;
    }

    public double getCummBalance() {
        return cummBalance;
    }

    public void setCummBalance(double cummBalance) {
        this.cummBalance = cummBalance;
    }

    public double getCummClosingCr() {
        return cummClosingCr;
    }

    public void setCummClosingCr(double cummClosingCr) {
        this.cummClosingCr = cummClosingCr;
    }

    public double getCummClosingDb() {
        return cummClosingDb;
    }

    public void setCummClosingDb(double cummClosingDb) {
        this.cummClosingDb = cummClosingDb;
    }

    public double getCummClosingDisbursal() {
        return cummClosingDisbursal;
    }

    public void setCummClosingDisbursal(double cummClosingDisbursal) {
        this.cummClosingDisbursal = cummClosingDisbursal;
    }

    public double getCummClosingPenaltyPenalRate() {
        return cummClosingPenaltyPenalRate;
    }

    public void setCummClosingPenaltyPenalRate(double cummClosingPenaltyPenalRate) {
        this.cummClosingPenaltyPenalRate = cummClosingPenaltyPenalRate;
    }

    public double getCummClosingRateApplicable() {
        return cummClosingRateApplicable;
    }

    public void setCummClosingRateApplicable(double cummClosingRateApplicable) {
        this.cummClosingRateApplicable = cummClosingRateApplicable;
    }

    public double getCummClosingTdsCollected() {
        return cummClosingTdsCollected;
    }

    public void setCummClosingTdsCollected(double cummClosingTdsCollected) {
        this.cummClosingTdsCollected = cummClosingTdsCollected;
    }

    public double getCummIntCredit() {
        return cummIntCredit;
    }

    public void setCummIntCredit(double cummIntCredit) {
        this.cummIntCredit = cummIntCredit;
    }

    public double getCummIntDebit() {
        return cummIntDebit;
    }

    public void setCummIntDebit(double cummIntDebit) {
        this.cummIntDebit = cummIntDebit;
    }

    public double getCummLienFreezeAmount() {
        return cummLienFreezeAmount;
    }

    public void setCummLienFreezeAmount(double cummLienFreezeAmount) {
        this.cummLienFreezeAmount = cummLienFreezeAmount;
    }

    public double getCummTDCollected() {
        return cummTDCollected;
    }

    public void setCummTDCollected(double cummTDCollected) {
        this.cummTDCollected = cummTDCollected;
    }

    public double getCummWithDrawnAmount() {
        return cummWithDrawnAmount;
    }

    public void setCummWithDrawnAmount(double cummWithDrawnAmount) {
        this.cummWithDrawnAmount = cummWithDrawnAmount;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public String getDeathClaim() {
        return deathClaim;
    }

    public void setDeathClaim(String deathClaim) {
        this.deathClaim = deathClaim;
    }

    public String getDeathClaimAmt() {
        return deathClaimAmt;
    }

    public void setDeathClaimAmt(String deathClaimAmt) {
        this.deathClaimAmt = deathClaimAmt;
    }

    public boolean isDeathFlag() {
        return deathFlag;
    }

    public void setDeathFlag(boolean deathFlag) {
        this.deathFlag = deathFlag;
    }

    public String getDelayedInstallments() {
        return delayedInstallments;
    }

    public void setDelayedInstallments(String delayedInstallments) {
        this.delayedInstallments = delayedInstallments;
    }

    public ArrayList getDeletePWList() {
        return deletePWList;
    }

    public void setDeletePWList(ArrayList deletePWList) {
        this.deletePWList = deletePWList;
    }

    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    public LinkedHashMap getDepSubNoTOMap() {
        return depSubNoTOMap;
    }

    public void setDepSubNoTOMap(LinkedHashMap depSubNoTOMap) {
        this.depSubNoTOMap = depSubNoTOMap;
    }

    public String getDepositPenalAmt() {
        return depositPenalAmt;
    }

    public void setDepositPenalAmt(String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }

    public String getDepositPenalMonth() {
        return depositPenalMonth;
    }

    public void setDepositPenalMonth(String depositPenalMonth) {
        this.depositPenalMonth = depositPenalMonth;
    }

    public String getDepositRunPeriod() {
        return depositRunPeriod;
    }

    public void setDepositRunPeriod(String depositRunPeriod) {
        this.depositRunPeriod = depositRunPeriod;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    public double getDisburseAmt() {
        return disburseAmt;
    }

    public void setDisburseAmt(double disburseAmt) {
        this.disburseAmt = disburseAmt;
    }

    public List getFinalTableList() {
        return finalTableList;
    }

    public void setFinalTableList(List finalTableList) {
        this.finalTableList = finalTableList;
    }

    public boolean isFlPtWithoutPeriod() {
        return flPtWithoutPeriod;
    }

    public void setFlPtWithoutPeriod(boolean flPtWithoutPeriod) {
        this.flPtWithoutPeriod = flPtWithoutPeriod;
    }

    public String getInterestRound() {
        return interestRound;
    }

    public void setInterestRound(String interestRound) {
        this.interestRound = interestRound;
    }

    public LinkedHashMap getJntAcctAll() {
        return jntAcctAll;
    }

    public void setJntAcctAll(LinkedHashMap jntAcctAll) {
        this.jntAcctAll = jntAcctAll;
    }

//    public HashMap getJntAcctSingleRec() {
//        return jntAcctSingleRec;
//    }
//
//    public void setJntAcctSingleRec(HashMap jntAcctSingleRec) {
//        this.jntAcctSingleRec = jntAcctSingleRec;
//    }

    public LinkedHashMap getJntAcctTOMap() {
        return jntAcctTOMap;
    }

    public void setJntAcctTOMap(LinkedHashMap jntAcctTOMap) {
        this.jntAcctTOMap = jntAcctTOMap;
    }

    public ArrayList getKey() {
        return key;
    }

    public void setKey(ArrayList key) {
        this.key = key;
    }

    public HashMap getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(HashMap keyValue) {
        this.keyValue = keyValue;
    }

    public String getLblAddIntRtAmtVal() {
        return lblAddIntRtAmtVal;
    }

    public void setLblAddIntRtAmtVal(String lblAddIntRtAmtVal) {
        this.lblAddIntRtAmtVal = lblAddIntRtAmtVal;
    }

    public String getLblAddIntrstRteVal() {
        return lblAddIntrstRteVal;
    }

    public void setLblAddIntrstRteVal(String lblAddIntrstRteVal) {
        this.lblAddIntrstRteVal = lblAddIntrstRteVal;
    }

    public String getLblBalanceDeposit() {
        return lblBalanceDeposit;
    }

    public void setLblBalanceDeposit(String lblBalanceDeposit) {
        this.lblBalanceDeposit = lblBalanceDeposit;
    }

    public String getLblMaturityPeriod() {
        return lblMaturityPeriod;
    }

    public void setLblMaturityPeriod(String lblMaturityPeriod) {
        this.lblMaturityPeriod = lblMaturityPeriod;
    }

    public String getLblPayRecDet() {
        return lblPayRecDet;
    }

    public void setLblPayRecDet(String lblPayRecDet) {
        this.lblPayRecDet = lblPayRecDet;
    }

    public String getLblReceive() {
        return lblReceive;
    }

    public void setLblReceive(String lblReceive) {
        this.lblReceive = lblReceive;
    }

    public HashMap getLookUpHash() {
        return lookUpHash;
    }

    public void setLookUpHash(HashMap lookUpHash) {
        this.lookUpHash = lookUpHash;
    }

    public String getLstProvDt() {
        return lstProvDt;
    }

    public void setLstProvDt(String lstProvDt) {
        this.lstProvDt = lstProvDt;
    }

    public HashMap getLtdClosingMap() {
        return ltdClosingMap;
    }

    public void setLtdClosingMap(HashMap ltdClosingMap) {
        this.ltdClosingMap = ltdClosingMap;
    }

    public String getLtdDeposit() {
        return ltdDeposit;
    }

    public void setLtdDeposit(String ltdDeposit) {
        this.ltdDeposit = ltdDeposit;
    }

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }

    public LinkedHashMap getNewTransactionMap() {
        return newTransactionMap;
    }

    public void setNewTransactionMap(LinkedHashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }

    public String getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(String noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public String getNoOfUnitsAvai() {
        return noOfUnitsAvai;
    }

    public void setNoOfUnitsAvai(String noOfUnitsAvai) {
        this.noOfUnitsAvai = noOfUnitsAvai;
    }

    public String getNoOfUnitsWithDrawn() {
        return noOfUnitsWithDrawn;
    }

    public void setNoOfUnitsWithDrawn(String noOfUnitsWithDrawn) {
        this.noOfUnitsWithDrawn = noOfUnitsWithDrawn;
    }

    public String getNoOfWithDrawalUnits() {
        return noOfWithDrawalUnits;
    }

    public void setNoOfWithDrawalUnits(String noOfWithDrawalUnits) {
        this.noOfWithDrawalUnits = noOfWithDrawalUnits;
    }

    public boolean isNormalClosing() {
        return normalClosing;
    }

    public void setNormalClosing(boolean normalClosing) {
        this.normalClosing = normalClosing;
    }

    public AccInfoTO getObjAccInfoTO() {
        return objAccInfoTO;
    }

    public void setObjAccInfoTO(AccInfoTO objAccInfoTO) {
        this.objAccInfoTO = objAccInfoTO;
    }

//    public JointAcctHolderManipulation getObjJointAcctHolderManipulation() {
//        return objJointAcctHolderManipulation;
//    }
//
//    public void setObjJointAcctHolderManipulation(JointAcctHolderManipulation objJointAcctHolderManipulation) {
//        this.objJointAcctHolderManipulation = objJointAcctHolderManipulation;
//    }

    public HashMap getOldTransMap() {
        return oldTransMap;
    }

    public void setOldTransMap(HashMap oldTransMap) {
        this.oldTransMap = oldTransMap;
    }

    public HashMap getOldTransactionMap() {
        return oldTransactionMap;
    }

    public void setOldTransactionMap(HashMap oldTransactionMap) {
        this.oldTransactionMap = oldTransactionMap;
    }

    public HashMap getParam() {
        return param;
    }

    public void setParam(HashMap param) {
        this.param = param;
    }

    public String getPartialAllowed() {
        return partialAllowed;
    }

    public void setPartialAllowed(String partialAllowed) {
        this.partialAllowed = partialAllowed;
    }

    public String getPayReceivable() {
        return payReceivable;
    }

    public void setPayReceivable(String payReceivable) {
        this.payReceivable = payReceivable;
    }

    public String getPenaltyInt() {
        return penaltyInt;
    }

    public void setPenaltyInt(String penaltyInt) {
        this.penaltyInt = penaltyInt;
    }

    public long getPeriodNoOfDays() {
        return periodNoOfDays;
    }

    public void setPeriodNoOfDays(long periodNoOfDays) {
        this.periodNoOfDays = periodNoOfDays;
    }

    public String getPermanentPayReceivable() {
        return permanentPayReceivable;
    }

    public void setPermanentPayReceivable(String permanentPayReceivable) {
        this.permanentPayReceivable = permanentPayReceivable;
    }

    public String getPrematureClosingDate() {
        return prematureClosingDate;
    }

    public void setPrematureClosingDate(String prematureClosingDate) {
        this.prematureClosingDate = prematureClosingDate;
    }

    public String getPrematureClosingRate() {
        return prematureClosingRate;
    }

    public void setPrematureClosingRate(String prematureClosingRate) {
        this.prematureClosingRate = prematureClosingRate;
    }

    public double getPrematureRunPeriod() {
        return prematureRunPeriod;
    }

    public void setPrematureRunPeriod(double prematureRunPeriod) {
        this.prematureRunPeriod = prematureRunPeriod;
    }

    public String getPresentUnitInt() {
        return presentUnitInt;
    }

    public void setPresentUnitInt(String presentUnitInt) {
        this.presentUnitInt = presentUnitInt;
    }

    public String getPrev_interest() {
        return prev_interest;
    }

    public void setPrev_interest(String prev_interest) {
        this.prev_interest = prev_interest;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProductBehavesLike() {
        return productBehavesLike;
    }

    public void setProductBehavesLike(String productBehavesLike) {
        this.productBehavesLike = productBehavesLike;
    }

    public String getProductInterestType() {
        return productInterestType;
    }

    public void setProductInterestType(String productInterestType) {
        this.productInterestType = productInterestType;
    }

    public ProxyFactory getProxy() {
        return proxy;
    }

    public void setProxy(ProxyFactory proxy) {
        this.proxy = proxy;
    }

    public boolean isRateOfIntCalculated() {
        return rateOfIntCalculated;
    }

    public void setRateOfIntCalculated(boolean rateOfIntCalculated) {
        this.rateOfIntCalculated = rateOfIntCalculated;
    }

    public ArrayList getRdList() {
        return rdList;
    }

    public void setRdList(ArrayList rdList) {
        this.rdList = rdList;
    }

    public boolean isRdoNoButton() {
        return rdoNoButton;
    }

    public void setRdoNoButton(boolean rdoNoButton) {
        this.rdoNoButton = rdoNoButton;
    }

    public boolean isRdoPenaltyPenalRate_no() {
        return rdoPenaltyPenalRate_no;
    }

    public void setRdoPenaltyPenalRate_no(boolean rdoPenaltyPenalRate_no) {
        this.rdoPenaltyPenalRate_no = rdoPenaltyPenalRate_no;
    }

    public boolean isRdoPenaltyPenalRate_yes() {
        return rdoPenaltyPenalRate_yes;
    }

    public void setRdoPenaltyPenalRate_yes(boolean rdoPenaltyPenalRate_yes) {
        this.rdoPenaltyPenalRate_yes = rdoPenaltyPenalRate_yes;
    }

    public boolean isRdoTransfer_No() {
        return rdoTransfer_No;
    }

    public void setRdoTransfer_No(boolean rdoTransfer_No) {
        this.rdoTransfer_No = rdoTransfer_No;
    }

    public boolean isRdoTransfer_Yes() {
        return rdoTransfer_Yes;
    }

    public void setRdoTransfer_Yes(boolean rdoTransfer_Yes) {
        this.rdoTransfer_Yes = rdoTransfer_Yes;
    }

    public boolean isRdoTypeOfDeposit_Yes() {
        return rdoTypeOfDeposit_Yes;
    }

    public void setRdoTypeOfDeposit_Yes(boolean rdoTypeOfDeposit_Yes) {
        this.rdoTypeOfDeposit_Yes = rdoTypeOfDeposit_Yes;
    }

    public boolean isRdoYesButton() {
        return rdoYesButton;
    }

    public void setRdoYesButton(boolean rdoYesButton) {
        this.rdoYesButton = rdoYesButton;
    }

    public HashMap getServiceChargeMap() {
        return serviceChargeMap;
    }

    public void setServiceChargeMap(HashMap serviceChargeMap) {
        this.serviceChargeMap = serviceChargeMap;
    }

    public String getSetPenaltyPenalRate() {
        return setPenaltyPenalRate;
    }

    public void setSetPenaltyPenalRate(String setPenaltyPenalRate) {
        this.setPenaltyPenalRate = setPenaltyPenalRate;
    }

    public String getSettlementUnitInt() {
        return settlementUnitInt;
    }

    public void setSettlementUnitInt(String settlementUnitInt) {
        this.settlementUnitInt = settlementUnitInt;
    }

//    public EnhancedTableModel getTblJointAccnt() {
//        return tblJointAccnt;
//    }
//
//    public void setTblJointAccnt(EnhancedTableModel tblJointAccnt) {
//        this.tblJointAccnt = tblJointAccnt;
//    }

    public String getTdsAcHd() {
        return tdsAcHd;
    }

    public void setTdsAcHd(String tdsAcHd) {
        this.tdsAcHd = tdsAcHd;
    }

    public String getTdsCollected() {
        return tdsCollected;
    }

    public void setTdsCollected(String tdsCollected) {
        this.tdsCollected = tdsCollected;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getTransProdId() {
        return transProdId;
    }

    public void setTransProdId(String transProdId) {
        this.transProdId = transProdId;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    public void setTransactionDetailsTO(LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    public String getTransferBranch_code() {
        return transferBranch_code;
    }

    public void setTransferBranch_code(String transferBranch_code) {
        this.transferBranch_code = transferBranch_code;
    }

    public String getTransfer_out_mode() {
        return transfer_out_mode;
    }

    public void setTransfer_out_mode(String transfer_out_mode) {
        this.transfer_out_mode = transfer_out_mode;
    }

    public String getTxtAmtWithDrawn() {
        return txtAmtWithDrawn;
    }

    public void setTxtAmtWithDrawn(String txtAmtWithDrawn) {
        this.txtAmtWithDrawn = txtAmtWithDrawn;
    }

    public String getTxtPWNoOfUnits() {
        return txtPWNoOfUnits;
    }

    public void setTxtPWNoOfUnits(String txtPWNoOfUnits) {
        this.txtPWNoOfUnits = txtPWNoOfUnits;
    }

    public String getTypeOfDep() {
        return typeOfDep;
    }

    public void setTypeOfDep(String typeOfDep) {
        this.typeOfDep = typeOfDep;
    }

    public double getUnitAmt() {
        return unitAmt;
    }

    public void setUnitAmt(double unitAmt) {
        this.unitAmt = unitAmt;
    }

    public ArrayList getValue() {
        return value;
    }

    public void setValue(ArrayList value) {
        this.value = value;
    }

    public String getWithdrawalTOStatus() {
        return withdrawalTOStatus;
    }

    public void setWithdrawalTOStatus(String withdrawalTOStatus) {
        this.withdrawalTOStatus = withdrawalTOStatus;
    }

    public ArrayList getWithdrawalTOs() {
        return withdrawalTOs;
    }

    public void setWithdrawalTOs(ArrayList withdrawalTOs) {
        this.withdrawalTOs = withdrawalTOs;
    }

//    public int getYearTobeAdded() {
//        return yearTobeAdded;
//    }
//
//    public void setYearTobeAdded(int yearTobeAdded) {
//        this.yearTobeAdded = yearTobeAdded;
//    }

    public List getChargelst() {
        return Chargelst;
    }

    public void setChargelst(List Chargelst) {
        this.Chargelst = Chargelst;
    }

    public HashMap getBothRecPayMap() {
        return bothRecPayMap;
    }

    public void setBothRecPayMap(HashMap bothRecPayMap) {
        this.bothRecPayMap = bothRecPayMap;
    }

    public HashMap getChargeMap() {
        return chargeMap;
    }

    public void setChargeMap(HashMap chargeMap) {
        this.chargeMap = chargeMap;
    }

//    public String getLblServiceTaxval() {
//        return lblServiceTaxval;
//    }
//
//    public void setLblServiceTaxval(String lblServiceTaxval) {
//        this.lblServiceTaxval = lblServiceTaxval;
//    }

    public String getPremClos() {
        return premClos;
    }

    public void setPremClos(String premClos) {
        this.premClos = premClos;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public String getPenaltyRateApplicatble() {
        return penaltyRateApplicatble;
    }

    public void setPenaltyRateApplicatble(String penaltyRateApplicatble) {
        this.penaltyRateApplicatble = penaltyRateApplicatble;
    }
    
    
}