/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.payroll.SalaryProcess;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.PayRollTo;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.payroll.employeeSalaryTO;

import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.SQLException;


/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @rishad
 *
 *
 */
public class SalaryProcessDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(SalaryProcessDAO.class);
    private EarnDeduPayTO earnDeduPayTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    // private LinkedHashMap payrollMap = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap returnDataMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    HashMap payrollMap = null;
    String UserId = "";
    HashMap returnMap;
    HashMap tansactionMap = new HashMap();
    private String userId = "";
    String generateSingleTransId="";
    private HashMap returnTransMap=null;
    String salMonthYear = "";
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public SalaryProcessDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }


    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
       
        return returnMap;

    }

    public static void main(String str[]) {
        try {
            SalaryProcessDAO dao = new SalaryProcessDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertPayroll(HashMap map) {
        try {
            HashMap payrollMap1 = new HashMap();
            if (map.containsKey("PayRollMap")) {
                PayRollTo payRollTo = (PayRollTo) map.get("PayRollMap");
                payRollTo.setAuthorizeStatus("N"); 
                payRollTo.setAuthorizeBy(logTO.getUserId());
                payRollTo.setFromDt(setProperDtFormat(payRollTo.getFromDt()));
               sqlMap.executeUpdate("insertPayRollTo", payRollTo);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    private Date setProperDtFormat(Date dt) {   
        Date tempDt = (Date) CurrDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public PayRollTo setPayrollTo(EarnDeduPayTO earnDeduPayTO) {
        PayRollTo payRollTo = new PayRollTo();
        payRollTo.setEmployeeId(earnDeduPayTO.getEmployeeId());
        payRollTo.setMonth(CommonUtil.getProperDate((Date)CurrDt.clone(), earnDeduPayTO.getCreatedDate()));
        payRollTo.setPayType(earnDeduPayTO.getProdType());
        //  payRollTo.setPayType(CommonUtil.convertObjToStr(getPaycodeType(CommonUtil.convertObjToStr(earnDeduPayTO.getPayCode()))));
        payRollTo.setTransDt(earnDeduPayTO.getCreatedDate());
        payRollTo.setPayCode(earnDeduPayTO.getPayCode());
        //  payRollTo.setPayDesc(getPayDescription());
        payRollTo.setAmount(earnDeduPayTO.getAmount());
        if (earnDeduPayTO.getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            payRollTo.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
        } else if (!earnDeduPayTO.getProdType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
            payRollTo.setProdType(earnDeduPayTO.getProdType());
            payRollTo.setProdId(earnDeduPayTO.getProdId());
            payRollTo.setAcct_num(earnDeduPayTO.getAccNo());
            payRollTo.setPricipal(earnDeduPayTO.getPrincipal());
            payRollTo.setPenal(earnDeduPayTO.getPenalInterest());
            payRollTo.setInterest(earnDeduPayTO.getInterest());
        }
        payRollTo.setTransDt(CurrDt);
        payRollTo.setFromDt(earnDeduPayTO.getFromDate());
        payRollTo.setToDt(earnDeduPayTO.getToDate());
        payRollTo.setSrlNo(CommonUtil.convertObjToInt("1"));
        payRollTo.setStatus("temp");
        Date curdt = (Date) CurrDt.clone();
        curdt.setDate(1);
        payRollTo.setCreatedBy(logTO.getUserId());
        payRollTo.setCreatedDt(earnDeduPayTO.getCreatedDate());
        payRollTo.setStatusBy(logTO.getUserId());
       // payRollTo.setMonth(curdt);
        payRollTo.setCalUpto(CurrDt);
        return payRollTo;
    }

    public List getNonGlDeduction(HashMap whereDudMap) {
        List lis;
        lis = ServerUtil.executeQuery("GetNonGLDeduction", whereDudMap);
        return lis;
    }

    private HashMap calLoanStatus(HashMap whereMap) {
        HashMap result = new HashMap();
        try {
            List lis = sqlMap.executeQueryForList("getLoanStatus", whereMap);
            if (lis != null && lis.size() > 0) {
                for (int i = 0; i < lis.size(); i++) {
                    HashMap tempResult = new HashMap();
                    tempResult = (HashMap) lis.get(i);
                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("PENAL INTEREST")) {
                        result.put("PENAL", tempResult.get("BALANCE"));
                    }
                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("INTEREST")) {
                        result.put("INTEREST", tempResult.get("BALANCE"));
                    }
                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("PRINCIPAL")) {
                        result.put("PRINCIPAL", tempResult.get("BALANCE"));
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
   public HashMap interestCalculationTLAD(HashMap whereMap ) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        String accountNo=CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
        String prod_id=CommonUtil.convertObjToStr(whereMap.get("prod_id"));
        String prodType=CommonUtil.convertObjToStr(whereMap.get("prod_type"));
         List facilitylst=null;
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", "AD");
            hash.put("PROD_ID", prod_id);
            hash.put("TRANS_DT", CurrDt.clone());
            hash.put("INITIATED_BRANCH", _branchCode);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                hash.put("TRANS_DT",CurrDt.clone());
                hash.put("INITIATED_BRANCH", _branchCode);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", _branchCode);
                hash.put("BRANCH_CODE", _branchCode);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                hash.put("CURR_DATE", CurrDt.clone());
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
                HashMap condMap = new HashMap();
                if (hash.containsKey("PREMATURE") && hash.containsKey("PREMATURE_INT_CALC_AMT")
                        && CommonUtil.convertObjToStr(hash.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                    condMap.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                } else {
                    condMap.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    condMap.put("FROM_DT", DateUtil.addDays(((Date) condMap.get("FROM_DT")), 2));
                }
                condMap.put("TO_DATE", CurrDt.clone());
                condMap.put("ACT_NUM", accountNo);
                if (!(hash != null && hash.containsKey("INSTALL_TYPE") && hash.get("INSTALL_TYPE") != null && hash.get("INSTALL_TYPE").equals("EMI"))) {
                    facilitylst = ServerUtil.executeQuery("getPaidPrinciple", condMap);
                } else {
                    facilitylst = null;
                }
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap resultMap = (HashMap) facilitylst.get(0);
                    Double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST"));
                    Double penalInterest = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT"));
                    interest -= CommonUtil.convertObjToDouble(resultMap.get("INTEREST")).doubleValue();
                    penalInterest -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    hash.put("INTEREST",interest);
                    hash.put("LOAN_CLOSING_PENAL_INT",penalInterest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    private void salaryProcess(HashMap map) {
        try {
            HashMap payrollmap = new HashMap();
            payrollmap = (HashMap) map.get("salarypost");
            String payrollid = "";
            Date chkDt = CommonUtil.getProperDate((Date) CurrDt.clone(), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(payrollmap.get("salaryMonth"))));
            chkDt.setDate(1);
            HashMap whereChkMap = new HashMap();
            whereChkMap.put("MONTH_YEAR", chkDt);
            List list = sqlMap.executeQueryForList("getExistingPayId", whereChkMap);
            if (list != null && list.size() > 0) {
                HashMap result = (HashMap) list.get(0);
                if (result.containsKey("PAYROLLID")) {
                    payrollid = CommonUtil.convertObjToStr(result.get("PAYROLLID"));
                }
            } else {
                payrollid = getPayrollID();
            }
            HashMap deductionMap;
            PayRollTo payRollTo = new PayRollTo();
            ArrayList payrollList = new ArrayList();
            List lis = getNonGlDeduction(payrollmap);
            if (lis != null && lis.size() > 0) {
                for (int i = 0; i < lis.size(); i++) {
                    try {
                        sqlMap.startTransaction();
                        deductionMap = new HashMap();
                        EarnDeduPayTO earnDeduPayTO = (EarnDeduPayTO) lis.get(i);
                        if (earnDeduPayTO.getPayEarndedu().equals("DEDUCTIONS") && earnDeduPayTO.getProdType() != null && !earnDeduPayTO.getProdType().equals("GL")) {
                            double amount = 0;

                            amount = earnDeduPayTO.getAmount();
                            if (earnDeduPayTO.getAccNo() != null && earnDeduPayTO.getProdId() != null) {
                                String accNo = CommonUtil.convertObjToStr(earnDeduPayTO.getAccNo());
                                String prodid = CommonUtil.convertObjToStr(earnDeduPayTO.getProdId());
                                String prodType = CommonUtil.convertObjToStr(earnDeduPayTO.getProdType());
                                java.util.Date calcUpto = null;
                                calcUpto = CurrDt;
                                double totalLoanBalAmount = 0;
                                if (prodType.equals(TransactionFactory.LOANS)) {
//                                HashMap whereLoanMap = new HashMap();
//                                whereLoanMap.put("ACT_NUM", accNo);
//                                whereLoanMap.put("FROM_DATE", calcUpto);
                                    HashMap whereLoanMap = new HashMap();
                                    whereLoanMap.put("prod_id", prodid);
                                    whereLoanMap.put("ACT_NUM", accNo);
                                    whereLoanMap.put("FROM_DATE", calcUpto);
                                    whereLoanMap.put("prod_type", prodType);
                                    List list1 = sqlMap.executeQueryForList("getPayrollLoanStatus", whereLoanMap);
                                    if (list1 != null && list1.size() > 0) {
                                        HashMap resultMap = (HashMap) list1.get(0);
                                        if (resultMap.get("ACCT_STATUS").equals("CLOSED")) {
                                            earnDeduPayTO.setPrincipal(0.0);
                                            earnDeduPayTO.setInterest(0.0);
                                            earnDeduPayTO.setPenalInterest(0.0);
                                            earnDeduPayTO.setAmount(0.0);

                                        } else {
                                            whereLoanMap.put("CURRDATE", CurrDt.clone());
                                            List creditList = sqlMap.executeQueryForList("getTotalLoanBalance", whereLoanMap);
                                            if (creditList != null && creditList.size() > 0) {
                                                HashMap totBalMap = (HashMap) creditList.get(0);
                                                if (totBalMap.containsKey("TOTAL_LOAN_BALANCE") && totBalMap.get("TOTAL_LOAN_BALANCE") != null) {
                                                    totalLoanBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_LOAN_BALANCE"));
                                                }
                                            }
                                            if (amount >= totalLoanBalAmount) {
                                                amount = totalLoanBalAmount;
                                                earnDeduPayTO.setAmount(amount);
                                            }
                                            HashMap hash = interestCalculationTLAD(whereLoanMap);
                                            double interesPenal = 0;
                                            if (hash != null) {
                                                Double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST"));
                                                Double penalInterest = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT"));
                                                if (amount >= (interest + penalInterest)) {
                                                    interesPenal = interest + penalInterest;
                                                    earnDeduPayTO.setPrincipal(amount - (interest + penalInterest));
                                                    earnDeduPayTO.setInterest(interest);
                                                    earnDeduPayTO.setPenalInterest(penalInterest);
                                                } else {
                                                    earnDeduPayTO.setPrincipal(0.0d);
                                                    earnDeduPayTO.setInterest(interest);
                                                    earnDeduPayTO.setPenalInterest(penalInterest);
                                                    HashMap whereMap = new HashMap();
                                                    whereMap.put("PRINCIPAL", 0.0d);
                                                    whereMap.put("INTEREST", interest);
                                                    whereMap.put("PENALINTEREST", penalInterest);
                                                    HashMap result = getLoanDetails(whereMap, prodid, accNo, amount, prodType);
                                                    earnDeduPayTO.setPrincipal(CommonUtil.convertObjToDouble(result.get("PRINCIPAL")));
                                                    earnDeduPayTO.setInterest(CommonUtil.convertObjToDouble(result.get("INTEREST")));
                                                    earnDeduPayTO.setPenalInterest(CommonUtil.convertObjToDouble(result.get("PENAL")));
                                                }
                                            }
                                        }
                                    }
                                }
                                if (prodType.equals(TransactionFactory.ADVANCES)) {
                                    HashMap whereLoanMap = new HashMap();
                                    whereLoanMap.put("prod_id", prodid);
                                    whereLoanMap.put("ACT_NUM", accNo);
                                    whereLoanMap.put("FROM_DATE", calcUpto);
                                    whereLoanMap.put("prod_type", prodType);

                                    HashMap hash = interestCalculationTLAD(whereLoanMap);
                                    double interesPenal = 0;
                                    List list2 = sqlMap.executeQueryForList("getPayrollLoanStatus", whereLoanMap);
                                    if (list2 != null && list2.size() > 0) {
                                        HashMap resultMap = (HashMap) list2.get(0);
                                        if (resultMap.get("ACCT_STATUS").equals("CLOSED")) {
                                            earnDeduPayTO.setPrincipal(0.0);
                                            earnDeduPayTO.setInterest(0.0);
                                            earnDeduPayTO.setPenalInterest(0.0);
                                            earnDeduPayTO.setAmount(0.0);
                                        } else {
                                            whereLoanMap.put("CURRDATE", CurrDt.clone());
                                            List creditList = sqlMap.executeQueryForList("getTotalLoanBalance", whereLoanMap);
                                            if (creditList != null && creditList.size() > 0) {
                                                HashMap totBalMap = (HashMap) creditList.get(0);
                                                if (totBalMap.containsKey("TOTAL_LOAN_BALANCE") && totBalMap.get("TOTAL_LOAN_BALANCE") != null) {
                                                    totalLoanBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_LOAN_BALANCE"));
                                                }
                                            }
                                            if (amount >= totalLoanBalAmount) {
                                                amount = totalLoanBalAmount;
                                                earnDeduPayTO.setAmount(amount);
                                            }
                                            if (hash != null) {
                                                Double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST"));
                                                Double penelInterest = CommonUtil.convertObjToDouble(hash.get("PENAL"));
                                                if (amount >= (interest + penelInterest)) {
                                                    interesPenal = interest + penelInterest;
                                                    earnDeduPayTO.setPrincipal(amount - (interest + penelInterest));
                                                    earnDeduPayTO.setInterest(interest);
                                                    earnDeduPayTO.setPenalInterest(penelInterest);
                                                } else {
                                                    earnDeduPayTO.setPrincipal(0.0d);
                                                    earnDeduPayTO.setInterest(interest);
                                                    earnDeduPayTO.setPenalInterest(penelInterest);
                                                    HashMap whereMap = new HashMap();
                                                    whereMap.put("PRINCIPAL", 0.0d);
                                                    whereMap.put("INTEREST", interest);
                                                    whereMap.put("PENALINTEREST", penelInterest);
                                                    HashMap result = getLoanDetails(whereMap, prodid, accNo, amount, prodType);
                                                    earnDeduPayTO.setPrincipal(CommonUtil.convertObjToDouble(result.get("PRINCIPAL")));
                                                    earnDeduPayTO.setInterest(CommonUtil.convertObjToDouble(result.get("INTEREST")));
                                                    earnDeduPayTO.setPenalInterest(CommonUtil.convertObjToDouble(result.get("PENAL")));
                                                }
                                            }
                                        }
                                    }
                                }
                                payRollTo = setPayrollTo(earnDeduPayTO);
                                payRollTo.setRemarks("SALARY_PROCESS");
                                payRollTo.setMonth(chkDt);
                                payRollTo.setTransType(CommonConstants.DEBIT);
                                payRollTo.setPayrollId(payrollid);
                                if (payRollTo.getAmount() > 0) {
                                    payrollMap.put("PayRollMap", payRollTo);
                                    insertPayroll(payrollMap);
                                }
                            }
                        } else {

                            payRollTo = setPayrollTo(earnDeduPayTO);
                            if (earnDeduPayTO.getPayEarndedu().equals("DEDUCTIONS")) {
                                payRollTo.setTransType(CommonConstants.DEBIT);
                            } else if (earnDeduPayTO.getPayEarndedu().equals("EARNINGS")) {
                                payRollTo.setTransType(CommonConstants.CREDIT);
                            } else if (earnDeduPayTO.getPayEarndedu().equals("CONTRA")) {
                                payRollTo.setTransType(CommonConstants.DEBIT);
                            }
                            payRollTo.setMonth(chkDt);
                            payRollTo.setPayrollId(payrollid);
                            payRollTo.setRemarks("SALARY_PROCESS");
                            if (payRollTo.getAmount() > 0) {
                                payrollMap.put("PayRollMap", payRollTo);
                                insertPayroll(payrollMap);
                            }
                        }
                        System.out.println("@#@#@ Insert payroll completed...");
                        sqlMap.commitTransaction();
                        System.out.println("@#@#@ Committed...");
                    } catch (Exception e) {
                        e.printStackTrace();;
                        sqlMap.rollbackTransaction();
                        log.error(e);
                        throw new TransRollbackException(e);
                    }
                }
                returnMap.put("PAYROLL_ID", payrollid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap getLoanDetails(HashMap whereMap, String prodid, String actnum, double amt,String prodType) {
        ArrayList alist1 = new ArrayList();
        String scheme_Name = prodType;
        String prod_id = prodid;
        String acNo = actnum;
        double transAmt = amt;
        double principal = 0.0;
        double interest = 0.0;
        double penal = 0.0;
        double charges = 0.0;
        HashMap dataMap1 = new HashMap();
        HashMap returnMap = whereMap;
        HashMap newMap = new HashMap();
        principal = CommonUtil.convertObjToDouble(returnMap.get("PRINCIPAL"));
        interest = CommonUtil.convertObjToDouble(returnMap.get("INTEREST"));
        penal = CommonUtil.convertObjToDouble(returnMap.get("PENALINTEREST"));
        double paidPrincipal = 0;
        double paidInterest = 0;
        double paidPenal = 0;
        double paidCharges = 0;
        try {
            HashMap ALL_LOAN_AMOUNT = new HashMap();
            HashMap hashList = new HashMap();
            hashList.put(CommonConstants.MAP_WHERE, prodid);
            String where = prodid;
            List appList = sqlMap.executeQueryForList("selectAppropriatTransaction", where);
            HashMap appropriateMap = new HashMap();
            if (appList != null && appList.size() > 0) {
                appropriateMap = (HashMap) appList.get(0);
                appropriateMap.remove("PROD_ID");
            } else {
                throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
            }
            java.util.Collection collectedValues = appropriateMap.values();
            java.util.Iterator it = collectedValues.iterator();
            int appTranValue = 0;
            while (it.hasNext()) {
                appTranValue++;
                String hierachyValue = CommonUtil.convertObjToStr(it.next());
                if (hierachyValue.equals("CHARGES")) {
                    if (transAmt > 0 && charges > 0) {

                        if (transAmt >= charges) {
                            transAmt -= charges;
                            paidCharges = charges;
                        } else {
                            paidCharges = transAmt;
                            transAmt -= charges;
                        }
                    }
                }
                if (hierachyValue.equals("PENALINTEREST")) {
                    //penal interest
                    if (transAmt > 0 && penal > 0) {
                        if (transAmt >= penal) {
                            transAmt -= penal;
                            paidPenal = penal;
                        } else {
                            paidPenal = transAmt;
                            transAmt -= penal;
                        }
                    }
                }
                if (hierachyValue.equals("INTEREST")) {
                    if (transAmt > 0 && interest > 0) {
                        if (transAmt >= interest) {
                            transAmt -= interest;
                            paidInterest = interest;
                        } else {
                            paidInterest = transAmt;
                            transAmt -= interest;
                        }
                    }
                }
                if (hierachyValue.equals("PRINCIPAL")) {
                    if (transAmt > 0 && principal > 0) {
                        if (transAmt >= principal) {
                            transAmt -= principal;
                            paidPrincipal = principal;
                        } else {
                            paidPrincipal = transAmt;
                            transAmt -= principal;
                        }
                    }
                }
            }
            ArrayList newList = new ArrayList();
            newList.add(CommonUtil.convertObjToStr(scheme_Name));
            newList.add(CommonUtil.convertObjToStr(acNo));
            newList.add((""));
            newList.add(CommonUtil.convertObjToStr(paidPrincipal));
            newList.add(CommonUtil.convertObjToStr(paidPenal));
            newList.add(CommonUtil.convertObjToStr(paidInterest));
            newList.add(CommonUtil.convertObjToStr(paidCharges));
            Double total1 = ((paidPrincipal + paidPenal + paidInterest + paidCharges));
            returnMap.put("RECOVERY_LIST_TABLE_DATA", newList);
            returnMap.put("TOTAL", total1);
            if (paidCharges > 0) {
                dataMap1.put("CHARGES", String.valueOf(paidCharges));
            } else {
                dataMap1.put("CHARGES", "0");
            }
            dataMap1.put("PRINCIPAL", String.valueOf(paidPrincipal));
            dataMap1.put("INTEREST", String.valueOf(paidInterest));
            dataMap1.put("PENAL", String.valueOf(paidPenal));
            dataMap1.put("PROD_TYPE", scheme_Name);
            dataMap1.put("ACT_NUM", acNo);
            dataMap1.put("RECOVERED_AMOUNT", total1);
            dataMap1.put("BRANCH_CODE", _branchCode);
            dataMap1.put("USER_ID", logTO.getUserId());
            dataMap1.put("PROD_ID", prod_id);
            dataMap1.put("TOTAL", total1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in getLoanDetails: " + e);
        }
        return dataMap1;
    }

    private void doGLTransactions(HashMap map, LogTO objLogTO) throws Exception {
        try {
            double transAmt = CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
            HashMap txMap;
            TransferTrans objTransferTrans = new TransferTrans();
            HashMap pfMap = new HashMap();
            _branchCode = "" + map.get("BRANCH_ID");
            objTransferTrans.setInitiatedBranch(_branchCode);
            txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            TransactionTO objTransactionTO = null;
            objTransactionTO = (TransactionTO) map.get("TransactionTO");
            double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt());
            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                String transType = objTransactionTO.getTransType();
                if (map.get("EMPLOYEE_NAME") != null) {
                    txMap.put(TransferTrans.PARTICULARS, map.get("EMPLOYEE_NAME"));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "");
                }
                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                txMap.put("generateSingleTransId", map.get("generateSingleTransId"));
                txMap.put("GL_TRANS_ACT_NUM", map.get("AC_NO"));
                if (map.get("PAY_DESCRIPTION") != null) {
                    txMap.put(TransferTrans.NARRATION, map.get("PAY_DESCRIPTION"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLGL Transactions");
                }
                if (objTransactionTO.getProductType().equals(TransactionFactory.GL)) {
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                if (debitAmt > 0.0) {
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(map.get("CREDIT_HD_ID")));
                    txMap.put("AUTHORIZEREMARKS", "GL PAYMENT :" + CommonUtil.convertObjToStr(map.get("CREDIT_HD_ID")));
                    txMap.put("generateSingleTransId", map.get("generateSingleTransId"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                }
                doDebitCredit(transferList, _branchCode, true);

            }

            transAmt = 0.0;
            objTransferTrans = null;
            transferList = null;
            txMap = null;

        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
        public void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
         try{
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        data.put(CommonConstants.BREAK_LOAN_HIERARCHY, "N");
          if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
            HashMap returnMap = transferDAO.execute(data, false);
            returnTransMap = new HashMap();
            returnTransMap.put("TRANS_ID", returnMap.get("TRANS_ID"));
         }
         catch(Exception e)
         {
        throw new TransRollbackException(e);
         }
        }
    private void doGLContraTransactions(String payrollid,List payrollContraData) throws SQLException, TransRollbackException {
        //getpayrollContraData
        try {
            HashMap where = new HashMap();
            where.put("payrollid", payrollid);
            if (payrollContraData != null && payrollContraData.size() > 0) {
                for (int i = 0; i < payrollContraData.size(); i++) {
                    HashMap resultMap = (HashMap) payrollContraData.get(i);
                    double transAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
                    HashMap txMap;
                    TransferTrans objTransferTrans = new TransferTrans();
                   // HashMap pfMap = new HashMap();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TransactionTO objTransactionTO = null;
                    TransactionTO transactionTO = new TransactionTO();
                    transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(resultMap.get("ACC_HD")));
                    transactionTO.setProductType(TransactionFactory.GL);
                    transactionTO.setTransType(CommonConstants.TX_TRANSFER);
                    transactionTO.setTransAmt(CommonUtil.convertObjToDouble("" + resultMap.get("AMOUNT")));
                    objTransactionTO = transactionTO;
                    double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt());
                    List contraList=null;
                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                        String transType = objTransactionTO.getTransType();
                        txMap.put(TransferTrans.PARTICULARS, "");
                        txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        // txMap.put("GL_TRANS_ACT_NUM", map.get("AC_NO"));
                        txMap.put(TransferTrans.NARRATION, CommonUtil.convertObjToStr(resultMap.get("PAY_DESCRI")));
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                        String creditchd = getCreditHead(CommonUtil.convertObjToStr(resultMap.get("PAY_CODE")));
                        where.put("pay_code", CommonUtil.convertObjToStr(resultMap.get("PAY_CODE")));
                         contraList = sqlMap.executeQueryForList("getpayrollContraData", where);
                        if (contraList != null && contraList.size() > 0) {
                            for (int k = 0; k < contraList.size(); k++) {
                                PayRollTo payrollTO = (PayRollTo) contraList.get(k);
                                if (payrollTO.getAmount() > 0.0) {
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put(TransferTrans.NARRATION, payrollTO.getPayDesc());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_AC_HD, creditchd);
                                    txMap.put("GL_TRANS_ACT_NUM", payrollTO.getEmployeeId());
                                    txMap.put("AUTHORIZEREMARKS", "GL PAYMENT :" + creditchd);
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    txMap.put(TransferTrans.PARTICULARS, payrollTO.getEmployeeName());
                                    if(payrollTO.getProdType().equals("SA")){ // Added by nithya on 22-05-2021 for KD-2788
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                        txMap.put(TransferTrans.CR_PROD_ID,payrollTO.getProdId()); // Added for KD-3298
                                        txMap.put(TransferTrans.CR_ACT_NUM, payrollTO.getAcct_num());
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                                    }
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, payrollTO.getAmount()));
                                }
                            }
                        }
                    }
                    doDebitCredit(transferList, _branchCode, true);
                    if (resultMap.containsKey("PAY_MODULE_TYPE") && resultMap.get("PAY_MODULE_TYPE") != null && resultMap.get("PAY_MODULE_TYPE").equals("PF")) {
                        if (contraList != null && contraList.size() > 0) {
                            for (int k = 0; k < contraList.size(); k++) {
                                PayRollTo payrollTO = (PayRollTo) contraList.get(k);
                                if (payrollTO.getPayModuleType() != null && payrollTO.getPayModuleType().equals("PF")) {
                                    HashMap pfMap = new HashMap();
                                    HashMap wherePf = new HashMap();
                                    wherePf.put("EMP_ID", payrollTO.getEmployeeId());
                                    List pfDetails = sqlMap.executeQueryForList("getPFNo", wherePf);
                                    if (pfDetails.size() > 0 && pfDetails != null) {
                                        HashMap resultMap1 = (HashMap) pfDetails.get(0);
                                        pfMap.put("PF_NO", resultMap1.get(("PF_ACT_NO")));
                                    }
                                    pfMap.put("TRAN_DT", CurrDt.clone());
                                    if (returnTransMap != null && returnTransMap.get("TRANS_ID") != null && !returnTransMap.get("TRANS_ID").equals("")) {
                                        pfMap.put("TRANS_ID", CommonUtil.convertObjToStr(returnTransMap.get("TRANS_ID")));
                                        pfMap.put("BATCH_ID", CommonUtil.convertObjToStr(returnTransMap.get("TRANS_ID")));
                                        pfMap.put("TRANS_MODE", CommonConstants.TX_TRANSFER);
                                    }
                                    pfMap.put("AMOUNT", payrollTO.getAmount());
                                    pfMap.put("PROD_TYPE", "C");
                                    pfMap.put("CREATED_BY", payrollTO.getCreatedBy());
                                    pfMap.put("CREATED_DATE", CurrDt.clone());
                                    pfMap.put("AUTHORIZED_BY", "");
                                    pfMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                                    pfMap.put("BRANCHID", _branchCode);
                                    pfMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    pfMap.put("REMARK", "");
                                    pfMap.put("PF_TRANS_TYPE", "EPF");
                                    pfMap.put("TRANS_TYPE", "CREDIT");
                                    sqlMap.executeUpdate("InsertPfTransTo", pfMap);
                                    sqlMap.executeUpdate("updatepfmasterbalance", pfMap);
                                }
                            }
                        }
                    }
                    transAmt = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void doLoanPayment(HashMap dataMap) throws Exception {
      try{
        String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tansactionMap = new HashMap();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        double paymentAmt = 0.0;
        double interestAmt = 0.0;
        double penalAmt = 0.0;
        String loanAccNo = "";
        paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT"));
        interestAmt = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE"));
        penalAmt = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
        loanAccNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
        transferTo.setInstrumentNo2("APPL_GL_TRANS");
        if (paymentAmt > 0) {//Debit Insert Start
            HashMap txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, linkBatchId + "-" + ":PAYMENT");
            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("generateSingleTransId",dataMap.get("generateSingleTransId"));
            if (dataMap.get("PAY_DESCRI") != null) {
                txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
            } else {
                txMap.put(TransferTrans.NARRATION, "PAYROLLLOAN Transactions");
            }
            transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(CurrDt);
            transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
            transferTo.setLinkBatchId(loanAccNo);
            transferTo.setInstrumentNo1("PAYROLL");
            TxTransferTO.add(transferTo);
        }                                                       // Debit Acc INSERT End

        HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
        applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
        List lst = sqlMap.executeQueryForList("getAccountHeadProdTL", applicationMap);    // Acc Head
        if (lst != null && lst.size() > 0) {
            applicationMap = (HashMap) lst.get(0);
        }
        HashMap TermLoanCloseCharge = new HashMap();
        TermLoanCloseCharge.put("BRANCH_CODE", CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        List chargeList = sqlMap.executeQueryForList("getChargeDetails", dataMap);
        if (chargeList != null && chargeList.size() > 0) {
            HashMap otherChargesMap = new HashMap();
            for (int j = 0; j < chargeList.size(); j++) {
                HashMap chargeMap = (HashMap) chargeList.get(j);
                double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                } else if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                    TermLoanCloseCharge.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                } else {
                    otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), chargeMap.get("CHARGE_AMT"));
                }
            }
            TermLoanCloseCharge.put("OTHER_CHARGES", otherChargesMap);
        }
        if (interestAmt > 0) {
            TermLoanCloseCharge.put("CURR_MONTH_INT", String.valueOf(interestAmt));
        }
        if (penalAmt > 0) {
            TermLoanCloseCharge.put("PENAL_INT", String.valueOf(penalAmt));
        }

        HashMap txMap = new HashMap();
        if (paymentAmt > 0.0) {
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_ACT_NUM, loanAccNo);
            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(applicationMap.get("AC_HEAD")));
            if (dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.SUSPENSE)) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
            } else if ((dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.ADVANCES))) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
            } else {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
            }
            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            txMap.put(TransferTrans.PARTICULARS, loanAccNo + "-" + ":PAYMENT");
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
            txMap.put(TransferTrans.NARRATION, "PAYROLLLOAN Transactions");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("generateSingleTransId",dataMap.get("generateSingleTransId"));
            transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransDt(CurrDt);
            transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transferTo.setStatusBy(CommonConstants.TTSYSTEM);
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setLinkBatchId(loanAccNo);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(loanAccNo));
            transferTo.setInstrumentNo1("PAYROLL");
            TxTransferTO.add(transferTo);
        }
        TransferDAO transferDAO = new TransferDAO();
        tansactionMap.put("ALL_AMOUNT", TermLoanCloseCharge);
        tansactionMap.put("TxTransferTO", TxTransferTO);
        tansactionMap.put("MODE", dataMap.get("COMMAND"));
        tansactionMap.put("COMMAND", dataMap.get("COMMAND"));
        tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
        tansactionMap.put("LINK_BATCH_ID", loanAccNo);
        // This map should be set if authorization should happen immediately
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        HashMap transMap = transferDAO.execute(tansactionMap, false);
        if(dataMap.containsKey("ACCT_STATUS")&&dataMap.get("ACCT_STATUS")!=null&&dataMap.get("ACCT_STATUS").equals("CLOSED"))
        {
        HashMap hash = new HashMap();
        hash.put("AUTHORIZEDT",CurrDt);
        hash.put("ACCOUNTNO", loanAccNo);
        hash.put("ACCOUNT_STATUS", "CLOSED");
        sqlMap.executeUpdate("authorizeAcctStatusTL", hash);
        }
      }catch(Exception e)
      {
      e.printStackTrace();
      throw new TransRollbackException(e);
      }
    }

    private void doRDPayment(HashMap dataMap) throws Exception {
        try {
            HashMap accountMap = new HashMap();
            // Take "balanceAmt" variable for Penal Amount
            String ACCOUNTNO = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
            if (ACCOUNTNO.lastIndexOf("_") != -1) {
                ACCOUNTNO = ACCOUNTNO;
            } else {
                ACCOUNTNO = ACCOUNTNO + "_1";
            }
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            HashMap txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            txMap.put(TransferTrans.PARTICULARS, "To " + dataMap.get("ACT_NUM") + " ");
            txMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            // In case the Debit Account type is GL
            txMap.put(TransferTrans.DR_AC_HD, dataMap.get("DEBIT_ACC_HD"));  // Give the Debit GL Head here
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("LINK_BATCH_ID", ACCOUNTNO);
            txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
            if (dataMap.get("PAY_DESCRI") != null) {
                txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
            } else {
                txMap.put(TransferTrans.NARRATION, "PAYROLLRD Payment");
            }
            double debitAmt = CommonUtil.convertObjToDouble("" + dataMap.get("DEPOSIT_AMT"));
            double penalAmt = 0.0;
            if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
                penalAmt = CommonUtil.convertObjToDouble("" + dataMap.get("DEPOSIT_PENAL_AMT"));
            }
            debitAmt += penalAmt;
            transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
            //Added by nithya on 13-01-2021 for Kd-3260
            HashMap interBranchCodeMap = new HashMap();
            interBranchCodeMap.put("ACT_NUM", ACCOUNTNO);
            List interBranchCodeList = sqlMap.executeQueryForList("getDepositBranchCode", interBranchCodeMap);//KD-3517
            String acctBranchId = "";
            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                //System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                if(interBranchCodeMap.containsKey("BRANCH_CODE") && interBranchCodeMap.get("BRANCH_CODE") != null){
                  acctBranchId = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                }
            }
            if(!acctBranchId.equals(_branchCode)){
              txMap.put(TransferTrans.CR_BRANCH, acctBranchId);  
            }else{
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            }
            if (!(acctBranchId.equalsIgnoreCase(_branchCode))) {// Added by nithya on 21-03-2019 - KD-3411
                txMap.put("SALARY_PROCESS_RD_INTER_BRANCH_TRANS", "SALARY_PROCESS_RD_INTER_BRANCH_TRANS");
                txMap.put("INITIATED_BRANCH", _branchCode);
            }

            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("ACT_NUM"));
            txMap.put("AUTHORIZEREMARKS", "");  // Any remarks
            txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
            txMap.put("LINK_BATCH_ID", ACCOUNTNO);
            txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
            // double creditAmt = debitAmt;// Give whichever amount wanted to Credit Ex: 5 * 500 (5 months due) + 50 (penal amount)
            transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
            tansactionMap = new HashMap();
            tansactionMap.put("SCREEN", "PAYROLL");
            tansactionMap.put("MODULE", "Transaction");
            tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
            tansactionMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
            tansactionMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            tansactionMap.put("TxTransferTO", transferList);
            if (dataMap.get("DEPOSIT_PENAL_AMT") != null) {
                // At the time of authorization pass the following values
                tansactionMap.put("DEPOSIT_PENAL_AMT", dataMap.get("DEPOSIT_PENAL_AMT"));
                tansactionMap.put("DEPOSIT_PENAL_MONTH", dataMap.get("SL_NO"));
            }
            // This map should be set if authorization should happen immediately
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);

            TransferDAO objTransferDAO = new TransferDAO();
            objTransferDAO.execute(tansactionMap, false);
            objTransferTrans = null;
            objTransferDAO = null;
            tansactionMap.clear();
            tansactionMap = null;
            transferList.clear();
            transferList = null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doOAPayment(HashMap dataMap) throws Exception {
        try {
            String accNo = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            HashMap tansactionMap = new HashMap();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            double paymentAmt = 0.0;
            paymentAmt = CommonUtil.convertObjToDouble(dataMap.get("PAYMENT")).doubleValue();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            if (paymentAmt > 0) {//Debit Insert Start
                HashMap txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                if (dataMap.get("PAY_DESCRI") != null) {
                    txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
                }
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
                txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, paymentAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(CurrDt);
                transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
                transferTo.setLinkBatchId(accNo);
                transferTo.setInstrumentNo1("PAYROLL");
                TxTransferTO.add(transferTo);
            } 
            else
            {   //when net salary going to negative
                 HashMap txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("DEBIT_ACC_NO")));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
               // txMap.put(TransferTrans.DRI, "PAYROLL");
                if (dataMap.get("PAY_DESCRI") != null) {
                    txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
                }
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
                txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(paymentAmt));
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(CurrDt);
                transferTo.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(dataMap.get("USER_ID"))));
                transferTo.setLinkBatchId(accNo);
                transferTo.setInstrumentNo1("PAYROLL");
                TxTransferTO.add(transferTo);
            
            
            }
            // Debit Acc INSERT End
            HashMap applicationMap = new HashMap();                // Crdit Acc INSERT Start
            applicationMap.put("PROD_ID", dataMap.get("PROD_ID"));
            HashMap txMap = new HashMap();
            if (paymentAmt > 0.0) {
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_ACT_NUM, accNo);
                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                if (dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.OPERATIVE)) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                } else if (dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.ADVANCES)) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                }
                txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                //txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
                txMap.put(TransferTrans.PARTICULARS, "Salary for the month " + salMonthYear);
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                if (dataMap.get("PAY_DESCRI") != null) {
                    txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
                }
                txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
                txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, paymentAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(CurrDt);
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setLinkBatchId(accNo);
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(accNo));
                transferTo.setInstrumentNo1("PAYROLL");
                TxTransferTO.add(transferTo);
            }
            else
            {
              //when net salary going to negative
              transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_ACT_NUM, accNo);
                txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                if (dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.OPERATIVE)) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                } else if (dataMap.get("PRODUCT_TYPE").equals(TransactionFactory.ADVANCES)) {
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                }
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                 txMap.put(TransferTrans.PARTICULARS, accNo + "-" + ":PAYMENT");
                txMap.put(TransferTrans.DR_INSTRUMENT_1, "PAYROLL");
                if (dataMap.get("PAY_DESCRI") != null) {
                    txMap.put(TransferTrans.NARRATION, dataMap.get("PAY_DESCRI"));
                } else {
                    txMap.put(TransferTrans.NARRATION, "PAYROLLOA Payment");
                }
                txMap.put("GL_TRANS_ACT_NUM", dataMap.get("AC_NO"));
                txMap.put("generateSingleTransId", dataMap.get("generateSingleTransId"));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(paymentAmt));
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(CurrDt);
                transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                transferTo.setStatusBy(CommonConstants.TTSYSTEM);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setLinkBatchId(accNo);
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(accNo));
                transferTo.setInstrumentNo1("PAYROLL");
                TxTransferTO.add(transferTo);
            }
            TransferDAO transferDAO = new TransferDAO();
            tansactionMap.put("TxTransferTO", TxTransferTO);
            tansactionMap.put("MODE", dataMap.get("COMMAND"));
            tansactionMap.put("COMMAND", dataMap.get("COMMAND"));
            tansactionMap.put(CommonConstants.BRANCH_ID, _branchCode);
            tansactionMap.put("LINK_BATCH_ID", accNo);
            // This map should be set if authorization should happen immediately
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            tansactionMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            HashMap transMap = transferDAO.execute(tansactionMap, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doGLDebit(HashMap map) throws Exception {
        try {
            TransactionTO objTransactionTO = null;
            objTransactionTO = (TransactionTO) map.get("TransactionTO");
            double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
            ArrayList cashList = new ArrayList();
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(TransactionFactory.GL));
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setParticulars("PAYROLL PAYMNET VOUCHER");
            objCashTO.setInpAmount(debitAmt);
            objCashTO.setAmount(debitAmt);
            objCashTO.setInitTransId("" + map.get(CommonConstants.USER_ID));
            objCashTO.setBranchId(_branchCode);
            objCashTO.setStatusBy("" + map.get(CommonConstants.USER_ID));
            userId = "" + map.get(CommonConstants.USER_ID);
            objCashTO.setStatusDt(CurrDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setAuthorizeRemarks("PAYROLL PAYMENT VOUCHER");
            objCashTO.setInitiatedBranch(_branchCode);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand( CommonConstants.TOSTATUS_INSERT);
            objCashTO.setInstrumentNo1("PAYROLL");
            objCashTO.setInstrumentNo2("PAYMENT_VOUCHER");
            if (map.get("PAY_DESCRIPTION") != null) {
                objCashTO.setNarration(CommonUtil.convertObjToStr(map.get("PAY_DESCRIPTION")));
            } else {
                objCashTO.setNarration("PAYROLLGL Transactions");
            }
            objCashTO.setInstType("WITHDRAW_SLIP");
            objCashTO.setInstDt(CurrDt);
            objCashTO.setLinkBatchId("");
            cashList.add(objCashTO);
            if (cashList != null && cashList.size() > 0) {
                doCashTransfer(cashList, _branchCode, false);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            if (e instanceof TTException) {
            } else {
                e.printStackTrace();
            }
            throw e;
            //  throw new TransRollbackException(e);
        }
    }

    private void doCashTransfer(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        data.put("SCREEN_NAME", "PAYROLL");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
            //  data.put(CommonConstants.AUTHORIZEDATA, batchList);
        }
        HashMap cashTransMap = cashDAO.execute(data, true);
        HashMap map = new HashMap();
        String status = CommonConstants.STATUS_AUTHORIZED;
        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
        ArrayList arrList = new ArrayList();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", status);
        singleAuthorizeMap.put("TRANS_ID", cashTransMap.get("TRANS_ID"));
        singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
        arrList.add(singleAuthorizeMap);
        map = new HashMap();
        map.put("SCREEN", "Cash");
        map.put("USER_ID", logTO.getUserId());
        map.put("SELECTED_BRANCH_ID", branchCode);
        map.put("BRANCH_CODE", branchCode);
        map.put("MODULE", "Transaction");
        map.put("SCREEN_NAME", "PAYROLL");
        HashMap dataMap = new HashMap();
        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
        dataMap.put("DAILY", "DAILY");
        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
        cashTransactionDAO.execute(map, false);

    }
    private HashMap NonGL(PayRollTo payrollto, String suspenceHdID) throws SQLException {
        HashMap nonGLentry = new HashMap();

        if (payrollto.getProdType().equals(TransactionFactory.LOANS) || payrollto.getProdType().equals(TransactionFactory.ADVANCES) || payrollto.getProdType().equals(TransactionFactory.SUSPENSE)) {
            nonGLentry.put("AC_NO", payrollto.getEmployeeId());
            nonGLentry.put("ACT_NUM", payrollto.getAcct_num());
            nonGLentry.put("BRANCH_ID", _branchCode);
            HashMap whereLoanMap = new HashMap();
            double totalLoanBalAmount = 0;
            whereLoanMap.put("ACT_NUM", payrollto.getAcct_num());
            whereLoanMap.put("CURRDATE", CurrDt.clone());
            List creditList = sqlMap.executeQueryForList("getTotalLoanBalance", whereLoanMap);
            if (creditList != null && creditList.size() > 0) {
                HashMap totBalMap = (HashMap) creditList.get(0);
                if (totBalMap.containsKey("TOTAL_LOAN_BALANCE") && totBalMap.get("TOTAL_LOAN_BALANCE") != null) {
                    totalLoanBalAmount = CommonUtil.convertObjToDouble(totBalMap.get("TOTAL_LOAN_BALANCE"));
                }
            }
            if(payrollto.getAmount()>=totalLoanBalAmount)
            {
             nonGLentry.put("ACCT_STATUS","CLOSED");
            }
            nonGLentry.put("PAYMENT", payrollto.getAmount());
            nonGLentry.put("INT_DUE", payrollto.getInterest());
            nonGLentry.put("PENAL", payrollto.getPenal());
            nonGLentry.put("PRODUCT_TYPE", payrollto.getProdType());
            nonGLentry.put("PROD_ID", payrollto.getProdId());
            nonGLentry.put("DEBIT_ACC_NO", suspenceHdID);
            nonGLentry.put("AC_HEAD", "");
            nonGLentry.put("BRANCH_CODE", _branchCode);
            nonGLentry.put("USER_ID", logTO.getUserId());
            nonGLentry.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            nonGLentry.put("PAY_DESCRI", payrollto.getPayDesc());

        } else if (payrollto.getProdType().equals(TransactionFactory.DEPOSITS)) {
            String accnum = payrollto.getAcct_num();
            nonGLentry.put("ACT_NUM", "" + accnum.split("_")[0]);
            nonGLentry.put("AC_NO", payrollto.getEmployeeId());
            nonGLentry.put("ACT_NUM", payrollto.getAcct_num());
            nonGLentry.put("BRANCH_ID", _branchCode);
            nonGLentry.put("PRODUCT_TYPE", payrollto.getProdType());
            nonGLentry.put("DEBIT_ACC_HD", suspenceHdID);
            nonGLentry.put("PROD_ID", payrollto.getProdId());
            nonGLentry.put("BRANCH_CODE", _branchCode);
            nonGLentry.put("USER_ID", logTO.getUserId());
            nonGLentry.put("PAY_DESCRI", payrollto.getPayDesc());
            nonGLentry.put("DEPOSIT_AMT", payrollto.getAmount());
        } else if (payrollto.getProdType().equals(TransactionFactory.OPERATIVE)) {
            nonGLentry.put("AC_NO", payrollto.getEmployeeId());
            nonGLentry.put("ACT_NUM", payrollto.getAcct_num());
            nonGLentry.put("BRANCH_ID", _branchCode);
            nonGLentry.put("PAYMENT", payrollto.getAmount());
            nonGLentry.put("PRODUCT_TYPE", payrollto.getProdType());
            nonGLentry.put("PROD_ID", payrollto.getProdId());
            nonGLentry.put("DEBIT_ACC_NO", suspenceHdID);
            nonGLentry.put("AC_HEAD", "");
            nonGLentry.put("BRANCH_CODE", _branchCode);
            nonGLentry.put("USER_ID", logTO.getUserId());
            nonGLentry.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            nonGLentry.put("PAY_DESCRI", payrollto.getPayDesc());
            nonGLentry.put("CREDIT_HD_ID", payrollto.getAccHd());
        }
        return nonGLentry;
    }
    private void payrollProcess(HashMap map) throws Exception {
        try {
            if (map.get("PRODUCT_TYPE").equals(TransactionFactory.GL)) {
                LogTO objLogTO = new LogTO();
                doGLTransactions(map, objLogTO);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.LOANS) ||map.get("PRODUCT_TYPE").equals(TransactionFactory.ADVANCES) || map.get("PRODUCT_TYPE").equals(TransactionFactory.SUSPENSE)) {
                doLoanPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.DEPOSITS)) {
                doRDPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.OPERATIVE)) {
                doOAPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals("GL_VOUCHER")) {
                doGLDebit(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    private void payrollSalrySuspense(HashMap map) throws Exception {
        try {
            if (map.get("PRODUCT_TYPE").equals(TransactionFactory.GL)) {
                LogTO objLogTO = new LogTO();
                doGLTransactions(map, objLogTO);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.LOANS) || map.get("PRODUCT_TYPE").equals(TransactionFactory.SUSPENSE)) {
                doLoanPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.DEPOSITS)) {
                doRDPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals(TransactionFactory.OPERATIVE) || map.get("PRODUCT_TYPE").equals(TransactionFactory.ADVANCES)) {
                doOAPayment(map);
            } else if (map.get("PRODUCT_TYPE").equals("GL_VOUCHER")) {
                doGLDebit(map);
            }
        } catch (Exception e) {
              e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
      public HashMap getDataMap(HashMap map) {
        HashMap dataMap = new HashMap();
        TransactionTO transactionTO = new TransactionTO();
        transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(map.get("DEBIT_HD_ID")));
        transactionTO.setProductType(CommonUtil.convertObjToStr(map.get("PRODUCT_TYPE")));
        transactionTO.setTransType(CommonConstants.TX_TRANSFER);
        transactionTO.setTransAmt(Double.parseDouble("" + map.get("AMOUNT")));
        dataMap.put("PRODUCT_TYPE", map.get("PRODUCT_TYPE"));
        dataMap.put("BRANCH_ID",_branchCode);
        dataMap.put("TransactionTO", transactionTO);
        dataMap.put("CREDIT_HD_ID", "" + map.get("CREDIT_HD_ID"));
        dataMap.put("CONTRA", "" + map.get("CONTRA"));
        dataMap.put("USER_ID",logTO.getUserId());
        dataMap.put("generateSingleTransId",map.get("generateSingleTransId"));
        if (map.get("EMPLOYEE_NAME") != null) {
            dataMap.put("EMPLOYEE_NAME", "" + map.get("EMPLOYEE_NAME"));
        }
        if (map.get("PAY_DESCRIPTION") != null) {
            dataMap.put("PAY_DESCRIPTION", "" + map.get("PAY_DESCRIPTION"));
        }
        dataMap.put("AC_NO", map.get("EMPLOYEEID"));
        if (map.get("AC_NUM") != null) {
            dataMap.put("ACT_NUM", map.get("AC_NUM"));
        }
        if (map.get("PRO_ID") != null) {
            dataMap.put("PRO", map.get("PRO_ID"));
        }
        return dataMap;
    }
    private void payroll(String payrolID) throws Exception {
        System.out.println("##$@@ inside payroll..."+payrolID);
        String suspenceHd_ID = getSuspenceAchdID();
        try {
            sqlMap.startTransaction();
            System.out.println("##$@@ inside payroll sqlMap.startTransaction executed...");
            String payrollid = "";
            HashMap wherePayroll = new HashMap();
            wherePayroll.put("payrollid", payrolID);
            List payrollEntries = sqlMap.executeQueryForList("getpayrollData", wherePayroll);
            for (int i = 0; i < payrollEntries.size(); i++) {
                PayRollTo payrollTO = (PayRollTo) payrollEntries.get(i);
                payrollid = payrollTO.getPayrollId();
                String paydescri = payrollTO.getPayDesc();
                if (payrollTO.getPayEarndedu().equals("EARNINGS")) {
                    HashMap glMap = new HashMap();
                    glMap.put("DEBIT_HD_ID", payrollTO.getAccHd());
                    glMap.put("PRODUCT_TYPE", TransactionFactory.GL);
                    glMap.put("CREDIT_HD_ID", suspenceHd_ID);
                    glMap.put("AMOUNT", payrollTO.getAmount());
                    glMap.put("EMPLOYEE_NAME", payrollTO.getEmployeeName());
                    glMap.put("PAY_DESCRIPTION", payrollTO.getPayDesc());
                    glMap.put("PAY_MODULE_TYPE", payrollTO.getPayModuleType());
                    glMap.put("EMPLOYEEID", payrollTO.getEmployeeId());
                    glMap.put("generateSingleTransId",generateSingleTransId);
                    payrollProcess(getDataMap(glMap));
                } else if (payrollTO.getPayEarndedu().equals("DEDUCTIONS")
                        && CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.GL)) {
                    HashMap glMap = new HashMap();
                    glMap.put("DEBIT_HD_ID", suspenceHd_ID);
                    glMap.put("PRODUCT_TYPE", TransactionFactory.GL);
                    glMap.put("CREDIT_HD_ID", payrollTO.getAccHd());
                    glMap.put("AMOUNT", payrollTO.getAmount());
                    glMap.put("EMPLOYEE_NAME", payrollTO.getEmployeeName());
                    glMap.put("PAY_DESCRIPTION", payrollTO.getPayDesc());
                    glMap.put("EMPLOYEEID", payrollTO.getEmployeeId());
                    glMap.put("CONTRA", "N");
                    glMap.put("PF_TRANS_TYPE", "EPF");
                    glMap.put("PAY_MODULE_TYPE", payrollTO.getPayModuleType());
                    glMap.put("generateSingleTransId",generateSingleTransId);
                    glMap.put("PROD_ID", payrollTO.getProdId());
                    payrollProcess(getDataMap(glMap));
                    if (payrollTO.getPayModuleType() != null && payrollTO.getPayModuleType().equals("PF")) {
                        HashMap pfMap = new HashMap();
                        HashMap wherePf = new HashMap();
                        wherePf.put("EMP_ID", payrollTO.getEmployeeId());
                        List pfDetails = sqlMap.executeQueryForList("getPFNo", wherePf);
                        if (pfDetails.size() > 0 && pfDetails != null) {
                            HashMap resultMap = (HashMap) pfDetails.get(0);
                            pfMap.put("PF_NO", resultMap.get(("PF_ACT_NO")));
                        }
                        pfMap.put("TRAN_DT", CurrDt.clone());
                        if (returnTransMap != null && returnTransMap.get("TRANS_ID") != null && !returnTransMap.get("TRANS_ID").equals("")) {
                            pfMap.put("TRANS_ID", CommonUtil.convertObjToStr(returnTransMap.get("TRANS_ID")));
                            pfMap.put("BATCH_ID", CommonUtil.convertObjToStr(returnTransMap.get("TRANS_ID")));
                            pfMap.put("TRANS_MODE", CommonConstants.TX_TRANSFER);
                        }
                        pfMap.put("AMOUNT", payrollTO.getAmount());
                        pfMap.put("PROD_TYPE", "C");
                        pfMap.put("CREATED_BY", payrollTO.getCreatedBy());
                        pfMap.put("CREATED_DATE", CurrDt.clone());
                        pfMap.put("AUTHORIZED_BY", "");
                        pfMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                        pfMap.put("BRANCHID", _branchCode);
                        pfMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        pfMap.put("REMARK", "");
                        pfMap.put("PF_TRANS_TYPE", "PF");
                        pfMap.put("TRANS_TYPE", "CREDIT");
                        sqlMap.executeUpdate("InsertPfTransTo", pfMap);
                        sqlMap.executeUpdate("updateprnbalcashforPayRollIndividual", pfMap);
                    }

    
                } else if (payrollTO.getPayEarndedu().equals("DEDUCTIONS") && CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.LOANS) || CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.ADVANCES)
                        || CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.SUSPENSE) || CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.OPERATIVE) || CommonUtil.convertObjToStr(payrollTO.getProdType()).equals(TransactionFactory.DEPOSITS)) {
                    HashMap map = NonGL(payrollTO, suspenceHd_ID);
                    map.put("generateSingleTransId",generateSingleTransId);
                    payrollProcess(map);
                }
            }
            //for contra transaction
             HashMap where = new HashMap();
             where.put("payrollid", payrollid);
             List payrollContraData = sqlMap.executeQueryForList("getpayrollConsolidateContraData", where);
             if(payrollContraData!=null&&payrollContraData.size()>0){
             doGLContraTransactions(payrolID,payrollContraData);
               }
           if(payrollEntries!=null&&payrollEntries.size()>0){
            List salEntries = sqlMap.executeQueryForList("getSalaryData", where);
            for (int i = 0; i < salEntries.size(); i++) {
                employeeSalaryTO salaryTO = (employeeSalaryTO) salEntries.get(i);
                HashMap map = netSalary(salaryTO, suspenceHd_ID);
                 map.put("generateSingleTransId",generateSingleTransId);
                payrollSalrySuspense(map);
            }}
            HashMap whereMap = new HashMap();
            whereMap.put("generateSingleTransId",generateSingleTransId);
            whereMap.put("PAYROLLID", payrollid);
            sqlMap.executeUpdate("updatePayrollData", whereMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
             e.printStackTrace();
             sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }
    //for getting credit achead of contra
    private String getCreditHead(String paycode) throws SQLException  {
        String achd = "";
        String where = paycode;
        List lis = sqlMap.executeQueryForList("getCreditHead", where);
        if (lis.size() > 0 && lis != null) {
            HashMap result = new HashMap();
            result = (HashMap) lis.get(0);
            achd = CommonUtil.convertObjToStr(result.get("ACC_HD"));
        }
        return achd;
    }
    private HashMap netSalary(employeeSalaryTO salaryTO, String suspenceHd_ID) {
        HashMap salaryMap = new HashMap();
        salaryMap.put("ACT_NUM", salaryTO.getNetSalaryAccNo());
        salaryMap.put("BRANCH_ID", _branchCode);
        salaryMap.put("PAYMENT", salaryTO.getNetSalary());
        salaryMap.put("PRODUCT_TYPE", salaryTO.getSalProdType());
        salaryMap.put("DEBIT_ACC_NO", suspenceHd_ID);
        salaryMap.put("PROD_ID", salaryTO.getSalProdId());
        salaryMap.put("BRANCH_CODE", _branchCode);
        salaryMap.put("USER_ID", logTO.getUserId());
        salaryMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        salaryMap.put("AC_NO", salaryTO.getEmployeeId());
        String s = "NET SALARY : ";
        s = s + salaryTO.getEmployeeName();
        salaryMap.put("PAY_DESCRI", s);
        return salaryMap;

    }
    public String getSuspenceAchdID() {
        String suspenseHd = "";
        try {
            List lis = sqlMap.executeQueryForList("GET_SUSPENSE_HD", null);
            if (lis != null && lis.size() > 0) {
                HashMap resultMap = new HashMap();
                resultMap = (HashMap) lis.get(0);
                if(resultMap.containsKey("SALARY_SUSPENSE")&&resultMap.get("SALARY_SUSPENSE")!=null){
                suspenseHd = CommonUtil.convertObjToStr(resultMap.get("SALARY_SUSPENSE"));
                return suspenseHd;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suspenseHd;
    }
    public HashMap execute(HashMap map) throws Exception {
        try {
            returnMap = new HashMap();
            payrollMap = new HashMap();
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            CurrDt = ServerUtil.getCurrentDate(_branchCode);
            HashMap returnMap = new HashMap();
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            returnDataMap = new HashMap();
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (map.containsKey("MODE") && map.get("MODE").equals("POST")) {
                salaryProcess(map);
            } 
            if (map.containsKey("MODE") && map.get("MODE").equals("PROCESS") && map.containsKey("payrollId")) {
                generateSingleTransId = generateLinkID();
                if (map.get("payrollId") != null) {
                    String payrollid = CommonUtil.convertObjToStr(map.get("payrollId"));
                    if(map.containsKey("SAL_MONTH_YEAR") && map.get("SAL_MONTH_YEAR") != null){
                        salMonthYear = CommonUtil.convertObjToStr(map.get("SAL_MONTH_YEAR"));
                    }
                    payroll(payrollid);
                }
            }
         
        } catch (Exception e) {
            //sqlMap.rollbackTransaction();
            log.error(e);
            throw (e);
            //throw new TransRollbackException(e);
        }
        map = null;
        destroyObjects();
        return returnMap;
    }
   private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", CurrDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsInvestment", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsInvestment", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private String getPayrollID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PAYROLL_ENTRY_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
//        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        earnDeduPayTO = null;
        payrollMap = null;
        salMonthYear = "";
    }
    
     private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        dao = null;
        return batchID;
    }

}