/*
 * Copyright 2014 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 *
 * LoanTransactionOB.java
 *
 * Created on January 15, 2019, 4:30 PM
 */
package com.see.truetransact.ui.termloan.loantransaction;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.termloan.loanTransaction.LoanTransactionTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.ui.common.viewall.EditTableUI;
import com.see.truetransact.ui.common.viewall.EditWaiveTableUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Rishad M.P
 */
public class LoanTransactionOB extends CObservable {

    private String cboProductID = "";
    private String txtAccountNumber = "";
    private String txtInterestPayable = "";
    private String txtInterestReceivable = "";
    private String txtPayableBalance = "";
    private ComboBoxModel cbmProductID;
    private final String AUTHORIZE = "AUTHORIZE";
    private String customerName;
    private String availableBalance;
    private int actionType;
    private int result;
    private HashMap linkMap = new HashMap();
    private HashMap calMap = new HashMap();
    private String loanBehaves = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private final static Logger log = Logger.getLogger(LoanTransactionOB.class);
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
//    AccountClosingRB objAccountClosingRB = new AccountClosingRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private HashMap _authorizeMap;
    private String asAnWhen = "";
    private String transProdId = "";
    private List chargelst = null;
    private String customerStreet = "";
    TableDialogUI tableDialogUI = null;
    private double rebateInterest = 0;
    private double rebateEditTransAmt = 0;
    private double waiveEditTransAmt = 0;
    private double waiveEditPenalTransAmt = 0;
    private Date currDt = null;
    private int nxtAccNo ;    
    private boolean rebateInterestflag = false;
    private String rebate_mode="";
    private Date futureDate=null;
    private String txtProdType="";
    private EditTableUI editableUI = null;
    private boolean waiveOffInterest = false;
    private boolean waiveoffPenal = false;
    private String balCrDR = "";
    private ComboBoxModel cbmProdIdCr;
    HashMap lookUpHash;
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
    private EditWaiveTableUI editableWaiveUI = null;
    HashMap returnWaiveMap = null;
    HashMap productRecord = null;
    private HashMap serviceTax_Map = null;
    private String lblServiceTaxval = "";
    private ServiceTaxDetailsTO objservicetaxDetTo;
    private String authorizeBy2 ="";
    private double txtTransAmount=0.0;

    public double getTxtTransAmount() {
        return txtTransAmount;
    }

    public void setTxtTransAmount(double txtTransAmount) {
        this.txtTransAmount = txtTransAmount;
    }
    
        public String getTxtProdType() {
        return txtProdType;
    }

    public void setTxtProdType(String txtProdType) {
        this.txtProdType = txtProdType;
    }
    
    

    public Date getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(Date futureDate) {
        this.futureDate = futureDate;
    }
   
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
    
    // for overdue interest

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
    
    public String getBalCrDR() {
        return balCrDR;
    }

    public void setBalCrDR(String balCrDR) {
        this.balCrDR = balCrDR;
    }

    /**
     * Creates a new instance of LoanTransactionOB
     */
       public LoanTransactionOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LoanTransactionJNDI");
            map.put(CommonConstants.HOME, "loantransaction.LoanTransactionHome");
            map.put(CommonConstants.REMOTE, "loantransaction.loantransaction");
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
            
    public void setCbmProdId(String prodType) {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            try {
                HashMap lookUpHash = new HashMap();
                if (prodType.equals("Advances")) {
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + "AD");
                } else {
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + "TL");
                }
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                HashMap keyValue = ClientUtil.populateLookupData(lookUpHash);

                keyValue = (HashMap) keyValue.get("DATA");
                key = (ArrayList) keyValue.get(CommonConstants.KEY);
                value = (ArrayList) keyValue.get(CommonConstants.VALUE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProductID = new ComboBoxModel(key, value);
        this.cbmProductID = cbmProductID;
        setChanged();
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
        return where;
    }

//    public void filterProd() {
//  
//      HashMap whereMap=new HashMap();
//        whereMap.put("MEMBERNO",CommonUtil.convertObjToStr(getMemNo()));
//         List prodDes = ClientUtil.executeQuery("getLoanProductsSearch", whereMap);
//        if (prodDes.size() <= 0 || prodDes.isEmpty()) {
//            ClientUtil.showAlertWindow("This member does not have a loan");
//            return;
//        }
//        param = new HashMap();
//        key = new ArrayList();
//        value = new ArrayList();
//        param.put("KEY", "");
//        param.put("VALUE", " ");
//        key.add("");
//        value.add("");
//        for (int i = 0; i < prodDes.size(); i++) {
//            param = (HashMap) prodDes.get(i);
//            key.add(param.get("KEY"));
//            value.add(param.get("VALUE"));
//        }
//        cbmProductID = new ComboBoxModel(key, value);
//    }

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
    void setTxtInterestPayable(String txtInterestPayable) {
        this.txtInterestPayable = txtInterestPayable;
        setChanged();
    }

    String getTxtInterestPayable() {
        return this.txtInterestPayable;
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
//    public void getAccountHeadForProduct() {
//
//        /*
//         * based on the selection from the product combo box, one accound head
//         * will be fetched from database and displayed on screen same LookUp
//         * bean will be used for this purpose
//         */
//        param.put(CommonConstants.MAP_NAME, "getAccHead");
//        param.put(CommonConstants.PARAMFORQUERY, getCboProductID());
//        try {
//            final HashMap lookupValues = ClientUtil.populateLookupData(param);
//            fillData((HashMap) lookupValues.get("DATA"));
//            //If proper value is returned, then the size will be more than 1, else do nothing
//            if (value.size() > 1) {
//                setAccountHeadId((String) value.get(1));
//                setAccountHeadDesc((String) key.get(1));
//            }
//            ttNotifyObservers();
//        } catch (Exception e) {
//            parseException.logException(e, true);
//        }
//
//    }
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
            //If actionType such as NEW, EDIT, DELETE is not 0, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                doActionPerform();
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
        try {
            final LoanTransactionTO objLoanTransactionTO = setLoanTransactionData(totalLoanAmount);
            objLoanTransactionTO.setCommand(getCommand());
            objLoanTransactionTO.setStatus(getAction());
            objLoanTransactionTO.setStatusBy(TrueTransactMain.USER_ID);
            objLoanTransactionTO.setStatusDt(currDt);
            double totalWaiveAmount = 0.0;
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
                    objTransactionTO.setScreenName("Loan Future Transaction");
                }
                allowedTransactionDetailsTO.put("1", objTransactionTO);
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            data.put("TransactionTO", transactionDetailsTO);
            data.put("FUTURE_CALC_INT_DT", getFutureDate());
            if (prodType.equals("TermLoan")) {
                data.put("BEHAVE_LIKE", getLoanBehaves());
                if (waiveoffPenal && penalWaiveAmount > 0) {
                    totalWaiveAmount += penalWaiveAmount;
                    totalLoanAmount.put("PENAL_WAIVE_OFF", "Y");
                    totalLoanAmount.put("PENAL_WAIVE_AMT", penalWaiveAmount);
                } else {
                    totalLoanAmount.put("PENAL_WAIVE_OFF", "N");
                }
                if (isInterestWaiveoff() && interestWaiveAmount > 0) {
                    totalWaiveAmount += interestWaiveAmount;
                    totalLoanAmount.put("WAIVE_OFF_INTEREST", "Y");
                    totalLoanAmount.put("INTEREST_WAIVE_AMT", interestWaiveAmount);
                } else {
                    totalLoanAmount.put("INTEREST_WAIVE_OFF", "N");
                }
                if (isNoticeWaiveoff() && noticeWaiveAmount > 0) {
                    totalWaiveAmount += noticeWaiveAmount;
                    totalLoanAmount.put("NOTICE_WAIVE_OFF", "Y");
                    totalLoanAmount.put("NOTICE_WAIVE_AMT", noticeWaiveAmount);
                } else {
                    totalLoanAmount.put("NOTICE_WAIVE_OFF", "N");
                }
                if (isPrincipalwaiveoff() && principalWaiveAmount > 0) {
                    totalWaiveAmount += principalWaiveAmount;
                    totalLoanAmount.put("PRINCIPAL_WAIVE_OFF", "Y");
                    totalLoanAmount.put("PRINCIPAL_WAIVE_AMT", principalWaiveAmount);
                } else {
                    totalLoanAmount.put("PRINCIPAL_WAIVE_OFF", "N");
                }
                if (isArcWaiveOff() && arcWaiveAmount > 0) {
                    totalWaiveAmount += arcWaiveAmount;
                    totalLoanAmount.put("ARC_WAIVE_OFF", "Y");
                    totalLoanAmount.put("ARC_WAIVE_AMT", arcWaiveAmount);
                } else {
                    totalLoanAmount.put("ARC_WAIVE_OFF", "N");
                }
                if (isArbitraryWaiveOff()) {
                    totalWaiveAmount += arbitarayWaivwAmount;
                    totalLoanAmount.put("ARBITRAY_WAIVE_OFF", "Y");
                    totalLoanAmount.put("ARBITRAY_WAIVE_AMT", arbitarayWaivwAmount);
                } else {
                    totalLoanAmount.put("ARBITRAY_WAIVE_OFF", "N");
                }
                if (isDecreeWaiveOff()) {
                    totalLoanAmount.put("DECREE_WAIVE_OFF", "Y");
                    totalLoanAmount.put("DECREE_WAIVE_AMT", decreeWaiveAmount);
                    totalWaiveAmount += decreeWaiveAmount;
                } else {
                    totalLoanAmount.put("DECREE_WAIVE_OFF", "N");
                }
                if (isEpCostWaiveOff()) {
                    totalWaiveAmount += epCostWaiveAmount;
                    totalLoanAmount.put("EP_WAIVE_OFF", "Y");
                    totalLoanAmount.put("EP_WAIVE_AMT", epCostWaiveAmount);
                } else {
                    totalLoanAmount.put("EP_WAIVE_OFF", "N");
                }
                if (isAdvertiseWaiveOff()) {
                    totalWaiveAmount += advertiseWaiveAmount;
                    totalLoanAmount.put("ADVERTISE_WAIVE_OFF", "Y");
                    totalLoanAmount.put("ADVERTISE_WAIVE_AMT", advertiseWaiveAmount);
                } else {
                    totalLoanAmount.put("ADVERTISE_WAIVE_OFF", "N");
                }
                if (isInsuranceWaiveOff()) {
                    totalWaiveAmount += insuranceWaiveAmont;
                    totalLoanAmount.put("INSURENCE_WAIVE_OFF", "Y");
                    totalLoanAmount.put("INSURENCE_WAIVE_AMT", insuranceWaiveAmont);
                } else {
                    totalLoanAmount.put("INSURENCE_WAIVE_OFF", "N");
                }
                if (isLegalWaiveOff()) {
                    totalWaiveAmount += legalWaiveAmount;
                    totalLoanAmount.put("LEGAL_WAIVE_OFF", "Y");
                    totalLoanAmount.put("LEGAL_WAIVE_AMT", legalWaiveAmount);
                } else {
                    totalLoanAmount.put("LEGAL_WAIVE_OFF", "N");
                }
                if (isMiscellaneousWaiveOff()) {
                    totalWaiveAmount += miscellaneousWaiveAmount;
                    totalLoanAmount.put("MISCELLANEOUS_WAIVE_OFF", "Y");
                    totalLoanAmount.put("MISCELLANEOUS_WAIVE_AMT", miscellaneousWaiveAmount);
                } else {
                    totalLoanAmount.put("MISCELLANEOUS_WAIVE_OFF", "N");
                }
                if (isPostageWaiveOff()) {
                    totalWaiveAmount += postageWaiveAmount;
                    totalLoanAmount.put("POSTAGE_WAIVE_OFF", "Y");
                    totalLoanAmount.put("POSTAGE_WAIVE_AMT", postageWaiveAmount);
                } else {
                    totalLoanAmount.put("POSTAGE_WAIVE_OFF", "N");
                }
                if (isRebateInterestflag()) {
                    totalLoanAmount.put("REBATE_MODE", getRebate_mode());
                    if (getRebate_mode().equals("Cash")) {
                        totalLoanAmount.put("REBATE_INTEREST_WAIVE_OFF", "Y");
                    }
                } else {
                    totalLoanAmount.put("REBATE_INTEREST_WAIVE_OFF", "N");
                }
                if (serviceTax_Map != null && serviceTax_Map.size() > 0) {
                    totalLoanAmount.put("TOT_SER_TAX_AMT", serviceTax_Map.get("TOT_TAX_AMT"));
                    totalLoanAmount.put("SER_TAX_HEAD", serviceTax_Map.get("TAX_HEAD_ID"));
                    totalLoanAmount.put("SER_TAX_MAP", serviceTax_Map);
                }
                objLoanTransactionTO.setTotalWaiveAmount(totalWaiveAmount);
                data.put("loantransaction", objLoanTransactionTO);
                data.put("TOTAL_AMOUNT", getTotalLoanAmount());
                data.put("PROD_TYPE", prodType);
                data.put("PROD_ID", getCboProductID());
                data.put("AS_CUSTOMER_COMES", getAsAnWhen());
                data.put("DEBIT_LOAN_TYPE", "DP");
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                    data.put("serviceTaxDetails", getServiceTax_Map());
                    data.put("serviceTaxDetailsTO", setServiceTaxDetails());
                }
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put("MODE", getCommand());
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            setResult(actionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private LoanTransactionTO setLoanTransactionData(HashMap localTotalLoanAmount) throws Exception {
        System.out.println("rish#################"+localTotalLoanAmount);
        final LoanTransactionTO objLoanTransactionTO = new LoanTransactionTO();
        objLoanTransactionTO.setActNum(getTxtAccountNumber());
        objLoanTransactionTO.setIntPayable(CommonUtil.convertObjToDouble(localTotalLoanAmount.get("INTEREST")));
        objLoanTransactionTO.setPenalInt(CommonUtil.convertObjToDouble(localTotalLoanAmount.get("LOAN_CLOSING_PENAL_INT")));       
        if (rebateEditTransAmt > 0) {
            objLoanTransactionTO.setRebateInterest(new Double(rebateEditTransAmt));
        } else {
            objLoanTransactionTO.setRebateInterest(new Double(0));
        }
        objLoanTransactionTO.setProdId(getCboProductID());
        objLoanTransactionTO.setIntCalcUptoDt(getFutureDate());
        objLoanTransactionTO.setProdType(getTxtProdType());
        return objLoanTransactionTO;
    }

    public AccountClosingTO setAccountClosingLTDData() {//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();//last line continution for the purpose of LTD deposits.
        objAccountClosingTO.setActNum(getTxtAccountNumber());
        objAccountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(this.getTxtInterestPayable()));
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
            whereMap.put("MODE", getCommand());
            if (prodType.equals("TermLoan")) {
                whereMap.put("PROD_TYPE", prodType);
            }
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            populateLoanFutureData(data);
            List list = (List) data.get("TransactionTO");
            transactionOB.setDetails(list);
            allowedTransactionDetailsTO = transactionOB.getAllowedTransactionDetailsTO();
            //ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To populate data into the screen
     */
    private void populateLoanFutureData(HashMap data) throws Exception {
        final LoanTransactionTO objLoanTransactionTO = new LoanTransactionTO();
        HashMap accTo = (HashMap) data.get("AccountDetailsTO");
        setStatusBy(CommonUtil.convertObjToStr(accTo.get("STATUS_BY")));
        setAuthorizeStatus(CommonUtil.convertObjToStr(accTo.get("AUTHORIZE_STATUS")));
        setCboProductID(CommonUtil.convertObjToStr(accTo.get("PROD_ID")));
        setCustomerName(CommonUtil.convertObjToStr(accTo.get("CUSTOMER NAME")));
        setCustomerStreet(CommonUtil.convertObjToStr(accTo.get("HOUSE_NAME")));
        setFutureDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(accTo.get("INT_CALC_UPTO_DT"))));
        if (prodType.equals("TermLoan")) {
            setAvailableBalance(CommonUtil.convertObjToStr(showTransactionTLBalance(data)));
            setTxtInterestPayable(CommonUtil.convertObjToStr(accTo.get("INT_PAYABLE")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(accTo.get("ACCT_NUM")));
        } 
        setTxtPayableBalance(CommonUtil.convertObjToStr(accTo.get("PAYABLE_BAL")));
        rebateEditTransAmt = CommonUtil.convertObjToDouble(accTo.get("REBATE_AMT")).doubleValue();
        if (rebateEditTransAmt > 0) {
            setRebateInterest(rebateEditTransAmt);
        }
        showTransactionDetails(data);
        accTo = null;
    }

    private void showTransactionDetails(HashMap map) {
        tableDialogUI = null;
        if (prodType.equals("TermLoan")) {
            map.put("ACCT_NUM", ((HashMap) map.get("AccountDetailsTO")).get("ACT_NUM"));
            HashMap selectMap = new HashMap();
            selectMap.put("LINK_BATCH_ID", ((HashMap) map.get("AccountDetailsTO")).get("ACCT_NUM"));
            selectMap.put("TODAY_DT", currDt.clone());
            selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            tableDialogUI = new TableDialogUI("getAllTransactionViewAD", selectMap, "");
            if (tableDialogUI != null) {
                setTxtTransAmount(CommonUtil.convertObjToDouble(tableDialogUI.getTotalAmtValue()));
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
        }
        return map;
    }
//    protected HashMap getInterestOnMaturityMap() {
//        HashMap maturityMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        log.info("productID--- : " + getLoanBehaves() +""+prodType +"  loanBehaves  "+loanBehaves);
//        whereMap.put("prodId", getCboProductID());
//        List InterestOnMaturityList = ClientUtil.executeQuery("getInterestOnMaturity", whereMap);
//        if (InterestOnMaturityList != null && InterestOnMaturityList.size() > 0) {
//            maturityMap = (HashMap) InterestOnMaturityList.get(0);
//    }
//        return maturityMap;
//    }
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

    public void resetForm() {
        setCboProductID((String) cbmProductID.getElementAt(0));
        setTxtAccountNumber("");
        setCustomerName("");
        setCustomerStreet("");
        setAvailableBalance("");
        setTxtInterestPayable("");
        setTxtPayableBalance("");
        setLoanInt("");
        setFutureDate(null);
        waiveOffInterest = false;
        setBalCrDR("");
        setChanged();
        if (chargelst != null) {
            chargelst.clear();
            chargelst = null;
        }
        setLblServiceTaxval("");
        setServiceTax_Map(null);
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
        setTxtTransAmount(0);
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
                cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
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
        HashMap taxMap; 
        List taxSettingsList = new ArrayList();
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
                        if (chargetype!=null&&chargetype.equals("EP CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype!=null&&chargetype.equals("ARC CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype!=null&&chargetype.equals("MISCELLANEOUS CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype!=null&&chargetype.equals("POSTAGE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("ADVERTISE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype!=null&&chargetype.equals("ARBITRARY CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("LEGAL CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("NOTICE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("INSURANCE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype!=null&&chargetype.equals("EXECUTION DECREE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        checkForTaxMap = checkServiceTaxApplicable(accId);
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
        return taxSettingsList;
     }
     
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
}
