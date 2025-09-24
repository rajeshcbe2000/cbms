/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BackDatedTransactionOB.java
 *
 * Created on March 12, 2014, 3:43 PM 2014  
 */
package com.see.truetransact.ui.batchprocess.BackDatedTransaction;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientproxy.ProxyParameters;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

/**
 * @author Suresh R
 *
 */
public class BackDatedTransactionOB extends CObservable {

    private BackDatedTransactionRB resourceBundle;
    private String batchId = "";
    private String transId = "";
    private String productId = "";
    private String accountHeadDesc = "";
    private String accountNo = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String instrumentDate = "";
    private String currencyType = "";
    private String transferAmount = "";
    private String particulars = "";
    private String narration = "";
    private HashMap ALL_LOAN_AMOUNT = new HashMap();
    private HashMap multiple_ALL_LOAN_AMOUNT = new HashMap();
    private HashMap debitLoanType = new HashMap();
    private int totalDrInstruments = 0;
    private double totalDrAmount = 0.0;
    private int totalCrInstruments = 0;
    private double totalCrAmount = 0.0;
    private HashMap loanDebitType = null;
    private String depInterestAmt = "";
    private String status = "";
    private String productTypeValue = "";
    private String instrumentTypeValue = "";
    private String currencyTypeValue = "";
    private String mainProductTypeValue = "";
    private HashMap map = null;
    private ProxyFactory proxy = null;
    private String accountHeadId = "";
    private String action = "";
    private String amount = "";
    private String transDate = "";
    private String transType = "";
    private String instType = "";
    //private String tokenNo = "0";
    private String initTransId = "";
    private String initChannType = "";
    private String transStatus = "";
    private String authorizeRemarks = null;
    private ComboBoxModel productTypeModel;
    private ComboBoxModel instrumentTypeModel;
    private ComboBoxModel currencyTypeModel;
    private ComboBoxModel mainProductTypeModel;
    private String initBran = "";
    private boolean rdoBulkTransaction_Yes = false;
    private boolean rdoBulkTransaction_No = false;
    //To populate comboboxes
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue;
    private HashMap lookupMap;
    private int operation;
    private boolean checkDebitTermLoan = false;
    private HashMap oldAmountMap;
    //To manipulate status message
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    // to show error in case of business rule checks
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap authMap = new HashMap();
    private TableModel tbmTransfer;
    private ArrayList transferTOs, deleteTOsList, reconciliationList;
    private HashMap authorizeMap = null;
    private String transAmt = "";
    private String loanActNo = "";
    private String transDepositAmt = "";
    private String depositTransId = null;
    private double interestAmt = 0.0;
    private String depLinkBatchId = "";
    private String depAccNO;
    private String renewDepAmt = null;
    public boolean depositRenewalFlag = false;
    public boolean renewalIntFlag = false;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private boolean depositPenalFlag = false;
    private String depositProdId = null;
    private Date curDate = null;
    private Date valueDate = null;
    private HashMap linkMap = new HashMap();
    private HashMap penalMap = new HashMap();
    private HashMap procChargeHash = new HashMap();  //This hashmap used to store details for Processing Charge
    private String creatingFlexi = "";
    private double flexiAmount = 0.0;
    private boolean valueDateFlag = false;
    private HashMap valueDateMap = null;
    private String reconcile = "";
    private String balanceType = "";
    public HashMap reconcileMap;
    private String authBy = "";
    boolean minBalException = false;
    private long emiNoInstallment = 0;
    private boolean penalWaiveOff = false;
    private boolean rebateInterest = false;
    private String closedAccNo = "";
    private boolean hoAccount = false;
    ArrayList orgRespList = null;
    private String hoAccountStatus = "";
    private String hoAccountCr = "";
    private String hoAccountDr = "";
    private boolean adviceAccount = false;
    ArrayList adviceList = null;
    public ArrayList deletedList = null;
    private String branchId = "";
    private String prodDebitTransfer = "";
    private String prodCreditTransfer = "";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    /**
     * Creates a new instance of BackDatedTransactionOB
     */
    public BackDatedTransactionOB() {
        /*
         * set up the map for proxy, this will be used to invoke the business
         * methods
         */
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TransferJNDI");
        map.put(CommonConstants.HOME, "transaction.transfer.TransferHome");
        map.put(CommonConstants.REMOTE, "transaction.transfer.TransferRemote");
        try {
            // create the proxy
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            transferTOs = new ArrayList();
            deleteTOsList = new ArrayList();
            reconciliationList = new ArrayList();
            oldAmountMap = new HashMap();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Account No");
        columnHeader.add("Batch ID");
        columnHeader.add("Trans ID");
        columnHeader.add("Amount");
        columnHeader.add("Type");
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            columnHeader.add("Verified");
        }
        ArrayList data = new ArrayList();
        tbmTransfer = new TableModel(data, columnHeader);
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

            final HashMap param = new HashMap();
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            productTypeModel = new ComboBoxModel(key, value);
            instrumentTypeModel = new ComboBoxModel(key, value);

            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("FOREX.CURRENCY");
            lookupKey.add("PRODUCTTYPE");
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);

            fillData((HashMap) lookupValues.get("FOREX.CURRENCY"));
            currencyTypeModel = new ComboBoxModel(key, value);

            fillData((HashMap) lookupValues.get("PRODUCTTYPE"));
            mainProductTypeModel = new ComboBoxModel(key, value);
//            mainProductTypeModel.removeKeyAndElement("OA");
            mainProductTypeModel.removeKeyAndElement("TL");
            mainProductTypeModel.removeKeyAndElement("TD");
            mainProductTypeModel.removeKeyAndElement("AD");
            mainProductTypeModel.removeKeyAndElement("OA"); // Added by nithya on 19-05-2016
//            mainProductTypeModel.removeKeyAndElement("AB");
            if(mainProductTypeModel.getKeys().contains("MDS")){
                mainProductTypeModel.removeKeyAndElement("MDS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillInstrumentType() {
        final HashMap param = new HashMap();
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("INSTRUMENTTYPE");
        try {
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);

            fillData((HashMap) lookupValues.get("INSTRUMENTTYPE"));
            if (this.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                int index = key.indexOf("ONLINE_TRANSFER");
                key.remove(index);
                value.remove(index);
            }
            key.add("ECS");
            value.add("ECS");
            instrumentTypeModel = new ComboBoxModel(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To set the key & value for comboboxes
     */
    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateDataLocal(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);

        return keyValue;
    }

    /**
     * set the values on the UI to some initial value
     */
    public void resetOBFields() {
        batchId = "";
        status = "";
        action = "";
        totalDrInstruments = 0;
        totalDrAmount = 0.0;
        totalCrInstruments = 0;
        totalCrAmount = 0.0;
        authorizeMap = null;
        setDepositPenalAmt("");
        setDepositPenalMonth("");
        resetTable();
        valueDateMap = null;
        valueDate = null;
        resetTransactionDetails();
        valueDateFlag = false;
        rdoBulkTransaction_No = false;
        rdoBulkTransaction_Yes = false;
        setClosedAccNo("");
        setHoAccount(false);
        setHoAccountStatus("");
        setAdviceAccount(false);
        setHoAccountCr("");
        setHoAccountDr("");
//        setTransDate("");
        deletedList = null;
        setOrgRespList(null);
    }

    public void resetTransactionDetails() {
        transId = "";
        productId = "";
        accountHeadDesc = "";
        accountNo = "";
        instrumentNo1 = "";
        instrumentNo2 = "";
        instrumentDate = "";
        currencyType = "";
        transferAmount = "";
        particulars = "";
        narration = "";
        accountHeadId = "";
        amount = "";
//        transDate = "";
        transType = "";
        instType = "";
        initTransId = "";
        initChannType = "";
        transStatus = "";
        productTypeValue = "";
        instrumentTypeValue = "";
        currencyTypeValue = "";
        mainProductTypeValue = "";
        setChanged();
        notifyObservers();
    }

    public void resetTransDate() {
        transDate = "";
    }

    /**
     * set the value of Account head ID and description based on the product
     * selected in the UI this method will use the LookupMap
     */
    public String getAccountHeadForProductId(String productId) {
        /*
         * may be the screen has been cleared, in that scenario we will have the
         * cboProductId as "", and we don;t want anything to be shown in place
         * of the account head description
         */

        if (productId == null || productId.equals("")) {
            return "";
        }

        final HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID", productId);
        final List resultList = ClientUtil.executeQuery("getAccountHeadProd" + this.getMainProductTypeValue(), accountHeadMap);
        final HashMap resultMap = (HashMap) resultList.get(0);
        accountHeadId = (resultMap.get("AC_HEAD").toString());
        accountHeadDesc = (resultMap.get("AC_HEAD_DESC").toString());

        return accountHeadId + " [" + accountHeadDesc + "]";
    }

    /**
     * get the value
     */
    public int getData(String transactionId) {
        ArrayList arr = null;
        try {
            getTransferData();
            this.fillInstrumentType();
            if (transactionId != null) {
                return populateTransfer(transactionId);
            }
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void setTransferTO(TxTransferTO to) {
        setTransId(to.getTransId());
        setBatchId(to.getBatchId());
        setMainProductTypeValue(to.getProdType());
        setProductTypeValue(to.getProdType());
        setProductId(to.getProdId());
        setAccountHeadId(to.getAcHdId());
        setAccountNo(to.getActNum());
        setTransferAmount(CommonUtil.convertObjToStr(to.getInpAmount()));
        setCurrencyType(to.getInpCurr());
        setCurrencyTypeValue(to.getInpCurr());
        setAmount(CommonUtil.convertObjToStr(to.getAmount()));
        Double amt = to.getAmount();
        setAmount(String.valueOf(to.getAmount()));
        setTransDate(DateUtil.getStringDate(to.getTransDt()));
        setTransType(to.getTransType());
        setInstType(to.getInstType());
        setInstrumentTypeValue(to.getInstType());
        setInstrumentNo1(to.getInstrumentNo1());
        setInstrumentNo2(to.getInstrumentNo2());
        setInstrumentDate(DateUtil.getStringDate(to.getInstDt()));
        setInitTransId(to.getInitTransId());
        setInitChannType(to.getInitChannType());
        setParticulars(to.getParticulars());
        setNarration(to.getNarration());
        setTransStatus(to.getStatus());
        setAuthorizeRemarks(to.getAuthorizeRemarks());
        setAuthBy(to.getAuthorizeBy());
        setBranchId(to.getBranchId());
        setChanged();
        notifyObservers();
    }

    // Checks a/c no. existence without prod_type & prod_id
    public boolean checkAcNoWithoutProdType(String actNum, boolean branchIdOnly) {

        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {
            if (actNum.indexOf("_") > 0) {
                actNum = actNum.substring(0, actNum.indexOf("_"));
            }
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                if (branchIdOnly) {
                    return true;
                } else {
                    setAccountNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }
                mainProductTypeModel.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setMainProductTypeValue(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                getProducts();
                productTypeModel.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
                ArrayList key = new ArrayList();
                ArrayList value = new ArrayList();
                key.add("");
                value.add("");
                productTypeModel = new ComboBoxModel(key, value);
                isExists = false;
                key = null;
                value = null;
                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }

    public boolean depositAuthorizationValidation(ArrayList transferTOs) {
        int size = this.transferTOs.size();
        TxTransferTO objTO;
        boolean auth = false;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            if (!objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED) && this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE
                    && objTO.getTransType().equals(CommonConstants.CREDIT) && objTO.getProdType() != null && objTO.getProdType().equals("TD")) {
                HashMap depAuthMap = new HashMap();
                String AcNo = "";
                if (objTO.getActNum().lastIndexOf("_") != -1) {
                    AcNo = objTO.getActNum().substring(0, objTO.getActNum().lastIndexOf("_"));
                }
                depAuthMap.put("DEPOSIT_NO", AcNo);
                List lst = ClientUtil.executeQuery("getSelectRemainingBalance", depAuthMap);
                if (lst != null && lst.size() > 0) {
                    depAuthMap = (HashMap) lst.get(0);
                    double depositAmt = CommonUtil.convertObjToDouble(depAuthMap.get("DEPOSIT_AMT")).doubleValue();
                    double totalBalance = CommonUtil.convertObjToDouble(depAuthMap.get("TOTAL_BALANCE")).doubleValue();
                    double remainingAmt = depositAmt - totalBalance;
                    if (remainingAmt < CommonUtil.convertObjToDouble(objTO.getAmount()).doubleValue()) {
                        if (remainingAmt > 0) {
                            ClientUtil.showAlertWindow("Cannot authorize, amount exceeding the deposit amount"
                                    + "\n Deposit Amount is :" + depositAmt
                                    + "\n Balance Amount to be collected :" + remainingAmt);
                        } else {
                            ClientUtil.showAlertWindow("Cannot authorize, amount exceeding the deposit amount"
                                    + "\n Deposit Amount is :" + depositAmt
                                    + "Please Reject the Transaction ");
                        }
                        auth = true;
                    }
                }
            }
        }
        return auth;
    }
    //as and when customer comes interest calculaion

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {//dont delete chck selectalldao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapData;
    }

    private HashMap populateBean(String parameter) throws Exception {
        HashMap transaction = new HashMap();
        if (!minBalException) {
            transferTOs.addAll(this.deleteTOsList);
        }

        if (operation == ClientConstants.ACTIONTYPE_NEW) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        } else if (operation == ClientConstants.ACTIONTYPE_EDIT) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
        } else if (operation == ClientConstants.ACTIONTYPE_DELETE) {
            transaction.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
            setStatusForTOs();
        }
        if (!getReconcile().equals("") && getReconcile().equals("N")) {
            transaction.put("LIST_OF_REDUCED", reconcileMap);
        }
        transaction.put("TxTransferTO", this.transferTOs);
        if (operation == ClientConstants.ACTIONTYPE_NEW && reconciliationList.size() > 0) {
            transaction.put("ReconciliationTO", this.reconciliationList);
        }
        transaction.put("OLDAMOUNT", this.oldAmountMap);

        boolean depositFlag = false;
        TxTransferTO objTOTD = null;
        int size = this.transferTOs.size();
        HashMap depositPenalMap = null;
        HashMap multiDepositPenalMap = new HashMap();
        for (int j = 0; j < size; j++) {
            objTOTD = (TxTransferTO) transferTOs.get(j);
            if (objTOTD.getProdType() != null && objTOTD.getProdType().equals("TD")) {
                depositFlag = true;
                depositPenalMap = new HashMap();
            }
            if (depositFlag == true) {
                String act_Num = CommonUtil.convertObjToStr(objTOTD.getActNum());
                if (act_Num.lastIndexOf("_") != -1) {
                    act_Num = act_Num.substring(0, act_Num.lastIndexOf("_"));
                }
                HashMap accountMap = new HashMap();
                accountMap.put("DEPOSIT_NO", act_Num);
                accountMap.put("BRANCH_ID", objTOTD.getBranchId());
                List lst = ClientUtil.executeQuery("getProductIdForDeposits", accountMap);
                if (lst != null && lst.size() > 0) {
                    accountMap = (HashMap) lst.get(0);
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID", accountMap.get("PROD_ID"));
                    List lstBehave = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                    if (lstBehave != null && lstBehave.size() > 0) {
                        prodMap = (HashMap) lstBehave.get(0);
                        if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            Date currDt = (Date) curDate.clone();
                            long totalDelay = 0;
                            long actualDelay = 0;
                            double delayAmt = 0.0;
                            double tot_Inst_paid = 0.0;
                            double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                            Date matDt = (Date) curDate.clone();
                            Date depDt = (Date) curDate.clone();
                            HashMap lastMap = new HashMap();
                            lastMap.put("DEPOSIT_NO", act_Num);
                            lst = ClientUtil.executeQuery("getInterestDeptIntTable", lastMap);
                            if (lst != null && lst.size() > 0) {
                                lastMap = (HashMap) lst.get(0);
                                tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                                HashMap prematureDateMap = new HashMap();
                                double monthPeriod = 0.0;
                                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                                if (matDate.getDate() > 0) {
                                    matDt.setDate(matDate.getDate());
                                    matDt.setMonth(matDate.getMonth());
                                    matDt.setYear(matDate.getYear());
                                }
                                Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                if (depDate.getDate() > 0) {
                                    depDt.setDate(depDate.getDate());
                                    depDt.setMonth(depDate.getMonth());
                                    depDt.setYear(depDate.getYear());
                                }
                                if (DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) {
                                    prematureDateMap.put("TO_DATE", matDt);
                                    prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                    lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                                    if (lst != null && lst.size() > 0) {
                                        prematureDateMap = (HashMap) lst.get(0);
                                        monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    }
                                    lst = null;
                                } else {
                                    int dep = depDt.getMonth() + 1;
                                    int curr = currDt.getMonth() + 1;
                                    int depYear = depDt.getYear() + 1900;
                                    int currYear = currDt.getYear() + 1900;
                                    if (depYear == currYear) {
                                        monthPeriod = curr - dep;
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    } else {
                                        int diffYear = currYear - depYear;
                                        monthPeriod = (diffYear * 12 - dep) + curr;
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                    }
                                }
                            }
                            lst = null;
                            //delayed installment calculation...
                            if (depositPenalFlag == true) {  // Condition added by Rajesh
                                if (DateUtil.dateDiff((Date) matDt, (Date) currDt) < 0) {
                                    depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT")).doubleValue();
                                    double chargeAmt = depAmt / 100;
                                    HashMap delayMap = new HashMap();
                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                    lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                                    if (lst != null && lst.size() > 0) {
                                        delayMap = (HashMap) lst.get(0);
                                        delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                        delayAmt = delayAmt * chargeAmt;
                                    }
                                    lst = null;
                                    HashMap depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", objTOTD.getActNum());
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
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                        }
                                    }
                                    lstRec = null;
                                    depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", objTOTD.getActNum());
                                    depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                    depRecMap.put("CURR_DT", currDt);
                                    depRecMap.put("SL_NO", new Double(tot_Inst_paid));
                                    lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
                                    if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                        for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                            depRecMap = (HashMap) lstRec.get(i);
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            int transMonth = currDt.getMonth() + 1;
                                            int dueMonth = dueDate.getMonth() + 1;
                                            int dueYear = dueDate.getYear() + 1900;
                                            int transYear = currDt.getYear() + 1900;
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                        }
                                    }
                                    lstRec = null;
                                    delayAmt = delayAmt * totalDelay;
                                    delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                    double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT")).doubleValue();
                                    long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                    double balanceAmt = 0.0;
                                    if (oldPenalAmt > 0) {
                                        balanceAmt = delayAmt - oldPenalAmt;
                                        totalDelay = totalDelay - oldPenalMonth;
                                    } else {
                                        balanceAmt = delayAmt;
                                    }
                                    if (balanceAmt > 0) {
                                        depositPenalMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                        depositPenalMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                        multiDepositPenalMap.put(objTOTD.getActNum(), depositPenalMap);
                                    }
                                }
                            }
                        }
                    }
                    lstBehave = null;
                }
                depositFlag = false;
            }
        }
        if (multiDepositPenalMap != null && multiDepositPenalMap.size() > 0) {
            transaction.put("MULTIPLE_DEPOSIT_PENAL", multiDepositPenalMap);
        }
        if (!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_CREATION")) {
            transaction.put("FLEXI_LIEN_CREATION", getCreatingFlexi());
            transaction.put("FLEXI_AMOUNT", String.valueOf(getFlexiAmount()));
        }
        if (!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_DELETION")) {
            transaction.put("FLEXI_LIEN_DELETION", getCreatingFlexi());
        }
        if (debitLoanType != null && debitLoanType.size() > 0) {
            transaction.putAll(debitLoanType);
        }
        transaction.put(CommonConstants.MODULE, getModule());
        transaction.put(CommonConstants.SCREEN, getScreen());
        if (procChargeHash.size() > 0) {
            transaction.put("PROCCHARGEHASH", procChargeHash);
        }
        if (multiple_ALL_LOAN_AMOUNT != null && multiple_ALL_LOAN_AMOUNT.size() > 0) {

            if (isPenalWaiveOff()) {
                multiple_ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF", "Y");
            } else {
                multiple_ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF", "N");
            }
            transaction.put("MULTIPLE_ALL_AMOUNT", multiple_ALL_LOAN_AMOUNT);
        }

        if (isRebateInterest()) {
            transaction.put("REBATE_INTEREST", "Y");
        } else {
            transaction.put("REBATE_INTEREST", "N");
        }

        if (getEmiNoInstallment() > 0) {
            transaction.put("EMI_INSTALLMENT", new Long(getEmiNoInstallment()));
        }
        if (parameter != null && parameter.length() > 0) {
            transaction.put("EXCEPTION", "EXCEPTION");
        }
        if (getDepInterestAmt().equals("DEP_INTEREST_AMT")) {
            transaction.put("DEP_INTEREST_AMT", "DEP_INTEREST_AMT");
        }
        if (!getDepLinkBatchId().equals("") && getDepLinkBatchId().equals("DEP_LINK")) {
            transaction.put("LINK_BATCH_ID", getDepAccNO());
        }
        if (valueDateFlag) {
            transaction.put("VALUE_DATE", valueDateMap);
        }

        if (getTransType() == CommonConstants.DEBIT) {
            transaction.put("RESPONDING", "RESPONDING");
        }
//        transaction.put("ORG_RESP_DETAILS", getOrgRespList());
        if (deletedList != null && deletedList.size() > 0) {
            transaction.put("DELETEDLIST", deletedList);
        }
        if (getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            transaction.put("STATUS_ORG", "MODIFIED");
        } else if (getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
            transaction.put("STATUS_ORG", "DELETED");
        }

        if (isAdviceAccount() == true) {
            transaction.put("ADVICE_REQ_DETAILS", getAdviceList());
        }
        transaction.put("TRANS_DATE", getTransDate());
        transaction.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
        return transaction;
    }

    public HashMap checkLoanDebit(String instrumentType) {
        loanDebitType = new HashMap();
        if (authorizeMap != null) {
            ArrayList toList = new ArrayList();
            toList = (ArrayList) authorizeMap.get("AUTHORIZEDATA");
            for (int i = 0; i < toList.size(); i++) {
                TxTransferTO obj = (TxTransferTO) toList.get(i);
                if (obj.getProdType().equals("TL") && obj.getTransType().equals("DEBIT") || obj.getProdType().equals("ATL") && obj.getTransType().equals("DEBIT") || (obj.getProdType().equals("AD") && obj.getTransType().equals("DEBIT")
                        || obj.getProdType().equals("AAD") && obj.getTransType().equals("DEBIT"))) {  //&& obj.getInstType().equals("VOUCHER")
                    loanDebitType.put("DEBIT_LOAN_TYPE", obj.getAuthorizeRemarks());
                }
            }
        } else {
            int res = 0;
            do {
                loanDebitType = new HashMap();
                if (getProductTypeValue().equals("TL") && getTransType().equals("DEBIT") || getProductTypeValue().equals("ATL") && getTransType().equals("DEBIT") || (getProductTypeValue().equals("AAD") && getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))
                        || (getProductTypeValue().equals("AD") && getTransType().equals("DEBIT") && instrumentType.equals("VOUCHER"))) {
                    String[] debitType = {"DebitPrinciple", "Debit Interest", "Debit_Penal_Int", "Other_Charges"};
                    String var = "";
                    if (getLinkMap() != null && getLinkMap().containsKey("AS_CUSTOMER_COMES") && getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                        var = "DebitPrinciple";
                    } else {
                        var = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                    }
                    res = 0;
                    if (null != var && !var.equals("") && var.equals("Debit Interest")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DI");
                    }
                    if (null != var && !var.equals("") && var.equals("DebitPrinciple")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
                    }
                    if (null != var && !var.equals("") && var.equals("Debit_Penal_Int")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "DPI");
                    }
                    if (null != var && !var.equals("") && var.equals("Other_Charges")) {
                        loanDebitType.put("DEBIT_LOAN_TYPE", "OTHERCHARGES");
                    }
                    if (null != var && !var.equals("") && !var.equals("DebitPrinciple")) {
                        double clearbalance = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CLEAR_BALANCE")).doubleValue();
                        double availableBalance = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();

                        if (clearbalance == 0.0) {
                            res = ClientUtil.confirmationAlert("Caution : Disbursement Not Yet Done \n" + "Whether to Proceed");
                        }
                    }
                } else if (getProductTypeValue().equals("AD") && getTransType().equals("DEBIT") && (!instrumentType.equals("VOUCHER"))) {
                    loanDebitType.put("DEBIT_LOAN_TYPE", "DP");
                }
            } while (res != 0);
        }
        return loanDebitType;
    }

    private void setStatusForTOs() {
        TxTransferTO objTO;
        this.setTransStatus(CommonConstants.STATUS_DELETED);
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            objTO = this.setTOStatus(objTO);
            transferTOs.set(i, objTO);
        }
        objTO = null;
    }

    private void setReconcileStatusForTOs() {
        ReconciliationTO objTO;
        this.setTransStatus(CommonConstants.STATUS_DELETED);
        int size = this.reconciliationList.size();
        for (int i = 0; i < size; i++) {
            objTO = (ReconciliationTO) reconciliationList.get(i);
            objTO = this.setReconcileTOStatus(objTO);
            reconciliationList.set(i, objTO);
        }
        objTO = null;
    }

    private TxTransferTO getTxTransferTO() {
        TxTransferTO newTxTransferTO = new TxTransferTO();
        //Added by Nidhin for setting transModType 28-may-2015 Mantiz ID->10685
        if (!getProdDebitTransfer().equals("") && getProdDebitTransfer() != null && getProdDebitTransfer().length() > 0)
        {
            newTxTransferTO.setTransModType(getProdDebitTransfer());
            setProdDebitTransfer("");
        }
        if (!getProdCreditTransfer().equals("") && getProdCreditTransfer() != null && getProdCreditTransfer().length() > 0)
        {
            newTxTransferTO.setTransModType(getProdCreditTransfer());
            setProdCreditTransfer("");
        }
        newTxTransferTO.setBatchId(getBatchId());
        newTxTransferTO.setTransId(getTransId());
        newTxTransferTO.setAcHdId(getAccountHeadId());
        // If AcHdId is not passed... retrive from the database based on Product Type and Prod ID
        if ((getAccountHeadId() == null || getAccountHeadId().trim().equals(""))
                && getAccountHeadId() != null && !getProductId().trim().equals("") && !getMainProductTypeValue().equals("GL")) {
            String qry = "getAccountHead" + getMainProductTypeValue();
            HashMap where = new HashMap();
            where.put(CommonConstants.MAP_WHERE, getProductId());
            List lst = ClientUtil.executeQuery(qry, where);
            if (lst != null && lst.size() > 0) {
                newTxTransferTO.setAcHdId((String) lst.get(0));
            }
        }
        newTxTransferTO.setActNum(getAccountNo());
        newTxTransferTO.setInpAmount(CommonUtil.convertObjToDouble(getTransferAmount()));
        newTxTransferTO.setInpCurr(getCurrencyType());
        newTxTransferTO.setAmount(CommonUtil.convertObjToDouble(getAmount()));
        Date dtDate = DateUtil.getDateMMDDYYYY(getTransDate());
        if (dtDate != null) {
            Date Dt = (Date) curDate.clone();
            Dt.setDate(dtDate.getDate());
            Dt.setMonth(dtDate.getMonth());
            Dt.setYear(dtDate.getYear());
            newTxTransferTO.setTransDt(Dt);
        } else {
            newTxTransferTO.setTransDt(DateUtil.getDateMMDDYYYY(getTransDate()));
        }
        newTxTransferTO.setTransType(getTransType());
        newTxTransferTO.setInstType(getInstType());
        newTxTransferTO.setInstrumentNo1(getInstrumentNo1());
        newTxTransferTO.setInstrumentNo2(getInstrumentNo2());
        Date ttDate = DateUtil.getDateMMDDYYYY(getInstrumentDate());
        if (ttDate != null) {
            Date Tt = (Date) curDate.clone();
            Tt.setDate(ttDate.getDate());
            Tt.setMonth(ttDate.getMonth());
            Tt.setYear(ttDate.getYear());
            newTxTransferTO.setInstDt(Tt);
        } else {
            newTxTransferTO.setInstDt(DateUtil.getDateMMDDYYYY(getInstrumentDate()));
        }
        newTxTransferTO.setProdType(getMainProductTypeValue());
        newTxTransferTO.setParticulars(getParticulars());
        newTxTransferTO.setNarration(getNarration());
        newTxTransferTO.setProdId(getProductId());
        newTxTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
        newTxTransferTO.setLinkBatchId(getClosedAccNo());
        if (getMainProductTypeValue().equals("TL")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("ATL")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("AD")
                && getTransType().equals("DEBIT") || getMainProductTypeValue().equals("AAD")
                && getTransType().equals("DEBIT")) {
            newTxTransferTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(loanDebitType.get("DEBIT_LOAN_TYPE")));
        }
        if (getMainProductTypeValue().equals("TD") && getTransType().equals("DEBIT")) {
            double totAmt = CommonUtil.convertObjToDouble(getRenewDepAmt()).doubleValue();
            double depAmt = CommonUtil.convertObjToDouble(getTransDepositAmt()).doubleValue();
            double txtAmt = CommonUtil.convertObjToDouble(getAmount()).doubleValue();
            setTransDepositAmt(String.valueOf(depAmt));
            if (txtAmt > totAmt) {
                ClientUtil.showAlertWindow("Amount is exceeding for that Available Balance ...");
                newTxTransferTO.setAmount(new Double(0.0));
            }
        }
        if (renewalIntFlag == true)//this is atthe time renewal any interest amount is withdrawing only it will work
        {
            newTxTransferTO.setInstrumentNo1("INTEREST_AMT");
        }
        if (depositRenewalFlag == true)//this is atthe time renewal any deposit & interest amount is withdrawing only it will work
        {
            newTxTransferTO.setInstrumentNo2("DEPOSIT_RENEWAL");//any renewal is going through means that authorization go through renewal screen itself,transfer screen should not come.
        }
        newTxTransferTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        newTxTransferTO.setAuthorizeRemarks("BACK_DATED_TRANSACTION");
        return newTxTransferTO;
    }

    //for bulk transaction purpose we created form excel sheet to screen
    private boolean getTxTransferTOBulkTransaction() {
        TxTransferTO newTxTransferTO = new TxTransferTO();
        StringBuffer act_num = new StringBuffer();
        StringBuffer glBuffer = new StringBuffer();
        ArrayList allTransactionTOs = new ArrayList();
        ArrayList singleTransList = null;
        HashMap actMap = new HashMap();
        HashMap accountNoMap = new HashMap();
        HashMap removeGLMap = new HashMap();
        HashMap glMap = new HashMap();

        try {
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            int result = fc.showOpenDialog(null);
            if (result == fc.APPROVE_OPTION) {
                java.io.File selectedFile = fc.getSelectedFile();
                String name = selectedFile.getName();
                if (name.substring(name.indexOf(".") + 1, name.length()).equalsIgnoreCase("xls")) {
                    java.io.FileInputStream inpuStream = new java.io.FileInputStream(selectedFile);
                    HSSFWorkbook workbook = new HSSFWorkbook(inpuStream);//new FileInputStream(fileToBeRead));
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int rows = sheet.getPhysicalNumberOfRows();//sheet.getLastRowNum();//sheet.getPhysicalNumberOfRows();
                    if (valueDateMap == null) {
                        valueDateMap = new HashMap();
                    }
                    for (int i = 1; i < rows; i++) {
                        HSSFRow row = sheet.getRow(i);
                        newTxTransferTO = new TxTransferTO();
                        HSSFCell cell = null;
                        cell = row.getCell((short) 0);
                        if (CommonUtil.convertObjToStr(getCellValue(cell)).length() == 0) {
                            continue;
                        }
                        newTxTransferTO = (TxTransferTO) setBulkTransaction(row);
                        if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("GL")) {
                            removeGLMap.put(newTxTransferTO.getAcHdId(), newTxTransferTO.getAcHdId());
                            if (glBuffer.length() > 0) {
                                glBuffer.append(",");
                            }
                            glBuffer.append("'" + newTxTransferTO.getAcHdId() + "'");

                        } else if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")) {
                            accountNoMap.put(newTxTransferTO.getActNum(), newTxTransferTO.getActNum());
                        }
                        singleTransList = new ArrayList();
                        singleTransList.add(newTxTransferTO);
                        allTransactionTOs.add(singleTransList);
                        if (CommonUtil.convertObjToStr(newTxTransferTO.getProdType()).equals("OA")) {
                            if (act_num.length() > 0) {
                                act_num.append(",");
                            }
                            act_num.append("'" + newTxTransferTO.getActNum() + "'");
                        }
                    }
                    actMap.put("ACT_NUM", act_num);
                    if (glBuffer != null && glBuffer.length() > 0) {
                        glMap.put("ACT_NUM", glBuffer);
                    }
                    if (verifiyOperativeAcctNo(actMap, allTransactionTOs, accountNoMap, glMap, removeGLMap)) {
                        return true;
                    }
                    inpuStream.close();
                } else {
                    ClientUtil.displayAlert("Please Select file name should be in Excel format");
                    return true;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean verifiyOperativeAcctNo(HashMap actMap, ArrayList allTransactionTOs, HashMap accountNoMap, HashMap glMap, HashMap removeGLMap) {
        StringBuffer act_num = new StringBuffer();
        ArrayList tempList = new ArrayList();
        ArrayList objList = null;
        HashMap singleMap = new HashMap();
        HashMap gldataMap = new HashMap();
        List glList = null;
        actMap.put(CommonConstants.MAP_WHERE, actMap.get("ACT_NUM"));
        if (glMap != null && glMap.size() > 0) {
            gldataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            gldataMap.put("BULK_TRANS", "BULK_TRANS");
            gldataMap.put(CommonConstants.MAP_WHERE, glMap.get("ACT_NUM"));
            glList = ClientUtil.executeQuery("Transfer.getSelectAcctHeadDBBulk", gldataMap);
        }
        List lst = ClientUtil.executeQuery("getVerifiedActNumber", actMap);
        for (int j = 0; j < allTransactionTOs.size(); j++) {

            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    singleMap = (HashMap) lst.get(i);
                    String operativeActNum = CommonUtil.convertObjToStr(singleMap.get("ACT_NUM"));
                    String achd = CommonUtil.convertObjToStr(singleMap.get("AC_HD_ID"));
                    String prodId = CommonUtil.convertObjToStr(singleMap.get("PROD_ID"));
                    String branchCode = CommonUtil.convertObjToStr(singleMap.get("BRANCH_CODE"));

                    objList = new ArrayList();
                    objList = (ArrayList) allTransactionTOs.get(j);
                    TxTransferTO obj = (TxTransferTO) objList.get(0);
                    if (obj.getActNum().equals(operativeActNum)) {
                        obj.setAcHdId(achd);
                        obj.setBranchId(branchCode);
                        obj.setProdId(prodId);
                        obj.setProdType("OA");
                        accountNoMap.remove(obj.getActNum());
                        objList.set(0, obj);
                        tempList.add(objList);
                    }
                }
            }
            //gl transaction
            if (glList != null && glList.size() > 0) {
                for (int i = 0; i < glList.size(); i++) {
                    singleMap = (HashMap) glList.get(i);
                    String achd = CommonUtil.convertObjToStr(singleMap.get("A/C Head"));
                    String branchCode = CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID);
                    objList = new ArrayList();
                    objList = (ArrayList) allTransactionTOs.get(j);
                    TxTransferTO obj = (TxTransferTO) objList.get(0);
                    if (obj.getAcHdId().equals(achd)) {
                        obj.setAcHdId(achd);
                        obj.setBranchId(branchCode);
                        obj.setProdId("");
                        obj.setProdType("GL");
                        removeGLMap.remove(achd);
                        objList.set(0, obj);
                        tempList.add(objList);
                    }
                }
            }
        }

        if (accountNoMap != null && accountNoMap.size() > 0) {
            java.util.Set keySet = (java.util.Set) accountNoMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int x = 0;
            for (int i = 0; i < keySet.size(); i++) {
                act_num.append("" + accountNoMap.get(objKeySet[i]) + ",");
                x++;
                if (x == 5) {
                    act_num.append("\n");
                    x = 0;
                }
            }
        }
        if (removeGLMap != null && removeGLMap.size() > 0) {
            java.util.Set keySet = (java.util.Set) removeGLMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            int x = 0;
            for (int i = 0; i < keySet.size(); i++) {
                act_num.append("" + glMap.get(objKeySet[i]) + ",");
                x++;
                if (x == 5) {
                    act_num.append("\n");
                    x = 0;
                }
            }
        }
        if (act_num != null && act_num.length() > 0) {
            ClientUtil.showMessageWindow("Please check the Excel sheet data. Following A/C Nos are Wrong " + act_num.toString());
            return true;
        }

        if (tempList != null && tempList.size() > 0) {
            for (int i = 0; i < tempList.size(); i++) {
                objList = new ArrayList();
                objList = (ArrayList) tempList.get(i);
                TxTransferTO obj = (TxTransferTO) objList.get(0);
                transferTOs.add(obj);
                valueDateMap.put(String.valueOf(transferTOs.size() - 1), getValueDate());
                ArrayList irRow = this.setRow(obj);
                tbmTransfer.insertRow(tbmTransfer.getRowCount(), irRow);
            }
        }
        return false;
    }

    private TxTransferTO setBulkTransaction(HSSFRow row) {
        TxTransferTO obj = new TxTransferTO();
        HSSFCell cell = null;
        cell = row.getCell((short) 0);
        String prodType = CommonUtil.convertObjToStr(getCellValue(cell));
        obj.setProdType(CommonUtil.convertObjToStr(getCellValue(cell)));
        cell = row.getCell((short) 1);
        if (prodType.equals("OA")) {
            obj.setActNum(CommonUtil.convertObjToStr(getCellValue(cell)));
        } else if (prodType.equals("GL")) {
            obj.setAcHdId(CommonUtil.convertObjToStr(getCellValue(cell)));
        }
        cell = row.getCell((short) 2);
        obj.setTransType(CommonUtil.convertObjToStr(getCellValue(cell)));
        cell = row.getCell((short) 3);
        obj.setAmount(CommonUtil.convertObjToDouble(getCellValue(cell)));
        obj.setInpAmount(CommonUtil.convertObjToDouble(getCellValue(cell)));
        cell = row.getCell((short) 4);
        obj.setInstType(getCellValue(cell));
        cell = row.getCell((short) 5);
        obj.setInstrumentNo1(getCellValue(cell));
        cell = row.getCell((short) 6);
        obj.setInstrumentNo2(getCellValue(cell));
        cell = row.getCell((short) 7);
        double dv = CommonUtil.convertObjToDouble(getCellValue(cell)).doubleValue();
        if (HSSFDateUtil.isValidExcelDate(dv)) {
            Date date = HSSFDateUtil.getJavaDate(dv);
            obj.setInstDt(date);
        }
        cell = row.getCell((short) 8);
        obj.setParticulars(getCellValue(cell));
        obj.setBatchId("-");
        obj.setTransId("-");
        obj.setTransDt(curDate);
        obj.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        obj.setInitChannType(TrueTransactMain.BRANCH_ID);
        obj.setInitTransId(TrueTransactMain.USER_ID);
        obj.setInpCurr("INR");
        obj.setInstDt(curDate);
        obj.setTransMode("TRANSFER");
        obj.setLinkBatchId(obj.getActNum());
        obj.setStatusDt(curDate);
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatus("CREATED");
        return obj;
    }

    public String getCellValue(HSSFCell cell) {
        String rowFields = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    rowFields += cell.getNumericCellValue();
                    break;
                case 1:
                    rowFields += cell.getStringCellValue();
                    break;
            }
        }
        return rowFields;
    }

    public boolean calcRecurringDates() {
        String prodId = CommonUtil.convertObjToStr(getProductId());
        HashMap recurrMap = new HashMap();
        recurrMap.put("PROD_ID", prodId);
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
        if (lst != null && lst.size() > 0) {
            HashMap recurringMap = (HashMap) lst.get(0);
            if (getMainProductTypeValue().equals("TD") && getTransType().equals("CREDIT") && recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                String depositNo = getAccountNo();
                if (depositNo.lastIndexOf("_") != -1) {
                    depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                }
                recurrMap.put("DEPOSIT_NO", depositNo);
                lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurrMap);
                if (lst != null && lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    double depAmt = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                    double totInstall = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALLMENTS")).doubleValue();
                    double totPaid = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                    double totAmt = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_BALANCE")).doubleValue();
                    double balAmt = depAmt * totInstall;
                    double payAmt = balAmt - totAmt;
                    double txtAmt = CommonUtil.convertObjToDouble(getAmount()).doubleValue();
                    double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                    if (penalAmt > 0) {
                        payAmt = payAmt + penalAmt;
                    }
                    if (payAmt >= txtAmt && payAmt != 0) {
                    } else {
                        ClientUtil.showAlertWindow("Exceeding the total Installments Amount...\n"
                                + "Balance Amount is " + payAmt);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction(String parameter) {
        TTException exception = null;
        HashMap proxyReturnMap = null;
        boolean oaExist = true;
        try {
            if ((!getBatchId().equals("-")) && getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE && getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
                HashMap whereMap = new HashMap();
                whereMap.put("BATCH_ID", getBatchId());
                //screen lock
                HashMap maps = new HashMap();
                maps.put("USER_ID", ProxyParameters.USER_ID);
                maps.put("TRANS_ID", getBatchId());
                maps.put("TRANS_DT", curDate.clone());
                maps.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List lstlock = ClientUtil.executeQuery("selectauthorizationLock", maps);
                if (lstlock != null && lstlock.size() > 0) {
                    HashMap map = new HashMap();
                    StringBuffer open = new StringBuffer();
                    for (int i = 0; i < lstlock.size(); i++) {
                        map = (HashMap) lstlock.get(i);
                        open.append("\n" + "User Id  :" + " ");
                        open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                        open.append("Mode Of Operation  :" + " ");
                        open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
                    }
                    ClientUtil.showMessageWindow("Already opened by" + open);
                    return;
                }
                whereMap.put("TRANS_DT", curDate.clone());
                whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List lst = ClientUtil.executeQuery("getTransferAuthorizeStatus", whereMap);
                if (lst != null && lst.size() > 0) {
                    whereMap = (HashMap) lst.get(0);
                    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                    if (!authStatus.equals("") && getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION) {
                        setOperation(ClientConstants.ACTIONTYPE_FAILED);
                        throw new TTException("This transaction already " + authStatus.toLowerCase() + " by " + authBy);
                    }
                }
            }

            boolean debitAndCreditBranchFlag = false;
            if (this.authorizeMap != null) {
                HashMap autho = new HashMap();
                autho.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                autho.put(CommonConstants.MODULE, getModule());
                autho.put(CommonConstants.SCREEN, getScreen());
                autho.put("OLDAMOUNT", null);
                if (ALL_LOAN_AMOUNT != null) {
                    autho.put("ALL_AMOUNT_SUBSIDY", ALL_LOAN_AMOUNT);
                }
                checkLoanDebit("debitloanType");
                if (loanDebitType != null && loanDebitType.size() > 0) {
                    autho.putAll(loanDebitType);
                }
                if (parameter != null && parameter.length() > 0) {
                    autho.put("EXCEPTION", "EXCEPTION");
                }
                if (authMap.containsKey("LINK_BATCH_ID")) {
                    autho.put("LINK_BATCH_ID", authMap.get("LINK_BATCH_ID"));
                    autho.put("BATCH_ID", authMap.get("BATCH_ID"));
                    autho.put("REMARKS", "Due to Processing Charge");
                }
                if (getHoAccountCr().equals("CREDIT") || getHoAccountDr().equals("DEBIT")) {
                    if (getTransType() == CommonConstants.DEBIT) {
                        autho.put("RESPONDING", "RESPONDING");
                    }
//                    autho.put("ORG_RESP_DETAILS", getOrgRespList());
                }
                if (isAdviceAccount() == true) {
                    autho.put("ADIVE_REQ_DETAILS", getAdviceList());
                }
                autho.put("TRANS_DATE", getProperDateFormat(getTransDate()));
                autho.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
                proxyReturnMap = proxy.execute(autho, map);
                authorizeMap = null;
            } else {
                debitAndCreditBranchFlag = getDebitAndCreditBranchFlag();
                if (!debitAndCreditBranchFlag) {
                    ClientUtil.displayAlert("In an Interbranch transaction either Dr or Cr A/c should belongs to initiating branch");
                } else {
                    proxyReturnMap = proxy.execute(populateBean(parameter), map);
                }
            }
            if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID) && proxyReturnMap.get(CommonConstants.TRANS_ID) != null && debitAndCreditBranchFlag) {
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                // System.out.println("#$#$$ yesNo : "+yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransId", proxyReturnMap.get(CommonConstants.TRANS_ID));
//                    paramMap.put("TransDt", curDate);
                    paramMap.put("TransDt", getTransDate());
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                }
                debitLoanType = new HashMap();
                ALL_LOAN_AMOUNT = new HashMap();
            }
            reconcileMap = null;
            setCreatingFlexi("");
            setFlexiAmount(0.0);
            setDepositTransId(CommonUtil.convertObjToStr(proxyReturnMap.get("TRANS_ID")));
            interestAmt = depTrans();
            setInterestAmt(interestAmt);
            setResult(getOperation());
            operation = ClientConstants.ACTIONTYPE_CANCEL;
            minBalException = false;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();

            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }

        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                if (list != null && list.size() > 0 && list.get(0) instanceof String && ((String) list.get(0)).startsWith("SUSPECIOUS")
                        || CommonUtil.convertObjToStr(list.get(0)).equals("ESL")) {//  || CommonUtil.convertObjToStr(list.get(0)).equals("AED") || CommonUtil.convertObjToStr(list.get(0)).equals("AEL")  AEL ,AED REMOVED FROM EXCPTION TRANSACTION  SRINATH SIR TOLD(03-JAN-2014) REMOVED BY ABI
                    TTException tt = new TTException(CommonUtil.convertObjToStr(list.get(0)));
                    Object[] dialogOption = {"EXCEPTION", "CANCEL"};
                    parseException.setDialogOptions(dialogOption);
                    if (parseException.logException(tt, true) == 0) {
                        try {
                            doAction("EXCEPTION");
                        } catch (Exception e) {
                            if (e instanceof TTException) {
                                Object[] dialog = {"OK"};
                                parseException.setDialogOptions(dialog);
                                exception = (TTException) e;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else if (CommonUtil.convertObjToStr(list.get(0)).equals("MIN")) {
                    minBalException = true;
                    Object[] dialogOption = {"CONTINUE", "CANCEL"};
                    parseException.setDialogOptions(dialogOption);
                    if (parseException.logException(exception, true) == 0) {
                        try {
                            doAction("EXCEPTION");
                        } catch (Exception e) {
                            if (e instanceof TTException) {
                                Object[] dialog = {"OK"};
                                parseException.setDialogOptions(dialog);
                                exception = (TTException) e;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else {
                    parseException.logException(exception, true);
                }
            } else {
                parseException.logException(exception, true);
                setResult(getOperation());
            }
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date properDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            properDt = (Date) curDate.clone();
            properDt.setDate(tempDt.getDate());
            properDt.setMonth(tempDt.getMonth());
            properDt.setYear(tempDt.getYear());
        }
        return properDt;
    }

    public double depTrans() {
        HashMap depMap = new HashMap();
        double intAmt = 0.0;
        double amt = 0.0;
        depMap.put("BATCH_ID", getDepositTransId());
        depMap.put("TRANS_DT", curDate.clone());
        depMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getTransactionAmount", depMap);
        if (lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                depMap = (HashMap) lst.get(i);
                amt = CommonUtil.convertObjToDouble(depMap.get("AMOUNT")).doubleValue();
                intAmt = amt + intAmt;
            }
        }
        return intAmt;
    }

    private ArrayList setActNumBlankForGL() {
        for (int i = 0; i < transferTOs.size(); i++) {
            TxTransferTO txtransferTo = (TxTransferTO) transferTOs.get(i);
            if (txtransferTo.getProdType().equals("GL")) {
                txtransferTo.setActNum("");
            }
        }
        return transferTOs;
    }

    private boolean checkForOA() {
        String loanProdId = "";
        HashMap hash = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap actHash = new HashMap();
        String actNum = "";
        if (getLoanActNo().lastIndexOf("_") != -1) {
            actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
        } else {
            actNum = getLoanActNo();
        }
        whereMap.put(CommonConstants.MAP_NAME, "getProcPercentageTL");
        actHash.put("ACT_NUM", actNum);
        whereMap.put(CommonConstants.MAP_WHERE, actHash);
        double procPer = 0.0, oaAmt = 0.0, procAmt = 0.0;
        try {
            hash = proxy.executeQuery(whereMap, map);
            ArrayList a = (ArrayList) hash.get(CommonConstants.DATA);
            HashMap b = (HashMap) a.get(0);
            procPer = CommonUtil.convertObjToDouble(b.get("PROC_CHRG_PER")).doubleValue();
            loanProdId = CommonUtil.convertObjToStr(b.get("PROD_ID"));
            String ACT_NUM = getAccountNo();
            whereMap.put(CommonConstants.MAP_WHERE, ACT_NUM);
            whereMap.put(CommonConstants.MAP_NAME, "getOABalanceTransferTL");
            hash = proxy.executeQuery(whereMap, map);
            hash = (HashMap) ((ArrayList) hash.get(CommonConstants.DATA)).get(0);
            hash.put("LOAN_PROD_ID", loanProdId);
            oaAmt = Double.parseDouble(hash.get("CLEAR_BALANCE").toString());
            procChargeHash = new HashMap();
            if (procPer > 0) {
                procChargeHash.putAll(hash);
            }
            a = null;
            b = null;
        } catch (Exception e) {
        }
        double amt = Double.parseDouble(transAmt.replaceAll(",", ""));
        procAmt = amt * (procPer / 100);
        procChargeHash.put("LINK_BATCH_ID", actNum);
        procChargeHash.put("PROC_AMT", new Double(procAmt));
        procChargeHash.put("OA_ACT_NUM", hash.get("ACT_NUM"));
        procChargeHash.put("OA_PROD_ID", hash.get("PROD_ID"));
        procChargeHash.put("TL_PROD_ID", loanProdId);
        procChargeHash.put("PROC_AMT", new Double(procAmt));
        hash = null;
        whereMap = null;
        actHash = null;
        if (procAmt > oaAmt) {
            int result = ClientUtil.confirmationAlert("Insufficient Balance in Operative A/c to Debit Processing amount.Collect Manually..");
            operation = ClientConstants.ACTIONTYPE_CANCEL;
            if (result == 0) {
                procChargeHash = new HashMap();
            }
            return true;
        }
        return true;
    }

    private TxTransferTO setTransactiontoTLAD(TxTransferTO txtransTo) {
        TxTransferTO txTo = new TxTransferTO();
        txTo.setProdType(txtransTo.getProdType());
        txTo.setProdId(txtransTo.getProdId());
        txTo.setActNum(txtransTo.getActNum());
        txTo.setAcHdId(txtransTo.getAcHdId());
        txTo.setAmount(txtransTo.getAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setAuthorizeDt(txtransTo.getAuthorizeDt());
        txTo.setAuthorizeRemarks(txtransTo.getAuthorizeRemarks());
        txTo.setAuthorizeStatus(txtransTo.getAuthorizeStatus());
        txTo.setBatchId(txtransTo.getBatchId());
        txTo.setBranchId(txtransTo.getBranchId());
        txTo.setInitChannType(txtransTo.getInitChannType());
        txTo.setInitTransId(txtransTo.getInitTransId());
        txTo.setInitiatedBranch(txtransTo.getInitiatedBranch());
        txTo.setInpAmount(txtransTo.getInpAmount());
        txTo.setAuthorizeBy(txtransTo.getAuthorizeBy());
        txTo.setTransDt(txtransTo.getTransDt());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setInstType(txtransTo.getInstType());
        txTo.setInstrumentNo1(txtransTo.getInstrumentNo1());
        txTo.setInstrumentNo2(txtransTo.getInstrumentNo2());
        txTo.setInstDt(txtransTo.getInstDt());
        txTo.setTransId(txtransTo.getTransId());
        txTo.setTransMode(txtransTo.getTransMode());
        txTo.setTransType(txtransTo.getTransType());
        txTo.setStatus(txtransTo.getStatus());
        txTo.setStatusBy(txtransTo.getStatusBy());
        txTo.setStatusDt(txtransTo.getStatusDt());
        return txTo;
    }

    public HashMap asAnWhenCustomerComesYesNO(String acct_no, String batch_id) {
        HashMap map = new HashMap();
        List lst = null;
        if (batch_id == null) {
            map.put("ACT_NUM", acct_no);
        } else {
            map.put("BATCH_ID", batch_id);
        }
        map.put("TRANS_DT", curDate.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        if (acct_no != null && acct_no.length() > 0) {
            lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        }
        if (lst == null || lst.isEmpty()) {
            lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            setLinkMap(map);
        } else {
            setLinkMap(null);
        }
        return map;
    }

    public HashMap depositPenalReceving(String batch_id) {
        HashMap map = new HashMap();
        List lst = null;
        if (!CommonUtil.convertObjToStr(batch_id).equals("")) {
            map.put("BATCH_ID", batch_id);
            map.put("TRANS_DT", curDate.clone());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("getDepositRecurringList", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                setPenalMap(map);
            }
        }
        return map;
    }

    public List getDocumentDetail(String mapName, String mapWhere) {
        List lst = null;
        HashMap hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME, mapName);
        hash.put(CommonConstants.MAP_WHERE, mapWhere);
        try {
            lst = (List) proxy.executeQuery(hash, map).get(CommonConstants.DATA);
        } catch (Exception e) {
        }
        hash = null;
        return lst;
    }

    /*
     * this method will check if the batch has been tallied or not
     */
    public boolean isBatchTalliedArrayList() {
        int size = this.transferTOs.size();
        double creditAmt = 0, debitAmt = 0;
        TxTransferTO objTO;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            if (!objTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                if (objTO.getTransType().equals(CommonConstants.DEBIT)) {
                    debitAmt += objTO.getAmount().doubleValue();
                    long dr = roundOff((long) (debitAmt * 1000));
                    debitAmt = dr / 100.0;
                } else if (objTO.getTransType().equals(CommonConstants.CREDIT)) {
                    creditAmt += objTO.getAmount().doubleValue();
                    long cr = roundOff((long) (creditAmt * 1000));
                    creditAmt = cr / 100.0;
                }
            }
        }
        if (creditAmt == debitAmt) {
            return true;
        }
        return false;
    }

    /**
     * this mehtod will return the basic account information for the transfering
     * account, as the data elements in the hashmap
     */
    public HashMap getTransActDetails(String actNum) {
        // if there is no specified account number then return a null hashmap
        // System.out.println("from Trans Details"+actNum);
        if (actNum == null || actNum.equals("")) {
            return null;
        }
        HashMap myMap = new HashMap();
        myMap.put("ACT_NUM", actNum);
        myMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getBalance" + this.getMainProductTypeValue(), myMap);
        HashMap resultMap = (HashMap) resultList.get(0);
        ArrayList resultList1 = (ArrayList) ClientUtil.executeQuery("getActData" + this.getMainProductTypeValue(), myMap);
        HashMap resultMap1 = null;
        if (resultList1 != null && resultList1.size() > 0) {
            resultMap1 = (HashMap) resultList1.get(0);
            resultMap.putAll(resultMap1);
        }
        return resultMap;
    }

    public ComboBoxModel getProductTypeModel() {
        return productTypeModel;
    }

    public void setProductTypeModel(ComboBoxModel _productTypeModel) {
        productTypeModel = _productTypeModel;
        setChanged();
    }

    public String getProductTypeValue() {
        return productTypeValue;
    }

    public void setProductTypeValue(String _productTypeValue) {
        productTypeValue = _productTypeValue;
        setChanged();
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String _batchId) {
        batchId = _batchId;
        setChanged();
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String _transId) {
        transId = _transId;
        setChanged();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String _productId) {
        productId = _productId;
        setChanged();
    }

    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }

    public void setAccountHeadDesc(String _accountHeadDesc) {
        accountHeadDesc = _accountHeadDesc;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String _accountNo) {
        accountNo = _accountNo;
        setChanged();
    }

    public ComboBoxModel getInstrumentTypeModel() {
        return instrumentTypeModel;
    }

    public void setInstrumentTypeModel(ComboBoxModel _instrumentTypeModel) {
        instrumentTypeModel = _instrumentTypeModel;
        setChanged();
    }

    public String getInstrumentTypeValue() {
        return instrumentTypeValue;
    }

    public void setInstrumentTypeValue(String _instrumentTypeValue) {
        instrumentTypeValue = _instrumentTypeValue;
        setChanged();
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    public void setInstrumentNo1(String _instrumentNo1) {
        instrumentNo1 = _instrumentNo1;
        setChanged();
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
    }

    public void setInstrumentNo2(String _instrumentNo2) {
        instrumentNo2 = _instrumentNo2;
        setChanged();
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(String _instrumentDate) {
        instrumentDate = _instrumentDate;
        setChanged();
    }

    public ComboBoxModel getCurrencyTypeModel() {
        return currencyTypeModel;
    }

    public void setCurrencyTypeModel(ComboBoxModel _currencyTypeModel) {
        currencyTypeModel = _currencyTypeModel;
        setChanged();
    }

    public String getCurrencyTypeValue() {
        return currencyTypeValue;
    }

    public void setCurrencyTypeValue(String _currencyTypeValue) {
        currencyTypeValue = _currencyTypeValue;
        setChanged();
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String _currencyType) {
        currencyType = _currencyType;
        setChanged();
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String _transferAmount) {
        transferAmount = _transferAmount;
        setChanged();
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String _particulars) {
        particulars = _particulars;
        setChanged();
    }

    public int getTotalDrInstruments() {
        return totalDrInstruments;
    }

    public void setTotalDrInstruments(int _totalDrInstruments) {
        totalDrInstruments = _totalDrInstruments;
        setChanged();
    }

    public double getTotalDrAmount() {
        return totalDrAmount;
    }

    public void setTotalDrAmount(double _totalDrAmount) {
        totalDrAmount = _totalDrAmount;
        setChanged();
    }

    public int getTotalCrInstruments() {
        return totalCrInstruments;
    }

    public void setTotalCrInstruments(int _totalCrInstruments) {
        totalCrInstruments = _totalCrInstruments;
        setChanged();
    }

    public double getTotalCrAmount() {
        return totalCrAmount;
    }

    public void setTotalCrAmount(double _totalCrAmount) {
        totalCrAmount = _totalCrAmount;
        setChanged();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String _status) {
        status = _status;
        setChanged();
    }

    public String getAccountHeadId() {
        return accountHeadId;
    }

    public void setAccountHeadId(String _accountHeadId) {
        accountHeadId = _accountHeadId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String _action) {
        action = _action;
    }

    public String getAmount() {
        return getTransferAmount();
    }

    public void setAmount(String _amount) {
        amount = _amount;
        setChanged();
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String _transDate) {
        transDate = _transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String _transType) {
        transType = _transType;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String _instType) {
        instType = _instType;
    }

    public String getInitTransId() {
        return initTransId;
    }

    public void setInitTransId(String _initTransId) {
        initTransId = _initTransId;

    }

    public String getInitChannType() {
        return initChannType;
    }

    public void setInitChannType(String _initChannType) {
        initChannType = _initChannType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String _transStatus) {
        transStatus = _transStatus;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
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
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getOperation()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public void getProducts() {
        List list = null;
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        HashMap data;
        if (getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
            list = ClientUtil.executeQuery("Transfer.getCreditProduct" + getMainProductTypeValue(), null);
        } else if (getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
            list = ClientUtil.executeQuery("Transfer.getDebitProduct" + getMainProductTypeValue(), null);
        }
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                data = (HashMap) list.get(i);
                key.add(data.get("PRODID"));
                value.add(data.get("PRODDESC"));
            }
        }
        data = null;
        productTypeModel = new ComboBoxModel(key, value);
        setChanged();
    }

    public void getTransferData() {
        HashMap whereMap = new HashMap();
        whereMap.put("BATCHID", batchId);
        whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDate)));
        whereMap.put("INITIATED_BRANCH", initBran);
        whereMap.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
        String mapName = "getBatchTxTransferTOs";
        List list = null;
        if (this.operation == ClientConstants.ACTIONTYPE_REJECT
                || this.operation == ClientConstants.ACTIONTYPE_EXCEPTION
                || this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            mapName = "getBatchTxTransferTOsAuthorize";
        }
        if (this.operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
            whereMap.put("AUTHORIZE_STATUS", "EXCEPTION");
        }
        if (this.operation == ClientConstants.ACTIONTYPE_REJECT) {
            whereMap.put("AUTHORIZE_STATUS", "REJECT");
        }
        if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            whereMap.put("AUTHORIZE_STATUS", "AUTHORIZE");
        }
        whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDate)));
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        list = ClientUtil.executeQuery(mapName, whereMap);
        if (list != null && list.size() > 0) {
            this.transferTOs = (ArrayList) list;
            setTableData();
            //scrrenlock
            HashMap hash = new HashMap();
            hash.put("USER_ID", ProxyParameters.USER_ID);
            hash.put("TRANS_ID", batchId);
            if (this.operation == ClientConstants.ACTIONTYPE_REJECT) {
                hash.put("MODE_OF_OPERATION", "REJECT");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
                hash.put("MODE_OF_OPERATION", "EXCEPTION");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                hash.put("MODE_OF_OPERATION", "AUTHORIZE");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_EDIT) {
                hash.put("MODE_OF_OPERATION", "EDIT");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_DELETE) {
                hash.put("MODE_OF_OPERATION", "DELETE");
            }
            if (this.operation == ClientConstants.ACTIONTYPE_VIEW) {
                hash.put("MODE_OF_OPERATION", "VIEW");
            }
            hash.put("TRANS_DT", curDate.clone());
            hash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("insertauthorizationLock", hash);
        } else {
            resetTable();
        }
        whereMap = null;
    }

    private void setTableData() {
        ArrayList row;
        ArrayList rows = new ArrayList();
        TxTransferTO obj;
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) this.transferTOs.get(i);
            authorizeAccountNo(obj);
            row = setRow(obj);
            setOldAmountMap(obj);
            rows.add(row);
        }
        setTable();
        tbmTransfer.setData(rows);
        tbmTransfer.fireTableDataChanged();
        obj = null;
    }

    private void setOldAmountMap(TxTransferTO obj) {
        this.oldAmountMap.put(obj.getTransId(), obj.getAmount());
    }

    private ArrayList setRow(TxTransferTO obj) {
        ArrayList row = new ArrayList();
        row.add(obj.getActNum());
        row.add(obj.getBatchId());
        row.add(obj.getTransId());
        row.add(obj.getAmount());
        row.add(obj.getTransType());
        if (obj.getProdType().equals("GL") && obj.getTransType().equals("DEBIT")) {
            row.set(0, null);
        }
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            row.add("No");
        }
        return row;
    }

    public void authorizeAccountNo(TxTransferTO obj) {
        if (obj.getTransType().equals(CommonConstants.DEBIT) && obj.getProdType().equals("TL")) {
            String actNum;
            if (getLoanActNo().lastIndexOf("_") != -1) {
                actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
            } else {
                actNum = getLoanActNo();
            }
            authMap.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getLinkBatchValues", authMap);
            if (lst.size() > 0) {
                authMap = (HashMap) lst.get(0);
            }
        }
    }
    //Added By Suresh

    public void setVerificationStatus(String status) {
        if (tbmTransfer.getRowCount() > 0) {
            for (int i = 0; i < tbmTransfer.getRowCount(); i++) {
                tbmTransfer.setValueAt(status, i, 5);
            }
        }
    }

    public void populatTranferTO(int rowNum) {
        TxTransferTO obj = (TxTransferTO) transferTOs.get(rowNum);
        if (operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            tbmTransfer.setValueAt("Yes", rowNum, 5);
        }
        Date valueDt = null;
        if (valueDateMap != null && valueDateMap.containsKey(String.valueOf(rowNum))) {
            valueDt = (Date) valueDateMap.get(String.valueOf(rowNum));
        } else {
            valueDt = (Date) getCurDate().clone();
        }
        setValueDate(valueDt);
        this.setTransferTO(obj);
        if (getCurDate() != null && getValueDate() != null
                && DateUtil.dateDiff(getCurDate(), getValueDate()) != 0) {
            valueDateFlag = true;
        }
        obj = null;
    }

    public void deleteTransferData(int rowNum) {
        TxTransferTO obj = this.setTOStatus((TxTransferTO) this.transferTOs.get(rowNum));
        if (obj.getBatchId().compareToIgnoreCase("-") != 0) {
            if (oldAmountMap.containsKey(obj.getTransId())) {
                obj.setAmount((Double) oldAmountMap.get(obj.getTransId()));
                obj.setInpAmount((Double) oldAmountMap.get(obj.getTransId()));
            }
            deleteTOsList.add(obj);
        }
        transferTOs.remove(rowNum);
        tbmTransfer.removeRow(rowNum);
        tbmTransfer.fireTableDataChanged();
        obj = null;
    }

    public boolean moreThanOneCreditNotAllowed(String prodType) {
        TxTransferTO to = new TxTransferTO();
        HashMap recurringMap = new HashMap();
        int count = 0;
        int debitList = 0;
        boolean val = false;
        for (int i = 0; i < transferTOs.size(); i++) {
            to = (TxTransferTO) transferTOs.get(i);
            if (to.getProdType().equals("TL") && to.getTransType().equals("CREDIT") || prodType.equals("TL")) {
                val = true;
            }
            if (to.getProdType().equals("TD") && to.getTransType().equals("CREDIT") || prodType.equals("TD")) {
                String prodId = "";
                if (prodType.equals("TD")) {
                    prodId = CommonUtil.convertObjToStr(productTypeModel.getKeyForSelected());
                } else {
                    prodId = CommonUtil.convertObjToStr(to.getProdId());
                }
                recurringMap.put("PROD_ID", prodId);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
                if (lst != null && lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                        val = true;
                    }
                }
            }
            if (to.getTransType().equals("CREDIT")) {
                count++;
            }
            if (to.getTransType().equals("DEBIT")) {
                debitList++;
            }
        }
        return false;
    }

    public String getTermLoanBatch_id() {
        for (int i = 0; i < transferTOs.size(); i++) {
            TxTransferTO to = (TxTransferTO) transferTOs.get(i);
            if (to.getLinkBatchId() != null && to.getLinkBatchId().length() > 0) {
                if (to.getProdType().equals("GL") || ((to.getProdType().equals("AD") || to.getProdType().equals("TL")) && to.getTransType().equals("CREDIT"))) {
                    HashMap map = new HashMap();
                    String link = (String) to.getLinkBatchId();
                    map.put("WHERE", link);
                    HashMap maps = new HashMap();
                    maps.put("WHERE", map);
                    List lst = ClientUtil.executeQuery("getLastIntCalDateAD", maps);
                    if (lst != null && lst.size() > 0) {
                        return to.getBatchId();
                    }
                }
            }
        }
        return "";
    }

    public int insertTransferData(int rowNo) {
        if (valueDateMap == null) {
            valueDateMap = new HashMap();
        }
        ReconciliationTO reconciliationTO = new ReconciliationTO();
        TxTransferTO obj = null;
        if (isRdoBulkTransaction_Yes() && rowNo == -1 && getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            if (this.getTxTransferTOBulkTransaction()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            obj = this.getTxTransferTO();
        }
        if (getOperation() == ClientConstants.ACTIONTYPE_NEW && !getReconcile().equals("") && getReconcile().equals("Y")) {
            reconciliationTO.setAcHdId(getAccountHeadId());
            reconciliationTO.setTransAmount(obj.getAmount());
            reconciliationTO.setBalanceAmount(obj.getAmount());
            reconciliationTO.setTransType(getTransType());
            reconciliationTO.setTransMode(obj.getTransMode());
        }
        if (rowNo == -1) {
            if (this.getBatchId() == null || this.getBatchId().equalsIgnoreCase("")) {
                obj.setBatchId("-");
            }
            obj.setTransId("-");
            obj = setTOStatus(obj);
            transferTOs.add(tbmTransfer.getRowCount(), obj);
            if (!reconciliationTO.getAcHdId().equals("") && reconciliationTO.getAcHdId().length() > 0) {
                reconciliationList.add(reconciliationTO);
                setReconcileTOStatus(reconciliationTO);
            }
            valueDateMap.put(String.valueOf(transferTOs.size() - 1), getValueDate());
            ArrayList irRow = this.setRow(obj);
            tbmTransfer.insertRow(tbmTransfer.getRowCount(), irRow);//tbmTransfer.getRowCount()
        } else {
            obj = updateTransferTO((TxTransferTO) transferTOs.get(rowNo), obj);
            if (obj.getBatchId().compareToIgnoreCase("-") != 0) {
                obj = setTOStatus(obj);
            }
            ArrayList irRow = setRow(obj);
            if (obj.getTransId().equals("-")) {
                obj.setStatus(CommonConstants.STATUS_CREATED);
            }
            transferTOs.set(rowNo, obj);
            valueDateMap.put(String.valueOf(rowNo), getValueDate());
            tbmTransfer.removeRow(rowNo);
            tbmTransfer.insertRow(rowNo, irRow);
        }
        tbmTransfer.fireTableDataChanged();
        obj = null;
        return 0;
    }

    public TxTransferTO setTOStatus(TxTransferTO obj) {
        obj.setStatus(this.getTransStatus());
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);

        if (this.getTransStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
            obj.setBranchId(TrueTransactMain.selBranch);
            obj.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            obj.setInitTransId(TrueTransactMain.USER_ID);
            obj.setInitChannType(TrueTransactMain.BRANCH_ID);
        }
        return obj;
    }

    public ReconciliationTO setReconcileTOStatus(ReconciliationTO obj) {
        obj.setStatus(this.getTransStatus());
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);
        if (this.getTransStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
            obj.setBranchId(getSelectedBranchID());
            obj.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        }
        return obj;
    }

    private TxTransferTO updateTransferTO(TxTransferTO oldTO, TxTransferTO newTO) {
        oldTO.setAmount(newTO.getAmount());
        oldTO.setInpAmount(newTO.getInpAmount());
        oldTO.setInpCurr(newTO.getInpCurr());
        oldTO.setInstDt(newTO.getInstDt());
        oldTO.setInstType(newTO.getInstType());
        oldTO.setInstrumentNo1(newTO.getInstrumentNo1());
        oldTO.setInstrumentNo2(newTO.getInstrumentNo2());
        oldTO.setParticulars(newTO.getParticulars());
        oldTO.setNarration(newTO.getNarration());
        oldTO.setActNum(newTO.getActNum());
        oldTO.setTransMode(newTO.getTransMode());
        //oldTO.setTransDt(newTO.getTransDt());
        newTO = null;
        return oldTO;
    }

    public void resetTable() {
        this.tbmTransfer.setData(new ArrayList());
        this.tbmTransfer.fireTableDataChanged();
        this.transferTOs.clear();
        this.reconciliationList.clear();
        this.deleteTOsList.clear();
        this.oldAmountMap.clear();

    }

    // The following method added by Rajesh.
    // To check any interbranch transactions happened or not.
    public boolean checkForInterBranchTransExistance() {
        boolean isInterBranchTrans = false;
        if (getTransferTOs() != null && getTransferTOs().size() > 0) {
            for (int i = 0; i < getTransferTOs().size(); i++) {
                TxTransferTO objTxTransferTO = (TxTransferTO) getTransferTOs().get(i);
                if (objTxTransferTO != null) {
                    if (!objTxTransferTO.getBranchId().equals(TrueTransactMain.BRANCH_ID)) {
                        isInterBranchTrans = true;
                    }
                }
                objTxTransferTO = null;
            }
        }
        return isInterBranchTrans;
    }

    /**
     * Getter for property transferTOs.
     *
     * @return Value of property transferTOs.
     *
     */
    public ArrayList getTransferTOs() {
        return transferTOs;
    }

    /**
     * Setter for property transferTOs.
     *
     * @param transferTOs New value of property transferTOs.
     *
     */
    public void setTransferTOs(ArrayList transferTOs) {
        this.transferTOs = transferTOs;
    }

    /**
     * Getter for property deleteTOsList.
     *
     * @return Value of property deleteTOsList.
     *
     */
    public ArrayList getDeleteTOsList() {
        return deleteTOsList;
    }

    /**
     * Setter for property deleteTOsList.
     *
     * @param deleteTOsList New value of property deleteTOsList.
     *
     */
    public void setDeleteTOsList(ArrayList deleteTOsList) {
        this.deleteTOsList = deleteTOsList;
    }

    /**
     * Getter for property tbmTransfer.
     *
     * @return Value of property tbmTransfer.
     *
     */
    public TableModel getTbmTransfer() {
        return tbmTransfer;
    }

    /**
     * Setter for property tbmTransfer.
     *
     * @param tbmTransfer New value of property tbmTransfer.
     *
     */
    public void setTbmTransfer(TableModel tbmTransfer) {
        this.tbmTransfer = tbmTransfer;
    }

    public void checkOAbalanceForTL() {
        boolean oaExist;
        if (isCheckDebitTermLoan()) {
            String actNum;
            if (getLoanActNo().lastIndexOf("_") != -1) {
                actNum = getLoanActNo().substring(0, getLoanActNo().lastIndexOf("_"));
            } else {
                actNum = getLoanActNo();
            }
            HashMap checkClearBalance = new HashMap();
            checkClearBalance.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getClearBalance", checkClearBalance);
            if (lst != null && lst.size() > 0) {
                checkClearBalance = (HashMap) lst.get(0);
                long clearBalance = CommonUtil.convertObjToLong(checkClearBalance.get("CLEAR_BALANCE"));
                if (clearBalance == 0) {
                    if (getTransAmt().length() > 0) {
                        oaExist = checkForOA();
                    }
                }
                checkClearBalance = null;
            }
            lst = null;
        }
    }

    public int populateTransfer(String transId) {
        TxTransferTO obj;
        int size = this.transferTOs.size();
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) transferTOs.get(i);
            if (obj.getTransId().equals(transId)) {
                populatTranferTO(i);
                obj = null;
                return i;
            }
        }
        return -1;
    }

    public void getTransDetails() {
        TxTransferTO obj = null;
        int size = this.transferTOs.size();
        double crAmt = 0, dbAmt = 0;
        int crInst = 0, dbInst = 0;
        for (int i = 0; i < size; i++) {
            obj = (TxTransferTO) this.transferTOs.get(i);
            if (obj.getTransType().equalsIgnoreCase(CommonConstants.DEBIT)) {
                dbAmt += obj.getAmount().doubleValue();
                long dr = roundOff((long) (dbAmt * 1000));
                dbAmt = dr / 100.0;
                dbInst += 1;
            } else if (obj.getTransType().equalsIgnoreCase(CommonConstants.CREDIT)) {
                crAmt += obj.getAmount().doubleValue();
                long cr = roundOff((long) (crAmt * 1000));
                crAmt = cr / 100.0;
                crInst += 1;
            }
        }
        setTotalDrInstruments(new Integer(dbInst).intValue());
        setTotalDrAmount(new Double(dbAmt).doubleValue());
        setTotalCrInstruments(new Integer(crInst).intValue());
        setTotalCrAmount(new Double(crAmt).doubleValue());
    }

    private long roundOff(long amt) {
        long amount = amt / 10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }

    public boolean isAccountNoExists(String actNum, boolean isGL) {
        TxTransferTO objTO;
        int size = this.transferTOs.size();
        String checkActNum = "";
        boolean isExists = false;
        for (int i = 0; i < size; i++) {
            objTO = (TxTransferTO) transferTOs.get(i);
            checkActNum = isGL ? objTO.getAcHdId() : objTO.getActNum();
            if (actNum.equals(checkActNum)) {
                isExists = true;
                break;
            }
        }
        objTO = null;
        return isExists;
    }

    public void authorize(String remarks) {
        authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", this.getBatchId());
        authorizeMap.put("REMARKS", remarks);
        //check screen lock
        HashMap authDataMap = new HashMap();
        authDataMap.put("TRANS_ID", this.getBatchId());
        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
        authDataMap.put("TRANS_DT", curDate.clone());
        authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
        StringBuffer open = new StringBuffer();
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap map = (HashMap) lst.get(i);
                open.append("\n" + "User Id  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                open.append("Mode Of Operation  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
            }
            ClientUtil.showMessageWindow("Already opened by" + open);
            return;
        }
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, this.getAuthorizeStatus1());
        double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
        if (penalAmt > 0) {
            authorizeMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penalAmt));
            authorizeMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
        }
        setActNumBlankForGL();
        authorizeMap.put(CommonConstants.AUTHORIZEDATA, this.transferTOs);

        // The following if condition added by Rajesh.
        if (checkForInterBranchTransExistance()) {
            authorizeMap.put("INTER_BRANCH_TRANS", new Boolean(true));
        }

        if (this.operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (!this.isBatchTalliedArrayList()) {
                resourceBundle = new BackDatedTransactionRB();
                COptionPane.showMessageDialog(null, resourceBundle.getString("BATCH_TALLY"));
                this.setResult(ClientConstants.ACTIONTYPE_FAILED);
                return;
            }
        }
        doAction(null);
    }

    private String getAuthorizeStatus1() {
        if (this.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            return CommonConstants.STATUS_AUTHORIZED;
        } else if (this.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            return CommonConstants.STATUS_EXCEPTION;
        } else if (this.getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            return CommonConstants.STATUS_REJECTED;
        }
        return "";
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

    /**
     * Getter for property authorizeMap.
     *
     * @return Value of property authorizeMap.
     *
     */
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     *
     * @param authorizeMap New value of property authorizeMap.
     *
     */
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    /**
     * Getter for property oldAmountMap.
     *
     * @return Value of property oldAmountMap.
     *
     */
    public HashMap getOldAmountMap() {
        return oldAmountMap;
    }

    /**
     * Setter for property oldAmountMap.
     *
     * @param oldAmountMap New value of property oldAmountMap.
     *
     */
    public void setOldAmountMap(HashMap oldAmountMap) {
        this.oldAmountMap = oldAmountMap;
    }

    /**
     * Getter for property mainProductTypeValue.
     *
     * @return Value of property mainProductTypeValue.
     *
     */
    public java.lang.String getMainProductTypeValue() {
        return mainProductTypeValue;
    }

    /**
     * Setter for property mainProductTypeValue.
     *
     * @param mainProductTypeValue New value of property mainProductTypeValue.
     *
     */
    public void setMainProductTypeValue(java.lang.String mainProductTypeValue) {
        this.mainProductTypeValue = mainProductTypeValue;
    }

    /**
     * Getter for property mainProductTypeModel.
     *
     * @return Value of property mainProductTypeModel.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getMainProductTypeModel() {
        return mainProductTypeModel;
    }

    /**
     * Setter for property mainProductTypeModel.
     *
     * @param mainProductTypeModel New value of property mainProductTypeModel.
     *
     */
    public void setMainProductTypeModel(com.see.truetransact.clientutil.ComboBoxModel mainProductTypeModel) {
        this.mainProductTypeModel = mainProductTypeModel;
    }

    public void getAccountHead() {
        HashMap whereMap = new HashMap();
        whereMap.put("ACHDID", getAccountHeadId());
        String mapName = "getSelectAcctHeadDesc";
        List list = ClientUtil.executeQuery(mapName, whereMap);
        if (list != null && list.size() > 0) {
            setAccountHeadDesc((String) ((HashMap) list.get(0)).get("AC_HD_DESC"));
        }
    }

    /**
     * Getter for property transAmt.
     *
     * @return Value of property transAmt.
     */
    public java.lang.String getTransAmt() {
        return transAmt;
    }

    /**
     * Setter for property transAmt.
     *
     * @param transAmt New value of property transAmt.
     */
    public void setTransAmt(java.lang.String transAmt) {
        this.transAmt = transAmt;
    }

    /**
     * Getter for property loanActNo.
     *
     * @return Value of property loanActNo.
     */
    public java.lang.String getLoanActNo() {
        return loanActNo;
    }

    /**
     * Setter for property loanActNo.
     *
     * @param loanActNo New value of property loanActNo.
     */
    public void setLoanActNo(java.lang.String loanActNo) {
        this.loanActNo = loanActNo;
    }

    /**
     * Getter for property transDepositAmt.
     *
     * @return Value of property transDepositAmt.
     */
    public java.lang.String getTransDepositAmt() {
        return transDepositAmt;
    }

    /**
     * Setter for property transDepositAmt.
     *
     * @param transDepositAmt New value of property transDepositAmt.
     */
    public void setTransDepositAmt(java.lang.String transDepositAmt) {
        this.transDepositAmt = transDepositAmt;
    }

    /**
     * Getter for property checkDebitTermLoan.
     *
     * @return Value of property checkDebitTermLoan.
     */
    public boolean isCheckDebitTermLoan() {
        return checkDebitTermLoan;
    }

    /**
     * Setter for property checkDebitTermLoan.
     *
     * @param checkDebitTermLoan New value of property checkDebitTermLoan.
     */
    public void setCheckDebitTermLoan(boolean checkDebitTermLoan) {
        this.checkDebitTermLoan = checkDebitTermLoan;
    }

    /**
     * Getter for property ALL_LOAN_AMOUNT.
     *
     * @return Value of property ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getALL_LOAN_AMOUNT() {
        return ALL_LOAN_AMOUNT;
    }

    /**
     * Setter for property ALL_LOAN_AMOUNT.
     *
     * @param ALL_LOAN_AMOUNT New value of property ALL_LOAN_AMOUNT.
     */
    public void setALL_LOAN_AMOUNT(java.util.HashMap ALL_LOAN_AMOUNT) {
        this.ALL_LOAN_AMOUNT = ALL_LOAN_AMOUNT;
    }

    /**
     * Getter for property depositTransId.
     *
     * @return Value of property depositTransId.
     */
    public java.lang.String getDepositTransId() {
        return depositTransId;
    }

    /**
     * Setter for property depositTransId.
     *
     * @param depositTransId New value of property depositTransId.
     */
    public void setDepositTransId(java.lang.String depositTransId) {
        this.depositTransId = depositTransId;
    }

    /**
     * Getter for property interestAmt.
     *
     * @return Value of property interestAmt.
     */
    public double getInterestAmt() {
        return interestAmt;
    }

    /**
     * Setter for property interestAmt.
     *
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(double interestAmt) {
        this.interestAmt = interestAmt;
    }

    /**
     * Getter for property debitLoanType.
     *
     * @return Value of property debitLoanType.
     */
    public java.util.HashMap getDebitLoanType() {
        return debitLoanType;
    }

    /**
     * Setter for property debitLoanType.
     *
     * @param debitLoanType New value of property debitLoanType.
     */
    public void setDebitLoanType(java.util.HashMap debitLoanType) {
        this.debitLoanType = debitLoanType;
    }

    /**
     * Getter for property depInterestAmt.
     *
     * @return Value of property depInterestAmt.
     */
    public java.lang.String getDepInterestAmt() {
        return depInterestAmt;
    }

    /**
     * Setter for property depInterestAmt.
     *
     * @param depInterestAmt New value of property depInterestAmt.
     */
    public void setDepInterestAmt(java.lang.String depInterestAmt) {
        this.depInterestAmt = depInterestAmt;
    }

    /**
     * Getter for property depLinkBatchId.
     *
     * @return Value of property depLinkBatchId.
     */
    public java.lang.String getDepLinkBatchId() {
        return depLinkBatchId;
    }

    /**
     * Setter for property depLinkBatchId.
     *
     * @param depLinkBatchId New value of property depLinkBatchId.
     */
    public void setDepLinkBatchId(java.lang.String depLinkBatchId) {
        this.depLinkBatchId = depLinkBatchId;
    }

    /**
     * Getter for property depAccNO.
     *
     * @return Value of property depAccNO.
     */
    public java.lang.String getDepAccNO() {
        return depAccNO;
    }

    /**
     * Setter for property depAccNO.
     *
     * @param depAccNO New value of property depAccNO.
     */
    public void setDepAccNO(java.lang.String depAccNO) {
        this.depAccNO = depAccNO;
    }

    /**
     * Getter for property renewDepAmt.
     *
     * @return Value of property renewDepAmt.
     */
    public java.lang.String getRenewDepAmt() {
        return renewDepAmt;
    }

    /**
     * Setter for property renewDepAmt.
     *
     * @param renewDepAmt New value of property renewDepAmt.
     */
    public void setRenewDepAmt(java.lang.String renewDepAmt) {
        this.renewDepAmt = renewDepAmt;
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
     * Getter for property depositPenalAmt.
     *
     * @return Value of property depositPenalAmt.
     */
    public java.lang.String getDepositPenalAmt() {
        return depositPenalAmt;
    }

    /**
     * Setter for property depositPenalAmt.
     *
     * @param depositPenalAmt New value of property depositPenalAmt.
     */
    public void setDepositPenalAmt(java.lang.String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }

    /**
     * Getter for property depositPenalMonth.
     *
     * @return Value of property depositPenalMonth.
     */
    public java.lang.String getDepositPenalMonth() {
        return depositPenalMonth;
    }

    /**
     * Setter for property depositPenalMonth.
     *
     * @param depositPenalMonth New value of property depositPenalMonth.
     */
    public void setDepositPenalMonth(java.lang.String depositPenalMonth) {
        this.depositPenalMonth = depositPenalMonth;
    }

    /**
     * Getter for property penalMap.
     *
     * @return Value of property penalMap.
     */
    public java.util.HashMap getPenalMap() {
        return penalMap;
    }

    /**
     * Setter for property penalMap.
     *
     * @param penalMap New value of property penalMap.
     */
    public void setPenalMap(java.util.HashMap penalMap) {
        this.penalMap = penalMap;
    }

    /**
     * Getter for property authorizeRemarks.
     *
     * @return Value of property authorizeRemarks.
     */
    public java.lang.String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter for property authorizeRemarks.
     *
     * @param authorizeRemarks New value of property authorizeRemarks.
     */
    public void setAuthorizeRemarks(java.lang.String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    /**
     * Getter for property creatingFlexi.
     *
     * @return Value of property creatingFlexi.
     */
    public java.lang.String getCreatingFlexi() {
        return creatingFlexi;
    }

    /**
     * Setter for property creatingFlexi.
     *
     * @param creatingFlexi New value of property creatingFlexi.
     */
    public void setCreatingFlexi(java.lang.String creatingFlexi) {
        this.creatingFlexi = creatingFlexi;
    }

    /**
     * Getter for property flexiAmount.
     *
     * @return Value of property flexiAmount.
     */
    public double getFlexiAmount() {
        return flexiAmount;
    }

    /**
     * Setter for property flexiAmount.
     *
     * @param flexiAmount New value of property flexiAmount.
     */
    public void setFlexiAmount(double flexiAmount) {
        this.flexiAmount = flexiAmount;
    }

    /**
     * Getter for property curDate.
     *
     * @return Value of property curDate.
     */
    public java.util.Date getCurDate() {
        return curDate;
    }

    /**
     * Setter for property curDate.
     *
     * @param curDate New value of property curDate.
     */
    public void setCurDate(java.util.Date curDate) {
        this.curDate = curDate;
    }

    /**
     * Getter for property valueDate.
     *
     * @return Value of property valueDate.
     */
    public java.util.Date getValueDate() {
        return valueDate;
    }

    /**
     * Setter for property valueDate.
     *
     * @param valueDate New value of property valueDate.
     */
    public void setValueDate(java.util.Date valueDate) {
        this.valueDate = valueDate;
    }

    public void checkForValueDate() {
        if (valueDateMap != null) {
            ArrayList txList = new ArrayList();
            txList = (ArrayList) getTransferTOs();
            if (txList != null && txList.size() > 0) {//as an when cusstomer credit loan account moore than one credit is not allowed
                TxTransferTO to = new TxTransferTO();
                HashMap recurringMap = new HashMap();
                int count = 0;
                int debitList = 0;
                boolean val = false;
                Date valDate = null;
                Date trnDate = null;
                for (int i = 0; i < transferTOs.size(); i++) {
                    to = (TxTransferTO) transferTOs.get(i);
                    trnDate = to.getTransDt();
                    valDate = (Date) valueDateMap.get(String.valueOf(i));
                    if (trnDate != null && valDate != null && DateUtil.dateDiff(trnDate, valDate) != 0) {
                        valueDateFlag = true;
                    }
                }
            }
        }
    }

    public String getAcHdID(int rowNum) {
        TxTransferTO obj = (TxTransferTO) transferTOs.get(rowNum);
        String acHdId = obj.getAcHdId();
        return acHdId;
    }

    /**
     * Getter for property reconcile.
     *
     * @return Value of property reconcile.
     */
    public java.lang.String getReconcile() {
        return reconcile;
    }

    /**
     * Setter for property reconcile.
     *
     * @param reconcile New value of property reconcile.
     */
    public void setReconcile(java.lang.String reconcile) {
        this.reconcile = reconcile;
    }

    /**
     * Getter for property balanceType.
     *
     * @return Value of property balanceType.
     */
    public java.lang.String getBalanceType() {
        return balanceType;
    }

    /**
     * Setter for property balanceType.
     *
     * @param balanceType New value of property balanceType.
     */
    public void setBalanceType(java.lang.String balanceType) {
        this.balanceType = balanceType;
    }

    /**
     * Getter for property initBran.
     *
     * @return Value of property initBran.
     */
    public java.lang.String getInitBran() {
        return initBran;
    }

    /**
     * Setter for property initBran.
     *
     * @param initBran New value of property initBran.
     */
    public void setInitBran(java.lang.String initBran) {
        this.initBran = initBran;
    }

    /**
     * Getter for property authBy.
     *
     * @return Value of property authBy.
     */
    public java.lang.String getAuthBy() {
        return authBy;
    }

    /**
     * Setter for property authBy.
     *
     * @param authBy New value of property authBy.
     */
    public void setAuthBy(java.lang.String authBy) {
        this.authBy = authBy;
    }

    /**
     * Getter for property depositPenalFlag.
     *
     * @return Value of property depositPenalFlag.
     */
    public boolean isDepositPenalFlag() {
        return depositPenalFlag;
    }

    /**
     * Setter for property depositPenalFlag.
     *
     * @param depositPenalFlag New value of property depositPenalFlag.
     */
    public void setDepositPenalFlag(boolean depositPenalFlag) {
        this.depositPenalFlag = depositPenalFlag;
    }

    /**
     * Getter for property emiNoInstallment.
     *
     * @return Value of property emiNoInstallment.
     */
    public long getEmiNoInstallment() {
        return emiNoInstallment;
    }

    /**
     * Setter for property emiNoInstallment.
     *
     * @param emiNoInstallment New value of property emiNoInstallment.
     */
    public void setEmiNoInstallment(long emiNoInstallment) {
        this.emiNoInstallment = emiNoInstallment;
    }

    /**
     * Getter for property rdoBulkTransaction_Yes.
     *
     * @return Value of property rdoBulkTransaction_Yes.
     */
    public boolean isRdoBulkTransaction_Yes() {
        return rdoBulkTransaction_Yes;
    }

    /**
     * Setter for property rdoBulkTransaction_Yes.
     *
     * @param rdoBulkTransaction_Yes New value of property
     * rdoBulkTransaction_Yes.
     */
    public void setRdoBulkTransaction_Yes(boolean rdoBulkTransaction_Yes) {
        this.rdoBulkTransaction_Yes = rdoBulkTransaction_Yes;
    }

    /**
     * Getter for property rdoBulkTransaction_No.
     *
     * @return Value of property rdoBulkTransaction_No.
     */
    public boolean isRdoBulkTransaction_No() {
        return rdoBulkTransaction_No;
    }

    /**
     * Setter for property rdoBulkTransaction_No.
     *
     * @param rdoBulkTransaction_No New value of property rdoBulkTransaction_No.
     */
    public void setRdoBulkTransaction_No(boolean rdoBulkTransaction_No) {
        this.rdoBulkTransaction_No = rdoBulkTransaction_No;
    }

    /**
     * Getter for property narration.
     *
     * @return Value of property narration.
     */
    public java.lang.String getNarration() {
        return narration;
    }

    /**
     * Setter for property narration.
     *
     * @param narration New value of property narration.
     */
    public void setNarration(java.lang.String narration) {
        this.narration = narration;
    }

    /**
     * Getter for property penalWaiveOff.
     *
     * @return Value of property penalWaiveOff.
     */
    public boolean isPenalWaiveOff() {
        return penalWaiveOff;
    }

    /**
     * Setter for property penalWaiveOff.
     *
     * @param penalWaiveOff New value of property penalWaiveOff.
     */
    public void setPenalWaiveOff(boolean penalWaiveOff) {
        this.penalWaiveOff = penalWaiveOff;
    }

    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public boolean isRebateInterest() {
        return rebateInterest;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(boolean rebateInterest) {
        this.rebateInterest = rebateInterest;
    }

    /**
     * Getter for property multiple_ALL_LOAN_AMOUNT.
     *
     * @return Value of property multiple_ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getMultiple_ALL_LOAN_AMOUNT() {
        return multiple_ALL_LOAN_AMOUNT;
    }

    /**
     * Setter for property multiple_ALL_LOAN_AMOUNT.
     *
     * @param multiple_ALL_LOAN_AMOUNT New value of property
     * multiple_ALL_LOAN_AMOUNT.
     */
    public void setMultiple_ALL_LOAN_AMOUNT(java.util.HashMap multiple_ALL_LOAN_AMOUNT) {
        this.multiple_ALL_LOAN_AMOUNT = multiple_ALL_LOAN_AMOUNT;
    }

    /**
     * Getter for property closedAccNo.
     *
     * @return Value of property closedAccNo.
     */
    public java.lang.String getClosedAccNo() {
        return closedAccNo;
    }

    /**
     * Setter for property closedAccNo.
     *
     * @param closedAccNo New value of property closedAccNo.
     */
    public void setClosedAccNo(java.lang.String closedAccNo) {
        this.closedAccNo = closedAccNo;
    }

    public boolean isHoAccount() {
        return hoAccount;
    }

    public void setHoAccountStatus(String hoAccountStatus) {
        this.hoAccountStatus = hoAccountStatus;
    }

    public String getHoAccountStatus() {
        return hoAccountStatus;
    }

    public void setHoAccountCr(String hoAccountCr) {
        this.hoAccountCr = hoAccountCr;
    }

    public String getHoAccountCr() {
        return hoAccountCr;
    }

    public void setHoAccountDr(String hoAccountDr) {
        this.hoAccountDr = hoAccountDr;
    }

    public String getHoAccountDr() {
        return hoAccountDr;
    }

    public void setHoAccount(boolean hoAccount) {
        this.hoAccount = hoAccount;
    }

    public ArrayList getOrgRespList() {
        return orgRespList;
    }

    public void setOrgRespList(ArrayList orgRespList) {
        this.orgRespList = orgRespList;
    }

    public boolean isAdviceAccount() {
        return adviceAccount;
    }

    public void setAdviceAccount(boolean adviceAccount) {
        this.adviceAccount = adviceAccount;
    }

    public ArrayList getAdviceList() {
        return adviceList;
    }

    public void setAdviceList(ArrayList adviceList) {
        this.adviceList = adviceList;
    }

    private boolean getDebitAndCreditBranchFlag() {
        boolean flag = false;
        TxTransferTO objTOTD = null;
        int size = this.transferTOs.size();
        String debitBranchId = "";
        String creditBranchId = "";
        String intiatedBranchId = "";
        for (int j = 0; j < size; j++) {
            objTOTD = (TxTransferTO) transferTOs.get(j);
            intiatedBranchId = CommonUtil.convertObjToStr(objTOTD.getInitiatedBranch());
//            if (objTOTD.getTransType().equals("DEBIT") && objTOTD.getProdType().equals("GL")) {
//                debitBranchId = intiatedBranchId;
//            } else 
//                
            if (objTOTD.getTransType().equals("DEBIT")) {
                debitBranchId = CommonUtil.convertObjToStr(objTOTD.getBranchId());
            }
//            if (objTOTD.getTransType().equals("CREDIT") && objTOTD.getProdType().equals("GL")) {
//                creditBranchId = intiatedBranchId;
//            } else 
                
            if (objTOTD.getTransType().equals("CREDIT")) {
                creditBranchId = CommonUtil.convertObjToStr(objTOTD.getBranchId());
            }
        }
        if (null != debitBranchId && null != creditBranchId) {
            if (intiatedBranchId.equals(debitBranchId)) {
                flag = true;
            }
            if (intiatedBranchId.equals(creditBranchId)) {
                flag = true;
            }
        }
        return flag;
    }

    public String getProdCreditTransfer() {
        return prodCreditTransfer;
    }

    public void setProdCreditTransfer(String prodCreditTransfer) {
        this.prodCreditTransfer = prodCreditTransfer;
    }

    public String getProdDebitTransfer() {
        return prodDebitTransfer;
    }

    public void setProdDebitTransfer(String prodDebitTransfer) {
        this.prodDebitTransfer = prodDebitTransfer;
    }
    
}
