/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RepaymentCalculator.java
 *
 * Created on March 18, 2005, 5:29 PM
 */
package com.see.truetransact.serverside.transaction.common.product.loan;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.interestcalc.LoanCalculateInterest;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;

/**
 *
 * @author 152713
 */
public class RepaymentCalculator {

    private HashMap dataMap;
    private SqlMap sqlMap = null;
    private List interestList = null;
    private String _branchCode = null;
    private final String ALL_RECORDS = "ALL_RECORDS";
    private final String INTEREST = "INTEREST";
    private final String PENALTY = "PENALTY";
    private final String REPAYMENT_SCHEDULE_NO = "REPAYMENT_SCHEDULE_NO";
    private final String ROI = "ROI";

    /**
     * Creates a new instance of RepaymentCalculator
     */
    public RepaymentCalculator(HashMap map) throws Exception {
        this.dataMap = map;

        String act_num = CommonUtil.convertObjToStr(dataMap.get(TransactionDAOConstants.ACCT_NO));

        String repayment_Schedule_No = "";
        if (!dataMap.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(dataMap.get(REPAYMENT_SCHEDULE_NO));
        }

        dataMap.put(TransactionDAOConstants.ACCT_NO, act_num);
        dataMap.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);
        System.out.println("####RepaymentCalculator map" + map);
        _branchCode = CommonUtil.convertObjToStr(map.get(TransactionDAOConstants.BRANCH_CODE));
        System.out.println("####RepaymentCalculator _branchCode" + _branchCode);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap calculateRepaymentInterestAmt() throws Exception {
        HashMap resultMap = new HashMap();
        HashMap interestResultMap = new HashMap();
        HashMap penaltIntResultMap = new HashMap();
        HashMap temp = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap data = new HashMap();
        HashMap interestMap = new HashMap();
        HashMap prodLevelWhereMap;
        HashMap prodLevelValues;
        HashMap penulIntDetails;
        HashMap nextPenulIntDet = null;
        double interestAmount = 0.0;
        double penulIntAmount = 0.0;
        double principalAmount = CommonUtil.convertObjToDouble(dataMap.get(TransactionDAOConstants.AMT)).doubleValue();
        double remOverDueAmount = 0.0;
        double overDueAmount = 0.0;
        Date paymentDate = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("##### calculateRepaymentInterestAmt paymentDate" + paymentDate);
        java.sql.Timestamp tsPaymentDate = new java.sql.Timestamp(paymentDate.getYear(), paymentDate.getMonth(), paymentDate.getDate(), paymentDate.getHours(), paymentDate.getMinutes(), paymentDate.getSeconds(), 0);
        System.out.println("##### calculateRepaymentInterestAmt paymentDate" + paymentDate);

        whereMap.put("ACCT_NUM", dataMap.get(TransactionDAOConstants.ACCT_NO));

        prodLevelWhereMap = getLoanRepayedDetails(whereMap);
        prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        System.out.println("getprod_id#####" + prodLevelWhereMap);
        whereMap.put("PROD_ID", prodLevelWhereMap.get("PROD_ID"));
        whereMap.put("CATEGORY_ID", prodLevelWhereMap.get("CATEGORY"));
        whereMap.put("AMOUNT", prodLevelWhereMap.get("LIMIT"));
        whereMap.put("FROM_DATE", prodLevelWhereMap.get("FROM_DT"));
        whereMap.put("TO_DATE", tsPaymentDate);

        data.put("PRINCIPAL", prodLevelWhereMap.get("LOAN_BALANCE_PRINCIPAL"));

        interestList = getInterestDetails(whereMap, CommonUtil.convertObjToStr(prodLevelWhereMap.get("INT_GET_FROM")));

        LoanCalculateInterest interestCalculator = new LoanCalculateInterest();
        // Get the Actual interest details
        HashMap map = setInterestCalculationBeanValues(whereMap, prodLevelWhereMap, prodLevelValues, paymentDate, ROI);

        // Calculate the Actual Interest
        System.out.println("loaninterestcalculation" + map);
        interestResultMap = interestCalculator.getInterest(map);
        interestAmount = CommonUtil.convertObjToDouble(interestResultMap.get(INTEREST)).doubleValue();
        principalAmount -= interestAmount;
        System.out.println("principal      :" + principalAmount);
        System.out.println("@@####interestresultmap" + interestResultMap);
        resultMap.put("INTEREST_DETAILS", interestResultMap);

        // Get the overdue Amount(Overdue amount is negative)
        overDueAmount = CommonUtil.convertObjToDouble(prodLevelWhereMap.get("EXCESS_AMOUNT")).doubleValue();
        prodLevelWhereMap.put("ACT_NUM", whereMap.get("ACCT_NUM"));
        List overDueList = getOverDueDates(prodLevelWhereMap, tsPaymentDate);
        //List overDueList = getOverDueDates((java.sql.Timestamp) prodLevelWhereMap.get("LAST_REPAY_DT"), tsPaymentDate);
        System.out.println("####@@@overduelist" + overDueList);
        remOverDueAmount = overDueAmount + principalAmount;
        if (overDueList.size() > 0) {
            int overDueListSize = overDueList.size();
            double overDuePrincipalVal = (-1) * remOverDueAmount;
            // Get the Penalty interest details
            for (int i = overDueListSize - 1, j = 0; i > 0; --i, ++j) {
                penulIntDetails = (HashMap) overDueList.get(j);
                if (j < overDueListSize - 1) {
                    nextPenulIntDet = (HashMap) overDueList.get(j + 1);
                }
                if (overDueListSize == 1) {
                    // Values have to set for the first record
                    prodLevelWhereMap.put("LOAN_BALANCE_PRINCIPAL", new Double(overDuePrincipalVal));
                    paymentDate = (Date) penulIntDetails.get("INSTALLMENT_DT");

                    map = setInterestCalculationBeanValues(whereMap, prodLevelWhereMap, prodLevelValues, paymentDate, PENALTY);
                    System.out.println("checkpenalmap1###" + map);
                    // Penalty Interest calculation is done here
                    temp = interestCalculator.getInterest(map);
                    System.out.println("tempcheck" + temp);
                    if (CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue() > 0) {
                        penulIntAmount += CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue();
                        penaltIntResultMap.put(String.valueOf(penaltIntResultMap.size() + 1), temp);
                    }
                    overDuePrincipalVal += CommonUtil.convertObjToDouble(penulIntDetails.get("PRINCIPAL_AMT")).doubleValue();
                    prodLevelWhereMap.put("LOAN_BALANCE_PRINCIPAL", new Double(overDuePrincipalVal));
                    prodLevelWhereMap.put("LAST_REPAY_DT", penulIntDetails.get("INSTALLMENT_DT"));
                    paymentDate = ServerUtil.getCurrentDate(_branchCode);

                    map = setInterestCalculationBeanValues(whereMap, prodLevelWhereMap, prodLevelValues, paymentDate, PENALTY);
                    System.out.println("checkpenalmap2###" + map);
                    // Penalty Interest calculation is done here
                    temp = interestCalculator.getInterest(map);
                    System.out.println("tempcheck2" + temp);
                    if (CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue() > 0) {
                        penulIntAmount += CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue();
                        penaltIntResultMap.put(String.valueOf(penaltIntResultMap.size() + 1), temp);
                    }
                } else if (j == 0) {
                    // Values have to set for the first record
                    prodLevelWhereMap.put("LOAN_BALANCE_PRINCIPAL", new Double(overDuePrincipalVal));
                    paymentDate = (Date) penulIntDetails.get("INSTALLMENT_DT");
                } else if (j == (overDueListSize - 1)) {
                    prodLevelWhereMap.put("LOAN_BALANCE_PRINCIPAL", new Double(overDuePrincipalVal));
                    prodLevelWhereMap.put("LAST_REPAY_DT", penulIntDetails.get("INSTALLMENT_DT"));
                    paymentDate = ServerUtil.getCurrentDate(_branchCode);
                }

                if (overDueListSize != 1) {
                    map = setInterestCalculationBeanValues(whereMap, prodLevelWhereMap, prodLevelValues, paymentDate, PENALTY);
                    if (j < (overDueListSize - 1)) {
                        overDuePrincipalVal += CommonUtil.convertObjToDouble(nextPenulIntDet.get("PRINCIPAL_AMT")).doubleValue();
                    }
                    // Penalty Interest calculation is done here
                    temp = interestCalculator.getInterest(map);
                    System.out.println("tempcheck3" + temp);
                    if (CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue() > 0) {
                        penulIntAmount += CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue();
                        penaltIntResultMap.put(String.valueOf(penaltIntResultMap.size() + 1), temp);
                    }
                }
            }
            resultMap.put("PENAL_INTEREST_DETAILS", penaltIntResultMap);
        } else if (remOverDueAmount < 0) {
            // If there is any overdue amount
            prodLevelWhereMap.put("LOAN_BALANCE_PRINCIPAL", new Double((-1) * remOverDueAmount));
            map = setInterestCalculationBeanValues(whereMap, prodLevelWhereMap, prodLevelValues, paymentDate, PENALTY);
            System.out.println("checkpenalmap4###" + map);
            // Penalty Interest calculation is done here
            temp = interestCalculator.getInterest(map);
            System.out.println("tempcheck4" + temp);
            if (CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue() > 0) {
                penulIntAmount += CommonUtil.convertObjToDouble(temp.get(INTEREST)).doubleValue();
                penaltIntResultMap.put(String.valueOf(penaltIntResultMap.size() + 1), temp);
            }

            resultMap.put("PENAL_INTEREST_DETAILS", penaltIntResultMap);
        }
        if (penulIntAmount > 0.0) {
            principalAmount -= penulIntAmount;
        }

        // Update the interest Details in Database(LOANS_REPAYMENT & LOANS_REPAYMENT_MULTIRATE)
        System.out.println("#####finalprincipal" + principalAmount + "@@@finalint" + interestAmount + "penalAmount" + penulIntAmount);
        updateInterestDetails(resultMap, principalAmount, interestAmount, penulIntAmount);

        resultMap = null;
        resultMap = new HashMap();
        resultMap = getInterestAndPenalIntActHead(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")), resultMap);

        resultMap.put(TransactionDAOConstants.ACCT_NO, dataMap.get(TransactionDAOConstants.ACCT_NO));
        resultMap.put("PRINCIPAL_AMOUNT", new Double(principalAmount));
        resultMap.put("INTEREST_AMOUNT", new Double(interestAmount));
        resultMap.put("PENUL_INTEREST_AMOUNT", new Double(penulIntAmount));

        // Update the Balance principal amound, interest amount, penalty interest amount
        // in LOANS_FACILITY_DETAILS
        updatePrincipalInterest(resultMap);

        prodLevelWhereMap = null;
        penulIntDetails = null;
        prodLevelValues = null;
        tsPaymentDate = null;
        paymentDate = null;
        temp = null;
        map = null;
        data = null;
        whereMap = null;

        return resultMap;
    }

    private HashMap setInterestCalculationBeanValues(HashMap whereMap, HashMap prodLevelWhereMap, HashMap prodLevelValues, Date paymentDate, String interestType) throws Exception {
        HashMap map = new HashMap();
        HashMap repayMap = new HashMap();
        InterestCalculationBean interestBean = new InterestCalculationBean();
        interestBean.setCompoundingPeriod("365");
        interestBean.setPrincipalAmt(CommonUtil.convertObjToStr(prodLevelWhereMap.get("LOAN_BALANCE_PRINCIPAL")));
        interestBean.setCompoundingType("REPAYMENT");
        interestBean.setInterestType("COMPOUND");
        interestBean.setIsDuration_ddmmyy(false);
        interestBean.setDuration_FromDate(CommonUtil.convertObjToStr(prodLevelWhereMap.get("LAST_REPAY_DT")));
        interestBean.setDuration_ToDate(DateUtil.getStringDate(paymentDate));
        interestBean.setFloatPrecision("2");
        if (prodLevelValues.containsKey("YEAR_OPTION")) {
            interestBean.setYearOption(CommonUtil.convertObjToStr(prodLevelValues.get("YEAR_OPTION")));
        } else {
            interestBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("MONTH_OPTION")) {
            interestBean.setMonthOption(CommonUtil.convertObjToStr(prodLevelValues.get("MONTH_OPTION")));
        } else {
            interestBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("ROUNDING_TYPE")) {
            interestBean.setRoundingType(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_TYPE")));
        } else {
            interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
        }
        if (prodLevelValues.containsKey("ROUNDING_FACTOR")) {
            interestBean.setRoundingFactor(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_FACTOR")));
        } else {
            interestBean.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_1_RUPEE);
        }
        repayMap.put("FROM_DATE", DateUtil.getStringDate((Date) prodLevelWhereMap.get("LAST_REPAY_DT")));
        repayMap.put("TO_DATE", DateUtil.getStringDate(paymentDate));
        ArrayList variousInterestList = null;
        if (interestType.equals(ROI)) {
            variousInterestList = getInterestList(interestList, interestType);
        } else if (interestType.equals(PENALTY)) {
            variousInterestList = getPenulInterestList(DateUtil.getStringDate((Date) prodLevelWhereMap.get("LAST_REPAY_DT")), DateUtil.getStringDate(paymentDate));
        }

        interestBean.setRateOfInterest(CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST)));
        repayMap.put("VARIOUS_INTEREST_RATE", variousInterestList);
        repayMap.put("REPAYMENT_TYPE", "REPAYMENT");
        map.put(CommonConstants.DATA, interestBean);
        map.put("REPAYMENT_DETAILS", repayMap);

        repayMap = null;
        interestBean = null;
        return map;
    }

    private HashMap getLoanRepayedDetails(HashMap whereMap) throws Exception {
        HashMap prodLevelWhereMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getLoanRepayedDetails", whereMap);
        prodLevelWhereMap = (HashMap) list.get(0);
        if (prodLevelWhereMap.get("LAST_REPAY_DT") == null) {
            Date today = ServerUtil.getCurrentDate(_branchCode);
            prodLevelWhereMap.put("LAST_REPAY_DT", new java.sql.Timestamp(today.getYear(), today.getMonth(), today.getDate(), today.getHours(), today.getMinutes(), today.getSeconds(), 0));
            today = null;
        }
        list = null;
        return prodLevelWhereMap;
    }

    private List getInterestDetails(HashMap whereMap, String intToGetFrom) throws Exception {
        List list = null;
        if (intToGetFrom.equals("PROD")) {
            // Add code for making interest periodwise : Added by nithya on 30-10-2017 for 7867
            List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", whereMap);
            if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
                HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
                if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
                    if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
                        whereMap.put("PERIOD_WISE_INT_RATE", "PERIOD_WISE_INT_RATE");
                        whereMap.put("INT_UP_TO_DATE", ServerUtil.getCurrentDate(_branchCode));
                    } else {
                        whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                    }
                }else{
                    whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                }
            }else{
                whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
            }
            list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestMap", whereMap);
            System.out.println("prodListrepaymentcalculator" + list);
        } else if (intToGetFrom.equals("ACT")) {
            list = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanInterestMap", whereMap);
        }

        if (list == null || list.size() == 0) {
            throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
        }
        return list;
    }

    private ArrayList getInterestList(List list, String interestType) throws Exception {
        HashMap temp;
        HashMap interestMap;
        System.out.println("getInterestList" + list + "interestType" + interestType);
        ArrayList various_interestList = new ArrayList();
        for (int i = list.size() - 1, j = 0; i >= 0; --i, ++j) {
            temp = (HashMap) list.get(j);
            interestMap = new HashMap();
            if (interestType.equals(ROI)) {
                interestMap.put(INTEREST, temp.get(INTEREST));
            } else if (interestType.equals(PENALTY)) {
                interestMap.put(INTEREST, temp.get("PENAL_INTEREST"));
            }
            interestMap.put("FROM_DATE", DateUtil.getStringDate((Date) temp.get("FROM_DT")));
            various_interestList.add(interestMap);
            interestMap = null;
            temp = null;
        }
        temp = null;
        return various_interestList;
    }

    private HashMap getProductLevelValues(String strProdID) throws Exception {
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        transactionMap.put("PROD_ID", strProdID);
        List resultList = (List) sqlMap.executeQueryForList("getCompFreqRoundOff_LoanProd", transactionMap);
        if (resultList.size() > 0) {
            // If Product Account Head exist in Database
            retrieve = (HashMap) resultList.get(0);
        }
        transactionMap = null;
        resultList = null;
        return retrieve;
    }

    private List getOverDueDates(HashMap hash, java.sql.Timestamp paymentDate) throws Exception {
        List list = null;
        //java.sql.Timestamp lastRepayDate;
        HashMap transactionMap = new HashMap();

        transactionMap.put("FROM_DATE", hash.get("LAST_REPAY_DT"));
        transactionMap.put("TO_DATE", paymentDate);
        transactionMap.put("ACT_NUM", hash.get("ACT_NUM"));
        list = (List) sqlMap.executeQueryForList("getOverDueDetails", transactionMap);
        return list;
    }

    public ArrayList getPenulInterestList(String fromDate, String toDate) throws Exception {
        ArrayList returnValue = new ArrayList();

        Date loanToDate = ServerUtil.getCurrentDate(_branchCode);
        HashMap eachRecs;
        HashMap interestRateAndPeriod;
        LinkedHashMap interestAll = new LinkedHashMap();
        int intSize = interestList.size();
        for (int i = intSize - 1, j = 0; i >= 0; --i, ++j) {
            interestAll.put(String.valueOf(j), interestList.get(j));
        }

        java.util.Set keySet = interestAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        Date repayFromDate = DateUtil.getDateMMDDYYYY(fromDate);
        Date repayToDate = DateUtil.getDateMMDDYYYY(toDate);
        Date interestFromDate;
        Date interestToDate;
        boolean gotFinalIntRate = false;
        for (int i = keySet.size() - 1, j = 0; i >= 0; --i, ++j) {
            interestRateAndPeriod = new HashMap();
            eachRecs = (HashMap) interestAll.get(objKeySet[j]);
            interestFromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get("FROM_DT")));

            if ((eachRecs.get("TO_DT") == null) || (CommonUtil.convertObjToStr(eachRecs.get("TO_DT")).length() == 0)) {
                interestToDate = loanToDate;
            } else {
                interestToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachRecs.get("TO_DT")));
            }

            if (repayFromDate.compareTo(interestFromDate) >= 0 && interestToDate.compareTo(repayFromDate) > 0) {
                interestRateAndPeriod.put(INTEREST, eachRecs.get("PENAL_INTEREST"));
                interestRateAndPeriod.put("FROM_DATE", DateUtil.getStringDate(repayFromDate));
            } else if ((interestFromDate.compareTo(repayFromDate) >= 0 && interestToDate.compareTo(repayToDate) <= 0) || (repayToDate.compareTo(interestFromDate) >= 0 && repayToDate.compareTo(interestToDate) <= 0)) {
                interestRateAndPeriod.put(INTEREST, eachRecs.get("PENAL_INTEREST"));
                interestRateAndPeriod.put("FROM_DATE", eachRecs.get("FROM_DT"));
            } else {
                interestRateAndPeriod = null;
            }

            if (interestRateAndPeriod != null) {
                returnValue.add(interestRateAndPeriod);
            }
            interestRateAndPeriod = null;
            interestFromDate = null;
            interestToDate = null;
            eachRecs = null;
        }
        eachRecs = null;
        keySet = null;
        objKeySet = null;
        interestAll = null;

        return returnValue;
    }

    private void updateInterestDetails(HashMap intDetailsMap, double principalAmt, double intAmt, double penalAmt) throws Exception {
        HashMap interestMap;
        HashMap penalIntMap;
        ArrayList interestRateList = new ArrayList();
        ArrayList penalIntRateList = new ArrayList();
        java.util.Set keySet;
        Object[] objKeySet;
        double interestRate = 0.0;
        double penalIntRate = 0.0;
        int interestRateListSize = 0;
        int penalIntRateListSize = 0;
        System.out.println("###intDetailMap" + intDetailsMap + "principleamt" + principalAmt + "int" + intAmt + "penalonly" + penalAmt);
        if (intDetailsMap.containsKey("INTEREST_DETAILS")) {
            interestMap = (HashMap) ((HashMap) intDetailsMap.get("INTEREST_DETAILS")).get(ALL_RECORDS);
            keySet = interestMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            interestMap = (HashMap) interestMap.get(objKeySet[0]);

            keySet = null;
            objKeySet = null;
            keySet = interestMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            if (keySet.size() > 0) {
                interestRateList = (ArrayList) interestMap.get(objKeySet[0]);
            }
            interestRateListSize = interestRateList.size();
            if (interestRateListSize > 0) {
                interestRate = CommonUtil.convertObjToDouble(((HashMap) (interestRateList.get(interestRateList.size() - 1))).get("INTEREST_RATE")).doubleValue();
            }
        }

        // To get the Penalty Interest Details(From Date, To Date, Rate of Interest)
        if (intDetailsMap.containsKey("PENAL_INTEREST_DETAILS")) {
            keySet = null;
            objKeySet = null;
            HashMap multiIntPenalMap;
            java.util.Set multiIntKeySet;
            Object[] objMultiIntKeySet;
            penalIntMap = (HashMap) intDetailsMap.get("PENAL_INTEREST_DETAILS");

            keySet = penalIntMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // Multiple Penal Interest Rate will be there
            for (int i = keySet.size() - 1, j = 0; i >= 0; --i, ++j) {
                multiIntPenalMap = (HashMap) penalIntMap.get(objKeySet[j]);
                // Get the Values by using the Key ALL_RECORDS
                multiIntPenalMap = (HashMap) multiIntPenalMap.get(ALL_RECORDS);

                multiIntKeySet = multiIntPenalMap.keySet();
                objMultiIntKeySet = (Object[]) multiIntKeySet.toArray();
                // In that ALL_RECORDS there is a HashMap with the Key as multiIntPenalMap Size
                if (multiIntKeySet.size() > 0) {
                    multiIntPenalMap = (HashMap) multiIntPenalMap.get(objMultiIntKeySet[0]);

                    multiIntKeySet = multiIntPenalMap.keySet();
                    objMultiIntKeySet = (Object[]) multiIntKeySet.toArray();
                }
                // There is a HashMap within the ArrayList and that HashMap contains From date, To date and RoI
                if (multiIntKeySet.size() > 0) {
                    penalIntRateList = (ArrayList) multiIntPenalMap.get(objMultiIntKeySet[0]);
                }
                penalIntRateListSize = penalIntRateList.size();
                if (penalIntRateListSize > 0) {
                    penalIntRate = CommonUtil.convertObjToDouble(((HashMap) (penalIntRateList.get(penalIntRateList.size() - 1))).get("INTEREST_RATE")).doubleValue();
                }

                // Insert the Penalty Interest Details in LOANS_REPAYMENT_MULTIRATE
                for (int k = penalIntRateListSize - 1, m = 0; k >= 0; --k, ++m) {
                    sqlMap.executeUpdate("insertRepaymentMultiInterest", getRepaymentMultiInterestDetails((HashMap) penalIntRateList.get(m), PENALTY));
                }

                multiIntKeySet = null;
                objMultiIntKeySet = null;
            }
        }

        // Insert LOANS_REPAYMENT
        sqlMap.executeUpdate("insertRepaymentInterest", getRepaymentInterestDetails(principalAmt, intAmt, interestRate, penalAmt, penalIntRate));

        // Update the PAID_STATUS in LOANS_INSTALLMENT based on Principal Amount
        updateLoanInstallmentStatus(principalAmt);

        // Insert LOANS_REPAYMENT_MULTIRATE
        for (int i = interestRateListSize - 1, j = 0; i >= 0; --i, ++j) {
            sqlMap.executeUpdate("insertRepaymentMultiInterest", getRepaymentMultiInterestDetails((HashMap) interestRateList.get(j), ROI));
        }

        keySet = null;
        objKeySet = null;
        interestMap = null;
        penalIntMap = null;
        interestList = null;
        penalIntRateList = null;
    }

    private void updateLoanInstallmentStatus(double principalAmt) throws Exception {
        HashMap transactionMap = new HashMap();
        HashMap resultMap = null;
        List list = null;
        double overDueAmt = 0.0;
        double paidPrincipalAmt = 0.0;
        double repaidAmt = 0.0;

        transactionMap.put(TransactionDAOConstants.ACCT_NO, dataMap.get(TransactionDAOConstants.ACCT_NO));

        list = (List) sqlMap.executeQueryForList("getLoanOverDueAmountTL", transactionMap);
        if (list.size() > 0) {
            resultMap = (HashMap) list.get(0);
            overDueAmt = CommonUtil.convertObjToDouble(resultMap.get("OVERDUE_AMOUNT")).doubleValue();
        }

        list = (List) sqlMap.executeQueryForList("getLOANS_INSTALLMENT_PaidPrincipalAmtTL", transactionMap);
        if (list.size() > 0) {
            resultMap = (HashMap) list.get(0);
            paidPrincipalAmt = CommonUtil.convertObjToDouble(resultMap.get("PAID_PRIN_AMOUNT")).doubleValue();
        }


        list = (List) sqlMap.executeQueryForList("getLoansRepaymentAmtTL", transactionMap);
        if (list.size() > 0) {
            resultMap = (HashMap) list.get(0);
            repaidAmt = CommonUtil.convertObjToDouble(resultMap.get("REPAID_AMOUNT")).doubleValue();
        }

    }

    private HashMap getRepaymentInterestDetails(double principalAmt, double intAmt, double intRate, double penalAmt, double penalIntRate) throws Exception {
        HashMap interestDetails = new HashMap();

        interestDetails.put("ACCT_NUM", dataMap.get(TransactionDAOConstants.ACCT_NO));
        interestDetails.put("TRANS_ID", dataMap.get(TransactionDAOConstants.TRANS_ID));
        interestDetails.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
        interestDetails.put("PRINCIPAL_AMT", new Double(principalAmt));
        interestDetails.put("INTEREST_AMT", new Double(intAmt));
        interestDetails.put("PENAL_INT_AMT", new Double(penalAmt));
        interestDetails.put("STATUS", CommonConstants.STATUS_CREATED);
        interestDetails.put("STATUS_BY", "");
        interestDetails.put("STATUS_DT", ServerUtil.getCurrentDate(_branchCode));
        interestDetails.put("AUTHORIZE_STATUS", dataMap.get(TransactionDAOConstants.AUTHORIZE_STATUS));
        interestDetails.put("AUTHORIZE_BY", dataMap.get(TransactionDAOConstants.AUTHORIZE_BY));
        interestDetails.put("AUTHORIZE_DT", ServerUtil.getCurrentDate(_branchCode));
        interestDetails.put("INTEREST_RATE", new Double(intRate));
        interestDetails.put("PENAL_INT_RATE", new Double(penalIntRate));

        List list = null;
        HashMap transactionMap = new HashMap();
        HashMap resultMap = null;
        transactionMap.put(TransactionDAOConstants.ACCT_NO, dataMap.get(TransactionDAOConstants.ACCT_NO));

        list = (List) sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", transactionMap);
        if (list.size() > 0) {
            resultMap = (HashMap) list.get(0);
        }

        double balanceAmt = 0.0;
        if (resultMap != null) {
            balanceAmt = CommonUtil.convertObjToDouble(resultMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
            balanceAmt -= principalAmt;
        }
        interestDetails.put("BALANCE_AMT", new Double(balanceAmt));

        list = null;
        transactionMap = null;
        resultMap = null;
        resultMap = null;

        return interestDetails;
    }

    private HashMap getRepaymentMultiInterestDetails(HashMap multiIntDetailsMap, String strInterestType) throws Exception {
        HashMap multiInterestDetails = new HashMap();

        multiInterestDetails.put("ACCT_NUM", dataMap.get(TransactionDAOConstants.ACCT_NO));
        multiInterestDetails.put("TRANS_ID", dataMap.get(TransactionDAOConstants.TRANS_ID));
        multiInterestDetails.put("INT_TYPE", strInterestType);
        multiInterestDetails.put("INTEREST_RATE", multiIntDetailsMap.get("INTEREST_RATE"));
//        multiInterestDetails.put("FROM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntDetailsMap.get("FROM_DATE"))));
//        multiInterestDetails.put("TO_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(multiIntDetailsMap.get("TO_DATE"))));
        multiInterestDetails.put("FROM_DT", (Date) multiIntDetailsMap.get("FROM_DATE"));
        multiInterestDetails.put("TO_DT", (Date) multiIntDetailsMap.get("TO_DATE"));

        return multiInterestDetails;
    }

    private HashMap getInterestAndPenalIntActHead(String prodID, HashMap resultMap) throws Exception {
        HashMap transactionMap = new HashMap();
        transactionMap.put("PROD_ID", prodID);
        List list = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", transactionMap);
        resultMap = (HashMap) list.get(0);
        transactionMap = null;
        list = null;
        return resultMap;
    }

    private void updatePrincipalInterest(HashMap amountMap) throws Exception {
        // Update the Balance principal amound, interest amount,
        // penalty interest amount and Last repayment date
        // in LOANS_FACILITY_DETAILS
        System.out.println("updatePrincipleInterest#######" + amountMap);
        amountMap.put("LAST_REPAY_DT", ServerUtil.getCurrentDate(_branchCode));
        sqlMap.executeUpdate("updatePaidPrincipalInterestTL", amountMap);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        HashMap data = new HashMap();
        data.put("ACCOUNTNO", "LA00000000001063");
        data.put("AMOUNT", new Double(1150.0));
        data.put(TransactionDAOConstants.TRANS_ID, "C0007173");

        new RepaymentCalculator(data).calculateRepaymentInterestAmt();
    }
}
