/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReleaseDetailsOB.java
 * 
 * Created on Thu Apr 18 10:51:55 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.termloan.kcctopacs.ReleaseDetailsTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;

/**
 *
 * @author Suresh R
 */
public class LoanRepaymentOB extends CObservable {

    private String txtNCLSanctionNo = "";
    private TransactionOB transactionOB;
    private List finalReleaseList = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private String tdtPaymentDt = "";
    private String tdtRepaymentDt = "";
    private String txtCustID = "";
    private String txtRepaymentAmt = "";
    private String txtKCCAccNo = "";
    private String txtKCCProdID = "";
    private String callingAccNo = "";
    private String rdoReleaseOverDue = "";
    private boolean newData = false;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(LoanRepaymentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int actionType;
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblFinYearWise, tblReleaseDetails, tblAppropriated;
    final ArrayList tblFinYearWiseTitle = new ArrayList();
    final ArrayList tblReleaseDetailsTitle = new ArrayList();
    final ArrayList tblPopulateReleaseTitle = new ArrayList();
    final ArrayList tblAppropriatedTitle = new ArrayList();
    private Date curDate = null;
    ArrayList appropriateList = new ArrayList();

    /**
     * Creates a new instance of TDS ReleaseDetailsOB
     */
    public LoanRepaymentOB() {
        try {
            proxy = ProxyFactory.createProxy();
            curDate = ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LoanRepaymentJNDI");
            map.put(CommonConstants.HOME, "LoanRepaymentHome");
            map.put(CommonConstants.REMOTE, "LoanRepayment");
            setTblFinYearWise();
            tblFinYearWise = new EnhancedTableModel(null, tblFinYearWiseTitle);
            setTblReleaseDetails();
            tblReleaseDetails = new EnhancedTableModel(null, tblReleaseDetailsTitle);
            setTblAppropriateDetails();
            tblAppropriated = new EnhancedTableModel(null, tblAppropriatedTitle);
            setTblReleaseDetailsPopulateTime();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTblFinYearWise() {
        tblFinYearWiseTitle.add("Financial Year");
        tblFinYearWiseTitle.add("Total O/S");
        tblFinYearWiseTitle.add("Overdue Amt");
    }

    public void setTblReleaseDetails() {
        tblReleaseDetailsTitle.add("Select");
        tblReleaseDetailsTitle.add("Release No");
        tblReleaseDetailsTitle.add("Due Date");
        tblReleaseDetailsTitle.add("O/S Balance");
        tblReleaseDetailsTitle.add("Balance Amount");
        tblReleaseDetailsTitle.add("Int Amount");
        tblReleaseDetailsTitle.add("OverDue Int Amt");
        tblReleaseDetailsTitle.add("Penal Int to Pay");
        tblReleaseDetailsTitle.add("Charges to Pay");
        tblReleaseDetailsTitle.add("Released Amt");
    }

    public void setTblAppropriateDetails() {
        tblAppropriatedTitle.add("Total");
        tblAppropriatedTitle.add("O/S Balance");
        tblAppropriatedTitle.add("Balance Amt");
        tblAppropriatedTitle.add("Int Amount");
        tblAppropriatedTitle.add("OverDue Int");
        tblAppropriatedTitle.add("Penal Int to Pay");
        tblAppropriatedTitle.add("Charges to Pay");
    }

    public void setTblReleaseDetailsPopulateTime() {
        tblPopulateReleaseTitle.add("Release No");
        tblPopulateReleaseTitle.add("Due Date");
        tblPopulateReleaseTitle.add("O/S Balance");
        tblPopulateReleaseTitle.add("Balance Amount");
        tblPopulateReleaseTitle.add("Int Amount");
        tblPopulateReleaseTitle.add("OverDue Int Amt");
        tblPopulateReleaseTitle.add("Penal Int to Pay");
        tblPopulateReleaseTitle.add("Charges to Pay");
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#### After DAO Data : " + data);
            HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST") || data.containsKey("CASH_TRANS_LIST")) {
                if (data.containsKey("TRANSFER_TRANS_LIST")) {
                    List transList = (List) data.get("TRANSFER_TRANS_LIST");
                    resultMap.put("TRANSFER_TRANS_LIST", transList);
                } else if (data.containsKey("CASH_TRANS_LIST")) {
                    List cashList = (List) data.get("CASH_TRANS_LIST");
                    resultMap.put("CASH_TRANS_LIST", cashList);
                }
                setProxyReturnMap(resultMap);
            }
            if (data.containsKey("TRANSACTION_LIST")) {
                List list = (List) data.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if (data.containsKey("RELEASE_LIST_DATA")) {
                populateUIData(data);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public void populateUIData(HashMap data) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            List releaseList = (List) data.get("RELEASE_LIST_DATA");
            if (releaseList != null && releaseList.size() > 0) {
                for (int i = 0; i < releaseList.size(); i++) {
                    dataMap = (HashMap) releaseList.get(i);
                    if (i == 0) {
                        setTxtNCLSanctionNo(CommonUtil.convertObjToStr(dataMap.get("NCL_SANCTION_NO")));
                        setTxtRepaymentAmt(CommonUtil.convertObjToStr(dataMap.get("REPAY_AMOUNT")));
                        setTdtRepaymentDt(CommonUtil.convertObjToStr(dataMap.get("REPAY_DATE")));
                    }
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("RELEASE_NO"));
                    rowList.add(dataMap.get("DUE_DATE"));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("CLEAR_BALANCE")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("INT_UPTO_DUE_DT")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("INT_AFTER_DUE_DT")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("PENAL")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("CHARGES")));
                    tableList.add(rowList);
                }
                tblReleaseDetails = new EnhancedTableModel((ArrayList) tableList, tblPopulateReleaseTitle);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateFinYearWiseTable(String nclSanctionNo) {
        HashMap whereMap = new HashMap();
        whereMap.put("NCL_SANCTION_NO", nclSanctionNo);
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getFinYearWiseOutstanding");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap = ClientUtil.executeTableQuery(viewMap);
        ArrayList data = (ArrayList) viewMap.get(CommonConstants.TABLEDATA);
        tblFinYearWise = new EnhancedTableModel(data, tblFinYearWiseTitle);
    }

    public void insertTableData(HashMap whereMap) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            Date dueDate = null;
            Date lastIntCalcDate = null;
            Date releaseDate = null;
            if (getRdoReleaseOverDue().equals("OVER_DUE ONLY")) {
                whereMap.put("DUE_DATE", curDate);
            }
            List releaseList = ClientUtil.executeQuery("getFinancialYearReleaseDetails", whereMap);
            if (releaseList != null && releaseList.size() > 0) {
                for (int i = 0; i < releaseList.size(); i++) {
                    dataMap = (HashMap) releaseList.get(i);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                    //Calculation Start
                    whereMap = new HashMap();
                    HashMap instMap = new HashMap();
                    double paidAmount = 0.0;
                    double custROI = 0.0;
                    double instAmount = 0.0;
                    double paidIntAmount = 0.0;
                    double interestUptoDtAmount = 0.0;
                    double clearBalance = 0.0;
                    long interestDays = 0;
                    whereMap.put("RELEASE_NO", CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                    if (!(dataMap.get("PRINCIPAL_FREQ_TYPE").equals("Lump Sum") || dataMap.get("PRINCIPAL_FREQ_TYPE").equals("On Maturity"))) {   // Required For Only Other than Lump Sum
                        whereMap.put("CURR_DT", curDate);
                    }
                    dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("DUE_DATE")));
                    lastIntCalcDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("LAST_INT_CALC_DT")));
                    releaseDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("RELEASE_DATE")));
                    whereMap.put("FROM_DT", lastIntCalcDate);
                    whereMap.put("TO_DATE", curDate);
                    whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                    List paidAmountList = ClientUtil.executeQuery("getPaidPrinciple", whereMap);
                    List paidPrincipalList = ClientUtil.executeQuery("getNCLPrincipalPaidDetails", whereMap);
                    List InstallmentList = ClientUtil.executeQuery("getInstallmentDetailsUptoCurrentDate", whereMap);

                    //Calculate Due Date
                    if (InstallmentList != null && InstallmentList.size() > 0) {
                        instMap = (HashMap) InstallmentList.get(0);
                        rowList.add(CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
                        dataMap.put("INSTALLMENT_DT", CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));

                        //Multiple Installments Only Required
//                        if (paidPrincipalList != null && paidPrincipalList.size() > 0) {
//                            whereMap = (HashMap) paidPrincipalList.get(0);
//                            paidAmount = CommonUtil.convertObjToDouble(whereMap.get("PAID_PRINCIPAL_AMT"));
//                            for (int j = 0; j < InstallmentList.size(); j++) {
//                                instMap = (HashMap) InstallmentList.get(j);
//                                instAmount += CommonUtil.convertObjToDouble(instMap.get("PRINCIPAL_AMT"));
//                                if (instAmount <= paidAmount) {
//                                    if (j == InstallmentList.size()-1) {
//                                        rowList.add(CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                                        dataMap.put("INSTALLMENT_DT",CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                                    }
//                                } else {
//                                    rowList.add(CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                                    dataMap.put("INSTALLMENT_DT",CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                                    j = InstallmentList.size();
//                                }
//                            }
//                        } else {
//                            instMap = (HashMap) InstallmentList.get(0);
//                            rowList.add(CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                            dataMap.put("INSTALLMENT_DT",CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_DT")));
//                        }
                    } else {
                        ClientUtil.showMessageWindow("Installment Table Should not be empty !!! ");
                        return;
                    }
                    clearBalance = CommonUtil.convertObjToDouble(dataMap.get("CLEAR_BALANCE"));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("CLEAR_BALANCE")));


                    //Calculate Principal to Pay
                    if (InstallmentList != null && InstallmentList.size() > 0) {
                        double paidPrincpalAmt = 0.0;
                        instAmount = 0.0;
                        //Multiple Installments Only Required
                        if (paidPrincipalList != null && paidPrincipalList.size() > 0) {
                            whereMap = (HashMap) paidPrincipalList.get(0);
                            paidPrincpalAmt = CommonUtil.convertObjToDouble(whereMap.get("PAID_PRINCIPAL_AMT"));
                        }
                        for (int j = 0; j < InstallmentList.size(); j++) {
                            instMap = (HashMap) InstallmentList.get(j);
                            instAmount += CommonUtil.convertObjToDouble(instMap.get("PRINCIPAL_AMT"));
                        }
                        instAmount -= paidPrincpalAmt;
                        if (instAmount > 0) {
                            rowList.add(CommonUtil.convertObjToDouble(String.valueOf(instAmount)));
                            dataMap.put("PRINCIPAL_TO_PAY", String.valueOf(instAmount));
                        } else {
                            rowList.add(CommonUtil.convertObjToDouble("0"));
                            dataMap.put("PRINCIPAL_TO_PAY", String.valueOf("0"));
                        }
                    }


                    //Interest Calculating
                    whereMap = new HashMap();
                    HashMap categoryMap = new HashMap();
                    whereMap.put("ACT_NUM", getTxtKCCAccNo());
                    whereMap.put("PROD_ID", getTxtKCCProdID());
                    List loanCategoryList = ClientUtil.executeQuery("getCategoryForIntSubsidy", whereMap);
                    if (loanCategoryList != null && loanCategoryList.size() > 0) {
                        categoryMap = (HashMap) loanCategoryList.get(0);
                        if (DateUtil.dateDiff(curDate, dueDate) > 0) {
                            interestDays = DateUtil.dateDiff(lastIntCalcDate, curDate);
                        } else {
                            interestDays = DateUtil.dateDiff(lastIntCalcDate, dueDate);
                        }
                        System.out.println("############### interestDiffDays : " + interestDays);
                        categoryMap.put("CUR_DT", releaseDate);
                        categoryMap.put("PRODUCT_TYPE", "AD");
                        categoryMap.put("PROD_ID", getTxtKCCProdID());
                        categoryMap.put("CATEGORY_ID", categoryMap.get("CATEGORY"));
                        System.out.println("####### categoryMap : " + categoryMap);
                        List intCategoryList = ClientUtil.executeQuery("getIntSubsidyRateOfInt", categoryMap);
                        if (intCategoryList != null && intCategoryList.size() > 0) {
                            categoryMap = (HashMap) intCategoryList.get(0);
                            custROI = CommonUtil.convertObjToDouble(categoryMap.get("CUST_ROI"));
                            interestUptoDtAmount = (clearBalance * interestDays * custROI) / 36500;
                            Rounding rod = new Rounding();
                            interestUptoDtAmount = (double) rod.getNearest((long) (interestUptoDtAmount * 100), 100) / 100;
                            System.out.println("############### interestUptoDtAmount : " + interestUptoDtAmount);
                            if (paidAmountList != null && paidAmountList.size() > 0) {
                                whereMap = (HashMap) paidAmountList.get(0);
                                paidIntAmount = CommonUtil.convertObjToDouble(whereMap.get("INTEREST"));
                                if (paidIntAmount > 0) {
                                    System.out.println("############### paidIntAmount : " + paidIntAmount);
                                    interestUptoDtAmount -= paidIntAmount;
                                }
                            }
                            if (interestUptoDtAmount < 0) {
                                interestUptoDtAmount = 0;
                            }
                            rowList.add(CommonUtil.convertObjToDouble(String.valueOf(interestUptoDtAmount)));
                            dataMap.put("INT_UP_TO_DATE_AMT", String.valueOf(interestUptoDtAmount));
                            dataMap.put("CALCULATED_INT_UP_TO_DATE_AMT", String.valueOf(interestUptoDtAmount));
                        } else {
                            ClientUtil.showMessageWindow("Interest Rate not set for this Product !!! ");
                            return;
                        }
                    }

                    // After Or Before Due Date Checking
                    if (DateUtil.dateDiff(curDate, dueDate) > 0) {
                        rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                        dataMap.put("INT_AFTER_DUE_DATE_AMT", String.valueOf("0"));
                        dataMap.put("CALCULATED_INT_AFTER_DUE_DATE_AMT", String.valueOf("0"));
                    } else {
                        whereMap = new HashMap();
                        categoryMap = new HashMap();
                        whereMap.put("ACT_NUM", getTxtKCCAccNo());
                        whereMap.put("PROD_ID", getTxtKCCProdID());
                        interestDays = 0;
                        loanCategoryList = ClientUtil.executeQuery("getCategoryForIntSubsidy", whereMap);
                        if (loanCategoryList != null && loanCategoryList.size() > 0) {
                            categoryMap = (HashMap) loanCategoryList.get(0);
                            interestDays = DateUtil.dateDiff(dueDate, curDate);
                            System.out.println("############### After Due Date interestDays : " + interestDays);
                            categoryMap.put("CUR_DT", releaseDate);
                            categoryMap.put("PRODUCT_TYPE", "AD");
                            categoryMap.put("PROD_ID", getTxtKCCProdID());
                            categoryMap.put("CATEGORY_ID", categoryMap.get("CATEGORY"));
                            System.out.println("####### categoryMap : " + categoryMap);
                            List intCategoryList = ClientUtil.executeQuery("getIntSubsidyRateOfInt", categoryMap);
                            if (intCategoryList != null && intCategoryList.size() > 0) {
                                categoryMap = (HashMap) intCategoryList.get(0);
                                custROI = CommonUtil.convertObjToDouble(categoryMap.get("INTEREST"));
                                interestUptoDtAmount = (clearBalance * interestDays * custROI) / 36500;
                                Rounding rod = new Rounding();
                                interestUptoDtAmount = (double) rod.getNearest((long) (interestUptoDtAmount * 100), 100) / 100;
                                System.out.println("############### interestUptoDtAmount : " + interestUptoDtAmount);
                                rowList.add(CommonUtil.convertObjToDouble(String.valueOf(interestUptoDtAmount)));
                                dataMap.put("INT_AFTER_DUE_DATE_AMT", String.valueOf(interestUptoDtAmount));
                                dataMap.put("CALCULATED_INT_AFTER_DUE_DATE_AMT", String.valueOf(interestUptoDtAmount));
                            } else {
                                rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                                dataMap.put("INT_AFTER_DUE_DATE_AMT", String.valueOf("0"));
                                dataMap.put("CALCULATED_INT_AFTER_DUE_DATE_AMT", String.valueOf("0"));
                            }
                        }
                    }
                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                    //Added 16-Sep-2013
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT_RELEASED")));
                    tableList.add(rowList);
                }
                setFinalReleaseList(releaseList);
                tblReleaseDetails = new EnhancedTableModel((ArrayList) tableList, tblReleaseDetailsTitle);
            }else{
                tblReleaseDetails.setDataArrayList(null, tblReleaseDetailsTitle);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setReleaseTableData(ArrayList tableList) {
        tblReleaseDetails = new EnhancedTableModel((ArrayList) tableList, tblReleaseDetailsTitle);
    }

    public void setAppropriateTableData(ArrayList tableList) {
        tblAppropriated = new EnhancedTableModel((ArrayList) tableList, tblAppropriatedTitle);
    }

    /**
     * To perform the appropriate operation
     */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        if (getFinalReleaseList() != null && getFinalReleaseList().size() > 0) {
            data.put("RELEASE_LIST_DATA", getFinalReleaseList());
        }
        if (getAppropriateList() != null && getAppropriateList().size() > 0) {
            data.put("APPROPRIATE_LIST_DATA", getAppropriateList());
        }
        data.put("CUST_ID", getTxtCustID());
        data.put("NCL_SANCTION_NO", getTxtNCLSanctionNo());
        data.put("KCC_ACC_NO", getTxtKCCAccNo());
        data.put("KCC_PROD_ID", getTxtKCCProdID());
        data.put("REPAYMENT_AMOUNT", getTxtRepaymentAmt());
        data.put("LOAN_REPAY_NO", getLoanRepaymentNo());
        data.put("USER_ID", TrueTransactMain.USER_ID);
        if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            data.put("TransactionTO", transactionDetailsTO);
            allowedTransactionDetailsTO = null;
        }
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        data.put("STATUS", getAction());
        data.put("STATUS_BY", TrueTransactMain.USER_ID);
        System.out.println("############# data : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        _authorizeMap = null;
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
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    public void resetForm() {
        setTxtNCLSanctionNo("");
        setTdtPaymentDt("");
        setTxtCustID("");
        setTxtRepaymentAmt("");
        setTxtKCCProdID("");
        setTxtKCCAccNo("");
        setRdoReleaseOverDue("");
        setLoanRepaymentNo("");
        resetTableValues();
        finalReleaseList = null;
        appropriateList = null;
    }

    public void resetTableValues() {
        tblFinYearWise.setDataArrayList(null, tblFinYearWiseTitle);
        tblReleaseDetails.setDataArrayList(null, tblReleaseDetailsTitle);
        tblAppropriated.setDataArrayList(null, tblAppropriatedTitle);
    }
    
    public EnhancedTableModel getTblFinYearWise() {
        return tblFinYearWise;
    }

    public void setTblFinYearWise(EnhancedTableModel tblFinYearWise) {
        this.tblFinYearWise = tblFinYearWise;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public EnhancedTableModel getTblReleaseDetails() {
        return tblReleaseDetails;
    }

    public void setTblReleaseDetails(EnhancedTableModel tblReleaseDetails) {
        this.tblReleaseDetails = tblReleaseDetails;
    }

    public String getTdtPaymentDt() {
        return tdtPaymentDt;
    }

    public void setTdtPaymentDt(String tdtPaymentDt) {
        this.tdtPaymentDt = tdtPaymentDt;
    }

    public String getTxtCustID() {
        return txtCustID;
    }

    public void setTxtCustID(String txtCustID) {
        this.txtCustID = txtCustID;
    }

    public String getTxtNCLSanctionNo() {
        return txtNCLSanctionNo;
    }

    public void setTxtNCLSanctionNo(String txtNCLSanctionNo) {
        this.txtNCLSanctionNo = txtNCLSanctionNo;
    }

    public String getTxtRepaymentAmt() {
        return txtRepaymentAmt;
    }

    public void setTxtRepaymentAmt(String txtRepaymentAmt) {
        this.txtRepaymentAmt = txtRepaymentAmt;
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
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property transactionDetailsTO.
     *
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    /**
     * Setter for property transactionDetailsTO.
     *
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    public List getFinalReleaseList() {
        return finalReleaseList;
    }

    public void setFinalReleaseList(List finalReleaseList) {
        this.finalReleaseList = finalReleaseList;
    }

    public String getTxtKCCAccNo() {
        return txtKCCAccNo;
    }

    public void setTxtKCCAccNo(String txtKCCAccNo) {
        this.txtKCCAccNo = txtKCCAccNo;
    }

    public String getTxtKCCProdID() {
        return txtKCCProdID;
    }

    public void setTxtKCCProdID(String txtKCCProdID) {
        this.txtKCCProdID = txtKCCProdID;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public EnhancedTableModel getTblAppropriated() {
        return tblAppropriated;
    }

    public void setTblAppropriated(EnhancedTableModel tblAppropriated) {
        this.tblAppropriated = tblAppropriated;
    }

    public String getRdoReleaseOverDue() {
        return rdoReleaseOverDue;
    }

    public void setRdoReleaseOverDue(String rdoReleaseOverDue) {
        this.rdoReleaseOverDue = rdoReleaseOverDue;
    }

    public String getCallingAccNo() {
        return callingAccNo;
    }

    public void setCallingAccNo(String callingAccNo) {
        this.callingAccNo = callingAccNo;
    }
    private String loanRepaymentNo = "";

    public String getLoanRepaymentNo() {
        return loanRepaymentNo;
    }

    public void setLoanRepaymentNo(String loanRepaymentNo) {
        this.loanRepaymentNo = loanRepaymentNo;
    }

    public String getTdtRepaymentDt() {
        return tdtRepaymentDt;
    }

    public void setTdtRepaymentDt(String tdtRepaymentDt) {
        this.tdtRepaymentDt = tdtRepaymentDt;
    }

    public ArrayList getAppropriateList() {
        return appropriateList;
    }

    public void setAppropriateList(ArrayList appropriateList) {
        this.appropriateList = appropriateList;
    }
}